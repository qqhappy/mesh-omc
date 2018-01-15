$(function(){
	var isLit = /^(-)?\d+(\.[0-9]{1,2}){0,1}$/;	   
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){	
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		var u8PttBPagingFN_Max = parseInt($("#u8PttBPagingFN_Max").html());
		if(!(isNum.test($("#u8PttBPagingFN").val()) && $("#u8PttBPagingFN").val()<=u8PttBPagingFN_Max  && $("#u8PttBPagingFN").val()>=0)){
			$("#u8PttBPagingFNError").text("/* 请输入0~"+u8PttBPagingFN_Max+"之间的整数 */");
			index++;
		}else{
			$("#u8PttBPagingFNError").text("");
		}
		if(!(isNum.test($("#u8PttBPagingSubFN").val()) && $("#u8PttBPagingSubFN").val()<=9  && $("#u8PttBPagingSubFN").val()>=0)){
			$("#u8PttBPagingSubFNError").text("/* 请输入0~9之间的整数 */");
			index++;
		}else{
			$("#u8PttBPagingSubFNError").text("");
		}
		if(!(isNum.test($("#u8PttSignalMCS").val()) && $("#u8PttSignalMCS").val()<=28  && $("#u8PttSignalMCS").val()>=0)){
			$("#u8PttSignalMCSError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else{
			$("#u8PttSignalMCSError").text("");
		}
		if(!(isNum.test($("#u8PttDataMCS").val()) && $("#u8PttDataMCS").val()<=28  && $("#u8PttDataMCS").val()>=0)){
			$("#u8PttDataMCSError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else{
			$("#u8PttDataMCSError").text("");
		}
		if(!(isNum.test($("#u16PttCfgSendCycle").val()) && $("#u16PttCfgSendCycle").val()<=2560  && $("#u16PttCfgSendCycle").val()>=128)){
			$("#u16PttCfgSendCycleError").text("/* 请输入128~2560之间的整数 */");
			index++;
		}else{
			$("#u16PttCfgSendCycleError").text("");
		}
		if(!(isNum.test($("#u16PttNbrCfgSendCycle").val()) && $("#u16PttNbrCfgSendCycle").val()<=2560  && $("#u16PttNbrCfgSendCycle").val()>=128)){
			$("#u16PttNbrCfgSendCycleError").text("/* 请输入128~2560之间的整数 */");
			index++;
		}else{
			$("#u16PttNbrCfgSendCycleError").text("");
		}
		if(!(isNum.test($("#u8PttPeriodPagingCycle").val()) && $("#u8PttPeriodPagingCycle").val()<=255  && $("#u8PttPeriodPagingCycle").val()>=1)){
			$("#u8PttPeriodPagingCycleError").text("/* 请输入1~255之间的整数 */");
			index++;
		}else{
			$("#u8PttPeriodPagingCycleError").text("");
		}
		if(!(isLit.test($("#u8PttSpsThrRbNum").val()) && $("#u8PttSpsThrRbNum").val()<=1  && $("#u8PttSpsThrRbNum").val()>=0)){
			$("#u8PttSpsThrRbNumError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u8PttSpsThrRbNumError").text("");
		}
		if(!(isNum.test($("#u8PttDlMaxRbNum").val()) && $("#u8PttDlMaxRbNum").val()<=100  && $("#u8PttDlMaxRbNum").val()>=0)){
			$("#u8PttDlMaxRbNumError").text("/* 请输入0~100之间的整数 */");
			index++;
		}else{
			$("#u8PttDlMaxRbNumError").text("");
		}
		if(!(isNum.test($("#u16PttSpsPackSizeMin").val()) && $("#u16PttSpsPackSizeMin").val()<=500  && $("#u16PttSpsPackSizeMin").val()>=0)){
			$("#u16PttSpsPackSizeMinError").text("/* 请输入0~500之间的整数 */");
			index++;
		}else{
			$("#u16PttSpsPackSizeMinError").text("");
		}
		if(!(isNum.test($("#u16PttSpsPackSizeMax").val()) && $("#u16PttSpsPackSizeMax").val()<=500  && $("#u16PttSpsPackSizeMax").val()>=0)){
			$("#u16PttSpsPackSizeMaxError").text("/* 请输入0~500之间的整数 */");
			index++;
		}else{
			$("#u16PttSpsPackSizeMaxError").text("");
		}
		if(!(isNum.test($("#u8SpsJitter").val()) && $("#u8SpsJitter").val()<=60  && $("#u8SpsJitter").val()>=0)){
			$("#u8SpsJitterError").text("/* 请输入0~60之间的整数 */");
			index++;
		}else{
			$("#u8SpsJitterError").text("");
		}
		
		var u8PttSpsThrRbNum = $("#u8PttSpsThrRbNum").val();
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8PttBPagingCycle"+"="+$("#u8PttBPagingCycle").val()+";"
				+"u8PttBPagingFN"+"="+$("#u8PttBPagingFN").val()+";"
				+"u8PttBPagingSubFN"+"="+$("#u8PttBPagingSubFN").val()+";"
				+"u8PtForPDSCH"+"="+$("#u8PtForPDSCH").val()+";"
				+"u8PttSignalMCS"+"="+$("#u8PttSignalMCS").val()+";"
				+"u8PttDataMCS"+"="+$("#u8PttDataMCS").val()+";"
				+"u16PttCfgSendCycle"+"="+$("#u16PttCfgSendCycle").val()+";"
				+"u16PttNbrCfgSendCycle"+"="+$("#u16PttNbrCfgSendCycle").val()+";"
				+"u8PttPeriodPagingCycle"+"="+$("#u8PttPeriodPagingCycle").val()+";"
				+"u8PttSpsSwitch"+"="+$("input[name='u8PttSpsSwitch']:checked").val()+";"
				+"u8PttSpsInterval"+"="+$("#u8PttSpsInterval").val()+";"
				+"u8PttSpsThrRbNum"+"="+accMul(u8PttSpsThrRbNum,100)+";"
				+"b8PttDlRbEnable"+"="+$("input[name='b8PttDlRbEnable']:checked").val()+";"
				+"u16PttSpsPackSizeMin"+"="+$("#u16PttSpsPackSizeMin").val()+";"
				+"u16PttSpsPackSizeMax"+"="+$("#u16PttSpsPackSizeMax").val()+";"
				+"u8SpsJitter"+"="+$("#u8SpsJitter").val()+";"
				+"u8PttDlMaxRbNum"+"="+$("#u8PttDlMaxRbNum").val()+";"
				+"u8TcchTransType="+$("#u8TcchTransType").val();
		$("#parameters").val(para);
		if(index==0){
			var moId = $("#moId").val();
			var basePath=$("#basePath").val();
			var tableName=$("input[name='tableName']").val();
			var operType=$("input[name='operType']").val();
			var enbVersion = $("#enbVersion").val();
			$.ajax({ 
				type:"post",
				url:"queryAsyncEnbBiz.do",
				data:"moId="+moId+
					"&basePath="+basePath+
					""+"&browseTime="+getBrowseTime()+"&tableName="+tableName+
					"&operType="+operType+
					"&enbVersion="+enbVersion+
					"&parameters="+para,
				dataType:"json",
				async:false,
				success:function(data){		
					if(!sessionsCheck(data,basePath)){
						return ;
					}
					var errorHtml = data.errorModel.error;
					if(errorHtml != null && errorHtml != ""){					
						if(data.errorModel.errorType == 0){
							
							//BizException
							$(".error").text("");
							$(".error").removeAttr("title");
							var errorEntity = data.errorModel.errorEntity;		
							if (errorHtml.length >10){
								var errorHtmlLow = errorHtml.substr(0,10) + "...";
								var errorStar = "/* "+errorHtmlLow+" */";
								$("#"+errorEntity+"Error").text(errorStar);
								$("#"+errorEntity+"Error").attr("title",errorHtml);
							}else{
								var errorStarTwo = "/* "+errorHtml+" */";
								$("#"+errorEntity+"Error").text(errorStarTwo);	
							}
						}else{
							
							//Exception
							$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();
						}
					}else{
						
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7";
					}				
				}
			});	
		}
		
	});
	//内存值转为显示值
	$("#t_cel_ptt1 tr").each(function(index){
		//
		if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf2");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf4");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf8");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf16");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf32");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf64");
		}else{
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(1)").text("rf128");	
		}	
		//
		if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB-6");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB-4dot77");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB-3");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB-1dot77");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB0");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 5){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB1");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text() == 6){
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB2");
		}else{
			$("#t_cel_ptt1 tr:eq("+index+") td:eq(4)").text("dB3");	
		}	
		//
		if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsSwitch").text() == 0){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsSwitch").text("关");
		}else{
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsSwitch").text("开");	
		}
		//
		if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 0){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("10");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 1){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("20");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 2){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("30");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 3){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("40");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 4){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("60");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 5){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("80");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 6){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("120");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 7){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("160");
		}else if($("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text() == 8){
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("320");
		}else{
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsInterval").text("640");	
		}
		//
		var u8PttSpsThrRbNum = $("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsThrRbNum").text();
		var u8PttSpsThrRbNumLit = accDiv(u8PttSpsThrRbNum,100);
		$("#t_cel_ptt1 tr:eq("+index+") td.u8PttSpsThrRbNum").text(u8PttSpsThrRbNumLit);
		//
		if($("#t_cel_ptt1 tr:eq("+index+") td.b8PttDlRbEnable").text() == 0){
			$("#t_cel_ptt1 tr:eq("+index+") td.b8PttDlRbEnable").text("关");
		}else{
			$("#t_cel_ptt1 tr:eq("+index+") td.b8PttDlRbEnable").text("开");	
		}
		//
		var u8PttPeriodPagingCycle = $("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text();
		switch(u8PttPeriodPagingCycle){
		case "0":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf2");
			break;
		case "1":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf4");
			break;
		case "2":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf8");
			break;
		case "3":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf216");
			break;
		case "4":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf32");
			break;
		case "5":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf64");
			break;
		case "6":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf128");
			break;
		case "7":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf256");
			break;
		case "8":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf512");
			break;
		case "9":
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf1024");
			break;
		default:
			$("#t_cel_ptt1 tr:eq("+index+") td.u8PttPeriodPagingCycle").text("rf512");
			break;
		}
	});	
	$("#t_cel_ptt1 td.u8TcchTransType").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("周期");
			break;
		case "1":
			$(this).text("绑定寻呼");
			break;
		}	
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7";
	});
	//全选
	$("#checkfather").live("click",function(){
		$("[name=checkson]:checkbox").attr("checked",this.checked);
	});
	$("[name=checkson]:checkbox").live("click",function(){
		var flag=true;
		$("[name=checkson]:checkbox").each(function(){
			if(!this.checked){
				flag=false;
			}
		});
		$("#checkfather").attr("checked",flag);
	});
	//跳转到配置
	$("#t_cel_ptt1 tr").each(function(index){
		$("#t_cel_ptt1 tr:eq("+index+") td:eq(19)").click(function(){
			var u8CId = $("#t_cel_ptt1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_ptt1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_ptt1 tr:eq("+index+") th:eq(1)").click(function(){
				
				var u8CId = $("#t_cel_ptt1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7&parameters="+para+"";
			});	
		}
						   
	});
	//删除
	$("#t_cel_ptt1 tr").each(function(index){
		$("#t_cel_ptt1 tr:eq("+index+") td:eq(20)").click(function(){
			var u8CId = $("#t_cel_ptt1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_ptt1 input[type=checkbox]").each(function(index){
			if($("#t_cel_ptt1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_ptt1 tr:eq("+index+") td:eq(0)").text();
				str.push(temp);
			}
		});	
		for(var i=0;i<str.length;i++){
			if(str[i]== "" || str[i]== null){
				str.splice(i,1);
			}
		}
		var para = "";
		for(var i=0;i<str.length;i++){
			var s = "u8CId"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PTT-3.0.7";
	});

	$("#u8PttBPagingCycle").change(function(){
		if($(this).val() != 6){
			$("#u8PttBPagingFN_Max").html(accSub(Math.pow(2,parseInt((parseInt($(this).val()) + parseInt(1)))),1));
		}else{
			$("#u8PttBPagingFN_Max").html("127");
		}
	});
	
	
});
//
function accAdd(arg1,arg2){
	var r1,r2,m;
	try{
		r1 = arg1.toString().split(".")[1].length;	
	}catch(e){
		r1 = 0;	
	}
	try{
		r2 = arg2.toString().split(".")[1].length;	
	}catch(e){
		r2 = 0;	
	}
	m = Math.pow(10,Math.max(r1,r2));
	return (arg1*m + arg2*m)/m;
}
function accSub(arg1,arg2){
	var r1,r2,m,n;
	try{
		r1 = arg1.toString().split(".")[1].length;	
	}catch(e){
		r1 = 0;	
	}
	try{
		r2 = arg2.toString().split(".")[1].length;	
	}catch(e){
		r2 = 0;	
	}
	m = Math.pow(10,Math.max(r1,r2));
	n = (r1>=r2)?r1:r2;
	return ((arg1*m - arg2*m)/m).toFixed(n);
}
function accMul(arg1,arg2){
	var m = 0;
	var s1 = arg1.toString();
	var s2 = arg2.toString();
	try{
		m += s1.split(".")[1].length	
	}catch(e){}
	try{
		m += s2.split(".")[1].length	
	}catch(e){}
	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
}
function accDiv(arg1,arg2){
	var t1 = 0;
	var t2 = 0;
	var r1,r2;
	try{
		t1 = arg1.toString().split(".")[1].length;
	}catch(e){}
	try{
		t2 = arg2.toString().split(".")[1].length;	
	}catch(e){}
	with(Math){
		r1 = Number(arg1.toString().replace(".",""));
		r2 = Number(arg2.toString().replace(".",""));
		return (r1/r2)*pow(10,t2-t1);
	}
}