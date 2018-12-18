<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<style type="text/css">
.reportHeader{font-size:12px;background-color: #DEDFDE;text-align: center;}
.report-content-div, .reportTable, .queryDiv{
	width: 900px !important;
}
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
<style type="text/css">
#stTime, #etTime{ width:150px;}
#stTime .x-form-field-wrap, #etTime .x-form-field-wrap{width:auto !important;}
</style>
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
	location.href = "/cmc/report/showCcmtsDownChlFlowAsset.tv?stTime=" + stTime + "&etTime=" + etTime;	
}
Ext.onReady(function(){
	window.top.closeWaitingDlg()
	window.stField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${stTime}',
		renderTo:"stTime",
		emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@'
	});
	window.etField = new Ext.ux.form.DateTimeField({
		width:150,
		value : '${etTime}',
		renderTo:"etTime",
		emptyText:'@report.plsFullfillDate@',
	    blankText:'@report.plsFullfillDate@'
	});
	$("#entityType").val('<s:property value="entityType"/>')
	nm3kPickData({
	    startTime : window.stField,
	    endTime : window.etField,
	    searchFunction : query
	});
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
	var stTime = stField.value;
	var etTime = etField.value;
	var entityType = $("#entityType").val();
	window.location.href="/cmc/report/exportCcmtsDownChlFlowReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime; 
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

function showCcmtsDownChlFlowDetail(entityId, rangeName){
	var stTime = stField.value;
	var etTime = etField.value;
	var url = "/cmc/report/showCcmtsDownChlFlowDetail.tv";
	url += "?stTime= " + stTime + "&etTime=" + etTime + "&rangeName=" + rangeName;
	if(entityId!='null'){
		url += "&entityId=" + entityId;
	}
	window.parent.addView("ccmtsDownChlFlowDetail"+ Math.random(),"@report.ccmtsDownChlFlowDetail@",'reportIcon', url);
}
</script>
</head>
<body class=whiteToBlack >
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button> --%>
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
						<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.ccmtsDownChannelFlowReport@</h1>
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
			<thead>
				<tr>
					<th class="nowrap">@report.folder@</th>
					<th class="nowrap">@report.entity@</th>
					<th class="nowrap">@report.channelNum@</th>
					<th class="nowrap">90%-100%</th>
					<th class="nowrap">80%-90%</th>
					<th class="nowrap">70%-80%</th>
					<th class="nowrap">60%-70%</th>
					<th class="nowrap">50%-60%</th>
					<th class="nowrap">40%-50%</th>
					<th class="nowrap">30%-40%</th>
					<th class="nowrap">20%-30%</th>
					<th class="nowrap">10%-20%</th>
					<th class="nowrap">0%-10%</th>
					<th class="nowrap">0%</th>
				</tr>
			</thead>
			<tbody>
				<%
				DecimalFormat df = new DecimalFormat("0.00");
				Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic = (Map<String, List<CcmtsChlFlowStatic>>) request.getAttribute("statDownChlFlowStatic");
				
				Integer totalChlNum = 0;
		        Integer totalUsage90to100 = 0;
		        Integer totalUsage80to90 = 0;
		        Integer totalUsage70to80 = 0;
		        Integer totalUsage60to70 = 0;
		        Integer totalUsage50to60 = 0;
		        Integer totalUsage40to50 = 0;
		        Integer totalUsage30to40 = 0;
		        Integer totalUsage20to30 = 0;
		        Integer totalUsage10to20 = 0;
		        Integer totalUsage0to10 = 0;
		        Integer totalUsage0 = 0;
		        List<Long> caculatedIds = new ArrayList<Long>();
		        
		        //遍历每一个地域
		        for (String folderName : statUpChlFlowStatic.keySet()) {
		            Boolean first = true;
		            //获取该地域下的所有局方设备
		            List<CcmtsChlFlowStatic> list = statUpChlFlowStatic.get(folderName);
	                //如果该地域下没有设备，则直接跳过该地域
		            if (list != null) {
		                //遍历该地域下面的所有设备，输出数据
		                for(CcmtsChlFlowStatic entity : list){
		                    //如果是第一个设备
		                    if(first==true){
		                        first = false;
		        %>   
		        	<tr>
		        		<td rowspan="<%=list.size()%>"><%=folderName%></td>
		        		<td><%=entity.getEntityName()%></td>
		        		<td><%=entity.getChlNum()%></td>
		        		<td>
		        		<%if(entity.getUsage90to100()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage90to100')"><%=entity.getUsage90to100()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage80to90()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage80to90')"><%=entity.getUsage80to90()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage70to80()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage70to80')"><%=entity.getUsage70to80()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage60to70()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage60to70')"><%=entity.getUsage60to70()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage50to60()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage50to60')"><%=entity.getUsage50to60()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage40to50()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage40to50')"><%=entity.getUsage40to50()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage30to40()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage30to40')"><%=entity.getUsage30to40()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage20to30()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage20to30')"><%=entity.getUsage20to30()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage10to20()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage10to20')"><%=entity.getUsage10to20()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage0to10()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage0to10')"><%=entity.getUsage0to10()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage0()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage0')"><%=entity.getUsage0()%></a>
		        		<%} %>
		        		</td>
		        	</tr>
		        <%             
		                    }else{
                %>   
   		        	<tr>
   		        		<td><%=entity.getEntityName()%></td>
   		        		<td><%=entity.getChlNum()%></td>
   		        		<td>
		        		<%if(entity.getUsage90to100()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage90to100')"><%=entity.getUsage90to100()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage80to90()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage80to90')"><%=entity.getUsage80to90()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage70to80()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage70to80')"><%=entity.getUsage70to80()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage60to70()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage60to70')"><%=entity.getUsage60to70()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage50to60()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage50to60')"><%=entity.getUsage50to60()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage40to50()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage40to50')"><%=entity.getUsage40to50()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage30to40()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage30to40')"><%=entity.getUsage30to40()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage20to30()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage20to30')"><%=entity.getUsage20to30()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage10to20()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage10to20')"><%=entity.getUsage10to20()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage0to10()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage0to10')"><%=entity.getUsage0to10()%></a>
		        		<%} %>
		        		</td>
		        		<td>
		        		<%if(entity.getUsage0()==0){ %>
		        			0
		        		<%}else{ %>
		        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('<%=entity.getEntityId()%>', 'usage0')"><%=entity.getUsage0()%></a>
		        		<%} %>
		        		</td>
   		        	</tr>
   		        <%    
		                    }
		                    if(!caculatedIds.contains(entity.getEntityId())){
		                        caculatedIds.add(entity.getEntityId());
			                    totalChlNum += entity.getChlNum();
			                    totalUsage90to100 += entity.getUsage90to100();
			    		        totalUsage80to90 += entity.getUsage80to90();
			    		        totalUsage70to80 += entity.getUsage70to80();
			    		        totalUsage60to70 += entity.getUsage60to70();
			    		        totalUsage50to60 += entity.getUsage50to60();
			    		        totalUsage40to50 += entity.getUsage40to50();
			    		        totalUsage30to40 += entity.getUsage30to40();
			    		        totalUsage20to30 += entity.getUsage20to30();
			    		        totalUsage10to20 += entity.getUsage10to20();
			    		        totalUsage0to10 += entity.getUsage0to10();
			    		        totalUsage0 += entity.getUsage0();
		                    }
		                }
		            }
		        }
		        //统计总计
	         	%>   
	        	<tr>
	        		<td>@report.summary@</td>
	        		<td></td>
        			<td><%=totalChlNum%></td>
  		        		<td>
	        		<%if(totalUsage90to100==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage90to100')"><%=totalUsage90to100%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage80to90==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage80to90')"><%=totalUsage80to90%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage70to80==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage70to80')"><%=totalUsage70to80%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage60to70==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage60to70')"><%=totalUsage60to70%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage50to60==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage50to60')"><%=totalUsage50to60%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage40to50==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage40to50')"><%=totalUsage40to50%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage30to40==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage30to40')"><%=totalUsage30to40%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage20to30==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage20to30')"><%=totalUsage20to30%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage10to20==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage10to20')"><%=totalUsage10to20%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage0to10==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage0to10')"><%=totalUsage0to10%></a>
	        		<%} %>
	        		</td>
	        		<td>
	        		<%if(totalUsage0==0){ %>
	        			0
	        		<%}else{ %>
	        			<a href="javascript:;" onclick="showCcmtsDownChlFlowDetail('null', 'usage0')"><%=totalUsage0%></a>
	        		<%} %>
	        		</td>
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
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>