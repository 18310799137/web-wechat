package com.yucheng.im.service.web.mq.thread.task;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.FriendInfoBean;
import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.entity.vo.req.ReqListVo;
import com.yucheng.im.service.entity.vo.session.QueryUserSessionVo;
import com.yucheng.im.service.manager.service.IUserMsgService;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.dwr.util.MessagePushToClientUtils;
import com.yucheng.im.service.web.mq.thread.HandleMsgThreadPool;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;
public class HandleUserMsgThreadTask extends AbstractThreadTask{
	private static Logger logger = Logger.getLogger(HandleUserMsgThreadTask.class);
	public void notifyThisTask(){
		this.interrupt();
	}

	private IUserMsgService iUserMsgService=(IUserMsgService) context.getBean("userMsgServiceImpl");;
	private void disposeMessage(String jsonMesg) {
		//将Json字符串转化成 UserMessage 
		UserMessage	message=WebConvertObjectUtils.convertJsonStrToObject(jsonMesg, UserMessage.class);
		logger.debug("ThreadName:"+currentThread().getName()+"iUserMsgService:"+iUserMsgService+" - Poll -"+jsonMesg);
		
		if(message!=null) {
			//替换消息内容中的特殊字符
			message.setContent(message.getContent().replaceAll("<script>", "&lt;script&gt;"));
			message.setContent(message.getContent().replaceAll("</script>", "&lt;/script&gt;"));
			
			
			
			message.setUuid(UUIDGenerateUtils.getDBUuid());
			if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
				//将消息保存到数据库
				iUserMsgService.addUserMsg(message);
			}
			
			//从sessionMap缓存中获取 指定id的session 推送到此sessionId对应的客户端
			String toUserId=message.getToUserId();
			String toUserSessionId = jedisCache.get(toUserId);
			logger.debug("推送到此sessionId对应的客户端 toUserSessionId - "+toUserSessionId);
			
			String fromUserId = message.getFromUserId();
			UserInfoBean fromUser = DwrUserSessionManagerUtils.getUserInfoByUserId(fromUserId);
			//获取发送者的姓名
			String fromUserName =	fromUser.getUserName();
			//获取发送者的图像
			String fromUserPhoto = fromUser.getUserPhoto();
			
			
			//如果是通知类消息  //如果是请求类的消息
			if(ServConstants.System.IS_NOTICE_YES.equals(message.getIsNotice()) && ServConstants.System.REQ_MSG_FLAG.equals(message.getmType())) {
					//添加要推送到客户端的sessionId
					if(null!=toUserSessionId) {
						Set<String> set = new HashSet<>();
						set.add(toUserSessionId.split("\\|")[1]);
						ReqListVo listVo = new ReqListVo();
						
						listVo.setReqMsg(message.getContent());
						listVo.setReqUserId(message.getFromUserId());
						listVo.setReqUserName(fromUserName);
						listVo.setUserPhoto(fromUserPhoto);
						MessagePushToClientUtils.methodPushBySessionId(set, "refreshNewUserReqMsgNumber",WebConvertObjectUtils.convertObjectToJsonStr(listVo));
					}else {
						logger.info("本条消息推送的客户端未在线,将不进行消息推送");
					}
			}else{
				
				//
				Map<String, String>params = new HashMap<>();
				if(ServConstants.System.RESP_MSG_FLAG.equals(message.getmType())) {
					
					
					if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
						//说明是同意好友请求类的消息调用客户端添加好友方法 把好友添加到列表中
						UserInfoBean recieveInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(toUserId);
						//接收者的好友列表中添加好友
						FriendInfoBean reqInfoBean = new FriendInfoBean();
						reqInfoBean.setUserId(fromUserId);
						reqInfoBean.setSource("通过搜索添加");
						
						
						//在好友列表中添加 本条申请记录
						jedisConfig.hset(toUserId+WebConstants.Flag.USERFRIENDSFLAG, fromUserId, WebConvertObjectUtils.convertObjectToJsonStr(reqInfoBean));
						//发送者好友列表中添加好友
						FriendInfoBean respInfoBean = new FriendInfoBean();
						respInfoBean.setUserId(recieveInfo.getId());				
						respInfoBean.setSource("通过搜索添加");

						//在好友列表中添加 本条申请记录
						jedisConfig.hset(fromUserId+WebConstants.Flag.USERFRIENDSFLAG, toUserId, WebConvertObjectUtils.convertObjectToJsonStr(respInfoBean));
						
					}
					
					params.put(ServConstants.ConditionUserMsg.DISPOSE_STATUS, ServConstants.System.DISPOSE_STATUS_YES);

					
					if(null!=toUserSessionId) {
						//将消息推送到接收者
						Set<String> set = new HashSet<>();
						set.add(toUserSessionId.split("\\|")[1]);
						
						
						MessagePushToClientUtils.methodPushBySessionId(set, "addUserToFriendList",fromUserId);
					}else {
						logger.info("本条消息推送的客户端未在线,将不进行消息推送");
					}
					
				}
			
				params.put(ServConstants.ConditionUserMsg.IS_UNREAD, ServConstants.System.IS_UNREAD_NO);
				params.put(ServConstants.ConditionUserMsg.TO_USER_ID, fromUserId);
				params.put(ServConstants.ConditionUserMsg.FROM_USER_ID, toUserId);
				
			
				
				iUserMsgService.modifyUserMsgStatus(params);
				//说明不是请求好友类消息 ,调用客户端 接收聊天消息方法
				 QueryUserSessionVo sessionVo = new QueryUserSessionVo();
				 sessionVo.setContent(message.getContent());
				 sessionVo.setSessionFlag(WebConstants.SESSION.SESSION_USER);
				 sessionVo.setUnReadMsgNum(1);
				 sessionVo.setUserId(fromUser.getId());
				 sessionVo.setUserName(fromUserName);
				 sessionVo.setUserPhoto(fromUserPhoto);
				 sessionVo.setSendTime(message.getSendTime());
					if(DwrBaseService.currentUserMap.containsKey(message.getFromUserId())) {
						iUserMsgService.modifyUserMsgStatus(params);
						 addUserChatToSessionList(toUserId, fromUserId,WebConstants.SESSION.SESSION_USER);
						 addUserChatToSessionList(fromUserId, toUserId,WebConstants.SESSION.SESSION_USER);
							
					}
				
					//判断是否是好友关系
					String friendJson = jedisConfig.hget(fromUserId+WebConstants.Flag.USERFRIENDSFLAG, toUserId);
					if(null==friendJson || "".equals(friendJson)) {
						sessionVo.setIsFriend(WebConstants.Friend.IS_FRIEND_NO);
					}else {
						sessionVo.setIsFriend(WebConstants.Friend.IS_FRIEND_YES);
					}
				 if(null!=toUserSessionId) {
					//将消息推送到接收者
						Set<String> set = new HashSet<>();
						set.add(toUserSessionId.split("\\|")[1]);
						MessagePushToClientUtils.methodPushBySessionId(set, "servPushToClientUserChatMsg",WebConvertObjectUtils.convertObjectToJsonStr(sessionVo));
				 }else {
						logger.info("本条消息推送的客户端未在线,将不进行消息推送");
				 }
				}
		}
	}
	/**
	 * 处理用户类消息线程任务
	 */
	public void run() {
		logger.info("ThreadName:"+currentThread().getName()+" - Start Working");
		int sum=0;
		while(true){
			int count=0;
			long start = System.currentTimeMillis();
			String jsonMesg=null;
			while((jsonMesg	=	HandleMsgThreadPool.userMessageQueue.poll())!= null){
				count++;
				sum++;
				disposeMessage(jsonMesg);
			}
			long end =	System.currentTimeMillis();
			logger.info(currentThread().getName()+"累计消费总数"+sum+"条,本次消费总数为:"+count+"条\t共计用时"+(end-start)+"毫秒");
			try {
				HandleMsgThreadPool.recycleToUserThreadPool(this);
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				logger.info(currentThread().getName()+"线程被唤醒 开始执行任务");
			}
		}
	}
}
