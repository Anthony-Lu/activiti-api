<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	function searchUser(){
		$("#dg").datagrid('load',{
			"id":$("#s_id").val()
		});
	}
	
	function deleteUser(){
		var selectRows=$("#dg").datagrid("getSelections");
		if(selectRows.length==0){
			$.messager.alert("系统提示","请选择要删除的数据！");
			return;
		}
		var strIds=[];
		for(var i=0;i<selectRows.length;i++){
			strIds.push(selectRows[i].id);
		}
		var ids=strIds.join(",");
		$.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectRows.length+"</font>条数据吗?",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/user/deleteUser.action",{ids:ids},function(result){
					if(result.success){
						$.messager.alert("系统提示","数据已经成功删除！");
						$("#dg").datagrid("reload");
					}else{
						$.messager.alert("系统提示","数据删除失败，请联系管理员！");
					}
				},"json");
			}
		});
	}
	
	
	function openUserAddDiglog(){
		$("#dlg").dialog("open").dialog("setTitle","添加用户信息");
		$("#flag").val(1);
		$("#id").attr("readonly",false);
	}
	
	function openUserModifyDiglog(){
		var selectRows=$("#dg").datagrid("getSelections");
		if(selectRows.length!=1){
			$.messager.alert("系统提示","请选择一条要编辑的数据！");
			return;
		}
		var row=selectRows[0];
		$("#dlg").dialog("open").dialog("setTitle","编辑用户信息");
		$("#fm").form("load",row);
		$("#flag").val(2);
		$("#id").attr("readonly",true);
	}
	
	
	function checkData(){
		if($("#id").val()==''){
			$.messager.alert("系统系统","请输入用户名！");
			$("#id").focus();
			return;
		}
		var flag=$("#flag").val();
		if(flag==1){
			$.post("${pageContext.request.contextPath}/user/existUserName.do",{userName:$("#id").val()},function(result){
				if(result.exist){
					$.messager.alert("系统系统","该用户名已存在，请更换下！");
					$("#id").focus();
				}else{
					saveUser();
				}
			},"json");
		}else{
			saveUser();
		}
	}
	
	function saveUser(){
		$("#fm").form("submit",{
			url:'${pageContext.request.contextPath}/user/userSave.action',
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","保存成功！");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统系统","保存失败！");
					return;
				}
			}
		});
	}
	
	function resetValue(){
		$("#id").val("");
		$("#password").val("");
		$("#firstName").val("");
		$("#lastName").val("");
		$("#email").val("");
	}
	
	function closeUserDialog(){
		$("#dlg").dialog("close");
		resetValue();
	}

</script>
</head>
<body style="margin: 1px">
<table id="dg" title="用户管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/user/userPage.action" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="cb" checkbox="true" align="center"></th>
 		<th field="id" width="80" align="center">用户名</th>
 		<th field="password" width="80" align="center">密码</th>
 		<th field="firstName" width="50" align="center">姓</th>
 		<th field="lastName" width="50" align="center">名</th>
 		<th field="email" width="100" align="center">邮箱</th>
 	</tr>
 </thead>
</table>
<div id="tb">
 <div>
	<a href="javascript:openUserAddDiglog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
	<a href="javascript:openUserModifyDiglog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
	<a href="javascript:deleteUser()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 </div>
 <div>
 	&nbsp;用户名&nbsp;<input type="text" id="s_id" size="20" onkeydown="if(event.keyCode==13) searchUser()"/>
 	<a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 </div>
</div>

<div id="dlg" class="easyui-dialog" style="width: 620px;height: 250px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
 
 	<form id="fm" method="post">
 		<table cellpadding="8px">
 			<tr>
 				<td>用户名：</td>
 				<td>
 					<input type="text" id="id" name="id" class="easyui-validatebox" required="true"/>
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>密码：</td>
 				<td>
 					<input type="text" id="password" name="password" class="easyui-validatebox" required="true"/>
 				</td>
 			</tr>
 			<tr>
 				<td>姓：</td>
 				<td>
 					<input type="text" id="firstName" name="firstName" class="easyui-validatebox" required="true"/>
 				</td>
 				<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
 				<td>名：</td>
 				<td>
 					<input type="text" id="lastName" name="lastName" class="easyui-validatebox" required="true"/>
 				</td>
 			</tr>
 			<tr>
 				<td>邮箱：</td>
 				<td colspan="4">
 					<input type="text" style="width: 200px" id="email" name="email" class="easyui-validatebox" validType="email" required="true"/>
 					<input type="hidden" id="flag" name="flag"/>
 				</td>
 			</tr>
 		</table>
 	</form>
 
</div>

<div id="dlg-buttons">
	<a href="javascript:checkData()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	<a href="javascript:closeUserDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>