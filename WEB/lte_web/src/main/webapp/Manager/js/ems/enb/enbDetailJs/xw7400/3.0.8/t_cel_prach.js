$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if($("#u8NumberOfRaPreambles").val() < $("#u8SizeOfRaPreamblesGroupA").val()){
			index++;
			$("#u8NumberOfRaPreamblesError").text("/* 不可小于组A中前导码个数 */");
		}else{
			$("#u8NumberOfRaPreamblesError").text("");
		}
		if(!(isNum.test($("#u8PrachCfgIndex").val()) && $("#u8PrachCfgIndex").val()<=57 && $("#u8PrachCfgIndex").val()>=0)){
			$("#u8PrachCfgIndexError").text("/* 请输入0~57之间的整数 */");
			index++;
		}else{
			$("#u8PrachCfgIndexError").text("");
		}
		if(!(isNum.test($("#u16RootSeqIndex").val()) && $("#u16RootSeqIndex").val()<=837 && $("#u16RootSeqIndex").val()>=0)){
			$("#u16RootSeqIndexError").text("/* 请输入0~837之间的整数 */");
			index++;
		}else{
			$("#u16RootSeqIndexError").text("");
		}
		if(!(isNum.test($("#u8PrachFreqOffset").val()) && $("#u8PrachFreqOffset").val()<=94 && $("#u8PrachFreqOffset").val()>=0)){
			$("#u8PrachFreqOffsetError").text("/* 请输入0~94之间的整数 */");
			index++;
		}else{
			$("#u8PrachFreqOffsetError").text("");
		}
		if(!(isNum.test($("#u16PreambleLifeTime").val()) && $("#u16PreambleLifeTime").val()<=2000 && $("#u16PreambleLifeTime").val()>=100)){
			$("#u16PreambleLifeTimeError").text("/* 请输入100~2000之间的整数 */");
			index++;
		}else{
			$("#u16PreambleLifeTimeError").text("");
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8NumberOfRaPreambles"+"="+$("#u8NumberOfRaPreambles").val()+";"
				+"u8SizeOfRaPreamblesGroupA"+"="+$("#u8SizeOfRaPreamblesGroupA").val()+";"
				+"u8MsgPwrOffsetGrpB"+"="+$("#u8MsgPwrOffsetGrpB").val()+";"
				+"u8MaxPreambleTranCt"+"="+$("#u8MaxPreambleTranCt").val()+";"
				+"u8RachRspWndSize"+"="+$("#u8RachRspWndSize").val()+";"
				+"u8MacConTentionTimer"+"="+$("#u8MacConTentionTimer").val()+";"
				+"u8MaxMsg3TranCt"+"="+$("#u8MaxMsg3TranCt").val()+";"
				+"u8PrachCfgIndex"+"="+$("#u8PrachCfgIndex").val()+";"
				+"u8HighSpeedFlag"+"="+$("input[name='u8HighSpeedFlag']:checked").val()+";"
				+"u16RootSeqIndex"+"="+$("#u16RootSeqIndex").val()+";"
				+"u8NcsCfg"+"="+$("#u8NcsCfg").val()+";"
				+"u8PrachFreqOffset"+"="+$("#u8PrachFreqOffset").val()+";"
				+"u8SelPreGrpThresh"+"="+$("#u8SelPreGrpThresh").val()+";"
				+"u8PrachPwrStep"+"="+$("#u8PrachPwrStep").val()+";"
				+"u8PreInitPwr"+"="+$("#u8PreInitPwr").val()+";"
				+"u16PreambleLifeTime"+"="+$("#u16PreambleLifeTime").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8";
					}				
				}
			});	
		}
	});
	//内存值转为显示值
	//u32Status
	$("#t_cel_prach1 tr").each(function(index){
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("4");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("8");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("12");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("16");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("20");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("24");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("28");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 7){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("32");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 8){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("36");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 9){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("40");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 10){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("44");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 11){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("48");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 12){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("52");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 13){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("56");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text() == 14){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("60");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(1)").text("64");
		}	
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("4");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("8");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("12");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("16");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("20");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("24");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("28");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 7){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("32");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 8){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("36");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 9){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("40");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 10){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("44");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 11){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("48");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 12){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("52");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text() == 13){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("56");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(2)").text("60");
		}	
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("负无穷");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("0");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("5");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("8");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("10");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("12");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("15");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(3)").text("18");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("3");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("4");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("5");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("6");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("7");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("8");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("10");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 7){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("20");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 8){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("50");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text() == 9){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("100");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(4)").text("200");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("2");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("3");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("4");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("5");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("6");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("7");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("8");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(5)").text("10");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("8");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("16");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("24");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("32");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("40");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("48");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("56");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(6)").text("64");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(9)").text("否");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(9)").text("是");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text("56");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text("144");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text("208");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(13)").text("256");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text("0");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text("2");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text("4");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(14)").text("6");
		}
		//
		if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 0){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-120");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 1){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-118");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 2){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-116");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 3){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-114");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 4){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-112");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 5){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-110");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 6){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-108");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 7){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-106");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 8){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-104");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 9){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-102");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 10){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-100");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 11){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-98");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 12){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-96");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 13){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-94");
		}else if($("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text() == 14){
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-92");
		}else{
			$("#t_cel_prach1 tr:eq("+index+") td:eq(15)").text("-90");
		}	
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8";
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
	$("#t_cel_prach1 tr").each(function(index){
		$("#t_cel_prach1 tr:eq("+index+") td:eq(17)").click(function(){
			var u8CId = $("#t_cel_prach1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_prach1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_prach1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_prach1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8&parameters="+para+"";
			});		
		}
					   
	});	
	//删除
	$("#t_cel_prach1 tr").each(function(index){
		$("#t_cel_prach1 tr:eq("+index+") td:eq(18)").click(function(){
			var u8CId = $("#t_cel_prach1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_prach1 input[type=checkbox]").each(function(index){
			if($("#t_cel_prach1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_prach1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PRACH-3.0.8";
	});
});
//查询空闲物理小区标识并填入dom
function getFreeU16RootSeqIndex(){
	var moId = $("#moId").val();
	var enbVersion = $("#enbVersion").val();
	$.ajax({
		type : "post",
		url : "getFreeRsi.do",
		data:"enbVersion="+enbVersion
		+"&moId="+moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				$("#u16RootSeqIndex").val(data.message);
			}else{
				alert(data.error);
			}
		}
	});
}