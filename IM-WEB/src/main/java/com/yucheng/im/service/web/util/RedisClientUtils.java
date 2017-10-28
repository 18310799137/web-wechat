package com.yucheng.im.service.web.util;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.com.yusys.redis.client.pool.RedisSentinelPool;
import cn.com.yusys.redis.mq.RedisMQConsumer;
import cn.com.yusys.redis.mq.RedisMQProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 
* @ClassName: RedisClientUtils 
* @Description: 连接Redis的客户端工具类
* @author zhanggh@yusys.com.cn
* @date 2017年9月2日 下午5:39:25 
*
 */
public class RedisClientUtils {
	private static Logger logger = Logger.getLogger(RedisClientUtils.class);
	private static JedisPoolConfig JEDIS_POOL_CONFIG = null;
	private static Set<String> SENTINELS_CACHE = null;
	private static Set<String> SENTINELS_CONFIG = null;
	private static Set<String> SENTINELS_MQ = null;
	private static JedisSentinelPool REDIS_SENTINEL_POOL_CACHE = null;
	private static JedisSentinelPool REDIS_SENTINEL_POOL_CONFIG = null;
	private static RedisSentinelPool REDIS_SENTINEL_POOL_MQ = null;
	private static String CACHE_GROUP_NAME = null;
	private static String CONFIG_GROUP_NAME = null;
	private static String MQ_GROUP_NAME = null;
	static{
		JEDIS_POOL_CONFIG = new JedisPoolConfig();
		JEDIS_POOL_CONFIG.setMaxWaitMillis(10000);
	      //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
	      JEDIS_POOL_CONFIG.setBlockWhenExhausted(true);
	      //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
	      JEDIS_POOL_CONFIG.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
	      //是否启用后进先出, 默认true
	      JEDIS_POOL_CONFIG.setLifo(true);
	      //最大空闲连接数
	      JEDIS_POOL_CONFIG.setMaxIdle(10);
	      //最大连接数
	      JEDIS_POOL_CONFIG.setMaxTotal(30);
	      //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	      JEDIS_POOL_CONFIG.setMaxWaitMillis(-1);
	      //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
	      JEDIS_POOL_CONFIG.setMinEvictableIdleTimeMillis(1800000);
	      //最小空闲连接数, 默认0
	      JEDIS_POOL_CONFIG.setMinIdle(5);
	      //每次逐出检查时 逐出的最大数目
	      JEDIS_POOL_CONFIG.setNumTestsPerEvictionRun(2);
	      //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
	      JEDIS_POOL_CONFIG.setSoftMinEvictableIdleTimeMillis(1800000);
	      //在获取连接的时候检查有效性, 默认false
	      JEDIS_POOL_CONFIG.setTestOnBorrow(false);
	      //在空闲时检查有效性, 默认false
	      JEDIS_POOL_CONFIG.setTestWhileIdle(true);
	      //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
	       JEDIS_POOL_CONFIG.setTimeBetweenEvictionRunsMillis(60000);
	      	int timeout=20000;
		SENTINELS_CACHE = new HashSet<String>();
		SENTINELS_CONFIG = new HashSet<String>();
		SENTINELS_MQ = new HashSet<String>();
		InputStream inputStream = null;
		inputStream = RedisClientUtils.class.getResourceAsStream(WebConstants.Config.REDIS_CONF_FILE_PATH);
		
		Properties properties = new Properties();
		try {
			logger.debug("读取 Redis 配置文件 - path:"+WebConstants.Config.REDIS_CONF_FILE_PATH);
			properties.load(inputStream);
		} catch (Exception e) {
			logger.error("读取 Redis配置文件 [redis-config.properties]失败!",e);
		}
		Set<String>keySet = properties.stringPropertyNames();
		Iterator<String>ite =	keySet.iterator();
		while(ite.hasNext()){
			String key = ite.next();
				//判断是否为缓存配置
			if(WebConstants.Config.REDISCACHE.equalsIgnoreCase(key)){
				//获取缓存的哨兵地址
				String [] value = properties.getProperty(key).split("\\,");
				for (int i = 0; i < value.length; i++) {
					SENTINELS_CACHE.add(value[i]);
				}
			}
			//判断是否为配置中心
			else if(WebConstants.Config.REDISCONFIG.equalsIgnoreCase(key)){
				//获取配置中心的哨兵地址
				String [] value = properties.get(key).toString().split("\\,");
				for (int i = 0; i < value.length; i++) {
					SENTINELS_CONFIG.add(value[i]);
				}
			}
			//判断配置是否是MQ的地址
			else if(WebConstants.Config.REDISMQ.equalsIgnoreCase(key)){
				//获取MQ的哨兵地址
				String [] value = properties.get(key).toString().split("\\,");
				for (int i = 0; i < value.length; i++) {
					SENTINELS_MQ.add(value[i]);
				}
			}
			else if(WebConstants.Config.REDISMQGROUP.equalsIgnoreCase(key)){
				//获取MQ的集群名称
				MQ_GROUP_NAME = properties.get(key).toString();
				
			}
			else if(WebConstants.Config.REDISCACHEGROUP.equalsIgnoreCase(key)){
				//获取缓存的集群名称
				CACHE_GROUP_NAME = properties.get(key).toString();
				
			}
			else if(WebConstants.Config.REDISCONFIGGROUP.equalsIgnoreCase(key)){
				//获取配置中心的集群名称
				CONFIG_GROUP_NAME = properties.get(key).toString();
				 
			}
		}
		logger.debug("SentinelCacheIp:"+SENTINELS_CACHE+"   -   RedisCacheClusterName:"+CACHE_GROUP_NAME);
		logger.debug("SentinelConfigIp:"+SENTINELS_CONFIG+"   -   RedisConfigClusterName:"+CONFIG_GROUP_NAME);
		logger.debug("SentinelMQIp:"+SENTINELS_MQ+"   -   RedisMQClusterName:"+MQ_GROUP_NAME);
		REDIS_SENTINEL_POOL_CACHE = new JedisSentinelPool(CACHE_GROUP_NAME,SENTINELS_CACHE,
				JEDIS_POOL_CONFIG,timeout);
		REDIS_SENTINEL_POOL_CONFIG = new JedisSentinelPool(CONFIG_GROUP_NAME,SENTINELS_CONFIG,
				JEDIS_POOL_CONFIG,timeout);
		REDIS_SENTINEL_POOL_MQ = new RedisSentinelPool(SENTINELS_MQ,MQ_GROUP_NAME,
				JEDIS_POOL_CONFIG);

	}

	public static Jedis getRedisCacheSource() {
		return REDIS_SENTINEL_POOL_CACHE.getResource();
	}
	public static Jedis getRedisConfigSource() {
		return REDIS_SENTINEL_POOL_CONFIG.getResource();
	}

	private RedisClientUtils() {
	}

	public static RedisMQProvider getMQProviderConn() {
		logger.info("Current master: "
				+ REDIS_SENTINEL_POOL_MQ.getConnectionInfo());
		return new RedisMQProvider(REDIS_SENTINEL_POOL_MQ);
	}

	public static RedisMQConsumer getMQConsumerConn() {
		logger.info("Current master: "
				+ REDIS_SENTINEL_POOL_MQ.getConnectionInfo());
		return new RedisMQConsumer(REDIS_SENTINEL_POOL_MQ);
	}
	
}