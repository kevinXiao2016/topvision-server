<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
function lastClick() {
	location.href = 'showQuickConfigWizard.tv';
}
function finishClick() {
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
</script>
</head><body class=POPUP_WND scroll=no>
<div class=formtip id=tips style="display: none"></div>
<form action="showQuickConfigTask.tv" method="post">
<table width=100% height=100% cellspacing=0 cellpadding=0>
<Tr><Td class=vseparator width=150px style="background:url(../images/wizard.jpg) no-repeat;">&nbsp;</Td>
<Td valign=top style="padding-left:5px;padding-top:5px;">
	<table cellspacing=10 cellpadding=0>
	<tr><td colspan=2><font style="font-size:10pt;font-weight:bold;"><fmt:message bundle="${sys}" key="sys.batchModi" /></font></td></tr>
	
	<tr><td width=100px><fmt:message bundle="${sys}" key="sys.belongTopo" />:</td>
	<td><select id="folderId" style="width:200px;">
		<option value="0">-- <fmt:message bundle="${sys}" key="sys.selTopo" /> --</option>
		<s:iterator value="topoFolders">
		<option value="<s:property value="folderId"/>"><s:property value="name"/></option>
		</s:iterator>
	</select></td></tr>
	
	<tr><td><fmt:message bundle="${sys}" key="sys.entityType" />:</td>
	<td><select id="entityType" style="width:200px;">
		<option value="0">-- <fmt:message bundle="${sys}" key="sys.selType" /> --</option>
		<s:iterator value="entityTypes">
		<option value="<s:property value="typeId"/>"><s:property value="displayName"/></option>
		</s:iterator>
	</select></td></tr>
	
	<tr><Td colspan=2>
	<table cellspacing=8 cellpadding=0>
	<tr><td width=120 align=right><fmt:message bundle="${sys}" key="sys.snmpVersion" />:</td>
	<td>
	<select style="width:150px;">
		<option value="0">V1</option>
		<option value="1" selected>V2C</option>
		<option value="3">V3</option>
	</select></td>
		<td width=120 align=right><fmt:message bundle="${sys}" key="sys.community" />: <font color=red>*</font></td>
		<td><input class=iptxt type=text style="width:150px" 
		id="community" name="snmpParam.community" value="<s:property value="snmpParam.community"/>"
		onfocus="inputFocused('community', '<fmt:message bundle="${sys}" key="sys.snmpCommNotNull" />', 'iptxt_focused')"
		onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);"></td>	
	</tr>		
	</table>
	</Td></tr>
	
	<tr><td colspan=2>
	<table id="v3setting" cellspacing=8 cellpadding=0>
	<tr><td width=100><fmt:message bundle="${sys}" key="sys.securityLevel" />:</td><td>
		<select id="securityLevel" name="snmpParam.securityLevel" style="width:150px">
			<option value="1">NOAUTH_NOPRIV</option>
			<option value="2">AUTH_NOPRIV</option>
			<option value="3">AUTH_PRIV</option>
		</select></td>	
		<td width=100><fmt:message bundle="${sys}" key="sys.chkProtocol" />:</td><td>
		<select id="authProtocol" name="snmpParam.authProtocol" style="width:150px">
			<option value="MD5">MD5</option>
			<option value="SHA">SHA</option>
		</select></td>	
	</tr>
	<tr><td><fmt:message bundle="${sys}" key="mibble.username" />:</td><td>
		<input class=iptxt type=text style="width:150px" 
			name="snmpParam.username"
			value="<s:property value="snmpParam.username"/>"></td>	
		<td><fmt:message bundle="${sys}" key="sys.chkPwd" />:</td><td>
		<input class=iptxt type=password style="width:150px" 
			name="snmpParam.authPassword"
			value="<s:property value="snmpParam.authPassword"/>">
		</td>
	</tr>
	<tr>
		<td><fmt:message bundle="${sys}" key="sys.priProtocol" />:</td><td><input class=iptxt type=text style="width:150px" 
		name="snmpParam.privProtocol" value="CBC-DES"></td>
		<td><fmt:message bundle="${sys}" key="sys.proPwd" />:</td><td><input class=iptxt type=password style="width:150px" 
		name="snmpParam.privPassword" value="<s:property value="snmpParam.privPassword"/>"></td>	
	</tr>
	<tr><td><fmt:message bundle="${sys}" key="sys.ctxName" />:</td><td>
		<input class=iptxt type=text style="width:150px" 
			name="snmpParam.contextName"
			value="<s:property value="snmpParam.contextName"/>">
		</td>	
		<td>Context ID:</td><td><input class=iptxt type=text style="width:150px" 
		name="snmpParam.contextId" value="<s:property value="snmpParam.contextId"/>"></td>	
	</tr>
	<tr><td><fmt:message bundle="${sys}" key="sys.engineID" />:</td><td>
		<input class=iptxt type=text style="width:150px" 
			name="snmpParam.authoritativeEngineID"
			value="<s:property value="snmpParam.authoritativeEngineID"/>">
		</td>	
		<Td></Td><td></td>
	</tr>
	</table>
	</td></tr>

	</table>
</Td></Tr>
<tr><td class=hseparator valign=top align=right colspan=2 
	height=50px style="padding-top:10px;padding-right:10px">
<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="lastClick()"><fmt:message bundle="${sys}" key="sys.prev" /></button><button
	disabled class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"><fmt:message bundle="${sys}" key="sys.next" /></button>&nbsp;	
	<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="finishClick()"><fmt:message bundle="${sys}" key="sys.done" /></button>&nbsp;
	<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="cancelClick()"><fmt:message bundle="${sys}" key="mibble.cancel" /></button></td></tr>
</table></form>
</body></html>
