// 光机类型常量
var TopCcmtsSysDorType = {
	CFA: 'CFA',
	CFB: 'CFB',
	CFC: 'CFC',
	CFD: 'CFD',
	EF: 'EF',
	EP06: 'EP06',
	EP09: 'EP09',
	FFA: 'FFA',
	FFB: 'FFB'
};

/**
 * 根据dorType获取对应的光机结构
 * @param {string} dorType 光机的类型，例如'CFA'， 'CFB'
 * @return {Object} 光机结构对象
 */
function loadStructureByDorType(dorType) {
	var structure;
	
	dorType = dorType.toUpperCase();
	
	switch(dorType) {
	case TopCcmtsSysDorType.CFA: 
		structure = cfaStructure;
		break;
	case TopCcmtsSysDorType.CFB: 
		structure = cfbStructure;
		break;
	case TopCcmtsSysDorType.CFC: 
		structure = cfcStructure;
		break;
	case TopCcmtsSysDorType.CFD: 
		structure = cfdStructure;
		break;
	case TopCcmtsSysDorType.EF: 
		structure = efStructure;
		break;
	case TopCcmtsSysDorType.EP06:
	case TopCcmtsSysDorType.EP09:
		structure = epStructure;
		break;
	case TopCcmtsSysDorType.FFA:
	case TopCcmtsSysDorType.FFB:
		structure = ffStructure;
		break;
	}
	return structure;
}

/**
 * 初始化页面tab
 */
function initTabs(){
	//CFA/CFB没有盖板图/原理框图，仅有表单，无需展示tab选项卡
	if(topCcmtsSysDorType != TopCcmtsSysDorType.CFA && topCcmtsSysDorType != TopCcmtsSysDorType.CFB) {
		(new Nm3kTabBtn({
			renderTo: "tab-container",
			callBack: "switchTab",
			tabs: ['@opt.form@', '@opt.principleChart@', '@opt.coverChart@']
		})).init();
	}
}

/**
 * 切换tab
 * @param {Integer} index tab在tab签中的index
 */
function switchTab (index) {
	$(".tabBody").css("display","none");
	$(".tabBody").eq(index).fadeIn();
}

//生成表单结构的模版文件，会在加载页面时预编译
var moduleTemplate = new Ext.XTemplate(
	'<tpl for="structure">',
    '<div class="edge10 pB0 clearBoth" id="basic" style="pading-top: 15px;">',
        '<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">',
            '<thead>',
                '<tr><th colspan="6" align="center" class="txtLeftTh">{name}</th></tr>',
            '</thead>',
            '<tbody>',
        	'<tpl for="items">',
	        	'<tpl if="xindex % 3 === 1">',
	        		'<tr>',
	        	'</tpl>',
        		'<td width="180" class="rightBlueTxt">{fieldLabel}@COMMON.maohao@</td>',
	        	'<td {[xindex % 3 !== 0 ? "width=200": ""]}>',
	        	'<tpl if="type === \'text\'">',
		        	'<input name="{name}" type="{type}" {[values.disabled ? "disabled": ""]} \
	        			class="normalInput {[values.disabled ? "normalInputDisabled": ""]} {[values.mask ? "maskedInput": ""]}"\
	        			{[values.tooltip ? "tooltip=\'"+values.tooltip + "\'" : ""]} \
	        			{[values.mask ? "mask=\'"+values.mask + "\'" : ""]}/>',
	        	'</tpl>',
	        	'<tpl if="type === \'radiogroup\'">',
	        	'<tpl for="items">',
	        		'<span class="column-span">',
		        		'<input type="radio" name="{parent.name}" value="{value}" {[parent.disabled ? "disabled": ""]}/>',
		        		'<label for="">{fieldLabel}</label>',
		        	'</span>',
	        	'</tpl>',
		    	'</tpl>',
	        	'</td>',
	        	'<tpl if="xindex % 3 === 0">',
		    		'</tr>',
		    	'</tpl>',
        	'</tpl>',
        	'<tpl if="items.length % 3 === 1">',
        		'<td colspan="4"></td></tr>',
        	'</tpl>',
        	'<tpl if="items.length % 3 === 2">',
        		'<td colspan="2"></td></tr>',
        	'</tpl>',
            '</tbody>',
        '</table>',
    '</div>',
    '</tpl>'
)
moduleTemplate.compile();

//光机结构
var opticalReceiverStructure;
//提交时需要验证的信息
var validateInfo = {};

/**
 * 加载光机结构数据
 */
function loadStructure() {
	//根据光机类型加载指定的结构
	opticalReceiverStructure = loadStructureByDorType(topCcmtsSysDorType);
	if(!opticalReceiverStructure) {
		return top.showMessageDlg('@COMMON.tip@', '@opt.unknownOpt@');
	}
	//生成光机的表单结构
	generateStructure(opticalReceiverStructure.structure);
	//生成光机的盖板图
	generateCover(opticalReceiverStructure.cover);
	//生成光机的原理框图
	generateSchematic(opticalReceiverStructure.schematic);
	//生成操作
	showOptions(opticalReceiverStructure.operations);
	
	//获取光机信息
	getOpticalReceiverInfo();
}

/**
 * 根据光机结构对象生成对应的展示表单
 * @param structure
 */
function generateStructure(structure) {
	moduleTemplate.overwrite(document.getElementById('structure'), {
		structure: structure
	});
	// 统计哪些提交时需要校验
	$.each(structure, function(i, module) {
		$.each(module.items, function(j, item) {
			if(!item.disabled && item.validate) {
				validateInfo[item.name] = {
					fn: item.validate
				}
			}
		});
	});
}

/**
 * 生成光机盖板图信息
 * @param cover
 */
function generateCover(cover) {
	if(!cover) {
		return;
	}
	//更换背景图片
	var url = '/cmc/optical/cover/' + cover.bgName;
	$("<img/>").attr("src", url).load(function() {
		var realWidth = this.width;
        var realHeight = this.height;
        $('#cover-container')
        	.css('background-image', 'url(' + url +')')
        	.css('width', realWidth)
        	.css('height', realHeight);
	});
}

/**
 * 生成光机原理框图信息
 * @param structure
 */
function generateSchematic(schematic) {
	if(!schematic) {
		return;
	}
	//更换背景图片
	var url = '/cmc/optical/schematic/' + schematic.bgName;
	$("<img/>").attr("src", url).load(function() {
		var realWidth = this.width;
        var realHeight = this.height;
        $('#schematic-container')
        	.css('background-image', 'url(' + url +')')
        	.css('width', realWidth)
        	.css('height', realHeight);
	});
}

/**
 * 根据配置展示对应的操作按钮
 */
function showOptions(operations) {
	$('#operation-div').show();
	$.each(operations, function(i, operation) {
		operation.support ? $('#'+operation.id).show() : $('#'+operation.id).hide();
	});
}

/**
 * 获取该设备的光机信息并展示
 */
function getOpticalReceiverInfo() {
	$.ajax({
		url: '/cmc/optReceiver/loadOpticalReceiverInfo.tv',
		data: {
			cmcId: cmcId
		},
		dataType: 'json',
		success: function(data) {
			outputData(data);
		},
		error: function(data) {
		}
	});
}

/**
 * 将光机信息展示到表单中
 * @param data 光机信息
 */
function outputData(data) {
	$.each(opticalReceiverStructure.structure, function(i, module) {
		$.each(module.items, function(j, item) {
			if(item.type == 'text') {
				$('input[name="' + item.name + '"]').val(data[item.name]);
			} else if(item.type == 'radiogroup') {
				$('input[name="' + item.name + '"][value=' + data[item.name] + ']').attr('checked', true);
			}
		});
	});
}

/**
 * 从设备获取光机数据
 */
function getDataFromDevice() {
	window.top.showWaitingDlg('@COMMON.wait@', '@opt.gettingData@', 'ext-mb-waiting');
	$.ajax({
	  url: '/cmc/optReceiver/refreshDataFromDevice.tv',
	  data: {
		  cmcId: cmcId
	  },
	  dataType: 'json',
	  success: function(data) {
		  top.closeWaitingDlg();
		  if(data.topCcmtsSysDorType) {
			  outputData(data);
			  top.afterSaveOrDelete({
				  title: '@COMMON.tip@',
				  html: '@opt.getOptDataSuccess@'
			  });
		  } else {
			  top.showMessageDlg("@COMMON.tip@", '@opt.getOptDataFail@');
		  }
	  },
	  error: function(data) {
		  top.closeWaitingDlg();
		  top.showMessageDlg("@COMMON.tip@", '@opt.getOptDataFail@');
	  }
	});
}

/**
 * 恢复出厂配置
 */
function reset() {
    window.parent.showConfirmDlg(I18N.COMMON.tip, '@opt.confirmRestore@', function(type) {
        if (type == 'no') {
            return;
        }
		window.top.showWaitingDlg('@COMMON.wait@', '@opt.restoring@', 'ext-mb-waiting');
		$.ajax({
		  url: '/cmc/optReceiver/reset.tv',
		  data: {
			  cmcId: cmcId
		  },
		  dataType: 'json',
		  success: function(data) {
			  top.closeWaitingDlg();
			  top.afterSaveOrDelete({
				  title: '@COMMON.tip@',
				  html: '@opt.restoreSuccess@'
			  });
		  },
		  error: function(data) {
			  top.closeWaitingDlg();
			  top.showMessageDlg("@COMMON.tip@", '@opt.restoreFail@');
		  }
		});
    });
}

/**
 * 展示固件升级上传文件页面
 */
function showUpgrade() {
	top.createDialog("modalDlg", '@opt.firmwareUpdate@', 600, 300, '/cmc/optReceiver/showUpgrade.tv?cmcId='+cmcId, null, true, true, function() {
		
	});
}


/**
 * 保存光机配置
 */
function saveOptData() {
	var formDataArray = $('#data-form').serializeArray();
	
	if(!validate(formDataArray)) {
		return;
	}
	
	//封装对象
	var data = {
		'opticalReceiverData.topCcmtsSysDorType': topCcmtsSysDorType,
		'opticalReceiverData.cmcId': cmcId,
		'opticalReceiverData.cmcIndex': cmcIndex
	};
	$.each(formDataArray, function(i, item) {
		data['opticalReceiverData.'+item.name] = item.value;
	})
	
	window.top.showWaitingDlg('@COMMON.wait@', '@COMMON.saving@', 'ext-mb-waiting');
	$.ajax({
		url: '/cmc/optReceiver/saveConfig.tv',
		data: data,
		cache: false,
		dataType: 'json',
		success: function(data) {
			top.closeWaitingDlg();
			top.afterSaveOrDelete({
	            title: '@COMMON.tip@',
	            html: '@opt.saveConfigSuccess@'
	    	});
		},
		error: function(data) {
			top.closeWaitingDlg();
			top.showMessageDlg("@COMMON.tip@", '@opt.saveConfigFail@');
		}
	});
}

/**
 * 验证提交数据是否符合规则
 * @param formDataArray
 * @returns {Boolean} 是否符合规则
 */
function validate(formDataArray) {
	var result = true;
	$.each(formDataArray, function(i, attr) {
		if(validateInfo[attr.name]) {
			if(!validateInfo[attr.name].fn(attr.value)) {
				$('input[name="' + attr.name + '"]').focus();
				result = false;
				return false;
			}
		}
	})
	return result;
}

/**
 * 页面加载onready事件
 */
$(function() {
	//加载光机结构
	loadStructure();
	// 初始化tab选项
	initTabs();
})