/**
* 获取对应录像的对应帧的画面
* @namespace spectrum
* @method getSpectrumFrame
* @param {Number} videoId 录像ID
* @param {Number} frameIndex 帧数Index
*/
spectrum.getSpectrumFrame = function(frameIndex){
	var lineData = spectrum.lineData, seriesOptions = spectrum.seriesOptions, chartAttr = spectrum.chartAttribute;
	
	if(frameIndex===0){
		//此时应该显示空图像
		lineData.line_real = [];
		lineData.line_max = [];
		lineData.line_min = [];
		lineData.line_average = [];
		for(var i=0; i < seriesOptions.length; i++){
			seriesOptions[i].data = [];
		}
		spectrum.reCreatChart();
	}else if(frameIndex>videoJSON.frameCount){
		return
	}
	
	$.ajax({
    	url:'/cmcSpectrum/getSpectrumVideoFrame.tv',
	  	type:'POST',
	  	dateType:'json',
	  	data:{
	  		videoId:videoId, 
	  		frameIndex:frameIndex
	  	},
        success:function(ret){
        	//var ret = Ext.decode(ret);
        	var data = ret.data;
        	spectrum.chartAttribute.xMin = ret.startFreq / 1000000;
    		spectrum.chartAttribute.xMax = ret.endFreq / 1000000;
    		
        	var channels = ret.channels, curChl;
        	//更新频谱信道数据
        	updateChannelOption(channels);
    		//根据获取的数据，更新各条线的数据
        	if($.isArray(data)){
        		spectrum.caculateLine(data);
        		if(spectrum.isUnderIE9){
					spectrum.reCreatChart();
				}else{
					var chartAttr = spectrum.chartAttribute;
					var sampledData = spectrum.getSampledData(chartAttr.xMin, chartAttr.xMax, 500);
					var line_real = sampledData.line_real,
						line_max = sampledData.line_max,
						line_min = sampledData.line_min,
						line_average = sampledData.line_average,
						seriesOptions = spectrum.seriesOptions,
						series = spectrum.chart.series;
					
					// modify by fanzidong, 更新X轴
                    spectrum.chart.xAxis[0].setExtremes(chartAttr.xMin, chartAttr.xMax, false, false);
                    
					//更新曲线中的对应的series
					for(var i=0; i<series.length; i++){
						switch(series[i].name){
						case 'line_real':
							series[i].setData(line_real,false);
							break;
						case 'line_max':
							series[i].setData(line_max,false);
							break;
						case 'line_min':
							series[i].setData(line_min,false);
							break;
						case 'line_average':
							series[i].setData(line_average,false);
							break;
						}
					}
					//更新信道对应的series
					for(var i=0, len=seriesOptions.length; i<len; i++){
						if(seriesOptions[i].type != 'arearange') continue;
						var existed = false;
						for(var j=0; j<series.length; j++){
							if(series[j].name == seriesOptions[i].name){
								existed = true;
								series[j].setData(seriesOptions[i].data, false);
							}
						}
						if(!existed){
							spectrum.chart.addSeries(seriesOptions[i], true,false);
						}
					}
					spectrum.chart.redraw();
				}
        		spectrum.updateTopBuoyMessage();
        		//此时需要刷新顶部信息栏的信息
        		if($('#buoyLine_x').css('display')!=="none"){
        			spectrum.updateTopBuoyMessage("buoyLine_x");
        		}
        		if($('#buoyLine_add').css('display')!=="none"){
        			spectrum.updateTopBuoyMessage("buoyLine_add");			
        		}
        	}
        },
	  	error:function(){
	  	},
	  	cache:false
    });
	
	/**
	 * 更新spectrum.seriesOptions中的信道信息
	 * 
	 * channels为信道数组，每一个元素组成为：
	 * channelFreq，channelId，channelStatus，channelWidth
	 * 需要进行转换，才能用于图表展示
	 */
	function updateChannelOption(channels){
		for(var i=0, len=channels.length; i<len; i++){
			var curChl = channels[i];
    		var start = (curChl.channelFreq - curChl.channelWidth/2)/1000000;
    		var end = (parseInt(curChl.channelFreq) + parseInt(curChl.channelWidth/2))/1000000;
			var existed = false;
			for(var j=0, jLen=seriesOptions.length; j<jLen; j++){
				if(seriesOptions[j].name == 'channel_'+curChl.channelId){
					existed = true;
					seriesOptions[j].data = [[start, chartAttr.yMin, chartAttr.yMax], 
					   				      [end, chartAttr.yMin, chartAttr.yMax]];
				}
			}
			if(!existed){
				seriesOptions.push({
					type: 'arearange',
					name: 'channel_'+curChl.channelId,
					lineWidth: 0,
					color: spectrum.lineColors['channel_'+curChl.channelId],
					zIndex: 0,
					data:[[start, chartAttr.yMin, chartAttr.yMax], 
					      [end, chartAttr.yMin, chartAttr.yMax]]	
	    		});
			}
		}
	}
}

$(function(){
	// at the beginning of everything, decive unit
	if(spectrumUnit == 'dBmV'){
		spectrum.chartAttribute.yMin = -80;
		spectrum.chartAttribute.yMax = 20;
		spectrum.chartAttribute.unitStr = 'dBmV';
		$('#referenceLevel_title').text('@spectrum.referenceLevel@(dBmV)');
	}else{
		$('#referenceLevel_title').text('@spectrum.referenceLevel@(@spectrum.dbuv@)');
	}
	
	if(startFreq) {
		spectrum.chartAttribute.xMin = parseInt(startFreq) / 1000000;
	}
	if(endFreq) {
		spectrum.chartAttribute.xMax = parseInt(endFreq) / 1000000;
	}
	
	//初始化图表
	var lineData = spectrum.lineData,
		chartAttr = spectrum.chartAttribute;
	spectrum.seriesOptions = [{
		type:'spline',
	    data: lineData['line_real'],
	    name: 'line_real',
	    color: 'black',
	    animation:false,
		marker: {
            enabled: false
        }
	}];
	
	if($.browser.msie){
		//如果是IE浏览器，则提醒使用firefox或者chrome去打开频谱
		$('#browerTip').show();
		//如果是IE8及以下，则标记，以便图表直接刷新
		if(parseInt($.browser.version)<9){
			spectrum.isUnderIE9 = true;
		}
	}
	
	var durationSeconds = (videoJSON.endTime.time - videoJSON.startTime.time)/1000; 
	
	function formatContinueTime(continueSeconds){
		if(continueSeconds<60){
			return "00:00:"+ checkNum(continueSeconds);
		}else{
			return "00:"+checkNum(parseInt(continueSeconds/60))+":"+checkNum(continueSeconds%60);
		}
	}
	
	function checkNum(num){
		return (num<10) ? "0" + num : num;
	}
	
	$('#nm3kPlayer').playerControl({
		startTime : videoJSON.startTime.time,
		endTime : videoJSON.endTime.time,
		firstFrameTime : videoJSON.firstFrameTime.time,
		frameInterval : videoJSON.timeInterval,
		playInterval: videoJSON.timeInterval<1000 ? videoJSON.timeInterval : 1000,
		callback : spectrum.getSpectrumFrame
	});
	
	$('#nm3kPlayer').find('.nm3kTip.nm3kPlayerBtnPlay').trigger('mousedown');
	
	$('#videoName').text(videoJSON.videoName);
	$('#videoTimeInfo').text("@realTimeVideo.recordLength@"+"@COMMON.maohao@"+formatContinueTime(durationSeconds) + "(" + videoJSON.startTimeString + " - " + videoJSON.endTimeString + ")");
	
	//创建右侧工具栏
	//设置右侧箭头国际化
	(language == 'en_US') ? $("#sideArrow").attr("class","spectrumSideEnglish") : $("#sideArrow").attr("class", "spectrumSideChinese");
	//设置右侧箭头在屏幕中间，并加上动画
	function autoSetArrPosition() {
		var h = $(window).height(), h2 = (h - 140) / 2;
		if (h2 < 0) {h2 = 0;}
		$("#sideArrow").css({top: h2});
	}
	autoSetArrPosition();
	$("#sideArrow").click(function() {
		if ($("#arrow").hasClass("cmListSideArrLeft")) { //展开
			spectrum.openSlidebar();
		} else { //折叠;
			spectrum.closeSlidebar();
		}
	});
	//手风琴风格及spinner控件初始化
	$("#accordion").accordion({
		header: "> div > h3",
		animate:300,
		collapsible: true
	}).sortable({
		axis: "y",
		handle: "h3",
		stop: function( event, ui ) {
			// IE doesn't register the blur when sorting
			// so trigger focusout handlers to remove .ui-state-focus
			ui.item.children( "h3" ).triggerHandler( "focusout" );
		}
	});
	$("#reference_power").spinner({
		step:1,
		max:220,
		min:-210
	});
	$('input.frequencyInput').each(function(){
		$(this).spinner({
			step:0.02,
			min : 0,
			numberFormat:"0.00"
		});
	});
	$('#center_frequency').val(parseFloat(41.05).toFixed(2));
	$('#frequency_span').val(parseFloat(81.10).toFixed(2));
	$('#start_frequency').val(parseFloat(0.50).toFixed(2));
	$('#end_frequency').val(parseFloat(81.60).toFixed(2));
	
	//计算container应有的高度
	$('#container').height($(window).height()-$('#container').offset().top-$("#bottom_message_div").outerHeight(true)-$("#nm3kPlayer").outerHeight(true));
	
	spectrum.reCreatChart();
	
	var chart = spectrum.chart;
	chartAttr.xAxisLength = chart.plotSizeX;
	chartAttr.yAxisLength = chart.plotSizeY;
	chartAttr.yAxisPaddingLeft = chart.plotLeft + $('#chart_wrapper').offset().left;
	chartAttr.xValuePerPx = (chartAttr.xMax - chartAttr.xMin) / chartAttr.xAxisLength;
	chartAttr.yValuePerPx = 100 / chartAttr.yAxisLength;
	chartAttr.yAxisMaxTop = $('#container').offset().top+chart.plotTop;
	chartAttr.center_frequency_padding_left = (chartAttr.mainBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
	//显示出浮标
	$('#buoyLine_x').show().css({
		"width": 2,
		"height": chartAttr.yAxisLength,
		"left": chartAttr.center_frequency_padding_left,
		"top": chartAttr.yAxisMaxTop,
		"background-color": 'black'
	});
	spectrum.updateTopBuoyMessage("buoyLine_x");
	
	//事件绑定
	//为曲线展示checkbox绑定事件
	$('#line_ul').bind('click',function(event){
		var cbxId = event.target.id,
			checked = event.target.checked,
			className = event.target.className;
		if(className=="pointer_label"){return;}
		//拼接出对应的曲线name
		var lineName = "line_" + cbxId.split('_')[1];
		if(checked){
			//显示对应的曲线，并置对应的浮标为可选
			spectrum.showSpecturmLine(lineName);
			$('#reset_'+cbxId.split('_')[1]).removeClass('disabledAlink');
		}else{
			//删除对应的曲线,并置对应的浮标为不可选，并判断是否只剩下一个勾选的曲线
			spectrum.deleteSpecturmLine(lineName);
			$('#reset_'+cbxId.split('_')[1]).addClass('disabledAlink');
		}
		spectrum.updateBasicAttrAndBuoy();
	});
	$('#reset_max').bind('click',function(event){
		if($(this).hasClass("disabledAlink")){
			return;
		}
		spectrum.resetSpectrumLine("line_max");
		event.stopPropagation()
	});
	$('#reset_min').bind('click',function(event){
		if($(this).hasClass("disabledAlink")){
			return;
		}
		spectrum.resetSpectrumLine("line_min");
		event.stopPropagation()
	});
	$('#reset_average').bind('click',function(event){
		if($(this).hasClass("disabledAlink")){
			return;
		}
		spectrum.resetSpectrumLine("line_average");
		event.stopPropagation()
	});
	
	//为浮标radio和checkbox绑定事件
	$("#buoy_ul").bind("click", function(event){
		var buoyId = event.target.id,
			checked = event.target.checked,
			className = event.target.className;
		if(className=="pointer_label"){return;}
		if(buoyId == "buoyRadio_none"){
			//此时取消主浮标和增量浮标的显示，以及右上角提示信息的显示,并置增量checkbox为不可选
			$('#buoyLine_x').hide();
			$('#buoyLine_add').hide();
			$('#buoy_x_div').hide();
			$('#buoy_add_div').hide();
			$('#buoyCbx_add').attr("checked", false).attr("disabled", true);
			spectrum.updateBasicAttrAndBuoy();
		}else if(buoyId == "buoyCbx_add"){
			if(checked){
				//显示增量浮标,初始位置为中心频率所在点
				/*$('#buoyLine_add').show().css({
					"height": chartAttr.yAxisLength,
					"left": chartAttr.center_frequency_padding_left,
					"top": chartAttr.yAxisMaxTop
				});*/
				//显示增量浮标提示信息
				$('#buoyLine_add').show().css({
					"height": chartAttr.yAxisLength,
					"left": chartAttr.center_frequency_padding_left,
					"top": chartAttr.yAxisMaxTop
				});
				$('#buoy_add_div').show();
				chartAttr.addBuoyLine_x = (chartAttr.xMin + chartAttr.xMax)/2;
				spectrum.adjustBuoyPositionByValue('buoyLine_add');
				spectrum.updateTopBuoyMessage("buoyLine_add");
			}else{
				//隐藏增量浮标
				$('#buoyLine_add').hide();
				$('#buoy_add_div').hide();
			};
			spectrum.updateBasicAttrAndBuoy();
		}else if(buoyId == "buoyCbx_horizontal"){
			if(checked){
				//显示水平浮标，初始位置设置在正中间
				var initPaddingTop = chartAttr.yAxisMaxTop + 50 / chartAttr.yValuePerPx;
				$('#buoyLine_y').show().css({
					width: chartAttr.xAxisLength,
					left : chartAttr.yAxisPaddingLeft,
					top : initPaddingTop
				});
				$('#buoy_y_div').show();
				chartAttr.horizontalBuoyLine_y = (chartAttr.yMax + chartAttr.yMin)/2;
				spectrum.adjustBuoyPositionByValue("buoyLine_y");
				spectrum.updateTopBuoyMessage("buoyLine_y");
				//$('#buoyLine_y').show().css({
				//	width: chartAttr.xAxisLength,
				//	left : chartAttr.yAxisPaddingLeft,
				//	top : initPaddingTop
				//});
				//显示水平浮标的提示信息
				//$('#buoy_y_div').show();
				//
			}else{
				//隐藏水平浮标
				$('#buoyLine_y').hide();
				//隐藏水平浮标的提示信息
				$('#buoy_y_div').hide();
			}
			spectrum.updateBasicAttrAndBuoy();
		}else{
			//此时是选择除none之外的radio，此时需要修改提示信息的颜色和名字，并且修改Y值
			//拼接出曲线的name
			var lineName = "line_" + buoyId.split("_")[1];
			$('#buoy_x_div').show();
			$('#buoyLine_x').show();
			$('#buoyCbx_add').attr("disabled", false);
			$('#buoyLine_x').css("background-color", spectrum.lineColors[lineName]);
			$('#buoyColorTip_x').css("background-color", spectrum.lineColors[lineName]);
			$('#buoy_name').text(spectrum.lineDisplayName[lineName]);
			spectrum.updateTopBuoyMessage("buoyLine_x");
			spectrum.updateTopBuoyMessage("buoyLine_add");
		}
	})
	
	var intReg = /^-{0,1}\d+$/,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	//为数字输入框绑定事件，防止输入或者粘贴非法字符
	$('#reference_power').bind('keydown',function(e){
		e = window.event || e;   
		//参考电平只能输入0-9或者-
		var keyCode = e.keyCode;
		if(e.shiftKey){
			if(window.event){
				window.event.returnValue = false;
			} else{
				e.preventDefault();
			}
		}else if((48<=keyCode && keyCode<=57) || (96<=keyCode && keyCode<=105)){
			//输入的是0-9
		}else if(keyCode==189 || keyCode==173 || keyCode==109){
			//输入的是-
		}else if(keyCode!=8 && keyCode!=46){
			if(window.event){
				window.event.returnValue = false;
			} else{
				e.preventDefault();
			}
		}
	}).bind('keyup',function(e){
		e = window.event || e; 
		if(!intReg.test(this.value)){
			$('#reference_power_tip').show();
		}else{
			var value = parseInt(this.value)
			if(this.value<-210){
				$(this).val(-210);
			}else if(this.value > 220){
				$(this).val(220);
			}
			$('#reference_power_tip').hide();
		}
	});
	
	$('input.frequencyInput').each(function(){
		var $input = $(this);
		$input.bind('keydown',function(e){
			e = window.event || e;
			//只能输入0-9，"."
			var keyCode = e.keyCode;
			if(e.shiftKey){
				if(window.event){
					window.event.returnValue = false;
				} else{
					e.preventDefault();
				}
			}else if((48<=keyCode && keyCode<=57) || (96<=keyCode && keyCode<=105)){
				//输入的是0-9
			}else if(keyCode==190 || keyCode==110){
				//输入的是"."
			}else if(keyCode!=8 && keyCode!=46){
				if(window.event){
					window.event.returnValue = false;
				} else{
					e.preventDefault();
				}
			}
		}).bind('keyup', function(e){
			var $tip = ($('#'+this.id+"_tip").length>0) ? $('#'+this.id+"_tip") : $('#channel_tip');
			if(!positiveIntReg.test(this.value) && !positiveFloatReg.test(this.value)){
				$tip.text('@spectrum.frequencyInputTip@').show();
			}else{
				//如果输入的数字导致小数点后数字已经超过2位，则去掉后面的
				var value = $input.val();
				if(value.indexOf('.')!=-1 && value.length-1-value.indexOf('.')>2){
					$input.val(value.substring(0, value.indexOf('.')+3))
				}
				$tip.hide();
			}
		});
	});
	
	//为浮标绑定事件
	$('#realTime_video_title').bind('mouseover',function(){spectrum.moveObj(this, true, true)});
	$('#buoyLine_x').bind('mouseover',function(){spectrum.moveObj(this, true, false)});
	$('#buoyLine_add').bind('mouseover',function(){spectrum.moveObj(this, true, false)});
	$('#buoyLine_y').bind('mouseover',function(){spectrum.moveObj(this, false, true)});
	//为侧边栏中的按钮绑定事件
	$('#modify_referencePower').bind('click',function(){spectrum.modifyReferencePower()});
	$('#modify_centerFrequency').bind('click',function(){spectrum.modifyCenterFrequency()});
	$('#modify_frequencySpan').bind('click',function(){spectrum.modifyFrequencySpan()});
	$('#modify_startFrequency').bind('click',function(){spectrum.modifyStartFrequency()});
	$('#modify_endFrequency').bind('click',function(){spectrum.modifyEndFrequency()});
	
	//页面resize事件
	$(window).resize(function() {
		autoSetArrPosition();
		//给highchart一点响应时间
		throttle(spectrum.updateBasicAttrAndBuoy);
	});
	
	function throttle(method, context) { 
		clearTimeout(method.tId);
		method.tId= setTimeout(function(){ 
			method.call(context); 
		}, 200);
	}
});