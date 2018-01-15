$(function() {

	$(".ui_timepicker").datetimepicker({
         showSecond: true,
         timeFormat: 'hh:mm:ss',
         stepHour: 1,
         stepMinute: 1,
         stepSecond: 1
	});
	
	var enbStatus = $("#enbStatus").val().split("");
	if (enbStatus == 1) {
		$("#enbStatus").val("不正常");
	} else {
		$("#enbStatus").val("正常");
	}
	
	// 显示时间
	var enbTime=$("#enbTimeHidden").val();
	var year = enbTime.substring(0, 4);
	var month = enbTime.substring(4, 6);
	var day = enbTime.substring(6, 8);
	var hour = enbTime.substring(8, 10);
	var minute = enbTime.substring(10, 12);
	var second = enbTime.substring(12, 14);
	var myEnbTime = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
	$("input[name='enbTime']").val(myEnbTime);	
	
	var runningTimeStr = $("#runningTime").val();
	var runningTime = parseInt(runningTimeStr);
	var oneDay = 60*60*24;
	var dayCount = Math.floor(runningTime/oneDay);
	var oneHour = 60*60;
	var hourCount = Math.floor(((runningTime - dayCount*oneDay)/oneHour));
	var oneMinute = 60;
	var minuteCount = Math.floor((runningTime - dayCount*oneDay - hourCount*oneHour)/oneMinute);
	var secondCount = runningTime - dayCount*oneDay - hourCount*oneHour - minuteCount*oneMinute;
	$("#runningTime").val(dayCount+"天"+hourCount+"小时"+minuteCount+"分钟"+secondCount+"秒");
	
	var clockType = $("#clockType").val().split("");
//	0-gps;1-本板时钟
	if(clockType == 0) {
		$("#clockType").val("GPS");
	} else {
		$("#clockType").val("本板时钟");
	}

	var clockStatus = $("#clockStatus").val().split("");
//	<3:自由振荡(0:startup 1:warmup 2:fast)
//	=3:锁定/同步(lock)
//	=4:保持(holdover)
//	=5:保持超时(holdover timeout)
//	=6:异常(abnormal)
	if(clockStatus < 3) {
		$("#clockStatus").val("自由振荡");
	} else if(clockStatus == 3) {
		$("#clockStatus").val("同步");
	} else if(clockStatus == 4) {
		$("#clockStatus").val("保持");
	} else if(clockStatus == 5) {
		$("#clockStatus").val("保持超时");
	} else if(clockStatus == 6) {
		$("#clockStatus").val("异常");
	}
	
	var portWorkMode = $("#portWorkMode").val();
	if(portWorkMode != undefined){
		var enbVersion = $("#enbVersion").val();
		// 版本兼容性处理，基站3.0.0版本开始加入端口工作模式，值为：0-电口，1光口
		if(enbVersion == "3.0.0" || enbVersion == "3.0.1") {
			if (portWorkMode == 0) {
				$("#portWorkMode").val("电口");
			} else if(portWorkMode == 1) {
				$("#portWorkMode").val("光口");
			} else {
				$("#portWorkMode").val("异常值("+portWorkMode+")");
			}
		} else {
			// 基站3.0.2版本将端口工作模式的值修改为：1-电口，2-光口
			if (portWorkMode == 1) {
				$("#portWorkMode").val("电口");
			} else if(portWorkMode == 2) {
				$("#portWorkMode").val("光口");
			} else {
				$("#portWorkMode").val("异常值("+portWorkMode+")");
			}
		}
		
	}
	
	var portRate = $("#portRate").val();
	if(portRate != undefined){
		if (portRate != 10 && portRate != 100 && portRate != 1000) {
			$("#portRate").val("异常值("+portRate+")");
		}
	}
	
	var portDuplexMode = $("#portDuplexMode").val();
	if(portDuplexMode != undefined){
		if (portDuplexMode == 0) {
			$("#portDuplexMode").val("半双工");
		} else if (portDuplexMode == 1) {
			$("#portDuplexMode").val("全双工");
		} else {
			$("#portDuplexMode").val("异常值("+portDuplexMode+")");
		}
	}
	
//	var rfFreqState = $("#rfFreqState").val();
//	if(rfFreqState == 0){
//		$("#rfFreqState").val();
//	}else if(rfFreqState == 1){
//		$("#rfFreqState").val();
//	} 
	
	$("#changeEnbTime").click(function(){
		var isTimeEnable = $("input[name='enbTime']").attr("disabled");
		if(isTimeEnable!="disabled") {
			$("input[name='enbTime']").attr("disabled","disabled");
			$("#changeEnbTime").val("修改");	

			var basePath=$("#basePath").val();
			var moId=$("#moId").val();
			var enbHexId=$("#enbHexId").val();
			var enbTime = $("input[name='enbTime']").val();
			var href = basePath+"/lte/changeEnbTime.do?moId="+moId+"&enbHexId="+enbHexId+"&enbTime="+enbTime;

			window.location.href=""+href+"";
		} else {
			$("input[name='enbTime']").removeAttr("disabled");
			$("#changeEnbTime").val("确定");
		}
		
	});	
	
});