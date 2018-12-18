var store;
var grid;
var selectionModel;
function addCmcVlan(){
	var win = window.parent.createDialog("addCmcVlan", '@VLAN.addCmcVlan@',600, 370, 
							'/cmcVlan/showAddCmcVlanJsp.tv?cmcId='+ cmcId , null, true, true);
		win.on('close', function(){
			store.reload();
		})
}

function deleteCmcVlan(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();	
		var topCcmtsVlanIndex = record.data.topCcmtsVlanIndex;
		window.top.showOkCancelConfirmDlg('@COMMON.tip@', "@VLAN.confirmDelVlan@ "+topCcmtsVlanIndex+"?", function (type) {
			if(type=="ok"){
				$.ajax({
					url:"/cmcVlan/deleteCmcVlan.tv?topCcmtsVlanIndex="+topCcmtsVlanIndex+"&cmcId="+cmcId,
					type:"POST",
					success:function(){	
						//window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.delSuc@");
		        		 top.afterSaveOrDelete({
		        				title: '@COMMON.tip@',
		        				html: '<b class="orangeTxt">@VLAN.delSuc@</b>'
		        		 }); 
		        		 window.top.closeWaitingDlg();
						 onRefreshClick();
					},error:function(){
						  window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.delFail@");
					}
				});
			}
		});
	}else{
		window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.pleaseSecVlan@");	
	}
}

//添加VLAN ip信息
function addCmcVlanIp(){
	var sm = grid.getSelectionModel();
	//判断是否选中
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();	
		var topCcmtsVlanIndex = record.data.topCcmtsVlanIndex;
		var dhcpAlloc = record.data.dhcpAlloc;
		if(dhcpAlloc == 1){
			window.parent.showMessageDlg('@COMMON.tip@', "@VLAM.cannotAddVlanIp@");
		}else{
			var priIpExist = record.data.priIpExist;
			var win = window.parent.createDialog("addCmcVlanIp", '@VLAN.vlanSubIpConfig@', 600, 370, 
							'/cmcVlan/showAddCmcVlanIp.tv?cmcId='+ cmcId +'&topCcmtsVlanIndex='+topCcmtsVlanIndex+'&priIpExist='+priIpExist, null, true, true);
			win.on('close', function(){
				store.reload();
			});
		}		
	}else{
		window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.pleaseSecVlan@");
	}	
}
function deleteBtClick(cmcId,cmcVlan,priTag){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	var ipAddr = record.data.ipAddr;
	var ipMask = record.data.ipMask;
	var secVidIndex = record.data.secVidIndex;
	//判断是否选中
	window.top.showOkCancelConfirmDlg('@COMMON.tip@', "@VLAN.confirmDelVlanIpConfirm@" + ipAddr+" ?", function (type) {
		if(type=="ok"){
			window.top.showWaitingDlg('@COMMON.waiting@', '@text.refreshData@', 'ext-mb-waiting');
			$.ajax({
				url:"/cmcVlan/deleteCmcVlanIp.tv?topCcmtsVlanIndex="+cmcVlan+"&cmcId="+cmcId+"&ipAddr="+ipAddr + "&ipMask="+ipMask + "&ipType="+priTag+"&secVidIndex="+secVidIndex,
				type:"POST",
				success:function(){	
					//window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.delVlanIpSuc@");
	       		 	top.afterSaveOrDelete({
	       				title: '@COMMON.tip@',
	       				html: '<b class="orangeTxt">@VLAN.delVlanIpSuc@</b>'
	       			}); 
	       		 	window.top.closeWaitingDlg();
	       		 	onRefreshClick();
				},error:function(){
						 window.parent.showMessageDlg('@COMMON.tip@', "@VLAN.delVlanIpFail@");
				}
			});
		}
	});
}

function modifyBtClick(cmcId,cmcVlan,priTag){
	var sm = grid.getSelectionModel();
	var record = sm.getSelected();
	var ipAddr = record.data.ipAddr;
	var ipMask = record.data.ipMask;
	var dhcpAlloc = record.data.dhcpAlloc;
	var dhcpAlloc = record.data.dhcpAlloc;
	var secVidIndex = record.data.secVidIndex;
	var option60 = record.data.option60;
	var win = window.parent.createDialog("modifyCmcVlanIp", '@VLAN.modifyVlanSubIp@', 600, 370, 
		'/cmcVlan/showModifyCmcVlanIp.tv?cmcId='+ cmcId +'&topCcmtsVlanIndex='+cmcVlan+'&ipType='+priTag +'&cmcVlanIp='+ipAddr+'&cmcVlanMask='+ipMask+'&dhcpAlloc='+dhcpAlloc
		+'&secVidIndex='+secVidIndex+'&option60='+option60, null, true, true);
		win.on('close', function(){
		store.reload();
	});
}

function onRefreshClick() {
	store.reload();
}

function manuRender(v, c, record){
    var imgStr1;
    var imgStr2;
    var ipAddr = record.data.ipAddr;
    var ipMask = record.data.ipMask;
    var editStr = '<span class="cccGrayTxt">@COMMON.edit@</span>';
    var editAndDel = '<span class="cccGrayTxt">@COMMON.edit@</span> / <span class="cccGrayTxt">@COMMON.del@</span>';
    if(operationDevicePower){
    		imgStr1 = String.format("onclick='modifyBtClick(\"{0}\",\"{1}\",\"{2}\")' title='@COMMON.edit@'",record.data.cmcId,record.data.topCcmtsVlanIndex,
                    record.data.priTag);
    		imgStr2 = String.format("onclick='deleteBtClick(\"{0}\",\"{1}\",\"{2}\")' title='@COMMON.del@'",record.data.cmcId,record.data.topCcmtsVlanIndex,
                    record.data.priTag);
    }
	if(record.data.dhcpAlloc == 1){
	    if(!operationDevicePower){
	        return editStr;
	    }
		return String.format("<a href='javascript:;' {0}>@COMMON.edit@</a>", imgStr1);
	}else{
		if(ipAddr == "" || ipMask == ""){
		    if(!operationDevicePower){
	            return editStr;
	        }
			return String.format("<a href='javascript:;' {0}>@COMMON.edit@</a>", imgStr1)
		}else{
		    if(!operationDevicePower){
	            return editAndDel;
	        }
			return String.format("<a href='javascript:;' {0}>@COMMON.edit@</a> / " + "<a href='javascript:;' {1}>@COMMON.del@</a>", imgStr1,imgStr2)
		}
	}
}

function renderIpType(v, c, record){
	if (v == 2) {
		return 'Secondary';	
	} else{
		return 'Primary';	
	}
}

function renderIp(v, c, record){
	var dhcpAlloc = record.data.dhcpAlloc;
	var dhcpAllocIpAddr = record.data.dhcpAllocIpAddr;
	if (dhcpAlloc == 1) {
		return dhcpAllocIpAddr;	
	}else{
		return v;
	}
}

function renderMask(v, c, record){
	var dhcpAlloc = record.data.dhcpAlloc;
	var dhcpAllocIpMask = record.data.dhcpAllocIpMask;
	if (dhcpAlloc == 1) {
		return dhcpAllocIpMask;	
	}else{
		return v;
	}
}

function renderDhcpAlloc(v, c, record){
	if (v == 1) {
		return '@COMMON.open@';	
	} else {
		return '-';	
	}
}

function refreshVlanConfig(){
	window.top.showWaitingDlg('@COMMON.waiting@', '@text.refreshData@', 'ext-mb-waiting');
	$.ajax({
		url:"/cmcVlan/refreshCmcVlan.tv?cmcId=" + cmcId,
		type:"POST",
		success:function(){	
			window.parent.closeWaitingDlg();
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@VLAN.accessDataSuccess@</b>'
   			}); 
			onRefreshClick();
		},error:function(){
			window.parent.showMessageDlg('@COMMON.tip@', "@CMC.tip.refreshFailure@");
		}
	});
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function() {
    store = new Ext.data.GroupingStore({
             url: '/cmcVlan/loadCmcVlanList.tv',
             method: 'POST',
             baseParams:{
            	cmcId : cmcId 
             },
             reader: new Ext.data.JsonReader
             ({  
                root: 'data',
                     fields:['cmcId', 'topCcmtsVlanIndex', 'ipAddr','ipMask','priTag','priIpExist','dhcpAlloc','option60','dhcpAllocIpAddr','dhcpAllocIpMask','secVidIndex']
                }),
             remoteSort: false, //是否从后台排序
             groupField: 'topCcmtsVlanIndex',
             sortInfo:{field:'topCcmtsVlanIndex',direction:'ASC' }   
        });         
                              
   
   // 解决点击grid下方刷新图标显示不一致的问题

    var groupTpl = 'VLAN ID  {text}';

	var toolbar = [
	    {text: '@VLAN.addVLAN@', iconCls: 'bmenu_new', cls:'mL10', handler:addCmcVlan,disabled:!operationDevicePower}, '-',
		{text: '@VLAN.delVLAN@', iconCls: 'bmenu_delete', handler:deleteCmcVlan,disabled:!operationDevicePower}, '-',
		{text: '@VLAN.addSubVlanIp@', iconCls: 'bmenu_new', handler:addCmcVlanIp,disabled:!operationDevicePower}, '-',
		{text: '@CMC.title.refreshDataFromEntity@', iconCls: 'bmenu_equipment', handler: refreshVlanConfig,disabled:!refreshDevicePower}];

	var columnModels = [
	    {header: '', align:'center',width:80, sortable:false,menuDisabled:true, dataIndex: 'topCcmtsVlanIndex'},
	    {header: '@VLAN.subVlanIp@', align:'center',width:80, sortable:false,menuDisabled:true, dataIndex: 'ipAddr',renderer:renderIp},
	    {header: '<div class="txtCenter">@VLAN.mask@</div>', width:150, sortable:false,menuDisabled:true, dataIndex:'ipMask',renderer:renderMask},
	    {header: '@VLAN.autoLoadIp@', width:50, sortable:false,menuDisabled:true, dataIndex: 'dhcpAlloc', align:'center',renderer:renderDhcpAlloc},
	    {header: 'option60', width:50, sortable:false,menuDisabled:true, dataIndex: 'option60', align:'center'},
	    {header: '@VLAN.ipType@', width:60, sortable:false, groupable: false, menuDisabled:true,dataIndex: 'priTag',renderer:renderIpType},
	    {header: '@VLAN.vlanOpera@', width:60, sortable:false, groupable: false, menuDisabled:true,dataIndex:'cmcId',renderer:manuRender,align:'center'}
	];
    grid = new Ext.grid.GridPanel({
		store: store,
        animCollapse: animCollapse,
        trackMouseOver: trackMouseOver,
        border: false,
        columns: columnModels,
        bodyCssClass: "normalTable",
        view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: true,enableNoGroups: false,groupTextTpl: groupTpl
        }),
        tbar: toolbar,
		renderTo: document.body
    });
    selectionModel = grid.getSelectionModel();

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
		var record = grid.getStore().getAt(rowIndex);  // Get the Record
		this.getSelectionModel().selectRow(rowIndex);
        showEntityMenu(e,record)
    });
    selectionModel = grid.getSelectionModel();
    
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);
        showEntitySnap(record.data.entityId, record.data.ip);
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
	store.load();
});