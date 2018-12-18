var SnmpV3UserGrid = Ext.extend(Ext.grid.GridPanel, {
    myid : "snmpV3UserGrid",
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
        
        var reader = new Ext.data.JsonReader({
    	    root: "data",
            fields: [
                'snmpUserEngineId','snmpUserName','snmpAuthProtocol','snmpPrivProtocol','snmpGroupName',"snmpSecurityMode","useNotifyUser"
            ]
    	});
        this["store"] = new Ext.data.GroupingStore({
        	url : "/snmp/loadSnmpV3UserList.tv",cache:false,
            baseParams:{entityId: entityId},
            reader : reader,
            groupField: 'snmpUserEngineId',
            sortInfo: {
                field: 'snmpUserEngineId',
                direction: 'DESC' // or 'DESC' (case sensitive for local sorting)
            },
            remoteGroup: false,
            groupOnSort: false,
    		remoteSort: false
        });
        this["store"].setDefaultSort('snmpUserEngineId', 'DESC');
        SnmpV3UserGrid.superclass.constructor.call(this, {
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',
            view: new Ext.grid.GroupingView({
                forceFit: true, hideGroupedColumn: true,enableNoGroups: true,groupTextTpl: "{group}"
            }),
            // 列的定义
            columns : [ {
		                header : "Remote Engine",
		                renderer: this.engineRender,
		                dataIndex : "snmpUserEngineId"
		            },{
                        header : "@USER.name@",
                        dataIndex : "snmpUserName",
                        sortable : true
                    },{
                        header : "@USER.userType2@",
                        dataIndex : "useNotifyUser",
                        renderer: this.userTypeRender
                    },  {
                        header : "@USER.authPro2@",
                        dataIndex : "snmpAuthProtocol"
                    }, {
                        header : "@USER.privPro2@",
                        dataIndex : "snmpPrivProtocol"
                    }, {
                        header : "@USER.belongGroup@",
                        dataIndex : "snmpGroupName"
                    }, {
                    	header : "@COMMON.manu@",
                    	renderer: this.manuRender
                    },{
                    	header : "@USER.testResult@",
                    	dataIndex : "snmpGroupName",
                        renderer: this.testRenderer
             }],
            loadMask : {msg : "@COMMON.loadingMask@"},
            // 选择模式
            selModel : new Ext.grid.RowSelectionModel({ singleSelect : true })
        });
    },
/////////////////////////  handers  //////////////////////////////
    testRenderer: function(v,m,r){
    	if(r.data.useNotifyUser == 1){
    		return "<div style='color:gray;font-style:italic'>@USER.canotTest@</div>";
    	}
    	return String.format("<div id='{0}Test' style='color:gray;font-style:italic'>@USER.forTesting@</div>", r.id);
    },
    engineRender : function(v,m,r){
    	v = v.replace(new RegExp(":","gm"),"");
    	/*if(r.data.useNotifyUser){
    		return String.format("Remote Engine: {0}",v);
    	}else{
    		return String.format("Local Engine: {0}",v);
    	}*/
    	return String.format("Engine: {0}",v);
    },
    userTypeRender : function(v,m,r){
    	if(v == -1){
    		return "@USER.notRecognize@";
    	}else if(v == 1){
    		return "@USER.notifyUser@";
    	}else{
    		return "@USER.accessUser@";
    	}
    },
    manuRender : function  (v, c, record){
        //return "<div id='{0}Test' style='color:gray;font-style:italic'>@USER.noaction@</div>";
        if(operationDevicePower){
            var imgStr1 = String.format("onclick='modifyBtClick(\"{0}\")' title='@COMMON.modify@'", record.id);
            var imgStr2 = String.format("onclick='deleteBtClick(\"{0}\")' title='@COMMON.del@'", record.id);
            if(record.data.useNotifyUser == 1){
                return String.format("<img src='/images/edit.gif' {0} /><img style='margin-left:5px;' src='/images/delete.gif' {1} />",imgStr1, imgStr2);
            }else{
                var imgStr3 = String.format("onclick='tryUserValid(this,\"{0}\")' title='@USER.test@'", record.id);
                return String.format("<img src='/images/edit.gif' {0} /><img style='margin-left:5px;' src='/images/delete.gif' {1} />" +
                        "<img style='margin-left:5px;' src='/images/test.png' {2} />",imgStr1, imgStr2, imgStr3);
            }
        }else{
            var imgStr1 = "title='@COMMON.modify@'";
            var imgStr2 = "title='@COMMON.del@'";
            var imgStr3 = "title='@USER.test@'";
            return String.format("<img src='/images/editDisable.gif' {0} /><img style='margin-left:5px;' src='/images/deleteDisable.gif' {1} />" +
                    "<img style='margin-left:5px;' src='/images/testDisable.png' {2} />",imgStr1, imgStr2, imgStr3);
        }
    }
});

