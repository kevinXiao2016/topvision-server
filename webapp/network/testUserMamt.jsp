<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/nocache.inc"%>
<%@ include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<title><fmt:message bundle="${i18n}" key="IGMP.callHistory" /></title>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var entityTypes = ${entityJson};
Ext.onReady(function(){
	var cm = new Ext.grid.ColumnModel([
	{id:"ip", header: "IP", align: 'center', dataIndex: 'ip', width: 180,sortable:true},
	{id:"name", header: "@ENTITYSNAP.basicInfo.name@", align: 'center', dataIndex: 'name', width: 180,sortable:true}
]);
    cm.defaultSortable = true;	
/* 	store = new Ext.data.SimpleStore({
		data : entityTypes,
		fields : ['entityIp']
	}); */
    store = new Ext.data.Store({
        proxy: new Ext.data.MemoryProxy(entityTypes),
        reader: new Ext.data.JsonReader({},[
		   {name:'ip'},
		   {name:'name'}
       ])
     });
    store.load();
	grid = new Ext.grid.GridPanel(
		 {border:true, 
		  region:'center',
		  width: 450, 
		  height: 500,
          store: store, 
          cm: cm,
		  renderTo: "cdrGrid"
          });
});

</script>
</head>
<body class=POPUP_WND style="margin: 15px;">
	<div id="cdrGrid"></div>
</body>
</html>