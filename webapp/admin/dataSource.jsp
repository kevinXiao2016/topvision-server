<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<title><fmt:message bundle="${sys}" key="sys.showVersionNum" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />

<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>

<script type="text/javascript">
// onload事件替代函数
$(function(){
    var server_ds_data=${serverDataSource};
    var engine_ds_data=${engineDataSource};
    var data1=${dataSourceInfo};
    var server_ds_store,engine_ds_store,serverDsGrid,engineDsGrid,store1,grid1;
    server_ds_store = new Ext.data.JsonStore({
        data : server_ds_data,
        root : 'server_ds',
        fields: ['Type','ServerDataSource_URL','ServerDataSource_All_Number','ServerDataSource_Use_Number','ServerDataSource_Idle_Number']
    });
    engine_ds_store = new Ext.data.JsonStore({
        data : engine_ds_data,
        root : 'engine_ds',
        fields: ['Type','EngineDataSource_URL','EngineDataSource_All_Number','EngineDataSource_Use_Number','EngineDataSource_Idle_Number']
    });
    store1 = new Ext.data.JsonStore({
        data : data1,
        root : 'dataSourceInfo',
        fields: ['id','user','host','db','command','time','state','info']
    });
    serverDsGrid = new Ext.grid.GridPanel({
        title: 'Server DataSource',
        renderTo : "serverDataSource",
        store : server_ds_store,
        width : 1000,
        height : 200,
        stripeRows: true,
        columns: [{
            header: '连接池类型',
            dataIndex: 'Type',
            align: 'center',
            width: 170,
            sortable: true
        },{
            header: '连接地址',
            dataIndex: 'ServerDataSource_URL',
            align: 'center',
            width: 270,
            sortable: true
        },{
            header: '总连接数',
            dataIndex: 'ServerDataSource_All_Number',
            align: 'center',
            width: 170,
            sortable: true
        }, {
                header: '正在使用连接数',
                dataIndex: 'ServerDataSource_Use_Number',
                width: 170,
                align: 'center'
            },{
                header: '空闲连接数',
                dataIndex: 'ServerDataSource_Idle_Number',
                width: 170,
                align: 'center'
            }],
        style:{
            margin:"0 auto"
        }
    });
    engineDsGrid = new Ext.grid.GridPanel({
        title: 'Engine DataSource',
        renderTo : "engineDataSource",
        store : engine_ds_store,
        width : 1000,
        height : 200,
        stripeRows: true,
        columns: [{
            header: '连接池类型',
            dataIndex: 'Type',
            align: 'center',
            width: 170,
            sortable: true
        },{
            header: '连接地址',
            dataIndex: 'EngineDataSource_URL',
            align: 'center',
            width: 270,
            sortable: true
        },{
            header: '总连接数',
            dataIndex: 'EngineDataSource_All_Number',
            align: 'center',
            width: 170,
            sortable: true
        }, {
                header: '正在使用连接数',
                dataIndex: 'EngineDataSource_Use_Number',
                width: 170,
                align: 'center'
            },{
                header: '空闲连接数',
                dataIndex: 'EngineDataSource_Idle_Number',
                width: 170,
                align: 'center'
            }],
        style:{
            margin:"0 auto"
        }
    });
    grid1 = new Ext.grid.GridPanel({
        title: 'dataSourceInfo',
        renderTo : "dataSourceInfo",
        store : store1,
        width : 1200,
        height : 500,
        stripeRows: true,
        columns: [{
            header: 'id',
            dataIndex: 'id',
            align: 'center',
            width: 100,
            sortable: true
        },{
            header: 'user',
            dataIndex: 'user',
            align: 'center',
            width: 100,
            sortable: true
        }, {
            header: 'host',
            dataIndex: 'host',
            align: 'center',
            width: 100,
            sortable: true
        },{
                header: 'db',
                dataIndex: 'db',
                width: 100,
                align: 'center'
            },{
                header: 'command',
                dataIndex: 'command',
                width: 170,
                align: 'center'
            },{
                header: 'time',
                dataIndex: 'time',
                width: 100,
                align: 'center'
            },{
                header: 'state',
                dataIndex: 'state',
                width: 100,
                align: 'center'
            },{
                header: 'info',
                dataIndex: 'info',
                width: 500,
                align: 'center'
            }],
        style:{
            margin:"0 auto"
        }
    });
});
</script>
</head>
<body style="padding-top: 10px;">
<div id="serverDataSource"></div>
<div id="engineDataSource"></div>
<span style="padding-left:30px;">连接池具体信息：</span>
<div id="dataSourceInfo"></div>
</body>
</html>