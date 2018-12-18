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
var scId = <s:property value="scId"/>;
var classStatus = <s:property value="cmcQosServiceClass.classStatus"/>
function listTypeChanged(){
	
}
function initData(){
	Zeta$('classStatus').value = classStatus;
}
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('downServiceClass');
}

function saveClick() {
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
	object = Zeta$('classMaxLatency').value.trim();
	if(object == '' || isNotNumber(object)){
		window.top.showMessageDlg(I18N.CMC.tip.tipMsg, String.format(I18N.CMC.tip.formatError,'MaxLatency'));
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
        url: 'createOrModifyServiceClassInfo.tv?cmcId=' + cmcId +'&scId=' + scId,
        type: 'post',
        data: jQuery(downServiceClassForm).serialize(),
        success: function(json) {
        	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.updateBasicInfoSuccess);
        	}, error: function(json) {
            window.top.showErrorDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.error);
        }, dataType: 'json',cache: false
    });
}
</script>
</HEAD>
<BODY class=POPUP_WND onload="initData()">
	<div class=formtip id=tips style="display: none"></div>
	<form id="downServiceClassForm" name="serviceClass" align="center">
	<table  cellspacing=5 cellpadding=0>
	<tr><td>
		<fieldset style='width: 100%; height: 80px; margin: 10px'>
				<legend>service class</legend>
				<table cellspacing=5 cellpadding=0>				
					<tr>
						<td width=100px align=center>Name:</td>
						<td><input disabled class=iptxt name="className"
							style="width: 100px; align: center" 
							value='<s:property value="cmcQosServiceClass.className"/>'
							/> </td>
						<td width=100px align=center>Direction:</td>
                        <td><input disabled class=iptxt name="classDirection"                        	
							style="width: 100px; align: center" 
							value='<s:property value="cmcQosServiceClass.classDirection"/>'
							> </td>					
					</tr>
					<tr>
						<td width=100px align=center>Priority:</td>
						<td><input class=iptxt type=text id=classPriority 
						name="classPriority" style="width: 100px; align: center"
						value='<s:property value="cmcQosServiceClass.classPriority"/>'
						onfocus="inputFocused('classPriority', '0-7', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
                        onclick="clearOrSetTips(this);"
						/></td>
						<td width=100px align=center>MaxTrafficRate:</td>
                        <td><input class=iptxt type=text id=classMaxTrafficRate 
                        name="classMaxTrafficRate"
                            style="width: 90px; align: center" 
                        value='<s:property value="cmcQosServiceClass.classMaxTrafficRate"/>'
                            />&nbsp;b/s</td>
					</tr>
					<tr>
						<td width=100px align=center>MaxTrafficBurst:</td>
                        <td><input class=iptxt type=text id=classMaxTrafficBurst 
                        name="classMaxTrafficBurst"
                            style="width: 90px; align: center"
                        value='<s:property value="cmcQosServiceClass.classMaxTrafficBurst"/>' 
                            />&nbsp;B</td>
						<td width=100px align=center>MinReservedRate:</td>
						<td><input class=iptxt type=text id=classMinReservedRate 
						name="classMinReservedRate"
						value='<s:property value="cmcQosServiceClass.classMinReservedRate"/>'
							style="width: 90px; align: center"/>&nbsp;b/s</td>							
					</tr>
					<tr>
						<td width=100px align=center>MinReservedPkt:</td>
                        <td><input class=iptxt type=text id=classMinReservedPkt 
                        name="classMinReservedPkt"
                        style="width: 90px; align: center" 
                        value='<s:property value="cmcQosServiceClass.classMinReservedPkt"/>'
                        onfocus="inputFocused('classMinReservedPkt', '0-65535', 'iptxt_focused')"
						onblur="inputBlured(this, 'iptxt');"
                        onclick="clearOrSetTips(this);"
                            />&nbsp;B</td>
                        <td width=100px align=center>MaxLatency:</td>
						<td><input class=iptxt type=text id=classMaxLatency
						name="classMaxLatency"
							style="width: 90px; align: center"
						value='<s:property value="cmcQosServiceClass.classMaxLatency"/>'
						/>&nbsp;<fmt:message bundle="${i18n}" key="COMMON.microseconds" /></td>							
					</tr>
					<tr>
						<td width=100px align=center>TosOrMask:</td>
						<td><input class=iptxt type=text id=classTosOrMask
						name="classTosOrMask"
						value='<s:property value="cmcQosServiceClass.classTosOrMask"/>'
							style="width: 100px; align: center"/></td>
						<td width=100px align=center>TosAndMask:</td>
                        <td><input class=iptxt type=text id=classTosAndMask 
                        name="classTosAndMask"
                        value='<s:property value="cmcQosServiceClass.classTosAndMask"/>'
                            style="width: 100px; align: center" 
                            /></td>						
					</tr>
					<tr>						
						<td width=100px align=center>Status:</td>
						<td style="width: 100px;"><select id="classStatus" style="width: 100px;" 
						name="classStatus">
                        <option value="1">active</option>
                        <option value="2">notInService</option>   
                        <option value="3">notReady</option>                                                      
                        </select></td>
					</tr>
					</table>
				</fieldset>
		</td></tr>
		</form>
		<tr><td>
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
							onclick="cancelClick()"><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></button></td>
							
				</tr>				
			</table>
		</td></tr>
		</table>
</BODY>
</HTML>