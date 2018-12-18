<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML>
<HEAD>
<%@include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" 
	src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="STYLESHEET" type="text/css" href="../../css/gui.css">
<link rel="STYLESHEET" type="text/css" href="../../css/ext-all.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/xtheme.css">
<link rel="STYLESHEET" type="text/css"
	href="../../css/<%= cssStyleName %>/mytheme.css">
<script type="text/javascript" src="../../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>;
var deviceType = '<s:property value='deviceType'/>';
var thresholdType = '<s:property value='thresholdType'/>';
var perfThresholdTypeIndex = <s:property value='perfThresholdTypeIndex'/>; 
var perfThresholdObject = <s:property value='perfThresholdObject'/>; 
function saveClick(){
	var perfThresholdUpper = $('#perfThresholdUpper').val();
	var perfThresholdLower = $('#perfThresholdLower').val();
	if(parseInt(perfThresholdUpper.length) == 20){
		var sub_perfThresholdUpper1 = perfThresholdUpper.substring(0,10);
		var sub_perfThresholdUpper2 = perfThresholdUpper.substring(10,20);
		if(parseInt(sub_perfThresholdUpper1) > 1844674407){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValUpper );
			return;
		}
		if(parseInt(sub_perfThresholdUpper1) == 1844674407 && parseInt(sub_perfThresholdUpper2) > 3709551615 ){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValUpper );
			return;
		}
	}
	if(compare(perfThresholdUpper,perfThresholdLower)){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.thresholdValError);
		return;
	}
	if(perfThresholdTypeIndex == 50){
		if(perfThresholdUpper > 85 || perfThresholdUpper < 0){
			Zeta$("perfThresholdUpper").focus();
			return;
		}
		if(perfThresholdLower > 85 || perfThresholdLower < 0){
			Zeta$("perfThresholdLower").focus();
			return;
		}
	}
	if(!checkInput(perfThresholdUpper))
	{
		Zeta$("perfThresholdUpper").focus();
		return ;
	}
	
	if(!checkInput(perfThresholdLower))
	{
		Zeta$("perfThresholdLower").focus();
		return ;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.port.modifyingThreshold, 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/epon/perf/modifyPerfThreshold.tv?r=' + Math.random(),
		success : function(text) {
			if (text.responseText != null && text.responseText != "success") {  
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdFailed, 'error');
			}
			else{
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdSuccess);
			window.parent.getWindow("perfThresholdConfig").body.dom.firstChild.contentWindow.store.load({params: {entityId:entityId,deviceType:deviceType,thresholdType:thresholdType}});
			cancelClick();
			}
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyThresholdFailed);
		},
		params : {
			entityId:entityId,
			thresholdType:thresholdType,
			perfThresholdTypeIndex:perfThresholdTypeIndex,
			perfThresholdObject:perfThresholdObject,
			perfThresholdUpper:perfThresholdUpper,
			perfThresholdLower:perfThresholdLower
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('modifyPerfThreshold');	
}
function compare(upper,lower){
	var len1 = upper.length
	var len2 = lower.length
	if(len1 > len2){
		return false
	}
	if(len1 < len2){
		return true
	}
	//如果上下限都是 0 的话,则不要求上限大于下限
	if((upper == "0" && lower == "0") || upper == lower ) 
		return false
	//如果上下限不为0,则必须上限大于下限
	var cursor = 0
	while(cursor < len1){
		var m = upper.charAt(cursor)
		var n = lower.charAt(cursor)
		if(m>n)
			return false
		if(m<n)
			return true
		cursor++
	}
	return true	
}

function loadFocus(){
	 //----硬性将焦点移入本页面,不让会出现焦点丢失的情况----//
	$("#perfThresholdUpper").focus();
	//----然后将焦点移除，否则会有提示框——---//
	$("#perfThresholdUpper").blur();
	//----动态绑定focus和blur事件，对于之前的绑定都需要进行移除---//
	if(perfThresholdTypeIndex == 50){
		/* $("#perfThresholdUpper").bind('focus',function(){
			inputFocused('perfThresholdUpper', I18N.PERF.port.thresholdTempError, 'iptxt_focused');
		}).bind('blur',function(){inputBlured(this, 'iptxt');inputBlured(this, 'iptxt');}); */
	}else{
		/* $("#perfThresholdUpper").bind('focus',function(){
			inputFocused('perfThresholdUpper', I18N.PERF.port.thresholdTempWrong, 'iptxt_focused');
		}).bind('blur',function(){inputBlured(this, 'iptxt');inputBlured(this, 'iptxt');}); */
	}
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
//设备操作权限-------------------------------------
function authLoad(){
	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
    var ids = new Array();
    ids.add("okBt");
    ids.add("perfThresholdUpper");
    ids.add("perfThresholdLower");
    operationAuthInit(operationDevicePower,ids);
}
//------------------------------------------------
</script>
</HEAD>
<body class="openWinBody" onload = "loadFocus();authLoad();">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
		
	<div class="edgeTB10LR20 pT20">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="200">
						<fmt:message bundle="${i18n}" key="PERF.port.thresholdIndex" />:
	                 </td>
	                 <td>
						<input id="perfTypeIndex" value="<s:property value='perfName'/>" disabled="disabled" class="normalInputDisabled w200" />
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						<fmt:message bundle="${i18n}" key="PERF.port.thresholdUpper" />:
	                 </td>
	                 <td>
						<s:if test="perfThresholdTypeIndex=='50'" >
				    		<input id="perfThresholdUpper" value="<s:property value='perfThresholdUpper'/>" maxlength="3" class="normalInput w200"
				    		toolTip='<fmt:message bundle="${i18n}" key="PERF.port.thresholdTempError" />'
								 />
						</s:if>
						<s:else>
				    		<input id="perfThresholdUpper" value="<s:property value='perfThresholdUpper'/>" maxlength="20" class="normalInput w200"
				    		toolTip='<fmt:message bundle="${i18n}" key="PERF.port.thresholdTempWrong" />'
								 />
						</s:else>
	                 </td>
	             </tr>
	              <tr>
	                 <td class="rightBlueTxt">
						<fmt:message bundle="${i18n}" key="PERF.port.thresholdLower" />:
	                 </td>
	                 <td>
						<s:if test="perfThresholdTypeIndex=='50'" >
				    		<input id="perfThresholdLower" value="<s:property value='perfThresholdLower'/>" maxlength="3"
								class="normalInput w200" toolTip='<fmt:message bundle="${i18n}" key="PERF.port.thresholdTempErrorLow" />' />
						</s:if>
						<s:else>
							<input id="perfThresholdLower" value="<s:property value='perfThresholdLower'/>" maxlength="20"
								class="normalInput w200" toolTip='<fmt:message bundle="${i18n}" key="PERF.port.thresholdTempWrongLow" />'
								 />
						</s:else>
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id="okBt"  onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i><fmt:message bundle="${i18n}" key="COMMON.save" /></span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${i18n}" key="COMMON.close" /></span></a></li>
		     </ol>
		</div>
	</div>
	


</BODY>
</HTML>