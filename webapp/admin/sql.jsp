<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
<title>SQL操作</title>
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
            <h2>SQL操作</h2>
        </center>
        <div>
            <h2>执行SQL语句</h2>
            <p>例如更新CC名称：update Entity A set name = (select topCcmtsSysName from CmcAttribute B where B.cmcId =
                A.entityId) where name like 'CC8800A%'</p>
            <h2>查询SQL语句</h2>
            <p>例如查询版本：select * from VersionControl</p>
            SQL:<br />
            <textarea id="sql" title="SQL" value="" rows="10" cols="180"></textarea>
            <input class="button" type="submit" value="查询" onclick="return run('/admin/querySql.tv');" /> <input
                class="button" type="submit" value="更新" onclick="return run('/admin/executeSql.tv');" />
        </div>
        <div id="result"></div>
    </body>
</Zeta:HTML>
