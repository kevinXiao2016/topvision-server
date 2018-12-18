<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	CSS css.main
    CSS css.messanger
    LIBRARY EXT
    LIBRARY JQUERY 
    LIBRARY ZETA
    LIBRARY Socket
    MODULE  PLATFORM
    IMPORT js.jquery.Nm3kMsg
    IMPORT js.constants.OltTrapTypeId
    IMPORT js.constants.CmcTrapTypeId
    IMPORT js.utils.PubSub
    IMPORT js.utils.ExcelUtil
    IMPORT js.utils.IpUtil
    IMPORT js.utils.MacUtil
    IMPORT js.utils.WindowUtil
    IMPORT js.utils.TabUtil
    IMPORT js.utils.Ping
    IMPORT js.utils.AlertUtil
    IMPORT js.utils.HelpUtil
    IMPORT js.utils.HttpUtil
    IMPORT js.utils.MessageUtil
    IMPORT js.utils.TopoUtil
    IMPORT js.lib.moment.moment
    IMPORT js.utils.DateUtil
    IMPORT js.entityType
    IMPORT js.jquery.dragMiddle static
    IMPORT js.zetaframework.Validator
    IMPORT js.zetaframework.VersionControl
    IMPORT main
</Zeta:Loader>
<script type="text/javascript" src="js/jquery/TopMsgTips.js"></script>
<title>@app.title@</title>
<link rel="shortcut icon" href="<%= request.getContextPath() %>images/favicon.ico" type="image/x-icon" />
<%@include file="include/tabPatch.inc"%>
<style type="text/css">
#menu_tips{ height:28px; padding-left:12px; position:absolute; left:210px; top:100px; z-index:99999999999999999; background:url(images/tip_left.gif) no-repeat; float:left; display:none;}
#menu_tips dd{ height:28px; background:#c00; line-height:26px; padding:0px 10px; color:#fff; text-shadow:#840000 0px 1px; float:left;}
.PLAIN_TOOLBAR {background: url(css/s.gif) repeat-x;border: 0px;}
.x-layout-split {height: 4px;width: 4px;}
#logo {	background: url("@app.logo@") no-repeat;width: 330px;height: 50px;}
.x-panel-mr, .x-panel-ml, .x-panel-mc {padding : 0 0 0 0 ;}
/* .x-panel-nofooter{height : 0px;overflow: hidden;}  */
#navi-panel .x-panel-nofooter{height : 0px;overflow: hidden;}
</style>
<script type="text/javascript">
//usercontext 相关属性初始化
var tipShowTime = '<%= uc.getTipShowTime() %>';
var userName = '<%= uc.getUser().getUserName() %>';
var firstDayOfWeek = '<%= uc.getWeekStartDay() %>';
var userId = '<%= uc.getUser().getUserId() %>';
var entityDisplayField = '<%= uc.getPreference("user.displayField") %>';
var topoEditPower = <%= uc.hasPower("topoEdit") %>;
var alertViewer = <%= uc.hasPower("alertViewer") %>;
var language = '<%=uc.getUser().getLanguage() %>';
var standalone = ${userContext.standalone};
var familyName = '${userContext.user.familyName}';
var isTabbed = ${userContext.tabbed};
var tabMaxLimit = ${userContext.tabMaxLimit};
var alarmWhenCloseManyTab = ${userContext.alarmWhenCloseManyTab};
var switchWhenNewTab = ${userContext.switchWhenNewTab};
var notifyWhenMsg = ${userContext.notifyWhenMsg};
var frontEndLogSwitch = ${frontEndLogSwitch};

//var supportGZ = <%= uc.hasSupportProject("gz") %>;
var supportGZ = false;

var socketServerPort = '${socketServerPort}';

//常量定义
var eventMaxNumber = 25;
var GLOBAL_SOCKET;
var MAX_TAB_NUM = 11;
var naviPanelState = 2;
var propertytPanelState = 2;
var DEFAULT_STATUS_INFO = EMPTY;

//声音/音乐相关变量
var musicFlag = ${musicFlag};
var soundTimeInterval = '<%= uc.getSoundTimeInterval() %>';
soundTimeInterval = parseInt(soundTimeInterval, 10) * 60000;
var oAlertSoundInterval = {             //存储声音间隔相关属性;
	timeIntrerval : 300,                //每300毫秒去计算一次时间差;
	lastTime      : 0,                  //上一次定时器记录的时间;
	soudInterval  : soundTimeInterval,  //默认一分钟;
	timeOut       : null                //存储setTimeout，当下一个告警来的时候，需要清除掉当前的定时器;
}
var oAlertSound ={};

//导航动画相关变量
var navCartoonFlag = ${navCartoonFlag};


var isLockScreen = ${lockScreen};
var lastSelectedNaviBar = "${lastSelectedNaviBar}";
var loadingBarEl,
    loadingProgressEl;
    
var usedFirstly = ${usedFirstly};
var ZetaGUI = {enableWaitingDlg: false, roundedBorders: true, enableDraggable: false,
               windowShadow: true, enableBorders: true, enableCollapsible: true, enableSplit: true,
               tabPlain: false, tabBorders: true, resizeTabs: true, minTabWidth: 80, tabWidth: 150,
               NAVI_BAR_HEIGHT: 32, headerHeight: 66, viewMargin: {left: 0, right: 0, top: 0, bottom: 0}};
var nm3kObj = {};//用来记录nm3kMsg控件弹出的框;
//nm3kObj.nm3kValidateTips;//记录验证提示;
//nm3kObj.nm3kSaveOrDelete;//记录保存成功，保存失败，删除成功，删除失败;
//nm3kObj.nm3kRightClickTips;//记录该页面可以使用右键的提示;
//nm3kObj.nm3kAlertTips;//记录告警提示;

var concernAlerts = [];

// public callback function
var ZetaCallback,
    ZetaClipboard,
    ZetaClickOltLink, //如果点击了拓扑图左侧的olt链接，那么将id存入。如果点击了main里面任何东西，将其清空;
    player = null,
    currentAlertType ,
    musicUrl = '',
    headerPanel,
    messageFrame,
    contentPanel,
    eventStore,
    contentView,
    viewport,
    messanger,
    mapInfoElement,  
    coordInfoElement,
    progressInterval,
    singleModalDlg,
    singleModelFrame, 
    singleModalCallback,
    homeView,
    contentFrameById,
    contentFrameByName,
    nm3kMsgAlert,//告警
    ignoreTimeout = true,
    tMsg, //公告牌只有一个，需要更新它的时候也是调用他，因此只好将其设为全局变量;
    nm3kAlertObj = {msg:EMPTY,add:EMPTY,mac:EMPTY,time:EMPTY,typeName:EMPTY},//记录告警的各种信息;
    isServerShutDown = false;
var billboardStore = [];

var maxView = false,
    isPlaying = false,
    processing = false,
    tabChangedListened = true;

var functionMapping = new Zeta$H();
var titleMapping = new Zeta$H();

/* message dispatcher */
var startTimeMillis = 0;
var pingInterval = 20 * 1000;
var pingTimer = null;
var pingFailureCount = 0;

var dayTimeMillis = 24 * 3600;
var hourTimeMillis = 3600;
var minuteTimeMillis = 60;
var dayStr, hourStr, minuteStr, secondStr;

/* load navigation bar and button */
var curNaviBarNumber = 8;
var moreMenu = null;
var moreMenuItem = null;
var lessMenuItem = null;

var dividerDragging = false;
var dividerOy;

function showMoreMenu(el){
    var $el = $(el);
    var menu = Ext.getCmp("moreActionMenu");
    if(!menu){
        var menuitems = [
            {text: I18N.MenuItem.onShowMessangerClick, handler: onShowMessangerClick},
            <%if(uc.hasPower("alertViewer")){ %>
            {text: '@resources/EVENT.alarmViewer@', handler: showCurrentAlert},
            <%}%>
            '-',
            {text: I18N.COMMON.setHome, handler: setHomeClick},
            {text: I18N.MenuItem.customDesktopClick, handler: customDesktopClick},
            <%if(uc.getUserId()!=1 && uc.getUserId()!=2){ %>
            {text: I18N.MenuItem.switchRootFolder, handler: switchRootFolder},
            <%}%>
            <%if(uc.hasPower("pwdEdit")){ %>
            {text: "@resources/MAIN.setPasswd@", handler: setPasswdClick},
            <%}%>
            <%if(uc.hasPower("personalize")){ %>
            {text: I18N.MenuItem.individualization, handler: showPersonalize},
            <%}%>
            '-',
            {text: I18N.MenuItem.openSoundTip , checked: musicFlag, handler: openSoundTip},
            {text: I18N.SYSTEM.NavigationAnimation, checked:navCartoonFlag,  id: "navCartoon", handler: saveNavCartoon},
            '-',
            {text:I18N.MenuItem.onShowRuntimeClick, handler: onShowRuntimeClick,hidden:!<%=uc.hasPower("sysInfoManagement")%>},
            <%if(uc.hasPower("personalInfo")){ %>
            {text:I18N.SYSTEM.PersonalInformation, handler: modifyPersonalInfoClick},
            <%}%>
            {text:I18N.MenuItem.onContactClick, handler: onContactClick},
            {text:'@COMMON.lockScreen@', handler: lockScreen},
            {text:I18N.userPower.announcement, handler: showBillboard},
            <%if(uc.hasPower("announcement")){ %>
            {text: I18N.MenuItem.about, handler: onAboutClick},
            <%}%>
        ];
        menu = new Ext.menu.Menu({
            minWidth: 160, 
            items: menuitems,
            id: "moreActionMenu"
        });
    }
    var $offset = $el.offset();
    if(menu.isVisible()){
    	menu.hide();
    }else{
    	menu.showAt([ $offset.left-110, $offset.top+29]);
    }
    
}

</script>
</head>
<body class="MAIN_WND" onunload="doMainOnUnload();">
	<!--   进入时的Loading Mask  -->
	<div id="loading-mask" align=center>
		<div id="loadingText">
			<img src="images/loadingCirIco.gif" align="absmiddle" border=0 /><span>@loading.wait@</span>
		</div>
		<div id="loadingProgressBar">
			<div id="loading-indicator"></div>
		</div>
		<div style="padding-top: 50px;">@about.copyrights@</div>
	</div>

	<!-- 	顶部导航栏BLOCK	 -->
	<div id="headerbar">
		<table width=100% cellspacing=0 cellpadding=0>
			<tr>
				<td width=350px>
				 <img src="css/white/@resources/SYSTEM.LogoImg@" />
				</td>
				<td align=right style="padding-top: 2px;">
					<table height=50px cellspacing=0 cellpadding=0>
						<tr>
							<td align=right valign=top colspan=2 height=25px
								style="padding-right: 400px;">
								<div id="toolbar"></div>
							</td>
						</tr>
						<tr>
							<td class=HEADER_BAR style="white-space: nowrap; padding-top: 4px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<dl class="menuDl" id="menuDl">
			<dt></dt>
			<%if(uc.hasPower("workbench")){ %>
			<dd>
				<ul class="menuBtn" alt="Workbench" id="menuBtn_workbench" onclick="showNaviBar('Workbench','workbench/showWorkbench.tv');">
					<li class="menuBtnIco0">@resources/SYSTEM.Workbench@</li>
					<li class="menuBtnIcoHover0">@resources/SYSTEM.Workbench@</li>
					<li class="menuBtnIcoSelected0">@resources/SYSTEM.Workbench@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<%if(uc.hasPower("network")){ %>
			<dd>
				<ul class="menuBtn" alt="Network" id="menuBtn_product" onclick="showNaviBar('Network','workbench/showNetwork.tv');">
					<li class="menuBtnIco1">@resources/SYSTEM.Equipment@</li>
					<li class="menuBtnIcoHover1">@resources/SYSTEM.Equipment@</li>
					<li class="menuBtnIcoSelected1">@resources/SYSTEM.Equipment@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<%if(uc.hasPower("topographic")){ %>
			<dd>
				<ul class="menuBtn" alt="Topology" id="menuBtn_topo" onclick="showNaviBar('Topology','workbench/showTopology.tv');">
					<li class="menuBtnIco2">@resources/SYSTEM.Topology@</li>
					<li class="menuBtnIcoHover2">@resources/SYSTEM.Topology@</li>
					<li class="menuBtnIcoSelected2">@resources/SYSTEM.Topology@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<dd>
				<ul class="menuBtn" alt="Terminal" id="menuBtn_config" onclick="showNaviBar('Terminal','workbench/showTerminal.tv');">
					<li class="menuBtnIco1">终端管理</li>
					<li class="menuBtnIcoHover1">终端管理</li>
					<li class="menuBtnIcoSelected1">终端管理</li>
				</ul>
			</dd>
			<dt></dt>
			<%if(uc.hasPower("config")){ %>
			<dd>
				<ul class="menuBtn" alt="Config" id="menuBtn_config" onclick="showNaviBar('Config','workbench/showConfig.tv');">
					<li class="menuBtnIco3">@resources/SYSTEM.Configuration@</li>
					<li class="menuBtnIcoHover3">@resources/SYSTEM.Configuration@</li>
					<li class="menuBtnIcoSelected3">@resources/SYSTEM.Configuration@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<%if(uc.hasPower("performance")){ %>
			<dd>
				<ul class="menuBtn"  alt="Performance" id="menuBtn_performance" onclick="showNaviBar('Performance','workbench/showPerformance.tv');">
					<li class="menuBtnIco4">@resources/SYSTEM.Performance@</li>
					<li class="menuBtnIcoHover4">@resources/SYSTEM.Performance@</li>
					<li class="menuBtnIcoSelected4">@resources/SYSTEM.Performance@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<%if(uc.hasPower("event")){ %>
			<dd>
				<ul class="menuBtn"  alt="Fault" id="menuBtn_fault" onclick="showNaviBar('Fault','workbench/showFault.tv');">
					<li class="menuBtnIco5">@resources/SYSTEM.Fault@</li>
					<li class="menuBtnIcoHover5">@resources/SYSTEM.Fault@</li>
					<li class="menuBtnIcoSelected5">@resources/SYSTEM.Fault@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
			<%if(uc.hasPower("report")){ %>
			<dd>
				<ul class="menuBtn" alt="Report" id="menuBtn_report" onclick="showNaviBar('Report','workbench/showReport.tv');">
					<li class="menuBtnIco6">@resources/SYSTEM.Report@</li>
					<li class="menuBtnIcoHover6">@resources/SYSTEM.Report@</li>
					<li class="menuBtnIcoSelected6">@resources/SYSTEM.Report@</li>
				</ul>
			</dd>
			<dt></dt>
			<% }%>
            <%if(uc.hasPower("system")){ %>
            <dd>
                <ul class="menuBtn" alt="System" id="menuBtn_system" onclick="showNaviBar('System','workbench/showSystem.tv');">
                    <li class="menuBtnIco7">@resources/SYSTEM.System@</li>
                    <li class="menuBtnIcoHover7">@resources/SYSTEM.System@</li>
                    <li class="menuBtnIcoSelected7">@resources/SYSTEM.System@</li>
                </ul>
            </dd>
            <dt></dt>
            <% }%>
            <%if(uc.hasPower("operationMgmt")){ %>
            <dd>
                <ul class="menuBtn" alt="Operation" id="menuBtn_operation" onclick="showNaviBar('Operation','workbench/showOperation.tv');">
                    <li class="menuBtnIco9">@resources/SYSTEM.Operation@</li>
                    <li class="menuBtnIcoHover9">@resources/SYSTEM.Operation@</li>
                    <li class="menuBtnIcoSelected9">@resources/SYSTEM.Operation@</li>
                </ul>
            </dd>
            <dt></dt>
            <% }%>
            <%if(uc.hasPower("admin")){ %>
            <dd>
                <ul class="menuBtn" alt="Admin" id="menuBtn_admin" onclick="showNaviBar('Admin','admin/showAdmin.tv');">
                    <li class="menuBtnIco3">Admin</li>
                    <li class="menuBtnIcoHover3">Admin</li>
                    <li class="menuBtnIcoSelected3">Admin</li>
                </ul>
            </dd>
            <dt></dt>
            <% }%>
            <dd class="lastShaden"></dd>
		</dl>
		
		<ol class="headerRightOl">
			<li><a id="helpCir" href="javascript:void(0)" class="headerRightBtn0" title="@COMMON.help@"></a></li>
			<li><a href="javascript:void(0)" class="headerRightBtn1" onclick="showMyDesktop();" title="@resources/COMMON.myDesktop@"></a></li>
			<%if(uc.hasPower("system")){ %>
			<li><a href="javascript:void(0)" class="headerRightBtn2" title="@resources/COMMON.gmView@" onclick="showGmView();"></a></li>
			<%} %>
			<%-- <%if(uc.hasPower("announcement")){ %>
			<li><a href="javascript:void(0)" class="headerRightBtn3" title="@resources/userPower.announcement@" onclick="showBillboard();"></a></li>
			<%} %> --%>
			<%if(uc.hasPower("myFolder")){ %>
			<li><a href="javascript:void(0)" class="headerRightBtn3" title="@resources/MenuItem.getFramePage@" onclick="getMainFrameToFavorite();"></a></li>
			<%} %>
			<%-- <%if(uc.hasPower("alertViewer")){ %>
			<li><a href="javascript:void(0)" class="headerRightBtn3" title="@resources/EVENT.alarmViewer@" onclick="showCurrentAlert();"></a></li>
			<%} %> --%>
			<li><a href="javascript:void(0)" class="headerRightBtn4" title="@resources/SYSTEM.Cancellation@" onclick="onLogoffClick()"></a></li>
			<li><a href="javascript:void(0)" class="headerRightBtn5" title="@resources/SYSTEM.Other@" onmousedown="showMoreMenu(this);"></a></li>
		</ol>
	</div>

	<!--        左侧导航栏BLOCK       -->
	<div id="navi-div" style="height : 100%;">
		<table width=100% height=100% cellspacing=0 cellpadding=0>
			<tr>
				<td id=naviContainer height=100% valign=top>
					<iframe id="menuFrame" name="menuFrame" src="workbench/show${lastSelectedNaviBar}.tv" frameborder=0 width=100% height=100%></iframe>
				</td>
			</tr>
		</table>
	</div>
	
	<!-- 提示信息 -->
	<dl id="menu_tips">	<dd>tips</dd>	</dl>

	<div id="music-div" style="position:absolute; top:0; left:-500px; width:10px;overflow:hidden;"></div>
	<div id="lockScreenDiv">
		<div id="lockScreenOuter">
			<div class="lockScreenLogo"></div>
			<div class="lockScreenLeftTxt">
				<p>@COMMON.screenLock@</p>
				<p>@COMMON.enterPass@</p>
			</div>
			<div class="lockScreenPic">
				<div class="lockScreenCir"></div>
				<div class="lockUser">
					<img src="images/lockUser.jpg" width="81" height="81" />
				</div>
			</div>
			<dl class="lockScreenRightPart">
				<dd class="lockName"><%= uc.getUser().getUserName() %></dd>
				<dd>@COMMON.pleaseEnter@</dd>
				<dd><input id="lockPass" type="password" class="normalInput w180" style="border:1px solid #016CA2;" onkeydown="addEnterKey(event);"/></dd>
				<dd style="padding-left:2px;"><a href="javascript:;" class="normalBtnBig" onclick="openLockScreen()"><span><i class="miniIcoSaveOK"></i>@resources/COMMON.ok@</span></a></dd>
				<dd class="clearBoth pT5" id="lockScreenTip"></dd>
			</dl>
		</div>
	</div>
	
	<!-- for message receiver -->
	<iframe id="messageReceiver" name="messageReceiver" style="visibility: hidden;" width=0 height=0 src="messageReceiver.jsp"></iframe>
	
	<iframe id="main_download_iframe" style="display:none;"></iframe>	
	
	<script type="text/javascript" src="js/jquery/TopMsgTips.js"></script>
	
	<script type="text/javascript" src="js/utils/CommonRenderer.js"></script>
	
	<script type="text/javascript" src="/js/ext/ux/TabCloseMenu.js"></script>
	<script type="text/javascript" src="js/jquery/ui.core.js"></script>
	<script type="text/javascript" src="js/jquery/ux/ui.messanger.js"></script>
    <script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.platform.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
</body>
</Zeta:HTML> 
