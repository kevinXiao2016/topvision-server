<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.google.resources" var="google"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><head>
  <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
<title>Google Maps - Net</title>
<%@include file="../include/meta.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.google.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&language=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="google-map.js"></script>
<script type="text/javascript">
  //<![CDATA[
var latitude = <s:property value="latitude"/>;
var longitude = <s:property value="longitude"/>;
var zoom = <s:property value="zoom"/>;
var marker = null;
var markerGallery = new Array();
function load() {
	//initialize google map objet
    initialize();
    //add first marker
    addMarker();
    //loadOther Marker
    loadMarkers();
}
function doOnResize() {
}

/******************************
 	添加一个新的标记
 *****************************/
function addMarker(){
	var newMarker = new google.maps.Marker({
	      position: googleMap.getCenter(),
	      draggable: true,
	      title:I18N.GOOGLE.defaultMaker,
	      map: googleMap
	});
	//google.maps.event.addListener(marker, "dragstart", function() {  googleMap.closeInfoWindow();  });
	google.maps.event.addListener(newMarker, "dragend", function(e){buildConfigWindow(newMarker,e)});
	google.maps.event.addListener(newMarker,'dbclick',function(e){buildConfigWindow(newMarker,e)})
	google.maps.event.addListener(newMarker,'click',function(e){buildConfigWindow(newMarker,e)})
	markerGallery.push(newMarker);
}

/******************************
 	加载其他已添加到拓扑图的标记
 	TODO:如果某些标记不在当前的视野中，是否会产生BUG
 ******************************/
function loadMarkers(){}

/**
 * 构造标记窗口
 */
function buildConfigWindow(m,e){
	var request_url = "http://ditu.google.cn/maps/geo?key=abcdef&output=csv&q=" + m.getPosition().toUrlValue()+"&callback=?";
	var options = new Array();
	var content = "";
	$.getJSON(request_url,function(geo){
		for(var i=0;i<geo.Placemark.length;i++){
			try{
				var o = geo.Placemark[i].AddressDetails;
				var city = o.Country.AdministrativeArea;
				var Locality = city.Locality;
				//province,city,county
				var res = Locality.LocalityName+Locality.DependentLocality.DependentLocalityName+','+city.AdministrativeAreaName;
				options.push(res)
			}catch(e){
				options.push(geo.Placemark[i].address);
			}
		}
		if(1==geo.Placemark.length){
			content = "<label>" + GOOGLE.currentPosition + " : "+options.pop()+"</label>"
		}else if(1<geo.Placemark.length){
			content += I18N.GOOGLE.currentPosition + " : <select id='targetLocation'>";
			for(var o in options){
				content += "<option>"+o+"</option>"
			}
			content += "</select>"
		}//TODO 如果位置不确定，有待处理
		$.ajax({
			url:'/google/queryAvaibleDevice.tv',method:'post',cache:false,dataType:'json',
			success:function(json){
				content += '<br><br>' + I18N.GOOGLE.chooseEntity + ' : <select id="targetDevice">';
				for(var i=0;i<json.list.length;i++){
					content += "<option value="+json.list[i].entityId+">"+json.list[i].displayText+"</option>"
				}
				content += '</select><br><br>' + I18N.GOOGLE.pleaseAddMaker + ' : <input id="textMarker" type="text" value=""/>';
				content += '<br>              <button onclick="saveMarker()">' + I18N.COMMON.ok  + '</button>'
				if(window.infowindow){
					window.infowindow.setContent(content);
				}else{
					window.infowindow = new google.maps.InfoWindow({
					    content : content
					});
				}
				infowindow.open(googleMap,m);
			},
			error:function(){}
		});
	})
}

/*
 * 当窗口被关闭的时候进行保存,handler in topograph.js
 */
function okClick() {
    window.top.ZetaCallback = {type:'ok', lat:marker.getPosition().lat(), lng:marker.getPosition().lng(), zoom:googleMap.getZoom()};
    window.top.closeWindow('googleDlg');
}
function cancelClick() {
    window.top.ZetaCallback = {type:'cancel'};
    window.top.closeWindow('googleDlg');
}
//]]>
</script>
</head>
<body class=POPUP_WND onload="load()" onresize="doOnResize()" onunload="GUnload()">
  <table width=100% height=100% cellspacing=0 cellpadding=0>
  <tr><td height=100%><div id="googleMap" style="width:100%; height:100%"></div></td></tr>
<tr><td valign=top align=right style="padding:10px">
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	    onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'" 
	    onclick="addMarker()"><fmt:message key="GOOGLE.addMaker" bundle="${google}"/></button>&nbsp;
	<button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	    onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'" 
	    onclick="okClick()"><fmt:message key="COMMON.ok" bundle="${resource}"/></button>&nbsp;
    <button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
	    onMouseOut="this.className='BUTTON75'" onMouseDown="this.className='BUTTON_PRESSED75'"
	    onclick="cancelClick()"><fmt:message key="COMMON.cancel" bundle="${resource}"/></button></td></tr>
  </table>
</body></html>