<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/js/json2.js"></script>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
	css css.reportTemplate
	library ext
	library zeta
	plugin DateTimeField
	plugin LovCombo
	plugin DropdownTree
    module report
    import js/jquery/nm3kToolTip
    import performance/js/select2
    import js/smartFixed
    import js/nm3k/nm3kPickDate
    import /report/javascript/customReportTemplate
    import /report/javascript/helper
</Zeta:Loader>
<script type="text/javascript">
var detailReportInitCondition = '${detailReportInitCondition}';
if(detailReportInitCondition!=null && detailReportInitCondition!=''){
	detailReportInitCondition = JSON.parse(detailReportInitCondition);
}
var reportId = '${reportId}';

$(function(){
	var reportTemplate = new ReportTemplate(reportId);
	reportTemplate.init();
})
</script>
</head>
<body class="grayBg" style="padding-bottom: 30px;">
	<div id="wrapper">
		<!-- Button area -->
		<div class="buttonDiv noWidthCenterOuter clearBoth">
			<ol class="upChannelListOl noWidthCenter mT10">
				<li id="topPutExportBtn" class="splitBtn"></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="printview()"><span><i class="miniIcoInfo"></i>@COMMON.preview@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="print()"><span><i class="miniIcoPrint"></i>@COMMON.print@</span></a></li>
				<li class="optionButton" ><a href="javascript:;" class="normalBtnBig" onclick="toogleOption()"><span><i class="miniIcoManager"></i>@COMMON.option@</span></a></li>
	        	<li class="searchButton"><a id="searchTopButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
	    	 </ol>
		</div><!-- End of button area -->
		
		<!-- Query condition area -->
		<div id="queryContainer"></div>
		
		<!-- Query result area -->
		<div id="resultContainer">
			<h1 id="reportTitle"></h1>
			<h3 id="queryTime"></h3>
			<h2 id="staticConditionHd">@report.statCondition@</h2>
			<div id="staticConditionContent"></div>
			<div class="descrption">
				<span id="topLeft"></span>
				<span id="topMiddle"></span>
				<span id="topRight"></span>
			</div>
			<!-- report content table -->
			<div id="tableContainer">
				<table class="reportTable" id="reportTable">
					<thead id="reportTableHeader"></thead>
					<tbody id="reportTableBody"></tbody>
				</table>
			</div>
			<div class="descrption">
				<span id="bottomLeft"></span>
				<span id="bottomMiddle"></span>
				<span id="bottomRight"></span>
			</div>
		</div><!-- End of query result area -->
		
		<!-- Button area -->
		<div class="buttonDiv noWidthCenterOuter clearBoth">
			<ol class="upChannelListOl noWidthCenter mT10">
				<li id="bottomPutExportBtn" class="splitBtn"></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="printview()"><span><i class="miniIcoInfo"></i>@COMMON.preview@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="print()"><span><i class="miniIcoPrint"></i>@COMMON.print@</span></a></li>
				<li class="optionButton"><a href="javascript:;" class="normalBtnBig" onclick="b_toogleOption()"><span><i class="miniIcoManager"></i>@COMMON.option@</span></a></li>
	        	<li class="searchButton"><a id="searchBottomButton" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></li>
	    	 </ol>
		</div><!-- End of button area -->
	</div>
	
	<div id="totop" style="display: none;"> 
		<a class="icons gotop" href="javascript:;">@report.goTop@</a>
	</div>
	
	<iframe id="download_iframe" style="display:none;"></iframe>
</body>
</Zeta:HTML>