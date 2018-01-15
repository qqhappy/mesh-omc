$(function(){  
	//控制eNB的C类参数显�?
	document.onkeydown = function(e){
		var e = e||event;
		var keyCode = e.keyCode||e.which||e.charCode;
		if(keyCode == 192  && e.ctrlKey && e.shiftKey && e.altKey){
			var cFlag = $("#topFrame",parent.document).attr("c_para_ctrl");
			cFlag = cFlag * (-1);
			$("#topFrame",parent.document).attr("c_para_ctrl",cFlag);
			window.parent.mainFrame.location.href = "${base}/lte/queryEnbList.do?operType=select";	
			window.parent.leftFrame.location.href = "${base}/lte/turnConfigManageLeft.do";	
		}
	}		   	
	// 窗口高度
	var winhei=$(window).height();
	// 窗口宽度
	var winWidth=$(window).width();
	//
	var leftWidth = $(".left_nav").width();
	// 蓝条高度
 	var pathHei=$(".path_nav").height();
	// 左部菜单栏高�?
	$("#leftDiv").css({"height":winhei-pathHei-31,"overflow":"auto"});
	$("#treeDemo").css("height",winhei-pathHei-41);
	$("#treeDemo2").css("height",winhei-pathHei-282);
	$(".left_nav").css("height",winhei-pathHei);
	// welcome页高�?
	$(".pageContent").css("height",winhei-pathHei-40);
	// 右部数据高度
	$(".enbPage").css({"height":winhei-pathHei-10,"overflow":"auto"});
	$(".ltePage").css({"height":winhei-pathHei-10,"overflow":"auto"});
	$(".alarmPage").css({"height":winhei-pathHei-40,"overflow":"auto"});
	// 新增配置页面高度
	$(".add_div").css("height",winhei-pathHei);	
	//基站列表右部数据宽度
	if($(".contentDivObj").width() < 1190){
		$(".contentDivObj").css({"width":1190});
	}
	if($(".asset_div").width() < 1190){
		$(".asset_div").css({"width":1190});
	}
	if($(".historyAsset_div").width() < 1600){
		$(".historyAsset_div").css({"width":1600});
	}
	// 新增配置页面宽度�?
	if($(".ad_Input").width() < 1140){
		$(".ad_Input").css({"width":1140,"overflow":"hidden"});	
	}
	if($(".bb_condition_ul").width() < 1140){
		$(".bb_condition_ul").css({"width":1140,"overflow":"hidden"});	
	}
	if($(".bb_content_div").width() < 1140){
		$(".bb_content_div").css({"width":1140,"overflow":"hidden"});	
	}
	
	
	window.onresize = function(){
		// 窗口高度
		var winhei=$(window).height();
		// 窗口宽度
		var winWidth=$(window).width();
		//
		var leftWidth = $(".left_nav").width();
		// 蓝条高度
	 	var pathHei=$(".path_nav").height();
		// 左部菜单栏高�?
		$("#leftDiv").css({"height":winhei-pathHei-31,"overflow":"auto"});
		$("#treeDemo").css("height",winhei-pathHei-41);
		$("#treeDemo2").css("height",winhei-pathHei-282);
		$(".left_nav").css("height",winhei-pathHei);
		// welcome页高�?
		$(".pageContent").css("height",winhei-pathHei-40);
		// 右部数据高度
		$(".enbPage").css({"height":winhei-pathHei-10,"overflow":"auto"});
		$(".ltePage").css({"height":winhei-pathHei-10,"overflow":"auto"});
		$(".alarmPage").css({"height":winhei-pathHei-40,"overflow":"auto"});
		// 新增配置页面高度
		$(".add_div").css("height",winhei-pathHei);
		//右部数据宽度
		if($(".contentDivObj").width() < 1190){
			$(".contentDivObj").css({"width":1190,"overflow":"hidden"});
		}
		if($(".enbPage").width() > 1190){
			$(".contentDivObj").css({"width":"100%","overflow":"hidden"});
		}
		if($(".asset_div").width() < 1190){
			$(".asset_div").css({"width":1190});
		}
		if($(".enbPage").width() > 1190){
			$(".asset_div").css({"width":"100%","overflow":"hidden"});
		}
		if($(".historyAsset_div").width() < 1600){
			$(".historyAsset_div").css({"width":1600});
		}
		if($(".enbPage").width() > 1600){
			$(".asset_div").css({"width":"100%","overflow":"hidden"});
		}
		// 新增配置页面宽度�?
		if($(".ad_Input").width() < 1140){
			$(".ad_Input").css({"width":1140,"overflow":"hidden"});	
		}
		if($(".bb_condition_ul").width() < 1140){
			$(".bb_condition_ul").css({"width":1140,"overflow":"hidden"});	
		}
		if($(".bb_content_div").width() < 1140){
			$(".bb_content_div").css({"width":1140,"overflow":"hidden"});	
		}
		if($("#form_add").width() > 1235){
			$(".ad_Input").css("width",$("#form_add").width() - 95);	
		}
	}
	
	
	// 隔行变色
//	$(".changeRowColor tr").each(function(index){
//		if(index > 0 && index%2 == 0){
//			$(".changeRowColor tr:eq("+index+") td").css("background-color","#f6f6f6");
//		}
//	});	
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
});
function checkLevel(){
	// 需要显示的字段
	var fieldLevelString = $("#fieldLevelString").val();
	fieldLevelString = getVersionMatchFields(fieldLevelString);
	fieldLevelString.push("checkWidth");
	fieldLevelString.push("justForView");
	$(".tableHeight table tr th").each(function(){
		var fieldName = $(this).attr("class");
		// 如果不显示该字段，则将该字段隐藏
		if(!showField(fieldName, fieldLevelString)) {
			$(this).css("display","none");
		}
	});
	$(".tableHeight table tr td").each(function(){
		var fieldName = $(this).attr("class");
		if(!showField(fieldName, fieldLevelString)) {
			$(this).css("display","none");
		}
	});	
}

function showField(fieldName, fieldNames) {
	var length = fieldNames.length;
	for(var i = 0;i < length ; i++){
		if(fieldNames[i] == fieldName){
			return true;
		}
	}
	return false;
}

function getVersionMatchFields(fieldLevelString){	
	fieldLevelString = $.parseJSON(fieldLevelString);
	var cFlag = $("#topFrame",parent.document).attr("c_para_ctrl");
	var resultNames = new Array();
	//全部字段
	var allNames = new Array();
	//除了C的全部字�?
	var abNames = new Array();
	for(var key in fieldLevelString){
		if(key != "C"){
			var list = fieldLevelString[key];
			for(var i = 0;i<list.length;i++){
				abNames.push(list[i]);
				allNames.push(list[i]);
			}
		}else{
			var list = fieldLevelString[key];
			for(var i = 0;i<list.length;i++){
				allNames.push(list[i]);
			}
		}		
	}
	if(parseInt(cFlag) < 0){
		resultNames = abNames;
	}else{
		resultNames = allNames;
	}
	return resultNames;
}

function checkLevelLen(){
	var fieldLevelString = $("#fieldLevelString").val();
	fieldLevelString = getVersionMatchFields(fieldLevelString);
	fieldLevelString.push("checkWidth");
	fieldLevelString.push("justForView");
	// 需要剪掉的长度
	var subLength = 0;
	$(".dynamicLengthDiv table tr:eq(0) th").each(function(index){
		var fieldName = $(this).attr("class");
		if(!showField(fieldName, fieldLevelString)) {
			var thLength = $(this).width();
			subLength = subLength + thLength;
		}
	});
	var dynamicLength = $(".dynamicLengthDiv").width();
	var resultLength = dynamicLength - subLength;
	var pageLength = $(".ltePage").width();
	// 计算需要显示的长度
	if(resultLength < pageLength){
		resultLength = pageLength;
		$(".dynamicLengthDiv").css("padding-right",0);
	}
	$(".dynamicLengthDiv").css("width",resultLength);
		
	$(".dynamicLengthDiv table tr th").each(function(){
		var fieldName = $(this).attr("class");
		if(!showField(fieldName, fieldLevelString)) {
			$(this).css("display","none");
		}
	});
	$(".dynamicLengthDiv table tr td").each(function(){
		var fieldName = $(this).attr("class");
		if(!showField(fieldName, fieldLevelString)) {
			$(this).css("display","none");
		}
	});
}

function sessionsCheck(sessionsVal,basePath){
	var loginFlag = sessionsVal.hasOwnProperty("loginFlag");

	if(loginFlag == true && sessionsVal.loginFlag == "false"){
		this.top.location.href=basePath+"/login/logout.do"; 
		return false;
	}

	return true;
}

// 验证IP合法�?
function checkIP_style(ipString){
	var ipArray = ipString.split(".");
	var first = ipArray[0];
	var last = ipArray[3];
	if(first == 0 || last == 255){
		return false;
	}
	return true;
}

// ip地址输入�?
function Address(divid){
	var perinput = divid+" input";
	$(perinput).each(function(index){
		$(this).keyup(function(e){
			e=e||window.event;
			
			var num = $(this).val().replace(/[^\d]/g,'');
	
			if(!((e.keyCode>=96&&e.keyCode<=105)||(e.keyCode>=48&&e.keyCode<=57)||e.keyCode==8||e.keyCode==9 ||e.keyCode==39||e.keyCode==37)){
				$(this).val(num);
				$(this).focus();
				return false;
			}
			if(parseInt(num)>=256 || parseInt(num)<0){
				alert(parseInt($(this).val())+"不是有效值，请输入一个介0~255之间的数值！");
				$(this).val("");
				$(this).focus();
				return false;
			}
			
			var myval = $(this).val().length;
			if(myval==3 && e.keyCode !=9 ||e.keyCode==39){
				$(perinput).eq(index+1).focus();
				return false;
			}
			if(myval==0 && e.keyCode !=9 ||e.keyCode==37){
				if(index!=0){
					$(perinput).eq(index-1).select();
					return false;
				}
			}
		 });  
	}); 
}
//返回当前时间
function getBrowseTime(){
	var date = new Date();	
	var  s  =date.getTime();
	return s;
}
//根据字段名和字段值选中单选框
function checkRadio(name,value){
	$("input[name='"+name+"']").each(function(){
		var thisValue = $(this).val();
		if(thisValue == value || parseInt(thisValue) == parseInt(value)){
			$(this).attr("checked","checked");
		}
	});
}
//根据正则表达式，id，最大值，最小值校验简单的闭区间input提交
function checkInputInForm(regex,id,max,min){
	if(!(regex.test($("#"+id).val()) && $("#"+id).val()<=max  && $("#"+id).val()>=min)){
		return false;
	}else{
		return true;
	}
}
//同步查询更新�?,argment参数是预留�?
function refresh_sync(enbVersion,basePath,moId,tableName,argment){
	if(argment == "" || argment == null || typeof(argment) == "undefined"){	
		if(queryEnbTypeOfMoId(moId) == 0){
			if(enbVersion>="3.0.8"){
				window.location.href = basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
			}else{
				window.location.href = basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
			}
		}else if(queryEnbTypeOfMoId(moId) == 200){
			window.location.href = basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
		}
		
	}	
} 
//同步删除更新�?   单主键表 ,argment参数是预留�?
function delete_sync(obj,pk,enbVersion,basePath,moId,tableName,argment){
	var obj_dom = $(obj);
	var pk_value = obj_dom.parents("tr:first").find("."+pk).text();
	var para = pk+"="+pk_value;
	if(tableName == "T_VLAN"){
		if(confirm(constantInfoMap["vlan_confirm"])){
			if(argment == "" || argment == null || typeof(argment) == "undefined"){	
				if(queryEnbTypeOfMoId(moId) == 0){
					if(enbVersion>="3.0.8"){
						window.location.href=basePath+"/lte/queryEnbBiz_xw7400.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}else{
						window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}
				}else if(queryEnbTypeOfMoId(moId) == 200){
					window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
				}
				
			}
		}
	}else{
		if(confirm(constantInfoMap["confirm_delete_info"])){
			if(argment == "" || argment == null || typeof(argment) == "undefined"){	
				if(queryEnbTypeOfMoId(moId) == 0){
					if(enbVersion>="3.0.8"){
						window.location.href=basePath+"/lte/queryEnbBiz_xw7400.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}else{
						window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}
				}else if(queryEnbTypeOfMoId(moId) == 200){
					window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=delete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
				}
				
			}
		}
	}			
} 
//同步批量删除更新�?   单主键表 ,argment参数是预留�?
function multiDelete_sync(tableId,pk,enbVersion,basePath,moId,tableName,argment){
	var pkArray = [];
	$("#"+tableId+" input[name='checkson']").each(function(){
		if($(this).attr("checked")){
			var pk_value = $(this).parents("tr:first").find("."+pk).text();
			pkArray.push(pk_value);
		}
	});	
	var para = "";
	for(var i=0;i<pkArray.length;i++){
		var s = pk+"="+pkArray[i]+";";
		para += s;
	}
	if(pkArray.length < 1){
		alert(constantInfoMap["no_selected_item_info"]);
	}else{
		if(tableName == "T_VLAN"){
			if(confirm(constantInfoMap["vlan_confirm"])){
				if(argment == "" || argment == null || typeof(argment) == "undefined"){	
					if(queryEnbTypeOfMoId(moId) == 0){
						if(enbVersion>="3.0.8"){
							window.location.href=basePath+"/lte/queryEnbBiz_xw7400.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
						}else{
							window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
						}
					}else if(queryEnbTypeOfMoId(moId) == 200){
						window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}
					
				}
			}
		}else{
			if(confirm(constantInfoMap["confirm_deleteAll_info"])){
				if(argment == "" || argment == null || typeof(argment) == "undefined"){	
					if(queryEnbTypeOfMoId(moId) == 0){
						if(enbVersion>="3.0.8"){
							window.location.href=basePath+"/lte/queryEnbBiz_xw7400.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
						}else{
							window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
						}
					}else if(queryEnbTypeOfMoId(moId) == 200){
						window.location.href=basePath+"/lte/queryEnbBiz.do?moId="+moId+"&operType=multiDelete&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&parameters="+para;
					}
					
				}
			}
		}
		
	}			
} 
//跳转到修改页�?   单主键表 ,argment参数是预留�?
function turnToModifyData_bySinglePK(obj,pk,enbVersion,basePath,moId,tableName,referTable,argment){
	var obj_dom = $(obj);
	var pk_value = obj_dom.parents("tr:first").find("."+pk).text();
	var para = pk+"="+pk_value;
	if(argment == "" || argment == null || typeof(argment) == "undefined"){	
		if(queryEnbTypeOfMoId(moId) == 0){
			if(enbVersion>="3.0.8"){
				window.location.href=basePath+"/lte/turnConfigEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable+"&parameters="+para;
			}else{
				window.location.href=basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable+"&parameters="+para;
			}
		}else if(queryEnbTypeOfMoId(moId) == 200){
			window.location.href=basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable+"&parameters="+para;
		}		
	}	
}
//跳转到新增页�?   argment参数是预留�?
function turnToAddData(enbVersion,basePath,moId,tableName,referTable,argment){
	if(argment == "" || argment == null || typeof(argment) == "undefined"){	
		if(queryEnbTypeOfMoId(moId) == 0){
			if(enbVersion>="3.0.8"){
				window.location.href=basePath+"/lte/turnAddEnbBiz_xw7400.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable;
			}else{
				window.location.href=basePath+"/lte/turnAddEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable;
			}
		}else if(queryEnbTypeOfMoId(moId) == 200){
			window.location.href=basePath+"/lte/turnAddEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+"&browseTime="+getBrowseTime()+"&tableName="+tableName+"&referTable="+referTable;
		}
		
	}	
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
//表单提交需要的ajax业务校验
function biz_table_ajaxCheck(enbVersion,basePath,moId,tableName,operType,para,argment){
	$.ajax({
		type:"post",
		url:"queryAsyncEnbBiz.do",
		data:"moId="+moId+
			"&basePath="+basePath+
			"&browseTime="+getBrowseTime()+
			"&tableName="+tableName+
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
				if(argment == "" || argment == null || typeof(argment) == "undefined"){	
					if(queryEnbTypeOfMoId(moId) == 0){
						if(enbVersion>="3.0.8"){
							window.location.href=basePath+"/lte/queryEnbBiz_xw7400.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
						}else{
							window.location.href=basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
						}
					}else if(queryEnbTypeOfMoId(moId) == 200){
						window.location.href=basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName="+tableName;
					}
					
				}			
			}				
		}
	});
}
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




