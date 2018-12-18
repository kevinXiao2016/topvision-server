<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module performance
    import js/jquery/jquery.wresize
</Zeta:Loader>
<style>

</style>
<script>
$(function(){
	
});


function openOltPerfCollect(){
	window.top.showWaitingDlg("@COMMON.wait@", "正在开启OLT性能采集...", 'ext-mb-waiting');
	$.ajax({
		url: '/epon/perfTarget/saveOltPerfCollect.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(result) {
   			top.closeWaitingDlg();
   			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '<p><b class="orangeTxt">开启OLT性能采集成功!</b></p>'
			});
   			doRefresh();
   		}, error: function(result) {
   			top.closeWaitingDlg();
   			window.parent.showMessageDlg("@COMMON.tip@", "开启OLT性能采集失败!");
		}, 
		cache: false
	});	
}

function openCmcPerfCollect(){
	window.top.showWaitingDlg("@COMMON.wait@", "正在开启CMC性能采集...", 'ext-mb-waiting');
	$.ajax({
		url: '/cmc/perfTarget/saveCmcPerfCollect.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(result) {
   			top.closeWaitingDlg();
   			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '<p><b class="orangeTxt">开启CMC性能采集成功!</b></p>'
			});
   			doRefresh();
   		}, error: function(result) {
   			top.closeWaitingDlg();
   			window.parent.showMessageDlg("@COMMON.tip@", "开启CMC性能采集失败!");
		}, 
		cache: false
	});	
}

function openCmtsPerfCollect(){
	window.top.showWaitingDlg("@COMMON.wait@", "正在开启CMTS性能采集...", 'ext-mb-waiting');
	$.ajax({
		url: '/cmts/perfTarget/saveCmtsPerfCollect.tv',
    	type: 'POST',
    	dataType:"json",
   		success: function(result) {
   			top.closeWaitingDlg();
   			top.afterSaveOrDelete({
				title : "@COMMON.tip@",
				html : '<p><b class="orangeTxt">开启CMTS性能采集成功!</b></p>'
			});
   			doRefresh();
   		}, error: function(result) {
   			top.closeWaitingDlg();
   			window.parent.showMessageDlg("@COMMON.tip@", "开启CMTS性能采集失败!");
		}, 
		cache: false
	});	
}

</script>
</head>
<body class="openWinBody">
	<div class="wrapper">
		<div class="edge10">
			<h1>性能采集开启,专用于设备性能采集开启(操作请谨慎)</h1>
		</div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 noWidthCenter">
		     	 <li><a href="javascript:;" class="normalBtnBig" onclick="openOltPerfCollect()"><span><i class="miniIcoSaveOK"></i>OLT性能采集开启</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="openCmcPerfCollect()"><span><i class="miniIcoSaveOK"></i>CMC性能采集开启</span></a></li>
		         <li><a href="javascript:;" class="normalBtnBig" onclick="openCmtsPerfCollect()"><span><i class="miniIcoSaveOK"></i>CMTS性能采集开启</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>