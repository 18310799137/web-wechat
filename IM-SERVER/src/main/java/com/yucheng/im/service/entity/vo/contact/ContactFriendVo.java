package com.yucheng.im.service.entity.vo.contact;

/**
 * 
 * @Description:联系人好友列表VO
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月21日上午10:30:58
 * @version V1.0
 */
public class ContactFriendVo {

	//好友ID
	private String friendId;
	//好友名称
	private String friendName;
	//好友图像
	private String userPhoto;
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	@Override
	public String toString() {
		return "ContactFriendVo [friendId=" + friendId + ", friendName=" + friendName + ", userPhoto=" + userPhoto
				+ "]";
	}
	
}
