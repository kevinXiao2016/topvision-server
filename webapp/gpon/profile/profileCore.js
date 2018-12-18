var Action = {
	ADD: 'add',
	EDIT: 'edit'
};
var NO_VALUE = '--';

var oneColumnTemplate = new Ext.XTemplate(
	'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
	'<tbody>',
		'<tpl for="items">',
		'<tr id="{dataIndex}-tr" class="{[xindex % 2 === 0 ? "darkZebraTr" : ""]}">',
			'<td class="rightBlueTxt" width="200">{[this.hasRequired(values.required)]}<span id="{dataIndex}-label">{text}@COMMON.maohao@</span></td>',
			'<td width="{[this.getTdWidth(values.addOn)]}" colspan={[this.getTdColSpan(values.addOn)]}>',
			'<tpl if="type === \'text\'">',
				'<input id="{dataIndex}" type="{type}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" \
					style="width:220px;" {[values.disabled ? "disabled" : ""]} maxlength="{[values.maxlength ? values.maxlength : ""]}" \
					{[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]}/>',
			'</tpl>',
			'<tpl if="type === \'textarea\'">',
				'<textarea id="{dataIndex}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" \
				rows="{[values.textareaRows ? values.textareaRows : 3]}" \
				style="width:220px;height:auto;" {[values.disabled ? "disabled" : ""]} maxlength="{[values.maxlength ? values.maxlength : ""]}" \
				{[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]}></textarea>',
		   '</tpl>',
			'<tpl if="type === \'switch\'">',
				'<div id="{dataIndex}_container"></div>',
			'</tpl>',
			'<tpl if="type === \'select\'">',
				'<select id="{dataIndex}" class="normalSel" style="width:220px;" {[values.disabled ? "disabled" : ""]} onchange={[this.changeEvent(values.onchange, values.dataIndex)]}>',
				'<tpl for="options">',
					'<option value="{value}">{name}</option>',
				'</tpl>',
				'</select>',
			'</tpl>',
			'<tpl if="type === \'radioboxgroup\'">',
				'<tpl for="options">',
					'<span class="columnSpan">',
						'<input type="radio" id="{parent.dataIndex}_{value}" name="{parent.dataIndex}" {[xindex===1 ? "checked" : ""]}/>',
						'<label for="{parent.dataIndex}_{value}">{name}</label>',
					'</span>',
				'</tpl>',
			'</tpl>',
			'<tpl if="type === \'segmentButton\'">',
				'<div id="{dataIndex}_container"></div>',
			'</tpl>',
			'<tpl if="values.addOn !== undefined">',
				'<td>',
					'<tpl for="values.addOn">',
						'<tpl if="type === \'button\'">',
						'<li><a href="javascript:dependOperation(\'{handler}\');" class="normalBtn"><span><i class="{iconCls}"></i>{text}</span></a></li>',
						'</tpl>',
						'<tpl if="type === \'displayfield\'">',
						'{text}',
						'</tpl>',
					'</tpl>',
				'</td>',
			'</tpl>',
			'</td>',
		'</tr>',
		'</tpl>',
	'</tbody>',
	'</table>', {
		getTdColSpan: function(addOn) {
			return addOn === undefined ? 2 : 1;
		},
		getTdWidth: function(addOn) {
			return addOn === undefined ? '' : 220;
		},
		hasRequired: function(required) {
			if(required) {
				var tip = '@COMMON.required@';
				return tip;
			}else {
				return '';
			}
		},
		changeEvent: function(eventName, dataIndex) {
			if(eventName) {
				return String.format("mapEvent(\'{0}\',\'{1}\')", eventName, dataIndex);
			}
			return "javascript:;"
		}
	}
);
oneColumnTemplate.compile();

var twoColumnTemplate = new Ext.XTemplate(
	'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
	'<tbody>',
	'<tpl for="items">',
	'<tpl if="this.isDarkTr(xindex)">',
	'<tr class="darkZebraTr">',
	'</tpl>',
	'<tpl if="this.isWhiteTr(xindex)">',
	'<tr>',
	'</tpl>',
		'<td class="rightBlueTxt" width="140">{[this.hasRequired(values.required)]}<span id="{dataIndex}-label">{text}@COMMON.maohao@</span></td>',
		'<td>',
		'<tpl if="type === \'text\'">',
			'<input id="{dataIndex}" type="{type}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" style="width:150px;" \
			{[values.disabled ? "disabled" : ""]} {[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]}/>',
		'</tpl>',
		'<tpl if="type === \'textarea\'">',
			'<textarea id="{dataIndex}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" \
			rows="{[values.textareaRows ? values.textareaRows : 3]}" \
			style="width:220px;height:auto;" {[values.disabled ? "disabled" : ""]} maxlength="{[values.maxlength ? values.maxlength : ""]}" \
			{[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]}></textarea>',
	   '</tpl>',
		'<tpl if="type === \'switch\'">',
			'<div id="{dataIndex}_container"></div>',
		'</tpl>',
		'<tpl if="type === \'select\'">',
			'<select id="{dataIndex}" {[values.disabled ? "disabled" : ""]} class="normalSel w150" onchange={[this.changeEvent(values.onchange, values.dataIndex)]}>',
			'<tpl for="options">',
				'<option value="{value}">{name}</option>',
			'</tpl>',
			'</select>',
		'</tpl>',
		'<tpl if="type === \'radioboxgroup\'">',
			'<tpl for="options">',
				'<span class="columnSpan">',
					'<input type="radio" id="{parent.dataIndex}_{value}" name="{parent.dataIndex}" {[xindex===1 ? "checked" : ""]}/>',
					'<label for="{parent.dataIndex}_{value}">{name}</label>',
				'</span>',
			'</tpl>',
		'</tpl>',
		'<tpl if="type === \'segmentButton\'">',
			'<div id="{dataIndex}_container"></div>',
		'</tpl>',
		'</td>',
		'<tpl if="xindex %2 === 1 && xcount === xindex">',
			'<td colspan="2"></td>',
		'</tpl>',
		
	'<tpl if="this.isTrEnd(xindex)">',
	'</tr>',
	'</tpl>',
	'</tpl>',
	'</tbody>',
	'</table>', {
		isWhiteTr: function(index) {
			return index % 4 === 1;
		},
		isDarkTr: function(index) {
			return index % 4 === 3;
		},
		isTrEnd: function(index) {
			return index % 2 === 0;
		},
		hasRequired: function(required) {
			if(required) {
				var tip = '@COMMON.required@';
				return tip;
			}else {
				return '';
			}
		},
		changeEvent: function(eventName, dataIndex) {
			if(eventName) {
				return String.format("mapEvent(\'{0}\',\'{1}\')", eventName, dataIndex);
			}
			return "javascript:;"
		}
	}
);
twoColumnTemplate.compile();

var groupTwoColumnTemplate = new Ext.XTemplate(
	'<tpl for="items">',
		'<div class="pT10">',
			'<div class="zebraTableCaption pT30">',
				'<div class="zebraTableCaptionTitle"><span>{text}</span></div>',
				'<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">',
					'<tbody>',
						'<tr>',
							'<tpl for="items">',
								'<tpl if="this.isDarkTr(xindex)">',
								'<tr class="darkZebraTr">',
								'</tpl>',
								'<tpl if="this.isWhiteTr(xindex)">',
								'<tr>',
								'</tpl>',
									'<tpl if="type === \'fill\'">',
									'<td></td>',
									'</tpl>',
									'<tpl if="type !== \'fill\'">',
									'<td class="rightBlueTxt" width="120">{[this.hasRequired(values.required)]}{text}@COMMON.maohao@</td>',
									'</tpl>',
									'<td width="{[this.getTdWidth(values, values.addOn)]}" colspan={[this.getTdColSpan(values,values.addOn)]}>',
									'<tpl if="type === \'text\'">',
										'<input id="{dataIndex}" type="{type}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" \
											style="width:{[this.getComponentWidth(values)]}px;" {[values.disabled ? "disabled" : ""]} {[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]} \
											maxlength="{[values.maxlength ? values.maxlength : ""]}"/>',
									'</tpl>',
									'<tpl if="type === \'textarea\'">',
										'<textarea id="{dataIndex}" class="normalInput {[values.disabled ? "normalInputDisabled": ""]}" \
										rows="{[values.textareaRows ? values.textareaRows : 3]}" \
										style="width:220px;height:auto;" {[values.disabled ? "disabled" : ""]} maxlength="{[values.maxlength ? values.maxlength : ""]}" \
										{[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]}></textarea>',
								   '</tpl>',
									'<tpl if="type === \'switch\'">',
										'<div id="{dataIndex}_container"></div>',
									'</tpl>',
									'<tpl if="type === \'select\'">',
										'<select id="{dataIndex}" class="normalSel" style="width:{[this.getComponentWidth(values)]}px;">',
										'<tpl for="options">',
											'<option value="{value}">{name}</option>',
										'</tpl>',
										'</select>',
									'</tpl>',
									'<tpl if="type === \'radioboxgroup\'">',
										'<tpl for="options">',
											'<span class="columnSpan">',
												'<input type="radio" id="{parent.dataIndex}_{value}" name="{parent.dataIndex}" {[xindex===1 ? "checked" : ""]}/>',
												'<label for="{parent.dataIndex}_{value}">{name}</label>',
											'</span>',
										'</tpl>',
									'</tpl>',
									'<tpl if="type === \'segmentButton\'">',
										'<div id="{dataIndex}_container"></div>',
									'</tpl>',
									'<tpl if="type === \'fill\'">',
										'<div></div>',
									'</tpl>',
									'<tpl if="values.addOn !== undefined">',
										'<td>',
											'<tpl for="values.addOn">',
												'<tpl if="type === \'button\'">',
												'<li><a href="javascript:dependOperation(\'{handler}\');" class="normalBtn"><span><i class="{iconCls}"></i>{text}</span></a></li>',
												'</tpl>',
												'<tpl if="type === \'displayfield\'">',
												'{text}',
												'</tpl>',
											'</tpl>',
										'</td>',
									'</tpl>',
									'</td>',
									'<tpl if="xindex %2 === 1 && xcount === xindex">',
										'<td colspan="2"></td>',
									'</tpl>',
								'<tpl if="this.isTrEnd(xindex)">',
								'</tr>',
								'</tpl>',
							'</tpl>',
						'</tr>',
					'</tbody>',
				'</table>',
			'</div>',
		'</div>',
	'</tpl>', {
		getTdColSpan: function(values, addOn) {
			return addOn === undefined ? 2 : 1;
		},
		getTdWidth: function(values, addOn) {
			return values.componentWidth || (addOn === undefined ? '' : 220);
		},
		getComponentWidth: function(values) {
			return values.componentWidth || 200;
		},
		isWhiteTr: function(index) {
			return index % 4 === 1;
		},
		isDarkTr: function(index) {
			return index % 4 === 3;
		},
		hasRequired: function(required) {
			if(required) {
				var tip = '@COMMON.required@';
				return tip;
			}else {
				return '';
			}
		},
		isTrEnd: function(index) {
			return index % 2 === 0;
		}
	}
);

/**
 * 选择对应的模版模型
 * @param {String} profileType 模板类型
 * @returns
 */
function chooseModule(profileType) {
	var selectModule;
	return window[profileType+'Module'];
}

/**
 * 根据模板类型来创造columnModel
 * @param {Object} profileModule 模板模型
 * @returns {Ext.grid.ColumnModel}
 */
function createColumnModel(profileModule) {
	var columns = [];
	
	
	
	Ext.each(profileModule.items, function(item){
		if(item.list) {
			// add by fanzidong, 版本控制
			if(item.versionControlId) {
				var supportSIP = top.VersionControl.support(item.versionControlId, entityId);
				if(!supportSIP) {
					return true;
				}
			}
			
			var column = {
				header: item.text,
				dataIndex: item.dataIndex,
				align: 'center'
			};
			if(item.columnWidth) {
				column.width = item.columnWidth;
			} else {
				column.flex = 1;
			}
			if(item.renderer) {
				column.renderer = item.renderer;
			}
			columns.push(column);
		}
	});
	
	// add operation column
	columns.push({
		header: '@COMMON.manu@',
		dataIndex: profileModule.getProfileIdIndex(),
		align: 'center',
		fixed : true,
		width: profileModule.operationColumnConfig.width,
		renderer: function(value, p, record) {
			var items = profileModule.operationColumnConfig.items;
			var array = [];
			Ext.each(items, function(item){
				if(item.condition && !item.condition(record.data)) {
					return;
				}
				array.push(String.format('<a class="yellowLink" href="javascript:triggerRowOperation(\'{0}\');">{1}</a>', item.handler, item.text));
			});
			return array.join(' / ')
		}
	});
	
	var cmConfig = CustomColumnModel.init(profileModule.name, columns, {});
	
	return cmConfig.cm;
}

/**
 * 创造toolbar
 * @param {Object} profileModule 模板模型
 * @returns {Ext.Toolbar}
 */
function createToolbar(profileModule) {
	var toolbar = [];
	Ext.each(profileModule.tbarOperations, function(operation){
		if(operation.condition && !operation.condition()) {
			return;
		}
		if(!operation.type || operation.type === 'button') {
			var item = {
				text: operation.text,
				iconCls: operation.iconCls,
				handler: profileModule[operation.handler]
			};
			if(operation.id) {
				item.itemId = operation.id;
			}
			toolbar.push(item);
			toolbar.push('-');
		} else if(operation.type === 'tbtext') {
			var item = {
				xtype: 'tbtext',
				text: operation.text
			};
			toolbar.push(item);
		} else if(operation.type === 'textfield') {
			var item = {
				xtype: 'textfield',
				id: operation.id,
				width: operation.width
			};
			toolbar.push(item);
		}
	});
	return new Ext.Toolbar({
    	items: toolbar
 	});
}

/**
 * 创建gridPanel
 * @param {Ext.data.JsonStore} profileStore 数据store
 * @param {Ext.grid.ColumnModel} cm columnModel模型
 * @param {Ext.Toolbar} toolbar toolbar工具栏
 * @param {String} renderTo 渲染div的id
 * @returns {Ext.grid.GridPanel}
 */
function createGridPanel(profileStore, cm, toolbar, renderTo) {
	var profileGrid = new Ext.grid.GridPanel({
   		stripeRows: true, 
   		bodyCssClass: 'normalTable',
   		border: false,
   		store: profileStore,
   		cm: cm,
   		tbar: toolbar,
     	bbar: new Ext.PagingToolbar({
     	    pageSize: 9999,
     	    store: profileStore,
     	    displayInfo: true,
     	    items: []
     	}),
   		viewConfig:{
   			forceFit: true
   		},
	    renderTo: renderTo,
	    width: $('#' + renderTo).width(),
	    height: $('#' + renderTo).height(),
	    loadMask: true
   	});
	return profileGrid;
}

/**
 * 获取当前store中对应id最小可用值
 * @param {Ext.data.JsonStore} store 数据所在的store对象
 * @param {String} key id对应的属性名称
 * @param {Integer} min 可用值最小值
 * @param {Integer} max 可用值最大值
 * @returns
 */
function findMinAvailableId(store, key, min, max) {
	var ids = [];
	store.each(function(record) {
		ids.push(record.data[key]);
	});
	ids.sort(function(a, b) {
		return a-b;
	});
	var minAvailableId = min;
	while($.inArray(minAvailableId, ids) >= 0){
		minAvailableId++;
	}
	if(minAvailableId <= max) {
		return minAvailableId;
	} else {
		return -1;
	}
}

/**
 * 根据范围设置添加按钮的状态
 * @param {Object} profileModule 模板模型
 * @param {Ext.data.JsonStore} profileStore
 * @param {Ext.Component} addButton 添加按钮
 */
function setAddButtonStateByRange(profileModule, profileStore, addButton) {
	var key = profileModule.getProfileIdIndex();
	var range = profileModule.getKeyRange();
	if(range && typeof range !== 'function') {
		var minAvalableId = findMinAvailableId(profileStore, key, range[0], range[1]);
		
		// 如果无可用id，添加按钮不可用
		if(minAvalableId === -1) {
			addButton && addButton.disable();
		} else {
			addButton && addButton.enable();
		}
	} else if(range && typeof range === 'function') {
		var minAvalableId = range();
		
		// 如果无可用id，添加按钮不可用
		if(minAvalableId === -1) {
			addButton && addButton.disable();
		} else {
			addButton && addButton.enable();
		}
	}
}

/**
 * ON/OFF展示列renderer
 * @param {Boolean} state 状态，true/false
 * @returns {String}
 */
function gridRenderSwitch(state) {
	if(state === true) {
		return '<img src="/images/speOn.png" />';
	} else if(state === false) {
		return '<img src="/images/speOff.png" />';
	}
}

/**
 * 需要去后台加载数据的select框
 * @param {Object} dom select控件
 * @param {String} url 异步加载的url
 * @param {String} displayAttr options展示的属性
 * @param {String} valueAttr options值属性
 * @param {Boolean} async 是否异步加载，一般添加时异步加载，编辑时需要赋值，所以同步加载
 */
function asyncLoadSelectOptions(dom, url, displayAttr, valueAttr, async) {
	$.ajax({
		url: url,
    	type: 'POST',
    	async: async,
    	dataType:"json",
   		success: function(array) {
   			Ext.each(array, function(item) {
   				$(dom).append(String.format('<option value="{0}">{1}({0})</option>', item[valueAttr], item[displayAttr]));
   			})
   		}, error: function(json) {
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 展示子模板配置页
 * @param {String} title 弹窗标题
 * @param {String} moduleType 子模板类型
 * @param {Object} index 各index的key-value
 * @param {Object} options 附加属性
 */
function showDependProfile(title, moduleType, index, options) {
	var url = options && options.url || 'gpon/profile/showDependProfileCfg.tv'
	url += '?entityId=' + entityId;
	url += String.format('&{0}={1}', 'moduleType', moduleType);
	for(var key in index) {
		url += String.format('&{0}={1}', key, index[key]);
	}
	window.parent.createDialog(moduleType, title,  options && options.width || 800, options && options.height || 500
			, url, null, true, true, function() {
		// 如果是切换主模板页面的操作，进行相关切换处理
		if(top.intermediateObject.gponprofile.profileName) {
			segmentButton && segmentButton.setValue(top.intermediateObject.gponprofile.profileName);
			top.intermediateObject.gponprofile.profileName = undefined;
		}
	});
}

/**
 * 创建表单区域
 * @param {Object} profileModule 模板模型
 * @param {String} action add/edit
 * @param {String} renderTo 待渲染div的id
 */
function createForm(profileModule, action, renderTo) {
	// 初始化变量
	uesfulColumns = [];
	loadQueue = [];
	customComponentItems = [];
	
	// 筛选出本次表单需要的item
	Ext.each(profileModule.items, function(column) {
		if(column[action]) {
			if(column.condition && !column.condition()) {
				return;
			}
			var copyColumn = $.extend({}, column);
			if(column[action+'Disabled'] && typeof column[action+'Disabled'] !== 'function') {
				copyColumn.disabled = column[action+'Disabled'];
			}
			uesfulColumns.push(copyColumn);
			if(copyColumn.type === 'select' && copyColumn.url) {
				loadQueue.push(copyColumn);
			}
			if(copyColumn.type === 'switch' || copyColumn.type === 'segmentButton') {
				customComponentItems.push(copyColumn);
			}
		}
	});
	
	// 生成合适的结构信息
	var customLayout = selectModule.getCustomLayout && selectModule.getCustomLayout(action);
	if(customLayout) {
		// 自定义结构
		if(customLayout.type === 'group') {
			groupTwoColumnTemplate.overwrite(document.getElementById(renderTo), customLayout);
		}
	} else {
		//增加一个forceOneColumn属性，不管有多少个，强制设置成一列;
		// 生成合适的结构信息
		if(uesfulColumns.length > 4 && !profileModule.forceOneColumn) {
			twoColumnTemplate.overwrite(document.getElementById(renderTo), {
				items: uesfulColumns
			});
		} else {
			oneColumnTemplate.overwrite(document.getElementById(renderTo), {
				items: uesfulColumns
			});
		}
	}
	
	// 为生成的结构补充自定义组件
	Ext.each(customComponentItems, function(item) {
		if(item.type === 'switch') {
			item.component = new Nm3kSwitch(item.dataIndex + '_container', item.offvalue, {
				yesNoValue: [item.onvalue, item.offvalue]
			});
			item.component.init();
		} else if(item.type === 'segmentButton') {
			var array = [];
			item.component = new SegmentButton(item.dataIndex + '_container', item.options);
			item.component.init();
		}
	});
	
	// 加载需要异步获取的数据
	Ext.each(loadQueue, function(item) {
		var url = item.url + '?entityId=' + entityId;
		Ext.each(item.addtionalAttrs, function(attr) {
			url += String.format('&{0}={1}', attr, window[attr])
		})
		asyncLoadSelectOptions($('#' + item.dataIndex), url, item.displayAttr, item.valueAttr, action===Action.ADD ? true : false);
	})
}

/**
 * 输出数据，填充表单
 * @param {Object} profileModule 模板模型
 * @param {Array} items 需要填充的item
 * @param {Object} data 待填充的数据
 */
function outputData(profileModule, items, data) {
	$.each(items, function(i, item) {
		if(item.type == 'text' || item.type == 'select' || item.type == 'textarea') {
			$('#' + item.dataIndex).val(data[item.dataIndex]);
		} else if(item.type == 'radioboxgroup') {
			$('#' + item.dataIndex + '_' + data[item.dataIndex]).attr('checked', true);
		} else if(item.type == 'switch') {
			item.component.setValue(data[item.dataIndex]);
		} else if(item.type == 'segmentButton') {
			item.component.setValue(data[item.dataIndex]);
		}
		if(typeof item[action+'Disabled'] === 'function') {
			$('#' + item.dataIndex).attr('disabled', item[action+'Disabled'](data));
		}
		if(item.onchange) {
			profileModule[item.onchange]();
		}
	});
}

/**
 * 封装待提交的数据
 * @param {Object} profileModule 模板模型对象
 * @param {Array} items 需要封装的属性
 * @returns 
 */
function packageData(profileModule, items) {
	//自动判断需要添加的id
	var data = {};
	data[profileModule.actionAttributeName + '.entityId'] = entityId;
	
	// 如果有依赖的模版，则添加该属性
	if(profileModule.dependProfile) {
		var dependentKeys = profileModule.dependProfile.primaryKeyMap;
		for(var key in dependentKeys) {
			data[profileModule.actionAttributeName + '.' + dependentKeys[key]] = window[key];
		}
	}
	
	//封装数据
	Ext.each(items, function(column) {
		var key = profileModule.actionAttributeName + '.' + column.dataIndex;
		if($('#'+column.dataIndex).css("display") == "none") {
			return
		}
		if(column.type == 'text' || column.type == 'select' || column.type == 'textarea') {
			data[key] = $('#'+column.dataIndex).val();
		} else if(column.type == 'radioboxgroup') {
			var selector = String.format(':radio[name={0}]:checked', column.dataIndex);
			var checkedRadioId = $(selector).attr('id');
			data[key] = checkedRadioId.split('_')[1];
		} else if(column.type == 'switch' || column.type == 'segmentButton') {
			var component = column.component;
			data[key] = component.getValue();
		}
	});
	
	return data;
}

/**
 * 校验数据
 * @param {Object} profileModule 模板模型对象
 * @param {Array} items 需要封装的属性
 */
function validate(profileModule, items, data) {
	var passValidate = true;
	
	// 遍历进行校验，可能需要对多个属性进行关联校验，所以在获取完数据后处理
	Ext.each(items, function(column) {
		var key = profileModule.actionAttributeName + '.' + column.dataIndex;
		// 是否需要校验
		if($('#'+column.dataIndex).length == 0 || $('#'+column.dataIndex).css("display") == "none") {
			return
		}
		if(column.validate) {
			var validateInfo = column.validate(data[key], data);
			if(validateInfo === false) {
				passValidate = false;
				$('#'+column.dataIndex).focus();
				return false;
			}
			// 有错误信息附加
			if(validateInfo.result === false) {
				passValidate = false;
				top.showMessageDlg('@COMMON.tip@', validateInfo.msg || column.text + ' @GPON.illegal@');
				$('#'+column.dataIndex).focus();
				return false;
			}
		}
	});
	
	return passValidate;
}

/**
 * 跳转至指定主模板
 * @param {String} profileType 主模板id
 */
function gotoMainProfile(profileType) {
	if(!top.intermediateObject.gponprofile) {
		top.intermediateObject.gponprofile = {};
	}
	top.intermediateObject.gponprofile.profileName = profileType;
	cancel();
}

/**
 * 刷新指定模版
 * @param {Object} profileModule 模板模型
 * @param {Object} data 数据
 * @param {Ext.data.JsonStore} store 待刷新的store
 */
function refreshProfile(profileModule, data, store) {
	window.top.showWaitingDlg('@COMMON.wait@', '@GPON.refreshing@', 'ext-mb-waiting');
	
	$.ajax({
		url: profileModule.route.refresh,
    	type: 'POST',
    	data: data,
    	dataType: "json",
   		success: function(json) {
   			top.closeWaitingDlg();
   			store.reload();
   		}, error: function(json) {
   			top.closeWaitingDlg();
   			store.reload();
   			window.top.showMessageDlg('@COMMON.tip@', '@GPON.loadDataFailed@');
		}, 
		cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 关闭当前弹窗
 */
function cancel() {
	window.top.closeWindow(moduleType);
}