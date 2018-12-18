<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<title></title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.topoFolderIcon {background-image: url(../images/network/topoicon.gif) !important;}
.topoFolderIcon1 {background-image: url(../images/network/topoicon.gif) !important;}
.topoFolderIcon20 {background-image: url(../images/network/region.gif) !important;}
.topoFolderIcon5 {background-image: url(../images/network/subnet.gif) !important;}
.topoFolderIcon6 {background-image: url(../images/network/cloudy16.gif) !important;}
.topoFolderIcon7 {background-image: url(../images/network/href.png) !important;}
.topoRegionIcon {background-image: url(../images/network/region.gif) !important;}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/data-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/data-json.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var folderId = <s:property value="topoParam.folderId"/>;
var discoverying = <s:property value="discoverying"/>;
var topologyUser = '<s:property value="topologyUser"/>';
var discoveryServiceFlag = <s:property value="topoParam.discoveryService"/>;
var onlySnmpFlag = <s:property value="topoParam.onlyDiscoverySnmp"/>;
var enableAutoDiscovery = <s:property value="topoParam.autoDiscovery"/>;
var enableTelnetDetect = <s:property value="topoParam.telnetDetected"/>;
var enableSshDetect = <s:property value="topoParam.sshDetected"/>;
var autoDiscoveryInterval = <s:property value="topoParam.autoDiscoveryInterval"/>/3600000;
var autoCreateMonitor = <s:property value="topoParam.autoCreateMonitor"/>;
var autoRefeshIpAddrBook = <s:property value="topoParam.autoRefeshIpAddrBook"/>;
var seed = '<s:property value="topoParam.seed"/>';
</script>
<script type="text/javascript" src="../network/topoDiscovery.js"></script>
</head><body class=POPUP_WND onunload="destroyRequestInterval()">
<div class=formtip id=tips style="display: none"></div>
<div style="display:none">
	<div id="contentstep1">
		<table width=100% height=100% cellspacing=0 cellpadding=0>
			<tr>
				<td height=20 colspan=2><input id="discoveryService"
					onclick="discoveryServiceFlag=this.checked;"
					<s:if test="topoParam.discoveryService">checked</s:if>
					type="checkbox"><label for="discoveryService"><fmt:message key="topoDiscovery.note1" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=8 colspan=2></td>
			</tr>
			<tr>
				<td height=22 width=100><fmt:message key="topoDiscovery.serviceList" bundle="${resources}" /></td>
				<td align=right><button id=selectAllBtn class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'" onclick="selectAllClick()"><fmt:message key="topoDiscovery.ReverseSelect" bundle="${resources}" /></button>&nbsp;
					<button id=cancelBtn class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onclick="addServiceClick()"><fmt:message key="topoDiscovery.Add" bundle="${resources}" /></button>&nbsp;
					<button id=cancelBtn class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onclick="removeServiceClick()"><fmt:message key="NETWORK.deleteMenuItem" bundle="${resources}" /></button></td>
			</tr>
			<tr>
				<td height=8 colspan=2></td>
			</tr>
			<tr>
				<td colspan=2 valign=top>
					<div id="services-div"></div>
				</td>
			</tr>
		</table>
	</div>
	<div id="contentstep2">
		<table width=100% height=100% cellspacing=0 cellpadding=0>
			<tr>
				<td height=20 colspan=2><input id="onlyDiscoverySnmp"
					onclick="onlySnmpFlag=this.checked;"
					<s:if test="topoParam.onlyDiscoverySnmp">checked</s:if>
					type="checkbox"><label for="onlyDiscoverySnmp"><fmt:message key="topoDiscovery.ManagesupportsSNMPdevicesonly" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td height=22 colspan=2><label for="onlyDiscoverySnmp"><fmt:message key="topoDiscovery.note2" bundle="${resources}" /></td>
			</tr>
			<tr>
				<td width=110 height=75><fmt:message key="topoDiscovery.SNMPcommunity" bundle="${resources}" /></td>
				<td><textarea id=communities rows=4
						style="overflow: auto; width: 302px" class=iptxa>
						<s:property value="topoParam.community" />
					</textarea></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.SNMPport" bundle="${resources}" /></td>
				<td><input id=snmpPort style="width: 60" class=iptxt
					type="text" value="<s:property value="topoParam.snmpPort"/>"></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.SNMPtimeout" bundle="${resources}" /></td>
				<td><input id=snmpTimeout style="width: 60" class=iptxt
					type="text" value="<s:property value="topoParam.snmpTimeout"/>">&nbsp;<fmt:message key="topoDiscovery.Millisecond" bundle="${resources}" /></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.SNMPretrycount" bundle="${resources}" /></td>
				<td><input id=snmpRetry style="width: 60" class=iptxt
					type="text" value="<s:property value="topoParam.snmpRetry"/>">&nbsp;<fmt:message key="topoDiscovery.bout" bundle="${resources}" /></td>
			</tr>
			<tr>
				<td valign=top height=100% colspan=2></td>
			</tr>
		</table>
	</div>
	<div id="contentstep3">
		<table width=100% height=100% cellspacing=0 cellpadding=0>
			<tr>
				<td height=20 colspan=2><input id="telnetDetected"
					onclick="enableTelnetDetect=this.checked;"
					<s:if test="topoParam.telnetDetected">checked</s:if>
					type="checkbox"><label for="telnetDetected"><fmt:message key="topoDiscovery.UseTelnettologindetection" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td height=20 colspan=2><input id="sshDetected"
					onclick="enableSshDetect=this.checked;"
					<s:if test="topoParam.sshDetected">checked</s:if> type="checkbox"><label
					for="sshDetected"><fmt:message key="topoDiscovery.UsingSSHtologindetection" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="label.username" bundle="${resources}" />:</td>
				<td><input id=username style="width: 120" class=iptxt
					type="text" value="<s:property value="topoParam.username"/>"></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.password" bundle="${resources}" /></td>
				<td><input id=passwd style="width: 120" class=iptxt
					type="text" value="<s:property value="topoParam.passwd"/>"></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.Command-lineprompt" bundle="${resources}" /></td>
				<td><input id=cmdPrompt style="width: 120" class=iptxt
					type="text" value="<s:property value="topoParam.cmdPrompt"/>"></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.Loginprompt" bundle="${resources}" /></td>
				<td><input id=loginPrompt style="width: 120" class=iptxt
					type="text" value="<s:property value="topoParam.loginPrompt"/>"></td>
			</tr>
			<tr>
				<td width=110 height=30><fmt:message key="topoDiscovery.Passwordhint" bundle="${resources}" /></td>
				<td><input id=passwdPrompt style="width: 120" class=iptxt
					type="text" value="<s:property value="topoParam.passwdPrompt"/>"></td>
			</tr>
			<tr>
				<td valign=top height=100% colspan=2></td>
			</tr>
		</table>
	</div>
	<div id="contentstep4">
		<table width=100% cellspacing=0 cellpadding=0>
			<tr>
				<td width=120 height=20><fmt:message key="topoDiscovery.Pingtimeout" bundle="${resources}" /></td>
				<td><input id=pingTimeout style="width: 60" class=iptxt
					type="text" value="<s:property value="topoParam.pingTimeout"/>">&nbsp;<fmt:message key="topoDiscovery.Millisecond" bundle="${resources}" /></td>
			</tr>
			<tr>
				<td height=8 colspan=2></td>
			</tr>
			<tr>
				<td height=20 colspan=2><input id=autoCreateMonitor
					onclick="startAutoCreateMonitor()"
					<s:if test="topoParam.autoCreateMonitor">checked</s:if>
					type="checkbox"><label for="autoCreateMonitor"><fmt:message key="topoDiscovery.note3" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=20 colspan=2><input id=autoRefeshIpAddrBook
					onclick="startAutoRefeshIpAddrBook()"
					<s:if test="topoParam.autoRefeshIpAddrBook">checked</s:if>
					type="checkbox"><label for="autoRefeshIpAddrBook"><fmt:message key="topoDiscovery.note4" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=20 colspan=2><input id=autoDiscovery
					onclick="startAutoDiscovery()"
					<s:if test="topoParam.autoDiscovery">checked</s:if>
					type="checkbox"><label for="autoDiscovery"><fmt:message key="topoDiscovery.Automatictopologydiscoverynetwork" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td width=120 height=30 style="padding-left: 20px"><fmt:message key="topoDiscovery.Automaticfoundtimeinterval" bundle="${resources}" /></td>
				<td><input id=autoDiscoveryInterval style="width: 60"
					<s:if test="!topoParam.autoDiscovery">disabled</s:if> class=iptxt
					type="text" value="">&nbsp;<fmt:message key="label.hours" bundle="${resources}" /></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td valign=top height=100% colspan=2></td>
			</tr>
		</table>
		<div style="height: 100%"></div>
	</div>
	<div id="contentstep5">
		<table width=100% height=100% cellspacing=0 cellpadding=0>
			<tr>
				<td height=20><fmt:message key="topoDiscovery.Arefound" bundle="${resources}" />&nbsp;&nbsp;<span id="discoverMsg"></span></td>
				<td height=20 align=right><span id="discoveryTime"><fmt:message key="topoDiscovery.spend0second" bundle="${resources}" /></span></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td height=18 colspan=2 class=progress-container><marquee
						id="progressMarquee" scrolldelay=200 scrollamount=25
						direction="right">
						<div id="progressBar-div" class=dprogress-bar></div>
					</marquee></td>
			</tr>
			<tr>
				<td height=8 colspan=2></td>
			</tr>
			<tr>
				<td height=20><fmt:message key="NETWORK.detail" bundle="${resources}" />:</td>
				<td align=right><input id=autoScrollBox checked
					onclick="setAutoScroll()" type="checkbox"><label
					for="autoScrollBox"><fmt:message key="topoDiscovery.Automaticrolling" bundle="${resources}" /></label></td>
			</tr>
			<tr>
				<td height=6 colspan=2></td>
			</tr>
			<tr>
				<td height=100% valign=top colspan=2>
					<div id=discoverMsgBox class=discovery-container
						style="height: 240px;"></div>
				</td>
			</tr>
		</table>
	</div>
</div>

<table width=100% height=100% cellspacing=0 cellpadding=0>
	<tr>
		<td width=180px class=vseparator
			style="background: url(../images/network/topology.jpg);" valign=top>
			<table>
				<tr>
					<Td height=50px style="padding-left: 10px;"><span id="newMsg"><fmt:message key="NETWORK.inputHostAndNetwork" bundle="${resources}" /></span>
					</Td>
				</tr>
			</table>
		</td>
		<Td style="padding: 10px 15px;">
			<div id="contentstep0">
				<table width=100% cellspacing=0 cellpadding=0>
					<tr>
						<td width=120 height=25><fmt:message key="topoDiscovery.Seednode" bundle="${resources}" />
						<td height=30><input type=text class=iptxt id=seed
							name="seed" value="" style="width: 280px"></td>
					</tr>
					<tr>
						<td height=60><fmt:message key="topoDiscovery.Hostnetwork" bundle="${resources}" /></td>
						<td height=30><textarea id="target" rows=3 name="target"
								class=iptxa
								onfocus="inputFocused('target', '<fmt:message key="topoDiscovery.note5" bundle="${resources}" />', 'iptxa_focused')"
								onblur="inputBlured(this, 'iptxa');"
								onclick="clearOrSetTips(this);" style="width: 280px">
								<s:property value="topoParam.target" />
							</textarea></td>
					</tr>
					<tr>
						<td height=60><fmt:message key="topoDiscovery.Eliminatehostnetwork" bundle="${resources}" />
						<td height=30><textarea id="excludeTarget" rows=3
								name="excludeTarget" class=iptxa
								onfocus="inputFocused('excludeTarget', '<fmt:message key="topoDiscovery.note6" bundle="${resources}" />', 'iptxa_focused')"
								onblur="inputBlured(this, 'iptxa');"
								onclick="clearOrSetTips(this);" style="width: 280px">
								<s:property value="topoParam.excludeTarget" />
							</textarea></td>
					</tr>

					<tr>
						<td height=25 colspan=2><fmt:message key="topoDiscovery.note7" bundle="${resources}" /></td>
					</tr>
					<tr>
						<td colspan=2 class=TREE-CONTAINER>
							<div id="topoTree"
								style="width: 100%; height: 100px; overflow: auto"></div>
						</td>
					</tr>
				</table>
			</div>
		</Td>
	</tr>
	<tr>
		<td height=50 class=hseparator style="padding: 5px;" colspan=2>
			<table width=100% cellspacing=0 cellpadding=0>
				<tr>
					<td align=right><button id=prevBtn disabled class=BUTTON75
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="navClick(-1, this); this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message key="topoDiscovery.Laststep" bundle="${resources}" /></button>
						<button id=nextBtn class=BUTTON75
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="navClick(1, this); this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message key="topoDiscovery.Nextstep" bundle="${resources}" /></button>&nbsp;
						<button id=finishBtn disabled class=BUTTON75
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="finishClick(this); this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message key="topoDiscovery.Done" bundle="${resources}" /></button>&nbsp;
						<button id=cancelButton class=BUTTON75
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'"
							onclick="cancelClick(this)"><fmt:message key="popDevicePollingInterval.Cancel" bundle="${resources}" /></button></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</body></html>
