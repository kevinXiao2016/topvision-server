var grid;
//存储数据
var perfTargetList = new Array();
var data = [];
var store;
var currentRecord;
var tbar;
//标记当前页面是否进行过编辑
var changedFlag = false;

/***************************
 *  添加模板页面的功能逻辑 ****
 ***************************/

//添加之前的验证函数
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
	var isExisted = checkNameExistSync($('#perfTemplateName').val());
	if(isExisted){
		result = false;
		$('#templateDesc').hide();
		$('#existed').show();
		$('#perfTemplateName').focus();
	}
	return result;
}

function addTemplate(){
	//进行验证
	if(!validate()){return false};
	//获取模板名称
	var perfTemplateName = $('#perfTemplateName').val();
	//获取模板类型
	var templateType = $('#templateType').val();
	//获取是否指定为默认模板
	var saveAsDefaultTemplate = $("#defaultTempCbx").is(":checked");
	//获取子类型
	var subTemplateType = $('#select_'+templateType).val();
	//将指标列表封装成字符串，传递给后台
	var array = new Array();
	for(var i = 0; i<perfTargetList.length; i++){
		//封装指标名称
		var str = perfTargetList[i].toString();
		array[array.length] = str;
	}
	//进行添加操作
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


/*************************
 ***  页面初始化后操作  ****
 *************************/
$(document).ready(function() {
	//创建顶部toolbar
	new Ext.Toolbar({
		renderTo : "toolbar",
		items : [ 
		    {xtype: 'tbspacer', width:5},
            {text : '@Tip.back@', iconCls : 'bmenu_back', handler : function(){ parent.closeFrame();} }, '-',
            {text : '@BUTTON.save@', iconCls : 'bmenu_data', handler : addTemplate} 
		]
	});

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
			}else if(type.isSubType && !type.hasDefaultTemplate && 
					(EntityType.isCcmtsWithAgentType(type.typeId) || EntityType.isCmtsType(type.typeId)) ){
				$('#select_'+cmcAndCmtsType).append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
			}
		});
	}else{
		//生成一级下拉菜单及二级下拉菜单
		$.each(allEntityTypes, function(index, type){
			if(!type.isSubType){
				$('#templateType').append('<option value="'+type.typeId +'">'+ type.displayName +'</option>');
			}else if(type.isSubType && !type.hasDefaultTemplate){
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
	//为模板类型下拉框和复选框绑定事件
	add_modify_template_eventsBind();
	$("#templateType").trigger("change");
	//创建Grid
	create_editable_perfTarget_grid();
    
    $(window).resize(function(){
    	setGridSize();
    });
});

