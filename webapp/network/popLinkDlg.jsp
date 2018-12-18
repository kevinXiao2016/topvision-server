<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/combox/dhtmlxcombo.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script src="../js/zeta-core.js"></script>
<script>
window.dhx_globalImgPath="../js/dhtmlx/combox/imgs/";
</script>
<script src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script src="../js/dhtmlx/combox/dhtmlxcombo.js"></script>
</HEAD>
<BODY class=POPUP_WND style="margin: 10px">
<table cellspacing=8 cellpadding=0 align="center">
	<tr>
		<td><fmt:message key="popLinkDlg.note1" bundle="${resources}" /></td>
	</tr>
	<tr>
		<td><div id="combo_zone2" style="width: 440px; height: 30px;"></div>
		</td>
	</tr>

	<tr>
		<td valign=top align=right>
			<button id=okBt class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'" onclick="okClick()"><fmt:message key="popDevicePollingInterval.confirm" bundle="${resources}" /></button>&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="popDevicePollingInterval.Cancel" bundle="${resources}" /></button>
		</td>
	</tr>
	<tr>
		<td height=4></td>
	</tr>
</table>
<script>
var z = new dhtmlXCombo("combo_zone2", "alfa5", 440);
z.addOption([[1,1111],[2,2222],[3,3333],[4,4444],[5,'255.255.255.255: 25[s] - 255.1.1.1: 1[ss]']]);
//z.enableFilteringMode(true, "complete.php", true, true);
	
function okClick() {
	window.top.ZetaCallback = {type:'ok', selectedItemId : itemId};
	window.top.closeWindow('modalDlg');
}
function cancelClick() {
	window.top.ZetaCallback = {type:'cancel'};
	window.top.closeWindow('modalDlg');
}	
</script>
</BODY>
</HTML>
