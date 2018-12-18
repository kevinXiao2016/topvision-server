<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<link rel="stylesheet" type="text/css" href="../css/main.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%=cssStyleName%>/mytheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/reset.css"></link>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.openIcon" />.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/<fmt:message bundle="${resources}" key="COMMON.closeIcon" />.png) no-repeat;}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-tree.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/pkg-menu.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=<%-- com.topvision.ems.ap.resources,  --%>com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
function showExpander(view, expander) {
    var el = Zeta$(view);
    var visible = (el.style.display == "");
    visible = !visible;
    setExpanderVisible(view, expander, visible);
    setCookieValue(userName + expander, visible);
}
function setExpanderVisible(view, expander, visible) {
    Zeta$(view).style.display = visible ? "" : "none";
    var o = Zeta$(expander);
    o.className = visible ? "NAVI_EXPANDER_UP" : "NAVI_EXPANDER_DOWN";
    o.title = visible ? I18N.WorkBench.NAVI_EXPANDER_UP : I18N.WorkBench.NAVI_EXPANDER_DOWN;
}
/* cookie */
function getCookieValue(name, defaultValue) {
    return getCookie(name, defaultValue);
}
function setCookieValue(name, value) {
    setCookie(name, value);
}
function clearCookieValue(name) {
    delCookie(name);
}
function showSystemStatus(){
    window.parent.addView("systemStatus", "状态信息", "icoG1", "/admin/systemStatus.tv", null, true);
}
function showEngineInfo(){
    window.parent.addView("engineInfo", "Engine信息", "icoG4", "/admin/engineInfo.tv", null, true);
}
function showLogonInfo(){
    window.parent.addView("logonInfo", "登录信息", "icoG2", "/admin/logonInfo.tv", null, true);
}
function showJobs(){
    window.parent.addView("jobs", "后台任务", "icoG3", "/admin/viewJobs.tv", null, true);
}
function showMonitors(){
    window.parent.addView("monitors", "性能任务统计", "icoG3", "/admin/viewMonitors.tv", null, true);
}
function showExecutorThreadSnap() {
	window.parent.addView("executorThreadSnap", "engine线程历史图", "icoG3", "/admin/showExecutorThreadSnap.tv", null, true);
}
function showDelayedPerfMonitors() {
	window.parent.addView("delayedPerfMonitors", "超期未更新性能采集器", "icoG3", "/admin/showDelayedPerfMonitors.tv", null, true);
}

function showEngineHealth() {
	window.parent.addView("engineHealth", 'Engine运行情况', "icoG2", "/admin/showEngineHealth.tv");
}
function showSystemInfo(){
    window.parent.addView("sytemInfo", "基本信息", "icoG4", "/admin/systemInfo.tv");
}
function showBeanMgr(){
    window.parent.addView("beanMgr", "Bean管理", "icoG5", "/admin/beanMgr.tv");
}
function showLogConfig(){
    window.parent.addView("logConfig", "日志配置.bak", "icoG6", "/admin/logConfig.tv");
}
function showFrontEndLogger() {
	window.parent.addView("frontEndLogger", "日志配置", "icoG7", "/admin/showFrontEndLogger.tv");
}
function showLoggerView(){
    window.parent.addView("loggerView", "日志配置", "icoG7", "/admin/showLogger.tv");
}
function showLogFiles(){
    window.parent.addView("logFiles", "日志文件", "icoG8", "/admin/viewLogs.tv", null, true);
}
function showFiles(){
    window.parent.addView("files", "文件管理", "icoG8", "/admin/listFiles.tv", null, true);
}
function showCmd(){
    window.parent.addView("cmd", "CMD", "icoG5", "/admin/cmd.tv", null, true);
}
function showVersion(){
    window.parent.addView("version", "版本信息", "icoG9", "/admin/version.tv");
}
function showSql(){
    window.parent.addView("sql", "SQL操作", "icoG10", "/admin/showSql.tv");
}
function showHSql(){
    window.parent.addView("hsql", "HSQL操作", "icoG10", "/admin/queryHSql.tv");
}
function showInstallInfo(){
    window.parent.addView("installInfo", "安装信息", "icoG11", "/admin/getInstallInfo.tv");
}
function showDebug(){
    window.parent.addView("debug", "Debug", "icoG12", "/admin/debug.tv");
}
function showRestartAnalyzer(){
    window.parent.addView("restartAnalyzer", "设备重启统计", "icoE1", "/admin/showRestartAnalyzer.tv");
}
function showRestartStatistics(){
    window.parent.addView("restartStatistics", "设备重启报表", "icoE2", "/admin/showRestartStatistic.tv");
}
function showReportMgr(){
    window.parent.addView("reportMgr", "报表管理", "icoE3", "/admin/showReportManagement.tv");
}
function showDatabasePool(){
    window.parent.addView("databasePool", "连接池管理", "icoE4", "/admin/showDataSource.tv");
}
function showCpuAndMemoryAnalyzer(){
	window.parent.addView("cpuAndMemory", "CPU和内存统计", "icoE4", "/admin/showCpuAndMemory.tv");
}

function showTestSpectrumAlert(){
	 window.parent.addView("showTestSpectrumAlert", "噪声测试配置", "icoG7", "/cmcSpectrum/showTestSpectrumAlert.tv");
}

function showTestCmHistory(){
	window.parent.addView("showTestCmHistory", "CM历史测试", "icoG7", "/cmHistory/showTestCmHistory.tv");
}
function showOpenPerfCollect(){
	window.parent.addView("openPerfCollect", "性能采集开启", "icoE4", "/admin/showOpenPerfCollect.tv");
}
function showExportAndImport(){
	//TODO 测试导出
	//$.post('/export/generateExportExcel.tv');
	window.parent.addView("openExportAndImport", "导入导出管理", "icoE4", "/export/showExportAndImport.tv");
}

function showTelnetClient() {
	window.parent.addView("telnetClient-tab", "内置Telnet客户端", "icoE4", "/system/telnet/showTelnetClient.tv");
}

function showCmtsFlowSampling(){
    window.parent.addView("cmtsFlowSampling", "CMTS速率采样", "icoE4", "/admin/showCmtsFlowSampling.tv");
}

function showSendMessage() {
	//'modalDlg', I18N.MAIN.setPasswd, 600, 370, 'system/popModifyPasswd.tv', null, true, true
	window.top.createDialog('sendMessage', "模拟告警推送", 800, 500, '/admin/showSendMessage.tv', null, true, true);
	//window.top.createDialog("sendMessage", "模拟告警推送", "icoE4", "/admin/showSendMessage.tv");
	//window.parent.addView("sendMessage", "模拟告警推送", "icoE4", "/admin/showSendMessage.tv");
}

Ext.BLANK_IMAGE_URL = "../images/s.gif";

$(function(){
    //加载树形菜单;
    $("#tree").treeview({ 
        animated: "fast",
        control:"#sliderOuter"
    }); //end treeview;
    $("#sliderLeftBtn").click(function(){
        $("#bar").stop().animate({left:0});         
    })
    $("#sliderRightBtn").click(function(){
        $("#bar").stop().animate({left:88});        
    })
    
    //点击树形节点变橙色背景;
    $(".linkBtn").live("click",function(){
        $(".linkBtn").removeClass("selectedTree");
        $(this).addClass("selectedTree");
    });//end live;
    
    function autoHeight(){
        var h = $(window).height();
        var h1 = $("#putSlider").outerHeight();
        var h2 = 20; //因为#putTree{padding:10px};
        var h3 = $("#threeBoxLine").outerHeight();
        var h4 = $("#putBtn").outerHeight();
        var putTreeH = h - h1 - h2 - h3 - h4;
        if(putTreeH > 20){
            $("#putTree").height(putTreeH);
        }   
    };//end autoHeight;
    
    autoHeight();
    $(window).wresize(function(){
        autoHeight();
    });//end resize;
})
</script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script> 
</head>
<body>
    <div class="putSlider" id="putSlider">
        <div class="sliderOuter" id="sliderOuter">
            <a id="sliderLeftBtn" href="javascript:;"></a>
            <div id="slider" class="slider">
                <span id="bar" class="bar"></span>
            </div>
            <a id="sliderRightBtn" href="javascript:;"></a>
        </div>
    </div>
    <div class="putTree" id="putTree">
        <div style="width:100%; overflow:hidden;">
            <ul id="tree" class="filetree">
                <li><span class="icoG1"><a href="javascript:;" class="linkBtn" onclick="showSystemStatus()" name="systemStatus">状态信息</a></span></li>
                <li><span class="icoG1"><a href="javascript:;" class="linkBtn" onclick="showEngineInfo()" name="systemInfo">Engine状态信息</a></span></li>
                <li><span class="icoG2"><a href="javascript:;" class="linkBtn" onclick="showLogonInfo()" name="logonInfo">登录信息</a></span></li>
                <li><span class="icoE2"><a href="javascript:;" class="linkBtn" onclick="showCpuAndMemoryAnalyzer()" name="cpuAndMemory">CPU和内存统计</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showDatabasePool()" name="databasePool">连接池管理</a></span></li>
                <li><span class="icoG10"><a href="javascript:;" class="linkBtn" onclick="showSql()" name="sql">SQL操作</a></span></li>
                <li><span class="icoG10"><a href="javascript:;" class="linkBtn" onclick="showHSql()" name="hsql">HSQL操作</a></span></li>
                <li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showJobs()" name="jobs">后台任务</a></span></li>
                <li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showMonitors()" name="jobs">性能任务统计</a></span></li>
                <li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showDelayedPerfMonitors()" name="jobs">超期未更新perfmonitor</a></span></li>
                <li><span class="icoG3"><a href="javascript:;" class="linkBtn" onclick="showExecutorThreadSnap()" name="jobs">Engine线程历史</a></span></li>
            <li>
            	<a id="engineHealth" href="javascript:;" class='icoE13' onclick="showEngineHealth()">Engine运行情况</a>
            </li>
                <li><span class="icoG4"><a href="javascript:;" class="linkBtn" onclick="showSystemInfo()" name="systemInfo">基本信息</a></span></li>
                <li><span class="icoG5"><a href="javascript:;" class="linkBtn" onclick="showBeanMgr()" name="beanMgr">Bean管理</a></span></li>
                <!-- li><span class="icoG6"><a href="javascript:;" class="linkBtn" onclick="showLogConfig()" name="logConfig"></a>日志查看</span></li -->
                <li><span class="icoG7"><a href="javascript:;" class="linkBtn" onclick="showFrontEndLogger()" name="frontEndLog">前端日志配置</a></span></li>
                <li><span class="icoG7"><a href="javascript:;" class="linkBtn" onclick="showLoggerView()" name="loggerView">日志配置</a></span></li>
                <li><span class="icoG8"><a href="javascript:;" class="linkBtn" onclick="showLogFiles()" name="logFiles">日志文件</a></span></li>
                <li><span class="icoG11"><a href="javascript:;" class="linkBtn" onclick="showInstallInfo()" name="installInfo">安装信息</a></span></li>
                <li><span class="icoG12"><a href="javascript:;" class="linkBtn" onclick="showDebug()" name="debug">Debug</a></span></li>
                <li><span class="icoG5"><a href="javascript:;" class="linkBtn" onclick="showCmd()" name="cmd">CMD</a></span></li>
                <li><span class="icoG8"><a href="javascript:;" class="linkBtn" onclick="showFiles()" name="files">文件管理</a></span></li>
                <!-- li><span class="icoE1"><a href="javascript:;" class="linkBtn" onclick="showRestartAnalyzer()" name="restartAnalyzer">设备重启统计</a></span></li -->
                <!-- li><span class="icoE2"><a href="javascript:;" class="linkBtn" onclick="showRestartStatistics()" name="restartStatistics">设备重启报表</a></span></li-->
                <li><span class="icoE3"><a href="javascript:;" class="linkBtn" onclick="showReportMgr()" name="reportMgr">报表管理</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showTestSpectrumAlert()" name="databasePool">噪声测试配置</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showTestCmHistory()" name="databasePool">CM历史测试</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showCmtsFlowSampling()" name="cmtsFlowSampling">CMTS速率采样置</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showOpenPerfCollect()" name="openPerfCollect">性能采集开启</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showExportAndImport()" name="exportAndImport">导入导出管理</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showSendMessage()" name="exportAndImport">模拟告警发送</a></span></li>
                <li><span class="icoE4"><a href="javascript:;" class="linkBtn" onclick="showTelnetClient()" name="telnetClient">内置TELNET客户端</a></span></li>
            </ul>
        </div>
    </div>
    <div id="threeBoxLine"></div>
    <div class="putBtn" id="putBtn"></div>
</body>
</html>