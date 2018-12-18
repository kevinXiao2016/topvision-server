<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<script src="dispersion.json" type="text/javascript" charset="utf-8"></script>
<Zeta:Loader>
    library ext
    library zeta
    module dispersion
    css css/bootstrap
    import js/highcharts-4.1.5/highcharts
    import dispersion/dispersion-detail
</Zeta:Loader>

<style type="text/css">
body,html{height:100%;overflow:hidden;background-color: #bdc3c7;}
#trendChart{
	width: 100%;
}
#menu{width: 100%;position: relative;height: 40px;}
#menu-btn-group{left:50%;position:absolute;margin-left: -258px;margin-top: 5px;}
#timeBanner{position:absolute;height:30px;font-size: 14px;color:#2980b9;font-family: "Microsoft YaHei" ! important;padding-top: 15px;}
#exactTime{font-size: 14px;font-family: "Microsoft YaHei" ! important;color:#2980b9;}
#snrDistributionChart, #powerDistributionChart{
	float: left;
}
#snrDistributionChart{
	padding-right: 5px;
}
#powerDistributionChart{
	padding-left: 5px;
}
#tip{
	position:absolute;
	display:none;
	width:250px;
	height:250px;
	background-color: #fff;
	border-radius:10px;
}
#tip img{
	margin:80px 87.5px 10px 87.5px;
}
#tip p{
	vertical-align: middle;
	text-align:center;
	font-family: "Microsoft YaHei" ! important;
	font-size: 20px;
	color: #909090;
}
</style>

<script type="text/javascript">
var opticalNodeId = ${opticalNodeId};
var opticalNode = ${opticalNode};

$(function () {
	//初始化布局
	initLayout();
	
	loadDispersionTrend();
	
	$(window).bind('resize', function(e){
	   throttle(resize, 50)();
    });
});				
</script>
</head>
<body class="whiteToBlack">
	<div id="trendChart"></div>

	<div id="menu">
		<div id="timeBanner">
			@dispersion.distChartTime@@COMMON.maohao@<span id="exactTime"></span>
		</div>
		
		<div id="menu-btn-group" class="btn-group" style="">
			<button type="button" id="firstCollectTime" class="btn btn-default" onclick="javascript:loadFirstDist();">
	  			<span class="glyphicon glyphicon-step-backward"></span>
	  			@dispersion.firstPoint@
			</button>
			<button type="button" id="prevCollectTime" class="btn btn-default" onclick="javascript:loadPrevDist();">
	  			<span class="glyphicon glyphicon-chevron-left"></span>
	  			@dispersion.prevPoint@
			</button>
			<button type="button" id="nextCollectTime" class="btn btn-default" onclick="javascript:loadNextDist();">
	  			<span class="glyphicon glyphicon-chevron-right"></span>
	  			@dispersion.nextPoint@
			</button>
			<button type="button" id="lastCollectTime" class="btn btn-default" onclick="javascript:loadLastDist();">
	  			<span class="glyphicon glyphicon-step-forward"></span>
	  			@dispersion.lastPoint@
			</button>
		</div>
	</div>
	
	<div id="snrDistributionChart"></div>
	<div id="powerDistributionChart"></div>
	
	<div id="tip">
		<img src="/images/sad.png" alt="" />
		<p>@dispersion.nodata@</p>
	</div>
</body>
</Zeta:HTML>