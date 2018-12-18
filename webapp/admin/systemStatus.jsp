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
	background:#fff;
    border-collapse: collapse;
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
p{ font-family:Menlo,Consolas,"Courier New",monospace,"宋体"}
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
	function restart() {
		var sure = window
				.confirm("Are you sure to update or restart the server?");
		if (sure) {
			document.update.submit();
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
        <h2><a name='home'>状态信息</a></h2>
    </center>
    <div></div>
    <div>
        <s:property value="m" escape="false" />
    </div>
</body>
</html>
