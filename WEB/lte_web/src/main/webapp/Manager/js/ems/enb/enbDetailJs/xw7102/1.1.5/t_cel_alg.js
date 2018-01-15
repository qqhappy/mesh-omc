$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	var isLit = /^(-)?\d+(\.[0-9]{1}){0,1}$/;
	$("#submit_add").click(function(){
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if(!(isNum.test($("#u8UlRbNum").val()) && $("#u8UlRbNum").val()<=100  && $("#u8UlRbNum").val()>=1)){
			$("#u8UlRbNumError").text("/* 请输入1~100之间的整数 */");
			index++;
		}else{
			$("#u8UlRbNumError").text("");
		}
		if(!(isNum.test($("#u8DlRbNum").val()) && $("#u8DlRbNum").val()<=100  && $("#u8DlRbNum").val()>=1)){
			$("#u8DlRbNumError").text("/* 请输入1~100之间的整数 */");
			index++;
		}else{
			$("#u8DlRbNumError").text("");
		}
		if(!(isNum.test($("#u8UlMaxMcs").val()) && $("#u8UlMaxMcs").val()<=28  && $("#u8UlMaxMcs").val()>=0)){
			$("#u8UlMaxMcsError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else{
			$("#u8UlMaxMcsError").text("");
		}
		if(!(isNum.test($("#u8UlMinMcs").val()) && $("#u8UlMinMcs").val()<=28  && $("#u8UlMinMcs").val()>=0)){
			$("#u8UlMinMcsError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else if($("#u8UlMinMcs").val() > $("#u8UlMaxMcs").val()){
			$("#u8UlMinMcsError").text("/* 不可大于上行最大MCS值 */");
			index++;
		}else{
			$("#u8UlMinMcsError").text("");
		}
		if(!(isNum.test($("#u8DlMaxMcs").val()) && $("#u8DlMaxMcs").val()<=28  && $("#u8DlMaxMcs").val()>=0)){
			$("#u8DlMaxMcsError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else{
			$("#u8DlMaxMcsError").text("");
		}
		if(!(isNum.test($("#u8DlMinMcs").val()) && $("#u8DlMinMcs").val()<=28  && $("#u8DlMinMcs").val()>=0)){
			$("#u8DlMinMcsError").text("/* 请输入0~28之间的整数 */");
			index++;
		}else if($("#u8DlMinMcs").val() > $("#u8DlMaxMcs").val()){
			$("#u8DlMinMcsError").text("/* 不可大于下行最大MCS值 */");
			index++;
		}else{
			$("#u8DlMinMcsError").text("");
		}
		if(!(isNum.test($("#u8UlMaxRbNum").val()) && $("#u8UlMaxRbNum").val()<=100  && $("#u8UlMaxRbNum").val()>=0)){
			$("#u8UlMaxRbNumError").text("/* 请输入0~100之间的整数 */");
			index++;
		}else{
			$("#u8UlMaxRbNumError").text("");
		}
		if(!(isNum.test($("#u8UlMinRbNum").val()) && $("#u8UlMinRbNum").val()<=100  && $("#u8UlMinRbNum").val()>=0)){
			$("#u8UlMinRbNumError").text("/* 请输入0~100之间的整数 */");
			index++;
		}else if($("#u8UlMinRbNum").val() > $("#u8UlMaxRbNum").val()){
			$("#u8UlMinRbNumError").text("/* 不可大于上行最大分配RB数 */");
			index++;
		}else{
			$("#u8UlMinRbNumError").text("");
		}
		if(!(isNum.test($("#u8DlMaxRbNum").val()) && $("#u8DlMaxRbNum").val()<=100  && $("#u8DlMaxRbNum").val()>=0)){
			$("#u8DlMaxRbNumError").text("/* 请输入0~100之间的整数 */");
			index++;
		}else{
			$("#u8DlMaxRbNumError").text("");
		}
		if(!(isNum.test($("#u8DlMinRbNum").val()) && $("#u8DlMinRbNum").val()<=100  && $("#u8DlMinRbNum").val()>=0)){
			$("#u8DlMinRbNumError").text("/* 请输入0~100之间的整数 */");
			index++;
		}else if($("#u8DlMinRbNum").val() > $("#u8DlMaxRbNum").val()){
			$("#u8DlMinRbNumError").text("/* 不可大于下行最大分配RB数 */");
			index++;
		}else{
			$("#u8DlMinRbNumError").text("");
		}
		if(!(isNum.test($("#u16RbNiThresh").val()) && $("#u16RbNiThresh").val()<=420  && $("#u16RbNiThresh").val()>=-600)){
			$("#u16RbNiThreshError").text("/* 请输入-600~420之间的整数 */");
			index++;
		}else{
			$("#u16RbNiThreshError").text("");
		}
		if(!(isNum.test($("#u32AlgReserved").val()) && $("#u32AlgReserved").val()<=4294967295  && $("#u32AlgReserved").val()>=0)){
			$("#u32AlgReservedError").text("/* 请输入0~4294967295之间的整数 */");
			index++;
		}else{
			$("#u32AlgReservedError").text("");
		}
		//取得au8DlCebBitmap值
		var au8DlCebBitmapBin = "";
		for(var i = 0;i<32;i++){
			var value = $("#dlBitMapId"+i).val();
			if(value == 1){
				for(var j = i;j<32;j++){
					
				}
			}
			if($("#dlBitMapId"+i).attr("checked") == "checked"){
				au8DlCebBitmapBin = au8DlCebBitmapBin + "1";
			}else{
				au8DlCebBitmapBin = au8DlCebBitmapBin + "0";
			}
		}
		var au8DlCebBitmapBinStr = au8DlCebBitmapBin.split("");
		var au8DlCebBitmapBinLength = au8DlCebBitmapBinStr.length;
		var au8DlCebBitmapHex = "";
		for(var i = 0;i<au8DlCebBitmapBinLength;i=i+8){
			var value = "";
			for(var j = 0;j<8;j++){
				value = value + au8DlCebBitmapBinStr[i+j];
			}
			var hexValue = parseInt(value,2).toString(16);
			if(hexValue.length <2){
				hexValue = "0" + hexValue;
			}
			au8DlCebBitmapHex = au8DlCebBitmapHex + hexValue;
		}
		$("#au8DlCebBitmap").val(au8DlCebBitmapHex);
		
		//取得au8UlCebBitmap值
		var au8UlCebBitmapBin = "";
		for(var i = 0;i<104;i++){
			if($("#ulBitMapId"+i).attr("checked") == "checked"){
				au8UlCebBitmapBin = au8UlCebBitmapBin + "1";
			}else{
				au8UlCebBitmapBin = au8UlCebBitmapBin + "0";
			}
		}
		var au8UlCebBitmapBinStr = au8UlCebBitmapBin.split("");
		var au8UlCebBitmapBinLength = au8UlCebBitmapBinStr.length;
		var au8UlCebBitmapHex = "";
		for(var i = 0;i<au8UlCebBitmapBinLength;i=i+8){
			var value = "";
			for(var j = 0;j<8;j++){
				value = value + au8UlCebBitmapBinStr[i+j];
			}
			var hexValue = parseInt(value,2).toString(16);
			if(hexValue.length <2){
				hexValue = "0" + hexValue;
			}
			au8UlCebBitmapHex = au8UlCebBitmapHex + hexValue;
		}
		$("#au8UlCebBitmap").val(au8UlCebBitmapHex);
	
		//校验au8DlCebBitmap
		if($("input[name='b8DlIcicSwitch']:checked").val() == 1){
			if(au8DlCebBitmapHex == "00000000" || au8DlCebBitmapHex == "ffffff80"){
				$("#au8DlCebBitmapError").text("DL ICIC算法开关打开时,此项不可全选或全不选");
				index++;
				return;
			}else{
				$("#au8DlCebBitmapError").text("");
			}
		}else{
			$("#au8DlCebBitmapError").text("");
		}
		//非0紧凑排列
		var dMapArray = [];
		for(var i=0;i<25;i++){
			if($("#dlBitMapId"+i).attr("checked")){
				dMapArray.push(i);
			}
		}
		var dMapArrayLen = dMapArray.length;
		if(dMapArrayLen>1){
			for(var i=dMapArray[0];i<dMapArray[dMapArrayLen-1];i++){
				if(!$("#dlBitMapId"+i).attr("checked")){
					$("#au8DlCebBitmapError").text("/* 非零选项必须紧凑排列 */");
					index++;
					return;
				}else{
					$("#au8DlCebBitmapError").text("");
				}
			}
		}else{
			$("#au8DlCebBitmapError").text("");
		}
		//校验au8UlCebBitmap
		if($("input[name='b8UlIcicSwitch']:checked").val() == 1){
			if(au8UlCebBitmapHex == "00000000000000000000000000" || au8UlCebBitmapHex == "fffffffffffffffffffffffff0"){
				$("#au8UlCebBitmapError").text("UL ICIC算法开关打开时,此项不可全选或全不选");
				index++;
				return;
			}else{
				$("#au8UlCebBitmapError").text("");
			}
		}else{
			$("#au8UlCebBitmapError").text("");
		}
		//非0紧凑排列
		var uMapArray = [];
		for(var i=0;i<100;i++){
			if($("#ulBitMapId"+i).attr("checked")){
				uMapArray.push(i);
			}
		}
		var uMapArrayLen = uMapArray.length;
		if(uMapArrayLen>1){
			for(var i=uMapArray[0];i<uMapArray[uMapArrayLen-1];i++){
				if(!$("#ulBitMapId"+i).attr("checked")){
					$("#au8UlCebBitmapError").text("/* 非零选项必须紧凑排列 */");
					index++;
					return;
				}else{
					$("#au8UlCebBitmapError").text("");
				}
			}
		}else{
			$("#au8UlCebBitmapError").text("");
		}
		
		
//		var au8DlCebBitmap = getCheckBoxValues("dlBitMap", 25);
//		au8DlCebBitmap = "0000000" + au8DlCebBitmap;
//		au8DlCebBitmap = convertToHexString(au8DlCebBitmap);
//		$("#au8DlCebBitmap").val(au8DlCebBitmap);
//		
//		var au8UlCebBitmap = getCheckBoxValues("ulBitMap", 100);
//		au8UlCebBitmap = "0000" + au8UlCebBitmap;
//		au8UlCebBitmap = convertToHexString(au8UlCebBitmap);
//		$("#au8UlCebBitmap").val(au8UlCebBitmap);
		
		var ab8UlSubfrmFlag1 = "0"+$("input[name='ab8UlSubfrmFlag1']:checked").val();
		var ab8UlSubfrmFlag2 = "0"+$("input[name='ab8UlSubfrmFlag2']:checked").val();
		var ab8UlSubfrmFlag3 = "0"+$("input[name='ab8UlSubfrmFlag3']:checked").val();
		var ab8UlSubfrmFlag4 = "0"+$("input[name='ab8UlSubfrmFlag4']:checked").val();
		var ab8UlSubfrmFlag5 = "0"+$("input[name='ab8UlSubfrmFlag5']:checked").val();
		var ab8UlSubfrmFlag6 = "0"+$("input[name='ab8UlSubfrmFlag6']:checked").val();
		var ab8UlSubfrmFlag7 = "0"+$("input[name='ab8UlSubfrmFlag7']:checked").val();
		var ab8UlSubfrmFlag8 = "0"+$("input[name='ab8UlSubfrmFlag8']:checked").val();
		var ab8UlSubfrmFlag9 = "0"+$("input[name='ab8UlSubfrmFlag9']:checked").val();
		var ab8UlSubfrmFlag10 = "0"+$("input[name='ab8UlSubfrmFlag10']:checked").val();
		var ab8UlSubfrmFlag = ab8UlSubfrmFlag1 +  ab8UlSubfrmFlag2 +  ab8UlSubfrmFlag3 +  ab8UlSubfrmFlag4 
						+  ab8UlSubfrmFlag5 +  ab8UlSubfrmFlag6 +  ab8UlSubfrmFlag7 +  ab8UlSubfrmFlag8 +  ab8UlSubfrmFlag9 +  ab8UlSubfrmFlag10;
		var ab8DlSubfrmFlag1 = "0"+$("input[name='ab8DlSubfrmFlag1']:checked").val();
		var ab8DlSubfrmFlag2 = "0"+$("input[name='ab8DlSubfrmFlag2']:checked").val();
		var ab8DlSubfrmFlag3 = "0"+$("input[name='ab8DlSubfrmFlag3']:checked").val();
		var ab8DlSubfrmFlag4 = "0"+$("input[name='ab8DlSubfrmFlag4']:checked").val();
		var ab8DlSubfrmFlag5 = "0"+$("input[name='ab8DlSubfrmFlag5']:checked").val();
		var ab8DlSubfrmFlag6 = "0"+$("input[name='ab8DlSubfrmFlag6']:checked").val();
		var ab8DlSubfrmFlag7 = "0"+$("input[name='ab8DlSubfrmFlag7']:checked").val();
		var ab8DlSubfrmFlag8 = "0"+$("input[name='ab8DlSubfrmFlag8']:checked").val();
		var ab8DlSubfrmFlag9 = "0"+$("input[name='ab8DlSubfrmFlag9']:checked").val();
		var ab8DlSubfrmFlag10 = "0"+$("input[name='ab8DlSubfrmFlag10']:checked").val();
		var ab8DlSubfrmFlag = ab8DlSubfrmFlag1 +  ab8DlSubfrmFlag2 +  ab8DlSubfrmFlag3 +  ab8DlSubfrmFlag4 
						+  ab8DlSubfrmFlag5 +  ab8DlSubfrmFlag6 +  ab8DlSubfrmFlag7 +  ab8DlSubfrmFlag8 +  ab8DlSubfrmFlag9 +  ab8DlSubfrmFlag10;
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"b8RlfEnable"+"="+$("input[name='b8RlfEnable']:checked").val()+";"
				+"b8UlPreSchEnable"+"="+$("input[name='b8UlPreSchEnable']:checked").val()+";"
				+"u8UlRbNum"+"="+$("#u8UlRbNum").val()+";"
				+"u8UlAmcTargetBler"+"="+$("#u8UlAmcTargetBler").val()+";"
				+"u8DlAmcTargetBler"+"="+$("#u8DlAmcTargetBler").val()+";"
				+"ab8UlSubfrmFlag"+"="+ab8UlSubfrmFlag+";"
				+"b8DlPreSchEnable"+"="+$("input[name='b8DlPreSchEnable']:checked").val()+";"
				+"u8DlRbNum"+"="+$("#u8DlRbNum").val()+";"
				+"ab8DlSubfrmFlag"+"="+ab8DlSubfrmFlag+";"
				+"b8UlAmcEnable"+"="+$("input[name='b8UlAmcEnable']:checked").val()+";"
				+"u8UlMaxMcs"+"="+$("#u8UlMaxMcs").val()+";"
				+"u8UlMinMcs"+"="+$("#u8UlMinMcs").val()+";"
				+"b8DlAmcEnable"+"="+$("input[name='b8DlAmcEnable']:checked").val()+";"
				+"u8TS"+"="+$("#u8TS").val()+";"
				+"u8DlMaxMcs"+"="+$("#u8DlMaxMcs").val()+";"
				+"u8DlMinMcs"+"="+$("#u8DlMinMcs").val()+";"
				+"b8UlRbEnable"+"="+$("input[name='b8UlRbEnable']:checked").val()+";"
				+"u8UlMaxRbNum"+"="+$("#u8UlMaxRbNum").val()+";"
				+"u8UlMinRbNum"+"="+$("#u8UlMinRbNum").val()+";"
				+"b8DlRbEnable"+"="+$("input[name='b8DlRbEnable']:checked").val()+";"
				
				+"b8PucchPcAlgSwitch"+"="+$("input[name='b8PucchPcAlgSwitch']:checked").val()+";"
				+"b8TAAlgoSwitch"+"="+$("input[name='b8TAAlgoSwitch']:checked").val()+";"
				+"b8SibRandSwitch"+"="+$("input[name='b8SibRandSwitch']:checked").val()+";"
				
				+"u8DlMaxRbNum"+"="+$("#u8DlMaxRbNum").val()+";"
				+"u8DlMinRbNum"+"="+$("#u8DlMinRbNum").val()+";"
				+"u8Cfi"+"="+$("#u8Cfi").val()+";"
				+"u8CceNum"+"="+$("#u8CceNum").val()+";"
				+"b8SingleEnable"+"="+$("input[name='b8SingleEnable']:checked").val()+";"
				+"b8DlIcicSwitch"+"="+$("input[name='b8DlIcicSwitch']:checked").val()+";"
				+"b8SinrUJSwitch"+"="+$("input[name='b8SinrUJSwitch']:checked").val()+";"
				+"u8T0"+"="+$("#u8T0").val()+";"
				+"au8DlCebBitmap"+"="+$("#au8DlCebBitmap").val()+";"
				+"u8PAForCEU"+"="+$("#u8PAForCEU").val()+";"
				+"u8PAForCCU"+"="+$("#u8PAForCCU").val()+";"
				+"b8UlIcicSwitch"+"="+$("input[name='b8UlIcicSwitch']:checked").val()+";"
				+"u16RbNiThresh"+"="+accAdd($("#u16RbNiThresh").val(),600)+";"
				+"u32AlgReserved"+"="+$("#u32AlgReserved").val()+";"
				+"u8UlHarqComb"+"="+$("input[name='u8UlHarqComb']:checked").val()+";"
				+"u8DlHarqComb"+"="+$("input[name='u8DlHarqComb']:checked").val()+";"
				+"u8CommSchdCCEAggre"+"="+$("input[name='u8CommSchdCCEAggre']:checked").val()+";"
				+"b8DvrbEnable"+"="+$("input[name='b8DvrbEnable']:checked").val()+";"
				+"u8UlSchedAlg"+"="+$("#u8UlSchedAlg").val()+";"
				+"u8DlSchedAlg"+"="+$("#u8DlSchedAlg").val()+";"
				+"au8UlCebBitmap"+"="+$("#au8UlCebBitmap").val()+";"
				+"u8MatrixType"+"="+$("input[name='u8MatrixType']:checked").val();
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
					""+
					"&browseTime="+getBrowseTime()+
					"&tableName="+tableName+
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5";
					}				
				}
			});
				
		}
		
	});
	//内存值转为显示值
	$("#t_cel_alg tr").each(function(index){
		// 
		if($("#t_cel_alg tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(1)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(1)").text("开");
		}	
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(2)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(2)").text("开");
		}
		//
		var ab8UlSubfrmFlag = $("#t_cel_alg tr:eq("+index+") td:eq(4)").text().split("");
		var span1 = ab8UlSubfrmFlag[1];
		var span2 = ab8UlSubfrmFlag[3];
		var span3 = ab8UlSubfrmFlag[5];
		var span4 = ab8UlSubfrmFlag[7];
		var span5 = ab8UlSubfrmFlag[9];
		var span6 = ab8UlSubfrmFlag[11];
		var span7 = ab8UlSubfrmFlag[13];
		var span8 = ab8UlSubfrmFlag[15];
		var span9 = ab8UlSubfrmFlag[17];
		var span10 = ab8UlSubfrmFlag[19];
		var spanA1 = "";
		var spanA2 = "";
		var spanA3 = "";
		var spanA4 = "";
		var spanA5 = "";
		var spanA6 = "";
		var spanA7 = "";
		var spanA8 = "";
		var spanA9 = "";
		var spanA10 = "";
		if(span1 == 1){
			spanA1 = "开";	
		}else{
			spanA1 = "关";	
		}
		if(span2 == 1){
			spanA2 = "开";	
		}else{
			spanA2 = "关";	
		}
		if(span3 == 1){
			spanA3 = "开";	
		}else{
			spanA3 = "关";	
		}
		if(span4 == 1){
			spanA4 = "开";	
		}else{
			spanA4 = "关";	
		}
		if(span5 == 1){
			spanA5 = "开";	
		}else{
			spanA5 = "关";	
		}
		if(span6 == 1){
			spanA6 = "开";	
		}else{
			spanA6 = "关";	
		}
		if(span7 == 1){
			spanA7 = "开";	
		}else{
			spanA7 = "关";	
		}
		if(span8 == 1){
			spanA8 = "开";	
		}else{
			spanA8 = "关";	
		}
		if(span9 == 1){
			spanA9 = "开";	
		}else{
			spanA9 = "关";	
		}
		if(span10 == 1){
			spanA10 = "开";	
		}else{
			spanA10 = "关";	
		}
		
		$("#t_cel_alg tr:eq("+index+") td:eq(4)").text(spanA1+" , "+spanA2+" , "+spanA3+" , "+spanA4+" , "+spanA5+" , "+spanA6+" , "+spanA7+" , "+spanA8+" , "+spanA9+" , "+spanA10);
		
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(5)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(5)").text("开");
		}
		//
		var ab8DlSubfrmFlag = $("#t_cel_alg tr:eq("+index+") td:eq(7)").text().split("");
		var spanB1 = ab8DlSubfrmFlag[1];
		var spanB2 = ab8DlSubfrmFlag[3];
		var spanB3 = ab8DlSubfrmFlag[5];
		var spanB4 = ab8DlSubfrmFlag[7];
		var spanB5 = ab8DlSubfrmFlag[9];
		var spanB6 = ab8DlSubfrmFlag[11];
		var spanB7 = ab8DlSubfrmFlag[13];
		var spanB8 = ab8DlSubfrmFlag[15];
		var spanB9 = ab8DlSubfrmFlag[17];
		var spanB10 = ab8DlSubfrmFlag[19];
		var spanBA1 = "";
		var spanBA2 = "";
		var spanBA3 = "";
		var spanBA4 = "";
		var spanBA5 = "";
		var spanBA6 = "";
		var spanBA7 = "";
		var spanBA8 = "";
		var spanBA9 = "";
		var spanBA10 = "";
		if(spanB1 == 1){
			spanBA1 = "开";	
		}else{
			spanBA1 = "关";	
		}
		if(spanB2 == 1){
			spanBA2 = "开";	
		}else{
			spanBA2 = "关";	
		}
		if(spanB3 == 1){
			spanBA3 = "开";	
		}else{
			spanBA3 = "关";	
		}
		if(spanB4 == 1){
			spanBA4 = "开";	
		}else{
			spanBA4 = "关";	
		}
		if(spanB5 == 1){
			spanBA5 = "开";	
		}else{
			spanBA5 = "关";	
		}
		if(spanB6 == 1){
			spanBA6 = "开";	
		}else{
			spanBA6 = "关";	
		}
		if(spanB7 == 1){
			spanBA7 = "开";	
		}else{
			spanBA7 = "关";	
		}
		if(spanB8 == 1){
			spanBA8 = "开";	
		}else{
			spanBA8 = "关";	
		}
		if(spanB9 == 1){
			spanBA9 = "开";	
		}else{
			spanBA9 = "关";	
		}
		if(spanB10 == 1){
			spanBA10 = "开";	
		}else{
			spanBA10 = "关";	
		}
		
		$("#t_cel_alg tr:eq("+index+") td:eq(7)").text(spanBA1+" , "+spanBA2+" , "+spanBA3+" , "+spanBA4+" , "+spanBA5+" , "+spanBA6+" , "+spanBA7+" , "+spanBA8+" , "+spanBA9+" , "+spanBA10);
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(8)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(8)").text("开");
		}
		if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.05");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.15");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.2");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 4){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.25");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 5){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.3");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 6){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.35");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 7){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.4");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 8){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.45");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 9){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.5");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 10){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.55");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 11){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.6");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 12){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.65");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 13){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.7");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 14){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.75");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 15){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.8");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 16){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.85");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 17){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.9");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(11)").text() == 18){
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("0.95");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(11)").text("1");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(12)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(12)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(13)").text("单天线");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(13)").text("发分集");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(13)").text("CDD");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(13)").text("自适应");
		}
		if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.05");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.15");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.2");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 4){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.25");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 5){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.3");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 6){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.35");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 7){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.4");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 8){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.45");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 9){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.5");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 10){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.55");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 11){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.6");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 12){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.65");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 13){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.7");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 14){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.75");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 15){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.8");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 16){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.85");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 17){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.9");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(16)").text() == 18){
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("0.95");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(16)").text("1");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(17)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(17)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(17)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(20)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(20)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(20)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(23)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(23)").text("自适应");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(23)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(23)").text("1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(23)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(23)").text("2");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(23)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(23)").text("3");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(23)").text("4");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(24)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(24)").text("聚合度1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(24)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(24)").text("聚合度2");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(24)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(24)").text("聚合度4");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(24)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(24)").text("聚合度8");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(24)").text("自适应");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(25)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(25)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(25)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(26)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(26)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(26)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(27)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(27)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(27)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(28)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(28)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(28)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(29)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(29)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(29)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(30)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(30)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(30)").text("开");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("1s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("2s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("3s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 4){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("4s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 5){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("5s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 6){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("6s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 7){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("7s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 8){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("8s");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(31)").text() == 9){
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("9s");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(31)").text("10s");
		}
		
		//
		var au8DlCebBitmap = $("#t_cel_alg tr:eq("+index+") td:eq(32)").text();
		var au8DlCebBitmapStr = au8DlCebBitmap.split("");
		var au8DlCebBitmapPrint = "";
		for(var i = 0 ;i<au8DlCebBitmapStr.length ;i=i+2 ){
			var str = "";
			for(var j=i ;j<i+2;j++){
				str = str + au8DlCebBitmapStr[j];
			}
			au8DlCebBitmapPrint = au8DlCebBitmapPrint +"," + str;
		}
		var au8DlCebBitmapText = au8DlCebBitmapPrint.replace(",","");
		$("#t_cel_alg tr:eq("+index+") td:eq(32)").text(au8DlCebBitmapText);
		
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("-6");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("-4.77");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("-3");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("-1.77");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 4){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("0");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 5){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(33)").text() == 6){
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("2");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(33)").text("3");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("-6");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("-4.77");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 2){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("-3");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 3){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("-1.77");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 4){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("0");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 5){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("1");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(34)").text() == 6){
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("2");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(34)").text("3");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(35)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(35)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(35)").text("开");
		}
		//
		var au8UlCebBitmap = $("#t_cel_alg tr:eq("+index+") td:eq(36)").text();
		var au8UlCebBitmapStr = au8UlCebBitmap.split("");
		var au8UlCebBitmapPrint = "";
		for(var i = 0 ;i<au8UlCebBitmapStr.length ;i=i+2 ){
			var str = "";
			for(var j=i ;j<i+2;j++){
				str = str + au8UlCebBitmapStr[j];
			}
			au8UlCebBitmapPrint = au8UlCebBitmapPrint +"," + str;
		}
		var au8UlCebBitmapText = au8UlCebBitmapPrint.replace(",","");
		$("#t_cel_alg tr:eq("+index+") td:eq(36)").text(au8UlCebBitmapText);
		//
		var u16RbNiThresh = $("#t_cel_alg tr:eq("+index+") td:eq(37)").text();
		var u16RbNiThreshMy = accSub(u16RbNiThresh,600);
		$("#t_cel_alg tr:eq("+index+") td:eq(37)").text(u16RbNiThreshMy);
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(39)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(39)").text("CC");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(39)").text("IR");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(40)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(40)").text("CC");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(40)").text("IR");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(41)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(41)").text("E_PRIO_MAXTB");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(41)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(41)").text("E_PRIO_RR");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(41)").text("E_PRIO_PF");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(42)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(42)").text("E_PRIO_MAXTB");
		}else if($("#t_cel_alg tr:eq("+index+") td:eq(42)").text() == 1){
			$("#t_cel_alg tr:eq("+index+") td:eq(42)").text("E_PRIO_RR");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(42)").text("E_PRIO_PF");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(43)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(43)").text("聚合度4");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(43)").text("聚合度8");
		}
		//
		if($("#t_cel_alg tr:eq("+index+") td:eq(44)").text() == 0){
			$("#t_cel_alg tr:eq("+index+") td:eq(44)").text("关");
		}else{
			$("#t_cel_alg tr:eq("+index+") td:eq(44)").text("开");
		}
	});	
	$("#t_cel_alg td.u8MatrixType").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("MRC");
			break;
		case "1":
			$(this).text("IRC");
			break;
		}	
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5";
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
	$("#t_cel_alg tr").each(function(index){
		$("#t_cel_alg tr:eq("+index+") td:eq(46)").click(function(){
			var u8CId = $("#t_cel_alg tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5&referTable=T_CEL_PARA&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_alg tr").each(function(index){
		if(index != 0){
			$("#t_cel_alg tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_alg tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5&referTable=T_CEL_PARA&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_cel_alg tr").each(function(index){
		$("#t_cel_alg tr:eq("+index+") td:eq(47)").click(function(){
			var u8CId = $("#t_cel_alg tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_alg input[type=checkbox]").each(function(index){
			if($("#t_cel_alg input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_alg tr:eq("+index+") td:eq(0)").text();
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
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ALG-1.1.5";
	});

	function convertToHexString(binaryString) {
		var length = binaryString.length;
		var hexString = "";
		for(var i = 0; i < length; i = i+8) {
			var temp = binaryString.substring(i, i+8);
			var result = 0;
			for(var j = 0; j < 8; j++) {
				var int = parseInt(temp[j]);
				int = int << (7-j);
				result += int;
			}
			var resStr = result.toString(16);
			if(resStr.length < 2) {
				resStr = "0" + resStr;
			}
			hexString += resStr;
		}
		return hexString;
	}
	
	function getCheckBoxValues(boxIdPreFix, boxNum) {
		var value = "";
		for(var i = 0; i < boxNum; i++) {
			var flag = $("#"+boxIdPreFix+i).attr("checked");
			if(flag == undefined) {
				value += "0";
			} else if(flag == "checked") {
				value += "1";
			}
		}
		return value;
	}
	
	$("input[name='b8DlIcicSwitch']").click(function(){
		if($(this).val() == 1){
			alert("DL ICIC开关打开需要核实PA配置是否导致功率超出RRU额定功率");	
		}
	});
	$("#u8PAForCEU").change(function(){
		alert("修改该值可能会导致功率超出RRU配置功率");	
	});
	$("#u8PAForCCU").change(function(){
		alert("修改该值可能会导致功率超出RRU配置功率");	
	});
		
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
