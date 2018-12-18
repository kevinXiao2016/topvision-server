<%@page import="net.sf.json.JSONObject"%>
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
    CSS css/white/disabledStyle
    IMPORT js/tools/ipText
    IMPORT epon/igmp/customOltIgmp
    IMPORT epon/igmp/igmpProtocol
</Zeta:Loader>
<style type="text/css">
	.empty{ height:1px; overflow:hidden;}
	#helpImg{ width:16px; padding:4px 0px 0px 4px; overflow:hidden; float:left;}
	.helpDiv{ width:16px; padding:4px 0px 0px 4px; overflow:hidden; float:left;}
</style>
<script type="text/javascript">
	var entityId = parent.entityId;
	var globalParam = ${paramJson};
	/**
	* 数据格式  {
	*   entityId: 30000000003
	*   commonInterval: 125            //通用查询间隔
	*   globalBW: 1000000              //IGMP总带宽
	*   igmpMode: 1                    //当前模式   
	*   igmpVersion: 3                 //IGMP版本
	*   querySrcIp: "192.168.1.1"      //查询报文源IP
	*   robustVariable: 2              //健壮系数
	*   snpAgingTime: 30               //Snooping老化时间
	*   specialInterval: 10            //特定组间隔查询
	*   squeryNum: 2                   //特定查询次数
	*   squeryRespV2: 8                //V2特定查询响应时间
	*   squeryRespV3: 8                //V3特定查询响应时间
	*   v2RespTime: 100                //V2通用查询响应时间
	*   v2Timeout: 400                 //V2版本老化时间
	*   v3RespTime: 100                //V3通用查询响应时间
	* }
	*/
	var mode =  parent.mode || globalParam.igmpMode; //这地方读取父级就行了，不需要从后台读取,proxy(1) router(2) disable(3) snooping(4);
	var igmpVersion = globalParam.igmpVersion; // 共有4种：v1(1) v2(2) v3Compatible(3) v3Only(4) 其中，设备不支持v1，因此我们有3种;
	var QuerySrcIp_ems; //ipV4的输入框;
	var IGMPMODE = {
		V2 : 2,
		V3 : 3,
		V3ONLY : 4
	}
	
</script>
</head>
<body class="frameBody pT10 pL10 pR10">
	<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		<thead>
			<tr>
				<th colspan="6" class="txtLeftTh">@igmp.baseParam@</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="rightBlueTxt w160">@igmp.version@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<select id="versionSel" class="w120 normalSel floatLeft" onchange="changeVersion()">
							<option value="2">V2</option>
							<option value="3">V3</option>
							<option value="4">V3-only</option>
						</select>
						<div id="helpImg">
							<img class="nm3kTip" nm3kTip="@igmp.tip.help@" src="/images/performance/Help.png" />
						</div>
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.searchInterval@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="QueryInterval_ems" maxlength="4" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip1@" value="125" /> s
					</div>
				</td>
				<td class="rightBlueTxt w160">@igmp.sQueryInterval@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="SqueryInterval_ems" maxlength="5" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip2@" value="10" /> 100ms
					</div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@igmp.robustVariable@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="RobustVariable_ems" maxlength="2" type="text" class="w120 normalInput" value="2" tooltip="@igmp.tip.tip3@" />
					</div>
				</td>
				<td class="rightBlueTxt">@igmp.squeryFreq@@COMMON.maohao@</td>
				<td>
					<div class="w180">
						<input id="SqueryFreq_ems" maxlength="2" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip3@" value="2" />
					</div>
				</td>
				<td class="rightBlueTxt">@igmp.querySrcIp@@COMMON.maohao@</td>
				<td>
					<div class="w180" id="putQuerySrcIp">
						
					</div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@igmp.maxBw@@COMMON.maohao@</td>
				<td colspan="5">
					<div class="w180">
						<input id="IgmpMaxBW_ems" maxlength="7" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip4@" value="1000000" /> Kbps
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	<div class="pT10" id="v2Container">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@igmp.v2Param@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w160">@igmp.v2QueryRespTime@@COMMON.maohao@</td>
					<td>
						<div class="w180">
							<input id="V2QueryRespTime_ems" maxlength="3" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip5@" value="100" /> 100ms
						</div>
					</td>
					<td class="rightBlueTxt w160">@igmp.v2SQueryRespTime@@COMMON.maohao@</td>
					<td>
						<div class="w180">
							<input id="SqueryRespV2_ems" maxlength="3" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip5@" value="8" /> 100ms
						</div>
					</td>
					<td class="rightBlueTxt w160"></td>
					<td>
						<div class="empty w180"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="pT10" id="v3Container">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@igmp.v3Param@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w160">@igmp.v3QueryRespTime@@COMMON.maohao@</td>
					<td>
						<div class="w180">
							<input id="V3QueryRespTime_ems" maxlength="5" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip6@" value="100" /> 100ms
						</div> 
					</td>
					<td class="rightBlueTxt w160">@igmp.v3SQueryRespTime@@COMMON.maohao@</td>
					<td>
						<div class="w180">
							<input id="SqueryRespV3_ems" maxlength="5" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip6@" value="8" /> 100ms
						</div>
					</td>
					<td class="rightBlueTxt w160"></td>
					<td>
						<div class="empty w180"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="pT10" id="otherContainer">
		<table id="otherTable" class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@igmp.otherParam@</th>
				</tr>
			</thead>
			<tbody>
				<td class="rightBlueTxt w160 snoopingTimeTd"><span class="snoopingTime">@igmp.snoopingTimed@@COMMON.maohao@</span></td>
				<td class="snoopingTimeTd">
					<div class="w180">
						<span class="snoopingTime">
							<input id="SnoopingAgingTime_ems" maxlength="2" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip7@" value="30" /> min
						</span>
					</div>
				</td>
				<td class="rightBlueTxt w160 v2TimeTd"><span class="v2Time">@igmp.v2Timed@@COMMON.maohao@</span></td>
				<td class="v2TimeTd">
					<div class="w180">
						<span class="v2Time">
							<input id="V3CompatV2Timeout_ems" maxlength="4" type="text" class="w120 normalInput" tooltip="@igmp.tip.tip8@" value="400" /> s
						</span>
					</div>
				</td>
				<td class="rightBlueTxt w160"></td>
				<td>
					<div class="empty w180"></div>
				</td>
			</tbody>
		</table>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB20 pT20 noWidthCenter">
	         <li><a href="javascript:;" class="normalBtnBig" onclick="refreshFromDevice()"><span><i class="miniIcoEquipment"></i>@igmp.fetchProtocolCon@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="applyFn()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
	     </ol>
	</div>
	<div class="yellowTip">
		<b class="orangeTxt">@COMMON.tip@</b>
		<p class="pT10">@igmp.tip.tip44@</p>
		<p class="pT5">@igmp.tip.tip40@</p>
		<p class="pT5">@igmp.tip.tip41@</p>
		<p class="pT5">@igmp.tip.tip45@</p>
	</div>
</body>
</Zeta:HTML>
