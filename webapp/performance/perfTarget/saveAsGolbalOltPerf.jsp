<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
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
var entityId = ${oltPerfTargetCycle.entityId};

/**
 * 将当前OLT的性能指标保存为全局变量
 */
function saveAsGolbalOltPerf(){
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
	//需要将修改的指标及其数据传递到后台
	$.ajax({
		url: '/epon/perfTarget/saveAsOltGolbalPerfTarget.tv?selectedPerfNames='+selectedPerfNames.join(",")+'&entityId='+entityId+formatUrlStr,
    	type: 'POST',
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
			handler : saveAsGolbalOltPerf
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
<body>
	<div id="topPart">
		<div id="toolbar-back"></div>
		<div style="margin:0 18px 0 0">
			<table id="topPartTable" class="contentTable" cellpadding="0" cellspacing="0" border="0" width="100%">
				<tbody>
					<tr>
						<th width="300" style="margin:0; padding:0;"></th>
						<th width="300" style="margin:0; padding:0;">@Tip.globalConfig@</th>
						<th width="300" style="margin:0; padding:0;" id="entityName">${oltPerfTargetCycle.deviceName}</th>
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
					<td class="vertical-middle">
						<img src="/images/performance/arrow_down.png" alt="" />
						<span>@Performance.service@</span>
					</td>
					<td></td>
					<td></td>
					<td class="lastSubCatTd vertical-middle">
						<span>@Tip.allSelect@</span>
						<input type="checkbox" class="selectGroupCbx"/>
					</td>
				</tr>
				<tr>
					<td>@Performance.cpuUsed@</td>
					<td id="cpuUsedGolbal">
						<span>${oltPerfTargetCycleGolbal.cpuUsed}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.cpuUsedEnable}"/>
					</td>
					<td id="cpuUsedCurrent">
						<span id="cpuUsedValue">${oltPerfTargetCycle.cpuUsed}</span>
						<img id="cpuUsedEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.cpuUsedEnable}"/>
					</td>
					<td><input id="cpuUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.memUsed@</td>
					<td id="memUsedGolbal">
						<span>${oltPerfTargetCycleGolbal.memUsed}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.memUsedEnable}"/>
					</td>
					<td id="memUsedCurrent">
						<span id="memUsedValue">${oltPerfTargetCycle.memUsed}</span>
						<img id="memUsedEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.memUsedEnable}"/>
					</td>
					<td><input id="memUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.flashUsed@</td>
					<td id="flashUsedGolbal">
						<span>${oltPerfTargetCycleGolbal.flashUsed}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.flashUsedEnable}"/>
					</td>
					<td id="flashUsedCurrent">
						<span id="flashUsedValue">${oltPerfTargetCycle.flashUsed}</span>
						<img id="flashUsedEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.flashUsedEnable}"/>
					</td>
					<td><input id="flashUsed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.boardTemp@</td>
					<td id="boardTempGolbal">
						<span>${oltPerfTargetCycleGolbal.boardTemp}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.boardTempEnable}"/>
					</td>
					<td id="boardTempCurrent">
						<span id="boardTempValue">${oltPerfTargetCycle.boardTemp}</span>
						<img id="boardTempEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.boardTempEnable}"/>
					</td>
					<td><input id="boardTemp" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.fanSpeed@</td>
					<td id="fanSpeedGolbal">
						<span>${oltPerfTargetCycleGolbal.fanSpeed}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.fanSpeedEnable}"/>
					</td>
					<td id="fanSpeedCurrent">
						<span id="fanSpeedValue">${oltPerfTargetCycle.fanSpeed}</span>
						<img id="fanSpeedEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.fanSpeedEnable}"/>
					</td>
					<td><input id="fanSpeed" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.optLink@</td>
					<td id="optLinkGolbal">
						<span>${oltPerfTargetCycleGolbal.optLink}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.optLinkEnable}"/>
					</td>
					<td id="optLinkCurrent">
						<span id="optLinkValue">${oltPerfTargetCycle.optLink}</span>
						<img id="optLinkEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.optLinkEnable}"/>
					</td>
					<td><input id="optLink" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
			</tbody>
			<tbody>
				<tr class="subCat">
					<td width="300" class="vertical-middle">
						<img src="/images/performance/arrow_down.png" alt="" />
						<span>@Performance.flow@</span>
					</td>
					<td width="300"></td>
					<td width="300"></td>
					<td width="200" class="lastSubCatTd vertical-middle">
						<span>@Tip.allSelect@</span>
						<input type="checkbox" class="selectGroupCbx"/>
					</td>
				</tr>
				<tr>
					<td>@Performance.sniFlow@</td>
					<td width="300" id="sniFlowGolbal">
						<span>${oltPerfTargetCycleGolbal.sniFlow}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.sniFlowEnable}"/>
					</td>
					<td width="300" id="sniFlowCurrent">
						<span id="sniFlowValue">${oltPerfTargetCycle.sniFlow}</span>
						<img id="sniFlowEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.sniFlowEnable}"/>
					</td>
					<td><input id="sniFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.ponFlow@</td>
					<td width="300" id="ponFlowGolbal">
						<span>${oltPerfTargetCycleGolbal.ponFlow}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.ponFlowEnable}"/>
					</td>
					<td width="300" id="ponFlowCurrent">
						<span id="ponFlowValue">${oltPerfTargetCycle.ponFlow}</span>
						<img id="ponFlowEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.ponFlowEnable}"/>
					</td>
					<td><input id="ponFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.onuPonFlow@</td>
					<td width="300" id="onuPonFlowGolbal">
						<span>${oltPerfTargetCycleGolbal.onuPonFlow}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.onuPonFlowEnable}"/>
					</td>
					<td width="300" id="onuPonFlowCurrent">
						<span id="onuPonFlowValue">${oltPerfTargetCycle.onuPonFlow}</span>
						<img id="onuPonFlowEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.onuPonFlowEnable}"/>
					</td>
					<td><input id="onuPonFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
				<tr>
					<td>@Performance.uniFlow@</td>
					<td width="300" id="uniFlowGolbal">
						<span>${oltPerfTargetCycleGolbal.uniFlow}</span>
						<img src="/images/performance/off.png" alt="${oltPerfTargetCycleGolbal.enableList.uniFlowEnable}"/>
					</td>
					<td width="300" id="uniFlowCurrent">
						<span id="uniFlowValue">${oltPerfTargetCycle.uniFlow}</span>
						<img id="uniFlowEnable" src="/images/performance/off.png" alt="${oltPerfTargetCycle.enableList.uniFlowEnable}"/>
					</td>
					<td><input id="uniFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>

	<script type="text/javascript">
		new Ext.Viewport({
			layout : 'border',
			items : [ {
				region : 'north',
				height : 60,
				contentEl : "topPart"
			},{
				region : 'center',
				contentEl : "middlePart",
				autoScroll : true,
				margins : "0px"
			} ]

		});
	</script>
</body>
</Zeta:HTML>