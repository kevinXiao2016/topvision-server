<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value='entityId'/>;
var uniId = <s:property value='uniId'/>;
var uniMacAddrAgeTime = '<s:property value="oltUniExtAttribute.macAge"/>';
function initData() {
	if(uniMacAddrAgeTime == -1){
		$('#uniMacAgeTime').val();
	}else{
		$('#uniMacAgeTime').val(uniMacAddrAgeTime);
		$("#uniMacAgeTime").focus();
	}
}
function checkValid() {
	var agingTime = parseInt($('#uniMacAgeTime').val());
	if (isNaN(agingTime) || agingTime < 0 || agingTime > 286) {
		$("#uniMacAgeTime").focus();
		return false;
	}
	return true;
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.UNI.settingMacAge , 'ext-mb-waiting');
	Ext.Ajax.request({
		url : '/onu/saveUniMacAgeTime.tv?r=' + Math.random(),
		success : function(text) {
			if(text.responseText == 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.setMacAgeOk );
				cancelClick();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.setMacAgeEr , 'error');
			}
		},
		failure : function(text) {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.UNI.setMacAgeEr );
		},
		params : {
			entityId: entityId,
			uniId: uniId,
			uniMacAddrAgeTime: $('#uniMacAgeTime').val()
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('setUniMacAgeTime');
}
function authLoad(){
	if(!operationDevicePower){
	    $("#uniMacAgeTime").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="initData();authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.uniMacAgeTimeConfig@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT40">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt w220">@SERVICE.macAgeTime@:</td>
					<td><input id="uniMacAgeTime" tooltip="@UNI.range286@" class="normalInput" maxlength="3">@COMMON.S@</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>