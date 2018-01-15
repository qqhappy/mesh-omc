
var isIp=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)$/;

/**
 * 校验IP地址是否合法
 * 
 * @param ipAddress
 * @returns 错误消息，如果IP地址合法，则返回空字符串
 */
function checkIpAddress(ipAddress) {
	var ip = convertIpTo8HexString(ipAddress);
	if(!(isIp.test(ipAddress))){
		return "/* 请输入正确的IP地址 */";	
	}else if(ip == "FFFFFFFF" || ip == "ffffffff" || ip == "00000000"){
		return "/* 请输入正确的IP地址 */";			
	}else{
		return "";
	}
}

/**
 * 校验子网掩码是否合法
 * 
 * @param netMask
 * @returns 错误消息，如果子网掩码合法，则返回空字符串
 */
function checkNetMask(netMask) {
	var mask = convertIpTo8HexString(netMask);
	if(!(isIp.test(netMask))){
		return "/* 请输入正确的掩码 */";		
	}else if(mask == "FFFFFFFF" || mask == "ffffffff"){
		return "/* 请输入正确的掩码 */";		
	}else if(!isNetMaskLegal(mask)) {
		return "/* 请输入正确的掩码 */";	
	} else{
		return "";
	}
}

/**
 * 校验网关地址是否合法
 * 
 * @param gateway
 * @returns 错误消息，如果网关地址合法，则返回空字符串
 */
function checkGateway(gateway) {
	var gate = convertIpTo8HexString(gateway);
	if(!(isIp.test(gateway))){
		return "/* 请输入正确的网关地址 */";	
	}else if(gate == "FFFFFFFF" || gate == "ffffffff" ){
		return "/* 请输入正确的网关地址 */";
	}else if(gate == "00000000") {
		return "/* 网关地址不能为0.0.0.0 */";	
	}else{
		return "";
	}
}

/**
 * 是否IP和网关在同一网段
 * @param ipAddrHex
 * @param netMaskHex
 * @param gatewayHex
 * @returns {Boolean}
 */
function isIpAndGatewaySameSegment(ipAddrHex, netMaskHex, gatewayHex) {
	var ipLong = parseInt(ipAddrHex, 16);
	var maskLong = parseInt(netMaskHex, 16);
	var gateLong = parseInt(gatewayHex, 16);

	return (ipLong & maskLong) == (gateLong & maskLong);
}

/**
 * 将IP地址类型的字符串转换为8位16进制字符串
 */
function convertIpTo8HexString(value) {
	var sw = value.split(".");
	var sw1 = parseInt(sw[0]).toString(16);
	if(sw1.length<2){
		sw1 = "0" + sw1;	
	}
	var sw2 = parseInt(sw[1]).toString(16);
	if(sw2.length<2){
		sw2 = "0" + sw2;	
	}
	var sw3 = parseInt(sw[2]).toString(16);
	if(sw3.length<2){
		sw3 = "0" + sw3;	
	}
	var sw4 = parseInt(sw[3]).toString(16);
	if(sw4.length<2){
		sw4 = "0" + sw4;	
	}
	return sw1+sw2+sw3+sw4;
}

/**
 * 将8位16进制数转换为*.*.*.*的IP地址格式 
 * @param hexString
 * @returns {String}
 */
function convert8HexStringToIp(hexString) {
	var str1 = hexString[0] + hexString[1] ;
	var str2 = hexString[2] + hexString[3] ;
	var str3 = hexString[4] + hexString[5] ;
	var str4 = hexString[6] + hexString[7] ;
	var no1 = parseInt(str1,16).toString(10);
	var no2 = parseInt(str2,16).toString(10);
	var no3 = parseInt(str3,16).toString(10);
	var no4 = parseInt(str4,16).toString(10);
	return no1 + "." + no2 + "." + no3 + "." + no4;
}

/**
 * 十六进制的掩码是否合法
 */
function isNetMaskLegal(netMask) {

	if(netMask == "00000000")
		return false;
	
	var binaryArray = convertHexStringToBinaryArray(netMask);
	binaryArray = binaryArray.split("");
	var zeroOccur = false;
	
	for(var i = 0; i < binaryArray.length; i++) {
		if(binaryArray[i] == "0") {
			zeroOccur = true;
		} else if(binaryArray[i] == "1") {
			if(zeroOccur) {
				return false;
			}
		}
	}
	return true;
}

function convertHexStringToBinaryArray(hexString) {
	hexString = hexString.split("");
	var stringLength = hexString.length;
	var resultStr = "";
	for(var i = 0; i < stringLength; i++) {
		var number = parseInt(hexString[i], 16);
		resultStr += getBinaryArray(number);
	}
	return resultStr;
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
