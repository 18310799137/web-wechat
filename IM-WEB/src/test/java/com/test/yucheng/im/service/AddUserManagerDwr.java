package com.test.yucheng.im.service;

import java.util.List;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;

import com.yucheng.im.service.web.util.RedisClientUtils;
import com.yucheng.im.service.web.util.WebConstants;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

//@Component
public class AddUserManagerDwr {
	private Jedis jedis = RedisClientUtils.getRedisCacheSource();
	/**
	 * 
	 */
	public String search(String param){
		
		return "Hello :"+param;
	}
	/**
	 * 根据客户号用户号查询用户群组列表
	 * @param custUserNo
	 */
	public void getUserGroupList(String custUserNo){
		//UserInfoBean userInfoBean  = SerializeUtil.unserialize(jedis.get(custUserNo.getBytes()));
	}
	public String addUser(String userId,String friendId){
		System.out.println("userId"+userId+"\t friendId"+friendId);
		jedis.rpush(userId+WebConstants.Flag.USERFRIENDSFLAG ,friendId);
		//ScriptSession.
		jedis.close();
		return "true";
	}
	
	
	/**
	 * 根据客户号用户号查询用户好友列表
	 * @param custUserNo
	 */
	public JSONObject getUserFriendsList(String custUserNo){
		//UserInfoBean userInfoBean  =(UserInfoBean)SerializeUtil.unserialize(jedis.get(custUserNo.getBytes()));
		jedis.close();
		
		List<String> list=jedis.lrange(custUserNo+WebConstants.Flag.USERFRIENDSFLAG,0l,-1l);
		for (String string :list) {
			System.out.println("StringFriendId:"+string);
		}
		return JSONObject.fromObject(list);
	}
	
	
	
	/**
	 * 下面只例出几个式子
 
CRON表达式    含义 
 "0 0 12 * * ?"    每天中午十二点触发 
 "0 15 10 ? * *"    每天早上10：15触发 
 "0 15 10 * * ?"    每天早上10：15触发 
 "0 15 10 * * ? *"    每天早上10：15触发 
 "0 15 10 * * ? 2005"    2005年的每天早上10：15触发 
 "0 * 14 * * ?"    每天从下午2点开始到2点59分每分钟一次触发 
 "0 0/5 14 * * ?"    每天从下午2点开始到2：55分结束每5分钟一次触发 
 "0 0/5 14,18 * * ?"    每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
 "0 0-5 14 * * ?"    每天14:00至14:05每分钟一次触发 
 "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发 
 "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发 

	 */
	
	// @Scheduled(cron="0/5 * *  * * ? ")   
	public void callBack(final String sessionId){
		 System.out.println("callBack....");
		Browser.withAllSessionsFiltered( new ScriptSessionFilter() {
			
			public boolean match(ScriptSession session) {
				System.out.println("session.getHttpSessionId():"+session.getHttpSessionId());
				System.out.println("session.getId():"+session.getId());
				if(session.getHttpSessionId().equals(sessionId)){
					return true;
				}
				return false;
			}
		},new Runnable() {
			//	Browser.withAllSessions(new Runnable() {
			
			public void run() {
				System.out.println("run....");
				//ScriptSessions.addScript("alert(1)");
			ScriptSessions.addFunctionCall("test", 1);
			
			}
		});
	}
}