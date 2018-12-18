<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-plugin.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<style type="text/css">
.mydiv {
	padding: 5px 10px 5px 10px;
}

.mydiv div {
	float: left;
	margin: 3px 10px 3px 0px;
}

.myStatusDiv {
	padding: 5px 10px 5px 10px;
}

.myStatusDiv div {
	float: left;
	margin: 3px 10px 3px 0px;
	width: 150px;
	height: 150px
}
</style>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="../js/ext/pkgs/window.js"></script>
<script type="text/javascript" src="../js/ext/ux/Portal.js"></script>
<script type="text/javascript" src="../js/ext/ux/PortalColumn.js"></script>
<script type="text/javascript" src="../js/ext/ux/Portlet.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ofc/json2.js"></script>
<script type="text/javascript" src="/js/ofc/swfobject.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxvismode.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxmedia.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxmedia-ie.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxflash.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxchart.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxofc.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="resources"/>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
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
function tabShown() {
}
// for tab changed end

var entityId = <s:property value="entity.entityId"/>;
var entityName = '<s:property value="entity.name"/>';
var entityIp = '<s:property value="entity.ip"/>';
var agentInstalled = <s:property value="entity.agentSupport"/>;
var snmpSupport = <s:property value="entity.snmpSupport"/>;
var entityType = <s:property value="entity.type"/>;

var timer = null;
var dispatchTotal = 120000;
var dispatchInterval = 15000;
var syncount = 0;
var synMax = 2;
function startTimer() {
	if (timer == null) {
		timer = setInterval("doRefresh()", dispatchTotal);
	}
}
function stopTimer() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
function doRefresh() {
}
function doOnunload() {
	stopTimer();
}
function getZetaCallback() {
	return window.top.ZetaCallback;
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function closeWaitingDlg(title) {
	window.top.closeWaitingDlg(title);
}
function showErrorDlg() {
	window.top.showErrorDlg();
}
function showMessageDlg(title, msg, type) {
	window.top.showMessageDlg(title, msg, type);
}
function showConfirmDlg(title, msg, callback) {
	window.top.showConfirmDlg(title, msg, callback);
}
function showInputDlg(title, msg, callback, text, scope, multiline) {
	window.top.showInputDlg(title, msg, callback, text, scope, multiline);
}
function showTextAreaDlg(title, msg, callback) {
	window.top.showTextAreaDlg(title, msg, callback);
}
function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
	window.top.createDialog(id, title, width, height, url, icon, modal, closable, closeHandler);
}
function closeWindow(id) {
	window.top.closeWindow(id);
}
function setWindowVisible(id, visible) {
	window.top.setWindowVisible(id, visible);
}
function setWindowTitle(id, title) {
	window.top.setWindowTitle(id, title);
}
function getWindow(id) {
	window.top.getWindow(id);
}
function addView(id, title, icon, url, history) {
	window.top.addView(id, title, icon, url, history);
}
function setActiveTab(id) {
	window.top.setActiveTab(id);
}
function setActiveTitle(id, title) {
	window.top.setActiveTitle(id, title);
}
function getActiveFrameById() {
	return window.top.getActiveFrameById();
}
function getActiveFrame() {
	return window.top.getActiveFrame();
}
function getFrameById(frameId) {
	return window.top.getFrameById(frameId);
}
function getFrame(frameId) {
	return window.top.getFrame(frameId);
}
function getPropertyFrame() {
	return window.top.getPropertyFrame();
}
function getNavigationFrame() {
	return window.top.getNavigationFrame();
}
function getMenuFrame() {
	return window.top.getNavigationFrame();
}
function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
}
function locateEntity(entityIp) {
	window.top.locateEntityByIp(entityIp);
}
function showPing(entityId, entityIp) {
	createDialog("modalDlg", 'Ping ' + entityIp, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
}
function onMibbleBrowserClick(entityId, entityIp){
	var entity = getSelectedEntity();
	if(entity)
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv?host="+entityIp)
	else
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv")
}
function onDiscoveryAgainClick(entityId, entityIp) {
	window.top.discoveryEntityAgain(entityId, entityIp, function() {
		window.top.onRefreshClick();
	});
}
function onLockClick() {
}
function onShutdownClick() {
}
function onUnloadAgentClick() {
}
function onNewMonitorClick() {
}
function cancelManagement() {
	showConfirmDlg(I18N.NETWORK.tip, I18N.NETWORK.comfirmCancelManegeEntity, function(type) {
		if (type == 'no') {return;}
		var entityIds = [];
		entityIds[0] = entityId;
		Ext.Ajax.request({url: '../entity/cancelManagement.tv',
		   success: function() {
		      location.href = 'showEntitySnapJsp.tv?entityId=' + entityId;
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {entityIds : entityIds}
		});			
	});
}
function onDiscoveryAgainClick(entityId, entityIp) {
	window.top.discoveryEntityAgain(entityId, entityIp, function() {
		window.top.onRefreshClick();
	});
}
function createExtOFC2Comp(id, url){
	var ofc = new Ext.ux.Chart.OFC.Component({
        height	: 250,
        width	: 250,
        renderTo: 'div_' + id,
        chartCfg:{ 
            id:id,
            autoSize : true,
            renderOnResize	: false,
            disableCaching  : true
           }
            ,loadMask  : {msg:'Gathering Chart Data'}
            ,autoMask  : true
            ,chartURL  : '/cmc/images/open-flash-chart.swf'
            ,dataURL   : url
            ,previews  :  new Array()
    });
    return ofc;
}
function click1(d){
	var url1 = '../../alert/loadChart.tv?entityId='+entityId+'&day='+d+'&c='+Math.random();
	Ext.Ajax.request({url: url1,
		   success: function(response) {
			   var json = Ext.util.JSON.decode(response.responseText);
			   Ext.getDom("performanceChart").load(JSON.stringify(json));
		   },
		   failure: function() {
			   window.top.showErrorDlg();}
	});	
	var url2 = '../../alert/loadEntityAvailability.tv?entityId='+entityId+'&day='+d+'&c='+Math.random();
	Ext.Ajax.request({url: url2,
		   success: function(response) {
			var json = Ext.util.JSON.decode(response.responseText);
			Ext.fly('subTime').dom.innerHTML = json.subTime;
			Ext.fly('percent').dom.innerHTML = json.percent;
		   },
		   failure: function() {
			   window.top.showErrorDlg();}
	});	
}
var portlets = [];
var urls = [];
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
 	var columns = 2;
 	var columnWidth = (columns == 3 ? .33 : (columns == 2 ? .5 : 1));
 	var size = 10;
 	if (columns > size) {
 		columns = size;
 	}

    var portletItems = [];
    portletItems[0] =  {
        columnWidth:.5,
        style:'padding:15px 10px 15px 15px',
        items:[{
	        id:'portletDetail',
	        title: I18N.summaryTitle,
	        bodyStyle:'padding:8px',
	        autoScroll:true,
	        contentEl:'detail'
	    	},{
	    		id:'panel',
                title: I18N.entityPortal.Panel,
                bodyStyle:'padding:8px',
                autoScroll:true,
                autoLoad:{text: I18N.entityPortal.loading, url:('../cmc/detail/showPanel.jsp')},
                tools: [
                    {
                        id:'refresh',
                        qtip: I18N.MENU.refresh,
                        handler: function(event, toolEl, panel) {
                            panel.getUpdater().update({url : ('../cmc/detail/showPanel.jsp')});
                    }
               }]
	     }]
    };

	var leftItems = portletItems[0].items;
    
    if (entityType == 72 || (entityType == 127 && agentInstalled)) {
	    leftItems[leftItems.length] = {
	        id:'portletDisk',
			tools: [
				{
		        id:'refresh',
		        qtip: I18N.MENU.refresh,
		        handler: function(event, toolEl, panel){
					panel.getUpdater().update({url : ('../portal/getDiskByEntityId.tv?entityId='+entityId)});
		        }
		    }],		        
	        title: I18N.entityPortal.DiskInformation,
	        bodyStyle:'padding:8px',
	        autoScroll:true,
			autoLoad:{text: I18N.entityPortal.loading, url:('../portal/getDiskByEntityId.tv?entityId='+entityId)}
	    };
    }

	var performanceComp = createExtOFC2Comp("performanceChart", '../../alert/loadChart.tv?entityId=' + entityId);
	
    portletItems[1] =  {
        columnWidth : .5,
        style:'padding:15px 10px 15px 15px',
        items:[{
	        id:'portletTools',
	        title: I18N.NETWORK.managementTools,
	        bodyStyle:'padding:0px',
	        autoScroll:true,
	        contentEl:'tools'
	    },
	    <s:if test="entity.snmpSupport">
	    {
			id:'portletSysUpTime',
	        tools: [
				{
		        id:'refresh',
		        qtip: I18N.MENU.refresh,
		        handler: function(event, toolEl, panel){
					panel.getUpdater().update({url : ('../portal/getSysuptimeByEntity.tv?entityId='+entityId)});
		        }
		    }],	        
	        title: I18N.entityPortal.uptime,
	        bodyStyle: 'padding:8px',
	        autoScroll: true,
	        autoLoad:{text : I18N.entityPortal.loading, url : ('../portal/getSysuptimeByEntity.tv?entityId='+entityId)}
	    }, 
	    </s:if>
		{
	        id:'performance',
                layout:'border',
                title: I18N.entityPortal.Availability,
                bodyStyle:'padding:8px',
                autoScroll:true,
                height: 300,
                items: [{
    					region : 'west',
    					bodyStyle:'filter:Alpha(opacity=100);',
    					width:280,
    					contentEl : 'div_panel_c_performance',
    					items:[performanceComp]
    				},{
    					region : 'center',
    					width:100,
    		            contentEl:'div_panel_t_performance'
    				}]
	           }]
    	};

	var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});

    var viewport = new Ext.Viewport({layout: 'border',
        items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    });
    
    startTimer();
});
</script>
</head>
<body onload="doOnunload()">
	<div id=header height=30px
		style="margin-left: 15px; margin-right: 15px; margin-top: 15px">
		<%@ include file="entity.inc"%>
	</div>
	<div style="dislpay: none">
		<div id=detail>
			<table width=100% cellspacing=5>
				<tr>
					<td width=80><fmt:message key="label.name" bundle="${resources}" /></td>
					<td><s:property value="entity.sysName" />
					</td>
				<tr>
					<td width=80><fmt:message key="port.ifAlias" bundle="${resources}" /></td>
					<td><s:property value="entity.name" />
					</td>
					<td rowspan=4 align=center><img id="entityIcon"
						src="<s:property value="entity.icon"/>" border=0>
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.className" bundle="${resources}" /></td>
					<td><s:property value="entity.typeName" />
					</td>
				</tr>
				<s:if test="entity.type==72 || entity.type==127">
					<tr>
						<td><fmt:message key="label.os" bundle="${resources}" /></td>
						<td><s:property value="entity.modelName" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td><fmt:message key="label.model" bundle="${resources}" /></td>
						<td><s:property value="entity.modelName" />
						</td>
					</tr>
				</s:else>
				<tr>
					<td><fmt:message key="label.vendor" bundle="${resources}" /></td>
					<td><s:property value="entity.corpName" />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="NETWORK.ipHeader" bundle="${resources}" />:</td>
					<td colspan=2><s:property value="entity.ip" />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="label.mac" bundle="${resources}" /></td>
					<td colspan=2><s:property value="entity.mac" />
					</td>
				</tr>
				<s:if test="entity.snmpSupport">
					<tr>
						<td>SNMP:</td>
						<td colspan=2><fmt:message key="label.yes" bundle="${resources}" /></td>
					</tr>
					<tr>
						<td>OID:</td>
						<td colspan=2><s:property value="entity.sysObjectID" />
						</td>
					</tr>
				</s:if>
				<s:else>
					<tr>
						<td>SNMP:</td>
						<td colspan=2><fmt:message key="label.no" bundle="${resources}" /></td>
					</tr>
				</s:else>
				<s:if test="entity.type==72||entity.type==127">
					<tr>
						<td>Agent:</td>
						<td colspan=2><s:if test="entity.agentInstalled"><fmt:message key="label.yes" bundle="${resources}" /></s:if>
							<s:else><fmt:message key="label.no" bundle="${resources}" /></s:else>
						</td>
					</tr>
				</s:if>
				<tr>
					<td><fmt:message key="label.location" bundle="${resources}" /></td>
					<td colspan=2><s:property value="entity.location" />
					</td>
				</tr>
				<tr>
					<td><fmt:message key="NETWORK.SysDescr" bundle="${resources}" /></td>
					<td colspan=2><textarea readonly class=iptxa rows=5
							style="overflow: auto; width: 100%">
							<s:property value="entity.sysDescr" />
						</textarea>
					</td>
				</tr>
				<tr>
					<td colspan=3 align=right><a class="MY-LINK"
						href="../entity/showEditEntityJsp.tv?module=2&entityId=<s:property value="entity.entityId"/>"><fmt:message key="entityPortal.Moreconfigurationinformation" bundle="${resources}" /></a>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div id=tools class=mydiv>
	    <div>
	        <button class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onClick="onDiscoveryAgainClick(entityId, entityIp);"
	                ><fmt:message key="entityPortal.Synchronizedevice" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button class=BUTTON120
	            onMouseOver="this.className='BUTTON_OVER120'"
	            onMouseOut="this.className='BUTTON120'"
	            onmousedown="this.className='BUTTON_PRESSED120'"
	            onClick="cancelManagement();"><fmt:message key="NETWORK.cancelManageEntity" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button id="downloadconfig" class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onClick="window.top.showModalDlg('<fmt:message key="entityPortal.Synchronizedeviceconfig" bundle="${resources}" />', 380, 240, '../cmc/detail/downloadCfgDlg.jsp')"><fmt:message key="entityPortal.Synchronizedeviceconfig" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button id="uploadconfig" class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onclick="window.top.showModalDlg('<fmt:message key="entityPortal.Issuedofflineconfiguration" bundle="${resources}" />', 380, 240, '../cmc/detail/uploadCfgDlg.jsp')"><fmt:message key="entityPortal.Issuedofflineconfiguration" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onClick="onAddToGoogleMaps();"><fmt:message key="entityPortal.linkgooglemap" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onClick="locateEntity(entityIp);"><fmt:message key="NETWORK.locateEntity" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onclick="showPing();">Ping<fmt:message key="port.adminTesting" bundle="${resources}" />
	        </button>
	    </div>
	    <div>
	        <button class=BUTTON120
	                onMouseOver="this.className='BUTTON_OVER120'"
	                onMouseOut="this.className='BUTTON120'"
	                onmousedown="this.className='BUTTON_PRESSED120'"
	                onclick="showPing();">MibbleBrowser
	        </button>
	    </div>
	    <br>
	    <br>
	</div>
	<div id="div_panel_t_performance">
		<a href="#" id="one" onclick="click1(1)">24H</a>&nbsp;|&nbsp;
		<a href="#" id="seven" onclick="click1(7)">7D</a>&nbsp;|&nbsp;
		<a href="#" id="thirty" onclick="click1(30)">30D</a><br>
			<fmt:message key="entityPortal.ConnectivityTotaltime" bundle="${resources}" /><label id="subTime"></label><br>
			<fmt:message key="entityPortal.Connectivitypercentage" bundle="${resources}" /><label id="percent"></label>
	</div>
	<div id="div_panel_c_performance">
		<div id="div_performanceChart"></div>
	</div>
</body>
</html>
