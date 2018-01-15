$(function(){
	
	
	$("#firstPage").click(function(){ 
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		queryUEHistoryAlarm(1,3,"",sortColumn,sortDirection);;
	});
	//尾页
	$("#endPage").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();	
		var totalPage = $("#totalPage").html();
		queryUEHistoryAlarm(totalPage,3,"",sortColumn,sortDirection);
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
			queryUEHistoryAlarm(page,3,"",sortColumn,sortDirection);
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
			queryUEHistoryAlarm(page,3,"",sortColumn,sortDirection);
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
				queryUEHistoryAlarm(targetPage,3,"",sortColumn,sortDirection);
			}else if(targetPage <= 1){
				queryUEHistoryAlarm(1,3,"",sortColumn,sortDirection);
			}else{
				queryUEHistoryAlarm(totalPage,3,"",sortColumn,sortDirection);
			}
		}else{
			queryUEHistoryAlarm(1,3,"",sortColumn,sortDirection);
		}
	});
	
	//查询
	$("#queryEnbHistoryAlarm").click(function(){
		var sortColumn = $("#sortColumn").val();
		var sortDirection = $("#sortDirection").val();
		queryUEHistoryAlarm(1,3,"",sortColumn,sortDirection);
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
	/*//全选
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

	//排序
	$("#sortBy1").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "serialNumber";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy2").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmLevel";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy3").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmContent";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy4").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "NeName";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy5").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alignedInfo";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy6").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "alarmState";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy7").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "firstAlarmTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy8").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "restoredTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy9").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "restoreUser";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy10").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "confirmTime";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});
	//排序
	$("#sortBy11").click(function(){
		var page = $("#currentPage").html();
		var array = $("#enbIdArray").val();
		var sortColumn = "confirmUser";
		var sortDirection = parseInt($("#sortDirection").val())*(-1);
		$("#sortDirection").val(sortDirection);
		$("#sortColumn").val(sortColumn);
		
		queryEnbHistoryAlarm(page,3,array,sortColumn,sortDirection);		
	});*/
});
function queryUEHistoryAlarm(page,type,array,sortColumn,sortDirection){
	
	//重置报错
	document.getElementById("submit_check_error").innerHTML = "";
	
	var isEnbId=/^[a-fA-F0-9]{1,}$/;
	
	var timeCheck = $("#timeCheck").is(":checked");
	var beginTime = "";
	var endTime = "";
	var submitObj = {page_num:page,count:10};
  var filter = {};
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
		filter.AlarmFirstTime = beginTime;
		filter.AlarmLastTime = endTime;
	}
	//IMSI
	var IMSI = $("#IMSI").val();
	if($.trim(IMSI).length !==0){
		filter.IMSI = IMSI;
	}
	//级别
	var level = $("#alarmLevel").is(":checked");
	var levelArray = null;
	if(level==true){
		levelArray = [];
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
		if(levelArray.length >0){
			filter.AlarmLevel = levelArray;
		}		
	}
	//状太
	var state = $("#alarmState").is(":checked");
	var stateArray = null;
	if(state==true){
		stateArray = [];
//		var state1 = $("#alarmState1").is(":checked");
		var state2 = $("#alarmState2").is(":checked");
		var state3 = $("#alarmState3").is(":checked");
		var state4 = $("#alarmState4").is(":checked");
//		if(state1==true){
//			stateArray.push("01");
//		}
		if(state2==true){
			stateArray.push("11");
		}
		if(state3==true){
			stateArray.push("00");
		}
		if(state4==true){
			stateArray.push("10");
		}
		if(stateArray.length >0){
			filter.AlarmState = stateArray;
		}		
	}

	//告警内容
	var alarmContent = $("#alarmContent").val();
	if($.trim(alarmContent).length !==0){
		filter.AlarmContent = alarmContent;
	}
	if(!$.isEmptyObject(filter)){
		var filterKeys = Object.getOwnPropertyNames(filter);		
		if(filterKeys.length>1 && !(filterKeys.length ==2 && filterKeys.indexOf("AlarmFirstTime") !== -1)){
			document.getElementById("submit_check_error").innerHTML = "目前不支持多条件查询";
			return;
		}				
		submitObj.alarmfilter = filter;
	}
	var basePath = $("#basePath").val();
	$.ajax({
		type:"post",
		url:"queryUEHistoryAlarm.do",
		data:{submitObj:JSON.stringify(submitObj)},			
		async:false,
		dataType:"json",
		success:function(data){
			if(data.result !=undefined &&data.result !="0"){
				alert("错误："+data.Message);
				return;
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
							else if(data.alarmListModel.alarmList[i].alarm.confirmState ==1 && data.alarmListModel.alarmList[i].alarm.alarmState ==1){
								td2 = '<td>已确认未恢复</td>';
							}
							else if(data.alarmListModel.alarmList[i].alarm.confirmState ==0 && data.alarmListModel.alarmList[i].alarm.alarmState ==0){
								td2 = '<td>未确认已恢复</td>';
							}
							else if(data.alarmListModel.alarmList[i].alarm.confirmState ==1 && data.alarmListModel.alarmList[i].alarm.alarmState ==0){
								td2 = '<td>已确认已恢复</td>';
							}
							else{
								td2 = '<td>未知类型</td>';
							}
							var alarmContent = data.alarmListModel.alarmList[i].alarm.alarmContent;


							//IMSI
							var IMSI =  data.alarmListModel.alarmList[i].alarm.IMSI;

							var restoreUserTd = "";
							var restoreUser = data.alarmListModel.alarmList[i].alarm.restoreUser;
							
							if(restoreUser !=null){
								restoreUserTd = restoreUser;
								if(restoreUserTd.length >10){
									restoreUserTd = restoreUserTd.substr(0, 7)+"...";
								}
							}
							
							var confirmUserTd = "";
							var confirmUser = data.alarmListModel.alarmList[i].alarm.confirmUser;
							if(confirmUser!=null){
								confirmUserTd = confirmUser;
								if(confirmUserTd.length > 10){
									confirmUserTd = confirmUserTd.substr(0, 7)+"...";
								}
							}
							
							var restoredTimeStringTd = "";
							var restoredTimeString = data.alarmListModel.alarmList[i].restoredTimeString;
							if (restoredTimeString != null){
								restoredTimeStringTd = restoredTimeString;		
							}
							
							var confirmTimeStringTd = "";
							var confirmTimeString = data.alarmListModel.alarmList[i].confirmTimeString;
							if (confirmTimeString != null){
								confirmTimeStringTd = confirmTimeString;		
							}
							
							myTr+='<tr>'
								+'<th scope="row"><input type="checkbox" value="checkbox" name="checkson" disabled="disabled"/></th>'
								+'<td style="display:none;">'+data.alarmListModel.alarmList[i].alarm.id+'</td>' 
								+''+td1+''
								+'<td style="text-align:left;padding:0 0 0 5px;">'+alarmContent+'</td>'
								+'<td>'+data.alarmListModel.alarmList[i].UEId+'</td>'
								+'<td style="text-align:left;padding:0 0 0 5px;">'+IMSI+'</td>'
								+''+td2+''
								+'<td>'+data.alarmListModel.alarmList[i].lastAlarmTimeString+'</td>'
								+'<td>'+restoredTimeStringTd+'</td>'
								+'<td style="cursor:default" title="'+restoreUser+'">'+restoreUserTd+'</td>'
								+'<td>'+confirmTimeStringTd+'</td>'
								+'<td style="cursor:default" title="'+confirmUser+'">'+confirmUserTd+'</td>'
								//+"<td></td>"+"<td></td>"+"<td></td>"+"<td></td>"+
								+'</tr>';
							
						}
						$("#currentPage").html(page);
						$("#totalPage").html(
								''+data.alarmListModel.totalPage+''
						);
						$("#targetPageInput").val(page);
						$("#checkfather").attr("disabled",true);
						$("#checkfather").attr("checked",false);
						$("#alarmListBody").html(myTr);
						$("#sysError").html("");
//						$(".changeRowColor tr").each(function(index){
//							if(index > 0 && index%2 == 0){
//								$(".changeRowColor tr:eq("+index+") td").css("background-color","#F6F6F6");
//							}
//						}); 
						
						
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
