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
	window.parent.addView('entity-'+id, I18N.COMMON.entity + ' - ' + ip, 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
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
	return String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
		record.data.entityId, record.data.ip, value);
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
function renderAgent(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/network/agent.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img alt="{0}" src="../images/network/no_agent.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/network/online.gif" border=0 align=absmiddle>',
			I18N.COMMON.online);	
	} else {
		return String.format('<img alt="{0}" src="../images/network/offline.gif" border=0 align=absmiddle>',
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

	var entityNewMenu = [{text:I18N.COMMON.monitor, handler : onNewMonitorClick}];

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

	if (Topvision.plugin.security) {
		entityNewMenu[1] = {text:I18N.COMMON.scan, handler : onScanEntityClick};
	}

	entityMenu = new Ext.menu.Menu({ id: 'entityMenu', items:[
		{text:I18N.COMMON.create, menu : entityNewMenu},
		{text:I18N.COMMON.view, menu : entityViewMenu}, '-',
		{text:I18N.NETWORK.discoveryAgain, handler: onDiscoveryAgainClick},
        {text:I18N.NETWORK.tool, menu:[
			{text:I18N.NETWORK.ping, handler:onPingClick}, {text:I18N.NETWORK.tracert, handler:onTracertClick}]}, "-",
        {text:I18N.COMMON.remove, iconCls:'bmenu_delete', handler:onDeleteClick}, '-',
        {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick}, '-',
        {text: I18N.NETWORK.property, handler:onEntityPropertyClick}		           
	]});

 	var viewMenu = new Ext.menu.Menu({id:'view-menu', items:[
        {text: I18N.COMMON.icon, group:'view',checked: false,checkHandler: onViewByIconClick},
		{text: I18N.COMMON.detail, group:'view',checked: true, checkHandler:onViewByDetailClick}]});

	var reader = new Ext.data.JsonReader({
	    totalProperty: "rowCount",
	    idProperty: "entityId",
	    root: "data",
        fields: [
			{name: 'entityId'},
		    {name: 'name'},
		    {name: 'typeName'},
		    {name: 'type'},
		    {name: 'ip'},
		    {name: 'mac'},
		    {name: 'snapTime'},
		    {name: 'state'},
			{name: 'alert'},
		    {name: 'snmpSupport'},
			{name: 'agentInstalled'}
        ]	    
	});

	store = new Ext.data.GroupingStore({
       	url: storeUrl,
       	reader: reader,
		remoteGroup: false,
		remoteSort: false,
		sortInfo: {field: 'name', direction: "ASC"},
		groupField: 'typeName',
		groupOnSort: false
    });

    var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "' + I18N.COMMON.items + '" : "'+ I18N.COMMON.item + '"]})';

	var toolbar = [new Ext.Toolbar.MenuButton(
		{text:I18N.COMMON.create, handler:onNewEntityClick, iconCls:'bmenu_new',  menu:{items:[
			{text:'<b>'+I18N.NETWORK.entity +'</b>', handler:onNewEntityClick},
			{text:I18N.MONITOR.monitor, handler:onNewMonitorClick}, '-',
			{text:I18N.NETWORK.topoFolder, handler:onNewTopoFolderClick}]}}), 
		{text: I18N.COMMON.print, iconCls: 'bmenu_print', handler:onPrintClick}, '-',
		{text: I18N.COMMON.deleteAction, iconCls: 'bmenu_delete', handler:onDeleteClick}, '-',
		{text: I18N.COMMON.find, iconCls: 'bmenu_find', handler:onFindClick}, 
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}];

    grid = new Ext.grid.GridPanel({
		store:store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: false,
        columns:columnModels,

        view: new Ext.grid.GroupingView({
            forceFit:true,
            hideGroupedColumn:true,
            enableNoGroups:true,
            groupTextTpl: groupTpl
        }),

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
    grid.on('contextmenu', function(e) {
    	e.preventDefault();
		contextMenu.showAt(e.getPoint());
    });
    grid.on('keydown', function(e) {
    	if (e.ctrlKey && e.getCharCode() == KeyEvent.VK_A) {
			onSelectAllClick();
    	}
    });
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);  // Get the Record
        showEntitySnap(record.data.entityId, record.data.ip);		
    });    
	store.on('load', loadCallback);
    new Ext.Viewport({layout:'fit', items:[grid]});
	store.load({params:{start:0, limit:pageSize}});
});

function loadCallback(store, records, options) {
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({pageSize:pageSize, store:store, displayInfo:true,
		items:['-', I18N.COMMON.perPage, buildPageBox(pageSize), I18N.COMMON.tiao]});
	return pagingToolbar;
}
function buildPageBox(page) {
	if (page == 50) {
		return '<select id="pageBox" style="width:60px;" onchange="pageBoxChanged(this);"><option value="25">25</option><option value="50" selected>50</option><option value="75">75</option><option value="100">100</option></select>';
	} else if(page == 75) {
		return '<select id="pageBox" style="width:60px;" onchange="pageBoxChanged(this);"><option value="25">25</option><option value="50">50</option><option value="75" selected>75</option><option value="100">100</option></select>';
	} else if(page == 100) {
		return '<select id="pageBox" style="width:60px;" onchange="pageBoxChanged(this);"><option value="25">25</option><option value="50">50</option><option value="75">75</option><option value="100" selected>100</option></select>';
	} else {
		return '<select id="pageBox" style="width:60px;" onchange="pageBoxChanged(this);"><option value="25">25</option><option value="50">50</option><option value="75">75</option><option value="100">100</option></select>';
	}
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	Ext.Ajax.request({url: '../system/setPageSize.tv',
		success: function() {
			pagingToolbar.pageSize = pageSize;
			pagingToolbar.doLoad(0);
		},
		failure: function(){},
		params: {pageSize: pageSize}
	});
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
		});
	}
}
function onEntitySnapClick() {
	var entity = getSelectedEntity();
	if (entity) {
		showEntitySnap(entity.entityId, entity.ip);
	}
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
	window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
}
function onEntityPropertyClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.showProperty("entity/showEntityPropertyJsp.tv?entityId=" + entity.entityId, true);
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

// global event handler
function doOnKeyDown() {
    var code = event.keyCode;
    var ctrl = event.ctrlKey;
    if (ctrl){
		if(code == KeyEvent.VK_P){window.print();}
		return;
	}
	if (code == KeyEvent.DEL){onDeleteClick();}
}
function doOnBeforePrint() {}
function doOnAfterPrint() {}
function doMainOnload() {
	refreshInterval = setInterval('onRefreshClick()', dispatcherInterval);
}
function doMainOnUnload() {
	if (refreshInterval != null) {clearInterval(refreshInterval);}
	if(document.addEventListener){
		window.detachEventListener('beforeprint', doOnBeforePrint, false);
		window.detachEventListener('afterprint', doOnAfterPrint, false);
		document.detachEventListener('keydown', doOnKeyDown, false);
	}else{
		window.detachEvent('onbeforeprint', doOnBeforePrint);
		window.detachEvent('onafterprint', doOnAfterPrint);
		document.detachEvent('onkeydown', doOnKeyDown);
	}
}
if(document.addEventListener){
	window.addEventListener('beforeprint', doOnBeforePrint, false);
	window.addEventListener('afterprint', doOnAfterPrint, false);
	document.addEventListener('keydown', doOnKeyDown, false);
	window.addEventListener('load', doMainOnload, false);
	window.addEventListener('unload', doMainOnUnload, false);
}else{
	window.attachEvent('onbeforeprint', doOnBeforePrint);
	window.attachEvent('onafterprint', doOnAfterPrint);
	document.attachEvent('onkeydown', doOnKeyDown);
	window.attachEvent('onload', doMainOnload);
	window.attachEvent('onunload', doMainOnUnload);
}