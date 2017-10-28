package com.yucheng.im.service.entity;
/**
 * 
* @Title: FriendInfoBean.java  
* @Package com.yucheng.im.service.entity  
* @Description: 存储好友所能展示的详细信息
* @author zhanggh@yusys.com.cn
* @date 2017年8月25日  
* @version V1.0
 */
public class FriendInfoBean {
	//好友id
	private String userId;
	//好友备注
	private String remarks;
	//消息免打扰
	private boolean noDisturbing;
	//置顶聊天
	private boolean toTop;
	//来源
	private String source;


	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**备注信息*/
	public String getRemarks() {
		return remarks;
	}
	/**备注信息*/
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**消息免打扰  取值范围[true=免打扰,false=接收消息]*/
	public boolean isNoDisturbing() {
		return noDisturbing;
	}
	/**消息免打扰  取值范围[true=免打扰,false=接收消息]*/
	public void setNoDisturbing(boolean noDisturbing) {
		this.noDisturbing = noDisturbing;
	}
	/**置顶聊天  取值范围[true=置顶,false=不置顶]*/
	public boolean isToTop() {
		return toTop;
	}
	/**置顶聊天  取值范围[true=置顶,false=不置顶]*/
	public void setToTop(boolean toTop) {
		this.toTop = toTop;
	}
	/**添加途径 好友来源 参加servConstans中 好友来源取值范围*/
	public String getSource() {
		return source;
	}
	/**添加途径 好友来源 参加servConstans中 好友来源取值范围*/
	public void setSource(String source) {
		this.source = source;
	}
	
}
