$(function(){
	//表单校验
	//整数正则
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if($("#u8CellId option").length < 1){
			index++;
			$("#u8CellIdError").text(constantInfoMap["no_item_exist_info"]);
		}
		if(!checkInputInForm(isNum,"u8MaxPrmtErabNum",5,1)){
			index++;
			$("#u8MaxPrmtErabNumError").text(dynamicInfo(10000,generateArgments_i18n_num(5,1)));
		}
		if(!checkInputInForm(isNum,"u16AdmitMaxUeNum",3600,1)){
			index++;
			$("#u16AdmitMaxUeNumError").text(dynamicInfo(10000,generateArgments_i18n_num(3600,1)));
		}
		if(!checkInputInForm(isNum,"u8AdmitUeMaxErabNum",8,1)){
			index++;
			$("#u8AdmitUeMaxErabNumError").text(dynamicInfo(10000,generateArgments_i18n_num(8,1)));
		}
		if(!checkInputInForm(isNum,"u16AdmitMaxErabNum",5400,1)){
			index++;
			$("#u16AdmitMaxErabNumError").text(dynamicInfo(10000,generateArgments_i18n_num(5400,1)));
		}
		var para = "u8CellId"+"="+$("#u8CellId").val()+";"
				+"b8AdmitSwitch"+"="+$("input[name='b8AdmitSwitch']:checked").val()+";"
				+"b8CongestSwitch"+"="+$("input[name='b8CongestSwitch']:checked").val()+";"
				+"u8CongestPrmtType"+"="+$("#u8CongestPrmtType").val()+";"
				+"u8MaxPrmtErabNum"+"="+$("#u8MaxPrmtErabNum").val()+";"
				+"u16AdmitMaxUeNum"+"="+$("#u16AdmitMaxUeNum").val()+";"
				+"u8AdmitUeMaxErabNum"+"="+$("#u8AdmitUeMaxErabNum").val()+";"
				+"u16AdmitMaxErabNum"+"="+$("#u16AdmitMaxErabNum").val();
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
	$("#t_cel_admit td.u8CongestPrmtType").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("不选择本UE作为强拆对象");
			break;
		case "1":
			$(this).text("优先选择本UE作为强拆对象");
			break;
		}
	});
	$("#t_cel_admit td.b8AdmitSwitch").each(function(){
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
	$("#t_cel_admit td.b8CongestSwitch").each(function(){
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
});

