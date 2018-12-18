<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zeta-core.js"></script>
<script>
</script>
</head>
<body class=BLANK_WND style="margin: 10px;">
	<table width=100% height=100% cellspacing=0 cellpadding=0>
		<tr>
			<td valign=top><%@ include file="entity.inc"%></td>
		</tr>

		<tr>
			<td>
				<table>
					<s:iterator value="processes">
						<tr>
							<td><s:property value="pid" /></td>
							<td><s:property value="name" /></td>
							<td><s:property value="status" /></td>
							<td><s:property value="cpuUsage" /></td>
							<td><s:property value="memUsage" /></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>

		<tr>
			<td></td>
		</tr>
	</table>
</body>
</html>
