<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
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
</script>
</HEAD><BODY class=POPUP_WND style="padding:10px" onload="doOnload()">
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td height=20><fmt:message bundle="${resources}" key="SYSTEM.maxVisibleNavigationNum" /></td><td width=80 align=right>
<select id="maxNavi" style="width:75px">
<% int count = 0; %>
<option value="3">3</option>		
<s:iterator value="naviBars">
	<% count++; if (count > 3) { %>
	<option value="<%= count %>"><%= count %></option>
	<% } %>
</s:iterator>								
</select></td></tr>
<tr><td height=8 colspan=2></td></tr>
<tr><td height=20 colspan=2><fmt:message bundle="${resources}" key="SYSTEM.NavigationNote1"/></td></tr>
<tr><td height=8 colspan=2></td></tr>
<tr><td><div class=PANEL-CONTAINER style="overflow:auto; width:200px; height:200px;">
<table id="naviContainer" cellspacing=0>
<s:iterator value="naviBars">
	<tr><td><input type=checkbox <s:if test="checked">checked</s:if> id="<s:property value="name"/>" name="<s:property value="name"/>"></td>
	<td onclick="onTrClick(this)" style="cursor:default;padding-left:5px;padding-top:3px"><s:property value="displayName"/></td></tr>
</s:iterator>
</table></div></td>
<td width=80px align=right><table height=120px cellspacing=0 cellpadding=0>
	<tr><td><button id="upBt" disabled class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="upMoveClick();this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message bundle="${resources}" key="SYSTEM.moveup"/></button></td></tr>
	<tr><td><button id="downBt" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="downMoveClick(); this.className= (this.disabled ? 'BUTTON75' : 'BUTTON_OVER75');"><fmt:message bundle="${resources}" key="SYSTEM.movedown"/></button></td></tr>
	<tr><td><button id="resetBt" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'"
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="resetClick()"><fmt:message bundle="${resources}" key="SYSTEM.Reset"/></button></td></tr>
</table></td></tr>
<tr><td height=10 colspan=2></td></tr>
<tr><td colspan=2 align=right valign=top height=30px><button id="okBt" 
	class=BUTTON75 
	onMouseOver="this.className='BUTTON_OVER75'"
    onMouseOut="this.className='BUTTON75'"
    onMouseDown="this.className='BUTTON_PRESSED75'"
    onclick="okClick()"><fmt:message bundle="${resources}" key="COMMON.ok" /></button>&nbsp;<button class=BUTTON75 
	onMouseOver="this.className='BUTTON_OVER75'" 
	onMouseDown="this.className='BUTTON_PRESSED75'" 
	onMouseOut="this.className='BUTTON75'" 
	onclick="cancelClick()"><fmt:message bundle="${resources}" key="COMMON.cancel" /></button></td></tr>
</table>
</body></html>
