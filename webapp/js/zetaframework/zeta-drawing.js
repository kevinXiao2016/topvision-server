/**
 * Zetaframework JavaScript Vml Drawing by no OO.
 *
 * (C) TopoView, 2008-04-08
 *
 * authro: niejun
 * 
 */
var USEROBJ_TYPE = { ENTITY: 1, FOLDER: 2, SHAPE: 3, LINK: 4 };

var SHAPE_TYPE = { SOLID_LINE: 10, DASHED_LINE: 11, ARROW_LINE: 12, DARROW_LINE: 13,
	POLY_LINE: 14, RECT: 16, ROUNDRECT: 17, OVAL: 18, TEXTRECT: 19,
	TEXT: 24, PICTURE: 25, CLOUDY: 31, SUBNET: 32, HREF: 33 };

var SHAPE_STYLE = {fillColor: "#c3d9ff", fillColor2: "#FFFFFF", 
	rectTextSize: 12, textSize: 12, shadowColor: '#808080',
	shadowOffset: '4px,4px', selectedColor: '#000000', gradient: true};

var linkStyle = {imgOffset: 6, selectedColor: '#000000', color: '#000000', width: '1px',
	linkShadowType: 'single', linkShadowColor: '#e0e0e0'};

var CURSOR_TYPE = {POINTER: 0, SQUARE: 1, MOVE: 2, SELECTALL: 3};

var shapeLang_en = {dbclickInputMsg: 'Please input message by double click'};
var shapeLang = shapeLang_en;

var dashedHelper = {border: '1px dashed black', background: 'transparent'};
var squareHelper = {border: '1px solid #316AC5', background: '#D2E0F0', opacity:0.5};

var zoom = {size: 1, fontSize: 16, iconSize: 32, 
	textWidth: 82, width: 92, height: 80,
	imgWidth: 48, imgHeight: 48, smallIconWidth: 16, smallIconHeight: 16,
	smallIconOffsetLeft: -32, smallIconOffsetBottom: 2,
	smallLeftOffset: 1, smallBottomOffset: 2,
	zooming: false, scaleIndex: 4, scale:[0.10, 0.25, 0.50, 0.75, 1, 1.25, 1.5, 2, 3, 5]};

var displayName = true;
var displayLinkLabel = true;
var backgroundFlag = true;
var backgroundImg = '';
var backgroundPosition = 0;
var backgroundColor = '#FFFFFF';
var displayGrid = false;
var backgroundGrid = 'url(../css/grid_bg.gif)';

var arrangeType = 0;
var curDrawingType = CURSOR_TYPE.POINTER;

var drawingCallbackFunc = function() {};
var shapeFocusedCallback = function() {};
var shapeBluredCallback = function() {};
var shapeMovedCallback = function() {};
var deleteCallbackFunc = function() {};
var selectCallbackFunc = function() {};

var toolbarOffset = 26;
var shapeIdSequence = 0;
var shapex = 0, shapey = 0;
var offsetx = 0, offsety = 0;
var movex = 0, movey = 0, movex1 = 0, movey1 = 0;
var shapeNodeDrawing = true;
var shapeMoving = false;
var shapeMoved = false;
var zgraphId;
var shapeContainer = null;

var curDrawingShape = null;
var focusedShapeId = 0;
var focusRenameId = 0;
var renameNodeId = null;
var renamable = true;
var renaming = false;
var focusLinkId;

var goToEntityId = 0;

var splashFlag = false;
var splashStarted = false;
var splashCount = 0;
var MAX_SPLASH = 50;

var allSelected = false;
var linkAdding = false;
var linkSrcEntityId = null;

var isSubnetMap = false;

var linkShadow = true;

var selectedCount = 0;

// cache src of link, key=entityId, value=link's ids
var linkSrcMap = null;
// cache dst of link, key=entityId, value=link's ids
var linkDestMap = null;
// cache link, key=srcEntityId,destEntityId, value=linkId
var linkMap = null;
// User Object
var userObjMap = null;

// entity and folder's element map
var nodeElMap = null;
// link's element map
var linkElMap = null;
// link label's element map
var linkLabelElMap = null;

function pxToPt(px) {
	return px * 0.75;
}

function ptToPx(pt) {
	return parseInt(pt * 4.0/3);
}

function emToPx(em) {
	return 16 * em;
}

function emToPt(em) {
	return 12 * em;
}

function pointToPt(from) {
	var arr = ('' + from).split(',');
	return [parseFloat(arr[0].replace("pt", "")),
		parseFloat(arr[1].replace("pt", ""))];
}

function pointToPx(from) {
	var arr = ('' + from).split(',');
	return [ptToPx(parseFloat(arr[0].replace("pt", ""))),
		ptToPx(parseFloat(arr[1].replace("pt", "")))];
}

/* 2px */
function pxToCoord(px) {
	return px.replace("px", "");
}

/* 2pt */
function ptToCoord(pt) {
	return pt.replace("pt", "");
}

function setDrawingCallback(func) {
	drawingCallbackFunc = func;
}

function setShapeCallback(focus, blur) {
	shapeFocusedCallback = focus;
	shapeBluredCallback = blur;
}

function setShapeMovedCallback(callback) {
	shapeMovedCallback = callback;
}

function setDeleteCallback(callback) {
	deleteCallbackFunc = callback;
}

function setSelectCallbackFunc(callback) {
	selectCallbackFunc = callback;
}

function getMapWidth() {
	var width = shapeContainer.scrollWidth;
	if (width < shapeContainer.offsetWidth) {
		width = shapeContainer.offsetWidth;
	}
	return width;
}
function getMapHeight() {
	var height = shapeContainer.scrollHeight;
	if (height < shapeContainer.offsetHeight) {
		height = shapeContainer.offsetHeight;
	}
	return height;
}

function initZoomParam(zoomSize, fontSize, iconSize) {
	zoom.size = zoomSize;
	zoom.fontSize = fontSize;
	zoom.iconSize = iconSize;
	if (zoom.iconSize > 48) {
		zoom.textWidth = 100;
	}
	zoom.fontSize = parseInt(zoom.fontSize * zoom.size);
	zoom.textWidth = parseInt(zoom.textWidth * zoom.size);
	zoom.width = zoom.textWidth + 10;
	zoom.height = parseInt(zoom.height * zoom.size);
	zoom.imgWidth = parseInt(zoom.iconSize * zoom.size);
	zoom.imgHeight = parseInt(zoom.iconSize * zoom.size);
	zoom.smallIconWidth = parseInt(zoom.smallIconWidth * zoom.size);
	zoom.smallIconHeight = parseInt(zoom.smallIconHeight * zoom.size);
	if (zoom.iconSize == 0) {
		zoom.smallIconOffsetLeft = -32;
	} else {
		zoom.smallIconOffsetLeft = parseInt(zoom.imgWidth / 2);
	}
	zoom.smallIconOffsetBottom = 2;
}

function setNodeIconSize(size) {
	if (zoom.iconSize == size || size == 0) {
		return;
	}
	zoom.iconSize = size;
	if (zoom.size == 1) {
		zoom.imgWidth = zoom.iconSize;
		zoom.imgHeight = zoom.iconSize;
		if (zoom.iconSize > 48) {
		   	zoom.textWidth = 100;
			zoom.width = zoom.textWidth + 10;
		} else {
			zoom.textWidth = 82;
			zoom.width = zoom.textWidth + 10;
		}
		zoom.smallIconOffsetLeft = -(zoom.imgWidth + zoom.smallIconWidth + zoom.smallLeftOffset);
		zoom.smallIconOffsetBottom = zoom.imgHeight - zoom.smallIconHeight - zoom.smallBottomOffset;
		var children = shapeContainer.children;
		var len = children.length;
	   	var child = null;
	   	for (var i = 0; i < len; i++) {
	   		child = children[i];
	   		if (child.objType == USEROBJ_TYPE.ENTITY || child.objType == USEROBJ_TYPE.FOLDER) {
	   			child.style.width = zoom.width;
				child.children[0].width = zoom.imgWidth;
				child.children[0].height = zoom.imgHeight;
				child.children[1].style.marginLeft = zoom.smallIconOffsetLeft;
				child.children[1].style.marginBottom = zoom.smallIconOffsetBottom;				
				changeLinkByEntity(child.entityId,
					child.offsetLeft + parseInt(zoom.width / 2),
					child.offsetTop + parseInt(zoom.imgHeight / 2) + linkStyle.imgOffset);
	   		}
	   	}
	}
}
function setBackgroundFlag(flag, param) {
	backgroundFlag = flag;
	if (flag) {
		backgroundImg = 'url(' + param + ')';
	}
	if (!displayGrid) {
		if (flag) {
			shapeContainer.style.backgroundImage = backgroundImg;
		} else {
			shapeContainer.style.backgroundImage = '';
			shapeContainer.style.backgroundColor = backgroundColor;
		}
	}
}
function setBackgroundColor(color) {
	backgroundColor = color;
	shapeContainer.style.backgroundColor = backgroundColor;
}

function setBackgroundPosition(p) {
	if (backgroundFlag) {
		if (p == 0) {
			shapeContainer.style.backgroundPosition = '0 0';
			shapeContainer.style.backgroundRepeat = 'no-repeat';
		} else if (p == 1) {
			shapeContainer.style.backgroundPosition = 'center center';
			shapeContainer.style.backgroundRepeat = 'no-repeat';
		} else if (p == 2) {
			shapeContainer.style.backgroundPosition = '0 0';
			shapeContainer.style.backgroundRepeat = 'repeat';
		}	
	}
}

function setDisplayGrid(flag) {
	displayGrid = flag;
	if(flag) {
		shapeContainer.style.backgroundImage = backgroundGrid;
		shapeContainer.style.backgroundRepeat = 'repeat';
	} else {
		if(backgroundFlag) {
			shapeContainer.style.backgroundImage = backgroundImg;
			if (backgroundPosition == 2) {
				shapeContainer.style.backgroundRepeat = 'repeat';
			} else {
				shapeContainer.style.backgroundRepeat = 'no-repeat';
			}
		} else {
			shapeContainer.style.backgroundImage = '';
			shapeContainer.style.backgroundColor = backgroundColor;
		}
	}
}

function getUserObject(nodeId) {
	return userObjMap.get(nodeId);
}

function putUserObject(nodeId, obj) {
	userObjMap.put(nodeId, obj);
}

function setCursorType(type) {
	curDrawingType = type;
	if (curDrawingType >= SHAPE_TYPE.SOLID_LINE) {
		shapeContainer.style.cursor = "crosshair";
	} else {
		if (curDrawingType == CURSOR_TYPE.MOVE) {
			shapeContainer.style.cursor = "url(../css/images/openhand.cur)";
		} else {
			shapeContainer.style.cursor = "default";
		}
	}
	var status = "enable";
	if (curDrawingType == CURSOR_TYPE.POINTER) {
		$("#" + zgraphId).selectable("setHelper", squareHelper);
	} else if(curDrawingType == CURSOR_TYPE.SQUARE) {
		$("#" + zgraphId).selectable("setHelper", dashedHelper);
	} else {
		status = "disable";
	}
	$("#" + zgraphId).selectable(status);
	nodeElMap.eachKey(function(k) {
		$('#vertex' + k).draggable(status);
	});	
}

var countForOrgin = 0;
var depthForOrgin = 1;
function findEntityByOrgin(filters, orgin) {
	countForOrgin++;
	if (countForOrgin > depthForOrgin) {
		return;
	}
	var link = null;
	var node = null;
	var arr = linkSrcMap.get(orgin);
	if (arr != null) {
		for (var i = 0; i < arr.length; i++) {
			link = linkElMap.get(arr[i]);
			if (link) {
				node = nodeElMap.get(link.destEntityId);
				if (node) {
					filters.put(link.destEntityId, link.destEntityId);
					findEntityByOrgin(filters, link.destEntityId);
					countForOrgin--;
				}
			}
		}
	}	
	arr = linkDestMap.get(orgin);
	if (arr != null) {
		for (var i = 0; i < arr.length; i++) {
			link = linkElMap.get(arr[i]);
			if (link) {
				node = nodeElMap.get(link.srcEntityId);
				if (node) {
					filters.put(link.srcEntityId, link.srcEntityId);
					findEntityByOrgin(filters, link.srcEntityId);
					countForOrgin--;
				}
			}
		}
	}	
}

function splashGoToEntity() {
	var e = nodeElMap.get(goToEntityId);
	if (e != null) {
		splashFlag = !splashFlag;
		if (splashFlag) {
			e.className = 'ui-unselecting';
		} else {
			e.className = 'ui-gotoed';
		}
	}
	if (goToEntityId > 0 && splashCount++ < MAX_SPLASH) {
		setTimeout("splashGoToEntity()", 300);
	}
}
function goToEntity(id) {
	if (id > 0) {
		if (goToEntityId > 0) {
			var e = nodeElMap.get(goToEntityId);
			if (e != null && e.className.indexOf("ui-gotoed") > -1) {
				e.className = 'ui-unselecting';
			}
		}
		var e = nodeElMap.get(id);
		if (e != null) {
			goToEntityId = id;
			shapeContainer.scrollLeft = e.offsetLeft;
			shapeContainer.scrollTop = e.offsetTop;
			e.className = 'ui-gotoed';
			if (!splashStarted) {
				splashCount = 0;
				splashStarted = true;
				setTimeout("splashGoToEntity()", 300);
			}
		}
	}
}

var rowHeight = 180;
var columnWidth = 130;
var entityOffsetLeft = 70;
var entityOffsetTop = 50;
var offsetTop = 40;
var offsetLeft = 40;
var offsetRight = 50;
var radius = 6;
function createSubnetBus(width, rows, columns) {
	setDrawingCallback(function(){});
	var startPoint = buildShapeNode(shapeIdSequence++, SHAPE_TYPE.OVAL, offsetLeft - radius, offsetTop - radius);
	startPoint.style.width = 2 * radius;
	startPoint.style.height = 2 * radius;
	startPoint.id = 'busStart';
	startPoint.title = topoFolderName;
	startPoint.onmousemove = null;
	
	var endPoint;
	if (rows % 2 == 0) {
		endPoint = buildShapeNode(shapeIdSequence++, SHAPE_TYPE.OVAL, offsetLeft - radius, (rows - 1) * rowHeight + offsetTop - radius);
	} else {
		endPoint = buildShapeNode(shapeIdSequence++, SHAPE_TYPE.OVAL, width - offsetRight - radius, (rows - 1) * rowHeight + offsetTop - radius);
	}
	endPoint.style.width = 2 * radius;
	endPoint.style.height = 2 * radius;	
	endPoint.id = 'busEnd';
	endPoint.title = topoFolderName;	
	endPoint.onmousemove = null;

	var bus = document.createElement('v:PolyLine');
	bus.id = 'bus';
	bus.filled = "false";
	bus.strokeWeight = '1.2pt';
	
	var buffer = new Zeta$StringBuffer();				
	for (var i = 0; i < rows; i++) {
		if (i % 2 == 0) {
			buffer.append(offsetLeft);
			buffer.append(',');
			buffer.append(i * rowHeight + offsetTop);
			buffer.append(' ');
			buffer.append(width - offsetRight);
			buffer.append(',');
			buffer.append(i * rowHeight + offsetTop);
		} else {
			buffer.append(width - offsetRight);
			buffer.append(',');
			buffer.append(i * rowHeight + offsetTop);
			buffer.append(' ');
			buffer.append(offsetLeft);
			buffer.append(',');
			buffer.append(i * rowHeight + offsetTop);											
		}
		buffer.append(' ');
	}
	
	bus.points = buffer.toString();
	shapeContainer.appendChild(bus);
	shapeContainer.appendChild(startPoint);
	shapeContainer.appendChild(endPoint);
	setDrawingCallback(requestMapNode);
	setDeleteCallback(onDeleteClick);
}

function initGraph(id) {
	zgraphId = id;
	shapeContainer = Zeta$(id);
	nodeElMap = new Zeta$H();
	linkElMap = new Zeta$H();
	linkLabelElMap = new Zeta$H();
	linkSrcMap = new Zeta$H();
	linkDestMap = new Zeta$H();
	linkMap = new Zeta$H();

	var jid = "#" + zgraphId; 
	$(jid).selectable({
		filter: 'div', 
		cancel: 'img,span',
		distance: 5,
		selected: function() {selectedCount++;},
		start: function() {selectedCount=0;},
		stop: function() {
			selectCallbackFunc();
		}
	});
	$(jid).selectable("setHelper", squareHelper);
	return shapeContainer;
}

function clearGraph() {
	var children = shapeContainer.children;
	for(var i = children.length - 1; i >= 0; i--) {
		shapeContainer.removeChild(children[i]);
	}
	nodeElMap.clear();
	linkElMap.clear();
	linkLabelElMap.clear();
	linkDestMap.clear;
	linkSrcMap.clear();
	linkMap.clear();
	goToEntityId = 0;
	splashStarted = false;
}

function destroyGraph() {
}

function zoomGraph(size) {
	if(zoom.size == size || zoom.zooming) return;
	zoom.zooming = true;
   	zoom.size = size;
   	zoom.fontSize = parseInt(12 * zoom.size);
   	zoom.imgWidth = parseInt(zoom.iconSize * zoom.size);
   	zoom.imgHeight = parseInt(zoom.iconSize * zoom.size);
   	if (displayName) {
   		zoom.textWidth = parseInt(82 * zoom.size);
   	} else {
   		zoom.textWidth = parseInt(100 * zoom.size);
   	}
	zoom.width = zoom.textWidth + 10;
	zoom.height = parseInt(80 * zoom.size);
	zoom.smallIconWidth = parseInt(16 * zoom.size);
	zoom.smallIconHeight = parseInt(16 * zoom.size);
	if (zoom.iconSize == 0) {
		zoom.smallIconOffsetLeft = -32;
	} else {
		zoom.smallIconOffsetLeft = parseInt(zoom.imgWidth / 2);
	}
	zoom.smallIconOffsetBottom = 2;

	//var zoomPer = Math.round(zoom.size*100) + '%';
	nodeElMap.eachValue(function(node) {
		if (node.objType == USEROBJ_TYPE.ENTITY || node.objType == USEROBJ_TYPE.FOLDER) {
			var w = parseInt(node.children[0].coordWidth * zoom.size);
			
			if (w > zoom.textWidth) {
				node.style.width = w + 10;
			} else {
				node.style.width = zoom.textWidth + 10;
			}

			node.children[0].width = w;
			node.children[0].height = parseInt(node.children[0].coordHeight * zoom.size);
			node.children[1].width = zoom.smallIconWidth;
			node.children[1].height = zoom.smallIconHeight;
			
			node.children[1].style.marginLeft = -parseInt((node.children[0].clientWidth + zoom.smallIconWidth) / 2);
			node.children[1].style.marginBottom = zoom.smallIconOffsetBottom;							
			
			//node.children[0].style.zoom = zoomPer;
			//node.children[1].style.zoom = zoomPer;

			node.children[3].style.width = zoom.textWidth;
			node.children[3].style.fontSize = zoom.fontSize + 'px';

			node.style.left = parseInt(node.coordX * size);
			node.style.top = parseInt(node.coordY * size);
			changeLinkByEntity(node.entityId,
				node.offsetLeft + parseInt(node.clientWidth / 2),
				node.offsetTop + parseInt(node.children[0].clientHeight / 2) + linkStyle.imgOffset);
   		} else if (node.objType == USEROBJ_TYPE.SHAPE) {
   			if (node.figureType < SHAPE_TYPE.POLY_LINE) {
   				node.from = node.coordX * zoom.size + ',' + node.coordY * zoom.size;
   				node.to = (node.coordX + node.coordWidth) * zoom.size + ',' + (node.coordY + node.coordHeight) * zoom.size;
   			} else {
				node.style.left = parseInt(node.coordX * zoom.size);
				node.style.top = parseInt(node.coordY * zoom.size);
				if (node.figureType == SHAPE_TYPE.TEXT) {
					node.style.fontSize = parseInt(node.fontSize * zoom.size) + 'pt'; 
				} else if (node.figureType == SHAPE_TYPE.TEXTRECT) {
					node.children[1].style.fontSize = parseInt(node.fontSize * zoom.size) + 'pt';
					node.style.width = parseInt(node.coordWidth * zoom.size);
					node.style.height = parseInt(node.coordHeight * zoom.size);
				} else if (node.figureType == SHAPE_TYPE.PICTURE) {
   					node.children[0].width = parseInt(node.children[0].coordWidth * zoom.size);
   					node.children[0].height = parseInt(node.children[0].coordHeight * zoom.size);
					node.children[3].style.width = zoom.textWidth;
					node.children[3].style.fontSize = zoom.fontSize + 'px';   					
					changeLinkByEntity(node.entityId, node.offsetLeft + parseInt(node.clientWidth / 2),
						node.offsetTop + parseInt(node.children[0].clientHeight/2) + linkStyle.imgOffset);					
   				} else {
					node.style.width = parseInt(node.coordWidth * zoom.size);
					node.style.height = parseInt(node.coordHeight * zoom.size);
				}
			}
   		}	
	});
   	zoom.zooming = false;
}

function changeLinkByEntity(entityId, x, y) {
	var fromx, fromy, tox, toy;
	var left = 0, top = 0;
	var link = null;
	var span = null;
	var arr = null;
	var arr1 = null;
	arr = linkSrcMap.get(entityId);
	for (var m = 0; m < arr.length; m++) {
		link = linkElMap.get(arr[m]);
		if (link == null) {
			continue;
		}
		fromx = x;
		fromy = y;
		arr1 = pointToPx(link.to);
		tox = arr1[0];
		toy = arr1[1];
		if (fromx > tox) {
			left = parseInt((fromx - tox)/2) - 2*zoom.fontSize;
		} else {
			left = parseInt((tox - fromx)/2) - 2*zoom.fontSize;
		}
		if (left < 0) {
			left = 2;
		}
		if (fromy > toy) {
			top = parseInt((fromy - toy)/2) - zoom.fontSize;
		} else {
			top = parseInt((toy - fromy)/2) - zoom.fontSize;
		}
		link.from = x + "px," + y + 'px';
		span = link.children[0];
		span.style.fontSize = zoom.fontSize;
		span.style.left = left;
		span.style.top = top;
	}
	arr = linkDestMap.get(entityId);
	for (var n = 0; n < arr.length; n++) {
		link = linkElMap.get(arr[n]);
		if(link != null){
			tox = x;
			toy = y;
			arr1 = pointToPx(link.from);
			fromx = arr1[0];
			fromy = arr1[1];
			if (fromx > tox) {
				left = parseInt((fromx - tox)/2) - 2*zoom.fontSize;
			} else {
				left = parseInt((tox - fromx)/2) - 2*zoom.fontSize;
			}
			if (left < 0) {
				left = 2;
			}
			if (fromy > toy) {
				top = parseInt((fromy - toy)/2) - zoom.fontSize;
			} else {
				top = parseInt((toy - fromy)/2) - zoom.fontSize;
			}
			link.to = x + "px," + y + 'px';
			span = link.children[0];
			span.style.fontSize = zoom.fontSize;
			span.style.left = left;
			span.style.top = top;	
		}
	}
}

function moveEntityUpOrDown(increment) {
   	var children = shapeContainer.children;
   	var child = null;
   	for (var i = 0; i < children.length; i++) {
   		child = children[i];
   		if (child.tagName == 'DIV' || child.tagName == 'div') {
			if (child.className.indexOf('selected') != -1) {
				child.style.top = child.offsetTop + increment;
				changeLinkByEntity(child.entityId,
					child.offsetLeft + parseInt(child.clientWidth / 2),
					child.offsetTop + parseInt(child.children[0].height / 2) + linkStyle.imgOffset);
			}
   		}
   	}
}
function moveEntityLeftOrRight(increment) {
   	var children = shapeContainer.children;
   	var child = null;
   	for (var i = 0; i < children.length; i++) {
   		child = children[i];
   		if (child.tagName == 'DIV' || child.tagName == 'div') {
			if (child.className.indexOf('selected') != -1) {
				child.style.left = child.offsetLeft+increment;
				changeLinkByEntity(child.entityId,
					child.offsetLeft + parseInt(child.clientWidth / 2),
					child.offsetTop + parseInt(child.children[0].height / 2) + linkStyle.imgOffset);
			}
   		}
   	}
}

function onLeftAlignClick() {
	var nodes = getSelectedVertexId();
	if (nodes.length < 2) {return;}
	var x = nodeElMap.get(nodes[0]).offsetLeft;
	for (var i = 1; i < nodes.length; i++) {
		var t = nodeElMap.get(nodes[i]).offsetLeft;
		if (t < x) {
			x = t;
		}
	}
	var el = null;
	for (var i = 0; i < nodes.length; i++) {
		el = nodeElMap.get(nodes[i]);
		el.style.left = x;
		changeLinkByEntity(nodes[i], el.offsetLeft+parseInt(el.clientWidth/2),
			el.offsetTop+linkStyle.imgOffset+parseInt(el.children[0].height/2));
	}
}
function onRightAlignClick() {
	var nodes = getSelectedVertexId();
	if (nodes.length < 2) {return;}
	var el = null;
	var x = nodeElMap.get(nodes[0]).offsetLeft;
	for (var i = 1; i < nodes.length; i++) {
		var t = nodeElMap.get(nodes[i]).offsetLeft;
		if (t > x) {
			x = t;
		}
	}
	for (var i = 0; i < nodes.length; i++) {
		el = nodeElMap.get(nodes[i]);
		el.style.left = x;
		changeLinkByEntity(nodes[i], el.offsetLeft+parseInt(el.clientWidth/2),
			el.offsetTop+linkStyle.imgOffset+parseInt(el.children[0].height/2));
	}
}
function onHorizontalAlignClick() {
	var nodes = getSelectedVertexId();
	if (nodes.length < 2) {return;}
	var el = null;
	var x = nodeElMap.get(nodes[0]).offsetTop;
	for (var i = 1; i < nodes.length; i++) {
		var t = nodeElMap.get(nodes[i]).offsetTop;
		if (t < x) {
			x = t;
		}
	}
	for (var i = 0; i < nodes.length; i++) {
		el = nodeElMap.get(nodes[i]);
		el.style.top = x;
		changeLinkByEntity(nodes[i], el.offsetLeft+parseInt(el.clientWidth/2),
			el.offsetTop+linkStyle.imgOffset+parseInt(el.children[0].height/2));
	}
}
function onVerticalAlignClick() {
	var nodes = getSelectedVertexId();
	if (nodes.length < 2) {return;}
	var el = null;
	var x = nodeElMap.get(nodes[0]).offsetTop;
	for (var i = 1; i < nodes.length; i++) {
		var t = nodeElMap.get(nodes[i]).offsetTop;
		if (t > x) {
			x = t;
		}
	}
	for (var i = 0; i < nodes.length; i++) {
		el = nodeElMap.get(nodes[i]);
		el.style.top = x;
		changeLinkByEntity(nodes[i], el.offsetLeft+parseInt(el.clientWidth/2),
			el.offsetTop+linkStyle.imgOffset+parseInt(el.children[0].height/2));
	}
}

function selectAllCell() {
	allSelected = true;
	nodeElMap.eachValue(function(v) {
 		if (v.objType == USEROBJ_TYPE.ENTITY || v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-selected';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselected';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '3';
   				v.style.padding = '0px';
   			} else {
   				v.strokecolor = linkStyle.selectedColor;
   			}
   		}	
	});
	
	linkElMap.eachValue(function(v) {
		v.strokecolor = linkStyle.selectedColor;
	});
}
function disselectAllCell() {
	allSelected = false;
	
	var selectees = $(".ui-selected");
	for (var i = 0; i < selectees.length; i++) {
		selectees[i].className = 'ui-unselecting';
	}
	/*nodeElMap.eachValue(function(v) {
 		if (v.objType == USEROBJ_TYPE.ENTITY || v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}	
	});*/
	
	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});
}

function selectAllEntity() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			v.className = 'ui-selected';
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});
	
	linkElMap.eachValue(function(v) {
		v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	});   	
}
function selectAllLink() {
	nodeElMap.eachValue(function(v) {
   		if (v.objType == USEROBJ_TYPE.ENTITY) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
   			v.className = 'ui-unselecting';
   		} else if (v.objType == USEROBJ_TYPE.SHAPE) {
   			if (v.figureType == SHAPE_TYPE.PICTURE) {
   				v.className = 'ui-unselecting';
   			} else if (v.figureType == SHAPE_TYPE.TEXT) {
   				v.style.borderWidth = '0';
   				v.style.padding = '3px';
   			} else {
   				v.strokecolor = linkStyle.color;
   			}
   		}
	});
	
	linkElMap.eachValue(function(v) {
		v.strokecolor = linkStyle.selectedColor;
	});   	
}

function disselectShapeNode() {
	if (allSelected) {
		disselectAllCell();
		return;
	}
	if (selectedCount > 0) {
		nodeElMap.eachValue(function(v) {
	   		if (v.objType == USEROBJ_TYPE.LINK) {
	   			v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	   		} else if (v.objType == USEROBJ_TYPE.SHAPE && v.figureType != SHAPE_TYPE.PICTURE) {
	   			if (v.figureType == SHAPE_TYPE.TEXT) {
	   				v.style.border = '0px';
	   				v.style.padding = '3px';
	   			} else {
	   				v.strokecolor = (v.oldColor ? v.oldColor : linkStyle.color);
	   			}
	   		} else {
	  			if (v.className.indexOf('ui-selected') != -1) {
	  				v.className = 'ui-unselecting';
				}
	   		}	
		});
	}
	if (linkSrcEntityId > 0) {
		var el = nodeElMap.get(linkSrcEntityId);
		if (el != null && el.className.indexOf('ui-selected') != -1) {
			el.className = 'ui-unselecting';
		}
	}
	if (focusedShapeId > 0) {
		var el = nodeElMap.get(focusedShapeId);
		if (el != null) {
			if (el.objType == USEROBJ_TYPE.SHAPE && el.figureType != SHAPE_TYPE.PICTURE) {
	   			if (el.figureType == SHAPE_TYPE.TEXT) {
	   				el.style.border = '0px';
	   				el.style.padding = '3px';
	   			} else {
	   				el.strokecolor = (el.oldColor ? el.oldColor : linkStyle.color);
	   			}
	   		} else {
	  			if (el.className.indexOf('ui-selected') != -1) {
	  				el.className = 'ui-unselecting';
				}
	   		}
		}
	}
	if (focusLinkId > 0) {
		var el = linkElMap.get(focusLinkId);
		if (el != null && el.strokecolor == linkStyle.selectedColor) {
			el.strokecolor = (el.oldColor ? el.oldColor : linkStyle.color);
		}
	}
	linkSrcEntityId = 0;
	focusedShapeId = 0;
	focusLinkId = 0;
   	selectedCount = 0;
   	selectCallbackFunc();
}

function getSelectedNodeId() {
    var entityIds = [];
	var linkIds = [];
	var folderIds = [];
    nodeElMap.eachValue(function(v) {
    	if (v.objType == USEROBJ_TYPE.ENTITY) {
 			if (v.className.indexOf('ui-selected') != -1) {
 				entityIds[entityIds.length] = v.entityId;
 			}
 		} else if (v.objType == USEROBJ_TYPE.FOLDER) {
 			if (v.className.indexOf('ui-selected') != -1) {
 				folderIds[folderIds.length] = v.entityId;
 			}
 		}
    });
    linkElMap.eachValue(function(v) {
		if (v.strokecolor == linkStyle.selectedColor) {
			linkIds[linkIds.length] = v.linkId;
		}
    });    
    var nodes = [entityIds, linkIds, folderIds];
    return nodes;
}
function getSelectedEntityId() {
    var entityIds = [];
    nodeElMap.eachValue(function(v) {
    	if (v.objType == USEROBJ_TYPE.ENTITY && v.className.indexOf('selected') != -1) {
    		entityIds[entityIds.length] = v.entityId;
    	}
    });
    return entityIds;
}
function getSelectedVertexId() {
    var vertexIds = [];
    nodeElMap.eachValue(function(v) {
    	if ((v.tagName == 'DIV' || v.tagName == 'div') && v.className.indexOf('selected') != -1) {
    		vertexIds[vertexIds.length] = v.entityId;
    	}
    });
    return vertexIds;
}

function textShapeMouseClicked() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
	shapeFocusedCallback();
	this.style.padding = "0px";
	this.style.border = 'solid 3px ' + SHAPE_STYLE.selectedColor;
	focusedShapeId = this.nodeId;
}
function shapeMouseClicked() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
	shapeFocusedCallback();
	if (event.ctrlKey) {
		if (this.strokecolor == SHAPE_STYLE.selectedColor) {
			this.strokecolor = SHAPE_STYLE.color;
		} else {
			this.strokecolor = SHAPE_STYLE.selectedColor;
		}
	} else {
		this.strokecolor = SHAPE_STYLE.selectedColor;
	}
	focusedShapeId = this.nodeId;
}
function shapeMouseReleased() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
	shapeMoving = false;
	movex = 0;
	movey = 0;
	shapeBluredCallback();
}
function lineShapeMouseClicked() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
	shapeFocusedCallback();
	if (event.ctrlKey) {
		if (this.strokecolor == SHAPE_STYLE.selectedColor) {
			this.strokecolor = SHAPE_STYLE.color;
		} else {
			this.strokecolor = SHAPE_STYLE.selectedColor;
		}
	} else {
		this.strokecolor = SHAPE_STYLE.selectedColor;
		if (event.button == MouseEvent.BUTTON1) {
			shapeMoving = true;
			var arr = ('' + this.from).split(',');
			var arr1 = ('' + this.to).split(',');
			movex1 = parseFloat(arr1[0].replace('pt', '')) - parseFloat(arr[0].replace('pt', ''));
			movey1 = parseFloat(arr1[1].replace('pt', '')) - parseFloat(arr[1].replace('pt', ''));				
			movex = event.x - ptToPx(parseFloat(arr[0].replace('pt', '')));
			movey = event.y - ptToPx(parseFloat(arr[1].replace('pt', '')));
			this.setCapture();
		}
	}
	focusedShapeId = this.nodeId;
}
function lineShapeMouseMoved() {
	if (shapeMoving) {
		var x = pxToPt(event.x - movex);
		var y = pxToPt(event.y - movey);
		this.from = x + 'pt,' + y + 'pt';
		this.to = (x + movex1) + 'pt,' + (y + movey1) + 'pt';
		shapeMoved = true;
	}
}
function defaultShapeMouseReleased() {
	if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
	shapeMoving = false;
	movex = 0;
	movey = 0;
	shapeBluredCallback();
	this.releaseCapture();
	if (shapeMoved) {
		shapeMoved = false;
		if (this.tagName == 'line') {
			var arr = pointToPt(this.from);
			shapeMovedCallback(this.nodeId, arr[0], arr[1]);
		} else {
			shapeMovedCallback(this.nodeId, this.style.left, this.style.top);
		}
	}	
}

/* Deprecated function */
function buildShapeNode1(type, x, y, text) {
	var shape = null;
	switch (type) {
		case SHAPE_TYPE.SOLID_LINE:
			shape = createVmlSolidLine(x, y);
			break;
		case SHAPE_TYPE.DASHED_LINE:
			shape = createVmlDashedLine(x, y);
			break;
		case SHAPE_TYPE.ARROW_LINE:
			shape = createVmlArrowLine(x, y);
			break;
		case SHAPE_TYPE.DARROW_LINE:
			shape = createVmlDoubleArrowLine(x, y);
			break;
		case SHAPE_TYPE.POLY_LINE:
			shape = createVmlPolyLine(x, y);
			break;
		case SHAPE_TYPE.RECT:
			shape = createVmlRect(x, y);
			break;			
		case SHAPE_TYPE.ROUNDRECT:
			shape = createVmlRoundRect(x, y);
			break;
		case SHAPE_TYPE.OVAL:
			shape = createVmlOval(x, y);
			break;
		case SHAPE_TYPE.TEXTRECT:
			shape = createVmlTextRect(x, y, text);
			break;
		default:
			return null;
	}

	if (type > SHAPE_TYPE.POLY_LINE) {
		shape.onmousedown = function() {
			if (curDrawingType > CURSOR_TYPE.SQUARE) {return;}
			shapeFocusedCallback();
			if (event.ctrlKey) {
				if (this.strokecolor == SHAPE_STYLE.selectedColor) {
					this.strokecolor = SHAPE_STYLE.color;
				} else {
					this.strokecolor = SHAPE_STYLE.selectedColor;
				}
			} else {
				this.strokecolor = SHAPE_STYLE.selectedColor;
				if (event.button == MouseEvent.BUTTON1) {
					shapeMoving = true;
					movex = event.x - parseInt(this.style.left);
					movey = event.y - parseInt(this.style.top);
					this.setCapture();					
				}
			}
			focusedShapeId = this.nodeId;
		};
		shape.onmousemove = function() {
			if (shapeMoving) {
				this.style.left = event.x - movex;
				this.style.top = event.y - movey;
				shapeMoved = true;
			}
		};
		if (type == SHAPE_TYPE.TEXTRECT) {
			shape.ondblclick = function() {
				if (curDrawingType <= CURSOR_TYPE.SQUARE || curDrawingType == SHAPE_TYPE.TEXTRECT) {
					setShapeNodeText(this.nodeId);
				}
			};
		}
	} else {
		shape.onmousedown = lineShapeMouseClicked;
		shape.onmousemove = lineShapeMouseMoved;
	}
	shape.onmouseup = defaultShapeMouseReleased;
	return shape;
}


function drawingShape(type) {
	shapex = event.offsetX;
	shapey = event.offsetY;
	offsetx = event.clientX;
	offsety = event.clientY;
	shapeNodeDrawing = true;
	document.onmousemove = moveShapeWhenDrawing;
	document.onmouseup = drawShapeCompleted;
}

function buildShapeNode(id, type, x, y, text) {
	var shape = null;
	switch (type) {
		case SHAPE_TYPE.SOLID_LINE:
			shape = createVmlSolidLine(id, x, y);
			break;
		case SHAPE_TYPE.DASHED_LINE:
			shape = createVmlDashedLine(id, x, y);
			break;
		case SHAPE_TYPE.ARROW_LINE:
			shape = createVmlArrowLine(id, x, y);
			break;
		case SHAPE_TYPE.DARROW_LINE:
			shape = createVmlDoubleArrowLine(id, x, y);
			break;
		case SHAPE_TYPE.POLY_LINE:
			shape = createVmlPolyLine(id, x, y);
			break;
		case SHAPE_TYPE.RECT:
			shape = createVmlRect(id, x, y);
			break;			
		case SHAPE_TYPE.ROUNDRECT:
			shape = createVmlRoundRect(id, x, y);
			break;
		case SHAPE_TYPE.OVAL:
			shape = createVmlOval(id, x, y);
			break;
		case SHAPE_TYPE.TEXTRECT:
			shape = createVmlTextRect(id, x, y, text);
			break;
		case SHAPE_TYPE.TEXT:
			shape = createTextBox(id, x, y, text);
			break;			
		default:
			return null;
	}

	// setup shape mouse linstener
	if (type > SHAPE_TYPE.POLY_LINE) {
		if (type == SHAPE_TYPE.TEXTRECT) {
			shape.ondblclick = function() {
				if (curDrawingType == CURSOR_TYPE.POINTER) {
					setShapeNodeText(this.nodeId);
				}
			};
		} else if (type == SHAPE_TYPE.TEXT) {
			shape.ondblclick = function() {
				if (curDrawingType == CURSOR_TYPE.POINTER) {
					setTextBoxText(this.nodeId);
				}
			};
		}
		if (type == SHAPE_TYPE.TEXT) {
			shape.onmousedown = textShapeMouseClicked;
		} else {
			shape.onmousedown = shapeMouseClicked;
		}
		shape.onmouseup = shapeMouseReleased;	
	} else {
		shape.onmousedown = lineShapeMouseClicked;
		shape.onmousemove = lineShapeMouseMoved;
		shape.onmouseup = defaultShapeMouseReleased;
	}
	return shape;
}

function moveShapeWhenDrawing() {
	if (shapeNodeDrawing) {
		curDrawingShape = buildShapeNode(shapeIdSequence++, curDrawingType, shapex, shapey);
		if (curDrawingShape != null) {
			shapeContainer.appendChild(curDrawingShape);
		}
		shapeNodeDrawing = false;
		return;
	}
	if (curDrawingShape == null) {
		return;
	}
	var vorm = curDrawingShape;
	if (vorm.tagName == 'line' || vorm.tagName == 'LINE') {
		vorm.to = (event.clientX + shapeContainer.scrollLeft) + "," + (event.clientY + shapeContainer.scrollTop - toolbarOffset);
	} else {
		var verschilX = event.x - offsetx;
		var verschilY = event.y - offsety;
		if(verschilX < 0){
			vorm.style.left = (shapex + verschilX) + 'px';
			vorm.style.width = -verschilX + 'px';
		} else {
			vorm.style.width = verschilX + 'px';
		}
		if(verschilY < 0){
			vorm.style.top = (shapey + verschilY) + 'px';
			vorm.style.height = -verschilY + 'px';
		} else {
			vorm.style.height = verschilY + 'px';
		}
	}
}
function drawShapeCompleted() {
	document.onmousemove = returnFalse;
	document.onmouseup = returnFalse;
	if (!shapeNodeDrawing && curDrawingShape != null) {
		if (event.button == MouseEvent.BUTTON2) {
			shapeContainer.removeChild(curDrawingShape);
		} else {
			drawingCallbackFunc();
		}
	}
	shapeNodeDrawing = true;
	curDrawingShape = null;
}
function drawLinkCompleted() {
	document.onmousemove = returnFalse;
	document.onmouseup = returnFalse;
	shapeNodeDrawing = true;
	shapeContainer.style.cursor = 'default';
	if (curDrawingShape != null) {
		shapeContainer.removeChild(curDrawingShape);
	}
}

// create vml shape
function createVmlPolyLine(id, x, y) {
	var line = document.createElement('v:PolyLine');
	line.id = 'vertex' + id;
	line.filled = "false"; 
	line.points = x + ',' + y;
	return line;
}
function createVmlSolidLine(id, x, y) {
	var line = document.createElement('v:line');
	line.id = 'vertex' + id;
	line.from = x + "," + y;
	line.to = x + "," + y;
	return line;
}
function createVmlDashedLine(id, x, y) {
	var line = document.createElement('v:line');
	line.id = 'vertex' + id;
	line.from = x + "," + y;
	line.to = x + "," + y;
	var stroke = document.createElement("v:stroke");
	stroke.dashstyle = 'LongDash';
	line.appendChild(stroke);
	return line;
}
function createVmlArrowLine(id, x, y) {
	var line = document.createElement('v:line');
	line.id = 'vertex' + id;
	line.from = x + "," + y;
	line.to = x + "," + y;
	var stroke = document.createElement("v:stroke");
	stroke.EndArrow = 'Classic';
	line.appendChild(stroke);
	return line;
}
function createVmlDoubleArrowLine(id, x, y) {
	var line = document.createElement('v:line');
	line.id = 'vertex' + id;
	line.from = x + "," + y;
	line.to = x + "," + y;
	var stroke = document.createElement("v:stroke");
	stroke.StartArrow = 'Classic';
	stroke.EndArrow = 'Classic';
	line.appendChild(stroke);
	return line;
}
function createVmlRect(id, x, y) {
	var rect = document.createElement('v:Rect');
	rect.id = 'vertex' + id;
	rect.style.position = 'absolute';
	rect.style.left = x;
	rect.style.top = y;
	rect.style.width = 1;
	rect.style.height = 1;
	rect.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
		fill.setAttribute('focus', '100%');
		rect.appendChild(fill);
	}
	return rect;
}
function createVmlRoundRect(id, x, y) {
	var rect = document.createElement('v:RoundRect');
	rect.id = 'vertex' + id;
	rect.style.position = 'absolute';
	rect.style.left = x;
	rect.style.top = y;
	rect.style.width = 1;
	rect.style.height = 1;
	rect.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
		fill.setAttribute('focus', '100%');
		rect.appendChild(fill);
	}	
	return rect;
}
function createVmlOval(id, x, y) {
	var oval = document.createElement('v:Oval');
	oval.id = 'vertex' + id;
	oval.style.position = 'absolute';
	oval.style.left = x;
	oval.style.top = y;
	oval.style.width = 1;
	oval.style.height = 1;
	oval.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
		fill.setAttribute('focus', '100%');
		oval.appendChild(fill);
	}	
	return oval;
}
function createVmlTextRect(id, x, y, text) {
	var rect = document.createElement('v:RoundRect');
	rect.id = 'vertex' + id;
	rect.style.position = 'absolute';
	rect.style.left = x;
	rect.style.top = y;
	rect.style.width = 1;
	rect.style.height = 1;
	rect.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	var shadow = document.createElement('v:shadow');
	shadow.on = 'T';
	shadow.type = 'single';
	shadow.color = SHAPE_STYLE.shadowColor;
	shadow.offset = SHAPE_STYLE.shadowOffset;
	var textBox = document.createElement('v:TextBox');
	textBox.id = 'textBox' + shapeIdSequence;
	textBox.inset = "5pt,5pt,5pt,5pt";
	textBox.style.fontSize = SHAPE_STYLE.rectTextSize + 'pt';
	textBox.style.fontWeight = 'bold';
	textBox.innerText = text == null  ? shapeLang.dbclickInputMsg : text;
	rect.appendChild(shadow);
	rect.appendChild(textBox);
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
		fill.setAttribute('focus', '100%');
		rect.appendChild(fill);
	}
	return rect;
}
function createTextBox(id, x, y, text) {
	var textBox = document.createElement('v:TextBox');
	textBox.id = 'vertex' + id;
	textBox.style.padding = "1px";
	textBox.style.zIndex = 9;
	textBox.style.position = 'absolute';
	textBox.style.left = x;
	textBox.style.top = y;
	textBox.style.padding = "3px";
	textBox.style.fontSize = SHAPE_STYLE.textSize + 'pt';
	textBox.style.fontWeight = 'bold';
	textBox.inset = "5pt,5pt,5pt,5pt";
	textBox.innerText = text ? text : shapeLang.dbclickInputMsg;
	return textBox;
}
function buildLine(id, x1, y1, x2, y2) {
	var line = document.createElement('v:line');
	line.id = id;
	line.from = x1 + "," + y1;
	line.to = x2 + "," + y2;
	return line;
}
