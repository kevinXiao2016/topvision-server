/**
 * author: victor
 *
 * Repository management.
 */
var store = null;
Ext.onReady(function(){
    Ext.BLANK_IMAGE_URL = '../images/s.gif';
    var xg = Ext.grid;

    store = new Ext.data.JsonStore({
        root: 'data', totalProperty: 'rowCount',
        remoteSort: true,
        fields: ['key1', 'key2', 'key3', 'key4', 'key5', 'content']
    });
    store.setDefaultSort('name', 'asc');

    var cm = new Ext.grid.ColumnModel([
        {header:'key1', dataIndex: 'key1', width: 100},
        {header:'key2', dataIndex: 'key2', width: 100},
        {header:'key3', dataIndex: 'key3', width: 150}, 
        {header:'key4', dataIndex: 'key4', width: 150},       
        {header:'key5', dataIndex: 'key5', width: 50},
        {header:'content', dataIndex:'content', width: 150}]);
    cm.defaultSortable = true;

    var grid = new xg.GridPanel({border:false, region:'center',
        store:store, cm:cm, trackMouseOver:false, 
        viewConfig: {forceFit:true, enableRowBody:true, showPreview:false},

        tbar:[{text:I18N.COMMON.create, iconCls:'bmenu_new', handler:onNewKnowledgeClick}, 
            {text:I18N.SYSTEM.deleteAction, iconCls:'bmenu_delete', handler:onDeleteClick}, '-',
            {text:I18N.SYSTEM.find, iconCls: 'bmenu_find', handler: onFindClick}],
        bbar:buildPagingToolBar(),
        renderTo: document.body
    });    

    var deleteMenu = new Ext.menu.Item({text:I18N.SYSTEM.deleteAction, iconCls:'bmenu_delete', handler:onDeleteClick});
    var propertyMenu = new Ext.menu.Item({text:I18N.SYSTEM.property, handler:onPropertyClick});
    var mainMenu = new Ext.menu.Menu({id: 'mainMenu', items:[
        {text:I18N.SYSTEM.selectAll, handler:onSelectAllClick}, '-',
        deleteMenu, '-',
        propertyMenu           
    ]});

    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
        var sm = grid.getSelectionModel();
        if (sm != null && !sm.isSelected(rowIndex)) {
            sm.selectRow(rowIndex);
        }
        deleteMenu.enable();
        propertyMenu.enable();
        mainMenu.showAt(e.getPoint());
    });  
    grid.on('contextmenu', function(e) {
        deleteMenu.disable();
        propertyMenu.disable(); 
        mainMenu.showAt(e.getPoint());
    });
    grid.on('keydown', function(e) {
        if (e.ctrlKey && e.getCharCode() == KeyEvent.VK_A) {
            onSelectAllClick();
        }
    });    
    
    new Ext.Viewport({layout:'fit', items:[grid]});

    //store.load({params:{start:0, limit:pageSize}});
});

function buildPagingToolBar() {
    var states = [
        ['page25', '25', '25'],
        ['page50', '50', '50'],
        ['page75', '75', '75'],
        ['page100', '100', '100']
    ];
    var pageStore = new Ext.data.SimpleStore({
        fields: ['abbr', 'size'],
        data: states
    });
    var combo = new Ext.form.ComboBox({
        store: pageStore,
        displayField:'size',
        typeAhead: true,
        mode:'local',
        editable:false,
        triggerAction:'all',
        selectOnFocus:true,
        width:60
    });
    combo.setValue(''+pageSize);
    combo.on('select', function(combo, record, index) {
        if(record.data.size == pageSize) return;
        Ext.Ajax.request({url: '../system/setPageSize.tv',
            success: function() {
                pagingToolbar.pageSize = pageSize = parseInt(record.data.size);
                pagingToolbar.doLoad(0);
            },
            failure: function(){},
            params: {pageSize:record.data.size}
        });
    });
    pagingToolbar = new Ext.PagingToolbar({pageSize:pageSize, store:store, displayInfo:true,
        items:['-', I18N.COMMON.perPage, combo, I18N.COMMON.tiao]});
    return pagingToolbar;
}

function onNewKnowledgeClick() {
}
function onSelectAllClick() {
    var sm = grid.getSelectionModel();
    if (sm != null) {
        sm.selectAll();
    }   
}
function onDeleteClick() {
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var selections = sm.getSelections();
        var entityIds = [];
        for (var i = 0; i < selections.length; i++) {
            entityIds[i] = selections[i].data.entityId;
        }
        window.top.showConfirmDlg(I18N.SYSTEM.tip, I18N.SYSTEM.confirmDeleteRow, function(type) {
            if (type == 'no') {return;}
            Ext.Ajax.request({url: '../system/deleteUser.tv',
               success: function() {onRefreshClick();},
               failure: function(){window.top.showMessageDlg(I18N.SYSTEM.error, I18N.SYSTEM.deleteFailure, 'error')},
               params: {entityIds:entityIds}
            });         
        });
    }
}
function onFindClick() {
}
function onPropertyClick() {
    var sm = grid.getSelectionModel();
    if (sm != null && sm.hasSelection()) {
        var r = sm.getSelected();
        window.top.showProperty('system/userProperty.tv?userId=' + r.data.userId);
    }   
}
document.onselectstart = function() {return false;}