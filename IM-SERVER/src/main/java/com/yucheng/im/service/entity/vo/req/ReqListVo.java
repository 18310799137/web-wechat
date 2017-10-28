package com.yucheng.im.service.entity.vo.req;

/***
 * @Description:展示用户好友申请列表所需的VO类
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月14日下午8:48:17
 * @version V1.0
 */
public class ReqListVo {

	//申请人的id
	private String reqUserId;
	//申请人的姓名
	private String reqUserName;
	//申请 人的图像
	private String userPhoto;
	//申请的验证信息
	private String reqMsg;
	@Override
	public String toString() {
		return "ReqListVo [reqUserId=" + reqUserId + ", reqUserName=" + reqUserName + ", userPhoto=" + userPhoto
				+ ", reqMsg=" + reqMsg + "]";
	}
	public String getReqUserId() {
		return reqUserId;
	}
	public void setReqUserId(String reqUserId) {
		this.reqUserId = reqUserId;
	}
	public String getReqUserName() {
		return reqUserName;
	}
	public void setReqUserName(String reqUserName) {
		this.reqUserName = reqUserName;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}
	
}
