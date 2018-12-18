var grid = null;
var store = null;
var entityMenu = null;
var lastRow = null;
var expander;
var isClickCpe=null;

function clickCpe(){
	isClickCpe=true;
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	
	expander = new Ext.ux.grid.RowExpander({
		id : 'expander' ,
		dataIndex : 'cpe',
	    enableCaching : false,
	    tpl : '',
	    expandOnDblClick : false,
	    isExpand: function(row){
	    	if(typeof row == 'number'){
	            row = this.grid.view.getRow(row);
	        }
	        return Ext.fly(row).hasClass('x-grid3-row-collapsed') ? false : true;
	    },
	    toggleRow : function(row){
	    	if(isClickCpe){
	    		if(typeof row == 'number'){
		            row = this.grid.view.getRow(row);
		        }
		        this[Ext.fly(row).hasClass('x-grid3-row-collapsed') ? 'expandRow' : 'collapseRow'](row);
	    	}
	    	isClickCpe=false;
	    }
	});
	
	expander.on("beforecollapse", function(expander,r,body,row){
		$("#expanderSpan" + row).removeClass();
		$("#expanderSpan" + row).addClass("cpeCollapse");
	});
	
	expander.on("beforeexpand",function(expander,r,body,row){
		if(lastRow != null && lastRow !== row){
			expander.collapseRow(lastRow);
		}
		lastRow = row;
		
		$("#expanderSpan" + row).removeClass();
		$("#expanderSpan" + row).addClass("cpeExpander");
			
		$.ajax({
			url: '/epon/onucpe/loadOnuUniCpeList.tv',
			data: {
				onuId : store.getAt(row).data.onuId
			},
			async: false,
			success: function(json){
				var cmcHtml = '';
				
				$.each(json, function(i, v){
					cmcHtml = cmcHtml + 
					"<tr>" +
			        "<td align='center'>" + v.uniDisplayName + "</td>" +
			        "<td align='center'>" + v.mac + "</td>" +
			        "<td align='center'>" + renderCommon(v.vlan) + "</td>" +
			        "<td align='center'>" + macTypeRender(v.type) + "</td>" +
			        "<td align='center'>" + renderCommon(v.ipAddress) + "</td>" +
			        "<td align='center'>" + cpeTypeRender(v.cpeType) + "</td>" +
			        "</tr>";
				});
				
		  		var tpl1 = new Ext.Template(
		    		'<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">@ONU.cpeListInfo@</span></p>' + 
		    		"<table width='96%' border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
		            "<thead>" +
		            "<tr>" +
		            "<th align='center'>@ONU.uniPort@</th>" +
		            "<th align='center'>CPE MAC</th>" +
		            "<th align='center'>VLAN ID</th>" +
		            "<th align='center'>@ONU.macType@</th>" +
		            "<th align='center'>CPE IP</th>" +
		            "<th align='center'>@ONU.cpeType@</th>" +
		            "</tr>" +
		            "</thead>" +
		            "<tbody id='tbody-append-child'>" + cmcHtml + "</tbody></table></div>"  
		        );
		    	
		        expander.tpl = tpl1;
				expander.tpl.compile();
			},
			error: function(){},
			cache: false
		});
			
	}); 
	
	Ext.override(Ext.grid.GridView,{  
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected"); 
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){  
	            var r = this.getRow(i);  
	            if(r){  
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	            }  
	        }  
	          
	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	          
	        if (selected == len && hd && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }  
	    },  
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");
	        this.removeRowClass(row, "yellow-row");
	        this.removeRowClass(row, "red-row");
	        this.removeRowClass(row, "white-row");
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
	var cmArr = [
        {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',width:80, align:'left',sortable:true,renderer:renderName},
        {id:"mac",header:"MAC",dataIndex:'onuMac',sortable:true, width:140},
        {id:"onuOperationStatus",header:"@COMMON.status@",dataIndex:'onuOperationStatus',width:80,sortable:true,renderer: renderOnline, align:'left'},
        {id:"entityIp",header:'<div class="txtCenter">@ONU.entity@</div>',dataIndex:'entityIp',width:160,align:'left', sortable:true,renderer: renderOlt},
        {id:"logicLocation",header:"@ONU.logicLocation@",dataIndex:'onuIndex',sortable:true,width:100, renderer:renderLogicLoc},
        {id:"tag",header:"@ONU.tag@",dataIndex:'tagName',sortable:true,width:100, renderer: renderCommon},
        {id:"uni1",header:"UNI1",dataIndex:'uniPorts',width:150, align:'center',renderer: renderUni},
        {id:"uni2",header:"UNI2",dataIndex:'uniPorts',width:150, align:'center',renderer: renderUni},
        {id:"uni3",header:"UNI3",dataIndex:'uniPorts',width:150, align:'center',renderer: renderUni},
        {id:"uni4",header:"UNI4",dataIndex:'uniPorts',width:150, align:'center',renderer: renderUni},
        {id:"cpe",header:"CPE",dataIndex:'cpeNum',sortable:true,width:80, renderer: renderCpe},
        {id:"wan1",header:"WAN1",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wan2",header:"WAN2",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan},
        {id:"wan3",header:"WAN3",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan},
        {id:"wan4",header:"WAN4",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wan5",header:"WAN5",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wan6",header:"WAN6",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wan7",header:"WAN7",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wan8",header:"WAN8",dataIndex:'wanConnects',width:180, align:'center',renderer: renderWan, hidden:true},
        {id:"wlan1",header:"WLAN1",dataIndex:'wanSsids',width:150, align:'center',renderer: renderWlan},
        {id:"wlan2",header:"WLAN2",dataIndex:'wanSsids',width:150, align:'center',renderer: renderWlan, hidden:true},
        {id:"wlan3",header:"WLAN3",dataIndex:'wanSsids',width:150, align:'center',renderer: renderWlan, hidden:true},
        {id:"wlan4",header:"WLAN4",dataIndex:'wanSsids',width:150, align:'center',renderer: renderWlan, hidden:true},
	    {header: "@COMMON.manu@", width:120 ,renderer : manuRender, fixed:true}
	];
	
	var cmConfig = CustomColumnModel.init("eponOnuBusinessList",cmArr,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'entityIp', direction: 'ASC'};
		
	store = new Ext.data.JsonStore({
	    url: '/onulist/queryEponOnuBusinessList.tv',
	    idProperty: "onuId",
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["name","onuMac","onuId","onuIndex","entityId","entityIp","onuOperationStatus","lastOfflineReason",
	             "tagName","manageName", "attention","uniPorts","cpeNum","wanConnects","wanSsids","tagId",,"templateId","replenish2",
	     	    "replenish3","replenish4","replenish5","replenish6","replenish7","replenish8","replenish9"]
	});

	store.setDefaultSort(sortInfo.field, sortInfo.direction);
    store.load({params: {start:0,limit: pageSize}});
    
	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		cls:"normalTable edge10",
   		bodyCssClass: 'normalTable',
   		border:true,
   		store: store,
   		enableColumnMove : true,
   		title:"@ONU.eponBusinessList@",
   		cm : cm,
   		sm : new Ext.grid.CheckboxSelectionModel({singleSelect: true}),
   		plugins: expander,
   		loadMask : true,
   		bbar: buildPagingToolBar(pageSize, store),
//   		viewConfig:{
//   			forceFit: true
//   		},
	   	listeners : {
	   		rowclick: function(grid, rowIndex, e) {
	   			reOnuLocate();
	   	    },
   			sortchange : function(grid,sortInfo){
   				CustomColumnModel.saveSortInfo('eponOnuBusinessList', sortInfo);
   			},
   		    columnresize: function(){
   	    	    CustomColumnModel.saveCustom('eponOnuBusinessList', cm.columns);
   	        },
   	        cellclick : function(cell,row,col,e){
   	           if(col == getColIndex(grid, "cpe")){
   	        	   expander.toggleRow(row);
   	           }
   	        }
   		},
		store:store
   	});
    var viewPort = new Ext.Viewport({
  	     layout: "border",
  	     items: [grid, {
	   		region: 'north',
	   		height: 95,
	   		contentEl : 'topPart',
				border: false
	       }]
  	}); 
});

//-------------------------------------------------------- render start

function manuRender(v,m,r){
	var str1 = String.format("<a href='javascript:;' onClick='refreshOnu();'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
			   r.id),
	    str2 = String.format("<span>@COMMON.refresh@</span> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
	 		   r.id);
	if(!refreshDevicePower) {
		return str2;
	}else{
		return str1;
	}
}

function showMoreOperation(rid,event){
	var record = grid.getStore().getById(rid);  // Get the Record
	WIN.entityId = record.data.entityId;
	WIN.onuId = record.data.onuId;
	WIN.onuIndex = record.data.onuIndex;
	WIN.onulocation = getLocationByIndex(record.data.onuIndex);
	grid.getSelectionModel().selectRecords([record]);
	showEntityMenu(event, makeItems(record));
}

/**
 * 显示更多菜单
 * @param event
 * @param items
 */
function showEntityMenu(event,items){
	if(!entityMenu){
		entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 150, enableScrolling: false, items: items});
	}else{
		entityMenu.removeAll();
		entityMenu.add( items );
	}
	entityMenu.showAt( [event.clientX,event.clientY] );
}

function makeItems(record){
	var $items = [];
	$items.push({id:"uniConfig",text: "@ONU.uniConfig@", handler: onuConfig, entityId:record.data.entityId});
	$items.push({id:'onuCapDeregister',text: "@ONU.unregister@", handler: unregisterOnu, disabled:!operationDevicePower});
	$items.push({text: "@COMMON.restore@", handler : resetOnu, disabled:!operationDevicePower});
	$items.push({text: "@COMMON.delete@", handler : deleteOnu, disabled:!operationDevicePower});
	return $items;
}

function renderUni(value, m, record, rowIndex, columnIndex){
	var uniInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "uni" + value[i].uniNo)){
			uniInfo = convertPortStatus(value[i].uniOperationStatus) + " / PVID:" + value[i].bindPvid + " / " + convertVlanMode(value[i].profileMode);
			break;
		}
	}
	return uniInfo;
}

function renderWan(value, m, record, rowIndex, columnIndex){
	var wanInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "wan" + value[i].connectId)){
			wanInfo = value[i].ipModeString + " / " + value[i].connectStatusString + "(" + value[i].pppoeStatusIpv4Addr + ")";
			break;
		}
	}
	return wanInfo;
}

function renderWlan(value, m, record, rowIndex, columnIndex){
	var wlanInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "wlan" + value[i].ssid)){
			wlanInfo = value[i].ssid + " / " + value[i].ssidName;
			break;
		}
	}
	return wlanInfo;
}
//--------------------------------------------------------------- render end

//--------------------------------------------------------------- 功能相关 start

function onuConfig(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, name = onu.name;
		onuConfigFn(entityId, onuId, name);
	}
}

/**
 * 重新加载关联信息
 */
function reOnuLocate(){
	var sm = grid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		showOnuLocate(sm.getSelected());
	}
}

/**
 * 解注册
 */
function unregisterOnu(){
	var onu = getSelectedEntity(grid);
	var callbacks = [refresh, reOnuLocate];
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		unregisterOnuFn(entityId, onuId, callbacks);
	}
}

/**
 * 复位
 */
function resetOnu(){
	var onu = getSelectedEntity(grid);
	var callbacks = [refresh, reOnuLocate];
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		resetOnuFn(entityId, onuId, callbacks);
	}
}

/**
 * 删除
 */
function deleteOnu(){
	var onu = getSelectedEntity(grid);
	var callbacks = [refresh];
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		deleteOnuFn(entityId, onuId, 'E', callbacks);
	}
}

/**
 * 替换
 */
function replaceOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var onuId = onu.onuId;
		replaceOnuFn(onuId);
	}
}

/**
 * 关注
 */
function attentionOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, attention = onu.attention;
		attentionOnuFn(entityId, onuId, attention, refresh);
	}
}

/**
 * 标签
 */
function tagOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var onuId = onu.onuId, tagId = onu.tagId;
		tagOnuFn(onuId, tagId, refresh);
	}
}

/**
 * 刷新onu信息
 */
function refreshOnu(){
	var onu = getSelectedEntity(grid);
	if(onu){
		var callbacks = [refresh, reOnuLocate];
		var entityId = onu.entityId, onuId = onu.onuId, onuIndex = onu.onuIndex;
		refreshOnuFn(entityId, onuId, onuIndex, callbacks);
		collapseRow();
	}
}

//--------------------------------------------------------------- 功能相关 end
