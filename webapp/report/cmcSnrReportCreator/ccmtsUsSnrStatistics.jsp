<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ page import="com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics" %>
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
	location.href = "/cmc/report/showCcmtsUsSnrStatistics.tv?stTime=" + stTime + "&etTime=" + etTime + "&entityType=" + entityType;	
}
var entityType = '<s:property value="entityType"/>';
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
	$("#entityType").val('<s:property value="entityType"/>');
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
  var deviceTypePosition = Zeta$('entityType');
  var cctype;
  if(<%=uc.hasSupportModule("cmc")%>&&<%=uc.hasSupportModule("olt")%>){
      cctype = EntityType.getCcmtsType();
  }else if(<%=uc.hasSupportModule("cmc")%>&&!<%=uc.hasSupportModule("olt")%>){
      cctype = EntityType.getCcmtsWithAgentType();
  }else if(<%=uc.hasSupportModule("olt")%>&&!<%=uc.hasSupportModule("cmc")%>){
      cctype = EntityType.getCcmtsWithoutAgentType();
  }
  var displayName;
  $.ajax({
      url:'/entity/loadSubEntityType.tv?type=' + cctype,
      type:'POST',
      dateType:'json',
      success:function(response) {
    	  var entityTypes = response.entityTypes 
    	  for(var i = 0; i < entityTypes.length; i++){
	            var option = document.createElement('option');
	            option.value = entityTypes[i].typeId;
	            if(entityType == entityTypes[i].typeId){
	            	displayName = entityTypes[i].displayName;
	            }
	            option.text = entityTypes[i].displayName;
	            try {
	            	deviceTypePosition.add(option, null);
	            } catch(ex) {
	            	deviceTypePosition.add(option);
	            }
    	  }
    	  $("#entityType").val(entityType)
    	  $("#statisRange1").text(displayName);
      },
      error:function(entityTypes) {},
      cache:false
  });
});

function exportExcelClick() {
	var stTime = stField.value;
	var etTime = etField.value;
	var entityType = $("#entityType").val();
	window.location.href="/cmc/report/exportSnrReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime + "&entityType=" + entityType; 
}

function showSnrDetail(f,v){
	var stTime = stField.value;
	var etTime = etField.value;
	var entityType = $("#entityType").val();
	var url ="/cmc/report/showSnrReprotDetail.tv?stTime= " + stTime + "&etTime=" + etTime + "&entityType=" + entityType;
	if(f!=''){
		url += "&folderId=" + f;
	}
	if(v!=''){
		url += "&entityId=" + v;
	}
	window.parent.addView("snrReportDetail"+ Math.random(),"@report.ccmtsSnrDetail@",'reportIcon', url);
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
<body class=whiteToBlack >
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
					<td class="rightBlueTxt">@report.deviceType@@report.maohao@</td>
					<td>
						<select id="entityType" class="normalSel" style="width: 150px;">
						</select>
					</td>
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
		<h1>@report.ccmtsSnr@</h1>
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
				<span>@report.deviceType@@report.maohao@</span>
				<span id="statisRange1">
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
					<th>@report.folder@</th>
					<th>@report.entity@</th>
					<th>@report.below20DB@</th>
					<th>@report.totalChannel@</th>
					<th>@report.percentage@</th>
				</tr>
			</thead>
			<tbody>
				<%
					DecimalFormat df = new DecimalFormat("0.00");
				    Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport = (Map<String, List<CmcSnrReportStatistics>>) request.getAttribute("statCmcSnrReport");
				    List<Long> calculatedEntity = new ArrayList<Long>();
				        Integer totalPorts = 0;
				        Integer totalPorts20 = 0;
				        String totalPortsPercent = "";
				        for (String folderName : statCmcSnrReport.keySet()) {
				            List<CmcSnrReportStatistics> list = statCmcSnrReport.get(folderName);
				            if (list == null) {
				%>
				<tr>
					<%if(folderName.equals("WorkBench.topology10")){ %>
					<td>@report.DefaultFolder@</td>
					<%}else{%>
					<td><%=folderName%></td>
					<%} %>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
				<%
			    } else {
	                Integer count = 0;
	                Integer count20 = 0;
	                String countPercent = "";
	                Long folderId = 0l;
	                for (int i = 0; i < list.size(); i++) {
	                    CmcSnrReportStatistics stastic = list.get(i);
	                    count += stastic.getPortTotalNum().intValue();
	                    count20 += stastic.getPortNum0To20().intValue();
	                    folderId = stastic.getFolderId();
	                    if(!calculatedEntity.contains(stastic.getEntityId())){
			 	     	  	 totalPorts += stastic.getPortTotalNum().intValue();
			 	     	  	 totalPorts20+= stastic.getPortNum0To20().intValue();
				 	     	 calculatedEntity.add(stastic.getEntityId());
		 	        	 }
				%>
				<tr>
				<% if (i == 0) {%>
					<%if(folderName.equals("WorkBench.topology10")){ %>
					<td rowspan="<%=list.size() + 1%>">@report.DefaultFolder@</td>
					<%}else{%>
					<td rowspan="<%=list.size() + 1%>"><%=folderName%></td>
					<%} %>
				<% }%>
				<td><%=stastic.getEntityName()%></td>
				<td><a href="#" onclick = "showSnrDetail('','<%=stastic.getEntityId()%>')"><%=stastic.getPortNum0To20()%></a></td>
				<td><%=stastic.getPortTotalNum()%></td>
				<td><%=stastic.getPortRateString()%></td>
				<%
				    }
                    //totalPorts += count;
                    //totalPorts20 += count20;
                    if(count == 0){
                        countPercent = "0%";
                    }else{
						countPercent = df.format((Double.parseDouble(count20.toString())/Double.parseDouble(count.toString()))*100) + "%";
                    }
				%>
				</tr>
				<!-- 小计栏 -->
				<tr>
					<th class=sumTd>@report.subtotal@</th>
					<th class=sumTd><a href="#" style="color: blue;" onclick = "showSnrDetail('<%=folderId%>','')"><%=count20%></a></th>
					<th class=sumTd><%=count%></th>
					<th class=sumTd><%=countPercent%></th>
				</tr>
			 <%
			     }
			 }
				    if(totalPorts == 0){
				        totalPortsPercent = "0%";
				    }else{
						totalPortsPercent = df.format((Double.parseDouble(totalPorts20.toString())/Double.parseDouble(totalPorts.toString()))*100)+"%";
				    }
			 %>
			 <tr>
			 		<td></td>
					<td >@report.totalling@</td>
					<td ><a href="#" style="color: blue;" onclick = "showSnrDetail('','')"><%=totalPorts20%></a></td>
					<td><%=totalPorts%></td>
					<td ><%=totalPortsPercent%></td>
				</tr>
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
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>
