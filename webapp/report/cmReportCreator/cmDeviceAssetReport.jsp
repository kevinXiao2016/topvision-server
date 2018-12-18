<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module report
	css css.report
	import report.javascript.CommonMethod
</Zeta:Loader>
<script type="text/javascript">
var rangeList = ${rangeList};
var range = '${range}';
var rangeDetail = '${rangeDetail}';
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	var sortName = '<s:property value="cmSortName"/>';
	$('#'+sortName+'Sort').attr("checked", true);
	Zeta$('<s:property value="cmSortName"/>Sort').checked = true;
}
//打印预览
function onPrintviewClick() {
	var wnd = window.open();
    doc = wnd.document;
    var formatHtml = document.getElementById('pageview').innerHTML.replace(/href="#"/g, '');
    formatHtml = formatHtml.replace(/onclick="([^"]*)"/g, '');
    doc.write('<!DOCTYPE html>');
    doc.write('<html>');
    doc.write('<head>');
    doc.write('<title>@report.cmDeviceList@</title>');
    doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
    doc.write('<link rel="stylesheet" type="text/css" href="/css/reportTemplate.css"/>');
    doc.write('</head>');
    doc.write('<body style="margin:50px auto;"><div id="wrapper"><div id="resultContainer">');
    doc.write(formatHtml);
    doc.write('</div></div></body>');
    doc.write('</html>');
    doc.close();
    return wnd;
}

//打印
function onPrintClick() {
	onPrintviewClick().print();
}
//点击选项
function optionClick() {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}
//查询按钮
function queryClick() {
    var range = $("#rangeSelect").val();
    var rangeDetail = "";
	if(range!="all"){
		rangeDetail = $('#'+range).val();
	}
	
	if(range!="all" && (rangeDetail=="" || rangeDetail==null)){
		window.top.showMessageDlg("@COMMON.tip@", '@tip.pleaseSelectOne@');
		return false;
	}
	
	queryForm.action = '/cmc/report/showCmDeviceAsset.tv?range='+range+'&rangeDetail='+rangeDetail;
	queryForm.target = "_self";
	queryForm.submit();
}
//导出EXCEL
function exportExcelClick() {
	var cmSortName = '<s:property value="cmSortName"/>';
	var cmAliasDisable = '<s:property value="cmAliasDisable"/>';
	var cmClassifiedDisable = '<s:property value="cmClassifiedDisable"/>';
    var range = $("#rangeSelect").val();
    var rangeDetail = "";
	if(range!="all"){
		rangeDetail = $('#'+range).val();
	}
	window.location.href="/cmc/report/exportCmDeviceReportToExcel.tv?cmSortName="+cmSortName
	        +"&cmAliasDisable="+cmAliasDisable+"&cmClassifiedDisable="+cmClassifiedDisable
	        +"&range="+range+"&rangeDetail="+rangeDetail;
}
$(function(){
	new Ext.SplitButton({
		renderTo : "topPutExportBtn",
		text : "@BUTTON.export@",
		handler : function(){this.showMenu()},
		iconCls : "bmenu_exportWithSub",
		menu : new Ext.menu.Menu({
			items : [{text : "EXCEL",   handler : exportExcelClick}]
		})
	});
	new Ext.SplitButton({
		renderTo : "bottomPutExportBtn",
		text : "@BUTTON.export@",
		handler : function(){this.showMenu()},
		iconCls : "bmenu_exportWithSub",
		menu : new Ext.menu.Menu({
			items : [{text : "EXCEL",   handler : exportExcelClick}]
		})
	});
	
	//构建查询下拉菜单
	for(var key in rangeList){
		$('<select class="rangeSelect normalSel" id="'+key+'"></select>').hide().appendTo($("#rangeTd"));
		$.each(rangeList[key], function(index, range){
			$('<option value="'+range.id+'">'+range.name+'</option>').appendTo($("#"+key));
		})
	}
	
	$("#rangeSelect").val(range);
	$("#rangeSelect").trigger("onchange");
	$(".rangeSelect").val(rangeDetail);
});//end document.ready;

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
<style type="text/css">
.reportHeader{
	font-size:12px;
	background-color: #DEDFDE;
	text-align: center;
}
.nowarp td,.nowarp th, { white-space:nowrap; word-break:keep-all; }
.nowarp td{
	padding: 0px 5px;
	text-align: center;
}
</style>
</head>
<body class=whiteToBlack onload="doOnload();">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	
	<!-- 查询区域 -->
	<div id="queryDiv" class="queryDiv zebraTableCaption" style="display: none;">
		<div class="zebraTableCaptionTitle"><span>@report/tip.queryCondition@</span></div>
		<form id="queryForm" name="queryForm" action="" method="post">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td width="200" class="rightBlueTxt">@report.sortInfo@:</td>
					<td>
						<div class="floatLeftDiv">
							<input type=radio id="statusInetAddressSort" name="cmSortName" value="statusInetAddress" /> 
							<label for="statusInetAddress">IP</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="statusMacAddressSort" name="cmSortName" value="statusMacAddress" /> 
							<label for="statusMacAddress">@label.mac@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="statusValueSort" name="cmSortName" value="statusValue" checked="checked"/> 
							<label for="statusValue">@report.status@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="cmcAliasSort" name="cmSortName" value="cmcAlias" /> 
							<label for="cmcAlias">@report.ccmtsAlias@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="cmAliasSort" name="cmSortName" value="cmAlias" /> 
							<label for="cmAlias">@report.aliasOrLocation@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="cmClassifiedSort" name="cmSortName" value="cmClassified" /> 
							<label for="cmClassified">@report.usageOrCat@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.displayFields@:</td>
					<td>
						<div class="floatLeftDiv">
							<input disabled="disabled" type="checkbox" <s:if test="ipDisable">checked</s:if> 
								id="ipColumn" name="ipDisable" value="true" /> 
							<label for="ipColumn">IP</label>
						</div>
						<div class="floatLeftDiv">
							<input disabled="disabled" type="checkbox" <s:if test="macDisable">checked</s:if> 
								id="macColumn" name="macDisable" value="true" /> 
							<label for="macColumn">@label.mac@</label>
						</div>
						<div class="floatLeftDiv">
							<input disabled="disabled" type="checkbox" <s:if test="statusDisable">checked</s:if> 
								id="statusColumn" name="statusDisable" value="true" /> 
							<label for="statusColumn">@report.status@</label>
						</div>
						<div class="floatLeftDiv">
							<input disabled="disabled" type="checkbox" <s:if test="ccDisable">checked</s:if> 
								id="ccColumn" name="ccDisable" value="true" /> 
							<label for="ccColumn">@report.ccmtsAlias@</label>
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" <s:if test="cmAliasDisable">checked</s:if> id="cmAliasColumn"
								name="cmAliasDisable" value="true" /> 
							<label for="cmAliasColumn">@report.aliasOrLocation@</label>
						</div>
						<div class="floatLeftDiv">
							<input type="checkbox" <s:if test="cmClassifiedDisable">checked</s:if>
								id="cmClassifiedColumn" name="cmClassifiedDisable" value="true" />
							<label for="cmClassifiedColumn">@report.usageOrCat@</label>
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
							<option value="cmts">CMTS</option>
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
					<td></td>
					<td>
						<a style="float: left;" href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@report.search@</span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.cmDeviceList@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<h4>@label.device.list@ <span style="color:#ff0000;">@report.pageMaxNum@</span></h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th class="">#</th>
					<th class="nowrap">IP</th>
					<th class="nowrap">@label.mac@</th>
					<th class="nowrap">@report.status@</th>
					<th class="">@report.ccmtsAlias@</th>
					<s:if test="cmAliasDisable">
						<th class="">@report.aliasOrLocation@</th>
					</s:if>
					<s:if test="cmClassifiedDisable">
						<th class="">@report.usageOrCat@</th>
					</s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="cmAttributes" status="statu">
					<tr>
						<td><s:property value="#statu.index+1" /></td>
						<td class="nowrap"><s:property value="statusInetAddress" /></td>
						<td class="nowrap"><s:property value="statusMacAddress" /></td>
						<td class="nowrap"><s:property value="statusVlaueString" /></td>
						<td><s:property value="cmcAlias" /></td>
						<s:if test="cmAliasDisable">
							<td><s:property value="cmAlias" /></td>
						</s:if>
						<s:if test="cmClassifiedDisable">
							<td><s:property value="cmClassified" /></td>
						</s:if>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>