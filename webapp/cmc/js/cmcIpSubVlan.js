var CmcIpSubVlanGrid = Ext.extend(Ext.grid.GridPanel, {
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
        	url : "/cmcVlan/loadCmcIpSubVlanCfg.tv",cache:false,
            baseParams:{cmcId: entityId},
            autoLoad : true,
            remoteSort: true,
            fields : [
                'cmcId','topCcmtsIpSubVlanIfIndex','topCcmtsIpSubVlanIpIndex','topCcmtsIpSubVlanIpMaskIndex',
                'topCcmtsIpSubVlanVlanId','topCcmtsIpSubVlanPri'
            ]
        });

        CmcIpSubVlanGrid.superclass.constructor.call(this, {
            stripeRows : true, // 交替行效果
            viewConfig : {forceFit : true},
            // 列的定义
            columns : [{
                        header : 'IP',
                        dataIndex : "topCcmtsIpSubVlanIpIndex",
                        sortable : true
                    },{
                        header : "@VLAN.subMask@",
                        dataIndex : "topCcmtsIpSubVlanIpMaskIndex",
                        sortable : true
                    }, {
                        header : "VLAN ID",
                        dataIndex : "topCcmtsIpSubVlanVlanId",
                        sortable : true
                    },{
                        header : "@VLAN.vlanPri@",
                        dataIndex : "topCcmtsIpSubVlanPri",
                        sortable : true
                    },{
                    	header: "@VLAN.vlanOpera@", 
                    	align: 'center', 
                    	width:60,
                    	renderer: this.manuRender
            }],
            
            /*listeners:{
            	rowclick : function(g,row,e){
            		var record = this.getStore().getAt(row);
            		newIp.setValue(record.data.topCcmtsIpSubVlanIpIndex);
            		newMask.setValue(record.data.topCcmtsIpSubVlanIpMaskIndex);
            		$('#vlanId').val(record.data.topCcmtsIpSubVlanVlanId);
            		$('#vlanPri').val(record.data.topCcmtsIpSubVlanPri);
            		$(".modifyBtn").show();
            		$(".backBtn").show();
            		$("input:text").attr("disabled", true);
            		$(".addBtn").hide();
            	}
            },*/
            loadMask : {msg : "Loading...."},
            // 选择模式
            selModel : new Ext.grid.RowSelectionModel({
                           singleSelect : true
                       })
        });
    },
    manuRender: function(v, c, record){
        var imgStr2 = String.format("onclick='deleteBtClick(\"{0}\",\"{1}\",\"{2}\")' title='@COMMON.del@'",
                record.data.cmcId,record.data.topCcmtsIpSubVlanIpIndex,
                record.data.topCcmtsIpSubVlanIpMaskIndex);
        var noPowerString = '<span class="cccGrayTxt">@COMMON.del@</span>';
        if(operationDevicePower){
            return String.format("<a  href='javascript:;' {0}>@COMMON.del@</a>", imgStr2);
        }else{
            return noPowerString;
        }
    }
});
 
