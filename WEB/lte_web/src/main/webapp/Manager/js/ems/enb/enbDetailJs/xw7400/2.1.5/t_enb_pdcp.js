$(function(){
	//表单校验
	var isNum=/^\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8CfgIdx").val()) && $("#u8CfgIdx").val()<=255  && $("#u8CfgIdx").val()>=0)){
			$("#u8CfgIdxError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8DiscardTimer"+"="+$("#u8DiscardTimer").val()+";"
				+"u8PdcpStatRptInd"+"="+$("input[name='u8PdcpStatRptInd']:checked").val()+";"
				+"u8SNType"+"="+$("input[name='u8SNType']:checked").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_pdcp tr").each(function(index){
		//
		if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("50");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 1){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("100");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 2){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("150");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 3){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("300");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 4){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("500");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 5){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("750");
		}else if($("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text() == 6){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("1500");
		}else{
			$("#t_enb_pdcp tr:eq("+index+") td:eq(1)").text("无穷大");
		}	
		//
		if($("#t_enb_pdcp tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(2)").text("否");
		}else{
			$("#t_enb_pdcp tr:eq("+index+") td:eq(2)").text("是");
		}
		//
		if($("#t_enb_pdcp tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_enb_pdcp tr:eq("+index+") td:eq(3)").text("7");
		}else{
			$("#t_enb_pdcp tr:eq("+index+") td:eq(3)").text("12");
		}	
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5";
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
	$("#t_enb_pdcp tr").each(function(index){
		$("#t_enb_pdcp tr:eq("+index+") td:eq(4)").click(function(){
			var u8CfgIdx = $("#t_enb_pdcp tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_pdcp tr").each(function(index){
		if(index != 0){
			$("#t_enb_pdcp tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_pdcp tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_pdcp tr").each(function(index){
		$("#t_enb_pdcp tr:eq("+index+") td:eq(5)").click(function(){
			var u8CfgIdx = $("#t_enb_pdcp tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_pdcp input[type=checkbox]").each(function(index){
			if($("#t_enb_pdcp input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_pdcp tr:eq("+index+") td:eq(0)").text();
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
			var s = "u8CfgIdx"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5&parameters="+para+"";
			}
		}
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_PDCP-2.1.5";
	});
});