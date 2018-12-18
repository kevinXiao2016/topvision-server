<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChannelSnrAvg" %>
<%@ page import="com.topvision.ems.facade.domain.Entity" %>
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
	library Ext
	library Jquery
	library Zeta
	plugin DateTimeField
    module report
    import report.javascript.CommonMethod
</Zeta:Loader>
<style type="text/css">
</style>
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
//无用方法
function doOnAfterPrint() {
	Zeta$('header').style.display = 'block';
	Zeta$('footer').style.display = 'block';
	if(queryVisible) {
		Zeta$('querybar').style.display= 'block'
	}
	Zeta$('pageview').style.border = "solid 1px black";
}
function doOnBeforePrint() {
	Zeta$('header').style.display = 'none';
	Zeta$('footer').style.display = 'none';
	Zeta$('pageview').style.border = 0;
	var el = Zeta$('querybar');
	queryVisible = (el.style.display != 'none');
	el.style.display= 'none'
}

Ext.onReady(function(){
	window.top.closeWaitingDlg()
	/* window.stField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${stTime}',
		renderTo:"stTime"
	});
	window.etField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${etTime}',
		renderTo:"etTime"
	}); */
});

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

function exportExcelClick() {
	var stTime = $("#stTime").val();
	var etTime =  $("#etTime").val();
	var entityId =  $("#entityId").val();
	window.location.href="/cmc/report/exportUserFlowDetailReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime + "&entityId=" + entityId; 
}
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
	<input type="hidden" id="stTime" value="${stTime}" />
	<input type="hidden" id="etTime" value="${etTime}" />
	<input type="hidden" id="entityId" value="${entityId}" />
	
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
		<h1>@report.ccmtsUserDetail@</h1>
		<h3 id="queryTime"><s:date name="statDate"	format="yyyy-MM-dd HH:mm:ss" /></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p style="font-size: 14px;">
				<span>@report.statRange@:</span>
				<span id="statisRange">
					<s:if test="stTime != null">
						${stTime} @report.to@   ${etTime}
					</s:if>
					<s:else>@report.noSelect@</s:else>
				</span>
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
					<th>OLT</th>
					<th>@report.no@</th>
					<th>@report.pon@</th>
					<th width="60">@report.ponMaxSend@</th>
					<th>@report.ccmtsName@</th>
					<th>@report.deviceType@</th>
					<!-- <th>@report.dsFlow@</th> -->
					<th width="30">@report.interactiveCmNum@</th>
					<th width="30">@report.broadbandCmNum@</th>
					<th width="30">@report.cmTotal@</th>
					<th width="30">@report.cpeInteractiveNum@</th>
					<th width="30">@report.cpeBroadbandNum@</th>
					<th width="30">@report.cpeNum@</th>
					<th width="60">@report.channel1@</th>
					<th width="60">@report.channel2@</th>
					<th width="60">@report.channel3@</th>
					<th width="60">@report.channel4@</th>
				</tr>
			</thead>
			<tbody>
				<%
					Map<String, List<CmcUserFlowReportDetail>> statSniFlowReport 
						= (Map<String, List<CmcUserFlowReportDetail>>)request.getAttribute("details");
					Entity entity = (Entity)request.getAttribute("entity");
					
					int totalInteractiveNum = 0;
					int totalBroadbandNum = 0;
					int totalCmNum = 0;
					int totalCpeInteractiveNum = 0;
					int totalCpeBroadbandNum = 0;
					int totalCpeNumTotal = 0;
					int counter=0;
					List<Long> calculatedEntity = new ArrayList<Long>();
				 	for(String ponName : statSniFlowReport.keySet()){
				 	   List<CmcUserFlowReportDetail> list = statSniFlowReport.get(ponName);
				 	   if(list == null){
				 	       //TODO nothing need to do
				 	   }else{
				     	 int ccmtsNum =0;
				     	 int peakOnlineNum =0;
				     	 int userNum =0;
				 	     for(int i=0;i<list.size();i++){
				 	        counter++;
				 	        CmcUserFlowReportDetail stastic = list.get(i);
				 	        totalInteractiveNum += stastic.getInteractiveNum();
				 	        totalBroadbandNum += stastic.getBroadbandNum();
				 	        totalCmNum += stastic.getCmNumTotal();
				 	        totalCpeInteractiveNum += stastic.getCpeInteractiveNum();
				 	        totalCpeBroadbandNum += stastic.getCpeBroadbandNum();
				 	        totalCpeNumTotal += stastic.getCpeNumTotal();
				 	%><tr class="contentRow">
				 		<% if(counter==1){ %><td id="olt" rowspan="<%=counter+1%>"><%=entity.getName() %><br>(<%= entity.getIp() %>)</td><%} %>
				 		<td><%= counter %></td>
						<% if(i==0){ %>
							<td rowspan="<%=list.size() %>"><%= ponName %></td>
							<td rowspan="<%=list.size() %>"><%= stastic.getPonMaxSendString()%></td>
						<%}%>
						<td align="center" ><%= stastic.getName()%></td>
						<td align="center" ><%= stastic.getTypeName()%></td>
						<%-- <td align="center" ><%= stastic.getCcmtsMaxSendString()%></td> --%>
						<td align="center" ><%= stastic.getInteractiveNum()%></td>
						<td align="center" ><%= stastic.getBroadbandNum()%></td>
						<td align="center" ><%= stastic.getCmNumTotal()%></td>
						<td align="center" ><%= stastic.getCpeInteractiveNum()%></td>
						<td align="center" ><%= stastic.getCpeBroadbandNum()%></td>
						<td align="center" ><%= stastic.getCpeNumTotal()%></td>
						<% for(CcmtsChannelSnrAvg avg : stastic.getSnrAvgs()){ 
							if(avg == null){%>
								<td align="center">-</td>
							<%}else{ %>
								<td align="center"><%=avg.getSnrDisplay() %></td>
						<%}}} %>
							
					</tr>
					<!-- 小计栏 -->
				 <% } } %>
				 <tr>
					<td>@report.totalling@</td>
			 		<td><%=counter %></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<!-- <td align="center"></td> -->
					
					<td align="center"><%=totalInteractiveNum %></td>
					<td align="center"><%=totalBroadbandNum %></td>
					<td align="center"><%=totalCmNum %></td>
					<td align="center"><%=totalCpeInteractiveNum %></td>
					<td align="center"><%=totalCpeBroadbandNum %></td>
					<td align="center"><%=totalCpeNumTotal %></td>
					
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
					<td align="center"></td>
				</tr>
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
<script type="text/javascript">
	$("#olt").attr("rowspan","<%=counter%>")
</script>
</body>
</Zeta:HTML>