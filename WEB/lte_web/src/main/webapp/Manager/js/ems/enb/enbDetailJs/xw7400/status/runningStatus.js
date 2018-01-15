$(function() {

	var error=$("#error").val();
	if(error != "") {
		return;
	}
	
	var rfLocalStatus = $("#rfLocalStatus").val();
	if (rfLocalStatus == 0) {
		$("#rfLocalStatus").val("锁定");
	} else {
		$("#rfLocalStatus").val("失锁");
	}

	var clockStatus = $("#clockStatus").val();
	if (clockStatus == 0) {
		$("#clockStatus").val("同步");
	} else {
		$("#clockStatus").val("失步");
	}

	var irInfWorkMode = $("#irInfWorkMode").val();
	if (irInfWorkMode == 1) {
		$("#irInfWorkMode").val("普通模式");
	} else {
		$("#irInfWorkMode").val("级连模式");
	}

	var runningStatus = $("#runningStatus").val();
	if (runningStatus == 0) {
		$("#runningStatus").val("未运营");
	} else if (runningStatus == 1){
		$("#runningStatus").val("测试中");
	} else if (runningStatus == 2){
		$("#runningStatus").val("运营中");
	} else if (runningStatus == 3){
		$("#runningStatus").val("版本升级中");
	} else if (runningStatus == 4){
		$("#runningStatus").val("不正常");
	}

	var dpdTrainResult = $("#dpdTrainResult").val();
	var channelNum = $("#channelNum").val();
	var resultArray = "";
	if(channelNum == 4) {
		resultArray = getBinaryArray(parseInt(dpdTrainResult));
	} else if(channelNum == 8){
		var temp = dpdTrainResult >>> 4;
		var resultArray = getBinaryArray(parseInt(temp));
		temp = dpdTrainResult << 28;
		temp = temp >>> 28;
		resultArray = resultArray + getBinaryArray(parseInt(temp));
	}
	for(var i = 1; i <= channelNum; i++) {
		if (resultArray[channelNum-i] == "0") {
			$("#dpdTrainResult"+i).val("失败");
		} else {
			$("#dpdTrainResult"+i).val("成功");
		}
	}

	function getBinaryArray(number) {
		if(number == 0) {
			return "0000";
		} else if(number == 1) {
			return "0001";
		} else if(number == 2) {
			return "0010";
		} else if(number == 3) {
			return "0011";
		} else if(number == 4) {
			return "0100";
		} else if(number == 5) {
			return "0101";
		} else if(number == 6) {
			return "0110";
		} else if(number == 7) {
			return "0111";
		} else if(number == 8) {
			return "1000";
		} else if(number == 9) {
			return "1001";
		} else if(number == 10) {
			return "1010";
		} else if(number == 11) {
			return "1011";
		} else if(number == 12) {
			return "1100";
		} else if(number == 13) {
			return "1101";
		} else if(number == 14) {
			return "1110";
		} else if(number == 15) {
			return "1111";
		}
	}
	
});
