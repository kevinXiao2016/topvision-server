/**
 * 图表的一些必备属性封装
 * @param title
 * @param yText
 * @param unit
 * @param perfTargetName
 * @param direction
 * @param url
 * @param indexs
 * @returns
 */
var generateChart = {};

function graphData(title, yText, unit, perfTargetName, direction, url, indexs){
	this.title = title;
	this.yText = yText;
	this.unit = unit;
	this.perfTargetName = perfTargetName;
	this.direction = direction;
	this.url = url;
	this.indexs = indexs;
};
var FLOW_K10 = 1000,
	FLOW_M10 = FLOW_K10 * 1000,
	TOOLTIP_TPL = '<span class="tooltipSpan" style="color:{0}">{1}:</span><span class="tooltipSpan" style="color:{0}">{2}{3}</span><br/>';

/**
 * highstock数据图表
 * 
 * @param {object} chart 图表的相关参数配置
 * @param {object} credits 图表版权信息参数配置
 * @param {object} lang 图表语言参数配置
 * @param {object} exporting 导出配置
 * @param {object} title 标题配置
 * @param {object} xAxis X轴配置
 * @param {object} yAxis Y轴配置
 * @param {array} series 数据源配置
 */
var generateGraph = function(seriesOptions, title, yText, unit, perfTargetName, startTime, endTime){
	//,
      //  transformStringToDate(endTime)
	
	/**
     * 
     * 为了让navigator能够与x轴一直，需要加两个空的点来标识开始结束时间,需要判断是否存在，免得开始和结束有两个点
     */
    //考虑到查询到没有数据的情况,加上相应判断
    if(seriesOptions  && seriesOptions[0] && seriesOptions[0].data.length>0){
        if(seriesOptions[0].data[0][0]>Date.parse(startTime.replace('-','/'))){
            seriesOptions[0].data = [[Date.parse(startTime.replace('-','/')),null]].concat(seriesOptions[0].data);
        }
        if(seriesOptions[0].data[seriesOptions[0].data.length-1][0]<Date.parse(endTime.replace('-','/'))){
            seriesOptions[0].data = seriesOptions[0].data.concat([[Date.parse(endTime.replace('-','/')),null]]);
        }
    } else {
        seriesOptions[0].data[0] = [Date.parse(startTime.replace('-','/')),null];
        seriesOptions[0].data[1] = [Date.parse(endTime.replace('-','/')),null];
        //seriesOptions[0].data[2] = [Date.parse(endTime.replace('-','/')),null];
    }
    
	var w = $("#container").width(),
		h = $("#container").height();
	
	//重写rangeselector的定位方法，我们想要定位到右上角
	var orgHighchartsRangeSelectorPrototypeRender = Highcharts.RangeSelector.prototype.render;
	Highcharts.RangeSelector.prototype.render = function (min, max) {
	    orgHighchartsRangeSelectorPrototypeRender.apply(this, [min, max]);
	    var leftPosition = this.chart.plotLeft + w - 100,
	        topPosition = this.chart.plotTop-25,
	        space = 2;
	    this.zoomText.attr({
	        x: leftPosition,
	        y: topPosition
	    });
	    leftPosition += this.zoomText.getBBox().width;
	    for (var i = 0; i < this.buttons.length; i++) {
	        this.buttons[i].attr({
	            x: leftPosition,
	            y: topPosition 
	        });
	        leftPosition += this.buttons[i].width + space;
	    }
	};
	
	//首先清空highstock所占用的内存
	if(chart!=null){
		chart.destroy();
		chart = null;
		$("#container").empty();
	}
	
	Highcharts.setOptions({
		global: {
			useUTC: false
		},
		lang:{
			contextButtonTitle: (lang='zh_CN')?'@report/graph.contextMenu@':'Chart context menu',
			printChart: (lang='zh_CN')?'@report/report.printGraph@':'Print chart',
			downloadJPEG: (lang='zh_CN')?'@report/graph.downloadJpeg@':'Download JPEG image',
			downloadPNG: (lang='zh_CN')?'@report/graph.downloadPng@':'Download PNG image',
			downloadPDF: (lang='zh_CN')?'@report/graph.downloadPdf@':'Download PDF document',
			downloadSVG: (lang='zh_CN')?'@report/graph.downloadSvg@':'Download SVG vector image'
		}
	});
	
	chart = new Highcharts.StockChart({
	    chart: {
	        renderTo: 'container',
	        type: 'spline',
	        zoomType: 'x'
	    },
	    credits:{enabled: false},
	    navigator : {
			adaptToUpdatedData: false,
			xAxis: {
			    ordinal: false,
				dateTimeLabelFormats: {
					millisecond: '%H:%M:%S.%L',
					second: '%H:%M:%S',
					minute: '%H:%M',
					hour: '%H:%M',
					day: '%m-%d',
					week: '%m-%d',
					month: '%Y-%m',
					year: '%Y'
				}
			}
		},
		scrollbar: {
			liveRedraw: false
		},
		title: {text: title},
		subtitle: {text: '@performance/Tip.trendSubTitle@'},
		exporting:{  
			sourceWidth: $('#container').width(),
            sourceHeight: 500,
            filename : "image"
        },
	    rangeSelector:{
	    	buttonTheme: { // styles for the buttons
                r: 1,
                padding: 3,
                fill: 'none',
                style: {
                    color: '#039',
    				cursor : 'pointer'
                },
	    		states: {
	    			hover: {
	    				fill: '#039',
	    				style: {
	    					color: 'white'
	    				}
	    			},
	    			select: {
	    				fill: 'none',
	    				style: {
	    					color: '#039'
	    				}
	    			}
	    		}
            },
	    	buttons:[
	            {type: 'all', count: 1, text: '@Tip.all@'}
	        ],
	        selected: 3,
	    	labelStyle: {
	    		display: 'none',
	    		color: '#C0C0C0',
	    		fontWeight: 'bold'
	    	},
	    	inputEnabled: false
	    },
	    xAxis : {
			events : {
				afterSetExtremes : afterSetExtremes
			},
			type: 'datetime',
			ordinal: false,
			dateTimeLabelFormats: {
				millisecond: '%H:%M:%S.%L',
				second: '%H:%M:%S',
				minute: '%H:%M',
				hour: '%H:%M',
				day: '%m-%d',
				week: '%m-%d',
				month: '%Y-%m',
				year: '%Y'
			},
			minRange: 60 * 1000 // one m,
		},
	    yAxis: {
	    	labels: {
	    		formatter: function() {
	    			if( perfTargetName !="olt_fanSpeed" && (perfTargetName.contains("Flow") || perfTargetName.contains("Speed"))){
	    				return this.value / FLOW_M10 + unit;
	    			}else if( perfTargetName =="olt_fanSpeed" ){
	    				return this.value*60 + unit;
	    			}
	    			return this.value + unit;
	    		}
	    	},
	    	
	    	plotLines : (function(){
	    		if(!["cmc_onlineStatus","olt_onlineStatus"].contains(perfTargetName)){
	    			return null;
	    		}
	    		return [
	  		    	  {
	  		    		  value : 0,
	  		    		  color : "red",
	  		    		  width : 3
	  		    	  }
	  		    	]
	  	    	})(),
	    	min: formatMin(perfTargetName),
	    	max: formatMax(perfTargetName,seriesOptions),
	    	allowDecimals:false,
	        title: {text: yText}
	    },
		
	   legend: {
	    	enabled: true,
	    	floating: true,
	    	align: 'left',
	    	width: getLegendWidth(seriesOptions.length),
	    	maxHeight: 55,
	    	itemWidth:150,
	    	x:27,
        	backgroundColor: '#FCFFC5',
        	borderColor: '#C0C0C0',
        	borderWidth: 1,
	    	layout: 'horizontal',
	    	verticalAlign: 'top',
	    	shadow: true
	    },
	    tooltip: {
           formatter: function() {
				var s = '<b>'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b><br/>';
				$.each(this.points, function(i, point) {
					var seriesIndex = point.series.index;
					var $series = point.series;
					if(point.y == -1){
						if(["cmc_onlineStatus","olt_onlineStatus"].contains(perfTargetName)){
							s +=  point.series.name + '@Tip.failedConnect@<br/>';
						}else{
							s +=  point.series.name + '@Tip.failedGetData@<br/>';
						}
					}else{
						if(["optTxPower","optRePower","onuCATVRePower"].contains(perfTargetName)){
							if(point.y == 0){
								s +=  point.series.name + '@Tip.failedGetData@<br/>';
							}else{
								s +=  String.format(TOOLTIP_TPL,$series.color,$series.name,point.y,unit);
							}
						}else if(["ccer","ucer"].contains(perfTargetName)){
							//var dataIndex = point.series.xData.indexOf(point.x);
							var $tip;
							$.each(seriesOptions[seriesIndex].data,function(i,$point){
								if($point[0]==point.x){
									$tip = $point[2];
									return false;
								}
							});
							//var $tip = seriesOptions[seriesIndex].data[dataIndex+1][2];
							if(perfTargetName == 'ccer'){
								s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
								  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.y + unit + '('+'@Performance.ccerNum@'+  $tip +')' + '</span><br/>';
							}else if(perfTargetName == 'ucer'){
								s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
								  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.y + unit +'('+'@Performance.ucerNum@'+  $tip +')'+  '</span><br/>';
							}
						}else if((perfTargetName.contains("Flow") || perfTargetName.contains("Speed")) && perfTargetName!="olt_fanSpeed"){/**单独处理速率的问题**/
							var $y = point.y ;
							var $unit;
							if(FLOW_K10 > $y){
								$y = $y.toFixed(2) ;
								$unit = "bps"
							}else if(FLOW_M10 > $y ){
								$y = ($y / FLOW_K10) .toFixed(2) ;
								$unit = "Kbps"
							}else{
								$y = ($y / FLOW_M10 ).toFixed(2);
								$unit = "Mbps"
							}
							s +=  String.format(TOOLTIP_TPL,$series.color, $series.name, $y, $unit);
						} else if(["olt_fanSpeed"].contains(perfTargetName)){
							s +=  String.format(TOOLTIP_TPL,$series.color,$series.name,point.y*60,unit);
						} else if(["cmc_dorLinePower"].contains(perfTargetName)){
							if(point.y == 0){
								s +=  point.series.name + '@tip.notSupport@';
							}else{
								s +=  String.format(TOOLTIP_TPL,$series.color,$series.name,point.y,unit);
							}
						} else {
							s +=  String.format(TOOLTIP_TPL,$series.color,$series.name,point.y,unit);
						}
					}
				});
           
				return s;
			}
	    },
	    plotOptions : {
	    	series:{
	    		lineWidth : 1,
	    		event : {
	    			mouseOver : function(e){
	    				//alert(1)
	    			}
	    		}
	    	}
	    },
	    series: seriesOptions
	});
	/*chart.xAxis[0].setExtremes(
		Date.parse(startTime.replace('-','/')),
		Date.parse(endTime.replace('-','/'))
    );*/
}

/**
 * 判断Y轴是否需要设置0为最小值
 */
var formatMin = function(perfTargetName){
	if(perfTargetName == 'cmc_onlineStatus' || perfTargetName == 'olt_onlineStatus'){
		return -1;
	}else if(perfTargetName == 'cmc_opticalReceiver' || perfTargetName=='optTxPower' || perfTargetName=='optRePower' 
		|| perfTargetName=='ponRePower' || perfTargetName == 'oltPonRePower' || perfTargetName == 'onuPonRePower'||perfTargetName=='onuCATVRePower'
		|| perfTargetName=='cmc_cmReAvg'|| perfTargetName=='cmc_cmTxAvg'){
		return null;
	}else{
		return 0;
	}
}

/**
 * 判断Y轴是否需要设置100为最大值
 * AshringerYY修改 2013-10-12
 * 当所有值都为0时，设置最大值为1，令纵坐标可以显示
 */
var formatMax = function(perfTargetName,seriesOptions){
	if(perfTargetName == 'ccUserNum'){
		return null;
	}
	for(var i = 0 ; i<seriesOptions.length ; i++){
		var dataxy = seriesOptions[i].data;
		for(var j  = 0 ; j<dataxy.length; j ++){
			if(dataxy[j][1] > 1){
				return null;
			}
		}
	}
	return 1;
	/*if($.inArray(perfTargetName, ["macUsed", "channelUsed", "cpuUsed", "memUsed", "flashUsed"])!=-1){
		return 100;
	}else{
		return null;
	}*/
}

/**
 * Load new data depending on the selected min and max
 */
function afterSetExtremes(e) {
	//获取截取的时间段
	var startTime = new Date();
	startTime.setTime(Math.round(e.min));
	var endTime = new Date();
	endTime.setTime(Math.round(e.max));
	//chart.showLoading('@Tip.loading@');
	var channelIndex = 0;
	if((typeof cmcIndex) == 'number'){
		channelIndex = cmcIndex;
	}
	//支持cc用户数历史查询页面 
	if(graphData.perfTargetName == 'ccUserNum'){
		$.getJSON(graphData.url + '?entityId=' + entityId + '&ponId=' + parseInt($('#select_pon').val()) + '&cmcId=' + parseInt($('#select_cmts').val())
				+ '&channelIndex=' + parseInt($('#select_Cnl').val())
				+ '&quotas=' + graphData.indexs  + '&startTime=' + formatDate(startTime) + '&endTime=' + formatDate(endTime)
				, function(json) {
			chart.hideLoading();
			//给每个线重新赋值
			var series = chart.series;
			$.each(series,function(seriesIdx , serie){
				if(serie.name!="Navigator"){
					//根据返回的是一条数据还是多条数据，进行不同的处理
					var key = serie.options.key;
					serie.setData(json[key], false);
				}
			});
			chart.redraw();
		});
	}else{
		$.getJSON(graphData.url + '?entityId=' + entityId + '&indexs=' + graphData.indexs + '&channelIndex=' + channelIndex + '&perfTarget=' + graphData.perfTargetName 
				+ '&direction=' + graphData.direction  + '&startTime=' + formatDate(startTime) + '&endTime=' + formatDate(endTime), function(json) {
			chart.hideLoading();
			//给每个线重新赋值
			var series = chart.series;
			$.each(series,function(seriesIdx , serie){
				if(serie.name!="Navigator"){
					//根据返回的是一条数据还是多条数据，进行不同的处理
					var key = serie.userOptions.key;
					serie.setData(json[key], false);
				}
			});
			chart.redraw();
		});
	}
	
	function formatDate(time){
		if(typeof time == 'string') time = parseDate(time);   
		if(time instanceof Date){   
			var y = time.getFullYear();  
			var m = time.getMonth() + 1; 
			if(m < 10){ m = "0" + m;}
			var d = time.getDate(); 
			if(d < 10){ d = "0" + d;}
			var h = (time.getHours()<10) ? ('0' + time.getHours()) : time.getHours();  
			var i = (time.getMinutes()<10) ? ('0' + time.getMinutes()) : time.getMinutes();   
			var s = (time.getSeconds()<10) ? ('0' + time.getSeconds()) : time.getSeconds();   

			if(h>0 || i>0 || s>0) return y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;  

			return y + '-' + m + '-' + d;   
		}
		return '';
	}
	
}

function getLegendWidth(n){
	if(n==1){
		return 150;
	}else{
		return 300;
	}
}
