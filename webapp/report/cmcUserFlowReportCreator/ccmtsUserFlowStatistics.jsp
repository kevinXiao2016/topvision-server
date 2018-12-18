<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CmcUserFlowReportStatistics" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CmcUserFlowPortValue" %>
<%@ page import="com.topvision.ems.report.util.NumberFormatterUtil" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Ext
	library Jquery
	library Zeta
	plugin DateTimeField
    module report
	css css.report
	import report.javascript.CommonMethod
	import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript">
function optionClick(obj) {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}

function timeChange(){
	var value = $("#reportCycle").val();
	if(value == 0){
		$("#personalTime").show();
	}else{
		$("#personalTime").hide();
	}
}

Ext.onReady(function(){
	window.top.closeWaitingDlg()
	window.stField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${stTime}',
		renderTo:"stTime",
		editable: false,
		emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@'
	});
	window.etField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${etTime}',
		renderTo:"etTime",
		editable: false,
		emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@'
	});
	nm3kPickData({
	    startTime : window.stField,
	    endTime : window.etField,
	    searchFunction : query
	});
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

function query(){
	var stTime = stField.value;
	if(stTime == null || stTime == ""){
		stField.focus();
		return;
	}
	var etTime = etField.value;
	if(etTime == null || etTime == ""){
		etField.focus();
		return;
	}
	if(stTime>etTime){
	    top.afterSaveOrDelete({
			title: '@report.tip@',
				html: '<b class="orangeTxt">@report.conflictPeriod@</b>'
   		});
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@report.stasticing@", 'ext-mb-waiting')
	location.href = "/cmc/report/showCcmtsUserFlowStatistics.tv?stTime=" + stTime + "&etTime="+etTime;	
}
function exportExcelClick() {
	var stTime = stField.value;
	var etTime = etField.value;
	window.location.href="/cmc/report/exportUserFlowReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime; 
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

function showDetail(entityId){
	var stTime = stField.value;
	var etTime = etField.value;
	window.top.addView("userFlowDetail"+Math.random(),"@report.ccmtsUserDetail@",null,"/cmc/report/showCcmtsUserFlowDetail.tv?stTime=" + stTime + "&etTime=" + etTime+"&entityId="+entityId);
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
#stTime, #etTime{ width:150px;}
#stTime .x-form-field-wrap, #etTime .x-form-field-wrap{width:auto !important;}
</style>
</head>
<body class="whiteToBlack">
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
					<td width="120" class="rightBlueTxt">@report.startTime@@report.maohao@</td>
					<td><div id="stTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.endTime@@report.maohao@</td>
					<td><div id="etTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.statMode@@report.maohao@</td>
					<td>
						<select id="statistical" class="normalSel normalSelDisabled" style="width: 150px;" disabled>
							<!-- TODO 设备类型的选择应该由DB控制 -->
							<option>@report.baseOnFolder@</option>
						</select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.ccmtsUserFlow@</h1>
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
				<span id="statisRange">@report.baseOnFolder@</span>
			</p>
		</div>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<%
									Map<String, Object> statSniFlowReport 
													= (Map<String, Object>)request.getAttribute("statCmcUserFlowReport");
									int totalCcmtsNum = 0;
									int totalPeakOnlineNum = 0;
									int totalUserNum = 0; 
									List<Long> calculatedEntity = new ArrayList<Long>();
									List<String> portIndexColumnList;
									if(statSniFlowReport.get("columns") != null){
										portIndexColumnList = (List<String>)statSniFlowReport.get("columns");
									}else{
									    portIndexColumnList = new ArrayList<String>();
									}
								%>
								<tr>
									<th class="reportHeader" rowspan="2">@report.folder@</th>
									<th class="reportHeader" rowspan="2">@report.entity@</th>
									<th class="reportHeader" rowspan="2">@report.ccmtsNum@</th>
									<th class="reportHeader" rowspan="2">@report.totalOnlineUser@</th>
									<th class="reportHeader" rowspan="2">@report.totalUser@</th>
									<th class="reportHeader" align="center"  colspan="<%= portIndexColumnList.size()+2 %>">@report.sniPeekSeg@</th>
								</tr>
								<tr>
									<% for(String column : portIndexColumnList ){ %>
										<th class="reportHeader"><%= column %></th>
									<%} %>
									<th class="reportHeader">@report.recvRate@</th>
									<th class="reportHeader">@report.bandwidthUsage@</th>
								</tr>
								<%
								 	for(String folderName : statSniFlowReport.keySet()){
								 	    if("columns".equals(folderName)){continue;}
								 	   List<CmcUserFlowReportStatistics> list = null;
								 	    if(statSniFlowReport.get(folderName) != null){
								 	       list = (List<CmcUserFlowReportStatistics>)statSniFlowReport.get(folderName);
								 	    }
								 	   if(list == null){ %>
								 		<tr>
											<td><%= folderName %></td>
											<td></td><td></td><td></td><td></td><td></td>
										</tr>
								 <% }else{
								     	 int ccmtsNum =0;
								     	 int peakOnlineNum =0;
								     	 int userNum =0;
								 	     for(int i=0;i<list.size();i++){
								 	         CmcUserFlowReportStatistics stastic = list.get(i);
								 	         ccmtsNum += stastic.getCcmtsTotal();
								 	       	 peakOnlineNum += stastic.getUserNumOnline();
							 	        	 userNum += stastic.getUserNumTotal();
							 	        	 Double maxValue = 0.0;
							 	        	 Double sumValue = 0.0;
							 	        	 if(!calculatedEntity.contains(stastic.getEntityId())){
									 	     	 totalCcmtsNum += stastic.getCcmtsTotal();
									 	     	 totalPeakOnlineNum += stastic.getUserNumOnline();
									 	     	 totalUserNum += stastic.getUserNumTotal();
									 	     	 calculatedEntity.add(stastic.getEntityId());
							 	        	 }
								 	%><tr class="contentRow"><% if(i==0){ %><td rowspan="<%=list.size()+1 %>"><%= folderName %></td><%}%>
										<td><%= stastic.getEntityName() %></td>
										<td align="center" <%if(stastic.getCcmtsTotal()==0){ %>class="L0"<% }%>>
											<a href="#" onclick="showDetail(<%=stastic.getEntityId()%>)"><%= stastic.getCcmtsTotal()%></a>
										</td>
										<td align="center" <%if(stastic.getUserNumOnline()==0){ %>class="L1"<% }%>><%= stastic.getUserNumOnline()%></td>
										<td align="center" <%if(stastic.getUserNumTotal()==0){ %>class="L2"<% }%>><%= stastic.getUserNumTotal()%></td>
										<% for(String column : portIndexColumnList ){
										    	boolean columnFound=false;
												for(CmcUserFlowPortValue pv :stastic.getPortValueList()){
												    System.out.println(column + "  -  "+pv.getPortName());
												    if(column.equals(pv.getPortName())){
												        columnFound = true;
												        if(maxValue < pv.getValue()){
												            maxValue = pv.getValue();
												        }
												        sumValue += pv.getValue();
														out.write("<td align=center>"+pv.getValueString()+"</td>");
														break;
												    }
												}
												if(!columnFound){
												    out.write("<td align=center> - </td>");
												}
										}
										%>
										<td align="center"><%= NumberFormatterUtil.formatDecimalTwo(sumValue) %></td>
										<td align="center"><%= NumberFormatterUtil.formatDecimalTwo(maxValue / 10)%> %</td>
											<% } %>
									</tr>
									<!-- 小计栏 -->
									<tr>
										<td>@report.subtotal@</td>
										<td align="center" class="counter L0"><%= ccmtsNum %></td>
										<td align="center" class="counter L1"><%= peakOnlineNum%></td>
										<td align="center" class="counter L2"><%= userNum%></td>
										<% for(String column : portIndexColumnList ){ %>
											<td align="center"></td>
										<%} %>
										<td align="center" class="counter L3"></td>
										<td align="center" class="counter L4"></td>
									</tr>
								 <% } } %>
								 <tr>
								 		<td></td>
										<td>@report.totalling@</td>
										<td align="center" class="counter L0"><%= totalCcmtsNum %></td>
										<td align="center" class="counter L1"><%= totalPeakOnlineNum%></td>
										<td align="center" class="counter L2"><%= totalUserNum%></td>
										<% for(String column : portIndexColumnList ){ %>
											<th class="reportHeader"></th>
										<%} %>
										<td align="center" class="counter L3"></td>
										<td align="center" class="counter L4"></td>
									</tr>
		</table>
	</div>
	
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>