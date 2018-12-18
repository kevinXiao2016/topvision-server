<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    import /js/json2
    import js.jquery.Nm3kTabBtn
    import epon.onucatv.onuCatvConfig
    CSS css/white/disabledStyle
</Zeta:Loader>
<head>
<style type="text/css">
.scrollBtn{ cursor:pointer;}
#putCollectTime{ position:absolute; top:52px; right:10px;}
#putCollectTime.greenConner{ padding:4px 6px; background:#25ad8f; color:#fff; border-radius:4px; top:48px;}
.greenConner .blueTxt{color:#fff;}
.radioBox{ position:relative; top:3px; margin-right:3px;}
</style>
<script type="text/javascript">
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var onuId = '${onu.entityId}';
	var entityId = '${onu.parentId}';
	var testProjectSupport = ${testProjectSupport};
	var switchCATV;//CATV开关
	var onuIndex;
	var onuType="EPON";
</script>
</head>
<body class="whiteToBlack">
	<div id="header">
		<%@ include file="/epon/onu/navigator.inc"%>
	</div>
	<div class="edge10">
		<div id="putTab"></div>
		<div class="clearBoth">
			<div class="jsShow pT10">
				<table class="dataTableRows zebra noWrap jsTbody" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
				     <thead>
				         <tr>
				             <th colspan="6" class="txtLeftTh">@CATV.OutputGainControl@</th>
				         </tr>
				     </thead>
				     <tbody>
				         <tr>
			                <td class="rightBlueTxt" width="150">
								@CATV.OutputGainType@@COMMON.maohao@
			                </td>
			                <td colspan="5">
			                	<label>
									<input class="radioBox" type="radio" name="gainControlType" id="unset" value="0" />@CATV.UNSET@
								</label>
								<label class="pL20">
									<input class="radioBox" type="radio" name="gainControlType" id="agc" value="1" />@CATV.AGC@
								</label>
								<label class="pL20">
									<input class="radioBox" type="radio" name="gainControlType" id="mgc" value="2" />@CATV.MGC@	
								</label>
			                </td>
				         </tr>
				     </tbody>
				     <tbody style="display:none">
				         <tr>
				         	<td width="150" class="rightBlueTxt">@CATV.Inputopticalpowerlimit@@COMMON.maohao@</td>
				         	<td>
				         		<div id="switchThres" class="w180 wordBreak">
				         			<input type="text" style="width: 100px;" id="agcUpValue" class="normalInput" toolTip="@CATV.agcUpValueTip@" /><span class="pL5">dBm</span>
				         		</div>
				         	</td>
				         	<td width="150" class="rightBlueTxt">@CATV.Inputopticalpowerrange@@COMMON.maohao@</td>
				         	<td>
				         		<div id="switchThres" class="w180 wordBreak">
				         			<input type="text" style="width: 100px;" id="agcRange" class="normalInput" toolTip="@CATV.agcRange@" /><span class="pL5">dBm</span>
				         		</div>
				         	</td>
				         	<td width="150"></td>
				         	<td><div class="w180"></div></td>
				         </tr>
				     </tbody>
				     <tbody style="display:none">
				         <tr>
				         	<td width="150" class="rightBlueTxt">@CATV.outputpowerattenuation@@COMMON.maohao@</td>
				         	<td colspan="5">
				         		<div id="switchThres" class="w200 wordBreak">
				         			<input type="text" style="width: 100px;" id="mgcTxAttenuation" class="normalInput" toolTip="@CATV.mgcTxAttenuation@" /><span class="pL5">dB</span>
				         		</div>
				         	</td>
				         </tr>
					</tbody>
				</table>
			</div>
			<div class="jsShow pT10" id = "shoresholdConfig">
				<table class="dataTableRows zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
				     <thead>
				         <tr>
				             <th colspan="6" class="txtLeftTh">@CATV.Alarmthresholdconfig@</th>
				         </tr>
				     </thead>
				     <tbody>
				         <tr>
				         <td width="50"></td>
				         	<td width="180" class="rightBlueTxt wordBreak">@CATV.InputPower@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@CATV.InputPowerLowthreshold@@COMMON.maohao@</td>
				         	<td width="60">
				         		<div id="switchThres" class=" wordBreak">
				         			<input type="text" style="width: 50px;" id="inputLO" class="normalInput" toolTip="@CATV.inputTip@" />
				         		</div>
				         	</td>
				         	<td width="80" class="rightBlueTxt wordBreak">@CATV.InputPowerHighthreshold@@COMMON.maohao@</td>
				         	<td>
				         		<div id="" class="w180 wordBreak">
				         			<input type="text" style="width: 50px;" id="inputHI" class="normalInput" toolTip="@CATV.inputTip@" />
				         			<span class="pL5">@CATV.Unit@(dBm)</span>
				         		</div>
				         	</td>
				         	
				         </tr>
				         <tr>
				         <td width="50"></td>
				         <td class="rightBlueTxt wordBreak">@CATV.OutputPower@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@CATV.InputPowerLowthreshold@@COMMON.maohao@</td>
                            <td>
                                <div id="switchThres" class=" wordBreak">
                                    <input type="text" style="width: 50px;" id="outputLO" class="normalInput" toolTip="@CATV.outputTip@" />
                                </div>
                            </td>
                            <td class="rightBlueTxt wordBreak">@CATV.InputPowerHighthreshold@@COMMON.maohao@</td>
                            <td>
                                <div id="switchThres" class="w180 wordBreak">
                                    <input type="text" style="width: 50px;" id="outputHI" class="normalInput" toolTip="@CATV.outputTip@" />
                                    <span class="pL5">@CATV.Unit@(dBμV)</span>
                                </div>
                            </td>
				         </tr>
				         <tr>	
				         <td width="50"></td>			         	
				         	<td class="rightBlueTxt wordBreak">@CATV.Voltage@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@CATV.InputPowerLowthreshold@@COMMON.maohao@</td>
				         	<td>
				         		<div id="switchThres" class=" wordBreak">
				         			<input type="text" style="width: 50px;" id="voltageLO" class="normalInput" toolTip="@CATV.voltageTip@" />
				         			<span class="pL5"></span>
				         		</div>
				         	</td>
				         	<td class="rightBlueTxt wordBreak">@CATV.InputPowerHighthreshold@@COMMON.maohao@</td>
				         	<td>
				         		<div id="switchThres" class=" wordBreak">
				         			<input type="text" style="width: 50px;" id="voltageHI" class="normalInput" toolTip="@CATV.voltageTip@" />
				         			<span class="pL5">@CATV.Unit@(V)</span>
				         		</div>
				         	</td>
				         </tr>
				         <tr>
				         <td width="50"></td>
				         	<td class="rightBlueTxt wordBreak">@CATV.Temperature@&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;@CATV.InputPowerLowthreshold@@COMMON.maohao@</td>
				         	<td>
				         		<div id="switchThres" class=" wordBreak">
				         			<input type="text" style="width: 50px;" id="temperatureLO" class="normalInput" toolTip="@CATV.temperatureTip@" />
				         			<span class="pL5"></span>
				         		</div>
				         	</td>
				         	<td class="rightBlueTxt wordBreak">@CATV.InputPowerHighthreshold@@COMMON.maohao@</td>
				         	<td >
				         		<div id="switchThres" class="w180 wordBreak">
				         			<input type="text" style="width: 50px;" id="temperatureHI" class="normalInput" toolTip="@CATV.temperatureTip@" />
				         			<span class="pL5">@CATV.Unit@(℃)</span>
				         		</div>
				         	</td>
				         </tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="noWidthCenterOuter clearBoth">
			<ol class="noWidthCenterOl pB0 pT10 noWidthCenter">	
				<li>
					<a href="javascript:;" class="normalBtnBig" id="refreshBtn" onclick="refreshOnuCatvConfig()">
					<span><i class="miniIcoSaveOK miniIcoRefresh"></i>@CATV.RefreshDatafromdevice@</span></a>
				</li>
				<li>
					<a href="javascript:;" class="normalBtnBig" id="modifyConfig" onclick="saveConfig()">
					<span><i class="miniIcoSaveOK miniIcoEdit"></i>@CATV.Modifyconfig@</span></a>
				</li>
			</ol>
		</div>
	</div>
	<div id="putCollectTime" class="greenConner">
		<span class="blueTxt">@CATV.lastCollecttime@@COMMON.maohao@</span>
		<span id="collectTime"></span>
	</div>
</body>
</Zeta:HTML> 
