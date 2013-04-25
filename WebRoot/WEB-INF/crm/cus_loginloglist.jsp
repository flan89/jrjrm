<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
   
<script type="text/javascript">
var mydate=new Date();
$(function(){
	
	$("#cus_loginloglist").datagrid({  
		striped: true,
	    url:"crm/cus_showloginlog.action",
	    pagination:true,
	    singleSelect:true,
	    title:"",
	    loadMsg:"正在加载，请稍候···",
	    pageSize:10,
	    columns:[[
	        {field:"id",title:"id",width:"100",align:"center"},  
	        {field:"userName",title:"用户名",width:"100",align:"center"},  
	        {field:"operateType",title:"登陆|登出",width:100,align:"center", formatter:function (val, row) {
	        	if(val == true){
	        		return "登录";
	        	}else{
	        		return "登出";
	        	}
	        }},
	        {field:"operateTime",title:"发生时间",width:100,align:"center", formatter:function (val, row) {
	        	if(val.time==undefined){
	        		return val;
	        	}else{
	        		mydate.setTime(val.time);
		        	return mydate.getFullYear()+"-"+(mydate.getMonth()+1)+"-"+mydate.getDate();
	        	}
	        	
	        }},
	        {field:"ip",title:"ip",width:100,align:"center"},
	        {field:"mac",title:"mac",width:150,align:"center"}
	    ]]  
	});
});

/*查询今日登录用户（不重复显示）*/
function loginNum(){
	$.post("crm/cus_showloginnum.action",function(data){
		$("#loginnum").attr("value",data);
	});
}


/*搜索操作*/
function loginSearch(){
	
    $("#cus_loginloglist").datagrid("load",{
    	userName: $("#tUName").val(),  
    	inOut: $("#oType").val(),  
    	osTime: $("#rsTime").val(),  
    	oeTime: $("#reTime").val()
    });
}

</script>

<!-- 搜索工具条 -->
<div id="cus_search" style="padding:10px;padding-left: 15px;">  
    用户名: <input type="text" id="tUName" size="15">
    &nbsp;&nbsp;
    操作类型 : <select id="oType" name="inOut">
		     <option value="">所有</option>
		     <option value="true">登录</option>
		     <option value="false">登出</option>
		    </select> 
     &nbsp;&nbsp;
    操纵时间: <input type="text" id="rsTime" name="rsTime" onclick="WdatePicker()" readonly="readonly" size="15"> 至
    	 <input type="text" id="reTime" name="reTime" onclick="WdatePicker()" readonly="readonly" size="15">
    	 &nbsp;
    	<a href="javascript:void(0);" class="easyui-linkbutton" onclick="loginSearch()">查询</a>
    &nbsp;&nbsp;&nbsp;
    <input type="text" id="loginnum"  readonly="readonly" size="5">
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="loginNum()">今日登录数</a>
</div> 
<!-- 登录日志列表 -->
<table id="cus_loginloglist" style="width:auto;"></table>




