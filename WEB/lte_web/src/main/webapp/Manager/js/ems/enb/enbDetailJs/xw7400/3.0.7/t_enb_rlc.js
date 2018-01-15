$(function(){
	//表单校验
	var isNum=/^(-)?\d+$/;
	$("#submit_add").click(function(){
		var index = 0;
		if(!(isNum.test($("#u8CfgIdx").val()) && $("#u8CfgIdx").val()<=255  && $("#u8CfgIdx").val()>=0)){
			$("#u8CfgIdxError").text("/* 请输入0~255之间的整数 */");
			index++;
		}else{
			$("#u8CfgIdxError").text("");
		}
		var para = "u8CfgIdx"+"="+$("#u8CfgIdx").val()+";"
				+"u8SNType"+"="+$("input[name='u8SNType']:checked").val()+";"
				+"u8ReorderingTimer"+"="+$("#u8ReorderingTimer").val()+";"
				+"u8StatusProhibtTimer"+"="+$("#u8StatusProhibtTimer").val()+";"
				+"u8PollRetransmtTimer"+"="+$("#u8PollRetransmtTimer").val()+";"
				+"u8PollPdu"+"="+$("#u8PollPdu").val()+";"
				+"u8PollByte"+"="+$("#u8PollByte").val()+";"
				+"u8MaxRetxThrd"+"="+$("#u8MaxRetxThrd").val();
		$("#parameters").val(para);
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
							$("#form_add").submit();	
		}
		
	});
	//内存值转为显示值
	$("#t_enb_rlc1 tr").each(function(index){
		//
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(1)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(1)").text("5");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(1)").text("10");
		}
		//					   
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("0");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("5");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("10");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("15");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("20");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("25");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("30");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 7){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("35");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 8){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("40");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 9){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("45");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 10){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("50");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 11){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("55");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 12){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("60");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 13){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("65");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 14){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("70");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 15){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("75");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 16){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("80");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 17){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("85");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 18){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("90");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 19){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("95");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 20){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("100");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 21){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("110");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 22){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("120");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 23){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("130");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 24){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("140");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 25){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("150");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 26){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("160");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 27){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("170");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 28){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("180");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text() == 29){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("190");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(2)").text("200");
		}	
		//					   
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("0");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("5");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("10");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("15");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("20");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("25");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("30");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 7){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("35");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 8){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("40");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 9){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("45");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 10){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("50");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 11){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("55");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 12){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("60");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 13){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("65");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 14){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("70");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 15){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("75");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 16){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("80");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 17){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("85");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 18){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("90");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 19){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("95");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 20){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("100");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 21){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("105");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 22){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("110");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 23){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("115");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 24){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("120");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 25){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("125");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 26){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("130");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 27){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("135");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 28){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("140");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 29){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("145");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 30){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("150");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 31){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("155");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 32){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("160");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 33){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("165");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 34){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("170");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 35){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("175");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 36){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("180");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 37){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("185");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 38){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("190");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 39){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("195");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 40){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("200");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 41){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("205");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 42){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("210");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 43){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("215");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 44){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("220");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 45){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("225");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 46){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("230");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 47){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("235");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 48){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("240");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 49){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("245");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 50){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("250");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 51){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("300");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 52){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("350");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 53){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("400");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text() == 54){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("450");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(3)").text("500");
		}
		//					   
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("5");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("10");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("15");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("20");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("25");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("30");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("35");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 7){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("40");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 8){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("45");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 9){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("50");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 10){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("55");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 11){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("60");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 12){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("65");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 13){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("70");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 14){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("75");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 15){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("80");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 16){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("85");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 17){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("90");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 18){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("95");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 19){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("100");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 20){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("105");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 21){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("110");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 22){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("115");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 23){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("120");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 24){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("125");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 25){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("130");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 26){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("135");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 27){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("140");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 28){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("145");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 29){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("150");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 30){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("155");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 31){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("160");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 32){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("165");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 33){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("170");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 34){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("175");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 35){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("180");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 36){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("185");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 37){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("190");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 38){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("195");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 39){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("200");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 40){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("205");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 41){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("210");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 42){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("215");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 43){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("220");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 44){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("225");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 45){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("230");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 46){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("235");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 47){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("240");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 48){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("245");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 49){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("250");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 50){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("300");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 51){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("350");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 52){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("400");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text() == 53){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("450");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(4)").text("500");
		}
		//
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("4");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("8");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("16");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("32");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("64");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("128");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("256");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(5)").text("无穷大");
		}
		//
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("25");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("50");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("75");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("100");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("125");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("250");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("375");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 7){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("500");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 8){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("750");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 9){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("1000");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 10){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("1250");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 11){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("1500");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 12){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("2000");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text() == 13){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("3000");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(6)").text("无穷大");
		}
		//
		if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 0){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("1");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 1){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("2");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 2){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("3");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 3){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("4");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 4){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("6");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 5){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("8");
		}else if($("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text() == 6){
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("16");
		}else{
			$("#t_enb_rlc1 tr:eq("+index+") td:eq(7)").text("32");
		}
	});	
	//刷新按钮
	$("#fresh").click(function(){
		var enbVersion = $("#enbVersion").val();
		var moId = $("#moId").val();
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7";
	});
	//全选
	$("#checkfather").live("click",function(){
		$("[name=checkson]:checkbox").attr("checked",this.checked);
	});
	$("[name=checkson]:checkbox").live("click",function(){
		var flag=true;
		$("[name=checkson]:checkbox").each(function(){
			if(!this.checked){
				flag=false;
			}
		});
		$("#checkfather").attr("checked",flag);
	});
	//跳转到配置
	$("#t_enb_rlc1 tr").each(function(index){
		$("#t_enb_rlc1 tr:eq("+index+") td:eq(8)").click(function(){
			var u8CfgIdx = $("#t_enb_rlc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			var enbVersion = $("#enbVersion").val();
			window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7&parameters="+para+"";
		});					   
	});	
	//跳转到配置
	$("#t_enb_rlc1 tr").each(function(index){
		if(index != 0){
			$("#t_enb_rlc1 tr:eq("+index+") th:eq(1)").click(function(){
				var u8CfgIdx = $("#t_enb_rlc1 tr:eq("+index+") td:eq(0)").text();
				var para = "u8CfgIdx"+"="+u8CfgIdx;
				var basePath = $("#basePath").val();
				var moId = $("#moId").val();
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/turnConfigEnbBiz.do?moId="+moId+"&enbVersion="+enbVersion+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7&parameters="+para+"";
			});	
		}
						   
	});	
	//删除
	$("#t_enb_rlc1 tr").each(function(index){
		$("#t_enb_rlc1 tr:eq("+index+") td:eq(9)").click(function(){
			var u8CfgIdx = $("#t_enb_rlc1 tr:eq("+index+") td:eq(0)").text();
			var para = "u8CfgIdx"+"="+u8CfgIdx;
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除该条记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=delete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7&parameters="+para+"";
			}
		});					   
	});	
	//批量删除
	$("#delete").click(function(){
		var str=[];
		$("#t_enb_rlc1 input[type=checkbox]").each(function(index){
			if($("#t_enb_rlc1 input[type=checkbox]:eq("+index+")").attr("checked")){
				var temp = $("#t_enb_rlc1 tr:eq("+index+") td:eq(0)").text();
				str.push(temp);
			}
		});	
		for(var i=0;i<str.length;i++){
			if(str[i]== "" || str[i]== null){
				str.splice(i,1);
			}
		}
		var para = "";
		for(var i=0;i<str.length;i++){
			var s = "u8CfgIdx"+"="+str[i]+";" ;
			para += s;
		}
		if(str.length < 1){
			alert("您并未选中任何记录...");
		}else{
			var basePath = $("#basePath").val();
			var moId = $("#moId").val();
			if(confirm("确定要删除所有选择的记录?")){
				var enbVersion = $("#enbVersion").val();
				window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&operType=multiDelete&moId="+moId+""+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7&parameters="+para+"";
			}
		}	
	});
	//取消按钮
	$("#cancel").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7";
	});
	$("#cancelx").click(function(){
		var enbVersion = $("#enbVersion").val();
		var basePath = $("#basePath").val();
		var moId = $("#moId").val();
		window.location.href=""+basePath+"/lte/queryEnbBiz.do?enbVersion="+enbVersion+"&moId="+moId+"&operType=select"+"&browseTime="+getBrowseTime()+"&tableName=T_ENB_RLC-3.0.7";
	});
});