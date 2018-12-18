<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    css  css.performanceMajorStyle
    import performance.js.performanceMajor
    import performance.js.oltPerfParmConfig
</Zeta:Loader>
<script type="text/javascript">
var cm = null;
var store = null;
var grid = null;
var tbar = null;
var bbar = null;
var data = null;
var sm = null;
var h = document.documentElement.clientHeight-100;
var w = document.documentElement.clientWidth-40;
var pageSize = <%= uc.getPageSize() %>;
</script>
<style type="text/css">
.x-panel-bbar td{height:auto !important;}
.x-panel-bbar td .x-btn-mc{ height:23px !important;}
</style>
</head>
<body>
<div id="mainDiv">
	<div class=formtip id=tips style="display: none"></div>
	<div id="north">
		<div id="toolbar"></div>
		<div id="serachDiv">
			<table style="white-space: nowrap;">
				<tbody>
					<tr>
						<td class="rightBlueTxt">@Performance.deviceName@:</td>
						<td><input class="queryInput normalInput" id="deviceName"/></td>
						<td class="rightBlueTxt queryBar">@Performance.manageIp@:</td>
						<td><input class="queryInput normalInput" id="manageIp_"/></td>
						<td class="rightBlueTxt queryBar">@Performance.perftarget@:</td>
						<td>
							<select id="perfType" class="normalSel">
								<option value="">@Tip.select@</option>
								<option value="cpuUsed">@Performance.cpuUsed@</option>
								<option value="memUsed">@Performance.memUsed@</option>
								<option value="flashUsed">@Performance.flashUsed@</option>
								<option value="boardTemp">@Performance.boardTemp@</option>
								<option value="fanSpeed">@Performance.fanSpeed@</option>
								<option value="optLink">@Performance.optLink@</option>
								<option value="sniFlow">@Performance.sniFlow@</option>
								<option value="ponFlow">@Performance.ponFlow@</option>
								<option value="onuPonFlow">@Performance.onuPonFlow@</option>
								<option value="uniFlow">@Performance.uniFlow@</option>
							</select>
						</td>
						<td class="rightBlueTxt queryBar">@Tip.collectCycle@:</td>
						<td><input maxlength="4" toolTip="@Tip.collectTimeRule@" class="queryInput normalInput" id="collectCycle"/></td>
						<td>
							<a href="javascript:;" onclick="query()" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> 							
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<div id="center"></div>
</div>

<!-- 编辑性能参数DIV -->
<div id="modifyDiv" class="cover" style="z-index:2;display:none;">
	<div id="toolbar-back"></div>
	<form id="modifyForm">
		<input type="hidden" id="selectedPerfNames" name="selectedPerfNames" value=""/>
		<input type="hidden" id="entityId" name="entityId" class="modifyPerfInput"/>
		<input type="hidden" id="deviceName" name="deviceName" class="modifyPerfInput" value=""/>
		<input type="hidden" id="manageIp" name="manageIp" class="modifyPerfInput"/>
		<div class="contentDiv">
			<table class="contentTable" cellpadding="0" cellspacing="0" rules="all" border="0">
				<tbody>
					<tr class="groupTitle">
						<td colspan="9" class="groupTitle_open">
							<span class="spanWidth100">@Performance.service@</span>
							<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
						</td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt" width=""><span class="chnSpan1">@Performance.cpuUsed@:</span></td>
						<td width="80"><input id="cpuUsed" name="cpuUsed" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="cpuUsedEnable" name="cpuUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" width=""><span class="chnSpan2">@Performance.memUsed@:</span></td>
						<td width="80"><input id="memUsed" name="memUsed" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="memUsedEnable" name="memUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" width=""><span class="chnSpan3">@Performance.flashUsed@:</span></td>
						<td width="80"><input id="flashUsed" name="flashUsed" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="flashUsedEnable" name="flashUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt"><span class="chnSpan1">@Performance.boardTemp@:</span></td>
						<td><input id="boardTemp" name="boardTemp" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td><input id="boardTempEnable" name="boardTempEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt"><span class="chnSpan2">@Performance.fanSpeed@:</span></td>
						<td><input id="fanSpeed" name="fanSpeed" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td><input id="fanSpeedEnable" name="fanSpeedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt vertical-middle">
							<img id="optLinkHelpPng" src="/images/performance/Help.png" alt="" class="cursorPointer" />
							<span class="chnSpan2">@Performance.optLink@:</span>
						</td>
						<td><input id="optLink" name="optLink" maxlength="4" class="numberInput normalInput w50"/><span>@Tip.minute@</span></td>
						<td><input id="optLinkEnable" name="optLinkEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
					</tr>
				</tbody>
				<tbody>
					<tr class="groupTitle">
						<td colspan="9" class="groupTitle_open">
							<span class="spanWidth100">@Performance.flow@</span>
							<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
						</td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt" width=""><span class="chnSpan1">@Performance.sniFlow@:</span></td>
						<td width="80"><input id="sniFlow" name="sniFlow" class="numberInput normalInput w50" maxlength="4" /><span>@Tip.minute@</span></td>
						<td width="70"><input id="sniFlowEnable" name="sniFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" width=""><span class="chnSpan2">@Performance.ponFlow@:</span></td>
						<td width="80"><input id="ponFlow" name="ponFlow" class="numberInput normalInput w50" maxlength="4" /><span>@Tip.minute@</span></td>
						<td width="70"><input id="ponFlowEnable" name="ponFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" ><span class="chnSpan3">@Performance.onuPonFlow@:</span></td>
						<td width="80"><input id="onuPonFlow" name="onuPonFlow" class="numberInput normalInput w50" maxlength="4" /><span>@Tip.minute@</span></td>
						<td width="70"><input id="onuPonFlowEnable" name="onuPonFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt"><span class="chnSpan1">@Performance.uniFlow@:</span></td>
						<td><input id="uniFlow" name="uniFlow" class="numberInput normalInput w50" maxlength="4" /><span>@Tip.minute@</span></td>
						<td><input id="uniFlowEnable" name="uniFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="6"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
	
	<div class="edge10">
		<div class="yellowTip" id="yellowTip">
			<p class="pB10"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.optLinkDetail@</b></p>
				<p>
				<span class="spanWidth150">1.@Performance.optTxPower@</span>
				<!-- <span class="spanWidth150">2.@Performance.optRePower@</span> -->
				<span class="spanWidth150">2.@Performance.optCurrent@</span>
				<span class="spanWidth150">3.@Performance.optVoltage@</span>
				<span class="spanWidth150">4.@Performance.optTemp@</span>
			</p>
		</div>		
	</div>
	
</div>


<div id="saveAsGolbalPerfOuter" class="cover" style="display:none; z-index:3;">
	<iframe id="saveAsGolbalPerf" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
</div>

<div id="loading" onclick="closeLoading()">@Tip.loading@</div>
</body>
</Zeta:HTML>