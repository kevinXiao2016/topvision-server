<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var cmcId = '<s:property value="cmcId"/>';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
function doOnload(){
	$("#arpLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteCpuPortEgressArp"/>');
	$("#uniLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteCpuPortEgressUni"/>');
	$("#udpLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteCpuPortEgressUdp"/>');
	$("#dhcpLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteCpuPortEgressDhcp"/>');
}

function saveClick() {
	var arpLimit = $("#arpLimit").val();
	if(!checkValue(arpLimit)){
		$("#arpLimit").focus();
		return;
	}
	var uniLimit = $("#uniLimit").val();
	if(!checkValue(uniLimit)){
		$("#uniLimit").focus();
		return;
	}
	var udpLimit = $("#udpLimit").val();
	if(!checkValue(udpLimit)){
		$("#udpLimit").focus();
		return;
	}
	var dhcpLimit = $("#dhcpLimit").val();
	if(!checkValue(dhcpLimit)){
		$("#dhcpLimit").focus();
		return;
	}
    $.ajax({
        url:'/cmc/sni/modifyCmcCpuPortRateLimit.tv?cmcId=' + cmcId + '&arpLimit=' + arpLimit + "&uniLimit=" + uniLimit+ "&udpLimit=" + udpLimit+ "&dhcpLimit=" + dhcpLimit,
        type:'POST',
        dateType:'text',
        success:function(response) {
            if (response == "success") {
                //window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setCpuPortRateLimitSuccess);
    			top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">'+ I18N.cmcSni.setCpuPortRateLimitSuccess + '</b>'
       			});
            } else {
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setCpuPortRateLimitFail);
            }
            cancelClick();
        },
        error:function() {
            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.setCpuPortRateLimitFail);
        },
        cache:false
    });
}

function checkValue(v) {
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 65535) || v < 0) {
		return false;
	}else{
		return true;
	}
}

function cancelClick() {
	window.parent.closeWindow('cpuRateLimit');	
}
function authLoad(){
	if(!operationDevicePower){
		$("#saveBt").attr("disabled",true);
		$("#arpLimit").attr("disabled",true);
		$("#uniLimit").attr("disabled",true);
		$("#udpLimit").attr("disabled",true);
		$("#dhcpLimit").attr("disabled",true);
	}
}
</script>
</head>
<body class="openWinBody" onload="doOnload();authLoad();">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco equipmentCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT60">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="300">
	                    @cmcSni.arpLimit@
	                </td>
	                <td>
	                    <input type=text id="arpLimit" class="normalInput w200" maxlength="5" toolTip='@cmcSni.inputTip@'  />pps
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   @cmcSni.uniLimit@
	                </td>
	                <td>
	                   <input type=text id="uniLimit" class="normalInput w200" maxlength="5" toolTip='@cmcSni.inputTip@'  />pps
	                </td>
	            </tr>
	            <tr>
	                <td class="rightBlueTxt">
	                    @cmcSni.udpLimit@
	                </td>
	                <td>
	                    <input type=text id="udpLimit" class="normalInput w200" maxlength="5" toolTip='@cmcSni.inputTip@'  />pps
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                   @cmcSni.dhcpLimit@
	                </td>
	                <td>
	                   <input type=text id="dhcpLimit" class="normalInput w200" maxlength="5" toolTip='@cmcSni.inputTip@'  />pps
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id="saveBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>

</body>
</Zeta:HTML>