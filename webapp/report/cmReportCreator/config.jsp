<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.util.CurrentRequest" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Ext
	library zeta
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
    var range = $("#rangeSelect").val();
    var rangeDetail = "";
	if(range!="all"){
		rangeDetail = $('#'+range).val();
	}
    
	var zetaCallback =  window.top.ZetaCallback;
	//报表的标题以及签名
	if(!ReportTaskUtil.check(zetaCallback))return;
	//封装condition
	var conditionMap = zetaCallback.conditionMap;
		conditionMap.sortName = $("input[name='sortInfo']:checked").val();
		conditionMap.cmAliasDisable = Ext.get("cmAliasDisable").dom.checked;
		conditionMap.cmClassifiedDisable = Ext.get("cmClassifiedDisable").dom.checked;
		conditionMap.range = range;
		conditionMap.rangeDetail = rangeDetail;
	zetaCallback.conditions = JSON.stringify(conditionMap);
	/*提交任务*/
	ReportTaskUtil.commit(zetaCallback);
}

$(document).ready(function(){
	$("INPUT#title").val(window.top.ZetaCallback.templateText);
	
	loadRangeSelect();
});

function cancelClick() {
	window.top.ZetaCallback = null;
	ReportTaskUtil.refreshTaskTree();
	window.top.closeWindow("modalDlg");
}

function loadRangeSelect(){
    $.ajax({
        url:'/cmc/report/loadRangeSelect.tv',
        dataType:'json',
        success:function(rangeList){
        	for(var key in rangeList){
        		$('<select class="rangeSelect normalSel" id="'+key+'"></select>').hide().appendTo($("#rangeTd"));
        		$.each(rangeList[key], function(index, range){
        			$('<option value="'+range.id+'">'+range.name+'</option>').appendTo($("#"+key));
        		})
        	}
        },error:function(){
        }
    });
}

function loadRangeDevice(){
	var value = $("#rangeSelect").val();
	if(value!="all"){
		$('#rangeTd').show();
		$('select.rangeSelect').hide();
		$('select#'+value).show();
	}else{
		$('#rangeTd').hide();
	}
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
							<input checked="checked" disabled="disabled" type="checkbox"/>@label.ip@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" disabled="disabled" type="checkbox"/>@label.mac@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" disabled="disabled" type="checkbox"/>@report.status@
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" disabled="disabled" type="checkbox"/>@report.ccmtsAlias@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="cmAliasDisable"/>@report.aliasOrLocation@
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" id="cmClassifiedDisable"/>@report.usageOrCat@
						</div>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@report.sortInfo@:</td>
					<td>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="statusInetAddress"/>IP
						</div>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="statusMacAddress"/>@label.mac@
						</div>
						<div class="floatLeftDiv">
							<input name="sortInfo" type="radio" value="statusValue" checked="checked"/>@report.status@
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.statScope@:</td>
					<td>
						<select id="rangeSelect" class="normalSel" style="width: 170px;" onchange="loadRangeDevice()">
							<option value="all">@tip.wholeScope@</option>
							<!-- <option value="folders">@report.folder@</option> -->
							<% if(uc.hasSupportModule("olt")){%>
							<option value="olts">OLT</option>
							<% } %>
							<option value="cmcs">CC</option>
						</select>
						<span id="rangeTd"></span>
					</td>
					<td>
						<span class="vertical-middle" id="alarmSelectDevice" style="color:#FF0000;display: none;">
							<img class="vertical-middle" src="/images/performance/alarm.png" alt="" />
							<span class="vertical-middle">@tip.pleaseSelectOne@</span>
						</span>
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