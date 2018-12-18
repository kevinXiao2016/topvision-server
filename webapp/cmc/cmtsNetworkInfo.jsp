<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc" %>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import cmc/js/cmIndexPartition
    import js/customColumnModel
    import cmc.js.autoFunMenu
    import js.zetaframework.component.NetworkNodeSelector static
</Zeta:Loader>
    <link rel="stylesheet" type="text/css" href="../../js/ext/ux/gridfilters/css/GridFilters.css" />
    <link rel="stylesheet" type="text/css" href="../../js/ext/ux/gridfilters/css/RangeMenu.css" />
<style>
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
#partition{width:780px;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.epon.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
var pageSize = <%= uc.getPageSize() %>;
var entityPonRelationList = '${entityPonRelationObject}';
var oltSelectObject = '${oltSelectObject}';
var hasSupportEpon = '<s:property value="hasSupportEpon"/>';
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var cmcPerfPower = <%=uc.hasPower("cmcPerfParamConfig")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var cmcTypes = '${cmcTypes}';
var loopbackStatus;
var loopbackStatusIcomcls;
var selector;
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
var queryData = {};
var showUpSnrMin='${ccInfoThreshold.upSnrMin}';
var showUpSnrMax='${ccInfoThreshold.upSnrMax}';
var showDownSnrMin='${ccInfoThreshold.downSnrMin}';
var showDownSnrMax='${ccInfoThreshold.downSnrMax}';
var showUpPowerMin='${ccInfoThreshold.upPowerMin}';
var showUpPowerMax='${ccInfoThreshold.upPowerMax}';
var showDownPowerMin='${ccInfoThreshold.downPowerMin}';
var showDownPowerMax='${ccInfoThreshold.downPowerMax}';
var partitionData = {
    upSnrMin: showUpSnrMin,
    upSnrMax: showUpSnrMax,
    downSnrMin: showDownSnrMin,
    downSnrMax: showDownSnrMax,
    upPowerMin: showUpPowerMin,
    upPowerMax: showUpPowerMax,
    downPowerMin: showDownPowerMin,
    downPowerMax: showDownPowerMax,
    cmcId:''
};
var FLOW_K10 = 1000,
FLOW_M10 = FLOW_K10 * 1000;
$(function(){
    /******************************** OLT *********************************/
    // 设备重启, store重载 ;
    var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
        setCmtsStateByOlt(data, 0);
    });
    //板卡离线，store重载;
    var board_offline = top.PubSub.on(top.OltTrapTypeId.BOARD_OFFLINE, function(data){
        //var source: "SLOT:3"
        trapStoreReload();
    });
    //板卡上线，store重载;
    var board_online = top.PubSub.on(top.OltTrapTypeId.BOARD_ONLINE, function(data){
        trapStoreReload();
    });
    //板卡拔出，store重载;
    var board_remove = top.PubSub.on(top.OltTrapTypeId.BOARD_REMOVE, function(data){
        trapStoreReload();
    });
    //板卡重启，store重载;
    var board_reset = top.PubSub.on(top.OltTrapTypeId.BOARD_RESET, function(data){
        trapStoreReload();
    });
    /******************************** CMTS ********************************/
    //系统手动重启，store重载;
    var dol_reboot = top.PubSub.on(top.CmcTrapTypeId.DOL_REBOOT, function(data){
        if(top.isClearAlert(data)){
            setCmtsState(data, 1);
        }else{
            setCmtsState(data, 0);
        }
        //trapStoreReload()
    });
    //CMTS断电，store重载;
    var cmts_power_off = top.PubSub.on(top.CmcTrapTypeId.CMTS_POWER_OFF, function(data){
        if(top.isClearAlert(data)){
            setCmtsState(data, 1);
        }else{
            setCmtsState(data, 0);
        }
        //trapStoreReload();
    });
    //CMTS断纤，store重载;
    var cmts_link_lose = top.PubSub.on(top.CmcTrapTypeId.CMTS_LINK_LOSE, function(data){
        if(top.isClearAlert(data)){
            setCmtsState(data, 1);
        }else{
            setCmtsState(data, 0);
        }
    });
    //CMC复位，store重载;
    var cmc_reset = top.PubSub.on(top.CmcTrapTypeId.CMC_RESET, function(data){
        if(top.isClearAlert(data)){
            setCmtsState(data, 1);
        }else{
            setCmtsState(data, 0);
        }
        //trapStoreReload();
    });
    //CMTS下线，store重载;
    var cmc_offline = top.PubSub.on(top.CmcTrapTypeId.CMTS_OFFLINE, function(data){
        if(top.isClearAlert(data)){
            setCmtsState(data, 1);
        }else{
            setCmtsState(data, 0);
        }
    });
    
    //CMTS上线，store重载;
    var cmc_online = top.PubSub.on(top.CmcTrapTypeId.CMTS_ONLINE, function(data){
        setCmtsState(data, 1);
        //trapStoreReload();
    });
    
    $(window).on('unload', function(){
        top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
        top.PubSub.off(top.OltTrapTypeId.BOARD_OFFLINE, board_offline);
        top.PubSub.off(top.OltTrapTypeId.BOARD_ONLINE, board_online);
        top.PubSub.off(top.OltTrapTypeId.BOARD_REMOVE, board_remove);
        top.PubSub.off(top.OltTrapTypeId.BOARD_RESET, board_reset);
        
        top.PubSub.off(top.CmcTrapTypeId.DOL_REBOOT, dol_reboot);
        top.PubSub.off(top.CmcTrapTypeId.CMTS_POWER_OFF, cmts_power_off);
        top.PubSub.off(top.CmcTrapTypeId.CMTS_LINK_LOSE, cmts_link_lose);
        top.PubSub.off(top.CmcTrapTypeId.CMC_RESET, cmc_reset);
        top.PubSub.off(top.CmcTrapTypeId.CMTS_OFFLINE, cmc_offline);
        top.PubSub.off(top.CmcTrapTypeId.CMTS_ONLINE, cmc_online);
    });
    
});

function setCmtsState(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('cmcId') === data.entityId) {
                store.getAt(i).set('status', state);
                store.getAt(i).set('statusChangeTimeStr', '@framework/Date.Obscure@1@framework/Date.SECOND@@framework/Date.Ago@');
                store.getAt(i).commit();
                break;
            }
        }
    }
}

function setCmtsStateByOlt(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('manageIp') === ip) {
                store.getAt(i).set('status', state);
                store.getAt(i).set('statusChangeTimeStr', '@framework/Date.Obscure@1@framework/Date.SECOND@@framework/Date.Ago@');
                store.getAt(i).commit();
            }
        }
    }
}

function trapStoreReload(){
    if(store){
        store.reload();
    }
}

function refreshClick() {
    store.reload();
}
function doRefresh() {
    refreshClick();
}


function viewCmtsSnap(cmcId, name) {
    window.parent.addView('entity-' + cmcId, name, 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
}
function viewOltSnap(cmcId, nmName, entityId, entityIp, deviceStyle, manageName) {
    if (EntityType.isCcmtsWithoutAgentType(deviceStyle)) {
        window.parent.addView('entity-' + entityId, unescape(manageName), 'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + entityId);
    } else {
        viewCmcSnap(cmcId, unescape(nmName), deviceStyle);
    }
}
function viewCmcSnap(cmcId, nmName, cmcDeviceStyle) {
    if (EntityType.isCcmtsWithAgentType(cmcDeviceStyle)) {
        window.parent.addView('entity-' + cmcId, unescape(nmName), 'entityTabIcon',
                '/cmc/showCmcPortal.tv?cmcId=' + cmcId + "&productType=" + cmcDeviceStyle);
    } if (EntityType.isCmtsType(cmcDeviceStyle)) {
        viewCmtsSnap(cmcId, unescape(nmName))
    }else {
        window.parent.addView('entity-' + cmcId, unescape(nmName), 'entityTabIcon',
                '/cmc/showCmcPortal.tv?cmcId=' + cmcId);
    }
}
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
        displayInfo: true, items: ["-", String.format(I18N.COMMON.displayPerPage, pageSize), '-'
    ]});
    return pagingToolbar;
}

/* 
function doOnload() {
    onTypeChange();
    if (hasSupportEpon == 'true') {
        document.getElementById("cmcIpAddress").style.display = 'none';
    } else {
        document.getElementById("cmcIpAddress").style.display = 'block';
    }
}
*/
 
function renderSysStatus(value, p, record) {
    if (record.data.status == '1') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                I18N.CMC.label.online);
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                I18N.CMC.label.offline);
    }
    /* if(EntityType.isCmtsType(record.data.cmcDeviceStyle)){
        
    }
    if (record.data.topCcmtsSysStatus == '4') {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                I18N.CMC.label.online);
    } else {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                I18N.CMC.label.offline);
    } */
}

function onTypeChange() {
    var type = $("#ccmtsType option:selected").attr("value");
    switch (type) {
        case "0":
            $(".changeA").hide();
            $(".changeB").hide();
            $(".changeAB").hide();
            break;
        case "1":
            $(".changeA").show();
            $(".changeB").hide();
            $(".changeAB").show();
            break;
        case "2":
            $(".changeA").hide();
            $(".changeB").show();
            $(".changeAB").show();
    }
}
function sendRequest(url, method, param, sn, fn) {
    Ext.Ajax.request({url: url+'?_='+Math.random(), failure: fn, success: sn,timeout:1800000,  params: param});
}


/**
 * 时间换算。用于ONU连续在线时长的时间 换算：秒数 —> String
 */
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

function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    if(cellValue == "" || cellValue == null){
        return "--";
    }
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

function renderMac(value, p, record){
    if(record.data.topCcmtsSysMacAddr == "" || record.data.topCcmtsSysMacAddr == null){
        return "--";
    }else{
        return record.data.topCcmtsSysMacAddr;
    }
}


function openPartitionSelect(){
    window.top.createDialog("modalDlg",'@CM.setPartitionRight@',600, 370, '/cmlist/showPartitionSelect2.tv?partitionDataStr='+encodeURI(JSON.stringify(partitionData)), null, true,true);
}

function showEntityPerf(typeId,name,cmcId,flag){
	if(EntityType.isCcmtsType(typeId)){
        window.top.addView("entity-" + cmcId,name,'entityTabIcon',"/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId="+cmcId+"&timeType=Today&perfType=noise&productType="+typeId+"&flag="+flag,null,true);   
    }else{
        window.top.addView("entity-" + cmcId,name,'entityTabIcon',"/cmts/showCmtsCurPerf.tv?module=8&cmcId="+cmcId+"&timeType=Today&perfType=noise&productType="+typeId+"&flag="+flag,null,true);   
    }  
}

function onlineRatioRender(value, p, record){
	var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    value=record.data.onlineNum+'/'+record.data.allNum
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,1,value)                                  
}
function upSpeedRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=parseFloat(value/1000000).toFixed(2);
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,2,value)                                  
}
function downSpeedRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=parseFloat(value/1000000).toFixed(2);
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,3,value)                                  
}
function cmOutAvgRender(value, p, record){
	console.log(value)
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
    	value='--';
    }else{
    	if(sysElecUnit=='dBμV'){
    		value=parseFloat(value).toFixed(2)+60;
    	}else{
    		value=parseFloat(value).toFixed(2);
    	}
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,4,value)                                  
}
function cmOutRatioRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=((parseFloat(value)*100).toFixed(2))+'%';
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,5,value)                                  
}
function cmInAvgRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
    	if(sysElecUnit=='dBμV'){
            value=parseFloat(value).toFixed(2)+60;
        }else{
            value=parseFloat(value).toFixed(2);
        }
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,6,value)                                  
}
function cmInRatioRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=((parseFloat(value))*100).toFixed(2)+'%';
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,7,value)                                  
}
function cmUpSnrAvgRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=parseFloat(value).toFixed(2);
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,8,value)                                  
}
function cmUpSnrRatioRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=((parseFloat(value))*100).toFixed(2)+'%';
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,9,value)                                  
}
function cmDownSnrAvgRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=parseFloat(value).toFixed(2);
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,10,value)                                  
}
function cmDownSnrRatioRender(value, p, record){
    var cmcId = record.data.cmcId;
    var name = record.data.nmName; 
    var typeId = record.data.cmcDeviceStyle;
    if(value==null||value===''){
        value='--';
    }else{
        value=((parseFloat(value)*100).toFixed(2))+'%';
    }
    return String.format("<a href='javascript:;' onClick='showEntityPerf(\"{0}\",\"{1}\",\"{2}\",{3})'>{4}</a>",typeId, name,cmcId,11,value)                                  
}

function partitionChanged(data){
    partitionData = data;
    $('#partition').cmIndexPartition.change(partitionData);
    var tmpData = $.extend(queryData, partitionData);
//     store.load({
//         params: tmpData
//     });
}
Ext.onReady(function () {
    Ext.QuickTips.init();
    var w = document.body.clientWidth;
    var h = document.body.clientHeight;
    $('#partition').cmIndexPartition().bind('click', openPartitionSelect);
    
    $('#partition').cmIndexPartition.change(partitionData);
        
    function opeartionRender(value, p, record) {
        if(!record.data.attention){
            return String.format('<a href="#" onclick=\'viewCmcSnap("' + record.data.cmcId + '","' + escape(record.data.nmName) + '","' + record.data.cmcDeviceStyle + '");\'>{0}</a>',
                        value);
        }else{
            return String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick=\'viewCmcSnap("' + record.data.cmcId + '","' + escape(record.data.nmName) + '","' + record.data.cmcDeviceStyle + '");\'>{0}</a>',
                    value);
        }

    }
    var columns = null;
    columns = [
        {id:'entityName',header: '<div class="txtCenter">@COMMON.alias@</div>', sortable:true, align : 'left', dataIndex: 'nmName',renderer:opeartionRender},
        {header: '<div class="txtCenter">@CCMTS.uplinkdevice@</div>' , width: 200, sortable:true, align : 'left', dataIndex: 'manageIp', renderer:function(value, p, record) {
            var disValue = value;
            if (EntityType.isCcmtsWithoutAgentType(record.data.cmcDeviceStyle)) {
                disValue = record.data.manageName +  "("+disValue+")";
            }
            return '<a href="#" class=my-link onclick=\'viewOltSnap("' + record.data.cmcId + '","' + escape(record.data.nmName)
                    + '","' + record.data.entityId + '","' + value + '","' + record.data.cmcDeviceStyle + '","' + escape(record.data.manageName) + '");\'>' + disValue + '</a>';
        }},
        {header: '<div class="txtCenter">@COMMON.name@</div>', width: 110, sortable:true,align: 'left', dataIndex: 'topCcmtsSysName'},
        {header:'<div class="txtCenter">@CCMTS.location@</div>',dataIndex:'location',align:'left',sortable:true,hidden:true,renderer:addCellTooltip},
        {header: '<div class="txtCenter">@CCMTS.onlineRatio@</div>', width: 110, sortable:true,align: 'center', dataIndex: 'onlineRatio',renderer:onlineRatioRender},
        {header: '<div class="txtCenter">@CCMTS.UpFlowSpeed@(Mbps)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'upSpeed',renderer:upSpeedRender},
        {header: '<div class="txtCenter">@CCMTS.DownFlowSpeed@(Mbps)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'downSpeed',renderer:downSpeedRender},
        {header: '<div class="txtCenter">@CCMTS.cmAvgTxPower@(@{unitConfigConstant.elecLevelUnit}@)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'cmOutPowerAvg',renderer:cmOutAvgRender},
        {header: '<div class="txtCenter">@CCMTS.TxNotInRange@</div>', width: 110, sortable:true,align: 'center', dataIndex: 'cmOutPowerNotInRange',renderer:cmOutRatioRender},
        {header: '<div class="txtCenter">@CCMTS.cmAvgRePower@(@{unitConfigConstant.elecLevelUnit}@)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'cmRePowerAvg',renderer:cmInAvgRender},
        {header: '<div class="txtCenter">@CCMTS.ReNotInRange@</div>', width: 110, sortable:true,align: 'center', dataIndex: 'cmRePowerNotInRange',renderer:cmInRatioRender},
        {header: '<div class="txtCenter">@CCMTS.upSnrAvg@(dB)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'upSnrAvg',renderer:cmUpSnrAvgRender},
        {header: '<div class="txtCenter">@CCMTS.upSnrNotInRange@</div>', width: 110, sortable:true,align: 'center', dataIndex: 'upSnrNotInRange',renderer:cmUpSnrRatioRender},
        {header: '<div class="txtCenter">@CCMTS.downSnrAvg@(dB)</div>', width: 110, sortable:true,align: 'center', dataIndex: 'downSnrAvg',renderer:cmDownSnrAvgRender},
        {header: '<div class="txtCenter">@CCMTS.downSnrNotInRange@</div>', width: 110, sortable:true,align: 'center', dataIndex: 'downSnrAvgNotInRange',renderer:cmDownSnrRatioRender},
        {header:'<div class="txtCenter">@CCMTS.note@</div>',dataIndex:'note',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip}
    ];

    store = new Ext.data.JsonStore({
        url: '/cmtsInfo/queryCmcList.tv',
        root: 'data1',
        idProperty: "cmcId",
        totalProperty: 'rowCount',
        remoteSort: true,
        fields: ['cmcId', 'nmName','cmcDeviceStyle', 'cmcDeviceStyleString', 'topCcmtsSysLocation',
            'manageIp', 'entityId', 'ipAddress', 'manageName', 'onlineRatio','upSpeed','downSpeed','cmOutPowerAvg','cmOutPowerNotInRange','cmRePowerAvg',
            'cmRePowerNotInRange','upSnrAvg','upSnrNotInRange','downSnrAvg','downSnrAvgNotInRange','status','topCcmtsSysName', 'typeId', 'typeName','location','note','onlineNum','allNum']
        //排序规则
    });

    //var cm = new Ext.grid.ColumnModel(columns);
    var cmConfig = CustomColumnModel.init("cmclist",columns,{}),
        cm = cmConfig.cm,
        sortInfo = cmConfig.sortInfo || {field: 'manageIp', direction: 'ASC'};
        
    store.setDefaultSort(sortInfo.field, sortInfo.direction);
        
    var toolbar = null;
//     if (hasSupportEpon == 'true') {
//         toolbar = [
//             buildEntityInput(),//'-',
//             //buildConnectPersonInput(),//'-',
//             {xtype: 'tbspacer', width: 3},
//             buildStatusInput(),
//             {xtype: 'tbspacer', width: 3},
//             buildDeviceTypeSelect(),//'-',
//             {xtype: 'tbspacer', width: 3},
//             buildOltSelect(),
//             {xtype: 'tbspacer', width: 3},
//             buildPonSelect(),'-',
//             {xtype: 'tbspacer', width: 3},
//             buildDocsis30Select(),'-',
//             //buildPatitionIndex(),
//             //buildIpInput(),//'-',
//             //buildMacInput(),'-',
//             {text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: queryClick},
            
                        
//         ];
//     } else {
//         toolbar = [
//             buildEntityInput(),
//             {xtype: 'tbspacer', width: 3},
//             buildDeviceTypeSelect(),
//             {xtype: 'tbspacer', width: 3},
//             buildStatusInput(),'-',
//             {xtype: 'tbspacer', width: 3},
//             buildDocsis30Select(),'-',
//             //buildPatitionIndex(),
//             //buildConnectPersonInput(),'-',
//             //buildIpInput(),'-',
//             //buildMacInput(),'-',
//             {text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: query8800BClick},
            
//         ];
//     }
    
    

   // configure whether filter query is encoded or not (initially)
   var encode = false;
   // configure whether filtering is performed locally or remotely (initially)
   var local = true;
   
    /* var filters = new Ext.ux.grid.GridFilters({
        // encode and local configuration options defined previously for easier reuse
        encode: encode, // json encode the filter query
        local: local,   // defaults to false (remote filtering)
        menuFilterText: "@grid.filters@",
        filters: [
        {
            type: 'list',
            dataIndex: 'topCcmtsSysStatus',
            options: [{text:'@CMC.label.online@',id:4},{text:'@CMC.label.offline@',id:2}],
            //此处如果需要添加后台筛选,只需要添加clickFn,
            //此功能未做国际化(修改GridFilters.js)，传出来的参数为在线,离线,null;
            // clickFn : function(para){
            //  alert(para)
            //},   
            phpMode: true
        }
        ]
    });   */

    /*  var w = document.body.clientWidth;
     var h = document.body.clientHeight; */
    grid = new Ext.grid.GridPanel({
        //plugins: [filters],
        stripeRows:true,
        region: "center",
        bodyCssClass: 'normalTable',
        id: 'extGridContainer',
        animCollapse: animCollapse,
        trackMouseOver:true,
        border: true,
        store: store,
       
        enableColumnMove : true,
        listeners : {
            columnresize: function(){
                CustomColumnModel.saveCustom('cmtsInfo', cm.columns);
            },
            sortchange : function(grid,sortInfo){
                CustomColumnModel.saveSortInfo('cmtsInfo', sortInfo);
            }
        },
        cm: cm,
        autoExpandColumn:'entityName',
//        autoExpandMin:100,
        viewConfig: {  /* forceFit:true,   */hideGroupedColumn: true,enableNoGroups: true},
        bbar: buildPagingToolBar()
    });
    store.load({params:{start:0, limit: pageSize}});
    var viewPort = new Ext.Viewport({
        layout: "border",
        items: 
         [{
             region: 'north',
             border: false,
             contentEl: 'topPart',
             height: 70
           },
           grid
         ]
    });
    //doOnload();
    
    // 添加查询框的回车事件
    $(".ccmtsListToolBar").keydown(function(event) {
        if (event.keyCode == 13) {
            if (hasSupportEpon == 'true') {
                queryClick();
            } else {
                query8800BClick();
            }
        }
    });
    
//     if (hasSupportEpon == 'true') {
//         selector = new NetworkNodeSelector({
//             id : 'oltSelect',
//             renderTo : "oltselectCont",
//             listeners:{
//                 selectChange: oltSelectChange
//             }
//         });
//     }
});

String.prototype.trim=function() {  
    return this.replace(/(^\s*)|(\s*$)/g,'');  
}; 
/**
 * 支持EPON模块查询
 */
function queryClick() {
    //获取各项的值
    var nmName = Zeta$('name').value.trim();
    if(nmName != "" && !Validator.isAnotherName(nmName)) {
        window.parent.showMessageDlg(I18N.COMMON.tip, "@COMMON.inputQueryInfo@");
        return;
    }
    /* 
    //验证IP地址是否符合模糊匹配的格式(只需要在选择设备类型为8800B时且输入了IP条件时才需要验证)
    if ( EntityType.isCcmtsWithAgentType(deviceType) && cmcIp != "" && !Validator.isFuzzyIpAddress(cmcIp)) {
        top.afterSaveOrDelete({
            title: I18N.CMC.tip.tipMsg,
            html: '<b class="orangeTxt">'+I18N.CMC.tip.ipInputFormatError+'</b>'
        });
        $("#ipAddress").focus();
        return;
    }
    if ( EntityType.isCmtsType(deviceType) && cmcIp != "" && !Validator.isFuzzyIpAddress(cmcIp)) {
        top.afterSaveOrDelete({
            title: I18N.CMC.tip.tipMsg,
            html: '<b class="orangeTxt">'+I18N.CMC.tip.ipInputFormatError+'</b>'
        });
        $("#ipAddress").focus();
        return;
    }
    //验证MAC地址是否符合模糊匹配的格式(只需要在输入了MAC地址条件时才需要验证)
    if (cmcMac != "" && !Validator.isFuzzyMacAddress(cmcMac)) {
        top.afterSaveOrDelete({
            title: I18N.CMC.tip.tipMsg,
            html: '<b class="orangeTxt">'+I18N.CCMTS.macError+'</b>'
        });
        $("#cmcMacAddress").focus();
        return;
    }
    */
    /* 
    store.on("beforeload", function() {
        store.baseParams = {deviceType:deviceType, entityId: entityId, ponId: ponId, cmcMac: cmcMac, cmcType:cmcType,
            cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize,topCcmtsSysStatus:cmcStatus};
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
    store.load({params: {deviceType:deviceType, entityId: entityId, ponId: ponId, cmcMac: cmcMac, cmcType:cmcType,
        cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize,topCcmtsSysStatus:cmcStatus}});
    */
    store.baseParams={
        start: 0,
        limit: pageSize,    
        cmcName: nmName
    }
    store.load();
}

function query8800BClick() {
    var deviceType = Zeta$('deviceType').value;
    //var cmcMac = Zeta$('cmcMacAddress').value;
    var nmName = Zeta$('name').value.trim();
    //var connectPerson = null;//Zeta$('connectPerson').value;
    //var cmcIp = Zeta$('ipAddress').value;
    var cmcStatus = $('#status_sel').val();
    var mddInterval = $("#mddInterval").val();
    if(nmName != "" && !Validator.isAnotherName(nmName)) {
        window.parent.showMessageDlg(I18N.COMMON.tip, "@COMMON.inputQueryInfo@");
        return;
    }
    /* 
    //验证IP地址是否符合模糊匹配的格式(只需要在选择设备类型为8800B时且输入了IP条件时才需要验证)
    if (cmcIp != "" && !Validator.isFuzzyIpAddress(cmcIp)) {
        top.afterSaveOrDelete({
            title: I18N.CMC.tip.tipMsg,
            html: '<b class="orangeTxt">'+I18N.CMC.tip.ipInputFormatError+'</b>'
        });
    }
        //验证MAC地址是否符合模糊匹配的格式(只需要在输入了MAC地址条件时才需要验证)
    if (cmcMac != "" && !Validator.isFuzzyMacAddress(cmcMac)) {
        top.afterSaveOrDelete({
            title: I18N.CMC.tip.tipMsg,
            html: '<b class="orangeTxt">'+I18N.CCMTS.macError+'</b>'
        });
    }

    store.on("beforeload", function() {
        store.baseParams = {cmcType:deviceType,cmcMac: cmcMac,
            cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize,topCcmtsSysStatus:cmcStatus};
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
    store.load({params: {cmcType:deviceType,cmcMac: cmcMac,
        cmcName:nmName, connectPerson:connectPerson, cmcIp: cmcIp, start:0,limit:pageSize,topCcmtsSysStatus:cmcStatus}});
    */
    store.baseParams={
        start: 0,
        limit: pageSize,    
        cmcName: nmName,
        cmcType: deviceType,
        topCcmtsSysStatus: cmcStatus,
        mddInterval : mddInterval
    }
    store.load();
}

</script>
</head>
<body >
<div id="topPart" class='query-container pT3'>
         <div >          
         <table>      
         <tr>
             <td width=120 align=right>&nbsp;@network/NETWORK.nameQuery@/MAC@COMMON.maohao@</td>
             <td width=120><input class="normalInput" type=text style="width: 120px" id="name" maxlength="63"></td>              
             <td >
                <ul class="myquery">
                    <li>
                        <a href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
                    </li>
                </ul>
             </td>
         </tr>
         <tr>
            <td width=120 align=right>@COMMON.InRange@@COMMON.maohao@</td>
            <td colspan="15"><div id="partition"></div></td>
         </tr>
         </table>
         </div>
    </div>
</body>
<script language="JavaScript" type="text/javascript">
    function addListener(element, e, fn) {
        if (element.addEventListener) {
            element.addEventListener(e, fn, false);
        } else {
            element.attachEvent("on" + e, fn);
        }
    }

    function validateMacAddress(macaddr)
    {
        var reg1 = /^([A-Fa-f0-9]{2,2}\:){0,5}[A-Fa-f0-9]{0,2}$/;
        var reg2 = /^([A-Fa-f0-9]{2,2}\-){0,5}[A-Fa-f0-9]{0,2}$/;
        if (reg1.test(macaddr)) {
            return true;
        } else if (reg2.test(macaddr)) {
            return true;
        } else {
            return false;
        }
    }

    function validateIpAddress(ipAddr) {
        var reg = /^((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){0,3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))$/;
        if (reg.test(ipAddr)) {
            return true;
        }
        return false;
    }

    function checkInfo() {
        var s = $('#cmcMacAddress');
        var cmcMacAddress = Zeta$('cmcMacAddress').value;
        if (cmcMacAddress.length == 0) {
            return;
        }
        var pattern = /^[a-z0-9:]{1,17}$/i;
        var result = cmcMacAddress.match(pattern);
        if (result == null) {
            Ext.Msg.alert(I18N.CMC.tip.tipMsg, I18N.CCMTS.macErrorMessage);
        }
    }
    function loadSniUplinkLoopbackStatus(v) {
        $.ajax({
            url: '/cmc/sni/loadSniUplinkLoopbackStatus.tv',
            type: 'POST',
            async: false,
            data: "cmcId=" + v + "&num=" + Math.random(),
            dataType:"json",
            success: function(json) {
                if (json && json.cmcSniConfig && json.cmcSniConfig.topCcmtsSniUplinkLoopbackStatus) {
                    loopbackStatus = json.cmcSniConfig.topCcmtsSniUplinkLoopbackStatus;
                }else{
                    loopbackStatus = false;
                }
            }, error: function(json) {
        }, cache: false,
            complete: function (XHR, TS) {
                XHR = null
            }
        });
    }
    function changeEntityName(entityId, name) {
        store.reload();
    }
    
</script>
</Zeta:HTML>