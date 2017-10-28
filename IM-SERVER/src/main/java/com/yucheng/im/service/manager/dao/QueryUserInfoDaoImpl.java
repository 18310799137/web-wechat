package com.yucheng.im.service.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.manager.BaseDao;
@Repository("queryUserInfoDaoImpl")
public class QueryUserInfoDaoImpl  extends BaseDao implements  IQueryUserInfoDao {

	@SuppressWarnings("unchecked")
	public List<UserInfoBean> queryAllUserInfo() {
		return (List<UserInfoBean>) queryForList("t_userInfo.queryAllUserInfo");
	}

}
