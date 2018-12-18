/**
 * Zetaframework JavaScript Core.
 *
 * (C) TopoView, 2008-04-08
 *
 * authro: kelers
 * 
 */
var Zetaframework = new Object();
var idSeed = 0,
	toString = Object.prototype.toString,
	ua = navigator.userAgent.toLowerCase(),
	check = function(r){
	    return r.test(ua);
	},
	DOC = document,
	isStrict = DOC.compatMode == "CSS1Compat",
	isOpera = check(/opera/),
	isChrome = check(/chrome/),
	isWebKit = check(/webkit/),
	isSafari = !isChrome && check(/safari/),
	isSafari2 = isSafari && check(/applewebkit\/4/), 
	isSafari3 = isSafari && check(/version\/3/),
	isSafari4 = isSafari && check(/version\/4/),
	isIE = !isOpera && check(/msie/),
	isIE7 = isIE && check(/msie 7/),
	isIE8 = isIE && check(/msie 8/),
	isIE6 = isIE && !isIE7 && !isIE8,
	isGecko = !isWebKit && check(/gecko/),
	isGecko2 = isGecko && check(/rv:1\.8/),
	isGecko3 = isGecko && check(/rv:1\.9/),
	isBorderBox = isIE && !isStrict,
	isWindows = check(/windows|win32/),
	isMac = check(/macintosh|mac os x/),
	isAir = check(/adobeair/),
	isLinux = check(/linux/),
	isSecure = /^https/i.test(window.location.protocol);

/* Array extend */
Array.prototype.indexOf = function(o, from) {
	var len = this.length;
	from = from || 0;
	from += (from < 0) ? len : 0;
	for (; from < len; ++from){
	    if(this[from] == o){
	        return from;
	    }
	}
	return -1;
};
Array.prototype.get = function(index){
	if(this.length < index+1){
		throw new Error("IndexOutOfArrayRange" + this);
	}
	return this[index];
}
Array.prototype.last = function(){
	return this.get(this.length-1);
}
Array.prototype.first = function(){
	return this.get(0);
}

/**
 * 添加一个新对象，并保证该对象在数组中唯一
 * TODO 返回新加对象的下标
 */
Array.prototype.add = function(obj) {
	return this.append(obj, true);
};
/**
 * 删除指定对象以及该对象之后的所有对象
 */
Array.prototype.remove = function(obj) {
    var index = this.indexOf(obj);
    if(index != -1){
        this.splice(index, 1);
        return true;
    }
    return false;
};
Array.prototype.removeObj = function(obj){	
	var index=0; 	
	index=this.indexOf(obj);		
	
	if(index>=0 && index<this.length){ 
		for(var i=index;i<this.length-1;i++) 
			this[i]=this[i+1]; 
		this.length-=1; 
		return true; 
	} 
	else return false; 
}
/**
 * 判断当前数组中是否包含有指定对象。
 */
Array.prototype.contains = function(obj) {
    return (this.indexOf(obj) >= 0);
};
Array.prototype.append = function(obj, isChecking) {
    if (!(isChecking && this.contains(obj))) {
        this[this.length] = obj;
        return true;
    }
    return false;
};
String.prototype.replaceAll = function(s1,s2) {
    return this.replace(new RegExp(s1,"gm"),s2);
}

/* String extend */
String.prototype.len = function() {
	return this.replace(/[^\x00-\xff]/g, "aa").length;
}
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.startWith = function(suffix){
	if (suffix == null || suffix == '') {
		return false;
	}
	return this.substring(0,suffix.length) == suffix;
}
String.prototype.endWith = function(suffix){
	if (suffix == null || suffix == '') {
		return false;
	}
	return this.substring(this.length - suffix.length) == suffix;
}
String.prototype.ellipse = function(maxLength){
    if(this.length > maxLength){
        return this.substr(0, maxLength-3) + '...';
    }
    return this;
};
String.prototype.ellipsex = function(maxLength){
    if(this.len() > maxLength){
        return this.substr(0, maxLength-3) + '...';
    }
    return this;
};

Zeta$ = function(id) {
	if (typeof(document.getElementById) != "undefined")
		return document.getElementById(id);
	//else if (typeof(document.all) != "undefined")
		//return document.all[id];
	return null;
};

Zeta$N = function(name) {
	if (name){
		return document.getElementsByName(name);
	}
	return null;
};

function Zeta$defined(obj){
	return (obj != undefined);
};

function Zeta$instanceof(obj){
	if (!Zeta$defined(obj)) return false;
	if (obj.htmlElement) return 'element';
	var type = typeof obj;
	if (type == 'object' && obj.nodeName){
		switch(obj.nodeType){
			case 1: return 'element';
			case 3: return (/\S/).test(obj.nodeValue) ? 'textnode' : 'whitespace';
		}
	}
	if (type == 'object' || type == 'function'){
		switch(obj.constructor){
			case Array: return 'array';
			case RegExp: return 'regexp';
			case ZetaObject: return 'object';
		}
		if (typeof obj.length == 'number'){
			if (obj.item) return 'collection';
			if (obj.callee) return 'arguments';
		}
	}
	return type;
};

var Zeta$extend = function() {
	var args = arguments;
	if (!args[1]) args = [this, args[0]];
	for (var property in args[1]) args[0][property] = args[1][property];
	return args[0];
};

var ZetaObject = function(properties){
	var klass = function(){
		return (arguments[0] !== null && this.initialize && Zeta$instanceof(this.initialize) == 'function') ? this.initialize.apply(this, arguments) : this;
	};
	Zeta$extend(klass, this);
	klass.prototype = properties;
	klass.constructor = ZetaObject;
	return klass;
};

var ZetaHash = new ZetaObject({
	length: 0,

	initialize: function(object){
		this.obj = object || {};
		this.setLength();
	},

	setLength: function(){
		this.length = 0;
		for (var p in this.obj) this.length++;
		return this;
	},
	
	containsKey: function(key){
		return (key in this.obj);
	},	
	
	get: function(key){
		return (this.containsKey(key)) ? this.obj[key] : null;
	},

	put: function(key, value){
		if (!this.containsKey(key)) this.length++;
		this.obj[key] = value;
		return this;
	},
	
	remove: function(key){
		if (this.containsKey(key)){
			delete this.obj[key];
			this.length--;
		}
		return this;
	},
	
	clear: function(){
		this.obj = {};
		this.length = 0;
		return this;

	},
	
	size: function(){
		return this.length;
	},

	values: function(){
		var values = [];
		for (var property in this.obj) values.push(this.obj[property]);
		return values;
	},
	
	keys: function() {
		var keys = [];
		for (var property in this.obj) keys.push(property);
		return keys;
	},
	
	eachValue: function(fn) {
		for (var property in this.obj) fn(this.obj[property]);
	},
	
	eachKey: function(fn) {
		for (var property in this.obj) fn(property);
	}	
});

/*
 * Shortcut to create a Hash from an Object.
 */
function Zeta$H(obj){
	return new ZetaHash(obj);
};

/* StringBuffer */
Zeta$StringBuffer = function() {
    this.strings = new Array;
}
Zeta$StringBuffer.prototype.append = function (str) {    
    this.strings.push(str);    
};
Zeta$StringBuffer.prototype.toString = function() {    
    return this.strings.join("");
};

/* StringBuilder */
function StringBuilder() {
    this.strings = new Array;
}
StringBuilder.prototype.append = function (str) {    
    this.strings.push(str);    
};
StringBuilder.prototype.toString = function() {    
    return this.strings.join("");
};

/* common function */
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

function judgeNumber(evt) {
	var key = window.event ? evt.keyCode : evt.which;
	return (key >= 48 && key <= 57) || (key >= 96 && key <= 105) || isSpecialKey(key);   
}

function isSpecialKey(key) {
    //8:backspace; 46:delete; 37-40:arrows; 36:home; 35:end; 9:tab; 13:enter, 16:shift   
    return key == 8 || key == 46 || (key >= 37 && key <= 40) || key == 35 || key == 36 || key == 9 || key == 13;
}

function includeJs(id, url, source) { 
	if (source != null && !document.getElementById(id)){ 
		var oHead = document.getElementsByTagName('HEAD').item(0); 
		var oScript = document.createElement( "script" ); 
		oScript.language = "javascript"; 
		oScript.type = "text/javascript"; 
		oScript.id = id; 
		oScript.defer = true; 
		oScript.text = source; 
		oHead.appendChild(oScript);
	} 
}

var isDebug = true;
function setEnabledDebug(enabled) {
    isDebug = enabled;
}
function debug(desc) {
    if (isDebug) {
        alert(desc);
    }
}

function forbiddenKey() {return false;}
function returnTrue(){return true;}
function returnFalse(){return false;}

/* cookie */
function setCookie(name,value){
    var Days = 30;
    var exp  = new Date();    //new Date("December 31, 9998");
        exp.setTime(exp.getTime() + Days*24*60*60*1000);
        document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
function getCookie(name, defaultValue) {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg)) return unescape(arr[2]);
    else return defaultValue;
}
function delCookie(name) {
    var exp = new Date();
        exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
        if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}


var KeyEvent = new Object();
// virtual key
KeyEvent.VK_BACKSPACE = 8;
KeyEvent.VK_TAB = 9;
KeyEvent.VK_ESC = 27;
KeyEvent.VK_UP = 38;
KeyEvent.VK_DOWN = 40;
KeyEvent.VK_DEL = 46;
KeyEvent.VK_0 = 47;
KeyEvent.VK_NUM0 = 95;
KeyEvent.VK_9 = 58;
KeyEvent.VK_A = 65;
KeyEvent.VK_C = 67;
KeyEvent.VK_P = 80;
KeyEvent.VK_V = 86;
KeyEvent.VK_X = 88;
KeyEvent.VK_NUM9 = 106;
KeyEvent.VK_F4 = 115;
KeyEvent.VK_F5 = 116;
KeyEvent.VK_ENTER = 13;
KeyEvent.VK_LEFT = 37;
KeyEvent.VK_RIGHT = 39;
KeyEvent.VK_PAGEDOWN = 34;
KeyEvent.VK_PAGEUP = 33;

// mouse event
var MouseEvent = new Object();
MouseEvent.BUTTON1 = 1;
MouseEvent.BUTTON2 = 2;
MouseEvent.BUTTON3 = 3;

MouseEvent.LEFT = 1;
MouseEvent.MIDDLE = 2;
MouseEvent.RIGHT = 3;

/* for firefox, chrome, ie */
var isFirefox = false;
function getBrowser() {
	var isBrowser ;
	if(window.ActiveXObject){
		isBrowser = "IE";
	}else if(window.XMLHttpRequest){
		isBrowser = "FireFox";
	}
	return isBrowser;
}
/*if (window.Node){
	isFirefox = true;
	MouseEvent.BUTTON1 = 0;
	Node.prototype.replaceNode = function(Node) {
		this.parentNode.replaceChild(Node,this);
	}
	Node.prototype.removeNode = function(removeChildren) {
		if(removeChildren)
			return this.parentNode.removeChild(this);
		else{
			var range=document.createRange();
			range.selectNodeContents(this);
			return this.parentNode.replaceChild(range.extractContents(),this);
		}
	}
	Node.prototype.swapNode=function(node){
		var nextSibling=this.nextSibling;
		var parentNode=this.parentNode;
		node.parentNode.replaceChild(this, node);
		parentNode.insertBefore(node, nextSibling);
	}
	
	HTMLElement.prototype.__defineGetter__("innerText", 
        function(){ 
            return this.textContent.replace(/(^\s*)|(\s*$)/g, "");
        } 
    ); 
    HTMLElement.prototype.__defineSetter__("innerText", 
        function(sText){ 
            this.textContent=sText; 
        } 
    );
}*/

/* for explorer's performance */
// for memory leak
/*
var garbageElementProps = ["data", "onkeydown", "onkeyup", "onmouseover", "onmouseout", 
	"onmousedown", "onmouseup", "onmousemove", "ondrag", "ondblclick", "onclick", 
	"onselectstart", "oncontextmenu"];
function garbageCollcetor() {
	var el;
	for (var d = document.all.length; d--;) {
		el = document.all[d];
		for (var c = garbageElementProps.length; c--; ) {
			if(!el[garbageElementProps[c]])
				continue;
			el[garbageElementProps[c]] = null;
		}
	}
	if(document.addEventListener){
		window.detachEventListener('unload', garbageCollcetor, false);
	} else{
		window.detachEvent('onunload', garbageCollcetor);
	}
}
// for accelerate Browser speed
function enableQuickBrowser() {
	try {
		document.execCommand("BackgroundImageCache", false, true);
	} catch (err) {
	}
}
if(document.addEventListener){
	window.addEventListener('unload', garbageCollcetor, false);
} else{
	window.attachEvent('onunload', garbageCollcetor);
	enableQuickBrowser();
}*/
/* for explorer's performance */

var EventUtils = {
	isLeftButton: function(e) {
		return e.button == MouseEvent.BUTTON1;
	},
	
	isRightButton: function(e) {
		return e.button == MouseEvent.BUTTON2;
	},

	getEvent: function(e) {
		return window.event||e; // for firefox
	},
	
	getSrcElement: function(e) {
		var event = window.event||e;
		return isFirefox ? event.target : event.srcElement;
	},
	
	setCapture: function(obj) {
		if(isFirefox) {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		} else {
			obj.setCapture();	
		}
	},
	
	releaseCapture: function(obj) {
		if(isFirefox) {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		} else {
			obj.releaseCapture();
		}	
	}
};

var ZetaUtils = {
	getIframe: function(id) {
		if (isIE) {
			return document.frames[id];
		} else {
			return window.frames[id];
		}	
	},
	
	getIframeById: function(id) {
		return document.getElementById(id);
	},	
	
	getEvent: function(e) {
		return window.event||e; // for firefox
	},
	
	getSrcElement: function(e) {
		var event = window.event||e;
		return isFirefox ? event.target : event.srcElement;
	},
	
	setCapture: function(obj) {
		if(isFirefox) {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		} else {
			obj.setCapture();	
		}
	},
	
	releaseCapture: function(obj) {
		if(isFirefox) {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		} else {
			obj.releaseCapture();
		}	
	}
};
/* for firefox, chrome, ie */

function selectComboxRow(id, value) {
	var combox = Zeta$(id);
	var options = combox.options;
	for (var i = 0; i < options.length; i++) {
		if (options[i].value == value) {
			combox.selectedIndex = i;
			break;
		}
	}
}

function selectInputText(textbox, start, len) {
	try {  
		var r = textbox.createTextRange();
		r.moveEnd('character', len -(textbox.value.length - start));
		r.moveStart('character', start);
		r.select();  
	} catch(e) {
	}
}
function setTextSelect(id) {
	var range = document.body.createTextRange();
	range.moveToElementText(id)  
	range.moveEnd('character',-1*parseInt(document.all.s.value));    
	range.moveStart('character',-1+parseInt(document.all.b.value));    
	range.select();  
}
function getCaretPosition(textbox) {
	var rang = document.selection.createRange();
	rang.setEndPoint("StartToStart", textbox.createTextRange());
	return rang.text.length;
}
function setCaretPosition(textbox, start) {  
	try {  
		var r = textbox.createTextRange();
		r.moveStart('character', start);
		r.select();
	} catch(e) {}  
}
function moveCaretToPosition(textbox, start) {  
	try {  
		var r = textbox.createTextRange();
		r.moveStart('character', start);
	} catch(e) {}  
}
function moveCaretToEnd(textbox) {  
	try {  
		var r = textbox.createTextRange();
		r.moveStart('character', textbox.value.length - 1);
	} catch(e) {}  
}

// input tip
var inputTipDisabled = false;
function enableInputTip() {
	inputTipDisabled = false;
}
function disableInputTip() {
	inputTipDisabled = true;
}
function showTips(id, str) {
	if (typeof(displayInputTip)=='undefined' || !displayInputTip) return;
	if (inputTipDisabled) return;
	var obj=document.getElementById(id);
	var x=getX(obj);
	var y=getY(obj);
	var l=x;var t=y-5;
	var popObj=document.getElementById("tips");
	if(popObj!=null) {
		popObj.innerHTML="<div class=\"tipcon\">"+str+"</div>";
		popObj.style.display="inline";
		var menu_h=popObj.offsetHeight;
		t-=menu_h;
		popObj.style.left=l+"px";
		popObj.style.top=t+"px";
		/*10000 > Ext.window的zIndex > 9000 ，为了防止弹出弹力的tootlip丢失，故设置一个较高的zindex ,modify by @bravin */
		popObj.style.zIndex = 100000;
	}
}
function hideTips() {
	if (typeof(displayInputTip)=='undefined' || !displayInputTip) return;
	document.getElementById("tips").style.display='none';
}
function clearOrSetTips(obj, msg) {
	if (typeof(displayInputTip)=='undefined' || !displayInputTip) return;
	if(!obj) return;
	if(obj.value!="") {
		if(obj.value==msg)obj.value="";
	} else obj.value=msg ? msg : '';
}
function getX(el) {
	var x=0;
	while(el) {
		x+=el.offsetLeft;
		el=el.offsetParent;
	}
	return x;
}
function getY(el) {
	var y=0;
	for(var e=el;e;e=e.offsetParent)y+=e.offsetTop;
	for(e=el.parentNode;e&&e!=document.body;e=e.parentNode)
	if(e.scrollTop) y-=e.scrollTop;
	return y;
}

/**
 * 输入框ToolTip提示
 * id：el.id   css: 为当前el将要修改class属性值（慎用）
 */
function inputFocused(id, msg, css) {
	if (inputTipDisabled) return;
	if(css){Zeta$(id).className=css;}
	if (msg != '') {
		showTips(id,msg);
	}
}
function inputBlured(obj, css) {
	if (typeof obj != 'object' && typeof obj == 'string') {
		obj = document.getElementById(obj);
	};
	if (inputTipDisabled) return;
	if(css){obj.className=css;}
	clearOrSetTips(obj);
	hideTips();
}

function showDownHelpTip(info, obj) {
	var el = Zeta$('downTooltips');
	el.innerHTML = '<div class=tipcon>' + info + '</div>';
	var l = getX(obj) - 12;
	var t = getY(obj) + 20;
	el.style.left = l + 'px';
	el.style.top = t + 'px';
	el.style.display = 'block';
}
function hideDownHelpTip() {
	Zeta$('downTooltips').style.display = 'none';
}

function showUpHelpTip(info, obj, direction, width) {
	var el = Zeta$('upTooltips');
	el.innerHTML = '<div class=tipcon>' + info + '</div>';
	var l = getX(obj) - 12;
	var t = getY(obj) + 20;
	if (direction) {
		l = l - 148;
		if (l < 0) {
			l = 0;
		}
		t = t + 5;
	}
	el.style.left = l + 'px';
	el.style.top = t + 'px';
	el.style.display = '';
}
function hideUpHelpTip() {
	Zeta$('upTooltips').style.display = 'none';
}

/* error tips */
function showSplashInfo(title, info, src, timeout) {
	showSplashError(title, info, src, timeout);
}
function showSplashError(title, info, src, timeout) {
	Zeta$('errorTitle').innerText = title;
	Zeta$('errorMsg').innerText = info;
	Zeta$('errorIcon').src = src;

	var el = Zeta$('errors');
	el.style.display = 'block';
	var l = parseInt((document.body.clientWidth - el.clientWidth) / 2);
	var t = 30;
	if (document.body.clientHeight > 200) {
		t = parseInt(document.body.scrollTop + document.body.clientHeight / 2 - el.clientHeight - 30);
	}
	el.style.left = l + 'px';
	el.style.top = t + 'px';
	setTimeout("hideSplashError()", timeout ? timeout : 2000);
}
function hideSplashError() {
	Zeta$('errors').style.display = 'none';
}
function showSplashLoading(title, info, src) {
	Zeta$('errorTitle').innerText = title;
	Zeta$('errorMsg').innerText = info;
	Zeta$('errorIcon').src = src;
	var el = Zeta$('errors');
	el.style.display = 'block';
	var l = parseInt((document.body.clientWidth - el.clientWidth) / 2);
	var t = 30;
	if (document.body.clientHeight > 200) {
		t = parseInt(document.body.scrollTop + document.body.clientHeight / 2 - el.clientHeight - 30);
	}
	el.style.left = l + 'px';
	el.style.top = t + 'px';
}

Zeta$NoHeaderAndFooter = function(doc) {};