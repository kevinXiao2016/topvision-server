<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext	
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '${entityId}';
var uniId = '${oltUniPortRateLimit.uniId}';
var inCIR = '${oltUniPortRateLimit.uniPortInCIR}';
var inCBS = '${oltUniPortRateLimit.uniPortInCBS}';
var inEBS = '${oltUniPortRateLimit.uniPortInEBS}';
var outCIR = '${oltUniPortRateLimit.uniPortOutCIR}';
var outPIR = '${oltUniPortRateLimit.uniPortOutPIR}';

function saveClick() {
	 var inCheck = $("#inCheck").attr("checked")?1:2;
	 var inCIR = $("#inCIR").val();
	 var inCBS = $("#inCBS").val();
	 var inEBS = $("#inEBS").val();
	 var outCheck = $("#outCheck").attr("checked")?1:2;
	 var outCIR = $("#outCIR").val();
	 var outPIR = $("#outPIR").val();
	 if(inCheck == 1){
		if(!checkInput(inCIR)||inCIR > 1000000 || inCIR < 1){
			return Zeta$("inCIR").focus();
		}
		if(!checkInput(inCBS)||inCBS > 16383  || inCBS < 0){
			return Zeta$("inCBS").focus();
		}
		if(!checkInput(inEBS)||inEBS > 16383 || inEBS < 0){
			return Zeta$("inEBS").focus();
		}
	 }
	 if(outCheck == 1){
		 if(!checkInput(outCIR)||outCIR > 1000000 || outCIR < 0){
			 return Zeta$("outCIR").focus();
		 }
		 if(!checkInput(outPIR)||outPIR > 1000000 || outPIR < 0){
			 return Zeta$("outPIR").focus();
		 }
		 if(parseInt(outPIR) < parseInt(outCIR)){
			 return Zeta$("outPIR").focus();
		 }
	 }
	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.udtingUniRateCfg ,'ext-mb-waiting');
	 $.ajax({
	        url: '/onu/updateUniPortRateLimit.tv',
	        type: 'POST',
	        data: "uniId=" + uniId + "&entityId=" + entityId + "&uniInEnable=" + inCheck +"&uniOutEnable=" + outCheck + "&uniPortInCBS=" + inCBS + "&uniPortInCIR=" + inCIR +"&uniPortInEBS=" + inEBS + "&uniPortOutPIR=" + outPIR + "&uniPortOutCIR=" + outCIR + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.udtUniRateCfgOk )
	            	cancelClick();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.udtUniRateCfgEr, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.udtUniRateCfgEr ,'error');
	    }, cache: false
	});
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
function initData() {
	var inEnable = '${oltUniPortRateLimit.uniPortInRateLimitEnable}';
	if (inEnable != 1) {
		$('#inCIR').attr({"class" : "normalInputDisabled",disabled: true});
		$('#inCBS').attr({"class" : "normalInputDisabled",disabled: true});
		$('#inEBS').attr({"class" : "normalInputDisabled",disabled: true});
	}
	var outEnable = '${oltUniPortRateLimit.uniPortOutRateLimitEnable}';
	if (outEnable != 1) {
		$('#outCIR').attr({"class" : "normalInputDisabled",disabled: true});
		$('#outPIR').attr({"class" : "normalInputDisabled",disabled: true});
	}
}
function enableChange(id) {
	if (id == 'inCheck') {
		if ($('#inCheck').attr('checked')) {
			$('#inCIR').attr({"class" : "normalInput",disabled: false});
			$('#inCBS').attr({"class" : "normalInput",disabled: false});
			$('#inEBS').attr({"class" : "normalInput",disabled: false});
			$("#inCIR").val(inCIR);
			$("#inCBS").val(inCBS);
			$("#inEBS").val(inEBS);
		} else {
			$('#inCIR').attr({"class" : "normalInputDisabled",disabled: true});
			$('#inCBS').attr({"class" : "normalInputDisabled",disabled: true});
			$('#inEBS').attr({"class" : "normalInputDisabled",disabled: true});
			$("#inCIR").val(0);
			$("#inCBS").val(0);
			$("#inEBS").val(0);
		}
	} else if (id == 'outCheck') {
		if ($('#outCheck').attr('checked')) {
			$('#outCIR').attr({"class" : "normalInput",disabled: false});
			$('#outPIR').attr({"class" : "normalInput",disabled: false});
			$("#outCIR").val(outCIR);
			$("#outPIR").val(outPIR);
		} else {
			$('#outCIR').attr({"class" : "normalInputDisabled",disabled: true});
			$('#outPIR').attr({"class" : "normalInputDisabled",disabled: true});
			$("#outCIR").val(0);
			$("#outPIR").val(0);
		}
	} 
}
function cancelClick() {
	window.parent.closeWindow('uniRateLimit');	
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching );
	 $.ajax({
	        url: '/onu/refreshUniRateLimit.tv',
	        type: 'POST',
	        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk );
	            	window.location.reload();
		        }else{
		        	if(text.split(":")[0] == "no response"){
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr, 'error');
				    }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr, 'error');
					}
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr,'error');
	    }, cache: false
	    });
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.saveBt.setDisabled(true);
	}
	if(!refreshDevicePower){
	    R.refreshBt.setDisabled(true);
	}
}
</script>
</head>
<body class="openWinBody" onload="initData();authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
			<div class="openWinTip">@ONU.uniRateLimit@</div>
			<div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT10">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td colspan="4">
	                 	<ul class="leftFloatUl">
	                 		<li style="padding-left:50px;">
	                 			<s:if  test="oltUniPortRateLimit.uniPortInRateLimitEnable == 1">
									<input type=checkbox id="inCheck" value="1"
										onclick="enableChange('inCheck')" checked>
								</s:if> 
								<s:else>
									<input type=checkbox id="inCheck" value="2"
										onclick="enableChange('inCheck')">
								</s:else>
	                 		</li>
	                 		<li>
	                 			<b class="blueTxt">@SERVICE.inRateRestrictEn@</b>
	                 		</li>
	                 	</ul>
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt" width="100">
						CIR:
	                 </td>
	                 <td width="150">
						<input type=text id="inCIR" class="normalInput"
								value="${oltUniPortRateLimit.uniPortInCIR}"
								size=10 maxlength=7 tooltip="@SERVICE.uniPortInCIRTip@" /> Kbps
	                 </td>
	                 <td class="rightBlueTxt" width="60">
						CBS:
	                 </td>
	                 <td>
						<input type=text id="inCBS" class="normalInput"
								value="${oltUniPortRateLimit.uniPortInCBS}"
								size=10 maxlength=5  tooltip="@SERVICE.uniPortInCBSTip@" /> KBytes
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						EBS:
	                 </td>
	                 <td>
						<input type=text id="inEBS" class="normalInput"
								value="${oltUniPortRateLimit.uniPortInEBS}"
								size=10 maxlength=5  tooltip="@SERVICE.uniPortInEBSTip@" /> KBytes
	                 </td>
	                  <td class="rightBlueTxt">
	
	                 </td>
	                 <td>
	
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td colspan="4">
						<ul class="leftFloatUl">
	                 		<li style="padding-left:50px;">
	                 			<s:if test="oltUniPortRateLimit.uniPortOutRateLimitEnable == 1">
									<input type=checkbox id="outCheck" value="1" 
										onclick="enableChange('outCheck')" checked>
								</s:if> <s:else>
									<input type=checkbox id="outCheck" value="2" 
										onclick="enableChange('outCheck')">
								</s:else>
	                 		</li>
	                 		<li>
	                 			<b class="blueTxt">@SERVICE.outRateRestrictEn@</b>
	                 		</li>
	                 	</ul>
	                 </td>
	             </tr>
	             <tr>
	                 <td class="rightBlueTxt">
						PIR:
	                 </td>
	                 <td>
						<input type=text id="outPIR" class="normalInput"
						value="${oltUniPortRateLimit.uniPortOutPIR}"
						size=10 maxlength=7  tooltip="@SERVICE.rangePIR@" /> Kbps
	                 </td>
	                  <td class="rightBlueTxt">
						CIR:
	                 </td>
	                 <td>
						<input type=text id="outCIR" class="normalInput"
						value="${oltUniPortRateLimit.uniPortOutCIR}"
						size=10 maxlength=7 tooltip="@SERVICE.rangeCIR@" /> Kbps
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshBt" onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
			<Zeta:Button id="cancelBt" onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>