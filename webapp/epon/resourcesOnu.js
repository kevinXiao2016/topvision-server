var contextMenu = null;
var entityMenu = null;
var grid = null;
var store = null;
var pagingToolbar = null;
var partitionData = {
	receiveMin:  '',
	receiveMax:  '',
    transmitMin: '',
    transmitMax: '',
    ponRecvMin:  '',
    ponRecvMax:  ''
};
//刷新超时时间
var REFRESH_TIMEOUT = 30000;

function showOnuConfigPage(onuId, entityId) {
	Ext.menu.MenuMgr.hideAll();
	window.top.createDialog('onuNameConfig', "@ONU.setName@", 600, 370, '/onu/showOnuConfig.tv?onuId=' + onuId + '&entityId=' + entityId, null, true, true);
}

function showOnuAbilityPage(onuId, entityId) {
	Ext.menu.MenuMgr.hideAll();
	window.top.createDialog('onuAbility', "@ONU.abilityInfo@", 800, 500, '/onu/showOnuAbility.tv?onuId=' + onuId + '&entityId=' + entityId, null, true, true);
}

function showOnOffRecordsView(onuId, onuIndex, entityId){
	Ext.menu.MenuMgr.hideAll();
	var queryString = "?onuId=" + onuId + "&onuIndex=" + onuIndex + "&entityId=" + entityId;
	window.top.createDialog('onuOnOffRecordDlog', "@ONU.viewOnOffRecord@", 800, 500, "/onu/onoffrecord/showOnOffRecords.tv" + queryString, null, true, true);
}

/************************************
移动到拓扑地域
***********************************/
function showOnuMoveToTopo(onuId, type,onuName) {
	Ext.menu.MenuMgr.hideAll();
	window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+onuId, null, true, true);
}

function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url, failure: fn, success: sn, params: param});
}

//添加到百度地图
function addBaiduMap(){
	var onu = getSelectedEntity();
	if (onu) {
		window.top.createDialog('modalDlg',
				"@network/BAIDU.viewMap@", 800, 600,
				'baidu/showAddEntity.tv?entityId=' + onu.onuId + "&entityName=" + onu.name, null, true, true);
	}
}

function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/online.gif" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/offline.gif" border=0 align=absmiddle>',
				"@COMMON.unreachable@");	
	}
}

function getSelectedEntity() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data;
	}
	return null;
}

function onOnuListClick() {
	var onu = getSelectedEntity();
	if (onu) {
		showOnuConfigPage(onu.onuId, onu.entityId);
	}
}

function onOnuAbilityClick() {
	var onu = getSelectedEntity();
	if (onu) {
		showOnuAbilityPage(onu.onuId, onu.entityId);
	}
}


function onMoveToTopo() {
	var onu = getSelectedEntity();
	if (onu) {
		showOnuMoveToTopo(onu.onuId, onu.onuType,onu.onuName);
	}
}

function showSimpleQuery() {
	// 隐藏高级查询DIV
	$('#advance-toolbar-div').effect("drop", {
		times : 1
	}, 200, function() {
		// 显示快捷查询DIV
		$('#simple-toolbar-div').effect("slide", {
			times : 1
		}, 200);
	});
}


function showAdvanceQuery() {
	// 隐藏简单查询DIV
	$('#simple-toolbar-div').effect("drop", {
		times : 1
	}, 200, function() {
		// 显示高级查询DIV
		$('#advance-toolbar-div').effect("slide", {
			times : 1
		}, 200,function(){
			//只允许执行一次
			if(WIN.SINGLE_RENDER_ADVANCE){
				return;
			}
			SINGLE_RENDER_ADVANCE = true;
			//onu类型选择
		    window.typeStore = new Ext.data.JsonStore({
			    url: '/onu/loadOnuTypes.tv',
			    root : 'data',
			    fields: ['typeId','displayName'],
			    listeners : {
					load : function(dataStore, records, options){
							var record = {typeId: -1, displayName: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
							clearSelect();
					}
				}
			});
		    window.typeCombo = new Ext.form.ComboBox({
			    id: 'type',
			    applyTo : typeSelect,
			    width : 170,
				triggerAction : 'all',
				editable : true,
				lazyInit : false,
				emptyText : "@COMMON.select@",
				valueField: 'typeId',
			    displayField: 'displayName',
				store : typeStore
		    });
		  
		    //加载olt对应的slot
			window.slotStore = new Ext.data.JsonStore({
				url : '/onu/getOltSlotList.tv',
				fields : ["slotId", "slotNo"],
				listeners : {
					load : function(dataStore, records, options){
						var entityId = $("#oltSelect").val();
						//只有选择了有效的设备才需要这样做
						if(entityId != null && entityId != -1){
							var record = {slotId: -1, slotNo: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
							clearSelect();
						}
					}
				}
			});
			
			window.slotCombo = new Ext.form.ComboBox({
				id : "slotCombox",
				store : slotStore,
				applyTo : "slotSelect",
				mode : 'local',
				valueField : 'slotId',
				displayField : 'slotNo',
				emptyText : "@COMMON.select@",
				selectOnFocus : true,
				typeAhead : true,			
				triggerAction : 'all',
				editable : false,
				width : 150,
				enableKeyEvents: true,
				listeners : {
					keydown : function(textField, event){
						if( event.getKey() ==  8 ){
							event.stopEvent();
							return;
						}
					}
				}
			});
			
			//加载slot对应的pon
			window.ponStore = new Ext.data.JsonStore({
				url : '/onu/getOltPonList.tv',
				fields : ["ponId", "ponNo"],
				listeners : {
					load : function(dataStore, records, options){
						var slotValue = slotCombo.getValue();
						//只有选择了有效的板卡才需要这样做
						if(slotValue != null && slotValue != -1){
							var record = {ponId: -1, ponNo: '@COMMON.select@'};
							var $record = new dataStore.recordType(record,"-1");
							dataStore.insert (0,[ $record ]);
							clearSelect();
						}
					}
				}
			});
			
			window.ponCombo = new Ext.form.ComboBox({
				id : "ponCombox",
				store : ponStore,
				applyTo : "ponSelect",
				mode : 'local',
				valueField : 'ponId',
				displayField : 'ponNo',
				emptyText : "@COMMON.select@",
				selectOnFocus : true,
				typeAhead : true,			
				triggerAction : 'all',
				editable : false,
				width : 120,
				enableKeyEvents: true,
				listeners : {
					keydown : function(textField, event){
						if( event.getKey() ==  8 ){
							event.stopEvent();
							return;
						}
					}
				}
			});
			//当slot改变时加载对应的pon
			slotCombo.on('select', function(comboBox){
				//先清除加载的pon
				ponCombo.clearValue(); 
			  	//加载对应pon
				var slotValue = comboBox.getValue();
				ponStore.load({params: {slotId: slotValue}});
			})
			
			window.selector = new NetworkNodeSelector({
			    id: 'oltSelect',
			    renderTo: "oltContainer",
			    //value : window["entityId"], //@赋值的方式一：配置默认值 
			    autoLayout: true,
			    listeners: {
			      selectChange: function(){
			    	    //olt设备改变时加载对应的slot
			    	    //先清除加载的slot,pon
			    	  	slotCombo.clearValue(); 
			    	  	ponCombo.clearValue();
			    	  	//加载对应的slot
			    		var entityId = $("#oltSelect").val();
			    		slotStore.load({params: {entityId: entityId}});
			    		//这时候删除上次加载的pon口数据,否则上次加载的pon口列表还存在
			    		ponStore.removeAll();
			    	}
			    }
			});
			$('#partition').onuIndexPartition().bind('click', function(){
				window.top.createDialog("modalDlg",'@ONU/ONU.onuIndexPartition@',800, 370, '/onu/showPartitionSelect.tv?partitionData='+encodeURI(JSON.stringify(partitionData)), null, true,true);
		    });
		});
	});
}

function partitionChanged(data){
	partitionData = data;
	$('#partition').onuIndexPartition.change(partitionData);
	/*var tmpdata = $.extend(store.baseParams, partitionData);
	store.baseParams = tmpdata;
	store.load({
        params: tmpData
    });*/
}

/**
 * added by huangdongsheng
 * 添加到google 地图
 */
function onAddToGoogleClick() {
    window.parent.addView("ngm", "@NETWORK/NETWORK.googleMapNet@", "googleIcon", "google/showEntityGoogleMap.tv");
}
function onResetClick() {
	var onu = getSelectedEntity();
	if (onu) {
        window.parent.showConfirmDlg("@COMMON.tip@", "@ONU.confirmReset@", function(type) {
            if (type == 'no') {
                return;
            }
            window.parent.showWaitingDlg("@COMMON.wait@", "@ONU.reseting@", 'ext-mb-waiting');
            $.ajax({
                url: '/onu/resetOnu.tv',
                type: 'POST',
                data: "entityId=" + onu.entityId + "&onuId=" + onu.onuId,
                success: function() {
                	top.closeWaitingDlg();
                	top.nm3kRightClickTips({
        				title: "@COMMON.tip@",
        				html: "@ONU.resetSuccess@"
        			});
                }, error: function() {
                	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.resetFail@");
            },cache: false
            });
        });
	}
}
function onRemoveRegClick() {
	var onu = getSelectedEntity();
	if (onu) {
        window.parent.showConfirmDlg("@COMMON.tip@", "@ONU.confirmRemoveReg@", function(type) {
            if (type == 'no') {
                return;
            }
            window.parent.showWaitingDlg("@COMMON.wait@", "@ONU.removingReg@", 'ext-mb-waiting');
            $.ajax({
                url: '/onu/deregisterOnu.tv',
                type: 'POST',
                data: "entityId=" + onu.entityId + "&onuId=" + onu.onuId,
                success: function() {
                	top.closeWaitingDlg();
                	top.nm3kRightClickTips({
        				title: "@COMMON.tip@",
        				html: "@ONU.removeRegSuccess@"
        			});
                }, error: function() {
                	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.removeRegFail@");
            }, cache: false
            });
        });
	}
}

function renderName(value,m,record){
	if(!record.data.attention){
		 return String.format('<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', value);
	}else{
		return String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>', value);
	}
}

function showOnuInfo(onuId,onuName){
	 window.parent.addView('entity-' + onuId, unescape(onuName), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + onuId );
}

function renderOnline(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
			"@COMMON.offline@");	
	}
}
function renderLevel(value, p, record){
	if (value == 0) {
		return "@ONU.Default@";
	} else if(value == 1) {
		return "@ONU.ImportantONU@";
	}else if(value == 2){
		return "@ONU.CommonMDU@";
	}else if(value == 3){
		return "@ONU.CommonSFU@";
	}else{
		return "@ONU.Default@";
	}
	
}

function renderIndex(value, p, record){
	return getLocationByIndex(value, "onu");
}

function renderType(value, p, record){
	try {
		return TYPE[''+value].type;
	}catch (e){
		return TYPE['255'].type;
	}
	
}

//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
		break;
	case 3: num = (index & 0xFF00) >> 8;//ONU
		break;
	case 4: num = index & 0xFF;//UNI
		break;
	}
	return num;
}

//通过index获得location
function getLocationByIndex(index, type){
	var t = parseInt(index / 256) + (index % 256);
	var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	if(type == "uni"){
		loc = loc + "/" + getNum(t, 4)
	}
	return loc
}

function onRefreshClick() {
	store.reload({
		callback: function(){
			clearSelect();
		}
	});
}

function doRefresh() {
	onRefreshClick();
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-'],
       listeners : {
       	beforechange : function(){
       		disabledToolbarBtn(0);
       	}
       }
    });
   return pagingToolbar;
}
function manuRender(v,m,r){
	var str1 = String.format("<a href='javascript:;' onClick='refreshOnu({1},{2},{3});'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
			   r.id, r.data.entityId, r.data.onuId, r.data.onuIndex),
	    str2 = String.format("<span>@COMMON.refresh@</span> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
	 		   r.id, r.data.entityId, r.data.onuId, r.data.onuIndex);
	if(!refreshDevicePower) {
		return str2;
	}else{
		return str1;
	}
	
}
function getSelectedEntity() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data;
	}
	return null;
}
function onAttentionClick(){
	var se = getSelectedEntity();
	var attention = se.attention
	if(attention){// 如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : se.onuId},
			success : function(){
				top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: I18N.OLT.cancelFocusSuccess
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.cancelFocusSuccess)
				store.reload({
					callback: function(){
						clearSelect();
					}
				})
			},
			error : function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.cancelFocusFail)
			}
		})
	}else{// 如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : se.onuId},
			success : function(){
				top.nm3kRightClickTips({
    				title: I18N.COMMON.tip,
    				html: I18N.OLT.focusSuccess
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.focusSuccess)
				store.reload({
					callback: function(){
						clearSelect();
					}
				})
			},
			error : function(){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.focusFail)
			}
		})
	}
}

function onuLaserClick(){
	var se = getSelectedEntity();
	var onuLaserSwitch = se.laserSwitch == 1 ? true:false;
	var laserSwitch = onuLaserSwitch == 1 ? 2 : 1;
	if(onuLaserSwitch){// 如果已关注,则从table中删除该记录
		$.ajax({
			url:'/onu/rogueonu/modifyOnuLaserSwitch.tv',cache:false,
			data : {
				onuId : se.onuId,
				entityId : se.entityId,
				onuLaser : laserSwitch
			},
			success : function(){
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@ONU.closeLaserSwitchSuc@'
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.cancelFocusSuccess)
				store.reload({
					callback: function(){
						clearSelect();
					}
				})
			},
			error : function(){
				window.parent.showMessageDlg('@COMMON.tip@', '@ONU.closeLaserSwitchFail@')
			}
		})
	}else{// 如果没有关注则添加该设备记录
		$.ajax({
			url:'/onu/rogueonu/modifyOnuLaserSwitch.tv',cache:false,
			data : {
				onuId : se.onuId,
				entityId : se.entityId,
				onuLaser : laserSwitch
			},
			success : function(){
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@ONU.openLaserSwitchSuc@'
    			});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.OLT.focusSuccess)
				store.reload({
					callback: function(){
						clearSelect();
					}
				})
			},
			error : function(){
				window.parent.showMessageDlg('@COMMON.tip@', '@ONU.openLaserSwitchFail@')
			}
		})
	}
}
function onTelnetClick() {
	var entity = getSelectedEntity();
	if (entity) {
		Ext.menu.MenuMgr.hideAll();
		showTelnetClient(entity.onuId, entity.entityIp);
	}
}
function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	WIN.entityId = record.data.entityId;
	WIN.onuId = record.data.onuId;
	WIN.onuIndex = record.data.onuIndex;
	WIN.onulocation = getLocationByIndex(record.data.onuIndex);
	grid.getSelectionModel().selectRecords([record]);
	showEntityMenu(record,event);
}
function modifyDevice() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.onuId;
    window.top.createDialog('renameEntity', "@RESOURCES/COMMON.deviceInfo@", 600, 370,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function changeEntityName(entityId, name) {
    store.reload({
		callback: function(){
			clearSelect();
		}
	});
}
function showViewOperation(){}

function renderLLID(value, m, record){
	return record.data.onuLlid16;
}

//刷新ONU信息
function refreshOnu(){
	var record = grid.getSelectionModel().getSelected();
	var entityId = record.data.entityId;
	var entityIp = record.data.entityIp;
    var onuId = record.data.onuId;
    var onuIndex = record.data.onuIndex;
    window.parent.showWaitingDlg("@COMMON.wait@", "@COMMON.refreshing@", 'waitingMsg', 'ext-mb-waiting');
    $.ajax({
        url:'/onu/refreshOnu.tv',
        data: {
        	entityId : entityId,
        	entityIp : entityIp,
        	onuId : onuId,
        	onuIndex : onuIndex
        },dateType:'json',
        success:function(json){
        	window.top.closeWaitingDlg();
        	   top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@network/NETWORK.refreshEntitySuccess@</b>'
   			});
        	onRefreshClick();
        },
        error:function(){
        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.refreshEntityFail@");
        },
        cache:false
    });
}

function renderOlt(value,m,record){
    var disValue = record.data.manageName+ "("+ value+")";
	return String.format('<a href="#" onclick=\'showOlt("' + record.data.entityId + '","' + escape(record.data.entityIp) + '");\'>{0}</a>',
	        disValue);
	
}

function showOlt(entityId,name){
	window.top.addView('entity-' + entityId,  name , 'entityTabIcon','/portal/showEntitySnapJsp.tv?entityId=' + entityId);
}

function renderPower(v,m,r){
	if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
		return " -- ";
	}
	return (Number(v)).toFixed(2) + " dBm";
}

function renderOnuPower(v,m,r) {
	if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
		return "<label class='showBubbleTipOnu first'> -- </label>";
	}
	return "<label class='showBubbleTipOnu first'>" + (Number(v)).toFixed(2) + " dBm</label>";
}

function renderCATVPower(v,m,r) {
	if(r.data.onuOperationStatus != 1 ||  v == null || v == 0|| Number(v) /10 < -20){
		return "<label class='showBubbleTipCATV first'> -- </label>";
	}
	return "<label class='showBubbleTipCATV first'>" + (Number(v)/10).toFixed(2) + " dBm</label>";
}

function renderMinPower(v,m,r){
	var text = showMinWithTime(v,r,r.data.minPowerTimeStr);
	return "<label class='showBubbleTipOnu second'>" + text + "</label>";
}

function renderMinCATVPower(v,m,r){
	if(r.data.onuOperationStatus != 1 ||  v == null || v == 0|| Number(v) /10 < -20){
		return "<label class='showBubbleTipCATV second'> -- </label>";
	}
	return "<label class='showBubbleTipCATV second'>" + (Number(v)/10).toFixed(2) + " dBm</label>";
}

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    if(cellValue == "" || cellValue == null){
    	return "--";
    }
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

//获取选择的设备
function getSelectedEntities(){
	var sm = grid.getSelectionModel();
	var selectedEntities = [];
	if (sm != null && sm.hasSelection()) {
		selectedEntities = sm.getSelections();
	}
	return selectedEntities;
}

//批量初始化
function initBatch(selectedEntities){
	  //初始化统计数据
	  $('#suc-num-dd').text(0);
	  $('#fail-num-dd').text(0);
	  //将选择的各行标记为正在刷新
	  for (i = 0, len = selectedEntities.length; i < len; i++) {
		  var rowId = grid.getStore().indexOf(selectedEntities[i]);
		  grid.getView().removeRowClass(rowId, 'red-row');
		  grid.getView().addRowClass(rowId, 'yellow-row');
	  }
	  //禁用查询功能、刷新功能、翻页功能
	  grid.getTopToolbar().setDisabled(true);
	  grid.getBottomToolbar().setDisabled(true);
	  //展示等待框
	  showLoading();
}

//刷新ONU光功率信息
function refreshOnuOptical(){
	//获取选择的设备
	selectedEntities = getSelectedEntities();
	if (selectedEntities.length == 0) {
	    return;
	}
	
	//批量初始化
	initBatch(selectedEntities);
	
	var successNum = 0,
	    failedNum = 0,
	    finishedIndex = [],
	    refreshedPonCounter = 0;
	
	//dwr接收处理函数
	window.top.addCallback("refreshOnuOptical", function(onuOptical) {
		//重置超时限制 
		clearTimeout(timeoutTimer);
		timeoutTimer = setTimeout(function(){
	    	unexpectedEnd();
	    }, REFRESH_TIMEOUT);
	    for (var j = 0, len = selectedEntities.length; j < len; j ++ ) {
	    	//遍历找到对应的行
	    	if (selectedEntities[j].get('onuId') == onuOptical.link.onuId) {
	    		finishedIndex.push(j);
	    		selectedEntities[j].set('onuPonRevPower', onuOptical.link.onuRevResult);
	    		selectedEntities[j].set('onuPonTransPower', onuOptical.link.onuTransResult);
	    		selectedEntities[j].set('oltponrevpower', onuOptical.link.oltRevResult);
	    		selectedEntities[j].set('onuCatvOrInfoRxPower', onuOptical.catv == null ? 0 : onuOptical.catv.onuCatvOrInfoRxPower);
	    		selectedEntities[j].commit();
	    		//进入此处都认为是成功的
	    		grid.getView().removeRowClass(grid.getStore().indexOf(selectedEntities[j]), 'yellow-row');
	    		successNum++;
	    		$('#suc-num-dd').text(successNum);
	    	}
	    }
		//刷新完成处理
		refreshedPonCounter++;
	    if (refreshedPonCounter >= selectedEntities.length) {
	    	clearTimeout(timeoutTimer);
	    	outputResult();
	    }
	});
	
	var onuArray = [];
	
	for(var i = 0;i<selectedEntities.length;i++){
		//加入该设备 onuId
		onuArray[onuArray.length]  = selectedEntities[i].data.onuId;
	}
	
	//发送查询请求
    $.post('/onu/refreshOnuOptical.tv',{
    	"onuIdList" : onuArray,
    	"jConnectedId": WIN.top.GLOBAL_SOCKET_CONNECT_ID
    });
    //发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, REFRESH_TIMEOUT);
	
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("refreshOnuOptical");
    	//将正在处理行全部置为失败
    	for (var i = 0, len = selectedEntities.length; i < len; i++) {
    		if(!contains(finishedIndex, i)){
    			grid.getView().addRowClass(grid.getStore().indexOf(selectedEntities[i]), 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	grid.getTopToolbar().setDisabled(false);
    	grid.getBottomToolbar().setDisabled(false);
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@olt.reOpticalFinish@<br/>@COMMON.sucNum@: ' + successNum + ' @COMMON.failNum@: ' + failedNum + '</b>'
    	});
    }
}

//查询24小时内最低收光功率和一个月内最高收光功率
function queryOnuOpticalHistory(){
	//获取选择的设备
	selectedEntities = getSelectedEntities();
	if (selectedEntities.length == 0) {
	    return;
	}
	
	//批量初始化
	initBatch(selectedEntities);
	
	var successNum = 0,
	    failedNum = 0,
	    finishedIndex = [],
	    refreshedPonCounter = 0;
	
	//dwr接收处理函数
	window.top.addCallback("queryOnuOpticalHistory", function(perfOnuQualityHistory) {
		//重置超时限制 
		clearTimeout(timeoutTimer);
		timeoutTimer = setTimeout(function(){
	    	unexpectedEnd();
	    }, REFRESH_TIMEOUT);
	    for (var j = 0, len = selectedEntities.length; j < len; j ++ ) {
	    	//遍历找到对应的行
	    	if (selectedEntities[j].get('onuId') == perfOnuQualityHistory.onuId) {
	    		finishedIndex.push(j);
	    		selectedEntities[j].set('minOnuPonRevPower', perfOnuQualityHistory.minOnuPonRevPower);
	    		selectedEntities[j].set('minPowerTimeStr', perfOnuQualityHistory.minPowerTimeStr);
	    		selectedEntities[j].set('minCatvRevPower', perfOnuQualityHistory.minCatvRevPower);
	    		selectedEntities[j].set('minCatvTimeStr', perfOnuQualityHistory.minCatvTimeStr);
	    		selectedEntities[j].commit();
	    		//进入此处都认为是成功的
	    		grid.getView().removeRowClass(grid.getStore().indexOf(selectedEntities[j]), 'yellow-row');
	    		successNum++;
	    		$('#suc-num-dd').text(successNum);
	    	}
	    }
		//刷新完成处理
		refreshedPonCounter++;
	    if (refreshedPonCounter >= selectedEntities.length) {
	    	clearTimeout(timeoutTimer);
	    	outputResult();
	    }
	});
	
	var onuArray = [];
	
	for(var i = 0;i<selectedEntities.length;i++){
		//加入该设备 onuId
		onuArray[onuArray.length]  = selectedEntities[i].data.onuId;
	}
	
	//发送查询请求
    $.post('/onu/queryOnuOpticalHistory.tv',{
    	"onuIdList" : onuArray,
    	"jConnectedId": WIN.top.GLOBAL_SOCKET_CONNECT_ID
    });
    //发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, REFRESH_TIMEOUT);
	
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("queryOnuOpticalHistory");
    	//将正在处理行全部置为失败
    	for (var i = 0, len = selectedEntities.length; i < len; i++) {
    		if(!contains(finishedIndex, i)){
    			grid.getView().addRowClass(grid.getStore().indexOf(selectedEntities[i]), 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	grid.getTopToolbar().setDisabled(false);
    	grid.getBottomToolbar().setDisabled(false);
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            html: '<b class="orangeTxt">@olt.reLowestIn24h@<br/>@COMMON.sucNum@: ' + successNum + ' @COMMON.failNum@: ' + failedNum + '</b>'
    	});
    }
}

function showEntityMenu(record,event){
	if(record.data.attention)
		attentionAction = '@resources/COMMON.cancelAttention@'
	else
		attentionAction = '@resources/COMMON.attention@'
			
	if(record.data.laserSwitch == 1){
		onuLaser = '@ONU.closeLaserSwitch@'
	}else{
		onuLaser = '@ONU.openLaserSwitch@'
	}
		
	var toolMenu = [
	    {text:'Telnet', handler: onTelnetClick}
	];
	
	var voipMenu = [];
	voipMenu.push({text:'@gpon/onuvoip.sipUserConfig@', handler: sipPstnUserFn});
	voipMenu.push({text:'@gpon/onuvoip.voiplineStatus@', handler: voIPLineStatusFn});
	var $items = [];
	if(record.data.onuEorG == "E"){
		$items.push({id:"serverLevel",text: "@ONU.ServiceLevel@", handler:serverConfig, entityId:record.data.entityId});
		$items.push({id:"uniConfig",text: "@ONU.uniConfig@", handler:onuConfigFn, entityId:record.data.entityId});
		$items.push({id:"onuCpeList",text: "@ONU.cpeList@", handler:onuCpeFn, entityId:record.data.entityId});
		$items.push({id:'onuCapDeregister',text: "@ONU.unregister@", handler: unregisterHandler,disabled:!operationDevicePower});
		$items.push({id:'renameOnu',text: "@RESOURCES/COMMON.deviceInfo@",  handler: modifyDevice});
		$items.push({id:'addBaiduMap', text:"@network/BAIDU.viewMap@", handler : addBaiduMap});
		$items.push({id:'userAttention',text:attentionAction, handler:onAttentionClick});
		$items.push('-');
		$items.push({text:"@network/NETWORK.tool@", hidden: !operationDevicePower, menu:toolMenu});
		$items.push('-');
		$items.push({id:'onuAbility',text:"@ONU.abilityInfo@", handler : onOnuAbilityClick});
		$items.push({id:'onuLaserSwitch',text:onuLaser, handler : onuLaserClick,entityId:record.data.entityId});
		$items.push({id:'onuOpticalAlarm',text:"@EPON.optThrMgmt@", handler : onuOpticalAlarmHandler,disabled:!operationDevicePower});
		$items.push({id:"onutemperatureStatus",text:"@ONU.tempretureEnable@", handler : tempretureEnableSwitchHandler,disabled:!operationDevicePower});
		if(!ONU_MTK_TYPE.contains(record.data.onuPreType)){
			$items.push({id:'onuIsolationEnable',text:"@downlink.menu.isolatedEnable@", handler : isolatedEnableSwitchHandler,disabled:!operationDevicePower});
			$items.push({id:'onuRstpConfig',text:"@ONU.RSTPCfg@", handler : rstpCfgHandler,disabled:!operationDevicePower});
		}
		$items.push({id:'onuSlaConfigBack',text:"@ONU.slaMgmt@", handler : slaMgmtHandler,disabled:!operationDevicePower});
		$items.push({id:'onuPerfEnable',text:"@ONU/ONU.ponPerfStastic@", handler : stasitcEnableSwitchHandler,disabled:!operationDevicePower});
		$items.push({id:'onuReplace',text:"@epon/onu.replace.replace@", handler : replaceOnu,disabled:!operationDevicePower, entityId:record.data.entityId});
		$items.push({id:'onuIgmpConfig',text:"@COMMON.igmpConfig@", handler : showOnuIgmpConfig,disabled:!operationDevicePower, entityId:record.data.entityId});
		$items.push({id:'onuMacAgeTime',text:"@EPON/downlink.menu.macAgingTime@", handler : macAgeHandler,disabled:!operationDevicePower});
		$items.push({text: "@network/COMMON.editFolder@",handler: onMoveToTopo,disabled:!topoGraphPower});
		$items.push({text: "@COMMON.delete@", handler : onDeleteClick,disabled:!operationDevicePower});
		$items.push({text: "@COMMON.restore@", handler : onResetClick,disabled:!operationDevicePower});
	}
	if(record.data.onuEorG == "G"){
		/*if(record.data.topGponOnuCapOnuPotsNum != null&&record.data.topGponOnuCapOnuPotsNum>0 ){
			$items.push({id:"onuVoip",text: "VOIP", menu:voipMenu,entityId:record.data.entityId});
		}*/
		$items.push({id:"serverLevel",text: "@ONU.ServiceLevel@", handler:serverConfig, entityId:record.data.entityId});
		$items.push({id:'renameOnu',text: "@RESOURCES/COMMON.deviceInfo@",  handler: modifyDevice});
		$items.push({id:'addBaiduMap', text:"@network/BAIDU.viewMap@", handler : addBaiduMap});
		$items.push({id:'userAttention',text:attentionAction, handler:onAttentionClick});
		$items.push('-');
		$items.push({text:"@network/NETWORK.tool@", menu:toolMenu});
		$items.push('-');
		$items.push({id:'onuLaserSwitch',text:onuLaser, handler : onuLaserClick,entityId:record.data.entityId});
		$items.push({id:'onuAbility',text:"@ONU.abilityInfo@", handler : showGponOnuCapability});
		$items.push({id:"onuCpeList",text: "@ONU.cpeList@", handler:gponOnuCpeFn});
		$items.push({text: "@gpon/GPON.hostList@", handler :gponOnuHost,disabled:!operationDevicePower});
		$items.push({id:'onuPerfEnable',text:"@ONU/ONU.ponPerfStastic@", handler : stasitcEnableSwitchHandler,disabled:!operationDevicePower});
		$items.push({text: "@network/COMMON.editFolder@",handler: onMoveToTopo,disabled:!topoGraphPower});
		$items.push({text: "@COMMON.delete@", handler : onDeleteClick,disabled:!operationDevicePower});
		$items.push({text: "@COMMON.restore@", handler : onResetClick,disabled:!operationDevicePower});
	}
	
	if(cameraSwitch == 'on' && record.data.onuEorG == "E" && !ONU_MTK_TYPE.contains(record.data.onuPreType)){
		$items.push({text: '@CAMERA/CAMERA.bindCamera@', handler:bindCameraFn,disabled:!operationDevicePower});
	}
	// 增加上下线记录查看，gpon，epon都支持
	$items.push({text: "@ONU.viewOnOffRecord@", handler : viewOnOffRecordHandler});
	if(!entityMenu){
		entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 150, enableScrolling: false, items: $items});
	}else{
		entityMenu.removeAll();
		entityMenu.add( $items );
	}
	entityMenu.showAt( [event.clientX,event.clientY] );
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.override(Ext.grid.GridView,{  
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected"); 
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){  
	            var r = this.getRow(i);  
	            if(r){  
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	            }  
	        }  
	          
	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	          
	        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }  
	    },  
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
	            var selected = 0;  
	            var len = this.grid.store.getCount();  
	            for(var i = 0; i < len; i++){  
	                var r = this.getRow(i);  
	                if(r){  
	                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	                }  
	            }  
	            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	              
	            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');   
	            }  
	    }  
	});  
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	}); 
	var cmArr = [
	    sm,
        {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',align:'left',sortable:true,renderer:renderName},
        {id:"identity",header:"MAC | SN",dataIndex:'onuUniqueIdentification',sortable:true, width:150},
        {id:"onuOperationStatus",header:"@COMMON.status@",dataIndex:'onuOperationStatus',width:50,sortable:true,renderer: renderOnline},
        {id:"onuLevel",header:"@ONU.ServiceLevel@",dataIndex:'onuLevel',width:80,sortable:true,renderer: renderLevel},
        {id:"sysUpTime",header:"@network/NETWORK.sysUpTime@",dataIndex:'onuRunTime',width:100,sortable:true},
        {id:"onuDesc",header:'<div class="txtCenter">@COMMON.desc@</div>',dataIndex:'onuDesc', align:'left', sortable:true},
	    {id:"entityIp",header:'<div class="txtCenter">@ONU.belongOlt@</div>',dataIndex:'entityIp',width:160,align:'left', sortable:true,renderer: renderOlt},
	    {id:"onuIndex",header:"@ONU.onuAddr@",dataIndex:'onuIndex',sortable:true,width:60,renderer:renderIndex},
        {id:"onuPreType",header:"@ONU.type@",dataIndex:'typeName',width:80,sortable:true},
        {id:"testDistance",header:"@downlink.prop.testDistance@(m)",dataIndex:'onuTestDistance',width:80,sortable:true},
	    {id:"onuSoftwareVersion",header:"@ONU.softVersion@",dataIndex:'onuSoftwareVersion',sortable:true},
	    {id:"onuHardwareVersion",header:"@epon/EPON.hardwareVersion@",dataIndex:'topOnuHardwareVersion',sortable:true},
	    {id:"onuChipVendor",header:"@downlink.prop.chipvender@",dataIndex:'onuChipVendor',sortable:true,renderer: convert2Bit},
	    {id:"onuPonRevPower",header:"@ONU.recOptRate@",dataIndex:'onuPonRevPower',sortable:true,renderer: renderOnuPower},
	    {id:"minOnuPonRevPower",header:"@ONU/ONU.MinRecvPwr@",dataIndex:'minOnuPonRevPower',sortable:true,renderer: renderMinPower},
	    {id:"onuPonTransPower",header:"@ONU.sendOptRate@",dataIndex:'onuPonTransPower',sortable:true,renderer: renderPower},
	    {id:"oltponrevpower",header:"@ONU/ONU.ponRecvPwr@",dataIndex:'oltponrevpower',sortable:true,renderer: renderPower},
	    {id:"onuCatvOrInfoRxPower",header:"@ONU/ONU.CATVRecvPwr@",dataIndex:'onuCatvOrInfoRxPower',sortable:true,renderer: renderCATVPower},
	    {id:"minCatvRevPower",header:"@ONU/ONU.CATVMinRecvPwr@",dataIndex:'minCatvRevPower',sortable:true,renderer: renderMinCATVPower},
	    {id:"onucontact",header:"@ONU/ONU.contact@",dataIndex:'contact',sortable:true,hidden:true,renderer:addCellTooltip},
	    {id:"onulocation",header:"@ONU/ONU.location@",dataIndex:'location',sortable:true,hidden:true,renderer:addCellTooltip},
	    {id:"onunote",header:"@ONU/ONU.note@",dataIndex:'note',sortable:true,hidden:true,renderer:addCellTooltip},
	    {header: "@COMMON.manu@", width:120 ,renderer : manuRender, fixed:true}
	];
	var cmConfig = CustomColumnModel.init("onulist",cmArr,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'entityIp', direction: 'ASC'};
	store = new Ext.data.JsonStore({
	    url: '/onu/queryForOnuList.tv',
	    idProperty: "onuId",
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["onuUniqueIdentification","typeName","onuId","onuIndex","onuLevel","entityId","entityIp","name","onuType","ponPerfStats15minuteEnable","onuIsolationEnable",
	     	    "onuPreType","onuMac","onuOperationStatus","onuSoftwareVersion","topOnuHardwareVersion","temperatureDetectEnable","contact","location","note",
	     	   "onuChipVendor","onuPonRevPower","minOnuPonRevPower","minPowerTimeStr","onuCatvOrInfoRxPower","minCatvRevPower",
	     	   "minCatvTimeStr","onuPonTransPower","oltponrevpower","manageName", "onuDesc","attention","onuTestDistance",'onuEorG',"onuRunTime","topGponOnuCapOnuPotsNum",'laserSwitch']
	});
	//store.setDefaultSort('entityIp', 'ASC');
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
    store.load({params: {start:0,limit: pageSize}});
    
	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		cls:"normalTable edge10",
   		bodyCssClass: 'normalTable',
   		border:true,
   		store: store,
   		enableColumnMove : true,
   		title:"@ONU.onulist@",
   		cm : cm,
   		sm : sm,
   		loadMask : true,
   		bbar: buildPagingToolBar(),
   		viewConfig:{
   			forceFit: true
   		},
	   	listeners : {
   			sortchange : function(grid,sortInfo){
   				CustomColumnModel.saveSortInfo('onulist', sortInfo);
   			},
   		    columnresize: function(){
   	    	    CustomColumnModel.saveCustom('onulist', cm.columns);
   	        }
   		},
		store:store,
	   	tbar : new Ext.Toolbar({
			items : [{
				xtype: 'tbspacer', 
				width: 5
			},{
				id: 'refreshBtn',
				text    : '@ONU.refreshOpt@',
				iconCls : 'bmenu_refresh',
				disabled: true,
				handler : refreshOnuOptical
			},{
				id: 'findBtn',
				text    : '@ONU.findPowerHistory@',
				iconCls : 'bmenu_find',
				disabled: true,
				handler : queryOnuOpticalHistory
			}]
   		})
   	});
    var viewPort = new Ext.Viewport({
  	     layout: "border",
  	     items: [grid, {
	   		region: 'north',
	   		height: 95,
	   		contentEl : 'topPart',
				border: false
	       }]
  	}); 
});

//根据选中的行数判断是否将查询收光功率历史记录按钮置灰
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['findBtn','refreshBtn'], false);
    }else{
    	disabledBtn(['findBtn','refreshBtn'], true);
    }
}

//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
}

//绑定摄像头
function bindCameraFn(){
	var data = getSelectedEntity();
	var displayOnu = getLocationByIndex(data.onuIndex);
	var entityId = data.entityId;
	window.parent.createDialog("bindCamera", '@CAMERA/CAMERA.bindCamera@',  800, 500, "epon/camera/bindCamera.jsp?entityId="+entityId+"&onuIndex="+data.onuIndex+"&displayOnu="+displayOnu, null, true, true);
}

function replaceOnu(){
     var record = grid.getSelectionModel().getSelected();
	 var entityId = record.data.entityId;
	 var onuId = record.data.onuId;
	 window.top.createDialog('replaceOnu', '@epon/onu.replace.replace@', 600, 370,
	            '/onu/replace/showOnuReplaceView.tv?onuId=' + onuId, null, true, true);
}

function tempretureEnableSwitchHandler() {
	var $temperatureDetectEnable = fetchTemperatureDetectEnable() ;
	$temperatureDetectEnable = $temperatureDetectEnable == 1 ? 2: 1;
	tempretureEnableHandler(function() {
		updateRowAttribute(onuId, 'temperatureDetectEnable', $temperatureDetectEnable);
	})
}

function isolatedEnableSwitchHandler() {
	var $onuIsolationEnable = fetchOnuIsolationEnable();
    $onuIsolationEnable = $onuIsolationEnable == 1 ? 2: 1;
    isolatedEnableHandler(function() {
		updateRowAttribute(onuId, 'onuIsolationEnable', $onuIsolationEnable);
	});
}

function stasitcEnableSwitchHandler() {
	var $ponPerfStats15minuteEnable = fetchPonPerfStats15minuteEnable();
    $ponPerfStats15minuteEnable = $ponPerfStats15minuteEnable == 1 ? 2: 1;
    stasitcEnableHandler(function() {
    	updateRowAttribute(onuId, 'ponPerfStats15minuteEnable', $ponPerfStats15minuteEnable);
	});
}

function unregisterHandler(entityId, onuId) {
    var record = grid.getSelectionModel().getSelected();
    var entityId = record.data.entityId;
    var onuId = record.data.onuId;
    tishi1 = "@ONUVIEW.deregisterTip@";
    tishi2 = "@ONUVIEW.deregisterError@";
    tishi3 = "@ONUVIEW.deregistering@";
    window.parent.showConfirmDlg("@COMMON.tip@", tishi1, function(type) {
        if (type == 'no') return
        top.showWaitingDlg("@COMMON.wait@", tishi3);
        $.ajax({
            url: '/onu/deregisterOnu.tv',data: "entityId=" + entityId + "&onuId=" + onuId,
            success: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title: "@COMMON.tip@",
                	html:  "@ONU.unregisterOk@"
            	});
            }, error: function() {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
                	title: "@COMMON.tip@",
                	html: tishi2
                })
            }, cache: false
        });
    });
}

//新增onu上下线记录查看，弹出框显示
function viewOnOffRecordHandler(){
	var onu = getSelectedEntity();
	if (onu) {
		showOnOffRecordsView(onu.onuId, onu.onuIndex, onu.entityId);
	}
}

function fetchPonPerfStats15minuteEnable(){
	return getSelectedEntity().ponPerfStats15minuteEnable;
}
function fetchTemperatureDetectEnable(){
	return getSelectedEntity().temperatureDetectEnable;
}
function fetchOnuIsolationEnable(){
	return getSelectedEntity().onuIsolationEnable;
}
//gpon onu能力信息;
function showGponOnuCapability(){
	var onu = getSelectedEntity();
	top.createDialog("modalDlg", "@ONU.abilityInfo@", 800, 500, "/gpon/onu/showGponOnuCapability.tv?onuId=" + onu.onuId, null, true, true);
	
}

//onu 业务配置;
function onuConfigFn(){
	var onu = getSelectedEntity();
	window.top.addView('uniserviceconfig-'+onu.onuId, '@ONU.uniConfig@[' + onu.name +']','icoD1', '/onu/showUniServiceConfig.tv?onuId='+onu.onuId + "&entityId="+onu.entityId,null,true);
}



function serverConfig(){
	var onu = getSelectedEntity();
	top.createDialog("onuServerLevel", "@ONU.ServiceLevelConfig@", 600, 370, "/onu/showOnuServerLevel.tv?onuId=" + onu.onuId + "&onuLevel=" + onu.onuLevel, null, true, true);
	
}

/**
 * 展示ONU CPE列表
 */
function onuCpeFn(){
	var onu = getSelectedEntity();
	window.parent.addView('entity-' + onu.onuId, unescape(onu.name), 'entityTabIcon', '/epon/onucpe/showOnuCpeList.tv?module=6&onuId=' + onu.onuId );
}

function gponOnuCpeFn(){
	var onu = getSelectedEntity();
	window.parent.addView('entity-' + onu.onuId, unescape(onu.name), 'entityTabIcon', '/gpon/onu/showOnuCpeList.tv?module=9&onuId=' + onu.onuId );
}

function gponOnuHost(){
	var onu = getSelectedEntity();
	window.parent.addView('entity-' + onu.onuId, unescape(onu.name), 'entityTabIcon', '/gpon/onu/showOnuHostList.tv?module=6&onuId=' + onu.onuId );
}

function showLoading(message) {
	if(message){
		$("#loading").text(message);
	}
	var lPos = ($(window).width() - $("#loading").outerWidth()) / 2;
	var tPos = 200;
	$("#loading").show().css({
		top: tPos,
		left: lPos
	});
}

function hideLoading() {
  $("#loading").hide();
}

//判断数组中是否包含指定元素
function contains(array, obj){
	var i = array.length;
	while(i--){
		if(array[i] == obj){
			return true;
		}
	}
	return false;
}

//显示ONU IGMP配置
function showOnuIgmpConfig(){
	 var record = grid.getSelectionModel().getSelected();
	 var entityId = record.data.entityId;
	 var onuId = record.data.onuId;
	 var onuIndex = record.data.onuIndex;
	 var name = record.data.name;
	window.parent.addView('ONUIGMP-' + onuId, "ONU IGMP[" + name + "]" , 'entityTabIcon','/epon/igmpconfig/showOnuIgmpConfig.tv?entityId=' + entityId + '&onuId=' + onuId + '&onuIndex=' + onuIndex);
}

function sipPstnUserFn(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var onuId = record.data.onuId;
    window.top.createDialog('sipPstnUser', "@gpon/onuvoip.sipUserConfig@", 600, 370,
            '/gpon/onuvoip/showSIPPstnUser.tv?onuId=' + onuId, null, true, true);
}

function voIPLineStatusFn(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var onuId = record.data.onuId;
    window.top.createDialog('voipLineStatus', "@gpon/onuvoip.voiplineStatus@", 600, 370,
            '/gpon/onuvoip/showVoIPLineStatus.tv?onuId=' + onuId, null, true, true);
}


//渲染光接收功率（指定对应的时间）
//v : value
//r : record
//timestamp : r.data.minPowerTime|maxPowerTime
function showMinWithTime(v,r,time){
	if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
		return " -- ";
	}
	return (Number(v)).toFixed(2) + " dBm("+ time + ")";
}

//渲染CATV光接收功率（指定对应的时间）
//v : value
//r : record
//timestamp : r.data.minPowerTime|maxPowerTime
function showCATVMinWithTime(v,r,time){
	if(r.data.onuOperationStatus != 1 ||  v == null || v == 0){
		return " -- ";
	}
	return (Number(v)/10).toFixed(2) + " dBm("+ time + ")";
}