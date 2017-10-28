package com.yucheng.im.service.web.mq.thread;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.yucheng.im.service.web.mq.thread.task.AbstractThreadTask;
import com.yucheng.im.service.web.mq.thread.task.HandleGroupMsgThreadTask;
import com.yucheng.im.service.web.mq.thread.task.HandleSysMsgThreadTask;
import com.yucheng.im.service.web.mq.thread.task.HandleUserMsgThreadTask;

/**
 * 
 * @ClassName: HandleMsgThreadPool
 * @Description: 处理用户类消息线程池
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月2日 上午1:07:25
 *
 */
public class HandleMsgThreadPool {
	private static Logger logger = Logger.getLogger(HandleMsgThreadPool.class);

	// 群组类消息处理类的 线程池大小
	private static int groupPoolSize = 10;
	// 用户类消息处理的 线程池大小
	private static int userPoolSize = 10;
	// 系统类消息处理的 线程池大小
	private static int sysPoolSize = 1;

	// 处理群组类 消息 线程池
	private static Vector<AbstractThreadTask> groupThreadPool = new Vector<AbstractThreadTask>();
	// 处理用户类消息 线程池
	private static Vector<AbstractThreadTask> userThreadPool = new Vector<AbstractThreadTask>();
	// 处理系统消息 线程池
	private static Vector<AbstractThreadTask> sysThreadPool = new Vector<AbstractThreadTask>();

	// 群组类消息堆集的 队列
	public static ConcurrentLinkedQueue<String> groupMessageQueue = new ConcurrentLinkedQueue<String>();
	// 用户类消息堆集的 队列
	public static ConcurrentLinkedQueue<String> userMessageQueue = new ConcurrentLinkedQueue<String>();
	// 系统类消息堆集的 队列
	public static ConcurrentLinkedQueue<String> sysMessageQueue = new ConcurrentLinkedQueue<String>();

	private static Lock groupLock = new ReentrantLock();
	private static Lock userLock = new ReentrantLock();

	

	/**
	 * 
	 * @Title: initAllThreadPool @Description: 初始化所有线程池 @param @return void @throws
	 */
	public static void initAllThreadPool() {
		initGroupThreadPool();
		initUserThreadPool();
		initSysThreadPool();
	}

	public HandleMsgThreadPool() {
		logger.error("HandleMsgThreadPool constructor");
	}

	/**
	 * 
	 * @Title: initSysThreadPool @Description: 初始化系统类消息线程池 @param @return
	 * void @throws
	 */
	private static void initSysThreadPool() {
			logger.info("initSysThreadPool - start");
		 
		// 初始化线程池
		for (int i = 0; i < sysPoolSize; i++) {
			HandleSysMsgThreadTask sysMsgThreadTask = new HandleSysMsgThreadTask();
			sysMsgThreadTask.setName("SysThreadTask - " + i);
			sysMsgThreadTask.start();
		}
		logger.info("initSysThreadPool - complete");
	}

	/**
	 * 
	 * @Title: initSysThreadPool @Description: 初始化好友类消息线程池 @param @return
	 * void @throws
	 */
	private static void initUserThreadPool() {
			logger.info("initUserThreadPool - start");
		// 初始化线程池
		for (int i = 0; i < userPoolSize; i++) {
			HandleUserMsgThreadTask userMsgThreadTask = new HandleUserMsgThreadTask();
			userMsgThreadTask.setName("UserThreadTask - " + i);
			userMsgThreadTask.start();
		}
		logger.info("initUserThreadPool - complete");
	}

	/**
	 * 
	 * @Title: initSysThreadPool @Description: 初始化群组类消息线程池 @param @return
	 * void @throws
	 */
	private static void initGroupThreadPool() {
			logger.info("initGroupThreadPool - start");
		// 初始化线程池
		for (int i = 0; i < groupPoolSize; i++) {
			HandleGroupMsgThreadTask groupMsgThreadTask = new HandleGroupMsgThreadTask();
			groupMsgThreadTask.setName("GroupThreadTask - " + i);
			groupMsgThreadTask.start();
		}
		logger.info("initGroupThreadPool - complete");
	}

	/**
	 * 
	 * @Description:唤醒处理群组消息的线程池中的空闲线程
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月30日
	 * @param message
	 * @return void
	 */
	public static void recieveGroupMessage(String message) {
		groupLock.lock();
		groupMessageQueue.add(message);
		if (!groupThreadPool.isEmpty()) {
			AbstractThreadTask groupThreadTask = groupThreadPool.get(0);
			groupThreadPool.remove(groupThreadTask);
			logger.info(groupThreadTask.getName() + "- 被分配执行任务 - 目前线程池大小" + groupThreadPool.size());
			groupThreadTask.notifyThisTask();
		} else {
			logger.error("groupThreadPool线程池中没有可用的线程 " + groupThreadPool.size());
		}
		groupLock.unlock();
	}

	/**
	 * 
	 * @Description: 唤醒处理用户消息 的线程池中的空闲线程
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月31日
	 * @param message
	 * @return void
	 */
	public static void recieveUserMessage(String message) {
		userLock.lock();
		userMessageQueue.add(message);
		if (!userThreadPool.isEmpty()) {
			AbstractThreadTask userThreadTask = userThreadPool.get(0);
			userThreadPool.remove(userThreadTask);
			logger.info(userThreadTask.getName() + "- 被分配执行任务 - 目前线程池大小" + userThreadPool.size());
			userThreadTask.notifyThisTask();
		} else {
			logger.error("userThreadPool线程池中没有可用的线程 " + userThreadPool.size());
		}
		userLock.unlock();
	}

	public static void recieveSysMessage(String message) {

		sysMessageQueue.add(message);
		if (!sysThreadPool.isEmpty()) {
			AbstractThreadTask sysThreadTask = sysThreadPool.get(0);
			sysThreadPool.remove(sysThreadTask);
			logger.info(sysThreadTask.getName() + "- 被分配执行任务 - 目前线程池大小" + sysThreadPool.size());
			sysThreadTask.notifyThisTask();
		} else {
			logger.error("sysThreadPool线程池中没有可用的线程 " + sysThreadPool.size());
		}

	}

	/**
	 * 
	 * @Description: 回收群组消息到线程池中
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月31日
	 * @param task
	 * @return void
	 */
	public static <T extends AbstractThreadTask> void recycleToGroupThreadPool(T task) {
		synchronized ("G") {
			if (!groupThreadPool.contains(task)) {
				groupThreadPool.add(task);
				logger.info(task.getName() + " 已回归线程池 ,目前线程池大小" + groupThreadPool.size());
			}
		}
	}

	/**
	 * 
	 * @Description: 回收处理用户消息的线程到线程池中
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月31日
	 * @param task
	 * @return void
	 */
	public static <T extends AbstractThreadTask> void recycleToUserThreadPool(T task) {
		synchronized ("U") {
			if (!userThreadPool.contains(task)) {
				userThreadPool.add(task);
				logger.info(task.getName() + " 已回归线程池 ,目前线程池大小" + userThreadPool.size());
			}
		}
	}

	public static <T extends AbstractThreadTask> void recycleToSysThreadPool(T task) {
		synchronized ("S") {
			if (!sysThreadPool.contains(task)) {
				sysThreadPool.add(task);
				logger.info(task.getName() + " 已回归线程池 ,目前线程池大小" + sysThreadPool.size());
			}
		}
	}

}
