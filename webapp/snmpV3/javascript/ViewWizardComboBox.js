var ViewWizardComboBox = Ext.extend(Ext.form.ComboBox,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        this["store"] =  new Ext.data.JsonStore({
			url : "/snmp/loadSnmpV3ViewNameList.tv",
			baseParams : {
				entityId : entityId
			},
			root : 'data',
			fields : [ 'snmpViewName' ]
		});
        ViewWizardComboBox.superclass.constructor.call(this, {
        	triggerAction : 'all',
    		width : 200,
    		editable : !this.editable ? false : true,
    		disabled : !this.disabled ? false : true,
    		enableKeyEvents : true,
    		valueField: 'snmpViewName',
    	    displayField: 'snmpViewName'
        });
        if(typeof viewChangeHandler == 'function'){
        	this.on("keyup",viewChangeHandler);
        	this.on("select",viewChangeHandler);
        }
    }
});