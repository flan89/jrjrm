<%@ page language="java" import="java.util.*,cn.djrj.jrjrm.model.Employee" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>金策略综合管理系统</title>
    
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="css/default/easyui.css">  
	<link rel="stylesheet" type="text/css" href="css/icon.css">  
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>  
 	<script type="text/javascript" src="js/jquery.easyui.min.js"></script> 
 	<script type="text/javascript" src="js/easyui-lang-zh_CN.js"></script> 
 	<script type="text/javascript" src="js/calendar/WdatePicker.js"></script> 
 	<script type="text/javascript" src="js/base64.js"></script> 
 	<script type="text/javascript" src="js/jquery.json-2.4.min.js"></script> 
 	
<script type="text/javascript">
	
	/*添加Tag选项卡页面*/
	function addTag(page){
		var title;
		if(page==="infolist"){
			title="客户信息列表";
		}else if(page==="loginloglist"){
			title="用户登录日志";
		}else if (page==="editer") {
			title="编辑用户信息";
		}else if(page==="rightsgrouplist"){
			title="创建权限组";
		}else if (page==="addnewcustom"){
			title="添加新客户";
		}
		
		var tagExist= $("#tag").tabs("exists",title);
		
		if(tagExist){
			$("#tag").tabs("select",title);
		}else{
			$("#tag").tabs("add",{  
			    title:title,  
			    href:"crm/show_"+page+".action",
			    closable:true  
			}); 
		}
	}
	
</script>
  </head>
  
  <body class="easyui-layout">
		
		<!-- 北部  -->
	    <div data-options="region:'north',title:'好策略综合管理系统',split:false" style="height:100px;">
	    	<div align="right" style="padding-right:50px; padding-top:17px; font-style:oblique; font-size: 35px;font-family:楷体;color:blue;">好策略资源管理系统    V 0.1.0</div>
	    </div>
	    <!-- 南部  -->
	    <div data-options="region:'south',title:'',split:false" style="height:30px; font-size: 15px;">
	    	欢迎：<s:property value="#session.employee.userName"/> 登录
	    </div>
	    
	    <!-- 西部  -->
	    <div data-options="region:'west',title:'菜单列表',split:false" style="width:200px;">
	    	<div id="aa" class="easyui-accordion" style="width:198px;">  
			    <div title="客户信息查询" data-options="selected:true" style="overflow:auto;padding:10px;" > 
			    	<h3><a href="javascript:void(0);" style="color:#0099FF;" onclick="addTag('infolist')">查询客户注册信息</a> </h3>
			    	<h3><a href="javascript:void(0);" style="color:#0099FF;" onclick="addTag('loginloglist')">查询客户登录日志</a> </h3>
			    	<h3><a href="javascript:void(0);" style="color:#0099FF;" onclick="addTag('addnewcustom')">添加新客户</a> </h3>
			    	<h3><a href="javascript:void(0);" style="color:#0099FF;" onclick="addTag('rightsgrouplist')">编辑用户权限组</a> </h3>
			    </div>  
			    <div title="登录查询" data-options="" style="padding:10px;"> 
			    	<h3><a href="javascript:void(0);" style="color:#0099FF;" onclick="addTag('loginloglist')">查询用户登录日志</a> </h3>
			    </div>  
			    <div title="系统管理">  
			    	<h3><a href="javascript:void(0);" style="padding:15px;color:#0099FF;" onclick="addTag('password')">修改登录密码</a></h3>
			    </div>  
			</div>
	    </div>  
	    
	    <!-- 中部  -->
	    <div data-options="region:'center',title:''" style="padding:0px;background:#eee;">
	    	<div id="tag" class="easyui-tabs" >  
			</div> 
	    </div> 
  </body>
</html>
