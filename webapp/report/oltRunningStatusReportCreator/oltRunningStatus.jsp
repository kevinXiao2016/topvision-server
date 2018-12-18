<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.ems.cmc.report.domain.OltRunningStatus" %>
<%@ page import="com.topvision.ems.cmc.report.domain.OltPonRunningStatus" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<style type="text/css">
.reportHeader{font-size:12px;background-color: #DEDFDE;}
#stTime, #etTime{ width:150px;}
#stTime .x-form-field-wrap, #etTime .x-form-field-wrap{width:auto !important;}
</style>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin DateTimeField
    module report
	css css.report
	import report.javascript.CommonMethod
	import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript" src="/js/json2.js"></script>
<script type="text/javascript">
function optionClick(obj) {
    $('#queryDiv').toggle();
    $("html,body").animate({scrollTop:"0px"},200);
}

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
	var entityType = $("#entityType").val();
	window.top.showWaitingDlg("@COMMON.wait@", "@report.stasticing@", 'ext-mb-waiting')
	location.href = "/cmc/report/queryOltRunningStatus.tv?stTime=" + stTime + "&etTime=" + etTime;	
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

function exportExcelClick() {
	var stTime = stField.value;
	var etTime = etField.value;
	window.location.href="/cmc/report/exportOltRunningStatusToExcel.tv?stTime=" + stTime + "&etTime=" + etTime; 
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
</script>
</head>
<body class="whiteToBlack" >
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
		<div class="zebraTableCaptionTitle"><span>@tip.queryCondition@</span></div>
		<form id="queryForm" method="post">
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
						<select id="statistical" class="normalSel normalSelDisabled" style="width: 150px;" disabled = "disabled">
							<!-- TODO 设备类型的选择应该由DB控制 -->
							<option>@report.baseOnFolder@</option>
						</select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<%-- <a style="float: left;" href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> --%>
						<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.oltRunningStatusReport@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p style="font-size: 14px;">
				<span>@report.statRange@:</span>
				<span id="statisRange">
					<s:if test="stTime != null">${stTime} @report.to@   ${etTime}</s:if>
					<s:else>@report.noSelect@</s:else>
				</span>
			</p>
			<p>
				<span>@report.statMode@:</span>
				<span id="statisRange"> @report.baseOnFolder@</span>
			</p>
		</div>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
				<!-- 标题行 -->
			<thead>
				<tr>
					<th rowspan="2">@report.folder@</th>
					<th rowspan="2">@report.oltName@</th>
					<th rowspan="2">@report.sniUsageTop@</th>
					<th colspan="4">@report.ponPortStatistics@</th>
				</tr>
				<tr>
					<th>@report.ponPortName@</th>
					<th>@report.usageTop@</th>
					<th>@report.ccmtsRealtimeNum@</th>
					<th>@report.cmNum@</th>
				</tr>
			</thead>
			<tbody>
				<%
				    Map<String, List<OltRunningStatus>> oltRunningStatus = (Map<String, List<OltRunningStatus>>) request.getAttribute("oltRunningReport");
				    List<Long> calculatedEntity = new ArrayList<Long>();
				        for (String folderName : oltRunningStatus.keySet()) {
				            List<OltRunningStatus> list = oltRunningStatus.get(folderName);
				            if (list == null) {
				                continue;
					} else {
				    
				    	int folderPonNum = 0;
				    	for(int n=0;n<list.size();n++){
				    	    folderPonNum += list.get(n).getOltPonRunningStatusList().size();
				    	}
				    	
						for(int i=0;i<list.size();i++){
						    OltRunningStatus stastic = list.get(i);
						    
						    int oltPonNum = stastic.getOltPonRunningStatusList().size();
						    
					    for(int j=0; j<stastic.getOltPonRunningStatusList().size();j++){
					        OltPonRunningStatus ponData = stastic.getOltPonRunningStatusList().get(j);
					        if(i==0&&j==0){
					            //地域的第一行
					            %>
					            	<tr>
										<td rowspan="<%=folderPonNum%>"><%= folderName %></td>
										<td rowspan="<%=oltPonNum%>"><%= ponData.getOltName() %></td>
										<td rowspan="<%=oltPonNum%>"><%= ponData.getSniUsageString() %></td>
										<td><%= ponData.getPonName() %></td>
										<td><%= ponData.getPonUsage() %></td>
										<td><%= ponData.getCcNum() %></td>
										<%if(ponData.getCmNum()!=null){ %>
										<td><%= ponData.getCmNum() %></td>
										<%}else{ %>
										<td>-</td>
										<%} %>
									</tr>
					            <%
					        }else if(i!=0 && j==0){
					          	//OLT的第一行,除去地域的第一行
					            %>
					            	<tr>
										<td rowspan="<%=oltPonNum%>"><%= ponData.getOltName() %></td>
										<td rowspan="<%=oltPonNum%>"><%= ponData.getSniUsageString() %></td>
										<td><%= ponData.getPonName() %></td>
										<td><%= ponData.getPonUsage() %></td>
										<td><%= ponData.getCcNum() %></td>
										<%if(ponData.getCmNum()!=null){ %>
										<td><%= ponData.getCmNum() %></td>
										<%}else{ %>
										<td>-</td>
										<%} %>
									</tr>
				            	<%
					        }else{
					            %>
					            	<tr>
										<td><%= ponData.getPonName() %></td>
										<td><%= ponData.getPonUsage() %></td>
										<td><%= ponData.getCcNum() %></td>
										<%if(ponData.getCmNum()!=null){ %>
										<td><%= ponData.getCmNum() %></td>
										<%}else{ %>
										<td>-</td>
										<%} %>
									</tr>
			            		<%
					        }
					} %>
				<%} %>
				<%} %>
				<%} %>
			</tbody>
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