<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">

var groupId="<%=request.getParameter("groupId")%>";
var groupName="<%=request.getParameter("name")%>";
var editIndex = undefined;

$(function(){
	
	$("#cus_editergroup").datagrid({  
		striped: true,
	    url:"crm/cus_showrightsofgroup.action?groupId="+groupId,
	    pagination:true,
	    singleSelect:false,
	    title:"[ "+groupName+" ] 中的权限",
	    loadMsg:"正在加载，请稍候···",
	    pageSize:20,
	    onClickRow:rightsOnClickRow,
	    columns:[[
			{field:"ck",checkbox:"true"},
	        {field:"rightsName",title:"权限名",width:"265",align:"center"},  
	        {field:"rightsValue",title:"权限值",width:"100",align:"center"},
	        {field:"effectDate",title:"有效期（天）",width:100,align:"center",editor:"numberbox"}
	    ]]  
	});
	
	$("#cus_allrights").datagrid({  
		striped: true,
	    url:"crm/cus_allrights.action",
	    pagination:true,
	    singleSelect:false,
	    title:"系统所有权限",
	    loadMsg:"正在加载，请稍候···",
	    pageSize:20,
	    columns:[[
			{field:"ck",checkbox:"true"},
	        {field:"rightsName",title:"权限名",width:"265",align:"center"},  
	        {field:"rightsValue",title:"权限值",width:"100",align:"center"},
	        {field:"effectDate",title:"建议试用时间（天）",width:120,align:"center",editor:"numberbox"}
	    ]]  
	});
});

function rightsEndEditing(){
	if (editIndex == undefined){return true}
	if ($("#cus_editergroup").datagrid('validateRow', editIndex)){
		$("#cus_editergroup").datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function rightsOnClickRow(index){
	if (editIndex != index){
		if (rightsEndEditing()){
			$("#cus_editergroup").datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$("#cus_editergroup").datagrid('selectRow', editIndex);
		}
	}
}
/*添加权限到权限组*/
function addRightsToGroup(){
	var $rows=$("#cus_allrights").datagrid("getSelections");
	for(var i=0;i<$rows.length;i++){
		$("#cus_editergroup").datagrid("appendRow",$rows[i]);
	}
}
/*移除权限组中权限*/
function removeRightsToGroup(){
	var $rows=$("#cus_editergroup").datagrid("getSelections");
	if($rows.length<=0){
		$.messager.alert("提示框","请选择要删除的权限！"); 
		return false;
	}
	
	$.messager.confirm("警告框","确认保存该权限？",function(r){
	    if (r){
	    	
	    	$("#cus_editergroup").datagrid('endEdit', editIndex);
	    	editIndex = undefined;
	    	
	    	var values=new String();
	    	for(var i=0;i<$rows.length;i++){
	    		values+=$rows[i].rightsValue+",";
	    		var index=$("#cus_editergroup").datagrid("getRowIndex",$rows[i]);
	    		$("#cus_editergroup").datagrid("deleteRow",index);
	    	}
	    	values=values.substring(0, values.length-1);
	    	values=$.base64({data:values,type:0});
			$.post("crm/cus_removerightsofgroup.action",{ids:values,groupId:groupId},function(rdata){
				if(rdata=="success"){
					$.messager.alert("提示框","删除成功！"); 
				}else {
					$.messager.alert("提示框","删除失败！");
				}
			});
	    }  
	});
	
	
}
/*保存权限组中编辑好的权限*/
function saveRightsToGroup(){
	
	$("#cus_editergroup").datagrid('endEdit', editIndex);
	editIndex = undefined;
	var $all=$("#cus_editergroup").datagrid("getData");
	var jsonData=$.toJSON($all);
	jsonData=$.base64({data:jsonData,type:0}); 
	//alert(jsonData);
	$.post("crm/cus_saverightsgroup.action",{groupId:groupId,ediJson:jsonData},function(rdata){
		if(rdata==="success"){
			$.messager.alert("提示框","保存成功！"); 
		}else {
			$.messager.alert("提示框","保存失败！"); 
		}
	});
}

</script>

<div id="toolbar" style="padding-left: 0px; padding-top: 6px;">  
    <a href="javascript:void(0);" style="padding-left: 50px"class="easyui-linkbutton" iconCls="icon-save"  onclick="javascript:saveRightsToGroup()">保存</a>  
    <a href="javascript:void(0);" style="padding-left: 170px"class="easyui-linkbutton" iconCls="icon-remove"  onclick="javascript:removeRightsToGroup()">删除权限</a>  
    <a href="javascript:void(0);" style="padding-left: 170px" class="easyui-linkbutton" iconCls="icon-add"  onclick="javascript:addRightsToGroup()">添加权限</a>  
</div>
<!-- 批量授权列表 -->
<div style=" float: left; width: 500px;">
	<table id="cus_editergroup" style="width:500px;"></table>
</div>
<div style=" float: left; width: 500px; margin-left: 10px;">
	<table id="cus_allrights" style="width:520px;"></table>
</div>
<div style="clear:both;"></div>













