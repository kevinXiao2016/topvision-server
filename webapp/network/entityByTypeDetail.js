/**
 * All entity view by detail.
 *
 * author:niejun
 */
var contextMenu = null;
var entityMenu = null;
var grid = null;
var store = null;
var pagingToolbar = null;
var refreshInterval = null;

function showEntitySnap(id, ip) {
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-' + id, I18N.COMMON.entity + '[' + ip + ']', 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
}

function showCcmts(cmcId,cmcMac){
    window.top.addView('entity-' + cmcId, "CCMTS" + '[' + cmcMac + ']', 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId);    
}

function defaultFailureCallback(response) {
	window.parent.showErrorDlg();
}
function defaultSuccessCallback(response) {
}
function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url, failure: fn, success: sn, params: param});
}

function renderName(value, p, record){
    var parentId=record.data.parentId;
    var mac=record.data.mac;
    
    if(parentId){
        return String.format('<a href="#" onclick="showCcmts({0}, \'{1}\')">{2}</a>',parentId, mac, value);
    }else{
        return String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
                record.data.entityId, record.data.ip, value);
    }
}
function renderSnmp(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/snmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/nosnmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderManagement(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/online.gif" border=0 align=absmiddle>',
			I18N.COMMON.manageble);	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/offline.gif" border=0 align=absmiddle>',
			I18N.COMMON.unmanageble);	
	}
}
function renderAgent(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/agent.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/noagent.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/online.gif" border=0 align=absmiddle>',
			I18N.COMMON.online);	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/offline.gif" border=0 align=absmiddle>',
			I18N.COMMON.unreachable);	
	}
}
function renderLevel(value, p, record) {
	switch (value) {
		case 1:
			return String.format('<img style="cursor:hand" alt="{0}" src="../images/fault/level1.gif" border=0 align=absmiddle>', I18N.EVENT.infoLevel);			
		case 2:
			return String.format('<img style="cursor:hand" alt="{0}" src="../images/fault/level2.gif" border=0 align=absmiddle>', I18N.EVENT.warningLevel);			
		case 3:
			return String.format('<img style="cursor:hand" alt="{0}" src="../images/fault/level3.gif" border=0 align=absmiddle>', I18N.EVENT.minorLevel);			
		case 4:
			return String.format('<img style="cursor:hand" alt="{0}" src="../images/fault/level4.gif" border=0 align=absmiddle>', I18N.EVENT.majorLevel);			
		case 5:
			return String.format('<img style="cursor:hand" alt="{0}" src="../images/fault/level5.gif" border=0 align=absmiddle>', I18N.EVENT.criticalLevel);			
		default:
			return String.format('<img alt="{0}" src="../images/fault/{1}.gif" border=0 align=absmiddle>', I18N.EVENT.normal, 'normal');		
	}
}

Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = '../images/s.gif';

	contextMenu = new Ext.menu.Menu({id:'context-menu', items:[
        {text:I18N.COMMON.view, id:'view-menu1', menu:[
		{text:I18N.NETWORK.icon, checked:false, group:'view1', handler:onViewByIconClick},
		{text:I18N.NETWORK.detail, checked:true, group:'view1', handler:onViewByDetailClick}]}, '-',					 
       	{text:I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler:onRefreshClick}, '-',
		{text:I18N.COMMON.selectAll, iconCls:'bmenu_selectall', handler:onSelectAllClick}, '-',      	
		{text:I18N.COMMON.print, iconCls:'bmenu_print', handler:onPrintClick}
	]});

	var physicalPaneItem = new Ext.menu.Item({text:I18N.NETWORK.physicalPane, handler:onPhysicalPaneClick});
	var portFlowItem = new Ext.menu.Item({text:I18N.NETWORK.portRealFlow, handler : onPortRealStateClick,
		disabled:true});
	var interfacesItem = new Ext.menu.Item({text:I18N.NETWORK.entityInterfaces, handler : onInterfacesClick,
		disabled:true});        	
    var entityViewMenu = [
		{text:I18N.COMMON.snap, handler : onEntitySnapClick}, '-', 
		physicalPaneItem, interfacesItem, '-',
		{text:I18N.NETWORK.cpuMemInfo, handler : onCpuMemClick} , portFlowItem
	];

	entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 150, items:[
		{text:I18N.COMMON.view, handler : onEntitySnapClick}, '-',
        {text:I18N.NETWORK.tool, menu:[
			{text:I18N.NETWORK.ping, handler:onPingClick}, {text:I18N.NETWORK.tracert, handler:onTracertClick}]},
		{text:I18N.NETWORK.discoveryAgain, handler: onDiscoveryAgainClick}, "-",
        //{text:I18N.COMMON.remove, iconCls:'bmenu_delete', handler:onDeleteClick}, '-',
        {text:I18N.NETWORK.property, handler: onEntityPropertyClick}		           
	]});

	store = new Ext.data.JsonStore({
	    url: storeUrl,
	    root: 'data', totalProperty: 'rowCount', idProperty: "entityId",
	    remoteSort: true,
	    fields: ['entityId', 'name', 'ip', 'modelName', 'type', 'mac', 'state', 'alert', 'snmpSupport', 'agentInstalled','snapTime','parentId','mac']
	});
    store.setDefaultSort('name', 'asc');

	var toolbar = [
		//{text:I18N.COMMON.create, handler:onNewEntityClick, iconCls:'bmenu_new'}, '-',
		//{text: I18N.COMMON.deleteAction, iconCls: 'bmenu_delete', handler:onDeleteClick, disabled : !editDevicePower}, '-',
		{text: I18N.COMMON.find, iconCls: 'bmenu_find', handler:onFindClick}, '-',
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}
	];

    grid = new Ext.grid.GridPanel({
		store:store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: false,
        columns:columnModels,
		viewConfig: {forceFit:true, enableRowBody:true, showPreview:false},
        tbar: toolbar,
        bbar: buildPagingToolBar(),
		renderTo: document.body
    });

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		var record = grid.getStore().getAt(rowIndex);  // Get the Record
		if (record.data.type <= 6) {
			physicalPaneItem.enable();
		} else {
			physicalPaneItem.disable();
		}
		if (record.data.snmpSupport) {
			portFlowItem.enable();
			interfacesItem.enable();		
		} else {
			portFlowItem.disable();
			interfacesItem.disable();		
		}
		if (record.data.agentInstalled) {
		} else {
		}
		entityMenu.showAt(e.getPoint());
    });
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);  // Get the Record
   		onEntitySnapClick(record.data.entityId, record.data.ip);		
    });    
	store.on('load', loadCallback);
    new Ext.Viewport({layout:'fit', items:[grid]});
	store.load({params:{start: 0, limit: pageSize}});
});

function loadCallback(store, records, options) {
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({pageSize:pageSize, store:store, displayInfo:true,
		items:['-', buildPageBox(pageSize)]});
	return pagingToolbar;
}
function buildPageBox(page) {
	return String.format(I18N.COMMON.displayPerPage, page);
}
function getSelectedEntityId() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data.entityId;
	}
	return null;
}
function getSelectedEntity() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data;
	}
	return null;
}
function onPhysicalPaneClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.NETWORK.physicalPane + " - " + entity.ip, 660, 450,
			"network/getPhysicalPane.tv?entityId=" + entity.ip, null, true, true);
	}
}

function onCpuMemClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		window.parent.addView('cpumem-' + entity.entityId,
			I18N.NETWORK.cpuMemInfo + ' - ' + entity.ip,
			'entityTabIcon', 'entity/viewCpuMemInfo.tv?entityId=' + entity.entityId +
			'&ip=' + entity.ip);
	}
}
function onPortRealStateClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();	
		window.parent.addView('interfaces-' + entity.entityId,
			I18N.NETWORK.portRealFlow + ' - ' + entity.ip,
			'entityTabIcon', 'link/showPortFlowInfo.tv?entityId=' + entity.entityId +
			'&ip=' + entity.ip);
	}
}
function onInterfacesClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();	
		window.parent.addView('interfaces-' + entity.entityId,
			I18N.NETWORK.entityInterfaces + ' - ' + entity.ip,
			'entityTabIcon', 'link/showEntityInterfacesJsp.tv?entityId=' + entity.entityId +
			'&ip=' + entity.ip);
	}
}

function onAssetDetailClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.NETWORK.assetDetail + " - " + entity.ip, 660, 450,
		"asset/getAssetDetail.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onSofrwareInstalledClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.NETWORK.softwareInstalled + " - " + entity.ip, 660, 440,
		"asset/getSoftwareInstalled.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onDiscoveryAgainClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.top.discoveryEntityAgain(entity.entityId, entity.ip, function() {
			onRefreshClick();
		});
	}
}
function onEntitySnapClick() {
	var entity = getSelectedEntity();
	var parentId=entity.parentId;
    var mac=entity.mac;
    
    if(parentId){
    	showCcmts(parentId,mac);
    }else{
    	showEntitySnap(entity.entityId, entity.ip);
    }
	/*if (entity) {
		showEntitySnap(entity.entityId, entity.ip);
	}*/
}
function onViewEntityEventClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.COMMON.event + " - " + entity.ip, 660, 450,
		"fault/showEventByEntityId.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onViewAlarmClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.COMMON.alarm + " - " + entity.ip, 660, 450,
		"fault/getAlarmByEntityId.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onViewEntityMonitorClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.COMMON.monitor + " - " + entity.ip, 660, 450,
		"monitor/showMonitorByEntityId.tv?entityId=" + entityId, null, true, true);
	}
}
function onReportClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.COMMON.report + " - " + entity.ip, 660, 450,
		"report/showReportByEntityId.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onVulnerabilityClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.COMMON.vulnerability + " - " + entity.ip, 660, 450,
		"security/showVulnByEntityId.tv?entityId=" + entity.entityId, null, true, true);
	}
}

function onPingClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", 'Ping' + " - " + entity.ip, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entity.ip, null, true, true);
	}
}
function onTracertClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", 'Ping' + " - " + entity.ip, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entity.ip, null, true, true);
	}
}
function onNetsendClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.NETWORK.netsend + " - " + entity.ip, 500, 350,
		"network/netsend.jsp?ip=" + entity.ip, null, true, true);
	}
}
function onMibBrowseClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("modalDlg", I18N.NETWORK.mibBrowser + " - " + entity.ip, 700, 500,
		"network/showMibBrowser.tv?entityId=" + entity.entityId, null, true, true);
	}
}
function onAddToGoogleClick() {
	window.parent.addView("ngm", I18N.MAIN.googleMap, "googleIcon", "google/showEntityGoogleMap.tv");
	/*var entity = getSelectedEntity();
	if (entity) {
		window.parent.createDialog("googleDlg", I18N.COMMON.addToGoogle, 600, 450, "google/add2GoogleMap.tv", null, true, true,
            function(){
                if (window.top.ZetaCallback.type != 'ok') {return;}
	            Ext.Ajax.request({url: '../google/saveEntity2GoogleMap.tv',
	                params: {entityId: entity.entityId, latitude:window.top.ZetaCallback.lat, 
	                	longitude:window.top.ZetaCallback.lng, zoom:window.top.ZetaCallback.zoom},
	                success: function() {},
	                failure: function(){window.parent.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.add2GoogleMapError, 'error');}
	            })
	            window.parent.ZetaCallback = null;
        });
	}*/
}
function onEntityPropertyClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.showProperty("entity/showEntityPropertyJsp.tv?entityId=" + entity.entityId);
	}
}

function onNewMonitorClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.newMonitor(entity.entityId, entity.ip);
	}
}
function onScanEntityClick() {
}
function onNewEntityClick() {
	window.parent.createDialog('modalDlg', I18N.COMMON.newEntity, 420, 450,
	'entity/showNewEntity.tv', null, true, true);
}	
function onNewTopoFolderClick() {
	window.top.openNewTopoFolder();
}
function onPrintClick() {
	window.print();
}
function onMaxViewClick() {
	window.parent.enableMaxView();
}
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.top.showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.confirmDeleteEntity, function(type) {
			if (type == 'no') {return;}
			Ext.Ajax.request({url: '../entity/moveEntiyToRecyle.tv',
			   success: function() {onRefreshClick();},
			   failure: function(){window.top.showMessageDlg(I18N.error, I18N.deleteFailure, 'error')},
			   params: {entityIds:entityIds}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSelected);
	}
}
function onFindClick() {
	window.parent.showInputDlg(I18N.NETWORK.findEntity, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim();
		if(match=='') {window.parent.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag=true;
		var records = [];
		store.each(function(record) {
			if (record.data.name.indexOf(match) != -1 || record.data.ip.indexOf(match) != -1) {
				records.push(record);
				flag = false;
			}
		});
		if (flag) {window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);}
		else {grid.getSelectionModel().selectRecords(records);}	   	
	});
}
function onRefreshClick() {
	store.reload();
}
function onViewByIconClick() {
	location.href = urlByIcon;	
}
function onViewByDetailClick() {
}
function onSelectAllClick() {
	var sm = grid.getSelectionModel();
	if (sm != null) {
		sm.selectAll();
	}	
}

function startTimer() {
	refreshInterval = setInterval('onRefreshClick()', dispatcherInterval);
}