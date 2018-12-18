//Google map
var googleMap = null;
var requesting;
// 地址解析器
var geocoder;

function initialize() {
	if (typeof google == 'undefined') {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.GOOGLE.networkWrong);
		return false;
	}
	size = new google.maps.Size(48, 48);
	geocoder = new google.maps.Geocoder();
	var myLatlng = new google.maps.LatLng(latitude, longitude);
	/*
	 * var scaleControl = new google.maps.ScaleControlOptions({ position :
	 * google.maps.ControlPosition.LEFT_BOTTOM })
	 */
	var myOptions = {
		zoom : zoom,
		center : myLatlng,
		scaleControl : true,
		// scaleControlOptions:scaleControl,
		streetViewControl : false,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	}
	googleMap = new google.maps.Map(document.getElementById("googleMap"),
			myOptions);
	google.maps.event.addListener(googleMap, "center_changed", function() {
		var center = googleMap.getCenter();
		if (window.googleSaveTimer)
			window.clearTimeout(window.googleSaveTimer);
		window.googleSaveTimer = window.setTimeout(function() {
			$.ajax({
				url : '../google/saveGoogleLocation.tv',
				type : 'POST',
				data : {
					latitude : center.lat(),
					longitude : center.lng(),
					zoom : googleMap.getZoom()
				},
				success : function() {
					window.googleSaveTimer = null;
				},
				error : function() {
				},
				dataType : 'plain',
				cache : false
			});
		}, 1000);
	});
	google.maps.event.addListener(googleMap, "zoom_changed", function() {
		zoomChanged();
		var center = googleMap.getCenter();
		if (window.googleSaveTimer)
			window.clearTimeout(window.googleSaveTimer);
		window.googleSaveTimer = window.setTimeout(function() {
			$.ajax({
				url : '../google/saveGoogleLocation.tv',
				type : 'POST',
				data : {
					latitude : center.lat(),
					longitude : center.lng(),
					zoom : googleMap.getZoom()
				},
				success : function() {
					window.googleSaveTimer = null;
				},
				error : function() {
				},
				dataType : 'plain',
				cache : false
			});
		}, 1000);
	});
	return true;
}

/*******************************************************************************
 * 添加一个新的标记
 ******************************************************************************/
function onNewMarkerClick() {
	var myPos = !!window.latLngs ? window.latLngs : googleMap.getCenter();
	// /clear latLng
	window.latLngs = null;
	var newMarker = new google.maps.Marker({
		position : myPos,
		draggable : true,
		map : googleMap
	});
	newMarker.newMark = true;
	// google.maps.event.addListener(marker, "dragstart", function() {
	// googleMap.closeInfoWindow(); });
	google.maps.event.addListener(newMarker, "dragend", function(e) {
		selectionModel.selected = newMarker;
		// note the marker
		buildConfigWindow(newMarker, e)
	});
	google.maps.event.addListener(newMarker, 'dbclick', function(e) {
		selectionModel.selected = newMarker;
		// note the marker
		buildConfigWindow(newMarker, e)
	})
	google.maps.event.addListener(newMarker, 'click', function(e) {
		selectionModel.selected = newMarker;
		// note the marker
		buildConfigWindow(newMarker, e)
	})
	markerGallery.push(newMarker);
	// 创建完就加设备
	selectionModel.selected = newMarker;
	buildConfigWindow(newMarker)
}
/*******************************************************************************
 * 加载其他已添加到拓扑图的标记 TODO:如果某些标记不在当前的视野中，是否会产生BUG
 ******************************************************************************/
function loadMarkers() {
}

function confirmSaveLocation(m) {
	if (window.requesting) {
		window.requesting.abort();
		return;
	}
	window.lnglat = m.getPosition();
	/**
	 * 构造窗口
	 */
	var buildConfigWindow_ = function(geo) {
		window.geoLocation = geo;
		content = "<div style='text-align:left;'>"
				+ window.geoLocation
				+ "</div><br><br><div style='text-align:left;'>"
				+ I18N.GOOGLE.moveToPosition
				+ "</div><button onclick='SaveLocation()' style='text-align:right;'>"
				+ I18N.COMMON.ok + "</button>"
		if (window.infowindow) {
			window.infowindow.setContent(content);
		} else {
			window.infowindow = new google.maps.InfoWindow({
				content : content
			});
		}
		infowindow.open(googleMap, m);
	}
	geocoder.geocode({
		"address" : m.getPosition().toUrlValue()
	}, function(results, status) {
		if (results.length == 0) {
			window.parent.showMessageDlg(I18N.COMMON.tip,
					I18N.GOOGLE.positionCannotFind);
		} else {
			buildConfigWindow_(results[0].formatted_address);
		}
	});
}

function SaveLocation() {
	var position = selectionModel.selected.getPosition();
	var zoomSize = selectionModel.selected.zoom;
	$.ajax({
		url : '/google/saveEntity2GoogleMap.tv',
		cache : false,
		method : 'post',
		dataType : 'plain',
		data : {
			entityId : selectionModel.selected.entityId,
			latitude : position.lat(),
			longitude : position.lng(),
			zoom : zoomSize,
			location : window.geoLocation
		},
		success : function() {
			window.infowindow.close()
			// selectionModel.selected.setMap(null)
			onRefreshClick()
		},
		error : function() {
			// window.parent.showMessageDlg(I18N.NETWORK.error,
			// I18N.NETWORK.add2GoogleMapError, 'error');
		}
	})

}

/**
 * 构造标记窗口：负责查询地理位置
 */
function buildConfigWindow(m, e) {
	if (window.requesting) {
		window.requesting.abort();
		return;
	}
	window.lnglat = m.getPosition();
	/**
	 * 构造窗口
	 */
	var buildConfigWindow_ = function(geo) {
		window.geoLocation = geo;
		var content = "<div  style='text-align:left;'><div>"
				+ I18N.GOOGLE.currentPosition + ": " + geo + "</div>";
		$.ajax({
					url : '/google/queryAvaibleDevice.tv',
					method : 'post',
					cache : false,
					dataType : 'json',
					success : function(json) {
						content += '<br>' + I18N.GOOGLE.chooseEntity
								+ ': <select id="targetDevice">';
						if (0 == json.list.length) {
							content += "<option>" + I18N.GOOGLE.noAddedEntity
									+ "</option>"
						} else {
							for ( var i = 0; i < json.list.length; i++) {
								content += "<option value="
										+ json.list[i].entityId + ">"
										+ json.list[i].displayText
										+ "</option>"
							}
							content += '</select></div><br><button style="text-align:right;" onclick="saveMarker()">'
									+ I18N.COMMON.ok + '</button>'
						}
						if (window.infowindow) {
							window.infowindow.setContent(content);
						} else {
							window.infowindow = new google.maps.InfoWindow({
								content : content
							});
						}
						infowindow.open(googleMap, m);
					},
					error : function() {
					}
				});
	}
	// /查询地理位置
	geocoder.geocode({
		"address" : m.getPosition().toUrlValue()
	}, function(results, status) {
		if (results.length == 0) {
			window.parent.showMessageDlg(I18N.COMMON.tip,
					I18N.GOOGLE.positionCannotFind);
		} else {
			buildConfigWindow_(results[0].formatted_address);
		}
	});

}

/**
 * 保存marker的配置
 */
function saveMarker() {
	var targetEntityId = $("#targetDevice").val();
	var position = lnglat;
	// var text = $("#textMarker").val();
	var position = selectionModel.selected.getPosition();
	$.ajax({
		url : '/google/saveEntity2GoogleMap.tv',
		cache : false,
		method : 'post',
		dataType : 'plain',
		data : {
			entityId : targetEntityId,
			latitude : position.lat(),
			longitude : position.lng(),
			zoom : googleMap.getZoom(),
			location : window.geoLocation
		},
		success : function() {
			window.infowindow.close()
			// selectionModel.selected.setDraggable(false);
			selectionModel.selected.setMap(null)
			onRefreshClick()
		},
		error : function() {
			window.parent.showMessageDlg(I18N.NETWORK.error,
					I18N.NETWORK.add2GoogleMapError, 'error');
		}
	})
}

/**
 * 设备右键菜单
 * 
 * @param position
 * @param vs
 * @param vertex
 */
function showEntityMenu(entity) {
    var items = [];
    if(!EntityType.isOnuType(entity.typeId)){
    	items[items.length] = {id:"view2",text: I18N.COMMON.view,handler: onEntitySnapClick};//查看
        /**
         * modified by huangdongsheng 
         * 右键菜单中去掉 CCMTS设备的工具
         */
        if(!isCcmtsType(entity.type)){
        	items[items.length] = {
        			text: I18N.NETWORK.tool, 
        			menu: [//工具
    			    	{text: I18N.NETWORK.ping, handler: onPingClick},
    			    	{text: I18N.NETWORK.tracert, handler: onTracertClick}
    			    	//{text: I18N.NETWORK.nativeTelnet, handler: onNativeTelnetClick}
    			    	]};
        }
        
       // if(refreshEquipment){
            items[items.length] = {text: I18N.NETWORK.discoveryAgain, iconCls: 'bmenu_refresh', handler: onDiscoveryAgainClick};//刷新设备
       // }
        //--------get additional menu dynamically----//
    	if(!isCcmtsType(entity.type)){
    		$.ajax({
    	        type: "GET",
    	        url: "/olt/js/extendMenu.js", 
    	        dataType: "script",
    	        async : false,
    	        success  : function () {
    	            items = extendOper(items,entity.entityId,entity.ip);
    	        }
    	    });
    	}        
        items[items.length] = '-';
    }
    
    //items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};//打开超链接
   // if (editTopoPower) {
        items[items.length] = {id:"fixentity",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};//固定位置
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
   // }
   /*//----设备名称是否可修改---//
    if(editDeviceName){
    	//----注意注意的id可能或组的id重叠-----//
        items[items.length] = {id:"renameEntity",text: I18N.COMMON.rename, handler: onRenameClick};//重命名
    }*/
    //items[items.length] = '-';
    //items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
    // delete entityMenu @niejun;
    //----如果设备菜单不存在 @bravin----//
    if (!entityMenu) {
        entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    } else {
    	//---存在bug--//
        entityMenu.removeAll();
        entityMenu.add(items)
    }
    entityMenu.items.items[0].addClass("first_menu");
    //----设置菜单内的固定位置状态----//
    var cmp = Ext.getCmp('fixentity');
    if (cmp != null) {
        if (entity.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);//固定位置
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);//解锁位置
        }
    }
    entityMenu.showAt([window.event.x,window.event.y]);
}
function onEntitySnapClick() {
	if (selected != null) {
		Ext.menu.MenuMgr.hideAll();
		/*
		 * 8800B的快照的Tab页的格式与其他设备的不一致 增加8800B类型验证 added by huangdongsheng
		 */
		if(isCcmtsType(selected.type)){
			window.parent.addView('entity-' + selected.entityId,  selected.name , 'entityTabIcon',
            '/cmc/showCmcPortal.tv?cmcId=' + selected.entityId);
		}else{
			window.top.addView('entity-' + selected.entityId,   selected.name ,
            'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + selected.entityId);
		}

		var state = window.top.propertyPanelState;
		if (state != null && state == 2) {
			window.top.minPropertyPanel();
		}
	}
}
function onPingClick() {
	if (selected != null) {
		window.top
				.createDialog("modalDlg", 'Ping ' + selected.ip, 600, 400,
						"entity/runCmd.tv?cmd=ping&ip=" + selected.ip, null,
						true, true);
	}
}
function onTracertClick() {
	if (selected != null) {
		window.top.createDialog("modalDlg", 'Tracert ' + selected.ip, 600, 400,
				"entity/runCmd.tv?cmd=tracert&ip=" + selected.ip, null, true,
				true);
	}
}
function onNativeTelnetClick() {
	if (selected != null) {
		var hwin = window.open('telnet://' + selected.ip, 'ntelnet'
				+ selected.entityId);
		hwin.close();
	}
}

function isCmc8800CAORA(type) {
	var CC8800A_TYPE = 30001;
	var CC8800C_A_TYPE = 30005;
	if (type != null && (CC8800A_TYPE == type || CC8800C_A_TYPE == type)) {
		return true;
	}
	return false;
}

function onDiscoveryAgainClick() {
	if (selected != null) {
		if (isCmc8800CAORA(selected.typeId)) {
			window.parent.showWaitingDlg(I18N.MENU.wait, String.format(
					I18N.NETWORK.discoveryingAgain, selected.mac),
					'waitingMsg', 'ext-mb-waiting');
			$.ajax({
				url : "../cmc/refreshCC.tv",
				data : {
					cmcId : selected.entityId,
					cmcType : selected.typeId,
					entityId : selected.parentId
				},
				dataType : 'plain',
				success : function() {
					window.top.closeWaitingDlg();
					window.top.showMessageDlg(I18N.COMMON.tip,
							I18N.NETWORK.discoverySuccess);
					onRefreshClick();
				},
				error : function() {
					window.top.closeWaitingDlg();
					window.top.showMessageDlg(I18N.COMMON.tip,
							I18N.NETWORK.notDiscoveryEntityAgain);
				}
			});
		} else {
			window.top.discoveryEntityAgain(selected.entityId, selected.ip, function() {
				onRefreshClick();
			});
		}
	}
}
function onQuickSet() {
	if (selected != null) {
		window.parent.createDialog("quickSetDlg", I18N.COMMON.quickSet, 600,
				450, "/ap/showQuickSetWnd.tv", null, true, true);
	}
}
function onOpenURLClick() {
	if (selected != null) {
		var url = selected.url;
		if (url != null && url != '') {
			Ext.menu.MenuMgr.hideAll();
			if (!url.startWith("http://") && !url.startWith("https://")) {
				url = "http://" + url;
			}
			window.top.addView('url' + selected.entityId, selected.name,
					"topoLeafIcon", url);
		} else {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.GRAPH.hrefMessage);
			onEntityPropertyClick();
		}
	}
}
function onFixLocationClickOne() {
	selected.fixed = selected.fixed ? false : true;
	$.ajax({
		url : '/google/fixlocation.tv',
		cache : false,
		method : 'post',
		data : {
			entityId : selected.entityId,
			fixed : selected.fixed
		},
		success : function() {
			selected.setDraggable(selected.fixed);
		},
		error : function() {
		}
	})

}

function onEntityPropertyClick() {
	if (selected != null) {
		window.top.showProperty('google/showEntityProperty.tv?entityId='
				+ selected.entityId);
	}
}
function onLinkPropertyClick() {
	if (selected != null) {
		window.top.showProperty('link/showLinkPropertyJsp.tv?linkId='
				+ selected.entityId);
	}
}
function onDeleteClick() {
	if (selected != null) {
		Ext.menu.MenuMgr.hideAll();
		window.parent.showConfirmDlg(I18N.COMMON.tip,
				I18N.NETWORK.deleteFromGoogleMap, function(type) {
					if (type == 'no') {
						return;
					}
					Ext.Ajax.request({
						url : 'deleteGoogleEntity.tv',
						method : 'POST',
						params : {
							entityId : selected.entityId
						},
						success : function(response) {
							loadEntities();
						},
						failure : function() {
							window.parent.showMessageDlg(I18N.NETWORK.error,
									I18N.NETWORK.deleteFromGoogleMapError,
									'error');
						}
					});
				});
	}
}