<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../network/entityByTypeDetail.js"></script>
<script type="text/javascript">
var pageSize = <s:property value="pageSize"/>;
var typeId = <s:property value="typeId"/>;
var columnModels = [
	{id:'name', header:I18N.topo.virtualDeviceList.newEntity.deviceAlias, width:150, sortable:true, groupable:false, dataIndex:'name', renderer: renderName},
    {header:I18N.NETWORK.ipHeader, width:150, sortable:true, groupable:false, dataIndex: 'ip'},
    {header:I18N.NETWORK.typeHeader, width:150, sortable:true, dataIndex: 'modelName'},
	{header:I18N.NETWORK.manageStatus, width:100, sortable:true, dataIndex: 'state', align:'center', renderer: renderManagement}    
	//{header:I18N.NETWORK.alarmLevelHeader, width:100, sortable:true, dataIndex: 'alert', align:'center', renderer: renderLevel}
];

columnModels[columnModels.length] = {header:I18N.COMMON.snapTimeHeader, width:150, sortable:true, groupable:false, dataIndex:'snapTime'};
var storeUrl = 'loadEntityByTypeByDetail.tv?typeId='+typeId;

var dispatcherInterval = <s:property value="refreshInterval"/>;
var dispatcherTimer = null;
</script>
</head>
<body class=CONTENT_WND>
</body>
</html>
