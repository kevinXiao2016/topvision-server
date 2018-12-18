<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
var entityId = '${entityId}';
var portIndex = '${portIndex}';
var portId = '${ponId}';
var upLinkRate = '${upLinkRate}';
var downLinkRate = '${downLinkRate}';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var portType = '${portType}';
var upMinRate;
var upMaxRate;
var downMinRate;
var downMaxRate;

function cancelClick() {
    window.parent.closeWindow('ponPortRateLmt');
}

function getDataFromDevice() {
    window.top.showWaitingDlg('@COMMON.wait@', '@VLAN.fetchingAgain@', 'ext-mb-waiting');
    $.ajax({
		url: '/epon/loadPonPortLmt.tv',
		type: 'POST',
        dataType: 'json',
	   	data: {entityId : entityId,ponId : portId},
	    success: function(response) {
	    	 if(response.status == 0){ 
		    	//刷新成功，刷新数据
		    	top.closeWaitingDlg();
		    	top.afterSaveOrDelete({
		            title: '@COMMON.tip@',
		            html: '@VLAN.fetchAgainOk@'
		    	});
		    	$("#upLinkRate").val(response.upLinkRate);
		    	$("#downLinkRate").val(response.downLinkRate);
	    	 }else if(response.status == 1){
	    		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.fetchAgainError@')
	    	} 
	    },
	    error: function(){
	   		window.parent.showMessageDlg('@COMMON.tip@', '@VLAN.fetchAgainError@')
	    }
	});
}

function saveClick(){
	var up = $("#upLinkRate").val();
	var down = $("#downLinkRate").val();
	
	// 输入框范围校验
	if(/^0$|^[1-9]\d+$/.test(up)) {
		up = parseInt(up, 10);
		if(up != 0 && (up < upMinRate || up > upMaxRate)) {
	        Zeta$("upLinkRate").focus();
	        return;
	    }
	} else {
		Zeta$("upLinkRate").focus();
        return;
	}
	
	if(/^0$|^[1-9]\d+$/.test(down)) {
		down = parseInt(down, 10);
		if(down != 0 && (down < downMinRate || down > downMaxRate)) {
	        Zeta$("downLinkRate").focus();
	        return;
	    }
	} else {
		Zeta$("downLinkRate").focus();
        return;
	}
	if(down != 0 && (down < downMinRate || down > downMaxRate)) {
		Zeta$("downLinkRate").focus();
		return;
	}
	
	up = up || upMaxRate;
	down = down || downMaxRate;
	
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving ,'ext-mb-waiting');
	$.ajax({
		url:'/epon/savePonPortLmtCfg.tv',cache:false,
		data:{
			entityId: entityId,
			upLinkRate: up,
			downLinkRate: down,
			portIndex: portIndex,
			ponId: portId
		},success:function(text){
			if(text == 'success'){
				window.top.frames["frameentity-" + entityId].reloadOltJson();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@EPON.setPonPortRateSuccess@</b>'
	   			});
				//window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.setPonPortRateSuccess);
			}else{
				window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.setPonPortRateFail);
			}
       	 	cancelClick();
		},error:function(){
			window.parent.showMessageDlg(I18N.COMMON.message, I18N.EPON.setPonPortRateFail);
       	 	cancelClick();
		}
	})
}

$(document).ready(function(){
	if(portType == 1){
		// pon 
		// up: 5000-1000000
		// down: 5000-1000000
		upMinRate = 5000;
		upMaxRate = 1000000;
		downMinRate = 5000;
		downMaxRate = 1000000;
		
		$("#upLinkRate,#downLinkRate").attr("maxlength",7);
	}else if(portType == 2){
		// 10g pon
		// up: 5000-10000000
        // down: 5000-10000000
		upMinRate = 5000;
        upMaxRate = 10000000;
        downMinRate = 5000;
        downMaxRate = 10000000;
        
		$("#upLinkRate,#downLinkRate").attr("maxlength",8);
	}else if(portType == 3){
		// gpon
		// up: 64-1250000
        // down: 64-2500000
		upMinRate = 64;
        upMaxRate = 1250000;
        downMinRate = 64;
        downMaxRate = 2500000;
        
		$("#upLinkRate,#downLinkRate").attr("maxlength", 7);
	}else{
		// up: 5000-1000000
        // down: 5000-1000000
		upMinRate = 5000;
        upMaxRate = 1000000;
        downMinRate = 5000;
        downMaxRate = 1000000;
        
		$("#upLinkRate,#downLinkRate").attr("maxlength",7);
	}
	
	if(upLinkRate == null || upLinkRate == ''){
		upLinkRate = upMaxRate;
	}
	if(downLinkRate == null || downLinkRate == ''){
		downLinkRate = downMaxRate;
	}
	$("#upLinkRate").val(upLinkRate);
	$("#downLinkRate").val(downLinkRate);
});

function authLoad(){
	if(!operationDevicePower){
	    $("#upLinkRate").attr("disabled",true);
	    $("#downLinkRate").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@EPON.portRateUpAndDownDesc@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w220">@EPON.ponPortUpRate@</td>
						<td><input type="text" id="upLinkRate" width=110 tooltip="String.format('@EPON.ponPortUpRateTip@', upMinRate, upMaxRate)" /> Kbps</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@EPON.ponPortDownRate@</td>
						<td><input type="text" id="downLinkRate" width=110 tooltip="String.format('@EPON.ponPortRateTip@', downMinRate, downMaxRate)" /> Kbps</td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="getDataFromDevice()" icon="miniIcoSaveOK miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@BUTTON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>