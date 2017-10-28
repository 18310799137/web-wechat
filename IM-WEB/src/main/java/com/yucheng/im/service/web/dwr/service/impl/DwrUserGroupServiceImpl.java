package com.yucheng.im.service.web.dwr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.GroupInfoBean;
import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.UserInfoForGroup;
import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.entity.vo.chat.group.GroupChatContentRecordVo;
import com.yucheng.im.service.entity.vo.contact.ContactGroupVo;
import com.yucheng.im.service.entity.vo.detail.QueryGroupResultVo;
import com.yucheng.im.service.entity.vo.group.GroupMemberVo;
import com.yucheng.im.service.util.DateUtils;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.service.api.IDwrUserGroupService;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;
import com.yucheng.im.service.web.util.WebStringUtils;

import cn.com.yusys.redis.client.exception.RedisException;
import cn.com.yusys.redis.mq.RedisMQProvider;
import redis.clients.jedis.Jedis;

/**
 * 
 * @Title: DwrUserGroupService.java
 * @Package com.yucheng.im.service.web.dwr
 * @Description: 用户群组管理服务类
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月2日 下午5:40:16
 * @version V1.0
 * 
 */

public class DwrUserGroupServiceImpl extends DwrBaseService implements IDwrUserGroupService {

	private static Logger logger = Logger.getLogger(DwrUserGroupServiceImpl.class);
 

	/**
	 * @Description: 当前用户的群组列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 上午10:09:30
	 * @return ArrayList<GroupInfoBean>
	 * @version V1.0
	 */

	public String queryUserGroupList() {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		String resultJson = null;
		// 获取登录用户信息
		UserInfoBean bean = DwrBaseService.getCurrentUser();
		// 获取用户的群组列表
		Set<String> groupIds = jedisConfig.smembers(bean.getId() + WebConstants.Flag.USERGROUPFLAG);
		if (null != groupIds && !groupIds.isEmpty()) {
			Iterator<String> iterator = groupIds.iterator();
			// 创建要返回给客户端的群组信息集合
			List<ContactGroupVo> list = new ArrayList<>();
			while (iterator.hasNext()) {
				// 创建一个要返回到客户端的群组信息VO类
				ContactGroupVo groupInfoVo = new ContactGroupVo();
				// 循环取出用户的群组id
				String groupId = iterator.next();
				// 根据群组id 获取缓存中的群组的详细信息
				GroupInfoBean groupInfo = WebConvertObjectUtils.convertJsonStrToObject(
						jedisConfig.get(groupId + WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
				// 赋值群组的详细信息
				groupInfoVo.setGroupId(groupId);

				groupInfoVo.setGroupName(groupInfo.getIndexName());
				groupInfoVo.setGroupDefaultName(groupInfo.getDefaultName());

				// 获取群成员id列表
				Set<String> userIds = jedisConfig.hkeys(groupId);
				// 迭代群成员列表
				Iterator<String> idIts = userIds.iterator();

				List<String> userPhotos = new ArrayList<>();
				int i = 0;
				while (idIts.hasNext() && i++ < 9) {
					String userId = idIts.next();
					// 获取群成员的详细信息
					UserInfoBean infoBean = DwrUserSessionManagerUtils.getUserInfoByUserId(userId);
					userPhotos.add(infoBean.getUserPhoto());
				}

				groupInfoVo.setUserPhotos(userPhotos);
				list.add(groupInfoVo);
			}
			resultJson = WebConvertObjectUtils.convertObjectArrayToJsonStr(list);
			logger.info("返回给客户端 包含群成员的 群组列表信息:" + resultJson);
		}
		jedisConfig.close();
		return resultJson;
	}

	/**
	 * @Description: 创建群,创建时可同时添加多个成员
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 上午10:09:30
	 * @version V1.0
	 */

	public String createGroup(String[] userId) {

		if (null == userId || userId.length < 2) {
			return WebConstants.RESULT.FAILURE;
		}
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		// 生成群id
		String groupUuid = UUIDGenerateUtils.getGroupUUID();
		// 获取登录用户
		UserInfoBean masterBean = DwrBaseService.getCurrentUser();
		// 群主id
		String masterId = masterBean.getId();
		// 设置群基本信息
		GroupInfoBean groupInfo = new GroupInfoBean();
		// 设置群的uuid
		groupInfo.setGroupId(groupUuid);
		// 设置群主id
		groupInfo.setGroupMasterId(masterId);
		// 设置群当前的数量
		groupInfo.setGroupMemberCount(userId.length + 1);
		// 设置群的默认名称
		groupInfo.setDefaultName("群聊 (" + (groupInfo.getGroupMemberCount()) + ")");

		StringBuilder nameBuilder = new StringBuilder();
		StringBuilder idBuilder = new StringBuilder();
		StringBuilder contentBuilder = new StringBuilder();
		for (int i = 0; i < userId.length; i++) {
			// 根据id获取要添加到群组中的用户信息
			UserInfoBean groupMember = DwrUserSessionManagerUtils.getUserInfoByUserId(userId[i]);
			if(i<3) {
				nameBuilder.append(groupMember.getUserName() + ",");
			}
			contentBuilder.append(groupMember.getNameCn()+"-"+ groupMember.getUserName() + ",");
			idBuilder.append(userId[i] + ",");
			
			// 创建用户在群组中的偏好设置Info
			UserInfoForGroup infoForGroup = new UserInfoForGroup();
			// 设置用户在群组中的偏好设置
			infoForGroup.setUserId(userId[i]);

			// 在群中添加成员 数据类型 <id - bean>
			jedisConfig.hset(groupUuid, userId[i], WebConvertObjectUtils.convertObjectToJsonStr(infoForGroup));
			// 在群员列表中添加群的id
			jedisConfig.sadd(userId[i] + WebConstants.Flag.USERGROUPFLAG, groupUuid);
			logger.info("添加了一个群成员:" + userId[i] + " -  userName:" + groupMember.getUserName());
		}
		groupInfo.setIndexName((nameBuilder.substring(0, nameBuilder.length() - 1)));
		groupInfo.setCreateDate(DateUtils.getDateStr());
		// 存放用户所包含的群组列表(list格式存储)
		jedisConfig.sadd(masterId + WebConstants.Flag.USERGROUPFLAG, groupUuid);

		// 设置群主在群中的信息
		UserInfoForGroup masterInfoForGroup = new UserInfoForGroup();
		masterInfoForGroup.setUserId(masterId);
		// 在群中添加群主信息 偏好设置等 数据类型 <id - bean>
		jedisConfig.hset(groupUuid, masterId, WebConvertObjectUtils.convertObjectToJsonStr(masterInfoForGroup));

		// 存放群id的详细信息 数据格式为( 以key(uuid+GI)-value(groupInfo)格式存储)
		jedisConfig.set((groupUuid + WebConstants.Flag.GROUP_INFO),
				WebConvertObjectUtils.convertObjectToJsonStr(groupInfo));

		logger.info("新创建群ID [" + groupUuid + "]   共添加了" + (userId.length + 1) + "个成员,群主id[" + masterId + "]===成员id为["
				+ (idBuilder.substring(0, idBuilder.length() - 1)) + "] ");
		logger.info("在群ID [" + groupUuid + "]中添加了成员,群主名称为[" + masterBean.getNameCn() + masterBean.getUserName()
				+ "] 成员名称为[" + (contentBuilder.substring(0, contentBuilder.length() - 1)) + "] ");
		sendSystemChatMessage(groupUuid, masterBean.getNameCn() + masterBean.getUserName()+"邀请了 "+(contentBuilder.substring(0, contentBuilder.length() - 1))+"加入了群聊",ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_CREATE);
		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}

	/**
	 * 
	 * @Description: 添加用户到群组中 ,一次可添加多个
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:33:45
	 * @version V1.0
	 * @param groupId
	 * @param ids
	 *            需要添加的群成员id 数组
	 * @return
	 */

	public String addUserToGroup(String groupId, String[] ids) {
		
		if(!WebStringUtils.checkStringIsNull(ids)) {
			return WebConstants.RESULT.FAILURE;
		}
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		
		//获取群成员列表
		Set<String>set =  jedisConfig.hkeys(groupId);
		if(!set.contains(getCurrentUser().getId())) {
			//如果不是群中的成员，不能添加群成员到群中
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		UserInfoForGroup infoForGroup = new UserInfoForGroup();
		StringBuilder nameBuilder = new StringBuilder();
		for (String memberId : ids) {
			//循环将群成员添加至群组当中
			infoForGroup.setUserId(memberId);
			//取出被邀请人的详细信息
			UserInfoBean memberInfo =  DwrUserSessionManagerUtils.getUserInfoByUserId(memberId);
			nameBuilder.append( memberInfo.getNameCn() + memberInfo.getUserName()+",");
			jedisConfig.hset(groupId, memberId, WebConvertObjectUtils.convertObjectToJsonStr(infoForGroup));
			jedisConfig.sadd(memberId + WebConstants.Flag.USERGROUPFLAG, groupId);

		}
		sendSystemChatMessage(groupId, getCurrentUser().getNameCn()+getCurrentUser().getUserName()+"邀请了 "+(nameBuilder.substring(0, nameBuilder.length() - 1))+"加入了群聊",ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_ADD);

		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}


	/**
	 * 
	 * @Description: 删除群成员
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:31:40
	 * @version V1.0
	 * @param groupId
	 * @param removeId
	 * @return
	 */

	public String removeGroupUser(String groupId,  String []removeId) {
		if(!WebStringUtils.checkStringIsNull(removeId)||!WebStringUtils.checkStringIsNull(groupId)) {
			return WebConstants.RESULT.FAILURE;
		}
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		GroupInfoBean groupInfoBean =	WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(groupId+WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
		if(null==groupInfoBean) {
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		//检查登录者是否为群主
		if(!getCurrentUser().getId().equals(groupInfoBean.getGroupMasterId())){
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		for (String string : removeId) {
			if(getCurrentUser().getId().equals(string)) {
				//如果移除的人中，包含群主，则操作违规，返回失败
				jedisConfig.close();
				return WebConstants.RESULT.FAILURE;
			}
		}
		Set<String>set =  jedisConfig.hkeys(groupId);
		if(null==set || set.size()-removeId.length<3) {
			//如果群成员小于三  ,解散群
			dissolveGroup(jedisConfig, groupId, set);
			jedisConfig.close();
			return WebConstants.RESULT.SUCCESS;
		}
		StringBuilder nameBuilder = new StringBuilder();
		for (String memberId : removeId) {
			UserInfoBean memberInfo =  DwrUserSessionManagerUtils.getUserInfoByUserId(memberId);
			nameBuilder.append(memberInfo.getNameCn() + memberInfo.getUserName()+",");
			Map<String, String> modifyUnreadMap = new HashMap<String, String>();
			//清空群成员未读消息数量
			modifyUnreadMap.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
			modifyUnreadMap.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, memberId);
			modifyUnreadMap.put(ServConstants.ConditionUserMemberMsg.UNREAD_NUM, "0");
			
			
			memMsgService.modifyGroupMemMsgUnreadCount(modifyUnreadMap);
			
			jedisConfig.hdel(groupId, memberId);
			//从群成员的群组列表中移除
			jedisConfig.srem(memberId+WebConstants.Flag.USERGROUPFLAG, groupId);
			//从群员的会话列表中移除
			jedisConfig.lrem(memberId+WebConstants.Flag.USERSESSIONFLAG,0, groupId+"|"+WebConstants.SESSION.SESSION_GROUP);
		}
		sendSystemChatMessage(groupId, getCurrentUser().getNameCn()+getCurrentUser().getUserName()+"将 "+(nameBuilder.substring(0, nameBuilder.length() - 1))+"移出了群聊",ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_REM);
	
		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}

	/**
	 * 
	 * @Description: 修改群信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:31:52
	 * @version V1.0
	 * @param groupId
	 * @param bean
	 * @return
	 */

	public 	String modifyGroupInfo(String groupInfoJsonStr){
		GroupInfoBean bean = null;
		try {
			bean = WebConvertObjectUtils.convertJsonStrToObject(groupInfoJsonStr, GroupInfoBean.class);
			if(null==bean) {
				return  WebConstants.RESULT.FAILURE;
			}
		} catch (Exception e) {
			return WebConstants.RESULT.FAILURE;
		}
		String groupId = bean.getGroupId();
		String groupName = bean.getGroupName();
		String groupNotice = bean.getGroupNotice();
		if(!WebStringUtils.checkStringIsNull(groupId)) {
			return WebConstants.RESULT.FAILURE;
		}
		if(!WebStringUtils.checkStringIsNull(groupName)&&!WebStringUtils.checkStringIsNull(groupNotice)) {
			return WebConstants.RESULT.FAILURE;
		}
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		GroupInfoBean oldGroupInfoBean = WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(groupId+WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
		UserInfoBean currentUser = getCurrentUser();
		if(!oldGroupInfoBean.getGroupMasterId().equals(currentUser.getId())) {
			//说明不是群主
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		oldGroupInfoBean.setDefaultName(groupName);
		if(WebStringUtils.checkStringIsNull(groupName)) {
			oldGroupInfoBean.setGroupName(groupName);
		}
		oldGroupInfoBean.setIndexName(groupName);
		oldGroupInfoBean.setGroupNotice(groupNotice);
		jedisConfig.set(groupId+WebConstants.Flag.GROUP_INFO, WebConvertObjectUtils.convertObjectToJsonStr(oldGroupInfoBean));
//		sendSystemChatMessage(groupId, currentUser+"修改了群名称为[ "+groupName+"]",ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_MODIFY);
		sendSystemChatMessage(groupId, groupName,ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_MODIFY);

		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}

	/**
	 * 
	 * @Description:解散群
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月10日下午3:05:35
	 * @version V1.0
	 * @param jedisConfig
	 * @param groupId
	 * @param set
	 */
	private void dissolveGroup(Jedis jedisConfig ,String groupId,Set<String>set) {
			//删除群组的hash键
			jedisConfig.del(groupId);
			Iterator<String>memSet = set.iterator();
			while(memSet.hasNext()) {
				String memberId = memSet.next();
				//从群成员的群组列表中移除
				jedisConfig.srem(memberId+WebConstants.Flag.USERGROUPFLAG, groupId);
				//从群员的会话列表中移除
				jedisConfig.lrem(memberId+WebConstants.Flag.USERSESSIONFLAG,0, groupId+"|"+WebConstants.SESSION.SESSION_GROUP);
			}
	}
	
	
	/**
	 * 
	 * @Description: 退群操作
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:32:00
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	public String exitGroup(String groupId) {
		if(!WebStringUtils.checkStringIsNull(groupId)) {
			return WebConstants.RESULT.FAILURE;
		}
		
		
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		//获取群成员列表
		Set<String>set =  jedisConfig.hkeys(groupId);
		//如果不存在当前群，将无法获取群员列表  ,返回失败
		if(null==set||set.isEmpty()) {
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		if(null==set || set.size()-1<3) {
			dissolveGroup(jedisConfig, groupId,set);
			jedisConfig.close();
			return WebConstants.RESULT.SUCCESS;
		}
		//获取群的详细信息
		GroupInfoBean groupInfoBean =	WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(groupId+WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
		if(null==groupInfoBean) {
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		
		UserInfoBean currentUser = getCurrentUser();
		//要退出群的用户为当前用户
		String loginId=currentUser.getId();
		
		
		//判断当前登录用户 的是否在群中
		String userInGroupInfo = jedisConfig.hget(groupId, loginId);
		if(!WebStringUtils.checkStringIsNull(userInGroupInfo)) {
			logger.error("当前登录人  已不在群中");
		}
		String loginName = currentUser.getNameCn()+currentUser.getUserName();
		//检查登录者是否为群主
		boolean isMaster = loginId.equals(groupInfoBean.getGroupMasterId());
		
		//从群中移除成员
		jedisConfig.hdel(groupId, loginId);
		String message=loginName+" 退出了群聊";
		
		// 如果是群主,重新命名新的群主 ，取第一个
		if(isMaster){
			String newMasterId=null;
			for (String memberId : jedisConfig.hkeys(groupId)) {
				// 如果是群主,重新命名新的群主 ，取第一个
				newMasterId=memberId;
				break;
			}
			groupInfoBean.setGroupMasterId(newMasterId);
			jedisConfig.set(groupId+WebConstants.Flag.GROUP_INFO,WebConvertObjectUtils.convertObjectToJsonStr(groupInfoBean));
			
			//新群主的信息
			UserInfoBean newMasterInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(newMasterId);
			message="群主 ["+loginName+"]退出了群聊,新的群主为["+newMasterInfo.getNameCn() +"-"+ newMasterInfo.getUserName()+"]";
		}
				
		jedisConfig.srem(loginId+WebConstants.Flag.USERGROUPFLAG, groupId);
		//从群员的会话列表中移除
		jedisConfig.lrem(loginId+WebConstants.Flag.USERSESSIONFLAG,0, groupId+"|"+WebConstants.SESSION.SESSION_GROUP);
		
		sendSystemChatMessage(groupId,message ,ServConstants.System.IS_NOTICE_YES,ServConstants.System.OPR_TYPE_EXIT);

		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}
	/**
	 * 
	 * @Description:发送系统消息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月29日下午1:26:20
	 * @version V1.0
	 * @param toGroupId
	 * @param content
	 * @param isNoticeFlag
	 * @param opr
	 * @return
	 */
	public String sendSystemChatMessage(String toGroupId, String content,String isNoticeFlag,String opr) {
		RedisMQProvider provider = RedisClientUtils.getMQProviderConn();
		GroupMessage message=null;
		try {
			// 获取登陆人的id
			String fromUserId = DwrBaseService.getCurrentUser().getId();
			if(!WebStringUtils.checkStringIsNull(isNoticeFlag,opr)) {
				 message = new GroupMessage(null, fromUserId, toGroupId, ServConstants.System.IS_NOTICE_NO,
						ServConstants.System.OPR_TYPE_NO, DateUtils.getDateStr(), content);
			}else {
				message = new GroupMessage(null, fromUserId, toGroupId, isNoticeFlag,
						opr, DateUtils.getDateStr(), content);
			}
			// 将封装消息的对象 转化为json字符串
			String messageObject = WebConvertObjectUtils.convertObjectToJsonStr(message);
			// 添加到聊天的会话列表当中
			provider.publish(WebConstants.Channel.GROUP_MESSAGE_CHANNEL_NAME, messageObject);
		} catch (RedisException e) {
			logger.error("广播消息失败! - Cause:" + e);
			e.printStackTrace();
			return WebConstants.RESULT.FAILURE;
		} finally {
			provider.release();
		}
		return WebConstants.RESULT.SUCCESS;
	}
	/**
	 * 接收客户端发送的群组消息内容
	 */

	public String sendChatMessage(String toGroupId, String content) {
		if("".equals(content.trim())) {
			return WebConstants.RESULT.FAILURE;
		}
		
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		
		Set<String>memberIds =  jedisConfig.hkeys(toGroupId);
		//如果群已经解散  则返回失败
		if(null==memberIds || memberIds.isEmpty()) {
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		System.out.println("===========getCurrentUser().getId()"+getCurrentUser().getId());
		boolean isExist = jedisConfig.hexists(toGroupId, getCurrentUser().getId());
		if(!isExist) {
			jedisConfig.close();
			return WebConstants.RESULT.FAILURE;
		}
		jedisConfig.close();
		return	sendSystemChatMessage(toGroupId, content, null, null);
	}

	/**
	 * 
	 * @Description:查询群组的聊天记录
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月18日下午1:48:41
	 * @version V1.0
	 * @param groupId
	 * @return
	 */

	public String queryGroupChatRecordList(String groupId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(ServConstants.ConditionGroupMsg.TO_GROUP_ID, groupId);
		params.put(ServConstants.PAGE.NOW_PAGE_STR, ServConstants.PAGE.DEFAULT_NOW_PAGE);
		params.put(ServConstants.PAGE.PAGE_SIZE_STR, ServConstants.PAGE.DEFAULT_MAX_UNREAD_SIZE);
		params.put(ServConstants.OrderField.ORDER_CONDITION, ServConstants.OrderField.ORDER_FIELD_SEND_TIME_ASC);
		List<GroupMessage> groupMessages = groupMsgService.queryGroupMsgByPage(params);
		if (null != groupMessages && !groupMessages.isEmpty()) {
			// 获取登录者的信息
			UserInfoBean loginUser = DwrBaseService.getCurrentUser();
			String loginId = loginUser.getId();
			List<GroupChatContentRecordVo> recordVos = new ArrayList<>();
			for (GroupMessage message : groupMessages) {
				GroupChatContentRecordVo chatContentRecordVo = new GroupChatContentRecordVo();
				// 获取发送者的详细信息
				UserInfoBean fromBean = DwrUserSessionManagerUtils.getUserInfoByUserId(message.getFromUserId());

				chatContentRecordVo.setContent(message.getContent());
				chatContentRecordVo.setUserId(message.getFromUserId());
				chatContentRecordVo.setUserPhoto(fromBean.getUserPhoto());
				chatContentRecordVo.setSendTime(message.getSendTime());
				chatContentRecordVo.setUserName(fromBean.getUserName());
				if (loginId.equals(message.getFromUserId())) {
					chatContentRecordVo.setFlag(WebConstants.Message.SHOW_MESSAGE_RIGHT);
				} else {
					chatContentRecordVo.setFlag(WebConstants.Message.SHOW_MESSAGE_LEFT);
				}
				recordVos.add(chatContentRecordVo);
			}
			return WebConvertObjectUtils.convertObjectArrayToJsonStr(recordVos);
		}
		return null;
	}

	/**
	 * 
	 * @Description:查询群组的成员列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月18日下午7:33:00
	 * @version V1.0
	 * @param groupId
	 * @return
	 */

	public String queryGroupMemberList(String groupId) {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		// 获取群组的成员列表
		Set<String> userIds = jedisConfig.hkeys(groupId);
		String str = null;
		if (null != userIds && !userIds.isEmpty()) {
			Iterator<String> iterator = userIds.iterator();
			// 创建要返回给客户端的群组信息集合
			List<GroupMemberVo> list = new ArrayList<>();

			while (iterator.hasNext()) {
				String userId = iterator.next();
				UserInfoBean userInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(userId);
				// 创建要返回给客户端的 群组成员VO类
				GroupMemberVo memberVo = new GroupMemberVo();

				memberVo.setUserId(userId);
				memberVo.setUserName(userInfo.getUserName());
				memberVo.setUserPhoto(userInfo.getUserPhoto());
				list.add(memberVo);
			}
			str = WebConvertObjectUtils.convertObjectArrayToJsonStr(list);
		}
		jedisConfig.close();
		return str;
	}

	/**
	 * 
	 * @Description:点击会话列表时 将未读消息置为已读
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月29日上午9:14:43
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	public String modifySessionListIsUnread(String groupId) {
		Map<String, String> params = new HashMap<>();
		params.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, groupId);
		params.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, getCurrentUser().getId());
		params.put(ServConstants.ConditionUserMemberMsg.UNREAD_NUM, "0");
		memMsgService.modifyGroupMemMsgUnreadCount(params);
		return null;
	}

	
	/***
	 * 
	 * @Description:点击右上角详情时 查询出来的成员列表 群公告等
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月29日上午9:10:11
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	public String queryGroupMemberListAndGroupDetail(String groupId) {
		
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		// 获取群组的成员列表
		Set<String> userIds = jedisConfig.hkeys(groupId);
		String str = null;
		// 创建要返回给客户端的群组信息集合
		List<GroupMemberVo> list = null;
		QueryGroupResultVo groupResultVo =null;
		if (null != userIds && !userIds.isEmpty()) {
			Iterator<String> iterator = userIds.iterator();
			 groupResultVo = new QueryGroupResultVo();
			 
			 GroupInfoBean bean =  WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(groupId+WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
			 groupResultVo.setGroupName(bean.getGroupName());
			 groupResultVo.setGroupNotice(bean.getGroupNotice());
			 String currentUserId = getCurrentUser().getId();
			 //当前登录用户是否 为群主
			 groupResultVo.setIsMaster(currentUserId.equals(bean.getGroupMasterId()));
			 
			 list = new ArrayList<>();
			while (iterator.hasNext()) {
				//获取群成员id
				String userId = iterator.next();
				UserInfoBean userInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(userId);
				// 创建要返回给客户端的 群组成员VO类
				GroupMemberVo memberVo = new GroupMemberVo();

				//获取群成员属性 封装成对象
				memberVo.setUserId(userId);
				memberVo.setUserName(userInfo.getUserName());
				memberVo.setUserPhoto(userInfo.getUserPhoto());
				//将群成员对象存入列表中
				list.add(memberVo);
			}
			groupResultVo.setDetailVOs(list);
			str = WebConvertObjectUtils.convertObjectToJsonStr(groupResultVo);
		}
		jedisConfig.close();
		return str;
	}
}
