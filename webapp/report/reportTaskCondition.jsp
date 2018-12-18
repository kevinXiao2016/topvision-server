<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    css css.reportTemplate
	library Zeta
	library Ext
	module report
	plugin DateTimeField
	plugin DropdownTree
	plugin LovCombo
	import js.json2
	import js.jquery.nm3kToolTip
	import /report/javascript/customReportTemplate
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
.columnSpan{
	vertical-align: middle;
	display: inline-block;
	margin-right:10px;
}
.columnSpan label{
	white-space:nowrap; 
	word-break:keep-all;
}
.columnSpan label,
.columnSpan input{
	vertical-align: middle;
}
</style>

<script type="text/javascript">
var reportId = '${reportId}';
var allColumns = ${allColumns};
var detailReportInitCondition;

function prevClick() {
	window.history.go(-1);
}

function cancelClick() {
	if (window.top.reportTask) {
		window.top.reportTask = null;
	}
	window.top.closeWindow("modalDlg");
}

function buildCondition(){
	//创建通用报表模版对象
	reportTemplate = new ReportTemplate(reportId, allColumns);
	reportTemplate.buildTaskConditionView();
}

function validateTask(){
	var reg = /^[^\\/:*?"<>|]{1,32}$/;
    var title = $("#title").val();
    if(!Validator.isReportName(title)){
        $("#title").focus();
        return false;
    }
    var author = $("#author").val();
    if(!Validator.isReportName(author)){
        $("#author").focus();
        return false;
    }
    //TODO 报表模版校验
    if(!reportTemplate.validate(true)){
    	return false;
    }
    //validate ip address
    /* if(queryCondition && queryCondition.deviceIp && $('#deviceIp').val()!=''){
    	 var ip = $('#deviceIp').val();
    	 if(!Validator.isFuzzyIpAddress(ip)){
    		 $('#deviceIp').focus();
		    return false;
		 }
	} */
   
    return true;
	
}

function commit(){
	var reportTask =  window.top.reportTask;
	//校验
	if(!validateTask()) return;
	//if(!ReportTaskUtil.check(reportTask))return;
	//封装condition
	reportTask.conditionMap = {};
	var conditionMap = reportTask.conditionMap;
	//生成报表类型
	reportTask.pdfSupport = false;
	reportTask.excelSupport = true;
	reportTask.htmlSupport = false;
	conditionMap.title =  $("#title").val();
	conditionMap.author = $("#author").val();
	//if(queryCondition){
		//封装查询条件
		var queryData = reportTemplate.buildQueryData(null, true);
		$.extend(conditionMap, queryData);
	//}
	reportTask.conditions = JSON.stringify(conditionMap);
	/*提交任务*/
	window.top.showWaitingDlg("@COMMON.wait@", "@report.creating@", 'ext-mb-waiting');
	var createTaskPromise = $.post('/report/createReportTask.tv', reportTask);
	createTaskPromise.done(function(){
		window.top.showMessageDlg("@COMMON.tip@", '@report.createOk@');
		cancelClick();
		var frame = window.top.getFrame('allreporttask');
        if (frame != null) {
            frame.onRefreshClick();
        }else{
        	window.parent.addView("allreporttask", '@report.allReportTask@', "taskIcon", "report/showAllReportTask.tv");
        }
	});
	createTaskPromise.fail(function(){
		window.top.showMessageDlg("@COMMON.tip@", '@report.createER@');
	});
	createTaskPromise.always(function(){
		window.top.closeWaitingDlg();
	});
}

function trimAllBlank(value){
	var chars = [];
	for(var i=0, len = value.length; i<len; i++){
		if(value[i]!=' '){
			chars.push(value[i]);
		}
	}
	return chars.join('');
}

$(function(){
	$('#title').val(window.top.reportTask.reportName);
	var author = $('#author').val();
	$('#author').val(trimAllBlank(author))
	//填充查询条件
	buildCondition();
});
</script>

</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">@report.reportProperty@</div>
		<div class="rightCirIco pageCirIco"></div>
	</div>
	
	<div id="content" class="edgeTB10LR20 pT20">
		<table class="zebraTableRows" id="conditionTable" cellpadding="0" cellspacing="0" border="0">
			<tbody id="queryTbody">
				<tr>
					<td class="rightBlueTxt" width="150">@report.reportTitle@<span class="required">*</span>:</td>
					<td><input id="title" type="text" maxlength=63 class="normalInput" tooltip="@COMMON.anotherName@"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt" width="150">@report.reportAuthor@<span class="required">*</span>:</td>
					<td><input id="author" type="text" maxlength=63 class="normalInput" value="<%=CurrentRequest.getCurrentUser().getUser().getFamilyName() %>" tooltip="@COMMON.anotherName@"/></td>
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