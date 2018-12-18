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
    CSS css/jquery.treeview
    CSS css/white/disabledStyle
    IMPORT js/jquery/jquery.treeview
    IMPORT epon/igmp/onuIgmpConfig
</Zeta:Loader>
<style type="text/css">
	#putMiddleToolbar .x-toolbar{ padding:2px;}
	.borderBbar{ border-left:1px solid #D7D7D7; border-right:1px solid #D7D7D7; /* border-top:1px solid #d7d7d7; border-bottom:none; */}
	.normalTable .x-panel-tbar .x-toolbar { border-top:1px solid #d7d7d7; border-bottom:none;}
	.lightGrayBg{ background:#fbfbfb;}
	.topGrayLine{ border-top:1px solid #ccc;}
	.whiteToBlack .x-panel-body{ background:transparent;}
	.whiteToBlack div.x-layout-split{ background:url(../../css/white/dragLine.gif) repeat-y; width:8px;}
	.whiteToBlack div.x-layout-split-north{ height:8px; background:url(../../css/white/horizontalLine.png) repeat-x; cursor:default;}
	.eastBg .x-panel-body{ background:#fbfbfb;}
	.oneTopLine .x-panel-header{ border-top:1px solid #ccc;}
	.oneRightLine {border-right:1px solid #ccc;}
	.eastBg .x-panel-header{ color:#0165B0; padding:0px 0px 6px 0px; border-color:#D7D7D7; background:#f0f0f0 url("../../css/white/pannelTitle.png") repeat-x;}
	.eastBg .x-panel-header-text{ font-size:12px; padding-top:10px;}
</style>
<script type="text/javascript">
	var entityId = '${entityId}';
	var onuId = '${onuId}';
	var onuIndex = '${onuIndex}';
	var uniListJson = ${uniListJson};
	var cm,grid,store;
	//用于保存当前选择的UNI口索引
	var currentIndex;
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart" class="edge10 pB0">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@onuIgmp.config@</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w160" style="padding:4px 0px;">ONU:</td>
					<td><div class="w120" id="onuName">@onuIgmp.loading@</div></td>
					<td class="rightBlueTxt w160">@onuIgmp.groupMode@:</td>
					<td id="putOnuMode">@onuIgmp.loading@</td>
					<td class="rightBlueTxt w160">@onuIgmp.qLeave@:</td>
					<td id="putOnuFastLeave">@onuIgmp.loading@</td>
				</tr>
				<tr>
					<td colspan="6" style="padding:4px 0px;">
						<div class="noWidthCenterOuter clearBoth">
     						<ol class="upChannelListOl pB0 pT0 noWidthCenter">
								<li><a href="javascript:;" class="normalBtn" onclick="refreshOnuIgmpConfig()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
								<li><a id="onuApplyBtn" href="javascript:;" class="normalBtn" onclick="modifyOnuIgmpConfig()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
							</ol>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="pT10">
			<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="6" class="txtLeftTh">@onuIgmp.uniIgmpConfig@</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
	<div id="leftPart" class="edge10">
		<b class="blueTxt pB10 displayBlock pL6">@onuIgmp.uniPortList@</b>
		<ul id="tree" class="filetree">
			
		</ul>
	</div>
	<div id="middlePart" class="pL10 pR10 pT10">
		<p class="flagP"><span class="flagInfo">@onuIgmp.basic@</span></p>
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@igmp.portNumber@:<span class="jsPortNum"></span></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="rightBlueTxt w140" style="padding:4px 0px;">@onuIgmp.maxVlanNum@:</td>
					<td>
						<div class="w180" id="putMaxGroupNum">
							@onuIgmp.loading@
						</div>
					</td>
					<td class="rightBlueTxt w140">@onuIgmp.vlanSquence@:</td>
					<td>
						<div class="w180" id="putVlanList">
							@onuIgmp.loading@
						</div>
					</td>
					<td class="rightBlueTxt w140">@onuIgmp.vlanMode@:</td>
					<td>
						<div class="w180" id="putVlanMode">
							@onuIgmp.loading@
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="6" style="padding:4px 0px;">
						<div class="noWidthCenterOuter clearBoth">
     						<ol class="upChannelListOl pB0 pT0 noWidthCenter">
								<li><a href="javascript:;" class="normalBtn" onclick="refreshUniIgmpConfig()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
								<li><a id="uniApplyBtn" href="javascript:;" class="normalBtn" onclick="modifyUniIgmpConfig()"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
								<li><a id="vlanTransBtn" href="javascript:;" class="normalBtn" onclick="showUniVlanTrans()"><span><i class="miniIcoManager"></i>@onuIgmp.vlanTransConfig@</span></a></li>
								<li class="pT5 pL5">
									<img class="nm3kTip" nm3ktip="@igmp.tip.tip27@" src="/images/performance/Help.png">
								</li>
							</ol>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</Zeta:HTML>