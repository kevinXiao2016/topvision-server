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
	var serverIp = getIpValue('ip');
	if (!ipIsFilled("ip")) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.ipIsNotNull);
		ipFocus("ip");
		return;
	}
	var mac = $('#mac').val();
	if(!checkMac(mac)){
		Zeta$("mac").focus();
		return;
	}
	var onuMac = $('#onuMac').val();
	if(!checkMac(onuMac)){
		Zeta$("onuMac").focus();
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.modifyingBand, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/dhcp/addDhcpIpMacStaticConfig.tv?r=' + Math.random(),
		success : function(text) {
			if (text.responseText != null && text.responseText != "addOK" && text.responseText != "addConfigExists" ) {  
				 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyFailed , 'error');
			    }else if(text.responseText == 'addConfigExists'){
			     window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.ipIsUsed , 'error');
				}else {
                 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifySuc);
                 window.parent.getWindow("dhcpIpMacStatic").body.dom.firstChild.contentWindow.store.load();
                 cancelClick();
		}
			 
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyFailed);
		},
		params : {
			entityId: entityId,
			dhcpIpMacStaticIp:serverIp,
			dhcpIpMacStaticMac:mac,
			dhcpIpMacStaticOnuMac: onuMac
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('dhcpIpMacStaticAdd');	
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
	var reg_name = /^([0-9a-f]{2})(([-:][0-9a-f]{2}){0,5})+$/i;
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
Ext.onReady(function(){
	var ip = new ipV4Input("ip", "span1");
	ip.width(139);
	ip.height(18);
});
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
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND>
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 250px; boder: 5px;">
		<fieldset
			style='width: 100%; height: 155px; background-color: #ffffff'>
			<table cellspacing=20 cellpadding=0>
				<tr height=20px>
					<td width=100px align=right><label><fmt:message bundle="${i18n}" key="DHCP.staticIp" />:</label>
					</td>
					<td align=left>
						<span id="span1"></span>
					</td>
				</tr>
				<tr height=20px>
					<td width=100px align=right><label><fmt:message bundle="${i18n}" key="DHCP.ne" /> Mac:</label>
					</td>
					<td align=left><INPUT id="mac" value=""
						onfocus="inputFocused('mac', '<fmt:message bundle="${i18n}" key="DHCP.maxFormat" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);">
					</td>
				</tr>
				<tr height=20px>
					<td width=100px align=right><label>ONU Mac:</label>
					</td>
					<td align=left><INPUT id="onuMac" value=""
						onfocus="inputFocused('onuMac', '<fmt:message bundle="${i18n}" key="DHCP.maxFormat" />', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
						onclick="clearOrSetTips(this);">
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