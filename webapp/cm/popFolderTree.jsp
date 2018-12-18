<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="STYLESHEET" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css">
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<style type="text/css">
.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoFolderIcon1 {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
}

.topoFolderIcon5 {
	background-image: url(../images/network/subnet.gif) !important;
}

.topoFolderIcon6 {
	background-image: url(../images/network/cloudy16.gif) !important;
}

.topoFolderIcon7 {
	background-image: url(../images/network/href.png) !important;
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;
Ext.onReady(function(){
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'../cmpoll/loadTopoFolder.tv?hasSubnet=true'});
	treeLoader.on('load', function() {
		//tree.getNodeById(0).select();
		tree.getRootNode().select()
	});
    tree = new Ext.tree.TreePanel({
        el:'topoTree', useArrows:useArrows, autoScroll:true, animate:true, border: false,
        trackMouseOver:false, lines:true, rootVisible:false, containerScroll:true, enableDD:false,
        loader: treeLoader 
    });
    var root = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: 'source'});
    tree.setRootNode(root);
    tree.render();
    root.expand();
    
	tree.on('click', function(n){
		Zeta$('okBt').disabled = (n.id == 1);
	});    
});
function getSelectedItemId(t) {
	var modal = t.getSelectionModel().getSelectedNode();
	var item = null
	if (modal != null) {
		item = {
				itemId : modal.id,
				itemName : modal.text
		}
	}
	return item
}
function okClick() {
	var item = getSelectedItemId(tree);// folder id
	if(item.itemId == 'source') {
		window.parent.showMessageDlg(I18N.NETWORK.tip, I18N.NETWORK.plsSelectFolder);
		return
	}
	window.top.ZetaCallback = {type:'ok', selectedItem:item};
	window.top.closeWindow('topoFolderTree');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('topoFolderTree');
}
</script>
</HEAD>
<BODY class=POPUP_WND style="margin: 10px">
	<table cellspacing=0 cellpadding=0 width=100% height=100%>
		<tr>
			<td height=20><fmt:message key="NETWORK.moveToSelectedTopoFolder" bundle="${network}"/>:</td>
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
				<button id=okBt class=BUTTON75
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onMouseOut="this.className='BUTTON75'" onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;
				<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button>
			</td>
		</tr>
		<tr>
			<td height=4></td>
		</tr>
	</table>
</BODY>
</HTML>