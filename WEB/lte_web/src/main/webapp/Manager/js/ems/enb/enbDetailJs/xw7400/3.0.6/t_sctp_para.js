$(function(){
	//表单校验
	//整数正则
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if(!checkInputInForm(isNum,"u8Id",255,1)){
			index++;
			$("#u8IdError").text(dynamicInfo(10000,generateArgments_i18n_num(255,1)));
		}
		if(!checkInputInForm(isNum,"u32Hb_interval",86400000,1)){
			index++;
			$("#u32Hb_intervalError").text(dynamicInfo(10000,generateArgments_i18n_num(86400000,1)));
		}
		if(!checkInputInForm(isNum,"u32Assoc_maxretrans",128,1)){
			index++;
			$("#u32Assoc_maxretransError").text(dynamicInfo(10000,generateArgments_i18n_num(128,1)));
		}
		if(!checkInputInForm(isNum,"u32Path_maxretrans",128,1)){
			index++;
			$("#u32Path_maxretransError").text(dynamicInfo(10000,generateArgments_i18n_num(128,1)));
		}
		if(!checkInputInForm(isNum,"u32Rto_initial",60000000,1000)){
			index++;
			$("#u32Rto_initialError").text(dynamicInfo(10000,generateArgments_i18n_num(60000000,1000)));
		}
		if(!checkInputInForm(isNum,"u32Rto_max",60000000,1000)){
			index++;
			$("#u32Rto_maxError").text(dynamicInfo(10000,generateArgments_i18n_num(60000000,1000)));
		}
		if(!checkInputInForm(isNum,"u32Rto_min",60000,500)){
			index++;
			$("#u32Rto_minError").text(dynamicInfo(10000,generateArgments_i18n_num(60000,500)));
		}
		var para = "u8Id"+"="+$("#u8Id").val()+";"
					+"u32Hb_interval"+"="+$("#u32Hb_interval").val()+";"
					+"u32Assoc_maxretrans"+"="+$("#u32Assoc_maxretrans").val()+";"
					+"u32Path_maxretrans"+"="+$("#u32Path_maxretrans").val()+";"
					+"u32Rto_initial"+"="+$("#u32Rto_initial").val()+";"
					+"u32Rto_max"+"="+$("#u32Rto_max").val()+";"
					+"u32Rto_min"+"="+$("#u32Rto_min").val()+";"
					+"u32Assoc_closetype"+"="+$("input[name='u32Assoc_closetype']:checked").val();
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
	$("#t_sctp_para td.u32Assoc_closetype").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("强制关闭");
			break;
		case "1":
			$(this).text("优雅关闭");
			break;
		}	
	});
});

