package com.yucheng.im.service.web.mq.thread.task;

import org.springframework.web.context.WebApplicationContext;

import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;

import redis.clients.jedis.Jedis;

public abstract class AbstractThreadTask extends Thread{

	public abstract void  notifyThisTask();
	
	protected Jedis jedisCache = RedisClientUtils.getRedisCacheSource();
	protected Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
	
	public static  WebApplicationContext context=null;
	
	/**
	 * 添加会话信息到会话列表中
	 * @param oneselfId
	 * @param friendId
	 * @param flag
	 * @return
	 */
	protected String addUserChatToSessionList(String oneselfId,String friendId,String flag) {
		 Jedis jedisConfig = RedisClientUtils.getRedisConfigSource();
		
		
		//先删除会话列表中已经存在的会话id
		jedisConfig.lrem(oneselfId+WebConstants.Flag.USERSESSIONFLAG, 0, friendId+"|"+flag);
		//将最后一次会话的 id排在最前面
		jedisConfig.lpush(oneselfId+WebConstants.Flag.USERSESSIONFLAG, friendId+"|"+flag);
		jedisConfig.close();
		return WebConstants.RESULT.SUCCESS;
	}

}
