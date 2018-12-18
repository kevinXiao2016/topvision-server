<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.cm.resources" var="resources"/>
<link rel="stylesheet" type="text/css" href="../css/main.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.openIcon" />.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.closeIcon" />.png) no-repeat;}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-menu.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<link href="../css/reset.css" type="text/css" rel="stylesheet"></link>
<link href="../css/jquery.treeview.css" type="text/css" rel="stylesheet"></link>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.wresize.js"></script>
<script type="text/javascript">
$(function(){
	//加载树形菜单;
	$("#tree").treeview({ 
		animated: "fast",
		control:"#sliderOuter"
	});	//end treeview;
	$("#sliderLeftBtn").click(function(){
		$("#bar").stop().animate({left:0});			
	})
	$("#sliderRightBtn").click(function(){
		$("#bar").stop().animate({left:88});		
	})
	
	//点击树形节点变橙色背景;
	$(".linkBtn").live("click",function(){
		$(".linkBtn").removeClass("selectedTree");
		$(this).addClass("selectedTree");
	});//end live;
	
	function autoHeight(){
		var h = $(window).height();
		var h1 = $("#putSlider").outerHeight();
		var h2 = 20; //因为#putTree{padding:10px};
		var h3 = $("#threeBoxLine").outerHeight();
		var h4 = $("#putBtn").outerHeight();
		var putTreeH = h - h1 - h2 - h3 - h4;
		if(putTreeH > 20){
			$("#putTree").height(putTreeH);
		}	
	};//end autoHeight;
	
	autoHeight();
	$(window).wresize(function(){
		autoHeight();
	});//end resize;
})
</script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	
<script type="text/javascript">
<%
boolean cmMonitorView = uc.hasPower("cmMonitorView");
boolean pollTask = uc.hasPower("pollTask");
boolean pollObject = uc.hasPower("pollObject");
boolean pollQuota = uc.hasPower("pollQuota");
boolean pollConfig = uc.hasPower("pollConfig");
%>
/*显示监控对象列表*/
function showPollObject(){
	window.parent.addView("pollobjectInfo", I18N.cmPoll.pollObject, "icoB6", "/cmpoll/showPollObjectList.tv");	
}

function showCmPollTask(){
	window.parent.addView("pollTask", I18N.cmPoll.pollTask, "icoH11", "/cm/cmpolltasklist.jsp");
}
function showCmMonitor(){
	window.parent.addView("cmMonitor", I18N.cmPoll.cmPOll, "icoH10", "/cmpoll/showCmMonitorView.tv");
}
function showPollQuota(){
	window.parent.addView("cmPollQuota", I18N.cmPoll.quotaList, "icoH12", "/cm/cmPollQuotaList.jsp");
}
function showPollConfig(){
	window.parent.addView("cmPollConfig", I18N.cmPoll.config, "icoH14", "/cmpoll/showCmPollConfig.tv");
}
function showPollAlert(){
	window.parent.addView("cmPollAlert", I18N.cmPoll.thresholdAlert, "icoH13", "/cmpoll/showCmPollAlertList.tv");
}
Ext.BLANK_IMAGE_URL = "../images/s.gif";

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
	<div class="putTree" id="putTree">
		<div style="width:100%; overflow:hidden;">
			<ul id="tree" class="filetree">
				<% if(cmMonitorView){%>
					<li>
						<span class="icoH10">
							<a href="javascript:;" class="linkBtn" onclick="showCmMonitor()" name="">
								<fmt:message key="cmPoll.cmPOll" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }if(pollTask){%>
					<li>
						<span class="icoH11">
							<a href="javascript:;" class="linkBtn" onclick="showCmPollTask()" name="">
								<fmt:message key="cmPoll.pollTask" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }if(pollObject){%>
					<li>
						<span class="icoB6">
							<a id="cmList" href="javascript:;" class="linkBtn" onclick="showPollObject()" name="">
								<fmt:message key="cmPoll.pollObject" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }if(pollQuota){%>
					<li>
						<span class="icoH12">
							<a id="" href="javascript:;" class="linkBtn" onclick="showPollQuota()" name="">
								<fmt:message key="cmPoll.quotaList" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }if(pollConfig){%>
					<li>
						<span class="icoH14">
							<a id="" href="javascript:;" class="linkBtn" onclick="showPollConfig()" name="">
								<fmt:message key="cmPoll.config" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }%>
				<% if(uc.hasPower("thresholdAlert")){%>
					<li>
						<span class="icoH13">
							<a id="" href="javascript:;" class="linkBtn" onclick="showPollAlert()" name="">
								<fmt:message key="cmPoll.thresholdAlert" bundle="${resources}"/>
							</a>
						</span>
					</li>
				<% }%>
			</ul>
		</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn"></div>
</body></html>
