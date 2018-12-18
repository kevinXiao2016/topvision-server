<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<meta http-equiv="Expires" CONTENT="0"> 
<meta http-equiv="Cache-Control" CONTENT="no-cache"> 
<meta http-equiv="Pragma" CONTENT="no-cache">
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">

Ext.onReady(function () {
	var dlg = window.top.Ext.WindowMgr.get('modalDlg'),
	    $body = $(top.document.body),
	    winW = 420,
	    winH = 140,
	    w = ($body.outerWidth() - winW) / 2,
	    h = ($body.outerHeight() - winH) / 2;
	
	dlg.setSize(winW, winH);
	if(w > 0 && h > 0){
		dlg.setPosition (w,h);
	}
});

function cancelClick() {
	window.top.closeWindow('modalDlg');
}

/**
 * 禁用页面backspace后退
 */
function backspace(evt){
	evt=evt?evt:window.event;

	if (evt.keyCode == 8 ){
		event.returnValue=false;
		return false;
	}
}
 
</script>
</HEAD>
<BODY class=POPUP_WND style="margin: 5px"  onkeydown="backspace(event)" >
	<table width=100% cellspacing=8 cellpadding=0>
		<tr>
			<td><fmt:message key="ALERT.noCurrentEvent" bundle="${fault}"/></td>
		</tr>
	
		<tr>
			<td align="center">
				<button class=BUTTON75 type="button"
					onMouseOver="this.className='BUTTON_OVER75'"
					onMouseDown="this.className='BUTTON_PRESSED75'"
					onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="COMMON.close" bundle="${resource}"/></button>
			</td>
		</tr>
	</table>
</BODY>
</HTML>