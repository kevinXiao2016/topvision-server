(function($){
	$.xTipWin  = function(opt){
		this.win_container 	= null;
		this.win_header		= null;
		this.win_content   	= null;
		//0 hide,1 show
		$.xTipWin.status	= 0;
		var opts			=  $.extend({width:300,height:150,autoClose:5000,autoShow:10000,ajax:'',cache:true,iframe:false,content:'hehe',reload:false,element:null},opt);
		$.xTipWin.opts		= opts;
		install(opts);
		function install(options){
			var tc = [],i=0;
			tc[i++] = '<div id="x-tip-window">';
			tc[i++] = '<table class="x-window-table" id="x-window-table" onselectstart="return false;" oncontextmenu="return false;">';
			tc[i++] = '<tr><td class="x-window-lt"></td><td class="x-window-ct" id="td-window-header"></td><td class="x-window-rt"></td></tr>';
			tc[i++] = '<tr><td class="x-window-lc"></td><td class="x-window-cc" id="td-window-content"></td><td class="x-window-rc"></td></tr>';
			tc[i++] = '<tr><td class="x-window-lb"></td><td class="x-window-cb"></td><td class="x-window-rb"></td></tr>';
			tc[i++] = '</table>';
			tc[i++]	= '</div>';
			tc = tc.join('');
			$(tc).css({display: 'none', width: options.width, height: 0}).appendTo($('body'));
			this.win_container = $('#x-tip-window');
			this.win_header    = $('<div id="x-tip-header"><div style="padding-top:8px;font-weight:bold;font-size:10pt;cursor:default">'+options.title+'</div><div id="x-tip-close" title="' + options.closeTip + '"></div></div>').appendTo($('#td-window-header'));

			this.win_content   = $('<div id="x-tip-content"></div>').css({'height':options.height}).appendTo($('#td-window-content'));
			$(window).scroll(redraw).resize(redraw)
			$('#x-tip-close').click(function(){close(opts)})
			/////@author : bravin ,bind a close handler to stop music
			if(options.closeHandler)
				$('#x-tip-close').bind('click',options.closeHandler)
			$('#x-tip-close').bind('mouseover', function(){$('#x-tip-close').css('backgroundImage', 'url(css/images/messanger/dialog_closebtn_over.png)')});
			$('#x-tip-close').bind('mouseout', function(){$('#x-tip-close').css('backgroundImage', 'url(css/images/messanger/dialog_closebtn.png)')});
			$.xTipWin.show	= show;
			$.xTipWin.close	= close;
			if(options.ajax !=''){//ajax load
				if(options.iframe){//use iframe
					$('<iframe id="x-tip-iframe" frameborder="0" src="'+options.ajax+'" onload="$.xTipWin.show($.xTipWin.opts,true)"></iframe>')
					.appendTo($('#x-tip-content'))
					.css({width:'100%',height:options.height,'border':0});
				}else{
					$.get(options.ajax,function(data){
						$('#x-tip-content').html(data);
						show(opts,true);
					});
				}
			}else if(options.element){//use content
				options.element.appendTo($('#x-tip-content'));
				options.element.show();
				//show(opts);
			}else {
				$('#x-tip-content').html(options.content);
				//show(opts);
			}
			//setInterval(function(){show(opts)},options.autoShow);
		}
		function show(opts,ajax){
			if($.xTipWin.status == 1) return;
			$.xTipWin.status = 1;
			if(opts.ajax !='' && opts.iframe == false && opts.cache==false && ajax!==true){
				$.get(opts.ajax,function(data){
					$('#x-tip-content').html(data);
					_show(opts);
				});
			}else if(opts.ajax !='' && opts.iframe == true && opts.cache==false && ajax!==true){
				$('#x-tip-iframe').empty().remove();
				$('<iframe id="x-tip-iframe" frameborder="0" src="'+opts.ajax+'"></iframe>')
				.appendTo($('#x-tip-content'))
				.css({width:'100%',height:opts.height,'border':0});
				_show(opts);
			}else{
				_show(opts);
			}
		}
		function _show(opts){
			var scroll  	 = getPageScroll();
			$('#x-window-table').show();
			$('#x-tip-window').css({bottom:1-scroll.top,right:1-scroll.left}).animate({height:opts.height+56},1000,function(){});
			//$("#x-tip-window").show('highlight',{},'normal');
			if(opts.autoClose > 0){
				setTimeout(function(){close(opts)},opts.autoClose);
			}
		}
		function redraw(){
			if($.xTipWin.status == 0) return;
			var scroll  	 = getPageScroll();
			$('#x-tip-window').css({bottom:1-scroll.top,right:1-scroll.left});
		}
		function close(opts){
			//if($.xTipWin.status == 0) return;
			$.xTipWin.status = 0;
			var win_container = $('#x-tip-window');
			//win_container.animate({height:0},1000,function(){win_container.css('display','none')});
			win_container.hide();
			//$("#x-tip-window").hide('highlight',{}, 'normal');
		}
		function getPageScroll() {
			var xScroll, yScroll;
			if (self.pageYOffset) {
				yScroll = self.pageYOffset;
				xScroll = self.pageXOffset;
			} else if (document.documentElement && document.documentElement.scrollTop) {	 // Explorer 6 Strict
				yScroll = document.documentElement.scrollTop;
				xScroll = document.documentElement.scrollLeft;
			} else if (document.body) {// all other Explorers
				yScroll = document.body.scrollTop;
				xScroll = document.body.scrollLeft;
			}
			return {left:xScroll,top:yScroll}
		}
		function getPageHeight() {
			var windowHeight
			if (self.innerHeight) {	// all except Explorer
				windowHeight = self.innerHeight;
			} else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
				windowHeight = document.documentElement.clientHeight;
			} else if (document.body) { // other Explorers
				windowHeight = document.body.clientHeight;
			}
			return windowHeight
		}
	}
})(jQuery);
