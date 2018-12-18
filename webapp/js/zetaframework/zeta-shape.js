/**
 * Zetaframework JavaScript Vml Shape by OO.
 *
 * (C) TopoView, 2008-04-08
 *
 * authro: kelers
 * 
 */
/* text */
function TextZetaCellRenderer(id, text, x, y) {
	this.x = x;
	this.y = y;
	
	var div = document.createElement('v:TextBox');
	div.id = id;
	div.style.zIndex = SHAPE_STYLE.textZIndex;
	div.style.position = 'absolute';
	div.style.left = x;
	div.style.top = y;
	div.innerText = text ? text : 'Text';

	this.ui = div;
	this.text = div;
}
TextZetaCellRenderer.prototype.ui = null;
TextZetaCellRenderer.prototype.text = null;
TextZetaCellRenderer.prototype.x = 0;
TextZetaCellRenderer.prototype.y = 0;

TextZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

/* href */
function HrefZetaCellRenderer(id, text, x, y) {
	this.x = x;
	this.y = y;
	
	var div = document.createElement('div');
	div.id = id;
	div.style.zIndex = SHAPE_STYLE.textZIndex;
	div.style.position = 'absolute';
	div.style.left = x;
	div.style.top = y;

	var textbox = document.createElement('a');
	textbox.href = 'http://www.sohu.com';
	textbox.target = '_blank';
	textbox.innerText = text ? text : 'Text';
	textbox.style.textDecoration = 'underline';
	textbox.style.padding = "3px";
	textbox.style.fontSize = '10pt';
	textbox.style.fontWeight = 'bold';
	
	div.appendChild(textbox);

	this.ui = div;
	this.text = textbox;
}
HrefZetaCellRenderer.prototype.ui = null;
HrefZetaCellRenderer.prototype.text = null;
HrefZetaCellRenderer.prototype.x = 0;
HrefZetaCellRenderer.prototype.y = 0;

HrefZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

/* label */
function LabelZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
	}
	if (typeof height == 'undefined') {
		height = 40;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.labelZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	
	shape.coordsize = width + ',' + height;
	shape.path = this.getPath();

	shape.setAttribute('fillcolor', '#c3d9ff');
	shape.setAttribute('strokecolor', '#c3d9ff');
	shape.setAttribute('strokeweight', '1');
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', 'white');
		fill.setAttribute('angle', '180');
		shape.appendChild(fill);
	}
	if (true) {
		var shadow = document.createElement('v:shadow');
		shadow.on = 't';
		shape.appendChild(shadow);
	}
	
	shape.align = 'center';
	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
LabelZetaCellRenderer.prototype.ui = null;
LabelZetaCellRenderer.prototype.text = null;
LabelZetaCellRenderer.prototype.x = 0;
LabelZetaCellRenderer.prototype.y = 0;
LabelZetaCellRenderer.prototype.width = 1;
LabelZetaCellRenderer.prototype.height = 1;
LabelZetaCellRenderer.prototype.path = null;

LabelZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

LabelZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

LabelZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	var height = h*0.8;
	path.moveTo(0,0);
	path.lineTo(w,0);
	path.lineTo(w,height);
	path.lineTo(0.4*w,height);
	path.lineTo(0.05*w,h);
	path.lineTo(0.2*w,height);
	path.lineTo(0,height);
	path.lineTo(0,0);
	path.close();
};

/* label */
function RoundLabelZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
	}
	if (typeof height == 'undefined') {
		height = 40;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.labelZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	
	shape.coordsize = width + ',' + height;
	shape.path = this.getPath();

	shape.setAttribute('fillcolor', '#c3d9ff');
	shape.setAttribute('strokecolor', '#c3d9ff');
	shape.setAttribute('strokeweight', '1');
	if (SHAPE_STYLE.gradient) {
		var fill = document.createElement('v:fill');
		fill.setAttribute('type', 'gradient');
		fill.setAttribute('color2', 'white');
		fill.setAttribute('angle', '180');
		shape.appendChild(fill);
	}
	if (true) {
		var shadow = document.createElement('v:shadow');
		shadow.on = 't';
		shape.appendChild(shadow);
	}
	
	shape.align = 'center';
	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
RoundLabelZetaCellRenderer.prototype.ui = null;
RoundLabelZetaCellRenderer.prototype.text = null;
RoundLabelZetaCellRenderer.prototype.x = 0;
RoundLabelZetaCellRenderer.prototype.y = 0;
RoundLabelZetaCellRenderer.prototype.width = 1;
RoundLabelZetaCellRenderer.prototype.height = 1;
RoundLabelZetaCellRenderer.prototype.path = null;

RoundLabelZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

RoundLabelZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

RoundLabelZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	var height = h*0.8;
	path.moveTo(0,0);
	path.lineTo(w,0);
	path.lineTo(w,height);
	path.lineTo(0.4*w,height);
	path.lineTo(0.05*w,h);
	path.lineTo(0.2*w,height);
	path.lineTo(0,height);
	path.lineTo(0,0);
	path.close();
};

/* round rect */
function RoundRectZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
		height = 40;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.width = height;

	var shape = document.createElement('v:RoundRect');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.style.padding = '5px';
	shape.setAttribute('arcsize', '9830f');
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	
	var span = document.createElement('span');
	span.innerText = text;
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
RoundRectZetaCellRenderer.prototype.ui = null;
RoundRectZetaCellRenderer.prototype.text = null;
RoundRectZetaCellRenderer.prototype.x = 0;
RoundRectZetaCellRenderer.prototype.y = 0;
RoundRectZetaCellRenderer.prototype.width = 1;
RoundRectZetaCellRenderer.prototype.height = 1;

RoundRectZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

/* rect */
function RectZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
		height = 40;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.width = height;

	var shape = document.createElement('v:Rect');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.setAttribute('arcsize', '9830f');
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
RectZetaCellRenderer.prototype.ui = null;
RectZetaCellRenderer.prototype.text = null;
RectZetaCellRenderer.prototype.x = null;
RectZetaCellRenderer.prototype.y = null;
RectZetaCellRenderer.prototype.width = 1;
RectZetaCellRenderer.prototype.height = 1;

RectZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

/* oval */
function OvalZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 60;
		height = 60;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.width = height;

	var shape = document.createElement('v:Oval');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.setAttribute('arcsize', '9830f');
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);

	var span = document.createElement('span');
	span.innerText = text;
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
OvalZetaCellRenderer.prototype.ui = null;
OvalZetaCellRenderer.prototype.text = null;
OvalZetaCellRenderer.prototype.x = null;
OvalZetaCellRenderer.prototype.y = null;
OvalZetaCellRenderer.prototype.width = 1;
OvalZetaCellRenderer.prototype.height = 1;

OvalZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

/* parallelogram */
function ParallZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
	}
	if (typeof height == 'undefined') {
		height = 40;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	shape.setAttribute('path', this.getPath());

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
ParallZetaCellRenderer.prototype.ui = null;
ParallZetaCellRenderer.prototype.text = null;
ParallZetaCellRenderer.prototype.x = 0;
ParallZetaCellRenderer.prototype.y = 0;
ParallZetaCellRenderer.prototype.width = 1;
ParallZetaCellRenderer.prototype.height = 1;
ParallZetaCellRenderer.prototype.path = null;
ParallZetaCellRenderer.prototype.north = false;

ParallZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

ParallZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

ParallZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	if (this.north) {
		path.moveTo(0.25*w,0);
		path.lineTo(w,0);
		path.lineTo(0.75*w,h);
		path.lineTo(0,h);
		path.lineTo(0.25*w,0);
	} else {
		path.moveTo(0.2*w,0);
		path.lineTo(w,0);
		path.lineTo(0.8*w,h);
		path.lineTo(0,h);
		path.lineTo(0.2*w,0);
	}
	path.close();	
};

/* hexagon */
function HexagonZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 80;
		height = 60;
	}

	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	shape.path = this.getPath();

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
HexagonZetaCellRenderer.prototype.ui = null;
HexagonZetaCellRenderer.prototype.text = null;
HexagonZetaCellRenderer.prototype.x = 0;
HexagonZetaCellRenderer.prototype.y = 0;
HexagonZetaCellRenderer.prototype.width = 1;
HexagonZetaCellRenderer.prototype.height = 1;
HexagonZetaCellRenderer.prototype.path = null;
HexagonZetaCellRenderer.prototype.north = false;

HexagonZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

HexagonZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

HexagonZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	if (this.north) {
		path.moveTo(0.5*w,0);
		path.lineTo(w,0.25*h);
		path.lineTo(w,0.75*h);
		path.lineTo(0.5*w,h);
		path.lineTo(0,0.75*h);
		path.lineTo(0,0.25*h);
	} else {
		path.moveTo(0.25*w,0);
		path.lineTo(0.75*w,0);
		path.lineTo(w,0.5*h);
		path.lineTo(0.75*w,h);
		path.lineTo(0.25*w,h);
		path.lineTo(0,0.5*h);
	}
	path.close();	
};


/* 
 * 菱形基本单元渲染
 * rhombus：菱形
 * 
 */
function RhombusZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 60;
		height = 60;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;	
	shape.coordsize = width + ',' + height;
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	shape.path = this.getPath();
	
	//-----其中文本将单独作为一个标签---//
	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);
	
	this.text = span;
	//----ui为图形单元---//
	this.ui = shape;
}
RhombusZetaCellRenderer.prototype.ui = null;
RhombusZetaCellRenderer.prototype.text = null;
RhombusZetaCellRenderer.prototype.x = 0;
RhombusZetaCellRenderer.prototype.y = 0;
RhombusZetaCellRenderer.prototype.width = 1;
RhombusZetaCellRenderer.prototype.height = 1;
RhombusZetaCellRenderer.prototype.path = null;
RhombusZetaCellRenderer.prototype.north = false;

RhombusZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

RhombusZetaCellRenderer.prototype.getPath = function() {
	var x=0;
	var y=0;
	var w=this.width;
	var h=this.height;
	var points = 'm '+Math.floor(x+w/2)+' '+y+' l '+(x+w)+' '+Math.floor(y+h/2)+' l '+
		Math.floor(x+w/2)+' '+(y+h)+' l '+x+' '+Math.floor(y+h/2);
	this.path = points+' x e';
	//---path: vmlPath--//
	return this.path;
};


/*
 * 三角形基本单元渲染
 * Triangle ： 三角形
 * id:三角形的id
 */
function TriangleZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 40;
		height = 60;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	shape.path = this.getPath();

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
TriangleZetaCellRenderer.prototype.ui = null;
TriangleZetaCellRenderer.prototype.text = null;
TriangleZetaCellRenderer.prototype.x = 0;
TriangleZetaCellRenderer.prototype.y = 0;
TriangleZetaCellRenderer.prototype.width = 1;
TriangleZetaCellRenderer.prototype.height = 1;
TriangleZetaCellRenderer.prototype.path = null;
TriangleZetaCellRenderer.prototype.direction = ZetaGraphConstants.DIRECTION_EAST;

TriangleZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

TriangleZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

/*
 * Triangle ： 三角形
 */
TriangleZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	var dir = this.direction;
	if(dir==ZetaGraphConstants.DIRECTION_NORTH) {
		path.moveTo(0,h);
		path.lineTo(0.5*w,0);
		path.lineTo(w,h);
	} else if(dir==ZetaGraphConstants.DIRECTION_SOUTH) {
		path.moveTo(0,0);
		path.lineTo(0.5*w,h);
		path.lineTo(w,0);
	} else if(dir==ZetaGraphConstants.DIRECTION_WEST) {
		path.moveTo(w,0);
		path.lineTo(0,0.5*h);
		path.lineTo(w,h);
	} else  {
		path.moveTo(0,0);
		path.lineTo(w,0.5*h);
		path.lineTo(0,h);
	}
	path.close();	
};

/*
 * Pentacle 五角星形
 */
function PentacleZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 60;
		height = 60;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('fillcolor', SHAPE_STYLE.fillColor);
	shape.path = this.getPath();

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
PentacleZetaCellRenderer.prototype.ui = null;
PentacleZetaCellRenderer.prototype.text = null;
PentacleZetaCellRenderer.prototype.x = 0;
PentacleZetaCellRenderer.prototype.y = 0;
PentacleZetaCellRenderer.prototype.width = 1;
PentacleZetaCellRenderer.prototype.height = 1;
PentacleZetaCellRenderer.prototype.path = null;
PentacleZetaCellRenderer.prototype.direction = ZetaGraphConstants.DIRECTION_EAST;

PentacleZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

PentacleZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

/*
 * Pentacle's raw path
 */
PentacleZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	/*path.moveTo(8,65);
	path.lineTo(72,65);
	path.lineTo(92,11);
	path.lineTo(112,65);
	path.lineTo(174,65);

	path.lineTo(122,100);
	path.lineTo(142,155);
	path.lineTo(92,121);
	path.lineTo(42,155);
	path.lineTo(60,100);*/
	
	path.moveTo(0,0.4*h);
	path.lineTo(0.5*w,0);
	path.lineTo(92,11);
	path.lineTo(112,65);
	path.lineTo(174,65);

	path.lineTo(122,100);
	path.lineTo(142,155);
	path.lineTo(92,121);
	path.lineTo(42,155);
	path.lineTo(60,100);
	path.close();	
};

/* actor */
function ActorZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 40;
		height = 60;
	}
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.position = 'absolute';
	shape.style.zIndex = SHAPE_STYLE.defaultZIndex;
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.path = this.getPath();

	var span = document.createElement('span');
	span.innerText = '';
	shape.appendChild(span);

	this.text = span;
	this.ui = shape;
}
ActorZetaCellRenderer.prototype.ui = null;
ActorZetaCellRenderer.prototype.text = null;
ActorZetaCellRenderer.prototype.x = 0;
ActorZetaCellRenderer.prototype.y = 0;
ActorZetaCellRenderer.prototype.width = 1;
ActorZetaCellRenderer.prototype.height = 1;
ActorZetaCellRenderer.prototype.path = null;

ActorZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

ActorZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

ActorZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	var width = w/3;
	path.moveTo(0,h);
	path.curveTo(0,3*h/5,0,2*h/5,w/2,2*h/5);
	path.curveTo(w/2-width,2*h/5,w/2-width,0,w/2,0);
	path.curveTo(w/2+width,0,w/2+width,2*h/5,w/2,2*h/5);
	path.curveTo(w,2*h/5,w,3*h/5,w,h);
	path.close();	
};

/*
 * 用于组单元的图形单元
 *  cloudy
 */ 
function CloudZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof text == 'undefined') {
		text = '';
	}
	if (typeof width == 'undefined') {
		width = 80;
		height = 60;
	}
	this.x = x;
	this.x = y;
	this.width = width;
	this.height = height;

	var shape = document.createElement('v:shape');
	shape.id = id;
	shape.style.zIndex = SHAPE_STYLE.shapeZIndex;	
	shape.style.position = 'absolute';
	shape.style.left = x;
	shape.style.top = y;
	shape.style.width = width;
	shape.style.height = height;
	shape.style.border = '1px solid transparent';
	shape.coordsize = width + ',' + height;
	shape.path = this.getPath();
	shape.setAttribute('fillcolor', '#cdeb8b');
	shape.setAttribute('strokecolor', '#cdeb8b');
	shape.setAttribute('strokeweight', '1');
	
	var textBox = document.createElement('v:TextBox');
	textBox.inset = "0,15pt,0,0";
	textBox.style.textAlign = 'center';
	textBox.style.border = '0px solid transparent';
	textBox.innerText = '';
	/*var textBox = document.createElement('span');
	textBox.style.textAlign = 'center';
	textBox.innerText = 'niejun';*/
	shape.appendChild(textBox);

	this.text = textBox;
	this.ui = shape;
}
CloudZetaCellRenderer.prototype.ui = null;
CloudZetaCellRenderer.prototype.text = null;
CloudZetaCellRenderer.prototype.x = 0;
CloudZetaCellRenderer.prototype.y = 0;
CloudZetaCellRenderer.prototype.width = 1;
CloudZetaCellRenderer.prototype.height = 1;
CloudZetaCellRenderer.prototype.path = null;

CloudZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

CloudZetaCellRenderer.prototype.setText = function(text) {
	this.text.innerText = text;
};

CloudZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

CloudZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	path.moveTo(0.25*w,0.25*h);
	path.curveTo(0.05*w,0.25*h,0,0.5*h,0.16*w,0.55*h);
	path.curveTo(0,0.66*h,0.18*w,0.9*h,0.31*w,0.8*h);
	path.curveTo(0.4*w,h,0.7*w,h,0.8*w,0.8*h);
	path.curveTo(w,0.8*h,w,0.6*h,0.875*w,0.5*h);
	path.curveTo(w,0.3*h,0.8*w,0.1*h,0.625*w,0.2*h);
	path.curveTo(0.5*w,0.05*h,0.3*w,0.05*h,0.25*w,0.25*h);
	path.close();	
};

/* FoldLineZetaEdgeRenderer */
function FoldLineZetaEdgeRenderer(id, text, from, to) {
	this.from = from;
	this.to = to;

	var line = document.createElement('v:shape');
	line.id = id;
	line.style.zIndex = SHAPE_STYLE.edgeZIndex;
	line.style.position = 'absolute';
	line.style.border = '0';
	line.filled = "false";
	line.strokeweight = SHAPE_STYLE.linkWeight;
	line.strokecolor = SHAPE_STYLE.linkColor;

	var span = document.createElement('span');
	span.style.position = 'relative';
	span.style.left = 0;
	span.style.top = 0;
	span.style.padding = '1px 2px';
	span.style.whiteSpace = 'nowrap';	

	line.appendChild(span);

	if (SHAPE_STYLE.linkShadow) {
		this.shadow = this.createShadow();
		line.appendChild(this.shadow);
	}
	
	this.ui = line;
	this.text = span;
}
FoldLineZetaEdgeRenderer.prototype.ui = null;
FoldLineZetaEdgeRenderer.prototype.text = null;
FoldLineZetaEdgeRenderer.prototype.from = null;
FoldLineZetaEdgeRenderer.prototype.to = null;
FoldLineZetaEdgeRenderer.prototype.width = 1;
FoldLineZetaEdgeRenderer.prototype.height = 1;
FoldLineZetaEdgeRenderer.prototype.shadow = null;

FoldLineZetaEdgeRenderer.prototype.getComponent = function() {
	return this.ui;
};

FoldLineZetaEdgeRenderer.prototype.redraw = function() {
	var from = this.from;
	var to = this.to;
	var fromx = from[0];
	var fromy = from[1];
	var tox = to[0];
	var toy = to[1];
	var w = Math.abs(fromx - tox);
	var h = Math.abs(fromy - toy);

	this.width = w;
	this.height = h;
	this.ui.coordsize = w + ',' + h;
	this.ui.style.width = w;
	this.ui.style.height = h;	

	var left = Math.floor(w/2);
	var top = 0;
	var path = new ZetaPath();	
	if (fromy <= toy) {
		if (w < 60) {
			top = Math.floor(h/2);
		} else {
			top = -12;
		}
		if (fromx <= tox ) {
		  	path.moveTo(0,0);
			path.lineTo(w,0);
			path.lineTo(w,h);
			this.ui.style.left = fromx;
		  	this.ui.style.top = fromy;
		} else {
		  	path.moveTo(0,h);
			path.lineTo(0,0);
			path.lineTo(w,0);
			this.ui.style.left = tox;
		  	this.ui.style.top = fromy;
		}
	} else {
		if (w < 60) {
			top = Math.floor(h/2);
		} else {
			top = h - 12;
		}
		if (fromx <= tox) {
		  	path.moveTo(w,0);
			path.lineTo(w,h);
			path.lineTo(0,h);
			this.ui.style.left = fromx;
		  	this.ui.style.top = toy;
		} else {
		  	path.moveTo(0,0);
			path.lineTo(0,h);
			path.lineTo(w,h);
			this.ui.style.left = tox;
		  	this.ui.style.top = toy;
		}
	}
	path.end();
	this.ui.path = path.getPath();
	this.text.style.left = left;
	this.text.style.top = top;
};

FoldLineZetaEdgeRenderer.prototype.createShadow = function() {
	var shadow = document.createElement('v:shadow');
	shadow.on = 'T';
	shadow.type = SHAPE_STYLE.linkShadowType;
	shadow.offset = '1.5pt,1.5pt';
	shadow.color = SHAPE_STYLE.linkShadowColor;
	return shadow;
};
FoldLineZetaEdgeRenderer.prototype.setSourceXY = function(from) {
	this.from = from;
	this.redraw();
};

FoldLineZetaEdgeRenderer.prototype.setTargetXY = function(to) {
	this.to = to;
	this.redraw();
};

FoldLineZetaEdgeRenderer.prototype.setXY = function(from, to) {	
	this.from = from;
	this.to = to;
	this.redraw();
};

/* HorizonalZetaEdgeRenderer */
function HorizonalZetaEdgeRenderer(id, text, from, to) {
	this.from = from;
	this.to = to;

	var line = document.createElement('v:shape');
	line.id = id;
	line.style.zIndex = SHAPE_STYLE.edgeZIndex;
	line.style.position = 'absolute';
	line.style.padding = '5px';
	line.style.border = '0';
	line.filled = "false";
	line.strokeweight = SHAPE_STYLE.linkWeight;
	line.strokecolor = SHAPE_STYLE.linkColor;

	var span = document.createElement('span');
	span.style.position = 'relative';
	span.style.left = 0;
	span.style.top = 0;
	span.style.padding = '1px 2px';
	span.style.whiteSpace = 'nowrap';	

	line.appendChild(span);

	if (SHAPE_STYLE.linkShadow) {
		this.shadow = this.createShadow();
		line.appendChild(this.shadow);
	}
	
	this.ui = line;
	this.text = span;
	//this.redraw();
	//this.ui.path = this.getPath();
}
HorizonalZetaEdgeRenderer.prototype.ui = null;
HorizonalZetaEdgeRenderer.prototype.text = null;
HorizonalZetaEdgeRenderer.prototype.from = null;
HorizonalZetaEdgeRenderer.prototype.to = null;
HorizonalZetaEdgeRenderer.prototype.width = 0;
HorizonalZetaEdgeRenderer.prototype.height = 0;
HorizonalZetaEdgeRenderer.prototype.shadow = 0;

HorizonalZetaEdgeRenderer.prototype.getComponent = function() {
	return this.ui;
};

HorizonalZetaEdgeRenderer.prototype.redraw = function() {
	var from = this.from;
	var to = this.to;

	var w = Math.abs(from[0] - to[0]);
	var h = Math.abs(from[1] - to[1]);
	this.width = w;
	this.height = h;
 	this.ui.coordsize = w + ',' + h;
	this.ui.style.width = w;
	this.ui.style.height = h;
	
	if (from[1] <= to[1]) {
		if (from[0] <= to[0] ) {
			this.ui.style.left = from[0];
		  	this.ui.style.top = from[1];		
		} else {
			this.ui.style.left = to[0];
		  	this.ui.style.top = from[1];		
		}
	} else {
		if (from[0] <= to[0] ) {
			this.ui.style.left = from[0];
		  	this.ui.style.top = to[1];		
		} else {
			this.ui.style.left = to[0];
		  	this.ui.style.top = to[1];		
		}	
	}
};

HorizonalZetaEdgeRenderer.prototype.getPath = function() {
	var from = this.from;
	var to = this.to;
	var w = this.width;
	var h = this.height;
	var c = Math.round(w/2);
	var distance = 20;
	
	if (w > 40) {
		if (h > 40) {
			distance = 20;
		} else {
			distance = Math.round(h/2);
		}
	} else {
		if (h > 40) {
			distance = Math.round(w/2);
		} else {
			distance = Math.round(Math.min(w,h)/2);
		}	
	}
	
	var sb = new Zeta$StringBuffer();
	sb.append('m');
		
	if (from[1] <= to[1]) {
		if (from[0] <= to[0]) {
			sb.append(0);
			sb.append(",");
			sb.append(0);
			sb.append(" ");
		
			sb.append('l');
			sb.append(c-distance);
			sb.append(",");
			sb.append(0);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(0);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(0);
			
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(distance);		
			
			sb.append(' l');
			sb.append(c);
			sb.append(",");
			sb.append(h-distance);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(h);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h);
			
			sb.append(",");
			sb.append(c+distance);
			sb.append(",");
			sb.append(h);		
			
			sb.append(' l');
			sb.append(w);
			sb.append(",");
			sb.append(h);
		} else {
			sb.append(w);
			sb.append(",");
			sb.append(0);
			sb.append(" ");
		
			sb.append('l');
			sb.append(c+distance);
			sb.append(",");
			sb.append(0);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(0);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(0);
			
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(distance);		
			
			sb.append(' l');
			sb.append(c);
			sb.append(",");
			sb.append(h-distance);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(h);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h);
			
			sb.append(",");
			sb.append(c-distance);
			sb.append(",");
			sb.append(h);		
			
			sb.append(' l');
			sb.append(0);
			sb.append(",");
			sb.append(h);		
		}
	} else {
		if (from[0] <= to[0]) {
			sb.append(0);
			sb.append(",");
			sb.append(h);
			sb.append(" ");
		
			sb.append('l');
			sb.append(c-distance);
			sb.append(",");
			sb.append(h);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(h);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h);
			
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h - distance);		
			
			sb.append(' l');
			sb.append(c);
			sb.append(",");
			sb.append(distance);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(0);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(0);
			
			sb.append(",");
			sb.append(c+distance);
			sb.append(",");
			sb.append(0);		
			
			sb.append(' l');
			sb.append(w);
			sb.append(",");
			sb.append(0);		
		} else {
			sb.append(w);
			sb.append(",");
			sb.append(h);
			sb.append(" ");
		
			sb.append('l');
			sb.append(c+distance);
			sb.append(",");
			sb.append(h);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(h);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h);
			
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(h - distance);		
			
			sb.append(' l');
			sb.append(c);
			sb.append(",");
			sb.append(distance);
			
			sb.append(' c');
			sb.append(c);
			sb.append(",");
			sb.append(0);
		
			sb.append(",");
			sb.append(c);
			sb.append(",");
			sb.append(0);
			
			sb.append(",");
			sb.append(c-distance);
			sb.append(",");
			sb.append(0);		
			
			sb.append(' l');
			sb.append(0);
			sb.append(",");
			sb.append(0);		
		}
	}

	sb.append(" ");	
	sb.append('e');
	return sb.toString();
};

HorizonalZetaEdgeRenderer.prototype.createShadow = function() {
	var shadow = document.createElement('v:shadow');
	shadow.on = 'T';
	shadow.type = SHAPE_STYLE.linkShadowType;
	shadow.offset = '1.5pt,1.5pt';
	shadow.color = SHAPE_STYLE.linkShadowColor;
	return shadow;
};
HorizonalZetaEdgeRenderer.prototype.setSourceXY = function(from) {
	this.from = from;
	this.redraw();
	this.ui.path = this.getPath();	

	var fromx, fromy, tox, toy;
	var left = 0, top = 0;

	fromx = from[0];
	fromy = from[1];

	var arr1 = this.to;
	tox = arr1[0];
	toy = arr1[1];
	if (fromx > tox) {
		left = Math.round((fromx - tox)/2);
	} else {
		left = Math.round((tox - fromx)/2);
	}
	if (left < 0) {
		left = 2;
	}
	if (fromy > toy) {
		top = Math.round((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		top = Math.round((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}
	top = top - 3;

	this.text.style.left = left;
	this.text.style.top = top;
};

HorizonalZetaEdgeRenderer.prototype.setTargetXY = function(to) {
	this.to = to;
	this.redraw();
	this.ui.path = this.getPath();

	var fromx, fromy, tox, toy;
	var left = 0, top = 0;
	tox = to[0];
	toy = to[1];

	var arr1 = this.from;
	fromx = arr1[0];
	fromy = arr1[1];
	if (fromx > tox) {
		left = Math.round((fromx - tox)/2);
	} else {
		left = Math.round((tox - fromx)/2);
	}
	if (left < 0) {
		left = 2;
	}
	if (fromy > toy) {
		top = Math.round((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		top = Math.round((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}
	top = top - 3;

	this.text.style.left = left;
	this.text.style.top = top;
};

HorizonalZetaEdgeRenderer.prototype.setXY = function(from, to) {
	this.from = from;
	this.to = to;
	this.redraw();
	this.ui.path = this.getPath();	
	
	var fromx, fromy, tox, toy;
	var left = 0, top = 0;
	
	fromx = from[0];
	fromy = from[1];

	tox = to[0];
	toy = to[1];

	if (fromx > tox) {
		left = Math.round((fromx - tox)/2);
	} else {
		left = Math.round((tox - fromx)/2);
	}
	if (left < 0) {
		left = 2;
	}
	if (fromy > toy) {
		top = Math.round((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		top = Math.round((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}
	top = top - 3;

	this.text.style.left = left;
	this.text.style.top = top;	
};

/* Arrow Edge Renderer */
function ArrowZetaEdgeRenderer(id, text, from, to) {
	var line = document.createElement('v:line');
	line.id = id;
	var fromx = from[0];
	var fromy = from[1];
	var tox = to[0];
	var toy = to[1];

	line.from = from[0] + 'px,' + from[1] + 'px';
	line.to = to[0] + 'px,' + to[1] + 'px';
	line.strokeweight = SHAPE_STYLE.linkWeight;
	line.strokecolor = SHAPE_STYLE.linkColor;

	var span = document.createElement('span');
	span.style.position = 'relative';
	span.className = 'edgeLabel';

	var diff = 0;
	if (fromx > tox) {
		diff = parseInt((fromx - tox)/2) - 2*SHAPE_STYLE.labelOffset;
	} else {
		diff = parseInt((tox - fromx)/2) - 2*SHAPE_STYLE.labelOffset;
	}
	if (diff < 0) {
		diff = 2;
	}

	span.style.left = diff;
	if (fromy > toy) {
		span.style.top = parseInt((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		span.style.top = parseInt((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}

	line.appendChild(span);

	if (SHAPE_STYLE.linkShadow) {
		var shadow = this.createShadow();
		line.appendChild(shadow);
	}

	this.text = span;
	this.ui = line;
}
ArrowZetaEdgeRenderer.prototype.ui = null;
ArrowZetaEdgeRenderer.prototype.text = null;
ArrowZetaEdgeRenderer.prototype.getComponent = function() {
	return this.ui;
};
ArrowZetaEdgeRenderer.prototype.createShadow = function() {
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
ArrowZetaEdgeRenderer.prototype.setSourceXY = function(from) {
	var fromx, fromy, tox, toy;
	var left = 0, top = 0;

	fromx = from[0];
	fromy = from[1];

	var arr1 = pointToPx(this.ui.to);
	tox = arr1[0];
	toy = arr1[1];
	if (fromx > tox) {
		left = parseInt((fromx - tox)/2) - 2*SHAPE_STYLE.labelOffset;
	} else {
		left = parseInt((tox - fromx)/2) - 2*SHAPE_STYLE.labelOffset;
	}
	if (left < 0) {
		left = 2;
	}
	if (fromy > toy) {
		top = parseInt((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		top = parseInt((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}
	top = top - 3;

	this.ui.from = fromx + "px," + fromy + 'px';
	this.text.style.left = left;
	this.text.style.top = top;
};

ArrowZetaEdgeRenderer.prototype.setTargetXY = function(to) {
	var fromx, fromy, tox, toy;
	var left = 0, top = 0;
	tox = to[0];
	toy = to[1];

	var arr1 = pointToPx(this.ui.from);
	fromx = arr1[0];
	fromy = arr1[1];
	if (fromx > tox) {
		left = parseInt((fromx - tox)/2) - 2*SHAPE_STYLE.labelOffset;
	} else {
		left = parseInt((tox - fromx)/2) - 2*SHAPE_STYLE.labelOffset;
	}
	if (left < 0) {
		left = 2;
	}
	if (fromy > toy) {
		top = parseInt((fromy - toy)/2) - SHAPE_STYLE.labelOffset;
	} else {
		top = parseInt((toy - fromy)/2) - SHAPE_STYLE.labelOffset;
	}
	top = top - 3;

	this.ui.to = tox + "px," + toy + 'px';
	this.text.style.left = left;
	this.text.style.top = top;
};
ArrowZetaEdgeRenderer.prototype.setTextForeground = function(color) {
	this.text.style.color = this.foregroundColor = color;
};
ArrowZetaEdgeRenderer.prototype.setTextBackground = function(color) {
	this.text.style.backgroundColor = color;
};
ArrowZetaEdgeRenderer.prototype.setEdgeForeground = function(color) {
	this.ui.strokecolor = color;
};
ArrowZetaEdgeRenderer.prototype.setText = function(text) {
	this.text.innerText = text;
};

/*
 * Polyline：多叉线
 * Polyline Relation Edge Renderer
 */ 
function PolylineZetaEdgeRenderer(id, text, from, to) {
	this.from = from;
	this.to = to;

	var line = document.createElement('v:shape');
	line.id = id;
	line.style.zIndex = SHAPE_STYLE.edgeZIndex;
	line.style.position = 'absolute';
	line.style.border = '0';
	line.filled = "false";
	line.strokeweight = SHAPE_STYLE.linkWeight;
	line.strokecolor = SHAPE_STYLE.linkColor;

	var span = document.createElement('span');
	span.style.position = 'relative';
	span.style.left = 0;
	span.style.top = 0;
	span.style.padding = '1px 2px';
	span.style.whiteSpace = 'nowrap';	

	line.appendChild(span);

	if (SHAPE_STYLE.linkShadow) {
		this.shadow = this.createShadow();
		line.appendChild(this.shadow);
	}
	
	this.ui = line;
	this.text = span;
}
PolylineZetaEdgeRenderer.prototype.ui = null;
PolylineZetaEdgeRenderer.prototype.text = null;
PolylineZetaEdgeRenderer.prototype.from = null;
PolylineZetaEdgeRenderer.prototype.to = null;
PolylineZetaEdgeRenderer.prototype.width = 1;
PolylineZetaEdgeRenderer.prototype.height = 1;
PolylineZetaEdgeRenderer.prototype.shadow = null;

PolylineZetaEdgeRenderer.prototype.getComponent = function() {
	return this.ui;
};

PolylineZetaEdgeRenderer.prototype.redraw = function() {
	var from = this.from;
	var to = this.to;
	var fromx = from[0];
	var fromy = from[1];
	var tox = to[0];
	var toy = to[1];
	var w = Math.abs(fromx - tox);
	var h = Math.abs(fromy - toy);

	this.width = w;
	this.height = h;
	this.ui.coordsize = w + ',' + h;
	this.ui.style.width = w;
	this.ui.style.height = h;

	var left = Math.floor(w/2);
	var top = 0;
	var path = new ZetaPath();	
	if (fromy <= toy) {
		if (w < 60) {
			top = Math.floor(h/2);
		} else {
			top = -12;
		}
		if (fromx <= tox ) {
		  	path.moveTo(0,0);
			path.curveTo(0,0,0.4*w,0.05*h,0.5*w,0.5*h);
			path.curveTo(0.5*w,0.5*h,0.55*w,0.95*h,w,h);
			this.ui.style.left = fromx;
		  	this.ui.style.top = fromy;
		} else {
		  	path.moveTo(0,h);
			path.curveTo(0,h,0.4*w,0.95*h,0.5*w,0.5*h);
			path.curveTo(0.5*w,0.5*h,0.55*w,0.05*h,w,0);
			this.ui.style.left = tox;
		  	this.ui.style.top = fromy;
		}
	} else {
		if (w < 60) {
			top = Math.floor(h/2);
		} else {
			top = h - 12;
		}
		if (fromx <= tox) {
		  	path.moveTo(w,0);
		  	path.curveTo(w,0,0.4*w,0.05*h,0.5*w,0.5*h);
			path.curveTo(0.5*w,0.5*h,0.55*w,0.95*h,0,h);
			this.ui.style.left = fromx;
		  	this.ui.style.top = toy;
		} else {
		  	path.moveTo(0,0);
		  	path.curveTo(0,0,0.4*w,0.05*h,0.5*w,0.5*h);
			path.curveTo(0.5*w,0.5*h,0.55*w,0.95*h,w,h);
			this.ui.style.left = tox;
		  	this.ui.style.top = toy;
		}
	}
	path.end();
	this.ui.path = path.getPath();
	this.text.style.left = left;
	this.text.style.top = top;
};

PolylineZetaEdgeRenderer.prototype.createShadow = function() {
	var shadow = document.createElement('v:shadow');
	shadow.on = 'T';
	shadow.type = SHAPE_STYLE.linkShadowType;
	shadow.offset = '1.5pt,1.5pt';
	shadow.color = SHAPE_STYLE.linkShadowColor;
	return shadow;
};
PolylineZetaEdgeRenderer.prototype.setSourceXY = function(from) {
	this.from = from;
	this.redraw();
};

PolylineZetaEdgeRenderer.prototype.setTargetXY = function(to) {
	this.to = to;
	this.redraw();
};

PolylineZetaEdgeRenderer.prototype.setXY = function(from, to) {	
	this.from = from;
	this.to = to;
	this.redraw();
};

/* Paral Group */
function ParallGroupZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 400;
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
	div.style.left = x;
	div.style.top = y;

	var shape = document.createElement('v:shape');
	shape.style.width = width;
	shape.style.height = height;
	shape.coordsize = width + ',' + height;
	shape.setAttribute('path', this.getPath());
	
	var fill = createFill();
	fill.type = 'gradient';
	shape.appendChild(fill);

	shape.appendChild(createExtrusion());
	
	var img = document.createElement('img');
	img.src = '../images/zgraph/parallGroup48.png';
	img.border = 0;
	img.style.display = 'none';		

	var span = document.createElement('span');
	span.innerText = text;
	
	div.appendChild(shape);
	div.appendChild(img);
	div.appendChild(document.createElement('br'));
	div.appendChild(span);

	this.icon = img;
	this.shape = shape;
	this.text = span;
	this.ui = div;
}
ParallGroupZetaCellRenderer.prototype.ui = null;
ParallGroupZetaCellRenderer.prototype.text = null;
ParallGroupZetaCellRenderer.prototype.icon = null;
ParallGroupZetaCellRenderer.prototype.shape = null;
ParallGroupZetaCellRenderer.prototype.x = 0;
ParallGroupZetaCellRenderer.prototype.y = 0;
ParallGroupZetaCellRenderer.prototype.width = 1;
ParallGroupZetaCellRenderer.prototype.height = 1;
ParallGroupZetaCellRenderer.prototype.path = null;
ParallGroupZetaCellRenderer.prototype.north = false;

ParallGroupZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

ParallGroupZetaCellRenderer.prototype.setExpanded = function(expaned) {
	if (expaned) {
		this.ui.style.zIndex = SHAPE_STYLE.groupZIndex;
		this.icon.style.display = 'none';
		this.shape.style.display = '';
	} else {
		this.ui.style.zIndex = SHAPE_STYLE.defaultZIndex;
		this.icon.style.display = '';
		this.shape.style.display = 'none';
	}
};

ParallGroupZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

ParallGroupZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	if (this.north) {
		path.moveTo(0.25*w,0);
		path.lineTo(w,0);
		path.lineTo(0.75*w,h);
		path.lineTo(0,h);
		path.lineTo(0.25*w,0);
	} else {
		path.moveTo(0.2*w,0);
		path.lineTo(w,0);
		path.lineTo(0.8*w,h);
		path.lineTo(0,h);
		path.lineTo(0.2*w,0);
	}
	path.close();	
};

/* EllipseGroupZetaCellRenderer */
function EllipseGroupZetaCellRenderer(id, text, x, y, width, height) {
	if (typeof width == 'undefined') {
		width = 300;
		height = 150;
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
	div.style.left = x;
	div.style.top = y;

	var shape = document.createElement('v:Oval');
	shape.id = id;
	shape.style.width = width;
	shape.style.height = height;
	shape.setAttribute('arcsize', '9830f');
	
	var fill = createFill();
	fill.type = 'gradient';
	shape.appendChild(fill);
	
	var img = document.createElement('img');
	img.border = 0;
	img.src = '../images/zgraph/ellipseGroup48.png';
	img.style.display = 'none';

	shape.appendChild(createExtrusion());

	var span = document.createElement('span');
	span.innerText = text;
	
	div.appendChild(shape);
	div.appendChild(img);
	div.appendChild(document.createElement('br'));
	div.appendChild(span);

	this.shape = shape;
	this.icon = img;
	this.text = span;
	this.ui = div;
}
EllipseGroupZetaCellRenderer.prototype.ui = null;
EllipseGroupZetaCellRenderer.prototype.text = null;
EllipseGroupZetaCellRenderer.prototype.icon = null;
EllipseGroupZetaCellRenderer.prototype.shape = null;
EllipseGroupZetaCellRenderer.prototype.x = 0;
EllipseGroupZetaCellRenderer.prototype.y = 0;
EllipseGroupZetaCellRenderer.prototype.width = 1;
EllipseGroupZetaCellRenderer.prototype.height = 1;
EllipseGroupZetaCellRenderer.prototype.path = null;
EllipseGroupZetaCellRenderer.prototype.north = false;

EllipseGroupZetaCellRenderer.prototype.getComponent = function() {
	return this.ui;
};

EllipseGroupZetaCellRenderer.prototype.setExpanded = function(expaned) {
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

EllipseGroupZetaCellRenderer.prototype.getPath = function() {
	if (this.path == null)
		this.path = new ZetaPath();
	this.redrawPath(this.path, this.x, this.y, this.width, this.height);
	return this.path.getPath();
};

EllipseGroupZetaCellRenderer.prototype.redrawPath = function(path,x,y,w,h) {
	path.moveTo(0.2*w,0);
	path.lineTo(w,0);
	path.lineTo(0.8*w,h);
	path.lineTo(0,h);
	path.lineTo(0.2*w,0);
	path.close();	
};

function createFill() {
	var fill = document.createElement('v:fill');
	fill.setAttribute('color', SHAPE_STYLE.fillColor);
	fill.setAttribute('color2', SHAPE_STYLE.fillColor2);
	fill.setAttribute('angle', '180');	
	//fill.setAttribute('focus', '100%');
	return fill;
}

function createStroke() {
	var stroke = document.createElement('v:stroke');
	return stroke;
}

function createExtrusion() {
	var extrusion = document.createElement('v:extrusion');
	extrusion.on = 't';
	//extrusion.metal = 't';
	//extrusion.color = '#000';
	extrusion.backdepth = '20pt';
	//extrusion.diffusity = '1.2';
	extrusion.skewangle = 90;
	extrusion.brightness = '0.2';
	//extrusion.rotationangle = '90,180';
	return extrusion;
}