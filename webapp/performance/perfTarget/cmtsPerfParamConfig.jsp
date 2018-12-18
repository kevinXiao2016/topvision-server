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
    import performance.js.cmtsPerfParmConfig
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
</script>

</head>
<body class="whiteToBlack">

<div id="mainDiv">
	<div id="north">
		<div id="toolbar"></div>
		<div id="serachDiv">
			<table style="white-space: nowrap;">
				<tr>
					<td>@Performance.deviceName@:</td>
					<td><input class="queryInput normalInput" id="cmcName"/></td>
					<td class="rightTxt queryBar">@Performance.manageIp@:</td>
					<td><input class="queryInput normalInput" id="manageIp_"/></td>
					<td class="rightTxt queryBar">@Performance.perftarget@:</td>
					<td>
						<select id="perfType" class="querySelect normalSel">
							<option value="">@Tip.select@</option>
							<option value="sysUptime">@Performance.sysUptime@</option>
							<option value="upLinkFlow">@Performance.upLinkFlow@</option>
							<option value="channelSpeed">@Performance.channelSpeed@</option>
							<option value="snr">@Performance.snr@</option>
							<option value="ber">@Performance.ber@</option>
						</select>
					</td>
					<td class="rightTxt queryBar">@Tip.collectCycle@:</td>
					<td><input maxlength="4" style="width: 50px;" class="queryInput normalInput" id="collectCycle"/></td>
					<td class="rightBlueTxt">
						<!-- <input type="button" class="BUTTON75" value="@Tip.query@" onclick="query()"/> -->
						<a href="javascript:;" class="normalBtn" onclick="query()">
				 			<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
				 		</a>
					</td>
					<!-- <td class="rightTxt queryBar"><input type="button" class="BUTTON75" value="@Tip.query@" onclick="query()"/></td> -->
				</tr>
			</table>
		</div>
	</div>
	<div id="center">
	</div>
</div>

<!-- 编辑性能参数DIV -->
<div id="modifyDiv" class="cover" style="z-index:2;display:none;">
	
	<div id="toolbar-back"></div>
	<form id="modifyForm">
		<input type="hidden" id="selectedPerfNames" name="selectedPerfNames" value=""/>
		<input type="hidden" id="entityId" name="entityId" />
		<input type="hidden" id="deviceName" name="deviceName" />
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
						<td class="rightBlueTxt" width="190">@Performance.sysUptime@:</td>
						<td width="80"><input id="sysUptime" name="sysUptime" class="numberInput" maxlength="4" toolTip="@Tip.collectTimeRule@"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="sysUptimeEnable" name="sysUptimeEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="6"></td>
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
						<td class="rightBlueTxt" width=""><span class="chnSpan1">@Performance.upLinkFlow@:</span></td>
						<td><input id="upLinkFlow" name="upLinkFlow" class="numberInput" maxlength="4" toolTip="@Tip.collectTimeRule@"/><span>@Tip.minute@</span></td>
						<td><input id="upLinkFlowEnable" name="upLinkFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" width="200"><span class="chnSpan3">@Performance.channelSpeed@:</span></td>
						<td><input id="channelSpeed" name="channelSpeed" class="numberInput" maxlength="4" toolTip="@Tip.collectTimeRule@"/><span>@Tip.minute@</span></td>
						<td><input id="channelSpeedEnable" name="channelSpeedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="3"></td>
					</tr>
				</tbody>
				<tbody>
					<tr class="groupTitle">
						<td colspan="9" class="groupTitle_open">
							<span class="spanWidth100">@Performance.signalQuality@</span>
							<span>@Tip.allSelect@</span><input type="checkbox" class="allSelectCheck"/>
						</td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt">@Performance.snr@:</td>
						<td width="80"><input id="snr" name="snr" class="numberInput" maxlength="4" toolTip="@Tip.collectTimeRule@"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="snrEnable" name="snrEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt vertical-middle">
							<img id="optLinkHelpPng" src="/images/performance/Help.png" alt="" class="cursorPointer" />
							<span class="chnSpan2">@Performance.ber@:</span>
						</td>
						<td width="80"><input id="ber" name="ber" class="numberInput" maxlength="4" toolTip="@Tip.collectTimeRule@"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="berEnable" name="berEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="3"></td>
					</tr>
				</tbody>
			</table>
	</div>
	</form>

	<div class="edge10">
		<div class="yellowTip" id="yellowTip">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.berDetail@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.ccer@</span>
					<span class="spanWidth150">2.@Performance.ucer@</span>
				</p>
		</div>
	</div>
</div>


<div id="saveAsGolbalPerfOuter" class="cover" style="display:none; z-index:3;">
	<iframe id="saveAsGolbalPerf" src="" style="width:100%; height:100%;" frameborder="0"></iframe>
</div>

<div id="loading" onclick="closeLoading()">@Tip.loading@</div>
	<script type="text/javascript">
		
	</script>
	<div class="formtip" id="tips" style="display: none;z-index: 999999999999; position:absolute"></div>
</body>
</Zeta:HTML>