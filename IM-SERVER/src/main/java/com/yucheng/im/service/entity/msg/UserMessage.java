package com.yucheng.im.service.entity.msg;

import java.io.Serializable;

/**
 * 
* @Title: UserMessage.java  
* @Package com.yucheng.im.service.web.mq.message.body  
* @Description: 用户 - 用户 类消息格式  
* @author zhanggh@yusys.com.cn
* @date 2017年8月30日  
* @version V1.0
 */
public class UserMessage extends BaseMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7503447296812190779L;

	private String uuid;
	/**发送者id*/
	private String fromUserId;
	
	/**接收者id*/
	private String toUserId;
	/**
	 * 是否是系统通知消息 {用户请求消息 与 响应消息} Y是请求类消息,N为聊天消息
	 */
	private String isNotice;
	/**
	 * 发送时间
	 */
	private String sendTime;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 *消息类型  <r为请求类型消息，p为响应类型消息>
	 */
	private String mType;
	//是否未读 Y为未读消息,N为已读消息
	private String isUnread;
	
	/**处理状态 Y为同意,N为拒绝*/
	private String disposeStatus;
	public UserMessage() {
	}
	public UserMessage(String uuid, String fromUserId, String toUserId, String isNotice, String sendTime,
			String content, String mType, String isUnread, String disposeStatus) {
		super();
		this.uuid = uuid;
		this.fromUserId = fromUserId;
		this.toUserId = toUserId;
		this.isNotice = isNotice;
		this.sendTime = sendTime;
		this.content = content;
		this.mType = mType;
		this.isUnread = isUnread;
		this.disposeStatus = disposeStatus;
	}
	@Override
	public String toString() {
		return "UserMessage [uuid=" + uuid + ", fromUserId=" + fromUserId + ", toUserId=" + toUserId + ", isNotice="
				+ isNotice + ", sendTime=" + sendTime + ", content=" + content + ", mType=" + mType + ", isUnread="
				+ isUnread + ", disposeStatus=" + disposeStatus + "]";
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(String isNotice) {
		this.isNotice = isNotice;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getIsUnread() {
		return isUnread;
	}
	public void setIsUnread(String isUnread) {
		this.isUnread = isUnread;
	}
	public String getDisposeStatus() {
		return disposeStatus;
	}
	public void setDisposeStatus(String disposeStatus) {
		this.disposeStatus = disposeStatus;
	}
	 
	
}
