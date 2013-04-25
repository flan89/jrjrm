<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
var ddate=new Date();
	var editIndex = undefined;
	$(function(){
		
		$("#cus_rightsgroup").datagrid({  
			striped: true,
		    url:"crm/cus_rightsgrouplist.action",
		    pagination:true,
		    singleSelect:true,
		    title:"权限列表",
		    loadMsg:"正在加载，请稍候···",
		    pageSize:10,
		    onClickRow:groupOnClickRow,
		    columns:[[
		        {field:"ck",checkbox:"true"},  
		        {field:"empId",title:"所属用户ID",width:80,align:'center'},  
		        {field:"name",title:"权限组名称",width:250,align:'center',editor:"text"},  
		        {field:"createDate",title:"权限组创建时间",width:120,align:"center", formatter:function (val, row) {
		        	if(val.time==undefined){
		        		return val;
		        	}else{
		        		ddate.setTime(val.time);
			        	return ddate.getFullYear()+"-"+(ddate.getMonth()+1)+"-"+ddate.getDate();
		        	}
		        	
		        }},
		        {field:"describe",title:"权限组描述",width:460,align:'center',editor:"text"},
		        {field:"action",title:"操作",width:275,align:'center',formatter:function (val, row) {
		        	var actionStr="<a href='javascript:void(0);' onclick=\"cusRightsAddTag('editergroup','"+row.id+"','"+row.name+"')\">添加/编辑 权限</a>";
		        	return actionStr;
		        }}
		    ]]  
		});
	});
	
	function cusRightsAddTag(page,id,name){
		var title;
		if(page==="editergroup"){
			title="编辑权限组";
		}else if (page==="addrights"){
			title="添加新权限";
		}
		
		var tagExist= $("#tag").tabs("exists",title);
		
		if(tagExist){
			$("#tag").tabs("select",title);
		}else{
			$("#tag").tabs("add",{  
			    title:title,  
			    href:"crm/show_"+page+".action?groupId="+id+"&name="+name,
			    closable:true  
			}); 
		}
		
	}
	
	function groupEndEditing(){
		if (editIndex == undefined){return true}
		if ($("#cus_rightsgroup").datagrid('validateRow', editIndex)){
			$("#cus_rightsgroup").datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function groupOnClickRow(index){
		if (editIndex != index){
			if (groupEndEditing()){
				$("#cus_rightsgroup").datagrid('selectRow', index).datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$("#cus_rightsgroup").datagrid('selectRow', editIndex);
			}
		}
	}
	
	/*添加新权限组*/
	function addGroup(){
		
		$empId=$("#empId").html();
		var today=new Date();
		today=today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
		var row = {
				  id:0,
				  empId:$empId,
				  name:"",  
				  createDate:today, 
				  describe:"",
				  action:"<a href='javascript:void(0);' onclick=\"cusRightsAddTag('editergroup','','')\">添加/编辑 权限</a>"
				};  
		$("#cus_rightsgroup").datagrid("appendRow",row);
	}
	
	/*删除权限组*/
	function removeGroup(){
		
		var row=$("#cus_rightsgroup").datagrid("getSelected");
		var index=$("#cus_rightsgroup").datagrid("getRowIndex",row);
		if(index==-1){
			$.messager.alert("提示框","请选择要删除的权限组记录!");  
			return false;
		}
		$.messager.confirm("警告框","确认删除该权限组？",function(r){
		    if (r){  
		    	$("#cus_rightsgroup").datagrid("deleteRow",index);
				$.post("crm/cus_removegroupinfo.action",{groupId:row.id},function(rdata){
					if(rdata=="success"){
						$.messager.alert("提示框","删除成功！"); 
					}else {
						$.messager.alert("提示框","删除失败！");
					}
				});
		    }  
		}); 
	}
	/*保存权限组信息*/
	function saveGroup(){
		var row=$("#cus_rightsgroup").datagrid("getSelected");
		var index=$("#cus_rightsgroup").datagrid("getRowIndex",row);
		if(index==-1){
			$.messager.alert("提示框","请选择要保存的权限组记录!");  
			return false;
		}
		$.messager.confirm("警告框","确认保存该权限组？",function(r){
		    if (r){
		    	$("#cus_rightsgroup").datagrid('endEdit', editIndex);
		    	editIndex = undefined;
		    	var row=$("#cus_rightsgroup").datagrid("getSelected");
		    	var jsonData=$.toJSON(row);
		    	jsonData=$.base64({data:jsonData,type:0});
		    	
				$.post("crm/cus_savegroupinfo.action",{groupId:row.id,ediJson:jsonData},function(rdata){
					if(rdata=="success"){
						$.messager.alert("提示框","保存成功！"); 
						$("#cus_rightsgroup").datagrid('reload');
					}else {
						$.messager.alert("提示框","保存失败！");
					}
				});
		    }  
		});
	}
	
</script>

<!-- 权限组列表 --> 
<table id="cus_rightsgroup" style="width:auto"></table>    

<div id="toolbar" style="padding-left : 40px; padding-top: 6px;"> 
	<span id="empId" style="display: none"><s:property value="#session.employee.id"/></span> 
	<span style="color: red">提示：添加权限组时，请先保存后在进行 [添加/编辑] 操作 </span>
	<span style="padding-left: 500px;">
		<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  onclick="javascript:addGroup()">添加组</a>  
	    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove"  onclick="javascript:removeGroup()">删除组</a>  
	    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save"  onclick="javascript:saveGroup()">保存</a>  
	</span>
</div> 
