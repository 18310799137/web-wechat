package com.yucheng.im.service.entity.msg;

import java.io.Serializable;

/**
 * 
 * @Title: SysBroadCastMessage.java
 * @Package com.yucheng.im.service.web.mq.message.body
 * @Description: 系统广播消息
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月30日
 * @version V1.0
 */
public class SysBroadCastMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4375803825661527420L;

	private String fromUserId;

	private String toUserIdFlag;

	private String content;

	private String sendTime;

	public SysBroadCastMessage() {
	}

	public SysBroadCastMessage(String fromUserId, String toUserIdFlag,
			String content, String sendTime) {
		super();
		this.fromUserId = fromUserId;
		this.toUserIdFlag = toUserIdFlag;
		this.content = content;
		this.sendTime = sendTime;
	}

	@Override
	public String toString() {
		return "SysBroadCastMessage [fromUserId=" + fromUserId
				+ ", toUserIdFlag=" + toUserIdFlag + ", content=" + content
				+ ", sendTime=" + sendTime + "]";
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserIdFlag() {
		return toUserIdFlag;
	}

	public void setToUserIdFlag(String toUserIdFlag) {
		this.toUserIdFlag = toUserIdFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}
