<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module onu
	import js.tools.numberInput
	css js/tools/css/numberInput
</Zeta:Loader>
<head>
<style type="text/css">
.vertical-middle{
	vertical-align: middle;
}
</style>
<script>
	var autoCollectTime = '${autoCollectTime}' != "" ? '${autoCollectTime}' : '3:0' ;
	
	//save config
	function okClick() {
		//获得时间
		var getHour = $("#startHour").val();
		if(getHour){
			if(isNaN(getHour)){
				$("#startHour").attr('tooltip','@ONU.autoCollectTimeNaNTip@');
				$("#startHour").focus();
				return;
			}
			if(getHour < 10){
				getHour = "0" + getHour;
			}
		}else{
			$("#startHour").attr('tooltip','@ONU.autoCollectTimeTip@');
			$("#startHour").focus();
			return;
		}
		var getMinute = $("#startMin").val();
		if(getMinute){
			if(isNaN(getMinute)){
				$("#startHour").attr('tooltip','@ONU.autoCollectTimeNaNTip@');
				$("#startHour").focus();
				return;
			}
			if(getMinute < 10){
				getMinute = "0" + getMinute;
			}
		}else{
			$("#startMin").attr('tooltip','@ONU.autoCollectTimeTip@');
			$("#startMin").focus();
			return;
		}
		var getTime = getHour + ":" + getMinute;
		//保存用户的修改
		$.ajax({
			url : '/onu/onoffrecord/saveOnOffRecordConfig.tv',
			type : 'POST',
			data : {
				autoCollectTime : getTime
			},
			success : function() {
				top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@ONU.saved@</b>'
   			});
				cancelClick();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@","@ONU.saveFailed@");
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('onuONOffRecordConfigDlg');
	}

	$(function() {
		//构建时间控件
		new NumberInput("startHour", "startHourDiv", 3, 0, 23);
		new NumberInput("startMin", "startMinDiv", 0, 0, 59);
		
		//先解析出时间，然后显示
		var time = autoCollectTime;
		$("#startHour").val(Number(time.split(":")[0]));
		$("#startMin").val(Number(time.split(":")[1]));
	});
</script>
	</head>
	<body class="openWinBody">
			<div class="openWinHeader">
				<div class="openWinTip">@ONU.onOffRecordTimeConfig@</div>
				<div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT40">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="219">
	            				<label 	for="autoWriteTime">@ONU.onOffRecordCollectTime@:</label>
	            			</td>
	            			<td class="vertical-middle">
	            			<div style="display: inline-table;" class="timePeriod vertical-middle"></div>
								<span class="spanWidth30 timePeriod mR10 vertical-middle">@ONU.lable.d@</span>
								<div id="startHourDiv" style="display: inline-table;" class="timePeriod vertical-middle"></div>
								<span class="spanWidth30 timePeriod mR10 vertical-middle">@ONU.label.h@</span>
								<div id="startMinDiv" style="display: inline-table;" class="timePeriod vertical-middle"></div>
								<span class="spanWidth30 timePeriod mR10 vertical-middle">@ONU.lable.m@</span>
	            			</td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT80 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()" id="autoClearSubmit"> 
							<span> 
								<i class="miniIcoData"> </i> @COMMON.save@
							</span>
						</a></li>
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="cancelClick()"> 
							<span> 
								<i class="miniIcoForbid"> </i>@COMMON.cancel@
							</span>
						</a></li>
					</ol>
				</div>
			</div>
	</body>
</Zeta:HTML>