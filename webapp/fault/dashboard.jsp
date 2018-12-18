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
    PLUGIN Portlet
    MODULE fault
</Zeta:Loader>
<style type="text/css">
#portlet1 .x-panel-body{ background:#fff;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script> 
// for tab changed start
function tabActivate() {
	window.parent.setStatusBarInfo('', '');
	doRefresh();
	startDispatchInterval();
}
function tabDeactivate() {
	doOnunload();
}
function tabRemoved() {
	doOnunload();
}
// for tab changed end

var timer = null;
var dispatchInterval = 60000;


//开始时间与结束时间控件
var beginTime,stopTime;
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');

if(beginTime == '' || beginTime == null){
	beginTime = lastWeek;
}

if(stopTime == '' || stopTime == null){
	stopTime = currentTime;
}

function doRefresh() {
	for (var i = 0; i < portlets.length; i++) {
		/* var mgr = portlets[i].getUpdater();
		mgr.update({disableCaching: true, url: urls[i]}); */
		var mgr = portlets[i].tools.refresh;
        mgr.dom.click()
	}
}
function startDispatchInterval() {
	if (timer == null) {
    	timer = setInterval("doRefresh()", dispatchInterval);
    }
}
function doOnunload() {
	if (timer != null) {
		clearInterval(timer);
		timer = null;
	}
}
function showEntitySnap(id, ip) {
	window.top.addView("entity-" + id, I18N.ALERT.entity + "[" + ip + ']', 'entityTabIcon',
		"portal/showEntitySnapJsp.tv?module=1&entityId=" + id);
}
function showAlertByLevel(index, key) {
    var level = 6;
    if (key == I18N.WorkBench.emergencyAlarm) {
        level = 6;
    }  else if (key == I18N.WorkBench.seriousAlarm) {
        level = 5;
    }   else if (key == I18N.WorkBench.mainAlarm) {
        level = 4;
    } else if (key == I18N.WorkBench.minorAlarm) {
        level = 3;
    } else if (key == I18N.WorkBench.generalAlarm) {
        level = 2;
    } else if (key == I18N.WorkBench.message) {
        level = 1;
    }
    if(window.parent.eventCategoryTree){
    	//@FIXBUG by bravin 当menu菜单切换到其他地方时，将导致 调用已释放的script异常
    	try{
	       window.parent.eventCategoryTree.getNodeById("cl"+level).select();
    	}catch(e){}
    }
	window.top.addView("alertViewer", I18N.EVENT.alarmViewer, "alarmTabIcon", 
			'alert/showCurrentAlertList.tv?level=' + level,null,true); 
}

function viewAlertByIp(name,ip,id,type) {
	var title = name;
	if(!name){
		title = id;
	}
	window.top.addView("entity-" + id, title, 'entityTabIcon',
				"/portal/showEntitySnapJsp.tv?module=1&entityId=" + id,null,true);
}

function viewAlertByName(name,id,type) {
	var oltType = EntityType.getOltType();
	var cmcType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	var onuType = EntityType.getOnuType();
	var title = name;
	if(!name){
		title = id;
	}
	//showEntitySnap(id,title)
	if(EntityType.isOltType(type)){
		window.top.addView("entity-" + id, title, 'entityTabIcon',
				"/portal/showEntitySnapJsp.tv?module=1&entityId=" + id,null,true);
	}
	if(EntityType.isCcmtsType(type)){
		window.top.addView("entity-" + id,title, 'entityTabIcon',
				"/cmc/showCmcPortal.tv?module=1&cmcId=" + id,null,true);
	}
	if(EntityType.isCmtsType(type)){
		window.top.addView("entity-" + id, title, 'entityTabIcon',
				"/cmts/showCmtsPortal.tv?module=1&cmcId="+id+"&productType=" + cmtsType ,null,true);
	}
	if(EntityType.isOnuType(type)){
	    window.top.addView("entity-" + id, title, 'entityTabIcon',
	    		"/onu/showOnuPortal.tv?module=1&onuId="+id ,null,true);
	}
}

function viewAlertByAlert(name,id,type) {
	var oltType = EntityType.getOltType();
	var cmcType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	var onuType = EntityType.getOnuType();
	var title = name;
	if(!name){
		title = id;
	}
	if(EntityType.isOltType(type)){
		window.top.addView("entity-" + id,  title , 'entityTabIcon',
				"alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
	}
	if(EntityType.isCcmtsType(type)){
		window.top.addView("entity-" + id, title , 'entityTabIcon',
				"/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
	}
	if(EntityType.isCmtsType(type)){
		window.top.addView("entity-" + id, title, 'entityTabIcon',
				"/cmts/alert/showCmtsAlert.tv?module=7&cmcId="+id+"&productType=" + cmtsType ,null,true);
	}
	if(EntityType.isOnuType(type)){
		var onuType = EntityType.getOnuType();
		window.top.addView("entity-" + id, title, 'entityTabIcon',
				"/onu/showOnuAlert.tv?module=3&onuId="+id ,null,true);
	}
}

var viewport = null;
var portlets = [];
var urls = [];
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
    Ext.Updater.defaults.disableCaching = true;
 
 	var portletItems = [];
 	var size = 2;
 	if (size > 2) {
 		size = 2;
 	}
 	for (var i = 0; i < size; i++) {
 		portletItems[i] =  {
            columnWidth:.5,
	        style:'padding:10px 0px 10px 10px',
            items:[]
        };
 	}
 	var alertDistGraphUrl = '/portal/getAlertDistGraph.tv';
 	var html = "<iframe width=100% id='portletAlertDist' name='portletAlertDist' height=330 frameborder=0 src='"+alertDistGraphUrl+"'></iframe>"
	
	var portlet1 = new Ext.ux.Portlet({
		id:'portlet1',
	    tools: [{id: 'refresh',
	        qtip: I18N.COMMON.refresh,
	        handler: function(event, toolEl, panel){
	            //ZetaUtils.getIframe('portletAlertDist').redraw();

	            var typeSrc = document.getElementById("portletAlertDist"); 
	            var src = alertDistGraphUrl; 
	            typeSrc.src = src; 
	        }
	    }],		
        title: I18N.PortletCategory.getAlertDistGraph,
        bodyStyle: 'padding:10px',
        autoScroll:true,
        html:html
    });
	portletItems[0].items[portletItems[0].items.length] = portlet1;
	portlets[portlets.length] = portlet1;

	var portlet2 = new Ext.ux.Portlet({
		id:'portlet2',
	    tools: [{
	        id: 'gear',
	        qtip:'@COMMON.more@',
            handler: function(event, toolEl, panel){
                window.top.addView("deviceAlertMore", '@resources/WorkBench.deviceAlert@', "apListIcon", "/portal/showDeviceAlertList.tv");
            }
        },{id: 'refresh',
	        qtip: I18N.COMMON.refresh,
	        handler: function(event, toolEl, panel){
	            panel.getUpdater().update({disableCaching: true, url: '/portal/getDeviceAlertTop.tv'});
	        }
	    }],		
        title: I18N.ALERT.oltAlertNumTop10,
        bodyStyle: 'padding:10px',
        autoScroll:true,
		autoLoad: {url: '/portal/getDeviceAlertTop.tv', text: I18N.WorkBench.loading, disableCaching: true}
        
    });
	portletItems[1].items[portletItems[1].items.length] = portlet2;
	portlets[portlets.length] = portlet2;
	urls[urls.length] = '/portal/getDeviceAlertTop.tv';
	
    viewport = new Ext.Viewport({
        layout: 'border',
        items: [{xtype:'portal', region:'center', margins:'0 0 0 0', border:false, items:portletItems}]
    });
    startDispatchInterval();
});
</script>
</head><body class=BLANK_WND onunload="doOnunload();">
</body></Zeta:HTML>