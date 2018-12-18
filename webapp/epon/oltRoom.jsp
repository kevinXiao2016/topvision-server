<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="portal"/>
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<STYLE>
v\:* {
	Behavior: url(#default#VML)
}
</STYLE>
<title>Simple jsp page</title>
</head>
<body>
	<div id=location>
		<table>
			<tr>
				<td id="vml"><s:property value="vmlStr" escape="false" /></td>
			</tr>
		</table>
		<table width=100% cellspacing=5>
			<tr>
				<td width=80 align="right"><fmt:message bundle="${portal}" key="entitySnapPage.contactUser" />:</td>
				<td width=10></td>
				<s:if test="entity.sysContact!=''">
					<td colspan=2><s:property value="entity.sysContact" />
					</td>
				</s:if>
				<s:else>
					<td colspan=2><fmt:message bundle="${portal}" key="entitySnapPage.noContactUser" /></td>
				</s:else>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle="${portal}" key="entitySnapPage.deviceAddr" />:</td>
				<td width=10></td>
				<s:if test="entity.sysLocation!=''">
					<td colspan=2><s:property value="room.cnName" />
					</td>
				</s:if>
				<s:else>
					<td colspan=2><fmt:message bundle="${portal}" key="entitySnapPage.noDeviceAddr" /></td>
				</s:else>
			</tr>
		</table>
	</div>
</body>
</html>