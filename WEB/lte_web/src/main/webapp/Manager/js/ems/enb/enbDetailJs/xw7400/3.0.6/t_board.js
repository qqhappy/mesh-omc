$(function(){
	//联动控制
	
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		$(".error").text("");
		$("#resultError").html("");
		var index = 0;
		var u8BDType = $("input[name=u8BDType]:checked").val();
		if(!(u8BDType == 1 || u8BDType == 2)){
			$("#u8BDTypeError").text("/* 请选择一个单板类型 */");
			index++;
		}		
		var para = "u8RackNO"+"="+$("#u8RackNO").val()+";"
				+"u8ShelfNO"+"="+$("#u8ShelfNO").val()+";"
				+"u8SlotNO"+"="+$("#u8SlotNO").val()+";"
				+"u8BDType"+"="+$("input[name='u8BDType']:checked").val()+";"
				+"u8RadioMode"+"="+$("input[name='u8RadioMode']:checked").val()+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
		$("#parameters").val(para);
		var fiberPort = $("#u8FiberPort").val();
		if(typeof(fiberPort) == "undefined" || fiberPort == null){
			fiberPort = "";
		}
		if(index==0){			
			var moId=$("#moId").val();
			var basePath=$("#basePath").val();
			var operType=$("input[name='operType']").val();
			var enbVersion = $("#enbVersion").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			$.ajax({
				type:"post",
				url:basePath+"/lte/checkBoardBizRules.do",
				data:"moId="+moId+
					"&browseTime="+getBrowseTime()+
					"&fiberPort="+fiberPort+
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
							$("#resultError").html("error: "+errorHtml);
						}
					}else{
						
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&isActiveEnbTable="+isActiveEnbTable;
					}				
				}
			});						
		}
		
	});
	//内存值转为显示值
	$("#t_board tr").each(function(index){
		//u8RackNO || u8FiberPort
		var u8RackNO = $("#t_board tr:eq("+index+") td.u8RackNO").text();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var u8FiberPort = queryU8FiberPortForBoard(basePath,u8RackNO,moId);
		if(u8RackNO == 1){
			$("#t_board tr:eq("+index+") td.u8RackNO").text("BBU");			
		}else if(u8RackNO == 2){
			$("#t_board tr:eq("+index+") td.u8RackNO").text("RRU1");
			$("#t_board tr:eq("+index+") td[info='u8FiberPort']").text(u8FiberPort);
		}else if(u8RackNO == 3){
			$("#t_board tr:eq("+index+") td.u8RackNO").text("RRU2");
			$("#t_board tr:eq("+index+") td[info='u8FiberPort']").text(u8FiberPort);
		}else{
			$("#t_board tr:eq("+index+") td.u8RackNO").text("RRU3");
			$("#t_board tr:eq("+index+") td[info='u8FiberPort']").text(u8FiberPort);
		}		
		//u8BDType
		if($("#t_board tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_board tr:eq("+index+") td:eq(3)").text("BBU");
		}else{
			$("#t_board tr:eq("+index+") td:eq(3)").text("RRU");
		}
		//u8RadioMode
		if($("#t_board tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_board tr:eq("+index+") td:eq(5)").text("TD-LTE");
		}else{
			$("#t_board tr:eq("+index+") td:eq(5)").text("McWiLL");
		}
		//u8ManualOP
		if($("#t_board tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_board tr:eq("+index+") td:eq(6)").text("解闭塞");
		}else{
			$("#t_board tr:eq("+index+") td:eq(6)").text("闭塞");
		}	
		//u32Status
		if($("#t_board tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_board tr:eq("+index+") td:eq(7)").text("正常");
		}else{
			$("#t_board tr:eq("+index+") td:eq(7)").text("不正常");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&isActiveEnbTable="+isActiveEnbTable+"";
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
	$("#t_board tr").each(function(index){
		$("#t_board tr:eq("+index+") td:eq(8)").click(function(){
			var u8RackNO = $("#t_board tr:eq("+index+") td.u8RackNO").attr("u8RackNO");
			var u8ShelfNO = $("#t_board tr:eq("+index+") td:eq(1)").text();
			var u8SlotNO = $("#t_board tr:eq("+index+") td:eq(2)").text();
			var para = "u8RackNO"+"="+u8RackNO+";"
					+"u8ShelfNO"+"="+u8ShelfNO+";"
					+"u8SlotNO"+"="+u8SlotNO;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
		});					   
	});	
	//跳转到配置
	$("#t_board tr").each(function(index){
		if(index != 0){
			$("#t_board tr:eq("+index+") th:eq(1)").click(function(){
				var u8RackNO = $("#t_board tr:eq("+index+") td.u8RackNO").attr("u8RackNO");
				var u8ShelfNO = $("#t_board tr:eq("+index+") td:eq(1)").text();
				var u8SlotNO = $("#t_board tr:eq("+index+") td:eq(2)").text();
				var para = "u8RackNO"+"="+u8RackNO+";"
						+"u8ShelfNO"+"="+u8ShelfNO+";"
						+"u8SlotNO"+"="+u8SlotNO;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				var isActiveEnbTable = $("#isActiveEnbTable").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			});	
		}						   
	});	
	//复位
	$("#t_board tr").each(function(index){
		$("#t_board tr:eq("+index+") td:eq(9)").click(function(){
			if(confirm("确定进行单板复位?")){
				var rackId = $("#t_board tr:eq("+index+") td.u8RackNO").attr("u8RackNO");
				var shelfId = $("#t_board tr:eq("+index+") td:eq(1)").text();
				var boardId = $("#t_board tr:eq("+index+") td:eq(2)").text();
				var moId = $("#moId").val();
				$.ajax({
					type:"post",
					url:"resetBoard.do",
					data:"moId="+moId+
					"&rackId="+rackId+
					"&shelfId="+shelfId+
					"&boardId="+boardId+
					"&browseTime="+getBrowseTime(),
					dataType:"json",
					async:false,
					success:function(data){
						var basePath = $("#basePath").val();
						if(!sessionsCheck(data,basePath)){
							return ;
						}
						if(data.error != ""){
							alert(data.error);
						}else{
							alert("单板复位请求下发成功！");
						}
						
					}
				});	
			}
		});					   
	});	
	//删除
	$("#t_board tr").each(function(index){
		$("#t_board tr:eq("+index+") td:eq(10)").click(function(){
			var u8RackNO = $("#t_board tr:eq("+index+") td.u8RackNO").attr("u8RackNO");
			var u8ShelfNO = $("#t_board tr:eq("+index+") td:eq(1)").text();
			var u8SlotNO = $("#t_board tr:eq("+index+") td:eq(2)").text();
			var para = "u8RackNO"+"="+u8RackNO+";"
					+"u8ShelfNO"+"="+u8ShelfNO+";"
					+"u8SlotNO"+"="+u8SlotNO;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			var enbVersion = $("#enbVersion").val();
			if(confirm("确定要删除该条记录?")){				
				$.ajax({
					type:"post",
					url:basePath+"/lte/checkBoardBizRules.do",
					data:"moId="+moId+
						"&browseTime="+getBrowseTime()+
						"&operType=delete"+
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
							$("#error").html("error: "+errorHtml);
						}else{
							window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&isActiveEnbTable="+isActiveEnbTable;
						}				
					}
				});	
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str1=[];
		var str2=[];
		var str3=[];
		$("#t_board input[type=checkbox]").each(function(index){
			if($("#t_board input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#t_board tr:eq("+index+") td.u8RackNO").attr("u8RackNO");
				var temp2 = $("#t_board tr:eq("+index+") td:eq(1)").text();
				var temp3 = $("#t_board tr:eq("+index+") td:eq(2)").text();
				str1.push(temp1);
				str2.push(temp2);
				str3.push(temp3);
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
		for(var i=0;i<str3.length;i++){
			if(str3[i]== "" || str3[i]== null){
				str3.splice(i,1);
			}
		}
		var para = "";
		for(var i=0;i<str1.length;i++){
			var s = "u8RackNO"+"="+str1[i]+","
						+"u8ShelfNO"+"="+str2[i]+","
						+"u8SlotNO"+"="+str3[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			var enbVersion = $("#enbVersion").val();
			if(confirm("确定要删除所有选择的记录?")){				
				$.ajax({
					type:"post",
					url:basePath+"/lte/checkBoardBizRules.do",
					data:"moId="+moId+
						"&browseTime="+getBrowseTime()+
						"&operType=multiDelete"+
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
							$("#error").html("error: "+errorHtml);
						}else{
							window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&isActiveEnbTable="+isActiveEnbTable;
						}				
					}
				});	
			}
		}
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_BOARD-3.0.6&isActiveEnbTable="+isActiveEnbTable+"";
	});
});