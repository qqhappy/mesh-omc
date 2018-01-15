$(function(){
		   
	//表单校验
	var isNum=/^\d+$/;
	var isMap2=/^[0-1]{2}$/;
	var isMap4=/^[0-1]{4}$/;
	$("#submit_add").click(function(){
		//							
		var au8OpLp2AntCdBk1 = $("#au8OpLp2AntCdBk1").val();
		var au8OpLp2AntCdBk2 = $("#au8OpLp2AntCdBk2").val();
		var s1 = parseInt(au8OpLp2AntCdBk1).toString(16);
		if(s1.length<2){
			s1 = "0" + s1;	
		}	
		var s2 = parseInt(au8OpLp2AntCdBk2).toString(16);
		if(s2.length<2){
			s2 = "0" + s2;	
		}
		$("#au8OpLp2AntCdBk").val(s1+s2);
		//
		var au8OpLp4AntCdBk1 = $("#au8OpLp4AntCdBk1").val();
		var au8OpLp4AntCdBk2 = $("#au8OpLp4AntCdBk2").val();
		var au8OpLp4AntCdBk3 = $("#au8OpLp4AntCdBk3").val();
		var au8OpLp4AntCdBk4 = $("#au8OpLp4AntCdBk4").val();
		var str1 = parseInt(au8OpLp4AntCdBk1).toString(16);
		if(str1.length<2){
			str1 = "0" + str1;	
		}
		var str2 = parseInt(au8OpLp4AntCdBk2).toString(16);
		if(str2.length<2){
			str2 = "0" + str2;	
		}	
		var str3 = parseInt(au8OpLp4AntCdBk3).toString(16);
		if(str3.length<2){
			str3 = "0" + str3;	
		}	
		var str4 = parseInt(au8OpLp4AntCdBk4).toString(16);
		if(str4.length<2){
			str4 = "0" + str4;	
		}	
		$("#au8OpLp4AntCdBk").val(str1+str2+str3+str4);
		//
		var index = 0;
		if($("#u8CId option").length<1){
			$("#u8CIdError").text("/* 没有可用的小区标识选项 */");
			index++;
		}else{
			$("#u8CIdError").text("");
		}
		
		var para = "u8CId"+"="+$("#u8CId").val()+";"
				+"au8OpLp2AntCdBk"+"="+$("#au8OpLp2AntCdBk").val()+";"
				+"au8OpLp4AntCdBk"+"="+$("#au8OpLp4AntCdBk").val()+";"
				+"u8PhichDuration"+"="+$("input[name='u8PhichDuration']:checked").val()+";"
				+"u8Ng"+"="+$("#u8Ng").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_cel_pdch1 tr").each(function(index){
		//u8PhichDuration
		if($("#t_cel_pdch1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(3)").text("正常");
		}else{
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(3)").text("延展");
		}
		//u8Ng
		if($("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text("1/6");
		}	
		if($("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text("1/2");
		}
		if($("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text("1");
		}
		if($("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_cel_pdch1 tr:eq("+index+") td:eq(4)").text("2");
		}
		//
		var au8OpLp2AntCdBk = $("#t_cel_pdch1 tr:eq("+index+") td:eq(1)").text().split("");	
		var str1 = au8OpLp2AntCdBk[0] + au8OpLp2AntCdBk[1] ;
		var str2 = au8OpLp2AntCdBk[2] + au8OpLp2AntCdBk[3] ;
		var no1 = parseInt(str1,16).toString(10);
		var no2 = parseInt(str2,16).toString(10); 
		var result1 = no1 + " , " + no2;		
		$("#t_cel_pdch1 tr:eq("+index+") td:eq(1)").text(result1);	
		//
		var au8OpLp4AntCdBk = $("#t_cel_pdch1 tr:eq("+index+") td:eq(2)").text().split("");	
		var strq1 = au8OpLp4AntCdBk[0] + au8OpLp4AntCdBk[1] ;
		var strq2 = au8OpLp4AntCdBk[2] + au8OpLp4AntCdBk[3] ;
		var strq3 = au8OpLp4AntCdBk[4] + au8OpLp4AntCdBk[5] ;
		var strq4 = au8OpLp4AntCdBk[6] + au8OpLp4AntCdBk[7] ;
		var noq1 = parseInt(strq1,16).toString(10);
		var noq2 = parseInt(strq2,16).toString(10);
		var noq3 = parseInt(strq3,16).toString(10);
		var noq4 = parseInt(strq4,16).toString(10);
		var result2 = noq1 + " , " + noq2 + " , " + noq3 + " , " + noq4;		
		$("#t_cel_pdch1 tr:eq("+index+") td:eq(2)").text(result2);	
		
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7";
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
	$("#t_cel_pdch1 tr").each(function(index){
		$("#t_cel_pdch1 tr:eq("+index+") td:eq(5)").click(function(){
			var u8CId = $("#t_cel_pdch1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_cel_pdch1 tr").each(function(index){
		if(index != 0){
			$("#t_cel_pdch1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CId = $("#t_cel_pdch1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CId"+"="+u8CId;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7&parameters="+para+"";
			});	
		}
						   
	});
	//删除
	$("#t_cel_pdch1 tr").each(function(index){
		$("#t_cel_pdch1 tr:eq("+index+") td:eq(6)").click(function(){
			var u8CId = $("#t_cel_pdch1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CId"+"="+u8CId;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_cel_pdch1 input[type=checkbox]").each(function(index){
			if($("#t_cel_pdch1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_cel_pdch1 tr:eq("+index+") td:eq(0)").text();
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
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_CEL_PDCH-3.0.7";
	});
});