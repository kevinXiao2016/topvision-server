<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

var entityId = <s:property value="entityId"/>;
var entityIp = '<s:property value="entity.ip"/>';
var store = null;
var gird = null;

function doRefresh() {
	refreshClick();
}

function refreshClick() {
	store.reload();
}

function onPropertyClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		window.top.showModalDlg(I18N.entityLinkInfo.portAttribute, 400, 400,
			'port/showPortPropertyJsp.tv?entityId=' + entityId + '&ifIndex=' + record.data.ifIndex);		
	}
}

function openPort() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelected();
		if (selections.data.ifOperStatus != '1') {
			window.top.showConfirmDlg(I18N.MENU.tip, I18N.entityLinkInfo.confirmopenport, function(type) {
				if (type == 'no') {return;}
				window.top.showWaitingDlg(I18N.MENU.wait, I18N.entityLinkInfo.openningport);
				Ext.Ajax.request({url: '../port/openPort.tv',
				   success: function() {
					window.top.closeWaitingDlg();
				       onRefreshClick();
				   },
				   failure: function() {
					   window.top.closeWaitingDlg();
					   window.top.showErrorDlg();
				   },
				   params: {'port.entityId': entityId, 
				   'port.ifIndex': selections.data.ifIndex, saveLog: true}
				});			
			});
		}
	}
}

function closePort() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelected();
		if (selections.data.ifOperStatus == '1') {
			window.top.showConfirmDlg(I18N.MENU.tip, I18N.entityLinkInfo.confirmcloseport, function(type) {
				if (type == 'no') {return;}
				window.top.showWaitingDlg(I18N.MENU.wait, I18N.entityLinkInfo.closeport);
				Ext.Ajax.request({url: '../port/closePort.tv',
				   success: function() {
					window.top.closeWaitingDlg();
				       onRefreshClick();
				   },
				   failure: function() {
					   window.top.closeWaitingDlg();
					   window.top.showErrorDlg();},
				   params: {'port.entityId': entityId, 
				   'port.ifIndex': selections.data.ifIndex, saveLog: true}
				});			
			});
		}
	}
}

function realtimeAnalyse() {
	Ext.menu.MenuMgr.hideAll();
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var r = sm.getSelected();
		window.open('../realtime/showPortFlowInfo.tv?entityId=' + entityId +
				'&ip=' + entityIp + '&ifIndex=' + r.data.ifIndex, 'realtime' + entityId);
	}
}

function renderOperStatus(value, p, record){
	if (record.data.ifAdminStatus == '1' && record.data.ifOperStatus == '1')  {
		return String.format('<img alt="{0}" src="../images/network/port/up.png" border=0 align=absmiddle>{1}', 'UP');	
	} else {
		return String.format('<img alt="{0}" src="../images/network/port/down.png" border=0 align=absmiddle>{1}', 'DOWN');	
	}
}

function renderAdminStatus(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		return String.format('<img alt="{0}" src="../images/network/port/admin.gif" border=0 align=absmiddle>{1}',
				I18N.NETWORK.portEnabled);		
	} else {
		return String.format('<img alt="{0}" src="../images/network/port/noadmin.gif" border=0 align=absmiddle>{1}',
				I18N.port.adminDown);	
	}
}

function doOnResize() {
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 70;
	grid.setWidth(w);
	grid.setHeight(h);
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function() {
	var entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 160, items:[
		{text: I18N.NETWORK.openPort, handler: openPort},
		{text: I18N.NETWORK.closePort, handler: closePort}, '-',
		{text: I18N.NETWORK.portRealFlow, handler: realtimeAnalyse}, '-',
		{text: I18N.MENU.refresh, iconCls: 'bmenu_refresh', handler: refreshClick}, '-',
		{text: I18N.NETWORK.property, handler: onPropertyClick}
	]});

    var cm = new Ext.grid.ColumnModel([
    	{header: I18N.entityLinkInfo.portNo, dataIndex: 'ifIndex', width: 100},
    	{header: I18N.entityLinkInfo.portName, dataIndex: 'ifName', width: 130},
    	{header: I18N.NETWORK.alias, dataIndex: 'ifAlias', width: 130},
    	{header: I18N.NETWORK.typeHeader, sortable:true, dataIndex: 'ifType', width: 100},
    	{header: I18N.entityPorts.MaximumTransmissionUnit, sortable:true, dataIndex: 'ifMtu', align : 'right', width: 100},
    	{header: I18N.topoLabel.speedRate, dataIndex: 'ifSpeed', sortable:true, align: 'right', width: 100},
    	{header: I18N.NETWORK.macHeader, dataIndex: 'ifPhysAddress', width: 120},
    	{header: I18N.NETWORK.manageStatus, dataIndex: 'ifAdminStatus', sortable:true, width: 100, align : 'center', renderer: renderAdminStatus},
    	{header: I18N.entityPorts.operationStatus, dataIndex: 'ifOperStatus', sortable:true, width: 100, align: 'center', renderer: renderOperStatus}]);
    cm.defaultSortable = true;
	store = new Ext.data.JsonStore({
	    url: ('../port/loadPortsByEntityId.tv?entityId=' + entityId),
	    root: 'data',
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['ifIndex', 'ifName', 'ifAlias', 'ifType', 'ifMtu', 'ifSpeed', 'ifPhysAddress', 'ifAdminStatus', 'ifOperStatus']
	});
	
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 70;	
    grid = new Ext.grid.GridPanel({
    	width : w,
        height: h,
    	viewConfig: {forceFit:false, enableRowBody:true, showPreview:false},
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: true,
        cm : cm,
 		sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
        store : store
    });
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}    	
		entityMenu.showAt(e.getPoint());
    });
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);
   		var url = 'port/showPortPropertyJsp.tv?entityId=' + entityId + '&ifIndex=' + record.data.ifIndex;
   		window.top.showModalDlg(I18N.entityLinkInfo.portAttribute, 400, 400, url);
    });
    grid.render("ports-div");
	store.load();
	tabShown();
});
</script>
</head>
<body class=BLANK_WND scroll=no style="padding: 15px;"
	onresize="doOnResize();">
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td valign=top><%@ include file="entity.inc"%></td>
		</tr>
		<tr>
			<td style="padding-top: 15px">
				<div id="ports-div"></div>
			</td>
		</tr>
	</table>
</body>
</html>