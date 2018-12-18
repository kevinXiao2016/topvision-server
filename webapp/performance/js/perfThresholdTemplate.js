var contextMenu = null;
var grid = null;
var selectionModel;
var store = null;

/********************************************************
 * 一些工具函数，包括resize()方法及grid各列的renderer方法****
 *******************************************************/

//返回frame的父页面
function closeFrame(){
	//刷新grid
	store.reload({
		callback : function(){
			notSupportCMTSLisence();
			disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
	});
	$("#content").css("display","block");
	$("#frameOuter").css("display","none");
	$("#frameInner").attr("src","");
}

function isDefaultRender(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="/images/correct.png" border=0 align=absmiddle>',
				'@resources/COMMON.yes@');
	} else {
		return "";	
	}
}

function operationRender(value, p, record){
	if (value && (record.data.templateType == record.data.parentType)) {
		return "<a href='javascript:;' onclick='applyTemplate()' >@COMMON.apply@</a> /" + 
				" <a href='javascript:;' onclick='editTemplate()' >@COMMON.edit@</a>";
	} else {
		return "<a href='javascript:;' onclick='applyTemplate()' >@COMMON.apply@</a> /" + 
				" <a href='javascript:;' onclick='editTemplate()' >@COMMON.edit@</a> /" + 
				" <a href='javascript:;' onclick='deleteTemplate()' >@COMMON.del@</a>";
	}
}

function setGridSize(){
	var h = $(window).height() - $("#toolbar").height() - 40;
	if(h<300) h = 300;
	var w = $(document.body).width();
	grid.setSize(w, h);
}

/********************
 **** 一些逻辑方法*****
 ********************/
//新建模板
function createTemplateClick() {
	$("#content").hide();
	$("#frameOuter").show();
	$("#frameInner").attr("src","/performance/perfThreshold/showCreateTemplate.tv?m="+Math.random()+'&originalFrame=frameperfTemplate');
}

//删除对应的阈值模板
function deleteTemplate(){
	var templateIds = new Array();
	var hasDefault = false;
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selectIds = sm.getSelections();
		for(var i = 0;i<selectIds.length;i++){
			if(selectIds[i].data.isDefaultTemplate && (selectIds[i].data.templateType == selectIds[i].data.parentType)){
				hasDefault = true;
			}else{
				templateIds[templateIds.length]  = selectIds[i].data.templateId;
			}
		}
		if(templateIds.length == 0){
			window.parent.showMessageDlg('@COMMON.tip@', '@tip.pleSlcNoDft@')
			return ;
		}
		if(hasDefault){
			window.parent.showMessageDlg('@COMMON.tip@', '@tip.cntDltDftTmp@')
			return;
		}
		window.parent.showConfirmDlg('@COMMON.tip@', '@Performance.confirmDelTemplate@', function(type) {
			if (type == 'no') {
		           return;
		    }
			$.ajax({
			       url: '/performance/perfThreshold/deletePerfTemplate.tv?templateIds='+templateIds,
			       type: 'POST',
			       success: function() {
		           	   top.afterSaveOrDelete({
		           		   title:'@COMMON.tip@',
		           		   html:'@Performance.delTemplateSuc@',
		           		   okBtnTxt:'@COMMON.ok@'		           		   
		           	   })
		           	   store.reload({
		           		   callback : function(){
		           			   disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		           		   }
		           	   });
			        }, error: function() {
			        	window.parent.showMessageDlg('@COMMON.tip@','@Performance.delTemplateFail@')
			    }, cache: false
			});
		})
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.noSelectTemplate@');
	}
}

//打开将该阈值模板应用到设备页面
function applyTemplate(){
	var sm = grid.getSelectionModel();
	if(sm != null && sm.hasSelection()){
		var record = sm.getSelected();
		var tempId = record.data.templateId;
		var templateName = record.data.templateName;
		$("#content").hide();
		$("#frameOuter").show();
		$("#frameInner").attr("src",'/performance/perfThreshold/showApplyTemplateJsp.tv?templateId='+tempId+"&m="+Math.random());
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.noSelectTemplate@');
	}
}

//打开编辑对应的阈值模板页面
function editTemplate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		var templateId = record.data.templateId;
		$("#content").hide();
		$("#frameOuter").show();
		$("#frameInner").attr("src",'/performance/perfThreshold/showModifyTemplate.tv?templateId='+templateId+"&m="+Math.random()+'&originalFrame=frameperfTemplate');
	}else{
		window.parent.showMessageDlg('@COMMON.tip@','@Performance.noSelectTemplate@');
	}
}

function typeRender(value, meta, record){
	if(EntityType.getOltType() == value){
		return "OLT";
	}else if(EntityType.getCCMTSAndCMTSType() == value){
		return "CMTS";
	}else if(EntityType.getOnuType() == value){
		return "ONU";
	}
	else{
		return "Other";
	}
}
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn("batchDelBtn", false);
    }else{
        disabledBtn("batchDelBtn", true);
    }
};
function disabledBtn(id, disabled){
    Ext.getCmp(id).setDisabled(disabled);
};

//构建模板列表
$(document).ready(function() {
	var reader = new Ext.data.JsonReader({
	    root: "data",
        fields: [
			{name: 'templateId'},
		    {name: 'templateName'},
		    {name: 'templateType'},
		    {name: 'typeDisplayName'},
		    {name: 'parentType'},
		    {name: 'parentDisplayName'},
		    {name: 'createUser'},
		    {name: 'isDefaultTemplate'},
		    {name: 'relaDeviceNum'},
		    {name: 'createTimeString'},
		    {name: 'modifyTimeString'}
        ]
	});

	store = new Ext.data.GroupingStore({
       	url: '/performance/perfThreshold/loadTemplateList.tv',
       	reader: reader,
		remoteGroup: false,
		remoteSort: false,
		groupField: 'parentType',
		groupOnSort: false
    });
   store.setDefaultSort('templateType', 'ASC');
   
   var groupTpl = I18N.Performance.templateType +':&nbsp;&nbsp;{group}';

   var toolbar = [
        {xtype: 'tbspacer', width:5},
	    {text: '@Performance.createTemplate@', iconCls: 'bmenu_new', handler:createTemplateClick}, '-',
	    {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', 
	    	handler: function(){
	    		store.reload({
	    			callback : function(){
	    				notSupportCMTSLisence();
	    				disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	    			}
	    		});
	    	}
	    }, '-',
	    {id: 'batchDelBtn', text: '@Performance.batchDeleteTemp@', iconCls: 'bmenu_delete', handler:deleteTemplate, disabled: true}
		];

   var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	}); 
   var columnModels = [
        sm,
	    {header: '@Performance.templateType@', groupable:true, menuDisabled:true,dataIndex:'parentType',hidden:true, renderer: typeRender},
		{header: "<div style='text-align:center'>@Performance.templateName@</div>",align:'left', width:200, sortable:false, dataIndex:'templateName'},
		{header: '@Performance.isDefaultTemplate@',align:'center', dataIndex:'isDefaultTemplate',renderer:isDefaultRender},
		{header: '@Performance.templateType@',align:'center', dataIndex:'typeDisplayName'},
		{header: '@tip.creator@',align:'center', dataIndex:'createUser'},
		{header: '@tip.relateNumber@',align:'center', dataIndex:'relaDeviceNum'},
	    {header: '@resources/COMMON.createTime@', align:'center', sortable:false,dataIndex: 'createTimeString'},
	    {header: '@resources/COMMON.modifyTime@', align:'center', sortable:false, dataIndex:'modifyTimeString'},
	    {header: '@COMMON.opration@', align:'center', width:150, fixed :true, dataIndex: 'isDefaultTemplate',renderer:operationRender}
	];
    grid = new Ext.grid.GridPanel({
		store: store,
        animCollapse: animCollapse,
        trackMouseOver: trackMouseOver,
        border: false,
        columns: columnModels,
        sm:sm,
        loadMask : true,
        bodyCssClass: 'normalTable',
        view: new Ext.grid.GroupingView({
            forceFit: true, 
            hideGroupedColumn: false,
            enableNoGroups: true,
            groupTextTpl: groupTpl
        }),
        tbar: toolbar,
		renderTo: 'content'
    });
    selectionModel = grid.getSelectionModel();

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
		var record = grid.getStore().getAt(rowIndex);  // Get the Record
		this.getSelectionModel().selectRow(rowIndex);
    });

    setGridSize();
    
    $(window).resize(function(){
    	setGridSize();
    });
    
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);
    });
	store.load({
		callback: function(){
			notSupportCMTSLisence();
		}
	});
});
//[EMS-14510]lisence不支持CMTS板块；
function notSupportCMTSLisence(){
	if(!cmcSupport){
		store.each(function(v, i){
			if(EntityType.getCCMTSAndCMTSType() == v.data.parentType){
				store.remove(v)
			}
		})
	}
}