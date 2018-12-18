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
	plugin DateTimeField
    module platform
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
});

function showMessageDlg(title, msg, type,fn) {
    var icon = (type == "error" ? Ext.MessageBox.ERROR : (type == "question" ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
    Ext.MessageBox.show({title: title, msg: msg, buttons: Ext.MessageBox.OK, icon: icon,fn:fn});
}

function query(){
	var _entityId_ = Ext.getCmp("entityCombo").getValue();
	var startTime =  $("#startTimeField").val();
	var endTime =  $("#endTimeField").val();
	//如果只输入了结束时间没有起始时间,提示错误
    if(!_entityId_){
        showMessageDlg("提示","请选择设备",'error',function(){
            Ext.getCmp("entityCombo").focus();
        });
        return;
    }
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
	entityId = _entityId_;
	renderGraphic(_entityId_,startTime,endTime);
}

/*************************************************************
                    创建顶部工具栏以及工具栏菜单
*************************************************************/
function buildToolbar() {
	var store = new Ext.data.JsonStore({
        autoDestroy: true,
        url : '/performance/loadEponDeviceList.tv',cache:false,
        autoLoad : true,
        fields: ['entityId', 'ip']
    });
	store.on("load",function(t,rs){
		if(rs.length > 0 ){
			var r = rs[0];
			entityId = r.data.entityId;
			Ext.getCmp("entityCombo").setValue(r.data.entityId);
			renderGraphic();
		}
	})
	jtb = new Ext.Toolbar();
	jtb.render('toolbar');
	var items = [];
	items[items.length] = '设备IP:';
	 items[items.length] = {xtype: 'tbspacer', width: 5};
    items[items.length] = {xtype:'combo', store: store, triggerAction : 'all',editable : false,valueField: 'entityId',displayField: 'ip',id: 'entityCombo'}
    items[items.length] = {xtype: 'tbspacer', width: 15}
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

function showDetail(onuId){
	var url = "/performance/showRestartDetail.tv?entityId=" + entityId;
	url += "&deviceIndex=" + onuId;
	url += "&startTime=" + startTime;
	url += "&endTime=" + endTime;
	createDialog("restartDetail", '设备重启报表', 670, 540, url , null, true, true);
}

function renderGraphic(){
	var data = {};
	if(arguments.length == 0){
		data.entityId = entityId;
	}else{
		data.entityId = arguments[0];
		if(arguments[1]){
		    data.startTime = arguments[1];
		    data.endTime = arguments[2];
		}
	}
	$.ajax({
		url : '/performance/loadRestartStasticData.tv',dataType:'json',cache:false,
		data : data,
		success:function(json){
			if(json.data.length == 0){
				showMessageDlg("提示","没有设备重启过")
			}else{
				window.startTime = json.startTimeLong;
				window.endTime =  json.endTimeLong;
				Ext.getCmp("startTimeField").setValue(json.startTime);
			    Ext.getCmp("endTimeField").setValue(json.endTime);
				if(typeof chart == 'object' && chart != null && chart.destroy){
			        chart.destroy();
			    }
			}
			_renderGraphic(json);
		},error:function(){
			showMessageDlg("提示","查询失败,请重试!")
		}
	});
}

function createDialog(id, title, width, height, url, icon, modal, closable, closeHandler) {
    if(typeof width == 'string'){
        switch(width){
            case 'small_4_3':
                width = 360
                height = 270
                break
            case 'normal_4_3':
                width = 480
                height = 360
                break
            case 'big_4_3':
                width = 640 
                height = 480
                break
            case 'small_3_4':
                width =  270
                height =360
                break
            case 'normal_3_4':
                width =  360
                height = 480
                break
            case 'big_3_4':
                width = 480
                height = 640
                break
            case 'small_16_9':
                width = 480
                height = 270
                break
            case 'normal_16_9': 
                width = 640
                height = 360
                break
            case 'big_16_9':
                width = 800
                height = 450
                break
        }
    }
    if (id == 'modalDlg') {
        showModalDlg(title, width, height, url, closeHandler)
    } else {
        var win = new Ext.Window({id: id, title: title, width: width, height: height,
            border :false,
            bodyBorder  :false,
            modal: (modal == undefined ? true : modal), closable: (closable == null ? true : closable),
            plain:true, resizable: false, stateful: false, shadow: ZetaGUI.windowShadow,
            html: '<iframe width=100% height=100% frameborder=0 src="' + url + '"></iframe>'});
        if (typeof closeHandler != 'undefined') {
                win.on("close", closeHandler);
        }
        win.show();
        return win;
    }
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
	var colors = Highcharts.getOptions().colors,
		categories = json.categories;
	    //categories = ['OLT', 'CCMTS(2/1:3)', 'CCMTS(2/1:4)', 'CCMTS(2/1:5)', 'CCMTS(2/1:6)',"CCMTS(2/1:7)","CCMTS(2/2:3)",'CCMTS(2/2:4)', 'CCMTS(2/13:3)', 'CCMTS(2/3:4)', 'CCMTS(2/3:5)', 'CCMTS(2/13:6)',"CCMTS(2/3:7)","CCMTS(2/3:3)", 'CCMTS(2/3:4)', 'CCMTS(2/3:5)', 'CCMTS(2/13:6)',"CCMTS(2/3:7)","CCMTS(2/3:3)"],
	    name = '2013-2-6 至今',
	    data = json.data;
	
	function setChart(name, categories, data, color) {
	    chart.xAxis[0].setCategories(categories);
	    chart.series[0].remove();
	    chart.addSeries({
	        name: name,
	        data: data,
	        color: color || 'white'
	    });
	}
	
	chart = new Highcharts.Chart({
		loading :{
			hideDuration : 3000,
			showDuration : 3000
		}, 
	    chart: {
	        renderTo: 'container',
	        type: 'column',
	        events: {
	        	load:function(e){
	        		setTimeout(function(){
	        		    window.chartLoaded = true;
	        		},1000)
	        	}
	        }
	    },
	    title: {
	        text: String.format('EPON设备({0}) 重启统计',json.entityId)
	    },
	    subtitle: {
	        text: String.format('时间区间：{0} 至  {1}',json.startTime, json.endTime)
	    },
	    legend : {enabled : false},
	    xAxis: {
	        title: false,
	        labels: {
	            rotation: -30,
	            align: 'right',
	            style: {font: 'normal 10px Verdana, sans-serif'}
	        },
	        categories: categories
	    },
	    yAxis: {title: {text: '重启次数'}},
	    plotOptions: {
	        column: {
	            cursor: 'pointer',
	            point: {
	                events: {
	                    click: function() {
	                        //var drilldown = this.drilldown;
	                    	showDetail(this.deviceIndex);
	                    }
	                }
	            },
	            dataLabels: {
	                enabled: true,
	                color: colors[0],
	                style: {
	                    fontWeight: 'bold'
	                },
	                formatter: function() {
	                    return this.y +'(次)';
	                }
	            }
	        }
	    },
	    tooltip: {
	    	enabled: true,
	    	animation : false,
	        formatter: function() {
	        	if(!window.chartLoaded){return false;}
                var point = this.point,
                    str = String.format("<font class='entityClazz'>{0}</font><br><font class='labelClazz'>重启次数</font>:<b>{1}</b><br><font class='labelClazz' style='font-style:italic'>点击查看详细</font>" , this.x,this.y)
                return str;
	        }
	    },
	    series: [{
	        name: name,data: data,color: 'white'
	    }],
	    credits: {enabled: false}
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
