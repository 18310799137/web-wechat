package com.yucheng.im.service.manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.manager.BaseService;
@Service("userGroupMsgServiceImpl")
public class UserGroupMsgServiceImpl  extends BaseService implements IUserGroupMsgService{

	public List<GroupMessage> queryGroupMsgByPage(Map<String, String> params) {
		return groupMsgDao.queryGroupMsgByPage(params);
	}

	public boolean addUserGroupMsg(GroupMessage groupMessage) {
		return groupMsgDao.addUserGroupMsg(groupMessage);
	}

}
