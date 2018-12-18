/* leexiang @20150215 add progressBar */
function Nm3kProgressBar(o){
	this.width = o.width ? o.width : 700;
	this.steps = o.steps;
	this.renderTo = o.renderTo;
	this.loading = true;
	this.scollOuter = o.scollOuter;
}
Nm3kProgressBar.prototype = {
	init : function(){
		var $putProgress = $("#" + this.renderTo),
			 steps = this.steps,
			 len = steps.length,
			 stepW = parseInt( (this.width - 20 - 26) / (len-1), 10),
		     str = '';
		
		str += '<div class="nm3kProgressBar" style="width:'+ this.width +'px">';
		str +=     '<div class="nm3kProgressBarLineBg"></div>';
		str +=     '<div class="nm3kProgressBarLine" style="width:10px;"></div>';
		for(var i=0; i<len; i++){
			var leftPos = i*stepW + 10,
				 txtLeft = leftPos - stepW/2 + 13,
			     outer = "nm3kProgressBarCir nm3kProgressGray",
				 inner = "nm3kProgressInner nm3kProgressGray",
				 tableOuterId = steps[i].tableOuterId;
				 
			if(i==0){
				leftPos =10;
				outer = "nm3kProgressBarCir",
			    inner = "nm3kProgressInner nm3kProgressLoading";
			};
			if(i==len-1){
				leftPos = this.width-10 - 26
			};
		str +=     '<div class="'+ outer +'" style="left:'+ leftPos +'px">';
		str +=         '<div class="'+ inner +'"></div>';
		str +=     '</div>';
		str +=     '<div class="nm3kProgressTxt" style="left:'+ txtLeft +'px; width:'+ stepW +'px;" alt="'+ tableOuterId +'">'+ steps[i].txt +'</div>';
		}	 
		str += '</div>'
		$putProgress.html(str);
		this.unBindingTxtClick();
		this.bindingTxtClick();
	},
	bindingTxtClick : function(){
		var me = this;
		$("#" + this.renderTo).delegate(".nm3kProgressTxt", "click", function(){
			var alt = $(this).attr("alt"),
			    $div = $("#" + alt);
			if($div.length === 1){
				var t = $div.position().top,
				    t2 = (t >= 0) ? "+=" + t : "-=" + Math.abs(t);
				
				$("#" + me.scollOuter).animate({scrollTop: t2});
			}
		});
	},
	unBindingTxtClick : function(){
		$("#" + this.renderTo).undelegate(".nm3kProgressTxt", "click");
	},
	loadEnd : function(cssName,txtColor){//加载结束,设置当前loading的成功nm3kProgressSuccess或者失败;
		var fontColor = txtColor || "#333333",
		     $loading = $("#"+this.renderTo).find(".nm3kProgressLoading");
		$loading.removeClass("nm3kProgressLoading").addClass(cssName);
		if($loading.parent().next().hasClass("nm3kProgressTxt")){
			$loading.parent().next().css({color: fontColor});
		}
		
	},
	setLoadingStep : function(num){ //将第n个(n从0开始计算)设置为加载loading;
		var me = this,
			 $renderTo = $("#"+this.renderTo),
			 $outer = $renderTo.find(".nm3kProgressBarCir").eq(num),
	         $inner = $outer.find(".nm3kProgressInner"),
			 w = $outer.position().left;
			 
		$renderTo.find(".nm3kProgressBarLine").animate({width : w+13},'slow',function(){
			if(me.loading){
				$outer.removeClass("nm3kProgressGray");
				$inner.removeClass("nm3kProgressGray").addClass("nm3kProgressLoading");
				(me.steps[num].next)();
			}
		});
	},
	complete : function(){
		var $renderTo = $("#"+this.renderTo),
			$bar = $renderTo.find(".nm3kProgressBarLine"),
		    w = this.width;
		$bar.css({width: w});
	},
	cancelLoading : function(){ //取消加载;
		this.loading = false;
		var me = this,
			 $renderTo = $("#"+this.renderTo),
			 $outer = $renderTo.find(".nm3kProgressBarCir");
		$outer.each(function(){
			var $me = $(this),
			     $inner = $me.find(".nm3kProgressInner");
				 
			if($me.hasClass("nm3kProgressGray")){
				$inner.removeClass("nm3kProgressGray").addClass("nm3kProgressCancel");
			}
			if($inner.hasClass("nm3kProgressLoading")){
				$inner.removeClass("nm3kProgressLoading").addClass("nm3kProgressCancel");
				$me.addClass("nm3kProgressGray");
			}
		});
	},
	setLoading : function(num){
		var $renderTo = $("#"+this.renderTo),
		    $outer = $renderTo.find(".nm3kProgressBarCir").eq(num),
            $inner = $outer.find(".nm3kProgressInner");
		$outer.removeClass("nm3kProgressGray");
		$inner.removeClass("nm3kProgressGray").addClass("nm3kProgressLoading");
	},
	setLoadingEnd : function(num, cssName, txtColor){
		var fontColor = txtColor || "#333333",
		    $renderTo = $("#"+this.renderTo),
		    $outer = $renderTo.find(".nm3kProgressBarCir").eq(num),
	        $inner = $outer.find(".nm3kProgressInner");
		
		$inner.attr({"class" : "nm3kProgressInner "+cssName });
		if($inner.parent().next().hasClass("nm3kProgressTxt")){
			$inner.parent().next().css({color: fontColor});
		}
	    
	}
};