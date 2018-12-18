<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/IpTextField.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var folderId = <s:property value="topoFolder.folderId"/>;
var superiorId = <s:property value="topoFolder.superiorId"/>;
var folderName = '<s:property value="topoFolder.name"/>';
var folderUrl = '<s:property value="topoFolder.url"/>';
var folderBgPosition = <s:property value="topoFolder.backgroundPosition"/>;
var backgroundImg = '<s:property value="topoFolder.backgroundImg"/>';
var backgroundColor = '<s:property value="topoFolder.backgroundColor"/>';
var linkColor = '<s:property value="topoFolder.linkColor"/>';
var linkWidth = '<s:property value="topoFolder.linkWidth"/>';
var iconSize = <s:property value="topoFolder.iconSize"/>;
var subnetIp = '<s:property value="topoFolder.subnetIp"/>';
var subnetMask = '<s:property value="topoFolder.subnetMask"/>';
<s:if test="topoFolder.folderId>4&&topoFolder.type==5">
var subnetFlag = true;
</s:if><s:else>
var subnetFlag = false;
</s:else>
var second = 'sec';
var minute = 'min';
<%boolean topoEditPower = uc.hasPower("topoEdit");%>
var topoEditPower = <%=topoEditPower%>;
</script>
<script type="text/javascript" src="../network/topoFolderProperty.js"></script>
</head>
<body class=BLANK_WND style="padding: 5px;" onload="doOnload()">
	<form id="folderForm" name="folderForm" onsubmit="return false;" align="center">
		<input type=hidden name="topoFolder.folderId" value="<s:property value="topoFolder.folderId"/>"> 
		<input type=hidden id="subnetIp" name="topoFolder.subnetIp"	value="<s:property value="topoFolder.subnetIp"/>"> 
		<input type=hidden id="subnetMask" name="topoFolder.subnetMask"	value="<s:property value="topoFolder.subnetMask"/>">
		<table width=320px cellspacing=5 cellpadding=0>
			<tr>
				<td align=center>
					<fieldSet>
						<legend><fmt:message key="NETWORK.nodeProperty" bundle="${resources}" /></legend>
						<table width=100% cellspacing=5>
							<tr>
								<td width=110><fmt:message key="label.type" bundle="${resources}" /></td>
								<td><fmt:message key="subnet" bundle="${resources}" /></td>
							</tr>
							<tr>
								<td><fmt:message key="subnet.ip" bundle="${resources}" /></td>
								<td>
									<div id="ip" class=ipTextField style="width: 170px;">
										<table cellspacing=0 cellpadding=0>
											<tr>
												<td><input type=text id="ip1" name="ip1" maxlength=3
													onbeforepaste="ipmask_c('ip')"
													onkeydown="return ipmask(this, event, 'ip2')"
													onkeyup="ipmaskup(this, event, 'ip2')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="ip2" name="ip2" maxlength=3
													onbeforepaste="ipmask_c('ip')"
													onkeydown="return ipmask(this, event, 'ip3')"
													onkeyup="ipmaskup(this, event, 'ip3')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="ip3" name="ip3" maxlength=3
													onbeforepaste="ipmask_c('ip')"
													onkeydown="return ipmask(this, event, 'ip4')"
													onkeyup="ipmaskup(this, event, 'ip4')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="ip4" name="ip4" maxlength=3
													onbeforepaste="ipmask_c('ip')"
													onkeydown="return ipmask(this, event, 'mask1')"
													onkeyup="ipmaskup(this, event, 'mask1')"></td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td><fmt:message key="subnet.mask" bundle="${resources}" /></td>
								<td>
									<div id="mask" class=ipTextField style="width: 170px;">
										<table cellspacing=0 cellpadding=0>
											<tr>
												<td><input type=text id="mask1" name="mask1"
													maxlength=3 onbeforepaste="ipmask_c('mask')"
													onkeydown="return ipmask(this, event, 'mask2')"
													onkeyup="ipmaskup(this, event, 'mask2')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="mask2" name="mask2"
													maxlength=3 onbeforepaste="ipmask_c('mask')"
													onkeydown="return ipmask(this, event, 'mask3')"
													onkeyup="ipmaskup(this, event, 'mask3')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="mask3" name="mask3"
													maxlength=3 onbeforepaste="ipmask_c('mask')"
													onkeydown="return ipmask(this, event, 'mask4')"
													onkeyup="ipmaskup(this, event, 'mask4')"></td>
												<td class=dot align=center>.</td>
												<td><input type=text id="mask4" name="mask4"
													maxlength=3 onbeforepaste="ipmask_c('mask')"
													onkeydown="return ipmask(this, event, 'note')"
													onkeyup="ipmaskup(this, event, 'note')"></td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
							<tr>
								<td><fmt:message key="label.name" bundle="${resources}" /><font color=red>*</font></td>
								<td align=right><input id="name" class=iptxt
									<s:if test="topoFolder.folderId<5">readonly</s:if>
									name="topoFolder.name" style="width: 200px;" type=text
									maxlength=24 value="<s:property value="topoFolder.name"/>">
								</td>
							</tr>
							<tr>
								<td><fmt:message key="NETWORK.href" bundle="${resources}" />:</td>
								<td align=right><textarea id="url" class=iptxa rows=3
										name="topoFolder.url" style="width: 200px; overflow: auto">
										<s:property value="topoFolder.url" />
									</textarea>
								</td>
							</tr>
							<tr>
								<td><fmt:message key="label.link.note" bundle="${resources}" /></td>
								<td align=right><textarea class=iptxa rows=3
										name="topoFolder.note" style="width: 200px; overflow: auto">
										<s:property value="topoFolder.note" />
									</textarea>
								</td>
							</tr>
							<%
							    if (topoEditPower) {
							%>
							<tr>
								<td colspan=2 align=right><button class=BUTTON75
										onMouseOver="this.className='BUTTON_OVER75'"
										onMouseOut="this.className='BUTTON75'"
										onMouseDown="this.className='BUTTON_PRESSED75'"
										onclick="onSaveClick()"><fmt:message key="herfFolderProperty.modify" bundle="${resources}" /></button>
								</td>
							</tr>
							<%
							    }
							%>

							<tr>
								<td><fmt:message key="herfFolderProperty.Nodeicon" bundle="${resources}" /></td>
								<td><img id="icon"
									src="<s:property value="topoFolder.icon"/>" border=0
									align=absmiddle></td>
							</tr>
							<%
							    if (topoEditPower) {
							%>
							<tr>
								<td colspan=2 align=right>
									<button class=BUTTON75 style="margin-left: 72px;"
										onMouseOver="this.className='BUTTON_OVER75'"
										onMouseOut="this.className='BUTTON75'"
										onMouseDown="this.className='BUTTON_PRESSED75'"
										onclick="popIconFile()"><fmt:message key="herfFolderProperty.select" bundle="${resources}" /></button></td>
							</tr>
							<%
							    }
							%>
							<tr>
								<td colspan=2><input id="fixed" type=checkbox
									<s:if test='%{topoFolder.fixed=="1"}'>checked</s:if>
									onclick="fixNodeLocation(this);"><label for="fixed"><fmt:message key="NETWORK.fixNodeLocation" bundle="${resources}" /></label>
								</td>
							</tr>
						</table>
					</fieldSet></td>
			</tr>
			<tr>
				<td align=center>
					<fieldSet>
						<legend><fmt:message key="label.showProperty" bundle="${resources}" /></legend>
						<table width=100% cellspacing=5>
							<tr>
								<Td colspan=2><input type=radio id="displayLabelName"
									name="displayLabel"
									<s:if test="topoFolder.displayName">checked</s:if>
									onclick="setDisplayNameLabel(true)"><label
									for="displayLabelName"><fmt:message key="subnetProperty.Displaydevicealiases" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<Td colspan=2><input type=radio id="displayLabelIp"
									name="displayLabel"
									<s:if test="!topoFolder.displayName">checked</s:if>
									onclick="setDisplayNameLabel(false)"><label
									for="displayLabelIp"><fmt:message key="subnetProperty.DisplaydeviceIPaddress" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<Td colspan=2><input id="displayEntityLabel" type=checkbox
									<s:if test="topoFolder.displayEntityLabel">checked</s:if>
									onclick="onDisplayEntityLabelClick(this)"><label
									for="displayEntityLabel"><fmt:message key="label.showDeviceMark" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<Td colspan=2><input id="displayCluetip" type=checkbox
									<s:if test="topoFolder.displayCluetip">checked</s:if>
									onclick="onDisplayCluetipClick(this)"><label
									for="displayCluetip"><fmt:message key="label.showFloatWindow" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<Td colspan=2><input id="displayAlertIcon" type=checkbox
									<s:if test="topoFolder.displayAlertIcon">checked</s:if>
									onclick="onDisplayAlertIconClick(this)"><label
									for="displayAlertIcon"><fmt:message key="label.showNodeWarnIcon" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<td colspan=2><input id="displayGrid" type=checkbox
									<s:if test="topoFolder.displayGrid">checked</s:if>
									onclick="onDisplayGridClick(this)"><label
									for="displayGrid"><fmt:message key="subnetProperty.Displaybackgroundgrid" bundle="${resources}" /></label>
								</td>
							</tr>
							<tr>
								<td><input id="bfid" type=checkbox
									<s:if test="topoFolder.backgroundFlag">checked</s:if>
									onclick="enableBackgroundImg(this)"><label for="bfid"><fmt:message key="subnetProperty.Backgroundimage" bundle="${resources}" /></label>:</td>
								<td align=right><textarea id="backgroundImg" class=iptxa
										rows=2 style="width: 200px; overflow: hidden" readonly>
										<s:property value="topoFolder.backgroundImg" />
									</textarea>
								</td>
							</tr>
							<%
							    if (topoEditPower) {
							%>
							<tr>
								<td></td>
								<td align=right>
									<button id="imgBt" class=BUTTON75
										onMouseOver="this.className='BUTTON_OVER75'"
										onMouseOut="this.className='BUTTON75'"
										onMouseDown="this.className='BUTTON_PRESSED75'"
										onclick="popSelectFile()"><fmt:message key="herfFolderProperty.select" bundle="${resources}" /></button>
								</td>
							</tr>
							<%
							    }
							%>
							<tr>
								<td><fmt:message key="subnetProperty.Backgroundimageposition" bundle="${resources}" /></td>
								<td><select id="backgroundPosition" style="width: 75px"
									onchange="onPositionChanged(this);">
										<option value="0"><fmt:message key="GRAPH.defaultArrange" bundle="${resources}" /></option>
										<option value="1"><fmt:message key="GRAPH.middle" bundle="${resources}" /></option>
										<option value="2"><fmt:message key="GRAPH.fit" bundle="${resources}" /></option>
								</select>
								</td>
							</tr>
							<tr>
								<td width=80><fmt:message key="subnetProperty.Backgroundcolor" bundle="${resources}" /></td>
								<td><div id="bgArea" class="color-area"
										style="float:left;width:75px;background:<s:property value="topoFolder.backgroundColor"/>">&nbsp;</div>
									<%
									    if (topoEditPower) {
									%>
									<div style="float: right">
										<button class=BUTTON75
											onMouseOver="this.className='BUTTON_OVER75'"
											onMouseOut="this.className='BUTTON75'"
											onMouseDown="this.className='BUTTON_PRESSED75'"
											onclick="popSelectBackgroundColor()"><fmt:message key="herfFolderProperty.select" bundle="${resources}" /></button>
									</div> 
									<%
    									}
									%>
								</td>
							</tr>
						</table>
					</fieldSet>
				</td>
			</tr>
		</table>
	</form>
	<br>
	<br>
</body>
</html>
