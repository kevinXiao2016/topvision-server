/**
 * All entity view by detail.
 *
 * author:niejun
 */
//var contextMenu = null;
var entityMenu = null;
var entityMenuMutl = null;
var grid = null;
var store = null;
var pagingToolbar = null;
var refreshInterval = null;

function showEntitySnap(id, ip) {
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-'+id,  ip , 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
}
function showCmcEntitySnap(id, ip){
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-'+id,   ip , 'entityTabIcon',
			'/cmc/showCmcPortal.tv?cmcId=' + id);
}

function defaultFailureCallback(response) {
	window.parent.showErrorDlg();
}
function defaultSuccessCallback(response) {
}
function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url, failure: fn, success: sn,timeout:1800000, params: param});
}

function renderName(value, p, record){
	if(EntityType.isCcmtsWithoutAgentType(record.json.type)){
		return String.format('<a style="padding-left:10px;" href="#" onclick="showCmcEntitySnap({0}, \'{1}\')">{2}</a>',
				record.data.parentId, record.data.name, value);
	}else if(EntityType.isOnuType(record.json.typeId)){
		return String.format('<span style="padding-left:10px; color:#666;">{0}</span>',value);
	}else if(record.json.type == -1){
		return String.format('<span style="padding-left:10px; color:#666;">{0}</span>',value);
	}else{
		return String.format('<a style="padding-left:10px;" href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
				record.data.entityId, record.data.name, value);
	}
}

function showApDetail(ip){
	window.open("http://"+ ip); 
}
function renderSnmp(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/snmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/nosnmp.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderAgent(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/agent.gif" border=0 align=absmiddle>',
			I18N.COMMON.supported);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/noagent.gif" border=0 align=absmiddle>',
			I18N.COMMON.unsupported);	
	}
}
function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			I18N.COMMON.online);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
			I18N.COMMON.offline);	
	}
}

function renderManagement(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/online.gif" border=0 align=absmiddle>',
			I18N.COMMON.manageble);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/offline.gif" border=0 align=absmiddle>',
			I18N.COMMON.unmanageble);	
	}
}

function renderLevel(value, p, record) {
	switch (value) {
		case 1:
			return String.format('<img  nm3kTip="{0}" class="nm3kTip" src="../images/fault/level1.gif" border=0 align=absmiddle>', I18N.EVENT.infoLevel);			
		case 2:
			return String.format('<img  nm3kTip="{0}" class="nm3kTip" src="../images/fault/level2.gif" border=0 align=absmiddle>', I18N.EVENT.warningLevel);			
		case 3:
			return String.format('<img  nm3kTip="{0}" class="nm3kTip" src="../images/fault/level3.gif" border=0 align=absmiddle>', I18N.EVENT.minorLevel);			
		case 4:
			return String.format('<img  nm3kTip="{0}" class="nm3kTip" src="../images/fault/level4.gif" border=0 align=absmiddle>', I18N.EVENT.majorLevel);			
		case 5:
			return String.format('<img  nm3kTip="{0}" class="nm3kTip" src="../images/fault/level5.gif" border=0 align=absmiddle>', I18N.EVENT.criticalLevel);			
		default:
			return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/{1}.gif" border=0 align=absmiddle>', I18N.EVENT.normal, 'normal');		
	}
}

function loadCallback(store, records, options) {
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
function getSelectedRecord() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record;
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
			'entityTabIcon', 'realtime/viewCpuMemInfo.tv?entityId=' + entity.entityId +
			'&ip=' + entity.ip);
	}
}
function onPortRealStateClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();	
		window.parent.addView('interfaces-' + entity.entityId,
			I18N.NETWORK.portRealFlow + ' - ' + entity.ip,
			'entityTabIcon', 'realtime/showPortFlowInfo.tv?entityId=' + entity.entityId +
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
function onDiscoveryAgainClick() {
	var entity = getSelectedEntity();
	var record = getSelectedRecord();
	if(EntityType.isCcmtsWithoutAgentType(record.json.typeId)){
	//if(EntityType.isCcmtsWithoutAgentType(record.json.type)){
		window.parent.showWaitingDlg("@COMMON.wait@", String.format("@network/NETWORK.reTopoing@", entity.mac),'waitingMsg','ext-mb-waiting');
		sendRequest('../cmc/refreshCC.tv', 'POST',{cmcId: entity.entityId, cmcType:record.json.type, entityId:entity.parentId}, function(response) {
 			onRefreshClick();
 	        if(response.responseText == 'success'){
	 	       	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
	   			});
 	        }else{
 	        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
 	        }
		}, function() {
			window.parent.closeWaitingDlg();
			window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.notDiscoveryEntityAgain@");
		});
		return;
	}
	if (entity) {
		window.top.discoveryEntityAgain(entity.entityId, entity.ip, function() {
			onRefreshClick();
		});
	}
}
function onEntitySnapClick() {
	var entity = getSelectedEntity();
	var record = getSelectedRecord();
	if(EntityType.isCcmtsWithoutAgentType(record.json.type)){
		showCmcEntitySnap(entity.parentId, record.data.name);
		return;
	}
	if (entity) {
		showEntitySnap(entity.entityId, entity.name);
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
		window.parent.createDialog("modalDlg", 'Tracert ' + entity.ip, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + entity.ip, null, true, true);
	}
}
function onMibbleBrowserClick(){
	var entity = getSelectedEntity();
	if(entity)
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv?host="+entity.ip,null,true)
	else
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv",null,true)
}

function onNativeTelnetClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		window.open('telnet://' + entity.ip, 'ntelnet' + entity.entityId);
	}
}
function onAddToGoogleClick() {
	window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
	return true;
}
function onMoveToClick() {
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	var entityId = selections[0].data.entityId;
	window.top.createDialog('editTopoFolder', I18N.COMMON.editFolder, 800, 500, '/entity/editTopoFolder.tv?entityId='+entityId, null, true, true);
}
function onEntityPropertyClick() {
	/*var entity = getSelectedEntity();
	if (entity) {
		//window.top.showproperty("entity/showEntityPropertyJsp.tv?entityId=" + entity.entityId);
	}*/
}

function onViewConfigClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		window.top.addView('entity-' + entity.entityId,   entity.name ,
			'entityTabIcon', 'entity/showEditEntityJsp.tv?entityId=' + entity.entityId);
	}
}

function onScanEntityClick() {
}
function onNewEntityClick() {
	window.parent.createDialog('modalDlg', I18N.COMMON.newEntity, 360, 230, 'entity/showNewEntity.tv?folderId=' + topoFolderId, null, true, true);
}	
function onNewTopoFolderClick() {
	window.top.openNewTopoFolder();
}
function onPrintClick() {
	window.print();
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
			Ext.Ajax.request({url: '/topology/deleteCellByIds.tv',
			   success: function(para) {
				   var json = $.parseJSON(para.responseText);
				   var successEntityIds = json.successEntityIds || [],
				       successEdgeIds = json.successEdgeIds || [],
				       successFolderIds = json.successFolderIds || [],
				       failedEntityNames = json.failedEntityNames || [],
				       failedFolderNames = json.failedFolderNames || [];
				   
				   if(failedEntityNames.length > 0 || failedFolderNames.length > 0){
						var str = '';
							if(failedEntityNames.length > 0){
						    	str += '<p>@network/topo.failedEntityNames@</p>';
						    	str += failedEntityNames.toString();
							}
							if(failedFolderNames.length > 0){
						    	str += '<p>@network/topo.failedFolderNames@</p>';
						    	str += failedFolderNames.toString();
							}
							top.afterSaveOrDelete({
								title : '@network/topo.failedTit@',
								html : str
							});
					};
				   onRefreshClick();
			   },
			   failure: function(){window.top.showMessageDlg(I18N.error, I18N.deleteFailure, 'error')},
			   params: {folderId: topoFolderId, nodeIds: entityIds}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSelected);
	}
}
function onFindClick() {
	window.parent.showInputDlg(I18N.COMMON.find, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim().toLowerCase();
		if(match=='') {window.parent.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag=true;
		var records = [];
		store.each(function(record) {
            var userObj = record.data;
            if ((userObj.name && userObj.name.toLowerCase().indexOf(match) != -1) ||
                (userObj.ip && userObj.ip.indexOf(match) != -1)||
                (userObj.sysName && userObj.sysName.toLowerCase().indexOf(match) != -1)) {
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
		document.detachEventListener('selectstart', returnFalse, false);
		document.detachEventListener('keydown', doOnKeyDown, false);
	}else{
		window.detachEvent('onbeforeprint', doOnBeforePrint);
		window.detachEvent('onafterprint', doOnAfterPrint);
		document.detachEvent('onselectstart', returnFalse);
		document.detachEvent('onkeydown', doOnKeyDown);
	}
}
if(document.addEventListener){
	window.addEventListener('beforeprint', doOnBeforePrint, false);
	window.addEventListener('afterprint', doOnAfterPrint, false);
	document.addEventListener('selectstart', returnFalse, false);
	document.addEventListener('keydown', doOnKeyDown, false);
	window.addEventListener('load', doMainOnload, false);
	window.addEventListener('unload', doMainOnUnload, false);
}else{
	window.attachEvent('onbeforeprint', doOnBeforePrint);
	window.attachEvent('onafterprint', doOnAfterPrint);
	document.attachEvent('onselectstart', returnFalse);
	document.attachEvent('onkeydown', doOnKeyDown);
	window.attachEvent('onload', doMainOnload);
	window.attachEvent('onunload', doMainOnUnload);
}

Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = '../images/s.gif';


 	var viewMenu = new Ext.menu.Menu({id:'view-menu', items:[
 		{text: I18N.NETWORK.topoMap, group:'view', checked: false,checkHandler: onViewByMapClick},
//        {text: I18N.COMMON.icon, group:'view', checked: false,checkHandler: onViewByIconClick},
		{text: I18N.COMMON.detail, group:'view', checked: true, checkHandler:onViewByDetailClick}]});

	var reader = new Ext.data.JsonReader({
	    totalProperty: "rowCount",
	    idProperty: "entityId",
	    root: "data",
        fields: [
			{name: 'entityId'},
		    {name: 'name'},
		    {name: 'typeName'},
		    {name: 'type'},
		    {name: 'typeId'},
		    {name: 'ip'},
		    {name: 'mac'},
		    {name: 'status'},
		    {name: 'snapTime'},
		    {name: 'state'},
		    {name: 'module'},
			{name: 'alert'},
			{name: 'parentId'},
		    {name: 'snmpSupport'},
			{name: 'agentInstalled'},
			{name: 'url'},
			{name: 'uplinkDevice'}
        ]	    
	});
	
	var cmConfig = CustomColumnModel.init(saveColumnId,columnModels,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'name', direction: 'ASC'};
		
	store = new Ext.data.GroupingStore({
       	url: storeUrl,
       	baseParams:{
       		displaySubnet:DISPLAY_SUBNET
       	},
       	reader: reader,
		remoteGroup: false,
		remoteSort: false,
		sortInfo: {field: 'entityId', direction: "ASC"},
		groupField: 'typeName',
		groupOnSort: false
    });
	store.setDefaultSort(sortInfo.field, sortInfo.direction);

    var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "' + I18N.COMMON.items + '" : "'+ I18N.COMMON.item + '"]})';

	var toolbar = [
	    //{text:I18N.COMMON.create, handler:onNewEntityClick, iconCls:'bmenu_new'}, '-',
        //{text: I18N.COMMON.deleteAction, iconCls: 'bmenu_delete', handler:onDeleteClick, disabled : !editTopoPower}, '-',
        {text: I18N.COMMON.find,cls:'mL10', iconCls: 'bmenu_find', handler:onFindClick}, '-',
        //{text: I18N.COMMON.view, iconCls: 'bmenu_view', menu: viewMenu}
        {text: I18N.topo.grah, iconCls: 'bmenu_pic', handler: onViewByMapClick},'-',
        {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick},'-',
        {id:'subDeviceBtn',text: '@network/topo.subDevice2@', iconCls:'',handler:showHideSubDevice}
	];
    	
    grid = new Ext.grid.GridPanel({
		store:store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: false,
        cm : cm,
        bodyCssClass:'normalTable',
        view: new Ext.grid.GroupingView({
            forceFit:true,
            hideGroupedColumn:true,
            enableNoGroups:true,
            groupTextTpl: groupTpl
        }),
        enableColumnMove : true,
        listeners : {
        	afterRender : function(){
				var cls;
				if(DISPLAY_SUBNET){
					cls = 'miniIcoSubDeviceCheck';
					Ext.getCmp('subDeviceBtn').addClass('borderLine');
				}else{
					cls = 'miniIcoSubDeviceUncheck';
				}
				Ext.getCmp('subDeviceBtn').setIconClass(cls)
			},
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
			}
        },
        tbar: toolbar,
		renderTo: document.body
    });
    store.on("load",function(){
    	selectOneRow();
    })
    
 /*   grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
   		var sm = grid.getSelectionModel();
        var nodes = sm.getSelections();
        var record = grid.getStore().getAt(rowIndex);  // Get the Record
        if (nodes != null && nodes.length > 1){
            getEntityMenuMutl(record).showAt(e.getPoint());
            return;
        }
        if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex);
		}
		getEntityMenu(record).showAt(e.getPoint());
    });  */
    
	store.on('load', loadCallback);
    new Ext.Viewport({layout:'fit', items:[grid]});
	store.load();

    $.ajax({url: '/topology/getTopologyStateFirstly.tv', dataType: 'json', cache: false,
        data: {folderId: topoFolderId, folderPath: '1/5/', entityLabel: 'cpu', linkLabel: ''},
        success: dispatcherHandler,
        error: defaultCallback//function(){},nothing happened
    });
});
function showHideSubDevice(){
	var $subDeviceBtn = Ext.getCmp('subDeviceBtn'), 
	    btnEl  = $subDeviceBtn.getEl(),
	    DISPLAY_SUBNET = true;
	
	if(btnEl.hasClass("borderLine")){
		DISPLAY_SUBNET = false;
		btnEl.removeClass("borderLine");
		$subDeviceBtn.setIconClass("miniIcoSubDeviceUncheck");
	}else{
		btnEl.addClass("borderLine");
		$subDeviceBtn.setIconClass("miniIcoSubDeviceCheck");
	}
	$.ajax({
		url: '/topology/saveToolView.tv', cache:false, 
		data: {displaySubnet:DISPLAY_SUBNET},
		success: function() {
			store.baseParams =  {
				displaySubnet : DISPLAY_SUBNET
			};
			store.reload();
		}
	}); 
}

function dispatcherHandler(json) {

}
function defaultCallback(json) {

}
function selectOneRow(){
	if( Number(entityId)==0 ){return;}
	var itemsArr = store.data.items;
	for(var i=0; i<itemsArr.length; i++){
		if( Number(itemsArr[i].data.entityId) == Number(window.entityId) ){
			window.entityId = 0;
			grid.getSelectionModel().selectRow(i);
		}
	}
}

function cmtsPerfManage(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
	window.parent.addView('cmcPerfParamConfig', I18N.CMC.title.performanceCollectConfig,"bmenu_preference",'/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + entityId);
}

//拓扑图列表页面是打开的。再次点击了“设备拓扑定位”;
function goToEntity(paraId){
	if(grid != null){
		try{
			window.entityId = paraId;
			selectOneRow();
		}catch(err){
			
		}
	}
};

function getEntityMenu(record) {
	var status = record.data.status;
	var type = record.data.type;
	var typeId = record.data.typeId;
	var isTypeA = EntityType.isCcmtsWithoutAgentType(typeId);//判断是不是A型或CA型设备;
    //if (entityMenu == null) {
    var items = [];
    if(!EntityType.isOnuType(typeId) && !EntityType.isUnkown_Type(typeId)){
    	//items[items.length] = {text: I18N.COMMON.view,handler: onEntitySnapClick};
    	if(!isTypeA){
	    	items[items.length] = {text: I18N.NETWORK.tool, id:'pingTool', menu: [
	    	    {text: I18N.NETWORK.ping, handler: onPingClick},
		        {text: I18N.NETWORK.tracert, handler: onTracertClick},
		        {text : "Mibble Browser",handler : onMibbleBrowserClick}
		        //{text: I18N.NETWORK.nativeTelnet, handler: onNativeTelnetClick}
		    ]};
    	}
    	
    	
    	items[items.length] = {text: "@network/NETWORK.reTopo@", id:'refresh',iconCls: 'bmenu_refresh', handler: onDiscoveryAgainClick,disabled:!operationDevicePower};
	 //--------get additional menu dynamically----//
    	var entityId = record.data.entityId
    	var ip = record.data.ip
    	if(EntityType.isCcmtsType(record.data.typeId)) {
	        var cmcId = record.data.parentId == null || record.data.parentId == "" ? -1 : record.data.parentId;
	        var ccMac = record.data.mac;
	        var typeId = record.data.typeId;
	        $.ajax({
	            type: "GET",
	            url:  String.format("/{0}/js/extendMenu.js",record.data.module),
	            dataType: "script",
	            async : false,
	            success  : function () {
	                // menuArray , entityId,ip 是必须的,不能为空
	                items = extendOper(items, entityId, cmcId , ccMac , typeId);
	            }
	        });
        }else if(EntityType.isCmtsType(record.data.typeId)){
        	items[items.length] = '-';
        	items[items.length] = {text: I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:cmtsPerfManage};
        }else {
            $.ajax({
                type: "GET",
                url: String.format("/{0}/js/extendMenu.js",record.data.module),
                dataType: "script",
                async : false,
                success  : function () {
                    // menuArray , entityId,ip 是必须的,不能为空
                    items = extendOper(items, entityId, ip);
                }
            });
        }
     	items[items.length] = '-';
     	items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};
    }
    if (editTopoPower) {
        /*if (googleSupported) {
            items[items.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:!operationDevicePower};
        }*/
        items[items.length] = '-';
        items[items.length] = {text:I18N.COMMON.editFolder, handler: onMoveToClick};
        items[items.length] = {text:I18N.COMMON.removeFromTopo, iconCls:'bmenu_delete', handler : onDeleteClick};
        items[items.length] = {id:"getEntityMenu",text: I18N.COMMON.deviceInfo, handler: renameEntity};//重命名
    }
    items[items.length] = '-';
    //items[items.length] = {text: I18N.COMMON.property, handler : onEntityPropertyClick};
     //entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    //}
     
     if (!entityMenu) {
         entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
     } else {
     	//---存在bug--//
     	entityMenu.removeAll();
     	entityMenu.add(items)
     }
     
    if(!EntityType.isOnuType(typeId) && EntityType.isUnkown_Type(typeId)){
    	if(status){
    		if(!isTypeA){
    			Ext.getCmp("pingTool").show();
    		}
        	Ext.getCmp("refresh").show();
        }else{
        	Ext.getCmp("pingTool").hide();
        	Ext.getCmp("refresh").hide();
        }
    }
    return entityMenu;
}

function renameEntity() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    window.top.createDialog('renameEntity', I18N.COMMON.deviceInfo, 600, 375,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function changeEntityName(entityId, name) {
    store.reload();
}

function getEntityMenuMutl(record) {
    if (entityMenuMutl == null) {
        var items = [];
        if (editTopoPower) {
            items[items.length] = {id:"quicksetmult",text: I18N.COMMON.quickSet, handler: onQuickSet};
            items[items.length] = {text:I18N.COMMON.editFolder, handler: onMoveToClick};
            items[items.length] = {text:I18N.COMMON.remove, iconCls:'bmenu_delete', handler : onDeleteClick};
        }
        entityMenuMutl = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    }
    var quickset = Ext.getCmp('quicksetmult');
    if (record.json.type == I18N.COMMON.AP) {
        quickset.enable();
    } else {
        quickset.disable();
    }
    return entityMenuMutl;
}

function onQuickSet() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.parent.createDialog("quickSetDlg", I18N.COMMON.quickSet, 600, 450, "/ap/showQuickSetWnd.tv", null, true, true);
    }
}
function onViewByMapClick() {
	//location.href = 'getTopoMapByFolderId.tv?viewType=map&folderId=' + topoFolderId;
	location.href = "showNewTopoDemo.tv?viewType=map&folderId=" + topoFolderId;
}
function onViewByIconClick() {
	location.href = 'getTopoMapByFolderId.tv?viewType=icon&folderId=' + topoFolderId;
}
function onViewByDetailClick() {
}
function onOpenURLClick() {
	var entity = getSelectedEntity();
	if (entity != null) {
		var entityId = entity.entityId;
		var url = entity.url;
		if (url != null && url != '') {
//			var evt = ZetaUtils.getEvent();
//			if (evt.shiftKey) {
//				window.open(url);
//			} else {
            Ext.menu.MenuMgr.hideAll();
            if (!url.startWith("http://") && !url.startWith("https://")) {
                url = "http://"+url;
            }
//				window.open(url);
            var testHost = matchHost(url);
			if(testHost == true){
            	window.top.addView('url' + entityId, 'URL',  "topoLeafIcon", url);
			}else{
				window.open(url);
			}
//			}
		} else {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.GRAPH.hrefMessage);
			onVertexPropertyClick();
		}
	}
}
//检测域名，是否在同一域名下;
function matchHost(url){
	var hostUrl = top.window.location.host;
	var arr = url.split("/");
	var myUrl = arr[2];
	if(myUrl == hostUrl){
		return true;
	}
	return false;
};
function onRenameClick() {
    window.top.showInputDlg(I18N.MENU.rename, I18N.NETWORK.newName, renameFolderCallback);
}
function renameFolderCallback(type, text) {
    if (type == "cancel") return;
    var entity = getSelectedEntity();
    if (entity != null) {
        if (text == null || text == "") {
            window.parent.showMessageDlg(I18N.NETWORK.error,I18N.NETWORK.nameCannotEnmpty,"error");
            return;
        }
        Ext.Ajax.request({url: '../entity/renameEntity.tv',
            params: {folderId: topoFolderId, entityId: entity.entityId, name: text},
            success: function() {
                onRefreshClick();
            },
            error: function() {
                window.parent.showErrorDlg();
            }
        });
    }
}
//打开超链接，由于没有侧边栏，所以这个函数是空的。在拓扑图图形里面是会打开侧边栏的;
function onVertexPropertyClick(selectObj){}
