$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8QCI").val()) && $("#u8QCI").val()<=255  && $("#u8QCI").val()>=1)){
			$("#u8QCIError").text("/* 请输入1~255之间的整数 */");
			index++;
		}else{
			$("#u8QCIError").text("");
		}
		if(!(isNum.test($("#u16SrvPacketDelay").val()) && $("#u16SrvPacketDelay").val()<=65535  && $("#u16SrvPacketDelay").val()>=0)){
			$("#u16SrvPacketDelayError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16SrvPacketDelayError").text("");
		}
		if(!(isNum.test($("#u32SrvPacketLoss").val()) && $("#u32SrvPacketLoss").val()<=1000000  && $("#u32SrvPacketLoss").val()>=0)){
			$("#u32SrvPacketLossError").text("/* 请输入0~1000000之间的整数 */");
			index++;
		}else{
			$("#u32SrvPacketLossError").text("");
		}
		if(!(isNum.test($("#u16RohcMaxCid").val()) && $("#u16RohcMaxCid").val()<=16383  && $("#u16RohcMaxCid").val()>=1)){
			$("#u16RohcMaxCidError").text("/* 请输入1~16383之间的整数 */");
			index++;
		}else{
			$("#u16RohcMaxCidError").text("");
		}
		var u8RohcProfileSwitch = "";
		for(var i = 0;i<9;i++){
			if($("#u8RohcProfileSwitch"+i).attr("checked") == "checked"){
				u8RohcProfileSwitch = u8RohcProfileSwitch + "01";
			}else{
				u8RohcProfileSwitch = u8RohcProfileSwitch + "00";
			}
		}
		
		var para = "u8QCI"+"="+$("#u8QCI").val()+";"
					+"u16SrvPacketDelay"+"="+$("#u16SrvPacketDelay").val()+";"
					+"u32SrvPacketLoss"+"="+$("#u32SrvPacketLoss").val()+";"
					+"u8SrvClassName"+"="+$("#u8SrvClassName").val()+";"
					+"u8SrvPrior"+"="+$("#u8SrvPrior").val()+";"
					+"u8SrvBearerType"+"="+$("input[name='u8SrvBearerType']:checked").val()+";"
					+"u8RlcMode"+"="+$("#u8RlcMode").val()+";"
					+"u8RohcSwitch"+"="+$("input[name='u8RohcSwitch']:checked").val()+";"
					+"u16RohcMaxCid"+"="+$("#u16RohcMaxCid").val()+";"
					+"u8RohcProfileSwitch"+"="+u8RohcProfileSwitch;
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_srvqci tr").each(function(index){
		//					   
		if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("CVoIP");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("CLSoIP");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("RealGaming");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("BSoIP");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("IMS Signaling");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("Prior IP Service");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 7){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("LSoIP");
		}else if($("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text() == 8){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("VIP Default Bearer");
		}else{
			$("#t_enb_srvqci tr:eq("+index+") td:eq(3)").text("NVIP Default Bearer");
		}
		//					   
		if($("#t_enb_srvqci tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(5)").text("GBR");
		}else{
			$("#t_enb_srvqci tr:eq("+index+") td:eq(5)").text("Non-GBR");
		}
		//					   
		if($("#t_enb_srvqci tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(6)").text("确认模式");
		}else{
			$("#t_enb_srvqci tr:eq("+index+") td:eq(6)").text("非确认模式");
		}
		//		   
		if($("#t_enb_srvqci tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_enb_srvqci tr:eq("+index+") td:eq(7)").text("开");
		}else{
			$("#t_enb_srvqci tr:eq("+index+") td:eq(7)").text("关");
		}
		//
		var u8RohcProfileSwitch = $("#t_enb_srvqci tr:eq("+index+") td:eq(9)").text();
		var u8RohcProfileSwitchText = "";
		for(var i = 0;i<9;i++){
			var j = 2*i +1;
			if(u8RohcProfileSwitch[j] == 1){
				u8RohcProfileSwitchText = u8RohcProfileSwitchText + "开,";
			}else{
				u8RohcProfileSwitchText = u8RohcProfileSwitchText + "关,";
			}
		}
		 $("#t_enb_srvqci tr:eq("+index+") td:eq(9)").text(u8RohcProfileSwitchText.substring(0,u8RohcProfileSwitchText.length - 1));
		
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5";
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
	$("#t_enb_srvqci tr").each(function(index){
		$("#t_enb_srvqci tr:eq("+index+") td:eq(10)").click(function(){
			var u8QCI = $("#t_enb_srvqci tr:eq("+index+") td:eq(0)").text();
			var para = "u8QCI"+"="+u8QCI;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_srvqci tr").each(function(index){
		if(index != 0){
			$("#t_enb_srvqci tr:eq("+index+") th:eq(1)").click(function(){
				var u8QCI = $("#t_enb_srvqci tr:eq("+index+") td:eq(0)").text();
				var para = "u8QCI"+"="+u8QCI;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5&parameters="+para+"";
			});
		}
							   
	});	
	//删除
	$("#t_enb_srvqci tr").each(function(index){
		$("#t_enb_srvqci tr:eq("+index+") td:eq(11)").click(function(){
			var u8QCI = $("#t_enb_srvqci tr:eq("+index+") td:eq(0)").text();
			var para = "u8QCI"+"="+u8QCI;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_srvqci input[type=checkbox]").each(function(index){
			if($("#t_enb_srvqci input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_srvqci tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8QCI"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQCI-2.1.5";
	});
});