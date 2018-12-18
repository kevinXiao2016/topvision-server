var gemProfileModule = {
	name: 'gemProfile',
	actionAttributeName: 'gponLineProfileGem',
	primaryKey: {
		profileId: 'gponLineProfileGemProfileIndex',
		subProfileId: 'gponLineProfileGemIndex'
	},
	getProfileIdIndex: function() {
		return 'gponLineProfileGemIndex';
	},
	getKeyRange: function() {
		return [1, 64];
	},
	dependProfile: {
		name: 'lineProfile',
		primaryKeyMap: {
			profileId: 'gponLineProfileGemProfileIndex'
		}
	},
	depentItems: [{
		dataIndex: 'gponLineProfileId',
		text: '@GPON.lineProfile@@GPON.Serial@'
	}, {
		dataIndex: 'gponLineProfileName',
		text: '@GPON.lineProfile@@GPON.name@'
	}],
	items: [{
		dataIndex: 'gponLineProfileGemProfileIndex'
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileGemIndex',
		text: 'GEM@GPON.Serial@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 70,
		add: true,
		edit: true
	}, {
		type: 'radioboxgroup',
		dataIndex: 'gponLineProfileGemEncrypt',
		text: 'GEM@GPON.encryptionEnabled@',
		options: [{
			value: 1,
			name: 'enable'
		}, {
			value: 2,
			name: 'disable'
		}],
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			switch(v) {
			case 0:
				return 'unconcern';
			case 1:
				return 'enable';
			case 2:
				return 'disable';
			}
		}
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileGemTcontId',
		text: 'T-CONT',
		options: [],
		required: true,
		list: true,
		columnWidth: 65,
		add: true,
		edit: true,
		url: '/gpon/profile/loadLineProfileTcontList.tv',
		addtionalAttrs: ['profileId'],
		displayAttr: 'gponLineProfileTcontIndex',
		valueAttr: 'gponLineProfileTcontIndex'
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileGemQueuePri',
		text: 'GEM@GPON.priority@',
		options: [{
			value: 0,
			name: '0'
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
		}],
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileGemMapNum',
		text: 'GEM@GPON.mapNumber@',
		options: [],
		required: true,
		list: true,
		columnWidth: 80
	}],
	route: {
		loadList: '/gpon/profile/loadLineProfileGemList.tv',
		loadOne: '/gpon/profile/loadLineProfileGem.tv',
		add: '/gpon/profile/addLineProfileGem.tv',
		edit: '/gpon/profile/modifyLineProfileGem.tv',
		'delete': '/gpon/profile/deleteLineProfileGem.tv',
		refresh: '/gpon/profile/refreshLineProfileGem.tv'
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
		handler: 'showAddGemProfile',
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
		handler: 'refreshGemProfile'
	}],
	operationColumnConfig: {
		width: 190,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditGemProfile',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: 'GEM@GPON.mapCfg@',
			handler: 'showGemMappingProfile',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteGemProfile',
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
	showGemMappingProfile: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		showDependProfile('GEM@GPON.mapCfg@', 'gemMapProfile', index);
	},
	showAddGemProfile: function() {
		showAddDependProfile('gemProfile');
	},
	showEditGemProfile: function(data) {
		showEditDependProfile('gemProfile', data);
	},
	showDeleteGemProfile: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('gemProfile', index, '@GPON.confirmDeleteGemCfg@');
	},
	refreshGemProfile: function() {
		refreshDepentProfile('gemProfile');
	},
	showTrafficProfile: function() {
		showTrafficProfile();
	}
}