$(function(){
	//新增
	$("#newAdd").click(function(){
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/turnAddSingleEnb.do"+"?browseTime="+getBrowseTime();
	});
	
	//批量删除
	$("#delete").click(function(){
		var str1=[];
		var str2=[];
		$("#enbListTable input[type=checkbox]").each(function(index){
			if($("#enbListTable input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp1 = $("#enbListTable tr:eq("+index+") td:eq(1)").text();
				str1.push(temp1);
				var temp2 = $("#enbListTable tr:eq("+index+") td:eq(1) input").val();
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
			var s = str1[i]+","+str2[i]+";";
			para += s;
		}
		if(str1.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			if(confirm("确定要删除所有选择的记录?")){
				window.location.href=""+basePath+"/lte/queryDeletedEnbList.do?operType=delete&parameters="+para+"&browseTime="+getBrowseTime();
			}
		}		
	});
	//导出
	$("#export").click(function(){
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/turnExportEnbData.do"+"?browseTime="+getBrowseTime();
	});
	
	//导入
	$("#import").click(function(){
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/turnImportEnbData.do"+"?browseTime="+getBrowseTime();
	});
});
//查询是否已开站
function queryIsActive(moId){
	var isActive = "";
	$.ajax({
		type:"post",
		url:"${base}/lte/queryIsActive.do",
		data:"moId="+moId,
		dataType:"json",
		async:false,
		success:function(data){
			if(data.isActive == "true"){
				isActive = true;
			}else{
				isActive = false;
			}
		}
	});
	return isActive;
}
//查询基站版本
function queryEnbVersion(moId){
	var version = "";
	$.ajax({
		type:"post",
		url:"${base}/lte/queryEnbVersion.do",
		data:"moId="+moId,
		dataType:"text",
		async:false,
		success:function(data){
			version = data;
		}
	});
	return version;
}
//查询基站类型
function queryEnbTypeOfMoId(moId){
	var result = 0;
	$.ajax({
		type : "post",
		url : "queryEnbTypeOfMoId.do",
		data : "moId=" + moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				result = data.message;
			}
		}
	});
	return result;
}
//跳转至修改enb
function turnModifyThisEnb(enbHexId){
	var basePath = $("#basePath").val();
	var sortBy = $("#sortBy").val();
	var currentPage = $("#currentPage").html();	
	window.location.href=""+basePath+"/lte/querySingleEnb.do?enbHexId="+enbHexId+"&sortBy="+sortBy+"&currentPage="+currentPage+"&browseTime="+getBrowseTime();
}
//跳转至配置enb
function turnConfigThisEnb(enbHexId,moId){				
	var basePath = $("#basePath").val();
	var roleId = $("#roleId").val();
	var isActive = queryIsActive(moId);
	if(isActive == false){
		alert("此基站暂未开站");
	}
	var enbVersion = queryEnbVersion(moId);
	$("#mainFrame",parent.document).attr("src",""+basePath+"/lte/queryWholeStatus.do?moId="+moId+"&enbHexId="+enbHexId+"&browseTime="+getBrowseTime());	
	if(queryEnbTypeOfMoId(moId) == 0){
		$("#leftFrame",parent.document).attr("src",""+basePath+"/lte/turnEnbWebLmtLeft_xw7400.do?moId="+moId+"&enbHexId="+enbHexId+"&fromAlarm=0&roleId="+roleId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime());
	}else if(queryEnbTypeOfMoId(moId) == 200){
		$("#leftFrame",parent.document).attr("src",""+basePath+"/lte/turnEnbWebLmtLeft_xw7102.do?moId="+moId+"&enbHexId="+enbHexId+"&fromAlarm=0&roleId="+roleId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime());
	}
}
//跳转至配置enb
function turnConfigThisEnbFromAlarm(enbHexId,moId){				
	var basePath = $("#basePath").val();
	var roleId = $("#roleId").val();
	var isActive = queryIsActive(moId);
	if(isActive == false){
		alert("此基站暂未开站");
	}
	var enbVersion = queryEnbVersion(moId);
	$("#mainFrame",parent.document).attr("src",""+basePath+"/lte/turnSingleCurrentAlarm.do?enbHexId="+enbHexId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime());	
	if(queryEnbTypeOfMoId(moId) == 0){
		$("#leftFrame",parent.document).attr("src",""+basePath+"/lte/turnEnbWebLmtLeft_xw7400.do?moId="+moId+"&enbHexId="+enbHexId+"&fromAlarm=1&roleId="+roleId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime());
	}else if(queryEnbTypeOfMoId(moId) == 200){
		$("#leftFrame",parent.document).attr("src",""+basePath+"/lte/turnEnbWebLmtLeft_xw7102.do?moId="+moId+"&enbHexId="+enbHexId+"&fromAlarm=1&roleId="+roleId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime());
	}
}