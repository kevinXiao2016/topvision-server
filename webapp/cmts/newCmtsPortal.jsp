<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ page import="com.topvision.ems.cmc.util.CmcConstants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    import js/tools/imgTool
    css css/white/disabledStyle
    import js/nm3k/Nm3kClock
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/nm3kClock.css" />
<!-- 内置css定义 -->
<style type="text/css">
#entityIcon{ padding-top: 80px;}
#portletTools .x-panel-body,#configFileMgmt .x-panel-mc,#tools { background:#fff;}
.mydiv {padding: 5px 10px 5px 10px;}
.mydiv div { float: left;  margin: 3px 10px 3px 0px;  }	
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<!-- 内置自定义js -->
<script type="text/javascript">
/*******************************************************
 * 变量定义及其初始化
 *******************************************************/
var cmcId = ${ cmcId };
var isSurportedGoogleMap = ' ${ isSurportedGoogleMap }';
var nodePath = ' ${ nodePath }';
var macAddr = ' ${ cmcAttribute.topCcmtsSysMacAddr }';
var cmcAttribute = ${cmcAttrJson};
var entityId = '${ cmcId }';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var dispatchInterval = 15000;
var timer = null;
/*
 * 记录时间控件的对象;
 * type类型：cir,grayCalendar,whiteCalendar;
 */
var oRunTime = {
	totalTime : Number(${ sysUpTime } / 1000),
	clock : null,//记录时间控件;
	language : '@COMMON.language@',
 	type : 'grayCalendar'
 }

/*******************************************************
 * 执行语句包括onReady/onload的执行语句，其后为onReady的方法定义
 *******************************************************/
 Ext.BLANK_IMAGE_URL = '/images/s.gif';
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
	 var portletDetail = {
 	        id:'portletDetail',
 	        title: I18N.SYSTEM.baseInfo,
 	        bodyStyle:'padding:8px',
 	        autoScroll:true,
 	        contentEl:'detail'
 	    }
	 //连续运行;
	 var portletSysUpTime = {
			id:'portletSysUpTime',
	        title: I18N.label.upTime,
	        bodyStyle: 'padding:10px',
	        autoScroll: true,
	        contentEl:'runTime'
	        //autoLoad:{text : I18N.WorkBench.loading, url : ('/cmc/showCmcUptimeByEntity.tv?cmcId='+cmcId+'&num='+ Math.random())}
	    }
	 //关联cm信息;
	 var portletCmInfo = {
			id:'portletCmInfo',
	        title: I18N.CCMTS.relaCMInfo,
	        bodyStyle: 'padding:10px',
	        autoScroll: true,
	        contentEl:'cmInfo'
	    }
	//上行端口信息
	    var upChannelInfo = new Ext.ux.Portlet({
	        id:'upChannelInfo',
	        tools: [{id: 'refresh',
	            handler: function(event, toolEl, panel) {
	                panel.getUpdater().update({disableCaching: true, url : '/cmts/channel/getUpChannelInfo.tv?cmcId=' + cmcId});
	            }
	        }],
	        title: I18N.CCMTS.upStreamPortInfo,
	        bodyStyle: 'padding:10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmts/channel/getUpChannelInfo.tv?cmcId=' + cmcId, text: I18N.WorkBench.loading, disableCaching: true}
	    });
		//下行端口信息
	    var downChannelInfo = new Ext.ux.Portlet({
	        id:'downChannelInfo',
	        tools: [{id: 'refresh',
	            handler: function(event, toolEl, panel) {
	                panel.getUpdater().update({disableCaching: true, url : '/cmts/channel/getDownChannelInfo.tv?cmcId=' + cmcId});
	            }
	        }],
	        title: I18N.CCMTS.downStreamPortInfo,
	        bodyStyle: 'padding:10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmts/channel/getDownChannelInfo.tv?cmcId=' + cmcId, text: I18N.WorkBench.loading, disableCaching: true}
	    });
	    //管理工具
	    portletTools = new Ext.ux.Portlet({
	    	id:'portletTools',
		        title: I18N.NETWORK.managementTools,
		        bodyStyle:'padding:0px',
		        autoScroll:true,
		        contentEl:'tools'
	    });
	  //端口统计
	    cmcPortInfo = new Ext.ux.Portlet({
	        id:'cmcPortInfo',
	        tools: [{id: 'refresh',
	            handler: function(event, toolEl, panel) {
	                panel.getUpdater().update({disableCaching: true, url : '/cmts/channel/getCmtsPortInfo.tv?cmcId=' + cmcId});
	            }
	        }],
	        
	        title: I18N.CMTS.portStatics,
	        bodyStyle: 'padding:10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmts/channel/getCmtsPortInfo.tv?cmcId=' + cmcId, text: I18N.WorkBench.loading, disableCaching: true}
	    });
	  //上行端口用户数统计
	    upChannelUserNum = new Ext.ux.Portlet({
	        id:'upChannelUserNum',
	        tools: [{id: 'refresh',
	            handler: function(event, toolEl, panel) {
	                panel.getUpdater().update({disableCaching: true, url : '/cmts/channel/getUpChannelUserNum.tv?cmcId=' + cmcId});
	            }
	        }],
	        title: I18N.CCMTS.upStreamPortUserCount,
	        bodyStyle: 'padding:5px 10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmts/channel/getUpChannelUserNum.tv?cmcId=' + cmcId, text: I18N.WorkBench.loading, disableCaching: true}
	    });
	    //下行端口用户数统计
	    downChannelUserNum = new Ext.ux.Portlet({
	        id:'downChannelUserNum',
	        tools: [{id: 'refresh',
	            handler: function(event, toolEl, panel) {
	                panel.getUpdater().update({disableCaching: true, url : '/cmts/channel/getDownChannelUserNum.tv?cmcId=' + cmcId});
	            }
	        }],
	        title: I18N.CCMTS.downStreamPortUserCount,
	        bodyStyle: 'padding:5px 10px',
	        autoScroll:true,
	        autoLoad: {url: '/cmts/channel/getDownChannelUserNum.tv?cmcId=' + cmcId, text: I18N.WorkBench.loading, disableCaching: true}

	    }); 
		//////////////////////////////////////////////////////////////布局必须从数据库中读取，因此提前分好左右2侧;
		 //左侧板块，从数据库读取;
		 var leftPartStr = '${cmtsPortalLeft}';
		 //右侧板块，从数据库读取;
		 var rightPartStr = '${cmtsProtalRight}';
		 //如果是第一次加载,就进行默认初始化
		 if(leftPartStr == '' && rightPartStr == ''){
			 leftPartStr = "portletDetail, portletSysUpTime, portletCmInfo,upChannelInfo,downChannelInfo";
			 rightPartStr = "portletTools,cmcPortInfo,upChannelUserNum,downChannelUserNum";
		 }
		 var leftPart = {};
		 leftPart.columnWidth = 0.5;
		 leftPart.style = "padding:10px 5px 10px 5px";
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

    if (isSurportedGoogleMap == 'false') {
        $("#googleMap").attr("disabled", true);
    }
     var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});
     var viewport = new Ext.Viewport({layout: 'border',
         items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
     });
	
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

  		}else{
  			leftPartStr = leftArr.toString();
  			rightPartStr = rightArr.toString();
  			$.ajax({
  				url: '/cmts/saveCmtsPortalView.tv',
  				cache:false, 
  				method:'post',
  				data: {
  					cmtsPortalLeft : leftPartStr, 
  					cmtsProtalRight : rightPartStr
  				},
  				success: function() {
  				},
  				error: function(){
  				}
  			});
  		};//end if else;
  	}
  	//$("#entityIcon").attr("src",'${ entity.icon }');
  	var newImgSrc = relaceImgSrc('${ entity.icon }');
	 loadImage(newImgSrc, function(){
		 $('.rightTopPutPic').html(this);
	 });
  	
  	
  	
});

/*******************************************************
 * 方法定义：操作，交互，菜单等归类组织在一起
 *******************************************************/

//基本信息管理
function cmtsBasicInfoMgmt(){
	window.top.createDialog('basicInfoMgmt', I18N.CCMTS.ccmtsBasicInfoMgmt, 600, 400,
			'/cmts/showCmtsBasicInfoConfig.tv?cmcId=' + cmcId, null, true, true);
}
//刷新CCMTS 
function refreshCcmts(){
	window.parent.showConfirmDlg("@COMMON.tip@", "@CCMTS.confirmRetopoCcmts@", function(button,text){
        if(button == "yes"){
        	window.parent.showWaitingDlg("@COMMON.waiting@", "@CCMTS.reTopoingCcmts@",'waitingMsg','ext-mb-waiting');
            $.ajax({
                url:'/cmts/refreshCmts.tv?entityId='+ entityId,
                type:'POST',
                dateType:'json',
                success:function(response){
                    if(response == "success"){
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CCMTS.refreshCmtsSuccess);
                        window.top.closeWaitingDlg();
                        top.afterSaveOrDelete({
               				title: '@COMMON.tip@',
               				html: '<b class="orangeTxt">@network/NETWORK.reTopoOk@</b>'
               			});
                        window.location.reload();
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
    });
}

function reload(){
	window.location.reload();
}
var totalTime =  ${ sysUpTime } / 1000;

$(document).ready(function(){
	setInterval("syTime()", 60000);
    refreshCmInfo();
})

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

function startChangeImageTimer(){
	
}

function stopChangeImageTimer(){
	
}
function showCmDetail(channelIndex, entityId ,channelType) {
	var cmtsType = EntityType.getCmtsType();
	var queryInitData = {
   		cmcDeviceStyle : cmtsType,
   		portal :'cmts', 
   		cmtsId : entityId,
   		channelIndex : channelIndex,
   		channelType : channelType
    };
	window.top.addView("cmListNew", I18N.CM.cmList, "apListIcon", encodeURI("/cmlist/showCmListPage.tv?queryInitDataStr="+JSON.stringify(queryInitData)), null, true);
}
function addView(id, title, icon, url) {
    window.parent.addView(id, title, icon, url,null,true);
}
//获取设备运行时间
function syTime() {
	Ext.Ajax.request({
		url:'/cmts/getCmtsUptimeByEntity.tv?cmcId=' + cmcId + '&num=' + Math.random(),
		method:"post",
		cache: false,
		success:function (response) {
			var json = Ext.util.JSON.decode(response.responseText);
			totalTime = json.sysUpTime / 1000;
			oRunTime.clock.update({startTime:totalTime});
		},
		failure:function () {
            //modify by Victor@20140725不需要弹出确认对话框，而且只要标签页打开，不管当前页是否在这个页面都弹出是不对的
			//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMC.tip.getDeviceRunTimeFailure);
		}
	});
}

function authLoad(){
    if(!operationDevicePower){
        $("#cmcBasicInfo").attr("disabled",true);
    }
    if(!refreshDevicePower){
        $("#refreshCmc").attr("disabled",true);
    }
}

//添加到百度地图
function addBaiduMap(){
	window.top.createDialog('modalDlg',
			"@network/BAIDU.viewMap@", 800, 600,
			'baidu/showAddEntity.tv?entityId=' + cmcId + "&typeId=" + cmcAttribute.cmcDeviceStyle + "&entityName=" + cmcAttribute.nmName, null, true, true);
}
</script>
</head>	
<body onload="authLoad();" class="newBody clear-x-panel-body whiteToBlack">
    <div id=header>
		<%@ include file="entity.inc"%>
	</div>
	<div id=detail style="dislpay: none">
		<div style="position:relative">
			<div class="rightTopPutPic">
				<img id="entityIcon" src="" border=0/>
			</div>
			<table class="dataTable zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
				<tr>
                    <td class="rightBlueTxt noWrap" width="120">@CMC.label.labelEntityType@:</td>
                    <td class="wordBreak"><div class="pR230">${ cmcAttribute.cmcDeviceStyleString }</div></td>
               </tr>
				<tr>
					<td class="rightBlueTxt noWrap" width="120">@RESOURCES/COMMON.alias@:</td>
					<td class="wordBreak"><div class="pR230">${ cmcAttribute.nmName }</div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt noWrap" width="120">@cmc/CMC.label.labelHardwareName@:</td>
					<td  class="wordBreak"><div class="pR230">${ cmcAttribute.topCcmtsSysName }</div></td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="rightBlueTxt noWrap">@cmc/CCMTS.entityLocation@:</td> -->
<%-- 					<td class="wordBreak"><div class="pR120">${ cmcAttribute.topCcmtsSysLocation }</div></td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="rightBlueTxt noWrap">@cmc/CCMTS.contactPerson@:</td> -->
<%-- 					<td class="wordBreak"><div class="pR120">${ cmcAttribute.topCcmtsSysContact }</div></td> --%>
<!-- 				</tr> -->
				<tr>
					<td class="rightBlueTxt noWrap">@cmc/CMC.text.ipaddress@:</td>
					<td class="wordBreak"><div class="pR230">${ entity.ip }</div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt noWrap">@cmc/CMC.label.SysDescr@:</td>
					<td class="wordBreak">
						<div class="pR230">
							${ cmcAttribute.topCcmtsSysDescr }
						</div>
					</td>
				</tr>
				<tr>
                    <td class="rightBlueTxt noWrap">@cmc/CMC.text.contact@:</td>
                    <td class="wordBreak"><div class="pR230">${ entity.contact }</div></td>
                </tr>
                <tr>
                    <td class="rightBlueTxt noWrap">@cmc/CMC.text.location@:</td>
                    <td class="wordBreak"><div class="pR230">${ entity.location }</div></td>
                </tr>
                <tr>
                    <td class="rightBlueTxt noWrap">@cmc/CMC.text.note@:</td>
                    <td class="wordBreak"><div class="pR230">${ entity.note }</div></td>
                </tr>
			</table>
		</div>
	</div>
	<div id=cmInfo style="dislpay: none">
        <table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
		<tr>
			<td class="rightBlueTxt" width="80">@CM.totalNum@:</td>
			<td id="cmNumTotal"> ${ cmcCmNumStatic.cmNumTotal }</td>
			<td class="rightBlueTxt" width="80">@CM.onlineNum@:</td>
           	<td id="cmNumOnline"> ${ cmcCmNumStatic.cmNumOnline }</td>
        </tr>
        <tr>
        	<td class="rightBlueTxt">@CM.activeCMCount@:</td>
            <td id="cmNumActive"> ${ cmcCmNumStatic.cmNumActive }</td>
            <td class="rightBlueTxt">@CM.offlineCMCount@:</td>
            <td id="cmNumOffline"> ${ cmcCmNumStatic.cmNumOffline }</td>
        </tr>
        <%-- <tr>
			<td class="rightBlueTxt">@CM.registedCMCount@:</td>
            <td id="cmNumRregistered"> ${ cmcCmNumStatic.cmNumRregistered }</td>
            <td class="rightBlueTxt">@CM.unregistedCMCount@:</td>
            <td id="cmNumUnregistered"> ${ cmcCmNumStatic.cmNumUnregistered }</td>
        </tr> --%>
        </table>
    </div>
	<div id=tools class="edge10 floatLeft">
		<dl class="leftFloatDl">
		<dd>
			<a id="cmcBasicInfo" onclick="cmtsBasicInfoMgmt();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoInfo"></i>@CCMTS.ccmtsBasicInfoMgmt@</span></a>
		</dd>
		<dd>
			<a id="refreshCmc" onclick="refreshCcmts();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoRefresh"></i>@network/NETWORK.reTopo@</span></a>
		</dd>
		<a id="baiduMap" onclick="addBaiduMap();" href="javascript:;" class="normalBtnBig"><span><i
                            class="miniIcoGoogle"></i>@network/BAIDU.viewMap@</span></a>
		</dl>
	</div>
	<div id="runTime"></div>
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