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
    module igmpconfig
    IMPORT epon/igmp/customOltIgmp
    IMPORT epon/igmp/igmpUplinkPort
</Zeta:Loader>
<style type="text/css">
.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
var entityId = '${entityId}';
var uplinkPort = ${uplinkJson};
</script>
</head>
<body class="frameBody pT10 pL10 pR10">
	<table id="upLinkTable" class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@igmp.uplinkConfig@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="rightBlueTxt w160">@igmp.portType@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<select id="typeSel" class="w120 normalSel" onchange="init()">
							<option value="1">INVALID</option>
							<option value="3">GE</option>
							<option value="4">XE</option>
							<option value="9">ETHAGG</option>
						</select>
					</div>
				</td>
				<td class="rightBlueTxt w160 jsPortNumTd"><label>@igmp.igmpUplinkPort2@@COMMON.maohao@</label></td>
				<td class="jsPortNumTd">
					<div class="w180">
						<select id="portNumSel" class="w120 normalSel"></select>
					</div>
				</td>
				<td class="rightBlueTxt w160 jsAggIdTd"><label>@igmp.uplinkAggId@@COMMON.maohao@</label></td>
				<td class="jsAggIdTd">
					<div class="w180">
						<select id="aggIdSel" class="w120 normalSel"></select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB10 pT20 noWidthCenter">
	         <li><a onclick="refreshUplinkPort()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@igmp.fetchUplinkPort@</span></a></li>
	         <li><a onclick="modifyUplinkPort()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	     </ol>
	</div>
	<div class="yellowTip">
		<p class="pB5"><b class="orangeTxt">@igmp.attention@@COMMON.maohao@</b></p>
		<p>@igmp.tip.tip30@<b class="orangeTxt"> INVALID </b></p>
	</div>
</body>
</Zeta:HTML>