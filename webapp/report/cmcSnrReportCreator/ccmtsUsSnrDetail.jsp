<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
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


//构造下拉框
var cctype = EntityType.getCcmtsType();
var entityType = '<s:property value="entityType"/>';
var displayName;
$.ajax({
    url:'/entity/loadSubEntityType.tv?type=' + cctype,
    type:'POST',
    dateType:'json',
    success:function(response) {
  	  var entityTypes = response.entityTypes 
  	  for(var i = 0; i < entityTypes.length; i++){
           if(entityType == entityTypes[i].typeId){
           	displayName = entityTypes[i].displayName;
           }
  	  }
  	  $("#entityType").val(entityType)
  	  $("#statisRange1").text(displayName);
    },
    error:function(entityTypes) {},
    cache:false
});

$(function(){
	if(!EntityType.isCcmtsWithoutAgentType(entityType)){
		$('.ccWithoutAgentTh').hide();
		$('.ccWithoutAgentTd').hide();
	}
})

//生成EXCEL
function exportExcelClick() {
	var stTime = '<s:property value="stTime"/>';
	var etTime = '<s:property value="etTime"/>';
	var entityType = '<s:property value="entityType"/>';
	var entityId = '<s:property value="entityId"/>';
	var folderId = '<s:property value="folderId"/>';
	window.location.href="/cmc/report/exportSnrDetailReportToExcel.tv?stTime=" + stTime + "&etTime=" + etTime + "&entityType=" + entityType +  "&entityId=" + entityId + "&folderId =" + folderId; 
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
		<h1>@report.ccmtsSnrDetail@</h1>
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
				<span>@report.deviceType@:</span>
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
					<th>#</th>
					<th class="ccWithoutAgentTh">@report.belongOlt@</th>
					<th>@report.channelName@</th>
					<th>@report.belongCcmts@</th>
					<th>@report.below20dbTimes@</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="cmcSnrReportDetailList" status="statu">
					<tr>
						<td><s:property value="#statu.index+1" /></td>
						<td class="ccWithoutAgentTd"><s:property value="entityName" /></td>
						<td><s:property value="channelName" /></td>
						<td><s:property value="cmcName" /></td>
						<td><s:property value="lowTimes" /></td>
					</tr>
				</s:iterator>
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