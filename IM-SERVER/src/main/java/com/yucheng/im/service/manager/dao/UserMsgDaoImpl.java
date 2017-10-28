package com.yucheng.im.service.manager.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.manager.BaseDao;

@Repository
public class UserMsgDaoImpl extends BaseDao implements IUserMsgDao{

	@Override
	public void addUserMsg(UserMessage message) {
		add("t_userMsg.addUserMsg", message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserMessage> queryUserMsgByPage( Map<String, String> params) {
		return (List<UserMessage>) queryForList("t_userMsg.queryUserMsgByPage", params);
	}

	@Override
	public void modifyUserMsgStatus(Map<String, String> params) {
		modify("t_userMsg.modifyUserMsgStatus", params);
	}

	@Override
	public int queryUnreadUserMsgCount(Map<String, String> params) {
		Integer obj = (Integer) queryForObject("t_userMsg.queryUnreadUserMsgCount", params);
		return  null==obj?0:obj.intValue(); 
	}

	/**
	 * 
	 * 
	 * @Description:查询好友申请列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月14日下午8:32:56
	 * @version V1.0
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserMessage> queryUserReqMsgList(Map<String, String> params) {
		return (List<UserMessage>) queryForList("t_userMsg.queryUserReqMsgList", params);
	}

}
