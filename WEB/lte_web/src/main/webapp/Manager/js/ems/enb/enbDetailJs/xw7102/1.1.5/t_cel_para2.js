$(function(){
	//u16IntraFreqPrdMeasCfg
	$("#u16IntraFreqPrdMeasCfg option").each(function(index){
		if($("#u16IntraFreqPrdMeasCfg option:eq("+index+")").val() == $("#u16IntraFreqPrdMeasCfgAAA").val()){
			$("#u16IntraFreqPrdMeasCfg option:eq("+index+")").attr("selected",true);
		}
	});
	//u8IntraFreqPrdMeasCfg
	$("#u8IntraFreqPrdMeasCfg option").each(function(index){
		if($("#u8IntraFreqPrdMeasCfg option:eq("+index+")").val() == $("#u8IntraFreqPrdMeasCfgAAA").val()){
			$("#u8IntraFreqPrdMeasCfg option:eq("+index+")").attr("selected",true);
		}
	}); 
	//u8IcicA3MeasCfg
	$("#u8IcicA3MeasCfg option").each(function(index){
		if($("#u8IcicA3MeasCfg option:eq("+index+")").val() == $("#u8IcicA3MeasCfgAAA").val()){
			$("#u8IcicA3MeasCfg option:eq("+index+")").attr("selected",true);
		}
	});
});  