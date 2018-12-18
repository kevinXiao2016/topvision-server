<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js/highcharts-3.0.5/highcharts
</Zeta:Loader>
</head>
<script type="text/javascript">
    Ext.onReady(function(){
    	redraw();
    });
    var chart,
        splineTemplate = "@resources/NETWORK.splineTemplate@",
        pieTemplate = "@resources/NETWORK.pieTemplate@",
        columnTemplate  = "@resources/NETWORK.columnTemplate@",
       	//colors = Highcharts.getOptions().colors,
       	colors	= [
       	      	   '#058DC7', 
       	      	   '#F07A4C', 
       	      	   '#78BBF1', 
       	      	   '#DDDF00', 
       	      	   '#24CBE5', 
       	      	   '#64E572', 
       	      	   '#FF9655',
       	      	   '#FFF263',
       	      	   '#6AF9C4'
       	      	  ],
        networkInfoStastic;
    function redraw(){
         if(typeof chart == 'object' && chart != null){
            chart.destroy()
            chart = null
        }
        $.ajax({
        	url: '/portal/loadNetworkInfoDist.tv',cache:false,dataType:'json',
        	success:function(json){
        		networkInfoStastic = json;
	        	initialize();
        	},error:function(){}
        })
    }
    function initialize(){
    	chart = new Highcharts.Chart({
            chart: { renderTo: 'container', animation : false},
            title: {text: "@resources/NETWORK.networkInfoDist@"},
            credits: {enabled : false},
            animation : false,
            xAxis: {
            	labels: {
     	            rotation: -30,
     	            align: 'right',
     	            style: {font: 'normal 10px Verdana, sans-serif'}
     	        },
                categories:  networkInfoStastic.displayNameArray
            },
            legend : { enabled : true , verticalAlign:'bottom' , align:'center'},
            yAxis: { 
            	allowDecimals: false,
            	title : false
            },
            tooltip: {
                formatter: function() {
                    switch(this.series.type){
                        case 'spline' :
                        	return  String.format(splineTemplate, this.x, this.y * 2);
                        case 'pie' :
                        	return  String.format(pieTemplate, this.key, this.y, Math.round(this.percentage));
                        case 'column':
                        	return String.format(columnTemplate, this.x ,this.series.name , this.y);
                    }
                }
            }, 
            plotOptions: {
                pie: {
                    shadow: false,
                    animation : false ,
                    enabled: false,
                    dataLabels: {
                        formatter: function() {
                        	return null;
                        },
			            color: '#3e576f',
			            distance: -20
                    }
                },
                column : {
                	stacking: 'normal',
                	animation : false ,
                	dataLabels: {
                		x: -3,
                        color: '#000000',
                        enabled: true,
                        formatter: function() {
                            return  this.y > 0 ? this.y + " @base/UNIT.tai@" : null;
                        }
                    }
                }
            },
            series: [{
                type: 'column',
                name: '@resources/COMMON.offline@',
                color: colors[1],
                data: networkInfoStastic.offlineArray
            },{
                type: 'column',
                name: '@resources/COMMON.online@',
                color: colors[2],
                data: networkInfoStastic.onlineArray
            },{
                type: 'pie',
                data: [{
	                    name: '@resources/COMMON.offline@',
	                    y: networkInfoStastic.offline,
	                    color: colors[1]
	                }, {
	                    name: '@resources/COMMON.online@',
	                    y: networkInfoStastic.online,
	                    color: colors[2] 
	                }],
                center: ["90%", 0],
                size: 70
            }
            ]
        });
    }
    
    function showAlertByLevel(level) {
        window.top.addView("alertViewer", "@resources/EVENT.alarmViewer@", "alarmTabIcon", 
            'alert/showCurrentAlertList.tv?level=' + level);
    }
</script>
<body  >
<div id="container" style="width: 100%; height: 330px;"></div>
</body>
</Zeta:HTML>