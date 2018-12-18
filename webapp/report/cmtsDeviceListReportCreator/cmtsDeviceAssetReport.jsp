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
    module cmc
	css css.report
	import report.javascript.CommonMethod
</Zeta:Loader>
<script>
var queryVisible = false;
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	Zeta$('<s:property value="cmtsSortName"/>Sort').checked = true;
}
//设备类型全选
function onSelectAllClick(box) {
	var types = document.getElementsByName('totalTypes');
	for (var i = 0; i < types.length; i++) {
		types[i].checked = box.checked;
	}
}
//查询按钮
function queryClick() {
	queryForm.action = '/cmts/report/showCmtsDeviceAsset.tv';
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
}
//点击选项
function optionClick() {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}

function exportExcelClick() {
	var cmtsSortName = '<s:property value="cmtsSortName"/>';
	var locationDisplayable = '<s:property value="locationDisplayable"/>';
	var dutyDisplayable = '<s:property value="dutyDisplayable"/>';
	var createTimeDisplayable = '<s:property value="createTimeDisplayable"/>';
	window.location.href="/cmts/report/exportCmtsAssetToExcel.tv?cmtsSortName="+cmtsSortName+"&locationDisplayable="
	        +locationDisplayable+"&dutyDisplayable="+dutyDisplayable+"&createTimeDisplayable="+createTimeDisplayable; 
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
<body class=whiteToBlack onload="doOnload()">
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
					<td width="200" class="rightBlueTxt">@CCMTS.report.sortWith@:</td>
					<td>
						<div class="floatLeftDiv">
							<input type=radio id="nmNameSort" name="cmtsSortName" value="nmName" />
							<label for="nmNameSort">@CMC.label.deviceName@</label>
						</div>
						<div class="floatLeftDiv">
							<input type=radio id="cmcDeviceStyleSort" name="cmtsSortName" value="cmcDeviceStyle" />
							<label for="cmcDeviceStyleSort">@CCMTS.entityStyle@</label>
						</div>
						<div class="floatLeftDiv">
							<input style="display: none" type=radio	id="ipAddressSort" name="cmtsSortName" value="ipAddress" />
							<label for="ipAddressSort" style="display: none">@resources/COMMON.ip@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CCMTS.report.displayWith@:</td>
					<td>
						<div class="floatLeftDiv">
							<input <s:if test="nameDisplayable">checked</s:if> disabled="disabled" id="nameDisplayable" name="nameDisplayable" type=checkbox value="true" />
							<label for="nameDisplayable">@CMC.label.deviceName@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="ipDisplayable">checked</s:if> disabled="disabled" id="ipColumn" name="ipDisplayable" type=checkbox value="true" />
							<label for="ipColumn">@resources/COMMON.ip@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="typeDisplayable">checked</s:if> disabled="disabled" id="typeColumn" name="typeDisplayable" type=checkbox value="true" />
							<label for="typeColumn">@CCMTS.entityStyle@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="locationDisplayable">checked</s:if> id="locationColumn" name="locationDisplayable" type=checkbox	value="true" />
							<label for="locationColumn">@CCMTS.entityLocation@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="dutyDisplayable">checked</s:if> id="dutyColumn" name="dutyDisplayable" type=checkbox	value="true" />
							<label for="dutyColumn">@CCMTS.contactPerson@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="createTimeDisplayable">checked</s:if> id="createTimeColumn" name="createTimeDisplayable" type=checkbox value="true" />
							<label for="createTimeColumn">@report/report.createTime@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<a style="float: left;" href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@resources/COMMON.query@</span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report/report.cmtsDeviceListReport@</h1>
		<h3 id="queryTime"><s:date name="statDate"	format="yyyy-MM-dd HH:mm:ss" /></h3>
		<h4>@network/NETWORK.entityList@</h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th><strong>#</strong></th>
					<s:if test="nameDisplayable">
						<th>@CMC.label.alias@</th>
					</s:if>
					<s:if test="ipDisplayable">
						<th class="nowrap">@resources/COMMON.ip@</th>
					</s:if>
					<s:if test="typeDisplayable">
						<th class="nowrap">@CCMTS.entityStyle@</th>
					</s:if>
					<s:if test="locationDisplayable">
						<th>@CCMTS.entityLocation@</th>
					</s:if>
					<s:if test="dutyDisplayable">
						<th>@CCMTS.contactPerson@</th>
					</s:if>
					<s:if test="createTimeDisplayable">
						<th>@report/report.createTime@</th>
					</s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="deviceListItems" status="status">
					<tr>
						<td><s:property value="#status.index+1" /></td>
						<s:if test="nameDisplayable">
							<td><s:property value="nmName" /></td>
						</s:if>
						<s:if test="ipDisplayable">
							<td class="nowrap"><s:property value="ipAddress" /></td>
						</s:if>
						<s:if test="typeDisplayable">
							<td class="nowrap"><s:property value="cmcDeviceStyleString" /></td>
						</s:if>
						<s:if test="locationDisplayable">
							<td><s:property value="topCcmtsSysLocation" /></td>
						</s:if>
						<s:if test="dutyDisplayable">
							<td><s:property value="topCcmtsSysContact" /></td>
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
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>