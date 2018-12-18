var InbandVlanComboBox = Ext.extend(Ext.form.ComboBox,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        var o = this;
        this["store"] =  new Ext.data.JsonStore({
			url : "/config/loadAvailableVlanList.tv",
			autoLoad : true,
			baseParams : {
				entityId : entityId
			},
			listeners:{
				load : function(){
					// 0 代表没有配置带内vlan,所以如果是0的话则不显示
					inbandVlanId > 0 && o.setValue(inbandVlanId)
				}
			},
			root : 'data',
			fields : [ 'vlanIndex','oltVlanName' ]
		});
        InbandVlanComboBox.superclass.constructor.call(this, {
        	triggerAction : 'all',
    		width : 180,
    		shadow : false,
    		editable : !this.editable ? false : true,
    		disabled : !this.disabled ? false : true,
    		enableKeyEvents : true,
    		forceSelection : true,
    		mode: 'local',
    		lazyInit : true,
    		valueField: 'vlanIndex',
    	    displayField: 'oltVlanName'
        });
    }
});