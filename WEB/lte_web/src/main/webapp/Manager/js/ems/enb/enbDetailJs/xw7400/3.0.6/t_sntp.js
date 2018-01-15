$(function(){
	//表单校验
	var isNum=/^\d+$/;
	var isIp=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)$/;
	$("#submit_add").click(function(){
		//转移值
		var au8SntpMServer1 = $("#au8SntpMServer1").val();
		var s = au8SntpMServer1.split(".");
		var s1 = parseInt(s[0]).toString(16);
		if(s1.length<2){
			s1 = "0" + s1;	
		}
		var s2 = parseInt(s[1]).toString(16);
		if(s2.length<2){
			s2 = "0" + s2;	
		}
		var s3 = parseInt(s[2]).toString(16);
		if(s3.length<2){
			s3 = "0" + s3;	
		}
		var s4 = parseInt(s[3]).toString(16);
		if(s4.length<2){
			s4 = "0" + s4;	
		}		
		var sr = s1+s2+s3+s4;
		$("#au8SntpMServer").val(sr);	
		
		var au8SntpSServer1 = $("#au8SntpSServer1").val();
		var sq = au8SntpSServer1.split(".");
		var sq1 = parseInt(sq[0]).toString(16);
		if(sq1.length<2){
			sq1 = "0" + sq1;	
		}
		var sq2 = parseInt(sq[1]).toString(16);
		if(sq2.length<2){
			sq2 = "0" + sq2;	
		}
		var sq3 = parseInt(sq[2]).toString(16);
		if(sq3.length<2){
			sq3 = "0" + sq3;	
		}
		var sq4 = parseInt(sq[3]).toString(16);
		if(sq4.length<2){
			sq4 = "0" + sq4;	
		}		
		var sqr = sq1+sq2+sq3+sq4;
		$("#au8SntpSServer").val(sqr);							
									
							
		var index = 0;
		if(!(isNum.test($("#u8SntpID").val()) && $("#u8SntpID").val()<=255  && $("#u8SntpID").val()>=0)){
			$("#u8SntpIDError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8SntpIDError").text("");
		}
		if(!(isNum.test($("#u16SyncCycle").val()) && $("#u16SyncCycle").val()<=1440 && $("#u16SyncCycle").val()>=30)){
			$("#u16SyncCycleError").text("/* 请输入30~1440之间的整数 */");
			index++;
		}else{
			$("#u16SyncCycleError").text("");
		}
		if(!(isIp.test($("#au8SntpMServer1").val()))){
			$("#au8SntpMServer1Error").text("/* 请输入正确的服务器地址 */");	
			index++;
		}else if(sr == "FFFFFFFF" || sr == "ffffffff" || sr == "00000000"){
			$("#au8SntpMServer1Error").text("/* 请输入正确的服务器地址 */");	
			index++;	
		}else if(sr == sqr){
			$("#au8SntpMServer1Error").text("/* 主、备服务器地址不可相同 */");	
			index++;	
		}else{
			$("#au8SntpMServer1Error").text("");	
		}
		
		if(!(isIp.test($("#au8SntpSServer1").val()))){
			$("#au8SntpSServer1Error").text("/* 请输入正确的服务器地址 */");	
			index++;
		}else if(sqr == "FFFFFFFF" || sqr == "ffffffff"){
			$("#au8SntpSServer1Error").text("/* 请输入正确的服务器地址 */");
			index++;	
		}else if(sr == sqr){
			$("#au8SntpSServer1Error").text("/* 主、备服务器地址不可相同 */");
			index++;	
		}else{
			$("#au8SntpSServer1Error").text("");	
		}
		var para = "u8SntpID"+"="+$("#u8SntpID").val()+";"
				+"u8IsSntpUse"+"="+$("input[name='u8IsSntpUse']:checked").val()+";"
				+"au8SntpMServer"+"="+$("#au8SntpMServer").val()+";"
				+"au8SntpSServer"+"="+$("#au8SntpSServer").val()+";"
				+"u16SyncCycle"+"="+$("#u16SyncCycle").val()+";"
				+"u16SntpPort"+"="+$("#u16SntpPort").val()+";"
				+"u8TimeZone"+"="+$("#u8TimeZone").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_sntp tr").each(function(index){
		//
		if($("#t_sntp tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_sntp tr:eq("+index+") td:eq(1)").text("不启用");
		}else{
			$("#t_sntp tr:eq("+index+") td:eq(1)").text("启用");
		}
		
		var au8SntpMServer = $("#t_sntp tr:eq("+index+") td:eq(2)").text().split("");	
		var str1 = au8SntpMServer[0] + au8SntpMServer[1] ;
		var str2 = au8SntpMServer[2] + au8SntpMServer[3] ;
		var str3 = au8SntpMServer[4] + au8SntpMServer[5] ;
		var str4 = au8SntpMServer[6] + au8SntpMServer[7] ;
		var no1 = parseInt(str1,16).toString(10);
		var no2 = parseInt(str2,16).toString(10);
		var no3 = parseInt(str3,16).toString(10);
		var no4 = parseInt(str4,16).toString(10);
		var result1 = no1 + "." + no2 + "." + no3 + "." + no4;		
		$("#t_sntp tr:eq("+index+") td:eq(2)").text(result1);	
		
		var au8SntpSServer = $("#t_sntp tr:eq("+index+") td:eq(3)").text().split("");	
		var strr1 = au8SntpSServer[0] + au8SntpSServer[1] ;
		var strr2 = au8SntpSServer[2] + au8SntpSServer[3] ;
		var strr3 = au8SntpSServer[4] + au8SntpSServer[5] ;
		var strr4 = au8SntpSServer[6] + au8SntpSServer[7] ;
		var nor1 = parseInt(strr1,16).toString(10);
		var nor2 = parseInt(strr2,16).toString(10);
		var nor3 = parseInt(strr3,16).toString(10);
		var nor4 = parseInt(strr4,16).toString(10);
		var result2 = nor1 + "." + nor2 + "." + nor3 + "." + nor4;		
		$("#t_sntp tr:eq("+index+") td:eq(3)").text(result2);	
	});	
	$("#t_sntp td.u8TimeZone").each(function(){
		var value = $.trim($(this).text());
		switch (value){
		case "0":
			$(this).text("西12区");
			break;
		case "1":
			$(this).text("西11区");
			break;
		case "2":
			$(this).text("西10区");
			break;
		case "3":
			$(this).text("西9区");
			break;
		case "4":
			$(this).text("西8区");
			break;
		case "5":
			$(this).text("西7区");
			break;
		case "6":
			$(this).text("西6区");
			break;
		case "7":
			$(this).text("西5区");
			break;
		case "8":
			$(this).text("西4区");
			break;
		case "9":
			$(this).text("西3区");
			break;
		case "10":
			$(this).text("西2区");
			break;
		case "11":
			$(this).text("西1区");
			break;
		case "12":
			$(this).text("0区");
			break;
		case "13":
			$(this).text("东1区");
			break;
		case "14":
			$(this).text("东2区");
			break;
		case "15":
			$(this).text("东3区");
			break;
		case "16":
			$(this).text("东4区");
			break;
		case "17":
			$(this).text("东5区");
			break;
		case "18":
			$(this).text("东6区");
			break;
		case "19":
			$(this).text("东7区");
			break;
		case "20":
			$(this).text("东8区");
			break;
		case "21":
			$(this).text("东9区");
			break;
		case "22":
			$(this).text("东10区");
			break;
		case "23":
			$(this).text("东11区");
			break;
		case "24":
			$(this).text("东12区");
			break;
		}	
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6";
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
	$("#t_sntp tr").each(function(index){
		$("#t_sntp tr:eq("+index+") td:eq(7)").click(function(){
			var u8SntpID = $("#t_sntp tr:eq("+index+") td:eq(0)").text();
			var para = "u8SntpID"+"="+u8SntpID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_sntp tr").each(function(index){
		if(index != 0){
			$("#t_sntp tr:eq("+index+") th:eq(1)").click(function(){
				var u8SntpID = $("#t_sntp tr:eq("+index+") td:eq(0)").text();
				var para = "u8SntpID"+"="+u8SntpID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_sntp tr").each(function(index){
		$("#t_sntp tr:eq("+index+") td:eq(8)").click(function(){
			var u8SntpID = $("#t_sntp tr:eq("+index+") td:eq(0)").text();
			var para = "u8SntpID"+"="+u8SntpID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_sntp input[type=checkbox]").each(function(index){
			if($("#t_sntp input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_sntp tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8SntpID"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SNTP-3.0.6";
	});
});