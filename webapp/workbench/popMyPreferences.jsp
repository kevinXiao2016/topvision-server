<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../js/dhtmlx/tree/dhtmlxtree.css">
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<%@include file="../include/tabPatch.inc"%>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ux/SliderTip.js"></script>
<script type="text/javascript" src="../js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="../js/dhtmlx/tree/dhtmlxtree.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script> 
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.workbench.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var tabbed = <s:property value="userContext.tabbed"/>;
var startPage = '<s:property value="userContext.startPage"/>';
var pageSize = <s:property value="userContext.pageSize"/>;
var hostAddr = '<%= request.getRemoteAddr() %>';
var columns = 2;

function cancelClick() {
	window.top.ZetaCallback = {type : null};
	window.top.hideModalDlg();
}
function defaultFailureCallback(response) {
	window.top.showErrorDlg();
}

function bindClick() {
	$.ajax({url: 'hasBindIp.tv', type: 'GET', 
		data: {bindIp: hostAddr},
		success: function(json) {
			if (json.exists) {
				window.top.showMessageDlg(I18N.popMyPreferences.tip, hostAddr + I18N.popMyPreferences.otherUser + json.name + I18N.popMyPreferences.bind);
			} else {
				Zeta$('bindIp').value = hostAddr;
			}
		}, 
		error: defaultFailureCallback,
		dataType: 'json', cache: false});
}

function unbindClick() {
	Zeta$('bindIp').value = '';
}

/* navigation option */
var maxIndex = <s:property value="naviBars.size"/> - 1;
var curMaxIndex = <s:property value="userContext.maxNaviNum"/>;

var curIndex = 0;
var oldObj = null;	
function doOnload() {
	var el = Zeta$('maxNavi')
	if (el.options.length > (curMaxIndex - 3)) {
		el.selectedIndex = curMaxIndex - 3;
	} else {
		el.selectedIndex = el.options.length - 1;
	}
	el = Zeta$('naviContainer');
	if (el.rows.length > 0) {
		oldObj = el.rows[0].children[1];
		oldObj.className = 'row-selected';	
	} else {
		Zeta$('okBt').disabled = true;
		Zeta$('upBt').disabled = true;
		Zeta$('downBt').disabled = true;	
		Zeta$('resetBt').disabled = true;					
	}
}
function resetClick() {
	location.href = 'resetNaviOption.tv';
}
function okClick() {
	var el = Zeta$('maxNavi');
	var naviBarOrder = [];
	var naviBarVisible = [];
	var list = document.getElementsByTagName('INPUT');
	for (var i = 0; i < list.length; i++) {
		if (list[i].checked) {
			naviBarVisible[naviBarVisible.length] = list[i].id;
		}
		naviBarOrder[naviBarOrder.length] = list[i].id;
	}
	$.ajax({url: 'setNaviOrder.tv', type: 'POST',
		data: {maxNaviNumber: el.options[el.selectedIndex].value, 
			naviBarVisible: naviBarVisible, naviBarOrder: naviBarOrder},
		success: function() {
			window.parent.location.href = window.parent.standalone ? '../mainFrame.tv?standalone=true' : '../mainFrame.tv';
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
}
function cancelClick() {
	window.parent.closeWindow('modalDlg');
}
function onTrClick(obj) {
	var p = obj.parentNode;
	oldObj.className = 'row-unselected';
	obj.className = 'row-selected';
	oldObj = obj;
	curIndex = p.rowIndex;
	if (curIndex == 0) {
		Zeta$('upBt').disabled = true;
		Zeta$('downBt').disabled = false;
	} else if (curIndex == maxIndex) {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = true;
	} else {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = false;		
	}
}
function upMoveClick() {
	var container = Zeta$('naviContainer');
	var src = container.rows[curIndex];
	var dst = container.rows[curIndex - 1];
	var checked = src.children[0].children[0].checked;
	var checked1 = dst.children[0].children[0].checked;
	src.swapNode(dst);
	src.children[0].children[0].checked = checked;
	dst.children[0].children[0].checked = checked1;
	curIndex--;
	if (curIndex == 0) {
		Zeta$('upBt').disabled = true;
		Zeta$('downBt').disabled = false;
	} else if (curIndex == maxIndex) {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = true;
	} else {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = false;		
	}
}
function downMoveClick() {
	var container = Zeta$('naviContainer');
	var src = container.rows[curIndex];
	var dst = container.rows[curIndex + 1];
	var checked = src.children[0].children[0].checked;
	var checked1 = dst.children[0].children[0].checked;
	src.swapNode(dst);
	src.children[0].children[0].checked = checked;
	dst.children[0].children[0].checked = checked1;
	curIndex++;
	if (curIndex == 0) {
		Zeta$('upBt').disabled = true;
		Zeta$('downBt').disabled = false;
	} else if (curIndex == maxIndex) {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = true;
	} else {
		Zeta$('upBt').disabled = false;
		Zeta$('downBt').disabled = false;		
	}		
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	var w = document.body.clientWidth - 20;
	var h = document.body.clientHeight - 60;
	if (w < 300) {
		w = 300;
	}
	if (h < 100) {
		h = 100;
	}
    var tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: h,
        activeTab: 0,
        frame: true,
        defaults:{autoHeight: true},
        items:[
        	{contentEl: 'generalTab', title: I18N.popMyPreferences.GeneralSettings},
        	{contentEl: 'reminderTab', title: I18N.popMyPreferences.Messageprompts},
            {contentEl: 'infoTab', title: I18N.popMyPreferences.TabbedBrowsing},
            {contentEl: 'timeTab', title: I18N.popMyPreferences.CustomizingNavigationPane},
            {contentEl: 'actionTab', title: I18N.popMyPreferences.CustomizeMyDesktop}
        ]
    });
    
    var tip = new Ext.ux.SliderTip({
        getText: function(slider){
            return String.format('<b>{0}</b>', slider.getValue());
        }
    });
    slider = new Ext.Slider({
        renderTo: 'tip-slider',
        width: 180,
        value: pageSize,
        minValue: 15,
        maxValue: 100,
        plugins: tip,
        listeners: {
        	changecomplete: function(slider, newValue ) {
        		Zeta$('hid7').value = newValue;
        	}
        }
    });     
    
	var el = Zeta$('columnDiv');
	for (var i = 0; i < el.options.length; i++) {
		if (el.options[i].value == columns) {
			el.selectedIndex = i;
			break;
		}
	}
	tree = new dhtmlXTreeObject("portletTree", "100%", "100%", 0);
	tree.setImagePath("../js/dhtmlx/tree/imgs/vista/");
	tree.enableCheckBoxes(1);
	tree.enableThreeStateCheckboxes(true);
	tree.loadXML("../portal/loadPortletItemsByUserId.tv"); 
	
	var el = Zeta$('maxNavi')
	if (el.options.length > (curMaxIndex - 3)) {
		el.selectedIndex = curMaxIndex - 3;
	} else {
		el.selectedIndex = el.options.length - 1;
	}
	el = Zeta$('naviContainer');
	if (el.rows.length > 0) {
		oldObj = el.rows[0].children[1];
		oldObj.className = 'row-selected';	
	} else {
		Zeta$('okBt').disabled = true;
		Zeta$('upBt').disabled = true;
		Zeta$('downBt').disabled = true;	
		Zeta$('resetBt').disabled = true;					
	}

	Zeta$('buttonPanel').style.display = '';   
});
</script>	  
</HEAD><BODY class=POPUP_WND style="padding:10px">
<div id="tabs">
<div id="generalTab" class="x-hide-display">
 	<table cellspacing=10 cellpadding=0>

	<tr><td><fmt:message key="td.styleName" bundle="${workbench}"/></td></tr>
	<tr><td style="padding-left:22px"><select id="styleName" name="generalPreferences.styleName" style="width:198px">
		<s:iterator value="themes">
			<option value="<s:property value="value"/>"><s:property value="name"/></option>
		</s:iterator>
	</select></td></tr>
	<tr><td><fmt:message key="popMyPreferences.note1" bundle="${workbench}"/></td></tr>
	<tr><td style="padding-left:22px">
		<table cellspacing=0 cellpadding=0>
		<tr><td>15&nbsp;</td><td><div id="tip-slider"></div></td><td>&nbsp;100</td></tr>
		</table>
	</td></tr>

	<s:if test="allowIpBindLogon&&userContext.user.ipLoginActive==1">
	<tr><td><fmt:message key="td.ipLoginActive" bundle="${workbench}"/></td></tr>
	<tr><td style="padding-left:22px">
		<input class=iptxt id="bindIp" readonly type=text name="bindIp" style="width:198px;" 
		value="<s:property value="userContext.user.bindIp"/>">
		<input type=button class=BUTTON75 
		onmousedown="this.className='BUTTON_PRESSED75'"
		onMouseOver="this.className='BUTTON_OVER75'"
		onMouseOut="this.className='BUTTON75'" value="<fmt:message key="onMouseOut.bind" bundle="${workbench}"/>" onclick="bindClick();">
		<input <s:if test="userContext.user.ipLoginActive==0">disabled</s:if> type=button 
		class=BUTTON75 onmousedown="this.className='BUTTON_PRESSED75'"
		onMouseOver="this.className='BUTTON_OVER75'" onclick="unbindClick();"
		onMouseOut="this.className='BUTTON75'" value="<fmt:message key="onMouseOut.unbind" bundle="${workbench}"/>">
	</td></tr>	
	</s:if>
 	</table>
</div>
<div id="infoTab" class="x-hide-display">
<table cellspacing=10 cellpadding=0>
	<tr><td><input id=tabbed type=checkbox <s:if test="userContext.tabbed">checked</s:if> 
	onclick="enableTabbed(this)" name="tabbed"><label for="tabbed"><fmt:message key="label.enableTabbed" bundle="${workbench}"/></label></td></tr>
	<tr><td style="padding-left:18px"><input id=alarmWhenCloseManyTab type=checkbox <s:if test="userContext.alarmWhenCloseManyTab">checked</s:if> 
	name="alarmWhenCloseManyTab" <s:if test="!userContext.tabbed">disabled</s:if>><label for="alarmWhenCloseManyTab"><fmt:message key="label.alarmWhenCloseManyTab" bundle="${workbench}"/></label></td></tr>
	<tr><td style="padding-left:18px"><input id=switchWhenNewTab type=checkbox <s:if test="userContext.switchWhenNewTab">checked</s:if> 
	name="switchWhenNewTab" <s:if test="!userContext.tabbed">disabled</s:if>><label for="switchWhenNewTab"><fmt:message key="label.switchWhenNewTab" bundle="${workbench}"/></label></td></tr>

	<tr><td style="padding-left:18px"><input id=tabMaxLimit type=checkbox <s:if test="userContext.tabMaxLimit">checked</s:if> 
	name="tabMaxLimit" <s:if test="!userContext.tabbed">disabled</s:if>><label for="tabMaxLimit"><fmt:message key="label.tabMaxLimit" bundle="${workbench}"/></label></td></tr>
  	</table>
</div>
<div id="actionTab" class="x-hide-display">
	<table cellspacing=10 cellpadding=0>
	<tr><td height=20 width=120><fmt:message key="td.columnNum" bundle="${workbench}"/></td><td>
		<select id="columnDiv" style="width:100px" onchange="columnChanged(this)">
		<option value="2">2</option>
		<option value="3">3</option>
		</select>
	</td></tr>
	<tr><td height=20 colspan=2><fmt:message key="td.selectVisiblePart" bundle="${workbench}"/></td></tr>
	<tr><td colspan=2 class=PANEL-CONTAINER>
		<div id="portletTree" style="height:190px; overflow:auto;"></div>
	</td></tr>
	</table>	  
</div>    
<div id="timeTab" class="x-hide-display">
	<table cellspacing=10 cellpadding=0>
	<tr><td height=20><fmt:message key="popMyPreferences.maxView" bundle="${workbench}"/></td><td width=80 align=right>
	<select id="maxNavi" style="width:75px">
	<% int count = 0; %>
	<option value="3">3</option>		
	<s:iterator value="naviBars">
		<% count++; if (count > 3) { %>
		<option value="<%= count %>"><%= count %></option>
		<% } %>
	</s:iterator>								
	</select></td></tr>
	<tr><td height=20 colspan=2><fmt:message key="popMyPreferences.note2" bundle="${workbench}"/></td></tr>
	<tr><td><div class=PANEL-CONTAINER style="overflow:auto; width:200px; height:190px;">
	<table id="naviContainer" cellspacing=0>
	<s:iterator value="naviBars">
		<tr><td><input type=checkbox <s:if test="checked">checked</s:if> id="<s:property value="name"/>" name="<s:property value="name"/>"></td>
		<td onclick="onTrClick(this)" style="cursor:default;padding-left:5px;padding-top:3px"><s:property value="displayName"/></td></tr>
	</s:iterator>
	</table></div></td>
	<td width=80px align=right><table height=120px cellspacing=0 cellpadding=0>
		<tr><td><button id="upBt" disabled class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
		onMouseDown="this.className='BUTTON_PRESSED75'"
		onclick="upMoveClick();this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message key="button.upMove" bundle="${workbench}"/></button></td></tr>
		<tr><td><button id="downBt" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
		onMouseDown="this.className='BUTTON_PRESSED75'"
		onclick="downMoveClick(); this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message key="button.downMove" bundle="${workbench}"/></button></td></tr>
		<tr><td><button id="resetBt" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
		onMouseDown="this.className='BUTTON_PRESSED75'" onclick="resetClick()"><fmt:message key="button.reset" bundle="${workbench}"/></button></td></tr>
	</table></td></tr>
	</table>
</div>
<div id="reminderTab" class="x-hide-display">
	<table cellspacing=10 cellpadding=0>
	<tr><td>
	<input id=inputTip type=checkbox <s:if test="userContext.displayInputTip">checked</s:if>><label for="inputTip"><fmt:message key="input.label.inputTip" bundle="${workbench}"/></label>
	</td></tr>

	<tr><td>
	<input id=notifyWhenMsg type=checkbox <s:if test="userContext.notifyWhenMsg">checked</s:if> 
	name="notifyWhenMsg"><label for="notifyWhenMsg"><fmt:message key="input.label.notifyWhenMsg" bundle="${workbench}"/></label>
	</td></tr>
	
	<tr><td>
	<input id=notifyWhenAlert type=checkbox <s:if test="userContext.notifyWhenAlert">checked</s:if> onclick="enableTabbed(this)" 
	name="notifyWhenAlert"><label for="notifyWhenAlert"><fmt:message key="input.label.notifyWhenAlert" bundle="${workbench}"/></label>
	</td></tr>
	
	<tr><td style="padding-left:20px">
	<input id=soundWhenAlert type=checkbox <s:if test="userContext.soundWhenAlert">checked</s:if> onclick="enableTabbed(this)" 
	name="soundWhenAlert"><label for="soundWhenAlert"><fmt:message key="input.label.soundWhenAlert" bundle="${workbench}"/></label>
	</td></tr>		
	
	<tr><td style="padding-left:22px"><fmt:message key="td.alarmConditions" bundle="${workbench}"/>&nbsp;<select id="notifyAlertLevel" style="width:100px">
			<option value="6"><fmt:message key="select.option.emergencyAlarm" bundle="${workbench}"/></option>
			<option value="5"><fmt:message key="select.option.seriousAlarm" bundle="${workbench}"/></option>
			<option value="4"><fmt:message key="select.option.mainAlarm" bundle="${workbench}"/></option>
			<option value="3"><fmt:message key="select.option.minorAlarm" bundle="${workbench}"/></option>
			<option value="2"><fmt:message key="select.option.generalAlarm" bundle="${workbench}"/></option>
			<option value="1"><fmt:message key="select.option.message" bundle="${workbench}"/></option>
		</select>&nbsp;<fmt:message key="td.alarmConditions2" bundle="${workbench}"/>
	</td></tr>	
	
	<tr><td style="padding-left:22px">
		
	</td></tr>	
	</table>
</div>
</div>
<div id="buttonPanel" align=right style="padding-top:10px;display:none">
	<button class=BUTTON75 type="button" 
    onMouseOver="this.className='BUTTON_OVER75'"
    onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
    onclick="okClick()"><fmt:message key="button.confirm" bundle="${workbench}"/></button>&nbsp;<button 
    class=BUTTON75 type="button" onMouseOver="this.className='BUTTON_OVER75'"
    onMouseOut="this.className='BUTTON75'"onMouseDown="this.className='BUTTON_PRESSED75'"
    onclick="cancelClick()"><fmt:message key="button.cancel" bundle="${workbench}"/></button>
</div>
</BODY></HTML>