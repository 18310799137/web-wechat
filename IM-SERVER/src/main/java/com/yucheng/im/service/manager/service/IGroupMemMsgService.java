package com.yucheng.im.service.manager.service;

import java.util.Map;

import com.yucheng.im.service.entity.msg.GroupMemMsgStatus;

public interface IGroupMemMsgService {
	public void addGroupMemMsg(GroupMemMsgStatus groupMemMsg);
	
	public int queryUnreadMsgCount(Map<String,String>params);
	public int queryAllGroupMemMsgUnreadCount(Map<String,String>params);
	
	public void modifyGroupMemMsgUnreadCount(Map<String,String>params);
	
	public void insertGroupMemMsgUnreadCount(Map<String,String>params);
}
