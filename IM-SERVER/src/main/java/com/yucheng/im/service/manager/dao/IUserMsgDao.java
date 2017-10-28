package com.yucheng.im.service.manager.dao;

import java.util.List;
import java.util.Map;

import com.yucheng.im.service.entity.msg.UserMessage;

public interface IUserMsgDao {

	/**保存用户消息*/
	void addUserMsg(UserMessage message);
	
	/**查询用户请求消息*/
	List<UserMessage>queryUserMsgByPage(Map<String, String>params);
	/**
	 * 
	 * 
	 * @Description:查询好友申请列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月14日下午8:31:47
	 * @version V1.0
	 * @param params
	 * @return
	 */
	List<UserMessage>queryUserReqMsgList(Map<String, String>params);
	/**修改用户请求消息状态*/
	void modifyUserMsgStatus(Map<String,String>params);
	
	/**查询未读消息数量*/
	int queryUnreadUserMsgCount(Map<String,String>params);
}
