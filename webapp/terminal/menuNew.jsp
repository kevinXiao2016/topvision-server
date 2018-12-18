<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE NETWORK
    CSS css/reset
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css" />
<style type="text/css">
#tree a{ white-space:nowrap; word-break:keep-all;}
#sliderLeftBtn:hover {background: url(../css/white/@resources/COMMON.openIcon@.png) no-repeat;}
#sliderRightBtn:hover {background: url(../css/white/@resources/COMMON.closeIcon@.png) no-repeat;}
.topoFolderIcon20 {background-image: url(../images/network/region.gif) !important;background-repeat: no-repeat;padding: 1px 0 1px 17px;valign: middle;display: block;}
</style>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.wresize.js"></script>
<script type="text/javascript" src="../js/nm3k/menuNewTreeTip.js"></script>
<script type="text/javascript">
var userId = <%= uc.getUser().getUserId() %>;

function refreshFn() {
    window.location.href = window.location.href;
}
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
	o.title = visible ? "@WorkBench.NAVI_EXPANDER_UP@" : "@WorkBench.NAVI_EXPANDER_DOWN@";
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

function showCmList() {
    window.top.addView("cmListNew", '@a.title.cmList@', "tabIcoB6", "/cmlist/showCmListPage.tv");
}

//点击CM导入信息 ;
function showCmImportInfo(){
	window.top.addView("CmImportInfo", '@a.title.cmImportInfo@', "tabIcoB7", "cm/showCmImportList.tv");
}

/************************************
查看CM\CPE综合查询页面
*************************************/
function showCmCpeQuery(){
	window.top.addView("CmCpeQuery", "@a.title.cmCpeQuery@", "icoG13", "/cmCpe/showCmCpeQuery.tv");
}

/************************************
查看终端实时定位
*************************************/
function showTerminalLocate(){
	window.top.addView("CmLocate", "@a.title.cmLocate@" , "icoB13", "/network/showTerminalLocation.tv");
}

function showCmServiceType() {
	window.top.addView("cmServiceType", '@resources/SYSTEM.cmServiceType@', "tabIcoH7", "cm/showCmServiceType.tv");
}

function showHighLevelMonitorCmList() {
	window.top.addView("highLevelMonitorCmList", '@a.title.highLevelMonitorCmList@', "icoI7", "/pnmp/monitor/showHighMonitorCmList.tv");
}

function showCmtsReport() {
	window.top.addView("pnmpCmtsReport", '@a.title.autoMaintenanceReport@', "icoG2", "/pnmp/cmtsreport/showCmtsReportView.tv");
}

function showPnmpTargetThresholdConfig() {
	window.top.addView("pnmpTargetThresholdConfig", '@pnmp/pnmp.targetThresholdConfig@', "icoG2", "/pnmp/target/showPnmpTargetConfig.tv");
}

function showCmSignalTargetThresholdConfig() {
	window.top.addView("cmSignalTargetThresholdConfig", '@pnmp/pnmp.cmTargetConfig@', "icoG2", "/pnmp/cmtarget/showCmTargetConfig.tv");
}

$(function(){
	$("#tree").treeview({ 
		animated: "fast",
		control:"#sliderOuter"
	});	//end treeview;
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
			<li>
				<span class="icoB6"> 
					<a href="javascript:;" class="linkBtn" onclick="showCmList()" name="cmlist">@a.title.cmList@</a>
				</span>
			</li>
			<li>
				<span class="icoB7">
					<a href="javascript:;" class="linkBtn hover" onclick="showCmImportInfo()">@a.title.cmImportInfo@</a>
				</span>
			</li>
			<li>
				<span class="icoG13">
					<a href="javascript:;" class="linkBtn hover" onclick="showCmCpeQuery()">@a.title.cmCpeQuery@</a>
				</span>
			</li>
			<li>
				<span class="icoB13">
					<a href="javascript:;" class="linkBtn" onclick="showTerminalLocate()">@a.title.cmLocate@</a>
				</span>
			</li>
			<li>
				<span class="icoH7">
					<a href="javascript:;" class="linkBtn hover" onclick="showCmServiceType()">@resources/SYSTEM.cmServiceType@</a>
				</span>
			</li>
			<li>
				<span class="folder">@pnmp/pnmp.desc@</span>
				<ul>
					<li>
						<span class="icoI7">
							<a href="javascript:;" class="linkBtn" onclick="showHighLevelMonitorCmList()">@pnmp/pnmp.highLevelCm@</a>
						</span>
					</li>
					<li>
						<span class="icoG2">
							<a href="javascript:;" class="linkBtn" onclick="showCmtsReport()">@pnmp/pnmp.cmtsReport@</a>
						</span>
					</li>
					<li>
						<span class="icoE13">
							<a href="javascript:;" class="linkBtn" onclick="showPnmpTargetThresholdConfig()">@pnmp/pnmp.targetThresholdConfig@</a>
						</span>
					</li>
				</ul>
			</li>
			<li>
				<span class="icoE13">
					<a href="javascript:;" class="linkBtn" onclick="showCmSignalTargetThresholdConfig()">@pnmp/pnmp.cmTargetConfig@</a>
				</span>
			</li>
		</ul>
	</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
	</div>
</body>
</Zeta:HTML>