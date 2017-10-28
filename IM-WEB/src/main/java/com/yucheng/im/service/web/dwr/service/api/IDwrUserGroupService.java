package com.yucheng.im.service.web.dwr.service.api;

public interface IDwrUserGroupService {
	/**
	 * 接收客户端发送的群组消息
	 * 
	 * @param toGroupId
	 * @param content
	 * @return
	 */
	String sendChatMessage(String toGroupId, String content);

	/**
	 * 
	 * @Description: 创建群组
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月4日 上午10:34:06
	 * @version V1.0
	 * @param id
	 *            需要添加的群成员 客户号
	 * @return
	 */
	String createGroup(String[] id);

	/**
	 * 
	 * @Description: 添加群成员到群组
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午4:33:45
	 * @version V1.0
	 * @param groupId
	 * @param id
	 * @return
	 */
	String addUserToGroup(String groupId, String[] id);

	/**
	 * 
	 * @Description: 获取群列表
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午4:04:06
	 * @version V1.0
	 * @return
	 */
	String queryUserGroupList();


	/**
	 * 
	 * @Description: 删除群成员
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:02:03
	 * @version V1.0
	 * @param groupId
	 *            群id
	 * @param removeId
	 *            成员id
	 * @return
	 */
	String removeGroupUser(String groupId,  String []removeId);

	/**
	 * 
	 * @Description: 修改群信息
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:04:10
	 * @version V1.0
	 * @param groupId
	 * @param bean
	 * @return
	 */
	String modifyGroupInfo(String groupInfoJsonStr);

	/**
	 * 
	 * @Description: 退出所在的群
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月22日 下午5:05:32
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	String exitGroup(String groupId);

	/**
	 * 
	 * @Description:根据群ID 查询该群的聊天记录
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月18日下午1:24:11
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	String queryGroupChatRecordList(String groupId);

	String queryGroupMemberList(String groupId);


	String modifySessionListIsUnread(String groupId);
	
	/***
	 * 
	 * @Description:点击右上角详情时 查询出来的成员列表 群公告等
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年9月29日上午9:10:11
	 * @version V1.0
	 * @param groupId
	 * @return
	 */
	String queryGroupMemberListAndGroupDetail(String groupId);
}