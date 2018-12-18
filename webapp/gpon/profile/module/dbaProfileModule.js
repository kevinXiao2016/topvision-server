var dbaProfileModule = {
	name: 'dbaProfile',
	forceOneColumn: true,
	actionAttributeName: 'gponDbaProfileInfo',
	primaryKey: {
		profileId: 'gponDbaProfileId'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [11, 256];
	},
	items: [{
		type: 'text',
		dataIndex: 'gponDbaProfileId',
		text: '@GPON.dbaProfileNo@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 100,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'gponDbaProfileName',
		text: '@GPON.dbaProfileName@',
		maxlength: 31,
		required: true,
		tooltip: '@GPON.nameTip@',
		list: true,
		flex: 1,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'select',
		dataIndex: 'gponDbaProfileType',
		text: '@GPON.profileType@',
		required: true,
		options: [{
			value: 0,
			name: 'Type0'
		}, {
			value: 1,
			name: 'Type1'
		}, {
			value: 2,
			name: 'Type2'
		}, {
			value: 3,
			name: 'Type3'
		}, {
			value: 4,
			name: 'Type4'
		}, {
			value: 5,
			name: 'Type5'
		}],
		list: true,
		renderer: function(v) {
			return 'Type' + v;
		},
		columnWidth: 80,
		add: true,
		edit: true,
		onchange: 'gponDbaProfileTypeOnChange'
	}, {
		type: 'text',
		dataIndex: 'gponDbaProfileFixRate',
		text: '@GPON.fixedBandWidth@',
		maxlength: 8,
		tooltip: '512-10240000, @GPON.512asUnit@',
		list: true,
		required: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'kbit/s'
		}],
		renderer: function(v, p, record) {
			if(record.data.gponDbaProfileType === 1
			 || record.data.gponDbaProfileType === 5) {
				return v;
			}
			return NO_VALUE;
		},
		validate: function(value) {
			if(/^[1-9]\d{2,7}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 512 <= intValue && intValue <= 10240000 && intValue%512 === 0;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponDbaProfileAssureRate',
		text: '@GPON.guaranteedBandwidth@',
		required: true,
		maxlength: 8,
		tooltip: '512-10240000, @GPON.512asUnit@',
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'kbit/s'
		}],
		renderer: function(v, p, record) {
			if(record.data.gponDbaProfileType === 2
			 || record.data.gponDbaProfileType === 3
			 || record.data.gponDbaProfileType === 5) {
				return v;
			}
			return NO_VALUE;
		},
		validate: function(value) {
			if(/^[1-9]\d{2,7}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 512 <= intValue && intValue <= 10240000 && intValue%512 === 0;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponDbaProfileMaxRate',
		text: '@GPON.maxBandwidth@',
		required: true,
		maxlength: 8,
		tooltip: '512-10240000, @GPON.512asUnit@',
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		addOn: [{
			type: 'displayfield',
			text: 'kbit/s'
		}],
		renderer: function(v, p, record) {
			if(record.data.gponDbaProfileType === 3
			 || record.data.gponDbaProfileType === 4
			 || record.data.gponDbaProfileType === 5) {
				return v;
			}
			return NO_VALUE;
		},
		validate: function(value, data) {
			if(/^[1-9]\d{2,7}$/.test(value)) {
				var intValue = parseInt(value, 10);
				if(intValue < 512 || intValue > 10240000 || intValue%512 !== 0) {
					return false;
				}
				var profileType = parseInt(data['gponDbaProfileInfo.gponDbaProfileType'], 10);
				if(profileType === 3) {
					//在type3类型的DBA模板中，最大带宽必须大于或等于保证带宽
					var fixedBandWidth = parseInt(data['gponDbaProfileInfo.gponDbaProfileFixRate'], 10);
					if(intValue < fixedBandWidth) {
						return {
							result: false,
							msg: '@GPON.dbaType3Rule@'
						};
					}
				} else if(profileType === 5) {
					//在type5类型的DBA模板中，最大带宽必须大于或等于固定带宽与保证带宽之和
					var fixedBandWidth = parseInt(data['gponDbaProfileInfo.gponDbaProfileFixRate'], 10);
					var guaranteedBandwidth = parseInt(data['gponDbaProfileInfo.gponDbaProfileAssureRate'], 10);
					if(intValue < fixedBandWidth + guaranteedBandwidth) {
						return {
							result: false,
							msg: '@GPON.dbaType5Rule@'
						};
					}
				}
				return true;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'gponDbaProfileBindNum',
		maxlength: 31,
		required: true,
		text: '@GPON.bindTcontNum@',
		list: true,
		columnWidth: 80
	}],
	route: {
		loadList: '/gpon/profile/loadDbaProfileList.tv',
		add: '/gpon/profile/addDbaProfile.tv',
		loadOne: '/gpon/profile/loadDbaProfile.tv',
		edit: '/gpon/profile/modifyDbaProfile.tv',
		'delete': '/gpon/profile/deleteDbaProfile.tv',
		refresh: '/gpon/profile/refreshDbaProfile.tv'
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
		handler: 'showAddDbaProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshDbaProfile'
	}],
	operationColumnConfig: {
		width: 150,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditDbaProfile',
			condition: function(data) {
				if(1 <= data.gponDbaProfileId && data.gponDbaProfileId <= 10) {
					return false;
				} else if(data.gponDbaProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteDbaProfile',
			condition: function(data) {
				if(1 <= data.gponDbaProfileId && data.gponDbaProfileId <= 10) {
					return false;
				} else if(data.gponDbaProfileBindNum > 0) {
					return false;
				}
				return true;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddDbaProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.dbaProfile@', 'dbaProfile', {
			height: 430
		});
	},
	showEditDbaProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.dbaProfile@', 'dbaProfile', data.gponDbaProfileId, {
			height: 430
		});
	},
	showDeleteDbaProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteDbaProfile@');
	},
	refreshDbaProfile: function() {
		refreshMainProfile();
	},
	showItem: function(dataIndex) {
		$('#' + dataIndex + '-tr').show();
		$('#' + dataIndex + '-label').show();
		$('#' + dataIndex).show();
	},
	hideItem: function(dataIndex) {
		$('#' + dataIndex + '-tr').hide();
		$('#' + dataIndex + '-label').hide();
		$('#' + dataIndex).hide();
	},
	gponDbaProfileTypeOnChange: function() {
		// 获取模版类型当前所选值
		var profileType = parseInt($('#gponDbaProfileType').val(), 10);
		switch(profileType) {
		case 0:
			if(action == Action.EDIT && moduleData.gponDbaProfileType !== 0) {
				window.top.showMessageDlg('@COMMON.tip@', '@GPON.cannotSwitchType0@');
				$('#gponDbaProfileType').val(moduleData.gponDbaProfileType)
				break;
			}
			this.hideItem('gponDbaProfileFixRate');
			this.hideItem('gponDbaProfileAssureRate');
			this.hideItem('gponDbaProfileMaxRate');
			break;
		case 1:
			this.showItem('gponDbaProfileFixRate');
			this.hideItem('gponDbaProfileAssureRate');
			this.hideItem('gponDbaProfileMaxRate');
			break;
		case 2:
			this.hideItem('gponDbaProfileFixRate');
			this.showItem('gponDbaProfileAssureRate');
			this.hideItem('gponDbaProfileMaxRate');
			break;
		case 3:
			this.hideItem('gponDbaProfileFixRate');
			this.showItem('gponDbaProfileAssureRate');
			this.showItem('gponDbaProfileMaxRate');
			break;
		case 4:
			this.hideItem('gponDbaProfileFixRate');
			this.hideItem('gponDbaProfileAssureRate');
			this.showItem('gponDbaProfileMaxRate');
			break;
		case 5:
			this.showItem('gponDbaProfileFixRate');
			this.showItem('gponDbaProfileAssureRate');
			this.showItem('gponDbaProfileMaxRate');
			break;
		}
	}
}