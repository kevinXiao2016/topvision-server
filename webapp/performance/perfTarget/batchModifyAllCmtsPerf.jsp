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
var cmtsGolbalPerfTargetCycle = ${cmtsGolbalPerfTargetCycleJson};

function backToMain(){
	parent.closeBatchModifyFrame();
	parent.onRefreshClick();
}

function saveBatchModifyAllCmtsPerf(){
	//保存之前的验证操作
	if(validatePerfValue()==false){return;}
    //展示等待框
    window.top.showWaitingDlg("@COMMON.waiting@",'@Performance.excuteCmd@');
	$.ajax({
		url: '/cmts/perfTarget/batchModifyCmtsAllPerfTarget.tv',
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
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveBatchModifyAllCmtsPerf},
           createSetTimeButton()
           ]
    });
	
	//加入分组展开或关闭效果
	groupExpandOrCollapse();
	//checkbox事件绑定
	golbalCbxBindEvent();
	
	//为各输入框及checkbox赋值
	for(var key in cmtsGolbalPerfTargetCycle){
		$("#"+key).val(cmtsGolbalPerfTargetCycle[key]);
	}
	for(var key in cmtsGolbalPerfTargetCycle.enableList){
		if(cmtsGolbalPerfTargetCycle.enableList[key]==0){
			$("#"+key).attr("checked", false).val(0);
		}else{
			$("#"+key).attr("checked", true).val(1);
		}
	}
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
						<td class="rightTxt">@Performance.sysUptime@:</td>
						<td width="80"><input id="sysUptime" name="sysUptime" class="numberInput" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="sysUptimeEnable" name="sysUptimeEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
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
						<td class="rightTxt" width=""><span class="chnSpan1">@Performance.upLinkFlow@:</span></td>
						<td><input id="upLinkFlow" name="upLinkFlow" class="numberInput" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="upLinkFlowEnable" name="upLinkFlowEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightTxt" ><span class="chnSpan3">@Performance.channelSpeed@:</span></td>
						<td><input id="channelSpeed" name="channelSpeed" class="numberInput" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td><input id="channelSpeedEnable" name="channelSpeedEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
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
						<td class="rightTxt">@Performance.snr@:</td>
						<td width="80"><input id="snr" name="snr" class="numberInput" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="snrEnable" name="snrEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td class="rightTxt vertical-middle">
							<img src="/images/performance/Help.png" alt="" />
							<span class="chnSpan2">@Performance.ber@:</span>
						</td>
						<td width="80"><input id="ber" name="ber" class="numberInput" maxlength="4" toolTip="1-1440"/><span>@Tip.minute@</span></td>
						<td width="70"><input id="berEnable" name="berEnable" type="checkbox" class="perfCheckbox"/><span>@Tip.open@</span></td>
						<td colspan="3"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>

	<div class="alert alert-info">
		<h3 class="vertical-middle">
			<img src="/images/performance/Help.png" alt="" />
			<span>@Tip.berDetail@</span>
		</h3>
		<p>
			<span class="spanWidth150">1.@Performance.ccer@</span>
			<span class="spanWidth150">2.@Performance.ucer@</span>
		</p>
	</div>
</body>
</Zeta:HTML>