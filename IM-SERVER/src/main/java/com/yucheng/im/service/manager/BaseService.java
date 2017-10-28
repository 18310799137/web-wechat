package com.yucheng.im.service.manager;

import javax.annotation.Resource;

import com.yucheng.im.service.manager.dao.IGroupMemMsgDao;
import com.yucheng.im.service.manager.dao.IQueryUserInfoDao;
import com.yucheng.im.service.manager.dao.IUserGroupMsgDao;
import com.yucheng.im.service.manager.dao.IUserMsgDao;

public class BaseService {
  
	 
	@Resource
	protected IQueryUserInfoDao iQueryUserInfoDao;
	
	@Resource
	protected IUserMsgDao userMsgDao;
	
	@Resource
	protected IUserGroupMsgDao groupMsgDao;
	
	@Resource
	protected IGroupMemMsgDao groupMemMsgDao;
}
