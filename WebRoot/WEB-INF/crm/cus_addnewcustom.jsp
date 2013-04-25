<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<script type="text/javascript">
   
   $(function(){
	   
	   $("#cus_newCustom").propertygrid({ 
		    url: "crm/cus_showaddpage.action",
		    showGroup: true,
		    title:"",
		    scrollbarSize: 0,
		    columns:[[
				        {field:"name",title:"字段",width:100,align:'center'},
				        {field:"value",title:"值",width:100,align:'center'}
				  ]]
		});
	   
   });
   
   function saveUser(){
	   $.messager.confirm("确认框","确定保存修改后的内容？",function(r){  
		    if (r){
				var data=$("#cus_newCustom").propertygrid("getRows");
				var userJson={userName:data[0].value,passWord:data[1].value,startTime:data[2].value,
						endTime:data[3].value,userType:data[4].value,payMoney:data[5].value,
						flag:data[6].value,clientFrom:data[7].value,telephone:data[8].value,
						sex:data[9].value,payTime:data[10].value};
				
				data=$.toJSON(userJson);
				data=$.base64({data:data,type:0}); 
				$.post("crm/cus_addcustom.action",{infoJson : data},function(rdata){
					if(rdata==="success"){
						$.messager.alert("提示框","添加成功！");
					}else {
						$.messager.alert("提示框","添加失败！");
					}
				});
				
		    }else{
		    	return false;
		    }
		});
   }
   
</script>
 

<!-- 用户注册信息列表 -->
<div id="toolbar" style="padding-left: 200px; padding-top: 6px;">  
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" onclick="saveUser()">保存编辑</a>  
</div> 
<div style="height: 577px">
<table id="cus_newCustom" style="width:350px;"></table>   
</div>




