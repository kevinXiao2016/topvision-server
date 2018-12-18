Ext.onReady(function(){
    columnModels = [
        {header: "VLAN_ID", width:100, dataIndex: "topOltDhcpVLANIndex", align: "center", sortable: true},
        {header: "@oltdhcp.mode@", width:100, dataIndex: "topOltDhcpVLANMode", align: "center", sortable: true, renderer: renderMode},
        {header: "@oltdhcp.relayMode@", width:100, dataIndex: "topOltDhcpVLANRelayMode", align: "center", sortable: true, renderer: renderRelayMode},
        {header: "<div class=txtCenter>@COMMON.manu@</div>", width:100, dataIndex: "dataIndex5", align: "center", sortable: false, renderer: manuRender}
    ];
    cm = new Ext.grid.ColumnModel({
        defaults : {
            menuDisabled : false
        },
        columns: columnModels
    });
    store = new Ext.data.JsonStore({
        url: "/epon/oltdhcp/loadOltDhcpVlanList.tv",
        root: "data",
        totalProperty: "rowCount",
        remoteSort: true,
        fields: ["entityId","topOltDhcpVLANIndex","topOltDhcpVLANMode","topOltDhcpVLANRelayMode"]
    });
    //store.setDefaultSort("entityId", "ASC");
    store.baseParams={
		entityId:entityId
    }
    store.load();
    tbar = new Ext.Toolbar({
        items : [{
            text : "@COMMON.fetch@",
            cls: 'pT5 pB5 pL10',
            iconCls : "miniIcoEquipment",
            handler: fetchData,
            disabled: !refreshDevicePower
        }]
    });
    bbar = new Ext.PagingToolbar({
        id: "extPagingBar",
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        items: ["-", String.format("@COMMON.displayPerPage@", pageSize), "-"]
    });
    grid = new Ext.grid.GridPanel({
    	margins: '0 10 0 10',
        stripeRows:true,
        cls:"normalTable",
        bodyCssClass: "normalTable",
        region: "center",
        store: store,
        //bbar : bbar,
        tbar : tbar,
        cm: cm,
        viewConfig:{ forceFit: true }
    });
    viewPort = new Ext.Viewport({
        layout: "border",
        items: [grid]
    });

});//end document.ready;
//render编辑按钮;
function manuRender(value, p, record){
	if(operationDevicePower) {
		var vlanIndex = record.data.topOltDhcpVLANIndex;
		return '<a href="javascript:;" onclick="editFn('+vlanIndex+')">@COMMON.edit@</a>'; 
	} else {
		return '<span>@COMMON.edit@</span>'; 
	}
}
function editFn(vlanIndex){
	top.createDialog("modalDlg", "@oltdhcp.modifyDhcpMode@", 600, 370, "/epon/oltdhcp/showModifyOltDhcpVlan.tv?entityId="+entityId+"&&vlanIndex="+vlanIndex, null, true, true,function(){
		onRefreshClick();
	});
}
//从设备获取数据;
function fetchData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.synDeviceData@....", 'ext-mb-waiting');
    $.ajax({
        url:"/epon/oltdhcp/refreshOltDhcpVlanList.tv?entityId="+entityId,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@oltdhcp.synDeviceData@@oltdhcp.success@！</b>'
            });
            window.location.reload();
        },error:function(){
        	window.top.closeWaitingDlg();
            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.synDeviceData@@oltdhcp.fail@！");
        }
    });
}

function renderMode(value, p, record) {
	switch (value){
	case 1:
		return 'snooping';
	case 2:
		return 'relay';
	default:
		return '--';
	}
}

function renderRelayMode(value, p, record) {
	var mode = record.data.topOltDhcpVLANMode;
	if(mode == 2&& value == 1){
		return 'standard';
	}else if(mode == 2&& value == 2){
		return 'option60';
	}else{
		return '--';
	}
}

function onRefreshClick(){
	store.reload();
}