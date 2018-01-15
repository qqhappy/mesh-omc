$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8Indx").val()) && $("#u8Indx").val()<=99  && $("#u8Indx").val()>=0)){
			$("#u8IndxError").text("/* 请输入0~99之间的整数 */");
			index++;
		}else{ 
			$("#u8IndxError").text("");
		}
		if(!(isNum.test($("#u32AlarmID").val()) && $("#u32AlarmID").val()<=5000  && $("#u32AlarmID").val()>=1)){
			$("#u32AlarmIDError").text("/* 请输入1~5000之间的整数 */");
			index++;
		}else{
			$("#u32AlarmIDError").text("");
		}
		if(!(isNum.test($("#u8FaultCode").val()) && $("#u8FaultCode").val()<=255  && $("#u8FaultCode").val()>=0)){
			$("#u8FaultCodeError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8FaultCodeError").text("");
		}
		if(!(isNum.test($("#u32ReportDelayTime").val()) && $("#u32ReportDelayTime").val()<=31536000  && $("#u32ReportDelayTime").val()>=0)){
			$("#u32ReportDelayTimeError").text("/* 请输入0~31536000之间的整数 */");
			index++;
		}else{
			$("#u32ReportDelayTimeError").text("");
		}
		if(!(isNum.test($("#u32MaxJitterTimes").val()) && $("#u32MaxJitterTimes").val()<=31536000  && $("#u32MaxJitterTimes").val()>=1)){
			$("#u32MaxJitterTimesError").text("/* 请输入1~31536000之间的整数 */");
			index++;
		}else{
			$("#u32MaxJitterTimesError").text("");
		}
		if(!(isNum.test($("#u32RecoverDelayTime").val()) && $("#u32RecoverDelayTime").val()<=31536000  && $("#u32RecoverDelayTime").val()>=0)){
			$("#u32RecoverDelayTimeError").text("/* 请输入0~31536000之间的整数 */");
			index++;
		}else{
			$("#u32RecoverDelayTimeError").text("");
		}
		if(!(isNum.test($("#u32RestDelayTime").val()) && $("#u32RestDelayTime").val()<=31536000  && $("#u32RestDelayTime").val()>=0)){
			$("#u32RestDelayTimeError").text("/* 请输入0~31536000之间的整数 */");
			index++;
		}else{
			$("#u32RestDelayTimeError").text("");
		}
		if(!(isNum.test($("#u32DetIntervalTime").val()) && $("#u32DetIntervalTime").val()<=86400  && $("#u32DetIntervalTime").val()>=1)){
			$("#u32DetIntervalTimeError").text("/* 请输入1~86400之间的整数 */");
			index++;
		}else{
			$("#u32DetIntervalTimeError").text("");
		}
		var para = "u8Indx"+"="+$("#u8Indx").val()+";"
				+"u32AlarmID"+"="+$("#u32AlarmID").val()+";"
				+"u8FaultCode"+"="+$("#u8FaultCode").val()+";"
				+"u32ReportDelayTime"+"="+$("#u32ReportDelayTime").val()+";"
				+"u32MaxJitterTimes"+"="+$("#u32MaxJitterTimes").val()+";"
				+"u32RecoverDelayTime"+"="+$("#u32RecoverDelayTime").val()+";"
				+"u32RestDelayTime"+"="+$("#u32RestDelayTime").val()+";"
				+"u32DetIntervalTime"+"="+$("#u32DetIntervalTime").val();
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
								var errorHtmlLow = errorHtml.substr(0,15) + "...";
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7";
					}				
				}
			});	
		}
		
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7";
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
	$("#t_alarm_para tr").each(function(index){
		$("#t_alarm_para tr:eq("+index+") td:eq(8)").click(function(){
			var u8Indx = $("#t_alarm_para tr:eq("+index+") td:eq(0)").text();
			var para = "u8Indx"+"="+u8Indx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_alarm_para tr").each(function(index){
		if(index != 0){
			$("#t_alarm_para tr:eq("+index+") th:eq(1)").click(function(){
				var u8Indx = $("#t_alarm_para tr:eq("+index+") td:eq(0)").text();
				var para = "u8Indx"+"="+u8Indx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_alarm_para tr").each(function(index){
		$("#t_alarm_para tr:eq("+index+") td:eq(9)").click(function(){
			var u8Indx = $("#t_alarm_para tr:eq("+index+") td:eq(0)").text();
			var para = "u8Indx"+"="+u8Indx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_alarm_para input[type=checkbox]").each(function(index){
			if($("#t_alarm_para input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_alarm_para tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8Indx"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ALARM_PARA-3.0.7";
	});
});