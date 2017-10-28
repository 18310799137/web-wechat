$(function(){
	//点击新的朋友，将未读好友申请标记为已读
	$("#txlNewFriendsBox").on("click",function(){
		DwrUserFriendServiceImpl.modifyUserMsgStatus(function(result){
		});
		removeUnReadMsg();
		
	});
	
	//点击接受，将该好友申请标记为已处理、已读
//	$("#contactList").on("click","button",function(){
//		var friendLi = $(this).parent().parent()["0"];
//		console.info(friendLi);
//		
//		removeUnReadMsg();
//	});
});

function removeUnReadMsg(){
	//删除好友请求未读信息红点提示
	$("#unReadReqCount").removeClass("unreadMes");
	$("#unReadReqCount").text("");
	$("#newUserReqMsgNumber").removeClass("unreadMes");
	$("#newUserReqMsgNumber").text("");
}