function setSelectedPerfNames(para){
	$("#selectedPerfNames").val(para);
}

function showEntityDetail(entityId, entityName){
	window.parent.addView('entity-' + entityId, entityName , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
}

function deviceNameRender(value, p, record){
	var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{1}</a>';
	return String.format(formatStr, record.data.entityId, record.data.deviceName);
}

function ipRender(value, p, record){
	var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
	return String.format(formatStr, record.data.entityId, record.data.deviceName, record.data.manageIp);
}

$(document).ready(function(){
	$("#modifyDiv").css("display","none");
	//构造列
	default5Column();
	//生成store
	store = new Ext.data.JsonStore({
		url: '/epon/perfTarget/loadOltPerfTargetList.tv',
		root: 'data',
        totalProperty:'rowCount',
        fields:['entityId', 'deviceName', 'manageIp', 'cpuUsed', 'memUsed', 'flashUsed', 
                'boardTemp', 'fanSpeed', 'optLink', 'sniFlow', 'ponFlow', 'onuPonFlow', 'uniFlow'],
       	sortInfo:{field:'entityId',direction:'asc'}
	});
	
	//创建顶部工具栏
	createTopToolbar();
	//创建修改页面的顶部工具栏
	createModifyTopToolbar();

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
		loadMask: true,
		cls: 'normalTable edge10',
		title:'@Tip.gridTitle@',
		viewConfig: gridView
	});
	store.load({params: {start:0,limit:pageSize}});
	
	//分组指标的开闭效果
	groupExpandOrCollapse();
	//绑定checkbox的事件
	golbalCbxBindEvent();
	
	viewPort = new Ext.Viewport({
		layout : 'border',
		items : [ {
			region : 'north',
			border: false,
			height : 90,
			contentEl : "north",
			autoScroll: true
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
	$("#optLinkHelpPng").click(function(){
		var $tip = $("#yellowTip"); 
		$tip.fadeTo("fast",0,function(){
			$tip.fadeTo("normal",1);
		})
	})

});

/**
 * 查询符合条件的OLT
 */
function query(){
	if(!queryValidate()){return false};
	var deviceName = $("#deviceName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	store.baseParams = {
		deviceName: deviceName,
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

function closeBatchModifyFrame(){
	$("#mainDiv").css("display","block");
	$("#modifyDiv").css("display","none");
	$("#saveAsGolbalPerfOuter").css("display","none");
}

/**
 * 操作列的renderer函数
 * @param value
 * @param p
 * @param record
 * @returns {String}
 */
function operatorRender(value, p, record){
	var str = "<a href='javascript:modifyCycle(\""+record.get('entityId')+"\");' style='margin-right:10px'>@Tip.edit@</a>";
	return str; 
}

/**
 * 展示修改页面
 */
function modifyCycle(entityId){
	$("#modifyDiv").show();
	//从后台获取该OLT的值
	$.ajax({
		url: '/epon/perfTarget/loadOltPerfTargetById.tv',
    	type: 'POST',
    	data: {entityId:entityId},
    	dataType:"json",
   		success: function(json) {
   			window.parent.closeWaitingDlg();
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
				var $input = $(this);
				$input.bind("focus",function(){
					_inputFocus($input.attr("id"), '@Tip.collectTimeRule@');
				}).bind("blur",function(){
					_inputBlur(this);
				}).bind("click",function(){
					_clearOrSetTips(this, '');
				});
			});
			//判断全选checkbox是否选上
			$("tbody").each(function(){
				if($(this).find(".perfCheckbox").length == $(this).find(".perfCheckbox:checked").length){
					$(this).find(".allSelectCheck").attr("checked",true);
				}else{
					$(this).find(".allSelectCheck").attr("checked",false);
				}
			});
   		}, error: function(json) {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

/**
 * 构造默认的5列性能指标
 */
function default5Column(){
	sm = new Ext.grid.CheckboxSelectionModel();
	cm = new Ext.grid.ColumnModel([
	    sm,
	    {header:'@Performance.deviceId@', dataIndex: 'entityId', align:"center",resizable: false, hidden: true},
		{header:'@Performance.deviceName@', dataIndex: 'deviceName', align:"center", width:w*0.12,resizable: false, renderer: deviceNameRender},
		{header:'@Performance.manageIp@', dataIndex: 'manageIp', align:"center", width:w*0.12,resizable: false, renderer: ipRender},
		{header:'@Performance.cpuUsed@', dataIndex: 'cpuUsed', align:"center", width:130,renderer: collectRender,resizable: false, menuDisabled : false},
		{header:'@Performance.memUsed@', dataIndex: 'memUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.flashUsed@', dataIndex: 'flashUsed', align:"center", width:150,renderer: collectRender,resizable: false},
		{header:'@Performance.boardTemp@', dataIndex: 'boardTemp', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.fanSpeed@', dataIndex: 'fanSpeed', align:"center", width:150,renderer: collectRender,resizable: false},
		{header:'@Tip.operator@', dataIndex: 'operator', align:"center", width:w*0.08,renderer: operatorRender,resizable: false, fixed : true}
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
		{header:'@Performance.deviceName@', dataIndex: 'deviceName', align:"center", width:100,resizable: false, renderer: deviceNameRender},
		{header:'@Performance.manageIp@', dataIndex: 'manageIp', align:"center", width:100,resizable: false, renderer: ipRender},
		{header:'@Performance.cpuUsed@', dataIndex: 'cpuUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.memUsed@', dataIndex: 'memUsed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.flashUsed@', dataIndex: 'flashUsed', align:"center", width:140,renderer: collectRender,resizable: false},
		{header:'@Performance.boardTemp@', dataIndex: 'boardTemp', align:"center", width:140,renderer: collectRender,resizable: false},
		{header:'@Performance.fanSpeed@', dataIndex: 'fanSpeed', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.optLink@', dataIndex: 'optLink', align:"center", width:140,renderer: collectRender,resizable: false},
		{header:'@Performance.sniFlow@', dataIndex: 'sniFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.ponFlow@', dataIndex: 'ponFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.onuPonFlow@', dataIndex: 'onuPonFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Performance.uniFlow@', dataIndex: 'uniFlow', align:"center", width:130,renderer: collectRender,resizable: false},
		{header:'@Tip.edit@', dataIndex: 'operator', align:"center", width:100,renderer: operatorRender,resizable: false, fixed : true}
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
           createBatchConfigButton(), '->',
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
           {text: '@Tip.back@',cls:'mL10', iconCls: 'bmenu_back', handler: back},'-',
           {text: '@Tip.save@', iconCls: 'bmenu_save', handler: saveModifyOneOltPerf},
           {text: '@Tip.applyToAllOLT@', iconCls: 'bmenu_view', handler: showApplyToAllOlt},
           {text: '@Tip.saveAsGlobalConfig@', iconCls: 'bmenu_save', handler: showSaveAsGolbal},
           {text: '@Tip.useGolbalConfig@', iconCls: 'bmenu_arrange', handler: useOltGolbalData},
           createSetTimeButton()
           ]
    });
}


/**
 * 展示默认5个性能指标
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
 * 展示保存为全局变量页面
 */
function showSaveAsGolbal(){
    //需要先校验数据
    if(validatePerfValue()==false){return;}
	$("#modifyDiv").hide();
	$("#saveAsGolbalPerfOuter").show();
	var m = Math.random();
	var formatDataStr = $("#modifyForm").serialize();
	$("#saveAsGolbalPerf").attr("src","/epon/perfTarget/showOltPerfTargetComparison.tv?m="+m+formatDataStr);
	var lPos = ($("#saveAsGolbalPerfOuter").width() - $("#loading").outerWidth())/2;
	var tPos = ($("#saveAsGolbalPerfOuter").height() - $("#loading").outerHeight())/2;
	$("#loading").show().css({top:tPos,left:lPos});	
}

/**
 * 关闭等待框的方法
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
 * 保存对单个OLT性能指标的修改
 */
function saveModifyOneOltPerf(){
	if(validatePerfValue()==false){return;}
	var entityId = $("#entityId").val();
	$.ajax({
		url: '/epon/perfTarget/modifyOltPerfTargetById.tv',
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
 * 应用到所有OLT
 */
function showApplyToAllOlt(){
	if(validatePerfValue()==false){return;}
	//弹出窗口要求选择哪些指标应用到所有的OLT
	window.top.createDialog('applyToAllEpon', '@Tip.selectPerfTarget@', 630, 370, '/epon/perfTarget/showOltPerfTargetSelectPage.tv', null, true, true);
}

function applyToAllOlt(){
	$.ajax({
		url: '/epon/perfTarget/applyToAllOlt.tv',
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
 * 使用OLT全局配置
 */
function useOltGolbalData(){
	var entityId = $("#entityId").val();
	window.parent.showConfirmDlg('@COMMON.tip@','@Tip.confirmUseGolbalConfig@', function(type) {
		if (type == 'no'){return;}
		$.ajax({
			url: '/epon/perfTarget/useOltGolbalPerfTaeget.tv',
	    	type: 'POST',
	    	data: {entityId:entityId},
	    	dataType:"json",
	   		success: function(json) {
	   			if(json.result){
	   				top.afterSaveOrDelete({
	   					title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@Tip.saveSuccess@</b>'
	   	   			});
	   				$.ajax({
	   					url: '/epon/perfTarget/loadOltPerfTargetById.tv',
	   			    	type: 'POST',
	   			    	data: {entityId:entityId},
	   			    	dataType:"json",
	   			   		success: function(json) {
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
	   			   		}, error: function(json) {
	   			   			window.parent.showErrorDlg();
	   					}, cache: false,
	   					complete: function (XHR, TS) { XHR = null }
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
 * 批量修改一个Olt指标
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
	window.top.createDialog('batchModifyOnePerf', '@Tip.selectPerfTarget@', 630, 370, '/epon/perfTarget/showBatchModifyOltSinglePerfTarget.tv?entityIds=' + entityIds.join(","), null, true, true);
}

/**
 * 批量修改一组Olt指标
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
	window.top.createDialog('batchModifyGroupOltPerf', '@Tip.selectPerfTarget@', 630, 370, '/epon/perfTarget/showBatchModifyOltGroupPerfTarget.tv?entityIds=' + entityIds.join(","), null, true, true);
}

/**
 * 批量修改所有Olt指标
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
	$("#saveAsGolbalPerf").attr("src","/epon/perfTarget/showBatchModifyOltAllPerfTarget.tv?entityIds="+entityIds.join(",")+"&m="+m);
	var lPos = ($("#saveAsGolbalPerfOuter").width() - $("#loading").outerWidth())/2;
	var tPos = ($("#saveAsGolbalPerfOuter").height() - $("#loading").outerHeight())/2;
	$("#loading").show().css({top:tPos,left:lPos});
}

/**
 * 不分页展示列表
 */
function showAllClick(){
	var deviceName = $("#deviceName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	
	bbar.unbind(store);
	store.load({params: {start:0,limit:-1,deviceName: deviceName,manageIp: manageIp ,perfType:perfType,collectCycle:collectCycle}});
	bbar.hide().setHeight(0);
}

/**
 * 切换到分页展示模式
 */
function showCurrentPageClick(){
	var deviceName = $("#deviceName").val();
	var manageIp = $("#manageIp_").val();
	var perfType = $("#perfType").val();
	var collectCycle = $("#collectCycle").val();
	
	bbar.bind(store);
	store.load({params: {start:0,limit:pageSize,deviceName: deviceName,manageIp: manageIp ,perfType:perfType,collectCycle:collectCycle}});
	bbar.show().setHeight(30);
}