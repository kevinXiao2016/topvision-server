/**
 * All entity view by detail.
 * 
 * author:niejun
 */
var contextMenu = null;
var entityMenu = null;
var grid = null;
var store = null;
var pagingToolbar = null;

function showEntitySnap(id, $name) {
	Ext.menu.MenuMgr.hideAll();
	window.parent.addView('entity-' + id, $name, 'entityTabIcon',
		'portal/showEntitySnapJsp.tv?entityId=' + id);
}

function renderOnlie(value, p, record){
	if (value) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
			'@COMMON.online@');	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
				'@COMMON.offline@');	
	}
}

function onRefreshClick() {
	store.reload();
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
                       		]});
   return pagingToolbar;
}

function onFindClick() {
	store.baseParams={
		start: 0,
		limit: pageSize,	
		queryText: $('#searchEntity').val(),
		deviceType: $('#deviceType').val(),
		onlineStatus: $('#onlineStatus').val()
	}
	store.load();
}


function showEntityDetail(entityId, $name){
	window.parent.addView('entity-' + entityId, $name , 'entityTabIcon','portal/showEntitySnapJsp.tv?entityId=' + entityId);
}


//构建设备类型查询
function buildDeviceTypeSelect() {
	var head = '<td style="width: 40px;"  align="right">'
			+ '@network/batchTopo.entityType@:'
			+ '</td>&nbsp;'
			+ '<td style="width: 100px;">'
			+ '<select id="deviceType" class="normalSel" style="width: 100px;">'
			+ '<option value="-1" selected>' + '@COMMON.pleaseSelect@'
			+ '</option>';
	var options = "";
	var tail = '</select></td>';
	// 获取设备类型
	
	$.ajax({
		url : '/entity/loadSubEntityType.tv?type=1',
		type : 'POST',
		dateType : 'json',
		success : function(response) {
			var entityTypes = response.entityTypes;
			for ( var i = 0; i < entityTypes.length; i++) {
				options += '<option value="' + entityTypes[i].typeId + '">'
						+ entityTypes[i].displayName + '</option>';
			}
		},
		error : function(entityTypes) {
		},
		async : false,
		cache : false
	});
	return head + options + tail;
}

//构建在线状态查询
function buildStatusInput() {
	return '<td width=40px align=center>'
			+ '@network/NETWORK.onlineStatus@:'
			+ '</td>&nbsp;'
			+ '<td><select class="normalSel" id="onlineStatus"><option value="-1">@COMMON.select@'
			+ '</option><option value="1">@COMMON.online@</option><option value="0">@COMMON.offline@</option></select></td>'
}


function buildEntityInput() {
    return '<td width=150>&nbsp;@network/NETWORK.attenQuery@:' + '</td>&nbsp;' +
           '<td><input class="normalInput" type="text" style="width: 150px" id="searchEntity" maxlength="63"></td>'
}
function renderMac(value, p, record){
	if(record.data.mac == "" || record.data.mac == null){
		return "--";
	}else{
		return record.data.mac;
	}
}
function viewAlertByIp(name,ip,id,parentId,parentName) {
	var title = name;
	if(!name){
		title = id;
	}
	if(parentId != null && parentId != ''){
		id = parentId;
		title = parentName;
	}
	window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/portal/showEntitySnapJsp.tv?module=1&entityId=" + id,null,true);
}

function viewAlertByAlert(name,id,type) {
	var oltType = EntityType.getOltType();
	var cmcType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	var onuType = EntityType.getOnuType();
	var title = name;
	if(!name){
		title = id;
	}
	if(EntityType.isOltType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
	}
	if(EntityType.isCcmtsType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
	}
	if(EntityType.isCmtsType(type)){
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/cmts/alert/showCmtsAlert.tv?module=7&cmcId="+id+"&productType=" + cmtsType ,null,true);
	}
	if(EntityType.isOnuType(type)){
		var onuType = EntityType.getOnuType();
		window.top.addView("entity-" + id, "@fault/ALERT.entity@" + "[" + title + ']', 'entityTabIcon',
				"/onu/showOnuAlert.tv?module=3&onuId="+id ,null,true);
	}
}

Ext.onReady(function() {
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var cm =  new Ext.grid.ColumnModel([
	    {id:"name",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',width: 150,align:'left',sortable:true,renderer:function(value, p, record){
	    		return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+record.data.name+'\');">'+value+'</a>';
	    }},
	    {id:"onlineStatus",header:"@NETWORK.onlineStatus@",dataIndex:'state',align:'center',width: 60,sortable:true,renderer: renderOnlie},
	    {id:"deviceIp",header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>',dataIndex:'uplinkDevice',align:'left',width: 110,sortable:true,renderer: function(value, p, record){
	    		return '<a href="#" onclick="viewAlertByIp(\''+record.data.name+'\',\''+record.data.ip+'\',\''+record.data.entityId+'\',\''+record.data.parentId+'\',\''+record.data.parentName+'\');">'+value+'</a>';
	    }},
	    {id:"mac",header:"MAC",dataIndex:'mac',align:'center',width: 80,sortable:true,renderer: renderMac},
	    {id:"alertNum",header:"@resources/WorkBench.AlertNum@",dataIndex:'alertNum',align:'center',width: 80,sortable:true,renderer:function(value, p, record){
	    		return '<a href="#" onclick="viewAlertByAlert(\''+record.data.name+'\',\''+record.data.entityId+'\',\''+record.data.typeId+'\');">'+value+'</a>';
	    }},
	    {id:"sysName",header:'<div class="txtCenter">@COMMON.name@</div>',dataIndex:'sysName',align:'left',width: 80,sortable:true},
	    {id:"typeName",header:"@NETWORK.typeHeader@",dataIndex:'typeName',align:'center',width: 80,sortable:true}
	]);
		
	store = new Ext.data.JsonStore({
	    url: '/entity/loadUserAttentionList.tv',
	    idProperty: "entityId",
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['entityId','parentId','uplinkDevice','ip','name','state','typeName','sysName','mac','parentName','alertNum','typeId']
	});
	store.setDefaultSort('name', 'ASC');
   
	var toolbar = [
	  	buildEntityInput(),
	    {xtype: 'tbspacer', width: 3},
	    buildDeviceTypeSelect(),
	    {xtype: 'tbspacer', width: 3},
	    buildStatusInput(),
	    {xtype: 'tbspacer', width: 3},
		{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onFindClick}
	];
    
   	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		border:false,
   		store: store,
   		cm : cm,
   		sm : sm,
   		tbar: toolbar,
   		bbar: buildPagingToolBar(),
   		viewConfig:{
   			forceFit: true
   		},
	    renderTo: "grid"
   	});
   	var viewPort = new Ext.Viewport({
   	     layout: "border",
   	     items: [grid]
   	}); 
	store.load({params:{start: 0, limit: pageSize}});
});
