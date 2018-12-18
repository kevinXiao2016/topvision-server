<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.ems.cmc.facade.domain.CmAttribute"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>Related CM info</title>
<%@include file="/include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript">
var cmAttribute = ${cmAttributeObject} ? ${cmAttributeObject} : new Array();
Ext.onReady(function() {
	var downIfIndex = cmAttribute[0].statusDownChannelIfIndex;
	var upIfIndex = cmAttribute[0].statusUpChannelIfIndex;
	var attrStore = {};
	// 对象的key如果是变量，只能用obj[变量]=xxx

		attrStore[I18N.CM.ipStyle]= cmAttribute[0].docsIfCmtsCmStatusInetAddressTypeString;
		attrStore[I18N.COMMON.ip]=cmAttribute[0].statusInetAddress;
	
		attrStore[I18N.CCMTS.macAddress]=  cmAttribute[0].statusMacAddress;
		attrStore[I18N.CCMTS.downStreamChannel]=  (downIfIndex>>27) + "/" + ((downIfIndex & 0x7800000) >> 23) + "/" +
		((downIfIndex & 0x7F0000)>>16) + "/" + (((downIfIndex & 0x00008000)>>15)==1?"D":"U")+
		((downIfIndex & 0x7F00)>>8);
		attrStore[I18N.CCMTS.upStreamChannel]=  (upIfIndex>>27) + "/" + ((upIfIndex & 0x7800000) >> 23) + "/" +
		((upIfIndex & 0x7F0000)>>16) + "/" + (((upIfIndex & 0x00008000)>>15)==1?"D":"U")+
		((upIfIndex & 0x7F00)>>8);
		
		attrStore[I18N.CHANNEL.snr]=  cmAttribute[0].docsIfCmtsCmStatusSignalNoiseString;
		attrStore[I18N.CM.timeOffset]=  cmAttribute[0].statusTimingOffset;
		
		attrStore[I18N.CM.connectionStatus]=  cmAttribute[0].docsIfCmtsCmStatusValueString;

	Ext.grid.PropertyGrid.prototype.setSource = function(source) {
        delete this.propStore.store.sortInfo;
        this.propStore.setSource(source);
    };
	var grid = new Ext.grid.PropertyGrid({
		renderTo : 'cmAttribute',
		headerAsText : true,
		hideHeaders : true,
		width : 350,
		height : 225,
		frame : false,
		source : attrStore
	});
	grid.on("beforeedit", function(e) {
		e.cancel = true;
		return false;
});
Ext.grid.PropertyGrid.prototype.setSource = function(source) {
	this.propStore.store.sortInfo
	this.propStore.setSource(source);
};
});
function cancelClick() {
	window.top.closeWindow('serviceFlowToCm');
}
</script>
</head>
<body class=POPUP_WND style="margin: 15px;" onload="">
  <div id="cmAttribute"> </div>
  <table width=100%>
	<tr>
		<td valign=top align=right>
			<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message bundle="${emsRes}" key="COMMON.off"/></button>
		</td>
	</tr>
  </table>
</body>
</html>