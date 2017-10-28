package com.yucheng.im.service.web.util;

public interface WebConstants {
	static interface Friend{
		String IS_FRIEND_YES="true";
		String IS_FRIEND_NO="false";
	}
	//如果发送人是自己,消息展示在右边
	static interface Message{
		String SHOW_MESSAGE_LEFT="L";
		String SHOW_MESSAGE_RIGHT="R";
	}
	/**
	 * 前端方法名称
	 */
	String REMOTE_CHECKUSER_URL = "http://www.baidu.com";

	String REMOTE_LOGIN_PAGE = "http://www.kaifa.com/IM-WEB/resource/chat.html?requestName=kaifaTest";

	/***
	 * 调用远程接口的 URL
	 */
	String REMOTE_SERVER_URL="http://www.kaifa.com/IM-WEB/AddUserController/addUser.html?name=test";
	

	static interface MsgType{
		/**
		 * 用户系统消息类型 - 请求或是响应
		 */
		//String MESSAGE_TYPE_REQ = "R";
		//String MESSAGE_TYPE_RESQ = "P";
		String DISPOSE_STATUS_YES="Y";
		String DISPOSE_STATUS_NO="N";
		String DISPOSE_STATUS_IGNORE="I";
		String DISPOSE_STATUS_UNTREATED="U";
	}




	
	static interface RESULT{
		//处理结果标志
			String SUCCESS="SUCCESS";
			String FAILURE="FAILURE";
	 }
	
	static interface Channel{
		// 消息队列频道
		/** 群组消息频道 */
		String GROUP_MESSAGE_CHANNEL_NAME = "GROUP-CHANNEL";
		/** 用户消息频道 */
		String USER_MESSAGE_CHANNEL_NAME = "USER-CHANNEL";
		/** 系统消息频道 */
		String SYS_MESSAGE_CHANNEL_NAME = "SYS-CHANNEL";
	}
	
	static interface SESSION{
		/**用户聊天的会话标志 SG表示为群组聊天,SU表示为用户对用户聊天*/
		String SESSION_GROUP="SG";
		String SESSION_USER="SU";
	}
	
	
static interface Flag{
	
	/***
	 * Redis 存储用户信息标志
	 */
	String USERLOGINFLAG = "UL";//用户登录标志
	String USERINFOFLAG = "UI";//用户信息标志
	String USERGROUPFLAG = "UG";//用户群组标志
	String USERSESSIONFLAG="US";//用户会话标志
	String USERFRIENDSFLAG="UF";//用户好友标志
	
	/**存储群组信息的标识*/
	String GROUP_INFO="GI";
	
	

}

	static interface Config{
	//	String SESSION_HASH_NAME="userSessionMap";
		//Session超时时间 默认为10分钟
		int SESSION_TIMEOUT=600;
		/**
		 * Redis 连接配置项
		 */
		String REDIS_HOST = "127.0.0.1";
		int REDIS_PORT = 6379;
		String REDISCACHE="redis.cache";
		String REDISCONFIG="redis.config";
		String REDISMQ="redis.mq";
		String REDISCACHEGROUP="redis.cache.group";
		String REDISCONFIGGROUP="redis.config.group";
		String REDISMQGROUP="redis.mq.group";
		/** Redis配置 */
		String REDIS_CONF_FILE_PATH = "/redis.properties";
		/** Zookeeper地址 */
		String ZOOKEEPER_CONF_FILE_PATH = "/zookeeper.properties";

		/**solr_collection name*/
		String SOLR_COLLECTIONS = "solr.collections";
		/**zk host ip*/
		String ZOOKEEPER_HOST = "zookeeper.host";
	}

	
	/**好友添加途径 QNUM=搜索号码添加*/
	String QUERY_NUMBER="QNUM";
	/**好友添加途径QNAME=搜索名称添加*/
	String QUERY_NAME="QNAME";
	/**好友添加途径GC=通过群聊天添加*/
	String GROUP_CHAT="GC";
}
