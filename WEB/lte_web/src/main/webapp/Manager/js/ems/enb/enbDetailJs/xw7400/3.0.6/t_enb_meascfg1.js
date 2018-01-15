$(function(){
	if($("#u8EvtId").val() == 1){
		//
		$("#u8RsrpThreshold").removeAttr("disabled");
		
		//
		$("#u8RsrqThreshold").removeAttr("disabled");
		
		//
		$("#u8A3offset").removeAttr("value");
		$("#u8A3offset").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrp").removeAttr("value");
		$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrq").removeAttr("value");
		$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
		//
		$("#radio1").removeAttr("checked");
		$("#radio1").attr("disabled","disabled");
		$("#radio2").removeAttr("checked");
		$("#radio2").attr("disabled","disabled");
		//
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}
	}
	if($("#u8EvtId").val() == 2){
		//
		$("#u8RsrpThreshold").removeAttr("disabled");
		
		//
		$("#u8RsrqThreshold").removeAttr("disabled");
		
		//
		$("#u8A3offset").removeAttr("value");
		$("#u8A3offset").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrp").removeAttr("value");
		$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrq").removeAttr("value");
		$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
		//
		$("#radio1").removeAttr("checked");
		$("#radio1").attr("disabled","disabled");
		$("#radio2").removeAttr("checked");
		$("#radio2").attr("disabled","disabled");
		//
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}
	}
	if($("#u8EvtId").val() == 3){
		//
		$("#u8RsrpThreshold").removeAttr("value");
		$("#u8RsrpThreshold").attr("disabled","disabled");
		//
		$("#u8RsrqThreshold").removeAttr("value");
		$("#u8RsrqThreshold").attr("disabled","disabled");
		//
		$("#u8A3offset").removeAttr("disabled");
		$("#u8A3offset").attr("value","3");
		//
		$("#u8A5Thrd2Rsrp").removeAttr("value");
		$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrq").removeAttr("value");
		$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
		//
		$("#radio1").removeAttr("disabled");
		$("#radio1").attr("checked","checked");
		$("#radio2").removeAttr("disabled");
		//
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}
	}
	if($("#u8EvtId").val() == 4){
		//
		$("#u8RsrpThreshold").removeAttr("disabled");
		
		//
		$("#u8RsrqThreshold").removeAttr("disabled");
		
		//
		$("#u8A3offset").removeAttr("value");
		$("#u8A3offset").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrp").removeAttr("value");
		$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrq").removeAttr("value");
		$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
		//
		$("#radio1").removeAttr("checked");
		$("#radio1").attr("disabled","disabled");
		$("#radio2").removeAttr("checked");
		$("#radio2").attr("disabled","disabled");
		//
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}
	}
	if($("#u8EvtId").val() == 5){
		//
		$("#u8RsrpThreshold").removeAttr("disabled");
		
		//
		$("#u8RsrqThreshold").removeAttr("disabled");
		
		//
		$("#u8A3offset").removeAttr("value");
		$("#u8A3offset").attr("disabled","disabled");
		//
		$("#u8A5Thrd2Rsrp").removeAttr("disabled");
		$("#u8A5Thrd2Rsrp").attr("value","-90");
		//
		$("#u8A5Thrd2Rsrq").removeAttr("disabled");
		$("#u8A5Thrd2Rsrq").attr("value","-11.5");
		//
		$("#radio1").removeAttr("checked");
		$("#radio1").attr("disabled","disabled");
		$("#radio2").removeAttr("checked");
		$("#radio2").attr("disabled","disabled");
		//
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}
	}
	$("#u8EvtId").change(function(){
		if($("#u8EvtId").val() == 1){
			//
			$("#u8RsrpThreshold").removeAttr("disabled");
			
			//
			$("#u8RsrqThreshold").removeAttr("disabled");
			
			//
			$("#u8A3offset").removeAttr("value");
			$("#u8A3offset").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrp").removeAttr("value");
			$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrq").removeAttr("value");
			$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
			//
			$("#radio1").removeAttr("checked");
			$("#radio1").attr("disabled","disabled");
			$("#radio2").removeAttr("checked");
			$("#radio2").attr("disabled","disabled");
			//
			if($("#u8RptCriteria").val() == 1){
				$("#u8RptIntvl").removeAttr("disabled");
				$("#u8RptAmt").removeAttr("disabled");
				$("#u8RptIntvl option:eq(4)").attr("selected",true);
				$("#u8RptAmt option:eq(2)").attr("selected",true);	
				$("#u8PrdRptInterval").attr("disabled","disabled");	
				$("#u8PrdRptAmount").attr("disabled","disabled");	
			}else{
				$("#u8PrdRptInterval").removeAttr("disabled");
				$("#u8PrdRptAmount").removeAttr("disabled");
				$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
				$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
				$("#u8RptIntvl").attr("disabled","disabled");	
				$("#u8RptAmt").attr("disabled","disabled");	
			}
		}
		if($("#u8EvtId").val() == 2){
			//
			$("#u8RsrpThreshold").removeAttr("disabled");
			
			//
			$("#u8RsrqThreshold").removeAttr("disabled");
			
			//
			$("#u8A3offset").removeAttr("value");
			$("#u8A3offset").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrp").removeAttr("value");
			$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrq").removeAttr("value");
			$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
			//
			$("#radio1").removeAttr("checked");
			$("#radio1").attr("disabled","disabled");
			$("#radio2").removeAttr("checked");
			$("#radio2").attr("disabled","disabled");
			//
			if($("#u8RptCriteria").val() == 1){
				$("#u8RptIntvl").removeAttr("disabled");
				$("#u8RptAmt").removeAttr("disabled");
				$("#u8RptIntvl option:eq(4)").attr("selected",true);
				$("#u8RptAmt option:eq(2)").attr("selected",true);	
				$("#u8PrdRptInterval").attr("disabled","disabled");	
				$("#u8PrdRptAmount").attr("disabled","disabled");	
			}else{
				$("#u8PrdRptInterval").removeAttr("disabled");
				$("#u8PrdRptAmount").removeAttr("disabled");
				$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
				$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
				$("#u8RptIntvl").attr("disabled","disabled");	
				$("#u8RptAmt").attr("disabled","disabled");	
			}
		}
		if($("#u8EvtId").val() == 3){
			//
			$("#u8RsrpThreshold").removeAttr("value");
			$("#u8RsrpThreshold").attr("disabled","disabled");
			//
			$("#u8RsrqThreshold").removeAttr("value");
			$("#u8RsrqThreshold").attr("disabled","disabled");
			//
			$("#u8A3offset").removeAttr("disabled");
			$("#u8A3offset").attr("value","3");
			//
			$("#u8A5Thrd2Rsrp").removeAttr("value");
			$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrq").removeAttr("value");
			$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
			//
			$("#radio1").removeAttr("disabled");
			$("#radio1").attr("checked","checked");
			$("#radio2").removeAttr("disabled");
			//
			if($("#u8RptCriteria").val() == 1){
				$("#u8RptIntvl").removeAttr("disabled");
				$("#u8RptAmt").removeAttr("disabled");
				$("#u8RptIntvl option:eq(4)").attr("selected",true);
				$("#u8RptAmt option:eq(2)").attr("selected",true);	
				$("#u8PrdRptInterval").attr("disabled","disabled");	
				$("#u8PrdRptAmount").attr("disabled","disabled");	
			}else{
				$("#u8PrdRptInterval").removeAttr("disabled");
				$("#u8PrdRptAmount").removeAttr("disabled");
				$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
				$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
				$("#u8RptIntvl").attr("disabled","disabled");	
				$("#u8RptAmt").attr("disabled","disabled");	
			}
		}
		if($("#u8EvtId").val() == 4){
			//
			$("#u8RsrpThreshold").removeAttr("disabled");
			
			//
			$("#u8RsrqThreshold").removeAttr("disabled");
			
			//
			$("#u8A3offset").removeAttr("value");
			$("#u8A3offset").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrp").removeAttr("value");
			$("#u8A5Thrd2Rsrp").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrq").removeAttr("value");
			$("#u8A5Thrd2Rsrq").attr("disabled","disabled");
			//
			$("#radio1").removeAttr("checked");
			$("#radio1").attr("disabled","disabled");
			$("#radio2").removeAttr("checked");
			$("#radio2").attr("disabled","disabled");
			//
			if($("#u8RptCriteria").val() == 1){
				$("#u8RptIntvl").removeAttr("disabled");
				$("#u8RptAmt").removeAttr("disabled");
				$("#u8RptIntvl option:eq(4)").attr("selected",true);
				$("#u8RptAmt option:eq(2)").attr("selected",true);	
				$("#u8PrdRptInterval").attr("disabled","disabled");	
				$("#u8PrdRptAmount").attr("disabled","disabled");	
			}else{
				$("#u8PrdRptInterval").removeAttr("disabled");
				$("#u8PrdRptAmount").removeAttr("disabled");
				$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
				$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
				$("#u8RptIntvl").attr("disabled","disabled");	
				$("#u8RptAmt").attr("disabled","disabled");	
			}
		}
		if($("#u8EvtId").val() == 5){
			//
			$("#u8RsrpThreshold").removeAttr("disabled");
			
			//
			$("#u8RsrqThreshold").removeAttr("disabled");
			
			//
			$("#u8A3offset").removeAttr("value");
			$("#u8A3offset").attr("disabled","disabled");
			//
			$("#u8A5Thrd2Rsrp").removeAttr("disabled");
			$("#u8A5Thrd2Rsrp").attr("value","-90");
			//
			$("#u8A5Thrd2Rsrq").removeAttr("disabled");
			$("#u8A5Thrd2Rsrq").attr("value","-11.5");
			//
			$("#radio1").removeAttr("checked");
			$("#radio1").attr("disabled","disabled");
			$("#radio2").removeAttr("checked");
			$("#radio2").attr("disabled","disabled");
			//
			if($("#u8RptCriteria").val() == 1){
				$("#u8RptIntvl").removeAttr("disabled");
				$("#u8RptAmt").removeAttr("disabled");
				$("#u8RptIntvl option:eq(4)").attr("selected",true);
				$("#u8RptAmt option:eq(2)").attr("selected",true);	
				$("#u8PrdRptInterval").attr("disabled","disabled");	
				$("#u8PrdRptAmount").attr("disabled","disabled");	
			}else{
				$("#u8PrdRptInterval").removeAttr("disabled");
				$("#u8PrdRptAmount").removeAttr("disabled");
				$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
				$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
				$("#u8RptIntvl").attr("disabled","disabled");	
				$("#u8RptAmt").attr("disabled","disabled");	
			}
		}						
	});			
	$("#u8RptCriteria").change(function(){
		if($("#u8RptCriteria").val() == 1){
			$("#u8RptIntvl").removeAttr("disabled");
			$("#u8RptAmt").removeAttr("disabled");
			$("#u8RptIntvl option:eq(4)").attr("selected",true);
			$("#u8RptAmt option:eq(2)").attr("selected",true);	
			$("#u8PrdRptInterval").attr("disabled","disabled");	
			$("#u8PrdRptAmount").attr("disabled","disabled");	
		}else{
			$("#u8PrdRptInterval").removeAttr("disabled");
			$("#u8PrdRptAmount").removeAttr("disabled");
			$("#u8PrdRptInterval option:eq(4)").attr("selected",true);
			$("#u8PrdRptAmount option:eq(7)").attr("selected",true);	
			$("#u8RptIntvl").attr("disabled","disabled");	
			$("#u8RptAmt").attr("disabled","disabled");	
		}							
	});
});