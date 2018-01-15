$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8CfgIdx").val()) && $("#u8CfgIdx").val()<=255  && $("#u8CfgIdx").val()>=0)){
			$("#u8CfgIdxError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}	
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8PeriodicPHRTimer"+"="+$("#u8PeriodicPHRTimer").val()+";"
				+"u8ProhibitPHRTimer"+"="+$("#u8ProhibitPHRTimer").val()+";"
				+"u8DlPathlossChange"+"="+$("#u8DlPathlossChange").val()+";"
				+"u8Ks"+"="+$("input[name='u8Ks']:checked").val()+";"
				+"u8PuschAccumulationEnable"+"="+$("#u8PuschAccumulationEnable").val()+";"
				+"u8PoUePuschDynamic"+"="+$("#u8PoUePuschDynamic").val()+";"
				+"u8PoUEPucchDynamic"+"="+$("#u8PoUEPucchDynamic").val()+";"
				+"u8PoUePuschPersistent"+"="+$("#u8PoUePuschPersistent").val()+";"
				+"u8PA"+"="+$("#u8PA").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5";
					}				
				}
			});	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_srvpc1 tr").each(function(index){
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("10");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("20");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("50");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("100");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("200");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("500");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("1000");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(1)").text("无穷大");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("0");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("10");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("20");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("50");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("100");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("200");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("500");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(2)").text("1000");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text("1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text("3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text("6");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(3)").text("无穷大");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(4)").text("0");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(4)").text("1.25");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(5)").text("Current Absolute");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(5)").text("Accumulation");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-8");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-7");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-6");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 7){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("-1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 8){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("0");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 9){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 10){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 11){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 12){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 13){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text() == 14){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("6");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(6)").text("7");
		}
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-8");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-7");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-6");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 7){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("-1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 8){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("0");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 9){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 10){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 11){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 12){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 13){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text() == 14){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("6");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(7)").text("7");
		}
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-8");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-7");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-6");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 7){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("-1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 8){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("0");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 9){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 10){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("2");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 11){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 12){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("4");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 13){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("5");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text() == 14){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("6");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(8)").text("7");
		}
		//
		if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("-6");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 1){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("-4.77");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 2){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("-3");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 3){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("-1.77");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 4){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("0");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 5){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("1");
		}else if($("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text() == 6){
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("2");
		}else{
			$("#t_enb_srvpc1 tr:eq("+index+") td:eq(9)").text("3");
		}					  
	});	
	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5";
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
	$("#t_enb_srvpc1 tr").each(function(index){
		$("#t_enb_srvpc1 tr:eq("+index+") td:eq(10)").click(function(){
			var u8CfgIdx = $("#t_enb_srvpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_srvpc1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_srvpc1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_srvpc1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_srvpc1 tr").each(function(index){
		$("#t_enb_srvpc1 tr:eq("+index+") td:eq(11)").click(function(){
			var u8CfgIdx = $("#t_enb_srvpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_srvpc1 input[type=checkbox]").each(function(index){
			if($("#t_enb_srvpc1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_srvpc1 tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8CfgIdx"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVPC-2.1.5";
	});
});