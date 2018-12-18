<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ page import="com.topvision.ems.cm.domain.CmPollObject"%>
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
var baseStore=null;
var baseGrid = null;
/*render模块*/
function operationRender(value, cellmate, record) {
	var objectId = record.data.objectId;
	var html = "<a href='javascript:;' onclick='showDetailPollObjectList("+objectId+")' >@COMMON.view@</a> / ";
	html+="<a href='javascript:;' onclick='modifyPollObject("+objectId+")' >@COMMON.modify@</a> / ";
    html+="<a href='javascript:;' onclick='deletePollObject("+objectId+")' >@COMMON.delete@</a>";
    return html;
}
function renderMonitorName(value, cellmate, record){
	var jobName = record.data.jobName;
	if(jobName == ''){
		return I18N.cmPoll.notAddToPollTask
	}else{
		return jobName;
	}
}
/*click模块*/
/*删除监控对象*/
function deletePollObject(value){
	window.top.showOkCancelConfirmDlg(I18N.EVENT.confirm, I18N.cmPoll.confirmDeletePollObject, function (type) {
		if(type=="ok"){
			$.ajax({
			      url: '/cmpoll/deletePollObjectByObjId.tv',
			      type: 'post',
			      dataType:"json",
			      data:{
			    	objectId:value  
			      },
				  cache: false, 
			      success: function(response) {
			    	  if(response.result == "true"){
			    	      if(response.isExistInMonitor){
			    	          window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.deletePollObjectFailPollObjectInTask);
			    	      }else{ //修改成功
			    	    	  top.afterSaveOrDelete({       
			    	    		  title: '@COMMON.tip@',        
			    	    		  html: '<b class="orangeTxt">@resources/COMMON.deleteSuccess@</b>'    
			    	    	  });
			    	      }
			    	  }else{
			    		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.deletePollObjectFail);
			    	  }
			    	  refresh();
			      }, 
			      error: function(response) {
			    	  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.cmPoll.deletePollObjectFail);
			      }
			});
		}
	})
}
/*修改监控对象*/
function modifyPollObject(value){
	var win = window.top.createDialog('modifyPollObjectWin', "@cmPoll.modifyPollObject@", 600, 450, 
			'/cmpoll/showModifyPollObjectPage.tv?objectId='+value, null, true, true, refresh);
}
/*查看监控对象关联的CM详细信息*/
function showDetailPollObjectList(value){
	window.top.createDialog('detailPollObjectWin', "@cmPoll.cmList@", 600, 370, '/cmpoll/showDetailPollObjectList.tv?objectId='+value, null, true, true);
}
/*创建新监控对象*/
function click_CreatePollObject(){
	var win = window.top.createDialog('createPollObjectWin', "@cmPoll.createPollObject@", 600, 450, '/cmpoll/showCreatePollObjectPage.tv', null, true, true);
    win.on('close', function(){
  		refresh();
  	});
}
/*刷新页面*/
function refresh(){
	baseStore.reload();
}
function renderObjectType(value, cellmate, record){
	var objectType = record.data.objectType;
	if(objectType == 0){
		return I18N.SYSTEM.Region;
	}else{
		return record.data.objectTypeToString;
	}
}
function buildPollObjectList(){
	var w = document.body.clientWidth;
	var h = document.body.clientHeight;
	//w = w*.5;
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
		                   if( this.fly(r).hasClass('x-grid3-row-selected')) 
		                	   selected = selected + 1;  
		                }  
		            }  
		            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
		              
		            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
		                 hd.removeClass('x-grid3-hd-checker-on');   
		            }  
		    }  
		});  
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [	
		//sm,
 		{header: I18N.cmPoll.pollObjectName, width: w/8,sortable: true, align: 'center', dataIndex: 'objectName'},
     	{header: I18N.cmPoll.pollObjectType, width: w/10, sortable: false, align: 'center', dataIndex: 'objectTypeToString', renderer: renderObjectType},	
     	{header: I18N.cmPoll.inTask, width: w/8, sortable: false, align: 'center', dataIndex: 'jobName', renderer:renderMonitorName},	
 		{header: I18N.COMMON.createTime, width: w/6, sortable: false, align: 'center', dataIndex: 'createTimeToString'},
 		{header: I18N.RECYLE.modifyTime, width: w/6, sortable: false, align: 'center', dataIndex: 'updateTimeToString'},
 		{header: "@COMMON.manu@", width: 120, sortable:false, align : 'center', renderer: operationRender} 
	];
	baseStore = new Ext.data.JsonStore({
	    url: ('/cmpoll/loadPollObjectList.tv'),
	    root: 'data',
	    //remoteSort: true,//是否支持后台排序 
	    fields: ['objectId','objectName','objectType','objectTypeToString','jobName','createTimeToString','updateTimeToString']
	});
	baseStore.setDefaultSort('objectId', 'ASC');
	
	var toolbar = [{
	       			text : I18N.COMMON.create,
	       			iconCls : 'bmenu_new',
	       			handler : click_CreatePollObject
	       		}, {
	       			text : I18N.COMMON.refresh,
	       			iconCls : 'bmenu_refresh',
	       			handler : refresh
	       		}];
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		id: 'pollObjectList', 
		border: false, 
		store: baseStore, 
		viewConfig : { forceFit: true },
		tbar : toolbar,
		cm: baseCm,
		loadMask : true, 
	});
	
	new Ext.Viewport({
	     layout: "border", items: [baseGrid]
	});
	baseStore.load();
}

Ext.onReady(function () {
	/*创建监控对象列表*/
	buildPollObjectList();
});
</script>
</head>
<body></body>
</Zeta:HTML>
