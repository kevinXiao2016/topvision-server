<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML><HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css">
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>  
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var itemName = '<%= request.getParameter("itemName") %>';
function cancelClick() {
	window.parent.closeWindow('modalDlg');
}
function okClick() {
	var selEntity = Zeta$('entityId');
	var eid = selEntity.options[selEntity.selectedIndex].value;

	var selPort = Zeta$('portIndex');
	var pix = selPort.options[selPort.selectedIndex].value;

	var el = Zeta$('thresholdId');
	if (el.options.length == 0) {
		cancelClick();
	} else {
		var tid = el.options[el.selectedIndex].value;
		$.ajax({url:'relateThreshold.tv', data:{'entityId' : eid,
			'portIndex': pix, 'itemName': itemName, 'thresholdId': tid},
			dataType: 'json', cache: 'false',
			success:function(json){
				try {
					window.parent.getActiveFrame().refreshThresholdTree();
				} catch (err) {
				}
				cancelClick();
			}, error:function(){
				alert();
			}
		});
	}
}

function deviceChanged() {
	var el = Zeta$('folderId');
	var fid = el.options[el.selectedIndex].value;
	el = Zeta$('entityType');
	var typeId = el.options[el.selectedIndex].value;
	$.ajax({url:'queryEntityForMonitor.tv', data:{'folderId' : fid, 'entityType': typeId,
			'snmpSupport': Zeta$('snmpSupport').checked},
		dataType: 'json', cache: 'false',
		success:function(json){
			if (json) {
				var selEntity = Zeta$('entityId');
				var len = selEntity.options.length;
				for (var i = len - 1; i >= 1; i--) {
					selEntity.remove(i);
				}
				var selPort = Zeta$('portIndex');
				len = selPort.options.length;
				for (var i = len - 1; i >= 1; i--) {
					selPort.remove(i);
				}				
				for (var i = 0; i < json.length; i++) {
					var option = new Option(json[i].text, json[i].entityId); 
  					selEntity.options[i + 1] = option; 
				}
			}
		}, error:function(){
		}
	});
}

function portChanged() {
	var selEntity = Zeta$('entityId');
	var eid = selEntity.options[selEntity.selectedIndex].value;
	var selPort = Zeta$('portIndex');
	var len = selPort.options.length;
	for (var i = len - 1; i >= 1; i--) {
		selPort.remove(i);
	}
	if (eid == 0) {
		return;
	}
	$.ajax({url: 'queryPortForEntity.tv', data:{'entityId' : eid},
		dataType: 'json', cache: 'false',
		success:function(json){
			if (json) {			
				for (var i = 0; i < json.length; i++) {
					var option = new Option(json[i].text, json[i].portIndex); 
  					selPort.options[i + 1] = option; 
				}
			}
		}, error:function(){
		}
	});
}
</script>	  
</HEAD><BODY class=POPUP_WND style="padding:10px" onload="deviceChanged();">
<center>
<table cellspacing=10 cellpadding=0>
<tr><Td width=80px>&nbsp;</Td><td>
<input id="snmpSupport" checked type=checkbox onclick="deviceChanged();">
<label for="snmpSupport"><fmt:message key="supportSnmpEntity" bundle="${network}"/></label></td></tr>

<tr><td height=20 width=80px><fmt:message key="NETWORK.topoMap" bundle="${network}"/>:</td><td height=20>
	<select id="folderId" name="folderId" style="width:300px;" onchange="deviceChanged();">
		<s:iterator value="topoFolders">
		<option value="<s:property value="folderId"/>"><s:property value="name"/></option>
		</s:iterator>
	</select>
</td></tr>

<tr><td height=20><fmt:message key="NETWORK.labelEntityType" bundle="${network}"/>:</td><td height=20>
	<select id="entityType" name="entityType" style="width:300px;" onchange="deviceChanged();">
		<option value="0">-- <fmt:message key="ALERT.chooseType" bundle="${fault}"/> --</option>
		<s:iterator value="entityTypes">
		<option value="<s:property value="typeId"/>"><s:property value="displayName"/></option>
		</s:iterator>
	</select>
</td></tr>

<tr><td height=20><fmt:message key="NETWORK.labelEntityIp" bundle="${network}"/>:</td><td height=20>
	<select id="entityId" style="width:300px;" onchange="portChanged();">
		<option value="0"><fmt:message key="NETWORK.selectAllEntity" bundle="${network}"/></option>
		<s:iterator value="entities">
		<option value="<s:property value="entityId"/>"><s:property value="ip"/> [<s:property value="name"/>]</option>
		</s:iterator>
	</select>
</td></tr>

<tr><td height=20><fmt:message key="port.ifIndex" bundle="${network}"/>:</td><td height=20>
	<select id="portIndex" style="width:300px;">
		<option value="0"><fmt:message key="ALERT.allPorts" bundle="${fault}"/></option>
	</select>
</td></tr>

<tr><td height=20><fmt:message key="ALERT.threshold" bundle="${fault}"/>:</td><td height=20>
	<select id="thresholdId" style="width:300px;">
		<s:iterator value="thresholds">
		<s:if test="!defaultThreshold">
		<option value="<s:property value="thresholdId"/>"><s:property value="name"/> [<s:property value="description"/>]</option>
		</s:if>
		</s:iterator>
	</select>
</td></tr>	
<tr><td colspan=2 valign=top align=right>
<button id=okBt type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;
	<button type="button" class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button></td></tr>
<tr><td height=20></td></tr>	
</table></center>
</BODY></HTML>
