package com.yucheng.im.service.entity;

import java.io.Serializable;

/**
 * 
* @Title: UserInfoBean.java
* @Package com.yucheng.im.service.entity
* @Description: 用户详细信息
* @author zhanggh@yusys.com.cn
* @date 2017年8月4日 下午6:03:29
* @version V1.0
*
 */
public class UserInfoBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 225334776835208461L;
	/**
	 * 标识用户唯一的主键  本系统中采用 cstNo+userNo 组成
	 */
	private String id;
	private String cstNo;
	private String nameCn;
	private String userNo;
	private String userName;
	private String mobile;
	private String email;
	private String phone;
	private String qqNo;
	/*private String wxQrcode;*/
	private String userPhoto;
	private String custUserName;
	private String region;
	
	
 
	/**地区*/
	public String getRegion() {
		return region;
	}
	/**地区*/
	public void setRegion(String region) {
		this.region = region;
	}
	private int friendCounts=0;
	
	private int groupCounts=0;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFriendCounts() {
		return friendCounts;
	}
	public void setFriendCounts(int friendCounts) {
		this.friendCounts = friendCounts;
	}
	public int getGroupCounts() {
		return groupCounts;
	}
	public void setGroupCounts(int groupCounts) {
		this.groupCounts = groupCounts;
	}
	public String getCustUserName() {
		return custUserName;
	}
	public void setCustUserName(String custUserName) {
		this.custUserName = custUserName;
	}
	/**
	 * 客户号
	 * @return
	 */
	public String getCstNo() {
		return cstNo;
	}
	/**
	 * 客户号
	 * @return
	 */
	public void setCstNo(String cstNo) {
		this.cstNo = cstNo;
	}
	/**
	 * 所属机构名称
	 * @return
	 */
	public String getNameCn() {
		return nameCn;
	}
	/**
	 * 所属机构名称
	 * @return
	 */
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	/**
	 *  用户编号
	 * @return
	 */
	public String getUserNo() {
		return userNo;
	}
	/**
	 * 用户编号
	 * @return
	 */
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	/**
	 * 用户名称
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 用户名称
	 * @return
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 手机号
	 * @return
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 手机号
	 * @return
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 电子邮件
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * 电子邮件
	 * @return
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 联系电话
	 * @return
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 联系电话
	 * @return
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * QQ号码
	 * @return
	 */
	public String getQqNo() {
		return qqNo;
	}
	/**
	 * QQ号码
	 * @return
	 */
	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}
	/**
	 * 微信二维码
	 * @return
	 */
	/*public String getWxQrcode() {
		return wxQrcode;
	}
	/**
	 * 微信二维码
	 * @return
	 *
	public void setWxQrcode(String wxQrcode) {
		this.wxQrcode = wxQrcode;
	}*/
	/***
	 * 用户图像
	 * @return
	 */
	public String getUserPhone() {
		return userPhoto;
	}
	/**
	 * 用户图像
	 * @return
	 */
	public void setUserPhone(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	@Override
	public String toString() {
		return "UserInfoBean [id=" + id + ", cstNo=" + cstNo + ", nameCn=" + nameCn + ", userNo=" + userNo
				+ ", userName=" + userName + ", mobile=" + mobile + ", email=" + email + ", phone=" + phone + ", qqNo="
				+ qqNo + ", userPhoto=" + userPhoto + ", custUserName=" + custUserName + ", region=" + region
				+ ", friendCounts=" + friendCounts + ", groupCounts=" + groupCounts + "]";
	} 
	 
	 

	 
	
}
