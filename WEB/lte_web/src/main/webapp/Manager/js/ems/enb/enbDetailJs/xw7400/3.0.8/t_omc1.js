$(function(){
		var va = $("#au8OmcServerIP1").val().split("");
		var va1 = va[0] + va[1] ;
		var va2 = va[2] + va[3] ;
		var va3 = va[4] + va[5] ;
		var va4 = va[6] + va[7] ;
		var num1 = parseInt(va1,16).toString(10);
		var num2 = parseInt(va2,16).toString(10);
		var num3 = parseInt(va3,16).toString(10);
		var num4 = parseInt(va4,16).toString(10);
		var res = num1 + "." + num2 + "." + num3 + "." + num4;
		$("#au8OmcServerIP1").val(res);
});