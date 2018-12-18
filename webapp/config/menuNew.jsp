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
<%
boolean eponConfigFileMgmt = uc.hasPower("eponConfigFileMgmt");
boolean loadBalPolicyTpl = uc.hasPower("loadBalPolicyTpl");
boolean globalSpectrumGpMgmtPower = uc.hasPower("globalSpectrumGpMgmt");
boolean spectrumGpTempLateMgmtPower = uc.hasPower("spectrumGpTempLateMgmt");
boolean templateConfigLogPower = uc.hasPower("templateConfigLog");
boolean onuAuthManage = uc.hasPower("onuAuthManage");
boolean onuAuthFailList = uc.hasPower("onuAuthFailList");
boolean gponOnuAutoFindList = uc.hasPower("gponOnuAutoFindList");
%>

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
function showModeList(){
	window.parent.addView("modeList", "@CongfigBackup/Config.apModeParaMgt@", "mydesktopIcon", "/ap/showModeList.tv");
}
function showConfigFileMgmt(){
	window.parent.addView("modeList", "@CongfigBackup/Config.eponConfigFileMgt@", "icoI9", "/configBackup/showConfigBackupView.tv");
}
function showOnuAuthManage(){
	window.parent.addView("onuAuthManage", "@resources/userPower.onuAuthManage@", "icoI9", "/epon/onuAuthManage.jsp");
}
function showOnuAuthFailList(){
	window.parent.addView("onuAuthFail", "@resources/userPower.onuAuthFailList@", "icoI9", "/epon/onuAuthFailList.jsp");
}
function showGponOnuAutoFindList(){
	window.parent.addView("gponOnuAutoFindList", "@resources/userPower.gponOnuAutoFindList@", "icoI9", "/gpon/onuauth/showGponOnuAutoFindList.tv");
}
function showRogueOnuList(){
	window.parent.addView("rogueOnuList", "@resources/userPower.rogueOnuList@", "icoI9", "/onu/rogueonu/showRogueOnuList.tv");
}
function showLoadBalPolicyTpl(){
    window.parent.addView("loadBalPolicyTpl", "@resources/loadBalPolicyTpl@", "icoI8", "/cmc/cmcLoadBalPolicyTplList.jsp");
}

function showTelnetLoginConfig(){
    window.parent.addView("entityPasswordConfig", "@resources/telnet.entityTelnetConfig@", "icoI6", "/entity/telnetLogin/showTelnetLoginList.tv?");
}

function showCommonConfigFileManage(){
	window.parent.addView("configFileManage", "@sendConfig.fileManage@", "icoI7", "/entity/commonConfig/showCommonConfigFileManage.tv?");
}

function showParamConfig(){
	window.parent.addView("paramConfig", "@resources/batchtopo.parameterconfig@", "icoD1", "/entity/commonConfig/showParamConfig.tv?");
}

function showCommandSendEntityList(){
	window.parent.addView("entityList", "@sendConfig.entityList@", "icoI4", "/entity/commandSend/showCommandSendEntityList.tv?");
}

/* global Group Management */
function showGlobalSpectrumGp(){
    window.parent.addView("showGlobalSpectrumGp", "@CongfigBackup/Config.globalGpManage@", "apListIcon", "/ccmtsspectrumgp/showGlobalSpectrumGp.tv");
}

/* Group Template Management */
function showSpectrumGpTempLate(){
    window.parent.addView("gpTemplateManage", "@CongfigBackup/Config.gpTempManage@", "apListIcon", "/ccmtsspectrumgp/showSpectrumGpTempLate.tv");
}

/* Group Template config log */
function showTemplateConfigLog(){
    window.parent.addView("showTemplateConfigLog", "@CongfigBackup/Config.tempConfigLog@", "apListIcon", "/ccmtsspectrumgp/showTemplateConfigLog.tv");
}

function showUpgradeParamConfig(){
	window.parent.addView("paramConfig2", "@resources/batchtopo.parameterconfig@", "icoI2", "/upgrade/showUpgradeParam.tv");
}

function showEntityVersionList(){
	window.parent.addView("entityVersionList", "@batchUpgrade.versionManage@", "icoI3", "/upgrade/showEntityVersionList.tv");
}

function showUpgradeRecord(){
	window.parent.addView("upgradeRecord", "@batchUpgrade.record@", "icoI1", "/upgrade/showUpgradeRecord.tv");
}

function showJob(jobId, name){
	window.parent.addView("upgradeJob" + jobId, name, "topoFolderIcon20", "/upgrade/showUpgradeJob.tv?jobId=" + jobId);
}

function addUpgradeJob(){
    window.parent.createDialog("newJob", "@batchUpgrade.addUpgradeJob@",  800, 500, "/upgrade/showAddJob.tv", null, true, true);
}

function deleteUpgradeJob(){
    window.parent.createDialog("deleteJob", "@batchUpgrade.deleteJob@",  800, 500, "/upgrade/showDeleteJob.tv", null, true, true);
}


$(function(){
	//加载树形菜单;
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
	buildUpgradeJobTree();
	
	if(!operationDevicePower){
		$("#addUpgradeJobBt").hide();
		$("#deleteUpgradeJobBt").hide();
	}
	
})

function buildNode(node){
	return String.format('<li id="{0}" name="{1}" class="jobTreeNode upgrade"><span class="topoFolderIcon20"><a href="javascript:;"  class="linkBtn upgradeA" name="{1}">{1}</a></span></li>'
			, node.jobId, node.name);
}

function buildLastNode(node){
	return String.format('<li id="{0}" name="{1}" class="last upgrade"><span class="topoFolderIcon20"><a href="javascript:;"  class="linkBtn upgradeA" name="{1}">{1}</a></span></li>'
			, node.jobId, node.name);
}
function buildUpgradeJobTree(){
	$.ajax({
		url: '/upgrade/getUpgradeJob.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(json) {
   			var length = json.length;
   			var html = ''
   			for(var i = 0; i < length; i++){
   				var temp = '';
   				if(i == length - 1){
   					temp = buildLastNode(json[i]);
   				}else{
   					temp = buildNode(json[i]);
   				}
   				html += temp;
   			}
   			$("#jobTree").html(html);
   			bindJobTreeClick();
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}
	
function bindJobTreeClick(){
	$(".upgrade").live("click",function(){
		var jobId = $(this).attr("id");
		var name = $(this).attr("name");
		showJob(jobId, name)
	});
	
	$(".upgradeA").hover(function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth();	
		if( w >= 186){
			var txt = $this.text();
			var tPos = $this.offset().top + 100;
			var o = {text:txt,display:"show",top:tPos};
			top.redTip(o);
		}
	},function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth(); 
		if(w >= 186){
			top.redTip({display:"hide"});
		}
	});
}
//升级配置;
function showCmUpgradeConfig(){
	top.createDialog('modalDlg', "@batchUpgrade.config@", 800, 500, '/cmupgrade/showCmUpgradeConfig.tv', null, true, true);
}
//升级统计;
function showCmUpgradeStatisitcs(){
	top.addView("upgradeStatisitcs", "@batchUpgrade.statistics@", "icoG2", "/cmupgrade/showCmUpgradeStatisitcs.tv");
}
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
			<li><span class="icoI9"><a href="javascript:;" class="linkBtn" onclick="showConfigFileMgmt()" name="eponStartConfig">@CongfigBackup/Config.conBackupMgt@</a></span></li>
				<% if(uc.hasSupportModule("olt")){%>
				<%if (onuAuthManage){%>
				<li><span class="icoI9"><a href="javascript:;" class="linkBtn" onclick="showOnuAuthManage()" name="onuAuthManage">@resources/userPower.onuAuthManage@</a></span></li>
				<%}if(onuAuthFailList){%>
				<li><span class="icoI9"><a href="javascript:;" class="linkBtn" onclick="showOnuAuthFailList()" name="onuAuthFail">@resources/userPower.onuAuthFailList@</a></span></li>
				<%}if(gponOnuAutoFindList){%>
				<li><span class="icoI9"><a href="javascript:;" class="linkBtn" onclick="showGponOnuAutoFindList()" name="gponOnuAutoFindList">@resources/userPower.gponOnuAutoFindList@</a></span></li>
				<%}
				}%>
				<li><span class="icoI9"><a href="javascript:;" class="linkBtn" onclick="showRogueOnuList()" name="rogueOnuList">@resources/userPower.rogueOnuList@</a></span></li>
				<% if (uc.hasSupportModule("cmc")) {%>
					<%if (loadBalPolicyTpl){%>
					<li><span class="icoI8"><a id="loadBalPolicyTpl" href="javascript:;" class='linkBtn' onclick="showLoadBalPolicyTpl()">@resources/loadBalPolicyTpl@</a></span></li>
					<%}%>
				<%}%>				
				<li>
			<% if(uc.hasPower("sendConfigFileManage")||uc.hasPower("entityTelnetConfig")||uc.hasPower("sendConfigParameterconfig")||uc.hasPower("sendConfigEntityList")){ %>
			<span class="folder">@sendConfig.batchSendConfig@</span>
			<ul>
				<%if (uc.hasPower("sendConfigFileManage")){%>
				<li>
					<span class="icoI7">
						<a href="javascript:;" class="linkBtn" onclick="showCommonConfigFileManage()" name="configFileManage">
							@sendConfig.fileManage@
						</a>
					</span>
				</li>
				<%}%>
				<%if (uc.hasPower("sendConfigParameterconfig")){%>
				<li>
					<span class="icoD1">
						<a href="javascript:;" class="linkBtn" onclick="showParamConfig()" name="paramConfig">
							@resources/batchtopo.parameterconfig@
						</a>
					</span>
				</li>
				<%}%>
				<%if (uc.hasPower("sendConfigEntityList")){%>
				<li>
					<span class="icoI4">
						<a href="javascript:;" class="linkBtn" onclick="showCommandSendEntityList()" name="entityList">
							@sendConfig.entityList@
						</a>
					</span>
				</li>
				<%}%>
			</ul>
		<%}%>
		</li>
		<li>
		<% if(uc.hasPower("upgradeVersionManage")||uc.hasPower("upgradeParameter")||uc.hasPower("upgradeJob")||uc.hasPower("upgradeRecord")){ %>
			<span class="folder">@batchUpgrade.batchUpgrade@</span>
			<ul>
				<%if (uc.hasPower("upgradeVersionManage")){%>
				<li>
					<span class="icoI3"> 
						<a href="javascript:;" class="linkBtn" onclick="showEntityVersionList()" name="versionManage"> 
						@batchUpgrade.versionManage@</a>
					</span>
				</li>
				<%}%>
				<%if (uc.hasPower("upgradeParameter")){%>
				<li>
					<span class="icoI2"> 
						<a href="javascript:;" class="linkBtn" onclick="showUpgradeParamConfig()" name="upgradeParam">
						@resources/batchtopo.parameterconfig@</a>
					</span>
				</li>
				<%}%>
				<%if (uc.hasPower("upgradeJob")){%>
				<li>
					<span class="folder" onclick="buildUpgradeJobTree()">
						@batchUpgrade.job@
					</span>
					<ul id="jobTree" class="filetree"></ul>
				</li>
				<%}%>
				<%if (uc.hasPower("upgradeRecord")){%>
				<li>
					<span class="icoI1"> 
						<a href="javascript:;" class="linkBtn" onclick="showUpgradeRecord()" name="upgradeRecord">
						@batchUpgrade.record@
						</a>
					</span>
				</li>
				<%}%>
			</ul>
		<%}%>
		</li>
		<% if(uc.hasSupportModule("cmc")){%>
			<li>
				<span class="folder">@batchUpgrade.automatic@</span>
				<ul>
					<li>
						<span class="icoI7">
							<a href="javascript:;" class="linkBtn" onclick="showCmUpgradeConfig()">@batchUpgrade.config@</a>
						</span>
					</li>
					<li>
						<span class="icoG2">
							<a href="javascript:;" class="linkBtn" onclick="showCmUpgradeStatisitcs()">@batchUpgrade.statistics@</a>
						</span>
					</li>
				</ul>
			</li>
		<% } %>	
		</ul>
	</div>
	</div>
	<div id="threeBoxLine"></div>
	<div class="putBtn" id="putBtn">
		<ol class="icoBOl">
			<li id="addUpgradeJobBt"><a href="javascript:;" class="icoC3" name="" onclick="addUpgradeJob()">@batchUpgrade.addUpgradeJob@</a></li>
			<li id="deleteUpgradeJobBt"><a href="javascript:;" class="icoC4" name="" onclick="deleteUpgradeJob()">@batchUpgrade.deleteJob@</a></li>
			<%if (uc.hasPower("entityTelnetConfig")){%>
				<li id="telnetLoginConfig"><a href="javascript:;" class="icoI2" name="" onclick="showTelnetLoginConfig()">@resources/telnet.entityTelnetConfig@</a></li>
			<%}%>	
		</ol>
	</div>
</body>
</Zeta:HTML>