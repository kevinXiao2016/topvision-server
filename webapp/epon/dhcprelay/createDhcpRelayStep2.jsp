<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="epon"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
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
    padding-left : 380;position:absolute;top:290px;
}
</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _RELAY_MODE_ = {snooping: 0, l2: 1, l3: 2};
var _MODIFY_TYPE_ = {add: 4, modify: 1, del: 6, keep: 2 };
var policy;
var cmcDhcpBaseConfig;
/**
 * 已经设置了的GiAddr
 */
var giAdrList = [];
var deviceData = [];
/**
 * 上次修改之后的GiAddr,如果未修改过则为从数据库获取数据
 */
var giAddrListOld;
var intIpList = null;
var entityId = window.parent.entityId
/**********************************************************************************
 * 初始化方法
 **********************************************************************************/
function initData(){
	if(window.parent.dhcpRelay.giAddrList){
		giAdrList = window.parent.dhcpRelay.giAddrList;
	}	
	if(window.parent.dhcpRelay.giAddrListOld){
		giAddrListOld = window.parent.dhcpRelay.giAddrListOld;
	}	
	if(window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy){
		policy = window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
		var ip = window.parent.dhcpRelay.bundle.virtualPrimaryIpAddr;
		var mask = window.parent.dhcpRelay.bundle.virtualPrimaryIpMask;
		var deviceType = "CM";	
	    if(policy != _POLICY_.strict){
	    	if(policy == _POLICY_.primary){
	    		deviceType = "ALL"
	        }
	    	if(typeof ip =="string" && ip !="0.0.0.0" && checkedIpValue(ip)){
	    		var primaryGiAddr = {};
	            primaryGiAddr.topCcmtsDhcpGiAddress = ip;
	            primaryGiAddr.topCcmtsDhcpGiAddrMask = mask;
	            primaryGiAddr.deviceTypeStr = deviceType;
	            giAdrList.unshift(primaryGiAddr);
	    	}	    	
	    }
	}	
	if(window.parent.dhcpRelay.deviceTypes){
		deviceData = window.parent.dhcpRelay.deviceTypes;
	}
	//intIpList = window.parent.intIpList;	
}

function initPage(){  
	if(window.parent.action == 2){
		//$("#sequenceStep").css("padding-left", 300);
		$("#saveClick").css("display", "inline");
		$("#saveClick-label").css("display", "inline");
	}
}
/**
 * 改变页面时准备数据
 */
function changePagePrepare(){
	window.parent.dhcpRelay.giAddrList = giAdrList;
    window.parent.dhcpRelay.deviceTypes = deviceData;
}
//初始化自定义设备类型
function initDeviceSpan(){
    var p = $("#deviceSpanDiv");
    p.empty();
    // 加入默认设备类型
    var defaultDevices = ["CM", "HOST", "MTA", "STB"];
    for(var i = 0; i < defaultDevices.length; i++){
    	 p.append("<span style='margin-bottom:10px;width:140px;padding:2px; border:1px solid #ccc; margin-right:4px; margin-bottom:4px;'><span " 
                 + "style='' class=spanClass"
                 + "id='span" + defaultDevices[i] + "'>" + defaultDevices[i] + "</span>" 
                 + "</span>");
    }
    for(var a=0; a<deviceData.length; a++){
        p.append("<span style='margin-bottom:10px;width:140px;padding:2px; border:1px solid #ccc; margin-right:4px;  margin-bottom:4px;'><span " 
                + "style='' class=spanClass"
                + "id='span" + deviceData[a] + "'>" + deviceData[a] + "</span>" 
                + "<img src='/images/silk/cross.png' "
                + "style='margin-left:5px;cursor:hand;' onclick='deleteDeviceSpan(this)' /></span>");
    }
}
//初始化giaddr
function initGiaddrSpan(){
	var p = $("#giaddrSpanDiv");
    p.empty();
    for(var a=0; a<giAdrList.length; a++){
        p.append("<span style='margin-bottom:10px;width:140px;padding:2px;'><span " 
                + "style='border-bottom:1px solid white;' class=spanClass"
                + "id='span" + giAdrList[a].deviceTypeStr + "'>" + giAdrList[a].deviceTypeStr + "</span>" 
                + " (<font style='cursor:hand; color:blue;' onclick='modifyGiaddr(this)'>" + giAdrList[a].topCcmtsDhcpGiAddress + "</font>) "
                + "<img src='/images/silk/cross.png' "
                + "style='margin-left:5px;cursor:hand;' onclick='deleteGiaddrSpan(this)' /></span>");
    }
}
function remove(array, dx)
{
    if(isNaN(dx)||dx>array.length){return false;}
    for(var i=0,n=0;i<array.length;i++)
    {
        if(array[i]!=array[dx])
        {
            array[n++]=array[i]
        }
    }
    array.pop();
    return array;
} 
//删除自定义设备类型
function deleteDeviceSpan(el){
	var i ;
	var device = $(el).prev().text();
    for(i=0; i<deviceData.length; i++){
        if(deviceData[i] == device){            
            break;
        }
    }
    deviceData = remove(deviceData, i);
    //删除 Option 60
    if(window.parent.dhcpRelay.option60List){
    	var devicePoint = [];
    	for(i = 0; i < window.parent.dhcpRelay.option60List.length; i++){
            if(window.parent.dhcpRelay.option60List[i].deviceTypeStr == device){
            	devicePoint.push(i);
            }
        }
    	if(devicePoint.length > 0){
    		for(i = devicePoint.length-1; i >=0; i--){
    			window.parent.dhcpRelay.option60List = remove(window.parent.dhcpRelay.option60List, devicePoint[i]);
    		}
    	}
    }
    //删除DHCP Server
    if(window.parent.dhcpRelay.serverList){
    	var serverPoint = [];
    	for(i = 0; i < window.parent.dhcpRelay.serverList.length; i++){
    		if(window.parent.dhcpRelay.serverList[i].deviceTypeStr == device){
    			serverPoint.push(i);
    		}
    	}
    	if(serverPoint.length > 0){
    		for(i = serverPoint.length-1; i >=0; i--){
                window.parent.dhcpRelay.serverList = remove(window.parent.dhcpRelay.serverList, serverPoint[i]);
            }
    	}
    }
    //删除 Giaddr
    if(giAdrList){
    	for(i = 0; i < giAdrList.length; i++){
            if(device == giAdrList[i].deviceTypeStr){
            	break;
            }
        }
    	if(i < giAdrList.length){
    		giAdrList = remove(giAdrList, i);
            initGiaddrSpan();
    	}        
    }        
    $($(el).parent()).remove();
}
//删除Giaddr
function deleteGiaddrSpan(el){
	var i;
	var test = [1,2,3];
    for(i=0; i<giAdrList.length; i++){    	
        if(giAdrList[i].topCcmtsDhcpGiAddress == $(el).prev().text()){        	
            break;
        }
    }
    if(i < giAdrList.length){
    	giAdrList = remove(giAdrList, i);
    } 
    $($(el).parent()).remove();
}
/********************************************************************************
 * 按钮事件动作
 ********************************************************************************/
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function preClick(){
    changePagePrepare();
    window.location.assign('/epon/dhcprelay/showModifyDhcpRelayConfig.tv?entityId=' + window.parent.entityId + 
    		"&action=" + window.parent.action);
}

function nextClick(){
	changePagePrepare();
	window.location.assign('/epon/dhcprelay/createDhcpRelayStep3.jsp');
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
function onSaveClick(){
	changePagePrepare();
    var option60s = window.parent.dhcpRelay.option60List;
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
                  window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifySuccess);
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
//显示 新增设备类型页面
function showDeviceDetail(){
	window.top.createDialog('deviceDetail', I18N.DHCPRELAY.addExtDevice, 320, 120, '/epon/dhcprelay/dhcpDeviceNameDetail.jsp?' ,
			null, true, true);
}
//修改Giaddr
function modifyGiaddr(el){
	showGiaddrDetail(2, $(el).text());
}
//显示Giaddr详细配置页面,action 1:add, 2:modify
function showGiaddrDetail(action, giaddr){	
	var giaddrListContainer = 0;
    if(policy == _POLICY_.primary){
    	giaddrListContainer = 1;
    }else if(policy == _POLICY_.policy){
    	giaddrListContainer = 5;
    }else if(policy == _POLICY_.strict){
    	giaddrListContainer = 4;
    	if(deviceData){
    		giaddrListContainer += deviceData.length;
    	}
    }
    if(action == 1 && giAdrList && giAdrList.length >= giaddrListContainer ){
    	return ;
    }
    giaddr = action==1 ? "": giaddr;
    var gimask = "";
    var deviceType = ""
    if(action == 2){
        for(var i = 0; i < giAdrList.length; i++){
            if(giaddr == giAdrList[i].topCcmtsDhcpGiAddress){
            	gimask = giAdrList[i].topCcmtsDhcpGiAddrMask;
            	deviceType = giAdrList[i].deviceTypeStr;
            }
        }
    }
    window.top.createDialog('giaddrDetail', I18N.DHCPRELAY.dhcpGiaddr, 320, 240, '/epon/dhcprelay/dhcpGiaddrDetail.jsp?policy=' 
    		+ policy + "&giaddr=" + giaddr + "&gimask=" + gimask + "&deviceType=" + deviceType + "&action=" +action 
    		, null, true, true);
}
/**
 * 页面渲染
 */
Ext.onReady(function (){	
	initData();
	initPage();
	initDeviceSpan();
	initGiaddrSpan();
});
</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpGiaddr"/></b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${epon}" key="DHCPRELAY.giaddrStepTip"/></span></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="100">
						<fmt:message bundle="${epon}" key="DHCPRELAY.entitytype"/>:
	                 </td>
	                 <td>
	                 	<a id="addDevice-button"  onclick="showDeviceDetail()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i><fmt:message bundle="${epon}" key="DHCPRELAY.addExtDevice"/></span></a>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
			
	                 </td>
	                 <td>
						<div id="deviceSpanDiv"></div> 
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
	                 	<fmt:message bundle="${epon}" key="DHCPRELAY.dhcpGiaddr"/>:
	                 </td>
	                 <td>
	                 	<a id="addGiaddr-button"  onclick="showGiaddrDetail(1)" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i><fmt:message bundle="${epon}" key="DHCPRELAY.addGiaddr"/></span></a>
	                 </td>
	             </tr>
	              <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
	
	                 </td>
	                 <td>
						<div id="giaddrSpanDiv"></div> 
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id="prevBt" onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.back"/></span></a></li>
		         <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.nextStep"/></span></a></li>
		         <li><a id="saveClick" style="display: none;" onclick="onSaveClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.finish"/></span></a></li>
		         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></span></a></li>
		     </ol>
		
		</div>
		
	</div>
		



<div class=formtip id=tips style="display: none"></div>

</BODY>
</HTML>