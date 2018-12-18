<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
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
var entityId = ${cmtsPerfTargetCycle.entityId};

/**
 * 将当前CMTS的性能指标保存为全局变量
 */
function saveAsGolbalCmtsPerf(){
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
		url: '/cmts/perfTarget/saveAsCmtsGolbalPerfTarget.tv?selectedPerfNames='+selectedPerfNames.join(",")+formatUrlStr,
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
			handler : saveAsGolbalCmtsPerf
		} ]
	});
	
	//将开启的性能指标图标更换为正确的提示
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
						<th width="300" style="margin:0; padding:0;">${cmtsPerfTargetCycle.deviceName}</th>
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
						<td>@Performance.sysUptime@</td>
						<td id="sysUptimeGolbal">
							<span>${cmtsPerfTargetCycleGolbal.sysUptime}</span>
							<img src="/images/performance/off.png" alt="${cmtsPerfTargetCycleGolbal.enableList.sysUptimeEnable}"/>
						</td>
						<td id="sysUptimeCurrent">
							<span id="sysUptimeValue">${cmtsPerfTargetCycle.sysUptime}</span>
							<img id="sysUptimeEnable" src="/images/performance/off.png" alt="${cmtsPerfTargetCycle.enableList.sysUptimeEnable}"/>
						</td>
						<td><input id="sysUptime" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
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
							<span>${cmtsPerfTargetCycleGolbal.upLinkFlow}</span>
							<img src="/images/performance/off.png" alt="${cmtsPerfTargetCycleGolbal.enableList.upLinkFlowEnable}"/>
						</td>
						<td width="300" id="upLinkFlowCurrent">
							<span id="upLinkFlowValue">${cmtsPerfTargetCycle.upLinkFlow}</span>
							<img id="upLinkFlowEnable" src="/images/performance/off.png" alt="${cmtsPerfTargetCycle.enableList.upLinkFlowEnable}"/>
						</td>
						<td><input id="upLinkFlow"  type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.channelSpeed@</td>
						<td id="channelSpeedGolbal">
							<span>${cmtsPerfTargetCycleGolbal.channelSpeed}</span>
							<img src="/images/performance/off.png" alt="${cmtsPerfTargetCycleGolbal.enableList.channelSpeedEnable}"/>
						</td>
						<td id="channelSpeedCurrent">
							<span id="channelSpeedValue">${cmtsPerfTargetCycle.channelSpeed}</span>
							<img id="channelSpeedEnable" src="/images/performance/off.png" alt="${cmtsPerfTargetCycle.enableList.channelSpeedEnable}"/>
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
							<span>${cmtsPerfTargetCycleGolbal.snr}</span>
							<img src="/images/performance/off.png" alt="${cmtsPerfTargetCycleGolbal.enableList.snrEnable}"/>
						</td>
						<td id="snrCurrent">
							<span id="snrValue">${cmtsPerfTargetCycle.snr}</span>
							<img id="snrEnable" src="/images/performance/off.png" alt="${cmtsPerfTargetCycle.enableList.snrEnable}"/>
						</td>
						<td><input id="snr" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
					</tr>
					<tr>
						<td>@Performance.ber@</td>
						<td id="berGolbal">
							<span>${cmtsPerfTargetCycleGolbal.ber}</span>
							<img src="/images/performance/off.png" alt="${cmtsPerfTargetCycleGolbal.enableList.berEnable}"/>
						</td>
						<td id="berCurrent">
							<span id="berValue">${cmtsPerfTargetCycle.ber}</span>
							<img id="berEnable" src="/images/performance/off.png" alt="${cmtsPerfTargetCycle.enableList.berEnable}"/>
						</td>
						<td><input id="ber" type="checkbox" class="saveAsGolbalPerfCbx"/> </td>
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