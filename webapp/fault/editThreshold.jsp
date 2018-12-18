<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
function doOnload() {
	var el = Zeta$('typeCombo');
	var options = el.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value == '<s:property value="threshold.type"/>') {
			el.selectedIndex = i;
			break;
		}
	}
	
	el = Zeta$('operator1');
	options = el.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value == '<s:property value="threshold.operator1"/>') {
			el.selectedIndex = i;
			break;
		}
	}
	
	el = Zeta$('alertLevel1');
	options = el.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value == '<s:property value="threshold.alertLevel1"/>') {
			el.selectedIndex = i;
			break;
		}
	}
	
	Zeta$('name').readOnly = true;	
}
function typeChanged(obj) {
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}
function okClick() {
	var el = Zeta$('name');
	if (el.value.trim() == '') {
		el.focus();
		return;
	}
	
	$.ajax({
		type: "POST",
		url: "../threshold/modifyThreshold.tv",
		data: jQuery(thresholdForm).serialize(),
		success: function(msg) {
			try {
				var frame = window.parent.getActiveFrame();
				frame.doRefresh();
			} catch (err) {
			}
			cancelClick();
		}, error: function() {
			window.parent.showErrorDlg();
		}
	});
}
</script>
</HEAD><BODY class=POPUP_WND onload="doOnload()">
<div class=formtip id=tips style="display: none"></div>
<form name="thresholdForm">
<input type=hidden name="threshold.thresholdId" value="<s:property value="threshold.thresholdId"/>">
<table width=100% height=100% cellspacing=8 cellpadding=0>
   <tr>
   	<td width=144px><fmt:message key="ALERT.thresholdType" bundle="${fault}"/>:</td><td align=right>
   	<select id="typeCombo" style="width:100px" name="threshold.type" 
   		onchange="typeChanged(this)">
   		<option value="0"><fmt:message key="ALERT.numType" bundle="${fault}"/></option>
   		<option value="1"><fmt:message key="ALERT.stringType" bundle="${fault}"/></option>
   	</select>
   	</td><td width=60px align=right><fmt:message key="COMMON.name" bundle="${resource}"/>: <font color=red>*</font></td>
  		<Td><input class=iptxt name="threshold.name" id="name" type=text 
  		style="margin-left:6px;width:140px" value="<s:property value="threshold.name"/>"
  		onfocus="inputFocused('name', '', 'iptxt_focused');" 
   		onblur="inputBlured(this, 'iptxt');" 
       onclick="clearOrSetTips(this);"></Td>
  	</tr>
   <tr><td><fmt:message key="COMMON.description" bundle="${resource}"/>: </td>
   	<td colspan=3 align=right><textarea  style="width:322px" id=description 
   	name="threshold.description" rows=2 
   	class=iptxa onfocus="inputFocused('description', '<fmt:message key="ALERT.thresholdNote" bundle="${fault}"/>', 'iptxa_focused');" 
   	onblur="inputBlured(this, 'iptxa');" 
    onclick="clearOrSetTips(this);"><s:property value="threshold.description"/></textarea></td></tr>

<tr><Td colspan=4>
<fieldset>
	<legend><fmt:message key="ALERT.judge" bundle="${fault}"/></legend>
	<table cellspacing=8 cellpadding=0 width=100%>
	    <tr><td width=68px><fmt:message key="ALERT.whenGetValue" bundle="${fault}"/></td><td>
	    <select id="operator1" name="threshold.operator1" style="width:60px">
	    		<option value="0">&gt;</option>
	    		<option value="1">&gt;=</option>
	    		<option value="2">=</option>
	    		<option value="3">!=</option>
	    		<option value="4">&lt;</option>
	    		<option value="5">&lt;=</option>
	    </select><input class=iptxt name="threshold.threshold1" id="threshold1" 
	    type=text style="margin-left:5px;width:60px" value="<s:property value="threshold.threshold1"/>">,<fmt:message key="ALERT.awaysOver" bundle="${fault}"/>
	    <select name="threshold.count1" id="count1" style="margin-right:5px;width:50px">
	    	<option value="1">1</option><option value="2">2</option><option value="3">3</option>
	    	<option value="4">4</option><option value="5">5</option>
	    </select><fmt:message key="ALERT.nowAlert" bundle="${fault}"/></td></tr>
	    <tr><td><fmt:message key="ALERT.alertLevel" bundle="${fault}"/>:</td><td>
	    	<select id="alertLevel1" name="threshold.alertLevel1" style="width:125px">
	    		<option value="6"><fmt:message key="WorkBench.emergencyAlarm" bundle="${fault}"/></option>
	    		<option value="5"><fmt:message key="WorkBench.seriousAlarm" bundle="${fault}"/></option>
	    		<option value="4"><fmt:message key="WorkBench.mainAlarm" bundle="${fault}"/></option>
	    		<option value="3"><fmt:message key="WorkBench.minorAlarm" bundle="${fault}"/></option>
	    		<option value="2"><fmt:message key="WorkBench.generalAlarm" bundle="${fault}"/></option>
	    		<option value="1"><fmt:message key="WorkBench.message" bundle="${fault}"/></option>
	    	</select></td></tr>			    
	    <tr><td><fmt:message key="ALERT.alertNote" bundle="${fault}"/>:</td><td colspan=3><textarea style="width:300px" id=alertDesc1 
	    	name="threshold.alertDesc1" rows=5 class=iptxa
	        onfocus="inputFocused('alertDesc1', '<fmt:message key="ALERT.varCanUse" bundle="${fault}"/>:\${source},\${host},\${value},<br>\${threshold},\${type},\${date},\${datetime}','iptxa_focused');"  
	        onblur="inputBlured(this, 'iptxa');" onclick="clearOrSetTips(this);"><s:property value="threshold.alertDesc1"/></textarea>
	   </td></tr>
	</table>			
</fieldset>
</td></tr>
<tr><td colspan=4 style="padding-top:5px" valign=top align=right>
	<button id=okBtn class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" 
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'" onclick="okClick()"><fmt:message key="COMMON.finish" bundle="${resource}"/></button>&nbsp;
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" 
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button>
</td></tr>
</table></form>
</BODY></HTML>