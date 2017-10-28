package com.yucheng.im.service.entity.vo.chat;

/**
 * 
 * @Description: 查询好友聊天记录 返回给客户端的VO  聊天内容的列表
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月15日下午4:19:53
 * @version V1.0
 */
public class FriendChatRecordListVo {

	//如果发送人是自己 则在右边展示 取值参考WebConstants.Message
	private String flag;
	//聊天内容
	private String content;
	//发送时间
	private String sendTime;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	@Override
	public String toString() {
		return "FriendChatRecordListVo [flag=" + flag + ", content=" + content + ", sendTime=" + sendTime + "]";
	}
	
}
