var vlanProfileModule = {
	name: 'vlanProfile',
	actionAttributeName: 'gponSrvProfilePortVlanCfg',
	primaryKey: {
		profileId: 'gponSrvProfilePortVlanProfileIndex',
		portTypeIndex: 'gponSrvProfilePortVlanPortTypeIndex',
		portIndex: 'gponSrvProfilePortVlanPortIdIndex'
	},
	getKeyRange: function() {
		return [-1, -1];
	},
	getProfileIdIndex: function() {
		return this.primaryKey.portIndex;
	},
	dependProfile: {
		name: 'serviceProfile',
		primaryKeyMap: {
			profileId: 'gponSrvProfilePortVlanProfileIndex',
			portTypeIndex: 'gponSrvProfilePortVlanPortTypeIndex',
			portIndex: 'gponSrvProfilePortVlanPortIdIndex'
		}
	},
	depentItems: [{
		dataIndex: 'gponSrvProfileId',
		text: '@GPON.serviceProfile@@GPON.Serial@'
	}, {
		dataIndex: 'gponSrvProfileName',
		text: '@GPON.serviceProfile@@GPON.name@'
	}],
	items: [{
		dataIndex: 'gponSrvProfilePortVlanProfileIndex'
	}, {
		type: 'select',
		dataIndex: 'gponSrvProfilePortVlanPortTypeIndex',
		text: '@GPON.portType@',
		disabled: true,
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		options: [{
			value: 0,
			name: 'eth'
		}, {
			value: 4,
			name: 'veip'
		}
		/*, {
			value: 1,
			name: 'wlan'
		}, {
			value: 2,
			name: 'catv'
		}*/],
		renderer: function(v) {
			switch(v) {
			case 0:
				return 'ETH';
			case 1:
				return 'WLAN';
			case 2:
				return 'CATV';
			case 3:
				return 'POTS';
			case 4:
				return 'VEIP';
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfilePortVlanPortIdIndex',
		text: '@GPON.portNo@',
		disabled: true,
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'unconcern';
			} else if(v === 0) {
				return 'unlimited';
			} else {
				return v;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfilePortVlanPvid',
		text: 'PVID',
		list: true,
		required: true,
		columnWidth: 80,
		add: true,
		edit: true,
		maxlength: 4,
		tooltip: '0-4094, 0:unconcern',
		renderer: function(v) {
			if(v === 0) {
				return 'unconcern';
			} else {
				return v;
			}
		},
		validate: function(value,p) {
			if(value === '0') {
				return true;
			}
			if(/^[1-9]\d{0,3}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 4094;
			} else {
				return false;
			}
		}
	}, {
		type: 'select',
		dataIndex: 'gponSrvProfilePortVlanPvidPri',
		text: '@GPON.priority@',
		list: true,
		required: true,
		columnWidth: 80,
		add: true,
		edit: true,
		tooltip: 'asdasdas',
		validate: function(value,p) {
			if(p['gponSrvProfilePortVlanCfg.gponSrvProfilePortVlanPvid'] == 0){
				if (value != 0) {
					var validateInfo = new Object();
					validateInfo.result = false;
					return validateInfo;
				}
			}
			return true;
		},
		options: [{
			value: 0,
			name: 'unconcern'
		}, {
			value: 1,
			name: '1'
		}, {
			value: 2,
			name: '2'
		}, {
			value: 3,
			name: '3'
		}, {
			value: 4,
			name: '4'
		}, {
			value: 5,
			name: '5'
		}, {
			value: 6,
			name: '6'
		}, {
			value: 7,
			name: '7'
		}]
	}, {
		type: 'select',
		dataIndex: 'gponSrvProfilePortVlanMode',
		text: '@GPON.mode@',
		required: true,
		options: [{
			value: 0,
			name: 'transparent'
		}, {
			value: 1,
			name: 'tag'
		}, {
			value: 2,
			name: 'translate'
		}, {
			value: 3,
			name: 'aggregation'
		}, {
			value: 4,
			name: 'trunk'
		}],
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			switch(v) {
			case 0:
				return 'transparent';
			case 1:
				return 'tag';
			case 2:
				return 'translate';
			case 3:
				return 'aggregation';
			case 4:
				return 'trunk';
			}
		}
	}],
	route: {
		loadList: '/gpon/profile/loadServiceProfileVlanList.tv',
		loadOne: '/gpon/profile/loadServiceProfileVlan.tv',
		add: '/gpon/profile/addServiceProfileVlan.tv',
		showEdit: '/gpon/profile/showModifyServiceProfileVlan.tv',
		edit: '/gpon/profile/modifyServiceProfileVlan.tv',
		editMode: '/gpon/profile/modifyServiceProfileVlanMode.tv',
		'delete': '/gpon/profile/deleteServiceProfileVlan.tv',
		refresh: '/gpon/profile/refreshServiceProfileVlan.tv'
	},
	getFields: function() {
		var fields = [];
		for(var i=0, len=this.items.length; i<len; i++) {
			fields.push(this.items[i].dataIndex);
		}
		return fields;
	},
	bbarOperations: [{
		text: '@GPON.refreshFromDevice@',
		iconCls: 'miniIcoEquipment',
		handler: 'refreshVlanProfile'
	}],
	operationColumnConfig: {
		width: 220,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditVlanProfile',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: '@GPON.translateRuleCfg@',
			handler: 'showTranslateCfg',
			condition: function(data) {
				return data.gponSrvProfilePortVlanMode === 2;
			}
		}, {
			text: '@GPON.aggregationRuleCfg@',
			handler: 'showAggregationCfg',
			condition: function(data) {
				return data.gponSrvProfilePortVlanMode === 3;
			}
		}, {
			text: '@GPON.trunkRuleCfg@',
			handler: 'showTrunkCfg',
			condition: function(data) {
				return data.gponSrvProfilePortVlanMode === 4;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showEditVlanProfile: function(data) {
		var url = this.route.showEdit;
		url += '?entityId=' + entityId;
		url += String.format('&{0}={1}', 'moduleType', this.name);

		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			url += String.format('&{0}={1}', key, data[keys[key]]);
		}
		showEditDependProfile('vlanProfile', data);
	},
	refreshVlanProfile: function() {
		refreshDepentProfile('vlanProfile');
	},
	showTranslateCfg: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		index.parentEditable = parentEditable;
		showDependProfile('@GPON.translateRuleCfg@', 'vlanTranslate', index);
	},
	showAggregationCfg: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		index.parentEditable = parentEditable;
		showDependProfile('@GPON.aggregationRuleCfg@', 'vlanAggregation', index);
	},
	showTrunkCfg: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		index.parentEditable = parentEditable;
		showDependProfile('@GPON.trunkRuleCfg@', 'vlanTrunk', index);
	}
}