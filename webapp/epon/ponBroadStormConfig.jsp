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
var entityId = '<s:property value="entityId"/>';
var ponPortType = '<s:property value="ponPortType"/>';
var rawUnicast = "<s:property value='ponStormSuppression.unicastStormInPacketRate'/>";
var rawMulticase = "<s:property value='ponStormSuppression.multicastStormInPacketRate'/>";
var rawBroadcase = "<s:property value='ponStormSuppression.broadcastStormInPacketRate'/>";
// 千兆口速率最大值：5000 万兆口速率最大值：5000pps
//统一按照最大值限制
var maxRate = 148810;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
if (ponPortType.split('-')[0] == 'GE') {
	maxRate = 1488100;
} else if (ponPortType.split('-')[0] == 'TENGE') {
	maxRate = 1488100;
} else if (ponPortType.split('-')[0] == 'GPON') {
	// TODO GPON尚未实现【预留】
}
var ponId = '<s:property value="ponId"/>';
function initData() {
	var unicastStormEnable = '<s:property value="ponStormSuppression.unicastStormEnable" />';
	if (unicastStormEnable != 1) {
		$('#unicastStormInPacketRate').attr('disabled', true);
	}
	var multicastStormEnable = '<s:property value="ponStormSuppression.multicastStormEnable" />';
	if (multicastStormEnable != 1) {
		$('#multicastStormInPacketRate').attr('disabled', true);
	}
	var broadcastStormEnable = '<s:property value="ponStormSuppression.broadcastStormEnable" />';
	if (broadcastStormEnable != 1) {
		$('#broadcastStormInPacketRate').attr('disabled', true);
	}
}
function checkValid() {
	if ($('#unicastStormEnable').attr('checked')) {
		var uin = $('#unicastStormInPacketRate').val();
		if (isNaN(uin) || uin < 1 || uin > maxRate) {
			$("#unicastStormInPacketRate").focus();
			return false;
		}
	}
	if ($('#multicastStormEnable').attr('checked')) {
		var min = $('#multicastStormInPacketRate').val();
		if (isNaN(min) || min < 1 || min > maxRate) {
			$("#multicastStormInPacketRate").focus();
			return false;
		}
	}
	if ($('#broadcastStormEnable').attr('checked')) {
		var bin = $('#broadcastStormInPacketRate').val();
		if (isNaN(bin) || bin < 1 || bin > maxRate) {
			$("#broadcastStormInPacketRate").focus();
			return false;
		}
	}
	return true;
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	var unicastStormEnable = $('#unicastStormEnable').attr('checked') ? 1 : 2;
	var unicastStormInPacketRate = $('#unicastStormEnable').attr('checked') ? $('#unicastStormInPacketRate').val() : 0;
	var multicastStormEnable = $('#multicastStormEnable').attr('checked') ? 1 : 2;
	var multicastStormInPacketRate = $('#multicastStormEnable').attr('checked') ? $('#multicastStormInPacketRate').val() : 0;
	var broadcastStormEnable = $('#broadcastStormEnable').attr('checked') ? 1 : 2;
	var broadcastStormInPacketRate = $('#broadcastStormEnable').attr('checked') ? $('#broadcastStormInPacketRate').val() : 0;
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.savingBdcastParam , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/modifyPonBroadStormConfig.tv?r=' + Math.random(),
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@SERVICE.saveBdcastParamOk@</b>'
       	    });
			cancelClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.saveBdcastParamEr )
		},
		params : {
			entityId: entityId,
			ponId: ponId,
			unicastStormEnable: unicastStormEnable,
			unicastStormInPacketRate: unicastStormInPacketRate,
			multicastStormEnable: multicastStormEnable,
			multicastStormInPacketRate: multicastStormInPacketRate,
			broadcastStormEnable: broadcastStormEnable,
			broadcastStormInPacketRate: broadcastStormInPacketRate
		}
	});
}

function refreshClick(){
	window.parent.showWaitingDlg(I18N.COMMON.wait , I18N.COMMON.fetching , 'ext-mb-waiting');
	var params = {entityId: entityId};
	$.ajax({
		url : '/epon/refreshPonBroadCastStorm.tv?&r=' + Math.random(),
		success : function(response) {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			location.reload();
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
			$("#refreshBt").attr("disabled", false);
		},
		data: params
	});
}

function cancelClick() {
    window.parent.closeWindow('ponBroadStormConfig');
}
function enableChange(id) {
	if (id == 'unicastStormEnable') {
		if ($('#unicastStormEnable').attr('checked')) {
			$('#unicastStormInPacketRate').attr('disabled', false);
			$('#unicastStormInPacketRate').val(parseInt(rawUnicast) || 500);
		} else {
			$('#unicastStormInPacketRate').attr('disabled', true);
			$('#unicastStormInPacketRate').val(0);
		}
	} else if (id == 'multicastStormEnable') {
		if ($('#multicastStormEnable').attr('checked')) {
			$('#multicastStormInPacketRate').attr('disabled', false);
			$('#multicastStormInPacketRate').val(parseInt(rawMulticase) || 500);
		} else {
			$('#multicastStormInPacketRate').attr('disabled', true);
			$('#multicastStormInPacketRate').val(0);
		}
	} else if (id == 'broadcastStormEnable') {
		if ($('#broadcastStormEnable').attr('checked')) {
			$('#broadcastStormInPacketRate').attr('disabled', false);
			$('#broadcastStormInPacketRate').val(parseInt(rawBroadcase) || 500);
		} else {
			$('#broadcastStormInPacketRate').attr('disabled', true);
			$('#broadcastStormInPacketRate').val(0);
		}
	}
}
function authLoad(){
	if(!operationDevicePower){
	    $("#unicastStormEnable").attr("disabled",true);
	    $("#unicastStormInPacketRate").attr("disabled",true);
	    $("#multicastStormEnable").attr("disabled",true);
	    $("#multicastStormInPacketRate").attr("disabled",true);
	    $("#broadcastStormEnable").attr("disabled",true);
	    $("#broadcastStormInPacketRate").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="initData();authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@EPON.broadCastConfig@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edge10 pB0 clearBoth tabBody" >
		<table class="dataTableRows zebraTableRows" width="100%" >
			<thead>
				<tr>
					<th colspan="2" class="txtLeftTh">@SERVICE.unknownPkg@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w240">@SERVICE.unknownPkgDecEn@</td>
					<td>
						<s:if test="ponStormSuppression.unicastStormEnable == 1">
							<input type="checkbox" id="unicastStormEnable" onclick="enableChange('unicastStormEnable')" checked>
						</s:if> 
						<s:else>
							<input type="checkbox" id="unicastStormEnable" onclick="enableChange('unicastStormEnable')">
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@SERVICE.unknownPkgInDerc@</td>
					<td><input type=text id="unicastStormInPacketRate"
						value="${ponStormSuppression.unicastStormInPacketRate}"
						class="normalInput w150"
						tooltip="String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate)"
						maxlength="6" >&nbsp;PPS</td>
				</tr>
				</tbody>
			</table>
		</div>	
		
		<div class="edge10 pB0 clearBoth tabBody" >
		<table class="dataTableRows zebraTableRows" width="100%" >
			<thead>
				<tr>
					<th colspan="2" class="txtLeftTh">@SERVICE.mvlan@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w240">@SERVICE.mvlanDec@</td>
					<td>
						<s:if
							test="ponStormSuppression.multicastStormEnable == 1">
							<input type=checkbox id="multicastStormEnable" onclick="enableChange('multicastStormEnable')" checked>
						</s:if>
						<s:else>
							<input type=checkbox id="multicastStormEnable" onclick="enableChange('multicastStormEnable')">
						</s:else>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@SERVICE.mvlanInPkgRate@</td>
					<td><input type=text
						id="multicastStormInPacketRate"
						value="${ponStormSuppression.multicastStormInPacketRate }"
						class="normalInput w150"
						tooltip="String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate)"
						maxlength="6" />&nbsp;PPS</td>
				</tr>
				</tbody>
			</table>
		</div>
		
		<div class="edge10 pB0 clearBoth tabBody" >
		<table class="dataTableRows zebraTableRows" width="100%" >
			<thead>
				<tr>
					<th colspan="2" class="txtLeftTh">@SERVICE.broadCast@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w240">@SERVICE.bdcastDecEn@</td>
					<td><s:if
							test="ponStormSuppression.broadcastStormEnable == 1">
							<input type=checkbox id="broadcastStormEnable" onclick="enableChange('broadcastStormEnable')" checked>
						</s:if> <s:else>
							<input type=checkbox id="broadcastStormEnable" onclick="enableChange('broadcastStormEnable')">
						</s:else></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@SERVICE.bdcastInPkgRate@</td>
					<td><input type=text
						id="broadcastStormInPacketRate"
						value="${ponStormSuppression.broadcastStormInPacketRate }"
						class="normalInput w150"
						tooltip="String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate)"
						maxlength="6">&nbsp;PPS</td>
				</tr>
				</tbody>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshBt" icon="miniIcoEquipment" onClick="refreshClick()">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSaveOK">@BUTTON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
</body>
</Zeta:HTML>