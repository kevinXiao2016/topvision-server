$(document).ready(function(){
	// at the beginning of everything, decive unit
	if(spectrumUnit == 'dBmV'){
		spectrum.chartAttribute.yMin = -80;
		spectrum.chartAttribute.yMax = 20;
		spectrum.chartAttribute.unitStr = 'dBmV';
		$('#referenceLevel_title').text('@spectrum.referenceLevel@(dBmV)');
	}else{
		$('#referenceLevel_title').text('@spectrum.referenceLevel@(@spectrum.dbuv@)');
	}
	
	if($.browser.msie){
		//如果是IE浏览器，则提醒使用firefox或者chrome去打开频谱
		$('#browerTip').show();
		//如果是IE8及以下，则标记，以便图表直接刷新
		if(parseInt($.browser.version)<9){
			spectrum.isUnderIE9 = true;
		}
	}
	//TODO 如果是A或者C-A，则显示其上联OLT开关
	if(EntityType.isCcmtsWithoutAgentType(productType)){
		$('#oltSwitch').show();
		if(spectrumOltSwitch=="true" || spectrumOltSwitch==true){
			$('#oltSwitch_a').removeClass('offImg').addClass('onImg').attr('nm3ktip', '@spectrum.on@');;
		}else{
			$('#oltSwitch_a').removeClass('onImg').addClass('offImg').attr('nm3ktip', '@spectrum.off@');;
		}
	}
	$('#oltSwitch_refresh').bind('click',function(){
		$.ajax({
	    	url:'/cmcSpectrum/loadOltSwitchStatus.tv',
		  	type:'POST',
		  	dateType:'json',
		  	data:{cmcId : cmcId},
	        success:function(result){
	        	if(result.status){
	        		$('#oltSwitch_a').removeClass('offImg').addClass('onImg').attr('nm3ktip', '@spectrum.on@');;
	        	}else{
	        		$('#oltSwitch_a').removeClass('onImg').addClass('offImg').attr('nm3ktip', '@spectrum.off@');;
	        	}
	        },
		  	error:function(){
		  	},
		  	cache:false
	    });
	});
	
	//创建顶部工具栏
	spectrum.toolbar = new Ext.Toolbar({
        renderTo: "toolbar",
        items: [
	       {text: '@spectrum.startCollect@', itemId:'startSpectrum', iconCls: 'bmenu_play', handler: spectrum.startCollect},'-',
	       {text: '@spectrum.stopCollect@', itemId:'endSpectrum', iconCls: 'bmenu_stop', handler: spectrum.stopCollect},'-',
	       {text: '@spectrum.reset@', iconCls: 'bmenu_refresh', handler: spectrum.resetTotalSpectrumLine},'-',
	       {text: '@spectrum.reduceReferPower@', itemId:'reduceReferPower', iconCls: 'bmenu_arrDown', handler: function(){spectrum.modifyReferencePower(-10)}},'-',
	       {text: '@spectrum.addReferPower@', itemId:'addReferPower', iconCls: 'bmenu_arrUp', handler: function(){spectrum.modifyReferencePower(10)}},'-',
	       {text: '@spectrum.realTimeVideo@', itemId:'realTimeVideo', iconCls: 'bmenu_equipment', handler: spectrum.openRealTimeVideo},'-'
       ]
    });
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
			numberFormat:"n"
		});
	});
	$('#center_frequency').val(parseFloat(41.05).toFixed(2));
	$('#frequency_span').val(parseFloat(81.10).toFixed(2));
	$('#start_frequency').val(parseFloat(0.50).toFixed(2));
	$('#end_frequency').val(parseFloat(81.60).toFixed(2));
	
	// 如果支持频谱优化，频宽改成5-65
	if(supportSpectrumOptimize || oltSupportSpectrumOptimize) {
		spectrum.chartAttribute.xMin = 5;
		spectrum.chartAttribute.xMax = 65;
		spectrum.chartAttribute.xMinForReset = 5;
		spectrum.chartAttribute.xMaxForReset = 65;
	}
	
	// add by fanzidong, 根据频宽动态生成数据
	spectrum.renderChannelWidth(spectrum.chartAttribute.xMin, spectrum.chartAttribute.xMax);
	
	//创建图表
	//让container占据剩余空间
	$('#container').height($(window).height()-$('#container').offset().top-$("#bottom_message_div").outerHeight(true));
	//初始化数据
	var lineData = spectrum.lineData,
		chartAttr = spectrum.chartAttribute;
	spectrum.originalChannel = {};
	spectrum.channel = {};
	$.extend(true, spectrum.originalChannel, upChannels);
	$.extend(true, spectrum.channel, upChannels);
	var channel = spectrum.channel;
	//初始化信道的值
	$('#channel_center_1').val(channel.channel_1 && channel.channel_1.center);
	$('#channel_center_2').val(channel.channel_2 && channel.channel_2.center);
	$('#channel_center_3').val(channel.channel_3 && channel.channel_3.center);
	$('#channel_center_4').val(channel.channel_4 && channel.channel_4.center);
	$('#channel_width_1').val(channel.channel_1 && channel.channel_1.width);
	$('#channel_width_2').val(channel.channel_2 && channel.channel_2.width);
	$('#channel_width_3').val(channel.channel_3 && channel.channel_3.width);
	$('#channel_width_4').val(channel.channel_4 && channel.channel_4.width);
	
	if(!channel.channel_1 || !channel.channel_1.adminStatus || !downChannelOpened){
		$('#channel_status_1').removeClass('onImg').addClass('offImg');
		$('#channel_tr_1').hide();
		//弹窗告知用户信道1关闭，无法开启录像
		// add by fanzidong@20171020,增加至少一个下行信道开启的限制
		var msgBox = new Nm3kMsg({
			title: '@spectrum.tip@',
			html: '<b class="orangeTxt">@spectrum.channel1Close@</b>',
			okBtnTxt:'@spectrum.gotoUpChannelPage@',
			okBtn : true,
			okBtnCallBack:function(){
				window.location.href = '/cmc/channel/showUpChannelList.tv?module=4&cmcId='+parseInt(cmcId)+'&productType='+parseInt(productType);
			},
			cancelBtn :true,
			cancelBtnTxt:'@COMMON.close@',
			cancelBtnCallBack:function(){
			},
			timeLoading: true,
			autoHide : false,
			id: "nm3kSaveOrDelete"		
		});
		msgBox.init();
	}
	if(!channel.channel_2 || !channel.channel_2.adminStatus){
		$('#channel_status_2').removeClass('onImg').addClass('offImg');
		$('#channel_tr_2').hide();
	}
	if(!channel.channel_3 || !channel.channel_3.adminStatus){
		$('#channel_status_3').removeClass('onImg').addClass('offImg');
		$('#channel_tr_3').hide();
	}
	if(!channel.channel_4 || !channel.channel_4.adminStatus){
		$('#channel_status_4').removeClass('onImg').addClass('offImg');
		$('#channel_tr_4').hide();
	}
	//如果四个信道都关闭，则隐藏信道提示
	if( (!channel.channel_1 || !channel.channel_1.adminStatus) 
		&& (!channel.channel_2 || !channel.channel_2.adminStatus)
		&& (!channel.channel_3 || !channel.channel_3.adminStatus)
		&& (!channel.channel_4 || !channel.channel_4.adminStatus)){
		$('#channel_tr_comment').hide();
	}
	
	var lineColors = spectrum.lineColors;
	for(var key in spectrum.originalChannel){
		var currentChannel = spectrum.originalChannel[key];
		if(currentChannel.adminStatus){
			spectrum.seriesOptions.push({
				type: 'arearange',
				name: key,
				lineWidth: 0,
				color: lineColors[key],
				zIndex: 0,
				data:[[currentChannel.start, chartAttr.yMin, chartAttr.yMax], 
				      [currentChannel.end, chartAttr.yMin, chartAttr.yMax]]
			});
		}
	}
	
	spectrum.seriesOptions.push({
		type:'spline',
	    data: lineData['line_real'],
	    name: 'line_real',
	    color: 'black',
	    animation:false,
		marker: {
            enabled: false
        }
	});
	
	spectrum.reCreatChart();
	
	var chart = spectrum.chart;
	chartAttr.xAxisLength = chart.plotSizeX;
	chartAttr.yAxisLength = chart.plotSizeY;
	chartAttr.yAxisPaddingLeft = chart.plotLeft;
	chartAttr.xValuePerPx = (chartAttr.xMax - chartAttr.xMin) / chartAttr.xAxisLength;
	chartAttr.yValuePerPx = 100 / chartAttr.yAxisLength;
	chartAttr.yAxisMaxTop = $('#container').offset().top+chart.plotTop;
	chartAttr.center_frequency_padding_left = (chartAttr.mainBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
	//显示出X轴浮标
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
			//显示对应的曲线，并置对应的浮标为可选，并置复位按钮可选
			spectrum.showSpecturmLine(lineName);
			$('#reset_'+cbxId.split('_')[1]).removeClass('disabledAlink');
		}else{
			//删除对应的曲线,并置对应的浮标为不可选，并判断是否只剩下一个勾选的曲线，并置复位按钮不可选
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
			}else{
				//隐藏水平浮标
				$('#buoyLine_y').hide();
				//隐藏水平浮标的提示信息
				$('#buoy_y_div').hide();
			}
			spectrum.updateBasicAttrAndBuoy();
		}else if(buoyId == "buoyRadio_real" || buoyId == "buoyRadio_max" || buoyId == "buoyRadio_min" || buoyId == "buoyRadio_average"){
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
			spectrum.updateBasicAttrAndBuoy();
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
	$('#reset_centerFrequency').bind('click',function(){spectrum.resetCenterFrequency()});
	$('#modify_frequencySpan').bind('click',function(){spectrum.modifyFrequencySpan()});
	$('#reset_frequencySpan').bind('click',function(){spectrum.resetFrequencySpan()});
	$('#modify_startFrequency').bind('click',function(){spectrum.modifyStartFrequency()});
	$('#reset_startFrequency').bind('click',function(){spectrum.resetStartFrequency()});
	$('#modify_endFrequency').bind('click',function(){spectrum.modifyEndFrequency()});
	$('#reset_endFrequency').bind('click',function(){spectrum.resetEndFrequency()});
	$('#channel_a_1').bind('click',function(){spectrum.modifyChannelData(1)});
	$('#channel_a_2').bind('click',function(){spectrum.modifyChannelData(2)});
	$('#channel_a_3').bind('click',function(){spectrum.modifyChannelData(3)});
	$('#channel_a_4').bind('click',function(){spectrum.modifyChannelData(4)});
	$('#resetChannelData').bind('click',function(){spectrum.resetChannelData()});
	$('#channel_save_1').bind('click',function(){spectrum.saveChannelData(1)});
	$('#channel_save_2').bind('click',function(){spectrum.saveChannelData(2)});
	$('#channel_save_3').bind('click',function(){spectrum.saveChannelData(3)});
	$('#channel_save_4').bind('click',function(){spectrum.saveChannelData(4)});
	//$('#saveChannelData').bind('click',function(){spectrum.saveChannelData()});
	
	//为录像绑定事件
	$('#realTime_video_close').bind('click',function(){spectrum.closeRealTimeVideo();});
	$('#realTime_video_button').bind('click',function(){spectrum.startOrStopVideo()})
	
	//页面resize事件
	$(window).resize(function() {
		autoSetArrPosition();
		//给highchart一点响应时间
		throttle(spectrum.updateBasicAttrAndBuoy);
	});
	
	if(window.onbeforeunload){
		window.onbeforeunload = function(){
			//关闭实时频谱采集
			spectrum.stopCollect();
		}
	}
	
	function throttle(method, context) { 
		clearTimeout(method.tId);
		method.tId= setTimeout(function(){ 
			method.call(context); 
		}, 200);
	}
	
	spectrum.toolbar.getComponent("startSpectrum").enable();
	spectrum.toolbar.getComponent("endSpectrum").disable();
	
	if(channel.channel_1.adminStatus && downChannelOpened){
		if(EntityType.isCcmtsWithoutAgentType(productType)) {
			//集中型，需要考虑OLT开关
			if(spectrumOltSwitch=="true" || spectrumOltSwitch==true) {
				spectrum.startCollect();
			}
		} else {
			//独立型
			spectrum.startCollect();
		}
	} else {
		spectrum.toolbar.getComponent("startSpectrum").disable();
	}
});

/**
 * 判断设备是否需要降级
 * @returns {Boolean}
 */
function needReduce() {
	// 如果系统刷新频率为300ms/600ms，但是版本不支持或者浏览器不支持时，提示并降级
	return (timeInterval == 300 || timeInterval == 600) && (!supportSpectrumOptimize || spectrum.isUnderIE9);
}
