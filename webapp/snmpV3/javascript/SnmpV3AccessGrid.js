var SnmpV3AccessGrid = Ext.extend(Ext.grid.GridPanel, {
    addWin : null,
    checkWin : null,
    /**
     * 构造方法
     */
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        this["store"] = new Ext.data.JsonStore({
        	url : "/snmp/loadSnmpV3AccessList.tv",cache:false,
            baseParams:{entityId: entityId},
            root : 'data',
            fields : [
                'snmpContextPrefix','snmpSecurityModel','snmpSecurityLevel','snmpContextMatch','snmpReadView',
                'snmpWriteView','snmpNotifyView','snmpSecurityMode','snmpGroupName'
            ]
        });

        SnmpV3AccessGrid.superclass.constructor.call(this, {
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',
            viewConfig : {forceFit : true},
            // 列的定义
            columns : [{
                        header : "@GROUP.accessName@",
                        dataIndex : "snmpGroupName",
                        sortable : true
                    },{
                        header : "@GROUP.securityLevel@",
                        dataIndex : "snmpSecurityLevel",
                        renderer: snmpSecurityLevelRender
                    }, {
                        header : "@GROUP.readView2@",
                        dataIndex : "snmpReadView",
                        renderer : snmpViewRender
                    }, {
                        header : "@GROUP.writeView2@",
                        dataIndex : "snmpWriteView",
                        renderer : snmpViewRender
                    }, {
                        header : "@GROUP.notifyView2@",
                        dataIndex : "snmpNotifyView",
                        renderer : snmpViewRender
                    },{
                    	header: "@COMMON.manu@", 
                    	align: 'center', 
                    	renderer: this.manuRender
             }],         
            loadMask : {msg : "@COMMON.loadingMask@"},
            // 选择模式
            selModel : new Ext.grid.RowSelectionModel({
                           singleSelect : true
                       })
        });
    },
    manuRender: function(v, c, record){
    	if(operationDevicePower){
    		var imgStr1 = String.format("onclick='modifyBtClick(\"{0}\")' title='@COMMON.modify@'",record.id);
    		var imgStr2 = String.format("onclick='deleteBtClick(\"{0}\")' title='@COMMON.del@'",record.id);
    		return String.format("<img src='/images/edit.gif' {0}><img style='margin-left:5px;' src='/images/delete.gif' {1}>", 
    				imgStr1, imgStr2);
    	}else{
    		var imgStr1 = "title='@COMMON.modify@'";
    		var imgStr2 = "title='@COMMON.del@'";
    		return String.format("<img src='/images/editDisable.gif' {0}><img style='margin-left:5px;' src='/images/deleteDisable.gif' {1}>", 
    				imgStr1, imgStr2);
    	}
    }
});
/////////////////////////  handers  //////////////////////////////
function snmpSecurityLevelRender(v,m,r){
	if(v==1){
		return "noauthnopriv";
	}else if(v == 2){
		return "authnopriv";
	}else if(v == 3){
		return "auth_priv";
	}
}

function snmpViewRender(v,m,r){
	if(v == "" || v == null){
		return "-";
	}else{
		return v;
	}
}

