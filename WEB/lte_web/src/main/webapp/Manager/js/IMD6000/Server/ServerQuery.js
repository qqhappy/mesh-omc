/**
 * Created by user on 16-4-27.
 */
$(function () {

  $(".enbTable").delegate("input[name=Modify][id!=DataServer]","click", function () {

    var ModelName = $(this).attr("id");

    window.parent.mainFrame.location.href = "TurnToConfigPage.do?ServerName="+ModelName;

  });

  	//全选
	$("#DataServercheckfather").live("click",function(){
		$("[name=DataServercheckson]:checkbox").attr("checked",this.checked);
	});
	$("[name=DataServercheckson]:checkbox").live("click",function(){
		var flag=true;
		$("[name=DataServercheckson]:checkbox").each(function(){
			if(!this.checked){
				flag=false;
			}
		});
		$("#DataServercheckfather").attr("checked",flag);
	});
  
  $("#AddDataServer").click(function (e) {

    window.parent.mainFrame.location.href = "TurnToConfigPage.do?ServerName=AddDataServer";
  });

  $("#DelDataServer").click(function (e) {
    var $checked = $("input[name=DataServercheckson]:checked");
    var ServerIDList = [];
    if($checked.length==0){
    	alert("请选择要删除的数据服务器");
    	return;
    }
    $checked.each(function(index){
      var ServerID = $(this).parents("tr").attr("id");
      if(ServerID==null ||ServerID.length==0){
        console.log("ServerID is null,index="+index);
        return;
      }
      ServerIDList.push(ServerID);
    });
    if(confirm("确定删除?")){
      window.parent.mainFrame.location.href = "DelServer.do?DataServerList="+JSON.stringify(ServerIDList);
    }

  });

  $("#DataServer").click(function (e) {
    var $checked = $("input[name=DataServercheckson]:checked");
    var ServerIDList = [];
    if($checked.length > 1){
      alert("不允许修改多个数据服务器");
      return;
    }
    else if($checked.length ==0){
	  alert("请选择要修改的数据服务器");
	  return;
    }
    
    ServerIDList.push($checked.parents("tr").attr("id"));
    window.parent.mainFrame.location.href = "TurnToConfigPage.do?ServerName=DataServer&ServerIDList="+JSON.stringify(ServerIDList);
  });

  $("#fresh").click(function () {
	  window.parent.mainFrame.location.href = "QueryServer.do";
  });


});
