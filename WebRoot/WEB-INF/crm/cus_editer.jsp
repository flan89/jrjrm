<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
	
	var id=<%=request.getParameter("id")%>;
	var uName="<%=request.getParameter("userName")%>";
	var ddate=new Date();
	var editIndex = undefined;
	
	$(function(){
		
		$("#cus_detail").propertygrid({ 
		    url: "crm/cus_detail.action?id="+id,  
		    showGroup: false,
		    title:"用户详细信息",
		    scrollbarSize: 0,
		    columns:[[
				        {field:"name",title:"字段",width:100,align:'center'},
				        {field:"value",title:"值",width:100,align:'center'}
				  ]]
		});
		
		$("#cus_rights").datagrid({  
			striped: true,
		    url:"crm/cus_rights.action?userName="+uName,
		    pagination:true,
		    singleSelect:false,
		    title:"权限列表",
		    loadMsg:"正在加载，请稍候···",
		    pageSize:20,
		    onClickRow:onClickRow,
		    columns:[[
		        {field:"ck",checkbox:"true"},  
		        {field:"userName",title:"用户名",width:100,align:"center"},  
		        {field:"info",title:"权限",width:"100",align:'center',editor:"numberbox"},  
		        {field:"startTime",title:"开始时间",width:100,align:"center", formatter:function (val, row) {
		        	if(val.time==undefined){
		        		return val;
		        	}else{
		        		ddate.setTime(val.time);
			        	return ddate.getFullYear()+"-"+(ddate.getMonth()+1)+"-"+ddate.getDate();
		        	}
		        	
		        }},
		        {field:"endTime",title:"结束时间",width:105,align:'center',editor:"datebox"}
		    ]]  
		});
	});
	
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($("#cus_rights").datagrid('validateRow', editIndex)){
			$("#cus_rights").datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$("#cus_rights").datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$("#cus_rights").datagrid('selectRow', editIndex);
			}
		}
	}
	
	/*添加新权限*/
	function addRights(){
		var today=new Date();
		today=today.getFullYear()+"-"+(today.getMonth()+1)+"-"+today.getDate();
		var row = {
				  userName:uName,  
				  info:"0",  
				  startTime:today, 
				  endTime:today,
				  modified_at:today,
				  created_at:null
				};  
		$("#cus_rights").datagrid("appendRow",row);
	}
	
	/*删除权限*/
	function removeRights(){
		
		var rows=$("#cus_rights").datagrid("getSelections");
		if(rows.length==0){
			$.messager.alert("提示框","请选择要删除的权限记录!");  
			return false;
		}
		
		$.messager.confirm("警告框","确认删除选中的权限？",function(r){
		    if (r){ 
		    	var infos=new String();
		    	for(var i=0;i<rows.length;i++){
					infos+=rows[i].info+",";
				}
		    	infos=infos.substring(0,infos.length-1);
				$.post("crm/cus_removerights.action",{userName:uName, info:infos},function(rdata){
					if(rdata=="success"){
						for(var i=0;i<rows.length;i++){
							var index=$("#cus_rights").datagrid("getRowIndex",rows[i]);
							$("#cus_rights").datagrid("deleteRow",index);
						}
						$.messager.alert("提示框","删除成功！"); 
					}else {
						$.messager.alert("提示框","删除失败！");
					}
				 });
		    }  
		}); 
	}
	
	/*保存用户基本信息*/
	function saveInfo(){
		$.messager.confirm("确认框","确定保存修改后的用户基本信息？",function(r){  
		    if (r){
		    	var data=$("#cus_detail").propertygrid("getRows");
				var eidterJson={userName:data[0].value,passWord:data[1].value,telphone:data[2].value,startTime:data[3].value,
						endTime:data[4].value,userType:data[5].value,paymoney:data[6].value,flag:data[7].value};
				data=$.toJSON(eidterJson);
				data=$.base64({data:data,type:0}); 
				$.post("crm/cus_saveinfo.action",{id:id,infoJson:data},function(rdata){
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
	/*保存用户权限信息*/
	function saveRights(){
		$.messager.confirm("确认框","确定保存修改后的用户权限？",function(r){  
		    if (r){
				$("#cus_rights").datagrid('endEdit', editIndex);
				editIndex = undefined;
				var rightsDate=$("#cus_rights").datagrid("getData");
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

<div id="toolbar" style="padding-left: 180px; padding-top: 6px;">  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save"  onclick="javascript:saveInfo()">保存编辑</a>  
    <a href="javascript:void(0);" style="padding-left: 150px" class="easyui-linkbutton" iconCls="icon-remove"  onclick="javascript:removeRights()">删除</a>  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add"  onclick="javascript:addRights()">添加</a>  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save"  onclick="javascript:saveRights()">保存权限</a>  
</div>

<div style="height: 577px;">
<!-- 用户详细信息列表 --> 
<div style=" float: left;">
	<table id="cus_detail" style="width:300px"></table>    
</div>

<!-- 用户权限列表 -->
<div style=" float: left; margin-left: 10px;">
	<table id="cus_rights" style="width:440px"></table>    
</div>
</div>






