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
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entity.entityId"/>';
var currentId = '<s:property value="currentId"/>';
var sniId = '<s:property value="sniId"/>';
var sniName = unescape('<s:property value="sniAttribute.sniPortName"/>');
var sniAutoNegotiationMode = '<s:property value="sniAttribute.sniAutoNegotiationMode"/>';
var AUTO_STATUS =  ['','Auto-Negotiate','Half-10M','Full-10M','Half-100M','Full-100M','Full-1000M','Full-10000M'];
var portIsXGUxFiber = <s:property value="portIsXGUxFiber"/>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var portSubType = '<s:property value="sniPortType"/>';

function doOnload(){
	$("#sniName").val(sniName);
	/* if(portSubType == 'geCopper'){
		AUTO_STATUS = ['','Auto-Negotiate','Half-10M','Full-10M','Half-100M','Full-100M','Full-1000M'];
	}else if(portSubType == 'geFiber'){
		AUTO_STATUS = ['','Auto-Negotiate','','','','','Full-1000M'];
	}else if(portSubType == 'xeFiber'){
		AUTO_STATUS = ['', '', '', '', '', '', '', 'Full-10000M'];
	} */
	var pa = $("#sniAutoNegotiationMode");
	for(var a=0,len=AUTO_STATUS.length; a<len; a++){
		if(AUTO_STATUS[a]){
			pa.append(String.format("<option value={0}>{1}</option>", a, AUTO_STATUS[a]));
		}
	}
	pa.val(sniAutoNegotiationMode);
}
function checkChange(){
	var changeFlag = false;
	var name = $("#sniName").val();
	if (name != sniName) {
		changeFlag = true;
	}
	var mode = $("#sniAutoNegotiationMode").val();
	if (mode != sniAutoNegotiationMode) {
		changeFlag = true;
	}
	if (changeFlag) {
		R.saveBt.setDisabled(false);
	} else {
		R.saveBt.setDisabled(true);
	}
	checkValid("sniName");
}
function checkValid(id) {
	var value = $("#" + id).val();
	if (id == "sniName") {
 		var reg = /^[a-zA-Z0-9-\s_:;/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/;
	    if(!reg.test(value)) {
	    	R.saveBt.setDisabled(true);
			$("#" + id).focus();
	    }
	}
}

function saveClick() {
	var sniName2 = $("#sniName").val();
	if( !Validator.isPortName(sniName2) ){
		$("#sniName").focus();
		return;
	}
	
	var sniAutoNegotiationMode2 = $("#sniAutoNegotiationMode").val();
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmSaveCfg , function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.savingCfg , 'ext-mb-waiting');
			Ext.Ajax.request({
				url:"/epon/modifySniBasicConfig.tv?r=" + Math.random(),
				success: function (response) {
				window.parent.closeWaitingDlg();
				var json = Ext.util.JSON.decode(response.responseText);
				if (json.message) {
					window.parent.showMessageDlg(I18N.COMMON.tip, json.message);
				} else {
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">'+I18N.SERVICE.savingCfgOk+'</b>'
		       	    });
					//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.savingCfgOk )
					updateOltJson(currentId, 'sniPortName', sniName2);
					if(sniAutoNegotiationMode != sniAutoNegotiationMode2){
						updateOltJson(currentId, 'sniAutoNegotiationMode', sniAutoNegotiationMode2);
						updateOltJson(currentId, 'sniAutoNegotiationStatus', AUTO_STATUS[json.status]);
					}
					try{
						window.parent.getFrame("entity-" + entityId).refreshNavgatorTree();//刷新树菜单;
					}catch(err){}
					cancelClick();
				}
		    }, failure: function (response) {
		    	window.parent.closeWaitingDlg();
		        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.saveCfgError )
		    }, params: {entityId : entityId, sniId : sniId, sniName:sniName2,sniPortType:portSubType,sniAutoNegotiationMode : sniAutoNegotiationMode2}
			});
	}) 
}
function updateOltJson(currentId, attr, value) {
	window.parent.getFrame("entity-" + entityId).updateOltJson(currentId, attr, value);
}
function cancelClick() {
	// 关闭窗口后，面板页面重新计时。
	window.parent.closeWindow('sniBaseConfig');
}
function authLoad(){
	if(!operationDevicePower){
	    $("#sniName").attr("disabled",true);
	    $("#sniAutoNegotiationMode").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
	if(!refreshDevicePower){
	    R.fetchBt.setDisabled(true);
    }
}

$(function(){
	R.saveBt.setDisabled(true);
});

function refreshData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/refreshSniBaseInfo.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			sniId : sniId
		},
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			window.location.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}
</script>
</head>
	<body class=openWinBody onload="doOnload();authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.basicConfig@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w220">@SERVICE.sniName@:</td>
						<td><input type=text id="sniName" class="normalInput w150" maxlength="63"
							onkeyup="checkChange();" tooltip="@COMMON.portName@" /></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@SERVICE.autoNegMode@:</td>
						<td><div>
								<select id="sniAutoNegotiationMode" class="normalSel w150"
									onchange="checkChange();">
								</select>
							</div></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="fetchBt" onClick="refreshData()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>