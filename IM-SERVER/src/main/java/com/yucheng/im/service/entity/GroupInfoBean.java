package com.yucheng.im.service.entity;

import java.io.Serializable;
/**
 * 
* @Title: GroupInfoBean.java
* @Package com.yucheng.im.service.entity
* @Description: 群组的详细信息
* @author zhanggh@yusys.com.cn
* @date 2017年8月4日 下午6:00:07
* @version V1.0
*
 */
public class GroupInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3364983677472690204L;

	private String groupId;
	
	private String groupMasterId;
	
	private int groupMemberCount;
	
	private String groupName;
	
	private String groupNotice;
	
	private String createDate;
	
	private String defaultName;
	
	private String indexName;
	
	
	/** 顶部默认名称 */
	public String getDefaultName() {
		return defaultName;
	}
	/** 顶部默认名称 */
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	/** 会话栏名称名称 */
	public String getIndexName() {
		return indexName;
	}
	/**会话栏名称名称 */
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	/**群uuid*/
	public String getGroupId() {
		return groupId;
	}
	/**群uuid*/
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**群主的uuid*/
	public String getGroupMasterId() {
		return groupMasterId;
	}
	/**群主的uuid*/
	public void setGroupMasterId(String groupMasterId) {
		this.groupMasterId = groupMasterId;
	}
	/**群成员数量*/
	public int getGroupMemberCount() {
		return groupMemberCount;
	}
	/**群成员数量*/
	public void setGroupMemberCount(int groupMemberCount) {
		this.groupMemberCount = groupMemberCount;
	}
	/**群名称*/
	public String getGroupName() {
		return groupName;
	}
	/**群名称*/
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**群公告*/
	public String getGroupNotice() {
		return groupNotice;
	}
	/**群公告*/
	public void setGroupNotice(String groupNotice) {
		this.groupNotice = groupNotice;
	}
	/**群创建日期*/
	public String getCreateDate() {
		return createDate;
	}
	/**群创建日期*/
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "GroupInfoBean [groupId=" + groupId + ", groupMasterId=" + groupMasterId + ", groupMemberCount="
				+ groupMemberCount + ", groupName=" + groupName + ", groupNotice=" + groupNotice + ", createDate="
				+ createDate + ", defaultName=" + defaultName + ", indexName=" + indexName + "]";
	}
	
}
