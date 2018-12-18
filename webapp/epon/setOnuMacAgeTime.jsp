<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var uniMacAddrAgeTime = '${oltOnuMacAge}';
function initData() {
	if(uniMacAddrAgeTime == -1){
		$('#onuMacAgeTime').val();
	}else{
		$('#onuMacAgeTime').val(uniMacAddrAgeTime);
		$("#onuMacAgeTime").focus();
	}
}
function checkValid() {
	var agingTime = parseInt($('#onuMacAgeTime').val());
	if (isNaN(agingTime) || agingTime < 0 || agingTime > 65535) {
		$("#onuMacAgeTime").focus();
		return false;
	}
	return true;
}
function fetchClick() {
	window.top.showWaitingDlg(I18N.COMMON.wait, '@UNI.fetchingMacAge@' , 'ext-mb-waiting');
	$.ajax({
		url : '/onu/fetchOnuMacAgeTime.tv',
		data : {
			entityId: ${entityId},
			uniIndex: ${uniIndex},
			onuId : ${onuId},
		},
		success : function(json) {
			$('#onuMacAgeTime').val(json.oltOnuMacAge);
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: '@UNI.fetchMacAgeOk@'
			});
		},
		error : function(text) {
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: '@UNI.fetchMacAgeEr@'
			});
		}
	});
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.UNI.settingMacAge , 'ext-mb-waiting');
	$.ajax({
		url : '/onu/saveOnuMacAgeTime.tv',
		data : {
			entityId: ${entityId},
			uniIndex: ${uniIndex},
			onuId : ${onuId},
			oltOnuMacAge: $('#onuMacAgeTime').val() || 0
		},
		success : function() {
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: I18N.UNI.setMacAgeOk
			});
			cancelClick();
		},
		error : function(text) {
			top.nm3kRightClickTips({
				title: I18N.COMMON.tip,
				html: I18N.UNI.setMacAgeEr
			});
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('setOnuMacAgeTime');
}
function authLoad(){
	if(!operationDevicePower){
	    $("#onuMacAgeTime").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="initData();authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@UNI.setOnuMacAge@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT40">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w220">@SERVICE.macAgeTime@:</td>
					<td><input id="onuMacAgeTime" tooltip="@UNI.range65535@" class="normalInput" maxlength="5">@COMMON.S@</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="fetchBt" onClick="fetchClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>