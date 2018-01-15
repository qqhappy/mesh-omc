$(function(){
	//表单校验
	var isNum=/^\d+$/;
	var isName=/^[a-zA-Z0-9_]+$/;
	var isPassword=/^[a-zA-Z0-9]+$/;
	$("#submit_add").click(function(){
		//拿到时间
		var date = $("#timepicker").val();
		var str = date.split("-");
		var s1 = str[0];
		var s2 = str[1];
		var s3 = str[2];
		var date1 = s1+s2+s3;
		$("#timepickerq").val(date1);		
		
		var index = 0;
		var userName=$("#userName").val()+"";
		var nameLength = userName.length;
		if(!isName.test(userName)){
			$("#userNameError").text("/* 用户名为字母或下划线 */");
			index++;
		}else if(nameLength > 20){
			index++;
			$("#userNameError").text("/* 用户名长度应不大于20 */");
		}else{
			$("#userNameError").text("");
		}
		
		var password = $("#passWord").val();
		var confirmPassWord = $("#confirmPassWord").val();
		var passwordLength = password.length;
		
		if(!isPassword.test(password)){
			$("#passWordError").text("/* 密码必须为字母或数字 */");
			index++;
		} else if(passwordLength <1){
			index++;
			$("#passWordError").text("/* 请输入密码 */");
		} else if(passwordLength >20){
			index++;
			$("#passWordError").text("/* 密码长度应不大于20 */");
		} else{
			$("#passWordError").text("");
		}
		if(password != confirmPassWord){
			index++;
			$("#confirmPassWordError").text("/* 密码不一致 */");
		}else{
			$("#confirmPassWordError").text("");
		}
		if($("input[name='ispermanentuser']:checked").val() == 1){
			$("#timepickerq").attr("name","validtime");
		}else{
			$("#timepickerq").removeAttr("name");
		}
		if(index==0){
			$("input[name='browseTime']").val(getBrowseTime());
			$("#form_add").submit();	
		}
		
	});
	//取消按钮
	$("#cancel").click(function(){
		var basePath = $("#basePath").val();
		window.location.href=""+basePath+"/lte/queryAllUser.do?browseTime="+getBrowseTime();
	});		   
});