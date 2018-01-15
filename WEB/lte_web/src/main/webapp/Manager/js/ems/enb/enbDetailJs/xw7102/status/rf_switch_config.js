$(function(){
	// 页面加载时设置
	var dlRfSwitch = $("#dlRfSwitch").val();
	var ulRfSwitch = $("#ulRfSwitch").val();
	var rruChannelNum = $("#rruChannelNum").val();
	// 创建开关表格
	createSwitchTable(rruChannelNum);
	
	$("#configButton").click(function(){
		config();
	});

	var error = $("#error").val();
	if(error != undefined && error != "") {
		alert("查询失败! 原因：" + error);
		return;
	}
	// 设置开关状态
	setRfSwitch(dlRfSwitch, ulRfSwitch, rruChannelNum);
	
	function config() {
		var basePath=$("#basePath").val();
		var statusFlag=$("#statusFlag").val();
		var moId=$("#moId").val();
		var enbHexId=$("#enbHexId").val();
		var rruChannelNum = $("#rruChannelNum").val();

		var href = basePath+"/lte/configrfStatus.do?statusFlag="+statusFlag+"&moId="+moId+"&enbHexId="+enbHexId;
		if(statusFlag == "102") {
			
			var dlRfSwitch = getDlRfSwitch(rruChannelNum);

			var ulRfSwitch = getUlRfSwitch(rruChannelNum);
			
			if(areAllSwitchOff(dlRfSwitch)) {
				if(!confirm("您将关闭所有下行通道射频开关,是否继续?")) {
					return;
				}
			}
			
			if(areAllSwitchOff(ulRfSwitch)) {
				if(!confirm("您将关闭所有上行通道射频开关,是否继续?")) {
					return;
				}
			}

			href = href +"&dlRfSwitch="+dlRfSwitch+"&ulRfSwitch="+ulRfSwitch;
		} else if(statusFlag == "ENB_TIME") {
			var moduleNo = $("#enbTime").val();
			href = href +"&enbTime="+enbTime;
		}
		window.location.href=""+href+"";
	}
	
	/**
	 * 是否所有的开关都为关闭状态
	 */
	function areAllSwitchOff(switchFlag) {
		var rruChannelNum = switchFlag.length;
		for(var i = 0; i < rruChannelNum; i++) {
			if(switchFlag[i] == "1")
				return false;
		}
		return true;
	}
	
	function getDlRfSwitch(rruChannelNum) {
		var dlRfSwitch = "";
		for(var i = 0; i < rruChannelNum; i++) {
			var temp = $("#dlRfSwitch"+(i+1)).attr("checked") == "checked"?1:0;
			dlRfSwitch += temp;
		}
		return dlRfSwitch;
	}
	
	function getUlRfSwitch(rruChannelNum) {
		var ulRfSwitch = "";
		for(var i = 0; i < rruChannelNum; i++) {
			var temp = $("#ulRfSwitch"+(i+1)).attr("checked") == "checked"?1:0;
			ulRfSwitch += temp;
		}
		return ulRfSwitch;
	}
	
	/**
	 * 设置天线开关状态
	 */
	function setRfSwitch(dlRfSwitch, ulRfSwitch,rruChannelNum) {
		for(var i = 0; i < rruChannelNum; i++) {
			var dlFlag = dlRfSwitch[i];
			if(dlFlag == "1") {
				$("#dlRfSwitch"+(i+1)).attr("checked", "checked");
			}
			var ulFlag = ulRfSwitch[i];
			if(ulFlag == "1") {
				$("#ulRfSwitch"+(i+1)).attr("checked", "checked");
			}
		}
	}
	
	function createSwitchTable(rruChannelNum) {

		var switchHeader = "<tr><th class=\"blankTd\">射频开关类型 </th>";
		var dlRfSwitchString = "<tr><td>下行射频开关 </td>";
		var ulRfSwitchString = "<tr><td>上行射频开关 </td>";
		for(var i = 0; i < rruChannelNum; i++) {
			var num = i+1;
			switchHeader += "<th>通道"+num+"</th>";
			dlRfSwitchString += "<td><input type=\"checkbox\" id=\"dlRfSwitch"+num+"\"/></td>";
			ulRfSwitchString += "<td><input type=\"checkbox\" id=\"ulRfSwitch"+num+"\"/></td>";
		}
		switchHeader += "</tr>";
		dlRfSwitchString += "</tr>";
		ulRfSwitchString += "</tr>";
		
		var tableHtml = 
			"<table width=\"100%\" class=\"newTabStyle\">"
				+ switchHeader
				+ dlRfSwitchString
				+ ulRfSwitchString
				+"<tr>"
					+"<td class=\"blankTd\" colspan=\""+(rruChannelNum+1)+"\" style=\"text-align:right; padding:5px;\">"
						+"<input type=\"button\" value=\"确定\" id=\"configButton\"/>"	
					+"</td>"
				+"</tr>"
			+"</table>";
		$("#switchSpan").html(tableHtml);
	}
});

