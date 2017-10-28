package com.yucheng.im.service.web.mq.consumer;

import org.apache.log4j.Logger;

import com.yucheng.im.service.web.mq.thread.HandleMsgThreadPool;
import com.yucheng.im.service.web.util.WebConstants;

import cn.com.yusys.redis.client.inter.ISubscribeCallback;

public class ConsumerGroupMessage extends BaseConsumer {
	private static Logger logger  = Logger.getLogger(ConsumerGroupMessage.class);

	
	 @Override
	public void run() {
		 logger.info("MonitorThread <MQ> Channel - ["+WebConstants.Channel.GROUP_MESSAGE_CHANNEL_NAME +"]      start Working");

		 cosumer.subscribe(new ISubscribeCallback() {
			public void onMessage(String paramString1, String message) {
				logger.info(currentThread().getName()+ "ConsumerGroupMessage 	==================  MonitorThread a message");
				HandleMsgThreadPool.recieveGroupMessage(message);
			}
		}, WebConstants.Channel.GROUP_MESSAGE_CHANNEL_NAME);
		 logger.info("MonitorThread <MQ> Channel - ["+WebConstants.Channel.GROUP_MESSAGE_CHANNEL_NAME +"]      start Complete");
	}
}
