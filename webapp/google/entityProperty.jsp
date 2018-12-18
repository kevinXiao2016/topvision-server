<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.google.resources" var="google"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.google.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../google/entityProperty.js"></script>
<script type="text/javascript">
var entityId = <s:property value="googleEntity.entityId"/>;
var icon48 = '../images/<s:property value="googleEntity.icon48"/>';
var ip = '<s:property value="googleEntity.ip"/>';
var name = '<s:property value="googleEntity.name"/>';
</script>
</head>
<body class=BLANK_WND style="padding: 5px">
	<center>
		<form name="entityForm">
			<input type=hidden name="entityId"
				value="<s:property value="entityId"/>"> <input type=hidden
				name="googleEntity.entityId" value="<s:property value="entityId"/>">
			<table width=310px cellspacing=5 cellpadding=0>
					<tr>
						<td>
							<fieldset>
								<legend><fmt:message key="GOOGLE.nodeProperty" bundle="${google}"/></legend>
								<table width=100% cellspacing=5 cellpadding=0>
									<tr>
										<td><fmt:message key="GOOGLE.alias" bundle="${google}"/>: <font color=red>*</font></td>
										<td><input id="name" type=text class=iptxt
											name="googleEntity.name" maxlength=32
											value="<s:property value="googleEntity.name"/>"
											style="width: 200px">
										</td>
									</tr>
									<tr>
										<td><fmt:message key="GOOGLE.href" bundle="${google}"/>:</td>
										<td><textarea id="url" class=iptxa name="googleEntity.url"
												rows=3 style="overflow: auto; width: 200px"><s:property value="googleEntity.url" /></textarea></td>
									</tr>
									<tr>
										<td><fmt:message key="GOOGLE.defaultLevel" bundle="${google}"/>:</td>
										<td><input id="zoom" type=text class=iptxt
											name="googleEntity.zoom"
											value="<s:property value="googleEntity.zoom"/>"
											style="width: 200px">
										</td>
									</tr>
									<tr>
										<td><fmt:message key="GOOGLE.minShowLevel" bundle="${google}"/>:</td>
										<td><input id="minZoom" type=text class=iptxt
											name="googleEntity.minZoom"
											value="<s:property value="googleEntity.minZoom"/>"
											style="width: 200px">
										</td>
									</tr>
									<tr>
										<td><fmt:message key="GOOGLE.maxShowLevel" bundle="${google}"/>:</td>
										<td><input id="maxZoom" type=text class=iptxt
											name="googleEntity.maxZoom"
											value="<s:property value="googleEntity.maxZoom"/>"
											style="width: 200px">
										</td>
									</tr>
									<tr>
										<td colspan=2 align=right>
											<button id=selectBt class=BUTTON95
												onMouseOver="this.className='BUTTON_OVER95'"
												onMouseOut="this.className='BUTTON95'"
												onMouseDown="this.className='BUTTON_PRESSED95'"
												onclick="onSaveClick();"><fmt:message key="GOOGLE.modifyProperty" bundle="${google}"/></button></td>
									</tr>
									<tr>
										<td><fmt:message key="COMMON.icon" bundle="${resource}"/>:</td>
										<td style="padding-top: 5px"><img id="entityIcon"
											src="../images/<s:property value="googleEntity.icon48"/>" border=0>
										</td>
									</tr>

									<tr>
										<td colspan=2 align=right>
											<button id=changeBt class=BUTTON95
												onMouseOver="this.className='BUTTON_OVER95'"
												onMouseOut="this.className='BUTTON95'"
												onMouseDown="this.className='BUTTON_PRESSED95'"
												onclick="changeIcon()"><fmt:message key="GOOGLE.modifyIcon" bundle="${google}"/></button></td>
									</tr>

									<tr>
										<td colspan=2 align=right>
											<button id=resetBt style="margin-top: 5px;" class=BUTTON95
												onMouseOver="this.className='BUTTON_OVER95'"
												onMouseOut="this.className='BUTTON95'"
												onMouseDown="this.className='BUTTON_PRESSED95'"
												onclick="resetIcon()"><fmt:message key="GOOGLE.resetIcon" bundle="${google}"/></button></td>
									</tr>
								</table>
							</fieldset></td>
					</tr>
			</table>
		</form>
		<br>
	</center>
</body>
</html>
