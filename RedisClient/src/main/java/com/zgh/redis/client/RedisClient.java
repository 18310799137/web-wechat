package com.zgh.redis.client;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
/**
 * 
* @ClassName: RedisClient 
* @Description: 连接redis哨兵 工具类
* @author zhanggh@yusys.com.cn
* @date 2017年8月27日 下午11:45:39 
*
*/
public class RedisClient {

	/**
	 * 
	* @Title: main 
	* @Description: TODO
	* @param @param args
	* @return void
	* @throws
	 */
	public static void main(String[] args) {
	        Set<String> sentinels = new HashSet<String>();
	        sentinels.add(new HostAndPort("192.168.106.128", 26379).toString());
	        sentinels.add(new HostAndPort("192.168.106.132", 26379).toString());
	        sentinels.add(new HostAndPort("192.168.106.133", 26379).toString());
	        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	        poolConfig.setMaxIdle(10);
	        poolConfig.setMaxTotal(1000);
	        poolConfig.setMaxWaitMillis(10000);
	        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster1", sentinels,poolConfig);
	        System.out.println("Current master: " + sentinelPool.getCurrentHostMaster().toString());
	        Jedis redisClient = sentinelPool.getResource();
	        //redisClient.set("hello", "world-2");
	        System.out.println(redisClient.get("hello"));;
	        redisClient.close();
	        sentinelPool.close();
	        sentinelPool.destroy();
	}
}
