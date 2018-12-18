var grid;
//存储数据
var perfTargetList;
var data = [];
var store;
var currentRecord;
var tbar;
var basicTemplate = false;
//标记当前页面是否进行过编辑
var changedFlag = false;
var oltType = EntityType.getOltType();
var cmtsType = EntityType.getCCMTSAndCMTSType();
$(document).ready(function() {
	perfTargetList = new Array();
	//创建顶部toolbar
	new Ext.Toolbar({
		renderTo : "toolbar",
		items : [
			{text : '@Tip.back@', iconCls : 'bmenu_back', handler : back },
			{text : '@Tip.save@', iconCls : 'bmenu_data', handler : save},
			{text : '@tip.saveAsAnother@', iconCls : 'bmenu_data', handler : saveAsAnother}
		]
	});
	//生成一级下拉菜单及二级下拉菜单
	//生成一级下拉菜单及二级下拉菜单
	var onuType = EntityType.getOnuType();
	var oltType = EntityType.getOltType();
	var ccmtsType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	var cmcAndCmtsType = EntityType.getCCMTSAndCMTSType();
	
	//在单独安装CC模块的时候,子类型下拉菜单默认只显示CMTS选择
	if(cmcSupport && !eponSupport){
		$('#select_'+oltType).hide();
		$('#select_'+cmcAndCmtsType).show();
		//生成一级下拉菜单及二级下拉菜单
		$.each(allEntityTypes, function(index, type){
			if(!type.isSubType){
				if(type.typeId == cmcAndCmtsType){
					$('#templateType').append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
				}
			}else if(type.isSubType && (!type.hasDefaultTemplate || type.typeId == perfThresholdTemplate.templateType) && 
					(EntityType.isCcmtsWithAgentType(type.typeId) || EntityType.isCmtsType(type.typeId)) ){
				$('#select_'+cmcAndCmtsType).append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
			}
		});
	}else{
		//生成一级下拉菜单及二级下拉菜单
		$.each(allEntityTypes, function(index, type){
			if(!type.isSubType){
				$('#templateType').append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
			}else if(type.isSubType && (!type.hasDefaultTemplate || type.typeId == perfThresholdTemplate.templateType)){
				//如果该设备类型是子类型并且没有默认模板，则加入相应的二级下拉菜单中
				if(type.parentTypeId == oltType){
					$('#select_'+type.parentTypeId).append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
				}
				if(type.parentTypeId == ccmtsType || type.parentTypeId == cmtsType){
					$('#select_'+cmcAndCmtsType).append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
				}
			}
		});
	}
	
	//为模板名称输入框绑定事件
	add_modify_template_eventsBind();
	create_editable_perfTarget_grid();
    //为页面赋值
    $("#perfTemplateName").val(perfThresholdTemplate.templateName);
    $("#templateType").val(perfThresholdTemplate.parentType);
    //修改时模板类型不允许修改
    $("#templateType").attr("disabled", true);
    if(perfThresholdTemplate.isDefaultTemplate){
    	if(perfThresholdTemplate.templateType == oltType || perfThresholdTemplate.templateType== cmcAndCmtsType || perfThresholdTemplate.templateType == onuType){
    		$("#defaultTempCbx").parents('tr').hide();
    		$("#templateType").attr("disabled", true);
    		basicTemplate = true;
    	}else{
    		$("#defaultTempCbx").attr("checked", true).next().show();
    		$('span#selectWrapper select').hide();
    		$('#select_'+ perfThresholdTemplate.parentType).val(perfThresholdTemplate.templateType).show();
    	}
    }else if(perfThresholdTemplate.templateType == onuType){
    	$("#defaultTempCbx").parents('tr').hide();
    }else{
    	//赋值子模板类型并决定显示那个下拉菜单
        $('span#selectWrapper select').hide();
        if(perfThresholdTemplate.parentType == cmcAndCmtsType){
        	$('#select_'+cmcAndCmtsType).val(perfThresholdTemplate.templateType).show();
        }else{
        	$('#select_'+perfThresholdTemplate.parentType).val(perfThresholdTemplate.templateType).show();
        }
    }
    
    //赋值指标列表
    for (var i = 0, item; item = perfThresholdRules[i++];) {
    	var perf = PerfTargetObject.fromJson(item);
    	perfTargetList[perfTargetList.length] = perf;
    	data[data.length] = [perf.perfTarget, perf.thresholds, perf.trigger, perf.timePeriod,perf.clearRules];
	}
    store.reload();
    var count = targetCountJson[perfThresholdTemplate.parentType];
	if(perfTargetList.length >= count){
		tbar.disable();
	}
    
    $(window).resize(function(){
    	setGridSize();
    });
    setGridSize();
});

function back(){
	if(changedFlag){
		window.parent.parent.showConfirmDlg('@COMMON.tip@',"@tip.needSave@", function(type) {
			if (type == 'yes'){
				save();
			}else{
				parent.closeFrame();
			}
		});
	}else{
		parent.closeFrame();
	}
}

function validate(){
	$('#existed').hide();
	$('#templateDesc').show();
	var result = true;
	//验证模板名称是否合法
	var reg = /^[a-zA-Z\d_\u4E00-\u9FA5]{3,32}$/;
	if(!reg.test($('#perfTemplateName').val())){
		$('#perfTemplateName').focus();
		return false;
	}
	//验证模板名称是否已存在
	var isExisted = false;
	if($('#perfTemplateName').val() != perfThresholdTemplate.templateName){
		isExisted = checkNameExistSync($('#perfTemplateName').val());
	}
	if(isExisted){
		result = false;
		$('#templateDesc').hide();
		$('#existed').show();
		$('#perfTemplateName').focus();
	}
	return result;
}

function isTemplateNameExist(){
	var perfTemplateName = $("#perfTemplateName").val();
	if(perfTemplateName!=perfThresholdTemplate.templateName){
		$.ajax({
			url: '/performance/perfThreshold/checkTemplateName.tv',
			type: 'POST',
			data: {
				templateName:perfTemplateName
			},
			dataType: 'json',
			success: function(result) {
				if(result==true){
					$("#perfTemplateName").css("border","2px solid #DB1111 #D91111 #D91111");
					$('#existed').show().next().hide();
				}else{
					$("#perfTemplateName").css("border","1px solid #CCCCCC #DDDDDD #DDDDDD #CCCCCC");
					$('#existed').hide().next().show();
				}
			}, error: function(result) {
				if(result==true){
					$("#perfTemplateName").css("border","2px solid #DB1111 #D91111 #D91111");
					$('#existed').show().next().hide();
				}else{
					$("#perfTemplateName").css("border","1px solid #CCCCCC #DDDDDD #DDDDDD #CCCCCC");
					$('#existed').hide().next().show();
				}
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		});
	}
}

function save(){
	//进行验证
	if(!validate()){return false};
	//获取模板名称
	var perfTemplateName = $('#perfTemplateName').val();
	//获取模板类型
	var templateType = $('#templateType').val();
	//获取是否指定为默认模板
	var saveAsDefaultTemplate = $("#defaultTempCbx").is(":checked");
	//获取子类型
	var subTemplateType = $('select#select_'+templateType).val();
	//将指标列表封装成字符串，传递给后台
	var array = new Array();
	for(var i = 0; i<perfTargetList.length; i++){
		//封装指标名称
		var str = perfTargetList[i].toString();
		array[array.length] = str;
	}
	//进行修改操作
	$.ajax({
		url: '/performance/perfThreshold/modifyTemplate.tv',
    	type: 'POST',
    	data: {
    		basicTemplate: basicTemplate,
    		templateId:perfThresholdTemplate.templateId,
    		templateName:perfTemplateName,
    		templateType:templateType,
    		saveAsDefaultTemplate:saveAsDefaultTemplate,
    		subTemplateType:subTemplateType,
    		perfTargetList:array.join("%")
    	},
   		success: function() {
			//关闭当前页面，刷新父页面
   			parent.closeFrame();
   		}, error: function() {
   			window.parent.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function saveAsAnother(){
	//获取模板名称
	var perfTemplateName = $('#perfTemplateName').val();
	//获取模板类型
	var templateType = $('#templateType').val();
	//获取是否指定为默认模板
	var saveAsDefaultTemplate = $("#defaultTempCbx").is(":checked");
	//获取子类型
	var subTemplateType = "";
	var oltType = EntityType.getOltType();
	if(templateType==oltType){
		subTemplateType = $('#select_10000').val();
	}else if(templateType == 60000){
		subTemplateType = $('#select_60000').val();
	}
	
	//进行验证
	if(perfTemplateName==perfThresholdTemplate.templateName){
		$("#perfTemplateName").css("border","2px solid #DB1111 #D91111 #D91111");
		$('#existed').show().next().hide();
		$('#existed').effect("shake", { times:2 }, 300).next().hide();
		return;
	}
	if(saveAsDefaultTemplate && subTemplateType==perfThresholdTemplate.templateType){
		$('#normalTypeTip').hide();
		$('#templateOnlyOne').show();
		$('#templateOnlyOne').effect("shake", { times:2 }, 300).next().hide();
		return;
	}else{
		$('#normalTypeTip').show();
		$('#templateOnlyOne').hide();
	}
	
	if(!validate()){return false};
	//将指标列表封装成字符串，传递给后台
	var array = new Array();
	for(var i = 0; i<perfTargetList.length; i++){
		//封装指标名称
		var str = perfTargetList[i].toString();
		array[array.length] = str;
	}
	//进行修改操作
	$.ajax({
		url: '/performance/perfThreshold/addTemplate.tv',
    	type: 'POST',
    	data: {
    		templateName:perfTemplateName,
    		templateType:templateType,
    		saveAsDefaultTemplate:saveAsDefaultTemplate,
    		subTemplateType:subTemplateType,
    		perfTargetList:array.join("%")
    	},
   		success: function() {
			//关闭当前页面，刷新父页面
   			parent.closeFrame();
   		}, error: function() {
   			window.parent.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}
