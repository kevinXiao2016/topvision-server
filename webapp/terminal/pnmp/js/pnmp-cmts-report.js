Ext.onReady(function() {
	drawPie();
	
	cmDistribution();
	
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			menuDisabled : false
		},
		columns : [
			{header: '@pnmp.cmcName@', width: 100, sortable: false, align: 'center', dataIndex: 'cmcName'},
			{header: '@pnmp.cmcIp@', width: 100, sortable: false, align: 'center', dataIndex: 'entityIp'},
			{header: 'MAC', width: 100, sortable: false, align: 'center', dataIndex: 'cmcMac'},
			{header: '@pnmp.CMAmount@', width: 100, sortable: false, align: 'center', dataIndex: 'totalCmNum'},
			{header: '@pnmp.onlineCMAmount@', width: 100, sortable: false, align: 'center', dataIndex: 'onlineCmNum'},
			{header: '@pnmp.highMonitorCMAmount@', width: 100, sortable: true, align: 'center', dataIndex: 'badCmNum', renderer: renderBadNum},
			{header: '@pnmp.middleMonitorCMAmount@', width: 100, sortable: false, align: 'center', dataIndex: 'marginalCmNum', renderer: renderMarginalNum},
			{header: '@pnmp.normalMonitorCMAmount@', width: 100, sortable: false, align: 'center', dataIndex: 'healthCmNum', renderer: renderHealthNum},
			{header: '@pnmp.operation@', width: 100, sortable: false, align: 'center', dataIndex: 'cmtsName', renderer: renderOperation}
		]
	});
	
	store = new Ext.data.JsonStore({
	    url: '/pnmp/cmtsreport/loadCmtsReportData.tv',
	    root: 'data',
	    idProperty: "cmId",
	    totalProperty: 'rowCount',	    
	    remoteSort: false,
	    fields: [
	      'cmcId', 'cmcIndex', 'cmcName', 'cmcMac', 'entityIp', 'entityName', 'totalCmNum', 'onlineCmNum', 'offlineCmNum', 'healthCmNum'
	      , 'marginalCmNum', 'badCmNum', 'updateTime', 'updateTimeString']
  	});
	
	store.load();
	
	bbar = new Ext.PagingToolbar({
	    id: 'extPagingBar',
	    pageSize: 25,
	    store: store,
	    displayInfo: true,
	    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", 25), '-']
  	});
	
	grid = new Ext.grid.EditorGridPanel({
	    cls: 'normalTable',
		bodyCssClass : 'normalTable',	
	    region: 'center',
	    border: true,
	    totalProperty: 'rowCount',
	    enableColumnMove: true,
	    store: store,
	    enableColumnMove: true,
	    cm: cm,
	    loadMask: true,
	    bbar: bbar,
	    sm: new Ext.grid.CheckboxSelectionModel({
	      singleSelect: true
	    }),
	    viewConfig: {
	      scrollOffset: 0,
	      forceFit: true
	    }
	  });
	
	new Ext.Viewport({
		layout : 'border',
		items : [ {
			region: 'west',
	        border: false,
	        contentEl: 'west-container',
	        width: 230
		} ,grid]
	});
});

function renderOperation(v) {
	return '<a class="mR10" href="javascript:showPnmDetail();" >@pnmp.query@</a>'
}

function renderBadNum(v, p, record) {
	var clazz = "";
	if((v / record.data.onlineCmNum) > 0.05) {
		clazz = "badColor";
	}
	return '<span class="' + clazz +'">' + v + '</span>';
}

function renderMarginalNum(v, p, record) {
	var clazz = "";
	if((v / record.data.onlineCmNum) > 0.2) {
		clazz = "badColor";
	}
	return '<span class="' + clazz +'">' + v + '</span>';
}

function renderHealthNum(v) {
	return '<span class="">' + v + '</span>';
}

//获取选中行的Cmc
function getSelectedCmc() {
	var selectedModel = grid.getSelectionModel();
	if (selectedModel != null && selectedModel.hasSelection()) {
		var record = selectedModel.getSelected();
		return record.data;
	}
	return null;
}
