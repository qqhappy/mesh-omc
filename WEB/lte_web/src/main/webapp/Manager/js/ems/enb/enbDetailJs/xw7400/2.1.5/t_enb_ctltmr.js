$(function(){
	//表单校验
	var isNum=/^(-)?\d+[0]{1}$/;
	var numRegex = /^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if($("#u8CfgIdx").val() != 1){
			$("#u8CfgIdxError").text("/* 此索引号只可为1 */");
			index++;
		}else{ 
			$("#u8CfgIdxError").text("");
		}
		if(!(isNum.test($("#u16RrcSetupTimer").val()) && $("#u16RrcSetupTimer").val()<=16000  && $("#u16RrcSetupTimer").val()>=10)){
			$("#u16RrcSetupTimerError").text("/* 10~16000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16RrcSetupTimerError").text("");
		}
		if(!(isNum.test($("#u16RrcReCfgTimer").val()) && $("#u16RrcReCfgTimer").val()<=16000  && $("#u16RrcReCfgTimer").val()>=10)){
			$("#u16RrcReCfgTimerError").text("/* 10~16000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16RrcReCfgTimerError").text("");
		}
		if(!(isNum.test($("#u16RrcReEstTimer").val()) && $("#u16RrcReEstTimer").val()<=6000  && $("#u16RrcReEstTimer").val()>=10)){
			$("#u16RrcReEstTimerError").text("/* 10~6000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16RrcReEstTimerError").text("");
		}
		if(!(isNum.test($("#u16RrcCnntRlsTimer").val()) && $("#u16RrcCnntRlsTimer").val()<=3000  && $("#u16RrcCnntRlsTimer").val()>=10)){
			$("#u16RrcCnntRlsTimerError").text("/* 10~3000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16RrcCnntRlsTimerError").text("");
		}
		if(!(isNum.test($("#u16UeCapTimer").val()) && $("#u16UeCapTimer").val()<=2000  && $("#u16UeCapTimer").val()>=10)){
			$("#u16UeCapTimerError").text("/* 10~2000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16UeCapTimerError").text("");
		}
		if(!(isNum.test($("#u16HoDataGuardTimer").val()) && $("#u16HoDataGuardTimer").val()<=2000  && $("#u16HoDataGuardTimer").val()>=10)){
			$("#u16HoDataGuardTimerError").text("/* 10~2000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16HoDataGuardTimerError").text("");
		}
		if(!(isNum.test($("#u16InitialUETimer").val()) && $("#u16InitialUETimer").val()<=16000  && $("#u16InitialUETimer").val()>=10)){
			$("#u16InitialUETimerError").text("/* 10~16000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16InitialUETimerError").text("");
		}
		if(!(isNum.test($("#u32S1SetupRspTimer").val()) && $("#u32S1SetupRspTimer").val()<=20000  && $("#u32S1SetupRspTimer").val()>=10)){
			$("#u32S1SetupRspTimerError").text("/* 10~20000之间的整数,步长10 */");
			index++;
		}else{
			$("#u32S1SetupRspTimerError").text("");
		}
		if(!(isNum.test($("#u16UeSetupFailTimer").val()) && $("#u16UeSetupFailTimer").val()<=1200  && $("#u16UeSetupFailTimer").val()>=10)){
			$("#u16UeSetupFailTimerError").text("/* 10~1200之间的整数,步长10 */");
			index++;
		}else{
			$("#u16UeSetupFailTimerError").text("");
		}
		if(!(isNum.test($("#u16UeCtxRelReqTimer").val()) && $("#u16UeCtxRelReqTimer").val()<=10000  && $("#u16UeCtxRelReqTimer").val()>=10)){
			$("#u16UeCtxRelReqTimerError").text("/* 10~10000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16UeCtxRelReqTimerError").text("");
		}
		if(!(isNum.test($("#u16S1HoPrepareTimer").val()) && $("#u16S1HoPrepareTimer").val()<=20000  && $("#u16S1HoPrepareTimer").val()>=10)){
			$("#u16S1HoPrepareTimerError").text("/* 10~20000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16S1HoPrepareTimerError").text("");
		}
		if(!(isNum.test($("#u16S1HoCompleteTimer").val()) && $("#u16S1HoCompleteTimer").val()<=30000  && $("#u16S1HoCompleteTimer").val()>=10)){
			$("#u16S1HoCompleteTimerError").text("/* 10~30000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16S1HoCompleteTimerError").text("");
		}
		if(!(isNum.test($("#u32CmmCellCfgTimer").val()) && $("#u32CmmCellCfgTimer").val()<=10000  && $("#u32CmmCellCfgTimer").val()>=1000)){
			$("#u32CmmCellCfgTimerError").text("/* 1000~10000之间的整数,步长10 */");
			index++;
		}else{
			$("#u32CmmCellCfgTimerError").text("");
		}
		if(!(isNum.test($("#u32S1ResetAckTimer").val()) && $("#u32S1ResetAckTimer").val()<=20000  && $("#u32S1ResetAckTimer").val()>=10)){
			$("#u32S1ResetAckTimerError").text("/* 10~20000之间的整数,步长10 */");
			index++;
		}else{
			$("#u32S1ResetAckTimerError").text("");
		}
		if(!(isNum.test($("#u32S1UptAckTimer").val()) && $("#u32S1UptAckTimer").val()<=20000  && $("#u32S1UptAckTimer").val()>=10)){
			$("#u32S1UptAckTimerError").text("/* 10~20000之间的整数,步长10 */");
			index++;
		}else{
			$("#u32S1UptAckTimerError").text("");
		}
		if(!(isNum.test($("#u16PingpongHoTimer").val()) && $("#u16PingpongHoTimer").val()<=20000  && $("#u16PingpongHoTimer").val()>=10)){
			$("#u16PingpongHoTimerError").text("/* 10~20000之间的整数,步长10 */");
			index++;
		}else{
			$("#u16PingpongHoTimerError").text("");
		}
		if(!(numRegex.test($("#u16A3RelInactTimer").val()) && $("#u16A3RelInactTimer").val()<=65535  && $("#u16A3RelInactTimer").val()>=0)){
			$("#u16A3RelInactTimerError").text("/* 0~65535之间的整数 */");
			index++;
		}else{
			$("#u16A3RelInactTimerError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u16RrcSetupTimer"+"="+$("#u16RrcSetupTimer").val()+";"
				+"u16RrcReCfgTimer"+"="+$("#u16RrcReCfgTimer").val()+";"
				+"u16RrcReEstTimer"+"="+$("#u16RrcReEstTimer").val()+";"
				+"u16RrcCnntRlsTimer"+"="+$("#u16RrcCnntRlsTimer").val()+";"
				+"u16UeCapTimer"+"="+$("#u16UeCapTimer").val()+";"
				+"u16HoDataGuardTimer"+"="+$("#u16HoDataGuardTimer").val()+";"
				+"u16InitialUETimer"+"="+$("#u16InitialUETimer").val()+";"
				+"u32S1SetupRspTimer"+"="+$("#u32S1SetupRspTimer").val()+";"
				+"u16UeSetupFailTimer"+"="+$("#u16UeSetupFailTimer").val()+";"
				+"u16UeCtxRelReqTimer"+"="+$("#u16UeCtxRelReqTimer").val()+";"
				+"u16S1HoPrepareTimer"+"="+$("#u16S1HoPrepareTimer").val()+";"
				+"u16S1HoCompleteTimer"+"="+$("#u16S1HoCompleteTimer").val()+";"
				+"u32CmmCellCfgTimer"+"="+$("#u32CmmCellCfgTimer").val()+";"
				+"u32S1ResetAckTimer"+"="+$("#u32S1ResetAckTimer").val()+";"
				+"u16PingpongHoTimer"+"="+$("#u16PingpongHoTimer").val()+";"
				+"u16A3RelInactTimer"+"="+$("#u16A3RelInactTimer").val()+";"
				+"u32S1UptAckTimer"+"="+$("#u32S1UptAckTimer").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5";
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
	$("#t_enb_ctltmr1 tr").each(function(index){
		$("#t_enb_ctltmr1 tr:eq("+index+") td:eq(18)").click(function(){
			var u8CfgIdx = $("#t_enb_ctltmr1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_ctltmr1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_ctltmr1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_ctltmr1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_ctltmr1 tr").each(function(index){
		$("#t_enb_ctltmr1 tr:eq("+index+") td:eq(19)").click(function(){
			var u8CfgIdx = $("#t_enb_ctltmr1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_ctltmr1 input[type=checkbox]").each(function(index){
			if($("#t_enb_ctltmr1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_ctltmr1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTLTMR-2.1.5";
	});
});