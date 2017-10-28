package com.yucheng.im.service.entity.vo.chat.group;

/**
 * 
 * @Description:前端查询群组的聊天记录 返回给客户端的VO类
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月18日下午1:20:42
 * @version V1.0
 */
public class GroupChatContentRecordVo {
	//发送者的id
	private String userId;
	//发送者的姓名
	private String userName;
	//发送者的图像地址
	private String userPhoto;
	//聊天内容
	private String content;
	//发送时间
	private String sendTime;
	//如果发送人是自己 则在右边展示 取值参考WebConstants.Message
	private String flag;
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "GroupChatContentVo [userId=" + userId + ", userName=" + userName + ", userPhoto=" + userPhoto
				+ ", content=" + content + ", sendTime=" + sendTime + ", flag=" + flag + "]";
	}
	
	
}
