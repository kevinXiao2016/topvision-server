<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
    module epon
    IMPORT js/jquery/Nm3kTabBtn
    import js/customColumnModel
    import epon.ofa.ofaAlarmThreshold
    import epon.ofa.ofaBasicInfo
    css css/white/disabledStyle
</Zeta:Loader>
<script>
var entityId = '${entityId}';
var ofaAlarmThresholdJson = '${ofaAlarmThresholdJson}';//告警阈值
var commonOptiTransJson = '${commonOptiTransJson}';
var ofaBasicInfoJson = '${ofaBasicInfoJson}';
var tempUnit = '${tempUnit}';
var oldOutputAtt;
var ranges={},alarmThreshold={},ofaAlarmThreshold={},oldOfaAlarmThreshold={},ofaBasicInfo={},commonOptiTrans={};

$(function(){
	//初始化切换tab签
	initTabs();
	//初始显示基本信息tab
	initBasicInfo();
	//初始化ofa告警阈值
	initAlarmThreshold(ofaAlarmThresholdJson);
})


/********************************************/
/*---------------tab start-------------*/
/**
 * 初始化tab
 */
function initTabs(){
	(new Nm3kTabBtn({
		renderTo:"putTab",
		callBack:"switchTab",
		tabs:['@OFA.basicInfo@', '@OFA.thresholdConfig@']
	})).init();
}

/**
 * 切换tab
 * @param {Integer} index tab在tab签中的index
 */
function switchTab (index) {
	$(".tabBody").css("display","none");
	$(".tabBody").eq(index).fadeIn();
}
/*---------------tab end-------------*/
/********************************************/
</script>
</head>
	<body class="newBody">
		<div id="header-container">
			<div id="header">
				<%@ include file="/epon/inc/navigator.inc"%>
			</div>
		
			<div id="putTab" class="edge10">
			</div>
		</div>
		
		<div class="edge10 pB0 clearBoth tabBody" id = "basicInfo" >
		  <div  style="pading-top: 15px;">
			<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="6" class="txtLeftTh">@OFA.basicInfo@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt w200">@OFA.platSN@@COMMON.maohao@</td>
					 	<td>
					 		<input id="platSN" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.HWVer@@COMMON.maohao@</td>
					 	<td >
					 		<input id="hWVer" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
						<td class="rightBlueTxt w200">@OFA.SWVer@@COMMON.maohao@</td>
					 	<td>
					 		<input id="sWVer" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 </tr>
					 <tr>
					 	<td class="rightBlueTxt w200">@OFA.moduleType@@COMMON.maohao@</td>
					 	<td >
					 		<input id="moduleType" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.deviceType@@COMMON.maohao@</td>
					 	<td >
					 		<input id="deviceType" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.deviceName@@COMMON.maohao@</td>
					 	<td >
					 		<input id="deviceName" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.vendorName@@COMMON.maohao@</td>
					 	<td>
					 		<input id="vendorName" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.modelNumber@@COMMON.maohao@</td>
					 	<td >
					 		<input id="modelNumber" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.serialNumber@@COMMON.maohao@</td>
					 	<td >
					 		<input id="serialNumber" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.ipAddress@@COMMON.maohao@</td>
					 	<td >
					 		<input id="ipAddress" disabled="disabled" type="text" class="normalInput w150"/>
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.macAddress@@COMMON.maohao@</td>
					 	<td >
					 		<input id="macAddress" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.deviceAcct@@COMMON.maohao@</td>
					 	<td >
					 		<input id="deviceAcctStr" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.deviceMFD@@COMMON.maohao@</td>
					 	<td colspan="5">
					 		<input id="deviceMFD" disabled="disabled" type="text" class="normalInput w150" />
					 	</td>
					 </tr>
				   </tbody>
				</table>
			  </div>
			  <div  style="pading-top: 15px; margin-top:15px;">
				<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					 <thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@OFA.OpticalPowerInformation@</th>
						</tr>
					</thead>
				   <tbody>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.inputPower@@COMMON.maohao@</td>
					 	<td>
					 		<input id="inputPower" disabled="disabled" type="text" class="normalInput w150" />
					 		dBm
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.outputPower@@COMMON.maohao@</td>
					 	<td >
					 		<input id="outputPower" disabled="disabled" type="text" class="normalInput w150" />
					 		dBm
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.outputAtt@@COMMON.maohao@</td>
					 	<td>
					 		<input id="outputAtt" type="text" class="normalInput w150" tooltip='@OFA.tooltipOutputAtt@'/>
					 		dB
					 	</td>
					 </tr>
				   </tbody>
				</table>
			  </div>
			  <div  style="pading-top: 15px; margin-top:15px;" >
				<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					 <thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@OFA.topOFAOptiPump@</th>
						</tr>
					</thead>
				   <tbody>
					<tr>
						<td class="rightBlueTxt w200">@OFA.pump1BiasCurr@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump1BiasCurr" disabled="disabled" type="text" class="normalInput w150" />
					 		mA
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump1Temp@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump1Temp" disabled="disabled" type="text" class="normalInput w150" />
					 		<font class = "tempUnit"></font>
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump1Tec@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump1Tec" disabled="disabled" type="text" class="normalInput w150" />
					 		mA
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump2BiasCurr@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump2BiasCurr" disabled="disabled" type="text" class="normalInput w150" />
					 		mA
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump2Temp@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump2Temp" disabled="disabled" type="text" class="normalInput w150" />
					 		<font class = "tempUnit"></font>
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump2Tec@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump2Tec" disabled="disabled" type="text" class="normalInput w150" />
					 		mA
					 	</td>
					 </tr>
				   </tbody>
				</table> 
			  </div>
			  <div style="pading-top: 25px; margin-top:15px;">
				<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					 <thead>
						<tr>
							<th colspan="6" class="txtLeftTh">@OFA.PowerTemperatureInformation@</th>
						</tr>
					</thead>
				   <tbody>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.voltage5v@@COMMON.maohao@</td>
					 	<td>
					 		<input id="voltage5v" disabled="disabled" type="text" class="normalInput w150" />
					 		V
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.voltage12v@@COMMON.maohao@</td>
					 	<td >
					 		<input id="voltage12v" disabled="disabled" type="text" class="normalInput w150" />
					 		V
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.systemTemp@@COMMON.maohao@</td>
					 	<td >
					 		<input id="systemTemp" disabled="disabled" type="text" class="normalInput w150" />
					 		<font class = "tempUnit"></font>
					 	</td>
					 </tr>
				</tbody>
			</table>
		  </div>
		  <Zeta:ButtonGroup>
			<Zeta:Button id="refreshBasicInfoBt" onClick="refreshBasicInfoConfig()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="modifyBasicInfoBt" onClick="updateBasicInfoConfig()" icon="miniIcoEdit">@config.page.update@</Zeta:Button>
		  </Zeta:ButtonGroup>
		</div>
		
		
		<div class="edge10 pB0 clearBoth tabBody" id="thresholdConfig" style="pading-top: 15px;">
			<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="4" class="txtLeftTh">@OFA.ofaThresholdConfig@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="rightBlueTxt w200">@OFA.inputAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="inputAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipInputAlarmUp@'/>
					 		@OFA.opticalUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.inputAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="inputAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipInputAlarmLow@'/>
					 		@OFA.opticalUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.outputAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="outputAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipOutputAlarmUp@'/>
					 		@OFA.opticalUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.outputAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="outputAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipOutputAlarmLow@'/>
					 		@OFA.opticalUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump1BiasAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump1BiasAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1BiasAlarmUp@'/>
					 		@OFA.currentUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump1BiasAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump1BiasAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1BiasAlarmLow@'/>
					 		@OFA.currentUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump1TempAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump1TempAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1TempAlarmUp@'/>
					 		<font class="tempUnit"></font>
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump1TempAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump1TempAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1TempAlarmLow@'/>
					 		<font class="tempUnit"></font>
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump1TecAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump1TecAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1TecAlarmUp@'/>
					 		@OFA.currentUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump1TecAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump1TecAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump1TecAlarmLow@'/>
					 		@OFA.currentUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump2BiasAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump2BiasAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump2BiasAlarmUp@'/>
					 		@OFA.currentUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump2BiasAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump2BiasAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump2BiasAlarmLow@'/>
					 		@OFA.currentUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.pump2TempAlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="pump2TempAlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump2TempAlarmUp@'/>
					 		<font class="tempUnit"></font>
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.pump2TempAlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="pump2TempAlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipPump2TempAlarmLow@'/>
					 		<font class="tempUnit"></font>
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.voltage5AlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="voltage5AlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipVoltage5AlarmUp@'/>
					 		@OFA.voltageUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.voltage5AlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="voltage5AlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipVoltage5AlarmLow@'/>
					 		@OFA.voltageUnit@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt w200">@OFA.voltage12AlarmUp@@COMMON.maohao@</td>
					 	<td>
					 		<input id="voltage12AlarmUp" type="text" class="normalInput w100" tooltip='@OFA.tooltipVoltage12AlarmUp@'/>
					 		@OFA.voltageUnit@
					 	</td>
					 	<td class="rightBlueTxt w200">@OFA.voltage12AlarmLow@@COMMON.maohao@</td>
					 	<td >
					 		<input id="voltage12AlarmLow" type="text" class="normalInput w100" tooltip='@OFA.tooltipVoltage12AlarmLow@'/>
					 		@OFA.voltageUnit@
					 	</td>
					 </tr>
					<!--  <tr>
					 	<td colspan="4">
					 		<div class="noWidthCenterOuter clearBoth">
							   
							</div>
					 	</td>
					 </tr> -->
				</tbody>
			</table>
			<Zeta:ButtonGroup>
				<Zeta:Button id="refreshBt" onClick="refreshConfig()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
				<Zeta:Button id="modifyBt" onClick="updateConfig()" icon="miniIcoEdit">@config.page.update@</Zeta:Button>
			</Zeta:ButtonGroup>
		</div>
	</body>
</Zeta:HTML>