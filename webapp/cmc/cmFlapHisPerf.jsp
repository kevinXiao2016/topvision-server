<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jQuery
    library Ext
    library Zeta
    module cmc
    import js/highstock/highstock
</Zeta:Loader>
<style type="text/css">
.errDiv{ padding:20px; border:1px solid #D8D8D8; background:#fff;
	border-radius: 6px; -webkit-border-radius: 6px; -moz-border-radius: 6px; -ms-border-radius: 6px; 
	box-shadow:0px 2px 3px #CBCBCB; -webkit-box-shadow:0px 2px 3px #CBCBCB; -moz-box-shadow:0px 2px 3px #CBCBCB; -ms-box-shadow:0px 2px 3px #CBCBCB;
}
.errDiv p b{ font-size:14px; font-weight:bord; color:#f00; padding-bottom:10px; display:block;}
</style>
<script type="text/javascript">
var cmMac = '${cmMac}';
$(document).ready(function (){
    $.ajax({
        url: '/cmflap/queryOneCMFlapHisData.tv?r=' + Math.random(),
        type: 'POST',
        dataType:'json',
        data:{
        	cmMac:cmMac
        },
        success: function(allData) {
           var insReal = allData.insRealNum;
           var insFailNumData = allData.insFailNum;
           var rangeData = allData.rangePercent;
           var poweAdjData = allData.poweAdjNum;
           var startTime = allData.startTime;
           var endTime = allData.endTime;
           var array = [];
           if(insReal){
        	   array.push([insReal,I18N.cmc.flap.onlineCounter,I18N.cmc.flap.onLineCounterUnit,1,I18N.cmc.flap.num,I18N.cmc.flap.counter,startTime,endTime]);
           }
           if(rangeData){
        	   array.push([rangeData,I18N.cmc.flap.rangingPercent,I18N.cmc.flap.rangingSuccessPercent,50,I18N.cmc.flap.successPercent,"%",startTime,endTime]);
           }
           if(poweAdjData){
        	   array.push([poweAdjData,I18N.cmc.flap.powadjgrowth,I18N.cmc.flap.adjCounterUnit,1,I18N.cmc.flap.num,I18N.cmc.flap.counter,startTime,endTime]);
           }
           if(insFailNumData){
        	   array.push([insFailNumData,I18N.cmc.flap.failOnlineCounter,I18N.cmc.flap.increaseCounterUnit,2,I18N.cmc.flap.increaseCounter,I18N.cmc.flap.counter,startTime,endTime]);
           }
           if(!insReal&&!rangeData&&!poweAdjData&&!insFailNumData){
        	   var html = '<div class="edge16"><div class="errDiv">';
        	       html += I18N.cmc.flap.noflapDataAtten;
        	       html += '</div></div>'
               $("#infoDetail").append(html);
           }else{
        	   var len = array.length;
        	   //获取图表div的宽度和高度
        	   var width = 790;
        	   var height = 530;
        	   if(len==2){
        		   width = width/2;
        	   }else if(len==4||len ==3){
        		   width = width/2;
        		   height = height/2;
        	   }
        	   for(var i=0;i<len;i++){
        		   var j = i+1;
        		   var obj = array[i];
        		   showHistoryPerf(obj[0],'chart'+j,obj[1], obj[2],obj[3],obj[4],obj[5],obj[6],obj[7],height,width);
        	   }
           }
        }
    });
});

function showHistoryPerf(data, divId, title, yText, valueDecimals, seriesName, seriesUnit, startTime, endTime,h,w){
	if(!h){
		h = 260;
	}
	if(!w){
		w = 395;
	}
	var seriesOptions = [{
		name: seriesName,
		data: data
	}];
	buttons = [];
	Highcharts.setOptions({ 
		global: { useUTC: false } 
	}); 
	var dateRegion = startTime + '~' + endTime;
	new Highcharts.StockChart({
		chart: {
            renderTo: divId,
            type : 'spline',
			zoomType: 'x',
			width : w,
			height : h
        },
        rangeSelector:{
        	buttonTheme: {
                r: 1,
                width: 54,
                fill: 'none',
                style: {
                    color: '#039'
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
                {type: 'all', count: 1, text: I18N.Tip.all}
            ], 
            labelStyle: {
                display: 'none',
                color: 'silver',
                fontWeight: 'bold'
            },
            inputEnabled: false
        },
		loading: {
			showDuration : 2000
		}, 
        title: {
            text: title
        },
        subtitle : {
        	text : dateRegion
        },
        credits: {
        	enabled : false
        },
        xAxis: {
        	ordinal:false, 
	    	gridLineWidth: 1,
	    	lineColor: 'gray',
	    	tickColor: 'gray',
	    	type: 'datetime',
	    	labels : {
	    		formatter : function(){
	    			return Highcharts.dateFormat('%m/%e %H:%M', this.value)
	    		}
	    	},
	    	minRange: 100
	    }, 
	    legend: { 
	    	enabled: true,
	    	floating: true,
	    	verticalAlign:'top',
	    	align:'right',
	    	itemStyle: {
			   fontSize: '10px'
			},
			borderWidth: 0,
			backgroundColor:null,
			y:25
		},
	    navigator: {
			baseSeries: 0,
			enabled: true,
			height: 30,
			xAxis: {
				labels: {
					formatter: function(){
	    				return Highcharts.dateFormat('%Y/%m/%e %H:%M',  this.value);
		    		}
				}
			}
		},
        yAxis: {
        	title: {
                text: yText
            },
	    	labels: {
	    		formatter: function() {
	    			return this.value;
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
					if(point.y == -1){
						s += '<span class="tooltipSpan" style="color:'+  point.series.color + '">' + point.series.name + I18N.CMCPE.failedGetDataTip + '</span><br/>';
					}else{
						var y;
						if(point.series.name == '@Flap.target@'){
							y = point.y.toFixed(2);
						}else{
							y = point.y;
						}
						s += '<span class="tooltipSpan" style="color:'+ point.series.color + '">' + point.series.name 
						  + ':</span><span class="tooltipSpan" style="color:'+ point.series.color + '">' + y + seriesUnit + '</span><br/>';
					}
				});
				return s;
			}
	    },
        series : seriesOptions
	});
}
</script>
</head>
<body class="openWinBody">
	<div id="infoDetail"></div>
	<table>
		<tr>
			<td><div class="highcharts-container" id="chart1"/></td>
			<td><div class="highcharts-container" id="chart2"/></td>
		</tr>
		<tr>
			<td><div class="highcharts-container" id="chart3"/></td>
			<td><div class="highcharts-container" id="chart4"/></td>
		</tr>
	</table>
</body>
</Zeta:HTML>