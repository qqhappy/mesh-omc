$(function(){
	
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if($("#u8CfgIdx").val() != 1){
			$("#u8CfgIdxError").text("/* 此索引号只可为1 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8T310"+"="+$("#u8T310").val()+";"
				+"u8T311"+"="+$("#u8T311").val()+";"
				+"u8T300"+"="+$("#u8T300").val()+";"
				+"u8T301"+"="+$("#u8T301").val()+";"
				+"u8T302"+"="+$("#u8T302").val()+";"
				+"u8T304"+"="+$("#u8T304").val()+";"
				+"u8T320"+"="+$("#u8T320").val()+";"
				+"u8N310"+"="+$("#u8N310").val()+";"
				+"u8N311"+"="+$("#u8N311").val();
		$("#parameters").val(para);
		if(index==0){
			
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_uepara1 tr").each(function(index){
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("0");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("50");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("100");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("200");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("500");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("1000");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(1)").text("2000");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("1000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("3000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("5000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("10000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("15000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("20000");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(2)").text("30000");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("100");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("200");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("300");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("400");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("600");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("1000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("1500");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(3)").text("2000");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("100");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("200");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("300");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("400");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("600");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("1000");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text() == 6){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("1500");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(4)").text("2000");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("50");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("100");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("150");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("200");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("500");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("1000");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(6)").text("2000");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("5");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("10");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("20");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("30");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("60");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("120");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(7)").text("180");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("1");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("2");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("3");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("4");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("6");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("8");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text() == 6){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("10");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(8)").text("20");
		}
		//
		if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("1");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 1){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("2");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 2){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("3");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 3){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("4");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 4){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("5");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 5){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("6");
		}else if($("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text() == 6){
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("8");
		}else{
			$("#t_enb_uepara1 tr:eq("+index+") td:eq(9)").text("10");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8";
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
	$("#t_enb_uepara1 tr").each(function(index){
		$("#t_enb_uepara1 tr:eq("+index+") td:eq(10)").click(function(){
			var u8CfgIdx = $("#t_enb_uepara1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_uepara1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_uepara1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_uepara1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_uepara1 tr").each(function(index){
		$("#t_enb_uepara1 tr:eq("+index+") td:eq(11)").click(function(){
			var u8CfgIdx = $("#t_enb_uepara1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_uepara1 input[type=checkbox]").each(function(index){
			if($("#t_enb_uepara1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_uepara1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_UEPARA-3.0.8";
	});
});