$(function(){
	//表单校验
	//整数正则
	var isNum=/^\d+$/;
	//精确至0.01的正则
	var isFloatPointTwo = /^-?\d+(\.\d{1,2}){0,1}$/;
	//精确至0.1的正则
	var isFloatPointOne = /^-?\d+(\.\d{1}){0,1}$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if($("#u8CellId option").length < 1){
			index++;
			$("#u8CellIdError").text(constantInfoMap["no_item_exist_info"]);
		}
		if(!checkInputInForm(isNum,"u8SpsPackNum",20,0)){
			index++;
			$("#u8SpsPackNumError").text(dynamicInfo(10000,generateArgments_i18n_num(20,0)));
		}
		if(!checkInputInForm(isNum,"u16SpsPacketStdDev",500,0)){
			index++;
			$("#u16SpsPacketStdDevError").text(dynamicInfo(10000,generateArgments_i18n_num(500,0)));
		}
		if(!checkInputInForm(isNum,"u8SpsJitter",60,0)){
			index++;
			$("#u8SpsJitterError").text(dynamicInfo(10000,generateArgments_i18n_num(60,0)));
		}
		if(!checkInputInForm(isNum,"u8NumHarqFail",10,0)){
			index++;
			$("#u8NumHarqFailError").text(dynamicInfo(10000,generateArgments_i18n_num(10,0)));
		}
		if(!checkInputInForm(isFloatPointTwo,"u8SpsTargetBler",1,0)){
			index++;
			$("#u8SpsTargetBlerError").text(dynamicInfo(10002,generateArgments_i18n_num(1,0)));
		}
		if(!checkInputInForm(isFloatPointOne,"u8StepForSps",1,0)){
			index++;
			$("#u8StepForSpsError").text(dynamicInfo(10001,generateArgments_i18n_num(1,0)));
		}
		if(!checkInputInForm(isNum,"u8DeltaToDynamicSch",15,1)){
			index++;
			$("#u8DeltaToDynamicSchError").text(dynamicInfo(10000,generateArgments_i18n_num(15,1)));
		}
		if(!checkInputInForm(isNum,"u8NumberOfConfSpsProcesses",8,1)){
			index++;
			$("#u8NumberOfConfSpsProcessesError").text(dynamicInfo(10000,generateArgments_i18n_num(8,1)));
		}
		if(!checkInputInForm(isNum,"u8ReTxTimesDlSPS",4,1)){
			index++;
			$("#u8ReTxTimesDlSPSError").text(dynamicInfo(10000,generateArgments_i18n_num(4,1)));
		}
		if(!checkInputInForm(isNum,"u16N_SpsANChn",1024,0)){
			index++;
			$("#u16N_SpsANChnError").text(dynamicInfo(10000,generateArgments_i18n_num(1024,0)));
		}
		if(!checkInputInForm(isNum,"u8ReTxTimesUlSps",4,1)){
			index++;
			$("#u8ReTxTimesUlSpsError").text(dynamicInfo(10000,generateArgments_i18n_num(4,1)));
		}
		var para = "u8CellId"+"="+$("#u8CellId").val()+";"
					+"u8SpsPackNum"+"="+$("#u8SpsPackNum").val()+";"
					+"u16SpsPacketStdDev"+"="+$("#u16SpsPacketStdDev").val()+";"
					+"u8SpsJitter"+"="+$("#u8SpsJitter").val()+";"
					+"u8NumHarqFail"+"="+$("#u8NumHarqFail").val()+";"
					+"u8SpsTargetBler"+"="+accMul($("#u8SpsTargetBler").val(),100)+";"
					+"u8StepForSps"+"="+accMul($("#u8StepForSps").val(),10)+";"
					+"u8DeltaToDynamicSch"+"="+$("#u8DeltaToDynamicSch").val()+";"
					+"u8SpsDownLinkSwicth"+"="+$("input[name='u8SpsDownLinkSwicth']:checked").val()+";"
					+"u8SemiPersistSchedIntervalDl"+"="+$("#u8SemiPersistSchedIntervalDl").val()+";"
					+"u8NumberOfConfSpsProcesses"+"="+$("#u8NumberOfConfSpsProcesses").val()+";"
					+"u8ReTxTimesDlSPS"+"="+$("#u8ReTxTimesDlSPS").val()+";"
					+"u16N_SpsANChn"+"="+$("#u16N_SpsANChn").val()+";"
					+"u8SpsUpLinkSwicth"+"="+$("input[name='u8SpsUpLinkSwicth']:checked").val()+";"
					+"u8SemiPersistSchedIntervalUl"+"="+$("#u8SemiPersistSchedIntervalUl").val()+";"
					+"u8ImplicitReleaseAfter"+"="+$("#u8ImplicitReleaseAfter").val()+";"
					+"u8TwoIntervalsConfig"+"="+$("input[name='u8TwoIntervalsConfig']:checked").val()+";"
					+"u8ReTxTimesUlSps"+"="+$("#u8ReTxTimesUlSps").val();
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
	$("#t_cel_sps td.u8SpsTargetBler").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accDiv(value,100));
		}		
	});
	$("#t_cel_sps td.u8StepForSps").each(function(){
		var value = $.trim($(this).text());
		if(value != null && typeof(value) != "undefined" && value != ""){
			$(this).text(accDiv(value,10));
		}		
	});
	$("#t_cel_sps td.u8SpsDownLinkSwicth").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("关");
			break;
		case "1":
			$(this).text("开");
			break;
		}	
	});
	$("#t_cel_sps td.u8SemiPersistSchedIntervalDl").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("sf10");
			break;
		case "1":
			$(this).text("sf20");
			break;
		case "2":
			$(this).text("sf30");
			break;
		case "3":
			$(this).text("sf40");
			break;
		case "4":
			$(this).text("sf60");
			break;
		case "5":
			$(this).text("sf80");
			break;
		case "6":
			$(this).text("sf120");
			break;
		case "7":
			$(this).text("sf160");
			break;
		case "8":
			$(this).text("sf320");
			break;
		case "9":
			$(this).text("sf640");
			break;
		}	
	});
	$("#t_cel_sps td.u8SpsUpLinkSwicth").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("关");
			break;
		case "1":
			$(this).text("开");
			break;
		}	
	});
	$("#t_cel_sps td.u8SemiPersistSchedIntervalUl").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("sf10");
			break;
		case "1":
			$(this).text("sf20");
			break;
		case "2":
			$(this).text("sf30");
			break;
		case "3":
			$(this).text("sf40");
			break;
		case "4":
			$(this).text("sf60");
			break;
		case "5":
			$(this).text("sf80");
			break;
		case "6":
			$(this).text("sf120");
			break;
		case "7":
			$(this).text("sf160");
			break;
		case "8":
			$(this).text("sf320");
			break;
		case "9":
			$(this).text("sf640");
			break;
		}	
	});
	$("#t_cel_sps td.u8ImplicitReleaseAfter").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("2");
			break;
		case "1":
			$(this).text("3");
			break;
		case "2":
			$(this).text("4");
			break;
		case "3":
			$(this).text("8");
			break;
		}	
	});
});

