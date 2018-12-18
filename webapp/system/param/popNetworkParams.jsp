<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<%@include file="../../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<%@include file="../../include/tabPatch.inc"%>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script> 
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var discoveryServiceFlag = <s:property value="topoParam.discoveryService"/>;
var onlySnmpFlag = <s:property value="topoParam.onlyDiscoverySnmp"/>;
var enableAutoDiscovery = <s:property value="topoParam.autoDiscovery"/>;
var enableTelnetDetect = <s:property value="topoParam.telnetDetected"/>;
var enableSshDetect = <s:property value="topoParam.sshDetected"/>;
var autoDiscoveryInterval = <s:property value="topoParam.autoDiscoveryInterval"/>/60000;
var autoCreateMonitor = <s:property value="topoParam.autoCreateMonitor"/>;
var autoRefeshIpAddrBook = <s:property value="topoParam.autoRefeshIpAddrBook"/>;
var seed = '<s:property value="topoParam.seed"/>';

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	var w = document.body.clientWidth - 20;
	var h = document.body.clientHeight - 60;
	if (w < 300) {
		w = 300;
	}
	if (h < 100) {
		h = 100;
	}	

    var tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: h,
        activeTab: 0,
        frame: true,
        defaults:{autoHeight: true},
        items:[
        	{contentEl: 'generalTab', title: '<fmt:message bundle="${sys}" key="sys.managedNetwork" />'},
            {contentEl: 'snmpTab', title: '<fmt:message bundle="${sys}" key="sys.snmpParam" />'},
            {contentEl: 'telnetTab', title: '<fmt:message bundle="${sys}" key="sys.sshParam" />'},
            {contentEl: 'actionTab', title: '<fmt:message bundle="${sys}" key="sys.otherParam" />'}
        ]
    });
    
    Zeta$('buttonPanel').style.display = '';
});
function setInputAble() {
	if(Zeta$('autoDiscovery').checked){
		Zeta$('autoDiscoveryInterval').disabled = false;
		}else {
			Zeta$('autoDiscoveryInterval').disabled = true;
			}	
}
function okClick() {
	$.ajax({url: '../param/modifySecurityParams.tv', type: 'POST',
	data: {},
	success: function() {
		cancelClick();
	},
	error: function() {
		window.top.showErrorDlg();
	},
	dataType: 'plain', cache: false});
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
</script>	  
</HEAD><BODY class=POPUP_WND style="padding:10px">
<div id="tabs">
<div id="generalTab" class="x-hide-display">
	<table width=100% cellspacing=10 cellpadding=0>
	<tr><td width=120><fmt:message bundle="${sys}" key="sys.seedNode" />:<td><input type=text class=iptxt id=seed disabled name="seed" 
	value="<s:property value="topoParam.seed"/>" style="width:280px"></td></tr>
	<tr><td><fmt:message bundle="${sys}" key="sys.hostAndNetwork" />:</td><td><textarea id="target" name="target" disabled rows=3
	class=iptxa style="width:280px"><s:property value="topoParam.target"/></textarea></td></tr>
	<tr><td><fmt:message bundle="${sys}" key="sys.excludeHostAndNetwork" />:<td><textarea id="excludeTarget" name="excludeTarget" disabled rows=3
	class=iptxa style="width:280px"><s:property value="topoParam.excludeTarget"/></textarea></td></tr>
</table>
</div>
<div id="snmpTab" class="x-hide-display">
	<table width=100% cellspacing=10 cellpadding=0>
		<tr><td colspan=2><input id="onlyDiscoverySnmp" onclick="onlySnmpFlag=this.checked;" <s:if test="topoParam.onlyDiscoverySnmp">checked</s:if> type="checkbox"><label for="onlyDiscoverySnmp"><fmt:message bundle="${sys}" key="sys.findedSupportSNMPEntity" /></label></td></tr>
		<tr><td colspan=2><fmt:message bundle="${sys}" key="sys.bycomma" /></td></tr>		
		<tr><td width=110><fmt:message bundle="${sys}" key="sys.community" />:</td><td><textarea id=communities rows=4 style="overflow:auto; width:302px" class=iptxa><s:property value="topoParam.community"/></textarea></td></tr>
		<tr><td><fmt:message bundle="${sys}" key="sys.snmpPort" />:</td><td><input id=snmpPort style="width:60" class=iptxt type="text" value="<s:property value="topoParam.snmpPort"/>"></td></tr>		   	
		<tr><td><fmt:message bundle="${sys}" key="sys.snmpTimeout" />:</td><td><input id=snmpTimeout style="width:60" class=iptxt type="text" value="<s:property value="topoParam.snmpTimeout"/>">&nbsp;<fmt:message bundle="${sys}" key="sys.millisecond" /></td></tr>
		<tr><td><fmt:message bundle="${sys}" key="sys.snmpRepeat" />:</td><td><input id=snmpRetry style="width:60" class=iptxt type="text" value="<s:property value="topoParam.snmpRetry"/>">&nbsp;<fmt:message bundle="${sys}" key="sys.times" /></td></tr>
		<tr><td valign=top height=100% colspan=2></td></tr>
	</table>
</div>
<div id="telnetTab" class="x-hide-display">
	<table width=100% cellspacing=10 cellpadding=0>
		<tr><td colspan=2><input id="telnetDetected" onclick="enableTelnetDetect=this.checked;" <s:if test="topoParam.telnetDetected">checked</s:if> type="checkbox"><label for="telnetDetected"><fmt:message bundle="${sys}" key="sys.telnet" /></label></td></tr>
		<tr><td colspan=2><input id="sshDetected" onclick="enableSshDetect=this.checked;" <s:if test="topoParam.sshDetected">checked</s:if> type="checkbox"><label for="sshDetected"><fmt:message bundle="${sys}" key="sys.ssh" /></label></td></tr>		
		<tr><td width=110><fmt:message bundle="${sys}" key="mibble.username" />:</td><td><input id=username style="width:120" class=iptxt type="text" value="<s:property value="topoParam.username"/>"></td></tr>		   	
		<tr><td><fmt:message bundle="${sys}" key="sys.pwd" />:</td><td><input id=passwd style="width:120" class=iptxt type="text" value="<s:property value="topoParam.passwd"/>"></td></tr>
		<tr><td><fmt:message bundle="${sys}" key="sys.cmd" />:</td><td><input id=cmdPrompt style="width:120" class=iptxt type="text" value="<s:property value="topoParam.cmdPrompt"/>"></td></tr>
		<tr><td><fmt:message bundle="${sys}" key="sys.login" />:</td><td><input id=loginPrompt style="width:120" class=iptxt type="text" value="<s:property value="topoParam.loginPrompt"/>"></td></tr>
		<tr><td><fmt:message bundle="${sys}" key="sys.pwdTip" />:</td><td><input id=passwdPrompt style="width:120" class=iptxt type="text" value="<s:property value="topoParam.passwdPrompt"/>"></td></tr>		
	</table>
</div>
<div id="actionTab" class="x-hide-display">
	<table width=100% cellspacing=10 cellpadding=0>
		<tr><td width=120><fmt:message bundle="${sys}" key="sys.pingTimeout" />:</td><td><input id=pingTimeout style="width:60" class=iptxt type="text" value="<s:property value="topoParam.pingTimeout"/>">&nbsp;<fmt:message bundle="${sys}" key="sys.millisecond" /></td></tr>		
		<tr><td colspan=2><input id=autoCreateMonitor onclick="startAutoCreateMonitor()"  <s:if test="topoParam.autoCreateMonitor">checked</s:if> type="checkbox"><label for="autoCreateMonitor"><fmt:message bundle="${sys}" key="sys.autoChk" /></label></td></tr>
		<tr><td colspan=2><input id=autoRefeshIpAddrBook onclick="startAutoRefeshIpAddrBook()"  <s:if test="topoParam.autoRefeshIpAddrBook">checked</s:if> type="checkbox"><label for="autoRefeshIpAddrBook"><fmt:message bundle="${sys}" key="sys.autoRefresh" /></label></td></tr>
		<tr><td colspan=2><input id=autoDiscovery onclick="setInputAble();startAutoDiscovery();"  <s:if test="topoParam.autoDiscovery">checked</s:if> type="checkbox"><label for="autoDiscovery"><fmt:message bundle="${sys}" key="sys.autoDiscovery" /></label></td></tr>
		<tr><td style="padding-left:20px"><fmt:message bundle="${sys}" key="sys.autoDiscPeriod" />:</td><td><input id=autoDiscoveryInterval style="width:60" <s:if test="!topoParam.autoDiscovery">disabled</s:if> class=iptxt type="text" value="<s:property value="topoParam.autoDiscoveryIntervalStr"/>">&nbsp;<fmt:message bundle="${sys}" key="sys.hour" /></td></tr>
	</table>   
</div>
</div>
<div id="buttonPanel" align=right style="padding-top:10px;display:none">
	<button class=BUTTON75 type="button" onMouseOver="this.className='BUTTON_OVER75'"
    onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
    onclick="okClick()"><fmt:message bundle="${sys}" key="sys.confirm" /></button>&nbsp;<button 
    class=BUTTON75 type="button" onMouseOver="this.className='BUTTON_OVER75'"
    onMouseOut="this.className='BUTTON75'"onMouseDown="this.className='BUTTON_PRESSED75'"
    onclick="cancelClick()"><fmt:message bundle="${sys}" key="mibble.cancel" /></button>
</div>
</BODY></HTML>
