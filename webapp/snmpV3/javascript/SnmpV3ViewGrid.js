var SnmpV3ViewGrid = Ext.extend(Ext.grid.GridPanel, {
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
        	url : "/snmp/loadSnmpV3ViewList.tv",cache:false,
            baseParams:{entityId: entityId},
            root : 'data',
            fields : [
                'snmpViewName','snmpViewSubtree','snmpViewMask','snmpViewMode'
            ]
        });

        SnmpV3ViewGrid.superclass.constructor.call(this, {
            stripeRows:true,region: "center",bodyCssClass: 'normalTable',
            viewConfig : {forceFit : true},
            // 列的定义
            columns : [{
                        header : "@VIEW.viewName2@",
                        dataIndex : "snmpViewName",
                        sortable : true
                    },{
                        header : "@VIEW.filterCondition2@",
                        dataIndex : "snmpViewSubtree",
                        renderer : this.subtreeMerge,
                        sortable : true
                    }, {
                        header : "@VIEW.execAction2@",
                        dataIndex : "snmpViewMode",
                        renderer : this.snmpViewModeRender,
                        sortable : true
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
    subtreeMerge: function (tree,m,r){
    	var re = [];
    	var mask = r.data.snmpViewMask;
    	mask = parseInt(mask, 16).toString(2);
    	tree = tree.split(".");
    	var len = tree.length;
    	var m = mask.length;
    	for(var a=0; a<len; a++){
    		if(a < m && mask.substring(a, a + 1) == '0'){
    			re.push("*");
    			continue;
    		}
    		re.push(tree[a]);
    	}
    	return re.join(".");
    },
    snmpViewModeRender: function(v,m,r){
    	if(v == 1){
    		return I18N.VIEW.include;
    	}else{
    		return I18N.VIEW.exclude;
    	}
    },
    manuRender: function(v, c, record){
    	if(operationDevicePower){
            return String.format("<a href='javascript:;' onclick='modifyBtClick(\"{0}\")'>@COMMON.modify@</a> / " +
                                 "<a href='javascript:;' deleteBtClick(\"{0}\")'>@COMMON.del@</a>",record.id);
        }else{
            return "<a href='javascript:;' disabled>@COMMON.modify@</a> / <a href='javascript:;' disabled>@COMMON.del@</a>";
        }
    }
});
 

