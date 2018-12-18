<%@ page language="java" pageEncoding="UTF-8"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource" />
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<jsp:useBean id="entity" scope="request" class="com.topvision.ems.facade.domain.Entity" />
<%
String entityId = String.valueOf(entity.getEntityId());
String moduleStr = request.getParameter("module");
if (moduleStr == null) {
	moduleStr = "1";
}
int module = Integer.parseInt(moduleStr);
%>
<div class=ultab>
<ul>
	<%
		if (module == 1) {
	%>
	<li class=selected><a href="../portal/showEntitySnapJsp.tv?module=1&entityId=<%= entityId %>"><fmt:message key="SUPPLY.entitySnap" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../portal/showEntitySnapJsp.tv?module=1&entityId=<%= entityId %>"><fmt:message key="SUPPLY.entitySnap" bundle="${resource}" /></a></li>
	<%
		}
	%>
	<%
		if (module == 2) {
	%>
	<li class=selected><a href="../entity/showEditEntityJsp.tv?module=2&entityId=<%= entityId %>"><fmt:message key="SUPPLY.configInfo" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../entity/showEditEntityJsp.tv?module=2&entityId=<%= entityId %>"><fmt:message key="SUPPLY.configInfo" bundle="${resource}" /></a></li>
	<%
		}
	%>	
	<%
		if (module == 3) {
	%>
	<li class=selected><a href="../port/showPortsJsp.tv?module=3&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.portInfo" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../port/showPortsJsp.tv?module=3&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.portInfo" bundle="${resource}" /></a></li>
	<%
		}
	%>
	
	<%
		if (entity.getType() < 72) { 
		if (module == 8) {
	%>
	<li class=selected><a href="../link/showEntityLinkTableJsp.tv?module=8&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.linkInfo" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../link/showEntityLinkTableJsp.tv?module=8&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.linkInfo" bundle="${resource}" /></a></li>
	<%
		}
		}
	%>	
	
	<%
		if (entity.isSnmpSupport()) {
			if (module == 4) {
	%>
	<li class=selected><a href="../realtime/showEntityMibJsp.tv?module=4&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.mibInfo" bundle="${resource}" /></a></li>
	<%
			} else {
	%>
	<li><a href="../realtime/showEntityMibJsp.tv?module=4&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.mibInfo" bundle="${resource}" /></a></li>
	<%
			}
			if (module == 7) {
	%>
		<li class=selected><a href="../entity/showLogicPane.tv?module=7&entityId=<%= entityId %>"><fmt:message key="SUPPLY.panel" bundle="${resource}" /></a></li>
	<%
			} else {
	%>
		<li><a href="../entity/showLogicPane.tv?module=7&entityId=<%= entityId %>"><fmt:message key="SUPPLY.panel" bundle="${resource}" /></a></li>
	<%
			}
		}
	%>
	<%
		if ((entity.getType() == EntityType.DESKTOP_TYPE && entity.isAgentSupport()) || entity.getType() == EntityType.SERVER_TYPE) {
		if (module == 5) {
	%>
	<li class=selected><a href="../entity/showInstalledSoftwareJsp.tv?module=5&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.installedSoftware" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../entity/showInstalledSoftwareJsp.tv?module=5&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.installedSoftware" bundle="${resource}" /></a></li>
	<%
		}
		
		if (module == 8) {
	%>
	<li class=selected><a href="../entity/showActiveProcessJsp.tv?module=8&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.activeProcesses" bundle="${resource}" /></a></li>
	<%	    
		} else {
	%>
	<li><a href="../entity/showActiveProcessJsp.tv?module=8&entityId=<%= entityId %>"><fmt:message key="networkEntityInc.activeProcesses" bundle="${resource}" /></a></li>
	<%
		}
		}
	%>

	<%
		if (module == 6) {
	%>
	<li class=selected><a href="../alert/showEntityAlertJsp.tv?module=6&entityId=<%= entityId %>"><fmt:message key="SUPPLY.deviceAlert" bundle="${resource}" /></a></li>
	<%
		} else {
	%>
	<li><a href="../alert/showEntityAlertJsp.tv?module=6&entityId=<%= entityId %>"><fmt:message key="SUPPLY.deviceAlert" bundle="${resource}" /></a></li>
	<%
		}
	%>
</ul>
</div>
