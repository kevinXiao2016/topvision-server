//on off 图片开关;
var Nm3kSwitch = (function(){
	var nm3kSwitch = function(renderId, value, opts){
		this.renderId = renderId;
		this.value = value;
		this.opts = $.extend(true, {}, nm3kSwitch.DEFAULTS_OPTS,  opts ? opts : {});
	};
	nm3kSwitch.DEFAULTS_OPTS = {
		yesNoValue : [1, 0],
		disabled : false
	};
	
	nm3kSwitch.prototype = {
		aImgSrc : ['/images/speOn.png', '/images/speOff.png'],
	    aAlt : ['on', 'off'],
		init : function(){
			this.createSwitch();
		},
		createSwitch : function(){
			var img,
			    me = this,
			    opts = me.opts,
			    imgStr = '<img src="{0}" class="cursorPointer" /><input type="hidden" value="{1}" alt="{2}" />',
			    aAlt = me.aAlt, 
			    $render = $("#"+this.renderId);
			
			if(this.value == me.opts.yesNoValue[0]){
				img = String.format(imgStr, this.aImgSrc[0], me.value, 'on');
			}else{
				img = String.format(imgStr, this.aImgSrc[1], me.value, 'off');
			}
			$render.html(img);
			if(me.opts.disabled){
				this._disabledImg();
			}
			if(opts.name){
				$render.find(':hidden').attr({name : opts.name});
			}
			$render.find('img').bind('click', function(){
				var $me = $(this),
				    $hidden = $render.find(":hidden"),
					v = me.value,
					num = (v == me.opts.yesNoValue[0]) ? 0 : 1,
					alt = me.opts[num];
				if(me.opts.disabled){
					return;
				}
				if(num == 0){
					var newValue = me.opts.yesNoValue[1];
					$hidden.attr({alt: 'off'}).val(newValue);
					me.value = newValue;
					me.changeImg($me, 1);
				}else{
					var newValue = me.opts.yesNoValue[0];
					$hidden.attr({alt: 'on'}).val(newValue);
					me.value = newValue;
					me.changeImg($me, 0);
				}
				
				if(me.opts.afterChangeCallback){
					me.opts.afterChangeCallback(newValue);
				}
			});
		},
		setDisabled : function(bDisabled){
			var $render = $("#" + this.renderId);
			if(bDisabled){
				this._disabledImg();
			}else{
				this._enabledImg(); 
			}
		},
		//图片设置半透明;
		_disabledImg: function(){
			this.opts.disabled = true;
			var $render = $("#" + this.renderId);
			$render.css({
				opacity: 0.5
			});
		},
		//图片正常，透明度1;
		_enabledImg: function(){
			this.opts.disabled = false;
			var $render = $("#" + this.renderId);
			$render.css({
				opacity: 1
			});
		},
		changeImg : function($dom, num){
			var src = this.aImgSrc[num];
			$dom.attr({
				src : src
			});
		},
		getValue : function(){
			return this.value;
		},
		getDisplayValue : function(){
			var v = (this.value == this.opts.yesNoValue[0]) ? 0 : 1; 
			return this.aAlt[v];
		},
		setValue : function(v){
			//设置了不合法的值;
			if($.inArray(v, this.opts.yesNoValue) == -1){
				try{
					//console.log('Nm3kSwitch Set Value is not valid, Please check setValue()');
				}catch(err){}
				return;
			}
			//设置的值和本来的值相等（什么也不做）
			if( this.getValue() === v ){
				return;
			}else{
				var num = (v === this.opts.yesNoValue[0]) ? 0 : 1,
				    newSrc = this.aImgSrc[num],
				    newValue = this.opts.yesNoValue[num],
				    newAlt = this.aAlt[num],
				    $render = $("#" + this.renderId);
				
				this.value = v;
				$render.find("img").attr({src: newSrc});
				$render.find(":hidden").val(newValue).attr({alt: newAlt});
			}
			
		}
	}
	return nm3kSwitch;
})();