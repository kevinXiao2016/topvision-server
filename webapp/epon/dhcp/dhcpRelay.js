Ext.onReady(function(){
	createGroupPanel();
	createRelayPanel();

    var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [{
	    	 border: false,
	    	 margins: '0 10 0 10',
	    	 region: 'center',
	    	 layout :'anchor',
	    	 items: [grid,grid2]
	    	 
	     }]
    });
});

function createGroupPanel(){
	columnModels = [
        {header: "@oltdhcp.groupId@", width:100, dataIndex: "topOltDhcpServerGroupIndex", align: "center", sortable: true},
        {header: "@oltdhcp.group@", width:100, dataIndex: "serverIpsDisplay", align: "center", sortable: true},
        {header: "<div class=txtCenter>@COMMON.manu@</div>", width:100, dataIndex: "dataIndex5", align: "center", sortable: false, renderer:groupManuRender}
    ];
    cm = new Ext.grid.ColumnModel({
        defaults : {
            menuDisabled : false
        },
        columns: columnModels
    });
    store = new Ext.data.JsonStore({
        url: "/epon/oltdhcp/loadOltDhcpGroupList.tv",
        root: "data",
        totalProperty: "rowCount",
        remoteSort: true,
        fields: ["entityId","topOltDhcpServerGroupIndex","topOltDhcpServerIpList","serverIpsDisplay","topOltDhcpServerBindNum"]
    });
    //store.setDefaultSort("createTime", "ASC");
    //store.load({params: {entityId:entityId,start:0,limit: 99999}});
    store.baseParams={
		entityId:entityId
    }
    store.load();
    tbar = new Ext.Toolbar({
    	cls: 'toolbarWithTitle',
        items : [{
        	text: '@COMMON.add@',
        	cls: 'pT5 pB5 pL10',
        	iconCls: "miniIcoAdd",
        	disabled: !operationDevicePower,
        	handler: function(){
        		addSeverGroup();
        	}
        	
        },'-',{
            text : "@COMMON.fetch@",
            iconCls : "miniIcoEquipment",
            disabled: !refreshDevicePower,
           	handler: function(){
        		refreshSeverGroup();
        	}
        }]
    });
    grid = new Ext.grid.GridPanel({
 		title: '@oltdhcp.group@',
    	anchor : '100%, 50%',
    	margins: '0 10 0 10',
        stripeRows:true,
        cls:"normalTable mB10",
        bodyCssClass: "normalTable",
        region: "center",
        store: store,
        tbar : tbar,
        cm: cm,
        viewConfig:{ forceFit: true }
    });
}
function createRelayPanel(){
	columnModels2 = [
        {header: "@oltdhcp.vlanifid@", width:100, dataIndex: "topOltDhcpVifIndex", align: "center", sortable: true},
        {header: "@oltdhcp.option60str@", width:100, dataIndex: "opt60StrDisplay", align: "center", sortable: true,renderer:opt60Render},
        {header: "@oltdhcp.agentAddr@", width:100, dataIndex: "topOltDhcpVifAgentAddr", align: "center", sortable: true},
        {header: "@oltdhcp.dhcpGroup@ID", width:100, dataIndex: "topOltDhcpVifServerGroup", align: "center", sortable: true,renderer:groupRender},
        {header: "<div class=txtCenter>@COMMON.manu@</div>", width:100, dataIndex: "dataIndex5", align: "center", sortable: false, renderer:relayManuRender}
    ];
    cm2 = new Ext.grid.ColumnModel({
        defaults : {
            menuDisabled : false
        },
        columns: columnModels2
    });
    store2 = new Ext.data.JsonStore({
        url: "/epon/oltdhcp/loadOltDhcpVifList.tv",
        root: "data",
        totalProperty: "rowCount",
        remoteSort: true,
        fields: ["entityId","topOltDhcpVifIndex","topOltDhcpVifOpt60StrIndex","opt60StrDisplay","topOltDhcpVifAgentAddr","topOltDhcpVifServerGroup"]
    });
    //store2.setDefaultSort("createTime", "ASC");
    //store2.load({params: {entityId:entityId,start:0,limit: 999999}});
    store2.baseParams={
		entityId:entityId
    }
    store2.load();
    tbar2 = new Ext.Toolbar({
    	cls: 'toolbarWithTitle',
        items : [{
        	text: '@COMMON.add@',
        	cls: 'pT5 pB5 pL10',
        	iconCls: "miniIcoAdd",
        	disabled: !operationDevicePower,
            handler: function(){
            	addRelay();
            }
        },'-',{
            text : "@COMMON.fetch@",
            iconCls : "miniIcoEquipment",
            disabled: !refreshDevicePower,
            handler: function(){
            	refreshRelay();
            }
        }]
    });
    grid2 = new Ext.grid.GridPanel({
 		title: '@oltdhcp.relay@',
    	anchor : '100%, 50%',
    	margins: '0 10 0 10',
        stripeRows:true,
        cls:"normalTable mB10",
        bodyCssClass: "normalTable",
        region: "center",
        store: store2,
        tbar : tbar2,
        cm: cm2,
        viewConfig:{ forceFit: true }
    });
}
function opt60Render(value,p,record){
	if (value == "") {
		return "--"; 
	}else{
		return value;
	}
}
function groupRender(value,p,record){
	if (value == "0") {
		return "--"; 
	}else{
		return value;
	}
}
//添加服务器组;
function addSeverGroup(){
	top.createDialog("modalDlg", "@oltdhcp.addGroup@", 600, 370, "/epon/oltdhcp/showAddOltDhcpGroup.tv?entityId="+entityId+"&&type=add", null, true, true,function(){
		onRefreshClick();
	});
}
//编辑服务器组;
function editSeverGroup(groupIndex){
	top.createDialog("modalDlg", "@oltdhcp.modifyGroup@", 600, 370, "/epon/oltdhcp/showModifyOltDhcpGroup.tv?entityId="+entityId+"&&groupIndex="+groupIndex+"&&type=edit", null, true, true,function(){
		onRefreshClick();
	});
}
//添加Relay规则;
function addRelay(){
	top.createDialog("modalDlg", "@oltdhcp.addRelay@", 600, 370, "/epon/oltdhcp/showAddOltDhcpVif.tv?entityId="+entityId+"&&type=add", null, true, true,function(){
		onRefreshClick2();
	});
}
//编辑Relay规则;
function editRelay(vifIndex,opt60Index){
	top.createDialog("modalDlg", "@oltdhcp.modifyRelay@", 600, 370, "/epon/oltdhcp/showModifyOltDhcpVif.tv?entityId="+entityId+"&&vifIndex="+vifIndex+"&&opt60StrIndex="+opt60Index+"&&type=edit", null, true, true,function(){
		onRefreshClick2();
	});
}
function refreshRelay(){
    window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshRelay@....", 'ext-mb-waiting');
    $.ajax({
        url:"/epon/oltdhcp/refreshOltDhcpVifList.tv?entityId="+entityId,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@oltdhcp.refreshRelay@@oltdhcp.success@！</b>'
            });
            onRefreshClick2();
        },error:function(){
        	window.top.closeWaitingDlg();
            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshRelay@@oltdhcp.fail@！");
        }
    });
}
function refreshSeverGroup(){
	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshGroup@....", 'ext-mb-waiting');
    $.ajax({
        url:"/epon/oltdhcp/refreshOltDhcpGroupList.tv?entityId="+entityId,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            window.top.closeWaitingDlg();
            top.afterSaveOrDelete({
                   title: '@COMMON.tip@',
                   html: '<b class="orangeTxt">@oltdhcp.refreshGroup@@oltdhcp.success@！</b>'
            });
            onRefreshClick();
        },error:function(){
        	window.top.closeWaitingDlg();
            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshGroup@@oltdhcp.fail@！");
        }
    });
}
function deleteServerGroup(groupIndex){
	window.top.showConfirmDlg("@COMMON.tip@", "@DHCP.delThisConfirm@", function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.deleteGroup@....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/deleteOltDhcpGroup.tv",
	        method:"post",cache: false,dataType:'text',
	        data : {
				entityId : entityId,
				groupIndex : groupIndex
			},
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">@oltdhcp.deleteGroup@@oltdhcp.success@！</b>'
	            });
	            onRefreshClick();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.deleteGroup@@oltdhcp.success@！");
	        }
	    });
	});
}
function deleteRelay(vifIndex,opt60Index){
	window.top.showConfirmDlg("@COMMON.tip@", "@DHCP.delThisConfirm@", function(type) {
		if (type == 'no') {return;}
		window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.deleteRelay@....", 'ext-mb-waiting');
	    $.ajax({
	        url:"/epon/oltdhcp/deleteOltDhcpVif.tv",
	        method:"post",cache: false,dataType:'text',
	        data : {
				entityId : entityId,
				vifIndex : vifIndex,
				opt60StrIndex : opt60Index
			},
	        success:function (text) {
	            window.top.closeWaitingDlg();
	            top.afterSaveOrDelete({
	                   title: '@COMMON.tip@',
	                   html: '<b class="orangeTxt">@oltdhcp.deleteRelay@@oltdhcp.success@！</b>'
	            });
	            onRefreshClick2();
	        },error:function(){
	        	window.top.closeWaitingDlg();
	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.deleteRelay@@oltdhcp.fail@！");
	        }
	    });
	});
}
function groupManuRender(value, p, record){
	var bindNum = record.data.topOltDhcpServerBindNum;
	if (operationDevicePower) {
		var groupIndex = record.data.topOltDhcpServerGroupIndex;
		var str;
		if(bindNum > 0){
			str = '<a href="javascript:;" onclick="editSeverGroup({0})">@COMMON.edit@</a> / <span>@COMMON.delete@</span>'.format(groupIndex);
		}else{
			str = '<a href="javascript:;" onclick="editSeverGroup({0})">@COMMON.edit@</a> / <a href="javascript:;" onclick="deleteServerGroup({0})">@COMMON.delete@</a>'.format(groupIndex);
		}
		return str;
	} else {
		return '<span>@COMMON.edit@</span> / <span>@COMMON.delete@</span>';
	}
}
function relayManuRender(value, p, record){
	if(operationDevicePower) {
		var vifIndex = record.data.topOltDhcpVifIndex;
		var opt60Index = record.data.topOltDhcpVifOpt60StrIndex;
		var str = '<a href="javascript:;" onclick="editRelay({0},\'{1}\')">@COMMON.edit@</a> / <a href="javascript:;" onclick="deleteRelay({0}, \'{1}\')">@COMMON.delete@</a>'.format(vifIndex, opt60Index);
		return str;
	} else {
		return '<span>@COMMON.edit@</span> / <span>@COMMON.delete@</span>';
	}
	
}

function onRefreshClick(){
	store.reload();
}

function onRefreshClick2(){
	store2.reload();
	refreshSeverGroupDelete();
}

function refreshSeverGroupDelete(){
    $.ajax({
        url:"/epon/oltdhcp/refreshOltDhcpGroupList.tv?entityId="+entityId,
        method:"post",cache: false,dataType:'text',
        success:function (text) {
            onRefreshClick();
        },error:function(){
        }
    });
}