<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:v="urn chemas-microsoft-com:vml" xml:lang="zh-CN" lang="zh-CN">
<head>
<title><s:property value="topoFolder.name" />
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resNetwork"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="res"/>


<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/zeta-graph.css" />
<link rel="stylesheet" type="text/css" href="../css/cluetip.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css" />
<style type="text/css">
v\:* {
	behavior: url(#default#VML);
}

.bmenu_zoom {
	background-image: url(../images/zgraph/zoomin.png) !important;
}

.connector_straight {
	background-image: url(../images/zgraph/straight.gif) !important;
}

.connector_horizonal {
	background-image: url(../images/zgraph/horizonal.gif) !important;
}

.bmenu_block {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 0;
}

.bmenu_classic {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -16px;
}

.bmenu_open {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -48px;
}

.bmenu_oval {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -64px;
}

.bmenu_block1 {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -80px;
}

.bmenu_classic1 {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -96px;
}

.bmenu_open1 {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -128px;
}

.bmenu_oval1 {
	background-image: url(../images/zgraph/connector.gif) !important;
	background-position: 0 -144px;
}

.bmenu_left {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 0;
}

.bmenu_center {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 -16px;
}

.bmenu_right {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 -32px;
}

.bmenu_top {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 -48px;
}

.bmenu_middle {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 -64px;
}

.bmenu_bottom {
	background-image: url(../images/zgraph/align.gif) !important;
	background-position: 0 -80px;
}

.bmenu_checked {
	background-image: url(../images/checked.gif); !important;
	background-position: 0 0;
}

.first_menu {
    font-weight : bold;	
}
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
</style>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>

<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.graph.base.js"></script>

<script type="text/javascript" src="../js/jquery/ux/cluetip.js"></script>
		<!-- hover效果的jquery插件 -->
<script type="text/javascript" src="../js/jquery/ux/hoverIntent.js"></script>

<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
		<!-- zeta基本图像和图路径库 -->
<script type="text/javascript" src="../js/zetaframework/zeta-graph.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-shape.js"></script>
<!-- 
<script type="text/javascript" src="../js/zetaframework/zeta-graph-all.js"></script>
 -->
<script type="text/javascript" src="../js/zetaframework/zeta-lang-zh_CN.js"></script>

		<!-- EMS拓扑图库文件 -->
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
</script>
<script type="text/javascript" src="../network/topoGraph.js"></script>

<script type="text/javascript">
// for tab changed start, tab interface
function tabActivate() {
	tabShown();
	dispatcherCallback();
	startTimer();
}
function tabDeactivate() {
	stopTimer();
}
function tabRemoved() {
	stopTimer();
}
function tabShown() {
	totalFlag = -1;
	showStatusBarInfo();
}
function selectAll() {
	onSelectAllClick();
}
function doRefresh() {
	onRefreshClick();
}
// for tab changed end
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var superiorFolderId = <s:property value="topoFolder.superiorId"/>;
var superiorFolderName = '<s:property value="topoFolder.superiorName"/>';
var topoFolderId = '<s:property value="topoFolder.folderId"/>';
var topoFolderName = '<s:property value="topoFolder.name"/>';
var folderPath = '<s:property value="topoFolder.path"/>';
var entityLabel = '<s:property value="topoFolder.entityLabel"/>';
var linkLabel = '<s:property value="topoFolder.linkLabel"/>';
var folderWidth = <s:property value="topoFolder.width"/>;
var folderHeight = <s:property value="topoFolder.height"/>;

<%--var relaGoogle = <%=uc.hasPower("relaGoogle")%>;
var apConfigure = <%=uc.hasPower("apConfigure")%>;
var apRestart = <%=uc.hasPower("apRestart")%>;
var editRegionProperty = <%=uc.hasPower("editRegionProperty")%>; 
var configManagement = <%=uc.hasPower("configManagement")%>;
var newEquipment = <%=uc.hasPower("newEquipment")%>;--%>

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function () {
	dispatcherInterval = <s:property value="topoFolder.refreshInterval"/>;
	isSubnetMap = <s:property value="topoFolder.type"/> == 5;
	openMode = <%= request.getParameter("open") %>;
	editTopoPower = <%= uc.hasPower("topoEdit") %>;
	remoteMode = <%= request.getParameter("remoteMode") %>;
	editDevice = <%= request.getParameter("editDevice") %>;
	if (remoteMode) {
		editTopoPower = false;
	}
	if (!editTopoPower) {
		cluetipShowDelaying = 500;
	}

	if (cssStyleName == 'office2003' || cssStyleName == 'apple') {
		SHAPE_STYLE.linkSelectedColor = '#316ac5';
	} else if (cssStyleName == 'vista') {
		SHAPE_STYLE.linkSelectedColor = '#99dffd';
	} else {
		SHAPE_STYLE.linkSelectedColor = '#cebd8b';
	}
	SHAPE_STYLE.linkWeight = '<s:property value="topoFolder.linkWidth"/>';	
	SHAPE_STYLE.linkColor = '<s:property value="topoFolder.linkColor"/>';
	SHAPE_STYLE.linkShadow = <s:property value="topoFolder.linkShadow"/>;	

	displayName = <s:property value="topoFolder.displayName"/>;
	displaySysName = <s:property value="topoFolder.displaySysName"/>;
	showType = <s:property value="topoFolder.showType"/>;
    displayLink = <s:property value="topoFolder.displayLink"/>;
	displayLinkLabel = <s:property value="topoFolder.displayLinkLabel"/>;
	displayEntityLabel = <s:property value="topoFolder.displayEntityLabel"/>;
	displayAlertIcon = <s:property value="topoFolder.displayAlertIcon"/>;
	setCluetipEnabled(<s:property value="topoFolder.displayCluetip"/>);

	backgroundFlag = <s:property value="topoFolder.backgroundFlag"/>;
	backgroundImg = '<s:property value="topoFolder.backgroundImg"/>';
	backgroundColor = '<s:property value="topoFolder.backgroundColor"/>';
	backgroundPosition = <s:property value="topoFolder.backgroundPosition"/>;
	displayGrid = <s:property value="topoFolder.displayGrid"/>;
	backgroundGrid = '../css/grid_bg.gif';
	markerAlertMode = <s:property value="topoFolder.markerAlertMode"/>;
	zoomSize = <s:property value="topoFolder.zoom"/>;
	//---如果gotoId存在，则进入页面后就定位到该id指定的设备---//
	gotoId = <s:property value="entityId"/>;
	ZGOnload();
});

</script>
<style type="text/css">
#topoGraphSidePart{width:380px; height:400px; background:#F3F3F3; position:absolute; top:35px; left:0px; z-index:999; display:none; 
filter:alpha(opacity = 90); opacity:0.9; -moz-opacity:0.9; border-right:1px solid #9A9A9A;}
#zetagraph{ overflow:auto;}
</style>
</head>
<body class="CONTENT_WND">
	<div id="topoGraphSidePart">
		<iframe id="sidePart" name="sidePart" frameborder="0" width="100%" height="100%" src=""></iframe>
	</div>
	<div id="topoGraphSidePartArr" class="sideMapRight" style="display:none;"></div>
	
	<div id=view  style="visibility: hidden;position:absolute;top:28px;left:42px; width:200px; height:10px; z-index:9999;"> 
		<table width="80" border="1" bordercolor="#CED8F6" cellspacing="0" cellpadding="0" height="30" bgcolor="#CED8F6">
			<tr> 
	 			<td align="center"><font color="#000000"><fmt:message bundle="${res}" key="topo.grap.saveSuccess" /></font></td>
			</tr>
		</table>
	</div>
	<div id="toolbar" style="position: absolute; left: 0; top: 0; width:100%; z-index:9;"></div>
	<div id="graphContainer" style="position: absolute; left: 0; top: 26px"></div>
	<div id="mGraph" class=microContainer>
		<table height=100% cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td>
					<div id="microGraph-div" class=microGraph
						style="width: 276px; width: 182px;">
						<div id="microSquare-div" class="microSquare">
							<div></div>
						</div>
						<iframe name="zoomFrame" width=250 height=180 frameborder=0
							src="../network/zoom.html"></iframe>
					</div>
					</td>
				<td valign=top style="padding-left: 5px">
					<table width=17 cellspacing="0" cellpadding="0" border="0">
						<tr>
							<td><a href="javascript:tickSelect(1);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 100%">
									<img src="../js/zetaframework/css/images/fullpage.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 100%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:onZoomInClick();" title="<fmt:message bundle="${res}" key="topo.grap.zoomOut" />"> <img
									src="../js/zetaframework/css/images/panplus.gif" alt="<fmt:message bundle="${res}" key="topo.grap.zoomOut" />"
									border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(5);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 500%">
									<img id="tick500"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 500%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(3);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 300%">
									<img id="tick300"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 300%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(2);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 200%">
									<img id="tick200"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 200%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(1.5);"
								title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 150%"> <img id="tick150"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" />150%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(1.25);"
								title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 125%"> <img id="tick125"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 125%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(1);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 100%">
									<img id="tick100"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" />100%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(0.75);"
								title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 75%"> <img id="tick75"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 75%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(0.5);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" />50%">
									<img id="tick50"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 50%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(0.25);"
								title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 25%"> <img id="tick25"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 25%" border="0">
							</a>
							</td>
						</tr>

						<tr>
							<td><a href="javascript:tickSelect(0.1);" title="<fmt:message bundle="${res}" key="topo.grap.zoomTo" />10%">
									<img id="tick10"
									src="../js/zetaframework/css/images/tick-off.gif"
									alt="<fmt:message bundle="${res}" key="topo.grap.zoomTo" /> 10%" border="0">
							</a>
							</td>
						</tr>
						<tr>
							<td><a href="javascript:onZoomOutClick();" title="<fmt:message bundle="${res}" key="topo.grap.zoomIn" />"> <img
									src="../js/zetaframework/css/images/panminus.gif" alt="<fmt:message bundle="${res}" key="topo.grap.zoomIn" />"
									border="0">
							</a>
							</td>
						</tr>
					</table></td>
			</tr>
		</table>
	</div>
	<div style="display: none">
		<div id="drawings-div" class="DRAWING-CONTAINER openWinBody" align=center>
			<div class="pL10">
				<div id="drawing0" class=DRAWING-TOOL-SELECTED align=center
					title="<fmt:message bundle="${res}" key="topo.grap.chooseTool" />" onclick="setDrawingType(0)">
					<img src="../images/zgraph/select.gif" border=0 align=absmiddle>
				</div>
				<div id="drawing2" class=DRAWING-TOOL align=center title="<fmt:message bundle="${res}" key="topo.grap.tool.hander" />"
					onclick="setDrawingType(2)">
					<img src="../images/zgraph/snap.gif" border=0 align=absmiddle>
				</div>
				<div id="drawing5" class=DRAWING-TOOL align=center title="<fmt:message bundle="${res}" key="topo.grap.tool.line" />"
					onclick="setDrawingType(5)">
					<img src="../images/zgraph/straight.gif" border=0 align=absmiddle>
				</div>
				<div id="drawing95" class=DRAWING-TOOL align=center title="<fmt:message bundle="${res}" key="topo.grap.tool.2DGroup" />"
					onclick="setDrawingType(95)">
					<img drawingType=95 src="../images/zgraph/group.png" border=0
						align=absmiddle>
				</div>
				<div id="drawing96" class=DRAWING-TOOL align=center title="<fmt:message bundle="${res}" key="topo.grap.tool.3DGroup" />"
					onclick="setDrawingType(96)">
					<img drawingType=96 src="../images/zgraph/ellipseGroup.png" border=0
						align=absmiddle>
				</div>
				<%-- <% if(uc.hasSupportModule("cmc")){%>
					<div id="drawing201" class=DRAWING-TOOL align=center title="<fmt:message bundle="${res}" key="topo.grap.tool.virtual" />"
					onclick="setDrawingType(201)">
					<img drawingType=201 src="../images/zgraph/rhombus.gif" border=0
						align=absmiddle>
					</div> 
				<% } %> --%>
			</div>
		</div>
	
	<script type="text/javascript">
	$(function(){
		autoHeight();
		
		function autoHeight(){
			var h = $(window).height() - 35;
			if(h < 0) h=100;
			$("#topoGraphSidePart, #sidePart").height(h);
			
			var tPos = ($(window).height() - $("#topoGraphSidePartArr").outerHeight())/2;
			if(tPos <0) tPos = 100;
			$("#topoGraphSidePartArr").css("top",tPos);
		}
		
		$(window).resize(function(){
			autoHeight();
		});//end resize;
		
		$("#topoGraphSidePartArr").click(function(){
			if($("#topoGraphSidePartArr").hasClass("sideMapRight")){
				window.openCloseSidePart("open");
			}else{
				window.openCloseSidePart("close");
			}
		});
		
		//显示右键的提示信息;
		top.nm3kRightClickTips({
			title: I18N.MENU.tip,
			html: "<fmt:message bundle="${res}" key="topo.rightTips" />" 				
		});
		
	});//end docuemnt.ready;
	
	//显示侧边栏 ，属性部分内容;
	function showSidePart(url){
		url = "../" + url;
		$("#sidePart").attr("src", url);
		openCloseSidePart("open");
	};
	
	function openCloseSidePart(para){
		$("#topoGraphSidePartArr").css("display","block");
		if(para == "open"){
			if($("#topoGraphSidePartArr").hasClass("sideMapLeft")){//如果本来就是打开的;
				return;
			}
			$("#topoGraphSidePartArr").animate({left:380});
			$("#topoGraphSidePart").css("display","block").css("left",-380).animate({left:0},function(){
				$("#topoGraphSidePartArr").css("left",380).attr("class","sideMapLeft");	
			});
			
		}else{
			$("#topoGraphSidePartArr").animate({left:0});
			$("#topoGraphSidePart").animate({left:-380},function(){
				$(this).css("display","none");
				$("#topoGraphSidePartArr").css("left",0).attr("class","sideMapRight");
			})
						
		}
	}
	</script>
	
</body>
</html>
