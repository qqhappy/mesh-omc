$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	var isStep=/^-?\d+(\.?\d{1}){0,1}$/;
	$("#submit_add").click(function(){
		//
		var u16CellTransPwr = $("#u16CellTransPwr").val();
		var u16CellTransPwr1 = accMul(u16CellTransPwr,10);
		$("#u16CellTransPwr1").val(u16CellTransPwr1);
		
		//
		var u16PschPwrOfst = $("#u16PschPwrOfst").val();
		var u16PschPwrOfst1 = accMul(accAdd(u16PschPwrOfst,76),10);
		$("#u16PschPwrOfst1").val(u16PschPwrOfst1);
		//
		var u16SschPwrOfst = $("#u16SschPwrOfst").val();
		var u16SschPwrOfst1 = accMul(accAdd(u16SschPwrOfst,76),10);
		$("#u16SschPwrOfst1").val(u16SschPwrOfst1);
		//
		var u16PcfichPwrOfst = $("#u16PcfichPwrOfst").val();
		var u16PcfichPwrOfst1 = accMul(accAdd(u16PcfichPwrOfst,76),10);
		$("#u16PcfichPwrOfst1").val(u16PcfichPwrOfst1);
		//
		var u16PhichPwrOfst = $("#u16PhichPwrOfst").val();
		var u16PhichPwrOfst1 = accMul(accAdd(u16PhichPwrOfst,76),10);
		$("#u16PhichPwrOfst1").val(u16PhichPwrOfst1);
		
		//				
		var au16PdcchF0PwrOfst1 = 	parseFloat($("#au16PdcchF0PwrOfst1").val());
		var au16PdcchF0PwrOfst2 = 	parseFloat($("#au16PdcchF0PwrOfst2").val());
		var au16PdcchF0PwrOfst3 = 	parseFloat($("#au16PdcchF0PwrOfst3").val());
		var au16PdcchF0PwrOfst4 = 	parseFloat($("#au16PdcchF0PwrOfst4").val());
		var no1 = accMul(accAdd(au16PdcchF0PwrOfst1,76),10);
		var s1 = parseInt(no1).toString(16);
		var length1 = s1.length;
		if(length1<4){
			for(var i=0;i<4-length1;i++){
				s1 = "0"+s1;	
			}
		}
		var no2 = accMul(accAdd(au16PdcchF0PwrOfst2,76),10);
		var s2 = parseInt(no2).toString(16);
		var length2 = s2.length;
		if(length2<4){
			for(var i=0;i<4-length2;i++){
				s2 = "0"+s2;	
			}
		}
		var no3 = accMul(accAdd(au16PdcchF0PwrOfst3,76),10);
		var s3 = parseInt(no3).toString(16);
		var length3 = s3.length;
		if(length3<4){
			for(var i=0;i<4-length3;i++){
				s3 = "0"+s3;	
			}
		}
		var no4 = accMul(accAdd(au16PdcchF0PwrOfst4,76),10);
		var s4 = parseInt(no4).toString(16);
		var length4 = s4.length;
		if(length4<4){
			for(var i=0;i<4-length4;i++){
				s4 = "0"+s4;	
			}
		}
		$("#au16PdcchF0PwrOfst").val(s1+s2+s3+s4);
		//
		
		var au16PdcchF1PwrOfst1 = 	parseFloat($("#au16PdcchF1PwrOfst1").val());
		var au16PdcchF1PwrOfst2 = 	parseFloat($("#au16PdcchF1PwrOfst2").val());
		var au16PdcchF1PwrOfst3 = 	parseFloat($("#au16PdcchF1PwrOfst3").val());
		var au16PdcchF1PwrOfst4 = 	parseFloat($("#au16PdcchF1PwrOfst4").val());
		var noq1 = accMul(accAdd(au16PdcchF1PwrOfst1,76),10);
		var sq1 = parseInt(noq1).toString(16);
		var lengthq1 = sq1.length;
		if(lengthq1<4){
			for(var i=0;i<4-lengthq1;i++){
				sq1 = "0"+sq1;	
			}
		}
		var noq2 = accMul(accAdd(au16PdcchF1PwrOfst2,76),10);
		var sq2 = parseInt(noq2).toString(16);
		var lengthq2 = sq2.length;
		if(lengthq2<4){
			for(var i=0;i<4-lengthq2;i++){
				sq2 = "0"+sq2;	
			}
		}
		var noq3 = accMul(accAdd(au16PdcchF1PwrOfst3,76),10);
		var sq3 = parseInt(noq3).toString(16);
		var lengthq3 = sq3.length;
		if(lengthq3<4){
			for(var i=0;i<4-lengthq3;i++){
				sq3 = "0"+sq3;	
			}
		}
		var noq4 = accMul(accAdd(au16PdcchF1PwrOfst4,76),10);
		var sq4 = parseInt(noq4).toString(16);
		var lengthq4 = sq4.length;
		if(lengthq4<4){
			for(var i=0;i<4-lengthq4;i++){
				sq4 = "0"+sq4;	
			}
		}
		$("#au16PdcchF1PwrOfst").val(sq1+sq2+sq3+sq4);
		
		//
		
		var au16PdcchF1APwrOfst1 = 	parseFloat($("#au16PdcchF1APwrOfst1").val());
		var au16PdcchF1APwrOfst2 = 	parseFloat($("#au16PdcchF1APwrOfst2").val());
		var au16PdcchF1APwrOfst3 = 	parseFloat($("#au16PdcchF1APwrOfst3").val());
		var au16PdcchF1APwrOfst4 = 	parseFloat($("#au16PdcchF1APwrOfst4").val());
		var now1 = accMul(accAdd(au16PdcchF1APwrOfst1,76),10);
		var sw1 = parseInt(now1).toString(16);
		var lengthw1 = sw1.length;
		if(lengthw1<4){
			for(var i=0;i<4-lengthw1;i++){
				sw1 = "0"+sw1;	
			}
		}
		var now2 = accMul(accAdd(au16PdcchF1APwrOfst2,76),10);
		var sw2 = parseInt(now2).toString(16);
		var lengthw2 = sw2.length;
		if(lengthw2<4){
			for(var i=0;i<4-lengthw2;i++){
				sw2 = "0"+sw2;	
			}
		}
		var now3 = accMul(accAdd(au16PdcchF1APwrOfst3,76),10);
		var sw3 = parseInt(now3).toString(16);
		var lengthw3 = sw3.length;
		if(lengthw3<4){
			for(var i=0;i<4-lengthw3;i++){
				sw3 = "0"+sw3;	
			}
		}
		var now4 = accMul(accAdd(au16PdcchF1APwrOfst4,76),10);
		var sw4 = parseInt(now4).toString(16);
		var lengthw4 = sw4.length;
		if(lengthw4<4){
			for(var i=0;i<4-lengthw4;i++){
				sw4 = "0"+sw4;	
			}
		}
		$("#au16PdcchF1APwrOfst").val(sw1+sw2+sw3+sw4);
		//
		
		var au16PdcchF2PwrOfst1 = 	parseFloat($("#au16PdcchF2PwrOfst1").val());
		var au16PdcchF2PwrOfst2 = 	parseFloat($("#au16PdcchF2PwrOfst2").val());
		var au16PdcchF2PwrOfst3 = 	parseFloat($("#au16PdcchF2PwrOfst3").val());
		var au16PdcchF2PwrOfst4 = 	parseFloat($("#au16PdcchF2PwrOfst4").val());
		var noe1 = accMul(accAdd(au16PdcchF2PwrOfst1,76),10);
		var se1 = parseInt(noe1).toString(16);
		var lengthe1 = se1.length;
		if(lengthe1<4){
			for(var i=0;i<4-lengthe1;i++){
				se1 = "0"+se1;	
			}
		}
		var noe2 = accMul(accAdd(au16PdcchF2PwrOfst2,76),10);
		var se2 = parseInt(noe2).toString(16);
		var lengthe2 = se2.length;
		if(lengthe2<4){
			for(var i=0;i<4-lengthe2;i++){
				se2 = "0"+se2;	
			}
		}
		var noe3 = accMul(accAdd(au16PdcchF2PwrOfst3,76),10);
		var se3 = parseInt(noe3).toString(16);
		var lengthe3 = se3.length;
		if(lengthe3<4){
			for(var i=0;i<4-lengthe3;i++){
				se3 = "0"+se3;	
			}
		}
		var noe4 = accMul(accAdd(au16PdcchF2PwrOfst4,76),10);
		var se4 = parseInt(noe4).toString(16);
		var lengthe4 = se4.length;
		if(lengthe4<4){
			for(var i=0;i<4-lengthe4;i++){
				se4 = "0"+se4;	
			}
		}
		$("#au16PdcchF2PwrOfst").val(se1+se2+se3+se4);
		
		
		//
		
		var au16PdcchF2APwrOfst1 = 	parseFloat($("#au16PdcchF2APwrOfst1").val());
		var au16PdcchF2APwrOfst2 = 	parseFloat($("#au16PdcchF2APwrOfst2").val());
		var au16PdcchF2APwrOfst3 = 	parseFloat($("#au16PdcchF2APwrOfst3").val());
		var au16PdcchF2APwrOfst4 = 	parseFloat($("#au16PdcchF2APwrOfst4").val());
		var nor1 = accMul(accAdd(au16PdcchF2APwrOfst1,76),10);
		var sr1 = parseInt(nor1).toString(16);
		var lengthr1 = sr1.length;
		if(lengthr1<4){
			for(var i=0;i<4-lengthr1;i++){
				sr1 = "0"+sr1;	
			}
		}
		var nor2 = accMul(accAdd(au16PdcchF2APwrOfst2,76),10);
		var sr2 = parseInt(nor2).toString(16);
		var lengthr2 = sr2.length;
		if(lengthr2<4){
			for(var i=0;i<4-lengthr2;i++){
				sr2 = "0"+sr2;	
			}
		}
		var nor3 = accMul(accAdd(au16PdcchF2APwrOfst3,76),10);
		var sr3 = parseInt(nor3).toString(16);
		var lengthr3 = sr3.length;
		if(lengthr3<4){
			for(var i=0;i<4-lengthr3;i++){
				sr3 = "0"+sr3;	
			}
		}
		var nor4 = accMul(accAdd(au16PdcchF2APwrOfst4,76),10);
		var sr4 = parseInt(nor4).toString(16);
		var lengthr4 = sr4.length;
		if(lengthr4<4){
			for(var i=0;i<4-lengthr4;i++){
				sr4 = "0"+sr4;	
			}
		}
		$("#au16PdcchF2APwrOfst").val(sr1+sr2+sr3+sr4);
		//
								
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if(!(isStep.test($("#u16CellTransPwr").val()) && $("#u16CellTransPwr").val()<=50 && $("#u16CellTransPwr").val()>=0)){
			$("#u16CellTransPwrError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u16CellTransPwrError").text("");
		}
		if(!(isNum.test($("#u16CellSpeRefSigPwr").val()) && $("#u16CellSpeRefSigPwr").val()<=50 && $("#u16CellSpeRefSigPwr").val()>=-60)){
			$("#u16CellSpeRefSigPwrError").text("/* 请输入-60~50之间的整数 */");
			index++;
		}else{
			$("#u16CellSpeRefSigPwrError").text("");
		}
		if(!(isStep.test($("#u16PschPwrOfst").val()) && $("#u16PschPwrOfst").val()<=40 && $("#u16PschPwrOfst").val()>=-76)){
			$("#u16PschPwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u16PschPwrOfstError").text("");
		}
		if(!(isStep.test($("#u16SschPwrOfst").val()) && $("#u16SschPwrOfst").val()<=40 && $("#u16SschPwrOfst").val()>=-76)){
			$("#u16SschPwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u16SschPwrOfstError").text("");
		}
		if(!(isStep.test($("#u16PcfichPwrOfst").val()) && $("#u16PcfichPwrOfst").val()<=40 && $("#u16PcfichPwrOfst").val()>=-76)){
			$("#u16PcfichPwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u16PcfichPwrOfstError").text("");
		}
		if(!(isStep.test($("#u16PhichPwrOfst").val()) && $("#u16PhichPwrOfst").val()<=40 && $("#u16PhichPwrOfst").val()>=-76)){
			$("#u16PhichPwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u16PhichPwrOfstError").text("");
		}
		if(!(isStep.test($("#au16PdcchF0PwrOfst1").val()) && $("#au16PdcchF0PwrOfst1").val()<=40 && $("#au16PdcchF0PwrOfst1").val()>=-76 && isStep.test($("#au16PdcchF0PwrOfst2").val()) && $("#au16PdcchF0PwrOfst2").val()<=40 && $("#au16PdcchF0PwrOfst2").val()>=-76 && isStep.test($("#au16PdcchF0PwrOfst3").val()) && $("#au16PdcchF0PwrOfst3").val()<=40 && $("#au16PdcchF0PwrOfst3").val()>=-76 && isStep.test($("#au16PdcchF0PwrOfst4").val()) && $("#au16PdcchF0PwrOfst4").val()<=40 && $("#au16PdcchF0PwrOfst4").val()>=-76)){
			$("#au16PdcchF0PwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#au16PdcchF0PwrOfstError").text("");
		}
		if(!(isStep.test($("#au16PdcchF1PwrOfst1").val()) && $("#au16PdcchF1PwrOfst1").val()<=40 && $("#au16PdcchF1PwrOfst1").val()>=-76 && isStep.test($("#au16PdcchF1PwrOfst2").val()) && $("#au16PdcchF1PwrOfst2").val()<=40 && $("#au16PdcchF1PwrOfst2").val()>=-76 && isStep.test($("#au16PdcchF1PwrOfst3").val()) && $("#au16PdcchF1PwrOfst3").val()<=40 && $("#au16PdcchF1PwrOfst3").val()>=-76 && isStep.test($("#au16PdcchF1PwrOfst4").val()) && $("#au16PdcchF1PwrOfst4").val()<=40 && $("#au16PdcchF1PwrOfst4").val()>=-76)){
			$("#au16PdcchF1PwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#au16PdcchF1PwrOfstError").text("");
		}
		if(!(isStep.test($("#au16PdcchF1APwrOfst1").val()) && $("#au16PdcchF1APwrOfst1").val()<=40 && $("#au16PdcchF1APwrOfst1").val()>=-76 && isStep.test($("#au16PdcchF1APwrOfst2").val()) && $("#au16PdcchF1APwrOfst2").val()<=40 && $("#au16PdcchF1APwrOfst2").val()>=-76 && isStep.test($("#au16PdcchF1APwrOfst3").val()) && $("#au16PdcchF1APwrOfst3").val()<=40 && $("#au16PdcchF1APwrOfst3").val()>=-76 && isStep.test($("#au16PdcchF1APwrOfst4").val()) && $("#au16PdcchF1APwrOfst4").val()<=40 && $("#au16PdcchF1APwrOfst4").val()>=-76)){
			$("#au16PdcchF1APwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#au16PdcchF1APwrOfstError").text("");
		}
		if(!(isStep.test($("#au16PdcchF2PwrOfst1").val()) && $("#au16PdcchF2PwrOfst1").val()<=40 && $("#au16PdcchF2PwrOfst1").val()>=-76 && isStep.test($("#au16PdcchF2PwrOfst2").val()) && $("#au16PdcchF2PwrOfst2").val()<=40 && $("#au16PdcchF2PwrOfst2").val()>=-76 && isStep.test($("#au16PdcchF2PwrOfst3").val()) && $("#au16PdcchF2PwrOfst3").val()<=40 && $("#au16PdcchF2PwrOfst3").val()>=-76 && isStep.test($("#au16PdcchF2PwrOfst4").val()) && $("#au16PdcchF2PwrOfst4").val()<=40 && $("#au16PdcchF2PwrOfst4").val()>=-76)){
			$("#au16PdcchF2PwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#au16PdcchF2PwrOfstError").text("");
		}
		if(!(isStep.test($("#au16PdcchF2APwrOfst1").val()) && $("#au16PdcchF2APwrOfst1").val()<=40 && $("#au16PdcchF2APwrOfst1").val()>=-76 && isStep.test($("#au16PdcchF2APwrOfst2").val()) && $("#au16PdcchF2APwrOfst2").val()<=40 && $("#au16PdcchF2APwrOfst2").val()>=-76 && isStep.test($("#au16PdcchF2APwrOfst3").val()) && $("#au16PdcchF2APwrOfst3").val()<=40 && $("#au16PdcchF2APwrOfst3").val()>=-76 && isStep.test($("#au16PdcchF2APwrOfst4").val()) && $("#au16PdcchF2APwrOfst4").val()<=40 && $("#au16PdcchF2APwrOfst4").val()>=-76)){
			$("#au16PdcchF2APwrOfstError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#au16PdcchF2APwrOfstError").text("");
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u16CellTransPwr"+"="+$("#u16CellTransPwr1").val()+";"
				+"u16CellSpeRefSigPwr"+"="+accAdd($("#u16CellSpeRefSigPwr").val(),60)+";"
				+"u16PschPwrOfst"+"="+$("#u16PschPwrOfst1").val()+";"
				+"u16SschPwrOfst"+"="+$("#u16SschPwrOfst1").val()+";"
				+"u16PcfichPwrOfst"+"="+$("#u16PcfichPwrOfst1").val()+";"
				+"u16PhichPwrOfst"+"="+$("#u16PhichPwrOfst1").val()+";"
				+"au16PdcchF0PwrOfst"+"="+$("#au16PdcchF0PwrOfst").val()+";"
				+"au16PdcchF1PwrOfst"+"="+$("#au16PdcchF1PwrOfst").val()+";"
				+"au16PdcchF1APwrOfst"+"="+$("#au16PdcchF1APwrOfst").val()+";"
				+"au16PdcchF2PwrOfst"+"="+$("#au16PdcchF2PwrOfst").val()+";"
				+"au16PdcchF2APwrOfst"+"="+$("#au16PdcchF2APwrOfst").val()+";"
				+"u8PAForBCCH"+"="+$("#u8PAForBCCH").val()+";"
				+"u8PAForCCCH"+"="+$("#u8PAForCCCH").val()+";"
				+"u8PAForPCCH"+"="+$("#u8PAForPCCH").val()+";"
				+"u8PAForMSG2"+"="+$("#u8PAForMSG2").val()+";"
				+"u8PB"+"="+$("#u8PB").val()+";"
				+"u8PAForDTCH"+"="+$("#u8PAForDTCH").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5";
					}				
				}
			});	
			
		}
		
	});
	//内存值转为显示值
	$("#t_cel_dlpc1 tr").each(function(index){
		//
		var u16CellTransPwr = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(1)").text();
		var u16CellTransPwr1 = parseInt(u16CellTransPwr);
		var u16CellTransPwr2 = accDiv(u16CellTransPwr1,10);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(1)").text(u16CellTransPwr2);							   
		
		//
		var u16CellSpeRefSigPwr = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(2)").text();
		var u16CellSpeRefSigPwr1 = parseInt(u16CellSpeRefSigPwr);
		var u16CellSpeRefSigPwr2 = u16CellSpeRefSigPwr1-60;
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(2)").text(u16CellSpeRefSigPwr2);	
		
		//
		var u16PschPwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(3)").text();
		var u16PschPwrOfst1 = parseInt(u16PschPwrOfst);
		var u16PschPwrOfst2 = accSub(accDiv(u16PschPwrOfst1,10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(3)").text(u16PschPwrOfst2);	
		//
		var u16SschPwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(4)").text();
		var u16SschPwrOfst1 = parseInt(u16SschPwrOfst);
		var u16SschPwrOfst2 = accSub(accDiv(u16SschPwrOfst1,10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(4)").text(u16SschPwrOfst2);	
		//
		var u16PcfichPwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(5)").text();
		var u16PcfichPwrOfst1 = parseInt(u16PcfichPwrOfst);
		var u16PcfichPwrOfst2 = accSub(accDiv(u16PcfichPwrOfst1,10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(5)").text(u16PcfichPwrOfst2);	
		//
		var u16PhichPwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(6)").text();
		var u16PhichPwrOfst1 = parseInt(u16PhichPwrOfst);
		var u16PhichPwrOfst2 = accSub(accDiv(u16PhichPwrOfst1,10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(6)").text(u16PhichPwrOfst2);	
		
		
									   
		
		//
		var au16PdcchF0PwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(7)").text().split("");
		var s1 = au16PdcchF0PwrOfst[0]+au16PdcchF0PwrOfst[1]+au16PdcchF0PwrOfst[2]+au16PdcchF0PwrOfst[3];
		var s2 = au16PdcchF0PwrOfst[4]+au16PdcchF0PwrOfst[5]+au16PdcchF0PwrOfst[6]+au16PdcchF0PwrOfst[7];
		var s3 = au16PdcchF0PwrOfst[8]+au16PdcchF0PwrOfst[9]+au16PdcchF0PwrOfst[10]+au16PdcchF0PwrOfst[11];
		var s4 = au16PdcchF0PwrOfst[12]+au16PdcchF0PwrOfst[13]+au16PdcchF0PwrOfst[14]+au16PdcchF0PwrOfst[15];
		var n1 = accSub(accDiv(parseInt(parseInt(s1,16).toString(10)),10),76);
		var n2 = accSub(accDiv(parseInt(parseInt(s2,16).toString(10)),10),76);
		var n3 = accSub(accDiv(parseInt(parseInt(s3,16).toString(10)),10),76);
		var n4 = accSub(accDiv(parseInt(parseInt(s4,16).toString(10)),10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(7)").text(n1+" , "+n2+" , "+n3+" , "+n4);
		//
		var au16PdcchF1PwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(8)").text().split("");
		var sq1 = au16PdcchF1PwrOfst[0]+au16PdcchF1PwrOfst[1]+au16PdcchF1PwrOfst[2]+au16PdcchF1PwrOfst[3];
		var sq2 = au16PdcchF1PwrOfst[4]+au16PdcchF1PwrOfst[5]+au16PdcchF1PwrOfst[6]+au16PdcchF1PwrOfst[7];
		var sq3 = au16PdcchF1PwrOfst[8]+au16PdcchF1PwrOfst[9]+au16PdcchF1PwrOfst[10]+au16PdcchF1PwrOfst[11];
		var sq4 = au16PdcchF1PwrOfst[12]+au16PdcchF1PwrOfst[13]+au16PdcchF1PwrOfst[14]+au16PdcchF1PwrOfst[15];
		var nq1 = accSub(accDiv(parseInt(parseInt(sq1,16).toString(10)),10),76);
		var nq2 = accSub(accDiv(parseInt(parseInt(sq2,16).toString(10)),10),76);
		var nq3 = accSub(accDiv(parseInt(parseInt(sq3,16).toString(10)),10),76);
		var nq4 = accSub(accDiv(parseInt(parseInt(sq4,16).toString(10)),10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(8)").text(nq1+" , "+nq2+" , "+nq3+" , "+nq4);
		//
		var au16PdcchF1APwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(9)").text().split("");
		var sw1 = au16PdcchF1APwrOfst[0]+au16PdcchF1APwrOfst[1]+au16PdcchF1APwrOfst[2]+au16PdcchF1APwrOfst[3];
		var sw2 = au16PdcchF1APwrOfst[4]+au16PdcchF1APwrOfst[5]+au16PdcchF1APwrOfst[6]+au16PdcchF1APwrOfst[7];
		var sw3 = au16PdcchF1APwrOfst[8]+au16PdcchF1APwrOfst[9]+au16PdcchF1APwrOfst[10]+au16PdcchF1APwrOfst[11];
		var sw4 = au16PdcchF1APwrOfst[12]+au16PdcchF1APwrOfst[13]+au16PdcchF1APwrOfst[14]+au16PdcchF1APwrOfst[15];
		var nw1 = accSub(accDiv(parseInt(parseInt(sw1,16).toString(10)),10),76);
		var nw2 = accSub(accDiv(parseInt(parseInt(sw2,16).toString(10)),10),76);
		var nw3 = accSub(accDiv(parseInt(parseInt(sw3,16).toString(10)),10),76);
		var nw4 = accSub(accDiv(parseInt(parseInt(sw4,16).toString(10)),10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(9)").text(nw1+" , "+nw2+" , "+nw3+" , "+nw4);
		//
		var au16PdcchF2PwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(10)").text().split("");
		var se1 = au16PdcchF2PwrOfst[0]+au16PdcchF2PwrOfst[1]+au16PdcchF2PwrOfst[2]+au16PdcchF2PwrOfst[3];
		var se2 = au16PdcchF2PwrOfst[4]+au16PdcchF2PwrOfst[5]+au16PdcchF2PwrOfst[6]+au16PdcchF2PwrOfst[7];
		var se3 = au16PdcchF2PwrOfst[8]+au16PdcchF2PwrOfst[9]+au16PdcchF2PwrOfst[10]+au16PdcchF2PwrOfst[11];
		var se4 = au16PdcchF2PwrOfst[12]+au16PdcchF2PwrOfst[13]+au16PdcchF2PwrOfst[14]+au16PdcchF2PwrOfst[15];
		var ne1 = accSub(accDiv(parseInt(parseInt(se1,16).toString(10)),10),76);
		var ne2 = accSub(accDiv(parseInt(parseInt(se2,16).toString(10)),10),76);
		var ne3 = accSub(accDiv(parseInt(parseInt(se3,16).toString(10)),10),76);
		var ne4 = accSub(accDiv(parseInt(parseInt(se4,16).toString(10)),10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(10)").text(ne1+" , "+ne2+" , "+ne3+" , "+ne4);
		//
		var au16PdcchF2APwrOfst = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(11)").text().split("");
		var sr1 = au16PdcchF2APwrOfst[0]+au16PdcchF2APwrOfst[1]+au16PdcchF2APwrOfst[2]+au16PdcchF2APwrOfst[3];
		var sr2 = au16PdcchF2APwrOfst[4]+au16PdcchF2APwrOfst[5]+au16PdcchF2APwrOfst[6]+au16PdcchF2APwrOfst[7];
		var sr3 = au16PdcchF2APwrOfst[8]+au16PdcchF2APwrOfst[9]+au16PdcchF2APwrOfst[10]+au16PdcchF2APwrOfst[11];
		var sr4 = au16PdcchF2APwrOfst[12]+au16PdcchF2APwrOfst[13]+au16PdcchF2APwrOfst[14]+au16PdcchF2APwrOfst[15];
		var nr1 = accSub(accDiv(parseInt(parseInt(sr1,16).toString(10)),10),76);
		var nr2 = accSub(accDiv(parseInt(parseInt(sr2,16).toString(10)),10),76);
		var nr3 = accSub(accDiv(parseInt(parseInt(sr3,16).toString(10)),10),76);
		var nr4 = accSub(accDiv(parseInt(parseInt(sr4,16).toString(10)),10),76);
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(11)").text(nr1+" , "+nr2+" , "+nr3+" , "+nr4);
		
									   
		//
		if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("-6");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 1){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("-4.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 2){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("-3");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 3){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("-1.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 4){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("0");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 5){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("1");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text() == 6){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("2");
		}else{
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(12)").text("3");
		}
		//
		if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("-6");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("-4.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("-3");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 3){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("-1.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 4){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("0");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 5){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("1");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text() == 6){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("2");
		}else{
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(13)").text("3");
		}
		//
		if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 0){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("-6");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 1){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("-4.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 2){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("-3");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 3){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("-1.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 4){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("0");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 5){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("1");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text() == 6){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("2");
		}else{
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(14)").text("3");
		}
		//
		if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 0){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("-6");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 1){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("-4.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 2){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("-3");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 3){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("-1.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 4){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("0");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 5){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("1");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text() == 6){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("2");
		}else{
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(15)").text("3");
		}
		//
		if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 0){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("-6");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 1){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("-4.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 2){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("-3");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 3){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("-1.77");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 4){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("0");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 5){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("1");
		}else if($("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text() == 6){
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("2");
		}else{
			$("#t_cel_dlpc1 tr:eq("+index+") td:eq(17)").text("3");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5";
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
	$("#t_cel_dlpc1 tr").each(function(index){
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(18)").click(function(){
			var u8CId = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_dlpc1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_dlpc1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_cel_dlpc1 tr").each(function(index){
		$("#t_cel_dlpc1 tr:eq("+index+") td:eq(19)").click(function(){
			var u8CId = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5&parameters="+para+"";
			}

		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_dlpc1 input[type=checkbox]").each(function(index){
			if($("#t_cel_dlpc1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_dlpc1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DLPC-2.1.5";
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