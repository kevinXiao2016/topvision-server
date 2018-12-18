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

function showEntitySnap(id, $name) {
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-' + id, $name, 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
}

function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url+'?_='+Math.random(), failure: fn, success: sn,timeout:1800000,  params: param});
}

function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			I18N.COMMON.online);	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
			I18N.COMMON.unreachable);	
	}
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

//刷新属性信息
function onMenuRefreshClick(){
	var entity = getSelectedEntity();
	if (entity) {
		window.parent.showWaitingDlg("@COMMON.wait@", String.format("@network/NETWORK.refreshingEntity@", entity.ip));
		$.ajax({
			url: '../topology/refreshEntity.tv',type: 'POST', cache:false,
			data:{entityId:entity.entityId},
			success:function() {
	 			window.parent.closeWaitingDlg();
 	        	onRefreshClick();
 	        	top.afterSaveOrDelete({
 	   				title: '@COMMON.tip@',
 	   				html: '<b class="orangeTxt">@network/NETWORK.refreshEntitySuccess@</b>'
 	   			});
			},
			error:function(e) {
				if(e.simpleName == "PingException") {
					window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.cannotConnectEntity@");
		        } else if (e.simpleName == "NetworkException") {
		        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.snmpConnectFail@");
		        } else if (e.simpleName == "UnknownEntityTypeException") {
		        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.unknownEntityType@");
		        } else if (e.simpleName == "EntityRefreshException") {
		        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.refreshEntityFail@");
		        } else{
		            window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.notDiscoveryEntityAgain@");
		        }
			}
		});
	}
}
//重新Topo
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
		showEntitySnap(entity.entityId, entity.name);
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
	if(entity)
		window.top.addView("mibbleBrowser","MIB Browser",null,"/mibble/showMibbleBrowser.tv?host="+entity.ip,null,true)
	else
		window.top.addView("mibbleBrowser","MIB Browser",null,"/mibble/showMibbleBrowser.tv",null,true)
}

function onAddToGoogleClick() {
	window.parent.addView("ngm", I18N.WorkBench.googleMap, "googleIcon", "google/showEntityGoogleMap.tv");
}
function onEntityPropertyClick() {
	var nodes = grid.getSelectionModel().getSelections();
	var entityId = nodes[0].json.entityId;
	if (nodes) {
		window.parent.showProperty("entity/showEntityPropertyJsp.tv?entityId="+entityId);
	}
}

function onNewEntityClick() {
	window.top.createDialog('modalDlg', I18N.COMMON.newEntity, 360, 230, 'entity/showNewEntity.tv?folderId=10', null, true, true); 
}

function modifyDevice() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    window.top.createDialog('renameEntity', "@RESOURCES/COMMON.deviceInfo@", 600, 370,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function changeEntityName(entityId, name) {
    store.reload();
}
/**
 * 删除设备.要求彻底删除,而不是只移动到回收站
 * 
 * @author bravin
 */
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
			   success: function() {
				   onRefreshClick();
				   top.nm3kRightClickTips({
	    				title: I18N.COMMON.tip,
	    				html: I18N.COMMON.deleteSuccess
	    		   });
			   },
			   failure: function(){window.top.showMessageDlg(I18N.COMMON.error, I18N.NETWORK.deleteFailure, 'error')},
			   params: {entityIds:entityIds}
			});			
		});
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSelected);
	}
}

function locateClick() {
	var entity = getSelectedEntity();
	if (entity != null) {
		Ext.menu.MenuMgr.hideAll();
		window.top.locateEntityByIp(entity.ip);
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

/*******************************************************************************
 * 移动到拓扑地域
 ******************************************************************************/
function onMoveToClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
			entityId = selections[0].data.entityId;
		window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+entityId, null, true, true);
	}	
}

function onRefreshClick() {
	store.reload();
}

function doRefresh() {
	onRefreshClick();
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
                       		]});
   return pagingToolbar;
}
/*******************************************************************************
 * 添加关注
 ******************************************************************************/
function onAttentionClick(){
	var se = getSelectedEntity();
	var attention = se.attention
	if(attention){// 如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : se.entityId},
			success : function(){
				top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: I18N.OLT.cancelFocusSuccess
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.cancelFocusSuccess)
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.cancelFocusFail)
			}
		})
	}else{// 如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : se.entityId},
			success : function(){
				top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: I18N.OLT.focusSuccess
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.focusSuccess)
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.focusFail)
			}
		})
	}
}

/**
 * 取消管理
 */
function cancelManagement() {
	window.top.showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.comfirmCancelManegeEntity, function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = getSelectedEntityId();
		Ext.Ajax.request({url: '../entity/cancelManagement.tv',
		   success: function() {
			   store.reload()
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});
	});
}

function systemRogueCheck(){ 
	var se = getSelectedEntity();
	var rogueCheck = se.systemRogueCheck;
	var entityId = se.entityId;
	var action =  rogueCheck == 1 ?  '@EPON.close@': '@EPON.open@';
    window.parent.showConfirmDlg("@EPON.tip@", String.format('@OLT.rogueCheckHandle@', action), function(type) {
      if (type == 'no') {
          return;
      }
      var systemRogueCheck = rogueCheck == 1 ? 2 : 1;
      window.top.showWaitingDlg("@EPON.wait@", String.format('@OLT.rogueCheckHandling@' , action));
      $.ajax({
          url: '/onu/rogueonu/setOltRogueCheck.tv',
          data: "entityId=" + entityId + "&systemRogueCheck=" + systemRogueCheck,
          success: function() {
        	  top.afterSaveOrDelete({
        		  title: "@EPON.tip@",
        		  		html: '<b class="orangeTxt">'+action + " " +'@OLT.rogueCheckSuccess@'
        		  });
        	  store.reload()
          }, error: function() {
        	  window.parent.showMessageDlg("@EPON.tip@", action + " " + '@OLT.rogueCheckFailure@')
         }, cache: false
      });
    });  
}
/**
 * 重新管理设备
 */
function enableManagement() {
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.EPON.confirmReManageEntity, function(type) {
		if (type == 'cancel') {return;}
		var entityIds = [];
		entityIds[0] = getSelectedEntityId();
	 	$.ajax({url: '../entity/enableManagement.tv', data: {'entityIds': entityIds},
	 		success: function() {
				store.reload()
	 		}, error: function() {
	 			window.parent.showErrorDlg();
	 		}, cache: false});			
	});
}

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
		top.addView('OLTDHCP-' + entityId, title, 'entityTabIcon', '/epon/oltdhcp/showOltDhcp.tv?entityId='+entityId);
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
		top.addView('OLTPPPoE-' + entityId, title, 'entityTabIcon', '/epon/oltdhcp/showOltPppoe.tv?entityId='+entityId);
	}
}

function showEntityMenu(event,record){
	var entityId = record.data.entityId
	var ip = record.data.ip
	var status = record.data.status// 管理状态
	var attentionAction;// 关注的操作
	var rogueCheckAction;//长发光检测
	if(record.data.attention)
		attentionAction = I18N.COMMON.cancelAttention
	else
		attentionAction = I18N.COMMON.attention
		
	if(record.data.systemRogueCheck == 1)
		rogueCheckAction = '@OLT.closeRogueCheck@'
	else
		rogueCheckAction = '@OLT.openRogueCheck@'
	
	// ---设备菜单-----//
	var items = []
	items[items.length] = {text: "@network/NETWORK.reTopo@", handler: onDiscoveryAgainClick,disabled:!refreshDevicePower};//重新TOPO
	items[items.length] = {text:"@network/BAIDU.viewMap@", handler : addBaiduMap};
	items[items.length] = {id : "logicInterfaceConfig",text:"@logicInterfaceCfg@", handler : showLogicInterfaceConfig,entityId:entityId};
	items[items.length] = {id : "oltVlan",text:"VLAN", handler : showOltVlanList,entityId:entityId};
	items[items.length] = {id : "systemRogueCheck",text:"@OLT.rogueCheck@", handler : systemRogueCheck,entityId:entityId};
	items[items.length] = {id : "oltIgmpConfig", text: "@COMMON.igmpConfig@", handler : showIgmpConfig,disabled:!operationDevicePower,entityId:entityId};
	items[items.length] = {id : "oltDhcpConfig", text: "@OLTDHCP/oltdhcp.dhcpManager@", handler: showDhcpConfig,entityId:entityId};
	items[items.length] = {id : "oltPPPoEConfig", text: "@OLTDHCP/oltdhcp.pppoeManager@", handler: showPPPoEConfig,entityId:entityId};
	items[items.length] = {text:attentionAction, handler : onAttentionClick};
	items[items.length] = '-';
	
	var toolMenu = [
	    {text:I18N.NETWORK.ping, handler: onPingClick}, 
	    {text:'Telnet', handler:onTelnetClick, hidden: !operationDevicePower},
		{text:I18N.NETWORK.tracert, handler: onTracertClick},
		{text : "MIB Browser",handler : onMibbleBrowserClick}
    ];
//	if( $.browser.webkit ){ //在ping后面加入一项，由于目前ie8不支持，暂时只考虑webkit内核;
//		toolMenu.splice(1,0,{text:'Telnet', handler:onTelnetClick});
//	}
	
	if(status){
		items[items.length] = {text:I18N.NETWORK.tool, menu:toolMenu};
		$.ajax({
	        type: "GET",
	        url: "/olt/js/extendMenu.js",
	        dataType: "script",
	        async : false,
	        success  : function () {
	        	// menuArray , entityId,ip 是必须的,不能为空
	        	items = extendOper(items,entityId,ip);
	        }
	    });
	}
	
	// 在这里确定设备的路径
	items[items.length]  = {text: "@network/COMMON.editFolder@", handler : onMoveToClick,disabled:!topoGraphPower}
	items[items.length]  = {text: I18N.NETWORK.locateEntity, handler:locateClick,disabled:!topoGraphPower||!operationDevicePower}
	
	if (googleSupported) {
		//items[items.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:!googleMapPower}
		items[items.length] = "-"
	}
    items[items.length] = {text: "@RESOURCES/COMMON.deviceInfo@", iconCls: 'bmenu_rename', handler: modifyDevice};//修改设备信息
    items[items.length] = {text: I18N.MODULE.deleteDevice, iconCls: 'bmenu_delete', handler: onDeleteClick,disabled:!operationDevicePower};// 删除
	/*
    if(status)
		items[items.length] = {text: I18N.NETWORK.cancelManageEntity, iconCls: 'bmenu_cancelmanage', handler: cancelManagement};// 取消管理
	else
		items[items.length] = {text: I18N.NETWORK.reManageEntity, iconCls: 'bmenu_remanage', handler: enableManagement};// 重新管理
	*/
	if (!entityMenu) {
        entityMenu = new Ext.menu.Menu({ id: 'entityMenu',minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    } else {
    	// ---存在bug--//
    	entityMenu.removeAll();
    	entityMenu.add(items)
    }
	entityMenu.showAt(event.getXY())
}

function showEntityDetail(entityId, $name){
	window.parent.addView('entity-' + entityId, $name , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + entityId);
}
function manuRender(v,m,r){
    if(!refreshDevicePower){
        return String.format("<a href='javascript:;' onClick='showOltRealTimeInfo({0})'>@COMMON.realtimeInfo@</a> / <span>@COMMON.refresh@</span> / <a href='javascript:;' class='withSub'  onClick = 'showMoreOperation({0},event)'>@COMMON.other@</a>",r.id);
    }else{
        return String.format("<a href='javascript:;' onClick='showOltRealTimeInfo({0})'>@COMMON.realtimeInfo@</a> / <a href='javascript:;' onClick='onMenuRefreshClick();'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub'  onClick = 'showMoreOperation({0},event)'>@COMMON.other@</a>",r.id);
    }
}
//添加到百度地图
function addBaiduMap(){
	var se = getSelectedEntity();
	var entityId = se.entityId;
	var entityName = se.name
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + entityId + "&entityName=" + entityName, null, true, true);
}

function showOltVlanList(){
	var entity = getSelectedEntity();
	window.parent.addView('entity-' + entity.entityId, entity.name , 'entityTabIcon',
			'/epon/showOltVlanList.tv?module=10&entityId=' + entity.entityId)
}

function showLogicInterfaceConfig(){
	var entity = getSelectedEntity();
	window.parent.addView('OLTLogicInterface-' + entity.entityId,'@logicInterfaceCfg@[' + entity.name + ']' , 'entityTabIcon',
			'/epon/logicinterface/showLogicInterfaceView.tv?entityId=' + entity.entityId + '&interfaceType=1')
}

function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	grid.getSelectionModel().selectRecords([record]);
	event.getXY = function(){return [event.clientX,event.clientY];};
    showEntityMenu(event,record);
}
function showViewOperation(rid){
	var entity = getSelectedEntity()
	window.parent.addView('entity-' + entity.entityId, entity.name , 'entityTabIcon',
			'portal/showEntitySnapJsp.tv?entityId=' + entity.entityId)
}

function showOltRealTimeInfo(rid){
	var entity = getSelectedEntity();
	var entityId = entity.entityId;
	var name = entity.name;
	var entityIp = entity.ip;
	window.parent.addView('oltRealTimeInfo' + entityId, '@COMMON.realtimeInfo@['+name+']' , 'entityTabIcon', '/epon/oltRealtime/showOltRealTime.tv?entityId='+ entityId + "&entityName=" + name + "&entityIp=" + entityIp);
}
function makePercentBar(v,c,r){
	var onlineStatus = r.data.state;
	if(v == null || v == '-' || !onlineStatus){
		return "--";
	}
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

function renderSysUpTime(value, p, record) {
	var onlineStatus = record.data.state;
	if(!onlineStatus){
		return "--";
	}
    if (value == "NoValue") {
        return "<img src='/images/canNot.png' class='nm3kTip' nm3kTip='@PERF.mo.notCollected@' />";
    }
    var timeString;
    if (value != null && value != -1) {
        timeString = arrive_timer_format(value)
    } else {
        timeString = I18N.label.deviceUpTimeGetFail;
    }
    return timeString;
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    if(cellValue == "" || cellValue == null){
    	return "--";
    }
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

function arrive_timer_format(s) {
    var t
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

//构建设备类型查询
function buildDeviceTypeSelect() {
	var head = '<td style="width: 40px;"  align="right">'
			+ '@network/batchTopo.entityType@:'
			+ '</td>&nbsp;'
			+ '<td style="width: 100px;">'
			+ '<select id="deviceType" class="normalSel" style="width: 100px;">'
			+ '<option value="-1" selected>' + I18N.COMMON.pleaseSelect
			+ '</option>';
	var options = "";
	var tail = '</select></td>';
	// 获取设备类型
	var deviceType = EntityType.getOltType();
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
			+ '@network/NETWORK.onlineStatus@:'
			+ '</td>&nbsp;'
			+ '<td><select class="normalSel" id="onlineStatus"><option value="-1">@COMMON.select@'
			+ '</option><option value="1">@COMMON.online@</option><option value="0">@COMMON.offline@</option></select></td>'
}

function showOltRealTimeInfo(rid){
	var entity = getSelectedEntity()
	var entityId = entity.entityId;
	var name = entity.name;
	var entityIp = entity.ip;
	window.parent.addView('oltRealTimeInfo' + entityId, '@COMMON.realtimeInfo@['+name+']' , 'entityTabIcon', '/epon/oltRealtime/showOltRealTime.tv?entityId='+ entityId + "&entityName=" + name + "&entityIp=" + entityIp);
}

function buildEntityInput() {
    return '<td width=150>&nbsp;@network/NETWORK.nameQuery@:' + '</td>&nbsp;' +
           '<td><input class="normalInput" type="text" style="width: 150px" id="searchEntity" maxlength="63"></td>'
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var cmArr =  [
	    {id:"name",header:'<div class="txtCenter">'+I18N.COMMON.alias+'</div>',dataIndex:'name',width: 150,align:'left',sortable:true,renderer:function(value, p, record){
	    	if(!record.data.attention){
	    		return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+record.data.name+'\');">'+value+'</a>';
	    	}else{
	    		return '<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+record.data.name+'\');">'+value+'</a>';
	    		/*return  String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>' + '&nbsp;&nbsp;<img src="/images/att.png"/>',
	    				record.data.entityId, record.data.name, value);*/
	    	}
	    }},
	    {id:"deviceIp",header:'<div class="txtCenter">'+I18N.OLT.deviceIp+'</div>',dataIndex:'ip',align:'left',width: 110,sortable:true},
	    {header:'<div class="txtCenter">@network/NETWORK.host@</div>',dataIndex:'oltName',align:'left',width: 120,sortable:true},
	    {id:"oltType",header:I18N.OLT.entityType,dataIndex:'oltType',align:'center',width: 80,sortable:true},
	    {id:"onlineStatus",header:I18N.OLT.onlineStatus,dataIndex:'state',align:'center',width: 60,sortable:true,renderer: renderOnlie},
	    //{id:"status",header:I18N.OLT.status,dataIndex:'status',align:'center',width: 80,sortable:true,renderer: renderManagement},
	    //{id:"outbandIp",header:'<div class="txtCenter">'+I18N.OLT.outbandIp+'</div>',dataIndex:'outbandIp',align:'left',width: 120,sortable:true},
	    //{id:"inbandMac",header:I18N.OLT.inbandMac,dataIndex:'inbandMac',align:'center',width: 120,sortable:true},
	    //{id:"outbandMac",header:I18N.OLT.outbandMac,dataIndex:'outbandMac',align:'center',width: 120,sortable:true},
	    {header: "@network/NETWORK.cpuUsage@", width:120, sortable:true,dataIndex: 'cpu', renderer: makePercentBar},
	    {header: "@network/NETWORK.memUsage@", width:120, sortable:true,dataIndex: 'mem',renderer: makePercentBar},
	    {header:'<div class="txtCenter">@network/NETWORK.sysUpTime@</div>', dataIndex:'sysUpTime', align:'left', width: 140 ,sortable:true, renderer:renderSysUpTime},
	    {id:"bSoftwareVersion",header:'<div class="txtCenter">'+I18N.OLT.softVersion+'</div>',dataIndex:'bSoftwareVersion',align:'left',width: 120,sortable:true},
	    {id:"oltContact",header:'<div class="txtCenter">'+I18N.OLT.contact+'</div>',dataIndex:'contact',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip},
	    {id:"oltLocation",header:'<div class="txtCenter">'+I18N.OLT.location+'</div>',dataIndex:'location',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip},
	    {id:"oltNote",header:'<div class="txtCenter">'+I18N.OLT.note+'</div>',dataIndex:'note',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip},
	    {header: "@COMMON.manu@", width:200,renderer : manuRender, fixed : true }
	    // {id:"entityId",header:'Id',dataIndex:'entityId'}
	];
	var cmConfig = CustomColumnModel.init(saveColumnId,cmArr,{}),
	    cm = cmConfig.cm,
	    sortInfo = cmConfig.sortInfo || {field: 'name', direction: 'ASC'};
		
	store = new Ext.data.JsonStore({
	    url: '/epon/getOltList.tv',
	    idProperty: "entityId",
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['entityId','ip','name','status','state','oltType','sysUpTime','vendorName','cpu','mem','bSoftwareVersion','oltName','attention','contact','location','note','systemRogueCheck']
	});
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
	//store.setDefaultSort('name', 'ASC');
	
	
    /*     
    var toolbar = [];
    toolbar[toolbar.length] = "@OLT.deviceIp@:";
    toolbar[toolbar.length] =  {xtype:'textfield', id:'ip'};
    toolbar[toolbar.length] =  {xtype: 'tbspacer', width: 3};
    toolbar[toolbar.length] = "@ENTITYSNAP.basicInfo.alias@:";
    toolbar[toolbar.length] = "-";
    toolbar[toolbar.length] =  {xtype:'textfield', id:'name'};
    toolbar[toolbar.length] =  {xtype: 'tbspacer', width: 3};
    toolbar[toolbar.length] = "-";
    toolbar[toolbar.length] = {xtype: 'button',handler:onSeachClick, iconCls: 'bmenu_find',text:"@COMMON.query@"};
    toolbar[toolbar.length] = "-";
    toolbar[toolbar.length] = {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick};
    */
	var toolbar = [
	  /*  "@network/NETWORK.nameQuery@:",
	    {xtype:'textfield', id:'searchEntity'},*/
	  	buildEntityInput(),
	    {xtype: 'tbspacer', width: 3},
	    buildDeviceTypeSelect(),
	    {xtype: 'tbspacer', width: 3},
	    buildStatusInput(),
	    {xtype: 'tbspacer', width: 3},
		{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onSeachClick}
	    /*, '-', {text: "@COMMON.refresh@", iconCls: 'bmenu_refresh', handler: onRefreshClick}*/
	];
    
   	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		border:false,
   		store: store,
   		cm : cm,
   		sm : sm,
   		tbar: toolbar,
   		bbar: buildPagingToolBar(),
   		enableColumnMove : true,
   		listeners : {
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
			}
        },
   		viewConfig:{
   			forceFit: true
   		},
	    renderTo: "grid"
   	});
   	var viewPort = new Ext.Viewport({
   	     layout: "border",
   	     items: [grid]
   	}); 
	store.load({params:{start: 0, limit: pageSize}});
}); 
