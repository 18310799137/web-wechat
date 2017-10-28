package com.yucheng.im.service.entity.vo.group;

/**
 * 
 * @Description:修改群名称推送到客户端所需的VO
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月29日下午3:09:57
 * @version V1.0
 */
public class PushToClienModifyGroupNameVo {

	@Override
	public String toString() {
		return "PushToClienModifyGroupNameVo [groupId=" + groupId + ", groupName=" + groupName + "]";
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String groupId;
	
	public String groupName;
	
}
