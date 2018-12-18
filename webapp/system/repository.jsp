<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<html><head>
<style type="text/css">
.bmenu_reset {background-image: url(../images/system/reset.gif) !important;}
.bmenu_role {background-image: url(../images/system/setRole.gif) !important;}
</style>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>-2.1.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript" src="repository.js"></script>
</head><body class=CONTENT_WND>
<script type="text/javascript">
var pageSize = 25;
setEnabledContextMenu(false);
</script>
</body></html>
