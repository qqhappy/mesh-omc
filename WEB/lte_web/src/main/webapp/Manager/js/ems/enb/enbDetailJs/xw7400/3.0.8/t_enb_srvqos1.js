$(function(){
	if($("#u8BearerType").val()==1){
		$("#u32PBR").removeAttr("disabled");
		$("#u32PBR").attr("value","1");
	}else{
		$("#u32PBR").attr("disabled","disabled");	
		$("#u32PBR").removeAttr("value");
	}
	$("#u8BearerType").change(function(){
		if($("#u8BearerType").val()==1){
			$("#u32PBR").removeAttr("disabled");
			$("#u32PBR").attr("value","1");
		}else{
			$("#u32PBR").attr("disabled","disabled");	
			$("#u32PBR").removeAttr("value");
		}							   
	});
});