package com.yucheng.im.service.web.listener;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.util.UUIDGenerateUtils;
import com.yucheng.im.service.web.dwr.service.DwrBaseService;
import com.yucheng.im.service.web.util.RedisClientUtils;

import redis.clients.jedis.Jedis;

public class HandleScriptSessionLinstener implements ScriptSessionListener {

	private static Logger logger = Logger.getLogger(HandleScriptSessionLinstener.class);
	private static  ConcurrentHashMap<String, ScriptSession> scriptSessionMap = new ConcurrentHashMap<String, ScriptSession>();  
	   /** 
	    * ScriptSession 创建时触发 
	    */  
	   public void sessionCreated(ScriptSessionEvent ev) {  
	       String sessionId = WebContextFactory.get().getSession().getId();
	       logger.debug("HandleScriptSessionLinstener Will Push SessionId To Map - "+sessionId);
	       scriptSessionMap.put(sessionId, ev.getSession());  
	    }  
	    /** 
	     * ScriptSession销毁时触发 
	     */  
	    public void sessionDestroyed(ScriptSessionEvent ev) {
	    	String sessionId = WebContextFactory.get().getSession().getId();
	    	Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
			String uuidSession=UUIDGenerateUtils.getLocalHostIpAddress()+"|"+sessionId;
			//从缓存中获取 sessionId 所对应的用户信息. 缓存中数据结构为[hash key (field)sessionId - (value)userId ]
			String userId =jedisCache.get(uuidSession);
			
			
		
	    	if(DwrBaseService.currentUserMap.containsKey(userId)){
	    		//清除在本应用节点登录的用户信息
	    		UserInfoBean logOut = DwrBaseService.currentUserMap.get(userId);
	    		String userName =logOut.getUserName();
	    		
	    		logger.debug("HandleScriptSessionLinstener Will Remove User ["+logOut.getId()+" -"+userName+"]");
	    		DwrBaseService.currentUserMap.remove(userId);
	    		
	    		//清空redis 缓存中的会话信息
	    		jedisCache.del(uuidSession);
				jedisCache.del(userId);
	    	}
	    	jedisCache.close();
	    	scriptSessionMap.remove(WebContextFactory.get().getSession().getId());  
	    }  
	    /** 获取所有的scriptsession */  
	    public static Collection<ScriptSession> getAllSctiptSessions() {  
	        return scriptSessionMap.values();
	    } 
  

}
