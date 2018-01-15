function modifyCellGuide(){
	var index = checkParameters();
	if(index == 0){
		var moId = $("#moId").val();
		var enbVersion = $("#enbVersion").val();
		var cel_info_para = createCellInfoPara();
		$.ajax({
			type : "post",
			url : "modifyCellGuide.do",
			data:"enbVersion="+enbVersion
				+"&moId="+moId
				+cel_info_para,
			dataType : "json",
			async : false,
			success : function(data) {
				if(data.status == 0){
					cancelModify();
				}else{
					$("#error").html(data.error);
				}
			}
		});
	}
}


function addCellGuide(){
	var index = checkParameters();
	if(index == 0){
		var moId = $("#moId").val();
		var enbVersion = $("#enbVersion").val();
		var topo = $("#u8TopoNO").val();
		var board_para = createBoardPara(topo);
		var cel_info_para = createCellInfoPara();
		$.ajax({
			type : "post",
			url : "addCellGuide.do",
			data:"enbVersion="+enbVersion
				+"&moId="+moId
				+"&fiberPort="+topo
				+"&parameters="+board_para+cel_info_para,
			dataType : "json",
			async : false,
			success : function(data) {
				if(data.status == 0){
//					var basePath = $("#basePath").val();
//					var roleId = $("#roleId").val();
//					var enbVersion = $("#enbVersion").val();
//					var enbHexId = $("#enbHexId").val();
//					$("#mainFrame",parent.document).attr("src",""+basePath+"/lte/queryWholeStatus.do?moId="+moId+"&enbHexId="+enbHexId);
//					$("#leftFrame",parent.document).attr("src",""+basePath+"/lte/turnEnbWebLmtLeft.do?moId="+moId+"&enbHexId="+enbHexId+"&fromAlarm=0&roleId="+roleId+"&enbVersion="+enbVersion);
					cancelModify();
				}else{
					$("#error").html(data.error);
				}
			}
		});
	}
}
// 校验参数
function checkParameters(){
	var index = 0;
	$(".para_warn_li").html("");
	$("#error").html("");
	var isNum = /^(-)?[0-9]+$/;
	var isFloat = /^-?\d+(\.\d{1}){0,1}$/;
	// 小区标识
	if(!checkInputInForm(isNum,"u8CId",254,0)){
		index++;
		$("#u8CId").parents("li:first").siblings(".para_warn_li").html(dynamicInfo(10000,generateArgments_i18n_num(254,0)));
	}
	// 频点
	var centerFreq_max = $("#centerFreq_max").html();
	var centerFreq_min = $("#centerFreq_min").html();
	if(!checkInputInForm(isFloat,"u32CenterFreq",parseFloat(centerFreq_max),parseFloat(centerFreq_min))){
		index++;
		$("#u32CenterFreq").parents("li:first").siblings(".para_warn_li").html(dynamicInfo(10001,generateArgments_i18n_num(centerFreq_max,centerFreq_min)));
	}
	// 小区名称
	var au8CellLable = $("#au8CellLable").val() + "";
	var length = au8CellLable.length;
	var isCharactor=/^[A-Za-z0-9\u4e00-\u9fa5_]+$/;
	if(length <1){
		$("#au8CellLable").parents("li:first").siblings(".para_warn_li").text("/* 请输入名称 */");
		index++;
	}else if(length >128){
		$("#au8CellLable").parents("li:first").siblings(".para_warn_li").text("/* 名称过长 */");
		index++;
	}else if(!isCharactor.test(au8CellLable)){
		$("#au8CellLable").parents("li:first").siblings(".para_warn_li").text("/* 只可输入汉字、数字、字母或下划线 */");
		index++;
	}
	//跟踪区码
	if($("#u16TAC option").length<1){
		$("#u16TAC").parents("li:first").siblings(".para_warn_li").html("/* 尚无可用的跟踪区码 */");
	}
	//RRU单板
	if($("#u8TopoNO option").length<1){
		$("#u8TopoNO").parents("li:first").siblings(".para_warn_li").html("/* 已无可用的RRU单板 */");
	}
	// 物理小区标识
	if(!checkInputInForm(isNum,"u16PhyCellId",503,0)){
		index++;
		$("#u16PhyCellId").parents("li:first").siblings(".para_warn_li").html(dynamicInfo(10000,generateArgments_i18n_num(503,0)));
	}
	// 逻辑根序列
	if(!checkInputInForm(isNum,"u16RootSeqIndex",837,0)){
		index++;
		$("#u16RootSeqIndex").parents("li:first").siblings(".para_warn_li").html(dynamicInfo(10000,generateArgments_i18n_num(837,0)));
	}
	return index;
}

// 为小区参数表查询跟踪区码列表，并填入dom
function queryTaForCel() {
	$.ajax({
		type : "post",
		url : "queryAllTa.do",
		dataType : "json",
		async : false,
		success : function(data) {
			var mcc = "";
			var mnc = "";
			if (data.status == 0) {
				if (data.message.length > 0) {
					var option = "";
					for ( var i = 0; i < data.message.length; i++) {
						option = option + '<option value="'
								+ data.message[i].code + '">'
								+ data.message[i].code + '</option>';
					}
					$("#u16TAC").html(option);
				}
			}
		}
	});
}
// 查询空闲物理小区标识并填入dom
function getFreeU16PhyCellId(){
	var moId = $("#moId").val();
	var enbVersion = $("#enbVersion").val();
	$.ajax({
		type : "post",
		url : "getFreePci.do",
		data:"enbVersion="+enbVersion
			+"&moId="+moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				$("#u16PhyCellId").val(data.message);
			}else{
				alert(data.error);
			}
		}
	});
}
// 查询逻辑根序列并填入dom
function getFreeU16RootSeqIndex(){
	var moId = parseInt($("#moId").val());
	var enbVersion = $("#enbVersion").val();
	$.ajax({
		type : "post",
		url : "getFreeRsi.do",
		data:"enbVersion="+enbVersion
		+"&moId="+moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				$("#u16RootSeqIndex").val(data.message);
			}else{
				alert(data.error);
			}
		}
	});
}
// 根据频段获取频点上边界
function getUpperBoundary(u8FreqBandInd){
	if(u8FreqBandInd == 33){
		return 1920;
	}else if(u8FreqBandInd == 34){
		return 2025;
	}else if(u8FreqBandInd == 35){
		return 1910;
	}else if(u8FreqBandInd == 36){
		return 1990;
	}else if(u8FreqBandInd == 37){
		return 1930;
	}else if(u8FreqBandInd == 38){
		return 2620;
	}else if(u8FreqBandInd == 39){
		return 1920;
	}else if(u8FreqBandInd == 40){
		return 2400;
	}else if(u8FreqBandInd == 53){
		return 798;
	}else if(u8FreqBandInd == 58){	
		return 430;
	}else if(u8FreqBandInd == 61){
		return 1467;
	}else if(u8FreqBandInd == 62){
		return 1805;
	}else{
		return 678;
	}
}
// 根据频段获取频点下边界
function getLowerBoundary(u8FreqBandInd){
	if(u8FreqBandInd == 33){
		return 1900;
	}else if(u8FreqBandInd == 34){
		return 2010;
	}else if(u8FreqBandInd == 35){
		return 1850;
	}else if(u8FreqBandInd == 36){
		return 1930;
	}else if(u8FreqBandInd == 37){
		return 1910;
	}else if(u8FreqBandInd == 38){
		return 2570;
	}else if(u8FreqBandInd == 39){	
		return 1880;
	}else if(u8FreqBandInd == 40){
		return 2300;
	}else if(u8FreqBandInd == 53){	
		return 778;
	}else if(u8FreqBandInd == 58){
		return 380;
	}else if(u8FreqBandInd == 61){
		return 1447;
	}else if(u8FreqBandInd == 62){
		return 1785;
	}else{	
		return 606;
	}
}
// 获取带宽
function getSysBandWidth(u8SysBandWidth){
	if(u8SysBandWidth == 0){
		return  1.4;
	}else if(u8SysBandWidth == 1){
		return  3;
	}else if(u8SysBandWidth == 2){
		return  5;
	}else if(u8SysBandWidth == 3){
		return  10;
	}else if(u8SysBandWidth == 4){
		return  15;
	}else{
		return  20;
	}
}
// 根据频段及带宽获取频点最大值
function getMaxBoundary(u8FreqBandInd,u8SysBandWidth){
	var upperBoundary = getUpperBoundary(u8FreqBandInd);
	var SysBandWidth = getSysBandWidth(u8SysBandWidth);
	var result = upperBoundary-accDiv(SysBandWidth,2);
	return result.toFixed(1);

}
// 根据频段及带宽获取频点最小值
function getMinBoundary(u8FreqBandInd,u8SysBandWidth){
	var lowerBoundary = getLowerBoundary(u8FreqBandInd);
	var SysBandWidth = getSysBandWidth(u8SysBandWidth);
	var result = accAdd(lowerBoundary,accDiv(SysBandWidth,2));
	return result.toFixed(1);
}
// 频段指示内显示详细频点范围
function setu8FreqBandIndOption(u8SysBandWidth){
	$("#u8FreqBandInd option").each(function(){
		var u8FreqBandInd = $(this).val();
		var maxBoundary = getMaxBoundary(u8FreqBandInd,u8SysBandWidth);
		var minBoundary = getMinBoundary(u8FreqBandInd,u8SysBandWidth);
		var optioHtml = $(this).val();	
		optioHtml = optioHtml+" ("+minBoundary+"~"+maxBoundary+")";
		$(this).html(optioHtml);
	});
}
function getCenterFreq_value(flag,u8FreqBandInd,u32CenterFreq){
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
	var centerFreq = parseFloat(u32CenterFreq);
	var result = "";
	if(flag == 1){
		// 内存值
		result =  accAdd(accMul(accSub(centerFreq,low),10),offset);
	}
	if(flag == 2){
		// 显示值
		result =  accAdd(accDiv(accSub(centerFreq,offset),10),low);
	}
	return result;
}
//根据topo号生成新增RRU和TOPO表记录所需要的参数
function createBoardPara(topo){
	var u8RackNO = 2+parseInt(topo);
	var para = "u8RackNO="+u8RackNO+";"
				+"u8ShelfNO=1;"
				+"u8SlotNO=1;"
				+"u8BDType=2;"
				+"u8RadioMode=1;"
				+"u8ManualOP=0;"
				+"u32Status=1";	
	return para;
}
function createCellInfoPara(){
	var u32CenterFreq = getCenterFreq_value(1,$("#u8FreqBandInd").val(),$("#u32CenterFreq").val());
	return "&enbCellStart.cid="+$("#u8CId").val()
			+"&enbCellStart.sceneId="+$("#scene").val()
			+"&enbCellStart.freqBandId="+$("#u8FreqBandInd").val()
			+"&enbCellStart.bandwidthId="+$("#u8SysBandWidth").val()
			+"&enbCellStart.centerFreq="+u32CenterFreq
			+"&enbCellStart.sfCfgId="+$("#u8UlDlSlotAlloc").val()
			+"&enbCellStart.rruTypeId="+$("#rruType").val()
			+"&enbCellStart.anNumId="+$("#anNum").val()
			+"&enbCellStart.tac="+$("#u16TAC").val()
			+"&enbCellStart.cellName="+$("#au8CellLable").val()
			+"&enbCellStart.phyCellId="+$("#u16PhyCellId").val()
			+"&enbCellStart.topoNO="+$("#u8TopoNO").val()
			+"&enbCellStart.rootSeqIndex="+$("#u16RootSeqIndex").val()
			+"&enbCellStart.manualOP=0";
}
function cancelAdd(){
	var basePath = $("#basePath").val();
	window.location.href = basePath+"/lte/queryEnbList.do?operType=select";
}
function cancelModify(){
	
	var basePath = $("#basePath").val();
	var enbVersion = $("#enbVersion").val();
	var moId = $("#moId").val();
	var enbHexId= $("#enbHexId").val();
	window.location.href = basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=select&enbHexId="+enbHexId+"&enbVersion="+enbVersion+"&tableName=T_CEL_PARA-"+enbVersion;
}
//function getCelParaVersion(enbVersion){
//	var enbType=$("#enbType").val();
//	if(enbType == 0){
//		if(enbVersion >= "3.0.2"){
//			return "-3.0.2";
//		}else if(enbVersion >= "2.1.5"){
//			return "-2.1.5";
//		}else{
//			return "";
//		}
//	}else{
//		return "-1.1.5";
//	}
//	
//}
