<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/flick/jquery-ui-1.10.3.custom.min.css" />
<Zeta:Loader>
	library jQuery
    library Ext
    library Zeta
    module spectrum
    import js/json2
    import js/jquery/jquery.ba-resize
    import js.jquery.Nm3kMsg
    import performance/js/jquery-ui-1.10.3.custom.min
    import js/highcharts-3.0.5/highcharts
    import js/highcharts-3.0.5/highcharts-more
    import cmc.spectrum.spectrumUtil
    css cmc.spectrum.playerControl
    import cmc.spectrum.playerControl
    import  cmc.spectrum.videoPlayer
</Zeta:Loader>
<style type="text/css">
body,html{height:100%;overflow:hidden;padding: 10px;padding-top: 0px;}
#buoy_x_div, #buoy_add_div, #buoy_y_div{vertical-align: middle;}
.tip_square{width: 14px;height: 14px;display: inline-block;vertical-align: middle;}
#buoyColorTip_x{background-color: black;}
#buoyColorTip_add{background-color: green;}
#buoyColorTip_horizontal{background-color: red;}
.span_with_width{display: inline-block;width: 30%;height: 14px;padding-top:2px;padding-bottom: 3px;vertical-align: middle;}
#sideArrow{width:19px;height:140px;overflow:hidden;position: absolute;right:0px;top:0px;z-index:1000;cursor:pointer;}
#arrow{position:absolute;top:20px;left:8px;width:4px;height:8px;overflow: hidden;}
#sidePart{width:400px; padding:0px 10px; height:100%; overflow:auto; background: #F4F4F4; position:absolute; top:0; right:-420px; border-left:1px solid #9a9a9a; z-index:999; opacity:0.9; filter:alpha(opacity=90);}
#line_ul li{line-height: 29px;vertical-align: middle;}
#line_ul li input, #line_ul li button, #line_ul li a{vertical-align: middle;}
.pointer_label{margin-left: 5px;cursor: pointer;}
#buoy_ul li{vertical-align: middle;line-height: 20px;padding:4px 3px;}
#buoy_ul li input, #buoy_ul li label{vertical-align: middle;}
.floatRight{float: right !important;}
.spinnerInput{height: 18px;text-indent:2px;width: 150px;}
.channelInput{width: 50px;}
#buoyLine_x{position: absolute;z-index: 10;}
#buoyLine_add{width: 2px;position: absolute;z-index: 11;background-color: green;display: none;}
#buoyLine_y{height: 2px;position: absolute;z-index: 11;background-color: red;display: none;}
.nowrap{white-space:nowrap;}
#channel_table > thead > tr > th{background-color: #0073EA;font-size: 12px;font-weight: bold;text-align: center;height: 29px;color: #FFFFFF;}
#channel_table > tbody > tr > td{text-align: center;color: #0073EA;}
#channel_table > tbody,  #channel_table > thead{border-left: 1px solid #D6D6D6;border-right: 1px solid #D6D6D6;}
#container{}
.alert-danger {background-color: #F2DEDE;border-color: #EBCCD1;color: #A94442;font-size: 14px;line-height: 1.42857;margin-top: 10px;border-radius: 4px;padding: 10px 10px;}
#channelDiv > .ui-accordion-content-active{height:260px !important;}
.spectrumSideChinese{ background:url(/images/spectrum/spectrumSideChinese.png) no-repeat;}
.spectrumSideEnglish{ background:url(/images/spectrum/spectrumSideEnglish.png) no-repeat;}
#chart_wrapper{
	border: 1px solid #D0D0D0;
	background-color: #ffffff;
}
#videoName{
	font-size: 20px;
	text-align: center;
}
#videoTimeInfo{
	font-size:14px;
	color: #555;
	text-align: center;
	margin-bottom: 10px;
	font-weight: normal;
}
#nm3kPlayer{
	border: 1px solid #D0D0D0;
	border-top: none;
	border-radius: 0px !important;
}
#buoy_name, #add_buoy_name, #yBuoy_name{
}
#top_message_div, #bottom_message_div{padding:0 15px;}
#browerTip{
	position: absolute;
	left: 30px;
	bottom:10px;
	z-index: 9999;
	display: none;
}
.important-span{
	color: #E34B39;
}
/* .nm3kPlayerBtnBack, .nm3kPlayerBtnForward{display: none;} */
</style>
<script type="text/javascript">
var language = '<%=uc.getUser().getLanguage()%>';
var videoId = '${videoId}';
var videoJSON = ${videoJSON};
var spectrumUnit = '${spectrumUnit}';
var startFreq = '${startFreq}';
var endFreq = '${endFreq}';
</script>
</head>
<body>
	<div id="buoyLine_x"></div>
	<div id="buoyLine_add"></div>
	<div id="buoyLine_y"></div>
	
	<h1 id="videoName" class="blueTxt"></h1>
	<h2 id="videoTimeInfo"></h2>
	
	<div style="width:100%;">
	</div>
	
	<div id="browerTip">
		@spectrum.browerTip@：<span class="important-span">Firefox</span> @spectrum.and@ <span class="important-span">Chrome</span>@spectrum.browerTip_1@
	</div>
	
	<div id="chart_wrapper">
		<div id="top_message_div">
			<table id="topTip" width="100%">
				<tbody>
					<tr>
						<td width="50%">
							<span class="rightBlueTxt">@spectrum.width@@COMMON.maohao@</span>
							<span id="frequency_span_top">81.10</span>
							<span>MHz</span>
						</td>
						<td width="50%">
							<div id="buoy_x_div">
								<span id="buoyColorTip_x" class="tip_square"></span>
								<span id="buoy_name" class="span_with_width">@spectrum.realTimeLine@</span>
								<span id="x_data" class="span_with_width"></span>
								<span id="y_data" class="span_with_width"></span>
							</div>
							<div id="buoy_add_div" style="display:none;">
								<span id="buoyColorTip_add" class="tip_square"></span>
								<span id="add_buoy_name" class="span_with_width">@spectrum.incremental@</span>
								<span id="add_x_data" class="span_with_width"></span>
								<span id="add_y_data" class="span_with_width"></span>
							</div>
							<div id="buoy_y_div" style="display:none;">
								<span id="buoyColorTip_horizontal" class="tip_square"></span>
								<span id="yBuoy_name" class="span_with_width">@spectrum.level@</span>
								<span id="yBuoy_x_data" class="span_with_width"></span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div id="container"></div>
		
		<div id="bottom_message_div">
			<table id="bottom_message" width="100%">
				<tbody>
					<tr>
						<td width="33.3%" align="left">
							<span>@spectrum.startValue@@COMMON.maohao@</span>
							<span id="start_frequency_bottom">0.50</span>
							<span>MHz</span>
						</td>
						<td width="33.3%" align="center">
							<span>@spectrum.centerValue@@COMMON.maohao@</span>
							<span id="center_frequency_bottom">41.05</span>
							<span>MHz</span>
						</td>
						<td width="33.3%" align="right">
							<span>@spectrum.endValue@@COMMON.maohao@</span>
							<span id="end_frequency_bottom">81.60</span>
							<span>MHz</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<!-- 播放器 -->
	
	<div id="nm3kPlayer" class="pT10">
	</div>
	
	<div id="sidePart">
		<div id="line_div">
			<ul id="line_ul">
				<li>
					<input type="checkbox" id="lineCbx_real" checked="checked" disabled="disabled"></input>
					<label class="pointer_label" for="lineCbx_real">@spectrum.realTimeLine@</label>
				</li>
				<li>
					<input type="checkbox" id="lineCbx_max"></input>
					<label class="pointer_label" for="lineCbx_max">@spectrum.maxLine@</label>
					<a id="reset_max" href="javascript:;" class="normalBtn floatRight disabledAlink" onclick=""><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
				</li>
				<li>
					<input type="checkbox" id="lineCbx_min"></input>
					<label class="pointer_label" for="lineCbx_min">@spectrum.minLine@</label>
					<a id="reset_min" href="javascript:;" class="normalBtn floatRight disabledAlink" onclick=""><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
				</li>
				<li>
					<input type="checkbox" id="lineCbx_average"></input>
					<label class="pointer_label" for="lineCbx_average">@spectrum.averageLine@</label>
					<a id="reset_average" href="javascript:;" class="normalBtn floatRight disabledAlink" onclick=""><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
				</li>
			</ul>
		</div>
		<hr />
		<div id="accordion">
			 <div class="group">
				<h3>@spectrum.buoy@</h3>
				<div>
					<ul id="buoy_ul">
						<li>
							<input type="radio" name="line_buoy_radio" id="buoyRadio_real" checked="checked"></input>
							<label class="pointer_label" for="buoyRadio_real">@spectrum.realTimeLine@</label>
						</li>
						<li>
							<input type="radio" name="line_buoy_radio" id="buoyRadio_max" disabled="disabled"></input>
							<label class="pointer_label" for="buoyRadio_max">@spectrum.maxLine@</label>
						</li>
						<li>
							<input type="radio" name="line_buoy_radio" id="buoyRadio_min" disabled="disabled"></input>
							<label class="pointer_label" for="buoyRadio_min">@spectrum.minLine@</label>
						</li>
						<li>
							<input type="radio" name="line_buoy_radio" id="buoyRadio_average" disabled="disabled"></input>
							<label class="pointer_label" for="buoyRadio_average">@spectrum.averageLine@</label>
						</li>
						<li>
							<input type="radio" name="line_buoy_radio" id="buoyRadio_none"></input>
							<label class="pointer_label" for="buoyRadio_none">@spectrum.none@</label>
						</li>
						<li><hr /></li>
						<li>
							<input type="checkbox" id="buoyCbx_add"></input>
							<label class="pointer_label" for="buoyCbx_add">@spectrum.incremental@</label>
						</li>
						<li>
							<input type="checkbox" id="buoyCbx_horizontal"></input>
							<label class="pointer_label" for="buoyCbx_horizontal">@spectrum.level@</label>
						</li>
					</ul>
				</div>
			</div>
			<div class="group">
				<h3 id="referenceLevel_title"></h3>
				<div>
					<input class="spinnerInput" id="reference_power" value="80" style="ime-mode:disabled" onpaste="return false"/>
					<a id="modify_referencePower" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div class="alert-danger" style="display: none;" id="reference_power_tip">
						@spectrum.referencePowerTip@
					</div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.centerFrequency@</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="center_frequency" value="41.05" style="ime-mode:disabled" onpaste="return false"/>
					<a id="modify_centerFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="center_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.width@(MHz)</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="frequency_span" value="81.10" style="ime-mode:disabled" onpaste="return false"/>
					<a id="modify_frequencySpan" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="frequency_span_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.startFrequency@</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="start_frequency" value="0.50" style="ime-mode:disabled" onpaste="return false"/>
					<a id="modify_startFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="start_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.endFrequency@</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="end_frequency" value="81.60" style="ime-mode:disabled" onpaste="return false"/>
					<a id="modify_endFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="end_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="spectrumSideChinese" id="sideArrow">
		<div class="cmListSideArrLeft" id="arrow"></div>
	</div>
</body>
</Zeta:HTML>