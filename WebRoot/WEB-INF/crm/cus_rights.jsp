<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">

var uName="<%=request.getParameter("userName")%>";

$(function(){
	
	$("#sys_rights").datagrid({ 
		striped: true,
	    url:"crm/cus_allrights.action",
	    pagination:true,
	    singleSelect:false,
	    title:"软件所有权限",
	    loadMsg:"正在加载，请稍候···",
	    pageSize:20,
	    columns:[[
			{field:"ck",checkbox:"true"},
	        {field:"rightsName",title:"权限名",width:"265",align:"center"},  
	        {field:"rightsValue",title:"权限值",width:100,align:"center"},
	        {field:"effectDate",title:"建议试用时间（天）",width:110,align:"center"}
	    ]]  
	});
	
	$("#cus_addrights").datagrid({  
		striped: true,
	    url:"crm/cus_rights.action?userName="+uName,
	    pagination:true,
	    singleSelect:false,
	    title:"["+uName+"] 的权限",
	    loadMsg:"正在加载，请稍候···",
	    pageSize:20,
	    onClickRow:cusRightsOnClickRow,
	    columns:[[
	        {field:"ck",checkbox:"true"},  
	        {field:"userName",title:"用户名",width:"120",align:"center"},  
	        {field:"info",title:"权限",width:100,align:'center',editor:"numberbox"},  
	        {field:"startTime",title:"开始时间",width:120,align:"center", formatter:function (val, row) {
	        	if(val.time==undefined){
	        		return val;
	        	}else{
	        		ddate.setTime(val.time);
		        	return ddate.getFullYear()+"-"+(ddate.getMonth()+1)+"-"+ddate.getDate();
	        	}
	        	
	        }},
	        {field:"endTime",title:"结束时间",width:120,align:'center',editor:"datebox"}
	    ]]  
	});
	
	/*显示权限组下拉列表*/
	$.post("crm/cus_groupname.action",{empId:1},function(rdata){
		var groupName=eval(rdata);
		for(var i=0;i<groupName.length;i++){
			$("#groupList").append("<option value='"+groupName[i]+"'>"+groupName[i]+"</option>");
		}
	});
	
});

function cusRightsEndEditing(){
	if (editIndex == undefined){return true}
	if ($("#cus_addrights").datagrid('validateRow', editIndex)){
		$("#cus_addrights").datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function cusRightsOnClickRow(index){
	if (editIndex != index){
		if (cusRightsEndEditing()){
			$("#cus_addrights").datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$("#cus_addrights").datagrid('selectRow', editIndex);
		}
	}
}

/*授权操作 */
function toAuthority(){
	
	$.messager.confirm("确认框","确定为该用户授权？",function(sure){ 
	    if (sure){  
	    	var groupName=$("#groupList").val();
	    	$.post("crm/cus_giverights.action",{userName:uName,groupName:groupName},function(rdata){
	    		if(rdata==="success"){
	    			$.messager.alert("提示框","授权成功！"); 
	    			$("#cus_addrights").datagrid("reload");
	    		}else {
	    			$.messager.alert("提示框","授权失败！"); 
				}
	    	});
	    	
	    }else{
	    	return false;  
	    }
	});
}

/*添加选中权限*/
function addSelectRights(){
	
	var today=new Date();
	var sToday=today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
	var rows=$("#sys_rights").datagrid("getSelections");
	
	for(var i=0;i<rows.length;i++){
		var tDate=new Date();
		tDate.setTime(tDate.getTime()+rows[i].effectDate*24*60*60*1000);
		endDay=tDate.getFullYear()+"-"+(tDate.getMonth()+1)+"-"+tDate.getDate();
		var row={userName:uName,
				 info:rows[i].rightsValue,
				 startTime:sToday,
				 endTime:endDay,
				 modified_at:today,
				  created_at:null
				};
		$("#cus_addrights").datagrid("appendRow",row);
	}
}

/*保存用户权限信息*/
function saveCusRights(){
	$.messager.confirm("确认框","确定保存修改后的用户权限？",function(r){  
	    if (r){
			$("#cus_addrights").datagrid('endEdit', editIndex);
			editIndex = undefined;
			var rightsDate=$("#cus_addrights").datagrid("getData");
			rightsDate=$.toJSON(rightsDate);
			rightsDate=$.base64({data:rightsDate,type:0});
			
			$.post("crm/cus_saverights.action",{userName:uName, rightsJson:rightsDate},function(rdata){
				if(rdata==="success"){
					$.messager.alert("提示框","保存成功！"); 
				}else {
					$.messager.alert("提示框","保存失败！"); 
				}
			});
	    }else{
	    	return false;
	    }
	}); 
}

</script>


<div id="toolbar" style="padding-top: 6px; padding-bottom: 3px; padding-left: 300px;">  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" onclick="javascript:saveCusRights()">保存权限</a>  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="javascript:addSelectRights()"  style="padding-left: 100px;">添加选中权限</a>  
   
	<span style="padding-left: 15px;">
	    直接授予 <select name="group" id="groupList"></select> 组权限<a href="javascript:void(0);" class="easyui-linkbutton" onclick="toAuthority()" >确定</a>
    </span>
</div>

<!-- 用户所有拥有的权限列表 -->
<div style=" float: left; ">
	<table id="cus_addrights" style="width:495px"></table>    
</div>
<!-- 软件产品所有的权限 -->
<div style=" float: left;margin-left: 10px;">
	<table id="sys_rights" style="width:510px;"></table>
</div>













