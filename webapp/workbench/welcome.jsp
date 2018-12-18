<%@ page language="java" contentType="text/html;charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ux/interface.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.workbench.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/fisheye.css" />
<!--[if lt IE 7]>
 <style type="text/css">
 .dock img { behavior: url(iepngfix.htc) }
 </style>
<![endif]-->
</head><body style="background:gray" scroll=no>
<!--bottom dock -->
	<div class="dock" id="dock2">
		<div class="dock-container2">
			<a class="dock-item2" onclick="jumpToMyDesktop()" href="#">
			<span><fmt:message key="a.myWorkBench" bundle="${workbench}"/></span>
			<img src="../images/fisheye/home.png" /></a> 
			
			<a class="dock-item2" onclick="jumpToTopology()" href="#">
			<span><fmt:message key="welcome.NetworkTopologyDiscovery" bundle="${workbench}"/></span>
			<img src="../images/fisheye/link.png" /></a> 
			
			<a class="dock-item2" onclick="jumpToPersonal()" href="#">
			<span><fmt:message key="welcome.Personalization" bundle="${workbench}"/></span>
			<img src="../images/fisheye/history.png" /></a> 
			
			<a class="dock-item2" href="../help/overview.jsp">
			<span><fmt:message key="welcome.advantageofworkbench" bundle="${workbench}"/></span>
			<img src="../images/fisheye/email.png" /></a> 
			
			<a class="dock-item2" href="../help/tutorial.html">
			<span><fmt:message key="welcome.Howtouseworkbench" bundle="${workbench}"/></span>
			<img src="../images/fisheye/portfolio.png" /></a> 
			
			<a class="dock-item2" href="../help/index.jsp" target="_blank">
			<span><fmt:message key="welcome.Help" bundle="${workbench}"/></span>
			<img src="../images/fisheye/calendar.png" /></a>
		</div>
	</div>
	<!--dock menu JS options -->
<script type="text/javascript">
	$(document).ready(
		function()
		{
			$('#dock2').Fisheye(
				{
					maxWidth: 80,
					items: 'a',
					itemsText: 'span',
					container: '.dock-container2',
					itemWidth: 50,
					proximity: 80,
					alignment : 'left',
					valign: 'bottom',
					halign : 'center'
				}
			)
		}
	);
	function jumpToTopology() {
		window.parent.startTopoDiscovery();
	}
	function jumpToMyDesktop() {
		window.parent.showMyDesktop();
	}
	function jumpToMyNetwork() {
		window.parent.addView('mynetwork', I18N.welcome.MyNetwork, 'networkIcon', 'topology/myNetwork.jsp');
	}	
	function jumpToMyMonitor() {
		window.parent.addView('myMonitor', I18N.welcome.MyMonitor, 'monitorIcon', 'monitor/myMonitor.jsp');
	}
	function jumpToPersonal() {
		window.parent.onPreferencesClick();
	}
	// show property interfaces by main page
	function getPropertyUrl() {
		return null;
	}

	// for tab changed start
	function tabActivate() {
		window.top.setStatusBarInfo('', '');
	}
	function tabDeactivate() {
	}
	function tabRemoved() {
	}
	function tabShown() {
		window.top.setStatusBarInfo('', '');
	}
	// for tab changed end
</script>
</body></html>