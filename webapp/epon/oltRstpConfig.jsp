<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
var entityId = '${entityId}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
function saveClick()
{
	 var stpGlobalSetEnable = $('#stpGlobalSetEnable').attr('checked')?1:2;
	 var stpGlobalSetVersion = $("#stpGlobalSetVersion").val();
	 var stpGlobalSetPriority = $("#stpGlobalSetPriority").val();
	 var stpGlobalSetBridgeMaxAge = $("#stpGlobalSetBridgeMaxAge").val();
	 if(!checkInput(stpGlobalSetBridgeMaxAge)||stpGlobalSetBridgeMaxAge>40||stpGlobalSetBridgeMaxAge<6){
			Zeta$("stpGlobalSetBridgeMaxAge").focus();
			return ;
	 }
	 var stpGlobalSetBridgeHelloTime = $("#stpGlobalSetBridgeHelloTime").val();
	 if(!checkInput(stpGlobalSetBridgeHelloTime)||stpGlobalSetBridgeHelloTime>10||stpGlobalSetBridgeHelloTime<1){
			Zeta$("stpGlobalSetBridgeHelloTime").focus();
			return ;
	 }
	 var stpGlobalSetBridgeForwardDelay = $("#stpGlobalSetBridgeForwardDelay").val();
	 if(!checkInput(stpGlobalSetBridgeForwardDelay)||stpGlobalSetBridgeForwardDelay>30||stpGlobalSetBridgeForwardDelay<4){
			Zeta$("stpGlobalSetBridgeForwardDelay").focus();
			return ;
	 }
	 var stpGlobalSetRstpTxHoldCount = $("#stpGlobalSetRstpTxHoldCount").val();
	 if(!checkInput(stpGlobalSetRstpTxHoldCount)||stpGlobalSetRstpTxHoldCount>10||stpGlobalSetRstpTxHoldCount<1){
			Zeta$("stpGlobalSetRstpTxHoldCount").focus();
			return ;
	 }
	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.RSTP.setting , 'ext-mb-waiting');
	  	Ext.Ajax.request({
	  		url : '/epon/rstp/updateStpGlobalConfig.tv?r=' + Math.random(),
	  		success : function(text) {
	  			if (text.responseText != null && text.responseText != "success") {  
	  				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.rstpCfgEr , 'error');
	  			} else {
	  				window.top.closeWaitingDlg();
	  	             // 配置成功后，修改面板图中RSTP使能状态
	  	             //updateRefresh();
	  	             window.parent.getFrame("entity-" + entityId).olt.stpGlobalSetEnable = stpGlobalSetEnable;
	  	             window.parent.getFrame("entity-" + entityId).oltUnrenderFlag = false;
	  	             window.parent.getFrame("entity-" + entityId).refreshDeviceMenu();
	  	             top.afterSaveOrDelete({
	  	 				title: '@COMMON.tip@',
	  	 				html: '<b class="orangeTxt">@RSTP.rstpCfgOk@</b>'
	  	 			 });
	  	             cancelClick();
	  			} 
	  		},
	  		failure : function() {
	  			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.rstpCfgEr)
	  		},
	  		params : {
	  			entityId: entityId,
	  			stpGlobalSetEnable : stpGlobalSetEnable,
	  			stpGlobalSetVersion : stpGlobalSetVersion,
	  			stpGlobalSetPriority : stpGlobalSetPriority,
	  			stpGlobalSetBridgeMaxAge : stpGlobalSetBridgeMaxAge,
	  			stpGlobalSetBridgeHelloTime :stpGlobalSetBridgeHelloTime,
	  			stpGlobalSetBridgeForwardDelay :stpGlobalSetBridgeForwardDelay,
	  			stpGlobalSetRstpTxHoldCount : stpGlobalSetRstpTxHoldCount
	  		}
	  	});
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
function cancelClick(){
	window.top.closeWindow("oltRstpConfig");
}
function load(){
	var stpGlobalSetVersion = '${oltStpGlobalConfig.version}';
	var stpGlobalSetPriority = '${oltStpGlobalConfig.priority}';
	$("#stpGlobalSetVersion").val(stpGlobalSetVersion);
	$("#stpGlobalSetPriority").val(stpGlobalSetPriority);
	var stpEnable = '${oltStpGlobalConfig.enable}';
	if(stpEnable == 1){
		Zeta$('stpGlobalSetEnable').checked = true;
	}else{
		Zeta$('stpGlobalSetVersion').disabled = true;
   		Zeta$('stpGlobalSetPriority').disabled = true;
   		Zeta$('stpGlobalSetBridgeMaxAge').disabled = true;
   		Zeta$('stpGlobalSetBridgeHelloTime').disabled = true;
   		Zeta$('stpGlobalSetBridgeForwardDelay').disabled = true;
   		Zeta$('stpGlobalSetRstpTxHoldCount').disabled = true;
	}
	
	//设备操作权限--------------------------------------------
	var ids = new Array();
	if(!operationDevicePower){
		R.okBt.setDisabled(true)
	}
	ids.add("stpGlobalSetEnable");
	ids.add("stpGlobalSetRstpTxHoldCount");
	ids.add("stpGlobalSetBridgeForwardDelay");
	ids.add("stpGlobalSetBridgeHelloTime");
	ids.add("stpGlobalSetBridgeMaxAge");
	ids.add("stpGlobalSetPriority");
	ids.add("stpGlobalSetVersion");
	operationAuthInit(operationDevicePower,ids);
	//-------------------------------------------------------
    if(!refreshDevicePower){
        R.fetch.setDisabled(true)
    }
}
function stpCheck(){
	var stpEnable = $('#stpGlobalSetEnable').attr('checked')?1:2;
	if(stpEnable == 1){
		Zeta$('stpGlobalSetEnable').checked = true;
		Zeta$('stpGlobalSetVersion').disabled = false;
   		Zeta$('stpGlobalSetPriority').disabled = false;
   		Zeta$('stpGlobalSetBridgeMaxAge').disabled = false;
   		Zeta$('stpGlobalSetBridgeHelloTime').disabled = false;
   		Zeta$('stpGlobalSetBridgeForwardDelay').disabled = false;
   		Zeta$('stpGlobalSetRstpTxHoldCount').disabled = false;
	}else{
		Zeta$('stpGlobalSetEnable').checked = false;
		Zeta$('stpGlobalSetVersion').disabled = true;
   		Zeta$('stpGlobalSetPriority').disabled = true;
   		Zeta$('stpGlobalSetBridgeMaxAge').disabled = true;
   		Zeta$('stpGlobalSetBridgeHelloTime').disabled = true;
   		Zeta$('stpGlobalSetBridgeForwardDelay').disabled = true;
   		Zeta$('stpGlobalSetRstpTxHoldCount').disabled = true;
	}
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching)
	 $.ajax({
	        url: '/epon/rstp/refreshStpGlobalConfig.tv',
	        type: 'POST',
	        data: "entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpOk)
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpEr, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpEr,'error');
	    }, cache: false
	    });
}

function updateRefresh(){
	 $.ajax({
	        url: '/epon/rstp/refreshStpGlobalConfig.tv',
	        type: 'POST',
	        data: "entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.refreshEr ,'error');
	 }, cache: false
	 });
}

</script>
</head>
<body class="openWinBody" onload="load()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@RSTP.label@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" style="width: 100%">
				<tr>
					<td class="w120 rightBlueTxt"><label>STP@RSTP.enableStatus@:</label></td>
					<td colspan=3><input type="checkbox" id="stpGlobalSetEnable"
						value="1" onClick="stpCheck()">@RSTP.on@</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.topoSwapTime@:</label></td>
					<td class="w100"><input type="text" id="stpGlobalSetTimeSinceTopologyChange"
						value="${oltStpGlobalConfig.timeSinceTopologyChange}" 
						disabled size="13">@COMMON.S@
					</td>
					<td class="w60 rightBlueTxt"><label>@RSTP.topoSwapCount@:</label></td>
					<td class="w160"><input type="text" id="stpGlobalSetTopChanges"
						value="${oltStpGlobalConfig.topChanges}" size="13" disabled>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.rootBdgId@:</label></td>
					<td ><input type="text"  id="stpGlobalSetDesignatedRoot"
						value="${oltStpGlobalConfig.stpGlobalSetDesginatedRootString}" disabled size="23">
					</td>
					<td class="rightBlueTxt"><label>@RSTP.rootPathSpend@:</label></td>
					<td ><input type="text" id="stpGlobalSetRootCost"
						value="${oltStpGlobalConfig.rootCost}" size="13" disabled>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.rootPort@:</label>
					</td>
					<td ><input type="text"  id="stpGlobalSetRootPort"
						value="${oltStpGlobalConfig.rootPortString}" size="13" disabled>
					</td>
					<td class="rightBlueTxt"><label>@RSTP.ageTime@:</label>
					</td>
					<td ><input type="text" id="stpGlobalSetMaxAge"
						value="${oltStpGlobalConfig.stpGlobalSetMaxAgeString}" size="13" disabled>
						@COMMON.S@</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.reportSendTick@:</label>
					</td>
					<td ><input type="text" id="stpGlobalSetHelloTime"
						value="${oltStpGlobalConfig.stpGlobalSetHelloTimeString}" size="13" disabled>
						@COMMON.S@</td>
					<td class="rightBlueTxt"><label>@RSTP.translateDelay@:</label></td>
					<td><input type="text"
						id="stpGlobalSetForwardDelay" value="${oltStpGlobalConfig.stpGlobalSetForwardDelayString}" size="13" disabled>
						@COMMON.S@</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.protocolVersion@:</label></td>
					<td ><select id="stpGlobalSetVersion">
							<option value="1" selected>RSTP</option>
							<option value="2">STP</option>
					</select></td>
					<td class="rightBlueTxt"><label>@COMMON.priority@:</label></td>
					<td ><select id="stpGlobalSetPriority">
							<option value="0">0</option>
							<option value="4096">4096</option>
							<option value="8192">8192</option>
							<option value="12288">12288</option>
							<option value="16384">16384</option>
							<option value="20480">20480</option>
							<option value="24576">24576</option>
							<option value="28672">28672</option>
							<option value="32768">32768</option>
							<option value="36864">36864</option>
							<option value="40960">40960</option>
							<option value="45056">45056</option>
							<option value="49152">49152</option>
							<option value="53248">53248</option>
							<option value="57344">57344</option>
							<option value="61440">61440</option>
					</select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.bridgeAgeTime@:</label>
					</td>
					<td ><input type="text" id="stpGlobalSetBridgeMaxAge"
						value="${oltStpGlobalConfig.stpGlobalSetBridgeMaxAgeString}" size="13"
						tooltip="@RSTP.range640@">@COMMON.S@</td>
					<td class="rightBlueTxt"><label>@RSTP.bridgeReportSendTick@:</label>
					</td>
					<td ><input type="text" id="stpGlobalSetBridgeHelloTime"
						value="${oltStpGlobalConfig.stpGlobalSetBridgeHelloTimeString}" size="13"
						tooltip="@RSTP.range10@">
						@COMMON.S@</td>
				</tr>
				<tr>
					<td class="rightBlueTxt"><label>@RSTP.bridgeSwapDelay@:</label></td>
					<td ><input type="text" id="stpGlobalSetBridgeForwardDelay" 
						value="${oltStpGlobalConfig.stpGlobalSetBridgeForwardDelayString}" size="13"
						tooltip="@RSTP.range30@" />@COMMON.S@	</td>
					<td class="rightBlueTxt"><label>@RSTP.bridgeRptSndRate@:</label></td>
					<td >
						<input type="text" id="stpGlobalSetRstpTxHoldCount"
						value="${oltStpGlobalConfig.rstpTxHoldCount}" size="13"
						tooltip="@RSTP.range10@" >packets/s
					</td>
				</tr>
			</table>
			</form>
		</div>
	
    <Zeta:ButtonGroup>
		<Zeta:Button id="fetch" onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="okBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>