/**
* CC实时频谱
*
* @module spectrum
*/
var spectrum = {
	isUnderIE9:false,
	chart:null,
	callbackId:-1,//modify by jay 2016-11-09 callbackId与heartbeatId相同，全部修改为callbackId
	seriesOptions:new Array(),
	chartAttribute: {
		xMin:0.5,
		xMax:81.6,
		xMinForReset: 0.5,
		xMaxForReset: 81.6,
		yMin:-20,
		yMax:80,
		xAxisLength:0, //X轴的长度像素值
		yAxisLength:0,  //Y轴长度像素值
		xValuePerPx:0, //X轴上每像素多少值
		yValuePerPx:0, //Y轴上每像素多少值
		yAxisPaddingLeft:0, //Y轴距页面左边像素值
		yAxisMaxTop:0, //Y轴最大刻度值对应于页面的上边距像素值
		mainBuoyLine_x: 41.05, //当前浮标的X值
		mainBuoyLine_y: 0,	//主浮标的当前Y值
		horizontalBuoyLine_y: 30,	//当前水平浮标的Y值
		addBuoyLine_x: 41.2,	//增量浮标的X值
		addBuoyLine_y: 0,
		center_frequency_padding_left:0,	//中心频点距离左边px
		unitStr: '@spectrum.dbuv@'
	},
	lineData:{
		frames : 0,
		'line_real': [],
		'line_max' : [],
		'line_min' : [],
		'line_average' : []
	},
	lineColors : {
		'line_real' : 'black',
		'line_max' : '#0000FF',
		'line_min' : '#CD853F',
		'line_average' : '#32CD32',
		'channel_1': '#2f7ed8',
		'channel_2': '#0d233a',
		'channel_3': '#8bbc21',
		'channel_4': '#910000'
	},
	lineDisplayName : {
		'line_real' : '@spectrum.realTimeLine@',
		'line_max' : '@spectrum.maxLine@',
		'line_min' : '@spectrum.minLine@',
		'line_average' : '@spectrum.averageLine@',
		'channel_1': '@spectrum.channel@1',
		'channel_2': '@spectrum.channel@2',
		'channel_3': '@spectrum.channel@3',
		'channel_4': '@spectrum.channel@4'
	},
	channel:null,
	originalChannel:null,
	videoStartTime:0,
	videoCallbackId:-1,//modify by jay 2016-11-09 callbackId与heartbeatId相同，全部修改为callbackId
	toolbar:null
}

// interval
var x = 65, arr = [];
while(x>=0.5){
	arr.push(x.toFixed(1));
	x -= 0.8;
}
spectrum.intervalArr = arr.reverse();

//实时和录像的轮询
var realTimeInterval, continueInterval;

/**
* 在曲线图上显示对应的线条(实时、最大保持、最小保持、平均)
* @namespace spectrum
* @method showSpecturmLine
* @param {Number} lineId 线条的ID(为：line_real/line_max/line_min/line_average)
*/
spectrum.showSpecturmLine = function(lineId){
	//如果lineId不是以上四个之一，则不做任何操作
	if($.inArray(lineId, ["line_real", "line_max", "line_min", "line_average"])==-1){
		return;
	}
	//初始化内部需要使用的变量，符合ES5标准
	var seriesOptions = spectrum.seriesOptions,
		lineData = spectrum.lineData,
		line_real = spectrum.getSampledData(spectrum.chartAttribute.xMin, spectrum.chartAttribute.xMax, 500).line_real,
		lineColors = spectrum.lineColors,
		chart = spectrum.chart;
	//获取对应的浮标ID(此处逻辑与html代码有关联)
	var buoyId = "buoyRadio_" + lineId.split("_")[1];
	//如果该曲线已经存在，则不予操作
	for(var j=0; j < seriesOptions.length; j++){
		if(seriesOptions[j].name===lineId){
			return;
		}
	}
	//将实时值赋予对应曲线
	if(lineId !== "line_real"){
		for(var i = 0,  length = line_real.length; i < length; i++){
			lineData[lineId][i] = [line_real[i][0], line_real[i][1]];
		}
	}
	if(lineId === "line_average"){
		lineData.frames = 0;
	}
	//获取抽样后的数据，进行赋值
	var sampledData = spectrum.getSampledData(spectrum.chartAttribute.xMin, spectrum.chartAttribute.xMax, 500);
	seriesOptions[seriesOptions.length] = {
		type:'spline',
		data: sampledData[lineId],
		name: lineId,
		color: lineColors[lineId],
		lineWidth:1,
		animation:false,
		marker: {
			enabled: false
		}
	};
	try{
		if(true){
			//如果是IE9以下，表示是由VML绘图，刷新效率过低，重新画图
			spectrum.reCreatChart();
		}else{
			chart.addSeries({
				type:'spline',
				data: sampledData[lineId],
				name: lineId,
				color: lineColors[lineId],
				lineWidth:1,
				animation:false,
				marker: {
					enabled: false
				}
			}, true,false);
		}
	}catch(e){}
	//置对应的浮标为可选
	$('#'+buoyId).attr("disabled",false);
	//如果曲线数量大于1，则checkbox均可选
	if($('#line_ul').find('input[type="checkbox"]:checked').length>1){
		$('#line_ul').find('input[type="checkbox"]').attr("disabled",false);
	}
}

/**
* 在曲线图上删除对应的线条(实时、最大保持、最小保持、平均)
* @namespace spectrum
* @method deleteSpecturmLine
* @param {Number} lineId 线条的ID(为：line_real/line_max/line_min/line_average)
* @param {Boolean} holdBuoy 是否保留浮标
*/
spectrum.deleteSpecturmLine = function(lineId, holdBuoy){
	//如果lineId不是以上四个之一，则不做任何操作
	if($.inArray(lineId, ["line_real", "line_max", "line_min", "line_average"])==-1){
		return;
	}
	//初始化内部需要使用的变量，符合ES5标准
	var chart = spectrum.chart,
		series = chart.series,
		seriesOptions = spectrum.seriesOptions;
	//获取对应的浮标ID(此处逻辑与html代码有关联)
	var buoyId = "buoyRadio_" + lineId.split("_")[1];
	for(var i = 0, length = series.length; i < length; i++){
		if(series[i].name==lineId){
			series[i].remove(false);
			break;
		}
	}
	//实时数据不能被清空，因为它是其他曲线的依据
	if(lineId === "line_real"){
		spectrum.lineData[lineId] = [];
	}
	for(var i=0, optionsLength = seriesOptions.length; i < optionsLength; i++){
		if(seriesOptions[i].name==lineId){
			seriesOptions.splice(i,1);
			break;
		}
	}
	if(lineId === "line_average"){
		spectrum.lineData.frames = 0;
	}
	if(!holdBuoy) {
		//置对应的浮标为不可选，并且如果此时显示的当前浮标，置为无
		if($('#'+buoyId).is(":checked")){
			//如果此时显示的浮标正好是将要删除的曲线，则取消浮标的显示及勾选，以及顶部的信息 
			$('#buoyRadio_none').attr("checked", "checked")
			$('#buoy_x_div').hide();
			$('#buoy_add_div').hide();
			$('#buoyLine_x').hide();
			$('#buoyLine_add').hide();
			$('#buoyCbx_add').attr("checked", false).attr("disabled", true);
		}
		$('#'+buoyId).attr("disabled", "disabled");
	}
	//如果只剩下一条曲线显示，则置灰其按钮表示不可取消显示
	var checkedLines = $('#line_ul').find('input[type="checkbox"]:checked');
	if(checkedLines.length==1){
		checkedLines.attr("disabled",true)
	}
}

/**
* 定位对应的信道
* @namespace spectrum
* @method locateChannel
*/
spectrum.locateChannel = function(channelId){
	if ($("#arrow").hasClass("cmListSideArrLeft")){
		spectrum.openSlidebar();
	}
	$("#accordion").accordion( "option", "active", 6 );
	//找到对应的信道所在的行
	$('#channel_tr_'+channelId).addClass('foucsChannel');
	setTimeout(function(){
		$('#channel_tr_'+channelId).removeClass('foucsChannel');
	},3000);
}

/**
* 打开侧边栏
* @namespace spectrum
* @method openSlidebar
*/
spectrum.openSlidebar = function(){
	//将侧边栏移动到对应位置
	$("#sideArrow").animate({right: 419});
	$("#sidePart").animate({right: 0});
	//将箭头更换为正确的
	$("#arrow").removeClass("cmListSideArrLeft").addClass("cmListSideArrRight");
}

/**
* 关闭侧边栏
* @namespace spectrum
* @method closeSlidebar
*/
spectrum.closeSlidebar = function(){
	//将侧边栏移动到对应位置
	$("#sideArrow").animate({right: 0});
	$("#sidePart").animate({right: -420});
	//将箭头更换为正确的
	$("#arrow").removeClass("cmListSideArrRight").addClass("cmListSideArrLeft");
}

/**
* 重新绘制曲线图
* @namespace spectrum
* @method reCreatChart
*/
spectrum.reCreatChart = function(){
	//初始化内部需要使用的变量，符合ES5标准
	var chartAttr = spectrum.chartAttribute,
		lineDisplayName = spectrum.lineDisplayName;
	
	spectrum.chart = new Highcharts.Chart({
		chart: {  
			renderTo: 'container'
		}, 
		title: {
			text: '@spectrum.realTimeChartTitle@'
		},
		xAxis: {
			min: chartAttr.xMin,
			max: chartAttr.xMax,
			zIndex:10
		},
		yAxis:{
			min:chartAttr.yMin,
			max:chartAttr.yMax,
			title:{
				text:''
			},
			tickInterval: 1,
			tickPositioner: function(min, max) {
	            var pos,
	                tickPositions = [],
	                tickStart = chartAttr.yMin;
	            for (pos = tickStart; pos <= chartAttr.yMax; pos += 10) {
	                tickPositions.push(pos);
	            }
	            return tickPositions;
	        }
		},
		plotOptions:{
			spline:{
				enableMouseTracking:false,
				lineWidth:1,
				marker: {
                    enabled: false
                },
                states:{
                	lineWidth:1
                }
			},
			arearange:{
				enableMouseTracking:true,
				events:{
					click:function(){
						spectrum.locateChannel(this.name.split('_')[1]);
					}
				},
				cursor:'pointer',
				fillOpacity:0.8
			}
		},
		tooltip:{
			formatter:function(){
				var lineName = this.point.series.name,
					series = this.series.data,
					minX = series[0].x,
					maxX = series[1].x,
					centerX = ((maxX + minX)/2).toFixed(2),
					xWidth = (maxX - minX).toFixed(2),
					tempStr = "@spectrum.channel@: {0}<br/>"+
						"@spectrum.channelCenter@: {1}<br/>"+
						"@spectrum.channelWidth@: {2}<br/>";
				return String.format(tempStr, lineName.split("_")[1], centerX, xWidth);
			}
		},
		legend:{
			enabled:true,
			labelFormatter: function() {
                return lineDisplayName[this.name];
            },
            width:400,
            itemWidth:100,
            itemHoverStyle: {
                color: '#FF0000'
            }
		},
		credits:{
			enabled:false
		},
		series: spectrum.seriesOptions
	}); 
}

/**
 * 更新浮标的数据
 */
spectrum.updateBuoyValueByPosition = function(buoyId, positionPx){
	var value = 0, 
		chartAttr = spectrum.chartAttribute;
	if(buoyId==='buoyLine_x'){	//主浮标
		//计算出此位置对应的X的值
		value = (positionPx - chartAttr.yAxisPaddingLeft) * chartAttr.xValuePerPx + chartAttr.xMin;
		chartAttr.mainBuoyLine_x = value;
	}else if(buoyId=="buoyLine_add"){	//增量浮标
		//计算出此位置对应的X的值
		value = (positionPx - chartAttr.yAxisPaddingLeft) * chartAttr.xValuePerPx + chartAttr.xMin;
		chartAttr.addBuoyLine_x = value;
	}else if(buoyId=="buoyLine_y"){	//水平浮标
		//计算出此位置对应的Y的值
		value = chartAttr.yMax - (positionPx - chartAttr.yAxisMaxTop) * chartAttr.yValuePerPx;
		chartAttr.horizontalBuoyLine_y = value;
	}
	spectrum.updateTopBuoyMessage(buoyId);
}

/**
 * 根据浮标的值调整浮标的位置
 * @param buoyId
 */
spectrum.adjustBuoyPositionByValue = function(buoyId){
	var chartAttr = spectrum.chartAttribute;
    if(buoyId==='buoyLine_x'){	//主浮标
    	var positionPx = (chartAttr.mainBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
    	$('#buoyLine_x').css({
        	left: positionPx
        });
    }else if(buoyId=="buoyLine_add"){	//增量浮标
    	var positionPx = (chartAttr.addBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
    	$('#buoyLine_add').css({
        	left: positionPx
        });
    }else if(buoyId=="buoyLine_y"){	//水平浮标
        var positionPx = (chartAttr.yMax - chartAttr.horizontalBuoyLine_y) / chartAttr.yValuePerPx + chartAttr.yAxisMaxTop;
        $('#buoyLine_y').css({
        	top: positionPx
        });
    }
}

/**
* 更新顶部浮标提示信息
* @namespace spectrum
* @method updateTopBuoyMessage
* @param {Number} buoyLineId 浮标的ID
*/
spectrum.updateTopBuoyMessage = function(buoyLineId){
	//初始化内部需要使用的变量，符合ES5标准
	var lineColors = spectrum.lineColors,
		chartAttr = spectrum.chartAttribute;
	
	if(buoyLineId=="buoyLine_x"){
		//此时移动的是主浮标,获取当前显示的主浮标类型
		var buoyId = $('input[name="line_buoy_radio"]:checked').attr("id"),
			lineName = "line_" + buoyId.split('_')[1];
		//改变主浮标类型方块颜色
		$('#buoyColorTip_x').css("background-color", lineColors[lineName]);
		$('#x_data').text(new Number(chartAttr.mainBuoyLine_x).toFixed(2) + "MHz");
		//计算出浮标当前位置的对应曲线的Y值
		chartAttr.mainBuoyLine_y = getYValue(lineName, chartAttr.mainBuoyLine_x);
		$('#y_data').text(chartAttr.mainBuoyLine_y + chartAttr.unitStr);
		//可能需要修改相应的增量浮标信息
		if($('#buoyLine_add').css('display')!='none'){
			$('#add_x_data').text(new Number(chartAttr.addBuoyLine_x - chartAttr.mainBuoyLine_x).toFixed(2) + "(" + new Number(chartAttr.addBuoyLine_x).toFixed(2) + ")MHz");
			$('#add_y_data').text(new Number(chartAttr.addBuoyLine_y - chartAttr.mainBuoyLine_y).toFixed(2) + "(" + new Number(chartAttr.addBuoyLine_y).toFixed(2) + ")"+chartAttr.unitStr);
		}
	}else if(buoyLineId=="buoyLine_add"){
		//此时移动的是增量浮标，获取当前显示的主浮标
		var buoyId = $('input[name="line_buoy_radio"]:checked').attr("id"),
			lineName = "line_" + buoyId.split('_')[1];
		chartAttr.addBuoyLine_y = getYValue(lineName, chartAttr.addBuoyLine_x);
		$('#add_x_data').text(new Number(chartAttr.addBuoyLine_x - chartAttr.mainBuoyLine_x).toFixed(2) + "(" + new Number(chartAttr.addBuoyLine_x).toFixed(2) + ")MHz");
		$('#add_y_data').text(new Number(chartAttr.addBuoyLine_y - chartAttr.mainBuoyLine_y).toFixed(2) + "(" + new Number(chartAttr.addBuoyLine_y).toFixed(2) + ")"+chartAttr.unitStr);
	}else if(buoyLineId=="buoyLine_y"){
		//此时移动的是水平浮标，需要修改水平浮标的顶部信息
		$('#yBuoy_x_data').text(new Number(chartAttr.horizontalBuoyLine_y).toFixed(2) + chartAttr.unitStr);
	}
	
	function getYValue(lineId, xValue){
		var yValue=null, 
			series = spectrum.chart.series,
			points = series[0].points;
		
	 	for(var i=0, seriesLength = series.length; i < seriesLength; i++){
	 		if(series[i].name==lineId){
	 			points = series[i].points;
	 		}
	 	}
	 	for(var j=0;j<points.length;j++){
	 		if(points[j].x>=xValue){
				if(points[j+1] && (points[j].x+points[j+1].x) / 2 < xValue ){
					yValue = points[j+1].y;
				}else{
					yValue = points[j].y;
				}
				break;
			}
	 	}
	 	return new Number(yValue).toFixed(2); 
	}
}

/**
* 由于chart位置或者大小发生变化，需要实时更新浮标位置
*
* @method updateBasicAttrAndBuoy
*/
spectrum.updateBasicAttrAndBuoy = function(){
	//初始化方法内部可能用到的变量
	var chart = spectrum.chart,
		chartAttr = spectrum.chartAttribute;
	//重新计算container应有的高度
	$('#container').height($('body').height()-$('#container').offset().top-$("#bottom_message_div").outerHeight(true) - $("#nm3kPlayer").outerHeight(true));
	//获取container此时的高度和高度
	var height = $('#container').height(), 
		width = $('#container').width();
	chart.setSize(width, height, false);
	//修改全局变量中的相关值
	chartAttr.xAxisLength = chart.plotSizeX; 
	chartAttr.yAxisMaxTop = $('#container').offset().top+chart.plotTop;
	chartAttr.yAxisLength = chart.plotSizeY;
	chartAttr.xValuePerPx = (chartAttr.xMax - chartAttr.xMin) / chartAttr.xAxisLength;
	chartAttr.yValuePerPx = 100 / chartAttr.yAxisLength;
	chartAttr.center_frequency_padding_left = (chartAttr.mainBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
	//此时需要改变浮标的位置
	if($('#buoyLine_x').css('display')!='none'){
		//改变主浮标的高度以及位置(top, left)
		var left = (chartAttr.mainBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
		$('#buoyLine_x').css({
			height: chartAttr.yAxisLength,
			top: chartAttr.yAxisMaxTop,
			left:left
		});
	}
	if($('#buoyLine_add').css('display')!='none'){
		//改变增量浮标的高度以及位置(top, left)
		var left = (chartAttr.addBuoyLine_x - chartAttr.xMin) / chartAttr.xValuePerPx + chartAttr.yAxisPaddingLeft;
		$('#buoyLine_add').css({
			height: chartAttr.yAxisLength,
			top: chartAttr.yAxisMaxTop,
			left:left
		});
	}
	if($('#buoyLine_y').css('display')!='none'){
		//改变水平浮标的长度及位置(top)
		var top = chartAttr.yAxisMaxTop + (chartAttr.yMax - chartAttr.horizontalBuoyLine_y) / chartAttr.yValuePerPx;
		$('#buoyLine_y').css({
			width: chartAttr.xAxisLength,
			top: top
		});
	}
	//重新定位OLT开关的位置
	if($('#oltSwitch').css('display')!='none'){
		$('#oltSwitch').css({
			top: $('#container').offset().top
		});
	}
}

/**
* 修改参考电平
*
* @method modifyReferencePower
* @param {Number} stepValue 在当前参考电平基础上，修改的值
*/
spectrum.modifyReferencePower = function(stepValue){
	//初始化方法内部可能用到的变量
	var chartAttr = spectrum.chartAttribute,
		seriesOptions = spectrum.seriesOptions,
		chart = spectrum.chart,
		lineColors = spectrum.lineColors,
		channel = spectrum.channel,
		intReg = /^-{0,1}\d+$/;
	
	var referencePower = 0, oldYMax = chartAttr.yMax;
	if(stepValue){
		//此时传入了修改值，表明是由toolbar中的按钮点击
		referencePower = chartAttr.yMax + stepValue;
	}else{
		//获取输入框中的值
		var reference_power_val = $('#reference_power').val();
		if(!intReg.test(reference_power_val)){
			spectrum.fadeObj('reference_power_tip');
			return false;
		}
		referencePower = parseInt($('#reference_power').val());
	}
	if(referencePower>220){
		referencePower = 220;
	}else if(referencePower<-210){
		referencePower = -210;
	}
	//修改输入框的值
	$('#reference_power').val(referencePower);
	$('#reference_power_tip').hide();
	//修改chartAttr的值
	chartAttr.yMax = referencePower;
	chartAttr.yMin = referencePower-100;
	//修改chart的值
	//修改seriesOptions中的值(反应信道的值)
	var i=0;
	for(i; i<seriesOptions.length; i++){
		if(seriesOptions[i].name.indexOf('channel')>-1){
			var channelId = chart.series[i].name.split('_')[1];
			seriesOptions[i].data = [[channel['channel_' + channelId].start, chartAttr.yMin, chartAttr.yMax], 
								      [channel['channel_' + channelId].end, chartAttr.yMin, chartAttr.yMax]];
		}
	}
	if(spectrum.isUnderIE9){
		//reCreatChart(seriesOptions);
		spectrum.reCreatChart();
	}else{
		spectrum.chart.yAxis[0].setExtremes(chartAttr.yMin,chartAttr.yMax,false,false);
		//修改信道的高度
		var index = 0;
		for(var i=0; i<chart.series.length; i++){
			if(index <=4 && chart.series[i].name.indexOf('channel')>-1){
				index++;
				var channelId = chart.series[i].name.split('_')[1];
				chart.series[i].setData([[channel['channel_' + channelId].start, chartAttr.yMin, chartAttr.yMax], [channel['channel_' + channelId].end, chartAttr.yMin, chartAttr.yMax]],false);
			}
		}
		spectrum.chart.redraw();
		//修改seriesOptions中的值
	}
	//修改水平浮标的位置(由值确定位置)
	if($('#buoyLine_y').css('display')!='none'){
		if(chartAttr.horizontalBuoyLine_y < chartAttr.yMin){
			//如果此时水平浮标的值小于最小值，则置其为最小值
			spectrum.chartAttribute.horizontalBuoyLine_y = chartAttr.yMin;
			spectrum.updateTopBuoyMessage('buoyLine_y');
		}else if(chartAttr.horizontalBuoyLine_y > chartAttr.yMax){
			//如果此时水平浮标的值大于最大值，则置其为最大值
			spectrum.chartAttribute.horizontalBuoyLine_y = chartAttr.yMax;
			spectrum.updateTopBuoyMessage('buoyLine_y');
		}
		spectrum.adjustBuoyPositionByValue('buoyLine_y');
	}
	//如果是最大值，则置灰增大参考电平按钮
	var toolbar = spectrum.toolbar;
	if(typeof toolbar !== 'undefined'){
		if(referencePower>=220){
			toolbar.getComponent("addReferPower").disable();
		}else{
			toolbar.getComponent("addReferPower").enable();
		}
		//如果是最小值，则置灰减小参考电平按钮
		if(referencePower<=-210){
			toolbar.getComponent("reduceReferPower").disable();
		}else{
			toolbar.getComponent("reduceReferPower").enable();
		}
	}
}

/**
* 修改中心频率
*
* @method modifyCenterFrequency
*/
spectrum.modifyCenterFrequency = function(){
	//修改中心频率时，起始频点不变，终止频率发生相应改变
	var center_frequency = $('#center_frequency').val(),
		chartAttr = spectrum.chartAttribute,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	//校验
	if(!positiveIntReg.test(center_frequency) && !positiveFloatReg.test(center_frequency)){
		$('#center_frequency_tip').text('@spectrum.positiveTip@');
		spectrum.fadeObj('center_frequency_tip');
		return;
	}
	//中心频率必须大于起始频率
	if(center_frequency <= chartAttr.xMin){
		$('#center_frequency_tip').text('@spectrum.centerFrequencyTip@').show();
		return false;
	}
	var cacu_end_frequency = parseFloat(2 * center_frequency - chartAttr.xMin);
	//展宽不能小于4M
	if((cacu_end_frequency - chartAttr.xMin) < 4){
		//给出提示说中心频率设置无效，会导致展宽小于4M
		$('#center_frequency_tip').text('@spectrum.centerFrequencyTip_2@').show();
		return false;
	}
	
	if(cacu_end_frequency > spectrum.chartAttribute.xMaxForReset){
		//给出提示说中心频率设置无效，会导致最大频率超过极限
		$('#center_frequency_tip').text('@spectrum.centerFrequencyTip_1@').show();
		return false;
	}
	$('#center_frequency_tip').hide();
	//更新X轴
	spectrum.updateChartXAxis(chartAttr.xMin, cacu_end_frequency);
}

/**
* 重置中心频率
*
* @method resetCenterFrequency
*/
spectrum.resetCenterFrequency = function(){
	var centerValue = (spectrum.chartAttribute.xMinForReset + spectrum.chartAttribute.xMaxForReset) / 2;
	$('#center_frequency').val(new Number(centerValue).toFixed(2));
	spectrum.modifyCenterFrequency();
}

/**
* 修改跨度
*
* @method modifyFrequencySpan
*/
spectrum.modifyFrequencySpan = function(){
	var chartAttr = spectrum.chartAttribute,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	//修改跨度时，中心频率不变，改变起始和结束频率
	var frequency_span = $('#frequency_span').val(),
		center_frequency = (chartAttr.xMax + chartAttr.xMin) /2;
	//校验
	if(!positiveIntReg.test(frequency_span) && !positiveFloatReg.test(frequency_span)){
		$('#frequency_span_tip').text('@spectrum.positiveTip@');
		spectrum.fadeObj('frequency_span_tip');
		return;
	}
	frequency_span = parseFloat(frequency_span);
	if(frequency_span<4 || frequency_span> (spectrum.chartAttribute.xMaxForReset - spectrum.chartAttribute.xMinForReset)){
		$('#frequency_span_tip').text('@spectrum.frequencySpanTip@').show();
		return
	}
	$('#frequency_span_tip').hide();
	var cacu_start = center_frequency - frequency_span / 2,
		cacu_end = center_frequency + frequency_span / 2;
	cacu_start = parseFloat(cacu_start.toFixed(2));
	cacu_end = parseFloat(cacu_end.toFixed(2));
	//防止频率最小值和最大值越界
	if(cacu_start < spectrum.chartAttribute.xMinForReset || cacu_end > spectrum.chartAttribute.xMaxForReset){
		$('#frequency_span_tip').text('@spectrum.frequencySpanTip_1@').show();
		return
	}
	//更新X轴
	spectrum.updateChartXAxis(cacu_start, cacu_end);
}

/**
* 重置频宽
*
* @method resetFrequencySpan
*/
spectrum.resetFrequencySpan = function(){
	$('#frequency_span').val(new Number(spectrum.chartAttribute.xMaxForReset - spectrum.chartAttribute.xMinForReset).toFixed(2));
	spectrum.modifyFrequencySpan();
}

/**
* 修改起始频率
*
* @method modifyStartFrequency
*/
spectrum.modifyStartFrequency = function(){
	var chartAttr = spectrum.chartAttribute,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	//修改起始频率时，终止频率保持不变
	var start_frequency = $('#start_frequency').val();
	//校验 
	if(!positiveIntReg.test(start_frequency) && !positiveFloatReg.test(start_frequency)){
		$('#start_frequency_tip').text('@spectrum.positiveTip@');
		spectrum.fadeObj('start_frequency_tip');
		return;
	}
	start_frequency = parseFloat(start_frequency);
	//开始频率不能小于0.5，必须小于当前终止频率
	if(start_frequency<spectrum.chartAttribute.xMinForReset || start_frequency >=chartAttr.xMax){
		$('#start_frequency_tip').text('@spectrum.startFrequencyTip@').show();
		spectrum.fadeObj('start_frequency_tip');
		return;
	}
	//展宽不能小于4M
	if((chartAttr.xMax - start_frequency) < 4){
		$('#start_frequency_tip').text('@spectrum.spanless4@').show();
		spectrum.fadeObj('start_frequency_tip');
		return;
	}
	$('#start_frequency_tip').hide();
	//更新X轴
	spectrum.updateChartXAxis(start_frequency, chartAttr.xMax);
}

/**
* 重置起始频率
*
* @method resetStartFrequency
*/
spectrum.resetStartFrequency = function resetStartFrequency(){
	$('#start_frequency').val(new Number(spectrum.chartAttribute.xMinForReset).toFixed(2));
	spectrum.modifyStartFrequency();
}

/**
* 修改终止频率
*
* @method modifyEndFrequency
*/
spectrum.modifyEndFrequency = function(){
	var chartAttr = spectrum.chartAttribute,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	//修改终止频率时，起始频率保持不变
	var end_frequency = $('#end_frequency').val();
	//TODO 校验
	if(!positiveIntReg.test(end_frequency) && !positiveFloatReg.test(end_frequency)){
		$('#end_frequency_tip').text('@spectrum.positiveTip@');
		spectrum.fadeObj('end_frequency_tip');
		return;
	}
	end_frequency = parseFloat(end_frequency);
	//结束频率不得大于最大值，必须大于当前开始频率
	if(end_frequency>spectrum.chartAttribute.xMaxForReset || end_frequency <= chartAttr.xMin){
		$('#end_frequency_tip').text('@spectrum.endFrequencyTip@').show();
		spectrum.fadeObj('end_frequency_tip');
		return;
	}
	//展宽不能小于4M
	if((end_frequency - chartAttr.xMin) < 4){
		$('#end_frequency_tip').text('@spectrum.spanless4@').show();
		spectrum.fadeObj('end_frequency_tip');
		return;
	}
	$('#end_frequency_tip').hide();
	//更新X轴
	spectrum.updateChartXAxis(chartAttr.xMin, end_frequency);
}

/**
* 重置终止频率
*
* @method resetEndFrequency
*/
spectrum.resetEndFrequency = function(){
	$('#end_frequency').val(new Number(spectrum.chartAttribute.xMaxForReset).toFixed(2));
	spectrum.modifyEndFrequency();
}

/**
 * 根据频宽值，修改展示信息及部分变量的值
 */
spectrum.renderChannelWidth = function(xMin, xMax) {
	if(typeof xMin != 'number'){xMin = parseFloat(xMin);}
	if(typeof xMax != 'number'){xMax = parseFloat(xMax);}
	
	var chartAttr = spectrum.chartAttribute;
	
	// 更新chartAttribute的值(X轴的最小值 / 最大值 / 单位像素值 / 中心频率左边距)
	spectrum.chartAttribute.xMin = xMin;
	spectrum.chartAttribute.xMax = xMax;
	spectrum.chartAttribute.mainBuoyLine_x = (xMin+xMax) / 2;
	spectrum.chartAttribute.addBuoyLine_x = (xMin+xMax) / 2;
	spectrum.chartAttribute.xValuePerPx = (chartAttr.xMax - chartAttr.xMin) / chartAttr.xAxisLength;
	
	// 左上角-展宽
	$('#frequency_span_top').text(new Number(xMax-xMin).toFixed(2));
	
	// 左下角-起始频宽
	$('#start_frequency_bottom').text(new Number(xMin).toFixed(2));
	// 中下部-中心频宽
	$('#center_frequency_bottom').text(new Number((xMin+xMax)/2).toFixed(2));
	// 右下角-终止频宽
	$('#end_frequency_bottom').text(new Number(xMax).toFixed(2));
	
	// 侧边栏-中心频率配置
	$('#center_frequency').val(new Number((xMin+xMax)/2).toFixed(2));
	// 侧边栏-展宽配置
	$('#frequency_span').val(new Number(xMax-xMin).toFixed(2));
	// 侧边栏-起始频率
	$('#start_frequency').val(new Number(xMin).toFixed(2));
	// 侧边栏-终止频率
	$('#end_frequency').val(new Number(xMax).toFixed(2));
	
	
}

/**
* 更新曲线图X轴范围
*
* @method modifyCenterFrequency
* @param {Number} xMin x轴最小值
* @param {Number} xMax x轴最大值
*/
spectrum.updateChartXAxis = function(xMin, xMax){
	var chartAttr = spectrum.chartAttribute,
		seriesOptions = spectrum.seriesOptions,
		chart = spectrum.chart;
	
	// add by fanzidong， 封装这些展示和值的设置
	spectrum.renderChannelWidth(xMin, xMax);
	
	//修改X轴的范围后，要设置seriesOptions的值
	var sampledData = spectrum.getSampledData(chartAttr.xMin, chartAttr.xMax, 500);
	//重置最大、最小、平均
	//获取当前曲线的显示状态
	var realEnable = $('#lineCbx_real').is(":checked"),
		maxEnable = $('#lineCbx_max').is(":checked"),
		minEnable = $('#lineCbx_min').is(":checked"),
		averageEnable = $('#lineCbx_average').is(":checked"),
		frames = spectrum.lineData.frames,
		line_real = sampledData.line_real,
		line_max = sampledData.line_max,
		line_min = sampledData.line_min,
		line_average = sampledData.line_average;
	//重置最大最小平均
	spectrum.lineData.line_max = [];
	spectrum.lineData.line_min = [];
	spectrum.lineData.line_average = [];
	spectrum.lineData.frames=0;
	for(var i = 0, length = line_real.length; i < length; i++){
		//如果开启最大保持，则需要判断是否需要更新
		if(maxEnable){
			line_max[i] = [line_real[i][0], line_real[i][1]];
		}
		//如果开启最小保持，则需要判断是否需要更新
		if(minEnable){
			line_min[i] = [line_real[i][0], line_real[i][1]];
		}
		//如果开启了平均值，则更新平均值
		if(averageEnable){
			line_average[i] = [line_real[i][0], line_real[i][1]];
		}
	}
	
	for(var j=0; j<seriesOptions.length; j++){
		switch(seriesOptions[j].name){
		case 'line_real':
			seriesOptions[j].data = sampledData.line_real;
			break;
		case 'line_max':
			seriesOptions[j].data = sampledData.line_max;
			break;
		case 'line_min':
			seriesOptions[j].data = sampledData.line_min;
			break;
		case 'line_average':
			seriesOptions[j].data = sampledData.line_average;
			break;
		}
	}
	//更新highchart属性
	if(spectrum.isUnderIE9){
		spectrum.reCreatChart();
	}else{
		chart.xAxis[0].setExtremes(xMin, xMax, true, false);
	}

	//每次更新X轴后，将浮标重置为中心位置(应EMS-8421bug单修改)
	var newLeft = chartAttr.xAxisLength/2 + chartAttr.yAxisPaddingLeft;
	if($('#buoyLine_x').css('display')!='none'){
		chartAttr.mainBuoyLine_x = (xMin + xMax)/2;
		spectrum.adjustBuoyPositionByValue('buoyLine_x');
		spectrum.updateTopBuoyMessage('buoyLine_x')
	}
	if($('#buoyLine_add').css('display')!='none'){
		chartAttr.addBuoyLine_x = (xMin + xMax)/2;
		spectrum.adjustBuoyPositionByValue('buoyLine_add');
		spectrum.updateTopBuoyMessage('buoyLine_add')
	}
}

/**
* 修改信道数据
*
* @method modifyChannelData
* @param {Number} channelId 信道ID
*/
spectrum.modifyChannelData = function(channelId){
	var chartAttr = spectrum.chartAttribute,
		channel = spectrum.channel,
		lineColors = spectrum.lineColors,
		positiveIntReg = /^\d+$/,
		positiveFloatReg = /^\d+\.\d+$/;
	
	if(typeof channelId !='number') {return false};
	//获取对应输入框的数据及原来数据
	var channel_center = parseFloat($('#channel_center_' + channelId).val()),
		channel_width = parseFloat($('#channel_width_' + channelId).val());
	if((!positiveIntReg.test(channel_center) && !positiveFloatReg.test(channel_center))
		|| (!positiveIntReg.test(channel_width) && !positiveFloatReg.test(channel_width))){
		$('#channel_tip').text('@spectrum.positiveTip@').show();
		spectrum.fadeObj('channel_tip');
		return;
	}
	channel_center = parseFloat(channel_center.toFixed(2));
	channel_width = parseFloat(channel_width.toFixed(2));
	var channel_start = channel_center - channel_width/2,
		channel_end = channel_center + channel_width/2,
		prev_channel_center = channel['channel_' + channelId].center,
		prev_channel_width = channel['channel_' + channelId].width;
	//确保计算出来的是两位小数
	channel_start = parseFloat(channel_start.toFixed(2));
	channel_end = parseFloat(channel_end.toFixed(2));
	prev_channel_center = parseFloat(prev_channel_center.toFixed(2));
	prev_channel_width = parseFloat(prev_channel_width.toFixed(2));
	//计算出来的channel_start不得小于最小值，channel_end不得大于最大值
	if(channel_start<5 || channel_end > 65){
		$('#channel_tip').text('@spectrum.channelTip@').show();
		spectrum.fadeObj('channel_tip');
		return;
	}
	//计算出来的信道不得覆盖其他信道的范围
	var cover = false, i=1;
	//与其他上行信道不能重叠
	for(i; i<=4; i++){
		if(i==channelId || !channel['channel_' + i]) continue;
		if(channel['channel_' + i].adminStatus == false) continue;
		//只有满足新信道最小值大于对比信道最大值或者新信道最大值小于对比信道最小值时，才不会覆盖
		if(!(channel_start>=channel['channel_' + i].end || channel_end<=channel['channel_' + i].start)){
			cover = true;
			break;
		}
	}
	if(cover){
		$('#channel_tip').text('@spectrum.channelTip_upConflict@').show();
		spectrum.fadeObj('channel_tip');
		return;
	}else{
		$('#channel_tip').hide();
	}
	//TODO 与下行信道不能重叠
	if(!cover){
		if(channel_end>=52){//进入识别区,上行信道频宽小于下行信道频宽
	        var i, len=downChannelListObject.length;
	        for(i = 0; i < len; i++){//由于downChannelListObject数据已存在排序ASC
	        	var curChl = downChannelListObject[i],
	        		curChlStart = (curChl.docsIfDownChannelFrequency - curChl.docsIfDownChannelWidth/2)/1000000,
	        		curChlEnd = (curChl.docsIfDownChannelFrequency + curChl.docsIfDownChannelWidth/2)/1000000;
	        	//只有满足新信道最小值大于对比信道最大值或者新信道最大值小于对比信道最小值时，才不会覆盖
	    		if(!(channel_start>=curChlEnd || channel_end<=curChlStart)){
	    			cover = true;
	    			break;
	    		}
	        }
	    }
	}
	if(cover){
		$('#channel_tip').text('@spectrum.channelTip_downConflict@').show();
		spectrum.fadeObj('channel_tip');
		return;
	}else{
		$('#channel_tip').hide();
	}
	//如果没有改变则无需修改
	if(channel_center!=prev_channel_center || channel_width!=prev_channel_width){
		//拼接出信道的name
		var channelName = "channel_" + channelId;
		//更新channel的值
		channel[channelName].center = channel_center;
		channel[channelName].width = channel_width;
		channel[channelName].start = channel_start;
		channel[channelName].end = channel_end;
		//改动options中的对应信道的值
		var i=0;
		for(i; i<spectrum.seriesOptions.length; i++){
			if(spectrum.seriesOptions[i].name == channelName){
				spectrum.seriesOptions[i].data = [[channel[channelName].start, chartAttr.yMin, chartAttr.yMax], 
				    						      [channel[channelName].end, chartAttr.yMin, chartAttr.yMax]];
			}
		}
		if(spectrum.isUnderIE9){
			spectrum.reCreatChart();
		}else{
			//重新绘制信息图形
			for(var i=0; i<spectrum.chart.series.length; i++){
				if(spectrum.chart.series[i].name==channelName){
					spectrum.chart.series[i].setData([[channel_start, chartAttr.yMin, chartAttr.yMax], [channel_end, chartAttr.yMin, chartAttr.yMax]],false);
				}
			}
			spectrum.chart.redraw();
		}
		//如果有操作设备权限，则此时使对应的保存按钮可用
		if(operationDevicePower){
			$('#channel_save_'+channelId).removeClass('disabledAlink');
		}
	}
}

/**
* 重置信道数据
*
* @method 	
*/
spectrum.resetChannelData = function(){
	var originalChannel = spectrum.originalChannel;
	//重置输入框的值
	// add by fanzidong,保存按钮置灰
	for(var i=0; i<4; i++){
		$('#channel_center_'+(i+1)).val(originalChannel['channel_'+(i+1)].center);
		$('#channel_width_'+(i+1)).val(originalChannel['channel_'+(i+1)].width);
		$('#channel_save_'+(i+1)).addClass('disabledAlink');
	}
	spectrum.channel = {};
	$.extend(true, spectrum.channel, originalChannel);
	var i=0,
		seriesOptions = spectrum.seriesOptions,
		series = spectrum.chart.series,
		lineColors = spectrum.lineColors,
		channel = spectrum.channel,
		chartAttr = spectrum.chartAttribute;
	for(i; i<seriesOptions.length; i++){
		if(seriesOptions[i].name.indexOf('channel')>-1){
			var channelId = series[i].name.split('_')[1];
			seriesOptions[i].data = [[channel['channel_' + channelId].start, chartAttr.yMin, chartAttr.yMax], 
								      [channel['channel_' + channelId].end, chartAttr.yMin, chartAttr.yMax]];
		}
	}
	spectrum.reCreatChart();
}

/**
* 保存对信道配置的修改
*
* @method saveChannelData
* @param {String} objId 对象id
*/
spectrum.saveChannelData = function(channelId){
	if($('#channel_save_'+channelId).hasClass('disabledAlink')){
		return;
	}
	//获取所修改信道的数据
	var channelName = "channel_"+channelId,
		upchannel = spectrum.channel[channelName],
		originalChannel = spectrum.originalChannel[channelName];
	//获取对应输入框的数据
	var channel_center = $('#channel_center_' + channelId).val(),
		channel_width = $('#channel_width_' + channelId).val();
	if(channel_center!=upchannel.center || channel_width!=upchannel.width){
		//如果此时输入框内的数据与保存的页面值不一致，则先将输入框的值进行输入操作
		spectrum.modifyChannelData(channelId);
	}
	upchannel.productType = productType;
	//在下发保存请求之前置灰此按钮，以防止多次触发，并加上等待提示
	$('#channel_save_'+channelId).addClass('disabledAlink');
	$('#refreshIcon_'+channelId).addClass('inline-block');
	//下发保存请求
	$.ajax({
    	url:'/cmcSpectrum/saveChannelData.tv',
	  	type:'POST',
	  	dateType:'json',
	  	data:{cmcId:cmcId, upChannelStr:JSON.stringify(upchannel)},
        success:function(response){
        	if(response.success){
        		//修改成功，刷新信道的相关值(originalChannel)
        		originalChannel.center = upchannel.center;
        		originalChannel.width = upchannel.width;
        		originalChannel.start = upchannel.start;
        		originalChannel.end = upchannel.end;
        		//给出提示，修改成功
        		top.afterSaveOrDelete({
        			title: '@spectrum.tip@',
        			html: '<b class="orangeTxt">@spectrum.channelModifySuccess@</b>'
        		});
        		//隐藏等待提示
        		$('#refreshIcon_'+channelId).removeClass('inline-block');
        	}else{
        		//修改失败，给予提示，但是不刷新信道的相关值
        		window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.channelModifyFailed@');
        		//使按钮继续可点，并且隐藏等待提示
        		$('#channel_save_'+channelId).removeClass('disabledAlink');
        		$('#refreshIcon_'+channelId).removeClass('inline-block');
        	}
        },
	  	error:function(){
	  	},
	  	cache:false
    });
}

/**
* 闪烁一个对象
*
* @method fadeObj
* @param {String} objId 对象id
*/
spectrum.fadeObj = function(objId){
	var $tip = $('#'+objId);
	$tip.fadeTo("fast",0,function(){
		$tip.fadeTo("normal",1);
	})
}

function isFlashInstalled(){
	var navigatorName = "Microsoft Internet Explorer"; 
	var isIE = (navigator.appName == navigatorName); 
	var swf;
	var flashVersion;
	try{
		if(isIE){
			swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
			if(swf){
				return true;
			}
		}else{
			if (navigator.plugins && navigator.plugins.length > 0){
				swf = navigator.plugins["Shockwave Flash"];
			}
			if(swf){
				return true;
			}
		}
	}catch(e){
		return false;
	}
	return false;
}

//add by jay 2016-11-09 添加发送heartbeat
var heartBeatInterval;

function checkForHeartbeat() {
    $.ajax({
        url: '/cmcSpectrum/heartbeat.tv',
        type: 'POST',
        dateType: 'text',
        data: {callbackId: spectrum.callbackId},
        cache: false,
        success: function (response) {
        },
        error: function () {
        }
    });
}

var soc;

/**
* 开启实时性能采集
*
* @method startCollect
*/
spectrum.startCollect = function(){
	if(typeof WebSocket == 'undefined' && !isFlashInstalled()){
		window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.installFlash@');
		return;
	}
	
	// 需要获取当前时间间隔
	$.ajax({
		url: '/cmcSpectrum/loadTimeInterval.tv',
		type: 'POST',
        dateType: 'text',
        async: false,
        cache: false,
        success: function (response) {
        	timeInterval = response.timeInterval;
        },
        error: function () {
        }
	});
	
	// 如果由于版本或者浏览器因素需要降级，告知用户
	if(needReduce()) {
		//降级为1s，弹窗告知用户
		var text = '';
		if(!supportSpectrumOptimize) {
			text = '@spectrum.versionNotSupport@';
		} else {
			text = '@spectrum.browserNotSupport@';
		}
		var reduceBox = new Nm3kMsg({
			title: '@COMMON.tip@',
			html: '<b class="orangeTxt">' + text + '</b>',
			timeLoading: true,
			autoHide: false,
			cancelBtn: true,
			id: "reduceBox"		
		});
		reduceBox.init();
	}
	
	var dwrId = "spectrum"+cmcId;
	var toolbar = spectrum.toolbar;
	var isOverTime = false;//频谱采集是否超时
	var isStart = false; //频谱是否正常开启
	toolbar.getComponent("startSpectrum").disable();

    //modify by jay 2016-11-09 调整调用顺序 启用heatbeat机制
    /*
     1、通过socket发送cmcId
     2、将socketResponce修改为MAINTAIN_SOCKET
     3、在SpectrumSocketService里面调用heartbeatService获取到callbackId回写到js
     4、在WebCallback里面保存SocketResponse，接收到数据就往回推送
     5、
     */
    var serverIp = location.hostname;
    var data = [];
    if(soc){
        return soc.reconnect();
    }
    
    soc = new TopvisionWebSocket("spectrumSocketService", {
    	params: {
    		cmcId: cmcId
    	},
    	onopen:function(){
            /*soc.sendRequest("spectrumSocketService",{
                //modify by jay 2016-11-09去掉了传递callbackId
                cmcId: cmcId
            })*/
            //开启实时录像开关
            spectrum.toolbar.getComponent("endSpectrum").enable();
            spectrum.toolbar.getComponent("realTimeVideo").enable();
            isStart = true;
        },
        onmessage:function(message){
        	json = Ext.decode(message);
        	// message的类型： json.callbackId，json.message
        	if(json.callbackId != undefined) {
        		// 表示这是connect时首先发送的callbackId，不做数据处理
        		isOverTime = false;
                window.callbackId = json.callbackId;
                if(json.callbackId != -1){
                    spectrum.callbackId = json.callbackId;
                    //能够开启，说明OLT开关已经打开
                    $('#oltSwitch_a').removeClass('offImg').addClass('onImg').attr('nm3ktip', '@spectrum.on@');
                    //add by jay 2016-11-09 添加发送heartbeat
                    if(!heartBeatInterval) {
                    	heartBeatInterval = setInterval(function(){
                    		checkForHeartbeat();
                    	},5000);
                    }
                }else{
                    var msg = '@spectrum.openRealCollectFailed@';
                    if(json.error){
                        switch(json.error){
                            case 'oltSwitchClosed':
                                msg = '@spectrum.oltSwitchClosed@';
                                $('#oltSwitch_a').removeClass('onImg').addClass('offImg').attr('nm3ktip', '@spectrum.off@');
                                break;
                            case 'cmtsSwitchClosed':
                                msg = '@spectrum.cmtsSwitchClosed@';
                                break;
                        }
                        window.parent.showMessageDlg('@spectrum.tip@', msg);
                    }else{
                        spectrum.stopCollect(function(){
                            window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.startCollectFailed@');
                        });
                    }
                    spectrum.toolbar.getComponent("startSpectrum").enable();
                    spectrum.toolbar.getComponent("endSpectrum").disable();
                }
        	} else if (json.message != undefined) {
        		switch(json.message){
	                case 'SPECTRUM_OVERTIME':
	                    isOverTime = true;
	                    spectrum.stopCollect(function(){
	                        //告诉用户已经超时
	                        window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.timeout@');
	                    });
	                    spectrum.toolbar.getComponent("startSpectrum").enable();
	                    break;
	                case 'SPECTRUM_TIMEOUT':
	                    spectrum.stopCollect(function(){
	                        //告诉用户已经超时
	                        window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.heartbeattimeout@');
	                    });
	                    spectrum.toolbar.getComponent("startSpectrum").enable();
	                    break;
	                case 'SPECTRUM_FRAME_START':
	                	top.frontEndLogSwitch && console.log(cmcId + '[' + json.cmcId + '][' + json.index + ']------SPECTRUM_FRAME_START')
	                    data = [];
	                    return;
	                case 'SPECTRUM_FRAME_DATA':
	                	top.frontEndLogSwitch && console.log(cmcId + '[' + json.cmcId + '][' + json.index + ']------SPECTRUM_FRAME_DATA: ' + json.data.length)
	                	// modify by fanzidong, 此处需要动态调整startFreq endFreq
	                	//console.log("startFreq: " + json.startFreq + ", endFreq: " + json.endFreq);
	                	spectrum.chartAttribute.xMin = json.startFreq / 1000000;
                		spectrum.chartAttribute.xMax = json.endFreq / 1000000;
	                    data = data.concat(json.data);
	                    return;
	                case 'SPECTRUM_FRAME_END':
	                	top.frontEndLogSwitch && console.log(cmcId + '[' + json.cmcId + '][' + json.index + ']------SPECTRUM_FRAME_END')
	                	break;
	            }
        		
        		 //排序整理
                data.sort(function(a, b) {
                	return a[0] - b[0];
                })
                
                top.frontEndLogSwitch && console.log(cmcId + '------' + data.length)

                try {
                	$('#refresh-time').html(top.moment().format('YYYY-MM-DD HH:mm:ss.SSS'));
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
                        spectrum.chart.redraw();
                    }
                    //此时需要刷新顶部信息栏的信息
                    if($('#buoyLine_x').css('display')!=="none"){
                        spectrum.updateTopBuoyMessage("buoyLine_x");
                    }
                    if($('#buoyLine_add').css('display')!=="none"){
                        spectrum.updateTopBuoyMessage("buoyLine_add");
                    }
                    var toolbar = spectrum.toolbar;
                    if(!toolbar.getComponent("startSpectrum").disabled){
                        toolbar.getComponent("startSpectrum").disable();
                        toolbar.getComponent("endSpectrum").enable();
                        toolbar.getComponent("realTimeVideo").enable();
                    }
                } catch (e) {
                	//console.log(e)
                }
        	}
        },
        onerror:function(){
            if(!isStart & !isOverTime){
                spectrum.stopCollect(function(){//当没有正常启动，并且不是由于超时造成了error时，提示用户
                    window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.connecterror@');
                });
                toolbar.getComponent("startSpectrum").enable();
            }
        },
        onclose:function(){
            if(!isStart & !isOverTime){ //当没有正常启动，并且不是由于超时造成了close时，提示用户
                spectrum.stopCollect(function(){
                    window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.connecterror@');
                });
                toolbar.getComponent("startSpectrum").enable();
            }
        }
    });
}

/**
* 停止实时性能采集
*
* @method stopCollect
*/
spectrum.stopCollect = function(callback){
    //add by jay 2016-11-09 停止heartBeat发送
    if (heartBeatInterval) {
        clearInterval(heartBeatInterval);
        heartBeatInterval = null;
    }
	//如果正在录像，录像停止
	if(continueInterval){
		spectrum.stopRealTimeVideo();
	}
	//spectrum.callbackId = -1;
	if(typeof callback === 'function'){
		callback();
	}
	
	//向服务端请求停止采集数据
    //modify by jay 2016-11-09 修改action名称
	$.ajax({
    	url:'/cmcSpectrum/stopCurSpectrum.tv',
	  	type:'POST',
	  	dateType:'json',
	  	data:{callbackId:spectrum.callbackId, cmcId: cmcId},
        success:function(response){
        	spectrum.callbackId = -1;
        	// add by fanzidong，访问按钮变化后又收到一次
        	soc.close();
        	//禁用实时录像开关
        	var toolbar = spectrum.toolbar;
        	toolbar.getComponent("startSpectrum").enable();
        	toolbar.getComponent("endSpectrum").disable();
        	toolbar.getComponent("realTimeVideo").disable();
        },
	  	error:function(){
	  	},
	  	cache:false
    });
}

/**
* 根据实时值，刷新最大/最小/平均曲线
*
* @method caculateLine
*/
spectrum.caculateLine = function(curLineData){
	//adjust y value base on spectrumUnit
	if(spectrumUnit && spectrumUnit==='dBmV'){
		for(var cl=0, len = curLineData.length; cl<len; cl++){
			curLineData[cl][1] = curLineData[cl][1] - 60;
		}
	}
	
	//给实时曲线赋值
	spectrum.lineData.line_real = curLineData;
	
	var	chartAttr = spectrum.chartAttribute,
		seriesOptions = spectrum.seriesOptions;
	
	//获取抽样后的值，需要根据此实时值更新最大、最小、平均
	var sampledData = spectrum.getSampledData(chartAttr.xMin, chartAttr.xMax, 500);
	//获取当前曲线的显示状态
	var realEnable = $('#lineCbx_real').is(":checked"),
		maxEnable = $('#lineCbx_max').is(":checked"),
		minEnable = $('#lineCbx_min').is(":checked"),
		averageEnable = $('#lineCbx_average').is(":checked"),
		frames = spectrum.lineData.frames,
		line_real = sampledData.line_real,
		line_max = sampledData.line_max,
		line_min = sampledData.line_min,
		line_average = sampledData.line_average;
	//遍历曲线中的每个点
	for(var i = 0, length = line_real.length; i < length; i++){
		//如果开启最大保持，则需要判断是否需要更新
		if(maxEnable){
			if(line_max[i]){
				if(line_real[i][1]>line_max[i][1]){
					line_max[i] = [line_real[i][0], line_real[i][1]];
				} else {
					line_max[i] = [line_real[i][0], line_max[i][1]];
				}
			}else{
				line_max[i] = [line_real[i][0], line_real[i][1]];
			}
		}
		//如果开启最小保持，则需要判断是否需要更新
		if(minEnable){
			if(line_min[i]){
				if(line_real[i][1]<line_min[i][1]){
					line_min[i] = [line_real[i][0], line_real[i][1]];
				} else {
					line_min[i] = [line_real[i][0], line_min[i][1]];
				}
			}else{
				line_min[i] = [line_real[i][0], line_real[i][1]];
			}
		}
		//如果开启了平均值，则更新平均值
		if(averageEnable){
			if(line_average[i]){
				line_average[i][0] = line_real[i][0];
				line_average[i][1] = (line_average[i][1] * frames + line_real[i][1]) / (frames+1);
			}else{
				line_average[i] = [line_real[i][0], line_real[i][1]];
			}
		}
	}
	if(averageEnable){
		spectrum.lineData.frames+=1;
	}
	
	//更新chart的options
	for(var j=0; j<seriesOptions.length; j++){
		switch(seriesOptions[j].name){
		case 'line_real':
			seriesOptions[j].data = sampledData.line_real;
			break;
		case 'line_max':
			seriesOptions[j].data = sampledData.line_max;
			break;
		case 'line_min':
			seriesOptions[j].data = sampledData.line_min;
			break;
		case 'line_average':
			seriesOptions[j].data = sampledData.line_average;
			break;
		}
	}
	
}

spectrum.getSampledData = function(minX, maxX, maxNum){
	//抽样后的数据结构
	var sampledDatas = {
			line_real: [],
			line_max: [],
			line_min: [],
			line_average: []
		}, 
		lineData = spectrum.lineData,
		frames = lineData.frames,
		currentData = lineData.line_real,
		maxData = lineData.line_max,
		minData = lineData.line_min,
		averageData = lineData.line_average;
	//获取当前曲线的显示状态
	var realEnable = $('#lineCbx_real').is(":checked"),
		maxEnable = $('#lineCbx_max').is(":checked"),
		minEnable = $('#lineCbx_min').is(":checked"),
		averageEnable = $('#lineCbx_average').is(":checked");
	//如果当前没有数据，则直接返回
	if(currentData.length===0){
        return sampledDatas;
    }
	//获取截取的开始和结束的index
    var splitStart = 0,
        splitEnd = 0;
    for(var i=0, il = currentData.length; i<il; i++) {
        if(parseFloat(currentData[i][0].toFixed(2))>=minX){
            splitStart = i;
            break;
        }
    }
    for(var i=il-1; i>0; i--) {
        if(parseFloat(currentData[i][0].toFixed(2))<=maxX){
            splitEnd = i;
            break;
        }
    }
    //计算出采样的间隔
    var intervalNum = Math.round((splitEnd-splitStart+1)/maxNum);
    if(intervalNum==0) {
        intervalNum=1;
    }
	//抽样出实时值，记录index
    var selectedIndex = 0;
    for(var i=splitStart; i<=splitEnd; i+=intervalNum){
        //取出i到i+intervalNum-1之间的最大值作为我们选中的点
        selectedIndex = i;
        for(var j=i+1; j<i+intervalNum; j++){
            if(j<splitEnd && currentData[j][1] > currentData[selectedIndex][1]){
                selectedIndex = j;
            }
        }
        sampledDatas.line_real.push([currentData[i][0], currentData[selectedIndex][1]]);
    }//循环完结时，已经获取到应该在完整数据中抽样的index数组
    //将取样的最后一个点数据加入
    sampledDatas.line_real.push([currentData[splitEnd][0], currentData[splitEnd][1]]);
    sampledDatas.line_max = maxData;
    sampledDatas.line_min = minData;
    sampledDatas.line_average = averageData;
	return sampledDatas;
}

/**
* 复位频谱曲线
*
* @method resetTotalSpectrumLine
*/
spectrum.resetTotalSpectrumLine = function(){
	if($('#lineCbx_max').is(":checked")){
		spectrum.resetSpectrumLine("line_max");
	}
	if($('#lineCbx_min').is(":checked")){
		spectrum.resetSpectrumLine("line_min");
	}
	if($('#lineCbx_average').is(":checked")){
		spectrum.resetSpectrumLine("line_average");
	}
}

spectrum.resetSpectrumLine = function(lineId){
	//复位就是删了再加
	spectrum.deleteSpecturmLine(lineId, true);
	spectrum.showSpecturmLine(lineId);
}

/**
* 打开录像面板
*
* @method openRealTimeVideo
*/
spectrum.openRealTimeVideo = function(){
	if($('#realTime_video').css('display')=="none"){
		$('#realTime_video').show();
		//重置输入框的值
		$('#video_startTime').text('');
		$('#video_continuedTime').text('');
		$('#video_endTime').text('');
	}
}

/**
* 关闭录像面板
*
* @method closeRealTimeVideo
*/
spectrum.closeRealTimeVideo = function(){
	if($('#realTime_video_button').hasClass('stop')){
		window.parent.showConfirmDlg('@spectrum.tip@', '@spectrum.makeSureStopVideo@', function(type) {
			if (type == 'no') {
		           return;
		    }
			spectrum.stopRealTimeVideo();
			$('#realTime_video').hide();
		})
	}else{
		$('#realTime_video').hide();
	}
}

/**
* 开启/停止录像采集
*
* @method startOrStopVideo
*/
spectrum.startOrStopVideo = function(){
	var start = $('#realTime_video_button').hasClass('start');
	if($('#realTime_video_button').attr("disabled")){
		return;
	}
	if(start){
		//表明触发的是开始录像操作,判断是否开启了实时采集
		if(spectrum.toolbar.getComponent("startSpectrum").disabled){
			spectrum.startRealTimeVideo();
		}else{
			window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.pleaseStartCollect@');
			/*top.afterSaveOrDelete({
    			title: '@spectrum.tip@',
    			html: '<b class="orangeTxt">@spectrum.pleaseStartCollect@</b>'
    		});*/
		}
	}else{
		//表明触发的是停止录像操作
		spectrum.stopRealTimeVideo();
	}
}

/**
* 开启录像采集
*
* @method startRealTimeVideo
*/
spectrum.startRealTimeVideo = function(){
	//先禁用按钮，防止连续点击
	$('#realTime_video_button').attr("disabled", true);
	$('#video_startTime').text('@spectrum.startRealTimeVideo@');
	$('#video_continuedTime').text('');
	$('#video_endTime').text('');
	//发送开启录像请求
	$.ajax({
		url: '/cmcSpectrum/realTimeVideoStart.tv',
		type:'post',
		data:{cmcId:cmcId},
		dataType:'json',
		error : function(){
			//开启录像失败，告知录像发生错误，意外中止
			$('#realTime_video_button').removeClass('stop').addClass('start').attr("disabled", false).attr('nm3kTip', "@realTimeVideo.startRecord@");
			$('#video_startTime').text('');
			window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.recordingStopped@');
			top.afterSaveOrDelete({
    			title: '@spectrum.tip@',
    			html: '<b class="orangeTxt">@spectrum.recordingStopped@</b>'
    		});
		},
		success : function(response){
			if(!response.isStarted){
				//开启录像失败，告知录像发生错误，意外中止
				$('#realTime_video_button').removeClass('stop').addClass('start').attr("disabled", false).attr('nm3kTip', "@realTimeVideo.startRecord@");
				$('#video_startTime').text('');
				window.parent.showMessageDlg('@spectrum.tip@', '@spectrum.recordingStopped@');
				top.afterSaveOrDelete({
	    			title: '@spectrum.tip@',
	    			html: '<b class="orangeTxt">@spectrum.recordingStopped@</b>'
	    		});
			}else{
				//开始时间输出,1s时间变化逻辑
				var videoStartTime = new Date(), continueSeconds = 0;
				videoStartTime.setTime(response.videoStartTime);
				continueInterval = setInterval(function(){
					continueSeconds++;
					$('#video_continuedTime').text(spectrum.formatContinueTime(continueSeconds));
				},1000);
				spectrum.videoStartTime = videoStartTime;
				$('#realTime_video_button').removeClass('start').addClass('stop').attr('nm3kTip', "@spectrum.stopRecording@");
				$('#realTime_video_button').attr('nm3kTip', "@spectrum.atLeast5@");
				setTimeout(function(){
					$('#realTime_video_button').attr('nm3kTip', "@spectrum.stopRecording@");
					$('#realTime_video_button').attr("disabled", false);
				},5000)
				$('#video_startTime').text(spectrum.formatDate(videoStartTime));
				$('#video_continuedTime').text("00:00:00");
				$('#video_endTime').text('');
				spectrum.videoCallbackId = response.callbackId;
			}
		},
		cache : false
	});
}

spectrum.stopRealTimeVideo = function(){
	//发送停止录像请求
	$.ajax({
		url: '/cmcSpectrum/realTimeVideoStop.tv',
		type:'post',
		data:{callbackId:spectrum.videoCallbackId, cmcId: cmcId},
		dataType:'json',
		success:function(response){
			var result = response.success;
			if(!result){
				$('#realTime_video_button').removeClass('stop').addClass('start').attr('nm3kTip', "@realTimeVideo.startRecord@");
				clearInterval(continueInterval);
				//重置输入框的值
				$('#video_startTime').text('');
				$('#video_continuedTime').text('');
				$('#video_endTime').text('');
			}else{
				var videoId = response.videoId,
					videoName = response.videoName;
				
				$('#realTime_video_button').removeClass('stop').addClass('start').attr('nm3kTip', "@realTimeVideo.startRecord@");
				clearInterval(continueInterval);
				continueInterval = null;
				
				//获取终止时间
				var endDate = new Date();
				endDate.setTime(parseInt(response.endTime));
				var startTime = spectrum.videoStartTime,
					_continueSeconds = parseInt(endDate.getTime()/1000) - parseInt(startTime.getTime()/1000);
				$('#video_endTime').text(spectrum.formatDate(endDate));
				$('#video_continuedTime').text(spectrum.formatContinueTime(_continueSeconds));
				//弹窗告知用户已经完成录像
				var msgBox = new Nm3kMsg({
					title: '@spectrum.recordSuccess@',
					html: '<p><b>@spectrum.videoDefaultName@</b><b class="blueTxt">'+ videoName +'</b></p><b class="orangeTxt">@spectrum.recordSuccessContent@</b>',
					okBtnTxt:'@spectrum.play@',
					okBtn : true,
					okBtnCallBack:function(){
						playVideo(videoId);
					},
					cancelBtn :true,
					cancelBtnTxt:'@COMMON.rename@',
					cancelBtnCallBack:function(){
						reNameVideo(videoId, videoName);
					},
					timeLoading: true,
					autoHide : false,
					id: "nm3kSaveOrDelete"		
				});
				msgBox.init();
			}
		},
		error:function(){
		},
		cache:false
	});
	
	/**
	 * 播放指定录像
	 */
	function playVideo(videoId){
		window.top.addView("videoPlayer", "@spectrum.videoPlay@", "apListIcon", "/cmcSpectrum/playVideo.tv?videoId="+videoId, "", true);
	}
	
	/**
	 * 重命名指定录像
	 */
	function reNameVideo(videoId, videoName){
		window.top.createDialog('renameVideo', '@spectrum.renameVideo@', 600, 250,
	            '/cmcSpectrum/showRenameVideo.tv?videoId=' + videoId+'&videoName='+videoName , null, true, true);
	}
}

spectrum.formatContinueTime = function(continueSeconds){
	if(continueSeconds<60){
		return "00:00:"+ checkNum(continueSeconds);
	}else{
		return "00:"+checkNum(parseInt(continueSeconds/60))+":"+checkNum(continueSeconds%60);
	}
	function checkNum(num){
		return (num<10) ? "0" + num : num;
	}
}

spectrum.formatDate = function(date){
	if(date instanceof Date){
		return String.format("{0}-{1}-{2} {3}:{4}:{5}", date.getFullYear(), checkNum(date.getMonth()+1), 
			checkNum(date.getDate()), checkNum(date.getHours()), checkNum(date.getMinutes()), 
			checkNum(date.getSeconds()));
	}
	function checkNum(num){
		return (num<10) ? "0" + num : num;
	}
}

/**
* 拖拽移动对象
*
* @method moveObj
* @param {Object} obj 对象
* @param {Boolean} moveX 是否在X轴上移动
* @param {Boolean} moveY 是否在Y轴上移动
*/
spectrum.moveObj = function(obj, moveX, moveY){
	obj.style.cursor = moveX && moveY ?  "move" : "pointer";
	obj.onmousedown = function(e) {
		//此处写法不太好，考虑更通用的做法
		obj = (obj.id == "realTime_video_title") ? document.getElementById("realTime_video") : obj;
		obj.style.position = "absolute";
		var left = obj.offsetLeft,
			top = obj.offsetTop,
			drag_x = document.all ? event.clientX : e.pageX,
			drag_y = document.all ? event.clientY : e.pageY,
		    selection = disableSelection(document.body),
		    minX = 0,
		    minY = 0,
		    maxX = $('body').width() - $(obj).width(),
		    maxY =  $('body').height() -$(obj).height(),
		    chartAttribute = spectrum.chartAttribute;
		//如果是浮标的话，其范围限制必须在图表内部
		if(obj.id === 'buoyLine_x' || obj.id === 'buoyLine_add' || obj.id === 'buoyLine_y'){
			minX = chartAttribute.yAxisPaddingLeft;
			minY = chartAttribute.yAxisMaxTop;
			maxX = minX + chartAttribute.xAxisLength;
			maxY = minY + chartAttribute.yAxisLength;
		}
		var move = function(e) {
			if(moveX){
				var _left = left + (document.all ? event.clientX : e.pageX) - drag_x;
				//不能超出边界
				if(_left<minX){
					_left=minX;
				}else if(_left>maxX){
					_left = maxX;
				}
				obj.style.left = _left + "px";
			}
			if(moveY){
				var _top = top + (document.all ? event.clientY : e.pageY) - drag_y;
				//不能越界
				if(_top<minY){
					_top=minY;
				}else if(_top>maxY){
					_top = maxY;
				}
				obj.style.top = _top + "px";
			}
			var value = 0;
			if(obj.id === 'buoyLine_x' || obj.id === 'buoyLine_add'){
				value = _left;
			}else if(obj.id === 'buoyLine_y'){
				value = _top;
			}
			spectrum.updateBuoyValueByPosition(obj.id, value);
    	};
    	var stop = function(e) {
    		if(obj.id === 'buoyLine_x' || obj.id === 'buoyLine_add'){
    			//如果移动的是主浮标或者增量浮标，则需要吸附最近位置放置
    			spectrum.atachBuoy(obj.id);
    		}
    		removeEvent(document, "mousemove", move);
    		removeEvent(document, "mouseup", stop);
    		if(selection) {
    			selection();
    			delete selection;
    		}
    	};
    	addEvent(document, "mousemove", move);
    	addEvent(document, "mouseup", stop);
    	selection();
	}
	
	function addEvent(target, type, listener, capture) { 
		return eventHandler(target, type, listener, true, capture); 
	};

	function removeEvent(target, type, listener, capture) { 
		return eventHandler(target, type, listener, false, capture); 
	};

	function eventHandler(target, type, listener, add, capture) {
	  type = type.substring(0, 2) === "on" ? type.substring(2) : type;
	  if(document.all) {
	    add ? target.attachEvent("on"+type, listener) : target.detachEvent("on"+type, listener);
	  } else {
	    capture = (typeof capture === "undefined" ? true : (capture === "false" ? false : ((capture === "true") ? true : (capture ? true : false))));
	    add ? target.addEventListener(type, listener, capture) : target.removeEventListener(type, listener, capture);
	  }
	};

	function disableSelection(target) {
		var disable = true;
		var oCursor = document.all ? target.currentStyle["cursor"] : window.getComputedStyle(target, null).getPropertyValue("cursor");
		var returnFalse = function(e) {
			e.stopPropagation();
			e.preventDefault();
			return false;
		};
		var returnFalseIE = function() { return false; };
		var selection = function() {
			if(document.all) {
				disable ? addEvent(target, "selectstart", returnFalseIE) : removeEvent(target, "selectstart", returnFalseIE);
			} else {
				disable ? addEvent(target, "mousedown", returnFalse, false) : removeEvent(target, "mousedown", returnFalse, false);
			}
			target.style.cursor = disable ? "default" : oCursor;
			disable = !disable;
		};
		return selection;
	};
}

spectrum.atachBuoy = function(buoyId){
	//获取此时浮标对应的频谱曲线类型
	var buoyRadioId = $('input[name="line_buoy_radio"]:checked').attr("id"),
		lineId = "line_" + buoyRadioId.split('_')[1];
	//找到图表中对应的series,并找到最近的X值
	var series = spectrum.chart.series,
		points = null,
		nearestX = 0,
		xValue = 0;
	if(buoyId==='buoyLine_x'){
		xValue = spectrum.chartAttribute.mainBuoyLine_x;
	}else if(buoyId === 'buoyLine_add'){
		xValue = spectrum.chartAttribute.addBuoyLine_x;
	}
	for(var i=0, seriesLength = series.length; i < seriesLength; i++){
		if(series[i].name==lineId){
			points = series[i].points;
		}
	}
	if(points === null || points.length===0){
		return
	}
	for(var j=0;j<points.length;j++){
		if(j===points.length-1){
			nearestX = points[j].x;
			break;
		}
		if(points[j].x>=xValue){
			if(points[j+1] && (points[j].x+points[j+1].x) / 2 < xValue ){
				nearestX = points[j+1].x;
			}else{
				nearestX = points[j].x;
			}
			break;
		}
	}
	//设置对应的值，并改变位置和显示
	if(buoyId==='buoyLine_x'){
		spectrum.chartAttribute.mainBuoyLine_x = nearestX;
	}else if(buoyId === 'buoyLine_add'){
		spectrum.chartAttribute.addBuoyLine_x = nearestX;
	}
	spectrum.adjustBuoyPositionByValue(buoyId);
	spectrum.updateTopBuoyMessage(buoyId);
}