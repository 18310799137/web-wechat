package com.yucheng.im.service.manager.dao;

import java.util.List;
import java.util.Map;

import com.yucheng.im.service.entity.msg.GroupMessage;


public interface IUserGroupMsgDao {
	List<GroupMessage>queryGroupMsgByPage(Map<String, String>params);
	
	boolean addUserGroupMsg(GroupMessage groupMessage);
}
