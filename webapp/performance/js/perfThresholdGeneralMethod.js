//设置按钮的disabled;
function disabledBtn(arr, disabled){
	$.each(arr,function(i, v){
		Ext.getCmp(v).setDisabled(disabled);
	})
};
//刷新当前页面的grid
function refreshGrid(){
	//在执行完相关操作后去掉grid表头上的复选框选中状态
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
	
	store.reload({
		callback : function(){
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
	});
}

/**
 * 返回frame的父页面
 */
function closeFrame(){
	//刷新grid
	store.reload({
		callback : function(){
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
	});
	$("#content").css("display","block");
	$("#frameOuter").css("display","none");
	$("#frameInner").attr("src","");
	set_entity_threshold_GridSize();
}

//检查模板名称是否存在
function checkNameExistSync(templateName){
	var isExisted = false;
	$.ajax({
		url: '/performance/perfThreshold/checkTemplateName.tv?templateName='+templateName,
		async: false,
    	type: 'POST',
    	dataType: 'json',
   		success: function(json) {
   			isExisted = json.result;
   		}, error: function(json) {
   			isExisted = json.result;
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
	return isExisted;
}

//添加/修改阈值模板页面通用事件绑定，包括模板名称输入框、模板类型下拉框、指定为子类型默认模板勾选框、设备子类型下拉框的事件
function add_modify_template_eventsBind(){

	//为模板类型下拉框绑定事件
	$('#templateType').bind('change', function(){
		var value = $(this).val();
		//获取对应的二级下拉菜单及其长度
		var $select = $('#select_'+value);
		//如果该类型下的子类型均有默认模板，则checkbox不可选
		if($('#select_'+value+' option').length==0){
			$('#defaultTempCbx').attr("checked", false).attr("disabled", true).parents('tr').hide();
		}else{
			$('#defaultTempCbx').attr("disabled", false).parents('tr').show();
			$select.parents('td').find('select').hide();
			$select.show();
		}
	});
	
	//为复选框绑定事件
	$('#defaultTempCbx').bind('click', function(){
		var $cbx = $(this);
		if($cbx.is(':checked')){
			$cbx.next("span").show();
		}else{
			$cbx.next("span").hide();
		}
	});
}

//创建可编辑的指标列表grid
function create_editable_perfTarget_grid(){
	//创建grid
	store = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy(data),
		reader: new Ext.data.ArrayReader({},[
		    {name: 'perfTarget'},
		    {name: 'thresholds'},
		    {name: 'trigger'},
		    {name: 'timePeriod'}
		])
	});
	//var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([
	    //sm,
   		{id:"perfTarget",header:"<div style='text-align:center'>@Performance.targetName@</div>",dataIndex:'perfTarget',align:'left',width:100,renderer:nameRender},
   		{id:"thresholds",header:'@Performance.threshold@',dataIndex:'thresholds',align:'center',width:140,renderer:thresholdsRender},
   		{id:"trigger",header:'@tip.trigger@',dataIndex:'trigger',align:'center',width:120,renderer:triggerRender},
   		{id:"timePeriod",header:'@performance/Performance.timeRange@',width:120,dataIndex:'timePeriod',align:'center',renderer:timePeriodRender},
   		{id:"opration",header:'@COMMON.opration@',dataIndex:'ruleName',width:140,fixed : true, resizable: false, renderer:oprationRender}
   	]);
	tbar = new Ext.Toolbar({
		items : [ {text : '@tip.addPerfTarget@', iconCls : 'bmenu_new', handler : addPerfTarget} ,'-']
	});
   	grid = new Ext.grid.GridPanel({
   		renderTo: "perfGrid",
   		store:store,
   		border: true,
   		//sm:sm,
      	cm:cm,
      	tbar: tbar,
      	cls: 'normalTable',
      	title:'@tip.perfTargetList@',
      	viewConfig:{
      		forceFit: true
      	},
    	sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		})
    });
	store.load();
	
	setGridSize();
}

//自适应grid的大小
function setGridSize(){
	var h = $(window).height() - $("#toolbar").height()- $("#content").outerHeight(true) - 10;
	if(h<150) h = 150;
	var w = $("body").width() - 40;
	grid.setSize(w, h);
}

//自适应设备模板页面grid的大小
function set_entity_threshold_GridSize(){
	var h = $(window).height() - 100;
	if(h<100) h = 100;
	var w = $("body").width();
	grid.setSize(w, h);
}

//打开添加指标页面
function addPerfTarget(){
	var parentType = $('#templateType').val();
	window.top.createDialog('addPerfTargetsToTemp', '@tip.addPerfTarget@', 820, 540, '/performance/perfThreshold/addPerfTargetsToTemplate.tv?parentType='+parentType+'&originalFrame='+originalFrame, null, true, true);
}

//打开修改指标页面
function modifyPerf(){
	var selected = grid.getSelectionModel().getSelected();
	var targetId = selected.data.perfTarget.value;
	//获取对应的数据
	for(var i=0; i<data.length; i++){
		if(perfTargetList[i].perfTarget.value == targetId){
			currentRecord = perfTargetList[i];
		}
	}
	var parentType = $('#templateType').val();
	window.top.createDialog('modifyTemplatePerfTargets', '@tip.mdyPerfTarget@', 820, 540, '/performance/perfThreshold/modifyTemplatePerfTargets.tv?parentType='+parentType+'&targetId='+targetId+'&originalFrame='+originalFrame, null, true, true);
}

//为当前模板添加指标
function addRecordToGrid(perfTarget, trigger, timePeriod, thresholds,clearRules){
	//创建对象
	var perf = new PerfTargetObject(perfTarget, trigger, timePeriod, thresholds,clearRules);
	//向perfTargetList中添加一项
	perfTargetList[perfTargetList.length] = perf;
	//将数据转换赋给data
	data[data.length] = [perf.perfTarget, perf.thresholds, perf.trigger, perf.timePeriod,perf.clearRules];
	store.reload();
	//添加指标后，需要设置模板类型下拉框不可选
	$("#templateType").attr("disabled", true);
	//还需要去判断指标是否均添加，如果是，则至添加按钮为不可用
	//获取该模板类型的指标总个数
	var type = $("#templateType").val();
	var count = targetCountJson[type];
	if(perfTargetList.length >= count){
		tbar.disable();
	}
	changedFlag = true;
}

//修改当前模板指定的指标
function modifyCurrentRecord(perfTarget, trigger, timePeriod, thresholds,clearRules){
	//创建对象
	var perf = new PerfTargetObject(perfTarget, trigger, timePeriod, thresholds,clearRules);
	//替换data和perfTargetList中相应的数据
	for(var i=0; i<data.length; i++){
		if(perfTargetList[i].perfTarget.value==perf.perfTarget.value){
			perfTargetList[i] = perf;
		}
		if(data[i][0].value==perf.perfTarget.value){
			data[i] = [perf.perfTarget, perf.thresholds, perf.trigger, perf.timePeriod,perf.clearRules];
		}
	}
	store.reload();
	currentRecord = null;
	changedFlag = true;
}

//删除当前模板指定的指标
function deletePerf(){
	var selected = grid.getSelectionModel().getSelected();
	var perfName = selected.data.perfTarget.value;
	//从data和perfTargetList中删除掉对应的数据
	for(var i=0; i<data.length; i++){
		if(data[i][0].value==perfName){
			data.splice(i, 1);
		}
		if(perfTargetList[i].perfTarget.value==perfName){
			perfTargetList.splice(i, 1);
		}
	}
	store.reload();
	//删除指标后，激活tbar
	tbar.enable();
	changedFlag = true;
}

function operationRender_detail(value, p, record){
	var formatStr = "<a class='detailA' onclick='showTemplateDetail({0})'>@tip.detail@</a>";
	return String.format(formatStr, record.data.templateId);
}

/***************************
 * 指标列表grid的render函数
 * ************************/
function nameRender(value, p, record){
	return value.name;
}

function thresholdsRender(value, p, record){
	var formatStrModule = '<span class="gridAlarmSpan" title="{0}">{1}</span>';
	var detailMsgModule = '@tip.when@{0}{1}{2}@tip.hour@,@tip.tgr@{3}';
	var text = "";
	for (var i = 0, item; item = value[i++];) {
		var detailMsg = String.format(detailMsgModule, actions[item.action], item.value, item.unit , alarms[item.level]);
		var formatStr = String.format(formatStrModule, detailMsg, alarms[item.level]);
		text += formatStr;
	}
	return text;
}

function triggerRender(value, p, record){
	return value.minute + "@tip.tgrRdr1@" + value.count + "@tip.tgrRdr2@";
}

var weekdayEnglish = ["Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
function timePeriodRender(value, p, record){
	var text = "";
	if(!value.used){
		text = "@tip.noTimeConfig@";
		return text;
	}else{
	    //modify by fanzidong,此处国际化需要考虑不同语言的表达
	    if(lang=="zh_CN"){
	        text = "@tip.everyWeek@";
            for (var i = 0, item; item = value.weekdays[i++];) {
                text += days[item];
            }
	    }else if(lang=="en_US"){
	        for (var i = 0, item; item = value.weekdays[i++];) {
	            text += weekdayEnglish[item-1] + ", ";
	        }
	        text = text.substring(0, text.length-2);
	    }
	    text += "(" + formatTime(String(value.startHour), String(value.startMin), String(value.endHour), String(value.endMin)) + ")";
	    return text;
	}
}

function formatTime(startHour, startMin, endHour, endMin){
	var sh=startHour, sm=startMin, eh=endHour, em=endMin;
	if(startHour.length==1) sh = "0"+startHour;
	if(startMin.length==1) sm = "0"+startMin;
	if(endHour.length==1) eh = "0"+endHour;
	if(endMin.length==1) em = "0"+endMin;
	return sh+":"+sm+"-"+eh+":"+em;
}

function oprationRender(value, p, record){
	var editStr = '<a href="javascript:;" onclick="modifyPerf()">@Tip.edit@</a>';
	var deleteStr = '<a href="javascript:;" onclick="deletePerf()">@tip.delete@</a>';
	return editStr + ' / ' + deleteStr;
}

/***************************
 * 设备模板关系grid的render函数
 * ************************/
function renderSwitch(value, p, record){
	if (value) {
		return String.format('<img class="switch" title="@tip.closeThd@" src="/images/performance/on.png" onclick="closeThresholdAlarm({0})" border=0 align=absmiddle>',
				record.data.entityId);	
	} else {
		return String.format('<img class="switch" title="@tip.openThd@" src="/images/performance/off.png" onclick="openThresholdAlarm({0})" border=0 align=absmiddle>',
				record.data.entityId);	
	}
}

function renderTemplate(value, p, record){
	var templateId = record.data.templateId;
	if (value == null || value == "" || templateId == -1) {
		return '@Performance.noRelaTemplateDesc@';	
	}else{
		return '<a href="#" onclick="showTemplateDetail(\''+record.data.templateId+'\');">'+value+'</a>';
	}
}
function renderIp(value, p, record){
	var typeId = record.data.typeId;
	if (EntityType.isCcmtsWithoutAgentType(typeId)) {
		return value + '(OLT)';	
	}else{
		return value;
	}
}
function showTemplateDetail(templateId){
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showModifyTemplate.tv?templateId='+templateId+"&m="+m);
}
function renderOperation_(value, p, record){
	var entityId = record.data.entityId;
	var startStr = String.format("<a href='javascript:;' onclick='openThresholdAlarm({0})'>@Tip.open@</a>", entityId);
	var closeStr = String.format("<a href='javascript:;' onclick='closeThresholdAlarm({0})'>@Tip.off@</a>", entityId);
	var linkStr = String.format("<a href='javascript:;' onclick='linkThresholdTemplate({0})'>@Performance.relaTemplate@</a>", entityId);
	var removeStr = String.format("<a href='javascript:;' onclick='removeThresholdTemplate({0})'>@tip.unBindTemp@</a>", entityId);
	
	return  startStr + " / " + closeStr + " / " + linkStr + " / " +  removeStr;
}
/***************************
 * 设备性能阈值模板的toolbar公用方法
 * ************************/
function batchOpenThresholdAlarm(){
	//获取选择的设备
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var hasWrongEntity = false;
		var selectedEntities = sm.getSelections();
		if(selectedEntities.length == 0){
			window.parent.showMessageDlg('@COMMON.tip@','@Performance.relaTemplateMes@');
			return ;
		}
		for(var i = 0;i<selectedEntities.length;i++){
			//如果该设备未绑定模版，则需要通知用户
			if(selectedEntities[i].data.templateName == null || selectedEntities[i].data.templateName == "" ||
					selectedEntities[i].data.templateId == -1){
				hasWrongEntity = true;
			}else{
				//加入该设备id
				entityIdArray[entityIdArray.length]  = selectedEntities[i].data.entityId;
			}
		}
		//如果所选设备全部未绑定模版，则告知无法操作
		if(hasWrongEntity && entityIdArray.length==0){
			window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemplateTip@');
			return ;
		}
		var tipMes = '@Performance.confirmOpenPerfAlert@';
		if(hasWrongEntity){
			tipMes = '@Performance.noRelaDeviceTip@'
		}
		window.parent.showConfirmDlg('@COMMON.tip@',tipMes, function(type) {
  			if (type == 'no'){return;}
  			window.top.showWaitingDlg('@COMMON.wait@', '@Performance.openDevicePerfAlert@', 'ext-mb-waiting');
  			$.ajax({
 		       url: '/performance/perfThreshold/openDeviceThreshold.tv?entityIds='+ entityIdArray,
 		       type: 'POST',
 		       success: function() {
 		    	   window.top.closeWaitingDlg();
	           	   refreshGrid();
		           	top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@Performance.openPerfAlertSuc@</b>'
	   	   			});
 		        }, error: function() {
 		        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.openPerfAlertFail@')
 		    }, cache: false
  			});
  		})
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}

function batchCloseThresholdAlarm(){
	//获取选择的设备
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var hasWrongEntity = false;
		var selectedEntities = sm.getSelections();
		if(selectedEntities.length == 0){
			window.parent.showMessageDlg('@COMMON.tip@','@Performance.relaTemplateMes@');
			return ;
		}
		for(var i = 0;i<selectedEntities.length;i++){
			//如果该设备未绑定模版，则需要通知用户
			if(selectedEntities[i].data.templateName == null || selectedEntities[i].data.templateName == "" ||
					selectedEntities[i].data.templateId == -1){
				hasWrongEntity = true;
			}else{
				//加入该设备id
				entityIdArray[entityIdArray.length]  = selectedEntities[i].data.entityId;
			}
		}
		//如果所选设备全部未绑定模版，则告知无法操作
		if(hasWrongEntity && entityIdArray.length==0){
			window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemplateTip2@');
			return ;
		}
		var tipMes = '@Performance.confirmClosePerfAlert@';
		if(hasWrongEntity){
			tipMes='@Performance.noRelaDeviceTip2@'
		}
		window.parent.showConfirmDlg('@COMMON.tip@',tipMes, function(type) {
  			if (type == 'no'){return;}
  			$.ajax({
 		       url: '/performance/perfThreshold/closeDeviceThreshold.tv?entityIds='+ entityIdArray,
 		       type: 'POST',
 		       success: function() {
 		    	   window.parent.closeWaitingDlg();
	           	   refreshGrid();
		           	top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@Performance.closePerfAlertSuc@</b>'
	   	   			});
 		        }, error: function() {
 		        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.closePerfAlertFail@')
 		    }, cache: false
  			});
  		})
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}

function unBindTemplate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var selectIds = sm.getSelections();
		for(var i = 0;i<selectIds.length;i++){
			entityIdArray[entityIdArray.length]  = selectIds[i].data.entityId;
		}
		window.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmNoRela@', function(type) {
  			if (type == 'no'){return;}
  			$.ajax({
 		       url: '/performance/perfThreshold/removeEntityRelaTemplate.tv?entityIds='+ entityIdArray,
 		       type: 'POST',
 		       success: function() {
 		    	  refreshGrid();
 		        }, error: function() {
 		        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemlateFail@')
 		    }, cache: false
  			});
  		})
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}

function batchBindTemplate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var selectIds = sm.getSelections();
		for(var i = 0;i<selectIds.length;i++){
			entityIdArray[entityIdArray.length]  = selectIds[i].data.entityId;
		}
		$("#content").hide();
		$("#frameOuter").show();
		var m = Math.random();
		$("#frameInner").attr("src",'/performance/perfThreshold/showTemplateDetailList.tv?entityIds='+entityIdArray+"&templateType=" + parentType +"&m="+m);
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}
function openThresholdAlarm(entityId){
	//开启前先判断该设备是否绑定模版
	var templateName = grid.getSelectionModel().getSelections()[0].data.templateName;
	if (templateName == null || templateName == "") {
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemplateTip3@')
		return
	}
	entityIdArray = [];
	entityIdArray[0] = entityId;
	window.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmOpenPerfAlert@', function(type) {
		if (type == 'no'){return;}
		window.top.showWaitingDlg('@COMMON.wait@', '@Performance.openDevicePerfAlert@', 'ext-mb-waiting');
		$.ajax({
	       url: '/performance/perfThreshold/openDeviceThreshold.tv?entityIds='+ entityIdArray,
	       type: 'POST',
	       success: function() {
	    	   window.parent.closeWaitingDlg();
	    	   refreshGrid();
	        }, error: function() {
	        	window.parent.closeWaitingDlg();
	        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.openPerfAlertFail@')
	    }, cache: false
		});
	})
}

function closeThresholdAlarm(entityId){
	//关闭前先判断该设备是否绑定模版
	var templateName = grid.getSelectionModel().getSelections()[0].data.templateName;
	if (templateName == null || templateName == "") {
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemplateTip4@')
		return
	}
	entityIdArray = [];
	entityIdArray[0] = entityId;
	window.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmClosePerfAlert@', function(type) {
		if (type == 'no'){return;}
		window.top.showWaitingDlg('@COMMON.wait@', '@Performance.closePerfAlert@', 'ext-mb-waiting');
		$.ajax({
	       url: '/performance/perfThreshold/closeDeviceThreshold.tv?entityIds='+ entityIdArray,
	       type: 'POST',
	       success: function() {
	    	   window.parent.closeWaitingDlg();
	    	   refreshGrid();
	        }, error: function() {
	        	window.parent.closeWaitingDlg();
	        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.closePerfAlertFail@')
	    }, cache: false
		});
	})
}

function linkThresholdTemplate(entityId){
	var entityIds = [];
	entityIds[0] = entityId;
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showTemplateDetailList.tv?entityIds='+entityIds+"&templateType="+parentType+"&m="+m);
}

function removeThresholdTemplate(entityId){
	entityIdArray = [];
	entityIdArray[0] = entityId;
	window.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmNoRela@', function(type) {
		if (type == 'no'){return;}
		window.top.showWaitingDlg('@COMMON.wait@', '@Performance.isNoRelaTemplate@', 'ext-mb-waiting');
		$.ajax({
	       url: '/performance/perfThreshold/removeEntityRelaTemplate.tv?entityIds='+ entityIdArray,
	       type: 'POST',
	       success: function() {
	    	   window.parent.closeWaitingDlg();
	    	   refreshGrid();
	        }, error: function() {
	        	window.parent.closeWaitingDlg();
	        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.noRelaTemlateFail@')
	    }, cache: false
		});
	})
}