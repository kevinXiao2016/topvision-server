<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.topvision.ems.epon.report.domain.CurrentAlarmReport" %>
<%@ page import="com.topvision.ems.report.domain.FolderEntities" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
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
	library jquery
	library zeta
	plugin DateTimeField
    module report
    import report.javascript.CommonMethod
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<style type="text/css">
#stTime, #etTime{ width:150px;}
#stTime .x-form-field-wrap, #etTime .x-form-field-wrap{width:auto !important;}
</style>
<script type="text/javascript">
var countedDeviceList = new Array();
function optionClick() {
	$('#queryDiv').toggle();
	$("html,body").animate({scrollTop:"0px"},200);
}
//查询条件检查
function validate(){
	if(stField.value==""){
		stField.focus();
		return false;
	}
	if(etField.value==""){
		etField.focus();
		return false;
	}
	if(stField.value>etField.value){
	    top.afterSaveOrDelete({
			title: '@report.tip@',
				html: '<b class="orangeTxt">@report.conflictPeriod@</b>'
   		});
		return false;
	}
	if($("#eponSelected").attr("checked")==false && $("#ccmtsSelected").attr("checked")==false){
		window.parent.showMessageDlg("@report.tip@", "@report.MoreThenOneType@");
		return false;
	}
	return true;
}
//查询按钮
function query() {
	//查询条件检查
	if(!validate()){
		return false;
	}
	window.top.showWaitingDlg("@report.waiting@", "@report.caculating@", 'ext-mb-waiting');
	var stTime = stField.value;
	var etTime = etField.value;
	queryForm.action = '/epon/report/showCurAlertReport.tv?query=' + true +'&stTime=' + stTime + '&etTime=' +etTime;
	queryForm.target = "_self";
	queryForm.submit();
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
});

//展示详细的告警信息
function showAlarmDetail(folderId, entityId, alarmTypeId){
	var stTime = stField.value;
	var etTime = etField.value;
	var eponSelected = $("#eponSelected").is(":checked");
	var ccmtsSelected = $("#ccmtsSelected").is(":checked");
	var url ="/epon/report/showCurAlertReportDetail.tv?stTime=" + stTime + "&etTime=" + etTime;
	if(folderId!=''){
		url += "&folderId=" + folderId;
	}
	if(entityId!=''){
		url += "&entityId=" + entityId;
	}
	if(alarmTypeId!=''){
		url += "&alarmTypeId=" + alarmTypeId; 
	}
	url += "&eponSelected=" + eponSelected; 
	url += "&ccmtsSelected=" + ccmtsSelected; 
	window.parent.addView("curAlertReportDetail"+ Math.random(),"@report.currentDetailReport@",'reportIcon', url);
}

function exportExcelClick() {
	var stTime = stField.value;
	var etTime = etField.value;
	var eponSelected = $("#eponSelected").attr("checked");
	var ccmtsSelected = $("#ccmtsSelected").attr("checked");
	var mainAlarmDisable = $("#mainAlarmDisable").attr("checked");
	var minorAlarmDisable = $("#minorAlarmDisable").attr("checked");
	var generalAlarmDisable = $("#generalAlarmDisable").attr("checked");
	var messageDisable = $("#messageDisable").attr("checked");
	window.location.href="/epon/report/exportCurAlertReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime + "&eponSelected=" + 
			eponSelected + "&ccmtsSelected=" + ccmtsSelected + "&mainAlarmDisable=" + mainAlarmDisable+ "&minorAlarmDisable=" + minorAlarmDisable+ 
			"&generalAlarmDisable=" + generalAlarmDisable + "&messageDisable=" + messageDisable; 
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
		<div class="zebraTableCaptionTitle"><span>@tip.queryCondition@</span></div>
		<form id="queryForm" method="post">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td width="120" class="rightBlueTxt">@report.statStartTime@:</td>
					<td><div id="stTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.statEndTime@:</td>
					<td><div id="etTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.deviceType@:</td>
					<td>
						<div class="floatLeftDiv">
							<input <s:if test="eponSelected">checked</s:if>
								id="eponSelected" name="eponSelected" type=checkbox value="true" />
							<span>EPON</span>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="ccmtsSelected">checked</s:if>
							id="ccmtsSelected" name="ccmtsSelected" type=checkbox value="true" />
							<span>CMTS</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.displayFields@:</td>
					<td>
						<div class="floatLeftDiv">
							<input checked="checked" id="folderDisable" name="folderDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@report.folder@</span>
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" id="entityNameDisable" name="entityNameDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@label.entityName@</span>
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" id="entityTypeDisable" name="entityTypeDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@report.deviceType@</span>
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" id="allAlarmDisable" name="allAlarmDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@report.allAlarm@</span>
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" id="emergencyAlarmDisable" name="emergencyAlarmDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@report.emergencyAlarm@</span>
						</div>
						<div class="floatLeftDiv">
							<input checked="checked" id="seriousAlarmDisable" name="seriousAlarmDisable" disabled="disabled" 
								type="checkbox" value="true" />
							<span>@report.seriousAlarm@</span>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="mainAlarmDisable">checked="checked"</s:if>
								id="mainAlarmDisable" name="mainAlarmDisable" type="checkbox" value="true" />
							<label for="type">@report.mainAlarm@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="minorAlarmDisable">checked="checked"</s:if>
								id="minorAlarmDisable" name="minorAlarmDisable" type="checkbox" value="true" />
							<label for="type">@report.minorAlarm@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="generalAlarmDisable">checked="checked"</s:if>
								id="generalAlarmDisable" name="generalAlarmDisable" type="checkbox" value="true" />
							<label for="type">@report.generalAlarm@</label>
						</div>
						<div class="floatLeftDiv">
							<input <s:if test="messageDisable">checked="checked"</s:if>
								id="messageDisable" name="messageDisable" type="checkbox" value="true" />
							<label for="type">@report.message@</label>
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="3">
						<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</td>
				</tr>
			</tbody>
		</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.currentAlarmStatic@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p style="font-size: 14px;">
				<span>@report.statRange@:</span>
				<span id="statisRange">
					<s:if test="stTime != null">${stTime} @report.to@      ${etTime}</s:if>
					<s:else>@report.noSelect@</s:else>
				</span>
			</p>
			<p>
				<span>@report.deviceType@:</span>
				<span id="statisRange">
					<s:if test="eponSelected">
					EPON
					<s:if test="ccmtsSelected">
					&nbsp;&nbsp;CMTS
					</s:if>
					</s:if>
					<s:elseif test="ccmtsSelected">
					CMTS
					</s:elseif>
					<s:else>@report.noSelect@</s:else>
				</span>
			</p>
		</div>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
				<!-- 标题行 -->
			<thead>
				<tr>
					<th>@report.folder@</th>
					<th>@label.entityName@</th>
					<th class="nowrap">@report.deviceType@</th>
					<th>@report.allAlarm@</th>
					<th>@report.emergencyAlarm@</th>
					<th>@report.seriousAlarm@</th>
					<s:if test="mainAlarmDisable">
						<th>@report.mainAlarm@</th>
					</s:if>
					<s:if test="minorAlarmDisable">
						<th>@report.minorAlarm@</th>
					</s:if>
					<s:if test="generalAlarmDisable">
						<th>@report.generalAlarm@</th>
					</s:if>
					<s:if test="messageDisable">
						<th>@report.message@</th>
					</s:if>
				</tr>
			</thead>
			<tbody>
				<%
				Map<Long, FolderEntities> folderEntities 
					= (Map<Long, FolderEntities>)request.getAttribute("currentAlarmReport");
				List<Long> countedEntityIds = new ArrayList<Long>();
				//总计
				Long totalAllAlarmNum = 0l;
				Long totalEmergencyAlarmNum = 0l;
				Long totalSeriousAlarmNum = 0l;
				Long totalMainAlarmNum = 0l;
				Long totalMinorAlarmNum = 0l;
				Long totalGeneralAlarmNum = 0l;
				Long totalMessageNum = 0l;
				if(folderEntities==null){
				    return;
				}
				//遍历每个地域
				for(Long folderId : folderEntities.keySet()){
				    //获取该地域的rowspan
				    int rowspan = folderEntities.get(folderId).getRowspan();
				    //地域统计
				    Long allAlarmNum = 0l;
				    Long emergencyAlarmNum = 0l;
				    Long seriousAlarmNum = 0l;
				    Long mainAlarmNum = 0l;
				    Long minorAlarmNum = 0l;
				    Long generalAlarmNum = 0l;
				    Long messageNum = 0l;
					//遍历该地域下的每台设备
					Map<Long, Object> entityMap =  folderEntities.get(folderId).getEntities();
					if(entityMap==null){
					    continue;
					}
					boolean firstEntity = true;
					for(Long entityId : entityMap.keySet()){
					    CurrentAlarmReport currentAlarmReport =  (CurrentAlarmReport)entityMap.get(entityId);
					    if(currentAlarmReport==null){
					        continue;
					    }
					    allAlarmNum += currentAlarmReport.getAllAlarmNum();
			 	        emergencyAlarmNum += currentAlarmReport.getEmergencyAlarmNum();
			 	        seriousAlarmNum += currentAlarmReport.getSeriousAlarmNum();
			 	        mainAlarmNum += currentAlarmReport.getMainAlarmNum();
			 	        minorAlarmNum += currentAlarmReport.getMinorAlarmNum();
			 	        generalAlarmNum += currentAlarmReport.getGeneralAlarmNum();
			 	        messageNum += currentAlarmReport.getMessageNum();
					    //更新统计值
					    if(!countedEntityIds.contains(entityId)){
						    countedEntityIds.add(entityId);
				 	       	totalAllAlarmNum += currentAlarmReport.getAllAlarmNum();
				 	      	totalEmergencyAlarmNum += currentAlarmReport.getEmergencyAlarmNum();
				 	     	totalSeriousAlarmNum += currentAlarmReport.getSeriousAlarmNum();
				 	    	totalMainAlarmNum += currentAlarmReport.getMainAlarmNum();
				 	   		totalMinorAlarmNum += currentAlarmReport.getMinorAlarmNum();
				 	  		totalGeneralAlarmNum += currentAlarmReport.getGeneralAlarmNum();
				 		 	totalMessageNum += currentAlarmReport.getMessageNum();
					    }
			 	        
			 		 	//输出数据行
			 		 	if(firstEntity){
	 		 	  %>
	 		 	  			<tr>
	 		 	  				<td rowspan="<%=folderEntities.get(folderId).getRowspan()%>">
	 		 	  					<%=folderEntities.get(folderId).getFolderName()%>
	 		 	  				</td>
	 		 	  				<td><%=currentAlarmReport.getEntityName()%></td>
	 		 	  				<td class="nowrap"><%=currentAlarmReport.getDisplayName()%></td>
	 		 	  				<td>
								<%if(currentAlarmReport.getAllAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick="showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '')">
										<%=currentAlarmReport.getAllAlarmNum()%>
									</a>
								<%} %>
								</td>
								<td>
								<%if(currentAlarmReport.getEmergencyAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '6')">
										<%=currentAlarmReport.getEmergencyAlarmNum()%>
									</a>
								<%} %>
								</td>
								<td>
								<%if(currentAlarmReport.getSeriousAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '5')">
										<%=currentAlarmReport.getSeriousAlarmNum()%>
									</a>
								<%} %>
								</td>
								<s:if test="mainAlarmDisable">
								<td>
								<%if(currentAlarmReport.getMainAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '4')">
										<%=currentAlarmReport.getMainAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="minorAlarmDisable">
								<td>
								<%if(currentAlarmReport.getMinorAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '3')">
										<%=currentAlarmReport.getMinorAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="generalAlarmDisable">
								<td>
								<%if(currentAlarmReport.getGeneralAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '2')">
										<%=currentAlarmReport.getGeneralAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="messageDisable">
								<td>
								<%if(currentAlarmReport.getMessageNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '1')">
										<%=currentAlarmReport.getMessageNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
	 		 	  			</tr>
	 		 	  <%
			 		 	}else{
			 	%>
			 				<tr>
	 		 	  				<td><%=currentAlarmReport.getEntityName()%></td>
	 		 	  				<td class="nowrap"><%=currentAlarmReport.getDisplayName()%></td>
	 		 	  				<td>
								<%if(currentAlarmReport.getAllAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick="showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '')">
										<%=currentAlarmReport.getAllAlarmNum()%>
									</a>
								<%} %>
								</td>
								<td>
								<%if(currentAlarmReport.getEmergencyAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '6')">
										<%=currentAlarmReport.getEmergencyAlarmNum()%>
									</a>
								<%} %>
								</td>
								<td>
								<%if(currentAlarmReport.getSeriousAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '5')">
										<%=currentAlarmReport.getSeriousAlarmNum()%>
									</a>
								<%} %>
								</td>
								<s:if test="mainAlarmDisable">
								<td>
								<%if(currentAlarmReport.getMainAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '4')">
										<%=currentAlarmReport.getMainAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="minorAlarmDisable">
								<td>
								<%if(currentAlarmReport.getMinorAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '3')">
										<%=currentAlarmReport.getMinorAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="generalAlarmDisable">
								<td>
								<%if(currentAlarmReport.getGeneralAlarmNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '2')">
										<%=currentAlarmReport.getGeneralAlarmNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
								<s:if test="messageDisable">
								<td>
								<%if(currentAlarmReport.getMessageNum()==0){ %>
									0
								<%}else{ %>
									<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '<%=currentAlarmReport.getEntityId()%>', '1')">
										<%=currentAlarmReport.getMessageNum()%>
									</a>
								<%} %>
								</td>
								</s:if>
	 		 	  			</tr>
			 	<%
			 		 	}
			 		 	firstEntity = false;
					}
					//进行地域小计
				%>
					<tr class="summaryTd">
					<%if(firstEntity){%>
						<td><%=folderEntities.get(folderId).getFolderName()%></td>
					<%}%>
						<td>@report.subtotal@</td>
						<td></td>
						<td>
							<%if(allAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '')"><%=allAlarmNum%></a>
							<%} %>
						</td>
						<td>
							<%if(emergencyAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '6')"><%=emergencyAlarmNum%></a>
							<%} %>
						</td>
						<td>
							<%if(seriousAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '5')"><%=seriousAlarmNum%></a>
							<%} %>
						</td>
						<s:if test="mainAlarmDisable">
							<td>
								<%if(mainAlarmNum==0){ %>
								0
								<%}else{ %>
								<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '4')"><%=mainAlarmNum%></a>
								<%} %>
							</td>
						</s:if>
						<s:if test="minorAlarmDisable">
							<td>
								<%if(minorAlarmNum==0){ %>
								0
								<%}else{ %>
								<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '3')"><%=minorAlarmNum%></a>
								<%} %>
							</td>
						</s:if>
						<s:if test="generalAlarmDisable">
							<td>
								<%if(generalAlarmNum==0){ %>
								0
								<%}else{ %>
								<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '2')"><%=generalAlarmNum%></a>
								<%} %>
							</td>
						</s:if>
						<s:if test="messageDisable">
							<td>
								<%if(messageNum==0){ %>
								0
								<%}else{ %>
								<a href="#" onclick = "showAlarmDetail('<%=folderId%>', '', '1')"><%=messageNum%></a>
								<%} %>
							</td>
						</s:if>
					</tr>
				<%
				}
				//进行总计
				%>
					<tr class="summaryTd">
				 		<td></td>
						<td>@report.totalling@</td>
						<td></td>
						<td>
						<%if(totalAllAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '')"><%=totalAllAlarmNum%></a>
						<%} %>
						</td>
						<td>
						<%if(totalEmergencyAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '6')"><%=totalEmergencyAlarmNum%></a>
						<%} %>
						</td>
						<td>
						<%if(totalSeriousAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '5')"><%=totalSeriousAlarmNum%></a>
						<%} %>
						</td>
						<s:if test="mainAlarmDisable">
							<td>
							<%if(totalMainAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '4')"><%=totalMainAlarmNum%></a>
							<%} %>
							</td>
						</s:if>
						<s:if test="minorAlarmDisable">
							<td>
							<%if(totalMinorAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '3')"><%=totalMinorAlarmNum%></a>
							<%} %>
							</td>
						</s:if>
						<s:if test="generalAlarmDisable">
							<td>
							<%if(totalGeneralAlarmNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '2')"><%=totalGeneralAlarmNum%></a>
							<%} %>
							</td>
						</s:if>
						<s:if test="messageDisable">
							<td>
							<%if(totalMessageNum==0){ %>
							0
							<%}else{ %>
							<a href="#" onclick = "showAlarmDetail('', '', '1')"><%=totalMessageNum%></a>
							<%} %>
							</td>
						</s:if>
					</tr>
				<%
				%>
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