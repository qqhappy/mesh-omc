$(function(){
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var au8EEA1 = "0" + $("#au8EEA1").val();													
		var au8EEA2 = "0" + $("#au8EEA2").val();								
		var au8EEA3 = "0" + $("#au8EEA3").val();
		
		var au8EIA1 = "0" + $("#au8EIA1").val();													
		var au8EIA2 = "0" + $("#au8EIA2").val();								
		var au8EIA3 = "0" + $("#au8EIA3").val();
		
		$("#au8EEA").val(au8EEA1+au8EEA2+au8EEA3);
		$("#au8EIA").val(au8EIA1+au8EIA2+au8EIA3);
		
		var index = 0;
		if(!(isNum.test($("#u16EndTmpRNTI").val()) && $("#u16EndTmpRNTI").val()<=65523  && $("#u16EndTmpRNTI").val()>=10000)){
			$("#u16EndTmpRNTIError").text("/* 请输入10000~65523之间的整数 */");
			index++;
		}else{
			$("#u16EndTmpRNTIError").text("");
		}
//		//判断两两不等							
//		for(var i=1;i<4;i++){
//			for(var j=i+1;j<4;j++){
//				if($("#au8EEA"+i).val() == $("#au8EEA"+j).val()){
//					index++;
//					$("#au8EEAError"+i).text("/* 加密算法优先级不可重复 */");
//					$("#au8EEAError"+j).text("/* 加密算法优先级不可重复 */");
//					return ;
//				}else{
//					$("#au8EEAError"+i).text("");
//					$("#au8EEAError"+j).text("");
//				}
//			}
//			
//		}
//		//判断两两不等							
//		for(var i=1;i<4;i++){
//			for(var j=i+1;j<4;j++){
//				if($("#au8EIA"+i).val() == $("#au8EIA"+j).val()){
//					index++;
//					$("#au8EIAError"+i).text("/* 完整性保护算法优先级不可重复 */");
//					$("#au8EIAError"+j).text("/* 完整性保护算法优先级不可重复 */");
//					return ;
//				}else{
//					$("#au8EIAError"+i).text("");
//					$("#au8EIAError"+j).text("");
//				}
//			}
//			
//		}
		
		var enbId = $("#u32eNBId").val()+"";
		var u32eNBId = parseInt(enbId,16);
		var para = "u32eNBId"+"="+u32eNBId+";"
				+"au8eNBName"+"="+$("#au8eNBName").val()+";"
				+"au8EEA"+"="+$("#au8EEA").val()+";"
				+"au8EIA"+"="+$("#au8EIA").val()+";"
				+"u8PageDrxCyc"+"="+$("#u8PageDrxCyc").val()+";"
				+"u16EndTmpRNTI"+"="+$("#u16EndTmpRNTI").val()+";"
				+"u8PttEnable"+"="+$("input[name='u8PttEnable']:checked").val()+";"
				+"u8NeedPeerPttCfgRsp"+"="+$("#u8NeedPeerPttCfgRsp").val()+";"
				+"u8UserInactTimer"+"="+$("#u8UserInactTimer").val()+";"
				+"u8PttMode"+"="+$("#u8PttMode").val()+";"
				+"u8CapsSwitch"+"="+$("input[name='u8CapsSwitch']:checked").val();
		$("#parameters").val(para);
		if(index==0){
			var moId = $("#moId").val();
			var basePath=$("#basePath").val();
			var tableName=$("input[name='tableName']").val();
			var operType=$("input[name='operType']").val();
			var isActiveEnbTable=$("#isActiveEnbTable").val();
			var enbVersion = $("#enbVersion").val();
			$.ajax({
				type:"post",
				url:"queryAsyncEnbBiz.do",
				data:"moId="+moId+
					"&basePath="+basePath+
					""+"&browseTime="+getBrowseTime()+"&tableName="+tableName+
					"&operType="+operType+
					"&isActiveEnbTable="+isActiveEnbTable+
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
							if (errorHtml.length >30){
								var errorHtmlLow = errorHtml.substr(0,30) + "...";
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
						var enbHexId = $("#u32eNBId").val();
						var enbName = $("#au8eNBName").val();
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&enbHexId="+enbHexId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
					}				
				}
			});	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_para tr").each(function(index){
		
		var enbId = $("#t_enb_para tr:eq("+index+") td:eq(0)").text();
		var enbHexId = parseInt(enbId).toString(16) + "";
		var enbHexIdLength  = enbHexId.length;
		if(enbHexIdLength < 8){
			for(var i = 0 ; i < 8 - enbHexIdLength ; i++){
				enbHexId = "0"+enbHexId;
			}
		}
		$("#t_enb_para tr:eq("+index+") td:eq(0)").text(enbHexId);
		//u8PageDrxCyc
		if($("#t_enb_para tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_enb_para tr:eq("+index+") td:eq(4)").text("32");
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_enb_para tr:eq("+index+") td:eq(4)").text("64");
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_enb_para tr:eq("+index+") td:eq(4)").text("128");
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_enb_para tr:eq("+index+") td:eq(4)").text("256");
		}
		
		var au8EEA = $("#t_enb_para tr:eq("+index+") td:eq(2)").text().split("");
		$("#t_enb_para tr:eq("+index+") td:eq(2)").text("Snow3G - AES - 祖冲之算法 ("+au8EEA[1]+"-"+au8EEA[3]+"-"+au8EEA[5]+")");
		var au8EIA = $("#t_enb_para tr:eq("+index+") td:eq(3)").text().split("");
		$("#t_enb_para tr:eq("+index+") td:eq(3)").text("Snow3G - AES - 祖冲之算法 ("+au8EIA[1]+"-"+au8EIA[3]+"-"+au8EIA[5]+")");
		
		if($("#t_enb_para tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_enb_para tr:eq("+index+") td:eq(6)").text("否");
		}else{
			$("#t_enb_para tr:eq("+index+") td:eq(6)").text("是");	
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_enb_para tr:eq("+index+") td:eq(7)").text("Not Need");
		}else{
			$("#t_enb_para tr:eq("+index+") td:eq(7)").text("Need");
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("100ms");
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("500ms");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("1s");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 3){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("2s");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 4){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("5s");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 5){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("10s");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 6){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("30s");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 7){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("1min");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 8){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("5min");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 9){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("10min");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 10){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("30min");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 11){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("1h");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 12){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("2h");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 13){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("5h");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 14){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("10h");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 15){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("1day");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 16){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("2days");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 17){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("5days");	
		}else if($("#t_enb_para tr:eq("+index+") td:eq(8)").text() == 18){
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("10days");	
		}else{
			$("#t_enb_para tr:eq("+index+") td:eq(8)").text("30days");	
		}
		if($("#t_enb_para tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_enb_para tr:eq("+index+") td:eq(9)").text("Normal");
		}else if($("#t_enb_para tr:eq("+index+") td:eq(9)").text() == 1){
			$("#t_enb_para tr:eq("+index+") td:eq(9)").text("Single");	
		}else{
			$("#t_enb_para tr:eq("+index+") td:eq(9)").text("Auto");
		}
		if($("#t_enb_para tr:eq("+index+") td.u8CapsSwitch").text() == 0){
			$("#t_enb_para tr:eq("+index+") td.u8CapsSwitch").text("否");
		}else{
			$("#t_enb_para tr:eq("+index+") td.u8CapsSwitch").text("是");	
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var enbHexId = $("#enbHexId").val();
		var enbName = $("#enbName").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&enbHexId="+enbHexId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
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
	$("#t_enb_para tr").each(function(index){
		$("#t_enb_para tr:eq("+index+") td:eq(11)").click(function(){
			var u32eNBId = $("#enbHexId").val();
			var para = "u32eNBId"+"="+parseInt(u32eNBId,16);
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			var enbName = $("#enbName").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&parameters="+para+"&enbHexId="+u32eNBId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_para tr").each(function(index){
		if(index != 0){
			$("#t_enb_para tr:eq("+index+") th:eq(1)").click(function(){
				var u32eNBId = $("#enbHexId").val();
				var para = "u32eNBId"+"="+parseInt(u32eNBId,16);
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				var enbName = $("#enbName").val();
				var isActiveEnbTable = $("#isActiveEnbTable").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&parameters="+para+"&enbHexId="+u32eNBId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_para tr").each(function(index){
		$("#t_enb_para tr:eq("+index+") td:eq(12)").click(function(){
//			var u32eNBId = $("#enbHexId").val();
//			var para = "u32eNBId"+"="+u32eNBId;
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			var enbName = $("#enbName").val();
//			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
//				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&parameters="+para+"&enbHexId="+u32eNBId+"&enbName="+enbName+"";
//			}
			alert("此表不允许进行删除操作");
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
//		var str=[];
//		$("#t_enb_para input[type=checkbox]").each(function(index){
//			if($("#t_enb_para input[type=checkbox]:eq("+index+")").attr("checked")){
//				var temp = $("#t_enb_para tr:eq("+index+") td:eq(0)").text();
//				str.push(temp);
//			}
//		});	
//		for(var i=0;i<str.length;i++){
//			if(str[i]== "" || str[i]== null){
//				str.splice(i,1);
//			}
//		}
//		var para = "";
//		for(var i=0;i<str.length;i++){
//			var s = "u32eNBId"+"="+str[i]+";" ;
//			para += s;
//		}
//		if(str.length < 1){
//			alert("您并未选中任何记录...");
//		}else{
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
//				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&parameters="+para+"";
//			}
//		}	
		alert("此表不允许进行删除操作");
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var enbHexId = $("#u32eNBId").val();
		var enbName = $("#au8eNBName").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&enbHexId="+enbHexId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var enbHexId = $("#u32eNBId").val();
		var enbName = $("#au8eNBName").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PARA-3.0.6&enbHexId="+enbHexId+"&enbName="+enbName+"&isActiveEnbTable="+isActiveEnbTable+"";
	});
});