$(function(){
	
	//表单校验
	var isNum=/^\d+$/;
	var isMac=/^[0-9A-Fa-f]{2}$/;
	$("#submit_add").click(function(){
		
		
		
	
		//转移值
		var au8Mac1 = $("#au8Mac1").val()+"";
		var au8Mac2 = $("#au8Mac2").val()+"";
		var au8Mac3 = $("#au8Mac3").val()+"";
		var au8Mac4 = $("#au8Mac4").val()+"";
		var au8Mac5 = $("#au8Mac5").val()+"";
		var au8Mac6 = $("#au8Mac6").val()+"";
		
		var sr = au8Mac1+au8Mac2+au8Mac3+au8Mac4+au8Mac5+au8Mac6;
		$("#au8Mac").val(sr);
			
		var index = 0;
		if(!(isNum.test($("#u8PortID").val()) && $("#u8PortID").val()<=255  && $("#u8PortID").val()>=1)){
			$("#u8PortIDError").text("/* 请输入 1~255之间的整数 */");
			index++;
		}else{
			$("#u8PortIDError").text("");
		}
		if(!(isNum.test($("#u32RsvBdWidth").val()) && $("#u32RsvBdWidth").val()<=65535 && $("#u32RsvBdWidth").val()>=0)){
			$("#u32RsvBdWidthError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u32RsvBdWidthError").text("");
		}
		if(!(isNum.test($("#u32FdBdWidth").val()) && $("#u32FdBdWidth").val()<=65535  && $("#u32FdBdWidth").val()>=0)){
			$("#u32FdBdWidthError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u32FdBdWidthError").text("");
		}
		if($("#u8RackNO option").length<1){
			$("#u8RackNOError").text("/* 没有可用的机架号选项 */");
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
		if(!(isNum.test($("#u8EthPort").val()) && $("#u8EthPort").val()<=6  && $("#u8EthPort").val()>=0)){
			$("#u8EthPortError").text("/* 请输入0~6之间的整数 */");
			index++;
		}else{
			$("#u8EthPortError").text("");
		}
		if(!(isMac.test($("#au8Mac1").val())&&isMac.test($("#au8Mac2").val())&&isMac.test($("#au8Mac3").val())&&isMac.test($("#au8Mac4").val())&&isMac.test($("#au8Mac5").val())&&isMac.test($("#au8Mac6").val()))){
			$("#au8Mac1Error").text("/* 请输入正确的物理地址 */");	
			index++;
		}else{
			$("#au8Mac1Error").text("");	
		}
		var u8ElecWorkMode = $("#u8ElecWorkMode").val();
		if(u8ElecWorkMode == null || typeof(u8ElecWorkMode) == "undefined"){
			u8ElecWorkMode = 0;
		}
		var u8OptiWorkMode = $("#u8OptiWorkMode").val();
		if(u8OptiWorkMode == null || typeof(u8OptiWorkMode) == "undefined"){
			u8OptiWorkMode = 0;
		}
		var para = "u8PortID"+"="+$("#u8PortID").val()+";"
				+"u8RackNO"+"="+$("#u8RackNO").val()+";"
				+"u8ShelfNO"+"="+$("#u8ShelfNO").val()+";"
				+"u8SlotNO"+"="+$("#u8SlotNO").val()+";"
				+"u8EthPort"+"="+$("#u8EthPort").val()+";"
				+"u8PortWorkType"+"="+$("#u8PortWorkType").val()+";"
				+"u8ElecWorkMode"+"="+u8ElecWorkMode+";"
				+"u8OptiWorkMode"+"="+u8OptiWorkMode+";"
				+"u32RsvBdWidth"+"="+$("#u32RsvBdWidth").val()+";"
				+"u32FdBdWidth"+"="+$("#u32FdBdWidth").val()+";"
				+"au8Mac"+"="+$("#au8Mac").val()+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
		$("#parameters").val(para);
		
		var operType=$("input[name='operType']").val();
		if(operType=="config"){
			if($("#u8PortWorkType").val() != $("#u8PortWorkType_old").val()
					|| u8ElecWorkMode != $("#u8ElecWorkMode_old").val()
					|| u8OptiWorkMode != $("#u8OptiWorkMode_old").val()
					|| $("input[name='u8ManualOP']:checked").val() != $("#u8ManualOP_old").val()
				){
				alert("修改后需要重启单板才可生效");
			}
		}
		
		if(index==0){
			var moId=$("#moId").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&isActiveEnbTable="+isActiveEnbTable+"";
					}				
				}
			});	
		}
	});
	
	//内存值转为显示值
	$("#t_ethpara tr").each(function(index){
		
		//u8ManualOP
		if($("#t_ethpara tr:eq("+index+") td.u8ManualOP").text() == 0){
			$("#t_ethpara tr:eq("+index+") td.u8ManualOP").text("解闭塞");
		}else{
			$("#t_ethpara tr:eq("+index+") td.u8ManualOP").text("闭塞");
		}	
		//u32Status
		if($("#t_ethpara tr:eq("+index+") td.u32Status").text() == 0){
			$("#t_ethpara tr:eq("+index+") td.u32Status").text("正常");
		}else{
			$("#t_ethpara tr:eq("+index+") td.u32Status").text("不正常");
		}
		//au8Mac//query
		var au8Mac = $("#t_ethpara tr:eq("+index+") td.au8Mac").text().split("");	
		var str1  = au8Mac[0] + au8Mac[1] + "";
		var str2 = au8Mac[2] + au8Mac[3] + "";
		var str3 = au8Mac[4] + au8Mac[5] + "";
		var str4 = au8Mac[6] + au8Mac[7] + "";
		var str5 = au8Mac[8] + au8Mac[9] + "";
		var str6 = au8Mac[10] + au8Mac[11] + "";
		var result = str1 + "-" + str2 + "-" + str3 + "-" + str4 + "-" + str5 + "-" + str6;
		$("#t_ethpara tr:eq("+index+") td.au8Mac").text(result);	
		
		
	});	
	$("#t_ethpara td.u8ElecWorkMode").each(function(){
		var u8PortWorkType = $.trim($(this).parents("tr:first").find(".u8PortWorkType").text());
		if(u8PortWorkType != 2){
			var value = $.trim($(this).text());
			switch (value){
			case "0":
				$(this).text("自协商");
				break;
			case "1":
				$(this).text("10M全双工");
				break;
			case "2":
				$(this).text("100M全双工");
				break;
			case "3":
				$(this).text("1000M全双工");
				break;
			}
		}else{
			$(this).text("");
		}
			
	});
	$("#t_ethpara td.u8OptiWorkMode").each(function(){
		var u8PortWorkType = $.trim($(this).parents("tr:first").find(".u8PortWorkType").text());
		if(u8PortWorkType != 1){
			var value = $.trim($(this).text());
			switch (value){
			case "0":
				$(this).text("1000M全双工");
				break;
			}	
		}else{
			$(this).text("");
		}		
	});
	$("#t_ethpara td.u8PortWorkType").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("自适应");
			break;
		case "1":
			$(this).text("电口");
			break;
		case "2":
			$(this).text("光口");
			break;
		}	
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&isActiveEnbTable="+isActiveEnbTable+"";
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
	$("#t_ethpara tr").each(function(index){
		$("#t_ethpara tr:eq("+index+") td:eq(13)").click(function(){
			var u8PortID = $("#t_ethpara tr:eq("+index+") td:eq(0)").text();
			var para = "u8PortID"+"="+u8PortID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&referTable=T_BOARD&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
		});					   
	});	
	//跳转到配置
	$("#t_ethpara tr").each(function(index){
		if(index != 0){
			$("#t_ethpara tr:eq("+index+") th:eq(1)").click(function(){
				var u8PortID = $("#t_ethpara tr:eq("+index+") td:eq(0)").text();
				var para = "u8PortID"+"="+u8PortID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var isActiveEnbTable = $("#isActiveEnbTable").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&referTable=T_BOARD&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			});	
		}
					   
	});
	
	//删除
	$("#t_ethpara tr").each(function(index){
		$("#t_ethpara tr:eq("+index+") td:eq(14)").click(function(){
			var u8PortID = $("#t_ethpara tr:eq("+index+") td:eq(0)").text();
			var para = "u8PortID"+"="+u8PortID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_ethpara input[type=checkbox]").each(function(index){
			if($("#t_ethpara input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_ethpara tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8PortID"+"="+str[i]+";" ;
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ETHPARA-3.0.7&isActiveEnbTable="+isActiveEnbTable+"";
	});
});
function add_u8ElecWorkMode(){
	$(".u8ElecWorkMode").html(
			 '<td>电口配置 :</td>'
			+'<td class="blankTd"></td>'
			+'<td>'
			+'	<div style="border:1px solid #C6D7E7;overflow:hidden;width:251px;">'
			+'	<select  style="width:251px;border:1px solid #fff" id="u8ElecWorkMode">		'							
			+'		<option value="0">自协商</option>'
			+'		<option value="1">10M全双工</option>'
			+'		<option value="2">100M全双工</option>'
			+'		<option value="3">1000M全双工</option>'
			+'	</select></div>'
			+'</td>'
			+'<td id="u8ElecWorkModeError" class="error"></td>	'
	);
}
function add_u8OptiWorkMode(){
	$(".u8OptiWorkMode").html(
			'<td>光口配置 :</td>'
			+'<td class="blankTd"></td>'
			+'<td>'
			+'	<div style="border:1px solid #C6D7E7;overflow:hidden;width:251px;">'
			+'	<select  style="width:251px;border:1px solid #fff" id="u8OptiWorkMode">'
			+'		<option value="0">1000M全双工</option>'
			+'	</select></div>'
			+'</td>'
			+'<td id="u8OptiWorkModeError" class="error"></td>'
	);
}
function sub_u8ElecWorkMode(){
	$(".u8ElecWorkMode").html("");
}
function sub_u8OptiWorkMode(){
	$(".u8OptiWorkMode").html("");
}









