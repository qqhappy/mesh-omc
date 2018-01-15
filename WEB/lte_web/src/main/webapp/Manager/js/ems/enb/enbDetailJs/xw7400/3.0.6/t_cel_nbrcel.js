$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	var isStep=/^-?\d+(\.\d{1}){0,1}$/;
	var isEnbId = /^[a-zA-Z0-9]{1,}$/;
	$("#submit_add").click(function(){
		var moId = $("#moId").val();
		//
		var index = 0;
		if($("#u8SvrCID option").length<1){
			$("#u8SvrCIDError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8SvrCIDError").text("");
		}
		
		var u32eNBId = $("#u32NbreNBID").val()+"";
		var u32eNBIdLen = u32eNBId.length;
		if(u32eNBIdLen!=8 || !(isEnbId.test(u32eNBId)) || parseInt(u32eNBId,16) > 1048575){
			$("#u32NbreNBIDError").text("/* 8位，00000000~000FFFFF */");	
			index++;
		}
//		else if(getEnbHexId(moId) == u32eNBId){
//			index++;
//			$("#u32NbreNBIDError").text("/* 不可为本eNB标识 */");	
//		}
		else{
			$("#u32NbreNBIDError").text("");
		}
		
		if(!(isNum.test($("#u8NbrCID").val()) && $("#u8NbrCID").val()<=255  && $("#u8NbrCID").val()>=0)){
			$("#u8NbrCIDError").text("/* 请输入合法的邻小区标识 */");
			index++;
		}else{
			$("#u8NbrCIDError").text("");
		}
		var au8MCC = $("#au8MCC").val();
		if(!isNum.test(au8MCC) || au8MCC.length != 3){
			$("#au8MCCError").text("/* 请输入三位数字 */");
			index++;
		}else{
			$("#au8MCCError").text("");
		}
		var myMcc = "";
		for(var i = 0;i<au8MCC.length;i++){
			myMcc = myMcc + "0" + au8MCC[i];
		}
		var au8MNC = $("#au8MNC").val();
		if(!isNum.test(au8MNC) || !(au8MNC.length == 3 || au8MNC.length == 2)){
			$("#au8MNCError").text("/* 请输入两位或三位数字 */");
			index++;
		}else{
			$("#au8MNCError").text("");
		}
		var myMnc = "";
		for(var i = 0;i<au8MNC.length;i++){
			myMnc = myMnc + "0" + au8MNC[i];
		}
		if(myMnc.length <6){
			myMnc = myMnc + "ff";
		}
		if(!(isNum.test($("#u16TAC").val()) && $("#u16TAC").val()<=65535 && $("#u16TAC").val()>=0)){
			$("#u16TACError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16TACError").text("");
		}		
		if(!(isNum.test($("#u16PhyCellId").val()) && $("#u16PhyCellId").val()<=503  && $("#u16PhyCellId").val()>=0)){
			$("#u16PhyCellIdError").text("/* 请输入0~503之间的整数 */");
			index++;
		}else{
			$("#u16PhyCellIdError").text("");
		}
		if(!(isNum.test($("#u8CenterFreqCfgIdx").val()) && $("#u8CenterFreqCfgIdx").val()<=255  && $("#u8CenterFreqCfgIdx").val()>=0)){
			$("#u8CenterFreqCfgIdxError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8CenterFreqCfgIdxError").text("");
		}
		
		var para = "u8SvrCID"+"="+$("#u8SvrCID").val()+";"
				+"u32NbreNBID"+"="+parseInt(u32eNBId,16)+";"
				+"u8NbrCID"+"="+$("#u8NbrCID").val()+";"
				+"au8MCC"+"="+myMcc+";"
				+"au8MNC"+"="+myMnc+";"
				+"u16PhyCellId"+"="+$("#u16PhyCellId").val()+";"
				+"u16TAC"+"="+$("#u16TAC").val()+";"
				+"u8QOffsetCell"+"="+$("#u8QOffsetCell").val()+";"
				+"u8CellIndividualOffset"+"="+$("#u8CellIndividualOffset").val()+";"
				+"u8NoRemove"+"="+$("input[name='u8NoRemove']:checked").val()+";"
				+"u8NoHO"+"="+$("input[name='u8NoHO']:checked").val()+";"
				+"u8CenterFreqCfgIdx"+"="+$("#u8CenterFreqCfgIdx").val();
		$("#parameters").val(para);
		if(index==0){

			var moId = $("#moId").val();
			var basePath=$("#basePath").val();
			var tableName=$("input[name='tableName']").val();
			var operType=$("input[name='operType']").val();
			var enbVersion = $("#enbVersion").val();
			var isNeighbour = $("input[name='isNeighbour']:checked").val();
			$.ajax({
				type:"post",
				url:"checkNbrcelBizRules.do",
				data:"moId="+moId+
					"&basePath="+basePath+
					"&browseTime="+getBrowseTime()+"&tableName="+tableName+
					"&operType="+operType+
					"&enbVersion="+enbVersion+
					"&isNeighbour="+isNeighbour+
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
							$("#resultError").html("error: "+errorHtml);
						}
					}else{
						window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
					}				
				}
			});	
		}
	});
	//改变邻小区ID
	$("#u8NbrCID").live("change",function(){
		var u32NbreNBIDFlag = $("#u32NbreNBIDFlag").val(); 
		if(u32NbreNBIDFlag == 1){
			var u8NbrCID = $(this).val();
			var enbHexId= $("#u32NbreNBID").val();
			var cellRecord = getCellDataByCellId(enbHexId,u8NbrCID);
			var mcc = cellRecord.fieldMap["au8MCC"].value;
			var mnc = cellRecord.fieldMap["au8MNC"].value;
			var u16PhyCellId = cellRecord.fieldMap["u16PhyCellId"].value;
			var u16TAC = cellRecord.fieldMap["u16TAC"].value;
			//
			$("#au8MCC").val(mcc[1]+mcc[3]+mcc[5]);
			if(mnc[5] == "f" || mnc[5] == "F"){
				$("#au8MNC").val(mnc[1]+mnc[3]);
			}else{
				$("#au8MNC").val(mnc[1]+mnc[3]+mnc[5]);
			}
			$("#au8MCC").attr("disabled","disabled");
			$("#au8MNC").attr("disabled","disabled");
			$("#u16PhyCellId").val(u16PhyCellId);
			$("#u16PhyCellId").attr("disabled","disabled");
			$("#u16TAC").val(u16TAC);
			$("#u16TAC").attr("disabled","disabled");
		}
	});
	//内存值转为显示值
	//u32Status
	$("#t_cel_nbrcel1 tr").each(function(index){
		var u32eNBId = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text();
		var u32eNBIdHex = parseInt(u32eNBId).toString(16) + "";
		var lengthCo = u32eNBIdHex.length;
		if(lengthCo <8){
			for(var i=0 ;i<8-lengthCo ;i++){
				u32eNBIdHex = "0" + u32eNBIdHex;	
			}	
		}
		$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text(u32eNBIdHex);
		//
		if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-24");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-22");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-20");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-18");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-16");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-14");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 6){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-12");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 7){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-10");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 8){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-8");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 9){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-6");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 10){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-5");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 11){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-4");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 12){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-3");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 13){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-2");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 14){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("-1");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 15){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("0");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 16){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("1");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 17){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("2");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 18){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("3");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 19){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("4");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 20){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("5");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 21){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("6");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 22){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("8");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 23){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("10");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 24){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("12");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 25){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("14");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 26){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("16");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 27){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("18");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 28){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("20");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text() == 29){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("22");
		}else{
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(7)").text("24");
		}
		//
		if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-24");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-22");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-20");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 3){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-18");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 4){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-16");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 5){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-14");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 6){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-12");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 7){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-10");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 8){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-8");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 9){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-6");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 10){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-5");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 11){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-4");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 12){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-3");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 13){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-2");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 14){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("-1");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 15){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("0");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 16){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("1");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 17){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("2");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 18){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("3");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 19){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("4");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 20){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("5");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 21){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("6");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 22){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("8");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 23){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("10");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 24){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("12");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 25){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("14");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 26){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("16");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 27){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("18");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 28){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("20");
		}else if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text() == 29){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("22");
		}else{
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(8)").text("24");
		}		
		//
		if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(9)").text("否");
		}else{
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(9)").text("是");
		}
		//
		if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(10)").text() == 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(10)").text("否");
		}else{
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(10)").text("是");
		}
		
		//
		if($("#t_cel_nbrcel1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(12)").text("否");
		}else{
			$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(12)").text("是");
		}
		//
			
		//
		
		var au8MCC = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(3)").text().split("");
		$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(3)").text(au8MCC[1]+au8MCC[3]+au8MCC[5]);
		
		//
		var au8MNC = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(4)").text().split("");
		var myMnc = "";
		if(au8MNC.length == 4){
			myMnc = au8MNC[1]+au8MNC[3];
		}else{
			if(au8MNC[5] == "f" || au8MNC[5] == "F"){
				myMnc = au8MNC[1]+au8MNC[3];
			}else{
				myMnc = au8MNC[1]+au8MNC[3]+au8MNC[5];
			}			
		}
		$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(4)").text(myMnc);
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
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
	$("#t_cel_nbrcel1 tr").each(function(index){
		$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(13)").click(function(){
			var u8SvrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(0)").text();
			var u32NbreNBID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text();
			var u32NbreNBID1 = parseInt(u32NbreNBID,16);
			var u8NbrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(2)").text();
			var para = "u8SvrCID"+"="+u8SvrCID+";"
					+"u32NbreNBID"+"="+u32NbreNBID1+";"
					+"u8NbrCID"+"="+u8NbrCID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/querySingleNbrcelRecord.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_nbrcel1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_nbrcel1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8SvrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(0)").text();
				var u32NbreNBID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text();
				var u32NbreNBID1 = parseInt(u32NbreNBID,16);
				var u8NbrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(2)").text();
				var para = "u8SvrCID"+"="+u8SvrCID+";"
						+"u32NbreNBID"+"="+u32NbreNBID1+";"
						+"u8NbrCID"+"="+u8NbrCID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/querySingleNbrcelRecord.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_cel_nbrcel1 tr").each(function(index){
		$("#t_cel_nbrcel1 tr:eq("+index+") td:eq(14)").click(function(){
			var u8SvrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(0)").text();
			var u32NbreNBID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text();
			var u32NbreNBID1 = parseInt(u32NbreNBID,16);
			var u8NbrCID = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(2)").text();
			var para = "u8SvrCID"+"="+u8SvrCID+";"
					+"u32NbreNBID"+"="+u32NbreNBID1+";"
					+"u8NbrCID"+"="+u8NbrCID;
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			var basePath = $("#basePath").val();
			if(confirm("确定要删除该条记录?")){				
				$.ajax({
					type:"post",
					url:"checkNbrcelBizRules.do",
					data:"moId="+moId+
						"&operType=delete"+
						"&enbVersion="+enbVersion+
						"&parameters="+para,
					dataType:"json",
					async:false,
					success:function(data){
						var errorHtml = data.errorModel.error;
						if(errorHtml == null || errorHtml == ""){
							window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
						}else{
							alert(errorHtml);
						}
					}
				}); 				
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str1=[];
		var str2=[];
		var str3=[];
		$("#t_cel_nbrcel1 input[type=checkbox]").each(function(index){
			if($("#t_cel_nbrcel1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(0)").text();
				var temp2 = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(1)").text();
				var str = "";
				if(isEnbId.test(temp2)){
					str = parseInt(temp2,16);
				}
				var temp3 = $("#t_cel_nbrcel1 tr:eq("+index+") td:eq(2)").text();
				str1.push(temp1);
				str2.push(str);
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
			var s = "u8SvrCID"+"="+str1[i]+","
						+"u32NbreNBID"+"="+str2[i]+","
						+"u8NbrCID"+"="+str3[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				$.ajax({
					type:"post",
					url:"checkNbrcelBizRules.do",
					data:"moId="+moId+
						"&operType=multiDelete"+
						"&enbVersion="+enbVersion+
						"&parameters="+para,
					dataType:"json",
					async:false,
					success:function(data){
						var errorHtml = data.errorModel.error;
						if(errorHtml == null || errorHtml == ""){
							window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
						}else{
							alert(errorHtml);
						}
					}
				}); 	
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryNbrcelRecords.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_NBRCEL-3.0.6";
	});
});
function isExistingInSystem(enbHexId){
	var flag = 0;
	$.ajax({
		type:"post",
		url:"isExistingInSystem.do",
		data:"enbHexId="+enbHexId,
		dataType:"json",
		async:false,
		success:function(data){
			flag = data.flag;
		}
	});
	return flag;
}
function getCellListByEnbHexId(enbHexId){
	var cellList;
	$.ajax({
		type:"post",
		url:"getCellListByEnbHexId.do",
		data:"enbHexId="+enbHexId,
		dataType:"json",
		async:false,
		success:function(data){
			cellList = data.cellList;
		}
	});
	return cellList;
}
function getCellDataByCellId(enbHexId,cellId){
	var record;
	var parameters = "u8CId="+cellId;
	$.ajax({
		type:"post",
		url:"getCellDataByCellId.do",
		data:"enbHexId="+enbHexId+
			"&parameters="+parameters,
		dataType:"json",
		async:false,
		success:function(data){
			record = data.record;
		}
	});
	return record;
}
function getEnbHexId(moId){
	var enbHexId;
	$.ajax({
		type:"post",
		url:"getEnbHexIdAsync.do",
		data:"moId="+moId,
		dataType:"json",
		async:false,
		success:function(data){
			enbHexId = data.enbHexId;
		}
	});
	return enbHexId;
}