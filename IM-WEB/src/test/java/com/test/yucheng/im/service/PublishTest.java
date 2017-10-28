package com.test.yucheng.im.service;

import cn.com.yusys.redis.client.exception.RedisException;
import cn.com.yusys.redis.mq.RedisMQProvider;

import com.yucheng.im.service.web.util.RedisClientUtils;

//@Component
public class PublishTest  extends Thread {

	private RedisMQProvider provider = RedisClientUtils.getMQProviderConn();
	
	 @Override
	public void run() {
		 try {
			Thread.sleep(15000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 System.out.println("PublishTest ==================      start");
		 try {
			for (int i = 0; i < 10000; i++) {
				Thread.sleep(3000);
				System.out.println("publish Message[topic - CosumerFriendResp] - First - publish Message,two Client will recieve"+i);
				provider.publish("ConsumerFriendReq","First - publish Message,two Client will recieve"+i);
			}
		} catch (RedisException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("PublishTest ==================      end");
	}

}