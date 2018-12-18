<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>EQAM CONFIG</title>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css" />
<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="/css/<%= cssStyleName %>/mytheme.css" />
<!-- 自定义css引入 -->
<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css"/>
<!-- 自定义js引入 -->
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>

<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<fmt:setBundle basename="com.topvision.ems.network.resources" var="networkRes"/>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="emsRes"/>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>

<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>

<script type="text/javascript">

var cmcId = ${cmcId};
var productType =${productType};
var cc8800BIpqamMappings = '${ipQamMappingsObj}'==''?[]:${ipQamMappingsObj};
var action = ${action};
var ipQamStatusList = '${ipQamStatusList}'==''?[]:${ipQamStatusList};
var ipQamMappingsList = '${ipQamMappingsList}'==''?[]:${ipQamMappingsList};

function modifyMappingsInfoBy(){
	var modifiedProgramStreamInfo = new Object;

	/* if(checkIpqamIpAddr($("#ipqamDestinationIPAddress").val().trim())
			&&)
	 */
	var fi = $("#formChanged input");
	$.each(fi, function(i,v){
		if(""!=v.id){
			modifiedProgramStreamInfo[v.id] = v.value;
		} 
	});
	modifiedProgramStreamInfo.ipqamStreamType = $("#ipqamStreamType").val();
	
	//目的IP地址
	var destIPAddr = modifiedProgramStreamInfo.ipqamDestinationIPAddress;
	if(!checkIpqamIpAddr(destIPAddr)){
		return false;
	}
	//UDP端口
	var ipqamUDPPort = modifiedProgramStreamInfo.ipqamUDPPort;
	if ( ipqamUDPPort != '' && !isNumber(ipqamUDPPort)) {
		window.top.showMessageDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.udpFormatError);
		return false;
	}
	if(ipqamUDPPort < 1 || ipqamUDPPort > 65535){
		var tipStr = I18N.CMC.ipqam.udpRange;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	//输入节目号
	var ipqamProgramNumberInput = modifiedProgramStreamInfo.ipqamProgramNumberInput;
	if ( ipqamProgramNumberInput != '' && !isNumber(ipqamProgramNumberInput)) {
		window.top.showMessageDlg(I18N.COMMON.waiting,I18N.CMC.ipqam.inputProgramFormatError );
		return false;
	}
	if(ipqamProgramNumberInput < 0 || ipqamProgramNumberInput > 65535){
		var tipStr = I18N.CMC.ipqam.inputProgramRange ;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	//输出节目号
	var ipqamProgramNumberOutput = modifiedProgramStreamInfo.ipqamProgramNumberOutput;
	if ( ipqamProgramNumberOutput != '' && !isNumber(ipqamProgramNumberOutput)) {
		window.top.showMessageDlg(I18N.COMMON.waiting, I18N.CMC.ipqam.outputProgramFormatError);
		return false;
	}
	if(ipqamProgramNumberOutput < 0 || ipqamProgramNumberOutput > 65535){
		var tipStr = I18N.CMC.ipqam.outPutProgramRange;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	//PMV
	var ipqamPMV = modifiedProgramStreamInfo.ipqamPMV;
	if ( ipqamPMV != '' && !isNumber(ipqamPMV)) {
		window.top.showMessageDlg(I18N.COMMON.waiting, 'PMV'+I18N.CMC.tip.numberFormatError);
		return false;
	}
	if(ipqamPMV < 0 || ipqamPMV > 65535){
		var tipStr = I18N.CMC.ipqam.pmvRange;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	//ipqamDataRate
	var ipqamDataRate = modifiedProgramStreamInfo.ipqamDataRate;
	var channelId = modifiedProgramStreamInfo.ipqamOutputQAMChannel;//获取当前信道ID
	var tmp;
	for(var j=0;j<ipQamStatusList.data.length;j++)
		{
			if(channelId==ipQamStatusList.data[j].ipqamOutputQAMChannel)
			{
				tmp = ipQamStatusList.data[j].ipqamBandwidthCapacity;
			}
		}
					
	if(ipqamDataRate < 0){
		var tipStr = I18N.CMC.ipqam.inputRateNoNegative ;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	else if(ipqamDataRate > tmp * 1024){
		var tipStr = I18N.CMC.ipqam.inputRateNoBeyondTheMark ;
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
		return false;
	}
	//冲突检测
	var ipQamMappingsListData = ipQamMappingsList.data;
	var mappingId = modifiedProgramStreamInfo.mappingId;
	for(var j = 0; j < ipQamMappingsListData.length; j++){
		//检测在同信道上是否有相同的UDP端口号
		//action==3表示新增，需要全部检测。修改时检测则需要将自己排除在外
		if(action==3||mappingId!=ipQamMappingsListData[j].mappingId){
			if(channelId == ipQamMappingsListData[j].ipqamOutputQAMChannel && modifiedProgramStreamInfo.ipqamUDPPort == ipQamMappingsListData[j].ipqamUDPPort){
				window.top.showMessageDlg(I18N.COMMON.waiting, String.format(I18N.CMC.ipqam.hasSameUdpInChannel,channelId));
				return false;
			}
		}
		//alert(modifiedProgramStreamInfo.ipqamStreamType);
		//流类型为Data、MPTS不用检查
		if(modifiedProgramStreamInfo.ipqamStreamType!=undefined&&(modifiedProgramStreamInfo.ipqamStreamType != 3 && ipQamMappingsListData[j].ipqamStreamType != 3 
				&& modifiedProgramStreamInfo.ipqamStreamType != 1 && ipQamMappingsListData[j].ipqamStreamType != 1)){
			//检测在所有信道上是否有相同的输出节目号
			if(action==3||mappingId!=ipQamMappingsListData[j].mappingId){
				if(modifiedProgramStreamInfo.ipqamProgramNumberOutput == ipQamMappingsListData[j].ipqamProgramNumberOutput){
					var tipStr;
					if(channelId == ipQamMappingsListData[j].ipqamOutputQAMChannel){
						tipStr = String.format(I18N.CMC.ipqam.hasSameOutPutProgram ,channelId);
					}else{
						tipStr = String.format(I18N.CMC.ipqam.hasSameOutPutProgramDifChannel ,channelId,ipQamMappingsListData[j].ipqamOutputQAMChannel);
					}
					window.top.showMessageDlg(I18N.COMMON.waiting, tipStr);
					return false;
				}
				//检测同信道是否有相同的PMV
				if(channelId == ipQamMappingsListData[j].ipqamOutputQAMChannel 
						&& modifiedProgramStreamInfo.ipqamPMV == ipQamMappingsListData[j].ipqamPMV){
					window.top.showMessageDlg(I18N.COMMON.waiting, String.format(I18N.CMC.ipqam.hasSamePMV ,channelId));
					return false;
				}
			}
		}
	}
	//1表示删除，2表示修改，3表示增加
	var cfgTip = "";
	var runTip = "";
	var rsTip = "";
	//alert(action)
	if(action==3){
		cfgTip = I18N.CMC.ipqam.isSureAddProgramMappings;
		runTip = I18N.CMC.ipqam.doingAddProgramMappings;
		rsTip = I18N.CMC.ipqam.addProgramMappings;
	}else{
		cfgTip = I18N.CMC.ipqam.isSureModifyProgramMappings ;
		runTip = I18N.CMC.ipqam.doingModifyProgramMappings ;
		rsTipe = I18N.CMC.ipqam.modifyProgramMappings ;
	}
	 window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, cfgTip, function (type) {
		if(type=="ok"){
		 window.top.showWaitingDlg(I18N.COMMON.waiting, runTip, 'ext-mb-waiting');
			$.ajax({url:"/cmc/ipQamChannel/modifyIPQAMStreamMapList.tv?cmcId=" + cmcId+'&productType='+productType+'&action='+action,
					data:jQuery(formChanged).serialize(),
					method:"post", 
					cache: false,
					success:function (response) {
						if(response == "success"){
				    		window.top.showMessageDlg(I18N.COMMON.waiting,rsTip+I18N.CMC.tip.successfully);
						 }else{
				    		window.top.showMessageDlg(I18N.COMMON.waiting, rsTip+I18N.CMC.tip.failure);
						}
						window.parent.getFrame("entity-" + cmcId).onRefreshClick();
						cancelClick();
					}, 
					failure:function () {
						 //window.top.showMessageDlg(I18N.COMMON.waiting,'');设备无法连通，请检查网络连接!
					}
			});
		}
	});
 }

function cancelClick() {
	window.top.closeWindow('ipqamMappingsConfig');
}
//check or change
function changeRateStatus(obj){
	var rateEnable = obj;
	if(rateEnable == 0){//关闭
		disableChange("ipqamDataRate",true);
	}else{
		disableChange("ipqamDataRate",false);
	}
}
function checkStreamType(obj){
	var streamType = obj;//obj.value;
	if(streamType == 3||streamType==1){//
		disableChange("ipqamProgramNumberInput",true);
		disableChange("ipqamProgramNumberOutput",true);
		disableChange("ipqamPMV",true);
		disableChange("ipqamPidMapString",false);
		
	} else if(streamType == 2||streamType==0){
	
		disableChange("ipqamProgramNumberInput",false);
		disableChange("ipqamProgramNumberOutput",false);
		disableChange("ipqamPMV",false);
		disableChange("ipqamPidMapString",true);
	} 
}
//pid映射修改
function modifyPidMap () {
	var ipqamPidMapString = cc8800BIpqamMappings.data.ipqamPidMapString;
	//window.top.ipqamPidMapInfo = ipqamPidMapString;
	var pidMapNum = 0;
	pidMapNum = ipqamPidMapString.substring(0,ipqamPidMapString.indexOf(","));
	
	pidMapNum = pidMapNum/2;
	
	var newIpqamStreamType = $("#ipqamStreamType").val();
	var dataStr = 'pidMapNum=' + pidMapNum + '&ipqamPidMapString=' + ipqamPidMapString + '&ipqamStreamType=' + newIpqamStreamType;
	var win = window.top.createDialog('pidMapModify', I18N.CMC.ipqam.configProgramPidMappings, 350, 300,
		'/cmc/ipQamChannel/ipqamPidMapModify.tv?' + dataStr, null, true, true, setPidMapFun);
	function setPidMapFun () {
		var displayValue = [];
		var displayValueStr = '';
		var value = window.top.ipqamPidMapInfo;
		if(value==undefined){
			value = "";
			displayValueStr = "";
		}else{
			var tmp = value.split(',');
			var num = 0;
			for(var i = 1; i < tmp.length - 1; i = i + 2){
				displayValue.push(tmp[i] + '-' + tmp[i + 1]);
			}
			displayValueStr =  displayValue.join(',');	
		}
		
		$("#ipqamPidMapString").val(value);
		$("#ipqamPidMapStringShow").val(displayValueStr);
		changeCheck();
		//record.set('ipqamPidMapString', window.top.ipqamPidMapInfo||'');
		//delete window.top.ipqamPidMapInfo;
	}
}
// 用途：检查数据是否符合非负整数格式
function isNumber(number) {
	var reg = /^[0-9]+$/;
	return reg.test(number);
} 
//IPQAM IP地址检查
function checkIpqamIpAddr(ipAddr){
	var tipStr = I18N.CMC.ipqam.ipqamDestinationIPAddress;
	if(!checkedIpValue(ipAddr)){
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr + I18N.CMC.ipqam.formatError);
		return false;
	}
	if("255.255.255.255" == ipAddr){
		window.top.showMessageDlg(I18N.COMMON.waiting, tipStr + I18N.route.ipIsBroadCastTip);
		return false;
	}
	return true;
}
function changeCheck(){
	var changeTag = false;
	if(action==3){//3表示新增
		changeTag = true;
	}else{
		if('${cc8800BIpqamMappings.ipqamDestinationIPAddress}' != $("#ipqamDestinationIPAddress").val().trim() 
				|| '${cc8800BIpqamMappings.ipqamUDPPort}' != $("#ipqamUDPPort").val().trim()
				|| '${cc8800BIpqamMappings.ipqamStreamType}'  != $("#ipqamStreamType").val().trim()
				|| '${cc8800BIpqamMappings.ipqamActive}'  !=$("#ipqamActive").val().trim() 
				|| '${cc8800BIpqamMappings.ipqamProgramNumberInput}'  !=$("#ipqamProgramNumberInput").val().trim()
				|| '${cc8800BIpqamMappings.ipqamProgramNumberOutput}' !=$("#ipqamProgramNumberOutput").val().trim()
				|| '${cc8800BIpqamMappings.ipqamPMV}' !=$("#ipqamPMV").val().trim()
				|| '${cc8800BIpqamMappings.ipqamDataRateEnable}' !=$("#ipqamDataRateEnable").val().trim()
				|| '${cc8800BIpqamMappings.ipqamDataRate}' !=$("#ipqamDataRate").val().trim()
				|| '${cc8800BIpqamMappings.ipqamPidMapString}' !=$("#ipqamPidMapString").val().trim()
				){
				changeTag = true;
			}else {
				changeTag = false;
			}	
	}
	//alert(!changeTag);
	$("#saveBtn").attr("disabled",!changeTag);
}
function disableChange(id,status){
	$("#"+id).attr("disabled",status);
}
$(function(){
	$("#saveBtn").attr("disabled",true);
	
	var rateEnable = cc8800BIpqamMappings.data.ipqamDataRateEnable;
	changeRateStatus(rateEnable);
	var streamType = cc8800BIpqamMappings.data.ipqamStreamType;
	checkStreamType(streamType);
	
})
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">
			IPQAM<fmt:message bundle='${cmc}' key='CHANNEL.id'/>:
			<span class="orangeTxt bigNumTip">${cc8800BIpqamMappings.ipqamOutputQAMChannel }</span>
		</div>
		<div class="rightCirIco downArrCirIco"></div>		
	</div>
	<div class="edgeTB10LR20 pT30">
		<form name="formChanged" id="formChanged">
			<input name="cc8800BIpqamMappings.ipqamAction" type="hidden"	value='${action}' />
			<input name="cc8800BIpqamMappings.mappingId" id="mappingId" type="hidden"	value='${cc8800BIpqamMappings.mappingId }' />
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt" width="100"><fmt:message bundle='${cmc}' key='CHANNEL.id'/></td>
					<td width="267">
						<input class="normalInputDisabled w150" type=text readonly="readonly" id="ipqamOutputQAMChannel" name="cc8800BIpqamMappings.ipqamOutputQAMChannel"
							value='${cc8800BIpqamMappings.ipqamOutputQAMChannel}'
							/>
					</td>
					<td class="rightBlueTxt" width="100">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.ipqamDestinationIPAddress'/>
					</td>
					<td>
						<input class="normalInput w150" type=text id="ipqamDestinationIPAddress" name="cc8800BIpqamMappings.ipqamDestinationIPAddress"
							value='${cc8800BIpqamMappings.ipqamDestinationIPAddress }'
                            onkeyup="changeCheck()"/>
                          
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.ipqamUDPPort'/>
					</td>
					<td>
						<input class="normalInput w150" type="hidden" id="ipqamOldUDPPort" name="cc8800BIpqamMappings.ipqamOldUDPPort"
							value='${cc8800BIpqamMappings.ipqamUDPPort }' />
						<input class="normalInput w150" type=text id="ipqamUDPPort" name="cc8800BIpqamMappings.ipqamUDPPort"
							value='${cc8800BIpqamMappings.ipqamUDPPort }' 
							onkeyup="changeCheck()" toolTip="1-65535"/>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.streamType'/>
					</td>
					<td>
						<select id="ipqamStreamType"  name="cc8800BIpqamMappings.ipqamStreamType" class="normalSel w150"
							onclick="changeCheck()" onchange="checkStreamType(this.value)">
	                        <option value="0" <s:if test="#request.cc8800BIpqamMappings.ipqamStreamType==0" >selected</s:if>>SPTS</option>
	                        <option value="1" <s:if test="#request.cc8800BIpqamMappings.ipqamStreamType==1" >selected</s:if>>MPTS</option>
	                        <option value="2" <s:if test="#request.cc8800BIpqamMappings.ipqamStreamType==2" >selected</s:if>>DataR</option>
	                        <option value="3" <s:if test="#request.cc8800BIpqamMappings.ipqamStreamType==3" >selected</s:if>>Data</option>  
                        </select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.label.status'/>
					</td>
					<td>
						<select id="ipqamActive"  name="cc8800BIpqamMappings.ipqamActive" class="normalSel w150"
						onclick="changeCheck()">
	                        <option value="1" <s:if test="#request.cc8800BIpqamMappings.ipqamActive==1" >selected</s:if>><fmt:message bundle='${cmc}' key='CMC.select.open'/></option>
	                        <option value="0" <s:if test="#request.cc8800BIpqamMappings.ipqamActive==0" >selected</s:if>><fmt:message bundle='${cmc}' key='CMC.select.close'/></option>
                        </select>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.ipqamProgramNumberInput'/>
					</td>
					<td>
						<input class="normalInput w150" type=text id="ipqamProgramNumberInput" name="cc8800BIpqamMappings.ipqamProgramNumberInput"
							value='${cc8800BIpqamMappings.ipqamProgramNumberInput }' 
							onkeyup="changeCheck()" toolTip="0-65535"/>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.ipqamProgramNumberOutput'/>
					</td>
					<td>
						<input class="normalInput w150" type=text id="ipqamProgramNumberOutput" name="cc8800BIpqamMappings.ipqamProgramNumberOutput"
							value='${cc8800BIpqamMappings.ipqamProgramNumberOutput }' 
							onkeyup="changeCheck()" toolTip="0-65535"/>
					</td>
					<td class="rightBlueTxt">
						PMV
					</td>
					<td>
						<input class="normalInput w150" type=text id="ipqamPMV" name="cc8800BIpqamMappings.ipqamPMV"
							value='${cc8800BIpqamMappings.ipqamPMV }' 
							onkeyup="changeCheck()" toolTip="0-65535"/>
					</td>
				</tr>
				<tr>					
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.inputStreamRateEnable'/>
					</td>
					<td>
						<select id="ipqamDataRateEnable"  name="cc8800BIpqamMappings.ipqamDataRateEnable" class="normalSel w150"
							onclick="changeCheck()" onchange="changeRateStatus(this.value)">
	                        <option value="1" <s:if test="#request.cc8800BIpqamMappings.ipqamDataRateEnable==1" >selected</s:if>>
	                           <fmt:message bundle='${cmc}' key='CMC.select.open'/></option>
	                        <option value="0" <s:if test="#request.cc8800BIpqamMappings.ipqamDataRateEnable==0" >selected</s:if>>
	                           <fmt:message bundle='${cmc}' key='CMC.select.close'/>
	                        </option>
                        </select>
					</td>
					<td class="rightBlueTxt">
						<fmt:message bundle='${cmc}' key='CMC.ipqam.ipqamDataRate'/>(Kbps)
					</td>
					<td>
						<input class="normalInput w150" type=text id="ipqamDataRate" name="cc8800BIpqamMappings.ipqamDataRate"
							value='${cc8800BIpqamMappings.ipqamDataRate }' 
							onkeyup="changeCheck()"/>
					</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">
						PID
					</td>
					<td colspan="3">
						<input id="ipqamPidMapString"  name="cc8800BIpqamMappings.ipqamPidMapString" class="normalInput" style="width: 510px;"
						 	value="${cc8800BIpqamMappings.ipqamPidMapString }"
						 	onkeyup="changeCheck()" type="hidden"/>
						<input id="ipqamPidMapStringShow" readonly="readonly" name="cc8800BIpqamMappings.ipqamPidMapStringShow" class="normalInput" style="width: 530px;"
						 	value="${cc8800BIpqamMappings.ipqamPidMapStringShow }"
						 	/>
						 	<a href="javascript:;" onclick="modifyPidMap()" class="nearInputBtn" style="float:right;">
						 	<span><fmt:message bundle='${cmc}' key='CMC.text.modify'/></span>
						 	</a>
					</td>
					
				</tr>
				
			</table>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT46 noWidthCenter">
			         <li><a id="saveBtn" onclick="modifyMappingsInfoBy()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${emsRes}" key="COMMON.save"/></span></a></li>
			         <li><a href="javascript:;" onclick="cancelClick()" class="normalBtnBig"><span><fmt:message bundle="${emsRes}" key="COMMON.cancel"/></span></a></li>
			     </ol>
			</div>
		</form>
	</div>
</body>
</html>
