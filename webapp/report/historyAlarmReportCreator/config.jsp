<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Zeta
	library Ext
	library Jquery
	module report
	import report.javascript.ReportTaskUtil
	import js.tools.numberInput
	import js.json2
	css css.reportConfig
</Zeta:Loader>

<style type="text/css">
.floatLeftDiv{
	float: left;
	margin: 2px 5px;
	vertical-align: middle;
	white-space: nowrap;
	word-spacing: normal;
}
.floatLeftDiv input,  .floatLeftDiv select{
	vertical-align: middle;
}
</style>

<script type="text/javascript">
var WINDOW_HEIGHT = 500;

function prevClick() {
	window.history.go(-1);
}

function commit(){
	//验证统计设备类型是否至少选了一个
	if(Ext.get("eponSelected").dom.checked==false && Ext.get("ccmtsSelected").dom.checked==false){
		return window.parent.showMessageDlg("@COMMON.tip@", "@report.MoreThenOneType@","error") && false;
	}
	var zetaCallback =  window.top.ZetaCallback;
	//报表的标题以及签名
	if(!ReportTaskUtil.check(zetaCallback))return;
	//封装condition
	var conditionMap = zetaCallback.conditionMap;
		conditionMap.mainAlarmDisable = Ext.get("mainAlarmDisable").dom.checked;
		conditionMap.minorAlarmDisable = Ext.get("minorAlarmDisable").dom.checked;
		conditionMap.generalAlarmDisable = Ext.get("generalAlarmDisable").dom.checked;
		conditionMap.messageDisable = Ext.get("messageDisable").dom.checked;
		conditionMap.eponSelected = Ext.get("eponSelected").dom.checked;
		conditionMap.ccmtsSelected = Ext.get("ccmtsSelected").dom.checked;
	zetaCallback.conditions = JSON.stringify(conditionMap);
	/*提交任务*/
	ReportTaskUtil.commit(zetaCallback);
}

$(document).ready(function(){
	$("INPUT#title").val(window.top.ZetaCallback.templateText);
});

function cancelClick() {
	window.top.ZetaCallback = null;
	ReportTaskUtil.refreshTaskTree();
	window.top.closeWindow("modalDlg");
}
</script>

</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportProperty@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<div id="content" class="edgeTB10LR20 pT20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="150">@report.reportTitle@<span class="required">*</span>:</td>
					<td><input id="title" type="text" maxlength=32 style="width: 310px;" class="normalInput" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="150">@report.reportAuthor@<span class="required">*</span>:</td>
					<td><input id="author" type="text" maxlength=32 style="width: 310px;" class="normalInput" value="<%=CurrentRequest.getCurrentUser().getUser().getFamilyName() %>"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="150">@report.displayFields@:</td>
					<td>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@report.folder@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@label.entityName@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@report.deviceType@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@report.allAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@report.emergencyAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" checked="checked" disabled="disabled"/>@report.seriousAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="mainAlarmDisable"/>@report.mainAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="minorAlarmDisable"/>@report.minorAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="generalAlarmDisable"/>@report.generalAlarm@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="messageDisable"/>@report.message@
						</div>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@report.statDeviceType@<span class="required">*</span>:</td>
					<td>
						<div class="floatLeftDiv">
							<input type="checkbox" id="eponSelected" checked="checked"/>EPON
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="ccmtsSelected" checked="checked"/>CMTS
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.reportType@<span class="required">*</span>:</td>
					<td>
						<input type="checkbox" id="excelSupport" checked="checked" disabled="disabled" />EXCEL 
						<input type="checkbox" id="pdfSupport" style="margin-left: 45px;display:none" disabled="disabled"/>
						<input type="checkbox" id="htmlSupport" style="margin-left: 50px;display:none" disabled="disabled"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="prevClick()" id="BTP" icon="miniIcoArrLeft">@COMMON.prev@</Zeta:Button>
		<Zeta:Button onClick="commit()" id="BTN" icon="miniIcoSaveOK">@COMMON.finish@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" id="BTC" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	<!-- 第三部分，按钮组合 -->
</body>
</Zeta:HTML>