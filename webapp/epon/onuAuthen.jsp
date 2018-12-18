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
    module epon
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
	button[disabled]{  filter: alpha(opacity=50); -webkit-opacity: 0.5; -moz-opacity: 0.5; -ms-opacity: 0.5; -o-opacity: 0.5; opacity: 0.5; cursor:default !important;}
	a[disabled]{  filter: alpha(opacity=50); -webkit-opacity: 0.5; -moz-opacity: 0.5; -ms-opacity: 0.5; -o-opacity: 0.5; opacity: 0.5; cursor:default; pointer-events:none;}
	#okBt, #closeBt{ background: transparent; border:none; color: #B3711A; cursor:pointer; padding-left: 2px; padding-right:2px;}
</style>
<script type="text/javascript">
var entityId = ${entityId};//OLT的entityId
var slotId = '${slotId}';
var ponId = '${ponId}';
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
//界面数据
var slotIdList = ${ponListObject};  //[{slotId: ,slotNo: },{},{}]
var ponIdList = ${slotPonObject};   //[{slotId: ,slotNo: ,ponId: ,ponNo: },{},{}]
var autoModeList = ${autoModePonIdList};  //是自动认证模式的ponId列表
autoModeList = autoModeList.join("")=="false" ? new Array() : autoModeList;
var macModeList = ${macModePonIdList};  //是MAC认证模式的ponId列表
macModeList = macModeList.join("")=="false" ? new Array() : macModeList;
var mixModeList = ${mixModePonIdList};  //是混合认证模式的ponId列表
mixModeList = mixModeList.join("")=="false" ? new Array() : mixModeList;
var snModeList = ${snModePonIdList};    //是SN认证模式的ponId列表
snModeList = snModeList.join("")=="false" ? new Array() : snModeList;
var snPwdModeList = ${snPwdModePonIdList};    //是SN密码认证模式的ponId列表
snPwdModeList = snPwdModeList.join("")=="false" ? new Array() : snPwdModeList;
var onuNoList = new Array();        //配置过的onuNo的列表(1-onuMaxNumInPon)
var blockOnuNoList = new Array();   //配置过的onuNo的block列表(129-160)
var onuNoListFlag = true;//onuNo是否还有剩余(1-onuMaxNumInPon)
var blockNoListFlag = true;//blockNo是否还有剩余(129-160)
var authMacList = ${onuAuthMacListObject};  //entity的认证规则的Mac列表 ,[mac,location,mac,location,mac,location,,,,,]
authMacList = authMacList.join("")=="false" ? new Array() : authMacList;
var authSnList = ${onuAuthSnListObject};    //entity的认证规则的SN列表,[sn,location,sn,location,sn,location,,,,]
authSnList = authSnList.join("")=="false" ? new Array() : authSnList;
var ponAuthStatus = ${ponAuthEnableList};   //开启了ONU认证管理的PON口的列表
ponAuthStatus = ponAuthStatus.join("")=="false" ? new Array() : ponAuthStatus;
var onuMaxNumInPon = 64;    //当前PON口下的ONU最大数目，default:64
var onuMaxNumList = ${onuMaxNumList};   //各个PON口支持的ONU最大数   [[num, num, num, num....],[],[]...]没有的全部赋值为0
//界面逻辑参数
var macWidth = 375; //( ,0, ) ( ,380, )
var snWidth = 465;  //( ,0, ) ( ,470, )
var macSnHeight = 190;  //(0, -) (, 240)
var addingFlag = false; //是否为正在添加的页面格式
var addingFlag2 = false;//false：添加mac，ture：添加sn
var modifyingIndex = -1;
var modifyingData = new Array();//[location, mac/sn, action/password, ponId, false/true, preType]
var insteadFlag = false;
var showBlockFirst = true;
var showBlockFlag = false;
var modeTipStr = [I18N.onuAuth.tip.modeTipStr0,I18N.onuAuth.tip.modeTipStr1,I18N.onuAuth.tip.modeTipStr2,'',I18N.onuAuth.tip.modeTipStr5];
//{'8621': 33, '8622': 34, '8624': 36, '8625': 37, '8641': 65, '8644': 68, '8647': 71, '8651': 81, 
//	'8652': 82, '8653': 83, '8654': 84, 'none': 0};
var onuTypes = ${onuTypes};
var ponRejectdList;
var CURRENT_AUTH_MODE;
var BLOCK_LIST_START_ID = 129,
	BLOCK_LIST_END_ID = 193;

var batchAddFlag = false;
var batchOnuNumList = new Array();//[[{ponId, ponNum, slotNum}, onuNoList[], blockNoList[] ],[],[]]

//界面全局变量
var blockData = new Array();//['location','mac','authTime','sn','password','ponId']
var blockStore;
var blockGrid;
var macData = new Array();
var macStore;
var macGrid;
var snData = new Array();
var snStore;
var snGrid;
$(function(){
	//非法ONU注册;
	var olt_illegal_reg = top.PubSub.on(top.OltTrapTypeId.ONU_ILLEGAL_REG, function(data){
		setTimeout(function(){
			trapRefreshPageEntityId(data);
		}, 15000);
	});
	//ONU删除;
	var onu_delete = top.PubSub.on(top.OltTrapTypeId.ONU_DELETE, function(data){
		setTimeout(function(){
			trapRefreshPage(data);
		},15000);
	});
	//ONU MAC认证失败，這個時候是沒有slotNum和portNum的，他們都是0; 
	var onu_mac_aith_fail = top.PubSub.on(top.OltTrapTypeId.ONU_MAC_AUTH_FAIL, function(data){
		trapMacAuthFail(data);
	});
	
	$(window).on('unload', function(){
		top.PubSub.off(top.OltTrapTypeId.ONU_ILLEGAL_REG, olt_illegal_reg);
		top.PubSub.off(top.OltTrapTypeId.ONU_DELETE, onu_delete);
		top.PubSub.off(top.OltTrapTypeId.ONU_MAC_AUTH_FAIL, onu_mac_aith_fail);
	});
});
function trapRefreshPage(data){
	var slotNum = parseInt($("#slotSel").find("option:selected").text(), 10),
	    portNum = parseInt($('#ponSel').find("option:selected").text(), 10);

	if(!data || !data.entityId || window.entityId != data.parentId || !data.sourceObject 
		|| !data.sourceObject.slotNo || !data.sourceObject.portNo 
		|| data.sourceObject.slotNo != slotNum || data.sourceObject.portNo != portNum){
		 return;
	} 
	HeadMessage.sendMsg(data.clearMessage || data.message);
}

function trapRefreshPageEntityId(data){
	var slotNum = parseInt($("#slotSel").find("option:selected").text(), 10),
    portNum = parseInt($('#ponSel').find("option:selected").text(), 10);
	
	if(!data || !data.entityId || window.entityId != data.entityId || !data.sourceObject 
		|| !data.sourceObject.slotNo || !data.sourceObject.portNo 
		|| data.sourceObject.slotNo != slotNum || data.sourceObject.portNo != portNum){
		 return;
	} 
	HeadMessage.sendMsg(data.clearMessage || data.message);
}
function trapMacAuthFail(data){
	if(!data || !data.entityId || window.entityId != data.parentId){
		 return;
	} 
	HeadMessage.sendMsg(data.clearMessage || data.message);
}
function disabledBtns(){
	var $macAddBt = $("#macAddBt");
	var $modeSel = $("#modeSel");
	var v = parseInt($modeSel.val(), 10);
	if(v===4 || v===5){
		$("#macAddBt").attr({disabled: true});
	}else{
		$macAddBt.removeAttr("disabled");
	}
}

function cancelClick(){
    window.parent.closeWindow('onuAuthen');
}
function refreshAuthClick(){
    if(addingFlag){
        window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.fetchConfirm, function(type) {
            if (type == 'no') {
                return;
            }
            refreshAuth();
        });
    }else{
        refreshAuth();
    }
}
function refreshAuth(){
    showMask();
    $.ajax({
        url : '/epon/onuauth/refreshOnuAuthInfo.tv?entityId=' + entityId,
        success : function() {
            var tmpW = window.parent.getWindow("onuAuthen");
            tmpW.setHeight(300);
            tmpW.body.setHeight(485);
            //$("#bodyFieldset").height(440);
            reloadLocation();
        },
        error : function() {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.fetchFailed);
            removeMask();
        }
    });
}
function refreshBlock(s){
    showMask();
    var url = '/epon/onuauth/refreshOnuAuthBlock.tv?entityId=' + entityId;
    Ext.Ajax.request({
        url : url,
        success : function(response) {
            if(response.responseText != 'success'){
                removeMask();
                $("#refreshBlockBt,#blockTip").show();
                //$("#blockTip").show();
                showBlockFirst = true;
                return;
            }
            removeMask();
            $("#blockTip").hide();
            if(s == 1){
                blockBtClick(1);
            }else if(showBlockFlag){
                loadBlockData();
            }
        },
        failure : function() {
            removeMask();
            $("#refreshBlockBt").show();
            $("#blockTip").show();
            showBlockFirst = true;
        }
    });
}
function deleteMacAuth(data){
    window.parent.showConfirmDlg("@COMMON.tip@", String.format(I18N.onuAuth.tip.delConfirm1, data.split("-")[0])
            + '<br><font color=red>'+ I18N.onuAuth.tip.delConfirm2 + '</font>', function(type) {
        if (type == 'no') {return;}
        deleteAuthRule(data, 1);
    });
}
function editMacAuth(rid){
	var record = macStore.getById(rid);
}

function deleteSnAuth(data){
    window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.delConfirm0, function(type) {
        if (type == 'no') {
            return;
        }
        deleteAuthRule(data, 2);
    });
}
function deleteAuthRule(data, s){
    $("#onuNoTip").hide();
    var ponIdDel;
    var slotNoDel;
    var ponNoDel;
    var onuNoDel;
    if(addingFlag){
        closeBtClick(1);
    }
    var authTypeDel = s;
    if(s == 1){
    	ponIdDel = data.split("-")[1];
        slotNoDel = data.split("-")[0].split("/")[0];
        ponNoDel = data.split("-")[0].split("/")[1].split(":")[0];
        onuNoDel = data.split("-")[0].split(":")[1];
    }else{
    	ponIdDel = data.split("#")[1];
   	    slotNoDel = data.split("#")[0].split("/")[0];
   	    ponNoDel = data.split("#")[0].split("/")[1].split(":")[0];
   	    onuNoDel = data.split("#")[0].split(":")[1];
    }
    var url = '/epon/onuauth/deleteOnuAuthRule.tv?authType=' + authTypeDel + '&entityId=' + entityId + '&ponId=' + ponIdDel 
        + '&slotNo=' + slotNoDel + '&ponNo=' + ponNoDel + '&onuNo=' + onuNoDel;
    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.tip.deleting, 'ext-mb-waiting');
    Ext.Ajax.request({
        url : url,
        success : function(response) {
            if(response.responseText != 'success'){
                window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.delFailed);
                return;
            }
            // 删除成功。
            window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.deSuccess);
            if(s == 1){
                authMacList.splice(authMacList.indexOf(changeMacToMaohao(data.split("-")[2])), 2);
                macData.splice(macStore.indexOf(macGrid.getSelectionModel().getSelected()), 1);
                macStore.loadData(macData);
            }else if(s ==2){
                authSnList.splice(authSnList.indexOf(data.split("#")[2]), 2);
                snData.splice(snStore.indexOf(snGrid.getSelectionModel().getSelected()), 1);
                snStore.loadData(snData);
            }
        },
        failure : function() {
            window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.delFailed);
        }
    });
}
function autoAuthChange(){
    var tmp = $("#autoAuthSel").val();
    if(tmp == 0){
        closeAuthBtClick();
    }else if(tmp == 1){
        openAuthBtClick();
    }
}
function closeAuthBtClick(){
    window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.autoOffConfirm1
            + '<br><font color=red>'+ I18N.onuAuth.tip.autoOffConfirm2 + '</font>', function(type) {
        if (type == 'no') {
            $("#autoAuthSel").val(1);
            return;
        }
        modifyOnuAuthEnable(0);
    });
}
function openAuthBtClick(){
    window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.autoOnConfirm1
            + '<br><font color=red>'+ I18N.onuAuth.tip.autoOnConfirm2 + '</font>', function(type) {
        if (type == 'no') {
            $("#autoAuthSel").val(0);
            return;
        }
        modifyOnuAuthEnable(1);
    });
}
function modifyOnuAuthEnable(stat){
    var str = stat == 1 ? I18N.onuAuth.tip.autoOpening : I18N.onuAuth.tip.autoClosing;
    window.top.showWaitingDlg(I18N.COMMON.wait, str, 'ext-mb-waiting');
    var url = '/epon/onuauth/modifyOnuAuthEnable.tv?entityId=' + entityId + '&ponId=' + ponId + '&authEnable=' + stat;
    str = stat == 1 ? I18N.onuAuth.tip.autoOpenFailed : I18N.onuAuth.tip.autoCloseFailed;
    Ext.Ajax.request({
        url : url,
        success : function(response) {
            if(response.responseText != 'success'){
                window.parent.showMessageDlg("@COMMON.tip@", str);
                if(stat == 0){
                    $("#autoAuthSel").val(1);
                }else if(stat == 1){
                    $("#autoAuthSel").val(0);
                }
                return;
            }
            str = stat == 1 ? I18N.onuAuth.tip.autoOpenSuc : I18N.onuAuth.tip.autoCloseSuc;
            window.parent.showMessageDlg("@COMMON.tip@", str);
            if(stat == 0){//open
                ponAuthStatus.unshift(ponId);
            }else if(stat == 1){//close
                ponAuthStatus.splice(ponAuthStatus.indexOf(ponId), 1);
            }
        },
        failure : function() {
            window.parent.showMessageDlg("@COMMON.tip@", str);
            if(stat == 0){
                $("#autoAuthSel").val(1);
            }else if(stat == 1){
                $("#autoAuthSel").val(0);
            }
        }
    });
}

/*
 * 页面数据
 */
function loadAuthData(){
    var authStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : '/epon/onuauth/getOnuAuthRule.tv?entityId=' + entityId + '&slotId=' + slotId + '&ponId=' + ponId
        }),
        reader : new Ext.data.ArrayReader({
            id : 0
        }, Ext.data.Record.create([ {
            name : 'macAuthRuleList'
        }, {
            name : 'snAuthRuleList'
        } ]))
    });
    authStore.load({
        callback : function(records, options, success) {
            // 数据加载完毕更新数组中的值
            var bb = 0;
            var dd = 0;         
            macData = new Array();
            snData = new Array();
            $.each(records, function(i, n) {
                if(n.json[6*i+1] == "MAC"){
                    macData[bb] = new Array();
                    macData[bb][0] = n.json[6*i];
                    macData[bb][1] = n.json[6*i+2];
                    macData[bb][2] = n.json[6*i+3];
                    macData[bb][3] = n.json[6*i+4];
                    macData[bb][4] = n.json[6*i+5];
                    bb++;
                }else if(n.json[6*i+1] == "SN"){
                    snData[dd] = new Array();
                    snData[dd][0] = n.json[6*i];
                    snData[dd][1] = n.json[6*i+2];
                    snData[dd][2] = n.json[6*i+3];
                    snData[dd][3] = n.json[6*i+4];
                    snData[dd][4] = n.json[6*i+5];
                    dd++;
                }
            });
            macStore.loadData(macData);
            snStore.loadData(snData);
        }
    });
}
function loadMacGrid(){
    isMacStoreReady = false;
    $("#macGridDiv").empty();
    macStore = new Ext.data.SimpleStore({
        fields : ['location','mac','action','ponId', 'preType','isAdd'],
        listeners:{
        	load:function(){
        		$("#macAddBt").attr("disabled",false);
        		disabledBtns();
        	}
        }
    });
    var m = $("#modeSel").val();
    var w1 = 120;
    var w2 = 150;
    var w3 = 120;
    var w4 = 190;
    if(m == 2){//780
        w1 = 200;
        w2 = 200;
        w3 = 155;
        w4 = 200;
    }
    macGrid = new Ext.grid.GridPanel({
        id : 'macGrid',
        renderTo : 'macGridDiv',
        store : macStore,
        width : macWidth,
        title: '@onuAuth.mac@',
        cls : 'normalTable',
        height : 90 + macSnHeight,
        frame : false,
        autoScroll : true,
        border : true,
        viewConfig: { forceFit: true },
        selModel : new Ext.grid.RowSelectionModel({
            //singleSelect : true
        }),
        listeners : {
        	viewready : function (){
                if(addingFlag && !addingFlag2 && !batchAddFlag){
                    macGrid.getSelectionModel().selectRow(modifyingIndex, true);
                    macGrid.getView().focusRow(modifyingIndex);
                    macKeyup();
                    if($("#actionSel").val() == 2){
                        $("#onuTypeSel").hide();
                    }else{
                        $("#onuTypeSel").show();
                    }
                }
                isMacStoreReady = true;
            },
            rowcontextmenu: function(grid,rowIndex,e){
                gridContext(grid,rowIndex,e);
            }
        },
        columns: [{
                header: I18N.onuAuth.onuLocation,
                dataIndex: 'location',
                width : 120,
                renderer : function (value, cellmeta, record) {
                	if(record.data.isAdd){
	                    if(batchAddFlag){
	                        if(record.data.batchDataFlag){
	                            return value;
	                        }
	                    }
	                    if(addingFlag && !addingFlag2 && !insteadFlag){
	                        if(record.json[1] == modifyingData[1]){
	                            return initOnuNoSel(value);
	                        }
	                    }
                    }
                    return value;
                }
            },{
                header: "MAC",
                dataIndex: 'mac',
                width : 185,
                hidden : addingFlag && addingFlag2,
                renderer : function(value, cellmeta, record) {
                	if(record.data.isAdd){
                		if(batchAddFlag){
                			if(record.data.batchDataFlag){
                				return value;
                			}
                		}
                		if(addingFlag && !addingFlag2){
                			if(value == modifyingData[1]){
                				return initMacInput(value);
                			}
                		}
                	}
                    return value;
                }
            },{
                header: I18N.onuAuth.action,
                dataIndex: 'action',
                css :"vertical-align:top;",
                hidden : addingFlag && addingFlag2,
                renderer : function(value, cellmeta, record) {
                	if(record.data.isAdd){
                		if(batchAddFlag){
                			if(record.data.batchDataFlag){
                				return value;
                			}
                		}
                		if(addingFlag && !addingFlag2 && !insteadFlag){
                			if(record.json[1] == modifyingData[1]){
                				if(value == "" || value == null){
                					value == 2;
                				}
                				return initActionSel(value);
                			}
                		}
                	}
                    if(value == 1 || value == I18N.onuAuth.permit ){
                        return "<font color=green>@onuAuth.permit@</font>";
                    }else{
                        return "<font color=red>@onuAuth.reject@</font>";
                    }
                }
            },{
                header: I18N.onuAuth.preType,
                dataIndex: 'preType',
                width: 125,
                css :"vertical-align:top;",
                hidden: addingFlag && addingFlag2,
                renderer: function (value, cellmate, record){
                	if(record.data.isAdd){
	                    if(batchAddFlag){
	                        if(record.data.batchDataFlag){
	                            return typeRange(value);
	                        }
	                    }
	                    if(addingFlag && !addingFlag2 && !insteadFlag){
	                        if(record.json[1] == modifyingData[1]){
	                            var ac = record.data.action;
	                            if(ac == 2){
	                                return initOnuTypeSel(1, value);
	                            }else{
	                                return initOnuTypeSel(0, value);
	                            }
	                        }
	                    }
                	}
                    return typeRange(value);
                }
            },{
                header: I18N.COMMON.manu,
                dataIndex: 'id',
                width: 190,
                css :"vertical-align:top;",
                renderer : function(value, cellmeta, record) {
                	if(record.data.isAdd){
	                    if(batchAddFlag){
	                        if(record.data.batchDataFlag){
	                            return initBatchImg(record.data.ponId);
	                        }
	                    }
	                    if(addingFlag && !addingFlag2){
	                        if(record.json[1] == modifyingData[1]){
	                            return initOkBt();
	                        }
	                    }
                	}
                    if(operationDevicePower){
                    	var tmpl = "<img src='/images/delete.gif' onclick='deleteMacAuth(\"{location}-{ponId}-{mac}\")' title='@COMMON.del@'/>";
	                    return String.format( tmpl, record.data );
                    }else{
                    	return "<img src='/images/deleteDisable.gif'  title='@COMMON.del@'/>";
                    }
                }
            }]
    });
}
function loadSnGrid(){
    $("#snGridDiv").empty();
    snStore = new Ext.data.SimpleStore({
        data : snData,
        fields : ['location','sn','password','ponId', 'preType'],
        listeners:{
        	load:function(){
        		$("#snAddBt").attr("disabled",false);
        	}
        }
    });
    var m = $("#modeSel").val();
    //465
    var w1 = 120;
    var w2 = 150;
    var w3 = 120;
    var w4 = 200;
    snGrid = new Ext.grid.GridPanel({
        id : 'snGrid',
        renderTo : 'snGridDiv',
        title : '@onuAuth.sn@',
        store : snStore,
        //width : snWidth,
        height : 90 + macSnHeight,
        frame : false,
        autoScroll : true,
        border : true,
        cls : 'normalTable',
        viewConfig: {
            forceFit: true
        },
        selModel : new Ext.grid.RowSelectionModel({
            //singleSelect : true,
            listeners : {
                'selectionchange' : function(){
                    
                }
            }
        }),
        listeners : {
            'viewready' : function (){
                /* $("#snInsteadBt").hide(); */
                if(addingFlag && addingFlag2){
                    snGrid.getSelectionModel().selectRow(modifyingIndex, true);
                    snGrid.getView().focusRow(modifyingIndex);
                    snKeyup();
                }
            },
            'rowcontextmenu': function(grid,rowIndex,e){
                gridContext(grid,rowIndex,e);
            }
        },
        columns : [{
                header: I18N.onuAuth.onuLocation,
                dataIndex: 'location',
                renderer : function (value, cellmeta, record) {
                    if(addingFlag && addingFlag2 && !insteadFlag){
                        if(record.json[1] == modifyingData[1]){
                            return initOnuNoSel(value);
                        }
                    }
                    return value;
                }
            },{
                header: "SN",
                dataIndex: 'sn',
                hidden : addingFlag && !addingFlag2,
                renderer : function(value, cellmeta, record) {
                    if(addingFlag && addingFlag2){
                        if(value == modifyingData[1]){
                            return initSnInput(value);
                        }
                    }
                    return value;
                }
            },{
                header: I18N.onuAuth.password,
                dataIndex: 'password',
                hidden : addingFlag && !addingFlag2,
                renderer : function (value, cellmeta, record) {
                    if(addingFlag && addingFlag2){
                        if(record.json[1] == modifyingData[1]){
                            return initPasswordInput(value);
                        }
                    }
                    return value;
                }
            },{
                header: I18N.onuAuth.preType,
                dataIndex: 'preType',
                hidden: addingFlag && !addingFlag2,
                renderer: function (value, cellmate, record){
                    if(addingFlag && addingFlag2 && !insteadFlag){
                        if(record.json[1] == modifyingData[1]){
                            return initOnuTypeSel(0, value);
                        }
                    }
                    return typeRange(value);
                }
            },{
                header: I18N.COMMON.manu,
                dataIndex: 'id',
                width: 180,
                renderer : function(value, cellmeta, record) {
                    if(addingFlag && addingFlag2){
                        if(record.json[1] == modifyingData[1]){
                            return initOkBt();
                        }
                    }
                    if(operationDevicePower){
	                    return"<img src='/images/delete.gif' onclick='deleteSnAuth(\"" + record.data.location + "#"
	                        + record.data.ponId + "#" + record.data.sn +"\")' title='@COMMON.del@' />";
                    }
                }
            }]
    });
}
function snContext(grid,rowIndex,e){
	preventBubble(e);
}
function loadBlockData(){
    var blockStoreTmp = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : '/epon/onuauth/loadOnuAuthBlock.tv?entityId=' + entityId + '&slotId=' + slotId + '&ponId=' + ponId
        }),
        reader : new Ext.data.ArrayReader({
                id : 0
            }, Ext.data.Record.create([{
                name : 'blockAuthRuleList'
            }])
        )
    });
    blockStoreTmp.load({
        callback : function(records, options, success) {
            // 数据加载完毕更新数组中的值
            blockData = new Array();
            $.each(records, function(i, n) {
                blockData[i] = new Array();
                blockData[i][0] = n.json[0];
                blockData[i][1] = n.json[1] || "";
                blockData[i][2] = n.json[2];
                blockData[i][3] = n.json[3] || "";
                blockData[i][4] = n.json[4] || "";
                blockData[i][5] = n.json[5];
            });
            blockStore.loadData(blockData);
        }
    });
}
function loadBlockGrid(){
    $("#blockGridDiv").empty();
    blockStore = new Ext.data.SimpleStore({
        data : blockData,
        fields : ['location','mac','authTime','sn','password','ponId']
    });
    blockGrid = new Ext.grid.GridPanel({
        id : 'blockGrid',
        renderTo : 'blockGridDiv',
        cls: 'normalTable',
        store : blockStore,
        height : 160,
        frame : false,
        autoScroll : true,
        border : true,
        viewConfig:{
        	forceFit:true
        },
        title : "@onuAuth.block@",
        selModel : new Ext.grid.RowSelectionModel({
            singleSelect : true
        }),
        columns: [{
                header: I18N.onuAuth.onuLocation,
                dataIndex: 'location',
                width: 120
            },{
                header: "Mac",
                dataIndex: 'mac',
                width: 160,
                renderer : function(value, cellmeta, record) {
                    var m = $("#modeSel").val();
                    if(m == 2 || m == 3){
                        return "<input align=center class='blockMacClass" + value.replace(/([:])/g,"q")
                        + "' type=text style='width:135px; border:0px;text-align:center;' value='"+ value
                        + "' onkeyup='onBlockMacKeyup(\"" + value + "\")' onblur='onBlockMacKeyup(\"" + value + "\")' />";
                    }else{
                        return value;
                    }
                }
            },{
                header: "SN",
                dataIndex: 'sn',
                width: 230,
                renderer : function(value, cellmeta, record) {
                    var m = $("#modeSel").val();
                    if(m == 4 || m == 3){
                        return "<input align=center class='blockSnClass" + value 
                        + "' type=text style='width:200px; border:0px;text-align:center;' value='"+ value 
                        + "' onkeyup='onBlockSnKeyup(\"" + value + "\")' onblur='onBlockSnKeyup(\"" + value + "\")' />";
                    }else{
                        return value;
                    }
                }
            },{
                header: I18N.onuAuth.password,
                dataIndex: 'password',
                width: 160,
            },{
                header: I18N.onuAuth.authTime,
                dataIndex: 'authTime',
                width: 150,
            }],
        listeners: {
        	rowcontextmenu: function(grid,rowIndex,e){
                blockGridContextMenu(grid,rowIndex,e);
                e.stopEvent();
            }
        }
    });
}
function slotIdChange(s){//s==0为初始化ponIdlist
    var pon = Zeta$('ponSel');
    var $pon = $("#ponSel");
    var position = Zeta$('slotSel');
    if(s != 0){
	    slotId = position.options[position.selectedIndex].value;
        ponId = -1;
    }
    pon.options.length = 0;
    for (var i = 0; i < ponIdList.length; i++) {
        if (ponIdList[i].slotId == slotId) {
            $pon.append(String.format("<option value={0}>{1}</option>", ponIdList[i].ponId, ponIdList[i].ponNo));
        }
    }
    $("#ponSel").val(ponId);
    ponIdChange();
}
function ponIdChange(){
    $("#modeTip").hide();
    $("#onuNoTip").hide();
    ponId = $("#ponSel").val();
    var modeSel = $("#modeSel");
    $("#modeText,#modeTipImg,#macAddBt,#snAddBt").show();
    if(autoModeList.indexOf(ponId) > -1){
    	$("#autoAuthDiv").hide();
    	 CURRENT_AUTH_MODE  = 1;
    }else if(macModeList.indexOf(ponId) > -1){
        $("#autoAuthDiv").show();
         CURRENT_AUTH_MODE  = 2;
    }else if(mixModeList.indexOf(ponId) > -1){
        $("#autoAuthDiv").show();
         CURRENT_AUTH_MODE  = 3;
    }else if(snModeList.indexOf(ponId) > -1){
        $("#autoAuthDiv").hide();
         CURRENT_AUTH_MODE  = 4;
    }else if(snPwdModeList.indexOf(ponId) > -1){
        $("#autoAuthDiv").hide();
         CURRENT_AUTH_MODE  = 5;
    }else{
        $("#autoAuthDiv").show();
         CURRENT_AUTH_MODE  = 3;
    }
    modeSel.val( CURRENT_AUTH_MODE );
    var slotNum = parseInt($("#slotSel").find("option:selected").text());
    onuMaxNumInPon = onuMaxNumList[slotNum][parseInt($("#ponSel").find("option:selected").text())];
    $.ajax({
    	url:"/epon/onuauth/loadRejectedMacList.tv",data:{ponId:ponId},cache:false,dataType:'json',async:false,
    	success:function(list){
    		ponRejectdList = list;
    	}
    });
    insteadFlag = false;
    addingFlag = false;
    modeChangeCss();
    getOnuAuth();
}
function getOnuAuth(){
    slotId = $("#slotSel").val();
    ponId = $("#ponSel").val();
    var m = $("#modeSel").val();
    if(showBlockFlag){
        if(!blockGrid || blockGrid == null || blockGrid == undefined){
            loadBlockGrid();
        }
        loadBlockData();
    }
    if(m == 2 || m == 3 || m == 4){
        if(macGrid == null || macGrid == undefined || !macGrid){
            loadMacGrid();
        }
    }
    if(m == 3 || m == 4 || m == 5){
        if(!snGrid || snGrid == null || snGrid == undefined){
            loadSnGrid();
        }
    }
    loadAuthData();
    disabledBtns();
}

/*
 * 初始化
 */
function initSelect(){
    var position = Zeta$("slotSel");
    var $slot = $("#slotSel");
    position.options.length = 0;
    for (var i = 0; i < slotIdList.length; i++) {
        $slot.append(String.format("<option value={0}>{1}</option>", slotIdList[i].slotId, slotIdList[i].slotNo));
    }
	if(slotId > 0){
		$slot.val(slotId);
	    slotIdChange(0);
	}
}

Ext.onReady(function(){
    loadBlockData();
    loadBlockGrid();
    $("#ponSel,#slotSel").empty();
    initSelect();
});

/*
 * 页面操作方法
 */
function blockGridContextMenu(grid,rowIndex,e){
    var sel = grid.getSelectionModel();
    var n = sel.selections.items.length;
    if(n == 1 || !sel.isSelected(rowIndex)){
        sel.clearSelections();
        sel.selectRow(rowIndex);
    }
    batchAddFlag = sel.selections.items.length > 1;
    var snAddFlag = false;
    if(!batchAddFlag){
        snAddFlag = !!!sel.selections.items[0].data.sn;
    }
    blockMenu.findById("onuBlockAddSn").setDisabled(batchAddFlag || snAddFlag);
    blockMenu.showAt(e.getXY());
}
var blockMenu = new Ext.menu.Menu({
    id:'blockMenu',
    enableScrolling: false,
    items:[{
        id:'onuBlockAddMac',
        text:I18N.onuAuth.mes.addToMac,
        handler : function(){
        	var mode = $("#modeSel").val();
        	if(mode == 4 || mode == 5){
        		window.parent.showMessageDlg("@COMMON.tip@", '@onuAuth.mes.contAddToMac@'); 
        	}else{
            	macAddBtClick(2);
        	}
        }
    },{
        id:'onuBlockAddSn',
        text:I18N.onuAuth.mes.addToSn,
        handler : function(){
            if($("#modeSel").val() == 2){
                window.parent.showMessageDlg("@COMMON.tip@", '@onuAuth.mes.contAddToSn@');                
            }else{
                snAddBtClick(2);
            }
        }
    }]
});
function onBlockMacKeyup(mac){
    $(".blockMacClass" + mac.replace(/([:])/g,"q")).val(mac);
}
function onBlockSnKeyup(sn){
    $(".blockSnClass" + sn).val(sn);
}
function changeMacToMaohao(mac){
    if(mac){
        if(mac.length == 12){
            var newMac = mac.substring(0,2);
            for(var u=1; u<6; u++){
                newMac += ":" + mac.substring((2*u),(2*u+2));
            }
            mac = newMac;
        }
        if(mac.length == 14){
            mac = mac.substring(0,2)+":"+mac.substring(2,7)+":"+mac.substring(7,12)+":"+mac.substring(12,14);
        }
        return mac.replace(/([/\s-.])/g,":").toLocaleUpperCase();
    }else{
        return "";
    }
}
function preventBubble(e) {
    if (e.stopPropagation) {
        e.stopPropagation();           // 火狐阻止冒泡
    } else {
        e.cancelBubble = true;         // IE阻止冒泡
    }
    if(e.preventDefault){
        e.preventDefault();
    }
}

/**
 * 页面效果方法
 */
function tabKeyDown(s){
    var keyCode;
    if(window.event){
        keyCode = window.event.keyCode;
    }else{
        return;
    }
    setTimeout(function(){
        if(keyCode == 13){
            if(addingFlag2){
                snKeyup();
            }else{
                macKeyup();
            }
            if(!$("#okBt").attr("disabled")){
                okBtClick(1);
            }
        }else if(keyCode == 9){
            if(insteadFlag){
                if(addingFlag2){
                    if(s == 4){
                        $("#pwInput").focus();
                    }else if(s == 5){
                        $("#okBt").focus();
                    }else if(s == 7){
                        $("#snInput").focus();
                    }
                }else{
                    if(s == 3){
                        $("#okBt").focus();
                    }else if(s == 7){
                        $("#macInput").focus();
                    }
                }
                if(s == 6){
                    $("#closeBt").focus();
                }
            }else{
                if(s == 1){//onuNo
                    if(addingFlag2){
                        $("#snInput").focus();
                    }else{
                        $("#macInput").focus();
                    }
                }else if(s == 2){//mac
                    $("#actionSel").focus();
                }else if(s == 3 || s == 5){//action,password
                    if(!$("#okBt").attr("disabled")){
                        $("#okBt").focus();
                    }else{
                        $("#closeBt").focus();
                    }
                }else if(s == 4){//sn
                    $("#pwInput").focus();
                }else if(s == 6){//ok
                    $("#closeBt").focus();
                }else if(s == 7){//close
                    $("#onuNoSel").focus();
                }
            }
        }
    }, 70);
    preventBubble(window.event);
}
function initOkBt(){
    /* var s = "<button disabled id=okBt class=BUTTON75 onkeydown='tabKeyDown(6)' type='button' " +
        "onclick='okBtClick(1)'>@COMMON.confirm@</button>&nbsp;&nbsp;" + 
        "<button id=closeBt class=BUTTON75 type='button' onkeydown='tabKeyDown(7)' " +
        "onclick='closeBtClick(1)'>@COMMON.cancel@</button>";
    return s; */
    
    var s = [
        '<button id="okBt" disabled onkeydown="tabKeyDown(6)" onclick="okBtClick(1)" >@COMMON.confirm@</button> ' + 
        '<button id="closeBt" onkeydown="tabKeyDown(7)" onclick="closeBtClick(1)">@COMMON.cancel@</button>'
    ].join('');
    return s;
}
function initOnuTypeSel(s, v){
    var re = "<select id=onuTypeSel><option value='0'>NONE</option>";
    if(s == 0){
        re += initOnuTypeSelOption(v);
    }
    re += "</select>";
    return re;
}
function initOnuTypeSelOption(v){
    var re = "";
    $.each(onuTypes,function(idx,$type){
    	if([255,241,13100].contains($type.typeId)){
    		return;
    	}
        re += String.format("<option value={typeId}>{displayName}</option>", $type);
    });
    return re;
}
function okBtClick(kk){
    var mac = "";
    var sn = "";
    var authType = 1;//1:mac,2:sn
    var authAction = $("#actionSel").val();
    if(!authAction){
        authAction = "";
    }
    if(addingFlag2){
        authType = 2;
        sn = $("#snInput").val();
        if(sn == undefined || sn == null){
            sn = "";
        }
        if(authSnList.indexOf(sn) > -1 && kk != 0){
            if(!modifyingData[4] || insteadFlag){
               /*  window.parent.showMessageDlg("@COMMON.tip@", 
                    String.format(I18N.onuAuth.tip.snUsed, sn, authSnList[authSnList.indexOf(sn) + 1]));
                return false; */
            }else if(!insteadFlag){
                window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.modifyConfirm, function(type) {
                    if (type == 'no') {
                        return;
                    }else{
                        okBtClick(0);
                    }
                });
                return false;
            }
        }
    }else{
        mac = $("#macInput").val();
        if(!mac){
            mac == "";
        }else{
            mac = changeMacToMaohao(mac);
        }
        if(authAction==2 && ponRejectdList.contains( mac )){
        	 window.top.showMessageDlg("@COMMON.tip@", String.format("@onuAuth.tip.macUsedNoWhere@", mac));
        	 return false;
		 }else if(authAction==1 && authMacList.contains(changeMacToMaohao(mac)) && kk != 0){
            if(!modifyingData[4] || insteadFlag){
                window.top.showMessageDlg("@COMMON.tip@", 
                    String.format(I18N.onuAuth.tip.macUsed, mac, authMacList[authMacList.indexOf(changeMacToMaohao(mac)) + 1]));
                return false;
            }else if(!insteadFlag){
                window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.modifyConfirm, function(type) {
                    if (type == 'no') {
                        return;
                    }else{
                        okBtClick(0);
                    }
                });
                return false;
            }
	      }
    }
    var ponIdTmp = modifyingData[3];
    var slotNo = "";
    var ponNo = "";
    var v = modifyingData[0];
    if(v == "" || v == undefined || v == null){
        for(var i=0; i<ponIdList.length; i++){
            if(ponIdList[i].ponId == ponId){
                ponNo = "" + ponIdList[i].ponNo;
            }
        }
        for(var i=0; i<slotIdList.length; i++){
            if(slotIdList[i].slotId == slotId){
                slotNo = "" + slotIdList[i].slotNo;
            }
        }
    }else{
        slotNo = v.split("/")[0];
        ponNo = v.split("/")[1].split(":")[0];
    }
    var onuNo = $("#onuNoSel").val();
    if(onuNo == undefined || onuNo == null || onuNo ==""){
        onuNo = v.split(":")[1];
    }
   
    var onuSnMode = 1;
    var password = $("#pwInput").val();
    if(password == undefined || password == null){
        password = "";
    }
    var onuPreType = $("#onuTypeSel").val() + "";
    if($("#onuTypeSel").css("display") == "none"){
        //onuPreType = "";
    }
    
    //取值结束，开始执行
    if(addingFlag){//添加
        var url = '/epon/onuauth/addOnuAuthRule.tv?authType=' + authType + '&entityId=' + entityId + '&ponId=' + ponIdTmp + '&slotNo=' 
            + slotNo + '&ponNo=' + ponNo + '&onuNo=' + onuNo + '&macAddress=' + mac + '&authAction=' + authAction 
            + '&authSnMode=' + onuSnMode + '&sn=' + sn + '&password=' + password + '&onuPreType=' + onuPreType;
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.tip.cfging, 'ext-mb-waiting');
        Ext.Ajax.timeout = 1800000;
        Ext.Ajax.request({
            url : url,
            success : function(response) {
                if(response.responseText != 'success'){
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.cfgError);
                    closeBtClick(1);
                }else{
                    window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.cfgSuc);
                    // 添加成功后，更新数据。
                    if(addingFlag2){
                        authSnList.push(sn);
                        authSnList.push(slotNo+"/"+ponNo+":"+onuNo);
                    }else{
                        authMacList.push(changeMacToMaohao(mac));
                        authMacList.push(slotNo+"/"+ponNo+":"+onuNo);
                    }
                    okBtSuccess();
                }
            },
            failure : function() {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.cfgFailed);
                closeBtClick(1);
            }
        });
    }else{
        closeBtClick(1);
        return false;
    }
}
function okBtSuccess(){
	reloadLocation();
    var v = modifyingData[0];
    if(v == "" || v == undefined || v == null){
        for(var i=0; i<ponIdList.length; i++){
            if(ponIdList[i].ponId == ponId){
                p = "" + ponIdList[i].ponNo;
            }
        }
        for(var i=0; i<slotIdList.length; i++){
            if(slotIdList[i].slotId == slotId){
                so = "" + slotIdList[i].slotNo;
            }
        }
    }else{
        so = v.split("/")[0];
        p = v.split("/")[1].split(":")[0];
    }
    var onuNo = $("#onuNoSel").val();
    if(onuNo == undefined || onuNo == null || onuNo == ""){
        onuNo = v.split(":")[1];
    }
    if(onuNo == undefined || onuNo == "" || onuNo == null){
        onuNo = modifyingData[0].split(":")[1];
    }
    var loc = so + "/" + p + ":" + onuNo;
    var tmpAction = $("#actionSel").val();
    if(tmpAction == undefined || tmpAction == "" || tmpAction == null){
        tmpAction = modifyingData[2];
    }
    var sel = blockGrid.getSelectionModel().getSelected();
    var preTypeTmp = modifyingData[5];
	var onuTypeSelect = $("#onuTypeSel");
	if(onuTypeSelect && onuTypeSelect.val() && onuTypeSelect.css("display") != "none"){
	    preTypeTmp = $("#onuTypeSel").val();
    }
    if(addingFlag2){//SN
        snData[modifyingIndex][0] = loc;
        snData[modifyingIndex][1] = $("#snInput").val();
        snData[modifyingIndex][2] = $("#pwInput").val();
        snData[modifyingIndex][3] = modifyingData[3];
        snData[modifyingIndex][4] = preTypeTmp;
        if(sel){
            if(sel.data.sn == $("#snInput").val()){
                blockData.splice(blockStore.indexOf(sel), 1);
                blockStore.loadData(blockData);
            }
        }
    }else{//MAC
        macData[modifyingIndex][0] = loc;
        macData[modifyingIndex][1] = changeMacToMaohao($("#macInput").val());
        macData[modifyingIndex][2] = tmpAction;
        macData[modifyingIndex][3] = modifyingData[3];
        macData[modifyingIndex][4] = preTypeTmp;
        if(sel){
            if(sel.data.mac == $("#macInput").val()){
                blockData.splice(blockStore.indexOf(sel), 1);
                blockStore.loadData(blockData);
            }
        }
    }
    closeBtClick(0);
}
function closeBtClick(f){//1:取消，0：OK后还原
    if(f && f == 1){
        f = true;
    }else{
        f = false;
    }
    if(!modifyingData[4] && f){
        var tmpI = macData.length - 1;
        var tmpII = snData.length - 1;
        if(addingFlag2 && modifyingIndex == tmpII){
            snData.splice(tmpII, 1);
        }else if(modifyingIndex == tmpI){
            macData.splice(tmpI, 1);
        }
    }
    insteadFlag = false;
    addingFlag = false;
    batchAddFlag = false;
    modifyingIndex = -1;
    modifyingData = new Array();
    if(ponId != -1){
        $("#snAddBt").show();
        $("#macAddBt").show();
    }
    modeChangeCss();
    macStore.loadData(macData);
    snStore.loadData(snData);
}
//something in grid
function initOnuNoSel(v){
    var p = "";
    var so = "";
    if( !v ){
        for(var i=0; i<ponIdList.length; i++){
            if(ponIdList[i].ponId == ponId){
                p = "" + ponIdList[i].ponNo;
            }
        }
        for(var i=0; i<slotIdList.length; i++){
            if(slotIdList[i].slotId == slotId){
                so = "" + slotIdList[i].slotNo;
            }
        }
    }else{
        so = v.split("/")[0];
        p = v.split("/")[1].split(":")[0];
    }
    var s = so + " / " + p + " : " + "<select id=onuNoSel onkeydown='tabKeyDown(1)' "
    	+ " onchange='onuNoSelChange()'>";
        /* + "onmousedown='preventBubble(window.event || e)' onchange='onuNoSelChange()'>"; */
    var m = $("#actionSel").val();
    if( !m ){
        m = 1;
    }
  //modify by bravin@20140221: block表中的index不是LLID，所以不能使用block表中的index作为默认的llid值
    /*if(v != undefined && v != '' && v != null){
        if(m == 1 && parseInt(v.split(":")[1]) < onuMaxNumInPon + 1){
            s = s + "<option value=" + v.split(":")[1] + ">" + v.split(":")[1] + "</option>";
        }else if(m == 2 && parseInt(v.split(":")[1]) > 128){
            s = s + "<option value=" + v.split(":")[1] + ">" + v.split(":")[1] + "</option>";
        }
    }*/
    var ac = modifyingData[2];
    if((ac == 2 && !addingFlag2) || (!onuNoListFlag && blockNoListFlag)){//拒绝
        for(var k= BLOCK_LIST_START_ID; k < BLOCK_LIST_END_ID; k++){
            if(blockOnuNoList.indexOf(k) == -1){
                s = s + "<option value=" + k + ">" + k + "</option>";
            }
        }
    }else if(onuNoListFlag){//允许
        for(var k=1; k<onuMaxNumInPon+1; k++){
            if(onuNoList.indexOf(k) == -1){
                s = s + "<option value=" + k + ">" + k + "</option>";
            }
        }
    }
    s = s + "</select>";
    return s;
}
function onuNoSelChange(){
    if(addingFlag && !addingFlag2){
        macKeyup();
    }else if(addingFlag && addingFlag2){
        snKeyup();
    }
}
function initMacInput(v){
    var s = "<input id=macInput style='width:140px;text-align:center;' onkeyup='macKeyup()' " +
    " type=text value='" 
    + v + "' title='" + I18N.onuAuth.tit.macFormat + "' " +
    "maxlength=17></input>";
    return s;
}
function macKeyup(){
    var mac = $("#macInput").val();
    if(checkedMac(mac)){
        $("#okBt").attr("disabled", false);
    }else{
        $("#okBt").attr("disabled", true);
        $("#okBt").mouseout();
    }
}
function checkedMac(mac){
    $("#macInput").css("border","1px solid #e3efff");
    var reg = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){5})+$/i;
    var reg1 = /^([0-9a-f]{4})(([.][0-9a-f]{4}){2})+$/i;
    var reg2 = /^([0-9a-f]{12})+$/i;
    if(mac == "" || mac == null || mac == undefined){
        return false;
    }
    if(reg.exec(mac) && mac.length == 17){
        return true;
    }
    if(reg1.exec(mac) && mac.length == 14){
        return true;
    }
    if(reg2.exec(mac) && mac.length == 12){
        return true;
    }
    $("#macInput").css("border","1px solid #ff0000");
    return false;
}
function initActionSel(v){
    var s = "<select id=actionSel onchange='actionChange()' " +
    "onkeydown='tabKeyDown(3)' style='width:70px;'>";
    if(v == 2){
        if(blockNoListFlag){
            s = s + "<option value=2 style='color:red;'>@onuAuth.reject@</option>";
        }
        if(onuNoListFlag){
            s = s + "<option value=1 style='color:green;'>@onuAuth.permit@</option>";
        }
    }else{
        if(onuNoListFlag){
            s = s + "<option value=1 style='color:green;'>@onuAuth.permit@</option>";
        }
        if(blockNoListFlag){
            s = s + "<option value=2 style='color:red;'>@onuAuth.reject@</option>";
        }
    }
    s = s + "</select>";
    return s;
}
function actionChange(){
    var ac = $("#actionSel").val();
    var onuSel = Zeta$('onuNoSel');
    var $onuSel = $("#onuNoSel");
    var tpyeSel = $("#onuTypeSel");
    onuSel.options.length = 0;
    var v = modifyingData[0];
    if(ponId == -1){
        var slotNum = parseInt(v.split("/")[0]);
        var ponNum = parseInt(v.split(":")[0].split("/")[1]);
        onuMaxNumInPon = onuMaxNumList[slotNum][ponNum];
    }
    if(v != "" && v != undefined && v != null && ((ac == 1 && v < onuMaxNumInPon+1) || (ac == 2 && v > 128))){
        v = v.split(":")[1];
        $onuSel.append(String.format("<option value={0}>{0}</option>", v));
    }
    if(ac == 1){//允许
        tpyeSel.show();
        //$onuSel.show();
        for(var k=1; k<onuMaxNumInPon+1; k++){
            if(onuNoList.indexOf(k) == -1){
                $onuSel.append(String.format("<option value={0}>{0}</option>", k));
            }
        }
        $onuSel.removeAttr('disabled');
    }else if(ac == 2){//拒绝
        tpyeSel.hide();
        //$onuSel.hide();
        //@EMS-10094 黑名单从32个增加为64个
         for(var k=BLOCK_LIST_START_ID; k<BLOCK_LIST_END_ID; k++){
            if(onuNoList.indexOf(k) == -1 && blockOnuNoList.indexOf(k) == -1){
                $onuSel.append(String.format("<option value={0}>{0}</option>", k));
            }
        } 
         $onuSel.attr({disabled:'disabled'})
    }
    onuNoSelChange();
}
function initSnInput(v){
    var s = "<input id=snInput style='width:180px;text-align:center;' onkeyup='snKeyup()' type=text value='" + 
    		v + "' title='@onuAuth.tit.snFormat@'  maxlength=24></input>";
    return s;
}
function snKeyup(){
    var sn = $("#snInput").val();
    var pw = $("#pwInput").val();
    if(checkedSn(sn) && checkedPw(pw)){
        $("#okBt").attr("disabled", false);
    }else{
        $("#okBt").attr("disabled", true);
        $("#okBt").mouseout();
    }
}
function checkedSn(sn){
    var reg = /^([0-9a-z])+$/ig;
    $("#snInput").css("border","1px solid #e3efff");
    if(sn == null || sn == "" || sn == undefined){
        return false;
    }else{
        if(sn.length < 25 && reg.exec(sn)){
            return true;
        }
    }
    $("#snInput").css("border","1px solid #ff0000");
    return false;
}
function initPasswordInput(v){
    var s = "<input id=pwInput style='width:110px;text-align:center;' onkeyup='snKeyup()' " +
    " type=text value='"+ v + "' title='@onuAuth.tit.pwFormat@' maxlength=12></input>";
    return s;
}
function checkedPw(pw){
    var reg = /^([0-9a-z])+$/ig;
    $("#pwInput").css("border","1px solid #e3efff");
    if(pw == null || pw == "" || pw == undefined){
    	//通常情况下密码可以不输入，但是sn密码认证的时候，密码不能为空;
    	var mode = parseInt($("#modeSel").val(), 10);
    	if(mode === 5){
    		return false;	
    	}
        return true;
    }else{
        if(pw.length < 13 && reg.exec(pw)){
            return true;
        }
    }
    $("#pwInput").css("border","1px solid #ff0000");
    return false;
}
//something in grid at batch adding
//p: [ponId,  mac,location,  mac,location,  mac,location, ...]
//batchOnuNumList: [[{ponId, ponNum, slotNum}, onuNoList[], blockNoList[] ],[],[]]
function initBatchLoc(p){
    var re = "";
    var thisNum = new Array();
    var thiMax = 0;
    var thisFlag = -1;
    for(var j=0; j<batchOnuNumList.length; j++){
        var tmpTmp = batchOnuNumList[j][0];
        if(p[0] == tmpTmp.ponId){
            thiMax = onuMaxNumList[tmpTmp.slotNum][tmpTmp.ponNum];
            thisFlag = j;
            break;
        }
    }
    for(var i=1; i<thiMax+1; i++){
        if(batchOnuNumList[thisFlag][1].indexOf(i) == -1){
            thisNum.push(i);
        }
    }
    for(var x=1; 2 * x < p.length; x++){
        if(x > 1){
            re += "<br>";
        }
        re += p[2 * x].split(":")[0] + String.format(":<span class=batchLoc{0}>{1}</span>", p[0], thisNum[x - 1]);
    }
    return re;
}
function batchActionChange(ponTmpId){
    var thisNum = new Array();
    var thiMax = 0;
    var thisFlag = -1;
    for(var j=0; j<batchOnuNumList.length; j++){
        var tmpTmp = batchOnuNumList[j][0];
        if(ponTmpId == tmpTmp.ponId){
            thiMax = onuMaxNumList[tmpTmp.slotNum][tmpTmp.ponNum];
            thisFlag = j;
            break;
        }
    }
    if($("#batchAction" + ponTmpId).val() == 1){
        $("#batchPreType" + ponTmpId).show();
        for(var i=1; i<thiMax; i++){
            if(batchOnuNumList[thisFlag][1].indexOf(i) == -1){
                thisNum.push(i);
            }
        }
    }else{
        $("#batchPreType" + ponTmpId).hide();
        for(var i=BLOCK_LIST_START_ID; i<BLOCK_END_START_ID; i++){
            if(batchOnuNumList[thisFlag][2].indexOf(i) == -1){
                thisNum.push(i);
            }
        }
    }
    var locSpan = $(".batchLoc" + ponTmpId);
    for(var t=0; t<locSpan.length; t++){
        $(locSpan[t]).text(thisNum[t]);
    }
}
function initBatchMac(p){
    var re = "";
    for(var x=1; 2 * x < p.length; x++){
        if(x > 1){
            re += "<br>";
        }
        re += String.format("<span class=batchMac{0}>{1}</span>", p[0], p[2 * x - 1]);
    }
    return re;
}
function initBatchAction(p){
    var ponTmpId = p[0];
    var thisActionFlag1 = false;
    var thisActionFlag2 = false;
    for(var x=0; x<batchOnuNumList.length; x++){
        var pp = batchOnuNumList[x];
        if(pp[0].ponId == ponTmpId){
            thisActionFlag1 = onuMaxNumList[pp[0].slotNum][pp[0].ponNum] >= pp[1].length + parseInt((p.length - 1) / 2);
            thisActionFlag2 = 32 >= pp[2].length + parseInt((p.length - 1) / 2);
            break;
        }
    }
    var re = "";
    if(!thisActionFlag1 && !thisActionFlag2){
        re = I18N.onuAuth.mes.noOnuNoWith2BR;
    }else{
        re += String.format("<select id=batchAction{0} onchange='batchActionChange({0})' onmousedown='preventBubble(window.event || e)'>",
                ponTmpId);
        if(thisActionFlag1){
            re += "<option value=1 style='color:green;'>@onuAuth.permit@</option>";
        }
        if(thisActionFlag2){
            re += "<option value=2 style='color:red;'>@onuAuth.reject@</option>";
        }
        re += "</select>";
    }
    return re;
}
function initBatchPreType(p){
    var re = "<select id=batchPreType" + p[0] + " onmousedown='preventBubble(window.event || e)' ><option value='0'>NONE</option>";
    $.each(onuTypes,function(idx,$type){
    	if([255,241,13100].contains($type.typeId)){
    		return;
    	}
        re += String.format("<option value={typeId}>{displayName}</option>", $type);
    });
    re += "</select>";
    return re;
}
function initBatchImg(ponIdTmp){
    var re = "";
    re += String.format("<img src='/epon/image/addThis.png' title='@onuAuth.tit.addAllPonAuth@' onclick='batchAddOne()' />" +
            "<img src='/epon/image/addClose.png' " +
            "style='margin-left:10px;' title='@onuAuth.tit.addNotPonAuth@' onclick='batchAddClose()' />", ponIdTmp);
    if(batchOnuNumList.length > 1){
        re += String.format("<img src='/epon/image/addAll.png' style='margin-left:15px;' " +
                "title='@onuAuth.tit.addAllAuth@' onclick='batchAddAll()' />", ponIdTmp);
    }
    return String.format("<div style='margin-top:0px;'>{0}</div>", re);
}
function batchAddOne(){
    var sel = macGrid.getSelectionModel().getSelected();
    if(sel.data.batchDataFlag){
        var list = new Array();
        var tmpPonId = sel.data.ponId;
        var $onuNo = $(".batchLoc" + tmpPonId);
        var $mac = $(".batchMac" + tmpPonId);
        var tmpSlotNo = false;
        var tmpPonNo = false;
        for(var i=0; i<batchOnuNumList.length; i++){
            var tmpTmp = batchOnuNumList[i][0];
            if(tmpTmp.ponId == tmpPonId){
                tmpSlotNo = tmpTmp.slotNum;
                tmpPonNo = tmpTmp.ponNum;
                break;
            }
        }
        var tmpAuthAction = $("#batchAction" + tmpPonId).val();
        var tmpType = "0";
        if(tmpAuthAction == 1){
            tmpType = $("#batchPreType" + tmpPonId).val() || tmpType;
        }
        if(tmpPonNo && $onuNo.length == $mac.length){
            var addedMac = new Array();
            var tmpMacList = new Array();
            for(var x=0; x<$onuNo.length; x++){
                var onuNoTmp = $($onuNo[x]).text();
                var tmpMac = $($mac[x]).text();
                var tmpMacM = changeMacToMaohao(tmpMac);
                if(authMacList.indexOf(tmpMacM) > -1){
                    addedMac.push(tmpMac);
                }else if(tmpMacList.indexOf(tmpMacM) == -1){
                    tmpMacList.push(tmpMacM);
                    list.push({ponId: tmpPonId, slotNo: tmpSlotNo, ponNo: tmpPonNo, onuNo: onuNoTmp, macAddress: tmpMac,
                                authAction: tmpAuthAction, onuPreType: tmpType});
                }
            }
            var re = {flag: false};
            if(addedMac.length > 0){
                var str = addedMac.join("、");
                if(list.length > 0){
                    window.parent.showConfirmDlg("@COMMON.tip@", String.format(I18N.onuAuth.mes.macBatchUsed, str), function(type) {
                        if (type == 'no') {
                            return false;
                        }else{
                            window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.mes.macAdding, 'ext-mb-waiting');
                            batchAddMacMgmt(list, re);
                        }
                    });
                }else{
                    window.parent.showMessageDlg("@COMMON.tip@", String.format(I18N.onuAuth.tip.macUsedNoWhere, str));
                    return false;
                }
            }else{
                window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.mes.macAdding, 'ext-mb-waiting');
                batchAddMacMgmt(list, re);
            }
        }
    }
}
function batchAddAll(){
    var list = new Array();
    var addedMac = new Array();
    var tmpMacList = new Array();
    macStore.each(function(record){
        if(record.data.batchDataFlag){
            var tmpPonId = record.data.ponId;
            var $onuNo = $(".batchLoc" + tmpPonId);
            var $mac = $(".batchMac" + tmpPonId);
            var tmpSlotNo = false;
            var tmpPonNo = false;
            for(var i=0; i<batchOnuNumList.length; i++){
                var tmpTmp = batchOnuNumList[i][0];
                if(tmpTmp.ponId == tmpPonId){
                    tmpSlotNo = tmpTmp.slotNum;
                    tmpPonNo = tmpTmp.ponNum;
                    break;
                }
            }
            var tmpAuthAction = $("#batchAction" + tmpPonId).val();
            var tmpType = "none";
            if(tmpAuthAction == 1){
                tmpType = $("#batchPreType" + tmpPonId).val();
            }
            if(tmpPonNo && $onuNo.length == $mac.length){
                for(var x=0; x<$onuNo.length; x++){
                    var onuNoTmp = $($onuNo[x]).text();
                    var tmpMac = $($mac[x]).text();
                    var tmpMacM = changeMacToMaohao(tmpMac);
                    if(authMacList.indexOf(tmpMacM) > -1){
                        addedMac.push(tmpMac);
                    }else if(tmpMacList.indexOf(tmpMacM) == -1){
                        tmpMacList.push(tmpMacM);
                        list.push({ponId: tmpPonId, slotNo: tmpSlotNo, ponNo: tmpPonNo, onuNo: onuNoTmp, macAddress: tmpMac,
                                    authAction: tmpAuthAction, onuPreType: tmpType});
                    }
                }
            }
        }
    });
    var re = {flag: false};
    if(addedMac.length > 0){
        var str = addedMac.join("、");
        if(list.length > 0){
            window.parent.showConfirmDlg("@COMMON.tip@", String.format(I18N.onuAuth.mes.macBatchUsed, str), function(type) {
                if (type == 'no') {
                    return false;
                }else{
                    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.mes.macAdding, 'ext-mb-waiting');
                    batchAddMacMgmt(list, re);
                }
            });
        }else{
            window.parent.showMessageDlg("@COMMON.tip@", String.format(I18N.onuAuth.tip.macUsedNoWhere, str));
            return false;
        }
    }else{
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.mes.macAdding, 'ext-mb-waiting');
        batchAddMacMgmt(list, re);
    }
}
//list: [{ponId, slotNo, ponNo, onuNo, macAddress, authAction, onuPreType}, {}, {}...]
//re: [{flag : false/true}, {mac: responseText}, {mac: responseText}, {mac: responseText} ,{}...]
function batchAddMacMgmt(list, re){
    if(list.length > 0){
        batchAddMac(list, re);
    }else if(re.flag){
        var suc = new Array();
        var str = "";
        for(var x in re){
            if(x != "flag"){
                if(re[x] == "success"){
                    suc.push(x);
                }else{
                    str += "<nobr>" + String.format(I18N.onuAuth.mes.macBatchAddFailed, x) + re[x] + "</nobr><br>";
                }
            }
        }
        var tmpS = "";
        tmpS += suc.join("、");
        if(tmpS){
            tmpS = String.format(I18N.onuAuth.mes.macBatchAddSuc, tmpS);
        }
        tmpS += str;
        window.parent.showMessageDlg("@COMMON.tip@", tmpS);
        ponIdChange();
    }else{
        window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.mes.unknowError);
        reloadLocation();
    }
}
function batchAddMac(list, re){
    var url = '/epon/onuauth/addOnuAuthRule.tv?authType=1&entityId=' + entityId + '&r=' + Math.random();
    Ext.Ajax.request({
        url : url,
        params: list[0],
        success : function(response) {
            re.flag = true;
            re[list[0].macAddress] = response.responseText;
            if(response.responseText == 'success'){
                // 添加成功后，更新数据。
                authMacList.push(changeMacToMaohao(list[0].macAddress));
                authMacList.push(list[0].slotNo + "/" + list[0].ponNo + ":" + list[0].onuNo);
                list.splice(0, 1);
                batchAddMacMgmt(list, re);
            }else{
                list.splice(0, 1);
                batchAddMacMgmt(list, re);
            }
        },
        failure : function(response) {
            re.flag = true;
            re[list[0].macAddress] = response.responseText;
            list.splice(0, 1);
            batchAddMacMgmt(list, re);
        }
    });
}
function batchAddClose(){
    var sel = macGrid.getSelectionModel().getSelected();
    for(var j=0; j<batchOnuNumList.length; j++){
        if(sel.data.ponId == batchOnuNumList[j][0].ponId){
            batchOnuNumList.splice(j, 1);
            break;
        }
    }
    macStore.remove(sel);
    if(batchOnuNumList.length == 0){
        closeBtClick(1);
    }
}
//end of batch add
//batch delete
function gridContext(grid,rowIndex,e){
	 preventBubble(e);
    if(!grid.getSelectionModel().isSelected(rowIndex)){
        return false;
    }
    var sel = grid.getSelectionModel().selections.items;
    if(sel.length > 1){
        var tmpMenu = new Ext.menu.Menu({
            id:'tmpMenu' + Math.random(),
            enableScrolling: false,
            items:[{
                id:'batchDeleteRule' + Math.random(),
                text: I18N.onuAuth.tit.delAuth,
                handler : function(){
                    batchDelMenuHandler(sel);
                }
            }]
        });
        tmpMenu.showAt(e.getXY());
    }
}
function batchDelMenuHandler(sel){
    var list = new Array();
    var str = "";
    var s = 0;
    for(var x=0; x<sel.length; x++){
        var tmpD = sel[x].data;
        if(s == 0){
            s = tmpD.mac ? 1 : tmpD.sn ? 2 : 0;
        }
        list.push(tmpD);
    }
    window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.delConfirm0, function(type) {
        if (type == 'no') {return;}
        if(addingFlag){
            closeBtClick(1);
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.tip.deleting, 'ext-mb-waiting');
        $("#onuNoTip").hide();
        var re = {flag: false};
        batchDelRuleMgmt(list, re, s);
    });
}
function batchDelRuleMgmt(list, re, s){
    if(list.length > 0){
        batchDelRule(list, re, s);
    }else if(re.flag){
        var str = "";
        var suc = new Array();
        for(var x in re){
            if(x != 'flag'){
                if(re[x] == "success"){
                    suc.push(x);
                }else{
                    continue;
                    //str += "<nobr>" + String.format(I18N.onuAuth.tip.deleteFailed, x) + "</nobr><br>";
                }
            }
        }
        var tmpS = "";
        if(suc.length > 0){
            tmpS = I18N.onuAuth.tip.deleteSuc;
        }
        if("" != str){
        	tmpS = I18N.onuAuth.tip.deleteFailed;
        }
        window.parent.showMessageDlg("@COMMON.tip@", tmpS);
        ponIdChange();
    }else{
        window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.mes.unknowError);
        reloadLocation();;
    }
}
function batchDelRule(list, re, s){
    var authTypeDel = s;
    var ponIdDel = list[0].ponId;
    var slotNoDel = list[0].location.split("/")[0];
    var ponNoDel = list[0].location.split("/")[1].split(":")[0];
    var onuNoDel = list[0].location.split(":")[1];
    var url = '/epon/onuauth/deleteOnuAuthRule.tv?authType=' + authTypeDel + '&entityId=' + entityId + '&ponId=' + ponIdDel 
        + '&slotNo=' + slotNoDel + '&ponNo=' + ponNoDel + '&onuNo=' + onuNoDel;
    Ext.Ajax.request({
        url : url,
        success : function(response) {
            re.flag = true;
            if(s == 1){
                re[list[0].mac] = response.responseText;
            }else if(s == 2){
                re[list[0].sn] = response.responseText;
            }
            if(response.responseText != 'success'){
                list.splice(0, 1);
                batchDelRuleMgmt(list, re, s);
            }else{
                // 删除成功。
                if(s == 1){
                    authMacList.splice(authMacList.indexOf(changeMacToMaohao(list[0].mac)), 2);
                }else if(s ==2){
                    authSnList.splice(authSnList.indexOf(list[0].sn), 2);
                }
                list.splice(0, 1);
                batchDelRuleMgmt(list, re, s);
            }
        },
        failure : function(response) {
            re.flag = true;
            if(s == 1){
                re[list[0].mac] = response.responseText;
            }else if(s == 2){
                re[list[0].sn] = response.responseText;
            }
            list.splice(0, 1);
            batchDelRuleMgmt(list, re, s);
        }
    });
}
//end of batch delete
function removeMask(){
    $(".loadingText").text(I18N.PERF.add.finish + "!").css("color","green");
    $("#loadingMask").fadeOut(1000);
    $("#loadingMask").css("zIndex",1);
}
function showMask(){
    $(".loadingText").text(I18N.COMMON.loading).css("color","white");
    $("#loadingMask").css("filter","alpha(opacity=50)");
    $("#loadingMask").css("zIndex",100000);
    $("#loadingMask").show();
}
function tipClick(el){
    $(el).hide();
}
function blockBtClick(s){
    var tmpW = window.parent.getWindow("onuAuthen");
    if(s == 1 && !showBlockFlag){
        if(showBlockFirst){
            showBlockFirst = false;
            refreshBlock(1);
            return;
        }else{
            $("#hideBlockBt").show();
            $("#refreshBlockBt").show();
            $("#blockDiv").show();
        }
        loadBlockData();
        $("#macFieldset").height(240);
        $("#snFieldset").height(240);
        macSnHeight = 100;
        loadMacGrid();
        loadSnGrid();
        tmpW.setHeight(350);
        tmpW.body.setHeight(585);
        //$("#bodyFieldset").height(540);
        tmpW.setPosition(tmpW.x, 0);
        //$("#onuNoTip").css("top", 533);
        showBlockFlag = true;
    }else if(s == 2 && showBlockFlag){
        $("#hideBlockBt").hide();
        $("#refreshBlockBt").hide();
        $("#blockDiv").hide();
        $("#blockTip").hide();
        $("#macFieldset").height(332);
        $("#snFieldset").height(332);
        macSnHeight = 190;
        loadSnGrid();
        loadMacGrid();
        tmpW.setHeight(300);
        tmpW.body.setHeight(485);
        //$("#bodyFieldset").height(440);
        //$("#onuNoTip").css("top", 433);
        showBlockFlag = false;
    }else{
        return false;
    }
    macStore.loadData(macData);
    snStore.loadData(snData);
}
function modeChange(){
    var modeTip = $("#modeTip");
    modeTip.hide();
    var modeSel = $("#modeSel");
    var m = modeSel.val();
    var a = $("#autoAuthSel").val();
    var tmpMacData = new Array();
    window.parent.showConfirmDlg("@COMMON.tip@", I18N.onuAuth.tip.changeModeConfirm, function(type) {
        if (type == 'no') {
            modeSel.val( CURRENT_AUTH_MODE );
            return;
        }
        var slotNo = "";
        var ponNo = "";
        for(var i=0; i<ponIdList.length; i++){
            if(ponIdList[i].ponId == ponId){
                ponNo = ponIdList[i].ponNo;
                break;
            }
        }
        for(var i=0; i<slotIdList.length; i++){
            if(slotIdList[i].slotId == slotId){
                slotNo = slotIdList[i].slotNo;
                break;
            }
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.onuAuth.tip.changingMode, 'ext-mb-waiting');
        $.ajax({
            url : "/epon/onuauth/modifyOnuAuthMode.tv",
            data: 'authMode=' + m + '&entityId=' + entityId + '&slotNo=' + slotNo + '&ponNo=' + ponNo,
            success : function() {
            	window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.changeModeSuc);
                window.location.reload();
            },
            error : function() {
                window.parent.showMessageDlg("@COMMON.tip@", I18N.onuAuth.tip.changeModeFailed);
                modeSel.val( CURRENT_AUTH_MODE );
            }
        });
    });
}
function modeChangeCss(){
    var m = $("#modeSel").val();
    if(m == 1){
		$("#snAddBt").hide();    	
    }
    else if(m == 2){//MAC
        macWidth = 850;
        $("#macDiv").show();
        $("#snDiv").hide();
    }else {//if(m == 3 || m == 4 || m=){
        $("#macDiv").show();
        $("#snDiv").show();
        if(addingFlag && !addingFlag2){//添加MAC
            macWidth = 665;
            snWidth = 175;
        }else if(addingFlag && addingFlag2){//添加SN
            macWidth = 175;
            snWidth = 665;
        }else{//正常
            macWidth = 400;
            snWidth = 445;
        }
    }
    $("#macFieldset").width(macWidth + 5);
    $("#snFieldset").width(snWidth + 5);
    loadMacGrid();
    loadSnGrid();
}
function macInsteadBtClick(){
    insteadFlag = true;
    macAddBtClick(1);   
}
function macAddBtClick(s){//s==0,添加按钮的点击,s==1,修改,s==2,block中加入
	$("#macAddBt").attr("disabled",true);
    if(addingFlag){
        var tmpU = macStore.indexOf(macGrid.getSelectionModel().getSelected());
        var tmpF = false;
        if(insteadFlag && s == 1){
            tmpF = true;
        }
        closeBtClick(1);
        macGrid.getSelectionModel().selectRow(tmpU, true);
        if(tmpF){
            insteadFlag = true;
        }
    }
    if(s == 2 && batchAddFlag){
        batchAddMacAuthFromBlock();
        return;
    }
    var tmpPonId = ponId;
    if(s == 2 && tmpPonId == -1){
        var dataTmp = blockGrid.getSelectionModel().getSelected().data;
        tmpPonId = dataTmp.ponId;
        var slotNum = parseInt(dataTmp.location.split("/")[0]);
        var ponNum = parseInt(dataTmp.location.split(":")[0].split("/")[1]);
        onuMaxNumInPon = onuMaxNumList[slotNum][ponNum];
    }
    var onuNoStore = new Ext.data.Store({
        url : '/onu/onuauth/loadOnuNoList.tv?ponId=' + tmpPonId,
        reader : new Ext.data.ArrayReader(
            {id : 0}, 
            Ext.data.Record.create([ {name : 'onuNumList'}, {name : 'blockOnuNumList'} ])
        )
    });
    onuNoStore.load({
        callback : function(records, options, success) {
            onuNoList = records[0].json;//配置过的onuNo的列表(1-onuMaxNumInPon)
            blockOnuNoList = records[1].json;//配置过的onuNo的block列表(129-160)
            var tmpList = new Array();
            for(var i=0; i<onuNoList.length; i++){
                if(onuNoList[i] > onuMaxNumInPon){
                    tmpList.push(onuNoList[i]);
                }
            }
            var tmpListL = tmpList.length;
            if(tmpListL > 0){
                for(var i=0; i<tmpListL; i++){
                    blockOnuNoList.push(tmpList[i]);
                    onuNoList.splice(onuNoList.indexOf(tmpList[i]),1);
                }
            }
            onuNoListFlag = false;
            blockNoListFlag = false;
            for(var t=1; t<onuMaxNumInPon+1; t++){
                if(onuNoList.indexOf(t) == -1){
                    onuNoListFlag = true;
                }
            }
            for(var t=BLOCK_LIST_START_ID; t<BLOCK_LIST_END_ID; t++){
                if(blockOnuNoList.indexOf(t) == -1 && onuNoList.indexOf(t) == -1){
                    blockNoListFlag = true;
                }
            }
            if(!onuNoListFlag && !blockNoListFlag){//无剩余
                $("#onuNoTip").show().text("@onuAuth.tip.hasNoOnu1@");
                if(s == 0){
                    return false;
                }
            }else if(!onuNoListFlag){
                $("#onuNoTip").show().text("@onuAuth.tip.hasNoOnu2@");
            }else if(!blockNoListFlag){
                $("#onuNoTip").show().text("@onuAuth.tip.hasNoOnu3@");
            }
            modifyingIndex = macData.length;
            $("#macAddBt").hide();
            if(ponId != -1){
                $("#snAddBt").show();
            }else{
                $("#snAddBt").hide();
            }
            addingFlag = true;
            addingFlag2 = false;
            var sel = null;
            if(s == 0){
                modifyingIndex = macData.length;
                modifyingData = ['','','', ponId, false, 0];
                macData.push([modifyingData[0], modifyingData[1], modifyingData[2], modifyingData[3], modifyingData[5],true]);
            }else if(s == 2){
                modifyingIndex = macData.length;
                sel = blockGrid.getSelectionModel().getSelected();
                if(sel){
                    modifyingData = [sel.data.location, sel.data.mac, "1", sel.data.ponId, false, sel.data.preType];
            		var $mac = changeMacToMaohao(modifyingData[1]);
            		if(authMacList.contains( $mac )){
                        window.top.showMessageDlg("@COMMON.tip@",String.format("@onuAuth.tip.macUsedNoWhere@", modifyingData[1]));
                        addingFlag = false;
                        modifyingData = [];
                        $("#macAddBt").show();
                        return false;
                    } 
                    macData.push([modifyingData[0], modifyingData[1], modifyingData[2], modifyingData[3], modifyingData[5],true]);
                }
            }
            modeChangeCss();
            macStore.loadData(macData);
        }
    });
}
function snInsteadBtClick(){
    insteadFlag = true;
    snAddBtClick(1);
}
function snAddBtClick(s){//s==0,添加按钮点击,s==1,修改,s==2:block中加入
	$("#snAddBt").attr("disabled",true);
    if(addingFlag){
        var tmpU = snStore.indexOf(snGrid.getSelectionModel().getSelected());
        var tmpF = false;
        if(insteadFlag && s == 1){
            tmpF = true;
        }
        closeBtClick(1);
        snGrid.getSelectionModel().selectRow(tmpU, true);
        if(tmpF){
            insteadFlag = true;
        }
    }
    var tmpPonId = ponId;
    if(s == 2 && tmpPonId == -1){
        var tmpLoc = blockGrid.getSelectionModel().getSelected().data.location;
        var slotNo = tmpLoc.split("/")[0];
        var ponNo = tmpLoc.split("/")[1].split(":")[0];
        for(var k=0; k<ponIdList.length; k++){
            if(ponIdList[k].slotNo == slotNo && ponNo == ponIdList[k].ponNo){
                tmpPonId = ponIdList[k].ponId;
            }
        }
    }
    var onuNoStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : '/onu/onuauth/loadOnuNoList.tv?ponId=' + tmpPonId
        }),
        reader : new Ext.data.ArrayReader(
            {id : 0}, 
            Ext.data.Record.create([ {name : 'onuNumList'}, {name : 'blockOnuNumList'} ])
        )
    });
    onuNoStore.load({
        callback : function(records, options, success) {
            onuNoList = records[0].json;//配置过的onuNo的列表(1-onuMaxNumInPon)
            blockOnuNoList = records[1].json;//配置过的onuNo的block列表(129-160)
            onuNoListFlag = false;
            blockNoListFlag = false;
            for(var t=1; t<onuMaxNumInPon+1; t++){
                if(onuNoList.indexOf(t) == -1){
                    onuNoListFlag = true;
                }
            }
            for(var t=BLOCK_LIST_START_ID; t<BLOCK_LIST_END_ID; t++){
                if(blockOnuNoList.indexOf(t) == -1 && onuNoList.indexOf(t) == -1){
                    blockNoListFlag = true;
                }
            }
            if(!onuNoListFlag && !blockNoListFlag){//无剩余
                $("#onuNoTip").show().text(I18N.onuAuth.tip.hasNoOnu1);
                if(s == 0){
                    return false;
                }
            }else if(!onuNoListFlag){
                $("#onuNoTip").show().text(I18N.onuAuth.tip.hasNoOnu2);
                if(s == 0){
                    return false;
                }
            }else if(!blockNoListFlag){
                $("#onuNoTip").show().text(I18N.onuAuth.tip.hasNoOnu3);
            }
            modifyingIndex = snData.length;
            $("#snAddBt").hide();
            if(ponId != -1){
                $("#macAddBt").show();
            }else {
                $("#macAddBt").hide();
            }
            addingFlag = true;
            addingFlag2 = true;
            var sel = null;
            if(s == 0){
                modifyingIndex = snData.length;
                modifyingData = ['','','', ponId, false, 0];
                snData.push([modifyingData[0], modifyingData[1], modifyingData[2], modifyingData[3], modifyingData[5]]);
            }else if(s == 1){
                if(snGrid){
                    sel = snGrid.getSelectionModel().getSelected();
                    if(sel){
                        modifyingIndex = snStore.indexOf(sel);
                    }
                }
                modifyingData = [sel.data.location, sel.data.sn, sel.data.password, sel.data.ponId, true, sel.data.preType];
            }else if(s == 2){
                modifyingIndex = snData.length;
                sel = blockGrid.getSelectionModel().getSelected();
                if(sel){
                    modifyingData = [sel.data.location, sel.data.sn, sel.data.password, sel.data.ponId, false, sel.data.preType];
                    if(authSnList.indexOf(modifyingData[1]) > -1){
                        window.parent.showMessageDlg("@COMMON.tip@", String.format(I18N.onuAuth.tip.snUsedNoWhere, modifyingData[1]));
                        addingFlag = false;
                        $("#snAddBt").show();
                        return false;
                    }
                    snData.push([modifyingData[0], modifyingData[1], modifyingData[2], modifyingData[3], modifyingData[5]]);
                }
            }
            modeChangeCss();
            snStore.loadData(snData);
        }
    });
}
function modeTipImgClick(){
    $("#modeTip").hide().text(modeTipStr[0]).slideDown(1500);
}


//窗口change方法
function changeWindowWidth(old, now, time){
    var a = parseInt(time / 100);
    var ww = (now - old) / a;
    setWindowWidth(a, ww, now);
}
function setWindowWidth(a, w, hw){
    if(a > 0){
        a--;
        setTimeout(function(){
            window.parent.getWindow("onuAuthen").setWidth(hw - a * w);
            window.parent.getWindow("onuAuthen").body.setWidth("100%");
            setWindowWidth(a, w, hw);
        }, 10);
    }
}

/**
 * 从block表中批量添加MAC认证
 */
function batchAddMacAuthFromBlock(){
    var sel = blockGrid.getSelectionModel().selections.items;
    var tmpPonList = new Array();
    var tmpTmp = new Array();
    for(var x=0; x<sel.length; x++){
        var tmpSelX = sel[x].data.ponId;
        var lo = sel[x].data.location;
        var slotNum = parseInt(lo.split("/")[0]);
        var ponNum = parseInt(lo.split(":")[0].split("/")[1]);
        if(tmpTmp.indexOf(tmpSelX) == -1){
            tmpPonList.push({ponId: tmpSelX, ponNum: ponNum, slotNum: slotNum});
            tmpTmp.push(tmpSelX);
        }
    }
    var re = new Array();
    getOnuNoFromPonList(tmpPonList, re);
}
function getOnuNoFromPonList(ponList, re){
    if(ponList.length > 0){
        getOnuNo(ponList, re);
    }else{
        batchOnuNumList = re;//[[{ponId, ponNum, slotNum}, onuNoList[], blockNoList[] ],[],[]]
        couldAddBlock();
    }
}
function getOnuNo(ponList, re){
    var onuNoStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : '/onu/onuauth/loadOnuNoList.tv?ponId=' + ponList[0].ponId
        }),
        reader : new Ext.data.ArrayReader(
            {id : 0}, 
            Ext.data.Record.create([ {name : 'onuNumList'}, {name : 'blockOnuNumList'} ])
        )
    });
    onuNoStore.load({
        callback : function(records, options, success) {
            var onuNums = records[0].json;//配置过的onuNo的列表(1-onuMaxNumInPon)
            var blockNums = records[1].json;//配置过的onuNo的block列表(129-160)
            for(var i=1; i<onuNums.length; i++){
                if(onuNums[i] > onuMaxNumList[ponList[0].slotNum][ponList[0].ponNum] + 1){
                    blockNums.push(onuNums[i]);
                    onuNums.splice(i, 1);
                    i--;
                }
            }
            re.push([ponList[0], onuNums, blockNums]);
            ponList.splice(0, 1);
            getOnuNoFromPonList(ponList, re);
        }
    });
}
function couldAddBlock(){
    if(addingFlag){
        closeBtClick(1);
    }
    var sel = blockGrid.getSelectionModel().selections.items;
    if(sel.length){
        batchAddFlag = true;
        addingFlag = true;
        addingFlag2 = false;
        var tmpList = new Array();
        var ponList = new Array();
        for(var x=0, j=0; x<sel.length; x++){
            var tmpP = sel[x].data.ponId;
            var tmpIndex = ponList.indexOf(tmpP);
            if(tmpIndex == -1){
                ponList[j] = tmpP;
                tmpList[j] = new Array();
                tmpList[j].push(tmpP);
                tmpList[j].push(sel[x].data.mac);
                tmpList[j].push(sel[x].data.location);
                j++;
            }else{
                tmpList[tmpIndex].push(sel[x].data.mac);
                tmpList[tmpIndex].push(sel[x].data.location);
            }
        }
        var recordList = new Array();
        for(var t=0; t<tmpList.length; t++){
            var record = new Ext.data.Record();
            var ttt = tmpList[t];
            record.data = {location: initBatchLoc(ttt), mac: initBatchMac(ttt), action: initBatchAction(ttt),
                    preType: initBatchPreType(ttt), ponId: ttt[0], batchDataFlag: true};
            recordList.push(record);
        }
        $("#macAddBt").hide();
        modeChangeCss();
        readyToAddBatchMac(recordList);
    }
}
function typeRange(value){
	if(value == 33){
		return "PN8621"
	}else if(value == 34){
		return "PN8622"
	}else if(value == 36){
		return "PN8624"
	}else if(value == '37'){
		return "PN8625"
	}else if(value == '38'){
		return "PN8626"
	}else if(value == '40'){
		return "PN8628"
	}else if(value == '48'){
		return "PN8630"
	}else if(value == '49'){
		return "PN8631"
	}else if(value == '65'){
		return "PN8641"
	}else if(value == '68'){
		return "PN8644"
	}else if(value == '71'){
		return "PN8647"
	}else if(value == '81'){
		return "PN8651"
	}else if(value == '82'){
		return "PN8652"
	}else if(value == '83'){
		return "PN8653"
	}else if(value == '84'){
		return "PN8654"
	}else if(value == '241'){
		return "CC8800"
	}else{
		return "NONE"
	}
}
var isMacStoreReady = false;
function readyToAddBatchMac(recordList){
    if(isMacStoreReady){
        macStore.add(recordList);
        setTimeout(function(){
            macGrid.getView().focusRow(macStore.getCount() - 1);
        }, 300);
    }else{
        setTimeout(function(){
            readyToAddBatchMac(recordList);
        }, 50);
    }
}

function reloadLocation(){
	location.href = '/epon/onuauth/showOnuAuthen.tv?entityId=' + entityId + '&slotId=' + slotId + '&ponId=' + ponId;
}

function authLoad(){
	var ids = new Array();
	ids.add("macAddBt");
	ids.add("snAddBt");
	ids.add("modeSel");
	ids.add("autoAuthSel");
	operationAuthInit(operationDevicePower,ids);
	$("#macAddBt,#snAddBt").attr("disabled",true);
	disabledBtns();
	if(!refreshDevicePower){
        $("#refreshAuthBt").attr("disabled",true);
        $("#refreshBlockBt").attr("disabled",true);
    }
}
</script>
</head>
<body class="openWinBody"  onload="authLoad()" style="overflow:hidden;">
	<div id=bodyFieldset style="padding:10px;">
	<!-- loadMask -->
	<div id="loadingMask" style="display:none;">
	    <img class="loadingImage" src='/images/gray_loading.gif' title="@COMMON.loading@"/>
	    <div class="loadingText" >@COMMON.loading@</div>
	</div>
<!-- 修改的提示 -->
<div id=modifyTip onclick="tipClick(this)" title="@onuAuth.tit.clickToHide@"
style="display:none;position:absolute;z-index:1;color:red;top:510px;left:210px;width:350px;height:15px;"
>@onuAuth.tit.modifyTip@</div>
<!-- 模式设置的提示 -->
<div id="modeTip" onclick="tipClick(this)" title="@onuAuth.tit.clickToHide@" 
style="display:none;position:absolute;z-index:100;top:52px;left:550px;border:1px solid black;background-color:#ffffbe;text-align:left;" 
></div>

<!-- 页面布局开始 -->
    <table width=100%>
        <tr><td >
            <table width="100%">
                <tr height=30px><td>
                
	                <table width="100%">
	                    <tr>
	                    	<td width=50px align=left class="blueTxt">@onuAuth.ponSlot@</td>
	                    	<td width=90px align=left>
	                    	     <select id="slotSel" style="width: 70px;" onChange="slotIdChange()">
	                        	</select>
	                    	</td>
	                    	<td width=70px align=center class="blueTxt">@PERF.ponPort@</td>
	                    	<td width=90px align=left>
	                        	<select id="ponSel" style="width: 70px;" onChange="ponIdChange()">
	                        	</select>
	                    	</td>
	                    	<td width=110px align=center class="blueTxt"><span id="modeText">@onuAuth.ponMode@</span></td>
	                    	<td width=150px align=left>
		                        <select id="modeSel" style="width: 175px;" onChange="modeChange()">
		                        	<option value="1">@onuAuth.autoMode@</option>
		                            <option value="2">@onuAuth.macMode@</option>
		                            <option value="3">@onuAuth.mixMode@</option>
		                            <option value="4">@onuAuth.snMode@</option>
		                            <option value="5">@onuAuth.snPwdMode@</option>
		                        </select>
	                        	<img id='modeTipImg' src='/images/help.gif' title='@COMMON.tip@' onclick='modeTipImgClick()' />
		                    </td>
		                </tr>
	                </table>
                
                
                
                </td></tr>
                <tr height=1px><td>
                    <hr size=1 style="filter:alpha(opacity=100,opacity=5,style=1);width:100%;color:#1973b4;">
                </td></tr>
                <tr height=30px><td><table>
                    <tr><td>
                            <a href="javascript:;" class="normalBtn" onclick="blockBtClick(1)"><span><i class="miniIcoSearch"></i>@onuAuth.showBlock@</span></a> 
                    </td><td>
                    	<a id=hideBlockBt href="javascript:;" onclick="blockBtClick(2)" class="normalBtn" style="display:none;"><span><i class="miniIcoArrUp"></i>@onuAuth.collapseBlock@</span></a> 
                        
                    </td><td>
                    	<a id=refreshBlockBt onclick="refreshBlock()" style="display:none;" href="javascript:;" class="normalBtn"><span><i class="miniIcoRefresh"></i>@onuAuth.refershBlock@</span></a> 
                    
                    
                    </td><td>
                        <span id=blockTip style="color:red;display:none;" onclick="tipClick(this)">
                            @onuAuth.refershBlockAgain@</span>
                            <!-- ONUnum的提示 -->
                            <div id="onuNoTip" onclick="tipClick(this)" title="@onuAuth.tit.clickToHide@"
                            	style="dispaly:block;left:210px;height:15px;color:red;"></div>

                    </td></tr>
                </table></td></tr>
            </table>
        </td></tr>
        <tr><td>
            <table>
                <tr>
                	<td colspan=2>
                    <div id=blockDiv style="display:none;">
 	                   <div id="blockGridDiv"></div>
                    </div>
                	</td>
                </tr>
                <tr><td>
                    <div id=macDiv >
                        <table><tr><td>
                            <div id="macFieldset" style='width: 380px;'>
                                <div id="macGridDiv"></div>
                                <div id="macAddBtDiv" class='pT5'>
                                	<a id=macAddBt onclick="macAddBtClick(0)" href="javascript:;" class="normalBtn"><span>@onuAuth.macAdd@</span></a> 
                                	<a id=macInsteadBt onclick="macInsteadBtClick()" style="display:none;"  href="javascript:;" class="normalBtnBig"><span>@onuAuth.macInstead@</span></a> 
                                </div>
                            </div>
                        </td></tr></table>
                    </div>
                </td><td>
                    <div id=snDiv style="margin-left:1px;">
                        <table><tr><td>
                            <div id="snFieldset" >
                                <div id="snGridDiv"></div>
                                
                                <div id="snAddBtDiv" class="pT5" style="height:30px">
                                	<a id=snAddBt  onclick="snAddBtClick(0)" href="javascript:;" class="normalBtn"><span>@onuAuth.snAdd@</span></a> 
                                	<a onclick="snInsteadBtClick()" style="display:none;" id=snInsteadBt href="javascript:;" class="normalBtn"><span>@onuAuth.snInstead@</span></a> 
                                </div>
                            </div>
                        </td></tr></table>
                    </div>
                </td></tr>
            </table>
        </td></tr>
    </table>
    <div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	         <li><a id=refreshAuthBt  onclick="refreshAuthClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a id=saveBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
	     </ol>
	</div>
</div>
<!-- 页面布局结束 -->
</body>
<style>
#loadingMask {
    position: absolute;
    left: 0px;
    top: 0px;
    width:100%;
    height: 500px;
    background-color: #000000;
    z-index: 1;
    filter:'alpha(opacity=50)';
    opacity:0.5;
}
.loadingText {
    position: absolute;
    left: 410px;
    top: 229px;
    font-size: 10;
    color:'white';
}
.loadingImage {
    position: absolute;
    left: 370px;
    top: 220px;
    height: 32px;
    width: 32px;
}
</style>
</Zeta:HTML>