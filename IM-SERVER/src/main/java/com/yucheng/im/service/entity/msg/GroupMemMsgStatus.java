package com.yucheng.im.service.entity.msg;

public class GroupMemMsgStatus {

	private String uuid;
	//群组id
	private String groupId;
	//群成员Id
	private String memberId;
	//未读消息数量
	private int unreadNum;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public GroupMemMsgStatus(String uuid, String groupId, String memberId, int unreadNum) {
		super();
		this.uuid = uuid;
		this.groupId = groupId;
		this.memberId = memberId;
		this.unreadNum = unreadNum;
	}
	public GroupMemMsgStatus() {
		// TODO Auto-generated constructor stub
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getUnreadNum() {
		return unreadNum;
	}
	public void setUnreadNum(int unreadNum) {
		this.unreadNum = unreadNum;
	}
	@Override
	public String toString() {
		return "GroupMemMsgStatus [uuid=" + uuid + ", groupId=" + groupId + ", memberId=" + memberId + ", unreadNum="
				+ unreadNum + "]";
	}
	
	
}
