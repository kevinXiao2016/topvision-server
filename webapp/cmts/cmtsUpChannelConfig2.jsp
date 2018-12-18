<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module cmc
    css /white/disabledStyle
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>

<script type="text/javascript">
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 5.0;
var _frequencyMax = 65.0;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var cmcId = <s:property value="cmcId"/>;
var cmcPortId = <s:property value="cmcPortId"/>;
var channelIndex = '<s:property value="channelIndex"/>';
var upChannelWidth = <s:property value="cmcUpChannelBaseShowInfo.channelWidth"/>;
var cmtsChannelModulationProfile = <s:property value="cmcUpChannelBaseShowInfo.cmtsChannelModulationProfile"/>;
var ifAdminStatus = '<s:property value="cmcUpChannelBaseShowInfo.ifAdminStatus"/>';
var ifName = '<s:property value="cmcUpChannelBaseShowInfo.ifName"/>';
var channelListObject = ${channelListObject};
var cmtsModTypeList = ${cmtsModTypeList};
var productType ='<s:property value="productType"/>';
var upChannelFrequery = <s:property value="cmcUpChannelBaseShowInfo.channelFrequency"/>/1000000;
var upChannelPower = <s:property value="cmcUpChannelBaseShowInfo.upChannelPower"/>;
//初始化电平的最大值最小值
var powerMax;
var powerMin;
//频率提示范围 
var frequencyMinTip;
var frequencyMaxTip;

//初始化电平的最大值 
powerMax = parsePower(26);
powerMin = parsePower(-13);

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == '@spectrum/spectrum.dbuv@' && powerValue != ''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

//数字验证
function isFloatNumber(number){
	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$|^\-\d+\.\d{1}$|^\-\d+$|^0\.\d{1}$|^0$/;
	return reg.test(number);
}
function is0_6BitsNumber(number){
	var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('cmtsUpStreamConfig');
}
function frequencyCheck(min, max, frequency, width){
	var i = 0;
	if(frequency < min || frequency > max)
		return false;
	for(i = 0; i < channelListObject.length; i++){
		if(frequency <= channelListObject[i].channelFrequency)
			break;
	}
	if(i==0){
		if(channelListObject.length!=0){
			if((frequency+width/2)> (channelListObject[i].channelFrequency - channelListObject[i].channelWidth/2))
				return false;
		}
	}else if( i < channelListObject.length){
		if(frequency+width/2 > channelListObject[i].channelFrequency - channelListObject[i].channelWidth/2||
		frequency-width/2 < channelListObject[i-1].channelFrequency + channelListObject[i-1].channelWidth/2)
			return false;
	}else{
		if(frequency-width/2 < channelListObject[i-1].channelFrequency + channelListObject[i-1].channelWidth/2)
			return false;
	}
	return true;	
}

function changeSaveButton(){
	if(ifName != $("#ifName").val() || 
			ifAdminStatus != $("#ifAdminStatus").val() 
			|| upChannelFrequery != Zeta$('upChannelFrequery').value
			|| upChannelWidth != Zeta$('upChannelWidth').value
			|| Zeta$('profile').value != cmtsChannelModulationProfile
			|| upChannelPower != Zeta$('docsIf3SignalPower').value) {
		R.saveBtn.setDisabled(false);
	} else {
		R.saveBtn.setDisabled(true);
	}
}


function onWidthChange(domObject){
    var width = parseInt($(domObject).val());
    frequencyMinTip = _frequencyMin + width/2000000;
    frequencyMaxTip = _frequencyMax - width/2000000;
    $("#upChannelFrequery").attr("toolTip",frequencyMinTip + '-' + frequencyMaxTip);
    changeSaveButton();
}
function saveClick() {
	//频道带宽
	/* object = Zeta$('upChannelWidth').value.trim();
	var channelWidth = new Number(object); */
    //电平
	object = Zeta$('docsIf3SignalPower').value.trim();
	if (object != '' && !isFloatNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerFormatTip);
		return;
	}else if(object < powerMin || object > powerMax){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerRangeTip+ powerMin +  '-' + powerMax);
		return;
	} 
	//中心频率
	object = Zeta$('upChannelFrequery').value.trim();
	var frequency = new Number(object) * 1000000;
	if (object != '' && !is0_6BitsNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyFormatTip);
		return;
	}else if(object<frequencyMinTip || object > frequencyMaxTip){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyRangeTip + frequencyMinTip + '-' + frequencyMaxTip);
		return;
	} 
	/* if(ifAdminStatus==1||$("#ifAdminStatus").val()==1){
		if( !frequencyCheck(_frequencyMin * 1000000,_frequencyMax * 1000000,frequency,channelWidth)){
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.conflictTip);
			return;
		}
	} */
	var tipStr = I18N.text.confirmModifyTip;
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, tipStr, function (type) {
	if(type=="ok"){
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
  	      url: '/cmts/channel/modifyUpStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+"&productType="+productType,
  	      type: 'post',
  	      data: jQuery(formChanged).serialize(),
  	      success: function(response) {
    	    	if(response.message == "success"){
    	    		window.top.closeWaitingDlg();
    	    		top.afterSaveOrDelete({
    	   				title: I18N.RECYLE.tip,
    	   				html: '<b class="orangeTxt">'+I18N.text.modifySuccessTip+'</b>'
    	   			});
				 }else{
					 window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
				 }
				window.parent.getFrame("entity-" + cmcId).onRefreshClick();
				cancelClick();
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
			}, cache: false
		});
		}
	});
}

$(document).ready(function(){
	frequencyMinTip = _frequencyMin + upChannelWidth/2000000;
    frequencyMaxTip = _frequencyMax - upChannelWidth/2000000;
    $("#upChannelFrequery").attr("toolTip",frequencyMinTip + '-' + frequencyMaxTip);
    
  	 //power tootip
	 var powerTooltip = powerMin + '-' + powerMax;
	 $("#docsIf3SignalPower").attr("toolTip",powerTooltip);
	 //$("#saveBtn").attr('disabled','disabled');
})

function authLoad(){
	$("#profile option").remove();
	 for(var i=0;i<cmtsModTypeList.length;i++){
		var modIndex = cmtsModTypeList[i].docsIfCmtsModIndex;
		var modType = cmtsModTypeList[i].modTypeString;
		var s = "<option value='"+ modIndex +"'>" + modIndex + "(" + modType + ")"+"</option>";
		$("#profile").append(s);
	}  
	$("#profile").val(cmtsChannelModulationProfile);
	
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.saveBtn.setDisabled(true);
	}
}

</script>
<title>@text.upChannelConfig@</title>
</head>
	<body class="openWinBody" onload="authLoad()">
		<div class="openWinHeader">
			<div class="openWinTip"></div>
			<div class="rightCirIco upArrCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT10">
			<form name="formChanged" id="formChanged">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0" id="zebraTable">
					<tr>
						<td class="rightBlueTxt" width="150">@CHANNEL.channelName@</td>
						<td width="200"><input class="normalInput w165" id='ifName' onchange="changeSaveButton()"
							name="cmcUpChannelBaseShowInfo.ifName"
							value="<s:property value="cmcUpChannelBaseShowInfo.ifName"/>"/></td>
						<td class="rightBlueTxt">@CCMTS.channel.adminStatus@</td>
						<td><select id="ifAdminStatus"
							name="cmcUpChannelBaseShowInfo.ifAdminStatus"
							class="normalSel w165" onchange="changeSaveButton()">
								<option value="1"
									<s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>>@CMC.select.open@</option>
								<option value="2"
									<s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>>@CMC.select.close@</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CHANNEL.frequency@(MHz)</td>
						<td><input class="normalInput w165" id="upChannelFrequery"
							name="cmcUpChannelBaseShowInfo.docsIfUpChannelFrequencyForunit"
							onchange="changeSaveButton()"
							value="<s:property value='%{cmcUpChannelBaseShowInfo.channelFrequency*1.0/1000000}'/>" />
						</td>
						<td class="rightBlueTxt">@CMC.label.bandwidth@(MHz)</td>
						<td><select id="upChannelWidth" class="normalSel w165"
							name="cmcUpChannelBaseShowInfo.channelWidth"
							onchange="onWidthChange(this)">
								<option value="1600000"
									<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==1600000">selected</s:if>>1.6</option>
								<option value="3200000"
									<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==3200000">selected</s:if>>3.2</option>
								<option value="6400000"
									<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==6400000">selected</s:if>>6.4</option>
						</select></td>
					</tr>
					<tr>
						<td class="rightBlueTxt" width="150">
							@CHANNEL.modulationProfile@</td>
						<td><select id="profile" class="normalSel w165"
							name="cmcUpChannelBaseShowInfo.channelModulationProfile"
							onchange="changeSaveButton()">
						</select>
						</td>
						<td class="rightBlueTxt">@CHANNEL.power@(@{unitConfigConstant.elecLevelUnit}@)</td>
						<td><input id="docsIf3SignalPower" class="normalInput w165"
							name="cmcUpChannelBaseShowInfo.upChannelPower" tooltip =''
							onchange="changeSaveButton()"
							value="<s:property value="cmcUpChannelBaseShowInfo.upChannelPower"/>" />
						</td>
					</tr>
					<input type="hidden" id="channelType" 
							name="cmcUpChannelBaseShowInfo.channelType"
							value="<s:property value="cmcUpChannelBaseShowInfo.channelType"/>" />
				</table>
			</form>
		</div>

		<Zeta:ButtonGroup>
			<Zeta:Button onclick="saveClick()" disabled="true" icon="miniIcoSave" id="saveBtn">@BUTTON.apply@</Zeta:Button>
			<Zeta:Button onclick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>