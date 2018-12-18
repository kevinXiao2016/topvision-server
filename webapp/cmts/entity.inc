<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<jsp:useBean id="cmcId" scope="request" class="Long" />
<%
String localAddr = request.getLocalAddr();
String moduleStr = request.getParameter("module");
if (moduleStr == null) {
	moduleStr = "1";
}
String productType = "40000";
int module = Integer.parseInt(moduleStr);
%>
<Zeta:ResourceBlock>
<div class=ultab>
<ul>
		<%
			if (module == 1) {
		%>
		<li class=selected><a href="/cmts/showCmtsPortal.tv?module=1&cmcId=<%= cmcId %>&productType=<%= productType %>">@network/snapTitle@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/showCmtsPortal.tv?module=1&cmcId=<%= cmcId %>&productType=<%= productType %>">@network/snapTitle@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 2) {
		%>
		<li class=selected><a href="/cmts/showCmtsConfig.tv?module=2&cmcId=<%= cmcId %>&productType=<%= productType %>">@resources/COMMON.configDetail@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/showCmtsConfig.tv?module=2&cmcId=<%= cmcId %>&productType=<%= productType %>">@resources/COMMON.configDetail@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 3) {
		%>
		<li class=selected><a href="/cmts/channel/showUpChannelList.tv?module=3&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.upStreamChannel@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/channel/showUpChannelList.tv?module=3&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.upStreamChannel@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 4) {
		%>
		<li class=selected><a href="/cmts/channel/showDownChannelList.tv?module=4&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.downStreamChannel@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/channel/showDownChannelList.tv?module=4&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.downStreamChannel@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 5) {
		%>
		<li class=selected><a href="/cmts/channel/showUpLinkPortList.tv?module=5&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/cmts.upLinkPortList@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/channel/showUpLinkPortList.tv?module=5&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/cmts.upLinkPortList@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 6) {
		%>
		<li class=selected><a href="/cmts/cm/showContactedCmList.tv?module=6&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CM.cmList@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/cm/showContactedCmList.tv?module=6&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CM.cmList@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 7) {
		%>
		<li class=selected><a href="/cmts/alert/showCmtsAlert.tv?module=7&cmcId=<%= cmcId %>&productType=<%= productType %>">@network/NETWORK.entityAlarm@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/alert/showCmtsAlert.tv?module=7&cmcId=<%= cmcId %>&productType=<%= productType %>">@network/NETWORK.entityAlarm@</a></li>
		<%
			}
		%>
		<li class="secondTabLine"></li>
		<%
			if (module == 8) {
		%>
		<li class=selected><a href="/cmts/showCmtsCurPerf.tv?module=8&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.perfDisplay@</a></li>
		<%
			} else {
		%>
		<li><a href="/cmts/showCmtsCurPerf.tv?module=8&cmcId=<%= cmcId %>&productType=<%= productType %>">@cmc/CCMTS.perfDisplay@</a></li>
		<%
			}
		%>
		
</ul>
</div>
               
</Zeta:ResourceBlock>
