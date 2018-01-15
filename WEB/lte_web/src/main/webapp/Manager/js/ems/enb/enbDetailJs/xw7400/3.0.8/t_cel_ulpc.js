$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		if(!(isNum.test($("#u8PoNominalPuschDynamic").val()) && $("#u8PoNominalPuschDynamic").val()<=24 && $("#u8PoNominalPuschDynamic").val()>=-126)){
			$("#u8PoNominalPuschDynamicError").text("/* 请输入-126~24之间的整数 */");
			index++;
		}else{
			$("#u8PoNominalPuschDynamicError").text("");
		}
		if(!(isNum.test($("#u8PoNominalPuschPersistent").val()) && $("#u8PoNominalPuschPersistent").val()<=24 && $("#u8PoNominalPuschPersistent").val()>=-126)){
			$("#u8PoNominalPuschPersistentError").text("/* 请输入-126~24之间的整数 */");
			index++;
		}else{
			$("#u8PoNominalPuschPersistentError").text("");
		}
		if(!(isNum.test($("#u8Pmax").val()) && $("#u8Pmax").val()<=33 && $("#u8Pmax").val()>=-30)){
			$("#u8PmaxError").text("/* 请输入-30~33之间的整数 */");
			index++;
		}else{
			$("#u8PmaxError").text("");
		}
		if(!(isNum.test($("#u8PoNominalPucch").val()) && $("#u8PoNominalPucch").val()<=-96 && $("#u8PoNominalPucch").val()>=-127)){
			$("#u8PoNominalPucchError").text("/* 请输入-127~-96之间的整数 */");
			index++;
		}else{
			$("#u8PoNominalPucchError").text("");
		}
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"u8PoNominalPuschDynamic"+"="+accAdd($("#u8PoNominalPuschDynamic").val(),126)+";"
				+"u8PoNominalPuschPersistent"+"="+accAdd($("#u8PoNominalPuschPersistent").val(),126)+";"
				+"u8Alpha4PL"+"="+$("#u8Alpha4PL").val()+";"
				+"u8Pmax"+"="+accAdd($("#u8Pmax").val(),30)+";"
				+"u8PoNominalPucch"+"="+accAdd($("#u8PoNominalPucch").val(),127)+";"
				+"u8dtaPoPucchF1"+"="+$("#u8dtaPoPucchF1").val()+";"
				+"u8dtaPoPucchF1b"+"="+$("#u8dtaPoPucchF1b").val()+";"
				+"u8dtaPoPucchF2"+"="+$("#u8dtaPoPucchF2").val()+";"
				+"u8dtaPoPucchF2a"+"="+$("#u8dtaPoPucchF2a").val()+";"
				+"u8dtaPoPucchF2b"+"="+$("#u8dtaPoPucchF2b").val()+";"
				+"u8dtaPrmbMsg3"+"="+$("#u8dtaPrmbMsg3").val()+";"
				+"u8SwchPHR"+"="+$("input[name='u8SwchPHR']:checked").val()+";"
				+"u8PCFilterCoeffRSRP"+"="+$("#u8PCFilterCoeffRSRP").val()+";"
				+"u8PSrsOffset"+"="+$("#u8PSrsOffset").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_cel_ulpc1 tr").each(function(index){
		var u8PoNominalPuschDynamic = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(1)").text();
		var u8PoNominalPuschDynamic1 = parseInt(u8PoNominalPuschDynamic);
		var u8PoNominalPuschDynamic2 = u8PoNominalPuschDynamic1-126;
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(1)").text(u8PoNominalPuschDynamic2);
		//
		var u8PoNominalPuschPersistent = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(2)").text();
		var u8PoNominalPuschPersistent1 = parseInt(u8PoNominalPuschPersistent);
		var u8PoNominalPuschPersistent2 = u8PoNominalPuschPersistent1-126;
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(2)").text(u8PoNominalPuschPersistent2);
		//
		var u8Pmax = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(4)").text();
		var u8Pmax1 = parseInt(u8Pmax);
		var u8Pmax2 = u8Pmax1-30;
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(4)").text(u8Pmax2);
		//
		var u8PoNominalPucch = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(5)").text();
		var u8PoNominalPucch1 = parseInt(u8PoNominalPucch);
		var u8PoNominalPucch2 = u8PoNominalPucch1-127;
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(5)").text(u8PoNominalPucch2);
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.4");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.5");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.6");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.7");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.8");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("0.9");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(3)").text("1");
		}
	
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(6)").text("-2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(6)").text("0");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(6)").text("2");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(7)").text("1");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(7)").text("3");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(7)").text("5");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text("-2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text("0");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text() == 2){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text("1");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(8)").text("2");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(9)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(9)").text("-2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(9)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(9)").text("0");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(9)").text("2");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(10)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(10)").text("-2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(10)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(10)").text("0");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(10)").text("2");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("-1");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("0");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 2){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("1");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 3){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 4){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("3");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 5){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("4");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text() == 6){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("5");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(11)").text("6");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(12)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(12)").text("关");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(12)").text("开");
		}
		//
		if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 0){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("0");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 1){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("1");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 2){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("2");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 3){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("3");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 4){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("4");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 5){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("5");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 6){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("6");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 7){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("7");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 8){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("8");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 9){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("9");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 10){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("11");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 11){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("13");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 12){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("15");
		}else if($("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text() == 13){
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("17");
		}else{
			$("#t_cel_ulpc1 tr:eq("+index+") td:eq(13)").text("19");
		}	
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8";
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
	$("#t_cel_ulpc1 tr").each(function(index){
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(15)").click(function(){
			var u8CId = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_ulpc1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_ulpc1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8&parameters="+para+"";
			});		
		}
					   
	});	
	//删除
	$("#t_cel_ulpc1 tr").each(function(index){
		$("#t_cel_ulpc1 tr:eq("+index+") td:eq(16)").click(function(){
			var u8CId = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_ulpc1 input[type=checkbox]").each(function(index){
			if($("#t_cel_ulpc1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_ulpc1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_ULPC-3.0.8";
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
});