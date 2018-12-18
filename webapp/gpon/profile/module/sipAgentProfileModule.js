/**
 * 模版的配置文件，由几大部分组成
 * name： 模版的唯一性标识
 * items：模版的属性(dataIndex对应java的domain中的属性)
 * route：模版与后台交互的所有路由集合
 * tbarOperations： 该模版以列表形式展示时，出现在toolbar上的操作项
 * operationColumnConfig： 该模版以列表形式展示时，出现在操作列中的操作项
 * 剩余为该模版对应方法
 */
var sipAgentProfileModule = {
	name: 'sipAgentProfile',
	actionAttributeName: 'topSIPAgentProfInfo',
	primaryKey: {
		profileId: 'topSIPAgtProfIdx'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [1, 64];
	},
	items: [{
		type: 'text',
		dataIndex: 'topSIPAgtProfIdx',
		text: '@GPON.sipAgentProfile@@GPON.Serial@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtProfName',
		text: '@GPON.sipAgentProfile@@GPON.name@',
		tooltip: '@GPON.nameTip@',
		maxlength: 31,
		required: true,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			return /^[\w-]{1,31}$/.test(value)
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtProxyAddr',
		text: '@GPON.topSIPAgtProxyAddr@',
		tooltip: '@GPON.ipTip@',
		maxlength: 63,
		required: true,
		columnWidth: 100,
		list: true,
		add: true,
		edit: true
		/*validate: function(value) {
			return top.IpUtil.isIpAddress(value);
		}*/
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtProxyPort',
		text: '@GPON.topSIPAgtProxyPort@',
		tooltip: '0-65535',
		maxlength: 5,
		required: true,
		columnWidth: 100,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			if(value === '0') {
				return true;
			}
			if(/^[1-9]\d{0,5}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 65535;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtSecProxyAddr',
		text: '@GPON.topSIPAgtSecProxyAddr@',
		tooltip: '@GPON.ipTip@',
		maxlength: 63,
		list: true,
		add: true,
		edit: true,
		columnWidth: 100
		/*validate: function(value) {
			return !value || top.IpUtil.isIpAddress(value);
		}*/
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtSecProxyPort',
		text: '@GPON.topSIPAgtSecProxyPort@',
		tooltip: '0-65535',
		maxlength: 5,
		list: true,
		add: true,
		edit: true,
		columnWidth: 100,
		validate: function(value) {
			if(value === '0' || !value) {
				return true;
			}
			if(/^[1-9]\d{0,5}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 65535;
			} else {
				return false;
			}
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtReqExpTime', 
		text: '@GPON.SIPServerTime@(s)', //SIP注册服务器有效时长
		required: true,
		tooltip: '1-65534',
		maxlength: 5,
		list: true,
		add: true,
		edit: true,
		validate: function(value) {
			if(!value) {
				return true;
			}
			if(/^[1-9]\d{0,5}$/.test(value)) {
				var intValue = parseInt(value, 10);
				return 1 <= intValue && intValue <= 65534;
			} else {
				return false;
			}
			
		}
	}, {
		type: 'text',
		dataIndex: 'topSIPAgtBindCnt',
		text: '@GPON.topSIPAgtBindCnt@',
		list: true,
		columnWidth: 100
	}],
	route: {
		loadList: '/gpon/profile/loadSipAgentProfileList.tv',
		add: '/gpon/profile/addSipAgentProfile.tv',
		loadOne: '/gpon/profile/loadSipAgentProfile.tv',
		edit: '/gpon/profile/modifySipAgentProfile.tv',
		'delete': '/gpon/profile/deleteSipAgentProfile.tv',
		refresh: '/gpon/profile/refreshSipAgentProfile.tv'
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
		handler: 'showAddSipAgentProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshSipAgentProfile'
	}],
	operationColumnConfig: {
		width: 100,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditSipAgentProfile',
			condition: function(data) {
				return data.topSIPAgtBindCnt === 0;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteSipAgentProfile',
			condition: function(data) {
				return data.topSIPAgtBindCnt === 0;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddSipAgentProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.sipAgentProfile@', 'sipAgentProfile', {
			width: 680
		});
	},
	showEditSipAgentProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.sipAgentProfile@', 'sipAgentProfile', data.topSIPAgtProfIdx, {
			width: 680
		});
	},
	showDeleteSipAgentProfile: function(data) {
		showDeleteMainProfile(data, '@GPON.confirmDeleteSipAgentProfile@');
	},
	refreshSipAgentProfile: function() {
		refreshMainProfile();
	}
}