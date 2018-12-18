<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>  
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = <%= request.getParameter("entityId") %>;
var itemName = '<%= request.getParameter("itemName") %>';
function cancelClick() {
	window.top.closeWindow('modalDlg');
}
function okClick() {
	var el = Zeta$('threshold');
	if (el.options.length == 0) {
		cancelClick();
	} else {
		$.ajax({url: 'relateThreshold.tv', data:{'entityId' : entityId,
			'itemName': itemName, 'thresholdId': el.options[el.selectedIndex].value},
			dataType: 'json', cache: 'false',
			success:function(json){
				try {
					window.top.getActiveFrame().refreshThresholdTree();
				} catch (err) {
				}
				cancelClick();
			}, error:function(){
				window.top.showErrorDlg();
			}
		});
	}
}
</script>	  
</HEAD><BODY class=POPUP_WND style="margin:10px">
<center>
<table cellspacing=0 cellpadding=0>
<tr><td height=25px><fmt:message key="ALERT.thresholdList" bundle="${fault}"/>:</td></tr>
<tr><td height=4>
	<select id="threshold" style="width:330px;">
	<s:iterator value="thresholds">
	<s:if test="!defaultThreshold">
	<option value="<s:property value="thresholdId"/>"><s:property value="name"/> [<s:property value="description"/>]</option>
	</s:if>
	</s:iterator>
	</select>
</td></tr>
<tr><td></td></tr>
<tr><td height=4>&nbsp;</td></tr>
<tr><td valign=top align=right>
<button id=okBt class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button></td></tr>
<tr><td height=4></td></tr>	
</table></center>
</BODY></HTML>