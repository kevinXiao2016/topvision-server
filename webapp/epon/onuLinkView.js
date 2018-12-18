var entityMenu = null;
grid = null,
store = null,
pagingToolbar = null,
cmArr = null,
partitionData = {
	receiveMin:  '',
	receiveMax:  '',
    transmitMin: '',
    transmitMax: '',
    ponRecvMin:  '',
    ponRecvMax:  ''
};
//刷新超时时间
var REFRESH_TIMEOUT = 30000;

var deactiveStatusIcomcls = "enableIconClass";

function partitionChanged(data){
	partitionData = data;
	$('#partition').onuIndexPartition.change(partitionData);
}

function manuRender(v,m,r){
	var str1 = String.format("<a href='javascript:;' onClick='refreshOnu();'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
			   r.id),
	    str2 = String.format("<span>@COMMON.refresh@</span> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
	 		   r.id);
	if(!refreshDevicePower) {
		return str2;
	}else{
		return str1;
	}
}

function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	WIN.entityId = record.data.entityId;
	WIN.onuId = record.data.onuId;
	WIN.onuIndex = record.data.onuIndex;
	WIN.onulocation = getLocationByIndex(record.data.onuIndex);
	grid.getSelectionModel().selectRecords([record]);
	showEntityMenu(event, makeItems(record));
}

/**
 * 显示更多菜单
 * @param event
 * @param items
 */
function showEntityMenu(event,items){
	if(!entityMenu){
		entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 150, enableScrolling: false, items: items});
	}else{
		entityMenu.removeAll();
		entityMenu.add( items );
	}
	entityMenu.showAt( [event.clientX,event.clientY] );
}

function makeItems(record){
	var $items = [];
	var onuEorG = record.data.onuEorG;
	var onuDeactive = record.data.onuDeactive;
	if (onuDeactive == 1) {
		deactiveStatusIcomcls = "disableIconClass";
    } else {
    	deactiveStatusIcomcls = "enableIconClass";
    }
	if(onuEorG == "E"){
		$items.push({id:'onuCapDeregister',text: "@ONU.unregister@", handler: unregisterOnu,disabled:!operationDevicePower});
	}else if(onuEorG == "G"){
		$items.push({id:'onuDeactive',text: "@COMMON.deactive@", handler: deactiveOnu, iconCls:deactiveStatusIcomcls,disabled:!operationDevicePower});
	}
	$items.push({text: "@COMMON.restore@", handler : resetOnu, disabled:!operationDevicePower});
	$items.push({text: "@COMMON.delete@", handler : deleteOnu, disabled:!operationDevicePower});
	return $items;
}

/**
 * 重新加载关联信息
 */
function reOnuLocate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		showOnuLocate(sm.getSelected());
	}
}

/**
 * 解注册
 */
function unregisterOnu(){
	var onu = getSelectedEntity(grid);
	var onuEorG = onu.onuEorG;
	if(onuEorG == "E"){
		var callbacks = [refresh, reOnuLocate];
		if(onu){
			var entityId = onu.entityId, onuId = onu.onuId;
			unregisterOnuFn(entityId, onuId, callbacks);
		}
	}else if(onuEorG == "G"){
		deactiveOnu();
	}
	
}

/**
 * 激活/去激活
 */
function deactiveOnu(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var onuId = record.data.onuId;
    var onuDeactiveTemp = record.data.onuDeactive == 1 ? 2 : 1;
    var action = onuDeactiveTemp == 1 ? "@COMMON.active@" : "@COMMON.deactive@";
    window.parent.showConfirmDlg("@COMMON.tip@", "@COMMON.confirm@ "+action+" ONU?", function(type) {
        if (type == 'no') {
            return;
        }
        top.showWaitingDlg("@COMMON.wait@", "@COMMON.ing@ "+action);
        $.ajax({
            url:'/onu/onuDeactive.tv?onuId=' + onuId + '&onuDeactive=' + onuDeactiveTemp,
            type:'POST',
            dateType:'text',
            success:function(response) {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">'+action+'@COMMON.success@！</b>'
	   			});
            	store.reload();
            },
            error:function() {
            	top.closeWaitingDlg("@COMMON.tip@");
                window.parent.showMessageDlg("@COMMON.tip@", action + "@COMMON.fail@！");
            },
            cache:false
        });
    });
}

/**
 * 复位
 */
function resetOnu(){
	var callbacks = [refresh, reOnuLocate];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		resetOnuFn(entityId, onuId, callbacks);
	}
}

/**
 * 删除
 */
function deleteOnu(){
	var callbacks = [refresh];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, onuEorG = onu.onuEorG;
		deleteOnuFn(entityId, onuId, onuEorG, callbacks);
	}
}

/**
 * 关注
 */
function attentionOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, attention = onu.attention;
		attentionOnuFn(entityId, onuId, attention, refresh);
	}
}

/**
 * 标签
 */
function tagOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var onuId = onu.onuId, tagId = onu.tagId;
		tagOnuFn(onuId, tagId, refresh);
	}
}

/**
 * 刷新onu信息
 */
function refreshOnu(){
	var callbacks = [refresh, reOnuLocate];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, onuIndex = onu.onuIndex;
		refreshOnuFn(entityId, onuId, onuIndex, callbacks);
	}
}

function renderPower(v,m,r){
	if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
		return " -- ";
	}
	return (Number(v)).toFixed(2);
}

function renderOnuPonRec(v,m,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="ONU_PON_RE_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				return "<label>--</label>";
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				return "<label style='color:red;'>" + (Number(v)).toFixed(2) + " </label>";
			}
			return "<label>" + (Number(v)).toFixed(2) + " </label>";
		}
	}
}

function renderOnuPonTrans(v,m,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="ONU_PON_TX_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				return "<label>--</label>";
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				return "<label style='color:red;'>" + (Number(v)).toFixed(2) + " </label>";
			}
			return "<label>" + (Number(v)).toFixed(2) + " </label>";
		}
	}
}

function renderOltPonRec(v,m,r) {
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId=="OLT_PONLLID_RE_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
				return "<label>--</label>";
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				return "<label style='color:red;'>" + (Number(v)).toFixed(2) + " </label>";
			}
			return "<label>" + (Number(v)).toFixed(2) + " </label>";
		}
	}
}

function renderCATVRec(v,m,r) {
	v = (Number(v)/10).toFixed(2);
	for(var i=0;i< onuLinkThreshold.length;i++){
		if(onuLinkThreshold[i].targetId==="ONU_CATV_RX_POWER" && r.data.templateId === onuLinkThreshold[i].templateId){
			var rule = onuLinkThreshold[i];
			var thresholds = rule.thresholds.split("_");
			var max = thresholds[1],min = thresholds[4];
			if(r.data.onuOperationStatus != 1 ||  v == null || v == 0|| v < -20){
				return "<label>--</label>";
			} else if(parseFloat(v) > max || parseFloat(v) < min){
				return "<label style='color:red;'>" + v + " </label>";
			}
			return "<label>" + v + " </label>";
		}	
	}
}

function renderMinPower(v,m,r){
	if(r.data.onuOperationStatus != 1 ||  v == null || parseInt(v, 10) <= -100 || v == 0){
		return " -- ";
	}
	return (Number(v)).toFixed(2) + " ("+ r.data.minPowerTimeStr + ")";
}

function renderMinCATVPower(v,m,r){
	if(r.data.onuOperationStatus != 1 ||  v == null || v == 0){
		return " -- ";
	}
	return (Number(v)/10).toFixed(2) + " dBm("+ r.data.minPowerTimeStr + ")";
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
	  showLoading0();
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
	    		selectedEntities[j].set('oltPonRevPower', onuOptical.link.oltRevResult);
	    		selectedEntities[j].set('onuCatvOrInfoRxPower', onuOptical.catv == null ? 0 : onuOptical.catv.onuCatvOrInfoRxPower);
	    		selectedEntities[j].set('oltPonTransPower', onuOptical.oltPon.transPower);
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
	
	var columnArr = [
	    sm,
        {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',align:'left',sortable:true,renderer:renderName},
        {id:"Mac",header:"MAC",dataIndex:'onuMac',align:'center',sortable:true, width:150,renderer: renderEponMac},
        {id:"gponSN",header:"GPON SN",dataIndex:'onuUniqueIdentification',align:'center',sortable:true, width:150, renderer: renderGponSn},
        {id:"onuOperationStatus",header:"<div class='txtCenter'>@COMMON.status@</div>",dataIndex:'onuOperationStatus',width:50,sortable:true,align:'left',renderer: renderOnline},
        {id:"sysUpTime",header:"@network/NETWORK.sysUpTime@",dataIndex:'changeTime',width:100,sortable:true, renderer:renderOnOffTime},
        {id:"testDistance",header:"@downlink.prop.testDistance@(m)",dataIndex:'onuTestDistance',width:80,sortable:true},
        {id:"entityIp",header:'<div class="txtCenter">@ONU.entity@</div>',dataIndex:'entityIp',width:160,align:'left', sortable:true,renderer: renderOlt},
        {id:"logicLocation",header:"@ONU.logicLocation@",dataIndex:'onuIndex',sortable:true,width:60,renderer:renderLogicLoc},
        {id:"laserSwitch",header:'<div class="txtCenter">@ONU.laserSwitch@</div>',dataIndex:'laserSwitch', align:'center', sortable:true,hidden:true,renderer:renderLaser},
        {id:"onuPonRevPower",header:"@ONU.recOptRate@",dataIndex:'onuPonRevPower',sortable:true,renderer: renderOnuPonRec},
        {id:"minOnuPonRevPower",header:"@ONU.minRecOptRate@",dataIndex:'minOnuPonRevPower',sortable:true,renderer: renderMinPower},
        {id:"onuPonTransPower",header:"@ONU.sendOptRate@",dataIndex:'onuPonTransPower',sortable:true,renderer: renderOnuPonTrans},
        {id:"oltPonRevPower",header:"@ONU/ONU.ponRecvPower@",dataIndex:'oltPonRevPower',sortable:true,renderer: renderOltPonRec},
        {id:"oltPonTransPower",header:"@ONU/ONU.ponTransPower@",dataIndex:'oltPonTransPower',align:'center',sortable:true, hidden:true,renderer: renderPower},
        {id:"onuCatvOrInfoRxPower",header:"@ONU/ONU.CATVRecvPwr@",dataIndex:'onuCatvOrInfoRxPower',sortable:true,renderer: renderCATVRec},
        {id:"minCatvRevPower",header:"@ONU/ONU.CATVMinRecvPwr@",dataIndex:'minCatvRevPower',sortable:true,renderer: renderMinCATVPower},
	    {header: "@COMMON.manu@", width:120 ,renderer : manuRender, fixed:true}
	];
	//不需要显示列的 id， 纯epon下不需要显示["gponSn"], 纯gpon下不需要显示["mac"]
	var spliceColIds = [["gponSN"],["Mac"]];
	//根据环境初始化显示列
	cmArr = initColumnArr(onuEnvironment, columnArr.slice(0), spliceColIds);
	
	var cmConfig = CustomColumnModel.init("onulist",cmArr,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'entityIp', direction: 'ASC'};
	store = new Ext.data.JsonStore({
	    url: '/onulist/queryOnuLinkList.tv',
	    idProperty: "onuId",
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["onuUniqueIdentification","onuId","onuIndex","entityId","entityIp","name","portIndex","tagId",
	     	    "onuMac","onuOperationStatus","location","attention",'lastOfflineReason',"templateId",
	     	   "onuPonRevPower","minOnuPonRevPower","minPowerTimeStr","onuCatvOrInfoRxPower","minCatvRevPower",
	     	   "minCatvTimeStr","onuPonTransPower","oltPonRevPower","oltPonTransPower","manageName", "onuTestDistance",'onuEorG',"onuRunTime","changeTime",'laserSwitch', "onuDeactive"]
	});
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
   		title:"@ONU.onuLinkView@",
   		cm : cm,
   		sm : sm,
   		loadMask : true,
   		bbar: buildPagingToolBar(pageSize,store),
   		/*viewConfig:{
   			forceFit: true
   		},*/
	   	listeners : {
	   		rowclick: function(grid, rowIndex, e) {
	   			reOnuLocate();
	   	    },
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

function showLoading0(message) {
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
