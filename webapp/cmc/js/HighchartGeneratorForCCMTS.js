var HighchartGeneratorForCCMTS = {}
HighchartGeneratorForCCMTS.generate = function(renderTo, title, yAxias , unit , type){
	var yAxiaConfig;
	switch(type){
		case 'percent' :
				yAxiaConfig = [{
					min : 0,
					showFirstLabel : false,
					minTickInterval: 0.01,
					labels : {
						formatter : function(){
							var value = this.value;
							return value + "%";
						}
					},
					title : {
						text : yAxias
					}
				}];
				break;
		default :
				yAxiaConfig = [{
					min : 0,
					allowDecimals: false,
					showFirstLabel : false,
					title : {
						text : yAxias
					}
				}];
				break;
	};
	return  {
		chart : {
			height : 300,
			id : renderTo,
			marginRight : 50,
			renderTo : renderTo,
			spacingRight : 20,
			type : "line",
			zoomType : "x"
		},
		credits : {
			enabled : false
		},
		global : {
			useUTC : false
		},
		legend : {
			"enabled" : true
		},
		plotOptions : {
			"line" : {
				animation : false ,
				lineWidth : 2,
				marker : {
					enabled : false,
					states : {
						hover : {
							enabled : true,
							radius : 5
						}
					}
				},
				shadow : false,
				states : {
					hover : {
						"lineWidth" : 3
					}
				}
			}
		},
		subtitle : {
			text : "@RESOURCES/HighChartsUtils.moveToSelectViewTimeRange@",
			y : 40
		},
		title : {
			text : title
		},
		tooltip: {
			crosshairs : false,
			shared : false,
			formatter: function() {
				var color = this.series.color
				var name = this.series.name
				var UTC_time  = this.x //+ 28800000 ;  
				if (unit) {
					return  "<span style='color : "+color+"'>" + name + "</span> : " + this.y + unit
					        + "<br><font style='color:#517CAD'>@base/COMMON.collectTime@:</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time) +'</span><br/>'
				} else {
					return  "<span style='color : "+color+"'>" + name + "</span> : " + this.y 
					        + "<br><font style='color:#517CAD'>@base/COMMON.collectTime@:</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time) +'</span><br/>'
				}
				
			}
		},
		xAxis : [{
			max : viewerParam.etLong,
			min : viewerParam.stLong,
			minRange: 3600000,
			type : "datetime",
			labels: {
				formatter : function(){
					var UTC_time  = this.value //+ 28800000 ;  
    				if(UTC_time % 86400000 == 57600000){
    					if(lang == "zh_CN"){
    						return Highcharts.dateFormat('%e@base/CALENDAR.Day@', UTC_time);
    					}else{
    						return Highcharts.dateFormat('%e,%b', UTC_time);
    					}
    				}else{
    					return Highcharts.dateFormat('%H:%M', UTC_time);
    				}
	    		}
			}
		}],
		yAxis : yAxiaConfig
	};
}

Highcharts.setOptions({
    global: {
        useUTC: false
    }
});