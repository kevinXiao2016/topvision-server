var ethProfileModule = {
	name: 'ethProfile',
	actionAttributeName: 'gponSrvProfileEthPortConfig',
	primaryKey: {
		profileId: 'gponSrvProfileEthPortProfileIndex',
		subProfileId: 'gponSrvProfileEthPortIdIndex'
	},
	getKeyRange: function() {
		return [-1, -1];
	},
	getProfileIdIndex: function() {
		return this.primaryKey.subProfileId;
	},
	dependProfile: {
		name: 'serviceProfile',
		primaryKeyMap: {
			profileId: 'gponSrvProfileEthPortProfileIndex'
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
		dataIndex: 'gponSrvProfileEthPortProfileIndex'
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileEthPortIdIndex',
		text: '@GPON.etcPort@@GPON.Serial@',
		maxlength: 31,
		required: true,
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileEthPortMacLimited',
		text: '@GPON.macLimit@',
		maxlength: 3,
		tooltip: '0-255 (-1:unconcern / 0:unlimited)',
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
		},
		validate: function(value) {
			if(value === '0' || value === '-1') {
				return true;
			}
			if(/^[1-9]\d{0,2}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 255;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileEthPortMtu',
		text: 'MTU',
		maxlength: 4,
		required: true,
		tooltip: '1518-2000 (0:unconcern)',
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return 'unconcern';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '0') {
				return true;
			}
			if(/^[1-9]\d{0,3}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1518 <= intValue && intValue <= 2000;
			} else {
				return false;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'gponSrvProfileEthPortFlowCtrl',
		text: '@GPON.trafficControl@',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		options: [{
			value: 0,
			name: 'unconcern'
		}, {
			value: 1,
			name: 'enable'
		}, {
			value: 2,
			name: 'disable'
		}],
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
		dataIndex: 'gponSrvProfileEthPortInTrafficProfileId',
		text: '@GPON.inDirection@@GPON.trafficProfile@',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return 'unconcern';
			} else {
				return v;
			}
		},
		options: [{
			value: 0,
			name: 'unconcern'
		}],
		url: '/gpon/profile/loadTrafficProfileList.tv',
		displayAttr: 'gponTrafficProfileName',
		valueAttr: 'gponTrafficProfileId'
	}, {
		type: 'select',
		dataIndex: 'gponSrvProfileEthPortOutTrafficProfileId',
		text: '@GPON.outDirection@@GPON.trafficProfile@',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return 'unconcern';
			} else {
				return v;
			}
		},
		options: [{
			value: 0,
			name: 'unconcern'
		}],
		url: '/gpon/profile/loadTrafficProfileList.tv',
		displayAttr: 'gponTrafficProfileName',
		valueAttr: 'gponTrafficProfileId',
		addOn: [{
			type: 'button',
			iconCls: 'bmenu_positon',
			text: '@GPON.trafficProfile@@GPON.mgt@',
			handler: 'showTrafficProfile'
		}]
	}],
	route: {
		loadList: '/gpon/profile/loadServiceProfileEthList.tv',
		add: '/gpon/profile/addServiceProfileEth.tv',
		edit: '/gpon/profile/modifyServiceProfileEth.tv',
		'delete': '/gpon/profile/deleteServiceProfileEth.tv',
		refresh: '/gpon/profile/refreshServiceProfileEth.tv'
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
		handler: 'refreshEthProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditEthProfile',
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
	showEditEthProfile: function(data) {
		showEditDependProfile('ethProfile', data);
	},
	refreshEthProfile: function() {
		refreshDepentProfile('ethProfile');
	},
	showTrafficProfile: function() {
		gotoMainProfile('trafficProfile');
	}
}