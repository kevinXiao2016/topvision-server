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
    library Socket
    module spectrum
    import js/jquery/jquery.ba-resize
    IMPORT js.jquery.Nm3kMsg
    import performance/js/jquery-ui-1.10.3.custom.min
    import js/highcharts-3.0.5/highcharts
    import js/highcharts-3.0.5/highcharts-more
    import js/json2
    import cmc.spectrum.spectrumUtil
    import cmc.spectrum.cmcCurSpectrum
</Zeta:Loader>
<style type="text/css">
body,html{height:100%;overflow:hidden;}
#buoy_x_div, #buoy_add_div, #buoy_y_div{vertical-align: middle;}
.tip_square{width: 14px;height: 14px;display: inline-block;vertical-align: middle;}
#buoyColorTip_x{background-color: black;}
#buoyColorTip_add{background-color: green;}
#buoyColorTip_horizontal{background-color: red;}
.span_with_width{display: inline-block;width: 30%;height: 14px;padding-top:2px;vertical-align: middle;}
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
#channelDiv > .ui-accordion-content-active{height:290px !important;}
.spectrumSideChinese{ background:url(/images/spectrum/spectrumSideChinese.png) no-repeat;}
.spectrumSideEnglish{ background:url(/images/spectrum/spectrumSideEnglish.png) no-repeat;}
#realTime_video{
	display:none;
	position: absolute;
    width: 320px;
	z-index: 1000;
	border-radius: 4px;
	top:65px;
	left:150px;
    background: none repeat scroll 0 0 #FAFAFA;
    border: 1px solid #DDDDDD;
}
#realTime_video_button{
	display:block;
	width: 32px;
	height: 32px;
	margin: 0 auto;
}
.start{
	background: url(/css/playerBtns.png) no-repeat;
}
.start:hover{
	background: url(/css/playerBtns.png) no-repeat -32px 0;
}
.stop{
	background: url(/css/playerBtns.png) no-repeat -64px 0;
}
.stop:HOVER{
	background: url(/css/playerBtns.png) no-repeat -96px 0;
}
#channelDiv .ui-accordion-content{
	padding: 10px 5px;
}
.channel_status{
	display: inline-block;
	width: 14px;
	height: 14px;
}
.onImg{
	width: 14px;
	height: 14px;
	display: inline-block;
	background-repeat:no-repeat;
	background-image: url(/images/correct.png);
}
.offImg{
	width: 14px;
	height: 14px;
	display: inline-block;
	background-repeat:no-repeat;
	background-image: url(/images/wrong.png);
}
.channel_button{
	padding-right: 5px !important;
}
.channel_button span{
	padding-left: 5px !important;
}
.foucsChannel{
	background-color: #EFDB7B;
}
#channel_table td{
	height: 21px;
}
#browerTip{
	position: absolute;
	left: 20px;
	bottom:50px;
	z-index: 999;
	display: none;
}
.important-span{
	color: #E34B39;
}
#oltSwitch{
	position: absolute;
	left: 12px;
	top:95px;
	z-index: 999;
	/* border: 1px #909090 solid; */
	padding: 2px 5px;
	vertical-align: middle;
	display: none;
}
#oltSwitch .onImg{
}
#oltSwitch_refresh{
	text-decoration: underline;
	cursor: pointer;
	color: #E07628;
}
.refreshIcon{
	background: url('../images/refreshing2.gif') no-repeat 1px center;
	width: 16px;
	height:16px;
	padding-top: 8px;
	display: none;
}
.inline-block{
	display: inline-block !important;
}
#oltSwitch_a{
	cursor: pointer;
}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var language = '<%=uc.getUser().getLanguage()%>';
var cmcId = '${cmcId}';
vcEntityKey = 'cmcId';
var productType = '${productType}';
var spectrumOltSwitch = '${spectrumOltSwitch}';
var upChannels = ${upChannels};
var _frequencyMin = 5.0;
var _frequencyMax = 65.0;
var upChannelListObject = ${upChannelList};
var downChannelListObject = ${downChannelList};
var spectrumUnit = '${spectrumUnit}';
var supportSpectrumOptimize = ${supportSpectrumOptimize};
var timeInterval = ${timeInterval};
var oltSupportSpectrumOptimize = ${oltSupportSpectrumOptimize};
var downChannelOpened = ${downChannelOpened};

function oltSwitch(){
    window.parent.addView("showOltSpectrumConfig", "@spectrum.oltSpectrumCollectConfig@", "icoG7", "/cmcSpectrum/showOltSpectrumConfig.tv?cmcId="+cmcId);
}

//电平值转换
function parsePower(powerValue){
	if(spectrumUnit == '@spectrum/spectrum.dbuv@' && powerValue != null && powerValue != ''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

$(function(){
	$('#reference_power').val(parsePower(20));
});
</script>
</head>
<body>
	<div id="header">
		<%@ include file="/cmc/entity.inc"%>
	</div>
	
	<div id="oltSwitch">
		@spectrum.oltSwitch@:
		<a id="oltSwitch_a" class="onImg nm3kTip" onclick="oltSwitch();" nm3ktip="@spectrum.on@"></a>
		<a id="oltSwitch_refresh">@spectrum.refresh@</a>
	</div>
	
	<div id="browerTip">
		@spectrum.browerTip@@tip.maohao@<span class="important-span">Firefox</span> @spectrum.and@ <span class="important-span">Chrome</span> @spectrum.browerTip_1@
	</div>
	
	<div id="toolbar"></div>
	
	<div id="buoyLine_x"></div>
	<div id="buoyLine_add"></div>
	<div id="buoyLine_y"></div>
	
	<div id="realTime_video" class="x-window">
		<div id="realTime_video_title" class="x-window-tl">
			<div class="x-window-tr">
				<div class="x-window-tc">
					<div id="realTime_video_close" class="x-tool x-tool-close "> </div>
					<span class="x-window-header-text">@spectrum.realTimeVideo@</span>
				</div>
			</div>
		</div>
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="realTime_video_table">
			<tbody>
				<tr>
					<td class="rightBlueTxt" width="118">@realTimeVideo.startTime@</td>
					<td id="video_startTime"></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@realTimeVideo.recordLength@</td>
					<td id="video_continuedTime"></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@realTimeVideo.endTime@</td>
					<td id="video_endTime"></td>
				</tr>
				<tr class="darkZebraTr">
					<td colspan="2">
						<a id="realTime_video_button" class="start nm3kTip" nm3kTip="@realTimeVideo.startRecord@"></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div id="top_message_div">
		<table id="topTip" width="100%">
			<tbody>
				<tr>
					<td width="25%">
						<span class="rightBlueTxt">@spectrum.width@@tip.maohao@</span>
						<span id="frequency_span_top">81.10</span>
						<span>MHz</span>
					</td>
					<td width="25%">
                        <span class="rightBlueTxt">@spectrum.refreshTime@@tip.maohao@</span>
                        <span id="refresh-time"></span>
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
						<span>@spectrum.startValue@@tip.maohao@</span>
						<span id="start_frequency_bottom">0.50</span>
						<span>MHz</span>
					</td>
					<td width="33.3%" align="center">
						<span>@spectrum.centerValue@@tip.maohao@</span>
						<span id="center_frequency_bottom">41.05</span>
						<span>MHz</span>
					</td>
					<td width="33.3%" align="right">
						<span>@spectrum.endValue@@tip.maohao@</span>
						<span id="end_frequency_bottom">81.60</span>
						<span>MHz</span>
					</td>
				</tr>
			</tbody>
		</table>
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
					<input class="spinnerInput" id="reference_power" style="ime-mode:disabled" onpaste="return false"/>
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
					<a id="reset_centerFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
					<a id="modify_centerFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="center_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.width@(MHz)</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="frequency_span" value="81.10" style="ime-mode:disabled" onpaste="return false"/>
					<a id="reset_frequencySpan" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
					<a id="modify_frequencySpan" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="frequency_span_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.startFrequency@</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="start_frequency" value="0.50" style="ime-mode:disabled" onpaste="return false"/>
					<a id="reset_startFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
					<a id="modify_startFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="start_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group">
				<h3>@spectrum.endFrequency@</h3>
				<div>
					<input class="spinnerInput frequencyInput" id="end_frequency" value="81.60" style="ime-mode:disabled" onpaste="return false"/>
					<a id="reset_endFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
					<a id="modify_endFrequency" href="javascript:;" class="normalBtn floatRight"><span><i class="miniIcoSaveOK"></i>@spectrum.input@</span></a>
					<div id="end_frequency_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
			<div class="group" id="channelDiv">
				<h3>@spectrum.channelSetting@</h3>
				<div>
					<table class="zebraTableRows" id="channel_table" cellpadding="0" cellspacing="0" border="0">
						<thead>
							<tr>
								<th class="nowrap" width="40">@spectrum.channel@</th>
								<th class="nowrap" width="20">@spectrum.channelStatus@</th>
								<th class="nowrap" width="100">@spectrum.channelCenter@</th>
								<th class="nowrap" width="60">@spectrum.channelWidth@</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<tr id="channel_tr_1">
								<td class="nowrap">@spectrum.channel@1</td>
								<td><a id="channel_status_1" class="channel_status onImg"></a></td>
								<td><input class="spinnerInput frequencyInput channelInput" id="channel_center_1" style="ime-mode:disabled" onpaste="return false"/></td>
								<td>
									<select id="channel_width_1" class="normalSel">
										<option value="1.6">1.6</option>
										<option value="3.2">3.2</option>
										<option value="6.4">6.4</option>
									</select>
								</td>
								<td>
									<a id="channel_a_1" href="javascript:;" class="normalBtn channel_button" ><span><i class="miniIcoSaveOK"></i>@spectrum.adjust@</span></a>
									<a class="refreshIcon" id="refreshIcon_1"></a>
									<a id="channel_save_1" href="javascript:;" class="normalBtn channel_button floatRight disabledAlink" ><span><i class="miniIcoArrDown"></i>@spectrum.save@</span></a>
								</td>
							</tr>
							<tr class="darkZebraTr" id="channel_tr_2">
								<td class="nowrap">@spectrum.channel@2</td>
								<td><a id="channel_status_2" class="channel_status onImg"></a></td>
								<td><input class="spinnerInput frequencyInput channelInput" id="channel_center_2" style="ime-mode:disabled" onpaste="return false"/></td>
								<td>
									<select id="channel_width_2" class="normalSel">
										<option value="1.6">1.6</option>
										<option value="3.2">3.2</option>
										<option value="6.4">6.4</option>
									</select>
								</td>
								<td>
									<a id="channel_a_2" href="javascript:;" class="normalBtn channel_button" ><span><i class="miniIcoSaveOK"></i>@spectrum.adjust@</span></a>
									<a class="refreshIcon" id="refreshIcon_2"></a>
									<a id="channel_save_2" href="javascript:;" class="normalBtn channel_button floatRight disabledAlink" ><span><i class="miniIcoArrDown"></i>@spectrum.save@</span></a>
								</td>
							</tr>
							<tr id="channel_tr_3">
								<td class="nowrap">@spectrum.channel@3</td>
								<td><a id="channel_status_3" class="channel_status onImg"></a></td>
								<td><input class="spinnerInput frequencyInput channelInput" id="channel_center_3" style="ime-mode:disabled" onpaste="return false"/></td>
								<td>
									<select id="channel_width_3" class="normalSel">
										<option value="1.6">1.6</option>
										<option value="3.2">3.2</option>
										<option value="6.4">6.4</option>
									</select>
								</td>
								<td>
									<a id="channel_a_3" href="javascript:;" class="normalBtn channel_button" ><span><i class="miniIcoSaveOK"></i>@spectrum.adjust@</span></a>
									<a class="refreshIcon" id="refreshIcon_3"></a>
									<a id="channel_save_3" href="javascript:;" class="normalBtn channel_button floatRight disabledAlink" ><span><i class="miniIcoArrDown"></i>@spectrum.save@</span></a>
								</td>
							</tr>
							<tr class="darkZebraTr" id="channel_tr_4">
								<td class="nowrap">@spectrum.channel@4</td>
								<td><a id="channel_status_4" class="channel_status onImg"></a></td>
								<td><input class="spinnerInput frequencyInput channelInput" id="channel_center_4" style="ime-mode:disabled" onpaste="return false"/></td>
								<td>
									<select id="channel_width_4" class="normalSel">
										<option value="1.6">1.6</option>
										<option value="3.2">3.2</option>
										<option value="6.4">6.4</option>
									</select>
								</td>
								<td>
									<a id="channel_a_4" href="javascript:;" class="normalBtn channel_button" ><span><i class="miniIcoSaveOK"></i>@spectrum.adjust@</span></a>
									<a class="refreshIcon" id="refreshIcon_4"></a>
									<a id="channel_save_4" href="javascript:;" class="normalBtn channel_button floatRight disabledAlink" ><span><i class="miniIcoArrDown"></i>@spectrum.save@</span></a>
								</td>
							</tr>
							<tr id="channel_tr_comment">
								<td colspan="4" align="left" style="text-align: left;">
									@spectrum.comment@@tip.maohao@<br /> 
									@spectrum.adjustComment@<br />
									@spectrum.saveComment@
								</td>
								<td>
									<a id="resetChannelData" href="javascript:;" class="normalBtn channel_button floatRight"><span><i class="miniIcoRefresh"></i>@spectrum.reset@</span></a>
								</td>
							</tr>
						</tbody>
					</table>
					<div id="channel_tip" class="alert-danger" style="display: none;"></div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="spectrumSideChinese" id="sideArrow">
		<div class="cmListSideArrLeft" id="arrow"></div>
	</div>
</body>
</Zeta:HTML>