<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<TITLE>DhcpServer Configuration</TITLE>
<%@include file="../include/cssStyle.inc"%>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/zetaframework/IpTextField.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript">
var bundleInterface = "${bundleInterface}";
var bundleInterfaceEnd;
var deviceType = ${deviceType};
var entityId = ${entityId};
var parentStore = null;
var vlanList = ${vlanList};
/************************************************************************************************************
 * 数据校验
 ************************************************************************************************************/
function ipInputValid(ip){
    if(!ipIsFilled("ip")){
        return false;
    }
    var ipCheck = new IpAddrCheck(ip);    
    return ipCheck.isHostIp();
}
//检查是否是CCMTS上存在的IP地址, 是：true， 否：false
function checkExistInDevice(val,vlanList){
    for(v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
        if(ipTmp==undefined||ipTmp==""||ipTmp=="-"||maskTmp==undefined||maskTmp==""||maskTmp=="-"){
            continue;
        }
        if(val == ipTmp){
            return true;
        }
    }
    return false;
}
function saveClick(){
    var dhcpServerIp = getIpValue("ip");
    deviceType = $("#deviceTypeSelect").val();
    if(checkExistInDevice(dhcpServerIp, vlanList)){
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.dhcpServerIsDeviceIp);
        return false;
    }
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
    $.ajax({
      url: '/cmc/dhcprelay/addDhcpServer.tv?entityId='+entityId+"&bundleInterface=" +bundleInterface
              +"&deviceType=" + deviceType+ "&dhcpServerIp=" + dhcpServerIp,
      type: 'post',
      dataType:'json',
      success: function(response) {
            if(response.message == "success"){                  
                //window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addSuccess);
                window.top.closeWaitingDlg();
                top.afterSaveOrDelete({
	   				title: I18N.RECYLE.tip,
	   				html: '<b class="orangeTxt">'+I18N.CMC.text.addSuccess+'</b>'
   				});
                window.parent.getFrame("entity-" + entityId).onRefresh();
                cancelClick();
             }else{
                 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addFailure);
             }
        }, error: function(response) {
            window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.text.addFailure);
        }, cache: false
    });
}
//转换Bundle号为数字
function transferBundleToNumber(val){
    if(typeof val == "string"){
        var bundleArray = val.replace("bundle", "").split(".");
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
function initIpInput(){
    ip = new ipV4Input("ip","span1");
    ip.width(200);
}

$(function (){
    initIpInput();
    bundleInterfaceEnd = transferBundleToNumber(bundleInterface);
    parentStore = window.parent.getFrame("entity-" + entityId).store;
    $("#relayConfigIndex").val(bundleInterfaceEnd);
});

function cancelClick() {
    window.parent.closeWindow('dhcpServerModify');  
}

function changeBt(){
    var dhcpServerIp = getIpValue("ip");    
    if(!ipInputValid(dhcpServerIp)){
        $("#okBt").attr("disabled",true);
        $("#okBt").mouseout();
        return;
    }
    var disabled = false;
    parentStore.each(function (){
        if(this.data.topCcmtsDhcpBundleInterface == bundleInterface){
            var len = this.data.topCcmtsDhcpHelperIpAddr.length;
            if(len > 0 && this.data.topCcmtsDhcpHelperDeviceType == deviceType){
                for(var i = 0; i < len; i++){
                    if(this.data.topCcmtsDhcpHelperIpAddr[i] == dhcpServerIp){
                        disabled = true;
                    }
                }
            }
        }
    });
    $("#okBt").attr("disabled",disabled);
}
function checkIsFilled(){
    if($("#bundleSelect").val()==0 || $("#deviceTypeSelect").val()==0){
        return false;
    }
    if(!ipIsFilled("ip")){
        setIpBorder("ip","red");
        return false;
    }else{
        setIpBorder("ip","default");
    }
    return true;
}
function checkedId(){
    var id = $("#index").val();
    var reg = /^([\d]{1,5})$/g;
    if(!isNaN(id) && reg.exec(id)){
        return true;
    }
    return false;
}
</script>
</HEAD>
<body  class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						Bundle ID:
	                 </td>
	                 <td>
						<input style="width:200px" id="relayConfigIndex" maxlength=36 disabled class="normalInputDisabled" />
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.label.type'/>:
	                 </td>
	                 <td>
						<select id="deviceTypeSelect" name="type" style="width:200px;" disabled onclick="changeBt()">
							<option value=1 <s:if test="deviceType==1">selected</s:if>>CM</option>
							<option value=2 <s:if test="deviceType==2">selected</s:if>>HOST</option>
							<option value=3 <s:if test="deviceType==3">selected</s:if>>MTA</option>
							<option value=4 <s:if test="deviceType==4">selected</s:if>>STB</option>
							<option value=5 <s:if test="deviceType==5">selected</s:if>>ALL</option>
						</select>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.label.serverIp'/>:
	                 </td>
	                 <td>
						<span id="span1"></span>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id="okBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i><fmt:message bundle='${cmc}' key='CMC.button.modifyConfig'/></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i><fmt:message bundle='${cmc}' key='CMC.button.cancel'/></span></a></li>
		     </ol>
		</div>
		
	</div>
		



</BODY>
</HTML>