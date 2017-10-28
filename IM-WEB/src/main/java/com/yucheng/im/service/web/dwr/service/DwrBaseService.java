package com.yucheng.im.service.web.dwr.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.directwebremoting.WebContextFactory;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.manager.service.IGroupMemMsgService;
import com.yucheng.im.service.manager.service.IUserGroupMsgService;
import com.yucheng.im.service.manager.service.IUserMsgService;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.util.DwrUserSessionManagerUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;

import redis.clients.jedis.Jedis;
/**
 * 
* @Title: DwrBaseService.java  
* @Package com.yucheng.im.service.web.dwr.service  
* @Description: dwr服务索要使用的第三方工具类
* @author zhanggh@yusys.com.cn
* @date 2017年8月31日  
* @version V1.0
 */
public class DwrBaseService {

	/**用户消息类型 service*/
	@Resource
	protected IUserMsgService iUserMsgService;
	
	/**群组消息类型 service*/
	@Resource
	protected IUserGroupMsgService groupMsgService;
	
	/**群成员消息个数处理类*/
	@Resource
	protected IGroupMemMsgService memMsgService;
	
	//统计当前节点登录用户信息的计数器,包含本节点所有登录用户信息<userId,UserInfoBean>格式存储
	public static ConcurrentHashMap<String, UserInfoBean>currentUserMap = new ConcurrentHashMap<>();
	/**
	 * 
	 * @Description:获取当前登录用户
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年10月12日下午1:04:36
	 * @version V1.0
	 * @return
	 */
	protected static UserInfoBean getCurrentUser() {
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		String uuidSession=UUIDGenerateUtils.getLocalHostIpAddress()+"|"+WebContextFactory.get().getSession().getId();
		//从缓存中获取 sessionId 所对应的用户信息. 缓存中数据结构为[hash key (field)sessionId - (value)userId ]
		String userId =jedisCache.get(uuidSession);
		
		UserInfoBean currentUser = currentUserMap.get(userId);
		if(currentUser.getId().equals(userId)) {
			jedisCache.close();
			return currentUser;
		}jedisCache.close();
		return	DwrUserSessionManagerUtils.getLoginUserInfoBySessionId();
	}
	
}
