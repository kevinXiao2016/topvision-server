<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="config"/>
<script type="text/javascript" 
src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>; 
function saveClick(){
	
}
function cancelClick() {
	window.parent.closeWindow('dhcpIpMacStaticModify');	
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
function checkMac(mac){
	var reg_name = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){0,5})+$/i;
	if(mac == "" ||mac == null){
		return false;
	}else{
		if (mac.length != 17) {
			return false;
		} else {
			if(reg_name.test(mac)){
				return true;
			}else{
				return false;
			}
		}
	}
}
function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
    	okClick();
    }
}
function doOnload(){
	var ip = '<s:property value="dhcpIpMacStaticIp"/>';
	var ipInput = new ipV4Input("ip", "span1");
	ipInput.width(139);
	setIpValue("ip",ip);
}
</script>
<style type="text/css">
.ipTextField input {
	ime-mode: disabled;
	width: 20px;
	border: 0px;
	text-align: center;
}
</style>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND onload="doOnload()">
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 300px; boder: 5px;">
		<fieldset
			style='width: 100%; height: 155; background-color: #ffffff'>
			<table cellspacing=10 cellpadding=0>
				<tr height=20px>
					<td width=120px align=right><label>ID:</label></td>
					<td align=left><INPUT id="dhcpIpMacStaticIdx" value="<s:property value="dhcpIpMacStaticIdx"/>" disabled></td>
				</tr>
					<tr height=20px>
					<td width=120px align=right><label><fmt:message bundle="${i18n}" key="DHCP.staticIp" />:</label></td>
			    	<td align=left>
			    		<span id="span1"></span>
			    	</td>
				</tr>
				<tr height=20px>
					<td width=120px align=right><label><fmt:message bundle="${i18n}" key="DHCP.neMax" />:</label></td>
			    	<td align=left><INPUT id="dhcpIpMacStaticMac" value="<s:property value="dhcpIpMacStaticMac"/>"></td>
				</tr>
				<tr height=20px>
					<td align=right width=120px><label>ONU Mac:</label></td>
                 	<td align=left><INPUT id="dhcpIpMacStaticOnuMac" value="<s:property value="dhcpIpMacStaticOnuMac"/>"></td>
				</tr>
			</table>
		</fieldset>
		<div align="right" style="padding-right: 5px; padding-top: 5px;">
			<button id="okBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle="${i18n}" key="COMMON.save" /></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>

	</div>
</BODY>
</HTML>