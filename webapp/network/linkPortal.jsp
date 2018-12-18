<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base-2.1.js"></script>
<script type="text/javascript" src="../js/ext/ext-all-2.1.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script type="text/javascript" src="../network/entity-property.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var entityId = <s:property value="entityId"/>;
var icon48 = '../images/network/<s:property value="entity.icon48"/>';
var entityIp = '<s:property value="entity.ip"/>';
var entityName = '<s:property value="entity.name"/>';
setEnabledContextMenu(true);
</script>
</head>
<body class=BLANK_WND style="margin: 5px">
	<form>
		<table width=280 cellspacing=5 cellpadding=0>
			<tr>
				<td width=75><fmt:message key="label.name" bundle="${resources}" /></td>
				<td colspan=2><input id="name" type=text class=iptxt
					name="entity.name" maxlength=32 style="width: 210px"
					value="<s:property value="entity.name"/>">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="label.className" bundle="${resources}" /></td>
				<td width="150px"><s:property value="entity.categoryName" />
				</td>
				<td width=55 rowspan=4 align=center><a href="#"
					onclick="changeIcon()"><img id="entityIcon"
						src="<s:property value="entity.icon"/>" border=0>
				</a>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="label.type" bundle="${resources}" /></td>
				<td><s:property value="entity.typeName" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="label.vendor" bundle="${resources}" /></td>
				<td><s:property value="entity.corpName" />
				</td>
			</tr>
			<tr>
				<td>IP:</td>
				<td colspan=2><s:property value="entity.ip" />
				</td>
			</tr>
			<tr>
				<td>MAC:</td>
				<td colspan=2><s:property value="entity.mac" />
				</td>
			</tr>
			<tr>
				<td>SNMP:</td>
				<td colspan=2><s:if test="entity.snmpSupport"><fmt:message key="label.yes" bundle="${resources}" /></s:if>
					<s:else><fmt:message key="label.no" bundle="${resources}" /></s:else>
				</td>
			</tr>
			<s:if test="entity.type==72 || entity.type==127">
				<tr>
					<td>TVAgent:</td>
					<td colspan=2><s:if test="entity.agentInstalled"><fmt:message key="label.yes" bundle="${resources}" /></s:if>
						<s:else><fmt:message key="label.no" bundle="${resources}" /></s:else>
					</td>
				</tr>
			</s:if>
			<tr>
				<td><fmt:message key="label.location" bundle="${resources}" /></td>
				<td colspan=2><input class=iptxt name="entity.location"
					style="width: 210px;" type=text
					value="<s:property value="entity.location"/>">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="label.duty" bundle="${resources}" /></td>
				<td colspan=2><input class=iptxt name="entity.duty"
					style="width: 210px;" type=text
					value="<s:property value="entity.duty"/>">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="NETWORK.SysDescr" bundle="${resources}" /></td>
				<Td colspan=2><textarea class=iptxa name="entity.sysDescr"
						rows=10 style="overflow: auto; width: 210px">
						<s:property value="entity.sysDescr" />
					</textarea>
				</Td>
			</tr>
			<tr>
				<td colspan=3 align=right><button class=BUTTON95
						style="margin-right: 20px;"
						onMouseOver="this.className='BUTTON_OVER95'"
						onMouseOut="this.className='BUTTON95'"
						onMouseDown="this.className='BUTTON_PRESSED95'"
						onclick="changeIcon()"><fmt:message key="herfFolderProperty.ChangeIcon" bundle="${resources}" /></button>
					<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
						onMouseOut="this.className='BUTTON95'"
						onMouseDown="this.className='BUTTON_PRESSED95'"
						onclick="resetIcon()"><fmt:message key="NETWORK.resetIcon" bundle="${resources}" /></button>
				</td>
			</tr>
			<tr>
				<td colspan=3 align=right><button class=BUTTON95
						style="margin-right: 20px;"
						onMouseOver="this.className='BUTTON_OVER95'"
						onMouseOut="this.className='BUTTON95'"
						onMouseDown="this.className='BUTTON_PRESSED95'"
						onclick="onSaveClick()"><fmt:message key="Modify the information" bundle="${resources}" /></button>
					<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
						onMouseOut="this.className='BUTTON95'"
						onMouseDown="this.className='BUTTON_PRESSED95'"
						onclick="showSnap()"><fmt:message key="herfFolderProperty.ShowsSnapshot" bundle="${resources}" /></button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
