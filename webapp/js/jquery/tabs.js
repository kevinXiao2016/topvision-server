(function($){
	$.fn.tabs = function(control){
		var element = $(this);
		control = $(control);
		element.delegate("li", "click", function () {
			// 遍历选项卡名称
			var tabName = $(this).attr("data-tab");
			// 在点击选项卡时触发自定义事件
			element.trigger("change.tabs", tabName);
		});
		// 绑定到自定义事件
		element.bind("change.tabs", function (e, tabName) {
			element.find("li > a").removeClass("selected selectedFirst selectedLast");
			var $a = element.find(">[data-tab='" + tabName + "']").find('a');
			if($a.hasClass('first')){
				$a.addClass("selectedFirst");
			}else if($a.hasClass('last')) {
				$a.addClass("selectedLast");
			}else {
				$a.addClass("selected");
			}
		});
		element.bind("change.tabs", function (e, tabName) {
			control.find(">[data-tab]").hide();
			control.find(">[data-tab='" + tabName + "']").show();
		});
		// 激活第1个选项卡
		var firstName = element.find("li:first").attr("data-tab");
		element.trigger("change.tabs", firstName);
		return this;
	}
})(jQuery)