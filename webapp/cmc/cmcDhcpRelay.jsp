<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.DhcpRelayMode'/></TITLE>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript">
var cmcId = <s:property value='cmcId'/>; 
var dhcpRelayMode = '<s:property value='DHCPRelayMode'/>';
if(dhcpRelayMode == "" || dhcpRelayMode == null || dhcpRelayMode == "false"){
	dhcpRelayMode = 0;
	window.parent.showMessageDlg(I18N.CMC.title.tip, CMC.title.DhcpRelayMode, 'error');
}else{
	dhcpRelayMode = parseInt(dhcpRelayMode);
}
function saveClick(){
	window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip, I18N.CMC.text.CfgTodoingSettings, function (type) {
		if(type=="ok"){
	    	window.top.showWaitingDlg(I18N.CMC.title.wait, I18N.CMC.text.doingSettings, 'ext-mb-waiting');
	    	Ext.Ajax.request({
	    		url : '/cmc/dhcp/modifyDhcpRelayMode.tv?r=' + Math.random(),
	    		success : function(text) {
	    			if (text.responseText != "success") {
	    				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.doingSettings , 'error');
	    			} else {
	    	             window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.doingSettings);
	    	             cancelClick();
	    			}
	    		},
	    		failure : function() {
	    			window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.doingSettings);
	    		},
	    		params : {
	    			cmcId: cmcId,
	    			dhcpRelayMode:$('#DHCPRelayMode').val()
	    		}
	    	});
	    }
	});
}
function loadData(){
   $("#DHCPRelayMode").val(dhcpRelayMode);
}
function cancelClick() {
	window.parent.closeWindow('dhcpRelay');	
}
function refreshClick(){
  	window.top.showWaitingDlg(I18N.CMC.title.wait, I18N.text.refreshCmTip, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/cmc/dhcp/refreshDhcpRelayMode.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "refreshOK") {  
  				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.refreshFailed , 'error');
  			} else {
  	             window.parent.showMessageDlg(I18N.CMC.title.tip,text.refreshSuccessTip);
  	           window.location.href=window.location.href;
  			} 
  		},
  		failure : function() {
  			window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.refreshFailed);
  		},
  		params : {
  			cmcId: cmcId
  		}
  	});
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND onload="loadData()">
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 100px; boder: 5px;">
		<fieldset style='width: 100%; height: 60px; background-color: #ffffff'>
			<table cellspacing=20 cellpadding=8>
				<tr height=20px>
					<td width=150px align=right><label><fmt:message bundle='${cmc}' key='CMC.title.DhcpRelayMode'/>:</label></td>
			        <td align=center><select id="DHCPRelayMode" style="width: 70px;">
							<option value="0"><fmt:message bundle='${cmc}' key='CMC.text.down'/></option>
							<option value="1"><fmt:message bundle='${cmc}' key='CMC.text.secondaryMode'/></option>
							<option value="2"><fmt:message bundle='${cmc}' key='CMC.text.thirdMode'/></option>
					</select></td>
				</tr>
			</table>
		</fieldset>
		<div align="right" style="padding-right: 5px; padding-top: 8px;">
			<button id="refreshBt" class=BUTTON95
				onMouseOver="this.className='BUTTON_OVER95'"
				onMouseOut="this.className='BUTTON95'"
				onMouseDown="this.className='BUTTON_PRESSED95'"
				onclick="refreshClick()"><fmt:message bundle='${cmc}' key='text.refreshData'/></button>
			&nbsp;&nbsp;
			<button id="okBt" class=BUTTON75
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle='${cmc}' key='CMC.label.savesetting'/></button>
			&nbsp;&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.label.close'/></button>
		</div>
	</div>
</BODY>
</HTML>