$(function(){
	//频点联动 
	var u8FreqBandInd = $("#u8FreqBandInd").val();
	var u8SysBandWidth = $("#u8SysBandWidth").val();
	$("#centerFreq1").html(getMinBoundary(u8FreqBandInd,u8SysBandWidth));	
	$("#centerFreq2").html(getMaxBoundary(u8FreqBandInd,u8SysBandWidth));
	//频段指示内显示详细频点范围
	setu8FreqBandIndOption(u8SysBandWidth);
	$("#u8FreqBandInd").change(function(){
		var u8FreqBandInd = $(this).val();
		var u8SysBandWidth = $("#u8SysBandWidth").val();
		$("#centerFreq1").html(getMinBoundary(u8FreqBandInd,u8SysBandWidth));	
		$("#centerFreq2").html(getMaxBoundary(u8FreqBandInd,u8SysBandWidth));
	});
	$("#u8SysBandWidth").change(function(){
		var u8FreqBandInd = $("#u8FreqBandInd").val();
		var u8SysBandWidth = $(this).val();
		setu8FreqBandIndOption(u8SysBandWidth);
		$("#centerFreq1").html(getMinBoundary(u8FreqBandInd,u8SysBandWidth));	
		$("#centerFreq2").html(getMaxBoundary(u8FreqBandInd,u8SysBandWidth));
	});

	//上行天线索引
	var ulAntIdxNum = getCheckedAntNum("ulAntCheckBox");
	var ulAntNum = getRealAntNum($("#u8UlAntNum").val());
	if(ulAntIdxNum >= ulAntNum){
		$(".ulAntCheckBox").each(function(){
			if($(this).attr("checked") !=  "checked"){
				$(this).attr("disabled","disabled");
			}
		});
	}		
	$(".ulAntCheckBox").click(function(){
		var ulAntIdxNum = getCheckedAntNum("ulAntCheckBox");
		var ulAntNum = getRealAntNum($("#u8UlAntNum").val());
		if(ulAntIdxNum >= ulAntNum){
			$(".ulAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).attr("disabled","disabled");
				}
			});
		}else{
			$(".ulAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).removeAttr("disabled");
				}
			});
		}
	});
	$("#u8UlAntNum").change(function(){
		var ulAntIdxNum = getCheckedAntNum("ulAntCheckBox");
		var ulAntNum = getRealAntNum($("#u8UlAntNum").val());
		if(ulAntIdxNum > ulAntNum){
			$(".ulAntCheckBox").removeAttr("checked");
			$(".ulAntCheckBox").removeAttr("disabled");
		}else if(ulAntIdxNum == ulAntNum){
			$(".ulAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).attr("disabled","disabled");
				}
			});
		}else{
			$(".ulAntCheckBox").removeAttr("disabled");
		}
	});
	//下行天线索引
	var dlAntIdxNum = getCheckedAntNum("dlAntCheckBox");
	var dlAntNum = getRealAntNum($("#u8DlAntNum").val());
	if(dlAntIdxNum >= dlAntNum){
		$(".dlAntCheckBox").each(function(){
			if($(this).attr("checked") !=  "checked"){
				$(this).attr("disabled","disabled");
			}
		});
	}		
	$(".dlAntCheckBox").click(function(){
		var dlAntIdxNum = getCheckedAntNum("dlAntCheckBox");
		var dlAntNum = getRealAntNum($("#u8DlAntNum").val());
		if(dlAntIdxNum >= dlAntNum){
			$(".dlAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).attr("disabled","disabled");
				}
			});
		}else{
			$(".dlAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).removeAttr("disabled");
				}
			});
		}
		//对端口映射关系的控制
		controlPortMap();		
	});
	$("#u8DlAntNum").change(function(){
		var dlAntIdxNum = getCheckedAntNum("dlAntCheckBox");
		var dlAntNum = getRealAntNum($("#u8DlAntNum").val());
		if(dlAntIdxNum > dlAntNum){
			$(".dlAntCheckBox").removeAttr("checked");
			$(".dlAntCheckBox").removeAttr("disabled");
		}else if(dlAntIdxNum == dlAntNum){
			$(".dlAntCheckBox").each(function(){
				if($(this).attr("checked") !=  "checked"){
					$(this).attr("disabled","disabled");
				}
			});
		}else{
			$(".dlAntCheckBox").removeAttr("disabled");
		}
		controlPortMap();
		
	});
	//端口映射关系
	$("#dlAntPortMapTable tr").each(function(trIndex){
		$("#dlAntPortMapTable tr:eq("+trIndex+") td").each(function(tdIndex){
			$("#dlAntPortMapTable tr:eq("+trIndex+") td:eq("+tdIndex+") input").addClass("portmap"+tdIndex);
			$("#dlAntPortMapTable tr:eq("+trIndex+") td:eq("+tdIndex+") input").val(tdIndex);
		});
	});
	controlPortMap();	
	$("#u8DlAntPortNum").change(function(){
		$("#dlAntPortMapTable input").attr("disabled","disabled");
		$(".dlAntCheckBox").each(function(){
			if($(this).attr("checked") ==  "checked"){
				var value = $(this).val();
				$(".portmap"+value).removeAttr("checked");
				$(".portmap"+value).removeAttr("disabled");
			}
		});
		var portNum = $("#u8DlAntPortNum").val();
		if(portNum == 0){
			$("#dlAntPortMapTable tr:eq(2) input").attr("disabled","disabled");
			$("#dlAntPortMapTable tr:eq(3) input").attr("disabled","disabled");
			$("#dlAntPortMapTable tr:eq(4) input").attr("disabled","disabled");
		}
		if(portNum == 1){
			$("#dlAntPortMapTable tr:eq(3) input").attr("disabled","disabled");
			$("#dlAntPortMapTable tr:eq(4) input").attr("disabled","disabled");
		}
		$("#dlAntPortMapTable input").each(function(){
			if($(this).attr("disabled") == "disabled"){
				$(this).removeAttr("checked");
			}
		});
	});	
	$("#dlAntPortMapTable input").click(function(){
		controlPortMap();
	});
});

//根据频段获取频点上边界
function getUpperBoundary(u8FreqBandInd){
	if(u8FreqBandInd == 33){
		return 1920;
	}else if(u8FreqBandInd == 34){
		return 2025;
	}else if(u8FreqBandInd == 35){
		return 1910;
	}else if(u8FreqBandInd == 36){
		return 1990;
	}else if(u8FreqBandInd == 37){
		return 1930;
	}else if(u8FreqBandInd == 38){
		return 2620;
	}else if(u8FreqBandInd == 39){
		return 1920;
	}else if(u8FreqBandInd == 40){
		return 2400;
	}else if(u8FreqBandInd == 45){
		return 1467;
	}else if(u8FreqBandInd == 53){
		return 798;
	}else if(u8FreqBandInd == 58){	
		return 430;
	}else if(u8FreqBandInd == 59){	
		return 1805;
	}else if(u8FreqBandInd == 61){
		return 1467;
	}else if(u8FreqBandInd == 62){
		return 1805;
	}else{
		return 678;
	}
}
//根据频段获取频点下边界
function getLowerBoundary(u8FreqBandInd){
	if(u8FreqBandInd == 33){
		return 1900;
	}else if(u8FreqBandInd == 34){
		return 2010;
	}else if(u8FreqBandInd == 35){
		return 1850;
	}else if(u8FreqBandInd == 36){
		return 1930;
	}else if(u8FreqBandInd == 37){
		return 1910;
	}else if(u8FreqBandInd == 38){
		return 2570;
	}else if(u8FreqBandInd == 39){	
		return 1880;
	}else if(u8FreqBandInd == 40){
		return 2300;
	}else if(u8FreqBandInd == 45){
		return 1447;
	}else if(u8FreqBandInd == 53){	
		return 778;
	}else if(u8FreqBandInd == 58){
		return 380;
	}else if(u8FreqBandInd == 59){
		return 1785;
	}else if(u8FreqBandInd == 61){
		return 1447;
	}else if(u8FreqBandInd == 62){
		return 1785;
	}else if(u8FreqBandInd == 63){	
		return 526;
	}
}
//获取带宽
function getSysBandWidth(u8SysBandWidth){
	if(u8SysBandWidth == 0){
		return  1.4;
	}else if(u8SysBandWidth == 1){
		return  3;
	}else if(u8SysBandWidth == 2){
		return  5;
	}else if(u8SysBandWidth == 3){
		return  10;
	}else if(u8SysBandWidth == 4){
		return  15;
	}else{
		return  20;
	}
}
//根据频段及带宽获取频点最大值
function getMaxBoundary(u8FreqBandInd,u8SysBandWidth){
	var upperBoundary = getUpperBoundary(u8FreqBandInd);
	var SysBandWidth = getSysBandWidth(u8SysBandWidth);
	var result = upperBoundary-accDiv(SysBandWidth,2);
	return result.toFixed(1);

}
//根据频段及带宽获取频点最小值
function getMinBoundary(u8FreqBandInd,u8SysBandWidth){
	var lowerBoundary = getLowerBoundary(u8FreqBandInd);
	var SysBandWidth = getSysBandWidth(u8SysBandWidth);
	var result = accAdd(lowerBoundary,accDiv(SysBandWidth,2));
	return result.toFixed(1);
}
//频段指示内显示详细频点范围
function setu8FreqBandIndOption(u8SysBandWidth){
	$("#u8FreqBandInd option").each(function(){
		var u8FreqBandInd = $(this).val();
		var maxBoundary = getUpperBoundary(u8FreqBandInd);
		var minBoundary = getLowerBoundary(u8FreqBandInd);
		var optioHtml = $(this).val();	
		optioHtml = optioHtml+" ("+minBoundary+"~"+maxBoundary+")(MHz)";
		$(this).html(optioHtml);
	});
}
//获得有效天线数
function getRealAntNum(antNum){
	if(antNum == 0){
		return 1;
	}else if(antNum == 1){
		return 2;
	}else if(antNum == 2){
		return 4;
	}else{
		return 8;
	}
}
//根据类名获取被选中的天线数
function getCheckedAntNum(antClass){
	var index = 0;
	$("."+antClass).each(function(){
		if($(this).attr("checked") == "checked"){
			index++;
		}
	});
	return index;
}
//下行使能天线索引和下行天线端口数变化时控制portMap
function controlPortMap(){
	$("#dlAntPortMapTable input").attr("disabled","disabled");
	$(".dlAntCheckBox").each(function(){
		if($(this).attr("checked") ==  "checked"){
			var value = $(this).val();
			var index = 0;
			$(".portmap"+value).each(function(){
				if($(this).attr("checked") == "checked"){
					index++;
				}
			});	
			if(index == 0){
				$(".portmap"+value).removeAttr("disabled");
			}else{
				$(".portmap"+value).attr("disabled","disabled");
				$(".portmap"+value).each(function(){
					if($(this).attr("checked") == "checked"){
						$(this).removeAttr("disabled");
					}
				});
			}		
		}
	});
	var portNum = $("#u8DlAntPortNum").val();
	if(portNum == 0){
		$("#dlAntPortMapTable tr:eq(2) input").attr("disabled","disabled");
		$("#dlAntPortMapTable tr:eq(3) input").attr("disabled","disabled");
		$("#dlAntPortMapTable tr:eq(4) input").attr("disabled","disabled");
	}
	if(portNum == 1){
		$("#dlAntPortMapTable tr:eq(3) input").attr("disabled","disabled");
		$("#dlAntPortMapTable tr:eq(4) input").attr("disabled","disabled");
	}
	$("#dlAntPortMapTable input").each(function(){
		if($(this).attr("disabled") == "disabled"){
			$(this).removeAttr("checked");
		}
	});
	
	
}
//查询空闲物理小区标识并填入dom
function getFreeU16PhyCellId(){
	var moId = $("#moId").val();
	var enbVersion = $("#enbVersion").val();
	$.ajax({
		type : "post",
		url : "getFreePci.do",
		data:"enbVersion="+enbVersion
			+"&moId="+moId,
		dataType : "json",
		async : false,
		success : function(data) {
			if(data.status == 0){
				$("#u16PhyCellId").val(data.message);
			}else{
				alert(data.error);
			}
		}
	});
}
