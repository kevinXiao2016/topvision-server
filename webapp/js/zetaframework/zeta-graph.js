/**
 * Zetaframework JavaScript Graph Drawing by OO, for IE.
 * Need the jquery framework: jquery.js, ui.graph.base.js
 *
 * (C) Zetaframework, 2008-04-08
 *
 * authro: niejun
 */
Zetaframework.graph = {};

var ZetaGraphI18N = {};

var cluetipShowDelaying= 100;
var cluetipHideDelaying= 0;
var cluetipCloseTimeout= 30000;
// click or hover
var cluetipShowType= 'hover';
// can be 'show' or 'slideDown' or 'fadeIn'
var cluetipEffectType = 'show';

var ZetaGraphConstants = {
	Firefox: false,
	DIRECTION_NORTH: 'north',
	DIRECTION_SOUTH: 'south',
	DIRECTION_EAST: 'east',
	DIRECTION_WEST: 'west',
	displayCluetip: true,
	oldDisplayCluetip: true
};

var LAYOUT_BUS = {
	rowHeight: 180,
	columnWidth: 130,
	entityOffsetLeft: 70,
	entityOffsetTop: 50,
	offsetTop: 40,
	offsetLeft: 40,
	offsetRight: 50,
	radius: 6
};

var SHAPE_STYLE = {
	textZIndex: 20,
	labelZIndex: 18,
	markerZIndex: 16,
	defaultZIndex: 12,
	edgeZIndex: 10,
	imageZIndex: 9,
	shapeZIndex: 8,
	groupZIndex: 5,

	imgOffset: 6,

	labelOffset: 12,
	linkWeight: 1,
	linkConnectorColor: '#0f0',
	linkConnectorWeight: '2pt',
	linkColor: '#000',
	linkSelectedColor: '#316ac5',
	linkShadow: false,
	linkShadowType: 'single',
	linkShadowColor: '#e0e0e0',
	linkDashStyle: 'ShortDash',

	textSize: 12,

	strokeColor: "#000",
	fillColor: "#c3d9ff",
	fillColor2: "#FFFFFF",

	shapeShadow: true,
	shadowColor: '#808080',
	shadowOffset: '4px,4px',
	gradient: true,

	shapeHelperOpacity: false,
	//shapeHelper: 'clone',	
	shapeHelper: function(ui) {
		var div = document.createElement('div');
		div.style.zIndex = 20;
		div.style.border = '1px dashed #000';
		div.style.width = this.clientWidth;
		div.style.height = this.clientHeight;
		return div;
	},

	//groupHelper: 'clone',
	groupHelper: function(ui) {
		var div = document.createElement('div');
		div.style.zIndex = 20;
		div.style.border = '1px dashed #000';
		div.style.width = this.clientWidth;
		div.style.height = this.clientHeight;
		return div;
	},	
	
	selectionHelper: {border: '1px solid #316AC5', background: '#D2E0F0', opacity: 0.5},

	helperOffset: 2,
	activeClass: false,
	//activeClass: 'ui-droppable-active',
	hoverClass: 'ui-droppable-hover'	
};

function getCluetipEnabled() {
	return ZetaGraphConstants.displayCluetip;
}

function setCluetipEnabled(enabled) {
	ZetaGraphConstants.displayCluetip = enabled;
}

/* Zeta Graph Class */
function ZetaGraph(container, w, h) {
	this.containerId = container.id;
	this.jContainerId = '#' + container.id;
	this.graphId = 'zetaGraph';
	this.jid = '#zetaGraph';
	this.init(container, w, h);
}

ZetaGraph.prototype.containerId = null;
ZetaGraph.prototype.jContainerId = null;
ZetaGraph.prototype.graphId = null;
ZetaGraph.prototype.jid = null;
ZetaGraph.prototype.model = null;
ZetaGraph.prototype.view = null;
ZetaGraph.prototype.bus = null;
ZetaGraph.prototype.selectionModel = null;
ZetaGraph.prototype.editable = true;
ZetaGraph.prototype.backgroundImage = null;
ZetaGraph.prototype.backgroundColor = null;
ZetaGraph.prototype.scrollForbidded = true;
ZetaGraph.prototype.mouseWheelForbidded = false;
ZetaGraph.prototype.drawing = false;
ZetaGraph.prototype.connector = null;
ZetaGraph.prototype.zoomSize = 1;
ZetaGraph.prototype.zooming = false;
ZetaGraph.prototype.zoomListener = null;
ZetaGraph.prototype.zoomHandler = null;
ZetaGraph.prototype.tempHeight = null;//为了制作打印预览有缩放功能，增加一个变量记录高度,修改：leexiang;

ZetaGraph.prototype.renaming = false;
ZetaGraph.prototype.renameVertex = null;
ZetaGraph.prototype.renameListener = null;
ZetaGraph.prototype.resizeVertex = null;
ZetaGraph.prototype.resizeBox = null;
ZetaGraph.prototype.resizeListener = null;

ZetaGraph.prototype.fragment = null;
ZetaGraph.prototype.edgeFragments = null;

/**
 * 拓扑图canvas初始化
 */
ZetaGraph.prototype.init = function(container, w, h) {
	//----view.cancas =  div---//
	var view = new ZetaGraphView(container, w, h);
	//vertexts,group,link etc. container
	this.model = new ZetaGraphModel(view);
	this.view = view;
	//selectionmal,centralize all event : down,up,left,ctrl etc..use event accreditation
	this.selectionModel = new ZetaGraphSelectionModel(this);
	//setup default event receiver
	this.setupDefaultListener();
};

ZetaGraph.prototype.setupDefaultListener = function() {
	var graph = this;
	this.addListener('keydown', function(evt) {
		if (graph.renaming) return true;
		var code = evt.keyCode;
	    if (evt.ctrlKey){
			if(code == KeyEvent.VK_A) {graph.getSelectionModel().selectAll();}
		} else {
		    if (code == KeyEvent.VK_DEL) {}
		    else if (code == KeyEvent.VK_UP) {graph.moveCellVertically(-1);}
			else if(code == KeyEvent.VK_DOWN) {graph.moveCellVertically(1);}
			else if (code == KeyEvent.VK_LEFT) {graph.moveCellHorizonally(-1);}
			else if (code == KeyEvent.VK_RIGHT) {graph.moveCellHorizonally(1);}
		}
		return !graph.scrollForbidded;
	});	
};

/**
 * 绘制总线型子网
 */
ZetaGraph.prototype.createSubnetBus = function(width, rows, columns) {
	var bus = document.createElement('v:PolyLine');
	bus.id = 'subnetBus';
	//bus.style.zIndex = SHAPE_STYLE.markerZIndex;
	bus.filled = "false";
	bus.strokeWeight = '1.2pt';
	bus.strokecolor = '#00ff00';
	
	var stroke = document.createElement('v:stroke');
	stroke.startarrow = 'oval';
	stroke.endarrow = 'oval';
	bus.appendChild(stroke);
	
	var rowHeight = 180;
	var columnWidth = 130;
	var entityOffsetLeft = 70;
	var entityOffsetTop = 50;
	var offsetTop = 40;
	var offsetLeft = 40;
	var offsetRight = 50;
	var radius = 6;
	
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
	this.view.canvas.appendChild(bus);
	this.bus = bus;
};

ZetaGraph.prototype.relayout = function(width, rows, columns) {
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
	this.bus.points.value = buffer.toString();
};

ZetaGraph.prototype.destroy = function() {
};

ZetaGraph.prototype.clear = function() {
	this.view.invalidate();
	this.model.vertexs.clear();
	this.model.edges.clear();	
};

ZetaGraph.prototype.getModel = function() {
	return this.model;
};

ZetaGraph.prototype.getView = function() {
	return this.view;
};

ZetaGraph.prototype.getSelectionModel = function() {
	return this.selectionModel;
};

ZetaGraph.prototype.setScrollForbidded = function(forbidded) {
	this.scrollForbidded = forbidded;
};

ZetaGraph.prototype.setMouseWheelForbidded = function(forbidded) {
	this.mouseWheelForbidded = forbidded;
};

/* fn(evt,vertex,edge) */
ZetaGraph.prototype.addPopupMenu = function(fn) {
	var model = this.model;
	var graphId = this.graphId;
	this.addListener('contextmenu', function(evt) {
		evt.preventDefault();
		var v = null;
		var e = null;
		var tag = evt.target.tagName;
		if (tag == 'DIV' && evt.target.children[0].tagName != 'Oval') {
			v = e = null;
		} else {
			var src = evt.target;
			while (src.parentNode != null && src.parentNode.id != graphId) {
				src = src.parentNode;
			}
			v = model.getVertex(src.id);
			if (v == null) {
				e = model.getEdge(src.id);
			}
		}
		fn(evt, v, e);
	});
};

ZetaGraph.prototype.moveCellVertically = function(increment) {
	var y = 0;
   	var selectee = null;
   	var selectees = this.selectionModel.getSelectedVertexs();
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		y = selectee.getY() + increment;
   		selectee.setY(y);
   	}
};

ZetaGraph.prototype.moveCellHorizonally = function(increment) {
	var x = 0;
	var selectee = null;
   	var selectees = this.selectionModel.getSelectedVertexs();
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		x = selectee.getX() + increment;
   		selectee.setX(x);
   	}
};

ZetaGraph.prototype.setLeftAlign = function() {
   	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var x = selectees[0].getX();
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getX();
		if (t < x) {
			x = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setX(x);
   	}
};

ZetaGraph.prototype.setCenterAlign = function() {
   	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var x = selectees[0].getCenter()[0];
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getCenter()[0];
		if (t < x) {
			x = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setX(selectee.getX() - selectee.getCenter()[0] + x);
   	}	
};

ZetaGraph.prototype.setRightAlign = function() {
	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var x = selectees[0].getX();
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getX();
		if (t > x) {
			x = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setX(x);
   	}
};

ZetaGraph.prototype.setMiddleAlign = function() {
   	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var x = selectees[0].getCenter()[1];
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getCenter()[1];
		if (t < x) {
			x = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setY(selectee.getY() - selectee.getCenter()[1] + x);
   	}
};

ZetaGraph.prototype.setTopAlign = function() {
	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var y = selectees[0].getY();
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getY();
		if (t < y) {
			y = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setY(y);
   	}
};

ZetaGraph.prototype.setBottomAlign = function() {
	var selectees = this.selectionModel.getSelectedVertexs();
   	if (selectees.length < 2) {return;}
   	var y = selectees[0].getY();
	for (var i = 1; i < selectees.length; i++) {
		var t = selectees[i].getY();
		if (t > y) {
			y = t;
		}
	}
	var selectee = null;
   	for (var i = 0; i < selectees.length; i++) {
   		selectee = selectees[i];
   		selectee.setY(y);
   	}
};

ZetaGraph.prototype.insertVertex = function(cell) {
    cell.setGraph(this);
    this.model.putVertex(cell);
	this.addDraggable(cell);
	this.view.insertDOMElement(cell.ui);
};

ZetaGraph.prototype.insertEdge = function(edge) {
	edge.firePositionChanged();
	this.model.putEdge(edge);
	this.view.insertDOMElement(edge.ui);
};

ZetaGraph.prototype.addDraggable = function(cell) {
	var selectionModel = this.selectionModel;
	var vertex = cell;
	var handler = false;
	var opacity = SHAPE_STYLE.shapeHelperOpacity;
	var helper = SHAPE_STYLE.shapeHelper;
	if (vertex.ui.tagName == 'DIV' && !vertex.groupable) {
		handler = 'img,span';
		opacity = 0.5;
		helper = 'clone';
	}
	$(vertex.ui).draggable({
		handle: handler,
		scroll: true,
		opacity: opacity,
		helper: helper,
		stop: function(e, ui) {
			ZetaGraphConstants.displayCluetip = ZetaGraphConstants.oldDisplayCluetip;
			this.className = 'ui-selected';
			vertex.setXY(ui.position.left, ui.position.top);
			if (vertex.groupable || this.tagName != 'DIV') {
				if (selectionModel.isEnabled()) {
						selectionModel.enable();
				}
			}
		},
		start: function(e, ui) {
			ZetaGraphConstants.oldDisplayCluetip = ZetaGraphConstants.displayCluetip;
			ZetaGraphConstants.displayCluetip = false;
			if (vertex.groupable || this.tagName != 'DIV') {
				if (selectionModel.isEnabled()) {
					selectionModel.disable();
				}
			} else {
				ui.helper.css("border", "0");
			}
		}
	});
	if (vertex.userObject.fixed) {
		$(vertex.ui).draggable('disable');
	}
};

ZetaGraph.prototype.addVertex = function(cell) {
	this.fragment.appendChild(vertex.getUI());
	this.addDraggable(cell);
};

ZetaGraph.prototype.addEdge = function(edge) {
	this.fragment.appendChild(vertex.getUI());
};

//创建文档碎片
ZetaGraph.prototype.beginUpdate = function() {
	if (this.fragment == null) {
		this.fragment = document.createDocumentFragment();
		this.edgeFragments = document.createDocumentFragment();
	}
};

//完成碎片的工作，并清空碎片
ZetaGraph.prototype.endUpdateVertexs = function() {
	if (this.fragment != null) {
		this.view.canvas.appendChild(this.fragment);
		this.fragment = null;
	}
};
ZetaGraph.prototype.endUpdateEdges = function() {
	if (this.edgeFragments != null) {
		this.view.canvas.appendChild(this.edgeFragments);
		this.edgeFragments = null;
	}
};
ZetaGraph.prototype.endUpdate = function() {
	if (this.fragment != null) {
		this.view.canvas.appendChild(this.fragment);
		this.fragment = null;
	}
	if (this.edgeFragments != null) {
		this.view.canvas.appendChild(this.edgeFragments);
		this.edgeFragments = null;
	}
};

ZetaGraph.prototype.insertMarker = function(marker) {
	this.view.canvas.appendChild(marker.getComponent());
};

ZetaGraph.prototype.addDraggedListener = function(listener) {
};

ZetaGraph.prototype.removeDraggedListener = function(listener) {
};

ZetaGraph.prototype.addGraphListener = function(listener) {
};

ZetaGraph.prototype.removeGraphListener = function(listener) {
};

ZetaGraph.prototype.setAutoZoom = function(auto) {
	if (auto) {
		this.mouseWheelForbidded = true;
		var graph = this;
		this.zoomHandler = function(evt) {
			//mark by @bravin: evt是jQuery包装事件，无法直接获取到 wheelDelta值
			var z = 100 * graph.zoomSize + evt.originalEvent.wheelDelta/12.0;
			z =  Math.round(z)/100.0;
			if (z < 0.1) z = 0.1;
			if (z > 5) z = 5;
			graph.zoom(z);
			return !graph.mouseWheelForbidded;
		};
		$('#graphContainer').bind('mousewheel', this.zoomHandler);
	} else {
		if (this.zoomHandler != null) {
			$('#graphContainer').unbind('mousewheel', this.zoomHandler);
		}
		this.zoomHandler = null;
		this.mouseWheelForbidded = false;
	}
};

ZetaGraph.prototype.addRenameListener = function(listener) {
	this.renameListener = listener;
};

ZetaGraph.prototype.addZoomListener = function(listener) {
	this.zoomListener = listener;
};

ZetaGraph.prototype.removeZoomListener = function(listener) {
	this.zoomListener = null;
};

ZetaGraph.prototype.addDropTargetListener = function(fn, scope) {
	$(this.jContainerId).droppable({
		scope: scope, 
		drop: fn
	});
};

ZetaGraph.prototype.addListener = function(name, listener) {
	$(this.jContainerId).bind(name, listener);
};

ZetaGraph.prototype.removeListener = function(name, listener) {
	$(this.jContainerId).unbind(name, listener);
};

ZetaGraph.prototype.getGraphId = function() {
	return this.graphId;
};

ZetaGraph.prototype.removeVertexById = function(id) {
	var v = this.model.removeVertexById(id);
	if (v != null) {
		var edges = v.getEdges();
		for (var i = 0; i < edges.length; i++) {
			this.removeEdgeById(edges[i].cellId);
		}
		this.view.canvas.removeChild(v.ui);
		if (v.marker != null) {
			this.view.canvas.removeChild(v.marker.getComponent());
		}
		if (v.iconMarker != null) {
			this.view.canvas.removeChild(v.iconMarker.getComponent());
		}
	}
};

ZetaGraph.prototype.removeEdgeById = function(id) {
	var e = this.model.removeEdgeById(id);
	if (e !=  null) {
		this.view.canvas.removeChild(e.ui);
	}
};

ZetaGraph.prototype.removeVertex = function(vertex) {
	this.removeVertexById(vertex.cellId);
};

ZetaGraph.prototype.removeEdge = function(edge) {
	this.removeEdgeById(edge.cellId);
};

ZetaGraph.prototype.setWidth = function(w) {
	//this.view.container.style.width = w
	this.view.canvas.style.width = w + "px"
};

ZetaGraph.prototype.setHeight = function(h) {
	//this.view.container.style.height = h
	this.view.canvas.style.height = h + "px"
};

ZetaGraph.prototype.getWidth = function() {
	var container = this.view.canvas;
	var w = container.scrollWidth;
	if (w < container.offsetWidth) {
		w = container.offsetWidth;
	}
	return w;
};

ZetaGraph.prototype.getHeight = function() {
	var container = this.view.canvas;
	var h = container.scrollHeight;
	if (h < container.offsetHeight) {
		h = container.offsetHeight;
	}
	return h;
};

ZetaGraph.prototype.getScrollLeft = function() {
	var container = this.view.container;
	return container.scrollLeft;
};

ZetaGraph.prototype.getScrollTop = function() {
	var container = this.view.container;
	return container.scrollTop;
};

ZetaGraph.prototype.setScrollLeft = function(left) {
	var container = this.view.container;
	container.scrollLeft = left;
};

ZetaGraph.prototype.setScrollTop = function(top) {
	var container = this.view.container;
	container.scrollTop = top;
};

ZetaGraph.prototype.setCursor = function(curosr) {
	this.view.canvas.style.cursor = curosr;
};

ZetaGraph.prototype.setEditable = function(able) {
	this.editable = able;
	if (able) {
		this.selectionModel.enable();
		this.model.eachVertex(function(v) {
			$(v.jid).draggable('enable');
		});		
	} else {
		this.selectionModel.disable();
		this.model.eachVertex(function(v) {
			$(v.jid).draggable('disable');
		});		
	}
};

ZetaGraph.prototype.isEditable = function(able) {
	return this.editable;
};

ZetaGraph.prototype.startDrawConnector = function(source) {
	if (this.connector == null) {
		var point = source.getCenter();
		var connector = document.createElement('v:Line');
		connector.style.zIndex = SHAPE_STYLE.edgeZIndex;
		connector.style.position = 'absolute';
		connector.from = connector.to = point[0] + ',' + point[1];
		connector.strokecolor = SHAPE_STYLE.linkConnectorColor;
		connector.strokeweight = SHAPE_STYLE.linkConnectorWeight;
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = 'Dash';
		connector.appendChild(stroke);

		this.view.canvas.appendChild(connector);
		this.connector = connector;
		this.drawing = true;
	}
};

ZetaGraph.prototype.moveConnector = function(x, y) {
	if (this.connector != null) {
		this.connector.to = x + ',' + y;
	}
};

ZetaGraph.prototype.stopDrawConnector = function() {
	if (this.connector != null) {
		this.view.canvas.removeChild(this.connector);
	}
	this.connector = null;
};

ZetaGraph.prototype.getZoomSize = function() {
	return this.zoomSize;
};

/**
 * 按比例缩放拓扑全图
 */
ZetaGraph.prototype.zoom = function(size) {
	if (this.zooming || this.zoomSize == size)
		return;
	this.zooming = true;
	//----style.zoom:ie5.5+专用属性，缩放---//
	this.view.canvas.style.zoom = size;
	//----由于div#resizeMe不属于canvas画布，故缩放对其不启用
    //添加支持画板缩放的时候拖放坐标计算错误的问题  hujiangyi
    //设置ui.graph.base.js的缩放比例
    $.ui.mouse._zoomSize = size;
    this.zoomSize = size;
    if (this.zoomListener != null)
		this.zoomListener(size);
   	this.zooming = false;
};

ZetaGraph.prototype.setBackgroundColor = function(color) {
	this.view.container.style.backgroundImage = '';
	this.view.container.style.backgroundColor = color;
};

ZetaGraph.prototype.setBackgroundImage = function(img) {
	this.view.container.style.backgroundImage = 'url(' + img + ')';
};

ZetaGraph.prototype.setBackgroundPosition = function(position) {
	this.view.container.style.backgroundPosition = position;
};

ZetaGraph.prototype.setBackgroundRepeat = function(repeat) {
	this.view.container.style.backgroundRepeat = repeat;
};

ZetaGraph.prototype.setConnector = function(edge, renderer) {
	this.view.canvas.removeChild(edge.ui);
	edge.setRenderer(renderer);
	this.view.canvas.appendChild(edge.ui);
};

ZetaGraph.prototype.addResizeListener = function(listener) {
	this.resizeListener = listener;
};

/**
 * 组调整大小框
 */
ZetaGraph.prototype.setResizable = function(able) {
	var div = document.createElement('div');
	div.id = 'resizeMe';
	div.style.display = 'none';
	
	var se = document.createElement('div');
	se.id = 'resizeSE';
	
	var e = document.createElement('div');
	e.id = 'resizeE';
	
	var ne = document.createElement('div');
	ne.id = 'resizeNE';
	
	var n = document.createElement('div');
	n.id = 'resizeN';
	
	var nw = document.createElement('div');
	nw.id = 'resizeNW';
	
	var w = document.createElement('div');
	w.id = 'resizeW';
	
	var sw = document.createElement('div');
	sw.id = 'resizeSW';
	
	var s = document.createElement('div');
	s.id = 'resizeS';
	
	div.appendChild(se);
	div.appendChild(e);
	div.appendChild(ne);
	div.appendChild(n);
	div.appendChild(nw);
	div.appendChild(w);
	div.appendChild(sw);
	div.appendChild(s);
	
	this.view.canvas.appendChild(div);
	this.resizeBox = div;

	var graph = this;
	$(div).Resizable({
		minWidth: 16,
		minHeight: 16,
		dragHandle: true,
		handlers: {
			se: '#resizeSE',
			e: '#resizeE',
			ne: '#resizeNE',
			n: '#resizeN',
			nw: '#resizeNW',
			w: '#resizeW',
			sw: '#resizeSW',
			s: '#resizeS'
		},
		onDrag: function(x,y) {
			var selectee = graph.resizeVertex;
			if (selectee != null) {
				selectee.setXY(x,y);
			}
		}, 
		onResize: function(size, position) {
			var selectee = graph.resizeVertex;
			selectee.setWidth(this.offsetWidth);
			selectee.setHeight(this.offsetHeight);
			//selectee.setXY(($(this.parentNode).offset().left+position.left)/zoomSize,($(this.parentNode).offset().top+position.top)/zoomSize);
			selectee.setXY(position.left,position.top);
		},
		onStop: function(direction) {
			if (graph.resizeListener != null) {
				graph.resizeListener(graph.resizeVertex);
			}
		}
	});
};

/**
 * group调整大小初始化框
 */
ZetaGraph.prototype.startResizing = function(selectee) {
	this.resizeVertex = selectee;
	var r = this.resizeBox;
	r.style.left = selectee.getX() + "px";
	r.style.top = selectee.getY() + "px";
	r.style.width = selectee.getWidth();
	r.style.height = selectee.getHeight();
	r.style.display = '';
};

ZetaGraph.prototype.stopResizing = function() {
	if (this.resizeVertex != null) {
		var r = this.resizeBox;
		r.style.display = 'none';
		r.style.left = 0 + "px";
		r.style.top = 0 + "px";
		r.style.width = 16;
		r.style.height = 16;		
		this.resizeVertex = null;
	}
};

ZetaGraph.prototype.startRenaming = function(vertex) {
	if (this.renaming) return;
	this.renaming = true;
	this.renameVertex = vertex;
	var span = vertex.renderer.text;
	var box = this.view.renameBox;
	var w = span.clientWidth;
	var h = span.clientHeight;
	box.style.width = (w < 10) ? 60 : w;
	box.style.height = (h < 10) ? 20 : h;
	box.value = span.innerText;
	span.swapNode(box);
	var r = box.createTextRange();
	r.select();
	r.moveStart('character', box.value.length);
	r.collapse(true);
};

ZetaGraph.prototype.stopRenaming = function() {
	if (this.renaming) {
		this.renaming = false;
		var box = this.view.renameBox;
		var span = this.renameVertex.renderer.text;
		var v = box.value.trim();
		var flag = false;
		if (v != null && v != "" & v != span.innerText) {
			span.innerText = v;
			flag = true;	
		}
		box.swapNode(span);
		if (flag && this.renameListener != null) {
			this.renameListener(this.renameVertex, v);
		}
		this.renameVertex = null;
	}
};

ZetaGraph.prototype.print = function(title, css) {
	var wnd = window.open();
	this.showPrintWnd(wnd.document, title, css);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(ZetaGraphConstants.Firefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
};

ZetaGraph.prototype.printview = function(title, css) {
	this.showPrintWnd(null, title, css);
};

ZetaGraph.prototype.showPrintWnd = function(doc, title, css) {
	if(doc == null) {
		var wnd = window.open();
		doc = wnd.document;
	} else {
		doc.open();
	}
	doc.write('<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn chemas-microsoft-com:vml" xml:lang="en" lang="en">');
	doc.write('<head>');
	doc.write('<style>v\\:* {behavior:url(#default#VML);}</style>');
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
	if (title) {
		doc.write('<title>');
		doc.write(title);
		doc.write('</title>');
	}
	if (css) {
		for (var i = 0; i < css.length; i++) {
			doc.write('<link rel="stylesheet" type="text/css" href="');
			doc.write(css[i]);
			doc.write('"/>');
		}
	} else {
		doc.write('<link rel="stylesheet" type="text/css" href="../css/zeta-graph.css"/>');
	}
	doc.write('</head>');
	doc.write('<body id=zetaGraph style="padding:0px;margin:0px;overflow:auto;');
	/**/
	if (displayGrid) {
        doc.write('background:' + backgroundColor + ';background-image:url(\'' + backgroundGrid + '\')');
        doc.write(';background-repeat: repeat');
    } else {
		if (backgroundFlag) {
			doc.write('background:' + backgroundColor + ';background-image:url(\'' + backgroundImg + '\')');
			if (backgroundPosition == 0) {
				doc.write(';background-repeat: no-repeat');
			} else if (backgroundPosition == 1) {
				doc.write(';background-position: center center;background-repeat: no-repeat');
			} else if (backgroundPosition == 2) {
				doc.write(';background-repeat: repeat');
			}
        } else {
		    doc.write('background:' + backgroundColor);
		}
	}

//    if (backgroundFlag) {
//		if (displayGrid) {
//			doc.write('background:' + backgroundGrid);
//		} else {
//			doc.write('background:' + backgroundColor + ';background-image:url(\'' + backgroundImg + '\')');
//			if (backgroundPosition == 0) {
//				doc.write(';background-repeat: no-repeat');
//			} else if (backgroundPosition == 1) {
//				doc.write(';background-repeat: center 0 no-repeat');
//			} else if (backgroundPosition == 2) {
//				doc.write(';background-repeat: repeat');
//			}
//		}
//	} else {
//		doc.write('background:' + backgroundColor);
//	}	
	doc.write('">');
	if(this.tempHeight == null){this.tempHeight = "100%"}
	doc.write('<div id=zetaGraph style="zoom:'+ this.zoomSize +'; height:'+ this.tempHeight +';">');
	doc.write(this.view.canvas.innerHTML);
	doc.write('</div>');
	//alert();
	//alert(graph.getView().ui.innerHTML);
	//doc.write('<script></script>');
	doc.write('</body>');
	doc.write('</html>');
	doc.close();
};

/* micro graph */
//function ZetaMicroGraph(container, graph, x, y) {
//	this.container = container;
//	this.graph = graph;
//	this.x = x;
//	this.y = y;
//	this.init();
//}
//ZetaMicroGraph.prototype.graph = null;
//ZetaMicroGraph.prototype.x = null;
//ZetaMicroGraph.prototype.y = null;
//ZetaMicroGraph.prototype.width = 276;
//ZetaMicroGraph.prototype.height = 178;
//ZetaMicroGraph.prototype.container = null;
//ZetaMicroGraph.prototype.microGraph = null;
//ZetaMicroGraph.prototype.square = null;
//ZetaMicroGraph.prototype.microGraphBt = null;
//ZetaMicroGraph.prototype.visible = false;
//ZetaMicroGraph.prototype.scrollFlag = false;
//ZetaMicroGraph.prototype.ui = null;
//ZetaMicroGraph.prototype.text = null;
//
//ZetaMicroGraph.prototype.buildDefaultContainer = function() {
//	var div = document.createElement('div');
//	div.className = 'microContainer';
//	div.style.top = this.y;
//
//	var div1 = document.createElement('div');
//	div1.className = 'microGraph';
//
//	var div2 = document.createElement('div');
//	div2.className = 'microSquare';
//	div2.appendChild(document.createElement('div'));
//
//	div1.appendChild(div2);
//	div.appendChild(div1);
//	document.body.appendChild(div);
//
//	this.container = div;
//	this.microGraph = div1;
//	this.square = div2;
//};
//
//ZetaMicroGraph.prototype.init = function() {
//	if (this.container == null) {
//		this.buildDefaultContainer();
//	} else {
////		var div1 = document.getElementById('microGraph-div');
////		var div2 = document.getElementById('microSquare-div');
////		this.microGraph = div1;
////		this.square = div2;
//	}
//
////	var div3 = document.createElement('div');
////	div3.className = 'microGraphBt';
////	div3.style.top = this.y;
////	div3.title = ZetaGraphI18N.showMicroGraph;
////	var self = this;
////	div3.onclick = function() {
////		self.setVisible(!self.visible);
////	};
////	document.body.appendChild(div3);
////
////	this.microGraphBt = div3;
////
////	this.microGraph.style.width = this.width;
////	this.microGraph.style.height = this.height;
////
////	var self = this;
////	var mg = this.microGraph;
//	$(this.square).draggable({opacity: 0.50, helper: 'clone',
//		containment: 'parent', iframeFix: true,
//		stop: function(e, ui) {
//			self.scrollFlag = false;
//			this.style.left = ui.position.left;
//			this.style.top = ui.position.top;
//			if (ui.position.left <= 5) {
//				graph.getView().setScrollLeft(0);
//			} else {
////				graph.getView().setScrollLeft(
////					Math.round(graph.getWidth() * ui.position.left / mg.clientWidth));
//				graph.getView().setScrollLeft(0);
//			}
//			if (ui.position.top <= 5) {
//				graph.getView().setScrollTop(0);
//			} else {
////				Math.round(graph.getView().setScrollTop(graph.getHeight() * ui.position.top / mg.clientHeight));
//				graph.getView().setScrollTop(0);
//			}
//		}
//	});
//
////	this.graph.getModel().addModelListener(function(evt) {
////		if (self.isVisible()) {
////			self.microGraph.style.width = 276;
////			self.microGraph.style.height = 182;
////	        var h = 182;
////	        var w = 276;
////	        var mapWidth = self.graph.getWidth();
////	        var mapHeight = self.graph.getHeight();
////	        var ratioh = 1.0 * h / mapHeight;
////	        var ratiow = 1.0 * w / mapWidth;
////	        ratio = ratioh;
////	        if (ratiow < ratio) {
////	        	ratio = ratiow;
////	        }
////			//alert(ratio);
////			zoomFrame.zetaGraph.style.zoom = ratio;
////			//alert(self.graph.view.ui.innerHTML);
////			zoomFrame.zetaGraph.innerHTML = self.graph.view.canvas.innerHTML;
////		}
////	});
//
//	/*this.graph.addListener('scroll', function(evt) {
//		if (self.scrollFlag && self.isVisible()) {
//			var obj = evt.target;
//			var l = obj.scrollWidth - obj.clientWidth - 18;
//			var x = 5, y = 5;
//			if (l > 0) {
//				x = parseInt(5 * obj.scrollLeft / l);
//				if (x < 5) {
//					x = 5;
//				}
//				if (x > 5) {
//					x = 5;
//				}
//			}
//			l = obj.scrollHeight - obj.clientHeight - 18;
//			if (l > 0) {
//				y = parseInt(5 * obj.scrollTop / l);
//				if (y > 5) {
//					y = 5;
//				}
//				if (y < 5) {
//					y = 5;
//				}
//			}
//			self.square.style.left = x;
//			self.square.style.top = y;
//		}
//		self.scrollFlag = true;
//	});*/
//};
//
//ZetaMicroGraph.prototype.setVisible = function(v) {
//	this.visible = v;
//	var offsetTop = this.y;
//	if (v) {
//		this.microGraph.style.width = 276;
//		this.microGraph.style.height = 182;
//        var h = 182;
//        var w = 276;
//        var mapWidth = this.graph.getWidth();
//        var mapHeight = this.graph.getHeight();
//        var ratioh = 1.0 * h / mapHeight;
//        var ratiow = 1.0 * w / mapWidth;
//        ratio = ratioh;
//        if (ratiow < ratio) {
//        	ratio = ratiow;
//        }
//		zoomFrame.zetaGraph.style.zoom = ratio;
//		zoomFrame.zetaGraph.innerHTML = this.graph.view.canvas.innerHTML;
//
//		this.container.style.left = 0;
//		this.container.style.top = offsetTop;
//		this.container.style.display = 'block';
//
//		this.microGraphBt.className = 'microGraphBt1';
//		this.microGraphBt.style.left = this.container.clientWidth - 16;
//		this.microGraphBt.style.top = offsetTop + this.container.clientHeight - 16;
//		this.microGraphBt.title = ZetaGraphI18N.hideMicroGraph;
//	} else {
//		this.container.style.display = 'none';
//		this.microGraphBt.className = 'microGraphBt';
//		this.microGraphBt.style.left = 0;
//		this.microGraphBt.style.top = offsetTop;
//		this.microGraphBt.title = ZetaGraphI18N.showMicroGraph;
//	}
//};
//
//ZetaMicroGraph.prototype.update = function() {
//	zoomFrame.zetaGraph.innerHTML = this.graph.view.canvas.innerHTML;
//};
//
//ZetaMicroGraph.prototype.setWidth = function(w) {
//	this.microGraph.style.width = this.width = w;
//};
//
//ZetaMicroGraph.prototype.setHeight = function(h) {
//	this.microGraph.style.height = this.height = h;
//};
//
//ZetaMicroGraph.prototype.setBackgroundImage = function(img) {
//	this.microGraph.style.backgroundImage = img;
//};
//ZetaMicroGraph.prototype.isVisible = function() {
//	return this.visible;
//};

/* graph model */
function ZetaGraphModel() {
	this.vertexs = new ZetaHash();
	this.edges = new ZetaHash();
}
ZetaGraphModel.prototype.vertexs = null;
ZetaGraphModel.prototype.edges = null;
ZetaGraphModel.prototype.modelListener = null;

ZetaGraphModel.prototype.putVertex = function(cell) {
	this.vertexs.put(cell.cellId, cell);	
};

ZetaGraphModel.prototype.putEdge = function(edge) {
	this.edges.put(edge.cellId, edge);
};

ZetaGraphModel.prototype.insertVertex = function(vertex) {
	this.putVertex(vertex);
};

ZetaGraphModel.prototype.insertEdge = function(edge) {
	this.putEdge(edge);
};

ZetaGraphModel.prototype.removeVertexById = function(id) {
	var v = this.vertexs.get(id);
	if (v != null) {
		this.vertexs.remove(id);
	}
	return v;
};

ZetaGraphModel.prototype.removeEdgeById = function(id) {
	var e = this.edges.get(id);
	if (e != null) {
		this.edges.remove(id);
	}
	return e;
};

ZetaGraphModel.prototype.size = function() {
	return (this.vertexs.size() + this.edges.size());
};

ZetaGraphModel.prototype.getVertexCount = function() {
	return this.vertexs.size();
};

ZetaGraphModel.prototype.getEdgeCount = function() {
	return this.edges.size();
};

ZetaGraphModel.prototype.getVertex = function(id) {
	return this.vertexs.get(id);
};

ZetaGraphModel.prototype.getEdge = function(id) {
	return this.edges.get(id);
};

ZetaGraphModel.prototype.getCell = function(id) {
	var cell = this.vertexs.get(id);
	if (cell ==  null) {
		cell = this.edges.get(id);
	}
	return cell;
};

ZetaGraphModel.prototype.eachVertex = function(fn) {
	this.vertexs.eachValue(fn);
};

ZetaGraphModel.prototype.eachEdge = function(fn) {
	this.edges.eachValue(fn);
};

ZetaGraphModel.prototype.eachCell = function(fn) {
	this.eachVertex(fn);
	this.eachEdge(fn);
};

/**
 * 用于小地图,目前未支持
 */
ZetaGraphModel.prototype.addModelListener = function(listener) {
	this.modelListener = listener;
};
ZetaGraphModel.prototype.removeModelListener = function(listener) {
	this.modelListener = null;
};

/* graph view */
function ZetaGraphView(container, w, h) {
	if (typeof w == 'undefined') {
		w = 1024;
		h = 768;
	}
	this.container = container;
	
	var div = document.createElement('div');
	div.id = 'zetaGraph';
	div.style.width = w;
	div.style.height = h;
	
	var textarea = document.createElement('textarea');
	textarea.className = 'renameBox';

	this.renameBox = textarea;
	this.canvas = div;
	
	container.appendChild(div);
}

ZetaGraphView.prototype.graph = null;
ZetaGraphView.prototype.canvas = null;
ZetaGraphView.prototype.container = null;
ZetaGraphView.prototype.renameBox = null;

ZetaGraphView.prototype.insertDOMElement = function(element) {
	this.canvas.appendChild(element);
};

ZetaGraphView.prototype.invalidate = function() {
    $(this.canvas).empty();
};

ZetaGraphView.prototype.getCanvas = function() {
	return this.canvas;
};

ZetaGraphView.prototype.setWidth = function(width) {
	this.canvas.style.width = width + "px";
};

ZetaGraphView.prototype.setHeight = function(h) {
	this.canvas.style.height = h + "px";
};

ZetaGraphView.prototype.getScrollLeft = function() {
	return this.canvas.scrollLeft;
};

ZetaGraphView.prototype.getScrollTop = function() {
	return this.canvas.scrollTop;
};

ZetaGraphView.prototype.setScrollLeft = function(left) {
	this.canvas.scrollLeft = left;
};

ZetaGraphView.prototype.setScrollTop = function(top) {
	this.canvas.scrollTop = top;
};


/* graph selection model */
function ZetaGraphSelectionModel(graph) {
	this.graph = graph;
	$(graph.jContainerId).bind('selectstart', function() {
		return graph.renaming;
	});
	$(graph.jid).selectable({
		filter: '*[isCell]',//是否含有属性isCell
		cancel: 'img,span,br',
		distance: 5	,
		selecting: function(evt, ui) {
			if (ui.selecting.isEdge) {
				ui.selecting.setAttribute('strokecolor', SHAPE_STYLE.linkSelectedColor);
			}
		},
		unselecting: function(evt, ui) {
			if (ui.unselecting.isEdge) {
				ui.unselecting.setAttribute('strokecolor', ui.unselecting.oldcolor);
			}
		}
	});
	this.setupDefaultListener();
}

ZetaGraphSelectionModel.prototype.graph = null;
ZetaGraphSelectionModel.prototype.selectedId = null;
ZetaGraphSelectionModel.prototype.selectionListener = null;
ZetaGraphSelectionModel.prototype.enabled = true;

ZetaGraphSelectionModel.prototype.isEnabled = function() {
	return this.enabled;
};

ZetaGraphSelectionModel.prototype.setEnabled = function(enabled) {
	this.enabled = enabled;
};

ZetaGraphSelectionModel.prototype.enable = function() {
	$(this.graph.jid).selectable('enable');
};

ZetaGraphSelectionModel.prototype.disable = function() {
	$(this.graph.jid).selectable('disable');
};

ZetaGraphSelectionModel.prototype.setSquareHelper = function(helper) {
	$(this.graph.jid).selectable("setHelper", helper);
};

ZetaGraphSelectionModel.prototype.goToVertex = function(id) {
	var v = this.graph.model.getVertex(id);
	if (v != null) {
		//this.graph.view.container.scrollLeft = v.ui.offsetLeft>500?v.ui.offsetLeft-300:0;
		//this.graph.view.container.scrollTop = v.ui.offsetTop>500?v.ui.offsetTop-300:0;
		if(v.ui.offsetLeft>500){
			this.graph.view.container.scrollLeft = v.ui.offsetLeft-300;
		}
		if(v.ui.offsetTop>500){
			this.graph.view.container.scrollTop = v.ui.offsetTop-300;
		}
		v.ui.className = 'ui-gotoed';
	}
};

/* listener is object: {selected: fn, unselected: fn, selecting: fn, unselecting: fn, start: fn, stop: fn} */
ZetaGraphSelectionModel.prototype.addSelectionListener = function(listener) {
	this.selectionListener = listener;
	if (listener.selected) {
		$(this.graph.jid).bind('selectableselected', listener.selected);
	}
	if (listener.unselected) {
		$(this.graph.jid).bind('selectableunselected', listener.unselected);
	}
	if (listener.selecting) {
		$(this.graph.jid).bind('selectableselecting', listener.selecting);
	}
	if (listener.unselecting) {
		$(this.graph.jid).bind('selectableunselecting', listener.unselecting);
	}
	if (listener.start) {
		$(this.graph.jid).bind('selectablestart', listener.start);
	}
	if (listener.stop) {
		$(this.graph.jid).bind('selectablestop', listener.stop);
	}					
};

ZetaGraphSelectionModel.prototype.removeSelectionListener = function(listener) {
	this.selectionListener = null;
	if (listener.selected) {
		$(this.graph.jid).unbind('selectableselected', listener.selected);
	}
	if (listener.unselected) {
		$(this.graph.jid).unbind('selectableunselected', listener.unselected);
	}
	if (listener.selecting) {
		$(this.graph.jid).unbind('selectableselecting', listener.selecting);
	}
	if (listener.unselecting) {
		$(this.graph.jid).unbind('selectableunselecting', listener.unselecting);
	}
	if (listener.start) {
		$(this.graph.jid).unbind('selectableselectablestart', listener.start);
	}
	if (listener.stop) {
		$(this.graph.jid).unbind('selectablestop', listener.stop);
	}
};

ZetaGraphSelectionModel.prototype.addListener = function(name, listener) {
	$(this.graph.jid).bind(name, listener);
};

ZetaGraphSelectionModel.prototype.removeListener = function(name, listener) {
	$(this.graph.jid).unbind(name, listener);
};

ZetaGraphSelectionModel.prototype.setupDefaultListener = function() {
	var selectionModel = this;
	//this.addListener equals $(this.graph.jid).bind('e',handler)
	this.addListener('mousedown', function(evt) {
		if (evt.target != selectionModel.graph.view.renameBox) {
			selectionModel.graph.stopRenaming();
		}
		if (selectionModel.graph.resizeVertex != null && evt.target != selectionModel.graph.resizeVertex.ui) {
			selectionModel.graph.stopResizing();
		}
		if (evt.target.tagName != 'DIV' ) {
			/*if(evt.target.id = evt.target.children[0].id){
				selectionModel.clearSelections();
				selectionModel.selectedId = selectionModel.selectElement(evt.target);
				return false;
			}//BREAK;
*/			if (!evt.ctrlKey) {
				if (!selectionModel.isSelectedElment(evt.target)) {
					selectionModel.clearSelections();
					selectionModel.selectedId = selectionModel.selectElement(evt.target);
				}
			}
		}
	});	
	this.addListener('mouseup', function(evt) {
		if (evt.target.tagName == 'DIV' ) {
			if (!evt.ctrlKey) {
				selectionModel.clearSelections();
				selectionModel.selectedId = null;
			}
		} else {
			if (evt.which == MouseEvent.LEFT || (selectionModel.getSelectedVertexs().length <= 1 && evt.which == MouseEvent.RIGHT)) {
				if (selectionModel.isSelectedElment(evt.target)) {
					if (evt.ctrlKey) {
						selectionModel.deselectElement(evt.target);
						selectionModel.selectedId = null;
					} else {
						selectionModel.clearSelections();
						/*if(evt.target.children[0] && evt.target.children[0].tagName == 'Oval'){
							selectionModel.selectedId = selectionModel.selectElement(evt.target.children[0]);
						}else{*/
							selectionModel.selectedId = selectionModel.selectElement(evt.target);
						//}
					}
				} else {
					if (evt.ctrlKey) {
						selectionModel.selectedId = selectionModel.selectElement(evt.target);
					}
				}
			}
		}
	});
};

ZetaGraphSelectionModel.prototype.selectAdjacent = function(vertex) {
	var edges = vertex.getEdges();
	for (var i = 0; i < edges.length; i++) {
		if (vertex != edges[i].getSource()) {
			this.selectCell(edges[i].getSource());
		}
		if (vertex != edges[i].getTarget()) {
			this.selectCell(edges[i].getTarget());
		}
	}
};


ZetaGraphSelectionModel.prototype.selectAllVertex = function() {
	this.graph.model.eachVertex(function(v) {
		v.ui.className = 'ui-selected';
	});	
};

ZetaGraphSelectionModel.prototype.selectAllEdge = function() {
	this.graph.model.eachEdge(function(e) {
		e.ui.className = 'ui-selected';
		e.ui.strokecolor = SHAPE_STYLE.linkSelectedColor;
	});
};

ZetaGraphSelectionModel.prototype.selectAll = function() {
	this.selectAllVertex();
	this.selectAllEdge();
};

ZetaGraphSelectionModel.prototype.deselectAll  = function() {
	this.clearSelections();
};

ZetaGraphSelectionModel.prototype.clearSelections = function() {
	var selectees = $(".ui-selected");
	for (var i = 0; i < selectees.length; i++) {
		selectees[i].className = 'ui-unselecting';
		if (selectees[i].isEdge) {
			selectees[i].strokecolor = selectees[i].oldcolor; 
		}
	}
};

ZetaGraphSelectionModel.prototype.deselectElement  = function(element) {
	var src = element;
	while (src.parentNode.id != this.graph.graphId) {
		src = src.parentNode;
	}
	if (src.isCell) {
		src.className = 'ui-unselecting';
		if (src.isEdge) {
			src.strokecolor = src.oldcolor; 
		}
	}
};

/**
 * 选中一个HTMLElement节点
 */
ZetaGraphSelectionModel.prototype.selectElement  = function(element) {
	var src = element;
	while (src.parentNode.id != this.graph.graphId) {
		src = src.parentNode;
	}
	if (src.isCell) {
		src.className = 'ui-selected';
		if (src.isEdge) {
			src.strokecolor = SHAPE_STYLE.linkSelectedColor; 
		}	
		return src.id;
	}
	return null;
};

ZetaGraphSelectionModel.prototype.selectCell  = function(cell) {
	cell.ui.className = 'ui-selected';
	if (cell.ui.isEdge) {
		cell.ui.strokecolor = SHAPE_STYLE.linkSelectedColor; 
	}	
};

ZetaGraphSelectionModel.prototype.deselectCell  = function(cell) {
	cell.ui.className = 'ui-unselecting';
	if (cell.ui.isEdge) {
		cell.ui.strokecolor = cell.ui.oldcolor; 
	}	
};

ZetaGraphSelectionModel.prototype.deselectById  = function(id) {
	var cell = this.graph.getModel().getVertex(id);
	if (cell != null) {
		this.deselectCell(cell);
	}
};

ZetaGraphSelectionModel.prototype.selectById  = function(id) {
	var cell = this.graph.getModel().getVertex(id);
	if (cell != null) {
		this.selectCell(cell);
	}
};

ZetaGraphSelectionModel.prototype.isCellSelected = function(cell) {
	return cell.getUI().className.indexOf('ui-selected') != -1;
};

ZetaGraphSelectionModel.prototype.isIdSelected = function(id) {
	var v = this.graph.getModel().getVertex(id);
	if (v == null) {
		return false;
	} else {
		return v.getUI().className.indexOf('ui-selected') != -1;
	}
};

/**
 * 判断当前HTMLElement是否已经被选中
 * 由于只对vertext进行支持，故img，span等的节点事件都委托由vertext节点处理
 */
ZetaGraphSelectionModel.prototype.isSelectedElment = function(element) {
	var src = element;
	while (src.parentNode != null && src.parentNode.id != this.graph.graphId) {
			src = src.parentNode;
	}
	//assert : src is vertext
	if (src.className) {
		return src.className.indexOf('ui-selected') != -1;
	} else {
		return false;
	}
};

/* return ZetaVertex and ZetaEdge Array */
ZetaGraphSelectionModel.prototype.getSelections  = function() {
	var arr = [];
	var model = this.graph.getModel();
	var selectees = $(".ui-selected");
	for (var i = 0; i < selectees.length; i++) {
		arr[i] = model.getCell(selectees[i].id);
	}
	return arr;
};

ZetaGraphSelectionModel.prototype.getSelectedVertexs  = function() {
	var arr = [];
	var model = this.graph.getModel();
	var selectees = $(".ui-selected");
	var vertex = null;
	for (var i = 0; i < selectees.length; i++) {
		vertex = model.getVertex(selectees[i].id);
		if (vertex != null) {
			arr[arr.length] = vertex;
		}
	}
	return arr;
};

ZetaGraphSelectionModel.prototype.getSelectedEdges  = function() {
	var arr = [];
	var model = this.graph.getModel();
	var selectees = $(".ui-selected");
	var edge = null;
	for (var i = 0; i < selectees.length; i++) {
		edge = model.getEdge(selectees[i].id);
		if (edge != null) {
			arr[arr.length] = edge;
		}
	}
	return arr;
};

/* return ZetaVertex or ZetaEdge */
ZetaGraphSelectionModel.prototype.getSelection  = function() {
	return this.selectedId == null ? null : this.graph.model.getCell(this.selectedId);
};

ZetaGraphSelectionModel.prototype.getSelectedVertex  = function() {
	return this.selectedId == null ? null : this.graph.model.getVertex(this.selectedId);
};

ZetaGraphSelectionModel.prototype.getSelectedEdge  = function() {
	return this.selectedId == null ? null : this.graph.model.getEdge(this.selectedId);
};

ZetaGraphSelectionModel.prototype.hasSelection  = function() {
	var selectee = $(".ui-selected:first");
	return selectee.attr("id") != null;
};

ZetaGraphSelectionModel.prototype.getCount = function(element) {
	return $(".ui-selected").length;
};

/* graph cell */
function ZetaPort(obj) {
	this.userObject = obj;
	this.edges = new Array();
}

ZetaPort.prototype.edges = null;
ZetaPort.prototype.parent = null;
ZetaPort.prototype.userObject = null;

ZetaPort.prototype.getUserObject  = function() {
	return this.userObject;
};

ZetaPort.prototype.getEdges  = function() {
	return this.edges;
};

ZetaPort.prototype.eachEdge  = function(fn) {
	for (var i = 0; i < this.edges.length; i++) {
		fn(this.edges[i]);
	}
};

ZetaPort.prototype.addEdge  = function(edge) {
};

ZetaPort.prototype.removeEdge  = function(edge) {
};

ZetaPort.prototype.setParent  = function(p) {
	this.parent = p;
};

/* zeta vertex */
function ZetaVertex(obj, render) {
	if (typeof render == 'undefined') {
		//---如果没有渲染对象，则使用默认渲染。render对象确定了拓扑节点的X,Y，Width，Height等信息----//
		render = new DefaultZetaCellRenderer(obj.id, obj.text, obj.x, obj.y);
	}
	this.x = obj.x;
	this.y = obj.y;
	this.userObject = obj;
	this.cellId = obj.id;
	this.jid =  '#' + obj.id;
	this.renderer = render;
	//-----ui就是组件对象：var shape = document.createElement('v:shape');this.ui = shape;getComponent:return ui----//
	//-----render对象不同于render.ui对象，render.ui对象的第一个孩子节点确定了this对象的Width，Height等信息-------//
	//-----BUG:在得到render.ui对象的同时，应该将render.ui的孩子节点的宽和高设置到this.ui对象上，否则在未出现状态信息时切换tab，导致状态信息移位---//
	this.ui = render.getComponent();	
	this.ui.isCell = 1;
	this.children = new Array();
	this.edges = new Array();
	this.ports = new Array();
}

ZetaVertex.prototype.userObject = null;
ZetaVertex.prototype.cellId = null;
ZetaVertex.prototype.jid = null;
ZetaVertex.prototype.fixed = false;
ZetaVertex.prototype.parent = null;
ZetaVertex.prototype.children = null;
ZetaVertex.prototype.edges = null;
ZetaVertex.prototype.ports = null;
ZetaVertex.prototype.marker = null;
ZetaVertex.prototype.markerVisible = true;
ZetaVertex.prototype.iconMarker = null;
ZetaVertex.prototype.iconMarkerVisible = true;
ZetaVertex.prototype.group = false;
ZetaVertex.prototype.expanded = true;
ZetaVertex.prototype.renderer = null;
ZetaVertex.prototype.ui = null;
ZetaVertex.prototype.stroke = null;
ZetaVertex.prototype.fill = null;
ZetaVertex.prototype.shadow = null;
ZetaVertex.prototype.extrusion = null;
ZetaVertex.prototype.visible = true;
ZetaVertex.prototype.groupable = false;
ZetaVertex.prototype.selectable = true;
ZetaVertex.prototype.movable = true;
ZetaVertex.prototype.resizable = true;
ZetaVertex.prototype.x = 0;
ZetaVertex.prototype.y = 0;
ZetaVertex.prototype.width = 0;
ZetaVertex.prototype.height = 0;
ZetaVertex.prototype.center = null;
ZetaVertex.prototype.anchor = null;
ZetaVertex.prototype.leftAnchor = null;
ZetaVertex.prototype.rightAnchor = null;
ZetaVertex.prototype.topAnchor = null;
ZetaVertex.prototype.bottomAnchor = null;
ZetaVertex.prototype.graph = null;

ZetaVertex.prototype.setGraph = function(g) {
    this.graph = g;
};
ZetaVertex.prototype.createShadow = function() {
	var shadow = document.createElement('v:shadow');
	return shadow;
};

ZetaVertex.prototype.createFill = function() {
	var fill = document.createElement('v:fill');
	fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
	fill.setAttribute('angle', '180');
	return fill;
};

ZetaVertex.prototype.createStroke = function() {
	var stroke = document.createElement('v:stroke');
	return stroke;
};

ZetaVertex.prototype.createExtrusion = function() {
	var extrusion = document.createElement('v:extrusion');
	//extrusion.metal = 't';
	//extrusion.color = '#000';
	extrusion.backdepth = '20pt';
	//extrusion.diffusity = '1.2';
	extrusion.skewangle = 90;
	extrusion.brightness = '0.2';
	//extrusion.rotationangle = '90,180';
	return extrusion;
};

ZetaVertex.prototype.isGroupable = function() {
	return this.groupable;
};

ZetaVertex.prototype.isExpanded = function() {
	return this.expanded;
};

ZetaVertex.prototype.expand = function() {
	this.setExpanded(true);
};

ZetaVertex.prototype.collapse = function() {
	this.setExpanded(false);
};

ZetaVertex.prototype.setExpanded = function(flag) {
	this.renderer.setExpanded(flag);
	this.expanded = flag;
	this.width = this.ui.offsetWidth;
	this.height = this.ui.offsetHeight;
	this.recomputeCenter();

	var child;
	var edges;
	var size1 = 0;
	var size = this.children.length;
	for(var i = 0; i < size; i++) {
		child = this.children[i]; 
		child.setVisible(flag);
		edges = child.getEdges();
		size1 = edges.length;
		for (var j = 0; j < size1; j++) {
			if (edges[j].getSource().getParent() == edges[j].getTarget().getParent()) {
				edges[j].setVisible(flag);
				//if (flag) {
					if (edges[j].getSource() == child) {
						edges[j].fireSourceChanged();
					} else {
						edges[j].fireTargetChanged();
					}
				//}
			} else {
				if (edges[j].getSource() == child) {
					edges[j].fireSourceChanged();
				} else {
					edges[j].fireTargetChanged();
				}
			}
		}
	}
};

ZetaVertex.prototype.getUserObject = function() {
	return this.userObject;
};

ZetaVertex.prototype.setUserObject = function(obj) {
	this.userObject = obj;
};

ZetaVertex.prototype.getJid = function() {
	return this.userObject;
};

ZetaVertex.prototype.setJid = function(jid) {
	this.jid = jid;
};

ZetaVertex.prototype.getUI = function() {
	return this.ui;
};

ZetaVertex.prototype.getRenderer = function() {
	return this.renderer;
};

ZetaVertex.prototype.getCellId = function() {
	return this.cellId;
};

ZetaVertex.prototype.setCellId = function(cellId) {
	this.cellId = cellId;
	this.ui.id = cellId;
};

ZetaVertex.prototype.setVisible = function(visible) {
	this.visible = visible;
	if (this.marker != null) {
		this.marker.setVisible(visible && this.markerVisible);
	}
	if (this.iconMarker != null) {
		this.iconMarker.setVisible(visible && this.iconMarkerVisible);
	}
	this.ui.style.display = visible ? '' : 'none';
};

ZetaVertex.prototype.isVisible = function(visible) {
	return this.visible;
};

/**
 * group changed listener: childAdded, childRemoved 
 * setup vertext groupable
 */ 
ZetaVertex.prototype.setGroupable = function(able, groupListener) {
	this.groupable = able;
	var group = this;
	var listener = groupListener;
	if (able) {
		$(this.ui).droppable({
			activeClass: SHAPE_STYLE.activeClass,
			hoverClass: SHAPE_STYLE.hoverClass,
			accept: '*[isCell]',
			drop: function(evt, ui) {
				var $item = ui.draggable;
				var v = graph.model.getVertex($item.attr('id'));
				if (v != null && !v.group) {
					if(v.children.indexOf(group) != -1){
						window.top.showMessageDlg('提示','不允许在组里相互添加！')
						return
					}
					if(group.addChild(v) && listener != null) {
						listener.childAdded(group, v);
					}
				}
			},
			out: function(evt, ui) {
				var $item  = ui.draggable;
				var v = graph.model.getVertex($item.attr('id'));
				if (v != null && !v.group) {
					group.removeChild(v);
					listener.childRemoved(group, v);
				}
			}
		});
	} else {
		$(this.ui).droppable('disable');
	}
};

ZetaVertex.prototype.setMovable = function(able) {
	this.movable = this.userObject.fixed = able;
	$(this.jid).draggable(!able ? 'disable' : 'enable');
};

ZetaVertex.prototype.setFixed = function(fixed) {
	this.fixed = this.userObject.fixed = fixed;
	$(this.jid).draggable(fixed ? 'disable' : 'enable');
};

ZetaVertex.prototype.isFixed = function() {
	return (this.userObject.fixed || this.fixed);
};

ZetaVertex.prototype.setDraggable = function(able) {
	$(this.jid).draggable(able ? 'enable' : 'disable');
};

ZetaVertex.prototype.setX = function(x) {
	if (x < 0) x = 0;
	var offsetx = x - this.x;
	var offsety = 0;
	this.ui.style.left = x + "px";
	this.x = x;
	this.recomputeCenter();
	this.coordChanged(offsetx, offsety);
};

ZetaVertex.prototype.setY = function(y) {
	if (y < 0) y = 0;
	var offsetx = 0;
	var offsety = y - this.y;
	this.ui.style.top = y + "px";
	this.y = y;
	this.recomputeCenter();
	this.coordChanged(offsetx, offsety);
};

/**
 * 设置拓扑节点的位置
 */
ZetaVertex.prototype.setXY = function(x, y) {
//    zoomsize = this.graph.view.canvas.style.zoom;
//    x = x / zoomsize;
//    y = y / zoomsize;
    if (x < 0) x = 0;
	if (y < 0) y = 0;
	var offsetx = x - this.x;
	var offsety = y - this.y;
	this.ui.style.left = x + "px";
	this.x = x;
	this.ui.style.top = y + "px";
	this.y = y;
	this.recomputeCenter();
	this.coordChanged(offsetx, offsety);
};

ZetaVertex.prototype.setPosition = function(position) {
	this.setXY(position.x, position.y);
};

ZetaVertex.prototype.getX = function() {
	return this.x;
};

ZetaVertex.prototype.getY = function() {
	return this.y;
};

/**
 * 设置拓扑节点的宽度
 */
ZetaVertex.prototype.setWidth = function(w) {
	if (this.ui.tagName == 'DIV') {
		this.renderer.shape.style.width = this.width = w + "px";;
	} else {
		this.ui.style.width = this.width = w + "px";;
	}
};
/**
 * 设置拓扑节点的高度
 */
ZetaVertex.prototype.setHeight = function(h) {
	if (this.ui.tagName == 'DIV') {
		this.renderer.shape.style.height = this.height = h  + "px";;
	} else {
		this.ui.style.height = this.height = h + "px";;
	}
};

/**
 * 设置拓扑节点的大小
 */
ZetaVertex.prototype.setSize = function(w, h) {
	this.ui.style.width = this.width = w + "px";;
	this.ui.style.height = this.height = h + "px";;
};

/**
 * 得到拓扑节点的宽度
 * BUG:load页面后立即切换视图，导致offsetwidth未设置，可能在其他地方有类似BUG
 */
ZetaVertex.prototype.getWidth = function() {
	////-----offsetWidth在页面不可见的时候会导致取值为0的异常，故判定如果为0，则使用默认值，默认值需要关联上缩放比例----//
	var defaultOffsetWidth = 110 * zoomSize;
	var w = this.ui.offsetWidth>0?this.ui.offsetWidth:defaultOffsetWidth;
	//var w = this.ui.children[0].style.width;
	//var w = this.render.width;
	//alert($("#cell30000000004").attr("offsetWidth"));
	//alert(this.width);
	if (w > 0) {
		this.width = w;
	}
	return this.width;
};
ZetaVertex.prototype.getHeight = function() {
	var h = this.ui.offsetHeight;
	if (h > 0) {
		this.height = h;
	}
	return this.height;
};

ZetaVertex.prototype.getIconWidth = function() {
	return this.renderer.icon.width;
};

ZetaVertex.prototype.getIconHeight = function() {
	return this.renderer.icon.height;
};

ZetaVertex.prototype.setIcon = function(icon) {
	this.renderer.setIcon(icon);
};

ZetaVertex.prototype.getIcon = function(icon) {
	return this.renderer.icon.src;
};

/**
 * 更新拓扑节点的位置信息，大小信息等
 */
ZetaVertex.prototype.update = function() {
	this.width = this.ui.offsetWidth;
	this.height = this.ui.offsetHeight;
	this.recomputeCenter();
	if (this.iconMarker != null) {
		this.iconMarker.setXY(this.getCenterX()-8, this.getY()+this.getIconHeight()-10);
	}
	if (this.marker != null) {
		//---设置tooltip的中心位置----//
		this.marker.setXY(this.getCenterX(), this.getY()-20);
	}
	this.coordChanged(0,0);
};

ZetaVertex.prototype.setText = function(text) {
	this.renderer.setText(text);
};

ZetaVertex.prototype.getText = function() {
	return this.renderer.text.innerText;
};

ZetaVertex.prototype.setLabel = function(label) {
	this.renderer.setText(label);
};

ZetaVertex.prototype.getTextStyle = function() {
	return this.text.style;
};

ZetaVertex.prototype.setTextWidth = function(w) {
	this.renderer.text.style.width = w;
};

ZetaVertex.prototype.setFontSize = function(size) {
	this.text.style.fontSize = size;
};

ZetaVertex.prototype.setForcolor = function(color) {
	this.text.style.fontColor = color;
};

ZetaVertex.prototype.setFillColor = function(color) {
	this.ui.setAttribute('fillcolor', color);
};

ZetaVertex.prototype.getFillColor = function() {
	var color = this.ui.getAttribute('fillcolor');
	return color == null ? SHAPE_STYLE.fillColor : color;
};

ZetaVertex.prototype.setGradientColor = function(color) {
	//this.fill.setAttribute('color', color);
	this.fill.setAttribute('color2', color);
};

ZetaVertex.prototype.setGradientDirection = function(dir) {
	this.fill.setAttribute('focus', '100%');
};

ZetaVertex.prototype.setGradient = function(gradient) {
	if (this.fill == null) {
		this.fill = this.createFill();
		this.ui.appendChild(this.fill);
	}
	this.fill.setAttribute('type', gradient ? 'gradient' : 'solid');
};

ZetaVertex.prototype.setStrokeWeight = function(weight) {
	this.ui.strokeweight = weight;
};

ZetaVertex.prototype.setStrokeColor = function(color) {
	this.ui.strokecolor = color;
};

ZetaVertex.prototype.getStrokeColor = function() {
	var color = this.ui.strokecolor;
	return color == null ? SHAPE_STYLE.strokeColor : color;
};

ZetaVertex.prototype.setShadowVisible = function(visible) {
	if (this.shadow == null) {
		this.shadow = this.createShadow();
		this.ui.appendChild(this.shadow);
	}
	if (visible) {
		this.shadow.on = 't';
	} else {
		this.shadow.on = 'false';
	}
};

ZetaVertex.prototype.setDashed = function(dashed) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.ui.appendChild(this.stroke);
	}
	if (dashed) {
		this.stroke.dashstyle = SHAPE_STYLE.linkDashStyle;
	} else {
		this.stroke.dashstyle = 'solid';
	}
};

ZetaVertex.prototype.setExtrusion = function(extrusion) {
	if (this.extrusion == null) {
		this.extrusion = this.createExtrusion();
		this.ui.appendChild(this.extrusion);
	}
	this.extrusion.on = extrusion ? 't' : 'false';
};

ZetaVertex.prototype.setBackgroundColor = function(color) {
	this.renderer.setBackground(color);
};

ZetaVertex.prototype.setIconBackgroundColor = function(color) {
	this.renderer.setIconBackground(color);
};

ZetaVertex.prototype.setTextBackgroundColor = function(color) {
	this.renderer.setTextBackground(color);
};

ZetaVertex.prototype.setParent = function(p) {
	this.parent = p;
};

ZetaVertex.prototype.getParent = function(p) {
	return this.parent;
};

ZetaVertex.prototype.addChild = function(child) {
	child.setParent(this);
	return this.children.add(child);
};

ZetaVertex.prototype.removeChild = function(child) {
	child.setParent(null);
	this.children.remove(child);
};

ZetaVertex.prototype.getChildren = function() {
	return this.children;
};

ZetaVertex.prototype.addEdge = function(edge) {
	this.edges[this.edges.length] = edge;
};

ZetaVertex.prototype.getEdges = function() {
	return this.edges;
};

ZetaVertex.prototype.getRightAnchor = function() {
	var fromx = this.ui.offsetLeft + this.ui.clientWidth;
	if (this.renderer.icon) {
		var fromy = this.ui.offsetTop + Math.round(this.renderer.icon.clientHeight / 2) + SHAPE_STYLE.imgOffset;
		return [fromx,fromy];
	} else {
		var fromy = this.ui.offsetTop + Math.round(this.ui.clientHeight / 2);
		return [fromx,fromy];
	}
};

ZetaVertex.prototype.getLeftAnchor = function() {
	var fromx = this.ui.offsetLeft;
	if (this.renderer.icon) {
		var fromy = this.ui.offsetTop + Math.round(this.renderer.icon.clientHeight / 2) + SHAPE_STYLE.imgOffset;
		return [fromx,fromy];
	} else {
		var fromy = this.ui.offsetTop + Math.round(this.ui.clientHeight / 2);
		return [fromx,fromy];
	}
};

ZetaVertex.prototype.getAnchor = function() {
	var fromx = this.ui.offsetLeft;
	var fromy = 0;
	if (this.renderer.icon) {
		fromx = fromx + Math.round((this.ui.clientWidth - this.renderer.icon.clientWidth) / 2);
	}
	if (this.renderer.icon) {
		fromy = this.ui.offsetTop + Math.round(this.renderer.icon.clientHeight / 2) + SHAPE_STYLE.imgOffset;
	} else {
		fromy = this.ui.offsetTop + Math.round(this.ui.clientHeight / 2);
	}
	return [fromx,fromy];
};

ZetaVertex.prototype.recomputeCenter = function() {
	if (this.center==null) {
		this.center = new ZetaPoint(0,0);
		this.anchor = new ZetaPoint(0,0);
		this.leftAnchor = new ZetaPoint(0,0);
		this.rightAnchor = new ZetaPoint(0,0);
		this.topAnchor = new ZetaPoint(0,0);
		this.bottomAnchor = new ZetaPoint(0,0);
	}
	this.center.x = this.x + Math.round(this.getWidth()/2);
	if (this.renderer.icon) {
		this.center.y = this.y + Math.round(this.renderer.icon.clientHeight/2) + SHAPE_STYLE.imgOffset;
	} else {
		this.center.y = this.y + Math.round(this.height/2);
	}
};
ZetaVertex.prototype.getCenter = function() {
	if (this.parent != null && !this.visible) {
		return this.parent.getCenter();
	}
	if (this.center==null) {
		this.recomputeCenter();
	}
	return [this.center.x,this.center.y];
};
ZetaVertex.prototype.getCenterX = function() {
	if (this.center==null) {
		this.recomputeCenter();
	}
	return this.center.x;
};
ZetaVertex.prototype.getCenterY = function() {
	if (this.center==null) {
		this.recomputeCenter();
	}
	return this.center.y;
};

/**
 * 拖动时将输入当前组的设备一起拖动
 */
ZetaVertex.prototype.coordChanged = function(offsetx, offsety) {
	if (this.marker != null) {
		this.marker.moveOffset(offsetx, offsety);
	}
	if (this.iconMarker != null) {
		this.iconMarker.moveOffset(offsetx, offsety);
	}
	//-----移动组中的设备-----//
	var size = this.children.length;
	if (size > 0) {
		for (var i = 0; i < this.children.length; i++) {
			this.children[i].moveOffset(offsetx, offsety);
		}
	}
	size = this.edges.length;
	if (size > 0) {
		var edge;
		for (var i = 0; i < size; i++) {
			edge = this.edges[i];
			if (edge.getSource() == this) {
				edge.fireSourceChanged();
			} else {
				edge.fireTargetChanged();
			}
		}
	}
};

ZetaVertex.prototype.moveOffset = function(offsetx, offsety) {
	var x = 0;
	var y = 0;
	var tagName = this.ui.tagName;
	//----BUG:平移的时候其实不应该跳过，否则y就为0，到了顶部:@bravin----//
	/*if (offsetx != 0) {
		if (tagName == 'DIV') {
			x = this.x + offsetx;
		} else {
			x = this.x + offsetx + SHAPE_STYLE.helperOffset;
		}
	}
	if (offsety != 0) {
		if (tagName == 'DIV') {
			y = this.y + offsety;;
		} else {
			y = this.y + offsety + SHAPE_STYLE.helperOffset;
		}
	}*/
	//---修正：@bravin--------//
	if (tagName == 'DIV') {//---------SET X
		x = this.x + offsetx;
	} else {
		x = this.x + offsetx + SHAPE_STYLE.helperOffset;
	}

	if (tagName == 'DIV') {//---------SET Y
		y = this.y + offsety;;
	} else {
		y = this.y + offsety + SHAPE_STYLE.helperOffset;
	}
	//----SET POSITON(ABSOLUTE TO BODY)----//
	this.setXY(x,y);
};

ZetaVertex.prototype.getDefaultPort = function() {
};

ZetaVertex.prototype.setIconMarker = function(marker) {
	this.iconMarker = marker;
};

ZetaVertex.prototype.getIconMarker = function() {
	return this.iconMarker;
};

ZetaVertex.prototype.setMarker = function(marker) {
	this.marker = marker;
};

ZetaVertex.prototype.getMarker = function() {
	return this.marker;
};

ZetaVertex.prototype.setMarkerVisible = function(visible) {
	this.markerVisible = visible;
	if (this.marker != null) {
		this.marker.setVisible(this.visible && visible);
	}	
};

ZetaVertex.prototype.setIconMarkerVisible = function(visible) {
	this.iconMarkerVisible = visible;
	if (this.iconMarker != null) {
		this.iconMarker.setVisible(this.visible && visible);
	}	
};

ZetaVertex.prototype.setZIndex = function(index) {
	this.ui.style.zIndex = index;
};

ZetaVertex.prototype.setTooltip = function(title) {
	this.ui.title = title;
};

ZetaVertex.prototype.setCluetip = function(title, url, cache) {
	if (typeof cache == 'undefined') {
		cache = false;
	}
	this.ui.cluetip = title;
	//设置cluetip中将要显示的内容的来源
	this.ui.rel = url;
	$(this.jid).cluetip({
		cluetipClass: 'rounded', titleAttribute: 'cluetip', positionBy: 'mouse',
		width: 320, arrows: false, waitImage: true, ajaxCache: cache, dropShadow: false,
		activation: cluetipShowType,
		delayedClose: cluetipCloseTimeout, 
		fx: {             
			open: cluetipEffectType,
			openSpeed: ''
	   	},
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled    
	});
};

/* edge, has id */
function ZetaEdge(obj, source, target, renderer) {
	if (typeof renderer == 'undefined' || renderer == null) {
		renderer = new DefaultZetaEdgeRenderer(obj.id, obj.text, 
			source.getCenter(), target.getCenter());
	} else {
		this.line = false;
	}
	this.userObject = obj;
	this.cellId = obj.id;
	this.jid = '#' + obj.id;
	this.source = source;
	this.target = target;
	source.addEdge(this);
	target.addEdge(this);	

	this.renderer = renderer;
	this.ui = renderer.getComponent();
	this.ui.isCell = 1;
	this.ui.isEdge = 1;
	this.ui.oldcolor = SHAPE_STYLE.linkColor;
}

ZetaEdge.prototype.userObject = null;
ZetaEdge.prototype.cellId = null;
ZetaEdge.prototype.jid = null;
ZetaEdge.prototype.ui = null;
ZetaEdge.prototype.renderer = null;
ZetaEdge.prototype.stroke = null;
ZetaEdge.prototype.source = null;
ZetaEdge.prototype.target = null;
ZetaEdge.prototype.foregroundColor = '#000';
ZetaEdge.prototype.label = '';
ZetaEdge.prototype.marker = null;
ZetaEdge.prototype.labelVisible = true;
ZetaEdge.prototype.startArrow = false;
ZetaEdge.prototype.endArrow = false;
ZetaEdge.prototype.arrow = null;
ZetaEdge.prototype.line = true;

ZetaEdge.prototype.getUserObject = function() {
	return this.userObject;
};

ZetaEdge.prototype.setUserObject = function(obj) {
	this.userObject = obj;
};

ZetaEdge.prototype.getCellId = function() {
	return this.cellId;
};

ZetaEdge.prototype.getUI = function() {
	return this.renderer.ui;
};

ZetaEdge.prototype.getRenderer = function() {
	return this.renderer;
};

ZetaEdge.prototype.setRenderer = function(renderer) {
	this.renderer = renderer;
	this.ui = renderer.getComponent();
	this.ui.isEdge = true;
	this.ui.oldcolor = SHAPE_STYLE.linkColor;	
	this.firePositionChanged();
};

ZetaEdge.prototype.getTextStyle = function() {
	return this.text.style;
};

ZetaEdge.prototype.setStrokeWeight = function(weight) {
	this.ui.strokeweight = weight;
};

ZetaEdge.prototype.setStrokeColor = function(color) {
	this.ui.strokecolor = color;
};

ZetaEdge.prototype.setEdgeColor = function(color) {
	this.ui.oldcolor = color;
	if (this.ui.strokecolor != SHAPE_STYLE.linkSelectedColor) {
		this.ui.strokecolor = color;
	}
};

ZetaEdge.prototype.setEdgeWeight = function(weight) {
	this.ui.strokeweight = weight;
};

ZetaEdge.prototype.setMarker = function(marker) {
	this.marker = marker;
};

ZetaEdge.prototype.getMarker = function() {
	return this.marker;
};

ZetaEdge.prototype.setMarkerVisible = function(visible) {
	if (this.marker != null) {
		this.marker.setVisible(visible);
	}
};

ZetaEdge.prototype.setLabel = function(text) {
	this.label = text;
	this.renderer.text.innerText = text;
};

ZetaEdge.prototype.setLabelVisible = function(visible) {
	this.renderer.text.style.display = visible ? '' : 'none';
};

ZetaEdge.prototype.setVisible = function(visible) {
	this.renderer.ui.style.display = visible ? '' : 'none';
};

ZetaEdge.prototype.setFontSize = function(size) {
	this.text.style.fontSize = size;
};

ZetaEdge.prototype.setForegroundColor = function(color) {
	this.renderer.text.style.color = this.foregroundColor = color;
};

ZetaEdge.prototype.setBackgroundColor = function(color) {
	this.renderer.ui.strokecolor = color;
};

ZetaEdge.prototype.putStyle = function(name, value) {
};

ZetaEdge.prototype.setShadowVisible = function(visible) {
	if (visible) {
		this.addShadow();
	} else {
		this.removeShadow();
	}
};

ZetaEdge.prototype.addShadow = function() {
	if (this.renderer.shadow == null) {
		var shadow = this.renderer.createShadow();
		this.renderer.shadow = shadow;
		this.renderer.ui.appendChild(shadow);
	}
};

ZetaEdge.prototype.removeShadow = function() {
	if (this.renderer.shadow != null) {
		this.renderer.ui.removeChild(this.renderer.shadow);
		this.renderer.shadow = null;
	}
};

ZetaEdge.prototype.getSource = function() {
	return this.source;
};

ZetaEdge.prototype.getTarget = function() {
	return this.target;
};

ZetaEdge.prototype.createStroke = function() {
	var stroke = document.createElement('v:stroke');
	return stroke;
};

ZetaEdge.prototype.update = function() {
	this.firePositionChanged();
};

ZetaEdge.prototype.setStartArrowStyle = function(style) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.renderer.ui.appendChild(this.stroke);
	}
	this.stroke.startarrow = style;
};

ZetaEdge.prototype.setEndArrowStyle = function(style) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.renderer.ui.appendChild(this.stroke);
	}
	this.stroke.endarrow = style;
};

ZetaEdge.prototype.setStartArrow = function(visible) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.renderer.ui.appendChild(this.stroke);
	}
	this.startArrow = visible;
	this.stroke.startarrow = visible ? 'Classic' : 'none';
};

ZetaEdge.prototype.setEndArrow = function(visible) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.renderer.ui.appendChild(this.stroke);
	}
	this.endArrow = visible;
	this.stroke.endarrow = visible ? 'Classic' : 'none';
};

ZetaEdge.prototype.setDashed = function(dashed) {
	if (this.stroke == null) {
		this.stroke = this.createStroke();
		this.renderer.ui.appendChild(this.stroke);
	}
	this.stroke.dashstyle = dashed ? SHAPE_STYLE.linkDashStyle : 'Solid';
};

ZetaEdge.prototype.firePortChanged = function() {
	var from = this.source.getCenter();
	var to = this.target.getCenter();

	var x0 = from[0];
	var y0 = from[1];
	var x1 = to[0];
	var y1 = to[1];

	var w0 = this.source.getWidth();
	var h0 = this.source.getHeight();
	var w1 = this.target.getWidth();
	var h1 = this.target.getHeight();

	var srcy = 0;
	var srcx = 0;
	var srcy1 = 0;
	var srcx1 = 0;

	if (x0 == x1) {
		if (y0 < y1) {
			srcx = x0;
			srcy = this.source.getY() + h0;
			srcx1 = x0;
			srcy1 = this.target.getY();
		} else {
			srcx = x0;
			srcy = this.source.getY();
			srcx1 = x0;
			srcy1 = this.target.getY() + h1;			
		}
	} else if (y0 == y1) {
		if (x0 < x1) {
			srcx = this.source.getX() + w0;
			srcy = y0;
			srcx1 = this.target.getX();
			srcy1 = y1;
		} else {
			srcx = this.source.getX();
			srcy = y0;
			srcx1 = this.target.getX() + w1;
			srcy1 = y1;						
		}
	} else {
		var k = 1.0*(y1 - y0)/(x1 - x0);
		var b = 1.0*(y0*x1 - x0*y1)/(x1 - x0);

		if (y0 < y1) {
			if (k > 0) {
				srcx = this.source.getX() + w0;
				srcx1 = this.target.getX();					
			} else {
				srcx = this.source.getX();
				srcx1 = this.target.getX() + w1;
			}
			srcy = Math.round(k*srcx) + b;
			srcy1 = Math.round(k*srcx1) + b;
			if (srcy > (this.source.getY() + h0)) {
				srcy = this.source.getY() + h0;
				srcx = Math.round(1.0*(srcy - b)/k);	
			}
			if (srcy1 < this.target.getY()) {
				srcy1 = this.target.getY();
				srcx1 = Math.round(1.0*(srcy1 - b)/k);	
			}					
		} else {
			if (k > 0) {
				srcx = this.source.getX();
			} else {
				srcx = this.source.getX() + w0;
			}
			srcy = Math.round(k*srcx) + b;
			if (srcy < this.source.getY()) {
				srcy = this.source.getY();
				srcx = Math.round(1.0*(srcy - b)/k);	
			}			
			if (k > 0) {
				if (k > 1) {
					srcy1 = this.target.getY() + h1;
					srcx1 = Math.round(1.0*(srcy1 - b)/k);
				} else {
					srcx1 = this.target.getX() + w1;
					srcy1 = Math.round(k*srcx1) + b;
				}				
			} else {
				if (k > -1) {
					srcx1 = this.target.getX();
					srcy1 = Math.round(k*srcx1) + b;			
				} else {
					srcy1 = this.target.getY() + h1;
					srcx1 = Math.round(1.0*(srcy1 - b)/k);			
				}			
			}							
		}
	}
	this.renderer.setXY([srcx, srcy], [srcx1, srcy1]);
};

ZetaEdge.prototype.firePositionChanged = function() {
	if(this.line) {
		if (this.startArrow || this.endArrow) {
			this.firePortChanged();
		} else {
			this.renderer.setXY(this.source.getCenter(), this.target.getCenter());
		}
	} else {
		if (this.startArrow || this.endArrow) {
			if (this.source.getX() < this.target.getX()) {
				this.renderer.setXY(this.source.getRightAnchor(), this.target.getLeftAnchor());
			} else {
				this.renderer.setXY(this.source.getLeftAnchor(), this.target.getRightAnchor());
			}
		} else {
			this.renderer.setXY(this.source.getCenter(), this.target.getCenter());
		}
	}
};

ZetaEdge.prototype.fireSourceChanged = function() {
	if (this.line) {
		if (this.startArrow || this.endArrow) {
			this.firePortChanged();
		} else {
			this.renderer.setSourceXY(this.source.getCenter());
		}		
	} else {
		if (this.startArrow || this.endArrow) {
			if (this.source.getX() < this.target.getX()) {
				this.renderer.setXY(this.source.getRightAnchor(), this.target.getLeftAnchor());
			} else {
				this.renderer.setXY(this.source.getLeftAnchor(), this.target.getRightAnchor());
			}
		} else {
			this.renderer.setSourceXY(this.source.getCenter());
		}	
	}
};

ZetaEdge.prototype.fireTargetChanged = function() {
	if (this.line) {
		if (this.endArrow) {
			this.firePortChanged();
		} else {
			this.renderer.setTargetXY(this.target.getCenter());
		}		
	} else {
		if (this.startArrow || this.endArrow) {
			if (this.source.getX() < this.target.getX()) {
				this.renderer.setXY(this.source.getRightAnchor(), this.target.getLeftAnchor());
			} else {
				this.renderer.setXY(this.source.getLeftAnchor(), this.target.getRightAnchor());
			}
		} else {
			this.renderer.setTargetXY(this.target.getCenter());
		}	
	}
};

ZetaEdge.prototype.setTooltip = function(title) {
	this.ui.title = title;
};

ZetaEdge.prototype.setCluetip = function(title, url, cache) {
	if (typeof cache == 'undefined') {
		cache = false;
	}
	this.ui.cluetip = title;
	this.ui.rel = url;
	$(this.jid).cluetip({
		cluetipClass: 'rounded', titleAttribute: 'cluetip', width: 320, positionBy: 'mouse',
		waitImage: true, arrows: false, dropShadow: false, ajaxCache: cache,
		activation: cluetipShowType,
		delayedClose: cluetipCloseTimeout,
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	    },
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
	    onActivate: getCluetipEnabled    
	});
};

/* zeta point */
function ZetaPoint(x,y) {
	this.x = x;
	this.y = y;
}
ZetaPoint.prototype.x = 0;
ZetaPoint.prototype.y = 0;
ZetaPoint.prototype.getX = function() {
	return this.x;
};
ZetaPoint.prototype.getY = function() {
	return this.y;
};

/* Zeta Bounds */
function ZetaBounds(x,y,w,h) {
	this.x = x;
	this.y = y;
	this.width = w;
	this.height = h;
}
ZetaBounds.prototype.x = 0;
ZetaBounds.prototype.y = 0;
ZetaBounds.prototype.width = 0;
ZetaBounds.prototype.height = 0;
ZetaBounds.prototype.getX = function() {
	return this.x;
};
ZetaBounds.prototype.getY = function() {
	return this.y;
};
ZetaBounds.prototype.getWidth = function() {
	return this.width;
};
ZetaBounds.prototype.getHeight = function() {
	return this.height;
};

/* zeta path */
function ZetaPath() {
	this.path = new Array();
	this.translate = new ZetaPoint(0, 0);
}

ZetaPath.prototype.path = null;
ZetaPath.prototype.translate = null;

ZetaPath.prototype.getPath = function() {
	return this.path.join('');
};

ZetaPath.prototype.setTranslate = function(x, y) {
	this.translate = new ZetaPoint(x, y);
};

ZetaPath.prototype.moveTo = function(x, y) {
	this.path.push('m ',Math.floor(this.translate.x+x),' ',Math.floor(this.translate.y+y),' ');
};

ZetaPath.prototype.lineTo = function(x, y) {
	this.path.push('l ',Math.floor(this.translate.x+x),' ',Math.floor(this.translate.y+y),' ');
};

ZetaPath.prototype.curveTo = function(x1,y1,x2,y2,x,y) {
	this.path.push('c ',Math.floor(this.translate.x+x1),' ',Math.floor(this.translate.y+y1),' ',Math.floor(this.translate.x+x2),' ',Math.floor(this.translate.y+y2),' ',Math.floor(this.translate.x+x),' ',Math.floor(this.translate.y+y),' ');
};

ZetaPath.prototype.write=function(string) {
	this.path.push(string,' ');
};

ZetaPath.prototype.end = function() {
	this.path.push('e');
};

ZetaPath.prototype.close = function() {
	this.path.push('x e');
};

/* Zeta Marker */
function ZetaMarker(x, y) {}

ZetaMarker.prototype.getComponent = function() {
	return null;
};

/* default icon markder */
function IconZetaMarker(x, y) {
	this.x = x;
	this.y = y;
	var img = document.createElement('img');
	img.style.zIndex = SHAPE_STYLE.markerZIndex;
	img.style.position = 'absolute';
	img.style.left = x + "px";
	img.style.top = y + "px";
	img.ondrag = returnFalse;
	this.icon = img;
}

IconZetaMarker.prototype.icon = null;
IconZetaMarker.prototype.x = 0;
IconZetaMarker.prototype.y = 0;

IconZetaMarker.prototype.getComponent = function() {
	return this.icon;
};

IconZetaMarker.prototype.setXY = function(x,y) {
	this.icon.style.left = x + "px";
	 this.x = x ;
	this.icon.style.top = y + "px";
	this.y = y;
};

IconZetaMarker.prototype.moveOffset = function(offsetx,offsety) {
	this.setXY(this.x+offsetx, this.y+offsety);
};

IconZetaMarker.prototype.setIcon = function(src) {
	this.icon.src = src;
};

IconZetaMarker.prototype.setTooltip = function(alt) {
	this.icon.title = alt;
};

IconZetaMarker.prototype.setCluetip = function(title, url, cache) {
	this.icon.title = title;
	this.icon.rel = url;
	$(this.icon).cluetip({
		cluetipClass: 'rounded', titleAttribute: 'title', width: 320, positionBy: 'mouse',
		waitImage: true, arrows: false, dropShadow: false, ajaxCache: cache,
		activation: 'hover',
		//sticky: true, closePosition: 'title', closeText: ZetaGraphI18N.closeClueTip,
		delayedClose: cluetipCloseTimeout,
	    hoverIntent: {
	        interval: cluetipShowDelaying,
	        timeout: cluetipHideDelaying
	    },
		fx: {
			open: cluetipEffectType,
			openSpeed: ''
	    },
	    onActivate: getCluetipEnabled    
	});
};

IconZetaMarker.prototype.setVisible = function(visible) {
	this.icon.style.display = visible ? '' : 'none';
};

/* float text marker */
function FloatZetaMarker(x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 40;
	}
	if (typeof height == 'undefined') {
		height = 24;
	}
	this.x = x;
	this.y = y < 0 ? 0 : y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.style.zIndex = SHAPE_STYLE.markerZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x + "px";
	shape.style.top = y + "px";
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('path', this.getPath());
	shape.setAttribute('strokecolor', '#000');
	shape.setAttribute('strokeweight', '1');

	var shadow = document.createElement('v:shadow');
	shadow.on = 't';
	shadow.color = '#000';
	shadow.offset = '3px,3px';

	var span = document.createElement('span');
	span.style.width = width - 10;
	span.style.padding = '2px 0px 0px 10px';
	span.style.textAlign = 'center';
	span.style.color = '#000';
	span.style.width = width;
	
	shape.appendChild(shadow);
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
FloatZetaMarker.prototype.ui = null;
FloatZetaMarker.prototype.text = null;
FloatZetaMarker.prototype.x = 0;
FloatZetaMarker.prototype.y = 0;
FloatZetaMarker.prototype.width = 1;
FloatZetaMarker.prototype.height = 1;
FloatZetaMarker.prototype.path = null;

FloatZetaMarker.prototype.getComponent = function() {
	return this.ui;
};

FloatZetaMarker.prototype.setXY = function(x,y) {
	this.ui.style.left = this.x = x;
	this.ui.style.top = this.y = y < 0 ? 0 : y;
};

FloatZetaMarker.prototype.moveOffset = function(offsetx,offsety) {
	this.setXY(this.x+offsetx, this.y+offsety);
};

FloatZetaMarker.prototype.getWidth = function() {
	return this.width;
};

FloatZetaMarker.prototype.getHeight = function() {
	return this.height;
};

FloatZetaMarker.prototype.getPath = function() {
	if (this.path == null) {
		this.path = new ZetaPath();
	}
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

FloatZetaMarker.prototype.redrawPath = function(path,x,y,w,h) {
	var height = h*0.7;
	path.moveTo(0,0);
	path.lineTo(w,0);
	path.lineTo(w,height);
	path.lineTo(0.4*w,height);
	path.lineTo(0.2*w,h);
	path.lineTo(0.2*w,height);
	path.lineTo(0,height);
	path.lineTo(0,0);
	path.close();	
};

FloatZetaMarker.prototype.setVisible = function(visible) {
	this.ui.style.display = visible ? '' : 'none';
};

FloatZetaMarker.prototype.setBackground = function(color) {
	this.ui.setAttribute('fillcolor', color);
};

FloatZetaMarker.prototype.setForeground = function(color) {
	this.text.style.color = color;
};

FloatZetaMarker.prototype.setText = function(text) {
	this.text.innerText = text;
};

FloatZetaMarker.prototype.setHtml = function(html) {
	this.text.innerHTML = html;
};

/* cell renderer interface */
function ZetaCellRenderer(id,text,x,y) {
}
ZetaCellRenderer.prototype.getComponent = function() {
	return null;
};

/* default cell renderer */
function DefaultZetaCellRenderer(id,text,x,y) {
	//--JUST LIKE A CONSTRUCTOR--//
	var div = document.createElement('div');
	div.id = id;
	div.align = 'center';
	div.style.zIndex = SHAPE_STYLE.defaultZIndex;
	div.style.position = 'absolute';
	div.style.left = x + "px";;
	div.style.top = y + "px";;

	var img = document.createElement('img');
	div.appendChild(img);
	img.vspace = 1;
	img.ondrag = returnFalse;

	var br = document.createElement('br');
	div.appendChild(br);

	var span = document.createElement('span');
	div.appendChild(span);
	span.style.textAlign = 'center';
	span.style.padding = '1px 2px';
	span.innerText = text;

	this.ui = div;
	this.icon = img;
	this.text = span;
}
DefaultZetaCellRenderer.prototype.ui = null;
DefaultZetaCellRenderer.prototype.icon = null;
DefaultZetaCellRenderer.prototype.text = null;
DefaultZetaCellRenderer.prototype.width = 1;
DefaultZetaCellRenderer.prototype.height = 1;

DefaultZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

DefaultZetaCellRenderer.prototype.setIcon = function(icon) {
	this.icon.src = icon;
};

DefaultZetaCellRenderer.prototype.setText = function(text) {
	this.text.innerText = text;
};

DefaultZetaCellRenderer.prototype.setTextBackground = function(color) {
	this.text.style.backgroundColor = color;
};

DefaultZetaCellRenderer.prototype.setIconBackground = function(color) {
	this.icon.style.backgroundColor = color;
};

DefaultZetaCellRenderer.prototype.setBackground = function(color) {
	this.ui.style.backgroundColor = color;
};

/* Zeta Graph Edge Renderer */
function ZetaEdgeRenderer(id,text) {
}
ZetaEdgeRenderer.prototype.getComponent = function() {
	return null;
};

function DefaultZetaEdgeRenderer(id, text, from, to) {
	var fromx = from[0];
	var fromy = from[1];
	var tox = to[0];
	var toy = to[1];

	var line = document.createElement('v:line');
	line.id = id;
	line.style.zIndex = SHAPE_STYLE.edgeZIndex;
	line.style.position = 'absolute';
	line.style.padding = '5px';
	line.style.border = '0';
	line.strokecolor = SHAPE_STYLE.linkColor;
	line.strokeweight = SHAPE_STYLE.linkWeight;
	line.from = from[0] + 'px,' + from[1] + 'px';
	line.to = to[0] + 'px,' + to[1] + 'px';	

	var span = document.createElement('span');
	span.style.position = 'relative';	
	span.style.left = 0 + "px";;
	span.style.top = 0 + "px";;
	span.style.padding = '1px 2px';
	span.style.whiteSpace = 'nowrap';

	line.appendChild(span);

	if (SHAPE_STYLE.linkShadow) {
		this.shadow = this.createShadow();
		line.appendChild(this.shadow);
	}

	this.text = span;
	this.ui = line;
	//this.fireTextPositionChanged(fromx, fromy, tox, toy);
}
DefaultZetaEdgeRenderer.prototype.ui = null;
DefaultZetaEdgeRenderer.prototype.text = null;
DefaultZetaEdgeRenderer.prototype.shadow = null;

DefaultZetaEdgeRenderer.prototype.getComponent = function() {
	return this.ui;
};

DefaultZetaEdgeRenderer.prototype.createShadow = function() {
	var shadow = document.createElement('v:shadow');
	shadow.on = 'T';
	shadow.type = SHAPE_STYLE.linkShadowType;
	if (true) {
		shadow.offset = '1.5pt,1.5pt';
		shadow.color = SHAPE_STYLE.linkShadowColor;
	} else {
		shadow.offset = '3pt,3pt';
		shadow.color = SHAPE_STYLE.linkColor;
		shadow.color2 = 'black';
	}
	return shadow;
};

DefaultZetaEdgeRenderer.prototype.fireTextPositionChanged = function(fromx, fromy, tox, toy) {
	var left = 0, top = 0;
	if (fromx == tox) {
		left = 0;
	} else if (fromx > tox) {
		left = Math.round((fromx - tox)/2) - SHAPE_STYLE.labelOffset;
	} else {
		left = Math.round((tox - fromx)/2) - SHAPE_STYLE.labelOffset;
	}
	if (fromy > toy) {
		top = Math.round((fromy - toy)/2) - SHAPE_STYLE.labelOffset - SHAPE_STYLE.linkWeight;
	} else {
		top = Math.round((toy - fromy)/2) - SHAPE_STYLE.labelOffset - SHAPE_STYLE.linkWeight;
	}
	this.text.style.left = left + "px";;
	this.text.style.top = top + "px";;	
};

DefaultZetaEdgeRenderer.prototype.setSourceXY = function(from) {
	var arr = pointToPx(this.ui.to);
	var fromx, fromy, tox, toy;
	fromx = from[0];
	fromy = from[1];
	tox = arr[0];
	toy = arr[1];
	this.ui.from = fromx + "px," + fromy + 'px';
	this.fireTextPositionChanged(fromx, fromy, tox, toy);
};

DefaultZetaEdgeRenderer.prototype.setTargetXY = function(to) {
	var arr = pointToPx(this.ui.from);
	var fromx, fromy, tox, toy;
	tox = to[0];
	toy = to[1];
	fromx = arr[0];
	fromy = arr[1];
	this.ui.to = tox + "px," + toy + 'px';
	this.fireTextPositionChanged(fromx, fromy, tox, toy);
};

DefaultZetaEdgeRenderer.prototype.setXY = function(from, to) {
	var fromx, fromy, tox, toy;
	fromx = from[0];
	fromy = from[1];	
	tox = to[0];
	toy = to[1];
	this.ui.from = fromx + "px," + fromy + 'px';
	this.ui.to = tox + "px," + toy + 'px';
	this.fireTextPositionChanged(fromx, fromy, tox, toy);
};

/* Container Cell Renderer */
function DefaultGroupZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 300;
		height = 100;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	
	var div = document.createElement('div');
	div.id = id;
	div.align = 'center';
	div.style.zIndex = SHAPE_STYLE.groupZIndex;
	div.style.position = 'absolute';
	div.style.left = x + "px";
	div.style.top = y + "px";

	var shape = document.createElement('v:RoundRect');
	shape.id = id;
	shape.style.width = width;
	shape.style.height = height;
	shape.style.padding = '5px';
	shape.setAttribute('arcsize', '9830f');
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	
	var fill = createFill();
	fill.type = 'gradient';
	shape.appendChild(fill);

	var img = document.createElement('img');
	img.src = '../images/zgraph/group48.png';
	img.border = 0;
	img.style.display = 'none';

	var span = document.createElement('span');
	span.innerText = text;
	
	div.appendChild(shape);
	div.appendChild(img);
	div.appendChild(document.createElement('br'));
	div.appendChild(span);

	this.shape = shape;
	this.text = span;
	this.icon = img;
	this.ui = div;
}
DefaultGroupZetaCellRenderer.prototype.ui = null;
DefaultGroupZetaCellRenderer.prototype.text = null;
DefaultGroupZetaCellRenderer.prototype.shape = null;
DefaultGroupZetaCellRenderer.prototype.icon = null;
DefaultGroupZetaCellRenderer.prototype.x = 0;
DefaultGroupZetaCellRenderer.prototype.y = 0;
DefaultGroupZetaCellRenderer.prototype.width = 1;
DefaultGroupZetaCellRenderer.prototype.height = 1;
DefaultGroupZetaCellRenderer.prototype.path = null;
DefaultGroupZetaCellRenderer.prototype.north = false;

DefaultGroupZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

DefaultGroupZetaCellRenderer.prototype.setExpanded = function(expaned) {
	if (expaned) {
		this.ui.style.zIndex = SHAPE_STYLE.groupZIndex;
		this.icon.style.display = 'none';
		this.shape.style.display = '';
	} else {
		this.ui.style.zIndex = SHAPE_STYLE.defaultZIndex;
		this.shape.style.display = 'none';
		this.icon.style.display = '';
	}
};


/* line */
function LineZetaCellRenderer(id, text, from, to) {
	this.from = from;
	this.to = to;

	var shape = document.createElement('v:shape');
	shape.id = id;
	//shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.setAttribute('strokeweight', '1');

	this.ui = shape;
	this.redraw();
}
LineZetaCellRenderer.prototype.ui = null;
LineZetaCellRenderer.prototype.text = null;
LineZetaCellRenderer.prototype.from = null;
LineZetaCellRenderer.prototype.to = null;

LineZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

LineZetaCellRenderer.prototype.setFillColor = function(color) {
	this.ui.setAttribute('fillcolor', color);
};

LineZetaCellRenderer.prototype.setStrokeColor = function(color) {
	this.ui.setAttribute('strokecolor', color);
};

LineZetaCellRenderer.prototype.redraw = function() {
	var from = this.from;
	var to = this.to;
	var fromx = from[0];
	var fromy = from[1];
	var tox = to[0];
	var toy = to[1];
	var w = Math.abs(fromx - tox);
	var h = Math.abs(fromy - toy);

	var path = new ZetaPath();	
  	path.moveTo(0,0);
	path.lineTo(0,h);
	path.lineTo(w,h);
	path.lineTo(w,0);
	path.end();
	
	this.ui.coordsize = w + ',' + h;
	this.ui.style.width = w;
	this.ui.style.height = h;
	this.ui.style.left = from[0] + "px";
	this.ui.style.top = from[1] + "px";	

	
	this.ui.setAttribute('path', path.getPath());
};