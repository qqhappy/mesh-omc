$(function(){
		var va = $("#au8SntpMServer1").val().split("");
		var va1 = va[0] + va[1] ;
		var va2 = va[2] + va[3] ;
		var va3 = va[4] + va[5] ;
		var va4 = va[6] + va[7] ;
		var num1 = parseInt(va1,16).toString(10);
		var num2 = parseInt(va2,16).toString(10);
		var num3 = parseInt(va3,16).toString(10);
		var num4 = parseInt(va4,16).toString(10);
		var res = num1 + "." + num2 + "." + num3 + "." + num4;
		$("#au8SntpMServer1").val(res);
		
		var vaq = $("#au8SntpSServer1").val().split("");
		var vaq1 = vaq[0] + vaq[1] ;
		var vaq2 = vaq[2] + vaq[3] ;
		var vaq3 = vaq[4] + vaq[5] ;
		var vaq4 = vaq[6] + vaq[7] ;
		var numq1 = parseInt(vaq1,16).toString(10);
		var numq2 = parseInt(vaq2,16).toString(10);
		var numq3 = parseInt(vaq3,16).toString(10);
		var numq4 = parseInt(vaq4,16).toString(10);
		var resq = numq1 + "." + numq2 + "." + numq3 + "." + numq4;
		$("#au8SntpSServer1").val(resq);
		
});