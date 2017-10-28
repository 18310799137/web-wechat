package com.yucheng.im.service.web.init;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.manager.dao.IQueryUserInfoDao;
import com.yucheng.im.service.manager.service.init.InitTableService;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.SolrUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

/**
 * 
 * @Title: LoadUserInfoToCacheService.java
 * @Package com.yucheng.im.service.init.data.execute
 * @Description: 加载用户数据到缓存,添加用户索引信息到solr
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月21日 下午1:31:30
 * @version V1.0
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring/applicationContext.xml")
public class LoadUserInfoToCacheService extends TestCase {

	private Logger logger = Logger.getLogger(LoadUserInfoToCacheService.class);
	@Resource(name = "queryUserInfoDaoImpl")
	private IQueryUserInfoDao queryUserInfoDao;

	@Resource(name = "initTableService")
	private InitTableService initTableService;

	/**
	 * 
	 * @Description:清空会话信息  (用户登录信息)
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月12日下午2:02:57
	 * @version V1.0
	 */
	@Test
	public void flushRedisCache() {
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		jedisCache.flushAll();
		jedisCache.close();
	}
	
	
	/**
	 * 
	 * @Description:清空Redis 配置中心数据
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月12日下午2:01:55
	 * @version V1.0
	 */
	@Test
	public void flushRedisConfig() {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		jedisConfig.flushAll();
		jedisConfig.close();
	}
	
	/**
	 * 
	 * @Description:加载数据到Redis 配置中心,添加索引到solr服务器
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月12日下午2:02:13
	 * @version V1.0
	 */
	@Test
	public void loadUserInfoToCache() {
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		jedisConfig.flushAll();
		jedisCache.flushAll();
		List<UserInfoBean> beans = queryUserInfoDao.queryAllUserInfo();
		for (UserInfoBean userInfoBean : beans) {
			userInfoBean.setId(userInfoBean.getCstNo() + userInfoBean.getUserNo());
			userInfoBean.setRegion(userInfoBean.getNameCn());
			if(null!=userInfoBean.getUserPhoto() || !"".equals(userInfoBean.getUserPhoto())) {
				userInfoBean.setUserPhoto("http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":8080/IM-WEB/resource/img/tx/tx5.jpg");
			}
			String userId = userInfoBean.getId() + WebConstants.Flag.USERINFOFLAG;
			logger.info("用户信息导入userId:" + userId + " - userInfoBean:" + userInfoBean);
			jedisConfig.set(userId, WebConvertObjectUtils.convertObjectToJsonStr(userInfoBean));
		}
		jedisCache.close();
		jedisConfig.close();
		SolrUtils.emptyIndex();
		SolrUtils.addUserInfo(beans);
	}
	@Test
	public void initMsgTables() {
		initTableService.initTables();
	}
}
