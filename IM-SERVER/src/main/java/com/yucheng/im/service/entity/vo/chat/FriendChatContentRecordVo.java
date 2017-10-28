package com.yucheng.im.service.entity.vo.chat;

import java.util.List;

/**
 * 
 * @Description:好友聊天记录查询 要返回给客户端的VO类
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月15日下午4:00:08
 * @version V1.0
 */
public class FriendChatContentRecordVo {

	//好友的ID
	private String userId;
	//好友的图像
	private String userPhoto;
	//好友的姓名
	private String userName;
	//自己的图像
	private String oneSelfPhoto;
	//聊天内容列表
	private List<FriendChatRecordListVo>recordListVos;
	
	public String getOneSelfPhoto() {
		return oneSelfPhoto;
	}
	public void setOneSelfPhoto(String oneSelfPhoto) {
		this.oneSelfPhoto = oneSelfPhoto;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<FriendChatRecordListVo> getRecordListVos() {
		return recordListVos;
	}
	public void setRecordListVos(List<FriendChatRecordListVo> recordListVos) {
		this.recordListVos = recordListVos;
	}
	@Override
	public String toString() {
		return "FriendChatContentRecordVo [userId=" + userId + ", userPhoto=" + userPhoto + ", userName=" + userName
				+ ", oneSelfPhoto=" + oneSelfPhoto + ", recordListVos=" + recordListVos + "]";
	}
 
	
}
