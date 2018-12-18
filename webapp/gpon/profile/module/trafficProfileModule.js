var trafficProfileModule = {
	name: 'trafficProfile',
	forceOneColumn: true,
	actionAttributeName: 'gponTrafficProfileInfo',
	primaryKey: {
		profileId: 'gponTrafficProfileId'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [11, 1024];
	},
	items: [{
		type: 'text',
		dataIndex: 'gponTrafficProfileId',
		text: '@GPON.trafficProfile@@GPON.Serial@',
		disabled: true,
		disabled: true,
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'gponTrafficProfileName',
		text: '@GPON.trafficProfile@@GPON.name@',
		maxlength: 31,
		tooltip: '@GPON.nameTip@',
		list: true,
		required: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'text',
		dataIndex: 'gponTrafficProfileCfgCir',
		text: '@GPON.cfgCir@',
		maxlength: 8,
		tooltip: '64-10240000, @GPON.64asUnit@',
		list: true,
		required: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'kbit/s'
		}],
		validate: function(value) {
			if(/^[1-9]\d{1,7}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 64 <= intValue && intValue <= 10240000 && intValue%64 === 0;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponTrafficProfileCfgPir',
		text: '@GPON.cfgPir@',
		maxlength: 8,
		tooltip: '64-10240000, @GPON.64asUnit@',
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		required: true,
		addOn: [{
			type: 'displayfield',
			text: 'kbit/s'
		}],
		validate: function(value, data) {
			if(/^[1-9]\d{1,7}$/.test(value)) {
				var intValue = parseInt(value, 10);
				if(intValue < 64 || intValue > 10240000 || intValue%64 !== 0) {
					return false;
				}
				
				var cirValue = parseInt(data['gponTrafficProfileInfo.gponTrafficProfileCfgCir'], 10);
				if(cirValue > intValue) {
					return {
						result: false,
						msg: '@GPON.cfgCir@@GPON.cannotLargeThan@@GPON.cfgPir@'
					};
				}
				return true;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponTrafficProfileCfgCbs',
		text: '@GPON.cfgCbs@',
		maxlength: 10,
		tooltip: '2000-1024000000',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'byte'
		}],
		validate: function(value) {
			if(/^[1-9]\d{3,9}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 2000 <= intValue && intValue <= 1024000000;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponTrafficProfileCfgPbs',
		text: '@GPON.cfgPbs@',
		maxlength: 10,
		required: true,
		tooltip: '2000-1024000000',
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'byte'
		}],
		validate: function(value) {
			if(/^[1-9]\d{3,9}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 2000 <= intValue && intValue <= 1024000000;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		required: true,
		dataIndex: 'gponTrafficProfileBindNum',
		text: '@GPON.bindNumber@',
		list: true,
		columnWidth: 80
	}],
	route: {
		loadList: '/gpon/profile/loadTrafficProfileList.tv',
		add: '/gpon/profile/addTrafficProfile.tv',
		loadOne: '/gpon/profile/loadTrafficProfile.tv',
		edit: '/gpon/profile/modifyTrafficProfile.tv',
		'delete': '/gpon/profile/deleteTrafficProfile.tv',
		refresh: '/gpon/profile/refreshTrafficProfile.tv'
	},
	getFields: function() {
		var fields = [];
		for(var i=0, len=this.items.length; i<len; i++) {
			fields.push(this.items[i].dataIndex);
		}
		return fields;
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	tbarOperations: [{
		text: '@COMMON.add@',
		id: 'addButton',
		iconCls: 'bmenu_new',
		handler: 'showAddTrafficProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshTrafficProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditTrafficProfile',
			condition: function(data) {
				if(1 <= data.gponTrafficProfileId && data.gponTrafficProfileId <= 10) {
					return false;
				} else if(data.gponTrafficProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteServiceProfile',
			condition: function(data) {
				if(1 <= data.gponTrafficProfileId && data.gponTrafficProfileId <= 10) {
					return false;
				} else if(data.gponTrafficProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}]
	},
	showAddTrafficProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.trafficProfile@', 'trafficProfile', {
			height: 430
		});
	},
	showEditTrafficProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.trafficProfile@', 'trafficProfile', data.gponTrafficProfileId, {
			height: 430
		});
	},
	showDeleteServiceProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteTrafficProfile@');
	},
	refreshTrafficProfile: function() {
		refreshMainProfile();
	}
}