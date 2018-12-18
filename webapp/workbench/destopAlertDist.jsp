<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library highchart
</Zeta:Loader>
</head>
<script type="text/javascript">
var date = new Date();
var startTime = date.add(Date.DAY, -7);
startTime = Ext.util.Format.date(startTime, 'Y-m-d H:i:s');
var endTime = Ext.util.Format.date(date, 'Y-m-d H:i:s');

    Ext.onReady(function(){
    	/* Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function(color) {
            return {
                radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
                stops: [
                    [0, color],
                    [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                ]
            };
        }); */
    	redraw();
    });
    var chart,
    	alertStateList;
    function redraw(){
    	if(typeof chart == 'object' && chart != null){
            chart.destroy()
            chart = null
        }
    	$.ajax({
        	url: '/portal/loadAlertDistGraph.tv?startTime='+escape(startTime)+'&endTime='+escape(endTime),cache:false,dataType:'json',
        	success:function(json){
        		alertStateList = json;
	        	initialize();
        	},error:function(){}
        })
    }
    function initialize(){
    	chart = new Highcharts.Chart({
            chart: {                
                renderTo: 'container',
                plotBackgroundColor: "#ffffff",
                plotBorderWidth: null,
                marginTop : 10,
                plotShadow: false,
                borderWidth:0,
                backgroundColor:"#ffffff"
            },
            title: { text: false },
            credits: {enabled: false},
            tooltip: {
            	enabled:false,
                formatter: function() {
                    return '<b>'+ this.point.name +'</b> @Approximatelyequal@ '+ Math.floor(this.percentage) +' %';
                }
            },
            legend : {
            	floating : false,
            	y:5 
            },
            plotOptions: {
                pie: {
                    allowPointSelect: false,
                    animation : false ,
                    cursor: 'pointer',
                    point: {
                        events: {
                        	click: function() {
                                showAlertByLevel(this.levelId)
                            }
                        }
                    },
                    startAngle : alertStateList.length == 1 ? 0 : 90,
                    showInLegend: true,
                    dataLabels: {
                        enabled: true,
                        formatter: function() {
                            return this.y + ' @resources/COMMON.tiao@'+ this.point.name;
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                size: 150,
                name: 'Browser share',
                data: alertStateList
            }]
        });
    	if(alertStateList.length == 0 ){
    		$("#container").html("@resources/FAULT.noAlert@");
    	}
    }
    
    function showAlertByLevel(level) {
    	// add by fanzidong,上下文是限定在一周，所以加上时间参数
        // TODO
        window.top.addView("alertViewer", "@resources/EVENT.alarmViewer@", "alarmTabIcon", 
            'alert/showCurrentAlertList.tv?level=' + level + '&startTime='+escape(startTime)+'&endTime='+escape(endTime),null,true);
    }
</script>
<body  style="background:#ffffff;">
	<div style="width:500px; position:relative; margin:0px auto;">
		<div id="container" style="position:absolute;width: 500px; height: 300px;"></div>
	</div>
</body>
</Zeta:HTML>