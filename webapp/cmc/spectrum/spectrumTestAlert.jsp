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
	import cmc.spectrum.spectrumTestAlert
</Zeta:Loader>
<style type="text/css">
.td_1{
	width: 80px;
}
.td_2{
	width: 100px;
}
.td_3{
	width: 140px;
}
.tr{
	width: 500px;
}
</style>
<script type="text/javascript">
</script>
</head>
<body class="whiteToBlack">
	<div class="jsTabPart  clearBoth">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
					<tr>
						<th colspan="7" class="txtLeftTh">@spectrum.Noisesimulationtest@</th>
					</tr>
			</thead>
			<tbody>
					<tr class = "tr">
						<td class="rightBlueTxt td_1">@spectrum.fre@</td>
					 	<td class="td_1">
					 		<input id="freq" type="text" class="normalInput w100"/>
					 		MHz
					 	</td>
					 	<td class="td_1"></td>
					 	<td class="rightBlueTxt td_1">@spectrum.Power@</td>
					 	<td class="td_1">
					 		<input id="power" type="text" class="normalInput w100"/>
					 		dBmV
					 	</td>
					 </tr>
					 <tr>
						<td class="rightBlueTxt td_1"></td>
						<td class="td_1"></td>
					 	<td class="td_1">
					 		<a onclick="save()" href="javascript:;" class="normalBtn" style="margin-right:2px;">
			        			<span><i class="miniIcoData"></i>@COMMON.save@</span>
			        		</a>
					 	</td>
					 	<td class="td_1"></td>
					 </tr>
			</tbody>
		</table>
	</div>
	<div id="grid"></div>
</body>
</Zeta:HTML>