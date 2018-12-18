Highcharts.setOptions({ 
	global: { useUTC: false } 
});
var colors = Highcharts.getOptions().colors
Highcharts.theme = {
	chart: {
		backgroundColor: {
			linearGradient: [0, 0, 0, 200],
			stops: [
				[0, 'rgb(92, 89, 79)'],
				[1, 'rgb(26, 24, 19)']
			]
		},
		borderWidth: 1,
		borderColor: 'silver',
		borderRadius: 0,
		plotBackgroundColor: null,
		plotShadow: false,
		plotBorderWidth: 0
	},
	title: {
		style: { 
			color: '#FFF',fontSize: '14px'
		}
	},
	credits: {
		enabled: false
	},
	subtitle: null ,
	xAxis: {
		gridLineWidth: 0,
		lineColor: 'gray',
		tickColor: 'gray',
		gridLineColor : 'gray',
		labels: {
			style: {
				color: '#999',
				fontWeight: 'bold'
			}
		},
		title: {
			style: {
				color: '#AAA',
				font: 'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			}				
		}
	},
	yAxis: {
		/*alternateGridColor: null,
		minorTickInterval: null,*/
		//gridLineColor: 'rgba(255, 255, 255, .01)',
		/*gridLineColor: 'gray',
		lineWidth: 0,
		tickWidth: 0,*/
		/*labels: {
			style: {
				color: '#999',
				fontWeight: 'bold'
			}
		},
		title: {
			style: {
				color: '#AAA',
				font: 'bold 12px Lucida Grande, Lucida Sans Unicode, Verdana, Arial, Helvetica, sans-serif'
			}				
		}*/
	},
	lang : {
		   rangeSelectorZoom : I18N.PERF.fastZoom 
	} ,
	legend: {
		itemStyle: {
			color: '#CCC'
		},
		itemHoverStyle: {
			color: '#FFF'
		},
		itemHiddenStyle: {
			color: '#333'
		}
	},
	labels: {
		style: {
			color: '#CCC'
		}
	},
	tooltip: {
		backgroundColor: {
			linearGradient: [0, 0, 0, 50],
			stops: [
				[0, 'rgba(96, 96, 96, .8)'],
				[1, 'rgba(16, 16, 16, .8)']
			]
		},
		borderWidth: 0,
		style: {
			color: '#FFF'
		}
	},
	
	
	plotOptions: {
		series: {
			dataLabels: {
				color: '#CCC',
				connectorColor: '#CCC'
			}
		},
		line: {
			dataLabels: {
				color: '#CCC'
			},
			marker: {
				lineColor: '#333'
			}
		},
		spline: {
			marker: {
				lineColor: '#333'
			}
		},
		scatter: {
			marker: {
				lineColor: '#333'
			}
		},
		candlestick: {
			lineColor: 'white'
		}
	},
	
	toolbar: {
		itemStyle: {
			color: '#CCC'
		}
	},
	
	navigation: {
		buttonOptions: {
			backgroundColor: {
				linearGradient: [0, 0, 0, 20],
				stops: [
					[0.4, '#606060'],
					[0.6, '#333333']
				]
			},
			borderColor: '#000000',
			symbolStroke: '#C0C0C0',
			hoverSymbolStroke: '#FFFFFF'
		}
	},
	
	exporting: {
		buttons: {
			exportButton: {
				symbolFill: '#55BE3B'
			},
			printButton: {
				symbolFill: '#7797BE'
			}
		}
	},
	
	// scroll charts
	rangeSelector: {
		buttonTheme: {
			fill: {
				linearGradient: [0, 0, 0, 20],
				stops: [
					[0.4, '#888'],
					[0.6, '#555']
				]
			},
			stroke: '#000000',
			style: {
				color: '#CCC',
				fontWeight: 'bold'
			},
			states: {
				hover: {
					fill: {
						linearGradient: [0, 0, 0, 20],
						stops: [
							[0.4, '#BBB'],
							[0.6, '#888']
						]
					},
					stroke: '#000000',
					style: {
						color: 'white'
					}
				},
				select: {
					fill: {
						linearGradient: [0, 0, 0, 20],
						stops: [
							[0.1, '#000'],
							[0.3, '#333']
						]
					},
					stroke: '#000000',
					style: {
						color: 'yellow'
					}
				}
			}					
		},
		inputStyle: {
			backgroundColor: '#333',
			color: 'silver'
		},
		labelStyle: {
			color: 'silver'
		}
	},
	
	navigator: {
		handles: {
			backgroundColor: '#666',
			borderColor: '#AAA'
		},
		outlineColor: '#CCC',
		maskFill: 'rgba(16, 16, 16, 0.5)',
		series: {
			color: '#7798BF',
			lineColor: '#A6C7ED'
		}
	},
	
	scrollbar: {
		barBackgroundColor: {
				linearGradient: [0, 0, 0, 20],
				stops: [
					[0.4, '#888'],
					[0.6, '#555']
				]
			},
		barBorderColor: '#333',
		buttonArrowColor: '#CCC',
		buttonBackgroundColor: {
				linearGradient: [0, 0, 0, 20],
				stops: [
					[0.4, '#888'],
					[0.6, '#555']
				]
			},
		buttonBorderColor: '#333',
		rifleColor: '#FFF',
		trackBackgroundColor: {
			linearGradient: [0, 0, 0, 10],
			stops: [
				[0, '#000'],
				[1, '#333']
			]
		},
		trackBorderColor: '#333'
	},
	
	// special colors for some of the demo examples
	//legendBackgroundColor: 'rgba(48, 48, 48, 0.8)',
	//legendBackgroundColorSolid: 'rgb(70, 70, 70)',
	dataLabelsColor: '#444',
	textColor: '#E0E0E0',
	maskColor: 'rgba(255,255,255,0.3)'
};

// Apply the theme
var highchartsOptions = Highcharts.setOptions(Highcharts.theme);
var executor = new Object;  
/**
 * 创建静态曲线图(历史性能)
 */
executor.createStaticCurve = function(){
	for(var i=0;i<historyPerformance.length;i++){
		historyPerformance[i].name = convertIdToName(historyPerformance[i].name) //need convert
		for(var j=0;j<historyPerformance[i].data.length;j++){
			historyPerformance[i].data[j][0] = new Date(historyPerformance[i].data[j][0]).getTime()
			historyPerformance[i].data[j][1] = parseInt(historyPerformance[i].data[j][1])
		}
	}
	var localTitle , buttons;
	//默认是15分钟
	if (1 == 1) {
		basicInterval = 60*15
		buttons = [
			          {type: 'day',count: 1,text: I18N.PERF.mo.aDay}, 
			          {type: 'week',count: 1,text: I18N.PERF.mo.aWeek}, 
			          {type: 'month', count: 1, text: I18N.PERF.mo.aMonth}
		         ]
	} else {
		basicInterval = 60*60*24		
		buttons = [
			          {type: 'week',count: 1,text: I18N.PERF.mo.aWeek}, 
			          {type: 'month',count: 1,text: I18N.PERF.mo.aMonth}, 
			          {type: 'year', count: 1, text: I18N.PERF.mo.aYear}
		         ]
	}
	
	var chart = new Highcharts.StockChart({
	    chart: {
	        renderTo: 'container',
	        type : 'spline',
			zoomType: 'x',
			events : {
				'load' : function(chart){
					if( 'ok' == param.queryStatus){
						this.setTitle(null, {
							text: param.date,
							align : 'center',
							verticalAlign : 'top',
							y : 30
						})
					}else{
						this.setTitle(null, {
							text: param.date + "(" + I18N.PERF.mo.noQueryInfo + ")",
							align : 'center',
							verticalAlign : 'top',
							y : 35
						})
					}
				}
			}
	   },
	   loading: {
		   showDuration : 2000
	   },
	   credits: {enabled : false},
	   rangeSelector: {
		    buttonSpacing : 2,inputEnabled: false,buttons: buttons,selected: 0,
		    buttonTheme : {
		    	'class' : 'highchart-selector-topvision' ,
		    	width : 40 ,
		    	height : 15 ,
		    	style : {
		    		padding : 1,top : 0 
		    	}
		    }
	    },
	    //X轴设置tickInterval后会导致X轴混乱,全部拥挤在一起
	    xAxis: {
	    	tickPixelInterval : 180,
	    	/*startOnTick : true, 
	    	endOnTick : true,*/
	    	type: 'datetime',
	    	labels : {
	    		formatter : function(){
	    			var UTC_time  = this.value //+ 28800000 ;  
	    			return Highcharts.dateFormat('%H:%M', UTC_time)
	    		}
	    	}
	    },
		yAxis: {
			title: {text: IndexUtil.getChineseName(param.index) },gridLineColor : 'gray',
			labels: {
				formatter : function(){
					if(["InOctets","OutOctets"].indexOf(param.index) != -1){
						return DateUtil.getFlowDisplayRest(this.value,1);
					}else{
						if(this.value < 100000){
							return this.value;
						}if(this.value < 100000000 ){
							return this.value / 1000 + " * e3";
						}else if(this.value < 100000000){
							return this.value / 1000000 + " * e6";
						}else{
							return this.value / 1000000000 + " * e9";
						}
					}
	    		}
			},
			/*startOnTick : true,min:0,*/minorGridLineWidth: 0,gridLineWidth: 1//,alternateGridColor: null
		},
	    title: { text: IndexUtil.getChineseName(param.index)},
		scrollbar :{height : 10,barBorderRadius : 5,rifleColor : 'white'},
		navigator: {
			baseSeries : 0,enabled : true,height : 30 ,
			xAxis: {
				labels: {
					formatter : function(){
						var UTC_time  = this.value //+ 28800000 ;  
	    				return Highcharts.dateFormat('%Y/%m/%e %H:%M', UTC_time)
		    		}
				}
			}
		},
		legend : { enabled : true , floating : true , verticalAlign:'top' , align:'right' ,itemStyle: {
			   fontSize: '10px'
		},borderWidth : 0,backgroundColor:null,y:25 },
		tooltip: {
			crosshairs : false,
			shared : false,
			formatter: function() {
				var color = this.series.color
				var name = this.series.name
				var UTC_time  = this.x //+ 28800000 ;  
				if(this.y == -1) //#517CAD.未采集到
					return  "<div style='color : "+color+"'>" + name + "</div>" 
							+ "<font style='color:#517CAD'>" + I18N.PERF.mo.nowValue 
							+ " :</font> <span style='color:red'>" + I18N.PERF.mo.notCollected + "</span><br>"
							+ "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
							+ Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time) +'</span><br/>'
				if(["InOctets","OutOctets"].indexOf(param.index) != -1){
					var value = this.y ,
						flow,
						oct,
						flowTitle,
						octTitle;
					flow = DateUtil.getFlowDisplayRest(this.y,1);
					oct = DateUtil.getOctDisplayRest(this.y);
					if( "OutOctets" == param.index ){
						flowTitle = I18N.PERF.moI.InFlowsSing;
						octTitle = I18N.PERF.moI.InOctetsSing;
					}else if( "InOctets" == param.index ){
						flowTitle = I18N.PERF.moI.OutFlowSing;
						octTitle = I18N.PERF.moI.OutOctetsSing;
					}
					return  "<div style='color : " + color + "'>" + name + "</div>"
					 		+ "<font style='color:#517CAD'>"+flowTitle+" :</font> " +  flow +  "<br>" 
					 		+ "<font style='color:#517CAD'>"+octTitle+" :</font> " +  oct +  "<br>" 
					        + "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time) +'</span><br/>';
				}
				return  "<div style='color : "+color+"'>" + name + "</div>" 
						+ "<font style='color:#517CAD'>" + I18N.PERF.mo.nowValue + " :</font> " +  this.y  +"<br>" 
				        + "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
				        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time) +'</span><br/>'
			}
		},
		series : window.historyPerformance
	})
	$(".highchart-selector-topvision span").css('marginTop',3)
}

/**
 * EXT JSONSTORE无法识别带ip的dataIndex,所以需要转换
 * demo  3/1(172.10.10.39) - 3/1/172101039
 * @param name
 */
function nameToDataIndex(name){
	return name.replaceAll("." , "")
}