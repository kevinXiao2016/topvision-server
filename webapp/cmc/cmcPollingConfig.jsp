<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>Performance Configuration Information</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var pollingInterval = '<s:property value="pollingInterval"/>';
var pollingStatus = '<s:property value="pollingStatus"/>';

function initData() {
	Zeta$('pollingInterval').value = pollingInterval/60000;
}
function setPolling() {
	var pollingInterval = Zeta$('pollingInterval').value.trim() * 60000;
	var pollingStatus = Zeta$('pollingStatus').checked;
	Ext.Ajax.request({url: '../cmc/updateCmcPollingConfig.tv?cmcId=' + cmcId + '&pollingInterval=' + pollingInterval + '&pollingStatus=' + pollingStatus,
        success: function(response) {
            var json = Ext.util.JSON.decode(response.responseText);
        	if (json.success) {
				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.setSuccess, null, closeClick);
			}
		},
        failure: function()
        {window.parent.showMessageDlg(I18N.CMC.tip.error, I18N.CMC.tip.pollingSetFail, 'error', closeClick);}
    });
}
function closeClick() {
	window.parent.closeWindow('modalDlg');
}
</script>
</head>
<body class=BLANK_WND onload="initData()" style="margin: 10px;">
<hr/>
	<table width=100% cellspacing=5 cellpadding=3>
		<tr>
			<td colspan=2 width=40% align="left"><input id="pollingStatus"
				type="checkbox" onclick="" <s:if test="pollingStatus">checked</s:if>><label
				for="polling">&nbsp;&nbsp;<fmt:message bundle='${cmc}' key='CMC.label.startPolling'/></label></td>
		</tr>
		<tr>
			<td width=25% align="left"><fmt:message bundle='${cmc}' key='CMC.label.pollingCycle'/>:</td>
			<td width=45%><input type=text style="width: 40px" value="15"
				id="pollingInterval"
				onkeyup="this.value=this.value.replace(/[^\d]/g,'')"><fmt:message bundle='${cmc}' key='CMC.label.minutes'/></td>
			<td width=30%></td>
		</tr>
	</table>
	<div>&nbsp;</div>
	<div align="right">
		<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="setPolling()"><fmt:message bundle='${cmc}' key='CMC.button.save'/></button>
		&nbsp;
		<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="closeClick()"><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></button>
	</div>
</body>
</html>