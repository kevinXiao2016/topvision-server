<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
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
</style>

<script type="text/javascript">
var oltGolbalPerfTargetCycle = ${golbalPerfTargetCycleJson};
var isBindDftTemp = ${isBindDftTemp};
var isPerfThdOn = ${isPerfThdOn};

/**
 * 将页面中的所有性能指标采集周期的值设为5分钟
 */
function set5ToAll(){
	$(".numberInput").val(5);
}

/**
 * 将页面中的所有性能指标采集周期的值设为10分钟
 */
function set10ToAll(){
	$(".numberInput").val(10);
}

/**
 * 将页面中的所有性能指标采集周期的值设为15分钟
 */
function set15ToAll(){
	$(".numberInput").val(15);
}

/**
 * 将页面中的所有性能指标采集周期的值设为30分钟
 */
function set30ToAll(){
	$(".numberInput").val(30);
}

/**
 * 将页面中的所有性能指标采集周期的值设为60分钟
 */
function set60ToAll(){
	$(".numberInput").val(60);
}

/**
 * 保存修改
 */
function save(){
	if(validatePerfValue()==false){return;}
	$.ajax({
		url: '/epon/perfTarget/modifyOltGolbalPerfTarget.tv',
    	type: 'POST',
    	data: $("#modifyForm").serialize(),
    	dataType:"json",
   		success: function(result) {
   	   		if(result){
	   	   		top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
	   			});
   	   	   	}else{
   	   	  		window.parent.showErrorDlg();
   	   	   	}
   			window.location.reload();
   		}, error: function(result) {
   			window.parent.showErrorDlg();
   			window.location.reload();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 恢复默认配置
 */
function undo(){
	//赋值
	$("#isBindDftTemp").val(isBindDftTemp);
	$("#isPerfThdOn").val(isPerfThdOn);

	for(var key in oltGolbalPerfTargetCycle){
		$("#"+key).val(oltGolbalPerfTargetCycle[key]);
	}
	
	for(var key in oltGolbalPerfTargetCycle.enableList){
		if(oltGolbalPerfTargetCycle.enableList[key]==1){
			$("#"+key).attr("checked", true).val(1);
		}else{
			$("#"+key).attr("checked", false).val(0);
		}
	}
	
	//检查checkbox是否被全部选中，来确定全选按钮是否勾上
	$("tbody").each(function(){
		if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
			$(this).find(".allSelectCheck").attr("checked",true);
		}else{
			$(this).find(".allSelectCheck").attr("checked",false);
		}
	});
}

$(document).ready(function(){
	//生成toolbar
	//分组展开与关闭方法
	groupExpandOrCollapse();
	//checkbox绑定事件
	golbalCbxBindEvent();
	
	new Ext.Toolbar({
        renderTo: "toolbar",
        items: [
           {text: '@Tip.save@',cls:'mL10', iconCls: 'bmenu_save', handler: save},'-',
           {text: '@Tip.reset@', iconCls: 'bmenu_miniIcoBack', handler: undo},'-',
           createSetTimeButton()
           ]
    });
	
	//赋值
	$("#isBindDftTemp").val(isBindDftTemp);
	$("#isPerfThdOn").val(isPerfThdOn);

	for(var key in oltGolbalPerfTargetCycle){
		$("#"+key).val(oltGolbalPerfTargetCycle[key]);
	}
	
	for(var key in oltGolbalPerfTargetCycle.enableList){
		if(oltGolbalPerfTargetCycle.enableList[key]==1){
			$("#"+key).attr("checked", true).val(1);
		}else{
			$("#"+key).attr("checked", false).val(0);
		}
	}
	
	//给输入框加上tooltip
	$(".numberInput").each(function(){
		$(this).attr("toolTip",'@Tip.collectTimeRule@');
	});
	
	//判断全选checkbox是否选上
	$("tbody").each(function(){
		if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
			$(this).find(".allSelectCheck").attr("checked",true);
		}
	});	
	
	//点击提示，提示信息闪烁一下;
	$("#optLinkHelpPng").click(function(){
		var $tip = $("#yellowTip"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	
})	
</script>
</head>
<body>
	
	<div id="toolbar"></div>
	<form id="modifyForm">
		<div class="contentDiv">
			<table class="contentTable" cellpadding="0" cellspacing="0" rules="all" border="0">
				<tbody>
					<tr class="groupTitle">
						<td class="groupTitle_open">@Performance.thresholdTemplate@</td>
						<td colspan="8"></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt"><span class="chnSpan1">@Performance.relaDefualtTemplate@:</span></td>
						<td colspan="2" width="150">
							<select id="isBindDftTemp" name="isBindDftTemp" style="width: 150px">
									<option value="1">@Tip.open@</option>
									<option value="0">@Tip.off@</option>
							</select>
						</td>
						<td class="rightBlueTxt"><span class="chnSpan2">@Performance.perfAlert@:</span></td>
						<td colspan="2" width="150">
							<select id="isPerfThdOn" name="isPerfThdOn" style="width: 150px">
									<option value="1">@Tip.open@</option>
									<option value="0">@Tip.off@</option>
							</select>
						</td>
						<td></td>
						<td width="150" colspan="2"></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt" colspan="3"><font color=red>@Performance.relaDefualtTemplateDesc@</font></td>
						<td class="rightBlueTxt" colspan="3"><font color=red>@Performance.perfAlertDesc@</font></td>
						<td></td>
						<td colspan="2" width="150"></td>
					</tr>
				</tbody>
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
			<p class="pB10"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" /> <b class="orangeTxt">@Tip.optLinkDetail@</b></p>
			<p>
				<span class="spanWidth150">1.@Performance.optTxPower@</span>
				<!-- <span class="spanWidth150">2.@Performance.optRePower@</span> -->
				<span class="spanWidth150">2.@Performance.optCurrent@</span>
				<span class="spanWidth150">3.@Performance.optVoltage@</span>
				<span class="spanWidth150">4.@Performance.optTemp@</span>
			</p>
		</div>
	</div>
	
	
</body>
</Zeta:HTML>