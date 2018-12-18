<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
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
var cmtsGolbalPerfTargetCycle = ${golbalPerfTargetCycleJson};
var isBindDftTemp = ${isBindDftTemp};
var isPerfThdOn = ${isPerfThdOn};

/**
 * 将页面中的所有性能指标采集周期的值设为5分钟
 */
function set5ToAll(){
	$(".numberInput").val(5).attr("disabled",false).removeClass("disabledInput");;
}

/**
 * 将页面中的所有性能指标采集周期的值设为10分钟
 */
function set10ToAll(){
	$(".numberInput").val(10).attr("disabled",false).removeClass("disabledInput");
}

/**
 * 将页面中的所有性能指标采集周期的值设为15分钟
 */
function set15ToAll(){
	$(".numberInput").val(15).attr("disabled",false).removeClass("disabledInput");
}

/**
 * 将页面中的所有性能指标采集周期的值设为30分钟
 */
function set30ToAll(){
	$(".numberInput").val(30).attr("disabled",false).removeClass("disabledInput");
}

/**
 * 将页面中的所有性能指标采集周期的值设为60分钟
 */
function set60ToAll(){
	$(".numberInput").val(60).attr("disabled",false).removeClass("disabledInput");
}

/**
 * 保存修改
 */
function save(){
	if(validatePerfValue()==false){return;}
	$.ajax({
		url: '/cmts/perfTarget/modifyCmtsGolbalPerfTarget.tv',
    	type: 'POST',
    	data: $("#modifyForm").serialize(),
    	dataType:"json",
   		success: function(result) {
   			if(!result){
   				window.parent.showErrorDlg();
   			}
   			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
   			});
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
	for(var key in cmtsGolbalPerfTargetCycle){
		$("#"+key).val(cmtsGolbalPerfTargetCycle[key]);
	}
	
	for(var key in cmtsGolbalPerfTargetCycle.enableList){
		if(cmtsGolbalPerfTargetCycle.enableList[key]==1){
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
	//分组展开与关闭方法
	groupExpandOrCollapse();
	//checkbox绑定事件
	golbalCbxBindEvent();
	
	//生成toolbar
	new Ext.Toolbar({
        renderTo: "toolbar",
        items: [
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: save},'-',
           {text: '@Tip.reset@', iconCls: 'bmenu_miniIcoBack', handler: undo},'-',
           createSetTimeButton()
           ]
    });
	
	//赋值
	$("#isBindDftTemp").val(isBindDftTemp);
	$("#isPerfThdOn").val(isPerfThdOn);
	
	for(var key in cmtsGolbalPerfTargetCycle){
		$("#"+key).val(cmtsGolbalPerfTargetCycle[key]);
	}
	
	for(var key in cmtsGolbalPerfTargetCycle.enableList){
		if(cmtsGolbalPerfTargetCycle.enableList[key]==1){
			$("#"+key).attr("checked", true).val(1);
		}else{
			$("#"+key).attr("checked", false).val(0);
		}
	}
	
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
	<div class="formtip" id="tips" style="display: none"></div>
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
</body>
</Zeta:HTML>