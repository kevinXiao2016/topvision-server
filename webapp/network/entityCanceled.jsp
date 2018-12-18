<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var entityId = <s:property value="entity.entityId"/>;
function enableManagement() {
	var entityIds = [entityId];
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.entityCanceled.confirmMgmtDevice, function(type) {
		if (type == 'cancel') {return;}
	 	$.ajax({url: '../entity/enableManagement.tv', data: {'entityIds': entityIds},
	 		success: function() {
	 			location.href = 'showEntitySnapJsp.tv?entityId=' + entityId;
	 		}, error: function() {
	 			window.parent.showErrorDlg();
	 		}, cache: false});			
	});
}
</script>
</head>
<body class=BLANK_WND style="padding: 20px">
	<table cellspacing=5 align="center">
		<tr>
			<td width=80><fmt:message key="COMMON.name1" bundle="${resources}"/></td>
			<td><s:property value="entity.name" /></td>
			<td rowspan=4 align=center><img id="entityIcon"
				src="<s:property value="entity.icon"/>" border=0></td>
		</tr>
		<tr>
			<td><fmt:message key="entityCanceled.Classification" bundle="${resources}"/></td>
			<td><s:property value="entity.typeName" /></td>
		</tr>
		<s:if test="entity.type==72 || entity.type==127">
			<tr>
				<td><fmt:message key="entityCanceled.Operatingsystem" bundle="${resources}"/></td>
				<td><s:property value="entity.modelName" /></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td><fmt:message key="entityCanceled.type" bundle="${resources}"/></td>
				<td><s:property value="entity.modelName" /></td>
			</tr>
		</s:else>
		<tr>
			<td><fmt:message key="entityCanceled.Firm" bundle="${resources}"/></td>
			<td><s:property value="entity.corpName" /></td>
		</tr>
		<tr>
			<td><fmt:message key="COMMON.ip" bundle="${resources}"/>:</td>
			<td colspan=2><s:property value="entity.ip" /></td>
		</tr>
		<tr>
			<td><fmt:message key="WorkBench.MacAddress" bundle="${resources}"/>:</td>
			<td colspan=2><s:property value="entity.mac" /></td>
		</tr>
		<s:if test="entity.snmpSupport">
			<tr>
				<td>SNMP:</td>
				<td colspan=2><fmt:message key="COMMON.supported" bundle="${resources}"/></td>
			</tr>
			<tr>
				<td>OID:</td>
				<td colspan=2><s:property value="entity.sysObjectID" /></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td>SNMP:</td>
				<td colspan=2><fmt:message key="COMMON.unsupported" bundle="${resources}"/></td>
			</tr>
		</s:else>
		<s:if test="entity.type==72||entity.type==127">
			<tr>
				<td>TVAgent:</td>
				<td colspan=2><s:if test="entity.agentInstalled"><fmt:message key="COMMON.supported" bundle="${resources}"/></s:if> <s:else><fmt:message key="COMMON.unsupported" bundle="${resources}"/></s:else>
				</td>
			</tr>
		</s:if>
		<tr>
			<td><fmt:message key="entityCanceled.Position" bundle="${resources}"/></td>
			<td colspan=2><s:property value="entity.location" /></td>
		</tr>
		<tr>
			<td><fmt:message key="entityCanceled.Personincharge" bundle="${resources}"/></td>
			<td colspan=2><s:property value="entity.duty" /></td>
		</tr>
		<tr>
			<td><fmt:message key="entityCanceled.SystemDesc" bundle="${resources}"/></td>
			<td colspan=2><textarea disabled rows=3 style="width: 100%">
					<s:property value="entity.sysDescr" />
				</textarea></td>
		</tr>

		<tr>
			<td colspan=3 align=center height=40px><font color=red><fmt:message key="COMMON.entity" bundle="${resources}"/>
					<s:property value="entity.ip" /><fmt:message key="entityCanceled.Managementcanceled" bundle="${resources}"/>
			</font></td>
		</tr>

		<tr>
			<td colspan=3 align=center height=40px><button type="button"
					class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
					onMouseDown="this.className='BUTTON_PRESSED120'"
					onMouseOut="this.className='BUTTON120'"
					onClick="enableManagement();"><fmt:message key="entityCanceled.Re-administerdevice" bundle="${resources}"/></button></td>
		</tr>
	</table>
</body>
</html>
