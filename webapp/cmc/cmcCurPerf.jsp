<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<!-- 此处需要使用高版本的jquery库和highstock库，来解决动态获取数据的问题  -->
<script src="/performance/js/jquery-1.8.3.js"></script>
<script src="/performance/js/highstock.js"></script>
<Zeta:Loader>
    library Ext
    import js.ext.ext-basex
    library Zeta
	plugin LovCombo
	plugin DateTimeField
    module  performance
    css  css.performanceMajorStyle
    import js/highcharts-3.0.5/modules/exporting.src    
    import js/nm3k/nm3kPickDate
    import performance.js.generateTrendGraph
    import cmc.js.cmcCurPerf
</Zeta:Loader>
<style type="text/css">
#subPerfTd .x-form-field-wrap, #portIndex .x-form-field-wrap{ width:auto !important; }
</style>
<script type="text/javascript">
//设备ID
var entityId = ${cmcId};
var cmcId = ${cmcId};
var cmcIndex = ${cmcIndex};
var entityName = '${entityName}';
//CCMTS类型
var deviceType = ${deviceType};
//图表数据
var graphData;
var chart = null;
var subCatStore = null;
var comboGroup;
var comboPerfTarget;
var lovCombo;
var stTime;
var etTime;
var labelText;
var nmProject = <%= uc.hasSupportProject("nm")%>;
var channelIndex='${channelIndex}'; //added by wubo   2017.01.23
var targetFlag='${flag}';//网络概况指标标记
</script>
</head>
<body>
	<div id="mainDiv">
		<div id="titleBar">
			<%@ include file="entity.inc"%>
		</div>

		<div id="searchDiv">
			<table cellspacing="0" cellpadding="0" border="0" style="white-space: nowrap;" >
				<tbody>
					<tr>
						<td width="88" align="right">@Tip.perfTargetGroup@:</td>
						<td><input type="text" id="comboGroup" /></td>
						<td width="88" align="right">@Performance.perftarget@:</td>
						<td><input type="text" id="comboPerfTarget" /></td>
						<td id="subPerfTd" width="278" style="display:none;">
							<ul style="float:left;">
								<li id="subText" style="float:left; text-align:right; width:75px; margin:56px 0px; position:absolute; top:3px;">@Performance.module@:</li>
								<li style="float:left; width:208px;position:absolute; margin:-11px 79px;"><input type="text" id="subPerftargets" /></li>
							</ul>
						</td>
						<td width="70" align="right" id="subCat"></td>
						<td width="170" style="display: none;"><div id="portIndex" class="tagget-index" style="width: 150px;"></div></td>
						<td rowspan="2" style="padding-left: 15px;">
							<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
							<!-- <button type="button" class="BUTTON75" onclick="query()" id="btnsreath">@COMMON.query@</button> -->
						</td>
					</tr>
					<tr>
						<td align="right"><span class="tagget-hide" style="width: 70px">@Tip.startTime@</span></td>
						<td><div id="startTime"></div></td>
						<td align="right"><span class="tagget-hide" style="width: 70px">@Tip.endTime@</span></td>
						<td><div id="endTime"></div></td>
						<td align="right"></td>
						<td align="right"></td>
						<td align="right"></td>
					</tr>
				</tbody>
			</table>
		</div>

		<div id="container"></div>
		<div id="loading" onclick="closeLoading()">@Tip.loading@</div>
	</div>
</body>
</Zeta:HTML>