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
var cmcId = <s:property value="cmcId"/>;
var cmcPortId = <s:property value="cmcPortId"/>;
var productType ='<s:property value="productType"/>';
var downChannelId = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/>;
var downChannelPower = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelPower"/>/10;
var downChannelFrquence = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency"/>/1000000;
var downChannelModulation = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation"/>;
var annex = '<s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex"/>';
var ifName = '<s:property value="cmcDownChannelBaseShowInfo.ifName"/>';
var ifAdminStatus = '<s:property value="cmcDownChannelBaseShowInfo.ifAdminStatus"/>';
//从后台传递过来的ChannelList需要按照frequency保持升序排序
var channelListObject = ${channelListObject};
var adminStatusUpNum = <s:property value="adminStatusUpNum" />;
var maxChanPowerLUT = [60, 60, 56, 54, 52, 51, 50, 49, 49, 48, 48, 47, 47, 46, 46, 45, 45];
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 52.0;
var _frequencyMax = 1000.0;
var isOverlap = false;
var isExceed = false;
//频率边界值提示 目前不支持带宽设置，如果之后支持带宽设置，需要重新测试该功能 
var frequencyMinTip;
var frequencyMaxTip;

//2016-2-22 EMS-11511 【EMS-V2.6.0.0】：cisco cmts设备的下行信道基本信息配置中，电平值的可配置范围与设备上实际可配的范围不一致 确认再将范围改大为44-63
var maxChanPower = parsePower(63);
var minChanPower = parsePower(44);

//2015-12-10 内蒙王凯要求将大C的下行电平范围修改为固定的45-62dBmV
//var maxChanPower = parsePower(maxPower(adminStatusUpNum, ifAdminStatus));
//var minChanPower = parsePower(minPower(ifAdminStatus));

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == '@spectrum/spectrum.dbuv@' && powerValue != ''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

//强制保留1位小数
function toDecimal1(x){
	var f = parseFloat(x);
	if(isNaN(f)){
		return false;
	}
	var f = Math.round(x*10)/10;
	var s = f.toString();
	var rs = s.indexOf('.');
	if(rs < 0){
		rs = s.length;
		s += '.';
	}
	while(s.length <= rs + 1){
		s += '0';
	}
	return s;
}

//数字验证
function isFloatNumber(number){
	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$/;
	return reg.test(number);
}

function is0_6BitsNumber(number){
    var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
    return reg.test(number);
}

function cancelClick() {
	window.top.closeWindow('cmtsDownStreamConfig');
}

function minPower(adminStatus){
	var i = 1;
	if(channelListObject.length==0||adminStatus == 2){
		return toDecimal1(17);
	} else {
		var power = channelListObject[0].docsIfDownChannelPower;
		for(; i < channelListObject.length; i++){
			if(power < channelListObject[i].docsIfDownChannelPower)
				power = channelListObject[i].docsIfDownChannelPower;
		}
		power = Math.abs(power - 80);
		if (power < 170){
			power = 170;
		}
		return toDecimal1(power/10);
	}
}

function maxPower(upNum, adminStatus){
	var i = 1;
	if(channelListObject.length==0||adminStatus == 2){
		return toDecimal1(60);
	} else {
		var power = channelListObject[0].docsIfDownChannelPower;
		for(; i < channelListObject.length; i++){
			if(power > channelListObject[i].docsIfDownChannelPower){
				power = channelListObject[i].docsIfDownChannelPower;
			}
		}
		power = Math.abs(power + 80);
		if (power > maxChanPowerLUT[upNum]*10){
			power = maxChanPowerLUT[upNum]*10;
		}
		return toDecimal1(power/10);
	}
}

function adminStatusCheck(){
	var i = 0;
	if(ifAdminStatus != $("#ifAdminStatus").val() && $("#ifAdminStatus").val() == 1){
		for(i = 0; i <channelListObject.length; i++){
			if(channelListObject[i].ifAdminStatus == 1 && channelListObject[i].docsIfDownChannelPower/10 > maxChanPower){
				return channelListObject[i].docsIfDownChannelId;
			}
		}
	}
	return 0;
}

function changeSaveButton(){
	if(ifName != $("#ifName").val() || 
			ifAdminStatus != $("#ifAdminStatus").val() 
			|| downChannelFrquence != Zeta$('downChannelFrequery').value
			|| downChannelPower != Zeta$('downChannelPower').value 
			|| downChannelModulation != Zeta$('profile').value) {
		R.saveBtn.setDisabled(false);
	} else {
		R.saveBtn.setDisabled(true);
	}
}

function saveClick() {
	var i;
	//中心频率
	object = Zeta$('downChannelFrequery').value.trim();
	var adminStatus = Zeta$('ifAdminStatus').value.trim();
	var frequency = new Number(object) * 1000000;
	if (object != '' && !is0_6BitsNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyFormatTip);
		return;
	}else if(object<frequencyMinTip||object>frequencyMaxTip){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyRangeTip + frequencyMinTip + '-' + frequencyMaxTip);
		return;
	}
	//电平
	object = Zeta$('downChannelPower').value.trim();
	//var power = new Number(object)*10;
	if (!isFloatNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerFormatTip);
		return;
	}
	if(parseFloat(object) < minChanPower || parseFloat(object) > maxChanPower){
		var tipStr = I18N.text.powerRangeTip + minChanPower + '-'+ maxChanPower;
		window.top.showMessageDlg(I18N.RECYLE.tip, tipStr);
		return;
	}
	if (ifAdminStatus != adminStatus && adminStatus == 1){
		i = adminStatusCheck();
		var tipStr = I18N.CHANNEL.channel + i + I18N.text.powerOverMaxValueTip + maxChanPower + ', ' + I18N.text.modifyPowerAndOpenChannelTip;
		if(i < 17 && i > 0){
			window.top.showMessageDlg(I18N.RECYLE.tip, tipStr);
			return;
		}
	}
	var tipStr = I18N.text.confirmModifyTip;
	if(downChannelFrquence != $("#downChannelFrequery").val()){
		tipStr = I18N.text.modifyFrequencyConfirmTip;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, tipStr, function (type) {
	if(type=="ok"){
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
        url: '/cmts/channel/modifyDownStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+'&productType='+productType,
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
	})
	}
	});
}

$(document).ready(function(){
	if(annex == 3){
		//欧标，频宽 8MHz
        frequencyMinTip = _frequencyMin + 4;
        frequencyMaxTip = _frequencyMax - 4;
    }else if(annex == 4){
        //美标，频宽6MHz
        frequencyMinTip = _frequencyMin + 3;
        frequencyMaxTip = _frequencyMax - 3;
        $("#channelInterleaveSelect option[value='8']").remove();  
        $('#channelInterleaveSelect').attr('disabled',false);
    }else{
        //其他Annex不对边界值进行修改 
        frequencyMinTip = _frequencyMin;
        frequencyMaxTip = _frequencyMax;
    }
	//$("#downChannelFrequery").focus();
	$("#downChannelFrequery").attr("toolTip",frequencyMinTip + '-' + frequencyMaxTip);
	$("#downChannelPower").attr("toolTip",minChanPower + '-' + maxChanPower);
})

function authLoad(){
	if(!operationDevicePower){
	    $(":input").attr("disabled",true);
	    R.saveBtn.setDisabled(true);
	}
}
</script>
	</head>
	<body class="openWinBody" onload="authLoad()">
		<div class="openWinHeader">
			<div class="openWinTip"></div>
			<div class="rightCirIco downArrCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT10">
			<form name="formChanged" id="formChanged">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0" id="zebraTable">
					<tr>
						<td class="rightBlueTxt" width="150">@CHANNEL.channelName@</td>
						<td width="200"><input class="normalInput w165" id='ifName' onchange="changeSaveButton()"
							name="cmcDownChannelBaseShowInfo.ifName"
							value="<s:property value="cmcDownChannelBaseShowInfo.ifName"/>"/></td>

						<td class="rightBlueTxt">@CCMTS.channel.adminStatus@</td>
						<td><select id="ifAdminStatus"
							name="cmcDownChannelBaseShowInfo.ifAdminStatus"
							onchange="changeSaveButton()"
							class="normalSel w165">
								<option value="1"
									<s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>>@CMC.select.open@</option>
								<option value="2"
									<s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>>@CMC.select.close@</option>
						</select></td>
					</tr>

					<tr>
						<td class="rightBlueTxt">@CHANNEL.frequency@(MHz)</td>
						<td><input class="normalInput w165" id="downChannelFrequery"
							onchange="changeSaveButton()"
							name="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequencyForunit"
							value="<s:property value='%{cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency*1.0/1000000}'/>" 
							/>
							<!-- 	
                            onblur="inputBlured(this, 'iptxt');"
                           ></td> --></td>

						<td class="rightBlueTxt">@CHANNEL.power@(@{unitConfigConstant.elecLevelUnit}@)</td>
						<td><input id="downChannelPower" class="normalInput w165"
							name="cmcDownChannelBaseShowInfo.downChannelPower"
							onchange="changeSaveButton()"
							value="<s:property value="cmcDownChannelBaseShowInfo.downChannelPower"/>" />
							<!-- 	onfocus="inputFocused('downChannelPower', minChanPower + '-' + maxChanPower, 'iptxt_focused')"
									onblur="inputBlured(this, 'iptxt');" --></td>
					</tr>

					<tr>
						<td class="rightBlueTxt" width="150">
							@CHANNEL.modulationProfile@</td>
						<td><select id="profile" class="normalSel w165"
							name="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation"
							onchange="changeSaveButton()">
								<option value="3"
									<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==3" >selected</s:if>>QAM64</option>
								<option value="4"
									<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==4" >selected</s:if>>QAM256</option>
								<option value="2"
									<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==2" >selected</s:if>>QAM1024</option>
						</select></td>
						<td class="rightBlueTxt" width="150"></td>
						<td></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onclick="saveClick()" disabled="true" icon="miniIcoSave" id="saveBtn">@BUTTON.apply@</Zeta:Button>
			<Zeta:Button onclick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>