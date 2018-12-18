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
    PLUGIN Portlet
    module onu
    import js/nm3k/Nm3kClock
    import epon.onu.OnuDeviceAction
    import js/entityType
    import gpon/customGponOnu
    import js/tools/imgTool
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
#portletTools .x-panel-body{ background: #fff;}
.putCir{ width:150px; height:170px; /*background:#eee;*/}
.cirLengend{width:16px; height:16px; overflow:hidden; display:block; float:left; margin-right:2px;}
.clrLendUl{border:1px solid #ccc; margin:10px 0px; background:#fff;-moz-border-radius: 5px;-khtml-border-radius: 5px;-webkit-border-radius: 5px;border-radius: 5px;}
.floatLabel{ float:left; padding:2px 10px 2px 0px;}
</style>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<script>
// for tab changed start
function tabActivate() {
	doRefresh();
	startTimer();
}
function tabDeactivate() {
	stopTimer();
}
function tabRemoved() {
	stopTimer();
}
function tabShown() {}
// for tab changed end
var entityId = '${olt.entityId}';
var entityIp = '${olt.ip}';
var onuId = ${onu.entityId};
var typeId = '${onu.typeId}';
var onuName = "${onu.name}";
var onulocation = "${onu.location}";
var onuIndex = ${onuAttribute.onuIndex};
var width = Ext.getBody().getWidth();
var ponPerfStats15minuteEnable = "${onuAttribute.ponPerfStats15minuteEnable}";
var temperatureDetectEnable = "${onuAttribute.temperatureDetectEnable}";
var onuIsolationEnable = "${onuAttribute.onuIsolationEnable}";
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var onuPerfPower= <%=uc.hasPower("onuPerfParamsConfig")%>;
var catvCapability = "${onuAttribute.catvCapability}";
var gponOnuCatvCap="${gponOnuCapability.onuTotalCatvNum}"
//var wlanCapability = "${onuAttribute.wlanCapability}";
var AUTO_REFRESH_TIMER,
	AUTO_SYSTIME_TIMER;

var dispatchTotal = 120000;
var dispatchInterval = 15000;
var totalTime;
var attention = "${onuAttribute.attention}";
var onuLaser = "${onuAttribute.laserSwitch}" == 1 ? true:false;
var hardVersion = '${onuAttribute.topOnuHardwareVersion}';
var onuStatus = "${onuAttribute.onuOperationStatus}";
var onuLaserSupport = "${onuLaserSupport}";
/*
 * 记录cpu利用率,内存利用率,Flash利用率三个图表;
 * err表示当获取到数据是-1的时候，则显示错误信息;
 */
var oProgressCir = {
	notGetData : "@Tip.failedGetData@",
	NaNData : "@ONU.loadDataError@"
}
/*
 * 记录时间控件的对象;
 * type类型：cir,grayCalendar,whiteCalendar;
 */
var oRunTime = {
	totalTime : 0,
	clock : null,//记录时间控件;
	language : '@COMMON.language@',
 	type : 'grayCalendar'
 }
 
 var onuImg = {
			'8625-004' : '/images/network/onu/8625_102_64.png',
			'8625-102' : '/images/network/onu/8625_102_64.png',
			'8625-103' : '/images/network/onu/8625_102_64.png',
			'8625-121' : '/images/network/onu/8625_102_64.png',
			'8625-127' : '/images/network/onu/8625_102_64.png',
			'8625-202' : '/images/network/onu/8625Icon_64.png',
			'8625-203' : '/images/network/onu/8625Icon_64.png',
			'8625-223' : '/images/network/onu/8625Icon_64.png',
			'8625-225' : '/images/network/onu/8625Icon_64.png',
			'8625-227' : '/images/network/onu/8625Icon_64.png',
			'8625-302' : '/images/network/onu/8625_102_64.png',
			'8625-402' : '/images/network/onu/8625Icon_64.png',
			'8625-405' : '/images/network/onu/8625_102_64.png',
			'8626-102' : '/images/network/onu/8626_102_64.png',
			'8626-107' : '/images/network/onu/8626_102_64.png',
			'8626-121' : '/images/network/onu/8626_102_64.png',
			'8626-124' : '/images/network/onu/8626_102_64.png',
			'8626-125' : '/images/network/onu/8626_102_64.png',
			'8626-127' : '/images/network/onu/8626_102_64.png',
			'8626-302' : '/images/network/onu/8626Icon_64.png',
			'8626-304' : '/images/network/onu/8626_wifi_Icon_64.png',
			'8626-305' : '/images/network/onu/8626_wifi_Icon_64.png',
			'8626-307' : '/images/network/onu/8626Icon_64.png',
			'8626-321' : '/images/network/onu/8626Icon_64.png',
			'8626-323' : '/images/network/onu/8626Icon_64.png',
			'8626-324' : '/images/network/onu/8626_wifi_Icon_64.png',
			'8626-325' : '/images/network/onu/8626_wifi_Icon_64.png',
			'8626-327' : '/images/network/onu/8626Icon_64.png',
			'8626-402' : '/images/network/onu/8626Icon_64.png',
			'8626-502' : '/images/network/onu/8626_102_64.png',
			'8626-602' : '/images/network/onu/8626_102_64.png',
			'8626-702' : '/images/network/onu/8626Icon_64.png',
			'8626-704' : '/images/network/onu/8626Icon_64.png',
			'8626-802' : '/images/network/onu/8626Icon_64.png'
		 }
 
function renderTrafficOption(){
	var option = parseInt('${gponOnuCapability.onuTrafficMgmtOption}',10);
	switch(option){
		case 0:
			return "@gpon/GPON.onuTrafficMgmtOpt0@";
		case 1:
			return "@gpon/GPON.onuTrafficMgmtOpt1@";
		case 2:
			return "@gpon/GPON.onuTrafficMgmtOpt2@";
		default:return "";
	}
}
//加载新的PON口光信息;
 function trapLoadOnuPonAttribute(data){
 	if(!data || !data.ip || !entityIp || entityIp != data.ip){
 		return;
 	}
 	$.ajax({
 		url:'/onu/loadOnuPonAttribute.tv',cache:false,
 		data : {onuId : onuId},
 		success : function(json){
 			updateOnuPonInfo(json);	
 		},
 		error : function(){
 			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.loadPonFail@")
 		}
 	})
 }
 //更新PON口光信息;
 function updateOnuPonInfo(data){
 	if(!data){ return;}
 	if(typeof(data.onuStatus) != 'undefined' && data.onuStatus != 1){ //不在线;
 		var offlineStr = '--';
 		$("#onuBiasCurrent").html(offlineStr);
 		$("#onuWorkingVoltage").html(offlineStr);
 		$("#tempreture").html(offlineStr);
 		$("#receivedOpticalPower").html(offlineStr);
 		$('#onuTramsmittedOpticalPower').html(offlineStr);
 		$('#ponTransmitPower').html(offlineStr);
 		return;
 	}
 	$("#onuBiasCurrent").html(changeData(data.onuBiasCurrent, 'mA'));
 	$("#onuWorkingVoltage").html(changeData(data.onuWorkingVoltage, 'mV'));
 	$("#tempreture").html(changeData(data.onuWorkingTemperature, '@{unitConfigConstant.tempUnit}@'));
 	$("#receivedOpticalPower").html(changeData(data.onuReceivedOpticalPower, '@COMMON.dBm@'));
 	$('#onuTramsmittedOpticalPower').html(changeData(data.onuTramsmittedOpticalPower, '@COMMON.dBm@'));
 	$('#ponTransmitPower').html(data.oltPonRevPower + '@COMMON.dBm@');
 }
 function changeData(data, unit){
 	if(data == 0 || typeof(data) == 'undefined'){
 		return '--';	
 	}else{
 		return String.format((data/100).toFixed(2) + ' {0}', unit)
 	}
 }
 function trapDisableClock(data){
 	if(!data || !data.ip || !window.entityIp || window.entityIp != data.ip || !oRunTime || !oRunTime.clock || oRunTime.clock == null){
 		return;
 	}
 	oRunTime.clock.update({startTime:-1});
 }
 //onu升级需要延迟70秒后再去读取mib;
 function trapChangeVersion(data){
 	setTimeout(function(){
 		if(!window.onuId || !data || !data.entityId || window.onuId != data.entityId){
 			return;
 		}
 		$.ajax({
 			url:'/onu/getOnuAttrById.tv',cache:false,
 			data : {onuId : onuId},
 			success : function(json){
 				if(json && json.onuSoftwareVersion){
 					$('#putSoftVersion').text(json.onuSoftwareVersion);
 				}
 				if(json && json.topOnuHardwareVersion){
 					$('#putTopOnuHardwareVersion').text(json.topOnuHardwareVersion);
 				}
 			},
 			error : function(){
 				
 			}
 		})
 	}, 70000);
 }
 //提示用户该ONU已经被删除;
 //加遮罩不让再操作了，并且置灰连续运行控件;
 function trapOnuDelete(data){
 	if(!data || !data.entityId || !window.onuId || window.onuId != data.entityId){
 		return;
 	}
 	Ext.getBody().mask("<label class='tipMask'>@COMMON.onuDelete@</label>");
 	if(!oRunTime || !oRunTime.clock || oRunTime.clock == null){
 		return;
 	}
 	oRunTime.clock.update({startTime:-1});
 } 

var portlets = [];
Ext.onReady(function(){
	//权限控制，开启激光器按钮不显示;
	if(!onuLaserSupport){
		$('#onuLaserOpen').parent().css({display: 'none'});
	}
	
	//GPON ONU 不支持;
	//ONU升级,更新软件版本和硬件版本;
	/* var onu_upgrade = top.PubSub.on(top.OltTrapTypeId.ONU_UPGRADE, function(data){
		trapChangeVersion(data);
	}); */
	//设备重启;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		trapDisableClock(data);
		trapLoadOnuPonAttribute(data);
	});
	//onu删除，加一个弹出层，直接提示用户ONU已经被删除;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapOnuDelete(data);
	});
	//PON禁用告警，加载新的PON口光信息,更新运行时长 ;
	var pon_port_disable = top.PubSub.on(top.OltTrapTypeId.PON_PORT_DISABLE, function(data){
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//PON口光模块拔出;
	var optical_module_remove = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, function(data){
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//PON口光模块插入;
	var optical_module_insert = top.PubSub.on(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, function(data){
		trapLoadOnuPonAttribute(data);
		syTime()
	});
	//PON口主干光纤断纤；
	var port_pon_los = top.PubSub.on(top.OltTrapTypeId.PORT_PON_LOS, function(data){
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//ONU掉电;
	var onu_pwr_off = top.PubSub.on(top.OltTrapTypeId.ONU_PWR_OFF, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//ONU下线;
	var onu_offline = top.PubSub.on(top.OltTrapTypeId.ONU_OFFLINE, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//ONU上线 ，注意其他地方连续运行时间都是直接置为-1，这里是重新去读一次;
	var onu_online = top.PubSub.on(top.OltTrapTypeId.ONU_ONLINE, function(data){
		trapLoadOnuPonAttribute(data);
		syTime();
	});
	//onu 断纤;
	var onu_fiber_break = top.PubSub.on(top.OltTrapTypeId.ONU_FIBER_BREAK, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//onu 长发光;
	var onu_rogue = top.PubSub.on(top.OltTrapTypeId.ONU_ROGUE, function(data){
		if(top.isClearAlert(data)) {
			return;	
		}
		trapLoadOnuPonAttribute(data);
		trapDisableClock(data);
	});
	//ONU自动升级 
	/* var onu_auto_upgrade = top.PubSub.on(top.OltTrapTypeId.ONU_AUTO_UPGRADE, function(data){
		trapChangeVersion(data);
	}); */
	
	$(window).on('unload', function(){
		//top.PubSub.off(top.OltTrapTypeId.ONU_UPGRADE, onu_upgrade);
		//top.PubSub.off(top.OltTrapTypeId.ONU_AUTO_UPGRADE, onu_auto_upgrade);
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
		top.PubSub.off(top.OltTrapTypeId.PON_PORT_DISABLE, pon_port_disable);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_REMOVE, optical_module_remove);
		top.PubSub.off(top.OltTrapTypeId.OPTICAL_MODULE_INSERT, optical_module_insert);
		top.PubSub.off(top.OltTrapTypeId.PORT_PON_LOS, port_pon_los);
		top.PubSub.off(top.OltTrapTypeId.ONU_PWR_OFF, onu_pwr_off);
		top.PubSub.off(top.OltTrapTypeId.ONU_OFFLINE, onu_offline);
		top.PubSub.off(top.OltTrapTypeId.ONU_FIBER_BREAK, onu_fiber_break);
		top.PubSub.off(top.OltTrapTypeId.ONU_ROGUE, onu_rogue);
	});
	
	$("#onuAdminStatus").text(${onuAttribute.onuAdminStatus} == 1 ? "@STATUS.up@" : "@STATUS.down@");
	$("#onuTrafficMgmtOption").text(renderTrafficOption());
	$("#onuConnect").html( GPON_ONU.renderConnectCapbility('${gponOnuCapability.onuConnectCapbility}') );
	//$("#onuQosFix").html( GPON_ONU.renderQosFlexibility('${gponOnuCapability.onuQosFlexibility}') );
	
	oRunTime.clock = new Nm3kClock({
		renderTo: 'runTime',//要渲染到哪个div的id;
		startTime: oRunTime.totalTime,
		type: oRunTime.type,
		language: oRunTime.language
	});
	oRunTime.clock.init();
	//修改为从数据库读取布局;
	//基本信息;
	var portletDetail = {id:'portletDetail', title: "@ONU.basicinfo@", bodyStyle:'padding:10px', autoScroll:true, contentEl:'detail'}
	//关联下级设备信息;
    var subDeviceInfo = {id:'subDeviceInfo', title: "@downlink.menu.ableInformation@", bodyStyle: 'padding:8px', autoScroll: true, contentEl:'subInfo'};
    //PON口光信息;
    var ponOpticalInfo = {id:'ponOpticalInfo', title: "@downlink.menu.ponInfo@", bodyStyle: 'padding:8px', autoScroll: true, contentEl:'opticalInfo'};
    //管理工具;
	var portletTools = {id:'portletTools', title: "@ENTITYSNAP.tools.tools@", bodyStyle:'padding:10px', autoScroll:true, contentEl:'tools'};
	//软件信息
	var portletSoftware= {id:'portletSoftware', title: "@gpon/GPON.softInfo@", bodyStyle: 'padding:10px', autoScroll: true, contentEl:'software'};
	//连续运行时间;
	var portletSysUpTime= {id:'portletSysUpTime', title: "@ENTITYSNAP.deviceUpTime.deviceUpTime@", bodyStyle: 'padding:10px', autoScroll: true, contentEl:'runTime'};
	//ONU catv信息;
	if(gponOnuCatvCap > 0){
    	var onuCatvInfo = {id:'onuCatvInfo', title: "@CATV.information@", bodyStyle: 'padding:8px', autoScroll: true, contentEl:'catvInfo'};
	    $("#catvInfo").show(); 
	}else{
	    $("#catvInfo").hide(); 
	}
	    
 	var columns = 2;
    var portletItems = [];
    
    //左侧板块，从数据库读取;
	var leftPartStr = '${leftPartItems}';
	//右侧板块，从数据库读取;
	var rightPartStr = '${rightPartItems}';
	//如果是第一次加载,就进行默认初始化
	if( !leftPartStr || !rightPartStr){
		leftPartStr = "portletDetail,subDeviceInfo";
		if(gponOnuCatvCap > 0){
			rightPartStr = "portletTools,portletSoftware,portletSysUpTime,ponOpticalInfo,onuCatvInfo";
		}else{
		    rightPartStr = "portletTools,portletSoftware,portletSysUpTime,ponOpticalInfo";
		}
	}
	
    var leftPart = {};
    leftPart.columnWidth = .5;
    leftPart.style = "padding:10px 5px 10px 10px";
    leftPart.items = new Array();
    //开始添加板块;
    if( leftPartStr ){
		 var tempArr = leftPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 leftPart.items.push(eval(tempArr[i]));
		 }
	}
    portletItems[0] = leftPart;
	var leftItems = portletItems[0].items;
    
	var rightPart = {};
	rightPart.columnWidth = 0.5;
	rightPart.style = "padding:10px 10px 10px 5px";
	rightPart.items = new Array();
	//开始添加板块;
  	 if( rightPartStr ){
	     var tempArr = rightPartStr.split(",");
		 for(var i=0; i<tempArr.length; i++){
			 rightPart.items.push(eval(tempArr[i]));
		 }
  	 }
    portletItems[1] =  rightPart;
    
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

			}else{//有变化;
				leftPartStr = leftArr.toString();
				rightPartStr = rightArr.toString();
				$.ajax({
					url: '/epon/savePortalView.tv',
					cache:false, 
					method:'post',
					data: {
						typeId : -parseInt(typeId,10),
						leftPartItems : leftPartStr, 
						rightPartItems : rightPartStr
					}
				});
			};//end if else;
		}
	var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 37, maxSize: 37});
    var viewport = new Ext.Viewport({layout: 'border',
        items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    });
    startTimer();
    if(onuStatus == 1){
    	$("#tempreture").html( '${onuPonAttribute.onuWorkingTemperature}' == 0? "--" : ('${onuPonAttribute.onuWorkingTemperature}'/ 100).toFixed(2) +"  @{unitConfigConstant.tempUnit}@");
    	$("#receivedOpticalPower").html('${onuPonAttribute.onuPonRevPower}' == 0? "--" : Number('${onuPonAttribute.onuPonRevPower}').toFixed(2) +'  @COMMON.dBm@');
    	$("#onuTramsmittedOpticalPower").html('${onuPonAttribute.onuPonTransPower}' == 0? "--" : Number('${onuPonAttribute.onuPonTransPower}').toFixed(2) +'  @COMMON.dBm@');
    	$("#onuBiasCurrent").html('${onuPonAttribute.onuBiasCurrent}' == 0? "--" : ('${onuPonAttribute.onuBiasCurrent}' / 100).toFixed(2) +'  mA');
    	$("#onuWorkingVoltage").html('${onuPonAttribute.onuWorkingVoltage}' == 0? "--" : ('${onuPonAttribute.onuWorkingVoltage}' / 100000).toFixed(2) +'  V');
    	$("#ponTransmitPower").text('${onuPonAttribute.oltPonRevPower}' == 0? "--" :  Number('${onuPonAttribute.oltPonRevPower}' || 0).toFixed(2) + " @COMMON.dBm@" );
    }else{
    	$("#tempreture").html("--");
    	$("#receivedOpticalPower").html("--");
    	$("#onuTramsmittedOpticalPower").html("--");
    	$("#onuBiasCurrent").html("--");
    	$("#onuWorkingVoltage").html("--");
    	$("#ponTransmitPower").text("--");
    }
    
	//$("#onuChipType").text(convert2Bit(${onuAttribute.onuChipType }) );
	//$("#LLID").text(convert2Bit( ${onuAttribute.onuLlidId} ));
	
	if(attention){
     	$("#userAttention").parent().hide();
     	$("#userAttention").parent().hide();
     }else{
     	$("#userAttentionCancel").parent().hide();
     }
	
	if(onuLaser){
     	$("#onuLaserOpen").parent().hide();
     }else{
     	$("#onuLaserClose").parent().hide();
     }
	
	var imgSrc = onuImg[hardVersion] || "/images/${onu.icon64}";
	var newImgSrc = relaceImgSrc(imgSrc);
	loadImage(newImgSrc, function(){
		$('.rightTopPutPic').html(this);
	});
	//$('#entityIcon').attr({src : imgSrc})
	
	//initWifiOnuModule();
	
	//初始化版本信息内容
	var software1Valid = '${gponOnuInfoSoftware.onuSoftware0Valid}' == 1? "@gpon/GPON.effective@":"@gpon/GPON.invalid@";
	var software1Active = '${gponOnuInfoSoftware.onuSoftware0Active}' == 1? "@gpon/GPON.yes@":"@gpon/GPON.no@";
	var software1Commited = '${gponOnuInfoSoftware.onuSoftware0Commited}' == 1? "@gpon/GPON.yes@":"@gpon/GPON.no@";
	var software2Valid = '${gponOnuInfoSoftware.onuSoftware1Valid}' == 1? "@gpon/GPON.effective@":"@gpon/GPON.invalid@";
	var software2Active = '${gponOnuInfoSoftware.onuSoftware1Active}' == 1? "@gpon/GPON.yes@":"@gpon/GPON.no@";
	var software2Commited = '${gponOnuInfoSoftware.onuSoftware1Commited}' == 1? "@gpon/GPON.yes@":"@gpon/GPON.no@";
	$("#software1Version").text('${gponOnuInfoSoftware.onuSoftware0Version}');
	$("#software1Valid").text(software1Valid);
	$("#software1Active").text(software1Active);
	$("#software1Commited").text(software1Commited);
	$("#software2Version").text('${gponOnuInfoSoftware.onuSoftware1Version}');
	$("#software2Valid").text(software2Valid);
	$("#software2Active").text(software2Active);
	$("#software2Commited").text(software2Commited);
});

function initWifiOnuModule(){
	/* if(wlanCapability == 1){
		R.restore.show();
	    $("#wifiSoftVersion").show();
	    $("#wifiHardVersion").show();
	}else{
		R.restore.hide();
		$("#wifiSoftVersion").hide();
	    $("#wifiHardVersion").hide();
	} */
}

function startTimer() {
	if ( !AUTO_REFRESH_TIMER ) {
		AUTO_REFRESH_TIMER = setInterval(doRefresh, dispatchTotal);
	}
	if( !AUTO_SYSTIME_TIMER ){
		syTime();
		AUTO_SYSTIME_TIMER = setInterval(syTime, 300000);
	}
}
function stopTimer() {
	if ( !AUTO_REFRESH_TIMER ) {
		clearInterval(AUTO_REFRESH_TIMER);
		AUTO_REFRESH_TIMER = null;
	}
	if( !AUTO_SYSTIME_TIMER ){
		clearInterval( AUTO_SYSTIME_TIMER );
		AUTO_SYSTIME_TIMER = null;
	}
}
function doRefresh() {
}

function syTime() {
	$.ajax({
		url:"/onu/getOnuUpTime.tv?onuId="+onuId,
		method:"post",cache: false,dataType:'json',
		success:function (json) {
    		totalTime = json.sysUpTime;
    		oRunTime.clock.update({startTime:totalTime});
		}
	});
}

function fetchPonPerfStats15minuteEnable(){
	return ponPerfStats15minuteEnable;
}
function fetchTemperatureDetectEnable(){
	return temperatureDetectEnable;
}
function fetchOnuIsolationEnable(){
	return onuIsolationEnable;
}

function authLoad(){
    if(!refreshDevicePower){
        R.reTopoOnu.setDisabled(true);
    }
    if(!onuPerfPower){
        R.onuPerfCfg.setDisabled(true);
    }
}

function onAttentionClick(){
	if(attention){//如果已关注,则从table中删除该记录
		$.ajax({
			url:'/entity/cancelAttention.tv',cache:false,
			data : {entityId : onuId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.cancelFocusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.cancelFocusSucccess@</b>'
	   			});
				attention = false;
				$("#userAttention").show();
				$("#userAttention").parent().show();
				$("#userAttentionCancel").parent().hide();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.cancelFocusFail@")
			}
		})
	}else{//如果没有关注则添加该设备记录
		$.ajax({
			url:'/entity/pushAttention.tv',cache:false,
			data : {entityId : onuId},
			success : function(){
				//window.parent.showMessageDlg("@COMMON.tip@", "@NETWORK.focusSucccess@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@network/NETWORK.focusSucccess@</b>'
	   			});
				attention = true;
				$("#userAttention").hide();
				$("#userAttention").parent().hide();
			    $("#userAttentionCancel").parent().show();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@network/NETWORK.focusFail@")
			}
		})
	}
}

function operationOnuLaser(){
	if(onuLaser){//ONU激光器开关
		$.ajax({
			url:'/onu/rogueonu/modifyOnuLaserSwitch.tv',cache:false,
			data : {
				onuId : onuId,
				entityId : entityId,
				onuLaser : 2
			},
			success : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@ONU.operaCloseLaser@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@ONU.closeLaserSwitchSuc@</b>'
	   			});
				onuLaser = false;
				$("#onuLaserOpen").parent().show();
				$("#onuLaserClose").parent().hide();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@ONU.closeLaserSwitchFail@")
			}
		})
	}else{
		$.ajax({
			url:'/onu/rogueonu/modifyOnuLaserSwitch.tv',cache:false,
			data : {
				onuId : onuId,
				entityId : entityId,
				onuLaser : 1
			},
			success : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@ONU.operaOpenLaser@")
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@ONU.openLaserSwitchSuc@</b>'
	   			});
				onuLaser = true;
				$("#onuLaserOpen").parent().hide();
			    $("#onuLaserClose").parent().show();
			},
			error : function(){
				window.parent.showMessageDlg("@COMMON.tip@", "@ONU.openLaserSwitchFail@")
			}
		})
	}
}

//添加到百度地图
function addBaiduMap(){
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + onuId + "&typeId=" + typeId + "&entityName=" + onuName, null, true, true);
}

//恢复出厂设置（仅针对wifi onu）
function restoreOnu(){
	 window.parent.showConfirmDlg(I18N.COMMON.tip, "@ONU.confirmrestore@", function(button, text) {
	     if (button == "yes") {
			window.parent.showWaitingDlg("@COMMON.wait@", '@ONU.restore@','waitingMsg','ext-mb-waiting');
		    $.ajax({
		        url:'/onu/restoreOnu.tv',
		        type:'POST',
		        data:{
		        	entityId: entityId,
		        	onuId: onuId
		        	},
		        dateType:'json',
		        success:function(response) {
		        	window.parent.closeWaitingDlg();
		            if (response == "success") {
		            	top.afterSaveOrDelete({
		       				title: I18N.COMMON.tip,
		       				html: '<b class="orangeTxt">' + '@ONU.restoresuccess@' + '</b>'
		       			});
		            	window.parent.getFrame("entity-" + onuId).reloadStore();
		            	cancelClick();
		            } else {
		            	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.restorefail@");
		            }
		        },
		        error:function() {
		        	window.parent.showMessageDlg("@COMMON.tip@", "@ONU.restorefail@");
		        },
		        cache:false
		    });
	     }
	 });
}
</script>
</head>
<body  class="newBody clear-x-panel-body" onload="authLoad()">
	<div id="header">
		 <%@ include file="navigator.inc"%> 
	</div>

	<div style="dislpay: none">
		<div id="detail">
			<div style="position:relative">
				<div class="rightTopPutPic">
					<img id="entityIcon" src="/images/${onu.icon64}" border=0 />
				</div>
				<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
					<tr>
						<td width="25%" class="rightBlueTxt">@COMMON.alias@:</td>					
						<td class="txtLeftEdge wordBreak"><div class="pR230">${onu.name}</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@COMMON.entityType@:</td>					
						<td class="txtLeftEdge wordBreak" id="displayType"><div class="pR230">${onu.typeName}</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">SN:</td>
						<td class="txtLeftEdge wordBreak"><div class="pR230">${onuAttribute.onuUniqueIdentification}</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ONU.upLinkName@:</td>
						<td class="txtLeftEdge wordBreak" ><div class="pR230">${olt.name }</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ONU.uplinkType@:</td>
						<td class="txtLeftEdge wordBreak" ><div class="pR230">${olt.typeName }</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@ONU.uplinkIp@:</td>
						<td class="txtLeftEdge wordBreak" ><div class="pR230">${olt.ip }</div></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@downlink.prop.mgmtstatus@:</td>
						<td class="txtLeftEdge wordBreak" ><div id="onuAdminStatus" class="pR230"></div></td>
					</tr>
					
					<tr>
						<td class="rightBlueTxt">@downlink.prop.testDistance@:</td>
						<td class="txtLeftEdge wordBreak" ><div class="pR230">${onuAttribute.onuTestDistance } m</div></td>
					</tr>
					
					<%-- <tr>
						<td class="rightBlueTxt">@ONU.softVersion@:</td>
						<td class="txtLeftEdge wordBreak" >${onuAttribute.onuSoftwareVersion }</td>
					</tr> --%>
					<tr>
						<td class="rightBlueTxt">@epon/EPON.hardwareVersion@:</td>
						<td class="txtLeftEdge wordBreak">${onuAttribute.topOnuHardwareVersion }</td>
					</tr>
					<%-- <tr id="wifiSoftVersion">
						<td class="rightBlueTxt">WIFI @ONU.softVersion@:</td>
						<td class="txtLeftEdge wordBreak" >${onuWanConfig.softVersion}</td>
					</tr>
					<tr id="wifiHardVersion">
						<td class="rightBlueTxt">WIFI @epon/EPON.hardwareVersion@:</td>
						<td class="txtLeftEdge wordBreak">${onuWanConfig.hardVersion}</td>
					</tr> --%>
					<tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.contact@:</td>                   
                        <td class="txtLeftEdge wordBreak"><div class="pR120">${onu.contact}</div></td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.location@:</td>                  
                        <td class="txtLeftEdge wordBreak"><div class="pR120">${onu.location}</div></td>
                    </tr>
                    <tr>
                        <td class="rightBlueTxt">@ENTITYSNAP.basicInfo.note@:</td>                  
                        <td class="txtLeftEdge wordBreak"><div class="pR120">${onu.note}</div></td>
                    </tr>
				</table>
			</div>
		</div>
	</div>
	
	<div id="tools">
		<dl class="btnGroupDl pB10">
			<Zeta:Button  id="reTopoOnu" ignoreLi="true" icon="miniIcoRefresh" onclick="refreshOnu();" >@network/NETWORK.reTopo@</Zeta:Button>
			<%-- <Zeta:Button  id="restore" ignoreLi="true" icon="miniIcoPower" onclick="restoreOnu();" checkOperatePower="true">@epon/ENTITYSNAP.tools.reset@</Zeta:Button> --%>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="modifyDevice()">@ONU.deviceInfo@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="onuOpticalAlarmHandler()" checkOperatePower="true">@EPON.optThrMgmt@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="addOnuToTopoHandler()">@COMMON.addToTopo@</Zeta:Button>
			<Zeta:Button  id="onuPerfCfg" ignoreLi="true" icon="miniIcoManager" onclick="showPerfCfg()">@ONU.perfCollectCfg@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoPower"   onclick="restoreHandler()" checkOperatePower="true">@COMMON.restore@</Zeta:Button>
			<%-- <Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="isolatedEnableHandler()" checkOperatePower="true">@downlink.menu.isolatedEnable@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="tempretureEnableHandler()" checkOperatePower="true">@downlink.menu.tempretureEnable@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="rstpCfgHandler()" checkOperatePower="true">@ONU.RSTPCfg@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="slaMgmtHandler()" checkOperatePower="true">@ONU.slaMgmt@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="macAgeHandler()" checkOperatePower="true">@downlink.menu.macAgingTime@</Zeta:Button> --%>
			<Zeta:Button  ignoreLi="true" icon="miniIcoRefresh" onclick="stasitcEnableHandler()" checkOperatePower="true">@ONU.ponPerfStastic@</Zeta:Button>
			<Zeta:Button  ignoreLi="true" icon="miniIcoGoogle" onclick="addBaiduMap()" checkOperatePower="true">@network/BAIDU.viewMap@</Zeta:Button>
            <Zeta:Button  ignoreLi="true" icon="miniIcoManager" onclick="macFiter()" checkOperatePower="true">@gpon/GPON.macFilter@</Zeta:Button>
			<dd>
				<a id="userAttention" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i
                 class="bmenu_eyes"></i>@resources/COMMON.attention@</span></a>
            </dd>
            <dd>
            	<a id="userAttentionCancel" onclick="onAttentionClick();" href="javascript:;" class="normalBtnBig"><span><i
                   class="bmenu_eyesClose"></i>@resources/COMMON.cancelAttention@</span></a>
            </dd>
            <dd>
				<a id="onuLaserOpen" onclick="operationOnuLaser();" href="javascript:;" class="normalBtnBig"><span><i
                 class="bmenu_eyes"></i>@ONU.openLaserSwitch@</span></a>
            </dd>
            <dd>
            	<a id="onuLaserClose" onclick="operationOnuLaser();" href="javascript:;" class="normalBtnBig"><span><i
                   class="bmenu_eyesClose"></i>@ONU.closeLaserSwitch@</span></a>
            </dd>
		</dl>
	</div>
    
    <div id="subInfo" style="dislpay: none">
    	<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="25%">@gpon/GPON.omccVersion@</td>
                <td id="onuTotal" width="25%">${gponOnuCapability.onuOMCCVersion }</td>
                <td class="rightBlueTxt" width="25%">@gpon/GPON.EthernetNumber@</td>
                <td>${gponOnuCapability.onuTotalEthNum}</td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@gpon/GPON.wifiPortNum@</td>
                <td>${gponOnuCapability.onuTotalWlanNum}</td>
                <td class="rightBlueTxt">@gpon/GPON.catvPortNum@</td>
                <td>${gponOnuCapability.onuTotalCatvNum}</td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@gpon/GPON.veipPortNum@</td>
                <td>${gponOnuCapability.onuTotalVeipNum}</td>
                <td class="rightBlueTxt">@gpon/GPON.ipHostNum@</td>
                <td>${gponOnuCapability.onuIpHostNum}</td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@gpon/GPON.FlowManagementMode@</td>
                <td><div id="onuTrafficMgmtOption"></div></td>
                <td class="rightBlueTxt">@gpon/GPON.gmePortNum@</td>
                <td>${gponOnuCapability.onuTotalGEMPortNum}</td>
            </tr>
            <tr>
           		<td class="rightBlueTxt">@gpon/GPON.potsNum@</td>
                <td>${gponOnuCapability.onuTotalPotsNum}</td>
                <td class="rightBlueTxt">@gpon/GPON.tContNum@</td>
                <td>${gponOnuCapability.onuTotalTContNum}</td>
            </tr>
            <tr>
            	<td class="rightBlueTxt">@gpon/GPON.ConnectionAbility@</td>
                <td colspan="3"><div id="onuConnect"></div></td>
            </tr>
            <!-- <tr>
            	<td class="rightBlueTxt">@gpon/GPON.qos@</td>
            	<td colspan="3"><div id="onuQosFix"></div></td>
            </tr> -->
        </table>
    </div>
    
    <div id="opticalInfo" style="dislpay: none">
    	<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="25%">@Optical.biasCurrent@:</td>
                <td id="onuBiasCurrent" width="25%"></td>
                <td class="rightBlueTxt" width="25%">@downlink.prop.voltage@:</td>
                <td id="onuWorkingVoltage"></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@ONU.tempretureOpt@:</td>
                <td id="tempreture"></td>
                <td class="rightBlueTxt">@ONU.recOptRate@:</td>
                <td id="receivedOpticalPower"></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@ONU.sendOptRate@:</td>
                <td id="onuTramsmittedOpticalPower"></td>
                <td class="rightBlueTxt">@ONU.ponOpticalRecv@:</td>
                <td id="ponTransmitPower"></td>
            </tr>
        </table>
    </div>
    <div id="software">
    	<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="25%">@gpon/GPON.Mirror1Version@</td>
                <td id="software1Version" width="25%"></td>
                <td class="rightBlueTxt" width="25%">@gpon/GPON.Mirror1Validity@</td>
                <td id="software1Valid"></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@gpon/GPON.Mirror1Activation@</td>
                <td id="software1Active"></td>
                <td class="rightBlueTxt">@gpon/GPON.Mirror1Commit@</td>
                <td id="software1Commited"></td>
            </tr>
           <tr>
                <td class="rightBlueTxt">@gpon/GPON.Mirror2Version@</td>
                <td id="software2Version"></td>
                <td class="rightBlueTxt">@gpon/GPON.Mirror2Validity@</td>
                <td id="software2Valid"></td>
            </tr>
            <tr>
                <td class="rightBlueTxt">@gpon/GPON.Mirror2Activation@</td>
                <td id="software2Active"></td>
                <td class="rightBlueTxt">@gpon/GPON.Mirror2Commit@</td>
                <td id="software2Commited"></td>
            </tr>
        </table>
    </div>
    
    <div id="runTime"></div>
    
    <div id="catvInfo" style="dislpay: none">
        <table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
            <tr>
                <td class="rightBlueTxt" width="100">@CATV.InputOpticalPower@@COMMON.maohao@</td>
                <td id="">${onuCatvInfo.rxPowerForunit}</td>
                <td class="rightBlueTxt" width="100">@CATV.OutputPower@@COMMON.maohao@</td>
                <td id="">${onuCatvInfo.rfOutVoltageForunit}</td>
            </tr>
            <tr>
                <td class="rightBlueTxt" width="100">@CATV.Voltage@@COMMON.maohao@</td>
                <td id="">${onuCatvInfo.voltageForunit}</td>
                <td class="rightBlueTxt" width="100">@CATV.Temperature@@COMMON.maohao@</td>
                <td id="">${onuCatvInfo.temperatureForunit}</td>
            </tr>
        </table>
    </div>
<script type="text/javascript" src="/js/jquery/zebra.js"></script>
<script type="text/javascript">
	//处理ie6和ie7不能自适应的问题;
	$(function(){
		$(".btnGroupDl dd").each(function(){
			$(this).css("float","none")
			var w = $(this).find("a").outerWidth();
			$(this).width(w);			
		})
		$(".btnGroupDl dd").css("float","left");
	}) 
</script>
</body>
</Zeta:HTML>  
