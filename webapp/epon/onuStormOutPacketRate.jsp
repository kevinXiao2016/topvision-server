<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/nocache.inc"%>
<%@ include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '../images/s.gif';
var entityId = <s:property value="entityId"/>;
var uniId = <s:property value="uniId"/>;
var uniIndex = <s:property value="uniIndex"/>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var unicastStorm = '<s:property value="uniStorm.unicastStormOutPacketRate" />';
var multicastStorm = '<s:property value="uniStorm.multicastStormOutPacketRate" />';
var broadcastStorm = '<s:property value="uniStorm.broadcastStormOutPacketRate" />';

function cancelClick(){
	window.parent.closeWindow('onuStormOutPacketRate');
}
function refreshClick(){
	$("#refreshBt").attr("disabled", true).mouseout();
	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.fetching , 'ext-mb-waiting');
	var params = {entityId : entityId};
	Ext.Ajax.request({
		url : '/onu/refreshOnuStormOutPacketRate.tv?&r=' + Math.random(),
		success : function(response) {
			if(response.responseText == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
				location.reload();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
				$("#refreshBt").attr("disabled", false);
			}
		},
		failure : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
			$("#refreshBt").attr("disabled", false);
		},
		params: params
	});
}
function saveClick(){
	var u = $("#unicastSel").val();
	var m = $("#mutilcastSel").val();
	var b = $("#broadcastSel").val();
	if(u == 1){
		u = $("#unicastInput").val();
	}
	if(m == 1){
		m = $("#mutilcastInput").val();
	}
	if(b == 1){
		b = $("#broadcastInput").val();
	}
	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.saving , 'ext-mb-waiting');
	var params = {entityId : entityId, uniId: uniId, unicastStormOutPacketRate: u, multicastStormOutPacketRate: m,broadcastStormOutPacketRate:b};
	Ext.Ajax.request({
		url : '/onu/modifyOnuStormOutPacketRate.tv?&r=' + Math.random(),
		success : function(response) {
			if(response.responseText == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.modifyStormSuc);
				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.modifyStormFail);
			}
		},
		failure : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ONU.modifyStormFail);
		},
		params: params
	});
}

Ext.onReady(function(){
	initButton(95);
	initData();
});
function initData(){
	if(uniIndex){
		var loc = "unknown uniIndex";
		try{
			if(window.parent.getFrame("entity-" + entityId) != null){
				loc = window.parent.getFrame("entity-" + entityId).getLocationByIndex(uniIndex, 'uni');
			}else if(window.parent.getFrame("onuList")){
				loc = window.parent.getFrame("onuList").getLocationByIndex(uniIndex, 'uni');
			}
		}catch (e) {
		}
		window.parent.getWindow("onuStormOutPacketRate").setTitle(I18N.ONU.onuStormOutPacketRate
				+ " ( UNI: " + loc + " ) ");
	}
	var u = unicastStorm ? (parseFloat(unicastStorm) < 2147483648 ? parseFloat(unicastStorm) : 0) : 0;
	var m = multicastStorm ? (parseFloat(multicastStorm) < 2147483648 ? parseFloat(multicastStorm) : 0) : 0;
	var b = broadcastStorm ? (parseFloat(broadcastStorm) < 2147483648 ? parseFloat(broadcastStorm) : 0) : 0;
	if(u){
		if(u == -1){//全部通过
			$("#unicastSel").val(u).change();
		}else{//部分抑制
			$("#unicastSel").val(1).change();
			$("#unicastInput").val(u);
		}
	}else{//全部抑制
		$("#unicastSel").val(u).change();
	}
	if(m){
		if(m == -1){//全部通过
			$("#mutilcastSel").val(m).change();
		}else{//部分抑制
			$("#mutilcastSel").val(1).change();
			$("#mutilcastInput").val(m);
		}
	}else{//全部抑制
		$("#mutilcastSel").val(m).change();
	}
	if(b){
		if(b == -1){//全部通过
			$("#broadcastSel").val(b).change();
		}else{//部分抑制
			$("#broadcastSel").val(1).change();
			$("#broadcastInput").val(b);
		}
	}else{//全部抑制
		$("#broadcastSel").val(b).change();
	}
	if(operationDevicePower){
		$("#saveBt").attr("disabled", false);
	}
	$("#refreshBt").attr("disabled", false);
}


function selChange(el){
	var tmp = !($(el).val() == 1);
	if(operationDevicePower){
		$("#saveBt").attr("disabled", false);
		$($(el).parent()).next().find("input").attr("disabled", tmp).css("border", "1px solid #" + (tmp ? "cccccc" : "8bb8f3")).focus();
	}
}
function inputChange(el){
	var v = $(el).val();
	var tmp = (v && v < 2147483648 && !isNaN(v) && v.indexOf(".") == -1 && v.indexOf("-") == -1);
	$(el).css("border", "1px solid #" + (tmp ? "8bb8f3" : "ff0000"));
	$("#saveBt").attr("disabled", !tmp).mouseout();
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#closeBt").attr("disabled",false);
		$("#refreshBt").attr("disabled",false);
	}
}
</script>
</head>
<body class=POPUP_WND style="padding: 10 10 10 10;" onload="authLoad()">
<fieldset style='width:400px;height:220px;background-color:white;padding:0 0 0 30'>
		<table>
			<tr height=30><td width=170 style="padding-top:15px;"><fmt:message bundle="${i18n}" key="ONU.unicastRate" />
			</td></tr>
			<tr><td style="padding-top:10px;">
				<select id=unicastSel onchange='selChange(this)' style='width:100px;margin-left:20px;'>
					<option value=0><fmt:message bundle="${i18n}" key="ONU.inhibitionAll" /></option>
					<option value=1><fmt:message bundle="${i18n}" key="ONU.inhibitionSome" /></option>
					<option value=-1><fmt:message bundle="${i18n}" key="ONU.inhibitionNo" /></option>
				</select>
			</td><td>
				<input id=unicastInput onkeyup='inputChange(this)' onblur='inputChange(this)'
					style='width:100px;border:1px solid #cccccc' disabled maxlength=10
					title="<fmt:message bundle="${i18n}" key="ONU.stormTip" />" />
			</td></tr>
			<tr height=30><td style="padding-top:15px;"><fmt:message bundle="${i18n}" key="ONU.mutilcastRate" />
			</td></tr>
			<tr><td style="padding-top:10px;">
				<select id=mutilcastSel onchange='selChange(this)' style='width:100px;margin-left:20px;'>
					<option value=0><fmt:message bundle="${i18n}" key="ONU.inhibitionAll" /></option>
					<option value=1><fmt:message bundle="${i18n}" key="ONU.inhibitionSome" /></option>
					<option value=-1><fmt:message bundle="${i18n}" key="ONU.inhibitionNo" /></option>
				</select>
			</td><td style="padding-top:15px;">
				<input id=mutilcastInput onkeyup='inputChange(this)' onblur='inputChange(this)'
					style='width:100px;border:1px solid #cccccc' disabled maxlength=10
					title="<fmt:message bundle="${i18n}" key="ONU.stormTip" />" />
			</td></tr>
			<tr height=30><td style="padding-top:15px;"><fmt:message bundle="${i18n}" key="ONU.broadcastRate" />
			</td></tr>
			<tr><td style="padding-top:10px;">
				<select id=broadcastSel onchange='selChange(this)' style='width:100px;margin-left:20px;'>
					<option value=0><fmt:message bundle="${i18n}" key="ONU.inhibitionAll" /></option>
					<option value=1><fmt:message bundle="${i18n}" key="ONU.inhibitionSome" /></option>
					<option value=-1><fmt:message bundle="${i18n}" key="ONU.inhibitionNo" /></option>
				</select>
			</td><td style="padding-top:15px;">
				<input id=broadcastInput onkeyup='inputChange(this)' onblur='inputChange(this)'
					style='width:100px;border:1px solid #cccccc' disabled maxlength=10
					title="<fmt:message bundle="${i18n}" key="ONU.stormTip" />" />
			</td></tr>
		</table>
	</fieldset>
	<div id="button" align="right" style="padding-top:6px;">
			<button id=saveBt class=BUTTON95 onclick='saveClick()' disabeled>
				<fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>&nbsp;&nbsp;
			<button id=refreshBt class=BUTTON95 onclick='refreshClick()' disabeled>
				<fmt:message bundle="${i18n}" key="COMMON.fetch" /></button>&nbsp;&nbsp;
			<button id=closeBt class=BUTTON95 onclick='cancelClick()'>
				<fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
</body>
<style>
</style>