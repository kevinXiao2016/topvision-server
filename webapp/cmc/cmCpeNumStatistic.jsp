<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title></title>
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/highcharts/highcharts.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<style type="text/css">
	body {
		overflow: hidden;height: 100%
	}
	#toolbar {
		position:absolute;height: 25px;width: 100%;
	}
</style>
<script type="text/javascript">
var chart;
var cmCpeNumInAreaJson = ${cmCpeNumInAreaJson};
function redraw(){
	initialize()
}
function initialize(){
	chart = new Highcharts.Chart({
    chart: { type: 'bar', renderTo: 'cmCpeNumStatistic', animation : false },
    title: {text: I18N.CMCPE.CMCPENUMStatistics},
    credits: {enabled : false},
    animation : false,
    xAxis: [{
        categories: cmCpeNumInAreaJson.regions,
        reversed: false
    }, { 
        opposite: true,
        reversed: false,
        categories: cmCpeNumInAreaJson.regions,
        linkedTo: 0
    }],
    yAxis: {
        title: {
            text: null
        },
        labels: {
            formatter: function(){
                return (Math.abs(this.value));
            }
        }
    },

    plotOptions: {
        series: {
            stacking: 'normal'
        }
    },

    tooltip: {
        formatter: function(){
            return '<b>'+I18N.CMCPE.AREA+':'+ this.point.category +'</b><br/>'+
                this.series.name + ':' + Highcharts.numberFormat(Math.abs(this.point.y), 0);
        }
    },
    series: [{
        name: I18N.CMCPE.CMNUM,
        data: cmCpeNumInAreaJson.onlineCmNum
    }, {
        name: I18N.CMCPE.CPENUM,
        data: cmCpeNumInAreaJson.onlineCpeNum
    }]
});
}
Ext.onReady(function(){
	redraw();
})

</script>
</head>
<body>
<div id="cmCpeNumStatistic" style="width: 100%; height: 400px;"></div>
</body>
</html>