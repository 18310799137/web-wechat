package com.yucheng.im.service.manager.dao;

import org.springframework.stereotype.Repository;

import com.yucheng.im.service.manager.BaseDao;

@Repository
public class InitTable   extends BaseDao {

	/**
	 * 
	* @Title: queryUserFriendTableIsExist 
	* @Description: 判断用户消息表是否存在
	* @param @return
	* @return int
	* @throws
	 */
	public int queryUserFriendTableIsExist() {
		return	(int) queryForObject("t_initTable.queryUserFriendTableIsExist");
	}
	/**
	 * 
	* @Title: createUserFriendTableIsExist 
	* @Description: 创建用户消息表
	* @param 
	* @return void
	* @throws
	 */
	public void createUserFriendTableIsExist() {
		modify("t_initTable.createUserFriendMsgTable");
	}
	public void dropUserFriendTable() {
		modify("t_initTable.dropUserMsgTables");
	}
	public int queryUserGroupTableIsExist() {
		return	(int) queryForObject("t_initTable.queryUserGroupMsgTableIsExist");
	}
	/**
	 * 
	 * @Title: createUserFriendTableIsExist 
	 * @Description: 创建用户消息表
	 * @param 
	 * @return void
	 * @throws
	 */
	public void createUserGroupTableIsExist() {
		modify("t_initTable.createUserGroupMsgTable");
	}
	public void dropUserGroupTable() {
		modify("t_initTable.dropUserGroupMsgTables");
	}

	
	public void createGroupMemMsgTable() {
		modify("t_initTable.createGroupMemMsgTable");
	}
	public void dropGroupMemMsgTables() {
		modify("t_initTable.dropGroupMemMsgTables");
	}
	public int queryGroupMemMsgTable() {
		return	(int) queryForObject("t_initTable.queryGroupMemMsgTable");
	}
	
	
}
