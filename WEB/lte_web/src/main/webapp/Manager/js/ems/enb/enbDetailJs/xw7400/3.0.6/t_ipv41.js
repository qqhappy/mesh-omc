$(function(){
		var va = $("#au8IPAddr1").val().split("");
		var va1 = va[0] + va[1] ;
		var va2 = va[2] + va[3] ;
		var va3 = va[4] + va[5] ;
		var va4 = va[6] + va[7] ;
		var num1 = parseInt(va1,16).toString(10);
		var num2 = parseInt(va2,16).toString(10);
		var num3 = parseInt(va3,16).toString(10);
		var num4 = parseInt(va4,16).toString(10);
		var res = num1 + "." + num2 + "." + num3 + "." + num4;
		$("#au8IPAddr1").val(res);
		
		var vaq = $("#au8NetMask1").val().split("");
		var vaq1 = vaq[0] + vaq[1] ;
		var vaq2 = vaq[2] + vaq[3] ;
		var vaq3 = vaq[4] + vaq[5] ;
		var vaq4 = vaq[6] + vaq[7] ;
		var numq1 = parseInt(vaq1,16).toString(10);
		var numq2 = parseInt(vaq2,16).toString(10);
		var numq3 = parseInt(vaq3,16).toString(10);
		var numq4 = parseInt(vaq4,16).toString(10);
		var resq = numq1 + "." + numq2 + "." + numq3 + "." + numq4;
		$("#au8NetMask1").val(resq);
		
		var vawAttr = $("#au8GWAddr1").val();
		if(vawAttr == "" || vawAttr == null){
			vawAttr = "00000000";
		}
		var vaw = vawAttr.split("");
		var vaw1 = vaw[0] + vaw[1] ;
		var vaw2 = vaw[2] + vaw[3] ;
		var vaw3 = vaw[4] + vaw[5] ;
		var vaw4 = vaw[6] + vaw[7] ;
		var numw1 = parseInt(vaw1,16).toString(10);
		var numw2 = parseInt(vaw2,16).toString(10);
		var numw3 = parseInt(vaw3,16).toString(10);
		var numw4 = parseInt(vaw4,16).toString(10);
		var resw = numw1 + "." + numw2 + "." + numw3 + "." + numw4;
		$("#au8GWAddr1").val(resw); 
});