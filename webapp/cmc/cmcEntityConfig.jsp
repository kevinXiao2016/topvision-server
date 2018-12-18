<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Device Configuration Information</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<link rel="stylesheet" type="text/css"
    href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script>
var cmcId = '<s:property value="cmcId"/>';
function updateClick() {
}
function resetClick() {
	Zeta$('entityForm').reset();
}
function pollConfig(){
	window.parent.createDialog("modalDlg", I18N.CMC.tip.pollingSet, 300, 180, "../cmc/showCmcPollingConfig.tv?cmcId =" + cmcId, null, true, true);
}
function checkSysTiming(){
 	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.confirmTimeSynchronism, function(type){
	    if(type == 'no'){
		    return;
		}
	     window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.beingTimeSynchronism, 'ext-mb-waiting');
	    Ext.Ajax.request({
		    url:"../cmc/checkCmcSysTiming.tv", 
		    success: function(text){
		    	if(text != null && text.responseText == 'success'){
		            window.parent.closeWaitingDlg();
		            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.timeSynchronismSuccess);
		        }else{
			        window.parent.closeWaitingDlg();
			        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.timeSynchronismFail);	    
		        }
		    }, failure: function(){
		        window.parent.closeWaitringDlg();
		        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.timeSynchronismFail);
			}, params: {cmcId: cmcId}
	    }); 
	}); 
}
function doOnload() {
}
</script>
</head>
<body class=BLANK_WND onload="doOnload()" style="margin: 13px;">
	<!--按摘要显示-->
	<div class=formtip id=tips style="display: none"></div>

	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td><%@ include file="entity.inc"%></td>
		</tr>
		<tr>
			<td valign=top style="padding-top: 10px" align=center>
				<form id="entityForm" name="entityForm">
					<input type=hidden name="entity.cmcId"
						value="<s:property value="curCmcEntity.cmcId"/>"> <input
						type=hidden id="oldIp" name="entity.oldIp"
						value="<s:property value="curCmcEntity.ip"/>">
					<table width=700px cellspacing=8 cellpadding=0>
						<tr>
							<td width=120 align=right><fmt:message bundle='${cmc}' key='CMC.label.deviceName'/>:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="curCmcEntity.sysName"/>">
							</td>
							<td width=120 align=right><fmt:message bundle='${cmc}' key='CMC.label.alias'/>:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="entity.name" id="name"
								value="<s:property value="curCmcEntity.name"/>"
								onfocus="inputFocused('name', '<fmt:message bundle="${cmc}" key="CMC.text.aliasNotNull"/>', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"></td>
						</tr>
						<tr>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.supervisor'/>:</td>
							<td><input class=iptxt type=text style="width: 218px"
								name="entity.duty" value="<s:property value="curCmcEntity.duty"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.title.macAddr'/>:</td>
							<td><input class=iptxt type=text disabled
								style="width: 218px" name="entity.mac"
								value="<s:property value="curCmcEntity.mac"/>">
							</td>
						</tr>
						<tr>
							<td align=right>SN:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="curCmcEntity.sn"/>">
							</td>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.channelAbility'/>:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="curCmcEntity.channelAbility"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.osVersion'/>:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="curCmcEntity.osVersion"/>">
							</td>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.hardVersion'/>:</td>
							<td><input disabled class=iptxt type=text
								style="width: 218px"
								value="<s:property value="curCmcEntity.hardVersion"/>">
							</td>
						</tr>
						<tr>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.sysDescr'/>:</td>
							<td>
							<textarea disabled class=iptxa rows=7 style="width: 218px; overflow: auto" value=""><s:property value="curCmcEntity.sysDescr" /></textarea>
							</td>
							<td align=right><fmt:message bundle='${cmc}' key='CMC.label.note'/>:</td>
							<td>
							<textarea name="entity.note" class=iptxa rows=7 style="width: 218px; overflow: auto" value=""><s:property value="curCmcEntity.note" /></textarea>
							</td>
						</tr>
					</table>
					<table width=700px cellspacing=8 cellpadding=0>
						<tr>
							<td align=right>
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="pollConfig()"><fmt:message bundle='${cmc}' key='CMC.button.pollingSet'/></button>&nbsp;
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="checkSysTiming()"><fmt:message bundle='${cmc}' key='CMC.button.timeSynchronism'/></button>&nbsp;
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="updateClick()"><fmt:message bundle='${cmc}' key='CMC.button.modifyConfig'/></button>&nbsp;
								<button id=saveBt class=BUTTON95 type="button"
									onMouseOver="this.className='BUTTON_OVER95'"
									onMouseOut="this.className='BUTTON95'"
									onMouseDown="this.className='BUTTON_PRESSED95'"
									onclick="resetClick()"><fmt:message bundle='${cmc}' key='CMC.button.reset'/></button></Td>
						</tr>
					</table>
				</form></td>
		</tr>
	</table>
</body>
</html>
