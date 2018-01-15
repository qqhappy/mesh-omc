$(function(){
	//表单校验
	//整数正则
	var isNum=/^(-)?\d+$/;
	//精确至0.1的正则
	var isFloatPointOne = /^-?\d+(\.\d{1}){0,1}$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if($("#u8CId option").length < 1){
			index++;
			$("#u8CIdError").text(constantInfoMap["no_item_exist_info"]);
		}
		if(!checkInputInForm(isNum,"u16PdcchPowerOffset",40,-76)){
			index++;
			$("#u16PdcchPowerOffsetError").text(dynamicInfo(10000,generateArgments_i18n_num(40,-76)));
		}
		if(!checkInputInForm(isNum,"u8UlMuMimoPwrThresh",-90,-120)){
			index++;
			$("#u8UlMuMimoPwrThreshError").text(dynamicInfo(10000,generateArgments_i18n_num(-90,-120)));
		}
		if(!checkInputInForm(isFloatPointOne,"u8UlMuMimoRbRatio",1,0)){
			index++;
			$("#u8UlMuMimoRbRatioError").text(dynamicInfo(10001,generateArgments_i18n_num(1,0)));
		}
		if(!checkInputInForm(isFloatPointOne,"u8UlMuMimoTbsizeGama",5,0)){
			index++;
			$("#u8UlMuMimoTbsizeGamaError").text(dynamicInfo(10001,generateArgments_i18n_num(5,0)));
		}
		if(!checkInputInForm(isNum,"u8McsForHoRrcCfg",28,0)){
			index++;
			$("#u8McsForHoRrcCfgError").text(dynamicInfo(10000,generateArgments_i18n_num(28,0)));
		}
		if(!checkInputInForm(isNum,"u8RbNumForMsg2",100,1)){
			index++;
			$("#u8RbNumForMsg2Error").text(dynamicInfo(10000,generateArgments_i18n_num(100,1)));
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8CceAdapMode="+$("#u8CceAdapMode").val()+";"
				+"u16PdcchPowerOffset"+"="+accMul(accAdd($("#u16PdcchPowerOffset").val(),76),10)+";"
				+"u8MatrixType="+$("input[name='u8MatrixType']:checked").val()+";"
				+"b8PttICICSwitch="+$("input[name='b8PttICICSwitch']:checked").val()+";"
				+"u8UlMuMimoPwrThresh="+accAdd($("#u8UlMuMimoPwrThresh").val(),120)+";"
				+"u8UlMuMimoRbRatio="+accMul($("#u8UlMuMimoRbRatio").val(),10)+";"
				+"u8UlMuMimoTbsizeGama="+accMul($("#u8UlMuMimoTbsizeGama").val(),10)+";"
				+"u8McsForHoRrcCfg="+$("#u8McsForHoRrcCfg").val()+";"
				+"u8GapPatternId="+$("#u8GapPatternId").val()+";"
				+"u8RbNumForMsg2="+$("#u8RbNumForMsg2").val()+";"
				+"u8Pkmode="+$("#u8Pkmode").val();
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
	$("#t_cel_alg2 td.u16PdcchPowerOffset").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accSub(accDiv(value,10),76));
		}	
	});
	$("#t_cel_alg2 td.u8UlMuMimoPwrThresh").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accSub(value,120));
		}
		
	});
	$("#t_cel_alg2 td.u8UlMuMimoRbRatio").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accDiv(value,10));
		}
		
	});
	$("#t_cel_alg2 td.u8UlMuMimoTbsizeGama").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accDiv(value,10));
		}		
	});
	$("#t_cel_alg2 td.u8MatrixType").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("MRC");
			break;
		case "1":
			$(this).text("IRC");
			break;
		}	
	});
	$("#t_cel_alg2 td.u8CceAdapMode").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("CceAdapOff");
			break;
		case "1":
			$(this).text("AdapLevel");
			break;
		case "2":
			$(this).text("AdapLevelAndPower");
			break;
		}	
	});
	$("#t_cel_alg2 td.u8GapPatternId").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("0");
			break;
		case "1":
			$(this).text("1");
			break;
		case "2":
			$(this).text("Adaption");
			break;
		}	
	});
	$("#t_cel_alg2 td.u8Pkmode").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("Close Pk Mode");
			break;
		case "1":
			$(this).text("Single Ue Pk Mode");
			break;
		case "2":
			$(this).text("Ping Pk Mode");
			break;
		case "3":
			$(this).text("Ra Pk Mode");
			break;
		case "4":
			$(this).text("Ho Pk Mode");
			break;
		case "5":
			$(this).text("MU_MIMO Pk Mode");
			break;
		}	
	});
	$("#t_cel_alg2 td.b8PttICICSwitch").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("关闭");
			break;
		case "1":
			$(this).text("打开");
			break;
		}	
	});
	
	
});

