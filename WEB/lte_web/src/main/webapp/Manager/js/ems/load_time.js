$(function(){
	var date = new Date();
	var yy = date.getFullYear();
	var mm = date.getMonth()+1;
	if(mm < 10){
		mm = "0"+mm;
	}
	var dd = date.getDate();
	if(dd<10){
		dd = "0"+dd;	
	}
	$("#timepicker").val(yy+"-"+mm+"-"+dd);//yy+"-"+mm+"-"+dd);
	$("#timepicker").datepicker({dateFormat:"yy-mm-dd",timeFormat:"hh:mm:ss",changeMonth:true,changeYear:true,yearRange:"1990:2100"});
});