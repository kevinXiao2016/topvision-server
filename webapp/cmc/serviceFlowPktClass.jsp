<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.ems.cmc.qos.facade.domain.CmcQosPktClassInfo"%>
<%@ page import="java.util.List"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript">
var recordFlag = 0;
var cmcQosPktClassObject = ${cmcQosPktClassObject} ? ${cmcQosPktClassObject} : new Array();
var length = cmcQosPktClassObject.length;
var attrStore = null;
var grid = null;
Ext.onReady(function() {
	$("#totalNum").val(length);
	$("#currentNum").val(recordFlag + 1);
	attrStore = {
		I18N.QoS.pktClassId : cmcQosPktClassObject[0].classId,
		I18N.QoS.direction : cmcQosPktClassObject[0].classDirection,
		I18N.QoS.priority : cmcQosPktClassObject[0].classIpTosLow,
		I18N.QoS.pktClassIpTosLow : cmcQosPktClassObject[0].classPriority,
		I18N.QoS.pktClassIpTosHigh : cmcQosPktClassObject[0].classIpTosHigh,
		I18N.QoS.pktClassIpTosMask : cmcQosPktClassObject[0].classIpTosMask,
		I18N.QoS.pktClassIpProtocol : cmcQosPktClassObject[0].classIpProtocol,
		I18N.QoS.pktClassSourcePortStart : cmcQosPktClassObject[0].classSourcePortStart,
		I18N.QoS.pktClassSourcePortEnd : cmcQosPktClassObject[0].classSourcePortEnd,
		I18N.QoS.pktClassDestPortStart : cmcQosPktClassObject[0].classDestPortStart,
		I18N.QoS.pktClassDestPortEnd : cmcQosPktClassObject[0].classDestPortEnd,
		I18N.QoS.pktClassDestMacAddr : cmcQosPktClassObject[0].classDestMacAddr,
		I18N.QoS.pktClassDestMacMask : cmcQosPktClassObject[0].classDestMacMask,
		I18N.QoS.pktClassSourceMacAddr : cmcQosPktClassObject[0].classSourceMacAddr,
		I18N.QoS.pktClassEnetProtocolType : cmcQosPktClassObject[0].docsQosPktClassEnetProtocolTypeName,
		I18N.QoS.pktClassEnetProtocol : cmcQosPktClassObject[0].classEnetProtocol,
		I18N.QoS.pktClassUserPriLow : cmcQosPktClassObject[0].classUserPriLow,
		I18N.QoS.pktClassUserPriHigh : cmcQosPktClassObject[0].classUserPriHigh,
		I18N.QoS.pktClassVlanId : cmcQosPktClassObject[0].classVlanId,
		I18N.QoS.pktClassState : cmcQosPktClassObject[0].classState,
		I18N.QoS.pktClassPkts : cmcQosPktClassObject[0].classPkts,
		I18N.QoS.pktClassBitMap : cmcQosPktClassObject[0].classPkts
	};
	grid = new Ext.grid.PropertyGrid({
		renderTo : 'pktClass',
		headerAsText : true,
		hideHeaders : true,
		width : 350,
		height : 550,
		frame : false,
		source : attrStore
	});
	grid.on("beforeedit", function(e) {
		e.cancel = true;
		return false;
});
Ext.grid.PropertyGrid.prototype.setSource = function(source) {
	delete this.propStore.store.sortInfo;
	this.propStore.setSource(source);
};
});
function cancelClick() {
	window.top.closeWindow('serviceFlowPktClass');
}

function beforeClick(){
	$("#nextClick").attr("disabled", false);
	if( 0 < recordFlag ){
		recordFlag -= 1;
	}
	attrStore = {
			I18N.QoS.pktClassId : cmcQosPktClassObject[recordFlag].classId,
			I18N.QoS.direction : cmcQosPktClassObject[recordFlag].classDirection,
			I18N.QoS.priority : cmcQosPktClassObject[recordFlag].classIpTosLow,
			I18N.QoS.pktClassIpTosLow : cmcQosPktClassObject[recordFlag].classPriority,
			I18N.QoS.pktClassIpTosHigh : cmcQosPktClassObject[recordFlag].classIpTosHigh,
			I18N.QoS.pktClassIpTosMask : cmcQosPktClassObject[recordFlag].classIpTosMask,
			I18N.QoS.pktClassIpProtocol : cmcQosPktClassObject[recordFlag].classIpProtocol,
			I18N.QoS.pktClassSourcePortStart : cmcQosPktClassObject[recordFlag].classSourcePortStart,
			I18N.QoS.pktClassSourcePortEnd : cmcQosPktClassObject[recordFlag].classSourcePortEnd,
			I18N.QoS.pktClassDestPortStart : cmcQosPktClassObject[recordFlag].classDestPortStart,
			I18N.QoS.pktClassDestPortEnd : cmcQosPktClassObject[recordFlag].classDestPortEnd,
			I18N.QoS.pktClassDestMacAddr : cmcQosPktClassObject[recordFlag].classDestMacAddr,
			I18N.QoS.pktClassDestMacMask : cmcQosPktClassObject[recordFlag].classDestMacMask,
			I18N.QoS.pktClassSourceMacAddr : cmcQosPktClassObject[recordFlag].classSourceMacAddr,
			I18N.QoS.pktClassEnetProtocolType : cmcQosPktClassObject[recordFlag].docsQosPktClassEnetProtocolTypeName,
			I18N.QoS.pktClassEnetProtocol : cmcQosPktClassObject[recordFlag].classEnetProtocol,
			I18N.QoS.pktClassUserPriLow : cmcQosPktClassObject[recordFlag].classUserPriLow,
			I18N.QoS.pktClassUserPriHigh : cmcQosPktClassObject[recordFlag].classUserPriHigh,
			I18N.QoS.pktClassVlanId : cmcQosPktClassObject[recordFlag].classVlanId,
			I18N.QoS.pktClassState : cmcQosPktClassObject[recordFlag].classState,
			I18N.QoS.pktClassPkts : cmcQosPktClassObject[recordFlag].classPkts,
			I18N.QoS.pktClassBitMap : cmcQosPktClassObject[recordFlag].classPkts
		};
	grid.setSource(attrStore);
	$("#currentNum").val(recordFlag + 1);
	if(recordFlag == 0){
		$("#beforeClick").attr("disabled", true);
		}
}
function nextClick(){
	$("#beforeClick").attr("disabled", false);
	if( recordFlag < 2){
		recordFlag += 1;
	}
	attrStore = {
			I18N.QoS.pktClassId : cmcQosPktClassObject[recordFlag].classId,
			I18N.QoS.direction : cmcQosPktClassObject[recordFlag].classDirection,
			I18N.QoS.priority : cmcQosPktClassObject[recordFlag].classIpTosLow,
			I18N.QoS.pktClassIpTosLow : cmcQosPktClassObject[recordFlag].classPriority,
			I18N.QoS.pktClassIpTosHigh : cmcQosPktClassObject[recordFlag].classIpTosHigh,
			I18N.QoS.pktClassIpTosMask : cmcQosPktClassObject[recordFlag].classIpTosMask,
			I18N.QoS.pktClassIpProtocol : cmcQosPktClassObject[recordFlag].classIpProtocol,
			I18N.QoS.pktClassSourcePortStart : cmcQosPktClassObject[recordFlag].classSourcePortStart,
			I18N.QoS.pktClassSourcePortEnd : cmcQosPktClassObject[recordFlag].classSourcePortEnd,
			I18N.QoS.pktClassDestPortStart : cmcQosPktClassObject[recordFlag].classDestPortStart,
			I18N.QoS.pktClassDestPortEnd : cmcQosPktClassObject[recordFlag].classDestPortEnd,
			I18N.QoS.pktClassDestMacAddr : cmcQosPktClassObject[recordFlag].classDestMacAddr,
			I18N.QoS.pktClassDestMacMask : cmcQosPktClassObject[recordFlag].classDestMacMask,
			I18N.QoS.pktClassSourceMacAddr : cmcQosPktClassObject[recordFlag].classSourceMacAddr,
			I18N.QoS.pktClassEnetProtocolType : cmcQosPktClassObject[recordFlag].docsQosPktClassEnetProtocolTypeName,
			I18N.QoS.pktClassEnetProtocol : cmcQosPktClassObject[recordFlag].classEnetProtocol,
			I18N.QoS.pktClassUserPriLow : cmcQosPktClassObject[recordFlag].classUserPriLow,
			I18N.QoS.pktClassUserPriHigh : cmcQosPktClassObject[recordFlag].classUserPriHigh,
			I18N.QoS.pktClassVlanId : cmcQosPktClassObject[recordFlag].classVlanId,
			I18N.QoS.pktClassState : cmcQosPktClassObject[recordFlag].classState,
			I18N.QoS.pktClassPkts : cmcQosPktClassObject[recordFlag].classPkts,
			I18N.QoS.pktClassBitMap : cmcQosPktClassObject[recordFlag].classPkts
		};
	grid.setSource(attrStore);
	$("#currentNum").val(recordFlag + 1);
	if(recordFlag == 2){
		$("#nextClick").attr("disabled", true);
		}
}
</script>
<title><fmt:message bundle="${cmc}" key="QoS.serviceFlowPktClass"/></title>
</head>
<body class=POPUP_WND style="margin: 15px;" onload="">
 	<div id="pktClass"> </div>
 	<div style="height:20px;">	
		<table width=100%>
			<tr>
				<td><fmt:message bundle="${cmc}" key="CCMTS.total"/><input class=iptxt type=text style="width: 20px" disabled
					id="totalNum"
					value=""><fmt:message bundle="${cmc}" key="QoS.ServiceFlowPktClassItem"/>
				</td>
				<td><fmt:message bundle="${cmc}" key="CCMTS.number"/><input class=iptxt type=text style="width: 20px" disabled
					id="currentNum" value=""><fmt:message bundle="${cmc}" key="QoS.ServiceFlowPktClassItem"/>
				</td>
				<td valign=top align=right>
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" id="beforeClick"
						onMouseOut="this.className='BUTTON75'" onclick="beforeClick()"><fmt:message bundle="${cmc}" key="CCMTS.previous"/></button>&nbsp;
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" id="nextClick"
						onMouseOut="this.className='BUTTON75'" onclick="nextClick()"><fmt:message bundle="${cmc}" key="CCMTS.next"/></button>&nbsp;
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message bundle="${resources}" key="COMMON.close"/></button>
				</td>
			</tr>
		</table>
  	</div>
</body>