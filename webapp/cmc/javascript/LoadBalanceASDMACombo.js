var LoadBalanceASDMACombo = Ext.extend(Ext.form.ComboBox,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        this["store"] =  new Ext.data.SimpleStore({
        	url : _config.url,
			fields : [ "initTechValue"/*, {name: "initTechName",mapping:"initTechValue",convert :function(v,r){
				return ASDMA[r-1];
			}}*/]
		});
        LoadBalanceASDMACombo.superclass.constructor.call(this, {
    		editable : false ,
    		valueField: 'initTechValue',
    	    displayField: 'initTechValue'
        });
    }
});
var ASDMA = ["@loadbalance.broadcastInit@","@loadbalance.unicastInit@","@loadbalance.init@","@loadbalance.direct@"]; 