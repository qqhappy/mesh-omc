$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8CellId").val()) && $("#u8CellId").val()<=254  && $("#u8CellId").val()>=0)){
			$("#u8CellIdError").text("/* 请输入0~254之间的整数 */");
			index++;
		}else{
			$("#u8CellIdError").text("");
		}
		if(!(isNum.test($("#u8ShortDrxCycleTimer").val()) && $("#u8ShortDrxCycleTimer").val()<=16  && $("#u8ShortDrxCycleTimer").val()>=1)){
			$("#u8ShortDrxCycleTimerError").text("/* 请输入1~16之间的整数 */");
			index++;
		}else{
			$("#u8ShortDrxCycleTimerError").text("");
		}
		var para = "u8CellId"+"="+$("#u8CellId").val()+";"
				+"u8DrxEnable"+"="+$("input[name='u8DrxEnable']:checked").val()+";"
				+"u8OnDurationTimer"+"="+$("#u8OnDurationTimer").val()+";"
				+"u8DrxInactivityTimer"+"="+$("#u8DrxInactivityTimer").val()+";"
				+"u8DrxRetransmissionTimer"+"="+$("#u8DrxRetransmissionTimer").val()+";"
				+"u8LongDrxCycle"+"="+$("#u8LongDrxCycle").val()+";"
				+"u8ShortDrxCycle"+"="+$("#u8ShortDrxCycle").val()+";"
				+"u8ShortDrxEnable"+"="+$("input[name='u8ShortDrxEnable']:checked").val()+";"
				+"u8ShortDrxCycleTimer"+"="+$("#u8ShortDrxCycleTimer").val();
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
						window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5";
					}				
				}
			});	
		}
		
	});
	//内存值转为显示值
	$("#t_cel_drx tr").each(function(index){
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(1)").text("关");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(1)").text("开");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf1");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf2");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf3");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf4");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf5");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf6");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 6){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf8");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 7){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf10");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 8){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf20");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 9){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf30");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 10){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf40");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 11){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf50");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 12){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf60");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 13){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf80");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(2)").text() == 14){
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf100");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(2)").text("psf200");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf1");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf2");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf3");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf4");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf5");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf6");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf8");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 7){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf10");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 8){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf20");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 9){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf30");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 10){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf40");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 11){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf50");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 12){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf60");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 13){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf80");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 14){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf100");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 15){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf200");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 16){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf300");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 17){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf500");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 18){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf750");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 19){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf1280");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(3)").text() == 20){
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf1920");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(3)").text("psf2560");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf1");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf2");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf4");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf6");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf8");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 5){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf16");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(4)").text() == 6){
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf24");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(4)").text("psf33");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf10");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf20");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 2){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf32");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 3){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf40");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 4){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf64");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 5){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf80");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 6){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf128");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 7){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf160");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 8){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf256");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 9){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf320");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 10){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf512");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 11){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf640");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 12){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf1024");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 13){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf1280");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(5)").text() == 14){
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf2048");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(5)").text("sf2560");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(6)").text("关");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(6)").text("开");
		}
		//
		if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf2");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf5");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf8");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf10");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf16");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf20");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 6){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf32");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 7){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf40");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 8){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf64");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 9){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf80");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 10){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf128");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 11){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf160");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 12){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf256");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 13){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf320");
		}else if($("#t_cel_drx tr:eq("+index+") td:eq(7)").text() == 14){
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf512");
		}else{
			$("#t_cel_drx tr:eq("+index+") td:eq(7)").text("sf640");
		}
	});
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5";
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
	$("#t_cel_drx tr").each(function(index){
		$("#t_cel_drx tr:eq("+index+") td:eq(9)").click(function(){
			var u8CellId = $("#t_cel_drx tr:eq("+index+") td:eq(0)").text();
			var para = "u8CellId"+"="+u8CellId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_drx tr").each(function(index){
		if(index != 0){
			$("#t_cel_drx tr:eq("+index+") th:eq(1)").click(function(){
				var u8CellId = $("#t_cel_drx tr:eq("+index+") td:eq(0)").text();
				var para = "u8CellId"+"="+u8CellId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5&parameters="+para+"";
			});	
		}
					   
	});	
	//删除
	$("#t_cel_drx tr").each(function(index){
		$("#t_cel_drx tr:eq("+index+") td:eq(10)").click(function(){
			var u8CellId = $("#t_cel_drx tr:eq("+index+") td:eq(0)").text();
			var para = "u8CellId"+"="+u8CellId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_drx input[type=checkbox]").each(function(index){
			if($("#t_cel_drx input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_drx tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8CellId"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_DRX-1.1.5";
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