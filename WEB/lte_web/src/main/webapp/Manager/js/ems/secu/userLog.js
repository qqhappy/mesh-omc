$(function(){
	var isEnbId=/^[a-fA-F0-9]{1,}$/;
	$("#operObjectType").change(function(){
		if($(this).val() == "none"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Config">配置</option>'
					+'<option value="Add">添加</option>'
					+'<option value="Delete">删除</option>'
					+'<option value="Modify">修改</option>'
					+'<option value="Synchronize">同步</option>'
					+'<option value="Download">下载</option>'
					+'<option value="Upgrade">升级</option>'
					+'<option value="Confirm">确认</option>'
					+'<option value="Restore">恢复</option>'
					+'<option value="Login">登录</option>'
					+'<option value="Logout">退出</option>'
			);
			queryUserLog(1);
		}
		if($(this).val() == "User"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Add">添加</option>'
					+'<option value="Delete">删除</option>'
					+'<option value="Modify">修改</option>'
					+'<option value="Login">登录</option>'
					+'<option value="Logout">退出</option>'
			);
			queryUserLog(1);
		}
		if($(this).val() == "Alarm"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Synchronize">同步</option>'
					+'<option value="Confirm">确认</option>'
					+'<option value="Restore">恢复</option>'
			);
			queryUserLog(1);
		}
		if($(this).val() == "eNB"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Config">配置</option>'
					+'<option value="Add">添加</option>'
					+'<option value="Delete">删除</option>'
					+'<option value="Modify">修改</option>'
					+'<option value="Synchronize">同步</option>'
					+'<option value="Download">下载</option>'
					+'<option value="Upgrade">升级</option>'
			);
			queryUserLog(1);
		}
		if($(this).val() == "TCN1000"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Config">配置</option>'
					+'<option value="Add">添加</option>'
					+'<option value="Delete">删除</option>'
					+'<option value="Modify">修改</option>'
			);
			queryUserLog(1);
		}
		if($(this).val() == "UT_User"){
			$("#operType").html(
					'<option value="none">全部</option>'
					+'<option value="Add">添加</option>'
					+'<option value="Delete">删除</option>'
					+'<option value="Modify">修改</option>'
			);
			queryUserLog(1);
		}
		
	});
	$("#workType").change(function(){
		queryUserLog(1);
	});
	$("#operUser").change(function(){
		queryUserLog(1);
	});
	$("#operType").change(function(){
		queryUserLog(1);
	});
	
	//首页
	
	$("#firstPage").click(function(){
		queryUserLog(1);;
	});
	//尾页
	$("#endPage").click(function(){
		var totalPage = $("#totalPage").html();
		queryUserLog(totalPage);
	});
	//上一页
	
	$("#previousPage").click(function(){
		var currentPage = $("#currentPage").html();
		if(parseInt(currentPage) == 1){
			alert("已是首页");
		}else{
			var page = (parseInt(currentPage)-1);
			queryUserLog(page);
		}
	});
	//下一页
	
	$("#nextPage").click(function(){
		var currentPage = $("#currentPage").html();
		var totalPage = $("#totalPage").html();
		if(parseInt(currentPage) == parseInt(totalPage)){
			alert("已是尾页");
		}else{
			var page = (parseInt(currentPage)+1);
			queryUserLog(page);
		}
	});
	//目标页	
	$("#targetPage").click(function(){
		var isNum = /^\d+$/;
		var targetPage = $("#targetPageInput").val();
		var totalPage = $("#totalPage").html();
		if(isNum.test(targetPage)){
			if(targetPage >= 1 && targetPage<= parseInt(totalPage)){
				queryUserLog(targetPage);
			}else if(targetPage <= 1){
				queryUserLog(1);
			}else{
				queryUserLog(totalPage);
			}
		}else{
			queryUserLog(1);
		}
	});
	
	//查询
	$("#queryUserLog").click(function(){
		var operObjectId = $("#operObjectId").val()+"";
		if(operObjectId == ""){
			queryUserLog(1);
		}else{
			var length = operObjectId.length;	
			if(length!=8 || !(isEnbId.test(operObjectId)) || parseInt(operObjectId,16) > 1048575){
				alert("请输入正确的eNB ID,00000000~000FFFFF");
				 $("#operObjectId").focus();
			}else{
				queryUserLog(1);
			}	
		}		
	});
});
function queryUserLog(page){	
	//隐藏详细信息
	$("#logDetail_div").css("display","none");
	var isEnbId=/^[a-fA-F0-9]{1,}$/;
	//起始时间
	var beginTime = $("input[name='beginTime']").val();
	//结束时间
	var endTime = $("input[name='endTime']").val();
	var str1 = beginTime.split("");
	var str2 = endTime.split("");
	var str3 = "";
	var str4 = "";
	var isNum = /^[0-9]+$/;
	for(var i = 0;i<str1.length;i++){
		if(isNum.test(str1[i])){
			str3+=(str1[i]);
		}
	}
	for(var j = 0;j<str2.length;j++){
		if(isNum.test(str2[j])){
			str4+=(str2[j]);
		}
	}
	if(parseInt($.trim(str4)) - parseInt($.trim(str3)) < 0){
		alert("开始时间不可以大于结束时间");
		return;
	}
	//业务类型
	var workType = $("#workType").val();
	//操作用户
	var operUser = $("#operUser").val();
	//操作对象类型
	var operObjectType = $("#operObjectType").val();
	//操作类型
	var operType = $("#operType").val();
	//操作对象ID
	var operObjectId = $("#operObjectId").val()+"";
	if(operObjectId != ""){
		var length = operObjectId.length;	
		if(length!=8 || !(isEnbId.test(operObjectId)) || parseInt(operObjectId,16) > 1048575){
			alert("请输入正确的eNB ID,00000000~000FFFFF");
			 $("#operObjectId").focus();
			 return;
		}
	}
	var basePath = $("#basePath").val();
	$.ajax({
		type:"post",
		url:"queryUserLog.do",
		data:"beginTime="+beginTime+
			"&endTime="+endTime+
			"&currentPage="+page+
			"&workType="+workType+
			"&operUser="+operUser+
			"&operObjectType="+operObjectType+
			"&operObjectId="+operObjectId+
			"&operType="+operType+
			"&browseTime="+getBrowseTime(),
		dataType:"json",
		async:false,
		success:function(data){
			if(!sessionsCheck(data,basePath)){
				return ;
			}
			var myTr = "";
			if(data.result.logModelList.length>0){
				for(var i=0;i<data.result.logModelList.length;i++){
					var td1 = '<td></td>';
					if(data.result.logModelList[i].log.operObject.objectType == "eNB"){
						td1 = '<td>eNB</td>';
					}
					if(data.result.logModelList[i].log.operObject.objectType == "System"){
						td1 = '<td>系统</td>';
					}
					if(data.result.logModelList[i].log.operObject.objectType == "Alarm"){
						td1 = '<td>告警</td>';
					}
					if(data.result.logModelList[i].log.operObject.objectType == "User"){
						td1 = '<td>用户</td>';
					}
					if(data.result.logModelList[i].log.operObject.objectType == "TCN1000"){
						td1 = '<td>TCN1000设备</td>';
					}
					if(data.result.logModelList[i].log.operObject.objectType == "UT_User"){
						td1 = '<td>终端用户</td>';
					}
					var td2 = '<td></td>';
					if(data.result.logModelList[i].log.operObject.objectId !== ""){
						td2 = '<td>'+data.result.logModelList[i].log.operObject.objectId+'</td>';
					}
					var td3 = '<td></td>';
					if(data.result.logModelList[i].log.operObject.operType == "Security"){
						td3 = '<td>安全管理</td>';
					}
					if(data.result.logModelList[i].log.operObject.operType == "Config"){
						td3 = '<td>配置管理</td>';
					}
					if(data.result.logModelList[i].log.operObject.operType == "Alarm"){
						td3 = '<td>故障管理</td>';
					}
					if(data.result.logModelList[i].log.operObject.operType == "UT_User"){
						td3 = '<td>终端用户管理</td>';
					}
					var td4 = '<td></td>';
					if(data.result.logModelList[i].log.actionDesc == "Query"){
						td4 = '<td>查看</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Config"){
						td4 = '<td>配置</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Add"){
						td4 = '<td>增加</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "BatchAdd"){
						td4 = '<td>批量增加</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Delete"){
						td4 = '<td>删除</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Modify"){
						td4 = '<td>修改</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Synchronize"){
						td4 = '<td>同步</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Upload"){
						td4 = '<td>上传</td>';
					}					
					if(data.result.logModelList[i].log.actionDesc == "Download"){
						td4 = '<td>下载</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Upgrade"){
						td4 = '<td>升级</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Confirm"){
						td4 = '<td>确认</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Backup"){
						td4 = '<td>备份</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Restore"){
						td4 = '<td>恢复</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Login"){
						td4 = '<td>登录</td>';
					}
					if(data.result.logModelList[i].log.actionDesc == "Logout"){
						td4 = '<td>退出</td>';
					}
					var dataDesc = data.result.logModelList[i].dataDesc;
					myTr+='<tr>'
						+'<td>'+data.result.logModelList[i].log.username+'</td>'
						+ td1
						+ td2
						+'<td>'+data.result.logModelList[i].log.operDesc+'</td>'
						+ td4
						+ td3
						+'<td>'+data.result.logModelList[i].operTimeString+'</td>'
						+'<td><a onclick="f1_log(this)" dataDesc="'+dataDesc+'" style="outline:none;cursor:pointer;">详细信息</a></td>' 
						+'</tr>';
					
				}
				$("#currentPage").html(
						''+data.result.currentPage+''
				);
				$("#totalPage").html(
						''+data.result.totalPage+''
				);
				$("#targetPageInput").val(data.result.currentPage);
				$("#userLogBody").html(myTr);
				
				
			}else{
				myTr = '<tr>'
					+'<td>暂无相关记录</td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>' 
					+'</tr>'
					;
				$("#currentPage").html('1');
				$("#totalPage").html('1');
				$("#targetPageInput").val(1);
				$("#userLogBody").html(myTr);
			}
		}
	});	
}
function showLogDetail(content){
	$("#logDetail_div").html("");
	$("#logDetail_div").css("display","block");
	$("#logDetail_div").html(content);
}
function f1_log(obj){
	$("#logDetail_div").html("");
	$("#logDetail_div").css("display","block");
	$("#logDetail_div").html($(obj).attr("dataDesc"));
}