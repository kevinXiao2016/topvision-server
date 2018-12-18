var SwitchButton = function(parentEl){
    this.parentObj = typeof parentEl == "string" ? document.getElementById(parentEl) : parentEl;
    this.create();
}
SwitchButton.prototype = {
	create : function(){
		var p = document.createElement("p");
		p.className = "field switch";
		this.parentObj.appendChild(p);
		var that = this;  
		var labelOn = document.createElement("label");
		p.appendChild(labelOn);
		labelOn.className = "cb-enable";
		var spanOn = document.createElement("span");
		labelOn.appendChild(spanOn);
		spanOn.innerHTML = "On";
		var labelOff = document.createElement("label");
		p.appendChild(labelOff);
		labelOff.className = "cb-disable selected";
		var spanOff = document.createElement("span");
		labelOff.appendChild(spanOff);
		spanOff.innerHTML = "Off";
		$(".cb-enable").click(function(){
			var parent = $(this).parents('.switch');
			$('.cb-disable',parent).removeClass('selected');
			$(this).addClass('selected');
		});
		$(".cb-disable").click(function(){
			var parent = $(this).parents('.switch');
			$('.cb-enable',parent).removeClass('selected');
			$(this).addClass('selected');
		});
	}
}
