$(function(){
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
									
		var u32Date = $("#timepicker").val();
		var str = u32Date.split("-");
		var s1 = str[0];
		var s2 = str[1];
		var s3 = str[2];
		var coco1 = parseInt(s1,10).toString(2)+"";
		var coco2 = parseInt(s2,10).toString(2)+"";
		var lengthco2 = coco2.length;
		if(lengthco2<8){
			for(var i=0;i<8-lengthco2;i++){
				coco2 = "0" + coco2;	
			}	
		}
		var coco3 = parseInt(s3,10).toString(2)+"";
		var lengthco3 = coco3.length;
		if(lengthco3<8){
			for(var j=0;j<8-lengthco3;j++){
				coco3 = "0" + coco3;	
			}	
		}
		var date2 = coco1+coco2+coco3;
		var result = parseInt(date2,2).toString(10);
		$("#timepickerq").val(result);							
									
		var index = 0;
		if(!(isNum.test($("#u8SWID").val()) && $("#u8SWID").val()<=255  && $("#u8SWID").val()>=0)){
			$("#u8SWIDError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8SWIDError").text("");
		}
		if($("#au8SwVer").val().length <1){
			$("#au8SwVerError").text("/* 请输入软件版本号 */");
			index++;
		}else if($("#au8SwVer").val().length >43){
			$("#au8SwVerError").text("/* 软件版本号过长 */");
			index++;
		}else{
			$("#au8SwVerError").text("");
		}
		
		if($("#au8FileName").val().length <1){
			$("#au8FileNameError").text("/* 请输入文件名 */");
			index++;
		}else if($("#au8FileName").val().length >127){
			$("#au8FileNameError").text("/* 文件名过长 */");
			index++;
		}else{
			$("#au8FileNameError").text("");
		}
		if($("#u8PkgIndx option").length<1){
			$("#u8PkgIndxError").text("/* 没有可用的包序号选项 */");
			index++;
		}else{
			$("#u8PkgIndxError").text("");
		}
		var para = "u8PkgIndx"+"="+$("#u8PkgIndx").val()+";"
				+"u8SWID"+"="+$("#u8SWID").val()+";"
				+"au8SwVer"+"="+$("#au8SwVer").val()+";"
				+"au8FileName"+"="+$("#au8FileName").val()+";"
				+"u32Date"+"="+$("#timepickerq").val();
		$("#parameters").val(para);
			
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	$("#submit_config").click(function(){
		
		var u32Date = $("#timepicker").val();
		var str = u32Date.split("-");
		var s1 = str[0];
		var s2 = str[1];
		var s3 = str[2];
		var coco1 = parseInt(s1,10).toString(2)+"";
		var coco2 = parseInt(s2,10).toString(2)+"";
		var lengthco2 = coco2.length;
		if(lengthco2<8){
			for(var i=0;i<8-lengthco2;i++){
				coco2 = "0" + coco2;	
			}	
		}
		var coco3 = parseInt(s3,10).toString(2)+"";
		var lengthco3 = coco3.length;
		if(lengthco3<8){
			for(var j=0;j<8-lengthco3;j++){
				coco3 = "0" + coco3;	
			}	
		}
		var date2 = coco1+coco2+coco3;
		var result = parseInt(date2,2).toString(10);
		$("#timepickerq").val(result);							
									
		var index = 0;
		if(!(isNum.test($("#u8SWID").val()) && $("#u8SWID").val()<=255  && $("#u8SWID").val()>=0)){
			$("#u8SWIDError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8SWIDError").text("");
		}
		if($("#au8SwVer").val().length <1){
			$("#au8SwVerError").text("/* 请输入软件版本号 */");
			index++;
		}else if($("#au8SwVer").val().length >43){
			$("#au8SwVerError").text("/* 软件版本号过长 */");
			index++;
		}else{
			$("#au8SwVerError").text("");
		}
		
		if($("#au8FileName").val().length <1){
			$("#au8FileNameError").text("/* 请输入文件名 */");
			index++;
		}else if($("#au8FileName").val().length >127){
			$("#au8FileNameError").text("/* 文件名过长 */");
			index++;
		}else{
			$("#au8FileNameError").text("");
		}
		var para = "u8PkgIndx"+"="+$("#u8PkgIndx").val()+";"
				+"u8SWID"+"="+$("#u8SWID").val()+";"
				+"au8SwVer"+"="+$("#au8SwVer").val()+";"
				+"au8FileName"+"="+$("#au8FileName").val()+";"
				+"u32Date"+"="+$("#timepickerq").val();
		$("#parameters").val(para);
			
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示�?
	$("#t_swinfo tr").each(function(index){		
		var u32Date = $("#t_swinfo tr:eq("+index+") td:eq(4)").text();
		var date = parseInt(u32Date,10).toString(2);
		var length = date.length;
		if(length<32){
			for(var i = 0 ;i<32-length ;i++){
				date = "0" + date;	
			}	
		}
		var dateStr = date.split("");
		var str1 = "";
		var str2 = "";
		var str3 = "";
		for(var i=0;i<16;i++){
			str1 = str1+dateStr[i]	;
		}
		for(var i=16;i<24;i++){
			str2 = str2+dateStr[i]	;
		}
		for(var i=24;i<32;i++){
			str3 = str3+dateStr[i]	;
		}
		var num1 = parseInt(str1,2).toString(10)+"";
		var num2 = parseInt(str2,2).toString(10)+"";
		if(num2.length<2){
			num2 = "0"+num2;	
		}
		var num3 = parseInt(str3,2).toString(10)+"";
		if(num3.length<2){
			num3 = "0"+num3;	
		}
		var result = num1 + "-" + num2 + "-"  + num3;
		$("#t_swinfo tr:eq("+index+") td:eq(4)").text(result);	
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5";
	});
	//全�?
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
//	//跳转到配�?
//	$("#t_swinfo tr").each(function(index){
//		$("#t_swinfo tr:eq("+index+") td:eq(5)").click(function(){
//			var u8PkgIndx = $("#t_swinfo tr:eq("+index+") td:eq(0)").text();
//			var u8SWID = $("#t_swinfo tr:eq("+index+") td:eq(1)").text();
//			var para = "u8PkgIndx"+"="+u8PkgIndx+";"
//							+"u8SWID"+"="+u8SWID;
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5&parameters="+para+"";
//		});					   
//	});	
//	//跳转到配�?
//	$("#t_swinfo tr").each(function(index){
//		if(index != 0){
//			$("#t_swinfo tr:eq("+index+") th:eq(1)").click(function(){
//				var u8PkgIndx = $("#t_swinfo tr:eq("+index+") td:eq(0)").text();
//				var u8SWID = $("#t_swinfo tr:eq("+index+") td:eq(1)").text();
//				var para = "u8PkgIndx"+"="+u8PkgIndx+";"
//								+"u8SWID"+"="+u8SWID;
//				var basePath = $("#basePath").val();
//				var moId = $("#moId").val();
//				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5&parameters="+para+"";
//			});	
//		}
//						   
//	});	
//	//删除
//	$("#t_swinfo tr").each(function(index){
//		$("#t_swinfo tr:eq("+index+") td:eq(6)").click(function(){
//			var u8PkgIndx = $("#t_swinfo tr:eq("+index+") td:eq(0)").text();
//			var u8SWID = $("#t_swinfo tr:eq("+index+") td:eq(1)").text();
//			var para = "u8PkgIndx"+"="+u8PkgIndx+";"
//							+"u8SWID"+"="+u8SWID;
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			if(confirm("确定要删除该条记�?")){
//				window.location.href=""+basePath+"/lte/queryEnbBiz.do?operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5&parameters="+para+"";
//			}
//		});					   
//	});	
	//批量删除
	$("#delete").click(function(){
		var str1=[];var str2=[];
		$("#t_swinfo input[type=checkbox]").each(function(index){
			if($("#t_swinfo input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#t_swinfo tr:eq("+index+") td:eq(0)").text();
				var temp2 = $("#t_swinfo tr:eq("+index+") td:eq(1)").text();
				str1.push(temp1);
				str2.push(temp2);
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
		var para = "";
		for(var i=0;i<str1.length;i++){
			var s = "u8PkgIndx"+"="+str1[i]+","
						+"u8SWID"+"="+str2[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录 ?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_SWINFO-2.1.5";
	});
});