<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<HEAD>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	library Highchart
	library Highstock
	plugin DateTimeField
    module performance
</Zeta:Loader>
<script type="text/javascript">
var ZetaGUI = {enableWaitingDlg: false, roundedBorders: true, enableDraggable: false,
        windowShadow: true, enableBorders: true, enableCollapsible: true, enableSplit: true,
        tabPlain: false, tabBorders: true, resizeTabs: true, minTabWidth: 80, tabWidth: 150,
        NAVI_BAR_HEIGHT: 32, headerHeight: 54, viewMargin: {left: 4, right: 4, top: 0, bottom: 4}};
var chart;

function closeWindow(id) {
    if (id == 'modalDlg') {
        closeModalDlg();
    } else {
        var w = Ext.WindowMgr.get(id);
        if (w != null) {
            w.close();
        }
    }
}
/*************************************************************
                                                            入口
*************************************************************/
$(document).ready(function(){
	//******初始化Monitor数据*********//
	buildToolbar();
	var now = new Date();
	Ext.getCmp("endTimeField").setValue(now);
	Ext.getCmp("startTimeField").setValue(new Date(now.getTime() - 7*24*60*60*1000));
	// 加载engine列表
	loadEngineList();
	//query();
});

function showMessageDlg(title, msg, type,fn) {
    var icon = (type == "error" ? Ext.MessageBox.ERROR : (type == "question" ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
    Ext.MessageBox.show({title: title, msg: msg, buttons: Ext.MessageBox.OK, icon: icon,fn:fn});
}

function query(){
	queryEngine($('#engineId').val())
}

/*************************************************************
                    创建顶部工具栏以及工具栏菜单
*************************************************************/
function buildToolbar() {
	jtb = new Ext.Toolbar();
	jtb.render('toolbar');
	var items = [];
    items[items.length] = buildEngineSelect()
    items[items.length] = {xtype: 'tbspacer', width: 5}// add a 10px space
    items[items.length] = '时间查询区间:';
    items[items.length] = {xtype: 'tbspacer', width: 5};
    items[items.length] = { xtype: 'datetimefield', id:"startTimeField",editable : true,validationEvent : 'keyup'}
    items[items.length] = {xtype: 'tbspacer', width: 5}// add a 10px space
    items[items.length] = '-';
    items[items.length] = {xtype: 'tbspacer', width: 5}// add a 10px space
    items[items.length] = { xtype: 'datetimefield', id:"endTimeField",editable : true}
    items[items.length] = {xtype: 'tbspacer', width: 5}
    items[items.length] = { xtype: 'button',text: '查询',iconCls:'bmenu_find', handler: query}
	jtb.add(items);
	jtb.doLayout();
}

function buildEngineSelect() {
    return '<div id="engine"><td style="width: 40px;" align="right">Engine:</td>&nbsp;' +
           '<td style="width: 70px;">' +
           '<select style="width: 70px;" class="normalSel" id="engineId"></select></td></div>'
}

function loadEngineList() {
	$.ajax({
		url : '/admin/loadEngineList.tv',
		dataType:'json',
		cache:false,
		success:function(json){
			for(var i=0; i<json.length; i++) {
				$('#engineId').append(String.format('<option value="{0}">{1}</option>', json[i].engineId, json[i].name));
			}
			// 查询第一个
			queryEngine(json[0].engineId);
		},error:function(){
		}
	});
}

function queryEngine(engineId) {
	var startTime =  $("#startTimeField").val();
	var endTime =  $("#endTimeField").val();
	//如果只输入了结束时间没有起始时间,提示错误
	if(!Ext.getCmp("startTimeField").isValid()){
		showMessageDlg("提示","请输入正确的时间格式",'error',function(){
            Ext.getCmp("startTimeField").focus();
        });
        return;
	}
	if(!Ext.getCmp("endTimeField").isValid()){
        showMessageDlg("提示","请输入正确的时间格式",'error',function(){
            Ext.getCmp("endTimeField").focus();
        });
        return;
    }
	//如果只输入了结束时间没有起始时间,提示错误
	if(!startTime && endTime){
		showMessageDlg("提示","请输入查询的起始时间",'error',function(){
			Ext.getCmp("startTimeField").focus();
		});
		return;
	}
	//如果只输入了起始时间没有结束时间,提示错误
	if(startTime && !endTime){
        showMessageDlg("提示","请输入查询的结束时间",'error',function(){
            Ext.getCmp("endTimeField").focus();
        });
        return;
    }
	
	$.ajax({
		url : '/admin/queryEngineExecutorThread.tv',
		dataType:'json',
		cache:false,
		data: {
			engineId: engineId,
			st: startTime,
			et: endTime
		},
		success:function(json) {
			_renderGraphic(json);
		},error:function(){
		}
	});
}

var doc = document;
function _renderGraphic(json){
    var $container = $("#container");
    var w = doc.body.offsetWidth;
    var h = doc.body.offsetHeight-28;
    $container.css({
    	 height : h,
    	 width : w - 30
    });
	
	chart = new Highcharts.StockChart({
	    chart: {
	        renderTo: 'container',type: 'spline',zoomType: 'x'
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
		//scrollbar: {liveRedraw: false},
		subtitle: {text: '@Tip.trendSubTitle@'},
	    rangeSelector:{
	    	buttonTheme: { // styles fro the buttons
	    		r: 2,
	    		width: 52,
				height: 16,
	    		style: {
	    			color: '#039'
	    		}
	    	},
	    	buttons:[
				{type: 'day', count: 1, text: '@Tip.oneDay@'},
				{type: 'week', count: 1, text: '@Tip.oneWeek@'},
				{type: 'month', count: 1, text: '@Tip.oneMonth@'},
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
			/*events : {
				afterSetExtremes : afterSetExtremes
			},*/
			type: 'datetime',
			//ordinal: false,
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
			minRange: 1 * 1000 // one hour,
		},
	    yAxis: {
	    	labels: {
	    		formatter: function() {
	    			return this.value ;
	    		}
	    	}
	    	//*min: formatMin(perfTargetName),
	    	//max: formatMax(perfTargetName,seriesOptions),
	        //title: {text: yText}*/
	    },
	    legend: {
	    	enabled: true,
	    	floating: true,
	    	align: 'right',
	    	width: 200,
	    	maxHeight: 55,
	    	itemWidth:200,
	    	x:27,
        	backgroundColor: '#FCFFC5',
        	borderColor: '#C0C0C0',
        	borderWidth: 1,
	    	layout: 'horizontal',
	    	verticalAlign: 'top',
	    	shadow: true
	    },
	    plotOptions : {
	    	series:{
	    		lineWidth : 1
	    	}
	    },
	    series: [
	        {name: "activeCount",data: json.activeCount, color: 'blue',cat:"activeCount"},
	        {name: "poolSize",data: json.poolSize, color: 'black',cat:"poolSize"},
	        {name: "completeedTaskCount",data: json.completeedTaskCount, color: 'purple',cat:"completeedTaskCount"}
	    ]
	});
	
}

</script>
</HEAD>
<style>
.entityClazz{color: green;}
.labelClazz {color: #517CAD;}
#detail{width:400px;height:350px;}
table th, table td ,table legend{
    font : normal 13px Verdana, sans-serif;
}
.ext-strict .ext-ie .x-date-menu, .ext-border-box .ext-ie8 .x-date-menu{
    height: 238px;
}
 .x-form-spinner-splitter {
    position : absolute;
 }
</style>
<body class=CONTENT_WND style=" overflow: auto;">
      <div id="toolbar" style="position: absolute; left: 0; top: 0;width:100%"></div> 
      <div id="container" style="position: absolute;left : 15px;top:27px;"></div>
</body>
</Zeta:HTML>
