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
	var vlanId = $('#dhcpGiaddrVid').val();
	var serverIp = getIpValue('ip');
	if (!ipIsFilled("ip")) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.giaddrNotNull);
		ipFocus$("ip");
		return;
	}
	if(!checkInput(vlanId)||vlanId > 4094 || vlanId < 1)
	{
		Zeta$("dhcpGiaddrVid").focus();
		return ;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.modifying, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/dhcp/modifyDhcpGiaddrConfig.tv?r=' + Math.random(),
		success : function(text) {
			if (text.responseText != null && text.responseText != "updateOK") {  
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyGiaddrFailed , 'error');
			}
			else{
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyGiaddrSuc);
			window.parent.getWindow("dhcpGiaddrConfig").body.dom.firstChild.contentWindow.store.load();
			cancelClick();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyGiaddrFailed);
		},
		params : {
			entityId:entityId,
			onuType: $('#onuType').val(),
			dhcpGiaddrIndex:$('#dhcpGiaddrIndex').val(),
			dhcpGiaddrIp:serverIp,
			dhcpGiaddrVid:vlanId
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpGiaddrModify');	
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
function addEnterKey(e) {
	var event = window.event || e; // for firefox
    if (event.keyCode == KeyEvent.VK_ENTER) {
    	okClick();
    }
}
function doOnload(){
	var ip = '<s:property value="dhcpGiaddrIp"/>';
	var ipInput = new ipV4Input("ip", "span1");
	ipInput.width(139);
	ipInput.height(18);
	ipInput.bgColor("white");
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
			style='width: 100%; height: 165; background-color: #ffffff'>
			<table cellspacing=14 cellpadding=0>
				<tr height=20px>
					<td width=120px align=right><label><fmt:message bundle="${i18n}" key="DHCP.eType" />:</label></td>
					<td align=left><INPUT id="onuType" value="<s:property value="onuType"/>" disabled></td>
				</tr>
					<tr height=20px>
					<td width=120px align=right><label><fmt:message bundle="${i18n}" key="DHCP.serviceGatewayId" />:</label></td>
			    	<td align=left><INPUT id="dhcpGiaddrIndex" value="<s:property value="dhcpGiaddrIndex"/>" disabled></td>
				</tr>
				<tr height=20px>
					<td width=120px align=right><label><fmt:message bundle="${i18n}" key="DHCP.allow" /> VLAN ID:</label></td>
			    	<td align=left><INPUT id="dhcpGiaddrVid" value="<s:property value="dhcpGiaddrVid"/>"
			    	maxlength=4 onfocus="inputFocused('dhcpGiaddrVid', '<fmt:message bundle="${i18n}" key="DHCP.range4094" />', 'iptxt_focused')"
							onblur="inputBlured(this, 'dhcpGiaddrVid');" onclick="clearOrSetTips(this);" ></td>
				</tr>
				<tr height=20px>
					<td align=right width=120px><label><fmt:message bundle="${i18n}" key="DHCP.gateWayAddr" />:</label></td>
                 	<td align=left>
                 		<span id="span1"></span>
                 	</td>
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