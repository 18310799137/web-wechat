package com.yucheng.im.service.entity;
/***
 * 
* @Title: UserInfoSolrIndex.java
* @Package com.yucheng.im.service.entity
* @Description: 查询用户索引信息  返回的临时类
* @author zhanggh@yusys.com.cn
* @date 2017年8月4日 下午6:04:41
* @version V1.0
*
 */
public class UserInfoSolrIndex {

	private String id;
	private String custUserName;
	
	public UserInfoSolrIndex() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustUserName() {
		return custUserName;
	}

	public void setCustUserName(String custUserName) {
		this.custUserName = custUserName;
	}

	@Override
	public String toString() {
		return "UserInfoSolrIndex [id=" + id + ", custUserName=" + custUserName + "]";
	}

	public UserInfoSolrIndex(String id, String custUserName) {
		super();
		this.id = id;
		this.custUserName = custUserName;
	}
	
	 
	
}
