$(function(){
	//表单校验
	var isNum=/^\d+$/;
	var isIp=/^((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0))|0$/;
	$("#submit_add").click(function(){
									
		//转移值
		var au8DstIP1q = $("#au8DstIP1q").val()+"";
		var sr = "00000000";
		if(au8DstIP1q != "0"){
			var s = au8DstIP1q.split(".");
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
			sr = s1+s2+s3+s4;
		}		
		$("#au8DstIP1").val(sr);	
		
		var au8DstIP2q = $("#au8DstIP2q").val()+"";
		var sqr = "00000000";
		if(au8DstIP2q != "0"){
			var sq = au8DstIP2q.split(".");
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
			sqr = sq1+sq2+sq3+sq4;
		}		
		$("#au8DstIP2").val(sqr);
		
		var au8DstIP3q = $("#au8DstIP3q").val()+"";
		var swr = "00000000";
		if(au8DstIP3q != "0"){
			var sw = au8DstIP3q.split(".");
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
			swr = sw1+sw2+sw3+sw4;
		}		
		$("#au8DstIP3").val(swr);
									
		var au8DstIP4q = $("#au8DstIP4q").val()+"";
		var sar = "00000000";
		if(au8DstIP4q != "0"){
			var sa = au8DstIP4q.split(".");
			var sa1 = parseInt(sa[0]).toString(16);
			if(sa1.length<2){
				sa1 = "0" + sa1;	
			}
			var sa2 = parseInt(sa[1]).toString(16);
			if(sa2.length<2){
				sa2 = "0" + sa2;	
			}
			var sa3 = parseInt(sa[2]).toString(16);
			if(sa3.length<2){
				sa3 = "0" + sa3;	
			}
			var sa4 = parseInt(sa[3]).toString(16);
			if(sa4.length<2){
				sa4 = "0" + sa4;	
			}		
			sar = sa1+sa2+sa3+sa4;
		}		
		$("#au8DstIP4").val(sar);							
									
									
		var index = 0;
		if(!(isNum.test($("#u16AssID").val()) && $("#u16AssID").val()<=48  && $("#u16AssID").val()>=1)){
			$("#u16AssIDError").text("/* 请输入1~48之间的整数 */");
			index++;
		}else{
			$("#u16AssIDError").text("");
		}
		if(!(isNum.test($("#u16LocPort").val()) && $("#u16LocPort").val()<=65535  && $("#u16LocPort").val()>=0)){
			$("#u16LocPortError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16LocPortError").text("");
		}
		if(!(isNum.test($("#u16RemotPort").val()) && $("#u16RemotPort").val()<=65535  && $("#u16RemotPort").val()>=0)){
			$("#u16RemotPortError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u16RemotPortError").text("");
		}
		if(!(isNum.test($("#u8InOutStream").val()) && $("#u8InOutStream").val()<=255  && $("#u8InOutStream").val()>=0)){
			$("#u8InOutStreamError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8InOutStreamError").text("");
		}
		
		if(!(isIp.test($("#au8DstIP1q").val()))){
			$("#au8DstIP1qError").text("/* 请输入正确的IP地址 */");	
			index++;
		}else{
			$("#au8DstIP1qError").text("");	
		}
		if(!(isIp.test($("#au8DstIP2q").val()))){
			$("#au8DstIP2qError").text("/* 请输入正确的IP地址 */");	
			index++;
		}else{
			$("#au8DstIP2qError").text("");	
		}
		if(!(isIp.test($("#au8DstIP3q").val()))){
			$("#au8DstIP3qError").text("/* 请输入正确的IP地址 */");	
			index++;
		}else{
			$("#au8DstIP3qError").text("");	
		}
		if(!(isIp.test($("#au8DstIP4q").val()))){
			$("#au8DstIP4qError").text("/* 请输入正确的IP地址 */");	
			index++;
		}else{
			$("#au8DstIP4qError").text("");	
		}
		if($("#u8SrcIPID1 option").length<1){
			$("#u8SrcIPIDError1").text("/* 没有可用的源IP地址选项 */");
			index++;
		}else{
			$("#u8SrcIPIDError1").text("");
		}
		if($("#u8SrcIPID2 option").length<1){
			$("#u8SrcIPIDError2").text("/* 没有可用的源IP地址选项 */");
			index++;
		}else{
			$("#u8SrcIPIDError2").text("");
		}
		if($("#u8SrcIPID3 option").length<1){
			$("#u8SrcIPIDError3").text("/* 没有可用的源IP地址选项 */");
			index++;
		}else{
			$("#u8SrcIPIDError3").text("");
		}
		if($("#u8SrcIPID4 option").length<1){
			$("#u8SrcIPIDError4").text("/* 没有可用的源IP地址选项 */");
			index++;
		}else{
			$("#u8SrcIPIDError4").text("");
		}
		//判断两两不等							
		for(var i=1;i<5;i++){
			for(var j=i+1;j<5;j++){
				if(($("#u8SrcIPID"+i).val() == $("#u8SrcIPID"+j).val()) && $("#u8SrcIPID"+i).val() != 0 ){
					index++;
					$("#u8SrcIPIDError"+i).text("/* 源IP地址除为0外不可相同 */");
					$("#u8SrcIPIDError"+j).text("/* 源IP地址除为0外不可相同 */");
					return ;
				}else{
					$("#u8SrcIPIDError"+i).text("");
					$("#u8SrcIPIDError"+j).text("");
				}
			}
			
		}
		for(var i=1;i<5;i++){
			for(var j=i+1;j<5;j++){
				if(!isIp.test($("#au8DstIP"+i+"q").val())){
					index++;
					$("#au8DstIP"+i+"qError").text("/* 请输入正确的IP地址 */");
					return;
				}
				if(!isIp.test($("#au8DstIP"+j+"q").val())){
					index++;
					$("#au8DstIP"+j+"qError").text("/* 请输入正确的IP地址 */");
					return;
				}
				if(($("#au8DstIP"+i).val() == $("#au8DstIP"+j).val()) && $("#au8DstIP"+i).val() != "00000000"){
					index++;
					$("#au8DstIP"+i+"qError").text("/* 目的IP地址除为0外不可相同 */");
					$("#au8DstIP"+j+"qError").text("/* 目的IP地址除为0外不可相同 */");
					return ;
				}else{
					$("#au8DstIP"+i+"qError").text("");
					$("#au8DstIP"+j+"qError").text("");
				}
			}
			
		}
		var para = "u16AssID"+"="+$("#u16AssID").val()+";"
				+"u8SrcIPID1"+"="+$("#u8SrcIPID1").val()+";"
				+"u8SrcIPID2"+"="+$("#u8SrcIPID2").val()+";"
				+"u8SrcIPID3"+"="+$("#u8SrcIPID3").val()+";"
				+"u8SrcIPID4"+"="+$("#u8SrcIPID4").val()+";"
				+"au8DstIP1"+"="+$("#au8DstIP1").val()+";"
				+"au8DstIP2"+"="+$("#au8DstIP2").val()+";"
				+"au8DstIP3"+"="+$("#au8DstIP3").val()+";"
				+"au8DstIP4"+"="+$("#au8DstIP4").val()+";"
				+"u16LocPort"+"="+$("#u16LocPort").val()+";"
				+"u16RemotPort"+"="+$("#u16RemotPort").val()+";"
				+"u8InOutStream"+"="+$("#u8InOutStream").val()+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
		$("#parameters").val(para);
		
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_sctp1 tr").each(function(index){
		
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		var ipId1 = $("#t_sctp1 tr:eq("+index+") td.u8SrcIPID1").text();
		if(ipId1 != "0"){
			$("#t_sctp1 tr:eq("+index+") td.u8SrcIPID1").text(ipId1+" ("+queryIpDetail(basePath,ipId1,moId)+")");
		}
		var ipId2 = $("#t_sctp1 tr:eq("+index+") td.u8SrcIPID2").text();
		if(ipId2 != "0"){			
			$("#t_sctp1 tr:eq("+index+") td.u8SrcIPID2").text(ipId2+" ("+queryIpDetail(basePath,ipId2,moId)+")");
		}
		var ipId3 = $("#t_sctp1 tr:eq("+index+") td.u8SrcIPID3").text();
		if(ipId3 != "0"){
			$("#t_sctp1 tr:eq("+index+") td.u8SrcIPID3").text(ipId3+" ("+queryIpDetail(basePath,ipId3,moId)+")");	
		}
		var ipId4 = $("#t_sctp1 tr:eq("+index+") td.u8SrcIPID4").text();
		if(ipId4 != "0"){
			$("#t_sctp1 tr:eq("+index+") td.u8SrcIPID4").text(ipId4+" ("+queryIpDetail(basePath,ipId4,moId)+")");			
		}
		//u8ManualOP
		if($("#t_sctp1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_sctp1 tr:eq("+index+") td:eq(12)").text("解闭塞");
		}else{
			$("#t_sctp1 tr:eq("+index+") td:eq(12)").text("闭塞");
		}	
		//u32Status
		if($("#t_sctp1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_sctp1 tr:eq("+index+") td:eq(13)").text("正常");
		}else{
			$("#t_sctp1 tr:eq("+index+") td:eq(13)").text("不正常");
		}	
		
		var au8DstIP1 = $("#t_sctp1 tr:eq("+index+") td:eq(5)").text();	
		var result1 = "0";
		if(au8DstIP1 != "00000000"){
			au8DstIP1 = au8DstIP1.split("");
			var str1 = au8DstIP1[0] + au8DstIP1[1] ;
			var str2 = au8DstIP1[2] + au8DstIP1[3] ;
			var str3 = au8DstIP1[4] + au8DstIP1[5] ;
			var str4 = au8DstIP1[6] + au8DstIP1[7] ;
			var no1 = parseInt(str1,16).toString(10);
			var no2 = parseInt(str2,16).toString(10);
			var no3 = parseInt(str3,16).toString(10);
			var no4 = parseInt(str4,16).toString(10);
			result1 = no1 + "." + no2 + "." + no3 + "." + no4;	
		}			
		$("#t_sctp1 tr:eq("+index+") td:eq(5)").text(result1);	
		
		var au8DstIP2 = $("#t_sctp1 tr:eq("+index+") td:eq(6)").text();	
		var result2 = "0";
		if(au8DstIP2 != "00000000"){
			au8DstIP2 = au8DstIP2.split("");
			var strr1 = au8DstIP2[0] + au8DstIP2[1] ;
			var strr2 = au8DstIP2[2] + au8DstIP2[3] ;
			var strr3 = au8DstIP2[4] + au8DstIP2[5] ;
			var strr4 = au8DstIP2[6] + au8DstIP2[7] ;
			var nor1 = parseInt(strr1,16).toString(10);
			var nor2 = parseInt(strr2,16).toString(10);
			var nor3 = parseInt(strr3,16).toString(10);
			var nor4 = parseInt(strr4,16).toString(10);
			result2 = nor1 + "." + nor2 + "." + nor3 + "." + nor4;	
		}			
		$("#t_sctp1 tr:eq("+index+") td:eq(6)").text(result2);	
		
		var au8DstIP3 = $("#t_sctp1 tr:eq("+index+") td:eq(7)").text();	
		var result3 = "0";
		if(au8DstIP3 != "00000000"){
			au8DstIP3 = au8DstIP3.split("");
			var strq1 = au8DstIP3[0] + au8DstIP3[1] ;
			var strq2 = au8DstIP3[2] + au8DstIP3[3] ;
			var strq3 = au8DstIP3[4] + au8DstIP3[5] ;
			var strq4 = au8DstIP3[6] + au8DstIP3[7] ;
			var noq1 = parseInt(strq1,16).toString(10);
			var noq2 = parseInt(strq2,16).toString(10);
			var noq3 = parseInt(strq3,16).toString(10);
			var noq4 = parseInt(strq4,16).toString(10);
			result3 = noq1 + "." + noq2 + "." + noq3 + "." + noq4;	
		}			
		$("#t_sctp1 tr:eq("+index+") td:eq(7)").text(result3);
		
		var au8DstIP4 = $("#t_sctp1 tr:eq("+index+") td:eq(8)").text();	
		var result4 = "0";
		if(au8DstIP4 != "00000000"){
			au8DstIP4 = au8DstIP4.split("");
			var strw1 = au8DstIP4[0] + au8DstIP4[1] ;
			var strw2 = au8DstIP4[2] + au8DstIP4[3] ;
			var strw3 = au8DstIP4[4] + au8DstIP4[5] ;
			var strw4 = au8DstIP4[6] + au8DstIP4[7] ;
			var now1 = parseInt(strw1,16).toString(10);
			var now2 = parseInt(strw2,16).toString(10);
			var now3 = parseInt(strw3,16).toString(10);
			var now4 = parseInt(strw4,16).toString(10);
			result4 = now1 + "." + now2 + "." + now3 + "." + now4;	
		}			
		$("#t_sctp1 tr:eq("+index+") td:eq(8)").text(result4);
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5";
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
	$("#t_sctp1 tr").each(function(index){
		$("#t_sctp1 tr:eq("+index+") td:eq(14)").click(function(){
			var u16AssID = $("#t_sctp1 tr:eq("+index+") td:eq(0)").text();
			var para = "u16AssID"+"="+u16AssID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5&referTable=T_IPV4&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_sctp1 tr").each(function(index){
		if(index != 0){
			$("#t_sctp1 tr:eq("+index+") th:eq(1)").click(function(){
				var u16AssID = $("#t_sctp1 tr:eq("+index+") td:eq(0)").text();
				var para = "u16AssID"+"="+u16AssID;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5&referTable=T_IPV4&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_sctp1 tr").each(function(index){
		$("#t_sctp1 tr:eq("+index+") td:eq(15)").click(function(){
			var u16AssID = $("#t_sctp1 tr:eq("+index+") td:eq(0)").text();
			var para = "u16AssID"+"="+u16AssID;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_sctp1 input[type=checkbox]").each(function(index){
			if($("#t_sctp1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_sctp1 tr:eq("+index+") td:eq(0)").text();
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
			var s = "u16AssID"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SCTP-1.1.5";
	});
});