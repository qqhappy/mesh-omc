$(function(){
	
	var priAddr = $("#au8IPAddr1").val();
	var oper = $("input[name='operType']").val();
	
	//表单校验
	var isNum=/^\d+$/;
	var isIp=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)$/;
	$("#submit_add").click(function(){
		//转移值
		var au8IPAddr1 = $("#au8IPAddr1").val();
		var s = au8IPAddr1.split(".");
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
		if(oper == "config"){
			if($.trim(priAddr) != $.trim(au8IPAddr1)){
				alert("修改IP需要重启单板方可生效");
			}
		}
		$("#au8IPAddr").val(sr);	
		
		var au8NetMask1 = $("#au8NetMask1").val();
		var sq = au8NetMask1.split(".");
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
		$("#au8NetMask").val(sqr);
		
		var au8GWAddr1 = $("#au8GWAddr1").val();
		var sw = au8GWAddr1.split(".");
		var sw1 = parseInt(sw[0]).toString(16);
		if(sw1.length<2){
			sw1 = "0" + sw1;	
		}
		var sw2 = parseInt(sw[1]).toString(16);
		if(sw2.length<2){
			sw2 = "0" + sw2;	
		}
		var sw3 = parseInt(sw[2]).toString(16);
		if(sw3.length<2){
			sw3 = "0" + sw3;	
		}
		var sw4 = parseInt(sw[3]).toString(16);
		if(sw4.length<2){
			sw4 = "0" + sw4;	
		}		
		var swr = sw1+sw2+sw3+sw4;
		$("#au8GWAddr").val(swr); 
									
		var index = 0;
		if(!(isNum.test($("#u8IPID").val()) && $("#u8IPID").val()<=255  && $("#u8IPID").val()>=1)){
			$("#u8IPIDError").text("/* 请输入1~255之间的整数 */");
			index++;
		}else{
			$("#u8IPIDError").text("");
		}
		if(!(isIp.test($("#au8IPAddr1").val()))){
			$("#au8IPAddrError").text("/* 请输入正确的IP地址 */");	
			index++;
		}else if(sr == "FFFFFFFF" || sr == "ffffffff" || sr == "00000000"){
			$("#au8IPAddrError").text("/* 请输入正确的IP地址 */");			
			index++;	
		}else{
			$("#au8IPAddrError").text("");	
		}

		if(sqr == "FFFFFFFF" || sqr == "ffffffff"){
			$("#au8NetMaskError").text("/* 请输入正确的掩码地址 */");		
			index++;	
		}else if(!isNetMaskLegal(sqr)) {
			$("#au8NetMaskError").text("/* 请输入正确的掩码地址 */");	
			index++;
		} else{
			$("#au8NetMaskError").text("");	
		}
		
		if(!(isIp.test($("#au8GWAddr1").val()))){
			$("#au8GWAddrError").text("/* 请输入正确的网关地址 */");	
			index++;
		}else if(swr == "FFFFFFFF" || swr == "ffffffff" ){
			$("#au8GWAddrError").text("/* 请输入正确的网关地址 */");
			index++;	
		}else if(swr == "00000000") {
			$("#au8GWAddrError").text("/* 网关地址不能为0.0.0.0 */");	
			index++;
		}else{
			$("#au8GWAddrError").text("");	
		}
		
		if($("#U8PortID option").length<1){
			$("#U8PortIDError").text("/* 没有可用的端口号选项 */");
			index++;
		}else{
			$("#U8PortIDError").text("");
		}
		var para = "u8IPID"+"="+$("#u8IPID").val()+";"
				+"U8PortID"+"="+$("#U8PortID").val()+";"
				+"au8IPAddr"+"="+$("#au8IPAddr").val()+";"
				+"au8NetMask"+"="+$("#au8NetMask").val()+";"
				+"u8VlanIndex"+"="+$("#u8VlanIndex").val()+";"
				+"au8GWAddr"+"="+$("#au8GWAddr").val();
		$("#parameters").val(para);
		if(index==0){
			var moId = $("#moId").val();
			var basePath=$("#basePath").val();
			var tableName=$("input[name='tableName']").val();
			var operType=$("input[name='operType']").val();
			var isActiveEnbTable=$("#isActiveEnbTable").val();
			var enbVersion = $("#enbVersion").val();
			$.ajax({
				type:"post",
				url:"queryAsyncEnbBiz.do",
				data:"moId="+moId+
					"&basePath="+basePath+
					""+"&browseTime="+getBrowseTime()+"&tableName="+tableName+
					"&operType="+operType+
					"&isActiveEnbTable="+isActiveEnbTable+
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
						
						window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&isActiveEnbTable="+isActiveEnbTable+"";
					}				
				}
			});	
		
			
		}
		
	});
	//内存值转为显示值
	$("#t_ipv4 tr").each(function(index){
		//au8IPAddr//query
		var au8IPAddr = $("#t_ipv4 tr:eq("+index+") td.au8IPAddr").text().split("");	
		var str1 = au8IPAddr[0] + au8IPAddr[1] ;
		var str2 = au8IPAddr[2] + au8IPAddr[3] ;
		var str3 = au8IPAddr[4] + au8IPAddr[5] ;
		var str4 = au8IPAddr[6] + au8IPAddr[7] ;
		var no1 = parseInt(str1,16).toString(10);
		var no2 = parseInt(str2,16).toString(10);
		var no3 = parseInt(str3,16).toString(10);
		var no4 = parseInt(str4,16).toString(10);
		var result1 = no1 + "." + no2 + "." + no3 + "." + no4;		
		$("#t_ipv4 tr:eq("+index+") td.au8IPAddr").text(result1);	
		
		var au8NetMask = $("#t_ipv4 tr:eq("+index+") td.au8NetMask").text().split("");	
		var strr1 = au8NetMask[0] + au8NetMask[1] ;
		var strr2 = au8NetMask[2] + au8NetMask[3] ;
		var strr3 = au8NetMask[4] + au8NetMask[5] ;
		var strr4 = au8NetMask[6] + au8NetMask[7] ;
		var nor1 = parseInt(strr1,16).toString(10);
		var nor2 = parseInt(strr2,16).toString(10);
		var nor3 = parseInt(strr3,16).toString(10);
		var nor4 = parseInt(strr4,16).toString(10);
		var result2 = nor1 + "." + nor2 + "." + nor3 + "." + nor4;		
		$("#t_ipv4 tr:eq("+index+") td.au8NetMask").text(result2);	
		
		
		var au8GWAddrAttr = $("#t_ipv4 tr:eq("+index+") td.au8GWAddr").text();	
		if(au8GWAddrAttr == "" || au8GWAddrAttr == null){
			au8GWAddrAttr = "00000000";
		}
		var au8GWAddr = au8GWAddrAttr.split("");
		var strq1 = au8GWAddr[0] + au8GWAddr[1] ;
		var strq2 = au8GWAddr[2] + au8GWAddr[3] ;
		var strq3 = au8GWAddr[4] + au8GWAddr[5] ;
		var strq4 = au8GWAddr[6] + au8GWAddr[7] ;
		var noq1 = parseInt(strq1,16).toString(10);
		var noq2 = parseInt(strq2,16).toString(10);
		var noq3 = parseInt(strq3,16).toString(10);
		var noq4 = parseInt(strq4,16).toString(10);
		var result3 = noq1 + "." + noq2 + "." + noq3 + "." + noq4;		
		$("#t_ipv4 tr:eq("+index+") td.au8GWAddr").text(result3);
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&isActiveEnbTable="+isActiveEnbTable+"";
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
	$("#t_ipv4 tr").each(function(index){
		$("#t_ipv4 tr:eq("+index+") td:eq(6)").click(function(){
			var u8IPID = $("#t_ipv4 tr:eq("+index+") td:eq(0)").text();
			var para = "u8IPID"+"="+u8IPID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
	var enbVersion = $("#enbVersion").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&referTable=T_ETHPARA&referTableVlan=T_VLAN&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
		});					   
	});	
	//跳转到配置
	$("#t_ipv4 tr").each(function(index){
		if(index != 0){
			$("#t_ipv4 tr:eq("+index+") th:eq(1)").click(function(){
				var u8IPID = $("#t_ipv4 tr:eq("+index+") td:eq(0)").text();
				var para = "u8IPID"+"="+u8IPID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				var isActiveEnbTable = $("#isActiveEnbTable").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&referTable=T_ETHPARA&referTableVlan=T_VLAN&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			});
		}
							   
	});	
	//删除
	$("#t_ipv4 tr").each(function(index){
		$("#t_ipv4 tr:eq("+index+") td:eq(7)").click(function(){
			var u8IPID = $("#t_ipv4 tr:eq("+index+") td:eq(0)").text();
			var para = "u8IPID"+"="+u8IPID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_ipv4 input[type=checkbox]").each(function(index){
			if($("#t_ipv4 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_ipv4 tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8IPID"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var isActiveEnbTable = $("#isActiveEnbTable").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&parameters="+para+"&isActiveEnbTable="+isActiveEnbTable+"";
			}
		}		
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		var isActiveEnbTable = $("#isActiveEnbTable").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_IPV4-3.0.8&isActiveEnbTable="+isActiveEnbTable+"";
	});
});


//十六进制的掩码是否合法
function isNetMaskLegal(netMask) {

	if(netMask == "00000000")
		return false;
	
	var binaryArray = convertHexStringToBinaryArray(netMask);
	binaryArray = binaryArray.split("");
	var zeroOccur = false;
	
	for(var i = 0; i < binaryArray.length; i++) {
		if(binaryArray[i] == "0") {
			zeroOccur = true;
		} else if(binaryArray[i] == "1") {
			if(zeroOccur) {
				return false;
			}
		}
	}
	return true;
}

function convertHexStringToBinaryArray(hexString) {
	hexString = hexString.split("");
	var stringLength = hexString.length;
	var resultStr = "";
	for(var i = 0; i < stringLength; i++) {
		var number = parseInt(hexString[i], 16);
		resultStr += getBinaryArray(number);
	}
	return resultStr;
}

function getBinaryArray(number) {
	if(number == 0) {
		return "0000";
	} else if(number == 1) {
		return "0001";
	} else if(number == 2) {
		return "0010";
	} else if(number == 3) {
		return "0011";
	} else if(number == 4) {
		return "0100";
	} else if(number == 5) {
		return "0101";
	} else if(number == 6) {
		return "0110";
	} else if(number == 7) {
		return "0111";
	} else if(number == 8) {
		return "1000";
	} else if(number == 9) {
		return "1001";
	} else if(number == 10) {
		return "1010";
	} else if(number == 11) {
		return "1011";
	} else if(number == 12) {
		return "1100";
	} else if(number == 13) {
		return "1101";
	} else if(number == 14) {
		return "1110";
	} else if(number == 15) {
		return "1111";
	}
}
