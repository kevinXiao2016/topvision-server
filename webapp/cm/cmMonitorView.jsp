<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	library highchart
	plugin portlet
	plugin DateTimeField
	module cm
	import js/nm3k/nm3kPickDate
	import cm.util.cmUtil
	import cm.javascript.CmMonitorViewLayout
	import cm.javascript.CmMonitorChartUtil
	import js/jquery/Nm3kTabBtn
</Zeta:Loader>
<style type="text/css">
html,body {height: 100%; overflow:hidden;}
#toolbar {position:absolute;height: 38px;width: 100%; top:0px; left:0px;}
#cmList {position:absolute;top:38px;width: 160px;height: 500px;  border-right:1px solid #D0D0D0; overflow:hidden; left:0;}
#curPerfPanel {position:absolute;top:38px;left:165px;width: 100%;height:230px;}
#historyPerfPanel {position:absolute;top:313px;left:165px;width: 100%;height:200px;overflow:scroll; background:#fff}
.downchannel-img {position:absolute; left : 55px;top:55px;}
.upchannel-img {position:absolute; left : 172px;top:35px;}
.channel-text {position: absolute; top : 0px;left:-50px;width:250px; z-index: 10000;}
#sigNoiseChartPanel { height:229px; overflow:hidden; margin:0px 274px 0px 468px; background:#fff; padding-top:38px; border:1px solid #d0d0d0; border-top:none;}
#sigNoiseChartPanelBody{ width:100%; height:197px; overflow:hidden; position:relative;}
#basicInfoPanel {position: absolute;width: 265px;height:229px; right:4px;top:38px; z-index:2; background:#fff; border:1px solid #d0d0d0; border-top:none; overflow:hidden;}
#basicInfoPanelBody{ width:100%; height:197px;}
#historyToolbar{/* width: 100%;position:absolute;top:268px;left:165px */; margin:0px 4px 0px 163px; border-left:1px solid #d0d0d0; border-right:1px solid #d0d0d0;}
#channelContent{/*  background: #fff; */ width:300px; height:229px; overflow:hidden; position:absolute; border:1px solid #d0d0d0; border-top:none; top:38px; left:163px;}
.x-toolbar{ height:25px;}
#historyPerfPanel .x-panel-body {background: #F0F0F0;}
</style>
<script type="text/javascript">
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
var CHART_MAP = {};
	function showOne(index){
	    if(index == 0){
	    	$(".jsHideDiv").css("display","block");
	    	return;
	    };
	    $(".jsHideDiv").css("display","none");
	    $(".jsHideDiv").eq(index - 1).show();
	}
	
	$(function(){
		
		autoHeight();
		$(window).resize(function(){
			//autoHeight();
			throttle(autoHeight,window);
		});//end resize;

		/* var tab1 = new Nm3kTabBtn({
		    renderTo:"putBtnGroup",
		    callBack:"showOne",
		    tabs:["@cmPoll.all@","@monitor.upChannelRate@","@monitor.upChannelBandWidth@","@monitor.downChannelRate@",
		          "@monitor.downChannelBandWidth@","@monitor.sendPowerHistory@","@monitor.recvPowerHistory@","@monitor.sigErrCodeHistory@","@monitor.sigQUnerroredsHistory@"]
		});
		tab1.init(); */
		    
	});
	//resize事件增加 函数节流;
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	
	
	function autoHeight(){
		var toolBarH = $("#toolbar").outerHeight();
		var h = $(window).height();
		$("#cmList").height(h - toolBarH);
		var bH = h - toolBarH - 230 - 40 - 20;
		$("#historyPerfPanel").height(bH);
		
		var w = $(window).width() - 170;
		if(w < 10) w = 10;
		$("#historyPerfPanel").css("width", w);
	}

	var doc = document,
		perfPanelWidth,
		histroyPanelHeight,
		defaultChannelPanelWidth = 300,
		defaultInfoPanelWidth = 265;
	var startTime,
		endTime,
		mac;
	var quots = ["downChannelFlow","downChannelSpeed","sendPower","receivePower","sigQCorrecteds","sigQMicroreflections",
	             "sigQUnerroreds","snr","upChannelFlow","upChannelSpeed"];
	Ext.onReady(initialize);
	/***** 系统初始化 *******/
	function initialize(){
		var height = doc.body.offsetHeight;
		var basicInfoPanelHeigh = DOC.getElementById("basicInfoPanel").offsetHeight;
		//window.histroyPanelHeight = height-basicInfoPanelHeigh-43;
		
		var toolBarH = $("#toolbar").outerHeight();
		var h = $(window).height();		
		var bH = h - toolBarH - 230 - 40 - 10;
		if(bH < 0){bH = 10}
		window.histroyPanelHeight = bH;
		
		// cm list
		renderCmList();
		//property grid
		window.propertyTable = createInfoTable();
		// channel info
	    propertyTable.render("basicInfoPanelBody");
		
		// dolayout
		showCurErrorRate();
		doLayout();
		
		// 为查询框加载监控任务
		$.ajax({
            url : '/cmpoll/loadCmPollTaskList.tv',
            cache:false,
            dataType:'json',
            data:{},
            success:function(json){
                $("#sel_task").empty().append("<option value=''>@cmpoll.pleaseSelect@</option>");
                $.each(json.data,function(){
                    $("#sel_task").append("<option value='"+this.monitorId+"'>"+this.name+"</option>");
                });
            }
        });
		//toolbar
		bulidToolbar();
		bulidHistoryToolbar();
		
		autoHeight();
		$(window).resize(function(){
			throttle(autoHeight,window)
		});//end resize;

	}
	
	function showCm(g,row,e){
		var curLFetchLock;
		var historyLFetchLock;
		var rec = g.getStore().getAt(row);
		var ip = rec.data.ip;
		window.thisIp = ip;
		window.mac = rec.data.mac;
		var check = function(){
			if(curLFetchLock && historyLFetchLock){
				window.parent.closeWaitingDlg();
			}
		}
		//切换CM的时候清掉之前的数据，因为实时性能的获取可能较慢，导致界面中的数据显示为停留在之前的数据
		showBasicInfo({data:[]});
		showCurErrorRate({data:[]});
		var startTime = Ext.getCmp("startTime").value;
		var endTime = Ext.getCmp("endTime").value;
		/* if( startTime > endTime ){
			return window.parent.showMessageDlg("@COMMON.tip@","@monitor.timeTip@");
		} */
		window.top.showWaitingDlg("wait", "@monitor.loading@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmpoll/loadCmCurStatus.tv',cache:false,dataType:'json',
			data:{cmIp : ip,
				cmMac : mac},
			success:function(json){
				window.parent.closeWaitingDlg();
				showCurPerf(json);
			},error:function(){
				window.parent.closeWaitingDlg();
			}
		});
		var data = {};
		data.cmMac = mac;
		if(startTime){
			data.startTime = startTime;
			data.endTime = endTime;
		}
		data.quots = quots.join(",");
		window.top.showWaitingDlg("wait1", "@monitor.loadingHistory@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmpoll/loadCmHisPerf.tv',cache:false,dataType:'json',
			data: data,
			success:function(json){
				window.parent.closeWaitingDlg();
				Ext.getCmp("startTime").setValue(json.startTime);
				Ext.getCmp("endTime").setValue(json.endTime);
				showHistory(json);
			},error:function(){
				window.parent.closeWaitingDlg();
			}
		});
		doLayout();
	}
	
	
	function doLayout(){
		$("#curPerfPanel").css("width",window.perfPanelWidth);
		//$("#historyPerfPanel").css("width",window.perfPanelWidth+10);
		//$("#historyPerfPanel").css("height",window.histroyPanelHeight);
		var w = $(window).width() - 170;
		if(w < 10) w = 10;
		$("#historyPerfPanel").css("width", w);
		var basicInfoLeft = doc.body.offsetWidth - defaultInfoPanelWidth - 5;
		//$("#basicInfoPanel").css("left",basicInfoLeft);
		window.curErrorRateChart.setWidth(perfPanelWidth - 562);// 570 = 270 + 300
	}
	
	function showCurPerf(json){
		var cmCurStatus = json.data;
		var noErrorCodes = cmCurStatus.docsIfSigQUnerroreds;
		var corectableCodes = cmCurStatus.docsIfSigQCorrecteds;
		var unCorectableCodes = cmCurStatus.docsIfSigQUncorrectables;
		var erroredCodes = corectableCodes + unCorectableCodes;
		var corectableRate =  corectableCodes/erroredCodes;
		var data = {};
		data.columns = [noErrorCodes,erroredCodes,corectableCodes];
		data.pie = corectableRate;
		showCurErrorRate(data);
		showBasicInfo(json);
	}
	
	function showHistory(json,isQuery){
		if( Ext.isArray(json.upChannelFlow)){
			chartUtil.createGeneral("@monitor.upChannelRateStastic@","channelInfo",json.upChannelFlow || [],"@monitor.upChannelRate@","Mbps", false, 0);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("channelInfotable");
				cmp && cmp.hide();
			}
		}
		
		if(  Ext.isArray(json.upChannelSpeed) ){
			chartUtil.createGeneral("@monitor.upChannelBdStatic@","channelBdInfo",json.upChannelSpeed || [],"@monitor.upChannelBandWidth@","Mbps", false);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("channelBdInfotable");
				cmp && cmp.hide();
			}
		}
		
		
		if( Ext.isArray(json.downChannelFlow) ){
			chartUtil.createGeneral("@monitor.downChannelRateStastic@","downchannelInfo",json.downChannelFlow || [],"@monitor.downChannelRate@","Mbps", false, 0);
		} else {
			if(isQuery){
				var cmp = Ext.getCmp("downchannelInfotable");
				cmp && cmp.hide();
			}
		}
		
		if(  Ext.isArray(json.downChannelSpeed) ){
			chartUtil.createGeneral("@monitor.downChannelBdStatic@","downchannelBdInfo", json.downChannelSpeed || [], "@monitor.downChannelBandWidth@","Mbps", false);
		} else {
			if(isQuery){
				var cmp = Ext.getCmp("downchannelBdInfotable");
				cmp && cmp.hide();
			}
		}
		
		
		if( Ext.isArray(json.snr) ){
			chartUtil.createGeneral("@monitor.snrHistory@","snrInfo",json.snr || [],"@monitor.snr@","dB", false);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("snrInfotable");
				cmp && cmp.hide();
			}
		}
		if( Ext.isArray(json.sigQMicroreflections) ){
			chartUtil.createGeneral("@monitor.microRelectHistory@","sigQMicroreflectionsInfo",json.sigQMicroreflections || [], "@monitor.microRelect@", "-dBc", false);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("sigQMicroreflectionsInfotable");
				cmp && cmp.hide();
			}
		}
		if( Ext.isArray(json.sendPower) ){
			chartUtil.createGeneral("@monitor.sendPowerHistory@","sendPowerInfo",json.sendPower || [],"@monitor.sendPower@","@{unitConfigConstant.elecLevelUnit}@", false);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("sendPowerInfotable");
				cmp && cmp.hide();
			}
		}
		if( Ext.isArray(json.sigQCorrecteds) ){
			chartUtil.createGeneral("@monitor.sigErrCodeHistory@","sigQCorrectedsInfo",json.sigQCorrecteds || [],"@monitor.corecError@","%",true, 0);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("sigQCorrectedsInfotable");
				cmp && cmp.hide();
			}
		}
		if( Ext.isArray(json.sigQUnerroreds) ){
			chartUtil.createGeneral("@monitor.sigQUnerroredsHistory@","sigQUnerroredsInfo",json.sigQUnerroreds || [],"@monitor.sigQUnerroreds@","%",true, 0);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("sigQUnerroredsInfotable");
				cmp && cmp.hide();
			}
		}
		
		if( Ext.isArray(json.receivePower) ){
			chartUtil.createGeneral("@monitor.recvPowerHistory@","receivePowerInfo",json.receivePower || [],"@monitor.recvPower@","@{unitConfigConstant.elecLevelUnit}@", false);
		}else{
			if(isQuery){
				var cmp = Ext.getCmp("receivePowerInfotable");
				cmp && cmp.hide();
			}
		}
	}
	
	function showBasicInfo(json){
		var cmCurStatus = json.data;
		var source = {
			"@monitor.microRelect@" : cmCurStatus.docsIfSigQMicroreflectionsForUnit || "-",
			"@monitor.ip@": window.thisIp || "-",
			"@monitor.mac@": window.mac || "-",
			"@monitor.snr@" : cmCurStatus.docsIfSigQSignalNoiseForUnit || "-",
			"@monitor.configFile@" : cmCurStatus.docsDevServerConfigFile || "-",
			"TFTP" : cmCurStatus.docsDevServerTftp || "-",
			"DHCP" : cmCurStatus.docsDevServerDhcp || "-",
			"@monitor.sysUpTime@" : cmCurStatus.sysUpTimeToString || "-"
		};
		propertyTable.setSource(source);
		$("#downRate").html(String.format("@monitor.downChRate@:{0}", cmCurStatus.downIfSpeedForUnit || "-"));
		$("#upRate").html(String.format("@monitor.upChRate@:{0}", cmCurStatus.upIfSpeedForUnit || "-"));
		$("#sendPower").html(String.format("@monitor.sendPower@:{0}", cmCurStatus.docsIfCmStatusTxPowerForUnit || "-"));
		$("#reveicePower").html(String.format("@monitor.recvPower@:{0}", cmCurStatus.docsIfDownChannelPowerForUnit || "-"));
		if(json.upflow){
			$("#upFlow").html(String.format("@monitor.upChFlow@:{0}Mbps", json.upflow || "-"));
		}else{
			$("#upFlow").html("@monitor.upChFlow@:-");
		}
		if(json.downflow){
			$("#downFlow").html(String.format("@monitor.downChFlow@:{0} Mbps", json.downflow || "-"));
		}else{
			$("#downFlow").html("@monitor.downChFlow@:-");
		}
	}
	
	function showCurErrorRate(data){
		if(typeof curErrorRateChart == 'object' && curErrorRateChart != null){
			curErrorRateChart.destroy();
            curErrorRateChart = null;
        }
		window.curErrorRateChart = chartUtil.createCurErrorRate("@monitor.accentErrorCode@","sigNoiseChartPanelBody", data || []);
	}
	
	function query(){
		if(!mac){
			return window.parent.showMessageDlg("@COMMON.tip@", "@monitor.noCm@");
		}
		var startTime = Ext.getCmp("startTime").value;
		var endTime = Ext.getCmp("endTime").value;
		if( startTime > endTime ){
			return window.parent.showMessageDlg("@COMMON.tip@","@monitor.timeTip@");
		} 
		window.top.showWaitingDlg("waiting", "@COMMON.querying@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmpoll/loadCmHisPerf.tv',cache:false,dataType:'json',
			data:{cmMac : mac,
				startTime : startTime,
				endTime : endTime,
				quots : quots.join(",")},
			success:function(json){
				window.parent.closeWaitingDlg();
				showHistory(json,true);
			},error:function(){
				window.parent.closeWaitingDlg();
			}
		});
	}
	
	function autoHeight(){
		var toolBarH = $("#toolbar").outerHeight();
		var h = $(window).height();
		var bH = h - toolBarH - 230 - 40 - 10;
		$("#historyPerfPanel").height(bH);
		
		var newW = $(window).width() - 165;
		$("#historyPerfPanel").width(newW)
		
	};//end autoHeight;
	
	//resize事件增加"函数节流";
	function throttle(method, context){
		clearTimeout(method.tId);
		method.tId = setTimeout(function(){
			method.call(context);
		},100);
	}
	
	
</script>
</head>
<body >
	<div id="toolbar"></div>
	<div id="cmList">
		<p class="pannelTit" id="cmListTit">@cmPoll.cmList@</p>
		<div id="cmListBody"></div>
	</div>
	<div id="channelContent" class="arrUpAndDown">
		<p class="pannelTit">@monitor.channelStastic@</p>
		<div class="downchannel-img">
			<!-- <img alt="@monitor.downchannel@" src="/cm/images/downchannel.png" height="135" style="position:absolute;top:33px;left:-15px;" /> -->
			<div class="channel-text" style="top:-15px">
				<p id="downFlow">@monitor.downChFlow@: -</p>
				<p id="downRate">@monitor.downChRate@:-</p>
				<p id="reveicePower">@monitor.recvPower@:-</p>
			</div>
		</div>
		<div class="upchannel-img">
			<!-- <img alt="@monitor.upchannel@" src="/cm/images/upchannel.png" height="135" style="position:absolute;top:0px;left:15px;" /> -->
			<div class="channel-text" style="top:133px;left:-55px;">
				<p id="upFlow">@monitor.upChFlow@:-</p>
				<p id="upRate">@monitor.upChRate@: -</p> 
				<p id="sendPower">@monitor.sendPower@: -</p>
			</div>
		</div>
	
	</div>
	<div class="curPerfPanel" id="curPerfPanel"></div>
	<div id="sigNoiseChartPanel" >
		<p class="pannelTit">@monitor.accentErrorCode@</p>
		<div id="sigNoiseChartPanelBody"></div>
	</div>
	<div id="basicInfoPanel">
		<p class="pannelTit" id="cmListTit">@monitor.cmBasicInfo@</p>
		<div id="basicInfoPanelBody"></div>
	</div>
	<div id="historyToolbar"></div>
	<div id="historyPerfPanel" style="overflow:auto;">
		<!-- 如果包含有什么指标，则画什么指标的图 -->
		<!-- <div id="putBtnGroup"></div> -->
		<!-- <div class="clearBoth"></div> -->
		<div id="channelInfo" class="jsHideDiv"></div>
		<div id="channelBdInfo" class="jsHideDiv"></div>
		<div id="downchannelInfo" class="jsHideDiv"></div>
		<div id="downchannelBdInfo" class="jsHideDiv"></div>
		<div id="snrInfo" class="jsHideDiv"></div>
		<div id="sigQMicroreflectionsInfo" class="jsHideDiv"></div>
		<div id="sendPowerInfo" class="jsHideDiv"></div>
		<div id="receivePowerInfo" class="jsHideDiv"></div>
		<div id="sigQCorrectedsInfo" class="jsHideDiv"></div>
		<div id="sigQUnerroredsInfo" class="jsHideDiv"></div>
	</div>
</body>
</Zeta:HTML>