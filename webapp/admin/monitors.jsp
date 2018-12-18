<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Admin</title>
</head>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
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
    function refresh() {
        window.location.reload();
    }
    function clearMonitors() {
        $.ajax({
            url : '/admin/clearMonitors.tv',
            cache : false,
            dataType : 'text',
            success : function(response) {
                window.location.reload();
            },
            error : function(e, status) {
                alert(e);
            }
        });
    }
    function restartMonitors() {
        $.ajax({
            url : '/admin/startMonitors.tv',
            cache : false,
            dataType : 'text',
            success : function(response) {
                window.location.reload();
            },
            error : function(e, status) {
                alert(e);
            }
        });
    }
</script>
<body>
    <center>
        <h2>
            <a name='home'>性能统计信息</a>
        </h2>
    </center>
    <div class="putTree" id="putTree">
        <div style="width: 100%; overflow: hidden;">
            <ul id="tree" class="filetree">
                <li><span class="icoG1"><a
                        href="javascript:;" class="linkBtn"
                        onclick="refresh()" name="refresh">刷新</a></span></li>
                <li><span class="icoG1"><a
                        href="javascript:;" class="linkBtn"
                        onclick="clearMonitors()" name="clearMonitors">清除Engine性能采集</a></span></li>
                <li><span class="icoG1"><a
                        href="javascript:;" class="linkBtn"
                        onclick="restartMonitors()"
                        name="restartMonitors">重新分配性能采集</a></span></li>
            </ul>
        </div>
    </div>
    <div>
        <table class="datagrid">
            <caption>性能统计表</caption>
            <tr>
                <th>EngineId</th>
                <th>Engine状态</th>
                <th>文件缓存</th>
                <th>文件缓存数量</th>
                <th>分配数</th>
                <th>任务数</th>
                <th>任务队列</th>
                <th>处理线程池</th>
            </tr>
            <s:iterator value="datas">
                <tr>
                    <td><s:property value="engineId" /></td>
                    <td><s:property value="status" /></td>
                    <td><s:property value="fileCache" /></td>
                    <td><s:property value="fileCacheSize" /></td>
                    <td><s:property value="monitors" /></td>
                    <td><s:property value="schedulerSize" /></td>
                    <td><s:property value="schedulerStatus" /></td>
                    <td><s:property value="threadpool" /></td>
                </tr>
            </s:iterator>
        </table>
    </div>
</body>
</html>
