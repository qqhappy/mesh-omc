$(function(){
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8CfgIdx").val()) && $("#u8CfgIdx").val()<=255  && $("#u8CfgIdx").val()>=0)){
			$("#u8CfgIdxError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8UlHarqMaxTxNum"+"="+$("#u8UlHarqMaxTxNum").val()+";"
				+"u8DlHarqMaxTxNum"+"="+$("#u8DlHarqMaxTxNum").val()+";"
				+"u8PrdBsrTimer"+"="+$("#u8PrdBsrTimer").val()+";"
				+"u8RetxBsrTimer"+"="+$("#u8RetxBsrTimer").val()+";"
				+"u8BucketSizeDura"+"="+$("#u8BucketSizeDura").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	$("#t_enb_srvmac1 tr").each(function(index){

		//					   
		if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("1");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("2");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("3");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("4");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("5");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("6");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 6){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("7");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 7){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("8");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 8){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("10");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 9){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("12");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 10){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("16");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 11){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("20");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text() == 12){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("24");
		}else{
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(1)").text("28");
		}	
		//					   
		if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("1");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("2");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("3");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("4");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("5");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("6");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 6){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("7");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 7){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("8");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 8){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("10");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 9){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("12");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 10){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("16");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 11){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("20");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text() == 12){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("24");
		}else{
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(2)").text("28");
		}	
		//					   
		if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("5");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("10");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("16");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("20");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("32");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("40");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("64");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 7){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("80");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 8){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("128");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 9){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("160");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 10){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("320");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 11){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("640");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 12){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("1280");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text() == 13){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("2560");
		}else{
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(3)").text("无穷大");
		}
		//
		if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("320");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("640");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("1280");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("2560");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("5120");
		}else{
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(4)").text("10240");
		}
		//
		if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("50");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("100");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text() == 2){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("150");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text() == 3){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("300");
		}else if($("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text() == 4){
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("500");
		}else{
			$("#t_enb_srvmac1 tr:eq("+index+") td:eq(5)").text("1000");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7";
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
	$("#t_enb_srvmac1 tr").each(function(index){
		$("#t_enb_srvmac1 tr:eq("+index+") td:eq(6)").click(function(){
			var u8CfgIdx = $("#t_enb_srvmac1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_srvmac1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_srvmac1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_srvmac1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7&parameters="+para+"";
			});
		}
							   
	});	
	//删除
	$("#t_enb_srvmac1 tr").each(function(index){
		$("#t_enb_srvmac1 tr:eq("+index+") td:eq(7)").click(function(){
			var u8CfgIdx = $("#t_enb_srvmac1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_srvmac1 input[type=checkbox]").each(function(index){
			if($("#t_enb_srvmac1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_srvmac1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVMAC-3.0.7";
	});
});