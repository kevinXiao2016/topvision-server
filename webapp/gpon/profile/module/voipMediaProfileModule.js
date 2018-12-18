/**
 * 模版的配置文件，由几大部分组成
 * name： 模版的唯一性标识
 * items：模版的属性(dataIndex对应java的domain中的属性)
 * route：模版与后台交互的所有路由集合
 * tbarOperations： 该模版以列表形式展示时，出现在toolbar上的操作项
 * operationColumnConfig： 该模版以列表形式展示时，出现在操作列中的操作项
 * 剩余为该模版对应方法
 */
var voipMediaProfileModule = {
	name: 'voipMediaProfile',
	actionAttributeName: 'topVoipMediaProfInfo',
	primaryKey: {
		profileId: 'topVoipMediaProfIdx'
	},
	getProfileIdIndex: function() {
		return this.primaryKey.profileId;
	},
	getKeyRange: function() {
		return [1, 64];
	},
	items: [{
		type: 'text',
		dataIndex: 'topVoipMediaProfIdx',
		text: '@GPON.voipMediaProfile@@GPON.Serial@',
		required: true,
		disabled: true,
		list: true,
		columnWidth: 80,
		add: true,
		edit: true
	}, {
		type: 'text',
		dataIndex: 'topVoipMediaProfName',
		text: '@GPON.voipMediaProfile@@GPON.name@',
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
		type: 'select',
		dataIndex: 'topVoipMediaFaxmode',
		text: '@onuvoip.faxMode@',
		tooltip: '@GPON.ipTip@',
		maxlength: 63,
		required: true,
		columnWidth: 100,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 0,
			name: 'Passthru'
		},{
			value: 1,
			name: 'T.38'
		}],
		renderer: function(v) {
			switch(v) {
				case 0:
					return 'Passthru';
					break;
				case 1:
					return 'T.38';
					break;
			}
		}
	}, {
		type: 'select',
		dataIndex: 'topVoipMediaNegotiate',
		text: '@onuvoip.negotiationMethod@',
		tooltip: '0-65535',
		maxlength: 5,
		required: true,
		columnWidth: 100,
		list: true,
		add: true,
		edit: true,
		options: [{
			value: 0,
			name: 'Negotiate'
		},{
			value: 1,
			name: 'self-switch'
		}],
		renderer: function(v){
			switch(v){
			case 0:
				return 'Negotiate';
				break;
			case 1:
				return 'self-switch';
				break;
			}
		}
	},{
		type: 'text',
		dataIndex: 'topVoipMediaBindCnt',
		text: '@GPON.topSIPAgtBindCnt@',
		list: true,
		columnWidth: 100
	}],
	route: {
		loadList: '/gpon/profile/loadTopVoipMediaProfList.tv',
		add: '/gpon/profile/addTopVoipMediaProf.tv',
		loadOne: '/gpon/profile/loadTopVoipMediaProf.tv',
		edit: '/gpon/profile/modifyTopVoipMediaProf.tv',
		'delete': '/gpon/profile/deleteTopVoipMediaProf.tv',
		refresh: '/gpon/profile/refreshTopVoipMediaProf.tv'
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
		handler: 'showAddVoipMediaProfile'
	}, {
		text: '@GPON.refreshFromDevice@',
		iconCls: 'bmenu_equipment',
		handler: 'refreshVoipMediaProfile'
	}],
	operationColumnConfig: {
		width: 220,
		items: [{
			text: '@COMMON.edit@',
			handler: 'showEditVoipMediaProfile',
			condition: function(data) {
				return data.topVoipMediaBindCnt === 0;
			}
		}, {
			text: '@COMMON.delete@',
			handler: 'showDeleteVoipMediaProfile',
			condition: function(data) {
				return data.topVoipMediaBindCnt === 0;
			}
		}]
	},
	getCustomLayout: function(action) {
		return undefined;
	},
	showAddVoipMediaProfile: function() {
		showAddMainProfile('@COMMON.add@@GPON.voipMediaProfile@', 'voipMediaProfile', {
			width: 680
		});
	},
	showEditVoipMediaProfile: function(data) {
		showEditMainProfile('@COMMON.edit@@GPON.voipMediaProfile@', 'voipMediaProfile', data.topVoipMediaProfIdx, {
			width: 680
		});
	},
	showDeleteVoipMediaProfile: function(data) {
		showDeleteMainProfile(data, '@onuvoip.confirmDelete@');
	},
	refreshVoipMediaProfile: function() {
		refreshMainProfile();
	}
}