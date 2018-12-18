<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<jsp:useBean id="entity" scope="request" class="com.topvision.ems.facade.domain.Entity" />
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:ResourceBlock>
<div class="ultab">
<%
	Object module = request.getParameter("module");
%>
<ul>
	<li><a href="/portal/showEntitySnapJsp.tv?module=1&entityId=${entityId}">@EPON/SUPPLY.entitySnap@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/epon/standardOlt/showStandardOltConfig.tv?module=2&entityId=${entityId}">@EPON/SUPPLY.configInfo@</a></li>
	<li class="secondTabLine"></li>
    <li><a href="/epon/standardOlt/showOltPortList.tv?module=3&entityId=${entityId}">@EPON/olt.portList@</a>
    </li>
    <li class="secondTabLine"></li>
	<li><a href="/epon/standardOlt/showStandardOltOnuView.tv?module=4&entityId=${entityId}">@EPON/SUPPLY.downDevice@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/epon/standardOlt/showStandardOltAlert.tv?module=5&entityId=${entityId}">@EPON/SUPPLY.deviceAlert@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/epon/standardOlt/showStandardOltPerfView.tv?module=7&entityId=${entityId}">@EPON/SUPPLY.devicePerf@</a></li>
</ul>
<script type="text/javascript">
	(function(){
		var pageModule = '<%= module %>' || 1,
		selectedItem = $(String.format("A[href*='module={0}']",pageModule));
		selectedItem.parent().addClass("selected");

	})()
</script>
</div>
</Zeta:ResourceBlock>