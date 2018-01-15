$(function(){	   
	//
	var u8sMeasure = parseInt($("#u8sMeasureAAA").val());
	var u8sMeasure1 = u8sMeasure - 141;
	$("#u8sMeasure").val(u8sMeasure1);
	
	//u8FreqBandInd
	$("#u8FreqBandInd").val($("#u8FreqBandIndAAA").val());
	//
	var u8FreqBandInd = parseInt($("#u8FreqBandIndAAA").val());
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
	var u32CenterFreq = parseInt($("#u32CenterFreqAAA").val());
	var u32CenterFreq2 =accAdd(accDiv(accSub(u32CenterFreq,offset),10),low);
	$("#u32CenterFreq").val(u32CenterFreq2);
	
		   
		  
	var au8UlAntUsdIdx = $("#au8UlAntUsdIdxAAA").val().split("");
	//au8UlAntUsdIdx
	for(var i = 1;i<9;i++){
		for(var j = 0;j<8;j++){
			if(i == au8UlAntUsdIdx[2*j+1]){
				$("#au8UlAntUsdIdx"+i).attr("checked","checked");
			}
		}
	}
	//
	var au8DlAntUsdIdx = $("#au8DlAntUsdIdxAAA").val().split("");
	//au8UlAntUsdIdx
	for(var i = 1;i<9;i++){
		for(var j = 0;j<8;j++){
			if(i == au8DlAntUsdIdx[2*j+1]){
				$("#au8DlAntUsdIdx"+i).attr("checked","checked");
			}
		}
	}
	//au8DlAntPortMap
	var au8DlAntPortMap = $("#au8DlAntPortMapAAA").val().split("");
	var au8DlAntPortMap1 = au8DlAntPortMap[0]+au8DlAntPortMap[1];
	var au8DlAntPortMap2 = au8DlAntPortMap[2]+au8DlAntPortMap[3];
	var au8DlAntPortMap3 = au8DlAntPortMap[4]+au8DlAntPortMap[5];
	var au8DlAntPortMap4 = au8DlAntPortMap[6]+au8DlAntPortMap[7];
	
	for(var i = 0;i<au8DlAntPortMap.length;i=i+2){
		var portmap = au8DlAntPortMap[i]+au8DlAntPortMap[i+1];
		binaryPortmap = parseInt(portmap,16).toString(2);
		var length = binaryPortmap.length;
		if(length < 8){
			for(var j = 0;j<8-length;j++){
				binaryPortmap = "0" + binaryPortmap;
			}
		}
		var value = (i/2) + 1;
		var array  = binaryPortmap.split("");
		$("#dlAntPortMapTable tr:eq("+value+") input").each(function(index){
			if(binaryPortmap[7-index] == "1" || binaryPortmap[7-index] == 1){
				$(this).attr("checked","checked");
			}else{
				$(this).removeAttr("checked");
			}
		});
	}
	
	
	//au16AntCchWgtAmpltd
	var au16AntCchWgtAmpltd = $("#au16AntCchWgtAmpltd").val().split("");
	var au16AntCchWgtAmpltd1 = au16AntCchWgtAmpltd[0]+au16AntCchWgtAmpltd[1]+au16AntCchWgtAmpltd[2]+au16AntCchWgtAmpltd[3];
	var au16AntCchWgtAmpltd2 = au16AntCchWgtAmpltd[4]+au16AntCchWgtAmpltd[5]+au16AntCchWgtAmpltd[6]+au16AntCchWgtAmpltd[7];
	var au16AntCchWgtAmpltd3 = au16AntCchWgtAmpltd[8]+au16AntCchWgtAmpltd[9]+au16AntCchWgtAmpltd[10]+au16AntCchWgtAmpltd[11];
	var au16AntCchWgtAmpltd4 = au16AntCchWgtAmpltd[12]+au16AntCchWgtAmpltd[13]+au16AntCchWgtAmpltd[14]+au16AntCchWgtAmpltd[15];
	var au16AntCchWgtAmpltd5 = au16AntCchWgtAmpltd[16]+au16AntCchWgtAmpltd[17]+au16AntCchWgtAmpltd[18]+au16AntCchWgtAmpltd[19];
	var au16AntCchWgtAmpltd6 = au16AntCchWgtAmpltd[20]+au16AntCchWgtAmpltd[21]+au16AntCchWgtAmpltd[22]+au16AntCchWgtAmpltd[23];
	var au16AntCchWgtAmpltd7 = au16AntCchWgtAmpltd[24]+au16AntCchWgtAmpltd[25]+au16AntCchWgtAmpltd[26]+au16AntCchWgtAmpltd[27];
	var au16AntCchWgtAmpltd8 = au16AntCchWgtAmpltd[28]+au16AntCchWgtAmpltd[29]+au16AntCchWgtAmpltd[30]+au16AntCchWgtAmpltd[31];
	$("#au16AntCchWgtAmpltd1").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd1,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd2").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd2,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd3").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd3,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd4").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd4,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd5").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd5,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd6").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd6,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd7").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd7,16).toString(10)),1000));
	$("#au16AntCchWgtAmpltd8").val(accDiv(parseInt(parseInt(au16AntCchWgtAmpltd8,16).toString(10)),1000));
	//au16AntSynWgtAmpltd
	var au16AntSynWgtAmpltd = $("#au16AntSynWgtAmpltd").val().split("");
	var au16AntSynWgtAmpltd1 = au16AntSynWgtAmpltd[0]+au16AntSynWgtAmpltd[1]+au16AntSynWgtAmpltd[2]+au16AntSynWgtAmpltd[3];
	var au16AntSynWgtAmpltd2 = au16AntSynWgtAmpltd[4]+au16AntSynWgtAmpltd[5]+au16AntSynWgtAmpltd[6]+au16AntSynWgtAmpltd[7];
	var au16AntSynWgtAmpltd3 = au16AntSynWgtAmpltd[8]+au16AntSynWgtAmpltd[9]+au16AntSynWgtAmpltd[10]+au16AntSynWgtAmpltd[11];
	var au16AntSynWgtAmpltd4 = au16AntSynWgtAmpltd[12]+au16AntSynWgtAmpltd[13]+au16AntSynWgtAmpltd[14]+au16AntSynWgtAmpltd[15];
	var au16AntSynWgtAmpltd5 = au16AntSynWgtAmpltd[16]+au16AntSynWgtAmpltd[17]+au16AntSynWgtAmpltd[18]+au16AntSynWgtAmpltd[19];
	var au16AntSynWgtAmpltd6 = au16AntSynWgtAmpltd[20]+au16AntSynWgtAmpltd[21]+au16AntSynWgtAmpltd[22]+au16AntSynWgtAmpltd[23];
	var au16AntSynWgtAmpltd7 = au16AntSynWgtAmpltd[24]+au16AntSynWgtAmpltd[25]+au16AntSynWgtAmpltd[26]+au16AntSynWgtAmpltd[27];
	var au16AntSynWgtAmpltd8 = au16AntSynWgtAmpltd[28]+au16AntSynWgtAmpltd[29]+au16AntSynWgtAmpltd[30]+au16AntSynWgtAmpltd[31];
	$("#au16AntSynWgtAmpltd1").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd1,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd2").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd2,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd3").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd3,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd4").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd4,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd5").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd5,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd6").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd6,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd7").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd7,16).toString(10)),1000));
	$("#au16AntSynWgtAmpltd8").val(accDiv(parseInt(parseInt(au16AntSynWgtAmpltd8,16).toString(10)),1000));
	//au16AntCchWgtPhase
	var au16AntCchWgtPhase = $("#au16AntCchWgtPhase").val().split("");
	var au16AntCchWgtPhase1 = au16AntCchWgtPhase[0]+au16AntCchWgtPhase[1]+au16AntCchWgtPhase[2]+au16AntCchWgtPhase[3];
	var au16AntCchWgtPhase2 = au16AntCchWgtPhase[4]+au16AntCchWgtPhase[5]+au16AntCchWgtPhase[6]+au16AntCchWgtPhase[7];
	var au16AntCchWgtPhase3 = au16AntCchWgtPhase[8]+au16AntCchWgtPhase[9]+au16AntCchWgtPhase[10]+au16AntCchWgtPhase[11];
	var au16AntCchWgtPhase4 = au16AntCchWgtPhase[12]+au16AntCchWgtPhase[13]+au16AntCchWgtPhase[14]+au16AntCchWgtPhase[15];
	var au16AntCchWgtPhase5 = au16AntCchWgtPhase[16]+au16AntCchWgtPhase[17]+au16AntCchWgtPhase[18]+au16AntCchWgtPhase[19];
	var au16AntCchWgtPhase6 = au16AntCchWgtPhase[20]+au16AntCchWgtPhase[21]+au16AntCchWgtPhase[22]+au16AntCchWgtPhase[23];
	var au16AntCchWgtPhase7 = au16AntCchWgtPhase[24]+au16AntCchWgtPhase[25]+au16AntCchWgtPhase[26]+au16AntCchWgtPhase[27];
	var au16AntCchWgtPhase8 = au16AntCchWgtPhase[28]+au16AntCchWgtPhase[29]+au16AntCchWgtPhase[30]+au16AntCchWgtPhase[31];
	$("#au16AntCchWgtPhase1").val(parseInt(parseInt(au16AntCchWgtPhase1,16).toString(10)));
	$("#au16AntCchWgtPhase2").val(parseInt(parseInt(au16AntCchWgtPhase2,16).toString(10)));
	$("#au16AntCchWgtPhase3").val(parseInt(parseInt(au16AntCchWgtPhase3,16).toString(10)));
	$("#au16AntCchWgtPhase4").val(parseInt(parseInt(au16AntCchWgtPhase4,16).toString(10)));
	$("#au16AntCchWgtPhase5").val(parseInt(parseInt(au16AntCchWgtPhase5,16).toString(10)));
	$("#au16AntCchWgtPhase6").val(parseInt(parseInt(au16AntCchWgtPhase6,16).toString(10)));
	$("#au16AntCchWgtPhase7").val(parseInt(parseInt(au16AntCchWgtPhase7,16).toString(10)));
	$("#au16AntCchWgtPhase8").val(parseInt(parseInt(au16AntCchWgtPhase8,16).toString(10)));
	//au16AntSynWgtPhase
	var au16AntSynWgtPhase = $("#au16AntSynWgtPhase").val().split("");
	var au16AntSynWgtPhase1 = au16AntSynWgtPhase[0]+au16AntSynWgtPhase[1]+au16AntSynWgtPhase[2]+au16AntSynWgtPhase[3];
	var au16AntSynWgtPhase2 = au16AntSynWgtPhase[4]+au16AntSynWgtPhase[5]+au16AntSynWgtPhase[6]+au16AntSynWgtPhase[7];
	var au16AntSynWgtPhase3 = au16AntSynWgtPhase[8]+au16AntSynWgtPhase[9]+au16AntSynWgtPhase[10]+au16AntSynWgtPhase[11];
	var au16AntSynWgtPhase4 = au16AntSynWgtPhase[12]+au16AntSynWgtPhase[13]+au16AntSynWgtPhase[14]+au16AntSynWgtPhase[15];
	var au16AntSynWgtPhase5 = au16AntSynWgtPhase[16]+au16AntSynWgtPhase[17]+au16AntSynWgtPhase[18]+au16AntSynWgtPhase[19];
	var au16AntSynWgtPhase6 = au16AntSynWgtPhase[20]+au16AntSynWgtPhase[21]+au16AntSynWgtPhase[22]+au16AntSynWgtPhase[23];
	var au16AntSynWgtPhase7 = au16AntSynWgtPhase[24]+au16AntSynWgtPhase[25]+au16AntSynWgtPhase[26]+au16AntSynWgtPhase[27];
	var au16AntSynWgtPhase8 = au16AntSynWgtPhase[28]+au16AntSynWgtPhase[29]+au16AntSynWgtPhase[30]+au16AntSynWgtPhase[31];
	$("#au16AntSynWgtPhase1").val(parseInt(parseInt(au16AntSynWgtPhase1,16).toString(10)));
	$("#au16AntSynWgtPhase2").val(parseInt(parseInt(au16AntSynWgtPhase2,16).toString(10)));
	$("#au16AntSynWgtPhase3").val(parseInt(parseInt(au16AntSynWgtPhase3,16).toString(10)));
	$("#au16AntSynWgtPhase4").val(parseInt(parseInt(au16AntSynWgtPhase4,16).toString(10)));
	$("#au16AntSynWgtPhase5").val(parseInt(parseInt(au16AntSynWgtPhase5,16).toString(10)));
	$("#au16AntSynWgtPhase6").val(parseInt(parseInt(au16AntSynWgtPhase6,16).toString(10)));
	$("#au16AntSynWgtPhase7").val(parseInt(parseInt(au16AntSynWgtPhase7,16).toString(10)));
	$("#au16AntSynWgtPhase8").val(parseInt(parseInt(au16AntSynWgtPhase8,16).toString(10)));
	//
	//u8NbrCellAntennaPort1
	for(var i=1;i<3;i++){
		if($("#radioFour"+i).val() == $("#u8NbrCellAntennaPort1AAA").val()){
			$("#radioFour"+i).attr("checked","checked");
		}
	}
	//u8UlAntUsdNum
	$("#u8UlAntUsdNum").val($("#u8UlAntUsdNumAAA").val());
	//u8DlAntNum
	$("#u8DlAntNum").val($("#u8DlAntNumAAA").val());
	//u8DlAntUsdNum
	$("#u8DlAntUsdNum").val($("#u8DlAntUsdNumAAA").val());	
	//u8IntraFreqMeasBW
	$("#u8IntraFreqMeasBW").val($("#u8IntraFreqMeasBWAAA").val());
	//u8FilterCoeffRsrp
	$("#u8FilterCoeffRsrp").val($("#u8FilterCoeffRsrpAAA").val());
	//u8FilterCoeffRsrq
	$("#u8FilterCoeffRsrq").val($("#u8FilterCoeffRsrqAAA").val());
	//u8DlAntPortNum
	$("#u8DlAntPortNum").val($("#u8DlAntPortNumAAA").val());
	//u8NbrCellConfig
	$("#u8NbrCellConfig").val($("#u8NbrCellConfigAAA").val());
	//u8SysBandWidth
	$("#u8SysBandWidth").val($("#u8SysBandWidthAAA").val());
	//u8BcchModPrdPara
	$("#u8BcchModPrdPara").val($("#u8BcchModPrdParaAAA").val());
	//u8UlDlSlotAlloc
	$("#u8UlDlSlotAlloc").val($("#u8UlDlSlotAllocAAA").val());
	//u8SIWindowLength
	$("#u8SIWindowLength").val($("#u8SIWindowLengthAAA").val());
	//u8SpecSubFramePat
	$("#u8SpecSubFramePat").val($("#u8SpecSubFramePatAAA").val());
	//u8UeTransMode
	$("#u8UeTransMode").val($("#u8UeTransModeAAA").val());
	//u8AdmitUeMaxErabNum
	if(typeof($("#u8AdmitUeMaxErabNumAAA").val()) != "undefined" && $("#u8AdmitUeMaxErabNumAAA").val() != null){
		$("#u8AdmitUeMaxErabNum").val($("#u8AdmitUeMaxErabNumAAA").val());
	}
	//u8nB
	$("#u8nB").val($("#u8nBAAA").val());
	//u8Ocs
	$("#u8Ocs").val($("#u8OcsAAA").val());
	//u8TimeAlignTimer
	$("#u8TimeAlignTimer").val($("#u8TimeAlignTimerAAA").val());
	//u8UlAntNum
	$("#u8UlAntNum").val($("#u8UlAntNumAAA").val());	 
});