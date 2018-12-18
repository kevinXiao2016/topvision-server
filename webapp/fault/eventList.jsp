<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head><title>Event List</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>-2.1.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript" src="event-detail.js"></script>
</head><body class=CONTENT_WND>
<script type="text/javascript">
setEnabledContextMenu(false);
</script>
</body></html>