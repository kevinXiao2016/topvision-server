function doUsmWizardLayout(){
	var authCombo = new Ext.form.ComboBox({
        id: 'authCombo',
        triggerAction: 'all',
        width:200,
        editable:false,
        transform:"authProtocol",
        listeners:{
            'select':function(f,n,o){
                if(n.data.value == "NOAUTH"){
                    $("#authLable").attr("disabled", true);
                    $("#authenticationKey").attr("disabled", true).val("");
                    //$("#authOldLable").attr("disabled", true);
                    //$("#authOldKey").attr("disabled", true);
                }else{
                	$("#authLable").attr("disabled", false);
                    $("#authenticationKey").attr("disabled", false);
                    //$("#authOldLable").attr("disabled", false);
                    //$("#authOldKey").attr("disabled", false);
                }
            }
        }
    });
    var privCombo = new Ext.form.ComboBox({
        id: 'privCombo',
        width:200,
        editable:false,
        triggerAction: 'all',
        transform:"privProtocol",
        listeners:{
            'select':function(f,n,o){
                if(n.data.value == "NOPRIV"){
                    $("#privLable").attr("disabled", true);
                    $("#privacyKey").attr("disabled", true).val("");
                    //$("#privOldLable").attr("disabled", true);
                    //$("#privOldKey").attr("disabled", true);
                }else{
                    $("#privLable").attr("disabled", false);
                    $("#privacyKey").attr("disabled", false);
                    //$("#privOldLable").attr("disabled", false);
                    //$("#privOldKey").attr("disabled", false);
                }
            }
        }
    });
    
    var cloneUserCombo = new CloneUserCombo({
    	width:200,
        editable:false,
        id: 'cloneUserCombo',
        renderTo: "cloneUser"
    });
    
    var cloneEngineCombo = new CloneEngineCombo({
    	width:200,
        editable:false,
        id: 'cloneEngineCombo',
        renderTo: "cloneEngineId"
    })
}

function beginCloneUser(store,o){
	var u = Ext.getCmp("cloneUserCombo")
	var e = Ext.getCmp("cloneEngineCombo")
	u.getStore().setBaseParam("snmpUserName",u.getValue());
	//o.snmpUserName = u.getValue();
	u.getStore().reload();
}

function beginCloneEngine(store,o){
    var u = Ext.getCmp("cloneUserCombo")
    var e = Ext.getCmp("cloneEngineCombo")
     e.getStore().setBaseParam("snmpUserName",u.getValue());
    //o.snmpUserName = u.getValue();
    e.getStore().reload();
}

//edit event handler here