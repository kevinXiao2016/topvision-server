<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>日志配置</title>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css">
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
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
var tree = null;
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
function buildTree() {
    tree = new dhtmlXTreeObject("logsTree", "200", "90%", 0);
    tree.setImagePath("../js/dhtmlx/tree/imgs/vista/");
    tree.loadXML("loadAllLogs.tv", loadLogConfigCallback);
    /*tree.setOnClickHandler(function(id) {
        location.href="/admin/showLog.tv?m="+id;
    });*/
}
function loadLogConfigCallback() {
}
function doOnload() {
    buildTree();
}
</script>
<body onload="doOnload();">
    <center>
        <h2>日志配置</h2>
    </center>
    <div><s:property value="m" escape="false"/></div>
    <div id="logsTree"></div>
</body>
</html>
