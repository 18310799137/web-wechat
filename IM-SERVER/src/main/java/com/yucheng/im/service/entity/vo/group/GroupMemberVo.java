package com.yucheng.im.service.entity.vo.group;
/**
 * 
 * @Description:点击群列表中的某个群 ，要显示的群成员列表信息
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月18日下午7:13:31
 * @version V1.0
 */
public class GroupMemberVo {

	private String userId;
	
	private String userName;
	
	private String userPhoto;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	@Override
	public String toString() {
		return "GroupDetailVo [userId=" + userId + ", userName=" + userName + ", userPhoto=" + userPhoto + "]";
	}
	
}
