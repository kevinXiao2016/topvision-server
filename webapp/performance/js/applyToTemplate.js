var related_store = null;
var un_related_store = null;
var related_grid = null;
var un_related_grid = null;
var oltType = EntityType.getOltType();
var ccType = EntityType.getCcmtsType();
var cmtsType = EntityType.getCmtsType();
var ccmtsAndCmtsType = EntityType.getCCMTSAndCMTSType();
var ccWithOutAgentType = EntityType.getCcmtsWithoutAgentType();
var onuType = EntityType.getOnuType();

$(document).ready(function(){
	//构建页面顶部工具栏
	new Ext.Toolbar({
		renderTo : "toolbar",
		items : [ {
			text : '@Tip.back@',
			iconCls : 'bmenu_back',
			handler : function(){
				parent.closeFrame();
			}
		}]
	});
	//在单独安装CC模块的时候,子类型下拉菜单默认只显示CMTS选择
	if(cmcSupport && !eponSupport){
		//填充CMC子类型及CMTS下拉框
		$.each(allEntityTypes, function(index, subType){
			if(parentType == ccmtsAndCmtsType){
				if(subType.isSubType && (EntityType.isCcmtsWithAgentType(subType.typeId) || EntityType.isCmtsType(subType.typeId))){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}else if(parentType == ccType){
				if(subType.isSubType && EntityType.isCcmtsWithAgentType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}else if(parentType == cmtsType){
				if(subType.isSubType && EntityType.isCmtsType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}
		});
	}else{
		//填充CMC子类型及CMTS下拉框
		$.each(allEntityTypes, function(index, subType){
		    if(parentType == ccmtsAndCmtsType){
				if(subType.isSubType && EntityType.isCcmtsAndCmtsType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}else if(parentType == ccType){
				if(subType.isSubType && EntityType.isCcmtsType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}else if(parentType == cmtsType){
				if(subType.isSubType && EntityType.isCmtsType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}else if(parentType == onuType){
				if(subType.isSubType && EntityType.isOnuType(subType.typeId)){
			        $('#deviceType').append('<option value="'+subType.typeId +'">'+ subType.displayName +'</option>');
			    }
			}
		});
	}
	
	//根据模板类型决定显示什么查询条件
	if(parentType==oltType){
		$(".cmcSearch").hide();
	}else{
		$(".cmcSearch").show();
	}
	//构建store
	un_related_store = new Ext.data.JsonStore({
	    url: '/performance/perfThreshold/loadNoRelaTemplateEntityList.tv',
	    baseParams:{start:0,limit:pageSize,templateId:templateId, parentType:parentType},
	    root: 'data',
	    totalProperty: 'rowCountUn',
	    remoteSort: false,
	    fields: ['entityId','entityName','mac','entityTypeDisplayName','entityIp', 'typeId', 'templateId', 'templateName']
	});
	related_store = new Ext.data.JsonStore({
	    url: '/performance/perfThreshold/loadRelaTemplateEntityList.tv',
	    baseParams:{start:0,limit:pageSize,templateId:templateId},
	    root: 'data',
	    totalProperty: 'rowCountRe',
	    remoteSort: false,
	    fields: ['entityId','entityName','mac','entityTypeDisplayName','entityIp', 'typeId', 'templateId', 'templateName'],
	});
	//根据设备是CCMTS还是OLT，构建不同的cm
	var sm1 = new Ext.grid.CheckboxSelectionModel(); 
	var sm2 = new Ext.grid.CheckboxSelectionModel(); 
	var cm = null;
	if(parentType==oltType || parentType==onuType){
		cm = new Ext.grid.ColumnModel([
       	    sm1,
       	    {id:"entityId",header:'@Performance.entityName@',dataIndex:'entityId',hidden:true,align:'center',sortable:true},
       	    {id:"templateId",header:'@Performance.entityName@',dataIndex:'templateId',hidden:true,align:'center',sortable:true},
       	    {id:"entityName",header:'@Performance.entityName@',dataIndex:'entityName',align:'center',sortable:true},
       	    {id:"entityTypeDisplayName",header:'@Performance.entityType@',dataIndex:'entityTypeDisplayName',align:'center',sortable:true},
       	    {id:"entityIp",header:'@resources/COMMON.manageIp@',dataIndex:'entityIp',align:'center',sortable:true},
       	    {id:"templateName",header:'@Performance.relaTemplate@',dataIndex:'templateName',align:'center',sortable:true,renderer:templateNameRender}
       	]);
	}else if(parentType==ccType || parentType==cmtsType || parentType == ccmtsAndCmtsType){
		cm = new Ext.grid.ColumnModel([
      	    sm2,
      	    {id:"entityId",header:'@Performance.entityName@',dataIndex:'entityId',hidden:true,align:'center',sortable:true},
      	    {id:"typeId",header:'@Performance.entityName@',dataIndex:'typeId',hidden:true,align:'center',sortable:true},
      	    {id:"templateId",header:'@Performance.entityName@',dataIndex:'templateId',hidden:true,align:'center',sortable:true},
      	    {id:"entityName",header:'@Performance.entityName@',dataIndex:'entityName',align:'center',sortable:true},
      	    {id:"entityTypeDisplayName",header:'@Performance.entityType@',dataIndex:'entityTypeDisplayName',align:'center',sortable:true},
      	    {id:"mac",header:'MAC',dataIndex:'mac',align:'center',sortable:true},
      	    {id:"entityIp",header:'@resources/COMMON.manageIp@',dataIndex:'entityIp',align:'center',sortable:true,renderer:ipRender},
      	    {id:"templateName",header:'@Performance.relaTemplate@',dataIndex:'templateName',align:'center',sortable:true,renderer:templateNameRender}
      	]);
	}
	//构建grid
	un_related_grid = new Ext.grid.GridPanel({
		store:un_related_store,
        animCollapse:false,
        trackMouseOver:true,
        border: true,
        renderTo: "deviceUnRelate",
        tbar: [	{xtype: 'tbtext',text: String.format("<b>@Performance.noRelaTemplate@</b>&nbsp;&nbsp;",templateName)},
	       	    {text: '@Performance.enabledRela@', iconCls: 'bmenu_rela',handler: relaTemplateClick},
	       	    {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh',handler: unRelatedRefreshClick}
	    ],
        cm:cm,
		sm:sm1,
		bbar: new Ext.PagingToolbar({
	        id: 'extPagingBar1', 
	        pageSize: pageSize,
	        store:un_related_store,
	        displayInfo: true, 
	        items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-']
	    }),
		bodyCssClass: 'normalTable',
      	viewConfig:{
      		forceFit: true
      	}
    });
	related_grid = new Ext.grid.GridPanel({
	    store:related_store,
	    animCollapse:false,
	    trackMouseOver:true,
	    border: true,
	    renderTo: "deviceRelated",
	    tbar: [	{xtype: 'tbtext',text:String.format("<b>@Performance.relaTemplateList@</b>&nbsp;&nbsp;",templateName)},
	       	    {text: '@Performance.disRela@', iconCls: 'bmenu_remove',handler: removeClick},
	       	    {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh',handler: relatedRefreshClick}
	    ],
	    cm:cm,
		sm:sm2,
		bbar: new Ext.PagingToolbar({
	        id: 'extPagingBar2', 
	        pageSize: pageSize,
	        store:related_store,
	        displayInfo: true, 
	        items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-']
	    }),
		bodyCssClass: 'normalTable',
      	viewConfig:{
      		forceFit: true
      	}
	});
	un_related_store.load({params: {start:0,limit:pageSize}});
	related_store.load({params: {start:0,limit:pageSize}});
	setGridSize();
    $(window).resize(function(){
    	setGridSize();
    });
});

function templateNameRender(value, p, record){
	if(value==""){
		return "@Performance.unRelaTemplate@";
	}else{
		return value;
	}
}

function ipRender(value, p, record){
	if(record.data.typeId==ccWithOutAgentType){
		return value + "(OLT)";
	}else{
		return value;
	}
}

function setGridSize(){
	var h = ($(window).height() - $("#toolbar").height()- $("#serach").outerHeight(true) - 40)/2;
	if(h<200) h = 200;
	var w = $(document.body).width() -40;
	un_related_grid.setSize(w, h);
	related_grid.setSize(w, h);
}

function unRelatedRefreshClick() {
	un_related_store.reload();
}

function relatedRefreshClick() {
	related_store.reload();
}

function relaTemplateClick(){
	var sm = un_related_grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var selectIds = sm.getSelections();
		for(var i = 0;i<selectIds.length;i++){
			entityIdArray[entityIdArray.length]  = selectIds[i].data.entityId;
		}
		window.parent.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmRelaTemplate@', function(type) {
  			if (type == 'no'){return;}
  			window.top.showWaitingDlg('@COMMON.wait@', '@Performance.isRelaTemplate@', 'ext-mb-waiting');
  			$.ajax({
 		       url: '/performance/perfThreshold/saveEntityRelaTemplate.tv',
 		      data: {entityIds:entityIdArray, templateId:templateId, templateType:parentType},
 		       type: 'POST',
 		       success: function() {
 		    	   window.parent.parent.closeWaitingDlg();
	           	   //window.parent.parent.showMessageDlg('@COMMON.tip@', '@Performance.relaTemplateSuc@')
 		    	   unRelatedRefreshClick();
 		    	   relatedRefreshClick();
 		       }, error: function() {
 		        	window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.relaTemplateFail@')
 		    }, cache: false
  			});
  		})
	}else{
		window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}

function removeClick(){
	var sm = related_grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var entityIdArray = new Array();
		var selectIds = sm.getSelections();
		for(var i = 0;i<selectIds.length;i++){
			entityIdArray[entityIdArray.length]  = selectIds[i].data.entityId;
		}
		window.parent.parent.showConfirmDlg('@COMMON.tip@','@Performance.confirmDisRela@', function(type) {
  			if (type == 'no'){return;}
  			window.top.showWaitingDlg('@COMMON.wait@', '@Performance.disRelaTemplate@', 'ext-mb-waiting');
  			$.ajax({
 		       url: '/performance/perfThreshold/removeEntityRelaTemplate.tv?entityIds='+ entityIdArray,
 		       type: 'POST',
 		       success: function() {
 		    	   window.parent.parent.closeWaitingDlg();
	           	   //window.parent.parent.showMessageDlg('@COMMON.tip@', '@Performance.disRelaSuc@')
 		    	  unRelatedRefreshClick();
 		    	  relatedRefreshClick();
 		       }, error: function() {
 		        	window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.disRelaFail@')
 		    }, cache: false
  			});
  		})
	}else{
		window.parent.parent.showMessageDlg('@COMMON.tip@','@Performance.selectDevice@')
	}
}

function searchForDevice(){
	var entityName = $("#entityName").val();
	var entityIp = $("#ipAddress").val();
	var mac ;
	var typeId;
	if(parentType == oltType){
		related_store.setBaseParam("entityName",entityName);
		related_store.setBaseParam("entityIp",entityIp);
		related_store.load();
		
		un_related_store.setBaseParam("entityName",entityName);
		un_related_store.setBaseParam("entityIp",entityIp);
		un_related_store.load();
	}else{
		mac = $("#mac").val();
		typeId = $("#deviceType").val();
		related_store.setBaseParam("entityIp",entityIp);
		related_store.setBaseParam("mac",mac);
		related_store.setBaseParam("typeId",typeId);
		related_store.setBaseParam("entityName",entityName);
		related_store.load();
		
		un_related_store.setBaseParam("entityIp",entityIp);
		un_related_store.setBaseParam("mac",mac);
		un_related_store.setBaseParam("typeId",typeId);
		un_related_store.setBaseParam("entityName",entityName);
		un_related_store.load();
	}
}