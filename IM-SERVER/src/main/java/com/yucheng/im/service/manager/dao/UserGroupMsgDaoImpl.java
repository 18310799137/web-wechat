package com.yucheng.im.service.manager.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.manager.BaseDao;
@Repository
public class UserGroupMsgDaoImpl extends BaseDao implements IUserGroupMsgDao {

	/**
	 * 
	 * 
	 * @Description: 查询用户消息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:44:28
	 * @version V1.0
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GroupMessage> queryGroupMsgByPage(Map<String, String> params) {
		return (List<GroupMessage>) queryForList("t_userGroupMsg.queryGroupMsgByPage",params);
	}

	/**
	 * 
	 * 
	 * @Description: 添加用户群组的消息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:44:28
	 * @version V1.0
	 * @return
	 */
	public boolean addUserGroupMsg(GroupMessage groupMessage) {
		return add("t_userGroupMsg.addUserGroupMsg",groupMessage);
	}

}
