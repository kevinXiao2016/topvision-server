var potsProfileModule = {
	name: 'potsProfile',
	actionAttributeName: 'topGponSrvPotsInfo',
	primaryKey: {
		profileId: 'topGponSrvPotsInfoProfIdx',
		subProfileId: 'topGponSrvPotsInfoPotsIdx'
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
			profileId: 'topGponSrvPotsInfoProfIdx',
			subProfileId: 'topGponSrvPotsInfoPotsIdx'
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
		type: 'text',
		dataIndex: 'topGponSrvPotsInfoPotsIdx',
		text: '@GPON.pots@@GPON.Serial@',
		maxlength: 31,
		required: true,
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'select',
		dataIndex: 'topGponSrvPotsInfoSIPAgtId',
		text: '@GPON.sipAgentProfile@',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return '@GPON.unbound@';
			} else {
				return v;
			}
		},
		options: [],
		url: '/gpon/profile/loadSipAgentProfileList.tv',
		displayAttr: 'topSIPAgtProfName',
		valueAttr: 'topSIPAgtProfIdx',
		addOn: [{
			type: 'button',
			iconCls: 'bmenu_positon',
			text: '@GPON.sipAgentProfile@@GPON.mgt@',
			handler: 'showSipAngentProfile'
		}]
	}, {
		type: 'select',
		dataIndex: 'topGponSrvPotsInfoVoipMediaId',
		text: '@GPON.voipMediaProfile@',
		required: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true,
		renderer: function(v) {
			if(v === 0) {
				return '@GPON.unbound@';
			} else {
				return v;
			}
		},
		options: [],
		url: '/gpon/profile/loadTopVoipMediaProfList.tv',
		displayAttr: 'topVoipMediaProfName',
		valueAttr: 'topVoipMediaProfIdx',
		addOn: [{
			type: 'button',
			iconCls: 'bmenu_positon',
			text: '@GPON.voipMediaProfile@@GPON.mgt@',
			handler: 'showVoipMediaProfileModule'
		}]
	}, {
		type: 'text',
		dataIndex: 'topGponSrvPotsInfoIpIdx',
		text: '@GPON.ipIndx@',
		required: true,
		list: true,
		tooltip: '1-64',
		columnWidth: 80,
		add: true,
		edit: true,
		validate: function(value) {
			if(/^[1-9]\d{0,2}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return intValue <= 64 && intValue >= 1;
			} else {
				return false;
			}
		}
	}],
	route: {
		loadList: '/gpon/profile/loadTopGponSrvPotsInfoList.tv',
		//add: '/gpon/profile/addServiceProfileEth.tv',
		edit: '/gpon/profile/modifyTopGponSrvPotsInfo.tv',
		//'delete': '/gpon/profile/deleteServiceProfileEth.tv',
		refresh: '/gpon/profile/refreshTopGponSrvPotsInfo.tv'
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
		handler: 'refreshPotsProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditPotsProfile',
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
	showEditPotsProfile: function(data) {
		showEditDependProfile('potsProfile', data);
	},
	refreshPotsProfile: function() {
		refreshDepentProfile('potsProfile');
	},
	showSipAngentProfile: function(){
		gotoMainProfile('sipAgentProfile');
	},
	showVoipMediaProfileModule: function(){
		gotoMainProfile('voipMediaProfile');
	}
}