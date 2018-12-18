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
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/jquery-ui-core-1.5.js"></script>
<script type="text/javascript"
	src="../js/jquery/jquery-ui-draggable-1.5.js"></script>
<script type="text/javascript"
	src="../js/jquery/jquery-ui-selectablex-1.5.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript">
var pageSize = <s:property value="pageSize"/>;
var displayName = <s:property value="displayName"/>;
var sortName = '<s:property value="sortType"/>';
var linkStyle = [];
var dashedHelper = {border:'1px dashed black', background:'transparent'};
var squareHelper = {border:'1px solid #316AC5', background:'#D2E0F0', opacity:0.5};

var storeUrl = 'loadSnmpEntityByIcon.tv';
var urlByDetail = 'showSnmpEntityJsp.tv?viewType=detail';
var urlByState = '../entity/getSnmpEntityState.tv';
var typeId = 0;

var dispatcherInterval = <s:property value="refreshInterval"/>;
var dispatcherTimer = null;

setEnabledContextMenu(false);
function getPropertyUrl() {
	return null;
}
function tabActivate() {
	window.parent.setCoordInfo('');
	showEntityCount();	
}
</script>
</head>
<body class=CONTENT_WND>
	<div style="display: none">
		<textarea id="renameBox" class=iptxa_focusing></textarea>
	</div>
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td valign=top><div id=toolbar></div>
			</td>
		</tr>
		<tr>
			<td height=100%><div id=entityMap style="position: fixed;"></div>
			</td>
		</tr>
		<tr id=pagingBar>
			<td><div id=pagingToolbar class=PAGING_TOOLBAR></div>
			</td>
		</tr>
	</table>
	<script type="text/javascript" src="../network/entity-icon.js"></script>
</body>
</html>
