<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:v="urn chemas-microsoft-com:vml" xml:lang="zh-CN" lang="zh-CN">
<head>
<style>
v\:* {
	behavior: url(#default#VML);
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:property value="topoFolder.name" />
</title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/cluetip.css" />
<link rel="stylesheet" type="text/css" href="../css/zeta-graph.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<style type="text/css">
.entityIcon {
	background-image: url(../images/network/entity.gif) !important;
	valign: middle;
}
</style>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.core.js"></script>
<script type="text/javascript" src="../js/jquery/ui.draggable.js"></script>
<script type="text/javascript" src="../js/jquery/ui.selectable.js"></script>
<script type="text/javascript" src="../js/jquery/ux/cluetip.js"></script>
<script type="text/javascript" src="../js/jquery/ux/hoverIntent.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-drawing.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../network/topoMap.js"></script>

<script type="text/javascript">
// for tab changed start
function tabActivate() {
	showEntityCount();
	dispatcherCallback();
	startTimer();
}
function tabDeactivate() {
	doOnunload();
}
function tabRemoved() {
	doOnunload();
}
function tabShown() {
	showEntityCount();
}
function selectAll() {
	onSelectAllClick();
}
// for tab changed end
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var superiorFolderId = <s:property value="topoFolder.superiorId"/>;
var superiorFolderName = '<s:property value="topoFolder.superiorName"/>';
var topoFolderId = '<s:property value="topoFolder.folderId"/>';
var topoFolderName = '<s:property value="topoFolder.name"/>';
var folderPath = '<s:property value="topoFolder.path"/>/';
var entityLabel = '<s:property value="topoFolder.entityLabel"/>';
var linkLabel = '<s:property value="topoFolder.linkLabel"/>';
var dispatcherInterval = <s:property value="topoFolder.refreshInterval"/>;
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function () {
	shapeLang = {dbclickInputMsg: I18N.topoMap.Double-click};
	initZoomParam(<s:property value="topoFolder.zoom"/>, <s:property value="topoFolder.fontSize"/>, <s:property value="topoFolder.iconSize"/>);

	linkShadow = <s:property value="topoFolder.linkShadow"/>;
	linkStyle.linkShadowColor = '<s:property value="topoFolder.linkShadowColor"/>';
	linkStyle.width = '<s:property value="topoFolder.linkWidth"/>';
	linkStyle.color = '<s:property value="topoFolder.linkColor"/>';
	if (cssStyleName == 'apple' || cssStyleName == 'gray' || cssStyleName == 'office2003') {
		linkStyle.selectedColor = '#316AC5';
		//squareHelper.border = 'solid 1px #316AC5';
	} else if (cssStyleName == 'vista') {
		linkStyle.selectedColor = '#99DFFD';
	} else if (cssStyleName == 'black') {
		linkStyle.selectedColor = '#808080';
	} else {
		linkStyle.selectedColor = '<s:property value="topoFolder.linkSelectedColor"/>';
	}
	SHAPE_STYLE.fillColor = '<s:property value="topoFolder.nodeFillColor"/>';
	SHAPE_STYLE.selectedColor = linkStyle.selectedColor;

	displayName = <s:property value="topoFolder.displayName"/>;
	if (!displayName) {
		zoom.textWidth = parseInt(100 * zoom.size);
		zoom.width = zoom.textWidth + 10;
	}
	renamable = displayName ? true : false;

	displayCluetip = <s:property value="topoFolder.displayCluetip"/>;
	displayLinkLabel = <s:property value="topoFolder.displayLinkLabel"/>;
	displayAlertIcon = <s:property value="topoFolder.displayAlertIcon"/>;
	displayNoSnmpEntity = <s:property value="topoFolder.displayNoSnmp"/>;
	displayRouter = <s:property value="topoFolder.displayRouter"/>;
	displaySwitch = <s:property value="topoFolder.displaySwitch"/>;
	displayL3switch = <s:property value="topoFolder.displayL3switch"/>;
	displayServer = <s:property value="topoFolder.displayServer"/>;
	displayDesktop = <s:property value="topoFolder.displayDesktop"/>;
	displayOthers = <s:property value="topoFolder.displayOthers"/>;
	
	entityForOrgin = <s:property value="topoFolder.entityForOrgin"/>;
	depthForOrgin = <s:property value="topoFolder.depthForOrgin"/>;

	backgroundPosition = <s:property value="topoFolder.backgroundPosition"/>;
	backgroundFlag = <s:property value="topoFolder.backgroundFlag"/>;
	backgroundImg = 'url(<s:property value="topoFolder.backgroundImg"/>)';
	backgroundColor = '<s:property value="topoFolder.backgroundColor"/>';
	displayGrid = <s:property value="topoFolder.displayGrid"/>;
	backgroundGrid = 'url(../css/grid_bg.gif)';
	
	editTopoPower = <%= uc.hasPower("topoEdit") %>;
	remoteMode = <%= request.getParameter("remoteMode") %>;
	if (remoteMode != null) {
		remoteMode = true;
		editTopoPower = false;
	}
	if (!editTopoPower) {
		cluetipShowDelaying = 500;
	}

	if (topoFolderId == '') {
		return;
	}
			
	// defferent from topo map for subnetmap
	isSubnetMap = (<s:property value="topoFolder.type"/> == 5);
	initialize();
	if (isSubnetMap) {
		loadSubnetData(<s:property value="entityId"/>, false);
	} else {
		loadMapData(<s:property value="entityId"/>, false);
	}
	startTimer();
});
</script>
</head>
<body class="CONTENT_WND" onunload="doOnunload()"
	onresize="doOnResize()">

	<div id="toolbar" style="left: 0px; top: 0px;"></div>
	<div id="zetaGraph" onkeydown="return doOnKeyDown();"
		onmousewheel="doMouseWheel();"
		style="width:100%;height:100%;overflow:auto;<s:if test="topoFolder.displayGrid">background:url(../css/grid_bg.gif) repeat;</s:if><s:elseif test="topoFolder.backgroundFlag">background: <s:property value="topoFolder.backgroundColor"/> url(<s:property value="topoFolder.backgroundImg"/>) <s:if test="topoFolder.backgroundPosition==0">0 0 no-repeat scroll</s:if><s:elseif test="topoFolder.backgroundPosition==1">center 0 no-repeat</s:elseif><s:else>0 0 repeat</s:else>;</s:elseif><s:else>background-color:<s:property value="topoFolder.backgroundColor"/>;</s:else>"></div>

	<div id="microContainer-div" class=microContainer>
		<table height=100% cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<div id="microGraph-div" class=microGraph>
						<div id="microSquare-div" class="microSquare">
							<div></div>
						</div>
					</div></td>
				<td valign=top style="padding-left: 5px">
					<table width=17 cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td><a href="javascript: onZoomFitClick5();"
								title='<fmt:message key="topoMap.Zoom100" bundle="${resources}" />'> <img
									src="../images/zgraph/fullpage.gif" alt='<fmt:message key="topoMap.Zoom100" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>
						<tr>
							<td><a href="javascript: onZoomInClick();" title='<fmt:message key="GRAPH.zoomOut" bundle="${resources}" />'> <img
									src="../images/zgraph/panplus.gif" alt='<fmt:message key="GRAPH.zoomOut" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick10();"
								title='<fmt:message key="topoMap.Zoom400" bundle="${resources}" />'> <img id="zoomImg10"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom400" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick9();"
								title='<fmt:message key="topoMap.Zoom300" bundle="${resources}" />'> <img id="zoomImg9"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom300" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick8();"
								title='<fmt:message key="topoMap.Zoom200" bundle="${resources}" />'> <img id="zoomImg8"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom200" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick7()"
								title='<fmt:message key="topoMap.Zoom150" bundle="${resources}" />'> <img id="zoomImg7"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom150" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick6();"
								title='<fmt:message key="topoMap.Zoom125" bundle="${resources}" />'> <img id="zoomImg6"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom125" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick5();"
								title='<fmt:message key="topoMap.Zoom100" bundle="${resources}" />'> <img id="zoomImg5"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom100" bundle="${resources}" />'
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick4();"
								title='<fmt:message key="topoMap.Zoom75" bundle="${resources}" />'> <img id="zoomImg4"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom75" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick3();"
								title='<fmt:message key="topoMap.Zoom50" bundle="${resources}" />'> <img id="zoomImg3"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom50" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick2();"
								title='<fmt:message key="topoMap.Zoom25" bundle="${resources}" />'> <img id="zoomImg2"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom25" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomFitClick1();"
								title='<fmt:message key="topoMap.Zoom10" bundle="${resources}" />'> <img id="zoomImg1"
									src="../images/zgraph/tick-off.gif" alt='<fmt:message key="topoMap.Zoom10" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript: onZoomOutClick();" title='<fmt:message key="GRAPH.zoomIn" bundle="${resources}" />'>
									<img src="../images/zgraph/panminus.gif" alt='<fmt:message key="GRAPH.zoomIn" bundle="${resources}" />' border="0">
							</a>
							</td>
						</tr>
					</table></td>
			</tr>
		</table>
	</div>
	<div style="display: none">
		<div id="drawings-div" class=DRAWING-CONTAINER align=center
			style="width: 100%; height: 100%;">
			<table cellspacing=0 cellpadding=0 width=100% height=100%>
				<tr>
					<td colspan=3 height=1></td>
				</tr>
				<tr>
					<td id="drawing0" class=DRAWING-TOOL-SELECTED
						onmouseover="doDrawingToolOver(this, 0)"
						onmouseout="doDrawingToolOut(this, 0)"
						onmousedown="doDrawingToolPressed(this, 0)"
						onmouseup="doDrawingToolOver(this, 0)" onclick="setDrawingType(0)"
						align=center title='<fmt:message key="topoMap.Defaultpointer" bundle="${resources}" />'><img
						src="../images/zgraph/pointer.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing1" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 1)"
						onmouseout="doDrawingToolOut(this, 1)"
						onmousedown="doDrawingToolPressed(this, 1)"
						onmouseup="doDrawingToolOver(this, 1)" onclick="setDrawingType(1)"
						align=center title='<fmt:message key="topoMap.Rectangularmarqueetool" bundle="${resources}" />'><img
						src="../images/zgraph/square.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>

				<tr>
					<td id="drawing2" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 2)"
						onmouseout="doDrawingToolOut(this, 2)"
						onmousedown="doDrawingToolPressed(this, 2)"
						onmouseup="doDrawingToolOver(this, 2)" onclick="setDrawingType(2)"
						align=center title='<fmt:message key="topoMap.Handshapemovetool" bundle="${resources}" />'><img
						src="../images/zgraph/snap.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing3" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 3)"
						onmouseout="doDrawingToolOut(this, 3)"
						onmousedown="doDrawingToolPressed(this, 3)"
						onmouseup="doDrawingToolOver(this, 3)" onclick="setAllSelected();"
						align=center title='<fmt:message key="topoMap.Selectall" bundle="${resources}" />'><img
						src="../images/zgraph/selectall.gif" border=0 align=absmiddle>
					</td>
				</tr>

				<tr>
					<td colspan=3 height=3></td>
				</tr>

				<tr>
					<td id="drawing7" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 7)"
						onmouseout="doDrawingToolOut(this, 7)"
						onmousedown="doDrawingToolPressed(this, 7)"
						onmouseup="doDrawingToolOver(this, 7)" onclick="onZoomInClick()"
						align=center title='<fmt:message key="GRAPH.zoomOut" bundle="${resources}" />'><img
						src="../images/zgraph/zoomin.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing8" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 8)"
						onmouseout="doDrawingToolOut(this, 8)"
						onmousedown="doDrawingToolPressed(this, 8)"
						onmouseup="doDrawingToolOver(this, 8)" onclick="onZoomOutClick();"
						align=center title='<fmt:message key="GRAPH.zoomIn" bundle="${resources}" />'><img
						src="../images/zgraph/zoomout.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing5" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 5)"
						onmouseout="doDrawingToolOut(this, 5)"
						onmousedown="doDrawingToolPressed(this, 5)"
						onmouseup="doDrawingToolOver(this, 5)" onclick="onZoomFitClick()"
						align=center title='<fmt:message key="GRAPH.zoomFit" bundle="${resources}" />'><img
						src="../images/zgraph/zoomfit.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing6" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 6)"
						onmouseout="doDrawingToolOut(this, 6)"
						onmousedown="doDrawingToolPressed(this, 6)"
						onmouseup="doDrawingToolOver(this, 6)"
						onclick="onZoomCustomClick();" align=center title='<fmt:message key="topoMap.Customscale" bundle="${resources}" />'><img
						src="../images/zgraph/zoom.gif" border=0 align=absmiddle>
					</td>
				</tr>

				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing21" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 21)"
						onmouseout="doDrawingToolOut(this, 21)"
						onmousedown="doDrawingToolPressed(this, 21)"
						onmouseup="doDrawingToolOver(this, 21)"
						onclick="selectFillColor()" align=center title='<fmt:message key="topoMap.Fillcolor" bundle="${resources}" />'><span
						id="fillColorContainer"
						style="padding: 0px; width: 16px; height: 10px; border: solid 1px gray; cursor: default; background: white; font-size: 9px;">&nbsp;</span>
					</td>
					<td width=2></td>
					<td id="drawing24" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 24)"
						onmouseout="doDrawingToolOut(this, 24)"
						onmouseup="doDrawingToolOver(this, 24)"
						onmousedown="doDrawingToolPressed(this, 24)"
						onclick="setDrawingType(24)" align=center title='<fmt:message key="topoMap.text" bundle="${resources}" />'><img
						src="../images/zgraph/text.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing19" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 19)"
						onmouseout="doDrawingToolOut(this, 19)"
						onmouseup="doDrawingToolOver(this, 19)"
						onmousedown="doDrawingToolPressed(this, 19)"
						onclick="setDrawingType(19)" align=center title='<fmt:message key="topoMap.label" bundle="${resources}" />'><img
						src="../images/zgraph/label.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing10" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 10)"
						onmouseout="doDrawingToolOut(this, 10)"
						onmouseup="doDrawingToolOver(this, 10)"
						onmousedown="doDrawingToolPressed(this, 10)"
						onclick="setDrawingType(10)" align=center title='<fmt:message key="GRAPH.straightConnector" bundle="${resources}" />'><img
						src="../images/zgraph/line.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing12" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 12)"
						onmouseout="doDrawingToolOut(this, 12)"
						onmousedown="doDrawingToolPressed(this, 12)"
						onmouseup="doDrawingToolOver(this, 0)"
						onclick="setDrawingType(12)" align=center title='<fmt:message key="topoMap.Singlearrowline" bundle="${resources}" />'><img
						src="../images/zgraph/arrow.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing13" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 13)"
						onmouseout="doDrawingToolOut(this, 13)"
						onmousedown="doDrawingToolPressed(this, 13)"
						onmouseup="doDrawingToolOver(this, 0)"
						onclick="setDrawingType(13)" align=center title='<fmt:message key="topoMap.Doublearrowlines" bundle="${resources}" />'><img
						src="../images/zgraph/darrow.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing16" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 16)"
						onmouseout="doDrawingToolOut(this, 16)"
						onmousedown="doDrawingToolPressed(this, 16)"
						onmouseup="doDrawingToolOver(this, 16)"
						onclick="setDrawingType(16)" align=center title='<fmt:message key="topoMap.rectangular" bundle="${resources}" />'><img
						src="../images/zgraph/rect.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing17" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 17)"
						onmouseout="doDrawingToolOut(this, 17)"
						onmousedown="doDrawingToolPressed(this, 17)"
						onmouseup="doDrawingToolOver(this, 17)"
						onclick="setDrawingType(17)" align=center title='<fmt:message key="topoMap.Roundedrectangle" bundle="${resources}" />'><img
						src="../images/zgraph/round.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>

				<tr>
					<td id="drawing18" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 18)"
						onmouseout="doDrawingToolOut(this, 18)"
						onmousedown="doDrawingToolPressed(this, 18)"
						onmouseup="doDrawingToolOver(this, 18)"
						onclick="setDrawingType(18)" align=center title='<fmt:message key="topoMap.elliptic" bundle="${resources}" />'><img
						src="../images/zgraph/eclipse.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing21" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 21)"
						onmouseout="doDrawingToolOut(this, 21)"
						onmousedown="doDrawingToolPressed(this, 21)"
						onmouseup="doDrawingToolOver(this, 21)" onclick="insertPicture()"
						align=center title='<fmt:message key="topoMap.Insertpictures" bundle="${resources}" />'><img
						src="../images/zgraph/picture.gif" border=0 align=absmiddle>
					</td>
				</tr>
				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing31" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 31)"
						onmouseout="doDrawingToolOut(this, 31)"
						onmouseup="doDrawingToolOver(this, 31)"
						onmousedown="doDrawingToolPressed(this, 31)"
						onclick="setDrawingType(31)" align=center title='<fmt:message key="NETWORK.cloudy" bundle="${resources}" />'><img
						src="../images/zgraph/cloudy16.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td id="drawing32" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 32)"
						onmouseout="doDrawingToolOut(this, 32)"
						onmousedown="doDrawingToolPressed(this, 32)"
						onmouseup="doDrawingToolOver(this, 32)"
						onclick="setDrawingType(32)" align=center title='<fmt:message key="NETWORK.subnet" bundle="${resources}" />'><img
						src="../images/zgraph/subnet16.gif" border=0 align=absmiddle>
					</td>
				</tr>

				<tr>
					<td colspan=3 height=3></td>
				</tr>
				<tr>
					<td id="drawing33" class=DRAWING-TOOL
						onmouseover="doDrawingToolOver(this, 33)"
						onmouseout="doDrawingToolOut(this, 33)"
						onmouseup="doDrawingToolOver(this, 33)"
						onmousedown="doDrawingToolPressed(this, 33)"
						onclick="setDrawingType(33)" align=center title='<fmt:message key="folder.type8" bundle="${resources}" />'><img
						src="../images/zgraph/href16.gif" border=0 align=absmiddle>
					</td>
					<td width=2></td>
					<td></td>
				</tr>
				<tr>
					<td colspan=3 valign=top height=100%></td>
				</tr>
			</table>
		</div>
		<div id="template-div"></div>
		<textarea id="renameBox" class=renameBox></textarea>
	</div>

	<form id="graphForm" name="graphForm" method="post" target="_blank">
		<input type=hidden name="folderId"
			value="<s:property value="topoFolder.folderId"/>"> <input
			type="hidden" id="mapWidth" name="mapWidth" value="1024"> <input
			type="hidden" id="mapHeight" name="mapHeight" value="768"> <input
			type="hidden" id="zoomSize" name="zoomSize" value="1"> <input
			type="hidden" id="displayName" name="displayName" value="true">
	</form>
	
	
	
	
</body>
</html>
