/**
 * 模版的配置文件，由几大部分组成
 * name： 模版的唯一性标识
 * items：模版的属性(dataIndex对应java的domain中的属性)
 * route：模版与后台交互的所有路由集合
 * tbarOperations： 该模版以列表形式展示时，出现在toolbar上的操作项
 * operationColumnConfig： 该模版以列表形式展示时，出现在操作列中的操作项
 * 剩余为该模版对应方法
 */
var digitalGraphicsProfileModule = {
	name: 'digitalGraphicsProfile',
	forceOneColumn: true,
	actionAttributeName: 'topDigitMapProfInfo',
	primaryKey: {
		profileId: 'topDigitMapProfIdx'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [1, 64];
	},
	items: [{
		type: 'text',
		dataIndex: 'topDigitMapProfIdx',
		text: '@GPON.DigitMapProfIdx@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 60,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'topDigitMapProfName',
		text: '@GPON.DigitMapProfName@',
		tooltip: '@GPON.nameTip@',
		maxlength: 31,
		required: true,
		list: true,
		columnWidth: 60,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'text',
		dataIndex: 'topDigitMapCirtDialTime',
		text: '@GPON.CirtDialTime@(ms)',
		tooltip: '1-65535',
		maxlength: 5,
		required: true,
		columnWidth: 80,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			if(/^[1-9]\d{0,5}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 65535;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topDigitMapPartDialTime',
		text: '@GPON.PartDialTime@(ms)',
		tooltip: '1-65535',
		maxlength: 5,
		required: true,
		columnWidth: 80,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			if(/^[1-9]\d{0,5}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 65535;
			} else {
				return false;
			}
		}
	}, {
		type: 'textarea',
		textareaRows : 4,
		dataIndex: 'topDigitMapDialPlanToken',
		text: '@GPON.DialPlanToken@',
		tooltip: '@GPON.DialPlanTokenTip@',
		maxlength: 1024,
		required: true,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			if(value.length > 1024 || value.length == 0){
				return false;
			}
			var arr = value.split(/[|]+/);
			var returnFalse = false;
			$.each(arr, function(i, v){
				if(v.length <= 0 || v.length > 28){
					returnFalse = true;
					return false;
				}
			})
			if(returnFalse){return false;}
			return true;
		}
	},{
		type: 'text',
		dataIndex: 'topDigitMapBindCnt',
		text: '@GPON.topSIPAgtBindCnt@',
		list: true,
		columnWidth: 50
	}],
	route: {
		loadList: '/gpon/profile/loadTopDigitMapProfList.tv',
		add: '/gpon/profile/addTopDigitMapProf.tv',
		loadOne: '/gpon/profile/loadTopDigitMapProf.tv',
		edit: '/gpon/profile/modifyTopDigitMapProf.tv',
		'delete': '/gpon/profile/deleteTopDigitMapProf.tv',
		refresh: '/gpon/profile/refreshTopDigitMapProf.tv'
	},
	getFields: function() {
		var fields = [];
		for(var i=0, len=this.items.length; i<len; i++) {
			fields.push(this.items[i].dataIndex);
		}
		return fields;
	},
	tbarOperations: [{
		text: '@COMMON.add@',
		id: 'addButton',
		iconCls: 'bmenu_new',
		handler: 'showAddVoipMediaProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshSipAgentProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditVoipMediaProfile',
			condition: function(data) {
				return data.topDigitMapBindCnt === 0;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteSipAgentProfile',
			condition: function(data) {
				return data.topDigitMapBindCnt === 0;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddVoipMediaProfile: function() {
		showAddMainProfile('@COMMON.add@@onuvoip.digitalChartTemplate@', 'digitalGraphicsProfile', {
			width: 680,
			height: 420
		});
	},
	showEditVoipMediaProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@onuvoip.digitalChartTemplate@', 'digitalGraphicsProfile', data.topDigitMapProfIdx, {
			width: 680,
			height: 420
		});
	},
	showDeleteSipAgentProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.deleteDigitMapProf@');
	},
	refreshSipAgentProfile: function() {
		refreshMainProfile();
	}
}