<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library Ext
    library Jquery    
    library Zeta
    module  performance
    css  css.performanceMajorStyle
    import performance.js.performanceMajor
</Zeta:Loader>
<style type="text/css">
.contentTable td, .contentTable th {
	text-align: center;
	border: 1px solid #D6D6D6;
	vertical-align: middle;
}
.contentTable td span, .contentTable td img{
	vertical-align: middle;
}
.contentTable td span{
	margin-right: 10px;
}
</style>
<script type="text/javascript">
var entityId = ${cmcPerfTargetCycle.entityId};

/**
 * 将当前CCMTS的性能指标保存为全局变量
 */
function saveAsGolbalCmcPerf(){
	//遍历每个checkbox，找出需要修改哪些指标,及各指标的值及其开关
	var selectedPerfNames = new Array();
	var formatUrlStr = "";
	var index = 0;
	$(".saveAsGolbalPerfCbx").each(function(){
		if($(this).attr("checked")){
			var targetName = $(this).attr("id");
			selectedPerfNames[index++] = targetName;
			var value = $("#"+targetName+"Value").text();
			var enable = $("#"+targetName+"Enable").attr("alt");
			formatUrlStr += "&"+targetName+"="+value+"&"+targetName+"Enable="+enable;
		}
	});
	//只需要将需要修改哪些指标及相应的id传递到后台即可
	$.ajax({
		url: '/cmc/perfTarget/saveAsCmcGolbalPerfTarget.tv?selectedPerfNames='+selectedPerfNames.join(",")+'&entityId='+entityId+formatUrlStr,
    	dataType:"json",
   		success: function(result) {
   			if(result){
   				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   	   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
   		   		});
   			}else{
   				window.parent.parent.showErrorDlg();
   			}
   			backToFrameParent();
   		}, error: function(result) {
   			window.parent.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

$(document).ready(function() {
		parent.closeLoading();
		
		new Ext.Toolbar({
			renderTo : "toolbar-back",
			items : [ {
				text : '@Tip.back@',
				iconCls : 'bmenu_back',
				handler : backToFrameParent
			},{
				text : '@Tip.save@',
				iconCls : 'bmenu_save',
				handler : saveAsGolbalCmcPerf
			} ]
		});
		
		$("img[alt='1']").each(function(){
			var $img = $(this);
			$img.attr("src", "/images/performance/on.png")
		})
		
		$("#middlePartTable").resize(function(){
			setTopTableWidth();
		});//end resize; 
		
		$(".selectGroupCbx").attr("checked", true);
		$(".saveAsGolbalPerfCbx").attr("checked", true);
		
		saveAsGolbalCbxBindEvent();
		setTopTableWidth();
		alternateRowColors();
	});//end document.ready;
</script>
</head>
<body class="whiteToBlack"> 
	<div id="topPart">
		<div id="toolbar-back"></div>
		<div style="margin:0 18px 0 0">
			<table id="topPartTable" class="contentTable" cellpadding="0" cellspacing="0" border="0" width="100%">
				<tbody>
					<tr>
						<th width="300" style="margin:0; padding:0;"></th>
						<th width="300" style="margin:0; padding:0;">@Tip.globalConfig@</th>
						<th width="300" style="margin:0; padding:0;">${cmcPerfTargetCycle.deviceName}</th>
						<th width="200" style="margin:0; padding:0;">@Tip.saveAsGlobalConfig@</th>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div id="middlePart">
		<div style="margin:0 1px 0 0; overflow:hidden;">
			<table id="middlePartTable" class="contentTable" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr class="subCat">
						<td width="300" class="vertical-middle">
							<img src="/images/performance/arrow_down.png" alt="" />
							<span>@Performance.service@</span>
						</td>
						<td width="300"></td>
						<td width="300"></td>
						<td width="200" class="lastSubCatTd vertical-middle">
							<span>@Tip.allSelect@</span>
							<input type="checkbox" class="selectGroupCbx"/>
						</td>
					</tr>
					<tr>
						<td>@Performance.cpuUsed@</td>
						<td id="cpuUsedGolbal">
							<span>${cmcPerfTargetCycleGolbal.cpuUsed}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.cpuUsedEnable}"/>
						</td>
						<td id="cpuUsedCurrent">
							<span id="cpuUsedValue">${cmcPerfTargetCycle.cpuUsed}</span>
							<img id="cpuUsedEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.cpuUsedEnable}"/>
						</td>
						<td><input id="cpuUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.cpuUsed@</td>
						<td id="memUsedGolbal">
							<span>${cmcPerfTargetCycleGolbal.memUsed}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.memUsedEnable}"/>
						</td>
						<td id="memUsedCurrent">
							<span id="memUsedValue">${cmcPerfTargetCycle.memUsed}</span>
							<img id="memUsedEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.memUsedEnable}"/>
						</td>
						<td><input id="memUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.flashUsed@</td>
						<td id="flashUsedGolbal">
							<span>${cmcPerfTargetCycleGolbal.flashUsed}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.flashUsedEnable}"/>
						</td>
						<td id="flashUsedCurrent">
							<span id="flashUsedValue">${cmcPerfTargetCycle.flashUsed}</span>
							<img id="flashUsedEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.flashUsedEnable}"/>
						</td>
						<td><input id="flashUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.optLink@</td>
						<td id="optLinkGolbal">
							<span>${cmcPerfTargetCycleGolbal.optLink}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.optLinkEnable}"/>
						</td>
						<td id="optLinkCurrent">
							<span id="optLinkValue">${cmcPerfTargetCycle.optLink}</span>
							<img id="optLinkEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.optLinkEnable}"/>
						</td>
						<td><input id="optLink" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.moduleTemp@</td>
						<td id="moduleTempGolbal">
							<span>${cmcPerfTargetCycleGolbal.moduleTemp}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.moduleTempEnable}"/>
						</td>
						<td id="moduleTempCurrent">
							<span id="moduleTempValue">${cmcPerfTargetCycle.moduleTemp}</span>
							<img id="moduleTempEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.moduleTempEnable}"/>
						</td>
						<td><input id="moduleTemp" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
				</tbody>
				<tbody>
					<tr class="subCat">
						<td class="vertical-middle">
							<img src="/images/performance/arrow_down.png" alt="" />
							<span>@Performance.flow@</span>
						</td>
						<td></td>
						<td></td>
						<td class="lastSubCatTd vertical-middle">
							<span>@Tip.allSelect@</span>
							<input type="checkbox" class="selectGroupCbx"/>
						</td>
					</tr>
					<tr>
						<td>@Performance.upLinkFlow@</td>
						<td width="300" id="upLinkFlowGolbal">
							<span>${cmcPerfTargetCycleGolbal.upLinkFlow}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.upLinkFlowEnable}"/>
						</td>
						<td width="300" id="upLinkFlowCurrent">
							<span id="upLinkFlowValue">${cmcPerfTargetCycle.upLinkFlow}</span>
							<img id="upLinkFlowEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.upLinkFlowEnable}"/>
						</td>
						<td><input id="upLinkFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.macFlow@</td>
						<td id="macFlowGolbal">
							<span>${cmcPerfTargetCycleGolbal.macFlow}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.macFlowEnable}"/>
						</td>
						<td id="macFlowCurrent">
							<span id="macFlowValue">${cmcPerfTargetCycle.macFlow}</span>
							<img id="macFlowEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.macFlowEnable}"/>
						</td>
						<td><input id="macFlow" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.channelSpeed@</td>
						<td id="channelSpeedGolbal">
							<span>${cmcPerfTargetCycleGolbal.channelSpeed}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.channelSpeedEnable}"/>
						</td>
						<td id="channelSpeedCurrent">
							<span id="channelSpeedValue">${cmcPerfTargetCycle.channelSpeed}</span>
							<img id="channelSpeedEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.channelSpeedEnable}"/>
						</td>
						<td><input id="channelSpeed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
				</tbody>
				<tbody>
					<tr class="subCat">
						<td class="vertical-middle">
							<img src="/images/performance/arrow_down.png" alt="" />
							<span>@Performance.signalQuality@</span>
						</td>
						<td></td>
						<td></td>
						<td class="lastSubCatTd vertical-middle">
							<span>@Tip.allSelect@</span>
							<input type="checkbox" class="selectGroupCbx"/>
						</td>
					</tr>
					<tr>
						<td>@Performance.snr@</td>
						<td id="snrGolbal">
							<span>${cmcPerfTargetCycleGolbal.snr}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.snrEnable}"/>
						</td>
						<td id="snrCurrent">
							<span id="snrValue">${cmcPerfTargetCycle.snr}</span>
							<img id="snrEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.snrEnable}"/>
						</td>
						<td><input id="snr" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.ber@</td>
						<td id="berGolbal">
							<span>${cmcPerfTargetCycleGolbal.ber}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.berEnable}"/>
						</td>
						<td id="berCurrent">
							<span id="berValue">${cmcPerfTargetCycle.ber}</span>
							<img id="berEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.berEnable}"/>
						</td>
						<td><input id="ber" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
				</tbody>
				
				<tbody>
					<tr class="subCat">
						<td class="vertical-middle">
							<img src="/images/performance/arrow_down.png" alt="" />
							<span>CM FLAP</span>
						</td>
						<td></td>
						<td></td>
						<td class="lastSubCatTd vertical-middle">
							<span>@Tip.allSelect@</span>
							<input type="checkbox" class="selectGroupCbx"/>
						</td>
					</tr>
					<tr>
						<td>FLAP</td>
						<td id="cmflapGolbal">
							<span>${cmcPerfTargetCycleGolbal.cmflap}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.cmflapEnable}"/>
						</td>
						<td id="cmflapCurrent">
							<span id="cmflapValue">${cmcPerfTargetCycle.cmflap}</span>
							<img id="cmflapEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.cmflapEnable}"/>
						</td>
						<td><input id="cmflap" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
				</tbody>
				
				<tbody>
					<tr class="subCat">
						<td class="vertical-middle">
							<img src="/images/performance/arrow_down.png" alt="" />
							<span>@Performance.optical@</span>
						</td>
						<td></td>
						<td></td>
						<td class="lastSubCatTd vertical-middle">
							<span>@Tip.allSelect@</span>
							<input type="checkbox" class="selectGroupCbx"/>
						</td>
					</tr>
					<tr>
						<td>@Performance.optReceiverPower@</td>
						<td id="opticalReceiver">
							<span>${cmcPerfTargetCycleGolbal.opticalReceiver}</span>
							<img src="/images/performance/off.png" alt="${cmcPerfTargetCycleGolbal.enableList.opticalReceiverEnable}"/>
						</td>
						<td id="opticalReceiverCurrent">
							<span id="opticalReceiverValue">${cmcPerfTargetCycle.opticalReceiver}</span>
							<img id="opticalReceiverEnable" src="/images/performance/off.png" alt="${cmcPerfTargetCycle.enableList.opticalReceiverEnable}"/>
						</td>
						<td><input id="opticalReceiver" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
				</tbody>
				
				
			</table>
		<form id="saveGolbalForm">
			<input type="hidden" id="selectedPerfNames"  name="selectedPerfNames"/>
			<input type="hidden" id="entityId"  name="entityId"/>
		</form>
		</div>
	</div>
	
	<script type="text/javascript">
		new Ext.Viewport({
			layout : 'border',
			items : [ {
				region : 'north',
				height : 60,
				contentEl : "topPart"
			}, {
				region : 'center',
				contentEl : "middlePart",
				autoScroll : true,
				margins : "0px"
			} ]

		});
	</script>
</body>
</Zeta:HTML>