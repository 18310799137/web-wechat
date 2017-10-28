package com.yucheng.im.service.entity.vo.session;

import java.util.List;

/**
 * @Description:页面刚加载时需要查询
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月12日下午5:44:31
 * @version V1.0
 */
public class QueryGroupSessionVo extends BaseSessionVo{
	//聊天的群id
	private String toChatGroupId;
	//聊天的群名称
	private String toChatGroupName;
	//聊天的群的图像集合
	private List<String>userPhotos;
	//聊天窗口顶部的名称
	private String defaultName;
	
	//发送最后一条消息记录的人的名称
	private String lastChatName;
	//最后一条消息发送人的图像
	private String lastChatPhotos;
	//最后一条消息发送人的id
	private String lastChatId;
	
	public String getLastChatId() {
		return lastChatId;
	}
	public void setLastChatId(String lastChatId) {
		this.lastChatId = lastChatId;
	}
	public String getLastChatPhotos() {
		return lastChatPhotos;
	}
	public void setLastChatPhotos(String lastChatPhotos) {
		this.lastChatPhotos = lastChatPhotos;
	}
	public String getLastChatName() {
		return lastChatName;
	}
	public void setLastChatName(String lastChatName) {
		this.lastChatName = lastChatName;
	}
	public String getToChatGroupId() {
		return toChatGroupId;
	}
	public void setToChatGroupId(String toChatGroupId) {
		this.toChatGroupId = toChatGroupId;
	}
	public String getToChatGroupName() {
		return toChatGroupName;
	}
	public void setToChatGroupName(String toChatGroupName) {
		this.toChatGroupName = toChatGroupName;
	}
	public List<String> getUserPhotos() {
		return userPhotos;
	}
	public void setUserPhotos(List<String> userPhotos) {
		this.userPhotos = userPhotos;
	}
	
	public String getDefaultName() {
		return defaultName;
	}
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}
	@Override
	public String toString() {
		return "QueryGroupSessionVo [toChatGroupId=" + toChatGroupId + ", toChatGroupName=" + toChatGroupName
				+ ", userPhotos=" + userPhotos + ", defaultName=" + defaultName + ", lastChatName=" + lastChatName
				+ ", lastChatPhotos=" + lastChatPhotos + ", lastChatId=" + lastChatId + "]";
	}
 
}
