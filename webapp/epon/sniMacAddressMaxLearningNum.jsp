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
var entityId = '<s:property value="entityId"/>';
var sniId = '<s:property value="sniId"/>';
var sniMacAddrLearnMaxNum = '<s:property value="sniMacAddrLearnMaxNum"/>';
function initData() {
	if(sniMacAddrLearnMaxNum == 0){
		$('#sniMacAddressMaxLearningNum').val(0);
	}else{
		$('#sniMacAddressMaxLearningNum').val(sniMacAddrLearnMaxNum);
	}
}
function checkValid() {
	var maxLearningNum = parseInt($('#sniMacAddressMaxLearningNum').val());
	var reg = /^([0-9])+$/;
	if (!(reg.exec($('#sniMacAddressMaxLearningNum').val())) || maxLearningNum < 0 || maxLearningNum > 32767) {
		$("#sniMacAddressMaxLearningNum").focus();
		return false;
	}
	return true;
}
function saveClick() {
	if (!checkValid()) {
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.settingMacMaxLearn );
	Ext.Ajax.request({
		url : '/epon/saveSniMacAddressMaxLearningNum.tv?r=' + Math.random(),
		success : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.setMacMaxLearnOk );
			cancelClick();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.setMacMaxLearnEr );
		},
		params : {
			entityId: entityId,
			sniId: sniId,
			sniMacAddrLearnMaxNum: $('#sniMacAddressMaxLearningNum').val()
		}
	});
}
function cancelClick() {
	window.parent.closeWindow('sniMacAddressMaxLearningNum');
}
function authLoad(){
	var ids = new Array();
	ids.add("sniMacAddressMaxLearningNum");
	ids.add("saveBt");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
	<body class=openWinBody onload="initData();authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@EPON.macLearnSet@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter ">
					<tr>
						<td class="rightBlueTxt w120">@SERVICE.macMaxLearn@:</td>
						<td><input id="sniMacAddressMaxLearningNum" class="normalInput w150" maxlength=5 tooltip="@SERVICE.macMaxLearnTip@" /></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>