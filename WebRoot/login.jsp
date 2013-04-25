<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>好策略综合管理系统</title>

<link href="css/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript">

function myLogin(){
	document.loginForm.submit();
}

function changeCode(){
	var myDate=new Date();
	var img=document.getElementById("vcode");
	img.src="varifyCode.action?time="+myDate.getTime();
}

function BindEnter(obj){
    //使用document.getElementById获取到按钮对象
    var button = document.getElementById("login");
    if(obj.keyCode == 13){
        button.click();
        obj.returnValue = false;
    }
}
</script>

</head>

<body onkeydown="BindEnter(event)">

<div class="wrapper">
	<div class="box">
        <div class="login_rj_sm_box">
            <div class="login_rj_shiz"></div>
            <div class="login_rj_tt"></div>
            <div class="login_rj_wz"></div>
            
           <form name="loginForm" action="index.action" method="post">
	            <div class="login_rj_loginbox">
	                <ul>
	                    <li>
	                    	<span class="span_1 fl">用户名：</span>
	                    	<input name="userName" class="lotext_tbx fl" type="text" />
	                    </li>
	                    <li class="margt_13">
	                    	<span class="span_1 fl">密&nbsp;&nbsp;码：</span>
	                    	<input name="passWord" class="lotext_tbx fl" type="password" />
	                    </li>
	                    <li class="margt_13">
	                    	<span class="span_1 fl">验证码：</span>
	                    	<input name="varifyCode" class="lotext_yzm fl" type="text" />
	                    	<img id="vcode" class="magl_10" src="varifyCode.action" />
	                    	<a href="javascript:void(0);" class="magl_10" onclick="changeCode()">看不清，换一张！</a>
	                    </li>
	                    <li class="margt_5">
	                    	<span class="span_2">
	                    	<input name="" class="margt_2" type="checkbox" value="" />&nbsp;&nbsp;记住密码</span>
	                    	<span class="span_3">忘记密码？在这里找回</span>
	                    </li>
	                    <li class="margt_5">
	                    	<span class="span_4">
	                    		<input name="login" id="login" class="lobutt_bom1" type="button" onclick="myLogin();"/>
	                    	</span>
		                    <span class="span_5">
		                    	<input name="register" class="lobutt_bom2" type="button" />
		                    </span>
	                   </li>
	                </ul>
	            </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>