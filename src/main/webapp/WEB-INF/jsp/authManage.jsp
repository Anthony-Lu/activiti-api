<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>权限管理</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">

        function searchUser() {
            $("#dg").datagrid('load', {
                "id": $("#s_id").val()
            });
        }

        function openAuthDiglog() {
            var selectRows = $("#dg").datagrid("getSelections");
            if (selectRows.length != 1) {
                $.messager.alert("系统提示", "请选择一条要设置的用户！");
                return;
            }
            var row = selectRows[0];
            $("#dlg").dialog("open").dialog("setTitle", "设置用户权限");
            loadAllGroups(); // 加载所有角色
            setRoles(selectRows[0].id);
            url = "${pageContext.request.contextPath}/memberShip/updateMemberShip.action?userId=" + selectRows[0].id;
        }

        function setRoles(userId) {
            $.post("${pageContext.request.contextPath}/group/findGroupByUserId.action", {userId: userId}, function (result) {
                var groups = result.groups;
                var groupsArr = groups.split(",");
                for (var i = 0; i < groupsArr.length; i++) {
                    $("[value=" + groupsArr[i] + "]:checkbox").attr("checked", true);
                }
            }, "json");
        }

        function loadAllGroups() {
            $.post("${pageContext.request.contextPath}/group/listAllGroups.action", {}, function (result) {
                var groupList = result.groupList;
                $("#groupsList").empty();
                for (var key in groupList) {
                    var cbStr = '<input type="checkbox" name="groupId" value="' + groupList[key].id + '" />' + '<font>' + groupList[key].name + '</font>' + '&nbsp;';
                    $("#groupsList").append(cbStr);
                }
            }, "json");
        }


        function closeAuthDialog() {
            $("#dlg").dialog("close");
        }

        function saveAuth() {
            var obj = document.getElementsByName("groupId");
            var s = '';
            for (var i = 0; i < obj.length; i++) {
                if (obj[i].checked) {
                    s += obj[i].value + ',';
                }
            }
            $.post(url, {groupsIds: s.substring(0, s.length - 1)}, function (result) {
                if (result.success) {
                    $.messager.alert("系统提示", "提交成功！");
                    closeAuthDialog();
                    $("#dg").datagrid("reload");
                } else {
                    $.messager.alert("系统提示", "提交失败，请联系管理员！");
                }
            }, "json");
        }

    </script>
</head>
<body style="margin: 1px">
<table id="dg" title="权限管理" class="easyui-datagrid"
       fitColumns="true" pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/user/listWithGroups.action" fit="true" toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="80" align="center">权限名</th>
        <th field="password" width="80" align="center">密码</th>
        <th field="firstName" width="50" align="center">姓</th>
        <th field="lastName" width="50" align="center">名</th>
        <th field="email" width="100" align="center">邮箱</th>
        <th field="groups" width="150" align="center">拥有权限</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openAuthDiglog()" class="easyui-linkbutton" iconCls="icon-power" plain="true">用户权限设置</a>
    </div>
    <div>
        &nbsp;用户名&nbsp;<input type="text" id="s_id" size="20" onkeydown="if(event.keyCode==13) searchUser()"/>
        <a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog" style="width: 450px;height: 200px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">

    <div id="groupsList" style="padding: 10px"></div>

</div>

<div id="dlg-buttons">
    <a href="javascript:saveAuth()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
    <a href="javascript:closeAuthDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>