<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<TITLE>Google Maps Key Register</TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.google.resources" var="google"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="STYLESHEET" type="text/css" href="../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script>
    function cancelClick() {
        window.parent.closeWindow('modalDlg');
    }
    function saveClick() {
    	var el = Zeta$('googleMapKey'); 
    	if (el.value.trim() == '') {
    		el.focus();
    		return;
    	}    
        var gmUrl = Zeta$('googleMapUrl').value;
        var gmKey = el.value;
        
		$.ajax({url: 'saveGoogleMapKey.tv', type: 'POST',
		data: {googleMapUrl: gmUrl, googleMapKey: gmKey},
		success: function() {
			cancelClick();
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
    }
    function googleKeyChanged(box) {
        Zeta$('okBtn').disabled = (box.value.trim() == '');
    }
</script>
</HEAD>
<BODY class=POPUP_WND>
<div class=formtip id=tips style="display: none"></div>
<table width=100% height=100%>
<tr><td align=center>
<table cellpadding=3>
<tr><td height=8><fmt:message key="GOOGLE.systemUrl" bundle="${google}"/>:<input type=text id=googleMapUrl name=googleMapUrl onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')" value="<s:property value="googleMapUrl"/>" style="width:100%;"></td></tr>
<tr><td height=8></td></tr>
<tr><td><a style="color:blue;TEXT-DECORATION: underline;" href="http://code.google.com/apis/maps/signup.html" target="_googlekey"><fmt:message key="GOOGLE.applyGoogleKey" bundle="${google}"/></a></td></tr>
<tr><td height=8></td></tr>
<tr><td><fmt:message key="GOOGLE.pasterGoogleKey" bundle="${google}"/>: <font color=red>*</font></td></tr>
<tr><td height=3></td></tr>
<tr><td><textarea id="googleMapKey" name="googleMapKey" class=iptxa rows=3
	onfocus="inputFocused('googleMapKey', '<fmt:message key="GOOGLE.googleKeyCannotEmpty" bundle="${google}"/>', 'iptxa_focused')"
	onblur="inputBlured(this, 'iptxa');" onclick="clearOrSetTips(this);" 
	style="width:400px; overflow:auto"><s:property value="googleMapKey"/></textarea></td></tr>
<tr><td height=8></td></tr>
<tr><td align=right><button id=okBtn class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="saveClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button></td></tr>
</table>
</td></tr></table>
</BODY></HTML>
