<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var cmcId = '<s:property value="cmcId"/>';
var entityId = '<s:property value="entityId"/>';
var topCmFlapInterval = '<s:property value="topCmFlapInterval"/>';
//var flapMonitorInterval= '<s:property value="flapMonitorInterval"/>';

function cancelClick() {
	window.top.closeWindow('flapInterval');
}
function isIntNumber(number){
	var reg = /^[1-9]\d*$/;
	return reg.test(number);
}
function applyFlapTime(){
	topCmFlapInterval = $('#topCmFlapInterval').val();
	if(!isIntNumber($('#topCmFlapInterval').val())||$('#topCmFlapInterval').val()<60||$('#topCmFlapInterval').val()>86400){
		$('#topCmFlapInterval').focus();
		return;
	}
	$.ajax({
    	url:'/cmflap/cmFlapConfig.tv?cmcId='+ cmcId + "&entityId=" + entityId+ "&topCmFlapInterval=" + topCmFlapInterval,
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.setFlapTimeSuccess);
	  			top.afterSaveOrDelete({
	   				title: I18N.CMC.tip.tipMsg,
	   				html: '<b class="orangeTxt">' + I18N.cmcPortal.setFlapTimeSuccess + '</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg,I18N.cmcPortal.setFlapTimeFail);
	  		}

	  	},
	  	error:function(){
	  		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.setFlapTimeFail);
	  	},
	  	cache:false
    });
}
function resetCounters(){

	$.ajax({
    	url:'/cmflap/resetFlapCounters.tv?cmcId='+ cmcId + "&entityId=" + entityId,
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			//window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.resetFlapSuccess);
	  			top.afterSaveOrDelete({
	   				title: I18N.CMC.tip.tipMsg,
	   				html: '<b class="orangeTxt">' + I18N.cmcPortal.resetFlapSuccess + '</b>'
	   			});
	  		}else{
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg,I18N.cmcPortal.resetFlapFail);
	  		}
		
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.resetFlapFail);
	  	},
	  	cache:false
    });
}
/* function applyFlapMonitorInterval(){
	var flapMonitorInterval =  $('#flapMonitorInterval').val();
	$.ajax({
    	url:'/cmflap/modifyFlapMonitor.tv?cmcId='+ cmcId+'&flapMonitorInterval='+flapMonitorInterval,
	  	type:'POST',
	  	dateType:'text',
	  	success:function(response){
	  		if(response == "success"){
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.resetFlapSuccess);
	  		}else{
	  			window.parent.showMessageDlg(I18N.CMC.tip.tipMsg,I18N.cmcPortal.resetFlapFail);
	  		}
			
	  	},
	  	error:function(){
	  		window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.cmcPortal.resetFlapFail);
	  	},
	  	cache:false
    });
}

function demo(){
	window.top.createDialog('cmFlapDemo', 'Flap历史曲线图', 800, 600, 
			'/cmflap/showOneCMFlapHisChart.tv?cmMac=00:25:f2:93:23:cb' , null, true, true);
}
 */

function dataload(){
	$('#topCmFlapInterval').val(topCmFlapInterval);
	
	//$('#flapMonitorInterval = ').val(flapMonitorInterval);
}
function addEnterKey() {
	if (event.keyCode==KeyEvent.VK_ENTER) {
		okClick();
	}
}
$(function (){
	dataload();
	
	//操作权限
	if(!operationDevicePower){
	    $("#topCmFlapInterval").attr("disabled",true);
	    R.applyFlapTime.setDisabled(true);
	    R.resetCountersButton.setDisabled(true);
	}
});
</script>
</head>
	<body class="openWinBody" onkeydown="addEnterKey(event || e)">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@cmcPortal.flapIntroduction@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td class="rightBlueTxt" width="200">@cmcPortal.flapInterval@:</td>
						<td width="140">
							<input type=text id="topCmFlapInterval" maxlength="5" class="normalInput"
							tooltip="@cmcPortal.inputTip@" style="width: 100px;" />  @CMC.label.seconds@
						</td>
						<td>
							<ul><Zeta:Button id="applyFlapTime" onClick="applyFlapTime()" icon="miniIcoSave">@BUTTON.apply@</Zeta:Button></ul>
						</td>	
					</tr>
					<tr class="darkZebraTr">
						<td colspan=3><div style="width:550px; overflow:hidden; padding-bottom:20px; color:#555;">@cmcPortal.flapIntervalIntroduction@</div></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="2">
							<ul><Zeta:Button onClick="resetCounters()" id="resetCountersButton" icon="miniIcoBack">@cmcPortal.flapResetButton@</Zeta:Button></ul>
						</td>
					</tr>
					<tr class="darkZebraTr">
					<td colspan="3" align="center" style="color:#555;">@cmcPortal.flapResetIntroduction@</td>
					</tr>
					
				</table>
		</div>
	</body>
</Zeta:HTML>