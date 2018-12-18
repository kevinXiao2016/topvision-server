var vlanTranslateModule = {
	name: 'vlanTranslate',
	actionAttributeName: 'gponSrvProfilePortVlanTranslation',
	primaryKey: {
		profileId: 'gponSrvProfilePortVlanTransProfileIndex',
		portTypeIndex: 'gponSrvProfilePortVlanTransPortTypeIndex',
		portIndex: 'gponSrvProfilePortVlanTransPortIdIndex',
		vlanIndex: 'gponSrvProfilePortVlanTransVlanIndex'
	},
	getKeyRange: function() {
		return undefined;
	},
	getProfileIdIndex: function() {
		return this.primaryKey.vlanIndex;
	},
	dependProfile: {
		name: 'vlanProfile',
		primaryKeyMap: {
			profileId: 'gponSrvProfilePortVlanTransProfileIndex',
			portTypeIndex: 'gponSrvProfilePortVlanTransPortTypeIndex',
			portIndex: 'gponSrvProfilePortVlanTransPortIdIndex'
		}
	},
	depentItems: [{
		dataIndex: 'gponSrvProfileId',
		text: '@GPON.serviceProfile@@GPON.Serial@'
	}, {
		dataIndex: 'gponSrvProfileName',
		text: '@GPON.serviceProfile@@GPON.name@'
	}, {
		dataIndex: 'gponSrvProfilePortVlanPortTypeIndex',
		text: '@GPON.portType@'
	}, {
		dataIndex: 'gponSrvProfilePortVlanPortIdIndex',
		text: '@GPON.portNo@'
	}],
	items: [{
		dataIndex: 'gponSrvProfilePortVlanTransProfileIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanTransPortTypeIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanTransPortIdIndex'
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfilePortVlanTransVlanIndex',
		text: '@GPON.transBeforeVlan@',
		maxlength: 4,
		tooltip: '1-4094',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 80,
		validate: function(value) {
			if(/^[1-9]\d{0,3}$/.test(value)) {
				var intValue = parseInt(value, 10);
				if(intValue > 4094) {
					return false;
				}
				// 判断vlan是否已存在
				var existedVlan = [];
				profileStore.each(function(record) {
					if(record.data.gponSrvProfilePortVlanTransVlanIndex === intValue) {
						existedVlan.push(value);
					}
				});
				if(existedVlan.length) {
					return {
						result: false,
						msg: 'vlan: ' + existedVlan.join(',') +'@GPON.hasTranslateRule@'
					};
				}
				return true;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfilePortVlanTransNewVlan',
		text: '@GPON.transAfterVlan@',
		maxlength: 4,
		tooltip: '1-4094',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 80,
		validate: function(value) {
			if(/^[1-9]\d{0,3}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 4094;
			} else {
				return false;
			}
		}
	}],
	route: {
		loadList: '/gpon/profile/loadServiceProfileVlanTranslateList.tv',
		loadOne: '/gpon/profile/loadServiceProfileVlan.tv',
		add: '/gpon/profile/addServiceProfileVlanTranslate.tv',
		'delete': '/gpon/profile/deleteServiceProfileVlanTranslate.tv',
		refresh: '/gpon/profile/refreshServiceProfileVlanTranslate.tv'
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
		iconCls: 'bmenu_new',
		handler: 'addVlanTranslate',
		condition: function(data) {
			if(parentEditable === 'false') {
				return false;
			}
			return true;
		}
	}],
	bbarOperations: [{
		text: '@GPON.refreshFromDevice@',
		iconCls: 'miniIcoEquipment',
		handler: 'refreshVlanProfile'
	}],
	operationColumnConfig: {
		width: 220,
		items: [{
			text: '@COMMON.delete@',
			handler: 'deleteVlanTranslate',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	addVlanTranslate: function() {
		showAddDependProfile('vlanTranslate');
	},
	refreshVlanProfile: function() {
		refreshDepentProfile('vlanProfile');
	},
	deleteVlanTranslate: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('vlanTranslate', index, '@GPON.confirmDeleteTransRule@');
	}
}