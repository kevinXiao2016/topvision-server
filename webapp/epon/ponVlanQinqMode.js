// QinQ
var qinqSvlanGrid;
var qinqCvlanGrid;
var qinqSvlanStore;
var qinqCvlanStore;
var qinqSvlanData = new Array();// [[qinqSvid],[qinqSvid],[qinqSvid]]
var qinqCvlanData = new Array();// 序号为qinqSvid [[],[],[[start,end,cosMode,cos],[start,end,cosMode,cos]],[],[[start,end,cosMode,cos]]]
var qinqSvid = 0;
qinqCvlanData[0] = new Array();
var qinqSvlanText;
var qinqCvlanText;

function loadQinqMode() {
	// PON/LLID方式处理
	var url = '';
	var qinqColumns;
	if (vlanConfigType == 'pon') {
		url = '/epon/vlan/loadQinqData.tv?ponId=' + ponId + '&r=' + Math.random();
		qinqColumns = [ {
			header : I18N.VLAN.vlanStart + " ID",
			width : 85,
			dataIndex : "qinqCvlanStartId",
			align : "center",
			renderer : qinqCvlanChangeRed
		}, {
			header : I18N.VLAN.vlanTermi + " ID",
			width : 85,
			dataIndex : "qinqCvlanEndId",
			align : "center"
		}, {
			header : I18N.VLAN.vlanPriority,
			width : 85,
			dataIndex : "qinqCvlanCos",
			align : "center",
			renderer:qinqPriRender
		}, {
			header : I18N.COMMON.manu,
			width : 60,
			dataIndex : "id",
			align : "center",
			renderer : function(value, cellmeta, record) {
				if(record.data.qinqCvlanEndId.length > 4){
					record.data.qinqCvlanEndId = parseInt(record.data.qinqCvlanEndId.substring(18).split("<")[0]);
				}
				if(record.data.qinqCvlanStartId.length > 4){
					record.data.qinqCvlanStartId = parseInt(record.data.qinqCvlanStartId.substring(18).split("<")[0]);
				}
				if(operationDevicePower){
					return "<img src='/images/delete.gif' onclick='deleteCvlanQinqRule(" + record.data.qinqCvlanStartId + ", " + record.data.qinqCvlanEndId + ")'/>";
				}else{
					return "<img src='/images/deleteDisable.gif'/>";
				}
			}
		} ];
	} else if (vlanConfigType == 'llid') {
		url = '/epon/vlan/loadLlidQinqData.tv?ponId=' + ponId + '&qinqMac=' + selectedMac + '&r=' + Math.random();
		qinqColumns = [ {
			header : I18N.VLAN.startVid,
			width : 65,
			dataIndex : "qinqCvlanStartId",
			align : "center",
			renderer : qinqCvlanChangeRed
		}, {
			header : I18N.VLAN.endVid,
			width : 65,
			dataIndex : "qinqCvlanEndId",
			align : "center"
		}, {
			header : I18N.VLAN.vlanPriority,
			width : 85,
			dataIndex : "qinqCvlanCos",
			align : "center"
		}, {
			header : "TPID",
			width : 60,
			dataIndex : "qinqCvlanTpid",
			align : "center"
		}, {
			header : I18N.COMMON.manu,
			width : 40,
			dataIndex : "id",
			align : "center",
			renderer : function(value, cellmeta, record) {
				if(record.data.qinqCvlanEndId.length > 4)
					record.data.qinqCvlanEndId = parseInt(record.data.qinqCvlanEndId.substring(18).split("<")[0]);
				if(record.data.qinqCvlanStartId.length > 4)
					record.data.qinqCvlanStartId = parseInt(record.data.qinqCvlanStartId.substring(18).split("<")[0]);
				if(operationDevicePower){
					return "<img src='/images/delete.gif' onclick='deleteCvlanQinqRule(" + record.data.qinqCvlanStartId + ", " + record.data.qinqCvlanEndId + ")'/>";
				}else{
					return "<img src='/images/deleteDisable.gif'/>";
				}
			}
		}]
	}
	var qinqStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : url
		}),
		reader : new Ext.data.ArrayReader({
			id : 0
		}, Ext.data.Record.create([ {
			name : 'qinqSvlanIdList'
		}, {
			name : 'qinqCvlanIdList'
		} ]))
	})
	qinqStore.load({
		callback : function(records, options, success) {
			// 数据加载完毕更新数组中的值
			qinqSvlanData = new Array()
			qinqCvlanData = new Array()
			qinqCvlanData[0] = new Array()
			$.each(records, function(i, n) {
				qinqSvlanData[i] = n.json[0]
				qinqCvlanData[n.json[0][0]] = new Array()
				for ( var j = 0; j < n.json[1].length; j++) {
					qinqCvlanData[n.json[0][0]][j] = new Array()
					qinqCvlanData[n.json[0][0]][j][0] = n.json[1][j][0]
					qinqCvlanData[n.json[0][0]][j][1] = n.json[1][j][1]
					if (n.json[1][j][2] == 2) {
						qinqCvlanData[n.json[0][0]][j][2] = 'copy'
					} else {
						qinqCvlanData[n.json[0][0]][j][2] = n.json[1][j][3]
					}
					if (n.json[1][j][4]) {
						qinqCvlanData[n.json[0][0]][j][3] = n.json[1][j][4]
					}
				}
			});
			// 初始时选中第一行
			if (qinqSvlanData[0] == null || qinqSvlanData[0] == "" || qinqSvlanData[0] == undefined) {
				qinqSvid = 0
			} else {
				qinqSvid = qinqSvlanData[0]
			}
			if (qinqSvlanGrid == null || qinqSvlanGrid == undefined) {
				qinqSvlanStore = new Ext.data.SimpleStore({
					data : qinqSvlanData,
					fields : [ "qinqSvlanId" ]
				})
				qinqSvlanGrid = new Ext.grid.GridPanel({
					stripeRows:true,
			   		region: "center",
			   		bodyCssClass: 'normalTable',
			   		viewConfig:{forceFit: true},
					id : 'qinqSvlanGrid',
					renderTo : 'qinqSvlan_grid',
					width : 142,
					height : 210,
					frame : false,
					border : false,
					selModel : new Ext.grid.RowSelectionModel({
						singleSelect : true
					}),
					autoScroll : true,
					columns : [ {
						header : I18N.VLAN.outerVlan + " ID",
						width : 120,
						dataIndex : "qinqSvlanId",
						align : "center",
						renderer : qinqSvlanChangeRed
					} ],
					store : qinqSvlanStore,
					listeners : {
						"click" : {
							fn : qinqGridClick,
							scope : this
						},
						'viewready' : {
							fn : qinqReady,
							scope : this
						}
					}
				});
			} else {
				qinqSvlanStore.loadData(qinqSvlanData);
			}
			qinqSvlanGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
				var tmpId = grid.selections.get(0).data.qinqSvlanId;
				if (tmpId) {
					selectedIndex = getQinqSvlanIndex(tmpId);
				} else {
					selectedIndex = 0;
				}
			})
			jQuery("#qinqOuterVlanId").one("focus", function() {
				jQuery("#qinqOuterVlanId").keyup(function() {
					var data = new Array();
					qinqSvlanText = jQuery("#qinqOuterVlanId").val();// 在监听的事件发生时更新text
					var j = 0;
					$("#qinqInnerVlanStartId").val("");
					$("#qinqInnerVlanEndId").val("");
					$("#qinqSubmit").attr("disabled", true);
					$("#qinqSubmit").mouseout();
					// 非空验证
					if (qinqSvlanText == null || qinqSvlanText == "" || qinqSvlanText == undefined) {
						// 为空，灰掉CVLANinput
						$("#qinqInnerVlanStartId").attr("disabled", true);
						$("#qinqInnerVlanEndId").attr("disabled", true);
					} else {
						// 激活 CVLANinput
						$("#qinqInnerVlanStartId").attr("disabled", false);
						$("#qinqInnerVlanEndId").attr("disabled", false);
						// 输入验证
						if (!checkedInput("qinqOuterVlanId")) {
							// 灰掉CVLANinput和submit按钮
							$("#qinqInnerVlanStartId").attr("disabled", true);
							$("#qinqInnerVlanEndId").attr("disabled", true);
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
							return;
						}
					}
					// 循环刷选
					jQuery.each(qinqSvlanData, function(i, n) {
						var tmp = "" + n[0];
						if (qinqSvlanText == null || qinqSvlanText == "" || tmp.startWith(qinqSvlanText)) {
							data[j] = n;
							j++;
						}
					});
					SvlanShowLength = j;
					j = 0; // empty j
					qinqSvlanStore.loadData(data);
					qinqSelectFirstRow();
					qinqCvlanReload();
				});
			});

			// QinQ CVLAN
			if (qinqCvlanGrid == null || qinqCvlanGrid == undefined) {
				if (qinqCvlanData[qinqSvid] == null || qinqCvlanData[qinqSvid] == undefined) {
					qinqSvid = 0;
				}
				qinqCvlanStore = new Ext.data.SimpleStore({
					data : qinqCvlanData[qinqSvid],
					fields : [ "qinqCvlanStartId", "qinqCvlanEndId", "qinqCvlanCos", "qinqCvlanTpid" ]
				});
				qinqCvlanGrid = new Ext.grid.GridPanel({
					stripeRows:true,
			   		region: "center",
			   		bodyCssClass: 'normalTable',
			   		viewConfig:{forceFit: true},
					renderTo : 'qinqCvlan_grid',
					width : 342,
					height : 210,
					frame : false,
					border : false,
					autoScroll : true,
					selModel : new Ext.grid.RowSelectionModel({
						singleSelect : true
					}),
					columns : qinqColumns,
					store : qinqCvlanStore
				});
			} else {
				if (qinqCvlanData[qinqSvid] == null || qinqCvlanData[qinqSvid] == undefined) {
					qinqSvid = 0;
				}
				qinqCvlanStore.loadData(qinqCvlanData[qinqSvid]);
			}
			qinqCvlanGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
				var tmpId = grid.selections.get(0).data.qinqCvlanStartId;
				if (tmpId) {
					selectedCIndex = getQinqCvlanIndex(tmpId);
				} else {
					selectedCIndex = 0;
				}
			});
			jQuery("#qinqInnerVlanStartId").one("focus", function() {
				$("#qinqInnerVlanStartId").keyup(function() {
					text = $("#qinqInnerVlanStartId").val();
					startText = text;
					var showFlag = false;// 筛选方式的标记
					var startNullFlag = false;// 筛选方式的标记
					var endNullFlag = false;// 筛选方式的标记
					endText = $("#qinqInnerVlanEndId").val();
					if (startText == null || startText == undefined || startText == "") {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
						startNullFlag = true;
					} else {
						if (!checkedInput("qinqInnerVlanStartId")) {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
							return;
						} else if (endText == null || endText == "" || endText == undefined) {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
							endNullFlag = true;
						} else if (checkedInput("qinqInnerVlanEndId")) {
							$("#qinqSubmit").attr("disabled", false);
						} else {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
							endNullFlag = true;
						}
					}
					if (parseInt(startText) > parseInt(endText)) {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
					}
					var data = new Array();
					var j = 0;
					$.each(qinqCvlanData[qinqSvid], function(i, n) {
						var tmp1 = "" + n[0];
						var tmp2 = "" + n[1];
						if (!startNullFlag && !endNullFlag) {
							showFlag = tmp2.qinqStartMatch(startText) && tmp1.qinqEndMatch(endText);
						} else if (!startNullFlag && endNullFlag) {
							showFlag = tmp2.qinqStartMatch(startText);
						} else if (startNullFlag && endNullFlag) {
							showFlag = true;
						}
						if (startText == null || startText == "" || showFlag) {
							data[j] = new Array();
							data[j][0] = n[0];
							data[j][1] = n[1];
							data[j][2] = n[2];
							data[j][3] = n[3];
							j++;
						}
					});
					if (j != 0) {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
					} else {
						j = 0;
					}
					qinqCvlanStore.loadData(data);
				});
			});
			jQuery("#qinqInnerVlanEndId").one("focus", function() {
				$("#qinqInnerVlanEndId").keyup(function() {
					text = $("#qinqInnerVlanEndId").val();
					endText = text;
					var showFlag = false;// 筛选方式的标记
					var startNullFlag = false;// 筛选方式的标记
					var endNullFlag = false;// 筛选方式的标记
					startText = $("#qinqInnerVlanStartId").val();
					if (endText == null || endText == undefined || endText == "") {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
					} else {
						if (!checkedInput("qinqInnerVlanEndId")) {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
							return;
						} else if (startText == null || startText == "" || startText == undefined) {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
						} else if (checkedInput("qinqInnerVlanStartId")) {
							$("#qinqSubmit").attr("disabled", false);
						} else {
							$("#qinqSubmit").attr("disabled", true);
							$("#qinqSubmit").mouseout();
						}
					}
					if (parseInt(startText) > parseInt(endText)) {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
					}
					var data = new Array();
					var j = 0;
					$.each(qinqCvlanData[qinqSvid], function(i, n) {
						var tmp1 = "" + n[0];
						var tmp2 = "" + n[1];
						if (!startNullFlag && !endNullFlag) {
							showFlag = tmp2.qinqStartMatch(startText) && tmp1.qinqEndMatch(endText);
						} else if (startNullFlag && !endNullFlag) {
							showFlag = tmp1.qinqEndMatch(endText);
						} else if (startNullFlag && endNullFlag) {
							showFlag = true;
						}
						if (endText == null || endText == "" || showFlag) {
							data[j] = new Array();
							data[j][0] = n[0];
							data[j][1] = n[1];
							data[j][2] = n[2];
							data[j][3] = n[3];
							j++;
						}
					});
					if (j != 0) {
						$("#qinqSubmit").attr("disabled", true);
						$("#qinqSubmit").mouseout();
					} else {
						j = 0;
					}
					qinqCvlanStore.loadData(data);
				});
			});
		}
	});
}

function qinqPriRender(value, a, b, c){
	if(value==128){
		return 'copy';
	}
	return value;
}

function getQinqSvlanIndex(qinqSvlanId) {
	var tmpIndex = -1;
	for ( var i = 0; i < qinqSvlanData.length; i++) {
		if (qinqSvlanData[i][0] == qinqSvlanId) {
			tmpIndex = i;
			break;
		}
	}
	return tmpIndex;
}
function getQinqCvlanIndex(qinqCvlanStartId) {
	var tmpIndex = -1;
	for ( var i = 0; i < qinqCvlanData.length; i++) {
		if (qinqCvlanData[qinqSvid][i][0] == qinqCvlanStartId) {
			tmpIndex = i;
			break;
		}
	}
	return tmpIndex;
}

function qinqReady() {
	qinqSelectFirstRow();
	qinqGridClick();
	SvlanShowLength = qinqSvlanData.length;
}

function qinqSelectFirstRow() {
	var model = Ext.getCmp("qinqSvlanGrid").getSelectionModel();
	model.selectFirstRow();
}

function qinqCvlanReload() {
	if(qinqSvlanGrid.getSelectionModel().getSelected() == null || qinqSvlanGrid.getSelectionModel().getSelected() == undefined) {
		qinqSvid = 0;
		if (qinqSvlanData.length == 0) {
			$("#qinqlegend").text(I18N.VLAN.qinqVlanNull)
		} else {
			var tmpText = $("#qinqOuterVlanId").val();
			$("#qinqlegend").text(String.format(I18N.VLAN.addOuterVlanList , tmpText))
		}
	}else{
		var record = qinqSvlanGrid.getSelectionModel().getSelected();
		qinqSvid = record.get('qinqSvlanId');
		if (qinqSvid == null || qinqSvid == undefined || qinqSvid == "") {
			qinqSvid = 0;
		} else {
			$("#qinqlegend").text(String.format(I18N.VLAN.addOuterVlanList , qinqSvid))
		}
	}
	qinqCvlanStore.loadData(qinqCvlanData[qinqSvid]);
}

function qinqGridClick() {
	qinqCvlanReload();
	if (qinqSvid == 0) {
		$("#qinqOuterVlanId").val("");
		$("#qinqInnerVlanStratId").attr("disabled", true);
		$("#qinqInnerVlanEndId").attr("disabled", true);
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
	} else {
		$("#qinqOuterVlanId").val(qinqSvid);
		$("#qinqInnerVlanStratId").attr("disabled", false);
		$("#qinqInnerVlanEndId").attr("disabled", false);
	}
}

function qinqCosModeClick() {
	if ($("#qinqCosCheckbox1").attr("checked")) {
		$("#qinqCos").attr("disabled", false);
	} else if ($("#qinqCosCheckbox2").attr("checked")) {
		$("#qinqCos").val("0");
		$("#qinqCos").attr("disabled", true);
	}
}

function qinqAdd() {
	if (vlanConfigType == 'llid') {
		if (!tpidChanged("qinqTpid")) {
			return;
		}
	}
	var startId = $("#qinqInnerVlanStartId").val();
	var endId = $("#qinqInnerVlanEndId").val();
	qinqSvid = parseInt($("#qinqOuterVlanId").val());
	var qinqSTagCosDetermine;
	var qinqSTagCosNewValue = $("#qinqCos").val();
	var tpid = $("#qinqTpid").val();
	if ($("#qinqCosCheckbox1").attr("checked")) {
		qinqSTagCosDetermine = 1;
	} else {
		qinqSTagCosDetermine = 2;
	}
	if ($("#qinqInnerVlanStartId").val() == null || $("#qinqInnerVlanStartId").val() == undefined || $("#qinqInnerVlanStartId").val() == "") {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
		return;
	} else {
		var qinqFlag = true;
		for ( var i = 0; i < qinqSvlanData.length; i++) {
			if(qinqSvlanData[i] != null && qinqSvlanData[i] != "" && qinqSvlanData[i] != undefined){
				if (parseInt($("#qinqOuterVlanId").val()) == qinqSvlanData[i]) {
					qinqFlag = false;
					break;
				}
			}
		}
		// 验证CVID唯一性
		if (!qinqCvidChecked()) {
			return;
		}
		var tempCos = 'copy';//jsp使用
		if(qinqSTagCosDetermine == 1){
			tempCos = qinqSTagCosNewValue;
		}
		// TODO 代码待重构
		if (qinqFlag) {
			// 验证SVID
			if (!qinqSvidChecked()) {
				return;
			}
			var qinqSvlanDataTmp = qinqSvlanData.slice();
			qinqSvlanDataTmp[qinqSvlanDataTmp.length] = [parseInt($("#qinqOuterVlanId").val())];
			// 临时存储qinqCvlanData数据
			var qinqCvlanDataTmp = new Array();
			qinqCvlanDataTmp = [ startId, endId, tempCos, tpid ];
			// 验证通过
			window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingOuterVlan , startId ,endId ,qinqSvid ), 'ext-mb-waiting');
			// PON/LLID方式处理
			var url = '';
			var params = {};
			if (vlanConfigType == 'pon') {
				url = '/epon/vlan/addQinqRule.tv?r=' + Math.random();
				params = {
					entityId : entityId,
					ponId : ponId,
					qinqSvid : qinqSvid,
					qinqStartCvid : startId,
					qinqEndCvid : endId,
					qinqSTagCosDetermine : qinqSTagCosDetermine,
					qinqSTagCosNewValue : qinqSTagCosNewValue
				};
			} else if (vlanConfigType == 'llid') {
				var tpid = $("#qinqTpid").val().toLocaleLowerCase();
				url = '/epon/vlan/addLlidQinqRule.tv?r=' + Math.random();
				params = {
					entityId : entityId,
					ponId : ponId,
					qinqMac : selectedMac,
					qinqSvid : qinqSvid,
					qinqStartCvid : startId,
					qinqEndCvid : endId,
					cosMode : qinqSTagCosDetermine,
					cosValue : qinqSTagCosNewValue,
					tpid : tpid
				};
			}
			$.ajax({
				url : url,
				success : function() {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addOuterVlanOk , startId ,endId ,qinqSvid));
                    // 添加成功时，修改页面数据。
                    qinqSvlanData = qinqSvlanDataTmp;
                    qinqSvlanData.sort(function(a,b){
                        return parseInt(a) - parseInt(b);
                    });
                    qinqSvlanStore.loadData(qinqSvlanData);
                    qinqCvlanData[qinqSvid] = new Array();
                    qinqCvlanData[qinqSvid][0] = qinqCvlanDataTmp;
                    //排序后选中需修改
                    var tempRowNum = -1;
                    for(var k=0; k<qinqSvlanData.length; k++){
                        if(qinqSvlanData[k][0] == qinqSvid){
                            tempRowNum = k;
                        }
                    }
                    if(tempRowNum < 0){
                        qinqSvid = qinqSvlanData[0][0];
                        tempRowNum = 0;
                    }
                    qinqSvlanGrid.selModel.selectRow(tempRowNum, true);
                    qinqSvlanGrid.getView().focusRow(tempRowNum);
                    qinqCvlanStore.loadData(qinqCvlanData[qinqSvid]);
                    loadScvidData();
				},
				error : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addOuterVlanError , startId ,endId ,qinqSvid))
				},
				data : params
			});
		} else {
			// 临时存储aggrCvlanData数据
			var qinqCvlanDataTmp = new Array();
			qinqCvlanDataTmp = [ startId, endId, tempCos, tpid ];
			// 验证通过
			window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingOuterVlan ,startId ,endId ,qinqSvid), 'ext-mb-waiting');
			// PON/LLID方式处理
			var url = '';
			var params = {};
			if (vlanConfigType == 'pon') {
				url = '/epon/vlan/addQinqRule.tv?r=' + Math.random();
				params = {
					entityId : entityId,
					ponId : ponId,
					qinqSvid : qinqSvid,
					qinqStartCvid : startId,
					qinqEndCvid : endId,
					qinqSTagCosDetermine : qinqSTagCosDetermine,
					qinqSTagCosNewValue : $("#qinqCos").val()
				};
			} else if (vlanConfigType == 'llid') {
				var tpid = $("#qinqTpid").val().toLocaleLowerCase();
				url = '/epon/vlan/addLlidQinqRule.tv?r=' + Math.random();
				params = {
					entityId : entityId,
					ponId : ponId,
					qinqMac : selectedMac,
					qinqSvid : qinqSvid,
					qinqStartCvid : startId,
					qinqEndCvid : endId,
					cosMode : qinqSTagCosDetermine,
					cosValue : qinqSTagCosNewValue,
					tpid : tpid
				};
			}
			$.ajax({
				url : url,
				success : function() {
                    window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addSetOuterVlanOk ,startId ,endId ,$("#qinqOuterVlanId").val()))
                    // 添加成功时，修改页面数据。
                    qinqCvlanData[qinqSvid][qinqCvlanData[qinqSvid].length] = qinqCvlanDataTmp;
                    qinqCvlanData[qinqSvid].sort(function(a,b){
                        return parseInt(a[0]) - parseInt(b[0]);
                    });
                    qinqCvlanStore.loadData(qinqCvlanData[qinqSvid]);
                    loadScvidData();
				},
				error : function() {
					window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.addOuterVlanError ,startId ,endId ,qinqSvid))
				},
				data : params
			});
		}
	}
	$("#qinqSubmit").attr("disabled", true);
	$("#qinqSubmit").mouseout();
	$("#qinqCos").attr("disabled", true);
	$("#qinqInnerVlanStartId").val("");
	$("#qinqInnerVlanEndId").val("");
	$("#qinqCos").val("0");
	$("#qinqCosCheckbox2").attr("checked", "checked");
}

function deleteCvlanQinqRule(cvidQinqStart, cvidQinqEnd) {
	/*if (vlanConfigType == 'llid') {
		if (!tpidChanged("qinqTpid")) {
			return;
		}
	}*/
	var selectRecord = qinqCvlanGrid.getSelectionModel().getSelections();
	var selectNum = qinqCvlanGrid.getStore().indexOf(selectRecord[0]);
	//var selectNum = record.data.getSelectedIndexes();
	var svidQinq = qinqSvid;
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanQinq , cvidQinqStart,cvidQinqEnd , svidQinq ), function(type) {
		if(type == 'no'){
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingVlanQinq , cvidQinqStart , cvidQinqEnd , svidQinq ), 'ext-mb-waiting')
		
		// PON/LLID方式处理
		var url = '';
		var params = {};
		if (vlanConfigType == 'pon') {
			url = '/epon/vlan/deleteQinqRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				qinqStartCvid : cvidQinqStart,
				qinqEndCvid : cvidQinqEnd
			};
		} else if (vlanConfigType == 'llid') {
			url = '/epon/vlan/deleteLlidQinqRule.tv?r=' + Math.random();
			params = {
				entityId : entityId,
				ponId : ponId,
				qinqMac : selectedMac,
				qinqStartCvid : cvidQinqStart,
				qinqEndCvid : cvidQinqEnd
			};
		}
		$.ajax({
			url : url,
			success : function() {
                window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanQinqOk ,cvidQinqStart , cvidQinqEnd , svidQinq ))
                // 删除成功时，修改页面数据。
                qinqCvlanData[qinqSvid].splice(selectNum,1);
                if(qinqCvlanData[qinqSvid].length == 0){
                    qinqSvlanDelete();
                }else{
                    qinqCvlanStore.loadData(qinqCvlanData[qinqSvid]);
                }
                loadScvidData();
			},
			error : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delVlanQinqError ,cvidQinqStart , cvidQinqEnd , svidQinq ))
			},
			data : params
		});
	});
}

// 当cvlan删除完之后qinqSvlan的删除
function qinqSvlanDelete() {
	qinqSvlanData.splice(selectedIndex,1);
	qinqSvlanStore.loadData(qinqSvlanData);
	var model = Ext.getCmp("qinqSvlanGrid").getSelectionModel();
	model.selectFirstRow();
	qinqGridClick();
	loadScvidData();
}

function qinqCvidChecked() {
	var qinqStartCid = parseInt($("#qinqInnerVlanStartId").val());
	var qinqEndCid = parseInt($("#qinqInnerVlanEndId").val());
	// CVID的唯一性
	for ( var j = 0; j < cvids.length; j++) {
		var mode = cvids[j][1];
		var cid = cvids[j][0];
		var modeStr = vlanModeString[mode];
		var mac = cvids[j][2];
		if(mac == "" || mac == null || mac ==undefined){
			mac = "0";
		}
		if(vlanConfigType == 'pon'){
			if(!(cvids[j][0] < qinqStartCid) && !(cvids[j][0] > qinqEndCid)){
				if ((mode == 1) || (mode == 2) || (mode == 3) || (mode == 4) || (mode == 5) || (mode == 6) || (mode == 7) || (mode == 8)) {
					if(mac == "0"){
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip2 ,{vlan:cvids[j][0] , mode:modeStr}))
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.ponVCfgTypeTip , {vlan:cvids[j][0] , mac:mac , mode:modeStr}))
					}
					return false;
				}
			}
		}else if(vlanConfigType == 'llid'){
			if (!(cvids[j][0] < qinqStartCid) && !(cvids[j][0] > qinqEndCid)) {
				if((mode==1)||(mode==2)||(mode==3)||(mode==4) || ((mac==selectedMac) && ((mode==5)||(mode==6)||(mode==7)||(mode==8)))){
					if (mac != "0") {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip , {vlan:cid , mac:mac , mode:modeStr}))
					} else {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.llidVCfgTypeTip2 ,{vlan:cid , mode:modeStr}))
					}
					return false;
				}
			}
		}
	}
	return true;
}

function qinqSvidChecked() {
	var qinqSid = parseInt($("#qinqOuterVlanId").val());
	var count = 0;
	// SVID的存在性
	for ( var i = 0; i < svids.length; i++) {
		if (svids[i][0] == qinqSid) {
			count++;
			// SVID的合法性
			var mode = svids[i][1];
			var modeStr = vlanModeString[mode];
			var mac = svids[i][2];
			if(mac == null || mac == undefined || mac == ""){
				mac = "0";
			}
			if (vlanConfigType == 'pon') {
				if (mode == 3 || mode == 7) {
					if(mac != "0"){
						//I18N.VLAN.conflictTipQinQ : VLAN {0} 在本PON口{1}下已经配置了 {2} 规则,不能重复配置!
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictTipQinQ, qinqSid, mac, modeStr));
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictTipQinQ, qinqSid, "", modeStr));
					}
					return false;
				}
			} else if (vlanConfigType == 'llid') {
				if (mode == 3 || (mode == 7 && (mac == "0" || mac == selectedMac))) {
					if (mac != "0") {
						//I18N.VLAN.conflictTipQinQ : VLAN {0} 在本PON口{1}下已经配置了 {2} 规则,不能重复配置!
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictTipQinQ, qinqSid, mac, modeStr));
					} else {
						window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.conflictTipQinQ, qinqSid, "", modeStr));
					}
					return false;
				}
			}
		}
	}
	if (count == 0) {
		window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.notExist ,qinqSid))
		return false;
	} else {
		count = 0;
		return true;
	}
}

// qinq内层vlan输入框onfocus
function qinqInnerIdOn() {
	var qinqFlag = true;
	for ( var i = 0; i < qinqSvlanData.length; i++) {
		if (parseInt($("#qinqOuterVlanId").val()) == (qinqSvlanData[i][0])) {
			qinqFlag = false;
		}
	}
	if (qinqFlag) {
		qinqSvid = 0;
	}
	qinqCvlanReload();
	if (qinqSvid == 0) {
		var tmpText = $("#qinqOuterVlanId").val();
		$("#qinqlegend").text(String.format(I18N.VLAN.addOuterVlanList , tmpText))
	}
}
// QinQ的外层VLAN id 输入框change
function qinqOuterIdChange() {
	$("#qinqInnerVlanStartId").val("");
	$("#qinqInnerVlanEndId").val("");
	$("#qinqSubmit").attr("disabled", true);
	$("#qinqSubmit").mouseout();
}

function qinqSvlanChangeRed(value, a, b, c) {
	if (value.toString().redMatchWith($("#qinqOuterVlanId"))) {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
		qinqSvlanGrid.selModel.selectRow(0, true);
		return "<font color='red'>" + value + "</font>";
	} else {
		return value;
	}
}
function qinqCvlanChangeRed(value, a, b, c) {
	var startId = $("#qinqInnerVlanStartId").val();
	var endId = $("#qinqInnerVlanEndId").val();
	if (startId == null || startId == undefined || startId == "" || endId == null || endId == "" || endId == undefined) {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
	}
	var startFlag = value.toString().redMatchWith(startId);
	var endFlag = b.data.qinqCvlanEndId.toString().redMatchWith(endId);
	if (startFlag && endFlag) {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
		b.data.qinqCvlanEndId = "<font color='red'>" + b.data.qinqCvlanEndId + "</font>";
		b.data.qinqCvlanCos = "<font color='red'>" + b.data.qinqCvlanCos + "</font>";
		if (vlanConfigType == 'llid') {
			b.data.qinqCvlanTpid = "<font color='red'>" + b.data.qinqCvlanTpid + "</font>";
		}
		return "<font color='red'>" + value + "</font>";
	} else if (startFlag) {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
		return "<font color='red'>" + value + "</font>";
	} else if (endFlag) {
		$("#qinqSubmit").attr("disabled", true);
		$("#qinqSubmit").mouseout();
		b.data.qinqCvlanEndId = "<font color='red'>" + b.data.qinqCvlanEndId + "</font>";
		return value;
	} else {
		return value;
	}
}