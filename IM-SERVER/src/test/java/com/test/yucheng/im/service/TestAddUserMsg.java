package com.test.yucheng.im.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import com.yucheng.im.service.entity.msg.UserMessage;
import com.yucheng.im.service.manager.service.IUserMsgService;
import com.yucheng.im.service.util.DateUtils;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring/applicationContext.xml")
public class TestAddUserMsg {
	private static Logger logger = Logger.getLogger(TestAddUserMsg.class);
static {
	try {
		Log4jConfigurer.initLogging("src/config/java/log4j.properties");
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}
		@Resource
		private IUserMsgService userMsgService;
		
		@Test
		public void testAddUserMsg() {
			userMsgService.addUserMsg(new UserMessage(UUIDGenerateUtils.getDBUuid(),"CB10000604100001","CB10000680100020",ServConstants.System.IS_NOTICE_NO,DateUtils.getDateStr(),"联系人会话3",ServConstants.System.REQ_MSG_FLAG,ServConstants.System.IS_UNREAD_YES,ServConstants.System.DISPOSE_STATUS_UN));
		}
		
		@Test
		public void testQueryUserMsgByPage() {
			Map<String,String> params = new HashMap<>();
			params.put(ServConstants.ConditionUserMsg.FROM_USER_ID,"CB10000680100020");
			params.put(ServConstants.ConditionUserMsg.TO_USER_ID,"CB10000604100001");
			params.put(ServConstants.OrderField.ORDER_CONDITION, ServConstants.OrderField.ORDER_FIELD_SEND_TIME_DESC);
			params.put(ServConstants.PAGE.NOW_PAGE_STR,"1");
			params.put(ServConstants.PAGE.PAGE_SIZE_STR,"1");
			List<UserMessage> list = userMsgService.queryUserMsgByPage( params);
			logger.info(list);
			System.out.println(list);
		}
		
		@Test
		public void testQueryUnreadUserMsgCount() {
			Map<String,String> params = new HashMap<>();
			params.put("toUserId", "002");
			params.put("isUnread", ServConstants.System.IS_UNREAD_YES);
			System.out.println(userMsgService.queryUnreadUserMsgCount(params));
		}
		
		@Test
		public void testModifyUserMsgStatus() {
			Map<String,String> params = new HashMap<>();
//			params.put("disposeStatus", ServConstants.System.DISPOSESTATUS_YES);
			params.put("isUnread", ServConstants.System.IS_UNREAD_NO);
			params.put("fromUserId", "001");
			params.put("toUserId", "002");
			params.put("isNotice", ServConstants.System.IS_NOTICE_YES);
			params.put("mType", ServConstants.System.REQ_MSG_FLAG);
			userMsgService.modifyUserMsgStatus(params);
		}
}
