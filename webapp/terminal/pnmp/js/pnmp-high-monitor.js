Ext.onReady(function() {
	loadTargetThreshold();
	
	sm = new Ext.grid.CheckboxSelectionModel({
		listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
    });
	
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			menuDisabled : false
		},
		columns : [
			sm,
			{header: '@pnmp.cmcName@', width: 100, sortable: false, align: 'center', dataIndex: 'cmcName'},
			{header: 'CM MAC', width: 100, sortable: false, align: 'center', dataIndex: 'cmMac'},
			{header: '@pnmp.cmcAddress@', width: 100, sortable: false, align: 'center', dataIndex: 'cmAddress'},
			{header: '@pnmp.collectTime@', width: 100, sortable: false, align: 'center', dataIndex: 'collectTimeString'},
			{header: '@pnmp.mtr@(dB)', width: 120, sortable: false, align: 'center', dataIndex: 'mtr', renderer: function(value){return renderUtil(value, 'mtr')}},
			{header: '@pnmp.faultDistance@(m)', width: 100, sortable: false, align: 'center', dataIndex: 'tdr', renderer: renderValue},
			{header: '@pnmp.upTxPower@(dBmV)', width: 100, sortable: false, align: 'center', dataIndex: 'upTxPower', renderer: function(value){return renderUtil(value, 'upSendPower')}},
			{header: '@pnmp.upSnr@(dB)', width: 100, sortable: false, align: 'center', dataIndex: 'upSnr', renderer: function(value){return renderUtil(value, 'upSnr')}},
			{header: '@pnmp.downRePower@(dBmV)', width: 100, sortable: false, align: 'center', dataIndex: 'downRxPower', renderer: function(value){return renderUtil(value, 'downRePower')}},
			{header: '@pnmp.downSnr@(dB)', width: 100, sortable: false, align: 'center', dataIndex: 'downSnr', renderer: function(value){return renderUtil(value, 'downSnr')}},
			{header: '@pnmp.operation@', width: 120, sortable: false, fixed:true, align: 'center', dataIndex: 'cmAlias', renderer: renderOperation}
		]
	});
	
	var sortInfo = {field: 'collectTime  ', direction: 'ASC'};
	
	store = new Ext.data.JsonStore({
	    url: '/pnmp/monitor/queryHighMonitorCmList.tv',
	    root: 'data',
	    idProperty: "cmId",
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: [
	      'cmcId', 'entityIp', 'cmcName', 'cmMac', 'cmAddress', 'collectTime', 'collectTimeString', 'tapCoefficient', 'spectrumResponse', 'correlationGroup'
	      , 'mte', 'preMte', 'postMte', 'tte', 'mtc', 'mtr', 'nmtter', 'premtter', 'postmtter', 'ppesr', 'mrLevel', 'tdr', 'upSnr'
	      , 'upTxPower', 'downSnr', 'downRxPower'
	    ]
  	});
	
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
    store.load({params: {start:0,limit: pageSize}});
	
	tbar = new Ext.Toolbar({
	    items: [
	        {id:'addBtn', text: '@pnmp.addCm@', iconCls: 'bmenu_new', handler: addCm},
	        {id:'deleteBtn', text:'@pnmp.delCm@', iconCls: 'bmenu_delete', handler: batchRemoveCm, disabled: true}
	    ]
  	});
	
	bbar = new Ext.PagingToolbar({
	    id: 'extPagingBar',
	    pageSize: pageSize,
	    store: store,
	    displayInfo: true,
	    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
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
	    sm: sm,
	    cm: cm,
	    tbar: tbar,
	    loadMask: true,
	    bbar: bbar,
	    viewConfig: {
	      scrollOffset: 0,
	      forceFit: true
	    }
	  });
	
	new Ext.Viewport({
		layout : 'border',
		items : [ {
			region: 'north',
	        border: false,
	        contentEl: 'query-container',
	        height: 30
		}, grid]
	});
});	

//根据选中的行数判断是否将查询收光功率历史记录按钮置灰
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['deleteBtn'], false);
    }else{
    	disabledBtn(['deleteBtn'], true);
    }
}

//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
}

//获取选中行的CM
function getSelectedCM() {
	var selectedModel = grid.getSelectionModel();
	if (selectedModel != null && selectedModel.hasSelection()) {
		var record = selectedModel.getSelected();
		return record.data;
	}
	return null;
}

function reloadAfterAddMac() {
	var cmcName = $("#cmcName").val();
	var cmMac = $("#cmMac").val();
	var cmAddress = $("#cmAddress").val();
	store.baseParams = {
			cmcName: cmcName,
			cmMac: cmMac,
			cmAddress, cmAddress
	};
    store.load({
		callback: function(){
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
	});
}

function renderOperation(v) {
	return '<a class="" href="javascript:showHistoryData();" >@pnmp.history@</a> / <a class="mR10" href="javascript:removeCm();" >@COMMON.delete@</a>'
}