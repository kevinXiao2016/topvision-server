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
    import js/customColumnModel
    import cmc.js.autoFunMenu
    import tools/cmdUtil
    import js.zetaframework.component.NetworkNodeSelector static
    import js/ext/ux/gridfilters/menu/RangeMenu
    import js/ext/ux/gridfilters/menu/ListMenu
    
    import js/ext/ux/gridfilters/filter/Filter
    import js/ext/ux/gridfilters/filter/StringFilter
    import js/ext/ux/gridfilters/filter/DateFilter
    import js/ext/ux/gridfilters/filter/ListFilter
    import js/ext/ux/gridfilters/filter/NumericFilter
    import js/ext/ux/gridfilters/filter/BooleanFilter
</Zeta:Loader>
	<link rel="stylesheet" type="text/css" href="../../js/ext/ux/gridfilters/css/GridFilters.css" />
    <link rel="stylesheet" type="text/css" href="../../js/ext/ux/gridfilters/css/RangeMenu.css" />
<style>
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.epon.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var store = null;
var grid = null;
Ext.BLANK_IMAGE_URL = "../images/s.gif";
var pageSize = <%= uc.getPageSize() %>;
var entityPonRelationList = ${entityPonRelationObject};
var oltSelectObject = ${oltSelectObject};
var hasSupportEpon = '<s:property value="hasSupportEpon"/>';
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var cmcPerfPower = <%=uc.hasPower("cmcPerfParamConfig")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var cmcTypes = ${cmcTypes};
var loopbackStatus;
var loopbackStatusIcomcls;
var selector;

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

//添加到百度地图
function addBaiduMap(){
	var se = grid.getSelectionModel().getSelected().data;
	var entityId = se.cmcId;
	var entityName = se.nmName
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + entityId + "&entityName=" + entityName, null, true, true);
}

//软件升级
function updateCmc() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('renameCmcJSP', I18N.CMC.title.ccmtsUpdate, "normal_4_3", 500,
            'cmc/showCmcUpdate.tv?cmcId=' + cmcId, null, true, true);
}
function viewCmtsSnap(cmcId, name) {
    window.parent.addView('entity-' + cmcId, name, 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
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

function renderMddInterval(value, p, record){
	if (record.data.mddInterval > 0) {
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
                'ON');
    } else if(record.data.mddInterval == 0){
        return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
                'OFF');
    }else{
    	return '--';
    }
}

function cmcReset() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.confirmResetCcmts, function(button, text) {
        if (button == "yes") {
            $.ajax({
                url:'cmc/resetCmc.tv?cmcId=' + cmcId,
                type:'POST',
                dateType:'json',
                success:function(response) {
                    if (response.message == "success") {
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsSuccess);
                    	top.afterSaveOrDelete({
               				title: I18N.COMMON.tip,
               				html: '<b class="orangeTxt">' + I18N.CCMTS.resetCcmtsSuccess + '</b>'
               			});
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsFail);
                    }
                },
                error:function() {
                },
                cache:false
            });
        }
    });
}


function cmcReplaceI() {
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	var entityId = record.data.cmcId;
	var dialogTitle = String.format('@CCMTS.replace.replace@ {0} <label class="pL10">@CCMTS.softVersion@ </label>{1} ',record.data.nmName, record.data.topCcmtsSysSwVersion);
    if(record.data.status == 1){
    	top.showConfirmDlg('@COMMON.tip@','@CCMTS.replace.onlinetip@', function(button, text){
			if(button === 'yes'){
				window.top.createDialog('replaceCmcI', dialogTitle, 800, 500, '/cmc/replace/showCmcReplaceIView.tv?cmcId=' + entityId, null, true, true);
			}
		});	
    } else{
        window.top.createDialog('replaceCmcI', dialogTitle, 800, 500, '/cmc/replace/showCmcReplaceIView.tv?cmcId=' + entityId, null, true, true);
    }
}

function cmcReplaceII() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.cmcId;
    var dialogTitle = '@CCMTS.replace.replace@';
	window.top.createDialog('replaceCmcII', dialogTitle, 600, 250,
	            '/cmc/replace/showCmcReplaceIIView.tv?cmcId=' + entityId, null, true, true);
}

function cmcNoMacBind() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.confirmNoMacBindCcmts, function(button, text) {
        if (button == "yes") {
            $.ajax({
                url:'cmc/cmcNoMacBind.tv?cmcId=' + cmcId,
                type:'POST',
                dateType:'json',
                success:function(response) {
                    if (response.message == "success") {
                        if (hasSupportEpon == 'true') {
                            queryClick();
                        } else {
                            query8800BClick();
                        }
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.noMacBindCcmtsSuccess);
                        top.afterSaveOrDelete({
        	   				title: '@COMMON.tip@',
        	   				html: '<b class="orangeTxt">@CCMTS.noMacBindCcmtsSuccess@</b>'
        	   			});
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.noMacBindCcmtsFail);
                    }
					//cancelClick();
                },
                error:function() {
                },
                cache:false
            });
        }
    });
}
/**
 * added by huangdongsheng
 * 添加到google 地图
 */
function onAddToGoogleClick() {
    window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
}
function cmcDetailInfo() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    viewCmcSnap(record.data.cmcId, unescape(record.data.nmName), record.data.cmcDeviceStyle);
}

function deviceTypeSelectChange() {
    var deviceType = Zeta$('deviceType').value;
    //var oltPosition = Zeta$('oltSelect');
    var ponPosition;
    if (hasSupportEpon == 'true') {
    	ponPosition = Zeta$('ponSelect');
    }
    /* for (var i = 1,len = oltPosition.length; i <= len; i++)
    {
        oltPosition.options[1] = null;
    }
    for (var i = 1,len = ponPosition.length; i <= len; i++)
    {
        ponPosition.options[1] = null;
    } */
    //Zeta$('ipAddress').value = "";
    selector.setValue(-1);
    Zeta$('ponSelect').value = "";
    if (EntityType.isCcmtsWithoutAgentType(deviceType)) {
        if (hasSupportEpon == 'true') {
        	$("#olt,#pon").show();
        }
        //$("#cmcIpAddress").hide();
        //document.getElementById("mac").style.display = 'block';
        /* for (var i = 0; i < oltSelectObject.length; i++) {
            var option = document.createElement('option');
            option.value = oltSelectObject[i].entityId;
            option.text = oltSelectObject[i].entityIp;
            try {
                oltPosition.add(option, null);
            } catch(ex) {
                oltPosition.add(option);
            }
        } */
    } else if (EntityType.isCcmtsWithAgentType(deviceType)) {
    	selector.setValue(-1);
        if (hasSupportEpon == 'true') {
	        document.getElementById("olt").style.display = 'none';
	        document.getElementById("pon").style.display = 'none';
        }
        //document.getElementById("cmcIpAddress").style.display = 'block';
        //document.getElementById("mac").style.display = 'block';
    } else if (EntityType.isCmtsType(deviceType)) {
    	selector.setValue(-1);
        if (hasSupportEpon == 'true') {
	    	document.getElementById("olt").style.display = 'none';
	        document.getElementById("pon").style.display = 'none';
        }
        //document.getElementById("mac").style.display = 'none';
        //document.getElementById("cmcIpAddress").style.display = 'block';
    } else {
        if (hasSupportEpon == 'true') {
	        selector.setValue(-1);
	        Zeta$('ponSelect').value = 0;
	        document.getElementById("olt").style.display = 'block';
	        document.getElementById("pon").style.display = 'block';
        }
        //document.getElementById("cmcIpAddress").style.display = 'none';
        //document.getElementById("mac").style.display = 'block';
    }
}

function oltSelectChange( selected ) {
    var entityId = selected.value;
    var ponPosition = Zeta$('ponSelect');
    for (var i = 1,len = ponPosition.length; i <= len; i++)
    {
        ponPosition.options[1] = null;
    }
    
    for (var i = 0; i < entityPonRelationList.length; i++) {
        if (entityPonRelationList[i].entityId == entityId) {
            var option = document.createElement('option');
            var ponIndex = entityPonRelationList[i].ponIndex;
            option.value = entityPonRelationList[i].ponId;
            option.text = ((ponIndex / 0xFFFF) >> 16) + "/" + entityPonRelationList[i].ponNo;
            try {
                ponPosition.add(option, null);
            } catch(ex) {
                ponPosition.add(option);
            }
        }
    }
	//YangYi 2013-10-31增加,将PON口文字排序
    /* var optionTemp = document.createElement('option');
    for (var j = 1; j < ponPosition.options.length; j++) {
        for (var k = j + 1; k < ponPosition.options.length; k ++) {
            if (ponPosition.options[j].text > ponPosition.options[k].text) {
                optionTemp.value = ponPosition.options[j].value;
                optionTemp.text = ponPosition.options[j].text;
                ponPosition.options[j].value = ponPosition.options[k].value;
                ponPosition.options[j].text = ponPosition.options[k].text;
                ponPosition.options[k].value = optionTemp.value;
                ponPosition.options[k].text = optionTemp.text;
            }
        }
    } */
}

//性能管理;
function ccmtsPerfManage() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    var $nmName = record.data.nmName;
    window.parent.addView('cmcPerfParamConfig', "@CCMTS.ccmtsPerformCollectConfig@[" + $nmName + "]", "bmenu_preference", '/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + cmcId);
}
function ccmtsVlanConfig() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcIp = record.data.manageIp;
    var cmcId = record.data.cmcId;
    window.parent.addView("cmcVlan-" + cmcId, I18N.CCMTS.vlanConfig, "entityTabIcon", "/cmcVlan/showCcmtsVlanJsp.tv?cmcId=" + cmcId);
}

//syslog 配置;
function ccmtsSyslogManagement() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    var cmcId = record.data.cmcId;
    if (EntityType.isCcmtsWithAgentType(record.data.cmcDeviceStyle)) {
        entityId = cmcId;
    }
    window.parent.createDialog("syslogManagement", I18N.syslog.syslogConfig, 800, 500, "/cmc/showSyslogManagement.tv?entityId=" + entityId + "&cmcId=" + cmcId, null, true, true);
}

//CCMTS Docsis 3.0 配置
function show8800ADocsisConfig() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.entityId;
    var cmcId = record.data.cmcId;
    var cmcType = record.data.cmcDeviceStyleString;
    window.top.createDialog('cmcDocsisView', I18N.cmc.docsisConfig, 600, 370,
            '/cmc/loadDocsisConfig.tv?entityId=' + entityId + "&cmcType=" + cmcType + "&cmcId=" + cmcId, null, true, true);
}

//CCMTS Docsis 3.0 配置
function show8800BDocsisConfig() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.cmcId;
    var cmcId = record.data.cmcId;
    var cmcType = record.data.cmcDeviceStyleString;
    window.top.createDialog('cmcDocsisView', I18N.cmc.docsisConfig, 600, 370,
            '/cmc/loadDocsisConfig.tv?entityId=' + entityId + "&cmcType=" + cmcType + "&cmcId=" + cmcId, null, true, true);
}

function ccmtsIpSubVlan() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('ccmtsIpSubVlan', I18N.CCMTS.vlanSubInfo, 800, 500,
            '/cmcVlan/showCcmtsIpSubVlan.tv?cmcId=' + cmcId, null, true, true);
}

function modifyDevice() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var entityId = record.data.cmcId;
    window.top.createDialog('renameEntity', I18N.COMMON.deviceInfo, 600, 370,
            '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

function buildEntityInput() {
    return '<td width=150 align=center>&nbsp;@network/NETWORK.nameQuery@/MAC:' + '</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 120px" id="name" maxlength="63"></td>'
}

function buildConnectPersonInput() {
    return '<td width=120 align=center>' + I18N.CCMTS.contactPerson + ':' + '</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 80px" id="connectPerson"></td>'
}

function buildDeviceTypeSelect() {
    var head = '<td style="width: 40px;"  align="right">' + I18N.CCMTS.entityStyle + ':' + '</td>&nbsp;' +
           '<td style="width: 100px;">' +
           '<select id="deviceType" class="normalSel" style="width: 180px;" onchange="deviceTypeSelectChange()">' +
           '<option value="0" selected>' + I18N.COMMON.pleaseSelect + '</option>';
    var tail = '</select></td>' ;
    var options = "";
    for(var i = 0; i < cmcTypes.length; i ++){
        options += '<option value="' +cmcTypes[i].typeId+ '">' + cmcTypes[i].displayName + '</option>';
    }
    return head + options + tail;
}

function buildOltSelect() {
    return '<div id="olt"><table><tr><td>OLT:</td><td><div id="oltselectCont" class="w120"></div></td></tr></table></div>';
}

function buildPonSelect() {
    return '<div id="pon"><td style="width: 40px;" align="right">PON:</td>&nbsp;' +
           '<td style="width: 70px;">' +
           '<select style="width: 70px;" class="normalSel" id="ponSelect">' +
           '<option value="0" <s:if test="type==0">selected</s:if>>' + I18N.COMMON.pleaseSelect + '</option>' +
           '</select></td></div>'
}

function buildIpInput() {
    return '<div id="cmcIpAddress"><td width=40px align="right">IP:</td>&nbsp;' +
           '<td><input class="normalInput" type=text style="width: 100px"  id="ipAddress"></td></div>'
}

function buildMacInput() {
    return '<div id="mac"><td width=40px align=center>MAC:</td>&nbsp;' +
           '<td><input class="changeAB normalInput" type=text style="width: 100px" id="cmcMacAddress";" maxlength="17"></td></div>'
}

function buildStatusInput() {
    return '<td width=40px align=center>'+I18N.CCMTS.entityStatus+':</td>&nbsp;' +
           '<td><select class="normalSel" id="status_sel"><option value="-1">'+I18N.CMC.text.pleaseselec+'</option><option value="1">'+I18N.CMC.label.online+'</option><option value="0">'+I18N.CMC.label.offline+'</option></select></td>'
}

function buildDocsis30Select(){
	return '<td width=40px align=center>@CCMTS.Docsis3.0@@COMMON.maohao@</td>&nbsp;' +
    '<td><select class="normalSel" id="mddInterval"><option value="-1">'+I18N.CMC.text.pleaseselec+'</option><option value="1">ON</option><option value="0">OFF</option></select></td>'
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
//重新发现CC8800B added by huangdongsheng 
function onDiscoveryAgainClick() {
	var record = grid.getSelectionModel().getSelected();
	var cmcId = record.data.cmcId;
	var magageIp = record.data.manageIp;
	reTopoDevice(cmcId, magageIp);
}

//重新Topo设备
function reTopoDevice(entityId, manageIp){
	window.top.discoveryEntityAgain(entityId, manageIp, function() {
		refreshClick();
	});
}

//刷新通过OLT设备管理的CC设备
function refreshCcmtsOnOLt(){
    var record = grid.getSelectionModel().getSelected();
    var cmcId = record.data.cmcId;
    var cmcType = record.data.cmcDeviceStyle;
    var entityId = record.data.entityId;
    window.parent.showWaitingDlg("@COMMON.wait@", "@network/NETWORK.reTopoingA@",'waitingMsg','ext-mb-waiting');
    $.ajax({
        url:'cmc/refreshCC.tv?cmcId='+ cmcId+'&cmcType='+cmcType+'&entityId='+entityId,
        type:'POST',
        dateType:'json',
        success:function(response){
        	window.top.closeWaitingDlg();
            if(response == "success"){
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCcmtsSuccess);
                top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
	   			});
                store.reload();
                //window.location.reload();
            }else{
                window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
            } 
        },
        error:function(){
        	//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCcmtsFail);
        	window.parent.closeWaitingDlg();
        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
        },
        cache:false
    });
}
/* 
function refreshCC() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    var cmcMac = record.data.topCcmtsSysMacAddr;

    var cmcType = record.json.cmcDeviceStyle;
    var entityId = record.json.entityId;
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.confirmRefreshCcmts, function(button, text) {
        if (button == "yes") {
            window.parent.showWaitingDlg(I18N.COMMON.waiting, String.format(I18N.CCMTS.refreshingCcmts), 'waitingMsg', 'ext-mb-waiting');
            if(EntityType.isCcmtsWithAgentType(cmcType) || EntityType.isCmtsType(cmcType)){
                onDiscoveryAgainClick(cmcId);
            }else{//8800A
                refreshCcmtsOnOLt(cmcId, cmcType, entityId);
            }
        }
    });
}
 */
function viewOltSnap(cmcId, nmName, entityId, entityIp, deviceStyle, manageName) {
    if (EntityType.isCcmtsWithoutAgentType(deviceStyle)) {
        window.parent.addView('entity-' + entityId, unescape(manageName), 'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + entityId);
    } else {
        viewCmcSnap(cmcId, unescape(nmName), deviceStyle);
    }
}
function renderSysUpTime(value, p, record) {
	var sysUptimeStr = record.data.topCcmtsSysUpTimeString;
    if (sysUptimeStr == "NoValue") {
        return "<img src='/images/canNot.png' class='nm3kTip' nm3kTip='@PERF.mo.notCollected@' />";
    }
    var time = record.data.topCcmtsSysUpTime;
    var cmcDeviceStyle = record.data.cmcDeviceStyle;
    var statusChangeTime = record.data.statusChangeTimeStr;
    var timeString;
    if(record.data.status == 1){
    	  timeString = sysUptimeStr;
    }else{
    	 timeString = I18N.label.deviceLinkDown + "(" + statusChangeTime + ")";
    }
/*     if (EntityType.isCcmtsType(cmcDeviceStyle) && record.data.topCcmtsSysStatus == 4) {
        timeString = arrive_timer_format(time)
    } else if(EntityType.isCmtsType(cmcDeviceStyle) && record.data.status == 1){
    	timeString = arrive_timer_format(time)
    } else {
        timeString = I18N.label.deviceLinkDown + "(" + statusChangeTime + ")";
    }
 */    return timeString;
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
	var se = getSelectedEntity()
	var attention = se.attention
	if(attention){//如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : se.cmcId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cancelFocusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.cancelFocusSucccess@</b>'
	   			});
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.cancelFocusFail@")
			}
		})
	}else{//如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : se.cmcId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.focusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.focusSucccess@</b>'
	   			});
				store.reload()
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.focusFail@")
			}
		})
	}
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
function clearOfflineCm() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.clearOfflineCM.confirmToClear, function(button, text) {
        if (button == "yes") {
            $.ajax({
                url:"/cm/offlineCmAll.tv",
                cache:false,
                type:"post",
                dataType:'json',
                data:{
                    cmcId:record.data.cmcId
                },
                success:function(data) {
                    if (data.success) {
                        //window.parent.showMessageDlg(getI18NString("COMMON.tip"), getI18NString("CCMTS.clearOfflineCM.success"));
                    	top.afterSaveOrDelete({
               				title: '@COMMON.tip@',
               				html: '<b class="orangeTxt">@CCMTS.clearOfflineCM.success@</b>'
               			});
                    } else {
                        window.parent.showMessageDlg(getI18NString("COMMON.tip"), getI18NString("CCMTS.clearOfflineCM.fail"));
                    }
                },
                error:function() {
                    window.parent.showMessageDlg(getI18NString("COMMON.tip"), getI18NString("CCMTS.clearOfflineCM.fail"));
                }
            });
        }
    })
}

function manuRender(v, m, r) {
    var html = "";
    if ( EntityType.isCcmtsType(r.data.cmcDeviceStyle)) {
	    html = "<a href='javascript:;' onClick='showRealTimeInfo(" + r.data.entityId+",\""+ r.data.name+ "\")'>@COMMON.realtimeCMTSInfo@</a> / ";
	    html += "<a href='javascript:;' onClick='showRealTimeCmListInfo()'>@COMMON.realtimeCmInfo@</a> / ";
    }   
    return String.format(html + "<a href='javascript:;' class='withSub'  onClick = 'showMoreOperation({0},event)'>@COMMON.other@</a>", r.id);
}


function showRealTimeCmListInfo(){
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
     window.parent.addView('cm-realTime' + record.data.cmcId, '@COMMON.realtimeCmInfo@', 'entityTabIcon',
     		"/realtimecmlist/showRealtimeCmList.tv?c=" + Math.random() + "&cmcId=" + record.data.cmcId);
}

function showMoreOperation(rid, event) {
    var record = grid.getStore().getById(rid);  // Get the Record
    grid.getSelectionModel().selectRecords([record]);
    var attentionAction ;
    if(record.data.attention)
		attentionAction = '@resources/COMMON.cancelAttention@'
	else
		attentionAction = '@resources/COMMON.attention@'
    if(EntityType.isCmtsType(record.data.cmcDeviceStyle)){
    	cmtsMenu.findById("userAttentionCmts").setText(attentionAction)
    	cmtsMenu.showAt([event.clientX,event.clientY]);
        return;
    }
    if (EntityType.isCcmtsWithoutAgentType(record.data.cmcDeviceStyle)) {
    	ccmts8800AMenu.findById("userAttention").setText(attentionAction)
        ccmts8800AMenu.show([event.clientX,event.clientY], record.data.cmcId);
    } else {
        loadSniUplinkLoopbackStatus(record.data.cmcId);
        if (loopbackStatus == 1) {
            loopbackStatusIcomcls = "enableIconClass";
        } else {
            loopbackStatusIcomcls = "disableIconClass";
        }
        ccmts8800BMenu.findById("loopback").setIconClass(loopbackStatusIcomcls);
        ccmts8800BMenu.findById("userAttentionCC8800B").setText(attentionAction)
        ccmts8800BMenu.show([event.clientX,event.clientY], record.data.cmcId);
    }
}
function renderInterfaceInfo(value, p, record){
	if(record.data.interfaceInfo == "" || record.data.interfaceInfo == null){
		return "--";
	}else{
		return record.data.interfaceInfo;
	}
}
function renderMac(value, p, record){
	if(record.data.topCcmtsSysMacAddr == "" || record.data.topCcmtsSysMacAddr == null){
		return "--";
	}else{
		return record.data.topCcmtsSysMacAddr;
	}
}
function showViewOperation(rid) {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    if(EntityType.isCmtsType(record.data.cmcDeviceStyle)){
    	viewCmtsSnap(record.data.cmcId, record.data.nmName)
    }
    viewCmcSnap(record.data.cmcId, unescape(record.data.nmName), record.data.cmcDeviceStyle);
}

function showRealTimeInfo(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    window.parent.addView('entity-realTime' + record.data.cmcId, '@COMMON.realtimeCMTSInfo@['+record.data.nmName+']', 'entityTabIcon',
            '/cmc/showCmcRealTimeData.tv?cmcId=' + record.data.cmcId);
}

//刷新设备,主要是刷新CC8800B和CMTS
function onMenuRefreshClick(){
	var record = grid.getSelectionModel().getSelected();
	if (record) {
		window.parent.showWaitingDlg("@COMMON.wait@", String.format("@network/NETWORK.refreshingEntity@", record.data.manageIp));
		$.ajax({
			url: '../topology/refreshEntity.tv',type: 'POST', cache:false,
			data:{entityId : record.data.cmcId},
			success:function() {
	 			window.parent.closeWaitingDlg();
	 			refreshClick();
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

function refreshCC8800A(){
	var record = grid.getSelectionModel().getSelected();
    var cmcId = record.data.cmcId;
    var entityId = record.data.entityId;
    var manageIp = record.data.manageIp;
    window.parent.showWaitingDlg(I18N.COMMON.waiting, "@CCMTS.refreshingCcmts@", 'waitingMsg', 'ext-mb-waiting');
    $.ajax({
        url:'cmc/refreshCC8800A.tv',
        type:'POST',
        data: {
        	entityId : entityId,
        	cmcId : cmcId
        },
        dateType:'json',
        success:function(json){
        	window.top.closeWaitingDlg();
        	   top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@CCMTS.refreshCcmtsSuccess@</b>'
   			});
            refreshClick();
        	if(json.typeChange){
       		 	window.parent.showConfirmDlg("@COMMON.tip@", String.format("@CMC.typeChange@", manageIp), function(button, text) {
       		        if (button == "yes") {
       		        	reTopoDevice(entityId, manageIp);
       		        }
       		    });
        	}else if(json.macChange){
        		window.parent.showConfirmDlg("@COMMON.tip@", String.format("@CMC.macChange@", manageIp), function(button, text) {
       		        if (button == "yes") {
       		        	reTopoDevice(entityId, manageIp);
       		        }
       		    });
        	}
            //window.location.reload();
        },
        error:function(){
        	//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCcmtsFail);
        	window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.refreshCcmtsFail@");
        },
        cache:false
    });
}

Ext.onReady(function () {
    Ext.QuickTips.init();
    var w = document.body.clientWidth;
    var h = document.body.clientHeight;
    
    ccmts8800AMenu = new AutoFunMenu({
        id:"ccmts8800AMenu",
        enableScrolling:false,
        minWidth:160,
        shadow:false,
        items:[
			{text:"@COMMON.refresh@",id:"refreshCC8800A",handler : refreshCC8800A,disabled:!refreshDevicePower},
			{text:"@network/NETWORK.reTopo@",id:"reTopoCC8800A",handler : refreshCcmtsOnOLt,disabled:!refreshDevicePower},'-',
            //{text:I18N.CCMTS.ccmtsMessage,id:"ccmts8800ADetailInfo",handler:cmcDetailInfo},
            {text:"@network/BAIDU.viewMap@", handler : addBaiduMap},
            {id:"userAttention",text:'@resources/COMMON.attention@', handler:onAttentionClick},
            '-',
            {text:"@network/NETWORK.tool@", hidden: !operationDevicePower, menu: [
                {text:'Telnet', handler: onTelnetClick}                                
            ]},
            '-',
            {text:I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800APerfManage",handler:ccmtsPerfManage,disabled:!cmcPerfPower},
            {text:I18N.syslog.syslogConfig,id:"cmc8800ASyslog",vcid: "cmc8800ASyslog", handler: ccmtsSyslogManagement},
            {text:I18N.cmc.docsisConfig,id:"cmc8800ADocsis",vcid: "cmc8800ADocsis", handler: show8800ADocsisConfig},
            {
                id:'addCCToTopo',
                text: "@network/COMMON.editFolder@",
                disabled:!topoGraphPower,
                handler:function() {
                    var sm = grid.getSelectionModel();
                    var record = sm.getSelected();
                    var cmcId = record.data.cmcId;
                    var ccMac = record.data.topCcmtsSysMacAddr;
                    var typeId = record.data.cmcDeviceStyle;
                    window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+cmcId, null, true, true);
                    //window.top.createDialog('moveToTopo', I18N.NETWORK.addToTopo, 800, 500, '/cmc/moveToTopoJsp.tv?cmcId=' + cmcId + '&typeId=' + typeId + '&cmcName=' + ccMac, null, true, true);
                }
            },
            /* {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:!googleMapPower}, */'-',
            {text:I18N.COMMON.deviceInfo, id:'renameCmc8800A',handler:modifyDevice},
            {text:I18N.CCMTS.noMacbind,id:"noMacBind8800A",handler:cmcNoMacBind,disabled:!operationDevicePower},
            {text:getI18NString("CCMTS.clearOfflineCM.menu"),id:"clearOfflineCm", vcid:"clearOfflineCm", handler:clearOfflineCm,disabled:!operationDevicePower},
            {text:I18N.CCMTS.reset,id:"reset8800A",handler: cmcReset,disabled:!operationDevicePower},
            {text:I18N.CCMTS.replace.replace,id:"replace8800A",vcid:"replace8800A",handler: cmcReplaceII,disabled:!operationDevicePower}
        ]});
    ccmts8800BMenu = new AutoFunMenu({
        id:"ccmts8800BMenu",
        enableScrolling:false,
        minWidth:160,
        shadow:false,
        items:[
           	{text:"@COMMON.refresh@",id:"refreshCC8800B",handler:onMenuRefreshClick,disabled:!refreshDevicePower},
			{text:"@network/NETWORK.reTopo@",id:"reTopoCC8800B",handler:onDiscoveryAgainClick,disabled:!refreshDevicePower},'-',
            //{text:I18N.CCMTS.ccmtsMessage,id:"ccmts8800BDetailInfo",handler:cmcDetailInfo},
            {text:"@network/BAIDU.viewMap@", handler : addBaiduMap},
            {id:"userAttentionCC8800B",text:'@resources/COMMON.attention@', handler:onAttentionClick},
            '-',
            {text:"@network/NETWORK.tool@", menu: [
                {text:"@network/NETWORK.ping@", handler: onPingClick}, 
                {text:'Telnet', handler: onTelnetClick, hidden: !operationDevicePower},
                {text:"@network/NETWORK.tracert@", handler: onTracertClick},
                {text : "MIB Browser",handler : onMibbleBrowserClick}
            ]},
            '-',
            {text:I18N.CCMTS.ccmtsPerformCollectConfig,id:"ccmts8800BPerfManage",handler:ccmtsPerfManage,disabled:!cmcPerfPower},
            {text: I18N.cmcSni.cpuPortRateLimit,id:"cpuRateLimit",vcid:"cpuRateLimit",handler: cpuRateLimit},
            {
                id:"uplink",hidden:true,
                text:I18N.cmcSni.sniConfig,
                menu:[
                    {text: I18N.cmcSni.sniRateLimit,id:"rateLimit", vcid:"rateLimit", handler:sniRateLimit},
                    {text: I18N.cmcSni.loopbackEnable,id:"loopback", vcid:"loopback", handler: cmcLoopbackStatusEnable, iconCls:loopbackStatusIcomcls,disabled:!operationDevicePower},
                    {text: I18N.cmcSni.phyConfig,id:"phyConfig", vcid:"phyConfig",handler: cmcPhyConfigFor4c}
                ]
            },
            {text: I18N.CMC.text.StormSuppression,id:"stromLimit", vcid:"stromLimit",handler: stromLimit},
            {text:I18N.CCMTS.vlanConfig,id:"vlan", vcid:"vlan", handler: ccmtsVlanConfig},
            {text:I18N.CCMTS.vlanSubConfig,id:"subVlan", vcid:"subVlan", handler:ccmtsIpSubVlan},
            {text:I18N.syslog.syslogConfig,id:"syslog", vcid:"syslog", handler: ccmtsSyslogManagement},
            {text:I18N.cmc.docsisConfig,id:"docsisConfig", vcid:"docsisConfig", handler: show8800BDocsisConfig},
            {
                id:'add8800BToTopo',
                text: "@network/COMMON.editFolder@",
                disabled:!topoGraphPower,
                handler:function() {
                    var sm = grid.getSelectionModel();
                    var record = sm.getSelected();
                    var cmcId = record.data.cmcId;
                    var ccMac = record.data.topCcmtsSysMacAddr;
                    var typeId = record.data.cmcDeviceStyle;
                    window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+cmcId, null, true, true);
                    //window.top.createDialog('moveToTopo', I18N.NETWORK.addToTopo, 800, 500, '/cmc/moveToTopoJsp.tv?cmcId=' + cmcId + '&typeId=' + typeId + '&cmcName=' + ccMac, null, true, true);
                }
            },
            /* {text:I18N.COMMON.addToGoogle,handler:onAddToGoogleClick,disabled:!googleMapPower}, */'-',
            {text:I18N.COMMON.deviceInfo, id:'renameCmc8800B',handler: modifyDevice},
            {text:I18N.CCMTS.reset,id:"reset8800B",handler: cmcReset,disabled:!operationDevicePower},
            {text:I18N.CCMTS.replace.replace,id:"replace8800B", vcid:"replace8800B", handler: cmcReplaceI,disabled:!operationDevicePower}
        ]});
    
    cmtsMenu = new Ext.menu.Menu({
        id:"cmtsmenu",
        enableScrolling:false,
        minWidth:160,
        shadow:false,
        items:[
			{text:"@COMMON.refresh@",id:"refreshCmts",handler:onMenuRefreshClick,disabled:!refreshDevicePower},
			{text:"@network/NETWORK.reTopo@",id:"reTopoCmts",handler:onDiscoveryAgainClick,disabled:!refreshDevicePower},'-',
			{text:"@network/BAIDU.viewMap@", handler : addBaiduMap},
			{id:"userAttentionCmts",text:'@resources/COMMON.attention@', handler:onAttentionClick},
            {text:I18N.CCMTS.ccmtsPerformCollectConfig,id:"cmtsPerfManage",handler:ccmtsPerfManage,disabled:!cmcPerfPower},
            {
                id:'addCmtsToTopo',
                text:'@network/COMMON.editFolder@',
                disabled:!topoGraphPower,
                handler:function() {
                    var sm = grid.getSelectionModel();
                    var record = sm.getSelected();
                    var cmcId = record.data.cmcId;
                    var ccMac = record.data.topCcmtsSysMacAddr;
                    var typeId = record.data.cmcDeviceStyle;
                    window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+cmcId, null, true, true);
                    //window.top.createDialog('moveToTopo', I18N.NETWORK.addToTopo, 800, 500, '/cmc/moveToTopoJsp.tv?cmcId=' + cmcId + '&typeId=' + typeId + '&cmcName=' + ccMac, null, true, true);
                }
            },
            /* {text:I18N.COMMON.addToGoogle,handler:onAddToGoogleClick,disabled:!googleMapPower}, */'-',
            {text:I18N.COMMON.deviceInfo, id:'renameCmts',handler: modifyDevice}
        ]});
	
    
    
    function opeartionRender(value, p, record) {
    	if(!record.data.attention){
    		return String.format('<a href="#" onclick=\'viewCmcSnap("' + record.data.cmcId + '","' + escape(record.data.nmName) + '","' + record.data.cmcDeviceStyle + '");\'>{0}</a>',
    	                value);
    	}else{
    		return String.format('<img src="/images/att.png"/>&nbsp;&nbsp;<a href="#" onclick=\'viewCmcSnap("' + record.data.cmcId + '","' + escape(record.data.nmName) + '","' + record.data.cmcDeviceStyle + '");\'>{0}</a>',
	                value);
    		//return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+record.data.name+'\');">'+value+'</a>&nbsp;&nbsp;<img src="/images/att.png"/>';
    		/*return  String.format('<a href="#" onclick="showEntitySnap({0}, \'{1}\')">{2}</a>' + '&nbsp;&nbsp;<img src="/images/att.png"/>',
    				record.data.entityId, record.data.name, value);*/
    	}
        /* return String.format('<a href="#" onclick=\'viewCmcSnap("' + record.data.cmcId + '","' + escape(record.data.nmName) + '","' + record.data.cmcDeviceStyle + '");\'>{0}</a>',
                value); */
    }
    var columns = null;
	//if(hasSupportEpon == 'true')
    //删除所有根据模块判断的不同显示，统一成一种。 modify by lzs at 2013.3.26
    columns = [
        {id:'entityName',header: '<div class="txtCenter">@COMMON.alias@</div>', sortable:true, align : 'left', dataIndex: 'nmName',renderer:opeartionRender},
        {header: '<div class="txtCenter">@CCMTS.uplinkdevice@</div>' , width: 200, sortable:true, align : 'left', dataIndex: 'manageIp', renderer:function(value, p, record) {
            var disValue = value;
            if (EntityType.isCcmtsWithoutAgentType(record.data.cmcDeviceStyle)) {
                disValue = record.data.manageName +  "("+disValue+")";
            }
            if(disValue == ""){
            	return "--";
            }
            return '<a href="#" class=my-link onclick=\'viewOltSnap("' + record.data.cmcId + '","' + escape(record.data.nmName)
                    + '","' + record.data.entityId + '","' + value + '","' + record.data.cmcDeviceStyle + '","' + escape(record.data.manageName) + '");\'>' + disValue + '</a>';
        }},
        {header: '@CCMTS.macAddress@', width: 120, sortable:true,align: 'center', dataIndex: 'topCcmtsSysMacAddr', renderer: renderMac},
        {header: '<div class="txtCenter">@COMMON.name@</div>', width: 110, sortable:true,align: 'left', dataIndex: 'topCcmtsSysName'},
        {header: '<div class="txtCenter">@COMMON.type@</div>', width: 110, sortable:true,align: 'left', dataIndex: 'typeName'},
        {header: '@CCMTS.Docsis3.0@', width:100 , sortable:true, align : 'center', dataIndex: 'mddInterval',renderer: renderMddInterval},
        {header: I18N.CMC.label.status, width:50 , sortable:true, align : 'center', dataIndex: 'topCcmtsSysStatus',renderer: renderSysStatus},//topCcmtsSysStatusString
        {header: '<div class="txtCenter">@CCMTS.sysUpTime@</div>', width:150 , sortable:true,align: 'left', dataIndex: 'topCcmtsSysUpTime',renderer: renderSysUpTime},
        {header: I18N.CCMTS.slotLocation, width: 60, sortable:true, align : 'center', dataIndex: 'interfaceInfo', renderer: renderInterfaceInfo},
//      {header: '<div class="txtCenter">@CCMTS.entityLocation@</div>', width:140, sortable:true,align: 'left', dataIndex: 'topCcmtsSysLocation',renderer:addCellTooltip},
        {header: '<div class="txtCenter">@CCMTS.softVersion@</div>', width:150 , sortable:true,align : 'left', dataIndex: 'topCcmtsSysSwVersion',renderer:addCellTooltip},
	    {header:'<div class="txtCenter">@CCMTS.contact@</div>',dataIndex:'contact',align:'left',sortable:true,hidden:true,renderer:addCellTooltip},
	    {header:'<div class="txtCenter">@CCMTS.location@</div>',dataIndex:'location',align:'left',sortable:true,hidden:true,renderer:addCellTooltip},
	    {header:'<div class="txtCenter">@CCMTS.note@</div>',dataIndex:'note',align:'left',width: 110,sortable:true,hidden:true,renderer:addCellTooltip},
        {header: "@COMMON.manu@", width:300, renderer : manuRender,fixed:true}
    ];


    store = new Ext.data.JsonStore({
        url: 'queryCmcList.tv',
        root: 'data',
        idProperty: "cmcId",
        totalProperty: 'rowCount',
        remoteSort: true,
        fields: ['cmcId', 'nmName','cmcDeviceStyle', 'cmcDeviceStyleString', 'topCcmtsSysDescr', 'topCcmtsSysMacAddr', 'topCcmtsSysLocation', 'topCcmtsSysContact',
            'topCcmtsSysRAMRatiotoString','topCcmtsSysUpTimeString','topCcmtsSysCPURatiotoString', 'topCcmtsSysFlashRatiotoString', 'topCcmtsSysStatus',
            'topCcmtsSysSwVersion', 'manageIp','interfaceInfo', 'entityId', 'ipAddress','topCcmtsSysUpTime','dt','oltVersion', 'manageName',
            'statusChangeTimeStr','statusChangeTimeLong', 'status','topCcmtsSysName','attention', 'typeId', 'typeName','mddInterval','contact','location','note']
        //排序规则
    });

    //var cm = new Ext.grid.ColumnModel(columns);
    var cmConfig = CustomColumnModel.init("cmclist",columns,{}),
        cm = cmConfig.cm,
        sortInfo = cmConfig.sortInfo;
        
    //store.setDefaultSort(sortInfo.field, sortInfo.direction);
        
    var toolbar = null;
    if (hasSupportEpon == 'true') {
        toolbar = [
            buildEntityInput(),//'-',
            //buildConnectPersonInput(),//'-',
            {xtype: 'tbspacer', width: 3},
            buildStatusInput(),
            {xtype: 'tbspacer', width: 3},
            buildDeviceTypeSelect(),//'-',
            {xtype: 'tbspacer', width: 3},
            buildOltSelect(),
            {xtype: 'tbspacer', width: 3},
            buildPonSelect(),'-',
            {xtype: 'tbspacer', width: 3},
            buildDocsis30Select(),'-',
            //buildIpInput(),//'-',
            //buildMacInput(),'-',
            {text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: queryClick}
        ];
    } else {
        toolbar = [
            buildEntityInput(),
            {xtype: 'tbspacer', width: 3},
			buildDeviceTypeSelect(),
			{xtype: 'tbspacer', width: 3},
            buildStatusInput(),'-',
            {xtype: 'tbspacer', width: 3},
            buildDocsis30Select(),'-',
            //buildConnectPersonInput(),'-',
            //buildIpInput(),'-',
            //buildMacInput(),'-',
            {text: I18N.COMMON.query, iconCls: 'bmenu_find', handler: query8800BClick}
        ];
    }

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
            //	alert(para)
            //},   
            phpMode: true
        }
        ]
    });   */

    /* 	var w = document.body.clientWidth;
     var h = document.body.clientHeight; */
    grid = new Ext.grid.GridPanel({
    	//plugins: [filters],
        stripeRows:true,
        region: "center",
        bodyCssClass: 'normalTable',
        id: 'extGridContainer',
        animCollapse: animCollapse,
        trackMouseOver:true,
        border: false,
        store: store,
        tbar: new Ext.Toolbar({
            cls:'ccmtsListToolBar',items:toolbar
        }),
        enableColumnMove : true,
        listeners : {
        	columnresize: function(){
   	    	    CustomColumnModel.saveCustom('cmclist', cm.columns);
   	        },
   	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo('cmclist', sortInfo);
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
        items: [grid]
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
    
    if (hasSupportEpon == 'true') {
	    selector = new NetworkNodeSelector({
			id : 'oltSelect',
			renderTo : "oltselectCont",
			listeners:{
				selectChange: oltSelectChange
			}
		});
    }
});

function onTelnetClick() {
    var entity = getSelectedEntity();
    if (entity) {
        Ext.menu.MenuMgr.hideAll();
        showTelnetClient(entity.cmcId, entity.manageIp);
    }   
}
function onPingClick() {
    var entity = getSelectedEntity();
    if (entity) {
        showPingWindow(entity.manageIp);
    }
}
function onTracertClick() {
    var entity = getSelectedEntity();
    if (entity) {
        showTracetWindow(entity.manageIp);
    }
}

function onMibbleBrowserClick(){
    var entity = getSelectedEntity();
    if(entity){
        window.top.addView("mibbleBrowser","MIB Browser",'entityTabIcon',"/mibble/showMibbleBrowser.tv?host="+entity.manageIp,null,true)
    }else{
        window.top.addView("mibbleBrowser","MIB Browser",'entityTabIcon',"/mibble/showMibbleBrowser.tv",null,true)
    }
}

String.prototype.trim=function() {  
    return this.replace(/(^\s*)|(\s*$)/g,'');  
}; 
/**
 * 支持EPON模块查询
 */
function queryClick() {
    //获取各项的值
    var deviceType = Zeta$('deviceType').value;
    var entityId = Zeta$('oltSelect').value;
    var ponId = Zeta$('ponSelect').value;
    //var cmcMac = Zeta$('cmcMacAddress').value;
    var nmName = Zeta$('name').value.trim();
    //var connectPerson = null;//Zeta$('connectPerson').value;
    var cmcType = null;
    //var cmcIp = Zeta$('ipAddress').value;
    var cmcStatus = $('#status_sel').val();
    var mddInterval = $('#mddInterval').val();
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
		cmcName: nmName,
		deviceType:deviceType,
		entityId: entityId, 
		ponId: ponId,
		topCcmtsSysStatus: cmcStatus,
		mddInterval : mddInterval
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
function cpuRateLimit() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('cpuRateLimit', I18N.cmcSni.cpuPortRateLimit, 800, 500,
            '/cmc/sni/showCpuRateLimit.tv?cmcId=' + cmcId, null, true, true);
}
function sniRateLimit() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('sniRateLimit', I18N.cmcSni.sniRateLimit, 600, 370,
            '/cmc/sni/showSniRateLimit.tv?cmcId=' + cmcId, null, true, true);
}
function stromLimit() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('stromLimit', I18N.CMC.text.StormSuppression, 800, 500,
            '/cmc/sni/showStormLimitConfig.tv?cmcId=' + cmcId, null, true, true);
}
function cmcLoopbackStatusEnable() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    loopbackStatusTemp = loopbackStatus == 1 ? 2 : 1;
    var action = loopbackStatus == 1 ? I18N.CMC.button.close : I18N.CMC.select.open;
    window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, String.format(I18N.cmcSni.setLoopbackEnable, action), function(type) {
        if (type == 'no') {
            return;
        }
        $.ajax({
            url:'/cmc/sni/modifySniLoopbackStatus.tv?cmcId=' + cmcId + '&loopbackStatus=' + loopbackStatusTemp,
            type:'POST',
            dateType:'text',
            success:function(response) {
                if (response == "success") {
                    //window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableSuccess);
                	top.afterSaveOrDelete({
           				title: '@COMMON.tip@',
           				html: '<b class="orangeTxt">' +  action + I18N.cmcSni.setLoopbackEnableSuccess + '</b>'
           			});
                } else {
                    window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableFail);
                }
            },
            error:function() {
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, action + I18N.cmcSni.setLoopbackEnableFail);
            },
            cache:false
        });
    });
}

//phy信息配置;
function cmcPhyConfigFor4c() {
    var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var cmcId = record.data.cmcId;
    window.top.createDialog('cmcPhyConfigFor4c', I18N.cmcSni.phyConfig, 600, 370,
            '/cmc/sni/showPhyConfigFor4c.tv?cmcId=' + cmcId, null, true, true);
}
</script>
</head>
<body class=CONTENT_WND>
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