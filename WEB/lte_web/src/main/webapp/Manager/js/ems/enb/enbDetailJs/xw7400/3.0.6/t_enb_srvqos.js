$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		if($("#u8BearerType").val()==1){	
			var index = 0;
			if(!(isNum.test($("#u32MaxBitRate").val()) && $("#u32MaxBitRate").val()<=1250000  && $("#u32MaxBitRate").val()>=0)){
				$("#u32MaxBitRateError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32MaxBitRateError").text("");
			}
			if(!(isNum.test($("#u32PBR").val()) && $("#u32PBR").val()<=1250000  && $("#u32PBR").val()>=0)){
				$("#u32PBRError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32PBRError").text("");
			}	
			
			if($("#u8SrvMacCfgIdx option").length<1){
				$("#u8SrvMacCfgIdxError").text("/* 无可用的MAC参数配置编号 */");
				index++;
			}else{
				$("#u8SrvMacCfgIdxError").text("");
			}
			if($("#u8SrvPcCfgIdx option").length<1){
				$("#u8SrvPcCfgIdxError").text("/* 无可用的功率控制配置索引 */");
				index++;
			}else{
				$("#u8SrvPcCfgIdxError").text("");
			}
			if($("#u8SrvPDCPCfgIdx option").length<1){
				$("#u8SrvPDCPCfgIdxError").text("/* 无可用的PDCP参数配置编号 */");
				index++;
			}else{
				$("#u8SrvPDCPCfgIdxError").text("");
			}
			if($("#u8SrvRLCCfgIdx option").length<1){
				$("#u8SrvRLCCfgIdxError").text("/* 无可用的RLC参数配置索引 */");
				index++;
			}else{
				$("#u8SrvRLCCfgIdxError").text("");
			}
			if(!($("input[name=u8Direction]:checked").val() == 0 || $("input[name=u8Direction]:checked").val() == 1)){
				$("#u8DirectionError").text("/* 请进行选择 */");
				index++;
			}else{
				$("#u8DirectionError").text("");
			}
			var para = "u8BearerType"+"="+$("#u8BearerType").val()+";"
						+"u32MaxBitRate"+"="+$("#u32MaxBitRate").val()+";"
						
						+"u32PBR"+"="+$("#u32PBR").val()+";"
						
						+"u8Direction"+"="+$("input[name='u8Direction']:checked").val()+";"
						+"u8SrvMacCfgIdx"+"="+$("#u8SrvMacCfgIdx").val()+";"
						+"u8SrvPcCfgIdx"+"="+$("#u8SrvPcCfgIdx").val()+";"
						+"u8SrvPDCPCfgIdx"+"="+$("#u8SrvPDCPCfgIdx").val()+";"
						+"u8SrvRLCCfgIdx"+"="+$("#u8SrvRLCCfgIdx").val();
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}
		}else{
			var index = 0;
			if(!(isNum.test($("#u32MaxBitRate").val()) && $("#u32MaxBitRate").val()<=1250000  && $("#u32MaxBitRate").val()>=0)){
				$("#u32MaxBitRateError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32MaxBitRateError").text("");
			}
			if($("#u8SrvMacCfgIdx option").length<1){
				$("#u8SrvMacCfgIdxError").text("/* 无可用的MAC参数配置编号 */");
				index++;
			}else{
				$("#u8SrvMacCfgIdxError").text("");
			}
			if($("#u8SrvPcCfgIdx option").length<1){
				$("#u8SrvPcCfgIdxError").text("/* 无可用的功率控制配置索引 */");
				index++;
			}else{
				$("#u8SrvPcCfgIdxError").text("");
			}
			if($("#u8SrvPDCPCfgIdx option").length<1){
				$("#u8SrvPDCPCfgIdxError").text("/* 无可用的PDCP参数配置编号 */");
				index++;
			}else{
				$("#u8SrvPDCPCfgIdxError").text("");
			}
			if($("#u8SrvRLCCfgIdx option").length<1){
				$("#u8SrvRLCCfgIdxError").text("/* 无可用的RLC参数配置索引 */");
				index++;
			}else{
				$("#u8SrvRLCCfgIdxError").text("");
			}
			if(!($("input[name=u8Direction]:checked").val() == 0 || $("input[name=u8Direction]:checked").val() == 1)){
				$("#u8DirectionError").text("/* 请进行选择 */");
				index++;
			}else{
				$("#u8DirectionError").text("");
			}
			var para = "u8BearerType"+"="+$("#u8BearerType").val()+";"
						+"u32MaxBitRate"+"="+$("#u32MaxBitRate").val()+";"
						
						+"u32PBR=0;"
						
						+"u8Direction"+"="+$("input[name='u8Direction']:checked").val()+";"
						+"u8SrvMacCfgIdx"+"="+$("#u8SrvMacCfgIdx").val()+";"
						+"u8SrvPcCfgIdx"+"="+$("#u8SrvPcCfgIdx").val()+";"
						+"u8SrvPDCPCfgIdx"+"="+$("#u8SrvPDCPCfgIdx").val()+";"
						+"u8SrvRLCCfgIdx"+"="+$("#u8SrvRLCCfgIdx").val();
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}			
		}
		
		
	});
	$("#submit_config").click(function(){
		if($("#u8BearerType").val()==1){	
			var index = 0;
			if(!(isNum.test($("#u32MaxBitRate").val()) && $("#u32MaxBitRate").val()<=1250000  && $("#u32MaxBitRate").val()>=0)){
				$("#u32MaxBitRateError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32MaxBitRateError").text("");
			}
			if(!(isNum.test($("#u32PBR").val()) && $("#u32PBR").val()<=1250000  && $("#u32PBR").val()>=0)){
				$("#u32PBRError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32PBRError").text("");
			}	
			if($("#u8SrvMacCfgIdx option").length<1){
				$("#u8SrvMacCfgIdxError").text("/* 无可用的MAC参数配置编号 */");
				index++;
			}else{
				$("#u8SrvMacCfgIdxError").text("");
			}
			if($("#u8SrvPcCfgIdx option").length<1){
				$("#u8SrvPcCfgIdxError").text("/* 无可用的功率控制配置索引 */");
				index++;
			}else{
				$("#u8SrvPcCfgIdxError").text("");
			}
			if($("#u8SrvPDCPCfgIdx option").length<1){
				$("#u8SrvPDCPCfgIdxError").text("/* 无可用的PDCP参数配置编号 */");
				index++;
			}else{
				$("#u8SrvPDCPCfgIdxError").text("");
			}
			if($("#u8SrvRLCCfgIdx option").length<1){
				$("#u8SrvRLCCfgIdxError").text("/* 无可用的RLC参数配置索引 */");
				index++;
			}else{
				$("#u8SrvRLCCfgIdxError").text("");
			}
			if(!($("#radio1").val() == 0 || $("#radio2").val() == 1)){
				$("#u8DirectionError").text("/* 请进行选择 */");
				index++;
			}else{
				$("#u8DirectionError").text("");
			}
			var para = "u8BearerType"+"="+$("#u8BearerType").val()+";"
						+"u32MaxBitRate"+"="+$("#u32MaxBitRate").val()+";"
						
						+"u32PBR"+"="+$("#u32PBR").val()+";"
						
						+"u8Direction"+"="+$("#u8Direction").val()+";"
						+"u8SrvMacCfgIdx"+"="+$("#u8SrvMacCfgIdx").val()+";"
						+"u8SrvPcCfgIdx"+"="+$("#u8SrvPcCfgIdx").val()+";"
						+"u8SrvPDCPCfgIdx"+"="+$("#u8SrvPDCPCfgIdx").val()+";"
						+"u8SrvRLCCfgIdx"+"="+$("#u8SrvRLCCfgIdx").val();
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_config").submit();	
			}
		}else{
			var index = 0;
			if(!(isNum.test($("#u32MaxBitRate").val()) && $("#u32MaxBitRate").val()<=1250000  && $("#u32MaxBitRate").val()>=0)){
				$("#u32MaxBitRateError").text("/* 请输入0~1250000之间的整数 */");
				index++;
			}else{
				$("#u32MaxBitRateError").text("");
			}
			if($("#u8SrvMacCfgIdx option").length<1){
				$("#u8SrvMacCfgIdxError").text("/* 无可用的MAC参数配置编号 */");
				index++;
			}else{
				$("#u8SrvMacCfgIdxError").text("");
			}
			if($("#u8SrvPcCfgIdx option").length<1){
				$("#u8SrvPcCfgIdxError").text("/* 无可用的功率控制配置索引 */");
				index++;
			}else{
				$("#u8SrvPcCfgIdxError").text("");
			}
			if($("#u8SrvPDCPCfgIdx option").length<1){
				$("#u8SrvPDCPCfgIdxError").text("/* 无可用的PDCP参数配置编号 */");
				index++;
			}else{
				$("#u8SrvPDCPCfgIdxError").text("");
			}
			if($("#u8SrvRLCCfgIdx option").length<1){
				$("#u8SrvRLCCfgIdxError").text("/* 无可用的RLC参数配置索引 */");
				index++;
			}else{
				$("#u8SrvRLCCfgIdxError").text("");
			}
			if(!($("#radio1").val() == 0 || $("#radio2").val() == 1)){
				$("#u8DirectionError").text("/* 请进行选择 */");
				index++;
			}else{
				$("#u8DirectionError").text("");
			}
			var para = "u8BearerType"+"="+$("#u8BearerType").val()+";"
						+"u32MaxBitRate"+"="+$("#u32MaxBitRate").val()+";"
						
						+"u32PBR=0;"
						
						+"u8Direction"+"="+$("#u8Direction").val()+";"
						+"u8SrvMacCfgIdx"+"="+$("#u8SrvMacCfgIdx").val()+";"
						+"u8SrvPcCfgIdx"+"="+$("#u8SrvPcCfgIdx").val()+";"
						+"u8SrvPDCPCfgIdx"+"="+$("#u8SrvPDCPCfgIdx").val()+";"
						+"u8SrvRLCCfgIdx"+"="+$("#u8SrvRLCCfgIdx").val();
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_config").submit();	
			}			
		}
		
		
	});
	//内存值转为显示值
	$("#t_enb_srvqos1 tr").each(function(index){
		//
		if($("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) input").val() == 0){
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) span").html("上行");
		}else{
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) span").html("下行");
		}
		//
		if($("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val() == 0){
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) span").html("GBR");
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(2)").text("");
		}else if($("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val() == 1){
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) span").html("NGBR");
		}else{
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) span").html("SRB");
			$("#t_enb_srvqos1 tr:eq("+index+") td:eq(2)").text("");
		}
	});	
	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6";
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
	$("#t_enb_srvqos1 tr").each(function(index){
		$("#t_enb_srvqos1 tr:eq("+index+") td:eq(8)").click(function(){
			var u8BearerType = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val();
			var u32MaxBitRate = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(1)").text();
			var u8Direction = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) input").val();
			var para = "u8BearerType"+"="+u8BearerType+";"
					+"u32MaxBitRate"+"="+u32MaxBitRate+";"
					+"u8Direction"+"="+u8Direction;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_srvqos1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_srvqos1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8BearerType = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val();
				var u32MaxBitRate = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(1)").text();
				var u8Direction = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) input").val();
				var para = "u8BearerType"+"="+u8BearerType+";"
						+"u32MaxBitRate"+"="+u32MaxBitRate+";"
						+"u8Direction"+"="+u8Direction;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_srvqos1 tr").each(function(index){
		$("#t_enb_srvqos1 tr:eq("+index+") td:eq(9)").click(function(){
			var u8BearerType = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val();
			var u32MaxBitRate = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(1)").text();
			var u8Direction = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) input").val();
			var para = "u8BearerType"+"="+u8BearerType+";"
					+"u32MaxBitRate"+"="+u32MaxBitRate+";"
					+"u8Direction"+"="+u8Direction;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str1=[];var str2=[];var str3=[];
		$("#t_enb_srvqos1 input[type=checkbox]").each(function(index){
			if($("#t_enb_srvqos1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(0) input").val();
				var temp2 = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(1)").text();
				var temp3 = $("#t_enb_srvqos1 tr:eq("+index+") td:eq(3) input").val();
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
			var s = "u8BearerType"+"="+str1[i]+","
						+"u32MaxBitRate"+"="+str2[i]+","
						+"u8Direction"+"="+str3[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_SRVQOS-3.0.6";
	});
});