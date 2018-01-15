$(function(){
	//表单校验
	var isNum=/^\d+$/;
	var isLit = /^(-)?\d+(\.[0-9]{1,3}){0,1}$/;
	var isCoco=/^[a-zA-Z0-9]+$/;
	$("#submit_add").click(function(){
		
		var u32Latitude = $("#u32Latitude").val();
		$("#u32Latitude1").val(accMul(accAdd(u32Latitude,90),1000));
		
		var u32Longitude = $("#u32Longitude").val();
		$("#u32Longitude1").val(accMul(accAdd(u32Longitude,180),1000));
		
		var index = 0;
		if(!(isNum.test($("#u32DevID").val()) && $("#u32DevID").val()<=65535  && $("#u32DevID").val()>=0)){
			$("#u32DevIDError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u32DevIDError").text("");
		}
		var u8DevName = $("#u8DevName").val() + "";
		var length = u8DevName.length;
		if(length <1){
			$("#u8DevNameError").text("/* 请输入名称 */");
			index++;
		}else if(length >128){
			$("#u8DevNameError").text("/* 名称过长 */");
			index++;
		}else if(!isCoco.test(u8DevName)){
			$("#u8DevNameError").text("/* 只可输入数字和字母 */");
			index++;
		}else {
			$("#u8DevNameError").text("");	
		}
		if(!(isLit.test($("#u32Latitude").val()) && $("#u32Latitude").val()<=90 && $("#u32Latitude").val()>=-90)){
			$("#u32LatitudeError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u32LatitudeError").text("");
		}
		if(!(isLit.test($("#u32Longitude").val()) && $("#u32Longitude").val()<=180  && $("#u32Longitude").val()>=-180)){
			$("#u32LongitudeError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u32LongitudeError").text("");
		}
		var para = "u32DevID"+"="+$("#u32DevID").val()+";"
				+"u8DevName"+"="+$("#u8DevName").val()+";"
				+"u8DevType"+"="+$("input[name='u8DevType']:checked").val()+";"
				+"u32Longitude"+"="+accMul(accAdd(u32Longitude,180),1000)+";"
				+"u32Latitude"+"="+accMul(accAdd(u32Latitude,90),1000)+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
		$("#parameters").val(para);
		
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	$("#submit_config").click(function(){
		
		var u32Latitude = $("#u32Latitude").val();
		$("#u32Latitude1").val(accMul(accAdd(u32Latitude,90),1000));
		
		var u32Longitude = $("#u32Longitude").val();
		$("#u32Longitude1").val(accMul(accAdd(u32Longitude,180),1000));
		
		var index = 0;
		if(!(isNum.test($("#u32DevID").val()) && $("#u32DevID").val()<=65535  && $("#u32DevID").val()>=0)){
			$("#u32DevIDError").text("/* 请输入0~65535之间的整数 */");
			index++;
		}else{
			$("#u32DevIDError").text("");
		}
		if($("#u8DevName").val().length <1){
			$("#u8DevNameError").text("/* 请输入名称 */");
			index++;
		}else if($("#u8DevName").val().length >127){
			$("#u8DevNameError").text("/* 名称过长 */");
			index++;
		}else{
			$("#u8DevNameError").text("");
		}
		if(!(isLit.test($("#u32Latitude").val()) && $("#u32Latitude").val()<=90 && $("#u32Latitude").val()>=-90)){
			$("#u32LatitudeError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u32LatitudeError").text("");
		}
		if(!(isLit.test($("#u32Longitude").val()) && $("#u32Longitude").val()<=180  && $("#u32Longitude").val()>=-180)){
			$("#u32LongitudeError").text("/* 请输入符合要求的数字 */");
			index++;
		}else{
			$("#u32LongitudeError").text("");
		}
		var para = "u32DevID"+"="+$("#u32DevID").val()+";"
				+"u8DevName"+"="+$("#u8DevName").val()+";"
				+"u8DevType"+"="+$("input[name='u8DevType']:checked").val()+";"
				+"u32Longitude"+"="+accMul(accAdd(u32Longitude,180),1000)+";"
				+"u32Latitude"+"="+accMul(accAdd(u32Latitude,90),1000)+";"
				+"u8ManualOP"+"="+$("input[name='u8ManualOP']:checked").val()+";"
				+"u32Status=1";
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_config").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_device tr").each(function(index){
		var u32Latitude = $("#t_device tr:eq("+index+") td:eq(4)").text();
		var s1 = parseInt(u32Latitude);
		var u32Latitude1 = accSub(accDiv(s1,1000),90);
		$("#t_device tr:eq("+index+") td:eq(4)").text(u32Latitude1);
		
		var u32Longitude = $("#t_device tr:eq("+index+") td:eq(3)").text();
		var s2 = parseInt(u32Longitude);
		var u32Longitude1 = accSub(accDiv(s2,1000),180);
		$("#t_device tr:eq("+index+") td:eq(3)").text(u32Longitude1);
		
		//u8DevType
		if($("#t_device tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_device tr:eq("+index+") td:eq(2)").text("标准");
		}else{
			$("#t_device tr:eq("+index+") td:eq(2)").text("级联");
		}
		//u8ManualOP
		if($("#t_device tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_device tr:eq("+index+") td:eq(5)").text("解闭塞");
		}else{
			$("#t_device tr:eq("+index+") td:eq(5)").text("闭塞");
		}	
		//u32Status
		if($("#t_device tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_device tr:eq("+index+") td:eq(6)").text("正常");
		}else{
			$("#t_device tr:eq("+index+") td:eq(6)").text("不正常");
		}				   
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5";
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
//	$("#t_device tr").each(function(index){
//		$("#t_device tr:eq("+index+") td:eq(7)").click(function(){
//			var u32DevID = $("#t_device tr:eq("+index+") td:eq(0)").text();
//			var para = "u32DevID"+"="+u32DevID;
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			var enbVersion = $("#enbVersion").val();
//			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5&parameters="+para+"";
//		});					   
//	});	 
	//跳转到配置
//	$("#t_device tr").each(function(index){
//		if(index != 0){
//			$("#t_device tr:eq("+index+") th:eq(1)").click(function(){
//				var u32DevID = $("#t_device tr:eq("+index+") td:eq(0)").text();
//				var para = "u32DevID"+"="+u32DevID;
//				var basePath = $("#basePath").val();
//				var moId = $("#moId").val();
//				var enbVersion = $("#enbVersion").val();
//				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5&parameters="+para+"";
//			});	
//		}						   
//	});	
	//删除
//	$("#t_device tr").each(function(index){
//		$("#t_device tr:eq("+index+") td:eq(9)").click(function(){
//			var u32DevID = $("#t_device tr:eq("+index+") td:eq(0)").text();
//			var para = "u32DevID"+"="+u32DevID;
//			var basePath = $("#basePath").val();
//			var moId = $("#moId").val();
//			if(confirm("确定要删除该条记录?")){
//				var enbVersion = $("#enbVersion").val();
//				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5&parameters="+para+"";
//			}
//		});					   
//	});	
	//复位
	$("#t_device tr").each(function(index){
		$("#t_device tr:eq("+index+") td:eq(7)").click(function(){
			if(confirm("确定进行站点复位?")){
				var moId = $("#moId").val();
				$.ajax({
					type:"post",
					url:"resetEnb.do",
					data:"moId="+moId+
						"&browseTime="+getBrowseTime(),
					dataType:"json",
					async:false,
					success:function(data){
						var basePath = $("#basePath").val();
						if(!sessionsCheck(data,basePath)){
							return ;
						}
						if(data.error != ""){
							alert(data.error);
						}else{
							alert("站点复位请求下发成功！");
						}			
					}
				});	
			}
		});					   
	});	
	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_device input[type=checkbox]").each(function(index){
			if($("#t_device input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_device tr:eq("+index+") td:eq(0)").text();
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
			var s = "u32DevID"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_DEVICE-2.1.5";
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
			m += s1.split(".")[1].length	
		}catch(e){}
		try{
			m += s2.split(".")[1].length	
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