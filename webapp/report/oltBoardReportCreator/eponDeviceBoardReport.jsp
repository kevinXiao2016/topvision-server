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
	import report.javascript.CommonMethod
    module epon
	css css.report
</Zeta:Loader>
<style type="text/css">
.floatLeftDiv input, .floatLeftDiv label{ float:left;}
</style>
<script>
var queryVisible = false;
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	Zeta$('ipSort').checked = true;
}
//查询按钮
function queryClick() {
	queryForm.action = '/epon/report/showEponBoardReport.tv';
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

function exportExcelClick() {
	var mpuaDisplayable = '<s:property value="mpuaDisplayable"/>';
	var mpubDisplayable = '<s:property value="mpubDisplayable"/>';
	var geuaDisplayable = '<s:property value="geuaDisplayable"/>';
	var geubDisplayable = '<s:property value="geubDisplayable"/>';
	var xguaDisplayable = '<s:property value="xguaDisplayable"/>';
	var xgubDisplayable = '<s:property value="xgubDisplayable"/>';
	var xgucDisplayable = '<s:property value="xgucDisplayable"/>';
	var epuaDisplayable = '<s:property value="epuaDisplayable"/>';
	var epubDisplayable = '<s:property value="epubDisplayable"/>';
	window.location.href="/epon/report/exportDeviceBoardToExcel.tv?mpuaDisplayable=" + mpuaDisplayable
			+ "&mpubDisplayable=" + mpubDisplayable
	        + "&geuaDisplayable=" + geuaDisplayable
	        + "&geubDisplayable=" + geubDisplayable
	        + "&xguaDisplayable=" + xguaDisplayable
	        + "&xgubDisplayable=" + xgubDisplayable
	        + "&xgucDisplayable=" + xgucDisplayable
	        + "&epuaDisplayable=" + epuaDisplayable
	        + "&epubDisplayable=" + epubDisplayable; 
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
						<td width="200" class="rightBlueTxt">@report.selectDeviceSortType@:</td>
						<td>
							<div class="floatLeftDiv">
								<input type=radio id="ipSort" name="sortName" value="ip" disabled = "disabled" />
								<label for="ipSort">@report.deviceIp@</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@report.propertyMode@:</td>
						<td>
							<div class="floatLeftDiv">
								<input <s:if test="ipDisplayable">checked</s:if> disabled="disabled" 
									id="ipDisplayable" name="ipDisplayable" type=checkbox value="true" />
								<label for="ipDisplayable">@report.deviceIp@</label>
							</div>
							<div class="floatLeftDiv">
								<input disabled="disabled" checked="checked" type=checkbox value="true" />
								<label>@report.deviceName@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="allSlotDisplayable">checked</s:if> disabled = "disabled"
									id="allSlotDisplayable" name="allSlotDisplayable"
									type=checkbox value="true" />
								<label for="allSlotDisplayable">@report.totalBoard@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="onLineDisplayable">checked</s:if>
									disabled = "disabled" id="onLineDisplayable" name="onLineDisplayable"
									type=checkbox value="true" />
								<label for="onLineDisplayable">@report.installBoard@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="mpuaDisplayable">checked</s:if>
									id="mpuaDisplayable" name="mpuaDisplayable" type=checkbox
									value="true" />
								<label for="mpuaDisplayable">MPUA</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="mpubDisplayable">checked</s:if>
									id="mpubDisplayable" name="mpubDisplayable" type=checkbox
									value="true" />
								<label for="mpubDisplayable">MPUB</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="geuaDisplayable">checked</s:if>
									id="geuaDisplayable" name="geuaDisplayable" type=checkbox
									value="true" />
								<label for="geuaDisplayable">GEUA</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="geubDisplayable">checked</s:if>
									id="geubDisplayable" name="geubDisplayable" type=checkbox
									value="true" />
								<label for="geubDisplayable">GEUB</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="xguaDisplayable">checked</s:if>
									id="xguaDisplayable" name="xguaDisplayable" type=checkbox
									value="true" />
								<label for="xguaDisplayable">XGUA</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="xgubDisplayable">checked</s:if>
									id="xgubDisplayable" name="xgubDisplayable" type=checkbox
									value="true" />
								<label for="xgubDisplayable">XGUB</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="xgucDisplayable">checked</s:if>
									id="xgucDisplayable" name="xgucDisplayable" type=checkbox
									value="true" />
								<label for="xgucDisplayable">XGUC</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="epuaDisplayable">checked</s:if>
									id="epuaDisplayable" name="epuaDisplayable" type=checkbox
									value="true" />
								<label for="epuaDisplayable">EPUA</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="epubDisplayable">checked</s:if>
									id="epubDisplayable" name="epubDisplayable" type=checkbox
									value="true" />
								<label for="epubDisplayable">EPUB</label>
							</div>
							<div class="floatLeftDiv">
							<input <s:if test="xpuaDisplayable">checked</s:if>
								id="xpuaDisplayable" name="xpuaDisplayable" type=checkbox
								value="true" />
							<label for="xpuaDisplayable">XPUA</label>
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
		<h1>@report.oltBoardUse@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<s:if test="ipDisplayable">
						<th align="center" class="nowrap"><strong>@report.deviceIp@</strong></th>
					</s:if>
					<th align="center"><strong>@report.deviceName@</strong></th>
					<s:if test="allSlotDisplayable">
						<th align="center"><strong>@report.totalBoard@</strong></th>
					</s:if>
					<s:if test="onLineDisplayable">
						<th align="center"><strong>@report.installBoard@</strong></th>
					</s:if>
					<s:if test="mpuaDisplayable">
						<th align="center"><strong>@report.mpuBoardNum@</strong></th>
					</s:if>
					<s:if test="mpubDisplayable">
						<th align="center"><strong>@report.mpubBoardNum@</strong></th>
					</s:if>
					<s:if test="geuaDisplayable">
						<th align="center"><strong>@report.geuBoardNum@</strong></th>
					</s:if>
					<s:if test="geubDisplayable">
						<th align="center"><strong>@report.geubBoardNum@</strong></th>
					</s:if>
					<s:if test="xguaDisplayable">
						<th align="center"><strong>@report.xguaBoardNum@</strong></th>
					</s:if>
					<s:if test="xgubDisplayable">
						<th align="center"><strong>@report.xgubBoardNum@</strong></th>
					</s:if>
					<s:if test="xgucDisplayable">
						<th align="center"><strong>@report.xgucBoardNum@</strong></th>
					</s:if>
					<s:if test="epuaDisplayable">
						<th align="center"><strong>@report.epuBoardNum@</strong></th>
					</s:if>
					<s:if test="epubDisplayable">
						<th align="center"><strong>@report.epubBoardNum@</strong></th>
					</s:if>
					<s:if test="xpuaDisplayable">
					<th align="center"><strong>@report.xpuaBoardNum@</strong></th>
				</s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="eponBoardStatistics">
					<tr>
						<s:if test="ipDisplayable">
							<td class="nowrap"><s:property value="ip" /></td>
						</s:if>
						<td><s:property value="name" /></td>
						<s:if test="allSlotDisplayable">
							<td><s:property value="allSlot" /></td>
						</s:if>
						<s:if test="onLineDisplayable">
							<td><s:property value="online" /></td>
						</s:if>
						<s:if test="mpuaDisplayable">
							<td><s:property value="mpua" /></td>
						</s:if>
						<s:if test="mpubDisplayable">
							<td><s:property value="mpub" /></td>
						</s:if>
						<s:if test="geuaDisplayable">
							<td><s:property value="geua" /></td>
						</s:if>
						<s:if test="geubDisplayable">
							<td><s:property value="geub" /></td>
						</s:if>
						<s:if test="xguaDisplayable">
							<td><s:property value="xgua" /></td>
						</s:if>
						<s:if test="xgubDisplayable">
							<td><s:property value="xgub" /></td>
						</s:if>
						<s:if test="xgucDisplayable">
							<td><s:property value="xguc" /></td>
						</s:if>
						<s:if test="epuaDisplayable">
							<td><s:property value="epua" /></td>
						</s:if>
						<s:if test="epubDisplayable">
							<td><s:property value="epub" /></td>
						</s:if>
						<s:if test="xpuaDisplayable">
							<td><s:property value="xpua" /></td>
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
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>