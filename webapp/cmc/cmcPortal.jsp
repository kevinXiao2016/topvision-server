<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>CCMTS-8800B Device Snapshot</title>
<%@include file="/include/cssStyle.inc" %>
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 自定义js引入 -->
<script type="text/javascript" src="/visifire/Visifire.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/window.js"></script>
<script type="text/javascript" src="/js/ext/ux/Portal.js"></script>
<script type="text/javascript" src="/js/ext/ux/PortalColumn.js"></script>
<script type="text/javascript" src="/js/ext/ux/Portlet.js"></script>

<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<!-- 内置css定义 -->
<style type="text/css">
.mydiv {
	padding: 5px 10px 5px 10px;
}

.mydiv div {
	float: left;
	margin: 3px 10px 3px 0px;
}
</style>
<!-- 内置自定义js -->
<script type="text/javascript">
/*******************************************************
 * 变量定义及其初始化
 *******************************************************/
var cmcId = <s:property value="cmcId"/>;
var cmcType = <s:property value="cmcType"/>;
//var entity = <s:property value="entity"/>;
var entityId = <s:property value="entityId"/>;
var entityIp = '<s:property value="entity.ip"/>';
var isSurportedGoogleMap = '<s:property value="isSurportedGoogleMap"/>';
var width = Ext.getBody().getWidth();

/*******************************************************
 * 执行语句包括onReady/onload的执行语句，其后为onReady的方法定义
 *******************************************************/
 Ext.BLANK_IMAGE_URL = '/images/s.gif';
 Ext.onReady(function(){
	 //界面portal
     var portletItems = [];
     portletItems[0] =  {
         columnWidth: .49,
         style:'padding:15px 10px 15px 15px',
         items:[{
 	        id:'portletDetail',
 	        title: I18N.text.baseInfo,
 	        bodyStyle:'padding:8px',
 	        autoScroll:true,
 	        contentEl:'detail'
 	    }, {
 			id:'portletSysUpTime',
 	        title: I18N.CMC.title.runningTime,
 	        bodyStyle: 'padding:8px',
 	        autoScroll: true,
 	        autoLoad:{text : I18N.CMC.text.loading, url : ('/cmc/showCmcUptimeByEntity.tv?entityId='+entityId+'&num='+ Math.random())}
 	    }/* , {
 			id:'portletCmInfo',
 	        title: I18N.CMC.title.relatedCMInformation,
 	        bodyStyle: 'padding:8px',
 	        autoScroll: true,
 	        contentEl:'cmInfo'
 	    } */]
     };

     portletItems[1] =  {
         columnWidth : .49,
         style:'padding:15px 10px 15px 15px',
         items:[
         {
 	        id:'portletTools',
 	        title: I18N.CMC.title.managementTool,
 	        bodyStyle:'padding:0px',
 	        autoScroll:true,
 	        contentEl:'tools'
 	    },
 	    {
 	        id:'portletPerformace',
 	        title: I18N.CMC.title.deviceStatusInfo,
 	        bodyStyle:'padding:0px',
 	        autoScroll:true,
 	        contentEl:'cmcStatusInfo'
 	    }]
     	};
  	if(isSurportedGoogleMap == 'false'){
  		$("#googleMap").attr("disabled",true);
  	  	}
 	 var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});
     var viewport = new Ext.Viewport({layout: 'border',
         items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
     });
 });

/*******************************************************
 * 方法定义：操作，交互，菜单等归类组织在一起
 *******************************************************/
 function showWaitingDlg(title, icon, text, duration) {
		window.top.showWaitingDlg(title, icon, text, duration);
	}
//刷新设备
function onDiscoveryAgainClick(cmcId, entityIp, cmcType) {
	window.parent.showWaitingDlg(I18N.CMC.tip.waiting, String.format(I18N.CMC.tip.refreshingCcmts, entityIp),'waitingMsg','ext-mb-waiting');
    Ext.Ajax.request({url:"/cmc/discoveryCmcAgain.tv",timeout: 600000, success: function () {
        window.parent.closeWaitingDlg();
        window.parent.onRefreshClick();
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsSuccess);
    }, failure: function () {
        window.parent.closeWaitingDlg();
        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsFail);
    }, params: {cmcId : cmcId, cmcType : cmcType}});
} 
//关联google地图
function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
}
//ping测试
function showPing() {
	window.parent.createDialog("modalDlg", 'Ping ' + entityIp, 600, 400,
		"/entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
}
//traceroute
function onTracertClick() {
	if (entityIp) {
		window.parent.createDialog("modalDlg", 'Tracert' + " - " + entityIp, 600, 400,
		"/entity/runCmd.tv?cmd=tracert&ip=" + entityIp, null, true, true);
	}
}
//本地telnet
function showTelnet() {
	window.open('telnet://' + entityIp, 'ntelnet' + entityId);
}
//软件升级
function updateCmc(){
	var ftpServiceEnable = true;
	if(ftpServiceEnable){
        window.parent.createDialog("showCmcUpdate", I18N.CMC.title.updateDevice, 320, 240, "/cmc/showCmcUpdate.tv?cmcId="+cmcId, null, true, true);
    	}else {
    		ftpServiceNoEnable();
    	}
}
//ftp服务无法启动时调用，被updateCmc调用
function ftpServiceNoEnable(){
	 window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.ftpServerNotStart,null);  
}
//设备重启
function resetCmc() {
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.sureToResetCcmts, function(type) {
		if (type == 'no') {return;}
		window.parent.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.refreshingMessage, 'ext-mb-waiting');
		//Ext.Ajax.request({url:"/cmc/resetCmc.tv", success: function (response) {
		Ext.Ajax.request({url:"/cmc/resetCmc.tv", success: function (response) {
			window.parent.closeWaitingDlg();
			if(response.responseText == "success"){
		        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsSuccess);
			}else{
				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsFail);
			}
	    }, failure: function (response) {
	        window.parent.closeWaitingDlg();
	        window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.resetCcmtsFail);
	    }, params: {cmcId : cmcId}});
	});
}
//取消管理
function cancelManagement() {
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.confirmCancelManage, function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = entityId;
		Ext.Ajax.request({url: '/entity/cancelManagement.tv',
		   success: function() {
		      location.href = '/cmc/entityPortalCancel.tv?entityId='+entityId+'&cmcId='+cmcId;
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});			
	});
}
//保存配置
function saveConfig() {
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.confirmSaveConfig, function(type) {
		if (type == 'no') {return;}
		showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.tip.savingConfig, 'ext-mb-waiting');
        $.ajax({
            url: '/cmc/saveConfig.tv',
            type: 'POST',
            data: "entityId=" + entityId+"&cmcId=" + cmcId,
            success: function(json) {
                if (json.message) {
                	window.parent.closeWaitingDlg();
            		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, json.message);
            	} else {
            		window.parent.closeWaitingDlg();
            		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigSuccess);
            	}
            }, error: function(json) {
            	window.parent.closeWaitingDlg();
            	window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.saveConfigFail);
        	}, timeout:180000,dataType: 'json', cache: false
        });
	});
}
//dol轮询设置
function setDolPolling(){
	window.top.createDialog('perfConfig', I18N.CMC.title.dolPollingSet, 300, 180,
			'/cmcperf/showDolPerfConf.tv?entityId=' + entityId+"&cmcId=" + cmcId+"&cmcType=" + cmcType, null, true, true);
}
var totalTime = <s:property value="sysUpTime"/>;
//每秒改变时间图片
setInterval("changeImage()", 1000);
//每五分钟一次重新获取系统运行时间
setInterval("syTime()", 300000);
var tempTime = parseInt(totalTime /100);
$(changeImage());
//更换时间图片
function changeImage() {
	tempTime++;
    var day = parseInt(tempTime / 60 / 60 / 24);
    var hour = parseInt((tempTime / 60 / 60) % 24);
    var min = parseInt((tempTime / 60 ) % 60);
    var sec = parseInt((tempTime) % 60);
    var day4 = parseInt(day / 1000);
    var day3 = parseInt(day % 1000 / 100);
    var day2 = parseInt(day % 100 / 10);
    var day1 = parseInt(day % 10);
    var hour2 = parseInt(hour / 10);
    var hour1 = parseInt(hour % 10);
    var min2 = parseInt(min / 10);
    var min1 = parseInt(min % 10);
    var sec2 = parseInt(sec / 10);
    var sec1 = parseInt(sec % 10);
    if (day4 == 0) {
        $("#day4").hide();
    } else {
        $("#day4").show();
        $("#day4").attr("src","/images/"+day4+".gif");
    }
    if (day3 == 0 && day4 == 0) {
        $("#day3").hide();
    } else {
        $("#day3").show();
        $("#day3").attr("src","/images/"+day3+".gif");
    }
    if (day2 == 0 && day3 == 0 && day4 == 0) {
        $("#day2").hide();
    } else {
        $("#day2").show();
        $("#day2").attr("src","/images/"+day2+".gif");
    }
    $("#day1").attr("src","/images/"+day1+".gif");
    if (hour2 == 0) {
        $("#hour2").hide();
    } else {
        $("#hour2").show();
        $("#hour2").attr("src","/images/"+hour2+".gif");
    }
    $("#hour1").attr("src","/images/"+hour1+".gif");
    if (min2 == 0) {
        $("#min2").hide();
    } else {
        $("#min2").show();
        $("#min2").attr("src","/images/"+min2+".gif");
    }
    $("#min1").attr("src","/images/"+min1+".gif");
    if (sec2 == 0) {
        $("#sec2").hide();
    } else {
        $("#sec2").show();
        $("#sec2").attr("src","/images/"+sec2+".gif");
    }
    $("#sec1").attr("src","/images/"+sec1+".gif");
}
//获取设备运行时间
function syTime() {
	Ext.Ajax.request({
		url:'/cmc/getCmcUptimeByEntity.tv?cmcId='+cmcId+'&num='+ Math.random(),
		method:"post",
		cache: false,
		success:function (response) {
			var json = Ext.util.JSON.decode(response.responseText);
			totalTime = json.sysUpTime;
		},
		failure:function () {
			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.getSysUpTimeFail);
		}});
}
</script>
</head>
<body onload="">
	<div id=header height=30px
		style="margin-left: 10px; margin-right: 10px; margin-top: 15px">
		<%@ include file="entity.inc"%>
	</div>
	<div id=detail style="dislpay: none">
		<table width=100% cellspacing=5>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.deviceName'/>:</td>
				<td width=10></td>
				<td><s:property value="cmcAttribute.nmName"/></td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.alias'/>:</td>
				<td width=10></td>
				<td><s:property value="entity.name"/></td>
			    <td rowspan=3 align=center>
					<img id="entityIcon" src="/epon/image/cmc/cmcIcon.png" border=0>
				</td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.type'/>:</td>
				<td width=10></td>
				<td><s:property value="cmcAttribute.cmcDeviceStyleString"/></td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.devicelocation'/>:</td>
				<td width=10></td>
				<td><s:property value="cmcAttribute.topCcmtsSysLocation"/></td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.contactPerson'/>:</td>
				<td width=10></td>
				<td><s:property value="cmcAttribute.topCcmtsSysContact"/></td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CCMTS.manageIP'/>:</td>
				<td width=10></td>
				<td colspan=2><s:property value="entity.ip"/></td>
			</tr>
			<tr>
				<td width=80 align="right"><fmt:message bundle='${cmc}' key='CMC.label.sysDescr'/>:</td>
				<td width=10></td>
				<td colspan=2>
					<textarea disabled rows=3 style="width: 100%"><s:property value="cmcAttribute.topCcmtsSysDescr"/></textarea>
				</td>
			</tr>
			<tr>
				<%-- <td colspan=4 align=right>
					<a class="MY-LINK" href="/cmc/config/showCmcConfig.tv?module=2&cmcId=<%= cmcId %>&entityId=<%= entityId %>"><fmt:message bundle='${cmc}' key='CMC.label.moreConfigInfo'/>>></a>
				</td> --%>
			</tr>
		</table>
	</div>
	<div id=tools class=mydiv>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="onDiscoveryAgainClick(cmcId, entityIp, cmcType);"><fmt:message bundle='${cmc}' key='CMC.button.refreshCcmt'/></button>
		</div>
		<div>
			<button id=googleMap class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="onAddToGoogleMaps();"><fmt:message bundle='${cmc}' key='CMC.button.relatedToGoogleMap'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onclick="showPing();"><fmt:message bundle='${cmc}' key='CMC.button.pingTest'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onclick="onTracertClick(entityIp);">TraceRoute</button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="showTelnet();"><fmt:message bundle='${cmc}' key='CMC.button.telnet'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="updateCmc();"><fmt:message bundle='${cmc}' key='CMC.button.updateDevice'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="resetCmc();"><fmt:message bundle='${cmc}' key='CCMTS.resetCMC'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="cancelManagement();"><fmt:message bundle='${cmc}' key='CMC.button.cancelManage'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="saveConfig();"><fmt:message bundle='${cmc}' key='CMC.button.saveConfig'/></button>
		</div>
		<div>
			<button class=BUTTON120 onMouseOver="this.className='BUTTON_OVER120'"
				onMouseOut="this.className='BUTTON120'"
				onmousedown="this.className='BUTTON_PRESSED120'"
				onClick="setDolPolling();"><fmt:message bundle='${cmc}' key='CMC.button.dolPollingSet'/></button>
		</div>
		<br><br>
	</div>
	<div id=cmcStatusInfo style="dislpay: none">
		<table width=100%>
			<tr>
				<td align=center>
					<div>
						<img id="flashImg"
							src="/cmc/getFlashUsageByEntityId.tv?cmcId=<s:property value="cmcId"/>"
							border=0 hspace=5>
					</div>
					<div>
						<img id="cpuImg"
							src="/cmc/getCpuUsageByEntityId.tv?cmcId=<s:property value="cmcId"/>"
							border=0 hspace=5>
					</div>
					<div>
						<img id="memImg"
							src="/cmc/getMemoryUsageByEntityId.tv?cmcId=<s:property value="cmcId"/>"
							border=0 hspace=5>
					</div>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>