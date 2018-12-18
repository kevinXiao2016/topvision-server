var cm,columnModels,sm,store,grid,viewPort,tbar;
Ext.onReady(function(){
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
    columnModels = [
        sm,
        {header: "@GPON.index@", width:40, dataIndex:"onuIpHostIndex", align:'center'},
		{header: "<div class=txtCenter>@GPON.disMode@</div>", width:100, dataIndex: "onuIpHostAddressConfigMode", align: "center",renderer:renderIpType},
        {header: "<div class=txtCenter>@GPON.ipAddress@</div>", width:100, dataIndex: "onuIpHostAddress", align: "left", renderer:renderIp},
        {header: "@GPON.subNetMask@", width:100, dataIndex: "onuIpHostSubnetMask", align: "center",renderer:renderIp},
        {header: "<div class=txtCenter>@GPON.gateWay@</div>", width:100, dataIndex: "onuIpHostGateway", align: "left",renderer:renderIp},
        {header: "<div class=txtCenter>@GPON.PreferredDNS@</div>", width:100, dataIndex: "onuIpHostPrimaryDNS", align: "left",renderer:renderIp},
        {header: "<div class=txtCenter>@GPON.AlternativeDNS@</div>", width:100, dataIndex: "onuIpHostSecondaryDNS", align: "left",renderer:renderIp},
        {header: "@GPON.VLANpriority@", width:100, dataIndex: "onuIpHostVlanTagPriority", align: "center"},
        {header: "<div class=txtCenter>VLAN ID</div>", width:100, dataIndex: "onuIpHostVlanPVid", align: "center"},
        {header: "@GPON.onuMacAdd@", width:100, dataIndex: "onuIpHostMacAddress", align: "center",renderer:renderMac},
        {header: "<div class=txtCenter>@COMMON.manu@</div>", width:100, dataIndex: "onuId", align: "center", renderer:manuRender}
    ];
    cm = new Ext.grid.ColumnModel({
        defaults : {
            menuDisabled : false
        },
        columns: columnModels
    });
    store = new Ext.data.JsonStore({
        url: "/gpon/onu/loadGponOnuIpHostList.tv",
        root: "data",
        baseParams :{onuId:onuId},
        remoteSort: true,
        fields: [
           "onuId","entityId","onuIpHostIndex","onuIpHostAddressConfigMode","onuIpHostAddress",
           "onuIpHostSubnetMask","onuIpHostGateway","onuIpHostPrimaryDNS","onuIpHostSecondaryDNS",
           "onuIpHostVlanTagPriority","onuIpHostVlanPVid","onuIpHostMacAddress"
        ]
    });
    store.load();
    tbar = new Ext.Toolbar({
        items : [
            {
            	text : "@BUTTON.add@",
            	iconCls : "bmenu_new",
            	handler: addClick
            },'-',{
            	text : "@COMMON.fetch@",
            	iconCls : "bmenu_equipment",
            	handler: refreshOnuIpHost
    		},'-',{
    			id : 'batchDeleteBtn',
    			text : "@COMMON.delete@",
    			iconCls : "bmenu_delete",
    			disabled: true,
    			handler : batchClick
    		}
        ]
    });
    grid = new Ext.grid.GridPanel({
        stripeRows:true,
        cls:"normalTable",
        bodyCssClass: "normalTable",
        region: "center",
        store: store,
        tbar : tbar,
        cm: cm,
        sm: sm,
        viewConfig:{ forceFit: true }
    });
    viewPort = new Ext.Viewport({
        cls: 'clear-x-panel-body',
        layout: 'border',
        items: [{
   	        region: 'north',
   	        height: 80,
   	        border: false,
   	        contentEl: 'topPart'
   	    },grid]
    });
});//end document.ready;
function reloadData(){
	store.reload({
		callback : function(){
			disabledBtn("batchDeleteBtn", true);
		}
	});
}
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn("batchDeleteBtn", false);
    }else{
        disabledBtn("batchDeleteBtn", true);
    }
};
function disabledBtn(id, disabled){
    Ext.getCmp(id).setDisabled(disabled);
};
function manuRender(v, o, r){
	return String.format('<a href="javascript:;" onclick="deleteClick({0},{1},{2},{3})">@COMMON.delete@</a>', 
			entityId,onuIndex,r.data.onuIpHostIndex,onuId)
}
function deleteClick(entityId,onuIndex,onuIpHostIndex,onuId){
	window.parent.showConfirmDlg("@COMMON.tip@", "@GPON.confirmDelIpHost@", function(type) {
		if (type == 'no') {
			return;
		}
		top.showWaitingDlg('@COMMON.wait@', '@COMMON.deleting@', 'ext-mb-waiting');
		$.ajax({
			url : '/gpon/onu/deleteOnuIpHost.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex,
				onuIpHostIndex : onuIpHostIndex
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.deleteOk@</b>'
	       	    });
				store.reload({
					callback : function(){
						disabledBtn("batchDeleteBtn", true);
					}
				});
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@COMMON.deleteEr@");
			},
			cache : false
		});
	});
}
function addClick(){
	var src = String.format('/gpon/onu/showAddOnuIpHostView.tv?entityId={0}&onuId={1}&onuIndex={2}',entityId, onuId, onuIndex);
	top.createDialog('addOnuIpHost', "@BUTTON.add@", 800, 500, src, null, true, true);
}
//批量删除;
function batchClick(){
	var onuIpHostIndexList = [],
	    count = grid.getSelectionModel().getCount(),
	    selections = grid.getSelectionModel().getSelections();
	
	if(count < 1){
		top.showMessageDlg("@COMMON.tip@", "@GPON.canNotDel@");
		return;
	}
	$.each(selections, function(i, v){
		onuIpHostIndexList.push(v.data.onuIpHostIndex);
	});
	var msg = String.format("@GPON.confirmDelIpHost2@",count);
	top.showConfirmDlg("@COMMON.tip@", msg, function(type) {
		if(type == "no"){ return;}
		top.showWaitingDlg('@COMMON.wait@', '@COMMON.deleting@', 'ext-mb-waiting');
		$.ajax({
			url : '/gpon/onu/batchDelOnuIpHost.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				onuId : onuId,
				onuIndex : onuIndex,
				onuIpHostIndexList : onuIpHostIndexList
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.deleteOk@</b>'
	       	    });
				store.reload({
					callback : function(){
						disabledBtn("batchDeleteBtn", true);
					}
				});
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@COMMON.deleteEr@");
			},
			cache : false
		});
	});
}
//从设备获取数据
function refreshOnuIpHost(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/gpon/onu/refreshOnuIpHost.tv',
		type : 'POST',
		data : {
			entityId : entityId,
			onuId : onuId
		},
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			store.reload({
				callback : function(){
					disabledBtn("batchDeleteBtn", true);
				}
			});
		},
		error : function(json) {
			top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}
function renderIpType(value, p, record){
	if (value == 1) {
		return 'DHCP';
	} else {
		return 'STATIC';
	}
}
function renderIp(value, p, record){
	if (value == '0.0.0.0') {
		return '--';
	} else {
		return value;
	}
}
function renderMac(value, p, record){
	if (V.formatMac(value) == '00:00:00:00:00:00') {
		return '--';
	} else {
		return value;
	}
}
function queryClick(){
	var $hostVlanId = $("#hostVlanId"),
		$selectValue = $("#selectValue"),
		hostVlanIdVal = $hostVlanId.val(),
		selectValueVal = $selectValue.val(),
	    numReg = /^[0-9]+$/,
	    ipReg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	
	//验证VLAN ID (1-4096);
	if( hostVlanIdVal != "" ){
    	if( !numReg.test(hostVlanIdVal) || hostVlanIdVal < 1 || hostVlanIdVal > 4094 ){
    		$hostVlanId.focus();
    		return;
    	}
	}
	var obj ={
		onuHostMode : $("#onuHostMode").val() == 0 ? null : $("#onuHostMode").val(),
		hostVlanPri : $("#hostVlanPri").val() == -1 ? null : $("#hostVlanPri").val()
	}
	if(selectValueVal != ""){ obj.selectValue = selectValueVal}
	if(hostVlanIdVal != ""){ obj.hostVlanId = hostVlanIdVal}
	store.load({
    	params:obj,
    	callback : function(){
    		disabledBtn("batchDeleteBtn", true);
    	} 
	});
}