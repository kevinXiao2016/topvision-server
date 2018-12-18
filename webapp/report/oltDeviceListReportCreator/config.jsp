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
	css report.report
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
	var sortInfo = $("input[name='sortInfo']:checked").val();
	var checkedColumns = new Array;
	var checkboxColumns = $(".COLUMN-SELECT[value!='NULL']:checked");
	for(var i=0 ; i <checkboxColumns.length;i++){
		checkedColumns.add(checkboxColumns[i].value);
	}
	//报表的标题以及签名
	if(!ReportTaskUtil.check(zetaCallback))return;
	//报表的条件
	var conditionMap = zetaCallback.conditionMap;
		conditionMap.sortInfo = sortInfo;
		conditionMap.displayColumns = checkedColumns.join(",");
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
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@report.alias@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@label.ip@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" checked="checked" value='NULL' disabled="disabled"/>
							@label.type@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" value='sysLocation'/>
							@label.location@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" class="COLUMN-SELECT" value='createTime'/>
							@report.createTime@
						</div>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@report.sortInfo@:</td>
					<td>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="name" checked="checked"/> @report.alias@
						</div>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="ip" /> @label.ip@
						</div>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="typeName" /> @label.type@
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
</body>
</Zeta:HTML>
