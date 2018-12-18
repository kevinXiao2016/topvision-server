var gemMapProfileModule = {
	name: 'gemMapProfile',
	actionAttributeName: 'gponLineProfileGemMap',
	primaryKey: {
		profileId: 'gponLineProfileGemMapProfileIndex',
		subProfileId: 'gponLineProfileGemMapGemIndex',
		thirdProfileId: 'gponLineProfileGemMapIndex'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.thirdProfileId;
	},
	getKeyRange: function() {
		return [1, 8];
	},
	dependProfile: {
		name: 'gemProfile',
		primaryKeyMap: {
			profileId: 'gponLineProfileGemMapProfileIndex',
			subProfileId: 'gponLineProfileGemMapGemIndex'
		}
	},
	depentItems: [{
		dataIndex: 'gponLineProfileGemProfileIndex',
		text: '@GPON.lineProfile@@GPON.Serial@'
	}, {
		dataIndex: 'gponLineProfileName',
		text: '@GPON.lineProfile@@GPON.name@'
	}, {
		dataIndex: 'gponLineProfileGemIndex',
		text: 'GEM@GPON.Serial@'
	}],
	items: [{
		dataIndex: 'gponLineProfileGemMapProfileIndex'
	}, {
		dataIndex: 'gponLineProfileGemMapGemIndex',
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileGemMapIndex',
		text: 'GEM@GPON.mapNo@',
		required: true,
		disabled: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 70
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileGemMapVlan',
		text: 'GEM@GPON.mapVlan@',
		tooltip: '1-4094',
		required: true,
		maxlength: 4,
		list: true,
		add: true,
		edit: true,
		columnWidth: 70,
		condition: function(data) {
			if(!lineProfile) {
				return true;
			}
			return lineProfile.gponLineProfileMappingMode === 1 || lineProfile.gponLineProfileMappingMode === 3;
		},
		renderer: function(v, p, record) {
			if(!lineProfile) {
				return v;
			}
			if(lineProfile.gponLineProfileMappingMode === 1 || lineProfile.gponLineProfileMappingMode === 3) {
				return v;
			}
			return NO_VALUE;
		},
		validate: function(value) {
			if(/^[1-9]\d{0,3}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 4094;
			} else {
				return false;
			}
		}
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileGemMapPriority',
		text: '@GPON.priority@',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 70,
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
		renderer: function(v, p, record) {
			if(!lineProfile) {
				return v;
			}
			if(lineProfile.gponLineProfileMappingMode === 2 || lineProfile.gponLineProfileMappingMode === 3) {
				return v;
			}
			return NO_VALUE;
		},
		condition: function(data) {
			if(!lineProfile) {
				return true;
			}
			return lineProfile.gponLineProfileMappingMode === 2 || lineProfile.gponLineProfileMappingMode === 3;
		}
	}, {
		type: 'select',
		dataIndex: 'gponLineProfileGemMapPortType',
		text: '@GPON.portType@',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 70,
		options: [{
			value: 1,
			name: 'eth'
		}, {
			value: 2,
			name: 'iphost'
		}],
		renderer: function(v, p, record) {
			if(lineProfile && lineProfile.gponLineProfileMappingMode !== 4) {
				return NO_VALUE;
			}
			switch(v) {
			case 1:
				return 'eth';
			case 2:
				return 'iphost';
			}
			return v;
		},
		condition: function(data) {
			if(!lineProfile) {
				return true;
			}
			return lineProfile.gponLineProfileMappingMode === 4;
		},
		onchange: 'gponLineProfileGemMapPortTypeOnChange'
	}, {
		type: 'text',
		dataIndex: 'gponLineProfileGemMapPortId',
		text: '@GPON.portNo@',
		tooltip: '1-24',
		required: true,
		maxlength: 2,
		list: true,
		add: true,
		edit: true,
		columnWidth: 70,
		renderer: function(v, p, record) {
			if(!lineProfile) {
				return v;
			}
			if(lineProfile.gponLineProfileMappingMode === 4) {
				return v;
			}
			return NO_VALUE;
		},
		condition: function(data) {
			if(!lineProfile) {
				return true;
			}
			return lineProfile.gponLineProfileMappingMode === 4;
		},
		validate: function(value) {
			var portType = parseInt($('#gponLineProfileGemMapPortType').val(), 10);
			var maxValue = 24;
			if(portType === 2) {
				maxValue = 64;
			}
			if(/^[1-9]\d{0,1}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= maxValue;
			} else {
				return false;
			}
		}
	}],
	route: {
		loadList: '/gpon/profile/loadLineProfileGemMapList.tv',
		add: '/gpon/profile/addLineProfileGemMap.tv',
		edit: '/gpon/profile/modifyLineProfileGemMap.tv',
		'delete': '/gpon/profile/deleteLineProfileGemMap.tv',
		refresh: '/gpon/profile/refreshLineProfileGemMap.tv'
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
		handler: 'showAddGemMapProfile'
	}],
	bbarOperations: [{
		text: '@GPON.refreshFromDevice@',
		iconCls: 'miniIcoEquipment',
		handler: 'refreshGemMapProfile'
	}],
	operationColumnConfig: {
		width: 190,
		items: [{
			text: '@COMMON.delete@',
			handler: 'showDeleteGemMapProfile'
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddGemMapProfile: function() {
		showAddDependProfile('gemMapProfile');
	},
	showEditGemMapProfile: function(data) {
		showEditDependProfile('gemMapProfile', data);
	},
	showDeleteGemMapProfile: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('gemMapProfile', index, '@GPON.confirmDeleteGemmapCfg@');
	},
	refreshGemMapProfile: function() {
		refreshDepentProfile('gemMapProfile');
	},
	gponLineProfileGemMapPortTypeOnChange: function() {
		var portType = parseInt($('#gponLineProfileGemMapPortType').val(), 10);
		switch(portType) {
		case 1:
			$('#gponLineProfileGemMapPortId').attr('tooltip', '1-24');
			break;
		case 2:
			$('#gponLineProfileGemMapPortId').attr('tooltip', '1-64');
			break;
		}
	}
}