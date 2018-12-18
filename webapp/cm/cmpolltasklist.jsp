<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CM
</Zeta:Loader>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cm.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
	var store = null;
	var grid = null;
	var SelectionModel = null
	function onNewMonitorClick() {
		window.top.createDialog('newCmPoll', I18N.cmPoll.createPollTask, 600, 335,'/cmpoll/showNewCmPoll.tv', null, true, true);
	}
	function onRefreshClick() {
		store.reload();
	}
	function renderSysStatus(value, p, record){
		if (record.data.status == '0') {
			return String.format('<img alt="{0}" src="/images/network/port/admin.gif" border=0 align=absmiddle>',
					I18N.COMMON.on);		
		} else {
			return String.format('<img alt="{0}" src="/images/network/port/noadmin.gif" border=0 align=absmiddle>',
					I18N.COMMON.off);	
		}
	}
	function operationRender(value, cellmate, record) {
		var monitorId = record.data.monitorId;
		return String.format("<a href='javascript:;' onclick='modifyMonitor(\"{0}\")'/>@COMMON.edit@</a>" + 
							" / <a href='javascript:;' onclick='deleteMonitor(\"{0}\")'/>@COMMON.delete@</a>", monitorId);
	}
	function intervalRender(value, cellmate, record){
		var interval = record.data.monitorInterval;
		return interval/60000 + I18N.cmPoll.minute;
	}
	function modifyMonitor(minitorId) {
		if (!SelectionModel.hasSelection()) {
	        window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChoosePollTask);
	        return;
	    }
		var count = SelectionModel.getCount();
		if(count > 1){
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChooseOnePollTask);
	        return;
		}
		var sm = grid.getSelectionModel();
		var record = sm.getSelected();	
		var monitorId = record.data.monitorId;
		var pollTaskName = record.data.name;
		var pollInterval = record.data.monitorInterval;
		var quotaIds = record.data.quotaIds;
		var objectIds = record.data.objectIds;
		window.top.createDialog('modifyCmPoll', I18N.cmPoll.modifyPollTask,  600, 335,
				'/cmpoll/showModifyCmPoll.tv?monitorId=' + monitorId + '&pollTaskName=' + pollTaskName + '&pollInterval=' + pollInterval + '&quotaIds=' + quotaIds + '&objectIds=' + objectIds, null, true, true);
	}
	function onDeleteClick(){
		if (!SelectionModel.hasSelection()) {
	        window.top.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.pleaseChoosePollTask);
	        return;
	    }
	    var selects = SelectionModel.getSelections();
	    var monitorIds = '';
	    $.each(selects, function(i, n) {
	    	monitorIds = monitorIds + n.data.monitorId + ';';
	    });
	    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.cmPoll.confirmDeletePollTask, function(button,text){
			if(button == "yes"){
	    $.ajax({
		    url:"/cmpoll/deletePollTasks.tv?monitorIds=" + monitorIds,
		    type : 'post',
		    success: function(response){
		    	//response = eval("(" + response + ")");
		    	if(response.success) {
		    		top.afterSaveOrDelete({       
	    				title: I18N.COMMON.tip,        
	    				html: '<b class="orangeTxt">' + I18N.COMMON.deleteSuccess + '</b>'    
	    			});
		    		store.reload()
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.topo.virtualDeviceList.removeFail);
		        }
		    }, error: function(response){
		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.topo.virtualDeviceList.removeFail);
			}, 
			cache : false
	    }); 
			}
		});
	}
	function deleteMonitor(monitorId) {
	    window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.cmPoll.confirmDeletePollTask ,function(button,text){
			if(button == "yes"){
	    $.ajax({
		    url:"/cmpoll/deletePollTask.tv?monitorId=" + monitorId,
		    type : 'post',
		    success: function(response){
		    	if(response.success) {
		    		top.afterSaveOrDelete({       
	    				title: I18N.COMMON.tip,        
	    				html: '<b class="orangeTxt">' + I18N.COMMON.deleteSuccess + '</b>'    
	    			});
		    		store.reload()
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.topo.virtualDeviceList.removeFail);
		        }
		    }, error: function(response){
		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.topo.virtualDeviceList.removeFail);
			}, 
			cache : false
	    }); 
			}
		});
	}
	function onFindClick() {}
	
	Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected");  
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){  
	            var r = this.getRow(i);  
	            if(r){  
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	            }  
	        }  
	          
	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	          
	        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }  
	    },  
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");  
	            var selected = 0;  
	            var len = this.grid.store.getCount();  
	            for(var i = 0; i < len; i++){  
	                var r = this.getRow(i);  
	                if(r){  
	                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	                }  
	            }  
	            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	              
	            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');   
	            }  
	    }  
	});  
	Ext.onReady(function() {
		var w = document.body.clientWidth;
		var h = document.body.clientHeight;
		SelectionModel =  new Ext.grid.CheckboxSelectionModel({
			header:'<div class="x-grid3-hd-checker"></div>',
			singleSelect:false
		});
		var columns = [ SelectionModel, {
			header : I18N.cmPoll.pollTaskName,
			width : w / 10,
			sortable : false,
			align : 'center',
			dataIndex : 'name'
		}, {
			header : I18N.COMMON.createTime,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'createTimeString'
		}, {
			header : I18N.SYSTEM.modifyTime ,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'modifyTimeString'
		}, {
			header : I18N.cmPoll.pollInterval,
			width : w / 16,
			sortable : false,
			align : 'center',
			dataIndex : 'monitorInterval',
			renderer : intervalRender
		}, {
			header : I18N.cmPoll.pollTaskObject,
			width : w / 6,
			sortable : false,
			align : 'center',
			dataIndex : 'objectsString'
		},{
			header : I18N.cmPoll.pollQuota,
			width : w / 6,
			sortable : false,
			align : 'center',
			dataIndex : 'quotaString'
		},{
			header : I18N.cmPoll.lastCollectTime,
			width : w / 9,
			sortable : false,
			align : 'center',
			dataIndex : 'lastCollectTimeString'
		}, /* {
			header : I18N.EVENT.statusHeader,
			width : w / 20,
			sortable : false,
			align : 'center',
			dataIndex : 'status',
			renderer : renderSysStatus
		}, */ {
			header : I18N.Config.oltConfigFileImported.operation ,
			width : w / 14,
			sortable : false,
			align : 'center',
			dataIndex : 'op',
			renderer : operationRender
		} ];
		store = new Ext.data.JsonStore({
			url : '/cmpoll/loadCmPollTaskList.tv',
			root : 'data', idProperty : 'monitorId',
			totalProperty : 'rowCount',
			fields : [ 'monitorId', 'name', 'createTimeString', 'objectsString', 'objectIds', 'modifyTimeString', 'quotaString', 'quotaIds', 'monitorInterval', 'lastCollectTimeString',
					'status']
		});

		var cm = new Ext.grid.ColumnModel(columns);
		var toolbar = [{
			text : I18N.COMMON.create ,iconCls : 'bmenu_new', handler : onNewMonitorClick
		}/* , {
			text : I18N.COMMON.modify , iconCls : 'bmenu_edit', handler : modifyMonitor
		} */,{
			text : I18N.COMMON.remove , iconCls : 'bmenu_delete', handler : onDeleteClick
		},{
			text : I18N.COMMON.refresh , iconCls : 'bmenu_refresh', handler : onRefreshClick
		}];
		grid = new Ext.grid.GridPanel({
			stripeRows:true,
	   		region: "center",
	   		bodyCssClass: 'normalTable',
			id : 'pollTaskGrid',
			sm:SelectionModel,
			border : false,
			region : 'center',
			store : store,
			tbar : toolbar,
			cm : cm,
			loadMask : true, 
			viewConfig : {
				forceFit: true,hideGroupedColumn : true, enableNoGroups : true
			}
		});
		new Ext.Viewport({
	  	     layout: "border",
	  	     items: [grid]
	  	}); 
		store.load();
	})
	
function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		// mouseHandled flag check for a duplicate selection (handleMouseDown) call
		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				//判断头部的全选CheckBox框是否选中，如果是，则删除
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}
	
function onHdMouseDown(e, t){
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});
</script>
</head>
<body>
	<div id="pollTask"></div>
</body>
</Zeta:HTML>