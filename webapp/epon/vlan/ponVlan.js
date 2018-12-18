var ponTabInited = false,
	ponVlanstore,
	ponVlanGrid;

/**
 * 初始化PON VLAN信息区域
 */
function initPonTab(){
	
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledPonVlanToolbarBtn(ponVlanGrid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	        	disabledPonVlanToolbarBtn(ponVlanGrid.getSelectionModel().getSelections().length);
	        }
	    }
	});
	var cm =  new Ext.grid.ColumnModel([
	    sm,                               
  	    {header:'@VLAN.portNum@', dataIndex:'portIndex', align:'center', width: 80, sortable:true, renderer: convertPortIndexToStr},
  	    {header: '<div class="txtCenter">@COMMON.belongs@TAGGED VLAN</div>', width: 100, dataIndex:'tagVlanList', align:'left', sortable:true, renderer: convertVlanArrayToAbbr},
  	    {header: '<div class="txtCenter">@COMMON.belongs@UNTAGGED VLAN</div>', width: 100, dataIndex:'untagVlanList', align:'left', sortable:true, renderer: convertVlanArrayToAbbr},
  	    {header: "PVID", sortable:true, align:'center', width: 80, dataIndex: 'vlanPvid'},
  	    {header: "@COMMON.manu@", dataIndex:'portIndex', align:'center', width: 200, fixed : true, renderer: ponMenuRender}
  	]);
	
  	ponVlanstore = new Ext.data.JsonStore({
  		url: '/epon/vlanList/loadPonPortVlanList.tv?entityId='+entityId,
 	    fields: ['portId', 'portIndex','tagVlanList','untagVlanList','tpid','priority','vlanPvid', 'vlanMode']
  	});
  	
  	ponVlanGrid = new Ext.grid.GridPanel({
   		stripeRows: true, 
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: ponVlanstore,
   		cm : cm,
   		sm : sm,
   		height: $('#center-container').height(),
   		tbar: new Ext.Toolbar({
	    	items: [
	    	    {xtype: 'spacer', width: 5},
        		{id: 'addPonVlanBtn', text: '@COMMON.batchEditVlan@', iconCls: 'bmenu_new', handler: showEditPonVlans, disabled: true}, 
	         	'-', 
	         	{text: '@VLAN.refreshDeviceVlan@', id: 'refreshVlan_button3', iconCls: 'bmenu_refresh', handler: refreshDeviceVlan}, 
	         	'-', 
	         	{text: '@VLAN.refreshPonVlan@', id: 'refreshPonVlan_button', iconCls: 'bmenu_refreshData', handler: refreshPonList}
	       	]
     	}),
   		viewConfig:{
   			forceFit: true
   		},
	    renderTo: "ponGridContainer",
	    loadMask: true
   	});
  	ponVlanstore.load();
	
	ponTabInited = true;
}

function refreshPonList(){
	ponVlanstore.reload({
		callback : function(){
			disabledPonVlanToolbarBtn(0);
		}
	});
}

function ponMenuRender(v){
	return String.format('<a class="yellowLink" href="javascript:showEditPonVlans();">@COMMON.belongs@VLAN</a>\
			/ <a class="yellowLink" href="javascript:showEditPonPvid();">@COMMON.modify@ PVID</a>\
			/ <a class="yellowLink" href="javascript:showPonVlanConfig({0});">@VLAN.vlanCfg@</a>', v); 
}

function showEditPonVlans(){
	//获取选中的端口
	var selected = ponVlanGrid.getSelectionModel().getSelections();
	if(!selected.length){
		return window.parent.showMessageDlg("@COMMON.tip@", '@VLAN.plsSelectPon@');
	}
	//整理出勾选的端口sniIndex
	var ponIndexArr = [];
	$.each(selected, function(i, curPon){
		ponIndexArr.push(curPon.data.portIndex);
	});
	var url = '/epon/vlanList/showEditPortVlans.tv?entityId='+entityId+'&portIndexsStr='+ponIndexArr.join(',');
	var tipStr = '@VLAN.batchEdit@@COMMON.belongs@VLAN';
	if(ponIndexArr.length == 1){
		tipStr = '@VLAN.editPon@@COMMON.maohao@' + convertPortIndexToStr(selected[0].data.portIndex) +'@COMMON.belongs@VLAN';
		var tagVlanList = selected[0].data.tagVlanList,
			untagVlanList = selected[0].data.untagVlanList;
		url += '&taggedVlanStr=' + convertVlanArrayToAbbr(tagVlanList);
		url += '&unTaggedVlanStr=' + convertVlanArrayToAbbr(untagVlanList);
	}
	window.top.createDialog('editVlans', tipStr, 600, 300, url, null, true, true,function(){
		ponVlanstore.reload({
			callback : function(){
				disabledPonVlanToolbarBtn(0);
			}
		});
	});
}

function showEditPonPvid(){
	//获取选中的PON口
	var selected = ponVlanGrid.getSelectionModel().getSelections(),
		selPonPort = selected[0].data;
	window.top.createDialog('ponPvidConfig', "@PON.pvidConfig@", 600, 270, "/epon/showPonPvidConfig.tv?entityId="
			+entityId+"&ponId="+selPonPort.portId+"&portIndex="+selPonPort.portIndex, null, true, true, function(){
		ponVlanstore.reload({
			callback : function(){
				disabledPonVlanToolbarBtn(0);
			}
		});
	});
}

function showPonVlanConfig(portIndex){
	var selected = ponVlanGrid.getSelectionModel().getSelections(),
		selPonPort = selected[0].data;
	window.parent.addView('ponVlanConfig-' + entityId, entityName + ' @VLAN.vlanCfg@', 'entityTabIcon', 
			'epon/vlanList/showPonVlanConfig.tv?entityId=' + entityId + '&portIndex='+portIndex + '&ponId=' + selPonPort.portId, null, true);
}

/**
 * 将PON口列表转换成供树结构展示的数据结构
 * @param {Array} ponList PON口列表
 * @returns {Array}
 */
function convertPonListToTreeNodes(ponList){
	var retArr = [];
	$.each(ponList, function(index, pon){
		retArr.push({
			text: pon.slotNo + '/' + pon.ponNo,
			icon: pon.ponPortAdminStatus === 1 ? '/images/network/port/admin.gif' : '/images/network/port/noadmin.gif',
			leaf: true
		});
	});
	return retArr;
}
function disabledPonVlanToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(["addPonVlanBtn"], false);
    }else{
        disabledBtn(["addPonVlanBtn"], true);
    }
};