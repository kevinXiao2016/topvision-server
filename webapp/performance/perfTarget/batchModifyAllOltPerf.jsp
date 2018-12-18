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
</style>
<script type="text/javascript">
var oltGolbalPerfTargetCycle = ${oltGolbalPerfTargetCycleJson};

function backToMain(){
	parent.closeBatchModifyFrame();
	parent.onRefreshClick();
}

function saveBatchModifyAllOltPerf(){
	//保存之前的验证操作
	if(validatePerfValue()==false){return;}
	//展示等待框
    window.top.showWaitingDlg("@COMMON.waiting@",'@Performance.excuteCmd@');
	$.ajax({
		url: '/epon/perfTarget/batchModifyOltAllPerfTarget.tv',
    	type: 'POST',
    	data: $("#modifyForm").serialize(),
    	dataType:"json",
   		success: function(result) {
   			window.parent.parent.closeWaitingDlg();
   			if(result){
   				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@Tip.modifySuccess@</b>'
   	   			});
   			}else{
   				window.parent.parent.showErrorDlg();
   			}
   			backToMain();
   		}, error: function(result) {
   			window.parent.parent.closeWaitingDlg();
   			window.parent.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

$(document).ready(function() {
	parent.closeLoading();
	
	new Ext.Toolbar({
        renderTo: "toolbar-back",
        items: [
           {text: '@Tip.back@', cls:'mL10', iconCls: 'bmenu_back', handler: backToMain},'-',
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveBatchModifyAllOltPerf},
           createSetTimeButton()
           ]
    });
	
	//加入分组展开或关闭效果
	groupExpandOrCollapse();
	//checkbox事件绑定
	golbalCbxBindEvent();
	
	//为各输入框及checkbox赋值
	for(var key in oltGolbalPerfTargetCycle){
		$("#"+key).val(oltGolbalPerfTargetCycle[key]);
	}
	for(var key in oltGolbalPerfTargetCycle.enableList){
		if(oltGolbalPerfTargetCycle.enableList[key]==0){
			$("#"+key).attr("checked", false).val(0);
		}else{
			$("#"+key).attr("checked", true).val(1);
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
});//end document.ready;
</script>
</head>
<body class="whiteToBlack">
	<div id="toolbar-back"></div>
	<form id="modifyForm">
		<input type="hidden" id="entityIds" name="entityIds" value="<s:property value="entityIds"/>"/>
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
							<img id="optLinkHelpPng" src="/images/performance/Help.png" alt="" style="cursor:pointer" />
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
				<span class="spanWidth150">2.@Performance.optRePower@</span>
				<span class="spanWidth150">3.@Performance.optCurrent@</span>
				<span class="spanWidth150">4.@Performance.optVoltage@</span>
				<span class="spanWidth150">5.@Performance.optTemp@</span>
			</p>
		</div>
	</div>
	
	<%-- <div class="alert alert-info">
		<h3 class="vertical-middle">
			<img id="optLinkHelpPng" src="/images/performance/Help.png" alt="" />
			<span>@Tip.optLinkDetail@</span>
		</h3>
		<p>
			<span class="spanWidth150">1.@Performance.optTxPower@</span>
			<span class="spanWidth150">2.@Performance.optRePower@</span>
			<span class="spanWidth150">3.@Performance.optCurrent@</span>
			<span class="spanWidth150">4.@Performance.optVoltage@</span>
			<span class="spanWidth150">5.@Performance.optTemp@</span>
		</p>
	</div> --%>
</body>
</Zeta:HTML>