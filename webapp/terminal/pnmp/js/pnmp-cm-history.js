Highcharts.setOptions({
	global: {
		useUTC: false
	}
});

function initTab(){
	var data = [{
	    name: '@pnmp.today@',
	    value: '1'
	},{
	    name: '@pnmp.lastThreeDays@',
	    value: '3'
	},{
	    name: '@pnmp.lastSevenDays@',
	    value: '7'
	},{
	    name: '@pnmp.lastThirtyDays@',
	    value: '30'
	}];
	var tab = new SegmentButton('putTab', data, {
	    callback: function(value){
	    	loadDaysData(value);
	    }
	});
	tab.init();
}

/*function loadTargetThreshold() {
	$.get('/pnmp/target/loadPnmpTargetConfig.tv', function(targetList) {
		for(var i=0, len=targetList.length; i<len; i++) {
			var threshold = targetList[i];
			if(thresholdMap[threshold.targetName] == null) {
				thresholdMap[threshold.targetName] = {};
			}
			thresholdMap[threshold.targetName][threshold.thresholdName] = {
				lowValue: threshold.lowValue,
				highValue: threshold.highValue
			};
		}
	});
	$.get('/pnmp/cmtarget/loadCmSignalTargetThresholds.tv', function(targetList) {
		for(var i=0, len=targetList.length; i<len; i++) {
			var threshold = targetList[i];
			if(cmSignalThresholdMap[threshold.targetName] == null) {
				cmSignalThresholdMap[threshold.targetName] = {};
			}
			cmSignalThresholdMap[threshold.targetName][threshold.thresholdName] = {
				lowValue: threshold.lowValue,
				highValue: threshold.highValue
			};
		}
	});
}*/

function loadDaysData(day){
	var startTime;
	var endTime = new Date();
	endTime.setHours(23);
	endTime.setMinutes(59);
	endTime.setSeconds(59);
	switch(day){
		case '1' :
			startTime = new Date();
			startTime.setHours(0);
			startTime.setMinutes(0);
			startTime.setSeconds(0);
			break;
		case '3' :
			startTime = new Date(endTime-3*24*60*60*1000 + 1000);
			break;
		case '7' :
			startTime = new Date(endTime-7*24*60*60*1000 + 1000);
			break;
		case '30' :
			startTime = new Date(endTime-30*24*60*60*1000 + 1000);
			break;
		default:
			startTime = new Date(endTime-3*24*60*60*1000 + 1000);
	}
	
	$.ajax({
		url : '/pnmp/cmdata/loadHistoryData.tv',
		type : 'POST',
		data : {
			cmMac : cmMac,
			startTime : Ext.util.Format.date(startTime, 'Y-m-d H:i:s'),
			endTime : Ext.util.Format.date(endTime, 'Y-m-d H:i:s')
		},
		dataType : 'json',
		success : function(json) {
			//格式：data = [[11,2]];
			var mtrData = [];
			var nmtterData = [];
			var premtterData = [];
			var postmtterData = [];
			var ppesrData = [];
			var mrLevelData = [];
			var downRxPowerData = [];
			var downSnrData = [];
			var upSnrData = [];
			var upTxPowerData = [];
			$.each(json.data, function(i, v){
				var mtrTemp = [];
				var nmtterTemp = [];
				var premtterTemp = [];
				var postmtterTemp = [];
				var ppesrTemp = [];
				var mrLevelTemp = [];
				var downRxPowerTemp = [];
				var downSnrTemp = [];
				var upSnrTemp = [];
				var upTxPowerTemp = [];
				mtrTemp.push(v['collectTime']['time']);
				mtrTemp.push(v['mtr']);
				
				nmtterTemp.push(v['collectTime']['time']);
				nmtterTemp.push(v['nmtter']);
				
				premtterTemp.push(v['collectTime']['time']);
				premtterTemp.push(v['premtter']);
				
				postmtterTemp.push(v['collectTime']['time']);
				postmtterTemp.push(v['postmtter']);
				
				ppesrTemp.push(v['collectTime']['time']);
				ppesrTemp.push(v['ppesr']);
				
				mrLevelTemp.push(v['collectTime']['time']);
				mrLevelTemp.push(v['mrLevel']);
				
				downRxPowerTemp.push(v['collectTime']['time']);
				downRxPowerTemp.push(v['downRxPower']);
				
				downSnrTemp.push(v['collectTime']['time']);
				downSnrTemp.push(v['downSnr']);
				
				upSnrTemp.push(v['collectTime']['time']);
				upSnrTemp.push(v['upSnr']);
				
				upTxPowerTemp.push(v['collectTime']['time']);
				upTxPowerTemp.push(v['upTxPower']);
				
				mtrData.push(mtrTemp);
				nmtterData.push(nmtterTemp);
				premtterData.push(premtterTemp);
				postmtterData.push(postmtterTemp);
				ppesrData.push(ppesrTemp);
				mrLevelData.push(mrLevelTemp);
				downRxPowerData.push(downRxPowerTemp);
				downSnrData.push(downSnrTemp);
				upSnrData.push(upSnrTemp);
				upTxPowerData.push(upTxPowerTemp);
			});
			createMtr(mtrData);
			createNMTER(nmtterData);
			createPreMTTER(premtterData);
			createPostMTTER(postmtterData);
			createPPESR(ppesrData);
			createMRLevel(mrLevelData);
			createUpTx(upTxPowerData);
			createUpSnr(upSnrData);
			createDownTx(downRxPowerData);
			createDownSnr(downSnrData);
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@pnmp.loadDataFail@");
		},
		cache : false
	});
}

function createMtr(data) {
	var yMin = 0;
	
	var yPlotBands = [];
	try{
		yPlotBands =  [ {
            from: thresholdMap.mtr.health.lowValue0,
            to: 1000,
            color: healthColor
        },{
            from: thresholdMap.mtr.marginal.lowValue,
            to: thresholdMap.mtr.marginal.highValue,
            color: marginalColor
        },{
            from: 0,
            to: thresholdMap.mtr.bad.highValue,
            color: badColor
        }];
	}catch(e) {
		
	}
	
	createChart('mtr-chart-container', '@pnmp.mtr@', data,'dB','mtr', {
		subTitle: '(MTR)',
		yMin: yMin,
		yPlotBands: yPlotBands,
        unit: 'dB'
	});
}

function createNMTER(data) {
	var yPlotBands = [];
	try{
		yPlotBands =  [{
        	from: -1000,
        	to: thresholdMap.nmtter.health.highValue,
        	color: healthColor
        }, {
        	from: thresholdMap.nmtter.marginal.lowValue,
        	to: thresholdMap.nmtter.marginal.highValue,
        	color: marginalColor
        }, {
        	from: thresholdMap.nmtter.bad.lowValue,
        	to: 0,
        	color: badColor
        }];
	}catch(e) {
		
	}
	
	createChart('nmter-chart-container', '@pnmp.nmter@', data,'dB','nmtter', {
		subTitle: '(NMTTER, Not Main Tap to Total Energy Ratio)',
		yMax: 0,
		yPlotBands: yPlotBands
	});
}

function createPreMTTER(data) {
	var yPlotBands = [];
	try{
		yPlotBands =  [{
         	from: -1000,
         	to: thresholdMap.premtter.health.highValue,
         	color: healthColor
         }, {
         	from: thresholdMap.premtter.marginal.lowValue,
         	to: thresholdMap.premtter.marginal.highValue,
         	color: marginalColor
         }, {
         	from: thresholdMap.premtter.bad.lowValue,
         	to: 0,
         	color: badColor
         }];
	}catch(e) {
		
	}
	
 	createChart('preMTTER-chart-container', '@pnmp.premtter@', data,'dB','premtter', {
 		subTitle: '(PreMTTER, Pre- Main Tap to Total Energy Ratio)',
 		yMax: 0,
 		yPlotBands: yPlotBands
 	});
}

function createPostMTTER(data) {
	var yPlotBands = [];
	try{
		yPlotBands =  [{
        	from: -1000,
        	to: thresholdMap.postmtter.health.highValue,
        	color: healthColor
        }, {
        	from: thresholdMap.postmtter.marginal.lowValue,
         	to: thresholdMap.postmtter.marginal.highValue,
        	color: marginalColor
        }, {
        	from: thresholdMap.postmtter.bad.lowValue,
        	to: 0,
        	color: badColor
        }];
	}catch(e) {
		
	}
	
	createChart('postMTTER-chart-container', '@pnmp.postmtter@', data, 'dB','postMtter', {
		subTitle: '(PostMTTER, Post- Main Tap to Total Energy Ratio)',
		yMax: 0,
		yPlotBands: yPlotBands
	});
}

function createPPESR(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
        	from: -1000,
        	to: thresholdMap.ppesr.mrlevelBad.highValue,
        	color: badColor
        }, {
        	from: thresholdMap.ppesr.mrlevelMarginal.lowValue,
        	to: thresholdMap.ppesr.mrlevelMarginal.highValue,
        	color: marginalColor
        }, {
        	from: thresholdMap.ppesr.health.lowValue,
        	to: thresholdMap.ppesr.health.highValue,
        	color: healthColor
        }, {
        	from: thresholdMap.ppesr.delayMarginal.lowValue,
        	to: thresholdMap.ppesr.delayMarginal.highValue,
        	color: marginalColor
        }, {
        	from: thresholdMap.ppesr.delayBad.lowValue,
        	to: 1000,
        	color: badColor
        }];
	}catch(e) {
		
	}
	
	createChart('PPESR-chart-container', '@pnmp.ppesr@', data,'dB','ppesr', {
		subTitle: '(PPESR, Pre-Post Energy Symmetry Ratio)',
		yPlotBands: yPlotBands
	});
}

function createMRLevel(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
	       	from: -1000,
	       	to: thresholdMap.mrlevel.health.highValue,
	       	color: healthColor
	       }, {
	       	from: thresholdMap.mrlevel.marginal.lowValue,
	       	to: thresholdMap.mrlevel.marginal.highValue,
	       	color: marginalColor
	       }, {
	       	from: thresholdMap.mrlevel.bad.lowValue,
	       	to: 10000,
	       	color: badColor
       }];
	}catch(e) {
		
	}
	
   	createChart('mrLevel-chart-container', '@pnmp.mrLevel@', data,'dB','mrLevel', {
   		yPlotBands: yPlotBands
   	});
}

function createUpTx(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
           	from: -1000,
           	to: cmSignalThresholdMap.upSendPower.tooLow.highValue,
           	color: badColor
           }, {
           	from: cmSignalThresholdMap.upSendPower.health.lowValue,
           	to: cmSignalThresholdMap.upSendPower.health.highValue,
           	color: healthColor
           }, {
           	from: cmSignalThresholdMap.upSendPower.tooHigh.lowValue,
           	to: 10000,
           	color: badColor
       }];
	}catch(e) {
		
	}
	
   	createChart('upTx-chart-container', '@pnmp.upTxPower@', data,'dBmV','downSnr', {
   		yPlotBands: yPlotBands
   	});
}

function createUpSnr(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
           	from: -1000,
           	to: cmSignalThresholdMap.upSnr.bad.highValue,
           	color: badColor
           }, {
           	from: cmSignalThresholdMap.upSnr.health.lowValue,
           	to: 10000,
           	color: healthColor
       }];
	}catch(e) {
		
	}
	
   	createChart('upSnr-chart-container', '@pnmp.upSnr@', data, 'dB','upSnr',{
   		yPlotBands: yPlotBands
   	});
}

function createDownTx(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
           	from: -1000,
           	to: cmSignalThresholdMap.downRePower.tooLow.highValue,
           	color: badColor
           }, {
           	from: cmSignalThresholdMap.downRePower.health.lowValue,
           	to: cmSignalThresholdMap.downRePower.health.highValue,
           	color: healthColor
           }, {
           	from: cmSignalThresholdMap.downRePower.tooHigh.lowValue,
           	to: 10000,
           	color: badColor
       }];
	}catch(e) {
		
	}
	
   	createChart('downTx-chart-container', '@pnmp.downRePower@', data,'dBmV','@pnmp.downRePower@', {
   		yPlotBands: yPlotBands
   	});
}

function createDownSnr(data) {
	var yPlotBands = [];
	try{
		yPlotBands = [{
   			from: -1000,
           	to: cmSignalThresholdMap.downSnr.bad.highValue,
           	color: badColor
           }, {
           	from: cmSignalThresholdMap.downSnr.health.lowValue,
           	to: 10000,
           	color: healthColor
        }];
	}catch(e) {
		
	}
	
   	createChart('downSnr-chart-container', '@pnmp.downSnr@', data,'dB','@pnmp.downSnr@', {
   		yMin: 0,
   		yPlotBands: yPlotBands
   	});
}

function createChart(containerId, text, data,unit,targetName, options) {
	$('#' + containerId).highcharts({
        chart: {
            type: 'spline',
            height: 300,
            width: $('#chart-container').width()
        },
        title: {
        	text: text
        },
        subtitle: {
        	text: options.subTitle
        },
        xAxis : {
        	type: 'datetime',
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
			minRange: 60 * 1000
		},
        yAxis: {
            title: {
                text: '@pnmp.collectValue@('+ unit +')'
            },
            labels: {
                formatter: function() {
                    return this.value + (unit || 'dB');
                }
            },
			min: options.yMin,
			max: options.yMax,
			plotBands: options.yPlotBands
        },
        plotOptions: {
        	columnrange: {
        		dataLabels: {
                    enabled: false
                }
        	}
        },
        legend: {
        	enabled:false
        },
        credits: {
            enabled:false
        },
        tooltip: {
            formatter: function() {
            	if($.inArray(targetName, ["mtc", "mrLevel", "postMtter","nmtter","ppesr","premtter"])!=-1){
            		return '<b>' + text + ":" + this.y.toFixed(3) + ' ' + unit + '</b><br/><b>@COMMON.time@:'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b>';
            	}else{
            		return '<b>' + text + ":" + this.y.toFixed(1) + ' ' + unit + '</b><br/><b>@COMMON.time@:'+ Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'</b>';
            	}
 			}
 	    },
        series: [{
            name: text,
            data: data
        }]
    });
}
