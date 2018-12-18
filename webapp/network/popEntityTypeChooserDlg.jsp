<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<%@include file="../include/cssStyle.inc"%>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="STYLESHEET" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css">
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script src="../js/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
</HEAD>
<BODY class=POPUP_WND style="margin: 10px">
	<table cellspacing=0 cellpadding=0 width=100% height=100%>
		<tr>
			<td height=20><fmt:message key="popEntityTypeChooserDlg.selectDeviceType" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td height=4></td>
		</tr>
		<tr>
			<td height=100% class=TREE-CONTAINER><div id="topoTree"
					style="height: 100%; overflow: auto;"></div>
			</td>
		</tr>
		<tr>
			<td height=4>&nbsp;</td>
		</tr>
		<tr>
			<td valign=top align=right>
				<button id=okBt class=BUTTON75 disabled
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onmousedown="this.className='BUTTON_PRESSED75'" onclick="okClick()"><fmt:message key="popDevicePollingInterval.confirm" bundle="${resources}" /></button>&nbsp;
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onmousedown="this.className='BUTTON_PRESSED75'"
					onclick="cancelClick()"><fmt:message key="popDevicePollingInterval.Cancel" bundle="${resources}" /></button>
			</td>
		</tr>
		<tr>
			<td height=4></td>
		</tr>
	</table>
	<script>
<!--
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
setEnabledContextMenu(true);
Ext.onReady(function(){
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'../entity/loadEntityTemplate.tv?hasRoot=false'});
    tree = new Ext.tree.TreePanel({
        el:'topoTree', singleExpand: true, useArrows:useArrows, autoScroll:true, animate : animCollapse , border: false,
        trackMouseOver : trackMouseOver, lines:true, rootVisible:false, containerScroll:true, enableDD:false,
        loader: treeLoader 
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id:'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    
	tree.on('click', function(n){
		Zeta$('okBt').disabled = (n.id == 1);
	});    
});
function getSelectedItemId(t) {
	var modal = t.getSelectionModel().getSelectedNode();
	var itemId = null;
	if (modal != null) {
		itemId = modal.id;
	}
	return itemId;
}
function okClick() {
	var itemId = getSelectedItemId(tree);
	window.top.ZetaCallback = {type:'ok', selectedItemId : itemId};
	window.top.closeWindow('typeDlg');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('typeDlg');
}
-->
</script>
</BODY>
</HTML>