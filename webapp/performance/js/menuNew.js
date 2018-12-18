function buildWorkbenchModule() {}

function doOnResize() {}

/*********************************************
		设备子菜单是否折叠
**********************************************/
function showExpander(view, expander) {
	var el = Zeta$(view);
	var visible = (el.style.display == "" || el.style.display == "block");
	visible = !visible;
	setExpanderVisible(view, expander, visible);
	setCookieValue(userName + expander, visible);
}

/*********************************************
	    设备子菜单是否展示,并修改折叠图标样式
**********************************************/
function setExpanderVisible(view, expander, visible) {
	Zeta$(view).style.display = visible ? "block" : "none";
	var o = Zeta$(expander);
	o.className = visible ? "NAVI_EXPANDER_UP" : "NAVI_EXPANDER_DOWN";
	o.title = visible ? I18N.WorkBench.NAVI_EXPANDER_UP : I18N.WorkBench.NAVI_EXPANDER_DOWN;
}


/* cookie */
function getCookieValue(name, defaultValue) {
	return getCookie(name, defaultValue);
}

/*********************************************
		设置COOKIE,方便下一次进入同一菜单
**********************************************/
function setCookieValue(name, value) {
	setCookie(name, value);
}

function clearCookieValue(name) {
	delCookie(name);
}

Ext.BLANK_IMAGE_URL = "../images/s.gif";

Ext.onReady(function () {
	doOnResize();
	buildWorkbenchModule();
});

//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//
//★★★★★★★★★★     新菜单结构(bravin)  ★★★★★★★★★★★//  
//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★//

/************************************
 		查看性能视图 
 *************************************/
function showPerformanceView(){}

/************************************
		查看EPON性能快速配置
*************************************/
function showEponFastConfig(){
	window.parent.addView("perfFastConfig", I18N.COMMON.quickSet, "eponFastConfigIcon", "/epon/oltPerfFastConfig.jsp");
}

/************************************
		查看EPON性能模板配置
*************************************/
function showEponTemplateConfig(){
	window.parent.addView("oltPerfTemplate", I18N.Performance.oltPerfTemplate, "eponTemplateIcon", "/epon/oltPerfTemplate.jsp");
}

/************************************
		查看EPON性能采集器
*************************************/
function showEponCollector(){
	window.parent.addView("oltCollectorMgmt", I18N.Performance.oltCollectorMgmt, "eponCollectorIcon", "/epon/oltPerfCollector.jsp");
}

/************************************
		查看CCMTS性能采集管理
*************************************/
function cmcPerfParamConfig(){
	window.parent.addView("cmcPerfParamConfig", I18N.Performance.cmcPerfParamConfig, "icoE6", "/cmc/perfTarget/showCmcPerfManage.tv");
}

/************************************
查看CMTS性能采集管理
*************************************/
function cmtsPerfParamConfig(){
window.parent.addView("cmtsPerfParamConfig", I18N.Performance.cmtsPerfParamConfig, "icoE6", "/cmts/perfTarget/showCmtsPerfManage.tv");
}

/************************************
查看EPON性能采集管理
*************************************/
function eponPerfParamConfig(){
    window.parent.addView("eponPerfParamConfig", I18N.Performance.oltPerformanConfig, "icoE4", "/epon/perfTarget/showOltPerfManage.tv");
}

/************************************
		查看CMC性能模板配置
*************************************/
function showCmcTemplateConfig(){}

/************************************
		查看CMC性能采集器
*************************************/
function showCmcCollector(){}

/************************************
		查看EPON端口监视器
*************************************/
function showEponPortMonitor(){
	window.parent.addView("EponPortMonitor", I18N.Performance.EponPortMonitor, "snap", "/performance/showMonitorDisplayer.tv",null,true);
}

/************************************
		查看EPON设备监视器
*************************************/
function showEponDeviceMonitor(){
	window.parent.addView("EponDeviceMonitor", I18N.Performance.EponDeviceMonitor, "eponDeviceMonitorIcon", "/performance/showMonitorDisplayer.tv",null,true);
}

/************************************
		查看CMC设备监视器
*************************************/
function showCmcDeviceMonitor(){
	window.parent.addView("CmcDeviceMonitor", I18N.Performance.CmcDeviceMonitor, "mydesktopIcon", "/performance/showMonitorDisplayer.tv",null,true);
}

/************************************
		查看CCMTS用户数查询
*************************************/
function showCmcUserNum(){
	window.parent.addView("CmcUserNum", I18N.cmCpe.cmcUserNumHis, "icoG4", "/cmCpe/showCmcHisUserNum.tv",null,true);
}

/************************************
		查看全网CCMTS实时用户数
*************************************/
function showCmcAllCcmtsUserNum(){
	window.parent.addView("AllCcmtsUserNum", I18N.cmCpe.cmcAllCcmtsUserNum, "icoG2", "/cmCpe/showAllCcmtsRealTimeUserNum.tv",null,true);
}

/************************************
		查看终端采集配置页面
*************************************/
function showTerminalCollectConfig(){
	window.parent.addView("terminalCollectConfig", I18N.cmCpe.terminalConfig, "icoE13", "/cmCpe/showTerminalCollectConfig.tv");
}

/************************************
		CMTS频谱采集配置页面
*************************************/
function showCmtsSpectrumConfig(){
    window.parent.addView("showCmtsSpectrumConfig", I18N.spectrum.spectrumCollectConfig, "icoG7", "/cmcSpectrum/showCmtsSpectrumConfig.tv");
}

/************************************
        OLT频谱采集配置页面
*************************************/
function showOltSpectrumConfig(){
    window.parent.addView("showOltSpectrumConfig", I18N.spectrum.oltSpectrumCollectConfig, "icoG7", "/cmcSpectrum/showOltSpectrumConfig.tv");
}

/************************************
        CMTS频谱录像管理页面
*************************************/
function showSpectrumVideoMgmt(){
    window.parent.addView("showSpectrumVideoMgmt", I18N.performance.spectrumVideoMgmt, "icoE14", "/cmcSpectrum/showSpectrumVideoMgmt.tv");
}

/************************************
		频谱噪声告警配置页面
*************************************/
function showSpectrumAlertConfig(){
	 window.parent.addView("showSpectrumAlertConfig", I18N.spectrum.alertConfig, "icoG7", "/cmcSpectrum/showSpectrumAlertConfig.tv");
}

/************************************
		查看业务(统计)监视器
*************************************/
function showStatisticsMonitor(){
	window.parent.addView("StatisticsMonitor", I18N.Performance.StatisticsMonitor, "mydesktopIcon", "/performance/showMonitorDisplayer.tv",null,true);
}

/************************************
OLT全局配置
*************************************/
function showOltGlobalPerfConfig(){
    window.parent.addView("oltGlobalPerfConfig", I18N.Performance.perfOltGlobal, "icoE2", "/epon/perfTarget/showOltGlobalPerfTarget.tv",null,true);
}
/************************************
CC全局配置
*************************************/
function showCCGlobalPerfConfig(){
    window.parent.addView("ccGlobalPerfConfig", I18N.Performance.perfCCGlobal, "icoE5", "/cmc/perfTarget/showCmcGlobalPerfTarget.tv",null,true);
}
/************************************
CMTS全局配置
*************************************/
function showCmtsGlobalPerfConfig(){
    window.parent.addView("cmtsGlobalPerfConfig", I18N.Performance.perfCmtsGlobal, "icoE5", "/cmts/perfTarget/showCmtsGlobalPerfTarget.tv",null,true);
}
/************************************
 * 模板管理
*************************************/
function showPerfTemplate(){
	window.parent.addView("perfTemplate", I18N.Performance.perfTemplateMgmt, "icoE1", "/performance/perfThreshold/showPerfTemplate.tv",null,true);
}

function showOltPerfConfig(){
	window.parent.addView("oltPerfTemp", I18N.Performance.oltPerfMgmt, "icoE3", "/epon/perfThreshold/showOltPerfTemp.tv",null,true);
}
function showCCPerfConfig(){
	window.parent.addView("ccPerfTemp", I18N.Performance.ccPerfMgmt, "icoE7", "/cmc/perfThreshold/showCCPerfTemp.tv",null,true);
}
function showCmtsPerfConfig(){
	window.parent.addView("cmtsPerfTemp", I18N.Performance.cmtsTemplate, "icoE7", "/cmts/perfThreshold/showCmtsPerfTemplate.tv",null,true);
}
/************************************
 * 批量配置
*************************************/
function showBatch(){
	window.parent.addView("batch", I18N.Performance.batchConfig, "icoE12", "/performance/showBatchConfigTarget.tv",null,true);
}
/************************************
 * CCMTS CMTS 性能采集管理
*************************************/
function CcmtsAndCmtsConfig(){
	window.parent.addView("ccmtsAndCmtsConfig", "@COMMON.cmtsCCMTS@", "icoE6", "/cmc/perfTarget/showCmcPerfManage.tv",null,true);
};

/************************************
 * 阈值指标管理
*************************************/
function showTargetManage(){
	window.parent.addView("thresholdTargetManage", I18N.Performance.targetManage, "icoE1", "/performance/perfThreshold/showTargetManage.tv",null,true);
} 

/************************************
 * ONU阈值指标管理
*************************************/
function showOnuPerfParamConfig(){
	window.parent.addView("onuPerfParamsConfig", I18N.Performance.onuPerfParamsConfig, "icoE4", "/onu/onuPerfGraph/showOnuPerfManage.tv",null,true);
}
/************************************
 * ONU性能管理全局配置
*************************************/
function showOnuGlobalPerfConfig(){
	window.parent.addView("onuGlobalConfig", I18N.Performance.onuGlobalConfig, "icoE2", "/onu/onuPerfGraph/showOnuGlobalPerfTarget.tv",null,true);
}
/************************************
 * ONU性能阈值模板管理
*************************************/
function showOnuPerfTempConfig(){
	window.parent.addView("onuPerfTempMgmt", I18N.Performance.onuPerfTempMgmt, "icoE3", "/onu/onuPerfGraph/showOnuPerfTemplate.tv",null,true);
}
