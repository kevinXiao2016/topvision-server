<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE>ServiceModelConfig</TITLE>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript"
	src="../..s/js/ext/ext-lang-<%= lang %>.js"></script>
	<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
var scId = <s:property value="scId"/>;
function initData(){
	Zeta$('classStatus').value = <s:property value="cmcQosServiceClass.classStatus"/>;
	Zeta$('classSchedulingType').value = <s:property value="cmcQosServiceClass.classSchedulingType"/>;
	var requestPolicy = '<s:property value="cmcQosServiceClass.docsQosServiceClassRequestPolicyString"/>';
	if(requestPolicy.indexOf("broadcastReqOpp") != -1){
		Zeta$('broadcastReqOpp').checked = true;
	}
	if(requestPolicy.indexOf("priorityReqMulticastReq") != -1){
		Zeta$('priorityReqMulticastReq').checked = true;
	}
	if(requestPolicy.indexOf("reqDataForReq") != -1){
		Zeta$('reqDataForReq').checked = true;
	}	
	if(requestPolicy.indexOf("reqDataForData") != -1){
		Zeta$('reqDataForData').checked = true;
	}	
	if(requestPolicy.indexOf("piggybackReqWithData") != -1){
		Zeta$('piggybackReqWithData').checked = true;
	}	
	if(requestPolicy.indexOf("concatenateData") != -1){
		Zeta$('concatenateData').checked = true;
	}	
	if(requestPolicy.indexOf("fragmentData") != -1){
		Zeta$('fragmentData').checked = true;
	}	
	if(requestPolicy.indexOf("suppresspayloadheaders") != -1){
		Zeta$('suppresspayloadheaders').checked = true;
	}	
	if(requestPolicy.indexOf("dropPktsExceedUGSize") != -1){
		Zeta$('dropPktsExceedUGSize').checked = true;
	}	
	
}
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('upServiceClass');
}

function saveClick() {
	//频道带宽
	var object = Zeta$('classPriority').value.trim();
	if ( object == '' || isNotNumber(object)||object<0||object>7) {
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.priorityFormatError);
		return;
	}
	object = Zeta$('classMaxTrafficRate').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'MaxTrafficRate'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classMaxTrafficBurst').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'TrafficBurst'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classMinReservedRate').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'MinReservedRate'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classMinReservedPkt').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'MinReservedPkt'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classMaxConcatBurst').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'MaxConcatBurst'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classNomPollInterval').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'NomPollInterval'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classTolPollJitter').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'TolPollJitter'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classUnsolicitGrantSize').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'UnsolicitGrantSize'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classNomGrantInterval').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'NomGrantInterval'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classTolGrantJitter').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'TolGrantJitter'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classMaxLatency').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'MaxLatency'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classActiveTimeout').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'ActiveTimeout'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classAdmittedTimeout').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'AdmittedTimeout'+I18N.CMC.tip.numberFormatError);
	}
	//需做成复选框
	//object = Zeta$('classRequestPolicy').value.trim();
	object = Zeta$('classTosAndMask').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'TosAndMask'+I18N.CMC.tip.numberFormatError);
	}
	object = Zeta$('classTosOrMask').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, 'TosOrMask'+I18N.CMC.tip.numberFormatError);
	}
	
	$.ajax({
        url: 'cmcQos/createOrModifyServiceClassInfo.tv?cmcId=' + cmcId +'&scId=' + scId,
        type: 'post',
        data: jQuery(serviceClassForm).serialize(),
        success: function(json) {
        	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.updateBasicInfoSuccess);
        	}, error: function(json) {
            window.top.showErrorDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.updateBasicInfoFailure);
        }, dataType: 'json',cache: false
    });

}
</script>
</HEAD>
<BODY class=POPUP_WND onload="initData()">
	<div class=formtip id=tips style="display: none"></div>
	<form id="serviceClassForm" name="serviceClass" align="center">
		<fieldset style='width: 100%; height: 80px; margin: 10px'>
			<legend>service class</legend>
			<table cellspacing=5 cellpadding=0>
				<tr>
					<td>
						<table cellspacing=5 cellpadding=0>
							<tr>
								<td width=100px align=center>Name:</td>
								<td><input disabled class=iptxt name="className"
									style="width: 100px; align: center"
									value='<s:property value="cmcQosServiceClass.className"/>' />
								</td>
								<td width=100px align=center>Direction:</td>
								<td><input disabled class=iptxt name="classDirection"
									style="width: 100px; align: center"
									value='<s:property value="cmcQosServiceClass.classDirection"/>'>
								</td>
							</tr>
							<tr>
								<td width=100px align=center>Priority:</td>
								<td><input class=iptxt type=text id=classPriority
									name="classPriority" style="width: 100px; align: center"
									value='<s:property value="cmcQosServiceClass.classPriority"/>'
									onfocus="inputFocused('classPriority', '0-7', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" /></td>
								<td width=100px align=center>MaxTrafficRate:</td>
								<td><input class=iptxt type=text id=classMaxTrafficRate
									name="classMaxTrafficRate" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classMaxTrafficRate"/>' />&nbsp;b/s</td>
							</tr>
							<tr>
								<td width=100px align=center>MaxTrafficBurst:</td>
								<td><input class=iptxt type=text id=classMaxTrafficBurst
									name="classMaxTrafficBurst" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classMaxTrafficBurst"/>' />&nbsp;B</td>
								<td width=100px align=center>MinReservedRate:</td>
								<td><input class=iptxt type=text id=classMinReservedRate
									name="classMinReservedRate"
									value='<s:property value="cmcQosServiceClass.classMinReservedRate"/>'
									style="width: 90px; align: center" />&nbsp;b/s</td>
							</tr>
							<tr>
								<td width=100px align=center>MinReservedPkt:</td>
								<td><input class=iptxt type=text id=classMinReservedPkt
									name="classMinReservedPkt" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classMinReservedPkt"/>'
									onfocus="inputFocused('classMinReservedPkt', '0-65535', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" />&nbsp;B</td>
								<td width=100px align=center>MaxConcatBurst:</td>
								<td><input class=iptxt type=text id=classMaxConcatBurst
									name="classMaxConcatBurst" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classMaxConcatBurst"/>'
									onfocus="inputFocused('classMaxConcatBurst', '0-65535', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" />&nbsp;B</td>

							</tr>
							<tr>
								<td width=100px align=center>NomPollInterval:</td>
								<td><input class=iptxt type=text id=classNomPollInterval
									name="classNomPollInterval" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classNomPollInterval"/>' />
									&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
								<td width=100px align=center>TolPollJitter:</td>
								<td><input class=iptxt type=text id=classTolPollJitter
									name="classTolPollJitter" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classTolPollJitter"/>' />
									&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
							</tr>
							<tr>
								<td width=100px align=center>UnsolicitGrantSize:</td>
								<td><input class=iptxt type=text id=classUnsolicitGrantSize
									name="classUnsolicitGrantSize"
									style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classUnsolicitGrantSize"/>'
									onfocus="inputFocused('classUnsolicitGrantSize', '0-65535', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" />&nbsp;B</td>
								<td width=100px align=center>NomGrantInterval:</td>
								<td><input class=iptxt type=text id=classNomGrantInterval
									name="classNomGrantInterval" style="width: 90px; align: center"
									value='<s:property value="cmcQosServiceClass.classNomGrantInterval"/>' />
									&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
							</tr>
							<tr>
								<td width=100px align=center>TolGrantJitter:</td>
								<td><input class=iptxt type=text id=classTolGrantJitter
									name="classTolGrantJitter"
									value='<s:property value="cmcQosServiceClass.classTolGrantJitter"/>'
									style="width: 90px; align: center" />&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
								<td width=100px align=center>GrantsPerInterval:</td>
								<td><input class=iptxt type=text id=classGrantsPerInterval
									name="classGrantsPerInterval"
									value='<s:property value="cmcQosServiceClass.classGrantsPerInterval"/>'
									style="width: 100px; align: center"
									onfocus="inputFocused('classGrantsPerInterval', '0-127', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" /></td>
							</tr>
							<tr>
								<td width=100px align=center>MaxLatency:</td>
								<td><input class=iptxt type=text id=classMaxLatency
									name="classMaxLatency"
									value='<s:property value="cmcQosServiceClass.classMaxLatency"/>'
									style="width: 90px; align: center" />&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
								<td width=100px align=center>ActiveTimeout:</td>
								<td><input class=iptxt type=text id=classActiveTimeout
									name="classActiveTimeout"
									value='<s:property value="cmcQosServiceClass.classActiveTimeout"/>'
									style="width: 90px; align: center"
									onfocus="inputFocused('classActiveTimeout', '0-65535', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" />&nbsp;s</td>
							</tr>
							<tr>
								<td width=100px align=center>AdmittedTimeout:</td>
								<td><input class=iptxt type=text id=classAdmittedTimeout
									name="classAdmittedTimeout"
									value='<s:property value="cmcQosServiceClass.classAdmittedTimeout"/>'
									style="width: 90px; align: center"
									onfocus="inputFocused('classAdmittedTimeout', '0-65535', 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');"
									onclick="clearOrSetTips(this);" />&nbsp;s</td>
								<td width=100px align=center>SchedulingType:</td>
								<td><select id="classSchedulingType" style="width: 100px"
									name="classSchedulingType"
									value='<s:property value="cmcQosServiceClass.classSchedulingType"/>'>
										<option value="2">bestEffort</option>
										<option value="3">noRlTmPllSvc</option>
										<option value="4">rlTimePllSvc</option>
										<option value="5">unslctGrSvcAD</option>
										<option value="6">unslctGrSvc</option></td>
							</tr>
							<tr>
								<td width=100px align=center>TosOrMask:</td>
								<td><input class=iptxt type=text id=classTosOrMask
									name="classTosOrMask"
									value='<s:property value="cmcQosServiceClass.classTosOrMask"/>'
									style="width: 100px; align: center" /></td>
								<td width=100px align=center>TosAndMask:</td>
								<td><input class=iptxt type=text id=classTosAndMask
									name="classTosAndMask"
									value='<s:property value="cmcQosServiceClass.classTosAndMask"/>'
									style="width: 100px; align: center" /></td>
							</tr>
							<tr>
								<td width=100px align=center>Status:</td>
								<td style="width: 100px;"><select id="classStatus"
									style="width: 100px;" name="classStatus"
									value='<s:property value="cmcQosServiceClass.classStatus"/>'>
										<option value="1">active</option>
										<option value="2">notInService</option>
										<option value="3">notReady</option>
								</select></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
							<tr>
								<td align="left">RequestPolicy:</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</table>
						<table>
							<tr>
								<td><input type="checkbox" name="broadcastReqOpp" />broadcastReqOpp</td>
								<td><input type="checkbox" name="priorityReqMulticastReq" />
									priorityReqMulticastReq</td>
								<td><input type="checkbox" name="reqDataForReq" />reqDataForReq
								</td>
							</tr>
							<tr>
								<td><input type="checkbox" name="reqDataForData" />
									reqDataForData</td>
								<td><input type="checkbox" name="piggybackReqWithData" />
									piggybackReqWithData</td>
								<td><input type="checkbox" name="concatenateData" />
									concatenateData</td>
							<tr>
								<td><input type="checkbox" name="fragmentData" />
									fragmentData</td>
								<td><input type="checkbox" name="suppresspayloadheaders" />
									suppresspayloadheaders</td>
								<td><input type="checkbox" name="dropPktsExceedUGSize" />
									dropPktsExceedUGSize</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fieldset>
		<table>
			<tr>
				<td width=300px></td>
				<td colspan=2 height=41 align=right>
					<button id=saveBtn class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onmousedown="this.className='BUTTON_PRESSED75'"
						onclick="saveClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.save"/></button>&nbsp;
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onmousedown="this.className='BUTTON_PRESSED75'"
						onclick="cancelClick()"><fmt:message bundle="${cmcRes}" key="CMC.button.cancel"/></button>
				</td>

			</tr>
		</table>
	</form>
</BODY>
</HTML>