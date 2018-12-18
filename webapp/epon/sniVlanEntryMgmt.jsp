<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
    css css.white.disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var entityId = '<s:property value="entityId"/>';
var sniId = '<s:property value="sniId"/>';
var sniIndex = '<s:property value="sniIndex"/>';
var vlanPVid = '<s:property value="portVlanAttribute.vlanPVid"/>';
var vlanTagPriority = '<s:property value="portVlanAttribute.vlanTagPriority"/>';
var vlanMode = '<s:property value="portVlanAttribute.vlanMode"/>';
var sniName = '<s:property value="sniName"/>';
var portRealIndex = '<s:property value="portRealIndex"/>';
var oltVlanListObject = ${oltVlanListObject};
/* var vlanListJson = null; */
//var sniPvlanPortIndex = sniName.split(" ")[2];
Ext.BLANK_IMAGE_URL = '../images/s.gif';
function doOnload(){
	//$("#sniPvlanPortIndex").val(sniPvlanPortIndex);
	//$("#vlanTagTpidSelect").val(vlanTagTpid);
	
	$("#vlanTagPriority").val(vlanTagPriority);
	$("#vlanPVid").val(vlanPVid);
	$("#sniPvlanPortIndex").val(sniName);
	//$("#vlanMode").val(vlanMode);

	var position = Zeta$('vlanPVid');
	var option = document.createElement('option');
	option.value = "";
	option.text = I18N.COMMON.select + "...."
	try {
		position.add(option, null);
    } catch(ex) {
    	position.add(option);
    }
	/* for(var i = 0; i < oltVlanListObject.length; i++) {
		var option = document.createElement('option');
		option.value = oltVlanListObject[i].vlanIndex;
		option.text = oltVlanListObject[i].vlanIndex;
		if(vlanPVid == oltVlanListObject[i].vlanIndex) {
			option.selected = true;
		}
		try {
			position.add(option, null);
        } catch(ex) {
        	position.add(option);
        }
	} */
}
function checkTpid(){
	var reg1 = /^([0][x][0-9a-f]{4})+$/i;
	var tpid = $("#vlanTagTpidSelect").val();
	if(tpid == "" ||tpid == null){
		return false;
	}else{
		if(reg1.exec(tpid)){
			return true;
		}else{
			return false;
		}
	}
}
function saveClick() {
	 var pvid = $("#vlanPVid").val();
	 var vlanTagPriority = $("#vlanTagPriority").val();
	 var vlanMode = $("#vlanMode").val();
	 /* 
	 if(!checkTpid()){
		 $("#vlanTagTpidSelect").focus();
		 window.parent.showMessageDlg(I18N.COMMON.tip, "@VLAN.tagPidSelect@");
		 return;
	 }
	  */
	 if(!Validator.isInteger(pvid) || pvid > 4094 || pvid < 1){
		 $("#vlanPVid").focus();
	     return;
	 }
	 pvid = parseInt(pvid, 10);
	 
	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.setting );
	 $.ajax({
	        url: 'epon/vlan/updateSniPortVlanAttribute.tv',
	        type: 'POST',
	        data: "entityId=" + entityId + "&sniIndex=" + sniIndex +"&sniId=" + sniId + "&vlanTagPriority=" + vlanTagPriority  +"&vlanPVid=" + pvid + "&vlanMode=3",
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
		        	top.afterSaveOrDelete({
           				title: "@COMMON.tip@",
           				html: '<b class="orangeTxt">@VLAN.mdfingVlanOk@</b>'
           			});
	            	//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfingVlanOk );
	            	window.parent.closeWindow('sniVlanConfig');
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfingVlanEr );
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfingVlanEr );
	    }, cache: false
	    });
}
function cancelClick() {
    window.parent.closeWindow('sniVlanConfig');
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching );
	 $.ajax({
	        url: 'epon/vlan/refreshSniPortVlanAttribute.tv',
	        type: 'POST',
	        data: "sniId=" + sniId + "&sniIndex=" + sniIndex +"&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk );
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError);
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError);
	    }, cache: false
	    });
}

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
<body class=openWinBody onload="doOnload();authLoad();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@EPON.sniVlanCfg@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	

	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" >
		<tr>
			<td class="w200 rightBlueTxt"><label for="ip">@VLAN.portName@:</label></td>
			<td><input type="text" id="sniPvlanPortIndex" class="normalInput w200" disabled /></td>
		</tr>
		<tr>
			<td class="rightBlueTxt"><label for="name">@VLAN.vlanPriority@:</label></td>
			<td><select id="vlanTagPriority" class="w200 normalSel" >
	    		<option value="0">0</option>
	    		<option value="1">1</option>
	   			<option value="2">2</option>
	   			<option value="3">3</option>
	   			<option value="4">4</option>
	   			<option value="5">5</option>
	   			<option value="6">6</option>
	   			<option value="7">7</option>
    		</select>				
			</td>
		</tr>
        <tr class="darkZebraTr">
			<td class="rightBlueTxt">PVID:</td>
			<td> <input id="vlanPVid" class="normalInput w200" tooltip="1-4094"/>
			<%-- <select id="vlanPVid" class="w200 normalSel"></select> --%></td>
		</tr>
		<tr>	
			<td class="rightBlueTxt"><label for="name">@VLAN.vlanMode@:</label></td>
			<td>
			<select id="vlanMode" class="w200 normalSel" disabled>
	    		<option value="0">@VLAN.notCfg@</option>
	    		<option value="1">trunk</option>
	   			<option value="2">access</option>
	   			<option value="3" selected>hybrid</option>
    		</select>
    		</td>
		</tr>
	</table>
	</form>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="fetchData" onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>