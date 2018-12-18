<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript">
var _POLICY_MODE = ["", "primary", "policy", "strict"];
var _CABLE_SOURCE_VERIFY = ["", I18N.CMC.select.open, I18N.CMC.select.close];
var _DEVICE_TYPE = ["", "CM", "HOST", "MTA", "STB", "ALL"];
var result;
function initPage(){
	var text = "";
	if(result.errorType && result.errorType == "bundle"){
		text += I18N.CMC.label.setting + "\n\n" + result.error.topCcmtsDhcpBundleInterface
		+"\n\n" + I18N.CMC.label.dhcpRelayPolicy + _POLICY_MODE[result.error.topCcmtsDhcpBundlePolicy] + "\r"
		+"\n\n" + I18N.CMC.label.dhcpCableSourceVerify + ":"+ _CABLE_SOURCE_VERIFY[result.error.cableSourceVerify]+ "\r"
		+"\n\nPrimary IP:" + result.error.virtualPrimaryIpAddr + "\n/\n" + result.error.virtualPrimaryIpMask+ "\r";
		text +=I18N.CMC.label.failure + "\r";
	}else{
		text += I18N.CMC.label.setting + "\n\nBundle\r" 
		+ "\n\n" + result.bundle.topCcmtsDhcpBundleInterface + "\r"
        +"\n\n" + I18N.CMC.label.dhcpRelayPolicy + _POLICY_MODE[result.bundle.topCcmtsDhcpBundlePolicy] + "\r"
        +"\n\n" + I18N.CMC.label.dhcpCableSourceVerify + ":" + _CABLE_SOURCE_VERIFY[result.bundle.cableSourceVerify] + "\r"
        +"\n\n\n\nPrimary IP:" + result.bundle.virtualPrimaryIpAddr + "\n/\n" + result.bundle.virtualPrimaryIpMask + "\r";
		text += I18N.COMMON.success +"\r";
	}
	if(result.errorType && result.errorType == "giaddr"){
		if(result.giaddr && result.giaddr.length > 0){
			text += I18N.CMC.label.setting + "\nGiaddr:\r";
			for(var i = 0; i < result.giaddr.length; i++){
				text += "\n\n" +_DEVICE_TYPE[result.giaddr[i].topCcmtsDhcpGiAddrDeviceType] + ":" 
			    + result.giaddr[i].topCcmtsDhcpGiAddress + "\r";
			}
			text += I18N.COMMON.success +"\r";
		}
		text += I18N.CMC.label.setting + "\nGiaddr\n" + _DEVICE_TYPE[result.error.topCcmtsDhcpGiAddrDeviceType] + ":" 
        + result.error.topCcmtsDhcpGiAddress + I18N.CMC.label.failure + "\r";
	}else{
		if(result.giaddr && result.giaddr.length > 0){
            text += I18N.CMC.label.setting + "\nGiaddr:\r";
            for(var i = 0; i < result.giaddr.length; i++){
                text += "\n\n" +_DEVICE_TYPE[result.giaddr[i].topCcmtsDhcpGiAddrDeviceType] + ":" 
                + result.giaddr[i].topCcmtsDhcpGiAddress+ "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
	}
	if(result.errorType && result.errorType == "deleteOption"){
		if(result.deleteOption && result.deleteOption.length > 0){
            text += I18N.CMC.label.deleteText + "\nOption60:\r";
            for(var i = 0; i < result.deleteOption.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deleteOption[i].topCcmtsDhcpOption60DeviceType] + ":" 
                + result.deleteOption[i].topCcmtsDhcpOption60Str+ "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.deleteText + "\nOption60 " + _DEVICE_TYPE[result.error.topCcmtsDhcpOption60DeviceType] + ":" 
        + result.error.topCcmtsDhcpOption60Str + I18N.CMC.label.failure + "\r";
	}else {
		if(result.deleteOption && result.deleteOption.length > 0){
            text += I18N.CMC.label.deleteText + "\nOption60:\r";
            for(var i = 0; i < result.deleteOption.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deleteOption[i].topCcmtsDhcpOption60DeviceType] + ":" 
                + result.deleteOption[i].topCcmtsDhcpOption60Str + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
	}
	if(result.errorType && result.errorType == "deleteServer"){
        if(result.deleteServer && result.deleteServer.length > 0){
            text += I18N.CMC.label.deleteText + "\nDHCP Server:\r";
            for(var i = 0; i < result.deleteServer.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deleteServer[i].topCcmtsDhcpHelperDeviceType] + ":" 
                + result.deleteServer[i].topCcmtsDhcpHelperIpAddr+ "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.deleteText + "\nDHCP Server " + _DEVICE_TYPE[result.error.topCcmtsDhcpHelperDeviceType] + ":" 
        + result.error.topCcmtsDhcpHelperIpAddr + I18N.CMC.label.failure + "\r";
    }else {
        if(result.deleteServer && result.deleteServer.length > 0){
            text += I18N.CMC.label.deleteText + "\nDHCP Server:\r";
            for(var i = 0; i < result.deleteServer.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deleteServer[i].topCcmtsDhcpHelperDeviceType] + ":" 
                + result.deleteServer[i].topCcmtsDhcpHelperIpAddr + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
	if(result.errorType && result.errorType == "deleteVirtualIp"){
        if(result.deleteVirtualIp && result.deleteVirtualIp.length > 0){
            text += I18N.CMC.label.deleteText + "\nSecondary:\r";
            for(var i = 0; i < result.deleteVirtualIp.length; i++){
                text += "\n\n" + result.deleteVirtualIp[i].topCcmtsDhcpIntIpAddr + "\n/\n" 
                + result.deleteVirtualIp[i].topCcmtsDhcpIntIpMask + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.deleteText + "\nSecondary " + result.error.topCcmtsDhcpIntIpAddr + "\n/\n" 
        + result.error.topCcmtsDhcpIntIpMask + I18N.CMC.label.failure + "\r";
    }else {
        if(result.deleteVirtualIp && result.deleteVirtualIp.length > 0){
            text += I18N.CMC.label.deleteText + "\nSecondary IP:\r";
            for(var i = 0; i < result.deleteVirtualIp.length; i++){
                text += "\n\n" + result.deleteVirtualIp[i].topCcmtsDhcpIntIpAddr + "\n/\n" 
                + result.deleteVirtualIp[i].topCcmtsDhcpIntIpMask + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
	if(result.errorType && result.errorType == "addServer"){
        if(result.addServer && result.addServer.length > 0){
            text += I18N.CMC.label.add + "\nDHCP Server:\r";
            for(var i = 0; i < result.addServer.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.addServer[i].topCcmtsDhcpHelperDeviceType] + ":" 
                + result.addServer[i].topCcmtsDhcpHelperIpAddr + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.add + "\nDHCP Server " + _DEVICE_TYPE[result.error.topCcmtsDhcpHelperDeviceType] + ":" 
        + result.error.topCcmtsDhcpHelperIpAddr + I18N.CMC.label.failure + "\r";
    }else {
        if(result.addServer && result.addServer.length > 0){
            text += I18N.CMC.label.add + "\nDHCP Server:\r";
            for(var i = 0; i < result.addServer.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.addServer[i].topCcmtsDhcpHelperDeviceType] + ":" 
                + result.addServer[i].topCcmtsDhcpHelperIpAddr + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
	if(result.errorType && result.errorType == "addOption"){
        if(result.addOption && result.addOption.length > 0){
            text += I18N.CMC.label.add + "\nOption60:\r";
            for(var i = 0; i < result.addOption.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.addOption[i].topCcmtsDhcpOption60DeviceType] + ":" 
                + result.addOption[i].topCcmtsDhcpOption60Str + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.add + "\nOption60 " + _DEVICE_TYPE[result.error.topCcmtsDhcpOption60DeviceType] + ":" 
        + result.error.topCcmtsDhcpOption60Str + I18N.CMC.label.failure + "\r";
    }else {
        if(result.addOption && result.addOption.length > 0){
            text += I18N.CMC.label.add + "\nOption60:\r";
            for(var i = 0; i < result.addOption.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.addOption[i].topCcmtsDhcpOption60DeviceType] + ":" 
                + result.addOption[i].topCcmtsDhcpOption60Str + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }    
    if(result.errorType && result.errorType == "addVirtualIp"){
        if(result.addVirtualIp && result.addVirtualIp.length > 0){
            text += I18N.CMC.label.add + "\nSecondary:\r";
            for(var i = 0; i < result.addVirtualIp.length; i++){
                text += "\n\n" + result.addVirtualIp[i].topCcmtsDhcpIntIpAddr + "\n/\n" 
                + result.addVirtualIp[i].topCcmtsDhcpIntIpMask + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.add + "\nSecondary " + result.error.topCcmtsDhcpIntIpAddr + "\n/\n" 
        + result.error.topCcmtsDhcpIntIpMask + I18N.CMC.label.failure + "\r";
    }else {
        if(result.addVirtualIp && result.addVirtualIp.length > 0){
            text += I18N.CMC.label.add + "\nSecondary IP:\r";
            for(var i = 0; i < result.addVirtualIp.length; i++){
                text += "\n\n" + result.addVirtualIp[i].topCcmtsDhcpIntIpAddr + "\n/\n" 
                + result.addVirtualIp[i].topCcmtsDhcpIntIpMask + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
    if(result.errorType && result.errorType == "modifyPacketVlan"){
        if(result.modifyPacketVlan && result.modifyPacketVlan.length > 0){
            text += I18N.CMC.label.setting + "\nPacket VLAN \r";
            for(var i = 0; i < result.modifyPacketVlan.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.modifyPacketVlan[i].topCcmtsDhcpDeviceType] +":" 
                + "VLAN:" + result.modifyPacketVlan[i].topCcmtsDhcpTagVlan + ", " 
                + I18N.CMC.label.vlanPriority + ":" + result.modifyPacketVlan[i].topCcmtsDhcpTagPriority + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.setting + "\nPacket VLAN " + _DEVICE_TYPE[result.modifyPacketVlan[i].topCcmtsDhcpDeviceType] +":" 
        + "VLAN:" + result.error.topCcmtsDhcpTagVlan + ", " 
        + I18N.CMC.label.vlanPriority + ":" + result.error.topCcmtsDhcpTagPriority + I18N.CMC.label.failure;
    }else {
        if(result.modifyPacketVlan && result.modifyPacketVlan.length > 0){
            text += I18N.CMC.label.setting + "\nPacket VLAN\r";
            for(var i = 0; i < result.modifyPacketVlan.length; i++){
            	text += "\n\n" + _DEVICE_TYPE[result.modifyPacketVlan[i].topCcmtsDhcpDeviceType] +":" 
                + "VLAN:" + result.modifyPacketVlan[i].topCcmtsDhcpTagVlan + ", " 
                + I18N.CMC.label.vlanPriority + ":" + result.modifyPacketVlan[i].topCcmtsDhcpTagPriority + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
    if(result.errorType && result.errorType == "deletePacketVlan"){
        if(result.deletePacketVlan && result.deletePacketVlan.length > 0){
            text += I18N.CMC.label.deleteText + "\nPacket VLAN:\r";
            for(var i = 0; i < result.deletePacketVlan.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deletePacketVlan[i].topCcmtsDhcpDeviceType] +":" 
                + "VLAN:" + result.deletePacketVlan[i].topCcmtsDhcpTagVlan + ", " 
                + I18N.CMC.label.vlanPriority + ":" + result.deletePacketVlan[i].topCcmtsDhcpTagPriority + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
        text += I18N.CMC.label.deleteText + "\nPacket VLAN " + _DEVICE_TYPE[result.error.topCcmtsDhcpDeviceType] +":" 
        + "VLAN:" + result.error.topCcmtsDhcpTagVlan + ", " 
        + I18N.CMC.label.vlanPriority + ":" + result.error.topCcmtsDhcpTagPriority + I18N.CMC.label.failure;
    }else {
        if(result.deletePacketVlan && result.deletePacketVlan.length > 0){
            text += I18N.CMC.label.deleteText + "\nPacket VLAN:\r";
            for(var i = 0; i < result.deletePacketVlan.length; i++){
                text += "\n\n" + _DEVICE_TYPE[result.deletePacketVlan[i].topCcmtsDhcpDeviceType] +":" 
                + "VLAN:" + result.deletePacketVlan[i].topCcmtsDhcpTagVlan + ", " 
                + I18N.CMC.label.vlanPriority + ":" + result.deletePacketVlan[i].topCcmtsDhcpTagPriority + "\r";
            }
            text += I18N.COMMON.success +"\r";
        }
    }
	$("#result-textarea").text(text);
	$("#result-textarea").scrollTop($("#result-textarea")[0].scrollHeight)
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
$(function (){
	result = window.parent.dhcpRelay.result;
	initPage();
});
</script>
</HEAD>
<body class="openWinBody">
    <div class="openWinHeader">
        <div class="openWinTip">
            <p><b class="orangeTxt"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayConfig"/></b></p>
            <p><span id="newMsg"><fmt:message bundle="${cmc}" key="CMC.text.dhcpRelaySetResult"/></span></p>
        </div>
        <div class="rightCirIco wheelCirIco"></div>
    </div>
    <div class="edgeTB10LR20 pT40">
        <textarea id="result-textarea" cols="145" rows="16" readonly="readonly" style="border:1px solid #ccc;"></textarea >
    </div>
    <div class="noWidthCenterOuter clearBoth" id="sequenceStep">
         <ol class="upChannelListOl pB20 pT10 noWidthCenter">
             <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
         </ol>
    </div>
</body>
</HTML>