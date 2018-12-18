<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css">
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import /js/json2
    import js.jquery.Nm3kTabBtn
    css css.white.disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var cmcId = '${cmcId}';
vcEntityKey = 'cmcId';
</script>
</head>
<body onload="authLoad()">
	<div id="header">
		<%@ include file="/cmc/entity.inc"%>
	</div>
	<div class="edge10">
		<div id="putTab"></div>
		<div class="clearBoth">
			<div class="jsShow pT10">
				<table class="dataTableRows zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
				     <thead>
				         <tr>
				             <th colspan="6" class="txtLeftTh">@CMC.optical.input@</th>
				         </tr>
				     </thead>
				     <tbody>
				         <tr>
				            <td width="120" class="rightBlueTxt">@CMC.optical.receiverPower@A@COMMON.maohao@</td>
				            <td><div id="inputPower1" class="w200 wordBreak"></div></td>
				            <td width="120" class="rightBlueTxt">@CMC.optical.receiverPower@B@COMMON.maohao@</td>
				            <td><div id="inputPower2" class="w200 wordBreak"></div></td>
				            <td width="120" class="rightBlueTxt">@CMC.optical.switchControl@@COMMON.maohao@</td>
				            <td><div id="switchControl" class="w200 wordBreak"></div></td>
				         </tr>
				         <tr>
				         	<td class="rightBlueTxt">@CMC.optical.changeThreshold@@COMMON.maohao@</td>
				         	<td><div id="switchThres" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt"></td>
				         	<td></td>
				         	<td class="rightBlueTxt"></td>
				         	<td></td>
				         </tr>
					</tbody>
				</table>
			</div>
			<div class="jsShow pT10">
				<table class="dataTableRows zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
				     <thead>
				         <tr>
				             <th colspan="6" class="txtLeftTh">@CMC.optical.power@</th>
				         </tr>
				     </thead>
				     <tbody>
				         <tr>
				         	<td width="120" class="rightBlueTxt">@CMC.optical.dcPower@(5V)@COMMON.maohao@</td>
				         	<td><div id="dcPower1" class="w200 wordBreak"></div></td>
				         	<td width="120" class="rightBlueTxt">@CMC.optical.dcPower@(12V)@COMMON.maohao@</td>
				         	<td><div id="dcPower2" class="w200 wordBreak"></div></td>
				         	<td width="120" class="rightBlueTxt">@CMC.optical.dcPower@(24V)@COMMON.maohao@</td>
				         	<td><div id="dcPower3" class="w200 wordBreak"></div></td>
				         </tr>
				         <tr>
				         	<td class="rightBlueTxt">@CMC.optical.linePower@@COMMON.maohao@</td>
				         	<td><div id="acPowerVoltage" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt"></td>
				         	<td></td>
				         	<td class="rightBlueTxt"></td>
				         	<td></td>
				         </tr>
					</tbody>
				</table>
			</div>
			<div class="jsShow pT10">
				<table class="dataTableRows zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
				     <thead>
				         <tr>
				             <th colspan="6" class="txtLeftTh">@CMC.optical.output@</th>
				         </tr>
				     </thead>
				     <tbody>
				         <tr>
				         	<td width="120" class="rightBlueTxt">@CMC.optical.linkSwitch@@COMMON.maohao@</td>
				         	<td><div id="outputControl" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt">@CMC.optical.gainType@@COMMON.maohao@</td>
				         	<td><div id="outputGainType" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt">@CMC.optical.agcOrigin@@COMMON.maohao@</td>
				         	<td><div id="outputAGCOrigin" class="w200 wordBreak"></div></td>
				         	<!-- <td width="120" class="rightBlueTxt">@CMC.optical.rfOutput@1@COMMON.maohao@</td>
				         	<td><div id="rfPort1" class="w200 wordBreak"></div></td>
				         	<td width="120" class="rightBlueTxt">@CMC.optical.rfOutput@2@COMMON.maohao@</td>
				         	<td><div id="rfPort2" class="w200 wordBreak"></div></td> -->
				         </tr>
				         <tr>
				         	<td class="rightBlueTxt">@CMC.optical.levelAttenuation@@COMMON.maohao@</td>
				         	<td><div id="outputRFlevelatt" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt">@CMC.optical.channelNum@@COMMON.maohao@</td>
				         	<td><div id="channelNum" class="w200 wordBreak"></div></td>
				         	<!-- <td width="120" class="rightBlueTxt">@CMC.optical.rfOutput@3@COMMON.maohao@</td>
				         	<td><div id="rfPort3" class="w200 wordBreak"></div></td>
				         	<td class="rightBlueTxt">@CMC.optical.rfOutput@4@COMMON.maohao@</td>
				         	<td><div id="rfPort4" class="w200 wordBreak"></div></td> -->
				         </tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="noWidthCenterOuter clearBoth">
			<ol class="noWidthCenterOl pB0 pT10 noWidthCenter">	
				<li>
					<a href="javascript:;" class="normalBtnBig" id="refreshBtn" onclick="loadCmcOpReceiverInfo()"><span><i class="miniIcoSaveOK miniIcoRefresh"></i>@CMC.optical.refresh@</span></a>
				</li>
				<!-- <li>
					<a id="modifyConfig" href="javascript:;" class="normalBtnBig" onclick="showCmcOpReceiver()"><span><i class="miniIcoSaveOK miniIcoEdit"></i>@CMC.optical.modify@</span></a>
				</li> -->
			</ol>
		</div>
	</div>
	
	<script type="text/javascript">
		var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
		var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
		var dorTypes = {
			0: '@CMC.optical.noOptical@',
			1: '@CMC.optical.typea@',
			2: '@CMC.optical.typeb@'
		}
		
		$(function(){
			var tab1 = new Nm3kTabBtn({
			    renderTo:"putTab",
			    callBack:"showTab",
			    tabs:["@COMMON.all@","@CMC.optical.input@","@CMC.optical.power@","@CMC.optical.output@"]
			});
			tab1.init();
			$("#putTab").append('<div style="float:left; padding:4px 0px 0px 20px;">'
				+ '<span class="blueTxt">@CMC.optical.type@:</span><span id="dorType" class="w100" style="display:inline-block;"></span>'
				+ '<span class="blueTxt">@CMC.optical.lastCollect@:</span><span id="opticalCollecttime"></span></div>')
		
			loadCmcOpReceiverInfo();
		});//end document.ready;
		
		function loadBbData(){
			$.get('/cmc/optical/loadOpticalReceiverStatus.tv', {cmcId: cmcId}, function(data){
				outputData(data);
			});
		}
		
		function loadCmcOpReceiverInfo(){
			top.showWaitingDlg('@COMMON.waiting@', '@CMC.refreshOpticalReciver@', 'ext-mb-waiting', null, true);
			$.get('/cmc/optical/refreshOpticalReceiverInfo.tv', {cmcId: cmcId}, function(data){
				window.top.closeWaitingDlg();
				if(data.result !== "success") return;
				//读取回来的光机数据
				outputData(data.receiverStatus);
			});
		}
		
		function outputData(receiverStatus){
			var inputInfoList = receiverStatus.inputInfoList,
				switchCfg = receiverStatus.switchCfg;
			if(!receiverStatus.dorType||!operationDevicePower){
				$('#modifyConfig').attr('disabled', 'disabled');
			}else{
				$('#modifyConfig').removeAttr('disabled');
			}
			$('#dorType').text(receiverStatus.dorType || '@CMC.optical.noOptical@');
			//输入数据
			if(inputInfoList && inputInfoList[0]){
	        	if(inputInfoList[0].collectTimeStr){
	        		$('#opticalCollecttime').text(inputInfoList[0].collectTimeStr);
	        	}
	            for(var i = 0; i < inputInfoList.length; i++){
	                var power = inputInfoList[i].inputPower;
	                var htmlTxt = '<a class="yellowLink" href="javascript:;" onclick="showCmcOpticalReveiverHis(' + 
	                inputInfoList[i].inputIndex + ')" >' + power / 10 + 'dBm</a>';
	                $("#inputPower" + (i+1)).html(htmlTxt);
	            }
	        }
			if(switchCfg){
	            var switchState = switchCfg.switchState;
	            if(switchState == 1){
	                $("#inputPower1 a").text($("#inputPower1 a").text() + "(@CMC.ipqam.inUse@)");
	            }else if(switchState == 2){
	                $("#inputPower2 a").text($("#inputPower2 a").text() + "(@CMC.ipqam.inUse@)");
	            }
	            var switchControl = switchCfg.switchControl;
	            var SWITCH_CONTROL = ["-", "@CMC.optical.forcePath@A", "@CMC.optical.forcePath@B", 
	                    "@CMC.optical.preferPath@A", "@CMC.optical.preferPath@B", "@CMC.optical.noPrefer@"];
	            switchControl = SWITCH_CONTROL[switchControl];
	            $("#switchControl").text(switchControl);
	            $("#switchThres").text(switchCfg.switchThres + "dBm");
	        }
			if(receiverStatus.rfCfg){
	            var RF_GAIN_TYPE = ["-", "@CMC.optical.constantLevel@", "@CMC.optical.constantGain@"];
	            var outputGainType = receiverStatus.rfCfg.outputGainType;
	            if(outputGainType && outputGainType > 0 && outputGainType <= 2){
	                outputGainType = RF_GAIN_TYPE[outputGainType];
	            }else{
	                outputGainType = "-";
	            }
	            $('#outputControl').text(receiverStatus.rfCfg.outputControl==1?'@CMC.optical.close@':'@CMC.optical.open@');
	            $("#outputGainType").text(outputGainType);
	            var outputAGCOrigin = receiverStatus.rfCfg.outputAGCOrigin;
	            $("#outputAGCOrigin").text(outputAGCOrigin + "dBm");
	            var outputRFlevelatt = receiverStatus.rfCfg.outputRFlevelatt;
	            $("#outputRFlevelatt").text(outputRFlevelatt + "dB");
	        }
			if(receiverStatus.acPowerVoltage){
				$("#acPowerVoltage").text((receiverStatus.acPowerVoltage/10).toFixed(1) + "V");
			}else{
				$("#acPowerVoltage").text("");
			}
	        if(receiverStatus.dcPower){
	            for(var i = 0; i < receiverStatus.dcPower.length; i++){
	                var powerVt = receiverStatus.dcPower[i].powerVoltage;
	                var powerTxt = powerVt == -1 ? "-" : powerVt/10 + "V";
	                $("#dcPower" + (i+1)).text(powerTxt);
	            }
	        }
	        var channelNum = receiverStatus.channelNum;
	        if(channelNum == -1){
	            channelNum = "-";
	        }
	        $("#channelNum").text(channelNum);
	        var rfPort = receiverStatus.rfPort;
	        var rfPortLevel;
	        if(rfPort){
	            for(var i = 0; i < rfPort.length; i ++){
	                rfPortLevel = rfPort[i].rfOutputLevel;
	                if(rfPortLevel == -1){
	                    rfPortLevel = "-";
	                }else{
	                    rfPortLevel += "@unit.dbuv@";
	                }
	                $("#rfPort" + (i+1)).text(rfPortLevel);
	            }　
	        }
		}
		
		function showCmcOpticalReveiverHis(index){
		    top.addView("historyShow-"+cmcId + "-noise", '@network/NETWORK.hisPerf@', "historyIcon", 
		            "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=opticalReceiver&index=" + index); 
		}
		
		function showCmcOpReceiver(){
		    window.top.createDialog('cmcOpReceiverCfg', '@CMC.optical.opticalConfig@', 600, 370, 
		            '/cmc/optical/showOpticalReceiverCfgView.tv?entityId=' + cmcId + "&cmcId=" + cmcId, null, true, true);
		}
		
		function showTab(index){
			if(index == 0){
				$("div.jsShow").css({display:"block"});
			}else{
		    	$("div.jsShow").css({display:"none"});
		    	$("div.jsShow").eq(index-1).fadeIn();
			}
		}
		
		function authLoad(){
		    if(!refreshDevicePower){
		        $("#refreshBtn").attr("disabled",true);
		    }
		}
	</script>
</body>
</Zeta:HTML> 
