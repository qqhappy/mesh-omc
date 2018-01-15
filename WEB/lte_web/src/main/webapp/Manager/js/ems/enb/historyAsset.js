$(function(){
	//查询用户填入dom
	queryAllUserForAsset();
	//首次进入页面加载
	queryHistoryAsset(1);
	
	
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
	$("#confirmStopTime_flag").change(function(){
		if(!$(this).attr("checked")){
			$(".confirmStopTime").attr("disabled","disabled");
		}else{
			$(".confirmStopTime").attr("disabled",false);
		}
	});
	$(".confirmStopTime").change(function(){
		if($(this).val() != "" || $(this).val() != null){
			$("#confirmStopTime_flag").attr("checked","checked");
		}
	});
	//资产状态改变刷新
	$("#status").change(function(){
		var currentPage = $("#currentPage").html();
		queryHistoryAsset(currentPage);
	});
	//节点类型改变刷新
	$("#nodeType").change(function(){
		var currentPage = $("#currentPage").html();
		queryHistoryAsset(1);
	});
	//用户改变刷新
	$("#confirmUser").change(function(){
		var currentPage = $("#currentPage").html();
		queryHistoryAsset(1);
	});
	//刷新
	$("#fresh").click(function(){
		var currentPage = $("#currentPage").html();
		queryHistoryAsset(currentPage);
	});
	//首页
	$("#firstPage").click(function(){
		queryHistoryAsset(1);
	}); 
	//尾页
	$("#endPage").click(function(){
		var totalPage = $("#totalPage").html();
		queryHistoryAsset(totalPage);
	});
	//上一页	
	$("#previousPage").click(function(){
		var currentPage = $("#currentPage").html();
		if(parseInt(currentPage) != 1){
			var page = (parseInt(currentPage)-1);
			queryHistoryAsset(page);
		}
	});
	//下一页
	$("#nextPage").click(function(){
		var currentPage = $("#currentPage").html();
		var totalPage = $("#totalPage").html();
		if(parseInt(currentPage) != parseInt(totalPage)){
			var page = (parseInt(currentPage)+1);
			queryHistoryAsset(page);
		}
	});
	//目标页
	$("#targetPage").click(function(){
		var isNum = /^\d+$/;
		var targetPage = $("#targetPageInput").val();
		var totalPage = $("#totalPage").html();
		if(isNum.test(targetPage)){
			if(targetPage >= 1 && targetPage<= parseInt(totalPage)){
				queryHistoryAsset(targetPage);
			}else if(targetPage <= 1){
				queryHistoryAsset(1);
			}else{
				queryHistoryAsset(totalPage);
			}
		}else{
			queryHistoryAsset(1);
		}
	});
});
function queryHistoryAsset(page){
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

	//确认时间
	var confirmStopTime_begin = -1;
	var confirmStopTime_end = -1;
	if($("#confirmStopTime_flag").attr("checked")){
		confirmStopTime_begin = $("#confirmStopTime_begin").val();
		if(confirmStopTime_begin == "" || confirmStopTime_begin == null){
			confirmStopTime_begin = -1;
		}
		confirmStopTime_end = $("#confirmStopTime_end").val();
		if(confirmStopTime_end == "" || confirmStopTime_end == null){
			confirmStopTime_end = -1;
		}
	}	
	//节点类型
	var nodeType  =$("#nodeType").val();
	//确认用户
	var confirmUser = $("#confirmUser").val();
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
			alert("eNB格式错误,范围是00000000至000FFFFF");
			 $("#enbIdArray").focus();
			 return;
		}else{
			enbId = parseInt(enbHexId,16);
		}
	}
	$.ajax({
		type:"post",
		url:"queryHistoryAsset.do",
		data:"assetCondition.startTime_start="+getNumFromString(startTime_begin)+
			"&assetCondition.startTime_end="+getNumFromString(startTime_end)+
			"&assetCondition.stopTime_start="+getNumFromString(stopTime_begin)+
			"&assetCondition.stopTime_end="+getNumFromString(stopTime_end)+
			"&assetCondition.confirmStopTime_start="+getNumFromString(confirmStopTime_begin)+
			"&assetCondition.confirmStopTime_end="+getNumFromString(confirmStopTime_end)+
			"&assetCondition.confirmUser="+confirmUser+
			"&assetCondition.currentPage="+page+
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
				if(length > 0){
					var myTr = "";
					for(var i =0 ;i<length;i++){
						var remark = data.message[i].remark;
						if(remark.length > 13){
							remark = remark.substring(0,10)+"...";
						}
						myTr = myTr + '<tr>'
								+'<td>'+transferDecimalToHex(data.message[i].enbId)+'</td>'
								+'<td>'+transferNodeType(data.message[i].nodeType)+'</td>'
								+'<td>'+transferLocation(data.message[i].locationInfo)+'</td>'
								+'<td>'+data.message[i].providerName+'</td>'
								+'<td>'+data.message[i].hardwareVersion+'</td>'
								+'<td>'+data.message[i].productionSN+'</td>'
								+'<td>'+formatTimeString12(data.message[i].manufactureDate)+'</td>'
								+'<td>'+formatTimeString14(data.message[i].startTime)+'</td>'
								+'<td>'+formatTimeString14(data.message[i].stopTime)+'</td>'
								+'<td>'+formatTimeString14(data.message[i].confirmStopTime)+'</td>'
								+'<td>'+data.message[i].confirmUser+'</td>'
								+'<td>'+formatTimeString12(data.message[i].lastServeTime)+'</td>'
								+'<td title="'+data.message[i].remark+'">'+remark+'</td>'
								+'</tr>';
					}
					$("#error").html("");
					$("#currentPage").html(data.currentPage);
					$("#totalPage").html(data.totalPage);
					$("#targetPageInput").val(data.currentPage);
					$("#asset_tbody").html(myTr);
				}else{
					var myTr = '<tr>'
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
						+'<td></td>'
						+'</tr>'
						;
					$("#error").html("");
					$("#currentPage").html('1');
					$("#totalPage").html('1');
					$("#targetPageInput").val(1);
					$("#asset_tbody").html(myTr);
				}
			}else{				
				var myTr = '<tr>'
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
					+'<td></td>'
					+'</tr>'
					;
				$("#error").html(data.error);
				$("#currentPage").html('1');
				$("#totalPage").html('1');
				$("#targetPageInput").val(1);
				$("#asset_tbody").html(myTr);
			}
		}
	});	
}
//从string中按序提取出number拼成新string
function getNumFromString(para){
	var result = "";
	if(para != -1){
		var numRegex = /^[0-9]{1}$/;
		var str = para.split("");
		for(var i = 0;i<str.length;i++){
			if(numRegex.test(str[i])){
				result = result+ str[i];
			}
		}
	}else{
		result = -1;
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

//查询所有用户
function queryAllUserForAsset(){
	$.ajax({
		type:"post",
		url:"queryAllUserForAsset.do",
		dataType:"json",
		async:false,
		success:function(data){
			var myOp = "<option value=''>全部</option>";
			if(data.status == 0){
				for(var i = 0 ;i<data.message.length;i++){
					myOp = myOp + '<option value="'+data.message[i].username+'">'+data.message[i].username+'</option>';
				}
			}
			$("#confirmUser").html(myOp);
		}
	});
}
