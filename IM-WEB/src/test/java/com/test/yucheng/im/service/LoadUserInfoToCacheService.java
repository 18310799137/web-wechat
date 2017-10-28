package com.test.yucheng.im.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.manager.dao.IQueryUserInfoDao;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring/applicationContext.xml")
public class LoadUserInfoToCacheService extends TestCase{

	private Logger logger = Logger.getLogger(LoadUserInfoToCacheService.class);
	@Resource(name="queryUserInfoDaoImpl")
	 private IQueryUserInfoDao queryUserInfoDao;

	private Jedis jedis = RedisClientUtils.getRedisCacheSource();
	@Test
	public void loadUserInfoToCache(){
		 List<UserInfoBean> beans =	queryUserInfoDao.queryAllUserInfo();
		for (UserInfoBean userInfoBean : beans) {
			userInfoBean.setId(userInfoBean.getCstNo()+userInfoBean.getUserNo());
			String userId = userInfoBean.getId()+WebConstants.Flag.USERINFOFLAG;
			logger.info(userId+"=========================================="+userInfoBean);
			System.out.println(userId+"=========================================="+userInfoBean);
			jedis.set(userId, WebConvertObjectUtils.convertObjectToJsonStr(userInfoBean));
		}
	}
}
