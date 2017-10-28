package com.zgh.redis.client;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class _RedisCluster {
	private static JedisSentinelPool jedisSentinelPool;

	public static JedisCluster getJedisCluster() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(32);
		poolConfig.setMaxIdle(4);
		poolConfig.setMaxWaitMillis(6000);

		Set<HostAndPort> sentinels = new HashSet<HostAndPort>();
		sentinels.add(new HostAndPort("192.168.106.128", 26379));
		sentinels.add(new HostAndPort("192.168.106.132", 26379));
		sentinels.add(new HostAndPort("192.168.106.133", 26379));
		JedisCluster jedisCluster = new JedisCluster(sentinels, poolConfig);// JedisCluster中默认分装好了连接池.
		return jedisCluster;
	}

	public static void test1() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		String masterName = "mymaster1";
		Set<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.106.128:26379");
		sentinels.add("192.168.106.132:26379");
		jedisSentinelPool = new JedisSentinelPool(masterName, sentinels, poolConfig);
		HostAndPort currentHostMaster = jedisSentinelPool.getCurrentHostMaster();
		System.out.println(currentHostMaster);// 获取主节点的信息
		Jedis resource = jedisSentinelPool.getResource();
		System.out.println(resource.get("hello"));// 获得键a对应的value值
		resource.close();

	}

	public static void main(String[] args) {
		test1();
		//getJedisCluster();

	}
}
