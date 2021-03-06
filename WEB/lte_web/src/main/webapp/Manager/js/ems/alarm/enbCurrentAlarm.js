$(function(){	
		
	$("#firstPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		queryEnbCurrentAlarm(1,3,"",sortColumn,sortDirection);
	}); 
	//尾页
	$("#endPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		var totalPage = $("#totalPage").html();
		queryEnbCurrentAlarm(totalPage,3,"",sortColumn,sortDirection);
	});
	//上一页
	
	$("#previousPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		var currentPage = $("#currentPage").html();
		if(parseInt(currentPage) == 1){
			alert("已是首页");
		}else{
			var page = (parseInt(currentPage)-1);
			queryEnbCurrentAlarm(page,3,"",sortColumn,sortDirection);
		}
	});
	//下一页
	$("#nextPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		var currentPage = $("#currentPage").html();
		var totalPage = $("#totalPage").html();
		if(parseInt(currentPage) == parseInt(totalPage)){
			alert("已是尾页");
		}else{
			var page = (parseInt(currentPage)+1);
			queryEnbCurrentAlarm(page,3,"",sortColumn,sortDirection);
		}
	});
	//目标页
	$("#targetPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		var isNum = /^\d+$/;
		var targetPage = $("#targetPageInput").val();
		var totalPage = $("#totalPage").html();
		if(isNum.test(targetPage)){
			if(targetPage >= 1 && targetPage<= parseInt(totalPage)){
				queryEnbCurrentAlarm(targetPage,3,"",sortColumn,sortDirection);
			}else if(targetPage <= 1){
				queryEnbCurrentAlarm(1,3,"",sortColumn,sortDirection);
			}else{
				queryEnbCurrentAlarm(totalPage,3,"",sortColumn,sortDirection);
			}
		}else{
			queryEnbCurrentAlarm(1,3,"",sortColumn,sortDirection);
		}
	});
	
	//查询
	$("#queryEnbCurrentAlarm").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		queryEnbCurrentAlarm(1,3,"",sortColumn,sortDirection);
	});
	
	//级别
	$("#alarmLevel").change(function(){	
		var level = $("#alarmLevel").is(":checked");
		if(level==true){
			$(".alarmLevel").removeAttr("disabled");
		}else{
			$(".alarmLevel").attr("disabled","disabled");
		}		
	});
	//状态
	$("#alarmState").change(function(){
		var state = $("#alarmState").is(":checked");
		if(state==true){
			$(".alarmState").removeAttr("disabled");
		}else{
			$(".alarmState").attr("disabled","disabled");
		}
	});
	//时间
	$("#timeCheck").change(function(){
		var timeCheck = $("#timeCheck").is(":checked");
		if(timeCheck==true){
			$(".timeCheck").removeAttr("disabled");
		}else{
			$(".timeCheck").attr("disabled","disabled");
		}
	});
	
	//全选
	$("#checkfather").live("click",function(){
		$("[name=checkson]:checkbox").attr("checked",this.checked);
	});
	$("[name=checkson]:checkbox").live("click",function(){
		var flag=true;
		$("[name=checkson]:checkbox").each(function(){
			if(!this.checked){
				flag=false;
			}
		});
		$("#checkfather").attr("checked",flag);
	});
	
	//确认
	$("#enbCurrentAlarmTable tr").each(function(index){
		$("#enbCurrentAlarmTable tr:eq("+index+") td:eq(11)").live("click",function(){
			if($("#enbCurrentAlarmTable tr:eq("+index+") td:eq(9) input").val() == 0){
				if(confirm("对此记录进行确认操作?")){
					var currentPage = $("#currentPage").html();
					var array = $("#enbCurrentAlarmTable tr:eq("+index+") td:eq(0)").text();
					var sortColumn = $("#sortColumn").val();
					var sortDirection = $("#sortDirection").val();
					queryEnbCurrentAlarm(currentPage,0,array,sortColumn,sortDirection);
				}
			}
		});					   
	});	
	//批量确认
	$("#multiConfirm").click(function(){
		var str=[];
		$("#enbCurrentAlarmTable input[type=checkbox]").each(function(index){
			if($("#enbCurrentAlarmTable input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#enbCurrentAlarmTable tr:eq("+index+") td:eq(0)").text();
				str.push(temp);
			}
		});	
		for(var i=0;i<str.length;i++){
			if(str[i]== "" || str[i]== null){
				str.splice(i,1);
			}
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			if(confirm("对所有选择的记录进行确认?")){
				var currentPage = $("#currentPage").html();
				var sortColumn = $("#sortColumn").val();
				var sortDirection = $("#sortDirection").val();
				queryEnbCurrentAlarm(currentPage,0,str,sortColumn,sortDirection);
			}
		}	
	});
	//恢复
	$("#enbCurrentAlarmTable tr").each(function(index){
		$("#enbCurrentAlarmTable tr:eq("+index+") td:eq(12)").live("click",function(){
			if($("#enbCurrentAlarmTable tr:eq("+index+") td:eq(10) input").val() == 1){
				if(confirm("对此记录进行恢复操作?")){
					var currentPage = $("#currentPage").html();
					var array = $("#enbCurrentAlarmTable tr:eq("+index+") td:eq(0)").text();
					var sortColumn = $("#sortColumn").val();
					var sortDirection = $("#sortDirection").val();
					queryEnbCurrentAlarm(currentPage,1,array,sortColumn,sortDirection);
				}
			}
		});					   
	});	
	//批量恢复
	$("#multiRestore").click(function(){
		var str=[];
		$("#enbCurrentAlarmTable input[type=checkbox]").each(function(index){
			if($("#enbCurrentAlarmTable input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#enbCurrentAlarmTable tr:eq("+index+") td:eq(0)").text();
				str.push(temp);
			}
		});	
		for(var i=0;i<str.length;i++){
			if(str[i]== "" || str[i]== null){
				str.splice(i,1);
			}
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			if(confirm("对所有选择的记录进行恢复?")){
				var currentPage = $("#currentPage").html();
				var sortColumn = $("#sortColumn").val();
				var sortDirection = $("#sortDirection").val();
				queryEnbCurrentAlarm(currentPage,1,str,sortColumn,sortDirection);
			}
		}	
	});
	//告警同步
	$("#sync").click(function(){
		if(confirm("确定进行告警同步?")){
			var basePath = $("#basePath").val();
			var enbIdArray = $("#enbIdArray").val();
			$.ajax({
				type:"post",
				url:"syncCurrentAlarm.do",
				data:"enbIdArray="+enbIdArray+
					"&browseTime="+getBrowseTime(),
				dataType:"json",
				async:false,
				success:function(data){
					if(!sessionsCheck(data,basePath)){
						return ;
					}
					if(data.error == null || data.error ==""){
						var sortColumn = $("#sortColumn").val();
						var sortDirection = $("#sortDirection").val();	
						queryEnbCurrentAlarm(1,3,"",sortColumn,sortDirection);
						alert("告警同步指令已下发");
					}else{
						alert(data.error);
					}
					
				}
			});	
		}		
	});
	
	//排序
	$("#sortBy1").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "serialNumber";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy2").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmLevel";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy3").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmContent";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy4").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "NeName";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy5").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alignedInfo";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy6").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmState";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy7").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "firstAlarmTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy8").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "restoredTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy9").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "restoreUser";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy10").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "confirmTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy11").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "confirmUser";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbCurrentAlarm(page,3,array,sortColumn,sortDirection);		
	});
});
function queryEnbCurrentAlarm(page,type,array,sortColumn,sortDirection){	
	//重置报错
	document.getElementById("submit_check_error").innerHTML = "";
	
	var isEnbId=/^[a-fA-F0-9]{1,}$/;
	
	var timeCheck = $("#timeCheck").is(":checked");
	var beginTime = "";
	var endTime = "";
	if(timeCheck == true){
		//起始时间
		beginTime = $("input[name='beginTime']").val();
		//结束时间
		endTime = $("input[name='endTime']").val();
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
			document.getElementById("submit_check_error").innerHTML = "结束时间需要大于开始时间";
			return;
		}
	}

	//enbID
	var enbHexId = $("#enbIdArray").val()+"";
	if(enbHexId != ""){
		var length = enbHexId.length;	
		if(length!=8 || !(isEnbId.test(enbHexId)) || parseInt(enbHexId,16) > 1048575){
			document.getElementById("submit_check_error").innerHTML = "请输入正确的eNB ID,00000000~000FFFFF";
			 $("#enbIdArray").focus();
			 return;
		}
	}
	//级别
	var level = $("#alarmLevel").is(":checked");
	var levelArray = new Array();
	if(level==true){
		var level1 = $("#alarmLevel1").is(":checked");
		var level2 = $("#alarmLevel2").is(":checked");
		var level3 = $("#alarmLevel3").is(":checked");
		var level4 = $("#alarmLevel4").is(":checked");
		if(level1==true){
			levelArray.push(1);
		}
		if(level2==true){
			levelArray.push(2);
		}
		if(level3==true){
			levelArray.push(3);
		}
		if(level4==true){
			levelArray.push(4);
		}
	}
	//状太
	var state = $("#alarmState").is(":checked");
	var stateArray = new Array();
	if(state==true){
		var state1 = $("#alarmState1").is(":checked");
		var state2 = $("#alarmState2").is(":checked");
		var state3 = $("#alarmState3").is(":checked");
		var state4 = $("#alarmState4").is(":checked");
		if(state1==true){
			stateArray.push(1);
		}
		if(state2==true){
			stateArray.push(2);
		}
		if(state3==true){
			stateArray.push(3);
		}
		if(state4==true){
			stateArray.push(4);
		}
	}
	//复选框状态
	var checkStateArray = new Array();
	$("input[name='checkson']:checked").each(function(){
		checkStateArray.push($(this).val());
	});
	//告警内容
	var alarmContent = $("#alarmContent").val();
	var basePath = $("#basePath").val();
	$.ajax({
		type:"post",
		url:"queryEnbCurrentAlarm.do",
		data:"levelArray="+levelArray+
			"&stateArray="+stateArray+
			"&beginTime="+beginTime+
			"&endTime="+endTime+
			"&currentPage="+page+
			"&alarmOperType="+type+
			"&alarmIdArray="+array+
			"&enbIdArray="+enbHexId+
			"&alarmContent="+alarmContent+
			"&sortColumn="+sortColumn+
			"&sortDirection="+sortDirection+
			"&checkStateArray="+checkStateArray+
			"&browseTime="+getBrowseTime(),
		dataType:"json",
		async:false,
		success:function(data){
			if(!sessionsCheck(data,basePath)){
				return ;
			}
			var myTr = "";
			if(data.alarmListModel.error != "" && data.alarmListModel.error != null){
				myTr = '<tr>'
					+'<th scope="row"><input type="checkbox" disabled="disabled" value="checkbox" name="checkson"/></th>'
					+'<td></td>'
					+'<td>暂无相关记录</td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
//					+'<td></td>'
//					+'<td></td>'
//					+'<td></td>'
					+'</tr>'
					;
				$("#currentPage").html('1');
				$("#totalPage").html('1');
				$("#targetPageInput").val(1);
				$("#checkfather").attr("disabled",true);
				$("#alarmListBody").html(myTr);
				$("#sysError").html(data.alarmListModel.error);	
			}else{
				if(data.alarmListModel.enbNullError == "" || data.alarmListModel.enbNullError == null){
					if(data.alarmListModel.alarmList.length>0){
						for(var i=0;i<data.alarmListModel.alarmList.length;i++){
							var td1 = "";
							if(data.alarmListModel.alarmList[i].alarm.alarmLevel==1){
								td1 = '<td><img src="'+basePath+'/Manager/images/tnc/alarm1.png"><span>&nbsp;</span>紧急</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.alarmLevel==2){
								td1 = '<td><img src="'+basePath+'/Manager/images/tnc/alarm2.png"><span>&nbsp;</span>重要</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.alarmLevel==3){
								td1 = '<td><img src="'+basePath+'/Manager/images/tnc/alarm4.png"><span>&nbsp;</span>次要</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.alarmLevel==4){
								td1 = '<td><img src="'+basePath+'/Manager/images/tnc/alarm3.png"><span>&nbsp;</span>提示</td>';
							}
							var td2 = "";
							if(data.alarmListModel.alarmList[i].alarm.confirmState ==0 && data.alarmListModel.alarmList[i].alarm.alarmState ==1){
								td2 = '<td>未确认未恢复</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.confirmState ==1 && data.alarmListModel.alarmList[i].alarm.alarmState ==1){
								td2 = '<td>已确认未恢复</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.confirmState ==0 && data.alarmListModel.alarmList[i].alarm.alarmState ==0){
								td2 = '<td>未确认已恢复</td>';
							}
							if(data.alarmListModel.alarmList[i].alarm.confirmState ==1 && data.alarmListModel.alarmList[i].alarm.alarmState ==0){
								td2 = '<td>已确认已恢复</td>';
							}
							var td3="";
							if(data.alarmListModel.alarmList[i].alarm.confirmState ==0){
								td3 = '<td style="cursor:pointer;"><a>确认</a></td>';
							}else{
								td3 = '<td>已确认</td>';
							}
							var td4 = "";
							if(data.alarmListModel.alarmList[i].alarm.alarmState ==0){
								td4 = '<td>已恢复</td>';
							}else{
								td4 = '<td style="cursor:pointer;"><a>恢复</a></td>';
							}
							var alarmContent = data.alarmListModel.alarmList[i].alarm.alarmContent;
							var location ;
							if(queryEnbTypeOfMoId(data.alarmListModel.alarmList[i].alarm.moId) == 0){
								location = data.alarmListModel.alarmList[i].alarm.location;
							}else if(queryEnbTypeOfMoId(data.alarmListModel.alarmList[i].alarm.moId) == 200){
								location = data.alarmListModel.alarmList[i].alarm.tinyLocation;
							}
							var restoreUserTd = "";
							var restoreUser = data.alarmListModel.alarmList[i].alarm.restoreUser;
							restoreUserTd = restoreUser;
							if(restoreUserTd.length >10){
								restoreUserTd = restoreUserTd.substr(0, 7)+"...";
							}
							var confirmUserTd = "";
							var confirmUser = data.alarmListModel.alarmList[i].alarm.confirmUser;
							confirmUserTd = confirmUser;
							if(confirmUserTd.length > 10){
								confirmUserTd = confirmUserTd.substr(0, 7)+"...";
							}
							myTr+='<tr>'
								+'<th scope="row"><input type="checkbox" value="'+data.alarmListModel.alarmList[i].alarm.id+'" name="checkson"/></th>'
								+'<td style="display:none;">'+data.alarmListModel.alarmList[i].alarm.id+'</td>'
								+''+td1+''
								+'<td style="text-align:left;padding:0 0 0 5px;">'+alarmContent+'</td>'
								+'<td>'+data.alarmListModel.alarmList[i].enbName+'</td>'
								+'<td style="text-align:left;padding:0 0 0 5px;">'+location+'</td>'
								+''+td2+''
								+'<td>'+data.alarmListModel.alarmList[i].firstAlarmTimeString+'</td>'
								+'<td>'+data.alarmListModel.alarmList[i].restoredTimeString+'</td>'
								+'<td title="'+restoreUser+'">'+restoreUserTd+'</td>'
								+'<td>'+data.alarmListModel.alarmList[i].confirmTimeString+'<input type="hidden" value="'+data.alarmListModel.alarmList[i].alarm.confirmState+'"></td>'
								+'<td title="'+confirmUser+'">'+confirmUserTd+'<input type="hidden" value="'+data.alarmListModel.alarmList[i].alarm.alarmState+'"></td>'
//								+''+td3+''
//								+''+td4+''
								+'</tr>';
							
						}
						$("#currentPage").html(
								''+data.alarmListModel.currentPage+''
						);
						$("#totalPage").html(
								''+data.alarmListModel.totalPage+''
						);
						$("#targetPageInput").val(data.alarmListModel.currentPage);
						$("#alarmListBody").html(myTr);
						$("#checkfather").attr("disabled",false);
						$("#checkfather").attr("checked",false);
						
//						$(".changeRowColor tr").each(function(index){
//							if(index > 0 && index%2 == 0){
//								$(".changeRowColor tr:eq("+index+") td").css("background-color","#F6F6F6");
//							}
//						}); 
						$("#sysError").html("");
						var checkStateArray  = data.checkStateArray;
						var checkStateStr = checkStateArray.split(",");						
						$("input[name='checkson']").each(function(){
							if($.inArray($(this).val(),checkStateStr)>-1){
								$(this).attr("checked","checked");
							}
						});
						var flag=true;
						$("[name=checkson]:checkbox").each(function(){
							if(!this.checked){
								flag=false;
							}
						});
						$("#checkfather").attr("checked",flag);
						
					}else{
						myTr = '<tr>'
							+'<th scope="row"><input type="checkbox" disabled="disabled" value="checkbox" name="checkson"/></th>'
							+'<td></td>'
							+'<td>暂无相关记录</td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
							+'<td></td>'
//							+'<td></td>'
//							+'<td></td>'
//							+'<td></td>'
							+'</tr>'
							;
						$("#currentPage").html('1');
						$("#totalPage").html('1');
						$("#targetPageInput").val(1);
						$("#checkfather").attr("disabled",true);
						$("#alarmListBody").html(myTr);
						$("#sysError").html("");
					}
				}else{
					$("#sysError").html(data.alarmListModel.enbNullError);
					myTr = '<tr>'
						+'<th scope="row"><input type="checkbox" disabled="disabled" value="checkbox" name="checkson"/></th>'
						+'<td></td>'
						+'<td>暂无相关记录</td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
//						+'<td></td>'
//						+'<td></td>'
//						+'<td></td>'
						+'</tr>'
						;
					$("#currentPage").html('1');
					$("#totalPage").html('1');
					$("#targetPageInput").val(1);
					$("#checkfather").attr("disabled",true);
					$("#alarmListBody").html(myTr);
				}
			}					
		}
	});	
}
function queryEnbTypeOfMoId(moId){
	var result = 0;
	$.ajax({
		type : "post",
		url : "queryEnbTypeOfMoId.do",
		data : "moId=" + moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				result = data.message;
			}
		}
	});
	return result;
}