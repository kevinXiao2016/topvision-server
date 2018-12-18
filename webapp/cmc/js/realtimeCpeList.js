var store;
var grid;
Ext.onReady(function() {
	var w = $(window).width();
	var h = $(window).height();
	var columns = [
		{header: 'CPE IP', 				 			width: parseInt(w/5), sortable:false, align: 'center', dataIndex: 'topCmCpeIpAddress'},
		{header: I18N.SYSTEM.status,				width: parseInt(w/10), sortable:false, align: 'center', dataIndex: 'topCmCpeStatus',			renderer: renderStatusValue},
		{header: 'CPE MAC',				 			width: parseInt(3*w/10), sortable:false, align: 'center', dataIndex: 'topCmCpeMacAddressString'},
		{header: I18N.cpe.type,						width: parseInt(w/10), sortable:false, align: 'center', dataIndex: 'topCmCpeTypeString'},
		{header: I18N.cpe.relateToCmMac,			width: 250, sortable:false, align: 'center', dataIndex: 'topCmCpeToCmMacAddr'}
	];
	Ext.Ajax.timeout = 90000;  
	store = new Ext.data.JsonStore({
        url: '/realtimecmlist/loadRealtimeCpeList.tv?cmcId='+cmcId+'&cpeType='+cpeType+'&cpeStatus='+cpeStatus,
        root: 'data',
        totalProperty: 'rowCount',
        remoteSort: false, 
        fields: ['topCmCpeIpAddress','topCmCpeMacAddressString','topCmCpeStatus','topCmCpeTypeString','topCmCpeToCmMacAddr']
    });
	var cm = new Ext.grid.ColumnModel(columns);
	grid = new Ext.grid.GridPanel({
		cls: 'normalTable',
		region: 'center',
		margins:'0px 22px 0px 5px',
		width: w, 
	    height: h-10,
		border: true, 
		loadMask: true,
		totalProperty: 'rowCount',
		store: store, 
		cm: cm,
		renderTo: 'grid'
	});
	store.load();
	store.on('load', function(){
	});
})

//----------------------------------
//-----------CPE状态解析-------------
//----------------------------------
function renderStatusValue(value,p,record){
	if (value == 1) {
	    return '<img nm3kTip="' + I18N.CMC.label.online + '" class="nm3kTip" src="/images/network/port/admin.gif" border=0 align=absmiddle>';
	} else {
	    return '<img nm3kTip="' + I18N.CMC.label.offline + '" class="nm3kTip" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>';
	}
}

//----------------------------------
//-----------CPE类型解析-------------
//----------------------------------
function renderCpeType(value,p,record){
	switch(value){
	case 1:
		return 'HOST';
	case 2:
		return 'MTA';
	case 3:
		return 'STB';
	default :
		return 'Extension Device';
	} 
}

function doRefresh(){}