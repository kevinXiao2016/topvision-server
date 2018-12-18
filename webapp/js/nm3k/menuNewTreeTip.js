//左侧树菜单，文字过长红色提示信息;
$(function(){
	$("#tree a").hover(function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth();		
		if( w >= 186){
			var txt = $this.text();
			var tPos = $this.offset().top + 100;
			var o = {text:txt,display:"show",top:tPos};
			top.redTip(o);
		}
	},function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth(); 
		if(w >= 186){
			top.redTip({display:"hide"});
		}
	});
});//end document.ready;