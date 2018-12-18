<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var entityId = ${entityId};
var onuIndex = ${onuIndex};
var slaValue = ${slaValue};
slaValue = slaValue.join() == "false" ? new Array() : slaValue;
var otherAllDsCir = parseInt(${otherAllDsCir});
var otherAllUsCir = parseInt(${otherAllUsCir});

function refreshClick(){
	url = '/epon/qos/refrshOnuSla.tv?r=' + Math.random();
	params = {
		entityId : entityId,
		onuIndex : onuIndex
	};
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SLA.fetchOnuParamAg , 'ext-mb-waiting')
	Ext.Ajax.request({
		url : url,
		success : function(response) {
			if(response.responseText != 'success'){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.refreshSlaEr )
				return;
			}
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.refreshSlaOk )
			window.location.reload();
		},
		failure : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.refreshSlaEr )
		},
		params : params
	})
}

function saveClick() {
	if(checkedInput(1) && checkedInput(2) && checkedInput(3) && checkedInput(4) && checkedInput(5)){
		url = '/epon/qos/modifyOnuSlaConfig.tv?r=' + Math.random();
		var dscir = $("#dsCir").val();
		var dspir = $("#dsPir").val();
		var usfir = $("#usFir").val();
		var uscir = $("#usCir").val();
		var uspir = $("#usPir").val();
		params = {
			entityId : entityId,
			onuIndex : onuIndex,
			dsCir : dscir,
			dsPir : dspir,
			usFir : usfir,
			usCir : uscir,
			usPir : uspir
		};
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SLA.mdfingOnuSla , 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.mdfOnuSlaEr )
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.mdfOnuSlaOk )
				cancelClick();
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SLA.mdfOnuSlaEr )
			},params : params
		});
	}else{
		$("#saveBt").attr("disabled",true);
		$("#saveBt").mouseout();
	}
}

function cancelClick() {
	window.parent.closeWindow('onuSlaConfig');
}
function updateOnuJson() {
	window.parent.getFrame("entity-" + entityId).updateOnuJson();
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}

/**
 * 3:  UP-FIXED
 * 4:  UP-COMMITED
 */
function keyup(s){
	var dscir = $("#dsCir").val();
	var dspir = $("#dsPir").val();
	var usfir = $("#usFir").val();
	var uscir = $("#usCir").val();
	var uspir = $("#usPir").val();
	$("#saveBt").attr("disabled",true);
	$("#saveBt").mouseout();
	$(".message").attr("color","#333");
	$("#leaveDsCir").attr("color","green");
	$("#leaveUsCir").attr("color","green");
	if(!checkedInput(s)){
		return;
	}
	if(s == 1){
		if(isNaN(dscir) || dscir==""){
			$("#leaveDsCir").html(1000000 - otherAllDsCir);
		}else{
			$("#leaveDsCir").html(1000000 - otherAllDsCir - parseInt(dscir));
		}
	}else if(s == 4){
		if(isNaN(uscir) || uscir==""){
			$("#leaveUsCir").html(1000000 - otherAllUsCir);
		}else{
			$("#leaveUsCir").html(1000000 - otherAllUsCir - parseInt(uscir));
		}
	}
	if(otherAllDsCir+parseInt(dscir) > 1000000){
		$("#message5").attr("color","red");
		$("#leaveDsCir").attr("color","red");
		return;
	}
	if(otherAllUsCir+parseInt(uscir) > 1000000){
		$("#message5").attr("color","red");
		$("#leaveUsCir").attr("color","red");
		return;
	}
	if(dscir!=null && dscir!="" && dspir!=null && dspir!="" && usfir!=null && usfir!="" && uscir!=null && uscir!="" && 
			uspir!=null && uspir!=""){
		if(parseInt(dscir)>parseInt(dspir) || parseInt(uscir)>parseInt(uspir)){
			$("#message2").attr("color","red");
		}else if(parseInt(usfir)>parseInt(uscir)){
			$("#message1").attr("color","red");
		}else if(usfir > 750000){
			$("#message6").attr("color","red");	
		}else if(uscir > 950000){
			$("#message7").attr("color","red");
		}else{
			if((parseInt(dscir)!=parseInt(dspir)) && (parseInt(dspir)-parseInt(dscir))<256){
				$("#message4").attr("color","red");
			}else if((parseInt(usfir)!=parseInt(uscir)) && (parseInt(uscir)-parseInt(usfir))<256){
				$("#message4").attr("color","red");
			}else if((parseInt(uscir)!=parseInt(uspir)) && (parseInt(uspir)-parseInt(uscir))<256){
				$("#message4").attr("color","red");
			}else{
				$("#saveBt").attr("disabled",false);
			}
		}
	}
}
function checkedInput(s){
	var dscir = $("#dsCir").val();
	var dspir = $("#dsPir").val();
	var usfir = $("#usFir").val();
	var uscir = $("#usCir").val();
	var uspir = $("#usPir").val();
	var reg = /^([1-9][0-9]{0,6})+$/ig;
	if(s == 1){
		$("#dsCir").css("border","1px solid #8bb8f3");
		if(isNaN(dscir) || parseInt(dscir)>1000000 || parseInt(dscir)<0 || (!reg.exec(dscir) && dscir!="0")){
			$("#dsCir").css("border","1px solid #FF0000");
			return false;
		}
		if(parseInt(dscir)<256 && parseInt(dscir)!=0){
			$("#dsCir").css("border","1px solid #FF0000");
			$("#message3").attr("color","red");
			return false;
		}
		return true;
	}else if(s == 2){
		$("#dsPir").css("border","1px solid #8bb8f3");
		if(isNaN(dspir) || parseInt(dspir)>1000000 || parseInt(dspir)<0 || (!reg.exec(dspir) && dspir!="0")){
			$("#dsPir").css("border","1px solid #FF0000");
			return false;
		}
		if(parseInt(dspir)<256){
			$("#dsPir").css("border","1px solid #FF0000");
			$("#message3").attr("color","red");
			return false;
		}
		return true;
	}else if(s == 3){
		$("#usFir").css("border","1px solid #8bb8f3");
		if(isNaN(usfir) || parseInt(usfir)>750000 || parseInt(usfir)<0 || (!reg.exec(usfir) && usfir!="0")){
			$("#usFir").css("border","1px solid #FF0000");
			return false;
		}
		if(parseInt(usfir)<21 && parseInt(usfir)!=0){
			$("#usFir").css("border","1px solid #FF0000");
			$("#message6").attr("color","red");
			return false;
		}
		return true;
	}else if(s == 4){
		$("#usCir").css("border","1px solid #8bb8f3");
		if(isNaN(uscir) || parseInt(uscir)>950000 || parseInt(uscir)<0 || (!reg.exec(uscir) && uscir!="0")){
			$("#usCir").css("border","1px solid #FF0000");
			return false;
		}
		if(parseInt(uscir)<256 && parseInt(uscir)!=0){
			$("#usCir").css("border","1px solid #FF0000");
			$("#message3").attr("color","red");
			return false;
		}
		return true;
	}else if(s == 5){
		$("#usPir").css("border","1px solid #8bb8f3");
		if(isNaN(uspir) || parseInt(uspir)>1000000 || parseInt(uspir)<0 || (!reg.exec(uspir) && uspir!="0")){
			$("#usPir").css("border","1px solid #FF0000");
			return false;
		}
		if(parseInt(uspir)<256){
			$("#usPir").css("border","1px solid #FF0000");
			$("#message3").attr("color","red");
			return false;
		}
		return true;
	}
}

Ext.onReady(function(){
	$("#dsCir").val(slaValue[0]);
	$("#dsPir").val(slaValue[1]);
	$("#usFir").val(slaValue[2]);
	$("#usCir").val(slaValue[3]);
	$("#usPir").val(slaValue[4]);
	var temp1 = 1000000 - otherAllDsCir - slaValue[0];
	var temp2 = 1000000 - otherAllUsCir - slaValue[3];
	if(temp1 < 0){
		$("#leaveDsCir").attr("color","red");
	}
	if(temp2 < 0){
		$("#leaveUsCir").attr("color","red");
	}
	$("#leaveDsCir").html(temp1);
	$("#leaveUsCir").html(temp2);
});	//end of Ext.onReady

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.saveBt.setDisabled(true);
	}
	if(!refreshDevicePower){
        R.fetchData.setDisabled(true);
    }
}
</script>
</head>
	<body class=openWinBody onload="authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">ONU SLA</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>


		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr>
					<td class="rightBlueTxt" width="120">@SLA.downBDRate@:</td>
					<td width="230">
						<input id="dsCir" type=text value="0" onkeyup="keyup(1)" class="normalInput"
						maxlength=7 tooltip='@SLA.range1000000D@' /> Kbps
					</td>
					<td class="rightBlueTxt" width="120">@SLA.downPeak@:</td>
					<td><input id="dsPir" type=text value="10000" onkeyup=keyup(2) class="normalInput"
						maxlength=7 tooltip='@SLA.range1000000@' /> Kbps</td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@SLA.upFix@:</td>
					<td><input id="usFir"  type=text value="0" onkeyup=keyup(3)  class="normalInput"
						maxlength=7 tooltip='@SLA.range1000000E@' /> Kbps</td>
					<td class="rightBlueTxt">@SLA.upAssure@:</td>
					<td><input id="usCir" type=text value="0" onkeyup=keyup(4)  class="normalInput"
						maxlength=7 tooltip='@SLA.range1000000D@' /> Kbps</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@SLA.upPeak@:</td>
					<td colspan="3"><input id="usPir" type=text value="10000" onkeyup=keyup(5)  class="normalInput"
						maxlength=7 tooltip='@SLA.range1000000@' /> Kbps</td>
				</tr>
				<tr  class="darkZebraTr">
					<td class="rightBlueTxt" valign="top">
						@SLA.cfgRule@:
					</td>
					<td colspan="3">
						<p><font id=message1 class=message>@SLA.tip1@</font></p>
						<p><font id=message2 class=message>@SLA.tip2@</font></p> 
						<p><font id=message3 class=message>@SLA.tip3@</font></p>
						<p><font id=message6 class=message>@SLA.tip4@</font></p> 
						<p><font id=message4 class=message>@SLA.tip5@</font></p> 
						<p><font id=message5 class=message>@SLA.tip6@</font></p>
						<p><font id=message7 class=message>@SLA.tip8@</font></p>
						<p><font id=message8 class=message>@SLA.tip9@</font></p>
						<p><font id=message9 class=message>@SLA.tip10@</font></p>
						<p class="pT10">
							@SLA.tip7@ @SLA.down@:<font color=green id=leaveDsCir>1000000</font> Kbps
							&nbsp;&nbsp;&nbsp;&nbsp;@SLA.up@:<font color=green
							id=leaveUsCir>1000000</font>Kbps 
						</p>
					</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="fetchData" onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>