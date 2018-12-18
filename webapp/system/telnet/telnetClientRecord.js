var grid;

$(function() {
	// 创建时间输入框
	window.stTime = new Ext.ux.form.DateTimeField({
		width:247,
		editable: false,
		startDay: 0,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	window.etTime = new Ext.ux.form.DateTimeField({
		width:247,
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s'
	});
	
	var columns = [
	    {dataIndex: 'ip', header: 'IP', width: 150},
	    {dataIndex: 'command', header: '@TelnetClient.command@', width: 400},
	    {dataIndex: 'userName', header: '@TelnetClient.username@', width: 150},
	    {dataIndex: 'createTimeStr', header: '@TelnetClient.createTime@', width: 200}
	];
	var cmConfig = CustomColumnModel.init('telnetRecord', columns, {}),
      	cm = cmConfig.cm;
	
	store = new Ext.data.JsonStore({
	    url: '/system/telnet/loadTelnetRecord.tv',
	    root: 'data',
	    idProperty: "id",
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['ip', 'command', 'userName', 'createTime', 'createTimeStr']
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
	    cm: cm,
	    loadMask: true,
	    bbar: bbar,
	    margins: '0 5px',
	    sm: new Ext.grid.RowSelectionModel({
	    	singleSelect: true
	    }),
	    listeners: {
	    	sortchange : function(grid,sortInfo){
	    		CustomColumnModel.saveSortInfo('cmlist', sortInfo);
	    	},
	    	columnresize: function(){
	    		CustomColumnModel.saveCustom('cmlist', cm.columns);
	    	}
	    },
	    viewConfig: {
	    	forceFit: true,
	    	scrollOffset: 0
	    }
	});
	
	// 布局
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'query-container',
	        height: 80
	      },
	      grid
	    ]
	});
	
	store.load();
});

function query() {
	var queryData = {};
	// ip
	queryData.entityIp = $('#ip').val();
	// command
	queryData.command = $('#command').val();
	// userName
	queryData.userName = $('#userName').val();
	// startTime
	queryData.startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	// endTime
	queryData.endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	
	store.baseParams = queryData;
	
	store.load();
}