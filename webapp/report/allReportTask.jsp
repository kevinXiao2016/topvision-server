<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
	library ext
	library zeta
    module report
    import	report.javascript.ReportTaskUtil 
    import js.json2
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.report.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
	var pageSize = <%=uc.getPageSize()%>;
	var store = null;
	var SelectionModel = null;
	var grid = null;

	function onRefreshClick() {
		//在执行完相关操作后去掉grid表头上的复选框选中状态
		grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
		store.reload({
			callback : function(){
				disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
			}
		});
	}
	function onDeleteClick() {
		var sm = grid.getSelectionModel();
		if (sm != null && sm.hasSelection()) {
			var selections = sm.getSelections();
			var taskIdList = [];
			for (var i = 0; i < selections.length; i++) {
				taskIdList[i] = selections[i].data.taskId;
			}
			window.top.showConfirmDlg(I18N.report.tip, I18N.report.confirmTodelReportTask, function(type) {
				if (type == 'no') {return;}
				$.ajax({
					url:'deleteReprotTask.tv',
					data: {taskIdList : taskIdList},
					success:function(){
						//ReportTaskUtil.refreshTaskTree();
						   onRefreshClick();
						   top.afterSaveOrDelete({
								title: '@COMMON.tip@',
								html: '<b class="orangeTxt">@report.deleteSuccess@</b>'
							});
					},error:function(){
						//ReportTaskUtil.refreshTaskTree();
						   window.parent.showErrorDlg();
					}
				});
			});
		} else {
			window.parent.showMessageDlg(I18N.report.tip, I18N.report.selectReportTask);
		}
	}
	function onStartClick() {
		var sm = grid.getSelectionModel(), msg = '';
		if (sm != null && sm.hasSelection()) {
			var selections = sm.getSelections(), taskIdList = [];
			//此处需要进行修改，对于已经开启的报表任务是不需要进行再次开启的
			for (var i = 0; i < selections.length; i++) {
				if(selections[i].data.state ==='true'|| selections[i].data.state ===true){
					continue;
				}
				taskIdList[taskIdList.length] = selections[i].data.taskId;
			}
			if(taskIdList.length===0){
				//所选报表任务没有处于关闭状态的
				window.parent.showMessageDlg(I18N.report.tip, I18N.report.plsSelectClosed);
				return;
			}
			if(taskIdList.length < selections.length){
				//所选报表任务中有部分是处于开启状态的报表
				msg =  I18N.report.closeTip;
			}else{
				msg = I18N.report.confirmToEnableTask;
			}
			window.top.showConfirmDlg(I18N.report.tip, msg, function(type) {
				if (type == 'no') {return;}
				Ext.Ajax.request({url: 'enableReportTask.tv',
				   success: function() {onRefreshClick();},
				   failure: function(){window.parent.showErrorDlg();},
				   params: {taskIdList : taskIdList}
				});			
			});
		} else {
			window.parent.showMessageDlg(I18N.report.tip, I18N.report.selectReportTask);
		}
	}
	function onStopClick() {
		var sm = grid.getSelectionModel(), msg = '';
		if (sm != null && sm.hasSelection()) {
			var selections = sm.getSelections(), taskIdList = [];
			//此处需要进行修改，对于已经关闭的报表任务是不需要进行再次关闭的
			for (var i = 0; i < selections.length; i++) {
				if(selections[i].data.state ==='false'|| selections[i].data.state ===false){
					continue;
				}
				taskIdList[taskIdList.length] = selections[i].data.taskId;
			}
			if(taskIdList.length===0){
				//所选报表任务没有处于开启状态的
				window.parent.showMessageDlg(I18N.report.tip, I18N.report.plsSelectOpened);
				return;
			}
			if(taskIdList.length < selections.length){
				//所选报表任务中有部分是处于关闭状态的报表
				msg = I18N.report.openTip;
			}else{
				msg = I18N.report.confirmToDisableTask;
			}
			window.top.showConfirmDlg(I18N.report.tip, msg, function(type) {
				if (type == 'no') {return;}
				Ext.Ajax.request({url: 'disableReportTask.tv',
				   success: function() {onRefreshClick();},
				   failure: function(){window.parent.showErrorDlg();},
				   params: {taskIdList : taskIdList}
				});			
			});
		} else {
			window.parent.showMessageDlg(I18N.report.tip, I18N.report.selectReportTask);
		}
	}

	function renderState(value, p, record) {
		if (value) {
			return '<img nm3kTip="@report.enable@" class="nm3kTip" src="../images/yes.png" border=0 align=absmiddle>';
		} else {
			return '<img nm3kTip="@report.stop@" class="nm3kTip" src="../images/wrong.png" border=0 align=absmiddle>';
		}
	}
	Ext.BLANK_IMAGE_URL = '../images/s.gif';
	Ext.onReady(function() {
		SelectionModel = new Ext.grid.CheckboxSelectionModel({
			header : '<div class="x-grid3-hd-checker"></div>',
			singleSelect : false,
			listeners : {
				rowselect : function(sm,rowIndex,record){
		            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		        },
		        rowdeselect : function(sm,rowIndex,record){
		            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		        }
			}
		});
		var cm = [
			SelectionModel,
			{header : '<div class="txtCenter">@report.taskName@</div>', sortable : true, dataIndex : 'taskName', align: 'left'},
			{header : '<div class="txtCenter">@report.taskModule@</div>', sortable : true, dataIndex : 'reportName', align: 'left'},
			{header : '<div class="txtCenter">@report.reportDesc@</div>', sortable : true, dataIndex : 'note', align: 'left'},
			{header : '<div class="txtCenter">@report.reportAuthor@</div>', sortable : true, dataIndex : 'author', align: 'left'},
			{header : '@report.taskType@', sortable : true, dataIndex : 'cycleTypeString', width:90},
			{header : '@report.nextExcuteTime@', sortable : true, dataIndex : 'executorTimeString'},
			{header : '@report.status@', sortable : true, dataIndex : 'state', width : 60, renderer : renderState},
			{header : '<div class="txtCenter">@report.operate@</div>', sortable : true, dataIndex :'op', renderer: opeartionRender, width:160, fixed:true, align:'center'} 
		];

		store = new Ext.data.JsonStore({
			url : 'getReportTaskByDetail.tv',
			root : 'data',
			totalProperty : 'totalProperty',
			remoteSort : false,
			fields : ['taskName', 'templateName', 'note', 'statTimeString', 'executorTimeString', 'state',
			          'cycleTypeString', 'taskId', 'author', 'reportId', 'reportName']
		});

				grid = new Ext.grid.GridPanel({
					    stripeRows:true,
				   		region: "center",
				   		bodyCssClass: 'normalTable',
						store : store,
						border : false,
						columns : cm,
						viewConfig:{ forceFit: true  },
						sm:SelectionModel,
						loadMask : true,
						tbar : [ {
								xtype : 'tbspacer', width:5
							},{
								text : '@report.create@', iconCls: 'bmenu_new', handler : newReportTask
							},  '-', {
								text : '@report.refresh@', iconCls : 'bmenu_refresh', handler : onRefreshClick
							}, '-', {
								text : '@report.enable@', iconCls : 'bmenu_play', handler : onStartClick, disabled: true, id: 'enableBtn'
							}, {
								text : '@report.stop@', iconCls : 'bmenu_stop', handler : onStopClick, disabled: true, id: 'stopBtn'
							}, '-', {
								text : '@report.remove@', iconCls : 'bmenu_delete', handler : onDeleteClick, disabled: true, id: 'removeBtn'
							} ],
						bbar : buildPagingToolBar()
					});

				new Ext.Viewport({
					layout : 'fit',
					items : [ grid ]
				});
				store.load({
					params : {
						start : 0,
						limit : pageSize
					}
				});
			});
	function buildPagingToolBar() {
		var pagingToolbar = new Ext.PagingToolbar({
			id : 'extPagingBar',
			pageSize : pageSize,
			store : store,
			displayInfo : true,
			items : [ "-", String.format('@report.displayperpage@', pageSize), '-' ],
			listeners : {
				beforechange : function(){
					disabledToolbarBtn(0);
				}
			}
		});
		return pagingToolbar;
	}

	function newReportTask() {
		window.parent.createDialog("modalDlg",'@report.createReportTask@',600, 500, "report/showNewReportTask.tv", null, true,true);
	}
	
	function opeartionRender(value, cellmate, record){
		var taskId = record.data.taskId;
		return String.format("<a href='javascript:;' onClick='showReportDetail({0})'>@report.viewReportDetail@</a>  / <a href='javascript:;' onClick = 'showModifyReportTask({0})'>@report.modifyReportTask@</a>", taskId);
	}
	
	function showModifyReportTask(taskId){
		window.parent.createDialog("modalDlg","@report.modifyReportTask@",600, 370, "report/showModifyReportTask.tv?taskId="+taskId, null, true,true);
	}
	
	function showReportDetail(v){
		window.parent.addView("allreport", '@report.allReports@', "reportIcon", "report/showAllReport.tv?taskId="+v,null,true);
	}
	function disabledToolbarBtn(num){ //num为选中的的行的个数;
	    if(num > 0){
	        disabledBtn(['enableBtn', 'stopBtn', 'removeBtn'], false);
	    }else{
	    	disabledBtn(['enableBtn', 'stopBtn', 'removeBtn'], true);
	    }
	};
	function disabledBtn(arr, disabled){
		$.each(arr, function(i, v){
	    	Ext.getCmp(v).setDisabled(disabled);
		})
	};
</script>
</head>
<body class="whiteToBlack">
</body>
</Zeta:HTML>