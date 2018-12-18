<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-plugin.css"/>
<link rel="stylesheet" type="text/css"
      href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css"
      href="../css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
    tr.col1 {
    }
    tr.col2 {
        background: #c9d7f1;
    }    
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
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/ext-foundation.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/cmp-foundation.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/ext-dd.js"></script>
<script type="text/javascript" src="/js/ext/pkgs/window.js"></script>
<script type="text/javascript" src="/js/ext/ux/Portal.js"></script>
<script type="text/javascript" src="/js/ext/ux/PortalColumn.js"></script>
<script type="text/javascript" src="/js/ext/ux/Portlet.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ofc/json2.js"></script>
<script type="text/javascript" src="/js/ofc/swfobject.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxvismode.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxmedia.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxmedia-ie.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxflash.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxchart.js"></script>
<script type="text/javascript" src="/js/ext/ux.media/uxofc.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=c.getUser().getLanguage() %>"></script>
<script>
var cmcId = <s:property value="cmcBaseInfo.entity.cmcId"/>;
var entityName = '<s:property value="cmcBaseInfo.entity.name"/>';
var entityIp = '<s:property value="cmcBaseInfo.entity.ip"/>';
var agentInstalled = <s:property value="cmcBaseInfo.entity.agentSupport"/>;
var snmpSupport = <s:property value="cmcBaseInfo.entity.snmpSupport"/>;
var entityType = <s:property value="cmcBaseInfo.entity.type"/>;
var portlets = [];
var urls = [];

function click1(d){
	var url1 = '../../alert/loadChart.tv?cmcId='+cmcId+'&day='+d+'&c='+Math.random();
	Ext.Ajax.request({url: url1,
		   success: function(response) {
			   var json = Ext.util.JSON.decode(response.responseText);
			   var pingComp = Ext.getDom("pingChart");
			   pingComp.load(JSON.stringify(json));
		   },
		   failure: function() {
			   window.top.showErrorDlg();}
		});	
	var url2 = '../../alert/loadEntityAvailability.tv?cmcId='+cmcId+'&day='+d+'&c='+Math.random();
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
Ext.BLANK_IMAGE_URL = '../../images/s.gif';
Ext.onReady(function() {
    var columns = 2;
    var columnWidth = (columns == 3 ? .33 : (columns == 2 ? .5 : 1));
    var size = 10;
    if (columns > size) {
        columns = size;
    }
    var portletItems = [];
    portletItems[0] = {
        columnWidth:columnWidth,
        style:'padding:15px 10px 15px 15px',
        items:[
            {
                id:'portletDetail',
                title: I18N.text.baseInfo,
                bodyStyle:'padding:8px',
                autoScroll:true,
                contentEl:'detail'
            },
            {
                id:'panel',
                title: I18N.CMC.title.panel,
                bodyStyle:'padding:8px',
                autoScroll:true,
                autoLoad:{text: I18N.CMC.text.onloading, url:('../cmc/showPanel.jsp')},
                tools: [
                    {
                        id:'refresh',
                        qtip: I18N.CMC.button.refresh,
                        handler: function(event, toolEl, panel) {
                            panel.getUpdater().update({url : ('../cmc/showPanel.jsp')});
                        }
                    }]
            }
        ]
    };
    var pingComp = createExtOFC2Comp("pingChart",'../../alert/loadChart.tv?cmcId='+cmcId);
    portletItems[1] = {
        columnWidth : columnWidth,
        style:'padding:15px 10px 15px 15px',
        items:[
            {
                id:'portletTools',
                title: I18N.CMC.title.manageTool,
                bodyStyle:'padding:0px',
                autoScroll:true,
                contentEl:'tools'
            },{
                id:'person',
                title: I18N.CMC.title.responsiblePerson,
                bodyStyle:'padding:8px',
                autoScroll:true,
                autoLoad:{text: I18N.CMC.text.onloading, url:('../entity/loadEntityRelationUser.tv?cmcId='+cmcId+'&dateTime='+(new Date().getTime()-60*60*24*7)),disableCaching:true,scripts:true},
                tools: [
                    {
                        id:'refresh',
                        qtip:I18N.CMC.button.refresh,
                        handler: function(event, toolEl, panel) {
                            panel.getUpdater().update({url : ('../entity/loadEntityRelationUser.tv?cmcId='+cmcId+'&dateTime='+(new Date().getTime()-60*60*24*7)),scripts:true});
                        }
                    }]
            },{
                id:'ping',
                layout:'border',
                title: I18N.CMC.title.usability,
                bodyStyle:'padding:8px',
                autoScroll:true,
                height: 300,
                items: [
        				{
    					region : 'west',
    					bodyStyle:'filter:Alpha(opacity=100);',
    					width:300,
    					contentEl : 'div_panel_c_ping',
    					items:[pingComp]
    				},{
    					region : 'center',
    					width:100,
    		            contentEl:'div_panel_t_ping'
    					}
            	]
            }
        ]
    };
    var headerPanel = new Ext.BoxComponent({region: 'north', el: 'header', margins: '0 0 0 0', height: 40, maxSize: 40});
    var viewport = new Ext.Viewport({layout: 'border',
        items:[headerPanel, {xtype: 'portal', region:'center', margins: '0 0 0 0', border: false, items: portletItems}]
    });
});

function showTelnet(cmcId, entityIp) {
	var hwin =
            window.open('telnet://' + entityIp, 'telnet' + cmcId);
    hwin.close();
}

function showPing() {
	createDialog("modalDlg", 'Ping ' + cmcId, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + entityIp, null, true, true);
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
function createWindow(id, title, width, height, url, icon, modal, closeHandler, resized) {
	return window.top.createWindow(id, title, width, height, url, icon, modal, closeHandler, resized);
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
function locateEntity(entityIp) {
	window.top.locateEntityByIp(entityIp);
}

function onDiscoveryAgainClick(cmcId, entityIp) {
	window.top.discoveryEntityAgain(cmcId, entityIp, function() {
		window.top.onRefreshClick();
	});
}
function cancelManagement() {
	showConfirmDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.confirmCancelManageEntity, function(type) {
		if (type == 'no') {return;}
		var cmcIds = [];
		cmcIds[0] = cmcId;
		Ext.Ajax.request({url: '../entity/cancelManagement.tv',
		   success: function() {
		      location.href = 'entityPortalCancel.tv?cmcId=' + cmcId;
		   },
		   failure: function(){window.parent.showErrorDlg();},
		   params: {cmcIds : cmcIds}
		});
	});
}

function onAddToGoogleMaps() {
	window.parent.addToGoogleMap();
}
</script>
</head>
<body onload="click1(1)">
<div id=header height=30px style="margin-left: 10px; margin-right: 10px; margin-top: 15px">
    <%@ include file="entity.inc" %>
</div>
<div style="dislpay: none">
<div id=detail align="center">
<table width=100% cellspacing=5>
	<tr>
	    <td width=80 id="name"><fmt:message bundle="${cmcRes}" key="CMC.title.name"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.sysName"/></td>
	</tr>
	    <tr>
	    <td><fmt:message bundle="${cmcRes}" key="CCMTS.entityStyle"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.typeName"/></td>
	    <td rowspan=4 align=center><img id="entityIcon" src="<s:property value="cmcBaseInfo.entity.icon"/>" border=0>
	    </td>
	</tr>
	<tr>
	    <td><fmt:message bundle="${cmcRes}" key="CCMTS.entityStyle"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.location"/></td>
	</tr>
	<tr>
	    <td>SN:</td>
	    <td><s:property value="cmcBaseInfo.entity.sn"/></td>
	</tr>
	<tr>
	    <td>OLT:</td>
	    <td><s:property value="cmcBaseInfo.entity.ponInfo.oltIp"/></td>
	</tr>
	<tr>
	    <td>Mac:</td>
	    <td><s:property value="cmcBaseInfo.entity.mac"/></td>    
	</tr>
	<tr>
		<td><fmt:message bundle="${cmcRes}" key="CMC.label.oltSlot"/>:</td>
		<td><s:property value="cmcBaseInfo.entity.ponInfo.oltSlot"/></td>
	    <td><fmt:message bundle="${cmcRes}" key="CMC.label.oltPon"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.ponInfo.oltPon"/></td>
	</tr>
	<tr>
	    <td><fmt:message bundle="${cmcRes}" key="CMC.label.osVersion"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.osVersion"/></td>
	    <td><fmt:message bundle="${cmcRes}" key="CMC.label.hardVersion"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.hardVersion"/></td>
	</tr>
	<tr>
	    <td><fmt:message bundle="${cmcRes}" key="CMC.label.channelAbility"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.channelAbility"/></td>
	    <td><fmt:message bundle="${cmcRes}" key="CMC.label.upTime"/>:</td>
	    <td><s:property value="cmcBaseInfo.entity.sysUpTime"/></td>
	</tr>
	<tr>
	    <td></td>
	    <td></td>
	</tr>
	<tr>
	    <td colspan=4><fmt:message bundle="${cmcRes}" key="CMC.label.lastFiveAlert"/></td>
	</tr>
	<tr>
	    <td colspan=4>
	        <table id="alerttable" border="1" align="center" width="100%">
	            <thead align="center">
	                <tr align="center">
	                    <th width="60%"><fmt:message bundle="${cmcRes}" key="CMC.label.eventName"/></th>
	                    <th width="40%"><fmt:message bundle="${cmcRes}" key="entity.alert.firstTimeStr"/></th>
	                </tr>
	            </thead>
	            <tbody>
	                <s:iterator value="cmcBaseInfo.alerts"  status="i">
	                    <tr align="center" class="<s:if test="#i.index % 2 == 0">col1</s:if><s:else>col2</s:else>">
	                        <td><s:property value="message"/></td>
	                        <td><s:property value="firstTimeStr"/></td>
	                    </tr>
	                </s:iterator>
	            </tbody>
	        </table>
	    </td>
	</tr>
	<tr>
	    <td colspan=4 align="right">
	        <table>
	            <tr>
	                <td></td>
	                <td></td>
	                <td colspan=3 align=right><a class="MY-LINK"
	                    href="../entity/showEditEntityJsp.tv?module=2&cmcId=<s:property value="entity.cmcId"/>"><fmt:message bundle="${cmcRes}" key="CMC.label.moreConfig"/>>></a>
	                </td>
	            </tr>
	        </table>
	    </td>
	</tr>
	<tr>
	    <td colspan=4 align="right">
	        <table>
	            <tr>
	                <td width="10"></td>
	                <td><img src="/cmc/images/tongbu.png"/><fmt:message bundle="${cmcRes}" key="CMC.label.needSync"/></td>
	                <td><img src="/cmc/images/xiafa.png"/><fmt:message bundle="${cmcRes}" key="CMC.label.notIssued"/></td>
	            </tr>
	        </table>
	    </td>
	</tr>
</table>
</div>
<div id=tools class=mydiv>
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onClick="onDiscoveryAgainClick(cmcId, entityIp);"
                ><fmt:message bundle="${cmcRes}" key="CMC.button.syncEntity"/>
        </button>
    </div>
    <div>
        <button class=BUTTON120
            onMouseOver="this.className='BUTTON_OVER120'"
            onMouseOut="this.className='BUTTON120'"
            onmousedown="this.className='BUTTON_PRESSED120'"
            onClick="cancelManagement();"><fmt:message bundle="${cmcRes}" key="CMC.button.cancelManage"/></button>
    </div>
    <div>
        <button id="downloadconfig" class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onClick="window.top.showModalDlg('"+I18N.CMC.button.syncEntityConfig+"', 380, 240, '../cmc/downloadCfgDlg.jsp')"><fmt:message bundle="${cmcRes}" key="CMC.button.syncEntityConfig"/>
        </button>
    </div>
    <div>
        <button id="uploadconfig" class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onclick="window.top.showModalDlg('"+I18N.CMC.button.issueOfflineConfig+"', 380, 240, '../cmc/uploadCfgDlg.jsp')"><fmt:message bundle="${cmcRes}" key="CMC.button.issueOfflineConfig"/>
        </button>
    </div>

    <!--
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onClick="onAddToGoogleMaps();">Google Map
        </button>
    </div>
    -->
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onClick="locateEntity(entityIp);"><fmt:message bundle="${cmcRes}" key="CMC.button.entityTopoLocation"/>
        </button>
    </div>
    <!--
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onclick="showPing();">Ping
        </button>
    </div>
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                <%--onclick="window.top.showModalDlg('REMORT PING', 480, 300, '../cmc/detail/rpingDlg.jsp')">REMORT PING--%>
                onclick="showPing()">Remote Ping
        </button>
    </div>
    <div>
        <button class=BUTTON120
                onMouseOver="this.className='BUTTON_OVER120'"
                onMouseOut="this.className='BUTTON120'"
                onmousedown="this.className='BUTTON_PRESSED120'"
                onClick="showTelnet(cmcId, entityIp);">Telnet
        </button>
    </div>
    -->
    <br>
    <br>
</div>
</div>
	<div id="div_panel_t_ping" >
		<a href="#" id="one" onclick="click1(1)">24H</a>|<a
			href="#" id="seven" onclick="click1(7)">7D</a>|<a href="#"
			id="thirty" onclick="click1(30)">30D</a><br><br>
			<fmt:message bundle="${cmcRes}" key="CMC.label.connectTime"/>:<label id="subTime"></label><br><br>
			<fmt:message bundle="${cmcRes}" key="CMC.label.connectPercent"/>:<label id="percent"></label>
	</div>
	<div id="div_panel_c_ping">
		<div id="div_pingChart" />
	</div>
</body>
</html>
