$(function() {
	$(".bb_condition_div input[type='checkbox']").attr("checked","checked");
	// 时间控件
	$(".ui_timepicker").datetimepicker({
		showSecond : true,
		timeFormat : 'hh:mm:ss',
		stepHour : 1,
		stepMinute : 1,
		stepSecond : 1
	});
	// 获取当前时间 
	var date = new Date();

	var yy = date.getFullYear();
	var mm = date.getMonth() + 1;
	var dd = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	// 获取前一周时间
	var yy1 = "";
	var mm1 = "";
	var dd1 = "";
	if (dd <= 6) {
		if (mm == 1) {
			yy1 = yy - 1;
			mm1 = 12;
			dd1 = 31 - (6 - dd);
		}
		if (mm == 2 || mm == 4 || mm == 6 || mm == 8 || mm == 9 || mm == 11) {
			yy1 = yy;
			mm1 = mm - 1;
			dd1 = 31 - (6 - dd);
		}
		if (mm == 5 || mm == 7 || mm == 10 || mm == 12) {
			yy1 = yy;
			mm1 = mm - 1;
			dd1 = 30 - (6 - dd);
		}
		if (mm == 3) {
			yy1 = yy;
			mm1 = 2;
			if ((yy % 4 == 0 && yy % 100 != 0) || yy % 400 == 0) {
				dd1 = 29 - (6 - dd);
			} else {
				dd1 = 28 - (6 - dd);
			}
		}
	} else {
		yy1 = yy;
		mm1 = mm;
		dd1 = dd - 6;
	}
	if (mm < 10) {
		mm = "0" + mm;
	}
	if (dd < 10) {
		dd = "0" + dd;
	}
	if (mm1 < 10) {
		mm1 = "0" + mm1;
	}
	if (dd1 < 10) {
		dd1 = "0" + dd1;
	}
	if (hour < 10) {
		hour = "0" + hour;
	}
	if (minute < 10) {
		minute = "0" + minute;
	}
	if (second < 10) {
		second = "0" + second;
	}
	// 获取时间字符串
	var nowTime = yy + "-" + mm + "-" + dd + " " + hour + ":" + minute + ":"
			+ second;
	var pastTime = yy1 + "-" + mm1 + "-" + dd1 + " " + hour + ":" + minute
			+ ":" + second;
	$("input[name='beginTime']").val(pastTime);
	$("input[name='endTime']").val(nowTime);
	
	$("#queryBB_info").click(function(){
		queryBlackBoxFile(1);
		alert("查询结束");
	});
	//首次查询
	queryBlackBoxFile(1);
	//首页
	$("#firstPage").click(function(){
		queryBlackBoxFile(1);
		$("#checkfather").attr("checked",false);
	}); 
	//尾页
	$("#endPage").click(function(){
		var totalPage = $("#totalPage").html();
		$("#checkfather").attr("checked",false);
		queryBlackBoxFile(totalPage);
	});
	//上一页
	
	$("#previousPage").click(function(){
		var currentPage = $("#currentPage").html();
		$("#checkfather").attr("checked",false);
		if(parseInt(currentPage) == 1){
//			alert("已是首页");
		}else{
			var page = (parseInt(currentPage)-1);
			queryBlackBoxFile(page);
		}
	});
	//下一页
	$("#nextPage").click(function(){
		var currentPage = $("#currentPage").html();
		var totalPage = $("#totalPage").html();
		$("#checkfather").attr("checked",false);
		if(parseInt(currentPage) == parseInt(totalPage)){
//			alert("已是尾页");
		}else{
			var page = (parseInt(currentPage)+1);
			
			queryBlackBoxFile(page);
		}
	});
	//目标页
	$("#targetPage").click(function(){
		var isNum = /^\d+$/;
		var targetPage = $("#targetPageInput").val();
		var totalPage = $("#totalPage").html();
		$("#checkfather").attr("checked",false);
		if(isNum.test(targetPage)){
			if(targetPage >= 1 && targetPage<= parseInt(totalPage)){
				queryBlackBoxFile(targetPage);
			}else if(targetPage <= 1){
				queryBlackBoxFile(1);
			}else{
				queryBlackBoxFile(totalPage);
			}
		}else{
			queryBlackBoxFile(1);
		}
	});
	//刷新
	$("#fresh").click(function(){
		var currentPage = $("#currentPage").html();
		$("#checkfather").attr("checked",false);
			queryBlackBoxFile(currentPage);
	});
	//批量下载
	$("#batch_download").click(function(){
		var fileName = new Array();
		$("#blackBoxFile_content input[type='checkbox']:checked").each(function(){
			fileName.push($(this).val());
		});
		if(fileName.length > 0){
			var basePath  =$("#basePath").val();
			window.location.href = basePath+"/lte/batchDownloadBlackBoxFile.do?fileName="+fileName;
		}
		if(fileName.length == 0){
			alert("请选择黑匣子文件");
		}
		
	});

	// 全选_device
	$("#checkfather_device").live("click", function() {
		$("[name=checkson_device]:checkbox").attr("checked", this.checked);
	});
	$("[name=checkson_device]:checkbox").live("click", function() {
		var flag = true;
		$("[name=checkson_device]:checkbox").each(function() {
			if (!this.checked) {
				flag = false;
			}
		});
		$("#checkfather_device").attr("checked", flag);
	});
	// 全选_condition
	$("#checkfather_condition").live("click", function() {
		$("[name=checkson_condition]:checkbox").attr("checked", this.checked);
	});
	$("[name=checkson_condition]:checkbox").live("click", function() {
		var flag = true;
		$("[name=checkson_condition]:checkbox").each(function() {
			if (!this.checked) {
				flag = false;
			}
		});
		$("#checkfather_condition").attr("checked", flag);
	});

});
function queryBlackBoxFile(page) {
	var begindate = getStringOfNum($("input[name='beginTime']").val());
	var enddate = getStringOfNum($("input[name='endTime']").val());
	var enbids = new Array();
	$("input[name='checkson_device']:checked").each(function(){
		var value = $(this).val();
		enbids.push(value);
	});
	var resetReason = new Array();
	$("input[name='checkson_condition']:checked").each(function(){
		var value = $(this).val();
		resetReason.push(value);
	});
	$.ajax({
		type : "post",
		url : "getPagingDataAction.do",
		data : "blackBoxCondition.begindate=" + begindate
				+ "&blackBoxCondition.enddate=" + enddate
				+ "&blackBoxCondition.enbids=" + enbids
				+ "&blackBoxCondition.resetReason=" + resetReason
				+ "&blackBoxCondition.currentPage=" + page
				+ "&blackBoxCondition.numPerPage=" + 10,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if(status == 0){
				var results = data.message.results;
				if(results.length === 0){
					var tr = '<tr>'+
								'<th></th>'+
								'<td>暂无相关数据</td>'+
								'<td></td>'+
								'<td></td>'+
								'<td></td>'+
								'<td></td>'+
							'</tr>';
					$("#blackBoxFile_content").html(tr);
					$("#currentPage").html("1");
					$("#totalPage").html("1");
					$("#targetPageInput").val("1");
					$("#checkfather").attr("disabled","disabled");
				}else{
					var tr = "";
					for(var i = 0 ;i<results.length;i++){
						tr = tr + '<tr>'+
									'<th><input type="checkbox"  name="checkson" value="'+results[i].fileName+'"/></th>'+
									'<td>'+results[i].enbId+'</td>'+
									'<td>'+results[i].fileTime+'</td>'+
									'<td>'+results[i].cause+'</td>'+
									'<td>'+results[i].fileName+'</td>'+
									'<td><a style="cursor:pointer" href="javascript:download('+ "'" +results[i].fileName+ "'" +');">下载</a></td>'+
								'</tr>';
					}
					$("#blackBoxFile_content").html(tr);
					$("#currentPage").html(data.message.currentPage);
					$("#totalPage").html(data.message.totalPages);
					$("#targetPageInput").val(data.message.currentPage);
					$("#checkfather").attr("disabled",false);
				}				
			}else{
				alert(data.error);
			}
		}
	});
}
// 提取字符串中数字拼成新串
function getStringOfNum(str) {
	var result = "";
	var array = str.split("");
	var numRegex = /^[0-9]{1}$/;
	for(var i = 0;i<array.length ;i++){
		if(numRegex.test(array[i])){
			result = result + array[i];
		}
	}
	return result;
}
function download(fileName){
	var basePath  =$("#basePath").val();
	window.location.href = basePath+"/lte/downLoadBlackBoxFile.do?fileName="+fileName;
}