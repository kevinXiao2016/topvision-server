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
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>; 
var pppoeEnable = <s:property value='baseConfig.topOltPPPOEPlusEnable'/>;
var dhcpRelayMode = <s:property value='baseConfig.topOltDHCPRelayMode'/>;
var dhcpIpMacDynamicEnable = <s:property value='baseConfig.topOltDHCPDyncIPMACBind'/>;
function saveClick(){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.DHCP.confirmMdf, function (type) {
	       if(type=="ok"){
	    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.DHCP.modifying, 'ext-mb-waiting');
	    		Ext.Ajax.request({
	    			url : '/epon/dhcp/modifyDhcpBaseConfig.tv?r=' + Math.random(),
	    			success : function(text) {
	    				if (text.responseText != null && text.responseText != "modifyOK") {  
	    					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyFailed , 'error');
	    				} else {
	    	                 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifySuc);
	    	                 cancelClick();
	    			}
	    				 
	    			},
	    			failure : function() {
	    				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.DHCP.modifyFailed);
	    			},
	    			params : {
	    				entityId: entityId,
	    				pppoeEnable: $('#topOltPPPOEPlusEnable').val(),
	    				dhcpRelayMode:$('#topOltDHCPRelayMode').val(),
	    				dhcpIpMacDynamicEnable:$('#topOltDHCPDyncIpMacBind').val()
	    			}
	    		});
	           }
			})
}
function loadData(){
   $("#topOltPPPOEPlusEnable").attr("value",pppoeEnable);
   $("#topOltDHCPRelayMode").attr("value",dhcpRelayMode);
   $("#topOltDHCPDyncIpMacBind").attr("value",dhcpIpMacDynamicEnable);
}
function cancelClick() {
	window.parent.closeWindow('dhcpBasic');	
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
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/dhcp/refreshDhcpBaseConfig.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "refreshOK") {  
  				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr , 'error');
  			} else {
  	             window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
  	             window.location.reload();
  			} 
  		},
  		failure : function() {
  			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
  		},
  		params : {
  			entityId: entityId
  		}
  	});
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND onload="loadData()">
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 200px; boder: 5px;">
		<fieldset
			style='width: 100%; height: 150px; background-color: #ffffff'>
			<table cellspacing=20 cellpadding=8>
				<tr height=20px>
					<td width=150px align=right><label>PPPOE+ <fmt:message bundle="${i18n}" key="DHCP.mode" />:</label></td>
					<td align=center>
					<select id="topOltPPPOEPlusEnable" style="width: 100px;">
							<option value="1"><fmt:message bundle="${i18n}" key="COMMON.open" /></option>
							<option value="2"><fmt:message bundle="${i18n}" key="COMMON.close" /></option>
					</select></td>
				</tr>
				<tr height=20px>
					<td width=150px align=right><label>DHCP Relay <fmt:message bundle="${i18n}" key="DHCP.mode" />:</label></td>
			        <td align=center><select id="topOltDHCPRelayMode" style="width: 100px;">
							<option value="0"><fmt:message bundle="${i18n}" key="COMMON.close" /></option>
							<option value="1"><fmt:message bundle="${i18n}" key="DHCP.mode2F" /></option>
							<option value="2"><fmt:message bundle="${i18n}" key="DHCP.mode3F" /></option>	
					</select></td>
				</tr>
				<tr height=20px>
					<td align=right width=150px><label><fmt:message bundle="${i18n}" key="DHCP.snoopingEnable" />:</label></td>
                    <td align=center><select id="topOltDHCPDyncIpMacBind"	style="width: 100px;">
							<option value="2"><fmt:message bundle="${i18n}" key="COMMON.close" /></option>
							<option value="1"><fmt:message bundle="${i18n}" key="COMMON.open" /></option>
					</select></td>
				</tr>
			</table>
		</fieldset>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>
				&nbsp;&nbsp;
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