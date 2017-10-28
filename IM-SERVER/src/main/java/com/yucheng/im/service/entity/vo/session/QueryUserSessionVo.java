package com.yucheng.im.service.entity.vo.session;

/**
 * @Description:页面刚加载时 需查询会话列表 用户对用户的VO类
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月12日下午3:13:30
 * @version V1.0
 */
public class QueryUserSessionVo  extends BaseSessionVo{
	
	//会话列表中 聊天者的id
	private String userId;
	//聊天者的姓名
	private String userName;
	//发送者图片
	private String userPhoto;
	//是否为好友
	private String isFriend;
	
	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
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
		return "QueryUserSessionVo [userId=" + userId + ", userName=" + userName + ", userPhoto=" + userPhoto
				+ ", isFriend=" + isFriend + "]";
	}
 
	
}
