<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<jsp:useBean id="entity" scope="request" class="com.topvision.ems.facade.domain.Entity" />
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%
String entityId = String.valueOf(entity.getEntityId());
String moduleStr = request.getParameter("module");
if (moduleStr == null) {
	moduleStr = "1";
}
int module = Integer.parseInt(moduleStr);
%>
<Zeta:ResourceBlock>
<div class=ultab>
<ul>
	<%
		if (module == 1) {
	%>
	<li class=selected><a href="/portal/showEntitySnapJsp.tv?module=1&entityId=<%= entityId %>">@EPON/SUPPLY.entitySnap@</a></li>
	<%
		} else {
	%>
	<li><a href="/portal/showEntitySnapJsp.tv?module=1&entityId=<%= entityId %>">@EPON/SUPPLY.entitySnap@</a></li>
	<%
		}
	%>
	<li class="secondTabLine"></li>
	<%
		if (module == 2) {
	%>
	<li class=selected><a href="/epon/showOltConfigJsp.tv?module=2&entityId=<%= entityId %>">@EPON/SUPPLY.configInfo@</a></li>
	<%
		} else {
	%>
	<li><a href="/epon/showOltConfigJsp.tv?module=2&entityId=<%= entityId %>">@EPON/SUPPLY.configInfo@</a></li>
	<%
		}
	%>	
<li class="secondTabLine"></li>
	<%
		if (entity.getModulePath() != null) {
		    if (module == 100) {
    %>
    <li class=selected><a class="withSub" href="<%=entity.getModulePath()%>/showOltFaceplate.tv?module=100&entityId=<%= entityId %>" sub="@EPON/EPON.entityView@$javascript:;|@EPON/EPON.vlanView@$javascript:;|@EPON/EPON.mirrorView@$javascript:;|@EPON/EPON.trunkView@$javascript:;"><span class="withSub">@EPON/SUPPLY.panel@</span></a></li>
    <%
            } else {
    %>
	
 <li><a class="withSub" href="<%=entity.getModulePath()%>/showOltFaceplate.tv?module=100&entityId=<%= entityId %>" sub="@EPON/EPON.entityView@$javascript:;|@EPON/EPON.vlanView@$javascript:;|@EPON/EPON.mirrorView@$javascript:;|@EPON/EPON.trunkView@$javascript:;"><span class="withSub">@EPON/SUPPLY.panel@</span></a></li>
    <%
            }
        } else {
            if (module == 100) {
    %>
	<li class=selected><a href="/epon/showOltFaceplate.tv?module=100&entityId=<%= entityId %>">@EPON/SUPPLY.panel@</a></li>
	<%
		    } else {
	%>
	<li><a href="/epon/showOltFaceplate.tv?module=100&entityId=<%= entityId %>">@EPON/SUPPLY.panel@</a></li>
	<%
		    }
        }
    %>
<li class="secondTabLine"></li>
	<%
		if (module == 200) {
	%>
	<li class=selected><a href="/epon/showOnuViewJsp.tv?module=200&entityId=<%= entityId %>">@EPON/SUPPLY.downDevice@</a></li>
	<%
		} else {
	%>
	<li><a href="/epon/showOnuViewJsp.tv?module=200&entityId=<%= entityId %>">@EPON/SUPPLY.downDevice@</a></li>
	<%
		}
	%>
	<%
		if (module == 6) {
	%>
	<li class=selected><a href="/epon/alert/showOltAlertJsp.tv?module=6&entityId=<%= entityId %>">@EPON/SUPPLY.deviceAlert@</a></li>
	<%
		} else {
	%>
	<li><a href="/epon/alert/showOltAlertJsp.tv?module=6&entityId=<%= entityId %>">@EPON/SUPPLY.deviceAlert@</a></li>
	<%
		}
	%>
	<%
		if (module == 8) {
	%>
	<li class=selected><a href="/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=8&entityId=<%= entityId %>">@EPON/SUPPLY.devicePerf@</a></li>
	<%
		} else {
	%>
	<li><a href="/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=8&entityId=<%= entityId %>">@EPON/SUPPLY.devicePerf@</a></li>
	<%
		}
	%>
	<!--
	<%
        if (module == 7) {
    %>
    <li class=selected><a href="/epon/dhcprelay/showDhcpRelayConfigList.tv?module=7&entityId=<%= entityId %>">DHCP Relay</a></li>
    <%
        } else {
    %>
    <li><a href="/epon/dhcprelay/showDhcpRelayConfigList.tv?module=7&entityId=<%= entityId %>">DHCP Relay</a></li>
    <%
        }
    %>  
    -->
</ul>
</div>
</Zeta:ResourceBlock>