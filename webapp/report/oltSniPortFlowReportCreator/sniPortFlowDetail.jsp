<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.epon.report.domain.OltSniPortFlowDetail" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css css.report
	library ext
	library Jquery
	library Zeta
    module report
    import report.javascript.CommonMethod
</Zeta:Loader>
<script type="text/javascript">
function optionClick(obj) {
	var el = Zeta$('querybar');
	if(el.style.display == 'none') {
		el.style.display = 'block';
	} else {
		el.style.display = 'none';
	}	
	document.body.scrollTop = 0;
}

function timeChange(){
	var value = $("#reportCycle").val();
	if(value == 0){
		$("#personalTime").show();
	}else{
		$("#personalTime").hide();
	}
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

function exportExcelClick() {
	var stTime = '<s:property value="stTime"/>';
	var etTime = '<s:property value="etTime"/>';
	var entityId = '<s:property value="entityId"/>';
	var rangeStart = '<s:property value="rangeStart"/>';
	var rangeEnd = '<s:property value="rangeEnd"/>';
	window.location.href = "/epon/report/exportSniFlowDetailToExcel.tv?stTime=" + stTime + "&etTime=" + etTime+"&entityId="+entityId+"&rangeStart="+rangeStart+"&rangeEnd="+rangeEnd;
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
<style type="text/css">
.L0{color: blue;font-family: Arial;}
.L1{color: #42b86e;font-family: Arial;}
.L2{color: #00a0dc;font-family: Arial;}
.L3{color: #ff6600;font-family: Arial;}
.L4{color: #ff0000;font-family: Arial;}
.L5{color: #de00ff;font-family: Arial;}
.counter{font-weight: bold;}
</style>
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
		<h1>@report.sniFlowDetail@</h1>
		<h3 id="queryTime"><s:date name="statDate"	format="yyyy-MM-dd HH:mm:ss" /></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p style="font-size: 14px;">
				<span>@report.statRange@:</span>
				<span id="statisRange">${stTime} @report.to@   ${etTime}</span>
			</p>
			<p>
				<span>@report.statMode@:</span>
				<span id="statisRange">@report.baseOnEntity@</span>
			</p>
		</div>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th>#</th>
					<th>@report.portLocation@</th>
					<th>@report.belongOlt@</th>
					<th>@report.maxUsage@(%)</th>
					<th>@report.maxRecvRate@(Mbps)</th>
					<th>@report.collectTime@</th>
				</tr>
			</thead>
			<tbody>
				<%
					List<OltSniPortFlowDetail> list = (List<OltSniPortFlowDetail>)request.getAttribute("oltSniPortFlowDetail");
				 	for(int i=0;i<list.size();i++){
				 	   OltSniPortFlowDetail stastic = list.get(i);
				 	%><tr class="contentRow">
				 		<td><%= i+1 %></td>
						<td><%= stastic.getPortName() %></td>
						<td><%= stastic.getName()%></td>
						<td><%= stastic.getUsage()%></td>
						<td><%= stastic.getFlowDisplay()%></td>
						<td><%= stastic.getCollectTimeString()%></td>
					</tr>
					<!-- 小计栏 -->
				 <% }  %>
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