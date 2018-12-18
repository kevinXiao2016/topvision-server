<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="../css/gui.css"/>
<link rel="stylesheet" type="text/css" href="../css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="../js/ext/ext-base.js"></script>
<script type="text/javascript" src="../js/ext/ext-all.js"></script>
<script type="text/javascript" src="../js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
// for tab changed start
function tabActivate() {
	window.top.setStatusBarInfo('', '');
	doRefresh();
}
function tabDeactivate() {
}
function tabRemoved() {
}
function tabShown() {
	window.top.setStatusBarInfo('', '');
}
// for tab changed end

function readerOperator(operator){
    switch(operator) {
    case 0:return ">";
    case 1:return ">=";
    case 2:return "=";
    case 3:return "!=";
    case 4:return "<";
    case 5:return "<=";
    case 6:return I18N.ALERT.contain;
    case 7:return I18N.ALERT.notContain;
    case 8:return I18N.ALERT.equal;
    case 9:return I18N.ALERT.unEqual;
    case 10:return I18N.ALERT.beginWith;
    case 11:return I18N.ALERT.endWith;
    default:return I18N.ALERT.unknow;
    }
}
function readerLevel(level){
    switch(level) {
    case 6:return I18N.WorkBench.emergencyAlarm;
    case 5:return I18N.WorkBench.seriousAlarm;
    case 4:return I18N.WorkBench.minorAlarm;
    case 3:return I18N.WorkBench.minorAlarm;
    case 2:return I18N.WorkBench.generalAlarm;
    case 1:return I18N.WorkBench.message;
    default:return I18N.ALERT.clearAlert;
    }
}
function renderOperator1(value, p, record){
    return (readerOperator(record.data.alertOperator1) + record.data.alertThreshold1);
}
function renderOperator2(value, p, record){
    return (readerOperator(record.data.alertOperator2) + record.data.alertThreshold2);
}
function renderLevel1(value, p, record){
    return readerLevel(record.data.alertLevel1);
}
function renderLevel2(value, p, record){
    return readerLevel(record.data.alertLevel2);
}
function onNewThresholdClick() {
	window.parent.createDialog("modalDlg", I18N.ALERT.addThreshold, 450, 430, "fault/newThreshold.jsp", null, true, true);
}
function onDeleteClick(){
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var thresholdIds = [];
        for (var i = 0; i < selections.length; i++) {
            thresholdIds[i] = selections[i].data.thresholdId;
        }
        window.top.showConfirmDlg(I18N.COMMON.tip, I18N.ALERT.ALERT.confirmDeleteThreshold, function(type) {
            if (type == 'no') {return;}
            Ext.Ajax.request({url: '../threshold/deleteThreshold.tv',
                success: function() {
                    onRefreshClick();
                },
                failure: function() {window.parent.showErrorDlg();},
                params: {thresholdIds: thresholdIds}
            });
        });
    } else {
        window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.pleaseChooseThresholdToDelete);
    }
}
function doRefresh() {
	onRefreshClick();
}
function onRefreshClick(){
    store.load();
}
function onPropertyClick() {
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selected = sm.getSelected();
		window.top.createDialog("modalDlg", I18N.ALERT.modifyThreshold, 450, 430,
			"threshold/showThresholdJsp.tv?thresholdId=" + selected.data.thresholdId, null, true, true);			
	}
}
function selectAll() {
    var sm = grid.getSelectionModel();
    if (sm != null) {
        sm.selectAll();
    }
}

var grid;
var store;
var contextMenu;
Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	var columnModels = [
	        {header: I18N.COMMON.name, width: 120, sortable:true, dataIndex:'name'},
	        {header: I18N.ALERT.checkCondition, width: 80, sortable:true, dataIndex: 'alertOperator1',
	        	renderer: renderOperator1},
	        {header: I18N.ALERT.overTimes, width: 80, sortable:true, dataIndex: 'alertCount1'},
	        {header: I18N.ALERT.alertLevel, width: 80, sortable:true, dataIndex: 'alertLevel1', renderer: renderLevel1},
			{header: I18N.COMMON.description, width: 200, sortable: false, dataIndex: 'description'}	        
	    ];
	var myToolbar = [
	    {text: I18N.COMMON.create, handler: onNewThresholdClick, iconCls:'bmenu_new'}, '-',
	    {text: I18N.COMMON.deleteAction, iconCls: 'bmenu_delete', handler:onDeleteClick}, '-',
	    {text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}];
    contextMenu = new Ext.menu.Menu({id: 'mainMenu', minWidth: 130, items:[
    	{text: I18N.COMMON.deleteAction, iconCls:'bmenu_delete', handler: onDeleteClick}, '-',
        {text: I18N.COMMON.refresh, iconCls:'bmenu_refresh', handler: onRefreshClick}, '-',
        {text: I18N.MAIN.property, handler: onPropertyClick}
    ]});
	var Threshold = Ext.data.Record.create([{
			name: 'thresholdId'},
	        {name: 'name'},
            {name: 'description'},
            {name: 'alertLevel1'},
            {name: 'alertOperator1'},
            {name: 'alertThreshold1'},
            {name: 'alertCount1'},
            {name: 'alertLevel2'},
            {name: 'alertOperator2'},
            {name: 'alertThreshold2'},
            {name: 'alertCount2'}]);
	var myReader = new Ext.data.JsonReader({id: "name"}, Threshold);
    store = new Ext.data.GroupingStore({
        id: 'name', reader: myReader,
        url: '../threshold/loadThreshold.tv'
    });
    var cm = new Ext.grid.ColumnModel(columnModels);
    grid = new Ext.grid.GridPanel({
        store: store, cm: cm,
        trackMouseOver: trackMouseOver, border: false,
        viewConfig: {forceFit: true, enableRowBody: true, showPreview: false},
        tbar: myToolbar,
        renderTo: document.body
    });
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault();
        var sm = grid.getSelectionModel();
        if (sm != null && !sm.isSelected(rowIndex)) {
            sm.selectRow(rowIndex);
        }
        contextMenu.showAt(e.getPoint());
    });
    grid.on('rowdblclick', function(grid, rowIndex, e) {
   		var record = grid.getStore().getAt(rowIndex);
		window.top.createDialog("modalDlg", I18N.ALERT.modifyThreshold, 450, 430,
			"threshold/showThresholdJsp.tv?thresholdId=" + record.data.thresholdId, null, true, true);	   		
    });    
    new Ext.Viewport({layout: 'fit', items: [grid]});
    store.load();
    tabShown();
}); 
</script>
</head><body class=CONTENT_WND>
</body></html>