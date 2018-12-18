<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=u8lHaOCM6WAmwPwXQFOrb0v9"></script>
<Zeta:Loader>
	css css.main
	css baidu.baiduMap
	library Jquery
	library ext
	library zeta
    module baidu
	import baidu.baiduMap
</Zeta:Loader>
<style type="text/css">
.tR_btn{ position:absolute; top:5px; left:10px; z-index:9;}
#allmap {
	width: 100%;
	height: 90%;
}

#normalTopPart {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
}

#results {
	width: 250px;
	margin-top: 10px;
	float: right;
}
</style>
<script type="text/javascript">
var longituInit = '${longitudStr}'
var latitudeInit = '${latitudeStr}'
var zoomInit = '${zoomStr}'
Ext.onReady(function() {
	var $allMap = $("#allmap"),
	    h = $("body").outerHeight() - $("#topPart").outerHeight();
	
	$allMap.height(h);		
	
	map = new BMap.Map("allmap");
	if(longituInit != '' && latitudeInit != '' && zoomInit != ''){
		map.centerAndZoom(new BMap.Point(longituInit, latitudeInit), zoomInit)
	}else{
		//定位到当前城市
		function myFun(result){
			var cityName = result.name;
			map.centerAndZoom(cityName, 12);
		}
		var myCity = new BMap.LocalCity();
		myCity.get(myFun);
	}

	add_control();
	//添加设备
	$.ajax({
		url : '/baidu/getBaiduMapEntity.tv',
		type : 'POST',
		dateType : 'json',
		success : function(response) {
			$.each(response, function(index, baiduEntity) {
				addEntity(baiduEntity)
				var content = makeMarkerContent(baiduEntity)
		    	content += makeMarkerButton(mode, baiduEntity.entityId, baiduEntity.name, baiduEntity.typeId)
		    	var marker = baiduEntity.marker;
		    	//marker.openInfoWindow(new BMap.InfoWindow(content)); 
		    	marker.addEventListener('click',function(event){  
		    		marker.openInfoWindow(new BMap.InfoWindow(content));  
		        });  
			});
		},
		error : function() {
			alert("error")
		},
		cache : false
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
	mode = 'view';
	addMapListener();
	
});

function autoSetArrPosition() { //设置右侧箭头在屏幕中间;
	var h = $(window).height(), h2 = (h - 140) / 2;
	if (h2 < 0) {
		h2 = 0;
	}
	$("#baiduMapSideArrow").css({
		top : h2
	});
}

function keydownEvent() {
    var e = window.event || arguments.callee.caller.arguments[0];
    if (e && e.keyCode == 13 ) {
    	$("#topoSearchLinkBtn").click()
    }
}
function refresh(){
	window.location.reload(); 
}
</script>
	</head>
	<body class="openWinBody" onkeydown="">
		<div class="tR_btn"><a onclick="refresh()" href="javascript:;" class="normalBtn"><span><i class="miniIcoRefresh"></i>@COMMON.refresh@</span></a></div>
		<div id="topPart" class="topoTopPart">
			<div id="normalTopPart" class="topBarSelected">
				<div class="topoBaiduSelectInput">
					<a href="javascript:;" class="baiduSearchLinkBtn"
						id="topoSearchLinkBtn"></a> <input type="text"
						id="topoSearchInput" onkeydown="keydownEvent()" class="baiduSearchInput" value="" />
				</div>
			</div>
		</div>
		<div id="searchResultPanel"
			style="border: 1px solid #C0C0C0; width: 150px; height: auto; display: none;"></div>
		<div id="allmap" style="cursor: crosshair;"></div>
		<div id="baiduMapSide">
			<div id="results"></div>
		</div>
		<div class="baiduSide" id="baiduMapSideArrow">
			<div class="cmListSideArrLeft" id="arrow" style="top:65px;"></div>
		</div>
	</body>
</Zeta:HTML>
