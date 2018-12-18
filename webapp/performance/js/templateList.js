var contextMenu = null;
var grid = null;
var selectionModel;
var store = null;
function sendRequest(url, method, param, sn, fn) {
	Ext.Ajax.request({url: url+'?_='+Math.random(), failure: fn, success: sn,timeout:1800000,  params: param});
}

function onRefreshClick() {
	store.reload();
}

function doRefresh() {
	onRefreshClick();
}
function startTimer() {
	
}
function doOnUnload() {
	
}
function gourpRender(value, p, record){
	if(value==1){
		return "OLT";
	}else{
		return "CCMTS";
	}
}

function renderDefault(value, p, record){
	if (value) {
		return String.format('<img alt="{0}" src="../images/correct.png" border=0 align=absmiddle>',
				I18N.COMMON.yes);	
	} else {
		return String.format('<img alt="{0}" src="../images/wrong.png" border=0 align=absmiddle>',
				I18N.COMMON.no);	
	}
}

function management(value, p, record){
	if (value) {
		return "<a href='javascript:;' onclick='applyTemplate()' >@Performance.detail@</a> / <a href='javascript:;' onclick='editTemplate()' >@tip.modify@</a>";
	} else {
		return "<a href='javascript:;' onclick='applyTemplate()' >@Performance.detail@</a> / <a href='javascript:;' onclick='editTemplate()' >@tip.modify@</a> / <a href='javascript:;' onclick='deleteTemplate()' >@tip.delete@</a>";
	}
}


/**
 * 新建阈值模板
 */
function createTemplateClick() {
	window.top.createDialog('addTemplate', I18N.Performance.createTemplate, 800, 500, '/performance/showCreateTemplate.tv', null, true, true); 
}

function applyTemplate(){
	var record = grid.getSelectionModel().getSelected();
	var tempId = record.data.templateId;
	var templateName = record.data.templateName;
	window.top.createDialog('applyTemplate', String.format(I18N.Performance.applayTemplate,templateName), 800, 600, '/performance/showApplyTemplateJsp.tv?templateId='+tempId, null, true, true); 
}

function editTemplate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		var templateId = record.data.templateId;
		window.top.createDialog('editTemplate',  I18N.Performance.editTemplate, 600,480, '/performance/modifyTemplate.tv?templateId='+templateId, null, true, true); 
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip,I18N.Performance.noSelectTemplate);
	}
}

function deleteTemplate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var record = sm.getSelected();
		var templateId = record.data.templateId;
		var isDefault = record.data.isDefaultTemplate;
		if(isDefault == 1){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.Performance.defaultTemplateNoDel);
			return ;
		}
		window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.Performance.confirmDelTemplate, function(type) {
		if (type == 'no') {
	           return;
	    }
		$.ajax({
		       url: '/performance/deletePerfTemplate.tv?templateId='+templateId,
		       type: 'POST',
		       success: function(text) {
			       if(text == 'success'){
		           	   window.parent.showMessageDlg(I18N.COMMON.tip, I18N.Performance.delTemplateSuc)
		           	   onRefreshClick();
			       }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip,I18N.Performance.delTemplateFail)
				   }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip,I18N.Performance.delTemplateFail)
		    }, cache: false
		});
		})
	}else{
		window.parent.showMessageDlg(I18N.COMMON.tip,I18N.Performance.noSelectTemplate);
	}
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function() {
	var reader = new Ext.data.JsonReader({
	    root: "data",
        fields: [
			{name: 'templateId'},
		    {name: 'templateName'},
		    {name: 'templateType'},
		    {name: 'isDefaultTemplate'},
		    {name: 'createTimeString'},
		    {name: 'modifyTimeString'}
        ]
	});

	store = new Ext.data.GroupingStore({
       	url: '/performance/loadTemplateList.tv',
       	reader: reader,
		remoteGroup: false,
		remoteSort: true,
		groupField: 'templateType',
		groupOnSort: false
    });
   store.setDefaultSort('templateType', 'DESC');
   var groupTpl = I18N.Performance.templateType +':&nbsp;&nbsp;{group}';

   var toolbar = [
	    {text: I18N.COMMON.create, iconCls: 'bmenu_new',cls:'mL10', handler:createTemplateClick},'-',
	    {text: I18N.COMMON.edit, iconCls: 'bmenu_edit', handler:editTemplate},'-',
	    {text: I18N.COMMON.remove, iconCls: 'bmenu_delete', handler:deleteTemplate},'-',
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}];

   var columnModels = [
	    {id:'templateType', width:70, renderer:gourpRender,sortable:true, groupable:true, menuDisabled:true,dataIndex:'templateType',hidden:true},
		{header: '<div style="text-align:center">' + I18N.Performance.templateName + '</div>',align:'left', width:70, sortable:true, dataIndex:'templateName'},
		{header: I18N.Performance.isDefaultTemplate,align:'center', width:70, dataIndex:'isDefaultTemplate',renderer:renderDefault},
	    {header: I18N.COMMON.createTime, align:'center',width:80, sortable:true,dataIndex: 'createTimeString'},
	    {header: I18N.COMMON.modifyTime, align:'center', width:150, sortable:false, dataIndex:'modifyTimeString'},
	    {header: I18N.COMMON.opration, align:'center', width:80, sortable:false,groupable: false, dataIndex: 'isDefaultTemplate',renderer:management}
	];
    grid = new Ext.grid.GridPanel({
		store: store,
        animCollapse: animCollapse,
        trackMouseOver: trackMouseOver,
        border: false,
        columns: columnModels,
        bodyCssClass: 'normalTable',
        view: new Ext.grid.GroupingView({
            forceFit: true, hideGroupedColumn: false,enableNoGroups: true,groupTextTpl: groupTpl
        }),
        tbar: toolbar,
		renderTo: document.body
    });
    selectionModel = grid.getSelectionModel();

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
		var record = grid.getStore().getAt(rowIndex);  // Get the Record
		this.getSelectionModel().selectRow(rowIndex);
    });

    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);
    });
    new Ext.Viewport({layout: 'fit', items: [grid]});
	store.load({params:{start: 0, limit: pageSize}});
});

