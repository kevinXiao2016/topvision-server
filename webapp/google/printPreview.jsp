<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml"><head>
<title>Google Map</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<!--[if !IE]><!-->
<link rel="stylesheet" type="text/css" href="../css/ext-patch.css"/>
<!--<![endif]-->
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>

<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&language=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="google-map.js"></script>
<script type="text/javascript">
//<![CDATA[
var latitude = <s:property value="latitude"/>;
var longitude = <s:property value="longitude"/>;
var zoom = <s:property value="zoom"/>;
var mapTypeId = '${mapTypeId}';
//the default size of icon
var size = new google.maps.Size(48,48);
var markersArray = [];
var linesArray = [];
function load() {
    doOnResize();
    initialize();
    googleMap.setOptions({
    	disableDoubleClickZoom:true,
    	draggable:false,
    	scrollwheel:false,
    	scaleControl:false,
    	mapTypeId : mapTypeId,
    	navigationControl:false,
    	mapTypeControl:false,
    	keyboardShortcuts:false,
    	streetViewControl:false,
    	zoom:zoom
    });
}

function loadEntities() {
    if (markersArray) {
      	for (var i = 0; i < markersArray.length; i++) {
        	markersArray[i].setMap(null);
      	}
    	markersArray.length = 0;
    }
    if (linesArray) {
      	for (var i = 0; i < linesArray.length; i++) {
        	linesArray[i].setMap(null);
      	}
    	linesArray.length = 0;
    }
    var bounds = googleMap.getBounds();
    var southWest = bounds.getSouthWest();
    var northEast = bounds.getNorthEast();
    var lngSpan = northEast.lng() - southWest.lng();
    var latSpan = northEast.lat() - southWest.lat();
    Ext.Ajax.request({url: 'loadGoogleEntities.tv', method:'POST',
       success: function(response) {
         var array = Ext.util.JSON.decode(response.responseText);
         for(var i=0;i<array.length;i++) {
            if(array[i].type == 'entity') {
    			markersArray.push(addEntity(array[i]));
            } else if(array[i].type == 'link') {
                linesArray.push(addLink(array[i]));
            }
         }
       },
       failure: function() {alert("failure");}
    });
}
function addEntity(entity) {
	var point = new google.maps.LatLng(entity.latitude,entity.longitude);
    var scale = getScale(entity.zoom);
	var scalesize = new google.maps.Size(48/scale,48/scale);
    var p = new google.maps.Point(scalesize.height/2,scalesize.width/2);
    var icon = new google.maps.MarkerImage("../images/"+entity.icon48,size,null,p,scalesize); 
    var marker = new google.maps.Marker({
      position: point,
      icon:icon,
      map: googleMap
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
    return line;
}
function getScale(zoom) {
	if(zoom > googleMap.getZoom()){
		return (zoom - googleMap.getZoom())*2;
	}else{
		return 1;
	}
}
Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = "../images/s.gif";
    Ext.QuickTips.init();
    var tb = new Ext.Toolbar();
    tb.render('toolbar');
    tb.add({text:I18N.COMMON.print, handler:onPrintClick, iconCls:'bmenu_print'});
    tb.doLayout();
    if (googleMap != null) {
    	loadEntities();
    }
});
function onPrintClick() {window.print();}
function doOnResize() {
    var winHeight;
    if (window.innerHeight)
        winHeight = window.innerHeight;
    else if ((document.body) && (document.body.clientHeight))
        winHeight = document.body.clientHeight;
    if (document.documentElement  && document.documentElement.clientHeight)
        winHeight = document.documentElement.clientHeight;
    Zeta$('googleMap').style.height = winHeight;
    if(googleMap!=null){
    	google.maps.event.trigger(googleMap, 'resize');
    }
}
//]]>
</script>
</head>
<body onload="load()" onresize="doOnResize()"" scroll=no>
<table width="100%" height="100%" cellspacing=0 cellpadding=0>
  <tr><td valign=top><div id=toolbar></div></td></tr>
  <tr><td align=right><div id="googleMap"></div></td></tr>
</table>
</body></html>
