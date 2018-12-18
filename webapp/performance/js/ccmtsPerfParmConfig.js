var cm = null;
var store = null;
var grid = null;
var tbar = null;
var bbar = null;
var data = null;
var sm = null;
var h = null;
var w = null;
var viewPort = null;

function showEntityPerf(cmcId,entityName,data,cmcDeviceStyle){
	window.parent.addView('entity-' + cmcId, entityName , 'entityTabIcon','/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=' + cmcId);
}

function showEntityDetail(entityId, entityName){
	window.parent.addView('entity-' + entityId, entityName , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
}

function deviceNameRender(value, p, record){
	var formatStr = '<a href="#" onclick="showEntityPerf({0},\'{1}\')">{1}</a>';
	return String.format(formatStr, record.data.entityId, record.data.deviceName);
}

function ipRender(value, p, record){
	var typeId = record.data.typeId;
	if(typeId == 30001 || typeId == 30005){
		var formatStr = '<a href="javascript:;" onclick="showEntityDetail({0},\'{1}\')">{1}</a>';
		return String.format(formatStr, record.data.parentId, record.data.manageIp);
	}else{
		var formatStr = '<a href="javascript:;" onclick="showEntityPerf({0},\'{1}\')">{2}</a>';
		return String.format(formatStr, record.data.entityId, record.data.deviceName, record.data.manageIp);
	}
}

$(document).ready(function(){
	$("#modifyDiv").css("display","none");
	//构造默认5列
	default5Column();
	//生成store
	store = new Ext.data.JsonStore({
		url: '/cmc/perfTarget/loadCmcPerfTargetList.tv',
		root: 'data',
        totalProperty:'rowCount',
        fields:['entityId', 'deviceName', 'manageIp', 'typeId', 'parentId','cpuUsed', 'memUsed', 'flashUsed', 
                'optLink', 'upLinkFlow', 'macFlow', 'channelSpeed', 'moduleTemp', 'snr', 'ber','cmflap','opticalReceiver'],
       	sortInfo:{field:'entityId',direction:'asc'}
	});
	//创建顶部工具栏
	createTopToolbar();
	//创建修改页面的顶部工具栏
	createModifyTopToolbar();
	//填充CMC子类型下拉框
	$.each(allEntityTypes, function(index, subType){
		$('select#deviceType').append('<option value="'+subType.typeId+'">'+subType.displayName+'</option>');
	})
	bbar = new Ext.PagingToolbar({
		pageSize: pageSize,
		cls: 'extPagingBar',
		store: store,
		displayInfo: true,
		displayMsg: '@Tip.paginateMsg@',
		emptyMsg: "@Tip.noRecord@"
	});
	
	window.gridView = new Ext.grid.GridView ({
		 forceFit: true
	});
	
	grid = new Ext.grid.EditorGridPanel({
		region : 'center',
		store: store,
		cm: cm,
		sm: sm,
		bbar: bbar,
		border: false,
		loadMask: true,
		title:'@Tip.gridTitle@',
		cls: 'normalTable',
		viewConfig: gridView
	});
	store.load({params: {start:0,limit:pageSize}});
	
	groupExpandOrCollapse();
	golbalCbxBindEvent();
	
	viewPort = new Ext.Viewport({
		layout : 'border',
		items : [ {
			region : 'north',
			height : 90,
			contentEl : "north",
			autoScroll: true,
			border: false
		}, grid]
	});
	
	var _layout_ = Ext.grid.GridView.prototype.layout;
	Ext.grid.GridView.prototype.layout = function(){
		if(this.forceFit){
	        this.scroller.setStyle('overflow-x', 'hidden');
	    }else{
	    	this.scroller.setStyle('overflow-x', 'auto');
	    }
		_layout_.apply( this );
	}
	
	//点击提示，提示信息闪烁一下;
	$("#optLinkHelpPng1").click(function(){
		var $tip = $("#yellowTip1"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	$("#optLinkHelpPng2").click(function(){
		var $tip = $("#yellowTip2"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	$("#optLinkHelpPng3").click(function(){
		var $tip = $("#yellowTip3"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	$("#optLinkHelpPng4").click(function(){
		var $tip = $("#yellowTip4"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})
	
});

/**
 * 查询符合条件的CCMTS
 */
function query(){
	if(!queryValidate()){return false};
	
	var deviceName = $("#cmcName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	var entityType = $("#deviceType").val();
	
	store.baseParams = {
		deviceName: deviceName,
		entityType: entityType,
		manageIp: manageIp,
		perfType:perfType,
		collectCycle: collectCycle,
		start: 0,
		limit: pageSize
    }
	store.load();
}

/**
 * 刷新grid
 */
function onRefreshClick(){
	//在执行完相关操作后去掉grid表头上的复选框选中状态
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
	
	store.reload();
}

/**
 * 操作列的renderer方法
 * @param value
 * @param p
 * @param record
 * @returns {String}
 */
function operatorRender(value, p, record){
	var str = "<a href='javascript:;' onclick='modifyCycle(\""+record.get('entityId')+"\")'>@Tip.edit@</a>";
	//var str ="<image title='@Tip.edit@' src='/images/performance/document_edit.png' onclick='modifyCycle(\""+record.get('entityId')+"\")' />";
	return str; 
}

/**
 * 编辑单个CCMTS的性能采集指标
 */
function modifyCycle(entityId){
	$("#modifyDiv").show();
	//从后台获取该CCMTS的值
	$.ajax({
		url: '/cmc/perfTarget/loadCmcPerfTargetById.tv',
    	type: 'POST',
    	data: {entityId:entityId},
    	dataType:"json",
   		success: function(json) {
			//为修改页面赋值
			for(var key in json.perfTargetCycle){
				$("#modifyForm #"+key).val(json.perfTargetCycle[key]);
			}
			for(var key in json.perfTargetCycle.enableList){
				if(json.perfTargetCycle.enableList[key]==1){
					$("#modifyForm #"+key).attr("checked", true).val(1);
				}else{
					$("#modifyForm #"+key).attr("checked", false).val(0);
				}
			}
			
			$(".numberInput").each(function(){
				$(this).attr("toolTip",'@Tip.collectTimeRule@');
			});
			//判断全选checkbox是否选上
			$("tbody").each(function(){
				if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
					$(this).find(".allSelectCheck").attr("checked",true);
				}
			});
   		}, error: function(cmcPerfTargetCycle) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 构造默认5个性能指标
 */
function default5Column(){
	sm = new Ext.grid.CheckboxSelectionModel();
	cm = new Ext.grid.ColumnModel([
	    sm,
	    {header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false,  hidden: true},
		{header:'@Performance.deviceName@', dataIndex: 'deviceName', align:"center",resizable: false, renderer: deviceNameRender},
		{header:'@Performance.manageIp@', dataIndex: 'manageIp', id: 'manageIp', align:"center",resizable: false, renderer: ipRender},
		{header:'@Performance.cpuUsed@', dataIndex: 'cpuUsed', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.memUsed@', dataIndex: 'memUsed', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.flashUsed@', dataIndex: 'flashUsed', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.upLinkFlow@', dataIndex: 'upLinkFlow', align:"center",renderer: collectRender,resizable: false},
		{header:'@Performance.macFlow@', dataIndex: 'macFlow', align:"center",renderer: collectRender,resizable: false},
		{header:'@Tip.operator@', dataIndex: 'operator', align:"center",renderer: operatorRender,resizable: false}
	]);
}

/**
 * 构造所有性能指标
 */
function allColumn(){
	sm = new Ext.grid.CheckboxSelectionModel();
	cm = new Ext.grid.ColumnModel([
	    sm,
	    {header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center", width:100,resizable: false,  hidden: true},
		{header:'@Performance.deviceName@', dataIndex: 'deviceName', id: 'cmcName', align:"center", width:100,resizable: false, renderer: deviceNameRender},
		{header:'@Performance.manageIp@', dataIndex: 'manageIp', align:"center", width:100,resizable: false, renderer: ipRender},
		{header:'@Performance.cpuUsed@', dataIndex: 'cpuUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.memUsed@', dataIndex: 'memUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.flashUsed@', dataIndex: 'flashUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.optLink@', dataIndex: 'optLink', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.upLinkFlow@', dataIndex: 'upLinkFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.macFlow@', dataIndex: 'macFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.channelSpeed@', dataIndex: 'channelSpeed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.moduleTemp@', dataIndex: 'moduleTemp', align:"center", width:140,renderer: collectRender,resizable: false},
		{header:'@Performance.snr@', dataIndex: 'snr', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.ber@', dataIndex: 'ber', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'FLAP', dataIndex: 'cmflap', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.optReceiverPower@', dataIndex: 'opticalReceiver', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Tip.operator@', dataIndex: 'operator', align:"center", width:70,renderer: operatorRender,resizable: false}
	]);
}

/**
 * 创建顶部工具栏
 */
function createTopToolbar(){
	new Ext.Toolbar({
        renderTo: "toolbar",
        items: [
           {text: '@Tip.refresh@',cls:'mL10', iconCls: 'bmenu_refresh', handler: onRefreshClick},
           createBatchConfigButton(),'->',
           createColumnChangesButton()
           ]
    });
}

/**
 * 创建修改页面的顶部工具栏
 */
function createModifyTopToolbar(){
	new Ext.Toolbar({
        renderTo: "toolbar-back",
        items: [
           {text: '@Tip.back@', iconCls: 'bmenu_back',cls:'mL10', handler: back},'-',
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveModifyOneCmcPerf},
           {text: '@Tip.applyToAllCmc@', iconCls: 'bmenu_view', handler: showApplyToAllCmc},
           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_save', handler: saveAsGolbal},
           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useCmcGolbalData},
           createSetTimeButton()
           ]
    });
}

/**
 * 展示默认的六项性能指标
 */
function showDefault5Perf(){
	default5Column();
	grid.getView().forceFit = true;
	grid.getView().refresh();
	grid.reconfigure(store, cm);
	grid.doLayout();
}

/**
 * 展示所有的性能指标
 */
function showAllPerf(){
	allColumn();
	grid.getView().forceFit = false;
	grid.getView().refresh();
	grid.reconfigure(store, cm);
	grid.doLayout();
}

/**
 * 打开保存为全局变量页面
 */
function saveAsGolbal(){
    //需要先校验数据
    if(validatePerfValue()==false){return;}
	$("#modifyDiv").hide();
	$("#saveAsGolbalPerfOuter").show();
	var m = Math.random();
	var formatDataStr = $("#modifyForm").serialize();
	$("#saveAsGolbalPerf").attr("src","/cmc/perfTarget/showCmcPerfTargetComparison.tv?m="+m+formatDataStr);
	var lPos = ($("#saveAsGolbalPerfOuter").width() - $("#loading").outerWidth())/2;
	var tPos = ($("#saveAsGolbalPerfOuter").height() - $("#loading").outerHeight())/2;
	$("#loading").show().css({top:tPos,left:lPos});	
}

/**
 * 关闭等待框
 */
function closeLoading(){
	$("#loading").fadeOut('slow');
}

/**
 * 关闭frame
 */
function closeFrame(){
	$("#mainDiv").css("display","none");
	$("#modifyDiv").css("display","block");
	$("#saveAsGolbalPerfOuter").css("display","none");
}

/**
 * 关闭批量修改的FRAME
 */
function closeBatchModifyFrame(){
	$("#mainDiv").css("display","block");
	$("#modifyDiv").css("display","none");
	$("#saveAsGolbalPerfOuter").css("display","none");
}

/**
 * 保存对单个CCMTS性能指标的修改
 */
function saveModifyOneCmcPerf(){
	if(validatePerfValue()==false){return;}
	$.ajax({
		url: '/cmc/perfTarget/modifyCmcPerfTargetById.tv',
    	type: 'POST',
    	data: $("#modifyForm").serialize(),
    	dataType:"json",
   		success: function(result) {
   			if(result){
   				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@Tip.modifySuccess@</b>'
   	   			});
   			}else{
   				window.parent.showErrorDlg();
   			}
   		}, error: function(result) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 展示应用到所有CCMTS
 */
function showApplyToAllCmc(){
	if(validatePerfValue()==false){return;}
	//弹出窗口要求选择哪些指标应用到所有的CCMTS
	window.top.createDialog('applyToAllCmc', '@Tip.selectPerfTarget@', 630, 370, '/cmc/perfTarget/showCmcPerfTargetSelectPage.tv', null, true, true);
}

/**
 * 应用到所有CCMTS
 */
function applyToAllCmc(){
	$.ajax({
		url: '/cmc/perfTarget/applyToAllCmc.tv',
    	type: 'POST',
    	data: $("#modifyForm").serialize(),
    	dataType:"json",
   		success: function(result) {
   			if(result){
   				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
   	   			});
   			}else{
   				window.parent.showErrorDlg();
   			}
   		}, error: function(result) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 使用CMC全局配置
 */
function useCmcGolbalData(){
	var entityId = $("#entityId").val();
	window.parent.showConfirmDlg('@COMMON.tip@','@Tip.confirmUseGolbalConfig@', function(type) {
		if (type == 'no'){return;}
		$.ajax({
			url: '/cmc/perfTarget/useCmcGolbalPerfTaeget.tv',
	    	type: 'POST',
	    	data: {entityId:entityId},
	    	dataType:"json",
	   		success: function(json) {
	   			if(json.result){
	   				top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
	   	   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
	   		   		});
	   				//为修改页面赋值
	   				for(var key in json.perfTargetCycle){
	   					$("#"+key).val(json.perfTargetCycle[key]);
	   				}
	   				for(var key in json.perfTargetCycle.enableList){
	   					if(json.perfTargetCycle.enableList[key]==1){
	   						$("#"+key).attr("checked", true).val(1);
	   					}else{
	   						$("#"+key).attr("checked", false).val(0);
	   					}
	   				}
	   				//判断全选checkbox是否选上
	   				$("tbody").each(function(){
	   					if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
	   						$(this).find(".allSelectCheck").attr("checked",true);
	   					}else{
	   						$(this).find(".allSelectCheck").attr("checked",false);
	   					}
	   				});
	   			}else{
	   				window.parent.showErrorDlg();
	   			}
	   		}, error: function(json) {
	   			window.parent.showErrorDlg();
			}, cache: false
		});
	})
}

/**
 * 展示批量修改一个CMC指标页面
 */
function batchModifyOnePerf(){
	var rows = grid.getSelectionModel().getSelections();//获取所选行
	if(rows.length==0){
		window.parent.showMessageDlg('@COMMON.tip@', "@Tip.pleaseselectDevice@");
		return;
	}
	var entityIds = new Array();
	for(var i=0; i<rows.length; i++){
		entityIds[i] = rows[i].data.entityId;
	}
	window.top.createDialog('batchModifyOnePerf', '@Tip.selectPerfTarget@', 630, 370, 'cmc/perfTarget/showBatchModifyCmcSinglePerfTarget.tv?entityIds=' + entityIds.join(","), null, true, true);
}

/**
 * 展示批量修改一组CMC指标页面
 */
function batchModifyGroupPerf(){
	var rows = grid.getSelectionModel().getSelections();//获取所选行
	if(rows.length==0){
		window.parent.showMessageDlg('@COMMON.tip@', "@Tip.pleaseselectDevice@");
		return;
	}
	var entityIds = new Array();
	for(var i=0; i<rows.length; i++){
		entityIds[i] = rows[i].data.entityId;
	}
	window.top.createDialog('batchModifyGroupCmcPerf', '@Tip.selectPerfTarget@', 630, 370, 'cmc/perfTarget/showBatchModifyCmcGroupPerfTarget.tv?entityIds=' + entityIds.join(","), null, true, true);
}

/**
 * 展示批量修改所有CMC指标页面
 */
function batchModifyAllPerf(){
	var rows = grid.getSelectionModel().getSelections();//获取所选行
	if(rows.length==0){
		window.parent.showMessageDlg('@COMMON.tip@', "@Tip.pleaseselectDevice@");
		return;
	}
	var entityIds = new Array();
	for(var i=0; i<rows.length; i++){
		entityIds[i] = rows[i].data.entityId;
	}
	
	$("#saveAsGolbalPerfOuter").show();
	var m = Math.random();
	$("#saveAsGolbalPerf").attr("src","/cmc/perfTarget/showBatchModifyCmcAllPerfTarget.tv?entityIds="+entityIds.join(",")+"&m="+m);
	var lPos = ($("#saveAsGolbalPerfOuter").width() - $("#loading").outerWidth())/2;
	var tPos = ($("#saveAsGolbalPerfOuter").height() - $("#loading").outerHeight())/2;
	$("#loading").show().css({top:tPos,left:lPos});
	
}

/**
 * 不分页展示列表
 */
function showAllClick(){
	var deviceName = $("#cmcName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	var entityType = $("#deviceType").val();
	
	bbar.unbind(store);
	store.load({params: {start:0,limit:-1,deviceName:deviceName,manageIp:manageIp,perfType:perfType,collectCycle:collectCycle,entityType:entityType}});
	bbar.hide().setHeight(0);
}

/**
 * 切换到分页展示模式
 */
function showCurrentPageClick(){
	var deviceName = $("#cmcName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	var entityType = $("#deviceType").val();
	
	bbar.bind(store);
	store.load({params: {start:0,limit:pageSize,deviceName:deviceName,manageIp:manageIp,perfType:perfType,collectCycle:collectCycle,entityType:entityType}});
	bbar.show().setHeight(30);
}