<%@page import="com.topvision.ems.cmc.report.domain.PonCmcRelation"%>
<%@page import="com.topvision.ems.cmc.report.domain.OltPonRelation"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.topvision.ems.cmc.report.domain.FolderOltRelation" %>
<%@ page import="com.topvision.ems.cmc.report.domain.CmRealTimeUserStaticReport" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library Jquery
	library Zeta
    module report
	css css.report
	import report.cmRealTimeUserStaticReportCreator.cmRealTimeUserStatic
	import report.javascript.CommonMethod
</Zeta:Loader>
<script type="text/javascript">
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
<body class=”whiteToBlack“>
	<!-- 功能按钮区域  -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl noWidthCenter">
        	<!-- <li><a href="javascript:;" class="normalBtnBig" onclick="exportExcelClick()"><span><i class="bmenu_export"></i>@COMMON.exportExcel@</span></a></li> -->
        	<li id="topPutExportBtn" class="splitBtn"></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="onPrintviewClick()"><span><i class="miniIcoInfo"></i>@COMMON.preview@</span></a></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="onPrintClick()"><span><i class="miniIcoPrint"></i>@COMMON.print@</span></a></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="queryForData()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
    	 </ol>
	</div>
	
	<!-- 报表内容展示区域 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.cmRealTimeUserStaticReportCreator@</h1>
		<h3 id="queryTime"></h3>
		<table class="reportTable" id="reportTable">
			<thead>
				<tr id="tHRow">
					<th>@report.folder@</th>
					<th>@report.oltName@</th>
					<th class="nowrap">OLT_IP</th>
					<th>@report.pon@</th>
					<th>@report.ccmtsName@</th>
					<th>@report.onLineCmNum@</th>
					<th>@report.offLineCmNum@</th>
					<th>@report.otherStatusCmNum@</th>
					<th>@report.allStatusCmNum@</th>
				</tr>
			</thead>
			<tbody>
				<%
				Map<String, FolderOltRelation> folderOltRelations = (Map<String, FolderOltRelation>)request.getAttribute("folderOltRelations");
				List<Long> countedEntityIds = new ArrayList<Long>();
					//总计
					int totalOnlineSummary = 0;
					int totalOfflineSummary = 0;
					int totalOtherStatusSummary = 0;
					int totalAllStatusSummary = 0;
					if(folderOltRelations==null){
					    return;
					}
					//遍历所有的地域
					boolean firstFolder = true;
					for(String folderId : folderOltRelations.keySet()){
					    //地域小计
					    int folderOnlineSummary = 0;
					    int folderOfflineSummary = 0;
					    int folderOtherStatusSummary = 0;
					    int folderAllStatusSummary = 0;
					    //地域信息
					    FolderOltRelation folderOltRelation = folderOltRelations.get(folderId);
					    //地域下OLT信息
					    Map<String, OltPonRelation> oltPonRelations =  folderOltRelation.getOltPonRelations();
					    if(oltPonRelations==null){
						    continue;
						}
					    //遍历该地域下的所有OLT
					    boolean firstOlt = true;
					    for(String oltIp : oltPonRelations.keySet()){
					        //OLT小计
					        int oltOnlineSummary = 0;
					        int oltOfflineSummary = 0;
					        int oltOtherStatusSummary = 0;
					        int oltAllStatusSummary = 0;
					        //OLT信息
					        OltPonRelation oltPonRelation = oltPonRelations.get(oltIp);
					        //OLT下PON信息
					        Map<String, PonCmcRelation> ponCmcRelations = oltPonRelation.getPonCmcRelations();
					        if(ponCmcRelations==null){
							    continue;
							}
					        //遍历该OLT下的所有PON
					        boolean firstPon = true;
					        for(String ponIndex : ponCmcRelations.keySet()){
					            //PON信息
					            PonCmcRelation ponCmcRelation = ponCmcRelations.get(ponIndex);
					            //PON下的CCMTS信息
					            List<CmRealTimeUserStaticReport> reportDatas = ponCmcRelation.getReportDatas();
					            int ponRowSpan = reportDatas.size();
					            //遍历PON口下的所有数据
					            boolean firstCmc = true;
					            for(CmRealTimeUserStaticReport data : reportDatas){
					                if(data==null){
								        continue;
								    }
					                //更新统计值
					                oltOnlineSummary +=data.getOnLineCmNum();
									oltOfflineSummary +=data.getOffLineCmNum();
									oltOtherStatusSummary +=data.getOtherStatusCmNum();
									oltAllStatusSummary +=data.getAllStatusCmNum();
									
									folderOnlineSummary +=data.getOnLineCmNum();
									folderOfflineSummary +=data.getOffLineCmNum();
									folderOtherStatusSummary +=data.getOtherStatusCmNum();
									folderAllStatusSummary +=data.getAllStatusCmNum();
									
									if(!countedEntityIds.contains(data.getCmcId())){
									    countedEntityIds.add(data.getCmcId());
										totalOnlineSummary +=data.getOnLineCmNum();
										totalOfflineSummary +=data.getOffLineCmNum();
										totalOtherStatusSummary +=data.getOtherStatusCmNum();
										totalAllStatusSummary +=data.getAllStatusCmNum();
									}
					                if(firstOlt && firstPon && firstCmc){
					                    //整个地域的第一行(第一个OLT的第一个PON口下的第一个CCMTS)
                %>  
				<tr>
					<td rowspan="<%=folderOltRelation.getRowspan()%>"><%=folderOltRelation.getFolderName()%></td>
					<td rowspan="<%=oltPonRelation.getRowspan()%>"><%=oltPonRelation.getOltName()%></td>
					<td class="nowrap" rowspan="<%=oltPonRelation.getRowspan()%>"><%=oltPonRelation.getOltIp()%></td>
					<td rowspan="<%=ponRowSpan%>"><%=ponCmcRelation.getPonIndexStr()%></td>
					<td><%=data.getCmcName()%></td>
					<td><%=data.getOnLineCmNum()%></td>
					<td><%=data.getOffLineCmNum()%></td>
					<td><%=data.getOtherStatusCmNum()%></td>
					<td><%=data.getAllStatusCmNum()%></td>
				</tr>
				<%
					                }else if(!firstOlt && firstPon && firstCmc){
					                    //OLT下的第一行(第一个PON口下的第一个CCMTS)
                   %>  
				<tr>
					<td rowspan="<%=oltPonRelation.getRowspan()%>"><%=oltPonRelation.getOltName()%></td>
					<td class="nowrap" rowspan="<%=oltPonRelation.getRowspan()%>"><%=oltPonRelation.getOltIp()%></td>
					<td rowspan="<%=ponRowSpan%>"><%=ponCmcRelation.getPonIndexStr()%></td>
					<td><%=data.getCmcName()%></td>
					<td><%=data.getOnLineCmNum()%></td>
					<td><%=data.getOffLineCmNum()%></td>
					<td><%=data.getOtherStatusCmNum()%></td>
					<td><%=data.getAllStatusCmNum()%></td>
				</tr>
				<%	                    
					                }else if(!firstPon && firstCmc ){
					                    //pon下的第一行(第一个CCMTS)
                   %>  
				<tr>
					<td rowspan="<%=ponRowSpan%>"><%=ponCmcRelation.getPonIndexStr()%></td>
					<td><%=data.getCmcName()%></td>
					<td><%=data.getOnLineCmNum()%></td>
					<td><%=data.getOffLineCmNum()%></td>
					<td><%=data.getOtherStatusCmNum()%></td>
					<td><%=data.getAllStatusCmNum()%></td>
				</tr>
				<%	                    
					                }else{
                   %>  
				<tr>
					<td><%=data.getCmcName()%></td>
					<td><%=data.getOnLineCmNum()%></td>
					<td><%=data.getOffLineCmNum()%></td>
					<td><%=data.getOtherStatusCmNum()%></td>
					<td><%=data.getAllStatusCmNum()%></td>
				</tr>
				<%	                    
					                }
					                firstCmc = false;
					            }
					            firstPon = false; 
					        }
					        //添加OLT小计行
				%>  
				<tr class="dataTr summaryTd">
					<td>@report.oltSummary@</td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=oltOnlineSummary%></td>
					<td><%=oltOfflineSummary%></td>
					<td><%=oltOtherStatusSummary%></td>
					<td><%=oltAllStatusSummary%></td>
				</tr>
				<%
					        firstOlt = false;
					    }
					    //添加地域小计
			    %>  
				<tr class="dataTr summaryTd">
					<td>@report.folderSummary@</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=folderOnlineSummary%></td>
					<td><%=folderOfflineSummary%></td>
					<td><%=folderOtherStatusSummary%></td>
					<td><%=folderAllStatusSummary%></td>
				</tr>
				<%
					firstFolder = false;
					}
					//添加总计
				 %>  
				<tr class="dataTr summaryTd">
					<td>@label.total@</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><%=totalOnlineSummary%></td>
					<td><%=totalOfflineSummary%></td>
					<td><%=totalOtherStatusSummary%></td>
					<td><%=totalAllStatusSummary%></td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- 功能按钮区域  -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl noWidthCenter">
        	<!-- <li><a href="javascript:;" class="normalBtnBig" onclick="exportExcelClick()"><span><i class="bmenu_export"></i>@COMMON.exportExcel@</span></a></li> -->
        	<li id="bottomPutExportBtn" class="splitBtn"></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="onPrintviewClick()"><span><i class="miniIcoInfo"></i>@COMMON.preview@</span></a></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="onPrintClick()"><span><i class="miniIcoPrint"></i>@COMMON.print@</span></a></li>
        	<li><a href="javascript:;" class="normalBtnBig" onclick="queryForData()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
    	 </ol>
	</div>
</body>
</Zeta:HTML>