$(function(){
	//初始化页面布局
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'header-container',
	        height: 40
	      },
	      {
	    	  region: 'center',
	    	  border: false,
	    	  contentEl: 'grid-container'
	      }
	    ]
	});
	//初始化grid
	initCpeList();
})

function initCpeList(){
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'@ONU.uniport@', dataIndex:'uniDisplayName', align:'center', width: 80, sortable:true},
  	  	{header:'CPE MAC', dataIndex:'mac', align:'center', width: 100, sortable:true},
  	    {header: 'VLAN ID', dataIndex:'vlan', align:'center', width: 100, sortable:true, renderer: vlanAndIpRender},
  	    {header: 'MAC@ONU.onuType@', dataIndex:'type', align:'center', width: 100, sortable:true, renderer: typeRender},
  	    {header: 'CPE IP', dataIndex:'ipAddress',renderer: vlanAndIpRender},
  	    {header: 'CPE@ONU.onuType@', dataIndex:'cpeType',renderer:cpeTypeRender}
  	]);
	
	onuCpeStore = new Ext.data.JsonStore({
  		url: '/epon/onucpe/loadOnuUniCpeList.tv?onuId=' + onuId,
 	    fields: ['uniIndex', 'uniNo', 'mac','vlan','type','uniDisplayName',"ipAddress",'cpeType']
  	});
  	
	onuCpeGrid = new Ext.grid.GridPanel({
   		stripeRows: true, 
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: onuCpeStore,
   		cm : cm,
   		height: $('#grid-container').height(),
   		viewConfig:{
   			forceFit: true
   		},
   		tbar: new Ext.Toolbar({
	    	items: [
        		{text: '@ONU.refreshDevice@', iconCls: 'bmenu_refresh', handler: refreshCpeList}
	       	]
     	}),
	    renderTo: "grid-container",
	    loadMask: true
   	});
	
	onuCpeStore.load();
}

/**
 * 刷新实时ONU CPE数据:refreshOnuUniCpe
 */
function refreshCpeList(){
	window.top.showWaitingDlg('@COMMON.wait@', '@ONU.refreshingCpe@', 'ext-mb-waiting');
	$.ajax({
		url: '/epon/onucpe/refreshOnuUniCpe.tv',
		type: 'POST',
		data: {
			onuId: onuId
		},
		dataType : "json",
		cache : false,
		success : function(vlanJson) {
			top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '@ONU.refreshCpeSuccess@'
	    	});
			onuCpeStore.reload();
		},
		error : function(vlanJson) {
			window.parent.showMessageDlg('@COMMON.tip@', '@ONU.refreshCpeError@');
		},
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function vlanAndIpRender(v){
	return v || '--';
}

function typeRender(v){
	switch(v){
	case 1:
		return '@ONU.static@';
	case 2:
		return '@ONU.dynamic@';
	default:
		return v;
	}
}
function cpeTypeRender(v){
	switch(v){
	case 1:return "host";
	case 2:return "mta";
	case 3:return "stb";	
	default:return "--";
	}
}