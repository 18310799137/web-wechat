package com.test.yucheng.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yucheng.im.service.entity.msg.GroupMessage;
import com.yucheng.im.service.manager.service.IUserGroupMsgService;
import com.yucheng.im.service.util.DateUtils;
import com.yucheng.im.service.util.ServConstants;
import com.yucheng.im.service.util.UUIDGenerateUtils;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath*:spring/applicationContext.xml")
public class TestAddUserGroupMsg extends TestCase{

	@Resource
	private IUserGroupMsgService userGroupService;
	@Test
	public void testAddUserGroupMsg() {
		userGroupService.addUserGroupMsg(new GroupMessage(UUIDGenerateUtils.getDBUuid(), "123456789", "123654987",ServConstants.System.IS_NOTICE_NO ,ServConstants.System.OPR_TYPE_NO, DateUtils.getDateStr(), "hello"));
	
	}
	@Test
	public void testQueryUserGroupMsgByPage() {
		Map<String, String>params = new HashMap<>();
		/*params.put("toGroupId", "123654987");*/
		params.put("pageNo", "123654987");
		params.put("pageSize", "123654987");
		params.put(ServConstants.OrderField.ORDER_CONDITION, ServConstants.OrderField.ORDER_FIELD_SEND_TIME_DESC);

		List<GroupMessage> list=	userGroupService.queryGroupMsgByPage(params);
		for (GroupMessage groupMessage : list) {
			System.out.println(groupMessage);
		}
	}
}
