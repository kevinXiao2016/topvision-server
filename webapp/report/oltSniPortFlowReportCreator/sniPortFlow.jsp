<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.epon.report.domain.OltSniPortFlowStastic" %>
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
	window.top.closeWaitingDlg();
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
   		});;
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@report.stasticing@", 'ext-mb-waiting');
	location.assign("/epon/report/querySniFlowStastic.tv?stTime=" + stTime + "&etTime="+etTime);	
}

//@report.exportExcel@
function exportExcelClick() {
	var stTime = stField.value;
	var etTime = etField.value;
	window.location.href="/epon/report/exportSniReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime; 
}

function showDetail(rangeStart,rangeEnd,entityId){
	var stTime = stField.value;
	var etTime = etField.value;
	var url = "/epon/report/querySniFlowDetail.tv?stTime=" + stTime + "&etTime=" + etTime+"&entityId="+entityId+"&rangeStart="+rangeStart+"&rangeEnd="+rangeEnd;
	window.top.addView("sniFlowDetail"+Math.random(),"@report.sniFlowDetail@",null,url);
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
						<select id="statistical" class="normalSel normalSelDisabled" style="width: 150px;" disabled="disabled">
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
		<h1>@report.sniFlow@</h1>
		<h3 id="queryTime"><s:date name="statDate"	format="yyyy-MM-dd HH:mm:ss" /></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p style="font-size: 14px;">
				<span>@report.statRange@:</span>
				<span id="statisRange">${stTime} @report.to@ ${etTime}</span>
			</p>
			<p>
				<span>@report.statMode@:</span>
				<span id="statisRange">@report.baseOnFolder@</span>
			</p>
		</div>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<caption>
				<span>@report.queryResult@</span>
				<span style="float:right;" id="statisRange">@report.flowUnit@@report.maohao@Mbps</span>
			</caption>
			<thead>
				<tr>
					<th>@report.folder@</th>
					<th>@report.entity@</th>
					<th>@report.linkedPorts@</th>
					<th>@report.maxSendPower@</th>
					<th>@report.maxRecvPower@</th>
					<th>0%~20%</th>
					<th>20%~40%</th>
					<th>40%~60%</th>
					<th>60%~80%</th>
					<th>80%~100%</th>
				</tr>
			</thead>
			<tbody>
				<%
				    Map<String, List<OltSniPortFlowStastic>> statSniFlowReport = (Map<String, List<OltSniPortFlowStastic>>) request
				                .getAttribute("statSniFlowReport");
				        List<Long> calculatedEntity = new ArrayList<Long>();
				        int totalPorts = 0;
				        int totalRange20 = 0;
				        int totalRange40 = 0;
				        int totalRange60 = 0;
				        int totalRange80 = 0;
				        int totalRange100 = 0;
				        for (String folderName : statSniFlowReport.keySet()) {
				            List<OltSniPortFlowStastic> list = statSniFlowReport.get(folderName);
				            if (list == null) {
				%>
				<tr>
					<td><%=folderName%></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<%
				    } else {
				                int count = 0;
				                int range20 = 0;
				                int range40 = 0;
				                int range60 = 0;
				                int range80 = 0;
				                int range100 = 0;
				                for (int i = 0; i < list.size(); i++) {
				                    OltSniPortFlowStastic stastic = list.get(i);
				                    count += stastic.getCurrentLinkedPortCount();
				                    range20 += stastic.getRange20();
				                    range40 += stastic.getRange40();
				                    range60 += stastic.getRange60();
				                    range80 += stastic.getRange80();
				                    range100 += stastic.getRange100();
				                    if (!calculatedEntity.contains(stastic.getEntityId())) {
				                        totalPorts += stastic.getCurrentLinkedPortCount();
				                        totalRange20 += stastic.getRange20();
				                        totalRange40 += stastic.getRange40();
				                        totalRange60 += stastic.getRange60();
				                        totalRange80 += stastic.getRange80();
				                        totalRange100 += stastic.getRange100();
				                        calculatedEntity.add(stastic.getEntityId());
				                    }
				%><tr class="contentRow">
					<%
					    if (i == 0) {
					%><td rowspan="<%=list.size() + 1%>"><%=folderName%></td>
					<%
					    }
					%>
					<td><%=stastic.getEntityName()%></td>
					<td align="center"
						<%if (stastic.getCurrentLinkedPortCount() > 0) {%> class="L0"
						<%}%>><a href="#"
						onclick="showDetail(0,100,<%=stastic.getEntityId()%>)"><%=stastic.getCurrentLinkedPortCount()%></a></td>
					<td align="right"><%=stastic.getMaxSendFlowString()%></td>
					<td align="right"><%=stastic.getMaxRecvFlowString()%></td>
					<td align="center" <%if (stastic.getRange20() > 0) {%> class="L1"
						<%}%>><a href="#"
						onclick="showDetail(0,20,<%=stastic.getEntityId()%>)"><%=stastic.getRange20()%></a></td>
					<td align="center" <%if (stastic.getRange40() > 0) {%> class="L2"
						<%}%>><a href="#"
						onclick="showDetail(20,40,<%=stastic.getEntityId()%>)"><%=stastic.getRange40()%></a></td>
					<td align="center" <%if (stastic.getRange60() > 0) {%> class="L3"
						<%}%>><a href="#"
						onclick="showDetail(40,60,<%=stastic.getEntityId()%>)"><%=stastic.getRange60()%></a></td>
					<td align="center" <%if (stastic.getRange80() > 0) {%> class="L4"
						<%}%>><a href="#"
						onclick="showDetail(60,80,<%=stastic.getEntityId()%>)"><%=stastic.getRange80()%></a></td>
					<td align="center" <%if (stastic.getRange100() > 0) {%>
						class="L5" <%}%>><a href="#"
						onclick="showDetail(80,100,<%=stastic.getEntityId()%>)"><%=stastic.getRange100()%></a></td>
					<%
					    }
					%>
				</tr>
				<!-- 小计栏 -->
				<tr>
					<td>@report.subtotal@</td>
					<td align="center" class="counter L0"><%=count%></td>
					<td></td>
					<td></td>
					<td align="center" class="counter L1"><%=range20%></td>
					<td align="center" class="counter L2"><%=range40%></td>
					<td align="center" class="counter L3"><%=range60%></td>
					<td align="center" class="counter L4"><%=range80%></td>
					<td align="center" class="counter L5"><%=range100%></td>
				</tr>
				<%
				    }
				        }
				%>
				<tr>
					<td></td>
					<td>@report.totalling@</td>
					<td align="center" class="counter L0"><%=totalPorts%></td>
					<td></td>
					<td></td>
					<td align="center" class="counter L1"><%=totalRange20%></td>
					<td align="center" class="counter L2"><%=totalRange40%></td>
					<td align="center" class="counter L3"><%=totalRange60%></td>
					<td align="center" class="counter L4"><%=totalRange80%></td>
					<td align="center" class="counter L5"><%=totalRange100%></td>
				</tr>
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