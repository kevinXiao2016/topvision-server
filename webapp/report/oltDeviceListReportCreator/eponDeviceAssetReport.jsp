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
<script>
var queryVisible = false;
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	Zeta$('<s:property value="oltSortName"/>Sort').checked = true;
}
//查询按钮
function queryClick() {
	queryForm.action = '/epon/report/showEponDeviceAsset.tv';
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
	var oltSortName = '<s:property value="oltSortName"/>';
	var locationDisplayable = '<s:property value="locationDisplayable"/>';
	var createTimeDisplayable = '<s:property value="createTimeDisplayable"/>';
	window.location.href="/epon/report/exportEponDeviceAssetToExcel.tv?oltSortName=" + oltSortName + "&locationDisplayable=" + locationDisplayable+ "&createTimeDisplayable=" + createTimeDisplayable; 
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
					<td width="200" class="rightBlueTxt">@report.sortMode@:</td>
					<td>
						<div class="floatLeftDiv">
							<input type=radio id="nameSort" name="oltSortName" value="name" />
							<label for="nameSort">@report.name@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="ipSort" name="oltSortName" value="ip" />
							<label for="ipSort">@report.ip@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="typeNameSort" name="oltSortName" value="typeName" />
							<label for="typeSort">@report.type@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.propertyMode@:</td>
					<td>
						<div class="floatLeftDiv">
							<input <s:if test="nameDisplayable">checked</s:if>
								disabled = "disabled" id="nameDisplayable" name="nameDisplayable" type=checkbox value="true" />
							<label for="nameDisplayable">@report.name@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="ipDisplayable">checked</s:if>
								disabled = "disabled" id="ipColumn" name="ipDisplayable" type=checkbox value="true" />
							<label for="ipColumn">@report.ip@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="typeDisplayable">checked</s:if>
								disabled = "disabled" id="typeColumn" name="typeDisplayable" type=checkbox value="true" />
							<label for="typeColumn">@report.type@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="locationDisplayable">checked</s:if>
								id="locationColumn" name="locationDisplayable" type=checkbox value="true" />
							<label for="locationColumn">@report.addr@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="createTimeDisplayable">checked</s:if>
								id="createTimeColumn" name="createTimeDisplayable" type=checkbox value="true" />
							<label for="createTimeColumn">@report.createTime@</label>
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
		<h1>@report.deviceAsset@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th>#</th>
					<th>@report.name@</th>
					<th class="nowrap">@report.ip@</th>
					<th class="nowrap">@report.type@</th>
					<s:if test="locationDisplayable">
						<th>@report.addr@</th>
					</s:if>
					<s:if test="createTimeDisplayable">
						<th>@report.createTime@</th>
					</s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="deviceListItems" status="statu">
					<tr>
						<td><s:property value="#statu.index+1" /></td>
						<td><s:property value="name" /></td>
						<td class="nowrap"><s:property value="ip" /></td>
						<td class="nowrap"><s:property value="typeName" /></td>
						<s:if test="locationDisplayable">
							<td><s:property value="sysLocation" /></td>
						</s:if>
						<s:if test="createTimeDisplayable">
							<td><s:property value="createTimeString" /></td>
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
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>
