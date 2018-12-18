/////////////////////////////////////////////////////////////
///			FRAMEWORK ADAPTER						/////////
/////////////////////////////////////////////////////////////
function getKeyValue(string,key){
	if(!key){
		key = string;
	}
	if(typeof key != 'string' || key.length == 0 ){
		throw new Error("topvision : I18N key is unAcceptable : " + key);
	}
	var c = key.split(".")
	var v = I18N;
	try{
		while(c.length != 0){
			v = v[c.shift()];
		}
		return v;
	}catch(e){
		//NEED TO CONSOLE & LOG
		return key;
	}
}

function showMessageDlg(msg,key,handler){
	if(typeof key == 'undefined'){
		//handler = null;
	}else if(typeof key == 'string'){
		if(typeof handler != 'function'){
			handler = null;
		}
		msg = getKeyValue(key);
	}else if(typeof key == 'function'){
		handler = key;
	}
	return window.parent.showMessageDlg(getKeyValue("COMMON.tip"), msg, "tip", handler);
}
function showConfirmDlg(msg,key,handler){
	if(typeof key == 'undefined'){
		//handler = null;
	}else if(typeof key == 'string'){
		if(typeof handler != 'function'){
			handler = null;
		}
		msg = getKeyValue(key);
	}else if(typeof key == 'function'){
		handler = key;
	}
	return window.parent.showConfirmDlg(getKeyValue("COMMON.confirm"), msg, handler);
}

function showErrorDlg(msg,key,handler){
	if(typeof key == 'undefined'){
		//handler = null;
	}else if(typeof key == 'string'){
		if(typeof handler != 'function'){
			handler = null;
		}
		msg = getKeyValue(key);
	}else if(typeof key == 'function'){
		handler = key;
	}
	window.parent.showErrorDlg(getKeyValue("COMMON.error"), msg , null , handler);
	return 
}
/*/////////////////////////////////////////////////////////////
///				DATA  ADAPTER						/////////
/////////////////////////////////////////////////////////////
String.format = function(string,key,args){
	//兼容Ext自身的需要,通过判断string中是否有中文或者特殊字符来判断是否使用key FIXME ： 但是这样理论上是存在判断不准确的情况的
	var result = string;
	var  index = escape(string).indexOf("%u");
	if(index > -1 && typeof key == 'string'){
		result = getKeyValue(key);
	}
    //if (arguments.length > 2) {
        if (typeof (args) == "object" || (typeof key == 'object' && key )) {
        	// 当key为 null的时候 typeof key 也为 'ojbect',但是如果 args没填的话,typeof args 为 undefined
        	//如果字符串为标准字符串,则表示不会使用到key，如果args/key 为 object，那么需要将args赋值为 key；
        	if(index == -1){
        		args = key;
        	}
            if(args instanceof Array){
            	for (var i = 0; i < args.length ; i++) {
                    if (args[i] != undefined) {
                        var reg = new RegExp("({[" + i + "]})", "g");
                        result = result.replace(reg, args[i]);
                    }
                }
            } else {
            	for (var key in args) {
	                if(args[key]!=undefined){
	                    var reg = new RegExp("({" + key + "})", "g");
	                    result = result.replace(reg, args[key]);
	                }
	            }
            }
        }else {
        	var offset;
        	if(index > -1 ){
        		offset = 2;
        	}else{
        		offset = 1;
        	}
        	for (var i = 0; i < arguments.length-offset ; i++) {
                if (arguments[i+offset] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i+offset]);
                }
            }
        }
    //}
    return result
}

// String format
String.prototype.format = function(key,args) {
	//兼容Ext自身的需要,通过判断string中是否有中文或者特殊字符来判断是否使用key FIXME ： 但是这样理论上是存在判断不准确的情况的
    var result = this;
	var  index = escape(this).indexOf("%u");
	if(index > -1 && typeof key == 'string'){
		result = getKeyValue(key);
	}
        if (typeof (args) == "object" || (typeof key == 'object' && key )) {
        	//如果字符串为标准字符串,则表示不会使用到key，如果args/key 为 object，那么需要将args赋值为 key；
        	if(index == -1){
        		args = key;
        	}
        	if(args instanceof Array){
            	for (var i = 0; i < args.length ; i++) {
                    if (args[i] != undefined) {
                        var reg = new RegExp("({[" + i + "]})", "g");
                        result = result.replace(reg, args[i]);
                    }
                }
            } else {
            	for (var key in args) {
	                if(args[key]!=undefined){
	                    var reg = new RegExp("({" + key + "})", "g");
	                    result = result.replace(reg, args[key]);
	                }
	            }
            }
        } else {
        	var offset;
        	if(index > -1 ){
        		offset = 1;
        	}else{
        		offset = 0;
        	}
            for (var i = 0; i < arguments.length-offset; i++) {
                if (arguments[i+offset] != undefined) {
                    var reg = new RegExp("({[" + i + "]})", "g");
                    result = result.replace(reg, arguments[i+offset]);
                }
            }
        }
    return result;
}

/////////////////////////////////////////////////////////////
///			EXT ADAPTER								/////////
/////////////////////////////////////////////////////////////
if(typeof Ext != 'undefined'){
	*//** Adapter For GridPanel ***//*
	var func = Ext.grid.ColumnModel.prototype.setConfig;
	Ext.override(Ext.grid.ColumnModel,{
		setConfig : function(config, initial){
			var i, c, len;
			for(i = 0, len = config.length; i < len; i++){
		        c = config[i];
		        //如果配置中存在KEY这个配置项,则进行适当的转换
		        if(c.key){
		        	c.header = getKeyValue(c.key);
		        }
		    }
			func.apply(this,[config, initial]);
		}
	});
	*//*** Adapater For Menu .使用ITEM解决****//*
	var menuAdapater = Ext.menu.Menu.prototype.initComponent;
	Ext.override(Ext.menu.Menu,{
		initComponent : function(){
			//调用原始的构造器
			menuAdapater.apply(this);
			//由于 Menu的使用范围比较广，比如ComboBox，ToolBar中都有使用，故不能单纯的直接使用items来判定
			if(typeof this.items != 'undefined'){
				var items = this.items.items;
				if( items instanceof Array && items.length > 0){
					for(var i = 0,len = items.length ; i < len; i++){
						var c = items[i];
						if(c.key){
							c.text = " hello ";
						}
					}
				}
			}
		}
	});
	*//***  Adapater For MenuItem *******//*
	var itemAdapater = Ext.menu.Item.prototype.initComponent;
	Ext.override(Ext.menu.Item,{
		initComponent : function(){
			if(this.key){
				this.text = getKeyValue(this.key);
			}
			itemAdapater.apply(this);
		}, 
		_setText_ : function(){///由于Text是直接设置的,在这里再做KEY恐有不便，故不作修改
			
		}
	});
	
	*//**** Adapater For Toolbar & Menu Addition Method ******//*
	var containerAdapater = Ext.Container.prototype.add;
	Ext.override(Ext.Container,{
		add : function(comp){
			if(typeof comp == 'object' && comp.text){
				if(comp.key){
					comp.text = getKeyValue(comp.key);
				}
			}
			return containerAdapater.apply(this,[comp])
		}
	});
	*//***** Adapater For LoadMask *********//*
	Ext.override(Ext.LoadMask,{
		onBeforeLoad : function(){
			if(this.key){
				this.msg = getKeyValue(this.key);
			}
	        if(!this.disabled){
	            this.el.mask(this.msg, this.msgCls);
	        }
	    }
	});
	*//***** Adapter For Element Mask  **********//*
	//TODO 未经测试
	var elementAdapater = Ext.Element.prototype.mask;
	Ext.override(Ext.Element,{
		mask : function(msg, msgCls,key){
			if(key){
				msg = getKeyValue(key);
			}
			return elementAdapater.apply(this,[msg, msgCls])
		}
	});
	
}

/////////////////////////////////////////////////////////////
///			JQUERY ADAPTER							/////////
/////////////////////////////////////////////////////////////
if(typeof $ != 'undefined' || typeof jQuery != 'undefined'){
	var htmlFn = jQuery.fn.html;
	var textFn = jQuery.fn.text;
	jQuery.fn.extend({
		html : function(value,key){
			if(key){
				value = getKeyValue(key);
			}
			return htmlFn.apply(this,[value]);
		},
		text : function(){
			if(key){
				value = getKeyValue(key);
			}
			return textFn.apply(this,[value])
		}
		
	});
}*/


/*
Function.prototype.intercept = function(fn){
	var thisFn = this;
    return function(){
        fn.apply(this,arguments);
        thisFn.apply(this,arguments);
    }
}*/