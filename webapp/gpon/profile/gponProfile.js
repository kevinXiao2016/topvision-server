// 主页面各模版的store，grid变量的容器
var profileDocker = {};

// 选中的模版
var selectProfile = {
	type: null,
	module: null
};

$(function(){
	top.intermediateObject.gponprofile = {};
	
	//初始化页面布局
	new Ext.Viewport({
	    layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'header-container',
	        height: 80
	      },
	      {
	    	  region: 'center',
	    	  border: false,
	    	  contentEl: 'center-container'
	      }
	    ]
	});
	
	//初始化tab签
	initTabs();
	
	$(window).resize(function() {
		//获取当前center-container的size
		var centerWidth = $('#center-container').width(),
			centerHeight = $('#center-container').height();
		profileDocker[selectProfile.type].grid.setSize(centerWidth-20, centerHeight);
	});
});

// GPON模版页，主模版的tab签信息
var profileTypeTabs = [{
	value: 'lineProfile',
	name: '@GPON.lineProfile@'
}, {
	value: 'serviceProfile',
	name: '@GPON.serviceProfile@'
}, {
	value: 'dbaProfile',
	name: '@GPON.dbaProfile@'
}, {
	value: 'trafficProfile',
	name: '@GPON.trafficProfile@'
}];

/**
 * 根据模版类型，获取其在tab签中的位置
 * @param {String} type 模板类型
 * @returns {Number}
 */
function getTabIndexByType(type) {
	for(var i=0, len=profileTypeTabs.length; i<len; i++) {
		if(profileTypeTabs[i].value === type) {
			return i;
		}
	}
}

/**
 * 初始化tab布局
 */
function initTabs() {
	// add by fanzidong, 根据版本支持情况，决定是否显示SIP代理模板
	if(supportSIP) {
		profileTypeTabs.push({
			value: 'sipAgentProfile',
			name: '@GPON.sipAgentProfile@'
		});
	}
	if(supportVOIP){
		profileTypeTabs.push({
			value: 'voipMediaProfile',
			name: '@GPON.voipMediaProfile@'
		});
	}
	if(supportSIPServiceData){
		profileTypeTabs.push({
			value: 'sipServiceDataProfile',
			name: '@GPON.sipServiceData@'
		});
	}
	if(supportDigitalGraphics){
		profileTypeTabs.push({
			value: 'digitalGraphicsProfile',
			name: '@onuvoip.digitalChartTemplate@'
		});
	}
	
	segmentButton = new SegmentButton("tab-container", profileTypeTabs, {
		callback: function(v) {
			var index = getTabIndexByType(v)
			switchTab(index)
		}
	})
	
	// 填充tab-container
	var shownDivTemplate = '<div class="edge10 pT0 clearBoth tabBody" id="{0}-container" style="height:100%;"></div>';
	var hiddenDivTemplate = '<div class="edge10 pT0 clearBoth tabBody" id="{0}-container" style="display:none;height:100%;"></div>';
	Ext.each(profileTypeTabs, function(tab, i) {
		if(i==0) {
			$('#center-container').append(String.format(shownDivTemplate, tab.value));
		} else {
			$('#center-container').append(String.format(hiddenDivTemplate, tab.value));
		}
	});
	
	segmentButton.init();
}

/**
 * 切换tab
 * @param {Integer} index tab在tab签中的index
 */
function switchTab(index) {
	$(".tabBody").css("display","none");
	$(".tabBody").eq(index).fadeIn();
	
	if(!isProfileTypeInited(profileTypeTabs[index].value)) {
		initProfile(profileTypeTabs[index].value)
	} else {
		selectProfile.type = profileTypeTabs[index].value;
		selectProfile.module = chooseModule(profileTypeTabs[index].value);
	}
}

/**
 * 指定的模板类型有没有初始化
 * @param {String} profileType 模板类型
 * @returns {Boolean}
 */
function isProfileTypeInited(profileType) {
	return profileDocker[profileType] !== undefined;
}

/**
 * 初始化指定的模板
 * @param {String} profileType 模板类型
 */
function initProfile(profileType) {
	selectProfile.type = profileType;
	selectProfile.module = chooseModule(profileType);
	
	// construct column model
	var cm = createColumnModel(selectProfile.module);
	
	// construct store
	var profileStore = new Ext.data.JsonStore({
  		url: selectProfile.module.route.loadList,
  		baseParams: {
  			entityId: entityId
  		},
 	    fields: selectProfile.module.getFields()
  	});
	
	// construct toolbar
	var toolbar = createToolbar(selectProfile.module);
	
	// construct gridpanel
	var profileGrid = createGridPanel(profileStore, cm, toolbar, profileType + '-container');
	
	profileStore.on('load', function() {
		var addButton = profileDocker[selectProfile.type].grid.getTopToolbar().getComponent("addButton");
		setAddButtonStateByRange(selectProfile.module, profileStore, addButton);
	});
	profileStore.load();
	
	profileDocker[profileType] = {
		store: profileStore,
		grid: profileGrid
	}
}

/**
 * gridPanel中每一行后的操作方法映射调用
 * @param {String} operationName 方法名
 */
function triggerRowOperation(operationName) {
	//获得选中行
	var selected = profileDocker[selectProfile.type].grid.getSelectionModel().getSelected();
	var data = selected.data;
	selectProfile.module[operationName](data);
}

/**
 * 打开添加主模板的功能
 * @param {String} title 弹窗标题
 * @param {String} moduleType 模板类型
 * @param {String} options 附加属性
 */
function showAddMainProfile(title, moduleType, options) {
	// 找到最小可用编号, 从1开始
	var store = profileDocker[selectProfile.type].store;
	var idKey = selectProfile.module.primaryKey.profileId;
	var keyRange = selectProfile.module.getKeyRange();
	var minAvailableId = findMinAvailableId(store, idKey, keyRange[0], keyRange[1]);
	
	window.parent.createDialog(moduleType, title, options && options.width || 600, options && options.height || 370, 
			"gpon/profile/showMainProfileCfg.tv?action=add&moduleType=" + moduleType + '&entityId=' + entityId + '&profileId=' + minAvailableId, 
			null, true, true, function() {
		store.reload();
		// 如果是切换主模板页面的操作，进行相关切换处理
		if(top.intermediateObject.gponprofile.profileName) {
			segmentButton && segmentButton.setValue(top.intermediateObject.gponprofile.profileName);
			top.intermediateObject.gponprofile.profileName = undefined;
		}
	});
}

/**
 * 打开编辑主模板的功能
 * @param {String} title 弹窗标题
 * @param {String} moduleType 模板类型
 * @param {String} profileId 模板id
 * @param {String} options 附加属性
 */
function showEditMainProfile(title, moduleType, profileId, options) {
	var store = profileDocker[selectProfile.type].store;
	
	var url = 'gpon/profile/showMainProfileCfg.tv';
	
	window.parent.createDialog(moduleType, title, options && options.width || 600, options && options.height || 370, 
			url + "?action=edit&moduleType=" + moduleType + '&entityId=' + entityId + '&profileId=' + profileId, 
			null, true, true, function() {
		store.reload();
		// 如果是切换主模板页面的操作，进行相关切换处理
		if(top.intermediateObject.gponprofile.profileName) {
			segmentButton && segmentButton.setValue(top.intermediateObject.gponprofile.profileName);
			top.intermediateObject.gponprofile.profileName = undefined;
		}
	});
}

/**
 * 展示删除主模板提示
 * @param {Object} data 待删除模板对象
 * @param {String} delTip 删除提示
 */
function showDeleteMainProfile(data, delTip) {
	window.parent.showConfirmDlg("@COMMON.tip@", delTip, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg('@COMMON.wait@', '@GPON.deleteing@', 'ext-mb-waiting');
		
		$.ajax({
			url: selectProfile.module.route['delete'],
	    	type: 'POST',
	    	data: {
	    		entityId: entityId,
	    		profileId: data[selectProfile.module.getProfileIdIndex()]
	    	},
	    	dataType: "json",
	   		success: function(json) {
	   			top.closeWaitingDlg();
	   			profileDocker[selectProfile.type].store.reload();
	   		}, error: function(json) {
	   			top.closeWaitingDlg();
	   			profileDocker[selectProfile.type].store.reload();
	   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.deleteProfileFailed@');
			}, 
			cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	});
}

/**
 * 刷新主模板
 */
function refreshMainProfile() {
	refreshProfile(selectProfile.module, {
		entityId: entityId
	}, profileDocker[selectProfile.type].store);
}