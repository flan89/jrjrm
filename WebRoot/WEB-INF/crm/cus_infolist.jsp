<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">

var mydate=new Date();

$(function(){
	$("#cus_list").datagrid({
		iconCls:"icon-save",//表格图标
		striped: true,
	    url:"crm/cus_list.action",
	    pagination:true,
	    singleSelect:true,
	    loadMsg:"正在加载，请稍候···",
	    pageSize:10,
	    columns:[[
	        {field:"ck",checkbox:"true"},  
	        {field:"id",title:"ID",width:70,align:'center'},  
	        {field:"userName",title:"用户名",width:"150",align:'center'},  
	        {field:"telephone",title:"电话",width:150,align:'center'},  
	        {field:"userType",title:"用户类型",width:150,align:'center',formatter:function (val, row) {
	        	var type;
	        	if(val=="0"){
	        		type="注册用户";
	        	}else if (val=="1") {
					type="认证用户";
				}else if(val=="2"){
					type="付费用户";
				}
	        	return type;
	        }},  
	        {field:"startTime",title:"注册时间",width:150,align:'center',formatter:function (val, row) {
	        	mydate.setTime(val.time);
	        	return mydate.getFullYear()+"-"+(mydate.getMonth()+1)+"-"+mydate.getDate();
	        }},  
	        {field:"endTime",title:"到期时间",width:150,align:'center',formatter:function (val, row) {
	        	mydate.setTime(val.time);
	        	return mydate.getFullYear()+"-"+(mydate.getMonth()+1)+"-"+mydate.getDate();
	        }},  
	        {field:"flag",title:"当前状态",width:100,align:'center',formatter:function (val, row) {
	        	var type;
	        	if(row.flag ==0){
	        		type="启用";
	        	}else if (row.flag==1) {
	        		type="停用";
				}else{
					type="启用";
				}
	        	return type;
	        }},  
	        {field:"action",title:"操作",width:260,align:'center',formatter:function (val, row) {
	        	var actionStr;
	        	if(row.flag == 1){
	        		actionStr="<a href='javascript:void(0)' onclick='cusFreeze("+row.id+",0)'>启用</a>    <a href='javascript:void(0)' onclick=\"cusAddTag('editer','"+row.id+"','"+row.userName+"')\">编辑</a>"+
	        		"    <a href='javascript:void(0);' onclick=\"cusAddTag('rights','"+row.id+"','"+row.userName+"')\">授权</a>";
	        	}else{
	        		actionStr="<a href='javascript:void(0)' onclick='cusFreeze("+row.id+",1)'>停用</a>    <a href='javascript:void(0)' onclick=\"cusAddTag('editer','"+row.id+"','"+row.userName+"')\">编辑</a>"+
	        		"    <a href='javascript:void(0);' onclick=\"cusAddTag('rights','"+row.id+"','"+row.userName+"')\">授权</a>";
	        	}
	        	return actionStr;
	        }}
	    ]]  
	});
});

/*启用 停用操作*/
function cusFreeze(rid,state){
	
	$.messager.confirm("确认框","确定  启用/停用 该用户？",function(sure){ 
	    if (sure){  
	    	$.post("crm/cus_freeze.action",{id:rid,flag:state},function(data){
	    		if(data=="success"){
	    			$("#cus_list").datagrid("reload");
	    			$.messager.alert("提示框","操作成功！"); 
	    		}else{
	    			$.messager.alert("提示框","操作失败，请刷新页面重试！");
	    		}
	    	});
	    }else{
	    	return false;  
	    }
	});  
	
}

/*处理从 用户列表页打开新选项卡标签*/
function cusAddTag(tagtype,rid,uname){
	
	if(tagtype==="editer"){
		var tagExist= $("#tag").tabs("exists","编辑用户信息");
		
		if(tagExist){
			$("#tag").tabs("select","编辑用户信息");
			$.messager.alert("提示框","请先 保存/关闭 当前操作！");
		}else{
			$("#tag").tabs("add",{  
			    title:"编辑用户信息",  
			    href:"crm/show_editer.action?id="+rid+"&userName="+uname,
			    closable:true  
			}); 
		}
		 
	}else if (tagtype==="rights") {
		
		var tagExist= $("#tag").tabs("exists","批量授权");
		if(tagExist){
			$("#tag").tabs("select","批量授权");
			$.messager.alert("提示框","请先 保存/关闭 当前操作！");
		}else{
			$("#tag").tabs("add",{
			    title:"批量授权",
			    href:"crm/show_rights.action?id="+rid+"&userName="+uname,
			    closable:true  
			}); 
		}
	}
	
}

/*搜索操作*/
function cusSearch(tableid){ 
    $("#"+tableid).datagrid("load",{
    	userName: $("#userName").val(),  
    	telephone: $("#telephone").val(),
    	userType: $("#userType").val(),  
    	flag: $("#flag").val(),  
    	rsTime: $("#rsTime").val(),  
    	reTime: $("#reTime").val(),  
    	esTime: $("#esTime").val(), 
    	eeTime: $("#eeTime").val()  
    });  
}


</script>

<!-- 搜索工具条 -->
 <div id="cus_search" style="padding:3px; margin: 15px 0;">  
 	<table cellpadding="0" cellspacing="0" border=0 style=" margin-left:20px; line-height: 30px; font-size: 12px;">
 		<tr>
 			<td>
 				用户名: <input type="text" id="userName"  size="15" name="userName">
   				&nbsp;&nbsp;
   				电话号: <input type="text" size="15" id="telephone" name="telephone"><br/>
     用户类型: <select id="userType">
		     <option value="">所有用户</option>
		     <option value="0">注册用户</option>
		     <option value="1">认证用户</option>
		     <option value="2">付费用户</option>
		    </select> 
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    用户状态: <select id="flag">
	    	<option value="">所有</option>
	    	<option value="0">启用</option>
	    	<option value="1">停用</option>
	      </select>
 			</td>
 			<td style="padding-left:20px;">
 				  注册时间 : <input type="text" id="rsTime" name="rsTime" onclick="WdatePicker()" readonly="readonly" size="15"> 至 
 				  			<input type="text" size="15" id="reTime" name="reTime" onclick="WdatePicker()" readonly="readonly"><br/>
   				  到期时间 : <input type="text" id="esTime" name="esTime" onclick="WdatePicker()" size="15" readonly="readonly"> 至 
   				  			<input type="text" id="eeTime" size="15" name="eeTime" onclick="WdatePicker()" readonly="readonly"><br/>
 			</td>
 			<td><a href="javascript:void(0);" style=" float: right; margin-left: 20px;" class="easyui-linkbutton" onclick="cusSearch('cus_list')">查询</a></td>
 		</tr>
 		
 	</table>
    
</div> 

<!-- table列表 --> 
<table id="cus_list"></table>














