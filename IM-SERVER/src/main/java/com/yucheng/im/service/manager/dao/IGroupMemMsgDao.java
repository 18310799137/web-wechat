package com.yucheng.im.service.manager.dao;

import java.util.Map;

import com.yucheng.im.service.entity.msg.GroupMemMsgStatus;

public interface IGroupMemMsgDao {
	public void addGroupMemMsg(GroupMemMsgStatus groupMemMsg);
	
	public int queryUnreadMsgCount(Map<String,String>params);
	
	public int queryAllGroupMemMsgUnreadCount(Map<String,String>params);

	public void modifyGroupMemMsgUnreadCount(Map<String,String>params);
	
	public void insertGroupMemMsgUnreadCount(Map<String,String>params);
}
