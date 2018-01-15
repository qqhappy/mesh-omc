$(function(){
	$("#dataExport").click(function(){
		//起始时间
		var beginTime = $("input[name='beginTime']").val();
		//结束时间
		var endTime = $("input[name='endTime']").val();
		
		var str1 = beginTime.split("");
		var str2 = endTime.split("");
		var str3 = "";
		var str4 = "";
		var isNum = /^[0-9]+$/;
		for(var i = 0;i<str1.length;i++){
			if(isNum.test(str1[i])){
				str3+=(str1[i]);
			}
		}
		for(var j = 0;j<str2.length;j++){
			if(isNum.test(str2[j])){
				str4+=(str2[j]);
			}
		}
		if(parseInt($.trim(str4)) - parseInt($.trim(str3)) < 0){
			alert("开始时间不可以大于结束时间");
			return;
		}
		
		var str1=[];
		$("#itemTable input[type=checkbox]").each(function(index){
			if($("#itemTable input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#itemTable tr:eq("+index+") td:eq(0) input[type=hidden]").val();
				str1.push(temp);
			}
		});	
		if(str1.length < 1){
			alert("您并未选中任何统计项");
			return;
		}
		var str2=[];
		$("#entityTable input[type=checkbox]").each(function(index){
			if($("#entityTable input[type=checkbox]:eq("+index+")").attr("checked")){
				var entity = $("#entityTable tr:eq("+index+") td:eq(0) input[type=hidden]").val();
				str2.push(entity);
			}
		});	
		if(str2.length < 1){
			alert("您并未选中任何设备");
			return;
		}
		$("#itemParameters").val(str1);
		$("#objectParameters").val(str2);
		
		
		var interval = 15;
		var intervalHtml = $("#interval").val();
		if(intervalHtml == 2){
			interval = 60;
		}
		if(intervalHtml == 3){
			interval = 24 * 60;
		}
		var itemTableLength = $("#itemTable tr td input[type=checkbox]:checked").length;
		var entityTableLength = $("#entityTable tr td input[type=checkbox]:checked").length;
		
		if(getTimeSub(beginTime,endTime,"minute")/interval < 1){
			alert("所选时间间隔小于统计间隔,请扩大时间范围");
			return ;
		}
		if($("#entityType").val() == "ENB.CELL"){
			var totalNum = parseInt(getTimeSub(beginTime,endTime,"minute")/interval)  *  itemTableLength * entityTableLength *3 ;
			if(totalNum > 10000){					
				if(confirm("按每个基站满小区计算,大约有"+totalNum+"条数据,导出耗时较大,是否继续?")){
					$("input[name='browseTime']").val(getBrowseTime());
					$("#dataForm").submit();
				}
			}else{
				$("input[name='browseTime']").val(getBrowseTime());
				$("#dataForm").submit();
			}
		}else{
			var totalNum = parseInt(getTimeSub(beginTime,endTime,"minute")/interval)  *  itemTableLength * entityTableLength ;
			if(totalNum > 10000){					
				if(confirm("大约有"+totalNum+"条数据,导出耗时较大,是否继续?")){
					$("input[name='browseTime']").val(getBrowseTime());
					$("#dataForm").submit();
				}
			}else{
				$("input[name='browseTime']").val(getBrowseTime());
				$("#dataForm").submit();
			}
		}
		
	});
});


function getTimeSub(start,end,type){
	var st = start.replace(/\-/g,"/");
	var et = end.replace(/\-/g,"/");
	var startTime = new Date(st);
	var endTime = new Date(et);
	var divNum = 1;
	switch (type){
		case "second":
			divNum = 1000;
			break;
		case "minute":
			divNum = 1000*60;
			break;
		case "hour":
			divNum = 1000*60*60;
			break;
		case "day":
			divNum = 1000*60*60*24;
			break;
		default :
			break;
	}
	return (     parseInt((endTime.getTime()  -  startTime.getTime())/parseInt(divNum))      );
}

function getIntervalTimesByType(interval){
	if(interval == 1){
		return 15*60*1000;
	}else if(interval == 2){
		return 60*60*1000;
	}else{
		return 24*60*60*1000;
	}
}

