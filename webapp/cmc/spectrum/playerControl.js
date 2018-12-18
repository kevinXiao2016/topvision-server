(function($) {
	$.fn.playerControl = function(options) {
		var opts = $.extend({}, $.fn.playerControl.defaults, options), 
			formatTime = $.fn.playerControl.formatTime,
			currentTime = opts.startTime,
			currentSpeedIntervals = opts.speedInterval[opts.frameInterval],
			currentSpeedIndex = 0,
			currentSpeedNumber = 1;
		
		//生成结构
		var playerDivStr = '<div class="nm3kPlayWrap">'
			+     '<div class="nm3kPlayBar"></div>'
			+     '<div class="nm3kPlayBarProgress"></div>'
			+     '<div class="nm3kPlayBarBg"></div>'
			+ '</div>'
			+ '<div class="nm3kPlayerBtn">'
			+     '<a href="javascript:;" class="nm3kTip nm3kPlayerBtnBack" nm3kTip="@spectrum.slowDown@"></a>'
			+ 	  '<a href="javascript:;" id="controlSwitch" class="nm3kTip nm3kPlayerBtnPlay" nm3kTip="@spectrum.play@"></a>'
			+ 	  '<a href="javascript:;" class="nm3kTip nm3kPlayerBtnForward" nm3kTip="@spectrum.speedUp@"></a>'
			+ '</div>'
			+ '<div class="nm3kFrameTip"></div>'
			+ '<div class="nm3kSpeedTip">@spectrum.currentPlaySpeed@: <span class="speedNumber"></span>@spectrum.beishu@(<span class="speedInterval"></span>@spectrum.secPerFrame@)</div>';
		//加入录像播放器DIV，并给容器加上属性，使其不可选
		$(this).append(playerDivStr).attr({
			"class" : "nm3kPlayer",
			"unselectable" : "on",
			"onselectstart" : "return false",
			"style" : "-moz-user-select:none;-webkit-user-select:none;"
		});
		//初始化一些属性
		var $timeTip = $(this).find('.nm3kFrameTip'),
			$barWrap = $(this).find('.nm3kPlayWrap'),
			$bar = $(this).find('.nm3kPlayBar'),
			$barProgress = $(this).find('.nm3kPlayBarProgress'),
			$speedNumber = $(this).find('.speedNumber'),
			$speedInterval = $(this).find('.speedInterval'),
			barWidth = $bar.width(),
			progressLength = $barWrap.width(),
			totalSeconds = (opts.endTime-opts.startTime)/1000,
			pxPerSecond = progressLength / totalSeconds;
		
		//初始化时间显示
		$timeTip.text(formatTime(opts.startTime) + " / "+ formatTime(opts.endTime));
		playing(0);
		//初始化播放速率显示
		$speedNumber.text(currentSpeedNumber);
		var intervalNum = opts.frameInterval/(currentSpeedIntervals[currentSpeedIndex]*1000) ;
		if(intervalNum < 1) {
			$speedInterval.text((opts.frameInterval /(currentSpeedIntervals[currentSpeedIndex])) + 'ms');
		} else {
			$speedInterval.text(intervalNum + 's');
		}
		
		//绑定resize事件
		$(window).bind('resize', function(e){
			clearTimeout(this.resize);
			this.resize = setTimeout(function(){
				//这里是每次resize时真正需要执行的操作
				//重新定位长度和每一帧的长度，定位滚动条的位置和进度的位置
				barWidth = $bar.width();
				progressLength = $barWrap.width();
				pxPerSecond = progressLength / totalSeconds;
			}, 100);
		});
		
		//绑定响应事件
		$(this).bind('mousedown', function(e){
			var target = $(e.target);
			switch(target.attr("class")){
			case "nm3kTip nm3kPlayerBtnPlay":
				//如果按下的是播放按钮,开始播放录像
				target.removeClass('nm3kPlayerBtnPlay').addClass('nm3kPlayerBtnPause').attr('nm3kTip', '@spectrum.stopPlay@');
				opts.play = true;
				if(currentTime>=opts.endTime){
					//已经播放到结尾
					currentTime = opts.startTime;
				}
				playAction = setInterval(function(){
					playing();
				}, opts.playInterval);
				break;
			case "nm3kTip nm3kPlayerBtnPause":
				//如果按下的是暂停按钮
				stop();
				break;
			case "nm3kPlayBarBg":
			case "nm3kPlayBarProgress":
				//如果按下的是进度条,重新计算进度条位置
				setProgress(e);
				break;
			case "nm3kPlayBar":
				//如果按下的是当前播放点
				startMoveBar(e);
				break;
			case "nm3kTip nm3kPlayerBtnBack":
				slowDown();
				break;
			case "nm3kTip nm3kPlayerBtnForward":
				speedUp();
				break;
			};
		});
		
		return this;
		
		//播放函数
		function playing(changeTime){
			//时间变化
			if(changeTime===undefined){
				currentTime += opts.playInterval * currentSpeedNumber;
			}else{
				currentTime += changeTime;
			}
			if(currentTime>=opts.endTime){
				currentTime = opts.endTime;
				//已经播放到结尾
				stop();
			}
			//触发时间变化
			currentTimeChanged();
			//改变进度条的位置
			var left = (currentTime - opts.startTime) * pxPerSecond / 1000;
			$bar.css({left : left});
			$barProgress.width(left + barWidth/2);
		}
		
		function stop(){
			$('#controlSwitch').removeClass('nm3kPlayerBtnPause').addClass('nm3kPlayerBtnPlay').attr('nm3kTip', '@spectrum.play@');
			opts.play = false;
			if(typeof playAction !== 'undefined'){
				clearInterval(playAction);
			}
		}
		
		function setProgress(e){
			//设置进度条样式
			var clickX =  document.all ? e.clientX : e.pageX,
				startX = $barWrap.offset().left,
				paddingLeft = clickX -  startX - barWidth/2;
			$bar.css({left : paddingLeft});
			$barProgress.width(paddingLeft + barWidth/2);
			//算出当前的时间，并修改相应的项
			currentTime = opts.startTime + parseInt(paddingLeft / pxPerSecond) * 1000; 
			currentTimeChanged();
		}
		
		function startMoveBar(e){
			var target = e.target, 
				$target = $(e.target),
				lPos = $target.offset().left,
				tPos = $target.offset().top,
				leftEdge = $barWrap.offset().left,
				rightEdge = leftEdge + $barWrap.width() - barWidth;
			
			target.style.cursor = "pointer";
			
			//创建供拖动的半透明元素
			if($(".nm3kpalyerFakeBar").length > 0){
				$(".nm3kpalyerFakeBar").remove();
			};
			var $moveFakeBar = $('<div class="nm3kpalyerFakeBar"></div>').css({
				left : lPos + 'px',
				top : tPos + 'px'
			}).appendTo($('body'));
			
			if($moveFakeBar[0].setCapture){//ie
				$moveFakeBar.bind("mousemove", move);
				$moveFakeBar.bind("mouseup", stop);
				$moveFakeBar[0].setCapture();
			}else{
				$(window).on("mousemove", move);
				$(window).on("mouseup", stop);	
			}
			
			function move(e){
				var leftX = document.all ? e.clientX : e.pageX;
				//不能超出边界
				if(leftX<leftEdge){
					leftX=leftEdge;
				}else if(leftX>rightEdge){
					leftX = rightEdge;
				}
				$moveFakeBar.css({
					"left" : leftX - barWidth/2
				});
			}
			
			function stop(e){
				if($moveFakeBar[0].releaseCapture){//ie;
					$moveFakeBar.unbind("mousemove", move);
					$moveFakeBar.unbind("mouseup", stop);
					$moveFakeBar[0].releaseCapture();
				}else{
					$(window).off("mousemove", move);
					$(window).off("mouseup", stop);
				}
				$moveFakeBar.remove();
				setProgress(e);
			}
		}
		
		function currentTimeChanged(){
			//时间显示变化
			$timeTip.text(formatTime(currentTime) + " / "+ formatTime(opts.endTime));
			//获取此时应该播放哪一帧
			var curIndex = 0;
			if(currentTime >= opts.firstFrameTime){
				curIndex = parseInt((currentTime - opts.firstFrameTime) / opts.frameInterval) +1;
			}
			//如果与当前播放的一帧不同，则执行回调函数
			if(!curIndex || curIndex!==opts.frameIndex){
				opts.frameIndex = curIndex;
				opts.callback.call(window, opts.frameIndex);
			}
		}
		
		function speedUp(){
			if( currentSpeedIntervals.length > (currentSpeedIndex+1) ){
				//此时还未达到最大速度,更新倍速index，倍速数字
				currentSpeedNumber = currentSpeedIntervals[++currentSpeedIndex];
				//更新播放速率显示
				$speedNumber.text(currentSpeedNumber);
				$speedInterval.text(opts.frameInterval/(currentSpeedIntervals[currentSpeedIndex]*1000));
				//TODO 如果达到了最大倍速，则禁止点击加速按钮
			}
		}
		
		function slowDown(){
			if(currentSpeedIndex>0){
				//此时还未达到最小速度
				currentSpeedNumber = currentSpeedIntervals[--currentSpeedIndex];
				//更新播放速率显示
				$speedNumber.text(currentSpeedNumber);
				$speedInterval.text(opts.frameInterval/(currentSpeedIntervals[currentSpeedIndex]*1000));
				//TODO 如果达到了最小倍速，则禁止点击减速按钮
			}
		}
	}
	
	$.fn.playerControl.formatTime = function(time){
		if(typeof time !== 'number'){
			return '';
		}
		var date = new Date();
		date.setTime(time);
		return String.format("{0}-{1}-{2} {3}:{4}:{5}", date.getFullYear(), checkNum(date.getMonth()+1), 
			checkNum(date.getDate()), checkNum(date.getHours()), checkNum(date.getMinutes()), 
			checkNum(date.getSeconds()));
		
		function checkNum(num){
			return (num<10) ? "0" + num : num;
		}
	}
	
	$.fn.playerControl.defaults = {
		play : false,
		playInterval : 1000,
		frameIndex :0,
		speedInterval : {
			300: [1],
			1000 : [1],
			2000 : [1],
			3000 : [1],
			5000 : [1, 5],
			10000 : [1, 5, 10],
			60000 : [1, 5, 10, 15, 30, 60],
			300000 : [1, 5, 10, 15, 30, 60, 300],
			900000 : [1, 5, 10, 15, 30, 60, 300, 900]
		}
	};
})(jQuery);