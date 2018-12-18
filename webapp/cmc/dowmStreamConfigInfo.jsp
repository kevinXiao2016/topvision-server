<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module CMC
</Zeta:Loader>
<title>Modify Downstream Channel Configuration</title>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script src="/js/jquery/nm3kToolTip.js"></script>
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
var cmcDownChannelBaseShowInfo = ${cmcDownChannelBaseShowInfoJson};
//cmcDownChannelBaseShowInfo.txPowerLimit

var supportFunction = ${supportFunction};

var currentUnit = '@{unitConfigConstant.elecLevelUnit}@';

//从后台传递过来的ChannelList需要按照frequency保持升序排序
var channelListObject = ${channelListObject};
var downChannelListObject = channelListObject.downList;
var upChannelListObject = channelListObject.upList;
var adminStatusUpNum = <s:property value="adminStatusUpNum" />;
var maxChanPowerLUT = [60, 60, 56, 54, 52, 51, 50, 49, 49, 48, 48, 47, 47, 46, 46, 45, 45];
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 52.0;
var _frequencyMax = 1002.0;//边界值改为1002.0
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

//电平值转换
function parsePower(powerValue){
	var powerUnit = '@{unitConfigConstant.elecLevelUnit}@';
	if(powerUnit == '@unit.dbuv@' && powerValue != ''){
		return parseFloat(powerValue) + 60;
	}
	return powerValue;
}

var maxChanPower = parsePower(maxPower(adminStatusUpNum, ifAdminStatus));
var minChanPower = parsePower(minPower(ifAdminStatus));

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
		|| downChannelModulation != $("#modulation").val()
		|| downChannelWidth !=$("#docsIfDownChannelWidth").val()
		||downChannelInterleave!=$("#channelInterleaveSelect").val()
		){
		changeTag = true;
	}else {
		changeTag = false;
	}
	if(changeTag ==true){
	    if(operationDevicePower){
			$("#saveBtn").attr("disabled",false);
	    }
	}else $("#saveBtn").attr("disabled",true);
	/* if(ifAdminStatus != $("#ifAdminStatus").val() && 1 == $("#ifAdminStatus").val()){
		maxChanPower = parsePower(maxPower(adminStatusUpNum+1, 1));
		minChanPower = parsePower(minPower(1));
		if(maxChanPower < $("#downChannelPower").val()){
			//window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerOverMaxValueTip);
			Zeta$("downChannelPower").focus();
		} else if(minChanPower > $("#downChannelPower").val()){
			//window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerUnderMaxValueTip);
			Zeta$("downChannelPower").focus();
		}
	}else if(ifAdminStatus != $("#ifAdminStatus").val() && 2 == $("#ifAdminStatus").val()){
		minChanPower = parsePower(toDecimal1(17));
		maxChanPower = parsePower(toDecimal1(60));
	}else if(ifAdminStatus == $("#ifAdminStatus").val()){
		minChanPower = parsePower(minPower(ifAdminStatus));
		maxChanPower = parsePower(maxPower(adminStatusUpNum, ifAdminStatus));
	} */
}
function frequencyCheck(frequency, width){
    if(downChannelListObject.length==0){
        return ;
    }
	isOverlap = false;
	isExceed = false;
	var i = 0;
	var j = -1;
	for(i = 0; i < downChannelListObject.length; i++){
		if(frequency >= downChannelListObject[i].docsIfDownChannelFrequency)
			j = i;
		if(Math.abs(frequency - downChannelListObject[i].docsIfDownChannelFrequency)/1000000 >= 192){
			isExceed = true;
			return downChannelListObject[i].docsIfDownChannelId;
		}
	}
	j++;
	if(j==0){
		if((frequency+width/2)> (downChannelListObject[j].docsIfDownChannelFrequency - downChannelListObject[j].docsIfDownChannelWidth/2)){
			isOverlap = true;
			return downChannelListObject[j].docsIfDownChannelId;
		}
	}else if( j < downChannelListObject.length){
		if(frequency-width/2 < downChannelListObject[j-1].docsIfDownChannelFrequency + downChannelListObject[j-1].docsIfDownChannelWidth/2){
			isOverlap = true;
			return downChannelListObject[j-1].docsIfDownChannelId;
		}
		if(frequency+width/2 > downChannelListObject[j].docsIfDownChannelFrequency - downChannelListObject[j].docsIfDownChannelWidth/2){
			isOverlap = true;
			return downChannelListObject[j].docsIfDownChannelId;
		}
	}else{
		if(frequency-width/2 < downChannelListObject[j-1].docsIfDownChannelFrequency + downChannelListObject[j-1].docsIfDownChannelWidth/2){
			isOverlap = true;
			return downChannelListObject[j-1].docsIfDownChannelId;
		}
	}
	return 0;
}
//增加上行信道与下行信道中心频率冲突检测:上行信道中心频率可配置范围5-65(MHz)，下行信道中心频率可配置范围52-1002(MHz).
//进入52-65MHz之间时，需要判断下行信道是否占用该区域，如果占用，则判断是否冲突
//两种处理方式：使用第二种
//如果冲突，则通过修改信道的tip上下限来限制输入范围--问题：与命令行显示的输入范围不符(实际相符)
//如果冲突，则返回冲突提示--问题：提示信息与可设置范围不符
//注意：下行信道带宽大于上行信道带宽，因此需要考虑上行信道被包在下行信道的情况
function upDownFrequencyCheck(downFrequency,width){
	var maxDF = downFrequency+width/2;
	var minDF = downFrequency-width/2;
	
	if(minDF<=65*1000000){//进入识别区,上行信道频宽小于下行信道频宽
		var i;
		for(i = 0; i < upChannelListObject.length; i++){//由于downChannelListObject数据已存在排序ASC
			if((maxDF >= upChannelListObject[i].channelFrequency- upChannelListObject[i].channelWidth/2&&
					maxDF<=upChannelListObject[i].channelFrequency+ upChannelListObject[i].channelWidth/2)||
					(minDF >= upChannelListObject[i].channelFrequency- upChannelListObject[i].channelWidth/2&&
							minDF<=upChannelListObject[i].channelFrequency+ upChannelListObject[i].channelWidth/2)||
							(minDF <= upChannelListObject[i].channelFrequency- upChannelListObject[i].channelWidth/2&&
									maxDF>=upChannelListObject[i].channelFrequency+ upChannelListObject[i].channelWidth/2)){
				return upChannelListObject[i].channelId;	
			}
		}
	}
	return -1;
}
function minPower(adminStatus){
	if(cmcDownChannelBaseShowInfo.txPowerLimit && cmcDownChannelBaseShowInfo.txPowerLimit.minPower) {
		return cmcDownChannelBaseShowInfo.txPowerLimit.minPower.toFixed(1) ;
	}
	
	var i = 1;
	if(downChannelListObject.length==0||adminStatus == 2){
		return toDecimal1(17);
	} else {
		var power = downChannelListObject[0].docsIfDownChannelPower;
		for(; i < downChannelListObject.length; i++){
			if(power < downChannelListObject[i].docsIfDownChannelPower)
				power = downChannelListObject[i].docsIfDownChannelPower;
		}
		power = Math.abs(power - 80);
		if (power < 170){
			power = 170;
		}
		return toDecimal1(power/10);
	}
}
function maxPower(upNum, adminStatus){
	if(cmcDownChannelBaseShowInfo.txPowerLimit && cmcDownChannelBaseShowInfo.txPowerLimit.maxPower) {
        return cmcDownChannelBaseShowInfo.txPowerLimit.maxPower.toFixed(1);
    }
	
	var i = 1;
	if(downChannelListObject.length==0||adminStatus == 2){
		return toDecimal1(60);
	} else {
		var power = downChannelListObject[0].docsIfDownChannelPower;
		for(; i < downChannelListObject.length; i++){
			if(power > downChannelListObject[i].docsIfDownChannelPower){
				power = downChannelListObject[i].docsIfDownChannelPower;
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
	/* if(ifAdminStatus != $("#ifAdminStatus").val() && $("#ifAdminStatus").val() == 1){
		for(i = 0; i <downChannelListObject.length; i++){
			if(downChannelListObject[i].ifAdminStatus == 1 && downChannelListObject[i].docsIfDownChannelPower/10 > maxChanPower){
				return downChannelListObject[i].docsIfDownChannelId;
			}
		}
	} */
	return 0;
}
function onAnnexChange(){
    var val = $("#annexMode").val();
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
    //inputFocused('downChannelFrequery', frequencyMinTip + '-' +frequencyMaxTip, 'iptxt_focused');
    $("#downChannelFrequery").attr("toolTip",frequencyMinTip + '-' +frequencyMaxTip);
}

/**
 * 检测编辑该值是否会导致在线的下行信道
 */
function checkChannelRange(power) {
	if(!downChannelListObject || !downChannelListObject.length) {
		return true;
	}
	var minPower = downChannelListObject[0].downChannelPower;
	var maxPower = downChannelListObject[0].downChannelPower;
    for(var i=0; i < downChannelListObject.length; i++){
        if(minPower > downChannelListObject[i].downChannelPower) {
        	minPower = downChannelListObject[i].downChannelPower;
        }
        if(maxPower < downChannelListObject[i].downChannelPower) {
        	maxPower = downChannelListObject[i].downChannelPower;
        }
    }
    
    if(power < minPower) {
    	minPower = power;
    }
    if(power > maxPower) {
    	maxPower = power;
    }
    
    // 最大电平/最小电平不能超过8dB
    return (maxPower - minPower) <= 8;
}

function saveClick() {
	var i;
	var object = Zeta$('annexMode').value.trim();
	if(object == 3){
		width = 8000000;
	}else width = 6000000;
	//中心频率
	object = Zeta$('downChannelFrequery').value.trim();
	var adminStatus = ifAdminStatus;
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
		var cid = upDownFrequencyCheck(frequency,width);
		if(cid!=-1){
			window.top.showMessageDlg(I18N.COMMON.tip, String.format(I18N.text.conflictWithUSTip, cid));
			return;
		};
	}
	//电平
	object = Zeta$('downChannelPower').value.trim();
	//var power = new Number(object)*10;
	if (!isFloatNumber(object)) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.text.powerFormatTip, null, function(){
			Zeta$('downChannelPower').focus();
		});
		return;
	}
	
	if(parseFloat(object) < minChanPower || parseFloat(object) > maxChanPower){
		var tipStr = I18N.text.powerRangeTip + minChanPower + '-'+ maxChanPower;
		window.top.showMessageDlg(I18N.RECYLE.tip, tipStr);
		return;
	}
	
	// TODO 不能小于17dBmV / 77dBuV
    var lowLimit = 17;
    if(currentUnit !== 'dBmV') {
    	lowLimit = 77;
    }
    if (minPower < lowLimit){
    	window.top.showMessageDlg(I18N.RECYLE.tip, '@CMC.tip.lowLimit@' + lowLimit + currentUnit);
		return;
    }
	
	// add by fanzidong,增加差值8dBmV的检查
	if(!checkChannelRange(parseFloat(object))) {
		window.top.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.rangeInvalid);
		return;
	}
	
	/* if (ifAdminStatus != adminStatus && adminStatus == 1){
		i = adminStatusCheck();
		var tipStr = I18N.CHANNEL.channel + i + I18N.text.powerOverMaxValueTip + maxChanPower + ', ' + I18N.text.modifyPowerAndOpenChannelTip;
		if(i < 17 && i > 0){
			window.top.showMessageDlg(I18N.RECYLE.tip, tipStr);
			return;
		}
	} */
	var tipStr = I18N.text.confirmModifyTip;
	if(downChannelFrquence != $("#downChannelFrequery").val()){
		tipStr = I18N.text.modifyFrequencyConfirmTip;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, tipStr, function (type) {
	var isDocsisInfChanged = true;
	if(type=="ok"){
		$("#saveBtn").attr("disabled",true);
		window.parent.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
        url: '/cmc/channel/modifyDownStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+'&productType='+productType+'&isDocsisInfChanged='+isDocsisInfChanged,
        type: 'post',
        data: jQuery(formChanged).serialize(),
        success: function(response) {
			//window.top.getFrame('cmcId1000').onRefreshClick();
			//在topvision-server/webapp/epon/onuView.jsp中找到
			//response = eval("(" + response + ")");
        	if(response.message == "success"){        		
        	    window.parent.closeWaitingDlg();
        	    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifySuccessTip);
			}else{
			    window.parent.closeWaitingDlg();
			    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
			}
			window.parent.getFrame("entity-" + cmcId).onRefreshClick();
			cancelClick();
		}, error: function(response) {
		    window.parent.closeWaitingDlg();
		    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
		}, cache: false
	})
	}
	});
}
//当频宽改变了，交织深度的可选项也随之改变，频宽为8时，交织深度只可选（-），当频宽为6时，交织深度可以选其他值
//频宽改变时，annex同时修改（只展示）
//1,改交织深度 2,改制式(annex) 3,频率边界 4,改符号率 (与调制配置文件一同修改)
function channelWidthChanged(widthObj){
	var channelWidth = $(widthObj).val();
	var channelModul = $("#modulation").val();
	
	if(channelWidth==6000000){
		//$("#channelInterleaveSelect option").remove();
		//$("#channelInterleaveSelect").append("<option value='3' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==3'>selected</s:if>>(8, 16)</option>");  
		//$("#channelInterleaveSelect").append("<option value='4' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==4'>selected</s:if>>(16, 8)</option>");  
		//$("#channelInterleaveSelect").append("<option value='5' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==5'>selected</s:if>>(32, 4)</option>");  
		//$("#channelInterleaveSelect").append("<option value='6' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==6'>selected</s:if>>(64, 2)</option>");  
		//$("#channelInterleaveSelect").append("<option value='7' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==7'>selected</s:if>>(128,1)</option>");  
		$("#channelInterleaveSelect").val(3);
		$("#annexMode").val(4);
		frequencyMinTip = _frequencyMin + 3;
        frequencyMaxTip = _frequencyMax - 3;
	}else if(channelWidth==8000000){
		//1,交织
		//$("#channelInterleaveSelect option").remove();
		//$("#channelInterleaveSelect").append("<option value='8'>-</option>"); 
		$("#channelInterleaveSelect").val(8);
		//2,annex
		$("#annexMode").val(3);
		//3,频率边界
		frequencyMinTip = _frequencyMin + 4;
        frequencyMaxTip = _frequencyMax - 4;
	}else{
        //其他Annex不对边界值进行修改 
        frequencyMinTip = _frequencyMin;
        frequencyMaxTip = _frequencyMax;
    }
	$("#downChannelFrequery").attr("toolTip",frequencyMinTip + '-' +frequencyMaxTip);
	//4,带宽与调制配置文件共同确定 IPQAM符号率
    //affectRateByWidthAndModulation(channelWidth,channelModul);
}
/*
* 1，修改width,触发width修改事件（修改交织深度，修改符号率，修改频率边界）
* 该方法只能手动触发，禁止程序触发
*/
function annexChanged(annexObj){
	var val = $(annexObj).val();
	if(val == 3){
	    $("#channelInterleaveSelect").val(8);
		$("#docsIfDownChannelWidth").val(8000000);//确定触发channelWidthChanged
	}else if(val == 4){
	    $("#channelInterleaveSelect").val(3);
		$("#docsIfDownChannelWidth").val(6000000);
	}
	//$("#docsIfDownChannelWidth").trigger("change");
	changeFrequencyTip();
	
}

function channelInterleaveChange(){
    var val = $("#channelInterleaveSelect").val();
    if(val == 8){
        $("#docsIfDownChannelWidth").val(8000000);
        $("#annexMode").val(3);
    }else{
        $("#docsIfDownChannelWidth").val(6000000);
        $("#annexMode").val(4);
    }
    //$("#docsIfDownChannelWidth").trigger("change");
    changeFrequencyTip();
}

function changeFrequencyTip(){
	var channelWidth = $("#docsIfDownChannelWidth").val();
	
	if(channelWidth==6000000){
		frequencyMinTip = _frequencyMin + 3;
        frequencyMaxTip = _frequencyMax - 3;
	}else if(channelWidth==8000000){
		frequencyMinTip = _frequencyMin + 4;
        frequencyMaxTip = _frequencyMax - 4;
	}else{
        frequencyMinTip = _frequencyMin;
        frequencyMaxTip = _frequencyMax;
    }
	$("#downChannelFrequery").attr("toolTip",frequencyMinTip + '-' +frequencyMaxTip);
}

$(document).ready(function(){
	
	$("#channelInterleaveSelect").val(downChannelInterleave);
	$("#channelInterleaveSelect").trigger("change");
	$("#saveBtn").attr("disabled",true);
	
	//工作状态展示
	if(cmcDownChannelBaseShowInfo.ifOperStatus == 1) {
		$('#ifOperStatus').val(I18N.COMMON.on);
	} else {
		$('#ifOperStatus').val(I18N.COMMON.off);
	}
	
	//频宽提示信息
	var t = frequencyMinTip + '-' + frequencyMaxTip;
    $("#downChannelFrequery").attr("toolTip",t);
    //$("#downChannelFrequery").focus();
    
    var t2 = minChanPower + '-' + maxChanPower;
    $("#downChannelPower").attr("toolTip",t2);
    if(supportFunction){
        for(var param in supportFunction){
            var val = supportFunction[param];
            if(val == "true" || val == true){
                $("#" + param).removeClass("disableInput").attr("disabled", false);
            }
        }
    }
})

function authLoad(){
	if(!operationDevicePower){
	    $(":input").attr("disabled",true);
	}
	$('#saveBtn').attr('disabled',true);
}
</script>
</head>
<body class="openWinBody" onload="authLoad()">
	<div class="openWinHeader">
		<div class="openWinTip">
			@CHANNEL.id@:
			<span class="orangeTxt bigNumTip"><s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/></span>
		</div>
		<div class="rightCirIco downArrCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 pT40">
		<form name="formChanged" id="formChanged">
			<input name="cmcDownChannelBaseShowInfo.channelIndex" type="hidden"	value=<s:property value="cmcDownChannelBaseShowInfo.channelIndex"/> />
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt" width="100">@CHANNEL.id@</td>
					<td width="267">
						<input class="normalInputDisabled w150" readonly="readonly" type=text  name="cmcDownChannelBaseShowInfo.docsIfDownChannelId"
							value=<s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/>
							/>
					</td>
					<td class="rightBlueTxt" width="100">@CHANNEL.frequency@(MHz)</td>
					<td>
						<input class="normalInput w150" type=text id="downChannelFrequery" name="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequencyForunit"
							value='<s:property value="%{cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency*1.0/1000000}"/>'
                            onkeyup="changeCheck()" toolTip="" />
                          
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						@CCMTS.channel.status@
					</td>
					<td>
					   <input id="ifOperStatus" class="normalInputDisabled w150" readonly="readonly" type=text />
						<%-- <select id="ifAdminStatus"  class="normalSel w150" name="cmcDownChannelBaseShowInfo.ifAdminStatus" 
							onchange="changeCheck()" disabled="disabled">
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==2">selected</s:if>>@CMC.select.close@</option>
	                        <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.ifAdminStatus==1">selected</s:if>>@CMC.select.open@</option>
	                    </select> --%>
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.modulationType@
					</td>
					<td>
						<select id="modulation"  name="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation" class="normalSel w150"
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
					<td class="rightBlueTxt">
						@CHANNEL.interleave@
					</td>
					<td>
						<select id="channelInterleaveSelect" name="cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave" 
						  class="normalSel w150 disableInput channelInterleave" onchange="changeCheck();channelInterleaveChange();">
	                        <!-- <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==1">selected</s:if>>unknown</option>
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==2">selected</s:if>>other</option>    -->
	                        <option value="3">(8, 16)</option>
	                        <option value="4">(16, 8)</option>
	                        <option value="5">(32, 4)</option>
	                        <option value="6">(64, 2)</option>
	                        <option value="7">(128,1)</option>
	                        <option value="8">-</option>
                        </select>
					</td>
					<td class="rightBlueTxt">
						@CHANNEL.power@(@{unitConfigConstant.elecLevelUnit}@)
					</td>
					<td>
						<input class="normalInput w150" type=text id="downChannelPower" name="cmcDownChannelBaseShowInfo.downChannelPower"
														onkeyup="changeCheck()"	toolTip=""						
							value=<s:property value="cmcDownChannelBaseShowInfo.downChannelPower"/>
							/>
							 
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						Annex<%-- <fmt:message bundle='${cmcRes}' key='CHANNEL.annex'/> --%>
					</td>
					<td>
						<select id="annexMode" name="cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex" 
						onchange="annexChanged(this);changeCheck()" class="normalSel w150 disableInput channelWidth">
	                        <!-- <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==1">selected</s:if>>unknown</option>
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==2">selected</s:if>>other</option>   --> 
	                        <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==3">selected</s:if>>annexA</option>
	                        <option value="4" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==4">selected</s:if>>annexB</option>
	                        <!-- <option value="5" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==5">selected</s:if>>annexC</option> -->
                        </select>
					</td>
					<td class="rightBlueTxt">
						@CMC.label.bandwidth@(MHz)
					</td>
					<td>
						<select id="docsIfDownChannelWidth" class="normalSel w150 disableInput channelWidth" 
						name="cmcDownChannelBaseShowInfo.docsIfDownChannelWidth" onchange = "channelWidthChanged(this)"
						onclick="changeCheck()">
							<option value="6000000"
								<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==6000000">selected</s:if>>6</option>
							<option value="8000000"
								<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==8000000">selected</s:if>>8</option>
						</select>
					</td>
				</tr>
			</table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
			         <li><a id="saveBtn" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@apply@</span></a></li>
			         <li><a href="javascript:;" onclick="cancelClick()" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
			     </ol>
			</div>
		</form>
	</div>
	 <script type="text/javascript">
     	$(window).load(function(){
     	      onAnnexChange()
     		  var t = frequencyMinTip + '-' + frequencyMaxTip;
              $("#downChannelFrequery").attr("toolTip",t);
              //$("#downChannelFrequery").focus();
              
              $(".disableInput").attr("disabled", true);
              if(supportFunction){
                  for(var param in supportFunction){
                      var val = supportFunction[param];
                      if(val == "true" || val == true){
                          $("#" + param).removeClass("disableInput").attr("disabled", false);
                      }
                  }
              }
              authLoad()
     	});
     </script>
</body>
</Zeta:HTML>