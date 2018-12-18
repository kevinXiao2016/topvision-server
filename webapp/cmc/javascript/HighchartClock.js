(function (){
	/**
	 * Pad numbers
	 */
	function pad(number, length) {
		// Create an array of the remaining length +1 and join it with 0's
		return new Array((length || 2) + 1 - String(number).length).join(0) + number;
	}
	/**
	 * 
	 * @param hour
	 * @param minute
	 * @param second
	 * @returns {___anonymous314_499}
	 */
	function changeTime (hour, minute, second) {        
	    return {
	        hours: hour + minute / 60,
	        minutes: minute * 12 / 60 + second * 12 / 3600,
	        seconds: second * 12 / 60
	    };
	};

	var defaultOptions = {
			id: "container",
			chart: {
				height: 200
			},
			title: {
				text: "Highchart Clock"
			}		
				
	}

	window.HighchartClock = function (options, hour, minute, second){
		this.options = options;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.time = changeTime(hour, minute, second);
		
	}

	HighchartClock.prototype.createHighchartClock = function (){
		var container = this.options.id ? this.options.id : defaultOptions.id;
		var chart = new Highcharts.Chart({
			
			chart: {
				renderTo: container,
		        type: 'gauge',
		        plotBackgroundColor: null,
		        plotBackgroundImage: null,
		        plotBorderWidth: 0,
		        plotShadow: false,
		        height:  this.options.chart.height ? this.options.chart.height: defaultOptions.chart.height
		    },
		    
		    credits: {
		        enabled: false
		    },
		    
		    title: {
		    	text: this.options.title.text ? this.options.title.text: defaultOptions.title.text
		    },
		    
		    pane: {
		    	background: [{
		    		// default background
		    	}, {
		    		// reflex for supported browsers
		    		backgroundColor: Highcharts.svg ? {
			    		radialGradient: {
			    			cx: 0.4,
			    			cy: -0.3,
			    			r: 1.8
			    		},
			    		stops: [
			    			[0.45, 'rgba(226, 242, 252, 0.5)'],
			    			[0.55, 'rgba(200, 200, 200, 0.3)']
			    		]
			    	} : null
		    	}]
		    },
		    
		    yAxis: {
		        labels: {
		            distance: -20,
		            style: {
		            	display: "none"
		            }
		        },
		        min: 0,
		        max: 12,
		        lineWidth: 0,
		        showFirstLabel: false,
		        
		        minorTickInterval: 'auto',
		        minorTickWidth: 1,
		        minorTickLength: 2,
		        minorTickPosition: 'inside',
		        minorGridLineWidth: 0,
		        minorTickColor: '#666',
		
		        tickInterval: 1,
		        tickWidth: 2,
		        tickPosition: 'inside',
		        tickLength: 8,
		        tickColor: '#666',
		        title: {
		            style: {
		                color: '#BBB',
		                fontWeight: 'normal',
		                fontSize: '8px',
		                lineHeight: '10px'                
		            },
		            y: 10
		        }       
		    },
		    
		    tooltip: {
		    	formatter: function () {
		    		return this.series.chart.tooltipText;
		    	}
		    },
		
		    series: [{
		        data: [{
		            id: 'hour',
		            y: this.time.hours,
		            dial: {
		                radius: '60%',
		                baseLength: '95%',
		                rearLength: 0,
		                backgroundColor: '#666'
		            }
		        }, {
		            id: 'minute',
		            y: this.time.minutes,
		            dial: {
		                baseLength: '95%',
		                rearLength: 0,
		                backgroundColor: '#666'
		            }
		        }, {
		            id: 'second',
		            y: this.time.seconds,
		            dial: {
		                radius: '100%',
		                baseWidth: 1,
		                rearLength: '20%',
		                backgroundColor: '#666'
		            }
		        }],
		        animation: false,
		        dataLabels: {
		            enabled: false
		        }
		    }]
		});
		this.move(chart);
	}

	HighchartClock.prototype.timeRun = function (){
		this.second ++;
		if(this.second == 60){
			this.second = 0;
			this.minute ++;
			if(this.minute == 60){
				this.minute = 0;
				this.hour ++;
				if(this.hour == 13){
					this.hour = 12
				}
			}
		}
		this.time = changeTime(this.hour, this.minute, this.second);
	}

	//Move
	HighchartClock.prototype.move = function (chart) {
		var _this = this;
	    setInterval(function () {
	        var hour = chart.get('hour'),
	            minute = chart.get('minute'),
	            second = chart.get('second'),
	            // run animation unless we're wrapping around from 59 to 0
	            animation = _this.time.seconds == 0 ? 
	                false : 
	                {
	                    easing: 'easeOutElastic'
	                };
	        // Cache the tooltip text
	        chart.tooltipText = 
				pad(Math.floor(_this.time.hours), 2) + ':' + 
	    		pad(Math.floor(_this.time.minutes * 5), 2) + ':' + 
	    		pad(_this.time.seconds * 5, 2);
	        _this.timeRun();
	        hour.update(_this.time.hours, true, animation);
	        minute.update(_this.time.minutes, true, animation);
	        second.update(_this.time.seconds, true, animation);	        
	    }, 1000);	
	}

	//Extend jQuery with some easing (copied from jQuery UI)
	$.extend($.easing, {
	    easeOutElastic: function (x, t, b, c, d) {
	        var s=1.70158;var p=0;var a=c;
	        if (t==0) return b;  if ((t/=d)==1) return b+c;  if (!p) p=d*.3;
	        if (a < Math.abs(c)) { a=c; var s=p/4; }
	        else var s = p/(2*Math.PI) * Math.asin (c/a);
	        return a*Math.pow(2,-10*t) * Math.sin( (t*d-s)*(2*Math.PI)/p ) + c + b;
	    }
	});	
})();