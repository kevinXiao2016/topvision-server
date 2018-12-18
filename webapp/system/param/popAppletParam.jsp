<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<%@include file="../../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="sys"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script>
function addEnterKey() {
    if (event.keyCode==KeyEvent.VK_ENTER) {
        okClick();
    }
}
function okClick() {
	$.ajax({url: '../system/updateAppletConfig.tv', type: 'POST',
		data: {appletActive: Zeta$('appletActive').checked},
		success: function(json) {
			cancelClick();
		},
		error: function(json) {
			window.parent.showErrorDlg();
		},
		dataType: 'plain', cache: false});
}
function cancelClick() {
	window.parent.closeWindow('modalDlg');
}

function doOnload() {
	setTimeout("doFocusing()", 500);
}

function doFocusing() {
	//Zeta$('oldPasswd').focus();
}
</script>
</HEAD><BODY class="POPUP_WND" style="margin:10px" onload="doOnload()" onkeydown="addEnterKey(event)">
<div class=formtip id=tips style="display: none"></div>
<center><table height=100% cellspacing=8 cellpadding=0>
<tr><Td colspan=2><input id="appletActive" type=checkbox <s:if test="appletActive">checked</s:if>><fmt:message bundle="${sys}" key="sys.useApplet" /></Td></tr>
<tr><Td colspan=2><input type=checkbox><label><fmt:message bundle="${sys}" key="sys.useRMI" /></label></Td></tr>
<tr><td></td><Td><fmt:message bundle="${sys}" key="sys.addr" />:&nbsp;&nbsp;&nbsp;&nbsp;<input class=iptxt type=text style="width:120px">
&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message bundle="${sys}" key="mibble.portNum" />:&nbsp;&nbsp;&nbsp;&nbsp;<input class=iptxt type=text style="width:80px"></Td></tr>
<tr><Td width=50px><font color=red style="font-weight:bold;"><fmt:message bundle="${sys}" key="sys.notice" />: &nbsp;&nbsp;</font></td>
	<td><fmt:message bundle="${sys}" key="sys.reqJRE" /></Td>
</tr>
<tr><td></td><Td><A href="../conf/download/jre1.6.x_windows586.exe" target="_blank"><fmt:message bundle="${sys}" key="sys.downloadJRE" /><a></Td></tr>
<tr><td height=100% valign=top colspan=2></td></tr>
<tr><td colspan=2 align=right height=40px><button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onmouseup="okClick()"><fmt:message bundle="${sys}" key="sys.confirm" /></button>&nbsp;<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onclick="cancelClick()"><fmt:message bundle="${sys}" key="mibble.cancel" /></button></td></tr>
</table></center>
</BODY></HTML>
