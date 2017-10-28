package com.yucheng.im.service.web.dwr.util;

import java.util.Set;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;

/**
* 
* @Title: MessagePushToClientUtils.java
* @Package com.yucheng.im.service.web.dwr.util
* @Description: 执行前端函数
* @author zhanggh@yusys.com.cn
* @date 2017年8月1日 下午5:32:28
* @version V1.0
*
*/
public class MessagePushToClientUtils {
	/**
	 * 
	 * @Description: 向指定客户端id的推送请求 执行一段javascript脚本
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月1日 下午5:47:23
	 * @version V1.0
	 * @param sessionIds
	 * @param script 要执行的字符串脚本
	 */
	public static void scriptPushBySessionId(final Set<String>sessionIds,final String script){
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				for (String sessionId : sessionIds) {
					if(sessionId.equals(session.getHttpSessionId())){
						return true;
					}
				}
				return false;
			}
		}, new Runnable() {
			public void run() {
				ScriptSessions.addScript(script);
			}
		});
	}
	/**
	 * 
	 * @Description: 向所有连接的客户端发送一段javascript脚本 并且立即执行
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月1日 下午5:47:34
	 * @version V1.0
	 * @param script 要执行的字符串脚本
	 */
	public static void scriptPushToAllClient(final String script){
		Browser.withAllSessions(new Runnable() {
			public void run() {
				ScriptSessions.addScript(script);
			}
		});
	}

	/**
	 * 
	 * @Description: 执行前端函数,目标为所有已连接的客户端
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月1日 下午5:49:22
	 * @version V1.0
	 * @param funcName 要执行的前端方法名称
	 * @param obj 执行前端方法存入的参数
	 */
	public static void methodPushToAllClient(final String funcName,final Object obj){
		Browser.withAllSessions(new Runnable() {
			public void run() {
				ScriptSessions.addFunctionCall(funcName,obj);
			}
		});
	}

/**
 * 
 * @Description: 根据sessionId 执行特定的客户端方法
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月1日 下午5:49:45
 * @version V1.0
 * @param sessionIds
 * @param funcName
 * @param params
 */
	public static void methodPushBySessionId(final Set<String>sessionIds,final String funcName,final Object ...params){
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				for (String sessionId : sessionIds) {
					if(sessionId.equals(session.getHttpSessionId())){
						return true;
					}
				}
				return false;
			}
		}, new Runnable() {
			public void run() {
				ScriptSessions.addFunctionCall(funcName, params);
			}
		});
	}
	
}
