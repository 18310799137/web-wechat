package com.yucheng.im.service.web.dwr.service.api;

/**
 * 
 * @Title: IDwrUserAddService.java
 * @Package com.yucheng.im.service.web.dwr
 * @Description: 用户请求添加好友发送验证信息 ,对方验证结果,处理验证信息
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月22日 下午2:27:46
 * @version V1.0
 * 
 */
public interface IDwrUserFriendService {

	/**
	 * 
	 * @Description: 请求添加好友
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午2:53:35
	 * @version V1.0
	 * @param friendId 请求添加好友的id
	 * @param reqMessage 验证消息
	 * @return
	 */
	 String addUserReq(String friendId, String reqMessage);

	/**
	 * 
	 * @Description: 处理用户请求
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午2:30:10
	 * @version V1.0
	 * @param friendId 被处理人的id
	 * @param userOpr  验证结果
	 * @return
	 */
	 String addUserResp(String friendId, String userOpr);
	
	 /**
	  * 
	  * @Description: 查询用户好友列表
	  * @author zhanggh@yusys.com.cn
	  * @date 2017年8月22日 下午5:28:49
	  * @version V1.0
	  * @return List<FriendInfoBean>
	  */
	 String queryUserFriendList(); 
		/***
		 * 查询用户的好友申请列表
		 * @return
		 */
		public abstract String queryUserReqList();
		/**
		 * 修改好友申请列表的状态
		 */
		public abstract String modifyReqListStatus();
		/**
		 * 修改  群组和好友的消息状态
		 * @return
		 */
		public abstract String modifyUserMsgStatus();
		/**
		 * 查询用户的聊天记录
		 */
		public abstract String queryFriendChatRecordList(String friendId,String nowPage,String pageSize);
		/**
		 * 
		 */
		public abstract String sendUserMsg(String friendId,String content);
		
		
		
		
		String modifySessionListStatus(String friendId);
		
		String modifyFriendRemarks(String friendId,String remarks);
		
}