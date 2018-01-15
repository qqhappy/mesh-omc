 function beginPackageTest(){
	//归零
	clearInterval(interval_1);
	document.getElementById("d3").style.display = "none";
	document.getElementById("d5").style.width = 0;
	var moId=$("#moId").val();
	var enbHexId=$("#enbHexId").val();
	$.ajax({
		type : "post",
		url : "configStatus.do",
		data : "&statusFlag="
				+ "S1_PACKAGE_TEST" + "&moId="+moId+"&enbHexId="+enbHexId,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if (status == 0) {
				var result = data.model;
				var errMes = data.model.errorMsg;
				if (result.result == 0) {
					document.getElementById("d3").style.display = "block";
					interval_1 = setInterval(percent_1,100);
				} else {
					alert("启动失败，请检查基站至核心网连接");
				}
			} else {
//				document.getElementById("d3").style.display = "block";
//				interval_1 = setInterval(percent_1,100);
				alert("失败："+data.error);
			}
		}
	});
}

function queryPackageTest(){
	
	var d6_per = $("#d6").html();
	if(d6_per == ""){
		alert("请先开始测量");
		return;
	}else if(d6_per != "100%"){
		return ;
	}
	$("#tbody1").html('<tr><td>--</td><td>--</td></tr>');
	var moId=$("#moId").val();
	var enbHexId=$("#enbHexId").val();
	$.ajax({
		type : "post",
		url : "queryStatus.do",
		data : "&statusFlag="
				+ "PACKET_LOSS_RATE" + "&moId="+moId+"&enbHexId="+enbHexId,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if (status == 0) {
				var result = data.model;
				if(result == null || result.length == 0){
					$("#tbody1").html('<tr><td>--</td><td>--</td></tr>');
					alert("请稍后查询或重新进行测量");					
				}else{
					var flag = 0,
						tbody = '';					
					for(var i=0 ;i<result.length;i++){
						if(result[i].result == 0){
//							document.getElementById("plt").getElementsByTagName("tr")[i+1].getElementsByTagName("td")[0].innerHTML = parseFloat(result[i].localpacketLossRate/1000)+"%";
//							document.getElementById("plt").getElementsByTagName("tr")[i+1].getElementsByTagName("td")[1].innerHTML = parseFloat(result[i].dstpacketLossRate/1000)+"%";
							tbody = tbody + '<tr><td>'+parseFloat(result[i].localpacketLossRate/1000)+"%"+'</td><td>'+parseFloat(result[i].dstpacketLossRate/1000)+"%"+'</td></tr>';
						}else{
							$("#tbody1").html('<tr><td>--</td><td>--</td></tr>');
							flag = 1;
							alert("请稍后查询或重新进行测量");
							break;
						}
					}
					if(flag == 0){
						$("#tbody1").html(tbody);
					}
				}
			} else {
				$("#tbody1").html('<tr><td>--</td><td>--</td></tr>');
				alert("失败："+data.error);
			}
		}
	}

	);
	
}

function beginTimeTest(){
	//归零
	clearInterval(interval_2);
	document.getElementById("d7").style.display = "none";
	document.getElementById("d9").style.width = 0;
	var moId=$("#moId").val();
	var enbHexId=$("#enbHexId").val();
	$.ajax({
		type : "post",
		url : "configStatus.do",
		data : "&statusFlag="
				+ "S1_TIME_TEST" + "&moId="+moId+"&enbHexId="+enbHexId,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if (status == 0) {
				var result = data.model;
				var errMes = data.model.errorMsg;
				if (result.result == 0) {
					document.getElementById("d7").style.display = "block";
					interval_2 = setInterval(percent_2,100);
				} else {
					alert("启动失败，请重新启动测量");
				}
			} else {
				alert("失败："+data.error);
			//	document.getElementById("d7").style.display = "block";
				//interval_2 = setInterval(percent_2,100);
			}
		}
	}

	);
}

function queryTimeTest(){
	
	var d10_per = $("#d10").html();
	if(d10_per == ""){
		alert("请先开始测量");
		return;
	}else if(d10_per != "100%"){
		return ;
	}
	$("#tbody2").html('<tr><td>--</td></tr>');
	var moId=$("#moId").val();
	var enbHexId=$("#enbHexId").val();
	$.ajax({
		type : "post",
		url : "queryStatus.do",
		data : "&statusFlag="
				+ "TIME_DELAY" + "&moId="+moId+"&enbHexId="+enbHexId,
		dataType : "json",
		async : false,
		success : function(data) {
			var status = data.status;
			if (status == 0) {
				var result = data.model;
				if(result == null || result.length == 0){
					$("#tbody2").html('<tr><td>--</td></tr>');
					alert("请稍后查询或重新进行测量");
				}else{
					var flag = 0,
					tbody = '';	
					for(var i=0 ;i<result.length;i++){
						if(result[i].result == 0){
//							document.getElementById("tdt").getElementsByTagName("td")[i+1].innerHTML = parseFloat(result[i].timeDelay/1000)+"s";	
							tbody = tbody + '<tr><td>'+parseFloat(result[i].timeDelay/1000)+"ms"+'</td></tr>';
						}else{
							$("#tbody2").html('<tr><td>--</td></tr>');
							flag = 1;
							alert("请稍后查询或重新进行测量");
							break;
						}
					}
					if(flag == 0){
						$("#tbody2").html(tbody);
					}
				}
			} else {
				$("#tbody2").html('<tr><td>--</td></tr>');
				alert("失败："+data.error);
			}
		}
	}

	);
}