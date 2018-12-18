var CloneEngineCombo = Ext.extend(Ext.form.ComboBox,{
    constructor : function(_config) {
        if (_config == null) {
            _config = {};
        }
        Ext.apply(this, _config);
        this["store"] =  new Ext.data.JsonStore({
            url : "/snmp/loadAvaiableCloneEngineList.tv",
            baseParams : {
                entityId : entityId,
                snmpUserName:null
            },
            autoLoad : true,
            root : 'data',
            fields : [ {name:'snmpUserEngineId',convert :function(v){
            	return v.replace(new RegExp(":","gm"),"");
            }}]
        });
        CloneEngineCombo.superclass.constructor.call(this, {
            triggerAction : 'all',
            editable : true,
            enableKeyEvents : true,
            valueField: 'snmpUserEngineId',
            displayField: 'snmpUserEngineId'
        });
        this.on("change",beginCloneEngine);
    }
});