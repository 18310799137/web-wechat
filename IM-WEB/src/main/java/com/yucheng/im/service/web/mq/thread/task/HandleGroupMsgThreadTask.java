package com.yucheng.im.service.web.mq.thread.task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.GroupInfoBean;
import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.entity.vo.group.PushToClienModifyGroupNameVo;
import com.yucheng.im.service.entity.vo.session.QueryGroupSessionVo;
import com.yucheng.im.service.manager.service.IGroupMemMsgService;
import com.yucheng.im.service.manager.service.IUserGroupMsgService;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.dwr.util.MessagePushToClientUtils;
import com.yucheng.im.service.web.mq.thread.HandleMsgThreadPool;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;
public class HandleGroupMsgThreadTask extends AbstractThreadTask{
	private static Logger logger = Logger.getLogger(HandleGroupMsgThreadTask.class);
	public void notifyThisTask(){
		this.interrupt();
	}
	private IUserGroupMsgService groupMsgService= (IUserGroupMsgService) context.getBean("userGroupMsgServiceImpl");
	private IGroupMemMsgService groupMemMsgService= (IGroupMemMsgService) context.getBean("groupMemMsgServiceImpl");
	
	/**
	 * 
	 * 
	 * @Description:推送群组类的消息到客户端
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月12日下午3:55:25
	 * @version V1.0
	 * @param jsonMesg
	 */
	private void pushMessageToClient(String jsonMesg) {
		GroupMessage message=null;
		message= WebConvertObjectUtils.convertJsonStrToObject(jsonMesg, GroupMessage.class);
		if(null!=message) {
			
			//替换消息内容中的特殊字符
			message.setContent(message.getContent().replaceAll("<script>", "&lt;script&gt;"));
			message.setContent(message.getContent().replaceAll("</script>", "&lt;/script&gt;"));
			
			message.setUuid(UUIDGenerateUtils.getDBUuid());
			//获取登录用户 即发送者的信息
			UserInfoBean userInfoBean = DwrUserSessionManagerUtils.getUserInfoByUserId(message.getFromUserId());
			//发送者的姓名
			String loginUserName=userInfoBean.getUserName();
			//发送者的id
			String fromUserId=userInfoBean.getId();
			//获取要发送消息到达的群组id
			String groupId = message.getToGroupId();
			
			
			//针对分布式应用的 保存措施，人员在本系统登录 则由本系统负责保存消息
			if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
				//除了修改群信息类型的消息 不保存到数据库 其余到要保存
				if(ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice()) && ServConstants.System.OPR_TYPE_MODIFY.equals(message.getOprType())) {
					//修改了群名称
				}else {
					//将消息保存到数据库
					groupMsgService.addUserGroupMsg(message);
				}
				if(!ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice()) && ServConstants.System.OPR_TYPE_EXIT.equals(message.getOprType())) {
					addUserChatToSessionList(fromUserId, groupId, WebConstants.SESSION.SESSION_GROUP);
				}
				
				//将发送人 未读群组消息置为0_(发消息时 全部已读)
				Map<String,String> params = new HashMap<>();
				params.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
				params.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, fromUserId);
				params.put(ServConstants.ConditionUserMemberMsg.UNREAD_NUM, "0");
				groupMemMsgService.modifyGroupMemMsgUnreadCount(params);
			}
			//获取发送内容 
			String content = message.getContent();

			//获取群组下的群成员列表
			Set<String> memberIds = jedisConfig.hkeys(groupId);
			Iterator<String>iterator =	memberIds.iterator();
			
			//创建用来存储群组图像的image集合
			List<String>userPhotos = new ArrayList<>();
			int imageSize=0;
			
			//创建要推送到客户端的sessionId列表
			Set<String>sessionSet = new HashSet<>();
			
			while(iterator.hasNext()){
				//获取群成员id
				String memberId = iterator.next(); 

				if(imageSize++<=9) {
					//获取群成员照片
					String memberPhoto = DwrUserSessionManagerUtils.getUserInfoByUserId(memberId).getUserPhoto();
					userPhotos.add(memberPhoto);
				}
				//针对分布式应用的 保存措施，人员在本系统登录 则由本系统负责保存消息
				if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
					if(ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice())&&ServConstants.System.OPR_TYPE_CREATE.equals(message.getOprType())) {
						//TODO 判断是否已经建立了新纪录
							Map<String, String> msgCountParams = new HashMap<String, String>();
						msgCountParams.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
						msgCountParams.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, memberId);
						int unReadMsgNum = groupMemMsgService.queryUnreadMsgCount(msgCountParams);
						if(-1==unReadMsgNum) {
							Map<String,String> params = new HashMap<>();
							params.put(ServConstants.ConditionUserMemberMsg.UUID, UUIDGenerateUtils.getDBUuid());
							params.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
							params.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, memberId);
							params.put(ServConstants.ConditionUserMemberMsg.UNREAD_NUM, "1");
							groupMemMsgService.insertGroupMemMsgUnreadCount(params);
						}
							
						
					}
				}
				//如果是非系统消息 或者 系统消息类的修改类消息则不对发送人进行推送,只有群主可以修改群昵称
				if((fromUserId.equals(memberId) && ServConstants.System.IS_NOTICE_NO.equals(message.getIsNotice())) || (fromUserId.equals(memberId)&&ServConstants.System.OPR_TYPE_MODIFY.equals(message.getOprType()))) {
					continue;
				}
				
				//针对分布式应用的 保存措施，人员在本系统登录 则由本系统负责保存消息
				if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
					if(ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice())&&ServConstants.System.OPR_TYPE_CREATE.equals(message.getOprType())) {
					}else {
						//对非发送者添加群聊未读消息数
						Map<String,String> params = new HashMap<>();
						params.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
						params.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, memberId);
						int unreadNum = groupMemMsgService.queryUnreadMsgCount(params);
						unreadNum++;
						params.put(ServConstants.ConditionUserMemberMsg.UNREAD_NUM, unreadNum+"");
						groupMemMsgService.modifyGroupMemMsgUnreadCount(params);
					}
					addUserChatToSessionList(memberId, groupId, WebConstants.SESSION.SESSION_GROUP);
				}
			
				
				//获取群成员的sessionId   使用dwr推送到客户端
				String memSessionId =jedisCache.get(memberId);
				if(null!=memSessionId) {
					//session在缓存中存储为<k-v>结构      <userId - ip|sessionId>,如果不为NULL说明客户端在线
					sessionSet.add(memSessionId.split("\\|")[1]);
				}
				
			}
			
			if(!sessionSet.isEmpty()) {
				if(ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice()) && ServConstants.System.OPR_TYPE_MODIFY.equals(message.getOprType())) {
					//将修改的群名称推送到其余客户端
					PushToClienModifyGroupNameVo toClienModifyGroupNameVo = new PushToClienModifyGroupNameVo();
					toClienModifyGroupNameVo.setGroupId(groupId);
					toClienModifyGroupNameVo.setGroupName(message.getContent());
					MessagePushToClientUtils.methodPushBySessionId(sessionSet, "servPushToClientModifyGroupMsg", WebConvertObjectUtils.convertObjectToJsonStr(toClienModifyGroupNameVo));
				}else {
					//组装要推送到客户端的VO对象
					QueryGroupSessionVo sessionVo = new QueryGroupSessionVo();
					sessionVo.setContent(content);
				
					//获取所在群的名称
					GroupInfoBean groupInfoBean = WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(groupId+WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
					sessionVo.setDefaultName(groupInfoBean.getDefaultName());
					sessionVo.setLastChatName(loginUserName);
					sessionVo.setSessionFlag(WebConstants.SESSION.SESSION_GROUP);
					sessionVo.setToChatGroupId(groupId);
					sessionVo.setToChatGroupName(groupInfoBean.getIndexName());
					sessionVo.setUserPhotos(userPhotos);
					//最后一个人的聊天图像  为发送人的图像
					sessionVo.setLastChatPhotos(userInfoBean.getUserPhoto());
					sessionVo.setSendTime(message.getSendTime());
					sessionVo.setLastChatId(fromUserId);
					MessagePushToClientUtils.methodPushBySessionId(sessionSet, "servPushToClientGroupMsg", WebConvertObjectUtils.convertObjectToJsonStr(sessionVo));
				}
				logger.debug("ThreadName:"+currentThread().getName()+"- Poll -"+message);
			}
		}
	}
	public void run() {
		int sum=0;
		logger.info("ThreadName:"+currentThread().getName()+" - Start Working");
		while(true){
			//计数使用,每个线程消费情况
			int count=0;
			//统计时间使用,记录数据库和推送到客户端
			long start = System.currentTimeMillis();
			String jsonMesg=null;
			while((jsonMesg	=	HandleMsgThreadPool.groupMessageQueue.poll())!= null){
				count++;
				sum++;
				pushMessageToClient(jsonMesg);
		
			}
			long end =	System.currentTimeMillis();
			logger.info(currentThread().getName()+"累计消费总数"+sum+"条,本次消费总数为:"+count+"条\t共计用时"+(end-start)+"毫秒");
			try {
				HandleMsgThreadPool.recycleToGroupThreadPool(this);
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				logger.info(currentThread().getName()+"号线程已被唤醒 开始执行任务");
			}
		}
	}
}
