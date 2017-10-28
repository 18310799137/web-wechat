package com.test.yucheng.im.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yucheng.im.service.entity.msg.GroupMemMsgStatus;
import com.yucheng.im.service.manager.service.IGroupMemMsgService;
import com.yucheng.im.service.util.UUIDGenerateUtils;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring/applicationContext.xml")
public class TestAddGroupMemMsg extends TestCase{
	
	@Resource
	private IGroupMemMsgService groupMemMsgService;
	
	@Test
	public void testAddGroupMemMsg() {
		GroupMemMsgStatus groupMemMsg = new GroupMemMsgStatus(UUIDGenerateUtils.getDBUuid(),"001","100",5);
		groupMemMsgService.addGroupMemMsg(groupMemMsg);
	}
	
	@Test
	public void testqueryUnreadMsgCount() {
		Map<String,String> params = new HashMap<>();
		params.put("groupId", "00");
		params.put("memberId", "101");
		System.out.println(groupMemMsgService.queryUnreadMsgCount(params));
	}
	
	@Test
	public void testModifyGroupMemMsgUnreadCount() {
		Map<String,String> params = new HashMap<>();
		params.put("groupId", "001");
		params.put("memberId", "101");
		params.put("unreadNum", "1");
		groupMemMsgService.modifyGroupMemMsgUnreadCount(params);
	}
}
