//*****执行器******//
var executor = {}

function renderColumn(value){
	if(value == -1){
		return "<div style='color:red'>" + I18N.PERF.mo.notCollected + "</div>"
	}else{
		if(["InOctets","OutOctets"].indexOf(monitor.index) != -1){
			return DateUtil.getFlowDisplayRest(value,monitor.type);
		}
		return value
	}
}

/**
 * 创建动态表单(实时性能)
 */
executor.createDynamicColumn = function(){
	var columns = [] , fields = [] , size = 1 , length = document.body.offsetWidth - 20
	for( var i =0 ; i<monitor.monitorPorts.length;i++){
		var o = monitor.monitorPorts[i].split("-")
		size += o.length - 2
	}
	var makeHeader = function(record){
		var head = toMark(record.portIndex)+"["+record.entityName+"]";
		var dataIndex = toMark(record.portIndex) + "@" + record.entityId;
		fields.push(dataIndex);
		return {header:head , dataIndex:dataIndex ,renderer : renderColumn, width:length/size, align: 'center',sortable: false,resizable: true,menuDisabled :true}
	}
	
	for(var i=0;i<monitor.portList.length;i++){
		if(monitor.portList[i].selected)
			columns.push(makeHeader(monitor.portList[i]))
	}
	
	columns.push({header:I18N.PERF.mo.time,dataIndex:"time",width:length/size, align: 'center',renderer: timeRender})
	fields.push("time")
	var cm = new Ext.grid.ColumnModel(columns)
	window.store = new Ext.data.JsonStore({
		 data : [],
         fields: fields
     })
	
	window.grid = new Ext.grid.GridPanel({
    	border: false, region: 'center',height: document.body.offsetHeight - 35,id : "grid", cm : cm,	trackMouseOver : false,
    	store : store, renderTo: "container"
    });
}

function timeRender(v,m,r){
	var date = new Date();
	date.setTime(v);
	return date.doFormat("yyyy-MM-dd hh:mm:ss");
	//return new Date(v).format("yyyy-MM-dd hh:mm:ss");
}


function getInitialData(now,value){
		var data = [];
		for(var i=-1;i<0;i++){
			data.push({
				x : now + 1000*i, 
				y : value ? value : 0,
				fake : true
			})
		}
		return data;
}

function createSeries(record,now){
	return { 
		//需要记录 series的下标
		name:  toMark(record.portIndex) + "[" + record.entityName + "]",
		mark : record.portIndex+"("+record.ip+")",
		data: getInitialData(now),
		fake : true
	}
}

/**
 * 创建动态曲线图(实时性能)
 */
executor.createDynamicCurve = function(){
	var series = []
	var now = new Date().getTime()
	for(var i =0; i<monitor.portList.length;i++){
		if(monitor.portList[i].selected)
			series.push(createSeries(monitor.portList[i],now));
	}
	chart = new Highcharts.Chart({
		chart: {renderTo: 'container',width : document.body.offsetWidth, defaultSeriesType: 'line',animation : false,
				events : {
					'load' : function(){
					}
				}
		},
		title : { text : monitor.name + "(" + I18N.PERF.port.real + ")" },credits:{enabled:false},legend: {enabled: true},
		xAxis: {gridLineWidth: 1,lineColor: 'gray',tickColor: 'gray',type: 'datetime',tickPixelInterval: 150,
			labels: {
				formatter : function(){
					return Highcharts.dateFormat('%H:%M:%S', this.value);
				}
			}
		},
		yAxis: {
			title: {text: IndexUtil.getChineseName(monitor.index) }, 
			min:0,minorGridLineWidth: 0,gridLineWidth: 1,gridLineColor : 'gray',alternateGridColor: null,
			labels: {
				formatter : function(){
					if(["InOctets","OutOctets"].indexOf(monitor.index) != -1){
						return DateUtil.getFlowDisplayRest(this.value,monitor.type);
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
			}
		},
		plotOptions: {
			line: {
				lineWidth: 1,states: {hover: {lineWidth: 2}},
				pointInterval: 3600000,pointStart: new Date()
			}
		},
		tooltip: {
			formatter: function() {
				var color = this.series.color
				var name = this.series.name
				if(this.y == -1 || this.point.fake == true) //#517CAD
					return  "<div style='color : "+color+"'>" + name + "</div>" 
						+ "<font style='color:#517CAD'>" + I18N.PERF.mo.nowValue 
						+ " :</font> <span style='color:red'>" + I18N.PERF.mo.notCollected + "</span><br>"
						+ "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
						+ Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', this.x) +'</span><br/>'
				if(["InOctets","OutOctets"].indexOf(monitor.index) != -1){
					var value = this.y ,
						flow,
						oct,
						flowTitle,
						octTitle;
					flow = DateUtil.getFlowDisplayRest(this.y,monitor.type);
					oct = DateUtil.getOctDisplayRest(this.y);
					if( "OutOctets" == monitor.index ){
						flowTitle = I18N.PERF.moI.OutFlowSing;
						octTitle = I18N.PERF.moI.OutOctetsSing;
					}else if( "InOctets" == monitor.index ){
						flowTitle = I18N.PERF.moI.InFlowsSing;
						octTitle = I18N.PERF.moI.InOctetsSing;
					}
					return  "<div style='color : " + color + "'>" + name + "</div>"
					 		+ "<font style='color:#517CAD'>"+flowTitle+" :</font> " +  flow +  "<br>" 
					 		+ "<font style='color:#517CAD'>"+octTitle+" :</font> " +  oct +  "<br>" 
					        + "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
					        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', this.x) +'</span><br/>';
				}
				return  "<div style='color : " + color + "'>" + name + "</div>"
				 		+ "<font style='color:#517CAD'>" + I18N.PERF.mo.nowValue + " :</font> " +  this.y +  "<br>" 
				        + "<font style='color:#517CAD'>" + I18N.PERF.mo.collectTime + " :</font> <span style='color:green'>" 
				        + Highcharts.dateFormat('%Y-%m-%e %H:%M:%S', this.x) +'</span><br/>';
			}
		},
		series: series
	});
	//if(0 < monitor.dataSource.length)
		//parseDataSource()
}