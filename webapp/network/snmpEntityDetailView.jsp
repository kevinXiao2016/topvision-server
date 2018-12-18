<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript"
	src="../js/ext/ext-lang-<%= lang %>-2.1.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript" src="../network/entityByDetail.js"></script>
<script type="text/javascript">
var pageSize = <s:property value="pageSize"/>;
var columnModels = [
	{id:'name', header:I18N.NETWORK.nameHeader, width:150, sortable:true, groupable:false, dataIndex:'name', renderer: renderName},
    {header:I18N.NETWORK.ipHeader, width:150, sortable:true, groupable:false, dataIndex: 'ip'},
    {header:I18N.NETWORK.typeHeader, width:150, sortable:true, dataIndex:'typeName'},
{header:I18N.NETWORK.stateHeader, width:100, sortable:true, dataIndex: 'state', align:'center', renderer: renderOnlie},    
	{header:I18N.NETWORK.alarmLevelHeader, width:100, sortable:true, dataIndex: 'alert', align:'center', renderer: renderLevel},
    {header:I18N.COMMON.snapTimeHeader, width:150, sortable:true, groupable:false, dataIndex:'snapTime'}
];
var storeUrl = 'loadSnmpEntityByDetail.tv';
var urlByIcon = 'showSnmpEntityJsp.tv?viewType=icon';

var dispatcherInterval = <s:property value="refreshInterval"/>;
var dispatcherTimer = null;
</script>
</head>
<body class=CONTENT_WND>
</body>
</html>
