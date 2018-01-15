$(function(){
	//表单校验
	//整数正则
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if($("#u8CId option").length < 1){
			index++;
			$("#u8CIdError").text(constantInfoMap["no_item_exist_info"]);
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8PingSwitch="+$("#u8PingSwitch").val()+";"
				+"u8SubframeAdv="+$("#u8SubframeAdv").val()+";"
				+"u8TimerRcvPactet="+$("#u8TimerRcvPactet").val()+";"
				+"u8PingCounter="+$("#u8PingCounter").val();
		$("#parameters").val(para);
		if(index==0){
			var moId = $("#moId").val(); 
			var basePath=$("#basePath").val();
			var tableName=$("input[name='tableName']").val();
			var operType=$("input[name='operType']").val();
			var enbVersion = $("#enbVersion").val();
			biz_table_ajaxCheck(enbVersion,basePath,moId,tableName,operType,para,'');	
		}		
	});
	//内存值转为显示值
	$("#t_cel_pingopt td.u8PingSwitch").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("off");
			break;
		case "1":
			$(this).text("part");
			break;
		case "2":
			$(this).text("full");
			break;
		}	
	});
	$("#t_cel_pingopt td.u8SubframeAdv").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("5");
			break;
		case "1":
			$(this).text("10");
			break;
		case "2":
			$(this).text("15");
			break;
		case "3":
			$(this).text("20");
			break;
		case "4":
			$(this).text("25");
			break;
		case "5":
			$(this).text("30");
			break;
		case "6":
			$(this).text("35");
			break;
		case "7":
			$(this).text("40");
			break;
		}	
	});
	$("#t_cel_pingopt td.u8TimerRcvPactet").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("1280");
			break;
		case "1":
			$(this).text("2560");
			break;
		case "2":
			$(this).text("5120");
			break;
		case "3":
			$(this).text("8192");
			break;
		}	
	});
	$("#t_cel_pingopt td.u8PingCounter").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("关闭PK模式");
			break;
		case "1":
			$(this).text("单UE峰值流量PK");
			break;
		case "2":
			$(this).text("ping包时延PK");
			break;
		case "3":
			$(this).text("接入时延PK");
			break;
		case "4":
			$(this).text("切换时延PK");
			break;
		case "5":
			$(this).text("MU_MIMO PK");
			break;
		}	
	});
	
	
});

