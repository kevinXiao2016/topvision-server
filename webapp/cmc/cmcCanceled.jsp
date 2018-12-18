<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc" %>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var cmcId = '<s:property value="cmcId"/>';
var cmcType = '<s:property value="cmcType"/>';

$(function(){
	var imgSrc = "";
	switch(cmcType){
		case '30002':
			imgSrc = "/images/network/ccmts/ccmts_b_48.png";
			break;
		case '30004':
			imgSrc = "/images/network/ccmts/ccmts_d_48.png";
			break;
		case '30006':
			imgSrc = "/images/network/ccmts/ccmts_c_b_48.png";
			break;
		case '30007':
			imgSrc = "/images/network/ccmts/ccmts_s_48.png";
			break;
		default:
			imgSrc = "/images/network/ccmts/ccmts_c_b_48.png";
			break;
	} 
	$("#entityIcon").attr({
		"src" : imgSrc
	});
});//end document.ready;  
var entityId = '<s:property value="entityId"/>';
function enableManagement() {
	var entityIds = [entityId];
	window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip,I18N.CMC.text.confirmmanageccmtsagain, function(type) {
		if (type == 'cancel') {return;}
	 	$.ajax({url: '/entity/enableManagement.tv', data: {'entityIds': entityIds},
	 		success: function() {
	 			//location.href = '/cmc/showCmcPortal.tv?cmcId=' + cmcId + '&entityId='+entityId+'&productType='+cmcType;
	 			location.href = '/cmc/entityPortal.tv?entityId='+entityId;
	 		}, error: function() {
	 			window.parent.showErrorDlg();
	 		}, cache: false});			
	});
}
</script>
</head>
<body class=BLANK_WND style="padding: 20px">
	<table cellspacing=5 align="center">
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.name'/>:</td>
			<td width=10></td>
			<td><s:property value="entity.sysName"/></td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.aliasname'/>:</td>
			<td width=10></td>
			<td><s:property value="entity.name"/></td>
		    <td rowspan=3 align=center>
				<img id="entityIcon" src="/images/network/ccmts/ccmts_c_b_48.png" border=0>
			</td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.entitytype'/>:</td>
			<td width=10></td>
			<td>${cmcTypeString}</td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.entityLocation'/>:</td>
			<td width=10></td>
			<td><s:property value="entity.sysLocation"/></td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.contactPerson'/>:</td>
			<td width=10></td>
			<td><s:property value="entity.sysContact"/></td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.manageIP'/>:</td>
			<td width=10></td>
			<td colspan=2><s:property value="entity.ip"/></td>
		</tr>
		<tr>
			<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.entityDescrib'/>:</td>
			<td width=10></td>
			<td colspan=2>
				<textarea disabled rows=3 style="width: 400px"><s:property value="entity.sysDescr"/></textarea>
			</td>
		</tr>
		<tr>
			<td colspan=3 align=center height=40px><font color=red><fmt:message bundle='${cmc}' key='CMC.text.equipment'/>
					<s:property value="entity.ip" /> <fmt:message bundle='${cmc}' key='CMC.text.cancelledmgt'/></font>
			</td>
		</tr>
		<tr>
			<td colspan=3 align=center height=40px>
			<button type="button"
					class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
					onMouseDown="this.className='BUTTON_PRESSED120'"
					onMouseOut="this.className='BUTTON120'"
					onClick="enableManagement();"><fmt:message bundle='${cmc}' key='CMC.label.manageentityagain'/>
			</button>
			</td>
		</tr>
	</table>
</body>
</html>
