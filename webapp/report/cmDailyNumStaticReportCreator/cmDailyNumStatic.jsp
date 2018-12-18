<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<!-- 此处需要使用高版本的jquery库和highstock库，来解决动态获取数据的问题  -->
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
	library Ext
	library Zeta
    module report
	css css.report
	import js/nm3k/nm3kPickDate
	import js/highcharts-3.0.5/highcharts
	import js/highcharts-3.0.5/modules/exporting.src
	import performance/js/jquery-ui-1.10.3.custom.min
	import report.javascript.CommonMethod
	import report.cmDailyNumStaticReportCreator.cmDailyNumStatic
</Zeta:Loader>
<style type="text/css">
.queryDiv{
	width: 700px;
	margin: 20px auto;
	background: #FFF;
	border: solid 1px #BDBDBD;
	padding: 30px 50px 0;
}
.zebraTableRows td{
	height: 24px;
}
.vertical-middle{
	vertical-align: middle;
}
.rangeSelect{
	width: 160px;
}
#content{
	padding: 20px;
	width: 760px;
}
#queryConditionDiv{
	margin-top: 30px;
}
#queryConditionDiv p{
	height: 22px;
	padding: 4px;
}
#queryConditionDiv p span{
	font-size: 16px;
	line-height: 18px;
}
#highStockDiv{
	border: 1px solid #0088CC;
	height: 400px;
}
.container {
	display: inline-block;
}
.x-btn button{
	color: #000000;
}
#stratTime, #endTime{ width:170px;}
#stratTime .x-form-field-wrap, #endTime .x-form-field-wrap{width:auto !important;}
</style>
<script type="text/javascript">
var rangeList = ${rangeList};
var lang = lang;
var chart = null;
</script>
</head>
<body class="whiteToBlack">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<ol class="upChannelListOl noWidthCenter">
			<li><div id="exportDiv" class="splitBtn"></div></li>
        	<!-- <li><a id="printLink" href="javascript:onPrintClick();" class="normalBtnBig disabledAlink" onclick="return checkDisable(this);"><span><i class="miniIcoPrint"></i>@report.printGraph@</span></a></li> -->
        	<li><a href="javascript:;" class="normalBtnBig" onclick="optionClick()"><span><i class="miniIcoManager"></i>@COMMON.option@</span></a></li>
    	 </ol>
	</div>
	
	<!-- 查询条件区域 -->
	<div id="queryDiv" class="queryDiv zebraTableCaption" style="display: none;clear: both;">
		<div class="zebraTableCaptionTitle"><span>@tip.queryCondition@</span></div>
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="120">@report.startTime@@report.maohao@</td>
					<td colspan="2"><div id="stratTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.endTime@@report.maohao@</td>
					<td colspan="2"><div id="endTime"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@report.statScope@@report.maohao@</td>
					<td>
						<select id="rangeSelect" class="normalSel" style="width: 170px;" onchange="loadRangeDevice()">
							<option value="all">@tip.wholeScope@</option>
							<option value="folders">@report.folder@</option>
							<% if(uc.hasSupportModule("olt")){%>
							<option value="olts">OLT</option>
							<% } %>
							<option value="cmcs">CMTS</option>
						</select>
						<span id="rangeTd"></span>
					</td>
					<td>
						<span class="vertical-middle" id="alarmSelectDevice" style="color:#FF0000;display: none;">
							<img class="vertical-middle" src="/images/performance/alarm.png" alt="" />
							<span class="vertical-middle">@tip.pleaseSelectOne@</span>
						</span>
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="3">
 						<!-- <a href="javascript:;" class="normalBtn" onclick="query()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a> -->
						<a href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="report-content-div" id="content">
		<h1>@report.cmDailyNumStaticReport@</h1>
		<h3 id="queryTime"></h3>
		<div id="queryConditionDiv">
			<h2>@report.condtions@</h2>
			<p>
				<span>@report.statRange@:</span>
				<span id="staticPeroid"></span>
			</p>
			<p>
				<span>@report.statScope@:</span>
				<span id="statisRange"></span>
			</p>
		</div>
		<div id="highStockDiv"></div>
	</div> 
	
	<!-- 功能按钮区域  -->
	<div class="buttonDiv  clearBoth">
		<ol class="upChannelListOl noWidthCenter">
			<li><div id="exportDiv_" style="margin-top: 1px;"></div></li>
        	<!-- <li><a href="javascript:;" class="normalBtnBig" onclick="onPrintClick()"><span><i class="miniIcoPrint"></i>@report.printGraph@</span></a></li> -->
        	<li><a href="javascript:;" class="normalBtnBig" onclick="optionClick()"><span><i class="miniIcoManager"></i>@COMMON.option@</span></a></li>
    	 </ol>
	</div>
</body>
</Zeta:HTML>