package com.yucheng.im.service.web.dwr.service.api;

public interface IDwrUserQueryService {

 
	/**
	 * 模糊搜索用户 查询平台用户
	 * @param custUserName
	 * @return
	 */
	  String queryUserInPlatform(String custUserName);
	  /**
	   * 模糊搜索用户 查询好友列表
	   * @param custUserName
	   * @return
	   */
	  String queryUserInContactList(String custUserName);
	/**
	 * 查询登录者的会话列表
	 * @return
	 */
	public abstract String queryUserSessionList();
	
	/**
	 * 
	 * @Description:查询自己的详细信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月18日下午8:25:15
	 * @version V1.0
	 * @return
	 */
	public abstract String queryOneSelfInfo();
	
	String queryUserDetail(String userId, String flag, String groupId);
	

}