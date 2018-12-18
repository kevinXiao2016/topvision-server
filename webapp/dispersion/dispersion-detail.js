var COLORS = {
	SNR_AVG: '#2ECC71', //接收SNR平均值
	SNR_SDV: '#2980B9', //接收SNR标准差
	PWR_AVG: '#E67E22', //上行发送电平平均值
	PWR_SDV: '#d14233'  //上行发送电平标准差
};
var timelines, curPointIndex=0;

Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

function loadDispersionTrend(){
	var startTime = new Date(),
		endTime= new Date();
	startTime.setMonth(startTime.getMonth()-1);
	
	
	$.get('/dispersion/loadDispersionTrend.tv', {
		opticalNodeId: opticalNodeId,
		startTime: Ext.util.Format.date(startTime, 'Y-m-d H:i:s'),
		endTime: Ext.util.Format.date(endTime, 'Y-m-d H:i:s')
	}, function(json){
		//组装四条曲线
		if($.isArray(json)){
			if(!json.length){
				//表示没有数据
				noData();
				return
			}
			var lines = format4Line(json);
			try{
				creatTrendChart(lines);
				loadDispersionByIdAndTime(lines['upSnrAvg'][0][0]);
			}catch(e){
			}
		}else{
			//数据不正常
			noData();
		}
	});
	
	function noData(){
		$('#trendChart').hide();
		$('#menu').hide();
		$('#snrDistributionChart').hide();
		$('#powerDistributionChart').hide();
		$('#tip').css({
			left: $(window).outerWidth()/2 - $('#tip').width()/2,
			top: Math.min($(window).outerHeight()/2 - $('#tip').height()/2,180)
		}).show();
	}
	
	function format4Line(json){
		var ret = {
			upSnrAvg:[],
			upSnrStd:[],
			upPowerAvg:[],
			upPowerStd:[]
		}, curObj 
			,curTimelines = [];
		for(var i=0, len=json.length; i<len; i++){
			curObj = json[i];
			curTimelines.push(curObj.collectTime.time);
			ret.upSnrAvg.push([curObj.collectTime.time, curObj.upSnrAvg]);
			ret.upSnrStd.push([curObj.collectTime.time, curObj.upSnrStd]);
			ret.upPowerAvg.push([curObj.collectTime.time, curObj.upPowerAvg]);
			ret.upPowerStd.push([curObj.collectTime.time, curObj.upPowerStd]);
		}
		timelines = curTimelines;
		return ret;
	}
	
	function creatTrendChart(lines){
		$('#trendChart').highcharts({
	        chart: {
	            zoomType: 'x'
	        },
	        title: {
	            text: '@dispersion.trendChart@ - ' + opticalNode.opticalNodeName + '(' + opticalNode.manageIp + ')' 
	        },
	        xAxis : {
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
	        yAxis: [{ // 接收SNR平均值
	            labels: {
	                formatter: function() {
	                    return this.value +'dB';
	                },
	                style: {
	                    color: COLORS.SNR_AVG
	                }
	            },
	            title: {
	                text: '@dispersion.upsnr@@dispersion.avg@',
	                style: {
	                    color: COLORS.SNR_AVG
	                }
	            },
	            opposite: false

	        }, { // 接收SNR标准差
	            gridLineWidth: 0,
	            title: {
	                text: '@dispersion.upsnr@@dispersion.std@',
	                style: {
	                    color: COLORS.SNR_SDV
	                }
	            },
	            labels: {
	                style: {
	                    color: COLORS.SNR_SDV
	                }
	            }

	        }, { // 上行发送电平平均值
	            gridLineWidth: 0,
	            title: {
	                text: '@dispersion.uppower@@dispersion.avg@',
	                style: {
	                    color: COLORS.PWR_AVG
	                }
	            },
	            labels: {
	                formatter: function() {
	                    return this.value +' dB';
	                },
	                style: {
	                    color: COLORS.PWR_AVG
	                }
	            },
	            opposite: true
	        }, { // 上行发送电平标准差
	            gridLineWidth: 0,
	            title: {
	                text: '@dispersion.uppower@@dispersion.std@',
	                style: {
	                    color: COLORS.PWR_SDV
	                }
	            },
	            labels: {
	                style: {
	                    color: COLORS.PWR_SDV
	                }
	            },
	            opposite: true
	        }],
	        tooltip: {
	        	crosshairs: true,
	            shared: true,
	            xDateFormat: '%Y-%m-%d %H:%M:%S'
	        },
	        credits: {
	            enabled:false
	        },
	        series: [{
	            name: '@dispersion.upsnr@@dispersion.avg@',
	            color: COLORS.SNR_AVG,
	            type: 'spline',
	            yAxis: 0,
	            events:{
	            	click: function(event){
	            		loadDispersionByIdAndTime(event.point.x);
	            	}
	            },
	            data: lines.upSnrAvg,
	            tooltip: {
	                valueSuffix: ' dB'
	            }

	        }, {
	            name: '@dispersion.upsnr@@dispersion.std@',
	            type: 'spline',
	            color: COLORS.SNR_SDV,
	            yAxis: 1,
	            events:{
	            	click: function(event){
	            		loadDispersionByIdAndTime(event.point.x);
	            	}
	            },
	            data: lines.upSnrStd,
	            marker: {
	                enabled: false
	            },
	            dashStyle: 'shortdot'
	        }, {
	            name: '@dispersion.uppower@@dispersion.avg@',
	            color: COLORS.PWR_AVG,
	            type: 'spline',
	            yAxis: 2,
	            events:{
	            	click: function(event){
	            		loadDispersionByIdAndTime(event.point.x);
	            	}
	            },
	            data: lines.upPowerAvg,
	            tooltip: {
	                valueSuffix: ' dB'
	            }
	        }, {
	            name: '@dispersion.uppower@@dispersion.std@',
	            color: COLORS.PWR_SDV,
	            type: 'spline',
	            yAxis: 3,
	            events:{
	            	click: function(event){
	            		loadDispersionByIdAndTime(event.point.x);
	            	}
	            },
	            data: lines.upPowerStd
	        }]
	    });
	}
	
}

function loadFirstDist(){
	loadDistByIndex(0);
}

function loadLastDist(){
	loadDistByIndex(timelines.length-1);
}

function loadPrevDist(){
	if(curPointIndex===0){
		return;
	}
	loadDistByIndex(curPointIndex-1);
}

function loadNextDist(){
	if(curPointIndex===timelines.length-1){
		return;
	}
	loadDistByIndex(curPointIndex+1);
}

function loadDistByIndex(index){
	curPointIndex = index;
	loadDispersionByIdAndTime(timelines[curPointIndex]);
	checkTime(curPointIndex);
}

function checkTime(curPointIndex){
	if(curPointIndex===0){
		$('#prevCollectTime').attr('disabled', 'disabled');
	}else{
		$('#prevCollectTime').removeAttr('disabled');
	}
	if(curPointIndex===timelines.length-1){
		$('#nextCollectTime').attr('disabled', 'disabled');
	}else{
		$('#nextCollectTime').removeAttr('disabled');
	}
}

function loadDispersionByIdAndTime(time){
	curPointIndex = timelines.indexOf(time);
	checkTime(curPointIndex);
	var date = new Date();
	date.setTime(time);
	
	//在趋势图上标出当前所在的采集点
	var trendChart = $('#trendChart').highcharts();
	trendChart.xAxis[0].removePlotLine('curTimeLine');
	trendChart.xAxis[0].addPlotLine({
		color:'red',
        dashStyle:'longdashdot',
        value:time,
        width:2,
        id: 'curTimeLine'
    });
	
	var exactTime = Ext.util.Format.date(date, 'Y-m-d H:i:s');
	$.get('/dispersion/loadDispersionByIdAndTime.tv', {
		opticalNodeId: opticalNodeId,
		exactTime: exactTime
	}, function(json){
		if(!json){
			return;
		}
		var distObj = formatDist(json);
		//显示选中时刻
		$('#exactTime').text(exactTime);
		//绘制此时的upsnr分布图和uppower分布图
		createSnrChart(distObj.upSnrDist);
		createPowerChart(distObj.upPowerDist);
		resize();
	});
	
	function formatDist(json){
		var ret = {
				upSnrDist:[],
				upSnrDist:[]
			}, 
			upSnrDist = json.upSnrDist,
			upPowerDist = json.upPowerDist;
		ret.upSnrDist = convertStrToArr(upSnrDist);
		ret.upPowerDist = convertStrToArr(upPowerDist);
		return ret;
		
		
		function convertStrToArr(dist){
			var ret = [], points = dist.split(';'), curPoint;
			for(var i=0; i<points.length; i++){
				curPoint = points[i];
				if(curPoint.indexOf(',')===-1){break;}
				ret.push([parseInt(points[i].split(',')[0]), parseInt(points[i].split(',')[1])]);
			}
			return ret;
		}
	}
	
	function createSnrChart(data){
    	$('#snrDistributionChart').highcharts({
			title: {
				text: '@dispersion.upsnrDistChart@',
				x: -20 //center 
			},
			xAxis: {
				tickInterval: 1,
				plotLines: [{
					color: '#FF0000',
    				width: 2,
					value: caculateAvg(data)
				}]
			},
			yAxis: {
				title: {
					text: '@dispersion.cmNum@'
				},
				min: 0
			},
			credits: {
				enabled: false
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'top',
				floating: true,
				borderWidth: 0
			},
			tooltip: {
				formatter: function(){
					var template = '@dispersion.upsnr@:{0}(dB)<br/>@dispersion.cmNum@:{1}';
					return String.format(template, this.x, this.y);
				}
			},
			series: [{
				type: 'spline',
				name: '@dispersion.upsnr@',
				data: data
			}]
		});
    }
    
    function createPowerChart(data){
    	$('#powerDistributionChart').highcharts({
			title: {
				text: '@dispersion.uppowerDistChart@',
				x: -20 //center 
			},
			xAxis: {
				tickInterval: 1,
				plotLines: [{
					color: '#FF0000',
    				width: 2,
					value: caculateAvg(data)
				}]
			},
			yAxis: {
				title: {
					text: '@dispersion.cmNum@'
				},
				min: 0
			},
			credits: {
				enabled: false
			},
			tooltip: {
				formatter: function(){
					var template = '@dispersion.uppower@:{0}(dBmV)<br/>@dispersion.cmNum@:{1}';
					return String.format(template, this.x, this.y);
				}
			},
			legend: {
				layout: 'vertical',
				align: 'right',
				verticalAlign: 'top',
				floating: true,
				borderWidth: 0
			},
			series: [{
				type: 'spline',
				name: '@dispersion.uppower@',
				data: data
			}]
		});
    }
    
    function caculateAvg(line){
    	var denominator = 0,
			numerator = 0;
    	for(var i=0, len=line.length; i<len; i++){
    		numerator += line[i][0] * line[i][1];
			denominator += line[i][1];
    	}
    	return numerator/denominator;
    }
}

function resize(){
	initLayout();
	//设置各chart的大小
	var trendChart = $('#trendChart').highcharts();
	var snrDistributionChart = $('#snrDistributionChart').highcharts();
	var powerDistributionChart = $('#powerDistributionChart').highcharts();
	
	trendChart && trendChart.setSize($('#trendChart').width(), $('#trendChart').height());
	snrDistributionChart && snrDistributionChart.setSize($('#snrDistributionChart').width(), $('#snrDistributionChart').height());
	powerDistributionChart && powerDistributionChart.setSize($('#powerDistributionChart').width(), $('#powerDistributionChart').height());
}

function initLayout(){
	//设置趋势图大小
	$('#trendChart').height($('body').height()*0.58);
	//设置分布图大小
	var h = $('body').height() - $('#trendChart').height() - $('#menu').height();
	var w = $('body').width();
	$('#snrDistributionChart').height(h);
	$('#snrDistributionChart').width(w/2);
	$('#powerDistributionChart').height(h);
	$('#powerDistributionChart').width(w/2);
}

function throttle(fn, delay){
   var timer = null;
   return function(){
	   var context = this, args = arguments;
	   clearTimeout(timer);
	   timer = setTimeout(function(){
		   fn.apply(context, args);
	   }, delay);
   }
}

function test(){
	$.post('/dispersion/test.tv', function(){});
}