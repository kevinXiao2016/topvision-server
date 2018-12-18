<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@include file="../include/nocache.inc"%>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>
<title><fmt:message bundle="${res}" key="topo.grap.node.attr.title"/></title>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"	href="../css/<%=cssStyleName%>/xtheme.css" />
<link rel="stylesheet" type="text/css"	href="../css/<%=cssStyleName%>/mytheme.css" />
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var nodeId = <s:property value="nodeId"/>;
var folderId = <%=request.getParameter("folderId")%>;
var vmlId = '<%=request.getParameter("id")%>';
var linkWidth = '<s:property value="mapNode.strokeWeight"/>';
var fontSize = <s:property value="mapNode.fontSize"/>;
</script>
<script type="text/javascript" src="../network/mapNodeProperty.js"></script>
</head>
<body class="sideMapBg" onload="doOnload()">
	<form id="mapNodeForm" name="mapNodeForm" action="#">
	<div class="edge10">
		
			<input type=hidden name="mapNode.nodeId" value="<s:property value="nodeId"/>" />
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<s:if test="mapNode.type < 14">
					<tr>
						<td width=85 class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.startPoint"/>:</td>
						<td colspan=2><s:property value="mapNode.x" />, <s:property
								value="mapNode.y" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.endPoint"/>:</td>
						<td colspan=2><s:property value="mapNode.width" />, <s:property
								value="mapNode.height" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.edgeLineStyle"/>:</td>
						<td colspan=2><input id="solidLine"
							onclick="solidLineStyle();" type=radio name="lineStyle"
							<s:if test="!mapNode.dashed">checked</s:if>><label
							for="solidLine"><fmt:message bundle="${res}" key="topo.grap.node.attr.solid"/></label> <input id="dashedLine"
							onclick="dashedLineStyle();" type=radio name="lineStyle"
							<s:if test="mapNode.dashed">checked</s:if>><label
							for="dashedLine"><fmt:message bundle="${res}" key="topo.grap.node.attr.dotted"/></label>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.lineWidth"/>:</td>
						<td colspan=2><select id="linkWidthBox" style="width: 180px"
							onchange="onLinkWidthChanged(this);">
								<option value="0.75">1px</option>
								<option value="1.0">2px</option>
								<option value="1.2">3px</option>
								<option value="2.0">4px</option>
								<option value="3.0">5px</option>
								<option value="4.0">6px</option>
								<option value="5.0">7px</option>
								<option value="6.0">8px</option>
								<option value="7.0">9px</option>
								<option value="8.0">10px</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.lineColor"/>:</td>
						<td colspan=2><span id="borderArea" class="color-area"
							onclick="setBorderColor()"
							style="width:100px; background:<s:property value="mapNode.strokeColor"/>"></span>
						<button class=BUTTON75
								onMouseOver="this.className='BUTTON_OVER75'"
								onMouseOut="this.className='BUTTON75'"
								onMouseDown="this.className='BUTTON_PRESSED75'"
								onclick="setBorderColor()"><fmt:message bundle="${res}" key="COMMON.select"/>...</button>
						</td>
					</tr>
				</s:if>
				<s:elseif test="mapNode.type==24">
					<tr>
						<td width=85 class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.x"/>:</td>
						<td ><s:property value="mapNode.x" />px</td>
					</tr>
					<tr>
						<td><fmt:message bundle="${res}" key="topo.grap.node.attr.y"/>:</td>
						<td ><s:property value="mapNode.y" />px</td>
					</tr>
				</s:elseif>
				<s:else>
					<tr>
						<td width=85 class="rightBlueTxt"><fmt:message bundle="${res}" key="COMMON.name"/>:</td>
						<td ><s:property value="mapNode.name"/></td>
					</tr>
					<tr>
						<td width=85 class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.x"/>:</td>
						<td ><s:property value="mapNode.x" />px</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.y"/>:</td>
						<td ><s:property value="mapNode.y" />px</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.width"/>:</td>
						<td ><s:property value="mapNode.width" />px</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.height"/>:</td>
						<td ><s:property value="mapNode.height" />px</td>
					</tr>
				</s:else>
				<s:if test="mapNode.type==19 || mapNode.type==24">
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.fontColor"/>:</td>
						<td >
							<div id="colorArea" class="color-area" onclick="setFontColor()"
								style="float:left;width:75px; background:<s:property value="mapNode.textColor"/>"></div>
							<div>
								<button class=BUTTON75 style="float: right;"
									onMouseOver="this.className='BUTTON_OVER75'"
									onMouseOut="this.className='BUTTON75'"
									onMouseDown="this.className='BUTTON_PRESSED75'"
									onclick="setFontColor()"><fmt:message bundle="${res}" key="COMMON.select"/>...</button>
							</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.fontSize"/>:</td>
						<td ><select id="fontSizeBox" style="width: 75px"
							onchange="onFontSizeChanged(this);">
								<option value="9">9pt</option>
								<option value="10">10pt</option>
								<option value="12">12pt</option>
								<option value="15">15pt</option>
								<option value="16">16pt</option>
								<option value="20">20pt</option>
								<option value="22">22pt</option>
								<option value="25">25pt</option>
								<option value="28">28pt</option>
								<option value="30">30pt</option>
								<option value="32">32pt</option>
								<option value="36">36pt</option>
								<option value="40">40pt</option>
								<option value="50">50pt</option>
						</select>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.fontStyle"/>:</td>
						<td ><input id="bold" type=checkbox /><label
							for="bold"><fmt:message bundle="${res}" key="topo.grap.node.attr.blod"/></label>&nbsp;&nbsp;&nbsp;&nbsp;<input id="italic"
							type=checkbox /><label for="italic"><fmt:message bundle="${res}" key="topo.grap.node.attr.tilt"/></label>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.text"/>:</td>
						<Td ><textarea id="text" class=iptxa
								name="mapNode.text" rows=3 style="overflow: auto; width: 180px">
									<s:property value="mapNode.text" />
								</textarea></td>
					</tr>
				</s:if>
				<tr>
					<td class="rightBlueTxt"><fmt:message bundle="${res}" key="topo.grap.node.attr.href"/>:</td>
					<td>
                        <textarea id="urls" class="iptxa normalInput" name="mapNode.url" rows=3 style="overflow: auto; width: 240px;height:80px;" value=""><s:property value="mapNode.url" /></textarea>
                    </td>
				</tr>
				<tr>
					<td></td>
					<td>
						<a href="javascript:;" class="normalBtn" onclick="onUpdateClick()"><span><i class="miniIcoEdit"></i><fmt:message bundle="${res}" key="COMMON.modify"/></span></a>
					</td>
				</tr>
			</table>
		
	</div>
	
	</form>
</body>
</html>
