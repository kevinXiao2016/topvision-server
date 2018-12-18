/**
 * 模版的配置文件，由几大部分组成
 * name： 模版的唯一性标识
 * items：模版的属性(dataIndex对应java的domain中的属性)
 * route：模版与后台交互的所有路由集合
 * tbarOperations： 该模版以列表形式展示时，出现在toolbar上的操作项
 * operationColumnConfig： 该模版以列表形式展示时，出现在操作列中的操作项
 * 剩余为该模版对应方法
 */
var lineProfileModule = {
	name: 'lineProfile',
	actionAttributeName: 'gponLineProfileInfo',
	primaryKey: {
		profileId: 'gponLineProfileId'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [2, 1024];
	},
	items: [{
		type: 'text',
		dataIndex: 'gponLineProfileId',
		text: '@GPON.lineProfile@@GPON.Serial@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileName',
		text: '@GPON.lineProfile@@GPON.name@',
		tooltip: '@GPON.nameTip@',
		maxlength: 31,
		required: true,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'switch',
		dataIndex: 'gponLineProfileUpstreamFECMode',
		text: '@GPON.upFecMode@',
		required: true,
		onvalue: 1,
		offvalue: 2,
		list: true,
		columnWidth: 100,
		add: true,
		edit: true,
		renderer: function(v) {
			return gridRenderSwitch(v === 1);
		}
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileMappingMode',
		text: '@GPON.mapMode@',
		required: true,
		list: true,
		columnWidth: 100,
		add: true,
		edit: true,
		editDisabled: function(data) {
			return data.gemMapNum !== 0;
		},
		options: [{
			value: 1,
			name: 'vlan'
		}, {
			value: 2,
			name: 'priority'
		}, {
			value: 3,
			name: 'vlan+priority'
		}, {
			value: 4,
			name: 'port'
		}],
		renderer: function(v) {
			switch(v) {
			case 1:
				return 'vlan';
			case 2:
				return 'priority';
			case 3:
				return 'vlan+priority';
			case 4:
				return 'port';
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileTcontNum',
		text: '@GPON.tContNumber@',
		list: true,
		columnWidth: 100
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileGemNum',
		text: '@GPON.gemNumber@',
		list: true,
		columnWidth: 100,
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileBindNum',
		text: '@GPON.bindOnuNumber@',
		list: true,
		columnWidth: 100
	}],
	route: {
		loadList: '/gpon/profile/loadLineProfileList.tv',
		add: '/gpon/profile/addLineProfile.tv',
		loadOne: '/gpon/profile/loadLineProfile.tv',
		edit: '/gpon/profile/modifyLineProfile.tv',
		'delete': '/gpon/profile/deleteLineProfile.tv',
		refresh: '/gpon/profile/refreshLineProfile.tv'
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
		handler: 'showAddLineProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshLineProfile'
	}],
	operationColumnConfig: {
		width: 220,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditLineProfile',
			condition: function(data) {
				if(data.gponLineProfileId === 1 || data.gponLineProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}, {
			text: '@GPON.tContCfg@',
			handler: 'showEditTcont'
		}, {
			text: '@GPON.gemCfg@',
			handler: 'showEditGem'
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteLineProfile',
			condition: function(data) {
				if(data.gponLineProfileId === 1 || data.gponLineProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddLineProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.lineProfile@', 'lineProfile');
	},
	showEditLineProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.lineProfile@', 'lineProfile', data.gponLineProfileId);
	},
	showDeleteLineProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteLineProfile@');
	},
	refreshLineProfile: function() {
		refreshMainProfile();
	},
	showEditTcont: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		// 线路模版是否能编辑
		index.parentEditable = data.gponLineProfileId !== 1 && data.gponLineProfileBindNum === 0;
		showDependProfile('@GPON.tContCfg@', 'tcontProfile', index);
	},
	showEditGem: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		// 线路模版是否能编辑
		index.parentEditable = data.gponLineProfileId !== 1 && data.gponLineProfileBindNum === 0;
		showDependProfile('@GPON.gemCfg@', 'gemProfile', index);
	}
}