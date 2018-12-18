var defaultSniColumn = [
	       {header:"<div class='txtCenter'>@EPON.portNum@</div>", align: 'left', width:80, dataIndex: 'portLocation', renderer: portLocationRender, sortable:true},
	       {header:'@REALTIME.portName@', width:80, dataIndex: 'portName', sortable:true},
	       {header:'@report.adminStatus@', width: 80, dataIndex: 'adminStatus', renderer : statusRender, sortable:true},
	       {header:'@report.operStatus@', width: 80, dataIndex: 'operationStatus', renderer : statusRender, sortable:true},
	       {header:'@EPON.portType@', width: 80, dataIndex: 'portType', renderer : sniTypeRender, sortable:true},
	       {header:'@REALTIME.negoMode@', width: 90, dataIndex: 'autoNegoMode', renderer : negoModeRender, sortable:true},
	       {header:'@ONU.flowcontrol@', width: 80, dataIndex: 'flowCtrlEnable', renderer : flowCtrlRender, sortable:true},
	       {header:'@olt.upLimit@(Kbps)', width: 130, dataIndex: 'upLimitRate', renderer : sniRateLimitRender, sortable:true} ,
	       {header:'@olt.downLimit@(Kbps)', width: 130, dataIndex: 'downLimitRate', renderer : sniRateLimitRender, sortable:true},
	       {header:'@report.inFlow@(Mbps)', width: 130, dataIndex: 'inCurrentRate', renderer : currentRateRender, sortable:true},
	       {header:'@report.outFlow@(Mbps)', width: 130, dataIndex: 'outCurrentRate', renderer : currentRateRender, sortable:true},
	       {header:'@Optical.tx@(dBm)', width: 115, dataIndex: 'txPower', renderer : powerRender, sortable:true},
	       {header:'@Optical.rx@(dBm)', width: 115, dataIndex: 'rxPower', renderer : powerRender, sortable:true},
	       {header:'@Optical.workingV@(mV)', width: 105, dataIndex: 'workingVoltage', renderer : voltageRender, sortable:true},
	       {header:'@Optical.biasCurrent@(μA)', width: 105, dataIndex: 'biasCurrent', renderer : baisCurrentRender, sortable:true},
	       {header:'@Optical.workingTemp@' + '(@{unitConfigConstant.tempUnit}@)', width: 100, dataIndex: 'workTempDisplay', renderer : workTempRender, sortable:true},
	       {header:'@Optical.identifier@', width: 95, dataIndex: 'identifier', renderer : fiberTypeRender, sortable:true},
	       {header:'@Optical.vendorName@', width: 100, dataIndex: 'vendorName', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.waveLength@(nm)', width: 80, dataIndex: 'waveLength', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.vendorPN@', width: 100, dataIndex: 'vendorPN', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.vendorSN@', width: 100, dataIndex: 'vendorSN', renderer : normalValueRender, sortable:true},
	       {header:'@COMMON.modifyTime@', width: 120, dataIndex: 'modifyTimeStr', renderer : normalValueRender, sortable:true},
	       {header:'@COMMON.manu@', width: 130, dataIndex: 'op', renderer: sniOperation, fixed:true}
	],
	sniStore = new Ext.data.JsonStore({
	 	url: '/epon/portinfo/loadSniOpticalInfoList.tv',
	    remoteSort: false,
	    fields: [
             "entityId","slotId","portId","portIndex","portName","operationStatus","adminStatus","inCurrentRate","outCurrentRate","perfStats",
             "autoNegoMode","flowCtrlEnable","upLimitRate","downLimitRate","identifier","vendorName","waveLength","vendorPN","vendorSN",
             "workingTemp","workingVoltage","biasCurrent","txPower","rxPower","modifyTime","portLocation","portType", "slotType","modifyTimeStr","workTempDisplay"
	      ]
	}),
		
	defaultPonColumn = [
	       {header:"<div class='txtCenter'>@EPON.portNum@</div>", align: 'left', width:70, dataIndex: 'portLocation', renderer: portLocationRender, sortable:true},
	       {header:'@report.adminStatus@', width: 80, dataIndex: 'adminStatus', renderer : statusRender, sortable:true},
	       {header:'@report.operStatus@', width: 80, dataIndex: 'operationStatus', renderer : statusRender, sortable:true},
	       {header:'@EPON.portType@', width: 80, dataIndex: 'portType', renderer : ponTypeRender, sortable:true},
	       {header:'@olt.upLimit@(Kbps)', width: 130, dataIndex: 'upLimitRate', sortable:true} ,
	       {header:'@olt.downLimit@(Kbps)', width: 130, dataIndex: 'downLimitRate', sortable:true},
	       {header:'@report.inFlow@(Mbps)', width: 130, dataIndex: 'inCurrentRate', renderer : currentRateRender, sortable:true},
	       {header:'@report.outFlow@(Mbps)', width: 130, dataIndex: 'outCurrentRate', renderer : currentRateRender, sortable:true},
	       {header:'@Optical.tx@(dBm)', width: 115, dataIndex: 'txPower', renderer : powerRender, sortable:true},
	       {header:'@Optical.rx@(dBm)', width: 115, dataIndex: 'rxPower', renderer : powerRender, sortable:true},
	       {header:'@Optical.workingV@(mV)', width: 105, dataIndex: 'workingVoltage', renderer : voltageRender, sortable:true},
	       {header:'@Optical.biasCurrent@(μA)', width: 105, dataIndex: 'biasCurrent', renderer : baisCurrentRender, sortable:true},
	       {header:'@Optical.workingTemp@' + '(@{unitConfigConstant.tempUnit}@)', width: 100, dataIndex: 'workTempDisplay', renderer : workTempRender, sortable:true},
	       {header:'@Optical.identifier@', width: 95, dataIndex: 'identifier', renderer : fiberTypeRender, sortable:true},
	       {header:'@Optical.vendorName@', width: 100, dataIndex: 'vendorName', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.waveLength@(nm)', width: 80, dataIndex: 'waveLength', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.vendorPN@', width: 100, dataIndex: 'vendorPN', renderer : normalValueRender, sortable:true},
	       {header:'@Optical.vendorSN@', width: 100, dataIndex: 'vendorSN', renderer : normalValueRender, sortable:true},
	       {header:'@COMMON.modifyTime@', width: 120, dataIndex: 'modifyTimeStr', renderer : normalValueRender, sortable:true},
	       {header:'@COMMON.manu@', width: 130, dataIndex: 'op', renderer: ponOperation, fixed:true}
   	],
	
    ponStore = new Ext.data.JsonStore({
		url: '/epon/portinfo/loadPonOpticalInfoList.tv',
	    remoteSort: false,
	    fields: [
	         "entityId","slotId","portId","portIndex","portName","operationStatus","adminStatus","inCurrentRate","outCurrentRate","perfStats",
	         "autoNegoMode","flowCtrlEnable","upLimitRate","downLimitRate","identifier","vendorName","waveLength","vendorPN","vendorSN",
	         "workingTemp","workingVoltage","biasCurrent","txPower","rxPower","modifyTime","portLocation","portType", "slotType","modifyTimeStr","workTempDisplay"
	      ]
			
	}),
	sniGrid = null,
	ponGrid = null,
	tab1;

//var sniColumn = CustomColumnModel.init('oltSniList', defaultSniColumn, {});
//var ponColumn = CustomColumnModel.init('oltPonList', defaultPonColumn, {});

var sniConfig   = CustomColumnModel.init("oltSniList",defaultSniColumn,{}),
    sniColumn   = sniConfig.cm,
    sniSortInfo = sniConfig.sortInfo || {field: 'portIndex', direction: 'ASC'};
    
var ponConfig   = CustomColumnModel.init("oltPonList",defaultPonColumn,{}),
    ponColumn   = ponConfig.cm,
    ponSortInfo = ponConfig.sortInfo || {field: 'portIndex', direction: 'ASC'};    

var slotTypeArr = ["non-board","mpua","mpub","epua","epub","geua","geub","xgua","xgub","xguc","xpua","mpu-geua","mpu-geub","mpu-xguc","gpua","epuc","epud","meua","meub","mefa","mefb","mefc","mefd","mgua","mgub","mgfa","mgfb"];
var negoModeArr = ["unknown", "auto-negotiate", "half-10", "full-10", "half-100", "full-100", "full-1000", "full-10000"];
var fiberTypeArr = ["unknown", "GBIC", "@Optical.moduleType@", "SFP","XBI","XENPAK","XFP","XFF","XFP_E","XPAK","X2"];
var sniTypeArr = ["unknown", "twistedPair", "fiber", "other"];
var ponTypeArr = ["unknown", "ge-epon", "tenge-epon", "gpon", "other"];

//刷新超时时间
var REFRESH_TIMEOUT = 30000;

//端口号(板卡类型)
function portLocationRender(value, meta, record){
	var slotType = record.data.slotType;
	var typeName;
	if(slotType == null || slotType == '' || slotType == 255){
		typeName = "unknown";
	}else{
		typeName = slotTypeArr[slotType];
	}
	return value + "(" + typeName + ")";
}

//端口状态
function statusRender(value, meta, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
				 "up");
	} else if(value == 2) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
				"down");
	}else if(value == 3){
		return "testing";
	}
}

//自协商模式
function negoModeRender(value, meta, record){
	return negoModeArr[value];
}

//SNI端口限速
function sniRateLimitRender(value, meta, record){
	return value;
}
//实时速率
function currentRateRender(value, meta, record){
	if(value === null || value === -1){
		return "--";
	}else{
		return value;
	}
}
//光功率
function powerRender(value, meta, record){
	if(value == null || value == 0){
		return "--";
	}else{
		return (Math.round((10 * (Math.log(value) / Math.LN10) - 40) * 10000) / 10000);
	}
}
//电压
function voltageRender(value, meta, record){
	if(value == null || value == 0){
		return "--";
	}else{
		return value / 10;
	}
}
//光模板类型
function fiberTypeRender(value, meta, record){
	if(value == null){
		return "--";
	}else{
		return fiberTypeArr[value];
	}
}
//偏置电流
function baisCurrentRender(value, meta, record){
	if(value == null || value == 0){
		return "--";
	}else{
		return 2 * value;
	}
}

//偏置电流
function workTempRender(value, meta, record){
	if(value == null || parseInt(value, 10) <= 0){
		return "--";
	}else{
		return value;
	}
}

function normalValueRender(value, meta, record){
	if(value == null || value === '' ){
		return "--";
	}else{
		return value;
	}
}

function sniTypeRender(value, meta, record){
	return sniTypeArr[value];
}

function ponTypeRender(value, meta, record){
	return ponTypeArr[value];
}

function flowCtrlRender(value, meta, record){
	if(value == 1){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
				"enable");
	}else{
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
				"disable");
	}
}

function sniOperation(value, meta, record){
	var portType = record.data.portType;
	if(portType == 2){
		return "<a href='javascript:;' onClick='refreshSniOptical()'>@olt.refreshOptical@</a>"; 
	}else{
		return "--";
	}
}

function refreshSniOptical(){
	var selectRecord = sniGrid.getSelectionModel().getSelected();
	var entityId = selectRecord.data.entityId;
	var portIndex = selectRecord.data.portIndex;
	var portId = selectRecord.data.portId;
	var perfStats = selectRecord.data.perfStats;
	window.top.showWaitingDlg('@COMMON.waiting@', '@COMMON.refreshing@', 'ext-mb-waiting');
	$.ajax({
	    url: '/epon/portinfo/refreshSniOpticalInfo.tv',
	    data: {
	    	entityId : entityId,
	    	portIndex : portIndex,
	    	portId : portId,
	    	perfStats : perfStats
	    },
	    type: 'post',
	    dataType: "json",
	    cache: false,
	    success: function(json) {
	    	window.top.closeWaitingDlg();
	    	top.afterSaveOrDelete({
	    		title: '@COMMON.tip@',
	    		showTime: 2000,
	    		html: '<b class="orangeTxt">@olt.reOpticalSuc@</b>'
	    	});
	    	//更新该行数据
	    	var sniPortOptical = json;
	    	selectRecord.beginEdit();
	    	for (var key in sniPortOptical) {
	    		//只更新光功率相关内容
	    		if($.inArray(key, [ "adminStatus","operationStatus","inCurrentRate","outCurrentRate","identifier","vendorName","waveLength","vendorPN","vendorSN","workingTemp","workingVoltage","biasCurrent","txPower","rxPower","modifyTime","modifyTimeStr","workTempDisplay"]) !=-1){
	    			sniPortOptical[key] && selectRecord.set(key, sniPortOptical[key]);
	    		}
	    	}
	    	selectRecord.commit();
	    },
	    error: function(response) {
	    	window.parent.showMessageDlg('@COMMON.tip@', '@ISOGROUP.refreshFail@');
	    }
	  });
}

function ponOperation(value, meta, record){
	return "<a href='javascript:;' onClick='refreshPonOptical()'>@olt.refreshOptical@</a>";
}

function refreshPonOptical(){
	var selectRecord = ponGrid.getSelectionModel().getSelected();
	var entityId = selectRecord.data.entityId;
	var portIndex = selectRecord.data.portIndex;
	var portId = selectRecord.data.portId;
	var perfStats = selectRecord.data.perfStats;
	window.top.showWaitingDlg('@COMMON.waiting@', '@COMMON.refreshing@', 'ext-mb-waiting');
	$.ajax({
	    url: '/epon/portinfo/refreshPonOpticalInfo.tv',
	    data: {
	    	entityId : entityId,
	    	portIndex : portIndex,
	    	portId : portId,
	    	perfStats : perfStats
	    },
	    type: 'post',
	    dataType: "json",
	    cache: false,
	    success: function(json) {
	    	window.top.closeWaitingDlg();
	    	top.afterSaveOrDelete({
	    		title: '@COMMON.tip@',
	    		html: '<b class="orangeTxt">@olt.reOpticalSuc@</b>'
	    	});
	    	//更新该行数据
	    	var ponPortOptical = json;
	    	selectRecord.beginEdit();
	    	for (var key in ponPortOptical) {
	    		//只更新光功率相关内容
	    		if($.inArray(key, ["adminStatus","operationStatus","inCurrentRate","outCurrentRate","identifier","vendorName","waveLength","vendorPN","vendorSN","workingTemp","workingVoltage","biasCurrent","txPower","rxPower","modifyTime","modifyTimeStr","workTempDisplay"]) !=-1){
	    			ponPortOptical[key] && selectRecord.set(key, ponPortOptical[key]);
	    		}
	    	}
	    	selectRecord.commit();
	    },
	    error: function(response) {
	    	window.parent.showMessageDlg('@COMMON.tip@', '@ISOGROUP.refreshFail@');
	    }
	  });
}

function refreshAllSniOptical() {
	if (sniStore.getCount() == 0) {
	    return;
	}
	//批量初始化
	initBatch();
	var successNum = 0,
	    failedNum = 0,
	    finishedIndex = [],
	    refreshedSniCounter = 0;
	
	//dwr接收处理函数
	window.top.addCallback("refreshAllSniOptical", function(sniOptical) {
		//重置超时限制 
		clearTimeout(timeoutTimer);
		timeoutTimer = setTimeout(function(){
	    	unexpectedEnd();
	    }, REFRESH_TIMEOUT);
	    for (var j = 0, len = sniStore.getCount(); j < len; j ++ ) {
	    	//遍历找到对应的行
	    	if (sniStore.getAt(j).get('portId') == sniOptical.portId) {
	    		finishedIndex.push(j);
	    		sniStore.getAt(j).set('adminStatus', sniOptical.adminStatus);
	    		sniStore.getAt(j).set('operationStatus', sniOptical.operationStatus);
	    		sniStore.getAt(j).set('inCurrentRate', sniOptical.inCurrentRate);
	    		sniStore.getAt(j).set('outCurrentRate', sniOptical.outCurrentRate);
	    		sniStore.getAt(j).set('identifier', sniOptical.identifier);
	    		sniStore.getAt(j).set('vendorName', sniOptical.vendorName);
	    		sniStore.getAt(j).set('waveLength', sniOptical.waveLength);
	    		sniStore.getAt(j).set('vendorPN', sniOptical.vendorPN);
	    		sniStore.getAt(j).set('vendorSN', sniOptical.vendorSN);
	    		sniStore.getAt(j).set('workingTemp', sniOptical.workingTemp);
	    		sniStore.getAt(j).set('workTempDisplay', sniOptical.workTempDisplay);
	    		sniStore.getAt(j).set('workingVoltage', sniOptical.workingVoltage);
	    		sniStore.getAt(j).set('biasCurrent', sniOptical.biasCurrent);
	    		sniStore.getAt(j).set('txPower', sniOptical.txPower);
	    		sniStore.getAt(j).set('rxPower', sniOptical.rxPower);
	    		sniStore.getAt(j).set('modifyTime', sniOptical.modifyTime);
	    		sniStore.getAt(j).set('modifyTimeStr', sniOptical.modifyTimeStr);
	    		sniStore.getAt(j).commit();
	    		//进入此处都认为是成功的
	    		sniGrid.getView().removeRowClass(j, 'yellow-row');
	        	successNum++;
	        	$('#suc-num-dd').text(successNum);
	    	}
	    }
		//刷新完成处理
		refreshedSniCounter ++;
	    if (refreshedSniCounter >= sniStore.getCount()) {
	    	clearTimeout(timeoutTimer);
	    	outputResult();
	    }
	    
	});
	
	//发送查询请求
    $.post('/epon/portinfo/refreshAllSniOptical.tv',{
    	entityId: entityId,
    	jConnectedId: WIN.top.GLOBAL_SOCKET_CONNECT_ID
    });
    
	//发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, REFRESH_TIMEOUT);
	
	//批量初始化
    function initBatch(){
  	  //初始化统计数据
  	  $('#suc-num-dd').text(0);
  	  $('#fail-num-dd').text(0);
  	  //将各行标记为正在刷新
  	  for (i = 0, len=sniStore.getCount(); i < len; i++) {
  		  sniGrid.getView().removeRowClass(i, 'red-row');
  		  sniGrid.getView().addRowClass(i, 'yellow-row');
  	  }
  	  //禁用查询功能、刷新功能、翻页功能
  	  sniGrid.getTopToolbar().setDisabled(true);
  	  //展示等待框
  	  showLoading();
    }
    
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("refreshAllSniOptical");
    	//将正在处理行全部置为失败
    	for (var i = 0, len=sniStore.getCount(); i < len; i++) {
    		if(!contains(finishedIndex, i)){
    			sniGrid.getView().addRowClass(i, 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	sniGrid.getTopToolbar().setDisabled(false);
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            showTime: 2000,
            html: '<b class="orangeTxt">@olt.reOpticalFinish@<br/>@COMMON.sucNum@: ' + successNum + ' @COMMON.failNum@: ' + failedNum + '</b>'
    	});
    }
}


function refreshAllPonOptical() {
	if (ponStore.getCount() == 0) {
	    return;
	}
	//批量初始化
	initBatch();
	var successNum = 0,
	    failedNum = 0,
	    finishedIndex = [],
	    refreshedPonCounter = 0;
	
	//dwr接收处理函数
	window.top.addCallback("refreshAllPonOptical", function(ponOptical) {
		//重置超时限制 
		clearTimeout(timeoutTimer);
		timeoutTimer = setTimeout(function(){
	    	unexpectedEnd();
	    }, 30000);
	    for (var j = 0, len = ponStore.getCount(); j < len; j ++ ) {
	    	//遍历找到对应的行
	    	if (ponStore.getAt(j).get('portId') == ponOptical.portId) {
	    		finishedIndex.push(j);
	    		ponStore.getAt(j).set('adminStatus', ponOptical.adminStatus);
	    		ponStore.getAt(j).set('operationStatus', ponOptical.operationStatus);
	    		ponStore.getAt(j).set('inCurrentRate', ponOptical.inCurrentRate);
	    		ponStore.getAt(j).set('outCurrentRate', ponOptical.outCurrentRate);
	    		ponStore.getAt(j).set('identifier', ponOptical.identifier);
	    		ponStore.getAt(j).set('vendorName', ponOptical.vendorName);
	    		ponStore.getAt(j).set('waveLength', ponOptical.waveLength);
	    		ponStore.getAt(j).set('vendorPN', ponOptical.vendorPN);
	    		ponStore.getAt(j).set('vendorSN', ponOptical.vendorSN);
	    		ponStore.getAt(j).set('workingTemp', ponOptical.workingTemp);
	    		ponStore.getAt(j).set('workTempDisplay', ponOptical.workTempDisplay);
	    		ponStore.getAt(j).set('workingVoltage', ponOptical.workingVoltage);
	    		ponStore.getAt(j).set('biasCurrent', ponOptical.biasCurrent);
	    		ponStore.getAt(j).set('txPower', ponOptical.txPower);
	    		ponStore.getAt(j).set('rxPower', ponOptical.rxPower);
	    		ponStore.getAt(j).set('modifyTime', ponOptical.modifyTime);
	    		ponStore.getAt(j).set('modifyTimeStr', ponOptical.modifyTimeStr);
	    		ponStore.getAt(j).commit();
	    		//进入此处都认为是成功的
	    		ponGrid.getView().removeRowClass(j, 'yellow-row');
	    		successNum++;
	    		$('#suc-num-dd').text(successNum);
	    	}
	    }
		//刷新完成处理
		refreshedPonCounter++;
	    if (refreshedPonCounter >= ponStore.getCount()) {
	    	clearTimeout(timeoutTimer);
	    	outputResult();
	    }
	});
	
	//发送查询请求
    $.post('/epon/portinfo/refreshAllPonOptical.tv',{
    	entityId: entityId,
    	jConnectedId: WIN.top.GLOBAL_SOCKET_CONNECT_ID
    });
    
	//发送完请求后开启计时器，如果超时未返回，则认为本次批量结束，未返回的均统计为失败
    timeoutTimer = setTimeout(function(){
    	unexpectedEnd();
    }, 30000);
	
    //批量初始化
    function initBatch(){
  	  //初始化统计数据
  	  $('#suc-num-dd').text(0);
  	  $('#fail-num-dd').text(0);
  	  //将各行标记为正在刷新
  	  for (i = 0, len=ponStore.getCount(); i < len; i++) {
  		ponGrid.getView().removeRowClass(i, 'red-row');
  		ponGrid.getView().addRowClass(i, 'yellow-row');
  	  }
  	  //禁用查询功能、刷新功能、翻页功能
  	  ponGrid.getTopToolbar().setDisabled(true);
  	  //展示等待框
  	  showLoading();
    }
    
    //批量操作意外中止处理函数
    function unexpectedEnd(){
    	clearTimeout(timeoutTimer);
    	//取消dwr推送接收
    	window.top.removeCallback("refreshAllPonOptical");
    	//将正在处理行全部置为失败
    	for (var i = 0, len=ponStore.getCount(); i < len; i++) {
    		if(!contains(finishedIndex, i)){
    			ponGrid.getView().addRowClass(i, 'red-row');
    		}
    	}
    	//刷新成功数为successNum，failedNum更新为len-failedNum
    	failedNum = len - successNum;
    	//展示结果
    	outputResult();
    }
    
    function outputResult(){
    	//启用查询功能、刷新功能、翻页功能
    	ponGrid.getTopToolbar().setDisabled(false);
    	//隐藏等待框
    	hideLoading();
    	//显示结果
    	$('#suc-num-dd').text(successNum);
    	$('#fail-num-dd').text(failedNum);
    	top.afterSaveOrDelete({
            title: '@COMMON.tip@',
            showTime: 2000,
            html: '<b class="orangeTxt">@olt.reOpticalFinish@<br/>@COMMON.sucNum@: ' + successNum + ' @COMMON.failNum@: ' + failedNum + '</b>'
    	});
    }
}
Ext.onReady(function(){
	//显示右上角help信息;
	top.showHelpIco('oltPortInfo.jsp');
	sniGrid = new Ext.grid.GridPanel({
    	viewConfig : {
    		forcefit : false
    	},
    	//width : '100%',
    	height: 400,
    	autoScroll : true,
    	renderTo : 'sniPort',
    	stripeRows:true,
    	bodyCssClass: 'normalTable',
    	border: false,
   		store: sniStore,
   		cm : sniColumn,
   		sm: new Ext.grid.RowSelectionModel({
   	      singleSelect: true
   	    }),
	   	enableColumnMove : true,
   	    listeners: {
   	    	columnresize: function(){
   	    		CustomColumnModel.saveCustom('oltSniList', sniColumn.columns);
   	    	},
   	    	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo('oltSniList', sortInfo);
			}
   	    },
   		tbar : new Ext.Toolbar({
			items : [{
				xtype: 'tbspacer', 
				width: 5
			},{
				text    : '@olt.refreshOptical@',
				iconCls : 'bmenu_refresh',
				handler : refreshAllSniOptical
			}]
   		})
	});
	sniStore.load();
	sniStore.setDefaultSort(sniSortInfo.field, sniSortInfo.direction);
    
    tab1 = new Nm3kTabBtn({
	    renderTo:"putTab",
	    callBack:"tabFn",
	    tabs:["@olt.sniInfo@","@olt.ponInfo@"]
	});
	tab1.init();
	tab1.ponGrid = false;
	
	$(window).resize(function(){
		autoHeight();
		throttle(autoHeight,window);
	});//end resize;
	autoHeight();
});//end document ready;

function createponGrid(){
	$("#ponPort").empty();
	ponGrid = new Ext.grid.GridPanel({
    	viewConfig : {
    		forcefit : false
    	},
    	width : '100%',
    	height : 400,
    	renderTo : 'ponPort',
    	stripeRows:true,
    	bodyCssClass: 'normalTable',
    	border: false,
   		store: ponStore,
   		cm : ponColumn,
   		sm: new Ext.grid.RowSelectionModel({
   	      singleSelect: true
   	    }),
   	    enableColumnMove : true,
   	    listeners: {
	    	columnresize: function(){
	    		CustomColumnModel.saveCustom('oltPonList', ponColumn.columns);
	    	},
	    	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo('oltPonList', sortInfo);
			}
	    },
   		tbar : new Ext.Toolbar({
			items : [{
				xtype: 'tbspacer', 
				width: 5
			},{
				text    : '@olt.refreshOptical@',
				iconCls : 'bmenu_refresh',
				handler: refreshAllPonOptical
			}]
   		})
	});
	ponStore.load();
	ponStore.setDefaultSort(ponSortInfo.field, ponSortInfo.direction);
}
function tabFn(index){
	$(".jsTabBody").css({display:'none'});
	$(".jsTabBody").eq(index).fadeIn(function(){
		if( index === 1 &&  !tab1.ponGrid){
			tab1.ponGrid = true;
			createponGrid();
		}
		autoHeight();
	});
};
//resize事件增加 函数节流;
function throttle(method, context){
	clearTimeout(method.tId);
	method.tId = setTimeout(function(){
		method.call(context);
	},100);
}
		
function autoHeight(){
	var h = $(window).height() - 80,
	    w = $(window).width();
	if(w<10){w=10;}
	if(h<10){h=10;}
	if(sniGrid != null && !$("#sniPort").is(":hidden")){
		sniGrid.setSize(w, h)
	}
	if(ponGrid != null && !$("#ponPort").is(":hidden")){
		ponGrid.setSize(w, h)
	}
}

function showLoading(message) {
	if(message){
		$("#loading").text(message);
	}
	var lPos = ($(window).width() - $("#loading").outerWidth()) / 2;
	var tPos = 150;
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