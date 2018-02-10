<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部署管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	var url;
	function searchDeploy(){
		$("#dg").datagrid('load',{
			"s_name":$("#s_name").val()
		});
	}
	function openDeployAddDiglog(){
		$("#dlg").dialog("open").dialog("setTitle","添加流程部署");
		url="${pageContext.request.contextPath}/deploy/addDeploy.action"
	}
	function saveDeploy(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统系统","部署成功！");
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统系统","部署失败，请联系管理员！");
					return;
				}
			}
		});
	}
	function closeDeployDialog(){
		$("#dlg").dialog("close");
	}
	function deleteDeploy(){
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
				$.post("${pageContext.request.contextPath}/deploy/delDeploy.action",{ids:ids},function(result){
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
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="部署管理" class="easyui-datagrid"
  fitColumns="true" pagination="true" rownumbers="true"
  url="${pageContext.request.contextPath}/deploy/deployPage.action" fit="true" toolbar="#tb">
 <thead>
 	<tr>
 		<th field="cb" checkbox="true" align="center"></th>
 		<th field="id" width="100" align="center">编号</th>
 		<th field="name" width="100" align="center">流程名称</th>
 		<th field="deploymentTime" width="100" align="center">部署时间</th>
 	</tr>
 </thead>
</table>
<div id="tb">
 <div>
	<a href="javascript:openDeployAddDiglog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
	<a href="javascript:deleteDeploy()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 </div>
 <div>
 	&nbsp;流程名称&nbsp;<input type="text" id="s_name" size="20" onkeydown="if(event.keyCode==13) searchDeploy()"/>
 	<a href="javascript:searchDeploy()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 </div>
</div>
<div id="dlg" class="easyui-dialog" style="width: 320px;height: 150px;padding: 10px 20px" closed="true" buttons="#dlg-buttons">
 
 	<form id="fm" method="post" enctype="multipart/form-data">
 		<input type="file" name="deployFile" class="easyui-validatebox" required="true"/>
 	</form>
 
</div>

<div id="dlg-buttons">
	<a href="javascript:saveDeploy()" class="easyui-linkbutton" iconCls="icon-ok">上传</a>
	<a href="javascript:closeDeployDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>