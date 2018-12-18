<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.net.InetAddress"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"></link>
<link rel="stylesheet" type="text/css"	href="../css/<%= cssStyleName %>/mytheme.css" />
<script language="javascript" type="text/javascript" src="../js/zeta-core.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
</head>
<body class=BLANK_WND style="margin: 15px;">
<%
//String host = request.getServerName();
String host = InetAddress.getLocalHost().getHostAddress();
if ("127.0.0.1".equals(host)) {
    host = "localhost";
} else {
    String temp = SystemConstants.getInstance().getStringParam("java.rmi.server.hostname", "");
    if (!"".equals(temp.trim())) {
        host = temp;
    }
}
%>
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td align=center height=24px style="white-space: nowrap">
				<table>
					<tr>
						<td><fmt:message key="label.src.ip" bundle="${resources}" /></td>
						<td><s:property value="linkEx.srcEntityName" /></td>
						<td width=20px>&nbsp;</td>
						<td><fmt:message key="linkRealFlow.Sourceportnumber" bundle="${resources}" /></td>
						<td><s:property value="linkEx.srcPortIndex" /></td>
						<td width=50px>&nbsp;</td>
						<td><fmt:message key="label.dest.ip" bundle="${resources}" /></td>
						<td><s:property value="linkEx.destIp" /></td>
						<td width=20px>&nbsp;</td>
						<td><fmt:message key="linkRealFlow.Destinationportnumber" bundle="${resources}" /></td>
						<td><s:property value="linkEx.destPortIndex" /></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align=center valign=top>
				<!--"CONVERTED_APPLET"--> <!-- HTML CONVERTER --> <object
					classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
					codebase="../conf/download/jre1.6.x_windows586.exe" WIDTH=100%
					HEIGHT=100%>
					<PARAM NAME=CODE
						VALUE="com.topvision.ems.applet.linkflow.LinkFlowApplet">
					<PARAM NAME=CODEBASE VALUE="../webstart">
					<PARAM NAME=ARCHIVE
						VALUE="conf.jar,logback-core-0.9.17.jar,logback-classic-0.9.17.jar,slf4j-api-1.5.9-RC0.jar,jfreechart-1.0.10.jar,jcommon-1.0.13.jar,zeta-core-1.0.1.jar,zeta-layout-1.0.1.jar,ems-applet.jar">
					<param name="type" value="application/x-java-applet;version=1.6">
					<param name="scriptable" value="false">
					<PARAM NAME="theme" VALUE="<%= cssStyleName %>">
					<PARAM NAME="linkId" VALUE="<%= request.getParameter("linkId") %>">
					<PARAM NAME="host" VALUE="<%= host %>">
					<PARAM NAME="port"
						VALUE="<%= SystemConstants.getInstance().getIntParam("rmi.port", 12121) %>">
					<PARAM NAME="serviceName"
						VALUE="<%= SystemConstants.getInstance().getStringParam("rmi.service", "ems") %>">

					<comment> <embed
						type="application/x-java-applet;version=1.6"
						\
            CODE="com.topvision.ems.applet.linkflow.LinkFlowApplet"
						\
            JAVA_CODEBASE="../webstart"
						\
            ARCHIVE="conf.jar,logback-core-0.9.17.jar,logback-classic-0.9.17.jar,slf4j-api-1.5.9-RC0.jar,jfreechart-1.0.10.jar,jcommon-1.0.13.jar,zeta-core-1.0.1.jar,zeta-layout-1.0.1.jar,ems-applet.jar"
						\
            WIDTH=100% \
            HEIGHT=100%
						\
            theme="<%= cssStyleName %>"
						\
            linkId="<%= request.getParameter("linkId") %>"
						\
            host="<%= host %>"
						\
            port="<%= SystemConstants.getInstance().getIntParam("rmi.port", 12121) %>"
						\
            serviceName="<%= SystemConstants.getInstance().getStringParam("rmi.service", "ems") %>"
						scriptable=false
						pluginspage="../conf/download/jre1.6.x_windows586.exe">
						<noembed> </noembed>
					</embed> </comment>
				</object> </td>
		</tr>
	</table>
</body>
</html>
