/*+-----------------------------------------+
  | FileName : Javascript Common Controller |
  | Edtion   : To all versions              |
  | Author   : TBS Software                 |
  +-----------------------------------------+*/

/*------ 扩展对象方法的函数 ------*/
Object.extend = function(destination,source){
	for (var property in source){
		destination[property] = source[property];
	}
	return destination;
}
/*-- 扩展document对象的方法getElementsByClassName函数 --*/
Object.extend(document,{
getElementsByClassName: function(Name){
	var Nodes = [];	
	var Elements = document.getElementsByTagName("*");
	
	for(var i = 0,theElement,E_Array = []; i < Elements.length; i++){
		theElement = Elements[i].className;
		E_Array    = theElement.split(" ");
		
		if(E_Array.length == 1){
			theElement = E_Array[0];
			if (theElement == Name){
				theElement = Elements[i];
				Nodes.push(theElement);
			}
		}
		else if(E_Array.length > 1){
			
			for(var j = 0; j < E_Array.length; j++){	
				theElement = E_Array[j];
				if (theElement == Name){
					theElement = Elements[i];
					Nodes.push(theElement);
				}
			}
		}
	}
	return Nodes;  //返回一个节点数组
	}
})
// 扩展string的用法
Object.extend(String.prototype,{
	delcomma: function(e){
	    return this.replace(/(^,*)|(,*$)/g,'');
	},
	strip: function() {
	    return this.replace(/^\s+/,'').replace(/\s+$/,'');
	},
	ellipsis: function(_len){
		var new_str = '';
		var Len     = _len || 16;
		if(this.length > Len){
			new_str = this.substr(0,Len) + '...';
			return new_str;
		}
		return this;
	},
	parseBoolean: function(){
		var flag = true;
		switch(this.toString()){
			case "true":
			case "1":
				flag = true;
				break;
			case "false":
			case "0":
			case "":
				flag = false;
				break;
		}
		return flag;
	}
});
/*------- 扩展原生对象ARRAY的方法 ---------*/
Array.prototype.append = function(obj,nodup){
	if(nodup){
		this[this.length] = obj;
	}
}

Array.prototype.remove = function(o){
	var i = this.indexOf(o);
	if (i > -1){
		this.splice(i,1);
	}
	return (i > -1);
}
//增加数组的查询方法
Array.prototype.indexOf = function($value){  
	for(var $i=0; $i<this.length; $i++){  
		if(this[$i] == $value)  
		return $i;  
	}  
	return -1;  
}
//根据下标来删除数组
Array.prototype.deleteIndex = function(index){  
	if(isNaN(index) || index > this.length){  
		return false;  
	}  
	this.splice(index,1);  
}
/*-- $函数其实getElementById函数的缩写，同时还扩展它的功能 ---*/
function $(){
	var elements = new Array();
	
    for(var i = 0; i < arguments.length; i++){
		var element = arguments[i];
		
		if(typeof element == "string"){
			element = document.getElementById(element);
		}
		elements.push(element);
	}
	
	if(elements.length == 1 && arguments.length > 0){
		return elements[0];
	} else {
		return elements;  // 当传入多个ID，则返回一个节点数组
	}
}

/*--------单个赋值方式函数：可以给INPUT/SELECT/TABLE/DIV/SPAN类型的节点进行赋值---------*/
function supplyValue(Name,Value){
	var node      = $(Name) || document.getElementsByName(Name);
	var bigType   = node.nodeName || node[0].nodeName;
	
	switch(bigType){
		case 'TD' : {}
		case 'DIV' : {}
		case 'SPAN' : {
			node.innerHTML = Value;
			node.title = Value;
			break;
		}
		case 'SELECT' : {
			node.value = Value;
			node.title = Value;
			break;
		}
		case 'INPUT' : {
			var smallType = node.type || node[0].type;
			
			switch(smallType){
				case 'text': {
					node.value = Value;
					node.title = Value;
					break;
				}
				case 'radio' : {
					node = document.getElementsByName(Name); //这个要特别注意
					for(var i = 0; i < node.length; i++){
						if(node[i] !== undefined && node[i].value == Value){
							node[i].checked = true;
						}
					}
					break;
				}
				case 'checkbox' : {
					node.checked = (Value == 1 ? true : false);
					break;
				}
			}
		}
	}
}

function ctrlElementsState(Nodes){//第一个是控制状态的节点。
	for(var i = 1; i < Nodes.length; i++){
		Nodes[i].disabled = !Nodes[0].checked;
	}
}

/*-------- supplyValue函数的批量处理版 ---------*/
function setJSONValue(array_json){// array_json的格式是JSON
	if(typeof array_json != 'object'){ return false;}
	
	for(var i in array_json){
		if($(i) != null){
			supplyValue(i,array_json[i]);
		}
	}
}

/*---------- 监听器函数 ----------*/
var jsEvent = new Array();

jsEvent.EventRouter = function(el,eventType){
	this.lsnrs = new Array();
	this.el = el;
	el.eventRouter = this;
	el[eventType] = jsEvent.EventRouter.callback;
}
//添加监听事件
jsEvent.EventRouter.prototype.addListener = function(lsnr){
	this.lsnrs.append(lsnr,true);
}
//删除监听事件
jsEvent.EventRouter.prototype.removeListener = function(lsnr){
	this.lsnrs.remove(lsnr);
}

jsEvent.EventRouter.prototype.notify = function(e){
	var lsnrs = this.lsnrs;
	for(var i = 0;i<lsnrs.length;i++){
		var lsnr = lsnrs[i];
		lsnr.call(this.el,e);
	}
}
//监听器的回调函数
jsEvent.EventRouter.callback=function(event){
	var e = event || window.event;
	var router=this.eventRouter;
	router.notify(e)
}

/*---- 关于表单的系列函数 ----*/
var Form = {
	Checkbox: function(Id,xValue){
		var _node = $(Id);
		
		switch(xValue){
			case undefined : {
				return (_node.checked == true) ? 1 : 0;
			}
			case '1' : {
				_node.checked = true;
				break;
			}
			case '0' : {
				_node.checked = false;
				break;
			}
		}
		return xValue;
	},
	Radio: function(Name,xValue){
		var _node = document.getElementsByName(Name);
		
		if(xValue == undefined){
			for(var i = 0; i < _node.length; i++){
				if(_node[i].checked == true){
					return _node[i].value;
				}
			}
		} else {
			for(var j = 0; j < _node.length; j++){
				if(_node[j] !== undefined && _node[j].value == xValue){
					_node[j].checked = true;
				}
			}
		}
		return xValue;
	},
	Select: function(Id,xValue){
		var _node = $(Id);
		
		if(xValue == undefined){
			return _node.value;
		} else {
			_node.value = xValue;
		}
		return xValue;
	},
	Action: function(){
		var Input_Array = document.getElementsByTagName("input");
	
		if(Input_Array.length == 0) return false;
		
		//for(var i = 0; i < Input_Array.length; i++){
			//if(Input_Array[i].type == "text" || Input_Array[i].type == "password"){
				//Input_Array[i].onfocus   = Form.Focus;
				//Input_Array[i].onblur    = Form.Blur;
			//}
		//}
	},
	//删除了很少用的Disabled/Enable函数
	Selected: function(node,element){ // Select --> Selected
		var Node    = $(node),S_Index;
		var Element = document.getElementsByClassName(element); 
		if(arguments[2] != null){Node.selectedIndex = arguments[2];}  // if you need to appoint the selectedIndex of option.
		S_Index = Node.selectedIndex;
		
		for(var i = 0; i < Element.length; i++ ){
			Element[i].style.display = "none";
		}
		Node.options[S_Index].selected = "true";
		Element[S_Index].style.display = "block";
	},
	Clear: function(){
		var _form   = document.forms[0];
		var _inputs = _form.getElementsByTagName('input');
		
		for(var i = _inputs.length; 0 < i; i--){
			try
			{
				_form.removeChild(_inputs[i-1]);
			}
			catch (ex)
			{
			}
			
		}
	},
	AddElements: function(Name,Value){
		var form_element = document.forms[0];
		var new_element = document.createElement('input');
		new_element.type = "hidden";
		new_element.name = Name;
		new_element.value = Value;
		form_element.appendChild(new_element);
	},
	CreateOptions: function(nodeName,optionValue,valueArray){
		var Node = $(nodeName),valueOptions;
		
		Node.options.length = 0;
		if(valueArray == undefined){
			valueOptions = optionValue;
		} else {
			valueOptions = valueArray;
		}
		
		for(var i = 0; i < optionValue.length; i++){
			Node.options[i] = new Option(optionValue[i]);
			Node.options[i].value = valueOptions[i];
		}
	},
	SendJSONValue: function(array_input,clear_form){
		if(typeof array_input != 'object') return false;
		if(clear_form){ //当用户需要在提交表单前清除旧表单，只需要传入一个true的参数即可;
			Form.Clear();
		}
		for(var i in array_input){
			if(typeof i == 'string' && i.length > 3 && array_input[i] !== undefined){
				Form.AddElements(i,array_input[i]);
			}
		}
	}
}

/*-- 函数缩写  --*/
var $F = Form.AddElements;   
var $H = Form.SendJSONValue; 
var $S = Form.CreateOptions;

/*----- about Table --------*/
var getValue = {}
var Table = {
	Mousemove: function(){
		this.style.background = "#eee";
	},
	Mouseout: function(){
		this.style.background = "#fff";
	},
	Action: function(){
		var Tr = document.getElementsByTagName("tr");
	
		if(Tr.length == 0) return false;
		for(var i = 1; i < Tr.length; i++){
			Tr[i].onmousemove = Table.Mousemove;
			Tr[i].onmouseout  = Table.Mouseout;
			Tr[i].onclick     = getValue;     // 如果这个函数要想使用，则要先定义
			if(arguments[0] == null) Tr[i].style.cursor = "pointer";
		}
	},
	Clear: function(Node){ // 清空表格内容
		var Table = $(Node);
		var Tr    = Table.getElementsByTagName('tr');
		
		for(var i = Tr.length - 1; i > 0; i--){
			Table.deleteRow(i);
		}
	},
	Create: function(Node,Value){ // 传入的Value必须是符合要求的数组
		var Table = $(Node);
		var Tbody = document.createElement('tbody');
		
		for(var i = 0; i < Value.length; i++){
			var Tr = [];
			Tr[i]  = document.createElement('tr');
			
			//防止空数组
			if(Value[i] == undefined) continue;
			for(var j = 0; j < Value[i].length; j++){
				var Td = [];
				Td[j]  = document.createElement('td');
				
				Td[j].innerHTML = Value[i][j];
				Td[j].id        = Node + "_" + i.toString() + j.toString();
				Tr[i].appendChild(Td[j]);
			}
			Tbody.appendChild(Tr[i]);
		}
		Table.appendChild(Tbody);
	}
}
var $T = Table.Create;

function evalJSON(json){
	var obj = null;
	try {
		obj = eval("(" + json + ")");
	} catch(E){}
	
  	return obj ;
}

/*------   语言转换函数    -----*/
function chg_language(obj){
	for (var attr in obj){
		try{
			switch (attr){
				case "title":
					$('lang_title').innerHTML = obj[attr];
					break;
				case "innerHTML":
					var o_arr = obj[attr];
					for (var k in o_arr){
						try{
							$(k).innerHTML = o_arr[k];
						} catch(E){
//							alert(k + ":" + E);
						}
					}
					break;
				case "value":
					var o_arr = obj[attr];
					for (var k in o_arr){
						try{
							$(k).value = o_arr[k];
						} catch(E){
//							alert(k + ":" + E);
						}
					}
					break;				
				case "option_title":
					var o_arr = obj[attr];
					for (var k in o_arr){
						try{
							for (var i=0; i<o_arr[k].length; i++){
								try{
									$(k).options[i].title = o_arr[k][i];
								} catch(E){
//									alert(o_arr[k][i] + ":" + E);
								}
							}
						} catch(E){
//							alert(k + ":" + E);
						}
					}
					break;
				case "option_text":
					var o_arr = obj[attr];
					for (var k in o_arr){
						try{
							for (var i=0; i<o_arr[k].length; i++){
								try{
									$(k).options[i].text = o_arr[k][i];
								} catch(E){
//									alert(o_arr[k][i] + ":" + E);
								}
							}
						} catch(E){
//							alert(k + ":" + E);
						}
					}
					break;
				default:
//					alert("unkown cmd: " + attr);	
			}
		} catch(E){
//			alert(obj + ":" + E);
		}
	}
}

/* Cookie 操作相关函数 */
var Cookie = {

    Get : function(name){
        var arrStr = document.cookie.split("; ");
        for(var i = 0;i < arrStr.length;i ++){
            var temp = arrStr[i].split("=");
            if(temp[0] == name) 
                return unescape(temp[1]);
        }
        return null;
    },
     
    //添加Cookie
    Set : function(name, value, hours, path){
        var str = name + "=" + escape(value);
        
        //为0时不设定过期时间，浏览器关闭时cookie自动消失
        if(hours != undefined && hours > 0){
            var date = new Date();
            var ms = hours * 3600 * 1000;
            date.setTime(date.getTime() + ms);
            str += "; expires=" + date.toGMTString();
        }
        
        //路径默认设置为根目录
        if(path == undefined)
            path = "/";
        str += "; path=" + path;
        
        document.cookie = str;
    },
    
    //删除Cookie
    Delete :function(name){
        var date = new Date();
        date.setTime(date.getTime() - 10000);
        document.cookie = name + "=0; expires=" + date.toGMTString();
    }
}