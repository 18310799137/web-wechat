package com.yucheng.im.service.manager.service;

import java.util.List;
import java.util.Map;

import com.yucheng.im.service.entity.msg.GroupMessage;

public interface IUserGroupMsgService {

	public List<GroupMessage> queryGroupMsgByPage(Map<String, String> params);

	public boolean addUserGroupMsg(GroupMessage groupMessage);
}
