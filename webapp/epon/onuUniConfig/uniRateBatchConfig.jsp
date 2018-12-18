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
    module epon
    css css/white/disabledStyle
    IMPORT epon.javascript.BatchDeployHelper
    IMPORT epon.onuUniConfig.uniRateBatchConfig
</Zeta:Loader>
<style type="text/css">
	#w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
	#w1600{ width:1600px; position:absolute; top:0; left:0;}
	#step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
	#step1{ left:800px;}
</style>
<script type="text/javascript">
	var entityId = ${entityId};
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
</script>
</head>
<body class="openWinBody">
	<div id="w800">
		<div id="w1600">
			<div id="step0">
				<div class="edge10">
					<div id="contentGrid"></div>
				</div>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" id="addBtn"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" id="closeBtn"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
				     </ol>
				</div>
			</div>
			<div id="step1">
				<div class="openWinHeader">
				    <div class="openWinTip">@ONU.uniRateLimit@</div>
				    <div class="rightCirIco wheelCirIco"></div>
				</div>
				<div class="edgeTB10LR20 pT30">
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				         	<tr>
				             	<td class="rightBlueTxt" style="padding-bottom:20px;">@UNI.templateName@:</td>
				             	<td colspan="3" style="padding-bottom:20px;">
				             		<input id="templateName" type="text" class="normalInput w200" maxlength="63" tooltip="@UNI.templateNameTip@" />
				             	</td>
				             </tr>
				         </tbody>
				         <tbody id="tbodyIn">
				             <tr class="darkZebraTr">
				                 <td colspan="4">
				                 	<ul class="leftFloatUl">
				                 		<li style="padding:0px 10px 0px 50px;">
				                 			<input type="checkbox" id="checkBoxIn" />
				                 		</li>
				                 		<li>
				                 			<b class="blueTxt">@SERVICE.inRateRestrictEn@</b>	
				                 		</li>
				                 	</ul>
				                 </td>
				             </tr>
				             <tr>
				                 <td class="rightBlueTxt" width="140">
									CIR:
				                 </td>
				                 <td>
									<input id="portInCIR" type="text" class="normalInput w100" maxlength="7" tooltip="@SERVICE.uniPortInCIRTip@" /> Kbps
				                 </td>
				                 <td class="rightBlueTxt" width="140">
									CBS:
				                 </td>
				                 <td>
									<input id="portInCBS" type="text" class="normalInput w100" maxlength="8"  tooltip="@SERVICE.uniPortInCBSTip@"/> KBytes
				                 </td>
				             </tr>
				             <tr>
				                 <td class="rightBlueTxt withoutBorderBottom">
									EBS:
				                 </td>
				                 <td class="withoutBorderBottom" colspan="3">
									<input id="portInEBS" type="text" class="normalInput w100" maxlength="8"  tooltip="@SERVICE.uniPortInEBSTip@"/> KBytes
				                 </td>
				             </tr>
				             <tr>
				                 <td colspan="4" style="height:10px;">
				
				                 </td>
				             </tr>
				         </tbody>
				         <tbody id="tbodyOut">
				             <tr class="darkZebraTr">
				                 <td colspan="4">
				                 	<ul class="leftFloatUl">
				                 		<li style="padding:0px 10px 0px 50px;">
				                 			<input type="checkbox" id="checkBoxOut"  />
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
									<input id="portOutPIR" type="text" class="normalInput w100" maxlength="7" tooltip="@SERVICE.rangePIR@" /> Kbps
				                 </td>
				                 <td class="rightBlueTxt">
									CIR:
				                 </td>
				                 <td>
									<input id="portOutCIR" type="text" class="normalInput w100" maxlength="7" tooltip="@SERVICE.rangeCIR@"/> Kbps
				                 </td>
				             </tr>
				         </tbody>
				     </table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
					         <li><a id="saveBtn" href="javascript:;" class="normalBtnBig" onclick="saveClick();"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
					         <li><a id="cancelBtn" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</Zeta:HTML>