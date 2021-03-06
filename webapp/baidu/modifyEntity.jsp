<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=u8lHaOCM6WAmwPwXQFOrb0v9"></script>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	css baidu.baiduMap
	library ext
	library jquery
	library zeta
	module baidu
	import baidu.baiduMap

</Zeta:Loader>
<style type="text/css">
#allmap {
	width: 100%;
	height: 80%;
}

#results{width:250px; margin-top:10px; float:right;}    
</style>

<script type="text/javascript">
var local;
var entityId = ${entityId}
var baiduEntity = ${baiduEntityJason}
var content = "";
var modifyMarker;
var longituInit = '${longitudStr}'
var latitudeInit = '${latitudeStr}'
var zoomInit = '${zoomStr}'

Ext.onReady(function() {
	map = new BMap.Map("allmap");
	mode = 'modify'
	if(longituInit != '' && latitudeInit != '' && zoomInit != ''){
		map.centerAndZoom(new BMap.Point(longituInit, latitudeInit), zoomInit)
	}
	//map.centerAndZoom(new BMap.Point(baiduEntity.longitude, baiduEntity.latitude), baiduEntity.zoom);
	addEntity(baiduEntity)
	var content = makeMarkerContent(baiduEntity)
   	marker = baiduEntity.marker;
   	marker.openInfoWindow(new BMap.InfoWindow(content)); 
   	
   	marker.addEventListener('click',function(event){  
   		marker.openInfoWindow(new BMap.InfoWindow(content));  
       });
   	
   	map.addEventListener('click', function(clickEvent) {
   		map.removeOverlay(modifyMarker); 
	});
   	
	local = new BMap.LocalSearch(map, {
		renderOptions : {
			map : map
		}
	});

	$("#topoSearchLinkBtn").bind("click", serachlocal);
	
	//初始化侧边弹出部分
	$(".olt-sidebar").hide();
	//设置右侧箭头国际化
	$("#baiduMapSideArrow").attr("class", "baiduSideLeft");

	autoSetArrPosition();
	$(window).resize(function() {
		autoSetArrPosition();
	});
	$("#baiduMapSideArrow").click(function() { //展开，折叠;
		if ($("#arrow").hasClass("cmListSideArrLeft")) { //展开
			showHideSide("show");
		} else { //折叠;
			showHideSide("hide");
		}
	});
	
	add_control();
	addMapListener();
});

function keydownEvent() {
    var e = window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13 ) {
    	$("#topoSearchLinkBtn").click()
    }
}

function autoSetArrPosition() { //设置右侧箭头在屏幕中间;
	var h = $(window).height(), h2 = (h - 140) / 2;
	if (h2 < 0) {
		h2 = 0;
	}
	$("#baiduMapSideArrow").css({
		top : h2
	});
}

function cancleClick() {
	window.parent.closeWindow("modalDlg");
}
	
</script>
	</head>
	<body class="openWinBody">
		<div id="topPart" class="topoTopPart">
			<div id="normalTopPart" class="topBarSelected">
				<div class="topoBaiduSelectInput">
					<a href="javascript:;" class="baiduSearchLinkBtn"
						id="topoSearchLinkBtn"></a> <input type="text"
						id="topoSearchInput" onkeydown="keydownEvent()" class="baiduSearchInput" value="" />
				</div>
			</div>
			<div id="searchResultPanel"
				style="border: 1px solid #C0C0C0; width: 150px; height: auto; display: none;"></div>
		</div>
		<div id="allmap" style="cursor: crosshair;"></div>
		<div id="baiduMapSide">
		<div id="results"></div></div>
		<div class="cmListSideEnglish" id="baiduMapSideArrow">
			<div class="cmListSideArrLeft" id="arrow" style="top:65px;"></div>
		</div>
		<div class="noWidthCenterOuter clearBoth">
			<ol class="upChannelListOl pB0 pT20 noWidthCenter">
				<li><a onclick="cancleClick()" href="javascript:;"
					class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			</ol>
		</div> 
	</body>
</Zeta:HTML>
