<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module igmpconfig
    CSS css/white/disabledStyle
    IMPORT epon/igmp/customOltIgmp
    IMPORT epon/igmp/igmpCtcProfile
</Zeta:Loader>
<style type="text/css">
	.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var entityId = parent.entityId;
	var cm,store,grid;
	var authArray = ["UNKOWN", "deny", "permit","preview"];
</script>
</head>
<body class="frameBody"></body>
</Zeta:HTML>