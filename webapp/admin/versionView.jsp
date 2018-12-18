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
    var data=${versions};
    var store,grid;
    
    store=new Ext.data.JsonStore({
        data : data,
        root : 'versions',
        fields: ['moduleName','versionNum','versionDate']
    });
    grid = new Ext.grid.GridPanel({
        title: '<fmt:message bundle="${sys}" key="sys.moduleVersion" />',
        renderTo : "moduleVersion",
        store : store,
        width : 560,
        height : 315,
        stripeRows: true,
        selModel : new Ext.grid.RowSelectionModel({singleSelect : true}),
        columns: [{
                header: '<fmt:message bundle="${sys}" key="sys.moduleName" />',
                dataIndex: 'moduleName',
                align: 'center',
                width: 170,
                sortable: true
            },{
                header: '<fmt:message bundle="${sys}" key="sys.version" />',
                dataIndex: 'versionNum',
                width: 170,
                align: 'center'
            },{
                header: '<fmt:message bundle="${sys}" key="sys.updateDate" />',
                dataIndex: 'versionDate',
                width: 170,
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
<div id="header" style="width:560px;margin:0 auto;">
<span><fmt:message bundle="${sys}" key="mibble.version" />:${version.buildVersion}</span>
<span style="padding-left:30px;"><fmt:message bundle="${sys}" key="sys.buildNum" />:${version.buildNumber}</span>
<span style="padding-left:30px;"><fmt:message bundle="${sys}" key="sys.buildDate" />:${version.buildTime}</span>
</div>
<div id="moduleVersion"></div>
</body>
</html>