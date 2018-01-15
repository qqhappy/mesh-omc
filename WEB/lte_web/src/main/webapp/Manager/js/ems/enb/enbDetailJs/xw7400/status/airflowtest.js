$(function(){
	
	
	//刷新
	$("#fresh").click(function(){
		var basePath=$("#basePath").val();
		var statusFlag=$("#statusFlag").val();
		var moId=$("#moId").val();
		var enbHexId=$("#enbHexId").val();

		var href = basePath+"/lte/queryStatus" +
				".do?statusFlag="+statusFlag+"&moId="+moId+"&enbHexId="+enbHexId;
		window.location.href=""+href+"";
	});
	Address("#ip_addrDiv");
	
});

function beginAirFlowTest() {
	
	var ip_addrStr="";
	$("#ip_addrDiv input").each(function(){
		ip_addrStr+=$(this).val()+".";
	});
	var ip_addr = ip_addrStr.substring(0,ip_addrStr.length-1);
	var moId = $("#moId").val();
	var enbHexId = $("#enbHexId").val();
	var ipTest = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if(!ipTest.test(ip_addr)){
		alert("地址格式错误");
		return false;
	}
	
	if(!checkIP_style(ip_addr)){
		alert("地址不合法");
		return false;
	}

		$.ajax({
			type : "post",
			url : "configStatus.do",
			data : "airFlowTest.ipAddress=" + ip_addr + "&statusFlag="
					+ "AIR_FLOW_BEGIN" + "&moId="+moId+"&enbHexId="+enbHexId,
			dataType : "json",
			async : false,
			success : function(data) {
				var status = data.status;
				if (status == 0) {
					var result = data.model;
					var errMes = data.model.errorStr;
					if (result.beginResult == 0) {
						alert("成功");
						//灌包成功后刷新
						var basePath=$("#basePath").val();
						var statusFlag=$("#statusFlag").val();
						var moId=$("#moId").val();
						var enbHexId=$("#enbHexId").val();

						var href = basePath+"/lte/queryStatus.do?statusFlag="+statusFlag+"&moId="+moId+"&enbHexId="+enbHexId;
						window.location.href=""+href+"";
						
					} else {
						alert("失败："+errMes);
					}
				} else {
					alert("失败："+data.error);
				}
			}
		}

		);

	}

function endAirFlowTest(){
	var moId = $("#moId").val();
	var enbHexId = $("#enbHexId").val();
	$.ajax({
		type : "post",
		url : "configStatus.do",
		data : "statusFlag="
				+ "AIR_FLOW_END" + "&moId="+moId+"&enbHexId="+enbHexId,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if (status == 0) {
				var result = data.model;
				var errMes = data.model.errorStr;
					if (result.beginResult == 0) {
						alert("成功");
						//停止灌包成功后刷新
						var basePath=$("#basePath").val();
						var statusFlag=$("#statusFlag").val();
						var moId=$("#moId").val();
						var enbHexId=$("#enbHexId").val();

						var href = basePath+"/lte/queryStatus" +
								".do?statusFlag="+statusFlag+"&moId="+moId+"&enbHexId="+enbHexId;
						window.location.href=""+href+"";
					} else {
						alert("失败："+errMes);
					}
				} else {
					alert("失败："+data.error);
				}
			}
		}
	
	);
}