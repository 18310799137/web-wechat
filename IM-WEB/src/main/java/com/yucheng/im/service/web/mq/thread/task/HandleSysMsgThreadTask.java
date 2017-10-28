package com.yucheng.im.service.web.mq.thread.task;
import org.apache.log4j.Logger;

import com.yucheng.im.service.entity.msg.SysBroadCastMessage;
import com.yucheng.im.service.web.mq.thread.HandleMsgThreadPool;

import net.sf.json.JSONObject;
public class HandleSysMsgThreadTask extends AbstractThreadTask{
	
	private static Logger logger = Logger.getLogger(HandleSysMsgThreadTask.class);
	public void notifyThisTask(){
		this.interrupt();
	}
	/**
	 * 处理用户类消息线程任务
	 */
	public void run() {
		logger.info("ThreadName:"+currentThread().getName()+" - Start Working");
		int sum=0;
		while(true){
			int count=0;
			long start = System.currentTimeMillis();
			SysBroadCastMessage message=null;
			String jsonMesg=null;
			while((jsonMesg	=	HandleMsgThreadPool.sysMessageQueue.poll())!= null){
				count++;
				sum++;
				message=(SysBroadCastMessage) JSONObject.toBean(JSONObject.fromObject(jsonMesg), SysBroadCastMessage.class);
				logger.debug("ThreadName:"+currentThread().getName()+" - Poll -"+message);
			}
			long end =	System.currentTimeMillis();
			logger.info(currentThread().getName()+"累计消费总数"+sum+"条,本次消费总数为:"+count+"条\t共计用时"+(end-start)+"毫秒");
			try {
				HandleMsgThreadPool.recycleToSysThreadPool(this);
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				logger.info(currentThread().getName()+"线程被唤醒 开始执行任务");
			}
		}
	}
}
