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
    MODULE google
    IMPORT google.google-map
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.google.resources,com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&language=<%=uc.getUser().getLanguage() %>"></script> 
<!--[if !IE]><!-->
<link rel="stylesheet" type="text/css" href="../css/ext-patch.css" />
<!--<![endif]-->
<style type="text/css">
#topoGraphSidePart{width:380px; height:400px; background:#F3F3F3; position:absolute; top:35px; left:0px; z-index:999; display:none; 
filter:alpha(opacity = 90); opacity:0.9; -moz-opacity:0.9; border-right:1px solid #9A9A9A;}
.bmenu_marker { background-image: url(../images/googleTip.png) !important;}
.bmenu_locatoin { background-image: url(../images/location.png) !important;}
</style>

<script type="text/javascript">
//<![CDATA[
var latitude = ${latitude};
var longitude = ${longitude};
var zoom = ${zoom};
var editTopoPower = true;
//标记弹出window
var tipWindow;
//图标的默认大小
var size;
//设备右键菜单
var entityMenu;
//连接右键菜单
var linkMenu;
//记录右键菜单的对象
var selected;
var markerGallery = [];
var linesArray = [];
var items = [];
var pagePosition = {};
var markerGallery = new Array();
var selectionModel = {};
function zoomChanged(){
    if (markerGallery) {
    	var z = googleMap.getZoom();
      	for (var i = 0; i < markerGallery.length; i++) {
          	//alert(markerGallery[i].ip)
      		if(markerGallery[i].minZoom > z){
      			markerGallery[i].setIcon(null);//不显示该设备了
      		}else if(markerGallery[i].maxZoom < z){
				//donothing;
            }else{
          		if(markerGallery[i].newMark){}else{
				    var scale = getScale(markerGallery[i].zoom);
 					var scalesize = new google.maps.Size(48/scale,48/scale);
				    var p = new google.maps.Point(scalesize.height/2,scalesize.width/2);
				    var icon = new google.maps.MarkerImage(markerGallery[i].icon48,size,null,p,scalesize); 
		        	markerGallery[i].setIcon(icon);
		        	markerGallery[i].setMap(googleMap);
                }
        	} 
      	}
    }
}
/***************************
 		 加载所有设备
 **************************/
function loadEntities() {
    if (markerGallery) {
      	for (var i = 0; i < markerGallery.length; i++) {
        	markerGallery[i].setMap(null);
      	}
    	markerGallery.length = 0;
    }
    if (linesArray) {
      	for (var i = 0; i < linesArray.length; i++) {
        	linesArray[i].setMap(null);
      	}
    	linesArray.length = 0;
    }
    //var bounds = googleMap.getBounds();
    //var southWest = bounds.getSouthWest();
    //var northEast = bounds.getNorthEast();
    //var lngSpan = northEast.lng() - southWest.lng();
    //var latSpan = northEast.lat() - southWest.lat();
    Ext.Ajax.request({url: 'loadGoogleEntities.tv', method:'POST',
       success: function(response) {
         var array = Ext.util.JSON.decode(response.responseText);
         for(var i=0;i<array.length;i++) {
            if(array[i].type == 'entity') {
    			markerGallery.push(addEntity(array[i]));
            } else if(array[i].type == 'link') {
                linesArray.push(addLink(array[i]));
            }
         }
         zoomChanged()
       },
       failure: function() {alert("failure");}
    });
}
function addEntity(entity) {
	var point = new google.maps.LatLng(entity.latitude,entity.longitude);
    var title = I18N.GOOGLE.entityIp + ':'+entity.ip+'\n' + I18N.GOOGLE.entityAlias + ':'+ entity.name;
    var scale = getScale(entity.zoom);         
	var scalesize = new google.maps.Size(48/scale,48/scale);
    var p = new google.maps.Point(scalesize.height/2,scalesize.width/2);
    var icon = new google.maps.MarkerImage("../images/"+entity.icon48,size,null,p,scalesize);
    var marker = new google.maps.Marker({
      position: point,
      icon:icon,
      title:title,
      draggable:entity.fixed,
      map: googleMap
    });
    marker.entityId = entity.entityId;
    marker.icon48 = "../images/"+entity.icon48;
    marker.zoom = entity.zoom;
    marker.minZoom = entity.minZoom;
    marker.maxZoom = entity.maxZoom;
    marker.fixed = entity.fixed;
    marker.name = entity.name;
    marker.ip = entity.ip;
    marker.url = entity.url;
    marker.type = entity.entityType;
    marker.typeId = entity.typeId;
    /*
     * 为了支持8800B设备，添加parentId和mac
     * added by huangdongsheng
     */
    marker.parentId = entity.parentId;
    marker.mac = entity.mac;
    /*
     *modified by huangdongsheng 修改CCMTS设备的Tip
     */
    var infowindow = new google.maps.InfoWindow({
        content: buildEntityTip(entity.entityId, entity.ip, entity.name,entity.location, entity.entityType, entity.mac)
    });
    google.maps.event.addListener(marker, "click", function (evt) {
    	selected = marker;
    	if(tipWindow){tipWindow.close();}
    	infowindow.open(googleMap,marker);
    	tipWindow=infowindow;
    });
    google.maps.event.addListener(marker, "rightclick", function(evt) {
    	showEntityMenu(this);
    	selected = marker;
    });
    
    google.maps.event.addListener(marker, "dragend", function(evt) {
    	//marker.setIcon(icon);
    	selectionModel.selected = marker;
		//note the marker
		confirmSaveLocation(marker)
		//refresh,for link prupose
		//onRefreshClick()
    });
    return marker;
}
function addLink(link) {
    var latlngs=[
    	new google.maps.LatLng(link.srcLatitude,link.srcLongitude),
    	new google.maps.LatLng(link.destLatitude,link.destLongitude)
    ];
    var line = new google.maps.Polyline({
      path: latlngs,
      strokeColor: "#0000FF",
      strokeOpacity: 1.0,
      clickable:true,
      strokeWeight: 2,
      map:googleMap
    });
    google.maps.event.addListener(line, "rightclick", function(evt) {
    	/* selected = line;
    	linkMenu.showAt([10,10]); */
    });
    return line;
}
function getScale(zoom) {
	if(zoom > googleMap.getZoom()){
		return (zoom - googleMap.getZoom())*2;
	}else{
		return 1;
	}
}
function locationTo(entityId,lat,lng,zoom,ip,name) {
    googleMap.setCenter(new google.maps.LatLng(lat,lng));
    googleMap.setZoom(zoom);
    google.maps.event.trigger(googleMap, 'resize');
  	for (var i = 0; i < markerGallery.length; i++) {
    	if(markerGallery[i].entityId == entityId){
    		google.maps.event.trigger(markerGallery[i], 'click');
    	}
  	}
}
<%
	boolean topoMapPower = uc.hasPower("topoEdit");
%>
function buildEntityTip(entityId, ip, name,entityLocation, type, mac) {
	var tip = null;
	/**
	 * 修改 CCMTS设备的TIP信息，为了兼容8800A，A/B/C/D设备均仅显示管理IP
	 */
	if(type == _CCMTS_TYPE_){
		tip = '<div style="text-align:left;"><b>' + I18N.SYSTEM.baseInfo + '</b><br><br>'+
	    I18N.GOOGLE.entityPosition + ':&nbsp;&nbsp;'+entityLocation+'<br> ' + I18N.GOOGLE.manageIp +':&nbsp;&nbsp;'+ip+'<br>' + I18N.GOOGLE.alias + ':&nbsp;&nbsp;'+ name+
	    '</div><br><button class=BUTTON75  onclick="onEntitySnapClick()">' + I18N.MenuItem.view + '</button>&nbsp;';
	}else if(type == _ONU_TYPE_){
		tip = '<div style="text-align:left;"><b>' + I18N.SYSTEM.baseInfo + '</b><br><br>'+
        I18N.GOOGLE.entityPosition + ':&nbsp;&nbsp;'+entityLocation+'<br>' + I18N.GOOGLE.manageIp +':&nbsp;&nbsp;'+ip+'<br>' + I18N.GOOGLE.alias + ':&nbsp;&nbsp;'+ name+ "<br>";
	}else{
		tip = '<div style="text-align:left;"><b>' + I18N.SYSTEM.baseInfo + '</b><br><br>'+
	    I18N.GOOGLE.entityPosition + ':&nbsp;&nbsp;'+entityLocation+'<br>' + I18N.GOOGLE.entityIp + ':&nbsp;&nbsp;'+ip+'<br>' + I18N.GOOGLE.alias + ':&nbsp;&nbsp;'+ name+
	    '</div><br><button class=BUTTON75  onclick="onEntitySnapClick()">' + I18N.MenuItem.view + '</button>&nbsp;';
	}	
    <%
    if (topoMapPower) {
    %>  
    + '<button class=BUTTON75  onclick="onDeleteClick()">' + I18N.COMMON.remove + '</button>';
    <%}%>
    return tip;
}
function onSearchClick() {
    //window.parent.showProperty("google/entityGoogleMapSearch.jsp", I18N.GOOGLE.googleMapSearch);
	showSidePart("google/entityGoogleMapSearch.jsp");
}

/**
 * 记录鼠标位置
 */
function note(e){
	pagePosition.x = e.pageX;
	pagePosition.y = e.pageY;
}

function onSearchGeoClick(htmlCell,e){
	var cell = $("#geoSearchInput")
	var address = $("#geoSearchInput").val();
    geocoder.geocode({"address":address}, function(results,status) {  
	  var length = results.length;
	  if(length == 0){
		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GOOGLE.noResult);
	  }else {
		items.length = 0;
		for(var i=0;i<length;i++){
			var geoItem = results[i].formatted_address;
			var position =  {lat:results[i].geometry.location.lat(),lng : results[i].geometry.location.lng()};
			items[items.length] = {text: geoItem, position:position,  handler: onGeoLocationClick};
			items[items.length] = '-';
		}
		new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items}).showAt([cell.position().left,cell.position().top+20]);
	  }
   });
}

function onGeoLocationClick(o){
	googleMap.setCenter(new google.maps.LatLng(o.position.lat,o.position.lng));
	onNewMarkerClick();
}

Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = "../images/s.gif";
    Ext.QuickTips.init();
    // calc appropriate size
    doOnResize();
    //initilize Google Map Object
    if(!initialize()){
    	return;
    }
    //工具栏
    var tb = new Ext.Toolbar();
    tb.render('toolbar');
    items.length = 0;
    /* items[items.length] = new Ext.Toolbar.SplitButton({text:I18N.COMMON.print, handler : onPrintClick, iconCls:'bmenu_print', menu:{items:[
			{text:'<b>'+I18N.COMMON.print+'</b>', handler:onPrintClick},
			{text:I18N.COMMON.printPreview, handler:onPrintPreviewClick}]}}); 
	items[items.length] = '-';*/
	items[items.length] = {text:I18N.COMMON.find, iconCls:'bmenu_find', handler : onSearchClick, cls:'pL10'};
	items[items.length] = {text:I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler : onRefreshClick};
	items[items.length] = {text:I18N.COMMON.marker, iconCls:'bmenu_mapBubble', handler : onNewMarkerClick};
	items[items.length] = {text:I18N.COMMON.marker, xtype: 'textfield',id:'geoSearchInput'};
	items[items.length] = {text :I18N.COMMON.location,iconCls:'bmenu_locatoin',  handler : onSearchGeoClick};
    tb.add(items);
    tb.doLayout();
    
    /* //设备右键菜单
	items.length = 0;
	items[items.length] = {id:"view2",text: I18N.COMMON.view,handler: onEntitySnapClick};
	items[items.length] = {text: I18N.NETWORK.tool, menu: [
       	{text: I18N.NETWORK.ping, handler: onPingClick},
       	{text: I18N.NETWORK.tracert, handler: onTracertClick},
       	{text: I18N.NETWORK.nativeTelnet, handler: onNativeTelnetClick}
       ]};
	items[items.length] = {text: I18N.NETWORK.discoveryAgain, iconCls: 'bmenu_refresh', handler: onDiscoveryAgainClick};
    items[items.length] = {id:"quickset",text: I18N.COMMON.quickSet, handler: onQuickSet};
  	//--------get additional menu dynamically----//
    $.ajax({
        type: "GET",
        url: "/olt/js/extendMenu.js",
        dataType: "script",
        async : false,
        success  : function () {
            items = extendOper(items);
        }
    });
	items[items.length] = '-';
	items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};
    if (editTopoPower) {
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
    }
	items[items.length] = '-';
	items[items.length] = {text: I18N.COMMON.property, handler: onEntityPropertyClick};
	entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	 */
    //连接右键菜单
	items.length = 0;
    if (editTopoPower) {
        items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onRefreshClick};
    }
	items[items.length] = '-';
	items[items.length] = {text: I18N.COMMON.property, handler: onRefreshClick};
	linkMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    if (googleMap != null) {
    	loadEntities();
    	items.length = 0;
		items[items.length] = {text:I18N.COMMON.print, handler : onPrintClick, iconCls:'bmenu_print'};
		items[items.length] = '-';
		items[items.length] = {text:I18N.COMMON.find, iconCls:'bmenu_find', handler : onSearchClick};
		items[items.length] = {text:I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler : onRefreshClick};
		items[items.length] = {text:I18N.GOOGLE.addEntity, iconCls:'bmenu_marker', handler : onNewEntityAddClick};
		var contextMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	    google.maps.event.addListener(googleMap, "rightclick", function(evt) {
	    	window.pagePosition = evt.latLng;
	    	contextMenu.showAt([evt.pixel.x,evt.pixel.y]);
	    });
	    google.maps.event.addListener(googleMap, "click", function (evt) {
	    	if(tipWindow){tipWindow.close();}
	    });
    }
});


function doOnResize() {
    var winHeight;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        winHeight = document.body.clientHeight;
    //通过深入Document内部对body进行检测，获取窗口大小
    if (document.documentElement  && document.documentElement.clientHeight)
        winHeight = document.documentElement.clientHeight;
    //Zeta$('googleMap').style.height = winHeight;
    var h = $(window).height();
    var h2 = $("#toolbar").outerHeight() || 35;
    var h3 = h-h2;
    if(h3>0){
    	$("#googleMap").height(h3);
    }
    if(googleMap!=null){
    	google.maps.event.trigger(googleMap, 'resize');
    }
}
function onPrintClick() {window.print();}
function onPrintPreviewClick() {
    var mapTypeId = googleMap.mapTypeId;
    window.open('showEntityGoogleMapPreview.tv?latitude='+googleMap.getCenter().lat()+'&longitude='+googleMap.getCenter().lng()+'&zoom='+googleMap.getZoom()+'&mapTypeId='+mapTypeId);}
function onMaxViewClick() {window.parent.enableMaxView();}
function doOnBeforePrint() {Zeta$('toolbar').style.display = 'none';}
function doOnAfterPrint() {Zeta$('toolbar').style.display = 'block';}
function onRefreshClick() {
    loadEntities();
}

/**
 * 直接添加一个设备
 */
function onNewEntityAddClick(){
	window.latLngs = new google.maps.LatLng(window.pagePosition.lat(),window.pagePosition.lng());
	onNewMarkerClick();
}



if(document.addEventListener){
	window.addEventListener('beforeprint', doOnBeforePrint, false);
	window.addEventListener('afterprint', doOnAfterPrint, false);
}else{
	window.attachEvent('onbeforeprint', doOnBeforePrint);
	window.attachEvent('onafterprint', doOnAfterPrint);
}

$(function(){
	autoHeight();
	function autoHeight(){
		var topPart = 36;
		var h = $(window).height() - topPart;
		if(h < 0) h=100;
		$("#topoGraphSidePart, #sidePart").height(h);
		
		var tPos = ($(window).height() - $("#topoGraphSidePartArr").outerHeight())/2;
		if(tPos <0) tPos = 100;
		$("#topoGraphSidePartArr").css("top",tPos);
	}
	
	$(window).resize(function(){
		autoHeight();
	});//end resize;
	
	$("#topoGraphSidePartArr").click(function(){
		if($("#topoGraphSidePartArr").hasClass("sideMapRight")){
			window.openCloseSidePart("open");
		}else{
			window.openCloseSidePart("close");
		}
	});	
});//end docuemnt.ready;
function openCloseSidePart(para){
	$("#topoGraphSidePartArr").css("display","block");
	if(para == "open"){
		if($("#topoGraphSidePartArr").hasClass("sideMapLeft")){//如果本来就是打开的;
			return;
		}
		$("#topoGraphSidePartArr").animate({left:380});
		$("#topoGraphSidePart").css("display","block").css("left",-380).animate({left:0},function(){
			$("#topoGraphSidePartArr").css("left",380).attr("class","sideMapLeft");	
		});
	}else{
		$("#topoGraphSidePartArr").animate({left:0});
		$("#topoGraphSidePart").animate({left:-380},function(){
			$(this).css("display","none");
			$("#topoGraphSidePartArr").css("left",0).attr("class","sideMapRight");
		})		
	}
};
//显示侧边栏 ，属性部分内容;
function showSidePart(url){
	url = "../" + url;
	$("#sidePart").attr("src", url);
	openCloseSidePart("open");
};

//]]>
</script>
</head>
<body  onresize="doOnResize()" style="width:100%; height:100%; overflow:hidden;">
	<div id="topoGraphSidePart">
		<iframe id="sidePart" name="sidePart" frameborder="0" width="100%" height="100%" src=""></iframe>
	</div>
	<div id="topoGraphSidePartArr" class="sideMapRight" style="display:none;"></div>
	
	<table width="100%" height="90%" cellspacing=0 cellpadding=0 id="topPart">
		<tr>
			<td valign=top><div id=toolbar></div></td>
		</tr>
		<tr>
			<td align=right><div id="googleMap"></div></td>
		</tr>
	</table>
</body>
</Zeta:HTML>
