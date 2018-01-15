$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8N_RB2").val()) && $("#u8N_RB2").val()<=98  && $("#u8N_RB2").val()>=0)){
			$("#u8N_RB2Error").text("/* 请输入0~98之间的整数 */");
			index++;
		}else{
			$("#u8N_RB2Error").text("");
		} 
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if(!(isNum.test($("#u16N_SrChn").val()) && $("#u16N_SrChn").val()<=2047  && $("#u16N_SrChn").val()>=0)){
			$("#u16N_SrChnError").text("/* 请输入0~2047之间的整数 */");
			index++;
		}else if((parseInt($("#u16N_SrChn").val()))%(parseInt(36/(parseInt($("#u8DeltaPucchShift").val())+1))) !=0){
			var di = parseInt(36/(parseInt($("#u8DeltaPucchShift").val())+1));
			$("#u16N_SrChnError").text("/* 必须为"+di+"的整数倍 */");
			index++;
		}else{
			$("#u16N_SrChnError").text("");
		}
		if(!($("input[name=u8UeTranAntennaSelectMode]:checked").val() == 0 || $("input[name=u8UeTranAntennaSelectMode]:checked").val() == 1)){
			$("#u8UeTranAntennaSelectModeError").text("/* 请选择一个模式 */");
			index++;
		}else{
			$("#u8UeTranAntennaSelectModeError").text("");
		}
		if(!(isNum.test($("#u8DeltaSs").val()) && $("#u8DeltaSs").val()<=29  && $("#u8DeltaSs").val()>=0)){
			$("#u8DeltaSsError").text("/* 请输入0~29之间的整数 */");
			index++;
		}else{
			$("#u8DeltaSsError").text("");
		}
		if(!(isNum.test($("#u8PuschhopOffset").val()) && $("#u8PuschhopOffset").val()<=98  && $("#u8PuschhopOffset").val()>=0)){
			$("#u8PuschhopOffsetError").text("/* 请输入0~98之间的整数 */");
			index++;
		}else{
			$("#u8PuschhopOffsetError").text("");
		}
		if(!checkInputInForm(isNum,"u8PucchBlankRBNum",100,0)){
			index++;
			$("#u8PucchBlankRBNumError").text(dynamicInfo(10000,generateArgments_i18n_num(100,0)));
		}
		var meterOne = $("#u8MaxUserPucchfmt1").val();
		var meterOneT = 4;
		if(meterOne == 0){
			meterOneT = 4;
		}else if(meterOne == 1){
			meterOneT = 6;
		}else if(meterOne == 2){
			meterOneT = 9;
		}else if(meterOne == 3){
			meterOneT = 12;
		}else if(meterOne == 4){
			meterOneT = 18;
		}else{
			meterOneT = 36;
		}
		var meterTwo = 36/(parseInt($("#u8DeltaPucchShift").val())+1);
		if(parseInt(meterOneT) > parseInt(meterTwo)){
			$("#u8MaxUserPucchfmt1Error").text("/* 请减少用户数 */");
			index++;
		}else{
			$("#u8MaxUserPucchfmt1Error").text("");
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8DeltaPucchShift"+"="+$("#u8DeltaPucchShift").val()+";"
				+"u8N_RB2"+"="+$("#u8N_RB2").val()+";"
				+"u8N_cs1"+"="+$("#u8N_cs1").val()+";"
				+"u16N_SrChn"+"="+$("#u16N_SrChn").val()+";"
				+"u8SRITransPrd"+"="+$("#u8SRITransPrd").val()+";"
				+"u8GroupHopEnable"+"="+$("input[name='u8GroupHopEnable']:checked").val()+";"
				+"u8SeqHopEnable"+"="+$("input[name='u8SeqHopEnable']:checked").val()+";"
				+"u8CyclicShift"+"="+$("#u8CyclicShift").val()+";"
				+"u8UeTranAntennaSelectEnable"+"="+$("input[name='u8UeTranAntennaSelectEnable']:checked").val()+";"
				+"u8UeTranAntennaSelectMode"+"="+$("input[name='u8UeTranAntennaSelectMode']:checked").val()+";"
				+"u8DeltaSs"+"="+$("#u8DeltaSs").val()+";"
				+"u8SrTransMax"+"="+$("#u8SrTransMax").val()+";"
				+"u8MaxUserPucchfmt2"+"="+$("#u8MaxUserPucchfmt2").val()+";"
				+"u8MaxUserPucchfmt1"+"="+$("#u8MaxUserPucchfmt1").val()+";"
				+"u8PuschCqiFbMethd"+"="+$("#u8PuschCqiFbMethd").val()+";"
				+"u8FormatIndicatorPeriodic"+"="+$("#u8FormatIndicatorPeriodic").val()+";"
				+"u8RptCqiPrd"+"="+$("#u8RptCqiPrd").val()+";"
				+"u8K"+"="+$("#u8K").val()+";"
				+"u8SimultaneousAckNackAndCQI"+"="+$("input[name='u8SimultaneousAckNackAndCQI']:checked").val()+";"
				+"u8AckNackFeedbackMode"+"="+$("#u8AckNackFeedbackMode").val()+";"
				+"u8ACKMCSBetaOffset"+"="+$("#u8ACKMCSBetaOffset").val()+";"
				+"u8RIMCSBetaOffset"+"="+$("#u8RIMCSBetaOffset").val()+";"
				+"u8CQIMCSBetaOffset"+"="+$("#u8CQIMCSBetaOffset").val()+";"
				+"u8PuschNsb"+"="+$("#u8PuschNsb").val()+";"
				+"u8PuschHopMode"+"="+$("#u8PuschHopMode").val()+";"
				+"u8PuschhopOffset"+"="+$("#u8PuschhopOffset").val()+";"
				+"U8PuschEnable64QAM"+"="+$("input[name='U8PuschEnable64QAM']:checked").val()+";"
		
				+"u8CellSrsEnable"+"="+$("input[name='u8CellSrsEnable']:checked").val()+";"
				+"u8SimulANSrsEnable"+"="+$("input[name='u8SimulANSrsEnable']:checked").val()+";"
				+"u8SrsMaxUpPts"+"="+$("input[name='u8SrsMaxUpPts']:checked").val()+";"
				+"u8SrsDuration"+"="+$("input[name='u8SrsDuration']:checked").val()+";"
				+"u8SrsBwCfgIndex"+"="+$("#u8SrsBwCfgIndex").val()+";"
				+"u8SrsCellCfgIndex"+"="+$("#u8SrsCellCfgIndex").val()+";"
				+"u8SrsMaxCodeDivUserNum"+"="+$("#u8SrsMaxCodeDivUserNum").val()+";"
				+"u8SrsUeInitBw"+"="+$("#u8SrsUeInitBw").val()+";"
				+"u8SrsUeHoppingBw"+"="+$("#u8SrsUeHoppingBw").val()+";"
				+"u16SrsUePeriod"+"="+$("#u16SrsUePeriod").val()+";"
				+"u8PucchBlankRBNum="+$("#u8PucchBlankRBNum").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7";
					}				
				}
			});	
		
		}
		
	});
	//内存值转为显示值
	$("#t_cel_puch1 tr").each(function(index){
		//u8MaxUserPucchfmt1						   
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("4");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("6");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("9");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text() == 3){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("12");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text() == 4){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("18");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(14)").text("36");
		}								   
		//u8DeltaPucchShift						   
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(1)").text("1");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(1)").text("2");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(1)").text("3");
		}
		//u8SRITransPrd
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text("5");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text("10");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text("20");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text() == 3){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text("40");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(5)").text("80");
		}
		//u8GroupHopEnable
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(6)").text("不可用");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(6)").text("可用");
		}
		//u8SeqHopEnable
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(7)").text("不可用");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(7)").text("可用");
		}
		//u8UeTranAntennaSelectEnable
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(9)").text("不可用");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(9)").text("可用");
		}
		//u8UeTranAntennaSelectMode
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(10)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(10)").text("闭环");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(10)").text("开环");
		}
		//u8SrTransMax
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text("4");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text("8");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text("16");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text() == 3){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text("32");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(12)").text("64");
		}
		//u8MaxUserPucchfmt2
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text("2");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text("4");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text("6");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(13)").text("12");
		}
		//u8PuschCqiFbMethd
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(15)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(15)").text("Wideband feedback");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(15)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(15)").text("Higher Layer-configured subband feedback");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(15)").text("UE-selected subbands feedback");
		}
		//u8FormatIndicatorPeriodic
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(16)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(16)").text("WidebandCQI");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(16)").text("SubbandCQI");
		}
		//u8RptCqiPrd
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("5");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("10");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("20");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text() == 3){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("40");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text() == 4){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("80");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(17)").text("160");
		}
		//u8K
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text("1");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text("2");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text("3");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(18)").text("4");
		}
		//u8SimultaneousAckNackAndCQI
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(19)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(19)").text("否");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(19)").text("是");
		}
		//u8AckNackFeedbackMode
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(20)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(20)").text("bundling");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(20)").text("multiplexing");
		}
		//u8PuschHopMode
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(25)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(25)").text("Only inter-subframe");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(25)").text("both intra and inter-subframe");			
		}
		//u8CellSrsEnable
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(28)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(28)").text("关");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(28)").text("开");
		}
		//u8SimulANSrsEnable
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(31)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(31)").text("关");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(31)").text("开");
		}
		//u8SrsMaxUpPts
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(32)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(32)").text("否");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(32)").text("是");
		}
		//u8SrsMaxCodeDivUserNum
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text("1");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text("2");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text("4");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(33)").text("8");
		}
		//u16SrsUePeriod
		if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("2");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 1){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("5");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 2){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("10");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 3){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("20");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 4){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("40");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 5){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("80");
		}else if($("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text() == 6){
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("160");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td:eq(36)").text("320");
		}
		//U8PuschEnable64QAM
		if($("#t_cel_puch1 tr:eq("+index+") td.U8PuschEnable64QAM").text() == 0){
			$("#t_cel_puch1 tr:eq("+index+") td.U8PuschEnable64QAM").text("不能");
		}else{
			$("#t_cel_puch1 tr:eq("+index+") td.U8PuschEnable64QAM").text("能");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7";
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
	$("#t_cel_puch1 tr").each(function(index){
		$("#t_cel_puch1 tr:eq("+index+") td:eq(39)").click(function(){
			var u8CId = $("#t_cel_puch1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_puch1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_puch1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_puch1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_cel_puch1 tr").each(function(index){
		$("#t_cel_puch1 tr:eq("+index+") td:eq(40)").click(function(){
			var u8CId = $("#t_cel_puch1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_puch1 input[type=checkbox]").each(function(index){
			if($("#t_cel_puch1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_puch1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PUCH-3.0.7";
	});
	
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