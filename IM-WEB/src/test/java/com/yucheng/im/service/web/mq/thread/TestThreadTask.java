package com.yucheng.im.service.web.mq.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.msg.SysBroadCastMessage;
import com.yucheng.im.service.web.mq.thread.task.HandleGroupMsgThreadTask;

import net.sf.json.JSONObject;

public class TestThreadTask {
	private static Logger logger = Logger.getLogger(HandleGroupMsgThreadTask.class);
	
	public static int count=10000;
	public static ConcurrentHashMap<String, Integer>groupMap = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<String, Integer>userMap = new ConcurrentHashMap<String, Integer>();
	public static ConcurrentHashMap<String, Integer>sysMap = new ConcurrentHashMap<String, Integer>();
	public static void initSysMsgQueue(){
		try {
			logger.info("2秒钟之后 初始化用户队列");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long start =	System.currentTimeMillis();
		
		for (int i = 0; i < count; i++) {
			JSONObject jsonObject =	JSONObject.fromObject(new SysBroadCastMessage("风清扬","All","hello",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
//				
			HandleMsgThreadPool.sysMessageQueue.add(jsonObject.toString());
		}
		long end =	System.currentTimeMillis();
		logger.info("系统队列初始化完毕 - 队列深度:"+count+"\t共计用时"+(end-start)+"毫秒");
	}
	public static void initUserMsgQueue(){
		try {
			logger.info("2秒钟之后 初始化用户队列");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long start =	System.currentTimeMillis();
		
		for (int i = 0; i < count; i++) {
			/*JSONObject jsonObject =	JSONObject.fromObject(new UserMessage(null,"风清扬","独孤求败",false,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"hello","Y-1",WebConstants.MsgType.DISPOSE_STATUS_UNTREATED));
//				
			HandleMsgThreadPool.userMessageQueue.add(jsonObject.toString());*/
		}
		long end =	System.currentTimeMillis();
		logger.info("用户队列初始化完毕 - 队列深度:"+count+"\t共计用时"+(end-start)+"毫秒");
	}
		public static void initGroupMsgQueue(){
			try {
				logger.info("2秒钟之后 初始化群组消息队列");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long start =	System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				//JSONObject jsonObject =	JSONObject.fromObject(new GroupMessage("lifeng","shaokai",false,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"hello") );
				//HandleMsgThreadPool.groupMessageQueue.add(jsonObject.toString());
			}
			long end =	System.currentTimeMillis();
			logger.info("队列初始化完毕 - 队列深度:"+count+"\t共计用时"+(end-start)+"毫秒");
		}
		
		public static void statisticsGroup()throws InterruptedException{
			Thread.sleep(10000);
			logger.info("群组初始化消息总数"+count+"条全部消费完 - 准备统计信息...");
			Thread.sleep(1000);
			int groupSum=0;
			for (Entry<String, Integer> entry : groupMap.entrySet()) {
				logger.info(entry.getKey()+":"+entry.getValue());
				groupSum+=entry.getValue();
			}
			logger.info("群组消息统计结果 - 共消费消息 "+groupSum+"条");
		}
		public static void statisticsUser()throws InterruptedException{
			Thread.sleep(10000);
			logger.info("用户初始化消息总数"+count+"条全部消费完 - 准备统计信息...");
			Thread.sleep(1000);
			int userSum=0;
			for (Entry<String, Integer> entry : groupMap.entrySet()) {
				logger.info(entry.getKey()+":"+entry.getValue());
				userSum+=entry.getValue();
			}
			logger.info("用户消息统计结果 - 共消费消息 "+userSum+"条");
		}
		public static void statisticsSys()throws InterruptedException{
			Thread.sleep(10000);
			logger.info("系统初始化消息总数"+count+"条全部消费完 - 准备统计信息...");
			Thread.sleep(1000);
			int sysSum=0;
			for (Entry<String, Integer> entry : sysMap.entrySet()) {
				logger.info(entry.getKey()+": 共消费消息-"+entry.getValue()+"条");
				sysSum+=entry.getValue();
			}
			logger.info("系统消息统计结果 - 共消费消息 "+sysSum+"条");
		}
	public static void main(String[] args) throws InterruptedException {
	
		TestThreadTask.initGroupMsgQueue();
		TestThreadTask.initUserMsgQueue();
		TestThreadTask.initSysMsgQueue();
		HandleMsgThreadPool.initAllThreadPool();
		statisticsGroup();
		statisticsUser();
		statisticsSys();
		
		while (true) {
			Thread.sleep(3000);
			//JSONObject groupJson =	JSONObject.fromObject(new GroupMessage("lifeng","shaokai",false,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"hello"));
			//HandleMsgThreadPool.recieveGroupMessage(groupJson.toString());
			Thread.sleep(3000);
			//JSONObject userJson =	JSONObject.fromObject(new UserMessage(null,"风清扬","独孤求败",false,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),"hello","Y-1",WebConstants.MsgType.DISPOSE_STATUS_UNTREATED));
			//HandleMsgThreadPool.recieveUserMessage(userJson.toString());
			Thread.sleep(3000);
			JSONObject sysJson =	JSONObject.fromObject(new SysBroadCastMessage("风清扬","ALL","hello",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			HandleMsgThreadPool.recieveSysMessage(sysJson.toString());
		}
	}

}
