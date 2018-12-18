<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY EXT
	LIBRARY JQUERY 
	LIBRARY ZETA
    MODULE  FAULT
</Zeta:Loader>
<style type="text/css">
#infoArea{
	height: 310px;
	overflow:auto;
	border: 1px solid #ccc;
}
</style>
<script type="text/javascript">
function cancelClick() {
	window.top.closeWindow('alertDlg');
}
function lastClick() {
	var frame = window.top.getActiveFrame();
	try {
		var alertId = frame.getLastAlertId();
		if (alertId > 0) {
			location.href = 'showHistoryAlertDetail.tv?alertId=' + alertId;
		}
	} catch(err) {
	}
}
function nextClick() {
	var frame = window.top.getActiveFrame();
	try {
		var alertId = frame.getNextAlertId();
		if (alertId > 0) {
			location.href = 'showHistoryAlertDetail.tv?alertId=' + alertId;
		}	
	} catch(err) {
	}	
}

</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="openWinTip">
			@ALERT.alertNum@:
			<span class="orangeTxt bigNumTip"><s:property value="historyAlert.alertId" /></span>
		</div>
		<div class="rightCirIco alarmCirIco"></div>
	</div>
	
	<div class="edge10TB10LR20">
		<div id="infoArea">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt" width="100">@ALERT.alertNum@:</td>
					<td width="267" class="wordBreak"><s:property value="historyAlert.alertId" /></td>
					<td class="rightBlueTxt" width="100">@ALERT.alertSource@:</td>
					<td class="wordBreak"><s:property value="historyAlert.source" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@ALERT.alertType@:</td>
					<td class="wordBreak"><s:property value="historyAlert.typeName" /></td>
					<td class="rightBlueTxt">@ALERT.alertLevel@:</td>
					<td class="wordBreak"><s:property value="historyAlert.levelName" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@ALERT.alertHost@:</td>
					<td class="wordBreak"><s:property value="historyAlert.host" /></td>
					<td class="rightBlueTxt">@ALERT.HappenTime@:</td>
					<td class="wordBreak"><s:date name="historyAlert.firstTime" format="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@ALERT.alertReason@:</td>
					<td class="wordBreak"><s:property value="historyAlert.message" /></td>
					<td class="rightBlueTxt">@ALERT.HappenTimes@:</td>
					<td class="wordBreak"><s:property value="historyAlert.happenTimes" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@ALERT.confirmPerson@:</td>
					<td class="wordBreak"><s:property value="historyAlert.confirmUser" /></td>
					<td class="rightBlueTxt">@ALERT.clearPerson@:</td>
					<td class="wordBreak"><s:property value="historyAlert.clearUser" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@ALERT.confirmTime@:</td>
					<td class="wordBreak">
						<s:if test="historyAlert.confirmUser!=null">
							<s:date name="historyAlert.confirmTime" format="yyyy-MM-dd HH:mm:ss" />
						</s:if>
					</td>
					<td class="rightBlueTxt">@ALERT.clearTime@:</td>
					<td class="wordBreak"><s:date name="historyAlert.clearTime" format="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				<tr>
					<td class="rightBlueTxt wordBreak">@ALERT.confirmNote@:</td>
					<td class="wordBreak"><s:property value="historyAlert.confirmMessage" /></td>
					<td class="rightBlueTxt">@ALERT.clearNote@:</td>
					<td class="wordBreak"><s:property value="historyAlert.clearMessage" /></td>
				</tr>
			</table>
		</div>
		<div class="noWidthCenterOuter clearBoth">
    	 	<ol class="upChannelListOl pB0 pT20 noWidthCenter">
				<li><a href="javascript:;" class="normalBtnBig" id="lastBtn" onclick="lastClick()"><span><i class="miniIcoArrUp"></i>@ALERT.lastItem@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" id="nextBtn" onclick="nextClick()"><span><i class="miniIcoArrDown"></i>@ALERT.nextItem@</span></a></li>
				<li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
			</ol>
		</div>
	</div>

</body>
</Zeta:HTML>