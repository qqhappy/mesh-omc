$(function(){
	//表单校验
	var isNum=/^-?\d+$/;
	var isMac=/^[0-9A-Fa-f]{2}$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u32EnvMNO").val()) && $("#u32EnvMNO").val()<=255  && $("#u32EnvMNO").val()>=0)){
			$("#u32EnvMNOError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u32EnvMNOError").text("");
		} 
		var maxMax = parseInt($("#maxMax").html());
		var maxMin = parseInt($("#maxMin").html());
		var u32EnvMaxValue = parseInt($("#u32EnvMax").val());
		if(!(isNum.test(u32EnvMaxValue) && u32EnvMaxValue <= maxMin  && u32EnvMaxValue >= maxMax)){
			$("#u32EnvMaxError").text("/* 请输入"+maxMax+"~"+maxMin+"之间的整数 */");
			index++;
		}else{
			$("#u32EnvMaxError").text("");
		}
		if(!(isNum.test($("#u32EnvMin").val()) && $("#u32EnvMin").val() <= 4294967295 && $("#u32EnvMin").val() >=0 )){
			$("#u32EnvMinError").text("/* 请输入0~4294967295之间的整数 */");
			index++;
		}else if(parseInt($("#u32EnvMin").val()) > parseInt($("#u32EnvMax").val())){
			$("#u32EnvMinError").text("/* 监控最小值不可大于监控最大值 */");	
			index++;
		}else{
			$("#u32EnvMinError").text("");
		}
		if(!(isNum.test($("#u32Rsv").val()) && $("#u32Rsv").val() <= 4294967295 && $("#u32Rsv").val() >=0 )){
			$("#u32RsvError").text("/* 请输入0~4294967295之间的整数 */");
			index++;
		}else{
			$("#u32RsvError").text("");
		}
		
		if($("#u8RackNO option").length<1){
			$("#u8RackNOError").text("/* 没有可用的单板选项 */");
			index++;
		}else{
			$("#u8RackNOError").text("");
		}
		if($("#u8ShelfNO option").length<1){
			$("#u8ShelfNOError").text("/* 没有可用的机框号选项 */");
			index++;
		}else{
			$("#u8ShelfNOError").text("");
		}
		if($("#u8SlotNO option").length<1){
			$("#u8SlotNOError").text("/* 没有可用的槽位号选项 */");
			index++;
		}else{
			$("#u8SlotNOError").text("");
		}
		var para = "u32EnvMNO"+"="+$("#u32EnvMNO").val()+";"
				+"u8RackNO"+"="+$("#u8RackNO").val()+";"
				+"u8ShelfNO"+"="+$("#u8ShelfNO").val()+";"
				+"u8SlotNO"+"="+$("#u8SlotNO").val()+";"
				+"u32EnvMType"+"="+$("#u32EnvMType").val()+";"
				+"u32EnvMax"+"="+$("#u32EnvMax").val()+";"
				+"u32EnvMin"+"="+$("#u32EnvMin").val()+";"
				+"u32Rsv"+"="+$("#u32Rsv").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5";
					}				
				}
			});	
		
		}
		
	});
	$("#t_envmon tr").each(function(index){
		var value =  $("#t_envmon tr:eq("+index+") td:eq(1)").text();
		var html = "";
		if(parseInt(value) == 1){
			html = "BBU";
		}else if(parseInt(value) == 2){
			html = "RRU1";
		}else if(parseInt(value) == 3){
			html = "RRU2";
		}else{
			html = "RRU3";
		}
		$("#t_envmon tr:eq("+index+") td:eq(1)").text(html);
		//u8BDType
		if($("#t_envmon tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_envmon tr:eq("+index+") td:eq(4)").text("温度(RRU或BBU)");
		}else if($("#t_envmon tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_envmon tr:eq("+index+") td:eq(4)").text("功放温度");
		}else{
			$("#t_envmon tr:eq("+index+") td:eq(4)").text("驻波比");
		}	
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5";
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
	$("#t_envmon tr").each(function(index){
		$("#t_envmon tr:eq("+index+") td:eq(8)").click(function(){
			var u32EnvMNO = $("#t_envmon tr:eq("+index+") td:eq(0)").text();
			var para = "u32EnvMNO"+"="+u32EnvMNO;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5&referTable=T_BOARD&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_envmon tr").each(function(index){
		if(index != 0){
			$("#t_envmon tr:eq("+index+") th:eq(1)").click(function(){
				var u32EnvMNO = $("#t_envmon tr:eq("+index+") td:eq(0)").text();
				var para = "u32EnvMNO"+"="+u32EnvMNO;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5&referTable=T_BOARD&parameters="+para+"";
			});		
		}
					   
	});	
	//删除
	$("#t_envmon tr").each(function(index){
		$("#t_envmon tr:eq("+index+") td:eq(9)").click(function(){
			var u32EnvMNO = $("#t_envmon tr:eq("+index+") td:eq(0)").text();
			var para = "u32EnvMNO"+"="+u32EnvMNO;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_envmon input[type=checkbox]").each(function(index){
			if($("#t_envmon input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_envmon tr:eq("+index+") td:eq(0)").text();
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
			var s = "u32EnvMNO"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5&parameters="+para+"";
			}
		}	
	});
	$("#u32EnvMType").change(function(){
		var type = $(this).val();
		if(type == 3){
			$("#maxMax").html("0");
			$("#maxMin").html("5");
			$("#u32EnvMax").val("3");
			
		}else if(type == 2){
			$("#maxMax").html("-100");
			$("#maxMin").html("500");
			$("#u32EnvMax").val("100");
		}else{
			$("#maxMax").html("-100");
			$("#maxMin").html("500");
			$("#u32EnvMax").val("80");
		}
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENVMON-2.1.5";
	});
});

function getBoardType(){
	var rack = $("#u8RackNO").val();
	var shelf = $("#u8ShelfNO").val();
	var slot = $("#u8SlotNO").val();
}