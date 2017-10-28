package com.yucheng.im.service.entity.vo.detail;

import java.util.List;

import com.yucheng.im.service.entity.vo.group.GroupMemberVo;

/**
 * 
 * @Description:点击聊天窗口右上角 返回群组成员列表 群公告等信息
 * @author zhanggh@yusys.com.cn
 * @date 2017年9月29日上午9:40:52
 * @version V1.0
 */
public class QueryGroupResultVo {
	public boolean getIsMaster() {
		return isMaster;
	}
	public void setIsMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}
	//群名称
	private String groupName;
	//群公告
	private String groupNotice;
	//是否为群主	
	private boolean isMaster;
	//群成员列表
	private List<GroupMemberVo>detailVOs;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupNotice() {
		return groupNotice;
	}
	public void setGroupNotice(String groupNotice) {
		this.groupNotice = groupNotice;
	}
	
	public List<GroupMemberVo> getDetailVOs() {
		return detailVOs;
	}
	public void setDetailVOs(List<GroupMemberVo> detailVOs) {
		this.detailVOs = detailVOs;
	}
	@Override
	public String toString() {
		return "QueryGroupResultVo [groupName=" + groupName + ", groupNotice=" + groupNotice + ", isMaster=" + isMaster
				+ ", detailVOs=" + detailVOs + "]";
	}
	
}
