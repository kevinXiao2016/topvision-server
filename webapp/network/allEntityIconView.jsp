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
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.core.js"></script>
<script type="text/javascript" src="../js/jquery/ui.selectable.js"></script>
<script type="text/javascript" src="../js/jquery/ui.draggable.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var pageSize = <s:property value="pageSize"/>;
var displayName = <s:property value="displayName"/>;
var sortName = '<s:property value="sortType"/>';
var linkStyle = [];
var dashedHelper = {border:'1px dashed black', background:'transparent'};
var squareHelper = {border:'1px solid #316AC5', background:'#D2E0F0', opacity:0.5};

var storeUrl = 'loadMyEntityByIcon.tv';
var urlByDetail = 'loadMyEntityJsp.tv?viewType=detail';
var urlByState = '../entity/getEntityState.tv';
var typeId = 0;

var dispatcherInterval = <s:property value="refreshInterval"/>;
var dispatcherTimer = null;

function getPropertyUrl() {return null;}
function tabActivate() {
	window.parent.setCoordInfo('');
	showEntityCount();
}
</script>
<script type="text/javascript" src="../network/entityByIcon.js"></script>
</head>
<body class=CONTENT_WND>
	<div style="display: none">
		<textarea id="renameBox" class=iptxa_focusing></textarea>
	</div>
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td><div id=toolbar></div>
			</td>
		</tr>
		<tr>
			<td height=100% valign=top><div id=entityMap
					style="position: fixed;"></div>
			</td>
		</tr>
		<tr id=pagingBar>
			<td><div id=pagingToolbar class=PAGING_TOOLBAR></div>
			</td>
		</tr>
	</table>
</body>
</html>
