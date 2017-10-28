package com.yucheng.im.service.entity.vo.session;

public class BaseSessionVo {
		//区别会话列表中 单独会话与群组会话的标识
		protected String sessionFlag;
		//获取未读消息数量
		protected int unReadMsgNum;
		//获取最后一条消息记录展示在会话列表中 
		protected String content;
		//消息的发送时间
		protected String sendTime;
		
		
		
		@Override
		public String toString() {
			return "BaseSessionVo [sessionFlag=" + sessionFlag + ", unReadMsgNum=" + unReadMsgNum + ", content="
					+ content + ", sendTime=" + sendTime + "]";
		}
		public String getSendTime() {
			return sendTime;
		}
		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}
		public String getSessionFlag() {
			return sessionFlag;
		}
		public void setSessionFlag(String sessionFlag) {
			this.sessionFlag = sessionFlag;
		}
		public int getUnReadMsgNum() {
			return unReadMsgNum;
		}
		public void setUnReadMsgNum(int unReadMsgNum) {
			this.unReadMsgNum = unReadMsgNum;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
}
