<%@ page language="java" contentType="text/html;charset=utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="cancel"/>
<link rel="stylesheet" type="text/css" href="../../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var entityId = ${entity.entityId};
function enableManagement() {
	var entityIds = [entityId];
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.ENTITYSNAP.basicInfo.conManagement, function(type) {
		if (type == 'cancel') {return;}
	 	$.ajax({url: '../../entity/enableManagement.tv', data: {'entityIds': entityIds},
	 		success: function() {
	 			location.href = 'entityPortal.tv?entityId=' + entityId;
	 		}, error: function() {
	 			window.parent.showErrorDlg();
	 		}, cache: false});			
	});
}
</script>
</head>
<body class=BLANK_WND style="padding: 20px">
	<center>
		<table cellspacing=5>
			<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.name" />:</td>
					<td width=10></td>
					<td><s:property value="oltAttribute.oltName"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.alias" />:</td>
					<td width=10></td>
					<td><s:property value="entity.name"/></td>
				    <td rowspan=3 align=center>
						<img id="entityIcon" src="<s:property value="entity.icon"/>" border=0>
					</td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.deviceStyle" />:</td>
					<td width=10></td>
					<s:if test="oltAttribute.oltDeviceStyle == 1">
					<td><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.fixed" /></td>
					</s:if>
					<s:if test="oltAttribute.oltDeviceStyle == 2">
					<td><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.chassisBased" /></td>
					</s:if>
					<s:else>
						<td><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.unknownType" /></td>
					</s:else>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.type" />:</td>
					<td width=10></td>
					<td><s:property value="oltAttribute.oltType"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.vender" />:</td>
					<td width=10></td>
					<td><s:property value="oltAttribute.vendorName"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.boards" />:</td>
					<td width=10></td>
					<td colspan=2><s:property value="oltAttribute.oltDeviceNumOfTotalServiceSlot"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.powers" />:</td>
					<td width=10></td>
					<td colspan=2><s:property value="oltAttribute.oltDeviceNumOfTotalPowerSlot"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.fans" />:</td>
					<td width=10></td>
					<td colspan=2><s:property value="oltAttribute.oltDeviceNumOfTotalFanSlot"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.controlIp" />:</td>
					<td width=10></td>
					<td colspan=2><s:property value="entity.ip"/></td>
				</tr>
				<tr>
					<td width=80 align="right"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.desc" />:</td>
					<td width=10></td>
					<td colspan=2>
						<textarea disabled rows=3 style="width: 100%"><s:property value="entity.sysDescr" /></textarea>
					</td>
				</tr>

			<tr>
				<td colspan=3 align=center height=40px><font color=red><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.device" />
						<s:property value="entity.ip" /> <fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.cancelManagement" /></font>
				</td>
			</tr>

			<tr>
				<td colspan=3 align=center height=40px><button type="button"
						class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
						onMouseDown="this.className='BUTTON_PRESSED120'"
						onMouseOut="this.className='BUTTON120'"
						onClick="enableManagement();"><fmt:message bundle="${cancel}" key="ENTITYSNAP.basicInfo.managementDevice" /></button>
				</td>
			</tr>
		</table>
	</center>
</body>
</html>
