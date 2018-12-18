<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=u8lHaOCM6WAmwPwXQFOrb0v9"></script>
<Zeta:Loader>
    library ext
    library jQuery
    library zeta
    module pnmp
    css terminal/pnmp/pnmp-cm-real
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import terminal/pnmp/js/pnmpUtil
    import terminal/pnmp/js/pnmp-cm-real
</Zeta:Loader>

<style type="text/css">
.grayBorder{ border: 1px solid #ccc;}
.blackSpan, .redSpan, .yellowSpan, .greenSpan, .graySpan{
	color: #fff;
	border-radius: 8px;
	padding: 2px 4px;
	background: #000;
}
.redSpan{ background: #E83C23;}
.yellowSpan{ background: #f38900;}
.greenSpan{ background: #449d44; }
.graySpan{ background: #ccc;}

#center-container{
	overflow: scroll;
}
#left-container {
	height: 100%;
}

#chart-container {
	height: 250px;
}

#center-container {
	/* background-color: white; */
	height: 100%;
}

#cm-chart-container {
	height: 320px;
}

.center-title {
	font-size: 16px;
	font-weight: bold;
	color:#B3711A;
}

#tip-div{ position:absolute; top:375px; right:0px;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}

.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }

.yellow-div{height:16px;width:16px;background-color: #f38900;}
.green-div{height:16px;width:16px;background-color: #449d44;}
.red-div{height:16px;width:16px;background-color: #E83C23;}

.red-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #E83C23;
}
.yellow-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #DCD345;
}
.green-circle{
	width: 14px;
	height: 14px;
	border-radius: 7px;
	background-color: #008000;
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
var sm, store, grid, bbr;
var map;
var cmcId = '${cmcId}';
var cmMac = '${cmMac}';
	
</script>
	</head>
	<body class="whiteToBlack">
		<div id="center-container">
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB20 pT20 noWidthCenter">
                    <li>
                        <a id="advance-query" href="javascript:reload();" class="normalBtnBig"><span><i class="miniIcoRefresh"></i>@pnmp.reload@</span></a>
                    </li>
                </ol>
            </div>
			<div>
				<div id="tab-container" style="width: 100%;height:500px;position: relative;">
					<div class="grayBorder" style="height:500px;width: 49.5%; position:absolute; top:0; left:0; background:#fff;">
						<div style="width:100%; height: 460px;">
                            <p class="flagP"><span class="flagInfo"><label class="flagOpen">@pnmp.cmrealdata@</label></span></p>
                            <table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                                <thead>
                                <tr>
                                    <th width="25%">@PERF.mo.Index@</th>
                                    <th width="25%">@PERF.value@</th>
                                    <th width="25%">@PERF.mo.Index@</th>
                                    <th width="25%">@PERF.value@</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="rightBlueTxt">@pnmp.cmMac@:</td>
                                    <td id = "cmMac" class = "textClass">@resources/WorkBench.loading@</td>
                                    <td class="rightBlueTxt">@pnmp.mtr@:</td>
                                    <td id = "mtr" class = "textClass">@resources/WorkBench.loading@</td>
                                </tr>
                                <tr>
                                    <td class="rightBlueTxt">@pnmp.upChannelFreq@:</td>
                                    <td id = "upChannelFreq" class = "textClass">@resources/WorkBench.loading@</td>
                                    <td class="rightBlueTxt">@pnmp.upChannelId@:</td>
                                    <td id = "upChannelId" class = "textClass">@resources/WorkBench.loading@</td>
                                </tr>
                                <tr>
                                    <td class="rightBlueTxt">@pnmp.upChannelWidth@:</td>
                                    <td id = "upChannelWidth" class = "textClass">@resources/WorkBench.loading@</td>
                                    <td class="rightBlueTxt">@pnmp.upTxPower@:</td>
                                    <td id = "upTxPower" class = "textClass">@resources/WorkBench.loading@</td>
                                </tr>
                                <tr>
                                    <td class="rightBlueTxt">@pnmp.upSnr@:</td>
                                    <td id = "upSnr" class = "textClass">@resources/WorkBench.loading@</td>
                                    <td class="rightBlueTxt">@pnmp.mtrVariance@:</td>
                                    <td id = "mtrVariance" class = "textClass">@resources/WorkBench.loading@</td>
                                </tr>
                                <tr>
                                    <td class="rightBlueTxt">@pnmp.upSnrVariance@:</td>
                                    <td id = "upSnrVariance" class = "textClass">@resources/WorkBench.loading@</td>
                                    <td class="rightBlueTxt">@pnmp.mtrToUpSnrSimilarity@:</td>
                                    <td id = "mtrToUpSnrSimilarity" class = "textClass">@resources/WorkBench.loading@</td>
                                </tr>
                                </tbody>
                            </table>
						</div>
					</div>
					<div id="a-container" class="grayBorder" style="height:500px;width: 49.5%; position:absolute; top:0; right:0;">
						<div id="spectrum-respond-chart-container" style="height:49.9%;width: 100%;"></div>
						<div id="tap-coefficients-chart-container" style="height:49.9%;width: 100%;"></div>
					</div>
				</div>
			</div>

		</div>
	</body>
</Zeta:HTML>