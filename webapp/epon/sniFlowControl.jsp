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
var entityId = '<s:property value="entity.entityId"/>';
var currentId = '<s:property value="currentId"/>';
var sniId = '<s:property value="sniId"/>';
var sniAutoNegotiationMode = '<s:property value="sniAutoNegotiationMode"/>';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var ingressRate = '<s:property value="ingressRate"/>';
var egressRate = '<s:property value="egressRate"/>';
var isXeFiber = <s:property value="portIsXGUxFiber"/>;
function doOnload(){
	Zeta$('inFlowCtrl').value = ingressRate;
   	Zeta$('outFlowCtrl').value = egressRate; 
}
function saveClick() {
	var inFlowCtrl = Zeta$('inFlowCtrl').value;
    var outFlowCtrl = Zeta$('outFlowCtrl').value;
    if(inFlowCtrl == "" || outFlowCtrl == ""){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.flowCtrolParamTip )
    }else{
        if(!checkInValue()){
        	$("#inFlowCtrl").focus();
        	return ;
        }
        if(!checkOutValue()){
    		$("#outFlowCtrl").focus();
			return ;
        }
	    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmMdfSniFlowControl , function(type) {
		if (type == 'no') {return;}
	      	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.mdfingSniFlowControlParam , 'ext-mb-waiting');
	          Ext.Ajax.request({url:"/epon/updateSniCtrlFlow.tv?r=" + Math.random(),
	          	params: {entityId : entityId,ingressRate : inFlowCtrl, egressRate : outFlowCtrl, sniId : sniId},
	      		success: function (response) {
	      			if (response && response.responseText) {
	      				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.mdfSniFlowControlParamEr )
	      			} else {
	      				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.mdfSniFlowControlParamOk )
		      	        updateOltJson(currentId, 'topSniAttrIngressRate', inFlowCtrl);
		      	        updateOltJson(currentId, 'topSniAttrEgressRate', outFlowCtrl);
		      	        cancelClick();
	      			}
	      	    }, failure: function () {
	      	        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.mdfSniFlowControlParamEr )
	      	        cancelClick();
	      	    }
	      	});
	    });
    }
}
function checkInValue() {
	var reg1 = /^([0-9])+$/;
	var inValue = $("#inFlowCtrl").val();
	var maxValue = isXeFiber ? 10000000 : 1000000;
	if ( (reg1.exec(inValue) && inValue <= maxValue && inValue >= 64 ) || inValue == 0 ) {
		return true;
	}else{
		return false;
	}
}
function checkOutValue(){
	var reg1 = /^([0-9])+$/;
	var outValue = $("#outFlowCtrl").val();
	var maxValue = isXeFiber ? 10000000 : 1000000;
	if ( (reg1.exec(outValue) && outValue <= maxValue && outValue >= 64 ) || outValue == 0 ) {
		return true;
	}else{
		return false;
	}
}
function updateOltJson(currentId, attr, value) {
	window.parent.getFrame("entity-" + entityId).updateOltJson(currentId, attr, value);
}

function refreshClick(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/refreshSniFlowControl.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			sniId : sniId
		},
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

function cancelClick() {
	// 关闭窗口后，面板页面重新计时。
	window.parent.getFrame("entity-" + entityId).timer = 0;
    window.parent.closeWindow('sniFlowControl');
}
function showTooltip(){
	 return isXeFiber ? "@SERVICE.range10000000@" : "@SERVICE.range1000000@";
}
function authLoad(){
	if(!operationDevicePower){
	    $("#inFlowCtrl").attr("disabled",true);
	    $("#outFlowCtrl").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="doOnload();authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.sniFlowLimitConfig@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w240">@SERVICE.indircFlowControl@</td>
						<td><input type=text id="inFlowCtrl" maxlength="8" class="normalInput"
							tooltip="javascript:showTooltip()" style="width: 125px;"> Kbps</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@SERVICE.outdircFlowControl@</td>
						<td><input type=text id="outFlowCtrl" maxlength="8"
							tooltip="javascript:showTooltip()" style="width: 125px;" class="normalInput"> Kbps</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshBt" icon="miniIcoEquipment" onClick="refreshClick()">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>