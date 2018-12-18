<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script src="../js/zetasframework/zeta-core.js"></script>
<script>
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
Ext.onReady(function(){
	Ext.QuickTips.init();

	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'../monitor/loadMonitorCategory.tv?hasRoot=false'});
    var tree = new Ext.tree.TreePanel({
        el: 'entityTree', useArrows: useArrows, autoScroll: true, animate: animCollapse, border: false,
        trackMouseOver: trackMouseOver,
        lines: true, rootVisible: false, enableDD: false,
        loader: treeLoader
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: "deviceRootNode"});
    tree.setRootNode(root);
    tree.render();
    root.expand();
});
</script>
</HEAD><BODY class=BLANK_WND style="margin-top:20px;">
<center>
<table width=650px cellspacing=0 cellpadding=0>
<tr><td>
	<div class=ultab>
		<ul>
			<li><a href="../fault/thresholdConfigByEntity.jsp"><fmt:message key="ALERT.setThresholdByEntity" bundle="${fault}"/></a></li>
			<li class=selected><a href="../fault/thresholdConfigByItem.jsp"><fmt:message key="ALERT.setThresholdByMonitorItem" bundle="${fault}"/></a></li>
		</ul>
	</div>
</td></tr>
<tr><td>
	<table cellspacing=0 cellpadding=0>
	<tr><Td width=280px height=25px><fmt:message key="ALERT.monitorItem" bundle="${fault}"/>:</Td><td width=20px></td><td><fmt:message key="ALERT.alertConfig" bundle="${fault}"/>:</td></tr>
	<tr><Td class=TREE-CONTAINER><div id="entityTree" style="width:280px; height:350;overflow:auto"></div></Td>
	<td width=20px></td><Td class=TREE-CONTAINER width=350px>sss</Td></tr>	
	</table>
</td></tr>

<tr><Td align=right style="padding-top:10px"><button type="button" class=BUTTON75
	onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onclick="okClick()"><fmt:message key="COMMON.save" bundle="${resource}"/></button></Td></tr>
</table>
</center>
</BODY></HTML>
