<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
<HEAD>
<TITLE>Modify Downstream Channel Configuration</TITLE>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript"
	src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
var cmcId = <s:property value="cmcId"/>;
var cmcPortId = <s:property value="cmcPortId"/>;
var productType ='<s:property value="productType"/>';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var downChannelId = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/>;
var downChannelPower = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelPower"/>/10;
var downChannelFrquence = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency"/>/1000000;
var downChannelModulation = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation"/>;
var annex = '<s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex"/>';
var downChannelWidth = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelWidth"/>;
var ifAdminStatus = '<s:property value="cmcDownChannelBaseShowInfo.ifAdminStatus"/>';
var downChannelInterleave = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave"/>;

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
//数字验证
function isNotNumber(number){
    var reg = /[^0-9^\-]/;
    return reg.test(number);
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

var maxChanPower = maxPower(adminStatusUpNum, ifAdminStatus);
var minChanPower = minPower(ifAdminStatus);

function isFloatNumber(number){
	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$/;
	return reg.test(number);
}
function is0_6BitsNumber(number){
    var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
    return reg.test(number);
}
function cancelClick() {
	window.top.closeWindow('downStreamConfig');
}
function changeCheck(){
	var changeTag = false;
	if(downChannelPower != $("#downChannelPower").val() || downChannelFrquence != $("#downChannelFrequery").val()
		|| downChannelModulation != $("#modulation").val()|| ifAdminStatus !=$("#ifAdminStatus").val() 
		|| downChannelWidth !=$("#docsIfDownChannelWidth").val()
		||downChannelInterleave!=$("#channelInterleaveSelect").val()
		){
		changeTag = true;
	}else {
		changeTag = false;
	}
	if(changeTag ==true){
		$("#saveBtn").attr("disabled",false);
	}else $("#saveBtn").attr("disabled",true);
	if(ifAdminStatus != $("#ifAdminStatus").val() && 1 == $("#ifAdminStatus").val()){
		maxChanPower = maxPower(adminStatusUpNum+1, 1);
		minChanPower = minPower(1);
		if(maxChanPower < $("#downChannelPower").val()){
			//window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerOverMaxValueTip);
			Zeta$("downChannelPower").focus();
		} else if(minChanPower > $("#downChannelPower").val()){
			//window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerUnderMaxValueTip);
			Zeta$("downChannelPower").focus();
		}
	}else if(ifAdminStatus != $("#ifAdminStatus").val() && 2 == $("#ifAdminStatus").val()){
		minChanPower = toDecimal1(17);
		maxChanPower = toDecimal1(60);
	}else if(ifAdminStatus == $("#ifAdminStatus").val()){
		minChanPower = minPower(ifAdminStatus);
		maxChanPower = maxPower(adminStatusUpNum, ifAdminStatus);
	}
}
function frequencyCheck(frequency, width){
    if(channelListObject.length==0){
        return ;
    }
	isOverlap = false;
	isExceed = false;
	var i = 0;
	var j = -1;
	for(i = 0; i < channelListObject.length; i++){
		if(frequency >= channelListObject[i].docsIfDownChannelFrequency)
			j = i;
		if(Math.abs(frequency - channelListObject[i].docsIfDownChannelFrequency)/1000000 >= 192){
			isExceed = true;
			return channelListObject[i].docsIfDownChannelId;
		}
	}
	j++;
	if(j==0){
		if((frequency+width/2)> (channelListObject[j].docsIfDownChannelFrequency - channelListObject[j].docsIfDownChannelWidth/2)){
			isOverlap = true;
			return channelListObject[j].docsIfDownChannelId;
		}
	}else if( j < channelListObject.length){
		if(frequency-width/2 < channelListObject[j-1].docsIfDownChannelFrequency + channelListObject[j-1].docsIfDownChannelWidth/2){
			isOverlap = true;
			return channelListObject[j-1].docsIfDownChannelId;
		}
		if(frequency+width/2 > channelListObject[j].docsIfDownChannelFrequency - channelListObject[j].docsIfDownChannelWidth/2){
			isOverlap = true;
			return channelListObject[j].docsIfDownChannelId;
		}
	}else{
		if(frequency-width/2 < channelListObject[j-1].docsIfDownChannelFrequency + channelListObject[j-1].docsIfDownChannelWidth/2){
			isOverlap = true;
			return channelListObject[j-1].docsIfDownChannelId;
		}
	}
	return 0;
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
function onAnnexChange(object){
    var val = $(object).val();
    if(val == 3){
        frequencyMinTip = _frequencyMin + 4;
        frequencyMaxTip = _frequencyMax - 4;
    }else if(val == 4){
        frequencyMinTip = _frequencyMin + 3;
        frequencyMaxTip = _frequencyMax - 3;
    }else{
        //其他Annex不对边界值进行修改 
        frequencyMinTip = _frequencyMin;
        frequencyMaxTip = _frequencyMax;
    }
    inputFocused('downChannelFrequery', frequencyMinTip + '-' +frequencyMaxTip, 'iptxt_focused');
}
function saveClick() {
	var i;
	var object = Zeta$('annexMode').value.trim();
	if(object == 3){
		width = 8000000;
	}else width = 6000000;
	//中心频率
	object = Zeta$('downChannelFrequery').value.trim();
	var adminStatus = Zeta$('ifAdminStatus').value.trim();
	var frequency = new Number(object) * 1000000;
	if ( object != '' && !is0_6BitsNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyFormatTip);
		return;
	}else if(object<frequencyMinTip||object>frequencyMaxTip){
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.frequencyRangeTip + frequencyMinTip + '-' + frequencyMaxTip);
		return;
	}
	if (adminStatus == 1){
		i = frequencyCheck(frequency,width);
		if(isOverlap){
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.toChannel + i + I18N.text.frequencyConflictTip);
			return;
		}
		if(isExceed){
			window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.toChannel + i + I18N.text.frequencyOver192MTip);
			return;
		}
	}
	//电平
	object = Zeta$('downChannelPower').value.trim();
	var power = new Number(object)*10;
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
        url: '/cmc/channel/modifyDownStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+'&productType='+productType,
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
	$("#downChannelFrequery").focus();
	
	//初始化频宽和交织深度
	$("#docsIfDownChannelWidth").val(downChannelWidth);
	changeChannelInterleaveSelect();
	$("#channelInterleaveSelect").val(downChannelInterleave);
})

function authLoad(){
	var ids = new Array();
	ids.add("downChannelFrequery");
	ids.add("ifAdminStatus");
	ids.add("modulation");
	ids.add("downChannelPower");
	operationAuthInit(operationDevicePower,ids);
}

//当频宽改变了，交织深度的可选项也随之改变，频宽为8时，交织深度只可选（12.17），当频宽为6时，交织深度可以选其他值
function changeChannelInterleaveSelect(){
	var docsIfDownChannelWidth = $("#docsIfDownChannelWidth").val();
	if(docsIfDownChannelWidth==6000000){
		$("#channelInterleaveSelect option[value='3']").remove();  
		$("#channelInterleaveSelect option[value='4']").remove();
		$("#channelInterleaveSelect option[value='5']").remove();
		$("#channelInterleaveSelect option[value='6']").remove();
		$("#channelInterleaveSelect option[value='7']").remove();
		$("#channelInterleaveSelect option[value='8']").remove();

		$("#channelInterleaveSelect").append("<option value='3'>(8, 16)</option>");  
		$("#channelInterleaveSelect").append("<option value='4'>(16, 8)</option>");  
		$("#channelInterleaveSelect").append("<option value='5'>(32, 4)</option>");  
		$("#channelInterleaveSelect").append("<option value='6'>(64, 2)</option>");  
		$("#channelInterleaveSelect").append("<option value='7'>(128,1)</option>");  
	}else if(docsIfDownChannelWidth==8000000){
		$("#channelInterleaveSelect option[value='3']").remove();  
		$("#channelInterleaveSelect option[value='4']").remove();
		$("#channelInterleaveSelect option[value='5']").remove();
		$("#channelInterleaveSelect option[value='6']").remove();
		$("#channelInterleaveSelect option[value='7']").remove();
		$("#channelInterleaveSelect option[value='8']").remove();
		
		$("#channelInterleaveSelect").append("<option value='8'>(12,17)</option>");  
	}
}
</script>
</HEAD>
<body class=POPUP_WND style="padding-top: 15px; padding-left: 15px;" onload="authLoad()">
<div class=formtip id=tips style="display: none"></div>
	<fieldset
			style="background-color: #ffffff; width: 440px;height:180px;">
	<table cellspacing=5 cellpadding=5 >
		<tr>
			<td align=center style="padding-top: 15px;">
			<form name="formChanged" id="formChanged">
				<table cellspacing=5 cellpadding=0>
					<tr>
						<td><input class=iptxt name="cmcDownChannelBaseShowInfo.channelIndex"
							style="width: 100px; align: center" type="hidden"
							value=<s:property value="cmcDownChannelBaseShowInfo.channelIndex"/> ></td>
					</tr>
					<tr>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.channel'/></td>
						<td><input class=iptxt type=text disabled name="cmcDownChannelBaseShowInfo.ifDescr"
							style="width: 110px; align: center" 
							value=<s:property value="cmcDownChannelBaseShowInfo.ifDescr"/>
							>
						</td>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.frequency'/>(MHz)</td>
                         <td><input class=iptxt type=text id="downChannelFrequery" name="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequencyForunit"
							style="width: 110px; align: center" 
							value='<s:property value="%{cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency*1.0/1000000}"/>'
							onfocus="inputFocused('downChannelFrequery', frequencyMinTip + '-' + frequencyMaxTip, 'iptxt_focused')"
                            onblur="inputBlured(this, 'iptxt');"
                            onkeyup="changeCheck()"
                            onclick="clearOrSetTips(this);"></td>
					</tr>
					<tr>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CCMTS.channel.adminStatus'/></td>
							<td><select id="ifAdminStatus" style="width: 110px;" name="cmcDownChannelBaseShowInfo.ifAdminStatus" 
							onchange="changeCheck()">
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>><fmt:message bundle='${emsRes}' key='COMMON.off'/></option>
	                        <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>><fmt:message bundle='${emsRes}' key='COMMON.on'/></option>
	                      	<!-- <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==3">selected</s:if>>testing</option> -->
	                      	</select></td>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.modulationType'/></td>
						<td style="width: 100px;"><select id="modulation"  name="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation" style="width: 110px;"
						onchange="changeCheck()">
						<!--不支持unknown设置
                        <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==1" >selected</s:if>>unknown</option>
                        -->                        
                        <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==3" >selected</s:if>>QAM64</option>
                        <option value="4" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==4" >selected</s:if>>QAM256</option>
                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelModulation==2" >selected</s:if>>QAM1024</option>  
                        </select>
                        </td>
					</tr>
					<tr>
                        <td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.interleave'/></td>
						<td style="width: 100px;"><select id="channelInterleaveSelect" name="cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave" style="width: 110px;" onchange="changeCheck()">
                        <!-- <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==1">selected</s:if>>unknown</option>
                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==2">selected</s:if>>other</option>    -->
                        <option value="3">(8, 16)</option>
                        <option value="4">(16, 8)</option>
                        <option value="5">(32, 4)</option>
                        <option value="6">(64, 2)</option>
                        <option value="7">(128,1)</option>
                        <option value="8">(12,17)</option>
                        </select>
                        </td>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.power'/>(dBmV)</td>
						<td><input class=iptxt type=text id="downChannelPower" name="cmcDownChannelBaseShowInfo.docsIfDownChannelPowerForunit"
							style="width: 110px; align: center" 
							onkeyup="changeCheck()"
							onfocus="inputFocused('downChannelPower', minChanPower + '-' + maxChanPower, 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							value=<s:property value="%{cmcDownChannelBaseShowInfo.docsIfDownChannelPower*1.0/10}"/>
							></td>
					</tr>
					<tr>
						<td width=80px align=right><fmt:message bundle='${cmcRes}' key='CHANNEL.annex'/></td>
						<td style="width: 100px;"><select disabled id="annexMode" name="cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex" 
						onchange="onAnnexChange(this)" style="width: 110px;"
						>
                        <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==1">selected</s:if>>unknown</option>
                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==2">selected</s:if>>other</option>   
                        <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==3">selected</s:if>>annexA</option>
                        <option value="4" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==4">selected</s:if>>annexB</option>
                        <option value="5" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==5">selected</s:if>>annexC</option>
                        </select>
                        </td>
						<td width=80px align=right><fmt:message bundle="${cmcRes}" key="CMC.label.bandwidth"/>(MHz)</td>
                        <td style="width: 100px;"><select id="docsIfDownChannelWidth" style="width: 110px;"
								name="cmcDownChannelBaseShowInfo.docsIfDownChannelWidth" onchange = "changeChannelInterleaveSelect()"
								onclick="changeCheck()">
									<option value="6000000"
										<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==6000000">selected</s:if>>6</option>
									<option value="8000000"
										<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==8000000">selected</s:if>>8</option>
								</select>
							</td>
					</tr>
					<tr>
					</tr>
				</table>
				</form>
			</td>
		</tr>
	</table>
	</fieldset>
	<div align=right style="padding-top:10px;padding-right:13px;">
		<button id=saveBtn class=BUTTON75
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="saveClick()" disabled><fmt:message bundle="${emsRes}" key="COMMON.save"/></button>&nbsp;
		<button class=BUTTON75
			onMouseOver="this.className='BUTTON_OVER75'"
			onMouseOut="this.className='BUTTON75'"
			onmousedown="this.className='BUTTON_PRESSED75'"
			onclick="cancelClick()"><fmt:message bundle="${emsRes}" key="COMMON.cancel"/></button>
							
	</div>
</BODY>
</HTML>