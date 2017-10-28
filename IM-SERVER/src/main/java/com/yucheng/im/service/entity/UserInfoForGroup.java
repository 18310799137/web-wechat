package com.yucheng.im.service.entity;

import java.io.Serializable;

/***
 * 
 * @Title: UserInfoForGroup.java
 * @Package com.yucheng.im.service.entity
 * @Description: 记录用户在群中的偏好设置, 目前不考虑APP ,只存储群昵称
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月4日 下午6:02:31
 * @version V1.0
 *
 */
public class UserInfoForGroup implements Serializable{
	private static final long serialVersionUID = 7957527711476236045L;
	// 用户id
	private String userId;
	// 群昵称
	private String groupNickname;
	//消息免打扰   默认开启消息通知
	private boolean noDisturbing=false;
	//置顶聊天
	private boolean toTop=false;
	//显示群成员昵称
	private boolean showGroupMemNm=true;
	//保存到通讯录  默认保存到通讯录
	private boolean saveToContacts=true;
	public UserInfoForGroup() {
		// TODO Auto-generated constructor stub
	}
	
	/**用户id*/
	public String getUserId() {
		return userId;
	}
	/***用户id*/
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 群昵称*/
	public String getGroupNickname() {
		return groupNickname;
	}
	/** 群昵称*/
	public void setGroupNickname(String groupNickname) {
		this.groupNickname = groupNickname;
	}

 
	/**消息免打扰*/
	public boolean isNoDisturbing() {
		return noDisturbing;
	}
	/**消息免打扰*/
	public void setNoDisturbing(boolean noDisturbing) {
		this.noDisturbing = noDisturbing;
	}
	/**是否置顶聊天*/
	public boolean isToTop() {
		return toTop;
	}
	/**是否置顶聊天*/
	public void setToTop(boolean toTop) {
		this.toTop = toTop;
	}
	/**展示群成员昵称*/
	public boolean isShowGroupMemNm() {
		return showGroupMemNm;
	}
	/**展示群成员昵称*/
	public void setShowGroupMemNm(boolean showGroupMemNm) {
		this.showGroupMemNm = showGroupMemNm;
	}
	/**保存到通讯录*/
	public boolean isSaveToContacts() {
		return saveToContacts;
	}
	/**保存到通讯录*/
	public void setSaveToContacts(boolean saveToContacts) {
		this.saveToContacts = saveToContacts;
	}

	@Override
	public String toString() {
		return "UserInfoForGroup [userId=" + userId + ", groupNickname=" + groupNickname + ", noDisturbing="
				+ noDisturbing + ", toTop=" + toTop + ", showGroupMemNm=" + showGroupMemNm + ", saveToContacts="
				+ saveToContacts + "]";
	}
	
}
