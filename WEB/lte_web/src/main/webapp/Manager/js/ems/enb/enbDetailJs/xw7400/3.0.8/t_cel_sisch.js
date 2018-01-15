$(function(){
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
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8SIId"+"="+$("#u8SIId").val()+";"
				+"u8Periodicity"+"="+$("#u8Periodicity").val()+";"
				+"u8Sib2"+"="+$("input[name='u8Sib2']:checked").val()+";"
				+"u8Sib3"+"="+$("input[name='u8Sib3']:checked").val()+";"
				+"u8Sib4"+"="+$("input[name='u8Sib4']:checked").val()+";"
				+"u8Sib5"+"="+$("input[name='u8Sib5']:checked").val()+";"
				+"u8SibPtt"+"="+$("input[name='u8SibPtt']:checked").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8";
					}				
				}
			});	
		}
		
	});
	$("#t_cel_sisch tr").each(function(index){

		//					   
		if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("8");
		}else if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("16");
		}else if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("32");
		}else if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("64");
		}else if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("128");
		}else if($("#t_cel_sisch tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("256");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(2)").text("512");
		}	
		//					   
		if($("#t_cel_sisch tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(3)").text("否");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(3)").text("是");
		}	
		//					   
		if($("#t_cel_sisch tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(4)").text("否");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(4)").text("是");
		}
		//
		if($("#t_cel_sisch tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(5)").text("否");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(5)").text("是");
		}
		//
		if($("#t_cel_sisch tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(6)").text("否");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(6)").text("是");
		}
		//
		if($("#t_cel_sisch tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_sisch tr:eq("+index+") td:eq(7)").text("否");
		}else{
			$("#t_cel_sisch tr:eq("+index+") td:eq(7)").text("是");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8";
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
	$("#t_cel_sisch tr").each(function(index){
		$("#t_cel_sisch tr:eq("+index+") td:eq(8)").click(function(){
			var u8CId = $("#t_cel_sisch tr:eq("+index+") td:eq(0)").text();
			var u8SIId = $("#t_cel_sisch tr:eq("+index+") td:eq(1)").text();
			var para = "u8CId"+"="+u8CId+";"
							+"u8SIId"+"="+u8SIId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_sisch tr").each(function(index){
		if(index != 0){
			$("#t_cel_sisch tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_sisch tr:eq("+index+") td:eq(0)").text();
				var u8SIId = $("#t_cel_sisch tr:eq("+index+") td:eq(1)").text();
				var para = "u8CId"+"="+u8CId+";"
								+"u8SIId"+"="+u8SIId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_cel_sisch tr").each(function(index){
		$("#t_cel_sisch tr:eq("+index+") td:eq(9)").click(function(){
			var u8CId = $("#t_cel_sisch tr:eq("+index+") td:eq(0)").text();
			var u8SIId = $("#t_cel_sisch tr:eq("+index+") td:eq(1)").text();
			var para = "u8CId"+"="+u8CId+";"
							+"u8SIId"+"="+u8SIId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str1=[];var str2=[];
		$("#t_cel_sisch input[type=checkbox]").each(function(index){
			if($("#t_cel_sisch input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#t_cel_sisch tr:eq("+index+") td:eq(0)").text();
				var temp2 = $("#t_cel_sisch tr:eq("+index+") td:eq(1)").text();
				str1.push(temp1);
				str2.push(temp2);
			}
		});	
		for(var i=0;i<str1.length;i++){
			if(str1[i]== "" || str1[i]== null){
				str1.splice(i,1);
			}
		}
		for(var i=0;i<str2.length;i++){
			if(str2[i]== "" || str2[i]== null){
				str2.splice(i,1);
			}
		}
		var para = "";
		for(var i=0;i<str1.length;i++){
			var s = "u8CId"+"="+str1[i]+","
						+"u8SIId"+"="+str2[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8";
	});	
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_SISCH-3.0.8";
	});	
});