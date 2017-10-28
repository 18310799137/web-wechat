package com.yucheng.im.service.entity.vo.session;

import java.util.List;
/**
 * 
 * @Description:存储会话列表的VO
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月12日下午7:48:31
 * @version V1.0
 */
public class QueryUserSessionListsVo {
	//会话消息未读总数量
	private int unReadSessionCount;
	//好友申请消息未读总数量
	private int unReadReqCount;
	//存储会话列表的集合
	private List<? super BaseSessionVo>sessionLists;
	
	@Override
	public String toString() {
		return "QueryUserSessionListsVo [unReadSessionCount=" + unReadSessionCount + ", unReadReqCount="
				+ unReadReqCount + ", sessionLists=" + sessionLists + "]";
	}
	public int getUnReadSessionCount() {
		return unReadSessionCount;
	}
	public void setUnReadSessionCount(int unReadSessionCount) {
		this.unReadSessionCount = unReadSessionCount;
	}
	public int getUnReadReqCount() {
		return unReadReqCount;
	}
	public void setUnReadReqCount(int unReadReqCount) {
		this.unReadReqCount = unReadReqCount;
	}
	public List<? super BaseSessionVo> getSessionLists() {
		return sessionLists;
	}
	public void setSessionLists(List<? super BaseSessionVo> sessionLists) {
		this.sessionLists = sessionLists;
	}
}
