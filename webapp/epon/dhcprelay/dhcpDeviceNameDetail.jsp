<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="epon"/>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
#sequenceStep {
    padding-left : 460;position:absolute;top:290px;
}
</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript">

function checkDeviceValid(val){
	return true;
}
function okClick(){
    var deviceName = $("#deviceName-input").val().trim();
    var p = window.parent.getWindow("createRelayConfig").body.dom.firstChild.contentWindow;
    if(!checkDeviceValid(deviceName)){
    	return;
    }
    p.deviceData.push(deviceName);
    p.initDeviceSpan();
    cancelClick();
}
function cancelClick(){
	window.parent.closeWindow("deviceDetail");
}
$(function (){
	
});
</script>
</HEAD>
<BODY class="POPUP_WND">
<table style="width:92%; height: 20px; margin: 10px 10px 10px 10px; " cellspacing=0 cellpadding=0 >
<tr>
    <td><label><fmt:message bundle="${epon}" key="DHCPRELAY.extDevice"/>:</label></td>
    <td><input id="deviceName-input" type="text" width=100 maxlength=16/></td>
</tr>
</table>
<div align="right" style='margin-top:6px;'>
    <button id=okBt  class=BUTTON75 onclick='okClick()'><fmt:message bundle="${epon}" key="DHCPRELAY.sure"/></button>&nbsp;&nbsp;
    <button class=BUTTON75 onclick='cancelClick()'><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></button>
</div>
</BODY>
</HTML>