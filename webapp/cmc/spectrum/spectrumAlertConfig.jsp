<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title></title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
    library Ext
    library Zeta
	module spectrum
	import cmc.spectrum.spectrumAlertConfig
</Zeta:Loader>
<style type="text/css">

</style>
<script type="text/javascript">
var overThresholdTimes = ${overThresholdTimes}; //累计超过阈值次数Y，默认为4
var notOverThresholdTimes = ${notOverThresholdTimes}; // 累计不超过阈值次数Z，默认为4
var overThresholdPercent = ${overThresholdPercent}; // 超过阈值N%个点的N，默认为20%
var notOverThresholdPercent = ${notOverThresholdPercent};  // 未超过阈值M%个点的N，默认为10%
</script>
</head>
<body class="whiteToBlack">
	<div class="edge10">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
					<tr>
						<th colspan="4" class="txtLeftTh">@spectrum.alertConfig@</th>
					</tr>
			</thead>
			<tbody>
					<tr>
						<td class="rightBlueTxt w200">@spectrum.overTime@@COMMON.maohao@</td>
					 	<td>
					 		<input id=overThresholdTimes type="text" class="normalInput w100" tooltip='@spectrum.tooltip1@'/>
					 		@UNIT.times@
					 	</td>
					 	<td class="rightBlueTxt w200">@spectrum.notOverTime@@COMMON.maohao@</td>
					 	<td >
					 		<input id="notOverThresholdTimes" type="text" class="normalInput w100" tooltip='@spectrum.tooltip1@'/>
					 		@UNIT.times@
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt ">@spectrum.overN@@COMMON.maohao@</td>
					 	<td >
					 		<input id=overThresholdPercent type="text" class="normalInput w100" tooltip='@spectrum.tooltip2@'/>
					 		%
					 	</td>
					 	<td class="rightBlueTxt ">@spectrum.overM@@COMMON.maohao@</td>
					 	<td >
					 		<input id="notOverThresholdPercent" type="text" class="normalInput w100" tooltip='@spectrum.tooltip3@'/>
					 		%
					 	</td>
					 </tr>
					 <tr>
					 	<td colspan="4">
					 		<div class="noWidthCenterOuter clearBoth">
							     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
							         <li><a href="javascript:;" onclick="save()" class="normalBtn"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
							         <li><a href="javascript:;" onclick="reset()" class="normalBtn"><span><i class="miniIcoBack"></i>@COMMON.reset@</span></a></li>
							     </ol>
							</div>
					 	</td>
					 </tr>
			</tbody>
		</table>
	</div>
	<div class="edge10 pT0">
		<div class="yellowTip">
			@spectrum.subTip@
		</div>
	</div>
</body>
</Zeta:HTML>