package com.yucheng.im.service.entity.vo.contact;

import java.util.List;

/**
 * 
 * @Description:查询用户联系人群组列表
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月18日下午5:05:47
 * @version V1.0
 */
public class ContactGroupVo {

	//群组id
	private String groupId;
	//群名称
	private String groupDefaultName;
	//群名称
	private String groupName;
	//群图像列表
	private List<String>userPhotos;
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getUserPhotos() {
		return userPhotos;
	}
	public void setUserPhotos(List<String> userPhotos) {
		this.userPhotos = userPhotos;
	}
	public String getGroupDefaultName() {
		return groupDefaultName;
	}
	public void setGroupDefaultName(String groupDefaultName) {
		this.groupDefaultName = groupDefaultName;
	}
	@Override
	public String toString() {
		return "ContactGroupVo [groupId=" + groupId + ", groupDefaultName=" + groupDefaultName + ", groupName="
				+ groupName + ", userPhotos=" + userPhotos + "]";
	}
 
	
	
}
