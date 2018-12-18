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
	css report.report
</Zeta:Loader>
<style type="text/css">
.floatLeftDiv{
	float: left;
	margin: 2px 5px;
	vertical-align: middle;
	white-space: nowrap;
	word-spacing: normal;
	line-height: 20px;
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
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="150">@report.displayFields@</td>
					<td>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@report.folder@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@report.entity@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@report.channelNum@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"90%-100%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"80%-90%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"70%-80%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"60%-70%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"50%-60%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"40%-50%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"30%-40%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"20%-30%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"10%-20%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"0%-10%"
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							"0%"
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
		<Zeta:Button onClick="prevClick()" id="BTP" icon="miniIcoSaveOK">@COMMON.prev@</Zeta:Button>
		<Zeta:Button onClick="commit()" id="BTN" icon="miniIcoSaveOK">@COMMON.finish@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" id="BTC">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
	<!-- 第三部分，按钮组合 -->
</body>
</Zeta:HTML>
