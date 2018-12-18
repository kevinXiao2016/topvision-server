var selectModule;
var editFormInited = false;
var uesfulColumns = [];
var customComponentItems = [];
var profileStore;
var profileGrid;
var operationAction;

$(function() {
	selectModule = chooseModule(moduleType);
	
	// 构建该配置所依赖的模版信息DOM结构
	Ext.each(selectModule.depentItems, function(item) {
		var str = String.format('<span class="label-span">{0}@COMMON.maohao@</span><span id="{1}" class="mR20"></span>', item.text, item.dataIndex)
		$('#description-container').append(str);
	});
	
	// 创建gridPanel
	initGridPanel();
	
	// 创建底部按钮
	initBottomButtons();
	
	// 加载该配置所依赖的模版信息
	loadDependedProfile();
});

/**
 * 创建gridPanel区域
 */
function initGridPanel() {
	// construct column model
	var cm = createColumnModel(selectModule);
	
	// construct store
	profileStore = new Ext.data.JsonStore({
  		url: selectModule.route.loadList,
  		baseParams: {
  			entityId: entityId,
  			profileId: profileId,
  			subProfileId: subProfileId,
    		portTypeIndex: portTypeIndex,
    		portIndex: portIndex
  		},
 	    fields: selectModule.getFields()
  	});
	
	// construct toolbar
	var toolbar = createToolbar(selectModule);
	
	// construct gridpanel
	profileGrid = createGridPanel(profileStore, cm, toolbar, 'grid-container');
	
	profileStore.on('load', function() {
		var addButton = profileGrid.getTopToolbar().getComponent("addButton");
		setAddButtonStateByRange(selectModule, profileStore, addButton);
	});
	profileStore.load();
}

/**
 * 创建底部button区域
 */
function initBottomButtons() {
	var btnTmpl = '<li><a href="javascript:;" class="normalBtnBig" onclick="dependOperation(\'{0}\')"><span><i class="{2}"></i>{1}</span></a></li>';
	Ext.each(selectModule.bbarOperations, function(item) {
		var str = String.format(btnTmpl, item.handler, item.text, item.iconCls)
		$('#bottom-button-container').append(str);
	});
	$('#bottom-button-container').append('<li><a href="javascript:;" class="normalBtnBig" onclick="cancel()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>');
}

/**
 * 获取当前模版的依赖结构，进行依赖数据的获取
 */
function loadDependedProfile() {
	var dependProfileCfg = selectModule.dependProfile;
	if(!dependProfileCfg) {
		return;
	}
	var dependModuleLoadQuque = [];
	var dependProfileModule = chooseModule(dependProfileCfg.name);
	while(dependProfileModule !== undefined) {
		$.ajax({
			url: dependProfileModule.route.loadOne,
	    	type: 'POST',
	    	data: {
	    		entityId: entityId,
	    		profileId: profileId,
	    		subProfileId: subProfileId,
	    		portTypeIndex: portTypeIndex,
	    		portIndex: portIndex
	    	},
	    	async: false,
	    	dataType:"json",
	   		success: function(json) {
	   			window[dependProfileModule.name] = json;
	   			//填充描述信息
	   			Ext.each(selectModule.depentItems, function(item) {
	   				var value = json[item.dataIndex];
	   				Ext.each(dependProfileModule.items, function(parentItem) {
	   					if(parentItem.dataIndex === item.dataIndex && parentItem.renderer) {
	   						value = parentItem.renderer(value);
	   						return false;
	   					}
	   				});
					$('#' + item.dataIndex).text(value);
	   			});
	   		}, error: function(json) {
			}, 
			cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
		
		dependProfileCfg = dependProfileModule.dependProfile;
		
		if(!dependProfileCfg) {
			break;
		}
		dependProfileModule = chooseModule(dependProfileCfg.name);
	}
	
}

/**
 * 模板方法映射调用
 * @param {String} operationName 方法名称
 */
function dependOperation(operationName) {
	selectModule[operationName]();
}

function mapEvent(methodName, dataIndex) {
	selectModule[methodName](dataIndex);
}

/**
 * gridPanel中每一行后的操作方法映射调用
 * @param {String} operationName 方法名
 */
function triggerRowOperation(operationName) {
	//获得选中行
	var selected = profileGrid.getSelectionModel().getSelected();
	var data = selected.data;
	selectModule[operationName](data);
}

/**
 * 展示添加子模板页面
 */
function showAddDependProfile() {
	operationAction = 'add';
	
	createForm(selectModule, operationAction, 'edit-tbody');
	
	$('#inner-title').text('@COMMON.add@');
	$('#inner-edit-container').show();
	$('#inner-form-bg-hover').show();
	
	var data = {};
	var keyRange = selectModule.getKeyRange();
	if(keyRange) {
		data[selectModule.getProfileIdIndex()] = findMinAvailableId(profileStore, selectModule.getProfileIdIndex(), keyRange[0], keyRange[1]);
	}
	outputData(selectModule, uesfulColumns, data);
}

/**
 * 展示编辑子模板页面
 * @param profileType
 * @param {Object} data
 */
function showEditDependProfile(profileType, data) {
	operationAction = 'edit';
	
	createForm(selectModule, operationAction, 'edit-tbody');
	
	$('#inner-title').text('@COMMON.edit@');
	$('#inner-edit-container').show();
	$('#inner-form-bg-hover').show();
	
	outputData(selectModule, uesfulColumns, data);
}

/**
 * 展示删除模版下配置页面
 * @param {String} profileType
 * @param {String} index 数据索引
 * @param {String} delTip 删除提示
 */
function showDeleteDependProfile(profileType, index, delTip) {
	index.entityId = entityId;
	window.parent.showConfirmDlg("@COMMON.tip@", delTip, function(type) {
		if (type == 'no') {
			return;
		}
		window.top.showWaitingDlg('@COMMON.wait@', '@GPON.deleteing@', 'ext-mb-waiting');
		
		$.ajax({
			url: selectModule.route['delete'],
	    	type: 'POST',
	    	data: index,
	    	dataType: "json",
	   		success: function(json) {
	   			top.closeWaitingDlg();
	   			profileStore.reload();
	   		}, error: function(json) {
	   			top.closeWaitingDlg();
	   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.deleteCfgFailed@');
			}, 
			cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	});
}

/**
 * 刷新当前模板
 * @param profileType
 */
function refreshDepentProfile(profileType) {
	refreshProfile(selectModule, {
		entityId: entityId,
		profileId: profileId,
		subProfileId: subProfileId,
		portTypeIndex: portTypeIndex,
		portIndex: portIndex
	}, profileStore);
}

/**
 * 保存
 */
function save() {
	// 封装数据
	var data = packageData(selectModule, uesfulColumns);
	
	// 进行校验
	var passValidate = validate(selectModule, uesfulColumns, data);
	if(!passValidate) {
		return;
	}
	
	window.top.showWaitingDlg('@COMMON.wait@', '@GPON.saving@', 'ext-mb-waiting');
	$.ajax({
		url: selectModule.route[operationAction],
    	type: 'POST',
    	data: data,
    	dataType:"json",
   		success: function(json) {
   			top.closeWaitingDlg();
   			hideInner();
   			profileStore.reload();
   		}, error: function(json) {
   			top.closeWaitingDlg();
   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.saveCfgFailed@');
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 隐藏编辑区域
 */
function hideInner() {
	$('#inner-edit-container').hide();
	$('#inner-form-bg-hover').fadeOut();
}