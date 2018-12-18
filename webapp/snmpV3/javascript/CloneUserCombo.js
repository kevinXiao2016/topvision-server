var CloneUserCombo = Ext.extend(Ext.form.ComboBox,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        this["store"] =  new Ext.data.JsonStore({
            url : "/snmp/loadAvaiableCloneUserList.tv",
            baseParams : {
                entityId : entityId,
                snmpUserName:null
            },
            autoLoad : true,
            root : 'data',
            fields : [ 'snmpUserName',"snmpUserEngineId" ]
        });
        CloneUserCombo.superclass.constructor.call(this, {
            triggerAction : 'all',
            editable : true,
            enableKeyEvents : true,
            valueField: 'snmpUserName',
            displayField: 'snmpUserName'
        });
        this.on("change",beginCloneUser);
    }
});