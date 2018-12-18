function doVacmWizardLayout() {
	var group = new Ext.form.ComboBox({
		id: 'vacmGroup',
		triggerAction : 'all',
		enableKeyEvents : true,
		width : 200,
		editable : true,
		lazyInit : false,
		maxLength : 31,
		renderTo : "group",
		valueField: 'snmpGroupName',
	    displayField: 'snmpGroupName',
	    queryParam : "snmpSecurityLevel",
		store : new Ext.data.JsonStore({
			url : "/snmp/loadSnmpV3AccessList.tv",
			baseParams : {
				entityId : entityId
			},
			root : 'data',
			fields : [ 'snmpGroupName' ]
		}),
		listeners : {
			'blur' : groupChangeHandler,
			'select' : groupChangeHandler,
			'keyup' : function(r,e){
				if(e.getKey() == 9){
					e.preventDefault();
					e.stopPropagation();
					return false;
				}else{
					groupChangeHandler(this);
				}
			},
			beforequery  : function(e){
				e.query = thisSecurityLevel;
		    }
		}
	});
	var securityMode = new Ext.form.ComboBox({
		id : "securityMode",
		triggerAction : 'all',
		width : 200,
		editable : false,
		disabled : true,
		transform : "securityMode"
	});
	var securityLevel = new Ext.form.ComboBox({
		id : "securityLevelCombo",
		triggerAction : 'all',
		width : 200,
		editable : false,
		disabled : true,
		transform : "securityLevel"
	});
	var readView = new ViewWizardComboBox({
		id : "readViewCombo",
		lazyInit : false,
		renderTo : "readView"
		
	});
	var writeView = new ViewWizardComboBox({
		id : "writeViewCombo",
		lazyInit : false,
		renderTo : "writeView"
	});
	var notifyView = new ViewWizardComboBox({
		id : "notifyViewCombo",
		lazyInit : false,
		renderTo : "notifyView",
		listeners:{
			'keydown':function(r,e){
				if(e.getKey() == 9){
					e.preventDefault();
					e.stopPropagation();
					return false;
				}
			}
		}
	});
	
}

function doViewWizardLayout(){
	var viewMode = new Ext.form.ComboBox({
		id : "viewModeCombo",
		triggerAction : 'all',
		width : 131,
		editable : false,
		disabled : false,
		transform : "viewMode"
	});
}

// edit event handler here

function groupChangeHandler(r) {
	var v = r.getRawValue();
	if (true) {
		var foundLock = false;
		if (v == "" || v == null) {
			return;
		}
		r.getStore().each(function(item) {
			//如果存在则返回false,表明group已存在,不能进行修改停止迭代
			if (item.data.snmpGroupName == v) {
				//show config item
				disableGroupConfigItem(v);
				foundLock = true;
				useExistedGroup = true;
				return false;
			}
		});
		if (!foundLock) {
			useExistedGroup = false;
			enableGroupConfigItem();
		}
	} else {
		//$("#groupTip").html(组存在,组不能进行编辑);
	}
}

function viewChangeHandler(r) {
	var v = r.getRawValue();
	var foundLock = false;
	if (v == "" || v == null) {
		$("#"+r.id.replace("Combo","Tip")).hide();
		return;
	}
	r.getStore().each(function(item) {
		//如果存在则返回false,表明group已存在,不能进行修改停止迭代
		if (item.data.snmpViewName == v) {
			//show config item
			disableViewConfigItem(r);
			foundLock = true;
			return false;
		}
	});
	//检查完了还得检查其他combo中是否包含这个view
	var r1 = Ext.getCmp("readViewCombo").getValue();
	var r2 = Ext.getCmp("writeViewCombo").getValue();
	var r3 = Ext.getCmp("notifyViewCombo").getValue();
	var count = 0;
	if(r1 == v){
		count++;
	}
	if(r2 == v){
		count++;
	}
	if(r3 == v){
		count++;
	}
	if(count > 1){
		foundLock= true;
	}
	if (!foundLock) {
		enableViewConfigItem(r);
	}
}

function disableViewConfigItem(v) {
	var thisId = v.getId();
	if (thisId == "readViewCombo") {
		$("#readViewTip").hide();
	} else if (thisId == "writeViewCombo") {
		$("#writeViewTip").hide();
	} else if (thisId == "notifyViewCombo") {
		$("#notifyViewTip").hide();
	}
}

function enableViewConfigItem(v) {
	var thisId = v.getId();
	if (thisId == "readViewCombo") {
		$("#readViewTip").show();
	} else if (thisId == "writeViewCombo") {
		$("#writeViewTip").show();
	} else if (thisId == "notifyViewCombo") {
		$("#notifyViewTip").show();
	}
}

function enableGroupConfigItem() {
	$("#groupTip").show();
	Ext.getCmp("securityMode").enable();
	Ext.getCmp("readViewCombo").enable();
	Ext.getCmp("writeViewCombo").enable();
	Ext.getCmp("notifyViewCombo").enable();
	$("#groupLayout .lableClazz").each(function(n, item) {
		if (n == 0) {
			return
		}
		$(item).attr("disabled", false);
	});
	//清空这四个组件的所有配置
}

function disableGroupConfigItem(v) {
	$("#groupTip").hide();
	//设置这四个组件的配置为该组的配置
	Ext.getCmp("securityMode").disable();
	Ext.getCmp("readViewCombo").disable();
	Ext.getCmp("writeViewCombo").disable();
	Ext.getCmp("notifyViewCombo").disable();
	
	$("#groupLayout .lableClazz").each(function(n, item) {
		if (n == 0) {
			return
		}
		$(item).attr("disabled", true);
	});
	//将所有的提示都清掉
	$("#readViewTip,#writeViewTip,#notifyViewTip").hide();
	//清空这四个组件的所有配置,导入该组的配置信息
	$.ajax({
		url : "/snmp/loadSnmpAccessInfo.tv",
		cache : false,
		dataType : 'json',
		data : {
			entityId : entityId,
			snmpGroupName : v,
			snmpSecurityLevel : thisSecurityLevel
		},
		success : function(json) {
			var group = json.data;
			Ext.getCmp("readViewCombo").setValue(group.snmpReadView);
			Ext.getCmp("writeViewCombo").setValue(group.snmpWriteView);
			Ext.getCmp("notifyViewCombo").setValue(group.snmpNotifyView);
		},
		error : function() {
			window.parent.showErrorDlg("@COMMON.tip@", "@GROUP.fetchEr@");
		}
	})
}