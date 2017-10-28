package com.yucheng.im.service.manager.service;

import java.util.List;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.manager.BaseService;

public class QueryUserInfoServiceImpl extends BaseService implements IQueryUserInfoService {

	
	/**
	 * 查询所有用户信息 加载到Redis缓存
	 */
	public List<UserInfoBean> queryAllUserInfo() {
		return iQueryUserInfoDao.queryAllUserInfo();
	}

}
