<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
var entityId = '${entityId}';
var onuComVlan = '${onuComVlan}'
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
window.onerror = function(msg,url,line){
    return true;
}

Ext.onReady(function(){
	loadVlanList();
});

function loadVlanList(){
	Ext.Ajax.request({
        url: 'epon/elc/loadVlanList.tv',type: 'POST',async: false,dataType:"json",
        params: "entityId=" + entityId,disableCaching :true,
        success: function(response) {
        	var vlanJson = Ext.decode(response.responseText)
            window.combo = new Ext.form.ComboBox({
                //typeAhead: true,
                id: 'vlanCombo',
                disabled:!operationDevicePower,
                triggerAction: 'all',
                lazyRender:true,
                value : onuComVlan == "" ? null : onuComVlan,
                mode: 'local',//forceSelection : true,
                renderTo : "vlanTab",
                forceSelection : true,
                store: new Ext.data.JsonStore({
                    id: 0,
                    fields: [
                       {name: 'vlan'},
                       {name: 'vlanName'/* ,convert : function(v,r){return "VLAN:" + r.vlan;} */}
                    ],
                    data: vlanJson
                }),
                valueField: 'vlan',
                displayField: 'vlanName'
            });
        }, failure: function(vlanJson) {
            window.parent.showMessageDlg(I18N.COMMON.message, I18N.VLAN.vlanLoadError);
    }, cache: false
    });
}

function cancelClick(){
	window.parent.closeWindow('elecOnuComVlan');
}

function saveClick(){
	var onuComVlan = combo.getValue();
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving ,'ext-mb-waiting');
	Ext.Ajax.request({
        url: 'epon/elc/cfgElecComVlan.tv',type: 'POST',async: false,dataType:"json",
        params: {entityId:entityId,onuComVlan:onuComVlan},disableCaching :true,
        success: function(response) {
        	 window.parent.showMessageDlg(I18N.COMMON.message, I18N.ELEC.saveCfgOk);
        	 cancelClick();
        }, failure: function(vlanJson) {
            window.parent.showMessageDlg(I18N.COMMON.message,  I18N.ELEC.saveCfgEr);
    }, cache: false
    });
}

function clearClick(){
	var onuComVlan = 0;
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving ,'ext-mb-waiting');
	Ext.Ajax.request({
        url: 'epon/elc/cfgElecComVlan.tv',type: 'POST',async: false,dataType:"json",
        params: {entityId:entityId,onuComVlan:onuComVlan},disableCaching :true,
        success: function(response) {
        	 window.parent.showMessageDlg(I18N.COMMON.message, I18N.ELEC.saveCfgOk);
        	 cancelClick();
        }, failure: function(vlanJson) {
            window.parent.showMessageDlg(I18N.COMMON.message,  I18N.ELEC.saveCfgEr);
    }, cache: false
    });
}

function fetch(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching ,'ext-mb-waiting');
    Ext.Ajax.request({
        url: 'epon/elc/refreshOnuComVlan.tv',type: 'POST',async: false,dataType:"json",
        params: {entityId:entityId},disableCaching :true,
        success: function(response) {
             window.parent.showMessageDlg(I18N.COMMON.message, I18N.COMMON.fetchOk);
             window.location.reload();
        }, failure: function(vlanJson) {
            window.parent.showMessageDlg(I18N.COMMON.message,  I18N.COMMON.fetchEr);
    }, cache: false
    });
}
function authLoad(){
	if(!operationDevicePower){
	    R.clearBt.setDisabled(true);
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ELEC.comVlan@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt withoutBorderBottom w240">VLAN ID:</td>
						<td class="withoutBorderBottom"><div id="vlanTab"></div></td>
					</tr>
				</table>
			</form>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="fetch()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="clearBt" onClick="clearClick()" icon="miniIcoClose">@VLAN.clearVlanConfig@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>