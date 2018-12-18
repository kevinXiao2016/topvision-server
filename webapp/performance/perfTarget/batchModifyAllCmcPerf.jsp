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
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
var cmcGolbalPerfTargetCycle = ${cmcGolbalPerfTargetCycleJson};

function backToMain(){
    parent.closeBatchModifyFrame();
    parent.onRefreshClick();
}

function saveBatchModifyAllCmcPerf(){
	//保存之前的验证操作
	if(validatePerfValue()==false){return;}
    //展示等待框
    window.top.showWaitingDlg("@COMMON.waiting@",'@Performance.excuteCmd@');
    $.ajax({
	url: '/cmc/perfTarget/batchModifyCmcAllPerfTarget.tv',
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
           {text: '@Tip.back@', iconCls: 'bmenu_back', handler: backToMain},
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveBatchModifyAllCmcPerf},
           createSetTimeButton()
           ]
    });
	
	//加入分组展开或关闭效果
	groupExpandOrCollapse();
	//checkbox事件绑定
	golbalCbxBindEvent();
	
	//为各输入框及checkbox赋值
	for(var key in cmcGolbalPerfTargetCycle){
		$("#"+key).val(cmcGolbalPerfTargetCycle[key]);
	}
	for(var key in cmcGolbalPerfTargetCycle.enableList){
		if(cmcGolbalPerfTargetCycle.enableList[key]==0){
			$("#"+key).attr("checked", false).val(0);
		}else{
			$("#"+key).attr("checked", true).val(1);
		}
	}
	//点击提示，提示信息闪烁一下;
	$("#optLinkHelpPng1").click(function(){
		var $tip = $("#yellowTip1"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	$("#optLinkHelpPng2").click(function(){
		var $tip = $("#yellowTip2"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	$("#optLinkHelpPng3").click(function(){
		var $tip = $("#yellowTip3"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	//判断全选checkbox是否选上
	$("tbody").each(function(){
		if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
			$(this).find(".allSelectCheck").attr("checked",true);
		}
	});
});//end document.ready;
</script>
</head>
<body>
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
						<td class="rightBlueTxt">@Performance.cpuUsed@:</td>
						<td width="80"><input id="cpuUsed" name="cpuUsed" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="cpuUsedEnable" name="cpuUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt">@Performance.memUsed@:</td>
						<td width="80"><input id="memUsed" name="memUsed" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="memUsedEnable" name="memUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt">@Performance.flashUsed@:</td>
						<td width="80"><input id="flashUsed" name="flashUsed" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="flashUsedEnable" name="flashUsedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt vertical-middle">
							<img id="optLinkHelpPng1" src="/images/performance/Help.png" alt="" class="cursorPointer" />
							<span>@Performance.optLink@:</span>
						</td>
						<td><input id="optLink" name="optLink" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="optLinkEnable" name="optLinkEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt vertical-middle" >
							<img id="optLinkHelpPng2" src="/images/performance/Help.png" alt=""  class="cursorPointer" />
							<span class="chnSpan3">@Performance.moduleTemp@:</span>
						</td>
						<td><input id="moduleTemp" name="moduleTemp" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="moduleTempEnable" name="moduleTempEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="3"></td>
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
						<td><input id="upLinkFlow" name="upLinkFlow" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="upLinkFlowEnable" name="upLinkFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" width=""><span class="chnSpan2">@Performance.macFlow@:</span></td>
						<td><input id="macFlow" name="macFlow" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="macFlowEnable" name="macFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt" ><span class="chnSpan3">@Performance.channelSpeed@:</span></td>
						<td><input id="channelSpeed" name="channelSpeed" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="channelSpeedEnable" name="channelSpeedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
					</tr>
					<tr class="groupContent">
						<td class="rightBlueTxt" colspan="3"><font color=red><span>@Performance.flowTip@</span></font></td>
						<td colspan=6></td>
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
						<td width="80"><input id="snr" name="snr" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="snrEnable" name="snrEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightBlueTxt vertical-middle">
							<img id="optLinkHelpPng3" src="/images/performance/Help.png" alt=""  class="cursorPointer" />
							<span class="chnSpan2">@Performance.ber@:</span>
						</td>
						<td width="80"><input id="ber" name="ber" class="numberInput normalInput w50" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="berEnable" name="berEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="3"></td>
					</tr>
				</tbody>
			</table>
	</div>
	</form>
	
	<div class="edge10">
		<div class="yellowTip" id="yellowTip">
			<div id="yellowTip1">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.optLinkDetail@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.optTxPower@</span>
					<span class="spanWidth150">2.@Performance.optRePower@</span>
					<span class="spanWidth150">3.@Performance.optCurrent@</span>
					<span class="spanWidth150">4.@Performance.optVoltage@</span>
					<span class="spanWidth150">5.@Performance.optTemp@</span>
				</p>
			</div>
			<div id="yellowTip2" class="pT10">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.moduleTempDetail@</b></p>
					<p>
						<span class="spanWidth150">1.@Performance.memTemp@</span>
						<span class="spanWidth150">2.@Performance.rfTemp@</span>
						<span class="spanWidth150">3.@Performance.upTemp@</span>
						<span class="spanWidth150">4.@Performance.bcmTemp@</span>
						<span class="spanWidth150">5.@Performance.powerTemp@</span>
					</p>
			</div>
			<div id="yellowTip3" class="pT10">
				<p class="pB5"><img id="optLinkHelpPngImg" src="/images/performance/Help.png" alt="" style="position:relative;top:3px;" /> <b class="orangeTxt">@Tip.berDetail@</b></p>
				<p>
					<span class="spanWidth150">1.@Performance.ccer@</span>
					<span class="spanWidth150">2.@Performance.ucer@</span>
				</p>
			</div>
			
		</div>
	</div>
</body>
</Zeta:HTML>