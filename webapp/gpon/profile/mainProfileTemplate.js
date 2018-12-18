// 本次添加/编辑时，可用的属性
var uesfulColumns = [];
// 需要使用自定义控件的属性
var customComponentItems = [];
var selectModule;

$(function(){
	// 判断模版类型
	selectModule = chooseModule(moduleType);
	
	//生成结构表单
	createForm(selectModule, action, 'item-container');
	
	// 根据不同的打开方式，进行不同的数据获取和填充
	if(action == Action.ADD) {
		//填充模版编号即可
		var data = {};
		data[selectModule.getProfileIdIndex()] = profileId;
		outputData(selectModule, uesfulColumns, data);
	} else if (action == Action.EDIT) {
		// 从数据库加载数据进行填充
		loadSingleData();
	}
});

/**
 * 控件事件方法映射调用
 * @param {String} methodName 方法名称
 * @param {String} dataIndex 
 */
function mapEvent(methodName, dataIndex) {
	selectModule[methodName](dataIndex);
}

/**
 * 模板方法映射调用
 * @param {String} operationName 方法名称
 */
function dependOperation(operationName) {
	selectModule[operationName]();
}

/**
 * 保存当前编辑页面
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
		url: selectModule.route[action],
    	type: 'POST',
    	data: data,
    	dataType:"json",
   		success: function(json) {
   			top.closeWaitingDlg();
   			cancel();
   		}, error: function(json) {
   			top.closeWaitingDlg();
   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.saveProfileFailed@');
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 编辑时，获取当前模板信息
 */
function loadSingleData() {
	$.ajax({
		url: selectModule.route.loadOne,
    	type: 'POST',
    	data: {
    		entityId: entityId,
    		profileId: profileId
    	},
    	dataType: "json",
   		success: function(json) {
   			moduleData = json;
   			outputData(selectModule, uesfulColumns, json);
   		}, error: function(json) {
   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.loadProfileFailed@');
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}