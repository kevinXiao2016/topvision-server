<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%
	String oltSoftVersion = (String)request.getAttribute("oltSoftVersion");
	boolean isSupport = true;
	if(oltSoftVersion.indexOf("1.6.9.5") > -1){
	    isSupport = false;
	}else if(oltSoftVersion.indexOf("1.6.9.9") > -1){
	    isSupport = false;
	}
	String url = request.getRequestURI().toLowerCase();
%>
<Zeta:ResourceBlock>
<div class="ultab" style="position: absolute; width: 495; height: 15; top: 0px; left: 0px">
	<ul>
		<li style="line-height: 0.8em;" <%if(url.contains("faceplate")){%>class="selected"<%}%>>
			<a href="#" style="font-size: 10px" onclick="changeToOltConfig()">@EPON/EPON.entityView@</a>
		</li>
		<li style="line-height: 0.8em;" <%if(url.contains("vlan")){%>class="selected"<%}%>>
			<a href="#" style="font-size: 10px" onclick="changeToVlanConfig()">@EPON/EPON.vlanView@</a>
		</li>
		<li style="line-height: 0.8em;" <%if(url.contains("mirror")){%>class="selected"<%}%>>
			<a href="#" style="font-size: 10px" onclick="changeToMirrorConfig()">@EPON/EPON.mirrorView@</a>
		</li>
		<li style="line-height: 0.8em;" <%if(url.contains("trunk")){%>class="selected"<%}%>>
			<a href="#" style="font-size: 10px" onclick="changeToTrunkConfig()">@EPON/EPON.trunkView@</a>
		</li>
		<% if(isSupport){ %>
		<li style="line-height: 0.8em;" <%if(url.contains("ponprotect")){%>class="selected"<%}%>>
			<a href="#" style="font-size: 10px" onclick="changeToPonProtect()">@EPON/ponprotect.ponprotectView@</a>
		</li>
		<%} %>
	</ul>
</div>
</Zeta:ResourceBlock>