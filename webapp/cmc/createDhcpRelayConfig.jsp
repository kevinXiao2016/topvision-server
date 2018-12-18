<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var _RELAYMODE_ = {snooping: 0, l2url: 1, l3url: 2}
var relayMode = 3;
var entityId = ${entityId};
var cmcId = '${cmcId}';
var dhcpRelayConfig = ${dhcpRelayConfig};
var cmcDhcpBaseConfig = ${cmcDhcpBaseConfig};
var cmcDhcpIntIpList = ${cmcDhcpIntIpList}
var bundleEndList;
var action = '${action}';
var vlanList = ${vlanList};
var bundleInterface = '${bundleInterface}';
/***************************************************************************
 *
 * 初始化
 * 
 ***************************************************************************/
function initData(){
	//将获取到的数据放到window.parent中，供下个页面使用
	window.parent.dhcpBaseConfig = cmcDhcpBaseConfig;
	bundleEndList = dhcpRelayConfig.bundleListEnd;
	window.parent.intIpList = cmcDhcpIntIpList
	if(!window.parent.dhcpRelay){
		window.parent.dhcpRelay = {};
       	if(dhcpRelayConfig){
       		window.parent.dhcpRelay.bundle= dhcpRelayConfig.cmcDhcpBundle;
       		if(dhcpRelayConfig.cmcDhcpBundle){
       			window.parent.dhcpRelay.bundle.policyOld = dhcpRelayConfig.cmcDhcpBundle.topCcmtsDhcpBundlePolicy;
       		}             
       		window.parent.dhcpRelay.giAddrList = dhcpRelayConfig.cmcDhcpGiAddr;
            window.parent.dhcpRelay.giAddrListOld = dhcpRelayConfig.cmcDhcpGiAddr;
       		window.parent.dhcpRelay.virtualIpList = dhcpRelayConfig.virtualIp;
       		window.parent.dhcpRelay.serverList = dhcpRelayConfig.cmcDhcpServer;
       		window.parent.dhcpRelay.option60List = dhcpRelayConfig.cmcDhcpOption60;
       		window.parent.dhcpRelay.packetVlanList = dhcpRelayConfig.cmcDhcpPacketVlan;
       	}         
	}
     
}

function initPage(){
    if(window.parent.dhcpRelay && window.parent.dhcpRelay.bundle ){     
        if(window.parent.dhcpRelay.bundle.cableSourceVerify && 
                window.parent.dhcpRelay.bundle.cableSourceVerify == 2){
            $(":radio[name='cableSourceVerify'][value=2]").attr("checked",true);
            
        }else{
            $(":radio[name='cableSourceVerify'][value=1]").attr("checked",true);
        }
        $("#relayConfigIndex").val(transferBundleToNumber(window.parent.dhcpRelay.bundle.bundleInterface));
        $("#policySelect").val(window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy);
        $("cableSourceVerify").val(window.parent.dhcpRelay.bundle.cableSourceVerify);
        $("#relayConfigIndex").attr("disabled", true);
    }else{
        $(":radio[name='cableSourceVerify'][value=1]").attr("checked",true);
        $("#relayConfigIndex").val(transferBundleToNumber(initBundleIndexInputValue(bundleEndList)));
    }   
    
}
/**
 * 获取未使用的bundle的值 ,必须保证list为排好顺序的
 */
function initBundleIndexInputValue(list){
	if(list){
		var index = list.length;
		var begin = ["1","0"];
	    for(var i = 0; i < list.length; i++){
		    var compare = list[i].split(".");
		    if(compare.length<2){
		    	compare.push("0");
		    }
		    if(parseInt(begin[1]) < parseInt(compare[1])){
		    	break;
		    }
		    begin[1] = parseInt(compare[1]) + 1;
	    }
	    if(begin[1] == "0"){
	    	return 1;
	    }else{
	    	return begin[0]+"."+ begin[1];
	    }		
	}else{
		return 1;
	}
}
/*****************************************************************************************
 * 数据校验
 *****************************************************************************************/
function isBundleIdInput(val){
	var reg = /^[1-3]\d{1}$|^[1-9]$/;
    return reg.test(val);
}
function isExitsInList(val){
	for(var i = 0; i < bundleEndList.length; i++){
		if(val == bundleEndList[i]){
			return true;
		}
	}
	return false;
}
function transferBundleToNumber(val){
	if(typeof val == "string"){
		var bundleArray = val.split(".");
	    var number;
	    if(bundleArray.length > 1){
	        number = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
	    }else{
	        number = parseInt(bundleArray[0]);
	    }
	    return number;
	}else{
		return val;
	}
    
}
function transferNumberToBundle(number){
    var bundle = "1";
    if(number > 1){
        bundle += "." + (number-1);
    }
    return bundle;
}
/*****************************************************************************************
 * 按钮事件方法
 *****************************************************************************************/
function nextClick(){ 
	if(!window.parent.dhcpRelay.bundle){
		window.parent.dhcpRelay.bundle = {};
	}
	window.parent.dhcpRelay.vlanList = vlanList;
    window.parent.dhcpRelay.bundle.bundleInterface = transferNumberToBundle($("#relayConfigIndex").val());
    window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy = $("#policySelect").val();
    window.parent.dhcpRelay.bundle.cableSourceVerify = $(":radio[checked=true]").val();
    window.parent.entityId = entityId;
    window.parent.dhcpRelay.cmcId = cmcId;
    window.parent.dhcpRelay.bundleInterface = bundleInterface;
    window.parent.dhcpRelay.action = action;
    window.location.assign("/cmc/createDhcpRelayStep2.jsp?cmcId=" + cmcId + "&action=" + action + "&bundleInterface=" + bundleInterface);
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function onBundleIdChange(val){
	var disabled = true;
	if(isBundleIdInput(val) && !isExitsInList(transferNumberToBundle(val))){
		if(val>=1 && val <= 32){
		  disabled = false;
		}
	}
	$("#nextStep").attr("disabled", disabled);
}
$(function (){
	initData();
	initPage();
})
</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayConfig"/></b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${cmc}" key="CMC.text.dhcpRelayPolicy_cable"/></span></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						<label for="relayConfig">Bundle:</label>
	                 </td>
	                 <td width="210">
						<input style="width:200px" id="relayConfigIndex" maxlength=36 class="normalInput"
                        	toolTip='1-32'  onkeyup="onBundleIdChange(this.value)" />
	                 </td>
	                 <td>
	                 	<span id=nameSpan ><fmt:message bundle="${cmc}" key="CMC.text.dhcpBundleIdSet"/></span>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<label for="name"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayPolicy"/></label>
	                 </td>
	                 <td>
						<select style="width:200px" maxlength=36 id="policySelect" class="normalSel">
				            <option value=1 selected>primary</option>
				            <option value=2>policy</option>
				            <option value=3>strict</option>
				        </select>
	                 </td>
	                 <td>
						<span id=nameSpan ><fmt:message bundle="${cmc}" key="CMC.text.dhcpGiAddrPolicySetting"/></span>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						<label for="cableSourceVerify"><fmt:message bundle="${cmc}" key="CMC.label.dhcpCableSourceVerify"/>: </label>
	                 </td>
	                 <td colspan="2">
				         <input name='cableSourceVerify' type="radio" value=1 /><span class="mR10"><fmt:message bundle="${cmc}" key="CMC.select.open"/></span>
				         <input name='cableSourceVerify' type="radio" value=2 /><fmt:message bundle="${cmc}" key="CMC.select.close"/>  
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth" id="sequenceStep">
		     <ol class="upChannelListOl pB20 pT10 noWidthCenter">
		         <li><a id="nextStep"  onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i><fmt:message bundle="${cmc}" key="CMC.button.nextStep"/></span></a></li>
		         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
		     </ol>
		</div>
		<div class="yellowTip">
			<fmt:message bundle="${cmc}" key="CMC.text.dhcpRelayPolicyDesc"/>
		</div>
	</div>
		
	
	
	
	

</BODY>
</HTML>