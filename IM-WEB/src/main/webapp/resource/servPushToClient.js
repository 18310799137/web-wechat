//接收服务端推送的群组消息
function servPushToClientGroupMsg(groupMessage){
	//刷新通讯录群组列表
 	queryGroupList();
	groupMessage=$.parseJSON(groupMessage);
	if(null==groupMessage){
		return false;
	}
	
	pushMessageInSessionList(groupMessage);
}
//接收服务端推送的好友消息
function servPushToClientUserChatMsg(userMessage){
	userMessage=$.parseJSON(userMessage);
	if(null==userMessage){
		return false;
	}
	pushMessageInSessionList(userMessage);
}

function showMessageInSessionList(sessionArray){
	for(var i in sessionArray){
		var obj = sessionArray[i];
		var unReadMsgNum = obj.unReadMsgNum;
		var li=null;
		
		//如果是对个人的聊天
		if(obj.sessionFlag=="SU"){
			li = $("<li sessionFlag='SU' isFriend='"+obj.isFriend+"' chatId='"+obj.userId+"' defaultName='"+obj.userName+"'><img src='"+obj.userPhoto+"' /> <div class='name_and_con'> <span>"+obj.userName+"</span> <p>"+obj.content+"</p> </div></li>");
		}
		//对群组的聊天 TODO
		else if(obj.sessionFlag=="SG"){
			
			var photoArray = obj.userPhotos;
			var groupSize=photoArray.length;
			if(groupSize<3){
				continue;
			}
			if(groupSize==3){
				// 群聊为三个人的时候  
					li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
						+"<li style='width: 50%; height: 50%; margin: 0 auto;'> <img src='"+photoArray[0]+"' /></li>"
						+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[1]+"' /></li>"
						+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[2]+"' /></li>"
						+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p> </div></li>")
			}
			if(groupSize==4){
				// 群聊为四个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
				+"<li style='float: left; width: 50%; height: 50%;'> <img src='"+photoArray[0]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img 	src='"+photoArray[1]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img  src='"+photoArray[2]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[3]+"' /></li>"
				+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>	")
			}
			if(groupSize==5){
				// 群聊为五个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'><li class='clearfix' style='text-align: center; height: 33.333%; margin-top: 18%;'>"
						+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
						+"<img src='"+photoArray[0]+"'/></div>"
						+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
						+"<img src='"+photoArray[1]+"'/></div></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[2]+"' /></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[3]+"' /></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[4]+"' /></li>"
						+"</ol> <div class='name_and_con'><span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize==6){
				// 群聊为六个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span> <p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p> </div> </li>");
			}
			if(groupSize==7){
				// 群聊为七个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'> <ol class='group_img_box'>"
					+"<li style='width: 33.333%; height: 33.333%; margin: 0 auto;'>"
					+"<img src='"+photoArray[0]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[1]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img 	src='"+photoArray[4]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
					+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize==8){
				// 群聊为八个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
					+"<li class='clearfix' style='text-align: center; height: 33.333%;'>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+photoArray[0]+"' /> </div>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+photoArray[1]+"' /> </div></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[7]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
					+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize>=9){
				// 群聊为九个人及以上的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'> <ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[7]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[8]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
				+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
		}
		if(obj.unReadMsgNum>0){
			li.prepend("<i class='unreadMes'>"+unReadMsgNum+"</i>");
		}
		if(li!=null){
			$("#sessionListUl > li[chatId="+(li.attr('chatId'))+"]").remove();
		}
		$("#sessionListUl").append(li);
		
	}
	
}


//从服务端推送的消息展示到会话列表
function pushMessageInSessionList(obj){
	//$("#sessionListUl").find(obj.userId).remove();
		var li=null;
		var div;
		
		
		//如果是对个人的聊天
		if(obj.sessionFlag=="SU"){
			//创建一个展示到会话列表的li
			li = $("<li sessionFlag='SU'  isFriend='"+obj.isFriend+"'   chatId='"+obj.userId+"' defaultName='"+obj.userName+"'>"
						+"<img src='"+obj.userPhoto+"' /> <div class='name_and_con'>"
						+"<span>"+obj.userName+"</span> <p>"+obj.content+"</p> </div></li>");
			
			//查询推送的这条消息是否原先存在,如果存在取出原先的未读消息数量 进行相加
			var newUnreadMsg = 1;
			var oldUnReadMsg = $("#sessionListUl > li[chatId='"+obj.userId+"']").find(".unreadMes").text();
			if(oldUnReadMsg!=""){
				newUnreadMsg=newUnreadMsg+parseInt(oldUnReadMsg);
			}
			//将原先的li标签删除,重新创建一个li标签 用来展示服务端推送的好友最新消息 
			var oldLi = $("#sessionListUl > li[chatId='"+obj.userId+"']");
			 
			 
		if(obj.append!="false"){
			   div=$("<div class='otherUserBox'>"
						+"<div class='otherUserDiv clearfix'>"
						+"<img src='"+obj.userPhoto+"' width='40' class='user_tx' toChatId='"+obj.userId+"' isUserImg='true' />"
						+"<div class='otherUserConBox'>"
						+"<div class='group_chat_user_title'>"+obj.userName+"</div>"
						+"<div class='otherUserCon'> <i></i>"
						+"<p>"+obj.content+"</p> </div>"
						+"<div class='chat_time'>"+obj.sendTime+"</div></div></div></div>");
			
			 
			//取出原先查询出的消息内容 进行传递
			 li.attr("chatContent",(oldLi.attr("chatContent")==undefined?'':oldLi.attr("chatContent"))+div.get(0).outerHTML);
		}else{
			li.attr("chatContent",oldLi.attr("chatContent"));
		}
			 //取出原先的查询标识进行传递
			 if(oldLi.attr("queryFlag")!=undefined && oldLi.attr("queryFlag")!="" && oldLi.attr("queryFlag")=="TRUE"){
					li.attr("queryFlag",oldLi.attr("queryFlag"))
				}
			 oldLi.remove();
			 li.prepend("<i class='unreadMes'>"+newUnreadMsg+"</i>");
			 $("#sessionListUl").prepend(li);
		}
		//对群组的聊天 TODO
		else if(obj.sessionFlag=="SG"){
			var photoArray = obj.userPhotos;
			var groupSize=photoArray.length;
			if(groupSize==3){
				//群聊为三个人的时候
					li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
						+"<li style='width: 50%; height: 50%; margin: 0 auto;'> <img src='"+photoArray[0]+"' /></li>"
						+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[1]+"' /></li>"
						+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[2]+"' /></li>"
						+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p> </div></li>")
			}
			if(groupSize==4){
				// 群聊为四个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
				+"<li style='float: left; width: 50%; height: 50%;'> <img src='"+photoArray[0]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img 	src='"+photoArray[1]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img  src='"+photoArray[2]+"' /></li>"
				+"<li style='float: left; width: 50%; height: 50%;'><img src='"+photoArray[3]+"' /></li>"
				+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>	")
			}
			if(groupSize==5){
				//群聊为五个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'><li class='clearfix' style='text-align: center; height: 33.333%; margin-top: 18%;'>"
						+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
						+"<img src='"+photoArray[0]+"'/></div>"
						+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
						+"<img src='"+photoArray[1]+"'/></div></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[2]+"' /></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[3]+"' /></li>"
						+"<li style='float: left; width: 33.333%; height: 33.333%;'><img src='"+photoArray[4]+"' /></li>"
						+"</ol> <div class='name_and_con'><span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize==6){
				// 群聊为六个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"</ol> <div class='name_and_con'> <span >"+obj.toChatGroupName+"</span> <p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p> </div> </li>");
			}
			if(groupSize==7){
				// 群聊为七个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'> <ol class='group_img_box'>"
					+"<li style='width: 33.333%; height: 33.333%; margin: 0 auto;'>"
					+"<img src='"+photoArray[0]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[1]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img 	src='"+photoArray[4]+"'  /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
					+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize==8){
				// 群聊为八个人的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'><ol class='group_img_box'>"
					+"<li class='clearfix' style='text-align: center; height: 33.333%;'>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+photoArray[0]+"' /> </div>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+photoArray[1]+"' /> </div></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[7]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
					+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			if(groupSize>=9){
				// 群聊为九个人及以上的时候  
				li=$("<li sessionFlag='SG' chatId='"+obj.toChatGroupId+"' defaultName='"+obj.defaultName+"'> <ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[7]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+photoArray[8]+"' /></li>"
					+"</ol> <div class='name_and_con'>"
				+"<span >"+obj.toChatGroupName+"</span><p><span style='float:left'>"+obj.lastChatName+"：</span>"+obj.content+"</p></div></li>");
			}
			var newUnreadMsg = 1;
			var oldUnReadMsg = $("#sessionListUl > li[chatId='"+obj.toChatGroupId+"']").find(".unreadMes").text();
			if(oldUnReadMsg!=""){
				newUnreadMsg=newUnreadMsg+parseInt(oldUnReadMsg);
			}
			//筛选出已经存在的回话列表
			var oldLi =$("#sessionListUl > li[chatId='"+obj.toChatGroupId+"']");
			  
			  div=$("<div class='otherUserBox'>"
						+"<div class='otherUserDiv clearfix'>"
						+"<img sessionflag='SG' groupid='"+obj.toChatGroupId+"' isUserImg='true' src='"+obj.lastChatPhotos+"' width='40' class='user_tx' toChatId='"+obj.lastChatId+"'/>"
						+"<div class='otherUserConBox'>"
						+"<div class='group_chat_user_title'>"+obj.lastChatName+"</div>"
						+"<div class='otherUserCon'> <i></i>"
						+"<p>"+obj.content+"</p> </div>"
						+"<div class='chat_time'>"+obj.sendTime+"</div></div></div></div>");
			
			//取出原先查询出的消息内容 加上本次推送的消息  一块儿进行传递
			 li.attr("chatContent",oldLi.attr("chatContent")+div.get(0).outerHTML);
			 //判断是否查询过数据库
			if(oldLi.attr("queryFlag")!=undefined && oldLi.attr("queryFlag")!="" && oldLi.attr("queryFlag")=="TRUE"){
				li.attr("queryFlag",oldLi.attr("queryFlag"))
			}
			//删除原先的会话列表
			oldLi.remove();
			
			 li.prepend("<i class='unreadMes'>"+newUnreadMsg+"</i>");
			 $("#sessionListUl").prepend(li);
		}
		
		//判断当前的聊天窗口是否和推送消息的窗口一致,如果是在当前界面 拼接推送的内容
		var toChatId = $("#sendButton").attr("toChatId");
		if(toChatId==obj.toChatGroupId || toChatId==obj.userId){
			$("#chatArea").append(div);
		}
		
		
		$("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
		var oldUnreadSum = $("#unReadSessionCount").text();
		var newUnreadSum=1;
		if(oldUnreadSum!=""){
			oldUnreadSum = parseInt(oldUnreadSum);
			newUnreadSum=newUnreadSum+oldUnreadSum;
		}
		$("#unReadSessionCount").addClass("unreadMes");
		$("#unReadSessionCount").text(newUnreadSum);
		
		clickSessionList();
}


function clickSessionList(){
	//点击左侧会话列表点击事件
	$("#chatLeftDiv").find("li").unbind("click").bind("click",function(){
		$("#centerChatBox").show();
		//聊天框 获取焦点
		$("#sendMessageContext").focus();
		//隐藏通讯录面板
		$("#addressBook").hide();
		//通讯录详情隐藏
		$("#centerAddressBookBox").hide();
		//聊天内容面板内容清空
		$("#chatArea").empty();
		$(this).addClass("active").siblings().removeClass("active");
		//聊天窗口顶部的的 聊天人名称 设置为选择的列表中的聊天人
		$("#toChatWindowName").html($(this).attr("defaultName"));
		var friendId=$(this).attr("chatId");
		var sessionFlag=$(this).attr("sessionFlag");
		
		var unrdNumText = $(this).find(".unreadMes").text();
		if(unrdNumText!=""){
			var unrdNum = parseInt(unrdNumText);
			$(this).find(".unreadMes").remove();
			var unrdSumText = $("#unReadSessionCount").text();
			var unrdSum = parseInt(unrdSumText);
			//未读消息总数
			var newSum = unrdSum-unrdNum;
			//如果未读消息总数为0 则不显示
			if(newSum<=0){
				$("#unReadSessionCount").removeClass("unreadMes");
				$("#unReadSessionCount").text("");
			}else{
				$("#unReadSessionCount").text(newSum);
			}
		}
		$("#sendButton").attr("toChatId",friendId);
	 	$("#sendButton").attr("sessionFlag",sessionFlag);
	 	var oldChatContent = $(this).attr("chatContent");
	  
	 	
	 	
	 	var $this_li = $(this);
	 	//如果是第一次点击 则进行查询,否则使用节点中保存的已查询出的记录
	 	if($this_li.attr("queryFlag")==undefined || $this_li.attr("queryFlag")==""){
	 		
	 		if(sessionFlag=="SG"){
	 			$("#chatWindowAddFriendButton").hide()
				$("#chatWindowFriendDeatailTagA").show()
				DwrUserGroupServiceImpl.modifySessionListIsUnread(friendId);
				//对群组的聊天
				DwrUserGroupServiceImpl.queryGroupChatRecordList(friendId,function(resultData){
					resultData = $.parseJSON(resultData);
					if(null==resultData){
						return false;
					}
					var div;
					for(var i in resultData){
						var object = resultData[i];
						if(object.flag=="L"){
							//如果是朋友发送的消息 展示在左边
											  div=$("<div class='otherUserBox'>"
													+"<div class='otherUserDiv clearfix'>"
													+"<img isUserImg='true' src='"+object.userPhoto+"' width='40' class='user_tx' sessionFlag='SG' groupId='"+friendId+"' toChatId='"+object.userId+"'/>"
													+"<div class='otherUserConBox'>"
													+"<div class='group_chat_user_title'>"+object.userName+"</div>"
													+"<div class='otherUserCon'> <i></i>"
													+"<p>"+object.content+"</p> </div>"
													+"<div class='chat_time'>"+object.sendTime+"</div></div></div></div>")
										}
										else if(object.flag=="R"){
											//如果是自己发送的消息 展示在右边
											div=$("<div class='currentUserBox'>"
											+"<div class='currentUserDiv clearfix'>"
											+"<img isUserImg='true' src='"+object.userPhoto+"' width='40' class='user_tx' toChatId='"+object.userId+"'/>"
											+"<div class='currentUserConBox'><div class='currentUserCon'><i></i>"
											+"<p>"+object.content+"</p>"
											+"</div> <div class='chat_time'>"+object.sendTime+"</div></div></div></div>");
										}
						
										$("#chatArea").append(div);
					}
					if($("#chatArea").html()!=undefined && $("#chatArea").html()!=""){
						//将聊天内容 存入节点中
						$this_li.attr("chatContent",$("#chatArea").html());
					}

					$("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
				});
			}else if(sessionFlag=="SU"){
				//如果是好友关系  则为true 不显示加好友按钮
				var isFriends= $(this).attr("isFriend");
				if(isFriends=="true"){
					$("#chatWindowAddFriendButton").hide()
					$("#chatWindowFriendDeatailTagA").show()
				}else if(isFriends=="false"){
					$("#chatWindowFriendDeatailTagA").hide()
					$("#chatWindowAddFriendButton").show()
				}
				
				DwrUserFriendServiceImpl.modifySessionListStatus();
				//对个人的聊天
				DwrUserFriendServiceImpl.queryFriendChatRecordList(friendId,1,10,function(chatRecord){
					chatRecord=$.parseJSON(chatRecord);
					if(null==chatRecord ){
						return false;
					}
					var array = chatRecord.recordListVos;
					if(array==null||array.length<1){
						return false;
					}
					var div;
					for(var i in array){
						var object = array[i];
						if(object.flag=="L"){
							//如果是朋友发送的消息 展示在左边
											  div=$("<div class='otherUserBox'>"
													+"<div class='otherUserDiv clearfix'>"
													+"<img isUserImg='true' src='"+chatRecord.userPhoto+"' width='40' class='user_tx' toChatId='"+chatRecord.userId+"'/>"
													+"<div class='otherUserConBox'>"
													+"<div class='group_chat_user_title'>"+chatRecord.userName+"</div>"
													+"<div class='otherUserCon'> <i></i>"
													+"<p>"+object.content+"</p> </div>"
													+"<div class='chat_time'>"+object.sendTime+"</div></div></div></div>")
										}
										else if(object.flag=="R"){
											//如果是自己发送的消息 展示在右边
											div=$("<div class='currentUserBox'>"
											+"<div class='currentUserDiv clearfix'>"
											+"<img isUserImg='true' src='"+chatRecord.oneSelfPhoto+"' width='40' class='user_tx' toChatId='"+oneSelf.id+"'/>"
											+"<div class='currentUserConBox'><div class='currentUserCon'><i></i>"
											+"<p>"+object.content+"</p>"
											+"</div> <div class='chat_time'>"+object.sendTime+"</div></div></div></div>");
										}
									
									$("#chatArea").append(div);
							}
					if($("#chatArea").html()!=undefined && $("#chatArea").html()!=""){
						//将聊天内容 存入节点中
						$this_li.attr("chatContent",$("#chatArea").html());
					}
					
					$("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
				});	
			}
	 		
	 		//标识这个节点是否进行过记录查询
	 		$this_li.attr("queryFlag","TRUE");
	 	}else{
	 		$("#chatArea").empty();
	 		if(oldChatContent!=undefined){
		 		$("#chatArea").html(oldChatContent);
	 		}
	 		$("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
	 		
	 		if(sessionFlag=="SG"){
	 			$("#chatWindowAddFriendButton").hide()
				$("#chatWindowFriendDeatailTagA").show()
	 		}else{
	 			//如果是好友关系  则为true 不显示加好友按钮
				var isFriends= $(this).attr("isFriend");
				if(isFriends=="true"){
					$("#chatWindowAddFriendButton").hide()
					$("#chatWindowFriendDeatailTagA").show()
				}else if(isFriends=="false"){
					$("#chatWindowFriendDeatailTagA").hide()
					$("#chatWindowAddFriendButton").show()
				}
	 		}
	 	}
	 	$("#chatArea").scrollTop($("#chatArea")[0].scrollHeight);
	});
}



$(function(){
	//点击右上角 非好友的临时会话   加好友按钮点击事件
		$("#chatWindowAddFriendButton").click(function(){
			
			
			$("#backGroundDIV").width($(document).width())
			$("#backGroundDIV").height($(document).height())
			$("#backGroundDIV").show();
			$(".add_friends_request_box").show();
			var chatId = $("#sendButton").attr("toChatId");
		 
			$("#addFriendButtonReq").attr("chatId",chatId);
		});
		
		
		
		$("#txlNewFriendsBox").click(function(){
			 $("#centerAddressBookBox").show();
			 $("#centerChatBox").hide();
			//$("#centerAddressBookBox").hide();
		});
		
		queryGroupList();
})


/**
 * 遍历通讯类群组的列表
 * @returns
 */
function queryGroupList(){
	DwrUserGroupServiceImpl.queryUserGroupList(function(data){
		$("#txlGroupChatBox").empty();
		data=$.parseJSON(data);
		if(null==data || data.length<1){
			return false;
		}
		var li;
		for(var i in  data){
			var groupInfo = data[i];
			var groupPhotos = groupInfo.userPhotos;
			var groupSize = groupPhotos.length;
			//txlGroupChatBox$.parseJSON
			if(groupSize<3){
				continue;
			}
			if(groupSize==3){
					// 群聊为三个人的时候 
					li = $("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+">"
					+"<ol class='group_img_box'>"
					+"<li style='width: 50%; height: 50%; margin: 0 auto;'>"
					+"<img src='"+groupPhotos[0]+"' /></li>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[1]+"' /></li>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p> </li>");
				
			}else if(groupSize==4){
				// 群聊为四个人的时候 
				 li=$("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"> <ol class='group_img_box'>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[0]+"' /></li>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[1]+"' /></li>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"<li style='float: left; width: 50%; height: 50%;'>"
					+"<img src='"+groupPhotos[3]+"' /></li>"
					+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p> </li>");
			}else if(groupSize==5){
			// 群聊为五个人的时候 
			 li=$("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"> <ol class='group_img_box'>"
				+"<li class='clearfix' style='text-align: center; height: 33.333%; margin-top: 18%;'>"
				+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
				+"<img src='"+groupPhotos[0]+"'  /> </div>"
				+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
				+"<img src='"+groupPhotos[1]+"'  /> </div> </li>"
				+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
				+"<img src='"+groupPhotos[2]+"'  /></li>"
				+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
				+"<img src='"+groupPhotos[3]+"'  /></li>"
				+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
				+"<img src='"+groupPhotos[4]+"'  /></li>"
				+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p> </li>");
			}else if(groupSize==6){
				// 群聊为六个人的时候 
				li=$("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"><ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+groupPhotos[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+groupPhotos[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%; margin-top: 18%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[5]+"' /></li>"
					+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p></li>");
			}else if(groupSize==7){
			// 群聊为七个人的时候 
				li=$("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"> <ol class='group_img_box'>"
					+"<li style='width: 33.333%; height: 33.333%; margin: 0 auto;'>"
					+"<img src='"+groupPhotos[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[6]+"' /></li>"
					+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p></li>");
			}else if(groupSize==8){
				// 群聊为八个人的时候 
				li=$("</li><li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"><ol class='group_img_box'>"
					+"<li class='clearfix' style='text-align: center; height: 33.333%;'>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+groupPhotos[0]+"' /> </div>"
					+"<div style='display: inline-block; width: 33.333%; height: 33.333%; vertical-align: top;'>"
					+"<img src='"+groupPhotos[1]+"' /> </div>"
					+"</li><li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[7]+"' /></li> </ol>"
					+"<p class='userNickName'>"+groupInfo.groupName+"</p></li>");
			}else if(groupSize==9){
				// 群聊为九个人及以上的时候 
				li=$("<li class='userLi clearfix' groupId='"+groupInfo.groupId+"' groupName="+groupInfo.groupDefaultName+"><ol class='group_img_box'>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[0]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[1]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[2]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[3]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[4]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[5]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[6]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[7]+"' /></li>"
					+"<li style='float: left; width: 33.333%; height: 33.333%;'>"
					+"<img src='"+groupPhotos[8]+"' /></li>"
					+"</ol> <p class='userNickName'>"+groupInfo.groupName+"</p></li>");
			}
			$("#txlGroupChatBox").append(li);
		}
 		
 		//左侧群聊通讯录列表点击事件JSON.stringify(jsonobj)
		$("#txlGroupChatBox").find("li").on("click",function(){
			//删除其他元素选中效果
			$("#addressBook").find("li").removeClass("active");
			$(this).addClass("active");
			
			$(".txl_groupChat_detail").show();//群聊详情内容显示
			$(".txl_newFriend_detail_con").hide();//新的朋友详情内容隐藏
			$(".platform_or_buddy_detail_con").hide();//平台或者好友列表详情内容隐藏
			$(".btn_wrappper_cen").show();//发消息按钮
			var groupId = $(this).attr("groupId");
			var groupName = $(this).attr("groupName");
			//取出这个群的名称 展示在群详细信息窗口
			$("#contactWindowTitle").text(groupName);
			$("#toChatButton").attr("chatId",groupId);
			$("#toChatButton").attr("sessionFlag","SG");
			$("#toChatButton").attr("toChatName",groupName);
			
			
			/***点击通讯录列表，打开右边的详情面板*/
			 $("#centerAddressBookBox").show();
			 $("#centerChatBox").hide();
			//$("#centerAddressBookBox").hide();
			
			
			$(".txl_groupChat_detail").empty();
			//查询此群的 成员列表 展示到群详细信息中去
			DwrUserGroupServiceImpl.queryGroupMemberList(groupId,function(resultData){
				$(".txl_groupChat_detail").empty();
				resultData=$.parseJSON(resultData);
				if(null==resultData){
					return false;
				}
				var li;
				for(var index in resultData){
					var object = resultData[index];
					 li=$("<li  chatId='"+object.userId+"' toChatId='"+object.userId+"' ><a href='javascript:;'>"
							+"<img src='"+object.userPhoto+"' class='user_tx' chatId='"+object.userId+"' toChatId='"+object.userId+"' isUserImg='true'/>"
							+"<p>"+object.userName+"</p></a></li>")
					$(".txl_groupChat_detail").append(li);
				}
				
			});
		});
});
}

/**
 * 服务端推送到客户端 修改的群昵称
 * @param modifyGroupNameJsonStr
 * @returns
 */
function servPushToClientModifyGroupMsg(modifyGroupNameJsonStr){
	var groupInfo = $.parseJSON(modifyGroupNameJsonStr);
	if(null==groupInfo){
		return false;
	}
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
}