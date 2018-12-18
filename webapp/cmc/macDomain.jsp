<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Mac domain info</title>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
function updateClick() {
	$.ajax({
        url: 'modifyMacDomainBaseInfo.tv?cmcId=' + cmcId,
        type: 'post',
        data: jQuery(macDomainForm).serialize(),
        success: function(response) {
        	if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.updateBasicInfoSuccess);
			 }else{
				 window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.updateBasicInfoFailure);
				 }
        	}, error: function(response) {
        	window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.updateBasicInfoFailure);
        }, dataType: 'json',cache: false
    });
}
function resetClick() {
	Zeta$('macDomainForm').reset();
}
function refreshInfo(){
	$.ajax({
	    url: 'refreshMacDomainInfo.tv?cmcId=' + cmcId ,
	    type: 'post',
	    success: function(response) {
	    	if(response){
	    		window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.refreshSuccess);
				 }else{
					 window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.refreshFailure);
				 }
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.entity.alert.TipTitle, I18N.CMC.tip.refreshFailure);
			}, cache: false
		}); 
}
</script>
</head>
<body class=BLANK_WND style="margin: 15px;">
	<table>
	<tr>
	<td valign=top><%@ include file="entity.inc"%></td>
	</tr>
	<tr><td>
	<form id="macDomainForm" name="macDomainForm">
	<table width=100% cellspacing=5 cellpadding=0>
		<tr style="pading-top: 15px;">
			<td>
				<fieldset style='width: 100%; height: 80px; margin: 10px'>
					<legend><fmt:message bundle="${cmcRes}" key="CMC.title.statusMsg"/></legend>
					<table cellspacing=5 cellpadding=0>
						<tr>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.invalidRangeReq"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled 
								value="<s:property value="macDomainStatusInfo.invalidRangeReqs"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.rangingAborted"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled 
								value="<s:property value="macDomainStatusInfo.rangingAborteds"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.invalidRegReq"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled
								value="<s:property value="macDomainStatusInfo.invalidRegReqs"/>"></td>
						</tr>
						<tr>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.failedRegReq"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled
								value="<s:property value="macDomainStatusInfo.failedRegReqs"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.invalidDataReq"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled
								value="<s:property value="macDomainStatusInfo.invalidDataReqs"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.t5Timeout"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled
								value="<s:property value="macDomainStatusInfo.t5Timeouts"/>"></td>
						</tr>
					</table>
				</fieldset></td>
		</tr>
		<tr>
			<td>
				<fieldset style='width: 100%; height: 150px; margin: 10px'>
					<legend><fmt:message bundle="${cmcRes}" key="text.baseInfo"/></legend>
					<table cellspacing=5 cellpadding=0>
						<tr>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.macCapability"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled name="macDomainBaseInfo.docsIfCmtsCapabilitiesString"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsCapabilitiesString"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.syncInterval"/>(ms):</td>
							<td><input class=iptxt type=text style="width: 120px" name="macDomainBaseInfo.docsIfCmtsSyncInterval"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsSyncInterval"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.ucdInterval"/>(ms):</td>
							<td><input class=iptxt type=text style="width: 120px" name="macDomainBaseInfo.docsIfCmtsUcdInterval"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsUcdInterval"/>"></td>
						</tr>
						<tr>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.maxServiceIds"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled name="macDomainBaseInfo.docsIfCmtsMaxServiceIds"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsMaxServiceIds"/>"></td>
							<!-- 改项废弃
							<td width=150 align=center>InsertionInterval:</td>							
							<td><input class=iptxt type=text style="width: 120px" name="macDomainBaseInfo.docsIfCmtsInsertionInterval"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsInsertionInterval"/>"></td>
							-->
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.invalidRangingAttempt"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" name="macDomainBaseInfo.invitedRangingAttempts"
								value="<s:property value="macDomainBaseInfo.invitedRangingAttempts"/>"></td>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.insertInterval"/>(s/100):</td>
							<td><input class=iptxt type=text style="width: 120px" name="macDomainBaseInfo.docsIfCmtsInsertInterval"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsInsertInterval"/>"></td>
						</tr>
						<tr>
							<td width=150 align=center><fmt:message bundle="${cmcRes}" key="CMC.label.macStorageType"/>:</td>
							<td><input class=iptxt type=text style="width: 120px" disabled name="macDomainBaseInfo.docsIfCmtsMacStorageTypeString"
								value="<s:property value="macDomainBaseInfo.docsIfCmtsMacStorageTypeString"/>"></td>
						</tr>
					</table>
					<div align="right" style="margin-top: 5px; margin-right:10px">
						<button id=saveBt class=BUTTON75 type="button"
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'" onclick="updateClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.save"/></button>
						&nbsp;
						<button id=saveBt class=BUTTON75 type="button"
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'" onclick="resetClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.reset"/></button>
							&nbsp;
						<button id=refresh class=BUTTON75 type="button"
							onMouseOver="this.className='BUTTON_OVER75'"
							onMouseOut="this.className='BUTTON75'"
							onMouseDown="this.className='BUTTON_PRESSED75'" onclick="refreshInfo()"><fmt:message bundle="${cmcRes}" key="CMC.button.refresh"/></button>
					</div>
				</fieldset></td>
	</table>
	</form>
	</td></tr>
	</table>
</body>
</html>