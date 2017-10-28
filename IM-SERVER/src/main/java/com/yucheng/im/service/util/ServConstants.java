package com.yucheng.im.service.util;

/***
 * 
 * @Description:数据库相关操作 所用到的常量
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月13日上午9:27:21
 * @version V1.0
 */
public interface ServConstants {

	static interface System {
		/** 是否为通知消息 */
		String IS_NOTICE_YES = "Y";
		/** 是否为通知消息 */
		String IS_NOTICE_NO = "N";

		/** 是否为未读消息 Y为未读消息,N为已读消息 */
		String IS_UNREAD_YES = "Y";
		/** 是否为未读消息 Y为未读消息,N为已读消息 */
		String IS_UNREAD_NO = "N";

		/** 如果为系统消息, 操作为 R删除成员,A增加成员 N在非系统消息时占位 */
		String OPR_TYPE_REM = "R";
		/** 如果为系统消息, 操作为 R删除成员,A增加成员 N在非系统消息时占位 */
		String OPR_TYPE_ADD = "A";
		/** 如果为系统消息, 操作为 R删除成员,A增加成员 N在非系统消息时占位 */
		String OPR_TYPE_NO = "N";
		/** 如果为系统消息, 操作为 R删除成员,A增加成员 N在非系统消息时占位 C表示创建了新群*/
		String OPR_TYPE_CREATE = "C";
		/** 如果为系统消息, 操作为 R删除成员,A增加成员 N在非系统消息时占位 C表示创建了新群 M表示修改了群信息 E为退出群聊*/
		String OPR_TYPE_MODIFY = "M";
		String OPR_TYPE_EXIT = "E";

		/** 用户请求类消息 Y表示同意申请，N表示拒绝申请，U表示未处理 */
		String DISPOSE_STATUS_YES = "Y";
		/** 用户请求类消息 Y表示同意申请，N表示拒绝申请，U表示未处理 */
		String DISPOSE_STATUS_NO = "N";
		/** 用户请求类消息 Y表示同意申请，N表示拒绝申请，U表示未处理 */
		String DISPOSE_STATUS_UN = "U";

		//对应ConditionUserMsg mType字段取值范围
		String REQ_MSG_FLAG = "R";
		String RESP_MSG_FLAG = "P";
		String CHAT_MSG_FLAG = "C";

	}
	static interface PAGE{
		String PAGE_SIZE_STR="pageSize";
		String NOW_PAGE_STR="nowPage";
		String DEFAULT_NOW_PAGE="1";
		String DEFAULT_PAGE_SIZE="10";
		String DEFAULT_MAX_UNREAD_SIZE="1000";
	}
	/**
	 * 
	 * @Description:数据库群组消息查询条件中所用到的相关常量
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月13日上午9:27:48
	 * @version V1.0
	 */
	static interface ConditionGroupMsg {
		String UUID = "uuid";
		String FROM_USER_ID = "fromUserId";
		String TO_GROUP_ID ="toGroupId";
		String IS_NOTICE = "isNotice";
		String OPR_TYPE ="oprType";
		String CONTENT = "content";
		String IS_UNREAD = "isUnread";
		String SEND_TIME = "sendTime";
	}
	/**
	 * 
	 * @Description:数据库用户消息查询条件中所用到的相关常量
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月13日上午9:27:48
	 * @version V1.0
	 */
	static interface ConditionUserMsg {
		String UUID = "uuid";
		String FROM_USER_ID = "fromUserId";
		String TO_USER_ID = "toUserId";
		String IS_NOTICE = "isNotice";
		String CONTENT = "content";
		String M_TYPE = "mType";
		String DISPOSE_STATUS = "disposeStatus";
		String IS_UNREAD = "isUnread";
		String SEND_TIME = "sendTime";
	}
	/**
	 * 
	 * @Description:数据库用户消息查询条件中所用到的相关常量
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月13日上午9:27:48
	 * @version V1.0
	 */
	static interface ConditionUserMemberMsg {
		String UUID = "uuid";
		String GROUP_ID = "groupId";
		String MEMBER_ID ="memberId";
		String UNREAD_NUM = "unreadNum";
	}
	static interface OrderField{
		/**使用发送时间倒序排序*/
		String ORDER_FIELD_SEND_TIME_DESC=" ORDER BY SEND_TIME DESC";
		/**使用发送时间正序排序*/
		String ORDER_FIELD_SEND_TIME_ASC=" ORDER BY SEND_TIME ASC";
		/**是否使用排序条件 使用的话则在paramMap中存入这个字段为Key,使用上面的倒序语句作为排序条件*/
		String ORDER_CONDITION="orderCondition";
	}
}
