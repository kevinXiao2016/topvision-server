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
    css js/tools/css/numberInput
    import js.tools.numberInput
    import epon/igmp/customOltIgmp
    import epon/igmp/igmpCtcConfig
</Zeta:Loader>
<style type="text/css">

</style>
<script type="text/javascript">
var entityId = parent.entityId;
var ctcParam = ${ctcJson};
/*  数据格式：
*  {
*     ctcEnable: 1             //CTC组播功能使能
*     cdrInterval: 600         //CDR主动上报间隔
*     cdrNum: 200              //CDR主动上报数量
*     cdrReport: 0             //手动上报CDR日志
*     autoResetTime: 14400     //自动清零时刻
*     entityId: 30000000003
*     recognitionTime: 30      //标识时间
*     onuFwdMode: 1            //ONU转发模式
*   }
*/
</script>
</head>
<body class="frameBody pT10 pL10 pR10">
	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@igmp.controllableMulti@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="rightBlueTxt w160">@igmp.CTCmultiEnabled@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<select id="ctcEnable" class="w120 normalSel">
							<option value="2">disable</option>
							<option value="1">enable</option>
						</select>
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.cdrInterval@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="cdrInterval" maxlength="5" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip17@" value="600" /> s
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.cdrNum@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="cdrNum" maxlength="3" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip18@" value="200" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt w160">@igmp.autoClear@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<ul class="leftFloatUl">
							<li id="putHourInput"></li>
							<li class="pT3">@CALENDAR.Hour@</li>
							<li id="putMinInput"></li>
							<li class="pT3">@CALENDAR.Min@</li>
							<li id="putSecondInput"></li>
							<li class="pT3">@CALENDAR.Sec@</li>
						</ul>
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.tagTime@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="recognitionTime" maxlength="3" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip19@" value="30" /> s
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.onuFwdMode@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<select id="onuFwdMode" class="w120 normalSel">
							<option value="2">VLAN + IP</option>
							<option value="1">VLAN + MAC</option>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	         <li><a onclick="refreshCtcParam()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@igmp.fetchCtcCon@</span></a></li>
	         <li><a onclick="modifyCtcParam()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	         <li><a onclick="reportCtcCdr()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoView"></i>@igmp.cdrReport@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>