//固定的提示信息----中文
var constantInfoMap_zh = {
	"no_selected_item_info":"您并未选中任何记录...",
	"confirm_deleteAll_info":"确定要删除所有选择的记录?",
	"confirm_delete_info":"确定要删除该条记录?",
	"no_item_exist_info":"/* 没有可用的选项 */",
	"vlan_confirm":"进行此操作后需要复位基站,确定进行此操作?"
};

//固定的提示信息----英文
var constantInfoMap_en = {
	"no_selected_item_info":"no record has been selected...",
	"confirm_deleteAll_info":"confirm to delete all the selected records?",
	"confirm_delete_info":"confirm to delete the selected record?",
	"no_item_exist_info":"/* no items can be selected */",
	"vlan_confirm":"you must restart the eNB later,confirm to do this ?"
};

//最终选择的固定提示信息的map
var constantInfoMap = null;

//当前语言环境
var i18n_type = "zh";

$(function(){
	//获取当前语言环境
	i18n_type = "zh";
	if(i18n_type == "zh"){
		constantInfoMap = constantInfoMap_zh;
	}
	if(i18n_type == "en"){
		constantInfoMap = constantInfoMap_en;
	}//待补充
	
});

//带有参数的信息国际化
//type 提示类别，获取哪条提示消息 
//argments 参数，如"请输入1~10之间的整数"中的1和10
function dynamicInfo(type,argment){
	var info = "";
	if(i18n_type == "zh"){
		//type=10000,input输入数字校验,校验整数
		if(type == 10000){
			info =  "/* 请输入"+argment.min+"~"+argment.max+"之间的整数 */";
		}
		//type=10001,input输入数字校验,校验步长为0.1的小数
		if(type == 10001){
			info =  "/* 请输入"+argment.min+"~"+argment.max+"之间的小数,步长0.1 */";
		}
		//type=10002,input输入数字校验,校验步长为0.01的小数
		if(type == 10002){
			info =  "/* 请输入"+argment.min+"~"+argment.max+"之间的小数,步长0.01 */";
		}
	}
	if(i18n_type == "en"){
		//type=10000,input输入数字校验,校验整数
		if(type == 10000){
			info =  "/* please input an integer between "+argment.min+" and "+argment.max+" */";
		}
		//type=10001,input输入数字校验,校验步长为0.1的小数
		if(type == 10001){
			info =  "/* please input an integer between "+argment.min+" and "+argment.max+",step is 0.1 */";
		}
		//type=10002,input输入数字校验,校验步长为0.01的小数
		if(type == 10002){
			info =  "/* please input an integer between "+argment.min+" and "+argment.max+",step is 0.01 */";
		}
	}//待补充
	return info;
}
//方法顾名思义
function generateArgments_i18n_num(max,min){
	var object = new Object();
	object.max = max;
	object.min = min;
	return object;
}