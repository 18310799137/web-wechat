package com.yucheng.im.service.manager.dao;

import java.util.List;

import com.yucheng.im.service.entity.UserInfoBean;

public interface IQueryUserInfoDao {

	List<UserInfoBean> queryAllUserInfo();

}
