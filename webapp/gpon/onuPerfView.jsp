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
	plugin Nm3kDropdownTree
    module  performance
    css  css.performanceMajorStyle
    import js/highcharts-3.0.5/modules/exporting.src 
    import js/nm3k/nm3kPickDate
    import performance.js.generateTrendGraph
    IMPORT epon/onu/onuDeleteTrap
    import epon.js.onuPerfView
</Zeta:Loader>
<style type="text/css">
#optSubTd .x-form-field-wrap, #portCombo .x-form-field-wrap{width: auto !important;}
</style>
<script type="text/javascript"> 
//设备ID
var onuId = ${onuId};
var entityId = ${onuId};
//图表数据
var graphData;
var perfGroup;
var groupName = "";
var entityName = '${onu.name}';
var chart = null;
var subCatStore = null;
var comboGroup;
var comboPerfTarget;
var lovCombo;
var stTime;
var etTime;
var labelText = '';
var portType = '${portType}';
var targetPortIndex = '${portIndex}';
</script>
</head>
<body>
	<div id="mainDiv">
		<%@ include file="navigator.inc"%>

		<div id="searchDiv">
			<table cellspacing="0" cellpadding="0" border="0" style="white-space: nowrap;" >
				<tbody>
					<tr>
						<td width="100" align="right">@Tip.perfTargetGroup@:</td>
						<td><input type="text" id="comboGroup" /></td>
						<td width="100" align="right">@Performance.perftarget@:</td>
						<td><input type="text" id="comboPerfTarget" /></td>
						<td id="optSubTd" width="235" style="display:none;">
							<ul style="float:left;">
								<li id="subText" style="float:left; margin:0px 3px; position:relative; top:3px;">@Performance.optTarget@:</li>
								<li style="float:left; width:160px;"><input type="text" id="optPerftargets" /></li>
							</ul>
						</td>
						<td width="80" align="right" id="subCat">@Performance.onuPonPort@</td>
						<td width="170" id="portSelect" style="display: none;">
							<div id="portCombo" class="tagget-index" style="width: 160px;"></div>
						</td>
						<td rowspan="2">
							<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
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
		
		<!-- port_tree -->
		<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
			<ul id="portTreeSel" class="ztree" style=""></ul>
		</div>
	</div>
</body>
</Zeta:HTML>