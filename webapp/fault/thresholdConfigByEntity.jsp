<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
.myEntityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}
.entityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}
.switchIcon {
	background-image: url(../images/network/switch_16.gif) !important;
	valign: middle;
}
.routerIcon {
	background-image: url(../images/network/router_16.gif) !important;
	valign: middle;
}
.l3switchIcon {
	background-image: url(../images/network/l3switch_16.gif) !important;
	valign: middle;
}
.serverIcon {
	background-image: url(../images/network/server_16.gif) !important;
	valign: middle;
}
.desktopIcon {
	background-image: url(../images/network/desktop_16.gif) !important;
	valign: middle;
}
.unknownIcon {
	background-image: url(../images/network/unknown_16.gif) !important;
	valign: middle;
}
.printerIcon {
	background-image: url(../images/network/printer_16.gif) !important;
	valign: middle;
}
.hubIcon {
	background-image: url(../images/network/hub_16.gif) !important;
	valign: middle;
}
.firewallIcon {
	background-image: url(../images/network/firewall_16.gif) !important;
	valign: middle;
}
.wirelessIcon {
	background-image: url(../images/network/wireless_16.gif) !important;
	valign: middle;
}
.upsIcon {
	background-image: url(../images/network/ups_16.gif) !important;
	valign: middle;
}
.domainControllerIcon {
	background-image: url(../images/network/controller_16.gif) !important;
	valign: middle;
}
.thresholdFolderIcon {
	background-image: url(../images/fault/threshold.gif) !important;
	valign: middle;
}
.thresholdLeafIcon {
	background-image: url(../images/fault/threshold.gif) !important;
	valign: middle;
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	refreshThresholdTree();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

function doOnresize() {
	var w = document.body.clientWidth - 350;
	var h = document.body.clientHeight - 120;
	if (w < 100) {
		w = 100;
	}
	if (h < 100) {
		h = 100;
	}	
	thresholdTree.setWidth(w);
	thresholdTree.setHeight(h);
	entityTree.setHeight(h);
}

var entityId = 0;
var itemName = null;
var entityTreeLoader = null;
var entityTreeRoot = null;
var entityTree = null;
function buildEntityTree(w, h) {
	entityTreeLoader = new Ext.tree.TreeLoader({dataUrl:'../resources/getSnmpEntityTree.tv'});
	entityTree = new Ext.tree.TreePanel({
        el: 'entityTree', useArrows: useArrows, autoScroll: true, animate: animCollapse,
        border: true, trackMouseOver: trackMouseOver,
		width: 300,
		height: h,        
        lines: true, rootVisible: false, enableDD: false,
        loader: entityTreeLoader
    });
    entityTreeRoot = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: "deviceRootNode"});
    entityTree.setRootNode(entityTreeRoot);
    entityTree.render();
    entityTreeRoot.expand();
    
    entityTree.on("click", function (n) {		
		if (n.attributes.type == 'entity') {
			entityId = n.attributes.entityId;
			thresholdLoader.dataUrl = '../threshold/getThresholdByEntity.tv?entityType=' + 
				n.parentNode.attributes.entityId + '&entityId=' + entityId;
			thresholdRoot.reload();
			Zeta$('relateBt').disabled = true;
			Zeta$('deleteBt').disabled = true;			
		}
	});
}

var thresholdLoader = null;
var thresholdTree = null;
var thresholdRoot = null;
var thresholdId = 0;
function buildThresholdTree(w, h) {
	thresholdLoader = new Ext.tree.TreeLoader({dataUrl: '../threshold/getThresholdByEntity.tv'});	
    thresholdTree = new Ext.tree.TreePanel({
        el: 'thresholdTree', useArrows: useArrows, autoScroll: true, 
        animate: animCollapse, border: true,
        trackMouseOver: trackMouseOver,
		width: w,
		height: h,        
        lines: true, rootVisible: false, enableDD: false,
        loader: thresholdLoader
    });    
    thresholdRoot = new Ext.tree.AsyncTreeNode({text: 'Topo Folder Tree', draggable:false, id: "deviceRootNode"});
    thresholdTree.setRootNode(thresholdRoot);
    thresholdTree.render();
    thresholdRoot.expand();
    
	thresholdTree.on("click", function (n) {
		itemName = n.attributes.itemName;
		if (n.attributes.monitorItem == 3) {
			thresholdId = n.attributes.thresholdId;
			Zeta$('relateBt').disabled = false;
			Zeta$('deleteBt').disabled = false;			
		} else {	
			thresholdId = 0;
			Zeta$('relateBt').disabled = false;
			Zeta$('deleteBt').disabled = true;		
		}
	});
}

function newThreshold(entityId) {
	window.parent.createDialog("modalDlg", I18N.MAIN.newThreshold, 450, 450, "fault/newThreshold.jsp", 
		null, true, true);
}
function relateThreshold() {
	window.parent.createDialog("modalDlg", I18N.ALERT.chooseThreshold, 380, 220,
		"threshold/showPopThresholdDlg.tv?entityId=" + entityId + '&itemName=' + itemName, 
		null, true, true);
}

function deleteThreshold() {
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.ALERT.confirmDeleteThresholdRelate, deleteThresholdCallback);
}
function deleteThresholdCallback(type) {
	if(type == 'no' || thresholdId == 0) return;
	$.ajax({url:'../threshold/deleteRelatedThreshold.tv',
		data:{'entityId' : entityId, 'itemName': itemName, 'thresholdId': thresholdId},
		dataType: 'json', cache: 'false',
		success:function(json){
			if (json.success) {
				refreshThresholdTree();		
			} else {
				window.parent.showErrorDlg();
			}
		}, error:function(){window.parent.showErrorDlg();}
	});
}

function selectSnmpEntity(obj) {
	if (obj.checked) {
		entityTreeLoader.dataUrl = '../resources/getSnmpEntityTree.tv';	
	} else {
		entityTreeLoader.dataUrl = '../resources/getEntityTree.tv';		
	}
	entityTreeRoot.reload();
}

function refreshThresholdTree() {
	thresholdRoot.reload();
	Zeta$('relateBt').disabled = true;
	Zeta$('deleteBt').disabled = true;	
}
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
Ext.onReady(function(){
	var w = document.body.clientWidth - 350;
	var h = document.body.clientHeight - 120;
	if (w < 100) {
		w = 100;
	}
	if (h < 100) {
		h = 100;
	}
	buildEntityTree(w, h);
	buildThresholdTree(w, h);
	tabShown();
});
</script>
</HEAD><BODY class=BLANK_WND scroll=no style="padding:15px;" onresize="doOnresize()">
<table cellspacing=0 cellpadding=0>
<tr><td colspan=2>
	<div class=ultab>
		<ul>
			<li class=selected><a href="#"><fmt:message key="ALERT.setThresholdByEntity" bundle="${fault}"/></a></li>
			<li><a href="../fault/thresholdConfigByItem.jsp"><fmt:message key="ALERT.setThresholdByMonitorItem" bundle="${fault}"/></a></li>
		</ul>
	</div>
</td></tr>
<tr><td colspan=2 style="padding-top:5px">
	<table cellspacing=0 cellpadding=0>
	<tr><Td height=25px><fmt:message key="allSnmpEntity" bundle="${network}"/>:</Td><td width=30px></td><td><fmt:message key="ALERT.alertThresholdConfig" bundle="${fault}"/>:</td></tr>
	<tr><Td><div id="entityTree"></div></Td><td></td><Td><div id="thresholdTree"></div></Td></tr>	
	</table>
</td></tr>

<tr><td>
<input id="allSnmpEntity" type=checkbox checked onclick="selectSnmpEntity(this)"><label for="allSnmpEntity"><fmt:message key="allSnmpEntity" bundle="${network}"/></label></td>
<Td align=right style="padding-top:10px"><button 
    id="relateBt" type="button" class=BUTTON75 disabled
	onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onclick="relateThreshold()"><fmt:message key="ALERT.relateThreshold" bundle="${fault}"/></button>&nbsp;<button 
    type="button" class=BUTTON75 id="deleteBt" disabled
	onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onclick="deleteThreshold()"><fmt:message key="ALERT.deleteThresholdRelate" bundle="${fault}"/></button></Td></tr>
</table>
</BODY></HTML>
