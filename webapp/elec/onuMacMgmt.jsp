<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js.tools.ipText
    module epon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = ${entityId};
var onuIndex = ${onuIndex};
var mgmtMode = ${mgmtMode};
var mgmtEnable = ${mgmtEnable};
var mgmtMacList = ${mgmtMacList};
if(mgmtMacList.join("") == 'false'){
	mgmtMacList = new Array();
}
var macData = [];
var macStore;
var macGrid;

Ext.onReady(function(){
	//$("#enabled").attr("checked", mgmtEnable == 1 ? true : false);
	if(mgmtEnable == 1){
	    $("#enabled").click();    
	}
	if(operationDevicePower){
		$("#mode").attr("disabled", mgmtEnable == 1 ? false : true);
	}
	$("#mode").val(mgmtMode);
	loadMacData();
	loadMacGrid();
});
function loadMacData(){
	var tmp = mgmtMacList.slice();
	for(var x=0; x<tmp.length; x++){
		macData[x] = new Array();
		macData[x][0] = tmp[x];
	}
}
function loadMacGrid(){
	macStore = new Ext.data.SimpleStore({
		data : macData,
		fields : ['mac']
	});
	macGrid = new Ext.grid.GridPanel({
	    stripeRows:true,cls:"normalTable edge10",bodyCssClass: 'normalTable',
		renderTo : "macGridDiv",
		viewConfig:{
   			forceFit: true
   		},
		title : initMacInput() + initAddBt(),
		height : 190,
		columns : [{
			header : I18N.ELEC.macAddr,
			dataIndex : "mac",
			width : 135,
			align : "center"
		},{
			header : I18N.COMMON.manu,
			dataIndex : "id",
			width : 170,
			align : "center",
			renderer : function(value, cellmeta, record) {
				if(operationDevicePower){
					return "<img src='/images/delete.gif' onclick='deleteMac()'/>";
				}else{
					return "<img src='/images/deleteDisable.gif'/>";
				}
			}
		}],
		store : macStore,
		listeners : {
			'viewready' : {
				fn : function() {
					initButton("all");
				},
				scope : this
			}
		}
	});
}
function initAddBt(){
	if(operationDevicePower){
		var re = "<button id=addbt class=BUTTON75 onclick='addBtClick()' style='margin-left:15px;'>" + I18N.ELEC.AddMac + "</button>";
	}else{
		var re = "<button id=addbt class=BUTTON75 onclick='addBtClick()' style='margin-left:15px;' disabled>" + I18N.ELEC.AddMac + "</button>";
	}
	return re;
}
function addBtClick(){
	$("#addBt").attr("disabled", true).mouseout();
	var v = changeMacToMaohao($("#macInput").val());
	if($("#enabled").attr("checked") && macIsOk && macData.indexOf(v) == -1){
		macIsOk = false;
		macData.push(v);
		var record = new Ext.data.Record();
		record.data = {mac: v};
		macStore.add(record);
		$("#macInput").val("");
	}
	$("#addBt").attr("disabled", false);
}
function deleteMac(){
	setTimeout(function(){
		if($("#enabled").attr("checked")){
			var sel = macGrid.getSelectionModel().getSelected();
			if(sel){
				macData.splice(macData.indexOf(sel.data.mac), 1);
				macStore.remove(sel);
			}
		}
	}, 100);
}

function saveClick() {
    var stat = $("#enabled").attr("checked") ? 1 : 2;
    var mode = $("#mode").val();
    if(stat == 1){
    	if(mode < 1){
    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.selectOnuAddrMethod );
    		return;
    	}
    	if(!macData.length){
    		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.plsJoinPort );
    		return;
        }
    }
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.saving, 'ext-mb-waiting');
    var par = {entityId: entityId, onuIndex: onuIndex, mgmtEnable: stat, mgmtMode: mode, topOnuMacList: macData.join(":")};
    var url = '/epon/elec/modifyOnuMacMgmt.tv?r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: par,
        success : function(response) {
            if(response.responseText == 'success'){
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfMacMgmtMethod );
            	cancelClick();
            }else{
            	saveFailed(response.responseText);
            }
        },
        failure : function(response) {
        	saveFailed(response.responseText);
        }
    });
}
function saveFailed(re){
	if(re.split(":")[0] == "no response"){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfOnuMacEr1 );
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.mdfOnuMacEr2 );
	}
}
function cancelClick() {
    window.parent.closeWindow('onuMacMgmt');
}
function refreshClick(){
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
	var par = {entityId: entityId, onuIndex: onuIndex};
	var url = '/epon/elec/refreshOnuMacMgmt.tv?r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: par,
        success : function(response) {
            if(response.responseText == 'success'){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
                location.reload();
            }else{
                refreshFailed(response.responseText);
            }
        },
        failure : function(response) {
            refreshFailed(response.responseText);
        }
    });
}
function refreshFailed(re){
	if(re.split(":")[0] == "no response"){
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ELEC.deviceNotConnect );
    }else{
        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
    }
}
var macIsOk = false;
function initMacInput(){
	if(operationDevicePower){
		var re = "<input id=macInput maxlength=17 style='width:140px;text-align:center;margin-left:10px;'" 
			+ " onkeyup='checkedMac(this);macKeyup();' onblur='checkedMac(this)' title='" + I18N.onuAuth.tit.macFormat + "' />";
	}else{
		var re = "<input id=macInput maxlength=17 style='width:140px;text-align:center;margin-left:10px;' disabled/>";
	}
	return re;
}
function macKeyup(){
	if($("#disabled").attr("checked")){
		macIsOk = false;
		$("#macInput").val("");
	}
}
function initOkBt(){
    var re = "<button id=okBt class=BUTTON75 onclick='okBtClick()'></button>";
	return re;
}
function okBtClick(){
	if($("#disabled").attr("checked")){
	    return;
	}
	$("#okBt").attr("disbalde", true).mouseout();
}
function changeMacToMaohao(mac){
    if(mac){
        if(mac.length == 12){
            var newMac = mac.substring(0,2);
            for(var u=1; u<6; u++){
                var tmpMac = mac.substring((2*u),(2*u+2));
                newMac = newMac + ":" + tmpMac;
            }
            mac = newMac;
        }
        if(mac.length == 14){
            mac = mac.substring(0,2)+":"+mac.substring(2,7)+":"+mac.substring(7,12)+":"+mac.substring(12,14);
        }
        var macText = mac.replace(/([/\s-.])/g,":");
        macText = macText.replace(/([a])/g,"A");
        macText = macText.replace(/([b])/g,"B");
        macText = macText.replace(/([c])/g,"C");
        macText = macText.replace(/([d])/g,"D");
        macText = macText.replace(/([e])/g,"E");
        macText = macText.replace(/([f])/g,"F");
        return macText;
    }else{
        return "";
    }
}
function checkedMac(el){
	var pa = $(el);
	var mac = pa.val();
    pa.css("border","1px solid #8bb8f3");
    var reg = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){5})+$/i;
    var reg1 = /^([0-9a-f]{4})(([.][0-9a-f]{4}){2})+$/i;
    var reg2 = /^([0-9a-f]{12})+$/i;
    if(mac == "" || mac == null || mac == undefined){
        return false;
    }
    if(reg.exec(mac) && mac.length == 17){
    	macIsOk = true;
        return true;
    }
    if(reg1.exec(mac) && mac.length == 14){
    	macIsOk = true;
        return true;
    }
    if(reg2.exec(mac) && mac.length == 12){
    	macIsOk = true;
        return true;
    }
    pa.css("border","1px solid #ff0000");
    return false;
}
function enableChanged(){
	var stat = Zeta$("enabled").checked;
	$("#mode").attr("disabled", !stat);
	if(stat){
		$("#macInput").unbind("focus", blurThis);
	}else{
		$("#macInput").bind("focus", blurThis).blur().val("");
	}
}
function blurThis(){
	this.blur();
}
function authLoad(){
	var ids = new Array();
	ids.add("disabled");
	ids.add("enabled");
	//ids.add("refreshBt");
	ids.add("saveBt");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@ELEC.configMac@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="blueTxt" width="100"><input id="disabled" name="rado" type=radio checked onclick='enableChanged()' />&nbsp;@ELEC.disable@</td>
					<td class="blueTxt"><input name="rado" id="enabled" type=radio onclick='enableChanged()' />&nbsp;@ELEC.start@</td>
				</tr>
				<tr>
					<td class="blueTxt">@ELEC.onuMacMgmtMode@:</td>
					<td><select id=mode disabled>
							<option value=0>@COMMON.select@</option>
							<option value=1>@ELEC.filter@</option>
							<option value=2>@ELEC.bind@</option>
					</select></td>
				</tr>
			</table>
		</div>
		<div id=macGridDiv></div>
		
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="saveClick()" icon="miniIcoSaveOK">@COMMON.saveCfg@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>