<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
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
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
#sequenceStep {
    padding-left : 380;position:absolute;top:390px;
}
.display-none{
    display: none;
}
</style>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _RELAY_MODE_ = {snooping: 0, l2: 1, l3: 2};
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6, keep: 2 };
var existL3Mode = false;
var existL2SnoopMode = false;
var policy = {};
var giAddrStringArray = [];
var giAddrMaskArray = [];
var giAddrTypeArray = [];
var cmcDhcpBaseConfig;
/**
 * 已经设置了的GiAddr
 */
var giAdrList = [];
var packetVlan = [];
var vlan = [0,0,0,0];
var priority = [0,0,0,0];
var preClickUrl;
/**
 * 上次修改之后的GiAddr,如果未修改过则为从数据库获取数据
 */
var giAddrListOld;
var intIpList = null;
/**********************************************************************************
 * 初始化方法
 **********************************************************************************/
function initData(){
	giAdrList = window.parent.dhcpRelay.giAddrList;
	giAddrListOld = window.parent.dhcpRelay.giAddrListOld;
	if(giAdrList == null){
	   giAdrList = [];
	}	
	policy = window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
	if(window.parent.dhcpRelay.bundle.policyOld != _POLICY_.strict){
       var primaryGiAddr = {};
       primaryGiAddr.topCcmtsDhcpGiAddress = window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr;
       primaryGiAddr.topCcmtsDhcpGiAddrMask = window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
       giAdrList.push(primaryGiAddr);
    }
	if(window.parent.dhcpRelay.packetVlanList){
		packetVlan = window.parent.dhcpRelay.packetVlanList;
		if(packetVlan && packetVlan.length > 0){
	        for(var i = 0; i < packetVlan.length; i++){
	            var packet = packetVlan[i];
	            vlan[packet.topCcmtsDhcpDeviceType-1] = packet.topCcmtsDhcpTagVlan;
	            priority[packet.topCcmtsDhcpDeviceType-1] = packet.topCcmtsDhcpTagPriority;
	        }
	    }
	}	
	intIpList = window.parent.intIpList;	
	cmcDhcpBaseConfig = window.parent.dhcpBaseConfig;
	existL3Mode = cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3 || cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3 ||
	cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3 || cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3;
	existL2SnoopMode = cmcDhcpBaseConfig.cmMode != _RELAY_MODE_.l3 || cmcDhcpBaseConfig.hostMode != _RELAY_MODE_.l3 ||
    cmcDhcpBaseConfig.mtaMode != _RELAY_MODE_.l3 || cmcDhcpBaseConfig.stbMode != _RELAY_MODE_.l3;
	if(window.parent.dhcpRelay.action == 1){
		preClickUrl = '/cmc/dhcprelay/showCreateDhcpRelayConfig.tv?entityId='+ window.parent.entityId + '&cmcId=' + window.parent.dhcpRelay.cmcId;
    }else {
    	preClickUrl = '/cmc/dhcprelay/showModifyDhcpRelayConfig.tv?entityId='+ window.parent.entityId +
        '&bundleInterface='+ window.parent.dhcpRelay.bundleInterface + '&cmcId=' + window.parent.dhcpRelay.cmcId;
    }
	
}
function initIpInput(){
	if(!existL3Mode){
		return ;
	}
    var primaryGiAddr = new ipV4Input("primaryGiAddr","span_primaryGiAddr");
    primaryGiAddr.width(141);
    var primaryGiMask = new ipV4Input("primaryGiMask","span_primaryGiMask");
    primaryGiMask.width(141);
    var cmGiaddr = new ipV4Input("cmGiaddr","span_cmGiaddr");
    cmGiaddr.width(141);
    var cmGimask = new ipV4Input("cmGimask","span_cmGimask");
    cmGimask.width(141);
    var strictCmGiaddr = new ipV4Input("strictCmGiaddr","span_strictCmGiaddr");
    strictCmGiaddr.width(141);
    var strictCmMask = new ipV4Input("strictCmMask","span_strictCmMask");
    strictCmMask.width(141);
    var hostGiaddr = new ipV4Input("hostGiaddr","span_hostGiaddr");
    hostGiaddr.width(141);
    var hostGiMask = new ipV4Input("hostGiMask","span_hostMask");
    hostGiMask.width(141);
    var mtaGiaddr = new ipV4Input("mtaGiaddr","span_mtaGiaddr");
    mtaGiaddr.width(141);
    var mtaGiMask = new ipV4Input("mtaGiMask","span_mtaMask");
    mtaGiMask.width(141);
    var stbGiaddr = new ipV4Input("stbGiaddr","span_stbGiaddr");
    stbGiaddr.width(141);
    var stbGiMask = new ipV4Input("stbGiMask","span_stbMask");
    stbGiMask.width(141);
}
function showGiaddrConfig(relayPolicy){
	if(!existL3Mode){
		$("#l3Relay-fieldset").addClass("display-none");
        $("#nextStep").attr("disabled", false);
    }else{
    	if(relayPolicy == _POLICY_.primary){
            $("#primaryGiaddr").removeClass("display-none");
        }else if(relayPolicy == _POLICY_.policy){
            $("#policyGiaddr").removeClass("display-none");
        }else{
            $("#strictGiaddr").removeClass("display-none");
        }
    	if(cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3){
            $("#policyCm").removeClass("display-none");
            $("#strictCm").removeClass("display-none");
        }else{
            $("#policyCm").addClass("display-none");
            $("#strictCm").addClass("display-none");
        }
        if(cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3){
            $("#strictHost").removeClass("display-none");
        }else{
            $("#strictHost").addClass("display-none");
        }
        if(cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3){
            $("#strictMta").removeClass("display-none");
        }else{
            $("#strictMta").addClass("display-none");
        }
        if(cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3){
            $("#strictStb").removeClass("display-none");
        }else{
            $("#strictStb").addClass("display-none");
        }
    }
    if(!existL2SnoopMode){
    	$("#l2Relay-fieldset").addClass("display-none");
    }else{
    	if(cmcDhcpBaseConfig.cmMode == _RELAY_MODE_.l3){
            $("#cm_td").addClass("display-none");
        }else{
            $("#cm_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.hostMode == _RELAY_MODE_.l3){
            $("#host_td").addClass("display-none");
        }else{
            $("#host_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.mtaMode == _RELAY_MODE_.l3){
            $("#mta_td").addClass("display-none");
        }else{
            $("#mta_td").removeClass("display-none");
        }
        if(cmcDhcpBaseConfig.stbMode == _RELAY_MODE_.l3){
            $("#stb_td").addClass("display-none");
        }else{
            $("#stb_td").removeClass("display-none");
        }  
    }
    
      
}
function initPage(){    
    setIpValue("primaryGiAddr", window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr);
    setIpValue("primaryGiMask", window.parent.dhcpRelay.bundle.virtualPrimaryIpMask);
    setIpValue("cmGiaddr", window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr);
    setIpValue("cmGimask", window.parent.dhcpRelay.bundle.virtualPrimaryIpMask);
    setIpValue("strictCmGiaddr", window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr);
    setIpValue("strictCmMask", window.parent.dhcpRelay.bundle.virtualPrimaryIpMask);
    if(window.parent.dhcpRelay.giAddrListOld){
        for(var i = 0; i < window.parent.dhcpRelay.giAddrListOld.length; i++){
            var giaddr = window.parent.dhcpRelay.giAddrListOld[i];
            if(giaddr && giaddr.topCcmtsDhcpGiAddrDeviceType){
                switch(giaddr.topCcmtsDhcpGiAddrDeviceType){
                case 2:
                    setIpValue("hostGiaddr", giaddr.topCcmtsDhcpGiAddress);
                    setIpValue("hostGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                    break;
                case 3:
                    setIpValue("mtaGiaddr", giaddr.topCcmtsDhcpGiAddress);
                    setIpValue("mtaGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                    break;
                case 4:
                    setIpValue("stbGiaddr", giaddr.topCcmtsDhcpGiAddress);
                    setIpValue("stbGiMask", giaddr.topCcmtsDhcpGiAddrMask);
                    break;
                }
            }
        }   
    }
    var i = 0;
    $("#vlan_table select").each(function (){
        this.value = priority[i];
        i++;
    });
    i = 0;
    $("#vlan_table  input").each(function (){
        if( vlan[i] != 0){
            this.value = vlan[i];
        }        
        i++;
    });
}
/**
 * 改变页面时准备数据
 */
function changePagePrepare(){
	if(policy == _POLICY_.primary){
		if(checkedIpValue(getIpValue("primaryGiAddr")) &&
				getIpValue("primaryGiAddr") != "0.0.0.0"){
			window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr = getIpValue("primaryGiAddr");
	        window.parent.dhcpRelay.bundle.virtualPrimaryIpMask = getIpValue("primaryGiMask");
		}
    }else if(policy == _POLICY_.policy){
    	if(checkedIpValue(getIpValue("cmGiaddr")) &&
                getIpValue("cmGiaddr") != "0.0.0.0"){
    		window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr = getIpValue("cmGiaddr");
            window.parent.dhcpRelay.bundle.virtualPrimaryIpMask = getIpValue("cmGimask");
        }     
        
    }else{
    	giAddrStringArray = [];
        giAddrMaskArray = [];
        giAddrTypeArray = [];
        giAdrList = [];
        //CM
        var cmGiAddr = {};
        cmGiAddr.topCcmtsDhcpGiAddrDeviceType = 2;
        cmGiAddr.topCcmtsDhcpGiAddress = getIpValue("strictCmGiaddr");
        cmGiAddr.topCcmtsDhcpGiAddrMask = getIpValue("strictCmMask");
        if(checkedIpValue(cmGiAddr.topCcmtsDhcpGiAddress) &&
                cmGiAddr.topCcmtsDhcpGiAddress != "0.0.0.0"){
            var old = {};
            old.topCcmtsDhcpGiAddress = window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr; 
            old.topCcmtsDhcpGiAddrMask = window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
            old.modifyTag = _MODIFY_TYPE_.add;
            window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr = cmGiAddr.topCcmtsDhcpGiAddress;
            window.parent.dhcpRelay.bundle.virtualPrimaryIpMask = cmGiAddr.topCcmtsDhcpGiAddrMask;
            giAddrStringArray.push(window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr);
            giAddrMaskArray.push(window.parent.dhcpRelay.bundle.virtualPrimaryIpMask);
            giAddrTypeArray.push(1);
            giAdrList.push(cmGiAddr);
            giAdrList.push(old);
        } 
        //host
        var hostGiAddr = {};
        hostGiAddr.topCcmtsDhcpGiAddrDeviceType = 2;
        hostGiAddr.topCcmtsDhcpGiAddress = getIpValue("hostGiaddr");
        hostGiAddr.topCcmtsDhcpGiAddrMask = getIpValue("hostGiMask");
        if(hostGiAddr.topCcmtsDhcpGiAddress != '' && checkedIpValue(hostGiAddr.topCcmtsDhcpGiAddress)){
        	giAddrStringArray.push(hostGiAddr.topCcmtsDhcpGiAddress);
            giAddrMaskArray.push(hostGiAddr.topCcmtsDhcpGiAddrMask);
            giAddrTypeArray.push(hostGiAddr.topCcmtsDhcpGiAddrDeviceType);
            giAdrList.push(hostGiAddr);
        }
        
        //mta
        var mtaGiAddr = {};
        mtaGiAddr.topCcmtsDhcpGiAddrDeviceType = 3;
        mtaGiAddr.topCcmtsDhcpGiAddress = getIpValue("mtaGiaddr");
        mtaGiAddr.topCcmtsDhcpGiAddrMask = getIpValue("mtaGiMask");
        if(mtaGiAddr.topCcmtsDhcpGiAddress != '' && checkedIpValue(mtaGiAddr.topCcmtsDhcpGiAddress)){
        	giAddrStringArray.push(mtaGiAddr.topCcmtsDhcpGiAddress);
            giAddrMaskArray.push(mtaGiAddr.topCcmtsDhcpGiAddrMask);
            giAddrTypeArray.push(mtaGiAddr.topCcmtsDhcpGiAddrDeviceType);
            giAdrList.push(mtaGiAddr);
        }        
        //stb
        var stbGiAddr = {};
        stbGiAddr.topCcmtsDhcpGiAddrDeviceType = 4;
        stbGiAddr.topCcmtsDhcpGiAddress = getIpValue("stbGiaddr");
        stbGiAddr.topCcmtsDhcpGiAddrMask = getIpValue("stbGiMask");
        if(stbGiAddr.topCcmtsDhcpGiAddress != '' && checkedIpValue(stbGiAddr.topCcmtsDhcpGiAddress)){
        	giAddrStringArray.push(stbGiAddr.topCcmtsDhcpGiAddress);
            giAddrMaskArray.push(stbGiAddr.topCcmtsDhcpGiAddrMask);
            giAddrTypeArray.push(stbGiAddr.topCcmtsDhcpGiAddrDeviceType);
            giAdrList.push(stbGiAddr);
        }      
        //giAdrList = [hostGiAddr, mtaGiAddr, stbGiAddr];
        var giAdrListTemp = giAdrList;
        for(var i = 0; i < giAdrListTemp.length; i++){
        	var giAddrTemp = giAdrListTemp[i];
        	if(giAddrTemp.topCcmtsDhcpGiAddress != ""){
        		giAddrTemp.modifyTag = _MODIFY_TYPE_.add;
        	}        	
        	if(giAddrListOld && giAddrListOld.length > 0){
        		for(var j = 0; j < giAddrListOld.length; j++){
        			var giAddr = giAddrListOld[j];
        			var existOld = containGiaddr(giAdrList, giAddr);
        			if(giAddr.topCcmtsDhcpGiAddrDeviceType && giAddr.topCcmtsDhcpGiAddrDeviceType == giAddrTemp.topCcmtsDhcpGiAddrDeviceType){
        				if(giAddr.topCcmtsDhcpGiAddress == "0.0.0.0" || giAddr.topCcmtsDhcpGiAddress == ""
        						|| giAddr.topCcmtsDhcpGiAddress == null || giAddr.topCcmtsDhcpGiAddrMask == "0.0.0.0" 
        						|| giAddr.topCcmtsDhcpGiAddrMask == "" || giAddr.topCcmtsDhcpGiAddrMask ==null){
        					if(!(giAddrTemp.topCcmtsDhcpGiAddress == "0.0.0.0" || giAddrTemp.topCcmtsDhcpGiAddress == ""
                                || giAddrTemp.topCcmtsDhcpGiAddress == null)){
        						giAddrTemp.modifyTag = _MODIFY_TYPE_.add;
        					}        					
        				}else{
        					if(giAddrTemp.topCcmtsDhcpGiAddress == "0.0.0.0" || giAddrTemp.topCcmtsDhcpGiAddress == ""
                                || giAddrTemp.topCcmtsDhcpGiAddress == null){
        					    if(!existOld){
        					        giAddrTemp.modifyTag = _MODIFY_TYPE_.del;
        					    }
                            }else{
                            	if(giAddrTemp.topCcmtsDhcpGiAddress != giAddr.topCcmtsDhcpGiAddress){
                            	    if(!existOld){
                            	        giAddrTemp.modifyTag = _MODIFY_TYPE_.modify;
                            	    }else{
                            	        giAddrTemp.modifyTag = _MODIFY_TYPE_.add;
                            	    }
                            	}else{
                            		giAddrTemp.modifyTag = _MODIFY_TYPE_.keep;
                            	}
                            	
                            }
        				}         				
        				giAddrTemp.topCcmtsDhcpGiAddressOld = giAddr.topCcmtsDhcpGiAddress;
        				continue;
        			}
        		}
        	}else{
        		if(!(giAddrTemp.topCcmtsDhcpGiAddress == "0.0.0.0" || giAddrTemp.topCcmtsDhcpGiAddress == ""
                    || giAddrTemp.topCcmtsDhcpGiAddress == null)){
                    giAddrTemp.modifyTag = _MODIFY_TYPE_.add;
                }   
        	}
        }
    }
	var i = 0;
    $("#vlan_table select").each(function (){
        priority[i] = this.value;
        i++;
    });
    i = 0;
    $("#vlan_table input").each(function (){
        if(this.value){
            vlan[i] = this.value;
        }else{
            vlan[i] = 0;
        }
        i++;
    });        
    if(packetVlan && packetVlan.length > 0){
    	for(var i  = 0; i < packetVlan.length; i++){
    		packetVlan[i].topCcmtsDhcpTagVlan = vlan[packetVlan[i].topCcmtsDhcpDeviceType-1];
    		packetVlan[i].topCcmtsDhcpTagPriority = priority[packetVlan[i].topCcmtsDhcpDeviceType-1];
    	}
    }else{
    	for(var i = 0; i < vlan.length; i++){
    		var packet = {};
    		packet.topCcmtsDhcpTagVlan = vlan[i];
    		packet.topCcmtsDhcpTagPriority = priority[i];
    		packet.topCcmtsDhcpDeviceType = i + 1;
    		packetVlan.push(packet);
    	}
    }
    window.parent.dhcpRelay.giAddrListTemp = giAdrListTemp;
    window.parent.dhcpRelay.giAddrListOld = giAdrList;
    //window.parent.dhcpRelay.giAddrList = giAdrList;
    window.parent.dhcpRelay.giAddrArray = giAddrStringArray;
    window.parent.dhcpRelay.giAddrMaskArray = giAddrMaskArray;
    window.parent.dhcpRelay.giAddrTypeArray = giAddrTypeArray;
    window.parent.dhcpRelay.vlan = vlan;
    window.parent.dhcpRelay.priotity = priority;
    window.parent.dhcpRelay.packetVlanList = packetVlan;
}
function containGiaddr(array, obj){
    for(var i = 0; i < array.length; i ++){
        if(array[i].topCcmtsDhcpGiAddress == obj.topCcmtsDhcpGiAddress){
            return true;
        }
    }
    return false;
}
/*******************************************************************************
 *数据校验 
 *******************************************************************************/
/**
 * 获取子网号
 *ip: IP地址,mask:掩码
 */
function getSubNetIp(ip, mask){
	var ipArray;
	var maskArray;
	var subNet = new Array(4);
	if(ip && mask){
		ipArray = ip.split(".");
		maskArray = mask.split(".");
		if(ipArray.length == 4 && maskArray.length == 4){
			for(var i = 0; i < ipArray.length; i++){
				subNet[i] = parseInt(ipArray[i]) & parseInt(maskArray[i]);
			}
		}
	}
	return subNet.join(".");
}
/**
 * 判断两个子网是否有冲突
 */
function isSameSubnet(sub1, mask1, sub2, mask2){
    if(sub1 && sub2){
        var sub1Mask1 = getSubNetIp(sub1, mask1);
        var sub2Mask1 = getSubNetIp(sub2, mask1);
        var sub1Mask2 = getSubNetIp(sub1, mask2);
        var sub2Mask2 = getSubNetIp(sub2, mask2);
        if(sub1Mask1 == sub2Mask1 || sub1Mask2 == sub2Mask2){
        	return true;
        }
    }
    return false;
}
function getIpInput(){
	var pageIpInput = [];
	var cm = {};
	var host = {};
	var mta = {};
	var stb = {};
	if(policy == _POLICY_.primary){
        cm.ip = getIpValue("primaryGiAddr");
        cm.mask = getIpValue("primaryGiMask");
    }else if(policy == _POLICY_.policy){
    	cm.ip = getIpValue("cmGiaddr");
    	cm.mask = getIpValue("cmGimask");
        
    }else{
    	cm.ip = getIpValue("strictCmGiaddr");
        cm.mask = getIpValue("strictCmMask");
        //host
        host.ip = getIpValue("hostGiaddr");
        host.mask = getIpValue("hostGiMask");
        //mta
        mta.ip = getIpValue("mtaGiaddr");
        mta.mask = getIpValue("mtaGiMask");
        //stb
        stb.ip = getIpValue("stbGiaddr");
        stb.mask = getIpValue("stbGiMask");
    }
	pageIpInput.push(cm);
	pageIpInput.push(host);
	pageIpInput.push(mta);
	pageIpInput.push(stb);
	return pageIpInput;
}
/**
 * 验证同一个数组中是否存在相同的IP子网
 */
function notExistSameIp(ipArray){
	if(ipArray && ipArray.length>0){
		for(var i=0; i < ipArray.length; i++){
			for(var j=i+1; j < ipArray.length; j++){
			    // strict策略下，可以有相同的中继地址
			    if(policy == _POLICY_.strict && ipArray[i].ip == ipArray[j].ip 
			            && ipArray[i].mask == ipArray[j].mask){
			        continue;
			    }
				if(ipArray[i].ip != "" && ipArray[j].ip != "" &&
						isSameSubnet(ipArray[i].ip, ipArray[i].mask, ipArray[j].ip, ipArray[j].mask)){
				    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.CMC.tip.dhcpSubnetConflict, ipArray[i].ip +
				            "/" + ipArray[i].mask , ipArray[j].ip +
                            "/" + ipArray[j].mask)
				            );
					return true;
				}
			}
		}
	}
	return false;
}
function isExistInGiAddr(ip, mask){
	var subnet = getSubNetIp(ip, mask);
	if(giAdrList){
		for(var i = 0; i < giAdrList.length; i++){			
			if((giAdrList[i].topCcmtsDhcpGiAddrMask != null || giAdrList[i].topCcmtsDhcpGiAddrMask != "") &&
					isSameSubnet(ip, mask, giAdrList[i].topCcmtsDhcpGiAddress, giAdrList[i].topCcmtsDhcpGiAddrMask)){
				return true;
			}
		}
	}
	return false;
}
function isExistInIntIp(ip, mask){
	var subnet = getSubNetIp(ip, mask);
    if(intIpList){
        for(var i = 0; i < intIpList.length; i++){            
            if(isSameSubnet(ip, mask, intIpList[i].topCcmtsDhcpIntIpAddr, intIpList[i].topCcmtsDhcpIntIpMask)){
                return true;
            }
        }
    }
    return false;
}
function checkSubnetConflict(){
	var ipArray = getIpInput();
	if(notExistSameIp(ipArray)){
		return true;
	}
	for(var i = 0; i< ipArray.length; i++){
		var ip = ipArray[i].ip;
		var mask = ipArray[i].mask;
		if(!isExistInGiAddr(ip, mask) && isExistInIntIp(ip, mask)){
		    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.CMC.tip.dhcpConflictWithOther, ip + "/" + mask));
			return true;
		}
	}
	return false;
}
/**
 * 验证输入的值是否为Vlan的合法值
 * 返回值 :true : 合法 ，false :非法
 *
 */
function checkVlanValue(val){
	if(val==null || val.trim()=="" || !isNaN(val) && (val>=1 && val <=4094)){
		return true;
	}else{
		return false;
	}
}
/**
 * 验证VLAN 输入框，如果 输入的值不合法则设置下一步按钮 为disabled
 */
function onVlanInputCheck(){
	var disabled = false;
	$("#vlan_table input").each(function (){
		if(!checkVlanValue(this.value)){
			disabled = true;
		}
	});
	$("#nextStep").attr("disabled", disabled);
	$("#nextStep").mouseout();
}
/**
 * 验证输入的 主IP地址（CM的中继/中继地址）格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onPrimaryIpCheck(){
	var valid = false;
	var ip;
	var mask;
	var ipCheck;
	if(!existL3Mode){
	    return true;
	}
	if(policy == _POLICY_.primary){
		ip = getIpValue("primaryGiAddr");
		mask = getIpValue("primaryGiMask");
		ipCheck = new IpAddrCheck(ip, mask);
		if(ipIsFilled("primaryGiAddr") && ipIsFilled("primaryGiMask") && 
		        checkedIpMask(getIpValue("primaryGiMask")) && ipCheck.isHostIp()){
			valid =  true;
	    }
    }else if(policy == _POLICY_.policy){
    	ip = getIpValue("cmGiaddr");
        mask = getIpValue("cmGimask");
        ipCheck = new IpAddrCheck(ip, mask);
    	if(ipIsFilled("cmGiaddr") && ipIsFilled("cmGimask") && checkedIpMask(getIpValue("cmGimask"))
    			&& ipCheck.isHostIp()){
    		valid =  true;
        }
    }else{
    	ip = getIpValue("strictCmGiaddr");
        mask = getIpValue("strictCmMask");
        ipCheck = new IpAddrCheck(ip, mask);
    	if(ipIsFilled("strictCmGiaddr") && ipIsFilled("strictCmMask") && checkedIpMask(getIpValue("strictCmMask"))
    			&& ipCheck.isHostIp()){
    		valid =  true;
        }
    }
	if(!valid){
	    window.parent.showMessageDlg(I18N.COMMON.tip, ip + "/" + mask + I18N.route.ipFormatError);
	}
	return valid;
}
/**
 * 验证输入的 HOST的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onHostIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
    	ip = getIpValue("hostGiaddr");
        mask = getIpValue("hostGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("hostGiaddr") == "" && getIpValue("hostGiMask") == "") || 
        		(ipIsFilled("hostGiaddr") && ipIsFilled("hostGiMask") && checkedIpMask(getIpValue("hostGiMask")) 
        				&& ipCheck.isHostIp())){
        	valid =  true;
        }
    }else{
        valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg(I18N.COMMON.tip, ip + "/" + mask + I18N.route.ipFormatError);
    }
    return valid;
}
/**
 * 验证输入的 MTA的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onMtaIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
    	ip = getIpValue("mtaGiaddr");
        mask = getIpValue("mtaGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("mtaGiaddr") == "" && getIpValue("mtaGiMask") == "") || 
                (ipIsFilled("mtaGiaddr") && ipIsFilled("mtaGiMask") && checkedIpMask(getIpValue("mtaGiMask"))
                		&& ipCheck.isHostIp())){
        	valid =  true;
        }
    }else{
    	valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg(I18N.COMMON.tip, ip + "/" + mask + I18N.route.ipFormatError);
    }
    return valid;
}
/**
 * 验证输入的 STB的中继地址格式是否正确
 * 返回值 : true:格式正确，false:格式错误
 */
function onStbIpCheck(){
    var valid = false;
    var ip;
    var mask;
    var ipCheck;
    if(policy == _POLICY_.strict){
    	ip = getIpValue("stbGiaddr");
        mask = getIpValue("stbGiMask");
        ipCheck = new IpAddrCheck(ip, mask);
        if((getIpValue("stbGiaddr") == "" && getIpValue("stbGiMask") == "") || 
                (ipIsFilled("stbGiaddr") && ipIsFilled("stbGiMask") && checkedIpMask(getIpValue("stbGiMask"))
                		&& ipCheck.isHostIp())){
        	valid =  true;
        }
    }else{
        valid =  true;
    }
    if(!valid){
        window.parent.showMessageDlg(I18N.COMMON.tip, ip + "/" + mask + I18N.route.ipFormatError);
    }
    return valid;
}

function onIpAndMaskInputCheck(){
	if(!onPrimaryIpCheck()){
	    return false;
	}
	if(!onHostIpCheck()){
	    return false;
	}
	if(!onMtaIpCheck()){
	    return false;
    }
	if(!onStbIpCheck()){
	    return false;
    }
	if(checkSubnetConflict()){
	    return false;
	}
	return true;
}
/********************************************************************************
 * 按钮事件动作
 ********************************************************************************/
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function preClick(){
    if(existL3Mode && !onIpAndMaskInputCheck()){
        return;
    }
    changePagePrepare();
    window.location.assign(preClickUrl);
}

function nextClick(){
    if(existL3Mode && !onIpAndMaskInputCheck()){
        return;
    }
	changePagePrepare();
	window.location.assign('/cmc/createDhcpRelayStep5.jsp');
}
/**
 * 页面渲染
 */
Ext.onReady(function (){	
	initData();
    initIpInput();
    initPage();
    showGiaddrConfig(policy);
});


</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayConfig"/></b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${cmc}" key="CMC.text.dhcpRelayL2L3Config"/></span></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>	

    <div class=formtip id=tips style="display: none"></div>
    <div class="edge10 pT20">
        <div class="zebraTableCaption" id="l3Relay-fieldset">
            <div class="zebraTableCaptionTitle"><span><fmt:message bundle="${cmc}" key="CMC.text.dhcpL3RelayConfig"/></span></div>
            <div id="primaryGiaddr" class="display-none">
                <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	                <tbody>
	                    <tr>
	                        <td class="rightBlueTxt" width="160">
	                            <fmt:message bundle="${cmc}" key="CMC.label.dhcpGiaddr"/>:
	                        </td>
	                        <td width="141">
	                            <span id="span_primaryGiAddr" ></span>
	                        </td>
	                        <td class="rightBlueTxt" width="50">
	                            <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
	                        </td>
	                        <td>
	                           <span id="span_primaryGiMask" ></span>
	                        </td>
	                </tbody>
	            </table>
            </div>
            <div id="policyGiaddr" class="display-none">
	            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	               <tbody>
			        <tr id="policyCm">
			            <td class="rightBlueTxt" width="160">CM:</td>
			            <td width="141">
			               <span id="span_cmGiaddr" ></span>
			            </td>
			            <td class="rightBlueTxt" width="50">
			               <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
			            </td>
			            <td>
			               <span id="span_cmGimask" ></span>
			            </td>
			        </tr>
			        </tbody>
			    </table>  
            </div>
            <div id="strictGiaddr" class="display-none">
	            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	                <tbody>   
			        <tr id="strictCm">
			            <td class="rightBlueTxt" width="160">CM:</td>
			            <td>
			               <span id="span_strictCmGiaddr" ></span>
			            </td>
			            <td class="rightBlueTxt" width="50">
			               <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
			            </td>
			            <td>
			               <span id="span_strictCmMask" ></span>
			            </td>
			        </tr> 
			        <tr  id="strictHost">
			            <td class="rightBlueTxt" width="160">HOST:</td>
			            <td>
			               <span id="span_hostGiaddr" ></span>
			            </td>
			            <td class="rightBlueTxt" width="50">
			               <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
			            </td>
			            <td>
			               <span id="span_hostMask" ></span>
			            </td>
			        </tr> 
			        <tr id="strictMta">
			            <td class="rightBlueTxt" width="160">MTA:</td>
			            <td>
			               <span id="span_mtaGiaddr"></span>
			            </td>
			            <td class="rightBlueTxt" width="50">
			               <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
			            </td>
			            <td>
			               <span id="span_mtaMask" ></span>
			            </td>
			        </tr>   
			        <tr  id="strictStb">
			            <td class="rightBlueTxt" width="160">STB:</td>
			            <td>
			               <span id="span_stbGiaddr"></span>
			            </td>
			            <td class="rightBlueTxt" width="50">
			               <fmt:message bundle="${cmc}" key="CMC.label.iPMask"/>:
			            </td>
			            <td>
			               <span id="span_stbMask"></span>
			            </td>
			        </tr>  
			        </tbody>     
			    </table>
            </div>
        </div>
        <div class="zebraTableCaption" id="l2Relay-fieldset">
            <div class="zebraTableCaptionTitle"><span><fmt:message bundle="${cmc}" key="CMC.text.dhcpL2RelayConfig"/></span></div>
            <table id="vlan_table" class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            <tbody>
			    <tr id="cm_td" class="display-none">
			        <td class="rightBlueTxt" width="160">CM:</td>
			        <td width="141"><input style="width:141px" id=cmVlanTag maxlength=36 class="normalInput" toolTip="1-4094" />
			        </td>
			        <td class="rightBlueTxt" width="50">
			            <fmt:message bundle="${cmc}" key="CMC.label.vlanPriority"/>:
			        </td>
			        <td>
			        <select style="width:141px" id="cmPriority">
			            <option value=0 >0</option>
			            <option value=1>1</option>
			            <option value=2>2</option>
			            <option value=3>3</option>
			            <option value=4>4</option>
			            <option value=5>5</option>
			            <option value=6>6</option>
			            <option value=7>7</option>
			        </select>
			        </td>
			    </tr>
			    <tr id="host_td" class="display-none" >
			         <td class="rightBlueTxt" width="160">HOST:</td>
			         <td width="141"><input style="width:141px" id=hostVlanTag maxlength=36 
			            class="normalInput" toolTip="1-4094"/>
			         </td>
			         <td class="rightBlueTxt" width="50">
			            <fmt:message bundle="${cmc}" key="CMC.label.vlanPriority"/>:
			         </td>
			         <td>
			         <select style="width:141px" id="hostPriority">
			             <option value=0>0</option>
			             <option value=1>1</option>
			             <option value=2>2</option>
			             <option value=3>3</option>
			             <option value=4>4</option>
			             <option value=5>5</option>
			             <option value=6>6</option>
			             <option value=7>7</option>
			         </select>
			         </td>
			      </tr> 
			    <tr id="mta_td" class="display-none">
			        <td class="rightBlueTxt" width="160">MTA:</td>
			        <td width="141"><input style="width:141px" id=mtaVlanTag maxlength=36 
			            class="normalInput" toolTip="1-4094"/>
			        </td>
			        <td class="rightBlueTxt" width="50">
			            <fmt:message bundle="${cmc}" key="CMC.label.vlanPriority"/>:
			        </td>
			        <td>
			        <select style="width:141px" id="mtaPriority">
			            <option value=0>0</option>
			            <option value=1>1</option>
			            <option value=2>2</option>
			            <option value=3>3</option>
			            <option value=4>4</option>
			            <option value=5>5</option>
			            <option value=6>6</option>
			            <option value=7>7</option>
			        </select>
			        </td>
			     </tr>
			     <tr id="stb_td" class="display-none">
			         <td class="rightBlueTxt" width="160">STB:</td>
			         <td width="141"><input style="width:141px" id=stbVlanTag maxlength=36
			            class="normalInput" toolTip="1-4094"/>
			         </td>
			         <td class="rightBlueTxt" width="50">
			            <fmt:message bundle="${cmc}" key="CMC.label.vlanPriority"/>:
			         </td>
			         <td>
			         <select style="width:141px" id="stbPriority">
			             <option value=0>0</option>
			             <option value=1>1</option>
			             <option value=2>2</option>
			             <option value=3>3</option>
			             <option value=4>4</option>
			             <option value=5>5</option>
			             <option value=6>6</option>
			             <option value=7>7</option>
			         </select>
			         </td>
			    </tr> 
			</tbody>      
			</table>
        </div>
    </div>

    <div class="noWidthCenterOuter clearBoth" id=sequenceStep>
         <ol class="upChannelListOl pB0 pT10 noWidthCenter">
             <li><a  id=prevBt onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i><fmt:message bundle="${cmc}" key="CMC.button.preStep"/></span></a></li>
             <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i><fmt:message bundle="${cmc}" key="CMC.button.nextStep"/></span></a></li>
             <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
         </ol>
    </div>
</body>
</html>