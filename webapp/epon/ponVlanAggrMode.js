// VLAN聚合
var aggrSvlanGrid;
var aggrCvlanGrid;
var aggrSvlanStore;
var aggrCvlanStore;
var aggrSvlanData = new Array();// [[aggrSvid,cosMode,cos],[]]
var aggrCvlanData = new Array();// 数组序号为aggrSvid
// [[],[],[[cvid],[cvid],[cvid]],[]]
var aggrSvid = 0;
aggrCvlanData[0] = new Array();
var aggrSvlanText;
var startText;
var endText;

function loadAggrMode() {
	// PON/LLID方式处理
	var url = '';
	var aggrColumns;
	if (vlanConfigType == 'pon') {
		url = '/epon/vlan/loadAggrData.tv?ponId=' + ponId + '&r=' + Math.random();
		aggrColumns = [ {
			header : I18N.VLAN.trunkedVlan + " ID",
			width : 140,
			dataIndex : "aggrSvlanId",
			align : "center",
			renderer : aggrSvlanChangeRed
		}, {
			header : I18N.COMMON.manu,
			width : 82,
			dataIndex : "id",
			align : "center",
			renderer : function(value, cellmeta, record) {
				if(operationDevicePower){
					return "<img src='/images/delete.gif' onclick='deleteSvlanAggrRule(" + record.data.aggrSvlanId + ")'/>"
				}else{
					return "<img src='/images/deleteDisable.gif'/>"
				}
			}
		} ];
	} else if (vlanConfigType == 'llid') {
		url = '/epon/vlan/loadLlidAggrData.tv?ponId=' + ponId + '&aggrMac=' + selectedMac + '&r=' + Math.random()
		aggrColumns = [ {
			header : I18N.VLAN.trunkVID,
			width : 75,
			dataIndex : "aggrSvlanId",
			align : "center",
			renderer : aggrSvlanChangeRed
		}, {
			header : I18N.VLAN.trunkPri,
			width : 77,
			dataIndex : "aggrCos",
			align : "center"
		}, {
			header : "TPID",
			width : 80,
			dataIndex : "aggrTpid",
			align : "center"
		}, {
			header : I18N.COMMON.manu,
			width : 40,
			dataIndex : "id",
			align : "center",
			renderer : function(value, cellmeta, record) {
				if(operationDevicePower){
					return "<img src='/images/delete.gif' onclick='deleteSvlanAggrRule(" + record.data.aggrSvlanId + ")'/>"
				}else{
					return "<img src='/images/deleteDisable.gif'/>"
				}
			}
		} ];
	}
	var aggrStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : url
		}),
		reader : new Ext.data.ArrayReader({
			id : 0
		}, Ext.data.Record.create([ {
			name : 'aggrSvlanIdList'
		}, {
			name : 'aggrCvlanIdList'
		} ]))
	});
	aggrStore.load({
		callback : function(records, options, success) {
			// 数据加载完毕更新数组中的值
			aggrSvlanData = new Array();
			aggrCvlanData = new Array();
			aggrCvlanData[0] = new Array();
			$.each(records, function(i, n) {
				aggrSvlanData[i] = new Array();
				aggrSvlanData[i][0] = n.json[0][0];
				if (n.json[0][1] == 2) {
					aggrSvlanData[i][1] = 'copy';
				} else {
					aggrSvlanData[i][1] = n.json[0][2];
				}
				aggrSvlanData[i][2] = n.json[0][3];
				aggrCvlanData[n.json[0][0]] = new Array();
				for ( var j = 0; j < n.json[1].length; j++) {
					aggrCvlanData[n.json[0][0]][j] = new Array();
					aggrCvlanData[n.json[0][0]][j][0] = n.json[1][j];
				}
			});
			// 初始时选中第一行
			if (aggrSvlanData[0] == null || aggrSvlanData[0] == "" || aggrSvlanData[0] == undefined) {
				aggrSvid = 0;
			} else {
				aggrSvid = aggrSvlanData[0][0];
			}
			// 聚合SVLANgrid
			if (aggrSvlanGrid == null || aggrSvlanGrid == undefined) {
				aggrSvlanStore = new Ext.data.SimpleStore({
					data : aggrSvlanData,
					fields : [ "aggrSvlanId", "aggrCos", "aggrTpid" ]
				});
				aggrSvlanGrid = new Ext.grid.GridPanel({
					stripeRows:true,
			   		region: "center",
			   		bodyCssClass: 'normalTable',
			   		viewConfig:{forceFit: true},
					id : 'aggrSvlanGrid',
					renderTo : 'aggrSvlan_grid',
					width : 242 + 2 * wid,
					height : 270 - heigh,
					frame : false,
					border : false,
					autoScroll : true,
					columns : aggrColumns,
					selModel : new Ext.grid.RowSelectionModel({
						singleSelect : true,
						listeners: {
							'selectionchange' : function(){
								var tmpS = aggrSvlanGrid.getSelectionModel().getSelected();
								if(tmpS){
									selectedIndex = aggrSvlanStore.indexOf(tmpS);
								}else{
									selectedIndex = -1;
								}
							}
						}
					}),
					store : aggrSvlanStore,
					listeners : {
						"click" : {
							fn : aggrGridClick,
							scope : this
						},
						'viewready' : {
							fn : aggrReady,
							scope : this
						}
					}
				});
			} else {
				aggrSvlanStore.loadData(aggrSvlanData);
			}
			jQuery("#aggregationSvlanId").one("focus", function() {
				jQuery("#aggregationSvlanId").keyup(function() {
					var data = new Array();
					aggrSvlanText = jQuery("#aggregationSvlanId").val();// 在监听的事件发生时更新text
					var j = 0;
					$("#aggregationCvlanId").val("");
					$("#aggrSubmit").attr("disabled", true);
					$("#aggrSubmit").mouseout();
					// 非空验证
					if (aggrSvlanText == null || aggrSvlanText == "" || aggrSvlanText == undefined) {
						// 为空，灰掉CVLANinput
						$("#aggregationCvlanId").attr("disabled", true)
					} else {
						// 激活 CVLANinput
						if(operationDevicePower){
							$("#aggregationCvlanId").attr("disabled", false)
						}
						// 输入验证
						if (!checkedInput("aggregationSvlanId")) {
							// 灰掉CVLANinput和submit按钮
							$("#aggregationCvlanId").attr("disabled", true)
							$("#aggrSubmit").attr("disabled", true)
							$("#aggrSubmit").mouseout()
							return
						}
					}
					// 循环刷选
					jQuery.each(aggrSvlanData, function(i, n) {
						var tmp = "" + n[0];
						if (aggrSvlanText == null || aggrSvlanText == "" || tmp.startWith(aggrSvlanText)) {
							data[j] = n;
							j++;
						}
					});
					SvlanShowLength = j;
					j = 0; // empty j
					aggrSvlanStore.loadData(data)
					aggrSelectFirstRow()
					aggrCvlanReload()
				})
			})
			// 聚合CVLAN
			if (aggrCvlanGrid == null || aggrCvlanGrid == undefined) {
				aggrCvlanStore = new Ext.data.SimpleStore({
					data : aggrCvlanData[aggrSvid],
					fields : [ "aggrCvlanId" ]
				});
				aggrCvlanGrid = new Ext.grid.GridPanel({
					stripeRows:true,
			   		region: "center",
			   		bodyCssClass: 'normalTable',
			   		viewConfig:{forceFit: true},
					renderTo : 'aggrCvlan_grid',
					width : 242 - 2 * wid,
					height : 270 - heigh,
					frame : false,
					border : false,
					selModel : new Ext.grid.RowSelectionModel({
						singleSelect : true,
						listeners: {
							'selectionchange' : function(){
								var tmpS = aggrSvlanGrid.getSelectionModel().getSelected();
								if(tmpS){
									selectedCIndex = aggrSvlanStore.indexOf(tmpS);
								}else{
									selectedCIndex = -1;
								}
							}
						}
					}),
					autoScroll : true,
					columns : [ {
						header : I18N.VLAN.trunkVlan + " ID",
						width : 140 - wid,
						dataIndex : "aggrCvlanId",
						align : "center",
						renderer : aggrCvlanChangeRed
					}, {
						header : I18N.COMMON.manu ,
						width : 82 - wid,
						dataIndex : "id",
						align : "center",
						renderer : function(value, cellmeta, record) {
							if(operationDevicePower){
								return "<img src='/images/delete.gif' onclick='deleteCvlanAggrRule(" + aggrSvid + "," + record.data.aggrCvlanId + ")'/>";
							}else{
								return "<img src='/images/deleteDisable.gif'/>";
							}
						}
					} ],
					store : aggrCvlanStore
				});
			} else {
				aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
			}
			jQuery("#aggregationCvlanId").one("focus", function() {
				jQuery("#aggregationCvlanId").keyup(function() {
					var data = new Array()
					aggrCvlanText = jQuery("#aggregationCvlanId").val() // 在监听的事件发生时更新text
					text = $("#aggregationCvlanId").val() // 在监听的事件发生时更新text
					var j = 0
					var vidList = new Array()
					vidList = changeToArray()
					// 非空验证
					if (aggrCvlanText == null || aggrCvlanText == "" || aggrCvlanText == undefined) {
						// 为空，灰掉添加按钮
						$("#aggrSubmit").attr("disabled", true)
						$("#aggrSubmit").mouseout()
					} else {
						// 激活 添加按钮
						$("#aggrSubmit").attr("disabled", false)
						// 输入验证
						if (!checkedInputList("aggregationCvlanId")) {
							// 灰掉submit按钮
							$("#aggrSubmit").attr("disabled", true)
							$("#aggrSubmit").mouseout()
							return
						}
					}
					// 循环刷选
					jQuery.each(aggrCvlanData[aggrSvid], function(i, n) {
						var tmp = "" + n[0]
						if (exactFlag) {
							searchShowFlag = tmp.exactMatchWith(vidList)
						} else {
							searchShowFlag = tmp.matchWith(vidList)
						}
						if (aggrCvlanText == null || aggrCvlanText == "" || searchShowFlag) {
							data[j] = new Array()
							data[j][0] = n[0]
							j++;
						}
					})
					j = 0; // empty j
					aggrCvlanStore.loadData(data)
				});
			});
		}
	});
}

function getAggrCvlanIndex(aggrCvlanId) {
	return aggrCvlanStore.find("aggrCvlanId", aggrCvlanId)
}

function getAggrSvlanIndex(aggrSvlanId) {
	return aggrSvlanStore.find("aggrSvlanId", aggrSvlanId)
}

function aggrReady() {
	aggrSelectFirstRow()
	aggrGridClick()
	SvlanShowLength = aggrSvlanData.length
}

function aggrSelectFirstRow() {
	var model = Ext.getCmp("aggrSvlanGrid").getSelectionModel()
	model.selectFirstRow()
}

function aggrCvlanReload() {
	if (aggrSvlanGrid.getSelectionModel().getSelected() == null || aggrSvlanGrid.getSelectionModel().getSelected() == undefined) {
		aggrSvid = 0
		if (aggrSvlanData.length == 0) {
			$("#aggrlegend").text(I18N.VLAN.trunkVlanNull)
		} else {
			var tmpText = $("#aggregationSvlanId").val()
			$("#aggrlegend").text(String.format(I18N.VLAN.trunkedVlanList , tmpText))
		}
		aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
	} else {
		var record = aggrSvlanGrid.getSelectionModel().getSelected()
		aggrSvid = record.get('aggrSvlanId')
		if (aggrSvid == null || aggrSvid == undefined || aggrSvid == "") {
			// 防止空指针
			aggrSvid = 0
			aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
		} else {
			$("#aggrlegend").text(String.format(I18N.VLAN.trunkedVlanList , aggrSvid))
			aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
		}
	}
}

function aggrGridClick() {
	aggrCvlanReload()
	if (aggrSvid == 0) {
		$("#aggregationSvlanId").val("")
		$("#aggregationCvlanId").attr("disabled", true)
	} else {
		$("#aggregationSvlanId").val(aggrSvid)
		if(operationDevicePower){
			$("#aggregationCvlanId").attr("disabled", false)
		}
	}
}

function aggrAdd() {
	if (vlanConfigType == 'llid') {
		if (!tpidChanged("aggrTpid")) {
			return;
		}
	}
	aggrSvid = parseInt($("#aggregationSvlanId").val());
	text = $("#aggregationCvlanId").val();
	var vidList = new Array();
	vidList = changeToArray();
	var aggrFlag = true;
	for ( var i = 0; i < aggrSvlanData.length; i++) {
		if (parseInt($("#aggregationSvlanId").val()) == (aggrSvlanData[i][0])) {
			aggrFlag = false;
		}
	}
	// 验证CVID唯一性
	if (!aggrCvidChecked()) {
		return;
	}
	// TODO 待重构
	if (aggrFlag) {
		// 验证SVID
		if (!aggrSvidChecked()) {
			return;
		}
		// 验证通过
		// 临时存储aggrSvlanData数据
		var aggrSvlanDataTmp = aggrSvlanData.slice();
		var cosMode = $('#aggrCos').val();
		if ($('#aggrCosCheckbox2').attr('checked')) {
			cosMode = 'copy';
		}
		aggrSvlanDataTmp[aggrSvlanDataTmp.length] = [ $("#aggregationSvlanId").val(), cosMode, $('#aggrTpid').val() ];

		// 临时存储aggrCvlanData数据
		aggrCvlanData[aggrSvid] = new Array();
		var aggrCvlanDataTmp = new Array();
		for ( var u = 0; u < vidList.length; u++) {
			aggrCvlanDataTmp[aggrCvlanDataTmp.length] = [ vidList[u] ];
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingAggVlanRule , aggrSvid), 'ext-mb-waiting');
		// PON/LLID方式处理
		var url = '';
		var params = {};
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/addSvlanAggrRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrSvid : aggrSvid,
				aggrCvids : aggrCvlanDataTmp.toString()
			};
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/addLlidSvlanAggrRule.tv?r=' + Math.random();
			var cosMode = $('#aggrCosCheckbox2').attr('checked') ? 2 : 1;
			var cosValue = cosMode == 1 ? $('#aggrCos').val() : 0;
			var tpid = $('#aggrTpid').val().toLocaleLowerCase();
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrMac : selectedMac,
				aggrSvid : aggrSvid,
				aggrCvids : aggrCvlanDataTmp.toString(),
				cosMode : cosMode,
				cosValue : cosValue,
				tpid : tpid
			};
		}
		$.ajax({
			url : url,
			success : function() {
				//I18N.VLAN.alreadyAggredVlan : 已经将VLAN {0} 聚合为VLAN {1}
				window.parent.showMessageDlg(I18N.COMMON.tip, 
						String.format(I18N.VLAN.alreadyAggredVlan, changeToString(vidList).join(","), $("#aggregationSvlanId").val()));
				// 添加成功时，修改页面数据。
				aggrSvlanData = aggrSvlanDataTmp;
				aggrSvlanData.sort(function(a, b) {
					return parseInt(a) - parseInt(b);
				});
				aggrSvlanStore.loadData(aggrSvlanData);
				aggrCvlanData[aggrSvid] = aggrCvlanDataTmp;
				// 排序后选中需修改
				var tempRowNum = -1;
				for ( var k = 0; k < aggrSvlanData.length; k++) {
					if (aggrSvlanData[k][0] == aggrSvid) {
						tempRowNum = k;
						break;
					}
				}
				if (tempRowNum < 0) {
					aggrSvid = aggrSvlanData[0][0];
					tempRowNum = 0;
				}
				aggrSvlanGrid.selModel.selectRow(tempRowNum, true);
				aggrSvlanGrid.getView().focusRow(tempRowNum);
				aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
				loadScvidData();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addAggVlanRuleError ,aggrSvid))
			},
			data : params
		});

		aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
		$("#aggrSubmit").attr("disabled", true);
		$("#aggrSubmit").mouseout();
		$("#aggregationCvlanId").val("");
	} else {
		// 验证SVID
		if (!aggrSvidChecked()) {
			return;
		}
		// 验证通过
		// 临时存储aggrCvlanData数据
		var aggrCvlanDataTmp = aggrCvlanData[aggrSvid].slice();
		for ( var u = 0; u < vidList.length; u++) {
			aggrCvlanDataTmp[aggrCvlanDataTmp.length] = [ vidList[u] ];
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingAggVlanRule, aggrSvid), 'ext-mb-waiting')
		// PON/LLID方式处理
		var url = '';
		var params = {};
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/modifyCvlanAggrRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrSvid : aggrSvid,
				aggrCvids : aggrCvlanDataTmp.toString()
			};
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/modifyLlidCvlanAggrRule.tv?r=' + Math.random();
			var cosMode = $('#aggrCosCheckbox2').attr('checked') ? 2 : 1;
			var cosValue = cosMode == 1 ? $('#aggrCos').val() : 0;
			var tpid = $('#aggrTpid').val().toLocaleLowerCase();
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrMac : selectedMac,
				aggrSvid : aggrSvid,
				aggrCvids : aggrCvlanDataTmp.toString(),
				cosMode : cosMode,
				cosValue : cosValue,
				tpid : tpid
			};
		}
		$.ajax({
			url : url,
			success : function() {
				//I18N.VLAN.alreadyAggredVlan : 已经将VLAN {0} 聚合为VLAN {1}
				window.parent.showMessageDlg(I18N.COMMON.tip, 
						String.format(I18N.VLAN.alreadyAggredVlan, changeToString(vidList).join(","), $("#aggregationSvlanId").val()));
				// 添加成功时，修改页面数据。
				aggrCvlanData[aggrSvid] = aggrCvlanDataTmp;
				aggrCvlanData[aggrSvid].sort(function(a, b) {
					return parseInt(a) - parseInt(b);
				});
				aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
				loadScvidData();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addAggVlanRuleError ,aggrSvid))
			},
			data : params
		});

		aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
		$("#aggrSubmit").attr("disabled", true);
		$("#aggrSubmit").mouseout();
		$("#aggregationCvlanId").val("");
	}
}

function deleteSvlanAggrRule(aggrSvid) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanAggRule , aggrSvid), function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingAggRule , aggrSvid) , 'ext-mb-waiting')
		// PON/LLID方式处理
		var url = ''
		var params = {}
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/deleteSvlanAggrRule.tv?r=' + Math.random()
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrSvid : aggrSvid
			}
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/deleteLlidSvlanAggrRule.tv?r=' + Math.random()
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrMac : selectedMac,
				aggrSvid : aggrSvid
			}
		}
		$.ajax({
			url : url,
			success : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delAggRuleOk , aggrSvid))
				// 删除成功时，修改页面数据。
				aggrCvlanData[aggrSvid] = new Array();
				selectedIndex = getAggrSvlanIndex(aggrSvid);
				if(selectedIndex != -1){
					aggrSvlanData.splice(selectedIndex,1);
				}
				aggrSvlanStore.loadData(aggrSvlanData);
				if(aggrSvlanData > 0){
					aggrSvlanGrid.selModel.selectRow(0, true);
				}
				aggrCvlanReload();
				loadScvidData();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delAggRuleError , aggrSvid))
			},
			data : params
		});
	});
}
function deleteCvlanAggrRule(svidAggrId, cvidAggrId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelAggRule , cvidAggrId , svidAggrId ), function(type) {
		if (type == 'no') {
			return;
		}
		var aggrCvlanDataTmp = aggrCvlanData[svidAggrId].slice();
		var cosMode = $('#aggrCosCheckbox2').attr('checked') ? 2 : 1;
		selectedCIndex = getAggrCvlanIndex(cvidAggrId);
		if(selectedCIndex != -1){
			aggrCvlanDataTmp.splice(selectedCIndex,1);
		}
		window.top.showWaitingDlg(I18N.COMMON.wait,  String.format(I18N.VLAN.delingVlanAggRule , cvidAggrId ,svidAggrId ), 'ext-mb-waiting')
		// PON/LLID方式处理
		var url = ''
		var params = {}
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/modifyCvlanAggrRule.tv?r=' + Math.random()
			params = {
				entityId : entityId,
				ponId : ponId,
				aggrSvid : svidAggrId,
				aggrCvids : aggrCvlanDataTmp.toString()
			};
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/modifyLlidCvlanAggrRule.tv?r=' + Math.random()
			params = {
				entityId : entityId,
				ponId : ponId,
				cosMode : cosMode,
				aggrMac : selectedMac,
				aggrSvid : svidAggrId,
				aggrCvids : aggrCvlanDataTmp.toString()
			}
		}
		$.ajax({
			url : url,
			success : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanAggRuleOk ,  cvidAggrId, svidAggrId))
				// 删除成功时，修改页面数据
				aggrCvlanData[aggrSvid] = aggrCvlanDataTmp
				aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
				if (aggrCvlanData[aggrSvid].length == 0) {
					aggrSvlanData.splice(aggrSvlanStore.indexOf(aggrSvlanGrid.getSelectionModel().getSelected()), 1)
					aggrSvlanStore.loadData(aggrSvlanData)
					if (aggrSvlanData.length > 0) {
						var sel = aggrSvlanGrid.getSelectionModel()
						sel.selectRow(0, true)
						aggrSvid = sel.getSelected().get('aggrSvlanId')
						aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
					}
				}
				loadScvidData()
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanAggRuleError , cvidAggrId, svidAggrId))
			},
			data : params
		});
	});
}

// 当cvlan删除完之后aggrSvlan的删除
function aggrSvlanDelete() {
	aggrSvlanData.splice(getAggrSvlanIndex(aggrSvid),1);
	aggrSvlanStore.loadData(aggrSvlanData);
	var model = Ext.getCmp("aggrSvlanGrid").getSelectionModel();
	model.selectFirstRow();
	aggrGridClick();
}

function aggrCvidChecked() {
	// CVID的唯一性
	text = $("#aggregationCvlanId").val();
	var aggrCidList = new Array();
	aggrCidList = changeToArray();
	for ( var k = 0; k < aggrCidList.length; k++) {
		var aggrCid = aggrCidList[k];
		for ( var j = 0; j < cvids.length; j++) {
			var mode = cvids[j][1];
			var modeStr = vlanModeString[mode];
			var mac = cvids[j][2];
			if(mac == "" || mac == null || mac == undefined){
				mac = "0";
			}
			if (vlanConfigType == 'pon') {
				if (cvids[j][0] == aggrCid) {
					if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || (mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)) {
						if (mac != "0") {
							window.parent.showMessageDlg(I18N.COMMON.tip,  String.format(I18N.VLAN.ponVCfgTypeTip ,  {vlan:aggrCid , mac:mac , mode:modeStr}))
						} else {
							window.parent.showMessageDlg(I18N.COMMON.tip,  String.format(I18N.VLAN.ponVCfgTypeTip2 , {vlan:aggrCid , mode:modeStr}))
						}
						return false;
					}
				}
			}else if(vlanConfigType == 'llid'){
				if (cvids[j][0] == aggrCid) {
					if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || ((mac == selectedMac) && ((mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)))) {
						if (mac != "0") {
							window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip ,  {vlan:aggrCid , mac:mac , mode:modeStr}))
						} else {
							window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip , {vlan:aggrCid , mode:modeStr}))
						}
						return false;
					}
				}
			}
		}
	}
	return true;
}

function aggrSvidChecked() {
	var aggrSid = parseInt($("#aggregationSvlanId").val());
	var count = 0;
	// SVID的存在性
	for ( var i = 0; i < svids.length; i++) {
		if (svids[i][0] == aggrSid) {
			count++;
			// SVID的合法性
			var mode = svids[i][1];
			var modeStr = vlanModeString[mode];
			var mac = svids[i][2];
			if(mac == null || mac == undefined || mac == ""){
				mac = "0";
			}
			if (vlanConfigType == 'pon') {
				if (mode == 1 || mode == 3 || mode == 5 || mode == 6 || mode == 7) {
					if(mac != "0"){
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip, 
								{vlan:aggrSid , mac:mac , mode:modeStr}));
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip2, 
								{vlan:aggrSid , mode:modeStr}));
					}
					return false;
				}
			} else if (vlanConfigType == 'llid') {
				if ((mode > 0 && mode < 4) || ((mode == 5 || mode == 7) && (mac == selectedMac || mac == "0"))) {
					if (mac != "0") {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip,  
								{vlan:aggrSid , mac:mac , mode:modeStr}));
					} else {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip2, 
								{vlan:aggrSid , mode:modeStr}));
					}
					return false;
				}
			}
		}
	}
	if (count == 0) {
		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.notExist , aggrSid));
		return false;
	} else {
		count = 0;
		return true;
	}
}

// aggr的聚合后VLAN ID 输入框change
function aggregationSvlanIdChange() {
	$("#aggregationCvlanId").val("");
	$("#aggrSubmit").attr("disabled", true).mouseout();
}

// aggr的聚合前VLAN ID onfocus
function aggregationCvlanIdOn() {
	var aggrFlag = true;
	for ( var i = 0; i < aggrSvlanData.length; i++) {
		if (parseInt($("#aggregationSvlanId").val()) == (aggrSvlanData[i][0])) {
			aggrFlag = false;
		}
	}
	if (aggrFlag) {
		aggrSvid = 0;
	}
	aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
	if (aggrSvid == 0) {
		var tmpText = $("#aggregationSvlanId").val();
		$("#qinqlegend").text(String.format(I18N.VLAN.addOuterVlanList, tmpText));
	}
}

// 变红操作
function aggrSvlanChangeRed(value, a, b, c) {
	if (value.toString().redMatchWith($("#aggregationSvlanId").val())) {
		$("#aggrSubmit").attr("disabled", true).mouseout();
		aggrSvlanGrid.selModel.selectRow(0, true);
		if (vlanConfigType == 'llid') {
			b.data.aggrCos = "<font color='red'>" + b.data.aggrCos + "</font>";
			b.data.aggrTpid = "<font color='red'>" + b.data.aggrTpid + "</font>";
		}
		return "<font color='red'>" + value + "</font>";
	} else {
		return value;
	}
}

function aggrCvlanChangeRed(value, a, b, c) {
	if ($("#aggregationCvlanId").val() == null || $("#aggregationCvlanId").val() == "" || $("#aggregationCvlanId").val() == undefined) {
		return value;
	} else {
		if (value.toString().exactMatchWith(changeToArray())) {
			$("#aggrSubmit").attr("disabled", true).mouseout();
			return "<font color='red'>" + value + "</font>";
		} else {
			return value;
		}
	}
}