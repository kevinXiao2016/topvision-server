var vlanTrunkModule = {
	name: 'vlanTrunk',
	actionAttributeName: 'gponSrvProfilePortVlanTrunk',
	primaryKey: {
		profileId: 'gponSrvProfilePortVlanTrunkProfileIndex',
		portTypeIndex: 'gponSrvProfilePortVlanTrunkPortTypeIndex',
		portIndex: 'gponSrvProfilePortVlanTrunkPortIdIndex'
	},
	getKeyRange: function() {
		return function(){
			if(profileStore.getCount() > 0) {
				return -1;
			} else {
				return 1;
			}
		}
	},
	getProfileIdIndex: function() {
		return this.primaryKey.portIndex;
	},
	dependProfile: {
		name: 'vlanProfile',
		primaryKeyMap: {
			profileId: 'gponSrvProfilePortVlanTrunkProfileIndex',
			portTypeIndex: 'gponSrvProfilePortVlanTrunkPortTypeIndex',
			portIndex: 'gponSrvProfilePortVlanTrunkPortIdIndex'
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
		dataIndex: 'gponSrvProfilePortVlanTrunkProfileIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanTrunkPortTypeIndex'
	}, {
		dataIndex: 'gponSrvProfilePortVlanTrunkPortIdIndex'
	}, {
		type: 'text',
		dataIndex: 'trunkVlanString',
		text: 'TRUNK VLAN',
		maxlength: 40,
		required: true,
		tooltip: '@GPON.vlanAggRule@',
		list: true,
		add: true,
		edit: true,
		columnWidth: 80,
		validate: function(value) {
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
					// 如果是edit，不需要比较自己
					if(operationAction === 'edit') {
						return;
					}
					var vlans = record.data.trunkVlanString.split(',');
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
						msg: 'vlan: ' + existedVlans.join(',') +'@GPON.hasTrunkRule@'
					};
				}
				return true;
			} else {
				return false;
			}
		}
	}],
	route: {
		loadList: '/gpon/profile/loadServiceProfileVlanTrunkList.tv',
		add: '/gpon/profile/addServiceProfileVlanTrunk.tv',
		edit: '/gpon/profile/modifyServiceProfileVlanTrunk.tv',
		'delete': '/gpon/profile/deleteServiceProfileVlanTrunk.tv',
		refresh: '/gpon/profile/refreshServiceProfileVlanTrunk.tv'
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
		handler: 'addVlanTrunk',
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
			handler: 'showEditVlanTrunk',
			condition: function(data) {
				if(parentEditable === 'false') {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'deleteVlanTrunk',
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
	addVlanTrunk: function() {
		showAddDependProfile('vlanTrunk');
	},
	showEditVlanTrunk: function(data) {
		showEditDependProfile('vlanTrunk', data);
	},
	refreshVlanProfile: function() {
		refreshDepentProfile('vlanProfile');
	},
	deleteVlanTrunk: function(data) {
		var keys = this.primaryKey;
		var index = {};
		for(var key in keys){
			index[key] = data[keys[key]]
		}
		showDeleteDependProfile('vlanTrunk', index, '@GPON.confirmDeleteTrunkRule@');
	}
}