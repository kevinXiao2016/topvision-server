<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChannelReportLocation" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChannelReportOLT" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChannelReportPON" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChannelReportCCMTS" %>
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
	import report.javascript.CommonMethod
	library Zeta
	plugin DateTimeField
    module report
</Zeta:Loader>
<style type="text/css">
.reportHeader{
	align:center;
	text-align:center;
	font-size:12px;
	background-color: #DEDFDE;
}
.nowarp th,.nowarp td{
	padding-right: 2px;
	padding-left: 2px;
	text-align: center;
}
.nowarp td,.nowarp th, { white-space:nowrap; word-break:keep-all; }
.L0{color: blue;font-family: Arial;}
.L1{color: #42b86e;font-family: Arial;}
.L2{color: #00a0dc;font-family: Arial;}
.L3{color: #ff6600;font-family: Arial;}
.L4{color: #ff0000;font-family: Arial;}
.L5{color: #de00ff;font-family: Arial;}
.counter{font-weight: bold;}
</style>
<script type="text/javascript">
//刷新
function onRefresh(obj) {
	location.href = "/cmc/report/showCcmtsChannelAsset.tv";	
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

function formatDate(time){
	if(typeof time == 'string') time = parseDate(time);   
	if(time instanceof Date){   
		var y = time.getFullYear();  
		var m = time.getMonth() + 1;  
		var d = time.getDate();  
		var h = (time.getHours()<10) ? ('0' + time.getHours()) : time.getHours();  
		var i = (time.getMinutes()<10) ? ('0' + time.getMinutes()) : time.getMinutes();   
		var s = (time.getSeconds()<10) ? ('0' + time.getSeconds()) : time.getSeconds();   

		if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;  

		return y + '-' + m + '-' + d;   
	}
	return '';
}

Ext.onReady(function(){
	$("#queryTime").text(formatDate(new Date()));
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
});

function exportExcelClick() {
	window.location.href="/cmc/report/exprotCcmtsChannelReportToExcel.tv"; 
}
</script>
</head>
<body class="whiteToBlack">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="onRefresh()" icon="miniIcoRefresh">@COMMON.refresh@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.ccmtsChannelListReport@</h1>
		<h3 id="queryTime"></h3>
		<h4>@label.device.list@</h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th>@report.folder@</th>
					<th>@report.oltName@</th>
					<th class="nowrap">OLT_IP</th>
					<th>@report.pon@</th>
					<th>@report.ccmtsName@</th>
					<th class="nowrap">CC_MAC</th>
					<th>@report.ccmtsUpChannelNum@</th>
					<th>@report.ccmtsDownChannelNum@</th>
					<th>@report.ccmtsUpChannelModule@</th>
					<th>@report.ccmtsDownChannelModule@</th>
				</tr>
			</thead>
			<tbody>
				<%
				 	List<CcmtsChannelReportLocation> locations 
						= (List<CcmtsChannelReportLocation>)request.getAttribute("locations");
					%>
				<tr class="contentRow">
					<%
				 	for(int i=0; i<locations.size(); i++){
				 	 	String locationId = locations.get(i).getLocationId();
				 	 	String loactionName = locations.get(i).getLocationName();
				 	 	int locationRowSpan = locations.get(i).getRowSpan();
				 	 	List<CcmtsChannelReportOLT> olts = locations.get(i).getCcmtsChannelReportOLT();
				 	 %>
				 		<td rowspan = "<%=locationRowSpan %>"><%=loactionName %> </td>
				 	 <%
				 	     for(int j=0; j<olts.size(); j++){
				 	     	String oltId = olts.get(j).getOltId();
				 	    	String oltName = olts.get(j).getOltName();
				 	    	String oltIp = olts.get(j).getOltIp();
				 	    	int oltRowSpan = olts.get(j).getRowSpan();
				 	    	List<CcmtsChannelReportPON> pons = olts.get(j).getCcmtsChannelReportPON();
				 	    	%>
				 	    	<td rowspan = "<%=oltRowSpan %>"><%=oltName %></td>
				 	    	<td class="nowrap" rowspan = "<%=oltRowSpan %>"><%=oltIp %></td>
				 	    	<%
				 	    	for(int k=0; k<pons.size(); k++){
				 	    		String ponId = pons.get(k).getPonId();
				 	    		String ponIndex = pons.get(k).getPonIndex();
				 	    		int ponRowSpan = pons.get(k).getRowSpan();
				 	    		List<CcmtsChannelReportCCMTS> ccs = pons.get(k).getCcmtsChannelReportCCMTS();
				 	    		%>
				 	    		<td rowspan = "<%=ponRowSpan%>"><%=ponIndex%></td>
				 	    		<%
				 	    		for(int l=0; l<ccs.size(); l++){
				 	    			%>
				 	    			<td><%=ccs.get(l).getCcmtsName() %></td>
				 	    			<td class="nowrap"><%=ccs.get(l).getCcmtsMAC() %></td>
				 	    			<td><%=ccs.get(l).getCcmtsUpChannelNum() %></td>
				 	    			<td><%=ccs.get(l).getCcmtsDownChannelNum() %></td>
				 	    			<td><%=ccs.get(l).getCcmtsUpChannelModule() %></td>
				 	    			<td><%=ccs.get(l).getCcmtsDownChannelModule()%></td>
				 	    			</tr>
				 	    			<%
				 	    		}
				 	    	}
				 	    }
				 	}
				 	%>
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
			<Zeta:Button onClick="onRefresh()" icon="miniIcoRefresh">@COMMON.refresh@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>