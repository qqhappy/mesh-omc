//为单板表每条记录查询对应的光口号
function queryU8FiberPortForBoard(basePath, u8SRackNO, moId) {
	var result = "";
	$.ajax({
		type : "post",
		url : basePath + "/lte/queryU8FiberPort.do",
		data : "u8SRackNO=" + u8SRackNO + "&moId=" + moId,
		dataType : "text",
		async : false,
		success : function(data) {
			if (data == null) {
				data = "";
			}
			result = data;
		}
	});
	return result;
}

// 为网管表查询IPID代表的具体IP地址
function queryIpDetail(basePath, ipId, moId) {
	var result = "";
	$.ajax({
		type : "post",
		url : basePath + "/lte/queryIpDetail.do",
		data : "ipId=" + ipId + "&moId=" + moId,
		dataType : "text",
		async : false,
		success : function(data) {
			result = transferHexIpToDecimalIp($.trim(data));
		}
	});
	return result;
}

// 将IP的16进制类型（00000000）转换为十进制（0.0.0.0）
function transferHexIpToDecimalIp(hexIp) {
	var ipArray = hexIp.split("");
	var result = "";
	for ( var i = 0; i < ipArray.length; i = i + 2) {
		var ipPart = ipArray[i] + ipArray[i + 1];
		result = result + parseInt(ipPart, 16).toString(10) + ".";
	}
	result = result.substring(0, result.length - 1);
	return result;
}

// 为小区参数表新增时查询移动国家码、移动网络码并赋值给dom
function queryMccMncForCel() {
	$.ajax({
		type : "post",
		url : "querySysPara.do",
		dataType : "json",
		async : false,
		success : function(data) {
			var mcc = "";
			var mnc = "";
			if (data.status == 0) {
				var myMcc = data.message.mcc;
				var myMnc = data.message.mnc;
				$("#au8MCC").val(myMcc);
				$("#au8MNC").val(myMnc);
			} else {
				$("#au8MCC").val("000000");
				$("#au8MNC").val("0000ff");
			}
		}
	});
}

// 为小区参数表查询跟踪区码列表，并填入dom
function queryTaForCel() {
	$.ajax({
		type : "post",
		url : "queryAllTa.do",
		dataType : "json",
		async : false,
		success : function(data) {
			var mcc = "";
			var mnc = "";
			if (data.status == 0) {
				if (data.message.length > 0) {
					var option = "";
					for ( var i = 0; i < data.message.length; i++) {
						option = option + '<option value="'
								+ data.message[i].code + '">'
								+ data.message[i].code + '</option>';
					}
					$("#u16TAC").html(option);
				}
			}
		}
	});
}

// 拓扑号转为对应RRU
function turnTopoIdToRRU() {
	$("#u8TopoNO option").each(function() {
		var value = $(this).val();
		var num = parseInt(value) + parseInt(1);
		var rru = "RRU" + num;
		$(this).html(rru);
	});
}
//查询核心网类型
function queryCoreNetType(){
	var type ;
	$.ajax({
		type:"post",
		url:"queryCoreNetType.do",
		dataType:"text",
		async:false,
		success:function(data){
			type = data;
		}
	});
	return type;
}

//查询核心网ran地址
function queryRanIp(){
	var ranIp ;
	$.ajax({
		type:"post",
		url:"queryRanIp.do",
		dataType:"text",
		async:false,
		success:function(data){
			if(data == null){
				ranIp = 0;
			}
			ranIp = data;
		}
	});
	return ranIp;
}