package com.yucheng.im.service.web.dwr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.FriendInfoBean;
import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.entity.vo.chat.FriendChatContentRecordVo;
import com.yucheng.im.service.entity.vo.chat.FriendChatRecordListVo;
import com.yucheng.im.service.entity.vo.contact.ContactFriendInfoVo;
import com.yucheng.im.service.entity.vo.contact.ContactFriendVo;
import com.yucheng.im.service.entity.vo.req.ReqListVo;
import com.yucheng.im.service.manager.service.IUserMsgService;
import com.yucheng.im.service.util.DateUtils;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.service.api.IDwrUserFriendService;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;

import cn.com.yusys.redis.client.exception.RedisException;
import cn.com.yusys.redis.mq.RedisMQProvider;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * 
 * @Title: DwrUserService.java
 * @Package com.yucheng.im.service.web.dwr
 * @Description: 处理用户关系服务类[添加好友,创建群聊,删除好友,解散群]
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月1日 下午7:32:50
 * @version V1.0
 * 
 */

public class DwrUserFriendServiceImpl extends DwrBaseService implements IDwrUserFriendService {

	private static Logger logger = Logger.getLogger(Logger.class);
	@Resource
	private IUserMsgService iUserMsgService;
	/**
	 * 
	 * 
	 * @Description:
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月12日上午11:48:28
	 * @version V1.0
	 * @param friendId
	 * @param reqMessage
	 * @return
	 */
	
	public String addUserReq(String friendId, String reqMessage) {
		String loginId = DwrBaseService.getCurrentUser().getId();
		if(loginId.equals(friendId)) {
			return WebConstants.RESULT.FAILURE;
		}
		logger.info("登录人ID ["+loginId+"] 请求添加["+friendId+"]为好友  - reqMessage:" + reqMessage);
		 RedisMQProvider provider = RedisClientUtils.getMQProviderConn();

		try {
			// 获取登陆人的id
			UserMessage object = new UserMessage();
			object.setContent(reqMessage);
			// 处理状态为未处理
			object.setDisposeStatus(ServConstants.System.DISPOSE_STATUS_UN);
			object.setFromUserId(loginId);
			// 默认为未读消息
			object.setIsUnread(ServConstants.System.IS_UNREAD_YES);
			// 请求类消息 为系统类消息
			object.setIsNotice(ServConstants.System.IS_NOTICE_YES);
			// R表示 request请求类消息
			object.setmType(ServConstants.System.REQ_MSG_FLAG);
			object.setSendTime(DateUtils.getDateStr());
			object.setToUserId(friendId);
			// 将封装消息的对象 转化为json字符串
			provider.publish(WebConstants.Channel.USER_MESSAGE_CHANNEL_NAME,
					WebConvertObjectUtils.convertObjectToJsonStr(object));
		} catch (RedisException e) {
			logger.error("广播消息失败! - Cause:" + e);
			return WebConstants.RESULT.FAILURE;
		}finally {
			provider.release();
		}
		logger.info("添加好友 消息已经广播出去了");
		return WebConstants.RESULT.SUCCESS;
	}

	/**
	 * 
	 * 
	 * @Description:
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月12日上午11:52:00
	 * @version V1.0
	 * @param friendId
	 * @param userOpr
	 * @return
	 */
	
	public String addUserResp(String friendId, String userOpr) {
		logger.debug("处理用户好友请求 - friendId" + friendId + " - userOpr:" + userOpr);
		// 获取登陆人的id
		String loginId = DwrBaseService.getCurrentUser().getId();
		// 同意消息的处理
		UserMessage object = new UserMessage();
		object.setContent("我同意了你的好友请求,开始聊天吧");
		// 处理状态为已处理
		object.setDisposeStatus(ServConstants.System.DISPOSE_STATUS_YES);
		object.setFromUserId(loginId);
		// 默认为未读消息
		object.setIsUnread(ServConstants.System.IS_UNREAD_YES);
		// 请求类消息 为系统类消息
		object.setIsNotice(ServConstants.System.IS_NOTICE_YES);
		// R表示 request请求类消息    P表示response类消息
		object.setmType(ServConstants.System.RESP_MSG_FLAG);
		object.setSendTime(DateUtils.getDateStr());
		object.setToUserId(friendId);
		// 将封装消息的对象 转化为json字符串
		JSONObject jsonObject = JSONObject.fromObject(object);
		
		 RedisMQProvider provider = RedisClientUtils.getMQProviderConn();
		try {
			// 添加到好友会话列表中
			
			provider.publish(WebConstants.Channel.USER_MESSAGE_CHANNEL_NAME, jsonObject.toString());
		} catch (RedisException e) {
			logger.error("广播消息失败! - Cause:" + e);
			e.printStackTrace();
			return WebConstants.RESULT.FAILURE;
		}finally {
			provider.release();
		}

		return WebConstants.RESULT.SUCCESS;
	}

	/***
	 * 获取登录者的好友列表信息
	 */
	
	public String queryUserFriendList() {
		 Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		// 获取登录者的详细信息
		UserInfoBean loginUser = DwrBaseService.getCurrentUser();
		// 登录用户的id
		String loginId = loginUser.getId();
		// 登录用户的好友列表 通过 用户id+标识存储好友列表 数据结构
		// <hashKey(userId+WebConstants.Flag.USERFRIENDSFLAG) ,friendId,friendInfoBean>
		String loginUserFriendFlag = loginId + WebConstants.Flag.USERFRIENDSFLAG;
		
		// 获取登录者的好友列表的 key集合
		Set<String> loginFriendIds = jedisConfig.hkeys(loginUserFriendFlag);
		
		logger.info("获取好友列表loginId:" + loginId+" - FriendList:"+loginFriendIds);
		
		
		List<ContactFriendVo> friendLists = null;
		if (null != loginFriendIds && !loginFriendIds.isEmpty()) {
			// 创建返回给客户端的 好友信息集合
			friendLists = new ArrayList<>();
			for (String friendId : loginFriendIds) {
				String friendJsonStr = jedisConfig.hget(loginUserFriendFlag, friendId);
				FriendInfoBean friendInfo = WebConvertObjectUtils.convertJsonStrToObject(friendJsonStr,
						FriendInfoBean.class);
				ContactFriendVo contactFriendVo = new ContactFriendVo();
				contactFriendVo.setFriendId(friendId);
				
				//如果存在好友备注 则返回备注
				String remarks = friendInfo.getRemarks();
				
				
				UserInfoBean friendInfos = DwrUserSessionManagerUtils.getUserInfoByUserId(friendId);
				contactFriendVo.setFriendName((remarks!=null && !"".equals(remarks.trim()))?remarks:friendInfos.getUserName());
				contactFriendVo.setUserPhoto(friendInfos.getUserPhoto());
				friendLists.add(contactFriendVo);
			}
		}
		jedisConfig.close();
		return WebConvertObjectUtils.convertObjectArrayToJsonStr(friendLists);
	}

	/**
	 * 
	 * 
	 * @Description:查询用户好友申请列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月14日下午8:09:52
	 * @version V1.0
	 * @return
	 */
	
	public String queryUserReqList() {
		UserInfoBean userInfo = DwrBaseService.getCurrentUser();
		String loginUserId = userInfo.getId();
		
		Map<String, String>params = new HashMap<String, String>();
		params.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginUserId);
		params.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_YES);
		params.put(ServConstants.ConditionUserMsg.DISPOSE_STATUS, ServConstants.System.DISPOSE_STATUS_UN);
		params.put(ServConstants.ConditionUserMsg.M_TYPE, ServConstants.System.REQ_MSG_FLAG);
		params.put(ServConstants.PAGE.NOW_PAGE_STR, "1");
		params.put(ServConstants.PAGE.PAGE_SIZE_STR,"1000");
		List<UserMessage> list = iUserMsgService.queryUserReqMsgList(params);
		if(null!=list && !list.isEmpty()) {
			//创建要返回给客户端申请列表的VO类集合
			List<ReqListVo>reqListVos = new ArrayList<>();
			
			for (UserMessage userMessage : list) {
				String reqUserId = userMessage.getFromUserId();
				//获取申请人的信息
				UserInfoBean reqUserInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(reqUserId);
				//把所需信息封装到VO类中
				ReqListVo listVo = new ReqListVo();
				listVo.setReqMsg(userMessage.getContent());
				listVo.setReqUserId(reqUserId);
				listVo.setReqUserName(reqUserInfo.getUserName());
				listVo.setUserPhoto(reqUserInfo.getUserPhoto());
				reqListVos.add(listVo);
			}
			return WebConvertObjectUtils.convertObjectArrayToJsonStr(reqListVos);
		}
		return null;
	}

	
	public String modifyReqListStatus() {
		return null;
	}

	
	public String modifyUserMsgStatus() {
		UserInfoBean userInfoBean = DwrBaseService.getCurrentUser();
		Map<String,String> params = new HashMap<>();
		params.put("toUserId", userInfoBean.getId());
		params.put("mType", ServConstants.System.REQ_MSG_FLAG);
		params.put("isUnread", ServConstants.System.IS_UNREAD_NO);
		params.put("isNotice", ServConstants.System.IS_NOTICE_YES);
		iUserMsgService.modifyUserMsgStatus(params);
		
	
		
		return WebConstants.RESULT.SUCCESS;
	}

	public static void main(String[] args) {
		List<String>list = new ArrayList<>();
		System.out.println(list.size());
		System.out.println(list);
		System.out.println(list.isEmpty());
		System.out.println(list.contains("a"));
	}

	/**
	 * 
	 * @Description: 客户端上送 好友id，查询与该ID所对应的聊天记录
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月15日下午4:51:26
	 * @version V1.0
	 * @param friendId
	 * @param nowPage
	 * @param pageSize
	 * @return
	 */
	
	public String queryFriendChatRecordList(String friendId, String nowPage, String pageSize) {
		//查询登录人的信息
		UserInfoBean loginUser =  DwrBaseService.getCurrentUser();
		String loginId = loginUser.getId();
		//查询好友的信息
		UserInfoBean friendUser = DwrUserSessionManagerUtils.getUserInfoByUserId(friendId);
	
		//对参数进行验证
		try {
			Integer.parseInt(nowPage);
			Integer.parseInt(pageSize);
		} catch (NumberFormatException e) {
			logger.error("解析前端分页字码错误   nowPage:"+nowPage+" - pageSize:"+pageSize);
			nowPage="1";
			pageSize="10";
		}
		
		//查询与好友的消息记录
		Map<String, String>queryMsg = new HashMap<>();
		queryMsg.put(ServConstants.PAGE.NOW_PAGE_STR,nowPage);
		queryMsg.put(ServConstants.PAGE.PAGE_SIZE_STR,ServConstants.PAGE.DEFAULT_MAX_UNREAD_SIZE);
		queryMsg.put(ServConstants.ConditionUserMsg.FROM_USER_ID, friendId);
		queryMsg.put(ServConstants.ConditionUserMsg.TO_USER_ID, loginId);
		queryMsg.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_NO);
		queryMsg.put(ServConstants.OrderField.ORDER_CONDITION, ServConstants.OrderField.ORDER_FIELD_SEND_TIME_ASC);

		List<UserMessage>lists =	iUserMsgService.queryUserMsgByPage(queryMsg);
		
		//创建要返回给客户端的VO类
		FriendChatContentRecordVo chatContentRecordVo = new FriendChatContentRecordVo();
		//创建消息体集合VO类
		List<FriendChatRecordListVo>recordListVos = new ArrayList<>();
		chatContentRecordVo.setRecordListVos(recordListVos);
		
		chatContentRecordVo.setUserId(friendId);
		chatContentRecordVo.setUserName(friendUser.getUserName());
		chatContentRecordVo.setUserPhoto(friendUser.getUserPhoto());
		chatContentRecordVo.setOneSelfPhoto(loginUser.getUserPhoto());
		
		
		if(null!=lists && !lists.isEmpty()) {
			for (UserMessage userMessage : lists) {
				FriendChatRecordListVo recordListVo = new FriendChatRecordListVo();
				recordListVo.setContent(userMessage.getContent());
				recordListVo.setSendTime(userMessage.getSendTime());
				//如果发送人是登录人自己,则将消息展示在右边
				if(loginId.equals(userMessage.getFromUserId())) {
					recordListVo.setFlag(WebConstants.Message.SHOW_MESSAGE_RIGHT);
				}else {
					recordListVo.setFlag(WebConstants.Message.SHOW_MESSAGE_LEFT);
				}
				recordListVos.add(recordListVo);
			}
			return WebConvertObjectUtils.convertObjectToJsonStr(chatContentRecordVo);
		}
		return null;
	}
	/**
	 * 
	 * @Description:接收前端的聊天内容
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月19日下午1:49:07
	 * @version V1.0
	 * @param friendId
	 * @param content
	 * @return
	 */
	
	public String sendUserMsg(String friendId,String content){
		RedisMQProvider provider = RedisClientUtils.getMQProviderConn();
		try{//获取登录人的信息
		UserInfoBean bean =  DwrBaseService.getCurrentUser();
		UserMessage message = new UserMessage();
		message.setContent(content);
		message.setDisposeStatus(ServConstants.System.DISPOSE_STATUS_UN);
		message.setFromUserId(bean.getId());
		message.setIsNotice(ServConstants.System.IS_NOTICE_NO);
		message.setIsUnread(ServConstants.System.IS_UNREAD_YES);
		message.setmType(ServConstants.System.CHAT_MSG_FLAG);
		message.setSendTime(DateUtils.getDateStr());
		message.setToUserId(friendId);
		// 将封装消息的对象 转化为json字符串
		String  messageObject = WebConvertObjectUtils.convertObjectToJsonStr(message);
		provider.publish(WebConstants.Channel.USER_MESSAGE_CHANNEL_NAME, messageObject);
		} catch (RedisException e) {
			logger.error("广播消息失败! - Cause:" + e);
			e.printStackTrace();
			return WebConstants.RESULT.FAILURE;
		}finally {
			provider.release();
		}
		logger.info("发送群组消息已经广播出去了");
		return WebConstants.RESULT.SUCCESS;
	}
	
	/**
	 * 通过好友Id获取好友详细信息
	 * @return
	 */
	
	public String queryUserFriendInfoDetail(String friendId) {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		UserInfoBean userInfo = DwrBaseService.getCurrentUser();
		UserInfoBean friUserInfo = DwrUserSessionManagerUtils.getUserInfoByUserId(friendId);
		FriendInfoBean friendInfo = WebConvertObjectUtils.convertJsonStrToObject(
				jedisConfig.hget(userInfo.getId()+WebConstants.Flag.USERFRIENDSFLAG, friendId), FriendInfoBean.class);
		ContactFriendInfoVo friendInfoDetail = new ContactFriendInfoVo();
		friendInfoDetail.setFriendId(friendId);
		friendInfoDetail.setRegion(friUserInfo.getRegion());
		friendInfoDetail.setRemarks(friendInfo.getRemarks());
		friendInfoDetail.setSource(friendInfo.getSource());
		friendInfoDetail.setUserName(friUserInfo.getUserName());
		friendInfoDetail.setUserPhoto(friUserInfo.getUserPhoto());
		
		jedisConfig.close();
		return WebConvertObjectUtils.convertObjectArrayToJsonStr(friendInfoDetail);
	}
	@Override
	public String modifySessionListStatus(String friendId) {
		Map<String, String>params = new HashMap<>();
		params.put(ServConstants.ConditionUserMsg.IS_UNREAD, ServConstants.System.IS_UNREAD_NO);
		params.put(ServConstants.ConditionUserMsg.TO_USER_ID, getCurrentUser().getId());
		params.put(ServConstants.ConditionUserMsg.FROM_USER_ID, friendId);
		params.put(ServConstants.ConditionUserMsg.IS_NOTICE, ServConstants.System.IS_NOTICE_NO);
		iUserMsgService.modifyUserMsgStatus(params);
		return null;
	}
	/**
	 * 修改好友备注
	 */
	@Override
	public String modifyFriendRemarks(String friendId, String remarks) {
		UserInfoBean userInfo = DwrBaseService.getCurrentUser();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		String friendInfoJSON = jedisConfig.hget(userInfo.getId()+WebConstants.Flag.USERFRIENDSFLAG, friendId) ;
		FriendInfoBean friendInfo = WebConvertObjectUtils.convertJsonStrToObject(friendInfoJSON, FriendInfoBean.class);
		friendInfo.setRemarks(remarks);
		jedisConfig.hset(userInfo.getId()+WebConstants.Flag.USERFRIENDSFLAG, friendId, WebConvertObjectUtils.convertObjectToJsonStr(friendInfo));
		jedisConfig.close();
		return null;
	}
}
