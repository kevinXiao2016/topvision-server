<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var pingIntervalOfNormal = <s:property value="pingIntervalOfNormal"/>/60000;
var flowIntervalOfNormal = <s:property value="flowIntervalOfNormal"/>/60000;
var systemIntervalOfNormal = <s:property value="systemIntervalOfNormal"/>/60000;
var pingIntervalOfError = <s:property value="pingIntervalOfError"/>/60000;
function doOnload() {
    var datas = [];
    datas['v1'] = 0;
    datas['v2'] = 1;
    datas['v3'] = 2;
    datas['v5'] = 3;
    datas['v8'] = 4;
    datas['v10'] = 5;
    datas['v15'] = 6;
    datas['v20'] = 7;
    datas['v30'] = 8;
    datas['v45'] = 9;
    datas['v60'] = 10;
    Zeta$('pingIntervalOfNormal').selectedIndex = datas['v' + pingIntervalOfNormal];
    Zeta$('flowIntervalOfNormal').selectedIndex = datas['v' + flowIntervalOfNormal];
    Zeta$('systemIntervalOfNormal').selectedIndex = datas['v' + systemIntervalOfNormal];
    Zeta$('pingIntervalOfError').selectedIndex = datas['v' + pingIntervalOfError]; 
}
function onIntervalChanged(name,box) {
	name = parseInt(box.options[box.selectedIndex].value);
}
function okClick() {
	pingIntervalOfNormal = parseInt(Zeta$('pingIntervalOfNormal').options[Zeta$('pingIntervalOfNormal').selectedIndex].value);
	flowIntervalOfNormal = parseInt(Zeta$('flowIntervalOfNormal').options[Zeta$('flowIntervalOfNormal').selectedIndex].value);
	systemIntervalOfNormal = parseInt(Zeta$('systemIntervalOfNormal').options[Zeta$('systemIntervalOfNormal').selectedIndex].value);
	pingIntervalOfError = parseInt(Zeta$('pingIntervalOfError').options[Zeta$('pingIntervalOfError').selectedIndex].value);
	$.ajax({url: 'savePollingInterval.tv', type: 'POST', data: {'entityId':entityId, 'pingIntervalOfNormal':pingIntervalOfNormal*60000, 'flowIntervalOfNormal':flowIntervalOfNormal*60000, 'systemIntervalOfNormal':systemIntervalOfNormal*60000, 'pingIntervalOfError':pingIntervalOfError*60000}, success: function() {
		window.top.closeWindow('modalDlg');
	}, error: function() {
		window.parent.showErrorDlg();
	}, dataType: 'plain', cache: false});
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
</script>
</HEAD>
<BODY class=POPUP_WND style="padding: 10px" onload="doOnload();">
	<table cellspacing=0 cellpadding=0 width=100% height=100%>
		<tr>
			<td colspan=2 height=18 style="padding-bottom: 8px"><fmt:message key="popDevicePollingInterval.note1" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td width=160><fmt:message key="popDevicePollingInterval.note2" bundle="${resources}" /></td>
			<td width=100><select id="pingIntervalOfNormal"
				onchange="onIntervalChanged(pingIntervalOfNormal,this);"
				style="width: 126px">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="5">5</option>
					<option value="8">8</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="45">45</option>
					<option value="60">60</option>
			</select>
			</td>
			<td>&nbsp;<fmt:message key="label.minutes" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="popDevicePollingInterval.note3" bundle="${resources}" /></td>
			<td><select id="pingIntervalOfError"
				onchange="onIntervalChanged(pingIntervalOfError,this);"
				style="width: 126px">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="5">5</option>
					<option value="8">8</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="45">45</option>
					<option value="60">60</option>
			</select>
			</td>
			<td>&nbsp;<fmt:message key="label.minutes" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="popDevicePollingInterval.Portpollingtime" bundle="${resources}" /></td>
			<td><select id="flowIntervalOfNormal"
				onchange="onIntervalChanged(flowIntervalOfNormal,this);"
				style="width: 126px">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="5">5</option>
					<option value="8">8</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="45">45</option>
					<option value="60">60</option>
			</select>
			</td>
			<td>&nbsp;<fmt:message key="label.minutes" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td><fmt:message key="popDevicePollingInterval.Pollingtime" bundle="${resources}" /></td>
			<td><select id="systemIntervalOfNormal"
				onchange="onIntervalChanged(systemIntervalOfNormal,this);"
				style="width: 126px">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="5">5</option>
					<option value="8">8</option>
					<option value="10">10</option>
					<option value="15">15</option>
					<option value="20">20</option>
					<option value="30">30</option>
					<option value="45">45</option>
					<option value="60">60</option>
			</select>
			</td>
			<td>&nbsp;<fmt:message key="label.minutes" bundle="${resources}" /></td>
		</tr>
		<tr>
			<td colspan=3 valign=top align=right style="padding-top: 10px">
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'" onclick="okClick()"><fmt:message key="popDevicePollingInterval.confirm" bundle="${resources}" /></button>&nbsp;
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onclick="cancelClick()"><fmt:message key="popDevicePollingInterval.Cancel" bundle="${resources}" /></button>
			</td>
		</tr>
		<tr>
			<td height=5></td>
		</tr>
	</table>
</BODY>
</HTML>