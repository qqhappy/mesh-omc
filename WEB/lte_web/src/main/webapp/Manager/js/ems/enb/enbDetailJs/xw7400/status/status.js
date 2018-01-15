$(function(){
	if($("#boardFlag option").length > 0){
		$("#boardFlag option").each(function(){
			var content = $(this).html();
			var array = content.split("-");
			switch (array[0]){
			case "2" : 
				$(this).html("RRU1");
				break;
			case "3" : 
				$(this).html("RRU2");
				break;
			case "4" : 
				$(this).html("RRU3");
				break;
			}
		});
	}	
	var error=$("#error").val();
	if(error != undefined && error != "") {
		alert("查询失败! 原因：" + error);
	}
	
	$("#boardFlag").change(function(){
		queryStatus();
	});

	$("#moduleNo").change(function(){
		queryStatus();
	});
	//刷新
	$("#fresh").click(function(){
		queryStatus();
	});
	
	function queryStatus() {
		var basePath=$("#basePath").val();
		var statusFlag=$("#statusFlag").val();
		var moId=$("#moId").val();
		var enbHexId=$("#enbHexId").val();

		var href = basePath+"/lte/queryStatus.do?statusFlag="+statusFlag+"&moId="+moId+"&enbHexId="+enbHexId;
		
		if(statusFlag != "BOARD_STATUS" && statusFlag != "BBU_OPTICAL_STATUS") {
			var boardFlag=$("#boardFlag").val();
			href = href + "&boardFlag="+boardFlag;
		}
		
		if(statusFlag == "RRU_OPTICAL_STATUS") {
			var moduleNo = $("#moduleNo").val();
			href = href +"&moduleNo="+moduleNo;
		}
		if(statusFlag == "BBU_OPTICAL_STATUS") {
			var moduleNo = $("#moduleNo").val();
			href = href +"&moduleNo="+moduleNo;
		}
		window.location.href=""+href+"";
	}
});
