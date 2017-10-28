package com.yucheng.im.service.manager.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yucheng.im.service.entity.msg.GroupMemMsgStatus;
import com.yucheng.im.service.manager.BaseDao;

@Repository
public class GroupMemMsgDaoImpl extends BaseDao implements IGroupMemMsgDao{

	@Override
	public int queryUnreadMsgCount(Map<String, String> params) {
		Integer obj = (Integer) queryForObject("t_GroupMemMsg.queryGroupMemMsgUnreadCount", params);
		return  null==obj?-1:obj.intValue(); 
	}

	@Override
	public void addGroupMemMsg(GroupMemMsgStatus groupMemMsg) {
		add("t_GroupMemMsg.addGroupMemMsg", groupMemMsg);
	}

	@Override
	public void modifyGroupMemMsgUnreadCount(Map<String, String> params) {
		modify("t_GroupMemMsg.modifyGroupMemMsgUnreadCount", params);
	}

	@Override
	public int queryAllGroupMemMsgUnreadCount(Map<String, String> params) {
		Integer obj = (Integer) queryForObject("t_GroupMemMsg.queryAllGroupMemMsgUnreadCount", params);
		return  null==obj?0:obj.intValue();
	}

	@Override
	public void insertGroupMemMsgUnreadCount(Map<String, String> params) {
		modify("t_GroupMemMsg.insertGroupMemMsgUnreadCount",params);
	}

}
