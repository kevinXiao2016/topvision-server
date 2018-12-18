<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CcmtsChlFlowInfo" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
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
<style type="text/css">
.reportHeader{font-size:12px;background-color: #DEDFDE;text-align: center;}
.report-content-div, .reportTable, .queryDiv{
	width: 900px !important;
}
</style>
<script>
var queryVisible = false;
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
//点击选项
function optionClick() {
    $('#queryDiv').toggle();
}

//生成EXCEL
function exportExcelClick() {
	var stTime = '<s:property value="stTime"/>',
		etTime = '<s:property value="etTime"/>',
		entityId = '<s:property value="entityId"/>',
		rangeName = '<s:property value="rangeName"/>',
		url = "/cmc/report/exportCcmtsUpChlDetailToExcel.tv";
	url += "?stTime= " + stTime + "&etTime=" + etTime + "&rangeName=" + rangeName;
	if(!isNaN(entityId)){
		url += "&entityId=" + entityId;
	}
	window.location.href=url;
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
		<h1>@report.ccmtsUpChlFlowDetail@</h1>
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
					<th class="nowrap">@report.ccmtsName@</th>
					<th class="nowrap">@report.uplinkOlt@</th>
					<th class="nowrap">@report.us@</th>
					<th class="nowrap">@report.modulationProfile@</th>
					<th>@report.usFlowMax@</th>
					<th>@report.usUsageMax@</th>
					<th>@report.usUsageAve@</th>
					<th>@report.userRegNumMax@</th>
					<th>@report.userNumMax@</th>
				</tr>
			</thead>
			<tbody>
				<%
				Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail = (Map<String, List<CcmtsChlFlowDetail>>) request.getAttribute("statUpChlFlowDetail");
				if(statUpChlFlowDetail==null){
				    return;
				}
				//遍历每一个地域
				Boolean folderFirstLine = true, ccmtsFirstLine = true;
				for (String folderName : statUpChlFlowDetail.keySet()) {
				    folderFirstLine = true;
				    //获取该地域下的所有设备信息
				    List<CcmtsChlFlowDetail> cmtsDetails = statUpChlFlowDetail.get(folderName);
				    //获取该地域总计多少行
				    Integer folderRowSpan = 0;
				    if(cmtsDetails!=null && cmtsDetails.size()!=0){
				      	//获取该设备下的所有上行信道
				      	for(CcmtsChlFlowDetail cmtsDetail : cmtsDetails){
				      	  	List<CcmtsChlFlowInfo> channels = cmtsDetail.getCcmtsChlFlowInfos();
				      	  	if(channels!=null && channels.size()!=0){
					      	  	folderRowSpan += cmtsDetail.getCcmtsChlFlowInfos().size();
				      	  	}
				      	}
				    }
				    
				    if(cmtsDetails!=null && cmtsDetails.size()!=0){
				        //遍历该地域下的所有设备
				        for(CcmtsChlFlowDetail cmtsDetail : cmtsDetails){
				            ccmtsFirstLine = true;
				            //获取该设备下的所有上行信道
				            List<CcmtsChlFlowInfo> channels = cmtsDetail.getCcmtsChlFlowInfos();
				            if(channels==null || channels.size()==0){
				                continue;
				            }
				            //遍历所有信道
				            for(CcmtsChlFlowInfo channel : channels){
				                //输出数据
				                if(folderFirstLine){
				                    //当前地域的第一行，需要输入地域td
					                %>
					                <tr>
					                	<td rowspan="<%=folderRowSpan %>"><%=folderName%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getEntityName()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getOltName()%></td>
					                	<td class="nowrap"><%=channel.getChannelName()%></td>
					                	<td><%=channel.getModulationProfileString()%></td>
					                	<td><%=channel.getMaxFlowString()%></td>
					                	<td><%=channel.getMaxFlowUsageString() %></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getUsageAvgString()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getMaxRegUserNum()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getMaxUserNum()%></td>
					                </tr>
					                <%
				                }else if(!folderFirstLine && ccmtsFirstLine){
				                    //当前地域下后面CC的第一行
				                    %>
					                <tr>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getEntityName()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getOltName()%></td>
					                	<td class="nowrap"><%=channel.getChannelName()%></td>
					                	<td><%=channel.getModulationProfileString()%></td>
					                	<td><%=channel.getMaxFlowString()%></td>
					                	<td><%=channel.getMaxFlowUsageString() %></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getUsageAvgString()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getMaxRegUserNum()%></td>
					                	<td rowspan="<%=channels.size() %>"><%=cmtsDetail.getMaxUserNum()%></td>
					                </tr>
					                <%
				                }else{
				                    %>
					                <tr>
					                	<td class="nowrap"><%=channel.getChannelName()%></td>
					                	<td><%=channel.getModulationProfileString()%></td>
					                	<td><%=channel.getMaxFlowString()%></td>
					                	<td><%=channel.getMaxFlowUsageString() %></td>
					                </tr>
					                <%
				                }
				                folderFirstLine = false;
				                ccmtsFirstLine = false;
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
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button> --%>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoInfo">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>