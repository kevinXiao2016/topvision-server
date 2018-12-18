<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:v="urn chemas-microsoft-com:vml" xml:lang="en" lang="en">
<head>
<style>
v\:* {
	behavior: url(#default#VML);
}
</style>
<title></title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script>
</script>
</head>
<body class=CONTENT_WND>
	<div></div>
	<div style="width: 100%; height: 100%; overflow: auto;">

		<% int x = 30, y = 10; %>
		<s:iterator value="ports">
			<div
				style="z-index:5;width:100px;position:absolute;left:<%= x %>;top:<%= y %>;"
				align=center>
				<img src="../images/network/port/ethernet_port48.gif"><br> <span><s:property
						value="ifDescr" />
				</span>
			</div>
			<% y = y + 60; %>
		</s:iterator>

		<% x = 500; y = 10; %>
		<s:iterator value="destPorts">
			<div
				style="z-index:5;width:100px;position:absolute;left:<%= x %>;top:<%= y %>;"
				align=center>
				<img src="../images/network/port/ethernet_port48.gif"><br> <span><s:property
						value="ifDescr" />
				</span>
			</div>
			<% y = y + 60; %>
		</s:iterator>

		<v:line from="80px,155px" to="535px,90px" strokeColor=black
			strokeWeight="1px">
			<span style="position: relative; left: 240; top: 10;">100M</span>
		</v:line>

		<v:line from="80px,95px" to="535px,35px" strokeColor=black
			strokeWeight="1px">
			<span style="position: relative; left: 240; top: 10;">10M</span>
		</v:line>
	</div>
</body>
</html>
