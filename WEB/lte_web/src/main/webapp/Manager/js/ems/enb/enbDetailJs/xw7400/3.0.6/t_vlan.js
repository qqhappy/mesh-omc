$(function(){
	//表单校验
	//整数正则
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		$(".error").text("");
		if(!checkInputInForm(isNum,"u8Id",10,1)){
			index++;
			$("#u8IdError").text(dynamicInfo(10000,generateArgments_i18n_num(10,1)));
		}
		if(!checkInputInForm(isNum,"u16VlanId",4094,1)){
			index++;
			$("#u16VlanIdError").text(dynamicInfo(10000,generateArgments_i18n_num(4094,1)));
		}
		if(!checkInputInForm(isNum,"u8VlanPri",7,0)){
			index++;
			$("#u8VlanPriError").text(dynamicInfo(10000,generateArgments_i18n_num(7,0)));
		}
		var para = "u8Id"+"="+$("#u8Id").val()+";"
				+"u16VlanId"+"="+$("#u16VlanId").val()+";"
				+"u8VlanPri"+"="+$("#u8VlanPri").val();
		$("#parameters").val(para);
		if(index==0){
//			if(confirm(constantInfoMap["vlan_confirm"])){ 
				var moId = $("#moId").val(); 
				var basePath=$("#basePath").val();
				var tableName=$("input[name='tableName']").val();
				var operType=$("input[name='operType']").val();
				var enbVersion = $("#enbVersion").val();
				biz_table_ajaxCheck(enbVersion,basePath,moId,tableName,operType,para,'');	
//			}			
		}		
	});
});

