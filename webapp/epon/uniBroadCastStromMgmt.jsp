<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="../include/nocache.inc"%>
<%@ include file="../include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="i18n" />
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<link rel="stylesheet" type="text/css" href="../css/gui.css" />
<link rel="stylesheet" type="text/css" href="../css/ext-all.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/xtheme.css" />
<link rel="stylesheet" type="text/css"
	href="../css/<%= cssStyleName %>/mytheme.css" />
<%@ include file="../include/tabPatch.inc"%>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
// 千兆口速率最大值：250000Kbps
var maxRate = 250000;
var uniId = '<s:property value="uniStorm.uniId"/>';
function initData() {
	var unicastStormEnable = '<s:property value="uniStorm.unicastStormEnable" />';
	if (unicastStormEnable != 1) {
		$('#unicastStormInPacketRate').attr('disabled', true);
		$('#unicastStormOutPacketRate').attr('disabled', true);
	}
	var multicastStormEnable = '<s:property value="uniStorm.multicastStormEnable" />';
	if (multicastStormEnable != 1) {
		$('#multicastStormInPacketRate').attr('disabled', true);
		$('#multicastStormOutPacketRate').attr('disabled', true);
	}
	var broadcastStormEnable = '<s:property value="uniStorm.broadcastStormEnable" />';
	if (broadcastStormEnable != 1) {
		$('#broadcastStormInPacketRate').attr('disabled', true);
		$('#broadcastStormOutPacketRate').attr('disabled', true);
	}
}
function checkValid() {
	if ($('#unicastStormEnable').attr('checked')) {
		var uin = $('#unicastStormInPacketRate').val();
		var uOut = $('#unicastStormOutPacketRate').val();
		if(uin == "" && uOut == ""){
			return false ;
		}
		if(uin != ""){
			if (isNaN(uin) || uin < 984 || uin > maxRate) {
				$("#unicastStormInPacketRate").focus();
				return false;
			}
		}
		if(uOut != ""){
			if (isNaN(uOut) || uOut < 984 || uOut > maxRate) {
				$("#unicastStormOutPacketRate").focus();
				return false;
			}
		}
	}
	if ($('#multicastStormEnable').attr('checked')) {
		var min = $('#multicastStormInPacketRate').val();
		var mOut = $('#multicastStormOutPacketRate').val();
		if(min == "" && mOut == ""){
			return false ;
		}
		if(min != ""){
			if (isNaN(min) || min < 984 || min > maxRate) {
				$("#multicastStormInPacketRate").focus();
				return false;
			}
		}
		if(mOut != ""){
			if (isNaN(mOut) || mOut < 984 || mOut > maxRate) {
				$("#multicastStormOutPacketRate").focus();
				return false;
			}
		}	
	}
	if ($('#broadcastStormEnable').attr('checked')) {
		var bin = $('#broadcastStormInPacketRate').val();
		var bOut = $('#broadcastStormOutPacketRate').val();
		if(bin == "" && bOut == ""){
			return false ;
		}
		if(bin != ""){
			if (isNaN(bin) || bin < 984 || bin > maxRate) {
				$("#broadcastStormInPacketRate").focus();
				return false;
			}
		}
		if(bOut != ""){
			if (isNaN(bOut) || bOut < 984 || bOut > maxRate) {
				$("#broadcastStormOutPacketRate").focus();
				return false;
			}
		}
	}
	return true;
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	var unicastStormEnable = $('#unicastStormEnable').attr('checked') ? 1 : 2;
	var unicastStormInPacketRate = $('#unicastStormEnable').attr('checked') ? $('#unicastStormInPacketRate').val() : 0;
	var unicastStormOutPacketRate = $('#unicastStormEnable').attr('checked') ? $('#unicastStormOutPacketRate').val() : 0;
	var multicastStormEnable = $('#multicastStormEnable').attr('checked') ? 1 : 2;
	var multicastStormInPacketRate = $('#multicastStormEnable').attr('checked') ? $('#multicastStormInPacketRate').val() : 0;
	var multicastStormOutPacketRate = $('#multicastStormEnable').attr('checked') ? $('#multicastStormOutPacketRate').val() : 0;
	var broadcastStormEnable = $('#broadcastStormEnable').attr('checked') ? 1 : 2;
	var broadcastStormInPacketRate = $('#broadcastStormEnable').attr('checked') ? $('#broadcastStormInPacketRate').val() : 0;
	var broadcastStormOutPacketRate = $('#broadcastStormEnable').attr('checked') ? $('#broadcastStormOutPacketRate').val() : 0;
	window.parent.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.savingBdcastParam, 'ext-mb-waiting');
	$.ajax({
		url : '/onu/updateUniBroadCastStromMgmt.tv',
		cache:false,
		method:'post',
		data: {entityId: entityId,
			   uniId: uniId,
			   unicastStormEnable: unicastStormEnable,
			   unicastStormInPacketRate: unicastStormInPacketRate,
			   unicastStormOutPacketRate: unicastStormOutPacketRate,
			   multicastStormEnable: multicastStormEnable,
			   multicastStormInPacketRate: multicastStormInPacketRate,
			   multicastStormOutPacketRate: multicastStormOutPacketRate,
			   broadcastStormEnable: broadcastStormEnable,
			   broadcastStormInPacketRate: broadcastStormInPacketRate,
			   broadcastStormOutPacketRate: broadcastStormOutPacketRate},
		success : function(response) {
			window.parent.closeWaitingDlg();
		    var text = response;
			if(text == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.saveBdcastParamOk);
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.saveBdcastParamEr);
			}
			cancelClick();
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.saveBdcastParamEr);
		}
	});
}
function cancelClick() {
	window.top.closeWindow("uniStorm");
}
function enableChange(id) {
	if (id == 'unicastStormEnable') {
		if ($('#unicastStormEnable').attr('checked')) {
			$('#unicastStormInPacketRate').attr('disabled', false);
			$('#unicastStormOutPacketRate').attr('disabled', false);
		} else {
			$('#unicastStormInPacketRate').attr('disabled', true);
			$('#unicastStormOutPacketRate').attr('disabled', true);
		}
	} else if (id == 'multicastStormEnable') {
		if ($('#multicastStormEnable').attr('checked')) {
			$('#multicastStormInPacketRate').attr('disabled', false);
			$('#multicastStormOutPacketRate').attr('disabled', false);
		} else {
			$('#multicastStormInPacketRate').attr('disabled', true);
			$('#multicastStormOutPacketRate').attr('disabled', true);
		}
	} else if (id == 'broadcastStormEnable') {
		if ($('#broadcastStormEnable').attr('checked')) {
			$('#broadcastStormInPacketRate').attr('disabled', false);
			$('#broadcastStormOutPacketRate').attr('disabled', false);
		} else {
			$('#broadcastStormInPacketRate').attr('disabled', true);
			$('#broadcastStormOutPacketRate').attr('disabled', true);
		}
	}
}
</script>
</head>
<body class=POPUP_WND style="padding: 15px;" onload="initData()">
	<div class=formtip id=tips style="display: none"></div>
	<center>
		<fieldset style="background-color: #ffffff; width: 480px;">
			<fieldset style='margin: 10px'>
				<legend><fmt:message bundle="${i18n}" key="SERVICE.unknownPkg" /></legend>
				<table>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.unknownPkgDecEn" /></td>
						<td width=100px><s:if test="uniStorm.unicastStormEnable == 1">
								<input type="checkbox" id="unicastStormEnable"
									onclick="enableChange('unicastStormEnable')" checked>
							</s:if> <s:else>
								<input type="checkbox" id="unicastStormEnable"
									onclick="enableChange('unicastStormEnable')">
							</s:else></td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.unknownPkgInDerc" />:</td>
						<td width=100px><input type=text
							id="unicastStormInPacketRate"
							value="<s:property value='uniStorm.unicastStormInPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('unicastStormInPacketRate', String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.unknownPkgInDerc" />:</td>
						<td width=100px><input type=text
							id="unicastStormOutPacketRate"
							value="<s:property value='uniStorm.unicastStormOutPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('unicastStormOutPacketRate',String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
				</table>
			</fieldset>
			<fieldset style='margin: 10px'>
				<legend><fmt:message bundle="${i18n}" key="SERVICE.mvlan" /></legend>
				<table>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.mvlanDec" /></td>
						<td width=100px><s:if
								test="uniStorm.multicastStormEnable == 1">
								<input type=checkbox id="multicastStormEnable"
									onclick="enableChange('multicastStormEnable')" checked>
							</s:if> <s:else>
								<input type=checkbox id="multicastStormEnable"
									onclick="enableChange('multicastStormEnable')">
							</s:else></td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.mvlanInPkgRate" />:</td>
						<td width=100px><input type=text
							id="multicastStormInPacketRate"
							value="<s:property value='uniStorm.multicastStormInPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('multicastStormInPacketRate', String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.mvlanInPkgRate" />:</td>
						<td width=100px><input type=text
							id="multicastStormOutPacketRate"
							value="<s:property value='uniStorm.multicastStormOutPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('multicastStormOutPacketRate', String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
				</table>
			</fieldset>
			<fieldset style='margin: 10px'>
				<legend><fmt:message bundle="${i18n}" key="SERVICE.broadCast" /></legend>
				<table>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.bdcastDecEn" /></td>
						<td width=100px><s:if
								test="uniStorm.broadcastStormEnable == 1">
								<input type=checkbox id="broadcastStormEnable"
									onclick="enableChange('broadcastStormEnable')" checked>
							</s:if> <s:else>
								<input type=checkbox id="broadcastStormEnable"
									onclick="enableChange('broadcastStormEnable')">
							</s:else></td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.bdcastInPkgRate" />:</td>
						<td width=100px><input type=text
							id="broadcastStormInPacketRate"
							value="<s:property value='uniStorm.broadcastStormInPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('broadcastStormInPacketRate', String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
					<tr>
						<td width=150px style="padding-left: 50px;"><fmt:message bundle="${i18n}" key="SERVICE.bdcastInPkgRate" />:</td>
						<td width=100px><input type=text
							id="broadcastStormOutPacketRate"
							value="<s:property value='uniStorm.broadcastStormOutPacketRate'/>"
							class=iptxt
							onfocus="inputFocused('broadcastStormOutPacketRate',String.format(I18N.SERVICE.plsIptMaxRateNum , maxRate), 'iptxt_focused')"
							onblur="inputBlured(this, 'iptxt');"
							onclick="clearOrSetTips(this);" maxlength="7"
							style="width: 150px;"></td>
						<td>&nbsp;Kbps</td>
					</tr>
				</table>
			</fieldset>
			&nbsp;
		</fieldset>
		<div style="padding-left: 325px;padding-top:5px;">
			<button id=saveBt class=BUTTON75 type="button"
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMouseDown="this.className='BUTTON_PRESSED75'"
				onclick="saveClick()"><fmt:message bundle="${i18n}" key="COMMON.saveCfg" /></button>
			&nbsp;
			<button id=cancelBt class=BUTTON75 type="button"
				onMouseOver="this.className='BUTTON_OVER75'"
				onMouseOut="this.className='BUTTON75'"
				onMousedown="this.className='BUTTON_PRESSED75'"
				onclick="cancelClick()"><fmt:message bundle="${i18n}" key="COMMON.close" /></button>
		</div>
	</center>
</body>
</html>