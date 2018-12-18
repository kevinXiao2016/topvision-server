Ext.onReady(function(){
    reload();
    new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'center',
			border : false,
			contentEl : 'center-container'
		} ]
	});
});

function reload() {
    window.top.showWaitingDlg("@pnmp.tip.waiting@", "@pnmp.tip.loading@", 'ext-mb-waiting');
    $.ajax({
        url: "/pnmp/cmdata/loadRealData.tv?cmcId=" + cmcId + "&cmMac=" + cmMac,
        type: "post",
        dataType: "json",
        success:function(json){
            renewValue(json);
            generateGroupGraph(json);
            window.parent.closeWaitingDlg();
        },
        error:function(){
            window.parent.closeWaitingDlg();
            window.parent.showMessageDlg("@pnmp.tip.tipMsg@",  "@pnmp.tip.loadingFail@");
        },
        cache:false
    });
}

function renewValue(realData){
    $("#cmMac").text(realData.cmMac ? realData.cmMac : "--");
    $("#mtr").text(realData.mtr ? Number(realData.mtr).toFixed(1) : "--");
    $("#upChannelFreq").text(realData.upChannelFreq ? realData.upChannelFreq : "--");
    $("#upChannelId").text(realData.upChannelId ? realData.upChannelId : "--");
    $("#upChannelWidth").text(realData.upChannelWidth ? realData.upChannelWidth : "--");
    $("#upTxPower").text(realData.upTxPower ? realData.upTxPower : "--");
    $("#upSnr").text(realData.upSnr ? realData.upSnr : "--");
    $("#mtrVariance").text(realData.mtrVariance ? Number(realData.mtrVariance).toFixed(1) : "--");
    $("#upSnrVariance").text(realData.upSnrVariance ? Number(realData.upSnrVariance).toFixed(1) : "--");
    $("#mtrToUpSnrSimilarity").text(realData.mtrToUpSnrSimilarity ? Number(realData.mtrToUpSnrSimilarity).toFixed(1) : "--");
}

//绘制故障组频响曲线和抽头系数条形图绘制
function generateGroupGraph(realData){
	var maxSpectrumResponsArr = [];//频响曲线最大值数组
	var spectrumResponsOptions = [];//频响曲线绘图用series
	var tapCoefficientOptions = [];//抽头系数绘图用series
	var labelName;
	var labelText = "";
	// 频响曲线副标题，展示上行信道和频宽
    var subTitle = '@pnmp.upChannelId@@COMMON.maohao@' + realData.upChannelId + ',@pnmp.upChannelWidth@@COMMON.maohao@' + realData.upChannelWidth/1000000 + 'Mhz';
    var spectrumResponsData= [], tapCoefficientData = [];
		
    var spectrumResponse = realData.spectrumResponse; //单个cm频响曲线 原始值  (1,2,3,4,5,6,7,8,9)
    var tapCoefficient = realData.tapCoefficient;//单个cm抽头系数 原始值 (-11,-33,-26,-49,-32,-45,-26,-13,-48,-31,-16,-24,-35,)
		
    // 解析数组作频响曲线 highchart series
    if(spectrumResponse){
        $.each(spectrumResponse.split(","), function(j, value){
            spectrumResponsData.push(parseFloat(value));
        });
    }
		
    //将每条频响曲线的绝对值的最大值放入一个数组
    maxSpectrumResponsArr.push(getArrAbsMax(spectrumResponsData, 3.0));

    // 构造二维数组作抽头系数highchart series
    if(tapCoefficient){
        $.each(tapCoefficient.split(","), function(k, val){
            var tapCoefficientArr = [];
            tapCoefficientArr.push(-60);
            tapCoefficientArr.push(parseFloat(val));
            tapCoefficientData.push(tapCoefficientArr);
        });
    }
		
    labelName = labelText + realData.cmMac;

    //构造频响曲线series
    spectrumResponsOptions.push({
        name: labelName,
        type: 'spline',
        data: spectrumResponsData
    });
    //构造抽头系数series
    tapCoefficientOptions.push({
        name: labelName,
        data: tapCoefficientData
    });
	
	//频响曲线纵坐标(用所有频响曲线的最大值最小值)
	var $ordinateMax = getArrAbsMax(maxSpectrumResponsArr, 3.0);
	var $ordinateMin = -$ordinateMax;
	//5代表5个颜色区间
	var $ordinate = formatSpecResOrdinate($ordinateMax, $ordinateMin, 5);
	
	// 频响曲线横坐标
	var $categories = [];
    var $categoryMin = (-realData.upChannelWidth/2 + realData.upChannelFreq)/1000000;//起始值 -upChannelWidth/2 + upChannelFreq
    var $categoryMax = (realData.upChannelWidth/2 + realData.upChannelFreq)/1000000;
    var size = spectrumResponsOptions[0].data.length - 1;//横坐标个数
    $categories = formatSpecResCategory($categoryMax, $categoryMin, size);
	
	//绘制频响曲线数据
	generateSpectrumResponseGraph(spectrumResponsOptions, subTitle, $categories, $ordinate, 'dB');
	//绘制抽头系数条形图
	generateTapCoefficientGraph(tapCoefficientOptions, 'dB');
}

/**
 * 频响曲线纵坐标
 * @param $ordinateMax 纵坐标最大值
 * @param $ordinateMin 纵坐标最小值
 * @param size 颜色区间的个色
 * @returns {___anonymous8392_8393}
 */
function formatSpecResOrdinate($ordinateMax, $ordinateMin, size){
	var $ordinate = {};//纵坐标
	var colors = ['#ffe3e5', '#fff8e5', '#d7feea', '#fff8e5','#ffe3e5'];
	var range = [$ordinateMin, -2.0, -1.0, 1.0, 2.0, $ordinateMax];
	var plotBands = [];
	for(var i=0; i< size; i++){
		plotBands[i] = 	{
			from : range[i],
			to: range[i+1],
			color: colors[i]
		}
	}
	$ordinate.min = $ordinateMin;
	$ordinate.max = $ordinateMax;
	$ordinate.plots = plotBands;
	return $ordinate;
}

/**
 * 频响曲线横坐标
 * @param $categoryMin
 * @param $categoryMax
 * @param size
 * @returns {Array}
 */
function formatSpecResCategory($categoryMax, $categoryMin, size){
	var interval = ($categoryMax - $categoryMin)/size;
	var $categories = [];
    for(var i=0; i<=(size + 1) / 2; i++){
        $categories[i] = (($categoryMin + interval*i).toFixed(1));
    }
    for(var i=0; i<(size + 1) / 2; i++){
        $categories[size - i] = (($categoryMax - interval*i).toFixed(1));
    }
	return $categories;
}

// 频响曲线折线图绘制
function generateSpectrumResponseGraph(seriesOptions, subTitle, $categories, $ordinate, unit){
	var noData = [{
            type: 'spline',
            data: [[0, 100]]
        }];
	$('#spectrum-respond-chart-container').highcharts({
	    chart: {
	        zoomType: 'x',
	        height: $('#spectrum-respond-chart-container').height(),
	        width: $('#spectrum-respond-chart-container').width()
	    },
	    title: {
	    	text: '@pnmp.cableModemResponse@'
	    },
	    subtitle: {
            text: subTitle
        },
	    xAxis : {
        	gridLineWidth: 1,
        	categories: $categories
		},
	    yAxis: [{ 
	        labels: {
	            formatter: function() {
	                return this.value.toFixed(1) + unit;
	            }
	        },
	        title: {
	            text: '@pnmp.collectValue@(dB)'
	        },
	        opposite: false,
	        min: $ordinate.min,
	        max: $ordinate.max,
	        tickInterval: 0.1,
	        plotBands: $ordinate.plots
	    }],
	    plotOptions: {
	    	spline: {
	    		marker: {
	    			enabled: false
	    		}
	    	}
	    },
	    tooltip: {
	    	crosshairs: true,
	        shared: true,
	        xDateFormat: '%Y-%m-%d %H:%M:%S'
	    },
	    legend: {
	    	enabled:false
	    },
	    credits: {
	        enabled:false
	    },
	    tooltip: {
       	 headerFormat: '<span style="font-size:10px">@pnmp.width@@COMMON.maohao@{point.key}Mhz</span><table>',
            pointFormat: '<tr><td>{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.3f} dB</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
	    },
	    series: seriesOptions.length != 0 ? seriesOptions : noData
	});
}

// 抽头系数条形图绘制
function generateTapCoefficientGraph(seriesOptions, unit){
	var noData = [{
        data: [[-100, -100]]
    }];
	$('#tap-coefficients-chart-container').highcharts({
        chart: {
        	type: 'columnrange',
            height: $('#tap-coefficients-chart-container').height(),
            width: $('#tap-coefficients-chart-container').width()
        },
        title: {
        	text: '@pnmp.coefficients@'
        },
        xAxis : {
        	gridLineWidth: 1,
        	categories: [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24]
		},
        yAxis: {
            title: {
                text: '@pnmp.collectValue@(dB)'
            },
            labels: {
                formatter: function() {
                    return this.value + unit;
                }
            },
			min: -60,
			floor: -60,
			max: 0,
			tickInterval: 10,
			plotLines: [{
	        	value: -30,
	        	color: 'red',
	        	width: 2
	        }]
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
        	 headerFormat: '<span style="font-size:10px">@pnmp.tap@@COMMON.maohao@{point.key}</span><table>',
             pointFormat: '<tr><td>{series.name}: </td>' +
             '<td style="padding:0"><b>{point.high:.3f} dB</b></td></tr>',
             footerFormat: '</table>',
             shared: true,
             useHTML: true
 	    },
        series: seriesOptions.length != 0 ? seriesOptions : noData
    });
}