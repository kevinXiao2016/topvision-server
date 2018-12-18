<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	LIBRARY EXT
	LIBRARY JQUERY 
	LIBRARY ZETA
    MODULE  PLATFORM
</Zeta:Loader>

<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>

<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript">
var linkId = <s:property value="linkId"/>;
function selectPort(linkId,entityId,ifIndex,isSrc) {
    top.createDialog('modalDlg', '<fmt:message bundle="${res}" key="topo.linkProperty.choosePort"/>', 600, 370,
        'link/showLinkSelectPortJsp.tv?linkId='+linkId+'&srcEntityId='+
        entityId+'&linkEx.srcPortIndex='+ifIndex, null, true, true,
        function(){
            if (window.top.ZetaCallback.type != 'ok') {return;}
            var params = 'linkId='+linkId+'&linkEx.srcPortIndex='+window.top.ZetaCallback.ifIndex+'&linkEx.startArrow='+isSrc; 
			$.ajax({url: '../link/changePortOfLink.tv', type: 'POST', 
				data: params, success: function() {
					location.href = "showLinkPropertyJsp.tv?linkId=" + linkId;
				}, error: function() {
					window.parent.showErrorDlg();
				}, dataType: 'plain', cache: false});            
            window.parent.ZetaCallback = null;
    });
}
function onSaveClick() {	
	$.ajax({url: '../link/updateLinkOutline.tv', type: 'POST', 
		data: jQuery(linkForm).serialize(), success: function() {
			//window.top.showMessageDlg('<fmt:message bundle="${res}" key="COMMON.tip"/> ',  '<fmt:message bundle="${res}" key="COMMON.updateSuccessfully"/>');
			top.afterSaveOrDelete({
				title : '<fmt:message bundle="${res}" key="COMMON.tip"/> ',
				html : '<fmt:message bundle="${res}" key="COMMON.updateSuccessfully"/>'
			});
			
			var frame = window.top.getActiveFrame();
			try {
				frame.synUpdateLinkName(linkId, Zeta$('name').value.trim());
			} catch (err) {
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'plain', cache: false});
}
function onFlowClick() {
	window.open('../realtime/showLinkRealFlowJsp.tv?linkId=' + linkId, 'linkflow' + linkId);
	//window.parent.addView('linkflow' + linkId, I18N.NETWORK.linkFlowAanlyse,
		//'flowTabIcon', 'realtime/showLinkRealFlowJsp.tv?linkId=' + linkId);
}
<%
	boolean linkPower = uc.hasPower("topoEdit");
%>
$(function(){
	var t1 = new TipTextArea({
   		id: "myNote"
   	});
   	t1.init();
   	var t2 = new TipTextArea({
   		id: "name"
   	});
   	t2.init();
});//end document.ready;
</script>
</head>
<body class="sideMapBg">
	<div class="edge10">
	<form name=linkForm>
		<input type=hidden name="linkId" value="<s:property value="linkId"/>">
		
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<tr>
					<td width=100 class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.sEntityName"/>:</td>
					<td colspan="2" class="wordBreak"><s:property value="linkEx.srcEntityName" />
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.sEntityIP"/>:</td>
					<td colspan=2 class="wordBreak"><s:property value="linkEx.srcIp" />
					</td>
				</tr>
				<%-- <tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.sEntityPort"/>:</td>
					<td class="wordBreak"><s:property value="linkEx.srcPortIndex" />
					</td>
					
				</tr> --%>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.sPortName"/>:</td>
					<td class="wordBreak"><s:property value="linkEx.srcPortName" />
					</td>
					<%
					if (linkPower) {
					%>
					<td  class="rightBlueTxt">
						
						
					</td>
					<%}%>
				</tr>
				<%-- <tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.sPortMAC"/>:</td>
					<td colspan=2 class="wordBreak"><s:property value="linkEx.srcPortMac" />
					</td>
				</tr> --%>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.tEntityName"/>:</td>
					<td colspan=2 class="wordBreak"><s:property value="linkEx.destEntityName" />
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.tEntityIP"/>:</td>
					<td colspan=2 class="wordBreak"><s:property value="linkEx.destIp" />
					</td>
				</tr>
				<%-- <tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.tEntityPort"/>:</td>
					<td class="wordBreak"><s:property value="linkEx.destPortIndex" />
					</td>
					
				</tr> --%>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.tPortName"/>:</td>
					<td class="wordBreak"><s:property value="linkEx.destPortName" />
					</td>
					<%
					if (linkPower) {
					%>
					<td align=right class="wordBreak">
											
					</td>
					<%}%>
				</tr>
				<%-- <tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.tPortMAC"/>:</td>
					<td colspan=2><s:property value="linkEx.destPortMac" />
					</td>
				</tr> --%>

				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.linkProperty.bandwidth"/>(bps):</td>
					<td colspan=2><input  name="linkEx.ifSpeed" id="bandWidth" type=text class="normalInput"
						style="width: 200px" value="<s:property value="linkEx.ifSpeed"/>" />
					</td>
				</tr>

				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="COMMON.name"/>:</td>
					<td colspan=2>
                        <textarea id="name" rows=2 class="normalInput" toolTip='@COMMON.max63@' maxlength="63" name="linkEx.name" style="width: 200px;height:40px;" value="" ><s:property value="linkEx.name" /></textarea>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="RECYLE.note"/>:</td>
					<td colspan=2>
                        <textarea id="myNote" class="normalInput" rows=5 toolTip='@COMMON.max63@' maxlength="63" style="width: 200px; height:40px;" name="linkEx.note" value=""><s:property value="linkEx.note" /></textarea>
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2">
    					<%
						if (linkPower) {
						%>
						<a href="javascript:;" class="normalBtn" onclick="onSaveClick()"><span><i class="miniIcoEdit"></i><fmt:message bundle="${res}" key="topo.linkProperty.modify"/></span></a>
						
						<%}%>
					</td>
				</tr>
			</table>
	</form>
	</div>
</body>
</Zeta:HTML>