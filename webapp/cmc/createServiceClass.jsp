<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE>Service Flow Template Configuration</TITLE>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript"
	src="../..s/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
function listTypeChanged(){
	
}
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('createServiceClass');
}
function directionChange(){
	switch(Zeta$('classDirection').value){
	case '1':
	Zeta$('downselected1').style.display = 'block';
	Zeta$('downselected2').style.display = 'block';
	Zeta$('upselected1').style.display = 'none';
	Zeta$('upselected2').style.display = 'none';
	Zeta$('upselected3').style.display = 'none';
	Zeta$('upselected4').style.display = 'none';
	Zeta$('upselected5').style.display = 'none';	
	Zeta$('upselected6').style.display = 'none';	
	break;
	case '2':
	Zeta$('downselected1').style.display = 'none';
	Zeta$('downselected2').style.display = 'none';
	Zeta$('upselected1').style.display = 'block';
	Zeta$('upselected2').style.display = 'block';
	Zeta$('upselected3').style.display = 'block';
	Zeta$('upselected4').style.display = 'block';	
	Zeta$('upselected5').style.display = 'block';
	Zeta$('upselected6').style.display = 'block';
	break;
	}
}
function setDefaultValue(){
	Zeta$('classPriority').value = 0;
	Zeta$('classMaxTrafficRate').value = 0;
	Zeta$('classMaxTrafficBurst').value = 1522;
	Zeta$('classMinReservedRate').value = 0;
	Zeta$('classMaxConcatBurst').value = 0;
	Zeta$('classNomPollInterval').value = 0;
	Zeta$('classTolPollJitter').value = 0;
	Zeta$('classUnsolicitGrantSize').value = 0;
	Zeta$('classNomGrantInterval').value = 0;
	Zeta$('classTolGrantJitter').value = 0;
	Zeta$('classGrantsPerInterval').value = 0;
	Zeta$('classMaxLatency').value = 0;
	Zeta$('classActiveTimeout').value = 0;
	Zeta$('classAdmittedTimeout').value = 0;
}
function doOnload(){
	directionChange();
	setDefaultValue();
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
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MaxTrafficRate'));
	}
	object = Zeta$('classMaxTrafficBurst').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'TrafficBurst'));
	}
	object = Zeta$('classMinReservedRate').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MinReservedRate'));
	}
	object = Zeta$('classMinReservedPkt').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MinReservedPkt'));
	}
	object = Zeta$('classMaxConcatBurst').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MaxConcatBurst'));
	}
	object = Zeta$('classNomPollInterval').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'NomPollInterval'));
	}
	object = Zeta$('classTolPollJitter').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'TolPollJitter'));
	}
	object = Zeta$('classUnsolicitGrantSize').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'UnsolicitGrantSize'));
	}
	object = Zeta$('classNomGrantInterval').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'NomGrantInterval'));
	}
	object = Zeta$('classTolGrantJitter').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'TolGrantJitter'));
	}
	object = Zeta$('classMaxLatency').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MaxLatency'));
	}
	object = Zeta$('classActiveTimeout').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'ActiveTimeout'));
	}
	object = Zeta$('classAdmittedTimeout').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'AdmittedTimeout'));
	}
	//需做成复选框
	//object = Zeta$('classRequestPolicy').value.trim();
	object = Zeta$('classTosAndMask').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'TosAndMask'));
	}
	object = Zeta$('classTosOrMask').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'TosOrMask'));
	}
		
	$.ajax({
        url: 'cmcQos/createOrModifyServiceClassInfo.tv?cmcId=' + cmcId,
        type: 'post',
        data: jQuery(serviceClassForm).serialize(),
        success: function(json) {
        	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.text.addSuccess);
        	}, error: function(json) {
            window.top.showErrorDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.error);
        }, dataType: 'json',cache: false
    });
}
</script>
</HEAD>
<BODY class=POPUP_WND onload="doOnload()">
	<div class=formtip id=tips style="display: none"></div>
	<form id="serviceClassForm" name="serviceClass" align="center">
	<fieldset style='width: 100%; height: 80px; margin: 10px'>
					<legend>service class</legend>
	<table  cellspacing=5 cellpadding=0>	
	<tr><td>		
		<table cellspacing=5 cellpadding=0>				
			<tr>
				<td width=100px align=center>Name:</td>
				<td><input class=iptxt name="className"
					style="width: 100px; align: center"/> </td>
				<td width=100px align=center>Direction:</td>
                      <td><select id=classDirection style="width:100px" name="classDirection" 
                      onchange="directionChange()">
                      <option value="2" <s:if test="classDirection==2">selected</s:if>>up</option>	
				<option value="1" <s:if test="classDirection==1">selected</s:if>>down</option>											
                      </select></td>						
			</tr>
			<tr>
				<td width=100px align=center>Priority:</td>
				<td><input class=iptxt type=text id=classPriority 
				name="classPriority" style="width: 100px; align: center"
				value='<s:property value="classPriority"/>'
				onfocus="inputFocused('classPriority', '0-7', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"
				/></td>
				<td width=100px align=center>MaxTrafficRate:</td>
                      <td><input class=iptxt type=text id=classMaxTrafficRate 
                      name="classMaxTrafficRate"
                          style="width: 90px; align: center" 
                          />&nbsp;b/s</td>
			</tr>
			<tr>
				<td width=100px align=center>MaxTrafficBurst:</td>
                      <td><input class=iptxt type=text id=classMaxTrafficBurst 
                      name="classMaxTrafficBurst"
                          style="width: 90px; align: center" 
                          />&nbsp;B</td>
				<td width=100px align=center>MinReservedRate:</td>
				<td><input class=iptxt type=text id=classMinReservedRate 
				name="classMinReservedRate"
					style="width: 90px; align: center"/>&nbsp;b/s</td>							
			</tr>
			<tr>
				<td width=100px align=center>MinReservedPkt:</td>
                      <td><input class=iptxt type=text id=classMinReservedPkt 
                      name="classMinReservedPkt"
                      style="width: 90px; align: center" 
                      onfocus="inputFocused('classMinReservedPkt', '0-65535', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"
                          />&nbsp;B</td>
                      <td width=100px align=center id="downSelected1">MaxLatency:</td>
				<td id="downSelected2"><input class=iptxt type=text id=classMaxLatency
				name="classMaxLatency"
					style="width: 90px; align: center"
				/>&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
			</tr>
			<tr id="upSelected1">                        
				<td width=100px align=center>MaxConcatBurst:</td>
				<td><input class=iptxt type=text id=classMaxConcatBurst 
				name="classMaxConcatBurst"
					style="width: 90px; align: center"
				onfocus="inputFocused('classMaxConcatBurst', '0-65535', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"/>&nbsp;B</td>										
				<td width=100px align=center>NomPollInterval:</td>
                      <td><input class=iptxt type=text id=classNomPollInterval 
                      name="classNomPollInterval"
                          style="width: 90px; align: center" 
                          />&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
                  </tr>
			<tr id="upSelected2">
                      <td width=100px align=center>TolPollJitter:</td>
                      <td><input class=iptxt type=text id=classTolPollJitter 
                      name="classTolPollJitter"
                          style="width: 90px; align: center" 
                          />&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
			
				<td width=100px align=center>UnsolicitGrantSize:</td>
				<td><input class=iptxt type=text id=classUnsolicitGrantSize 
				name="classUnsolicitGrantSize"
					style="width: 90px; align: center"
				onfocus="inputFocused('classUnsolicitGrantSize', '0-65535', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"/>&nbsp;B</td>
                  </tr>
			<tr id="upSelected3">
					<td width=100px align=center>NomGrantInterval:</td>
                      <td><input class=iptxt type=text id=classNomGrantInterval 
                      name="classNomGrantInterval"
                          style="width: 90px; align: center" 
                      />&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>					
				<td width=100px align=center>TolGrantJitter:</td>
				<td><input class=iptxt type=text id=classTolGrantJitter
				name="classTolGrantJitter"
					style="width: 90px; align: center"/>&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>
			</tr>
			<tr id="upSelected4">
					<td width=100px align=center>GrantsPerInterval:</td>						
                      <td><input class=iptxt type=text id=classGrantsPerInterval 
                      name="classGrantsPerInterval"
                          style="width: 100px; align: center" 
                      onfocus="inputFocused('classGrantsPerInterval', '0-127', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"/></td>		
				
					<td width=100px align=center>ActiveTimeout:</td>
                      <td><input class=iptxt type=text id=classActiveTimeout 
                      name="classActiveTimeout"
                          style="width: 90px; align: center" 
                      onfocus="inputFocused('classActiveTimeout', '0-65535', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"/>&nbsp;s</td>
			</tr>
			<tr  id="upSelected5">
				<td width=100px align=center id="admittedTimeout">AdmittedTimeout:</td>
                      <td><input class=iptxt type=text id=classAdmittedTimeout 
                      name="classAdmittedTimeout"
                          style="width: 90px; align: center" 
                      onfocus="inputFocused('classAdmittedTimeout', '0-65535', 'iptxt_focused')"
				onblur="inputBlured(this, 'iptxt');"
                      onclick="clearOrSetTips(this);"/>&nbsp;s</td>
				<td width=100px align=center id="schedulingType">SchedulingType:</td>
				<td><select id=classSchedulingType style="width:100px" name="classSchedulingType">
				<option value="2" <s:if test="classSchedulingType==2">selected</s:if>>bestEffort</option>
				<option value="3" <s:if test="classSchedulingType==3">selected</s:if>>nonRealTimePollingService</option>
				<option value="4" <s:if test="classSchedulingType==4">selected</s:if>>realTimePollingService</option>
				<option value="5" <s:if test="classSchedulingType==5">selected</s:if>>unsolictedGrantServiceWithAD</option>
				<option value="6" <s:if test="classSchedulingType==6">selected</s:if>>unsolictedGrantService</option>
				</td>							
			</tr>
			<tr >
				<td width=100px align=center>TosOrMask:</td>
				<td><input class=iptxt type=text id=classTosOrMask
				name="classTosOrMask"
					style="width: 100px; align: center"/></td>
				<td width=100px align=center>TosAndMask:</td>
                      <td><input class=iptxt type=text id=classTosAndMask 
                      name="classTosAndMask"
                          style="width: 100px; align: center" 
                          /></td>						
			</tr>
			<tr>						
				<td width=100px align=center>Status:</td>
				<td style="width: 100px;"><select id="classStatus" style="width: 100px;" name="classStatus" >
                      <option value="4" <s:if test="#request.classStatus==4">selected</s:if>>createAndGo</option>
                      <option value="5" <s:if test="#request.classStatus==5">selected</s:if>>createAndWait</option>                              
                      </select></td>
			</tr>
			</table>				
		</td></tr>		
		<tr><td>			
			<table id="upselected6" >			
			<tr><td align="center">RequestPolicy:</td><td></td><td></td></tr>
			<tr><td><input type="checkbox" name="broadcastReqOpp" />broadcastReqOpp</td>
                <td><input type="checkbox" name="priorityReqMulticastReq" />  
                    priorityReqMulticastReq</td>    
                <td><input type="checkbox" name="reqDataForReq" />reqDataForReq </td>
            </tr>
            <tr><td><input type="checkbox" name="reqDataForData" />  
                        reqDataForData </td>
                <td><input type="checkbox" name="piggybackReqWithData" />  
                        piggybackReqWithData </td>
                <td><input type="checkbox" name="concatenateData" />  
                        concatenateData</td> 
            <tr><td><input type="checkbox" name="fragmentData" />  
                        fragmentData</td> 
                <td><input type="checkbox" name="suppresspayloadheaders" />  
                        suppresspayloadheaders</td>   
                <td><input type="checkbox" name="dropPktsExceedUGSize" />  
                        dropPktsExceedUGSize</td>
			</tr>
			</table>
		</td></tr>				
		</table>
		</fieldset>
		</form>	
		<table>	
			<tr>
				<td width=300px></td>
				<td colspan=2 height=41 align=right>
					<button id=saveBtn class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onmousedown="this.className='BUTTON_PRESSED75'"
						onclick="saveClick()"><fmt:message bundle='${cmc}' key='CMC.button.save'/></button>&nbsp;
					<button class=BUTTON75
						onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'"
						onmousedown="this.className='BUTTON_PRESSED75'"
						onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></button>
				</td>
			</tr>				
		</table>
</BODY>
</HTML>
