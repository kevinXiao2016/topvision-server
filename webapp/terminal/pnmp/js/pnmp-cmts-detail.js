sm = new Ext.grid.CheckboxSelectionModel({
	listeners : {
        rowselect : function(sm,rowIndex,record){
            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
        },
        rowdeselect : function(sm,rowIndex,record){
            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
        }
    }
});

var cm = new Ext.grid.ColumnModel({
	defaults : {
		menuDisabled : false
	},
	columns : [
		sm,
        {header: '@pnmp.correlationGroup@', width: 100, sortable: true, align: 'center', dataIndex: 'correlationGroup', renderer: renderGroup,doSort: sortCorrelationGroup},
        {header: '@pnmp.severity@', width: 100, sortable: true, align: 'center', dataIndex: 'mtr', renderer: renderSevertity},
		{header: 'CM MAC', width: 100, sortable: true, align: 'center', dataIndex: 'cmMac'},
		{header: '@pnmp.cmcAddress@', width: 100, sortable: true, align: 'center', dataIndex: 'cmAddress'},
		{header: '@pnmp.mtr@(dB)', width: 120, sortable: true, align: 'center', dataIndex: 'mtr', renderer: function(value){return renderUtil(value, 'mtr')}},
//		{header: '@pnmp.faultDistance@(m)', width: 100, sortable: true, align: 'center', dataIndex: 'tdr', renderer: renderValue},
		{header: '@pnmp.upTxPower@(dBmV)', width: 100, sortable: true, align: 'center', dataIndex: 'upTxPower', renderer: function(value){return renderUtil(value, 'upSendPower')}},
		{header: '@pnmp.upSnr@(dB)', width: 100, sortable: true, align: 'center', dataIndex: 'upSnr', renderer: function(value){return renderUtil(value, 'upSnr')}},
		{header: '@pnmp.downRePower@(dBmV)', width: 100, sortable: true, align: 'center', dataIndex: 'downRxPower', renderer: function(value){return renderUtil(value, 'downRePower')}},
		{header: '@pnmp.downSnr@(dB)', width: 100, sortable: true, align: 'center', dataIndex: 'downSnr', renderer: function(value){return renderUtil(value, 'downSnr')}},
		{header: '@pnmp.operation@', width: 120, sortable: false, fixed:true, align: 'center', dataIndex: 'cmAlias', renderer: renderOperation}
	]
});

//var sortInfo = {field: 'collectTime  ', direction: 'ASC'};

var tbar = new Ext.Toolbar({
    items: [
        buildEntityInput(),
        {xtype: 'tbspacer', width: 3},
        {id:'searchBtn', text: '@pnmp.search@', iconCls: 'bmenu_find', handler: serachByMac},
        {id:'compareCmBtn', text:'@pnmp.compare@', iconCls: 'bmenu_compare', handler: compareCm, disabled: true}
    ]
});

Ext.onReady(function(){
	loadTargetThreshold();
	
	buildCorrelationGroup();
	
	// 初始化map
	showMap();
	
	//显示cmtsInfo
	$("#cmtsInfo").text(cmcName + "(" + ipAddress + ")");
	
	store = new Ext.data.JsonStore({
	    url: '/pnmp/cmdata/loadCmtsDataByGroup.tv',
	    root: 'data',
	    idProperty: "cmtsDatas",
	    remoteSort: false,
	    fields: [
	      'cmcId', 'entityIp', 'cmcName', 'cmMac', 'cmAddress', 'collectTime', 'collectTimeString', 'tapCoefficient', 'spectrumResponse', 'correlationGroup'
	      , 'mte', 'preMte', 'postMte', 'tte', 'mtc', 'mtr', 'nmtter', 'premtter', 'postmtter', 'ppesr', 'mrLevel', 'tdr', 'upSnr'
	      , 'upTxPower', 'downSnr', 'downRxPower', 'statusValue','upChannelWidth','upChannelId','upChannelFreq']
	});

//	store.setDefaultSort(sortInfo.field, sortInfo.direction);
	store.baseParams = {
		cmcId: cmcId,
		correlationGroup: $("#correlationGroup").val()
	};
	store.load({
		callback: function(){
			// 故障组频响曲线和抽头系数条形图绘制
			var records = getRecords(store);
			countCmNumByGroup(records);
			generateGroupGraph(records); 
		}
	});
	
	grid = new Ext.grid.EditorGridPanel({
	    cls: 'normalTable',
	    autoHeight : true,
		bodyCssClass : 'normalTable',	
	    region: 'center',
	    border: true,
	    totalProperty: 'rowCount',
	    enableColumnMove: true,
	    store: store,
	    enableColumnMove: true,
	    sm: sm,
	    cm: cm,
	    tbar: tbar,
	    loadMask: true,
	    renderTo: 'cm-group-table-container',
	    viewConfig: {
	      scrollOffset: 0,
	      forceFit: true
	    }
	  });
	
	new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'center',
			border : false,
			contentEl : 'center-container'
		} ]
	});
});

function buildEntityInput() {
    return '<td><input class="normalInput" type="text" style="width: 150px" id="searchCm" maxlength="63"></td>'
}

//通过mac查询
function serachByMac(){
	var cmMac = $("#searchCm").val();
	var correlationGroup = $("#correlationGroup").val();
	var cmcId = cmcId;
	store.baseParams = {
		cmMac: cmMac,
		correlationGroup: correlationGroup,
		cmcId: cmcId
	};
	//在执行完相关操作后去掉grid表头上的复选框选中状态
	store.load({
    	callback: function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
    });
}

//根据选中的行数判断是否将查询收光功率历史记录按钮置灰
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['compareCmBtn'], false);
    }else{
    	disabledBtn(['compareCmBtn'], true);
    }
}

//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
}

//获取选中行的CM
function getSelectedCM() {
	var selectedModel = grid.getSelectionModel();
	if (selectedModel != null && selectedModel.hasSelection()) {
		var record = selectedModel.getSelected();
		return record.data;
	}
	return null;
}
function sortCorrelationGroup(state) {
    var ds = this.up('grid').getStore();
    var field = this.getSortParam();
    ds.sort({
        property: field,
        direction: state,
        //排序规则（重点）
        sorterFn: function(v1, v2){
            v1 = v1.get(field);
            v2 = v2.get(field);
            return v1.length > v2.length ? 1 : (v1.length < v2.length ? -1 : 0);
        }
    });
}
function renderGroup(value, p, record){
    if(value != 0){
        return 'Group-' + value
    }else{
        return '--';
    }
}

function renderSevertity(value, p, record){
    if(value){
        if(value > thresholdMap.mtr.health.lowValue){
            return '<div style="margin: auto" class="green-circle"></div>';
        }else if(value < thresholdMap.mtr.bad.highValue){
            return '<div style="margin: auto" class="red-circle"></div>';
        }else{
            return '<div style="margin: auto" class="yellow-circle"></div>';
        }
    }else{
        return '--';
    }
}

function renderOperation(v) {
	return '<a class="yellowLink" href="javascript:showRealData();" >@pnmp.cmrealdata@</a>/<a class="yellowLink" href="javascript:showHistoryData();" >@pnmp.history@</a>';
}

//绘制故障组频响曲线和抽头系数条形图绘制
function generateGroupGraph(records){
	var maxSpectrumResponsArr = [];//频响曲线最大值数组
	var spectrumResponsOptions = [];//频响曲线绘图用series
	var tapCoefficientOptions = [];//抽头系数绘图用series
	var labelName;
	var labelText = "";
	// 频响曲线副标题，展示上行信道和频宽
    var correlationGroup = $("#correlationGroup").val();
    var subTitle = ''
    if (correlationGroup != null && correlationGroup != -1) {
        subTitle = records.length>0 ? '@pnmp.upChannelId@@COMMON.maohao@' + records[0].data.upChannelId + ',@pnmp.upChannelWidth@@COMMON.maohao@' + records[0].data.upChannelWidth/1000000 + 'Mhz':'';
    }

	$.each(records, function(i, record){
		var spectrumResponsData= [], tapCoefficientData = [];
		
		var spectrumResponse = record.data.spectrumResponse; //单个cm频响曲线 原始值  (1,2,3,4,5,6,7,8,9)
		var tapCoefficient = record.data.tapCoefficient;//单个cm抽头系数 原始值 (-11,-33,-26,-49,-32,-45,-26,-13,-48,-31,-16,-24,-35,)
		
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
		
		labelName = labelText + record.data.cmMac;
		
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
		}) 
	});
	
	//频响曲线纵坐标(用所有频响曲线的最大值最小值)
	var $ordinateMax = getArrAbsMax(maxSpectrumResponsArr, 3.0);
	var $ordinateMin = -$ordinateMax;
	//5代表5个颜色区间
	var $ordinate = formatSpecResOrdinate($ordinateMax, $ordinateMin, 5);
	
	// 频响曲线横坐标
	var $categories = [];
	if(records.length>0){
		var $categoryMin = (-records[0].data.upChannelWidth/2 + records[0].data.upChannelFreq)/1000000;//起始值 -upChannelWidth/2 + upChannelFreq
		var $categoryMax = (records[0].data.upChannelWidth/2 + records[0].data.upChannelFreq)/1000000;
		if(spectrumResponsOptions.length>0){
			var size = spectrumResponsOptions[0].data.length - 1;//横坐标个数
			$categories = formatSpecResCategory($categoryMax, $categoryMin, size);
		}
	}
	
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

// 获取store的所有record
function getRecords(store){
	var records = [];
	if(store){
		if($("#correlationGroup").val() == -1 && store.getCount() > 5){// 如果是全部CM，则默认只选中前5个
			records = store.getRange(0,4);
			grid.getSelectionModel().selectRows(new Array(0,1,2,3,4));
		}else{
			store.each(function(record){
				records.push(record);
			});
		}
	}
	return records;
}

// 选中cm后绘制曲线，在地图上显示等
function compareCm(){
	if(sm){
		if(sm.getSelections().length==0){
	        window.top.showMessageDlg(I18N.COMMON.tip, "@pnmp.selectCM@");
	        return;
	    }
		
		var rs=sm.getSelections();
	    var cmMacs=[];
	    for(var i = 0; i < rs.length; i++){
	    	cmMacs[i]=rs[i].data.cmMac;
		}
	    reShowSiteOnMap();
	    countCmNumByGroup(rs);
	    generateGroupGraph(rs);
	}
}

//重新解析地址到百度地图
function reShowSiteOnMap(){
	//清除上次的覆盖物
	map.clearOverlays();
	//地址重置，下标重置
	adds = [], index = 0;;
	var rs=sm.getSelections();
    for(var i = 0; i < rs.length; i++){
    	adds.push(rs[i].data.cmAddress);
	}
	//解析地址
	bdGEO();
}

// 统计CM健康，劣化数
function countCmNumByGroup(records){
	var totalNum = 0, badNum = 0, margialNum = 0, healthNum = 0, offlineNum = 0;
	if(records){
		totalNum = records.length;
		$.each(records, function(i, record){
			if(record.data.mtr < thresholdMap.mtr.bad.highValue){
				badNum++;
			}else if(record.data.mtr > thresholdMap.mtr.health.lowValue){
				healthNum++;
			}else{
				margialNum++;
			}
		});
	}
	$("#totalNum").text(totalNum);
	$("#badNum").text(badNum);
	$("#margialNum").text(margialNum);
	$("#healthNum").text(healthNum);
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
	        }],
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

//跳转历史记录页面
function showHistoryData(){
    var CM = getSelectedCM();
    if(CM){
        window.top.addView('cmhistorydata-'+CM.cmMac, CM.cmMac + '-@pnmp.cmhistorydata@','icoI3', '/pnmp/cmdata/showHistoryData.tv?cmMac='+CM.cmMac,null,true);
    }
}

function showRealData(){
    var CM = getSelectedCM();
    if(CM){
        window.top.addView('cmrealdata-'+CM.cmMac, CM.cmMac + '-@pnmp.cmrealdata@','icoI3', '/pnmp/cmdata/showRealData.tv?cmMac='+CM.cmMac+'&cmcId='+cmcId,null,true);
    }
}