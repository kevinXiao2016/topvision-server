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
    import performance.js.generateTrendGraph
    import cmts.cmtsCurPerf
	import js/nm3k/nm3kPickDate
</Zeta:Loader>
<style type="text/css">
#subPerfTd .x-form-field-wrap, #portIndex .x-form-field-wrap{ width:auto !important; }
</style>
<script type="text/javascript">
//设备ID
var entityId = ${cmcId};
var entityName = '${entityName}';
var cmcId = ${cmcId};
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
var targetFlag='${flag}';
</script>
</head>
<body class="whiteToBlack">
		<div id="titleBar">
			<%@ include file="entity.inc"%>
		</div>

		<div class="edgeAndClearFloat" id="searchDiv">
			<table class="topSearchTable" border="0">
					<tr>
						<td class="rightBlueTxt w70">@Tip.perfTargetGroup@:</td>
						<td><input type="text" id="comboGroup" /></td>
						<td class="rightBlueTxt w70">@Performance.perftarget@:</td>
						<td><input type="text" id="comboPerfTarget" /></td>
						<td id="subPerfTd" width="235" style="display:none;">
							<ul style="float:left;">
								<li id="subText" style="float:left; margin:0px 3px; position:relative; top:3px;">@Performance.berTarget@:</li>
								<li style="float:left; width:160px;"><input type="text" id="subPerftargets" /></li>
							</ul>
						</td>
						<td class="rightBlueTxt w70" id="subCat"></td>
						<td class="w70" style="display: none;"><div id="portIndex" class="tagget-index" style="width: 160px;"></div></td>
						<td rowspan="2">
						<ol class="upChannelListOl pB0">
							<li>
								<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
								<%-- <a onclick="query()" id="btn1" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> --%>
							</li>
						</ol>
					</tr>
					<tr>
						<td class="rightBlueTxt w70"><span class="tagget-hide" style="width: 70px">@Tip.startTime@</span></td>
						<td><div id="startTime"></div></td>
						<td class="rightBlueTxt w70"><span class="tagget-hide" style="width: 70px">@Tip.endTime@</span></td>
						<td><div id="endTime"></div></td>
						<td align="right"></td>
						<td align="right"></td>
						<td align="right"></td>
					</tr>
			</table>

		<div id="container"></div>
		<div id="loading" onclick="closeLoading()">@Tip.loading@</div>
	</div>
</body>
</Zeta:HTML>