<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>服务器文件管理</title>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
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
	border-collapse: collapse;
	background: #fff;
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

tr:hover, tr.hover {
	background: #9cf;
}

.tb1, .tb7 {
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
		for (var i = 1; i < trs.length; i++) {
			var tr = trs[i];
			add_event(tr);
		}
	}
	window.onload = function() {
		var tables = document.getElementsByTagName('table');
		for (var i = 0; i < tables.length; i++) {
			var table = tables[i];
			if (table.className == 'datagrid') {
				stripe(tables[i]);
			}
		}
	}
</script>
<body>
	<center>
		<h2>服务器文件管理</h2>
	</center>
	<div>
        <s:form action="/admin/postUpload.tv" method="POST" enctype="multipart/form-data">
            <s:textfield name="m" value="" label="Path:"/>
            <s:file name="upload" label="File"/>
            <s:submit />
        </s:form>
    </div>
    <div>
		<table class="datagrid">
			<caption>文件列表</caption>
			<tr>
				<th>日志</th>
				<th>大小</th>
				<th>日期</th>
				<th>操作</th>
			</tr>
			<s:iterator value="ds">
				<tr>
					<td><s:if test="l < 15728640">
							<a href='/admin/listFiles.tv?m=<s:property value="path" />'>
						</s:if> <s:property value="name" /> <s:if test="l < 15728640">
							</a>
						</s:if></td>
					<td><s:property value="length" /></td>
					<td><s:property value="createDate" /></td>
					<td><a
						href='/admin/downloadFile.tv?m=<s:property value="path" />'>下载</a>
						<a href='/admin/deleteFile.tv?m=<s:property value="path" />'>删除</a></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<div>
		<s:property value="m" escape="false" />
	</div>
</body>
</html>
