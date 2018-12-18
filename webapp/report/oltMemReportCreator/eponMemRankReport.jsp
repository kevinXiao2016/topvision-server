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
    module epon
	css css.report
	import report.javascript.CommonMethod
</Zeta:Loader>
<script>
var queryVisible = false;
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	Zeta$('ipSort').checked = true;
}
//查询按钮
function queryClick() {
	queryForm.action = '/epon/report/showEponMemRankReport.tv';
	queryForm.target = "_self";
	queryForm.submit();
}
//打印预览
function onPrintviewClick() {
	showPrintWnd(Zeta$('pageview'));
}

//打印
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(Zeta$('pageview'), wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
	//window.print();
}
//点击选项
function optionClick() {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}
//导出EXCEL
function exportExcelClick() {
	window.location.href="/epon/report/exportMenRankReportToExcel.tv"; 
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
});//end document.ready;
</script>
</head>
<body class="whiteToBlack" onload="doOnload()">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="queryClick()" icon="miniIcoManager">@COMMON.query@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	
	<!-- 查询区域 -->
	<div id="queryDiv" class="queryDiv zebraTableCaption" style="display: none;">
		<div class="zebraTableCaptionTitle"><span>@report/tip.queryCondition@</span></div>
		<form id="queryForm" name="queryForm" action="" method="post">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td width="200" class="rightBlueTxt">@report.selectDeviceSortType@:</td>
					<td>
						<div class="floatLeftDiv">
							<input type=radio id="ipSort" name="sortName" value="ip" disabled = "disabled" />
							<label for="ipSort">@report.memUse@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.propertyMode@:</td>
					<td>
						<div class="floatLeftDiv">
							<input <s:if test="ipDisplayable">checked</s:if> disabled="disabled" id="ipDisplayable" name="ipDisplayable" type=checkbox value="true" />
							<label for="ipDisplayable" >@report.deviceIp@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="nameDisplayable">checked</s:if> disabled = "disabled" id="nameDisplayable" name="nameDisplayable" type=checkbox value="true" />
							<label for="nameDisplayable">@config.basicInfo.alias@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="delayDisplayable">checked</s:if> disabled = "disabled" id="memDisplayable" name="memDisplayable" type=checkbox value="true" />
							<label for="memDisplayable">@report.memUse@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="snapTimeDisplayable">checked</s:if> disabled = "disabled" id="snapTimeDisplayable" name="snapTimeDisplayable" type=checkbox value="true" />
							<label for="snapTimeDisplayable">@report/report.collectTime@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<a style="float: left;" href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.memUseTop10@</h1>
		<h3 id="queryTime"><s:date name="statDate"	format="yyyy-MM-dd HH:mm:ss" /></h3>
		<h4>@network/NETWORK.entityList@</h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<s:if test="ipDisplayable">
						<th class="nowrap">@report.deviceIp@</th>
					</s:if>
					<s:if test="nameDisplayable">
						<th>@config.basicInfo.alias@</th>
					</s:if>
					<s:if test="memDisplayable">
						<th class="nowrap">@report.memUse@</th>
					</s:if>
					<s:if test="snapTimeDisplayable">
						<th>@report/report.collectTime@</th>
					</s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="performanceRank">
					<tr>
						<s:if test="ipDisplayable">
							<td class="nowrap"><s:property value="ip" /></td>
						</s:if>
						<s:if test="nameDisplayable">
							<td><s:property value="name" /></td>
						</s:if>
						<s:if test="memDisplayable">
							<td class="nowrap"><s:property value="MEM_p" /></td>
						</s:if>
						<s:if test="snapTimeDisplayable">
							<td><s:date name="snapTime"
									format="yyyy-MM-dd HH:mm:ss" /></td>
						</s:if>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="queryClick()" icon="miniIcoManager">@COMMON.query@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>
