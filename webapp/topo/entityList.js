var grid;
var bbar;
var store;
var queryData = {};
$(function(){
	
	//device type multi select
	var DeviceType = Ext.data.Record.create([
         {name: 'id', type: 'int'},
         {name: 'name', type: 'string'}
	]);
	var typeList = [];
	var deviceTypeStore = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy(typeList),
		reader: new Ext.data.ArrayReader({}, DeviceType)
	});
	var deviceTypeCombo = new Ext.ux.form.LovCombo({
        width: 196,
        id:'deviceTypeSelect',
        hideOnSelect : true,
        editable : false,
        renderTo:"deviceType_div",  
        store : deviceTypeStore,
        valueField: 'id',
		displayField: 'name',
        triggerAction : 'all',  
        mode:'local',
        emptyText : "@COMMON.pleaseSelect@",
        showSelectAll : true,
        beforeBlur : function(){}
	});
	//获取所有的子设备类型
	$.get('/entity/loadSubEntityType.tv', {
		type: 1
	},function(map){
		var list = map.entityTypes;
		$.each(list, function(index, entityType){
			typeList.append([entityType.typeId, entityType.displayName]);
		})
		deviceTypeStore.load();
		deviceTypeCombo.selectAll();
	})
	
	var regionTree = $('#region_tree').dropdowntree({
		width: 200
	}).data('nm3k.dropdowntree');
	regionTree.checkAllNodes();
	
    var sm = new Ext.grid.CheckboxSelectionModel();
	var columnModels = [
		{id:'name', header: "<div class='txtCenter'>@COMMON.alias@</div>",align:'left', width:100, sortable:true, groupable:false, dataIndex:'name', renderer: renderName},
	    {header: "<div class='textCenter'>@resources/COMMON.uplinkDevice@</div>", align:'center',width:60, sortable:true, groupable:false, dataIndex: 'uplinkDevice'},
	    {header: "@COMMON.entityType@", width:60, sortable:true, groupable: false, dataIndex: 'typeName'},
	    //{header: "@COMMON.upperDevice@", width:70, sortable:true, groupable: false, dataIndex: 'sysName'},
	    {header: "@COMMON.upstreamDeviceRegion@", width:70, sortable:true, groupable: false, dataIndex: 'sysDescr'},
        {header: "@COMMON.folder@", width:150, sortable:true, groupable: false, dataIndex: 'location'},
        {header: "@COMMON.RegionNum@", width:40, sortable:true, groupable: false, dataIndex: 'topoInfo'},
        {header: "@COMMON.createTime@", width:80, sortable:true, menuDisabled:true,groupable: false, dataIndex: 'createTime'},
        {header: "<div class='txtCenter'>@COMMON.manu@</div>", width:130, dataIndex: 'lastRefreshTime',renderer : menuRender, fixed:true}
	];
	var cmConfig = CustomColumnModel.init(saveColumnId,columnModels,{sm:sm}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'name', direction: 'ASC'};
		
	store = new Ext.data.JsonStore({
       	url: '/entity/loadEntityList.tv',
       	totalProperty: "rowCount",
	    idProperty: "entityId",
	    root: "data",
		remoteSort: true,
		baseParams: {limit: pageSize},
		fields:['entityId', 'parentId','uplinkDevice','name', 'ip', 'location', 'typeName', 'sysName', 'sysDescr', 'createTime', 'topoInfo', 'typeId']
    });
   store.setDefaultSort(sortInfo.field, sortInfo.direction);
   store.load({params:{start: 0, limit: pageSize}});
   store.on('beforeload', function() {$.extend(store.baseParams, queryData)});
   
   var tbar = new Ext.Toolbar({
	   items: [
	        {text: '@COMMON.batchEditFolder@', id: 'batch_edit_folder', iconCls: 'bmenu_edit', handler: batchEditFolder}, 
	        '-', 
	        {text: '@COMMON.batchInsertFolder@', id: 'batch_insert_folder', iconCls: 'bmenu_new', handler: batchInsertFolder}, 
	        '-', 
	        {text: '@COMMON.batchRemoveFolder@', id: 'batch_remove_folder', iconCls: 'bmenu_delete', handler: batchRemoveFolder} 
	  ]
	});
   
   bbar = new Ext.PagingToolbar({
    id: 'extPagingBar',
    pageSize: pageSize,
    store: store,
    displayInfo: true,
    items: ["-", String.format("@ccm/CCMTS.CmList.perPageShow@", pageSize), '-']
  });
   
   grid = new Ext.grid.EditorGridPanel({
	   cls: 'normalTable',
	   renderTo: 'grid-container',
	   border: true,
	   totalProperty: 'rowCount',
	   store: store,
	   sm: sm,
	   cm: cm,
	   tbar: tbar,
	   loadMask: true,
	   bbar: bbar,
	   margins: '0px 22px 0px 5px',
	   autoScroll: false,
	   enableColumnMove : true,
       listeners : {
       	columnresize: function(){
  	    	    CustomColumnModel.saveCustom(saveColumnId, cm.columns);
  	        },
  	     	sortchange : function(grid,sortInfo){
				CustomColumnModel.saveSortInfo(saveColumnId, sortInfo);
			}
       },
	   viewConfig: {
		   forceFit: true
	   }
   });
   
   $('#query_button').bind('click', function(){
	   //组装查询条件
	   queryData.alias = $('#alias').val();
	   queryData.ip = $('#ip').val();
	   queryData.entityType = Ext.getCmp("deviceTypeSelect").getCheckedValue();
	   queryData.upperName = $('#upperName').val();
	   queryData.folderIds = regionTree.getSelectedIds();
	   
	   //校验
	   if(!validate(queryData)) return;
	   
	   //进行查询
	   store.load({
		   params: queryData 
	   });
	   
   });
   
   function validate(queryData){
	   //校验别名
	   //校验IP
	   //校验设备类型
	   //校验上级设备名称
	   //校验地域
	   return true;
   }
   
   resize();
   
   $(window).bind('resize', function(e){
	   throttle(resize, 50)();
   });
   
   function resize(){
	   $('#grid-container').height($('body').height()-$('#query-container').outerHeight());
	   grid.setSize($('#grid-container').width(),$('#grid-container').height());
   }
   
   function throttle(fn, delay){
	   var timer = null;
	   return function(){
		   var context = this, args = arguments;
		   clearTimeout(timer);
		   timer = setTimeout(function(){
			   fn.apply(context, args);
		   }, delay);
	   }
   }
   
   function renderName(value, p, record){
	   //组装信息
	   var _data = record.data,
	       entityData = {
			   parentId: _data.parentId,
			   entityId: _data.entityId,
			   name: _data.name,
			   ip: _data.ip,
			   typeId: _data.typeId
	   	   };
	   return String.format("<a href='javascript:;' onclick='showEntity({1})'>{0}</a>", value, JSON.stringify(entityData)); 
	}
   
   function menuRender(v,m,r){
		return "<a href='javascript:editTopoFolder();' class='editTopoFolder'>@network/COMMON.editFolder@</a>";
	}
   
   function batchEditFolder(){
	   if(!checkSelect()) return;
	   var selections = grid.getSelectionModel().getSelections();
	   var ids = getSelectionIds(selections);
	   window.top.createDialog('editTopoFolder', "@COMMON.batchEditFolder@", 800, 500, '/entity/editTopoFolder.tv?action=batchEditFolder&entityIdStr='+ids.join(','), null, true, true, function(){
		   store.reload();
	   });
   }
   
   function batchInsertFolder(){
	   if(!checkSelect()) return;
	   var selections = grid.getSelectionModel().getSelections();
	   var ids = getSelectionIds(selections);
	   window.top.createDialog('editTopoFolder', "@COMMON.batchInsertFolder@", 800, 500, '/entity/editTopoFolder.tv?action=batchInsertFolder&entityIdStr='+ids.join(','), null, true, true, function(){
		   store.reload();
	   });
   }
   
   function batchRemoveFolder(){
	   if(!checkSelect()) return;
	   var selections = grid.getSelectionModel().getSelections();
	   var ids = getSelectionIds(selections);
	   window.top.createDialog('editTopoFolder', "@COMMON.batchRemoveFolder@", 800, 500, '/entity/editTopoFolder.tv?action=batchRemoveFolder&entityIdStr='+ids.join(','), null, true, true, function(){
		   store.reload();
	   });
   }
   
   function checkSelect(){
	   var sm = grid.getSelectionModel();
	   if(sm == null || !sm.hasSelection()){
		   window.parent.showMessageDlg('@COMMON.tip@', "@COMMON.pleaseSelectDevice@");
		   return false;
	   }
	   return true;
   }
   
   function getSelectionIds(selections){
	   var idArr = [];
	   for(var i=0, len=selections.length; i<len; i++){
		   idArr.push(selections[i].data.entityId);
	   }
	   return idArr;
   }
})
function editTopoFolder(){
   var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections(),
			entityId = selections[0].data.entityId;
		window.top.createDialog('editTopoFolder', "@network/COMMON.editFolder@", 800, 500, '/entity/editTopoFolder.tv?entityId='+entityId, null, true, true, function(){
			   store.reload();
		   });
	}	
}

function showEntity(data){
	var _data = data;
	if(EntityType.isUnkown_Type(_data.typeId)){
		//未知设备类型先处理
		window.parent.createDialog("entityBasicInfo", String.format("@NETWORK.entityBasicInfo@",_data.ip),600,510,"portal/showUnknownEntityJsp.tv?entityId=" + _data.entityId, null, true, true);
	}else if(EntityType.isOnuType(_data.typeId)){
		//处理ONU类型
		window.parent.addView('entity-' + _data.entityId, unescape(_data.name), 'entityTabIcon', '/onu/showOnuPortal.tv?onuId=' + _data.entityId );
	}else if(EntityType.isCmtsType(_data.typeId)){
		//处理CMTS
		window.parent.addView('entity-' + _data.entityId, _data.name, 'entityTabIcon', '/cmts/showCmtsPortal.tv?cmcId=' + _data.entityId);
	}else if(EntityType.isCcmtsType(_data.typeId)){
		//处理CCMTS
		window.parent.addView('entity-' + _data.entityId, unescape(_data.name), 'entityTabIcon', '/cmc/showCmcPortal.tv?cmcId=' + _data.entityId + "&productType=" + _data.typeId);
	}else if(EntityType.isOltType(_data.typeId)){
		//处理OLT
		window.parent.addView('entity-' + _data.entityId, _data.name, 'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + _data.entityId);
	}
}