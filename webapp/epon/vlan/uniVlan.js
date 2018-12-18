var uniTabInited = false,
	uniVlanStore,
	uniVlanGrid,
	selOnuId;

function createUniVlanList(){
	//加载下级设备视图
	loadOnuList();
	
	//创建UNI口表格
	var cm =  new Ext.grid.ColumnModel([
  	    {header:'@VLAN.portNum@', dataIndex:'portLocation', align:'center', width: 0.2, sortable:true},
  	    {header: "PVID", sortable:true, align:'center', width: 0.2, dataIndex: 'vlanPVid'},
  	    {header:'PRI', dataIndex:'vlanTagPriority', align:'center', width: 0.2 ,sortable:true},
  	    {header:'@VLAN.vlanMode@', dataIndex:'vlanMode', align:'center', width: 0.2 ,sortable:true, renderer: univVlanModeRender},
  	  	{header:'@VLAN.bindTemplate@', dataIndex:'profileName', align:'center', width: 0.2, sortable:true},
  	    {header: "@COMMON.manu@", dataIndex:'vlanMode', width: 200, align:'center', fixed : true, renderer: uniMenuRenderE},
  	    {header: "@COMMON.manu@", dataIndex:'vlanMode', width: 200, align:'center', fixed : true, renderer: uniMenuRenderG}
  	]);
	
	uniVlanStore = new Ext.data.JsonStore({
		url: '/epon/vlanList/loadUniPortVlan.tv',
   	    fields: ['entityId', 'uniIndex', 'uniId', 'profileName', 'vlanPVid', 'vlanMode','portLocation','vlanTagPriority']
   	});
	
	uniVlanGrid = new Ext.grid.GridPanel({
   		stripeRows: true,
   		columnLines: true,
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: uniVlanStore,
   		cm : cm,
   		viewConfig:{
   			forceFit: true
   		},
   		tbar: new Ext.Toolbar({
	    	items: [
	    	    {xtype: 'spacer', width:5},
	         	{text: '@VLAN.refreshDeviceVlan@', id: 'refreshVlan_button4', iconCls: 'bmenu_refresh', handler: refreshDeviceVlan}
	       	]
     	}),
   		height: $('#center-container').height(),
	    renderTo: "uniGridPanelContainer"
   	});
	
	uniTabInited = true;
}

function refreshUniVlanList(){
	uniVlanStore.load({params: {onuId:selOnuId}});
}

function loadOnuList(){
	$.ajax({
		url: '/epon/vlanList/loadOnuList.tv',
		data: {
			entityId: entityId
		},
		type : 'POST',
		dataType : "json",
		success : function(onuList) {
			var newDom = '<ul id="newtree" class="filetree">';
			for(var i=0; i < onuList.length; i++){
				var slot = onuList[i];
				var portArray = slot.children;
				newDom +=    '<li>';
				newDom +=     '<span class="folder">slot:'+ slot.slotNo +'</span>';
				newDom +=     '<ul>';
				for(var j=0; j<portArray.length; j++){
					var port = portArray[j];
					var subDevice = port.children;
					newDom +=    '<li>';
					newDom +=     '<span class="folder">port:'+ port.portNo +'</span>';
					newDom +=     '<ul>';
					for(var k=0; k < subDevice.length; k++){
						var entity = subDevice[k];
						var online = (entity.oprationStatus == 1) ? 'onlineLink' : 'offlineLink';
						newDom += String.format('<li><span><a href="javascript:;" class=" TREE-NODE yellowLink {0}" mac="{4}" name="{3}" onuid="{1}" onclick="showOnuUni({1},\'{2}\')">{3}</a></span></li>', online, entity.entityId, entity.onuEorG, entity.entityName, entity.onuMac);
					}
					newDom += '</ul>';
					newDom += '</li>';
				}
				newDom += '</ul>';
				newDom += '</li>';
			}
			newDom += '</ul>';
			$("#uniTreeContainer").html(newDom);
			//加载树形菜单;
			$("#newtree").treeview({ 
				animated: "fast"
			});	//end treeview;

			$(".TREE-NODE").click(function(){
				$(".TREE-NODE").removeClass("selectedTree");
				$(this).addClass("selectedTree");
			});
			var $first = $(".TREE-NODE").eq(0);
			$first && $first.click();
			
			//$("#newtree a.yellowLink:eq(0)").addClass("selectedTree");
		},
		error : function(response) {
			window.parent.showMessageDlg(I18N.COMMON.message, '@VLAN.loadOnuFailed@');
		},
		cache : false,
		complete : function(XHR, TS) {
			XHR = null
		}
	});
}

function univVlanModeRender(vlanMode){
	switch(vlanMode){
	case 0:
		return "transparent";
    case 1:
        return "tag";
    case 2:
        return "translation";
    case 3:
        return "aggregation";
    case 4:
        return "trunk";
    case 5:
        return "stacking";
	}
}

function uniMenuRenderE(vlanMode){
	return '<a class="yellowLink" href="javascript:showUniVlanConfig();">@VLAN.vlanCfg@</a>';
}
function uniMenuRenderG(vlanMode){
	return '<a class="yellowLink" href="javascript:showGponUniVlanConfig();">@VLAN.vlanCfg@</a>';
}

function showOnuUni(onuId, onuEorG){
	selOnuId = onuId;
	var column = uniVlanGrid.getColumnModel();
	if(onuEorG == "G"){//gpon;
		column.setHidden(3,true);
		column.setHidden(4,true);
		column.setHidden(5,true);
		column.setHidden(6,false);
	}else{//epon;
		column.setHidden(3,false);
		column.setHidden(4,false);
		column.setHidden(5,false);
		column.setHidden(6,true);
	}
	uniVlanStore.load({params: {onuId:onuId}});
}

function showUniVlanConfig(){
	//获取当前选中的UNI口
	var selected = uniVlanGrid.getSelectionModel().getSelections(),
		selUniPort = selected[0].data;
	if(VC.support('uniVlanBindInfo', entityId)){
		window.top.createDialog('uniVlanBindInfo', "@ONU.uniVlanInfo@", 600, 370, 'epon/univlanprofile/showUniBindInfo.tv?uniId=' +selUniPort.uniId + '&entityId=' + entityId+"&onuId="+selOnuId, null, true, true, function(){
			refreshUniVlanList();
		});
	}else if (VC.support('uniPortVlan', entityId)) {
		window.top.createDialog('uniPortVlan', "@ONU.uniVlanInfo@", 800, 500, '/epon/uniportvlan/showUniVlanView.tv?uniId=' +selUniPort.uniId + '&entityId=' + entityId, null, true, true, function(){
			refreshUniVlanList();
		});
	}
}

function showGponUniVlanConfig(){
	var selected = uniVlanGrid.getSelectionModel().getSelections(),
	selUniPort = selected[0].data;
	window.top.createDialog('uniVlanConfig', '@onu/EPON.vlanCfg@', 600, 370, '/gpon/onu/showUniVlanView.tv?&uniId=' + selUniPort.uniId, null, true, true, function(){
		refreshUniVlanList();
	});
}