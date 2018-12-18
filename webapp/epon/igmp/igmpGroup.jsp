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
    IMPORT js/tools/ipText
    IMPORT epon/igmp/igmpGroup
</Zeta:Loader>
<style type="text/css">
    body,html{height:100%;}
	.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var entityId = parent.entityId;
	var addressIpV4,sourceIpV4; //ip输入框;
	var cm,store,grid,
	    isCreateSelect = null, //点击添加是时候，组播vlan的下拉框只需要创建一次;
	    igmpVersion = parseInt('${igmpVersion}', 10),//imgp版本;
	    versionObj = {
	    	2 : 'V2',
	    	3 : 'V3',
	    	4 : 'V3-only'
	    }
</script>
</head>
<body class="frameBody"></body>
</Zeta:HTML>