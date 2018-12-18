var chartUtil = {
	createChannel : function(title,renderTo,flow,speed){
		var cfg =   {
			chart: { zoomType : "x"},
			title: { text: title },
			legend : { enabled : true , floating : true , verticalAlign:'top' , align:'right', y:-10, x:10},
			plotOptions : {
				line : {
					animation : false ,lineWidth : 1,
					marker : {enabled : false,states : {hover : {enabled : true,radius : 1}}},
					shadow : false,states : {hover : {lineWidth : 1}}
				},
				area: {
					fillColor: {
						linearGradient: [0, 0, 0, 300],
						stops: [
							[0, Highcharts.getOptions().colors[0]],
							[1, 'rgba(2,0,0,0)']
						]
					},
					lineWidth: 1,
					marker: {
						enabled: false,
						states: {
							hover: {
								enabled: true,
								radius: 5
							}
						}
					},
					shadow: false,
					states: {
						hover: {
							lineWidth: 1
						}
					}
				}
			},
			tooltip: {
                formatter: function() {
                   switch(this.series.name){
                   		case '@monitor.realFlow@':
                   			return "@monitor.realFlow@ :" + this.y + " Mbps <br> @COMMON.collectTime@ :" + Highcharts.dateFormat('@monitor.timeFormat@', this.x);
                   		case '@monitor.standardFlow@':
                   			return "@monitor.standardFlow@:" + this.y + " Mbps <br> @COMMON.collectTime@:" + Highcharts.dateFormat('@monitor.timeFormat@', this.x); 
                   }
                }
            }, 
			credits: {enabled : false},
			xAxis : [{
				minRange: 3600000,type : "datetime",labels : {
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
			yAxis: [{
				title :{
	                text: "@cmMonitor.rateWithUnit@"
	            },
				min : 0
			}/*,{
				opposite: true,
				title : {
	                text: "@cmMonitor.bandwidthWithUnit@"
	            },
				min : 0
			}*/],
			series: [{name:"@monitor.realFlow@",data:flow,type: 'line'}/*,
			         {name:"@monitor.standardFlow@",data:speed,type: 'line',yAxis: 1}*/]
		};
		return this.create(cfg,title,renderTo);
	},
	
	createCurErrorRate : function(title,renderTo,data){
		var cfg =  {
				chart: { height:197},
				xAxis : {
					categories: ["@monitor.noError@","@monitor.errorCode@","@monitor.sigErrorCode@"],
					labels: {
						y : 18
					}
				},
				credits: {enabled : false},
				legend : { enabled : false},
				yAxis: {
					title : false,
					stackLabels: {
						enabled: true,
						style: {
							fontWeight: 'bold',
							color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
						}
					}
				},
				plotOptions: {
					column: {
						dataLabels: {
							enabled: true
						}
					}
				},
				tooltip: {
	                formatter: function() {
	                   return this.x + " : " + this.y;
	                }
	            }, 
				title: false,
				series: [
				    {type: 'column',data: data ? data.columns : []}
				]
		};
		return this.create(cfg,title,renderTo,$("#sigNoiseChartPanelBody").width(),230);
	},
	
	createGeneral : function(title,renderTo,series,chartTitle,unit,denyDecimal, yMin){
		var cfg =   {
			chart: {type: 'line', zoomType : "x"},
			title: { text: title },
			plotOptions : {
				"line" : {
					animation : false ,lineWidth : 1,
					marker : {enabled : false,states : {hover : {enabled : true,radius : 1}}},
					shadow : false,states : {hover : {lineWidth : 1}}
				}
			},
			credits: {enabled : false},legend : { enabled : false},
			xAxis : [{
				minRange: 3600000,type : "datetime",labels : {
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
			yAxis: {
				title : {
	                text: chartTitle + "(" + unit + ")"
	            }, allowDecimals : !denyDecimal,
	            min : yMin
			},
			tooltip: {
                formatter: function() {
                   return   chartTitle + " : " + this.y +unit + " <br> @COMMON.collectTime@ :" + Highcharts.dateFormat('@monitor.timeFormat@', this.x);
                }
            }, 
			series: [{data:series}]
		};
		return this.create(cfg,title,renderTo);
	},
	
	create :function(cfg,title,renderTo,width,height){
		cfg.chart.width = width || (perfPanelWidth-20);		
		cfg.chart.height = (height || 197) - 25;
		var chartName = cfg.chart.renderTo = renderTo+"EL";
		if(!perfPanelWidth){
			var bodyWidth = doc.body.offsetWidth;
			var cmListWidth = doc.getElementById("cmList").offsetWidth;
			window.perfPanelWidth = bodyWidth-cmListWidth-20;
		}
		var $el =  $("#"+chartName);
		if($el.length > 0){
			$el.empty();
    		new Highcharts.Chart(cfg);
			return Ext.getCmp(renderTo+"table").show();
		}else{
			var chartEl = document.createElement("div");
			chartEl.id = chartName;
			return  new Ext.Panel({
		        renderTo : renderTo,
		        id : renderTo+"table",
		        height : height || 197,
		        border : false,
		        listeners:{
	            	afterrender:function(){
	            		CHART_MAP[renderTo] = new Highcharts.Chart(cfg);
	            	}
	            },
	            contentEl : chartEl
		    });
		}
		
	}
}