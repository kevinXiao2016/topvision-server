var vnet = {};

var CONSTANT = {
	radius = 30	
}


/*************************
 ******** VERTEX *********
 *************************/
vnet.vertex = function(name,children,entityType,title,position,parent){
	this.name = name;
	this.parent = parent;
	this.children = children;
	this.entityType = entityType;
	this.title = title;
	this.id = window.sequence++;
	this.jid = 'cell'+id;
	this.data = null;
	this.position = position;
	this.ui = new VertexRender({name:name,x:position.x,y:position.y});
}
vnet.vertex.prototype.jid = null//JQUERY ID
vnet.vertex.prototype.setName = function(){}
vnet.vertex.prototype.getName = function(){}
vnet.vertex.prototype.setChildren = function(){}
vnet.vertex.prototype.getChildren = function(){}
vnet.vertex.prototype.setEntityType = function(){}
vnet.vertex.prototype.getEntityType = function(){}
vnet.vertex.prototype.setTitile = function(){}
vnet.vertex.prototype.getTitle = function(){}
vnet.vertex.prototype.setData = function(){}
vnet.vertex.prototype.getData = function(){}
vnet.vertex.prototype.setPosition = function(){}
vnet.vertex.prototype.getPosition = function(){}
vnet.vertex.prototype.upLink = null; //ui,htmlElement
vnet.vertex.prototype.downLink = new Array();

vnet.vertex.prototype.remove = function(){
	$(this.ui).hide('slow',function(){
		$this.remove();
	});
}

/*************************
 ******** LINE ***********
 *************************/
vnet.line = function(name,property,start,end){
	this.name = name;
	this.property = property;
	this.start = start;
	this.end = end;
	this.ui = new vnet.LineRender({start:start,end:end,length:length,name:name});
}

vnet.line.prototype.setName = function(){}
vnet.line.prototype.getName = function(){}
vnet.line.prototype.setProperty = function(){}
vnet.line.prototype.getProperty = function(){}
vnet.line.prototype.setStart = function(){}
vnet.line.prototype.getStart = function(){}
vnet.line.prototype.setStart = function(){}
vnet.line.prototype.getStart = function(){}

vnet.line.prototype.remove = function(){
	$(this.ui).hide('slow',function(){
		$this.remove();
	});
}


/*******************************
 ******** CORE MANAGER *********
 *******************************/
vnet.manager = function(canvas,source){
	this.canvas = canvas;
	this.source = source;
}

vnet.manager.prototype.drawRootNode = function(){
	var o = this;
	var root = this.source.root
	//this.expandNode(root)
	//TODO
	var xo = 300;
	var yo = 300;
	var ox = 300;
	var oy = 300;
	//根节点单独处理
	var count = root.children.length 
	$.each(root.children,function(i,n){
		var position = o.getLocation((Math.random()-0.5)*2*CONSTANT.radius,xo,yo)
		o.drawEntity(n,position)
	})
} 



vnet.manager.prototype.expandNode = function(vertex){
	try{
		var o = this;
		if(vertex.parent){
			//HIDE BROTHERS
			$.each(vertex.parent.children.filter(vertex).children,function(i,v){
				//TODO 后期将下面一句移入remove方法中
				o.fire('iRemoved',v)
				v.remove();
			})
		}
		//START TO EXPAND
		
	}catch(e){
		//NOTHING TO DO
	}
}

vnet.manager.destoryAllChild = function(v){
	var o = this;
	$.each(v.children,function(i,n){
		n.remove()
		n.upLink.remove()
		o.destoryAllChild(n)
	})
	
	
}


vnet.manager.drawVertex = function(x,y,vertex){
	var v = document.createElement("div")
	v.style.left = x
	v.style.top = y
	v.style.postion = 'absolute'
	v.style.backgroundImage = 'url(/epon/network/olt.png) no-reapt !important'
	v.className = 'cellnode'
	this.canvas.fireEvent('vertexCreated',vertex)
}

vnet.manager.prototype.drawEntity = function(position,vertex){
	var v = document.createElement("div")
	v.id = vertex.jid;
	v.style.left = position.x;
	v.style.top = position.y;
	var image = document.createElement("img")
	image.src = '/epon/image/olt.png'
	image.width = '50px'
	image.height = '50px'
	image.style.position = 'absolute'
	image.style.left = '0px'
	image.style.top = '0px'
	v.appenChild(image)
	var span = document.createElement("span")
	span.innerHTML = 'HELLOWORLD'
	span.style.position = 'absolute'
	span.style.left = '0px'
	span.style.top = '60px'	
	v.appendChild(span)
	//ADD UI
	vertex.ui = v;
}

vnet.magager.drawLine = function(start,end) {
	return new vnet.line('link','property',start,end);
}

vnet.manager.fire = function(eventName,obj){
	this.canvas.fireEvent(eventName,obj)
}

/***************************************************
not nesseray,note the rotation can resolve problem
***************************************************/
vnet.manager.addEventListener = function(){
	var o = this;//a closure
	
	$(this.canvas).bind('vertexCreated',function(e,vertex){
		//console.log('a vertex has been created,a line need to be created to bridge')
		var pp = $(vertex.parent.ui).offset();
		var pc = $(vertex.ui).offset();
		//TODO need a line id....只确定每个顶点的上行是否足够，是否需要定义下行，但是下行可能一对多
		vertex.upLink = this.drawLine(pp,pc)
	})
	
	//TODO obj对象能正常传过来么,是否应该以数组的形式
	$(this.canvas).bind('iRemoved',function(e,obj){
		//STEP 1
		obj.upLink.remove()
		//STEP 2
		o.destoryAllChild(obj)
	})
	
	
	
}




/***************************************************
 not nesseray,note the rotation can resolve problem
 ***************************************************/
vnet.manager.computeAnchor = function(vertex){
	var perent = vertex.parent;
	var postion1 = $(vertex.ui).offset();
	var postion2 = $(vertex.perent.ui).offset();
	var posX = postion1.x  - postion2.x;
	var posY = postion1.y  - postion2.y;
	if(posY == 0){
		if(posX == 0) {
			return 0;
		}else {
			if(posX>0){
				return 0;
			}else{
				return 180;
			}
		}
	}else{
		if(posX == 0) {
			if(posY > 0){
				return 90;
			}else{
				return -90;
			}
		}else{
			var k = posX/posY;
			return Math.round(Math.atan(k) / Math.PI /2 * 360) ;
		}
	}
}




/***************************
  		 RENDER
 **************************/
vnet.LineRender = function(obj){
	var start = obj.start;
	var end = obj.end;
	var line = document.createElement('v:line');
	line.style.position = 'absolute';
	line.style.padding = '5px';
	line.style.border = '0';
	//TODO NEED CONSTANT
	line.strokecolor = 'black';
	line.strokeweight = 2;
	line.from = start.left + 'px,' + start.top + 'px';
	line.to = end.left + 'px,' + end.top + 'px';
	this.canvas.appendChild(line);
	return line;
}

vnet.VertexRender = function(){}

/***********************************
		计算出Y坐标
***********************************/
vnet.manager.prototype.getLocation = function(x,xo,yo){
	var y2 = Math.pow(CONSTANT.radius,2) -Math.pow(x-xo,2);
	var y = Math.sqrt(y2);
	var ya = yo+y;
	var yb = yo-y;
	var r1 = Math.abs(ya-oy);
	var r2 = Math.abs(yb-oy);
	return {x:x,y:r1>r2?ya:yb};
	//边缘检测，如果x<0或者y<0就应该开始调整canvas
	//if(r<0){}
}


vnet.manager.rotate = function(el, angle, scale) {
    //if (el && el.nodeType === 1) {
	if(el){
        angle = parseFloat(angle) || 0;
        scale = parseFloat(scale) || 1;
        if (typeof(angle) === "number") {
            //IE
            var rad = angle * (Math.PI / 180);
            var m11 = Math.cos(rad) * scale, m12 = -1 * Math.sin(rad) * scale, m21 = Math.sin(rad) * scale, m22 = m11;
            if (!el.style.Transform) {
                el.style.filter = "progid:DXImageTransform.Microsoft.Matrix(M11="+ m11 +",M12="+ m12 +",M21="+ m21 +",M22="+ m22 +",SizingMethod='auto expand')";
            }
            //Modern
            el.style.MozTransform = "rotate("+ angle +"deg) scale("+ scale +")";
            el.style.WebkitTransform = "rotate("+ angle +"deg) scale("+ scale +")";
            el.style.OTransform = "rotate("+ angle +"deg) scale("+ scale +")";
            el.style.Transform = "rotate("+ angle +"deg) scale("+ scale +")";
        }
    }
};

Array.prototype.filter = function(el){
	var idx = this.indexOf(el);
	return this.splice(idx,1);
}

//Function.prototype.constuctor = (function(){
//	return function(){}
//})();
