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
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var entityId = <s:property value="entityId" />;
var portIndex = <s:property value="portIndex" />;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
//0:sni, 1:pon
var portType = <s:property value="portType" />;
var attrStr = [I18N.Optical.identifier, I18N.Optical.vendorName, I18N.Optical.waveLength, 
       		I18N.Optical.vendorPN, I18N.Optical.vendorSN, I18N.Optical.dateCode,I18N.Optical.bitRate];
var statStr = [I18N.Optical.workingTemp, I18N.Optical.workingV, I18N.Optical.biasCurrent, I18N.Optical.tx, I18N.Optical.rx];
var opticalVal = ${opticalVal};
if(opticalVal.join("") == "false"){
	opticalVal = new Array();
}
var fiberType = [I18N.Optical.unknownType, "GBIC", I18N.Optical.moduleType, "SFP","XBI","XENPAK","XFP","XFF","XFP_E","XPAK","X2"];

Ext.onReady(function(){
	for(var a=0; a<5; a++){
		$("#statFont" + (a + 1)).text(statStr[a]);
	}
	for(var a=0; a<7; a++){
		$("#attrFont" + (a + 1)).text(attrStr[a]);
	}
	//$("#attrFont6").text(attrStr[portType][5]);
	initData();
	
	//SNI口才有接收功率显示,做出限制
	if(portType == 1){
		$("#portOptical").hide();
	}
	
});
function initData(){//opticalVal
	$("#attr1").text(fiberType[opticalVal[0]] ? fiberType[opticalVal[0]] : fiberType[0]);
	$("#attr2").text(opticalVal[1]);
	$("#attr3").text(opticalVal[2] && opticalVal[2] != 0 ? (opticalVal[2] + "  nm") : I18N.Optical.couldntGetData);
	$("#attr4").text(opticalVal[3]);
	$("#attr5").text(opticalVal[4]);
	$("#attr6").text(opticalVal[5]);
	$("#attr7").text(opticalVal[11]*100 +"  Mb/s");
	$("#stat1").text(getOpticalValStr(6));
	$("#stat2").text(getOpticalValStr(7));
	$("#stat3").text(getOpticalValStr(8));
	$("#stat4").text(getOpticalValStr(9));
	$("#stat5").text(getOpticalValStr(10));
	//$("#stat6").text(getOpticalValStr(11));
}
function getOpticalValStr(s){
	if(opticalVal[s]){
		switch (s){
		//case 6: return (Math.round((opticalVal[6] / 256) * 1000) / 1000) + " "+ I18N.COMMON.degreesCelsius;
		case 6: 
			if(opticalVal[6] == 65535){
				return "--" + " @{unitConfigConstant.tempUnit}@"
			}else{
				return opticalVal[6] + " @{unitConfigConstant.tempUnit}@";
			}
		case 7:
			if(opticalVal[7] == 65535){
				return "--" + "  mV";
			}else{
				return (opticalVal[7] / 10) + "  mV";
			}
		case 8: 
			if(opticalVal[8] == 65535){
				return "-- " + I18N.COMMON.microampere;
			}else{
				return (2 * opticalVal[8]) + "  " + I18N.COMMON.microampere;
			}
		//powerNew[dBm] = 10*log10((double)  powerOld[0.1μW]  )-40 
		case 9: 
			if(opticalVal[9] == 0 || opticalVal[9] == 65535){
				return "--  "+I18N.COMMON.dBm;
			}else{
				return (Math.round((10 * (Math.log(opticalVal[9]) / Math.LN10) - 40) * 10000) / 10000) + "  "+I18N.COMMON.dBm;
			}
		case 10: 
			if(opticalVal[10] == 0 || opticalVal[10] == 65535){
				return "--  "+I18N.COMMON.dBm;
			}else{
				return (Math.round((10 * (Math.log(opticalVal[10]) / Math.LN10) - 40) * 10000) / 10000) + "  "+I18N.COMMON.dBm;
			}
		case 11: return opticalVal[11] +"  Mb/s";
		}
	}
	return I18N.Optical.couldntGetData;
}

function cancelClick(){
	window.parent.closeWindow('oltOpticalInfo');
}
var tipNeedToHide = 0;
function refreshClick(el){
	tipNeedToHide++;
	$(el).attr("disabled", true).mouseout();
	$("#tipFont").text(I18N.Optical.fetching).css("color", "blue").show();
	var url = (portType ? '/epon/optical/loadOltPonOptical.tv?r=' : '/epon/optical/loadOltSniOptical.tv?r=') + Math.random();
	var par = {entityId: entityId, portIndex: portIndex};
	window.top.showWaitingDlg('@COMMON.waiting@', '@COMMON.refreshing@', 'ext-mb-waiting');
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			top.afterSaveOrDelete({
	    		title: '@COMMON.tip@',
	    		showTime: 2000,
	    		html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
	    	});
			$(el).attr("disabled", false);
			if(response.responseText == 'false'){
				$("#tipFont").text(I18N.Optical.fetchFailed).css("color", "red").show();
				return;
			}
			$("#tipFont").text(I18N.Optical.fetchSuc).css("color", "green").show();
			opticalVal = Ext.util.JSON.decode(response.responseText);
			var data = [null, null, null, null, null, null];
			if(opticalVal.data.length > 0){
				opticalVal = opticalVal.data;
				data = opticalVal.slice(0);
			}else{
				opticalVal = [];
			}
			initData();
			//修改父页面
			if(window.parent.getFrame("entity-" + entityId) != null){
				var pa = window.parent.getFrame("entity-" + entityId);
				var opticalLength = pa.oltPortOptical.length;
				if(opticalLength > 0){
					for(var a=0; a<opticalLength; a++){
						if(pa.oltPortOptical[a].portIndex == portIndex){
							pa.oltPortOptical[a] = {portIndex: portIndex, identifier: data[0], vendorName: data[1], waveLength: data[2],
									vendorPN: data[3], vendorSN: data[4], dateCode: data[5]};
							var tmpSource = pa.grid.getSource();
							tmpSource[I18N.Optical.identifier] = data[0] && parseInt(data[0]) > 0 
												&& parseInt(data[0]) < 4 ? fiberType[data[0]] : null;
							tmpSource[I18N.Optical.vendorName] = data[1] ? data[1] : null;
							tmpSource[I18N.Optical.waveLength] = data[2] && data[2] != 0 ? data[2] + "  nm" : null;
							tmpSource[I18N.Optical.vendorPN] = data[3] ? data[3] : null;
							tmpSource[I18N.Optical.vendorSN] = data[4] ? data[4] : null;
							tmpSource[I18N.Optical.dateCode] = data[5] ? data[5] : null;
							pa.grid.setSource(tmpSource);
							break;
						}
					}
				}
			}
			var tmpHideFlag = ++tipNeedToHide;
			setTimeout(function(){
				if(tmpHideFlag == tipNeedToHide){
					$("#tipFont").fadeOut(1000);
				}
			}, 5000);
		},
		failure : function() {
			$("#tipFont").text(I18N.Optical.fetchFailed).css("color", "red").show();
			$(el).attr("disabled", false);
		},
		params : par
	});
}

function authLoad(){
    if(!refreshDevicePower){
        R.fetchData.setDisabled(true)
    }
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr height=240>
						<td>
							<table align=center>
								<tr>
									<td><b>@Optical.attr@</b></td>
									<td></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont1></font></td>
									<td><font id=attr1></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont2></font></td>
									<td><font id=attr2></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont3></font></td>
									<td><font id=attr3></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont4></font></td>
									<td><font id=attr4></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont5></font></td>
									<td><font id=attr5></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont6></font></td>
									<td><font id=attr6></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=attrFont7></font></td>
									<td><font id=attr7></font></td>
								</tr>
							</table>
						</td>
						<td>
							<table>
								<tr>
									<td><b>@Optical.statInfo@</b></td>
									<td></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=statFont1></font></td>
									<td><font id=stat1></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=statFont2></font></td>
									<td><font id=stat2></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=statFont3></font></td>
									<td><font id=stat3></font></td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><font id=statFont4></font></td>
									<td><font id=stat4></font></td>
								</tr>
								<tr id = "portOptical">
									<td class="rightBlueTxt"><font id=statFont5></font></td>
									<td><font id=stat5></font></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="fetchData" onClick="refreshClick(this)" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoWrong">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>