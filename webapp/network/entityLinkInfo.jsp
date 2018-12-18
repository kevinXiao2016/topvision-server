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
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
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

function doRefresh() {
	onRefreshClick();
}

function onRefreshClick() {
	store.reload();
}

var entityId = <s:property value="entityId"/>;
var entityIp = '<s:property value="entity.ip"/>';
var store = null;
var gird = null;

function renderIndex(value, p, record){
	if (record.data.ifAdminStatus == '1') {
		if (record.data.ifOperStatus == '1')  {
			return String.format('<img alt="{0}" src="../images/network/port/up.png" border=0 align=absmiddle>{1}',
			'UP', value);	
		} else {
			return String.format('<img alt="{0}" src="../images/network/port/down.png" border=0 align=absmiddle>{1}',
			'DOWN', value);	
		}
	} else {
		return String.format('<img alt="{0}" src="../images/network/port/noadmin.gif" border=0 align=absmiddle>{1}',
				I18N.NETWORK.portDisabled, value);	
	}
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
		{text: I18N.MENU.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}, '-',
		{text: I18N.NETWORK.property, handler: onPropertyClick}
	]});

	var reader = new Ext.data.JsonReader({
	    totalProperty: "rowCount",
	    root: "data",
        fields: [
			{name: 'ifAdminStatus'},
		    {name: 'ifOperStatus'},
		    {name: 'ifIndex'},
		    {name: 'ifName'},
		    {name: 'mac'},
		    {name: 'ip'},
		    {name: 'name'},
		    {name: 'type'},
		    {name: 'destIfIndex'},
			{name: 'destIfName'}
        ]	    
	});

	store = new Ext.data.GroupingStore({
		 url: ('../link/getEntityLinkTable.tv?entityId=' + entityId),
		  reader: reader,
		    remoteSort: false,
		    remoteGroup: false,
			groupField: 'type',
			groupOnSort: false
    });
    store.setDefaultSort('ifIndex', 'ASC');

    var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "'+I18N.entityLinkInfo.item+'" : "'+I18N.entityLinkInfo.item+'"]})';


    var cm = new Ext.grid.ColumnModel([
    	{header: I18N.entityLinkInfo.portNo, dataIndex: 'ifIndex', width: 100, groupable: false, sortable: true, renderer: renderIndex},
    	{header: I18N.entityLinkInfo.portName, dataIndex: 'ifName', width: 130, groupable: false, sortable: true},
    	{header: I18N.entityLinkInfo.peerdevice, dataIndex: 'name', width: 100, groupable: false, sortable: true},
    	{header: I18N.entityLinkInfo.peerIp, dataIndex: 'ip', width: 100, groupable: false, sortable: true},
    	{header: I18N.entityLinkInfo.peerType, dataIndex: 'type', width: 100, sortable: true},
    	{header: I18N.entityLinkInfo.peerMac, dataIndex: 'mac', width: 100, groupable: false, sortable: true},
    	{header: I18N.entityLinkInfo.peerPort, dataIndex: 'destIfIndex', width: 130, groupable: false, sortable: true},
    	{header: I18N.entityLinkInfo.peerPortName, dataIndex: 'destIfName', width: 130, groupable: false, sortable: true}
    ]);
    cm.defaultSortable = true;
	
	var w = document.body.clientWidth - 30;
	var h = document.body.clientHeight - 70;	
    grid = new Ext.grid.GridPanel({
    	width : w,
        height: h,

        view: new Ext.grid.GroupingView({
            forceFit: true,
            hideGroupedColumn: true,
            enableNoGroups: true,
            groupTextTpl: groupTpl
        }),

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