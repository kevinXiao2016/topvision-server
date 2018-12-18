<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module onu
    IMPORT gpon/customGponOnu
</Zeta:Loader>
<style type="text/css">
.winMainBody{ height: 320px; overflow:auto;}
.floatLabel{ float:left; padding:2px 10px 2px 0px;}
</style>
<script type="text/javascript">
	var gponOnuCapability = '${gponOnuCapability}';
	var onuCapabilityDeviceIndex = formatBlank('${gponOnuCapability.onuCapabilityDeviceIndex}');
	var onuOMCCVersion = formatBlank('${gponOnuCapability.onuOMCCVersion}');
	var onuTotalEthNum = formatBlank('${gponOnuCapability.onuTotalEthNum}');
	var onuTotalWlanNum = formatBlank('${gponOnuCapability.onuTotalWlanNum}');
	var onuTotalCatvNum = formatBlank('${gponOnuCapability.onuTotalCatvNum}');
	var onuTotalVeipNum = formatBlank('${gponOnuCapability.onuTotalVeipNum}');
	var onuIpHostNum = formatBlank('${gponOnuCapability.onuIpHostNum}');
	var onuTrafficMgmtOption = formatBlank('${gponOnuCapability.onuTrafficMgmtOption}');
	var onuTotalGEMPortNum = formatBlank('${gponOnuCapability.onuTotalGEMPortNum}');
	var onuTotalTContNum = formatBlank('${gponOnuCapability.onuTotalTContNum}');
	var onuTotalTPotsNum = formatBlank('${gponOnuCapability.onuTotalPotsNum}');
	//var onuConnectCapbility = formatBlank('${gponOnuCapability.onuConnectCapbility}');
	//var onuQosFlexibility = formatBlank('${gponOnuCapability.onuQosFlexibility}');
	
	
	
	$(function(){
		$("#putOnuOMCCVersion").html(onuOMCCVersion);
		$("#putOnuTotalEthNum").html(onuTotalEthNum);
		$("#putOnuTotalWlanNum").html(onuTotalWlanNum);
		$("#putOnuTotalCatvNum").html(onuTotalCatvNum);
		$("#putOnuTotalVeipNum").html(onuTotalVeipNum);
		$("#putOnuIpHostNum").html(onuIpHostNum);
		$("#putOnuTrafficMgmtOption").html(renderTrafficOption(onuTrafficMgmtOption));
		$("#putOnuTotalGEMPortNum").html(onuTotalGEMPortNum);
		$("#putOnuTotalTContNum").html(onuTotalTContNum);
		$("#putOnuTotalPotsNum").html(onuTotalTPotsNum);
		$("#putOnuConnectCapbility").html( GPON_ONU.renderConnectCapbility('${gponOnuCapability.onuConnectCapbility}') );
		$("#putOnuQosFlexibility").html( GPON_ONU.renderQosFlexibility('${gponOnuCapability.onuQosFlexibility}') );
	});//end document.ready;
	function formatBlank(str){
		if(str === ''){
			return '--'
		}else{
			return str;
		}
	}
    function cancelClick(){
    	top.closeWindow("modalDlg");
    }
    function renderTrafficOption(num){
		var option = parseInt(num, 10);
		switch(option){
			case 0:
				return "@gpon/GPON.onuTrafficMgmtOpt0@";
			case 1:
				return "@gpon/GPON.onuTrafficMgmtOpt1@";
			case 2:
				return "@gpon/GPON.onuTrafficMgmtOpt2@";
			default:return "";
		}
	}
</script>
</head>
    <body class="openWinBody">
		<div class="edgeTB10LR20 pT20">
			<div class="winMainBody">
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="160">@ONU.omcc@@COMMON.maohao@</td>
			                <td width="140" id="putOnuOMCCVersion">@resources/WorkBench.loading@</td>
			                <td class="rightBlueTxt" width="160">@gpon/GPON.EthernetNumber@</td>
			                <td id="putOnuTotalEthNum">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">@ONU.wifiNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalWlanNum">@resources/WorkBench.loading@</td>
			                <td class="rightBlueTxt">@ONU.catvPortNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalCatvNum">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr>
			                <td class="rightBlueTxt">@ONU.veipPortNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalVeipNum">@resources/WorkBench.loading@</td>
			                <td class="rightBlueTxt">@ONU.iphostNum@@COMMON.maohao@</td>
			                <td id="putOnuIpHostNum">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">@ONU.manager@@COMMON.maohao@</td>
			                <td id="putOnuTrafficMgmtOption">@resources/WorkBench.loading@</td>
			                <td class="rightBlueTxt">@ONU.gemPortNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalGEMPortNum">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr>
			           		<td class="rightBlueTxt">@ONU.potsNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalPotsNum">@resources/WorkBench.loading@</td>
			                <td class="rightBlueTxt">@ONU.tcontNum@@COMMON.maohao@</td>
			                <td id="putOnuTotalTContNum">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">@ONU.conAbility@@COMMON.maohao@</td>
			                <td id="putOnuConnectCapbility" colspan="3">@resources/WorkBench.loading@</td>
			            </tr>
			            <tr>
			                <td class="rightBlueTxt">@ONU.qos@@COMMON.maohao@</td>
			                <td id="putOnuQosFlexibility" colspan="3">@resources/WorkBench.loading@</td>
			            </tr>
			        </tbody>
			    </table>
			</div>
		    <div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		            <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i>@COMMON.close@</span></a></li>
		        </ol>
		    </div>
		</div>
    </body>
</Zeta:HTML>