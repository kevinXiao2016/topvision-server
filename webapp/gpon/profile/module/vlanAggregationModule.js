var vlanAggregationModule = {
	name: 'vlanAggregation',
	actionAttributeName: 'gponSrvProfilePortVlanAggregation',
	primaryKey: {
		profileId: 'gponSrvProfilePortVlanAggrProfileIndex',
		portTypeIndex: 'gponSrvProfilePortVlanAggrPortTypeIndex',
		portIndex: 'gponSrvProfilePortVlanAggrPortIdIndex',
		vlanIndex: 'gponSrvProfilePortVlanAggrVlanIndex'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.vlanIndex;
	},
	getKeyRange: function() {
		return undefined;
	},
	dependProfile: {
		name: 'vlanProfile',
		primaryKeyMap: {
			profileId: 'gponSrvProfilePortVlanAggrProfileIndex',
			portTypeIndex: 'gponSrvProfilePortVlanAggrPortTypeIndex',
			portIndex: 'gponSrvProfilePortVlanAggrPortIdIndex'
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
		dataIndex: 'gponSrvProfilePortVlanAggrProfileIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanAggrPortTypeIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanAggrPortIdIndex'
	}, {
		type: 'text',
		dataIndex: 'aggrVlanString',
		text: '@GPON.vlanBeforeAgg@',
		maxlength: 40,
		tooltip: '@GPON.vlanAggRule@',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 80,
		validate: function(value, data) {
			var reg = /^([1-9]\d{0,3}[,]{0,1})+$/;
			if (reg.exec(value) && value.lastIndexOf(',') !== value.length - 1) {
				var vlanIds = value.split(',');
				if(vlanIds.length > 8) {
					return false;
				}
				
				for(var i=0,len=vlanIds.length; i<len; i++) {
					vlanIds[i] = parseInt(vlanIds[i], 10);
					if(vlanIds[i] > 4094) {
						return false;
					}
				}
				// 判断vlan是否已存在
				var curVlanIds = [];
				profileStore.each(function(record) {
					// 如果是edit，需要不比较自己
					if(operationAction === 'edit' && 
						record.data.gponSrvProfilePortVlanAggrVlanIndex == parseInt(data['gponSrvProfilePortVlanAggregation.gponSrvProfilePortVlanAggrVlanIndex'], 10)) {
						return;
					}
					var vlans = record.data.aggrVlanString.split(',');
					for(var i=0,len=vlans.length; i<len; i++) {
						curVlanIds.push(parseInt(vlans[i], 10));
					}
				});
				
				var existedVlans = [];
				for(var i=0,len=vlanIds.length; i<len; i++) {
					if($.inArray(vlanIds[i], curVlanIds) !== -1) {
						existedVlans.push(vlanIds[i]);
					}
				}
				if(existedVlans.length) {
					return {
						result: false,
						msg: 'vlan: ' + existedVlans.join(',') +'@GPON.hasAggRule@'
					};
				}
				return true;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponSrvProfilePortVlanAggrVlanIndex',
		text: '@GPON.vlanAfterAgg@',
		maxlength: 4,
		tooltip: '1-4094',
		required: true,
		list: true,
		add: true,
		edit: true,
		columnWidth: 80,
		editDisabled: true,
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
		loadList: '/gpon/profile/loadServiceProfileVlanAggregationList.tv',
		add: '/gpon/profile/addServiceProfileVlanAggregation.tv',
		edit: '/gpon/profile/modifyServiceProfileVlanAggregation.tv',
		'delete': '/gpon/profile/deleteServiceProfileVlanAggregation.tv',
		refresh: '/gpon/profile/refreshServiceProfileVlanAggregation.tv'
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
		handler: 'addVlanAggr',
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
			text: '@COMMON.edit@',
			handler: 'editVlanAggr',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'deleteVlanAggr',
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
	addVlanAggr: function() {
		showAddDependProfile('vlanAggregation');
	},
	editVlanAggr: function(data) {
		showEditDependProfile('vlanAggregation', data);
	},
	refreshVlanProfile: function() {
		refreshDepentProfile('vlanProfile');
	},
	deleteVlanAggr: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('vlanAggregation', index, '@GPON.confirmDeleteAggRule@');
	}
}