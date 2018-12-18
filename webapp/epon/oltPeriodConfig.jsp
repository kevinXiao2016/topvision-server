<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<TITLE><fmt:message bundle="${i18n}" key="PERF.perfPeridCfg" /></TITLE>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>; 
var topPerfStatOLTCycle = '<s:property value='perfStatCycle.topPerfStatOLTCycle'/>';
var topPerfStatONUCycle = '<s:property value='perfStatCycle.topPerfStatONUCycle'/>';
function saveClick(){
	var oltcle = $('#olt').val();
	var onucle = $('#onu').val();
	var oltTemp = $('#oltTemp').val();
	var onuTemp = $('#onuTemp').val();
	if(oltTemp < 1 || oltTemp > 10){
		Zeta$("oltTemp").focus();
		return ;
	}
	if(onuTemp < 1 || onuTemp > 10){
		Zeta$("onuTemp").focus();
		return ;
	}
	if(!checkInput(oltTemp)){
		Zeta$("oltTemp").focus();
		return ;
	}
	if(!checkInput(onuTemp)){
		Zeta$("onuTemp").focus();
		return ;
	}
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.PERF.modifyConfirm, function (type) {
	       if(type=="ok"){
	    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving, 'ext-mb-waiting');
	    		Ext.Ajax.request({
	    			url : '/epon/perf/savePerfStatCycle.tv?r=' + Math.random(),
	    			success : function(text) {
	    				if (text.responseText != null && text.responseText != "success") {  
	    					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyCollectPeroidEr  , 'error');
	    				} else {
	    	                 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyCollectPeroidOk );
	    	                 cancelClick();
	    			}
	    				 
	    			},
	    			failure : function() {
	    				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.modifyCollectPeroidEr );
	    			},
	    			params : {
	    				entityId: entityId,
	    				topPerfStatOLTCycle: oltcle,
	    				topPerfStatONUCycle:onucle,
	    				topPerfOLTTemperatureCycle:oltTemp,
	    				topPerfONUTemperatureCycle:onuTemp
	    			}
	    		});
	           }
			})
}
function cancelClick() {
	window.parent.closeWindow('periodConfig');	
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
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching , 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/perf/refreshPerfStatCycle.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "success") {  
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
function loadCycle(){
	$("#olt").val(topPerfStatOLTCycle);
	$("#onu").val(topPerfStatONUCycle);
}
</script>
</HEAD>
<BODY style="margin: 10pt 10pt 10pt 10pt;" class=POPUP_WND onload="loadCycle();">
	<div class=formtip id=tips style="display: none"></div>
	<div style="height: 200px; boder: 5px;">
		<fieldset
			style='width: 100%; height: 180px; background-color: #ffffff; padding-top:10px;'>
			<table cellspacing=12 cellpadding=0>
				<tr height=20px>
					<td width=140px align=center><label><fmt:message bundle="${i18n}" key="PERF.oltTempPeriod" />:</label></td>
					<td align=center> 
						<select id="olt" style="width:132px;">
							<option value="5">5</option>
    						<option value="10">10</option>
    						<option value="15">15</option>
   							<option value="30">30</option>
   							<option value="60">60</option>
   							<option value="100">100</option>
   							<option value="300">300</option>
   							<option value="900">900</option>
						</select>
						&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.S" />
					</td>
				</tr>
				<tr height=20px>
					<td width=140px align=center><label><fmt:message bundle="${i18n}" key="PERF.onuTempPeriod" />:</label></td>
						<td align=center>
						<select id="onu" style="width:132px;">
							<option value="5">5</option>
    						<option value="10">10</option>
    						<option value="15">15</option>
   							<option value="30">30</option>
   							<option value="60">60</option>
   							<option value="100">100</option>
   							<option value="300">300</option>
   							<option value="900">900</option>
						</select>
						&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.S" />
					</td>
				</tr>
				<tr height=20px>
					<td width=140px align=center><label><fmt:message bundle="${i18n}" key="PERF.oltTempPeriod" />:</label></td>
			        <td align=center><input type="text" id="oltTemp" value="<s:property value='perfStatCycle.topPerfOLTTemperatureCycle'/>"
			        onfocus="inputFocused('oltTemp', I18N.PERF.range10, 'iptxt_focused')"
					onblur="inputBlured(this, 'oltTemp');" onclick="clearOrSetTips(this);">&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.S" /></td>
				</tr>
				<tr height=20px>
				 	<td width=140px align=center><label><fmt:message bundle="${i18n}" key="PERF.onuTempPeriod" />:</label></td>
			        <td align=center><input type="text" id="onuTemp" value="<s:property value='perfStatCycle.topPerfONUTemperatureCycle'/>"
			         onfocus="inputFocused('onuTemp',  I18N.PERF.range10, 'iptxt_focused')"
					onblur="inputBlured(this, 'onuTemp');" onclick="clearOrSetTips(this);">&nbsp;&nbsp;<fmt:message bundle="${i18n}" key="COMMON.S" />
					</td>
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