// VLAN转换
var transData = new Array();
var transStore;
var transGrid;
var transType;
var transIndex;
var beforeTransText;
var afterTransText;
var beforeTransFlag = 0;
var afterTransFlag = 0;
var transNotChangeRed = false;

function loadTransMode() {
    // PON/LLID方式处理
    var url = '';
    var transColumns;
    if (vlanConfigType == 'pon') {
        url = '/epon/vlan/loadTransData.tv?ponId=' + ponId + '&r=' + Math.random();
        transColumns = [
            {
                header : I18N.VLAN.originVlan + " ID",
                dataIndex : "beforeTransId",
                width : 155,
                align : "center",
                renderer : transChangeRed
            },{
                header : I18N.VLAN.translatedVlan + " ID",
                dataIndex : "afterTransId",
                width : 200,
                align : "center"
            },
            {
                header : I18N.COMMON.manu,
                dataIndex : "id",
                width : 120,
                align : "center",
                renderer : function(value, cellmeta, record) {
                    // 变红操作后，特殊处理。
                    if (record.data.afterTransId.length > 4) {
                        record.data.afterTransId = parseInt(record.data.afterTransId.substring(18).split("<")[0]);
                    }
                    if(operationDevicePower){
                    	return "<img src='/images/delete.gif' onclick='deleteTransRule(" + record.data.beforeTransId + ", " + record.data.afterTransId + ")'/>";
                    }else{
                    	return "<img src='/images/deleteDisable.gif'/>";
                    }
                }
            }
        ];
    } else if (vlanConfigType == 'llid') {
        url = '/epon/vlan/loadLlidTransData.tv?ponId=' + ponId + '&transMac=' + selectedMac + '&r=' + Math.random();
        transColumns = [
            {
                header : I18N.VLAN.originVlan + " ID",
                dataIndex : "beforeTransId",
                width : 80,
                align : "center",
                renderer : transChangeRed
            },
            {
                header : I18N.VLAN.translatedVlan + " ID",
                dataIndex : "afterTransId",
                width : 100,
                align : "center"
            },
            {
                header :  I18N.VLAN.translatedVlanPri ,
                dataIndex : "transCos",
                width : 125,
                align : "center",
                renderer : function(value) {
                    return value == 8 ? 'copy' : value;
                }
            },
            {
                header : I18N.VLAN.translatedVlan  + " TPID",
                dataIndex : "transTpid",
                width : 125,
                align : "center"
            },
            {
                header : I18N.COMMON.manu,
                dataIndex : "id",
                width : 45,
                align : "center",
                renderer : function(value, cellmeta, record) {
                    // 变红操作后，特殊处理。
                    if (record.data.afterTransId.length > 4) {
                        record.data.afterTransId = parseInt(record.data.afterTransId.substring(18).split("<")[0]);
                    }
                    if (record.data.afterTransId.length > 4) {
                        record.data.afterTransId = parseInt(record.data.afterTransId.substring(18).split("<")[0]);
                    }
                    if(operationDevicePower){
                    	return "<img src='/images/delete.gif' onclick='deleteTransRule(" + record.data.beforeTransId + ", " + record.data.afterTransId + ")'/>";
                    }else{
                    	return "<img src='/images/deleteDisable.gif'/>";
                    }
                }
            }
        ];
    }
    var initTransStore = new Ext.data.Store({
        proxy : new Ext.data.HttpProxy({
            url : url
        }),
        reader : new Ext.data.ArrayReader({}, Ext.data.Record.create([
            {
                name : 'beforeTransId'
            },
            {
                name : 'afterTransId'
            },
            {
                name : 'transCos'
            },
            {
                name : 'transTpid'
            }
        ]))
    });
    initTransStore.load({
        callback : function(records, options, success) {
            // 数据加载完毕更新数组中的值
            transData = new Array();
            $.each(records, function(i, n) {
                transData[i] = new Array();
                transData[i][0] = n.json[0];
                transData[i][1] = n.json[1];
                transData[i][2] = n.json[2];
                transData[i][3] = n.json[3];
            });
            if (transGrid == null || transGrid == undefined) {
                transStore = new Ext.data.SimpleStore({
                    data : transData,
                    fields : [ 'beforeTransId', 'afterTransId', 'transCos', 'transTpid' ]
                });
                transGrid = new Ext.grid.GridPanel({
                	stripeRows:true,
               		region: "center",
               		bodyCssClass: 'normalTable',
               		viewConfig:{forceFit: true},
                    renderTo : "transIdList",
                    height : 270 - heigh,
                    width : 500,
                    border : false,
                    autoScroll : true,
                    loadMask : {
                        msg : I18N.COMMON.loading
                    },
                    columns : transColumns,
                    sm : new Ext.grid.RowSelectionModel({
                        singleSelect : true
                    }),
                    store : transStore,
                    listeners : {
                        'viewready' : {
                            fn : function() {
                                transGrid.getSelectionModel().selectFirstRow();
                            },
                            scope : this
                        }
                    }
                });
            } else {
                transStore.loadData(transData);
            }
            transGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
                var tmpId = transGrid.selModel.selections.get(0).data.beforeTransId;
                if (tmpId) {
                    selectedIndex = getTransIndex(tmpId);
                    $("#beforeTransId").val(tmpId);
                } else {
                    selectedIndex = 0;
                }
            });
            $("#beforeTransId").keyup(function() {
                text = $("#beforeTransId").val();
                beforeTransText = text;
                afterTransText = $("#afterTransId").val();
                beforeTransFlag = 0;
                afterTransFalg = 0;
                if (beforeTransText == null || beforeTransText == undefined || beforeTransText == "") {
                    transSubmitChange(1, 1);
                } else {
                    if (!checkedInput("beforeTransId")) {
                        transSubmitChange(1, 1);
                        return;
                    } else if (afterTransText == null || afterTransText == "" || afterTransText == undefined) {
                        transSubmitChange(1, 1);
                    } else if (checkedInput("beforeTransId")) {
                        $("#transSubmit").attr("disabled", false);
                    } else {
                        $("#transSubmit").attr("disabled", true);
                        $("#transSubmit").mouseout();
                    }
                }
                var data = new Array();
                var j = 0;
                $.each(transData, function(i, n) {
                    var tmp1 = "" + n[0];
                    var tmp2 = "" + n[1];
                    if ( text == null || text == "" || tmp1.startWith(text) ) {
                    	if( (tmp2.redMatchWith(afterTransText)) ||  (afterTransText == "") ){
                    		data[j] = n;
                        	j++;
                    	}
                    }
                });
                j = 0;
                transStore.loadData(data);
                transSubmitChange(beforeTransFlag, afterTransFalg);
            });
            $("#afterTransId").keyup(function() {
                text = $("#afterTransId").val();
                afterTransText = text;
                beforeTransText = $("#beforeTransId").val();
                beforeTransFlag = 0;
                afterTransFalg = 0;
                if (afterTransText == null || afterTransText == undefined || afterTransText == "") {
                    transSubmitChange(1, 1);
                } else {
                    if (!checkedInput("afterTransId")) {
                        transSubmitChange(1, 1);
                        return;
                    } else if (beforeTransText == null || beforeTransText == "" || beforeTransText == undefined) {
                        transSubmitChange(1, 1);
                    } else if (checkedInput("beforeTransId")) {
                        $("#transSubmit").attr("disabled", false);
                    } else {
                        $("#transSubmit").attr("disabled", true);
                        $("#transSubmit").mouseout();
                    }
                }
                var data = new Array();
                var j = 0;
                $.each(transData, function(i, n) {
                    var tmp1 = "" + n[0];
                    var tmp2 = "" + n[1];
                    if (text == null || text == "" || tmp2.startWith(text)) {
                    	if( (tmp1.redMatchWith(beforeTransText)) ||  (beforeTransText == "") ){
                    		data[j] = n;
                    		j++;
                    	}
                    }
                });
                j = 0;
                transStore.loadData(data);
                transSubmitChange(beforeTransFlag, afterTransFalg);
            });
        }
    });
}

function getTransIndex(transId) {
    var tmpIndex = -1;
    for (var i = 0; i < transData.length; i++) {
        if (transData[i][0] == transId) {
            tmpIndex = i;
            break;
        }
    }
    return tmpIndex;
}

function transSvidChecked() {
    var afterId = parseInt($("#afterTransId").val());
    var count = 0;
    // SVID的存在性
    for (var i = 0; i < svids.length; i++) {
        if (svids[i][0] == afterId) {
            count++;
            // SVID的合法性
            var mode = svids[i][1];
            var modeStr = vlanModeString[mode];
            var mac = svids[i][2];
            if(mac == "" || mac == null || mac == undefined){
        		mac = "0";
        	}
            if (vlanConfigType == 'pon') {
            	if (mode == 1 || mode == 2 || mode == 3 || mode == 5 || mode == 6 || mode == 7) {
                    if(mac != "0"){
                		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip, 
                				{vlan: afterId, mac : mac , mode : modeStr}) );
                	}else{
                		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip2, 
                				{vlan : afterId , mode : modeStr}));
                	}
                    $("#afterTransId").focus();
                    return false;
                }
            } else if (vlanConfigType == 'llid') {
            	if ((mode == 1 || mode == 2 || mode == 3) || ((mode == 5 || mode == 6 || mode == 7) && (mac == selectedMac || mac == "0"))) {
                    if(mac != "0"){
                		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip, 
                				{vlan:afterId , mac:mac ,mode: modeStr}));
                	}else{
                		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip2, 
                				{vlan:afterId , mode:modeStr}));
                	}
                    $("#afterTransId").focus();
                    return false;
                }
            }
        }
    }
    if (count == 0) {
        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.vlanCountTip ,afterId))
        $("#afterTransId").focus()
        return false
    } else {
        count = 0;
        return true;
    }
}
function transCvidChecked() {
    var beforeId = parseInt($("#beforeTransId").val());
    // CVID的唯一性
    for (var j = 0; j < cvids.length; j++) {
        var mode = cvids[j][1];
        var modeStr = vlanModeString[mode];
        var mac = cvids[j][2];
    	if(mac == "" || mac == null || mac ==undefined){
    		mac = "0";
    	}
        if (vlanConfigType == 'pon') {
        	if(cvids[j][0] == beforeId){
        		if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || (mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)) {
        			if (mac != "0") {
                        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip , {vlan: beforeId, mac : mac , mode : modeStr}));
                    } else {
                        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip2 , {vlan : beforeId , mode : modeStr}))
                    }
    				return false;
    			}
        	}
        }else if(vlanConfigType == 'llid'){
        	if ((cvids[j][0] == beforeId) && (((mac == selectedMac) && ((mode == 5) || (mode == 6) || (mode == 7) || (mode == 8))) || (mode == 1) || (mode == 2) || (mode == 3) || (mode == 4))) {
                if (mac != "0") {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip , {vlan:beforeId , mac:mac ,mode: modeStr}))
                } else {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip2 , {vlan:beforeId , mode:modeStr}))
                }
                $("#beforeTransId").focus()
                return false
            }
        }
    }
    return true;
}

function deleteTransRule(beforeTransId, afterTransId) {
    window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanTransRule , {before:beforeTransId ,after:afterTransId}), function(type) {
        if (type == 'no') {
            return;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingTransRule , {before:beforeTransId , after: afterTransId}))
        // PON/LLID方式处理
        var url = '';
        var params = {};
        if (vlanConfigType == 'pon') {
            url = '/epon/vlan/deleteTransRule.tv?r=' + Math.random()
            params = {
                entityId : entityId,
                ponId : ponId,
                oldBeforeTransVid : beforeTransId
            }
        } else if (vlanConfigType == 'llid') {
            url = '/epon/vlan/deleteLlidTransRule.tv?r=' + Math.random()
            params = {
                entityId : entityId,
                ponId : ponId,
                transMac : selectedMac,
                oldBeforeTransVid : beforeTransId
            }
        }
        $.ajax({
            url : url,cache:false,
            success : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delRuleOk , {before:beforeTransId , after: afterTransId}));
                // 删除成功时，修改页面数据。
                transData.splice(selectedIndex,1); 
                //transData.remove(selectedIndex);
                beforeTransText = "";
                afterTransText = "";
                transStore.loadData(transData);
                transGrid.selModel.selectRow(0, true);
                $("#beforeTransId").val("");
                $("#afterTransId").val("");
                $("#beforeTransId").focus();
                $("#transSubmit").attr("value", I18N.COMMON.confirm)
                $("#transSubmit").attr("disabled", true);
                $("#transSubmit").mouseout();
                loadScvidData();
            },
            error : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delRuleError , {before:beforeTransId , after: afterTransId}))
            },
            data : params
        });
    });
}

function transChangeRed(value, a, b, c) {
    if (transNotChangeRed) {
        return value;
    } else {
        if ((b.data.beforeTransId == parseInt(beforeTransText)) && (b.data.afterTransId == parseInt(afterTransText))) {
            b.data.afterTransId = "<font color='red'>" + b.data.afterTransId + "</font>";
            transGrid.selModel.selectRow(0, true);
            $("#transSubmit").attr("disabled", true);
            $("#transSubmit").mouseout();
            beforeTransFlag = 1;
            afterTransFalg = 1;
            return "<font color='red'>" + value + "</font>";
        } else if ((b.data.beforeTransId == parseInt(beforeTransText)) && (b.data.afterTransId != parseInt(afterTransText))) {
            $("#transSubmit").attr("disabled", false);
            beforeTransFlag = 1;
            return "<font color='red'>" + value + "</font>";
        } else if ((b.data.beforeTransId != parseInt(beforeTransText)) && (b.data.afterTransId == parseInt(afterTransText))) {
            b.data.afterTransId = "<font color='red'>" + b.data.afterTransId + "</font>";
            afterTransFalg = 1;
            $("#transSubmit").attr("disabled", false);
            return value;
        } else {
            return value;
        }
    }
}

function transSubmitChange(i, j) {
    if (2 == i + j) {
        transType = "filter";
        $("#transSubmit").attr("value", I18N.COMMON.confirm)
        $("#transSubmit").attr("disabled", true);
        $("#transSubmit").mouseout();
    } else if (1 == i + j) {
        if (i == 1) {
            transType = "modify1";
        } else if (j == 1) {
            transType = "modify2";
        }
        $("#transSubmit").attr("value", I18N.COMMON.modify)
        $("#transSubmit").attr("disabled", false);
    } else if (0 == i + j) {
        transType = "add";
        $("#transSubmit").attr("value", I18N.VLAN.add)
        $("#transSubmit").attr("disabled", false);
    }
    if ($("#afterTransId").val() == null || $("#afterTransId").val() == "" || $("#afterTransId").val() == undefined || $("#beforeTransId").val() == null || $("#beforeTransId").val() == ""
        || $("#beforeTransId").val() == undefined) {
        $("#transSubmit").attr("disabled", true);
        $("#transSubmit").mouseout();
    }
    if (!checkedInput("beforeTransId") || !checkedInput("afterTransId")) {
        $("#transSubmit").attr("disabled", true);
        $("#transSubmit").mouseout();
    }
}

function transOkClick() {
    if (vlanConfigType == 'llid') {
        if (!tpidChanged("transTpid")) {
            return
        }
    }
    if (transType == "add") {
        // trans SVID全局验证
        if (!transSvidChecked()) {
            return
        }
        // trans CVID全局验证
        if (!transCvidChecked()) {
            return
        }
        var beforeTransId = $("#beforeTransId").val()
        var afterTransId = $("#afterTransId").val()
        for(var i=0;i<qinqSvlanData.length;i++){
        	if(qinqSvlanData[i] == beforeTransId){
        		$("#beforeTransId").focus();
        		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictWithOther1))
        		return;
        	}
        	if(qinqSvlanData[i] == afterTransId){
        		$("#afterTransId").focus();
        		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictWithOther1))
        		return;
        	}
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingVlanRule , {before:beforeTransId , after: afterTransId}), 'ext-mb-waiting')
        // PON/LLID方式处理
        var url = ''
        var params = ''
        if (vlanConfigType == 'pon') {
            url = '/epon/vlan/addTransRule.tv?r=' + Math.random();
            params = {
                entityId : entityId,
                ponId : ponId,
                newBeforeTransVid : beforeTransId,
                newAfterTransVid : afterTransId
            };
        } else if (vlanConfigType == 'llid') {
            url = '/epon/vlan/addLlidTransRule.tv?r=' + Math.random();
            var cosMode = $('#transCosCheckbox2').attr('checked') ? 2 : 1;
            var cosValue = cosMode == 1 ? $('#transCos').val() : 0;
            var tpid = $('#transTpid').val().toLocaleLowerCase();
            params = {
                entityId : entityId,
                ponId : ponId,
                transMac : selectedMac,
                newBeforeTransVid : beforeTransId,
                newAfterTransVid : afterTransId,
                cosMode : cosMode,
                cosValue : cosValue,
                tpid : tpid
            };
        }
        $.ajax({
            url : url,
            success : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addVlanRuleOk , {before:beforeTransId , after : afterTransId}))
                // 添加成功时，修改页面数据。
                if (cosMode == 2) {
                    cosMode = 8;
                } else {
                    cosMode = cosValue;
                }
                transData[transData.length] = [ parseInt(beforeTransId), parseInt(afterTransId), cosMode, tpid ];
                beforeTransText = "";
                afterTransText = "";
                // 排序
                transData.sort(function(a, b) {
                    return parseInt(a[0]) - parseInt(b[0]);
                });
                // 获取排序后的index
                var newRuleIndex = 0;
                $.each(transData, function(i, n) {
                    if (n[0] == beforeTransId) {
                        newRuleIndex = i;
                        return false;
                    }
                })
                transStore.loadData(transData)
                transGrid.selModel.selectRow(newRuleIndex, true)
                $("#beforeTransId").val("")
                $("#afterTransId").val("")
                $("#beforeTransId").focus()
                $("#transSubmit").attr("value", I18N.COMMON.confirm)
                $("#transSubmit").attr("disabled", true)
                $("#transSubmit").mouseout()
                loadScvidData()
            },
            error : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addVlanRuleError , {before:beforeTransId , after : afterTransId}))
            },
            data : params
        });
    } else if (transType == "modify1" || transType == "modify2") {
        var newBeforeTransId = $("#beforeTransId").val()
        var newAfterTransId = $("#afterTransId").val()
        var oldBeforeTransId
        var oldAfterTransId
        var modifyIndex
        if (transType == "modify1") {
            // trans SVID全局验证
            if (!transSvidChecked()) {
                return
            }
            // 获取转换前参数
            oldBeforeTransId = newBeforeTransId;
            for (var i = 0; i < transData.length; i++) {
                if (transData[i][0] == oldBeforeTransId) {
                    oldAfterTransId = transData[i][1]
                    modifyIndex = i
                    break
                }
            }
        } else if (transType == "modify2") {
            // trans CVID全局验证
            if (!transCvidChecked()) {
                return
            }
            // 获取转换前参数
            oldAfterTransId = newAfterTransId;
            for (var i = 0; i < transData.length; i++) {
                if (transData[i][1] == oldAfterTransId) {
                    oldBeforeTransId = transData[i][0]
                    modifyIndex = i
                    break
                }
            }
        } else {
            return;
        }

        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.mdfingVlanRule , [oldBeforeTransId, oldAfterTransId, newBeforeTransId, newAfterTransId]))
        // PON/LLID方式处理
        var url = ''
        var params = ''
        if (vlanConfigType == 'pon') {
            url = '/epon/vlan/modifyTransRule.tv?r=' + Math.random();
            params = {
                entityId : entityId,
                ponId : ponId,
                oldBeforeTransVid : oldBeforeTransId,
                oldAfterTransVid : oldAfterTransId,
                newBeforeTransVid : newBeforeTransId,
                newAfterTransVid : newAfterTransId
            };
        } else if (vlanConfigType == 'llid') {
            url = '/epon/vlan/modifyLlidTransRule.tv?r=' + Math.random();
            var cosMode = $('#transCosCheckbox2').attr('checked') ? 2 : 1;
            var cosValue = cosMode == 1 ? $('#transCos').val() : 0;
            var tpid = $('#transTpid').val().toLocaleLowerCase();
            params = {
                entityId : entityId,
                ponId : ponId,
                transMac : selectedMac,
                oldBeforeTransVid : oldBeforeTransId,
                oldAfterTransVid : oldAfterTransId,
                newBeforeTransVid : newBeforeTransId,
                newAfterTransVid : newAfterTransId,
                cosMode : cosMode,
                cosValue : cosValue,
                tpid : tpid
            };
        }
        $.ajax({
            url : url,
            success : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.mdfVlanRuleOk , [oldBeforeTransId, oldAfterTransId, newBeforeTransId, newAfterTransId]))
                // 修改成功时，修改页面数据。
                transNotChangeRed = true;
                transData[modifyIndex][0] = newBeforeTransId;
                transData[modifyIndex][1] = newAfterTransId;
                if (vlanConfigType == 'llid') {
                    if (cosMode == 2) {
                        transData[modifyIndex][2] = 8;
                    } else {
                        transData[modifyIndex][2] = cosValue;
                    }
                    transData[modifyIndex][3] = tpid;
                }
                transStore.loadData(transData);
                transNotChangeRed = false;
                $("#beforeTransId").val("");
                $("#afterTransId").val("");
                $("#beforeTransId").focus();
                $("#transSubmit").attr("value", I18N.COMMON.confirm);
                $("#transSubmit").attr("disabled", true);
                $("#transSubmit").mouseout();
                loadScvidData();
            },
            error : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.mdfVlanRuleError , [oldBeforeTransId, oldAfterTransId, newBeforeTransId, newAfterTransId]))
            },
            data : params
        });
    }
}