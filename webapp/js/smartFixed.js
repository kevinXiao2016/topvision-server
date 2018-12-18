var headerTop;
$.fn.smartFixed = function() {
	var position = function(element) {
		var pos = element.css("position");
		$(window).off('scroll', _scroll);
		$(window).scroll(_scroll);
		
		function _scroll(){
			var scrolls = $(this).scrollTop();
			//var scrolls = fixed ? $(this).scrollTop() +element.outerHeight() : $(this).scrollTop();
			if (scrolls > headerTop) {
				//表明滚动内容已经使得正常情况下该元素不可见
				if (window.XMLHttpRequest) {
					//支持position:fixed定位
					element.css({
						position: "fixed",
						top: 0
					});	
				} else {
					//IE8-不支持position:fixed定位
					element.css({
						position: "absolute",
						top: scrolls
					});	
				}
				//滚动元素fixed之后，原DOM流中会失去该元素的高度，scroll会突然变小，变成小于该元素的top值，此时便会产生抖动
			}else if(scrolls <= headerTop - element.height()){
				//正常显示即可
				element.css({
					position: pos,
					top: headerTop
				});	
			}
		}
	};
	return $(this).each(function() {
		position($(this));						 
	});
};