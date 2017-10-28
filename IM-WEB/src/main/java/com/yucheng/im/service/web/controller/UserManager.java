package com.yucheng.im.service.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;
import com.yucheng.im.service.web.util.WebStringUtils;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("UserManager")
@Scope(value = "prototype")
public class UserManager {

	private static Logger logger = Logger.getLogger(UserManager.class);


	public UserManager() {
	}

	@RequestMapping("checkUserLogin.htm")
	@ResponseBody
	public String checkUserLogin(HttpServletRequest request, String userLoginKey, String userId) {
		boolean checkResult = true;
		/*
		 * try { checkResult = ConnRemoteServer.openRemoteServer(userLoginKey, userId,
		 * WebConstants.REMOTE_CHECKUSER_URL); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		if (checkResult) {

		}
		return "true";
	}

	@RequestMapping("setUserKey.do")
	public String setUserKey(HttpServletRequest request) {
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		// 客户端参数 客户号用户号
		String userId = request.getParameter("userId");
		// 获取sessionId
		String sessionId = request.getSession().getId();
		String uuidSession = UUIDGenerateUtils.getLocalHostIpAddress() + "|" + sessionId;
		int port = request.getServerPort();

		
		if(!WebStringUtils.checkStringIsNull(userId)) {
			String	sendUrl ="http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":"+port+"/IM-WEB/userlogin/index.html?EMPTY_PARAM";
			jedisCache.close();
			jedisConfig.close();
			return "redirect:"+sendUrl;
		}
		String isLogin = jedisCache.get(userId);
		
		//检查用户是否已经登录
		if(WebStringUtils.checkStringIsNull(isLogin)) {
			logger.info("isLogin"+isLogin);
			String	sendUrl ="http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":"+port+"/IM-WEB/userlogin/index.html?ERROR_ALREADY_LOGIN";
			jedisCache.close();
			jedisConfig.close();
			return "redirect:"+sendUrl;
		}
		String UserInfoBeanStr = jedisConfig.get(userId + WebConstants.Flag.USERINFOFLAG);
		//不存在当前用户 则返回登录界面
		if(!WebStringUtils.checkStringIsNull(UserInfoBeanStr)) {
			String	sendUrl ="http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":"+port+"/IM-WEB/userlogin/index.html?ERROR_USERNAME_NOT_EXIST";
			jedisCache.close();
			jedisConfig.close();
			return "redirect:"+sendUrl;
		}
		// 保存会话信息到redis  默认时间10分钟
		jedisCache.set(uuidSession, userId);
		jedisCache.set(userId, uuidSession);
		jedisCache.expire(uuidSession, WebConstants.Config.SESSION_TIMEOUT);
		jedisCache.expire(userId, WebConstants.Config.SESSION_TIMEOUT);

		// TODO 如果一个客户端 使用多个账户登录 要更新sessionMap
		UserInfoBean currentUser = WebConvertObjectUtils
				.convertJsonStrToObject(jedisConfig.get(userId + WebConstants.Flag.USERINFOFLAG), UserInfoBean.class);
		logger.info("Save a Login user      " + userId + " - " + currentUser);
		//保存登录用户信息 到本地内存
		DwrBaseService.currentUserMap.put(userId, currentUser);

		jedisCache.close();
		jedisConfig.close();
		logger.info("保存用户登录到sessionHashMap - uuidSession: " + uuidSession + "\t userId" + userId);
		return "/resource/chat";
	}

	@RequestMapping("getUserKey.do")
	@ResponseBody
	public String getUserKey(HttpServletRequest request) {
		
		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		String sessionId = request.getRequestedSessionId();
		String uuidSession = UUIDGenerateUtils.getLocalHostIpAddress() + "|" + sessionId;
		String userId = jedisCache.get(uuidSession);
		UserInfoBean bean = WebConvertObjectUtils
				.convertJsonStrToObject(jedisConfig.get(userId + WebConstants.Flag.USERINFOFLAG), UserInfoBean.class);
		String resultJson = JSONObject.fromObject(bean).toString();
		jedisCache.close();
		jedisConfig.close();
		logger.info("根据userId获取用户详细信息 getUserInfoByUserId - userId:" + userId + " - Jsonbean:" + resultJson);
		return resultJson;
	}

}
