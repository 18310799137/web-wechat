package com.yucheng.im.service.web.dwr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.FriendInfoBean;
import com.yucheng.im.service.entity.GroupInfoBean;
import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.UserInfoForGroup;
import com.yucheng.im.service.entity.UserInfoSolrIndex;
import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.entity.vo.friend.FriendVo;
import com.yucheng.im.service.entity.vo.session.BaseSessionVo;
import com.yucheng.im.service.entity.vo.session.QueryGroupSessionVo;
import com.yucheng.im.service.entity.vo.session.QueryUserSessionListsVo;
import com.yucheng.im.service.entity.vo.session.QueryUserSessionVo;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.service.api.IDwrUserQueryService;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.SolrUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

public class DwrUserQueryServiceImpl extends DwrBaseService implements IDwrUserQueryService {

	private static Logger logger = Logger.getLogger(DwrUserQueryServiceImpl.class);

	/**
	 * @Description:
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 上午10:07:31
	 * @version V1.0
	 */

	public String queryUserInPlatform(String custUserName) {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		//获取登录用户的好友列表
		UserInfoBean loginUser = DwrUserSessionManagerUtils.getLoginUserInfoBySessionId();
		Jedis jedisConf = RedisClientUtils.getRedisConfigSource();
		Set<String>set = jedisConf.hkeys(loginUser.getId()+WebConstants.Flag.USERFRIENDSFLAG);
		
		if(null==set) {
			set = new HashSet<>();
		}
		//将登录人的id 添加到排除人员中
		set.add(getCurrentUser().getId());
		
		
		Iterator<String>ite = set.iterator();
		String []filterQueryList = new String[set.size()];
		int index=0;
		while(ite.hasNext()) {
			//如果是好友的成员,搜索时不出现在平台用户列表中
			String friendId =  ite.next();
			filterQueryList[index++]="-id:"+friendId;
		}
		
		// 根据UserName字段模糊匹配全量用户信息 获取索引列表
		List<UserInfoSolrIndex> indexs = SolrUtils.queryUserFilter("id,custUserName", "custUserName:" + custUserName, 0, 10, filterQueryList);
		List<UserInfoBean> lists = new ArrayList<UserInfoBean>();
		logger.info("querycustUserName:" + custUserName + "\tindexs:" + indexs);
		if (null == indexs || indexs.size() < 1) {
			logger.info(custUserName + " - 查询没有结果 - resultSize:" + indexs);
			jedisConfig.close();
			jedisConf.close();
			return null;
		}
		// 对索引 ID进行关联 用户信息
		for (UserInfoSolrIndex userInfoSolrIndex : indexs) {
			String userId = (userInfoSolrIndex.getId() + WebConstants.Flag.USERINFOFLAG);

			UserInfoBean bean = WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(userId),
					UserInfoBean.class);
			bean.setCustUserName(userInfoSolrIndex.getCustUserName());
			lists.add(bean);
		}
		String resultJson = JSONArray.fromObject(lists).toString();
		logger.info("resultJson:" + resultJson);
		jedisConfig.close();
		jedisConf.close();
		return resultJson;
	}


	public String queryUserSessionList() {
		// 获取登录者的id 姓名
		UserInfoBean userInfo = DwrBaseService.getCurrentUser();
		String loginUserId = userInfo.getId();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();

		// 获取好友会话列表
		List<String> list = jedisConfig.lrange(loginUserId + WebConstants.Flag.USERSESSIONFLAG, 0, -1);
		// 创建一个要返回给客户端的会话列表VO类
		QueryUserSessionListsVo listsVo = new QueryUserSessionListsVo();
		// 创建一个存放 会话列表的集合
		List<? super BaseSessionVo> sessionLists = new ArrayList<>();
		listsVo.setSessionLists(sessionLists);

		for (String string : list) {
			// 遍历会话列表 取出会话列表ID,集合中存储的格式为 ,会话的ID|FLAG
			String[] sessionIds = string.split("\\|");
			// 获取会话列表中的Id 用户id或者群组id
			String id = sessionIds[0];
			// 如果会话对象为群组
			if (WebConstants.SESSION.SESSION_GROUP.equals(sessionIds[1])) {
				// 获取群成员列表
				Map<String, String> map = jedisConfig.hgetAll(id);
				List<String> userPhotos = new ArrayList<>();
				int groupImageCounts = 0;

				// 获取群组的详细信息
				GroupInfoBean groupInfoBean = WebConvertObjectUtils.convertJsonStrToObject(
						jedisConfig.get(id + WebConstants.Flag.GROUP_INFO), GroupInfoBean.class);
				// 创建与此群会话列表中要展示的详细信息 包含 群id,群名称,群图片信息,群中未读消息,群中最后一条消息记录
				QueryGroupSessionVo groupSessionVo = new QueryGroupSessionVo();
				// 查询群组中最后一条消息记录
				Map<String, String> pageParams = new HashMap<String, String>();
				pageParams.put(ServConstants.ConditionGroupMsg.TO_GROUP_ID, id);
				pageParams.put(ServConstants.OrderField.ORDER_CONDITION,
						ServConstants.OrderField.ORDER_FIELD_SEND_TIME_DESC);
				pageParams.put(ServConstants.PAGE.NOW_PAGE_STR, "1");
				pageParams.put(ServConstants.PAGE.PAGE_SIZE_STR, "1");

				List<GroupMessage> lists = groupMsgService.queryGroupMsgByPage(pageParams);
				GroupMessage lastChatMember = null;
				if (null != lists && !lists.isEmpty()) {
					lastChatMember = lists.get(0);
				}

				// 查询群组中未读消息数量
				Map<String, String> msgCountParams = new HashMap<String, String>();
				msgCountParams.put(ServConstants.ConditionUserMemberMsg.GROUP_ID, id);
				msgCountParams.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, loginUserId);
				int unReadMsgNum = memMsgService.queryUnreadMsgCount(msgCountParams);

				// 获取最后一位发言人的内容和发言人的名称
				groupSessionVo.setContent(lastChatMember.getContent());
				groupSessionVo.setLastChatName(
						DwrUserSessionManagerUtils.getUserInfoByUserId(lastChatMember.getFromUserId()).getUserName());

				groupSessionVo.setSessionFlag(WebConstants.SESSION.SESSION_GROUP);
				groupSessionVo.setToChatGroupId(id);
				groupSessionVo.setToChatGroupName(groupInfoBean.getIndexName());
				groupSessionVo.setDefaultName(groupInfoBean.getDefaultName());
				groupSessionVo.setUnReadMsgNum(unReadMsgNum);

				for (Entry<String, String> entry : map.entrySet()) {
					// 获取群成员的id
					String memberId = entry.getKey();
					// 获取群成员的详细信息
					UserInfoBean bean = DwrUserSessionManagerUtils.getUserInfoByUserId(memberId);
					String userPhoto = bean.getUserPhoto();
					if (groupImageCounts++ < 9) {
						// 将群成员的图片信息 存储到展示群图片的集合中
						userPhotos.add(userPhoto);
					}

				}

				groupSessionVo.setUserPhotos(userPhotos);
				sessionLists.add(groupSessionVo);

			}
			// 如果会话对象为个人
			else if (WebConstants.SESSION.SESSION_USER.equals(sessionIds[1])) {
				QueryUserSessionVo queryUserSessionVo = new QueryUserSessionVo();

				// 查询未读消息数量
				Map<String, String> params = new HashMap<>();
				// 设置会话列表中的人的id为 发送人的姓名
				params.put(ServConstants.ConditionUserMsg.FROM_USER_ID, id);
				// 设置接收消息人的id 为登录者的id
				params.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginUserId);
				// 设置查询条件 为未读的消息
				params.put(ServConstants.ConditionUserMsg.IS_UNREAD, ServConstants.System.IS_UNREAD_YES);
				params.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_NO);

				int unReadMsgNum = iUserMsgService.queryUnreadUserMsgCount(params);

				// 查询最后一条消息内容
				Map<String, String> qryLastChatParam = new HashMap<>();
				qryLastChatParam.put(ServConstants.ConditionUserMsg.FROM_USER_ID, id);
				qryLastChatParam.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginUserId);
				qryLastChatParam.put(ServConstants.OrderField.ORDER_CONDITION,
						ServConstants.OrderField.ORDER_FIELD_SEND_TIME_DESC);
				qryLastChatParam.put(ServConstants.PAGE.NOW_PAGE_STR, "1");
				qryLastChatParam.put(ServConstants.PAGE.PAGE_SIZE_STR, "1");
				List<UserMessage> messageLists = iUserMsgService.queryUserMsgByPage(qryLastChatParam);
				UserMessage lastChatMessage = null;
				if (null != messageLists && !messageLists.isEmpty()) {
					lastChatMessage = messageLists.get(0);
				}

				// 查询最后一条消息内容 和发送人的名字
				if (null != lastChatMessage) {
					queryUserSessionVo.setContent(lastChatMessage.getContent());
				}
				// 查询聊天人的名称
				UserInfoBean bean = DwrUserSessionManagerUtils.getUserInfoByUserId(id);
				String chatUserName = bean.getUserName();
				//设置会话标识为个人
				queryUserSessionVo.setSessionFlag(WebConstants.SESSION.SESSION_USER);
				queryUserSessionVo.setUnReadMsgNum(unReadMsgNum);
			
				queryUserSessionVo.setUserId(id);
				
				
				//判断是否是好友关系
				String friendJson = jedisConfig.hget(loginUserId+WebConstants.Flag.USERFRIENDSFLAG, id);
				if(null==friendJson || "".equals(friendJson)) {
					queryUserSessionVo.setIsFriend(WebConstants.Friend.IS_FRIEND_NO);
				}else {
					queryUserSessionVo.setIsFriend(WebConstants.Friend.IS_FRIEND_YES);
				}
				
				queryUserSessionVo.setUserName(chatUserName);
				queryUserSessionVo.setUserPhoto(bean.getUserPhoto());
				sessionLists.add(queryUserSessionVo);
			}
		}
		Map<String, String> grpMap = new HashMap<>();
		grpMap.put(ServConstants.ConditionUserMemberMsg.MEMBER_ID, loginUserId);
		// 获取群组中所有未读消息总数
		int grpUnrdCnt = memMsgService.queryAllGroupMemMsgUnreadCount(grpMap);

		// 获取所有好友未读消息总数
		Map<String, String> usrMap = new HashMap<>();
		usrMap.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginUserId);
		usrMap.put(ServConstants.ConditionUserMsg.IS_UNREAD, ServConstants.System.IS_UNREAD_YES);
		usrMap.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_NO);
		// 查询用户的未读消息
		int usrUnrdCount = iUserMsgService.queryUnreadUserMsgCount(usrMap);
		// 群组未读消息和好友未读消息总数
		int allUnrdSum = grpUnrdCnt + usrUnrdCount;

		//查询未读消息数量
		Map<String, String> reqUnrdMap = new HashMap<>();
		reqUnrdMap.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginUserId);
		reqUnrdMap.put(ServConstants.ConditionUserMsg.IS_UNREAD, ServConstants.System.IS_UNREAD_YES);
		reqUnrdMap.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_YES);
		reqUnrdMap.put(ServConstants.ConditionUserMsg.M_TYPE, ServConstants.System.REQ_MSG_FLAG);
		reqUnrdMap.put(ServConstants.ConditionUserMsg.DISPOSE_STATUS, ServConstants.System.DISPOSE_STATUS_UN);
		int reqUnrdCount = iUserMsgService.queryUnreadUserMsgCount(reqUnrdMap);

		listsVo.setUnReadReqCount(reqUnrdCount);
		listsVo.setUnReadSessionCount(allUnrdSum);
		String resultJson = WebConvertObjectUtils.convertObjectToJsonStr(listsVo);
		logger.debug("返回给客户端的会话列表 Json字符串:" + resultJson);
		jedisConfig.close();
		return resultJson;
	}

	public static void main(String[] args) {
		System.out.println(UUIDGenerateUtils.getLocalHostIpAddressNoReplace());
		
		
		String str="";
		System.out.println(str.substring(0, str.length()-1));
	}

	/**
	 * 
	 * @Description:查询登录者自己的信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月18日下午8:27:49
	 * @version V1.0
	 * @return
	 */

	public String queryOneSelfInfo() {
		UserInfoBean oneself = DwrBaseService.getCurrentUser();
		return WebConvertObjectUtils.convertObjectToJsonStr(oneself);
	}
	/**
	 * 
	 * @Description:点击个人名片时    返回是否为好友 如果是则返回 好友信息及备注
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月22日下午5:32:37
	 * @version V1.0
	 * @param userId
	 * @param flag
	 * @param groupId
	 * @return
	 */
	public String queryUserDetail(String userId,String flag,String groupId) {
		String currentUserId = getCurrentUser().getId();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		FriendVo friendVo = new FriendVo();
		UserInfoBean friendUserInfo =	DwrUserSessionManagerUtils.getUserInfoByUserId(userId);
		String friendInfoJsonStr = jedisConfig.hget(currentUserId+WebConstants.Flag.USERFRIENDSFLAG, friendUserInfo.getId());
		FriendInfoBean friendInfoBean =	WebConvertObjectUtils.convertJsonStrToObject(friendInfoJsonStr, FriendInfoBean.class);
		
		//如果不为null,说明好友列表中存在  
		if(null!=friendInfoJsonStr && !"".equals(friendInfoJsonStr)) {
			friendVo.setIsFriend("Y");
			friendVo.setSource(friendInfoBean.getSource()==null?"":friendInfoBean.getSource());
			friendVo.setRemarks(friendInfoBean.getRemarks()==null?"":friendInfoBean.getRemarks());
		}
			
		if(WebConstants.SESSION.SESSION_GROUP.equals(flag)) {
			//如果是在群中点击个人名片  ,查询群昵称
			String userInfoForGroupJSON = jedisConfig.hget(groupId, userId);
			UserInfoForGroup userInfoGorGroup = WebConvertObjectUtils.convertJsonStrToObject(userInfoForGroupJSON, UserInfoForGroup.class);
			friendVo.setGroupNickName(userInfoGorGroup.getGroupNickname()==null?"":userInfoGorGroup.getGroupNickname());
		}
		friendVo.setFriendPhoto(friendUserInfo.getUserPhoto());;
		friendVo.setFriendId(friendUserInfo.getId());
		friendVo.setFriendName(friendUserInfo.getUserName());
		friendVo.setRegion(friendUserInfo.getRegion()==null?"":friendUserInfo.getRegion());
		jedisConfig.close();
		return WebConvertObjectUtils.convertObjectToJsonStr(friendVo);
	}
	

	
	 
	/**
	 * 
	 * @Description:搜索好友列表中的好友
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月10日下午8:04:36
	 * @version V1.0
	 * @param custUserName
	 * @return
	 */
	public String queryUserInContactList(String custUserName) {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		//获取登录用户的好友列表
		UserInfoBean loginUser = getCurrentUser();
		
		Set<String>set = jedisConfig.hkeys(loginUser.getId()+WebConstants.Flag.USERFRIENDSFLAG);
		if(null==set || set.isEmpty()) {
			jedisConfig.close();
			return null;
		}
		
		Iterator<String>ite = set.iterator();
		StringBuilder filterQueryList = new StringBuilder();
		while(ite.hasNext()) {
			String friendId =  ite.next();
			filterQueryList.append("id:"+friendId+" or ");
		}
		String filterQueryListStr = "";
		if(filterQueryList.length()>0) {
			filterQueryListStr=filterQueryList.substring(0,filterQueryList.length()-3);
			System.out.println("================================"+filterQueryListStr);
		}
		// 根据UserName字段模糊匹配全量用户信息 获取索引列表
		List<UserInfoSolrIndex> indexs = SolrUtils.queryUserFilter("id,custUserName", "custUserName:" + custUserName, 0, 10, filterQueryListStr);
		List<UserInfoBean> lists = new ArrayList<UserInfoBean>();
		logger.info("querycustUserName:" + custUserName + "\tindexs:" + indexs);
		if (null == indexs || indexs.size() < 1) {
			logger.info(custUserName + " - 查询没有结果 - resultSize:" + indexs);
			jedisConfig.close();
			return null;
		}
		// 对索引 ID进行关联 用户信息
		for (UserInfoSolrIndex userInfoSolrIndex : indexs) {
			String userId = (userInfoSolrIndex.getId() + WebConstants.Flag.USERINFOFLAG);

			UserInfoBean bean = WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(userId),
					UserInfoBean.class);
			bean.setCustUserName(userInfoSolrIndex.getCustUserName());
			lists.add(bean);
		}
		String resultJson = WebConvertObjectUtils.convertObjectArrayToJsonStr(lists);

		logger.info("resultJson:" + resultJson);
		jedisConfig.close();
		return resultJson;
	}
}
