<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.ems.facade.domain.EntityType" %>
<%@ page import="com.topvision.ems.facade.domain.Entity" %>
<%@ page import="com.topvision.ems.epon.onu.domain.OltOnuAttribute" %>

<%@ page import="net.sf.json.JSONObject" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:ResourceBlock>
<div class=ultab>
<%
	Object module = request.getParameter("module");
	Entity onu = (Entity)request.getAttribute("onu");
	long onuTypeId = onu.getTypeId();
	OltOnuAttribute onuAttribute = (OltOnuAttribute)request.getAttribute("onuAttribute");
	boolean isEponOnu = onuAttribute.getOnuEorG().equals("E");
	
	Integer catvCapability = onuAttribute.getCatvCapability();
	Integer wlanCapability = onuAttribute.getWlanCapability();
	
%>
<ul>
	<li><a href="/onu/showOnuPortal.tv?module=1&onuId=${onu.entityId}&entityId=${onu.parentId}">@EPON/SUPPLY.entitySnap@</a></li>
	<li class="secondTabLine"></li>
	<% if(!isEponOnu){%>
	<li><a href="/gpon/onu/showOnuHostList.tv?module=6&onuId=${onu.entityId}&entityId=${onu.parentId}">@GPON/GPON.hostList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
    <% if(isEponOnu){%>
	<li><a href="/onu/showOnuUniList.tv?module=2&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.uniList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
    <% if(!isEponOnu){%>
    <li><a href="/gpon/onu/showOnuUniList.tv?module=2&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.uniList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
    <% if(wlanCapability != null && wlanCapability == 1 ){ %>
    <li><a href="/onu/showOnuWanInfo.tv?module=3&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.WLANCONFIG@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/onu/showOnuWanStatus.tv?module=4&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.WANCONNECT@</a></li>
	<li class="secondTabLine"></li>
	<% }%>
	<% if(isEponOnu){%> 
	<% if(catvCapability != null && catvCapability == 1){%> 
	<li id="onuTabLink"><a href="/onu/catv/showOnuCatvConfig.tv?module=5&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.OPTICALMACHINE@</a></li>
    <li id="onuTabLinkSecondLine" class="secondTabLine"></li>
	<% }%>
	<% }%>
	<% if(!isEponOnu){%> 
    <li><a href="/gpon/onu/showGponOnuPotsList.tv?module=11&onuId=${onu.entityId}&entityId=${onu.parentId}">@GPON/GPON.potsList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
	<% if(isEponOnu){%>
    <li><a href="/epon/onucpe/showOnuCpeList.tv?module=6&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.cpeList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
    <% if(!isEponOnu){%>
    <li><a href="/gpon/onu/showOnuCpeList.tv?module=9&onuId=${onu.entityId}&entityId=${onu.parentId}">@ONU/ONU.cpeList@</a></li>
    <li class="secondTabLine"></li>
    <% }%>
	<li><a href="/onu/showOnuAlert.tv?module=7&onuId=${onu.entityId}&entityId=${onu.parentId}">@EPON/SUPPLY.deviceAlert@</a></li>
	<li class="secondTabLine"></li>
	<li><a href="/onu/showOnuPerf.tv?module=8&onuId=${onu.entityId}&entityId=${onu.parentId}">@EPON/SUPPLY.devicePerf@</a></li>
	<li class="secondTabLine"></li>
</ul>
<script type="text/javascript">
	var onuTypeId = <%= onuTypeId %>;
	(function(){
		var pageModule = <%= module %> || 1,
			selectedItem = $(String.format("A[href*='module={0}']",pageModule));
		selectedItem.parent().addClass("selected");

		$(".version-control").each(function(){
 			var $this = $(this);
 			var $controlId = $this.attr("controlId");
 			var $support = VersionControl.supportNode( $controlId );
			if( $support.disabled ){
				$this.hide();
				if( $this.next().hasClass("secondTabLine") ){
				    $this.next().hide();
				}
			}
 		});
	})()
</script>
</div>
</Zeta:ResourceBlock> 