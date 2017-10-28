package com.yucheng.im.service.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.dwr.util.MessagePushToClientUtils;
import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;
import com.yucheng.im.service.web.util.WebConvertObjectUtils;
import com.yucheng.im.service.web.util.WebStringUtils;

import redis.clients.jedis.Jedis;

public class RequertFilter implements Filter {
	private static Logger logger = Logger.getLogger(RequertFilter.class);
	private static String ENCODING = "utf-8";

	private static String[] resourceSuffix = { "js", "css", "png", "jpg", "htc", "gif" };

	private static String[] directory = { "/css/", "/js/", "/img/", "UserManager/checkUserLogin.htm",
			"UserManager/setUserKey.do", "userlogin/index.html","ReverseAjax.dwr","checkHeartbeat.dwr" };
	private static String[]dwrService= {"DwrUserGroupServiceImpl","DwrUserFriendServiceImpl","DwrUserQueryServiceImpl"};
	

	public void init(FilterConfig filterConfig) throws ServletException {
		String configEncode = filterConfig.getInitParameter("IM-ENCODING");
		if (null != configEncode && !"".equals(configEncode)) {
			ENCODING = configEncode;
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		// 设置编码格式
		httpServletRequest.setCharacterEncoding(ENCODING);
		httpServletResponse.setCharacterEncoding(ENCODING);

		// 获取请求的URL
		StringBuffer url = httpServletRequest.getRequestURL();
		// 获取请求的后缀
		String suffix = url.substring(url.lastIndexOf(".") + 1);
		String sessionId = httpServletRequest.getSession().getId();

		for (String suf : resourceSuffix) {
			if (suf.equals(suffix)) {
				chain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
		}
		logger.debug(sessionId+"请求的URL - " + url);
		for (String folder : directory) {
			if (url.indexOf(folder) != -1) {
				chain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
		}
		

		Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
		//用户数据缓存在配置中心    通过查询配置中心获取
		Jedis jedisConfig  = RedisClientUtils.getRedisConfigSource();


		
		String uuidSession=UUIDGenerateUtils.getLocalHostIpAddress()+"|"+sessionId;
		String userId = jedisCache.get(uuidSession);
		
		if(-1!=url.indexOf("unloadCurrentUser") || -1!=url.indexOf("dwr/call/plaincall/__System.pageUnloaded.dwr")) {
		 	if(WebStringUtils.checkStringIsNull(userId) && DwrBaseService.currentUserMap.containsKey(userId)){
	    		//清除在本应用节点登录的用户信息
	    		UserInfoBean logOut = DwrBaseService.currentUserMap.get(userId);
	    		String userName =logOut.getUserName();
	    		
	    		logger.debug("清除用户 ["+logOut.getId()+" -"+userName+"]  登录信息" );
	    		DwrBaseService.currentUserMap.remove(userId);
	    		
	    		//清空redis 缓存中的会话信息
	    		jedisCache.del(uuidSession);
				jedisCache.del(userId);
	    	}
		 	jedisConfig.close();
		 	jedisCache.close();
		 	return;
		}
		
		if (userId == null) {
			int port = httpServletRequest.getServerPort();
			Long rTime =jedisCache.ttl(uuidSession);
			String sendUrl =null;
			String script =  null;
			if(rTime==-2) {
				script="(function(){alert('会话超时');window.location.href='"+sendUrl+"';window.location.href='"+sendUrl+"';  location.reload(); window.location.assign('"+sendUrl+"');}());";
				jedisCache.del(uuidSession);
				sendUrl ="http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":"+port+"/IM-WEB/userlogin/index.html?TIMEOUT";
			}else {
				script="(function(){alert('访问出错');window.location.href='"+sendUrl+"';window.location.href='"+sendUrl+"';  location.reload(); window.location.assign('"+sendUrl+"');}());";
				sendUrl ="http://"+UUIDGenerateUtils.getLocalHostIpAddressNoReplace()+":"+port+"/IM-WEB/userlogin/index.html?ReqFail";
			}
			Set<String>set = new HashSet<>();
			set.add(sessionId);
			logger.error("已拦截URL - " + url + "\t Will send to - "+sendUrl);
			MessagePushToClientUtils.scriptPushBySessionId(set,script);
			httpServletResponse.sendRedirect(sendUrl);
			jedisCache.close();
			jedisConfig.close();
			return;
		}

		


		UserInfoBean object = WebConvertObjectUtils.convertJsonStrToObject(jedisConfig.get(userId + WebConstants.Flag.USERINFOFLAG),UserInfoBean.class);
		
		for (int i = 0; i < dwrService.length; i++) {
			if(url.toString().contains(dwrService[i])) {
				logger.info("用户["+object.getUserName()+"]    服务端超时时间重新刷新");
					jedisCache.set(uuidSession,userId);
					jedisCache.set(userId,uuidSession);
					jedisCache.expire(uuidSession, WebConstants.Config.SESSION_TIMEOUT);
					jedisCache.expire(userId, WebConstants.Config.SESSION_TIMEOUT);
			}
		}
		jedisCache.close();
		jedisConfig.close();
		if (object instanceof UserInfoBean) {
			UserInfoBean userInfo = (UserInfoBean) object;
			chain.doFilter(httpServletRequest, httpServletResponse);
			logger.debug("用户已登录|已放行的用户信息:" + userInfo);
			return;
		}

	}

	public void destroy() {

	}

}
