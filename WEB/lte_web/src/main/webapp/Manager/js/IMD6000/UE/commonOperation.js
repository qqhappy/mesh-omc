/**
 * Created by user on 16-4-28.
 */

$(function(){
  
	WindowSizeCalc();
  $.extend($.validator.messages,{
    required:"必填字段",
    number:"请输入合法的数字",
    digits:"只能输入整数",
    minlength: $.validator.format("请输入一个长度最少是{0}的字符串"),
    maxlength: $.validator.format("请输入一个长度最大是{0}的字符串"),
    range:$.validator.format("请输入介于{0}到{1}之间的数")

  });

//  $.validator.addMethod("isPeriod",function(value,element){
//		var isPeriod = /^[0-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/;
//	    return this.optional(element)||isPeriod.test(value);
//	  },"请输入0~65535的整数");
  
  $.validator.addMethod("isLatitude",function(value,element){
	  	var isLatitude = /^[-]?(\d|[1-8]\d)(\.\d{1,6})?$|(-90$)|(90$)/;
	    return this.optional(element)||isLatitude.test(value);
	  },"请输入-90~90之间的数，步长为0.000001");
  
  //经度
  $.validator.addMethod("isLongitude",function(value,element){
	  	var isLongitude = /^[-]?(\d|([1-9]\d)|(1[0-7]\d))(\.\d{1,6})?$|(-180$)|(180$)/;
	    return this.optional(element)||isLongitude.test(value);
	  },"请输入-180~180之间的数，步长为0.000001");

  $("#form_add").validate({
    rules:{
      IMSI:{
        required:true,
        digits:true,
        minlength:15,
        maxlength:15
      },
      Location:{
        required:true,
        minlength:2,
        maxlength:10
      },
      Latitude:{
        required:true,
        isLatitude:true
      },
      Longitude:{
        required:true,
        isLongitude:true
      },
      DataReportPeriod:{
        required:true,
        digits:true,
        range:[0,65535]
      },
      ControlReportPeriod:{
        required:true,
        digits:true,
        range:[0,65535]
      }
    },
    messages:{
      IMSI:{
        minlength:"请输入15位数字"
      }
    },
    submitHandler:function(form){
      var submitMap = {};

      $("tr[id!='justForView']").each(function(e){
        var Key = $(this).attr("id");
        var Val = $("[name="+Key+"]").val();
        submitMap[Key] = Val;
      });

      $("body").load("Config.do",{submitMap:JSON.stringify(submitMap)});
      DisableAllInput();
    }
  });


  $("#cancel").click(function(){
    turnToUEQueryPage();
  });

});

function turnToUEQueryPage(){
  window.parent.mainFrame.location.href = "lte/UE/QueryUE.do";
}

function DisableAllInput(){
  $("input").attr("disabled",true);
  $("#justForView").html($("<p style='color: #A60000'></p>").text("正在提交...."));
}

function WindowSizeCalc(){
	// 窗口高度
	var winhei=$(window).height();
	// 窗口宽度
	var winWidth=$(window).width();
	//
	var leftWidth = $(".left_nav").width();
	// 蓝条高度
 	var pathHei=$(".path_nav").height();
	// 左部菜单栏高�?
	$("#leftDiv").css({"height":winhei-pathHei-31,"overflow":"auto"});
	$("#treeDemo").css("height",winhei-pathHei-41);
	$("#treeDemo2").css("height",winhei-pathHei-282);
	$(".left_nav").css("height",winhei-pathHei);
	// welcome页高�?
	$(".pageContent").css("height",winhei-pathHei-40);
	// 右部数据高度
	$(".enbPage").css({"height":winhei-pathHei-10,"overflow":"auto"});
	$(".ltePage").css({"height":winhei-pathHei-10,"overflow":"auto"});
	$(".alarmPage").css({"height":winhei-pathHei-40,"overflow":"auto"});
	// 新增配置页面高度
	$(".add_div").css("height",winhei-pathHei);	
	//基站列表右部数据宽度
	if($(".contentDivObj").width() < 1190){
		$(".contentDivObj").css({"width":1190});
	}
	if($(".asset_div").width() < 1190){
		$(".asset_div").css({"width":1190});
	}
	if($(".historyAsset_div").width() < 1600){
		$(".historyAsset_div").css({"width":1600});
	}
	// 新增配置页面宽度�?
	if($(".ad_Input").width() < 1140){
		$(".ad_Input").css({"width":1140,"overflow":"hidden"});	
	}
	if($(".bb_condition_ul").width() < 1140){
		$(".bb_condition_ul").css({"width":1140,"overflow":"hidden"});	
	}
	if($(".bb_content_div").width() < 1140){
		$(".bb_content_div").css({"width":1140,"overflow":"hidden"});	
	}
	window.onresize = function(){
		// 窗口高度
		var winhei=$(window).height();
		// 窗口宽度
		var winWidth=$(window).width();
		//
		var leftWidth = $(".left_nav").width();
		// 蓝条高度
	 	var pathHei=$(".path_nav").height();
		// 左部菜单栏高�?
		$("#leftDiv").css({"height":winhei-pathHei-31,"overflow":"auto"});
		$("#treeDemo").css("height",winhei-pathHei-41);
		$("#treeDemo2").css("height",winhei-pathHei-282);
		$(".left_nav").css("height",winhei-pathHei);
		// welcome页高�?
		$(".pageContent").css("height",winhei-pathHei-40);
		// 右部数据高度
		$(".enbPage").css({"height":winhei-pathHei-10,"overflow":"auto"});
		$(".ltePage").css({"height":winhei-pathHei-10,"overflow":"auto"});
		$(".alarmPage").css({"height":winhei-pathHei-40,"overflow":"auto"});
		// 新增配置页面高度
		$(".add_div").css("height",winhei-pathHei);
		//右部数据宽度
		if($(".contentDivObj").width() < 1190){
			$(".contentDivObj").css({"width":1190,"overflow":"hidden"});
		}
		if($(".enbPage").width() > 1190){
			$(".contentDivObj").css({"width":"100%","overflow":"hidden"});
		}
		if($(".asset_div").width() < 1190){
			$(".asset_div").css({"width":1190});
		}
		if($(".enbPage").width() > 1190){
			$(".asset_div").css({"width":"100%","overflow":"hidden"});
		}
		if($(".historyAsset_div").width() < 1600){
			$(".historyAsset_div").css({"width":1600});
		}
		if($(".enbPage").width() > 1600){
			$(".asset_div").css({"width":"100%","overflow":"hidden"});
		}
		// 新增配置页面宽度�?
		if($(".ad_Input").width() < 1140){
			$(".ad_Input").css({"width":1140,"overflow":"hidden"});	
		}
		if($(".bb_condition_ul").width() < 1140){
			$(".bb_condition_ul").css({"width":1140,"overflow":"hidden"});	
		}
		if($(".bb_content_div").width() < 1140){
			$(".bb_content_div").css({"width":1140,"overflow":"hidden"});	
		}
		if($("#form_add").width() > 1235){
			$(".ad_Input").css("width",$("#form_add").width() - 95);	
		}
	};

	
}