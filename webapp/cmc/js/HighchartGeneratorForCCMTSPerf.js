var HighchartGeneratorForCCMTSPerf = {}
HighchartGeneratorForCCMTSPerf.generate = function(renderTo, title, yAxias , unit, type){
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
							return value*100 + "%";
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
					//allowDecimals: false,
					showFirstLabel : false,
					title : {
						text : yAxias
					}
				}];
				break;
	};
	return  {
		chart:{
			id: renderTo,
			marginTop: 100,
			renderTo: renderTo,
			spacingRight:20,
			type:"line",
			zoomType:"x"
		},
		credits:{
			enabled:false
		},
		legend:{
			enabled:true
		},
		plotOptions:{
			line:{
				animation : false ,
				lineWidth:3,
				marker:{
					enabled:false,
					states:{
						hover:{
							enabled:true,
							radius:5}
					}
				},
				shadow:false,
				states:{
					hover:{
						lineWidth:3
					}
				}
			}
		},
		subtitle:{
			text : "@RESOURCES/HighChartsUtils.moveToSelectViewTimeRange@",
			y:55
		},
		title:{
			text: title,
			y:35
		},
		tooltip: {
			crosshairs : false,
			shared : false,
			formatter: function() {
				var color = this.series.color;
				var name = yAxias
				var UTC_time  = this.x //+ 28800000 ;  
				if (unit) {
					return  "<span style='color : "+color+"'>" + name + "</span> : " + this.y + unit
					        + "<br><font style='color:#517CAD'>@base/COMMON.collectTime@:</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time ) +'</span><br/>'
				} else {
					return  "<span style='color : "+color+"'>" + name + "</span> : " + this.y 
					        + "<br><font style='color:#517CAD'>@base/COMMON.collectTime@:</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', UTC_time ) +'</span><br/>'
				}
				
			}
		},
		xAxis:[
			{
				max : viewerParam.etLong,
				min : viewerParam.stLong,
				tickPixelInterval:75,
				minRange: 3600000,
				type:"datetime",
				labels : {
		    		formatter : function(){
		    			var type = viewerParam.timeType;
		    			var UTC_time  = this.value //+ 28800000;
		    			if(["Today","Yesterday","The last two days","The last seven days","This month","Last month","The last thirty days"].indexOf(type) > -1 ){
		    				if(UTC_time % 86400000 == 57600000){
		    					if(lang == "zh_CN"){
		    						return Highcharts.dateFormat('%e@base/CALENDAR.Day@', UTC_time);	
		    					}else{
		    						return Highcharts.dateFormat('%e,%b', UTC_time);
		    					}
		    				}else{
		    					return Highcharts.dateFormat('%H:%M', UTC_time);
		    				}
		    			}else {
		    				if(lang == "zh_CN"){
		    					return Highcharts.dateFormat('%b%e@base/CALENDAR.Day@', UTC_time);
		    				}else{
		    					return Highcharts.dateFormat('%e,%b', UTC_time);
		    				}
		    			}
		    		}
		    	}
			}
		],
		yAxis:yAxiaConfig
	};
}