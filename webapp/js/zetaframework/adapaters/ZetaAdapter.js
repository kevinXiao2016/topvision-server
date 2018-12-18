var doc = document,
	DOC = doc,
	WIN = window;
DOC.get = DOC.getElementById;
var NULL = null,
	EMPTY = "",
	enableMenuScrolling = false,
	stripeRows = false,
	chkSelModel = false,
	animCollapse = false;
var Topvision = {
	version : 1.0,
	lang : lang,
	isDevelopment : isEmsDevelopment
};
var chkSelModel = false;
var animCollapse = false;
var stripeRows = false; 
var useArrows = false;
var trackMouseOver = false;
var enableMenuScrolling = false;
(function(){
	/*WIN.onerror = function(){
		alert(1);
		return true;
	}*/
	DOC.oncontextmenu = function(){
		if(Topvision.isDevelopment){
			return true;
		}else{
			return false;
		}
	}
	$( DOC ).bind("keydown",function(e){
		if( !["INPUT","TEXTAREA","PASSWORD"].contains( e.target.tagName) ){
			if(e.keyCode == KeyEvent.VK_BACKSPACE){
				e.stopPropagation();
				return false;
			}
		}
	})
	// listen
	$( WIN ).bind( "load", loadHndler );

	if(window != top){
		window.EntityType = top.EntityType;
	}
	
	/*防止弹出窗口焦点丢失*/
	function loadHndler(){
		setTimeout(function(){
			try{
				$( DOC ).focus();
			    $( DOC.body ).focus().bind('mousedown', window.top.switchMouseDown)
			    		.bind('mouseover', window.top.switchMouseOver)
			    		.bind('mouseout', window.top.switchMouseOut);
			}catch(e){
				throw new Error()
			}
		}, 170);
		
		if(!Ext.EventManager){
			Ext.EventManager = {
				_unload : Ext.emptyFn
			};
		}
	}
})();

var PageAction = { CREATE : 1, MODIFY : 2, DELETE : 3 };
var Button = function(element,disabled){
    //如果以ID名直接传的方式容易出现DOM name与Function name重名的情况
	this.element = DOC.getElementById( element );
	this.element.style.display = 'none';
	this.el = this.element.parentNode.firstChild;
	this.setDisabled(disabled);
}
Button.prototype.element;
Button.prototype.el;
Button.prototype.setDisabled = function( disabled ){
	if( disabled ){
		this.el.onclick=function(){}
		$( this.el ).addClass("disabledAlink");
		$( this.element ).prev().addClass("disabledAlink");
	}else{
		var element = this.element;
		$( this.el ).removeClass("disabledAlink");
		$( this.element ).prev().removeClass("disabledAlink");
		this.el.onclick=function(event){
			$( element ).trigger("click");
		}
	}
	return this;
}
Button.prototype.setText = function(text){
	$(this.el).find( ".BUTTON-TEXT" ).text( text );
	return this;
}
Button.prototype.show = function(text){
	$(this.el.parentNode).show();
	return this;
}
Button.prototype.hide = function(text){
	$(this.el.parentNode).hide();
	return this;
}
var R = {};

function operationAuthInit(power,ids){
    if(!power){
        for(var i=0; i<ids.length; i++){
            $("#"+ids[i]).attr("disabled",true);
        }
    }
}
var V = Validator = top.Validator;


/***************************************************************
  					console 对象代理
***************************************************************/
if(window.top != window){
	if(typeof console == "undefined"){
		window.console = top.console;
	}
	window.VersionControl = VC = {
		support : function( $id,$entityId){
			return top.VersionControl.support($id, $entityId || CONTROL_ENTITYID);
		},
		supportNode : top.VersionControl.supportNode
	}
}