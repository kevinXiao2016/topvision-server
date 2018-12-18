<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Modify Downstream Channel Configuration</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css" />
<fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmcRes"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript"
	src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
/**
 * 工具类函数
 */
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
 //用途：检查数据是否符合非负整数格式
 function isNumber(number) {
 	var reg = /^[0-9]+$/;
 	return reg.test(number);
 }
 // 用途：检查数据是否是该范围的非负整数
 function detectionLimits(value, valueName, minValue, maxValue){
 	var result = '';
 	if ( value != '' && !isNumber(value)) {
 		result = valueName + I18N.CMC.tip.numberFormatError;
 	}
 	if(value < minValue || value > maxValue){
 		result = valueName + I18N.CMC.ipqam.rangingTip + minValue + '-' + maxValue + '!';
 	}
 	return result;
 }
 //用途:检测数据是否Float数据
 function isFloatNumber(number){
 	var reg = /^[1-9]\d*\.\d{1}$|^[1-9]\d*$/;
 	return reg.test(number);
 }
 //用途：检查是否0-6位小数的float数据
 function is0_6BitsNumber(number){
     var reg = /^[1-9]\d*\.\d{1,6}$|^[1-9]\d*$|^\-\d+\.\d{1,6}$|^\-\d+$|^0\.\d{1,6}$|^0$/;
     return reg.test(number);
 }
 /*
 用途：检查输入字符串是否只由英文字母、数字、横线和逗号组成
 输入：
 s：字符串
 返回：
 如果通过验证返回true,否则返回false
 */
 function isNumberOr_Letter(s) {//判断是否是数字或字母
     var regu = "^[0-9a-zA-Z\-,]+$";
     var re = new RegExp(regu);
     if (re.test(s)) {
         return true;
     } else {
         return false;
     }
 } 
</script>
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
var ifAdminStatus = '${cc8800bHttpDownChannelIPQAM.ifAdminStatus}'==''?'${cmcDownChannelBaseShowInfo.ifAdminStatus}':'${cc8800bHttpDownChannelIPQAM.ifAdminStatus}';
var downChannelInterleave = <s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave"/>;
//是否是docsis信道 
var isDocsisChannel = false;

var supportFunction = ${supportFunction};

//从后台传递过来的ChannelList需要按照frequency保持升序排序
var channelListObject = ${channelListObject};
var downChannelListObject = channelListObject.downList;
var upChannelListObject = channelListObject.upList;
var adminStatusUpNum = <s:property value="adminStatusUpNum" />;
var maxChanPowerLUT = [60, 60, 56, 54, 52, 51, 50, 49, 49, 48, 48, 47, 47, 46, 46, 45, 45];
//定义两个常量 _frequencyMin _frequencyMax用于存放设备提供频率边界值 
var _frequencyMin = 52.0;
var _frequencyMax = 1002.0;
var isOverlap = false;
var isExceed = false;
var symRate = ['', '6.952', '6.900', '6.875', '5.361', '5.057'];
var isDocsisInfChanged = false;
var isIPQAMInfChanged = false;
var isIpqamSupported = "${isIpqamSupported}"=="true"?true:false;//是否支持IPQAM

//频率边界值提示 目前不支持带宽设置，如果之后支持带宽设置，需要重新测试该功能 
var frequencyMinTip;
var frequencyMaxTip;

var maxChanPower = maxPower(adminStatusUpNum, ifAdminStatus);
var minChanPower = minPower(ifAdminStatus);


function cancelClick() {
	window.top.closeWindow('downStreamConfig');
}
function changeCheck(){
	var changeTag = false;
	if(downChannelPower != $("#downChannelPower").val() || downChannelFrquence != $("#downChannelFrequery").val()
		|| downChannelModulation != $("#modulation").val()//||( ifAdminStatus!=3&&ifAdminStatus !=$("#ifAdminStatus").val())
		|| downChannelWidth !=$("#docsIfDownChannelWidth").val()
		|| downChannelInterleave!=$("#channelInterleaveSelect").val()
		){
		isDocsisInfChanged  = true;
	}else{
		isDocsisInfChanged  = false;
	}
	if(!isDocsisChannel){
		if('${cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate}'!=$("#docsIfDownChannelSymRate").val().trim()
				|| '${cc8800bHttpDownChannelIPQAM.ipqamTranspStreamID}'!=$("#ipqamTranspStreamID").val().trim()
				|| '${cc8800bHttpDownChannelIPQAM.ipqamQAMGroupName}'!=$("#ipqamQAMGroupName").val().trim()
				|| '${cc8800bHttpDownChannelIPQAM.ipqamQAMManager}'!=$("#ipqamQAMManager").val().trim()
				|| '${cc8800bHttpDownChannelIPQAM.ipqamOriginalNetworkID}'!=$("#ipqamOriginalNetworkID").val().trim()
				|| '${cc8800bHttpDownChannelIPQAM.ipqamDtsAdjust}'!=$("#ipqamDtsAdjust").val().trim()
				//|| (ifAdminStatus==3&&ifAdminStatus !=$("#ifAdminStatus").val())
				){
				isIPQAMInfChanged = true;	
			}else{
				isIPQAMInfChanged = false;//防止进行修改后又改回去
			}	
	}
	
	if(isDocsisInfChanged||isIPQAMInfChanged){
		changeTag = true;		
	}else {
		changeTag = false;
	}
	if(changeTag ==true){
		$("#saveBtn").attr("disabled",false);
	}else {
		$("#saveBtn").attr("disabled",true);
	}
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
	if(ifAdminStatus != $("#ifAdminStatus").val() && $("#ifAdminStatus").val() == 1){
		for(i = 0; i <downChannelListObject.length; i++){
			if(downChannelListObject[i].ifAdminStatus == 1 && downChannelListObject[i].docsIfDownChannelPower/10 > maxChanPower){
				return downChannelListObject[i].docsIfDownChannelId;
			}
		}
	}
	return 0;
}


function saveClick() {
	var i;
	var width = $("#docsIfDownChannelWidth").val().trim();
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
	if (adminStatus == 1||ifAdminStatus==3){//当前信道开启（1：DOCSIS 3：IPQAM）的情况下进行判断
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
		//alert(cid)
		if(cid!=-1){
			window.top.showMessageDlg("@COMMON.tip@", String.format("@text.conflictWithUSTip@", cid));
			return;
		};
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
	//ipqam模块
	//TranspStreamID,QAM Group Name,OriginalNetworkID
	if(!isDocsisChannel){
		minValue = 0;
		maxValue = 65535;
		// Transp Stream ID
		object = $('#ipqamTranspStreamID').val().trim();
		result = detectionLimits(object, 'Transp Stream ID', minValue, maxValue);
		if(result != ''){
			window.parent.showMessageDlg(I18N.RECYLE.tip, result);
			return "error";
		}
		// Original Network ID
		object = $('#ipqamOriginalNetworkID').val().trim();
		result = detectionLimits(object, 'Original Network ID', minValue, maxValue);
		if(result != ''){
			window.parent.showMessageDlg(I18N.RECYLE.tip, result);
			return "error";
		}	
		object = $("#ipqamQAMGroupName").val().trim();
		if (!isNumberOr_Letter(object)) {
			window.parent.showMessageDlg(I18N.RECYLE.tip,
					'QAM Group Name '+I18N.CHANNEL.qamGroupNameFormat);
			return;
		}
	}
	
	var tipStr = I18N.text.confirmModifyTip;
	if(downChannelFrquence != $("#downChannelFrequery").val()){
		tipStr = I18N.text.modifyFrequencyConfirmTip;
	}
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, tipStr, function (type) {
	if(type=="ok"){
		$("#saveBtn").attr("disabled",true);
		var data = $(formChanged).serialize();//+"&cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex="+$("#annexMode").val();
		//alert(data);
		//return;
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
		$.ajax({
        url: '/cmc/channel/modifyDownStreamBaseInfo.tv?cmcId='+cmcId+"&cmcPortId="+cmcPortId+'&productType='+productType
        		+'&isDocsisInfChanged='+isDocsisInfChanged+'&isIPQAMInfChanged='+isIPQAMInfChanged,
        type: 'post',
        data: data,
        success: function(response) {
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
//当频宽改变了，交织深度的可选项也随之改变，频宽为8时，交织深度只可选（-），当频宽为6时，交织深度可以选其他值
//频宽改变时，annex同时修改（只展示）
//1,改交织深度 2,改制式(annex) 3,频率边界 4,改符号率 (与调制配置文件一同修改)
function channelWidthChanged(widthObj){
	var channelWidth = $(widthObj).val();
	var channelModul = $("#modulation").val();
	
	if(channelWidth==6000000){
		$("#channelInterleaveSelect option").remove();
		$("#channelInterleaveSelect").append("<option value='3' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==3'>selected</s:if>>(8, 16)</option>");  
		$("#channelInterleaveSelect").append("<option value='4' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==4'>selected</s:if>>(16, 8)</option>");  
		$("#channelInterleaveSelect").append("<option value='5' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==5'>selected</s:if>>(32, 4)</option>");  
		$("#channelInterleaveSelect").append("<option value='6' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==6'>selected</s:if>>(64, 2)</option>");  
		$("#channelInterleaveSelect").append("<option value='7' <s:if test='#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==7'>selected</s:if>>(128,1)</option>");  
		
		$("#annexMode").val(4);
		frequencyMinTip = _frequencyMin + 3;
      frequencyMaxTip = _frequencyMax - 3;
	}else if(channelWidth==8000000){
		//1,交织
		$("#channelInterleaveSelect option").remove();
		$("#channelInterleaveSelect").append("<option value='8'>-</option>"); 
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
  affectRateByWidthAndModulation(channelWidth,channelModul);
}
/*
 * 1，修改width,触发width修改事件（修改交织深度，修改符号率，修改频率边界）
 * 该方法只能手动触发，禁止程序触发
 */
function annexChanged(annexObj){
	var val = $(annexObj).val();
  if(val == 3){
  	$("#docsIfDownChannelWidth").val(8000000);//确定触发channelWidthChanged
  }else if(val == 4){
  	$("#docsIfDownChannelWidth").val(6000000);
  }
  $("#docsIfDownChannelWidth").trigger("change");
}
/*width,modulation确定Rate的展示关系*/
function affectRateByWidthAndModulation(width,modulation){
	if(width == 6000000){
		$("#docsIfDownChannelSymRate option").remove();
		$("#docsIfDownChannelSymRate").append("<option value='4' <s:if test='#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==4'>selected</s:if>>"+symRate[4]+"</option>");
		$("#docsIfDownChannelSymRate").append("<option value='5' <s:if test='#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==5'>selected</s:if>>"+symRate[5]+"</option>");
		modulationTochangeRate(modulation);
		$("#docsIfDownChannelSymRate").attr("disabled", true);
	}else{
		$("#docsIfDownChannelSymRate option").remove();
		$("#docsIfDownChannelSymRate").append("<option value='1' <s:if test='#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==1'>selected</s:if>>"+symRate[1]+"</option>");// option[value='1']").remove();  
		$("#docsIfDownChannelSymRate").append("<option value='2' <s:if test='#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==2'>selected</s:if>>"+symRate[2]+"</option>");// option[value='2']").remove();
		$("#docsIfDownChannelSymRate").append("<option value='3' <s:if test='#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==3'>selected</s:if>>"+symRate[3]+"</option>");// option[value='3']").remove();
		$("#docsIfDownChannelSymRate").attr("disabled", false);
	}	
}
//modulation 与Rate
function modulationTochangeRate(modulation){
	if (modulation == 4) {
		$("#docsIfDownChannelSymRate").val(4);
	} else if (modulation == 3) {
		$("#docsIfDownChannelSymRate").val(5);
	}
}
function authLoad(){
	var ids = new Array();
	ids.add("downChannelFrequery");
	ids.add("ifAdminStatus");
	ids.add("modulation");
	ids.add("downChannelPower");
	operationAuthInit(operationDevicePower,ids);
}



$(document).ready(function(){
	authLoad();
	$("#annexMode").trigger("change");
	$("#saveBtn").attr("disabled",true);
	
	//频宽提示信息
	var t = frequencyMinTip + '-' + frequencyMaxTip;
    $("#downChannelFrequery").attr("toolTip",t);
    $("#downChannelFrequery").focus();
    
    var t2 = minChanPower + '-' + maxChanPower;
    $("#downChannelPower").attr("toolTip",t2);
    if(supportFunction){
        for(var param in supportFunction){
            var val = supportFunction[param];
            if(val == "true" || val == true){
                $("." + param).attr("disabled", false);
            }
        }
    }
})




</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">
			<fmt:message bundle='${cmcRes}' key='CHANNEL.id'/>:
			<span class="orangeTxt bigNumTip"><s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/></span>
		</div>
		<div class="rightCirIco downArrCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 pT30">
		<form name="formChanged" id="formChanged">
			<input name="cmcDownChannelBaseShowInfo.channelIndex" type="hidden"	value=<s:property value="cmcDownChannelBaseShowInfo.channelIndex"/> />
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt" width="100"><fmt:message bundle='${cmcRes}' key='CHANNEL.id'/></td>
					<td width="267">
						<input class="normalInputDisabled w150" readonly="readonly" type=text name="cmcDownChannelBaseShowInfo.docsIfDownChannelId"
							value=<s:property value="cmcDownChannelBaseShowInfo.docsIfDownChannelId"/>
							/>
					</td>
					<td class="rightBlueTxt" width="100"><fmt:message bundle='${cmcRes}' key='CHANNEL.frequency'/>(MHz)</td>
					<td>
						<input class="normalInput w150" type=text id="downChannelFrequery" name="cmcDownChannelBaseShowInfo.docsIfDownChannelFrequencyForunit"
							value='<s:property value="%{cmcDownChannelBaseShowInfo.docsIfDownChannelFrequency*1.0/1000000}"/>'
                            onkeyup="changeCheck()" toolTip="" />
                          
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmcRes}' key='CCMTS.channel.adminStatus'/>
					</td>
					<td>
						<input class="normalInput w150" type=text name="cmcDownChannelBaseShowInfo.ifAdminStatus"
							value='2' style="display: none;"/>
						<select disabled="disabled" id="ifAdminStatus"  class="normalSel w150" name="cc8800bHttpDownChannelIPQAM.ifAdminStatus" 
							onchange="changeCheck()">
	                        <option value="2" <s:if test="#request.cc8800bHttpDownChannelIPQAM.channelType==2">selected</s:if>><fmt:message bundle='${emsRes}' key='COMMON.off'/></option>
	                        <option value="3" <s:if test="#request.cc8800bHttpDownChannelIPQAM.channelType==3">selected</s:if>>EQAM<fmt:message bundle='${emsRes}' key='COMMON.on'/></option>
	                        <option value="1" <s:if test="#request.cc8800bHttpDownChannelIPQAM.channelType==1">selected</s:if>>DOCSIS<fmt:message bundle='${emsRes}' key='COMMON.on'/></option>
	                    </select>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmcRes}' key='CHANNEL.modulationType'/>
					</td>
					<td>
						<select id="modulation"  name="cmcDownChannelBaseShowInfo.docsIfDownChannelModulation" class="normalSel w150"
						onclick="changeCheck()" onchange="modulationTochangeRate(this.value)" >
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
						<fmt:message bundle='${cmcRes}' key='CHANNEL.interleave'/>
					</td>
					<td>
						<select id="channelInterleaveSelect" name="cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave" 
						class="normalSel w150 channelInterleave" onchange="changeCheck()">
	                        <!-- <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==1">selected</s:if>>unknown</option>
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==2">selected</s:if>>other</option>    -->
	                        <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==3">selected</s:if>>(8, 16)</option>
	                        <option value="4" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==4">selected</s:if>>(16, 8)</option>
	                        <option value="5" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==5">selected</s:if>>(32, 4)</option>
	                        <option value="6" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==6">selected</s:if>>(64, 2)</option>
	                        <option value="7" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==7">selected</s:if>>(128,1)</option>
	                        <option value="8" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelInterleave==8">selected</s:if>>-</option>
                        </select>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmcRes}' key='CHANNEL.power'/>(dBmV)
					</td>
					<td>
						<input class="normalInput w150" type=text id="downChannelPower" name="cmcDownChannelBaseShowInfo.docsIfDownChannelPowerForunit"
														onkeyup="changeCheck()"	toolTip=""						
							value=<s:property value="%{cmcDownChannelBaseShowInfo.docsIfDownChannelPower*1.0/10}"/>
							/>
							 
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						Annex<%-- <fmt:message bundle='${cmcRes}' key='CHANNEL.annex'/> --%>
					</td>
					<td>
						<select id="annexMode" name="cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex"
						onchange="annexChanged(this);changeCheck()" class="normalSel w150 channelWidth">
	                        <!-- <option value="1" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==1">selected</s:if>>unknown</option>
	                        <option value="2" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==2">selected</s:if>>other</option>  -->  
	                        <option value="3" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==3">selected</s:if>>annexA</option>
	                        <option value="4" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==4">selected</s:if>>annexB</option>
	                        <!-- <option value="5" <s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelAnnex==5">selected</s:if>>annexC</option> -->
                        </select>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle="${cmcRes}" key="CMC.label.bandwidth"/>(MHz)
					</td>
					<td>
						<select id="docsIfDownChannelWidth" class="normalSel w150 channelWidth"
						name="cmcDownChannelBaseShowInfo.docsIfDownChannelWidth" onchange = "channelWidthChanged(this)"
						onclick="changeCheck()">
							<option value="6000000"
								<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==6000000">selected</s:if>>6</option>
							<option value="8000000"
								<s:if test="#request.cmcDownChannelBaseShowInfo.docsIfDownChannelWidth==8000000">selected</s:if>>8</option>
						</select>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						SymbolRate
					</td>
					<td>
						<select id="docsIfDownChannelSymRate" name="cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate" 
						onchange="changeCheck()" class="normalSel w150">
	                        <!-- <option value="1" <s:if test="#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==1">selected</s:if>>6.952</option>
	                        <option value="2" <s:if test="#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==2">selected</s:if>>6.900</option>   
	                        <option value="3" <s:if test="#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==3">selected</s:if>>6.875</option>
	                        <option value="4" <s:if test="#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==4">selected</s:if>>5.361</option>
	                        <option value="5" <s:if test="#request.cc8800bHttpDownChannelIPQAM.docsIfDownChannelSymRate==5">selected</s:if>>5.057</option> -->
                        </select>
					</td>
					<td class="rightBlueTxt">
						TranspStreamID
					</td>
					<td>
						<input id="ipqamTranspStreamID" class="normalInput w150"
						name="cc8800bHttpDownChannelIPQAM.ipqamTranspStreamID" onchange = ""
						onkeyup="changeCheck()" toolTip="0-65535"
						value="${cc8800bHttpDownChannelIPQAM.ipqamTranspStreamID }">
			
						</input>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						QAM Group Name
					</td>
					<td>
						<input  id="ipqamQAMGroupName" name="cc8800bHttpDownChannelIPQAM.ipqamQAMGroupName" 
						onchange="" maxlength="20"
						onkeyup="changeCheck()" class="normalInput w150"
						toolTip="<fmt:message bundle="${cmcRes}" key="CHANNEL.qamGroupNameFormat"/>"
						value="${cc8800bHttpDownChannelIPQAM.ipqamQAMGroupName }">
                        </input>
                        
					</td>
					<td class="rightBlueTxt">
						QAM Manager
					</td>
					<td>
						<select id="ipqamQAMManager" class="normalSel w150"
							name="cc8800bHttpDownChannelIPQAM.ipqamQAMManager" onchange = "changeCheck()"
							>
							<option value="0" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamQAMManager==0">selected</s:if>>VOD</option>
	                        <option value="1" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamQAMManager==1">selected</s:if>>Broadcast</option>   
	                        <option value="2" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamQAMManager==2">selected</s:if>>NGOD</option>
	                        <option value="3" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamQAMManager==3">selected</s:if>>S_VOD</option>								
						</select>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						OriginalNetworkID
					</td>
					<td>
						<input id="ipqamOriginalNetworkID" class="normalSel w150"
							name="cc8800bHttpDownChannelIPQAM.ipqamOriginalNetworkID" onkeyup="changeCheck()"
							 toolTip="0-65535"
							value="${cc8800bHttpDownChannelIPQAM.ipqamOriginalNetworkID }">
			
						</input>
					</td>
					<td class="rightBlueTxt">
						Dts Adjust
					</td>
					<td>
						<select id="ipqamDtsAdjust" class="normalSel w150"
						name="cc8800bHttpDownChannelIPQAM.ipqamDtsAdjust" onchange ="changeCheck()"
						onclick="">
	                        <option value="1" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamDtsAdjust==1">selected</s:if>><fmt:message bundle='${cmcRes}' key='CMC.select.open'/></option>   
							<option value="0" <s:if test="#request.cc8800bHttpDownChannelIPQAM.ipqamDtsAdjust==0">selected</s:if>><fmt:message bundle='${cmcRes}' key='CMC.select.close'/></option>								
						</select>
					</td>
				</tr>
			</table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT46 noWidthCenter">
			         <li><a id="saveBtn" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${emsRes}" key="COMMON.save"/></span></a></li>
			         <li><a href="javascript:;" onclick="cancelClick()" class="normalBtnBig"><span><fmt:message bundle="${emsRes}" key="COMMON.cancel"/></span></a></li>
			     </ol>
			</div>
		</form>
	</div>
</body>
</HTML>