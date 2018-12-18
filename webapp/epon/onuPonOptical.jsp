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
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;

var entityId = <s:property value="entityId" />;
var portIndex = <s:property value="portIndex" />;
var opticalVal = ${opticalVal};
if(opticalVal.join("") == "false"){
	opticalVal = new Array();
}

function closeClick() {
	window.parent.closeWindow('modalDlg');	
}
$(function(){
	initHtmlVal();
});
function initHtmlVal(){
	if(opticalVal.length){
		//$("#tempreture").html(opticalVal[1] == 0 ? I18N.Optical.couldntGetData : (opticalVal[1] / 100) +'  '+ I18N.COMMON.degreesCelsius);
		$("#tempreture").html(opticalVal[1] == null ? '--' : opticalVal[6] +'  '+ "@{unitConfigConstant.tempUnit}@");
		$("#receivedOpticalPower").html(opticalVal[2] == null ? '--' : (opticalVal[2] / 100) +'  '+I18N.COMMON.dBm);
		$("#onuTramsmittedOpticalPower").html(opticalVal[3] == null ? '--' : (opticalVal[3] / 100) +'  '+I18N.COMMON.dBm);
		$("#onuBiasCurrent").html(opticalVal[4] == null ? '--' : (opticalVal[4] / 100) +'  mA');
		$("#onuWorkingVoltage").html(opticalVal[5] == null ? '--' : (opticalVal[5] / 100000) +'  V');
	}
}

function refreshClick(el){
	R.refreshBt.setDisabled(true);
	$("#tipFont").text(I18N.Optical.fetching).css("color", "blue").show();
	var url = '/epon/optical/loadOnuPonOptical.tv?r=' + Math.random();
	var par = {entityId: entityId, portIndex: portIndex};
	window.parent.showWaitingDlg('@COMMON.wait@', '@COMMON.fetching@' , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText == 'false'){
				$("#tipFont").text(I18N.Optical.fetchFailed).css("color", "red").show();
				R.refreshBt.setDisabled(false);
				window.parent.closeWaitingDlg();
				return;
			}
			opticalVal = Ext.util.JSON.decode(response.responseText).data || new Array();
			initHtmlVal();
			var pa = window.parent.getFrame("entity-" + entityId);
			if(pa){
				var tmpSource = pa.page.grid.getSource();
				//tmpSource[I18N.ONU.tempreture] = opticalVal[1] && opticalVal[1] != 0 ? parseInt(opticalVal[1])/100 + "  "+ I18N.COMMON.degreesCelsius : I18N.Optical.couldntGetData;
				tmpSource[I18N.ONU.tempreture] = opticalVal[6] == null ? '--' : opticalVal[6] + "  "+ "@{unitConfigConstant.tempUnit}@" ;
				tmpSource[I18N.ONU.recOptRate] = opticalVal[2] == null ? '--' : parseInt(opticalVal[2])/100 + "  "+I18N.COMMON.dBm;
				tmpSource[I18N.ONU.sendOptRate] = opticalVal[3] == null ? '--' : parseInt(opticalVal[3])/100 + "  "+I18N.COMMON.dBm ;
				tmpSource[I18N.SUPPLY.biasCurrent] = opticalVal[4] == null ? '--' : parseInt(opticalVal[4])/100 + "  mA" ;
				tmpSource[I18N.ONU.voltage] = opticalVal[5] == null ? '--' : parseInt(opticalVal[5])/100000 + "  V";
				pa.page.grid.setSource(tmpSource);
			}
			window.parent.closeWaitingDlg();
			$("#tipFont").text(I18N.Optical.fetchSuc).css("color", "green").show();
			R.refreshBt.setDisabled(false);
		},
		failure : function() {
			$("#tipFont").text(I18N.Optical.fetchFailed).css("color", "red").show();
			R.refreshBt.setDisabled(false);
		},
		params : par
	});
}
function authLoad(){
    if(!refreshDevicePower){
        R.refreshBt.setDisabled(true);
    }
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class="openWinHeader">
			<div class="openWinTip">@onu.onuponoptinfo@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class=" w240 rightBlueTxt">@ONU.recOptRate@:</td>
						<td>
						<div id='receivedOpticalPower'></div></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@ONU.optTransRate@:</td>
						<td>
						<div id='onuTramsmittedOpticalPower'></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@SUPPLY.biasCurrent@:</td>
						<td>
						<div id='onuBiasCurrent'></div></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@ONU.voltage@:</td>
						<td>
						<div id='onuWorkingVoltage'></div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ONU.tempretureOpt@:</td>
						<td>
							<div id='tempreture'></div>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshBt" onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="closeClick()">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>