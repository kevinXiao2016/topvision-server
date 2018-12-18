/**
 * 模版的配置文件，由几大部分组成
 * name： 模版的唯一性标识
 * items：模版的属性(dataIndex对应java的domain中的属性)
 * route：模版与后台交互的所有路由集合
 * tbarOperations： 该模版以列表形式展示时，出现在toolbar上的操作项
 * operationColumnConfig： 该模版以列表形式展示时，出现在操作列中的操作项
 * 剩余为该模版对应方法
 */
var sipServiceDataProfileModule = {
	name: 'sipServiceDataProfile',
	actionAttributeName: 'topSIPSrvProfInfo',
	primaryKey: {
		profileId: 'topSIPSrvProfIdx'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [1, 64];
	},
	items: [{
		type: 'text',
		dataIndex: 'topSIPSrvProfIdx',
		text: '@GPON.sipServiceData@@GPON.Serial@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 100,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'topSIPSrvProfName',
		text: '@GPON.sipServiceData@@GPON.name@',
		tooltip: '@GPON.nameTip@',
		maxlength: 31,
		required: true,
		columnWidth: 100,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfCallWait',
		text: '@GPON.callWaiting@',
		tooltip: '@GPON.ipTip@',
		maxlength: 63,
		required: true,
		columnWidth: 60,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProf3Way',
		text: '@GPON.ThreeWayCall@',
		list: true,
		columnWidth: 60,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfCallTransfer',
		text: '@GPON.callForwarding@',
		list: true,
		columnWidth: 60,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfCallHold',
		text: '@GPON.CallHoldPermission@',
		list: true,
		columnWidth: 60,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfDND',
		text: '@GPON.callDoNotDisturb@',
		list: true,
		columnWidth: 70,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfHotline',
		text: '@GPON.HotlineBusinessAuthority@',
		list: true,
		columnWidth: 60,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPSrvProfHotlineNum',
		text: '@GPON.HotlineBusinessNum@',
		list: true,
		columnWidth: 60,
		required: true,
		tooltip: '@GPON.Tip1to32@',
		maxlength: 32,
		required: true,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,32}$/.test(value)
		}
	}, {
		type: 'radioboxgroup',
		dataIndex: 'topSIPSrvProfHotDelay',
		text: '@GPON.DelayedHotlinePermissions@',
		list: true,
		columnWidth: 60,
		required: true,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 2,
			name: 'disable'
		},{
			value: 1,
			name: 'enable'
		}],
		renderer: function(v) {
			switch(v) {
				case 2:
					return 'disable';
					break;
				case 1:
					return 'enable';
					break;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPSrvProfBindCnt',
		text: '@GPON.bindNumber@',
		list: true,
		columnWidth: 50
	}],
	route: {
		loadList: '/gpon/profile/loadTopSIPSrvProfList.tv',
		add: '/gpon/profile/addTopSIPSrvProf.tv',
		loadOne: '/gpon/profile/loadTopSIPSrvProf.tv',
		edit: '/gpon/profile/modifyTopSIPSrvProf.tv',
		'delete': '/gpon/profile/deleteTopSIPSrvProf.tv',
		refresh: '/gpon/profile/refreshTopSIPSrvProf.tv'
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
		handler: 'showAddSipServiceDataProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshSipServiceDataProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditSipServiceDataProfile',
			condition: function(data) {
				return data.topSIPSrvProfBindCnt === 0;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteSipServiceDataProfile',
			condition: function(data) {
				return data.topSIPSrvProfBindCnt === 0;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddSipServiceDataProfile: function() {
		showAddMainProfile('@COMMON.add@@onuvoip.sipServicesDataTemplate@', 'sipServiceDataProfile', {
			width: 680
		});
	},
	showEditSipServiceDataProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@onuvoip.sipServicesDataTemplate@', 'sipServiceDataProfile', data.topSIPSrvProfIdx, {
			width: 680
		});
	},
	showDeleteSipServiceDataProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteSipServiceData@');
	},
	refreshSipServiceDataProfile: function() {
		refreshMainProfile();
	}
}