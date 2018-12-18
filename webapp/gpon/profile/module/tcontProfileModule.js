var tcontProfileModule = {
	name: 'tcontProfile',
	actionAttributeName: 'gponLineProfileTcont',
	primaryKey: {
		profileId: 'gponLineProfileTcontProfileIndex',
		subProfileId: 'gponLineProfileTcontIndex'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.subProfileId;
	},
	getKeyRange: function() {
		return [1, 4];
	},
	dependProfile: {
		name: 'lineProfile',
		primaryKeyMap: {
			profileId: 'gponLineProfileTcontProfileIndex'
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
		dataIndex: 'gponLineProfileTcontProfileIndex',
		primaryKey: true
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileTcontIndex',
		primaryKey: true,
		required: true,
		text: 'T-CONT@GPON.Serial@',
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileTcontDbaProfileId',
		text: '@GPON.dbaProfileNo@',
		list: true,
		required: true,
		columnWidth: 80,
		add: true,
		edit: true,
		options: [],
		url: '/gpon/profile/loadDbaProfileList.tv',
		displayAttr: 'gponDbaProfileName',
		valueAttr: 'gponDbaProfileId',
		addOn: [{
			type: 'button',
			iconCls: 'bmenu_positon',
			text: '@GPON.dbaProfile@@GPON.mgt@',
			handler: 'showDbaProfile'
		}]
	}],
	route: {
		loadList: '/gpon/profile/loadLineProfileTcontList.tv',
		add: '/gpon/profile/addLineProfileTcont.tv',
		edit: '/gpon/profile/modifyLineProfileTcont.tv',
		'delete': '/gpon/profile/deleteLineProfileTcont.tv',
		refresh: '/gpon/profile/refreshLineProfileTcont.tv'
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
		handler: 'showAddTcontProfile',
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
		handler: 'refreshTcontProfile'
	}],
	operationColumnConfig: {
		width: 220,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditTcontProfile',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteTcontProfile',
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
	showAddTcontProfile: function() {
		showAddDependProfile('tcontProfile');
	},
	showEditTcontProfile: function(data) {
		showEditDependProfile('tcontProfile', data);
	},
	showDeleteTcontProfile: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('tcontProfile', index, '@GPON.confirmDeleteTcontCfg@');
	},
	refreshTcontProfile: function() {
		refreshDepentProfile('tcontProfile');
	},
	showDbaProfile: function() {
		gotoMainProfile('dbaProfile');
	}
}