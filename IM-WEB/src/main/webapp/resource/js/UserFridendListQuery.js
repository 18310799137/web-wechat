$(function(){
	queryUserFriendsList();
});
function queryUserFriendsList(){
	DwrUserFriendServiceImpl.queryUserFriendList(function(resultJSON){
		$("#txlCirclesBox").empty();
		var userFriends = JSON.parse(resultJSON);
		if(null==userFriends||userFriends==""){
			return false;
		}
		
		var names = [];
		for(var i in userFriends){
			var sortName = userFriends[i].friendName+","+i;
			var firstLetter = makePy(sortName)[0].charAt(0);
			names.push(firstLetter+","+sortName);
		}
		names.sort();
		names.sort(function(names,b){return names.localeCompare(b)});
		for ( var j in userFriends) {
			var li = "";
			var index = names[j].split(','); 
			var nickName = index[1];
			var firstLetter = index[0];
			var reg= /^[A-Za-z]+$/;
			//判断好友备注首字符是否为字母
			if (reg.test(firstLetter)){
				if(document.getElementById("userFriendListTag"+firstLetter)==null){
					li = li+"<li class='list_headlines' id='userFriendListTag"+firstLetter+"'>"+
								firstLetter.toUpperCase()+
	                		"</li>";
				}
			}else{
				li = li+"<li class='list_headlines' id='userFriendListTag#'>"+
							"#"+
			    		"</li>";
			}
			li = li+"<li class='userLi clearfix' friendInfoId='"+userFriends[index[2]].friendId+"' chatId='"+userFriends[index[2]].friendId+"' >"+
						"<input value='"+userFriends[index[2]].friendId+"' type='hidden' />"+
						"<img src='"+userFriends[index[2]].userPhoto+"' />"+
						"<p class='userNickName'>"+nickName+"</p>"+
					"</li>";
			$("#txlCirclesBox").append(li);
		}
		
		//为li添加点击事件，功能：点击出现好友详情
		$("#txlCirclesBox").find("li").click(function(){
			 $("#centerChatBox").hide();
			var chatId = $(this).attr("chatId");
			if(chatId==undefined||chatId==""){
				return false;
			}
			$("#centerAddressBookBox").show();
			//根据好友ID查询好友详情
			DwrUserQueryServiceImpl.queryUserDetail(chatId,"SU","",function(resultData){
				
				addUserInfoDetail(resultData);
			});
			
		});
		
	})
}

//点击创建群显示已添加好友列表
function addFriendListForCreateGroup(memberIds ){
	var flag = $(".create_group_sure_btn").attr("oprFlag");
	var groupId =$(".create_group_sure_btn").attr("groupId");
	if(flag=="add"){
		
		$(".create_group_left_list_box .left_users_list").empty();
		DwrUserFriendServiceImpl.queryUserFriendList(function(resultJSON){
			var userFriends = JSON.parse(resultJSON);
			if(null==userFriends){
				return false;
			}
			var names = [];
			for(var i in userFriends){
				var sortName = userFriends[i].friendName+","+i;
				var firstLetter = makePy(sortName)[0].charAt(0);
				names.push(firstLetter+","+sortName);
			}
			names.sort();
			names.sort(function(names,b){return names.localeCompare(b)});
			for ( var j in userFriends) {
				var index = names[j].split(','); 
				var chatId = userFriends[index[2]].friendId;
				var containFlag = false;
				
				for(var k in memberIds){
					if(chatId==memberIds[k]){
						containFlag=true;
						break;
					}
				}
				if(containFlag==false){
					var li = "";
					var nickName = index[1];
					var userPhoto = userFriends[index[2]].userPhoto;
					var firstLetter = index[0];
					var reg= /^[A-Za-z]+$/;
					
					var tags = $(".create_group_left_list_box");
					var lastTag = tags.find(".list_headlines").last().text();
					//判断好友备注首字符是否为字母
					if (reg.test(firstLetter)){
						if(firstLetter.toLowerCase()!=lastTag&&firstLetter.toUpperCase()!=lastTag){
							li = li+"<li class='list_headlines'>"+firstLetter.toUpperCase()+"</li>";
						}
					}else{
						li = li+"<li class='list_headlines'>#</li>";
					}
					li = li+"<li class='userLi clearfix' chatId='"+chatId+"'>"+
					"<img src='"+userPhoto+"' />"+
					"<label class='checkbox' data-value=''></label>"+
					"<p class='userNickName'>"+nickName+"</p></li>";
					$(".create_group_left_list_box .left_users_list").append(li);
				}
				
			}
			
			//创建群弹窗里面的checkbox点击事件
			$(".create_group_left_list_box").find("label").on("click",function(){
				var oLiHtml = '<li class="userLi clearfix" id="li'+$(this).parent().index()+'" chatId="'+$(this).parent().attr("chatId")+'"><img src="'+$(this).siblings("img").attr("src")+'"/><i class="create_group_selected_cancel"></i><p class="userNickName">'+$(this).siblings(".userNickName").html()+'</p></li>';
				if($(this).hasClass("checked")){
					if($("#createGroupSelectedList").children().length==1){
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("请勾选需要添加的联系人");//右侧没有选中好友的提示文字
						$(".create_group_sure_btn").addClass("disabled");//右侧确定按钮添加不可点击的展示效果
						$(".create_group_sure_btn").attr("disabled",true);//右侧确定按钮添加不可点击状态
					}else{
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					}
				}else{
					$(this).attr("data-value","checked");//左侧模拟的checkbox选中状态下data-value="checkbox"
					$(this).addClass("checked");//左侧模拟的checkbox的类名添加，展示效果变成选中状态
					$("#createGroupSelectedList").append(oLiHtml);//右侧选中好友列表处展示选中的好友内容
					$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					$(".create_group_sure_btn").removeClass("disabled");//右侧确定按钮移除不可点击的展示效果
					$(".create_group_sure_btn").attr("disabled",false);//右侧确定按钮移除不可点击状态
				}
			});
		});
		
		//添加群成员
	}else if(flag=="rem"){
		//删除群成员
		$(".create_group_left_list_box .left_users_list").empty();
			var names = [];
			for(var i in memberIds){
				var sortName = memberIds[i].userName+","+i;
				var firstLetter = makePy(sortName)[0].charAt(0);
				names.push(firstLetter+","+sortName);
			}
			names.sort();
			names.sort(function(names,b){return names.localeCompare(b)});
			for ( var j in memberIds) {
				var index = names[j].split(','); 
				var chatId = memberIds[index[2]].userId;
				if(chatId==oneSelf.id){
					continue;
				}
					var li = "";
					var nickName = index[1];
					var userPhoto = memberIds[index[2]].userPhoto;
					var firstLetter = index[0];
					var reg= /^[A-Za-z]+$/;
					
					var tags = $(".create_group_left_list_box");
					var lastTag = tags.find(".list_headlines").last().text();
					//判断好友备注首字符是否为字母
					if (reg.test(firstLetter)){
						if(firstLetter.toLowerCase()!=lastTag&&firstLetter.toUpperCase()!=lastTag){
							li = li+"<li class='list_headlines'>"+firstLetter.toUpperCase()+"</li>";
						}
					}else{
						li = li+"<li class='list_headlines'>#</li>";
					}
					li = li+"<li class='userLi clearfix' chatId='"+chatId+"'>"+
					"<img src='"+userPhoto+"' />"+
					"<label class='checkbox' data-value=''></label>"+
					"<p class='userNickName'>"+nickName+"</p></li>";
					$(".create_group_left_list_box .left_users_list").append(li);
				
			}
			
			//创建群弹窗里面的checkbox点击事件
			$(".create_group_left_list_box").find("label").on("click",function(){
				var oLiHtml = '<li class="userLi clearfix" id="li'+$(this).parent().index()+'" chatId="'+$(this).parent().attr("chatId")+'"><img src="'+$(this).siblings("img").attr("src")+'"/><i class="create_group_selected_cancel"></i><p class="userNickName">'+$(this).siblings(".userNickName").html()+'</p></li>';
				if($(this).hasClass("checked")){
					if($("#createGroupSelectedList").children().length==1){
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("请勾选需要添加的联系人");//右侧没有选中好友的提示文字
						$(".create_group_sure_btn").addClass("disabled");//右侧确定按钮添加不可点击的展示效果
						$(".create_group_sure_btn").attr("disabled",true);//右侧确定按钮添加不可点击状态
					}else{
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					}
				}else{
					$(this).attr("data-value","checked");//左侧模拟的checkbox选中状态下data-value="checkbox"
					$(this).addClass("checked");//左侧模拟的checkbox的类名添加，展示效果变成选中状态
					$("#createGroupSelectedList").append(oLiHtml);//右侧选中好友列表处展示选中的好友内容
					$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					$(".create_group_sure_btn").removeClass("disabled");//右侧确定按钮移除不可点击的展示效果
					$(".create_group_sure_btn").attr("disabled",false);//右侧确定按钮移除不可点击状态
				}
			});
	}else{
		//否则为创建群操作
		
		
		
		
		
		$(".create_group_left_list_box .left_users_list").empty();
		DwrUserFriendServiceImpl.queryUserFriendList(function(resultJSON){
			var userFriends = JSON.parse(resultJSON);
			if(null==userFriends){
				return false;
			}
			var names = [];
			for(var i in userFriends){
				var sortName = userFriends[i].friendName+","+i;
				var firstLetter = makePy(sortName)[0].charAt(0);
				names.push(firstLetter+","+sortName);
			}
			names.sort();
			names.sort(function(names,b){return names.localeCompare(b)});
			for ( var j in userFriends) {
				var li = "";
				var index = names[j].split(','); 
				var nickName = index[1];
				var userPhoto = userFriends[index[2]].userPhoto;
				var chatId = userFriends[index[2]].friendId;
				var firstLetter = index[0];
				var reg= /^[A-Za-z]+$/;
				
				var tags = $(".create_group_left_list_box");
				var lastTag = tags.find(".list_headlines").last().text();
				//判断好友备注首字符是否为字母
				if (reg.test(firstLetter)){
					if(firstLetter.toLowerCase()!=lastTag&&firstLetter.toUpperCase()!=lastTag){
						li = li+"<li class='list_headlines'>"+firstLetter.toUpperCase()+"</li>";
					}
				}else{
					li = li+"<li class='list_headlines'>#</li>";
				}
				li = li+"<li class='userLi clearfix' chatId='"+chatId+"'>"+
							"<img src='"+userPhoto+"' />"+
							"<label class='checkbox' data-value=''></label>"+
							"<p class='userNickName'>"+nickName+"</p></li>";
				$(".create_group_left_list_box .left_users_list").append(li);
			}
			
			//创建群弹窗里面的checkbox点击事件
			$(".create_group_left_list_box").find("label").on("click",function(){
				var oLiHtml = '<li class="userLi clearfix" id="li'+$(this).parent().index()+'" chatId="'+$(this).parent().attr("chatId")+'"><img src="'+$(this).siblings("img").attr("src")+'"/><i class="create_group_selected_cancel"></i><p class="userNickName">'+$(this).siblings(".userNickName").html()+'</p></li>';
				if($(this).hasClass("checked")){
					if($("#createGroupSelectedList").children().length==1){
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("请勾选需要添加的联系人");//右侧没有选中好友的提示文字
					
					}else{
						$(this).attr("data-value","");//左侧模拟的checkbox非选中状态下data-value=""
						$(this).removeClass("checked");//左侧模拟的checkbox的类名移除，展示效果变成非选中状态
						$("#li"+$(this).parent().index()).remove();//右侧选中好友列表处移除左侧变为非选中状态的好友内容
						$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					}
				}else{
					$(this).attr("data-value","checked");//左侧模拟的checkbox选中状态下data-value="checkbox"
					$(this).addClass("checked");//左侧模拟的checkbox的类名添加，展示效果变成选中状态
					$("#createGroupSelectedList").append(oLiHtml);//右侧选中好友列表处展示选中的好友内容
					$("#createGroupTip").html("已选择了"+$("#createGroupSelectedList").children().length+"个联系人");//右侧选中好友提示文字的地方显示已选择几个联系人
					
				}
				
				if($("#createGroupSelectedList").children().length<2){
					$(".create_group_sure_btn").addClass("disabled");//右侧确定按钮添加不可点击的展示效果
					$(".create_group_sure_btn").attr("disabled",true);//右侧确定按钮添加不可点击状态
				}else{
					$(".create_group_sure_btn").removeClass("disabled");//右侧确定按钮移除不可点击的展示效果
					$(".create_group_sure_btn").attr("disabled",false);//右侧确定按钮移除不可点击状态
				}
			});
		});
	}
	

}

function addUserInfoDetail(resultData){
	var info = $.parseJSON(resultData);
	if(null==info){
		return false;
	}
	var userName = info.friendName;
	var remarks = info.remarks;
	var region = info.region;
	var friendId = info.friendId;
	var source = info.source;
	var photo = info.friendPhoto;
	
	//将得到的信息回填
	$("#contactWindowTitle").text(userName);
	$("div[class='platform_or_buddy_detail_con cen']").empty();
	var detailHtml = "<div class='platform_or_buddy_cen'>"+
						"<div class='platform_or_buddy_cen_top clearfix'>"+
							"<img src='"+photo+"'"+
								"class='platform_or_buddy_img fr' />"+
							"<div class='platform_or_buddy_left fl'>"+
								"<div class='platform_or_buddy_left_top clearfix'>"+
									"<h3 class='users_nickname fl'>"+userName+"</h3>"+
									"<i class='icon_sex_girl fl'></i>"+
								"</div>"+
								"<p class='personalized_signature'>个性签名</p>"+
							"</div>"+
						"</div>"+
						"<ul class='platform_or_buddy_cen_btm'>"+
							"<li class='clearfix'><label>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注&nbsp;&nbsp;&nbsp;</label>"+
								"<div class='ipt_wrapper'>"+
									"<input type='text' value='"+remarks+"' id='cardRemark2'"+
										"onfocus='showBtmLine(this);' onblur='hideBtmLine(this);' />"+
								"</div></li>"+
							"<li class='clearfix'><label>单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;&nbsp;&nbsp;</label>"+
								"<div class='ipt_wrapper'>"+
									"<input type='text' value='"+region+"' readonly />"+
								"</div></li>"+
							"<li class='clearfix'><label>企业用户号</label>"+
								"<div class='ipt_wrapper'>"+
									"<input type='text' value='"+friendId+"' readonly id='cardNumber2' />"+
								"</div></li>"+
							"<li class='clearfix'><label>来&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;源&nbsp;&nbsp;&nbsp;</label>"+
								"<div class='ipt_wrapper'>"+
									"<input type='text' value='"+source+"' readonly />"+
								"</div></li>"+
						"</ul>"+
					"</div>";
	$("div[class='platform_or_buddy_detail_con cen']").append(detailHtml);
	$("#toChatButton").attr("chatId",friendId);
	$("#toChatButton").attr("sessionFlag","SU");
	$("#toChatButton").attr("toChatName",userName);
	$("#toChatButton").attr("isFriend","true");

	$("#cardRemark2").blur(function(){
		var remarks = $(this).val();
		var friendId = $("#cardNumber2").val();
		DwrUserFriendServiceImpl.modifyFriendRemarks(friendId,remarks);
	});
}

function makePy(str)  
{    
        var arrResult = new Array();   
        //将字符串首位转码后转为数组  
        var ch = str.charAt(0);    
        arrResult.push(checkCh(ch)); 
        return arrResult;    
}
//检查备注首字符是否为汉字
function checkCh(ch)  
{    
    var uni = ch.charCodeAt(0);    
    //如果不在汉字处理范围之内,返回原字符
    if(uni > 40869 || uni < 19968)    
    return ch;    
    //检查是否是多音字,是按多音字处理,不是就直接在strChineseFirstPY字符串中找对应的首字母    
    return (oMultiDiff[uni]?oMultiDiff[uni]:(strChineseFirstPY.charAt(uni-19968)));    
}    
