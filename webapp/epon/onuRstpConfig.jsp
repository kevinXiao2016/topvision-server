<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
var onuId = <s:property value = "onuId"/>;
var rstpMode = '<s:property value="oltOnuRstp.rstpBridgeMode"/>';//用于标识真实值的
function fetchClick(){
	window.parent.showWaitingDlg('@COMMON.wait@', '@RSTP.gettingBdg@' , 'ext-mb-waiting');
	$.ajax({
		url: '/onu/fetchOnuRstpMode.tv', 
		type: 'POST',
		data: 'entityId='+entityId+'&onuId='+onuId,
		success: function(json) {
			if(json.message == 'success'){
				$("#rstpMode").val(json.oltOnuRstp.rstpBridgeMode);
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@RSTP.getBdgOk@'
    			});
			}else{
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@RSTP.getBdgEr@'
    			});
			}
		}, error: function() {
			top.nm3kRightClickTips({
				title: '@COMMON.tip@',
				html: '@RSTP.getBdgEr@'
			});
		}, cache: false
	});//从设备获取ONURSTP桥模式
}
function saveClick() {
	var modeId = $("#rstpMode").val();
	window.parent.showWaitingDlg('@COMMON.wait@', '@RSTP.settingBdg@' , 'ext-mb-waiting');
	$.ajax({
		url: '/onu/modifyOnuRstpMode.tv', 
		type: 'POST',
		data: 'entityId='+entityId+'&onuId='+onuId+'&onuRstpBridgeMode='+modeId,
		success: function(text) {
			if(text == 'success'){
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@RSTP.setBdgOk@'
    			});
			}else{
				top.nm3kRightClickTips({
    				title: '@COMMON.tip@',
    				html: '@RSTP.setBdgEr@'
    			});
			}
			cancelClick();
		}, error: function() {
			top.nm3kRightClickTips({
				title: '@COMMON.tip@',
				html: '@RSTP.setBdgEr@'
			});
		}, cache: false
	});//提交ONURSTP桥模式的修改
}
function cancelClick() {
	window.parent.closeWindow('onuRstpConfig');
}
function loadMode(){
	$("#rstpMode").val(rstpMode);
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function authLoad(){
	if(!operationDevicePower){
	    $("#rstpMode").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="loadMode();authLoad();">
		<div class="openWinHeader">
			<div class="openWinTip">@RSTP.onuRstpMode@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT30">
			<table class="zebraTableRows">
				<tr>
					<td class="withoutBorderBottom rightBlueTxt w200">@RSTP.rstpMode@:</td>
					<td class="withoutBorderBottom"><select id="rstpMode"
						class="normalSel w200">
							<option value="1">@RSTP.on@</option>
							<option value="2">@COMMON.close@</option>
							<option value="3">@RSTP.transparent@</option>
					</select></td>
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