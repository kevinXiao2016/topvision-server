<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
var entityId = '<s:property value="portVlanAttribute.entityId"/>';
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var flag = false;
if(entityId == ''||entityId == null){
	flag = true;
	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.dbHasNoInfo)
}
//var vlanPriority = '<s:property value="portVlanAttribute.vlanTagPriority"/>';
//var vlanTpid = '<s:property value="portVlanAttribute.vlanTagTpidString"/>';
var uniId = '<s:property value="portVlanAttribute.portId"/>';
function saveClick()
{
	 var pvid = $("#pvid").val();
	 //var vlanTagPriority = $("#vlanTagPriority").val();
	 var vlanTagTpid = $("#vlanTagTpidSelectText").val();
	 if(!checkPvid()||pvid > 4094 || pvid < 1){
	    Zeta$("pvid").focus();
	    return;
	 }
	 window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.VLAN.setting)
	 $.ajax({
	        url: 'epon/vlan/updatePortVlanAttribute.tv',
	        data: "uniId=" + uniId +"&vlanTagTpid=" + vlanTagTpid +"&vlanPVid=" + pvid +"&entityId="+ entityId +"&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfVlanOk)
	            	cancelClick();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.VLAN.udtError ,'error')
	    }, cache: false
	    });
}
function checkPvid(){
	var reg0 = /^([0-9])+$/;
	var pvid = $("#pvid").val();
	if(pvid == "" || pvid == null){
		return false;
	}else{
		if(reg0.exec(pvid)){
			return true;
		}else{
			return false;
		}
	}
}

function cancelClick(){
	window.top.closeWindow("vlanBaseConfig");
}

function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.loading)
	 $.ajax({
	        url: 'epon/vlan/refreshPortVlanAttribute.tv',
	        type: 'POST',
	        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk)
	            	window.location.reload()
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError, 'error')
	    }, cache: false
	    });
}
function load(){
	if(flag){
		$("#saveBt").attr("disabled", true)
		$("#okBt").attr("disabled", true)
	}
}
function authLoad(){
	if(!operationDevicePower){
	    $("#pvid").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</HEAD>
<body class=openWinBody onload="load();authLoad()">
	<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ONU.vlanBasicCfg@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		
		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt"><label for="ip">@VLAN.portNum@:</label></td>
				<td><input type="text"  class="normalInput" id="portId"
					value="<s:property value="portVlanAttribute.portString"/>" disabled>
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@VLAN.protocolMark@(TPID):</label></td>
				<td>
					<input type="text"  class="normalInput" id="vlanTagTpidSelectText" value="0x8100" disabled>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">PVID:</td>
				<td><input type="text" id="pvid" class="normalInput"
					value="${portVlanAttribute.vlanPVid}" maxlength='4' tooltip="@VLAN.portVlanId@" />
				</td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="saveClick()">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>