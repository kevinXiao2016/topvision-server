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
var deviceOperPower = <%=uc.hasPower("operationDevice")%>;
function doOnload(){
	$("#uplinkBroadcastLimit").val('<s:property value="cmcRateLimit.uplinkIngressBroadcast"/>');
	$("#uplinkMulticastLimit").val('<s:property value="cmcRateLimit.uplinkIngressMulticast"/>');
	$("#cableBroadcastLimit").val('<s:property value="cmcRateLimit.cableIngressBroadcast"/>');
    $("#cableMulticastLimit").val('<s:property value="cmcRateLimit.cableIngressMulticast"/>');
    
    if(!deviceOperPower){
        $("#uplinkBroadcastLimit").attr("disabled",true);
        $("#uplinkMulticastLimit").attr("disabled",true);
        $("#cableBroadcastLimit").attr("disabled",true);
        $("#cableMulticastLimit").attr("disabled",true);
        $("#saveBt").attr("disabled",true);
    }
}

function saveClick() {
	var uplinkBroadcastLimit = $("#uplinkBroadcastLimit").val();
	if(!checkBroadcastValue(uplinkBroadcastLimit)){
		$("#uplinkBroadcastLimit").focus();
        return;
	}
	var uplinkMulticastLimit = $("#uplinkMulticastLimit").val();
    if(!checkBroadcastValue(uplinkMulticastLimit)){
        $("#uplinkMulticastLimit").focus();
        return;
    }
    var cableBroadcastLimit = $("#cableBroadcastLimit").val();
    if(!checkBroadcastValue(cableBroadcastLimit)){
        $("#cableBroadcastLimit").focus();
        return;
    }
    var cableMulticastLimit = $("#cableMulticastLimit").val();
    if(!checkBroadcastValue(cableMulticastLimit)){
        $("#cableMulticastLimit").focus();
        return;
    }
    $.ajax({
        url:'/cmc/sni/modifyStormLimitConfig.tv?cmcId=' + cmcId,
        type:'POST',
        data:$("#formChanged").serialize(),
        dateType:'text',
        success:function(response) {
            if (response == "success") {
                //window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.stormLimitConfigSuccess);
            	top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">' + I18N.cmcSni.stormLimitConfigSuccess + '</b>'
       			});
            } else {
                window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.stormLimitConfigFail);
            }
            cancelClick();
        },
        error:function() {
            window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcSni.stormLimitConfigFail);
        },
        cache:false
    });
}
function checkBroadcastValue(v){
	var reg1 = /^([0-9])+$/;
    if (!reg1.exec(v)|| (v > 1488100) || v < 0) {
        return false;
    }else{
        return true;
    }
}
function cancelClick() {
	window.parent.closeWindow('stromLimit');	
}
</script>
</head>
<body class="openWinBody"  onload="doOnload()">
	<form name="formChanged" id="formChanged">		
		<div class="edge10 pT20">
			<div class="zebraTableCaption">
		     	<div class="zebraTableCaptionTitle"><span>@cmcSni.sni@</span></div>
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="260">
			                    @cmcSni.broadcastLimit@
			                </td>
			                <td>
			                    <input type=text id="uplinkBroadcastLimit" class="normalInput" maxlength="7" toolTip='@cmcSni.inputBroadcastTip@'
			                        name="cmcRateLimit.uplinkIngressBroadcast" style="width:200px;" /> pps
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    @cmcSni.multicastLimit@
			                </td>
			                <td>
			                   <input type=text id="uplinkMulticastLimit" class="normalInput" maxlength="7" toolTip='@cmcSni.inputBroadcastTip@'
		                        name="cmcRateLimit.uplinkIngressMulticast"  style="width:200px;" /> pps
			                </td>
			            </tr>
			        </tbody>
			    </table>
			</div>
			
			<div class="zebraTableCaption mT20">
		     	<div class="zebraTableCaptionTitle"><span>@cmcSni.cable@</span></div>
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="260">
			                    @cmcSni.broadcastLimit@
			                </td>
			                <td>
			                    <input type=text id="cableBroadcastLimit" class="normalInput" maxlength="7" toolTip='@cmcSni.inputBroadcastTip@'
		                        name="cmcRateLimit.cableIngressBroadcast" style="width:200px;" /> pps
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    @cmcSni.multicastLimit@
			                </td>
			                <td>
			                   <input type=text id="cableMulticastLimit" class="normalInput" maxlength="7" toolTip='@cmcSni.inputBroadcastTip@'
		                        name="cmcRateLimit.cableIngressMulticast"
		                       style="width:200px;" /> pps
			                </td>
			            </tr>
			        </tbody>
			    </table>
			</div>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
			         <li><a id="saveBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
			         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			     </ol>
			</div>
		</div>
	</form>
</body>
</Zeta:HTML>