<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.monitorItemIcon {
	background-image: url(../images/fault/monitorItem.gif) !important;
	valign: middle;
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
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

var itemName = null;

function doOnresize() {
	var w = document.body.clientWidth - 350;
	var h = document.body.clientHeight - 120;
	if (w < 100) {
		w = 100;
	}
	if (h < 100) {
		h = 100;
	}	
	grid.setWidth(w);
	grid.setHeight(h);
	entityTree.setHeight(h);
}

function buildItemTree(w, h) {
	var treeLoader = new Ext.tree.TreeLoader({dataUrl:'../threshold/loadMonitorItems.tv'});
	treeLoader.on('load', function() {
		var node = entityTree.getNodeById('item1');
		itemName = node.attributes.itemName;
		node.select();
		Zeta$('addBt').disabled = false;		
	});
	entityTree = new Ext.tree.TreePanel({
        el: 'itemtree-div', useArrows: useArrows,
		width: 300,
		height: h,        
        autoScroll: true, 
        animate: animCollapse, border: true,
        trackMouseOver: trackMouseOver,
        lines: true, rootVisible: false, enableDD: false,
        loader: treeLoader
    });

    var root = new Ext.tree.AsyncTreeNode({text: 'Monitor Item Tree', draggable:false, id: "itemRootNode"});
    entityTree.setRootNode(root);
    entityTree.render();
    root.expand();

    entityTree.on("click", function (n) {
		itemName = n.attributes.itemName;
		thresholdStore.load({params: {itemName: itemName}});
		Zeta$('addBt').disabled = false;
		Zeta$('deleteBt').disabled = true;
	});
}

var thresholdStore = null;
var grid = null;
function buildThresholdTree(w, h) {
	var columnModels = [
	    {header: I18N.COMMON.entity, width: 105, dataIndex: 'entityName'},
	    {header: I18N.ALERT.portNum, width: 75, dataIndex: 'portName'},
		{header: I18N.ALERT.threshold, width: 320, dataIndex: 'thresholdDesc'}
	];

	thresholdStore = new Ext.data.JsonStore({
	    url: '../threshold/loadObjByMonitorItem.tv',
	    root: 'data',
	    remoteSort: true,
	    fields: ['entityId', 'entityName', 'entityIp', 'portIndex',
	    	'portName', 'thresholdId', 'thresholdDesc']
	});
    thresholdStore.setDefaultSort('name', 'asc');

    grid = new Ext.grid.GridPanel({
		store: thresholdStore,
		columns: columnModels,
		width: w,
		height: h,
        animCollapse: false,
        trackMouseOver: trackMouseOver,
        border: true
    });
    grid.render('thresholdTree');
    
    grid.on('rowclick', function(grid, rowIndex, e) {
    	if (rowIndex == 0) {    			
			Zeta$('deleteBt').disabled = true;	
    	} else {
    		Zeta$('deleteBt').disabled = false;
    	}
    });
    thresholdStore.load({params: {itemName: 'ping'}});
}

function relateThreshold() {
	window.parent.createDialog("modalDlg", I18N.ALERT.addMonitorObject, 480, 300,
		'threshold/showPopMonitorObject.tv?itemName=' + itemName, 
		null, true, true);
}

function deleteThreshold() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selected = sm.getSelected();
		$.ajax({url:'../threshold/deleteRelatedThreshold.tv', data:{'entityId' : selected.data.entityId,
			'itemName': itemName, portIndex: selected.data.portIndex},
			dataType: 'json', cache: 'false',
			success:function(json){
				thresholdStore.remove(selected);
				Zeta$('deleteBt').disabled = true;
			}, error:function(){
			}
		});		
	}
}

function refreshThresholdTree() {
	thresholdStore.load({params: {itemName: itemName}});
	Zeta$('relateBt').disabled = true;
	Zeta$('deleteBt').disabled = true;	
}
Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
Ext.onReady(function() {
	var w = document.body.clientWidth - 350;
	var h = document.body.clientHeight - 120;
	if (w < 100) {
		w = 100;
	}
	if (h < 100) {
		h = 100;
	}
	buildItemTree(w, h);
	buildThresholdTree(w, h);  
	tabShown(); 
});
</script>
</HEAD><BODY class=BLANK_WND scroll=no style="padding:15px;" onresize="doOnresize()">
<table cellspacing=0 cellpadding=0>
<tr><td>
	<div class=ultab>
		<ul>
			<li><a href="../fault/thresholdConfigByEntity.jsp"><fmt:message key="ALERT.setThresholdByEntity" bundle="${fault}"/></a></li>
			<li class=selected><a href="../fault/thresholdConfigByItem.jsp"><fmt:message key="ALERT.setThresholdByMonitorItem" bundle="${fault}"/></a></li>
		</ul>
	</div>	
</td></tr>
<tr><td style="padding-top:5px;">
	<table cellspacing=0 cellpadding=0>
	<tr><Td height=25px><fmt:message key="ALERT.monitorItem" bundle="${fault}"/>:</Td><td width=30px></td><td><fmt:message key="ALERT.monitorObject bundle="${fault}"/>:</td></tr>
	<tr><Td><div id="itemtree-div"></div></Td><td></td><Td><div id="thresholdTree"></div></Td></tr>	
	</table>
</td></tr>

<tr><Td align=right style="padding-top:10px"><button 
    id="addBt" type="button" class=BUTTON75 disabled
	onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onclick="relateThreshold()"><fmt:message key="COMMON.add" bundle="${resource}"/>...</button>&nbsp;<button 
    type="button" class=BUTTON75 id="deleteBt" disabled
	onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'" onclick="deleteThreshold()"><fmt:message key="COMMON.deleteAction" bundle="${resource}"/></button></Td></tr>
</table>
</BODY></HTML>
