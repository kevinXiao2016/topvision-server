<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<link rel="stylesheet" href="/performance/css/flick/jquery-ui-1.10.3.custom.min.css" />
<Zeta:Loader>
    library Ext
    library Zeta
    module network
    import performance/js/jquery-ui-1.10.3.custom.min
    import js/jquery/jquery.ba-resize
    IMPORT js.jquery.Nm3kMsg
    css css/batchTopo
    import js.jquery.nm3kToolTip
    import js.jquery.tabs
    import js.jquery.nm3kPassword
    import network/batchAutoDiscovery/batchTopo
    import network/batchAutoDiscovery/matchNetSeg
    import js/customColumnModel
    CSS css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var batchTopoData = ${batchTopoData};
</script>
</head>
<body class="whiteToBlack">
	<div id="scaningTip"></div>

	<!-- 顶部tab签 -->
	<div id="putTab" class="edge10">
		<ul class="whiteTabUl" id="tabUl">
			<li data-tab="netSegmentConfig"><a class="first selectedFirst" href="javascript:;"><span>@batchtopo.networkconfig@</span></a></li>
			<li data-tab="config"><a class="last" href="javascript:;"><span>@batchtopo.parameterconfig@ </span></a></li>
		</ul>
	</div>
	
	<!-- tab签对应的内容区域 -->
	<div id="tabContent">
		<div data-tab="netSegmentConfig" id="gridContainer">
		<div class="topoNetQuery">
		<table>
		<tr>
		<td><input type="text" class="normalInput" id="inputNet" placeHolder="@ipOrNetCheck@"></input></td>
        <td><a id="queryIpClick" href="javascript:;" class="normalBtn" ><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>		
		</tr>
		</table>
		</div>
		</div>
		
		<div data-tab="config">
			<div class="configDivLeft" id="snmpContainer">
				<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="2" class="txtLeftTh">@batchtopo.snmpbasicconfig@</th>
						</tr>
					</thead>
					<tbody>
						<%-- <tr class="darkZebraTr">
							<td class="rightBlueTxt" width="220">@batchtopo.snmptimeout@</td>
							<td class="pR10">
								<input id="snmpTimeout" type="text" class="spinnerInput" maxlength="5" onpaste="return false" toolTip="@platform/sys.snmpTimeoutFocus@"/>
								<span>@batchtopo.millisecond@</span>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@batchtopo.snmpretries@</td>
							<td class="pR10">
								<select class="normalSel" id="snmpRetries">
			                        <option value="0">0</option>
			                        <option value="1">1</option>
			                        <option value="2">2</option>
			                        <option value="3">3</option>
			                    </select>
							</td>
						</tr> --%>
						<tr>
							<td class="rightBlueTxt" width="220">@batchtopo.pingtimeout@</td>
							<td class="pR10">
								<input id="pingTimeoutConfig" type="text" class="spinnerInput" maxlength="5" onpaste="return false" toolTip="@platform/sys.TimeoutFocus@"/>
								<span>@batchtopo.millisecond@</span>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt">@batchtopo.snmplabels@</td>
							<td class="pR10" id="snmpConfigTd"></td>
						</tr>
						<tr>
							<td></td>
							<td class="pR10">
								<a id="saveSnmpConfig" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="configDivRight" id="scanConfig">
				<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
					<thead>
						<tr>
							<th colspan="2" class="txtLeftTh">@batchtopo.scanconfiguration@</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="150">@batchtopo.scanningdevicetype@</td>
							<td class="pR10">
								<p><span class="block100"><input id="allTypeCbx" type="checkbox" /><label>@batchtopo.selectall@</label></span></p>
								<p id="allTypesP"></p>
							</td>
						</tr>
						<tr>
							<td class="rightBlueTxt" width="150">@batchtopo.timingstrategy@</td>
							<td class="pR10">
								<select class="normalSel" id="strategyType">
			                        <option value="1">@batchtopo.scheduledscanning@</option>
			                        <option value="2">@batchtopo.intervalscanning@</option>
			                    </select>
			                    <span id="periodStart" class="strategyType">@batchtopo.everyday@
			                    	<input class="spinnerInput w20" id="periodHour" onpaste="return false" maxlength="2" toolTip="0~23"/>:
			                    	<input class="spinnerInput w20" id="periodMinute" onpaste="return false" maxlength="2" toolTip="0~59"/>:
			                    	<input class="spinnerInput w20" id="periodSecond" onpaste="return false" maxlength="2" toolTip="0~59"/>
			                    </span>
			                    <span id="periodSpan" class="strategyType">
			                    	<input class="spinnerInput w50" id="period" maxlength="3" onpaste="return false" toolTip="10-10080"/>@ENTITYSNAP.deviceUpTime.minute@ @batchtopo.periodTip@
			                    </span>
							</td>
						</tr>
						<tr>
							<td></td>
							<td class="pR10">
								<a id="saveScanConfig" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="scanTip" class="treeLoading greenTxt" style="display: none;">
		@batchtopo.automaticscan@
		<a onclick="" class="deleteSpanClose" href="javascript:;"></a>
	</div>
</body>
</Zeta:HTML>