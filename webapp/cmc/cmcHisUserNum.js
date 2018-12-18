var chartW;
var FLOW_K10 = 1000,
FLOW_M10 = FLOW_K10 * 1000,
TOOLTIP_TPL = '<span class="tooltipSpan" style="color:{0}">{1}:</span><span class="tooltipSpan" style="color:{0}">{2}{3}</span><br/>';
$(function() {
	chartW = $(window).outerWidth() - 102;

})

function getLegendWidth(n){
	if("@zhongwen@"=="@languageJudge@"){
		if(n<2){
			return 80;
		}else if(n<4){
			return 160;
		}else if(n<6){
			return 240;
		}else if(n<8){
			return 320;
		}else if(n<10){
			return 380;
		}else{
			return 380;
		}
	}else{
		if(n<2){
			return 250;
		}else {
			return 450;
		}
	}	
}
function getLegendInteval(){
	if("@zhongwen@"=="@languageJudge@"){
		return 85;
	}else{
		return 225;
	}	
}
function showHistoryPerf(seriesOptions, divId, title, yText, valueDecimals, seriesUnit, startTime, endTime){
	/**
     * 
     * 为了让navigator能够与x轴一直，需要加两个空的点来标识开始结束时间,需要判断是否存在，免得开始和结束有两个点
     */
    //考虑到查询到没有数据的情况,加上相应判断
    if(seriesOptions  && seriesOptions[0] && seriesOptions[0].data.length>0){
        if(seriesOptions[0].data[0].x>Date.parse(startTime.replace('-','/'))){
            seriesOptions[0].data = [{
            	x: Date.parse(startTime.replace('-','/')),
            	y: null
            }].concat(seriesOptions[0].data);
        }
        if(seriesOptions[0].data[seriesOptions[0].data.length-1].x<Date.parse(endTime.replace('-','/'))){
            seriesOptions[0].data = seriesOptions[0].data.concat([{
            	x: Date.parse(endTime.replace('-','/')),
            	y: null
            }]);
        }
    } else {
        seriesOptions[0].data[0] = {
            	x: Date.parse(startTime.replace('-','/')),
            	y: null
            };
        seriesOptions[0].data[1] = {
            	x: Date.parse(endTime.replace('-','/')),
            	y: null
            };
        //seriesOptions[0].data[2] = [Date.parse(endTime.replace('-','/')),null];
    }
    
	var orgHighchartsRangeSelectorPrototypeRender = Highcharts.RangeSelector.prototype.render;
	Highcharts.RangeSelector.prototype.render = function(min, max) {
		orgHighchartsRangeSelectorPrototypeRender.apply(this, [ min, max ]);
		var leftPosition = this.chart.plotLeft + chartW, topPosition = this.chart.plotTop - 30, space = 2;
		this.zoomText.attr({
			x : leftPosition,
			y : topPosition 
		});
		leftPosition += this.zoomText.getBBox().width;
		for (var i = 0; i < this.buttons.length; i++) {
			this.buttons[i].attr({
				x : leftPosition - 100,
				y : topPosition
			});
			leftPosition += this.buttons[i].width + space;
			//alert(this.buttons[i].width)
		}
	};
			buttons = []
        	
        	Highcharts.setOptions({ 
        		global: { useUTC: false } 
        	}); 
        	var dateRegion = startTime + '~' + endTime;
        	new Highcharts.StockChart({
        		chart: {
                    renderTo: divId,
                    type : 'spline',
        			zoomType: 'x',
        			width : chartW,
        			height : $(window).height()-180
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
        		   
                title: {
                    text: title
                },
                subtitle : {
                	text : '@performance/Tip.trendSubTitle@'
                },
                
        	    legend: {
        	    	enabled: true,
        	    	floating: true,
        	    	align: 'left',
        	    	width: getLegendWidth(seriesOptions.length),
        	    	//maxHeight: 55,
        	    	itemWidth: getLegendInteval(),
        	    	x:27,
                	backgroundColor: '#FCFFC5',
                	borderColor: '#C0C0C0',
                	borderWidth: 1,
        	    	layout: 'horizontal',
        	    	verticalAlign: 'top',
        	    	shadow: true
        	    },
        		
                yAxis: {
                	title: {
                        text: yText
                    },
        	    	labels: {
        	    		formatter: function() {
        	    			if(divId == "flowHisPerf"){
        	    				return this.value / FLOW_M10 + seriesUnit;
        	    			}else{
        	    				return this.value + seriesUnit;
        	    			}
        	    		}
        	    	},
        	    	plotLines: [{
        	    		value: 0,
        	    		width: 2,
        	    		color: 'silver'
        	    	}],
        	    	min: 0
        	    },
        	    
        	    tooltip: {
        	    	formatter: function() {
        				var s = '<b>'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b><br/>';
        				$.each(this.points, function(i, point) {
        					var y
        					if(point.y == -1){
        						s += '<span class="tooltipSpan" style="color:'+  point.series.color + '">' + point.series.name + I18N.CMCPE.failedGetDataTip + '</span><br/>';
        					}else if (divId == "flowHisPerf"){
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
    							s +=  String.format(TOOLTIP_TPL,point.series.color, point.series.name, $y, $unit);	
        					}else{
        						y = point.y.toFixed(valueDecimals);
        						s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
        						  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + y + seriesUnit + '</span><br/>';
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
                series : seriesOptions
        		})
        }

function refresh(){
	window.location.reload();;
}

function query(){
	queryCmHisUserPerf();
	var cpuDisplay = document.getElementById("cpuHisPerf").style.display;
	var snrDisplay = document.getElementById("snrHisPerf").style.display;
	var flowDisplay = document.getElementById("flowHisPerf").style.display;
	if(cpuDisplay == 'block'){
		showCpuHisPerf()
	}
	if(snrDisplay == 'block'){
		showSnrHisPerf()
	}
	if(flowDisplay == 'block'){
		showFlowHisPerf()
	}
}

function showCpuHisPerf(){
    var entityId = parseInt($('#select_olt').val());
    var cmcId = parseInt($('#select_cmts').val());
    var cpuDisplay = document.getElementById("cpuHisPerf").style.display;
    if(cpuDisplay == 'block'){
    	$("#cpuHisPerf").css("display","none");
    	return;
	}
	$.ajax({
		url : '/cmCpe/showCpuHisPerf.tv',cache:false,dataType:'json',
		data:{	
			//regionId : regionId,
			startTime :  startTime.value.format("yyyy-mm-dd HH:MM:ss"),
			entityId : entityId,
            cmcId : cmcId,
			endTime : endTime.value.format("yyyy-mm-dd HH:MM:ss")
			//status : status
			},
		success:function(json){				
			document.getElementById("cpuHisPerf").style.display='block';
			var seriesOptions = [];
        	seriesOptions[0] = {
        				name:  '@CCMTS.view.cpu.name@',
        				data: json.cpuHisPerf
        			};
			showHistoryPerf(seriesOptions, "cpuHisPerf", '@CMCPE.ccCpuHisPerf@', "CPU " + '@CCMTS.view.cpu.ytitle@', 0 ,"%", startTime.value.format("yyyy-mm-dd HH:MM:ss"), endTime.value.format("yyyy-mm-dd HH:MM:ss"));
		},error:function(){
		}
	});
}
function showSnrHisPerf(){
    var entityId = parseInt($('#select_olt').val());
    var entityType = parseInt($('#select_deviceType').val());
    var cmcId = parseInt($('#select_cmts').val());
    var channelIndex = parseInt($('#select_Cnl').val());
    var snrDisplay = document.getElementById("snrHisPerf").style.display;
    if(snrDisplay == 'block'){
    	$("#snrHisPerf").css("display","none");
    	return;
	}
	$.ajax({
		url : '/cmCpe/showSnrHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime : startTime.value.format("yyyy-mm-dd HH:MM:ss"),
			entityId : entityId,
            cmcId : cmcId,
            entityType : entityType,
            channelIndex : channelIndex,
			endTime : endTime.value.format("yyyy-mm-dd HH:MM:ss")
			},
		success:function(json){				
			document.getElementById("snrHisPerf").style.display='block';
			var seriesOptions = [];
        	seriesOptions[0] = {
        				name:  '@CHANNEL.snr@',
        				data: json.snrHisPerf
        			};
			showHistoryPerf(seriesOptions, "snrHisPerf", '@CMCPE.channelSnrHisPerf@', '@CCMTS.view.noice.ytitle@', 1, "dB", startTime.value.format("yyyy-mm-dd HH:MM:ss"),endTime.value.format("yyyy-mm-dd HH:MM:ss"));
		},error:function(){
		}
	});
}
function showFlowHisPerf(){
    var entityId = parseInt($('#select_olt').val());
    var entityType = parseInt($('#select_deviceType').val());
    var cmcId = parseInt($('#select_cmts').val());
    var channelIndex = parseInt($('#select_Cnl').val());
    var flowDisplay = document.getElementById("flowHisPerf").style.display;
	if(flowDisplay == 'block'){
		$("#flowHisPerf").css("display","none");
    	return;
	}
	$.ajax({
		url : '/cmCpe/showFlowHisPerf.tv',cache:false,dataType:'json',
		data:{	
			startTime :  startTime.value.format("yyyy-mm-dd HH:MM:ss"),
			entityId : entityId,
            cmcId : cmcId,
            entityType : entityType,
            channelIndex : channelIndex,
			endTime : endTime.value.format("yyyy-mm-dd HH:MM:ss")
			},
		success:function(json){	
			document.getElementById("flowHisPerf").style.display='block';
			var seriesOptions = [];
        	seriesOptions[0] = {
        				name:  '@CMCPE.channelFlow@',
        				data: json.flowHisPerf
        			};
			showHistoryPerf(seriesOptions, "flowHisPerf", '@CMCPE.channelFlowHisPerf@', '@CMCPE.channelFlow@' + "(Mbps)", 2, "Mbps", startTime.value.format("yyyy-mm-dd HH:MM:ss"),endTime.value.format("yyyy-mm-dd HH:MM:ss"));
		},error:function(){
		}
	});
}

function showUserNumHistory(data, startTime, endTime){
	var seriesOptions = [];
	var i = 0;
	if(selectedQuots.contains('onlineQuota')){
		seriesOptions[i] = {
				key: 'onlineNum',
				name: '@CMCPE.CMONLINENUM@',
				data: data.onlineNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('offlineQuota')){
		seriesOptions[i] = {
				key: 'offlineNum',
				name: '@CMCPE.CMOFFLINENUM@',
				data: data.offlineNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('interactiveQuota')){
		seriesOptions[i] = {
				key: 'interactiveNum',
				name: '@CMCPE.CMInteractiveOnlineNum@',
				data: data.interactiveNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('broadbandQuota')){
		seriesOptions[i] = {
				key: 'broadbandNum',
				name: '@CMCPE.CMBroadbandOnlineNum@',
				data: data.broadbandNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('mtaQuota')){
		seriesOptions[i] = {
				key: 'mtaNum',
				name:  '@CMCPE.CMMTANum@',
				data: data.mtaNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('integratedQuota')){
		seriesOptions[i] = {
				key: 'integratedNum',
				name:  '@CMCPE.CMIntegratedMachineNum@',
				data: data.integratedNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('otherQuota')){
		seriesOptions[i] = {
				key: 'otherNum',
				name:  '@CMCPE.CMOtherNum@',
				data: data.otherNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	} 
	if(selectedQuots.contains('cpeInteractiveQuota')){
		seriesOptions[i] = {
				key: 'cpeInteractiveNum',
				name:  '@CMCPE.CPEInteractiveQNum@',
				data: data.cpeInteractiveNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};  
		i += 1; 
	}
	if(selectedQuots.contains('cpeBroadbandQuota')){
		seriesOptions[i] = {
				key: 'cpeBroadbandNum',
				name:  '@CMCPE.CPEBroadbandNum@',
				data: data.cpeBroadbandNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	if(selectedQuots.contains('cpeMtaQuota')){
		seriesOptions[i] = {
				key: 'cpeMtaNum',
				name:  '@CMCPE.CPEMtaNum@',
				data: data.cpeMtaNum || [],
				dataGrouping: {
	   						enabled: false
	   					}
			};
		i += 1;
	}
	showHistoryPerf(seriesOptions, "container", '@CMCPE.CCMTSUsersHistoryGraph@', '@CMCPE.userNum@', 0, "", startTime, endTime)
}

function GetDateStr(AddDayCount) {  
    var dd = new Date();
    dd.setDate(dd.getDate() + AddDayCount);
    var r = [];
    r.push(dd.getFullYear());
    r.push('-');
    r.push(dd.getMonth() + 1);
    r.push('-');
    r.push(dd.getDate());
    r.push(' ');
    r.push(dd.getHours() < 10 ? '0' + dd.getHours() : dd.getHours());
    r.push(':');
    r.push(dd.getMinutes() < 10 ? '0' + dd.getMinutes() : dd.getMinutes());
    r.push(':');
    r.push(dd.getSeconds() < 10 ? '0' + dd.getSeconds() : dd.getSeconds());
    return r.join('');
}
