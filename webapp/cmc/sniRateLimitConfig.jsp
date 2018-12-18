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
	$("#icmpLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteUplinkEgressIcmp"/>');
	$("#igmpLimit").val('<s:property value="cmcRateLimit.topCcmtsRateLimiteUplinkEgressIgmp"/>');
}

function saveClick() {
	var icmpLimit = $("#icmpLimit").val();
	if(!checkValue(icmpLimit)){
		$("#icmpLimit").focus();
		return;
	}
	var igmpLimit = $("#igmpLimit").val();
	if(!checkValue(igmpLimit)){
		$("#igmpLimit").focus();
		return;
	}
    $.ajax({
        url:'/cmc/sni/modifyCmcSniRateLimit.tv?cmcId=' + cmcId + '&icmpLimit=' + icmpLimit + "&igmpLimit=" + igmpLimit,
        type:'POST',
        data:$("#formChanged").serialize(),
        dateType:'text',
        success:function(response) {
            if (response == "success") {
                //window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@cmcSni.setSniRateLimitSuccess@");
    			top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">@cmcSni.setSniRateLimitSuccess@</b>'
       			});
            } else {
                window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@cmcSni.setSniRateLimitFail@");
            }
            cancelClick();
        },
        error:function() {
            window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@cmcSni.setSniRateLimitFail@");
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
function checkBroadcastValue(v){
	var reg1 = /^([0-9])+$/;
    if (!reg1.exec(v)|| (v > 1488100) || v < 0) {
        return false;
    }else{
        return true;
    }
}
function cancelClick() {
	window.parent.closeWindow('sniRateLimit');	
}
function authLoad(){
	if(!operationDevicePower){
		$("#icmpLimit").attr("disabled",true);
		$("#igmpLimit").attr("disabled",true);
		$("#saveBt").attr("disabled",true);
	}
}
</script>
</head>
<body class="openWinBody" onload="doOnload();authLoad()">
<form name="formChanged" id="formChanged">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco alarmCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="240">
	                    @cmcSni.icmpLimit@
	                </td>
	                <td>
	                    <input type=text id="icmpLimit" class="normalInput" maxlength="5" 
					    	name="cmcRateLimit.topCcmtsRateLimiteUplinkEgressIcmp" toolTip='@cmcSni.inputTip@'
							style="width: 125px;" /> pps
	                </td>
	            </tr>
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    @cmcSni.igmpLimit@
	                </td>
	                <td>
	                   <input type=text id="igmpLimit" class="normalInput" maxlength="5" toolTip='@cmcSni.inputTip@'
					    name="cmcRateLimit.topCcmtsRateLimiteUplinkEgressIgmp" style="width: 125px;" /> pps
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id="saveBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a  id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</form>
</body>
</Zeta:HTML>