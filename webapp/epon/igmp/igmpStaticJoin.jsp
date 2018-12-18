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
    IMPORT epon/igmp/customOltIgmp
    IMPORT js/tools/ipText
    IMPORT epon/igmp/igmpStaticJoin
</Zeta:Loader>
<style type="text/css">
.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = parent.entityId;
var igmpVersion = parseInt('${igmpVersion}', 10);//imgp版本;
var	versionObj = {
    	'2' : 'V2',
    	'3' : 'V3',
    	'4' : 'V3-only'
    };
</script>
</head>
<body class="frameBody">
</body>
</Zeta:HTML>