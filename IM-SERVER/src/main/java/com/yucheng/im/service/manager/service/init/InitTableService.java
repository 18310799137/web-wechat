package com.yucheng.im.service.manager.service.init;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.yucheng.im.service.manager.dao.InitTable;

@Service("initTableService")
public class InitTableService {

	private static Logger logger = Logger.getLogger(InitTableService.class);
	@Resource
	private InitTable initTable;
	
	public void initTables() {
		int i =initTable.queryUserFriendTableIsExist();
		int g=initTable.queryUserGroupTableIsExist();
		int m=initTable.queryGroupMemMsgTable();
		if(i==1) {
			initTable.dropUserFriendTable();
			logger.debug("UserFriendTable表创建");
			initTable.createUserFriendTableIsExist();
		}
		if(g==1) {
			initTable.dropUserGroupTable();
			logger.debug("UserGroupTable表创建");
			initTable.createUserGroupTableIsExist();
		}
		if(m==1) {
			initTable.dropGroupMemMsgTables();
			logger.debug("GroupMemMsgTable表创建");
			initTable.createGroupMemMsgTable();
		}
		
	}
}
