package com.yucheng.im.service.entity.msg;

import java.io.Serializable;

/**
 * 
* @Title: GroupMessage.java  
* @Package com.yucheng.im.service.web.mq.message.body  
* @Description:群组消息格式定义类
* @author zhanggh@yusys.com.cn
* @date 2017年8月30日  
* @version V1.0
 */
public class GroupMessage extends BaseMsg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2549731260009929595L;
	private String uuid;
	/**
	 *发送消息 的用户id
	 */
	private String fromUserId;
	/**
	 * 要发送到的群id
	 */
	private String toGroupId;
	/**
	 * 是否是系统通知消息 ,系统通知消息在消息框最中间小字体展示
	 */
	private String isNotice;
	
	private String oprType;
	/**
	 * 发送时间
	 */
	private String sendTime;
	/**
	 * 发送内容
	 */
	private String content;
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
	public String getToGroupId() {
		return toGroupId;
	}
	public void setToGroupId(String toGroupId) {
		this.toGroupId = toGroupId;
	}
	public String getIsNotice() {
		return isNotice;
	}
	public void setIsNotice(String isNotice) {
		this.isNotice = isNotice;
	}
	public String getOprType() {
		return oprType;
	}
	public void setOprType(String oprType) {
		this.oprType = oprType;
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
	
	
 public GroupMessage(String uuid, String fromUserId, String toGroupId, String isNotice, String oprType,
			String sendTime, String content) {
		super();
		this.uuid = uuid;
		this.fromUserId = fromUserId;
		this.toGroupId = toGroupId;
		this.isNotice = isNotice;
		this.oprType = oprType;
		this.sendTime = sendTime;
		this.content = content;
	}
@Override
	public String toString() {
		return "GroupMessage [uuid=" + uuid + ", fromUserId=" + fromUserId + ", toGroupId=" + toGroupId + ", isNotice="
				+ isNotice + ", oprType=" + oprType + ", sendTime=" + sendTime + ", content=" + content + "]";
	}
public GroupMessage() {
	// TODO Auto-generated constructor stub
}
	
}
