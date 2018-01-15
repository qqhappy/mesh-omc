/**
 * Created by user on 16-9-7.
 */
$(function(){			
	$("#newAdd").click(function(){
		var type = $("#type").val();
		var UEID = $("#UEID").val();
		window.location.href="addUEVersion.do?type=1"+"&UEID="+UEID;
	});	
		
	$("#UeVersionTbody").delegate("a[opetype=update]", "click", function (e) {
		 var $tr = $(this).parents("tr");
		 var verFileName = $tr.find("#verFileName").text();
		 if(confirm("确定进行升级?\n")){
			 UpgradeUE(verFileName);
		 }
		    
	});	
	
	 $("#delete").click(function (e) {
		    var VerFileList = [];
			 var type = $("#type").val();
			 var UEID = $("#UEID").val();
		    var $UEchecked = $("[name=checkson]:checked");
		    if($UEchecked.length ==0){
		      alert("请选择版本");
		      return;
		    }
		    
		    if ($UEchecked.length == 1){
		    	var verFileName = $UEchecked.parent().nextAll("#verFileName").html();
		    	if (verFileName == ""){
		    		alert("当前无UE版本记录!");
		    		return;
		    	}
		    }

		    var strInfo = "\n";		    
		    $UEchecked.each(function(index){
		      var verFileName = $(this).parent().nextAll("#verFileName").html();
		      if(verFileName !=null || $.trim(verFileName).length !==0){
		    	  VerFileList.push(verFileName);
		    	  strInfo += verFileName + "\n";
		      }
		      else{
		        console.log("delete tr="+index+" versionfile is empty");
		      }
		    });
		    
		    if(confirm("确定要删除终端版本：" + strInfo + "")){
		    	DeleteUEVer(VerFileList);
		    }
	 });	 
});

function UpgradeUE(verFileName){
	 var UEList = [];
	 var type = $("#type").val();
	 var UEID = $("#UEID").val();
	 var basePath = $("#basePath").val();
	 
	 UEList.push(UEID); 
	  $.ajax({
	    type:"GET",
	    url:"upgrade.do",
	    data:{Version:verFileName,type:type,UEIDList:UEList.join(",")},
	    dataType:"json",
		success:function(Result){
			if (Result != null && Result.result == "success"){
				alert("UE版本升级命令下发成功!");
				$("#mainFrame",parent.document).attr("src","" + basePath + "/uem_web/UE/QueryUE.do?browseTime="+getBrowseTime());
				$("#leftFrame",parent.document).attr("src","" + basePath + "/lte/turnConfigManageLeft.do?browseTime="+getBrowseTime());
			}else{
				alert("UE版本升级命令下发失败!");
			}
	    },
	    error:function(xhr){
	    	alert("UE版本升级命令下发失败!");
	        console.log("升级UE ajax响应错误，提示："+xhr.status+"  "+xhr.statusText);
	      }
	  });
}

function DeleteUEVer(VerFileList){
	 var UEList = [];
	 var type = $("#type").val();
	 var UEID = $("#UEID").val();
	  $.ajax({
	    type:"GET",
	    url:"deleteVersion.do",
	    data:{UEID:UEID,type:type,VerFileList:VerFileList.join(",")},
	    dataType:"json",
		success:function(Result){
			if (Result != null && Result.result == "success"){
				alert("UE版本删除命令下发成功!");
				QueryUeVersion();
			}else{
		    	alert("UE版本删除命令下发失败!");
		    	QueryUeVersion(); 
			}
	    },
	    error:function(xhr){
	    	alert("UE版本删除命令下发失败!");
	    	QueryUeVersion(); 
	        console.log("删除UE ajax响应错误，提示："+xhr.status+"  "+xhr.statusText);
	      }
	  });
}

function QueryUeVersion(){
	var type = $("#type").val();
	var UEID = $("#UEID").val();
	var basePath = $("#basePath").val();
	window.location.href = "" + basePath + "/uem_web/UE/QueryUEVersion.do?type=" + type +"&UEID="+UEID;	
}
