$(function(){
	
	//首次进入页面加载
	queryCurrentAsset(1);
	
	//控制时间的disabled
	$("#startTime_flag").change(function(){
		if(!$(this).attr("checked")){
			$(".startTime").attr("disabled","disabled");
		}else{
			$(".startTime").attr("disabled",false);
		}
	});
	$(".startTime").change(function(){
		if($(this).val() != "" || $(this).val() != null){
			$("#startTime_flag").attr("checked","checked");
		}
	});
	$("#stopTime_flag").change(function(){
		if(!$(this).attr("checked")){
			$(".stopTime").attr("disabled","disabled");
		}else{
			$(".stopTime").attr("disabled",false);
		}
	});
	$(".stopTime").change(function(){
		if($(this).val() != "" || $(this).val() != null){
			$("#stopTime_flag").attr("checked","checked");
		}
	});
	//资产状态改变刷新
	$("#status").change(function(){
		var currentPage = $("#currentPage").html();
		queryCurrentAsset(1);
	});
	//节点类型改变刷新
	$("#nodeType").change(function(){
		var currentPage = $("#currentPage").html();
		queryCurrentAsset(1);
	});
	//刷新
	$("#fresh").click(function(){
		var currentPage = $("#currentPage").html();
		queryCurrentAsset(currentPage);
	});
	//首页
	$("#firstPage").click(function(){
		queryCurrentAsset(1);
	}); 
	//尾页
	$("#endPage").click(function(){
		var totalPage = $("#totalPage").html();
		queryCurrentAsset(totalPage);
	});
	//上一页	
	$("#previousPage").click(function(){
		var currentPage = $("#currentPage").html();
		if(parseInt(currentPage) != 1){
			var page = (parseInt(currentPage)-1);
			queryCurrentAsset(page);
		}
	});
	//下一页
	$("#nextPage").click(function(){
		var currentPage = $("#currentPage").html();
		var totalPage = $("#totalPage").html();
		if(parseInt(currentPage) != parseInt(totalPage)){
			var page = (parseInt(currentPage)+1);
			queryCurrentAsset(page);
		}
	});
	//目标页
	$("#targetPage").click(function(){
		var isNum = /^\d+$/;
		var targetPage = $("#targetPageInput").val();
		var totalPage = $("#totalPage").html();
		if(isNum.test(targetPage)){
			if(targetPage >= 1 && targetPage<= parseInt(totalPage)){
				queryCurrentAsset(targetPage);
			}else if(targetPage <= 1){
				queryCurrentAsset(1);
			}else{
				queryCurrentAsset(totalPage);
			}
		}else{
			queryCurrentAsset(1);
		}
	});
	//取消修改最后服务时间/其他信息
	$("#confirm_cancle").live("click",function(){
		$(this).parent().parent().parent().parent().hide().siblings("#maskLayer").remove();
	});
	//修改最后服务时间/其他信息
	$("#confirm_submit").live("click",function(){
		var id = $("#assetId").val();
		var actionType = $("#actionType").val();
		var time = $("#timepicker").val();
		var remark = $("#remark").val();
		var regex=/^[A-Za-z0-9\u4e00-\u9fa5_]*$/;
		if(!regex.test(remark)){
			$("#confirm_error").html("只可输入中文、字母、下划线");
			return;
		}else if(remark.length > 128){
			$("#confirm_error").html("输入过长，不可大于128个字符");
			return;
		}
		$(this).parent().parent().parent().parent().hide().siblings("#maskLayer").remove();
		var link = "";
		if(actionType == 0){
			link = "updateLastServerTime.do";
		}else{
			link = "updateRemark.do";
		}
		$.ajax({
			type:"post",
			url: link,
			data:"assetCondition.id="+id+
				"&lastServeTime="+getNumFromString(time)+
				"&remark="+remark,
			dataType:"json",
			async:false,
			success:function(data){
				if(data.status == 0){
					var currentPage = $("#currentPage").html();
					queryCurrentAsset(currentPage);
				}else{
					$("#error").html(data.error);
				}
			}
		});
	});
	
});

function queryCurrentAsset(page){
	//开始使用时间
	var startTime_begin = -1;
	var startTime_end = -1;
	if($("#startTime_flag").attr("checked")){
		startTime_begin = $("#startTime_begin").val();
		if(startTime_begin == "" || startTime_begin == null){
			startTime_begin = -1;
		}
		startTime_end = $("#startTime_end").val();
		if(startTime_end == "" || startTime_end == null){
			startTime_end = -1;
		}
	}
	
	//停止使用时间
	var stopTime_begin = -1;
	var stopTime_end = -1;
	if($("#stopTime_flag").attr("checked")){
		stopTime_begin = $("#stopTime_begin").val();
		if(stopTime_begin == "" || stopTime_begin == null){
			stopTime_begin = -1;
		}
		stopTime_end = $("#stopTime_end").val();
		if(stopTime_end == "" || stopTime_end == null){
			stopTime_end = -1;
		}
	}	
	//资产状态
	var status = $("#status").val();
	//节点类型
	var nodeType  =$("#nodeType").val();
	//资产序列号
	var productionSN = $("#productionSN").val();
	if(productionSN == null){
		productionSN = "";
	}
	//enbHexId
	var enbHexId = $("#enbHexId").val();	
	var enbId = -1;
	/*
	 *校验资产序列号和enbHexId
	 */
	var productionSN_regex = /^[0-9A-Za-z]*$/;
	if(!productionSN_regex.test(productionSN)){
		alert("资产序列号格式错误,只可输入字母和数字");
		return ;
	}
	var enbHexId_regex = /^[0-9A-Za-z]*$/;
	if(enbHexId != "" && enbHexId != null){
		var length = enbHexId.length;	
		if(length!=8 || !(enbHexId_regex.test(enbHexId)) || parseInt(enbHexId,16) > 1048575){
			alert("eNB标识格式错误,范围是00000000至000FFFFF");
			 $("#enbIdArray").focus();
			 return;
		}else{
			enbId = parseInt(enbHexId,16);
		}
	}
	$.ajax({
		type:"post",
		url:"queryCurrentAsset.do",
		data:"assetCondition.startTime_start="+getNumFromString(startTime_begin)+
			"&assetCondition.startTime_end="+getNumFromString(startTime_end)+
			"&assetCondition.stopTime_start="+getNumFromString(stopTime_begin)+
			"&assetCondition.stopTime_end="+getNumFromString(stopTime_end)+
			"&assetCondition.currentPage="+page+
			"&assetCondition.status="+status+
			"&assetCondition.nodeType="+nodeType+
			"&assetCondition.productionSN="+productionSN+
			"&assetCondition.enbId="+enbId+
			"&assetCondition.numPerPage="+10,
		dataType:"json",
		async:false,
		success:function(data){
			var status = data.status;
			if(status == 0){				
				var length  = data.message.length;
				var basePath = $("#basePath").val();
				if(length > 0){
					var myTr = "";
					for(var i =0 ;i<length;i++){
						var tr = '<tr>';
						var confirmTd = '<td></td>';
						if(data.message[i].status == 1){
							confirmTd = '<td><a style="outline:none;cursor:pointer" onclick="javascript:confirmAsset('+data.message[i].id+')">确认</a></td>';
							tr = '<tr style="background-color:yellow;">';
						}	
						var remark = data.message[i].remark+"";
						if(remark.length > 13){
							remark = remark.substring(0,10)+"...";
						}
						myTr = myTr 
									+tr
									+'<th scope="row"><input type="checkbox" value="" name="checkson"/></th>'
									+'<td>'+transferDecimalToHex(data.message[i].enbId)+'</td>'
									+'<td>'+transferNodeType(data.message[i].nodeType)+'</td>'
									+'<td>'+transferLocation(data.message[i].locationInfo)+'</td>'
									+'<td>'+data.message[i].providerName+'</td>'
									+'<td>'+data.message[i].hardwareVersion+'</td>'
									+'<td>'+data.message[i].productionSN+'</td>'
									+'<td>'+formatTimeString12(data.message[i].manufactureDate)+'</td>'
									+'<td>'+formatTimeString14(data.message[i].startTime)+'</td>'
									+'<td>'+formatTimeString14(data.message[i].stopTime)+'</td>'
									+'<td style="width:90px;"><span style="float:left;margin-left:5px;">'+formatTimeString12(data.message[i].lastServeTime)+'</span><img src="'+basePath+'/Manager/js/tree/css/zTreeStyle/img/diy/2.png" style="float:right;cursor:pointer;margin-top:2px;" onclick="javascript:showModifyLST('+data.message[i].id+',this,0)"></td>'
									+'<td style="width:160px;"><span style="float:left;margin-left:5px;" title="'+data.message[i].remark+'" content="'+data.message[i].remark+'">'+remark+'</span><img src="'+basePath+'/Manager/js/tree/css/zTreeStyle/img/diy/2.png" style="float:right;cursor:pointer;margin-top:2px;" onclick="javascript:showRemark('+data.message[i].id+',this,1)"></td>'
									+confirmTd
									+'</tr>';
					}
					$("#error").html("");
					$("#currentPage").html(data.currentPage);
					$("#totalPage").html(data.totalPage);
					$("#targetPageInput").val(data.currentPage);
					$("#checkfather").attr("disabled",false);
					$("#checkfather").attr("checked",false);
					$("#asset_tbody").html(myTr);
				}else{
					var myTr = '<tr>'
						+'<th scope="row"><input type="checkbox" disabled="disabled" value="" name="checkson"/></th>'
						+'<td>暂无相关记录</td>'
						+'<td></td>'
						+'<td></td>'
						+'<td></td>'
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
					$("#error").html("");
					$("#currentPage").html('1');
					$("#totalPage").html('1');
					$("#targetPageInput").val(1);
					$("#checkfather").attr("disabled","disabled");
					$("#asset_tbody").html(myTr);
				}
			}else{				
				var myTr = '<tr>'
					+'<th scope="row"><input type="checkbox" disabled="disabled" value="" name="checkson"/></th>'
					+'<td>暂无相关记录</td>'
					+'<td></td>'
					+'<td></td>'
					+'<td></td>'
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
				$("#error").html(data.error);
				$("#currentPage").html('1');
				$("#totalPage").html('1');
				$("#targetPageInput").val(1);
				$("#checkfather").attr("disabled","disabled");
				$("#asset_tbody").html(myTr);
			}
		}
	});	
}
//从string中按序提取出number拼成新string
function getNumFromString(para){
	var result = "";
	if(para == -1 ){		
		result = -1;
	}else{
		var numRegex = /^[0-9]{1}$/;
		var str = para.split("");
		for(var i = 0;i<str.length;i++){
			if(numRegex.test(str[i])){
				result = result+ str[i];
			}
		}
	}
	return result;
}
//10进制转为16进制 enbHexId
function transferDecimalToHex(enbId){
	var result = parseInt(enbId,10).toString(16);
	var length  = result.length;
	if(length<8){
		for(var i = 0;i<8-length;i++){
			result = "0" + result;
		}
	}
	return result;
}
//将long(14位)型时间字符串format
function formatTimeString14(para){
	var result = "";
	var time = para + "";
	if(para != -1 && time != "" && time != null && time.length == 14){
		result = time.substring(0,4) + "-" + 
					time.substring(4,6)+"-"+
					time.substring(6,8)+" "+
					time.substring(8,10) + ":" + 
					time.substring(10,12)+":"+
					time.substring(12,14);
	}
	return result;
}
//将long(12位)型时间字符串format
function formatTimeString12(para){
	var result = "";
	var time = para + "";
	if(para != -1 && time != "" && time != null && time.length == 8){
		result = time.substring(0,4) + "-" + 
					time.substring(4,6)+"-"+
					time.substring(6,8);
	}
	return result;
}
//转换位置信息
function transferLocation(para){
	var result = para;
	if(para != "" && para != null){
		var lastchar = para.substring(para.length - 1,para.length);
		if(lastchar == 0){
			result="单板";
		}
		var firstchar = para.substring(0,1);
		if(firstchar == 1){
			result = result + "(BBU)";
		}else if(firstchar == 2){
			result = result + "(RRU1)";
		}else if(firstchar == 3){
			result = result + "(RRU2)";
		}else if(firstchar == 4){
			result = result + "(RRU3)";
		}else if(firstchar == 0){
			result = "未配置";
		}
	}
	return result;
}
//转换节点类型
function transferNodeType(para){
	var result = "";
	if(para != "" && para != null){
		if(para == 1){
			result = "BBU";
		}else{
			result = "RRU";
		}
	}
	return result;
}
//确认
function confirmAsset(id){
	if(confirm("确定对此资产进行确认?")){
		$.ajax({
			type:"post",
			url:"confirmAsset.do",
			data:"assetCondition.id="+id,
			dataType:"json",
			async:false,
			success:function(data){
				if(data.status == 0){
					var currentPage = $("#currentPage").html();
					queryCurrentAsset(currentPage);
				}else{
					$("#error").html(data.error);
				}
			}
		});
	}
}
//展示最近服务日期修改窗
function showModifyLST(id,x,y){
	$("#confirm_error").html("");
	$("#remark_li").css("display","none");
	$("#LST_li").css("display","block");
	$("#assetId").val(id);
	$("#actionType").val(y);
	var time = $(x).parents("td:first").find("span").html();
	$("#timepicker").val(time);
	$("#remark").val("");
	popWin("detail");
}
//展示其他信息修改窗
function showRemark(id,x,y){
	$("#confirm_error").html("");
	$("#remark_li").css("display","block");
	$("#LST_li").css("display","none");
	$("#assetId").val(id);
	$("#actionType").val(y);
	var content = $(x).parents("td:first").find("span").attr("content");
	$("#remark").val(content);	
	popWin("detail");
}

