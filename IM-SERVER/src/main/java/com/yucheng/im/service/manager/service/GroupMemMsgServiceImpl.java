package com.yucheng.im.service.manager.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.yucheng.im.service.entity.msg.GroupMemMsgStatus;
import com.yucheng.im.service.manager.BaseService;

@Service("groupMemMsgServiceImpl")
public class GroupMemMsgServiceImpl extends BaseService implements IGroupMemMsgService{

	@Override
	public void addGroupMemMsg(GroupMemMsgStatus groupMemMsg) {
		groupMemMsgDao.addGroupMemMsg(groupMemMsg);
	}

	@Override
	public int queryUnreadMsgCount(Map<String, String> params) {
		return groupMemMsgDao.queryUnreadMsgCount(params);
	}

	@Override
	public void modifyGroupMemMsgUnreadCount(Map<String, String> params) {
		groupMemMsgDao.modifyGroupMemMsgUnreadCount(params);
	}

	@Override
	public int queryAllGroupMemMsgUnreadCount(Map<String, String> params) {
		return groupMemMsgDao.queryAllGroupMemMsgUnreadCount(params);
	}

	@Override
	public void insertGroupMemMsgUnreadCount(Map<String, String> params) {
		groupMemMsgDao.insertGroupMemMsgUnreadCount(params);
	}

}
