var serviceProfileModule = {
	name: 'serviceProfile',
	actionAttributeName: 'gponSrvProfileInfo',
	primaryKey: {
		profileId: 'gponSrvProfileId'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [11, 1024];
	},
	items: [{
		type: 'text',
		dataIndex: 'gponSrvProfileId',
		text: '@GPON.serviceProfile@@GPON.Serial@',
		disabled: true,
		required: true,
		list: true,
		columnWidth: 70,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileName',
		text: '@GPON.serviceProfile@@GPON.name@',
		maxlength: 31,
		required: true,
		tooltip: '@GPON.nameTip@',
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'gponSrvProfileMacLearning',
		text: '@GPON.macLearn@',
		list: true,
		required: true,
		columnWidth: 70,
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
		type: 'text',
		dataIndex: 'gponSrvProfileMacAgeSeconds',
		text: '@GPON.agingTime@(s)',
		maxlength: 7,
		tooltip: '10-1000000 (0:unconcern / -1:unlimited)',
		list: true,
		required: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return 'unconcern';
			} else if(v === -1) {
				return 'unlimited';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '0' || value === '-1') {
				return true;
			}
			if(/^[1-9]\d{0,6}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 10 <= intValue && intValue <= 1000000;
			} else {
				return false;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'gponSrvProfileLoopbackDetectCheck',
		text: '@GPON.loopbackDetect@',
		list: true,
		required: true,
		columnWidth: 70,
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
		dataIndex: 'gponSrvProfileMcMode',
		text: '@GPON.multicastMode@',
		list: true,
		required: true,
		columnWidth: 70,
		edit: true,
		options: [{
			value: 0,
			name: 'unconcern'
		}, {
			value: 1,
			name: 'igmp snooping'
		}, {
			value: 2,
			name: 'olt-control'
		}],
		renderer: function(v) {
			switch(v) {
			case 0:
				return 'unconcern';
			case 1:
				return 'igmp snooping';
			case 2:
				return 'olt-control';
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'gponSrvProfileMcFastLeave',
		text: '@GPON.fastLeave@',
		list: true,
		required: true,
		columnWidth: 70,
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
		type: 'text',
		dataIndex: 'gponSrvProfileEthNum',
		text: '@GPON.etcNumber@',
		maxlength: 2,
		required: true,
		tooltip: '0-24 (-1:adaptive)',
		list: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'adaptive';
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
				return intValue <= 24;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileCatvNum',
		text: '@GPON.catvNumber@',
		maxlength: 2,
		required: true,
		tooltip: '0-2 (-1:adaptive)',
		list: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'adaptive';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '0' || value === '-1') {
				return true;
			}
			if(/^[1-9]$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 2;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileWlanNum',
		text: '@GPON.wlanNumber@',
		maxlength: 2,
		required: true,
		tooltip: '0-2 (-1:adaptive)',
		list: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'adaptive';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '0' || value === '-1') {
				return true;
			}
			if(/^[1-9]$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 2;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileVeipNum',
		text: '@GPON.veipNumber@',
		maxlength: 2,
		required: true,
		tooltip: '0-4 (-1:adaptive)',
		list: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'adaptive';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '-1') {
				return true;
			}
			if(/^[0-9]\d{0,1}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 4;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topGponSrvProfilePotsNum',
		text: '@GPON.potsNum@',
		versionControlId: 'onuVoip',
		maxlength: 2,
		required: true,
		tooltip: '-1 self-adaption,0-2',
		list: true,
		columnWidth: 70,
		edit: true,
		renderer: function(v) {
			if(v === -1) {
				return 'adaptive';
			} else {
				return v;
			}
		},
		validate: function(value) {
			if(value === '-1') {
				return true;
			}
			if(/^[0-9]\d{0,1}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 2;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfileBindNum',
		text: '@GPON.bindOnuNumber@',
		list: true,
		required: true,
		columnWidth: 70,
		edit: true
	}],
	route: {
		loadList: '/gpon/profile/loadServiceProfileList.tv',
		add: '/gpon/profile/addServiceProfile.tv',
		showEdit: 'gpon/profile/showServiceProfileCfg.tv',
		loadOne: '/gpon/profile/loadServiceProfile.tv',
		edit: '/gpon/profile/modifyServiceProfile.tv',
		'delete': '/gpon/profile/deleteServiceProfile.tv',
		refresh: '/gpon/profile/refreshServiceProfile.tv'
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
		handler: 'showAddServiceProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshServiceProfile'
	}],
	operationColumnConfig: {
		width: 340,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditServiceProfile',
			condition: function(data) {
				if(1 <= data.gponSrvProfileId && data.gponSrvProfileId <= 10) {
					return false;
				} else if(data.gponSrvProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}, {
			text: '@GPON.etcCfg@',
			handler: 'showEthConfig'
		}, {
			text: '@GPON.portVlanCfg@',
			handler: 'showVlanConfig'
		}, {
			text: '@GPON.potsconfig@',
			handler: 'showPotsConfig',
			condition: function(data) {
				var supportSIP = top.VersionControl.support('onuVoip', entityId);
				if(supportSIP){
					return true;
				}else{
					return false;
				}
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteServiceProfile',
			condition: function(data) {
				if(1 <= data.gponSrvProfileId && data.gponSrvProfileId <= 10) {
					return false;
				} else if(data.gponSrvProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}]
	},
	getItemMap: function() {
		var items = this.items;
		var map = {};
		var curItem;
		for(var i=0, len = items.length; i<len; i++) {
			curItem = items[i];
			map[curItem.dataIndex] = curItem;
		}
		return map;
	},
	getCustomLayout: function(action) {
		if(action==Action.EDIT) {
			var itemMap = this.getItemMap();
			
			var layout = {
				type: 'group',
				items: [{
					text: '@GPON.baseParam@',
					items: [
					    itemMap['gponSrvProfileId'],
					    itemMap['gponSrvProfileName']
					]
				}, {
					text: '@GPON.baseCfg@',
					items: [
				        itemMap['gponSrvProfileMacLearning'],
					    itemMap['gponSrvProfileMacAgeSeconds'],
					    itemMap['gponSrvProfileLoopbackDetectCheck'],
					    itemMap['gponSrvProfileMcMode'],
					    itemMap['gponSrvProfileMcFastLeave']
				    ]
				}, {
					text: '@GPON.numberCfg@',
					items: [
				        itemMap['gponSrvProfileEthNum'],
					    itemMap['gponSrvProfileCatvNum'],
					    itemMap['gponSrvProfileWlanNum'],
				        itemMap['gponSrvProfileVeipNum']
					    //itemMap['topGponSrvProfilePotsNum']
				    ]
				}]
			};
			var supportSIP = top.VersionControl.support('onuVoip', entityId);
			if(supportSIP) {
				layout.items[2].items.push(itemMap['topGponSrvProfilePotsNum']);
			}
			
			return layout;
		}
		return undefined;
	},
	showAddServiceProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.serviceProfile@', 'serviceProfile');
	},
	showEditServiceProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.serviceProfile@', 'serviceProfile', data.gponSrvProfileId, {
			width: 830,
			height: 670
		});
	},
	showDeleteServiceProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteSrvProfile@');
	},
	refreshServiceProfile: function() {
		refreshMainProfile();
	},
	showEthConfig: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		// 业务模版是否能编辑
		index.parentEditable = data.gponSrvProfileId > 10 && data.gponSrvProfileBindNum === 0;
		showDependProfile('@GPON.etcCfg@', 'ethProfile', index);
	},
	showVlanConfig: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		// 业务模版是否能编辑
		index.parentEditable = data.gponSrvProfileId > 10 && data.gponSrvProfileBindNum === 0;
		showDependProfile('@GPON.portVlanCfg@', 'vlanProfile', index);
	},
	showPotsConfig: function(data){
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]];
		}
		// 业务模版是否能编辑
		index.parentEditable = data.gponSrvProfileId > 10 && data.gponSrvProfileBindNum === 0;
		showDependProfile('@GPON.potsconfig@', 'potsProfile', index);
		
	},
	showSipAgentProfile: function() {
		gotoMainProfile('sipAgentProfile');
	}
}