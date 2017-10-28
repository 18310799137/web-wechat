package com.yucheng.im.service.web.dwr.util;

import org.directwebremoting.WebContextFactory;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;

import redis.clients.jedis.Jedis;

/**
 * 
* @Title: DwrUserSessionManagerUtils.java
* @Package com.yucheng.im.service.web.dwr.util
* @Description: 用户进行操作时,不需要传递操作人标识,使用dwr获取当前登录用户
* @author zhanggh@yusys.com.cn
* @date 2017年8月22日 下午5:56:06
* @version V1.0
*
 */
public class DwrUserSessionManagerUtils {
	//private static Logger logger = Logger.getLogger(DwrUserSessionManagerUtils.class);
	
	

	/**    
	 * @Description: 获取当前登录用户信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 上午10:09:30
	 * @version V1.0
	 */
	public static UserInfoBean getLoginUserInfoBySessionId() {
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		//获取登录用户的sessionId
		String sessionId = WebContextFactory.get().getSession().getId();
		String uuidSession=UUIDGenerateUtils.getLocalHostIpAddress()+"|"+sessionId;
		//从缓存中获取 sessionId 所对应的用户信息. 缓存中数据结构为[hash key (field)sessionId - (value)userId ]
		UserInfoBean bean =   WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get((jedisCache.get(uuidSession) + WebConstants.Flag.USERINFOFLAG)),UserInfoBean.class);
		//logger.debug("获取当前登录用户信息 sessionId - " + sessionId + "\t  - userId:"+ jedisCache.get(uuidSession)+" - bean "+bean);
		jedisCache.close();
		jedisConfig.close();
		
		return bean;
	}
	/**
	 * @Description: 获取用户详细信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 上午10:09:30
	 * @version V1.0
	 */
	public static UserInfoBean getUserInfoByUserId(String userId) {
			Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
			UserInfoBean bean =  WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(userId + WebConstants.Flag.USERINFOFLAG),UserInfoBean.class);
			jedisConfig.close();
			//logger.debug("根据userId获取用户详细信息 getUserInfoByUserId - userId:" + userId+" - bean:"+bean);
			return bean;
	}
}
