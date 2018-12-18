<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.topvision.platform.domain.ToolbarButton"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html><head>
<%@ include file="include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.platform.resources" var="resources"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources2"/>
<title><fmt:message bundle="${resources}" key="app.title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" type="image/x-icon" href="<%= request.getContextPath() %>images/favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/main.css"/>
<link rel="stylesheet" type="text/css" href="css/messanger.css"/>
<link rel="stylesheet" type="text/css" href="css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="css/<%= cssStyleName %>/mytheme.css"/>

<s:iterator value="userContext.naviBarsVisible">
<link rel="stylesheet" type="text/css" href="<s:property value="name"/>/icon.css"/>
</s:iterator>
<%
    boolean system = uc.hasPower("system");
    boolean eventPower = uc.hasPower("event");
	List<ToolbarButton> toolbarButtons = uc.getToolbarButtons();
%>
<%@include file="include/tabPatch.inc"%>
<style type="text/css">
.PLAIN_TOOLBAR {background: url(css/s.gif) repeat-x;border: 0px;}
.x-layout-split {height: 4px; width: 4px;}
</style>

<!-- menubar  -->
<script type="text/javascript" src="js/jscookmenu/JSCookMenu.js"></script>
<link rel="stylesheet" type="text/css" href="css/<%= cssStyleName %>/jscookmenu/theme.css"/>
<script type="text/javascript" src="css/<%= cssStyleName %>/jscookmenu/theme.js"></script>

<script type="text/javascript">
var myDeskTopPower= <%=uc.hasPower("myDeskTop")%>;
var alertViewerPower= <%=uc.hasPower("alertViewer")%>;
var licenseManagementPower= <%=uc.hasPower("licenseManagement")%>;
var sysInfoManagementPower= <%=uc.hasPower("sysInfoManagement")%>;
var systemPower= <%=uc.hasPower("system")%>;
var loadingBarEl = null;
var musicFlag = ${musicFlag};
var loadingProgressEl = null;
function setLoadingProgress(progress) {
	if (loadingBarEl == null) {
		loadingBarEl = document.getElementById('loading-indicator');
		//loadingProgressEl = document.getElementById('loadingProgress');
	}
	loadingBarEl.style.width = parseInt(360 * progress / 100);
	//loadingProgressEl.innerText = progress + '%';
	if (progress >= 100) {
		Ext.get('loading-mask').fadeOut({remove: true});
	}
}
</script>
</head><body class="MAIN_WND" onunload="doMainOnUnload();"> 
<div id="loading-mask" align=center>
	<div id="loadingText"><img src="images/gray_loading.gif" align="absmiddle" border=0><span><fmt:message bundle="${resources}" key="loading.wait" /></span></div>
	<div id="loadingProgressBar"><div id="loading-indicator"></div></div>
	<!-- 
	<div id="loadingProgress"></div>
	 -->
	<div style="padding-top:50px;"><fmt:message bundle="${resources}" key="about.copyrights"/></div>
</div>

<div id="headerbar" style="z-index:2001">
<table width=100% cellspacing=0 cellpadding=0>
	<tr><td style="padding-left:6px;"><div id="myMenuID"></div></td>
	<td align=right style="padding-top:3px;padding-right:4px;">
	<%if(uc.hasPower("system")){ %>
		<div style="height:22px;white-space:nowrap;"><a href="#" onclick="modifyPersonalInfoClick();" style="font-weight:bold;font-size:10pt;"><s:property value="userContext.user.userName"/></a>&nbsp;&nbsp;-&nbsp;&nbsp;<fmt:formatDate value="${today}" type="date" dateStyle="full"/></div>
	<%}else{ %>
		<div style="height:22px;white-space:nowrap;"><a href="#" onclick="" style="font-weight:bold;font-size:10pt;"><s:property value="userContext.user.userName"/></a>&nbsp;&nbsp;-&nbsp;&nbsp;<fmt:formatDate value="${today}" type="date" dateStyle="full"/></div>
	<%} %>
	</td></tr>
	<tr><td colspan=2 style="padding-left:4px;white-space:nowrap;">
		<table cellspacing=0 cellpadding=0><tr>
		<td class=PLAIN_TOOLBAR_L></td>
		<Td class=PLAIN_TOOLBAR_C><div id="toolbar"></div></td>
		<Td class=PLAIN_TOOLBAR_R></td>
		</tr></table>
	</td></tr>
</table>
</div>

<div id=navi-div><table width=100% height=100% cellspacing=0 cellpadding=0>
	<tr><td id=naviContainer height=100% valign=top><iframe id="menuFrame" name="menuFrame" src="" 
		frameborder=0 width=100% height=100%></iframe></td></tr>
	<tr><td id=naviDivider valign=bottom align=center class=NAVI_DIVIDER
		onmouseover="this.className='NAVI_DIVIDER_OVER';" onmouseout="this.className='NAVI_DIVIDER';"
		onmousedown="dividerDown(event)" onmouseup="dividerUp(event)" onmousemove="dividerMove(event)" 
		ondblclick="setNaviDividerVisible()">
		<table width=100% cellspacing=0 cellpadding=0>
			<tr><td class=divider-l></td>
			<td class=divider-c align=center><div id=dividerImg title='<fmt:message bundle="${resources}" key="div.title.NaviDividerVisible" />' onclick="setNaviDividerVisible()"></div></td>
			<td class=divider-r></td></tr>
		</table>
	</td></tr>
	<tr id=naviBars><td class=NAVI_NAR_CONTAINER>
		<table id="naviBarsContainer" width=100% height=100% cellspacing=0 cellpadding=0></table>
	</td></tr>
	<tr id="naviBts"><td class=NAVI_BT_CONTAINER align=right>
		<table cellspacing=0 cellpadding=0><tr id="naviBtsContainer"></tr></table>
	</td></tr>
</table></div>

<div id="statusbar">
	<table width=100% cellspacing="0" cellpadding=0><tr>
	<td style="padding-top: 2px; padding-left: 4px; padding-right: 8px">
		<div id=statusInfo style="white-space:nowrap;"></div></td>
	<td id="progressBox" width=120px style="display:none;padding-right:8px;"></td>
	<td width=2px class=STATUS-SEPARATOR></td>
	<td width=100px style="padding-top: 2px; padding-left: 8px; padding-right: 8px" align=center>
		<div id=mapInfo style="white-space:nowrap;"></div></td>		
	<td width=2px class=STATUS-SEPARATOR></td>
	<td width=100px style="padding-top: 2px; padding-left: 8px; padding-right: 8px">
		<div id=coordInfo style="white-space:nowrap;"></div></td>
	<td width=2px class=STATUS-SEPARATOR></td>	
	<td width=244px style="padding-top: 2px; padding-left: 8px; padding-right: 8px">
	<%if(uc.hasPower("sysInfoManagement")){ %>
		<div style="white-space:nowrap;"><a href="#" title='<fmt:message bundle="${resources}" key="a.title.onShowRuntimeClick" />' onclick="onShowRuntimeClick()">
	<%} %>
		<img src="images/runtime.gif" align=absmiddle border=0>&nbsp;&nbsp;<fmt:message bundle="${resources}" key="img.text.runtime" />&nbsp;&nbsp;
		<span id="runtimeInfo"><fmt:message bundle="${resources}" key="span.text.runtimeInfo" /></span></a></div></td>
	<td width=2px class=STATUS-SEPARATOR></td>	  
	<td width=226px style="padding-top:2px; padding-right: 4px;" align=right>
		<div style="white-space:nowrap;"><a href="#" title='<fmt:message bundle="${resources}" key="a.title.onContactClick" />' onclick="onContactClick()"><fmt:message bundle="${resources}" key="app.copyrights"/></a></div></td>
	</tr></table>
</div>

<div id=fullscreenBt class="FULLSCREEN-BT" style="display:none"  onclick="enableMaxView();"
	onmouseover="this.className='FULLSCREEN-BT1';" onmouseout="this.className='FULLSCREEN-BT';"></div>

<div id=upTooltips class="upHelpTip" style="width:160px;display:none"></div>

<div id="music-div" style="display:none"></div>

<!-- for alert notify -->
<div id="alert-notify">
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td>
	<table width=100% cellspacing=8>
		<tr><td width=100px><img id="alert-img" src="images/fault/illegeal48.gif" hspace=5 border=0></td>
		<td><a id="alert-message"  href="javascript:void(0)" onclick="viewAllSecurity()"  style="font-size:10pt;font-weight:bold;color:red;"></a></td></tr>
		<tr><td colspan=2 style="height:2px;"><div class=hseparator style="height:2px;font-size:1px;"></div></td></tr>
		<tr><td><fmt:message bundle="${resources}" key="td.alert-notify.deviceAddress" /></td><td id="alert-name"></td></tr>
		<tr><td><fmt:message bundle="${resources}" key="td.alert-notify.source" /></td><td id="alert-mac"></td></tr>
		<tr><td><fmt:message bundle="${resources}" key="td.alert-notify.occuretime" /></td><td id="alert-time"></td></tr>
	</table>
</td></tr>
<tr><td valign=bottom align=right style="padding:10px">
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'" 
	onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'" 
	onclick="viewAllSecurity('<fmt:message bundle="${resources}" key="button.param.viewAllSecurity" />');"><fmt:message bundle="${resources}" key="button.text.viewAllSecurity" /></button>&nbsp;<button class=BUTTON75
	onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" 
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="closeMessanger()"><fmt:message bundle="${resources}" key="button.text.closeMessanger" /></button>
</td></tr>
</table>
</div>
<!-- for message notify -->
<div id="message-notify" style="display:none">
<table width=100% height=100% cellspacing=0 cellpadding=0>
<tr><td>
</td></tr>
<tr><td valign=bottom align=right style="padding:10px">
	<button class=BUTTON75 
	onMouseOver="this.className='BUTTON_OVER75'" onMouseOut="this.className='BUTTON75'" 
	onMouseDown="this.className='BUTTON_PRESSED75'" onclick="closeMessanger()"><fmt:message bundle="${resources}" key="button.text.closeMessanger" /></button>
</td></tr>
</table>
</div>

<!-- for message receiver -->
<iframe id="messageReceiver" name="messageReceiver" style="visibility:hidden;" 
	width=0 height=0 src="messageReceiver.jsp"></iframe>

<script type="text/javascript">setLoadingProgress(10);</script>
<script type="text/javascript" src="js/ext/ext-base.js"></script>
<script type="text/javascript">setLoadingProgress(15);</script>
<script type="text/javascript" src="js/ext/ext-all.js"></script>
<script type="text/javascript">setLoadingProgress(30);</script>
<script type="text/javascript" src="js/ext/ux/TabCloseMenu.js"></script>
<script type="text/javascript">setLoadingProgress(35);</script>  
<script type="text/javascript" src="js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">setLoadingProgress(40);</script>
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<script type="text/javascript">setLoadingProgress(45);</script>
<script type="text/javascript" src="js/jquery/ui.core.js"></script>
<script type="text/javascript" src="js/jquery/ux/ui.messanger.js"></script>
<script type="text/javascript">setLoadingProgress(55);</script>
<script type="text/javascript" src="js/zetaframework/zeta-core.js"></script>
    <script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">setLoadingProgress(60);</script>

<script type="text/javascript">
var displayMenuBar = true;
var topoEditPower = <%= uc.hasPower("topoEdit") %>;
var standalone = <s:property value="userContext.standalone"/>;
var aboutTitle = '<fmt:message bundle="${resources}" key="about.title"/>';
var appTitle = '<fmt:message bundle="${resources}" key="app.title"/>';
var solutionIndex = '<fmt:message bundle="${resources}" key="Topvision.solution"/>';
var supportIndex = '<fmt:message bundle="${resources}" key="Topvision.support"/>';
var userId = <s:property value="userContext.user.userId"/>;
var familyName = '<s:property value="userContext.user.familyName"/>';
var usedFirstly = <s:property value="usedFirstly"/>;
var isTabbed = <s:property value="userContext.tabbed"/>;//TODO isTabbed为何总是true
var tabMaxLimit = <s:property value="userContext.tabMaxLimit"/>;
var alarmWhenCloseManyTab = <s:property value="userContext.alarmWhenCloseManyTab"/>;
var switchWhenNewTab = <s:property value="userContext.switchWhenNewTab"/>;
var notifyWhenMsg = <s:property value="userContext.notifyWhenMsg"/>;
var eventMaxNumber = 25;
var MAX_NAVI_NUM = <s:property value="maxNaviNumber"/>;
var ZetaGUI = {enableWaitingDlg: false, roundedBorders: false, enableDraggable: false,
			   windowShadow: true, enableBorders: true, enableCollapsible: true, enableSplit: true,
			   tabPlain: false, tabBorders: true, resizeTabs: true, minTabWidth: 80, tabWidth: 150,
			   NAVI_BAR_HEIGHT: 32, headerHeight: 56, viewMargin: {left: 4, right: 4, top: 0, bottom: 4}};
if ('vista' == cssStyleName) {
	ZetaGUI.NAVI_BAR_HEIGHT = 21;
//	ZetaGUI.viewMargin.left = ZetaGUI.viewMargin.right = 0;
} else if ('apple' == cssStyleName && !displayMenuBar) {
	ZetaGUI.tabPlain = true;
}

// for menu bar
<%@include file="menubar.inc"%>

function loadToolbarButton() {
	cmTheme.clickOpen = 2;
	cmDraw ('myMenuID', myMenu, 'hbr', cmTheme);
	var tb = new Ext.Toolbar({cls : 'PLAIN_TOOLBAR'});
	tb.render('toolbar');
	tb.add(
		{iconCls: 'bmenu_back', handler: previousViewClick, tooltipType: 'title', tooltip: I18N.COMMON.backward},	
		{iconCls: 'bmenu_forward', handler: nextViewClick, tooltipType: 'title', tooltip: I18N.COMMON.forward}, '-',
		{iconCls: 'bmenu_startPage', handler: goToStartPage, tooltipType: 'title', tooltip: I18N.COMMON.startPage},
		{iconCls: 'bmenu_home', handler: showMyDesktop, tooltipType: 'title', tooltip: I18N.COMMON.myDesktop,hidden:!myDeskTopPower}, '-',
		{iconCls: 'bmenu_f5', handler: onRefreshClick, tooltipType: 'title', tooltip: I18N.COMMON.refresh},
		{iconCls: 'bmenu_maxview', handler: onMaxViewClick, tooltipType: 'title', tooltip: I18N.COMMON.maxView}
	);	
	/*
	<s:if test="userContext.toolbarButtons.size>0">	
	try {
		tb.add('-');
		<s:iterator value="userContext.toolbarButtons">
		<s:if test="type==1">
		tb.add({text: '<s:property value="text"/>', icon: '<s:property value="icon"/>', handler: <s:property value="action"/>, tooltipType: 'title', tooltip: '<s:property value="tooltip"/>'});
		</s:if><s:elseif test="type==0">
		tb.add('-');
		</s:elseif>
		</s:iterator>
	} catch (err) {
	}
	</s:if>
	*/
	<%
		int i;
		for(i = 0; i < toolbarButtons.size(); i++){
		    if(toolbarButtons.get(i).getButtonId() == 1080 && system){
	%>
	tb.add('-');
	tb.add({text: '', icon: 'images/preferences.gif', handler: showGmView, tooltipType: 'title', tooltip: I18N.COMMON.gmView,hidden:!systemPower});
	<%
		    }
		
			if(toolbarButtons.get(i).getButtonId() == 1050 && eventPower){
	%>
	tb.add('-');
	tb.add({text: '', icon: 'images/fault/bt.gif', handler: showCurrentAlert, tooltipType: 'title', tooltip: I18N.EVENT.currentAlarmViewer,hidden:!alertViewerPower});
	<%
			}
		}
	%>
	tb.add('-');
	tb.add({text: '', icon: 'images/help.gif', handler: onHelpClick, tooltipType: 'title', tooltip: I18N.COMMON.help});
	tb.add('-');
	tb.add({text: '', icon: 'images/logoff.gif', handler: onLogoffClick, tooltipType: 'title', tooltip: I18N.COMMON.exit});
	tb.add('-');
	tb.doLayout();
}

function loadModules() {
	<s:iterator value="userContext.naviBarsVisible">
	addModule(getI18NModuleString('<s:property value="name"/>'), '<s:property value="icon24"/>', '<s:property value="icon16"/>', '<s:property value="name"/>', '<s:property value="action"/>');
	</s:iterator>
	createMoreBt();
	curNaviBarNumber = getCookieValue(userName + "curNaviBarNumber", MAX_NAVI_NUM);
	curNaviBarNumber = (curNaviBarNumber <= naviBarList.length ? curNaviBarNumber : naviBarList.length);
	curNaviBarNumber = (curNaviBarNumber > MAX_NAVI_NUM ? MAX_NAVI_NUM : curNaviBarNumber);	
	defaultStatusInfo = '<fmt:message bundle="${resources2}" key="SYSTEM.license.licensedTo"/><s:property value="licenseView.organisation"/>';
	setStatusBarInfo(defaultStatusInfo, false);
	startTimeMillis = <%= uc.getUser().getLastLoginTime().getTime()/1000 %>;
}
</script>
<script type="text/javascript">setLoadingProgress(70);</script>
<script type="text/javascript" src="main.js"></script>
</body></html>