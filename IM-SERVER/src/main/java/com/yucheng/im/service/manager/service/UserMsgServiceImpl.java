package com.yucheng.im.service.manager.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.manager.BaseService;

@Service("userMsgServiceImpl")
public class UserMsgServiceImpl extends BaseService implements IUserMsgService {

	
	@Override
	public void addUserMsg(UserMessage message) {
		userMsgDao.addUserMsg(message);

	}

	@Override
	public List<UserMessage> queryUserMsgByPage( Map<String, String> params) {
		return userMsgDao.queryUserMsgByPage( params);
	}

	@Override
	public void modifyUserMsgStatus(Map<String, String> params) {
		// TODO Auto-generated method stub
		userMsgDao.modifyUserMsgStatus(params);
	}

	@Override
	public int queryUnreadUserMsgCount(Map<String, String> params) {
		return userMsgDao.queryUnreadUserMsgCount(params);
	}

	@Override
	public List<UserMessage> queryUserReqMsgList(Map<String, String> params) {
		return userMsgDao.queryUserReqMsgList(params);
	}

}
