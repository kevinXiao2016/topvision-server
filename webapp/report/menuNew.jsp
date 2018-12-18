<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.report.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<Zeta:Loader>
	css css.main
	css css.reportTemplate
	css css.reset
	library Jquery
	library ext
	library zeta
    module report
    import js/jquery/jquery.wresize
    import js/jquery/jquery.treeview
    import js.tools.treeBuilder
    import report/javascript/reportMenu
</Zeta:Loader>
<style type="text/css">
#report_tree a:hover {
	background: #39c;
	color: #fff;
}
#report_tree a {
	padding: 3px;
	color: #069;
}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
</style>
<script type="text/javascript">
function lookForReportDetail(id, name, url){
	window.parent.addView("report" + id, name, "reportIcon", url);
}

function showReportDetail(id, name){
	window.top.addView("report-" + id, name, "reportIcon", "/report/showSingleReport.tv?reportId="+id);
}

function showAllReport() {
	window.parent.addView("allreport", '@report.allReports@', "icoG1", "report/showAllReport.tv");
}
function showAllReportTask() {
	window.parent.addView("allreporttask", '@report.allReportTask@', "icoG11", "report/showAllReportTask.tv");
}
function newReportTask() {
	window.parent.createDialog("modalDlg", '@report.createReportTask@', 600, 500, '/report/showNewReportTask.tv?r='+Math.random(), null, true, true);
}
</script>
</head>
	<body>
		<div class="putSlider" id="putSlider">
			<div class="sliderOuter" id="sliderOuter">
				<a id="sliderLeftBtn" href="javascript:;"></a>
				<div id="slider" class="slider">
					<span id="bar" class="bar"></span>
				</div>
				<a id="sliderRightBtn" href="javascript:;"></a>
			</div>
		</div>
		<%if(uc.hasPower("report")){ %>
		<div class="putTree" id="putTree">
			<div style="width:100%; overflow:hidden;">
				<ul id="tree" class="filetree">
					<li id="resource_root">
						<span class="folder"><a href="javascript:;">@report.resourceReport@</a></span>
						<ul></ul>
					</li>
					<li id="performance_root">
						<span class="folder"><a href="javascript:;">@report.performanceReport@</a></span>
						<ul></ul>
					</li>
					<li id="alert_root">
						<span class="folder"><a href="javascript:;">@report.alertReport@</a></span>
						<ul></ul>
					</li>
					<li id="custom_root">
						<span class="folder"><a href="javascript:;">@report.customReport@</a></span>
						<ul></ul>
					</li>
				</ul>
			</div>
		</div>
		<%} %>
		<!-- <div id="newTreeContainer">
			<ul id="newTree" class="filetree">
			</ul>
		</div> -->
		<div id="threeBoxLine"></div>
		<div class="putBtn" id="putBtn">
			<%if(uc.hasPower("reportView")){ %>		
			<ol class="icoBOl">
				<li><a href="javascript:;" class="icoG11" name="@report.allReportTask@" onclick="showAllReportTask()">@report.allReportTask@</a></li>
				<li><a href="javascript:;" class="icoG1" name="@report.allGeneratedReport@" onclick="showAllReport()">@report.allGeneratedReport@</a></li>
				<li><a href="javascript:;" class="icoG12" name="@report.createReportTask@" onclick="newReportTask()">@report.newReportTask@</a></li>
			</ol>
			<%} %>
		</div>
		<script type="text/javascript">
		$(function(){
			
			function autoHeight(){
				var h = $(window).height();
				var h1 = $("#putSlider").outerHeight();
				var h2 = 20; //因为#putTree{padding:10px};
				var h3 = $("#threeBoxLine").outerHeight();
				var h4 = $("#putBtn").outerHeight();
				var putTreeH = h - h1 - h2 - h3 - h4;
				if(putTreeH > 20){
					$("#putTree").height(putTreeH-2);
				}	
			};//end autoHeight;
			
			autoHeight();
			$(window).wresize(function(){
				autoHeight();
			});//end resize;
		})
		</script>
	</body>
</Zeta:HTML>
