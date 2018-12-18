<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css css.report
	library ext
	library jquery
	library zeta
	plugin DateTimeField
    module report
    import report.javascript.CommonMethod
</Zeta:Loader>
<style type="text/css">
</style>

<script type="text/javascript">
var eponSelected = ${eponSelected};
var ccmtsSelected = ${ccmtsSelected};

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
//生成EXCEL
function exportExcelClick() {
	var stTime = '<s:property value="stTime"/>';
	var etTime = '<s:property value="etTime"/>';
	var entityId = '<s:property value="entityId"/>';
	var folderId = '<s:property value="folderId"/>';
	var alarmTypeId = '<s:property value="alarmTypeId"/>';
	window.location.href="/epon/report/exportCurrentDetailToExcel.tv?stTime=" + stTime + "&etTime=" + etTime +  "&entityId=" + entityId + "&folderId=" + folderId + "&alarmTypeId=" + alarmTypeId
	        +"&eponSelected=" + eponSelected + "&ccmtsSelected=" + ccmtsSelected;  
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
})
</script>
</head>
<body class="whiteToBlack">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button> --%>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.currentDetailReport@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<h4><span style="color:#ff0000;">@report.pageMaxNum@</span></h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
				<!-- 标题行 -->
			<thead>
				<tr>
					<th>#</th>
					<th>@report.alarmDescr@</th>
					<th>@report.alertType@</th>
					<th>@report.relateDevice@</th>
					<th>@report.lastTime@</th>
					<th>@report.queryStatus@</th>
					<th>@report.queryUser@</th>
					<th>@report.queryTime@</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="currentAlarmDetails" status="statu">
					<tr>
						<td><s:property value="#statu.index+1" /></td>
						<td><s:property value="message" /></td>
						<td><s:property value="displayName" /></td>
						<td><s:property value="entityName" /></td>
						<td><s:property value="lastTimeStr" /></td>
						<td><s:property value="statusStr" /></td>
						<td><s:property value="confirmUser" /></td>
						<td><s:property value="confirmTimeStr" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button> --%>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>