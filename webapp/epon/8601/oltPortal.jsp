<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    PLUGIN Portlet
    module epon
    import js/raphael/raphael
    import js/raphael/RaphaelCir
    import js/nm3k/Nm3kClock
    import epon/js/oltPortalTrap
    import js/tools/imgTool
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
#portletTools .x-panel-body, #portletPerformace .x-panel-body{ background: #fff;}
#location .wordBreak span{ display:block; float:left; padding:3px; white-space:nowrap; word-break:keep-all;}
.putCir{ width:150px; height:170px; /*background:#eee;*/}
.cirLengend{width:16px; height:16px; overflow:hidden; display:block; float:left; margin-right:2px;}
.clrLendUl{border:1px solid #ccc; margin:10px 0px; background:#fff;-moz-border-radius: 5px;-khtml-border-radius: 5px;-webkit-border-radius: 5px;border-radius: 5px;}
#entityIcon{ padding-top: 80px;}
</style>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<script>
// for tab changed start
function tabActivate() {
	doRefresh();
	startTimer();
}
function tabDeactivate() {
	stopTimer();
}
function tabRemoved() {
	stopTimer();
}
function tabShown() {}
// for tab changed end
var entityId = '<s:property value="entityId"/>';
var entityName = '<s:property value="entity.name"/>';
var typePath = '<s:property value="entity.typePath"/>';
var modulePath = '<s:property value="entity.modulePath"/>';
var entityIp = '<s:property value="entity.ip"/>';
var agentInstalled = '<s:property value="entity.agentSupport"/>';
var snmpSupport = '<s:property value="entity.snmpSupport"/>';
var entityType = '<s:property value="entity.type"/>';
var entity = '<s:property value="entity"/>'
var slotNumInUse = '<s:property value="slotNumInUse"/>';
var fanNumInUse = '<s:property value="fanNumInUse"/>';
var powerNumInUse = '<s:property value="powerNumInUse"/>';
var typeId = '${entity.typeId}';
var operPower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var cameraSwitch = '<s:property value="cameraSwitch"/>';
var attention = '<s:property value="oltAttribute.attention"/>';
//var oltAttribute = '<s:property value="oltAttribute"/>';
var width = Ext.getBody().getWidth();
var timer = null;
var dispatchTotal = 120000;
var dispatchInterval = 15000;
var syncount = 0;
var synMax = 2;
//内存利用率的值 0至1之间的数字，如果是-1则表示没有获取到数据;
var oUsedRatio = {
	mem : ${memUsedRatio},
	cpu : ${cpuUsedRatio},
	flash : ${flashUsedRatio}
}
/*
 * 记录cpu利用率,内存利用率,Flash利用率三个图表;
 * err表示当获取到数据是-1的时候，则显示错误信息;
 */
var oProgressCir = {
	cpuCir : null, 
	memCir : null,
	flashCir : null,
	notGetData : "@Tip.failedGetData@",
	NaNData : "@ONU.loadDataError@"
}
/*
 * 记录时间控件的对象;
 * type类型：cir,grayCalendar,whiteCalendar;
 */
var oRunTime = {
	totalTime : Number('<s:property value="oltAttribute.oltDeviceUpTime"/>'),
	clock : null,//记录时间控件;
	language : '@COMMON.language@',
 	type : 'grayCalendar'
 }

var isSupportCmc = <%=uc.hasSupportModule("cmc")%>

function startTimer() {
	if (timer == null) {
		timer = setInterval("doRefresh()", dispatchTotal);
	}
}
function stopTimer() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
//添加到百度地图
function addBaiduMap(){
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + entityId + "&typeId=" + typeId + "&entityName=" + entityName, null, true, true);
}
/* function onOnuGlobalConfigClick(){
    window.top.createDialog('onuGlobalConfig', "@resources/COMMON.onuGlobalConfig@", 600, 370, 'epon/showOnuGlobalConfig.tv?entityId=' + entityId + "&typeId=" + typeId + "&entityName=" + entityName, null, true, true);
} */
function doRefresh() {
}
function doOnunload() {
	stopTimer();
}
function getZetaCallback() {
	return window.top.ZetaCallback;
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function closeWaitingDlg(title) {
	window.top.closeWaitingDlg(title);
}
function showErrorDlg() {
	window.top.showErrorDlg();
}
function showMessageDlg(title, msg, type) {
	window.top.showMessageDlg(title, msg, type);
}
function showConfirmDlg(title, msg, callback) {
	window.top.showConfirmDlg(title, msg, callback);
}
function showInputDlg(title, msg, callback, text, scope, multiline) {
	window.top.showInputDlg(title, msg, callback, text, scope, multiline);
}
function showTextAreaDlg(title, msg, callback) {
	window.top.showTextAreaDlg(title, msg, callback);
}
function showProgressDlg(title, msg, action) {
}
function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
	window.top.createDialog(id, title, width, height, url, icon, modal, closable, closeHandler);
}
function closeWindow(id) {
	window.top.closeWindow(id);
}
function setWindowVisible(id, visible) {
	window.top.setWindowVisible(id, visible);
}
function setWindowTitle(id, title) {
	window.top.setWindowTitle(id, title);
}
function getWindow(id) {
	window.top.getWindow(id);
}
function addView(id, title, icon, url, history) {
	window.top.addView(id, title, icon, url, history);
}
function setActiveTab(id) {
	window.top.setActiveTab(id);
}
function setActiveTitle(id, title) {
	window.top.setActiveTitle(id, title);
}
function getActiveFrameById() {
	return window.top.getActiveFrameById();
}
function getActiveFrame() {
	return window.top.getActiveFrame();
}
function getFrameById(frameId) {
	return window.top.getFrameById(frameId);
}
function getFrame(frameId) {
	return window.top.getFrame(frameId);
}
function getPropertyFrame() {
	return window.top.getPropertyFrame();
}
function getNavigationFrame() {
	return window.top.getNavigationFrame();
}
function getMenuFrame() {
	return window.top.getNavigationFrame();
}
function showFileManage(){
	window.parent.createDialog("fileManage", I18N.ENTITYSNAP.tools.fileManagement, 703, 450, "epon/showFileManage.tv?entityId=" + entityId, null, true, true);			  
}
function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
}
function locateEntity(entityIp) {
	//window.top.locateEntityByIp(entityIp);
	window.top.locateEntityByIp_Id(entityIp, entityId);
}
function showGponAutoAuth(){
	window.top.createDialog('gponAutoAuth',"@GPON/onuauth.autoAuthConfig@", 700, 500, '/gpon/onuauth/showGponAutoAuthConfig.tv?entityId=' + entityId, null, true, true);
}
function showPing() {
	createDialog("modalDlg", 'Ping ' + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
}
function onDiscoveryAgainClick(entityId, entityIp) {
	window.top.discoveryEntityAgain(entityId, entityIp, function() {
		window.top.onRefreshClick();
	});
}
function cancelManagement() {
	showConfirmDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.cancelManagement, function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = entityId;
		Ext.Ajax.request({url: '/entity/cancelManagement.tv',
		   success: function() {
		      location.href = 'entityPortalCancel.tv?entityId=' + entityId;
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});			
	});
}
function onTracertClick() {
	if (entity) {
		window.parent.createDialog("modalDlg", 'Tracert' + " - " + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + entityIp, null, true, true);
	}
}
function showTelnet() {
	window.open('telnet://' + entityIp, 'ntelnet' + entityId);
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function checkSysTiming() {
	window.parent.showConfirmDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.sysTimeCheck, function(type) {
		if (type == 'no') {return;}
		showWaitingDlg(I18N.entitySnapPage.wait, I18N.entitySnapPage.sysTimeChecking, 'ext-mb-waiting');
		Ext.Ajax.request({url:"/epon/checkSysTiming.tv", success: function (text) {
			if(text!=null&&text.responseText=='success'){
	             window.parent.closeWaitingDlg();
	             top.afterSaveOrDelete({
	    				title: '@COMMON.tip@',
	    				html: '<b class="orangeTxt">'+I18N.entitySnapPage.checkTimeSuccess+'</b>'
    			});
	             //window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.checkTimeSuccess);
				}else{ 
				 window.parent.closeWaitingDlg();
			     window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.checkTimeFail);
				}
	    }, failure: function () {
	        window.parent.closeWaitingDlg();
	        window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.checkTimeFail);
	    }, params: {entityId : entityId}});
	});
}
function resetOlt() {
	window.parent.showConfirmDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.deviceRestart, function(type) {
		if (type == 'no') {return;}
		showWaitingDlg(I18N.entitySnapPage.wait, I18N.entitySnapPage.restarting, 'ext-mb-waiting');
		Ext.Ajax.request({url:"/epon/resetOlt.tv", success: function (response) {
	        window.parent.closeWaitingDlg();
	        if(response.responseText == "success"){
	        	top.afterSaveOrDelete({
    				title: '@COMMON.tip@',
    				html: '<b class="orangeTxt">'+I18N.entitySnapPage.restartSuccess+'</b>'
				});
		        //window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartSuccess);
	        }else{
	        	window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartFail);
	        }
	    }, failure: function () {
	        window.parent.closeWaitingDlg(response);
	        window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartFail);
	    }, params: {entityId : entityId}});
	});
}
function restoreOlt() {
	window.parent.showConfirmDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.reset, function(type) {
		if (type == 'no') {return;}
		showWaitingDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.resetting, 'ext-mb-waiting');
		Ext.Ajax.request({url:"/epon/restoreOlt.tv", success: function (response) {
	        window.parent.closeWaitingDlg();
	        if(response.responseText == "success"){
	        	window.parent.showConfirmDlg(I18N.entitySnapPage.message,I18N.entitySnapPage.resetAndRestart, function(type) {
	        		if (type == 'no') {return;}
	        		showWaitingDlg(I18N.entitySnapPage.wait, I18N.entitySnapPage.restarting, 'ext-mb-waiting');
	        		Ext.Ajax.request({url:"/epon/resetOlt.tv", success: function (response) {
	        	        window.parent.closeWaitingDlg();
	        	        if(response.responseText == "success"){
	        	        	top.afterSaveOrDelete({
	            				title: '@COMMON.tip@',
	            				html: '<b class="orangeTxt">'+I18N.entitySnapPage.restartSuccess+'</b>'
	        				});
	        		        //window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartSuccess);
	        	        }else{
	        	        	window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartFail);
	        	        }
	        	    }, failure: function () {
	        	        window.parent.closeWaitingDlg(response);
	        	        window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.restartFail);
	        	    }, params: {entityId : entityId}});
	        	})
	        }else{
	        	window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.resetFail);
	        }
	    }, failure: function (response) {
	        window.parent.closeWaitingDlg();
	        window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.resetFail);
	    }, params: {entityId : entityId}});
	});
}
function saveConfig() {
	window.parent.showConfirmDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.save, function(type) {
		if (type == 'no') {return;}
		showWaitingDlg(I18N.entitySnapPage.wait, I18N.entitySnapPage.saving, 'ext-mb-waiting');
        $.ajax({
            url: '/configBackup/saveConfig.tv',
            type: 'POST',
            data: "entityId=" + entityId,
            success: function(json) {
                if (!json.success) {
                	window.parent.closeWaitingDlg();
            		window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.saveFail);
            	} else {
            		window.parent.closeWaitingDlg();
            		top.afterSaveOrDelete({
        				title: '@COMMON.tip@',
        				html: '<b class="orangeTxt">'+I18N.entitySnapPage.saveSuccess+'</b>'
    				});
            		//window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.saveSuccess);
            	}
            }, error: function(json) {
            	window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.saveFail);
        	}, timeout:180000,dataType: 'json', cache: false
        });
	});
}
function setDolPolling(){
	window.top.createDialog('perfConfig', I18N.ENTITYSNAP.tools.dol, 600, 370, 
			'/cmcperf/showDolPerfConf.tv?entityId=' + entityId, null, true, true);
}

//刷新主控板性能数据。  @add by flack 2013-09-11
function refreshPerformance(){
	$.ajax({
        url: '/epon/getUsedRatio.tv?entityId=' + entityId,
        type: 'POST',
        dataType:'json',
        success: function(json) {
        	oUsedRatio.cpu = json.cpuUsedRatio;
        	oUsedRatio.mem = json.memUsedRatio;
        	oUsedRatio.flash = json.flashUsedRatio;
        	//如果这个板块不可见（用户可能收起了这个板块或者正在拖拽这个板块），那就不更新它(因为更新的时候raphael中attr函数的y轴计算不正确)；
        	if($("#cpuCir").is(":hidden")){
				return;
			 }
       		oProgressCir.cpuCir.update(oUsedRatio.cpu);
       		oProgressCir.memCir.update(oUsedRatio.mem);
       		oProgressCir.flashCir.update(oUsedRatio.flash);
        	
        }
    });
}

function getDeviceSniLoading() {
    $.ajax({
        url: '/epon/perf/getDeviceSniLoading.tv?entityId='+ entityId,
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getDeviceSniLoading").replaceWith(text);
        }
    });
}

function getDevicePonLoading() {
    $.ajax({
        url: '/epon/perf/getDevicePonLoading.tv?entityId=' + entityId,
        type: 'POST',
        dataType:'text',
        success: function(text) {
            $("#getDevicePonLoading").replaceWith(text);
        }
    });
}

function showOltPerfView(entityId,portIndex,portType){
	location.href = "/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId="+entityId
	+"&portIndex="+portIndex+"&portType="+portType;
}

function refreshCmInfo() {
    $.ajax({
        url: '/epon/refreshCmInfo.tv?entityId=' + entityId + '&c=' + Math.random(),
        type: 'POST',
        dataType:'json',
        success: function(eponCmNumStatic) {
            $("#cmNumTotal").text(eponCmNumStatic.cmNumTotal);
            $("#cmNumOnline").text(eponCmNumStatic.cmNumOnline);
            $("#cmNumActive").text(eponCmNumStatic.cmNumActive);
            $("#cmNumOffline").text(eponCmNumStatic.cmNumOffline);
            $("#cmNumRregistered").text(eponCmNumStatic.cmNumRregistered);
            $("#cmNumUnregistered").text(eponCmNumStatic.cmNumUnregistered);
        }
    });
}
function switchHandler(){
    window.parent.showConfirmDlg("@EPON.tip@", I18N.EPON.switchMaster, function(type) {
        if (type == 'no') {return;}
        window.top.showWaitingDlg(I18N.EPON.wait, I18N.EPON.switching, 'ext-mb-waiting');
        $.ajax({
            url: '/epon/switchoverOlt.tv',
            data: "entityId=" + entityId,
            success: function(json) {
                if (json.message) {
                    window.parent.showMessageDlg("@EPON.tip@", json.message);
                } else {
                    window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.switchOk);
                }
            }, error: function() {
                window.parent.showMessageDlg("@EPON.tip@", I18N.EPON.switchError);
            }, dataType: 'json', cache: false
        });
    });
}
function loadOltSubTotal(){
	 $.ajax({
       url: '/epon/loadOltSubInfo.tv?entityId=' + entityId + '&c=' + Math.random(),
       type: 'POST',
       dataType:'json',
       success: function(json) {
           $("#onuTotal").text(json.onuTotal);
           $("#onuOnline").text(json.onuOnline);
       }
   });
}

function loadSubCmcTotal(){
	 $.ajax({
      url: '/cmc/loadCmcSubInfo.tv?cmcId=' + entityId + '&c=' + Math.random(),
      type: 'POST',
      dataType:'json',
      success: function(json) {
   	   $("#cmcTotal").text(json.cmcTotal);
          $("#cmcOnline").text(json.cmcOnline);
      }
  });
}

function loadPonUsedInfo(){
	 $.ajax({
	        url: '/epon/loadPonUsedInfo.tv?entityId=' + entityId + '&c=' + Math.random(),
	        type: 'POST',
	        dataType:'json',
	        success: function(json) {
	            $("#ponUpNum").text(json.ponUpNum);
	            $("#slotTotalNum").text(json.slotTotalNum);
	            $("#unusedPon").text(json.unusedPonNum);
				$("#unusedSlot").text(json.unusedSlotNum);
	        }
	    });
}
function onAttentionClick(){
	if(attention){// 如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : entityId},
			success : function(){
				top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: I18N.OLT.cancelFocusSuccess
    			});
				attention = false;
				//window.parent.showMessageDlg("@COMMON.tip@", I18N.OLT.cancelFocusSuccess)
			    $("#userAttention").show();
			    $("#userAttention").parent().show();
			    $("#userAttentionCancel").hide();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", I18N.OLT.cancelFocusFail)
			}
		})
	}else{// 如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : entityId},
			success : function(){
				top.nm3kRightClickTips({
    				title: "@COMMON.tip@",
    				html: I18N.OLT.focusSuccess
    			});
				attention = true;
				$("#userAttention").hide();
				$("#userAttention").parent().hide();
			    $("#userAttentionCancel").show();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", I18N.OLT.focusFail)
			}
		})
	}
}
//上行端口用户数统计
var ponCmNumColumns = [{header: I18N.EPON.PortName, width: 100, sortable: false, align: 'center', dataIndex: 'ponPortName', renderer: renderPonPortName},
    {header: I18N.CM.total, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumTotal', renderer: renderPonCmTotalNum},
    {header: I18N.CM.online, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOnline', renderer: renderPonCmOnlineNum},
    {header: I18N.CM.active, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumActive', renderer: renderPonCmActiveNum},
    {header: I18N.CM.offline, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOffline', renderer: renderPonCmOfflineNum},
    {header: I18N.CM.cmList, width: 50, sortable: false, align: 'center', dataIndex: 'op', renderer: renderPonToCmList}
];
var ponCmNumStore = new Ext.data.JsonStore({
    url: '/epon/getPonCmNum.tv?entityId=' + entityId,
    root: 'data',
    fields: ['cmNumTotal', 'cmNumOnline','cmNumActive','cmNumOffline', 'ponPortName','ponId', 'ifIndex', 'entityId']
});

//pon端口用户数统计
function renderPonPortName(value,p,record){
    return value;
}
function renderPonCmTotalNum(value, p, record) {
    return String.format("<a href='#' class=my-link onclick='showPonAllCmNumInfo(\"{0}\")'>" + value +
            "</a>", record.data.ifIndex);
}
function renderPonCmOnlineNum(value, p, record) {
    return String.format("<a href='#' class=my-link onclick='showPonOnlineCmNumInfo(\"{0}\")'>" + value +
            "</a>", record.data.ifIndex);
}
function renderPonCmActiveNum(value, p, record) {
    return String.format("<a href='#' class=my-link onclick='showPonActiveCmNumInfo(\"{0}\")'>" + value +
            "</a>", record.data.ifIndex);
}
function renderPonCmOfflineNum(value, p, record) {
    return String.format("<a href='#' class=my-link onclick='showPonOfflineCmNumInfo(\"{0}\")'>" + value +
            "</a>", record.data.ifIndex);
}
function renderPonToCmList(value, p, record) {
    return String.format("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(\"{0}\",\"{1}\")'>@COMMON.view@</a>",
            record.data.entityId, record.data.ponId);
}

//cmNumActive
function showPonActiveCmNumInfo(ifIndex){
    addView("historyShow-"+entityId + "-"+ifIndex + "showPonActiveCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=ponCmNumActive&index="+ifIndex);
}
//cmNumActive
function showPonAllCmNumInfo(ifIndex){
    addView("historyShow-"+entityId + "-"+ifIndex + "showPonTotalCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=ponCmNumTotal&index="+ifIndex);
}
//cmNumActive
function showPonOnlineCmNumInfo(ifIndex){
    addView("historyShow-"+entityId + "-"+ifIndex + "showPonOnlineCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=ponCmNumOnline&index="+ifIndex);
}
//cmNumOffline
function showPonOfflineCmNumInfo(ifIndex){
    addView("historyShow-"+entityId + "-"+ifIndex + "-cmNumOffline", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=ponCmNumOffline&index="+ifIndex);
}

function showCmDetail(entityId, ponId) {
	var queryInitData = {
		portal :'olt', 
   		oltId : entityId,
   		ponId : ponId
    };
	window.top.addView("cmListNew", I18N.CM.cmList, "apListIcon", encodeURI("/cmlist/showCmListPage.tv?queryInitDataStr="+JSON.stringify(queryInitData)), null, true);
}

//上行端口用户数统计
var cmtsCmNumColumns = [{header: I18N.EPON.cmtsName, width: 100, sortable: false, align: 'center', dataIndex: 'cmtsName'/* , renderer: renderCmtsName */},
  {header: I18N.CM.total, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumTotal'/* , renderer: renderCmtsCmTotalNum */},
  {header: I18N.CM.online, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOnline'/* , renderer: renderCmtsCmOnlineNum */},
  {header: I18N.CM.active, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumActive'/* , renderer: renderCmtsCmActiveNum */},
  {header: I18N.CM.offline, width: 50, sortable: false, align: 'center', dataIndex: 'cmNumOffline'/* , renderer: renderCmtsCmOfflineNum */}
  /* ,
  {header: I18N.CM.cmList, width: 50, sortable: false, align: 'center', dataIndex: 'op', renderer: renderCmtsToCmList} */
];
var cmtsCmNumStore = new Ext.data.JsonStore({
  url: '/epon/getCmtsCmNum.tv?entityId=' + entityId,
  root: 'data',
  fields: ['cmNumTotal', 'cmNumOnline','cmNumActive','cmNumOffline', 'cmtsName','cmcId', 'ifIndex', 'entityId']
});

//Cmts端口用户数统计
function renderCmtsPortName(value,p,record){
  return value;
}
function renderCmtsCmTotalNum(value, p, record) {
  return String.format("<a href='#' class=my-link onclick='showCmtsAllCmNumInfo(\"{0}\")'>" + value +
          "</a>", record.data.ifIndex);
}
function renderCmtsCmOnlineNum(value, p, record) {
  return String.format("<a href='#' class=my-link onclick='showCmtsOnlineCmNumInfo(\"{0}\")'>" + value +
          "</a>", record.data.ifIndex);
}
function renderCmtsCmActiveNum(value, p, record) {
  return String.format("<a href='#' class=my-link onclick='showCmtsActiveCmNumInfo(\"{0}\")'>" + value +
          "</a>", record.data.ifIndex);
}
function renderCmtsCmOfflineNum(value, p, record) {
  return String.format("<a href='#' class=my-link onclick='showCmtsOfflineCmNumInfo(\"{0}\")'>" + value +
          "</a>", record.data.ifIndex);
}
function renderCmtsToCmList(value, p, record) {
  return String.format("<a  href='javascript:;' border=0 align=absmiddle onclick = 'showCmDetail(\"{0}\",\"{1}\")'>@COMMON.view@</a>",
          record.data.entityId, record.data.cmcId);
}

//cmNumActive
function showCmtsActiveCmNumInfo(ifIndex){
  addView("historyShow-"+entityId + "-"+ifIndex + "showCmtsActiveCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=cmtsCmNumActive&index="+ifIndex);
}
//cmNumActive
function showCmtsAllCmNumInfo(ifIndex){
  addView("historyShow-"+entityId + "-"+ifIndex + "showCmtsTotalCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=cmtsCmNumTotal&index="+ifIndex);
}
//cmNumActive
function showCmtsOnlineCmNumInfo(ifIndex){
  addView("historyShow-"+entityId + "-"+ifIndex + "showCmtsOnlineCmNumInfo", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=cmtsCmNumOnline&index="+ifIndex);
}
//cmNumOffline
function showCmtsOfflineCmNumInfo(ifIndex){
  addView("historyShow-"+entityId + "-"+ifIndex + "-cmNumOffline", "@network/NETWORK.hisPerf@", "historyIcon", "/cmcperf/showCmcHisPerf.tv?entityId=" + entityId + "&timeType=Today&perfType=cmtsCmNumOffline&index="+ifIndex);
}

//自动清除离线cm配置
function onclearTimeConfigClick(){
    window.top.createDialog('modalDlg', "@resources/COMMON.clearOfflineCm@", 600, 370, '/cmc/showAutoClearOfflineCm.tv?cmcId=' + entityId, null, true, true);
}

function showCmDetailByCmc(entityId, cmcId) {
	var queryInitData = {
		portal :'olt', 
 		oltId : entityId,
 		cmcId : cmcId
  };
	window.top.addView("cmListNew", I18N.CM.cmList, "cmListIcon", encodeURI("/cmlist/showCmListPage.tv?queryInitDataStr="+JSON.stringify(queryInitData)), null, true);
}

function showRealtimeInfo(){
	window.parent.addView('oltRealTimeInfo' + entityId, '@COMMON.realtimeInfo@['+entityName+']' , 'entityTabIcon', '/epon/oltRealtime/showOltRealTime.tv?entityId='+ entityId + "&entityName=" + entityName + "&entityIp=" + entityIp);
}

var portlets = [];
Ext.BLANK_IMAGE_URL = '/images/s.gif';
Ext.onReady(function(){
	oProgressCir.cpuCir = new RaphaelCir({
		renderTo : "cpuCir",
		title : "@PerformanceAlert.cpuUsed@",
		notGetData : oProgressCir.notGetData,
		NaNData : oProgressCir.NaNData,
		percent : oUsedRatio.cpu
	}); 
	oProgressCir.memCir = new RaphaelCir({
		renderTo : "memCir",
		title : "@PerformanceAlert.memUsed@",
		notGetData : oProgressCir.notGetData,
		NaNData : oProgressCir.NaNData,
		percent : oUsedRatio.mem
	});
	oProgressCir.flashCir = new RaphaelCir({
		renderTo : "flashCir",
		title : "@PerformanceAlert.flashUsed@",
		notGetData : oProgressCir.notGetData,
		NaNData : oProgressCir.NaNData,
		percent : oUsedRatio.flash
	});
	oRunTime.clock = new Nm3kClock({
		renderTo: 'runTime',//要渲染到哪个div的id;
		startTime: oRunTime.totalTime,
		type: oRunTime.type,
		language: oRunTime.language
	});
	oRunTime.clock.init();
	//修改为从数据库读取布局;
	//基本信息;
	var portletDetail = {id:'portletDetail', title: I18N.ENTITYSNAP.basicInfo.basicInfo, bodyStyle:'padding:10px', autoScroll:true, contentEl:'detail'}
	//OLT位置信息;
	var oltLocation = {id:'oltLocation', title: I18N.ENTITYSNAP.basicInfo.deviceIpInfo, bodyStyle: 'padding:10px', autoScroll: true, contentEl:'location'}
	//关联下级设备信息;
    var subDeviceInfo = {id:'subDeviceInfo', title: "@EPON.relaSubInfo@", bodyStyle: 'padding:8px', autoScroll: true, contentEl:'subInfo'};
    
    var ponUsedInfo = {id:'ponUsedInfo', title: "@EPON.ponPortInfo@", bodyStyle: 'padding:8px', autoScroll: true, contentEl:'putPonUsedInfo'};
    //关联cm信息;
    var portletCmInfo = {id:'portletCmInfo', title: I18N.EPON.relaCMInfo, bodyStyle: 'padding:8px', autoScroll: true, contentEl:'cmInfo'};
    //上行端口用户数统计
    var ponCmNum = new Ext.ux.Portlet({
        id:'ponCmNum',
        title: I18N.EPON.ponCmCount,
        tools: [{id: 'refresh',
            handler: function(event, toolEl, panel) {
                ponCmNumStore.reload();
            }
        }],
        items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'ponCmNumGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse,
                trackMouseOver: trackMouseOver,
                border: false,
                cm: new Ext.grid.ColumnModel(ponCmNumColumns),
                store: ponCmNumStore,
                viewConfig: {
                    forceFit: true
                }
            })
        }]
    });
    ponCmNumStore.setDefaultSort('ifIndex', 'ASC');
    ponCmNumStore.load();
  //上行端口用户数统计
    var cmtsCmNum = new Ext.ux.Portlet({
        id:'cmtsCmNum',
        title: I18N.EPON.cmtsCmNum,
        tools: [{id: 'refresh',
            handler: function(event, toolEl, panel) {
            	cmtsCmNumStore.reload();
            }
        }],
        items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'cmtsCmNumGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse,
                trackMouseOver: trackMouseOver,
                border: false,
                cm: new Ext.grid.ColumnModel(cmtsCmNumColumns),
                store: cmtsCmNumStore,
                viewConfig: {
                    forceFit: true
                }
            })
        }]
    });
    cmtsCmNumStore.setDefaultSort('ifIndex', 'ASC');
    cmtsCmNumStore.load();
    //管理工具;
	var portletTools = {id:'portletTools', title: I18N.ENTITYSNAP.tools.tools, bodyStyle:'padding:10px', autoScroll:true, contentEl:'tools'}
	//连续运行时间;
	var portletSysUpTime= {id:'portletSysUpTime', title: I18N.ENTITYSNAP.deviceUpTime.deviceUpTime, bodyStyle: 'padding:10px', autoScroll: true, contentEl:'runTime' 
			//autoLoad:{text : I18N.entitySnapPage.loading, url : ('/epon/showOltUptimeByEntity.tv?entityId='+entityId+'&num='+ Math.random())}
	}
	
	//主控板最近性能;
	var portletPerformace = {id:'portletPerformace', title: I18N.ENTITYSNAP.performance.mpu, bodyStyle:'padding:0px',autoScroll:true, contentEl:'cpuandmemperformance'}
	//sni端口速率;
	var sniSpeed = new Ext.ux.Portlet({
		id:'sniSpeed',
	    title: '@ENTITYSNAP.speedPortal.sniSpeed@',
		bodyStyle: 'padding:10px',
	    autoScroll:true,
	    autoLoad: {url: '/epon/perf/getDeviceSniLoading.tv?entityId='+entityId, text: '@COMMON.loading@', disableCaching: true}
	});
	//pon口速率;
	var ponSpeed = new Ext.ux.Portlet({
		id:'ponSpeed',
        title: '@ENTITYSNAP.speedPortal.ponSpeed@',
		bodyStyle: 'padding:10px',
        autoScroll:true,
        autoLoad: {url: '/epon/perf/getDevicePonLoading.tv?entityId='+entityId, text: '@COMMON.loading@', disableCaching: true}
	});
	
 	var columns = 2;
    var portletItems = [];
    
    //左侧板块，从数据库读取;
	 var leftPartStr = '${leftPartItems}';
	 //右侧板块，从数据库读取;
	 var rightPartStr = '${rightPartItems}';
	 //如果是第一次加载,就进行默认初始化
	 if(leftPartStr == '' && rightPartStr == ''){
         if (isSupportCmc) {
             leftPartStr = "portletDetail,oltLocation,subDeviceInfo,ponUsedInfo,portletCmInfo,ponCmNum,cmtsCmNum";
             rightPartStr = "portletTools,portletSysUpTime,portletPerformace,sniSpeed,ponSpeed";
         } else {
             leftPartStr = "portletDetail,oltLocation,subDeviceInfo,ponUsedInfo,ponSpeed";
             rightPartStr = "portletTools,portletSysUpTime,portletPerformace,sniSpeed";
         }
     }
    
    var leftPart = {};
    leftPart.columnWidth = .5;
    leftPart.style = "padding:10px 5px 10px 10px";
    leftPart.items = new Array();
    //开始添加板块;
    if(leftPartStr != ''){
		 var tempArr = leftPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 leftPart.items.push(eval(tempArr[i]));
		 }
	}
    portletItems[0] = leftPart;
    
    /* portletItems[0] =  {
        columnWidth: .5,
        style:'padding:10px 5px 10px 10px',
        items:[basicInfo,oltLocation]
    }; */

	var leftItems = portletItems[0].items;
    
	var rightPart = {};
	rightPart.columnWidth = 0.5;
	rightPart.style = "padding:10px 10px 10px 5px";
	rightPart.items = new Array();
	//开始添加板块;
  	 if(rightPartStr != ''){
	     var tempArr = rightPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 rightPart.items.push(eval(tempArr[i]));
		 }
  	 }
	
    portletItems[1] =  rightPart;
 	//通过判断左右两侧id的顺序，判断布局是否改变，如果改变，那么保存布局(要求id和变量名一致);
	$(".x-panel-tl").live("mouseup",function(){
		 setTimeout(saveLayout,200);
		});//end live; 
		function saveLayout(){
			var leftArr = [];
			var rightArr = [];
			$(".x-portal-column").eq(0).find(".x-portlet").each(function(){
				leftArr.push($(this).attr("id"));
			})
			$(".x-portal-column").eq(1).find(".x-portlet").each(function(){
				rightArr.push($(this).attr("id"));
			})
			
			if(leftPartStr == leftArr.toString() && rightPartStr == rightArr.toString()){//没有变化;

			}else{//有变化;
				leftPartStr = leftArr.toString();
				rightPartStr = rightArr.toString();
				$.ajax({
					url: '/epon/savePortalView.tv',
					cache:false, 
					method:'post',
					data: {
						typeId : typeId,
						leftPartItems : leftPartStr, 
						rightPartItems : rightPartStr
					},
					success: function() {
					},
					error: function(){
					}
				});
			};//end if else;
		}
	var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 37, maxSize: 37});
    var viewport = new Ext.Viewport({layout: 'border',
        items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    });

    startTimer();
    
  	//每隔三分钟刷新一次主控板性能数据。  @add by flack 2013-09-11
    setInterval(refreshPerformance,5000);
  	
  	//操作权限控制，主要是控制管理工具里的按钮
  	if(!operPower){
  	    $("#sysTime").attr("disabled",true);
  	  	$("#restart").attr("disabled",true);
  	  	$("#reset").attr("disabled",true);
  	  	$("#saveConfig").attr("disabled",true);
  	 	$("#topoGraph").attr("disabled",true);
  	 	$("#showRealtimeInfo").attr("disabled",true);
  	 	$("#setDolPolling").attr("disabled",true);
  	}
  	
  	if(!refreshDevicePower){
  	 	$("#onDiscoveryAgain").attr("disabled",true);
  	}

  	if(!topoGraphPower){
	 	$("#topoGraph").attr("disabled",true);
  	}
    refreshCmInfo();
    //加载下级设备信息
    loadOltSubTotal();
    if(isSupportCmc){
    	loadSubCmcTotal();
    }
    loadPonUsedInfo();
    //对ip分组;
    var str = $("#location .wordBreak").text();
    if(str.length > 0){
    	var arr = [];
    	arr = str.split(",");
    	var newStr = "";
    	for(var i=0,len=arr.length; i<len; i++){
    		newStr += '<span style="margin-right:10px;">' + arr[i] + '</span>';
    	}
    	$("#location .wordBreak").html(newStr);
    }
    if(attention){
    	$("#userAttention").hide();
    	$("#userAttention").parent().hide();
    }else{
    	$("#userAttentionCancel").hide();
    }
    
    var imgSrc = "/images/${entity.icon64}";
	var newImgSrc = relaceImgSrc(imgSrc);
	loadImage(newImgSrc, function(){
		$('.rightTopPutPic').html(this);
	});
});
</script>
</head>
<body onload="doOnunload()" class="newBody clear-x-panel-body">
	<div id="header">
		<%@ include file="/epon/inc/navigator.inc"%>
	</div>

	<div style="dislpay: none">
		<div id=detail>
			<div style="position:relative">
				<div class="rightTopPutPic">
					<img id="entityIcon" src="/images/<s:property value='entity.icon64'/>" border=0 />
				</div>
				<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
					<tr>
						<td width=100 class="rightBlueTxt">@ENTITYSNAP.basicInfo.name@:</td>					
						<td class="txtLeftEdge wordBreak"><div class="pR230"><s:property value="oltAttribute.oltName"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.alias@:</td>					
						<td class="txtLeftEdge wordBreak"><div class="pR230"><s:property value="entity.name"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.deviceStyle@:</td>
						<s:if test="oltAttribute.oltDeviceStyle == 1">
						<td class="txtLeftEdge wordBreak"><div class="pR230">@ENTITYSNAP.basicInfo.fixed@</div></td>
						</s:if>
						<s:if test="oltAttribute.oltDeviceStyle == 2">
						<td class="txtLeftEdge wordBreak"><div class="pR230">@ENTITYSNAP.basicInfo.chassisBased@</div></td>
						</s:if>
						<s:else>
							<td class="txtLeftEdge wordBreak"><div class="pR230">@ENTITYSNAP.basicInfo.unknownType@</div></td>
						</s:else>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.type@:</td>
						<td class="txtLeftEdge wordBreak"><div class="pR230"><s:property value="oltAttribute.oltType"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.vender@:</td>
						<td class="txtLeftEdge wordBreak"><div class="pR230"><s:property value="oltAttribute.vendorName"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.boards@:</td>
						<td class="txtLeftEdge"><div class="pR230"><s:property value="slotNumInUse"/> @ENTITYSNAP.basicInfo.zaiwei@，
						@ENTITYSNAP.basicInfo.gong@ <s:property value="oltAttribute.oltDeviceNumOfTotalServiceSlot"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.powers@:</td>
						
						<td class="txtLeftEdge"><div class="pR230"><s:property value="powerNumInUse"/> @ENTITYSNAP.basicInfo.zaiwei@，
						@ENTITYSNAP.basicInfo.gong@ <s:property value="oltAttribute.oltDeviceNumOfTotalPowerSlot"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.fans@:</td>
						
						<td class="txtLeftEdge"><div class="pR230"><s:property value="fanNumInUse"/> @ENTITYSNAP.basicInfo.zaiwei@，
						@ENTITYSNAP.basicInfo.gong@ <s:property value="oltAttribute.oltDeviceNumOfTotalFanSlot"/></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.controlIp@:</td>
						
						<td class="txtLeftEdge" ><s:property value="entity.ip"/></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ENTITYSNAP.basicInfo.desc@:</td>
						
						<td class="txtLeftEdge wordBreak" >
							<s:property value="entity.sysDescr" />
						</td>
					</tr>
					<tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.contact@:</td>                   
                        <td class="txtLeftEdge wordBreak"><div class="pR120"><s:property value="entity.contact"/></div></td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.location@:</td>                  
                        <td class="txtLeftEdge wordBreak"><div class="pR120"><s:property value="entity.location"/></div></td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.note@:</td>                  
                        <td class="txtLeftEdge wordBreak"><div class="pR120"><s:property value="entity.note"/></div></td>
                    </tr>
					<tr>
						<td colspan="2" class="txtRight">
							<a class="mR10" href="/config/showOltConfigJsp.tv?module=2&entityId=<s:property value="entity.entityId"/>">@ENTITYSNAP.basicInfo.more@</a>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	<div id=tools>
		<dl class="btnGroupDl pB10">
			<dd>
				<a id ="onDiscoveryAgain" href="javascript:;" class="normalBtnBig" onclick="onDiscoveryAgainClick(entityId, entityIp);"><span><i class="miniIcoRefresh"></i>@network/NETWORK.reTopo@</span></a>
			</dd>
			<dd>
				<a id="sysTime" href="javascript:;" class="normalBtnBig" onclick="checkSysTiming()"><span><i class="miniIcoTime"></i>@ENTITYSNAP.tools.sysTime@</span></a>
			</dd>
			<%-- <dd>
				<a id="googleMap" href="javascript:;" class="normalBtnBig" onclick="onAddToGoogleMaps()"><span><i class="miniIcoGoogle"></i>@ENTITYSNAP.tools.googleMap@</span></a>
			</dd> --%>
			<dd>
				<a id="topoGraph" href="javascript:;" class="normalBtnBig" onclick="locateEntity(entityIp);"><span><i class="miniIcoPosition"></i>@ENTITYSNAP.tools.topo@</span></a>
			</dd>
			<dd>
				<a href="javascript:;" class="normalBtnBig" onclick="showFileManage()"><span><i class="miniIcoInfo"></i>@ENTITYSNAP.tools.fileManagement@</span></a>
			</dd>
			<dd>
				<a href="javascript:;" class="normalBtnBig" onclick="showPing()"><span><i class="miniIcoCmd"></i>@ENTITYSNAP.tools.ping@</span></a>
			</dd>
			<dd>
				<a href="javascript:;" class="normalBtnBig" onclick="onTracertClick(entityIp);"><span><i class="miniIcoTwoArr"></i>TraceRoute</span></a>
			</dd>
			<dd>
				<a id="restart" href="javascript:;" class="normalBtnBig" onclick="resetOlt()"><span><i class="miniIcoEquipment"></i>@ENTITYSNAP.tools.restart@</span></a>
			</dd>
			<dd>
				<a id="reset" href="javascript:;" class="normalBtnBig" onclick="restoreOlt()"><span><i class="miniIcoBack"></i>@ENTITYSNAP.tools.reset@</span></a>
			</dd>
			<%-- <dd>
				<a href="javascript:;" class="normalBtnBig" onclick="cancelManagement()"><span><i class="miniIcoWrong"></i>@ENTITYSNAP.tools.cancel@</span></a>
			</dd> --%>
			<dd id="masterSwitch">
				<a id="switchHandler"  href="javascript:;" class="normalBtnBig" onclick="switchHandler()"><span><i class="miniIcoBack"></i>@EPON.Switch@</span></a>
			</dd>
			<dd>
				<a id="saveConfig" href="javascript:;" class="normalBtnBig" onclick="saveConfig()"><span><i class="miniIcoSaveOK"></i>@ENTITYSNAP.tools.save@</span></a>
			</dd>
			<dd>
				<a id="showRealtimeInfo" href="javascript:;" class="normalBtnBig" onclick="showRealtimeInfo()"><span><i class="miniIcoView"></i>@COMMON.realtimeInfo@</span></a>
			</dd>
			<% if(uc.hasSupportModule("cmc")){%>
			<dd>
				<a id="setDolPolling" href="javascript:;" class="normalBtnBig" onclick="setDolPolling()"><span><i class="miniIcoManager"></i>@ENTITYSNAP.tools.dol@</span></a>
			</dd>		
			<% } %>	
            <%-- <dd>              
                <a id="onuGlobalConfig" onclick="onOnuGlobalConfigClick();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoManager"></i>@resources/COMMON.onuGlobalConfig@</span></a>
            </dd> --%>
			<dd>
			    <a id="baiduMap" onclick="addBaiduMap();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoGoogle"></i>@network/BAIDU.viewMap@</span></a>
            </dd>
            <dd id="dd_gponAutoAuthConfig">               
                <a id="gponAutoAuthConfig" onclick="showGponAutoAuth();" href="javascript:;" class="normalBtnBig"><span><i class="bmenu_eyesClose"></i>@GPON/onuauth.autoAuthConfig@</span></a>
            </dd>
            <dd>
                <a id="userAttention" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i class="bmenu_eyes"></i>@resources/COMMON.attention@</span></a>
            </dd>
            <dd>              
                <a id="userAttentionCancel" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i class="bmenu_eyesClose"></i>@resources/COMMON.cancelAttention@</span></a>
            </dd>
		</dl>		
	</div>
	<div style="dislpay: none">
		<div id="location">
            <table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
                 <tr>
                    <td width=80 class="rightBlueTxt">@ENTITYSNAP.basicInfo.deviceIpInfo@:</td>
					<td class="wordBreak"><s:property value="deviceIpList"/></td>
                </tr>
            </table>
		</div>
	</div>
	<div id=cpuandmemperformance>
		<table width="100%" border="0" cellspacing="0" cellpadding="0" rules="none">
			<tr>
				<td align="center">
					<div class="putCir" id="cpuCir"></div>
				</td>
				<td align="center">
					<div class="putCir" id="memCir"></div>
				</td>
				<td align="center">
					<div class="putCir" id="flashCir"></div>
				</td>
			</tr>
		</table>
		<div style="width:280px; margin:0 auto;">
			<ul class="leftFloatUl edge5 clrLendUl">
				<li style="margin-right:10px;">
					<span style="background:#1BBC9B;" class="cirLengend" ></span>
					0%-59%
				</li>
				<li style="margin-right:10px;">
					<span style="background:#F1C40F;" class="cirLengend" ></span>
					60%-84%
				</li>
				<li>
					<span style="background:#F26C63;" class="cirLengend" ></span>
					85%-100%
				</li>
			</ul>
		</div>
	</div>
    <div id=cmInfo style="dislpay: none">
        <table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="80">@CM.totalNum@:</td>
                <td id="cmNumTotal"> ${ oltCmNumStatic.cmNumTotal }</td>
                <td class="rightBlueTxt" width="80">@CM.onlineNum@:</td>
                <td id="cmNumOnline"> ${ oltCmNumStatic.cmNumOnline }</td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@CM.activeCMCount@:</td>
                <td id="cmNumActive"> ${ oltCmNumStatic.cmNumActive }</td>
                <td class="rightBlueTxt">@CM.offlineCMCount@:</td>
                <td id="cmNumOffline"> ${ oltCmNumStatic.cmNumOffline }</td>
            </tr>
        </table>
    </div>
    
    <div id="subInfo" style="dislpay: none">
    	<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="100">@EPON.onuTotal@:</td>
                <td id="onuTotal">0</td>
                <td class="rightBlueTxt" width="100">@EPON.onuOnline@:</td>
                <td id="onuOnline">0</td>
            </tr>
            <% if (uc.hasSupportModule("cmc")) {%>
	            <tr>
	                <td class="rightBlueTxt" width="100">@EPON.cmcTotal@:</td>
	                <td id="cmcTotal">0</td>
	                <td class="rightBlueTxt" width="100">@EPON.cmcOnline@:</td>
	                <td id="cmcOnline">0</td>
	            </tr>
            <% } %>
        </table>
    </div>
    <div id="putPonUsedInfo" style="dislpay: none">
    	<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
				<td class="rightBlueTxt" width="100">@Portal.ponSlotTotalNum@ :</td>
                <td id="slotTotalNum">0</td>
                <td class="rightBlueTxt" width="100">@EPON.ponUseInfo@:</td>
                <td id="ponUpNum">0</td>
			</tr><tr>
                 <td class="rightBlueTxt" width="100">@Portal.unusedPonNum@ :</td>
                <td id="unusedPon">0</td>
                <td class="rightBlueTxt" width="100">@Portal.unusedSlotNum@:</td>
                <td id="unusedSlot">0</td>
            </tr>
        </table>
    </div>
    <div id="runTime"></div>
	<script type="text/javascript">
	var totalTime = '<s:property value="oltAttribute.oltDeviceUpTime"/>';
	$(document).ready(function(){
		setInterval("syTime()", 300000);
		if(!VC.support("gpon")){
			$("#dd_gponAutoAuthConfig").hide();
		}
	})
    
    function syTime() {
    	Ext.Ajax.request({
    		url:"/epon/getOltUptimeByEntity.tv?entityId="+entityId+'&num='+ Math.random(),
    		method:"post",
    		cache: false,
    		success:function (response) {
    			var json = Ext.util.JSON.decode(response.responseText);
        		totalTime = json.sysUpTime;
        		oRunTime.clock.update({startTime:totalTime});
    		},
    		failure:function () {
    			window.parent.showMessageDlg(I18N.entitySnapPage.message, I18N.entitySnapPage.deviceUpTime);
    		}});
    }

    //设备不在线告警
   function deviceUnConnectable(deviceId){
	 //window.location.reload();
	 if(deviceId == entityId){
	   oRunTime.clock.update({totalTime:-1});
	 }
   }
</script>
<script type="text/javascript" src="/js/jquery/zebra.js"></script>
<script type="text/javascript">
	//处理ie6和ie7不能自适应的问题;
	$(function(){
		$(".btnGroupDl dd").each(function(){
			$(this).css("float","none")
			var w = $(this).find("a").outerWidth();
			$(this).width(w);			
		})
		$(".btnGroupDl dd").css("float","left");
	})
</script>
</body>
</Zeta:HTML>  
