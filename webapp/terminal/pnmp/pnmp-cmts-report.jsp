<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jQuery
    library zeta
    module pnmp
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import terminal/pnmp/js/pnmp-cmts-report
</Zeta:Loader>

<style type="text/css">
.x-panel-body{ background: transparent;}
#chart-title {
	text-align: center;
	font-size: 15px;
	font-weight: bolder;
	padding-top: 10px;
	color: #0266B1;
}
.x-block {
    width: 100%;
    height: 60px;
    padding: 16px;
    padding-bottom: 0px;
    padding-right: 0;
    background: #fff;
    margin-right: 20px;
}

.x-block .celltable {
    border-collapse: separate;
    border-spacing: 0;
    vertical-align: middle;
}

.x-block .x-block-icon {
    width: 40px;
    height: 40px;
    border-radius: 25px;
    text-align: center;
    line-height: 55px;
    margin-right: 0px;
}
.badColor {
	color: #E83C23;
}
.marginalColor {
	color: #f38900;
}
.healthColor {
	color: #449d44;
}
</style>

<script type="text/javascript">
var grid, store;
var onlineCmNum = ('${onlineCmNum}' == '' || '${onlineCmNum}' == null) ? 0 : parseInt('${onlineCmNum}'); //在线CM总数
var healthCmNum = ('${healthCmNum}' == '' || '${healthCmNum}' == null) ? 0 : parseInt('${healthCmNum}'); //健康CM总数
var marginalCmNum = ('${marginalCmNum}' == '' || '${marginalCmNum}' == null) ? 0 : parseInt('${marginalCmNum}'); //轻度劣化倾向CM总数
var badCmNum = ('${badCmNum}' == '' || '${badCmNum}' == null) ? 0 : parseInt('${badCmNum}'); //严重劣化倾向CM总数

function showPnmDetail() {
	var cmc = getSelectedCmc();
	window.parent.addView("pnmp-detail"+ cmc.cmcId, cmc.cmcName + "@pnmp.report@", "icoI3", "/pnmp/cmtsreport/showCmtsReportDetailView.tv?cmcId=" + cmc.cmcId + "&cmcName=" + cmc.cmcName + "&ipAddress=" + cmc.entityIp);
}

function drawPie() {
	var data;
	if(onlineCmNum == 0 || (healthCmNum == 0 && marginalCmNum == 0 && badCmNum == 0)){
		data = createNoPie();
	}else{
		data = createPie();
	}
	$('#chart-container').highcharts(data);
}
function createNoPie(){
	var data = createPie();
	data.series[0].data.push({
		name: '@pnmp.onlineCMAmount@',
    	y: 1,
    	color: '#cccccc'
	})
	data.tooltip.pointFormat = '{point.name}: <b>0</b>'
	return data;
}
function createPie(){
	return {
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false,
	            width: 220,
	            height: 220
	        },
	        title: {
	        	text: '@pnmp.onlineCMTotalNums@<br>' + onlineCmNum,
	        	align: 'center',
	            verticalAlign: 'middle',
	            y:-10
	        },
	        tooltip: {
	            headerFormat: '{series.name}<br>',
	            pointFormat: '{point.name}: <b>{point.y}</b>'
	        },
	        credits:{enabled: false},
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: false
	                },
	                showInLegend: false
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: '@pnmp.healthDistribution@',
	            innerSize: '70%',
	            data: [{
	            	name: '@pnmp.bad@',
	            	y: badCmNum,
	            	color: '#E83C23'
	            }, {
	            	name: '@pnmp.marginal@',
	            	y: marginalCmNum,
	            	color: '#f38900'
	            }, {
	            	name: '@pnmp.health@',
	            	y: healthCmNum,
	            	color: '#449d44'
	            }]
	        }]
	    }
}

function cmDistribution(){
	var $badCmNum = $("#badCmNum"),
		$marginalCmNum = $("#marginalCmNum"),
		$healthCmNum = $("#healthCmNum");
	$badCmNum.text(badCmNum);
	$marginalCmNum.text(marginalCmNum);
	$healthCmNum.text(healthCmNum);
}
</script>
</head>

<body style="background-color: #fff;">
	<div id="west-container" >
		<div id="chart-title">@pnmp.healthDistribution@</div>
		<div id="chart-container"></div>
		<div id="total-container">
			
			<div class="x-block">
		   		<table class="celltable" width="100%">
		       		<tbody>
		       			<tr>
		       				<td id="_san_942" width="76px">
		           				<div id="_san_944" class="x-block-icon" style="background-color:#E83C23">
		               				<i id="_san_946" class="iconfont icon-overview-balance"></i>
		           				</div>
		       				</td>
		       				<td id="_san_950" class="x-block-right">
		       					<p style="color:#E83C23">@pnmp.badCmNum@</p>
		       					<p id="badCmNum" style="color:#E83C23;font-size: 20px;"></p>
		            		</td>
		   				</tr>
		   			</tbody>
		   		</table>
			</div>
			
			<div class="x-block">
		   		<table class="celltable" width="100%">
		       		<tbody>
		       			<tr>
		       				<td id="_san_942" width="76px">
		           				<div id="_san_944" class="x-block-icon" style="background-color:#f38900">
		               				<i id="_san_946" class="iconfont icon-overview-balance"></i>
		           				</div>
		       				</td>
		       				<td id="_san_950" class="x-block-right">
		       					<p style="color:#f38900">@pnmp.marginalCmNum@</p>
		       					<p id="marginalCmNum" style="color:#f38900;font-size: 20px;"></p>
		            		</td>
		   				</tr>
		   			</tbody>
		   		</table>
			</div>
			
			<div class="x-block">
		   		<table class="celltable" width="100%">
		       		<tbody>
		       			<tr>
		       				<td id="_san_942" width="76px">
		           				<div id="_san_944" class="x-block-icon" style="background-color:#449d44">
		               				<i id="_san_946" class="iconfont icon-overview-balance"></i>
		           				</div>
		       				</td>
		       				<td id="_san_950" class="x-block-right">
		       					<p style="color:#449d44">@pnmp.healthCmNum@</p>
		       					<p id="healthCmNum" style="color:#449d44;font-size: 20px;"></p>
		            		</td>
		   				</tr>
		   			</tbody>
		   		</table>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>