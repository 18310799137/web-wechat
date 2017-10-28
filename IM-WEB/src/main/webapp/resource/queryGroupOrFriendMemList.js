/***
 * 点击右上角的按钮 查询好友详情 或者 群组详情
 * @returns
 */
function queryGroupOrFriendList(){

	$("#chatWindowFriendDeatailTagA").click(function(){
		var flag = $("#sendButton").attr("sessionFlag");
		var toChatId = $("#sendButton").attr("toChatId");
		if(flag=="SU"){
				//右上角省略号点击事件
				if($("#add_members_box_person").css("right")=="-188px"){
					
				
					
					$("#add_members_box_person").animate({"right":"0"},300);
					if($(".group_add_members_top").is(".hidden")){
						$("#addMembersFriend").height($("#add_members_box_person").height());
					}else{
						$("#addMembersFriend").height($("#add_members_box_person").height()-$(".group_add_members_top").height()-$(".delete_and_exit_box").height()-10);
					}
				}else{
					$("#add_members_box_person").animate({"right":"-188px"},300);
				}
		}else if(flag=="SG"){
			//右上角省略号点击事件
				if($("#addMemberBoxGroup").css("right")=="-188px"){
					//遍历列表
					//查询群组成员列表以及 群的公共 群名称 当前登录用户是否为群主
					DwrUserGroupServiceImpl.queryGroupMemberListAndGroupDetail(toChatId,function(resultData){
						resultData = $.parseJSON(resultData);
						if(null==resultData){
							return false;
						}
						$("#addMemberBoxGroupName").val('');
						$("#addMemberBoxGroupNotice").val('');
						$("#addMembersGroup").empty();
						if(resultData.groupName!=""){
							$("#addMemberBoxGroupName").val(resultData.groupName);
						}
						if(resultData.groupNotice!=""){
							$("#addMemberBoxGroupNotice").val(resultData.groupNotice);
						}
					
						if(!resultData.isMaster){
							//非群主只展示添加按钮 禁止修改群昵称和群公告
							$("#addMemberBoxGroupName").attr("disabled","disabled");
							$("#addMemberBoxGroupNotice").attr("disabled","disabled");
							
							var addLi = $("<li class='add_members_li' id='addMembersBtnGroup'>"
									+"<img src='/IM-WEB/resource/img/add_pic.jpg'  photoFlag='false'/>"
									+"<p class='members_name'>添加成员</p></li>");
							$("#addMembersGroup").append(addLi);
						
						}else{
							//群主展示添加 和删除按钮 可以修改群昵称和群公告
							$("#addMemberBoxGroupName").removeAttr("disabled");
							$("#addMemberBoxGroupNotice").removeAttr("disabled");
							
							var addLi = $("<li class='add_members_li' id='addMembersBtnGroup'>"
									+"<img src='/IM-WEB/resource/img/add_pic.jpg' photoFlag='false'/>"
									+"<p class='members_name'>添加成员</p></li>");
							var minLi = $("<li class='add_members_li' id='minMembersBtn'>"
									+"<img src='/IM-WEB/resource/img/minus_pic.jpg'  photoFlag='false'/>"
									+"<p class='members_name'>删除成员</p></li>");
							
							$("#addMembersGroup").append(addLi);
							$("#addMembersGroup").append(minLi);
						}
						//清空成员列表
						//$("#addMembersGroup").empty();
						var groupMembers = resultData.detailVOs;
						//遍历成员列表
						for(var i in groupMembers){
							var object = groupMembers[i];
							var li = $("<li class='add_members_li'>" +
									"<img isUserImg='true' src='"+object.userPhoto+"' class='user_tx' toChatId='"+object.userId+"' groupId='"+toChatId+"' sessionFlag='SG' userName='"+object.userName+"' />"+
									"<p class='members_name'>"+object.userName+"</p></li>")
							$("#addMembersGroup").append(li);
						}
						
						//展开div
						$("#addMemberBoxGroup").animate({"right":"0"},300);
						if($(".group_add_members_top").is(".hidden")){
							$("#addMembersGroup").height($("#addMemberBoxGroup").height());
						}else{
							$("#addMembersGroup").height($("#addMemberBoxGroup").height()-$(".group_add_members_top").height()-$(".delete_and_exit_box").height()-10);
						}
						
						$("#addMembersGroup").find("li").click(function(){
								//点击添加群成员按钮
							if($(this).attr("id")=="addMembersBtnGroup"){
								//将添加还是移除的标记传入选择框中
								$(".create_group_sure_btn").attr("oprFlag","add");
								$(".create_group_sure_btn").attr("groupId",toChatId);
								var memberIds = [];
								$("#addMembersGroup > li").each(function(index){
									var chatId = $(this).find("img").attr("toChatId");
									if(null!=chatId && undefined!=chatId && ""!=chatId){
										memberIds.push(chatId);
									}
								})
								addFriendListForCreateGroup(memberIds);
								$(".create_group_box").show();
								//关闭div
								$("#addMemberBoxGroup").animate({"right":"-188px"},300);
							}else if($(this).attr("id")=="minMembersBtn"){
								var objArray = [];
								$("#addMembersGroup > li").each(function(index){
									var userId = $(this).find("img").attr("toChatId");
									var userPhoto = $(this).find("img").attr("src");
									var userName = $(this).find("img").attr("userName");
									if(null!=userId && undefined!=userId && ""!=userId){
										var object = new Object;
										object.userId = userId;
										object.userPhoto = userPhoto;
										object.userName = userName;
										objArray.push(object);
									}
								})
								//移除群成员按钮
								$(".create_group_sure_btn").attr("oprFlag","rem");
								$(".create_group_sure_btn").attr("groupId",toChatId);
								addFriendListForCreateGroup(objArray);
								$(".create_group_box").show();
								//关闭div
								$("#addMemberBoxGroup").animate({"right":"-188px"},300);
							}else{
								return;
							}
							
						});
					});
					
				}else{
					//关闭div
					$("#addMemberBoxGroup").animate({"right":"-188px"},300);
				}
		
		}else{
			alert("[queryGroupOrFriendMemList.js : 14 ] Server ERROR - QUERY FRIEND OR GROUP NO FLAG");
		} 
	});
}

function replace_space(str) {
    return str.replace(/^\s+|\s+$/gm,'');
}
function replace_em(str){
	str = str.replace(/\</g,'&lt;');
	str = str.replace(/\>/g,'&gt;');
	str = str.replace(/\n/g,'<br/>');
	str = str.replace(/\[em_([0-9]*)\]/g,'<img src="/IM-WEB/qqFace/arclist/$1.gif" border="0" style="width:20px"/>');
	return str;
}
$(function(){
	$('.icon_face').qqFace({
		id : 'facebox', 
		assign:'sendMessageContext', 
		path:'/IM-WEB/qqFace/arclist/'	//表情存放的路径
	});
	
	
	//快捷发送按钮事件
$(document).keydown(function(evt){
 if((evt.shiftKey  && evt.which == 13 )|| evt.which == 10) {
	/*	 var chatValue = $("#sendMessageContext").val();
		 $("#sendMessageContext").html(chatValue+'<br/>');*/
	} else	if(evt.which==13){
		if(!$("#centerChatBox").is(':hidden')){
			$("#sendButton").click();
			evt.preventDefault();
			evt.stopPropagation();
			return false;
		}
	}

});
})

$(function(){
	
	
	
	
	
	
	//点击与个人会话时 右上角详情中的加号(+) 弹出添加群成员面板
	$("#addMembersFriend").find("li").click(function(){
		//点击添加群成员按钮
	if($(this).attr("id")=="addMembersBtnPerson"){
		//将添加还是移除的标记传入选择框中
		addFriendListForCreateGroup([]);
		$(".create_group_box").show();
		//关闭div
		$("#addMemberBoxGroup").animate({"right":"-188px"},300);
	}
});
	
	
	
	//聊天界面的右上角点击出现会话详情，好友或者群组的列表
	queryGroupOrFriendList();
	$("#addMemberBoxGroupName").blur(function(){
		var groupInfo = new Object();
		groupInfo.groupId=$("#sendButton").attr("toChatId");
		groupInfo.groupName = $("#addMemberBoxGroupName").val();
		groupInfo.groupNotice = $("#addMemberBoxGroupNotice").val();
		if(groupInfo.groupName=="" && groupInfo.groupNotice==""){
			return false;
		}
		//修改群名称时   一起修改会话列表中的群名称和通讯录中的群名称
		
		if($.trim(groupInfo.groupName)!=""){
			//修改会话列表中的群名称
			var oldLiSession = $("#sessionListUl > li[chatId='"+groupInfo.groupId+"']");
			oldLiSession.attr("defaultName",groupInfo.groupName);
			oldLiSession.find("span").each(function(index){
				if(0==index){
					$(this).text(groupInfo.groupName)
				}
			})
			//修改通讯录中的群名称
			var oldLiContact = $("#txlGroupChatBox > li[groupId='"+groupInfo.groupId+"']");
			oldLiContact.attr("groupName",groupInfo.groupName);
			oldLiContact.find("p").text(groupInfo.groupName);
		}
		DwrUserGroupServiceImpl.modifyGroupInfo(JSON.stringify(groupInfo));
	});
	$("#addMemberBoxGroupNotice").blur(function(){
		var groupInfo = new Object();
		groupInfo.groupId=$("#sendButton").attr("toChatId");
		groupInfo.groupName = $("#addMemberBoxGroupName").val();
		groupInfo.groupNotice = $("#addMemberBoxGroupNotice").val();
		if(groupInfo.groupName=="" && groupInfo.groupNotice==""){
			return false;
		}
		DwrUserGroupServiceImpl.modifyGroupInfo(JSON.stringify(groupInfo));
	});
});