var SHOW_TYPE = { NAME: 2, SYSNAME: 1, IP: 0};
var USEROBJ_TYPE = { ENTITY: 1, FOLDER: 2, SHAPE: 3, LINK: 4 ,OLT: 10000 , CCMTS:30000, CC8800A: 30001,CC8800B: 30002,CC8800C_A: 30005, SFU:13000,AP:20000,AL8101:20001,AL8102:20002,AL8103:20003,UNKNOWN:-1};

var SHAPE_TYPE = {
	POINTER: 0, SQUARE: 1, MOVE: 2, SELECTALL: 3,
	STRAIGHT_CONNECT: 5, HORIZONAL_CONNECT: 6, VERTICAL_CONNECT: 7,
	ARROW_CONNECT: 8, ENTITY_CONNECT: 9,
	FOLDLINE_CONNECT: 10, POLYLINE_CONNECT: 11,
	IMAGE: 50, CLOUDY: 51, SUBNET: 52, BACKGROUND: 53,
	CONTAINER: 95, CONTAINER_ELLIPSE: 96, CONTAINER_PARALL: 97,
	ACTOR: 102, CLOUD: 103, HEXAGON: 104, RHOMBUS: 105, PARALLEOGRAM: 115,
	TRIANGLE: 106, ROUNDRECT: 107, TEXTRECT: 108, OVAL: 109, RECT: 110,
	PENTACLE: 111, TEXT: 120, HREF: 121,
	RECTLABEL: 122, ROUNDLABEL: 123,
	VIRTUALNET : 201
};

var ALERT_ICON = [];
ALERT_ICON[0] = '../images/fault/level0.gif';
ALERT_ICON[1] = '../images/fault/level1.gif';
ALERT_ICON[2] = '../images/fault/level2.gif';
ALERT_ICON[3] = '../images/fault/level3.gif';
ALERT_ICON[4] = '../images/fault/level4.gif';
ALERT_ICON[5] = '../images/fault/level5.gif';
ALERT_ICON[6] = '../images/fault/level6.gif';

var refreshTick;
var graph;
var model;
var selectionModel;
var groupListener;
var jtb;
var contextMenu;
var entityMenu;
var entityMenuMult;
var folderMenu;
var folderMenuMult;
var shapeMenu;
var shapeMenuMult;
var virtualNetMenu;
var edgeMenu;
var shapeIdSequence = 10000;
var drawingDlg;
var curDrawingType = SHAPE_TYPE.POINTER;
var connectStartFlag = false;
var connectSource = null;
var connectTarget = null;
var connector = null;
var scrollFlag = false;
var scrollX = 0;
var scrollY = 0;
var offsetX = 0;
var offsetY = 0;
var dispatcherTimer = null;
var dispatcherInterval = 5000;
var dispatcherCount = 0;
var selectedCount = 0;
var totalFlag = 0;
var isSubnetMap = false;
var editTopoPower = true;
var remoteMode = false;
var openMode = null;
var displayName = true;
var displaySysName = false;
var showType = 1;
var displayLink = true;
var displayLinkLabel = true;
var displayEntityLabel = true;
var displayAlertIcon = true;
var markerAlertMode = true;
var backgroundFlag = true;
var backgroundColor = '#FFFFFF';
var backgroundImg = '';
var displayGrid = false;
var backgroundGrid = 'url(../css/grid_bg.gif)';
var displayNoSnmpEntity = true;
var zoomSize = 1;
var splashCount = 0;
var splashFlag = true;
var gotoId = -1;
var grouped = true;
var automousezoon = false;
var extendStatus = [];

function isCmc8800CAORA(type){
	var CC8800A_TYPE = USEROBJ_TYPE.CC8800A;
	var CC8800C_A_TYPE = USEROBJ_TYPE.CC8800C_A;
	if(type != null && (CC8800A_TYPE == type || CC8800C_A_TYPE == type)){
		return true;
	}
	return false;
}

//var microGraph;
function ZGOnload() {
	if (!remoteMode) {
		//---创建工具栏---//
		buildToolbar();
		offsetY = jtb.getHeight();
	}

	var c = document.getElementById('graphContainer');

	c.style.top = offsetY;
	//---创建一个拓扑图画布---//
	graph = new ZetaGraph(c, folderWidth/zoomSize, folderHeight/zoomSize);
	//---调整画布大小--//
	resizeGraph();

	showBackground();

//	microGraph = new ZetaMicroGraph(document.getElementById('mGraph'), graph, 0, offsetY);
	selectionModel = graph.getSelectionModel();
    selectionModel.setSquareHelper({border: '1px solid #316AC5', background: '#D2E0F0', opacity: 0.5});
	model = graph.getModel();
	//---为画布添加监听---//
	addListener();
	//--@important 按照缩放比例对graph进行缩放调整 --//
	graph.zoom(zoomSize);
	if (!editTopoPower) {
		graph.setEditable(false);
	} else {
		graph.setResizable(true);
	}

	loadData(gotoId, false);
	var el = Zeta$('tick' + 100*zoomSize);
	if (el != null) {
		el.src = '../images/zgraph/tick-on.gif';
	}
	var item = Ext.getCmp('zoom' + 100*zoomSize);
	if (item != null) {
		item.setChecked(true)
	}
	//---定时获取数据---//
	startTimer();
}
function onOpenUrl() {
	var selectee = selectionModel.getSelection();
	if(selectee != null){
		var userObj = selectee.getUserObject();
		window.open("http://"+userObj.ip+"/cgi-bin/webproc?getpage=html%2Findex.html&errorpage=html%2Fmain.html&var%3Amenu=status&var%3Apage=system_msg&var%3Alogin=true&obj-action=auth&%3Ausername=admin&%3Apassword=password&%3Aaction=check&%3Asessionid=787f9705");
	}
}
/**
 * 退出页面时清空计时器，销毁拓扑图（未实现）
 */
function ZGUnload() {
	stopTimer();
	graph.destroy();
}
/*
 * 
 */
function resizeGraph() {
	var w = $(window).width();
	if (isSubnetMap) {
		graph.getView().setWidth(w - 20);
	} else {
		graph.setWidth(w);
	}
	graph.setHeight($(window).height() - offsetY);
}
function resizeTopoFolderSize(w, h) {
    var graphContainer = document.getElementById('graphContainer');
    if (graphContainer != null) {
        //graphContainer.style.width = w;
	    //graphContainer.style.height = h;
    }
    var zetaGraph = document.getElementById('zetaGraph');
    if (zetaGraph != null) {
        zetaGraph.style.width = w;
	    zetaGraph.style.height = h;
    }
}

/**
 * 为拓扑图全局添加监听，比如组的创建,线的绘制，重命名等等在此处理
 */
function addListener() {
	$(window).bind('unload', ZGUnload);
	$(window).bind('resize', function() { resizeGraph(); });
	if (remoteMode) return;
    graph.addListener('mousedown', function(evt) {
		goToEntity(0);
		//right click
		if(evt.button == 2)return;
		//left click
		if (curDrawingType == SHAPE_TYPE.POINTER) {
		} else {
			if (curDrawingType == SHAPE_TYPE.MOVE) {
				scrollFlag = true;
				//----什么时候重新渲染graph的----//
				scrollX = evt.pageX;
				scrollY = evt.pageY;
			//----Draw bridge Line : STRAIGHT_CONNECT || POLYLINE_CONNECT ----//
			} else if (curDrawingType >= SHAPE_TYPE.STRAIGHT_CONNECT && // >=5 && <=6
				curDrawingType <= SHAPE_TYPE.POLYLINE_CONNECT) {
				//---bridge entity by line---// 
				if (!connectStartFlag) {
					if (evt.target != evt.currentTarget && evt.target.tagName != 'DIV') {
						var src = evt.target;
						while(src.parentNode.id != 'zetaGraph') {
							src = src.parentNode;
						}
						var vertex = model.getVertex(src.id);
						if (vertex != null && vertex.userObject.objType != USEROBJ_TYPE.SHAPE) {
							//---able to draw line---//
							connectStartFlag = true;
							connectSource = vertex;
						}
					}
				}
			} 
		}
	});
	graph.addListener('mouseup', function(evt) {
		if(evt.button == 2)return;//right click
		if (curDrawingType == SHAPE_TYPE.POINTER) {
			//---show cluetip----//
			showStatusBarInfo();
		} else if (curDrawingType == SHAPE_TYPE.MOVE) {
			//----end of move---//
			scrollFlag = false;
		} else if (curDrawingType >= SHAPE_TYPE.STRAIGHT_CONNECT &&
			curDrawingType <= SHAPE_TYPE.POLYLINE_CONNECT) {
			// connect
			if (connectStartFlag) {
				//----TODO target属性对于IE应该不支持才对啊,currentTarget这个自定义属性哪里来的——--//
				if (evt.target != evt.currentTarget && evt.target.tagName != 'DIV') {
					var src = evt.target;
					while(src.parentNode.id != 'zetaGraph') {
						src = src.parentNode;
					}
					var vertex = model.getVertex(src.id);
                    if (vertex != null && vertex != connectSource && vertex.userObject.objType != USEROBJ_TYPE.SHAPE) {
						connectTarget = vertex;
						var src = connectSource;
						var dst = connectTarget;
						var name = 'new link';
						$.ajax({url:'../link/insertNewLink.tv',
							data: {'srcEntityId': src.getUserObject().nodeId,
							'destEntityId':  dst.getUserObject().nodeId,
							folderId: topoFolderId, name: name, connectType: curDrawingType},
							dataType: 'json', cache:'false',
							success: function(json) {
                                var renderer = null;
								if (curDrawingType == SHAPE_TYPE.STRAIGHT_CONNECT) {
									renderer = null;
								} else if (curDrawingType == SHAPE_TYPE.HORIZONAL_CONNECT) {
									renderer = new HorizonalZetaEdgeRenderer(json.id, '', src.getAnchor(), dst.getAnchor())
								} else if (curDrawingType == SHAPE_TYPE.POLYLINE_CONNECT) {
									renderer = new PolylineZetaEdgeRenderer(json.id, '', src.getAnchor(), dst.getAnchor())
								} else if (curDrawingType == SHAPE_TYPE.FOLDLINE_CONNECT) {
									renderer = new FoldLineZetaEdgeRenderer(json.id, '', src.getAnchor(), dst.getAnchor())
								}
								var edge = new ZetaEdge(json, src, dst, renderer);
								graph.insertEdge(edge);
							}, error: function(){}
						});
					}
				}
				connectStartFlag = false;
				connectSource = connectTarget = null;
				graph.stopDrawConnector();
			}
		}else{
			//----计算出当前鼠标的绝对位置----//
			var x = graph.getScrollLeft() + evt.pageX;
			var y = graph.getScrollTop() + evt.pageY - offsetY;
			//---按照工具类型在当前位置画一个该图形---//
			drawShape(curDrawingType, x, y);
		}
	});
    graph.addListener('mousemove', function(evt) {
		if (curDrawingType == SHAPE_TYPE.MOVE) {
			if (scrollFlag) {
				var left = graph.getScrollLeft() + scrollX - evt.pageX;
				if (left < 0) left = 0;
				var top = graph.getScrollTop() + scrollY - evt.pageY;
				if (top < 0) top = 0;
				graph.setScrollLeft(left);
				graph.setScrollTop(top);
				scrollX = evt.pageX;
				scrollY = evt.pageY;
			}
		//----Draw bridge Line : STRAIGHT_CONNECT || POLYLINE_CONNECT ----//
		} else if (curDrawingType >= SHAPE_TYPE.STRAIGHT_CONNECT &&
			curDrawingType <= SHAPE_TYPE.POLYLINE_CONNECT) {
			if (connectStartFlag) {				
				//---draw  line,from start:from == to---//
				graph.startDrawConnector(connectSource);
				var left = graph.getScrollLeft() + evt.pageX;
				if (left < 0) left = 0;
				var top = graph.getScrollTop() + evt.pageY - offsetY;
				if (top < 0) top = 0;
				//----style.zoom的影响，会使e.pageX,Y产生放大,需要还原----//
				graph.moveConnector(left/zoomSize, top/zoomSize);
			}
		} else if (curDrawingType >= SHAPE_TYPE.CONTAINER) {
			window.top.setCoordInfo(
				(graph.getScrollLeft() + evt.pageX) + ' : '
				+ (graph.getScrollTop() + evt.pageY  - offsetY));
		}
	});
    graph.addListener('dblclick', function(evt) {
		if (curDrawingType == SHAPE_TYPE.POINTER && evt.target.tagName != 'DIV') {
			var selectee = selectionModel.getSelection();
			if (selectee != null) {
				var entityId = selectee.getUserObject().userObjId;
				var url = selectee.getUserObject().url;
				Ext.menu.MenuMgr.hideAll();
				if (url != null && url != '') {
					if (!url.startWith("http://") && !url.startWith("https://")) {
			                url = "http://"+url;
			        }
					window.top.addView("url" + entityId, 'URL',  "topoLeafIcon", url);
				} else {
					var type = selectee.getUserObject().objType;
					if (type == USEROBJ_TYPE.ENTITY) {
						onEntitySnapClick();
					} else if (type == USEROBJ_TYPE.FOLDER) {
						window.top.addView("topo" + entityId, selectee.getUserObject().name,
							"topoRegionIcon", 'topology/getTopoMapByFolderId.tv?folderId=' + entityId);
					} else if (type == USEROBJ_TYPE.SHAPE) {
						var v = selectee.getUserObject().type;
						if(v == SHAPE_TYPE.VIRTUALNET){
							var virtualNetId = selectee.getUserObject().id;
							window.top.addView('entity-' + virtualNetId,
									I18N.COMMON.virtual + '[' + selectee.getUserObject().text + ']',
									'entityTabIcon', 'topology/showVitualDevice.tv?virtualNetId=' + virtualNetId+"&folderId="+topoFolderId);
							return;
						}
						if (selectee.isGroupable()) {
							expandGroup(selectee, !selectee.isExpanded());
						}
					} else if (type == USEROBJ_TYPE.LINK) {
						
					}else if (type == USEROBJ_TYPE.UNKNOWN) {
						
					}
				}
			}
		}
	});
    //-----节点重命名操作后触发----//
	graph.addRenameListener(function(vertex, newName) {
		//去掉名字中的换行符,否则在打开地域时会导致语法错误
		newName = newName.replace(/\r\n|\n/g,"");
		if(newName.length>63){
			window.parent.showMessageDlg(I18N.MENU.tip,I18N.topoGraph.note1,"error");
			onRefreshClick();
			return;
		}
		var userObj = vertex.getUserObject();
		var type = userObj.objType;
		//var entityId = userObj.userObjId;
		var entityId = userObj.nodeId;
		//DEVICE
		if (type == USEROBJ_TYPE.ENTITY) {
			$.ajax({url: '../entity/renameEntity.tv', type: 'POST',  cache: false, dataType: 'plain',
				data: {folderId: topoFolderId, entityId: entityId, name: newName},
				success: function() {
                    window.top.getMenuFrame().doRefresh();
                },
				error: function() {}
			});
		//folder-region
		} else if (type == USEROBJ_TYPE.FOLDER) {
			var _folerId = userObj.userObjId
			$.ajax({url: '../topology/renameTopoFolder.tv', type: 'POST', cache: false, dataType: 'plain',
				dataType: 'json',
				data: {superiorId: superiorFolderId, folderId: _folerId, oldName: userObj.name, name: newName},
				success: function(json) {
					if (json.exists) {
						onRefreshClick();
						window.top.showMessageDlg(I18N.MENU.tip, I18N.NETWORK.folderExist);
						return;
					}
					userObj.name = newName;
					var frame = window.top.getMenuFrame();
					try {
						frame.renameTopoFolder(entityId, newName);
					} catch (err) {
					}
                    window.top.getMenuFrame().doRefresh();
				},
				error: function() {}
			});
		//group
		} else if (type == USEROBJ_TYPE.SHAPE) {
			//var param = 'mapNode.nodeId=' + entityId+ '&mapNode.text=' + newName + '&mapNode.folderId=' + topoFolderId;
			switch(userObj.type){
				case SHAPE_TYPE.VIRTUALNET:
					var virtualNetId = userObj.id;
					$.ajax({
						url: '../virtualnet/renameVirtualNet.tv',
						method:'post',
						data:{virtualNetId:virtualNetId,virtualNetName:newName},
						cache:false,
						success:function(){},
						error:function(){}
					})
					return;
				default:
					break;
			}
			var param = 'nodeId=' + entityId+ '&text=' + newName + '&folderId=' + topoFolderId;
			$.ajax({url: '/topology/setMapNodeText.tv', type: 'POST', cache: false, 
				data: param,
				success : function() {
					window.top.getMenuFrame().doRefresh();
				},
				error: function() {}
			});
		}
    });
	//------拓扑图中的所有右键操作-----//
	graph.addPopupMenu(function(evt, vertex, edge) {
        var vs = selectionModel.getSelectedVertexs();
        if (vs.length > 1) {
			var objType = vertex.getUserObject().objType;
			if (objType == USEROBJ_TYPE.ENTITY) {//-----1
				showEntityMenuMult([evt.pageX, evt.pageY], vs,vertex);
			} else if (objType == USEROBJ_TYPE.FOLDER) {//-----2
				showFolderMenuMult([evt.pageX, evt.pageY],vs,vertex);
			} else if (objType == USEROBJ_TYPE.SHAPE) {//-----3
				showShapeMenuMult([evt.pageX, evt.pageY], vs,vertex);
			}
            return;
        }
        if (vertex == null && edge == null) {//blank
			showContextMenu([evt.pageX, evt.pageY]);
		} else if (vertex == null) {//line
			showEdgeMenu([evt.pageX, evt.pageY]);
		} else {//entity,region,group
			var objType = vertex.getUserObject().objType;
			if (objType == USEROBJ_TYPE.ENTITY) {
				var type = vertex.getUserObject().type;
				//modify by @bravin ： 以大类 type来区分设备，而不以小类typeid来区分
				switch(type){
					case USEROBJ_TYPE.CCMTS:
						showCmcEntityMenu([evt.pageX, evt.pageY], vertex);
						break;
					case USEROBJ_TYPE.SFU:
						showOnuEntityMenu([evt.pageX, evt.pageY], vertex);
						break;
					case USEROBJ_TYPE.UNKNOWN:
						showUnknownEntityMenu([evt.pageX, evt.pageY], vertex);
						break;
					default:
						showEntityMenu([evt.pageX, evt.pageY], vertex);
						break;
				}
			} else if (objType == USEROBJ_TYPE.FOLDER) {
				showFolderMenu([evt.pageX, evt.pageY],vertex);
			} else if (objType == USEROBJ_TYPE.SHAPE) {
				switch(vertex.getUserObject().type){//判断图形type
					case SHAPE_TYPE.VIRTUALNET://虚拟子网
						showVirtualNetMenu([evt.pageX, evt.pageY], vertex);
						break;
					default: //other
						showShapeMenu([evt.pageX, evt.pageY], vertex);
				}
			}
		}
	});
	graph.addResizeListener(function(vertex) {
		var param = 'mapNode.nodeId=' + vertex.getUserObject().nodeId +
			'&mapNode.width=' + vertex.getWidth() + '&mapNode.height=' + vertex.getHeight();
		$.ajax({
			url: '../topology/setMapNodeSize.tv', cache: false,
			data: param
		});
	});
	groupListener = {
		childAdded: function(group, child) {
			//----当有一个网络节点加入后，将这个节点信息进行保存---//
			$.ajax({
				url: '../topology/updateMapNodeGroup.tv', cache: false,
				data: {nodeId: child.getUserObject().nodeId, folderId: topoFolderId, groupId: group.getUserObject().nodeId},
				success:function(){
					//---方便起见，直接将所有信息进行保存，或者可以只保存该设备的位置信息-----//
					onSaveClick();
				}
			});
		},
		childRemoved: function(group, child) {
			//----当有一个网络节点被移除后，将这个节点信息进行保存---//
			$.ajax({
				url: '../topology/updateMapNodeGroup.tv', cache: false,
				data: {nodeId: child.getUserObject().nodeId, folderId: topoFolderId, groupId: 0},
				success:function(){
						//---方便起见，直接将所有信息进行保存，或者可以只保存该设备的位置信息-----//
						onSaveClick();
				}
			});
		}
	};
	graph.addDropTargetListener(function(evt, ui) {
		var $item = ui.draggable;
		var id = $item.attr('drawingType');
		var x = graph.getScrollLeft() + evt.pageX;
		var y = graph.getScrollTop() + evt.pageY - offsetY;
		drawShape(id, x, y);
	}, 'drawing');
	/**
	 * 缩放操作
	 */
    graph.addZoomListener(function(size) {
		var old = zoomSize;
		var el = Zeta$('tick' + 100*size);
		if (el != null) {
			el.src = '../images/zgraph/tick-on.gif';
		}
		el = Zeta$('tick' + 100*old);
		if (el != null) {
			el.src = '../images/zgraph/tick-off.gif';
		}
		var item = Ext.getCmp('zoom' + 100*size);
		if (item != null) {
			item.setChecked(true);
		}
		item = Ext.getCmp('zoom' + 100*old);
		if (item != null) {
			item.setChecked(false);
		}
		zoomSize = size;
		if (editTopoPower) {
			$.ajax({url: '../topology/setTopoMapZoom.tv', cache: false,
				data: {'topoFolder.folderId':topoFolderId, 'topoFolder.zoom': size}
			});
		}
	});
	selectionModel.addSelectionListener({
		selecting: function(evt, ui) {
			selectedCount++;
			setStatusBarInfo(String.format(I18N.NETWORK.selectedCountMsg, selectedCount));
		},
		unselecting: function(evt, ui) {
			selectedCount--;
			setStatusBarInfo(String.format(I18N.NETWORK.selectedCountMsg, selectedCount));
		},
		selected: function(evt, ui) {
		},
		unselected: function(evt, ui) {
		},
		start: function(evt, ui) {
			selectedCount = selectionModel.getCount();
		},
		stop: function(evt, ui) {
			selectedCount = 0;
			totalFlag = 0;
		}
	});
}

/**
 * 按工具类型绘制一个图形
 * @param type
 * @param x
 * @param y
 */
function drawShape(type, x, y) {
	var vertex = null;
	var id = 'cell' + shapeIdSequence++;
	if (type == SHAPE_TYPE.IMAGE) { insertPicture(); } else if (type == SHAPE_TYPE.CLOUDY) { insertCloudy(x, y); } else if (type == SHAPE_TYPE.SUBNET) { insertSubnet(x, y);
	} else {
		var shadowed = false;
		if (type == SHAPE_TYPE.CONTAINER) {
			vertex = new ZetaVertex({id: id, x: x, y: y}, new DefaultGroupZetaCellRenderer(id, I18N.GRAPH.group, x, y));
			vertex.setStrokeColor('#ffc7c7');
			vertex.setFillColor('#ffc7c7');
			vertex.setGradient(true);
			vertex.setGradientColor('#ffffff');
			vertex.setGroupable(true, groupListener);
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.CONTAINER_ELLIPSE) {
			vertex = new ZetaVertex({id: id, x: x, y: y}, new EllipseGroupZetaCellRenderer(id, I18N.GRAPH.group, x, y));
			vertex.setGroupable(true, groupListener);
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.CONTAINER_PARALL) {
			vertex = new ZetaVertex({id: id, x: x, y: y}, new ParallGroupZetaCellRenderer(id, I18N.GRAPH.group, x, y));
			vertex.setGroupable(true, groupListener);
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.ACTOR) {
			vertex = new ZetaVertex({id: id}, new ActorZetaCellRenderer(id,'',x,y,40,60));
			vertex.setStrokeColor('#ffc7c7');
			vertex.setFillColor('#ffc7c7');
			vertex.setGradient(true);
			vertex.setGradientColor('#ffffff');
			vertex.setShadowVisible(true);
			graph.insertVertex(vertex);
			shadowed = true;
		} else if (type == SHAPE_TYPE.CLOUD) {
			vertex = new ZetaVertex({id: id}, new CloudZetaCellRenderer(id,'',x,y,80,60));
			vertex.setStrokeColor('#cdeb8b');
			vertex.setFillColor('#cdeb8b');
			vertex.setGradient(true);
			vertex.setGradientColor('#ffffff');
			vertex.setShadowVisible(true);
			graph.insertVertex(vertex);
			shadowed = true;
		} else if (type == SHAPE_TYPE.PENTACLE) {
			vertex = new ZetaVertex({id: id}, new PentacleZetaCellRenderer(id,'',x,y,60,60));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.RHOMBUS) {
			vertex = new ZetaVertex({id: id}, new RhombusZetaCellRenderer(id,'',x,y,60,60));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.PARALLEOGRAM) {
			vertex = new ZetaVertex({id: id}, new ParallZetaCellRenderer(id,'',x,y,80,40));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.HEXAGON) {
			vertex = new ZetaVertex({id: id}, new HexagonZetaCellRenderer(id,'',x,y,80,60));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.TRIANGLE) {
			vertex = new ZetaVertex({id: id}, new TriangleZetaCellRenderer(id,'',x,y,40,60));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.RECT) {
			vertex = new ZetaVertex({id: id}, new RectZetaCellRenderer(id,'',x,y,80,40));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.ROUNDRECT) {
			vertex = new ZetaVertex({id: id}, new RoundRectZetaCellRenderer(id,'',x,y,80,40));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.OVAL) {
			vertex = new ZetaVertex({id: id}, new OvalZetaCellRenderer(id,'',x,y,60,60));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.RECTLABEL) {
			vertex = new ZetaVertex({id: id}, new LabelZetaCellRenderer(id,'',x,y,80,40));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.ROUNDLABEL) {
			vertex = new ZetaVertex({id: id}, new LabelZetaCellRenderer(id,'',x,y,80,40));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.TEXT) {
			vertex = new ZetaVertex({id: id}, new TextZetaCellRenderer(id,'',x,y));
			graph.insertVertex(vertex);
		} else if (type == SHAPE_TYPE.HREF) {
			vertex = new ZetaVertex({id: id}, new HrefZetaCellRenderer(id,'',x,y));
			graph.insertVertex(vertex);
		} else if(type == SHAPE_TYPE.VIRTUALNET) { //virtual sub network
			vertex = new ZetaVertex({id: id,x:x,y:y}, new CloudZetaCellRenderer(id,'',x,y));
			graph.insertVertex(vertex);
			//虚拟子网单独处理
			$.ajax({
				url: '../virtualnet/createVirtualNet.tv', type: 'post', dataType: 'json', cache: false,
				data: {'virtualNetAttribute.folderId': topoFolderId, 'virtualNetAttribute.virtualType': type,
					   'virtualNetAttribute.x': x, 'virtualNetAttribute.y': y,
					   'virtualNetAttribute.width': vertex.getWidth(), 'virtualNetAttribute.height': vertex.getHeight(),
					   'virtualNetAttribute.virtualName': I18N.NETWORK.virtualnet,
					   'virtualNetAttribute.visiable': 1
			    },
	            success: function(json) {
	                vertex.setUserObject(json);
	                vertex.setCellId(json.id);
	                vertex.setText(json.name);
	                vertex.setJid("#"+json.id);
	                model.putVertex(vertex);
	            }});
			return;
		}
		$.ajax({
			url: '../topology/insertMapNode.tv', type: 'post', dataType: 'json', cache: false,
			data: {'mapNode.folderId': topoFolderId, 'mapNode.type': type,
				   'mapNode.x': x, 'mapNode.y': y,
				   'mapNode.width': vertex.getWidth(), 'mapNode.height': vertex.getHeight(),
				   'mapNode.text': vertex.getText(), 'mapNode.fillColor': vertex.getFillColor(),
				   'mapNode.strokeColor': vertex.getStrokeColor(),
				   'mapNode.gradient': true,
				   'mapNode.shadow': shadowed
		    },
            success: function(json) {
//                    parseTopoNode(json);
                vertex.setUserObject(json);
                vertex.setCellId(json.id);
                vertex.setJid("#"+json.id);
                model.putVertex(vertex);
            }});
	}
}

/**
 * 创建顶部工具栏以及工具栏菜单
 */
function buildToolbar() {
	jtb = new Ext.Toolbar();
	jtb.render('toolbar');
	var items = [];
	if (editTopoPower) {
		if (!isSubnetMap) {
			items[items.length] = {text: I18N.COMMON.save, iconCls: 'bmenu_save',cls:'mL10', handler: onSaveClick}
		}
//		items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
        items[items.length] = {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}
        items[items.length] = {text: I18N.topo.picProperties, iconCls:'bmenu_href' , handler: onPropertyClick}
		items[items.length] = '-';
	}
	/*items[items.length] = new Ext.Toolbar.SplitButton({
		text: I18N.COMMON.print, handler: onPrintClick, iconCls: 'bmenu_print', menu: {items: [
			{text: '<b>' + I18N.COMMON.print + '</b>', handler: onPrintClick},
			{text: I18N.COMMON.printPreview, handler: onPrintPreviewClick}
		]}
	})
	items[items.length] = '-';*/
    items[items.length] = {text: I18N.COMMON.find, iconCls: 'bmenu_find', handler: onFind1Click};
//	items[items.length] = new Ext.Toolbar.SplitButton({
//		text: I18N.COMMON.find, handler: onFindClick, iconCls: 'bmenu_find', menu: {items: [
//			{text: '<b>'+I18N.COMMON.fullFind +'</b>', handler: onFindClick},
//			{text:I18N.COMMON.similarFind, handler: onFind1Click}
//		]}
//	});

	if (editTopoPower && !isSubnetMap) {
		items[items.length] = new Ext.Toolbar.SplitButton({
			text: I18N.COMMON.tool, iconCls: 'bmenu_tool', handler: onShowDrawingClick, menu: {minWidth: 150, items: [
				{text: '<b>' + I18N.NETWORK.drawTools + '</b>', handler: onShowDrawingClick}, '-',
//				{text: I18N.NETWORK.showMicroGraph, handler: onShowMicroGraphClick},
//				{text: I18N.NETWORK.synMicroGraph, handler: onSynMicroGraphClick}, '-',
				{text: I18N.NETWORK.saveAsPicture, handler: onSaveAsPictureClick}, '-',
				{text: I18N.NETWORK.labelAndLegend, handler: onLabelAndLegendClick}
			]}
		})
	}
	items[items.length] = new Ext.Toolbar.SplitButton({
		text: I18N.GRAPH.zoom, iconCls: 'bmenu_find', handler: onAutoZoomClick,
		menu: {minWidth: 150, items: [
			{id: "automousezoon",text: I18N.GRAPH.activeAutoZoom, handler: onAutoZoomClick}, '-',//激活鼠标滚轮缩放
			{id: 'zoom500', text: '500%', group: 'zoom', checked: (zoomSize==5?true:false), handler: onZoomFitClick500},
			{id: 'zoom300', text: '300%', group: 'zoom', checked: zoomSize==3?true:false , handler: onZoomFitClick300},
			{id: 'zoom200', text: '200%', group: 'zoom', checked: (zoomSize==2?true:false), handler: onZoomFitClick200},
			{id: 'zoom150', text: '150%', group: 'zoom', checked: (zoomSize==1.5?true:false), handler: onZoomFitClick150},
			{id: 'zoom125', text: '125%', group: 'zoom', checked: (zoomSize==1.25?true:false), handler: onZoomFitClick125},
			{id: 'zoom100', text: '100%', group: 'zoom', checked: (zoomSize==1?true:false), handler: onZoomFitClick100},
			{id: 'zoom75', text: '75%', group: 'zoom', checked: (zoomSize==0.75?true:false), handler: onZoomFitClick75},
			{id: 'zoom50', text: '50%', group: 'zoom', checked: (zoomSize==0.5?true:false), handler: onZoomFitClick50},
			{id: 'zoom25', text: '25%', group: 'zoom', checked: (zoomSize==0.25?true:false), handler: onZoomFitClick25},
			{id: 'zoom10', text: '10%', group: 'zoom', checked: (zoomSize==0.1?true:false), handler: onZoomFitClick10}, '-',
	    	{text: I18N.GRAPH.customLevel, handler: onZoomCustomClick}//自定义
	    ]}
	})

	if (!isSubnetMap) {
		items[items.length] = new Ext.Toolbar.SplitButton({
			text: I18N.COMMON.arrange, iconCls: 'bmenu_arrange', handler: onDefaultArrangeClick, menu: {minWidth: 150, items: [
				{text: '<b>' + I18N.GRAPH.defaultArrange + '</b>', handler: onDefaultArrangeClick},
				{text: I18N.GRAPH.circular, handler: onCircularArrangeClick}]}
		})
	}
	items[items.length] = '-';
	items[items.length] = {text:I18N.COMMON.view, iconCls: 'bmenu_view', menu: {minWidth: 150, items: [
		{text: '<b>' + I18N.NETWORK.topoMap + '</b>', group: 'viewMenu1', checked: true, checkHandler: onViewByTopoMapClick},
//		{text: I18N.NETWORK.icon, group: 'viewMenu1', checked: false, checkHandler: onViewByIconClick},
		{text: I18N.NETWORK.table, group: 'viewMenu1', checked: false, checkHandler: onViewByDetailClick}, '-',
		{text: I18N.COMMON.openInNewWindow, handler: openFolderInNewWindow}]}
	}
//	items[items.length] = '-';
//	items[items.length] = {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick};
	jtb.add(items);
	jtb.doLayout();
}

/**
 * 点击拓扑图中空白处的菜单
 * @param position
 */
function showContextMenu(position) {
    if (contextMenu == null) {
		var items = [];
		items[items.length] = {id: 'backSuperiorItem', text: I18N.NETWORK.returnSuperiorFolder, handler: onReturnSuperiorClick};
		items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.selectAll, handler: onSelectAllEntityClick};
		items[items.length] = {text: I18N.COMMON.save, iconCls: 'bmenu_save', handler: onSaveClick};
		items[items.length] = {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick};
		if (editTopoPower) {
		    items[items.length] = {id: 'pasteItem', text: I18N.COMMON.paste, iconCls: 'bmenu_paste', handler: onPasteClick};
		    items[items.length] = '-';
		}
//		items[items.length] = {text: I18N.COMMON.view, menu: [
//        	{text: I18N.NETWORK.topoMap, checked: true, group: 'view', handler: onViewByTopoMapClick},
//			{text: I18N.NETWORK.icon, checked: false, group: 'view', handler: onViewByIconClick},
//        	{text: I18N.NETWORK.detail, checked: false, group: 'view', handler: onViewByDetailClick}, '-',
//        	{text: I18N.COMMON.openInNewWindow, handler: openFolderInNewWindow}
//        ]};
//		if (!isSubnetMap) {
//			items[items.length] = {text: I18N.GRAPH.layout, menu: [
//				{text: I18N.GRAPH.defaultArrange, handler: onDefaultArrangeClick},
//				{text: I18N.GRAPH.circular, handler: onCircularArrangeClick}
//			]};
//		}
//		items[items.length] = {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick};
//		items[items.length] = '-';
//		if (editTopoPower) {
//			if(!isSubnetMap) {
//				items[items.length] = {text: I18N.COMMON.select, menu: [
//					{text: I18N.COMMON.selectAll, handler: onSelectAllClick}, '-',
//					{text: I18N.GRAPH.allVertex, handler: onSelectVertexsClick},
//					{text: I18N.GRAPH.allEdge, handler: onSelectEdgesClick}, '-',
//					{text: I18N.NETWORK.selectAllEntity, handler: onSelectAllEntityClick},
//					{text: I18N.NETWORK.selectSnmpEntity,handler: onSelectSnmpEntityClick}
//				]};
//		    }
//		    items[items.length] = {id: 'pasteItem', text: I18N.COMMON.paste, iconCls: 'bmenu_paste', handler: onPasteClick};
//		    items[items.length] = '-';
//		}
		items[items.length] = {text: I18N.NETWORK.saveAsPicture, handler: onSaveAsPictureClick};
		items[items.length] = {text: I18N.NETWORK.labelAndLegend, handler: onLabelAndLegendClick};
		items[items.length] = '-';
		items[items.length] = {text: I18N.COMMON.property, handler: onPropertyClick};
		contextMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	}
	var clipboard = window.top.getZetaClipboard();
	var item = contextMenu.get('pasteItem');
	if (item != null) { if (clipboard != null && clipboard.src != topoFolderId && clipboard.target == 'topoFolder') { item.enable(); } else { item.disable(); } }
	if (superiorFolderId > 1) { contextMenu.get('backSuperiorItem').enable(); } else { contextMenu.get('backSuperiorItem').disable(); }
	contextMenu.showAt(position);
}

var b = true;

/**
 * 设备节点右键菜单处理器:for olt
 * @param position
 * @param vs
 * @param vertex
 */
function showEntityMenu(position, vertex) {
	var status = vertex.getUserObject().status;
    var items = [];
    items[items.length] = {id:"view2",text: I18N.COMMON.view,handler: onEntitySnapClick};//查看
    if(status){
    	items[items.length] = {text: I18N.NETWORK.tool, menu: [//工具
	        {text: I18N.NETWORK.ping, handler: onPingClick},
	        {text: I18N.NETWORK.tracert, handler: onTracertClick},
	        {text: "Mibble Browser", handler: onMibBrowseClick}
       ]};
	  items[items.length] = {text: I18N.NETWORK.discoveryAgain, iconCls: 'bmenu_refresh', handler: onDiscoveryAgainClick};//刷新设备
	    //--------get additional menu dynamically----//
	     $.ajax({
	         type: "GET",
	         url: String.format("/{0}/js/extendMenu.js",vertex.getUserObject().module),
	         dataType: "script",
	         async : false,
	         success  : function () {
	        	 items = extendOper(items);
	         }
	    });
    }
    items[items.length] = '-';
    items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};//打开超链接
    if (googleSupported) {
        items[items.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:!googleMapPower};//添加到谷歌地图
    }
    if (editTopoPower) {
        items[items.length] = {id:"fixentity",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};//固定位置
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};//剪切
        items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};//复制
        items[items.length] = {text: I18N.COMMON.removeFromTopo, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
    }
   //----设备名称是否可修改---//
    	//----注意注意的id可能或组的id重叠-----//
    items[items.length] = {id:"showEntityMenu",text: I18N.COMMON.realias, handler: renameEntity};//重命名
    items[items.length] = '-';
    items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
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
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);//固定位置
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);//解锁位置
        }
    }
    entityMenu.showAt(position);
}

/**
 * 设备节点右键菜单处理器:for 未知类型
 * @param position
 * @param vs
 * @param vertex
 */
function showUnknownEntityMenu(position, vertex) {
	var status = vertex.getUserObject().status;
    var items = [];
    items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};//打开超链接
    if (editTopoPower) {
        items[items.length] = {id:"fixentity",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};//固定位置
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};//剪切
        items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};//复制
        items[items.length] = {text: I18N.COMMON.removeFromTopo, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
    }
   //----设备名称是否可修改---//
    	//----注意注意的id可能或组的id重叠-----//
    items[items.length] = {id:"showUnknownEntityMenu",text: I18N.COMMON.realias, handler: renameEntity};//重命名
    items[items.length] = '-';
    items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
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
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);//固定位置
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);//解锁位置
        }
    }
    entityMenu.showAt(position);
}

/**
 * 设备多选菜单：for olt
 * @param position
 * @param vs
 * @param vertex
 */
function showEntityMenuMult(position, vs,vertex) {
	if (entityMenuMult == null) {
        var items = [];
        if (editTopoPower) {
            items[items.length] = {id:"fixentitymult",text: I18N.GRAPH.fixLocation, handler: onFixLocationClick};
            items[items.length] = {id:"unfixentitymult",text: I18N.GRAPH.unfixLocation, handler: onUnFixLocationClick};
            //items[items.length] = {id:"quicksetmult",text: I18N.COMMON.quickSet, handler: onQuickSet};
            items[items.length] = '-';
            items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};
            items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};
            items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
			if (!isSubnetMap) {
				items[items.length] = {text: I18N.GRAPH.align, menu: [
                    {text: I18N.GRAPH.leftAlign, iconCls: 'bmenu_left', handler: onLeftAlignClick},
                    {text: I18N.GRAPH.centerAlign, iconCls: 'bmenu_center', handler: onCenterAlignClick},
                    {text: I18N.GRAPH.rightAlign, iconCls: 'bmenu_right', handler: onRightAlignClick}, '-',
                    {text: I18N.GRAPH.topAlign, iconCls: 'bmenu_top', handler: onTopAlignClick},
                    {text: I18N.GRAPH.middleAlign, iconCls: 'bmenu_middle', handler: onMiddleAlignClick},
                    {text: I18N.GRAPH.bottomAlign, iconCls: 'bmenu_bottom', handler: onBottomAlignClick}
                ]}
            }
        }
		entityMenuMult = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    }
//    var quickset = Ext.getCmp('quicksetmult');
//    if (vertex.userObject.type == I18N.COMMON.AP) {
//        quickset.enable();
//    } else {
//        quickset.disable();
//    }
    var fixentitymult = Ext.getCmp('fixentitymult');
    var unfixentitymult = Ext.getCmp('unfixentitymult');
    if (fixentitymult != null && unfixentitymult != null) {
        fixentitymult.disable();
        unfixentitymult.disable();
        for (var i = 0; i < vs.length; i++) {
            if (!vs[i].userObject.fixed) {
                fixentitymult.enable();
            } else {
                unfixentitymult.enable();
            }
        }
    }
    entityMenuMult.showAt(position);
}

/**
 * 显示CMCENTITY菜单
 * @param position
 * @param vertex
 */
function showCmcEntityMenu(position, vertex){
	 //isExpress, true: is express,false:is not express
	 var isExpress = vertex.getUserObject().virtualNetworkStatus != 1 ? true:false;
	 window.currentVertex = vertex;
	 switch(isExpress){
	 	case true:
	 		showCmcEntityMenu2(position, vertex);
	 		break;
	 	case false:
	 		showCmcEntityMenu1(position, vertex);
	 		break;
	 }
}

/**
 * 显示ONU ENTITY菜单，其中包括8800A
 * @param position
 * @param vertex
 */
function showOnuEntityMenu(position, vertex){
	window.currentVertex = vertex;
	var items = [];
	if (editTopoPower) {
        items[items.length] = {id:"fixentity",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};//固定位置
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};//剪切
        items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};//复制
        items[items.length] = {text: I18N.COMMON.removeFromTopo, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
    }
    items[items.length] = {id:"showCmcEntityMenu1",text: I18N.COMMON.realias, handler: renameEntity};//重命名
    if (!entityMenu) {
        entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    } else {
    	//---存在bug--//
        entityMenu.removeAll();
        entityMenu.add(items);
    }
  //----设置菜单内的固定位置状态----//
    var cmp = Ext.getCmp('fixentity');
    if (cmp != null) {
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);//固定位置
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);//解锁位置
        }
    }
    entityMenu.items.items[0].addClass("first_menu");
    entityMenu.showAt(position);
	
	
}

/**
 * 拓扑图上创建的CMC的右键
 * @param position
 * @param vertex
 */
function showCmcEntityMenu1(position, vertex){
    var items = [];
    items[items.length] = {id:"view2",text: I18N.COMMON.view,handler: onEntitySnapClick};//查看
    if(!isCmc8800CAORA(vertex.userObject.typeId)){ // 8800A不显示[工具]菜单
        items[items.length] = {
            text : I18N.NETWORK.tool,
            menu : [// 工具
            {
                text : I18N.NETWORK.ping,
                handler : onPingClick
            }, {
                text : I18N.NETWORK.tracert,
                handler : onTracertClick
            }
            // ,
            // {text: I18N.NETWORK.nativeTelnet, handler: onNativeTelnetClick}
            ]
        };
    }
    
    items[items.length] = {text: I18N.NETWORK.discoveryAgain, iconCls: 'bmenu_refresh', handler: onDiscoveryAgainClick};//刷新设备
//    //--------get additional menu dynamically----//
    var cmcId = vertex.userObject.nodeId;
    var entityId = vertex.userObject.userObjId;
    var ccMac = vertex.userObject.mac;
    var typeId = vertex.userObject.typeId;
    $.ajax({
        type: "GET",
        url: vertex.userObject.modulePath+"/js/extendMenu.js",
        dataType: "script",
        async : false,
        success  : function () {
            items = extendOper(items, entityId, cmcId , ccMac , typeId);
        }
    });

    items[items.length] = '-';
    items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};//打开超链接
    if (googleSupported) {
        items[items.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:!googleMapPower};//添加到谷歌地图
    }
    if (editTopoPower) {
        items[items.length] = {id:"fixentity",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};//固定位置
        items[items.length] = '-';
        items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};//剪切
        items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};//复制
        items[items.length] = {text: I18N.COMMON.removeFromTopo, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
    }
   //----设备名称是否可修改---//
    	//----注意注意的id可能或组的id重叠-----//
    items[items.length] = {id:"showCmcEntityMenu1",text: I18N.COMMON.realias, handler: renameEntity};//重命名
    items[items.length] = '-';
    items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
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
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);//固定位置
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);//解锁位置
        }
    }
    entityMenu.showAt(position);
}

/**
 * 虚拟子网上创建的CMC快捷方式的右键
 * @param position
 * @param vertex
 */
function showCmcEntityMenu2(position, vertex){
	var items = [];
    items[items.length] = {id:"view2",text: I18N.COMMON.view,handler: onEntitySnapClick};//查看
    //--------get additional menu dynamically----//
    items[items.length] = '-';
    if (googleSupported) {
        items[items.length] = {text:I18N.COMMON.addToGoogle, handler : onAddToGoogleClick,disabled:googleMapPower};//添加到谷歌地图
    }
   //----设备名称是否可修改---//
    	//----注意注意的id可能或组的id重叠-----//
    items[items.length] = {id:"deletecmcEntity",text: I18N.topoGraph.Deleteshortcut, handler: onExpressDeleteClick};//删除
    items[items.length] = '-';
    items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
    if (!entityMenu) {
        entityMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
    } else {
    	//---存在bug--//
        entityMenu.removeAll();
        entityMenu.add(items);
    }
    entityMenu.items.items[0].addClass("first_menu");
    entityMenu.showAt(position);
}

function onExpressDeleteClick(){
	var entityId = currentVertex.getUserObject().nodeId;
	currentVertex = null
	$.ajax({
		url:'/virtualnet/removeProductFromTopoGraph.tv',
		method:'post',
		cache:false,
		data:{entityId:entityId},
		dataType:'json',
		success:function(json){
			if(json.success){
				window.top.showMessageDlg(I18N.NETWORK.tip,I18N.topoGraph.Deleteshortcutsuccess)
				onRefreshClick();
			}else{
				window.top.showMessageDlg(I18N.NETWORK.tip,I18N.topoGraph.Deleteshortcutfail)
			}
		},
		error:function(){
			window.top.showMessageDlg(I18N.NETWORK.tip,I18N.topoGraph.Deleteshortcutfail)
		}
	})
}

/**
 * 组菜单
 * @param position
 * @param vertex
 */
function showShapeMenu(position, vertex) {
	if (shapeMenu == null) {
		var items = []; 
		items[items.length] = {id: 'expandItem', text: I18N.GRAPH.expand,iconCls:"bmenu_checked", handler: onGroupClick};
//		items[items.length] = {id: 'collapseItem', text: I18N.GRAPH.collapse, handler: onCollapseClick};
		items[items.length] = '-';
		items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};
        if (editTopoPower) {
            items[items.length] = {id: 'resizeItem', text: I18N.GRAPH.resize, handler: onResizeClick};
            items[items.length] = {id:"fixshape",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};
			items[items.length] = '-';
            //items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};
            //items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};
			items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
            //items[items.length] = {text: I18N.COMMON.rename, handler: renameEntity};
        }
          items[items.length] = {id:"renameGroup",text: I18N.COMMON.rename, handler: onRenameClick};
//        if (editTopoPower) {
//			items[items.length] = '-';
//			items[items.length] = {text: I18N.COMMON.edit, menu: [
//				{text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick},
//				{text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick}, '-',
//				{text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick},
//				{text: I18N.COMMON.rename, handler: renameEntity}
//			]};
//			if (!isSubnetMap) {
//				items[items.length] = {text: I18N.COMMON.select, menu: [
//					{text: I18N.COMMON.selectAll, handler: onSelectAllClick}, '-',
//					{text: I18N.GRAPH.allVertex, handler: onSelectVertexsClick},
//					{text: I18N.GRAPH.allEdge, handler: onSelectEdgesClick}, '-',
//					{text: I18N.GRAPH.selectAdjacent, handler: onSelectAdjacentClick},
//					{text: I18N.GRAPH.selectSameType, handler: onSelectSameTypeClick}
//				]};
//				items[items.length] = {text: I18N.COMMON.format, menu: [
//					{id: 'resizeItem', text: I18N.GRAPH.resize, handler: onResizeClick},
//					{text: I18N.GRAPH.fixLocation, handler: onFixLocationClick},
//					 '-',
//					{text: I18N.GRAPH.align, menu: [
//						{text: I18N.GRAPH.leftAlign, iconCls: 'bmenu_left', handler: onLeftAlignClick},
//						{text: I18N.GRAPH.centerAlign, iconCls: 'bmenu_center', handler: onCenterAlignClick},
//			        	{text: I18N.GRAPH.rightAlign, iconCls: 'bmenu_right', handler: onRightAlignClick}, '-',
//						{text: I18N.GRAPH.topAlign, iconCls: 'bmenu_top', handler: onTopAlignClick},
//						{text: I18N.GRAPH.middleAlign, iconCls: 'bmenu_middle', handler: onMiddleAlignClick},
//						{text: I18N.GRAPH.bottomAlign, iconCls: 'bmenu_bottom', handler: onBottomAlignClick}
//					]},
//					{id: 'backgroundItem', text: I18N.GRAPH.background, menu: [
//						{text: I18N.GRAPH.fillColor, handler: onFillColorClick},
//						{text: I18N.GRAPH.gradientColor, handler: onGradientColorClick}, '-',
//						{text: I18N.GRAPH.shadow, handler: onShadowClick}
//					]},
//					{id: 'sidelineItem', text: I18N.GRAPH.sideline, menu: [
//						{text: I18N.GRAPH.strokeColor, handler: onSetStrokeColorClick},
//						{text: I18N.GRAPH.strokeWeight, handler: onSetStrokeWeightClick}, '-',
//						{text: I18N.GRAPH.solid, handler: onEnableSolidClick},
//						{text: I18N.GRAPH.dashed, handler: onEnableDashedClick}
//					]}
//		        ]};
//			}
//		}
		items[items.length] = '-';
		items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};
		shapeMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
        shapeMenu.items.items[0].addClass("first_menu");
    }
    if  (vertex.isExpanded()) {
        var cmp = Ext.getCmp('expandItem');
        cmp.setIconClass("bmenu_checked");
        cmp.setText(I18N.GRAPH.collapse);
    } else {
        var cmp = Ext.getCmp('expandItem');
        cmp.setIconClass("");
        cmp.setText(I18N.GRAPH.expand);
    }
	if (vertex.isGroupable()) {
		Ext.getCmp('expandItem').enable();
        if  (vertex.isExpanded()) {
            var cmp = Ext.getCmp('expandItem');
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.collapse);
        } else {
            var cmp = Ext.getCmp('expandItem');
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.expand);
        }
    } else {
		Ext.getCmp('expandItem').disable();
	}
    var cmp = Ext.getCmp('fixshape');
    if (cmp != null) {
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);
        }
    }
	shapeMenu.showAt(position);
}

/**
 * 虚拟子网菜单
 * @param position
 * @param vertex
 */
function showVirtualNetMenu(position, vertex){
	if (virtualNetMenu == null) {
		var items = []; 
		items[items.length] = {id: 'showVNet', text: I18N.MENU.show, handler: onShowVNetClick};
		items[items.length] = '-';
		items[items.length] = {text: I18N.MENU.del, iconCls: 'bmenu_delete', handler: onVNetDelClick};
        items[items.length] = {id:"renameVNet",text: I18N.MENU.rename, handler: onRenameClick};
		//items[items.length] = '-';
		//items[items.length] = {text: I18N.MENU.property, handler: onVNetPropertyClick};
		virtualNetMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
		virtualNetMenu.items.items[0].addClass("first_menu");
    }
	virtualNetMenu.showAt(position);
}


function showShapeMenuMult(position, vs) {
	if (shapeMenuMult == null) {
        var items = [];
        if (editTopoPower) {
            items[items.length] = {id:"fixshapemult",text: I18N.GRAPH.fixLocation, handler: onFixLocationClick};
            items[items.length] = {id:"unfixshapemult",text: I18N.GRAPH.unfixLocation, handler: onUnFixLocationClick};
            items[items.length] = '-';
            //items[items.length] = {text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick};
            //items[items.length] = {text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick};
            items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
			if (!isSubnetMap) {
				items[items.length] = {text: I18N.GRAPH.align, menu: [
                    {text: I18N.GRAPH.leftAlign, iconCls: 'bmenu_left', handler: onLeftAlignClick},
                    {text: I18N.GRAPH.centerAlign, iconCls: 'bmenu_center', handler: onCenterAlignClick},
                    {text: I18N.GRAPH.rightAlign, iconCls: 'bmenu_right', handler: onRightAlignClick}, '-',
                    {text: I18N.GRAPH.topAlign, iconCls: 'bmenu_top', handler: onTopAlignClick},
                    {text: I18N.GRAPH.middleAlign, iconCls: 'bmenu_middle', handler: onMiddleAlignClick},
                    {text: I18N.GRAPH.bottomAlign, iconCls: 'bmenu_bottom', handler: onBottomAlignClick}
                ]}
            }
        }
		shapeMenuMult = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	}
    var fixentitymult = Ext.getCmp('fixshapemult');
    var unfixentitymult = Ext.getCmp('unfixshapemult');
    if (fixentitymult != null && unfixentitymult != null) {
        fixentitymult.disable();
        unfixentitymult.disable();
        for (var i = 0; i < vs.length; i++) {
            if (!vs[i].userObject.fixed) {
                fixentitymult.enable();
            } else {
                unfixentitymult.enable();
            }
        }
    }
    shapeMenuMult.showAt(position);
}

function showFolderMenu(position,vertex) {
	if (folderMenu == null) {
		var items = [];
		items[items.length] = {text: I18N.NETWORK.openChildGraph, handler: onOpenFolderClick};
		items[items.length] = {text: I18N.NETWORK.openGraphInNewWindow, handler: onOpenFolerInWindow};
		items[items.length] = '-';
		items[items.length] = {text: I18N.GRAPH.openURL, iconCls: 'bmenu_href', handler: onOpenURLClick};
        if (editTopoPower) {
            items[items.length] = {id:"fixfolder",text: I18N.GRAPH.fixLocation, handler: onFixLocationClickOne};
			items[items.length] = '-';
		    items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
            //items[items.length] = {text: I18N.COMMON.rename, handler: renameEntity};
        }
        items[items.length] = {id:"rename",text: I18N.COMMON.rename, handler: onRenameClick};
		//items[items.length] = '-';
		//items[items.length] = {text: I18N.NETWORK.networkStatInfo, handler: onOpenFolderClick};
//		if (editTopoPower) {
//			items[items.length] = '-';
//			items[items.length] = {text: I18N.COMMON.edit, menu: [
//				{text: I18N.COMMON.cut, iconCls: 'bmenu_cut', handler: onCutClick, disabled: true},
//				{text: I18N.COMMON.copy, iconCls: 'bmenu_copy', handler: onCopyClick, disabled: true}, '-',
//				{text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick},
//				{text: I18N.COMMON.rename, handler: renameEntity}
//			]};
//			items[items.length] = {text: I18N.COMMON.select, menu: [
//				{text: I18N.COMMON.selectAll, handler: onSelectAllClick}, '-',
//				{text: I18N.GRAPH.allVertex, handler: onSelectVertexsClick},
//				{text: I18N.GRAPH.allEdge, handler: onSelectEdgesClick}, '-',
//				{text: I18N.GRAPH.selectAdjacent, handler: onSelectAdjacentClick},
//				{text: I18N.GRAPH.selectSameType, handler: onSelectSameTypeClick}
//			]};
//			items[items.length] = {text: I18N.COMMON.format, menu: [
//				{text: I18N.GRAPH.fixLocation, handler: onFixLocationClick}, '-',
//				{text: I18N.GRAPH.align, menu: [
//					{text: I18N.GRAPH.leftAlign, iconCls: 'bmenu_left', handler: onLeftAlignClick},
//					{text: I18N.GRAPH.centerAlign, iconCls: 'bmenu_center', handler: onCenterAlignClick},
//		        	{text: I18N.GRAPH.rightAlign, iconCls: 'bmenu_right', handler: onRightAlignClick}, '-',
//					{text: I18N.GRAPH.topAlign, iconCls: 'bmenu_top', handler: onTopAlignClick},
//					{text: I18N.GRAPH.middleAlign, iconCls: 'bmenu_middle', handler: onMiddleAlignClick},
//					{text: I18N.GRAPH.bottomAlign, iconCls: 'bmenu_bottom', handler: onBottomAlignClick}
//				]}
//	        ]};
//		}
		items[items.length] = '-';
		items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};
		folderMenu = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
        folderMenu.items.items[0].addClass("first_menu");
	}
    var cmp = Ext.getCmp('fixfolder');
    if (cmp != null) {
        if (!vertex.userObject.fixed) {
            cmp.setIconClass("");
            cmp.setText(I18N.GRAPH.fixLocation);
        } else {
            cmp.setIconClass("bmenu_checked");
            cmp.setText(I18N.GRAPH.unfixLocation);
        }
    }
	folderMenu.showAt(position);
}


function showFolderMenuMult(position,vs) {
	if (folderMenuMult == null) {
        var items = [];
        if (editTopoPower) {
            items[items.length] = {id:"fixfoldermult",text: I18N.GRAPH.fixLocation, handler: onFixLocationClick};
            items[items.length] = {id:"unfixfoldermult",text: I18N.GRAPH.unfixLocation, handler: onUnFixLocationClick};
            items[items.length] = '-';
            items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};
			if (!isSubnetMap) {
				items[items.length] = {text: I18N.GRAPH.align, menu: [
                    {text: I18N.GRAPH.leftAlign, iconCls: 'bmenu_left', handler: onLeftAlignClick},
                    {text: I18N.GRAPH.centerAlign, iconCls: 'bmenu_center', handler: onCenterAlignClick},
                    {text: I18N.GRAPH.rightAlign, iconCls: 'bmenu_right', handler: onRightAlignClick}, '-',
                    {text: I18N.GRAPH.topAlign, iconCls: 'bmenu_top', handler: onTopAlignClick},
                    {text: I18N.GRAPH.middleAlign, iconCls: 'bmenu_middle', handler: onMiddleAlignClick},
                    {text: I18N.GRAPH.bottomAlign, iconCls: 'bmenu_bottom', handler: onBottomAlignClick}
                ]}
            }
        }
		folderMenuMult = new Ext.menu.Menu({minWidth: 180, enableScrolling: false, ignoreParentClicks: true, items: items});
	}
    var fixentitymult = Ext.getCmp('fixfoldermult');
    var unfixentitymult = Ext.getCmp('unfixfoldermult');
    if (fixentitymult != null && unfixentitymult != null) {
        fixentitymult.disable();
        unfixentitymult.disable();
        for (var i = 0; i < vs.length; i++) {
            if (!vs[i].userObject.fixed) {
                fixentitymult.enable();
            } else {
                unfixentitymult.enable();
            }
        }
    }
	folderMenuMult.showAt(position);
}

function showEdgeMenu(position) {
	if (edgeMenu == null) {
		var items = [];
		//items[items.length] = {id: 'linkRealItem', text: 'View Flow Rela', handler: onLinkPropertyClick};
        if (editTopoPower) {
        	items[items.length] = {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick};//删除
			items[items.length] = '-';
        }               
//		if (editTopoPower) {
//			//items[items.length] = '-';
//			items[items.length] = {text: I18N.COMMON.edit, menu: [
//				{text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler: onDeleteClick}
//			]};
//			items[items.length] = {text: I18N.COMMON.select, menu: [
//				{text: I18N.COMMON.selectAll, handler: onSelectAllClick}, '-',
//				{text: I18N.GRAPH.allVertex, handler: onSelectVertexsClick},
//				{text: I18N.GRAPH.allEdge, handler: onSelectEdgesClick}, '-',
//				{text: I18N.GRAPH.selectAdjacent, handler: onSelectEdgeAdjacentClick},
//				{text: I18N.GRAPH.selectSameType, handler: onSelectEdgesClick}
//			]};
//			items[items.length] = {text: I18N.COMMON.format, menu: [
//				{text: I18N.GRAPH.sideline, menu: [
//					{text: I18N.GRAPH.strokeColor, handler: onSetStrokeColorClick1},
//					{text: I18N.GRAPH.strokeWeight, handler: onSetStrokeWeightClick1}, '-',
//					{text: I18N.GRAPH.solid, handler: onEnableEdgeSolidClick},
//					{text: I18N.GRAPH.dashed, handler: onEnableEdgeDashedClick}
//				]},
//				{text: I18N.GRAPH.connector, menu: [
//					{text: I18N.GRAPH.straightConnector, iconCls: 'connector_straight', handler: onStraightConnectorClick},
//					{text: I18N.GRAPH.horizonalConnector, iconCls: 'connector_horizonal', handler: onHorizonalConnectorClick}
//				]},
//				{text: I18N.GRAPH.lineStart, menu: [
//					{text: I18N.GRAPH.classic, iconCls: 'bmenu_classic', handler: onArrowClassicClick},
//					{text: I18N.GRAPH.open, iconCls: 'bmenu_open', handler: onArrowOpenClick},
//					{text: I18N.GRAPH.block, iconCls: 'bmenu_block', handler: onArrowBlockClick},
//					{text: I18N.GRAPH.oval, iconCls: 'bmenu_oval', handler: onArrowOvalClick},
//					{text: I18N.GRAPH.none, handler: onArrowNoneClick}
//				]},
//				{text: I18N.GRAPH.lineEnd, menu: [
//					{text: I18N.GRAPH.classic, iconCls: 'bmenu_classic1', handler: onArrowClassicClick1},
//					{text: I18N.GRAPH.open, iconCls: 'bmenu_open1', handler: onArrowOpenClick1},
//					{text: I18N.GRAPH.block, iconCls: 'bmenu_block1', handler: onArrowBlockClick1},
//					{text: I18N.GRAPH.oval, iconCls: 'bmenu_oval1', handler: onArrowOvalClick1},
//					{text: I18N.GRAPH.none, handler: onArrowNoneClick1}
//				]}
//	        ]};
//			items[items.length] = '-';
//		}
		items[items.length] = {text: I18N.COMMON.property, handler: onVertexPropertyClick};//属性
		edgeMenu = new Ext.menu.Menu({minWidth: 150, enableScrolling: false, ignoreParentClicks: true, items: items});
	}
	edgeMenu.showAt(position);
}

function showStatusBarInfo() {
	if (openMode == null) {
		if (selectedCount < 1) {
			var c1 = model.getVertexCount();
			var c2 = model.getEdgeCount();
			var t = c1 + c2;
			if (totalFlag != t) {
				totalFlag = t;
				setStatusBarInfo(String.format(I18N.NETWORK.entityCountMsg, t,
						c1, c2));
			}
		} else {
			totalFlag = 0;
			setStatusBarInfo(String.format(I18N.NETWORK.selectedCountMsg,
					selectedCount));
		}
	}
}
function setStatusBarInfo(info) {
	try { window.top.setStatusBarInfo(info); } catch (err) { }
}

//*************************//
//***** 菜单处理器  *********//
//*************************//
function onShowVNetClick(){
	var userObj = selectionModel.getSelectedVertex().getUserObject();
	var virtualNetId = userObj.id;
	window.top.addView('entity-' + virtualNetId,
			I18N.COMMON.virtual + '[' + userObj.text + ']',
			'entityTabIcon', 'topology/showVitualDevice.tv?virtualNetId=' + virtualNetId+"&folderId="+topoFolderId);
	return false;
}

function onVNetDelClick(){
	var userObj = selectionModel.getSelectedVertex().getUserObject();
	var virtualNetId = userObj.id;
	window.top.showConfirmDlg(I18N.COMMON.tip, I18N.topo.grap.js.removeVirtual, function(type) {
		if (type == 'no') {return;}
//		if (size > 10) {
//			window.top.showWaitingDlg(I18N.COMMON.WAITING, I18N.NETWORK.deletingEntityAndLink);
//		}
//		var cellIds = [];
//		var edgeIds = [];
//		for (var i = 0; i < selectees.length; i++) {
//			if (selectees[i].getUserObject().objType == USEROBJ_TYPE.LINK) {
//				edgeIds[edgeIds.length] = selectees[i].getUserObject().nodeId;
//			} else if(selectees[i].getUserObject().objType == USEROBJ_TYPE.FOLDER){
//				cellIds[cellIds.length] = selectees[i].getUserObject().userObjId;
//			} else{
//				cellIds[cellIds.length] = selectees[i].getUserObject().nodeId;
//			}
//		}
		$.ajax({url: '/virtualnet/deleteVirtualNet.tv', type: 'POST',
			data: {virtualNetId: virtualNetId},
			cache: false,
			dataType : 'json',
			success: function(json) {
				if(json.success){
					if(json.hasProduct){
						window.top.showMessageDlg(I18N.MENU.tip,I18N.topo.grap.js.virtualIncludeEntity);
					}else{
						window.top.showMessageDlg(I18N.MENU.tip,I18N.topo.grap.js.virtualRemoveSuccess);
						graph.removeVertex(selectionModel.getSelectedVertex());
					}
				}else{
					window.top.showMessageDlg(I18N.MENU.tip,I18N.topo.grap.js.virtualRemoveFail);
				}
			},
			error: function() {
				window.top.showErrorDlg();
			}
		});
	});
}

function onVirtualNetRenameClick(){
	//var userObj = selectionModel.getSelection().getUserObject();
}

function onVNetPropertyClick(){
	var userObj = selectionModel.getSelection().getUserObject();
}

function onEntitySnapClick() {
    var selectee = selectionModel.getSelection();
    if (selectee != null) {
        var userObj = selectee.getUserObject();
        Ext.menu.MenuMgr.hideAll();
        if (isCmc8800CAORA(userObj.typeId) ||
            userObj.typeId == USEROBJ_TYPE.CC8800B) {
            window.top.addView('entity-' + userObj.userObjId,  userObj.name ,
                    'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + userObj.userObjId);
        }else if(userObj.type == USEROBJ_TYPE.AP||userObj.type == USEROBJ_TYPE.AL8101||userObj.type == USEROBJ_TYPE.AL8102||userObj.type == USEROBJ_TYPE.AL8103){
        	window.open("http://"+ userObj.ip); 
        }else if(userObj.type == USEROBJ_TYPE.SFU){
        	return;
        }else if(userObj.type == USEROBJ_TYPE.UNKNOWN){
        	return;
        }else {
            window.top.addView('entity-' + userObj.userObjId, userObj.name ,
                    'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + userObj.userObjId);
        }
        var state = window.top.propertyPanelState;
        if (state != null && state == 2) {
            window.top.minPropertyPanel();
        }
    }
}

function onOnuEntitySnapClick(){
	var selectee = selectionModel.getSelection();
	var entityId = selectee.getUserObject().userObjId;
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		Ext.menu.MenuMgr.hideAll();
		window.parent.addView('entity-' + entityId,  "cmcMac" , 'entityTabIcon',
				'/cmc/macdomain/showCmcMacDomainInfo.tv?entityId=' + entityId)
		var state = window.top.propertyPanelState;
		if (state != null && state == 2) {
			window.top.minPropertyPanel();
		}
	}
}

function onViewConfigClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		Ext.menu.MenuMgr.hideAll();
		window.top.addView('entity' + userObj.userObjId,  userObj.name ,
		'entityTabIcon', 'entity/showEditEntityJsp.tv?module=2&entityId=' + userObj.userObjId);
	}
}
function viewAlarm(entityId, ip) {
	window.top.addView('entity' + entityId, ip, 'entityTabIcon', 'alert/showEntityAlertJsp.tv?module=6&entityId=' + entityId);
}
function onViewAlarmClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		Ext.menu.MenuMgr.hideAll();
		window.top.addView('entity' + userObj.userObjId, I18N.COMMON.entity + '[' + userObj.name + ']',
		'entityTabIcon', 'alert/showEntityAlertJsp.tv?module=6&entityId=' + userObj.userObjId);
	}
}
function onMibBrowseClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		Ext.menu.MenuMgr.hideAll();
		window.top.addView("mibbleBrowser","Mibble Browser",null,"/mibble/showMibbleBrowser.tv?host="+userObj.ip)		
	}
}
function onCpuMemClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		window.open('../realtime/viewCpuMemInfo.tv?entityId=' + userObj.userObjId + '&ip=' + userObj.ip, 'cpumem' + userObj.userObjId);
	}
}
function onPortRealStateClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		window.open('../realtime/showPortFlowInfo.tv?entityId=' + userObj.userObjId + '&ip=' + userObj.ip, 'portreal' + userObj.userObjId);
	}
}
function onDiscoveryAgainClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		if(userObj.typeId == 30001){
			window.parent.showWaitingDlg(I18N.COMMON.waiting, String.format(I18N.NETWORK.dicoveryingEntityAgain, userObj.mac),'waitingMsg','ext-mb-waiting');
			$.ajax({
    			url:'../cmc/refreshCC.tv?cmcId='+ userObj.nodeId+'&cmcType='+userObj.typeId,
	  			type:'POST',
	  			dateType:'json',
	  			success:function(response){
        			if(response == "success"){
        				window.parent.showMessageDlg(I18N.MENU.tip, I18N.NETWORK.refreshEntitySuccess);
					}else{
						window.parent.showMessageDlg(I18N.MENU.tip, I18N.NETWORK.refreshEntityFail);
					} 
	  			},
	  			error:function(){
					 window.parent.showMessageDlg(I18N.MENU.tip, I18N.NETWORK.refreshEntityFail);
	  			},
	  			cache:false
    		});
			return;
		}
		
		window.top.discoveryEntityAgain(userObj.userObjId, userObj.ip, function() {
			onRefreshClick();
		});
	}
}
function onPingClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		window.top.createDialog("modalDlg", 'Ping ' + userObj.ip, 600, 400,
		"entity/runCmd.tv?cmd=ping&ip=" + userObj.ip, null, true, true);
	}
}
function onTracertClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		window.top.createDialog("modalDlg", 'Tracert ' + userObj.ip, 600, 400,
		"entity/runCmd.tv?cmd=tracert&ip=" + userObj.ip, null, true, true);
	}
}
function onTelnetClick() { }
function onNativeTelnetClick() {
	//检查注册表中是否对ie的telnet功能进行了设置,如果没有其键值就为其补上
	(function(flag){
		 if(!(window.attachEvent && navigator.userAgent.indexOf('Opera') === -1)){return false;}  
		 try{  
		    //新建一个WScript.Shell对象  
		    var shell = new ActiveXObject("WScript.Shell");      
		    if(flag){
			//往注册表中写入值  
				shell.RegWrite("HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Internet Explorer\\Main\\FeatureControl\\FEATURE_DISABLE_TELNET_PROTOCOL\\iexplore.exe",
				"00000000","REG_DWORD");				
		    }  
		 }catch(e){  
		 }
	})(true);
	//----CORE LOGIC----//
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		var hwin = window.open('telnet://' + userObj.ip, 'ntelnet' + userObj.userObjId);
        setTimeout(function(){hwin.close()},300);
    }
}
function onOpenURLClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var entityId = selectee.getUserObject().userObjId;
		var url = selectee.getUserObject().url;
		if (url != null && url != '') {
            Ext.menu.MenuMgr.hideAll();
            if (!url.startWith("http://") && !url.startWith("https://")) {
                url = "http://"+url;
            }
            window.top.addView('url' + entityId, 'URL',  "topoLeafIcon", url);
		} else {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.GRAPH.hrefMessage);
			onVertexPropertyClick();
		}
	}
}
function onOpenURLInWindowClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var entityId = selectee.getUserObject().userObjId;
		var url = selectee.getUserObject().url;
		if (url != null && url != '') {
			window.open(url);
		}
	}
}
function onTopAlignClick() { graph.setTopAlign(); }
function onMiddleAlignClick() { graph.setMiddleAlign(); }
function onBottomAlignClick() { graph.setBottomAlign(); }
function onLeftAlignClick() { graph.setLeftAlign(); }
function onCenterAlignClick() { graph.setCenterAlign(); }
function onRightAlignClick() { graph.setRightAlign(); }
function onVertexPropertyClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		var url = '';
		var entityId = selectee.getUserObject().nodeId;
		var type = selectee.getUserObject().objType;
//        alert(type+":"+entityId+":"+selectee.getUserObject());
        if (type == USEROBJ_TYPE.ENTITY) {
			url = 'entity/showEntityPropertyJsp.tv?entityId=' + entityId + '&folderId=' + topoFolderId;
		} else if (type == USEROBJ_TYPE.FOLDER) {
			url = 'topology/getTopoFolderProperty.tv?nodeId=' + entityId + '&isRegion=1';
		} else if (type == USEROBJ_TYPE.SHAPE) {
			url = 'topology/showVmlPropertyJsp.tv?nodeId=' + entityId +
				'&folderId=' + topoFolderId + '&id=' + entityId;
		} else if (type == USEROBJ_TYPE.LINK) {
			url = 'link/showLinkPropertyJsp.tv?linkId=' + entityId;
		}
        //alert(url)
		//window.top.showProperty(url);
        showSidePart(url);
	}
}
function onShowDrawingClick(item, pressed) {
	if (pressed) {
		if (drawingDlg == null) {
			var y = parseInt((document.body.clientHeight - 380) / 2);
			y = parseInt(($(window).height() - 380)/2);
			y = y < 30 ? 30 : y;
			drawingDlg = new Ext.Window({
				id: 'drawingsDlg', title: I18N.NETWORK.drawingTitle, contentEl: 'drawings-div',
				width: 100, minWidth: 35, height: 200, minHeight: 100,
				layout: 'fit', plain: true, resizable: true, stateful: false, shadow: false, closable: true
			});
			drawingDlg.on('hide', function() {
				setDrawingType(SHAPE_TYPE.POINTER);
				return false;
			});
			drawingDlg.setPosition(10, y);
            var drawings = $('#drawings-div');
			$('img[drawingType]', drawings).draggable({
				scope: 'drawing',
				appendTo: 'body',
				containment: 'document',
				revert: 'invalid',
				helper: function() {
					var div = document.createElement('div');
					div.style.position = 'absolute';
					div.style.width = 50;
					div.style.height = 50;
					div.style.border = '1px dashed black';
					return div;
				}
			});
		}
		drawingDlg.show();
	} else {
		drawingDlg.hide();
	}
}

/*
 * 设置绘图工具箱中的选中工具，以便进行绘制
 */
function setDrawingType(type) {
	if (curDrawingType == type) {
		return;
	}
	graph.stopDrawConnector();
	Zeta$('drawing' + curDrawingType).className = "DRAWING-TOOL";
	Zeta$('drawing' + type).className = "DRAWING-TOOL-SELECTED";

	if (type >= SHAPE_TYPE.SELECTALL) {//3
		graph.setCursor("crosshair");
	} else {
		if (type == SHAPE_TYPE.MOVE) {
			graph.setCursor("url(../css/images/openhand.cur)");
		} else {
			graph.setCursor("default");
		}
		try {
			window.top.setCoordInfo('');
		} catch (err) {
		}
	}
	if (type == SHAPE_TYPE.POINTER) {
		graph.setEditable(true);
	} else {
		graph.setEditable(false);
	}
	//设置当前的绘图工具为 type
	curDrawingType = type;
}
function doDrawingToolOver(obj, type) { if (curDrawingType == type) { obj.className = "DRAWING-TOOL-SELECTED-OVER"; } else { obj.className = "DRAWING-TOOL-OVER"; } }
function doDrawingToolPressed(obj, type) { obj.className = "DRAWING-TOOL-PRESSED"; }
function doDrawingToolSelected(obj) { obj.className = 'DRAWING-TOOL-SELECTED'; }
function doDrawingToolOut(obj, type) { if (curDrawingType == type) { obj.className = "DRAWING-TOOL-SELECTED"; } else { obj.className = "DRAWING-TOOL"; } }
//function onSynMicroGraphClick() { microGraph.update(); }
//function onShowMicroGraphClick() { microGraph.setVisible(true); }
function tickSelect(size) { 
	if( size < 1 ){
		graph.view.setWidth(folderWidth/size)
		graph.view.setHeight(folderHeight/size)
		graph.setWidth(folderWidth/size)
		graph.setHeight(folderHeight/size)
	}else{
		graph.view.setWidth(folderWidth * size)
		graph.view.setHeight(folderHeight * size)
		graph.setWidth(folderWidth * size)
		graph.setHeight(folderHeight * size)
	}
	graph.zoom(size)
}
function onZoomOutClick() { switch (zoomSize) { case 0.1: break; case 0.25: tickSelect(0.1); break; case 0.5: tickSelect(0.25); break; case 0.75: tickSelect(0.5); break; case 1: tickSelect(0.75); break; case 1.25: tickSelect(1); break; case 1.5: tickSelect(1.25); break; case 2: tickSelect(1.5); break; case 3: tickSelect(2); break; case 5: tickSelect(3); break; } }
function onZoomInClick() { switch (zoomSize) { case 0.1: tickSelect(0.25); break; case 0.25: tickSelect(0.5); break; case 0.5: tickSelect(0.75); break; case 0.75: tickSelect(1); break; case 1: tickSelect(1.25); break; case 1.25: tickSelect(1.5); break; case 1.5: tickSelect(2); break; case 2: tickSelect(3); break; case 3: tickSelect(5); break; case 5: break; } }
function onZoomFitClick10() {tickSelect(0.1);}
function onZoomFitClick25() {tickSelect(0.25);}
function onZoomFitClick50() {tickSelect(0.5);}
function onZoomFitClick75() {tickSelect(0.75);}
function onZoomFitClick100() {tickSelect(1);}
function onZoomFitClick125() {tickSelect(1.25);}
function onZoomFitClick150() {tickSelect(1.5);}
function onZoomFitClick200() {tickSelect(2);}
function onZoomFitClick300() {tickSelect(3);}
function onZoomFitClick500() {tickSelect(5);}
/**
 * 自定义缩放
 */
function onZoomCustomClick() {
	window.top.showInputDlg(I18N.NETWORK.input, I18N.GRAPH.customLevelMsg, function(type, text) {
		try {
			if('cancel' == type)return false;
			var size = parseInt(text.trim());
			if (size >= 10 && size <= 500) {
				tickSelect(size / 100);
			} else{
			    throw new Error();
			}
		} catch (err) {
			window.top.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.inputError, 'error');
		}
    });
}
/**
 * 激活鼠标滚动缩放对话框
 */
function onAutoZoomClick() {
    automousezoon = !automousezoon;
    var cmp = Ext.getCmp('automousezoon');
    //cmp.setChecked(automousezoon);
    if (automousezoon) {
        cmp.setIconClass("bmenu_checked");
    } else {
        cmp.setIconClass("");
    }
    graph.setAutoZoom(automousezoon);
}
function onNewEntityClick() {
	window.top.createDialog('modalDlg', I18N.COMMON.newEntity, 360, 230, 'entity/showNewEntity.tv?folderId=' + topoFolderId, null, true, true); 
}
function createEntity(json) {
	var vertex = new ZetaVertex(json);
	vertex.setIconMarkerVisible(displayAlertIcon);
	graph.insertVertex(vertex);
	vertex.setIcon(json.icon);
	vertex.setDynamicTooltip(json.text, '../entity/loadEntityTip.tv?entityId=' + json.nodeId
		 + '&folderId=' + topoFolderId);
}
function onPrintClick(){
	var h = $("#zetaGraph").outerHeight();
	if(h > 10){	graph.tempHeight = h + "px";};
	graph.print(topoFolderName, ['../css/zeta-graph.css']); 
}
function onPrintPreviewClick(){
	var h = $("#zetaGraph").outerHeight();
	if(h > 10){	graph.tempHeight = h + "px";};
	graph.printview(topoFolderName, ['../css/zeta-graph.css']); 
}
function onReturnSuperiorClick() { Ext.menu.MenuMgr.hideAll(); window.top.addView('topo' + superiorFolderId, superiorFolderName, 'topoLeafIcon', 'topology/getTopoMapByFolderId.tv?folderId=' + superiorFolderId); }

/*
 *删除事件处理器
 */
function onDeleteClick() {
	var selectees = selectionModel.getSelections();
	var size = selectees.length;
	if (size==0) {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSelected);
		return;
	}
	window.top.showConfirmDlg(I18N.COMMON.tip, I18N.NETWORK.deleteEntity, function(type) {
		if (type == 'no') {return;}
		if (size > 10) {
			window.top.showWaitingDlg(I18N.COMMON.WAITING, I18N.NETWORK.deletingEntityAndLink);
		}
		var cellIds = [];
		var edgeIds = [];
        //本身不存在Entity的设备类型  例如 8800A  MDU等
        var noEntityIds = [];
		for (var i = 0; i < selectees.length; i++) {
			if (selectees[i].getUserObject().objType == USEROBJ_TYPE.LINK) {
				edgeIds[edgeIds.length] = selectees[i].getUserObject().nodeId;
			} else if(selectees[i].getUserObject().objType == USEROBJ_TYPE.FOLDER){
				cellIds[cellIds.length] = selectees[i].getUserObject().userObjId;
			}  else if(isCmc8800CAORA(selectees[i].getUserObject().typeId) ||
                    selectees[i].getUserObject().type == USEROBJ_TYPE.SFU ){
					noEntityIds[noEntityIds.length] = selectees[i].getUserObject().userObjId;
			} else{
				cellIds[cellIds.length] = selectees[i].getUserObject().nodeId;
			}
		}
		$.ajax({url: 'topology/deleteCellByIds.tv', type: 'POST',
			data: {folderId: topoFolderId, nodeIds: cellIds, edgeIds: edgeIds,noEntityIds:noEntityIds},
			dataType: 'json', cache: false,
			success: function(json) {
				if (json.ExistEntity) {
					window.top.showMessageDlg(I18N.COMMON.tip, json.msg);
					if(json.hasDeleteFolder){		
						onRefreshClick();
						window.top.getMenuFrame().doRefresh();
					}					
				} else {
					var flag = false;
					var objType;
					for (var i = 0; i < selectees.length; i++) {
					    if(selectees[i].getUserObject().type == USEROBJ_TYPE.OLT) {
					        flag = true;
					    }
						objType = selectees[i].getUserObject().objType;
						if (objType == USEROBJ_TYPE.LINK) {
							graph.removeEdge(selectees[i]);
						} else {
							if (objType == USEROBJ_TYPE.FOLDER) {
								flag = true;
							}
							graph.removeVertex(selectees[i]);
						}
					}
					if (flag) {
						try {
							window.top.getMenuFrame().doRefresh();
						} catch (err) {
						}
					}
				}
			},
			error: function() {
				window.top.showErrorDlg();
			},
			complete: function() {
				if (size > 10) {
					window.top.closeWaitingDlg();
				}
			}
		});
	});
}
function onRenameClick() {
	var selectee = selectionModel.getSelection();
	if (selectee !=  null) {
		graph.startRenaming(selectee);
	}
}

function renameEntity() {
	var selectee = selectionModel.getSelection();
	if (selectee !=  null) {
        var entityId = selectee.userObject.userObjId;
        window.top.createDialog('renameEntity', I18N.COMMON.realias, 600, 250,
                '/entity/showRenameEntity.tv?entityId=' + entityId + "&pageId=" + window.parent.getActiveFrameId(), null, true, true);
	}
}

function changeEntityName(entityId, name) {
    onRefreshClick();
}

function onFixLocationClickOne() {
    var vertex = selectionModel.getSelectedVertex();
	if (vertex !=  null) {
		if (!vertex.userObject.fixed) {
            onFixLocationClick();
        } else {
            onUnFixLocationClick();
        }
	}
}

function onFixLocationClick() {
	var selectees = selectionModel.getSelectedVertexs();
	if (selectees.length > 0) {
		var nodeIds = [];
		for (var i = 0; i < selectees.length; i++) {
			nodeIds[i] = selectees[i].getUserObject().nodeId;
		}
		$.ajax({url: '../topology/updateVertexFixed.tv', type: 'POST',
		data: {folderId: topoFolderId, nodeIds: nodeIds,fixed:true},
		success: function() {
			for (var i = 0; i < selectees.length; i++) {
//				$(selectees[i].jid).draggable('disable');
                selectees[i].setFixed(true)
            }
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
	}
}
function onUnFixLocationClick() {
	var selectees = selectionModel.getSelectedVertexs();
	if (selectees.length > 0) {
		var nodeIds = [];
		for (var i = 0; i < selectees.length; i++) {
			nodeIds[i] = selectees[i].getUserObject().nodeId;
		}
		$.ajax({url: '../topology/updateVertexFixed.tv', type: 'POST',
		data: {folderId: topoFolderId, nodeIds: nodeIds,fixed:false},
		success: function() {
			for (var i = 0; i < selectees.length; i++) {
//				$(selectees[i].jid).draggable('disable');
                selectees[i].setFixed(false)
            }
		}, error: function() {
			window.parent.showErrorDlg();
		}, dataType: 'plain', cache: false});
	}
}

/**
 * 当页面被关闭的时候进行保存，不明白作者的意图 ：＠bravin
 */
function onAddToGoogleClick() {
	window.parent.addView("ngm", I18N.NETWORK.googleMapNet, "googleIcon", "google/showEntityGoogleMap.tv");
}
/**
 * 组调整大小
 */
function onResizeClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		if (selectee.getUserObject().objType == USEROBJ_TYPE.SHAPE) {
			graph.startResizing(selectee);
		}
	}
}
function expandGroup(group, flag) {
	group.setExpanded(flag);
	$.ajax({
		url: '../topology/setMapNodeExpanded.tv', cache: false,  type: 'get',
		data: {'mapNode.nodeId': group.getUserObject().nodeId, 'mapNode.expanded': flag}
	});
}
function onGroupClick() {
	var selectee = selectionModel.getSelectedVertex();
    if (selectee.isExpanded()) {
        onCollapseClick();
    } else {
        onExpandClick();
    }
}
function onExpandClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		expandGroup(selectee, true);
	}
}
function onCollapseClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		expandGroup(selectee, false);
	}
}
function pasteEntity(json) {
	if (showType == SHOW_TYPE.NAME) {
		for (var i = 0; i < json.entity.length; i++) {
			var vertex = new ZetaVertex(json.entity[i]);
			vertex.setTextWidth(100);
			vertex.setLabel(json.entity[i].name);
			vertex.setIconMarkerVisible(displayAlertIcon);
			graph.insertVertex(vertex);
			vertex.setIcon(json.entity[i].icon);
			/*
			vertex.setCluetip(json.entity[i].text, '../entity/loadEntityTip.tv?entityId=' + json.entity[i].nodeId
				 + '&folderId=' + topoFolderId);
		    */
			vertex.setCluetip(json.entity[i].text, '/topology/loadNodeTip.tv?userObjType='+json.entity[i].objType+'&userObjId='+json.entity[i].userObjId+'&folderId='+topoFolderId);
		}
	} else if (showType == SHOW_TYPE.SYSNAME) {
		for (var i = 0; i < json.entity.length; i++) {
			var vertex = new ZetaVertex(json.entity[i]);
			vertex.setTextWidth(100);
			vertex.setLabel(json.entity[i].sysName);
			vertex.setIconMarkerVisible(displayAlertIcon);
			graph.insertVertex(vertex);
			vertex.setIcon(json.entity[i].icon);
			/*
			vertex.setCluetip(json.entity[i].text, '../entity/loadEntityTip.tv?entityId=' + json.entity[i].nodeId
				 + '&folderId=' + topoFolderId);
		    */
			vertex.setCluetip(json.entity[i].text, '/topology/loadNodeTip.tv?userObjType='+json.entity[i].objType+'&userObjId='+json.entity[i].userObjId+'&folderId='+topoFolderId);
		}
	} else  {
		for (var i = 0; i < json.entity.length; i++) {
			var vertex = new ZetaVertex(json.entity[i]);
			vertex.setTextWidth(100);
			vertex.setLabel(json.entity[i].ip);
			vertex.setIconMarkerVisible(displayAlertIcon);
			graph.insertVertex(vertex);
			vertex.setIcon(json.entity[i].icon);
			/*
			vertex.setCluetip(json.entity[i].text, '../entity/loadEntityTip.tv?entityId=' + json.entity[i].nodeId
				 + '&folderId=' + topoFolderId);
		    */
			vertex.setCluetip(json.entity[i].text, '/topology/loadNodeTip.tv?userObjType='+json.entity[i].objType+'&userObjId='+json.entity[i].userObjId+'&folderId='+topoFolderId);
		}
	}
}
function onPasteClick() {
    var clipboard = window.top.getZetaClipboard();
    if (clipboard != null) {
		if (clipboard.src == topoFolderId) {
            window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notEntityIdPaste);
            return;
        }

        var b = false;
        for (var i = 0;i<clipboard.entityIds.length ; i++) {
            if (!graph.model.vertexs.obj['cell' + clipboard.entityIds[i]]) {
                b = true;
            }
        }
        if (!b) {
            window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notEntityIdPaste);
            return;
        }
        var folderId = clipboard.src;
		if (clipboard.type == 'copy') {
			$.ajax({
				url: '../topology/copyEntityById.tv', type: 'POST', dataType: 'json', cache: false,
				data: {folderId: folderId, destFolderId: topoFolderId, entityIds: clipboard.entityIds},
				success: function(json) {
					pasteEntity(json);
					if (isSubnetMap) {
						relayoutSubnet();
					}
				},
				error: function() {
					window.top.showErrorDlg();
				}
			});
		} else if (clipboard.type == 'cut') {
			$.ajax({
				url: '../topology/cutEntityById.tv', type: 'POST', dataType: 'json', cache: false,
				data: {folderId: folderId, destFolderId: topoFolderId, entityIds: clipboard.entityIds},
				success: function(json) {
                    try {
                        pasteEntity(json);
                    } catch(ex) {
                    }
                    var frame = window.top.getFrame('topo' + folderId);
					try {
						for (var i = 0; i < json.entity.length; i++) {
							frame.synRemoveVertex(json.entity[i].nodeId);
						}
					} catch (err) {
					}
				},
				error: function() {
					window.top.showErrorDlg();
				}
			});
		}
		window.top.setZetaClipboard(null);
	} else {
        window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notSrcPaste);
    }
}
function onCutClick() {
	var selectees = selectionModel.getSelectedVertexs();
	if (selectees.length == 0) return;
	var entityIds = [];
	for (var i = 0; i < selectees.length; i++) {
		entityIds[entityIds.length] = selectees[i].getUserObject().nodeId;
	}
	if (entityIds.length == 0) return;
	window.top.setZetaClipboard({type: 'cut', target: 'topoFolder', src: topoFolderId, entityIds: entityIds});
}
function onCopyClick() {
	var selectees = selectionModel.getSelectedVertexs();
	if (selectees.length == 0) return;
	var entityIds = [];
	for (var i = 0; i < selectees.length; i++) {
		entityIds[entityIds.length] = selectees[i].getUserObject().nodeId;
	}
	if (entityIds.length == 0) return;
	window.top.setZetaClipboard({type: 'copy', target: 'topoFolder', src: topoFolderId, entityIds: entityIds});
}
function onSaveAsPictureClick() { onPrintPreviewClick(); }

/*
 * 保存拓扑图结构
 */
function onSaveClick() {
	var length = model.size();
	if (length == 0) return;
	if (length > 100)
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.NETWORK.saveEntityCoord);
	var x = [];
	var y = [];
	var nodeIds = [];
	var count = 0;
	var vx = [];
	var vy = [];
	var vnodeIds = [];
	var vcount = 0;
	model.eachVertex(function(v) {
		switch(v.getUserObject().type){
			case SHAPE_TYPE.VIRTUALNET:
				vx[vcount] = Math.floor(v.getX());
				vy[vcount] = Math.floor(v.getY());
				vnodeIds[vcount] = v.getUserObject().nodeId;
				vcount++;
				break;
			default:
				x[count] = Math.floor(v.getX());
				y[count] = Math.floor(v.getY());
				nodeIds[count] = v.getUserObject().nodeId;
				count++;
				break;
		}
	});
	var failureCallback = function() {
		if (length > 100)
			window.top.closeWaitingDlg();
		window.top.showErrorDlg();
	};
	var successCallback = function() {
		setTimeout("document.getElementById('view').style.visibility = 'visible'","500");//保存成功
		setTimeout("document.getElementById('view').style.visibility = 'hidden'","2000");//保存失败
		if (length > 100)
			window.top.closeWaitingDlg();
		};
	var successVCallback = function(){
		//vertex
		if(nodeIds.length == 0 ){
			successCallback();
		}else{
			$.ajax({url: '../topology/saveCoordinateByIds.tv', type: 'POST', dataType: 'plain', cache: false,
				data: {folderId: topoFolderId, nodeIds: nodeIds, x: x, y: y},
				success: successCallback,
				error: failureCallback
			});
		}
	};
	var failureVCallback = function(){
		failureCallback();
	}
	//virtualnet
	if(vnodeIds.length == 0 ){
		successVCallback();
	}else{
		$.ajax({url: '/virtualnet/saveVirtualNetCoordinate.tv', type: 'POST', dataType: 'json', cache: false,
			data: {folderId: topoFolderId, virtualNetIds: vnodeIds, x: vx, y: vy},
			success: successVCallback,
			error: failureVCallback
		});
	}
}
function onDefaultArrangeClick() {arrangeTopoMap(0);}
function onCircularArrangeClick() {arrangeTopoMap(1);}
function onTreeArrangeClick() {arrangeTopoMap(2);}
function onReverseTreeArrangeClick() {arrangeTopoMap(3);}
function onEastTreeArrangeClick() {arrangeTopoMap(5);}
function onWestTreeArrangeClick() {arrangeTopoMap(6);}
function onSymmetricArrangeClick() {arrangeTopoMap(4);}
function onHierarchicArrangeClick() {arrangeTopoMap(7);}
function arrangeTopoMap(type) {
	var size = model.size();
	if (size < 2) return;
	window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.NETWORK.arranging);

	$.ajax({url: 'arrangeEntityByFolderId.tv', dataType: 'json', cache: 'false',
		data: {folderId: topoFolderId, arrangeType: type,
			mapWidth: (graph.getWidth()-100), mapHeight: (graph.getHeight()-100)
		},
		success: function(json){
	   	   var vertex = null;
	   	   var len = json.length;
	       for (var i = 0; i < len; i++) {
	       	  if (!json[i].fixed) {
	       	  	  vertex = model.getVertex('cell' + json[i].nodeId);
		       	  if (vertex != null) {
		       	  	  vertex.setXY(json[i].x,json[i].y);
		       	  }
	       	  }
	       }
		},
		complete: function() {
			window.top.closeWaitingDlg();
		}
	});
}
function onViewByTopoMapClick() {}
function onViewByIconClick() { location.href = 'getTopoMapByFolderId.tv?viewType=icon&folderId=' + topoFolderId; }
function onViewByDetailClick() { location.href = 'getTopoMapByFolderId.tv?viewType=detail&folderId=' + topoFolderId; }
//function onViewByDetailClick() { location.href = '../ap/showApList.tv'; }
function openFolderInNewWindow() { window.open('getTopoMapByFolderId.tv?open=true&remoteMode=true&folderId=' + topoFolderId); }
function onPropertyClick() { 
	showSidePart('topology/getTopoFolderProperty.tv?nodeId=' + topoFolderId + '&isRegion=1'); 
}
function onLabelAndLegendClick() { 
	//window.top.showProperty("topology/showTopoLabelJsp.tv?folderId=" + topoFolderId, I18N.NETWORK.labelAndLegend);
	showSidePart("topology/showTopoLabelJsp.tv?folderId=" + topoFolderId);
}
function insertPicture() { window.top.createWindow('imageChooser', I18N.COMMON.selectPicture, 600, 400, 'include/showImageChooser.tv', null, true, true, insertPictureCallback); }
function insertPictureCallback() {
	var callback = window.top.getZetaCallback();
	if (callback != null && callback.type == 'image') {
		$.ajax({url: 'insertMapNode.tv', type: 'POST',
			data: {'mapNode.folderId': topoFolderId,
				   'mapNode.type': SHAPE_TYPE.IMAGE, 'mapNode.x': 50, 'mapNode.y': 50,
				   'mapNode.text': 'picture', 'mapNode.icon': callback.path},
			success: function(json) {
				var vertex = new ZetaVertex(json);
				graph.insertVertex(vertex);
				vertex.setIcon(json.icon);
			}, error: function() {
				window.top.showErrorDlg();
			}, dataType: 'json', cache: false});
	}
}
function insertSubnet(x, y) {
	var param = 'topoFolder.superiorId=' + topoFolderId + '&topoFolder.x=' +
		x + '&topoFolder.y=' + y;
	$.ajax({url: '../topology/addSubnet.tv', type: 'POST',
		data: param,
		success: function(json) {
			var vertex = new ZetaVertex(json);
			graph.insertVertex(vertex);
			vertex.setIcon(json.icon);
			try {
				window.top.getMenuFrame().doRefresh();
			} catch (err) {
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'json', cache: false});
}
function insertCloudy(x, y) {
	var param = 'topoFolder.superiorId=' + topoFolderId + '&topoFolder.x=' +
		x + '&topoFolder.y=' + y;
	$.ajax({url: '../topology/addCloudy.tv', type: 'POST',
		data: param,
		success: function(json) {
			var vertex = new ZetaVertex(json);
			graph.insertVertex(vertex);
			vertex.setIcon(json.icon);
			try {
				window.top.getMenuFrame().doRefresh();
			} catch (err) {
			}
		}, error: function() {
			window.top.showErrorDlg();
		}, dataType: 'json', cache: false});
}
function onSelectAllClick() { selectionModel.selectAll(); }
function onSelectVertexsClick() { selectionModel.selectAllVertex(); }
function onSelectEdgesClick() { selectionModel.selectAllEdge(); }
function onSelectAdjacentClick() {
	var selection = selectionModel.getSelection();
	if (selection != null) {
		selectionModel.selectAdjacent(selection);
	}
}
function onSelectSameTypeClick() {
	var selection = selectionModel.getSelection();
	if (selection != null) {
		var userObj = null;
		model.eachVertex(function(v) {
			userObj = v.getUserObject();
			if (selection.getUserObject().objType == userObj.objType) {
				if (selection.getUserObject().objType == USEROBJ_TYPE.SHAPE) {
					if (selection.getUserObject().type == userObj.type) {
						selectionModel.selectCell(v);
					}
				} else {
					selectionModel.selectCell(v);
				}
			}
		});
	}
}
function onSelectEdgeAdjacentClick() {
	var selection = selectionModel.getSelectedEdge();
	if (selection != null) {
		selectionModel.selectCell(selection.getSource());
		selectionModel.selectCell(selection.getTarget());
	}
}
function setVisibleByType(visible, type) {
	model.eachVertex(function(v) {
		if (v.getUserObject().objType == USEROBJ_TYPE.ENTITY && v.getUserObject().type == type) {
			v.setVisible(visible);
		}
	});
	model.eachEdge(function(e) {
		e.setVisible(e.getSource().isVisible() && e.getTarget().isVisible());
	});
}
function onDisplayEntityClick() { }
function onSelectAllLinkClick() { }
function onSelectAllEntityClick() { model.eachVertex(function(v) { if (v.getUserObject().objType == USEROBJ_TYPE.ENTITY) { selectionModel.selectCell(v); } }); }
function onSelectSnmpEntityClick() { model.eachVertex(function(v) { if (v.getUserObject().objType == USEROBJ_TYPE.ENTITY && v.getUserObject().snmpSupport) { selectionModel.selectCell(v); } }); }
function onSelectNotSnmpEntityClick() { }
function onSelectHasLinkClick() { }
function onSelectNoLinkClick() { }
function onSelectAgentEntityClick() { model.eachVertex(function(v) { if (v.getUserObject().objType == USEROBJ_TYPE.ENTITY && v.getUserObject().agentSupport) { selectionModel.selectCell(v); } }); }
function onDisplayNoSnmpEntityClick() { }
function onOpenFolderClick() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		Ext.menu.MenuMgr.hideAll();
		var userObj = selectee.getUserObject();
		window.top.addView("topo" + userObj.userObjId, userObj.name,
			"topoLeafIcon", 'topology/getTopoMapByFolderId.tv?folderId=' + userObj.userObjId);
	}
}
function onOpenFolerInWindow() {
	var selectee = selectionModel.getSelection();
	if (selectee != null) {
		window.open('getTopoMapByFolderId.tv?open=true&remoteMode=true&folderId=' +
			selectee.getUserObject().userObjId);
	}
}
/*
 * 刷新拓扑图结构，相当于重置拓扑结构，但是并没有刷新本地缓存数据（BUG），最小刷新间隔为500毫秒
 */
function onRefreshClick(gotoId) {
	if(window.refreshTick != null){
		return;
	}
	window.refreshTick = setTimeout(function(){
		refreshTick = null;
		gotoId = gotoId ? gotoId:0;
		graph.clear();
	    if (!editTopoPower) {
	        graph.setEditable(false);
	    } else {
	        graph.setResizable(true);
	    }
		loadData(gotoId, true);
	},500);
	 
}

function relayoutSubnet() { onRefreshClick(); }
/*
 * 
 */
function onFindClick() {
	window.top.showInputDlg(I18N.NETWORK.findEntity, I18N.NETWORK.findMsg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim();
		if(match=='') {window.top.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		var flag = true;
		model.eachVertex(function(vertex) {
			var userObj = vertex.getUserObject();
			if (userObj.objType == USEROBJ_TYPE.ENTITY || userObj.objType == USEROBJ_TYPE.FOLDER) {
				if ((userObj.name == match) || (userObj.ip == match)) {
					goToEntity(userObj.nodeId);
					if (flag) {
						flag = false;
					}
				}
			}
		});
		if (flag) {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);
		}
	});
}
function onFind1Click() {
	window.top.showInputDlg(I18N.COMMON.find, I18N.NETWORK.find1Msg, function(type, text) {
		if (type == 'cancel') {return;}
		var match = text.trim();
		if(match=='') {window.top.showMessageDlg(I18N.NETWORK.error, I18N.NETWORK.findMsgError, 'error'); return;}
		match = match.toLowerCase();
		goToEntity(0);
		var flag = true;
		model.eachVertex(function(vertex) {
			var userObj = vertex.getUserObject();
			if (userObj.objType == USEROBJ_TYPE.ENTITY || userObj.objType == USEROBJ_TYPE.FOLDER) {
				if ((userObj.name && userObj.name.toLowerCase().indexOf(match) != -1) ||
					(userObj.ip && userObj.ip.indexOf(match) != -1)||
					(userObj.sysName && userObj.sysName.toLowerCase().indexOf(match) != -1)) {
					vertex.ui.className = 'ui-gotoed';
					if (flag) {
						flag = false;
					}
				} else {
					vertex.ui.className = 'ui-unselecting';
				}
			}
		});
		if (flag) {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.NETWORK.notFindEntity);
		}
	});
}
function onFillColorClick() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340, 'include/colorDlg.jsp', null, true, true,
		function() {
			var callback = window.top.getZetaCallback();
			if (callback != null && callback.type == 'colorpicker') {
				var selectee = selectionModel.getSelectedVertex();
				if (selectee != null) {
					selectee.setFillColor(callback.color);
					$.ajax({
						url: '../topology/setMapNodeFillColor.tv', cache: false,
						data: {'mapNode.nodeId': selectee.getUserObject().nodeId,
							   'mapNode.fillColor': callback.color}
					});
				}
			}
		}
	);
}
function onGradientColorClick() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340, 'include/colorDlg.jsp', null, true, true,
		function() {
			var callback = window.top.getZetaCallback();
			if (callback != null && callback.type == 'colorpicker') {
				var selectee = selectionModel.getSelectedVertex();
				if (selectee != null) {
					selectee.setGradientColor(callback.color);
					$.ajax({
						url: '../topology/setMapNodeGradientColor.tv', cache: false,
						data: {'mapNode.nodeId': selectee.getUserObject().nodeId,
							   'mapNode.gradientColor': callback.color}
					});
				}
			}
		}
	);
}
function onGradientClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		selectee.setGradient(true);
		$.ajax({
			url: '../topology/setMapNodeShadow.tv', cache: false,
			data: {'mapNode.nodeId': selectee.getUserObject().nodeId,
				   'mapNode.gradient': true}
		});
	}
}
function onShadowClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		selectee.setShadowVisible(true);
		$.ajax({
			url: '../topology/setMapNodeShadow.tv', cache: false,
			data: {'mapNode.nodeId': selectee.getUserObject().nodeId,
				   'mapNode.shadow': true}
		});
	}
}
function onSetStrokeWeightClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		window.top.showInputDlg(I18N.COMMON.inputTitle, I18N.NETWORK.inputStrokeWeightMsg, function(type, text) {
			if (type == 'cancel') {return;}
			var match = text.trim();
			if (isNaN(match)) {
				window.top.showMessageDlg(I18N.COMMON.error, I18N.COMMON.parameterIllegal, 'error');
			} else {
				selectee.setStrokeWeight(match);
				var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.strokeWeight=' + match;
				$.ajax({
					url: '../topology/setMapNodeStrokeWeight.tv', cache: false,
					data: param
				});
			}
		});
	}
}
function onSetStrokeColorClick() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		var callback = window.top.getZetaCallback();
		if (callback != null && callback.type == 'colorpicker') {
			var selectee = selectionModel.getSelectedVertex();
			if (selectee != null) {
				selectee.setStrokeColor(callback.color);
				var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.strokeColor=' + callback.color;
				$.ajax({
					url: '../topology/setMapNodeStrokeColor.tv', cache: false,
					data: param
				});
			}
		}
	});
}
function onEnableDashedClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		selectee.setDashed(true);
		var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.dashed=true';
		$.ajax({
			url: '../topology/setMapNodeDashedBorder.tv', cache: false,
			data: param
		});
	}
}
function onEnableSolidClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		selectee.setDashed(false);
		var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.dashed=false';
		$.ajax({
			url: '../topology/setMapNodeDashedBorder.tv', cache: false,
			data: param
		});
	}
}
function onEnableShadowClick() {
	var selectee = selectionModel.getSelectedVertex();
	if (selectee != null) {
		selectee.setShadowVisible(true);
		var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.shadow=true';
		$.ajax({
			url: '../topology/setMapNodeDashedBorder.tv', cache: false,
			data: param
		});
	}
}
function onSetStrokeWeightClick1() {
	var selectee = selectionModel.getSelectedEdge();
	if (selectee != null) {
		window.top.showInputDlg(I18N.COMMON.inputTitle, I18N.NETWORK.inputStrokeWeightMsg, function(type, text) {
			if (type == 'cancel') {return;}
			var match = text.trim();
			if (isNaN(match)) {
				window.top.showMessageDlg(I18N.COMMON.error, I18N.COMMON.parameterIllegal, 'error');
			} else {
				selectee.setStrokeWeight(match);
				var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.strokeWeight=' + match;
				$.ajax({
					url: '../topology/setMapNodeStrokeWeight.tv', cache: false,
					data: param
				});
			}
		});
	}
}
function onSetStrokeColorClick1() {
	window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340,
		'include/colorDlg.jsp', null, true, true, function() {
		var callback = window.top.getZetaCallback();
		if (callback != null && callback.type == 'colorpicker') {
			var selectee = selectionModel.getSelectedEdge();
			if (selectee != null) {
				selectee.setStrokeColor(callback.color);
				var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.strokeColor=' + callback.color;
				$.ajax({
					url: '../topology/setMapNodeStrokeColor.tv', cache: false,
					data: param
				});
			}
		}
	});
}
function onEnableEdgeDashedClick() {
	var selectee = selectionModel.getSelectedEdge();
	if (selectee != null) {
		selectee.setDashed(true);
		return;
		var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.dashed=true';
		$.ajax({
			url: '../topology/setMapNodeDashedBorder.tv', cache: false,
			data: param
		});
	}
}
function onEnableEdgeSolidClick() {
	var selectee = selectionModel.getSelectedEdge();
	if (selectee != null) {
		selectee.setDashed(false);
		return;
		var param = 'mapNode.nodeId=' + selectee.getUserObject().nodeId + '&mapNode.dashed=true';
		$.ajax({
			url: '../topology/setMapNodeDashedBorder.tv', cache: false,
			data: param
		});
	}
}
/**
 * 解析拓扑数据
 * @param json
 */
function parseTopoNode(json) {
	var size = 0;
	var groups = [];
	var vertex;
	var userObj;
	//创建文档碎片，存储在this.fragment中
	graph.beginUpdate();
	try {
		//组，vNet...    
		if (json.node != null) {
			var id, x, y, type, renderer;
			size = json.node.length;
			for (var i = 0; i < size; i++) {
				userObj = json.node[i];
				id = userObj.id;
				x = userObj.x;
				y = userObj.y;
				type = userObj.type;
				if (type == SHAPE_TYPE.IMAGE) {
					vertex = new ZetaVertex(userObj);
					vertex.setZIndex(SHAPE_STYLE.imageZIndex);
					model.insertVertex(vertex);
					vertex.setIcon(userObj.icon);
				} else {
					var group = false;
					switch (type) {
						case SHAPE_TYPE.CONTAINER:
							group = true;
							renderer = new DefaultGroupZetaCellRenderer(id, userObj.text, x, y, userObj.width, userObj.height);
							break;
						case SHAPE_TYPE.CONTAINER_ELLIPSE:
							group = true;
							renderer = new EllipseGroupZetaCellRenderer(id, userObj.text, x, y, userObj.width, userObj.height);
							break;
						case SHAPE_TYPE.CONTAINER_PARALL:
							group = true;
							renderer = new ParallGroupZetaCellRenderer(id, userObj.text, x, y, userObj.width, userObj.height);
							break;
						case SHAPE_TYPE.ACTOR:
							renderer = new ActorZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.CLOUD:
							renderer = new CloudZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.PENTACLE:
							renderer = new PentacleZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.RHOMBUS:
							renderer = new RhombusZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.PARALLEOGRAM:
							renderer = new ParallZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.HEXAGON:
							renderer = new HexagonZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.TRIANGLE:
							renderer = new TriangleZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.RECT:
							renderer = new RectZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.ROUNDRECT:
							renderer = new RoundRectZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.OVAL:
							renderer = new OvalZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.RECTLABEL:
							renderer = new LabelZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.ROUNDLABEL:
							renderer = new RoundLabelZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
						case SHAPE_TYPE.TEXT:
							renderer = new TextZetaCellRenderer(id,'',x,y);
							break;
						case SHAPE_TYPE.HREF:
							renderer = new HrefZetaCellRenderer(id,'',x,y);
							break;
							//virtual sub net
						case SHAPE_TYPE.VIRTUALNET:
							//展示使用cloud的渲染器
							renderer = new CloudZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
							break;
					}
					vertex = new ZetaVertex(userObj, renderer);
					vertex.setStrokeColor(userObj.strokeColor);
					vertex.setStrokeWeight(userObj.strokeWeight);
					vertex.setFillColor(userObj.fillColor);
					if (userObj.dashed) {
						vertex.setDashed(true);
					}
					if (userObj.shadow) {
						vertex.setShadowVisible(true);
					}
					if (userObj.gradient) {
						vertex.setGradient(true);
						vertex.setGradientColor(userObj.gradientColor);
					}
					if (userObj.extrusion) {
						vertex.setExtrusion(true);
					}
					if (group) {
						groups[groups.length] = vertex;
						vertex.setGroupable(group, groupListener);
					}
					if (userObj.groupId > 0) {
						var parent = model.getVertex('cell' + userObj.groupId);
						if (parent != null) {
							parent.addChild(vertex);
						}
					}
					graph.insertVertex(vertex);
				}
			}
		}//END OF GROUP CREATION
		
		//FOLDER
		if (json.folder != null) {
			size = json.folder.length;
			for (var i = 0; i < size; i++) {
				userObj = json.folder[i];
				vertex = new ZetaVertex(userObj);
				vertex.setIconMarkerVisible(displayAlertIcon);
				if (userObj.groupId > 0) {
					var group = model.getVertex('cell' + userObj.groupId);
					if (group != null) {
						group.addChild(vertex);
					}
				}
				vertex.setIcon(userObj.icon);
				graph.insertVertex(vertex);
			}
		}//END OF FOLDER CREATION
		
		//ENTITY
        if (json.entity != null) {
			size = json.entity.length;
			if (showType == SHOW_TYPE.NAME) {
				for (var i = 0; i < size; i++) {
					userObj = json.entity[i];
					vertex = new ZetaVertex(userObj);
					vertex.setTextWidth(100);
					vertex.setLabel(userObj.name);
					vertex.setIconMarkerVisible(displayAlertIcon);
					if (userObj.groupId > 0) {
						var group = model.getVertex('cell' + userObj.groupId);
						if (group != null) {
							group.addChild(vertex);
						}
					}
					vertex.setIcon(userObj.icon);
					graph.insertVertex(vertex);
				}
			} else if (showType == SHOW_TYPE.SYSNAME){
				for (var i = 0; i < size; i++) {
					userObj = json.entity[i];
					vertex = new ZetaVertex(userObj);
					vertex.setTextWidth(100);
					vertex.setLabel(userObj.sysName);
					vertex.setIconMarkerVisible(displayAlertIcon);
					if (userObj.groupId > 0) {
						var group = model.getVertex('cell' + userObj.groupId);
						if (group != null) {
							group.addChild(vertex);
						}
					}
					vertex.setIcon(userObj.icon);
					graph.insertVertex(vertex);
				}
			} else {
                for (var i = 0; i < size; i++) {
					userObj = json.entity[i];
					vertex = new ZetaVertex(userObj);			
					vertex.setTextWidth(100);
					vertex.setLabel(userObj.ip);
					vertex.setIconMarkerVisible(displayAlertIcon);
					if (userObj.groupId > 0) {
						var group = model.getVertex('cell' + userObj.groupId);
						if (group != null) {
							group.addChild(vertex);
						}
					}
					vertex.setIcon(userObj.icon);
					graph.insertVertex(vertex);
				}
            }
		}//END OF ENTITY CREATION
        
        //把碎片加入到文档中，并清除掉碎片
		graph.endUpdateVertexs();
		
		//LINK
		if (json.link != null) {
			var src,dst;
			var edge;
			var size = json.link.length;
			for (var i = 0; i < size; i++) {
				userObj = json.link[i];
				if (model.getEdge(userObj.id)!=null) continue;
				src = model.getVertex('cell' + userObj.srcEntityId);
				if (src == null) continue;
				dst = model.getVertex('cell' + userObj.destEntityId);
				if (dst == null) continue;
				if (userObj.connectType == SHAPE_TYPE.STRAIGHT_CONNECT) {
					edge = new ZetaEdge(userObj, src, dst);
				} else if (userObj.connectType == SHAPE_TYPE.HORIZONAL_CONNECT) {
					edge = new ZetaEdge(userObj, src, dst,
						new HorizonalZetaEdgeRenderer(userObj.id, userObj.text, src.getAnchor(), dst.getAnchor()));
				} else if (userObj.connectType == SHAPE_TYPE.FOLDLINE_CONNECT) {
					edge = new ZetaEdge(userObj, src, dst,
					new FoldLineZetaEdgeRenderer(userObj.id, userObj.text, src.getAnchor(), dst.getAnchor()));
				} else if (userObj.connectType == SHAPE_TYPE.POLYLINE_CONNECT) {
					edge = new ZetaEdge(userObj, src, dst,
					new PolylineZetaEdgeRenderer(userObj.id, userObj.text, src.getAnchor(), dst.getAnchor()));
				} else {
					edge = new ZetaEdge(userObj, src, dst);
				}
				if (userObj.dashed) {
					edge.setDashed(true);
				}
				if (userObj.startArrow) {
					edge.setStartArrow(true);
				}
				if (userObj.endArrow) {
					edge.setEndArrow(true);
				}
				edge.update();
				edge.setLabelVisible(displayLinkLabel);
				graph.insertEdge(edge);
			}
		}//END OF LINK CREATION
		
		//VIRTUAL NET
		if(json.virtualNet != null){
			var id, x, y, type, renderer;
			size = json.virtualNet.length;
			for (var i = 0; i < size; i++) {
				userObj = json.virtualNet[i];
				id = userObj.id;
				x = userObj.x;
				y = userObj.y;
				type = userObj.type;
				if(type ==  SHAPE_TYPE.VIRTUALNET) {
					//展示使用cloud的渲染器
					renderer = new CloudZetaCellRenderer(id,'',x,y,userObj.width,userObj.height);
				}else{
					continue;
				}
				vertex = new ZetaVertex(userObj, renderer);
				vertex.setLabel(userObj.name);
				if (userObj.dashed) {
					vertex.setDashed(true);
				}
				if (userObj.shadow) {
					vertex.setShadowVisible(true);
				}
				if (userObj.gradient) {
					vertex.setGradient(true);
					vertex.setGradientColor(userObj.gradientColor);
				}
				if (userObj.extrusion) {
					vertex.setExtrusion(true);
				}
				if (group) {
					groups[groups.length] = vertex;
					vertex.setGroupable(group, groupListener);
				}
				if (userObj.groupId > 0) {
					var parent = model.getVertex('cell' + userObj.groupId);
					if (parent != null) {
						parent.addChild(vertex);
					}
				}
				graph.insertVertex(vertex);
			}
		}
	} finally {
		//一定要把碎片加入到文档中，并清除掉所有的碎片
		graph.endUpdate();
	}
	for (var i = 0; i < groups.length; i++) {
		if (groups[i].getUserObject().collapsed) {
			groups[i].collapse();
		}
	}
	setTimeout('setCellCluetip()', 2000);
}
function setCellCluetip() {
	model.eachCell(
		function(vertex) {
			userObj = vertex.getUserObject();
			//如果是SHAPE类型，则不添加cluetip
			if(userObj.objType == USEROBJ_TYPE.SHAPE){
				;
			}else{
				vertex.setCluetip(userObj.text, '../topology/loadNodeTip.tv?userObjType=' +
						userObj.objType +  '&userObjId=' + userObj.userObjId + '&folderId=' + topoFolderId);
			}
		}
	);
}
function createTopoFolder(json) {
	var vertex = new ZetaVertex(json);
	vertex.setIcon(json.icon);
	vertex.setIconMarkerVisible(displayAlertIcon);
	graph.insertVertex(vertex);
	vertex.setCluetip(json.text, '../topology/loadNodeTip.tv?userObjType=2&userObjId=' + json.userObjId);
}
function parseSubnetNode(json) {
	var rowHeight = 180;
	var columnWidth = 130;
	var entityOffsetLeft = 70;
	var entityOffsetTop = 50;
	var offsetTop = 40;
	var offsetLeft = 40;
	var offsetRight = 50;
	var radius = 6;
    if (json.entity == null) {
		return;
	}
	var len = json.entity.length;
	if (len > 0) {
		var width = 1024;
		var columns = Math.round((width - offsetLeft - offsetRight) / columnWidth);
		var rows = parseInt(len / columns);
		if (len % columns > 0) {
			rows++;
		}
        var count = 1;
		for (var i = 0; i < len; i++) {
			var r = parseInt(i / columns);
			var c = parseInt(i % columns);
			if (c == 0) {
				count = 1;
			}
			var x = entityOffsetLeft + c * columnWidth;
			var y = offsetTop + r * rowHeight;
            json.entity[i].x = x;
			json.entity[i].y = y + entityOffsetTop;
			json.entity[i].fixed = true;
			var vertex = new ZetaVertex(json.entity[i]);
			vertex.setIconMarkerVisible(displayAlertIcon);
			vertex.setIcon(json.entity[i].icon);
			graph.insertVertex(vertex);
			vertex.setCluetip(json.entity[i].text, '../topology/loadNodeTip.tv?userObjType=1&userObjId=' + json.entity[i].userObjId
				 + '&folderId=' + topoFolderId);
			var point = vertex.getCenter();
			var edge = new LineZetaCellRenderer('edge' + json.entity[i].nodeId, '', [point[0], y], [point[0]+2,point[1]]);
			edge.setStrokeColor('blue');
			edge.setFillColor('green');
			graph.view.insertDOMElement(edge.ui);
		}
        graph.createSubnetBus(width, rows, columns);
	}
}
function loadData(gotoId, refreshed) {
	selectedCount = 0;
	//---如果当前是子网拓扑图，就加载子网数据---//
	if (isSubnetMap) {
		loadSubnetData(gotoId, refreshed);
	} else {
		loadMapData(gotoId, refreshed);
	}
}
/**
 * 加载全网数据
 * @param gotoId
 * @param refreshed
 */
function loadMapData(gotoId, refreshed) {
	$.ajax({url: 'loadVertexByFolderId.tv', dataType: 'json', cache: 'false',
		data: {folderId: topoFolderId, refreshed: refreshed},
		success: function(json){
			parseTopoNode(json);
			goToEntity(gotoId);
			setTimeout('restoreTopologyState()', 5000);
			tabShown();
		},
		error: function(){
			window.top.showErrorDlg();
		}
	});
}
/**
 * 加载子网数据
 * @param gotoId
 * @param refreshed
 */
function loadSubnetData(gotoId, refreshed) {
	$.ajax({url: 'loadVertexByFolderId.tv', dataType: 'json', cache: 'false',
		data:{'folderId': topoFolderId, refreshed: refreshed},
		success:function(json){
			parseSubnetNode(json);
			goToEntity(gotoId);
			tabShown();
			setTimeout('restoreTopologyState()', 3000);
		},
		error:function(){
			window.top.showErrorDlg();
		}
	});
}
function splashGoToEntity() {
	splashCount++;
	splashFlag = !splashFlag;
	if (splashFlag) {
		selectionModel.deselectById('cell'+gotoId);
	} else {
		selectionModel.goToVertex('cell'+gotoId);
	}
	if (gotoId > 0 && splashCount++ < 50) {
		//---递归--//
		setTimeout("splashGoToEntity()", 300);
	}
}
/**
 * focus到某设备节点，如果为0则不定位
 * @param entityId
 */
function goToEntity(entityId) {
	gotoId = entityId;
	if (entityId > 0) {
		splashFlag = false;
		splashCount = 0;
		selectionModel.goToVertex('cell' + entityId);
		setTimeout('splashGoToEntity()', 300);
	}
}
function startTimer() {
	if(dispatcherTimer){
		clearInterval(dispatcherTimer);
	}
	dispatcherTimer = setInterval('dispatcherCallback()', dispatcherInterval);
}
function stopTimer() {
	if (dispatcherTimer != null) {
		clearInterval(dispatcherTimer);
		dispatcherTimer = null;
	}
	dispatcherCount = 0;
}
function defaultCallback() {}

/**
 * 返回的topo图设备状态数据的处理器
 * 状态信息Tooltip
 * @param json:entities,flows,folders
 */
function dispatcherHandler(json) {
	var size = 0;
	var arr = json.entities;
	size = arr.length;
	if (size > 0) {
		var vertex;
		var marker;
		var iconMarker;
		//----对每一个设备entity，设置其状态信息----//
		for (var i = 0; i < size; i++) {
			vertex = model.getVertex(arr[i].id);
			if (vertex == null) continue;
			//----marker就是tooltip----//
			marker = vertex.getMarker();
			if (marker == null) {
				marker = new FloatZetaMarker(vertex.getCenterX(), vertex.getY()-20);
				vertex.setMarker(marker);
				vertex.setMarkerVisible(displayEntityLabel);
				graph.insertMarker(marker);
			}
			if (arr[i].value > 0) {
				marker.setText(arr[i].value + '%');
				marker.setBackground(arr[i].backgroundColor);
			} else {
				marker.setText('');
				marker.setVisible(false);
			}
			if (vertex.getUserObject().alert != arr[i].alert) {
				vertex.getUserObject().alert = arr[i].alert;
			    if (markerAlertMode) {
			    	iconMarker = vertex.getIconMarker();
					if (iconMarker == null) {
						iconMarker = new IconZetaMarker(vertex.getCenterX()-8, vertex.getY()+vertex.getIconHeight()-10);
						iconMarker.setCluetip(arr[i].alt, '../alert/loadFloatingAlert.tv?entityId='+
							vertex.getUserObject().userObjId/* + '&ip=' + vertex.getUserObject().ip*/, false);
						vertex.setIconMarker(iconMarker);
						vertex.setIconMarkerVisible(displayAlertIcon);
						graph.insertMarker(iconMarker);
					}
					iconMarker.setIcon(ALERT_ICON[arr[i].alert]);
			    } else {
			    	var icon = vertex.getUserObject().icon;
			    	var index = icon.lastIndexOf('.');
			    	var prefix = icon.substring(0,index);
			    	var suffix = icon.substring(index+1);
			    	vertex.setIcon(prefix + arr[i].alert + '.png');
			    	vertex.update();
				}
			}
		}
	}
    arr = json.folders;
	size = arr.length;
	if (size > 0) {
		var vertex;
		var iconMarker;
		for (var i = 0; i < size; i++) {
			vertex = model.getVertex(arr[i].id);
			if (vertex == null) continue;
			if (vertex.getUserObject().alert != arr[i].alert) {
				vertex.getUserObject().alert = arr[i].alert;
				if (markerAlertMode) {
					iconMarker = vertex.getIconMarker();
					if (iconMarker == null) {
						iconMarker = new IconZetaMarker(vertex.getCenterX()-8, vertex.getY() + vertex.getIconHeight() - 10);
						vertex.setIconMarker(iconMarker);
						vertex.setIconMarkerVisible(displayAlertIcon);
						graph.insertMarker(vertex.getIconMarker());
					}
					iconMarker.setIcon(ALERT_ICON[arr[i].alert]);
				} else {
					var icon = vertex.getUserObject().icon;
			    	var index = icon.lastIndexOf('.');
			    	var prefix = icon.substring(0,index);
			    	var suffix = icon.substring(index+1);
			    	vertex.setIcon(prefix + arr[i].alert + '.png');
			    	vertex.update();
				}
			}
		}
	}
    arr = json.flows;
	size = arr.length;
	if (size > 0) {
		var edge;
		for (var i = 0; i < size; i++) {
			edge = model.getEdge(arr[i].id);
			if (edge != null) {
				edge.setEdgeColor(arr[i].strokeColor);
				edge.setLabel(arr[i].linklabel);
			}
		}
	}
}
/**
 * （第一次？）获取拓扑图中状态信息等
 */
function restoreTopologyState() {
 	$.ajax({url: '../topology/getTopologyStateFirstly.tv', dataType: 'json', cache: false,
 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
    	success: dispatcherHandler,
    	error: defaultCallback//function(){},nothing happened
    });
}
/**
 * 每隔1个基本时间间隔就采集一次拓扑结构
 * 每隔30个基本时间间隔就刷新一次页面
 * target : cell周围的告警状态等,tooltip中的状态信息不是从这里读取
 */
function dispatcherCallback() {
	if (++dispatcherCount > 30) {
		dispatcherCount = 0;
		onRefreshClick();
	} else {
	 	$.ajax({url: '../topology/getTopologyStateNewly.tv', dataType: 'json', cache: false,
	 		data: {folderId: topoFolderId, folderPath: folderPath, entityLabel: entityLabel, linkLabel: linkLabel},
	    	success: dispatcherHandler,
	    	error: defaultCallback
	    });
    }
}
function setMapRefreshInterval(i, folderId) {
	if (folderId == topoFolderId) { 
		stopTimer();
		dispatcherInterval = i; 
		startTimer(); 
	} 
}
function setBackgroundFlag(flag, background) { backgroundFlag = flag; if (flag) { setBackgroundPosition(backgroundPosition);graph.setBackgroundColor(backgroundColor);graph.setBackgroundImage(background); backgroundImg = background; } else { graph.setBackgroundColor(background); backgroundColor = background; } }
function setBackgroundPosition(p) { backgroundPosition = p; if (p == 0) { graph.setBackgroundPosition('0 0'); graph.setBackgroundRepeat('no-repeat'); } else if (p == 1) { graph.setBackgroundPosition('center center'); graph.setBackgroundRepeat('no-repeat'); } else if (p == 2) { graph.setBackgroundPosition('0 0'); graph.setBackgroundRepeat('repeat'); } }
function setDisplayName(showTypeI) {
	renamable = (showTypeI == SHOW_TYPE.NAME);
    displayName = (showTypeI == SHOW_TYPE.NAME);
    displaySysName = (showTypeI == SHOW_TYPE.SYSNAME);
    showType = showTypeI
    var userObj;
    if (showTypeI == SHOW_TYPE.NAME) {
		model.eachVertex(
			function(vertex) {
				userObj = vertex.getUserObject();
				if(userObj != null && userObj.objType == USEROBJ_TYPE.ENTITY) {
					vertex.setLabel(userObj.name);
					vertex.setTextWidth(100);
				}
			}
		);
	} else if (showTypeI == SHOW_TYPE.SYSNAME) {
		model.eachVertex(
			function(vertex) {
				userObj = vertex.getUserObject();
				if(userObj != null && userObj.objType == USEROBJ_TYPE.ENTITY) {
					vertex.setLabel(userObj.sysName);
					vertex.setTextWidth(100);
				}
			}
		);
	} else {
		model.eachVertex(
			function(vertex) {
				userObj = vertex.getUserObject();
				if(userObj != null && userObj.objType == USEROBJ_TYPE.ENTITY) {
					vertex.setLabel(userObj.ip);
					vertex.setTextWidth(100);
				}
			}
		);
	}

	if (isSubnetMap) {
		relayoutSubnet();
	}
}
function setDisplayAlertIcon(visible) { displayAlertIcon = visible; model.eachVertex(function(vertex) { vertex.setIconMarkerVisible(visible); }); }
function setDisplahEntityLabel(visible) { displayEntityLabel = visible; model.eachVertex(function(vertex) { vertex.setMarkerVisible(visible); }); }
function setDisplahLinkLabel(visible) { displayLinkLabel = visible; model.eachEdge(function(edge) { edge.setLabelVisible(visible); }); }
function setDisplahLinkShadow(visible) { SHAPE_STYLE.linkShadow = visible; model.eachEdge(function(edge) { edge.setShadowVisible(visible); }); }
function setDisplayLink(visible) { displayLink = visible; model.eachEdge(function(edge) { edge.setVisible(visible); }); }
function synUpdateLinkWidth(w) { SHAPE_STYLE.linkWeight = w; model.eachEdge(function(edge) { edge.setEdgeWeight(w); }); }
function setDefaultEdgeColor(color) { SHAPE_STYLE.linkColor = color; model.eachEdge(function(edge) { edge.setEdgeColor(color); }); }
function updateNodeUrl(id, url) { var vertex = model.getVertex('cell' + id); if (vertex !=  null) { var userObj = vertex.getUserObject(); if (typeof url != 'undefined') { userObj.url = url; } } }
function updateNodeName(id, name, url) {
	var vertex = model.getVertex('cell' + id);
	if (vertex !=  null) {
		var userObj = vertex.getUserObject();
		if (typeof url != 'undefined') {
			userObj.url = url;
		}
		userObj.name = name;
		if (showType == SHOW_TYPE.NAME) {
			vertex.setLabel(name);
		}
	}
}
function fixNodeLocation(id, fixed) {
    var vertex = model.getVertex('cell' + id);
    if (vertex != null) {
        vertex.setFixed(fixed);
    }
}
function synRemoveVertex(id) { graph.removeVertexById('cell' + id); }
function synUpdateNodeIcon(folderId, src) { var vertex = model.getVertex('cell'+folderId); if (vertex != null) { vertex.setIcon(src); vertex.update(); } }
function onArrowClassicClick() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setStartArrowStyle('Classic'); selectee.update(); } }
function onArrowOpenClick() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setStartArrowStyle('Open'); selectee.update(); } }
function onArrowOvalClick() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setStartArrowStyle('Oval'); selectee.update(); } }
function onArrowBlockClick() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setStartArrowStyle('Block'); selectee.update(); } }
function onArrowNoneClick() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setStartArrowStyle('none'); selectee.update(); } }
function onArrowClassicClick1() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setEndArrowStyle('Classic'); selectee.update(); } }
function onArrowOpenClick1() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setEndArrowStyle('Open'); selectee.update(); } }
function onArrowOvalClick1() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setEndArrowStyle('Oval'); selectee.update(); } }
function onArrowBlockClick1() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setEndArrowStyle('Block'); selectee.update(); } }
function onArrowNoneClick1() { var selectee = selectionModel.getSelectedEdge(); if (selectee != null) { selectee.setEndArrowStyle('none'); selectee.update(); } }
function onStraightConnectorClick() {
	var selectee = selectionModel.getSelectedEdge();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		graph.setConnector(selectee,
		new DefaultZetaEdgeRenderer(userObj.id, userObj.text, selectee.getSource().getAnchor(), selectee.getTarget().getAnchor()));
	}
}
function onHorizonalConnectorClick() {
	var selectee = selectionModel.getSelectedEdge();
	if (selectee != null) {
		var userObj = selectee.getUserObject();
		graph.setConnector(selectee,
			new HorizonalZetaEdgeRenderer(userObj.id, userObj.text, selectee.getSource().getAnchor(), selectee.getTarget().getAnchor()));
	}
}
function selectFillColor() { window.top.createDialog('colorDlg', I18N.COMMON.colorPicker, 280, 340, 'include/colorDlg.jsp', null, true, true, setVmlFillColor); }
function setVmlFillColor() { var callback = window.top.getZetaCallback(); if (callback != null && callback.type == 'colorpicker') { SHAPE_STYLE.fillColor = callback.color; } }
function setDisplayGridFlag(flag) {
	displayGrid = flag;
}
function setImageOrColorFlag(flag) {
	backgroundFlag = flag;
}
function setBackgroundImg(image) {
	backgroundImg = image;
}
function setBackgroundColor(color) {
	backgroundColor = color;
}
function showBackground() {  
	if (displayGrid) {
		setBackgroundPosition(2);
		graph.setBackgroundImage(backgroundGrid);
	} else {
		if (backgroundFlag) {
			setBackgroundPosition(backgroundPosition);
			graph.setBackgroundColor(backgroundColor);
			graph.setBackgroundImage(backgroundImg);
		} else {
			graph.setBackgroundColor(backgroundColor);
		}
	}
}
function setMarkerAlertMode(flag) { markerAlertMode = flag; }