package com.yucheng.im.service.web.mq.consumer;

import com.yucheng.im.service.web.util.RedisClientUtils;

import cn.com.yusys.redis.mq.RedisMQConsumer;

public class BaseConsumer extends Thread{
	public RedisMQConsumer cosumer = RedisClientUtils.getMQConsumerConn();

}
