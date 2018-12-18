<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<fmt:setBundle basename="com.topvision.ems.performance.resources" var="performance"/>
<fmt:setBundle basename="com.topvision.ems.cmc.spectrum.resources" var="spectrum"/>
<link rel="stylesheet" type="text/css" href="../css/main.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css"/>
<link href="../css/reset.css" type="text/css" rel="stylesheet"></link>
<link href="../css/jquery.treeview.css" type="text/css" rel="stylesheet"></link>

<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.openIcon" />.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.closeIcon" />.png) no-repeat;}
</style>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-menu.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.performance.resources,com.topvision.ems.cmc.spectrum.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js" ></script>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.slimscroll.min.js"></script>	
<script type="text/javascript">
var userId = <%=uc.getUser().getUserId()%>;
// my workbench module
var favouriteFolderTree = null;
var favouriteFolderRoot = null;
var favouriteFolderMenu = null;
var openFavouriteFolderItem = null;
var deleteFavouriteFolderItem = null;
var renameFavouriteFolderItem = null;
</script>
<script type="text/javascript">
function showDispersion(){
	window.parent.addView("dispersion-list", I18N.Performance.dispersion, "icoG14", "/dispersion/showDispersion.tv");
}
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
	
	var h = $(window).height();
	var h1 = $("#putSlider").outerHeight();
	var h2 = 20; //因为#putTree{padding:10px};
	var h3 = $("#threeBoxLine").outerHeight();
	var h4 = $("#putBtn").outerHeight();
	var putTreeH = h - h1 - h2 - h3 - h4;
	if(putTreeH < 20){
		putTreeH = 20;
	}
	
})

function showDailyMaxCm(){
	window.parent.addView("report_20009", I18N.report.cmDailyNumStaticReport, "icoG14", "/cmc/report/showCmDailyNumStatic.tv");
}

function showCmPollCollect(){
	window.parent.addView("cmPollCollect", I18N.cmPoll.collectList, "icoG2", "/cmpoll/showCmPollCollectList.tv");
}
</script>
<script type="text/javascript" src="/performance/js/menuNew.js"></script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>	

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
			<%if (uc.hasSupportModule("cmc")) {%>
			<%if(uc.hasPower("cmcUserNumHis")){ %>
        	<li>
        		<span class="icoG4">
					<a href="javascript:;" class="linkBtn" onclick="showCmcUserNum()" name="">
						<fmt:message key="cmCpe.cmcUserNumHis" bundle="${performance}"/>
					</a>
				</span>
        	</li>
        	<%}if(uc.hasPower("cmDailyNumStatic")){%>
        	<li id="20009">
        		<span class="icoG14">
        			<a href="javascript:;" onclick="showDailyMaxCm()" class="linkBtn">
        				<fmt:message key="report.cmDailyNumStaticReport" bundle="${performance}"/>
        			</a>
        		</span>
        	</li>
        	<%}if(uc.hasPower("cmcAllCcmtsUserNum")){ %>
        	<li>
        		<span class="icoG2">
					<a href="javascript:;" class="linkBtn" onclick="showCmcAllCcmtsUserNum()" name="">
						<fmt:message key="cmCpe.cmcAllCcmtsUserNum" bundle="${performance}"/>
					</a>
				</span>
        	</li>
        	<%} %>
        	<%-- 
        	<%if(uc.hasPower("cmCpeQuery")){ %>
        	<li>
        		<span class="icoD1">
					<a href="javascript:;" class="linkBtn" onclick="showCmLocate()" name="">
						<fmt:message key="Performance.cmLocate" bundle="${performance}"/>
					</a>
				</span>
        	</li>
        	<%} %> --%>
        	
            <%}%>
            <%if(uc.hasPower("cmCollectList") && uc.hasSupportModule("cmc")){ %>
            <li><span class="icoG2"><a href="javascript:;" class="linkBtn" onclick="showCmPollCollect()" ><fmt:message key="cmPoll.collectList" bundle="${performance}"/></a></span></li>
            <%} %>
            <%if(uc.hasPower("dispersion") && uc.hasSupportModule("cmc")){ %>
            <li><span class="icoG14"><a href="javascript:;" class="linkBtn" onclick="showDispersion()" ><fmt:message key="Performance.dispersion" bundle="${performance}"/></a></span></li>
			<%} %>
            <!-- EPON PERFORMANCE CONFIGURATION UNIT -->
        	<% if(uc.hasSupportModule("olt")){%>
        	<% if(uc.hasPower("oltPerformanConfig")||uc.hasPower("oltGlobalConfig")||uc.hasPower("oltPerfMgmt")){ %>
        	<li><span class="folder"><fmt:message key="Performance.eponPerformanConfig" bundle="${performance}"/></span>
       			<ul>
       				<%if (uc.hasPower("oltPerformanConfig")) {%>
       				<li>
		        		<span class="icoE4">
							<a href="javascript:;" class="linkBtn" onclick="eponPerfParamConfig()" name="">
								<fmt:message key="Performance.oltPerformanConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<%}	%>
		        	<%if (uc.hasPower("oltGlobalConfig")) {%>
		       		<li>
		        		<span class="icoE2">
							<a href="javascript:;" class="linkBtn" onclick="showOltGlobalPerfConfig()" name="">
								<fmt:message key="Performance.oltGlobalConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<%}	%>
		        	<% if (uc.hasPower("oltPerfMgmt")) {%>
                       <li>
		        		<span class="icoE3">
							<a href="javascript:;" class="linkBtn" onclick="showOltPerfConfig()" name="">
								<fmt:message key="Performance.oltPerfMgmt" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
                    <%}%>
       			</ul>
       		</li>
       		<%} %>
       		<%}%>
       		
       		<!-- CMC PERFORMANCE UNIT -->
      		<% if(uc.hasSupportModule("cmc")){%>
       		<% if(uc.hasPower("cmcPerfParamConfig")||uc.hasPower("ccGlobalConfig")||uc.hasPower("ccPerfMgmt")){ %>
       		<li><span class="folder"><fmt:message key="Performance.cmcPerformanceConfig" bundle="${performance}"/></span>
       			<ul>
       				<%if (uc.hasPower("cmcPerfParamConfig")) {%>
	       			<li>
		        		<span class="icoE6">
							<a href="javascript:;" class="linkBtn" onclick="cmcPerfParamConfig()" name="">
								<fmt:message key="Performance.cmcPerfParamConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<%}%>
		        	<%if (uc.hasPower("ccGlobalConfig")) {%>
		        	<li>
		        		<span class="icoE5">
							<a href="javascript:;" class="linkBtn" onclick="showCCGlobalPerfConfig()" name="">
								<fmt:message key="Performance.ccGlobalConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<%}%>
		        	<%if (uc.hasPower("ccPerfMgmt")) {%>
		        	<li>
		        		<span class="icoE7">
							<a href="javascript:;" class="linkBtn" onclick="showCCPerfConfig()" name="">
								<fmt:message key="Performance.ccPerfMgmt" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<%}%>
	       		</ul>
			</li>
			<%}%>
   			<%}%>
 
			<!-- ONU PERFORMANCE UNIT -->
            <% if(uc.hasSupportModule("onu")){%>
            <% if(uc.hasPower("onuPerfParamsConfig")||uc.hasPower("onuGlobalConfig")||uc.hasPower("onuPerfTempMgmt")){ %>
			<li><span class="folder"><fmt:message key="Performance.onuPerfConfig" bundle="${performance}"/></span>
       			<ul>
       				<%if (uc.hasPower("onuPerfParamsConfig")) {%>
	       			<li>
		        		<span class="icoE6">
							<a href="javascript:;" class="linkBtn" onclick="showOnuPerfParamConfig()">
								<fmt:message key="Performance.onuPerfParamsConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<% } %>
		        	<%if (uc.hasPower("onuGlobalConfig")) {%>
		        	<li>
		        		<span class="icoE5">
							<a href="javascript:;" class="linkBtn" onclick="showOnuGlobalPerfConfig()">
								<fmt:message key="Performance.onuGlobalConfig" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	 <% } %>
		        	 <%if (uc.hasPower("onuPerfTempMgmt")) {%>
		        	<li>
		        		<span class="icoE7">
							<a href="javascript:;" class="linkBtn" onclick="showOnuPerfTempConfig()">
								<fmt:message key="Performance.onuPerfTempMgmt" bundle="${performance}"/>
							</a>
						</span>
		        	</li>
		        	<% } %>
	       		</ul>
			</li>
            <% } %>
            <% } %>
 
			<!-- SPECTRUM UNIT -->
			<% if(uc.hasSupportModule("cmc")){%>
            <% if(uc.hasPower("oltSpectrumConfig")||uc.hasPower("cmtsSpectrumConfig")||uc.hasPower("spectrumVideoMgmt")||uc.hasPower("spectrumAlertConfig")){ %>
        	<li><span class="folder"><fmt:message key="performance.spectrum" bundle="${performance}"/></span>
        		<ul>
        			<% if(uc.hasSupportModule("olt") && uc.hasPower("oltSpectrumConfig")){ %>
        			<li>
           			 	<span class="icoG7">
            				<a href="javascript:;" class='linkBtn' onclick="showOltSpectrumConfig()">
            					<fmt:message key="spectrum.oltSpectrumCollectConfig" bundle="${spectrum}"/>
            				</a>
            			</span>
            		</li>
            		<% }if(uc.hasPower("cmtsSpectrumConfig")){ %>
        			<li>
           			 	<span class="icoG7">
            				<a href="javascript:;" class='linkBtn' onclick="showCmtsSpectrumConfig()">
            					<fmt:message key="spectrum.spectrumCollectConfig" bundle="${spectrum}"/>
            				</a>
            			</span>
            		</li>
            		<% }if(uc.hasPower("spectrumVideoMgmt")){ %>
            		<li>
           			 	<span class="icoE14">
            				<a href="javascript:;" class='linkBtn' onclick="showSpectrumVideoMgmt()">
            					<fmt:message key="performance.spectrumVideoMgmt" bundle="${performance}"/>
            				</a>
            			</span>
            		</li>
            		<%} if(uc.hasPower("spectrumAlertConfig")){ %> 
            		<li>
           			 	<span class="icoG7">
            				<a href="javascript:;" class='linkBtn' onclick="showSpectrumAlertConfig()">
            					<fmt:message key="spectrum.alertConfig" bundle="${spectrum}"/>
            				</a>
            			</span>
            		</li>
            		<%}%>
        		</ul>
        	</li>
        	<%} %>
        	<%} %>
		</ul>
		</div>
	</div>
	
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<%if (uc.hasPower("performanceBatchConfig")) {%>
			<li><a href="#" class="icoE12" id="" onclick="showBatch();">
			<fmt:message key="Performance.batchConfig" bundle="${performance}"/></a></li>
			<%}%>
			<!-- MONITOR UNIT -->
			<%if (uc.hasPower("perfTemplateMgmt")) {%>
			<li>
				<a class="icoE1" href="javascript:;" class="linkBtn" onclick="showTargetManage()">
					<fmt:message key="Performance.targetManage" bundle="${performance}"/>
				</a>
			</li>
			<li>
				<a class="icoE1" href="javascript:;" class="linkBtn" onclick="showPerfTemplate()">
					<fmt:message key="Performance.perfTemplateMgmt" bundle="${performance}"/>
				</a>
			</li>
			<%}%>
			<%if (uc.hasSupportModule("cmc")) {%>
				<%if(uc.hasPower("terminalCollectConfig")) {%>
	            <li>
	            	<a id="terminalCollectConfig" href="javascript:;" class='icoE13' onclick="showTerminalCollectConfig()">
	            		<fmt:message key="cmCpe.terminalConfig" bundle="${resources}"/>
	            	</a>
	            </li>
	            <%} %>
            <%} %>
		</ol>
	</div>
	
</body></html>
 
