$(function(){
	//表单校验
	var isNum=/^-?\d+$/;
	var isStep=/^-?\d+(\.\d{1}){0,1}$/;
	$("#submit_add").click(function(){
		//
		var u8FreqBandInd = $("#u8FreqBandInd").val();
		var low = 0;
		var offset = 0;
		if(u8FreqBandInd == 33){
			low = 1900;
			offset = 36000;
		}else if(u8FreqBandInd == 34){
			low = 2010;
			offset = 36200;
		}else if(u8FreqBandInd == 35){
			low = 1850;
			offset = 36350;
		}else if(u8FreqBandInd == 36){
			low = 1930;
			offset = 36950;
		}else if(u8FreqBandInd == 37){
			low = 1910;
			offset = 37550;
		}else if(u8FreqBandInd == 38){
			low = 2570;
			offset = 37750;
		}else if(u8FreqBandInd == 39){
			low = 1880;
			offset = 38250;
		}else if(u8FreqBandInd == 40){
			low = 2300;
			offset = 38650;
		}else if(u8FreqBandInd == 45){
			low = 1447;
			offset = 46590;
		}else if(u8FreqBandInd == 53){
			low = 778;
			offset = 64000;
		}else if(u8FreqBandInd == 58){
			low = 380;
			offset = 58200;
		}else if(u8FreqBandInd == 59){
			low = 1785;
			offset = 54200;
		}else if(u8FreqBandInd == 61){
			low = 1447;
			offset = 65000;
		}else if(u8FreqBandInd == 62){
			low = 1785;
			offset = 65200;
		}else if(u8FreqBandInd == 63){
			low = 526;
			offset = 58260;
		}
		var u32CenterFreq = parseFloat($("#u32CenterFreq").val());
		var u32CenterFreq1 = accAdd(accMul(accSub(u32CenterFreq,low),10),offset);
		$("#u32CenterFreq1").val(u32CenterFreq1);
		//
		var index = 0;
		if(!(isNum.test($("#u8CfgIdx").val()) && $("#u8CfgIdx").val()<=254  && $("#u8CfgIdx").val()>=0)){
			$("#u8CfgIdxError").text("/* 请输入0~254之间的整数 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}
		var big = parseFloat($("#centerFreq2").text());
		var small = parseFloat($("#centerFreq1").text());
		if(!(isStep.test($("#u32CenterFreq").val()) && parseFloat($("#u32CenterFreq").val())<=big  && parseFloat($("#u32CenterFreq").val())>=small)){
			$("#u32CenterFreqError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u32CenterFreqError").text("");
		}
		if(!(isNum.test($("#u8InterQrxLevMin").val()) && $("#u8InterQrxLevMin").val()<=-44  && $("#u8InterQrxLevMin").val()>=-140 && $("#u8InterQrxLevMin").val()%2 == 0)){
			$("#u8InterQrxLevMinError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u8InterQrxLevMinError").text("");
		}
		if(!(isNum.test($("#u8InterPmax").val()) && $("#u8InterPmax").val()<=33 && $("#u8InterPmax").val()>=-30)){
			$("#u8InterPmaxError").text("/* 请输入-30~33之间的整数 */");
			index++;
		}else{
			$("#u8InterPmaxError").text("");
		}
		if(!(isNum.test($("#u8ThreshXHigh").val()) && $("#u8ThreshXHigh").val()<=62 && $("#u8ThreshXHigh").val()>=0 && $("#u8ThreshXHigh").val()%2 == 0)){
			$("#u8ThreshXHighError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u8ThreshXHighError").text("");
		}
		if(!(isNum.test($("#u8ThreshXLow").val()) && $("#u8ThreshXLow").val()<=62 && $("#u8ThreshXLow").val()>=0 && $("#u8ThreshXLow").val()%2 == 0)){
			$("#u8ThreshXLowError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u8ThreshXLowError").text("");
		}
		if(!(isNum.test($("#u8InterFreqQqualMin").val()) && $("#u8InterFreqQqualMin").val()<=-3 && $("#u8InterFreqQqualMin").val()>=-34)){
			$("#u8InterFreqQqualMinError").text("/* 请输入-34~-3之间的整数 */");
			index++;
		}else{
			$("#u8InterFreqQqualMinError").text("");
		}
		if(!(isNum.test($("#u8InterThreshXHighQ").val()) && $("#u8InterThreshXHighQ").val()<=31 && $("#u8InterThreshXHighQ").val()>=0)){
			$("#u8InterThreshXHighQError").text("/* 请输入0~31之间的整数 */");
			index++;
		}else{
			$("#u8InterThreshXHighQError").text("");
		}
		if(!(isNum.test($("#u8InterThreshXLowQ").val()) && $("#u8InterThreshXLowQ").val()<=31 && $("#u8InterThreshXLowQ").val()>=0)){
			$("#u8InterThreshXLowQError").text("/* 请输入0~31之间的整数 */");
			index++;
		}else{
			$("#u8InterThreshXLowQError").text("");
		}
		if($("#u8InterFreqHOMeasCfg option").length<1){
			$("#u8InterFreqHOMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}else{
			$("#u8InterFreqHOMeasCfgError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8FreqBandInd"+"="+$("#u8FreqBandInd").val()+";"
				+"u32CenterFreq"+"="+$("#u32CenterFreq1").val()+";"
				+"u8InterQrxLevMin"+"="+accDiv(accAdd($("#u8InterQrxLevMin").val(),140),2)+";"
				+"u8InterPmax"+"="+accAdd($("#u8InterPmax").val(),30)+";"
				+"u8tRslInterEutra"+"="+$("#u8tRslInterEutra").val()+";"
				+"u8ThreshXHigh"+"="+accDiv($("#u8ThreshXHigh").val(),2)+";"
				+"u8ThreshXLow"+"="+accDiv($("#u8ThreshXLow").val(),2)+";"
				+"u8RslAllowedMeasBW"+"="+$("#u8RslAllowedMeasBW").val()+";"
				+"u8InterAntPort1"+"="+$("input[name='u8InterAntPort1']:checked").val()+";"
				+"u8InterRslPrior"+"="+$("#u8InterRslPrior").val()+";"
				+"ucRslQOffsetFreq"+"="+$("#ucRslQOffsetFreq").val()+";"
				+"u8InterFreqQqualMin"+"="+accAdd($("#u8InterFreqQqualMin").val(),34)+";"
				+"u8InterThreshXHighQ"+"="+$("#u8InterThreshXHighQ").val()+";"
				+"u8InterThreshXLowQ"+"="+$("#u8InterThreshXLowQ").val()+";"
				+"ucMeasQOffsetFreq"+"="+$("#ucMeasQOffsetFreq").val()+";"
				+"u8MeasAllowedMeasBW"+"="+$("#u8MeasAllowedMeasBW").val()+";"
				+"u8NbrCellConfig"+"="+$("#u8NbrCellConfig").val()+";"
				+"u8InterFreqHOMeasCfg"+"="+$("#u8InterFreqHOMeasCfg").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8";
					}				
				}
			});	
		}
	});
	$("#t_enb_ctfreq1 tr").each(function(index){
		//
		var u8FreqBandInd = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(1)").text());
		var low = 0;
		var offset = 0;
		if(u8FreqBandInd == 33){
			low = 1900;
			offset = 36000;
		}else if(u8FreqBandInd == 34){
			low = 2010;
			offset = 36200;
		}else if(u8FreqBandInd == 35){
			low = 1850;
			offset = 36350;
		}else if(u8FreqBandInd == 36){
			low = 1930;
			offset = 36950;
		}else if(u8FreqBandInd == 37){
			low = 1910;
			offset = 37550;
		}else if(u8FreqBandInd == 38){
			low = 2570;
			offset = 37750;
		}else if(u8FreqBandInd == 39){
			low = 1880;
			offset = 38250;
		}else if(u8FreqBandInd == 40){
			low = 2300;
			offset = 38650;
		}else if(u8FreqBandInd == 45){
			low = 1447;
			offset = 46590;
		}else if(u8FreqBandInd == 53){
			low = 778;
			offset = 64000;
		}else if(u8FreqBandInd == 58){
			low = 380;
			offset = 58200;
		}else if(u8FreqBandInd == 59){
			low = 1785;
			offset = 54200;
		}else if(u8FreqBandInd == 61){
			low = 1447;
			offset = 65000;
		}else if(u8FreqBandInd == 62){
			low = 1785;
			offset = 65200;
		}else if(u8FreqBandInd == 63){
			low = 526;
			offset = 58260;
		}
		var u32CenterFreq = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(2)").text());
		var u32CenterFreq1 = accAdd(accDiv(accSub(u32CenterFreq,offset),10),low);
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(2)").text(u32CenterFreq1);
		//
		var u8InterQrxLevMin = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(3)").text());
		var u8InterQrxLevMin1 = 2*u8InterQrxLevMin - 140;
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(3)").text(u8InterQrxLevMin1);
		//
		var u8InterPmax = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(4)").text());
		var u8InterPmax1 = u8InterPmax - 30;
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(4)").text(u8InterPmax1);
		//
		var u8ThreshXHigh = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(6)").text());
		var u8ThreshXHigh1 = u8ThreshXHigh*2;
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(6)").text(u8ThreshXHigh1);
		//
		var u8ThreshXLow = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(7)").text());
		var u8ThreshXLow1 = u8ThreshXLow*2;
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(7)").text(u8ThreshXLow1);
		//
		var u8InterFreqQqualMin = parseInt($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(12)").text());
		var u8InterFreqQqualMin1 = u8InterFreqQqualMin-34;
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(12)").text(u8InterFreqQqualMin1);

		//					   
		if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("1.4M(6RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("3M(15RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("5M(25RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text() == 3){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("10M(50RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text() == 4){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("15M(75RB)");
		}else{
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(8)").text("20M(100RB)");
		}	
		//					   
		if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(9)").text("否");
		}else{
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(9)").text("是");
		}
		//
		if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-24");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 1){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-22");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 2){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-20");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 3){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-18");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 4){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-16");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 5){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-14");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 6){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-12");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 7){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-10");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 8){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-8");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 9){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-6");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 10){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-5");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 11){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-4");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 12){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-3");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 13){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-2");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 14){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("-1");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 15){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("0");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 16){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("1");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 17){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("2");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 18){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("3");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 19){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("4");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 20){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("5");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 21){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("6");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 22){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("8");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 23){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("10");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 24){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("12");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 25){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("14");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 26){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("16");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 27){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("18");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 28){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("20");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text() == 29){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("22");
		}else{
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(11)").text("24");
		}
		//
		if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-24");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 1){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-22");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 2){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-20");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 3){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-18");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 4){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-16");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 5){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-14");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 6){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-12");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 7){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-10");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 8){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-8");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 9){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-6");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 10){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-5");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 11){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-4");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 12){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-3");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 13){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-2");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 14){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("-1");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 15){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("0");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 16){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("1");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 17){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("2");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 18){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("3");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 19){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("4");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 20){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("5");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 21){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("6");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 22){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("8");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 23){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("10");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 24){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("12");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 25){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("14");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 26){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("16");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 27){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("18");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 28){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("20");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text() == 29){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("22");
		}else{
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(15)").text("24");
		}
		//
		if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text() == 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("1.4M(6RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text() == 1){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("3M(15RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text() == 2){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("5M(25RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text() == 3){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("10M(50RB)");
		}else if($("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text() == 4){
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("15M(75RB)");
		}else{
			$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(16)").text("20M(100RB)");
		}
		
	});	
	//联动控制
	if($("#u8FreqBandInd").val() == 33){
		$("#centerFreq1").html("1900.7");	
		$("#centerFreq2").html("1919.3");	
	}else if($("#u8FreqBandInd").val() == 34){
		$("#centerFreq1").html("2010.7");
		$("#centerFreq2").html("2024.3");
	}else if($("#u8FreqBandInd").val() == 35){
		$("#centerFreq1").html("1850.7");	
		$("#centerFreq2").html("1909.3");	
	}else if($("#u8FreqBandInd").val() == 36){
		$("#centerFreq1").html("1930.7");	
		$("#centerFreq2").html("1989.3");	
	}else if($("#u8FreqBandInd").val() == 37){
		$("#centerFreq1").html("1910.7");	
		$("#centerFreq2").html("1929.3");	
	}else if($("#u8FreqBandInd").val() == 38){
		$("#centerFreq1").html("2570.7");	
		$("#centerFreq2").html("2619.3");	
	}else if($("#u8FreqBandInd").val() == 39){
		$("#centerFreq1").html("1880.7");	
		$("#centerFreq2").html("1919.3");	
	}else if($("#u8FreqBandInd").val() == 40){
		$("#centerFreq1").html("2300.7");	
		$("#centerFreq2").html("2399.3");	
	}else if($("#u8FreqBandInd").val() == 45){
		$("#centerFreq1").html("1447.7");	
		$("#centerFreq2").html("1466.3");	
	}else if($("#u8FreqBandInd").val() == 53){
		$("#centerFreq1").html("778.7");	
		$("#centerFreq2").html("797.3");	
	}else if($("#u8FreqBandInd").val() == 58){
		$("#centerFreq1").html("380.7");	
		$("#centerFreq2").html("429.3");	
	}else if($("#u8FreqBandInd").val() == 59){
		$("#centerFreq1").html("1785.7");	
		$("#centerFreq2").html("1804.3");	
	}else if($("#u8FreqBandInd").val() == 61){
		$("#centerFreq1").html("1447.7");	
		$("#centerFreq2").html("1466.3");	
	}else if($("#u8FreqBandInd").val() == 62){
		$("#centerFreq1").html("1785.7");	
		$("#centerFreq2").html("1804.3");	
	}else if($("#u8FreqBandInd").val() == 63){
		$("#centerFreq1").html("526.7");	
		$("#centerFreq2").html("677.3");	
	}
	$("#u8FreqBandInd").change(function(){
		if($("#u8FreqBandInd").val() == 33){
			$("#centerFreq1").html("1900.7");	
			$("#centerFreq2").html("1919.3");	
		}else if($("#u8FreqBandInd").val() == 34){
			$("#centerFreq1").html("2010.7");
			$("#centerFreq2").html("2024.3");
		}else if($("#u8FreqBandInd").val() == 35){
			$("#centerFreq1").html("1850.7");	
			$("#centerFreq2").html("1909.3");	
		}else if($("#u8FreqBandInd").val() == 36){
			$("#centerFreq1").html("1930.7");	
			$("#centerFreq2").html("1989.3");	
		}else if($("#u8FreqBandInd").val() == 37){
			$("#centerFreq1").html("1910.7");	
			$("#centerFreq2").html("1929.3");	
		}else if($("#u8FreqBandInd").val() == 38){
			$("#centerFreq1").html("2570.7");	
			$("#centerFreq2").html("2619.3");	
		}else if($("#u8FreqBandInd").val() == 39){
			$("#centerFreq1").html("1880.7");	
			$("#centerFreq2").html("1919.3");	
		}else if($("#u8FreqBandInd").val() == 40){
			$("#centerFreq1").html("2300.7");	
			$("#centerFreq2").html("2399.3");	
		}else if($("#u8FreqBandInd").val() == 45){
			$("#centerFreq1").html("1447.7");	
			$("#centerFreq2").html("1466.3");	
		}else if($("#u8FreqBandInd").val() == 53){
			$("#centerFreq1").html("778.7");	
			$("#centerFreq2").html("797.3");	
		}else if($("#u8FreqBandInd").val() == 58){
			$("#centerFreq1").html("380.7");	
			$("#centerFreq2").html("429.3");	
		}else if($("#u8FreqBandInd").val() == 59){
			$("#centerFreq1").html("1785.7");	
			$("#centerFreq2").html("1804.3");	
		}else if($("#u8FreqBandInd").val() == 61){
			$("#centerFreq1").html("1447.7");	
			$("#centerFreq2").html("1466.3");	
		}else if($("#u8FreqBandInd").val() == 62){
			$("#centerFreq1").html("1785.7");	
			$("#centerFreq2").html("1804.3");	
		}else if($("#u8FreqBandInd").val() == 63){
			$("#centerFreq1").html("526.7");	
			$("#centerFreq2").html("677.3");	
		}
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8";
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
	//删除
	$("#t_enb_ctfreq1 tr").each(function(index){
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(20)").click(function(){
			var u8CfgIdx = $("#t_enb_ctfreq1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8&parameters="+para+"";
			}
		});					   
	});	
	//跳转到配置
	$("#t_enb_ctfreq1 tr").each(function(index){
		$("#t_enb_ctfreq1 tr:eq("+index+") td:eq(19)").click(function(){
			var u8CfgIdx = $("#t_enb_ctfreq1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8&parameters="+para+"&referTable=T_ENB_MEASCFG";
		});					   
	});	
	//跳转到配置
	$("#t_enb_ctfreq1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_ctfreq1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_ctfreq1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8&parameters="+para+"&referTable=T_ENB_MEASCFG";
			});	
		}
						   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_ctfreq1 input[type=checkbox]").each(function(index){
			if($("#t_enb_ctfreq1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_ctfreq1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_CTFREQ-3.0.8";
	});
	//
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