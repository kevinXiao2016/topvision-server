<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.ems.cmc.util.CmcConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library Highchart
    PLUGIN Portlet
    PLUGIN Highchart-Ext
    import js.tools.ipText
    module cmc
    import js/nm3k/Nm3kClock
    import js/tools/imgTool
    css css/white/disabledStyle
    import js/tools/authTool
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<!-- 内置css定义 -->
<style type="text/css">
#entityIcon{ padding-top: 80px;}
#portletTools .x-panel-body,#configFileMgmt .x-panel-mc,#tools {
    background: #fff;
}

.mydiv {
    padding: 5px 10px 5px 10px;
}

.mydiv div {
    float: left;
    margin: 3px 10px 3px 0px;
}
</style>
<script type="text/javascript"
    src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<!-- 内置自定义js -->
<script type="text/javascript">
/*******************************************************
 * 变量定义及其初始化
 *******************************************************/
vcEntityKey = 'cmcId';
var cmcId = ${ cmcId };
var cmcType = ${ cmcType };
var formatedMac = '${formatedMac}';
var isSurportedGoogleMap = '${ isSurportedGoogleMap }';
var nodePath = '${ nodePath }';
var macAddr = '${ cmcAttribute.topCcmtsSysMacAddr }';
var $nmName = '${ cmcAttribute.nmName }';
var entityName = '${ entity.name }';
var entityIp = '${ entity.ip }';
var upDeviceId = '${ entity.entityId }';
var cmcAttribute = ${cmcAttrJson};
var supportFuncs = ${supportFuncs};
var chartParam = ${chartParam};
var TotalBandWidthchartParam = chartParam;//${TotalBandWidthchartParam};
var viewerParam = ${viewerParam};
var entityId = '${ cmcId }';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var cmcPerfPower = <%=uc.hasPower("cmcPerfParamConfig")%>;
var dispatchInterval = 15000;
var timer = null;
var attention = '${cmcAttribute.attention}';
/*
 * 记录时间控件的对象;
 * type类型：cir,grayCalendar,whiteCalendar;
 */
var oRunTime = {
	totalTime : Number( ${ sysUpTime } ),
	clock : null,//记录时间控件;
	language : '@COMMON.language@',
 	type : 'grayCalendar'
 }
 $(function(){
	//设备重启，置灰时间控件;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		if(top.isClearAlert(data)){
            setTimeout(function(){
                setRfModuleStateOnline(data);
                syTime();
            }, 5000);
        }else{
            setRfModuleStateOffline(data);
            trapDisableClock(data);
        }
	});
	
	//CMC复位，基本信息中射频状态置为离线, 置灰时间控件;
    var cmc_reset = top.PubSub.on(top.CmcTrapTypeId.CMC_RESET, function(data){
    	if(top.isClearAlert(data)){
            setTimeout(function(){
                setRfModuleStateOnline(data);
                syTime();
            }, 5000);
        }else{
            setRfModuleStateOffline(data);
            trapDisableClock(data);
        }
    });
    
    //系统手动重启，基本信息中射频状态置为离线, 置灰时间控件;
    var dol_reboot = top.PubSub.on(top.CmcTrapTypeId.DOL_REBOOT, function(data){
    	if(top.isClearAlert(data)){
            setTimeout(function(){
                setRfModuleStateOnline(data);
                syTime();
            }, 5000);
        }else{
            setRfModuleStateOffline(data);
            trapDisableClock(data);
        }
    });
    
    //CMTS断电，store重载;
    var cmts_power_off = top.PubSub.on(top.CmcTrapTypeId.CMTS_POWER_OFF, function(data){
    	if(top.isClearAlert(data)){
            setTimeout(function(){
                setRfModuleStateOnline(data);
                syTime();
            }, 5000);
        }else{
       	    setRfModuleStateOffline(data);
            trapDisableClock(data);
        }
    });
    
    //CMTS断纤，刷新页面;
    var cmts_link_lose = top.PubSub.on(top.CmcTrapTypeId.CMTS_LINK_LOSE, function(data){
    	if(top.isClearAlert(data)){
    		setTimeout(function(){
	    		setRfModuleStateOnline(data);
	    		syTime();
    		}, 5000);
    	}else{
    		setRfModuleStateOffline(data);
        	trapDisableClock(data);
    	}
    });
    
    //CMTS下线，刷新页面;
    var cmc_offline = top.PubSub.on(top.CmcTrapTypeId.CMTS_OFFLINE, function(data){
    	if(top.isClearAlert(data)){
        	setTimeout(function(){
    			setRfModuleStateOnline(data);
    			syTime();
    		}, 5000);
    	}else{
    		setRfModuleStateOffline(data);
        	trapDisableClock(data);
    	}
    });
    
    //CMTS上线;
    var cmc_online = top.PubSub.on(top.CmcTrapTypeId.CMTS_ONLINE, function(data){
    	setRfModuleStateOnline(data);
    	syTime();
    });
	
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
		
		top.PubSub.off(top.CmcTrapTypeId.CMC_RESET, cmc_reset);
		top.PubSub.off(top.CmcTrapTypeId.DOL_REBOOT, dol_reboot);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_POWER_OFF, cmts_power_off);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_LINK_LOSE, cmts_link_lose);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_OFFLINE, cmc_offline);
		top.PubSub.off(top.CmcTrapTypeId.CMTS_ONLINE, cmc_online);
	});
 });
 
function setRfModuleStateOffline(data) {
	var ip = data.ip || data.host;
    if(!data || !ip || !window.entityIp || window.entityIp != ip || !oRunTime || !oRunTime.clock || oRunTime.clock == null){
        return;
    }
    $('#rfModuleStatus-with-agent').text('@CCMTS.status.2@');
    $('#rfModuleStatus-without-agent').text('@CCMTS.status.2@');
}
function setRfModuleStateOnline(data) {
	var ip = data.ip || data.host;
    if(!data || !ip || !window.entityIp || window.entityIp != ip || !oRunTime || !oRunTime.clock || oRunTime.clock == null){
        return;
    }
    $('#rfModuleStatus-with-agent').text('@CCMTS.status.4@');
    $('#rfModuleStatus-without-agent').text('@CCMTS.status.4@');
}

function trapDisableClock(data){
	var ip = data.ip || data.host;
	if(!data || !ip || !window.entityIp || window.entityIp != ip || !oRunTime || !oRunTime.clock || oRunTime.clock == null){
		return;
	}
	oRunTime.clock.update({startTime:-1}); 
}

 
function startTimer() {
    if (timer == null) {
        timer = setInterval("doRefresh()", dispatchInterval);
    }
}
function doRefresh() {
    refreshCmInfo();
}
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});
var width = Ext.getBody().getWidth();
/*******************************************************
 * 执行语句包括onReady/onload的执行语句，其后为onReady的方法定义
 *******************************************************/
 Ext.BLANK_IMAGE_URL = '/images/s.gif';
function viewOltSnap(entityId, entityName) {
	window.parent.addView('entity-' + entityId, entityName, 'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + parseInt(entityId));
}

 Ext.onReady(function(){
	 oRunTime.clock = new Nm3kClock({
		renderTo: 'runTime',//要渲染到哪个div的id;
		startTime: oRunTime.totalTime,
		type: oRunTime.type,
		language: oRunTime.language
	});
	oRunTime.clock.init();
	
	
	
     Ext.QuickTips.init();
     
     //界面portal
     var portletItems = [];
     //基本信息;
     var portletDetail = { id:'portletDetail', title: I18N.SYSTEM.baseInfo, bodyStyle:'padding:8px', autoScroll:true, contentEl:'detail'};
     
     if(EntityType.isCcmtsWithoutAgentType(cmcType)){
    	 $("#entityName").html('<a href="#" class=my-link onclick=\'viewOltSnap("' + upDeviceId + '","' + entityName + '");\'>' + entityName + '</a>');
    	 $("#entityIp").html('<a href="#" class=my-link onclick=\'viewOltSnap("' + upDeviceId + '","' + entityName + '");\'>' + entityIp + '</a>'); 
     } 
     
     //连续运行时间;
     var portletSysUpTime = {id:'portletSysUpTime', title: I18N.label.upTime, bodyStyle: 'padding:8px', autoScroll: true, 
         contentEl:'runTime'
     };
     //关联cm信息;
     var portletCmInfo = {id:'portletCmInfo', title: I18N.CCMTS.relaCMInfo, bodyStyle: 'padding:8px', autoScroll: true, contentEl:'cmInfo'};
     
     //上行端口信息
     var upChannelInfo = new Ext.ux.Portlet({
         id:'upChannelInfo',
         tools: [{id: 'refresh',
             handler: function(event, toolEl, panel) {
                upChannelInfoStore.reload();
             }
         }],
         title: I18N.CCMTS.upStreamPortInfo,
         items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'upChannelInfoGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse, 
                trackMouseOver: trackMouseOver, 
                border: true,
                cm: new Ext.grid.ColumnModel(upChannelInfoColumns),
                store: upChannelInfoStore,
                viewConfig: {
                    forceFit: true
                }
            })
         }]
     });
     upChannelInfoStore.setDefaultSort('channelIndex', 'ASC');
     upChannelInfoStore.load();
     //下行端口信息
     var downChannelInfo = new Ext.ux.Portlet({
         id:'downChannelInfo',
         tools: [{id: 'refresh',
             handler: function(event, toolEl, panel) {
                downChannelInfoStore.reload();
             }
         }],
         title: I18N.CCMTS.downStreamPortInfo,
         items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'downChannelInfoGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse, 
                trackMouseOver: trackMouseOver, 
                border: true,
                cm: new Ext.grid.ColumnModel(downChannelInfoColumns),
                store: downChannelInfoStore,
                viewConfig: {
                    forceFit: true
                }
            })
         }]
     });
     downChannelInfoStore.setDefaultSort('channelIndex', 'ASC');
     downChannelInfoStore.load();
     //管理工具;
     var portletTools = { id:'portletTools', title: I18N.NETWORK.managementTools, bodyStyle:'padding:0px', autoScroll:true, contentEl:'tools'};
     
     autoFunctionControl(supportFuncs);
     if(!EntityType.isCcmtsWithAgentType(cmcType)){
    	 $(".autoClearCm").css("display", "none");
     }
     //配置文件管理;
     var configFileMgmt = {id:'configFileMgmt', title: I18N.CMC.title.configFileMgmt, bodyStyle:'padding:0px', autoScroll:true,contentEl:'configMgmtTools'}
     //ccmts端口统计;
     var cmcPortInfo = new Ext.ux.Portlet({
         id:'cmcPortInfo',
         tools: [{id: 'refresh',
             handler: function(event, toolEl, panel) {
                portInfoStore.reload();
             }
         }],
         
         title: I18N.CCMTS.portStatics,
         items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'cmcPortInfoGrid',              
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse, 
                trackMouseOver: trackMouseOver, 
                border: true,
                cm: new Ext.grid.ColumnModel(portInfoColumns),
                store: portInfoStore,
                viewConfig: {
                    forceFit: true
                }
            })
         }]
     });
     portInfoStore.load();
    //上行端口用户数统计
     var upChannelUserNum = new Ext.ux.Portlet({
         id:'upChannelUserNum',
         title: I18N.CCMTS.upStreamPortUserCount,
         tools: [{id: 'refresh',
             handler: function(event, toolEl, panel) {
                upChannelUserNumStore.reload();
             }
         }],
         items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'upChannelUserNumGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse, 
                trackMouseOver: trackMouseOver, 
                border: false,
                cm: new Ext.grid.ColumnModel(portUpCMNumColumns),
                store: upChannelUserNumStore,
                viewConfig: {
                    forceFit: true
                }
            })
         }]
     });
     upChannelUserNumStore.setDefaultSort('channelIndex', 'ASC');
     upChannelUserNumStore.load();
     //下行端口用户数统计
     var downChannelUserNum = new Ext.ux.Portlet({
         id:'downChannelUserNum',
         tools: [{id: 'refresh',
             handler: function(event, toolEl, panel) {
                downChannelUserNumStore.reload();
             }
         }],
         title: I18N.CCMTS.downStreamPortUserCount,        
         items: [{
            layout: 'fit',
            items: new Ext.grid.GridPanel({
                id: 'downChannelUserNumGrid',
                cls:'normalTable edge10',
                autoHeight: true,
                autoWidth: true,
                animCollapse: animCollapse, 
                trackMouseOver: trackMouseOver, 
                border: true,
                cm: new Ext.grid.ColumnModel(portDownCMNumColumns),
                store: downChannelUserNumStore,
                viewConfig: {
                    forceFit: true
                }
            })
         }]
     });
     downChannelUserNumStore.setDefaultSort('channelIndex', 'ASC');
     downChannelUserNumStore.load();
     //////////////////////////////////////////////////////////////布局必须从数据库中读取，因此提前分好左右2侧;
     //左侧板块，从数据库读取;
     var leftPartStr = '${leftPartItems}';
     //右侧板块，从数据库读取;
     var rightPartStr = '${rightPartItems}';
     //如果是第一次加载,就进行默认初始化
     if(leftPartStr == '' && rightPartStr == ''){
         leftPartStr = "portletDetail,portletSysUpTime,portletCmInfo,upChannelInfo,downChannelInfo";
		 if(EntityType.isCcmtsWithAgentType(cmcType)){//8800B
             rightPartStr = "portletTools,configFileMgmt,cmcPortInfo,upChannelUserNum,downChannelUserNum";
         }else{//8800A
             rightPartStr = "portletTools,cmcPortInfo,upChannelUserNum,downChannelUserNum";
         }
     }
     var leftPart = {};
     leftPart.columnWidth = 0.5;
     leftPart.style = "padding:10px 5px 10px 10px";
     leftPart.items = [];
     if(leftPartStr != ''){
         var tempArr = leftPartStr.split(",");
         for(var i=0; i<tempArr.length; i++){
             leftPart.items.push(eval(tempArr[i]));
         }
     }
     portletItems[0] = leftPart;
     //右侧板块;
     var rightPart = {};
     rightPart.columnWidth = 0.5;
     rightPart.style = "padding:10px 10px 10px 5px";
     rightPart.items = [];
     //开始添加板块;
     if(rightPartStr != ''){
         var tempArr = rightPartStr.split(",");
         for(var i=0; i<tempArr.length; i++){
             rightPart.items.push(eval(tempArr[i]));
         }
     }
     
     //结束添加板块;
     portletItems[1] = rightPart;
     
    //通过判断左右两侧id的顺序，判断布局是否改变，如果改变，那么保存布局(要求id和变量名一致);
    $(".x-panel-tl").live("mouseup",function(){
         setTimeout(saveLayout,200);
    });//end live; 
    function saveLayout(){
        var leftArr = [];
        var rightArr = [];
        $(".x-portal-column").eq(0).find(".x-portlet").each(function(){
            leftArr.push($(this).attr("id"));
        })
        $(".x-portal-column").eq(1).find(".x-portlet").each(function(){
            rightArr.push($(this).attr("id"));
        })
        
        if(leftPartStr == leftArr.toString() && rightPartStr == rightArr.toString()){//没有变化;
            //没有变化
        }else{
            //布局已经改变，我已经保存了新的布局
            leftPartStr = leftArr.toString();
            rightPartStr = rightArr.toString();
            $.ajax({
                url: '/cmc/savePortalView.tv',
                cache:false, 
                method:'post',
                data: {
                    cmcType : cmcType,
                    leftPartItems : leftPartStr, 
                    rightPartItems : rightPartStr
                },
                success: function() {
                },
                error: function(){
                }
            });
        };//end if else;
    }
     
     //设备总带宽(目前并没有使用);
     panel = new Ext.ux.Portlet({
        id:'totalBandWidthShow',
        title: I18N.Entity.totalWidth,
        items:[{
            items:[{
                    id:'totalBandWidth',
                    bodyStyle: 'padding:8px;height:300',
                    xtype: 'highchartpaneljson',
                    titleCollapse: true,
                    layout:'fit',
                    border: true,
                    chartConfig: chartParam //TotalBandWidthchartParam
                },{
                    id:"detailPanel1",
                    html:"teeset"
                }]
        }]
     });
    var panel = null;
    if (isSurportedGoogleMap == 'false') {
        $("#googleMap").attr("disabled", true);
    }
     var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});
     var viewport = new Ext.Viewport({layout: 'border',
         listeners:{
             statesave :function(){
                 alert("save")
             } ,
             statechange :function(){
                 alert("render")
             }
         },
         items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
     });
     try {
         seriesCpuAndMemRead();
     } catch(ex) {
         //alert(ex);
     }
     //loadCmcOpReceiverInfo();
     startTimer();
     //$("#entityIcon").attr("src",'${ cmcIcon }');     
     var newImgSrc = relaceImgSrc('${ cmcIcon }');
	 loadImage(newImgSrc, function(){
		 $('.rightTopPutPic').html(this);
	 });
     
     if(EntityType.isCcmtsWithAgentType(cmcType)){
    	 $(".ccWithAgent").css("display",'');   
    	 $(".ccWithoutAgent").css("display",'none');   
     }else{
    	 $(".ccWithoutAgent").css("display",'');     
    	 $(".ccWithAgent").css("display",'none');     
     }
     if(attention){
     	$("#userAttention").hide();
     	$("#userAttention").parent().hide();
     }else{
     	$("#userAttentionCancel").hide();
     }
});
 
$(function() {
	//version-control-id
    //autoFunctionControl(supportFuncs);
});
 
 
function autoFunctionControl(funcs){
    for(var param in funcs){
        if(funcs[param]){
            $("." + param).css("display", "block");
        }else{
            $("." + param).css("display", "none");
        }
    }
}

function onAttentionClick(){
	if(attention){//如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : cmcId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cancelFocusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.cancelFocusSucccess@</b>'
	   			});
				attention = false;
				$("#userAttention").show();
				$("#userAttention").parent().show();
				$("#userAttentionCancel").hide();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.cancelFocusFail@")
			}
		})
	}else{//如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : cmcId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.focusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.focusSucccess@</b>'
	   			});
				attention = true;
				$("#userAttention").hide();
				$("#userAttention").parent().hide();
			    $("#userAttentionCancel").show();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.focusFail@")
			}
		})
	}
}

function refreshCmInfo() {
    $.ajax({
        url: '/cmc/refreshCmInfo.tv?cmcId=' + cmcId + '&c=' + Math.random(),
        type: 'POST',
        dataType:'json',
        success: function(cmcCmNumStatic) {
            $("#cmNumTotal").text(cmcCmNumStatic.cmNumTotal);
            $("#cmNumOnline").text(cmcCmNumStatic.cmNumOnline);
            $("#cmNumActive").text(cmcCmNumStatic.cmNumActive);
            $("#cmNumOffline").text(cmcCmNumStatic.cmNumOffline);
            $("#cmNumRregistered").text(cmcCmNumStatic.cmNumRregistered);
            $("#cmNumUnregistered").text(cmcCmNumStatic.cmNumUnregistered);
        }
    });
}

function seriesCpuAndMemRead() {
    
    $.ajax({
        url: '/cmcperf/seriesCpuAndMemRead.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:viewerParam,
        success: function(series) {
            try {
                var chartpanel = Ext.getCmp('cpuAndMemGraph');
                $.each(series, function(i, n) {
                    chartpanel.chart.addSeries(n, false);
                    var html = "<tr> <td style='COLOR: " + n.color + "'><strong>" + n.name + "</strong></td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.max + n.max + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.avg + n.avg + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.min + n.min + n.unit + "</td> <td style='COLOR: " + n.color + "'>"+I18N.ap.chart.cur + n.cur + n.unit + "</td> </tr>"
                    $("#seriesDetail").append(html);
                });
                chartpanel.hideLoading();
                chartpanel.chart.redraw();
            } catch(ex) {
                //alert("here "+ex);
            }
        }
    });
}

function showCmDetail(entityId, cmcId, channelType, channelId) {
    var queryInitData = {
   		cmcDeviceStyle : cmcType,
   		portal :'ccmts', 
   		entityId : entityId,
   		cmcId : cmcId,
   		channelId : channelId,
   		channelType : channelType
    };
    window.top.addView("cmListNew", I18N.CM.cmList, "apListIcon", encodeURI("/cmlist/showCmListPage.tv?queryInitDataStr="+JSON.stringify(queryInitData)), null, true);
}

function showUpChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    addView("channelPortal-"+cmcId + "-"+channelIndex, $nmName + "-"+cmcPortName , "apListIcon", "cmc/showUpChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}

function showDownChannelInfo(portId,cmcId,cmcPortName,channelIndex) {
    addView("channelPortal-"+cmcId + "-"+channelIndex, $nmName + "-"+cmcPortName , "apListIcon", "cmc/showDownChannelPortal.tv?cmcPortId="+portId+"&channelIndex=" + channelIndex+"&cmcId="+cmcId+"&portName="+cmcPortName);
}
//noise 
function showUpChannelNoiseInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "-noise", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=noise&index="+channelIndex); 
}
//usSpeed
function showUpChannelSpeedInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showUpChannelSpeedInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=usSpeed&index="+channelIndex); 
}
//dsSpeed
function showDownChannelSpeedInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showDownChannelSpeedInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=dsSpeed&index="+channelIndex); 
}
//channelUtilization
function showChannelUtilizationInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelUtilizationInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=channelUtilization&index="+channelIndex); 
}
//cmNumActive
function showChannelActiveCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelActiveCmNumInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumActive&index="+channelIndex); 
}
//cmNumActive
function showChannelAllCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelTotalCmNumInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumTotal&index="+channelIndex); 
}
//cmNumActive
function showChannelOnlineCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelOnlineCmNumInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumOnline&index="+channelIndex); 
}
//cmNumRegistered
function showChannelRregisteredCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelRregisteredCmNumInfo", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumRegistered&index="+channelIndex); 
}
//cmNumOffline
function showChannelOfflineCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "-cmNumOffline", I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumOffline&index="+channelIndex); 
}
//cmNumUnregistered
function showChannelUnregisteredCmNumInfo(channelIndex){
    addView("historyShow-"+cmcId + "-"+channelIndex + "showChannelUnregisteredCmNumInfo" , I18N.NETWORK.hisPerf, "historyIcon", "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=cmNumUnregistered&index="+channelIndex); 
}
//Optical Reveiver  Added by huangdongsheng 增加光机功能历史性能
function showCmcOpticalReveiverHis(index){
    addView("historyShow-"+cmcId + "-noise", I18N.NETWORK.hisPerf, "historyIcon", 
            "cmcperf/showCmcHisPerf.tv?cmcId=" + cmcId + "&timeType=Today&perfType=opticalReceiver&index=" + index); 
}
//性能管理
function ccmtsPerfManage(){
    //addView('perfConfig-'+cmcId, I18N.CCMTS.ccmtsPerformCollectConfig+'['+$nmName+']',"bmenu_preference",'/cmcperf/showModifyCmcPerfTargetJsp.tv?cmcId=' + cmcId);
	window.parent.addView("cmcPerfParamConfig", "@performance/Performance.cmcPerfParamConfig@", "icoE6", "/cmc/perfTarget/showCmcPerfManage.tv?entityId=" + cmcId, null, true);
}
//cm升级文件管理
function showCmUpgradeFile(){
	window.top.createDialog('modalDlg', "@CM.UpgradeFileMan@", 800, 500, '/cmupgrade/showCcmtsFile.tv?entityId=' + cmcId, null, true, true);
}
//自动清除离线cm配置
function onclearTimeConfigClick(){
    window.top.createDialog('modalDlg', "@resources/COMMON.clearOfflineCm@", 600, 370, '/cmc/showAutoClearOfflineCm.tv?cmcId=' + cmcId, null, true, true);
}

/*******************************************************
 * 方法定义：操作，交互，菜单等归类组织在一起
 *******************************************************/
//设备重启
function resetCmc() {
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CCMTS.confirmResetCcmts, function(type) {
        if (type == 'no') {
            return;
        }
        $.ajax({
            url:'cmc/resetCmc.tv?cmcId='+ cmcId,
            type:'POST',
            dateType:'json',
            success:function(response){
                //response = eval("(" + response + ")");
                if(response.message == "success"){
                    //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsSuccess);
                	top.afterSaveOrDelete({
           				title: '@COMMON.tip@',
           				html: '<b class="orangeTxt">' + I18N.CCMTS.resetCcmtsSuccess + '</b>'
           			});
                }else{
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsFail);
                }
            },
            error:function(){
            },
            cache:false
        });
    });
}

//授权管理
function authorizationMgmt(){
    window.top.createDialog('authMgmt', I18N.CCMTS.authorizationMgmt, "small_4_3", 200, 
        '/cmcauth/showCmcAuthMgmtConfig.tv?cmcId=' + cmcId, null, true, true);
}

//基本信息管理
function ccmtsBasicInfoMgmt(){
    window.top.createDialog('basicInfoMgmt', I18N.CCMTS.ccmtsBasicInfoMgmt, 600, 370,
            '/cmc/showCmcBasicInfoConfig.tv?cmcId=' + cmcId, null, true, true);
}
//保存配置 
function saveConfig(){
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CMC.tip.confirmSaveConfig, function(type) {
        if (type == 'no') {
            return;
        }
        window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.tip.savingConfig, 'ext-mb-waiting');
        $.ajax({
            url:'cmc/saveConfig.tv?cmcId='+ cmcId,
            type:'POST',
            dateType:'json',
            success:function(response){
                //response = eval("(" + response + ")");
                if(response.message == "success"){
                    //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.saveConfigSuccess );
                    window.parent.closeWaitingDlg();
                    top.afterSaveOrDelete({
           				title: I18N.COMMON.tip,
           				html: '<b class="orangeTxt">' + I18N.CMC.tip.saveConfigSuccess + '</b>'
           			});
                }else{
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.saveConfigFail);
                }
            },
            error:function(){
            },
            cache:false
        });
    });
}
function sendRequest(url, method, param, sn, fn) {
    Ext.Ajax.request({url: url+'?_='+Math.random(), failure: fn, success: sn,timeout:1800000,  params: param});
}
//重新发现CC8800B/D/C-B added by huangdongsheng 
function onDiscoveryAgainClick() {
	window.top.discoveryEntityAgain(entityId, entityIp, function() {
		if(top.getFrame("entity-"+cmcId)!=null){
            top.contentPanel.setActiveTab("entity-"+cmcId);
            window.location.reload();
        }
		top.afterSaveOrDelete({
			title: '@COMMON.tip@',
			html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
		});
	});
}
//刷新通过OLT设备管理的CC设备
function refreshCcmtsOnOLt(){
    $.ajax({
        url:'cmc/refreshCC.tv?cmcId='+ cmcId+'&cmcType='+cmcType+'&entityId='+entityId,
        type:'POST',
        dateType:'json',
        success:function(response){
            if(response == "success"){
                //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCcmtsSuccess);
                top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
   				});
                //window.parent.onRefreshClick();
                if(top.getFrame("entity-"+cmcId)!=null){
                    top.contentPanel.setActiveTab("entity-"+cmcId);
                    window.location.reload();
                }
            }else{
            	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
            } 
        },
        error:function(){
        	window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.reTopoEr@");
        },
        cache:false
    });
}

//刷新CCMTS 
function refreshCcmts(){
	window.parent.showWaitingDlg("@COMMON.waiting@", "@CCMTS.reTopoingCcmts@",'waitingMsg','ext-mb-waiting');
	if(EntityType.isCcmtsWithAgentType(cmcType)){//8800B
		onDiscoveryAgainClick();
	}else{//8800A
		refreshCcmtsOnOLt();
	}
}
//刷新Cm列表
function refreshCmList(){
    window.parent.showConfirmDlg("@COMMON.tip@", "@CCMTS.confirmRefreshCmList@", function(button,text){
        if(button == "yes"){
            window.parent.showWaitingDlg("@COMMON.waiting@", "@CCMTS.refreshCmList@",'waitingMsg','ext-mb-waiting');
            $.ajax({
                url:'/cmlist/refreshCmList.tv?cmcId='+ cmcId+'&entityId='+upDeviceId,
                type:'POST',
                dateType:'json',
                success:function(response){
                    if(response == "success"){
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCcmtsSuccess);
                        top.afterSaveOrDelete({
                            title: '@COMMON.tip@',
                            html: '<b class="orangeTxt">@CCMTS.refreshCmListSuccess@</b>'
                        });
                        //window.parent.onRefreshClick();
                        if(top.getFrame("entity-"+cmcId)!=null){
                            top.contentPanel.setActiveTab("entity-"+cmcId);
                            window.location.reload();
                        }
                    }else{
                        window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.refreshCmListFail@");
                    }
                },
                error:function(){
                    window.parent.showMessageDlg("@COMMON.tip@", "@CCMTS.refreshCmListFail@");
                },
                cache:false
            });
        }
    });
}

//软件升级
function updateCmc(){
    var ftpServiceEnable = true;
    if(ftpServiceEnable){
        window.parent.createDialog("uploadfile", I18N.CMC.title.updateDevice, 600, 370, "/cmc/showCmcUpdate.tv?cmcId="+cmcId + "&fileType=" + 5, null, true, true);
    }else {
        ftpServiceNoEnable();
    }
}
//导出配置
function downloadConfig(){
    window.top.createDialog('downloadConfig', I18N.CMC.label.exportConfig, 600, 370, 
            '/cmc/showDownloadConfig.tv?cmcId=' + cmcId, null, true, true); 
}
//导入配置
function updateCmcConfig(){
    var ftpServiceEnable = true;
    if(ftpServiceEnable){
        window.parent.createDialog("uploadfile", I18N.CMC.label.importConfig, 600, 370, "/cmc/showCmcUpdate.tv?cmcId="+cmcId + "&fileType=" + 4, null, true, true);
    }else {
        ftpServiceNoEnable();
    }
}
//清除配置
function clearConfig(){
    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CMC.tip.clearConfigConfirm, function(type) {
        if (type == 'no') {
            return;
        }
        window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.CMC.tip.clearConfigIng, 'ext-mb-waiting');
        $.ajax({
            url:'cmc/clearConfig.tv?cmcId='+ cmcId,
            type:'POST',
            dateType:'json',
            success:function(response){
                if(response.message == "success"){
                    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.CMC.tip.clearConfigSuccess, function(type){
                        if(type == "yes"){
                            $.ajax({
                                url:'cmc/resetCmc.tv?cmcId='+ cmcId,
                                type:'POST',
                                dateType:'json',
                                success:function(response){
                                    if(response.message == "success"){
                                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsSuccess);
                                        window.parent.closeWaitingDlg();
                                        top.afterSaveOrDelete({
                               				title: I18N.COMMON.tip,
                               				html: '<b class="orangeTxt">' +  I18N.CCMTS.resetCcmtsSuccess + '</b>'
                               			});
                                    }else{
                                        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.resetCcmtsFail);
                                    }
                                },
                                error:function(){
                                },
                                cache:false
                            });
                        }
                    } );
                }else{
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.clearConfigFailure);
                }
            },
            error:function(){
            },
            cache:false
        });
    });
}

//syslog配置
function syslogConfig(){
    window.top.createDialog('syslogManagement', I18N.syslog.syslogConfig, 800, 500, 
            '/cmc/showSyslogManagement.tv?entityId='+cmcId + "&cmcId=" + cmcId, null, true, true);
}
//ftp服务无法启动时调用，被updateCmc调用
function ftpServiceNoEnable(){
     window.parent.showMessageDlg(I18N.COMMON.tip,I18N.CMC.tip.ftpServerNotStart,null);  
}

//TrapServer配置
function showTrapServer(){
    window.top.createDialog('trapServerConfig', I18N.cmc.trapServerConfig, 800, 500,
            '/cmc/showTrapServer.tv?entityId=' + entityId, null, true, true);
}

//CCMTS Docsis 3.0 配置
function showDocsisConfig(){
    window.top.createDialog('cmcDocsisView', I18N.cmc.docsisConfig, 600, 370,
            '/cmc/loadDocsisConfig.tv?entityId=' + entityId + "&cmcId=" + cmcId, null, true, true);
}
//静态路由配置
function showCmc_bRoute(){
    window.top.createDialog('cmc_bRouteView', I18N.route.staticRouteMgmt, 600, 400, 
            '/cmc_b/route/showRouteViewList.tv?entityId=' + entityId + "&cmcId=" + cmcId, null, true, true);
}
//CCMTS 8800B 系统校时 
function showCmcSystemTimeConfig(){
    window.parent.createDialog('cmcSystemTimeConfig', I18N.SYSTEMTIME.modifyTime, 800, 500,
            '/cmc/systemtime/showSystemTimeConfig.tv?entityId=' + entityId, null, true, true, function (){
        clearInterval(window.top.cmcSystemTime);
    });
}
//设置通道利用率采集间隔
function setDolPolling(){
    window.top.createDialog('perfConfig', I18N.CMC.button.utilizationIntervalSet, 600, 370, 
            '/cmcperf/showDolPerfConf.tv?entityId=' + entityId, null, true, true);
}
function reload(){
    window.location.reload();
}
var totalTime =  ${ sysUpTime };
var offlineTime = cmcAttribute.statusChangeTimeStr;

function startChangeImageTimer(){
   
}

function stopChangeImageTimer(){
   
}

//获取设备运行时间
function syTime() {
    Ext.Ajax.request({
        url:'/cmc/getCmcUptimeByEntity.tv?cmcId=' + cmcId + '&num=' + Math.random(),
        method:"post",
        cache: false,
        success:function (response) {
            var json = Ext.util.JSON.decode(response.responseText);
            offlineTime = json.statusChangeTimeStr;
            totalTime = json.sysUpTime;
            oRunTime.clock.update({startTime:totalTime});
        },
        failure:function () {
            //modify by Victor@20140725不需要弹出确认对话框，而且只要标签页打开，不管当前页是否在这个页面都弹出是不对的
            //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.getDeviceRunTimeFailure);
        }});
}
function addView(id, title, icon, url) {
    window.parent.addView(id, title, icon, url,null,true);
}
function authLoad(){
    var ids = new Array();
    ids.add("resetCmc");
    ids.add("cmcSaveConfig");
    ids.add("updateConfig");
    ids.add("downloadConfig");
    ids.add("updateCmc");
    ids.add("cmcClearConfig");
    ids.add("noMacbind-Button")
    ids.add("cmcSystemTimeConfig")
    ids.add("cmcBasicInfo")
    operationAuthInit(operationDevicePower,ids);
    
    if(!refreshDevicePower){
        $("#refreshBt").attr("disabled",true);
    }
    
    if(!cmcPerfPower){
        $("#ccmtsPerfManage").attr("disabled",true);
    }
}
function setFlapInterval(){
    window.top.createDialog('flapInterval', I18N.cmcPortal.FlapConfig, 600, 370, 
            '/cmflap/showDolflapConf.tv?cmcId=' + cmcId, null, true, true);
}

function showSnmpBasicInfo(){
    /* window.top.createDialog('snmpBasicInfo', '', 380, 240, //Snmp参数配置
            '/cmc/config/showSnmpBasicInfo.tv?entityId=' + cmcId, null, true, true); */
}

function showEmsBasicInfo(){
    window.top.createDialog('emsBasicInfo', I18N.cmcPortal.EmsConfig, 600, 370, 
            '/cmc/config/showEmsBasicInfo.tv?entityId=' + cmcId, null, true, true);
}

function showSystemIpInfo(){
    window.top.createDialog('systemIpInfo', I18N.cmcPortal.SystemIpConfig, 800, 500, 
            '/cmc/config/showSystemIpInfo.tv?entityId=' + cmcId, null, true, true);
}

//实时信息查看
function showRealtimeInfo(){
	window.parent.addView('entity-realTime' + cmcId, '@COMMON.realtimeInfo@['+$nmName+']', 'entityTabIcon', '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
}

$(window).load(function(){
    //var leftPartStr = "portletDetail,portletSysUpTime,portletCmInfo,upChannelInfo,downChannelInfo";
    //var rightPartStr = "portletTools,configFileMgmt,cmcPortInfo,upChannelUserNum,downChannelUserNum";
    
});

//添加到百度地图
function addBaiduMap(){
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + cmcId + "&typeId=" + cmcType + "&entityName=" + $nmName, null, true, true);
}
    
</script>
    </head>
    <body onload="authLoad();" class="newBody clear-x-panel-body">
        <div id="header">
            <%@ include file="entity.inc"%>
        </div>
        <div id=detail style="dislpay: none">
            <div style="position: relative">
                <div class="rightTopPutPic">
                     <img id="entityIcon" src="" border=0 />
                </div>
                <table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0"
                    bordercolor="#DFDFDF" rules="all">
                        <tbody class="ccWithAgent">
                            <tr>
                                <td class="rightBlueTxt noWrap" width="120">@CMC.label.labelEntityType@:</td>
                                <td><div class="pR230">${ cmcAttribute.cmcDeviceStyleString }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.entityName@:</td>
                                <td class="wordBreak"><div class="pR230">${cmcAttribute.nmName}</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.labelHardwareName@:</td>
                                <td class="wordBreak">
                                    <div class="pR230">${cmcAttribute.topCcmtsSysName}</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.text.mac@:</td>
                                <td class="wordBreak">
                                    <div class="pR230">${formatedMac}</div>
                                </td>
                            </tr>
<!--                             <tr> -->
<!--                                 <td class="rightBlueTxt noWrap">@CCMTS.entityLocation@:</td> -->
<!--                                 <td class="wordBreak"> -->
<%--                                     <div class="pR120">${ cmcAttribute.topCcmtsSysLocation }</div> --%>
<!--                                 </td> -->
<!--                             </tr> -->
<!--                             <tr> -->
<!--                                 <td class="rightBlueTxt noWrap">@CCMTS.contactPerson@:</td> -->
<!--                                 <td class="wordBreak"> -->
<%--                                     <div class="pR120">${ cmcAttribute.topCcmtsSysContact }</div> --%>
<!--                                 </td> -->
<!--                             </tr> -->
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.rfModuleStatus@:</td>
                                <td class="wordBreak"><div  id="rfModuleStatus-with-agent" class="pR230">${ cmcAttribute.topCcmtsSysStatusString }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.text.ipaddress@:</td>
                                <td class="wordBreak"><div class="pR230">${ entity.ip }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.SysDescr@:</td>
                                <td class="wordBreak"><div class="pR230">${ cmcAttribute.topCcmtsSysDescr }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.contact@:</td>
                                <td class="wordBreak"><div class="pR230">${ entity.contact }</div></td>
                            </tr>
                             <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.location@:</td>
                                <td class="wordBreak">${ entity.location }</td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.note@:</td>
                                <td class="wordBreak">${ entity.note }</td>
                            </tr>
                        </tbody>
                        <tbody class="ccWithoutAgent">
                            <tr>
                                <td class="rightBlueTxt noWrap" width="120">@CMC.label.labelEntityType@:</td>
                                <td class="wordBreak"><div class="pR230">${ cmcAttribute.cmcDeviceStyleString }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.entityName@:</td>
                                <td class="wordBreak"><div class="pR230">${ cmcAttribute.nmName }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.labelHardwareName@:</td>
                                <td class="wordBreak">
                                    <div class="pR230">${ cmcAttribute.topCcmtsSysName }</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.oltName@:</td>
                                <td class="wordBreak"><div class="pR230" id="entityName"></div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.oltType@:</td>
                                <td class="wordBreak">
                                    <div class="pR230">${ entity.typeName }</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">OLT IP:</td>
                                <td class="wordBreak"><div  id="entityIp" class="pR230"></div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.cmcUpLinkPonPort@:</td>
                                <td class="wordBreak"><div class="pR230">${ cmcUpLinkPonPort }</div></td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.text.mac@:</td>
                                <td class="wordBreak"><div class="pR230">${ formatedMac }</div></td>
                            </tr>
<!--                             <tr> -->
<!--                                 <td class="rightBlueTxt noWrap">@CCMTS.entityLocation@:</td> -->
<%--                                 <td class="wordBreak">${ cmcAttribute.topCcmtsSysLocation }</td> --%>
<!--                             </tr> -->
<!--                             <tr> -->
<!--                                 <td class="rightBlueTxt noWrap">@CCMTS.contactPerson@:</td> -->
<%--                                 <td class="wordBreak">${ cmcAttribute.topCcmtsSysContact }</td> --%>
<!--                             </tr> -->
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.rfModuleStatus@:</td>
                                <td id="rfModuleStatus-without-agent" class="wordBreak">${ cmcAttribute.topCcmtsSysStatusString }</td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CMC.label.SysDescr@:</td>
                                <td class="wordBreak">${ cmcAttribute.topCcmtsSysDescr }</td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.contact@:</td>
                                <td class="wordBreak">${ cmts.contact }</td>
                            </tr>
                             <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.location@:</td>
                                <td class="wordBreak">${ cmts.location }</td>
                            </tr>
                            <tr>
                                <td class="rightBlueTxt noWrap">@CCMTS.note@:</td>
                                <td class="wordBreak">${ cmts.note }</td>
                            </tr>
                        </tbody>
                </table>
            </div>

        </div>
        <div id=tools class="edge10 floatLeft">
            <dl class="leftFloatDl">
                <dd>
                    <a id="refreshBt" onclick="refreshCcmts();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoRefresh"></i>@network/NETWORK.reTopo@</span></a>
                </dd>
                <dd>
                    <a id="ccmtsPerfManage" onclick="ccmtsPerfManage();" href="javascript:;" class="normalBtnBig"><span><i 
                            class="miniIcoManager"></i>@CCMTS.ccmtsPerformCollectConfig@</span></a>
                </dd>
                <dd>
                    <a id="refreshCmList" onclick="refreshCmList();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoRefresh"></i>@network/NETWORK.refreshCmList@</span></a>

                </dd>
                <dd>
                    <a id="resetCmc" onclick="resetCmc();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoPower"></i>@CCMTS.resetCMC@</span></a>
                </dd>
                <dd class="baseInfoCfg">
                    <a id="cmcBasicInfo" onclick="ccmtsBasicInfoMgmt();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoInfo"></i>@CCMTS.ccmtsBasicInfoMgmt@</span></a>
                </dd>
                	<dd class="ccWithoutAgent">
	                    <a id="noMacbind-Button" onclick="cmcNoMacBind();" href="javascript:;" class="normalBtnBig"><span><i
	                            class="miniIcoManager"></i>@CCMTS.noMacbind@</span></a>
                	</dd>

                    <dd class="upgrade ccWithAgent">
                        <a id="updateCmc" onclick="updateCmc();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoArrUp"></i>@CMC.title.updateDevice@</span></a>
                    </dd>
                    <dd class="trapServer ccWithAgent">
                        <a id="configTrapServer" onclick="showTrapServer();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@cmc.trapServerConfig@</span></a>
                    </dd>
                    <dd class="syslog ccWithAgent">
                        <a id="syslogConfig" onclick="syslogConfig();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@syslog.syslogConfig@</span></a>
                    </dd>
                    <dd class="flap ccWithAgent">
                        <a onclick="setFlapInterval();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoTime"></i>@cmcPortal.FlapConfig@</span></a>
                    </dd>
                    <dd class="ccWithAgent">
                        <a onclick="showEmsBasicInfo();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@cmcPortal.EmsConfig@</span></a>
                    </dd>
                    <dd class="systemIp ccWithAgent">
                        <a onclick="showSystemIpInfo();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@cmcPortal.SystemIpConfig@</span></a>
                    </dd>
                    <dd class="docsisConfig ccWithAgent">
                        <a id="cmcDocisConfig" onclick="showDocsisConfig();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@cmc.docsisConfig@</span></a>
                    </dd>
                    <dd class="systemTime ccWithAgent">
                        <a id="cmcSystemTimeConfig" onclick="showCmcSystemTimeConfig();" href="javascript:;"
                            class="normalBtnBig"><span><i class="miniIcoTime"></i>@SYSTEMTIME.modifyTime@</span></a>
                    </dd>
                    <dd class="staticRoute ccWithAgent">
                        <a id="cmc_bRoute" onclick="showCmc_bRoute();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@route.staticRouteMgmt@</span></a>
                    </dd>
                    <dd class="ccWithAgent">
                        <a id="utilizationInterval" onclick="setDolPolling();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@CMC.button.utilizationIntervalSet@</span></a>
                    </dd>
                    <dd>
						<a id="showRealtimeInfo" href="javascript:;" class="normalBtnBig" onclick="showRealtimeInfo()"><span><i 
							    class="miniIcoView"></i>@COMMON.realtimeInfo@</span></a>
					</dd>
					<dd>
                    	<a id="baiduMap" onclick="addBaiduMap();" href="javascript:;" class="normalBtnBig"><span><i 
                    			class="miniIcoGoogle"></i>@network/BAIDU.viewMap@</span></a>
                	</dd>
                	<dd class="cmUpgrade">
                    	<a id="cmUpgradeFileManagement" onclick="showCmUpgradeFile()" href="javascript:;" class="normalBtnBig cmUpgrade" ><span><i 
                    			class="miniIcoInfo"></i>@CM.UpgradeFileMan@</span></a>
                    </dd>
                	<dd>
                		<a id="userAttention" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i
                           		class="bmenu_eyes"></i>@resources/COMMON.attention@</span></a>
                    </dd>
                    <dd class="autoClearCm">
                        <a id="clearOfflineCmOfCmts" onclick="onclearTimeConfigClick();" href="javascript:;" class="normalBtnBig"><span><i
                                class="miniIcoManager"></i>@resources/COMMON.clearOfflineCm@</span></a>
                    </dd>
                    <dd>
                    	<a id="userAttentionCancel" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i
                            	class="bmenu_eyesClose"></i>@resources/COMMON.cancelAttention@</span></a>
                    </dd>
                    
                    
            </dl>

        </div>
        <div id=cmInfo style="dislpay: none">
            <table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0"
                bordercolor="#DFDFDF" rules="all">
                <tr>
                    <td class="rightBlueTxt" width="80">@CM.totalNum@:</td>
                    <td id="cmNumTotal">${ cmcCmNumStatic.cmNumTotal }</td>
                    <td class="rightBlueTxt" width="80">@CM.onlineNum@:</td>
                    <td id="cmNumOnline">${ cmcCmNumStatic.cmNumOnline }</td>
                </tr>
                <tr>
                    <td class="rightBlueTxt">@CM.activeCMCount@:</td>
                    <td id="cmNumActive">${ cmcCmNumStatic.cmNumActive }</td>
                    <td class="rightBlueTxt">@CM.offlineCMCount@:</td>
                    <td id="cmNumOffline">${ cmcCmNumStatic.cmNumOffline }</td>
                </tr>
            </table>
        </div>
        <div id="configMgmtTools" class="edge10 floatLeft ccWithAgent">
            <dl class="leftFloatDl">
                <dd>
                    <a id="cmcSaveConfig" onClick="saveConfig();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoData"></i>@CMC.button.saveConfig@</span></a>
                </dd>
                <dd class="configFile">
                    <a id="downloadConfig" onClick="downloadConfig();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoExport"></i>@CMC.label.exportConfig@</span></a>
                </dd>
                <dd class="configFile">
                    <a id="updateConfig" onClick="updateCmcConfig();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoInport"></i>@CMC.label.importConfig@</span></a>
                </dd>
                <dd class="clearConfig">
                    <a id="cmcClearConfig" onClick="clearConfig();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoClose"></i>@CMC.title.clearConfig@</span></a>
                </dd>
            </dl>
        </div>
        <div id="runTime"></div>
        <script type="text/javascript" src="../js/jquery/zebra.js"></script>
        <script type="text/javascript">
    //处理ie6和ie7不能自适应的问题;
    $(function(){
        $(".leftFloatDl dd").each(function(){
            $(this).css("float","none")
            var w = $(this).find("a").outerWidth();
            $(this).width(w);           
        })
        $(".leftFloatDl dd").css("float","left");
    })
    </script>
        <script type="text/javascript" src="/cmc/js/newCmcPortal.js"></script>
    </body>
</Zeta:HTML> 
