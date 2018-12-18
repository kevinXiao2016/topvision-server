var sniTabInited = false,
	sniVlanstore,
	sniVlanGrid;

function createSniPortList(){
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledSniVlanToolbarBtn(sniVlanGrid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	        	disabledSniVlanToolbarBtn(sniVlanGrid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	var cm =  new Ext.grid.ColumnModel([
	    sm,                               
  	    {header:'@VLAN.portNum@', dataIndex:'portDisplay', align:'center', width: 80, sortable:true, renderer: renderPortNo},
  	  	{header:'@VLAN.portName@', dataIndex:'sniPortName', align:'center', width: 100, sortable:true},
  	    {header: '<div class="txtCenter">@COMMON.belongs@TAGGED VLAN</div>', width: 100, dataIndex:'tagVlanList', align:'left', sortable:true, renderer: vlanListRender},
  	    {header: '<div class="txtCenter">@COMMON.belongs@UNTAGGED VLAN</div>', width: 100, dataIndex:'untagVlanList', align:'left', sortable:true, renderer: vlanListRender},
  	    {header:'@VLAN.protocolMark@(Tpid)', dataIndex:'tpid', align:'center', width: 90, sortable:true, renderer: sniTpidRender},
  	    {header: "@VLAN.vlanPriority@", sortable:true, align:'center', width: 80, dataIndex: 'priority'},
  	    {header: "PVID", sortable:true, align:'center', width: 80, dataIndex: 'vlanPvid'},
  	    {header:'@VLAN.vlanMode@', dataIndex:'vlanMode', align:'center', width: 50 ,sortable:true, renderer: sniVlanModeRender},
  	    {header: "@COMMON.manu@", dataIndex:'port', align:'center', width: 200, fixed : true, renderer: menuRender}
  	]);
	
  	sniVlanstore = new Ext.data.JsonStore({
  		url: '/epon/vlanList/loadSniPortVlanList.tv?entityId='+entityId,
 	    fields: ['portId', 'portIndex','portDisplay','sniPortName','tagVlanList','untagVlanList','tpid','priority','vlanPvid', 'vlanMode', 'bName']
  	});
  	sniVlanstore.baseParams = {limit: 9999};
  	
  	sniVlanGrid = new Ext.grid.GridPanel({
   		stripeRows: true, 
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: sniVlanstore,
   		cm : cm,
   		sm : sm,
   		height: $('#center-container').height(),
   		tbar: new Ext.Toolbar({
	    	items: [
	    	    {xtype: 'tbspacer', width:5},
        		{id: 'editSniVlanBtn', text: '@COMMON.batchEditVlan@', iconCls: 'bmenu_new', handler: showEditSniVlans, disabled: true}, 
	         	'-', 
	         	{text: '@VLAN.refreshDeviceVlan@', id: 'refreshVlan_button2', iconCls: 'bmenu_refresh', handler: refreshDeviceVlan}, 
	         	'-', 
	         	{text: '@VLAN.refreshSniVlan@', id: 'refreshSniVlan_button', iconCls: 'bmenu_refreshData', handler: refreshSniVlan}
	       	]
     	}),
   		viewConfig:{
   			forceFit: true
   		},
	    renderTo: "sniGridContainer",
	    loadMask: true
   	});
  	sniVlanstore.reload();
  	
  	sniTabInited = true;
}

function refreshSniPortList(){
	sniVlanstore.reload({
		callback : function(){
			disabledSniVlanToolbarBtn(sniVlanGrid.getSelectionModel().getSelections().length);
		}
	});
}

function refreshSniVlan(){
	sniVlanstore.reload({
		callback : function(){
			disabledSniVlanToolbarBtn(sniVlanGrid.getSelectionModel().getSelections().length);
		}
	});
}

function renderPortNo(v, p, record) {
	var portIndex = record.data.portIndex;
	return String.format('{0}({1})', convertPortIndexToStrWithType(record.data.portIndex), record.data.bName.toUpperCase());
}

function vlanListRender(v){
	return convertVlanArrayToAbbr(v);
}

function sniTpidRender(v){
	if(v){
		return '0x' + v.replace(':', '')
	}
	return v;
}

function sniVlanModeRender(v,p,record) {
	if(record.data.vlanMode == 1){
		return 'trunk';
	}else if(record.data.vlanMode == 2){
		return 'access';
	}else if(record.data.vlanMode == 3){
		return 'hybrid';
	}else{
		return 'hybrid';
	}
}

function menuRender(v){
	return '<a class="yellowLink" href="javascript:showEditSniVlans();">@COMMON.belongs@VLAN</a> / <a class="yellowLink" href="javascript:showVlanConfig();">@VLAN.vlanCfg@</a>';
}

/**
 * 展示编辑SNI口所属VLAN页面
 */
function showEditSniVlans(){
	//获取选中的端口
	var selected = sniVlanGrid.getSelectionModel().getSelections();
	if(!selected.length){
		return window.parent.showMessageDlg("@COMMON.tip@", '@VLAN.plsSelectSni@');
	}
	//整理出勾选的端口sniIndex
	var sniIndexArr = [];
	$.each(selected, function(i, curSni){
		sniIndexArr.push(curSni.data.portIndex);
	});
	var url = '/epon/vlanList/showEditPortVlans.tv?entityId='+entityId+'&portIndexsStr='+sniIndexArr.join(',');
	var tipStr = '@VLAN.batchEdit@@COMMON.belongs@VLAN';
	if(sniIndexArr.length == 1){
		tipStr = '@VLAN.editSni@@COMMON.maohao@' + selected[0].data.sniPortName +'@COMMON.belongs@VLAN';
		var tagVlanList = selected[0].data.tagVlanList,
			untagVlanList = selected[0].data.untagVlanList;
		url += '&taggedVlanStr=' + convertVlanArrayToAbbr(tagVlanList);
		url += '&unTaggedVlanStr=' + convertVlanArrayToAbbr(untagVlanList);
	}
	window.top.createDialog('editVlans', tipStr, 600, 300, url, null, true, true,function(){
		sniVlanstore.reload({
			callback : function(){
				disabledSniVlanToolbarBtn(sniVlanGrid.getSelectionModel().getSelections().length);
			}
		});
	});
}

function showVlanConfig(){
	var selected = sniVlanGrid.getSelectionModel().getSelections(),
		selSniPort = selected[0].data;
	var portRealIndex = analysisPortIndex(selSniPort.portIndex).port;
	var sniName =  selSniPort.sniPortName;
	window.top.createDialog('sniVlanConfig', '@EPON.sniVlanCfg@', 600, 380, 'epon/vlan/getSniPortVlanAttribute.tv?entityId='+entityId+'&sniIndex='+selSniPort.portIndex+'&portRealIndex='+portRealIndex + '&sniName=' + sniName, null, true, true, function(){
		refreshSniPortList();
	});
}
function disabledSniVlanToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
    	disabledBtn(["editSniVlanBtn"], false);
    }else{
    	disabledBtn(["editSniVlanBtn"], true);
    }
};