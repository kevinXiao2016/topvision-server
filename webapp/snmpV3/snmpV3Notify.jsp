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
    import js.tools.ipText static
</Zeta:Loader>
<script type="text/javascript">
Ext.Ajax.timeout = 60000;
var entityId = <s:property value="entityId"/>;
var Target = [];
var TargetParam = [];
var Notify = [];
var NFProfile = [];
var NotifyFilter = [];
var SimpleDefault = {
	targetName : "TA-",
	domain : "1.3.6.1.6.1.1",
	timeout : 1500,
	retryCount : 3,
	tagList : "TG-",
	paramsName : "TP-",
	tarParMPModel : 3,
	profileName : "NF-",//实际表示时使用notify filter,故用NF
	notifyName : "NT-",
	notifyTag : "NTT-",
	type : 1,
	subtree : "1.3",
	mask : "2",
	storType : 3,
	securityModel : 3
}
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var part = ["simple", "target", "tParam", "notify", "nFilter"];
var partCN = [I18N.SNMPv3.n.fastConfig, I18N.SNMPv3.n.targetConfig, I18N.SNMPv3.n.targetParamConfig, 
              I18N.SNMPv3.n.notifyConfig, I18N.SNMPv3.n.notifyFilterConfig];
var partName = [I18N.SNMPv3.n.simpleConfig, "Target", "TargetParams", "Notify", "NotifyFilter"];
var selNum = 0;
var grid = null;
var data = {simpleData: [], targetData: [], tParamData: [], notifyData: [], nFilterData: []}
var store = {simpleStore: null, targetStore: null, tParamStore: null, notifyStore: null, nFilterStore: null}
var fields = {
	simpleFields: ['address', 'port', 'notifyType', 'securityModel', 'securityName', 'securityLevel', 'storType', 'isModifying'],
	targetFields: ['targetName', 'domain', 'address', 'timeout', 'retryCount', 'tagList', 'paramsName', 'storType', 'port', 'isModifying'],
	tParamFields: ['paramsName', 'tarParMPModel', 'securityModel', 'securityName', 'securityLevel','profileName','storType','isModifying'],
	notifyFields: ['notifyName', 'notifyTag', 'notifyType', 'storType', 'isModifying'],
	nFilterFields: ['profileName', 'subtreeMask', 'type', 'storType', 'subtree', 'mask', 'isModifying']
}
var cmHeader = [{}, {}, {}, {}, {}];
var cm = {simpleCm: [],	targetCm: [], tParamCm: [],	notifyCm: [], nFilterCm: []}
var TDomainStr = {"1.3.6.1.6.1.1": "UDP"}//snmpUDPDomain
var levelCNStr = [I18N.SNMPv3.c.plsSelect, I18N.SNMPv3.n.noAuthNoEncrypt, I18N.SNMPv3.n.authNoEncrypt, I18N.SNMPv3.n.authAndEncrypt];
var nFilterTypeStr = [I18N.SNMPv3.c.plsSelect, I18N.SNMPv3.n.include, I18N.SNMPv3.n.exclude];
var notifyTypeStr = [I18N.SNMPv3.c.plsSelect, "Trap", "Inform"];
//SNMP_VERSION_1	0 /* now being tagged as version 1 */
//SNMP_VERSION_2	1 /* SNMPV2 */
//SNMP_VERSION_USEC	2 /* User Security option */
//SNMP_VERSION_3    3 /* SNMPv3 */
//SNMP_VERSION_ALL  4 /* SNMPv1,v2c,v3 */
//SNMP_VERSION_MAX  0x7fffffff /* Upper bound for SNMP version */
var tarParMPModelValue = ["SNMPv1", "SNMPv2", "SNMPv2C", "SNMPv3", "ALL"];
//ETC_SEC_MODEL_ANY 0x00
//ETC_SEC_MODEL_V1  0x01
//ETC_SEC_MODEL_V2  0x02
//ETC_SEC_MODEL_USM 0x03
//ETC_SEC_MODEL_MAX 0x7fffffff
var securityModelValue = ["ANY SEC MODEL", "SEC MODEL V1", "SEC MODEL V2", "SEC MODEL USM"];
var isCouldnotManuGrid = true;
var manuRenderFalg = 0;
var isOperationFlag = false;
var addBtMouseFlag = true;
var modifyData = {};
var partIndexFlag = [[0], [0], [0], [0], [0, 4]];
var DefaultSimpleIndexFlag = [["address"], [SimpleDefault.targetName], [SimpleDefault.paramsName],
                              [SimpleDefault.notifyName], [SimpleDefault.profileName, SimpleDefault.subtree]];

//*****************************************************************************************************************************************
function addOne(s, d){
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SNMPv3.d.creating, partName[s]), 'ext-mb-waiting');
	Ext.Ajax.request({
        url : '/v3/create_' + part[s] + '.tv?entityId=' + entityId + '&r=' + Math.random(),
        disableCachingParam : true,
        success : function(response) {
            if(response.responseText != 'success'){
                if(response.responseText.split(":")[0] == "no response"){
					return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutTip);
                }
                return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.createFailed, partName[s]));
            }
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.createSuc, partName[s]));
            if(s == 0){
            	updateSimpleDataInAll(d);
	        }else{
            	data[part[s] + "Data"].unshift(d);
	        }
            resetGrid();
        },
        failure : function(response) {
	        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.createFailed, partName[s]));
        },
        params : getObjectFromArray(s, d)
    });
}
function modifyOne(s, d){
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SNMPv3.d.modifying, partName[s]), 'ext-mb-waiting');
	var tmpCount = 0;
	var f = fields[part[s] + "Fields"];
	for(var y=0; y<partIndexFlag[s].length; y++){
		var isy = partIndexFlag[s][y];
		tmpCount += (modifyData[f[isy]] == d[isy]) ? 1 : 0;
	}
	var isDelAndAdd = (tmpCount == partIndexFlag[s].length) ? false : true;
	if(isDelAndAdd){
		Ext.Ajax.request({
	        url : '/v3/delete_' + part[s] + '.tv?entityId=' + entityId + '&r=' + Math.random(),
	        success : function(response) {
	            if(response.responseText != 'success'){
	                if(response.responseText.split(":")[0] == "no response"){
						return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutTip);
	                }
	                return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifyFailed, partName[s]));
	            }
	            Ext.Ajax.request({
	    	        url : '/v3/create_' + part[s] + '.tv?entityId=' + entityId + '&r=' + Math.random(),
	    	        success : function(response) {
	    	            if(response.responseText != 'success'){
	    	            	if(s == 0){
	    		            	deleteSimpleDataInAll(d);
	    			        }else{
	    	            		data[part[s] + "Data"].splice(_initF_.index, 1);
	    			        }
	    	            	resetGrid();
	    	                if(response.responseText.split(":")[0] == "no response"){
	    						return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutAndDelSuc);
	    	                }
	    	                return window.parent.showMessageDlg(I18N.COMMON.tip, 
	    	    	                String.format(I18N.SNMPv3.d.modifyFailedAndDelSuc, partName[s]));
	    	            }
	    	            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifySuc, partName[s]));
	    	            if(s == 0){
	    	            	updateSimpleDataInAll(d);
	    		        }else{
	    	            	data[part[s] + "Data"][_initF_.index] = d;
	    		        }
	    	            resetGrid();
	    	        },
	    	        failure : function(response) {
	    	        	if(s == 0){
	    	            	deleteSimpleDataInAll(d);
	    		        }else{
	    	        		data[part[s] + "Data"].splice(_initF_.index, 1);
	    		        }
	    	        	resetGrid();
	    		        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifyFailedAndDelSuc, partName[s]));
	    	        },
	    	        params : getObjectFromArray(s, d)
	    	    });
	        },
	        failure : function(response) {
		        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifyFailed, partName[s]));
	        },
	        params : modifyData
	    });
	}else{
		Ext.Ajax.request({
	        url : '/v3/modify_' + part[s] + '.tv?entityId=' + entityId + '&r=' + Math.random(),
	        success : function(response) {
	            if(response.responseText != 'success'){
	                if(response.responseText.split(":")[0] == "no response"){
						return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutTip);
	                }
	                return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifyFailed, partName[s]));
	            }
	            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifySuc, partName[s]));
	            if(s == 0){
	            	updateSimpleDataInAll(d);
		        }else{
		            data[part[s] + "Data"][_initF_.index] = d;
			    }
	            resetGrid();
	        },
	        failure : function(response) {
		        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.modifyFailed, partName[s]));
	        },
	        params : getObjectFromArray(s, d)
	    });
	}
}
function deleteOne(s, d){
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.SNMPv3.d.deleting, partName[s]), 'ext-mb-waiting');
	Ext.Ajax.request({
        url : '/v3/delete_' + part[s] + '.tv?entityId=' + entityId + '&r=' + Math.random(),
        success : function(response) {
            if(response.responseText != 'success'){
                if(response.responseText.split(":")[0] == "no response"){
					return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutTip);
                }
                return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.deleteFailed, partName[s]));
            }
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.deleteSuc, partName[s]));
            if(s == 0){
                deleteSimpleDataInAll(d);
           	}else{
	            data[part[s] + "Data"].splice(_initF_.index, 1);
            }
            resetGrid();
        },
        failure : function(response) {
	        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.d.deleteFailed, partName[s]));
        },
        params : getObjectFromArray(s, d)
    });
}
function refreshBtClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
	var pa = Ext.getCmp("refreshImg");
	pa.setIcon("/images/refreshing2.gif");
	Ext.Ajax.request({
        url : '/v3/refreshSnmpV3Notify.tv?entityId=' + entityId,
        success : function(response) {
            if(response.responseText != 'success'){
            	pa.setIcon("/images/refresh.gif");
                if(response.responseText.split(":")[0] == "no response"){
					return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SNMPv3.t.timeoutTip);
                }
                return window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
            }
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
            loadAllSnmpNotifyData();
        },
        failure : function(response) {
	        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchEr);
	        pa.setIcon("/images/refresh.gif");
        }
    });
}
function loadAllSnmpNotifyData(){
	var pa = Ext.getCmp("refreshImg");
	pa.setIcon("/images/refreshing2.gif");
	Ext.Ajax.request({
        url : '/v3/loadAllSnmpNotifyData.tv?entityId=' + entityId + '&r=' + Math.random(),
        success : function(response) {
            var obj = Ext.util.JSON.decode(response.responseText);
           	initData(obj);
           	store[part[selNum] + "Store"].loadData(data[part[selNum] + "Data"]);
           	pa.setIcon("/images/refresh.gif");
        },
        failure : function(response) {
        	pa.setIcon("/images/refresh.gif");
        }
    });
	modifyData = {};
	_initF_.ipIsInput = false;
	isOperationFlag = false;
}
var debugMessageFlag = false;
window.onerror = function(){
    if(debugMessageFlag){
		arglen = arguments.length;
	    var errorMsg = "arguments.length:" + arglen;
	    for(var i=0; i<arglen; i++){
	    	errorMsg += "\narguments[" + i + "]: " + arguments[i];
    	}
		alert(errorMsg);
    }
	loadAllSnmpNotifyData();
	return true;
}

//*****************************************************************************************************************************************
Ext.onReady(function(){
	setTimeout(function(){
		initPartDiv();
	}, 10);
	initCm();
	loadGrid();
	loadAllSnmpNotifyData();
});
function initPartDiv(){
	var pa = $("#partDiv");
	var style = "height:28px;background-color:#efffff;border:0px;margin-right:5px;cursor:hand;";
	var handler = "onmouseover='selBtOver(this)' onmouseout='selBtOut(this)' onclick='selBtClick(this)'";
	for(var a=0; part[a]; a++){
		pa.append(String.format("<button id='{1}' class='selBtClass' style='{2}' {3}>{0}</button>", 
				partCN[a], part[a], style, handler));
		$("#" + part[a]).mouseout();
	}
	pa.append("<br>");
	for(a=0; part[a]; a++){
		style = "width:" + $("#" + part[a]).width() + ";height:2px;background:transparent;border:0px;margin-right:5px;";
		pa.append(String.format("<button class=selSpanClass id='{0}_span' style='{1}'></button>", part[a], style));
	}
	$("#" + part[selNum] + "_span").css("background-color", "#00ff40");
}
function selBtOver(el){
	if(el.id != part[selNum]){
		$(el).css("background-color", "#ccffff");
	}
}
function selBtOut(el){
	if(el.id == part[selNum]){
		$(el).css({"background-color": "#eeeeee", "color": "red"});
	}else{
		$(el).css("background-color", "#efffff");
	}
}
function selBtClick(el){
	var tmpNum = part.indexOf(el.id);
	if(tmpNum > -1 && selNum != tmpNum){
		if(!isCouldnotManuGrid){
			isCouldnotManuGrid = true;
			isOperationFlag = false;
			_initF_.ipIsInput = false;
			selNum = tmpNum;
			loadGrid();
			setTimeout(function(){
				$(".selBtClass").css({"background-color": "#efffff", "color": ""});
				$(el).css({"background-color": "#cccccc", "color": "red"}).mouseout();
				$(".selSpanClass").css("background", "transparent");
				$("#" + el.id + "_span").css("background-color", "#00ff40");
			}, 10);
		}
	}
}
function initData(obj){
	Target = obj.targetJson;
   	Target = Target.join("") == "false" ? [] : Target;
   	TargetParam = obj.tarParamJson;
   	TargetParam = TargetParam.join("") == 'false' ? [] : TargetParam;
   	Notify = obj.notifyJson;
   	Notify = Notify.join("") == "false" ? [] : Notify;
   	NFProfile = obj.nfProfileJson;
   	NFProfile = NFProfile.join("") == 'false' ? [] : NFProfile;
   	NotifyFilter = obj.notifyFilterJson;
   	NotifyFilter = NotifyFilter.join("") == "false" ? [] : NotifyFilter;
	data = {simpleData: [], targetData: [], tParamData: [], notifyData: [], nFilterData: []};
	var len = 0;
	//data.simpleData ['address', 'port', 'notifyType', 'securityModel', 'securityName', 'securityLevel', 'storType']
	var defaultData = {targetData: [], tParamData: [], notifyData: [], nFilterData: []}
	//data.targetData ['targetName', 'domain', 'address', 'timeout', 'retryCount', 'tagList', 'paramsName', 'storType', 'port']
	len = Target.length;
	if(len > 0){
		for(var a=0; a<len; a++){
			var tmp = Target[a];
			data.targetData[a] = new Array();
			data.targetData[a][0] = tmp.targetName;
			data.targetData[a][1] = tmp.targetDomain;
			var ips = tmp.targetAddress.split(":");
			if(ips.length == 6){
				var qq = [];
				for(var q=0; q<4; q++){
					qq.push(parseInt(ips[q], 16));
				}
				if(checkedIpValue(qq.join("."))){
					data.targetData[a][2] = qq.join(".");
				}else{
					data.targetData[a][2] = "";
				}
				data.targetData[a][8] = parseInt(ips[4] + ips[5], 16);
			}else{
				data.targetData[a][2] = "";
				data.targetData[a][8] = "";
			}
			data.targetData[a][3] = tmp.targetTimeout;
			data.targetData[a][4] = tmp.targetRetryCount;
			data.targetData[a][5] = tmp.targetTagList;
			data.targetData[a][6] = tmp.targetParams;
			data.targetData[a][7] = tmp.targetStorageType;
			if(tmp.targetName.indexOf(SimpleDefault.targetName) == 0){
				defaultData.targetData.push(data.targetData[a]);
			}
		}
	}
	//relation of targetParam & notifyFilter
	len = NFProfile.length;
	var relationUseInTP = {};
	var relationUseInNF = {};
	var relationStorInNF = {};
	if(len > 0){
		for(a=0; a<len; a++){
			tmp = NFProfile[a];
			relationUseInTP[tmp.targetParamsName] = tmp.notifyFilterProfileName;
			relationUseInNF[tmp.notifyFilterProfileName] = tmp.targetParamsName;
			relationStorInNF[tmp.targetParamsName] = tmp.notifyFilterProfileStorType;
		}
	}
	//data.tParamData ['paramsName', 'tarParMPModel', 'securityModel', 'securityName', 'securityLevel', 'profileName', 'storType']
	len = TargetParam.length;
	if(len > 0){
		for(a=0; a<len; a++){
			tmp = TargetParam[a];
			data.tParamData[a] = new Array();
			data.tParamData[a][0] = tmp.targetParamsName;
			data.tParamData[a][1] = tmp.targetParamsMPModel;
			data.tParamData[a][2] = tmp.targetParamsSecurityModel;
			data.tParamData[a][3] = tmp.targetParamsSecurityName;
			data.tParamData[a][4] = tmp.targetParamsSecurityLevel;
			if(relationUseInTP[tmp.targetParamsName]){
				data.tParamData[a][5] = relationUseInTP[tmp.targetParamsName];
			}else{
				data.tParamData[a][5] = "";
			}
			if(tmp.targetParamsStorageType){
				data.tParamData[a][6] = tmp.targetParamsStorageType;
			}else if(relationStorInNF[tmp.targetParamsName]){
				data.tParamData[a][6] = relationStorInNF[tmp.targetParamsName];
			}else{
				data.tParamData[a][6] = 3;
			}
			if(tmp.targetParamsName.indexOf(SimpleDefault.paramsName) == 0 && 
					data.tParamData[a][5].indexOf(SimpleDefault.profileName) == 0){
				defaultData.tParamData.push(data.tParamData[a]);
			}
		}
	}
	//data.notifyData ['notifyName', 'notifyTag', 'notifyType', 'storType']
	len = Notify.length;
	if(len > 0){
		for(a=0; a<len; a++){
			tmp = Notify[a];
			data.notifyData[a] = new Array();
			data.notifyData[a][0] = tmp.notifyName;
			data.notifyData[a][1] = tmp.notifyTag;
			data.notifyData[a][2] = tmp.notifyType;
			data.notifyData[a][3] = tmp.notifyStorageType;
			if(tmp.notifyName.indexOf(SimpleDefault.notifyName) == 0){
				defaultData.notifyData.push(data.notifyData[a]);
			}
		}
	}
	//data.nFilterData ['profileName', 'subtreeMask', 'type', 'storType', 'subtree', 'mask']
	len = NotifyFilter.length;
	if(len > 0){
		for(a=0; a<len; a++){
			tmp = NotifyFilter[a];
			data.nFilterData[a] = new Array();
			data.nFilterData[a][0] = tmp.notifyFilterProfileName;
			data.nFilterData[a][1] = subtreeMerge(tmp.notifyFilterSubtree, tmp.notifyFilterMask);
			data.nFilterData[a][2] = tmp.notifyFilterType;
			if(relationStorInNF[tmp.notifyFilterProfileName]){
				data.nFilterData[a][3] = relationStorInNF[tmp.notifyFilterProfileName];
			}else{
				data.nFilterData[a][3] = 3;
			}
			data.nFilterData[a][4] = tmp.notifyFilterSubtree;
			data.nFilterData[a][5] = tmp.notifyFilterMask;
			if(tmp.notifyFilterProfileName.indexOf(SimpleDefault.profileName) == 0 && 
					tmp.notifyFilterSubtree == SimpleDefault.subtree){
				defaultData.nFilterData.push(data.nFilterData[a]);
			}
		}
	}
	//data.simpleData ['address', 'port', 'notifyType', 'securityModel', 'securityName', 'securityLevel', 'storType']
	len = defaultData.targetData.length;
	for(a=0; a<len; a++){
		tmp = defaultData.targetData[a];
		var ipLong = getIpStringToLong(defaultData.targetData[a][2]).toString();
		var flag = false;
		var simpleList = [null, null, null, null, null, null, null];
		if(defaultData.targetData[a][0] == (SimpleDefault.targetName + "" + ipLong)){
			simpleList[0] = defaultData.targetData[a][2];
			simpleList[1] = defaultData.targetData[a][8];
			simpleList[6] = defaultData.targetData[a][7];
			flag = !flag;
		}
		if(flag){
			flag = !flag;
			for(var b=0; b<defaultData.tParamData.length; b++){
				if(defaultData.tParamData[b][0] == (SimpleDefault.paramsName + "" + ipLong) &&
						defaultData.tParamData[b][5] == (SimpleDefault.profileName + "" + ipLong)){
					simpleList[3] = defaultData.tParamData[b][2];
					simpleList[4] = defaultData.tParamData[b][3];
					simpleList[5] = defaultData.tParamData[b][4];
					flag = !flag;
					break;
				}
			}
		}else{
			continue;
		}
		if(flag){
			flag = !flag;
			for(var c=0; c<defaultData.notifyData.length; c++){
				if(defaultData.notifyData[c][0] == (SimpleDefault.notifyName + "" + ipLong)){
					simpleList[2] = defaultData.notifyData[c][2];
					flag = !flag;
					break;
				}
			}
		}else{
			continue;
		}
		if(flag){
			flag = !flag;
			for(var d=0; d<defaultData.nFilterData.length; d++){
				if(defaultData.nFilterData[d][0] == (SimpleDefault.profileName + "" + ipLong) &&
						defaultData.nFilterData[d][4] == SimpleDefault.subtree){
					flag = !flag;
					break;
				}
			}
		}else{
			continue;
		}
		if(flag){
			data.simpleData.push(simpleList);
		}
	}
}
function getObjectFromArray(s, a){
	var re = {};
	var f = fields[part[s] + "Fields"];
	for(var i=0; i<f.length; i++){
		re[f[i]] = a[i];
	}
	return re;
}
function getIpStringToLong(ip){
	if(checkedIpValue(ip)){
		var ips = ip.split(".");
		var tmp = "";
		for(var a=0; a<4; a++){
			var tt = parseInt(ips[a]).toString(16).toUpperCase();
			tmp += tt;
		}
		return tmp;
		return parseInt(tmp, 16);
	}else{
		return 0;
	}
}
function getDefaultInOtherIndex(ip){
	var ipLong = getIpStringToLong(ip);
	var tmpI = [-1, -1, -1, -1, -1];
	for(var a=0; a<part.length; a++){
		var tmpD = data[part[a] + "Data"];
		var len = tmpD.length;
		var tmp = partIndexFlag[a];
		for(var b=0; b<len; b++){
			var flag = tmp.length;
			for(var c=0; c<tmp.length; c++){
				if((tmpD[b][tmp[c]] == (DefaultSimpleIndexFlag[a][c] + "" + ipLong)) 
						|| (a == 4 && c == 1 && tmpD[b][tmp[c]] == DefaultSimpleIndexFlag[a][c])
						|| (a == 0 && c == 0 && tmpD[b][tmp[c]] == ip)){
					flag--;
				}
			}
			if(!flag){
				tmpI[a] = b;
			}
		}
	}
	return tmpI;
}
function deleteSimpleDataInAll(d){
	var tmpI = getDefaultInOtherIndex(d[0]);
    for(var a=0; a<tmpI.length; a++){
		if(tmpI[a] > -1){
			data[part[a] + "Data"].splice(tmpI[a], 1);
		}
   	}
}
function updateSimpleDataInAll(d){
	var ipLong = getIpStringToLong(d[0]);
	var tmpDD = {
		simple : d,
		target : [SimpleDefault.targetName + "" + ipLong, SimpleDefault.domain, d[0], SimpleDefault.timeout, 
               	SimpleDefault.retryCount, SimpleDefault.tagList + "" + ipLong, SimpleDefault.paramsName + "" + ipLong,
             	SimpleDefault.storType, d[1], false],
		tParam : [SimpleDefault.paramsName + "" + ipLong, SimpleDefault.tarParMPModel, d[3], d[4], d[5], 
               	SimpleDefault.profileName + "" + ipLong, SimpleDefault.storType, false],
		notify : [SimpleDefault.notifyName + "" + ipLong, SimpleDefault.notifyTag + ipLong, d[2], SimpleDefault.storType, false],
		nFilter : [SimpleDefault.profileName + "" + ipLong, subtreeMerge(SimpleDefault.subtree, SimpleDefault.mask), 
                 	SimpleDefault.type, SimpleDefault.storType, SimpleDefault.subtree, SimpleDefault.mask, false]
	}
	var tmpI = getDefaultInOtherIndex(modifyData.address);
    for(var a=0; a<tmpI.length; a++){
		if(tmpI[a] > -1){
			data[part[a] + "Data"].splice(tmpI[a], 1, tmpDD[part[a]]);
		}else{
			data[part[a] + "Data"].unshift(tmpDD[part[a]]);
		}
   	}
}
function initCm(){
	var f1 = fields.simpleFields;
	var f2 = fields.targetFields;
	var f3 = fields.tParamFields;
	var f4 = fields.notifyFields;
	var f5 = fields.nFilterFields;
	cmHeader = [{address: I18N.SNMPv3.n.ipAddress, port: I18N.SNMPv3.n.portNum, notifyType: I18N.SNMPv3.n.notifyType, 
					securityName: "securityName", securityLevel: "securityLevel"}
				,{targetName: I18N.SNMPv3.n.targetName, address: I18N.SNMPv3.n.ipAddress, timeout: I18N.SNMPv3.n.timeout, retryCount: 
					I18N.SNMPv3.n.retryCount, tagList: I18N.SNMPv3.n.tagList, paramsName: "TargetParam", port: I18N.SNMPv3.n.portNum}
				,{paramsName: I18N.SNMPv3.n.targetParamName, securityName: "securityName", 
					securityLevel: "securityLevel", profileName: "NotifyFilter"}
				,{notifyName: I18N.SNMPv3.n.notifyName, notifyTag: "Tag", notifyType: I18N.SNMPv3.n.notifyType}
				,{profileName: I18N.SNMPv3.n.notifyFilterName, subtreeMask: I18N.SNMPv3.n.subtreeMask, type: I18N.SNMPv3.n.mode}];
	var h0 = cmHeader[0];
	var h1 = cmHeader[1];
	var h2 = cmHeader[2];
	var h3 = cmHeader[3];
	var h4 = cmHeader[4];
	cm.simpleCm = [{header: h0[f1[0]], id:'id0', dataIndex: f1[0], width:140}
				,{header: h0[f1[1]], id:'id1', dataIndex: f1[1], width:70}
				,{header: h0[f1[2]], id:'id2', dataIndex: f1[2], width:120, renderer: notifyTypeRender}
				,{header: I18N.SNMPv3.n.usmMode, id:'id3', dataIndex: f1[3], width:80, hidden: true, renderer: secModelRender}
				,{header: h0[f1[4]], id:'id4', dataIndex: f1[4], width:160}
				,{header: h0[f1[5]], id:'id5', dataIndex: f1[5], width:130, renderer: secuLevRender}
				,{header: I18N.SNMPv3.n.storeType, id:'id6', dataIndex: f1[6], width:90, hidden: true, renderer: storTypeRender}
				,{header: I18N.COMMON.manu, id:'id9', dataIndex: f1[7], width:60, renderer: manuRender}];
	cm.targetCm = [{header: h1[f2[0]], id:'id0', dataIndex: f2[0], width:100}
				,{header: h1[f2[2]], id:'id2', dataIndex: f2[2], width:120}
				,{header: h1[f2[8]], id:'id8', dataIndex: f2[8], width:50}
				,{header: I18N.SNMPv3.n.connectType, id:'id1', dataIndex: f2[1], width:70, hidden: true, renderer: connectRender}
				,{header: h1[f2[3]], id:'id3', dataIndex: f2[3], width:70}
				,{header: h1[f2[4]], id:'id4', dataIndex: f2[4], width:60}
				,{header: h1[f2[5]], id:'id5', dataIndex: f2[5], width:100}
				,{header: h1[f2[6]], id:'id6', dataIndex: f2[6], width:120}
				,{header: I18N.SNMPv3.n.storeType, id:'id7', dataIndex: f2[7], width:90, hidden: true, renderer: storTypeRender}
				,{header: I18N.COMMON.manu, id:'id9', dataIndex: f2[9], width:60, renderer: manuRender}];
	cm.tParamCm = [{header: h2[f3[0]], id:'id0', dataIndex: f3[0], width:140}
				,{header: I18N.SNMPv3.n.versionMode, id:'id1', dataIndex: f3[1], width:80, hidden: true, renderer: mpModelRender}
				,{header: I18N.SNMPv3.n.usmMode, id:'id2', dataIndex: f3[2], width:80, hidden: true, renderer: secModelRender}
				,{header: h2[f3[3]], id:'id3', dataIndex: f3[3], width:170}
				,{header: h2[f3[4]], id:'id4', dataIndex: f3[4], width:130, renderer: secuLevRender}
				,{header: h2[f3[5]], id:'id5', dataIndex: f3[5], width:150}
				,{header: I18N.SNMPv3.n.storeType, id:'id6', dataIndex: f3[6], width:90, hidden: true, renderer: storTypeRender}
				,{header: I18N.COMMON.manu, id:'id9', dataIndex: f3[7], width:80, renderer: manuRender}];
	cm.notifyCm = [{header: h3[f4[0]], id:'id0', dataIndex: f4[0], width:230}
				,{header: h3[f4[1]], id:'id1', dataIndex: f4[1], width:180}
				,{header: h3[f4[2]], id:'id2', dataIndex: f4[2], width:170, renderer: notifyTypeRender}
				,{header: I18N.SNMPv3.n.storeType, id:'id3', dataIndex: f4[3], width:90, hidden: true, renderer: storTypeRender}
				,{header: I18N.COMMON.manu, id:'id9', dataIndex: f4[6], width:100, renderer: manuRender}];
	cm.nFilterCm = [{header: h4[f5[0]], id:'id0', dataIndex: f5[0], width:200}
				,{header: h4[f5[2]], id:'id2', dataIndex: f5[2], width:80, renderer: nFilterTypeRender}
				,{header: h4[f5[1]], id:'id1', dataIndex: f5[1], width:300, align: 'left'}
				,{header: I18N.SNMPv3.n.storeType, id:'id3', dataIndex: f5[3], width:90, hidden: true, renderer: storTypeRender}
				,{header: I18N.COMMON.manu, id:'id9', dataIndex: f5[4], width:100, renderer: manuRender}];
}
function notifyTypeRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return notifyTypeStr[parseInt(v)] ? notifyTypeStr[parseInt(v)] : I18N.SNMPv3.c.unknownType;
	}
}
function nFilterTypeRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return nFilterTypeStr[parseInt(v)] ? nFilterTypeStr[parseInt(v)] : I18N.SNMPv3.c.unknownType;
	}
}
function secuLevRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return levelCNStr[parseInt(v)] ? levelCNStr[parseInt(v)] : I18N.SNMPv3.c.unknownType;
	}
}
function manuRender(v, c, record){
	manuRenderFalg++;
	var tmpFlag = manuRenderFalg;
	setTimeout(function(){
		if(manuRenderFalg == tmpFlag){
			if(isOperationFlag){
				setTimeout(function(){
					if(_initF_.ipIsInput && manuRenderFalg == tmpFlag){
						initTheIpInput();
					}
				}, 100);
			}
		}
	}, 100);
	var hand = "cursor:hand;";
	if(isOperationFlag && record.data.storType == _initF_.storType){
		var imgStr1 = String.format("onclick='okBtClick()' title='{0}' style='{1}'", I18N.SNMPv3.c.ok, hand);
		var imgStr2 = String.format("onclick='cancelBtClick()' title='{0}' style='{1}'", I18N.SNMPv3.c.cancel, hand);
		return String.format("<img src='/images/save.gif' {0}><img style='margin-left:5px;' src='/images/minus.png' {1}>", 
				imgStr1, imgStr2);
	}else{
		if(operationDevicePower){
			var imgStr1 = String.format("onclick='modifyBtClick()' title='{0}' style='{1}'", I18N.SNMPv3.c.modify, hand);
			var imgStr2 = String.format("onclick='deleteBtClick()' title='{0}' style='{1}'", I18N.SNMPv3.c.del, hand);
			return String.format("<img src='/images/edit.gif' {0}><img style='margin-left:5px;' src='/images/delete.gif' {1}>", 
					imgStr1, imgStr2);
		}else{
			var imgStr1 = String.format("title='{0}' style='{1}'", I18N.SNMPv3.c.modify, hand);
			var imgStr2 = String.format("title='{0}' style='{1}'", I18N.SNMPv3.c.del, hand);
			return String.format("<img src='/images/editDisable.gif' {0}><img style='margin-left:5px;' src='/images/deleteDisable.gif' {1}>", 
					imgStr1, imgStr2);
		}
	}
}
function storTypeRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return v == 3 ? I18N.SNMPv3.n.nonVolatile : I18N.SNMPv3.c.unknownType;
	}
}
function secModelRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return securityModelValue[v] ? securityModelValue[v] : I18N.SNMPv3.c.unknownType;
	}
}
function mpModelRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return tarParMPModelValue[v] ? tarParMPModelValue[v] : I18N.SNMPv3.c.unknownType;
	}
}
function connectRender(v, c, record){
	if(isOperationFlag){
		return v;
	}else{
		return TDomainStr[v] ? TDomainStr[v] : I18N.SNMPv3.c.unknownType;
	}
}
function cancelBtClick(){
	resetGrid();
}
function resetGrid(){
	modifyData = {};
	_initF_.ipIsInput = false;
	isOperationFlag = false;
	store[part[selNum] + "Store"].loadData(data[part[selNum] + "Data"]);
	setTimeout(function(){
		if(store[part[selNum] + "Store"].getCount() > _initF_.index){
			grid.getView().focusRow(grid.getStore().getCount() - 7 > _initF_.index ? _initF_.index + 6 : grid.getStore().getCount() - 1);
		}
	}, 100);
}
function initStore(){
	store[part[selNum] + "Store"] = new Ext.data.SimpleStore({
        data : data[part[selNum] + "Data"],
        fields : fields[part[selNum] + "Fields"]
    });
	return store[part[selNum] + "Store"];
}
function loadGrid(){
	if(grid){
		grid.getColumnModel().destroy();
		grid.destroy();
		$("#gridDiv").empty();
	}
	var tmpData = data[part[selNum] + "Data"];
	var tmpCm = cm[part[selNum] + "Cm"];
	var toolBar = new Ext.Toolbar({
		items: [{
			text : I18N.SNMPv3.c.create,
			iconCls: 'bmenu_new',
			disabled: !operationDevicePower,
			handler: addBtClick
		},'-',{
			text : I18N.SNMPv3.c.modify,
			iconCls: 'bmenu_edit',
			disabled: !operationDevicePower,
			handler: modifyBtClick
		},'-',{
			text : I18N.SNMPv3.c.del,
			iconCls: 'bmenu_delete',
			disabled: !operationDevicePower,
			handler: deleteBtClick
		},'-',{
			id : "refreshImg",
			text: I18N.SNMPv3.c.refresh,
			icon: '/images/refresh.gif',
			handler: loadAllSnmpNotifyData
		}]
	});
	grid = new Ext.grid.GridPanel({
	    stripeRows:true,region: "center",bodyCssClass: 'normalTable',
        id : 'snmpNotifyGrid',
        renderTo : 'gridDiv',
        store : initStore(),
        height : 330,
        frame : false,
        autoScroll : true,
        border : true,
        viewConfig: {forceFit: false},
        selModel : new Ext.grid.RowSelectionModel({
        	singleSelect : true,
        	listeners : {
				'selectionchange' : function(){
					
				}
			}
        }),
        listeners : {
            'viewready' : function (){
                setTimeout(function(){
	            	isCouldnotManuGrid = false;
                }, 150);
            },
            'rowcontextmenu': function(grid,rowIndex,e){
                
            }
        },
        columns: tmpCm,
        tbar : toolBar
    });
    grid.getColumnModel().addListener({
        columnmoved : function(thisCm, oldIndex, newIndex){
	        if(isOperationFlag){
				if(selNum == 0 || selNum == 1){
					setTimeout(function(){
						initTheIpInput();
					}, 100);
				}
	        }
    	}
    });
    grid.getView().refresh();
}
function closeDetailDlg(){
	if(detailDlg != null){
		detailDlg.hide();
	}
}
function initTheIpInput(){
	try{
		var in_Ip = new ipV4Input("ip_address", "in_address", ipChanged);
		setIpBgColor("ip_address", "white");
		if(_initF_.ipValue && checkedIpValue(_initF_.ipValue)){
			setIpValue("ip_address", _initF_.ipValue);
		}
		_initF_.ipIsInput = false;
	}catch(e){
		_initF_.ipIsInput = false;
	}
}

//****************************************************************************************************************************************
function addBtClick(){
	if(!isCouldnotManuGrid){
		isCouldnotManuGrid = true;
		modifyData = {};
		if(isOperationFlag){
			isOperationFlag = false;
			store[part[selNum] + "Store"].loadData(data[part[selNum] + "Data"]);
			setTimeout(function(){
				isCouldnotManuGrid = false;
				addBtClick();
			}, 300);
			return;
		}
		isOperationFlag = true;
		_initF_.index = 0;
		var record = new Ext.data.Record();
		var ff = fields[part[selNum] + "Fields"];
		for(var a=0; ff[a]; a++){
			record.data[ff[a]] = _init_(ff[a]);
		}
		addRecordToGrid(record);
		isCouldnotManuGrid = false;
	}
}
function modifyBtClick(){
	if(!isCouldnotManuGrid){
		isCouldnotManuGrid = true;
		setTimeout(function(){
			var sel = grid.getSelectionModel().getSelected();
			if(sel){
				if(isOperationFlag){
					var tmpI = store[part[selNum] + "Store"].indexOf(sel);
					if(!modifyData.storType){
						tmpI--;
					}
					isOperationFlag = false;
					store[part[selNum] + "Store"].loadData(data[part[selNum] + "Data"]);
					setTimeout(function(){
						grid.getSelectionModel().selectRow(tmpI, true);
						sel = grid.getSelectionModel().getSelected();
						modifyData = {};
						isCouldnotManuGrid = false;
						modifyBtClick();
					}, 300);
					return;
				}
				isOperationFlag = true;
				_initF_.index = store[part[selNum] + "Store"].indexOf(sel);
				store[part[selNum] + "Store"].remove(sel);
				var record = new Ext.data.Record();
				var ff = fields[part[selNum] + "Fields"];
				for(var a=0; ff[a]; a++){
					record.data[ff[a]] = _init_(ff[a], sel.data[ff[a]]);
				}
				modifyData = sel.data;
				addRecordToGrid(record);
			}
			isCouldnotManuGrid = false;
		}, 100);
	}
}
function addRecordToGrid(record){
	store[part[selNum] + "Store"].insert(_initF_.index, record);
	setTimeout(function(){
		grid.getSelectionModel().selectRow(_initF_.index, true);
		grid.getView().focusRow(grid.getStore().getCount() - 7 > _initF_.index ? _initF_.index + 6 : grid.getStore().getCount() - 1);
	}, 100);
}
function deleteBtClick(){
	if(!isCouldnotManuGrid){
		isCouldnotManuGrid = true;
		setTimeout(function(){
			var sel = grid.getSelectionModel().getSelected();
			if(sel){
				if(isOperationFlag){
					var tmpI = store[part[selNum] + "Store"].indexOf(sel);
					if(!modifyData.storType){
						tmpI--;
					}
					isOperationFlag = false;
					store[part[selNum] + "Store"].loadData(data[part[selNum] + "Data"]);
					isCouldnotManuGrid = true;
					setTimeout(function(){
						isCouldnotManuGrid = false;
						grid.getSelectionModel().selectRow(tmpI, true);
						deleteBtClick();
					}, 300);
					return;
				}
				var d = [];
				var f = fields[part[selNum] + "Fields"];
				for(var a=0; a<f.length; a++){
					d.push(sel.data[f[a]]);
				}
				window.parent.showConfirmDlg(I18N.COMMON.tip,String.format(I18N.SNMPv3.d.confirmDel, 
						partName[selNum] + ": " + d[0]),function(type){
		            if (type == 'no') {
		                return;
		            }
		            _initF_.index = store[part[selNum] + "Store"].indexOf(sel);
					deleteOne(selNum, d);
		        });
			}
			isCouldnotManuGrid = false;
		}, 100);
	}
}
function okBtClick(){
	var d = [];
	var f = fields[part[selNum] + "Fields"];
	_check_.errorMessage = "";
	for(var a=0; a<f.length; a++){
		var pa = $("#in_" + f[a]);
		_check_.va = pa.val();
		if(_check_['c_' + f[a]]()){
			d.push(_check_.va);
		}else{
			try{
				if(selNum == 4 && (f[a] == "subtree" || f[a] == "mask")){
					$("#in_subtreeMask").focus();
					document.getElementById("in_subtreeMask").select();
				}else if(f[a] == "address"){
					ipFocus("ip_address", 1);
				}else{
					pa.focus();
					document.getElementById("in_" + f[a]).select();
				}
			}catch (e){
				var mess = "";
				for(var z in e){
					mess += z + ":" + e[z] + ";";
				}
			}
			if(_check_.errorMessage){
				return window.parent.showMessageDlg(I18N.COMMON.tip, _check_.errorMessage);
			}else{
				return;
			}
		}
	}
	if(!checkedIndexOnly(selNum, d)){
		return window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.SNMPv3.t.conflict, partName[selNum]));
	}
	if(modifyData.storType){
		modifyOne(selNum, d);
	}else{
		addOne(selNum, d);
	}
}
function checkedIndexOnly(s, d){
	var tmpD = data[part[s] + "Data"];
	var f = fields[part[s] + "Fields"];
	for(var a=0; a<tmpD.length; a++){
		var tmpCount = 0;
		var flag = false;
		for(var y=0; y<partIndexFlag[s].length; y++){
			var isy = partIndexFlag[s][y];
			tmpCount += tmpD[a][isy] == d[isy] ? 1 : 0;
			if(tmpD[a][isy] != modifyData[f[isy]]){
				flag = true;
			}
		}
		flag = (tmpCount == partIndexFlag[s].length) && flag ? true : false;
		if(flag){
			return false;
		}
	}
	return true;
}
function _init_(fa, v){
	_initF_.va = "";
	if(v || v == 0){
		_initF_.va = v;
	}else{
		if(fa == "tarParMPModel"){
			//tarParMPModelValue = ["SNMPv1", "SNMPv2", "SNMPv2C", "SNMPv3", "ALL"];
			_initF_.va = 3;
		}else if(fa == "securityModel"){
			//securityModelValue = ["ANY SEC MODEL", "SEC MODEL V1", "SEC MODEL V2", "SEC MODEL USM"];
			_initF_.va = 3;
		}
	}
	return _initF_["f_" + fa]();
}
var _initF_ = {
	va : "",
	index : 0,
	ipIsInput : false,
	ipValue : "",
	f_isModifying : function(){
		return false;
	},
	storType : "<select id='in_storType' style='width:80px;'><option value=3>" + I18N.SNMPv3.n.nonVolatile + "</option></select>"
	//simpleFields: ['address', 'port', 'notifyType', 'securityModel', 'securityName', 'securityLevel', 'storType']
	,f_storType : function(){
		return this.storType;
	}
	//targetFields: ['targetName', 'domain', 'address', 'timeout', 'retryCount', 'tagList', 'paramsName', 'storType', 'port']
	,f_targetName : function(){
		return String.format("<input id='in_targetName' style='width:90px;border:1px solid #8bb8f3;' value='{0}' {1} title='{2}' />", 
				this.va, "maxlength=32 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.length32Tip);
	}
	,f_domain : function(){
		var re = "<select id='in_domain' style='width:50px;'>";
		if(this.va){
			re += String.format("<option value='{0}'>{1}</option>", this.va, TDomainStr[this.va]);
		}
		for(var x in TDomainStr){
			if(typeof x == 'string' && this.va != x){
				re += String.format("<option value='{0}'>{1}</option>", x, TDomainStr[x]);
			}
		}
		re += "</select>";
		return re;
	}
	,f_address : function(){
		this.ipValue = "";
		if(this.va){
			if(this.va.indexOf(":") > -1){
				var ips = this.va.split(":");
				if(ips.length == 6){
					this.ipValue = [];
					for(var t=0; t<4; t++){
						this.ipValue.push(parseInt(ips[t], 16));
					}
					this.ipValue = this.ipValue.join(".");
					if(!checkedIpValue(this.ipValue)){
						this.ipValue = "";
					}
				}
			}else if(this.va.toString().indexOf(".") > -1 && checkedIpValue(this.va)){
				this.ipValue = this.va;	
			}
		}
		this.ipIsInput = true;
		return "<span id=in_address></span>";
	}
	,f_port : function(){
		if(this.va && this.va.toString().indexOf(":") > -1){
			var ips = this.va.split(":");
			if(ips.length == 6){
				this.va = parseInt(ips[4] + ips[5], 16);
			}
		}
		return String.format("<input id=in_port value='{0}' style='width:40px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=5 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.portNumTip);
	}
	,f_timeout : function(){
		return String.format("<input id=in_timeout value='{0}' style='width:60px;border:1px solid #8bb8f3;' {1}  title='{2}' />", 
				this.va, "maxlength=10 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.longNumTip);
	}
	,f_retryCount : function(){
		return String.format("<input id=in_retryCount value='{0}' style='width:50px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=3 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.num255);
	}
	,f_tagList : function(){
		return String.format("<input id=in_tagList value='{0}' style='width:90px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=255 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.tagListTip);
	}
	//tParamFields: ['paramsName', 'tarParMPModel', 'securityModel', 'securityName', 'securityLevel', 'profileName', 'storType']
	,f_paramsName : function(){
		return String.format("<input id=in_paramsName value='{0}' style='width:110px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=32 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.targetNameTip);
	}
	,f_tarParMPModel : function(){
		return String.format("<input id=in_tarParMPModel value='{0}' style='width:70px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=10 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.longNumTip);
	}
	,f_securityModel : function(){
		return String.format("<input id=in_securityModel value='{0}' style='width:70px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=10 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.longNumTip2);
	}
	,f_securityName : function(){
		return String.format("<input id=in_securityName value='{0}' style='width:150px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=255 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.length255);
	}
	,f_securityLevel : function(){
		var re = "<select id=in_securityLevel style='width:110px;'>"
		if(this.va){
			re += String.format("<option value={0}>{1}</option>", this.va, levelCNStr[this.va]);
		}else{
			re += String.format("<option value={0}>{1}</option>", -1, levelCNStr[0]);
		}
		for(var t=1; t<levelCNStr.length; t++){
			if(levelCNStr[t] && t != this.va){
				re += String.format("<option value={0}>{1}</option>", t, levelCNStr[t]);
			}
		}
		re += "</select>";
		return re;
	}
	,f_profileName : function(){
		return String.format("<input id=in_profileName value='{0}' style='width:140px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=32 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.length32Tip);
	}
	//notifyFields: ['notifyName', 'notifyTag', 'notifyType', 'storType']
	,f_notifyName : function(){
		return String.format("<input id=in_notifyName value='{0}' style='width:220px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=32 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.length32Tip);
	}
	,f_notifyTag : function(){
		return String.format("<input id=in_notifyTag value='{0}' style='width:170px;border:1px solid #8bb8f3;' {1} title='{2}' />", 
				this.va, "maxlength=255 onkeyup='keyup(this)' onblur='keyup(this)'", I18N.SNMPv3.t.length255);
	}
	,f_notifyType : function(){
		var re = "<select id=in_notifyType style='width:90px;'>"
		if(this.va){
			re += String.format("<option value={0}>{1}</option>", this.va, notifyTypeStr[this.va]);
		}else{
			re += String.format("<option value={0}>{1}</option>", 0, notifyTypeStr[0]);
		}
		for(var t=1; t<notifyTypeStr.length; t++){
			if(notifyTypeStr[t] && t != this.va){
				re += String.format("<option value={0}>{1}</option>", t, notifyTypeStr[t]);
			}
		}
		re += "</select>";
		return re;
	}
	//nFilterFields: ['profileName', 'subtreeMask', 'type', 'storType', 'subtree', 'mask']
	,f_subtreeMask : function(){
		return String.format("<input id=in_subtreeMask value='{0}' style='width:290px;border:1px solid #8bb8f3;' {1} />", 
				this.va, "maxlength=127 onkeyup='keyup(this)' onblur='keyup(this);subtreeBlur()'");
	}
	,f_subtree : function(){
		return "";
	}
	,f_mask : function(){
		return "";
	}
	,f_type : function(){
		var re = "<select id=in_type style='width:70px;'>"
		if(this.va){
			re += String.format("<option value={0}>{1}</option>", this.va, nFilterTypeStr[this.va]);
		}else{
			re += String.format("<option value={0}>{1}</option>", 0, nFilterTypeStr[0]);
		}
		for(var t=1; t<nFilterTypeStr.length; t++){
			if(nFilterTypeStr[t] && t != this.va){
				re += String.format("<option value={0}>{1}</option>", t, nFilterTypeStr[t]);
			}
		}
		re += "</select>";
		return re;
	}
}
var _check_ = {
	va : "",
	errorMessage : "",
	c_isModifying : function(){
		return true;
	}
	//simpleFields: ['address', 'port', 'notifyType', 'securityModel', 'securityName', 'securityLevel', 'storType']
	,c_storType : function(){
		return true;
	}
	//targetFields: ['targetName', 'domain', 'address', 'timeout', 'retryCount', 'tagList', 'paramsName', 'storType', 'port']
	,c_targetName : function(){
		if(this.va && this.va.length < 33 && isNotChineseStr(this.va)){
			return true;
		}else if(this.va == '0'){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errTargetName;
			return false;
		}
	}
	,c_domain : function(){
		return true;
	}
	,c_address : function(){
		var flag = ipIsFilled("ip_address");
		setIpBorder("ip_address", (flag ? "default" : "1px solid #ff0000"));
		if(!flag){
			this.errorMessage = I18N.SNMPv3.t.errIpAddress;
		}else{
			this.va = getIpValue("ip_address");
		}
		return flag;
	}
	,c_timeout : function(){
		if((this.va || this.va == '0') && isOnlyNumStr(this.va) && parseFloat(this.va) > -1 && parseFloat(this.va) < 2147483648){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errTimeout;
			return false;
		}
	}
	,c_retryCount : function(){
		if((this.va || this.va == '0') && isOnlyNumStr(this.va) && parseFloat(this.va) > -1 && parseFloat(this.va) < 256){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errRetryCount;
			return false;
		}
	}
	,c_tagList : function(){
		var flag = true;
		var list = this.va.split(",");
		for(var u=0; u<list.length; u++){
			if(!list[u]){
				flag = false;
				break;
			}
		}
		if(this.va && this.va.length < 256 && this.va.indexOf(",,") == -1 && flag && isNotChineseStr(this.va)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errTagList;
			return false;
		}
	}
	,c_port : function(){
		if(this.va && isOnlyNumStr(this.va) && ((parseFloat(this.va) > 1023 && parseFloat(this.va) < 65536) || parseFloat(this.va) == 162)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errPortNum;
			return false;
		}
	}
	//tParamFields: ['paramsName', 'tarParMPModel', 'securityModel', 'securityName', 'securityLevel', 'profileName', 'storType']
	,c_paramsName : function(){
		if(this.va && this.va.length < 33 && isNotChineseStr(this.va)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errTargetParamName;
			return false;
		}
	}
	,c_tarParMPModel : function(){
		if((this.va == '0' || this.va) && isOnlyNumStr(this.va) && parseFloat(this.va) > -1 && parseFloat(this.va) < 2147483648){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errMPModel;
			return false;
		}
	}
	,c_securityModel : function(){
		if(this.va && isOnlyNumStr(this.va) && parseFloat(this.va) > 0 && parseFloat(this.va) < 2147483648){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errSeModel;
			return false;
		}
	}
	,c_securityName : function(){
		if(this.va && this.va.length < 256 && isNotChineseStr(this.va)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errSeName;
			return false;
		}
	}
	,c_securityLevel : function(){
		if(this.va && parseInt(this.va) > 0 && parseInt(this.va) < 4){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.c.plsSelect + "securityLevel!";
			return false;
		}
	}
	,c_profileName : function(){
		if(this.va && this.va.length < 33 && isNotChineseStr(this.va)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errProfileName;
			return false;
		}
	}
	//notifyFields: ['notifyName', 'notifyTag', 'notifyType', 'storType']
	,c_notifyName : function(){
		if(this.va && this.va.length < 33 && isNotChineseStr(this.va)){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errNotifyName;
			return false;
		}
	}
	,c_notifyTag : function(){
		if(this.va.length < 256 && isNotChineseStr(this.va) && this.va.indexOf(",") == -1){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errTag;
			return false;
		}
	}
	,c_notifyType : function(){
		if(this.va && parseInt(this.va) > 0 && parseInt(this.va) < 3){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errNotifyType;
			return false;
		}
	}
	//nFilterFields: ['profileName', 'subtreeMask', 'type', 'storType', 'subtree', 'mask']
	,c_subtreeMask : function (){
		var reg = /^[\d][\d*.]{1,125}[*\d]+$/ig;
		if(!reg.exec(this.va) || this.va.length < 3 || this.va.length > 127 || this.va.indexOf("..") > -1){
			this.errorMessage = I18N.SNMPv3.t.errSubtreeMask;
			return false;
		}
		var tmp = this.va.split("*.");
		for(var a=0; a<tmp.length; a++){
			if(tmp[a].indexOf("*") > -1){
				if(a == tmp.length - 1 && tmp[a].indexOf("*") == tmp[a].length - 1){
					continue;
				}
				this.errorMessage = I18N.SNMPv3.t.errSubtreeMask2;
				return false;
			}
		}
		tmp = this.va.split(".*");
		for(a=0; a<tmp.length; a++){
			if(tmp[a].indexOf("*") > -1){
				this.errorMessage = I18N.SNMPv3.t.errSubtreeMask2;
				return false;
			}
		}
		return true;
	}
	,c_type : function(){
		if(this.va && parseInt(this.va) > 0 && parseInt(this.va) < 3){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errActionType;
			return false;
		}
	}
	,c_subtree : function(){
		if($("#in_subtree").val()){
			if($("#in_subtree").val() == "flag_subtreeIsUsed_flag"){
				this.errorMessage = I18N.SNMPv3.t.errSubtree;
				return false;
			}
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errSubtreeMask2;
			return false;
		}
	}
	,c_mask : function(){
		if($("#in_mask").val()){
			return true;
		}else{
			this.errorMessage = I18N.SNMPv3.t.errSubtreeMask2;
			return false;
		}
	}
}
function ipChanged(){
	var ip = getIpValue("ip_address");
	setIpBorder("ip_address", "default");
	if(!ip || ip == "" || ip == "..."){
		
	}else if(!ipIsFilled("ip_address")){
		setIpBorder("ip_address", "1px solid #ff0000");
	}
}
function keyup(el){
	_check_.va = $(el).val();
	if(!_check_["c_" + el.id.split("_")[1]]()){
		$(el).css("border", "1px solid #ff0000");
	}else{
		$(el).css("border", "1px solid #8bb8f3");
	}
}
function addBtOver(el){
	addBtMouseFlag = false;
	$(el).css("background-color", "#dbf7fd");
}
function addBtOut(el){
	addBtMouseFlag = true;
	setTimeout(function(){
		if(addBtMouseFlag){
			$(el).css('background-color', 'transparent');
		}
	}, 80);
}
function subtreeBlur(){
	var v = $("#in_subtreeMask").val();
	var n = $("#in_profileName").val();
	_check_.va = v;
	if(n && v && _check_.c_subtreeMask()){
		$('#in_subtree').val(getAUseableSubtree(v, n));
		$('#in_mask').val(getMaskFromSubtree(v));
	}else{
		$('#in_subtree').val("");
		$('#in_mask').val("");
	}
}
function subtreeMerge(tree, mask){
	var re = [];
	mask = parseInt(mask, 16).toString(2);
	tree = tree.split(".");
	var len = tree.length;
	var m = mask.length;
	for(var a=0; a<len; a++){
		if(a < m && mask.substring(a, a + 1) == '0'){
			re.push("*");
			continue;
		}
		re.push(tree[a]);
	}
	return re.join(".");
}
function getMaskFromSubtree(subtree){
	var mask = "";
	var ss = subtree.split(".");
	for(var a=0; a<ss.length; a++){
		if(ss[a] == "*" && mask){
			mask += "0";
		}else{
			mask += "1";
		}
	}
	if(mask.length < 33){
		mask = parseInt(mask, 2).toString(16);
	}else{
		var len = mask.length;
		mask = parseInt(mask.substring(0, 32), 2).toString(16) + parseInt(mask.substring(32, len), 2).toString(16);
	}
	len = mask.length;
	if(len % 2 == 1){
		mask = "0" + mask;
	}
	return mask;
}
function getAUseableSubtree(subtree, proName){
	var subList = [];
	for(var a=0; a<NotifyFilter.length; a++){
		if(NotifyFilter[a].notifyFilterProfileName == proName){
			subList.push(NotifyFilter[a].notifyFilterSubtree);
		}
	}
	if(isOperationFlag && modifyData.storType){
		subList.splice(subList.indexOf(modifyData.subtree), 1);
	}
	var index = subtree.indexOf("*");
	if(index == -1){
		if(subList.indexOf(subtree) == -1){
			_check_.va = subtree;
			return _check_.c_subtreeMask() ? subtree : "";
		}else{
			return "flag_subtreeIsUsed_flag";
		}
	}else if(index > 1){
		var tt = subtree.split("*");
		index = subtree.length - tt[tt.length - 1].length - 1;
		if(index > -1){
			for(var x=1; x<1000; x++){
				var flag = false;
				for(var a=0; a<subList.length; a++){
					if(subList[a].substring(index, index + 1) == ("" + x)){
						flag = true;
						break;
					}
				}
				if(!flag){
					var s = subtree.replace(/[*]/g, "1");
					subtree = s.substring(0, index) + x + s.substring(index + 1, s.length);
					_check_.va = subtree;
					return _check_.c_subtreeMask() ? subtree : "";
				}
			}
		}
		return "flag_subtreeIsUsed_flag";
	}else{
		return "";
	}
}

function cancelClick(){
	window.parent.closeWindow('snmpV3Notify');
}
</script>
</head>
	<body class=openWinBody>
			<div class="edge10">
			<table class="mCenter" >
				<tr>
					<td><div id=partDiv></div></td>
				</tr>
				<tr>
					<td><div id=gridDiv></div></td>
				</tr>
			</table>
			</div>
		<input id=in_subtree style='display: none;' />
		<input id=in_mask style='display: none;' />
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshBtClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>