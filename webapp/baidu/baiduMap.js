var map;
var searchAddress = new Array();//搜索出来的地址。记录下来用于下次搜索之前清除上次搜索出来的地址
var entityMap = {};
var curLocation;
var curLongitude;
var curLatitude;
var mode = 'view'//当前模式 添加/修改/查看   用于生成地图tip按钮
var modifyMarker;

/*以下部分为添加地图上控件*/
var top_left_control = new BMap.ScaleControl({
	anchor : BMAP_ANCHOR_TOP_LEFT
});// 左上角，添加比例尺
var top_left_navigation = new BMap.NavigationControl(); // 左上角，添加默认缩放平移控件

// 添加定位控件
var geolocationControl = new BMap.GeolocationControl();
geolocationControl.addEventListener("locationSuccess", function(e) {
	// 定位成功事件
});
geolocationControl.addEventListener("locationError", function(e) {
	// 定位失败事件
});
// 添加控件和比例尺
function add_control() {
	map.addControl(top_left_control);
	map.addControl(top_left_navigation);
	map.addControl(geolocationControl);// 定位控件
}

/*添加设备marker到百度地图*/
function addEntity(baiduEntity) {
	iconUrl = "../images/" + baiduEntity.icon32;
	var zoom = map.getZoom();
	var iconUrl = "../images/" + baiduEntity.icon48
	var icon = new BMap.Icon(iconUrl, new BMap.Size(48, 48));
	if(zoom < 10){
		iconUrl = "../images/" + baiduEntity.icon16
		icon = new BMap.Icon(iconUrl, new BMap.Size(16, 16));
	}
	if(zoom < 15 && zoom > 9){
		iconUrl = "../images/" + baiduEntity.icon32
		icon = new BMap.Icon(iconUrl, new BMap.Size(32, 32));
	}
	marker = new BMap.Marker(new BMap.Point(baiduEntity.longitude, baiduEntity.latitude), {
		icon : icon
	});
	marker.setTitle(baiduEntity.name)
	map.addOverlay(marker); // 将标注添加到地图中
	marker.disableDragging(); // 可拖拽
	entityMap[baiduEntity.entityId] = baiduEntity;
	var content = makeMarkerContent(baiduEntity)
	content += makeMarkerButton(mode, baiduEntity.entityId, baiduEntity.name, baiduEntity.typeId)
	if(mode != 'view'){
		marker.openInfoWindow(new BMap.InfoWindow(content)); 
	}
	marker.addEventListener('click',function(event){  
		marker.openInfoWindow(new BMap.InfoWindow(content));  
    });  
	baiduEntity.marker = marker;
	if(mode == 'modify'){
		curLongitude = baiduEntity.longitude
		curLatitude = baiduEntity.latitude
		curLocation = baiduEntity.location
	}
	return marker;
}

/*搜索方法*/
function serachlocal(){  
	//清空上次搜索的地址信息
	for(var i = 0; i < searchAddress.length; i++){
		map.removeOverlay(searchAddress[i])
	}
	
	searchAddress = new Array();
	//先搜索设备，再搜索地图
	var searchTxt = $.trim($("#topoSearchInput").val())
	if(mode == 'view'){
		var size = 0;
	    $.ajax({
	        url: '/baidu/searchEntity.tv',
	        type: 'POST',
	        data: {searchTxt: searchTxt},
	        async: false, 
	        dateType:'json',
	        success:function(response) {
	        	var marker;
	        	size = response.length
	        	if(size > 0){
	        		//搜索时右侧弹出栏内容
	                var html='<ol style="list-style: none outside none; padding: 0px; margin: 0px;">'; 
	                for(var i = 0;i < size; i++){  
	                    html += makeSearchContent(i, response[i].name, response[i].location)
	                }  
	                html+="</ol>";  
	                $("#results").html(html);  
	                
	                for(var i = 0;i < size; i++){  
	                    (function(){  
	                    	var entityId = response[i].entityId;
	                    	var name = response[i].name;
	                    	var typeId = response[i].typeId;
	                    	var content = makeMarkerContent(response[i])
	                        var index = i + 1;  
	                        content += makeMarkerButton(mode, entityId, name, typeId)
	                        var info = new BMap.InfoWindow(content)  
	                        $("#poi" + i).click(function(){  
	                        	entityMap[entityId].marker.openInfoWindow(info); 
	                            var position = marker.getPosition();  
	                            curLocation = poi.address;
	                            curLongitude = position.lng;
	                            curLatitude = position.lat;
	                        });  
	                    })();  
	                }  
	                showHideSide("show");  
	                map.panTo(new BMap.Point(response[0].longitude, response[0].latitude))
	                $("#poi0").click()
	                return;
	        	}
	        },
	        error:function() {
	        },
	        cache:false
	    });
	}
    
    var markerArray=new Array();  
    var local = new BMap.LocalSearch(map, {       
    renderOptions: {       
        map: map,       
        autoViewport: true,   
        selectFirstResult: false   //不指定到第一个目标    
    },       
    pageCapacity: 8,  
    //自定义marker事件  
    onMarkersSet:function(pois){  
        for(var i=0;i<pois.length;i++){  
            (function(){  
                var index=i;  
                var curPoi=pois[i];  
                var curMarker=pois[i].marker;  
                markerArray[i]=curMarker;  
                content='<span style="color:#36c;"><b>'+ curPoi.title +"</b></span>";  
                content+="<div class='nm3kBubbleBaidu'> <p><b>@baidu/BAIDU.address@: </b> "+ curPoi.address +"</p>";
                content += makeMarkerButton(mode)
                
                curMarker.addEventListener('click',function(event){  
                    var info=new BMap.InfoWindow(content);  
                    curMarker.openInfoWindow(info);  
                    var position=curMarker.getPosition();  
                    curLocation = curPoi.address;
                    curLongitude = position.lng;
                    curLatitude = position.lat;
                });  
                searchAddress.push(curMarker)
            })();  
        }  
          
    },  
    //自定义搜索回调数据  
    onSearchComplete:function(results){  
        if(local.getStatus() == BMAP_STATUS_SUCCESS){  
        	//搜索时右侧弹出栏内容
            var html='<ol style="list-style: none outside none; padding: 0px; margin: 0px;">';  
            for(var i = 0; i < results.getCurrentNumPois(); i++){  
                var poi = results.getPoi(i);  
                html += makeSearchContent(i, poi.title, poi.address)
            }  
            if(mode == 'modify'){
            	var bYposition=2-i*20; 
            	var tempHtml = '';
            	tempHtml += '<li id="poi'+ i +'" index="'+ i +'" style="margin: 0px 0px; padding: 10px 5px 10px 10px; overflow: hidden; line-height: 17px;"><span style="color:#36c;">';  
            	tempHtml += '<a onclick="addModifyMarker()" href="javascript:; "class="normalBtn">'
            	tempHtml += '<span><i class="miniIcoEdit"></i>@baidu/BAIDU.location@</span></a></span></li>';  
                html += tempHtml
            }
            html+="</ol>";  
            $("#results").html(html);  
            showHideSide("show");  
            
            for(var i=0;i<results.getCurrentNumPois();i++){  
                (function(){  
                    var index=i+1;  
                    var poi=results.getPoi(i);  
                    content='<span style="color:#36c;"><b>'+poi.title+"</b></span>";  
                    content+="<div class='nm3kBubbleBaidu'> <p><b>@baidu/BAIDU.address@: </b> "+poi.address+"</p>";  
                    content += makeMarkerButton(mode)
                    var info=new BMap.InfoWindow(content)  
                    $("#poi"+i).click(function(){  
                        var marker = markerArray[$(this).attr('index')]
                        marker.openInfoWindow(info); 
                        var position=marker.getPosition();  
                        curLocation = poi.address;
                        curLongitude = position.lng;
                        curLatitude = position.lat;
                    });  
                })();  
            }  
        }  
    }
    });  
    local.search(searchTxt); 
}  
//构造右侧弹出搜索框内容
function makeSearchContent(i, title, address){
	var bYposition=2-i*20; 
	var html = '';
	html += '<li class="baiduSearchResult" id="poi'+ i +'" index="'+ i +'" style="margin: 0px 0px; padding: 10px 5px 10px 10px; cursor: pointer; overflow: hidden; line-height: 17px;">';  
    html += '<span style="background:url(http://api.map.baidu.com/bmap/red_labels.gif) 0 '+ bYposition + 'px no-repeat;padding-left:10px;margin-right:3px"></span>'  
    html += '<span style="color:#36c;"><b>'+ title + '</b>' + '</span>';  
    html += '<p style="color:#666;">'+ address+'</p>'  
    html += '</li>';  
    return html
}

//构造marker的tip内容
function makeMarkerContent(baiduEntity){
	var content="<div class='nm3kBubbleBaidu'> <p><b>@network/NETWORK.alias@@COMMON.maohao@</b> "+ baiduEntity.name + "</p>";  
	content += "<p><b>@network/batchTopo.entityType@@COMMON.maohao@</b> " + baiduEntity.displayName + "</p>";
	content += "<p><b>MAC@COMMON.maohao@</b> " + baiduEntity.mac + "</p>"; 
	content += "<p><b>@network/NETWORK.manageIp@@COMMON.maohao@</b> "+ baiduEntity.ip + "</p>"; 
	content += "<p><b>@baidu/BAIDU.address@@COMMON.maohao@</b> " + baiduEntity.location + "</p>"; 
	if(baiduEntity.state == 1){
		content += "<p><b>@baidu/BAIDU.currentStatus@@COMMON.maohao@</b> @network/label.online@</div> </p>"; 
	}else{
		content += "<p><b>@baidu/BAIDU.currentStatus@@COMMON.maohao@</b> @network/label.deviceLinkDown@</div> </p>"; 
	}
	
	return content;
}

function addModifyMarker(){
	iconUrl = "http://api.map.baidu.com/bmap/red_labels.gif";
	modifyMarker = new BMap.Marker(new BMap.Point(curLongitude, curLatitude));
	modifyMarker.setTitle(curLocation)
	var content = '<h3 class="orangeTxt" style="padding: 2px;">@baidu/BAIDU.addressError@</h3>';  
    content += '<div class="nm3kBubbleBaidu" style="padding: 2px;">@baidu/BAIDU.pushIconToAddress@</div>';  
    content += '<div class="nm3kBubbleBaidu" style="padding: 2px;">@baidu/BAIDU.address@@COMMON.maohao@<input id="location" type="text" class="normalInput" maxlength="128"/></div>'; 
    content += '<div class="noWidthCenterOuter clearBoth"><ol class="upChannelListOl pB0 pT20 noWidthCenter"><li>'
    	+ '<a onclick="modifyBaiduEntity()" href="javascript:;"class="normalBtn"><span><i class="miniIcoEdit"></i>@baidu/BAIDU.move@</span></a></li></ol>';
    var info=new BMap.InfoWindow(content) 
    
    info.addEventListener('close',function(event){  
		map.removeOverlay(modifyMarker); 
    });
                        
	map.addOverlay(modifyMarker); // 将标注添加到地图中
	modifyMarker.enableDragging(); // 可拖拽
	modifyMarker.openInfoWindow(info); 
	$("#location").val(curLocation);
}

//构造marker的tip按钮
function makeMarkerButton(mode, entityId, name, typeId){
	var content = ''
	if(mode == 'view' && entityId != null){
		content =  '<div class="noWidthCenterOuter clearBoth">';
		content +=    '<ol class="upChannelListOl pB0 pT20 noWidthCenter">' ;
		content +=        String.format('<li><a onclick="viewEntity({0}, \'{1}\', {2})"' + 'href="javascript:;"class="normalBtn"><span><i class="miniIcoView"></i>@baidu/BAIDU.viewEntity@</span></a></li>', entityId, name, typeId)
		content +=        '<li><a onclick="deleteEntity(' +　entityId + ')" href="javascript:;"class="normalBtn"><span><i class="miniIcoClose"></i>@baidu/BAIDU.removeEntity@</span></a></li>'
		content +=    '</ol>';
		content += '</div>';
	}/*else if(mode == 'modify'){
    	content += '<div class="noWidthCenterOuter clearBoth"><ol class="upChannelListOl pB0 pT20 noWidthCenter"><li>'
        	+ '<a onclick="modifyBaiduEntity()" href="javascript:;"class="normalBtn"><span><i class="miniIcoEdit"></i>@baidu/BAIDU.move@</span></a></li></ol>'; 
    }*/else if(mode == 'add'){
    	content += '<div class="noWidthCenterOuter clearBoth"><ol class="upChannelListOl pB0 pT20 noWidthCenter"><li>'
        	+ '<a onclick="addNewEntityToMap()" href="javascript:;"class="normalBtn"><span><i class="miniIcoAdd"></i>@network/sendConfig.addEntity@</span></a></li></ol>'; 
    }
	return content;
}

//从百度地图上移除设备方法
function deleteEntity(entityId){
	window.parent.showConfirmDlg(I18N.COMMON.tip, "@baidu/BAIDU.confirmDelete@", function(button, text) {
        if (button == "yes") {
        	$.ajax({
                url:'/baidu/deleteEntityFromBaiduMap.tv',
                type:'POST',
                data:{entityId: entityId},
                dateType:'json',
                success:function(response) {
                	//从地图上删除
                	map.removeOverlay(entityMap[entityId].marker)
                	top.afterSaveOrDelete({
           				title: I18N.COMMON.tip,
           				html: '<b class="orangeTxt">' + "@baidu/BAIDU.confirmOK@" + '</b>'
           			});
                },
                error:function() {
                	top.afterSaveOrDelete({
           				title: I18N.COMMON.tip,
           				html: '<b class="orangeTxt">' + "@baidu/BAIDU.confirmFail@" + '</b>'
           			});
                },
                cache:false
            });
        }
    });
}

//添加设备到百度地图方法
function addNewEntityToMap() {
	var zoom = map.getZoom();
    $.ajax({
        url:'/baidu/addEntityToBaiduMap.tv',
        type:'POST',
        data:{entityId: entityId, longitud: curLongitude, latitude: curLatitude,  location: curLocation, zoom: zoom},
        dateType:'json',
        success:function(response) {
        	mode = "modify"
        	baiduEntity = response;
        	addEntity(baiduEntity)
        	emptySerch();
        },
        error:function() {
        },
        cache:false
    });
}

function viewEntity(entityId, name, typeId){
	if (EntityType.isCcmtsWithAgentType(typeId)) {
        window.parent.addView('entity-' + entityId, unescape(name), 'entityTabIcon',
                '/cmc/showCmcPortal.tv?cmcId=' + entityId + "&productType=" + typeId);
    }else if(EntityType.isCmtsType(typeId)) {
    	window.parent.addView('entity-' + entityId, unescape(name), 'entityTabIcon',
                '/cmts/showCmtsPortal.tv?cmcId=' + entityId);
    }else if(EntityType.isCcmtsWithoutAgentType(typeId)) {
        window.parent.addView('entity-' + entityId, unescape(name), 'entityTabIcon',
                '/cmc/showCmcPortal.tv?cmcId=' + entityId);
    }else{
    	window.parent.addView('entity-' + entityId,  unescape(name),  'entityTabIcon',
    			'portal/showEntitySnapJsp.tv?entityId=' + entityId);
    }
}

function modifyBaiduEntity() {
	if(modifyMarker != null){
		var longitude = modifyMarker.getPosition().lng;  
		var latitude = modifyMarker.getPosition().lat;  
		var location = $("#location").val().trim();
		if(location == null || location == ''){
			top.afterSaveOrDelete({
       				title: I18N.COMMON.tip,
       				html: '<b class="orangeTxt">' + '@BAIDU.pleaseEnterAddress@' + '</b>'
       			});
			return;
		}
		baiduEntity.longitude = longitude;
		baiduEntity.latitude = latitude;
		baiduEntity.location = location;
	}else{
		baiduEntity.longitude = curLongitude;
		baiduEntity.latitude = curLatitude;
		baiduEntity.location = curLocation;
	}
	
	var zoom = map.getZoom();
    $.ajax({
        url:'/baidu/modifyBaiduEntity.tv',
        type:'POST',
        data:{entityId: entityId, longitud: longitude, latitude: latitude,  location: location, zoom: zoom},
        dateType:'json',
        success:function(response) {
            map.clearOverlays();
            addEntity(baiduEntity)
            var content = makeMarkerContent(baiduEntity)
        	/* content += '<div class="noWidthCenterOuter clearBoth"><ol class="upChannelListOl pB0 pT20 noWidthCenter"><li>'
        	+ '<a onclick="addMarker()" href="javascript:;"class="normalBtn"><span><i class="miniIcoForbid"></i>@BAIDU.modifyAddress@</span></a></li></ol>'; */  
        	marker = baiduEntity.marker;
        	marker.openInfoWindow(new BMap.InfoWindow(content)); 
        	marker.addEventListener('click',function(event){  
        		marker.openInfoWindow(new BMap.InfoWindow(content));  
            });  
        	map.panTo(new BMap.Point(baiduEntity.longitude, baiduEntity.latitude))
        	emptySerch();
        },
        error:function() {
        },
        cache:false
    });  
}


/*查询框关键字提示*/
var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
	{"input" : "topoSearchInput"
	,"location" : map
});

ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
var str = "";
var _value = e.fromitem.value;
var value = "";
	if (e.fromitem.index > -1) {
		value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
	}    
	str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
	value = "";
	if (e.toitem.index > -1) {
		_value = e.toitem.value;
		value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
	}    
	str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
	document.getElementById("searchResultPanel").innerHTML = str;
});

var myValue;
ac.addEventListener("onconfirm", function(e) {    
	//鼠标点击下拉列表后的事件
	var _value = e.item.value;
	myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
	setPlace();
});

function setPlace(){
	function myFun(){
		var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
		map.centerAndZoom(pp, 18);
		map.addOverlay(new BMap.Marker(pp));    //添加标注
	}
	var local = new BMap.LocalSearch(map, { //智能搜索
	  onSearchComplete: myFun
	});
	serachlocal(myValue);
}

function showHideSide(str) { //展开，折叠;
	switch (str) {
	case "show":
		$("#arrow").removeClass("cmListSideArrLeft").addClass(
				"cmListSideArrRight");
		$("#baiduMapSideArrow").animate({
			right : 269
		});
		$("#baiduMapSide").animate({
			right : 0
		});
		break;
	case "hide":
		$("#arrow").removeClass("cmListSideArrRight").addClass(
				"cmListSideArrLeft");
		$("#baiduMapSideArrow").animate({
			right : 0
		});
		$("#baiduMapSide").animate({
			right : -350
		});
		break;
	}
}

function emptySerch(){
	$("#results").html("");
	$("#topoSearchInput").attr("value", "")
	showHideSide("hide")
}

function addMapListener(){
	map.addEventListener('zoomend',function(event){  
		saveCurrentLocation();
			var zoom = map.getZoom();
			//更改设备ICON大小
			for(var key in entityMap){
				var marker = entityMap[key].marker;
				var iconUrl = "../images/" + entityMap[key].icon48
				var icon = new BMap.Icon(iconUrl, new BMap.Size(48, 48));
				if(zoom < 10){
					iconUrl = "../images/" + entityMap[key].icon16
					icon = new BMap.Icon(iconUrl, new BMap.Size(16, 16));
				}
				if(zoom < 15 && zoom > 9){
					iconUrl = "../images/" + entityMap[key].icon32
					icon = new BMap.Icon(iconUrl, new BMap.Size(32, 32));
				}
				marker.setIcon(icon);
				};
	});

	map.addEventListener('dragend',function(event){  
		saveCurrentLocation();
	});

}

function saveCurrentLocation(){
	var zoom = map.getZoom();
	var longitude = map.getCenter().lng;
	var latitude = map.getCenter().lat;
    $.ajax({
        url:'/baidu/saveBaiduMapCenter.tv',
        type:'POST',
        data:{longitud: longitude, latitude: latitude, zoom: zoom},
        dateType:'json',
        success:function(response) {
        },
        error:function() {
        },
        cache:false
    }); 
}
