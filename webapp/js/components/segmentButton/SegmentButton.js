var SegmentButton = (function() {
	var _SegmentButton = function(renderId, items, opts){
		this.renderId = renderId;
		this.items = items;
		this.opts = $.extend(true, {}, _SegmentButton.DEFAULTS_OPTS,  opts ? opts : {});
		this.value = undefined;
	};
	_SegmentButton.DEFAULTS_OPTS = {
	};
	
	_SegmentButton.prototype.init = function() {
		this.createButton();
		
		// 绑定触发事件
		this.bindEvent();
		
		// 默认选中第一项
		this.setValue(this.items[0].value);
	};
	
	_SegmentButton.prototype.createButton = function() {
		var str ='';
		var liTpl = '<li><a href="javascript:;" class="{0}" data-aaa="{3}" value="{1}"><span><label>{2}</label></span></li>';
		str += '<ul class="whiteTabUl">';
		
		var curItem;
		var curLiCls;
		for(var i=0; i<this.items.length; i++){
			curItem = this.items[i];
			
			if(i === 0) {
				curLiCls = 'first';
			} else if(i === this.items.length-1) {
				curLiCls = 'last';
			} else {
				curLiCls = '';
			}
			
			str += String.format(liTpl, curLiCls, curItem.value, curItem.name, i);	
		}
		str += '</ul>';
		$("#" + this.renderId).html(str);
	};
	
	_SegmentButton.prototype.bindEvent = function() {
		var $render = $("#" + this.renderId);
		var itemLength = this.items.length;
		var me = this;
		
		$render.find('a').on('click', function(event) {
			var $target = $(this);
			var curValue = $target.attr('value');
			me.setValue(curValue);
		});
	};
	
	_SegmentButton.prototype.getValue = function() {
		return this.value;
	};
	
	_SegmentButton.prototype.setValue = function(value) {
		if(value === undefined) {
			this.setValue(this.items[0].value);
			return;
		}

		// 找到value在items中的index
		var index = 0;
		var curItem;
		for(var i=0, len = this.items.length; i<len; i++) {
			curItem = this.items[i];
			if(curItem.value.toString() === value.toString()) {
				index = i;
				this.value = curItem.value;
			}
		}
		
		// 选中指定的li
		this.selectLi(index);
		
		// 触发回调
		this.opts.callback && typeof this.opts.callback === 'function' && this.opts.callback(value);
	}
	
	_SegmentButton.prototype.selectLi = function(index) {
		var $render = $("#" + this.renderId);
		var curTarget = $render.find("li").eq(index);
		var selectCls = 'selected';
		
		if(index === 0) {
			selectCls = 'selectedFirst';
		} else if(index === this.items.length - 1) {
			selectCls = 'selectedLast';
		}
		
		$render.find('a').removeClass('selectedFirst').removeClass('selectedLast').removeClass('selected');
		curTarget.find("a").addClass(selectCls);
	}
	
	return _SegmentButton;
})();