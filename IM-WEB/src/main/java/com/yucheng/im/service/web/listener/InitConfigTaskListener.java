package com.yucheng.im.service.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yucheng.im.service.web.mq.consumer.ConsumerFriendMessage;
import com.yucheng.im.service.web.mq.consumer.ConsumerGroupMessage;
import com.yucheng.im.service.web.mq.consumer.ConsumerSysMessage;
import com.yucheng.im.service.web.mq.thread.HandleMsgThreadPool;
import com.yucheng.im.service.web.mq.thread.task.AbstractThreadTask;

public class InitConfigTaskListener implements HttpSessionListener,
HttpSessionAttributeListener ,ServletContextListener  , ApplicationListener<ContextRefreshedEvent>{

	private static Logger logger = Logger.getLogger(InitConfigTaskListener.class);
	

	
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * 初始化系统配置信息 MQ消费 程序
	 */
	public void contextInitialized(ServletContextEvent event) {
		logger.info(" contextInitialized  - MessageQueueTaskListener      <start>");
		
		  WebApplicationContext applicationContext =  	WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());  
		 
		  AbstractThreadTask.context=applicationContext;
		  //初始化线程池
		  HandleMsgThreadPool.initAllThreadPool();

		  
		  
		ConsumerFriendMessage friendMessage =	new ConsumerFriendMessage();
		friendMessage.setName("MonitorThread - ConsumerFriendMessage");
		friendMessage.start();
		
		
		ConsumerGroupMessage groupMessage  =	new ConsumerGroupMessage();
		groupMessage.setName("MonitorThread - ConsumerGroupMessage");
		groupMessage.start();
		
		ConsumerSysMessage sysMessage  =	new ConsumerSysMessage();
		sysMessage.setName("MonitorThread - ConsumerSysMessage");
		sysMessage.start();
		logger.info(" contextInitialized  - MessageQueueTaskListener      <complete>");

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}

}
