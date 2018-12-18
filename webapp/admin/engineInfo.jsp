<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Admin</title>
</head>
<style type="text/css">
<!--
caption {
    padding: 0 0 5px 0;
    width: 700px;
    font: italic 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
    text-align: right;
}

table.datagrid {
    border-collapse: collapse;background:#fff;
}

table.datagrid th {
    text-align: left;
    background: #9cf;
    padding: 3px;
    border: 1px #333 solid;
}

table.datagrid td {
    padding: 3px;
    border: none;
    border: 1px #333 solid;
}

tr:hover,tr.hover {
    background: #9cf;
}

.tb1,.tb7 {
    background: #c9d7f1;
}
-->
</style>
<script type="text/javascript">
    function add_event(tr) {
        tr.onmouseover = function() {
            tr.className += ' hover';
        };
        tr.onmouseout = function() {
            tr.className = tr.className.replace(' hover', '');
        };
    }
    function stripe(table) {
        var trs = table.getElementsByTagName("tr");
        for ( var i = 1; i < trs.length; i++) {
            var tr = trs[i];
            add_event(tr);
        }
    }
    window.onload = function() {
        var tables = document.getElementsByTagName('table');
        for ( var i = 0; i < tables.length; i++) {
            var table = tables[i];
            if (table.className == 'datagrid') {
                stripe(tables[i]);
            }
        }
    }
</script>
<body>
    <center>
        <h2><a name='home'>Engine信息</a></h2>
    </center>
	<div>
		<table class="datagrid">
			<caption>Engine列表</caption>
			<tr>
				<th>ID</th>
				<th>IP</th>
				<th>Port</th>
				<th>Name</th>
			</tr>
			<s:iterator value="engines">
				<tr>
					<td><s:property value="id"/></td>
					<td><s:property value="ip"/></td>
					<td><s:property value="port"/></td>
					<td><a href='/admin/engineInfo.tv?m=<s:property value="name" />'><s:property value="name"/></a></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<div><s:property value="m" escape="false"/></div>
</body>
</html>
