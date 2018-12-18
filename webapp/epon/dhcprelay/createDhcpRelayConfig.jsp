<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<fmt:setLocale value="<%=uc.getUser().getLanguage()%>"/>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="epon"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}

</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript">
var entityId = ${entityId};
var action = ${action};
var dhcpRelayConfig = null;
var bundleEndList = ${bundleInterfaceListEnd};
var deviceTypes = null;
if(!window.parent.dhcpRelay){
	dhcpRelayConfig = ${dhcpRelayBundleFullConfig};
	deviceTypes = ${deviceTypes};
}

/***************************************************************************
 *
 * 初始化
 * 
 ***************************************************************************/
function initData(){
	//将获取到的数据放到window.parent中，供下个页面使用	
    if(!window.parent.dhcpRelay){
        window.parent.dhcpRelay = {};
        if(!window.parent.dhcpRelay.bundle){
            window.parent.dhcpRelay.bundle= dhcpRelayConfig.dhcpBundle;
        }
        if(!window.parent.dhcpRelay.giAddrList){
            window.parent.dhcpRelay.giAddrList = dhcpRelayConfig.dhcpGiAddr;
            window.parent.dhcpRelay.giAddrListOld = dhcpRelayConfig.dhcpGiAddr;
        }
        if(!window.parent.dhcpRelay.serverList){
            window.parent.dhcpRelay.serverList = dhcpRelayConfig.dhcpServer;
        }
        if(!window.parent.dhcpRelay.option60List){
            window.parent.dhcpRelay.option60List = dhcpRelayConfig.dhcpOption60;
        }
        window.parent.dhcpRelay.deviceTypes = deviceTypes;      
    } 
    window.parent.entityId = entityId;
    window.parent.action = action;
}

function initPage(){	
	if(action == 2){
        //$("#sequenceStep").css("padding-left", 380);
        $("#saveClick").css("display", "inline");
        $("#saveClick-label").css("display", "inline");
    }
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
        $(":radio[name='bundleVlan']").each(function (){
        	if(this.value == window.parent.dhcpRelay.bundle.vlanMapSwitch){
        		this.checked = true;
        	}
        });
        if(window.parent.dhcpRelay.bundle.vlanMapStr){
        	$("#vlanMap-input").val(window.parent.dhcpRelay.bundle.vlanMapStr);
        }
    }else{
        $(":radio[name='cableSourceVerify'][value=1]").attr("checked",true);
        $("#relayConfigIndex").val(transferBundleToNumber(initBundleIndexInputValue(bundleEndList)));
    }   
    bundleVlanChange();
    
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
	if(typeof val != "string"){
		return 1;
	}
    var bundleArray = val.split(".");
    var number;
    if(bundleArray.length > 1){
        number = parseInt(bundleArray[0]) + parseInt(bundleArray[1]);
    }else{
        number = parseInt(bundleArray[0]);
    }
    return number;
}
function transferNumberToBundle(number){
    var bundle = "1";
    if(number > 1){
        bundle += "." + (number-1);
    }
    return bundle;
}
function checkOption(deviceTypes, options){
    for(var i = 0; i < deviceTypes.length; i++){
        var j = 0;
        for(; j < options.length; j++){
            if(deviceTypes[i] == options[j].deviceTypeStr){
                break;
            }           
        }
        if(j >= options.length){
            return false;
        }
    }
    return true;
}
/*****************************************************************************************
 * 按钮事件方法
 *****************************************************************************************/
function nextClick(){
	changePagePrepare();
    window.location.assign("/epon/dhcprelay/createDhcpRelayStep2.jsp");
}

function changePagePrepare(){
	if(!window.parent.dhcpRelay){
        window.parent.dhcpRelay = {};       
    }    
    if(!window.parent.dhcpRelay.bundle){
        window.parent.dhcpRelay.bundle={};
    }
    window.parent.dhcpRelay.bundle.bundleInterface = transferNumberToBundle($("#relayConfigIndex").val());
    window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy = $("#policySelect").val();
    window.parent.dhcpRelay.bundle.cableSourceVerify = $(":radio[checked=true]").val();
    window.parent.dhcpRelay.bundle.vlanMapSwitch = $(":radio[name='bundleVlan'][checked=true]").val();
    window.parent.dhcpRelay.bundle.vlanMapStr = $("#vlanMap-input").val();    
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function onSaveClick(){
    changePagePrepare();
    var option60s = window.parent.dhcpRelay.option60List;
    var deviceData = window.parent.dhcpRelay.deviceTypes;
    if(!checkOption(deviceData, option60s)){
        return ;
    }
    var dataStr = "";
    dataStr += "dhcpRelayBundle.bundleInterface=" + window.parent.dhcpRelay.bundle.bundleInterface;
    dataStr += "&dhcpRelayBundle.topCcmtsDhcpBundlePolicy=" + window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
    dataStr += "&dhcpRelayBundle.cableSourceVerify=" + window.parent.dhcpRelay.bundle.cableSourceVerify;
    var vlanMap;
    if(window.parent.dhcpRelay.bundle.vlanMapSwitch == 1){
        vlanMap = window.parent.dhcpRelay.bundle.vlanMapStr;
    }else{
        vlanMap="1-4094";
    }
    dataStr += "&dhcpRelayVlanMap.vlanMapStr=" + vlanMap;
    var giaddrList = window.parent.dhcpRelay.giAddrList;
    if(giaddrList && giaddrList.length>0){
        for(var i = 0; i < giaddrList.length; i++){
            dataStr +=String.format("&dhcpGiaddrConfigs[{0}].deviceTypeStr={1}", i,giaddrList[i].deviceTypeStr);
            dataStr +=String.format("&dhcpGiaddrConfigs[{0}].topCcmtsDhcpGiAddress={1}", i,giaddrList[i].topCcmtsDhcpGiAddress);
            dataStr +=String.format("&dhcpGiaddrConfigs[{0}].topCcmtsDhcpGiAddrMask={1}", i,giaddrList[i].topCcmtsDhcpGiAddrMask);
        }
    }
    var dhcpRelayServer = window.parent.dhcpRelay.serverList;
    if(dhcpRelayServer && dhcpRelayServer.length > 0){
        for(var i = 0; i < dhcpRelayServer.length; i++){
            dataStr +=String.format("&dhcpServerConfigs[{0}].deviceTypeStr={1}", i,dhcpRelayServer[i].deviceTypeStr);
            dataStr +=String.format("&dhcpServerConfigs[{0}].topCcmtsDhcpHelperIpAddr={1}", i,dhcpRelayServer[i].topCcmtsDhcpHelperIpAddr);
        }
    }
    if(option60s && option60s.length > 0){
        for(var i = 0; i < option60s.length; i++){
            dataStr +=String.format("&dhcpOption60Configs[{0}].deviceTypeStr={1}", i,option60s[i].deviceTypeStr);
            dataStr +=String.format("&dhcpOption60Configs[{0}].topCcmtsDhcpOption60Str={1}", i,option60s[i].topCcmtsDhcpOption60Str);
        }
    }
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.DHCPRELAY.configuring, 'ext-mb-waiting');   
    $.ajax({
        url: '/epon/dhcprelay/modifyDhcpRelayBundleConfig.tv?entityId='+entityId + "&bundleInterfaceEnd=" + window.parent.dhcpRelay.bundle.bundleInterface,
        type: 'post',
        data: dataStr,
        dataType:'json',
        success: function(response) {
              if(response.message == "success"){   
            	  top.closeWaitingDlg();
              	  top.nm3kRightClickTips({
      				title: I18N.RECYLE.tip,
      				html: I18N.DHCPRELAY.modifySuccess
      			  });
                  //window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifySuccess);
                  setTimeout(function (){
                      window.top.closeWaitingDlg(I18N.RECYLE.tip);                      
                      window.parent.getFrame("entity-" + entityId).onRefresh();
                      cancelClick();
                  }, 500);                  
               }else{
                   window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifyFail);
               }
          }, error: function(response) {
              window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifyFail);
          }, cache: false
      });
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
function bundleVlanChange(){
	var bundleVlanEnable = $(":radio[name='bundleVlan'][checked=true]").val();
    $("#vlanMap-input").attr    ("disabled", !(bundleVlanEnable == 1));
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
	    	<p><b><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpRelayConfig"/></b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${epon}" key="DHCPRELAY.setRelayPolicyVlanTip"/></span></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						<label for="relayConfig">Bundle ID:</label>
	                 </td>
	                 <td width="220">
						<input style="width:200px" id="relayConfigIndex" maxlength=36 class="normalInput" toolTip="1-32"
                        onkeyup="onBundleIdChange(this.value)"/>
	                 </td>
	                 <td>
	                 	<span id=nameSpan style="padding-left:10px;"><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpBundleIdSet"/></span>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle="${epon}" key="DHCPRELAY.dhcpRelayPolicy"/>
	                 </td>
	                 <td>
						<select style="width:200px" maxlength=36 id="policySelect" class="normalSel">
				            <option value=1 selected>primary</option>
				            <option value=2>policy</option>
				            <option value=3>strict</option>
				        </select>
	                 </td>
	                 <td>
	                 	<span id=nameSpan style="padding-left:10px;"><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpGiAddrPolicySetting"/></span>
	                 </td>
	             </tr>
	              <tr>
	                 <td class="rightBlueTxt">
	                 	<label for="cableSourceVerify"><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpCableSourceVerify"/>: </label>
	                 </td>
	                 <td colspan="2">
	                 	<span style="width:205px">
				            <input name='cableSourceVerify' type="radio" value=1 /><fmt:message bundle="${epon}" key="DHCPRELAY.open"/>&nbsp;
				            <input name='cableSourceVerify' type="radio" value=2 /><fmt:message bundle="${epon}" key="DHCPRELAY.close"/>  
				        </span> 
				         <span id=nameSpan style="padding-left:10px;"><fmt:message bundle="${epon}" key="DHCPRELAY.switchCableSourceTip"/></span> 
	                 </td>
	             </tr>
	               <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
	                 	<label for="vlanMap"><fmt:message bundle="${epon}" key="DHCPRELAY.matchVlan"/>:</label>
	                 </td>
	                 <td colspan="2">
	                 	<span style="width:205px">
				            <input name='bundleVlan' type="radio" value=1 onclick="bundleVlanChange()" /><fmt:message bundle="${epon}" key="DHCPRELAY.enable"/>&nbsp;
				            <input name='bundleVlan' type="radio" value=2 onclick="bundleVlanChange()" checked /><fmt:message bundle="${epon}" key="DHCPRELAY.disable"/> 
				        </span> 
				        <span id=nameSpan style="padding-left:10px;"><fmt:message bundle="${epon}" key="DHCPRELAY.matchVlanSwitchTip"/></span>
	                 </td>
	             </tr>
	              <tr>
	                 <td class="rightBlueTxt">
	                 
	                 </td>
	                 <td>
	                 	<input style="width:200px; " id="vlanMap-input" value="1-4094" class="normalInput"
                        onfocus="inputFocused('vlanMap-input', I18N.DHCPRELAY.matchVlanInputTip, 'iptxt_focused')"
                        onblur="inputBlured(this, 'iptxt');"/> 
	                 </td>
	                 <td>
            			<span id=nameSpan style="padding-left:10px;"><fmt:message bundle="${epon}" key="DHCPRELAY.matchVlanTip"/></span>
	                 </td>
	             </tr>
	         </tbody>
	     </table>	
	     <div class="yellowTip" style="margin-top:10px;">
			<fmt:message bundle="${epon}" key="DHCPRELAY.createBundleTip"/>  
		</div>	
		<div class="noWidthCenterOuter clearBoth"  id=sequenceStep>
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		         <li><a id="nextStep" href="javascript:;" class="normalBtnBig" onclick="nextClick()"><span><fmt:message bundle="${epon}" key="DHCPRELAY.nextStep"/></span></a></li>
		         <li><a id="saveClick" href="javascript:;" class="normalBtnBig"  onclick="onSaveClick()" style="display:none"><span><fmt:message bundle="${epon}" key="DHCPRELAY.finish"/></span></a></li>
		         <li><a id="cancelBt" href="javascript:;" class="normalBtnBig"  onclick="cancelClick()"><span><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></span></a></li>
		     </ol>
		</div>
	</div>
	





</BODY>
</HTML>