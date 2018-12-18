<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<title>HSQL操作</title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
</Zeta:Loader>
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
		function run(url) {
			$.ajax({
				url : url,
				cache : false,
				dataType : 'text',
				data : {
					m : $("#sql").val()
				},
				success : function(response) {
					$("#result").html(response);
				},
				error : function(e, status) {
					$("#result").html(e.message);
				}
			});
		}
	</script>
	<body>
		<center>
			<h2>HSQL操作</h2>
		</center>
		<div>
			<h2>执行HSQL语句</h2>
			<p>例如清空HSQL：<br />
			#ENGINE 1;<br />
			drop table CMPOLLRESULT201608230927</p>
			<h2>查询HSQL语句</h2>
			<p>例如查询所有表：<br />
			#ENGINE 1;<br />
			SELECT * FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='PUBLIC'</p>
			SQL:<br />
			<textarea id="sql" title="SQL" value="" rows="10" cols="180"></textarea>
			<input class="button" type="submit" value="查询"
				onclick="return run('/admin/queryHSql.tv');" /> <input
				class="button" type="submit" value="更新"
				onclick="return run('/admin/executeHSql.tv');" />
		</div>
		<div id="result"></div>
	</body>
</Zeta:HTML>
