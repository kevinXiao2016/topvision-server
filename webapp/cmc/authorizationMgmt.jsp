<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<TITLE><fmt:message bundle='${cmc}' key='CMC.title.LicenseManagement'/></TITLE>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var authMgmt = ${json};

function onloadFunction() {
	var ip = new ipV4Input("ipAddress", "span1");
	ip.width(150);
	ip.onChange = function() {
		changeBt();
	}
	setIpValue("ipAddress", "0.0.0.0");
	if(authMgmt.topCcmtsAuthCurrentInfo){
		$("#authInfo").append(authMgmt.topCcmtsAuthCurrentInfo);
	}
}
function changeBt(){
	//按钮是否设置为disabled标识，如果为true设置为disabled
	var buttonStatus = false;
	if(!ipIsFilled("ipAddress") || getIpValue("ipAddress")=="0.0.0.0"){
		buttonStatus = true;
	}
	if($("#fileName").val()==null || $("#fileName").val() == ""){
		buttonStatus = true;
	}
	$("#authBT").attr("disabled", buttonStatus);
}
function authManagementSet(){
	var ipAddress = getIpValue("ipAddress");
	var fileName = $("#fileName").val().trim();
	var dataStr = "cmcId=" + cmcId
				+ "&authMgmt.ipAddress=" + ipAddress
				+ "&authMgmt.fileName=" + fileName;				
	$.ajax({
		url : '/cmcauth/cmcAuthMgmtSet.tv',
		type : 'post',
		data : dataStr,
		success : function(response) {
			if (response.message == "success") {
				getAuthManagementStatusInfo();							
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.SetSuccess);
			} else {		
				getAuthManagementStatusInfo();					
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.SetFailed);
			}
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.SetFailed);
		},
		cache : false
	});		
}
function getAuthManagementStatusInfo(){
	$.ajax({
		url : '/cmcauth/getCmcAuthStatus.tv?cmcId='+cmcId,
		type : 'post',
		success : function(response) {
			//alert(response);
			if (response.message == "success") {
				authMgmt = response.authMgmt;
			} else {							
				window.parent.showMessageDlg(I18N.CMC.title.tip, 's');
			}
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.CMC.title.tip, 's');
		},
		cache : false
	});	
}
function cancelClick(){
	window.parent.closeWindow('authMgmt');
}
</script>
</HEAD>
<BODY class=POPUP_WND
	style="padding-top: 10px; padding-left: 10px; padding-right: 10px; padding-bottom: 10px"
	onload="onloadFunction();">
	<div class=formtip id=tips style="display: none"><fmt:message bundle='${cmc}' key='CMC.text.otherccmtsisauthorized'/></div>
	<fieldset
		style="background-color: #ffffff; width: 100%; height: 100px;">
		<legend><fmt:message bundle='${cmc}' key='CMC.title.licensemanagement'/></legend>
		<table width=100% height="85%" cellspacing=0 cellpadding=0
			bgcolor="white">
			<tr>
				<td align=center style="margin-bottom: 15px; margin-top: 15px;">
					<form name="formChanged" id="formChanged">
						<table cellspacing=5 cellpadding=0>
							<tr>
								<td width=81px align=left> <fmt:message bundle='${cmc}' key='CMC.title.transportprotocol'/>:</td>
								<td style="width: 150px;"><select disabled
									id="topCcmtsAuthTransferProtocol" style="width: 150px;"
									name="topCcmtsAuthTransferProtocol"
									onclick="changeBt()">
										<option value="1" selected>tftp</option>
										<option value="2">http</option>
								</select>
								</td>
							</tr>							
							<tr>
								<td width=81px align=left><fmt:message bundle='${cmc}' key='CMC.title.serverip'/>:</td>
								<td style="width: 150px;"><span id="span1"></span>
							</tr>
							<tr>
								<td width=81px align=left><fmt:message bundle='${cmc}' key='CMC.title.filename'/>:</td>
								<td><input class=iptxt type=text id="fileName"
									name="filename"
									style="width: 150px; align: center" onkeyup="changeBt()">
								</td>
							</tr>							
						</table>
					</form>
				</td>
			</tr>
		</table>
	</fieldset>
	<div align=right
		style="margin-right: 8px; margin-bottom: 15px; margin-top: 15px;">
		<div align=right style="margin-right: 8px; margin-bottom: 15px;">
			<button id=authBT class=BUTTON75 disabled
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onmousedown="this.className='BUTTON_PRESSED75'" onclick="authManagementSet()"><fmt:message bundle='${cmc}' key='CMC.title.authorization'/></button>
			&nbsp;
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onmousedown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.title.cancel'/></button>
		</div>
	</div>
	<fieldset
		style="background-color: #ffffff; width: 100%; height: 50px;">
		<legend><fmt:message bundle='${cmc}' key='CMC.title.licenseinfor'/></legend>
		<div><fmt:message bundle='${cmc}' key='CMC.title.authorized'/>:</div>
		<div id="authInfo"></div>
		<div><fmt:message bundle='${cmc}' key='CMC.title.areauthorized'/>:</div>
		<div></div>
	</fieldset>
</BODY>