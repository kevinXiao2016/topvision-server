var contextMenu = null;
var grid = null;
var selectionModel;
var entityMenu = null
var store = null;
var pagingToolbar = null;
var UNKNOWN_TYPE = -1;
//var _queryText="";

function showEntitySnap(id, name) {
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-' + id,  name , 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
}

function defaultFailureCallback(response) {
	window.parent.showErrorDlg();
}
function defaultSuccessCallback(response) {
}
function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url+'?_='+Math.random(), failure: fn, success: sn,timeout:1800000,  params: param});
}

function renderName(value, p, record){
	if(!record.data.attention){
		if(EntityType.isCcmtsWithAgentType(record.data.typeId)){
            return  String.format('<a href="#" onclick="showCmc8800BSnap({0}, \'{1}\')">{2}</a>',
            		record.data.entityId, record.data.name, value);
		}
		if(EntityType.isUnkown_Type(record.data.typeId)){
			return  String.format('<a href="#" onclick="unknowClick({0}, \'{1}\')">{2}</a>',
					record.data.entityId, record.data.ip, value);
		}
		return  String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
				record.data.entityId, record.data.name, value);
	}
    if(EntityType.isCcmtsWithAgentType(record.data.typeId)){
		return  String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick="showCmc8800BSnap({0}, \'{1}\')">{2}</a>',
				record.data.entityId, record.data.name, value);
	}
	return  String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>',
			record.data.entityId, record.data.name, value);
}

function renderIp(value, p, record){
	if(value == ""){
		return "--";
	}
	return value;
}

function showApDetail(entityId,ip,value){
	window.open("http://"+ ip); 
}
function unknowClick(entityId,ip){
	window.parent.createDialog("entityBasicInfo", String.format("@NETWORK.entityBasicInfo@",ip),600,510,"portal/showUnknownEntityJsp.tv?entityId=" + entityId, null, true, true);
}
function renderSnmp(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/snmp.gif" border=0 align=absmiddle>',"@resources/COMMON.supported@");	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/nosnmp.gif" border=0 align=absmiddle>',"@resources/COMMON.unsupported@");	
	}
}
function renderAgent(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/fault/agent.gif" border=0 align=absmiddle>',
			"@resources/COMMON.supported@");	
	} else {
		return String.format('<img alt="{0}" src="../images/fault/no_agent.gif" border=0 align=absmiddle>',
			"@resources/COMMON.unsupported@");	
	}
}
function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
			"@resources/COMMON.unreachable@");	
	}
}

function renderManagement(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/online.gif" border=0 align=absmiddle>',
			"@resources/COMMON.manageble@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/offline.gif" border=0 align=absmiddle>',
				"@resources/COMMON.unmanageble@");	
	}
}

function loadCallback(store, records, options) {}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({pageSize: pageSize, store:store, displayInfo:true, cls:"extPagingBar",
		items: ["-", String.format("@COMMON.pagingTip@", pageSize), '-']});
	return pagingToolbar;
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value; 
	if(p == pageSize) return;
	pageSize = parseInt(p);
	pagingToolbar.pageSize = pageSize;
	pagingToolbar.doLoad(0);
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
		window.parent.createDialog("modalDlg", "@NETWORK.physicalPane@ - " + entity.ip, 660, 450,
			"network/getPhysicalPane.tv?entityId=" + entity.ip, null, true, true);
	}
}

function onCpuMemClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		showCpuAndMem(entity.entityId, entity.ip);
	}
}
function onPortRealStateClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		showPortRealState(entity.entityId, entity.ip);
	}
}
function onInterfacesClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();	
		window.parent.addView('interfaces-' + entity.entityId,
			"@NETWORK.entityInterfaces@ - " + entity.ip,
			'entityTabIcon', 'link/showEntityInterfacesJsp.tv?entityId=' + entity.entityId +
			'&ip=' + entity.ip);
	}
}

function onMenuRefreshClick(){
	var entity = getSelectedEntity();
	if (entity) {
		if(showRefreshTip == true || showRefreshTip == 'true'){//如果后台保存这个属性，则不出现提示;
			onMenuRefreshClick2(entity);
		}else{
			top.Ext.Msg.show({
				   title:'@COMMON.tip@',
				   msg: "@COMMON.refreshTip@",
				   buttons  : {
					   ok : '@COMMON.Iknow@',
					   yes : '@COMMON.stopBugging@'
				   },
				   fn: function(type){
					   if(type == "ok" || type == 'cancel'){
						   onMenuRefreshClick2(entity);
					   }else if(type == 'yes'){
						   showRefreshTip = true;
						   $.ajax({
								url : '/network/saveEntitySnapView.tv',
								type : 'POST',
								data : {
									showRefreshTip: true
								},
								dataType : 'json',
								success : function(json) {
								},
								error : function(json) {
								},
								cache : false
							});
						   onMenuRefreshClick2(entity);
					   }
				   },
				   icon: Ext.MessageBox.QUESTION
				});
		}
	}
}
function onMenuRefreshClick2(entity){
	window.parent.showWaitingDlg("@COMMON.wait@", String.format("@NETWORK.refreshingEntity@", entity.ip));
	$.ajax({
		url: '../topology/refreshEntity.tv',type: 'POST', cache:false,
		data:{entityId:entity.entityId},
		success:function() {
 			window.parent.closeWaitingDlg();
	        	onRefreshClick();
	        	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@NETWORK.refreshEntitySuccess@</b>'
	   			});
		},
		error:function(e) {
			if(e.simpleName == "PingException") {
				window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cannotConnectEntity@");
	        } else if (e.simpleName == "NetworkException") {
	        	window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.snmpConnectFail@");
	        } else if (e.simpleName == "UnknownEntityTypeException") {
	        	window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.unknownEntityType@");
	        } else if (e.simpleName == "EntityRefreshException") {
	        	window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.refreshEntityFail@");
	        } else{
	            window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.notDiscoveryEntityAgain@");
	        }
		}
	});
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
	if (entity) {
        if (EntityType.isCcmtsWithAgentType(entity.type)) {
            showCmc8800BSnap(record.data.entityId, entity.name, entity.typeId);
        } else {
            showEntitySnap(entity.entityId, entity.name);
        }
    }
}

function showCmc8800BSnap(entityId, name, typeId){
    if (entityId == -1) {
        window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.discoveryUnfinished@");
        return;
    }
    window.parent.addView('entity-' + entityId,   name  , 'entityTabIcon',
            '/cmc/showCmcPortal.tv?cmcId=' + entityId);
}

function getNameById(entityId){
	//遍历store找到对应设备的名称
	var record;
	for (var j = 0; j < store.getCount(); j++) {
		if (store.getAt(j).get('entityId') == entityId) {
			record = store.getAt(j);
			return record.data.name;
		}
	}
	return '';
}

/************************************
 		添加关注
 ***********************************/
function onAttentionClick(){
	var se = getSelectedEntity()
	var attention = se.attention
	if(attention){//如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : se.entityId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cancelFocusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@NETWORK.cancelFocusSucccess@</b>'
	   			});
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cancelFocusFail@")
			}
		})
	}else{//如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : se.entityId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.focusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@NETWORK.focusSucccess@</b>'
	   			});
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.focusFail@")
			}
		})
	}
}

//添加到百度地图
function addBaiduMap(){
	var se = getSelectedEntity()
	var entityId = se.entityId;
	var typeId = se.typeId
	var entityName = se.name
	window.top.createDialog('modalDlg',
			"@BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + entityId + "&typeId=" + typeId + "&entityName=" + entityName, null, true, true);
}
/************************************
		移动到拓扑地域
***********************************/
function onMoveToClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
			entityId = selections[0].data.entityId;
		window.top.createDialog('editTopoFolder', "@network/COMMON.editSnapRegion@", 800, 500, '/entity/editTopoFolder.tv?entityId='+entityId, null, true, true);
	}	
}

function onViewAlarmClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		window.parent.addView("entity-" + entity.entityId, "@COMMON.entity@[" + entity.ip + ']', 'entityTabIcon',
		"alert/showEntityAlertJsp.tv?module=6&entityId=" + entity.entityId);
	}
}
function onTelnetClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		showTelnetClient(entity.entityId, entity.ip);
//		var str;
//		if( $("#telnetIframe").length == 0 ){
//			str = String.format('<div id="telnetIframe" style="width:1px; height:1px; overflow:hidden;"><iframe src="telnet://{0}"></iframe></div>',entity.ip);
//			$('body').append(str);
//		}else{
//			str = String.format('<iframe src="telnet://{0}"></iframe>',entity.ip);
//			$("#telnetIframe").html(str);
//		}
		//window.open('telnet://' + entity.ip, 'ntelnet' + entity.entityId);
	}	
}
function onPingClick() {
	var entity = getSelectedEntity();
	if (entity) {
		showPingWindow(entity.ip);
//		window.parent.createDialog("modalDlg", 'Ping' + " - " + entity.ip, 600, 400,
//		"entity/runCmd.tv?cmd=ping&ip=" + entity.ip, null, true, true);
	}
}
function onTracertClick() {
	var entity = getSelectedEntity();
	if (entity) {
		showTracetWindow(entity.ip);
//		window.parent.createDialog("modalDlg", 'Tracert ' + entity.ip, 600, 400,
//		"entity/runCmd.tv?cmd=tracert&ip=" + entity.ip, null, true, true);
	}
}
function onMibbleBrowserClick(){
	var entity = getSelectedEntity();
	if(entity){
		window.top.addView("mibbleBrowser","MIB Browser",'entityTabIcon',"/mibble/showMibbleBrowser.tv?host="+entity.ip,null,true)
	}else{
		window.top.addView("mibbleBrowser","MIB Browser",'entityTabIcon',"/mibble/showMibbleBrowser.tv",null,true)
	}
}

function onMibBrowseClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();	
		window.top.addView('mib' + entity.entityId, "@COMMON.entity@[" + entity.ip + ']',
			'entityTabIcon', 'realtime/showEntityMibJsp.tv?module=4&entityId=' + entity.entityId);
	}
}
function onAddToGoogleClick() {
	window.parent.addView("ngm", "@NETWORK.googleMapNet@", "googleIcon", "google/showEntityGoogleMap.tv");
}
function onEntityPropertyClick() {
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.showProperty("entity/showEntityPropertyJsp.tv?entityId=" + entity.entityId);
	}
}

function modifyDevice() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    window.top.createDialog('renameEntity', I18N.COMMON.deviceInfo, 600, 370,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function changeEntityName(entityId, name) {
    store.reload();
}
/**
 * 删除设备.要求彻底删除,而不是只移动到回收站 @author bravin
 */
function onDeleteClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.top.showConfirmDlg("@COMMON.tip@", "@NETWORK.confirmDeleteEntity@", function(type) {
			if (type == 'no') {return;}
			window.top.showWaitingDlg("@COMMON.wait@", "@NETWORK.deletingEntityAndLink@",'waitingMsg','ext-mb-waiting');
			Ext.Ajax.request({url: '../entity/moveEntiyToRecyle.tv',
			   success: function() {
				   onRefreshClick();
				   window.top.closeWaitingDlg();
				   top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@resources/COMMON.deleteSuccess@</b>'
		   		   });
			   },
			   failure: function(){window.top.showMessageDlg("@COMMON.error@", "@NETWORK.deleteFailure@", 'error');window.top.closeWaitingDlg();},
			   params: {entityIds:entityIds}
			});			
		});
	} else {
		window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.notSelected@");
	}
}

/**
 * 替换设备,获取旧设备的信息，重新拓扑设备
 * 1、IP 别名
 * 2、snmp信息
 * 3、地域信息（下级设备跟随上联设备）
 */
function replaceEntity(){
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    window.top.showConfirmDlg("@COMMON.tip@", "@network.confirmreplace@", function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg("@COMMON.wait@", "@network.replacing@",'waitingMsg','ext-mb-waiting');
        Ext.Ajax.request({
           url: '/entity/replaceEntity.tv',
           success: function(json) {
               var obj = JSON.parse(json.responseText)
               if(obj.result){
                   onRefreshClick();
                   window.top.closeWaitingDlg();
               }else{
                   window.top.closeWaitingDlg();
                   top.afterSaveOrDelete({
                       title: I18N.RECYLE.tip,
                       html: '<b class="orangeTxt">' + "@network.replacefail@" + '</b>'
                   });
               }
           },
           failure: function(json){
               window.top.closeWaitingDlg();
               top.afterSaveOrDelete({
                   title: I18N.RECYLE.tip,
                   html: '<b class="orangeTxt">' + "@network.replacefail@" + '</b>'
               });
           },
           params: {entityId:entityId}
        });         
    });
}

/**
 * 取消管理
 */
function cancelManagement() {
	window.top.showConfirmDlg("@COMMON.tip@", "@NETWORK.comfirmCancelManegeEntity@", function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = getSelectedEntityId();
		Ext.Ajax.request({url: '../entity/cancelManagement.tv',
		   success: function() {
			   store.reload();
			   window.parent.removeTab('entity-' + entityIds[0]);
			   top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@resources/COMMON.operateSuccess@</b>'
	   		   });
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});
	});
}

/**
 * 重新管理设备
 */
function enableManagement() {
	window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@RESOURCES/RECYLE.confirmManagement@", function(type) {
		if (type == 'cancel') {return;}
		var entityIds = [];
		entityIds[0] = getSelectedEntityId();
	 	$.ajax({url: '../entity/enableManagement.tv', data: {'entityIds': entityIds},
	 		success: function() {
				store.reload()
				window.parent.removeTab('entity-' + entityIds[0]);
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@resources/COMMON.operateSuccess@</b>'
	   		    });
	 		}, error: function() {
	 			window.parent.showErrorDlg();
	 		}, cache: false});			
	});
}

function onFindClick() {
/*	var opts = store.lastOptions;
	_queryText = Ext.getCmp("searchEntity").getValue();
	Ext.apply(opts.params, {
	    queryText: _queryText,
	    start: 0
	});
	store.reload(opts);*/
	store.baseParams={
		start: 0,
		limit: pageSize,	
		queryText: $('#searchEntity').val(),
		deviceType: $('#deviceType').val(),
		onlineStatus: $('#onlineStatus').val()
	}
	store.load();
}

function onViewConfigClick() {
	var entity = getSelectedEntity();
	if (entity != null) {
		Ext.menu.MenuMgr.hideAll();
		window.top.addView('entity-' + entity.entityId, "@COMMON.entity@[" + entity.ip + ']',
			'entityTabIcon', 'entity/showEditEntityJsp.tv?module=2&entityId=' + entity.entityId);
	}
}
function locateClick() {
	var entity = getSelectedEntity();
	if (entity != null) {
		Ext.menu.MenuMgr.hideAll();
		window.top.locateEntityByIp(entity.ip);
	}
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
function doRefresh() {
	onRefreshClick();
}
function startTimer() {
	if (dispatcherTimer == null) {
		dispatcherTimer = setInterval('onRefreshClick()', dispatcherInterval);
	}
}
function doOnUnload() {
	if (dispatcherTimer != null) {
		clearInterval(dispatcherTimer);
		dispatcherTimer = null;
	}
}

function onQuickSet() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var entityIds = [];
		for (var i = 0; i < selections.length; i++) {
			entityIds[i] = selections[i].data.entityId;
		}
		window.parent.createDialog("quickSetDlg", "@RESOURCES/COMMON.quickSet@", 600, 450, "/ap/showQuickSetWnd.tv", null, true, true);
    }
}

/**
 * 新建设备
 */
function onNewEntityClick() {
	window.top.createDialog('modalDlg', "@RESOURCES/COMMON.newEntity@", 600, 500, 'entity/showNewEntity.tv?folderId=10', null, true, true); 
}
function cmtsPerfManage(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
	window.parent.addView('cmcPerfParamConfig', I18N.CMC.title.performanceCollectConfig,"bmenu_preference",'/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + entityId);
}
function showEntityMenu(event,record){
    var entityId = record.data.entityId
	var ip = record.data.ip;
	var type = record.data.typeId;//设备类型
	var status = record.data.status//管理状态
	var supportMap = record.data.supportMap;
	var attentionAction;//关注的操作
	if(record.data.attention)
		attentionAction = "@RESOURCES/COMMON.cancelAttention@"
	else
		attentionAction = "@RESOURCES/COMMON.attention@"
    //---设备菜单-----//   
	var items = []
	if(record.data.typeId != UNKNOWN_TYPE){
		items[items.length] = {text: "@NETWORK.reTopo@", handler: onDiscoveryAgainClick,disabled:!operationDevicePower};//重新TOPO
		items[items.length] = '-';
	}
	if(type != -1){
		items[items.length] = {text:attentionAction, handler : onAttentionClick};
		items[items.length] = {text:"@BAIDU.viewMap@", handler : addBaiduMap};
		if(EntityType.isOltType(record.data.typeId)){
			items[items.length]  = {id:'logicInterfaceConfig', text: "@COMMON.logicInterfaceConfig@", handler : showLogicInterfaceConfig,disabled:!operationDevicePower,entityId:entityId}
			items[items.length] = { 
    		    id: 'oltVlan',
    		    text: 'VLAN',
    		    handler: function(){
    		    	window.parent.addView('entity-' + userObj.userObjId, userObj.name , 'entityTabIcon', '/epon/showOltVlanList.tv?module=10&entityId=' + userObj.userObjId)
    		    }
        	};
			items[items.length] = {id : "oltIgmpConfig", text: "@COMMON.igmpConfig@", handler : showIgmpConfig,disabled:!operationDevicePower,entityId:entityId}
			items[items.length] = {id : "oltDhcpConfig", text: "@OLTDHCP/oltdhcp.dhcpManager@", handler: showDhcpConfig,entityId:entityId};
			items[items.length] = {id : "oltPPPoEConfig", text: "@OLTDHCP/oltdhcp.pppoeManager@", handler: showPPPoEConfig,entityId:entityId};
		}
		items[items.length] = '-';
		/*items = [
	        //{text:"@COMMON.view@", handler : onEntitySnapClick},
	        {text:attentionAction, handler : onAttentionClick},
	        '-'
		]*/
	}
	var toolMenu = [
	    {text:"@NETWORK.ping@", handler: onPingClick}, 
	    {text:'Telnet', handler: onTelnetClick, hidden: !operationDevicePower},
	    {text:"@NETWORK.tracert@", handler: onTracertClick},
	    {text : "MIB Browser",handler : onMibbleBrowserClick}
	];
	
//	if( $.browser.webkit ){ 
//		//在ping后面加入一项，由于目前ie8不支持，暂时只考虑webkit内核;
//		toolMenu.splice(1,0,{text:'Telnet', handler:onTelnetClick});
//	}
	
	if(status ){
		if(ip != null && ip != ''){
			items[items.length] = {text:"@NETWORK.tool@", menu:toolMenu};
		}
        
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
	                items = extendOper(items, entityId, cmcId , ccMac , typeId, supportMap);
	            }
	        });
        }else if(EntityType.isCmtsType(record.data.typeId)){
        	items[items.length] = '-';
        	items[items.length] = {text: I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:cmtsPerfManage,disabled:!cmcPerfPower};
        } else if(record.data.typeId == UNKNOWN_TYPE){//unknow设备不做任何处理;
        	//之前没有判断unknow设备导致页面报js错误[EMS-8625]
        } else {
            $.ajax({
                type: "GET",
                url: String.format("/{0}/js/extendMenu.js",record.data.module),
                dataType: "script",
                async : false,
                success  : function () {
                    // menuArray , entityId,ip 是必须的,不能为空
                    items = extendOper(items, entityId, ip, record.data.name);
                }
            });
        }
	}
	if(type != -1){
		items[items.length]  = {text: "@network/COMMON.editSnapRegion@", handler : onMoveToClick,disabled:!topoGraphPower}
		items[items.length]  = {text: "@NETWORK.locateEntity@", handler:locateClick,disabled:!topoGraphPower || !operationDevicePower}
	}
	if (googleSupported && type != -1) {
		//items[items.length] = {text:"@COMMON.addToGoogle@", handler : onAddToGoogleClick,disabled:!googleMapPower}
		items[items.length] = "-"
	}
    items[items.length] = {text: "@RESOURCES/COMMON.deviceInfo@", iconCls: 'bmenu_rename', handler: modifyDevice};//修改别名
	items[items.length] = {text: "@RESOURCES/MODULE.deleteDevice@", iconCls: 'bmenu_delete', handler: onDeleteClick,disabled:!operationDevicePower};//删除
	items[items.length] = {text: "@network.replaceEntity@", iconCls: 'bmenu_replace', handler: replaceEntity,disabled:!operationDevicePower};//替换设备
	/*
	if(type != -1){
		if(status)
			items[items.length] = {text: "@NETWORK.cancelManageEntity@", iconCls: 'bmenu_cancelmanage', handler: cancelManagement};//取消管理
		else
			items[items.length] = {text: "@NETWORK.reManageEntity@", iconCls: 'bmenu_remanage', handler: enableManagement};//重新管理
	}
	*/
	if (!entityMenu) {
        entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    } else {
    	//---存在bug--//
    	entityMenu.removeAll();
    	entityMenu.add(items)
    }
	entityMenu.showAt(event.getXY())
}

function numberToPic(str){
	var newStr = '';	
	if(str.length < 0) return;
	newStr += '<div class="noWidthCenterOuter clearBoth"><ol class="noWidthCenter leftFloatOl pT1">';
	for(var i=0; i<str.length-1; i++){
		var cls = "percentNum" + str.substr(i,1);
		newStr += '<li><div class="' + cls + '"></div></li>';
	}
	newStr += '<li><div class="percentNumPercent"></div><li></ol></div>';
	return newStr;
}

function makePercentBar(v,c,r){
	var onlineStatus = r.data.state;
	if(v == null || v == '-' || !onlineStatus)
		return "--";
	var color;
	if(parseInt(v.split("%")[0])<90)
		color = "#14a600";
	else
		color = "red";
	var value = v;
	var left = -50
	var str = '';
	str += '<div class="percentBarBg">';
	if(color == "red"){
		str +=     '<div class="percentBarRed" style="width:'+ value +';">';
		str +=     '<div class="percentBarLeftConnerRed"></div>';
	}else{
		str +=     '<div class="percentBar" style="width:'+ value +';">';
		str +=     '<div class="percentBarLeftConner"></div>';
	}		
	str += '</div>';
	str += '<div class="percentBarTxt">'+ numberToPic(value) +'</div></div>';
	return str;
}


function showRealTimeInfo(cmcId,name){
    window.parent.addView('entity-realTime' + cmcId, '@COMMON.realtimeInfo@['+name+']', 'entityTabIcon', '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
}
function showOltRealTimeInfo(entityId, name, entityIp){
	window.parent.addView('oltRealTimeInfo' + entityId, '@COMMON.realtimeInfo@['+name+']' , 'entityTabIcon', '/epon/oltRealtime/showOltRealTime.tv?entityId='+ entityId + "&entityName=" + name + "&entityIp=" + entityIp);
}

function renderHandler(){
	return '<img src="/images/attention.png"/>'
}
function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    if(cellValue == "" || cellValue == null){
    	return "--";
    }
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}
function manuRender(v,m,r){
    var html = "";
    if ( EntityType.isCcmtsType(r.data.typeId)) {
        html = "<a href='javascript:;' onClick='showRealTimeInfo(" + r.data.entityId+",\""+ r.data.name+ "\")'>@COMMON.realtimeInfo@</a> / ";
    }else if(EntityType.isOltType(r.data.typeId)){//如果是8602或8603;
        html = "<a href='javascript:;' onClick='showOltRealTimeInfo(" + r.data.entityId+",\""+ r.data.name+ "\""+",\""+ r.data.ip+ "\")'>@COMMON.realtimeInfo@</a> / ";
    }
    //对于未知类型设备,不提供简单刷新,要求先进行重新拓扑
    var typeId = r.data.typeId;
    if(typeId == null || typeId == "" || typeId == -1){
    	return html + "<a href='javascript:;' class='withSub' style='margin-right:20px;'  onClick = 'showMoreOperation(" + r.data.entityId + ",event)'>@COMMON.other@</a>";
    }else{
        if(!refreshDevicePower){
            return html + "<span>@COMMON.refresh@</span> / <a href='javascript:;' class='withSub' style='margin-right:20px;'  onClick = 'showMoreOperation(" + r.data.entityId + ",event)'>@COMMON.other@</a>";
        }else{
            return html + "<a href='javascript:;' onClick='onMenuRefreshClick();'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub' style='margin-right:20px;'  onClick = 'showMoreOperation(" + r.data.entityId + ",event)'>@COMMON.other@</a>";
        }
    }
}
function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	event.getXY = function(){return [event.clientX,event.clientY];};
    showEntityMenu(event,record);
}
function showViewOperation(rid){
	var record = grid.getStore().getById( rid );
	if (EntityType.isCcmtsWithAgentType(record.data.typeId)) {
        showCmc8800BSnap(record.data.entityId, record.data.name, record.data.typeId);
    } else if (EntityType.isUnkown_Type(record.data.typeId)){
    	window.parent.createDialog("entityBasicInfo", String.format("@NETWORK.entityBasicInfo@", record.data.name),600,370,"portal/showUnknownEntityJsp.tv?entityId=" + record.data.entityId, null, true, true);
    } else {
        showEntitySnap(record.data.entityId, record.data.name);
    }
}

//构建设备类型查询
function buildDeviceTypeSelect() {
	var head = '<td style="width: 40px;"  align="right">'
			+ '@batchTopo.entityType@:'
			+ '</td>&nbsp;'
			+ '<td style="width: 100px;">'
			+ '<select id="deviceType" class="normalSel" style="width: 100px;">'
			+ '<option value="-1" selected>' + I18N.COMMON.pleaseSelect
			+ '</option>';
	var options = "";
	var tail = '</select></td>';
	// 获取设备类型
	var deviceType = EntityType.getEntityWithIpType();
	$.ajax({
		url : '/entity/loadSubEntityType.tv?type=' + deviceType,
		type : 'POST',
		dateType : 'json',
		success : function(response) {
			var entityTypes = response.entityTypes;
			for ( var i = 0; i < entityTypes.length; i++) {
				options += '<option value="' + entityTypes[i].typeId + '">'
						+ entityTypes[i].displayName + '</option>';
			}
		},
		error : function(entityTypes) {
		},
		async : false,
		cache : false
	});
	return head + options + tail;
}

//构建在线状态查询
function buildStatusInput() {
	return '<td width=40px align=center>'
			+ '@NETWORK.onlineStatus@:'
			+ '</td>&nbsp;'
			+ '<td><select class="normalSel" id="onlineStatus"><option value="-1">@COMMON.select@'
			+ '</option><option value="1">@COMMON.online@</option><option value="0">@COMMON.offline@</option></select></td>'
}

//处理运行时长
function renderSysUpTime(value, p, record) {
	if(!record.data.state) {
		return '--';
	}
    if (value == "NoValue") {
        return "<img src='/images/canNot.png' class='nm3kTip' nm3kTip='@PERF.mo.notCollected@' />";
    }
    var timeString;
    if (value != null && value != -1) {
        timeString = arrive_timer_format(parseInt(value));
    } else {
        timeString = "--";
    }
    return timeString;
}

function arrive_timer_format(s) {
    var t;
    if (s > -1) {
        hour = Math.floor(s / 3600);
        min = Math.floor(s / 60) % 60;
        sec = Math.floor(s) % 60;
        day = parseInt(hour / 24);
        if (day > 0) {
            hour = hour - 24 * day
            t = day + I18N.COMMON.D + hour + I18N.COMMON.H
        } else {
            t = hour + I18N.COMMON.H
        }
        if (min < 10) {
            t += "0"
        }
        t += min + I18N.COMMON.M
        if (sec < 10) {
            t += "0"
        }
        t += sec + I18N.COMMON.S
    }
    return t
}

function buildEntityInput() {
    return '<td width=150>&nbsp;@NETWORK.nameQuery@:' + '</td>&nbsp;' +
           '<td><input class="normalInput" type="text" style="width: 150px" id="searchEntity" maxlength="63"></td>'
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	window.physicalPaneItem = new Ext.menu.Item({text:"@NETWORK.physicalPane@", handler:onPhysicalPaneClick});
	window.portFlowItem = new Ext.menu.Item({text:"@NETWORK.portRealFlow@", handler : onPortRealStateClick});
	window.interfacesItem = new Ext.menu.Item({text: "@NETWORK.mibInfo@", handler: onMibBrowseClick, disabled: true});  
	
	var reader = new Ext.data.JsonReader({
	    totalProperty: "rowCount",
	    idProperty: "entityId",
	    root: "data",
        fields: [
			{name: 'entityId'},
			{name: 'parentId'},
			{name: 'module'},
		    {name: 'modulePath'},
		    {name: 'name'},
		    {name: 'ip'},
		    {name: 'mac'},
		    {name: 'type'},
		    {name: 'typeId'},
		    {name: 'typeName'},
		    {name: 'cpu'},
		    {name: 'mem'},
		    {name: 'state'},//在线状态
		    {name: 'status'},//管理状态
			{name: 'alert'},
			{name: 'attention',type:'boolean'},//关注
		    {name: 'snapTime'},
		    {name: 'lastRefreshTime'},
		    {name: 'supportMap'},
		    {name: 'sysName'},
		    {name: 'sysUptime'},
		    {name: 'createTime'},
		    {name: 'contact'},
		    {name: 'location'},
		    {name: 'note'}
        ]
	});
	var columnModels = [
		{id:'name', header: "<div class='txtCenter'>@COMMON.alias@</div>",align:'left', width:100, sortable:true, groupable:false, dataIndex:'name', renderer: renderName},
	    {header: "<div class='txtCenter'>@resources/COMMON.manageIp@</div>", align:'left',width:70, sortable:true, groupable:false, dataIndex: 'ip',renderer: renderIp},
	    {header: "<div class='txtCenter'>@COMMON.name@</div>",align:'left', sortable:true, groupable:false, dataIndex:'sysName'},
	    {header: "@NETWORK.typeHeader@", sortable:false, dataIndex:'typeName',menuDisabled:true, hideable :false},
	    {header: "@NETWORK.onlineStatus@", width:50, sortable:true,  dataIndex: 'state', align:'center', renderer: renderOnlie},
	    //{header: "@NETWORK.manageStatus@", width:50, sortable:false,menuDisabled:true, dataIndex: 'status', align:'center', renderer: renderManagement},
	    {header: "@NETWORK.cpuUsage@", width:70, sortable:true, groupable: false, dataIndex: 'cpu', renderer: makePercentBar},
	    {header: "@NETWORK.memUsage@", width:70, sortable:true, groupable: false, dataIndex: 'mem',renderer: makePercentBar},
	    {header: "@NETWORK.lastUpdateTime@", width:70, sortable:true, groupable: false, dataIndex: 'snapTime'},
	    //{header: "@resources/COMMON.lastRefreshTime@", width:80, sortable:false, menuDisabled:true,groupable: false, dataIndex: 'lastRefreshTime'},
	    {header: "@batchtopo.createtime@", width:70, sortable:true, groupable: false, dataIndex: 'createTime'},
	    {header: '<div class="txtCenter">@network/NETWORK.sysUpTime@</div>',align:'left', width:100, sortable:true, dataIndex: 'sysUptime', renderer : renderSysUpTime},
	    {header:'<div class="txtCenter">@COMMON.contact@</div>',dataIndex:'contact',align:'left',sortable:true,hidden:true,renderer:addCellTooltip},
	    {header:'<div class="txtCenter">@COMMON.location@</div>',dataIndex:'location',align:'left',sortable:true,hidden:true,renderer:addCellTooltip},
	    {header:'<div class="txtCenter">@COMMON.note@</div>',dataIndex:'note',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip},
	    {header: "<div class='txtCenter'>@COMMON.manu@</div>", width:216, dataIndex: 'lastRefreshTime',renderer : manuRender, align:'right', fixed:true}
	    /*{header: "@PortletCategory.getAttentionEntityList, width:100, sortable:false, menuDisabled:true,groupable: false, align: 'center',renderer : renderHandler}*/
	];
	var cmConfig = CustomColumnModel.init("entitySnapList",columnModels,{}),
        cm = cmConfig.cm,
        sortInfo = cmConfig.sortInfo || {field: 'createTime', direction: 'ASC'};

	store = new Ext.data.GroupingStore({
       	url: '/entity/loadEntitySnapList.tv',
       	reader: reader,
		remoteGroup: false,
		remoteSort: true,
		groupField: 'typeName',
		groupOnSort: false
    });
   //store.setDefaultSort('createTime', 'ASC');
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
   
   // 解决点击grid下方刷新图标显示不一致的问题
   /* 
   store.on("beforeload",function(s,opts){
       opts.params["queryText"]=_queryText;
   });*/

    var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "@COMMON.items@" : "@COMMON.items@"]})';

	var toolbar = [
	    {text: "@COMMON.create@", iconCls: 'bmenu_new', handler:onNewEntityClick, disabled:!operationDevicePower }, '-',
	    buildEntityInput(), 
	  /*  "@NETWORK.nameQuery@:",
	    {xtype:'textfield', id:'searchEntity'},*/
	    {xtype: 'tbspacer', width: 3},
	    buildDeviceTypeSelect(),
	    {xtype: 'tbspacer', width: 3},
	    buildStatusInput(),
	    {xtype: 'tbspacer', width: 3},
		{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onFindClick}
	    /*, '-', {text: "@COMMON.refresh@", iconCls: 'bmenu_refresh', handler: onRefreshClick}*/
		];


	
    grid = new Ext.grid.GridPanel({
    	stripeRows:true,
		store: store,
        border: false,
        cm : cm,
        bodyCssClass: 'normalTable',
        view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: true,enableNoGroups: true,groupTextTpl: groupTpl
        }),
        enableColumnMove : true,
        listeners : {
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom('entitySnapList', cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo('entitySnapList', sortInfo);
			}
        },
        tbar: toolbar,
        bbar: buildPagingToolBar(),
		renderTo: document.body
    });
    selectionModel = grid.getSelectionModel();
    new Ext.Viewport({layout: 'fit', items: [grid]});
	
	store.load({params: {start: 0, limit: pageSize},
		callback : function(records, options, success) {
            if (records && records.length == 0 && !showCartoon) {
            	top.addCartoonTip();
			}
        }});
});

function showIgmpConfig(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
			entityId = selections[0].data.entityId;
			name = selections[0].data.name;
			window.parent.addView('OLTIGMP-' + entityId, "OLT IGMP[" + name + "]" , 'entityTabIcon','/epon/igmpconfig/showOltIgmpConfig.tv?entityId=' + entityId);
	}
}
//DHCP管理;
function showDhcpConfig(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
		entityId = selections[0].data.entityId;
		name = selections[0].data.name;
		var title = "OLT DHCP[{0}]".format(name);
		window.parent.addView('OLTDHCP-' + entityId, title, 'entityTabIcon', '/epon/oltdhcp/showOltDhcp.tv?entityId='+entityId);
	}
}
//PPPoE+ 管理;
function showPPPoEConfig(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
		entityId = selections[0].data.entityId;
		name = selections[0].data.name;
		var title = "OLT PPPoE+[{0}]".format(name);
		window.parent.addView('OLTPPPoE-' + entityId, title, 'entityTabIcon', '/epon/oltdhcp/showOltPppoe.tv?entityId='+entityId);
	}
}

function showLogicInterfaceConfig(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
			entityId = selections[0].data.entityId;
			name = selections[0].data.name;
			window.parent.addView('OLTLogicInterface-' + entityId, "@epon/logicInterfaceCfg@[" + name + "]" , 'entityTabIcon','/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entityId  + '&interfaceType=1');
	}
}