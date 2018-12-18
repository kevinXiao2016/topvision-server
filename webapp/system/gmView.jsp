<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ page import="com.topvision.ems.mobile.MobileEMSIIIOSVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileEMSIIAndroidVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileMUIIOSVersion"%>
<%@ page import="com.topvision.ems.mobile.MobileMUIAndroidVersion"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
	IMPORT js/qrcode/qrcode
</Zeta:Loader>
<head>
<style type="text/css">
body,html{width:100%; height:100%; overflow:hidden;}
.container{ width:100%; height:100%; overflow:auto;}
.gmContainer {padding: 20px;}
.gmContainer div {float: left;}
.gm-icon { overflow:hidden;}
#bg{ background:#fff; position:absolute; top:0; left:0; overflow:hidden; width:100%; height:100%; z-index:2; opacity:0.9; filter:alpha(opacity=90); display:none;} 
#openLayer{ width:800px; height:314px; overflow:hidden;position:absolute; top:0px; left:0px; display:none; z-index:3;}
#androidqrcode table, #iosqrcode table{ margin:0px 0px 0px 0px !important;}

</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jqueryEasing.js"></script>
<script type="text/javascript">
var language = '@app.logo.type@'; //en 或者 zh;
var cmcSupport = <%= uc.hasSupportModule("cmc")%>;
var androidVersion = '<%=MobileEMSIIAndroidVersion.version%>';
var iosVersion = '<%=MobileEMSIIIOSVersion.version%>';
var iosUrl = 'http://ems.top-vision.cn:8110/download/download_app_V2.jsp?version=V'+iosVersion+'&platform=ios&language='+ language;
var androidUrl = 'http://ems.top-vision.cn:8110/download/download_app_V2.jsp?version=V'+androidVersion+'&platform=android&language='+ language;

var terminalAndroidVersion = '<%=MobileMUIAndroidVersion.version%>';
var terminalIosVersion = '<%=MobileMUIIOSVersion.version%>';
var terminalAndroidUrl = 'http://ems.top-vision.cn:8110/terminal/android/V'+ terminalAndroidVersion +'/terminal.apk';
var terminalIosUrl = 'http://ems.top-vision.cn:8110/terminal/ios/V'+ terminalIosVersion +'/terminal.html'
$(function(){
	$(".downloadMain").click(function(event){
		event.stopPropagation();
	});
	$("#appDownload, #terminalDownload").click(function(){
		$(this).fadeOut();
	});
	 window.onload =function(){
        var iosqrcode = new QRCode(document.getElementById("iosqrcode"), {
            width : 200,//设置宽高
            height : 200
        });
        iosqrcode.makeCode(iosUrl);
        $("#iosA").attr("href",iosUrl);
        var androidqrcode = new QRCode(document.getElementById("androidqrcode"), {
            width : 200,//设置宽高
            height : 200
        });
        androidqrcode.makeCode(androidUrl);
        $("#androidA").attr("href",androidUrl);
        
        var terminalIos = new QRCode(document.getElementById("terminalIos"), {
            width : 200,//设置宽高
            height : 200
        });
        terminalIos.makeCode(terminalIosUrl);
        $("#terminalIosLink").attr("href",terminalIosUrl);
        var terminalAndroid = new QRCode(document.getElementById("terminalAndroid"), {
            width : 200,//设置宽高
            height : 200
        });
        terminalAndroid.makeCode(terminalAndroidUrl);
        $("#terminalAndroidLink").attr("href",terminalAndroidUrl);
    }
});
function showDownload(){
	$("#appDownload").fadeIn();
	$(".downloadMain").css({display:'block'});
}
function showTerminalDownload(){
    $("#terminalDownload").fadeIn();
    $(".downloadMain").css({display:'block'});
}
function statusClear() {
	window.top.setStatusBarInfo('', '');
}
function personalSettingClick() {
	window.top.showPersonalize();
}
function modifyPersonalInfo() {
    window.top.modifyPersonalInfoClick();
}
function setPasswdClick() {
	window.top.createDialog('modalDlg', I18N.MAIN.setPasswd, 600, 370, 'system/popModifyPasswd.tv', null, true, true);
}
function naviSettingClick() {
	window.top.onNaviBarOptionClick();
}
function customDesktopClick() {
	window.top.createDialog('modalDlg', I18N.MAIN.customMyDesktop, 800, 500, 'portal/showPopPortletItems.tv', null, true, true);
}

function emailServerClick() {
    window.top.createDialog('modalDlg', I18N.SYSTEM.mailServer, 800, 500, 'param/showEmailServer.tv', null, true, true);
}
function smsServerClick() {
    window.top.createDialog('modalDlg', I18N.SYSTEM.smsServer, 600, 370, 'param/showSmsServer.tv', null, true, true);
}
function setLanguageClick() {
	window.top.createDialog('modalDlg', I18N.SYSTEM.languageConfig, 600, 370, 'include/showLanguageConfig.tv', null, true, true);
}

function databaseManagerClick() {
	window.top.createDialog('modalDlg', I18N.SYSTEM.databaseMaintenance, 600, 370, 'system/showDatabaseParam.tv', null, true, true);
}

function setGoogleKeyClick() {
	window.top.createDialog('modalDlg', 'Google Map Key', 500, 300, 'google/showPopGoogleMapKey.tv', null, true, true);
}

function viewLicenseClick() {
	window.top.onLicenseClick();
}

function viewAboutClick() {
	window.top.onAboutClick();
}

function viewRuntimeInfoClick() {
	window.top.onShowRuntimeClick();
}

function showFaultAction() {
    window.parent.addView("actionList", I18N.SYSTEM.alertAction, "actionTabIcon", "fault/actionList.jsp");
}

function onSecurityParamClick() {
	window.top.createDialog('modalDlg', I18N.SYSTEM.safetyManagementParameters, 600, 400, 'security/showSecurityParams.tv', null, true, true);
}

function onSecurityAccessClick() {
	window.top.createDialog('modalDlg', I18N.SYSTEM.systemAccessStrategy, 600, 370, 'param/showSecurityCenter.tv', null, true, true);
}

function alertSettingClick() {
	window.top.createDialog('modalDlgAlertSet', I18N.FAULT.alertConfig, 800, 500, 'fault/alertSetting.tv', null, true, true);
}
function onMibBrowseClick() {
	window.top.addView('mibbleBrowser', 'MIB Browser','entityTabIcon', '/mibble/showMibbleBrowser.tv',null,true);
}
function ftpServerManageClick() {
	window.top.createDialog('ftpServerManage', I18N.ftp.ftpServer, 800, 500, 'system/showFtpServer.tv', null, true, true);
}
function ftpConnectManageClick(){
	window.top.createDialog('ftpConnectManage', I18N.ftp.ftpConnect, 800, 500, 'system/showFtpConnect.tv', null, true, true);
}
function tftpServerManageClick() {
	window.top.createDialog('tftpServerManage', I18N.ftp.tftpServer, 800, 500, 'system/showTftpServer.tv', null, true, true);
}
function tftpClientManageClick(){
	window.top.createDialog('tftpClientManage', '@resources/ftp.tftpClient@', 800, 500, 'system/showTftpClient.tv', null, true, true);
}
function telnetClientManageClick() {
	window.top.createDialog('telnetClientMgt', '@network/TelnetClient.telnetClientMgt@', 600, 370, 'system/telnet/showTelnetClientMgt.tv', null, true, true);
}
function telnetClientClick() {
	window.parent.addView("telnetClient-tab", "@network/TelnetClient.innerClient@", "icoG9", "/system/telnet/showTelnetClient.tv");
}
function telnetClientRecordClick() {
	window.parent.addView("telnetClientRecord-tab", "@network/TelnetClient.innerTypeRecord@", "icoG9", "/system/telnet/showTelnetRecord.tv");
}
function engineServerManageClick() {
	window.parent.addView("engineServerManage", "@platform/sys.engineServerConfig@", "icoA7", "system/engineServerManagement.jsp");
}

function viewConnectConfigClick() {
	window.top.createDialog('modalDlg', "@platform/sys.connectConfig@", 600, 370, '/connectivity/showConnectivityConfig.tv', null, true, true);
}

//ping config
function viewPingConfigClick(){
	window.top.createDialog('modalDlg', "@platform/sys.pingConfig@", 600, 370, 'system/showPingConfig.tv', null, true, true);
}
// snmp config
function viewSnmpConfigClick(){
	window.top.createDialog('modalDlg', "@platform/sys.snmpConfig@", 600, 370, '/network/showSnmpConfig.tv', null, true, true);
}

function viewTcpConnectConfigClick() {
	window.top.createDialog('modalDlg', "@platform/sys.tcpconnectConfig@", 600, 370, '/connectivity/showTcpConnectivityConfig.tv', null, true, true);
}

//trapListenports config
function viewTrapListenportsClick(){
	window.top.createDialog('modalDlg',"@platform/sys.trapConfig@", 600, 370, 'system/showTrapListenports.tv', null, true, true);
}

//syslogListenports config
function viewSyslogListenportsClick(){
	window.top.createDialog('modalDlg', "@platform/sys.syslogConfig@", 600, 370, 'system/showSyslogListenports.tv', null, true, true);
}

//配置文件自动备份设置
function viewFileAutoWriteClick(){
	window.top.createDialog('modalDlg', "@platform/sys.fileAutoWrite@", 600, 370, '/configBackup/showAutoWriteConfig.tv', null, true, true);
}

//发布一个公告;
function sendMessage(){
	window.top.createDialog('modalDlg', "@sys.announcement@", 600, 370, 'system/sendMessage.jsp', null, true, true);
}

//配置CM采集方式
function remoteQueryCmClick(){	
	window.top.createDialog('modalDlg', "@sys.cmCollectModeConfig@", 600, 370, '/remotequerycm/showCmRemoteQueryConfig.tv', null, true, true);
}

//
function defaultPowerLevelConfig(){	
	window.top.createDialog('defaultPowerLevelConfigDlg', "@sys.defaultPowerLevelConfig@", 600, 370, '/mobile/showDefaultPowerLevelConfig.tv', null, true, true);
}

//配置CM采集方式
function mobileDeviceConfigClick(e){	
	closeBg(e);
	window.top.addView('mobileDeviceConfigDlg', '@sys.mobileDeviceConfig@','entityTabIcon', '/mobile/showMobileDeviceConfig.tv');
}

//配置设备自动刷新
function autoRefreshClick(){
	window.top.createDialog('modalDlg', "@sys.autoRefreshConfig@", 600, 370, '/network/showAutoRefreshConfig.tv', null, true, true);
}

//告警确认全局配置
function alertConfimConfig(){
	window.top.createDialog('modalDlg', "@sys.alertConfirmConfig@", 600, 370, '/fault/showAlertConfirmConfig.tv', null, true, true);	
}


function nbiTrapConfig(){
	window.top.createDialog('modalDlg', "@sys.nbiTrapConfig@", 800, 500, '/fault/showNorthBoundConfig.tv', null, true, true);	
}

//性能北向接口
function nbiPerferformanceConfig(){
	window.top.createDialog('nbiBaseConfig', '@sys.nbiPerformanceConfig@', 800, 500, '/nbi/showNbiBaseConfigView.tv', null, true, true);
}


//自动清除
function autoClearConfig(e){
	closeBg(e);
	window.top.createDialog('modalDlg', "@platform/sys.autoClearConfig@", 600, 370, '/autoclear/showAutoClearConfig.tv', null, true, true);
}

function showAutoClearHistory(e){
	closeBg(e);
	window.top.addView('showClearHistory', '@platform/sys.autoClearHistory@','entityTabIcon', '/autoclear/showAutoClearHistory.tv');
}

function cmcFlapClearConfig(){
	window.top.createDialog('cmcFlapClear', "@sys.clearCmtsFlap@", 600, 370, '/cmc/showCmtsFlapClearConfig.tv', null, true, true);
}

//Onu上下线记录自动采集配置
function onuOnOffLineRecordConfig(){
	window.top.createDialog('onuONOffRecordConfigDlg', "@sys.onOffRecordAutoWrite@", 600, 370, '/onu/onoffrecord/showOnOffRecordConfig.tv', null, true, true);
}

//显示单位显示配置
function showUnitConfig(){
	window.top.createDialog('modalDlg', "@UNIT.unitConfig@", 600, 370, 'param/showUnitConfig.tv', null, true, true);
}
//CM管理页面跳转方式设置;
function showCmJumpMode(){
	window.top.createDialog('modalDlg', "@resources/userPower.cmJump@", 600, 370, '/webproxy/showCmWebJumpConfig.tv', null, true, true);
}

function overFn(_this){
	$(_this).attr("class","gm-icon-over");
}
function outFn(_this){
	$(_this).attr("class","gm-icon");
}

//打开多个菜单;
function openTheLayer(){
	var arr = [
               {imgSrc:"../images/system/gvMail.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:0},
               {imgSrc:"../images/system/gvMail.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:1},
               {imgSrc:"../images/system/gvMail.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:2},
               {imgSrc:"../images/system/gvMail.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:3},
               {imgSrc:"../images/system/gvBroad.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:4},
               {imgSrc:"../images/system/gvMail.png",title:"@resources/SYSTEM.mailServer@", text:"@resources/SYSTEM.setSystemAccessStrategy@",clickHand:"sendMessage()",num:5}
               ];
	addOpenIcon(arr);
}

function openTheLayer2(){
	var arr = [
               {imgSrc:"../images/system/gvClear.png",title:"@sys.autoClearConfig@", text:"@sys.autoClearConfig@",clickHand:"autoClearConfig(event)",num:0},
               {imgSrc:"../images/system/gvClear.png",title:"@sys.autoClearHistoryRecord@", text:"@sys.autoClearHistoryRecord@",clickHand:"showAutoClearHistory(event)",num:2}
               ];
	addOpenIcon(arr);
}

function openTheLayer3(){
	var arr = [
               {imgSrc:"../images/system/gvPhoneConfig.png",title:"@sys.defaultPowerLevelConfig@", text:"@sys.defaultPowerLevelConfig@",clickHand:"defaultPowerLevelConfig(event)",num:0},
               {imgSrc:"../images/system/gvPhoneConfig.png",title:"@sys.mobileDeviceConfig@", text:"@sys.mobileDeviceConfig@",clickHand:"mobileDeviceConfigClick(event)",num:1},
               {imgSrc:"../images/system/gvPhoneConfig.png",title:"@COMMON.appSetUp@", text:"@COMMON.appSetUp@",clickHand:"showDownload()",num:2}
               ];
	addOpenIcon(arr);
}

function openTelnetLayer() {
	var arr = [
        {imgSrc:"/images/system/gvTelnet.png", title:"@network/TelnetClient.telnetClientMgt@", text:"@network/TelnetClient.telnetClientCfg@", clickHand:"telnetClientManageClick(event)", num:0},
        {imgSrc:"/images/system/gvTelnet.png", title:"@network/TelnetClient.innerClient@", text:"@network/TelnetClient.innerClient@", clickHand:"telnetClientClick(event)", num:1},
        {imgSrc:"/images/system/gvTelnet.png", title:"@network/TelnetClient.innerTypeRecord@", text:"@network/TelnetClient.innerTypeRecord@", clickHand:"telnetClientRecordClick()", num:2}
    ];
    addOpenIcon(arr);
}

function closeBg(e){
	if(!e){e=window.event;}
	if (e.stopPropagation){ 
		e.stopPropagation();
	} else {
		e.cancelBubble = true;
	}
	$(window).unbind("resize",resizeWin);
	$("#openLayer").empty();
	$("#openLayer, #bg").css("display","none");
	endMoveNum = 0;
}
//点击返回按钮;
function getBackFn(){
	var numChildren = $("#openLayer").children().length;
	$("#openLayer").children().each(function(){
		$(this).animate({left:275, top:127},function(){
			endMoveMent(numChildren);
		});
	})
	
}
var endMoveNum = 0;
function endMoveMent(num){
	endMoveNum++;
	if(endMoveNum == num){
		$("#openLayer").empty();
		$("#openLayer").css("display","none");
		$("#bg").fadeOut("slow");
		endMoveNum = 0;
		$(window).unbind("resize",resizeWin);
	}
}
//添加弹出的图标;
function addOpenIcon(paraArr){
	$("#bg, #openLayer").css("display","block");
	$("#bg").css({opacity:0.9});
	var $openLayer =  $("#openLayer");
	var w = $(window).width();
	var h = $(window).height();
	
	var w2 = (w - $openLayer.outerWidth()) /2;
	var h2 = (h - $openLayer.outerHeight()) / 2;
	if(w2 < 0){ w2 = 0;}
	if(h2 < 0){ h2 = 0;}
	$openLayer.css({left:w2, top:h2});
	
	var str = setString(paraArr);
	$openLayer.append(str)
	$("#openLayer .gm-icon-over").each(function(){
		var num = $(this).attr("alt");
		var t = Math.floor(num / 3) + Math.floor(num / 3) * 82;
		var x = num - (Math.floor(num / 3) * 3)
		var l = x + x * 275;
		
			$(this).animate({left:l,top:t},{easing:"easeOutBounce",duration:800,complete:function(){
								$(this).attr("class","gm-icon");
							}
			});
			
			/* $(this).animate({left:l,top:t},function(){
			$(this).attr("class","gm-icon");
		}) */
	});
	$(window).bind("resize",resizeWin);
}
function resizeWin(){
	var $openLayer =  $("#openLayer");
	var w = $("#bg").width();
	var h = $("#bg").height();
	var w2 = (w - $openLayer.outerWidth()) /2;
	var h2 = (h - $openLayer.outerHeight()) / 2;
	if(w2 < 0){ w2 = 0;}
	if(h2 < 0){ h2 = 0;}
	$openLayer.css({left:w2, top:h2});
}
//拼接字符串;
function setString(arr){
	/* [{imgSrc,   title,   text,    num,    clickHand}] */
	var str = ''
	for(var i=0; i<arr.length; i++){
		var o = arr[i];
		str += '<div class="gm-icon-over" onmouseover="overFn(this)" onmouseout="outFn(this)" onclick="'+ o.clickHand +'" style="position:absolute; top:127px; left:275px;" alt="'+ o.num +'">';
		str += '	<table cellspacing=0 cellpadding=0  class="gvTables">';
		str += '		<tr>';
		str += '			<td rowspan=2 width="58">';
		str += '				<img class="mL9" src="'+ o.imgSrc +'" border=0 hspace=5 align=absmiddle>';
		str += '</td>';
		str += '<td style="font-weight: bold" valign="bottom">'+ o.title +'</td>';
		str += '</tr>';
		str += '<tr>';
		str += '<td style="color: gray" valign="top">'+ o.text +'</td>';
		str += '</tr>';
		str += '</table>';
		str += '</div>';
	}
	return str;
}
</script>
<%
    boolean visitStrategyPower = uc.hasPower("visitStrategyManagement");
    boolean mailPower = uc.hasPower("mailManagement");
    boolean smsPower = uc.hasPower("smsManagement");
    boolean dataBasePower = uc.hasPower("databaseManagement");
    boolean sysInfoPower = uc.hasPower("sysInfoManagement");
    boolean licensePower = uc.hasPower("licenseManagement");
    boolean alertPolicyPower = uc.hasPower("alertStrategy");
    boolean alertActionPower = uc.hasPower("alertAction");
    boolean ftpManagePower = uc.hasPower("ftpManagement");
    boolean ftpClientPower = uc.hasPower("ftpClient");
    boolean tftpManagePower = uc.hasPower("tftpManagement");
    boolean pingConfigPower = uc.hasPower("pingConfig");
    boolean snmpConfigPower = uc.hasPower("snmpConfig");
    boolean trapListenerConfigPower = uc.hasPower("trapListenerConfig");
    boolean syslogListenerConfigPower = uc.hasPower("syslogListenerConfig");
    boolean autoClear8800APower = uc.hasPower("autoClear8800A");
    boolean remoteQueryConfigPower = uc.hasPower("remoteQueryConfig");
    boolean mobileDeviceConfig = uc.hasPower("mobileDeviceConfig");
    
    boolean autoRefreshConfig = uc.hasPower("autoRefreshConfig");
    boolean unitConfig = uc.hasPower("unitConfig");
    boolean alertConfig = uc.hasPower("alertConfig");
    boolean northBoundConfig = uc.hasPower("northBoundConfig");
    boolean cmcFlapClear = uc.hasPower("cmcFlapClear");
    
    boolean personalInfoPower = uc.hasPower("personalInfo");
    boolean pwdEditPower = uc.hasPower("pwdEdit");
    boolean personalizePower = uc.hasPower("personalize");
    boolean tftpClientPower = uc.hasPower("tftpClient");
    boolean connectTestConfigPower = uc.hasPower("connectTestConfig");
    boolean tcpPortConfigPower = uc.hasPower("tcpPortConfig");
    boolean northPerfConfigPower = uc.hasPower("northPerfConfig");
    boolean cmJumpConfigPower = uc.hasPower("cmJumpConfig");
    boolean onuOnOffRecordPower = uc.hasPower("onuOnOffRecord");
%>
</HEAD>
<body class="whiteToBlack" onload="statusClear()">
	<div class="container">
		<div class="gmContainer">
		<%if(visitStrategyPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="onSecurityAccessClick()">
			<table cellspacing=0 cellpadding=0  class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvTpLogo.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@resources/SYSTEM.systemAccessStrategy@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setSystemAccessStrategy@</td>
				</tr>
			</table>
		</div>
		<%} if(mailPower){%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="emailServerClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvMail.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.mailServer@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setMailServer@</td>
				</tr>
			</table>
		</div>
		<%}if(smsPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="smsServerClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvPhone.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.smsServer@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setSmsServer@</td>
				</tr>
			</table>
		</div>
		<%}if(dataBasePower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="databaseManagerClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvTool.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@resources/SYSTEM.databaseMaintenance@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setDatabaseMaintenance@</td>
				</tr>
			</table>
		</div>
		<%}if(sysInfoPower){%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="viewRuntimeInfoClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel2.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/MAIN.systemRuntime@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.viewSystemruntime@</td>
				</tr>
			</table>
		</div>
		<% }if(licensePower){%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewLicenseClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvKey.png"
						border=0 hspace=5 align=absmiddle />
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.licenseManager@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.viewLicenseInfo@</td>
				</tr>
			</table>
		</div>
		<%} %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewAboutClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvBubble.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.about@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.visionInfo@</td>
				</tr>
			</table>
		</div>
		<%if (alertPolicyPower) {%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="alertSettingClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvAlert.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.alertConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setAlertConfig@</td>
				</tr>
			</table>
		</div>
		<%}if (alertActionPower) {%>
<!-- 		<div class=gm-icon onmouseover="this.className='gm-icon-over'" -->
<!-- 			onmouseout="this.className='gm-icon'" onclick="showFaultAction()"> -->
<!-- 			<table cellspacing=0 cellpadding=0 class="gvTables"> -->
<!-- 				<tr> -->
<!-- 					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvAlert2.png" -->
<!-- 						border=0 hspace=5 align=absmiddle> -->
<!-- 					</td> -->
<!-- 					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.alertAction@</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td style="color: gray" valign="top">@resources/SYSTEM.setAlertAction@</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 		</div> -->
		<%}if (personalInfoPower) {%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="modifyPersonalInfo()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvUser.png"
						border=0 hspace=5>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/MAIN.personalInfo@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/MAIN.modifyPersonalInfo@</td>
				</tr>
			</table>
		</div>
		<%}if (pwdEditPower) {%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="setPasswdClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvLock.png"
						border=0 hspace=5>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/MAIN.setPasswd@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/MAIN.setPasswd@</td>
				</tr>
			</table>
		</div>
		<%}if (personalizePower) {%>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="personalSettingClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvTheme.png"
						border=0 hspace=5>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.personalize@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setPersonalize@</td>
				</tr>
			</table>
		</div>
		<%} %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="customDesktopClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/myDesk.png"
						border=0 hspace=5>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.customMyDesktop@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/SYSTEM.setCustomMyDesktop@</td>
				</tr>
			</table>
		</div>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="onMibBrowseClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvEarth.png"
						border=0 hspace=5>
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">MIB Browser</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">MIB Browser</td>
				</tr>
			</table>
		</div>
		<%if(ftpManagePower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="ftpServerManageClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvFtp.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@resources/ftp.ftpServer@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/ftp.setFtpServer@</td>
				</tr>
			</table>
		</div>
		<%}if(ftpClientPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="ftpConnectManageClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvFtp2.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold">@resources/ftp.ftpConnect@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/ftp.setFtpConnect@</td>
				</tr>
			</table>
		</div>
		<%}if(tftpManagePower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="tftpServerManageClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="/images/system/gvTftp.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@resources/ftp.tftpServer@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/ftp.setTftpServer@</td>
				</tr>
			</table>
		</div>
		<%}if(tftpClientPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="tftpClientManageClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="/images/system/gvTftp2.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@resources/ftp.tftpClient@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@resources/ftp.setTftpClient@</td>
				</tr>
			</table>
		</div>
		<% }if(uc.hasPower("telnetClient")){ %> 
		<div class="gm-icon" onmouseover="this.className='gm-icon-over'" style="position:relative;"
            onmouseout="this.className='gm-icon'" onclick="openTelnetLayer()">
            <table cellspacing=0 cellpadding=0 class="gvTables">
                <tr>
                    <td rowspan=2 width="58">
                        <img class="mL9" src="/images/system/gvTelnet.png" border=0 hspace=5 align=absmiddle />
                    </td>
                    <td style="padding-top: 3px; font-weight: bold" valign="bottom">@network/TelnetClient.telnetClient@</td>
                </tr>
                <tr>
                    <td style="color: gray" valign="top">@network/TelnetClient.telnetClientCfg@</td>
                </tr>
            </table>
            <span class="orangeCir">3</span>
        </div>
        <% }%> 
		<%if(uc.hasPower("engineServerConfig")){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'"
			onclick="engineServerManageClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="/images/system/gvClock.png"
						border=0 hspace=5 align=absmiddle>
					</td>
					<td style="font-weight: bold" valign="bottom">@sys.engineServerConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.engineServerConfig@</td>
				</tr>
			</table>
		</div>
		<%} if(connectTestConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
							onmouseout="this.className='gm-icon'" onclick="viewConnectConfigClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvConnect.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.connectConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.setConnectConfig@</td>
				</tr>
			</table>
		</div>		
		<%}if(pingConfigPower){ %>	
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
							onmouseout="this.className='gm-icon'" onclick="viewPingConfigClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvPing.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.pingConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.setPingConfig@</td>
				</tr>
			</table>
		</div>
		<%}if(snmpConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewSnmpConfigClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.snmpConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.setSnmpConfig@</td>
				</tr>
			</table>
		</div>
		<%}if(tcpPortConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
							onmouseout="this.className='gm-icon'" onclick="viewTcpConnectConfigClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvPort.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.tcpconnectConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.setTcpConnectConfig@</td>
				</tr>
			</table>
		</div>
		<%}if(trapListenerConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewTrapListenportsClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWave.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.trapConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.setTrapConfig@</td>
				</tr>
			</table>
		</div>
		<%}if(syslogListenerConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewSyslogListenportsClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWave2.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.syslogConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.steSyslogConfig@</td>
				</tr>
			</table>
		</div>
		<%} %>
		<%if(uc.hasPower("fileAutoWrite") && uc.hasSupportModule("olt")){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="viewFileAutoWriteClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/autoSave.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.fileAutoWrite@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.autoWriteConfig@</td>
				</tr>
			</table>
		</div>
		<%} %>
		<%if(uc.hasPower("announcement")){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="sendMessage()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvBroad.png" border=0
						hspace=5 align=absmiddle></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.announcement@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.postAnnouncement@</td>
				</tr>
			</table>
		</div>
		<%} %>
		<%-- <% if(uc.hasSupportModule("olt") && uc.hasSupportModule("cmc")){%>
			<%if(autoClear8800APower){ %>
			<div class=gm-icon onmouseover="this.className='gm-icon-over'"
				onmouseout="this.className='gm-icon'" onclick="autoClear8800AConfig()">
				<table cellspacing=0 cellpadding=0 class="gvTables">
					<tr>
						<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel.png" border=0
							hspace=5 align=absmiddle></td>
						<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.autoClear8800AConfig@</td>
					</tr>
					<tr>
						<td style="color: gray" valign="top">@sys.autoClear8800AConfig@</td>
					</tr>
				</table>
			</div>
			<%} %>
		<%} %>
		<% if(uc.hasSupportModule("olt") && uc.hasSupportModule("cmc")){%>
			<%if(autoClear8800APower){ %>
			<div class=gm-icon onmouseover="this.className='gm-icon-over'"
				onmouseout="this.className='gm-icon'" onclick="showClear8800AHistory()">
				<table cellspacing=0 cellpadding=0 class="gvTables">
					<tr>
						<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvBroad.png" border=0
							hspace=5 align=absmiddle></td>
						<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.autoClearHistoryRecord@</td>
					</tr>
					<tr>
						<td style="color: gray" valign="top">@sys.autoClearHistoryRecord@</td>
					</tr>
				</table>
			</div>
			<%} %>
		<%} %> --%>
		
		<%if(autoClear8800APower){ %>
			<div class=gm-icon onmouseover="this.className='gm-icon-over'" style="position:relative;"
			onmouseout="this.className='gm-icon'" onclick="openTheLayer2()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvClear.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.autoClear@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.autoClear@</td>
				</tr>
			</table>
			<span class="orangeCir">2</span>
			</div>
		<%} %>

		
		<%if(remoteQueryConfigPower && uc.hasSupportModule("cmc")){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="remoteQueryCmClick()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvCmConfig.png" border=0
						hspace=5 align="absmiddle" /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.cmCollectModeConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.cmCollectModeTip@</td>
				</tr>
			</table>
		</div>
		<%} %>
		
		<%if(autoRefreshConfig){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"			
			onmouseout="this.className='gm-icon'" onclick="autoRefreshClick()">			
			<table cellspacing=0 cellpadding=0 class="gvTables">				
				<tr>					
					<td rowspan=2 width="58">
						<img class="mL9" src="../images/system/gvRefresh.png" border=0 hspace=5 align=absmiddle />
					</td>					
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.autoRefreshConfig@</td>				
				</tr>				
				<tr>					
					<td style="color: gray" valign="top">@sys.autoRefreshConfig@</td>				
				</tr>			
			</table>		
		</div>		
		<%} %>
				
		<%if(mobileDeviceConfig && uc.hasSupportModule("mobile")){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" style="position:relative;"
			onmouseout="this.className='gm-icon'" onclick="openTheLayer3()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvPhoneConfig.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.mobileConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.mobileConfigTip@</td>
				</tr>
			</table>
			<span class="orangeCir">3</span>
		</div>
		<%} %>
		
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" onmouseout="this.className='gm-icon'" onclick="showTerminalDownload()">
            <table cellspacing=0 cellpadding=0 class="gvTables">
                <tr>
                    <td rowspan=2 width="58"><img class="mL9" src="../images/system/gvPhoneConfig.png"
                        border=0 hspace=5 />
                    </td>
                    <td style="padding-top: 3px; font-weight: bold" valign="bottom">@COMMON.terminal@</td>
                </tr>
                <tr>
                    <td style="color: gray" valign="top">@COMMON.terminalDownload@</td>
                </tr>
            </table>
        </div>
		
		<%if(unitConfig){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" onmouseout="this.className='gm-icon'" onclick="showUnitConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvUnit.png"
						border=0 hspace=5 />
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@UNIT.unitConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@UNIT.configTip@</td>
				</tr>
			</table>
		</div>
		<%} %>
		
		<%if(alertConfig){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" onmouseout="this.className='gm-icon'" onclick="alertConfimConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvAlertConfirm.png"
						border=0 hspace=5 />
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.alertConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.alertConfig@</td>
				</tr>
			</table>
		</div>
		<%} %>			
		
		<%if(northBoundConfig){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" onmouseout="this.className='gm-icon'" onclick="nbiTrapConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWave.png"
						border=0 hspace=5 />
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.nbiTrapConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.nbiTrapConfig@</td>
				</tr>
			</table>
		</div>
		<%} if(northPerfConfigPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" onmouseout="this.className='gm-icon'" onclick="nbiPerferformanceConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel.png"
						border=0 hspace=5 />
					</td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.nbiPerformanceConfig@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.nbiPerformanceConfigTip@</td>
				</tr>
			</table>
		</div>
		
		<%} if(uc.hasSupportModule("cmc")){%>
			<%if(cmcFlapClear){ %>
			<div class=gm-icon onmouseover="this.className='gm-icon-over'" style="position:relative;"
			onmouseout="this.className='gm-icon'" onclick="cmcFlapClearConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvClear.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.clearCmtsFlap@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.clearCmtsFlap@</td>
				</tr>
			</table>
		</div>
		<% } %>
		<% } %>
		
		<% if(uc.hasSupportModule("cmc")){%>
			<% if(cmJumpConfigPower){ %>
			<div class=gm-icon onmouseover="this.className='gm-icon-over'" style="position:relative;"
			onmouseout="this.className='gm-icon'" onclick="showCmJumpMode()">
				<table cellspacing=0 cellpadding=0 class="gvTables">
					<tr>
						<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel.png" border=0
							hspace=5 align=absmiddle /></td>
						<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/userPower.cmJump@</td>
					</tr>
					<tr>
						<td style="color: gray" valign="top">@resources/userPower.cmJumpTip@</td>
					</tr>
				</table>
			</div>
			<%} %>
		<% } if(onuOnOffRecordPower){ %>
		<div class=gm-icon onmouseover="this.className='gm-icon-over'"
			onmouseout="this.className='gm-icon'" onclick="onuOnOffLineRecordConfig()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/autoSave.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">@sys.onOffRecordAutoWrite@</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">@sys.onOffRecordConfig@</td>
				</tr>
			</table>
		</div>
		<% } %>
		<!-- 请不要删除此处注释，后期扩展需要使用.leexiang
		<div class=gm-icon onmouseover="this.className='gm-icon-over'" style="position:relative;"
			onmouseout="this.className='gm-icon'" onclick="openTheLayer()">
			<table cellspacing=0 cellpadding=0 class="gvTables">
				<tr>
					<td rowspan=2 width="58"><img class="mL9" src="../images/system/gvWheel2.png" border=0
						hspace=5 align=absmiddle /></td>
					<td style="padding-top: 3px; font-weight: bold" valign="bottom">test txt</td>
				</tr>
				<tr>
					<td style="color: gray" valign="top">test text</td>
				</tr>
			</table>
			<span class="orangeCir">6</span>
		</div>
		 -->
		 <!-- 请不要删除，调试国际化使用
		 <div class=gm-icon onmouseover="this.className='gm-icon-over'"
				onmouseout="this.className='gm-icon'" onclick="setLanguageClick()">
				<table cellspacing=0 cellpadding=0 class="gvTables">
					<tr>
						<td rowspan=2 width="58"><img class="mL9" src="../images/system/config_language.png"
							border=0 hspace=5>
						</td>
						<td style="padding-top: 3px; font-weight: bold" valign="bottom">@resources/SYSTEM.languageConfig@</td>
					</tr>
					<tr>
						<td style="color: gray" valign="top">@resources/SYSTEM.setLanguageConfig@</td>
					</tr>
				</table>
			</div>
       -->
	  </div>
	  <div id="bg" onclick="getBackFn()">
	  	<div class="edge10">
	  		<a id="backBtn" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrLeft"></i>@COMMON.back@</span></a>
	  	</div>	  	
	  </div>
	  <div id="openLayer" onclick="getBackFn()"></div>
	</div>
	<div id="appDownload" style="display:none; width:100%; height:100%; position:absolute; top:0px; left:0px; z-index:2;  background:#000; filter:alpha(opacity=95); background:rgba(0,0,0,0.65);">
		<div class="downloadMain">
			<div class="box">
				<img src="../images/androidIcon.png" class="androidIcon" />
				<p class="txtCenter txt24Tip">@COMMON.downloadAndroid@</p>
                <div id="androidqrcode" class="middlePic" ></div>
				<p class="txtCenter">@COMMON.loginClick@ <a id="androidA" class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
			</div>
			<div class="box">
				<img src="../images/appleIcon.png" class="appleIcon" />
				<p class="txtCenter txt24Tip">@COMMON.downloadIOS@</p>
                <div id="iosqrcode" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="iosA"  class="blueLink"  target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
			</div>
		</div>
	</div>
	<div id="terminalDownload" style="display:none; width:100%; height:100%; position:absolute; top:0px; left:0px; z-index:2;  background:#000; filter:alpha(opacity=95); background:rgba(0,0,0,0.65);">
        <div class="downloadMain">
            <div class="box">
                <img src="../images/androidIcon.png" class="androidIcon" />
                <p class="txtCenter txt24Tip">@COMMON.downloadAndroid@</p>
                <div id="terminalAndroid" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="terminalAndroidLink" class="blueLink" target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
            </div>
            <div class="box">
                <img src="../images/appleIcon.png" class="appleIcon" />
                <p class="txtCenter txt24Tip">@COMMON.downloadIOS@</p>
                <div id="terminalIos" class="middlePic" ></div>
                <p class="txtCenter">@COMMON.loginClick@ <a id="terminalIosLink"  class="blueLink"  target="_blank">@COMMON.loginHere@</a> @COMMON.loginDownload@</p>
            </div>
        </div>
    </div>
</body>
</Zeta:HTML> 
