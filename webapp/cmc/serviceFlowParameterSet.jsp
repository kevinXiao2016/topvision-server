<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.ems.cmc.qos.facade.domain.CmcQosParamSetInfo"%>
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
var cmcQosParamSetObject = ${cmcQosParamSetObject} ? ${cmcQosParamSetObject} : new Array();
var length = cmcQosParamSetObject.length; 
var grid = null;
var attrStore =null;

function attrStoreInfo(recordFlag){
	attrStore={};
	// 对象的key如果是变量，只能用obj[变量]=xxx
	attrStore[I18N.QoS.serviceFlowId]=cmcQosParamSetObject[recordFlag].serviceFlowId;
	attrStore[I18N.QoS.paramSetType]=cmcQosParamSetObject[recordFlag].docsQosParamSetTypeName;
	attrStore[I18N.QoS.paramSetServiceClassName]=cmcQosParamSetObject[recordFlag].docsQosParamSetServiceClassName;
	attrStore[I18N.QoS.priority]=cmcQosParamSetObject[recordFlag].priority;
	attrStore[I18N.QoS.paramSetMaxTrafficRate]=cmcQosParamSetObject[recordFlag].docsQosParamSetMaxTrafficRateString;
	attrStore[I18N.QoS.paramSetMaxTrafficBurst]=cmcQosParamSetObject[recordFlag].docsQosParamSetMaxTrafficBurstString;
	attrStore[I18N.QoS.paramSetMinReservedRate]=cmcQosParamSetObject[recordFlag].docsQosParamSetMinReservedRateString;
	attrStore[I18N.QoS.paramSetMinReservedPkt]=cmcQosParamSetObject[recordFlag].docsQosParamSetMinReservedPktString;
	attrStore[I18N.QoS.paramSetActiveTimeout]=cmcQosParamSetObject[recordFlag].docsQosParamSetActiveTimeoutString;
	attrStore[I18N.QoS.paramSetAdmittedTimeout]=cmcQosParamSetObject[recordFlag].docsQosParamSetAdmittedTimeoutString;
	attrStore[I18N.QoS.paramSetMaxConcatBurst]=cmcQosParamSetObject[recordFlag].docsQosParamSetMaxConcatBurstString;
	attrStore[I18N.QoS.paramSetSchedulingType]=cmcQosParamSetObject[recordFlag].docsQosParamSetSchedulingTypeName;
	attrStore[I18N.QoS.paramSetNomPollInterval]=cmcQosParamSetObject[recordFlag].nomPollInterval;
	attrStore[I18N.QoS.paramSetTolPollJitter]=cmcQosParamSetObject[recordFlag].tolPollJitter;
	attrStore[I18N.QoS.paramSetUnsolicitGrantSize]=cmcQosParamSetObject[recordFlag].unsolicitGrantSize;
	attrStore[I18N.QoS.paramSetNomGrantInterval]=cmcQosParamSetObject[recordFlag].nomGrantInterval;
	attrStore[I18N.QoS.paramSetTolGrantJitter]=cmcQosParamSetObject[recordFlag].tolPollJitter;
	attrStore[I18N.QoS.paramSetGrantsPerInterval]=cmcQosParamSetObject[recordFlag].nomGrantInterval;
	attrStore[I18N.QoS.paramSetMaxLatency]=cmcQosParamSetObject[recordFlag].maxLatency;
	attrStore[I18N.QoS.paramSetRequestPolicyOct]=cmcQosParamSetObject[recordFlag].requestPolicyOct;
}

function judgeObjIndex(recordFlag,length){
	if(length!=null&&length!=0){
		if(length<=1){
			$("#nextClick").attr("disabled", true);
			$("#prevClick").attr("disabled", true);
		}else{
			if(recordFlag==0){
				$("#prevClick").attr("disabled", true);
				$("#nextClick").attr("disabled", false);
			}
			if(recordFlag<length-1&&recordFlag>0){
				$("#nextClick").attr("disabled", false);
				$("#prevClick").attr("disabled", false);
			}
			if(recordFlag==length-1){
				$("#nextClick").attr("disabled", true);
				$("#prevClick").attr("disabled", false);
			}
		}
	}
}
Ext.onReady(function() {
	if(length!=0){
		attrStoreInfo(recordFlag);
		
		Ext.grid.PropertyGrid.prototype.setSource = function(source) {
	        delete this.propStore.store.sortInfo;
	        this.propStore.setSource(source);
	    };
		grid = new Ext.grid.PropertyGrid({
			renderTo : 'paramSet',
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
	}else{
		$("#paramSet").text('@QoS.noThisRecord@');
	}
});
function cancelClick() {
	window.top.closeWindow('serviceFlowSet');
}
function prevClick(){
	recordFlag -= 1;
	attrStoreInfo(recordFlag);
	grid.setSource(attrStore);
	$("#currentNum").replaceWith("<div id='currentNum'>" + String.format(I18N.QoS.displayCurrentItem, (recordFlag + 1)) + "</div>");
	judgeObjIndex(recordFlag,length);
}
function nextClick(){
	recordFlag += 1;
	attrStoreInfo(recordFlag);
	grid.setSource(attrStore);
	$("#currentNum").replaceWith("<div id='currentNum'>" + String.format(I18N.QoS.displayCurrentItem, (recordFlag + 1)) + "</div>");
	judgeObjIndex(recordFlag,length);
}
</script>
<title><fmt:message bundle="${cmc}" key="QoS.ServiceFlowParamSet"/></title>
</head>
<body class=POPUP_WND style="margin: 15px;" onload="">
	<div id="paramSet" style="padding-left:4px"> </div>
 	<div style="padding-top:10px;">	
		<table width=100%>
			<tr>
				<td><div id="currentNum"></div>
				</td>
				<td><div id="totalNum"></div>
				</td>
				<td valign=top align=right>
					<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
						onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message bundle='${resources}' key='COMMON.close'/></button>
				</td>
			</tr>
		</table>
  	</div>
</body>