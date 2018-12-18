<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
    import js/customColumnModel
    import js.json2
    import tools/cmdUtil
    import network.entityAction
    import network.entitySnapList
    import cmc.js.extendMenu
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
</style>
<script type="text/javascript">
$(function(){
	/******************************* OLT *************************************/
	//设备重启, 更新指定行;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		setEntityState(data, false);
	});
	//设备启动，更新指定行;
	var olt_start = top.PubSub.on(top.OltTrapTypeId.OLT_START, function(data){
		setEntityState(data, true);
	});
	/******************************* CMTS ************************************/
	//系统手动重启，更新指定行;
	var dol_reboot = top.PubSub.on(top.CmcTrapTypeId.DOL_REBOOT, function(data){
		if(top.isClearAlert(data)){
            setEntityState(data, true);
        }else{
            setEntityState(data, false);
        }
	});
	//CMTS断电，更新指定行;
	var cmts_power_off = top.PubSub.on(top.CmcTrapTypeId.CMTS_POWER_OFF, function(data){
		if(top.isClearAlert(data)){
            setEntityState(data, true);
        }else{
            setEntityState(data, false);
        }
	});
	//CMTS断纤，更新指定行;
	var cmts_link_lose = top.PubSub.on(top.CmcTrapTypeId.CMTS_LINK_LOSE, function(data){
		if(top.isClearAlert(data)){
            setEntityState(data, true);
        }else{
            setEntityState(data, false);
        }
	});
	//CMC复位，更新指定行;
	var cmc_reset = top.PubSub.on(top.CmcTrapTypeId.CMC_RESET, function(data){
		if(top.isClearAlert(data)){
			setEntityState(data, true);
        }else{
        	setEntityState(data, false);
        }
	});
	//CMTS下线，更新指定行;
    var cmc_offline = top.PubSub.on(top.CmcTrapTypeId.CMTS_OFFLINE, function(data){
    	if(top.isClearAlert(data)){
            setEntityState(data, true);
        }else{
            setEntityState(data, false);
        }
    });
	
	window.onunload = function(){
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);	 
		top.PubSub.off(top.OltTrapTypeId.OLT_START, olt_start);
		
		top.PubSub.off(top.CmcTrapTypeId.DOL_REBOOT, dol_reboot);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_POWER_OFF, cmts_power_off);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_LINK_LOSE, cmts_link_lose);
		top.PubSub.off(top.CmcTrapTypeId.CMC_RESET, cmc_reset);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_OFFLINE, cmc_offline);
	};
});

function setEntityState(data, state) {
	if(store) {
		var ip = data.ip || data.host;
		// 根据ip找到对应的行，置为下线
		for (var i = 0, len=store.getCount(); i < len; i++) {
			if (store.getAt(i).get('entityId') === data.entityId) {
				store.getAt(i).set('state', state);
				store.getAt(i).commit();
				break;
			}
		}
	}
}

// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
	startTimer();
}
function tabDeactivate() {
    try {
        doOnunload();
    } catch(ex) {
    }
}
function tabRemoved() {
    try {
        doOnunload();
    } catch(ex) {
    }
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
	startTimer();
}
// for tab changed end
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var pageSize = <%= uc.getPageSize() %>;
var nmProject = <%= uc.hasSupportProject("nm")%>;
//内蒙要求资源列表pagesize为1000
if(nmProject){
	pageSize = 1000;
}
var dispatcherInterval = <%= SystemConstants.getInstance().getLongParam("entitySnap.refresh.interval", 60000L) %>;
var dispatcherTimer = null;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var cmcPerfPower = <%=uc.hasPower("cmcPerfParamConfig")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
var showCartoon = '${showCartoon}' == 'false' ? false : true;//如果是false,那么没有设备的时候会出现提示信息;
var showRefreshTip = '${showRefreshTip}';
</script>
</head>
<body class=whiteToBlack onunload="doOnUnload();">
</Zeta:HTML>
