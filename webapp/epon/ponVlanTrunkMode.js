// VLAN Trunk
var trunkData = new Array();
var trunkStore;
var trunkGrid;
var trunkText;

// TODO: 合并删除和添加的ajax操作

function loadTrunkMode() {
	// PON/LLID方式处理
	var url = '';
	if (vlanConfigType == 'pon') {
		url = '/epon/vlan/loadTrunkData.tv?ponId=' + ponId + '&r=' + Math.random();
	} else if (vlanConfigType == 'llid') {
		url = '/epon/vlan/loadLlidTrunkData.tv?ponId=' + ponId + '&trunkMac=' + selectedMac + '&r=' + Math.random();
	}
	var initTrunkStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : url
		}),
		reader : new Ext.data.ArrayReader({}, Ext.data.Record.create([ {
			name : 'trunkId',
			type : 'int'
		} ]))
	});
	initTrunkStore.load({
		callback : function(records, options, success) {
			// 数据加载完毕更新数组中的值
			trunkData = new Array();
			$.each(records, function(i, n) {
				trunkData[i] = n.json;
			});
			if (trunkGrid == null || trunkGrid == undefined) {
				trunkStore = new Ext.data.SimpleStore({
					data : trunkData,
					fields : [ 'trunkId' ]
				});
				trunkGrid = new Ext.grid.GridPanel({
					stripeRows:true,
			   		region: "center",
			   		bodyCssClass: 'normalTable',
			   		viewConfig:{forceFit: true},
					renderTo : "trunkIdList",
					height : 265,
					width : 390,
					frame : false,
					border : false,
					autoScroll : true,
					columns : [ {
						header : "Trunk VLAN ID",
						dataIndex : "trunkId",
						width : 200,
						align : "center",
						renderer : trunkChangeRed
					}, {
						header : I18N.COMMON.manu,
						dataIndex : "id",
						width : 168,
						align : "center",
						renderer : function(value, cellmeta, record) {
							if(operationDevicePower){
								return "<img src='/images/delete.gif' onclick='deleteTrunkRule(" + record.data.trunkId + ")'/>";
							}else{
								return "<img src='/images/deleteDisable.gif'/>";
							}
						}
					} ],
					store : trunkStore,
					listeners : {
						'viewready' : {
							fn : function() {
								trunkGrid.getSelectionModel().selectFirstRow();
							},
							scope : this
						}
					}
				});
			} else {
				trunkStore.loadData(trunkData);
			}
			trunkGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
				var tmpId = grid.selections.get(0).data.trunkId;
				if (tmpId) {
					selectedIndex = getTrunkIndex(tmpId);
				} else {
					selectedIndex = 0;
				}
			});
			$("#trunkId").keyup(function() {
				text = $("#trunkId").val();
				trunkText = text;
				var data = [];
				// 非空验证
				if (trunkText == null || trunkText == "" || trunkText == undefined) {
					// 为空，灰掉添加按钮
					$("#trunkSubmit").attr("disabled", true);
					$("#trunkSubmit").mouseout();
				} else {
					// 激活 添加按钮
					$("#trunkSubmit").attr("disabled", false);
					// 输入验证
					if (!checkedInputList("trunkId")) {
						// 灰掉submit按钮
						$("#trunkSubmit").attr("disabled", true);
						$("#trunkSubmit").mouseout();
						return;
					}
				}
				var vidList = new Array();
				vidList = changeToArray();
				$.each(trunkData, function(i, n) {
					var tmp = "" + n;
					if (exactFlag) {
						searchShowFlag = tmp.exactMatchWith(vidList);
					} else {
						searchShowFlag = tmp.matchWith(vidList);
					}
					if (text == null || text == "" || searchShowFlag) {
						data[data.length] = [ tmp ];
					}
				});
				if (data.length == 0) {
					$("#trunkSubmit").attr("disabled", false);
				}
				trunkStore.loadData(data);
			});
		}
	});
}

function getTrunkIndex(trunkId) {
	var tmpIndex = -1;
	for ( var i = 0; i < trunkData.length; i++) {
		if (trunkData[i][0] == trunkId) {
			tmpIndex = i;
			break;
		}
	}
	return tmpIndex;
}

function trunkVidChecked() {
	text = $("#trunkId").val();
	var trunkIdList = new Array();
	trunkIdList = changeToArray();
	for ( var k = 0; k < trunkIdList.length; k++) {
		var trunkId = trunkIdList[k];
		var count = 0;
		// SVID的存在性
		for ( var i = 0; i < svids.length; i++) {
			if (svids[i][0] == trunkId) {
				count++;
				// SVID的合法性
				var mode = svids[i][1];
				var modeStr = vlanModeString[mode];
				var mac = svids[i][2];
				if(mac == null || mac == undefined || mac == ""){
					mac = "0";
				}
				if (vlanConfigType == 'pon') {
					//PON已经没有Trunk模式的设置了，不应进入该循环
					return false;
				} else if (vlanConfigType == 'llid') {
					if ((mode > 0 && mode < 5) || (mode > 4 && mode < 9 && (mac == selectedMac || mac == "0"))) {
						window.parent.showMessageDlg(I18N.COMMON.tip, 
							String.format(I18N.VLAN.conflictTipQinQ2, trunkId, mac, modeStr));
						return false;
					}
				}
			}
		}
		if (count == 0) {
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.notExist , trunkId))
			return false;
		} else {
			count = 0;
			// CVID的唯一性
			for ( var j = 0; j < cvids.length; j++) {
				var mode = cvids[j][1];
				var modeStr = vlanModeString[mode];
				var mac = cvids[j][2];
				if(mac == "" || mac == null || mac == undefined){
					mac = "0";
				}
				if (vlanConfigType == 'pon') {
					if (cvids[j][0] == trunkId) {
						if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || (mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)) {
							if(mac != "0"){
								window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip ,  {vlan:trunkId , mac:mac , mode:modeStr}))
							}else{
								window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip2 , {vlan:trunkId , mode:modeStr}))
							}
							return false;
						}
					}
				}else if(vlanConfigType == 'llid'){
					if (cvids[j][0] == trunkId) {
						if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || ((mac == selectedMac) && ((mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)))) {
							if (mac != "0") {
								window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip ,  {vlan:trunkId , mac:mac , mode:modeStr}))
							} else {
								window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip2 , {vlan:trunkId , mode:modeStr}))
							}
							return false
						}
					}
				}
			}
		}
	}
	return true;
}

function deleteTrunkRule(trunkId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanTrunk , trunkId) , function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingVlanTrunk , trunkId), 'ext-mb-waiting');
		// 临时变量存储删除后Trunk列表
		var trunkDataTmp = trunkData.slice();
		trunkDataTmp.splice(selectedIndex,1);
		// PON/LLID方式处理
		var url = '';
		var params = {};
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/modifyTrunkRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				trunkVids : trunkDataTmp.toString()
			};
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/modifyLlidTrunkRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				trunkMac : selectedMac,
				trunkVids : trunkDataTmp.toString()
			};
		}
		$.ajax({
			url : url,
			success : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanTrunkOk , trunkId))
                // 删除成功时，修改页面数据。
                trunkData.splice(selectedIndex,1);
                trunkStore.loadData(trunkData);
                trunkGrid.selModel.selectRow(0);
                $("#trunkSubmit").attr("disabled", true);
                $("#trunkSubmit").mouseout();
                $("#trunkId").val("");
                $("#trunkId").focus();
                loadScvidData();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanTrunkError , trunkId))
			},
			data : params
		});
	});
}

function trunkChangeRed(value, a, b, c) {
	if (value.toString().redMatchWith($("#trunkId").val())) {
		$("#trunkSubmit").attr("disabled", true);
		$("#trunkSubmit").mouseout();
		return "<font color='red'>" + value + "</font>";
	} else {
		return value;
	}
}

function trunkOkClick() {
	text = $("#trunkId").val();
	var vidList = new Array();
	vidList = changeToArray();
	// 临时存储trunkData数据
	var trunkDataTmp = trunkData.slice();
	var message = changeToString(vidList).join(",");
	for ( var i = 0; i < vidList.length; i++) {
		trunkDataTmp[trunkDataTmp.length] = vidList[i];
	}
	// trunk VID全局验证
	if (!trunkVidChecked()) {
		return;
	}
	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingVlanTrunk, message), 'ext-mb-waiting');
	// PON/LLID方式处理
	var url = '';
	var params = {};
	if (vlanConfigType == 'pon') {
		url = '/epon/vlan/modifyTrunkRule.tv?r=' + Math.random();
		params = {
			entityId : entityId,
			ponId : ponId,
			trunkVids : trunkDataTmp.toString()
		};
	} else if (vlanConfigType == 'llid') {
		url = '/epon/vlan/modifyLlidTrunkRule.tv?r=' + Math.random();
		params = {
			entityId : entityId,
			ponId : ponId,
			trunkMac : selectedMac,
			trunkVids : trunkDataTmp.toString()
		};
	}
	$.ajax({
		url : url,
		success : function() {
            window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addVlanTrunkOk, message));
            // 添加成功时，修改页面数据。
            for ( var u = 0; u < vidList.length; u++) {
                trunkData[trunkData.length] = [ vidList[u] ];
            }
            trunkStore.loadData(trunkData);
            trunkGrid.selModel.selectRow(trunkData.length - 1);
            $("#trunkSubmit").attr("disabled", true);
            $("#trunkSubmit").mouseout();
            $("#trunkId").val("");
            $("#trunkId").focus();
            loadScvidData();
		},
		error : function() {
			window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addVlanTrunkError, message))
		},
		data : params
	});
}
