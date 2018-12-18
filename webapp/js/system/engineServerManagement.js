//全局变量
var store = null;
var grid = null;
var sm = null;
var cm = null;
var tbar = null;
// 其它部分菜单操作
var engineMenu = null;

// 连接状态列的renderer操作
function linkRender(value, p, record) {
	var str = null;
	if (record.get('linkStatus') == '2') {// 不连接
		str = '<img nm3kTip="offline" class="nm3kTip" src="../images/wrong.png"/>';
	} else if (record.get('linkStatus') == '3') {// 不连接
		str = '<img nm3kTip="offline" class="nm3kTip" src="../images/wrong.png"/>'
	} else {
		str = '<img nm3kTip="online" class="nm3kTip" src="../images/yes.png"/>'
	}
	return str;
}
// 管理状态列的renderer操作
function adminRender(value, p, record) {
	var str = null;
	if (record.get('adminStatus') == '1') {
		str = I18N.engine.use;
	} else {
		str = I18N.engine.unUse;
	}
	return str;
}

// 操作列的renderer操作
function addOper(value, p, record) {
	var id = record.get('id');
	var recordId = record.id;
	if (id != 1) {
		return String
				.format(
						" <a href='javascript:;' onclick='modifyEngineServer({0})'>@COMMON.edit@</a> / <a href='javascript:;'  onclick='deleteEngineServer({0})'>@COMMON.del@</a> /"
								+ "<a href='javascript:;' class='withSub' onClick = 'showMoreOperation({1},event)'>@COMMON.other@</a>",
						id, recordId);
	} else {
		return String
				.format(
						" <a href='javascript:;' onclick='modifyEngineServer({0})'>@COMMON.edit@</a>",
						id, recordId);
	}
}

// 其它操作
function showMoreOperation(recordid, event) {
	var record = grid.getStore().getById(recordid); // Get the Record
	grid.getSelectionModel().selectRecords([ record ]);
	engineMenu.showAt([ event.clientX, event.clientY ]);
}

// 获取列表选中项的id,解决MenuItem不能参数的问题
function getSelectedItemId() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		return record.data.id;
	}
	return null;
}

// 重启分布式采集器
function reStartEngineServer() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		return window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.selectEngineServer@");
	}
	// 获取选中行的id
	/*var selections = sm.getSelections(), ids = [];
	for ( var i = 0; i < selections.length; i++) {
		ids.push(selections[i].data.id);
	}*/
	var selected = sm.getSelected(); 
	var id = selected.data.id;
	
	if (id == 1) {
		window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.notAllowOperLocalhost@");
		return;
	}
	var linkStatus = selected.data.linkStatus;
	if (linkStatus != 1){
		window.parent.showMessageDlg("@COMMON.tip@","@engine.notAllowRestartDisconnect@");
        return;
	} 
	window.top.showWaitingDlg("@sys.waiting@", "@engine.restartWaiting@");
	$.ajax({
		url : '/system/reStartEngineServer.tv',
		type : 'POST',
		data : {
			id : id
		},
		success : function() {
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
				title : I18N.sys.tip,
				html : '<b class="orangeTxt">' + I18N.engine.restartSucc
						+ '</b>'
			});
			grid.store.reload();
			// 成功后禁用启用按钮，启用停止按钮
			// tbar.getComponent("startServer").disable();
			// tbar.getComponent("stopServer").enable();
		},
		error : function(result) {
			window.parent.showErrorDlg();
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

// 启用分布式采集器
function startEngineServer() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		return window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.selectEngineServer@");
	}
	// 获取选中行的id
	/*var selections = sm.getSelections(), ids = [];
	for ( var i = 0; i < selections.length; i++) {
		ids.push(selections[i].data.id);
	}*/
	var selected = sm.getSelected(); 
	var id = selected.data.id;
	
	if (id == 1) {
		window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.notAllowOperLocalhost@");
		return;
	}
	$.ajax({
		url : '/system/startEngineServer.tv',
		type : 'POST',
		data : {
			id : id
		},
		success : function() {
			top.afterSaveOrDelete({
				title : I18N.sys.tip,
				html : '<b class="orangeTxt">' + I18N.engine.startSucc + '</b>'
			});
			grid.store.reload();
			// 成功后禁用启用按钮，启用停止按钮
			// tbar.getComponent("startServer").disable();
			// tbar.getComponent("stopServer").enable();
		},
		error : function(result) {
			window.parent.showErrorDlg();
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

// 停用分布式采集器
function stopEngineServer() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		return window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.selectEngineServer@");
	}
	// 获取选中行的id
	/*var selections = sm.getSelections(), ids = [];
	for ( var i = 0; i < selections.length; i++) {
		ids.push(selections[i].data.id);
	}*/
	var selected = sm.getSelected(); 
	var id = selected.data.id;
	
	if (id == 1) {
		window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.notAllowOperLocalhost@");
		return;
	}
	$.ajax({
		url : '/system/stopEngineServer.tv',
		type : 'POST',
		data : {
			id : id
		},
		success : function() {
			top.afterSaveOrDelete({
				title : I18N.sys.tip,
				html : '<b class="orangeTxt">' + I18N.engine.stopSucc + '</b>'
			});
			// window.parent.showMessageDlg(I18N.sys.tip, I18N.engine.stopSucc);
			grid.store.reload();
			// 成功后启用启用按钮，禁用停止按钮
			// tbar.getComponent("startServer").enable();
			// tbar.getComponent("stopServer").disable();
		},
		error : function(result) {
			window.parent.showErrorDlg();
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

// 刷新列表
function refreshList() {
	grid.store.reload();
}

// 添加采集服务器
function addEngineServer() {
	window.top.createDialog('addEngineServer', I18N.engine.addEngineServer,
			800, 500, 'system/showAddEngineServer.tv', null, true, true);
}

// 修改采集服务器
function modifyEngineServer(id) {
	window.top.createDialog('modifyEngineServer',
			I18N.engine.updateEngineServer, 800, 500,
			'system/showModifyEngineServer.tv?id=' + id, null, true, true);
}

// 删除分布式采集器 
function deleteEngineServer() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		return window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.selectEngineServer@");
	}
	/*var selections = sm.getSelections(), ids = [];
	for ( var i = 0; i < selections.length; i++) {
		ids.push(selections[i].data.id);
	}*/
	var selected = sm.getSelected(); 
	var id = selected.data.id;
	
	if (id == 1) {
		window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.notAllowOperLocalhost@");
		return;
	}
	window.top.showOkCancelConfirmDlg(I18N.sys.tip, I18N.ftp.confirmDelete,
			function(confirm) {
				window.top.showWaitingDlg("@COMMON.wait@", "@ftp.isDeleting@...", "waitingMsg", 600000, false);
				if (confirm == "ok") {
					$.ajax({
						url : '/system/deleteEngineServer.tv',
						type : 'POST',
						data : {
							id : id
						},
						success : function() {
							top.afterSaveOrDelete({
								title : I18N.sys.tip,
								html : '<b class="orangeTxt">@engine.deleteSucc@</b>'
							});
							grid.store.reload();
						},
						error : function(result) {
							window.parent.showErrorDlg();
						},
						cache : false,
						complete : function(XHR, TS) {
							XHR = null
						}
					});
				}else{
					top.closeWaitingDlg();
				}
			});
}

// 查看采集器状态
function loadEngineServerStatuss() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		return window.parent.showMessageDlg("@COMMON.tip@",
				"@engine.selectEngineServer@");
	}
	// 获取选中行的id
	var selections = sm.getSelections(), ids = [];
	for ( var i = 0; i < selections.length; i++) {
		ids.push(selections[i].data.id);
	}
	// 隐藏状态DIV，显示刷新DIV
	$("#eSM_subSide").css("display", "none");
	$("#loading_div").css("display", "inline");

	// 清空状态表格的值
	var tb = document.getElementById("statusTable");
	for ( var i = tb.rows.length - 1; i > 0; i--) {
		tb.deleteRow(i);
	}
	$.ajax({
		url : '/system/loadEngineServerStatuss.tv',
		type : 'POST',
		data : {
			ids : ids
		},
		dataType : "json",
		success : function(json) {
			// 显示状态
			presentStatuss(json);
			// 隐藏刷新DIV，显示状态DIV
			$("#loading_div").css("display", "none");
			$("#eSM_subSide").css("display", "inline");
		},
		error : function(result) {
			window.parent.showErrorDlg();
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function presentStatuss(json) {
	var tb = document.getElementById("statusTable");
	for ( var i = 0; i < json.number; i++) {
		var row = tb.insertRow(tb.rows.length);
		var c1 = row.insertCell(0);
		c1.innerHTML = json.statuss[i].name;
		var c2 = row.insertCell(1);
		c2.innerHTML = (json.statuss[i].runTime == "") ? I18N.engine.readless
				: json.statuss[i].runTime;
		var c3 = row.insertCell(2);
		c3.innerHTML = (json.statuss[i].memUsage == "") ? I18N.engine.readless
				: json.statuss[i].memUsage;
		var c4 = row.insertCell(3);
		c4.innerHTML = (json.statuss[i].threadNumber == 0) ? I18N.engine.readless
				: json.statuss[i].threadNumber;
	}
}

function updateButtonStatus() {
	var sm = grid.getSelectionModel();
	if (sm == null || !sm.hasSelection()) {
		//tbar.getComponent("stopServer").disable();
		engineMenu.getComponent("stopServerMenu").disable();
		//tbar.getComponent("startServer").disable();
		engineMenu.getComponent("startServerMenu").disable();
		return;
	}
	var selections = sm.getSelections(), hasOpen = false, hasNoOpen = false;
	for ( var i = 0; i < selections.length; i++) {
		if (hasOpen && hasNoOpen)
			break;
		if (selections[i].data.adminStatus == 1) {// use
			hasOpen = true;
		} else { // nouse
			hasNoOpen = true;
		}
	}
	if (!hasOpen) {// 全是禁用的，则禁用停止按钮
		//tbar.getComponent("stopServer").disable();
		engineMenu.getComponent("stopServerMenu").disable();
		engineMenu.getComponent("restartServerMenu").disable();
	} else {
		//tbar.getComponent("stopServer").enable();
		engineMenu.getComponent("stopServerMenu").enable();
		engineMenu.getComponent("restartServerMenu").enable();
	}
	if (!hasNoOpen) {// 全是开启的，则禁用开启按钮
		//tbar.getComponent("startServer").disable();
		engineMenu.getComponent("startServerMenu").disable();
	} else {
		//tbar.getComponent("startServer").enable();
		engineMenu.getComponent("startServerMenu").enable();
	}
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	var w = document.documentElement.clientWidth;
	var h = document.documentElement.clientHeight;
	sm = new Ext.grid.CheckboxSelectionModel({
		listeners : {
			selectionchange : updateButtonStatus
		}
	});
	cm = new Ext.grid.ColumnModel([{
		header : "id",
		dataIndex : 'id',
		align : "left",
		hidden : true,
		resizable : false
	}, {
		header : I18N.engine.name,
		dataIndex : 'name',
		align : "left",
		width : 90,
		resizable : false
	}, {
		header : I18N.engine.ipAdress,
		id : 'ip',
		dataIndex : 'ip',
		align : "center",
		width : 110,
		sortable : true,
		resizable : false
	}, {
		header : I18N.engine.mgr,
		id : 'ipGroupDisplay',
		dataIndex : 'ipGroupDisplay',
		align : "center",
		width : 110,
		sortable : true,
		resizable : false
	}, {
		header : I18N.engine.port,
		dataIndex : 'port',
		align : "center",
		sortable : true,
		width : 70,
		resizable : false
	}, {
		header : I18N.engine.note,
		dataIndex : 'note',
		align : "center",
		width : 110,
		sortable : true,
		resizable : false
	}, {
		header : I18N.engine.type,
		dataIndex : 'type',
		align : "center",
		width : 250,
		resizable : false
	}, {
		header : I18N.engine.connectStatus,
		dataIndex : 'linkStatus',
		align : "center",
		width : 80,
		resizable : false,
		renderer : linkRender
	}, {
		header : I18N.engine.adminStatus,
		dataIndex : 'adminStatus',
		align : "center",
		width : 80,
		resizable : false,
		renderer : adminRender
	}, {
		header : I18N.engine.operator,
		dataIndex : 'operator',
		align : "center",
		width : 170,
		fixed : true,
		resizable : false,
		renderer : addOper
	} ]);

	var reader = new Ext.data.JsonReader({
		totalProperty : "engineServerNumber",
		idProperty : "id",
		root : "data",
		fields : [ {
			name : 'id'
		},// 管理状态
		{
			name : 'name'
		}, {
			name : 'ip'
		}, {
			name : 'ipGroupDisplay'
		}, {
			name : 'port'
		}, {
			name : 'note'
		}, {
			name : 'type'
		}, {
			name : 'linkStatus'
		}, {
			name : 'adminStatus'
		} ]
	});

	store = new Ext.data.GroupingStore({
		url : 'system/getEngineServerList.tv',
		reader : reader,
		groupField : 'ipGroupDisplay',
		groupOnSort : false,
		sortInfo : {
			field : 'id',
			direction : 'asc'
		}
	});
	store.load();

	tbar = new Ext.Toolbar([ {
		iconCls : 'bmenu_new',
		text : I18N.engine.add,
		handler : addEngineServer
	}, '-', {
		iconCls : 'bmenu_refresh',
		text : I18N.engine.refresh,
		handler : refreshList
	}, '-' ]);

	// var groupTpl = '{text} ({[values.rs.length]} {[values.rs.length > 1 ?
	// "@COMMON.items@" : "@COMMON.items@"]})';

	var groupTpl = '{text}';

	grid = new Ext.grid.GridPanel({
		region : "center",
		border : false,
		bodyCssClass : "normalTable",
		view : new Ext.grid.GroupingView({
			forceFit : true,
			hideGroupedColumn : true,
			enableNoGroups : true,
			groupTextTpl : groupTpl
		}),
		stripeRows : true,
		store : store,
		cm : cm,
		sm : sm,
		loadMask : true,
		tbar : tbar,
		viewConfig : {
			forceFit : true
		}
	});

	var viewPort = new Ext.Viewport({
		layout : "border",
		items : [ grid, {
			border : true,
			region : 'east',
			// title: 'Title for the Grid Panel',
			width : 316,
			contentEl : 'form_div'
		} ]
	});

	// 列表中其它部分菜单项
	engineMenu = new Ext.menu.Menu({
		id : 'entityMenu',
		minWidth : 150,
		enableScrolling : false,
		items : [ {
			text : "@engine.use@",
			itemId : 'startServerMenu',
			handler : startEngineServer
		}, '-', {
			text : "@engine.stop@",
			itemId : 'stopServerMenu',
			handler : stopEngineServer
		}, '-', {
			text : "@engine.restart@",
			itemId : 'restartServerMenu',
			handler : reStartEngineServer
		}, '-', {
			text : "@engine.lookStatus@",
			handler : loadEngineServerStatuss
		} ]
	});

	//tbar.getComponent("stopServer").disable();
	engineMenu.getComponent("stopServerMenu").disable();
	//tbar.getComponent("startServer").disable();
	engineMenu.getComponent("startServerMenu").disable();

});

// 自动适应浏览器窗口调整
window.onresize = function() {

};
