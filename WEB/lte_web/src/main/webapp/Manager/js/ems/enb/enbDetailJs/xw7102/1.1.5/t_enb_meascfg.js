$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	var isStep=/^-?\d+(\.[0,5]{1}){0,1}$/;
	$("#submit_add").click(function(){
		if($("#u8EvtId").val()==1){
			var index = 0;
			if(!(isNum.test($("#u8MeasCfgIdx").val()) && $("#u8MeasCfgIdx").val()<=128  && $("#u8MeasCfgIdx").val()>=1)){
				$("#u8MeasCfgIdxError").text("/* 请输入1~128之间的整数 */");
				index++;
			}else{
				$("#u8MeasCfgIdxError").text("");
			}	
			if(!(isNum.test($("#u8RsrpThreshold").val()) && $("#u8RsrpThreshold").val()<=-44  && $("#u8RsrpThreshold").val()>=-141)){
				$("#u8RsrpThresholdError").text("/* 请输入-141~-44之间的整数 */");
				index++;
			}else{
				$("#u8RsrpThresholdError").text("");
			}
			if(!(isStep.test($("#u8RsrqThreshold").val()) && $("#u8RsrqThreshold").val()<=-3 && $("#u8RsrqThreshold").val()>=-20)){
				$("#u8RsrqThresholdError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8RsrqThresholdError").text("");
			}
			if(!(isStep.test($("#u8Hysteresis").val()) && $("#u8Hysteresis").val()<=15  && $("#u8Hysteresis").val()>=0)){
				$("#u8HysteresisError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8HysteresisError").text("");
			}	
			var para = "";
			if($("#u8RptCriteria").val() == 1){
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				 
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+$("#u8RptIntvl").val()+";"
				+"u8RptAmt"+"="+$("#u8RptAmt").val()+";"
				+"u8PrdRptAmount"+"="+0+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"
				
				+"u8PrdRptInterval"+"="+0+";"
				+"u8ReportOnLeave"+"=0";
				
				
			}else{
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+0+";"
				+"u8RptAmt"+"="+0+";"
				+"u8PrdRptAmount"+"="+$("#u8PrdRptAmount").val()+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"		
				+"u8PrdRptInterval"+"="+$("#u8PrdRptInterval").val()+";"
				+"u8ReportOnLeave"+"=0";
			}
			
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}
		}else if($("#u8EvtId").val()==2){
			var index = 0;
			if(!(isNum.test($("#u8MeasCfgIdx").val()) && $("#u8MeasCfgIdx").val()<=128  && $("#u8MeasCfgIdx").val()>=1)){
				$("#u8MeasCfgIdxError").text("/* 请输入1~128之间的整数 */");
				index++;
			}else{
				$("#u8MeasCfgIdxError").text("");
			}	
			if(!(isNum.test($("#u8RsrpThreshold").val()) && $("#u8RsrpThreshold").val()<=-44  && $("#u8RsrpThreshold").val()>=-141)){
				$("#u8RsrpThresholdError").text("/* 请输入-141~-44之间的整数 */");
				index++;
			}else{
				$("#u8RsrpThresholdError").text("");
			}
			if(!(isStep.test($("#u8RsrqThreshold").val()) && $("#u8RsrqThreshold").val()<=-3 && $("#u8RsrqThreshold").val()>=-20)){
				$("#u8RsrqThresholdError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8RsrqThresholdError").text("");
			}
			if(!(isStep.test($("#u8Hysteresis").val()) && $("#u8Hysteresis").val()<=15  && $("#u8Hysteresis").val()>=0)){
				$("#u8HysteresisError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8HysteresisError").text("");
			}	
			var para = "";
			if($("#u8RptCriteria").val() == 1){
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+$("#u8RptIntvl").val()+";"
				+"u8RptAmt"+"="+$("#u8RptAmt").val()+";"
				+"u8PrdRptAmount"+"="+0+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"
				
				+"u8PrdRptInterval"+"="+0+";"
				+"u8ReportOnLeave"+"=0";
				
				
			}else{
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+0+";"
				+"u8RptAmt"+"="+0+";"
				+"u8PrdRptAmount"+"="+$("#u8PrdRptAmount").val()+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"		
				
				+"u8PrdRptInterval"+"="+$("#u8PrdRptInterval").val()+";"
				+"u8ReportOnLeave"+"=0";
			}
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}		
		}else if($("#u8EvtId").val()==3){
			var index = 0;
			if(!(isNum.test($("#u8MeasCfgIdx").val()) && $("#u8MeasCfgIdx").val()<=128  && $("#u8MeasCfgIdx").val()>=1)){
				$("#u8MeasCfgIdxError").text("/* 请输入1~128之间的整数 */");
				index++;
			}else{
				$("#u8MeasCfgIdxError").text("");
			}	
			if(!(isStep.test($("#u8A3offset").val()) && $("#u8A3offset").val()<=15 && $("#u8A3offset").val()>=-15)){
				$("#u8A3offsetError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8A3offsetError").text("");
			}	
			if(!(isStep.test($("#u8Hysteresis").val()) && $("#u8Hysteresis").val()<=15  && $("#u8Hysteresis").val()>=0)){
				$("#u8HysteresisError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8HysteresisError").text("");
			}	
			var para = "";
			if($("#u8RptCriteria").val() == 1){
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"=0;"
				+"u8RsrqThreshold"+"=0;"
				+"u8A3offset"+"="+accMul(accAdd($("#u8A3offset").val(),15),2)+";"	
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+$("#u8RptIntvl").val()+";"
				+"u8RptAmt"+"="+$("#u8RptAmt").val()+";"
				+"u8PrdRptAmount"+"="+0+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"
				
				+"u8PrdRptInterval"+"="+0+";"
				+"u8ReportOnLeave"+"="+$("input[name='u8ReportOnLeave']:checked").val();
				
				
			}else{
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"=0;"
				+"u8RsrqThreshold"+"=0;"
				+"u8A3offset"+"="+accMul(accAdd($("#u8A3offset").val(),15),2)+";"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+0+";"
				+"u8RptAmt"+"="+0+";"
				+"u8PrdRptAmount"+"="+$("#u8PrdRptAmount").val()+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"		
				
				+"u8PrdRptInterval"+"="+$("#u8PrdRptInterval").val()+";"
				+"u8ReportOnLeave"+"="+$("input[name='u8ReportOnLeave']:checked").val();
			}
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}		
		}else if($("#u8EvtId").val()==4){
			var index = 0;
			if(!(isNum.test($("#u8MeasCfgIdx").val()) && $("#u8MeasCfgIdx").val()<=128  && $("#u8MeasCfgIdx").val()>=1)){
				$("#u8MeasCfgIdxError").text("/* 请输入1~128之间的整数 */");
				index++;
			}else{
				$("#u8MeasCfgIdxError").text("");
			}	
			if(!(isNum.test($("#u8RsrpThreshold").val()) && $("#u8RsrpThreshold").val()<=-44  && $("#u8RsrpThreshold").val()>=-141)){
				$("#u8RsrpThresholdError").text("/* 请输入-141~-44之间的整数 */");
				index++;
			}else{
				$("#u8RsrpThresholdError").text("");
			}
			if(!(isStep.test($("#u8RsrqThreshold").val()) && $("#u8RsrqThreshold").val()<=-3 && $("#u8RsrqThreshold").val()>=-20)){
				$("#u8RsrqThresholdError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8RsrqThresholdError").text("");
			}
			if(!(isStep.test($("#u8Hysteresis").val()) && $("#u8Hysteresis").val()<=15  && $("#u8Hysteresis").val()>=0)){
				$("#u8HysteresisError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8HysteresisError").text("");
			}	
			if($("#u8RptCriteria").val() == 1){
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+$("#u8RptIntvl").val()+";"
				+"u8RptAmt"+"="+$("#u8RptAmt").val()+";"
				+"u8PrdRptAmount"+"="+0+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"
				
				+"u8PrdRptInterval"+"="+0+";"
				+"u8ReportOnLeave"+"=0";
				
				
			}else{
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"=0;"
				+"u8A5Thrd2Rsrq"+"=0;"
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+0+";"
				+"u8RptAmt"+"="+0+";"
				+"u8PrdRptAmount"+"="+$("#u8PrdRptAmount").val()+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"		
				
				+"u8PrdRptInterval"+"="+$("#u8PrdRptInterval").val()+";"
				+"u8ReportOnLeave"+"=0";
			}
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}		
		}else{
			var index = 0;
			if(!(isNum.test($("#u8MeasCfgIdx").val()) && $("#u8MeasCfgIdx").val()<=128  && $("#u8MeasCfgIdx").val()>=1)){
				$("#u8MeasCfgIdxError").text("/* 请输入1~128之间的整数 */");
				index++;
			}else{
				$("#u8MeasCfgIdxError").text("");
			}	
			if(!(isNum.test($("#u8RsrpThreshold").val()) && $("#u8RsrpThreshold").val()<=-44  && $("#u8RsrpThreshold").val()>=-141)){
				$("#u8RsrpThresholdError").text("/* 请输入-141~-44之间的整数 */");
				index++;
			}else{
				$("#u8RsrpThresholdError").text("");
			}
			if(!(isStep.test($("#u8RsrqThreshold").val()) && $("#u8RsrqThreshold").val()<=-3 && $("#u8RsrqThreshold").val()>=-20)){
				$("#u8RsrqThresholdError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8RsrqThresholdError").text("");
			}
			if(!(isNum.test($("#u8A5Thrd2Rsrp").val()) && $("#u8A5Thrd2Rsrp").val()<=-44  && $("#u8A5Thrd2Rsrp").val()>=-141)){
				$("#u8A5Thrd2RsrpError").text("/* 请输入-141~-44之间的整数 */");
				index++;
			}else{
				$("#u8A5Thrd2RsrpError").text("");
			}	
			if(!(isStep.test($("#u8A5Thrd2Rsrq").val()) && $("#u8A5Thrd2Rsrq").val()<=-3  && $("#u8A5Thrd2Rsrq").val()>=-20)){
				$("#u8A5Thrd2RsrqError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8A5Thrd2RsrqError").text("");
			}	
			if(!(isStep.test($("#u8Hysteresis").val()) && $("#u8Hysteresis").val()<=15  && $("#u8Hysteresis").val()>=0)){
				$("#u8HysteresisError").text("/* 请输入符合要求的数字 */");
				index++;
			}else{
				$("#u8HysteresisError").text("");
			}	
			var para = "";
			if($("#u8RptCriteria").val() == 1){
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"="+accAdd($("#u8A5Thrd2Rsrp").val(),141)+";"
				+"u8A5Thrd2Rsrq"+"="+accMul(accAdd($("#u8A5Thrd2Rsrq").val(),20),2)+";"	
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+$("#u8RptIntvl").val()+";"
				+"u8RptAmt"+"="+$("#u8RptAmt").val()+";"
				+"u8PrdRptAmount"+"="+0+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"
				
				+"u8PrdRptInterval"+"="+0+";"
				+"u8ReportOnLeave"+"=0";
				
				
			}else{
				para = "u8MeasCfgIdx"+"="+$("#u8MeasCfgIdx").val()+";"
				+"u8TrigQuan"+"="+$("#u8TrigQuan").val()+";"
				+"u8ReportQuan"+"="+$("#u8ReportQuan").val()+";"
				+"u8RptCriteria"+"="+$("#u8RptCriteria").val()+";"
				+"u8EvtId"+"="+$("#u8EvtId").val()+";"
				
				+"u8RsrpThreshold"+"="+accAdd($("#u8RsrpThreshold").val(),141)+";"
				+"u8RsrqThreshold"+"="+accMul(accAdd($("#u8RsrqThreshold").val(),20),2)+";"	
				+"u8A3offset"+"=0;"
				+"u8A5Thrd2Rsrp"+"="+accAdd($("#u8A5Thrd2Rsrp").val(),141)+";"
				+"u8A5Thrd2Rsrq"+"="+accMul(accAdd($("#u8A5Thrd2Rsrq").val(),20),2)+";"	
				
				+"u8Hysteresis"+"="+accMul($("#u8Hysteresis").val(),2)+";"
				+"u8TrigTime"+"="+$("#u8TrigTime").val()+";"
				+"u8RptIntvl"+"="+0+";"
				+"u8RptAmt"+"="+0+";"
				+"u8PrdRptAmount"+"="+$("#u8PrdRptAmount").val()+";"
				+"u8MaxRptCellNum"+"="+$("#u8MaxRptCellNum").val()+";"		
				
				+"u8PrdRptInterval"+"="+$("#u8PrdRptInterval").val()+";"
				+"u8ReportOnLeave"+"=0";
			}
			
			$("#parameters").val(para);
			if(index==0){
				$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
			}			
		}
		
		
	});
	//内存值转为显示值
	$("#t_enb_meascfg1 tr").each(function(index){
		//
		if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(1)").text("rsrp");
		}else{
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(1)").text("rsrq");
		}
		//
		if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(2)").text("sameAsTriggerQuantity");
		}else{
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(2)").text("both");
		}
		//
		if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(3)").text("事件上报");
			//
			if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 0){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("120ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 1){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("240ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 2){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("480ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 3){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("640ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 4){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("1024ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 5){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("2048ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 6){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("5120ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 7){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("10240ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 8){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("1min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 9){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("6min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 10){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("12min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text() == 11){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("30min");
			}else{
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("60min");
			}
			//
			if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 0){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("1");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 1){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("2");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 2){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("4");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 3){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("8");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 4){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("16");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 5){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("32");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text() == 6){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("64");
			}else{
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("无穷大");
			}
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("");
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("");
		}else{
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(3)").text("周期上报");
			//
			if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 0){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("120ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 1){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("240ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 2){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("480ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 3){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("640ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 4){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("1024ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 5){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("2048ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 6){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("5120ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 7){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("10240ms");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 8){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("1min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 9){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("6min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 10){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("12min");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text() == 11){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("30min");
			}else{
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(14)").text("60min");
			}
			//
			if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 0){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("1");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 1){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("2");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 2){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("4");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 3){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("8");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 4){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("16");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 5){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("32");
			}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text() == 6){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("64");
			}else{
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(15)").text("无穷大");
			}
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(12)").text("");
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(13)").text("");
		}
		//
		if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text("A1");
			//
			var u8RsrpThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text());
			var jack = u8RsrpThreshold - 141;
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text(jack);
			//
			var u8RsrqThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text());
			var may = accSub(accDiv(u8RsrqThreshold,2),20);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text(may);	
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("");
			//
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text("A2");
			//
			var u8RsrpThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text());
			var jack = u8RsrpThreshold - 141;
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text(jack);
			//
			var u8RsrqThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text());
			var may = accSub(accDiv(u8RsrqThreshold,2),20);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text(may);	
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("");
			//
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text("A3");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text("");
			//
			var u8A3offset = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text());
			var helen = accSub(accDiv(u8A3offset,2),15);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text(helen);
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text("");			
			//
			if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text() == 0){
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("否");
			}else{
				$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("是");
			}
			//
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text("A4");
			//
			var u8RsrpThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text());
			var jack = u8RsrpThreshold - 141;
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text(jack);
			//
			var u8RsrqThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text());
			var may = accSub(accDiv(u8RsrqThreshold,2),20);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text(may);	
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text("");
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("");
			//
		}else{
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(4)").text("A5");
			//
			var u8RsrpThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text());
			var jack = u8RsrpThreshold - 141;
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(5)").text(jack);
			//
			var u8RsrqThreshold = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text());
			var may = accSub(accDiv(u8RsrqThreshold,2),20);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(6)").text(may);
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(7)").text("");
			//
			var u8A5Thrd2Rsrp = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text());
			var coco = u8A5Thrd2Rsrp - 141 ;
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(8)").text(coco);
			//
			var u8A5Thrd2Rsrq = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text());
			var spe = accSub(accDiv(u8A5Thrd2Rsrq,2),20);
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(9)").text(spe);
			//
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(17)").text("");
			//			
		}
		//
		var u8Hysteresis = parseInt($("#t_enb_meascfg1 tr:eq("+index+") td:eq(10)").text());
		var st = accDiv(u8Hysteresis,2);
		$("#t_enb_meascfg1 tr:eq("+index+") td:eq(10)").text(st);
		//
		if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 0){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("0");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 1){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("40");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 2){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("64");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 3){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("80");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 4){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("100");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 5){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("128");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 6){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("160");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 7){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("256");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 8){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("320");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 9){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("480");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 10){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("512");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 11){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("640");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 12){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("1024");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 13){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("1280");
		}else if($("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text() == 14){
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("2560");
		}else{
			$("#t_enb_meascfg1 tr:eq("+index+") td:eq(11)").text("5120");
		}
		
		
	});	
	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5";
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
	$("#t_enb_meascfg1 tr").each(function(index){
		$("#t_enb_meascfg1 tr:eq("+index+") td:eq(18)").click(function(){
			var u8MeasCfgIdx = $("#t_enb_meascfg1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8MeasCfgIdx"+"="+u8MeasCfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_meascfg1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_meascfg1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8MeasCfgIdx = $("#t_enb_meascfg1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8MeasCfgIdx"+"="+u8MeasCfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5&parameters="+para+"";
			});
		}
							   
	});	
	//删除
	$("#t_enb_meascfg1 tr").each(function(index){
		$("#t_enb_meascfg1 tr:eq("+index+") td:eq(19)").click(function(){
			var u8MeasCfgIdx = $("#t_enb_meascfg1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8MeasCfgIdx"+"="+u8MeasCfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_meascfg1 input[type=checkbox]").each(function(index){
			if($("#t_enb_meascfg1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_meascfg1 tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8MeasCfgIdx"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_MEASCFG-1.1.5";
	});
	function accAdd(arg1,arg2){
		var r1,r2,m;
		try{
			r1 = arg1.toString().split(".")[1].length;	
		}catch(e){
			r1 = 0;	
		}
		try{
			r2 = arg2.toString().split(".")[1].length;	
		}catch(e){
			r2 = 0;	
		}
		m = Math.pow(10,Math.max(r1,r2));
		return (arg1*m + arg2*m)/m;
	}
	function accSub(arg1,arg2){
		var r1,r2,m,n;
		try{
			r1 = arg1.toString().split(".")[1].length;	
		}catch(e){
			r1 = 0;	
		}
		try{
			r2 = arg2.toString().split(".")[1].length;	
		}catch(e){
			r2 = 0;	
		}
		m = Math.pow(10,Math.max(r1,r2));
		n = (r1>=r2)?r1:r2;
		return ((arg1*m - arg2*m)/m).toFixed(n);
	}
	function accMul(arg1,arg2){
		var m = 0;
		var s1 = arg1.toString();
		var s2 = arg2.toString();
		try{
			m += s1.split(".")[1].length	;
		}catch(e){}
		try{
			m += s2.split(".")[1].length	;
		}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}
	function accDiv(arg1,arg2){
		var t1 = 0;
		var t2 = 0;
		var r1,r2;
		try{
			t1 = arg1.toString().split(".")[1].length;
		}catch(e){}
		try{
			t2 = arg2.toString().split(".")[1].length;	
		}catch(e){}
		with(Math){
			r1 = Number(arg1.toString().replace(".",""));
			r2 = Number(arg2.toString().replace(".",""));
			return (r1/r2)*pow(10,t2-t1);
		}
	}	
});