<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head><title>Search Entity For Google Maps</title>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.google.resources" var="google"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/jquery/ui.core.js"></script>
<script type="text/javascript" src="../js/jquery/ui.autocomplete.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.google.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script>
var googleMap;
var searchResult;

function onSearchClick() {
	
   // googleMap = window.parent.getFrame('ngm').googleMap;
    searchResult = Zeta$('searchResult');
    while(searchResult.rows.length>0){
        searchResult.deleteRow(0);
    }
    $.ajax({url: 'searchGoogleEntity.tv', type: 'POST', data: {search:Zeta$('search').value},
    	success: function(array) {
         if (array.length == 0) {
        	var tr = searchResult.insertRow();
            tr.style.valign='middle';
	        var td = tr.insertCell();
	        td.innerHTML = I18N.GOOGLE.noEntity;
         } else {
			for(var i=0;i<array.length;i++) {
		        var tr = searchResult.insertRow();
	            tr.style.valign='middle';
		        var td = tr.insertCell();
		        if(array[i].icon48.startWith("/images/")){
		        	td.innerHTML = '<img src="'+array[i].icon48+'">';
			    }else{
			        td.innerHTML = '<img src="/images/'+array[i].icon48+'">';
				}
		        td = tr.insertCell();
	            td.innerHTML = '<a href="javascript:onDeviceClick('+array[i].entityId+','+array[i].latitude+','+array[i].longitude+','+
	                            array[i].zoom+',\''+array[i].ip+'\',\''+array[i].name+'\');">'+array[i].name+'/'+array[i].ip+'</a>';
	            td = tr.insertCell();
	            td.innerHTML = ''+array[i].location
	         }      
         }
       }, error: function() {window.parent.showErrorDlg();}, dataType: 'json', cache: false});
}

function onDeviceClick(entityId,lat,lng,zoom,ip,name) {
	
	if(!window.top.getFrame('ngm')){
		window.top.addView("ngm", I18N.GOOGLE.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
		setTimeout(function(){
			window.top.getFrame('ngm').locationTo(entityId,lat,lng,zoom,ip,name);
		},1000);
	}else{
	    window.top.getFrame('ngm').locationTo(entityId,lat,lng,zoom,ip,name);
	}
}

function doOnKeydown(box) {
	if(event.keyCode == KeyEvent.VK_ENTER) {
		onSearchClick();
	}
}

function doOnload() {
	Zeta$('search').focus();
}

$(document).ready(function(){
 	//var data = "Core Selectors Attributes Traversing Manipulation CSS COKS C1 C2 C3 C4 C5 C6 C7 C8 C9 C10 C11 Events Effects Ajax Utilities".split(" ");
	//$("#search").autocomplete(data);
 }); 
</script>
</head><body class=BLANK_WND style="margin:5px" onload="doOnload();">
<div class=formtip id=tips style="display: none"></div>
<center><table cellspacing=5 cellpadding=0>
<tr><td><img src="/images/google.png" /></td>
    </tr><tr><td><input id="search" type=text class=iptxt name="search" maxlength=32
    	style="width:286px" value="" onkeydown="doOnKeydown(this);" onblur="inputBlured(this, 'iptxt');" onclick="clearOrSetTips(this);"
    	onfocus="inputFocused('search', '', 'iptxt_focused')"></td>
    </tr><tr><td align=right><button class=BUTTON95 onMouseOver="this.className='BUTTON_OVER95'"
    onMouseOut="this.className='BUTTON95'" onMouseDown="this.className='BUTTON_PRESSED95'" onclick="onSearchClick()"><fmt:message key="GOOGLE.searchEntity" bundle="${google}"/></button></td>
</tr></table>
<table id=searchResult width=280 cellspacing=5 cellpadding=0></table></center>
</body></html>
