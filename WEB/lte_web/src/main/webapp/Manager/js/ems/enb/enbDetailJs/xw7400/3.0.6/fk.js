$(function(){
	var moId = $("input[name='moId']").val();
	var tableName = $("input[name='tableName']").val();
	//单板表
	if(tableName == "T_BOARD"){
		queryFk(moId,tableName,"u8RackNO",0);
		queryFk(moId,tableName,"u8ShelfNO",0);
		$("#u8RackNO").change(function(){
			queryFk(moId,tableName,"u8ShelfNO",0);
		});
	}
	//拓扑表
	if(tableName == "T_TOPO"){
		if($("input[name='operType']").val() == "config"){
			//判定主机架号
			var u8MRackNO = $("#u8MRackNOValue").val();
			queryFk(moId,tableName,"u8RackNO",0);
			$("#u8MRackNO option").each(function(index){
				if($("#u8MRackNO option:eq("+index+")").val() == u8MRackNO){
					$("#u8MRackNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定主机框号
			var u8MShelfNO = $("#u8MShelfNOValue").val();
			queryFk(moId,tableName,"u8ShelfNO",0);
			$("#u8MShelfNO option").each(function(index){
				if($("#u8MShelfNO option:eq("+index+")").val() == u8MShelfNO){
					$("#u8MShelfNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定主槽位号
			var u8MSlotNO = $("#u8MSlotNOValue").val();
			queryFk(moId,tableName,"u8SlotNO",0);
			$("#u8MSlotNO option").each(function(index){
				if($("#u8MSlotNO option:eq("+index+")").val() == u8MSlotNO){
					$("#u8MSlotNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定从机架号
			var u8SRackNO = $("#u8SRackNOValue").val();
			queryFk(moId,tableName,"u8RackNO",1);
			$("#u8SRackNO option").each(function(index){
				if($("#u8SRackNO option:eq("+index+")").val() == u8SRackNO){
					$("#u8SRackNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定从机框号
			var u8SShelfNO = $("#u8SShelfNOValue").val();
			queryFk(moId,tableName,"u8ShelfNO",1);
			$("#u8SShelfNO option").each(function(index){
				if($("#u8SShelfNO option:eq("+index+")").val() == u8SShelfNO){
					$("#u8SShelfNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定从槽位号
			var u8SSlotNO = $("#u8SSlotNOValue").val();
			queryFk(moId,tableName,"u8SlotNO",1);
			$("#u8SSlotNO option").each(function(index){
				if($("#u8SSlotNO option:eq("+index+")").val() == u8SSlotNO){
					$("#u8SSlotNO option:eq("+index+")").attr("selected",true);
				}
			});

		}else{
			queryFk(moId,tableName,"u8RackNO",0);
			queryFk(moId,tableName,"u8ShelfNO",0);
			queryFk(moId,tableName,"u8SlotNO",0);
			queryFk(moId,tableName,"u8RackNO",1);
			queryFk(moId,tableName,"u8ShelfNO",1);
			queryFk(moId,tableName,"u8SlotNO",1);
		}
		$("#u8MRackNO").change(function(){
			queryFk(moId,tableName,"u8ShelfNO",0);
			queryFk(moId,tableName,"u8SlotNO",0);
		});
		$("#u8MShelfNO").change(function(){
			queryFk(moId,tableName,"u8SlotNO",0);
		});
		$("#u8SRackNO").change(function(){
			queryFk(moId,tableName,"u8ShelfNO",1);
			queryFk(moId,tableName,"u8SlotNO",1);
		});
		$("#u8SShelfNO").change(function(){
			queryFk(moId,tableName,"u8SlotNO",1);
		});
	}
	//环境监控表和以太网表
	if(tableName == "T_ENVMON" || tableName == "T_ETHPARA" || tableName == "T_ETHPARA-3.0.6"){
		if($("input[name='operType']").val() == "config"){
			//判定机架号
			var u8RackNO = $("#u8RackNOValue").val();
			queryFk(moId,tableName,"u8RackNO",0);
			$("#u8RackNO option").each(function(index){
				if($("#u8RackNO option:eq("+index+")").val() == u8RackNO){
					$("#u8RackNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定机框号
			var u8ShelfNO = $("#u8ShelfNOValue").val();
			queryFk(moId,tableName,"u8ShelfNO",0);
			$("#u8ShelfNO option").each(function(index){
				if($("#u8ShelfNO option:eq("+index+")").val() == u8ShelfNO){
					$("#u8ShelfNO option:eq("+index+")").attr("selected",true);
				}
			});
			//判定槽位号
			var u8SlotNO = $("#u8SlotNOValue").val();
			queryFk(moId,tableName,"u8SlotNO",0);
			$("#u8SlotNO option").each(function(index){
				if($("#u8SlotNO option:eq("+index+")").val() == u8SlotNO){
					$("#u8SlotNO option:eq("+index+")").attr("selected",true);
				}
			});
		}else{
			queryFk(moId,tableName,"u8RackNO",0);
			queryFk(moId,tableName,"u8ShelfNO",0);
			queryFk(moId,tableName,"u8SlotNO",0);
		}
		$("#u8RackNO").change(function(){
			queryFk(moId,tableName,"u8ShelfNO",0);
			queryFk(moId,tableName,"u8SlotNO",0);
		});
		$("#u8ShelfNO").change(function(){
			queryFk(moId,tableName,"u8SlotNO",0);
		});
	}
});
function queryFk(moId,tableName,fkName,isMain){
	var referTable = "";
	var para = "";
	//单板表
	if(tableName == "T_BOARD"){
		referTable = "T_SHELF";
		if(fkName == "u8ShelfNO"){
			var u8RackNO = $("#u8RackNO").val();
			para = "u8RackNO="+u8RackNO;
		}
	}
	//拓扑表
	if(tableName == "T_TOPO"){
		referTable = "T_BOARD";
		if(fkName == "u8ShelfNO"){
			if(isMain == 1){
				var u8RackNO = $("#u8SRackNO").val();
				para = "u8RackNO="+u8RackNO;
			}else{
				var u8RackNO = $("#u8MRackNO").val();
				para = "u8RackNO="+u8RackNO;
			}			
		}
		if(fkName == "u8SlotNO"){
			if(isMain == 1){
				var u8RackNO = $("#u8SRackNO").val();
				var u8ShelfNO = $("#u8SShelfNO").val();
				para = "u8RackNO="+u8RackNO+";"
						+"u8ShelfNO="+u8ShelfNO;
			}else{
				var u8RackNO = $("#u8MRackNO").val();
				var u8ShelfNO = $("#u8MShelfNO").val();
				para = "u8RackNO="+u8RackNO+";"
						+"u8ShelfNO="+u8ShelfNO;
			}			
		}
	}
	//环境监控表和以太网表
	if(tableName == "T_ENVMON" || tableName == "T_ETHPARA"  || tableName == "T_ETHPARA-3.0.6"){
		referTable = "T_BOARD";
		if(fkName == "u8ShelfNO"){
			var u8RackNO = $("#u8RackNO").val();
			para = "u8RackNO="+u8RackNO;
		}
		if(fkName == "u8SlotNO"){
			var u8RackNO = $("#u8RackNO").val();
			var u8ShelfNO = $("#u8ShelfNO").val();
			para = "u8RackNO="+u8RackNO+";"
					+"u8ShelfNO="+u8ShelfNO;
		}
	}
	$.ajax({
		type:"post",
		url:"queryFk.do",
		data:"parameters="+para+
			"&moId="+moId+
			"&referTable="+referTable+
			"&browseTime="+getBrowseTime()+
			"&tableName="+tableName+
			"&isMain="+isMain+
			"&fkName="+fkName,
		dataType:"json",
		async:false,
		success:function(data){
			var basePath = $("#basePath").val();
			if(!sessionsCheck(data,basePath)){
				return ;
			}
			var myOption = "";
			var length = data.referFkModel.fkList.length;
			if(length > 0){
				for(var i=0;i<length ;i++){
					myOption+='<option value="'+data.referFkModel.fkList[i]+'">'+data.referFkModel.fkList[i]+'</option>';
				}
			}
			//单板表
			if(data.referFkModel.tableName == "T_BOARD"){
				if(data.referFkModel.fkName == "u8RackNO"){
					$("#u8RackNO").html(myOption);
				}
				if(data.referFkModel.fkName == "u8ShelfNO"){
					$("#u8ShelfNO").html(myOption);
				}
			}	
			//拓扑表
			if(data.referFkModel.tableName == "T_TOPO"){
				if(data.referFkModel.fkName == "u8RackNO"){
					if(data.referFkModel.isMain == 1){
						$("#u8SRackNO").html(myOption);
					}else{
						$("#u8MRackNO").html(myOption);
					}
					
				}
				if(data.referFkModel.fkName == "u8ShelfNO"){
					if(data.referFkModel.isMain == 1){
						$("#u8SShelfNO").html(myOption);
					}else{
						$("#u8MShelfNO").html(myOption);
					}					
				}
				if(data.referFkModel.fkName == "u8SlotNO"){
					if(data.referFkModel.isMain == 1){
						$("#u8SSlotNO").html(myOption);
					}else{
						$("#u8MSlotNO").html(myOption);
					}
					
				}
			}
			//环境监控表和以太网表
			if(data.referFkModel.tableName == "T_ENVMON" || data.referFkModel.tableName == "T_ETHPARA"  || tableName == "T_ETHPARA-3.0.6"){
				if(data.referFkModel.fkName == "u8RackNO"){
					$("#u8RackNO").html(myOption);
				}
				if(data.referFkModel.fkName == "u8ShelfNO"){
					$("#u8ShelfNO").html(myOption);
				}
				if(data.referFkModel.fkName == "u8SlotNO"){
					$("#u8SlotNO").html(myOption);
				}
			}
		}
	});
}
