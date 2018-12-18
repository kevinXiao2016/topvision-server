<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
.floatLeftDiv input {
	vertical-align: middle;
}
</style>

<script type="text/javascript">
var WINDOW_HEIGHT = 500;
function prevClick() {
	window.history.go(-1);
}

function commit(){
	var zetaCallback =  window.top.ZetaCallback;
	//报表的标题以及签名
	if(!ReportTaskUtil.check(zetaCallback))return;
	//封装condition
	var conditionMap = zetaCallback.conditionMap;
		conditionMap.mpuaDisplayable = $("#mpuaDisplayable").is(":checked");
		conditionMap.mpubDisplayable = $("#mpubDisplayable").is(":checked");
		conditionMap.geuaDisplayable = $("#geuaDisplayable").is(":checked");
		conditionMap.geubDisplayable = $("#geubDisplayable").is(":checked");
		conditionMap.xguaDisplayable = $("#xguaDisplayable").is(":checked");
		conditionMap.xgubDisplayable = $("#xgubDisplayable").is(":checked");
		conditionMap.xgucDisplayable = $("#xgucDisplayable").is(":checked");
		conditionMap.epuaDisplayable = $("#epuaDisplayable").is(":checked");
		conditionMap.epubDisplayable = $("#epubDisplayable").is(":checked");
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
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportProperty@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>
	
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
							<input checked="checked" disabled="disabled" type="checkbox"/>@report.deviceIP@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" disabled="disabled" type="checkbox"/>@report.totalBoard@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" disabled="disabled" type="checkbox"/>@report.onlineBoard@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="mpuaDisplayable"/>MPUA
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="mpubDisplayable"/>MPUB
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="geuaDisplayable"/>GEUA
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="geubDisplayable"/>GEUB
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="xguaDisplayable"/>XGUA
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="xgubDisplayable"/>XGUB
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="xgucDisplayable"/>XGUC
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="epuaDisplayable"/>EPUA
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="epubDisplayable"/>EPUB
						</div>
					</td>
				</tr>
				<tr class="darkZebraTr">
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
</body>
</Zeta:HTML>