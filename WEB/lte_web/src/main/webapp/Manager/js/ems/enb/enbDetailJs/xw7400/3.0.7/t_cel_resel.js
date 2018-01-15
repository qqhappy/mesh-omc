$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var au8AcBarListSig1 = "0" + $("#au8AcBarListSig1").val();	
		var au8AcBarListSig2 = "0" + $("#au8AcBarListSig2").val();	
		var au8AcBarListSig3 = "0" + $("#au8AcBarListSig3").val();	
		var au8AcBarListSig4 = "0" + $("#au8AcBarListSig4").val();	
		var au8AcBarListSig5 = "0" + $("#au8AcBarListSig5").val();	
		$("#au8AcBarListSig").val(au8AcBarListSig1+au8AcBarListSig2+au8AcBarListSig3+au8AcBarListSig4+au8AcBarListSig5);
		
		var au8AcBarListOrig1 = "0" + $("#au8AcBarListOrig1").val();	
		var au8AcBarListOrig2 = "0" + $("#au8AcBarListOrig2").val();	
		var au8AcBarListOrig3 = "0" + $("#au8AcBarListOrig3").val();	
		var au8AcBarListOrig4 = "0" + $("#au8AcBarListOrig4").val();	
		var au8AcBarListOrig5 = "0" + $("#au8AcBarListOrig5").val();	
		$("#au8AcBarListOrig").val(au8AcBarListOrig1+au8AcBarListOrig2+au8AcBarListOrig3+au8AcBarListOrig4+au8AcBarListOrig5);
		
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if(!(isNum.test($("#u8SelQrxLevMin").val()) && $("#u8SelQrxLevMin").val()<=-44 && $("#u8SelQrxLevMin").val()>=-140)){
			$("#u8SelQrxLevMinError").text("/* 请输入-140~-44之间的整数 */");
			index++;
		}else if($("#u8SelQrxLevMin").val()%2 !=0){
			$("#u8SelQrxLevMinError").text("/* 请注意步长为2 */");
			index++;
		}else{
			$("#u8SelQrxLevMinError").text("");
		}
		if(!(isNum.test($("#u8SNintraSrch").val()) && $("#u8SNintraSrch").val()<=62 && $("#u8SNintraSrch").val()>=0)){
			$("#u8SNintraSrchError").text("/* 请输入0~62之间的整数 */");
			index++;
		}else if($("#u8SNintraSrch").val()%2 !=0){
			$("#u8SNintraSrchError").text("/* 请注意步长为2 */");
			index++;
		}else{
			$("#u8SNintraSrchError").text("");
		}
		if(!(isNum.test($("#u8ThreshSvrLow").val()) && $("#u8ThreshSvrLow").val()<=62 && $("#u8ThreshSvrLow").val()>=0)){
			$("#u8ThreshSvrLowError").text("/* 请输入0~62之间的整数 */");
			index++;
		}else if($("#u8ThreshSvrLow").val()%2 !=0){
			$("#u8ThreshSvrLowError").text("/* 请注意步长为2 */");
			index++;
		}else{
			$("#u8ThreshSvrLowError").text("");
		}
		if(!(isNum.test($("#u8IntraPmax").val()) && $("#u8IntraPmax").val()<=33  && $("#u8IntraPmax").val()>=-30)){
			$("#u8IntraPmaxError").text("/* 请输入-30~33之间的整数 */");
			index++;
		}else{
			$("#u8IntraPmaxError").text("");
		}
		if(!(isNum.test($("#u8IntraQrxLevMin").val()) && $("#u8IntraQrxLevMin").val()<=-44 && $("#u8IntraQrxLevMin").val()>=-140)){
			$("#u8IntraQrxLevMinError").text("/* 请输入-140~-44之间的整数 */");
			index++;
		}else if($("#u8IntraQrxLevMin").val()%2 !=0){
			$("#u8IntraQrxLevMinError").text("/* 请注意步长为2 */");
			index++;
		}else{
			$("#u8IntraQrxLevMinError").text("");
		}
		if(!(isNum.test($("#u8SIntraSrch").val()) && $("#u8SIntraSrch").val()<=62 && $("#u8SIntraSrch").val()>=0)){
			$("#u8SIntraSrchError").text("/* 请输入0~62之间的整数 */");
			index++;
		}else if($("#u8SIntraSrch").val()%2 !=0){
			$("#u8SIntraSrchError").text("/* 请注意步长为2 */");
			index++;
		}else{
			$("#u8SIntraSrchError").text("");
		}
		if(!(isNum.test($("#u8CellSelQqualMin").val()) && $("#u8CellSelQqualMin").val()<=-3  && $("#u8CellSelQqualMin").val()>=-34)){
			$("#u8CellSelQqualMinError").text("/* 请输入-34~-3之间的整数 */");
			index++;
		}else{
			$("#u8CellSelQqualMinError").text("");
		}
		if(!(isNum.test($("#u8SIntraSrchQ").val()) && $("#u8SIntraSrchQ").val()<=31  && $("#u8SIntraSrchQ").val()>=0)){
			$("#u8SIntraSrchQError").text("/* 请输入0~31之间的整数 */");
			index++;
		}else{
			$("#u8SIntraSrchQError").text("");
		}
		if(!(isNum.test($("#u8SNintraSrchQ").val()) && $("#u8SNintraSrchQ").val()<=31  && $("#u8SNintraSrchQ").val()>=0)){
			$("#u8SNintraSrchQError").text("/* 请输入0~31之间的整数 */");
			index++;
		}else{
			$("#u8SNintraSrchQError").text("");
		}
		if(!(isNum.test($("#u8IntraFreqQqualMin").val()) && $("#u8IntraFreqQqualMin").val()<=-3  && $("#u8IntraFreqQqualMin").val()>=-34)){
			$("#u8IntraFreqQqualMinError").text("/* 请输入-34~-3之间的整数 */");
			index++;
		}else{
			$("#u8IntraFreqQqualMinError").text("");
		}
		if(!(isNum.test($("#u8ThreshSrvLowQ").val()) && $("#u8ThreshSrvLowQ").val()<=31  && $("#u8ThreshSrvLowQ").val()>=0)){
			$("#u8ThreshSrvLowQError").text("/* 请输入0~31之间的整数 */");
			index++;
		}else{
			$("#u8ThreshSrvLowQError").text("");
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8CellBarred"+"="+$("input[name='u8CellBarred']:checked").val()+";"
				+"u8IntraFReselInd"+"="+$("input[name='u8IntraFReselInd']:checked").val()+";"
				+"u8SelQrxLevMin"+"="+accDiv(accAdd($("#u8SelQrxLevMin").val(),140),2)+";"
				+"u8QrxLevMinOfst"+"="+$("#u8QrxLevMinOfst").val()+";"
				+"u8AcBarEmerCall"+"="+$("input[name='u8AcBarEmerCall']:checked").val()+";"
				+"u8AcProbFactorSig"+"="+$("#u8AcProbFactorSig").val()+";"
				+"u8AcBarTimeSig"+"="+$("#u8AcBarTimeSig").val()+";"
				+"au8AcBarListSig"+"="+$("#au8AcBarListSig").val()+";"
				+"u8AcProbFactOrig"+"="+$("#u8AcProbFactOrig").val()+";"
				+"u8AcBarTimeOrig"+"="+$("#u8AcBarTimeOrig").val()+";"
				+"au8AcBarListOrig"+"="+$("#au8AcBarListOrig").val()+";"
				+"u8Qhyst"+"="+$("#u8Qhyst").val()+";"
				+"u8SNintraSrchPre"+"="+$("input[name='u8SNintraSrchPre']:checked").val()+";"
				+"u8SNintraSrch"+"="+accDiv($("#u8SNintraSrch").val(),2)+";"
				+"u8ThreshSvrLow"+"="+accDiv($("#u8ThreshSvrLow").val(),2)+";"
				+"u8IntraPmax"+"="+accAdd($("#u8IntraPmax").val(),30)+";"
				+"u8IntraReselPrio"+"="+$("#u8IntraReselPrio").val()+";"
				+"u8IntraQrxLevMin"+"="+accDiv(accAdd($("#u8IntraQrxLevMin").val(),140),2)+";"
				+"u8SIntraSrchPre"+"="+$("input[name='u8SIntraSrchPre']:checked").val()+";"
				+"u8SIntraSrch"+"="+accDiv($("#u8SIntraSrch").val(),2)+";"
				+"u8tRslIntraEutra"+"="+$("#u8tRslIntraEutra").val()+";"
				+"u8CellSelQqualMin"+"="+accAdd($("#u8CellSelQqualMin").val(),34)+";"
				+"u8Qqualminoffset"+"="+$("#u8Qqualminoffset").val()+";"
				+"u8SIntraSrchQ"+"="+$("#u8SIntraSrchQ").val()+";"
				+"u8SNintraSrchQ"+"="+$("#u8SNintraSrchQ").val()+";"
				+"u8IntraFreqQqualMin"+"="+accAdd($("#u8IntraFreqQqualMin").val(),34)+";"
				+"u8ThreshSrvLowQ"+"="+$("#u8ThreshSrvLowQ").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_cel_resel1 tr").each(function(index){
		//				   
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(1)").text("禁止");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(1)").text("允许");
		}	
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(2)").text("是");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(2)").text("否");
		}	
		//
		var u8SelQrxLevMin = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(3)").text());
		var tom = u8SelQrxLevMin*2 - 140;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(3)").text(tom);
		//
		var u8QrxLevMinOfst = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(4)").text());
		var jack = u8QrxLevMinOfst*2;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(4)").text(jack);
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(5)").text("否");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(5)").text("是");
		}
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.05");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 2){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.1");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 3){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.15");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 4){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.2");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 5){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.25");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 6){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.3");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 7){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.4");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 8){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.5");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 9){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.6");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 10){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.7");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 11){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.75");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 12){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.8");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 13){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.85");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text() == 14){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.9");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(6)").text("0.95");
		}
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("4");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("8");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("16");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("32");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("64");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("128");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text() == 6){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("256");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(7)").text("512");
		}
		//
		var au8AcBarListSig = $("#t_cel_resel1 tr:eq("+index+") td:eq(8)").text().split("");
		var au8AcBarListSig1 = "";
		if(parseInt(au8AcBarListSig[1]) == 0){
			au8AcBarListSig1 = "否";
		}else{
			au8AcBarListSig1 = "是";	
		}
		var au8AcBarListSig2 = "";
		if(parseInt(au8AcBarListSig[3]) == 0){
			au8AcBarListSig2 = "否";
		}else{
			au8AcBarListSig2 = "是";	
		}
		var au8AcBarListSig3 = "";
		if(parseInt(au8AcBarListSig[5]) == 0){
			au8AcBarListSig3 = "否";
		}else{
			au8AcBarListSig3 = "是";	
		}
		var au8AcBarListSig4 = "";
		if(parseInt(au8AcBarListSig[7]) == 0){
			au8AcBarListSig4 = "否";
		}else{
			au8AcBarListSig4 = "是";	
		}
		var au8AcBarListSig5 = "";
		if(parseInt(au8AcBarListSig[9]) == 0){
			au8AcBarListSig5 = "否";
		}else{
			au8AcBarListSig5 = "是";	
		}
		$("#t_cel_resel1 tr:eq("+index+") td:eq(8)").text(au8AcBarListSig1 + " , " + au8AcBarListSig2 + " , " + au8AcBarListSig3 + " , "  +au8AcBarListSig4 + " , " + au8AcBarListSig5);
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 1){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.05");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 2){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.1");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 3){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.15");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 4){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.2");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 5){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.25");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 6){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.3");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 7){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.4");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 8){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.5");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 9){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.6");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 10){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.7");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 11){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.75");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 12){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.8");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 13){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.85");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text() == 14){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.9");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(9)").text("0.95");
		}
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("4");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 1){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("8");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 2){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("16");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 3){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("32");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 4){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("64");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 5){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("128");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text() == 6){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("256");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(10)").text("512");
		}
		//
		//
		var au8AcBarListOrig = $("#t_cel_resel1 tr:eq("+index+") td:eq(11)").text().split("");
		var au8AcBarListOrig1 = "";
		if(parseInt(au8AcBarListOrig[1]) == 0){
			au8AcBarListOrig1 = "否";
		}else{
			au8AcBarListOrig1 = "是";	
		}
		var au8AcBarListOrig2 = "";
		if(parseInt(au8AcBarListOrig[3]) == 0){
			au8AcBarListOrig2 = "否";
		}else{
			au8AcBarListOrig2 = "是";	
		}
		var au8AcBarListOrig3 = "";
		if(parseInt(au8AcBarListOrig[5]) == 0){
			au8AcBarListOrig3 = "否";
		}else{
			au8AcBarListOrig3 = "是";	
		}
		var au8AcBarListOrig4 = "";
		if(parseInt(au8AcBarListOrig[7]) == 0){
			au8AcBarListOrig4 = "否";
		}else{
			au8AcBarListOrig4 = "是";	
		}
		var au8AcBarListOrig5 = "";
		if(parseInt(au8AcBarListOrig[9]) == 0){
			au8AcBarListOrig5 = "否";
		}else{
			au8AcBarListOrig5 = "是";	
		}
		$("#t_cel_resel1 tr:eq("+index+") td:eq(11)").text(au8AcBarListOrig1 + " , " + au8AcBarListOrig2 + " , " + au8AcBarListOrig3 + " , "  +au8AcBarListOrig4 + " , " + au8AcBarListOrig5);
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("0");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 1){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("1");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 2){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("2");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 3){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("3");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 4){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("4");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 5){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("5");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 6){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("6");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 7){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("8");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 8){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("10");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 9){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("12");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 10){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("14");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 11){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("16");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 12){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("18");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 13){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("20");
		}else if($("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text() == 14){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("22");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(12)").text("24");
		}
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(13)").text("否");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(13)").text("是");
		}
		//
		var u8SNintraSrch = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(14)").text());
		var helen = u8SNintraSrch*2;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(14)").text(helen);
		//
		var u8ThreshSvrLow = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(15)").text());
		var may = u8ThreshSvrLow*2;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(15)").text(may);
		//
		var u8IntraPmax = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(16)").text());
		var june = u8IntraPmax-30;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(16)").text(june);
		//
		var u8IntraQrxLevMin = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(18)").text());
		var apirl = u8IntraQrxLevMin*2 -140 ;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(18)").text(apirl);
		//
		if($("#t_cel_resel1 tr:eq("+index+") td:eq(19)").text() == 0){
			$("#t_cel_resel1 tr:eq("+index+") td:eq(19)").text("否");
		}else{
			$("#t_cel_resel1 tr:eq("+index+") td:eq(19)").text("是");
		}
		//
		var u8SIntraSrch = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(20)").text());
		var grep = u8SIntraSrch*2;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(20)").text(grep);
		//
		var u8CellSelQqualMin = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(22)").text());
		var black = u8CellSelQqualMin-34;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(22)").text(black);
		//
		var u8IntraFreqQqualMin = parseInt($("#t_cel_resel1 tr:eq("+index+") td:eq(26)").text());
		var white = u8IntraFreqQqualMin-34;
		$("#t_cel_resel1 tr:eq("+index+") td:eq(26)").text(white);
		
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7";
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
	$("#t_cel_resel1 tr").each(function(index){
		$("#t_cel_resel1 tr:eq("+index+") td:eq(28)").click(function(){
			var u8CId = $("#t_cel_resel1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_resel1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_resel1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_resel1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7&parameters="+para+"";
			});	
		}
					   
	});	
	//删除
	$("#t_cel_resel1 tr").each(function(index){
		$("#t_cel_resel1 tr:eq("+index+") td:eq(29)").click(function(){
			var u8CId = $("#t_cel_resel1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_resel1 input[type=checkbox]").each(function(index){
			if($("#t_cel_resel1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_resel1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_RESEL-3.0.7";
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