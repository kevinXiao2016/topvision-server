<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title></title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
function viewClick() {
	location.href = '../portal/showEntitySnapJsp.tv?entityId=<s:property value="entity.entityId"/>';
}
function updateClick() {
	if (validateForm()) {
		$.ajax({
			url: 'saveEntityDetail.tv', type: 'POST',
			data: jQuery(entityForm).serialize(),
			success: function(json) {
				if (json.exist) {
					window.top.showMessageDlg(I18N.COMMON.tip, I18N.entityConfig.note1);
				} else {
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.entityConfig.modifyConfigSuccess);
					window.top.setActiveTitle(I18N.COMMON.entity+'[' + json.ip + ']');
					Zeta$('oldIp').value = json.ip;
				}
			}, error: function(json) {
				window.top.showErrorDlg();
			}, dataType: 'json', cache: false
		});
	}
}
function resetClick() {
	var value=Zeta$('snmpVersion').value;
	Zeta$('entityForm').reset();
	Zeta$('snmpVersion').value=value;
}
function doOnload() {
	Zeta$('saveBt').disabled = true;
	var el = Zeta$('snmpVersion');
	for(var i = 0; i< el.options.length; i++) {
		if(el.options[i].value == <s:property value="snmpParam.version"/>) {
			el.selectedIndex = i;
			break;
		}
	}
	
	el = Zeta$('securityLevel');
	for(var i = 0; i< el.options.length; i++) {
		if(el.options[i].value == <s:property value="snmpParam.securityLevel"/>) {
			el.selectedIndex = i;
			break;
		}
	}
	
	el = Zeta$('authProtocol');
	for(var i = 0; i< el.options.length; i++) {
		if(el.options[i].value == '<s:property value="snmpParam.authProtocol"/>') {
			el.selectedIndex = i;
			break;
		}
	}
	snmpChanged();
}
function validateForm() {
	var el = Zeta$('ip');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}	
	el = Zeta$('community');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}
	el = Zeta$('name');
	if (el.value.trim() == '') {
		el.focus();
		return false;
	}	
	return true;
}

function snmpChanged(obj) {
	switch(Zeta$('snmpVersion').value) {
	case '0':
	case '1':
		Zeta$('v2setting').style.display = '';
		Zeta$('v3setting').style.display = 'none';
		break;
	case '3':
		Zeta$('v2setting').style.display = 'none';
		Zeta$('v3setting').style.display = '';
		break;
	}
}
</script>
</head>
<body class=BLANK_WND onload="doOnload();" style="margin: 10px;">
	<div class=formtip id=tips style="display: none"></div>
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td><%@ include file="entity.inc"%></td>
		</tr>
		<tr>
			<td valign=top style="padding-top: 10px" align=center>
				<form id="entityForm" name="entityForm">
					<input type=hidden name="entity.entityId"
						value="<s:property value="entity.entityId"/>"> <input
						type=hidden id="oldIp" name="entity.oldIp"
						value="<s:property value="entity.ip"/>">
					<table width=700px cellspacing=8 cellpadding=0>
						<tr>
							<td width=120 align=right><fmt:message key="Config.oltConfigFileImported.deviceName" bundle="${resources}"/>:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="entity.sysName"/>">
							</td>
							<td width=120 align=right><fmt:message key="topo.virtualDeviceList.newEntity.deviceAlias" bundle="${resources}"/>:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="entity.name" id="name"
								value="<s:property value="entity.name"/>"
								onfocus="inputFocused('name', '<fmt:message key="entityConfig.Devicealiasnotempty" bundle="${resources}"/>', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"></td>
						</tr>
						<tr>
							<td align=right><font color=red>*</font><fmt:message key="COMMON.ip" bundle="${resources}"/>:</td>
							<td><input id="ip" name="entity.ip"
								onfocus="inputFocused('ip', '<fmt:message key="entityConfig.ipnotnull" bundle="${resources}"/>', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);" class=iptxt type=text
								style="width: 218px" value="<s:property value="entity.ip"/>">
							</td>
							<td align=right><fmt:message key="entityCanceled.Personincharge" bundle="${resources}"/></td>
							<td><input class=iptxt type=text style="width: 218px"
								name="entity.duty" value="<s:property value="entity.duty"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message key="WorkBench.MacAddress" bundle="${resources}"/>:</td>
							<td><input class=iptxt type=text disabled
								style="width: 218px" name="entity.mac"
								value="<s:property value="entity.mac"/>">
							</td>
							<td align=right><fmt:message key="entityCanceled.Position" bundle="${resources}"/></td>
							<td><input class=iptxt type=text style="width: 218px"
								name="entity.location"
								value="<s:property value="entity.location"/>">
							</td>
						</tr>
						<tr>
							<td align=right>OID:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="entity.sysObjectID"/>">
							</td>
							<td align=right><fmt:message key="entityConfig.server" bundle="${resources}"/></td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="entity.sysServices"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message key="COMMON.description" bundle="${resources}"/>:</td>
							<td>
							<textarea disabled class=iptxa rows=7 style="width: 218px; overflow: auto" value=""><s:property value="entity.sysDescr" /></textarea>
							</td>
							<td align=right><fmt:message key="COMMON.note1" bundle="${resources}"/></td>
							<td>
							<textarea name="entity.note" class=iptxa rows=7 style="width: 218px; overflow: auto" value=""><s:property value="entity.note" /></textarea>
							</td>
						</tr>
						<tr>
							<td align=right>SNMP<fmt:message key="SYSTEM.version" bundle="${resources}"/></td>
							<td><select id="snmpVersion" name="snmpParam.version"
								style="width: 218px" onchange="snmpChanged();">
									<option value="0">V1</option>
									<option value="1">V2C</option>
									<option value="3">V3</option>
							</select>
							</td>
							<td></td>
							<Td></Td>
						</tr>
						<tr id="v2setting">
							<td align=right><font color=red>*</font><fmt:message key="entityConfig.communityreadonly" bundle="${resources}"/></td>
							<td><input class=iptxt type=password style="width: 218px"
								id="community" name="snmpParam.community"
								value="<s:property value="snmpParam.community"/>"
								onfocus="inputFocused('community', '<fmt:message key="entityConfig.note2" bundle="${resources}"/>', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);">
							</td>
							<Td align=right><fmt:message key="entityConfig.communitywritable" bundle="${resources}"/></Td>
							<td><input class=iptxt type=password style="width: 218px"
								id="writeCommunity" name="snmpParam.writeCommunity"
								value="<s:property value="snmpParam.writeCommunity"/>"
								onfocus="inputFocused('writeCommunity', '', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);">
							</td>
						</tr>
					</table>
					<table id="v3setting" width=700px cellspacing=8 cellpadding=0>
						<tr>
							<td width=120 align=right><fmt:message key="entityConfig.Securitylevel" bundle="${resources}"/></td>
							<td><select id="securityLevel"
								name="snmpParam.securityLevel" style="width: 218px">
									<option value="1">NOAUTH_NOPRIV</option>
									<option value="2">AUTH_NOPRIV</option>
									<option value="3">AUTH_PRIV</option>
							</select>
							</td>
							<td width=120 align=right><fmt:message key="entityCongig.AuthenticationProtocol" bundle="${resources}"/></td>
							<td><select id="authProtocol" name="snmpParam.authProtocol"
								style="width: 218px">
									<option value="MD5">MD5</option>
									<option value="SHA">SHA</option>
							</select>
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message key="SYSTEM.userName" bundle="${resources}"/>:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="snmpParam.username"
								value="<s:property value="snmpParam.username"/>">
							</td>
							<td align=right><fmt:message key="entityConfig.VerifyPassword" bundle="${resources}"/></td>
							<td><input class=iptxt type=password style="width: 218px"
								name="snmpParam.authPassword"
								value="<s:property value="snmpParam.authPassword"/>"></td>
						</tr>
						<tr>
							<td align=right><fmt:message key="entityConfig.Privateagreement" bundle="${resources}"/></td>
							<td><input class=iptxt type=text style="width: 218px"
								name="snmpParam.privProtocol" value="CBC-DES">
							</td>
							<td align=right><fmt:message key="entityConfig.Privatepassword" bundle="${resources}"/></td>
							<td><input class=iptxt type=password style="width: 218px"
								name="snmpParam.privPassword"
								value="<s:property value="snmpParam.privPassword"/>">
							</td>
						</tr>
						<tr>
							<td align=right>Context<fmt:message key="SYSTEM.engineManagement" bundle="${resources}"/>:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="snmpParam.contextName"
								value="<s:property value="snmpParam.contextName"/>"></td>
							<td align=right>Context ID:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="snmpParam.contextId"
								value="<s:property value="snmpParam.contextId"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message key="entityConfig.ValidationengineID" bundle="${resources}"/></td>
							<td><input class=iptxt type=text style="width: 218px"
								name="snmpParam.authoritativeEngineID"
								value="<s:property value="snmpParam.authoritativeEngineID"/>">
							</td>
							<Td></Td>
							<td></td>
						</tr>
					</table>
					<table width=700px cellspacing=8 cellpadding=0>
						<tr>
							<Td align=right>
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="updateClick()"><fmt:message key="entityConfig.Modifyconfiguration" bundle="${resources}"/></button>&nbsp;
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="resetClick()"><fmt:message key="SYSTEM.Reset" bundle="${resources}"/></button></Td>
						</tr>
					</table>
				</form></td>
		</tr>
	</table>
</body>
</html>
