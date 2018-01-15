$(function(){
	//表单校验
	var isNum=/^\d+$/;
	var isIp=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)$/;
	$("#submit_add").click(function(){
									
		var au8OmcServerIP1 = $("#au8OmcServerIP1").val();
		var s = au8OmcServerIP1.split(".");
		var s1 = parseInt(s[0]).toString(16);
		if(s1.length<2){
			s1 = "0" + s1;	
		}
		var s2 = parseInt(s[1]).toString(16);
		if(s2.length<2){
			s2 = "0" + s2;	
		}
		var s3 = parseInt(s[2]).toString(16);
		if(s3.length<2){
			s3 = "0" + s3;	
		}
		var s4 = parseInt(s[3]).toString(16);
		if(s4.length<2){
			s4 = "0" + s4;	
		}		
		var sr = s1+s2+s3+s4;
		$("#au8OmcServerIP").val(sr);								
									
									
		var index = 0;
		if(!(isNum.test($("#u8OmcID").val()) && $("#u8OmcID").val()<=255  && $("#u8OmcID").val()>=1)){
			$("#u8OmcIDError").text("/* 请输入1~255之间的整数 */");
			index++;
		}else{
			$("#u8OmcIDError").text("");
		}
		if(!(isNum.test($("#u16SrcPort").val()) && $("#u16SrcPort").val()<=65535  && $("#u16SrcPort").val()>=0)){
			$("#u16SrcPortError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16SrcPortError").text("");
		}
		if(!(isNum.test($("#u16DstPort").val()) && $("#u16DstPort").val()<=65535  && $("#u16DstPort").val()>=0)){
			$("#u16DstPortError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16DstPortError").text("");
		}
		if(!(isIp.test($("#au8OmcServerIP1").val()))){
			$("#au8OmcServerIP1Error").text("/* 请输入正确的服务器地址 */");	
			index++;
		}else if(sr == "FFFFFFFF" || sr == "ffffffff" || sr == "00000000"){
			$("#au8OmcServerIP1Error").text("/* 请输入正确的服务器地址 */");		
			index++;	
		}else{
			$("#au8OmcServerIP1Error").text("");	
		}
		if($("#u8eNBIPID option").length<1){
			$("#u8eNBIPIDError").text("/* 没有可用的IP标识选项 */");
			index++;
		}else{
			$("#u8eNBIPIDError").text("");
		}
		var para = "u8OmcID"+"="+$("#u8OmcID").val()+";"
				+"u8eNBIPID"+"="+$("#u8eNBIPID").val()+";"
				+"au8OmcServerIP"+"="+$("#au8OmcServerIP").val()+";"
				+"u16SrcPort"+"="+$("#u16SrcPort").val()+";"
				+"u16DstPort"+"="+$("#u16DstPort").val()+";"
				+"u8Qos"+"="+$("#u8Qos").val()+";"
				+"u32Status=1";
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
						
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&isActiveEnbTable="+isActiveEnbTable+"";
					}				
				}
			});	
		
		}
		
	});
	//内存值转为显示值
	$("#t_omc tr").each(function(index){
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var ipId = $("#t_omc tr:eq("+index+") td.u8eNBIPID").text();	
		$("#t_omc tr:eq("+index+") td.u8eNBIPID").text(ipId+" ("+queryIpDetail(basePath,ipId,moId)+")");
				
		//u32Status
		if($("#t_omc tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_omc tr:eq("+index+") td:eq(6)").text("正常");
		}else{
			$("#t_omc tr:eq("+index+") td:eq(6)").text("不正常");
		}				   
		
		var au8OmcServerIP = $("#t_omc tr:eq("+index+") td:eq(2)").text().split("");	
		var str1 = au8OmcServerIP[0] + au8OmcServerIP[1] ;
		var str2 = au8OmcServerIP[2] + au8OmcServerIP[3] ;
		var str3 = au8OmcServerIP[4] + au8OmcServerIP[5] ;
		var str4 = au8OmcServerIP[6] + au8OmcServerIP[7] ;
		var no1 = parseInt(str1,16).toString(10);
		var no2 = parseInt(str2,16).toString(10);
		var no3 = parseInt(str3,16).toString(10);
		var no4 = parseInt(str4,16).toString(10);
		var result1 = no1 + "." + no2 + "." + no3 + "." + no4;		
		$("#t_omc tr:eq("+index+") td:eq(2)").text(result1);		
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&isActiveEnbTable="+isActiveEnbTable+"";
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
	$("#t_omc tr").each(function(index){
		$("#t_omc tr:eq("+index+") td:eq(7)").click(function(){
			var u8OmcID = $("#t_omc tr:eq("+index+") td:eq(0)").text();
			var para = "u8OmcID"+"="+u8OmcID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&referTable=T_IPV4&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
		});					   
	});	
	//跳转到配置
	$("#t_omc tr").each(function(index){
		if(index != 0){
			$("#t_omc tr:eq("+index+") th:eq(1)").click(function(){
				var u8OmcID = $("#t_omc tr:eq("+index+") td:eq(0)").text();
				var para = "u8OmcID"+"="+u8OmcID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				var isActiveEnbTable = $("#isActiveEnbTable").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&referTable=T_IPV4&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			});		
		}
					   
	});
	//删除
	$("#t_omc tr").each(function(index){
		$("#t_omc tr:eq("+index+") td:eq(8)").click(function(){
			var u8OmcID = $("#t_omc tr:eq("+index+") td:eq(0)").text();
			var para = "u8OmcID"+"="+u8OmcID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_omc input[type=checkbox]").each(function(index){
			if($("#t_omc input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_omc tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8OmcID"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_OMC-1.1.5&isActiveEnbTable="+isActiveEnbTable+"";
	});
});