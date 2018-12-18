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
    module fault
</Zeta:Loader>
<script type="text/javascript">

window.onload = function(){
	setTimeout(function(){
		$("body").focus()
	},500);
	
}
var mainframe = <%= request.getParameter("mainframe") %>;
function confirmClick() {
	window.parent.onConfirmEventClick();
}
function clearClick() {
	window.parent.onClearEventClick();
	Zeta$('confirmBt').disabled = true;
	Zeta$('clearBt').disabled = true;
}
function lastClick() {
	var frame = null;
	if (mainframe) {
		frame = window.top;
	} else {
		frame = window.top.getActiveFrame();
	}
	try {
		var alertId = frame.getLastAlertId();
		if (alertId > 0) {
			if (mainframe) {
				location.href = 'showAlertDetail.tv?mainframe=true&alertId=' + alertId;
			} else {
				location.href = 'showAlertDetail.tv?alertId=' + alertId;
			}
		}
	} catch(err) {
	}
}
function nextClick() {
	var frame = null;
	if (mainframe) {
		frame = window.top;
	} else {
		frame = window.top.getActiveFrame();
	}
	try {
		var alertId = frame.getNextAlertId();
		if (mainframe) {
			location.href = 'showAlertDetail.tv?mainframe=true&alertId=' + alertId;
		} else {
			location.href = 'showAlertDetail.tv?alertId=' + alertId;
		}
	} catch(err) {
	}	
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}

/**
 * 禁用页面backspace后退
 */
function backspace(evt){
	evt=evt?evt:window.event;
	//window.location = "#";
	if (evt.keyCode == 8 ){//&& evt.srcElement.tagName != "INPUT" && evt.srcElement.type != "text" && evt.srcElement.tagName != "TEXTAREA"
		//location.replace(this.href); 
		event.returnValue=false;
		return false;
	}
}
 
</script>
</head>
<body class="openWinBody"  onkeydown="backspace(event)" >
	<div class="openWinHeader">
		<div class="openWinTip">
			@ALERT.alertNum@:
			<span class="orangeTxt bigNumTip"><s:property value="alert.alertId" /></span>
		</div>
		<div class="rightCirIco alarmCirIco"></div>		
	</div>
		<div class="edgeTB10LR20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="rightBlueTxt" width="100">@ALERT.alertNum@:</td>
				<td width="267" class="wordBreak"><s:property value="alert.alertId" /></td>
				<td class="rightBlueTxt" width="100">@ALERT.alertSource@:</td>
				<td width="267" class="wordBreak"><s:property value="alert.source" /></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@ALERT.alertType@:</td>
				<td class="wordBreak"><s:property value="alert.typeName" /></td>
				<td class="rightBlueTxt">@ALERT.alertLevel@:</td>
				<td class="wordBreak"><s:property value="alert.levelName" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ALERT.alertHost@:</td>
				<td class="wordBreak"><s:property value="alert.host" /></td>
				<td class="rightBlueTxt">@ALERT.HappenTime@:</td>
				<td class="wordBreak"><s:date name="alert.firstTime"	format="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@ALERT.HappenTimes@:</td>
				<td class="wordBreak"><s:property value="alert.happenTimes" /></td>
				<td class="rightBlueTxt">@ALERT.lastHappenTime@:</td>
				<td class="wordBreak"><s:date name="alert.lastTime" format="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ALERT.confirmPerson@:</td>
				<td class="wordBreak"><s:property value="alert.confirmUser" /></td>
				<td class="rightBlueTxt">@ALERT.clearPerson@:</td>
				<td class="wordBreak"><s:property value="alert.clearUser" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ALERT.confirmTime@:</td>
				<td class="wordBreak">
					<s:if test="alert.confirmUser!=null">
						<s:date name="alert.confirmTime" format="yyyy-MM-dd HH:mm:ss" />
					</s:if>
				</td>
				<td class="rightBlueTxt">@ALERT.clearTime@:</td>
				<td class="wordBreak">
					<s:if test="alert.clearUser!=null">
						<s:date name="alert.clearTime" format="yyyy-MM-dd HH:mm:ss" />
					</s:if>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt">@ALERT.confirmNote@:</td>
				<td class="wordBreak"><s:property value="alert.confirmMessage" /></td>
				<td class="rightBlueTxt">@ALERT.clearNote@:</td>
				<td class="wordBreak"><s:property value="alert.clearMessage" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@ALERT.alertReason@:</td>
				<td colspan="3" class="wordBreak"><s:property value="alert.message" /></td>
			</tr>			
		</table>
		<div class="w306 toCenter clearBoth">
			<ol class="upChannelListOl pB0 pT20">
				<li><a href="javascript:;" class="normalBtnBig" onclick="lastClick()"><span><i class="miniIcoArrUp"></i>@ALERT.lastItem@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="nextClick()"><span><i class="miniIcoArrDown"></i>@ALERT.nextItem@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
			</ol>
		</div>
	</div>
	
	
</body>
</Zeta:HTML>