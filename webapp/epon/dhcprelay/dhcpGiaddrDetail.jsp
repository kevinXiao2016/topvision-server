<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="epon"/>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
#sequenceStep {
    padding-left : 460;position:absolute;top:290px;
}
</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _PRIMARY_DEVICE = ["ALL"];
var _POLICY_DEVICE = ["CM", "CPE"];
var _STRICT_DEVICE = ["CM", "HOST", "MTA", "STB"];
var deviceTypes = null;
var extendDevice = null;
var policy = <%= request.getParameter("policy") %>;
var giaddr = '<%= request.getParameter("giaddr") %>';
var gimask = '<%= request.getParameter("gimask") %>';
var deviceType = '<%= request.getParameter("deviceType") %>';
var action = <%= request.getParameter("action") %>;
var dhcpIntIpList = window.parent.dhcpRelay.dhcpIntIpList;
function checkIpConflict(ip, mask){
	var ipCheck = new IpAddrCheck(ip, mask);
	if(ipCheck.isSubnetConflict(giaddr, gimask)){
		return false
	}
	if(dhcpIntIpList){
		for(var i = 0; i < dhcpIntIpList.length; i++){		
			if(ipCheck.isSubnetConflict(dhcpIntIpList[i].topCcmtsDhcpIntIpAddr, dhcpIntIpList[i].topCcmtsDhcpIntIpMask)){
				return true;
			}
		}
	}
    return false;
}
function initData(){	
	if(policy == _POLICY_.primary){
		deviceTypes = _PRIMARY_DEVICE;
	}else if(policy == _POLICY_.policy){
		deviceTypes = _POLICY_DEVICE;
	}else{
		var p = window.parent.getWindow("createRelayConfig").body.dom.firstChild.contentWindow;
	    extendDevice = p.deviceData;
		deviceTypes = _STRICT_DEVICE;
		if(extendDevice){
			for(var i = 0; i < extendDevice.length; i++){
	            deviceTypes.push(extendDevice[i]);
	        }
		}
		
	}	
}
function initPage(){
	createDeviceTypeSelect();
	createIpInput();
	if(action == 2){
		$("#deviceType-select").val(deviceType);
		$("#deviceType-select").attr("disabled", true);
		setIpValue("giaddr", giaddr);
		setIpValue("gimask", gimask);
	}
}
//查找deviceType是否已经在giaddrList中存在
function findDeviceTypeInGiaddr(deviceType){
	var p = window.parent.getWindow("createRelayConfig").body.dom.firstChild.contentWindow;
    var giaddrList = p.giAdrList;
    if(giaddrList){
    	for(var i = 0; i < giaddrList.length; i++){
            if(deviceType == giaddrList[i].deviceTypeStr){
            	return true;
            }
        }
    }
    return false;
}
//创建设备类型选择select 
function createDeviceTypeSelect(){
	var options = "";	
	for(var i = 0; i < deviceTypes.length; i++){
		if(action == 1 && _POLICY_DEVICE[1] != deviceTypes[i] && findDeviceTypeInGiaddr(deviceTypes[i])){
			//当新增giaddr时，并且设备类型不为CPE时，设备类型已经在giaddr中存在了的不创建选择项
			continue;
		}
		options += "<option value='" + deviceTypes[i] + "'>" + deviceTypes[i] + "</option>";
	}
	$("#deviceType-select").append(options);
}
function createIpInput(){
	var giaddr = new ipV4Input("giaddr","giAddr-span");
	giaddr.width(141);
    var gimask = new ipV4Input("gimask","giMask-span");
    gimask.width(141);    
}
function checkIpValid(ip, mask){
	var ipCheck = new IpAddrCheck(ip, mask);
	if(!ipCheck.isHostIp()){
		return false;
	}
	if(checkIpConflict(ip, mask)){
		return false;
	}
	return true;
}
function checkIpInput(giaddrList){
	var ip = getIpValue("giaddr");
    var mask = getIpValue("gimask");
    if(ipIsFilled("giaddr") && ipIsFilled("gimask") && checkedIpMask(getIpValue("gimask"))){
        return checkIpValid(getIpValue("giaddr"), getIpValue("gimask"), giaddrList);
    }else{
        return false;
    }
}
function onIpInputEvt(){
	var p = window.parent.getWindow("createRelayConfig").body.dom.firstChild.contentWindow;
	var giaddrList = p.giAdrList;
	$("#okBt").attr("disabled", !checkIpInput(giaddrList));
}
function okClick(){
	var o = {};
	var giaddrTemp = getIpValue("giaddr");
	var gimaskTemp = getIpValue("gimask");
	var devicetypeTemp = $("#deviceType-select").val().trim();
	o.deviceTypeStr = devicetypeTemp;
	o.topCcmtsDhcpGiAddress = giaddrTemp;
	o.topCcmtsDhcpGiAddrMask = gimaskTemp;
	var p = window.parent.getWindow("createRelayConfig").body.dom.firstChild.contentWindow;
    if(!checkIpValid(giaddr, gimask)){
        return;
    }
    if(action == 1){
    	p.giAdrList.push(o);
    	var intIp = {};
    	intIp.topCcmtsDhcpIntIpAddr = giaddrTemp;
    	intIp.topCcmtsDhcpIntIpMask = gimaskTemp;
    	window.parent.dhcpRelay.dhcpIntIpList.push(intIp);
    }else{
    	for(var i = 0; i < p.giAdrList.length; i++){
    		if(p.giAdrList[i].topCcmtsDhcpGiAddress == giaddr){
    			p.giAdrList[i].topCcmtsDhcpGiAddress = giaddrTemp;
    			p.giAdrList[i].topCcmtsDhcpGiAddrMask = gimaskTemp;
    			break;
    		}
    	}
    	for(var i = 0; i < window.parent.dhcpRelay.dhcpIntIpList.length; i++){
    		if(window.parent.dhcpRelay.dhcpIntIpList[i].topCcmtsDhcpIntIpAddr == giaddr){
    			window.parent.dhcpRelay.dhcpIntIpList[i].topCcmtsDhcpIntIpAddr = giaddrTemp;
    			window.parent.dhcpRelay.dhcpIntIpList[i].topCcmtsDhcpIntIpMask = gimaskTemp;
    			break;
    		}
    	}
    }    
    p.initGiaddrSpan();
    cancelClick();
}
function cancelClick(){
	window.parent.closeWindow("giaddrDetail");
}
$(function (){
	initData();
	initPage();
});
</script>
</HEAD>
<BODY class="POPUP_WND">
<table style="width:92%; height: 120px; margin: 20px 10px 10px 10px; " cellspacing=0 cellpadding=0 bgcolor="white">
<tr>
    <td><label><fmt:message bundle="${epon}" key="DHCPRELAY.entitytype"/>: </label></td>
	<td>
	   <select id="deviceType-select" style="width: 141px;">
	   </select>
	</td>
</tr>
<tr>
	<td><label><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpGiaddr"/>:</label></td>
	<td onkeyup="onIpInputEvt()"><span id="giAddr-span"></span></td>
</tr>
<tr>
    <td><label><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpGiaddrMask"/>:</label></td>
    <td onkeyup="onIpInputEvt()"><span id="giMask-span"></span></td>
</tr>
</table>
<div align="right" style='margin-top:6px;'>
    <button id=okBt  class=BUTTON75 onclick='okClick()' disabled><fmt:message bundle="${epon}" key="DHCPRELAY.sure"/></button>&nbsp;&nbsp;
    <button class=BUTTON75 onclick='cancelClick()'><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></button>
</div>
</BODY>
</HTML>