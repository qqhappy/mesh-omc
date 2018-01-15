/**
 * Created by user on 16-4-27.
 */
$(function () {
  QueryUEPerformanceInfo();
  setInterval(QueryUEPerformanceInfo,3000);


  $("#add").click(function (e) {
    window.parent.mainFrame.location.href = "AddUE.do";
  });

  $("#reboot").click(function (e) {
    var IMSIList = [];
    var UEIDList = [];
    var $UEchecked = $("[name=checkson]:checked");
    if($UEchecked.length ==0){
    	alert("请选择终端");
    	return;
    }
    
    $UEchecked.each(function(index){

      var IMSI = $(this).parent().nextAll("#IMSI").html();
      var UEID = $(this).parents("tr").attr("id");
      if(IMSI !=null || $.trim(IMSI).length !==0){
        IMSIList.push(IMSI);
      }
      else{
        console.log("Reboot tr="+index+" IMSI is empty");
      }
      if(UEID !=null || $.trim(UEID).length !==0){
        UEIDList.push(UEID);
      }
      else{
        console.log("Reboot tr="+index+" UEID is empty");
      }

    });
    if(confirm("重启终端 IMSI:"+IMSIList.join("\n"))){
      RebootUE(UEIDList);
    }
  });

  $("#delete").click(function (e) {
    var IMSIList = [];
    var UEIDList = [];
    var $UEchecked = $("[name=checkson]:checked");
    if($UEchecked.length ==0){
      alert("请选择终端");
      return;
    }

    $UEchecked.each(function(index){

      var IMSI = $(this).parent().nextAll("#IMSI").html();
      var UEID = $(this).parents("tr").attr("id");
      if(IMSI !=null || $.trim(IMSI).length !==0){
        IMSIList.push(IMSI);
      }
      else{
        console.log("Reboot tr="+index+" IMSI is empty");
      }
      if(UEID !=null || $.trim(UEID).length !==0){
        UEIDList.push(UEID);
      }
      else{
        console.log("Reboot tr="+index+" UEID is empty");
      }

    });
    if(confirm("确定删除终端 IMSI:\n"+IMSIList.join("\n"))){
      DeleteUE(UEIDList);
    }
  });

  $("#UeListTbody").delegate("a[opetype=modify]", "click", function (e) {
    var IMSI = $(this).parent().prevAll("#IMSI").html();    
    window.parent.mainFrame.location.href = "ModifyUE.do?UEQueryFilter=" + JSON.stringify({IMSI:IMSI});
  });

  $("#UeListTbody").delegate("a[opetype=reboot]", "click", function (e) {
    var IMSI = $(this).parent().prevAll("#IMSI").html();
    var UEID = $(this).parents("tr").attr("id");
    if(confirm("重启终端 IMSI:\n"+IMSI)){
      RebootUE([UEID]);
    }
    
  });
  
  $("#UeListTbody").delegate("a[opetype=update]", "click", function (e) {
	    var UEID = $(this).parents("tr").attr("id");
	    var IMSI = $(this).parent().prevAll("#IMSI").html();
	    var basePath = $("#basePath").val();
	    window.parent.mainFrame.location.href = "" + basePath + "/uem_web/UE/QueryUEVersion.do?type=1"+"&UEID="+UEID;
	    window.parent.leftFrame.location.href = "" + basePath + "/uem_web/UE/ShowUEUpgradeLeft.do?type=1"+"&UEID="+UEID+"&IMSI=" + IMSI;
  });

  $("#fresh").click(function () {
    var currentPage = $("#currentPage").html();
    var sortBy = $("#sortBy").val();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    queryUEStaticListInfo(currentPage, sortBy, searchBy, searchValue);
  });


  $("#firstPage").click(function () {
    var sortBy = $("#sortBy").val();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    queryUEStaticListInfo(1, sortBy, searchBy, searchValue);
  });

  $("#endPage").click(function () {
    var sortBy = $("#sortBy").val();
    var totalPage = $("#totalPage").html();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    queryUEStaticListInfo(totalPage, sortBy, searchBy, searchValue);
  });

  $("#previousPage").click(function () {
    var sortBy = $("#sortBy").val();
    var currentPage = $("#currentPage").html();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    if (parseInt(currentPage) == 1) {
      alert("已是首页");
    } else {
      var page = (parseInt(currentPage) - 1);
      queryUEStaticListInfo(page, sortBy, searchBy, searchValue);
    }
  });

  $("#nextPage").click(function () {
    var sortBy = $("#sortBy").val();
    var currentPage = $("#currentPage").html();
    var totalPage = $("#totalPage").html();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    if (parseInt(currentPage) == parseInt(totalPage)) {
    	 alert("已是尾页");
    } else {
      var page = (parseInt(currentPage) + 1);
      queryUEStaticListInfo(page, sortBy, searchBy, searchValue);
    }
  });
  //Ŀ��ҳ
  $("#targetPage").click(function () {
    var sortBy = $("#sortBy").val();
    var isNum = /^\d+$/;
    var targetPage = $("#targetPageInput").val();
    var totalPage = $("#totalPage").html();
    var searchBy = $("#searchByHidden").val();
    var searchValue = $("#searchValueHidden").val();
    if (isNum.test(targetPage)) {
      if (targetPage >= 1 && targetPage <= parseInt(totalPage)) {
        queryUEStaticListInfo(targetPage, sortBy, searchBy, searchValue);
      } else if (targetPage <= 1) {
        queryUEStaticListInfo(1, sortBy, searchBy, searchValue);
      } else {
        queryUEStaticListInfo(totalPage, sortBy, searchBy, searchValue);
      }
    } else {
      queryUEStaticListInfo(1, sortBy, searchBy, searchValue);
    }
  });

});

function QueryUEPerformanceInfo(){
  var $UEListtr = $("#UeListTbody tr");
  var UEList = [];

  $UEListtr.each(function (index) {
    var UEId = $(this).attr("id");
    if(UEId ==null || $.trim(UEId).length ==0){
      console.log("tr "+index+"has no UEID");
      return;
    }
    UEList.push(UEId);
  });

  $.ajax({
    type:"GET",
    url:"perform.do",
    data:{RecordNum:"1",UEIDList:UEList.join(",")},
    dataType:"json",
    success:function(Result){
      if(Result==null || Result.length==0){
        console.log("Performance Info is empty");
        return;
      }
      if(Result.result !="success"){
    	  console.log("查询失败,原因:"+Result.DataBody);
    	  return;
      }
      var UEParaList = Result.DataBody;
      
      for(var UEIndex in UEParaList){
        if(UEParaList.hasOwnProperty(UEIndex)){
          var UEPara = UEParaList[UEIndex];
          HandleUEPara(UEPara);

        }

      }
    },
    error:function(xhr){
      console.log("ajax响应错误，提示："+xhr.status+"  "+xhr.statusText);
    }
  });

}

function HandleUEPara(UEParaListStr){
  var UEID = null;
  var $tr = null;
  var disVal = null;
  var UEParaList = {};
  var basePath = $("#basePath").val();
  if(UEParaListStr.length==0){
	  return;
  }
  
  UEParaList = UEParaListStr[0];
  UEID = UEParaList["UEID"];
  if(UEID ==null){
	  console.log("UEID is null");
	  return;
  }
  $tr = $("#UeListTbody tr[id="+UEID+"]");
  
  
  for(var ParaName in UEParaList){
    if(UEParaList.hasOwnProperty(ParaName)){
      var ParaVal = UEParaList[ParaName];

      switch(ParaName){
        case "SignalStrength":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"dbm";
          }

          break;
        case "UEState":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            switch(ParaVal){
            case 1:
                disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm0.png" + '><span>&nbsp;</span><span>正常</span>';
                break;
              case 2:
            	disVal = '<img src=' + "" + basePath+ "/Manager/images/tnc/alarm-1.png" + '><span>&nbsp;</span><span>离线</span>'; 
                break;
              case 3:
                disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm1.png" + '><span>&nbsp;</span><span>故障</span>';
                break;
              case 4:
            	  disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm3.png" + '><span>&nbsp;</span><span>升级中</span>';
            	  break;
              case 5:
              	  disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm0.png" + '><span>&nbsp;</span><span>升级完成</span>';
              	  break;
              case 6:
            	  disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm1.png" + '><span>&nbsp;</span><span>升级失败</span>';
            	  break;
              case 10:
            	  disVal = '<img src=' + "" + basePath + "/Manager/images/tnc/alarm-1.png" + '><span>&nbsp;</span><span>删除中</span>';
            	  break;           
              default:
            	disVal = "未知状态";
            	break;

            }            
          }
          break;
        case "PCI":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal;
          }
          break;
        case "UlRate":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"bps";
          }
          break;
        case "DlRate":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"bps";
          }
          break;
        case "RSRP":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"dbm";
          }
          break;
        case "SNR":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"dbm";
          }
          break;
        case "RSSI":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{
            disVal = ParaVal+"dbm";
          }
          break;
        case "UpTime":
          if(ParaVal==null || ParaVal.length ==0){
            disVal =  "无";
          }
          else{        	  
            disVal = transSecToDHMS(parseInt(ParaVal));
          }
          break;

      }
      $tr.find("#"+ParaName).html(disVal);
    }
  }
}

function transSecToDHMS(time){
	var result = "";
	var day = 0;
	var hour = 0;
	var min = 0;
	var sec = 0;
	
	if (time >= 60){
		min = parseInt(time/60);
		sec = parseInt(time%60);
		if (min >= 60){
			hour = parseInt(min/60);
			min = parseInt(min%60);
			
			if (hour >= 24){
				day = parseInt(hour/24);
				hour = parseInt(hour%24);
			}
		}		
	}
	
	if (day > 0){
		result += day + "天"; 
	}
	
	if(hour > 0){
		result += hour + "时"; 
	}
	
	if(min > 0){
		result += min + "分";
	}
	
	if (sec >= 0){
		result += sec + "秒";
	}
	
	return result;
}

function queryUEStaticListInfo(targetPage, sortBy, searchBy, searchValue){
  window.location.href="QueryUE.do?currentPage="+targetPage+"&UECountPerPage=15";
}

function RebootUE(UEIDList){
  $.ajax({
    type:"GET",
    url:"reboot.do",
    data:{UEIDList:UEIDList.join(",")},
    dataType:"json",
    success:function(Result){
    	if(Result !==null){
    		alert("重启成功!");
    		/*for(var index in UEIDList){
    			var UEID = UEIDList[index];
    			$("tr[id="+UEID+"]").find("a[opetype=reboot]").html("重启成功").attr("disable",true);
    		}*/
    	}
    },
    error:function(xhr){
      console.log("重启UE ajax响应错误，提示："+xhr.status+"  "+xhr.statusText);
    }
  });

}

function DeleteUE(UEIDList){
  $.ajax({
    type:"GET",
    url:"Delete.do",
    data:{UEIDList:UEIDList.join(",")},
    dataType:"json",
    success:function(Result){
      if(Result !==null){
        alert("删除成功!");
        $("#fresh").click();
      }
    },
    error:function(xhr){
      console.log("删除UE ajax响应错误，提示："+xhr.status+"  "+xhr.statusText);
    }
  });
}