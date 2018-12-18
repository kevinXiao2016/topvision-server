<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    PLUGIN  LovCombo
    MODULE  CM
</Zeta:Loader>
<style type="text/css">
.readonlyInput {
	background-color: #DCDCDC;
	width:200px;
}
.normalInput{width:200px;}
input[type="checkbox"]{width:17px;height:17px;line-height:17px;align:left;margin-right:2px; vertical-align:-2px;*vertical-align:middle;_vertical-align:3px;}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = "../images/s.gif";
var quotaId = '<s:property value="quotaId"/>';
var quotaDisplayName = '<s:property value="quotaDisplayName"/>';
var quotaMax = '<s:property value="quotaMax"/>';
var quotaMin = '<s:property value="quotaMin"/>';
var quotaAttributeObject = ${quotaAttributeObject};
function cancelClick(){
	window.parent.closeWindow('modifyCmPoll');
}

function isDigit(value)
{
	if(parseInt(value)==value){
		return true
	}else{
		return false
	}
}

function okClick(){
	var tooHigh = $("#tooHigh").val();
	var high = $("#high").val();
	var lower = $("#lower").val();
	var tooLower = $("#tooLower").val();
	
	var tooHighStatus = $("#tooHighCheckBox").is(':checked');
	var highStatus = $("#highCheckBox").is(':checked');
	var lowerStatus = $("#lowerCheckBox").is(':checked');
	var tooLowerStatus = $("#tooLowerCheckBox").is(':checked');
	
	if(tooLowerStatus && tooLower == quotaMin){
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		return;
	}
	if(tooLowerStatus && lowerStatus && lower == tooLower){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(highStatus && lowerStatus && high == lower){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(tooHighStatus && highStatus && tooHigh == high){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(tooHighStatus && tooHigh == quotaMax){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(!isDigit(tooHigh) || !isDigit(high) || !isDigit(lower) || !isDigit(tooLower)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdMustBeInteger);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(tooHighStatus && parseInt(tooHigh, 10) > parseInt(quotaMax, 10)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(tooHighStatus && highStatus &&  parseInt(high, 10) > parseInt(tooHigh, 10)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	if(highStatus && lowerStatus &&  parseInt(lower, 10) > parseInt(high, 10)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	
	if(lowerStatus && tooLowerStatus &&  parseInt(tooLower, 10) > parseInt(lower, 10)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	
	if(tooLowerStatus &&  parseInt(quotaMin, 10) > parseInt(tooLower, 10)){
		//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		top.afterSaveOrDelete({        
			title: I18N.COMMON.tip,        
			html: '<b class="orangeTxt">' + I18N.cmPoll.thresholdSetError + '</b>'    
		});
		return;
	}
	
	if(parseInt(tooLower, 10) < quotaMin || parseInt(tooLower, 10) > quotaMax || parseInt(lower, 10) < quotaMin || parseInt(lower, 10) > quotaMax 
			|| parseInt(high, 10) < quotaMin || parseInt(high, 10) > quotaMax  || parseInt(tooHigh, 10) < quotaMin || parseInt(tooHigh, 10) > quotaMax ){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.thresholdSetError);
		return;
	}
	
	$.ajax({
		url: '/cmpoll/modifyCmQuotaAttribute.tv?quotaId=' + quotaId + '&tooHigh=' + tooHigh + '&high=' + high + '&lower=' + lower + '&tooLower=' + tooLower
			+ '&tooHighStatus=' + tooHighStatus + '&highStatus=' + highStatus + '&lowerStatus=' + lowerStatus + '&tooLowerStatus=' + tooLowerStatus,
	      type: 'post',
	      success: function(response) {
		  	    	if(response.success){
		  	    		top.afterSaveOrDelete({        
		  	  			title: I18N.COMMON.tip,        
		  	  			html: '<b class="orangeTxt">' + I18N.COMMON.modifySuccess + '</b>'    
		  	  		});
	  	    		window.top.getActiveFrame().onRefreshClick();
	  	    		cancelClick();
	  	    	}else{
	  	    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
	  	    		cancelClick();
	  	    	}
			}, error: function(response) {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.modifyFail);
				cancelClick();
			}, cache: false
	});
}

Ext.onReady(function() {
	//初始化数据
	$("#quotaDisplayName").val(quotaDisplayName);
	$("#quotaRange").val(quotaMin + '-' + quotaMax);
	$("#tooHigh").val(quotaAttributeObject[0].tooHigh);
	$("#high").val(quotaAttributeObject[0].high);
	$("#lower").val(quotaAttributeObject[0].lower);
	$("#tooLower").val(quotaAttributeObject[0].tooLower);
	
	if(quotaAttributeObject[0].tooHighStatus){
		$("#tooHighCheckBox").attr("checked","true"); 
		$("#tooHigh").attr("disabled", false);
	}else{
		$("#tooHigh").attr("disabled", true);
	}
	if(quotaAttributeObject[0].highStatus){
		$("#highCheckBox").attr("checked","true"); 
		$("#high").attr("disabled", false);
	}else{
		$("#high").attr("disabled", true);
	}
	if(quotaAttributeObject[0].lowerStatus){
		$("#lowerCheckBox").attr("checked","true"); 
		$("#lower").attr("disabled", false);
	}else{
		$("#lower").attr("disabled", true);
	}
	if(quotaAttributeObject[0].tooLowerStatus){
		$("#tooLowerCheckBox").attr("checked","true"); 
		$("#tooLower").attr("disabled", false);
	}else{
		$("#tooLower").attr("disabled", true);
	}
	
	$("#tooHighCheckBox").bind('click', function(){
		if($("#tooHighCheckBox").is(':checked')){
			$("#tooHigh").attr("disabled", false);
		}else{
			$("#tooHigh").attr("disabled", true);
		}
	});
	
	$("#highCheckBox").bind('click', function(){
		if($("#highCheckBox").is(':checked')){
			$("#high").attr("disabled", false);
		}else{
			$("#high").attr("disabled", true);
		}
	});
	
	$("#lowerCheckBox").bind('click', function(){
		if($("#lowerCheckBox").is(':checked')){
			$("#lower").attr("disabled", false);
		}else{
			$("#lower").attr("disabled", true);
		}
	});
	
	$("#tooLowerCheckBox").bind('click', function(){
		if($("#tooLowerCheckBox").is(':checked')){
			$("#tooLower").attr("disabled", false);
		}else{
			$("#tooLower").attr("disabled", true);
		}
	});
})
</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@cmPoll.modifyPollQuotaTip@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
		<form onsubmit="return false;">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt vertical-middle"><label for="quotaDisplayName">@cmPoll.threshold@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInputDisabled w200" id="quotaDisplayName" disabled="disabled"/></td>
					<td></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt vertical-middle"><label for="quotaRange">@cmPoll.thresholdPeriod@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInputDisabled w200" id="quotaRange" disabled="disabled"/></td>
					<td></td>
				</tr>
				<tr>
					<td  width="200" class="rightBlueTxt vertical-middle"><label for="tooHigh">@cmPoll.tooHighDangerAlertValue@<font color=red>*</font></label></td>
					<td width="120"><input type="text" class="normalInput" id="tooHigh" /></td>
					<td><input type="checkbox" class="alarmCbx" id="tooHighCheckBox"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="high">@cmPoll.tooHighAlertValue@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" id="high" /></td>
					<td><input type="checkbox" class="alarmCbx" id="highCheckBox" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label for="lower">@cmPoll.tooLowAlertValue@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" id="lower" /></td>
					<td><input type="checkbox" class="alarmCbx" id="lowerCheckBox"/></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt"><label for="tooLower">@cmPoll.tooLowDangerAlertValue@<font color=red>*</font></label></td>
					<td><input type="text" class="normalInput" id="tooLower" /></td>
					<td><input type="checkbox" class="alarmCbx" id="tooLowerCheckBox"/></td>
				</tr>
			</table>
		</form>
	</div>
	
	<!-- 第三部分，按钮组合 -->
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="okClick();" icon="miniIcoEdit">@COMMON.modify@</Zeta:Button>
		<Zeta:Button onClick="cancelClick();" icon="miniIcoForbid" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
