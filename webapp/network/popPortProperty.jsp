<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.topvision.framework.common.DateUtils" %>
<jsp:useBean id="port" scope="request" class="com.topvision.ems.network.domain.Port" />
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
function okClick() {
	$.ajax({
		url: 'updatePortInfo.tv', type: 'POST',
		data: jQuery(portForm).serialize(),
		success: function(json) {
			try {
				window.top.getActiveFrame().doRefresh();
			} catch (err) {
			}
			cancelClick();
		}, error: function(json) {
			window.top.showErrorDlg();
		}, cache: false
	});
}
function cancelClick() {
	window.top.closeModalDlg();
}
</script>
</HEAD>
<BODY class=POPUP_WND style="padding: 10px">
	<form id="portForm" name="portForm" align = "center">
		<input type=hidden name="port.portId"
			value="<s:property value="port.portId"/>">
		<table cellspacing=8 cellpadding=0>
			<tr>
				<td width=120px><fmt:message key="entityLinkInfo.portName" bundle="${resources}" />:</td>
				<td><input class=iptxt style="width: 220px" type=text
					maxlength=32 name="port.name"
					value="<s:property value="port.name"/>">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="port.ifIndex" bundle="${resources}" /></td>
				<td><s:property value="port.ifIndex" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="port.ifType" bundle="${resources}" /></td>
				<td><s:property value="port.ifTypeString" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="popPortProperty.operationStatus" bundle="${resources}" /></td>
				<td><s:property value="port.ifOperStatusString" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="popPortProperty.HardwareAddress" bundle="${resources}" /></td>
				<td><s:property value="port.ifPhysAddress" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="entityPorts.MaximumTransmissionUnit" bundle="${resources}" />:</td>
				<td><s:property value="port.ifMtu" />
				</td>
			</tr>
			<tr>
				<td><fmt:message key="port.ifSpeed" bundle="${resources}" /></td>
				<td><input class=iptxt style="width: 120px" type=text
					maxlength=16 name="port.ifSpeed"
					onkeydown="return judgeNumber(event)"
					value="<%= (long)port.getIfSpeed() %>">&nbsp;bps</td>
			</tr>
			<tr>
				<td><fmt:message key="port.ifDescr" bundle="${resources}" /></td>
				<td><textarea rows=3 class=iptxa style="width: 220px"
						name="port.note">
						<s:property value="port.note" />
					</textarea>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="NETWORK.mondifyTimeHeader" bundle="${resources}" />:</td>
				<td><%= port.getLastChangeTime() == 0 ? "-" : DateUtils.format(port.getLastChangeTime()) %></td>
			</tr>
			<tr>
				<Td></Td>
			</tr>
			<tr>
				<td colspan=2 valign=bottom align=right>
					<button class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'"
						onclick="okClick()"><fmt:message key="popDevicePollingInterval.confirm" bundle="${resources}" /></button>&nbsp;
					<button class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onMouseDown="this.className='BUTTON_PRESSED75'"
						onclick="cancelClick()"><fmt:message key="popDevicePollingInterval.Cancel" bundle="${resources}" /></button>
				</td>
			</tr>
		</table>
	</form>
</BODY>
</HTML>