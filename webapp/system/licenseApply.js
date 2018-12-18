//生成带有所有IP地址的下拉菜单
var engineNumCombo;
var licenseTimeCombo;
var userNumCombo;
var productNameCombo;
var oltNumCombo;
var onuNumCombo;
var cmcNumCombo;
var cmtsNumCombo;
var licenseType = 'trial';//记录类型是 试用 还是 商用;
var reportIds; //以&分割开的报表ID字符串
var entityTypes;
//设备类型字符串
var CMCI;
var CMCII;
var CMTS;
var OLT;
var ONU;
var entityTypeTree = null,reportTree = null;
function tabFn(num){
	switch (num){
		case 0:
			licenseType = 'trial';
			break;
		case 1:
			licenseType = 'commercial';
			break;
	}
};

function createTree(){
	entityTypeTree = new dhtmlXTreeObject("putTree", "100%", "100%", 0);
	entityTypeTree.setImagePath("../js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	entityTypeTree.enableCheckBoxes(1);
	entityTypeTree.enableThreeStateCheckboxes(true);
	entityTypeTree.loadXML("/system/loadAllDeviceTypes.tv");
	entityTypeTree.attachEvent("onCheck",function(itemId){ 
		//当某种设备大类都未选择时，对应的报表均不勾选
		changeReportTreeByEntityTypeTree()
	}); 
	
	reportTree = new dhtmlXTreeObject("putTree2", "100%", "100%", 0);
	reportTree.setImagePath("../js/dhtmlx/tree/imgs/dhxtree_skyblue/");
	reportTree.enableCheckBoxes(1);
	reportTree.enableThreeStateCheckboxes(true);
	reportTree.loadXML("/system/loadAllReports.tv");
	
}
var licenseTypeNo = 0;
Ext.onReady(function(){
	//后台获取当前license数据，初始化
	$.ajax({
        url:'/system/getCuLicView.tv',
        type:'get',
        data:{},
        dateType:'json',
        success:function(response) {
        	var licView = response.licView
        	var version = response.version.buildVersion
        	//设备类型的name字符串拼接，用于控制设备类型树
        	CMCI = response.CMCI;
        	CMCII = response.CMCII;
        	CMTS = response.CMTS;
        	OLT = response.OLT;
        	ONU = response.ONU;
        	//设置基本信息值
        	$('#man').attr("value", (licView.user=="Victor" ? "" : licView.user));
        	$('#phone').attr("value", (licView.phone=="02787800697" ? "" : licView.phone));
        	$('#mobilePhone').attr("value", (licView.mobile=="13971079829" ? "" : licView.mobile));
        	$('#email').attr("value", (licView.email=="13971079829@139.com" ? "" : licView.email));
        	$('#emsVersion').attr("value", version);
        	$('#company').attr("value", (licView.organisation=="鼎点视讯科技有限公司" ? "" : licView.organisation));
        	if(licView.numberOfDays == '2147483647'){
        		licenseTimeCombo.setValue(-1);
        	}else{
        		licenseTimeCombo.setValue(licView.numberOfDays);
        	}
        	if(licView.numberOfUsers == '2147483647'){
        		userNumCombo.setValue(-1);
        	}else{
        		userNumCombo.setValue(licView.numberOfUsers);
        	}
        	if(licView.numberOfEngines == '2147483647'){
        		engineNumCombo.setValue(-1);
        	}else{
        		engineNumCombo.setValue(licView.numberOfEngines);
        	}
        	if(licView.licenseType == 'commercial'){
        		licenseTypeNo = 1;
        	}
        	var tab1 = new Nm3kTabBtn({
        	    renderTo:"licenseType",
        	    callBack:"tabFn",
        	    selectedIndex : licenseTypeNo,
        	    tabs:["@license.trial@","@license.commercial@"]
        	});
        	tab1.init();
    		//设置模块信息值
        	initModuleCheck(licView.modules)
        	//设置设备树及报表树
        	initReportTree(licView)
        	initEntityTree(licView)
        },
        error:function() {
        	
        },
        cache:false
    });
	
	
	engineNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[1, '1'], [5, '5'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'numberOfEngines'
	});
	engineNumCombo.setValue('1')
	
	licenseTimeCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[90, '90'], [180, '180'], [365, '365'], [-1, '@license.forever@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'numberOfDays'
	});
	licenseTimeCombo.setValue('90')
	
	userNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[5, '5'], [10, '10'], [30, '30'], [50, '50'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'numberOfUsers'
	});
	userNumCombo.setValue('10')
	
	oltNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[10, '10'], [20, '20'], [50, '50'], [100, '100'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'oltNum'
	});
	oltNumCombo.setValue('20')
	
	onuNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[100, '100'], [200, '200'], [500, '500'], [1000, '1000'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'onuNum'
	});
	onuNumCombo.setValue('200')
	
	cmcNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[100, '100'], [200, '200'], [500, '500'], [1000, '1000'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'cmcNum'
	});
	cmcNumCombo.setValue('200')
	
	cmtsNumCombo = new Ext.form.ComboBox({
	    triggerAction: 'all',
	    editable: true,
	    mode: 'local',
	    store: new Ext.data.ArrayStore({
	        id: 0,
	        fields: [
	            'myId',
	            'displayText'
	        ],
	        data: [[10, '10'], [20, '20'], [50, '50'], [100, '100'], [-1, '@SYSTEM.license.unlimited@']]
	    }),
	    valueField: 'myId',
        displayField: 'displayText',
	    applyTo : 'cmtsNum'
	});
	cmtsNumCombo.setValue('20')
	
	createTree();	
	
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [{
	    	 region : 'east',
	    	 layout :'anchor',
	    	 width : '316',
	    	 items : [{
	    		 title : '@license.checkYourModule@',
	    		 anchor : '100%, 50%',
	    		 autoScroll: true,
	    		 border : false,
	    		 cls : 'eastBg',
	    		 height : 300,
	    		 contentEl : 'putSide'
	    	 },{
	    		 cls : 'eastBg oneTopLine',
	    		 anchor : '100%, 50%',
	    		 autoScroll: true,
	    		 border : false,
	    		 title : '@license.checkYourReport@',
	    		 contentEl : 'putReport'
	    	 }]
	     },{
	    	 autoScroll: true,
	    	 border : false,
	    	 region : 'center',
	    	 contentEl : 'putMainPart'
	     }]
	});
	
	//模块checkbox点击事件
	$("#olt, #onu, #cmc, #cmts").click(function(){
		var $me = $(this),
			clickId = $me.attr("id"),
		    ck = $me.prop("checked"),
		    $tr = $me.parent().parent(),
		    $text = $tr.find(":text"),
		    combo,
			module;
		switch(clickId){
			case "olt":
				combo = oltNumCombo;
				module = 'OLT';
				entityTypeModule = 'olt'
				entityTypeNames = OLT
				break;
			case "onu":
				combo = onuNumCombo;
				module = 'ONU';
				entityTypeModule = 'onu'
				entityTypeNames = ONU
				break;
			case "cmc":
				combo = cmcNumCombo;
				entityTypeModule = 'cmc'
				module = 'CMC';
				entityTypeNames = CMCI + CMCII
				break;
			case "cmts":
				combo = cmtsNumCombo;
				entityTypeModule = 'cmts'
				module = 'CMTS';
				entityTypeNames = CMTS
				break;
		}
		
		if(ck){
			combo.setDisabled(false);
			changeReportTreeByModule(module, true)
			changeEntityTypeTreeByModule(entityTypeModule, true, entityTypeNames)
		}else{
			combo.setDisabled(true);
			changeReportTreeByModule(module, false)
			changeEntityTypeTreeByModule(entityTypeModule, false, entityTypeNames)
		}
		
		//olt与onu checkbox级联
		if(module == 'OLT'){
			if(ck){
				//$('#onu').prop("checked", true)
	    		//$('#onu').click();
	    		//$('#onu').prop("checked", true)
	    		$('#onu').attr("disabled", false)
			}else{
				$('#onu').prop("checked", false)
	    		$('#onu').click();
	    		$('#onu').prop("checked", false)
	    		onuNumCombo.setDisabled(true);
	    		$('#onu').attr("disabled", true)
			}
		}
	})
	
	//报表checkbox点击事件
	$("#report").click(function(){
			var $me = $(this),
			ck = $me.prop("checked");
			 if(!ck){
				 disableAndUncheckTree(reportTree);
			 }else{
				 // modify by fanzidong,勾选报表checkbox时，应当根据具体选择模块来决定选择哪些报表
				 if($('#olt').prop("checked") && $('#onu').prop("checked")
					 && $('#cmc').prop("checked") && $('#cmts').prop("checked")){
					 enableAndCheckTree(reportTree);
				 }
				 $('#olt').prop("checked") && changeReportTreeByModule('OLT', true);
				 $('#onu').prop("checked") && changeReportTreeByModule('ONU', true);
				 $('#cmc').prop("checked") && changeReportTreeByModule('CMC', true);
				 $('#cmts').prop("checked") && changeReportTreeByModule('CMTS', true);
				 modifyAlertTreeItem();
			 }
			})
});
function backFn(){
	window.location.href = "/system/showLicense.tv";
}
function modifyAlertTreeItem(){
	var ck;
	//如果报表选项选中了，并且至少选中了一个模块;
	if( $('#report').prop("checked") && 
		($('#olt').prop("checked") || $('#onu').prop("checked") || $('#cmc').prop("checked") || $('#cmts').prop("checked")) 
	){
		ck = true;
	}else{
		ck = false;
	}
	modifyTreeItemByCk(ck, 'curAlert-ALL', reportTree);
	modifyTreeItemByCk(ck, 'hisAlert-ALL', reportTree);
	modifyTreeItemByCk(ck, 'currentAlertDetail-ALL', reportTree);
	modifyTreeItemByCk(ck, 'historyAlertDetail-ALL', reportTree);
	modifyTreeItemByCk(ck, 'alert-', reportTree);
}