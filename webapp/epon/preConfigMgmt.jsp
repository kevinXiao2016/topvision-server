<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
 	library ext
    library jquery
    library zeta
	module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
var currentId = '<s:property value="currentId"/>';
var slotId = <s:property value="slotId"/>;
var deviceType = '<s:property value="deviceType"/>';
var preConfigType = '<s:property value="preConfigType"/>';
var actualConfigType = '<s:property value="actualConfigType"/>';
function initData() {
	var boardType = ["Non-Board","MPUA","MPUB","EPUA","EPUB","GEUA","GEUB","XGUA","XGUB","XGUC","XPUA","GPUA","MEUA","MEUB","MEFA","MEFB","EPUC","EPUD","MEFC","MEFD"];
	if(actualConfigType == null || actualConfigType == ''){
		actualConfigType = 0;
	}

	if(actualConfigType == 255){
		$("#actualConfigType").text("Unknown");
	}else{
		$("#actualConfigType").text(boardType[actualConfigType]);
	}
	$("#preConfigType").val(preConfigType);
	   
}
function saveClick() {
	var configType = $("#preConfigType").val();
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.EPON.cfmMdfPretype , function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.EPON.mdfingPretype , 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/modifyPreconfigMgmt.tv?r=" + Math.random(),
			timeout: 600000,
			success: function (response) {
				window.parent.closeWaitingDlg();
				var json = Ext.util.JSON.decode(response.responseText);
				if (json.message) {
					if (json.type) {
						window.parent.showConfirmDlg(I18N.COMMON.tip, json.message, function(type) {
							if (type == 'no') {
								return;
							}
							window.top.showWaitingDlg(I18N.COMMON.wait, I18N.EPON.refreshingSlotInfo , 'ext-mb-waiting');
							Ext.Ajax.request({
								url: '/epon/refreshBoardInfo.tv?r=' + Math.random(),
								disableCaching :true,
								params: {
									entityId: entityId,
									slotId: slotId
								},
								success: function () {
									window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.refreshSlotInfoOk );
									// 刷新OLT面板
									window.parent.getFrame("entity-" + entityId).location.reload();
									window.parent.closeWindow('preConfigMgmt');
								},
								error: function() {
									window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.refreshSlotInfoEr );
								}
							});
						});
					} else {
						if(json.message == "error"){
							json.message = I18N.EPON.mdfPretypeEr
						}
						window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
					}
				} else {
                    window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.mdfPretypeOk )
					reloadOltJson();
					cancelClick();
				}
		    }, error: function () {
		        window.parent.closeWaitingDlg();
		        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.mdfPretypeEr )
		        cancelClick();
		    }, params: {entityId : entityId, slotId : slotId, preConfigType : configType}});
	});
}
/* function checkChange() {
	var preConfigType2 = $("#preConfigType").val();
	if (preConfigType2 != preConfigType) {
		$("#saveBt").attr("disabled", false);
	} else {
		$("#saveBt").attr("disabled", true);
	}
} */
function reloadOltJson() {
	window.parent.getFrame("entity-" + entityId).reloadOltJson();
}
function cancelClick() {
	// 关闭窗口后，面板页面重新计时。
	window.parent.getFrame("entity-" + entityId).timer = 0;
	window.parent.closeWindow('preConfigMgmt');
}
</script>
</head>
<body class="openWinBody" onload="initData()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.preCfgMgmt@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10">
		<table class="mCenter zebraTableRows" >
			<tr>
				<td class="rightBlueTxt w220">@EPON.curBDType@</td>
				<td ><div id="actualConfigType"></div>
				</td>
			</tr>
			<tr  class="darkZebraTr">
				<td class="rightBlueTxt w220">@EPON.preBDtype@</td>
				<td><select id="preConfigType"  class="normalSel w200">
					<option value="0">Non-Board</option>
					<option value="3">EPUA</option>
					<option value="4">EPUB</option>
					<option value="15">EPUC</option>
					<option value="14">GPUA</option>
					<s:if test="deviceType==10001" >
						<option value="5">GEUA</option>
						<option value="6">GEUB</option>
						<option value="7">XGUA</option>
						<option value="8">XGUB</option>
						<option value="9">XGUC</option>
					</s:if>
					<s:if test="deviceType==10003" >
						<option value="5">GEUA</option>
						<option value="6">GEUB</option>
						<option value="7">XGUA</option>
						<option value="8">XGUB</option>
						<option value="9">XGUC</option>
						<option value="10">XPUA</option>
					</s:if>
					<s:elseif test="deviceType==10002">
					</s:elseif>
				</select></td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="saveClick()" icon="miniIcoData">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>