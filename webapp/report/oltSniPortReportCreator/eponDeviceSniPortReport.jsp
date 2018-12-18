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
function doOnload() {
    $("#entityType").val(<s:property value="entityType"/>);
	$("#adminState").val(<s:property value="adminState"/>);
	$("#operationState").val(<s:property value="operationState"/>);
	$("#entityIp").val('<s:property value="entityIp"/>');
	
   	$("#entityType").find("option[value=10001]").remove();
}

function optionClick() {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}

function queryClick() {
	queryForm.action = '/epon/report/showEponSniPortReport.tv';
	queryForm.target = '_self';
	queryForm.submit();
}

function onPrintviewClick() {
	showPrintWnd(Zeta$('pageview'));
}

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
}

function exportExcelClick() {
	var entityType = '<s:property value="entityType"/>';
	var entityIp = '<s:property value="entityIp"/>';
	var adminState = '<s:property value="adminState"/>';
	var operationState = '<s:property value="operationState"/>';
	window.location.href="/epon/report/exportDeviceSniToExcel.tv?entityType=" + entityType+ "&entityIp=" + entityIp+ "&adminState=" + adminState+ "&operationState=" + operationState ; 
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
<body class="whiteToBlack" onload="doOnload();">
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
						<td width="200" class="rightBlueTxt">@report.deviceType@:</td>
						<td>
							<select id="entityType" name="entityType" style="width: 120px;">
								<option value="0">-- @report.selectType@ --</option>
								<s:iterator value="entityTypes">
									<option value="<s:property value="typeId"/>">
										<s:property value="displayName" />
									</option>
								</s:iterator>
							</select>
						</td>
						<td class="rightBlueTxt">@report.deviceIp@:</td>
						<td>
							<input name="entityIp" id="entityIp" type="text" style="width: 120px;" class="normalInput" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@report.adminStatus@:</td>
						<td>
							<select id="adminState" name="adminState" style="width: 120px;" class="normalSel">
								<option value="0">-- @report.selectStatus@ --</option>
								<option value="1">UP</option>
								<option value="2">DOWN</option>
							</select>
						</td>
						<td class="rightBlueTxt">@report.workStatus@:</td>
						<td>
							<select id="operationState" name="operationState" style="width: 120px;" class="normalSel">
								<option value="0">-- @report.selectStatus@ --</option>
								<option value="1">UP</option>
								<option value="2">DOWN</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"></td>
						<td colspan="3">
							<!-- <input type="button" class=BUTTON75 onclick="queryClick()" value="@COMMON.query@" /> -->
							<a href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.sniUse@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th class="nowrap">@report.device@</th>
					<th>@report.deviceName@</th>
					<th>@report.portNum@</th>
					<th>@report.portDesc@</th>
					<th>@report.class@</th>
					<th class="nowrap">@report.adminStatus@</th>
					<th class="nowrap">@report.workStatus@</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="snilist">
					<tr>
						<td class="nowrap"><s:property value="entityIp" /></td>
						<td><s:property value="entityName" /></td>
						<td><s:property value="sniPort" /></td>
						<td><s:property value="sniPortName" /></td>
						<td><s:property value="sniMediaTypeString" /></td>
						<td class="nowrap"><s:property value="sniAdminStatusString" /></td>
						<td class="nowrap"><s:property value="sniOperationStatusString" /></td>
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
