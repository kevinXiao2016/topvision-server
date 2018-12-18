<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>

<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resources"/>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="network"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript">
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 5.0;
var _frequencyMax = 65.0;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var cmcId = <s:property value="cmcId"/>;
var cmcPortId = <s:property value="cmcPortId"/>;
var upChannelWidth = <s:property value="cmcUpChannelBaseShowInfo.channelWidth"/>;
var upChannelFrequery = <s:property value="cmcUpChannelBaseShowInfo.channelFrequency"/>/1000000;
var upChannelModulationProfile = <s:property value="cmcUpChannelBaseShowInfo.channelModulationProfile"/>;
var ifAdminStatus = '<s:property value="cmcUpChannelBaseShowInfo.ifAdminStatus"/>';
var docsIf3SignalPower = <s:property value="cmcUpChannelBaseShowInfo.docsIf3SignalPower"/>/10;
var channelMode = '${cmcUpChannelBaseShowInfo.channelExtMode}';
var channelListObject = ${channelListObject};
var productType = <s:property value="productType"/>;
var rangingBackoffStart = ${cmcUpChannelBaseShowInfo.channelRangingBackoffStart};
var rangingBackoffEnd = ${cmcUpChannelBaseShowInfo.channelRangingBackoffEnd};
var txBackoffStart =${cmcUpChannelBaseShowInfo.channelTxBackoffStart};
var txBackoffEnd = ${cmcUpChannelBaseShowInfo.channelTxBackoffEnd}
var powerMax;
var powerMin = -13;
//频率提示范围 
var frequencyMinTip;
var frequencyMaxTip;

//初始化电平的最大值 
powerMax = 26;

function listTypeChanged(){
	
}
//数字验证
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
}
function isFloatNumber(number){
	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$|^\-\d+\.\d{1}$|^\-\d+$|^0\.\d{1}$|^0$/;
	return reg.test(number);
}
function is0_6BitsNumber(number){
	var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
    return reg.test(number);
}
function isNumber(number){
	var reg =  /^[0-9]+$/;///^[1-9]+[0-9]*]*$/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('upStreamConfig');
}
function changeCheck(){
	var changeTag = false;
	if(upChannelWidth != $("#upChannelWidth").val() || upChannelFrequery != $("#upChannelFrequery").val()||
		upChannelModulationProfile != $("#profile").val()||docsIf3SignalPower!= $("#docsIf3SignalPower").val()||
		ifAdminStatus!= $("#ifAdminStatus").val()|| rangingBackoffStart!=$("#rangingBackoffStart").val()||
		rangingBackoffEnd!=$("#rangingBackoffEnd").val()||txBackoffStart!=$("#txBackoffStart").val()||
		txBackoffEnd!=$("#txBackoffEnd").val()||channelMode !=$("#channelExtMode").val()
		){
		changeTag = true;
	}else{
		changeTag = false;
	}
	if(changeTag == true){
		$("#saveBtn").attr("disabled", false);
	}else{
		$("#saveBtn").attr("disabled", true);
	}
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
function onWidthChange(domObject){
    var width = parseInt($(domObject).val());
    frequencyMinTip = _frequencyMin + width/2000000;
    frequencyMaxTip = _frequencyMax - width/2000000;
    inputFocused('upChannelFrequery', frequencyMinTip + '-' + frequencyMaxTip, 'iptxt_focused');
}
function saveClick() {
	//频道带宽
	object = Zeta$('upChannelWidth').value.trim();
	var channelWidth = new Number(object);

    //电平
	object = Zeta$('docsIf3SignalPower').value.trim();
	if ( object != '' && !isFloatNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerFormatTip);
		return;
	}else if(object< powerMin || object > powerMax){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerRangeTip+ powerMin +  '-' + powerMax);
		return;
	}

	//中心频率
	object = Zeta$('upChannelFrequery').value.trim();
	var frequency = new Number(object) * 1000000;
	if ( object != '' && !is0_6BitsNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyFormatTip);
		return;

	}else if(object<frequencyMinTip || object > frequencyMaxTip){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyRangeTip + frequencyMinTip + '-' + frequencyMaxTip);
		return;
	}
	if(ifAdminStatus==1||$("#ifAdminStatus").val()==1){
		if( !frequencyCheck(_frequencyMin * 1000000,_frequencyMax * 1000000,frequency,channelWidth)){
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.conflictTip);
			return;
		}
	}
	//测距，数据偏移
	object = Zeta$('rangingBackoffStart').value.trim();
	//alert(!(object>=0&&16>=object)+"/"+(object != '' && !isNumber(object)));
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CHANNEL.rangingBackoffStart+I18N.CHANNEL.numberLimit);
		return;
	}
	object = Zeta$('rangingBackoffEnd').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CHANNEL.rangingBackoffEnd+I18N.CHANNEL.numberLimit);
		return;
	}
	object = Zeta$('txBackoffStart').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CHANNEL.txBackoffStart+I18N.CHANNEL.numberLimit);
		return;
	}
	object = Zeta$('txBackoffEnd').value.trim();
	if (object==null||object==''||(object != '' && !isNumber(object))||!(object>=0&&15>=object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CHANNEL.txBackoffEnd+I18N.CHANNEL.numberLimit);
		return;
	}
	var tipStr = I18N.text.confirmModifyTip;
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, tipStr, function (type) {
	if(type=="ok"){
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
  	      url: '/cmc/channel/modifyUpStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+"&productType="+productType,
  	      type: 'post',
  	      data: jQuery(formChanged).serialize(),
  	      success: function(response) {
				//window.top.getFrame('cmcId1000').onRefreshClick();
				//在topvision-server/webapp/epon/onuView.jsp中找到
				response = eval("(" + response + ")");
    	    	if(response.message == "success"){    	    		
    	    		window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifySuccessTip);
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
	$("#upChannelFrequery").focus();
})

function authLoad(){
	var ids = new Array();
	ids.add("profile");
	ids.add("upChannelWidth");
	ids.add("upChannelFrequery");
	ids.add("ifAdminStatus");
	ids.add("docsIf3SignalPower");
	operationAuthInit(operationDevicePower,ids);
}
</script>
<TITLE><fmt:message bundle="${cmc}" key="text.upChannelConfig"/></TITLE>
</HEAD>
<body class=POPUP_WND style="padding-top: 15px; padding-left: 15px; padding-right: 15px; padding-bottom: 15px" onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<fieldset style="background-color: #ffffff; width: 600px;height:270px;" align="center">
	<table width=100% height="85%" cellspacing=0 cellpadding=0 bgcolor="white">
		<tr>
			<td align=center style="margin-bottom: 15px; margin-top: 15px;">
				<form name="formChanged" id="formChanged">
					<table cellspacing=5 cellpadding=0>
						<tr>
							<td><input class=iptxt
								name="cmcUpChannelBaseShowInfo.channelIndex"
								style="width: 150px; align: left" type="hidden"
								value=<s:property value="cmcUpChannelBaseShowInfo.channelIndex"/>>
							</td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.channel"/></td>
							<td><input disabled class=iptxt
								name="cmcUpChannelBaseShowInfo.ifDescr"
								style="width: 150px; align: center"
								value=<s:property value="cmcUpChannelBaseShowInfo.ifDescr"/>>
							</td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.modulationProfile"/></td>
							<td><select id="profile" style="width: 150px;"
								name="cmcUpChannelBaseShowInfo.channelModulationProfile"
								onclick="changeCheck()">
									<option value="1"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==1">selected</s:if>>QPSK-Fair-Scdma</option>
									<option value="2"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==2">selected</s:if>>QAM16-Fair-Scdma</option>
									<option value="3"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==3">selected</s:if>>QAM64-Fair-Scdma</option>
									<option value="4"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==4">selected</s:if>>QAM256-Fair-Scdma</option>
									<option value="5"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==5">selected</s:if>>QPSK-Good-Scdma</option>
									<option value="6"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==6">selected</s:if>>QAM16-Good-Scdma</option>
									<option value="7"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==7">selected</s:if>>QAM64-Good-Scdma</option>
									<option value="8"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==8">selected</s:if>>QAM256-Good-Scdma</option>
									<option value="9"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==9">selected</s:if>>QAM64-Best-Scdma</option>
									<option value="10"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==10">selected</s:if>>QAM256-Best-Scdma</option>
									<option value="11"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==11">selected</s:if>>QPSK-Good-Atdma</option>
									<option value="12"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==12">selected</s:if>>QAM16-Good-Atdma</option>
									<option value="13"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==13">selected</s:if>>QAM64-Good-Atdma</option>
									<option value="14"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelModulationProfile==14">selected</s:if>>QAM256-Good-Atdma</option>
							</select></td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.snr"/>(dB)</td>
							<td><input disabled="disabled" class=iptxt type=text
								name="cmcUpChannelBaseShowInfo.docsIfSigQSignalNoiseForunit"
								style="width: 150px; align: center"
								value="<s:property value='%{cmcUpChannelBaseShowInfo.docsIfSigQSignalNoise*1.0/10}'/>" /></td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CMC.label.bandwidth"/>(MHz)</td>
						<td><select id="upChannelWidth" style="width: 150px;"
								name="cmcUpChannelBaseShowInfo.channelWidth"
								onchange="onWidthChange(this)"
								onclick="changeCheck()">
									<option value="1600000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==1600000">selected</s:if>>1.6</option>
									<option value="3200000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==3200000">selected</s:if>>3.2</option>
									<option value="6400000"
										<s:if test="#request.cmcUpChannelBaseShowInfo.channelWidth==6400000">selected</s:if>>6.4</option>
								</select>
							</td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.frequency"/>(MHz)</td>
							<td><input class=iptxt type=text id=upChannelFrequery
								name="cmcUpChannelBaseShowInfo.docsIfUpChannelFrequencyForunit"
								style="width: 150px; align: center"
								value="<s:property value='%{cmcUpChannelBaseShowInfo.channelFrequency*1.0/1000000}'/>"
								onfocus="inputFocused('upChannelFrequery', frequencyMinTip + '-' + frequencyMaxTip, 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');" onkeyup="changeCheck()"
								onclick="clearOrSetTips(this);"></td>
						<td width=81px align=right><fmt:message bundle="${cmc}" key="CCMTS.channel.adminStatus"/></td>
							<td><select id="ifAdminStatus" style="width: 150px;" name="cmcUpChannelBaseShowInfo.ifAdminStatus" 
							onclick="changeCheck()">
	                        <option value="1" <s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>><fmt:message bundle="${cmc}" key="CMC.select.open"/></option>
	                        <option value="2" <s:if test="#request.cmcUpChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>><fmt:message bundle="${cmc}" key="CMC.select.close"/></option>
	                      	<!-- <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==3">selected</s:if>>testing</option> -->
	                      	</select></td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.rangingBackoffStart"/></td>
							<td><input class=iptxt type=text
								id=rangingBackoffStart
								name="cmcUpChannelBaseShowInfo.channelRangingBackoffStart"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelRangingBackoffStart"/>'
								onfocus="inputFocused('rangingBackoffStart', '0-15', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"
								onkeyup="changeCheck()">
							</td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.rangingBackoffEnd"/></td>
							<td><input class=iptxt type=text
								id=rangingBackoffEnd
								name="cmcUpChannelBaseShowInfo.channelRangingBackoffEnd"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelRangingBackoffEnd"/>'
								onfocus="inputFocused('rangingBackoffEnd', '0-15', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"
								onkeyup="changeCheck()">
							</td>
							
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.txBackoffStart"/></td>
							<td><input class=iptxt type=text id=txBackoffStart
								name="cmcUpChannelBaseShowInfo.channelTxBackoffStart"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelTxBackoffStart"/>'
								onfocus="inputFocused('txBackoffStart', '0-15', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"
								onkeyup="changeCheck()">
							</td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.txBackoffEnd"/></td>
							<td><input class=iptxt type=text id=txBackoffEnd
								name="cmcUpChannelBaseShowInfo.channelTxBackoffEnd"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelTxBackoffEnd"/>'
								onfocus="inputFocused('txBackoffEnd', '0-15', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);"
								onkeyup="changeCheck()">
							</td>
						</tr>
						
						<tr>
							<td align=right><fmt:message bundle="${cmc}" key="CHANNEL.power"/>(dBmV)</td>
                            <td style="width: 150px;"><input id="docsIf3SignalPower" style="width: 150px;"
                                name="cmcUpChannelBaseShowInfo.docsIf3SignalPowerForunit"
                                onkeyup="changeCheck()"
                                onfocus="inputFocused('docsIf3SignalPower', '-13.0-26.0', 'iptxt_focused')"
                                onblur="inputBlured(this, 'iptxt');"
                                value=<s:property value="%{cmcUpChannelBaseShowInfo.docsIf3SignalPower*1.0/10}"/> >
                            </td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.preEqEnable"/></td>
                            <td style="width: 150px;">
                            <input disabled class=iptxt type=text id=channelPreEqEnable
								name="cmcUpChannelBaseShowInfo.channelPreEqEnable"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.docsIfUpChannelPreEqEnableName"/>'>
                            </td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.channelType"/></td>
							<td style="width: 150px;"><input disabled 
								style="width: 150px;"
								name="cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName"
								value=<s:property value="cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName"/>>
							</td>	
							<td align=right><fmt:message bundle="${cmc}" key="CHANNEL.extChannelMode"/></td>
                            <td style="width: 150px;">
	                            <select  id="channelExtMode"
	                                style="width: 150px;"
	                                name="cmcUpChannelBaseShowInfo.channelExtMode"
	                                onclick="changeCheck()">
	                                    <option value="2"
	                                        <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==2">selected</s:if>>V2</option>
	                                    <option value="3"
	                                        <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==3">selected</s:if>>V3</option>
	                                    <!-- <option value="4"
	                                        <s:if test="#request.cmcUpChannelBaseShowInfo.channelExtMode==4">selected</s:if>>Other</option> -->
	                            </select>
                            </td>											
						</tr>
						<s:if test="#request.cmcUpChannelBaseShowInfo.docsIfUpChannelTypeName=='SCDMA'">
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.activeCodes"/></td>
							<td><input disabled id='activeCode' class=iptxt type=text
								name="cmcUpChannelBaseShowInfo.channelScdmaActiveCodes"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaActiveCodes"/>'
								onfocus="inputFocused('activeCode', '0|64..66|68..70|72|74..78|80..82|84..88|90..96|98..100|102|104..106|108|110..112|114..126|128', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);">
							</td>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.codesPerSlot"/></td>
							<td><input disabled class=iptxt type=text id=codesPerSlot
								name="cmcUpChannelBaseShowInfo.channelScdmaCodesPerSlot"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaCodesPerSlot"/>'
								onfocus="inputFocused('codesPerSlot', '0,2-32', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);">
							</td>
						</tr>
						<tr>
							<td width=81px align=right><fmt:message bundle="${cmc}" key="CHANNEL.frameSize"/></td>
							<td><input disabled class=iptxt type=text id=frameSize
								name="cmcUpChannelBaseShowInfo.channelScdmaFrameSize"
								style="width: 150px; align: center"
								value='<s:property value="cmcUpChannelBaseShowInfo.channelScdmaFrameSize"/>'
								onfocus="inputFocused('frameSize', '0-32', 'iptxt_focused')"
								onblur="inputBlured(this, 'iptxt');"
								onclick="clearOrSetTips(this);">
							</td>			
						</tr>
						<tr>
							
						</tr>
						</s:if>
					</table>
				</form></td>
		</tr>
	</table>
	</fieldset>	
	<div align=right style="padding-top: 10px;">
		<%-- <button id=saveBtn class=BUTTON75 disabled
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="saveClick()"><fmt:message bundle="${resources}" key="COMMON.save"/></button>&nbsp; --%>
		<button class=BUTTON75
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle="${resources}" key="COMMON.close"/></button>
	</div>
</BODY>
</HTML>
