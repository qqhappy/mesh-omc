$(function(){
	// 表单校验
	var isNum=/^(-)?\d+$/;
	var isMa = /^[0-9]{3}$|^[0-9]{3}$|^[0-9]{3}$|^[0-9]{3}$|^[0-9]{3}$|^[0-9]{3}$|^[0-9]{3}$/; 
	var isMap=/^[0-3]{4}$/;
	var isStep=/^-?\d+(\.\d{1}){0,1}$/;
	var isOne=/^-?\d+(\.\d{1,3}){0,1}$/;
	var isCoco=/^[A-Za-z0-9\u4e00-\u9fa5_]+$/;
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
		}else if(u8FreqBandInd == 53){
			low = 778;
			offset = 64000;
		}else if(u8FreqBandInd == 58){
			low = 380;
			offset = 58200;
		}else if(u8FreqBandInd == 61){
			low = 1447;
			offset = 65000;
		}else if(u8FreqBandInd == 62){
			low = 1785;
			offset = 65200;
		}else{
			low = 606;
			offset = 59060;
		}
		var u32CenterFreq = parseFloat($("#u32CenterFreq").val());
		var u32CenterFreq1 = accAdd(accMul(accSub(u32CenterFreq,low),10),offset);
		$("#u32CenterFreq1").val(u32CenterFreq1);	
		
		//
		var au16AntCchWgtAmpltd = "";
		for(var i = 1;i<9;i++){
			var value = $("#au16AntCchWgtAmpltd"+i).val();
			var myValue = accMul(value,1000);
			var hexValue = parseInt(myValue,10).toString(16);
			var length = hexValue.length;
			if(length <4){
				for(var j = 0;j<4-length;j++){
					hexValue = "0" + hexValue;
				}
			}
			au16AntCchWgtAmpltd = au16AntCchWgtAmpltd + hexValue;
		}
		$("#au16AntCchWgtAmpltd").val(au16AntCchWgtAmpltd);
		
		//
		var au16AntSynWgtAmpltd = "";
		for(var i = 1;i<9;i++){
			var value = $("#au16AntSynWgtAmpltd"+i).val();
			var myValue = accMul(value,1000);
			var hexValue = parseInt(myValue,10).toString(16);
			var length = hexValue.length;
			if(length <4){
				for(var j = 0;j<4-length;j++){
					hexValue = "0" + hexValue;
				}
			}
			au16AntSynWgtAmpltd = au16AntSynWgtAmpltd + hexValue;
		}
		$("#au16AntSynWgtAmpltd").val(au16AntSynWgtAmpltd);
		//
		var au16AntCchWgtPhase = "";
		for(var i = 1;i<9;i++){
			var value = $("#au16AntCchWgtPhase"+i).val();
			var hexValue = parseInt(value,10).toString(16);
			var length = hexValue.length;
			if(length <4){
				for(var j = 0;j<4-length;j++){
					hexValue = "0" + hexValue;
				}
			}
			au16AntCchWgtPhase = au16AntCchWgtPhase + hexValue;
		}
		$("#au16AntCchWgtPhase").val(au16AntCchWgtPhase);
		//
		var au16AntSynWgtPhase = "";
		for(var i = 1;i<9;i++){
			var value = $("#au16AntSynWgtPhase"+i).val();
			var hexValue = parseInt(value,10).toString(16);
			var length = hexValue.length;
			if(length <4){
				for(var j = 0;j<4-length;j++){
					hexValue = "0" + hexValue;
				}
			}
			au16AntSynWgtPhase = au16AntSynWgtPhase + hexValue;
		}
		$("#au16AntSynWgtPhase").val(au16AntSynWgtPhase);
		//
		var au8UlAntUsdIdx = "";
		var au8UlAntUsdNum = 0;
		$(".ulAntCheckBox").each(function(){
			if($(this).attr("checked") == "checked"){
				au8UlAntUsdIdx = au8UlAntUsdIdx + "0"+ $(this).val();
				au8UlAntUsdNum++;
			}
		});
		
		var au8UlAntUsdIdxLength = au8UlAntUsdIdx.length;
		if(au8UlAntUsdIdxLength < 16){
			for(var i = 0;i<16-au8UlAntUsdIdxLength;i++){
				au8UlAntUsdIdx = au8UlAntUsdIdx + "0";
			}
		}
		//
		var au8DlAntUsdIdx = "";
		var au8DlAntUsdNum = 0;
		$(".dlAntCheckBox").each(function(){
			if($(this).attr("checked") == "checked"){
				au8DlAntUsdIdx = au8DlAntUsdIdx+ "0"+ $(this).val();
				au8DlAntUsdNum++;
			}
		});
		var au8DlAntUsdIdxLength = au8DlAntUsdIdx.length;
		if(au8DlAntUsdIdxLength < 16){
			for(var i = 0;i<16-au8DlAntUsdIdxLength;i++){
				au8DlAntUsdIdx = au8DlAntUsdIdx + "0";
			}
		}
		//
		var au8DlAntPortMap = "";
		$("#dlAntPortMapTable tr").each(function(index){
			if(index != 0){
				var binaryNum = "";
				$("#dlAntPortMapTable tr:eq("+index+") input").each(function(){
					if($(this).attr("checked") == "checked"){
						binaryNum = "1"+binaryNum;
					}else{
						binaryNum = "0"+binaryNum;
					}				
				});
				var hexNum = parseInt(binaryNum,2).toString(16);
				if(hexNum.length<2){
					hexNum = "0" +hexNum;
				}
				au8DlAntPortMap = au8DlAntPortMap + hexNum;
			}			
		});
		//清空错误信息
		$(".error").text("");
		var index = 0;
		if(!(isNum.test($("#u8CId").val()) && $("#u8CId").val()<=254  && $("#u8CId").val()>=0)){
			$("#u8CIdError").text("/* 请输入0~254之间的整数 */");
			index++;
		}
		if($("#u16TAC option").length < 1){
			$("#u16TACError").text("/* 没有可用的跟踪区码选项 */");
			index++;
		}
		var big = $("#centerFreq2").text();
		var small = $("#centerFreq1").text();	
		if(!(isStep.test($("#u32CenterFreq").val()) && $("#u32CenterFreq").val()<=parseFloat(big)  && $("#u32CenterFreq").val()>=parseFloat(small))){
			
			$("#u32CenterFreqError").text("/* 请输入符合要求的数字 */");
			index++;
		}	
		if(!(isNum.test($("#u16PhyCellId").val()) && $("#u16PhyCellId").val()<=503  && $("#u16PhyCellId").val()>=0)){
			$("#u16PhyCellIdError").text("/* 请输入0~503之间的整数 */");
			index++;
		}
		if($("#u8IntraFreqHOMeasCfg option").length<1){
			$("#u8IntraFreqHOMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u8A2ForInterFreqMeasCfg option").length<1){
			$("#u8A2ForInterFreqMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u8A1ForInterFreqMeasCfg option").length<1){
			$("#u8A1ForInterFreqMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u8A2ForRedirectMeasCfg option").length<1){
			$("#u8A2ForRedirectMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u16IntraFreqPrdMeasCfg option").length<1){
			$("#u16IntraFreqPrdMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u8IntraFreqPrdMeasCfg option").length<1){
			$("#u8IntraFreqPrdMeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if($("#u8IcicA3MeasCfg option").length<1){
			$("#u8IcicA3MeasCfgError").text("/* 没有可用的配置索引选项 */");
			index++;
		}
		if(!(isNum.test($("#u8sMeasure").val()) && $("#u8sMeasure").val()<=-44  && $("#u8sMeasure").val()>=-141)){
			$("#u8sMeasureError").text("/* 请输入-141~-44之间的整数 */");
			index++;
		}
		if(!(isNum.test($("#u8RedSrvCellRsrp").val()) && $("#u8RedSrvCellRsrp").val()<=-44  && $("#u8RedSrvCellRsrp").val()>=-141)){
			$("#u8RedSrvCellRsrpError").text("/* 请输入-141~-44之间的整数 */");
			index++;
		}
		if($("#u8TopoNO option").length<1){
			$("#u8TopoNOError").text("/* 没有可用的单板号选项 */");
			index++;
		}
		if(!($("input[name=u8NbrCellAntennaPort1]:checked").val() == 0 || $("input[name=u8NbrCellAntennaPort1]:checked").val() == 1)){
			$("#u8NbrCellAntennaPort1Error").text("/* 请进行选择 */");
			index++;
		}
		
		var au8CellLable = $("#au8CellLable").val() + "";
		var length = au8CellLable.length;
		if(length <1){
			$("#au8CellLableError").text("/* 请输入名称 */");
			index++;
		}else if(length >128){
			$("#au8CellLableError").text("/* 名称过长 */");
			index++;
		}else if(!isCoco.test(au8CellLable)){
			$("#au8CellLableError").text("/* 只可输入汉字、数字、字母或下划线 */");
			index++;
		}
		
		if(!((isOne.test($("#au16AntCchWgtAmpltd1").val()) && $("#au16AntCchWgtAmpltd1").val()>=0 && $("#au16AntCchWgtAmpltd1").val()<=1 )
&&(isOne.test($("#au16AntCchWgtAmpltd2").val()) && $("#au16AntCchWgtAmpltd2").val()>=0 && $("#au16AntCchWgtAmpltd2").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd3").val()) && $("#au16AntCchWgtAmpltd3").val()>=0 && $("#au16AntCchWgtAmpltd3").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd4").val()) && $("#au16AntCchWgtAmpltd4").val()>=0 && $("#au16AntCchWgtAmpltd4").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd5").val()) && $("#au16AntCchWgtAmpltd5").val()>=0 && $("#au16AntCchWgtAmpltd5").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd6").val()) && $("#au16AntCchWgtAmpltd6").val()>=0 && $("#au16AntCchWgtAmpltd6").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd7").val()) && $("#au16AntCchWgtAmpltd7").val()>=0 && $("#au16AntCchWgtAmpltd7").val()<=1 )&&
(isOne.test($("#au16AntCchWgtAmpltd8").val()) && $("#au16AntCchWgtAmpltd8").val()>=0 && $("#au16AntCchWgtAmpltd8").val()<=1 ))){
			$("#au16AntCchWgtAmpltdError").text("/* 每空0~1,步长0.001 */");
			index++;
		}
		if(!((isNum.test($("#au16AntCchWgtPhase1").val()) && $("#au16AntCchWgtPhase1").val()>=0 && $("#au16AntCchWgtPhase1").val()<=359 )
&&(isNum.test($("#au16AntCchWgtPhase2").val()) && $("#au16AntCchWgtPhase2").val()>=0 && $("#au16AntCchWgtPhase2").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase3").val()) && $("#au16AntCchWgtPhase3").val()>=0 && $("#au16AntCchWgtPhase3").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase4").val()) && $("#au16AntCchWgtPhase4").val()>=0 && $("#au16AntCchWgtPhase4").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase5").val()) && $("#au16AntCchWgtPhase5").val()>=0 && $("#au16AntCchWgtPhase5").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase6").val()) && $("#au16AntCchWgtPhase6").val()>=0 && $("#au16AntCchWgtPhase6").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase7").val()) && $("#au16AntCchWgtPhase7").val()>=0 && $("#au16AntCchWgtPhase7").val()<=359 )&&
(isNum.test($("#au16AntCchWgtPhase8").val()) && $("#au16AntCchWgtPhase8").val()>=0 && $("#au16AntCchWgtPhase8").val()<=359 ))){
			$("#au16AntCchWgtPhaseError").text("/* 每空为0~359之间的整数 */");
			index++;
		}
		if(!((isOne.test($("#au16AntSynWgtAmpltd1").val()) && $("#au16AntSynWgtAmpltd1").val()>=0 && $("#au16AntSynWgtAmpltd1").val()<=1 )
&&(isOne.test($("#au16AntSynWgtAmpltd2").val()) && $("#au16AntSynWgtAmpltd2").val()>=0 && $("#au16AntSynWgtAmpltd2").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd3").val()) && $("#au16AntSynWgtAmpltd3").val()>=0 && $("#au16AntSynWgtAmpltd3").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd4").val()) && $("#au16AntSynWgtAmpltd4").val()>=0 && $("#au16AntSynWgtAmpltd4").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd5").val()) && $("#au16AntSynWgtAmpltd5").val()>=0 && $("#au16AntSynWgtAmpltd5").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd6").val()) && $("#au16AntSynWgtAmpltd6").val()>=0 && $("#au16AntSynWgtAmpltd6").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd7").val()) && $("#au16AntSynWgtAmpltd7").val()>=0 && $("#au16AntSynWgtAmpltd7").val()<=1 )&&
(isOne.test($("#au16AntSynWgtAmpltd8").val()) && $("#au16AntSynWgtAmpltd8").val()>=0 && $("#au16AntSynWgtAmpltd8").val()<=1 ))){
			$("#au16AntSynWgtAmpltdError").text("/* 每空0~1,步长0.001 */");
			index++;
		}
		if(!((isNum.test($("#au16AntSynWgtPhase1").val()) && $("#au16AntSynWgtPhase1").val()>=0 && $("#au16AntSynWgtPhase1").val()<=359 )
&&(isNum.test($("#au16AntSynWgtPhase2").val()) && $("#au16AntSynWgtPhase2").val()>=0 && $("#au16AntSynWgtPhase2").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase3").val()) && $("#au16AntSynWgtPhase3").val()>=0 && $("#au16AntSynWgtPhase3").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase4").val()) && $("#au16AntSynWgtPhase4").val()>=0 && $("#au16AntSynWgtPhase4").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase5").val()) && $("#au16AntSynWgtPhase5").val()>=0 && $("#au16AntSynWgtPhase5").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase6").val()) && $("#au16AntSynWgtPhase6").val()>=0 && $("#au16AntSynWgtPhase6").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase7").val()) && $("#au16AntSynWgtPhase7").val()>=0 && $("#au16AntSynWgtPhase7").val()<=359 )&&
(isNum.test($("#au16AntSynWgtPhase8").val()) && $("#au16AntSynWgtPhase8").val()>=0 && $("#au16AntSynWgtPhase8").val()<=359 ))){
			$("#au16AntSynWgtPhaseError").text("/* 每空位0~359之间的整数 */");
			index++;
		}
		if((au8UlAntUsdNum%2 != 0 && au8UlAntUsdNum !=1) || au8UlAntUsdNum == 0){
			index++;
			$("#au8UlAntUsdIdxError").text("/* 必选选填1个,或2的倍数个天线 */");
		}
		if((au8DlAntUsdNum%2 != 0 && au8DlAntUsdNum !=1) || au8DlAntUsdNum == 0){
			index++;
			$("#au8DlAntUsdIdxError").text("/* 必选选填1个,或2的倍数个天线 */");
		}
		if($("#u8DlAntPortNum").val() > getAntUsdNum(au8DlAntUsdNum)){
			$("#u8DlAntPortNumError").text("/* 不可大于下行使能天线索引数 */");
			index++;
			return false;
		}
		var portmapCheckNum = 0;
		$("#dlAntPortMapTable tr").each(function(index){
			var checkedNum = 0;
			$("#dlAntPortMapTable tr:eq("+index+") td input").each(function(){
				if($(this).attr("checked") == "checked"){
					checkedNum++;
				}
			});
			if(checkedNum != 0){
				portmapCheckNum++;
			}
		});
		if($("#u8UeTransMode").val() != 0){
			if($("#u8DlAntPortNum").val() == 0){
				index++;
				$("#u8DlAntPortNumError").text("/* 传输模式不为tm1时,此项至少为port2 */");
			}
		}
		if(portmapCheckNum != getPortNum($("#u8DlAntPortNum").val())){
			index++;
			$("#au8DlAntPortMapError").text("/* 每个可用端口必须选择至少1个天线 */");
			return false;
		}
		
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u16TAC"+"="+$("#u16TAC").val()+";"
				+"au8MCC"+"="+$("#au8MCC").val()+";"
				+"au8MNC"+"="+$("#au8MNC").val()+";"
				+"au8CellLable"+"="+$("#au8CellLable").val()+";"
				+"u8FreqBandInd"+"="+$("#u8FreqBandInd").val()+";"
				+"u32CenterFreq"+"="+$("#u32CenterFreq1").val()+";"
				+"u8SysBandWidth"+"="+$("#u8SysBandWidth").val()+";"
				+"u8UlDlSlotAlloc"+"="+$("#u8UlDlSlotAlloc").val()+";"
				+"u8SpecSubFramePat"+"="+$("#u8SpecSubFramePat").val()+";"
				+"u16PhyCellId"+"="+$("#u16PhyCellId").val()+";"
				+"u8nB"+"="+$("#u8nB").val()+";"
				+"u8UlAntNum"+"="+$("#u8UlAntNum").val()+";"
				+"u8UlAntUsdNum"+"="+getAntUsdNum(au8UlAntUsdNum)+";"
				+"au8UlAntUsdIdx"+"="+au8UlAntUsdIdx+";"
				+"u8DlAntNum"+"="+$("#u8DlAntNum").val()+";"
				+"u8DlAntUsdNum"+"="+getAntUsdNum(au8DlAntUsdNum)+";"
				+"au8DlAntUsdIdx"+"="+au8DlAntUsdIdx+";"
				+"u8DlAntPortNum"+"="+$("#u8DlAntPortNum").val()+";"
				+"au8DlAntPortMap"+"="+au8DlAntPortMap+";"
				+"au16AntCchWgtAmpltd"+"="+$("#au16AntCchWgtAmpltd").val()+";"
				+"au16AntCchWgtPhase"+"="+$("#au16AntCchWgtPhase").val()+";"
				+"au16AntSynWgtAmpltd"+"="+$("#au16AntSynWgtAmpltd").val()+";"
				+"au16AntSynWgtPhase"+"="+$("#au16AntSynWgtPhase").val()+";"
				+"u8BcchModPrdPara"+"="+$("#u8BcchModPrdPara").val()+";"
				+"u8SIWindowLength"+"="+$("#u8SIWindowLength").val()+";"
				+"u8UeTransMode"+"="+$("#u8UeTransMode").val()+";"
				+"u8Ocs"+"="+$("#u8Ocs").val()+";"
				+"u8TimeAlignTimer"+"="+$("#u8TimeAlignTimer").val()+";"
				+"u8IntraFreqHOMeasCfg"+"="+$("#u8IntraFreqHOMeasCfg").val()+";"
				+"u8A2ForInterFreqMeasCfg"+"="+$("#u8A2ForInterFreqMeasCfg").val()+";"
				+"u8A1ForInterFreqMeasCfg"+"="+$("#u8A1ForInterFreqMeasCfg").val()+";"
				+"u8A2ForRedirectMeasCfg"+"="+$("#u8A2ForRedirectMeasCfg").val()+";"
				+"u8IntraFreqMeasBW"+"="+$("#u8IntraFreqMeasBW").val()+";"
				+"u8FilterCoeffRsrp"+"="+$("#u8FilterCoeffRsrp").val()+";"
				+"u8FilterCoeffRsrq"+"="+$("#u8FilterCoeffRsrq").val()+";"
				+"u8sMeasure"+"="+accAdd($("#u8sMeasure").val(),141)+";"
				+"u8NbrCellAntennaPort1"+"="+$("input[name='u8NbrCellAntennaPort1']:checked").val()+";"
				+"u8NbrCellConfig"+"="+$("#u8NbrCellConfig").val()+";"
				+"u16IntraFreqPrdMeasCfg"+"="+$("#u16IntraFreqPrdMeasCfg").val()+";"
				+"u8IntraFreqPrdMeasCfg"+"="+$("#u8IntraFreqPrdMeasCfg").val()+";"
				+"u8IcicA3MeasCfg"+"="+$("#u8IcicA3MeasCfg").val()+";"
				+"u8TopoNO"+"="+$("#u8TopoNO").val()+";"
				+"u8RedSrvCellRsrp"+"="+accAdd($("#u8RedSrvCellRsrp").val(),141)+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
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
							// BizException
							$(".error").text("");
							$(".error").removeAttr("title");
							var errorEntity = data.errorModel.errorEntity;							
							if (errorHtml.length >10){
								var errorHtmlLow = errorHtml.substr(0,15) + "...";
								var errorStar = "/* "+errorHtmlLow+" */";
								$("#"+errorEntity+"Error").text(errorStar);
								$("#"+errorEntity+"Error").attr("title",errorHtml);								
							}else{
								var errorStarTwo = "/* "+errorHtml+" */";
								$("#"+errorEntity+"Error").text(errorStarTwo);	
							}
						}else{
							// Exception
							$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();
						}	
					}else{
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7";
					}				
				}
			});	
		}
	});
	// 内存值转为显示值
	$("#t_cel_para1 tr").each(function(index){	
		//
		var u8FreqBandInd = parseInt($("#t_cel_para1 tr:eq("+index+") td:eq(5)").text());
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
		}else if(u8FreqBandInd == 53){
			low = 778;
			offset = 64000;
		}else if(u8FreqBandInd == 58){
			low = 380;
			offset = 58200;
		}else if(u8FreqBandInd == 61){
			low = 1447;
			offset = 65000;
		}else if(u8FreqBandInd == 62){
			low = 1785;
			offset = 65200;
		}else{
			low = 606;
			offset = 59060;
		}
		var u32CenterFreq = parseInt($("#t_cel_para1 tr:eq("+index+") td:eq(6)").text());
		var u32CenterFreq1 = accAdd(accDiv(accSub(u32CenterFreq,offset),10),low);
		$("#t_cel_para1 tr:eq("+index+") td:eq(6)").text(u32CenterFreq1);	
		
		// u8SysBandWidth
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("1.4M(6RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("3M(15RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("5M(25RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("10M(50RB)");
		}	
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("15M(75RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(7)").text("20M(100RB)");
		}
		// u8nB
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("4T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("2T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("1/2T");
		}	
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("1/4T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("1/8T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 6){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("1/16T");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(11)").text() == 7){
			$("#t_cel_para1 tr:eq("+index+") td:eq(11)").text("1/32T");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(12)").text("an1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(12)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(12)").text("an2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(12)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(12)").text("an4");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(12)").text("an8");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(13)").text("an1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(13)").text("an2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(13)").text("an4");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(13)").text("an8");
		}
		//
		var au8UlAntUsdIdx = $("#t_cel_para1 tr:eq("+index+") td:eq(14)").text().split("");
		var ulAntUsdIdxTdContent = "";
		for(var i = 0;i<au8UlAntUsdIdx.length;i=i+2){
			var arrayIndex = i+1;
			var span = "";
			if(au8UlAntUsdIdx[arrayIndex] != 0){
				span = "an" + au8UlAntUsdIdx[arrayIndex];
				ulAntUsdIdxTdContent = ulAntUsdIdxTdContent +  span + "-";
			}		
		}
		var ulAntUsdIdxTdContentResult  = ulAntUsdIdxTdContent.substring(0, ulAntUsdIdxTdContent.length-1);
		$("#t_cel_para1 tr:eq("+index+") td:eq(14)").text(ulAntUsdIdxTdContentResult);

		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(15)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(15)").text("an1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(15)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(15)").text("an2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(15)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(15)").text("an4");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(15)").text("an8");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(16)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(16)").text("an1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(16)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(16)").text("an2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(16)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(16)").text("an4");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(16)").text("an8");
		}
		//
		//
		var au8DlAntUsdIdx = $("#t_cel_para1 tr:eq("+index+") td:eq(17)").text().split("");
		var dlAntUsdIdxTdContent = "";
		for(var i = 0;i<au8DlAntUsdIdx.length;i=i+2){
			var arrayIndex = i+1;
			var span = "";
			if(au8DlAntUsdIdx[arrayIndex] != 0 ){
				span = "an" + au8DlAntUsdIdx[arrayIndex];
				dlAntUsdIdxTdContent = dlAntUsdIdxTdContent +  span + "-";
			}			
		}
		var dlAntUsdIdxTdContentResult  = dlAntUsdIdxTdContent.substring(0, dlAntUsdIdxTdContent.length-1);
		$("#t_cel_para1 tr:eq("+index+") td:eq(17)").text(dlAntUsdIdxTdContentResult);
		
	
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(18)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(18)").text("port1");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(18)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(18)").text("port2");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(18)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(18)").text("port4");
		}
		
		var au8DlAntPortMap = $("#t_cel_para1 tr:eq("+index+") td:eq(19)").text().split("");
		var str1 = au8DlAntPortMap[0] + au8DlAntPortMap[1] ;
		var str2 = au8DlAntPortMap[2] + au8DlAntPortMap[3] ;
		var str3 = au8DlAntPortMap[4] + au8DlAntPortMap[5] ;
		var str4 = au8DlAntPortMap[6] + au8DlAntPortMap[7] ;
		var no1 = parseInt(str1,16).toString(10);
		var no2 = parseInt(str2,16).toString(10);
		var no3 = parseInt(str3,16).toString(10);
		var no4 = parseInt(str4,16).toString(10);
		var result1 = no1 + " , " + no2 + " , " + no3 + " , " + no4;		
		$("#t_cel_para1 tr:eq("+index+") td:eq(19)").text(result1);
		//
		var au16AntCchWgtAmpltd = $("#t_cel_para1 tr:eq("+index+") td:eq(20)").text().split("");
		var coco1 = au16AntCchWgtAmpltd[0]+au16AntCchWgtAmpltd[1]+au16AntCchWgtAmpltd[2]+au16AntCchWgtAmpltd[3];
		var coco2 = au16AntCchWgtAmpltd[4]+au16AntCchWgtAmpltd[5]+au16AntCchWgtAmpltd[6]+au16AntCchWgtAmpltd[7];
		var coco3 = au16AntCchWgtAmpltd[8]+au16AntCchWgtAmpltd[9]+au16AntCchWgtAmpltd[10]+au16AntCchWgtAmpltd[11];
		var coco4 = au16AntCchWgtAmpltd[12]+au16AntCchWgtAmpltd[13]+au16AntCchWgtAmpltd[14]+au16AntCchWgtAmpltd[15];
		var coco5 = au16AntCchWgtAmpltd[16]+au16AntCchWgtAmpltd[17]+au16AntCchWgtAmpltd[18]+au16AntCchWgtAmpltd[19];
		var coco6 = au16AntCchWgtAmpltd[20]+au16AntCchWgtAmpltd[21]+au16AntCchWgtAmpltd[22]+au16AntCchWgtAmpltd[23];
		var coco7 = au16AntCchWgtAmpltd[24]+au16AntCchWgtAmpltd[25]+au16AntCchWgtAmpltd[26]+au16AntCchWgtAmpltd[27];
		var coco8 = au16AntCchWgtAmpltd[28]+au16AntCchWgtAmpltd[29]+au16AntCchWgtAmpltd[30]+au16AntCchWgtAmpltd[31];
		var bh1 = accDiv(parseInt(parseInt(coco1,16).toString(10)),1000);
		var bh2 = accDiv(parseInt(parseInt(coco2,16).toString(10)),1000);
		var bh3 = accDiv(parseInt(parseInt(coco3,16).toString(10)),1000);
		var bh4 = accDiv(parseInt(parseInt(coco4,16).toString(10)),1000);
		var bh5 = accDiv(parseInt(parseInt(coco5,16).toString(10)),1000);
		var bh6 = accDiv(parseInt(parseInt(coco6,16).toString(10)),1000);
		var bh7 = accDiv(parseInt(parseInt(coco7,16).toString(10)),1000);
		var bh8 = accDiv(parseInt(parseInt(coco8,16).toString(10)),1000);
		$("#t_cel_para1 tr:eq("+index+") td:eq(20)").text(bh1+" , "+bh2+" , "+bh3+" , "+bh4+" , "+bh5+" , "+bh6+" , "+bh7+" , "+bh8);
		//
		var au16AntCchWgtPhase = $("#t_cel_para1 tr:eq("+index+") td:eq(21)").text().split("");
		var cocoA1 = au16AntCchWgtPhase[0]+au16AntCchWgtPhase[1]+au16AntCchWgtPhase[2]+au16AntCchWgtPhase[3];
		var cocoA2 = au16AntCchWgtPhase[4]+au16AntCchWgtPhase[5]+au16AntCchWgtPhase[6]+au16AntCchWgtPhase[7];
		var cocoA3 = au16AntCchWgtPhase[8]+au16AntCchWgtPhase[9]+au16AntCchWgtPhase[10]+au16AntCchWgtPhase[11];
		var cocoA4 = au16AntCchWgtPhase[12]+au16AntCchWgtPhase[13]+au16AntCchWgtPhase[14]+au16AntCchWgtPhase[15];
		var cocoA5 = au16AntCchWgtPhase[16]+au16AntCchWgtPhase[17]+au16AntCchWgtPhase[18]+au16AntCchWgtPhase[19];
		var cocoA6 = au16AntCchWgtPhase[20]+au16AntCchWgtPhase[21]+au16AntCchWgtPhase[22]+au16AntCchWgtPhase[23];
		var cocoA7 = au16AntCchWgtPhase[24]+au16AntCchWgtPhase[25]+au16AntCchWgtPhase[26]+au16AntCchWgtPhase[27];
		var cocoA8 = au16AntCchWgtPhase[28]+au16AntCchWgtPhase[29]+au16AntCchWgtPhase[30]+au16AntCchWgtPhase[31];
		var bhA1 = parseInt(cocoA1,16).toString(10);
		var bhA2 = parseInt(cocoA2,16).toString(10);
		var bhA3 = parseInt(cocoA3,16).toString(10);
		var bhA4 = parseInt(cocoA4,16).toString(10);
		var bhA5 = parseInt(cocoA5,16).toString(10);
		var bhA6 = parseInt(cocoA6,16).toString(10);
		var bhA7 = parseInt(cocoA7,16).toString(10);
		var bhA8 = parseInt(cocoA8,16).toString(10);
		$("#t_cel_para1 tr:eq("+index+") td:eq(21)").text(bhA1+" , "+bhA2+" , "+bhA3+" , "+bhA4+" , "+bhA5+" , "+bhA6+" , "+bhA7+" , "+bhA8);
		//
		var au16AntSynWgtAmpltd = $("#t_cel_para1 tr:eq("+index+") td:eq(22)").text().split("");
		var cocoB1 = au16AntSynWgtAmpltd[0]+au16AntSynWgtAmpltd[1]+au16AntSynWgtAmpltd[2]+au16AntSynWgtAmpltd[3];
		var cocoB2 = au16AntSynWgtAmpltd[4]+au16AntSynWgtAmpltd[5]+au16AntSynWgtAmpltd[6]+au16AntSynWgtAmpltd[7];
		var cocoB3 = au16AntSynWgtAmpltd[8]+au16AntSynWgtAmpltd[9]+au16AntSynWgtAmpltd[10]+au16AntSynWgtAmpltd[11];
		var cocoB4 = au16AntSynWgtAmpltd[12]+au16AntSynWgtAmpltd[13]+au16AntSynWgtAmpltd[14]+au16AntSynWgtAmpltd[15];
		var cocoB5 = au16AntSynWgtAmpltd[16]+au16AntSynWgtAmpltd[17]+au16AntSynWgtAmpltd[18]+au16AntSynWgtAmpltd[19];
		var cocoB6 = au16AntSynWgtAmpltd[20]+au16AntSynWgtAmpltd[21]+au16AntSynWgtAmpltd[22]+au16AntSynWgtAmpltd[23];
		var cocoB7 = au16AntSynWgtAmpltd[24]+au16AntSynWgtAmpltd[25]+au16AntSynWgtAmpltd[26]+au16AntSynWgtAmpltd[27];
		var cocoB8 = au16AntSynWgtAmpltd[28]+au16AntSynWgtAmpltd[29]+au16AntSynWgtAmpltd[30]+au16AntSynWgtAmpltd[31];
		var bhB1 = accDiv(parseInt(parseInt(cocoB1,16).toString(10)),1000);
		var bhB2 = accDiv(parseInt(parseInt(cocoB2,16).toString(10)),1000);
		var bhB3 = accDiv(parseInt(parseInt(cocoB3,16).toString(10)),1000);
		var bhB4 = accDiv(parseInt(parseInt(cocoB4,16).toString(10)),1000);
		var bhB5 = accDiv(parseInt(parseInt(cocoB5,16).toString(10)),1000);
		var bhB6 = accDiv(parseInt(parseInt(cocoB6,16).toString(10)),1000);
		var bhB7 = accDiv(parseInt(parseInt(cocoB7,16).toString(10)),1000);
		var bhB8 = accDiv(parseInt(parseInt(cocoB8,16).toString(10)),1000);
		$("#t_cel_para1 tr:eq("+index+") td:eq(22)").text(bhB1+" , "+bhB2+" , "+bhB3+" , "+bhB4+" , "+bhB5+" , "+bhB6+" , "+bhB7+" , "+bhB8);
		//
		var au16AntSynWgtPhase = $("#t_cel_para1 tr:eq("+index+") td:eq(23)").text().split("");
		var cocoC1 = au16AntSynWgtPhase[0]+au16AntSynWgtPhase[1]+au16AntSynWgtPhase[2]+au16AntSynWgtPhase[3];
		var cocoC2 = au16AntSynWgtPhase[4]+au16AntSynWgtPhase[5]+au16AntSynWgtPhase[6]+au16AntSynWgtPhase[7];
		var cocoC3 = au16AntSynWgtPhase[8]+au16AntSynWgtPhase[9]+au16AntSynWgtPhase[10]+au16AntSynWgtPhase[11];
		var cocoC4 = au16AntSynWgtPhase[12]+au16AntSynWgtPhase[13]+au16AntSynWgtPhase[14]+au16AntSynWgtPhase[15];
		var cocoC5 = au16AntSynWgtPhase[16]+au16AntSynWgtPhase[17]+au16AntSynWgtPhase[18]+au16AntSynWgtPhase[19];
		var cocoC6 = au16AntSynWgtPhase[20]+au16AntSynWgtPhase[21]+au16AntSynWgtPhase[22]+au16AntSynWgtPhase[23];
		var cocoC7 = au16AntSynWgtPhase[24]+au16AntSynWgtPhase[25]+au16AntSynWgtPhase[26]+au16AntSynWgtPhase[27];
		var cocoC8 = au16AntSynWgtPhase[28]+au16AntSynWgtPhase[29]+au16AntSynWgtPhase[30]+au16AntSynWgtPhase[31];
		var bhC1 = parseInt(cocoC1,16).toString(10);
		var bhC2 = parseInt(cocoC2,16).toString(10);
		var bhC3 = parseInt(cocoC3,16).toString(10);
		var bhC4 = parseInt(cocoC4,16).toString(10);
		var bhC5 = parseInt(cocoC5,16).toString(10);
		var bhC6 = parseInt(cocoC6,16).toString(10);
		var bhC7 = parseInt(cocoC7,16).toString(10);
		var bhC8 = parseInt(cocoC8,16).toString(10);
		$("#t_cel_para1 tr:eq("+index+") td:eq(23)").text(bhC1+" , "+bhC2+" , "+bhC3+" , "+bhC4+" , "+bhC5+" , "+bhC6+" , "+bhC7+" , "+bhC8);
			
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(24)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(24)").text("2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(24)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(24)").text("4");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(24)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(24)").text("8");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(24)").text("16");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("5");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("10");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("15");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(25)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("20");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(25)").text("40");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(26)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(26)").text("tm1");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(26)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(26)").text("tm2");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(26)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(26)").text("tm3");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-24");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-22");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-20");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-18");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-16");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-14");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 6){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-12");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 7){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-10");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 8){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-8");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 9){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-6");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 10){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-5");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 11){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-4");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 12){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-3");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 13){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 14){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("-1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 15){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("0");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 16){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 17){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 18){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("3");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 19){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("4");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 20){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("5");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 21){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("6");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 22){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("8");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 23){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("10");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 24){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("12");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 25){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("14");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 26){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("16");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 27){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("18");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 28){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("20");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(27)").text() == 29){
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("22");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(27)").text("24");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("500");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("750");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("1280");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("1920");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("2560");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("5120");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 6){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("10240");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(28)").text() == 7){
			$("#t_cel_para1 tr:eq("+index+") td:eq(28)").text("正无穷");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("1.4M(6RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("3M(15RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("5M(25RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("10M(50RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("15M(75RB)");
		}
		if($("#t_cel_para1 tr:eq("+index+") td:eq(33)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(33)").text("20M(100RB)");
		}
		
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("0");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("3");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("4");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("5");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 6){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("6");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 7){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("7");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 8){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("8");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 9){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("9");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 10){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("11");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 11){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("13");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 12){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("15");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(34)").text() == 13){
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("17");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(34)").text("19");
		}
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("0");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 1){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("1");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 2){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("2");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 3){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("3");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 4){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("4");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 5){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("5");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 6){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("6");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 7){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("7");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 8){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("8");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 9){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("9");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 10){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("11");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 11){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("13");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 12){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("15");
		}else if($("#t_cel_para1 tr:eq("+index+") td:eq(35)").text() == 13){
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("17");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(35)").text("19");
		}
		
		
		var u8sMeasure = parseInt($("#t_cel_para1 tr:eq("+index+") td:eq(36)").text());
		var u8sMeasure1 = u8sMeasure - 141 ;
		$("#t_cel_para1 tr:eq("+index+") td:eq(36)").text(u8sMeasure1);
		//
		if($("#t_cel_para1 tr:eq("+index+") td:eq(37)").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td:eq(37)").text("否");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td:eq(37)").text("是");
		}
		// u8TopoNO
		var u8TopoNO = $("#t_cel_para1 tr:eq("+index+") td.u8TopoNO").text();
		var rruNum = parseInt(u8TopoNO)+parseInt(1);
		$("#t_cel_para1 tr:eq("+index+") td.u8TopoNO").text("RRU"+rruNum);
		
		// u8TopoNO
		var u8RedSrvCellRsrp = $("#t_cel_para1 tr:eq("+index+") td.u8RedSrvCellRsrp").text();
		var u8RedSrvCellRsrp_pValue = accSub(u8RedSrvCellRsrp,141);
		$("#t_cel_para1 tr:eq("+index+") td.u8RedSrvCellRsrp").text(u8RedSrvCellRsrp_pValue);
		
		// u8ManualOP
		if($("#t_cel_para1 tr:eq("+index+") td:.u8ManualOP").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td.u8ManualOP").text("解闭塞");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td.u8ManualOP").text("闭塞");
		}	
		// u32Status
		if($("#t_cel_para1 tr:eq("+index+") td.u32Status").text() == 0){
			$("#t_cel_para1 tr:eq("+index+") td.u32Status").text("正常");
		}else{
			$("#t_cel_para1 tr:eq("+index+") td.u32Status").text("不正常");
		}							  
	});	
	// 刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7";
	});
	// 全选
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
	// 跳转到配置
	$("#t_cel_para1 tr").each(function(index){
		$("#t_cel_para1 tr:eq("+index+") td:eq(46)").click(function(){
			var u8CId = $("#t_cel_para1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7&parameters="+para+"&referTable=T_ENB_MEASCFG&referTableTopo=T_TOPO";
		});					   
	});	
	// 跳转到配置
	$("#t_cel_para1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_para1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_para1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7&parameters="+para+"&referTable=T_ENB_MEASCFG&referTableTopo=T_TOPO";
			});	
		}						   
	});	
	// 删除
	$("#t_cel_para1 tr").each(function(index){
		$("#t_cel_para1 tr:eq("+index+") td:eq(48)").click(function(){
			var u8CId = $("#t_cel_para1 tr:eq("+index+") td:eq(0)").text();
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("与该小区相关的其他表记录也将删除，是否继续?")){
				var enbVersion = $("#enbVersion").val();
				$.ajax({
					type : "post",
					url : "deleteCellGuide.do",
					data:"moId="+moId+
						"&enbVersion="+enbVersion+
						"&cid="+u8CId,
					dataType : "json",
					async : false,
					success : function(data) {
						if(data.status == 0){
							window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select&tableName=T_CEL_PARA-3.0.7";
						}else{
							alert(data.error);
						}
					}
				});
			}
		});					   
	});	
	// 批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_para1 input[type=checkbox]").each(function(index){
			if($("#t_cel_para1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_para1 tr:eq("+index+") td:eq(0)").text();
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
			if(confirm("与所选小区相关的其他表记录也将删除，是否继续?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7&parameters="+para+"";
			}
		}	
	});
	// 取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PARA-3.0.7";
	});
	
});
//加
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
//减
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
//乘
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
//除
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
function getAntUsdNum(num){
	if(num == 1){
		return 0;
	}else if(num == 2){
		return 1;
	}else if(num == 4){
		return 2;
	}else{
		return 3;
	}
}
function getPortNum(num){
	if(num == 0){
		return 1;
	}else if(num == 1){
		return 2;
	}else{
		return 4;
	}
}

