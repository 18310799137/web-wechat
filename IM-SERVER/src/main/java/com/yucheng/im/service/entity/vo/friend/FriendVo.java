package com.yucheng.im.service.entity.vo.friend;

/**
 * 
 * @Description:点击名片时 返回给客户端的VO类
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月22日下午5:18:17
 * @version V1.0
 */
public class FriendVo {
	//好友ID
	private String friendId;
	//好友名称
	private String friendName;
	//是否是好友
	private String isFriend;
	//备注
	private String remarks;
	//群昵称
	private String groupNickName;
	//地区
	private String region;
	//添加方式
	private String source;
	//头像
	private String friendPhoto;
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
	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getGroupNickName() {
		return groupNickName;
	}
	public void setGroupNickName(String groupNickName) {
		this.groupNickName = groupNickName;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFriendPhoto() {
		return friendPhoto;
	}
	public void setFriendPhoto(String friendPhoto) {
		this.friendPhoto = friendPhoto;
	}
	@Override
	public String toString() {
		return "FriendVo [friendId=" + friendId + ", friendName=" + friendName + ", isFriend=" + isFriend + ", remarks="
				+ remarks + ", groupNickName=" + groupNickName + ", region=" + region + ", source=" + source
				+ ", friendPhoto=" + friendPhoto + "]";
	}
	
}
