var entityMenu = null;
var grid = null;
var store = null;
var expander;
var lastRow = null;
var isClickCpe=null;

//刷新超时时间
var REFRESH_TIMEOUT = 30000;

function manuRender(v,m,r){
	var str1 = String.format("<a href='javascript:;' onClick='refreshGponOnu();'>@COMMON.refresh@</a> / <a href='javascript:;' class='withSub'  onClick='showMoreOperation({0},event)'>@COMMON.other@</a>",
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
	showEntityMenu(event,makeItems(record));
}

function makeItems(record){
	var $items = [];
	$items.push({id:'onuCapDeregister',text: "@ONU.unregister@", handler: unregisterOnu,disabled:!operationDevicePower});
	$items.push({text: "@COMMON.restore@", handler : resetOnu,disabled:!operationDevicePower});
	$items.push({text: "@COMMON.delete@", handler : deleteOnu,disabled:!operationDevicePower});
	return $items;
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
	var callbacks = [refresh, reOnuLocate];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		unregisterOnuFn(entityId, onuId, callbacks);
	}
}

/**
 * 复位
 */
function resetOnu(){
	var callbacks = [refresh, reOnuLocate];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		resetOnuFn(entityId, onuId, callbacks);
	}
}

/**
 * 删除
 */
function deleteOnu(){
	var callbacks = [refresh];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId;
		deleteOnuFn(entityId, onuId, 'G', callbacks);
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

//gpon业务模板引用的epon的onuAssociatedInfo.inc,里面是refreshOnu
//避免其他地方调用refreshGponOnu,所以在这增加这个方法来跳转
function refreshOnu() {
	refreshGponOnu();
}

/**
 * 刷新onu信息
 */
function refreshGponOnu(){
	var callbacks = [refresh, reOnuLocate];
	var onu = getSelectedEntity(grid);
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, onuIndex = onu.onuIndex;
		refreshOnuFn(entityId, onuId, onuIndex, callbacks);
		collapseRow();
	}
}

function showEntityMenu(event,items){
	if(!entityMenu){
		entityMenu = new Ext.menu.Menu({ id: 'entityMenu', minWidth: 150, enableScrolling: false, items: items});
	}else{
		entityMenu.removeAll();
		entityMenu.add( items );
	}
	entityMenu.showAt( [event.clientX,event.clientY] );
}

function renderUNI(value, m, record, rowIndex, columnIndex){
	var uniInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "UNI" + value[i].ethAttributePortIndex)){
			uniInfo = convertPortStatus(value[i].uniOperationStatus) + " / PVID:" + value[i].gponOnuUniPvid+" / @ACL.priority@："+value[i].gponOnuUniPri;
			break;
		}
	}
	return uniInfo;
}

function renderWan(value, m, record, rowIndex, columnIndex){
	var wanInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "WAN" + value[i].connectId)){
			wanInfo = value[i].ipModeString + " / " + value[i].connectStatusString + "(" + value[i].pppoeStatusIpv4Addr + ")";
			break;
		}
	}
	return wanInfo;
}

function renderWlan(value, m, record, rowIndex, columnIndex){
	var wlanInfo = '--';
	for(var i=0; i<value.length; i++){
		if(columnIndex == getColIndex(grid, "WLAN" + value[i].ssid)){
			wlanInfo = value[i].ssid + " / " + value[i].ssidName;
			break;
		}
	}
	return wlanInfo;
}

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
		
		var onuId = r.data.onuId;
		$.ajax({
	      url : "/epon/onucpe/loadOnuUniCpeList.tv?onuId=" + onuId,
	      async:false,
	      cache : false,
	      dataType:'json',
	      success : function(list) {
	    	  if(list == null ){
	        		var tpl1 = new Ext.Template(
		    	        		'No Data' );
		    			expander.tpl = tpl1;
		    			expander.tpl.compile();
	        	}else{
	    			var onuHtml = '';
	        		$.each(list,function(i,x){
	        			onuHtml = onuHtml +
	    					"<tr>" +
		                    "<td align='center'>" + x.uniDisplayName + "</td>" +
		                    "<td align='center'>" + x.mac + "</td>" +
		                    "<td align='center'>" + x.vlan + "</td>" +
		                    "<td align='center'>" + x.renderType+"</td>" +"</tr>";
	              });
	        		
	        		var tpl1 = new Ext.Template(
		        		'<div style="margin:5px 5px 5px 10px;"><p class="flagP"><span class="flagInfo">@ONU.cpeListInfo@</span></p>' + 
		        		"<table width=96% border='1' cellspacing='0' cellpadding='0' bordercolor='#DFDFDF' class='dataTable extGridPanelTable addOneTr' rules='none'>" +
		                "<thead>" +
		                "<tr>" +
		                "<th align='center'>@ONU.uniPort@</th>" +
		                "<th align='center'>CPE MAC</th>" +
		                "<th align='center'>VLAN ID</th>" +
		                "<th align='center'>@ONU.macType@</th>" +
		                "</tr>" +
		                "</thead>" +
		                "<tbody id='tbody-append-child'>" + onuHtml + "</tbody></table></div>"  
	              );
	          	
	  	        expander.tpl = tpl1;
	  			expander.tpl.compile();
	    	 }
	      }
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
	              
	            if (hd != null && selected != len && hd && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');   
	            }  
	    }  
	});  
	var cmArr = [
        {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',align:'left',width:80,sortable:true,renderer:renderName},
        {id:"identity",header:"GPON SN",dataIndex:'onuUniqueIdentification',align:'center',sortable:true, width:160},
        {id:"onuOperationStatus",header:"<div class='txtCenter'>@COMMON.status@</div>",dataIndex:'onuOperationStatus',width:80,sortable:true,align:'left',renderer: renderOnline},
        {id:"entityIp",header:'<div class="txtCenter">@ONU.entity@</div>',dataIndex:'entityIp',width:160,align:'left', sortable:true,renderer: renderOlt},
        {id:"logicLocation",header:"@ONU.logicLocation@",dataIndex:'onuIndex',sortable:true,width:100, renderer:renderLogicLoc},
        {id:"tag",header:"@ONU.tag@",dataIndex:'tagName',sortable:true,width:100, renderer: renderCommon},
        {id:"UNI1",header:"UNI1",dataIndex:'uniPorts',align:'center',width:180,renderer:renderUNI},
        {id:"UNI2",header:"UNI2",dataIndex:'uniPorts',align:'center',width:180,renderer:renderUNI},
        {id:"UNI3",header:"UNI3",dataIndex:'uniPorts',align:'center',width:180,renderer:renderUNI},
        {id:"UNI4",header:"UNI4",dataIndex:'uniPorts',align:'center',width:180,renderer:renderUNI},
        {id:"cpe",header:"CPE",dataIndex:'cpeNum',sortable:true,align:'center',width:80,renderer:renderCpe},
        {id:"WAN1",header:"WAN1",dataIndex:'wanConnects',align:'center',hidden:true, width:60,renderer:renderWan},
        {id:"WAN2",header:"WAN2",dataIndex:'wanConnects',align:'center',width:180,renderer:renderWan},
        {id:"WAN3",header:"WAN3",dataIndex:'wanConnects',align:'center',width:180,renderer:renderWan},
        {id:"WAN4",header:"WAN4",dataIndex:'wanConnects',align:'center',hidden:true, width:180,renderer:renderWan},
        {id:"WAN5",header:"WAN5",dataIndex:'wanConnects',align:'center',hidden:true, width:180,renderer:renderWan},
        {id:"WAN6",header:"WAN6",dataIndex:'wanConnects',align:'center',hidden:true, width:180,renderer:renderWan},
        {id:"WAN7",header:"WAN7",dataIndex:'wanConnects',align:'center',hidden:true, width:180,renderer:renderWan},
        {id:"WAN8",header:"WAN8",dataIndex:'wanConnects',align:'center',hidden:true, width:180,renderer:renderWan},
        {id:"WLAN1",header:"WLAN1",dataIndex:'wanSsids',align:'center',width:150,renderer:renderWlan},
        {id:"WLAN2",header:"WLAN2",dataIndex:'wanSsids',align:'center',hidden:true, width:150,renderer:renderWlan},
        {id:"WLAN3",header:"WLAN3",dataIndex:'wanSsids',align:'center',hidden:true, width:150,renderer:renderWlan},
        {id:"WLAN4",header:"WLAN4",dataIndex:'wanSsids',align:'center',hidden:true, width:150,renderer:renderWlan},
        {header: "@COMMON.manu@", width:120 ,renderer : manuRender, fixed:true}
	];
	var cmConfig = CustomColumnModel.init("gponOnuBusinessList",cmArr),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'entityIp', direction: 'ASC'};
	store = new Ext.data.JsonStore({
	    url: '/onulist/queryGponOnuBusinessList.tv',
	    idProperty: "onuId",
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["name","onuUniqueIdentification","onuId","onuIndex","entityId","entityIp","onuOperationStatus","lastOfflineReason",
	             "tagName","manageName", "attention","uniPorts","cpeNum","wanConnects","wanSsids","tagId","templateId","replenish2",
		     	    "replenish3","replenish4","replenish5","replenish6","replenish7","replenish8","replenish9"]
	});
	store.setDefaultSort(sortInfo.field, sortInfo.direction);
    store.load({params: {start:0,limit: pageSize,onuEorG: 'G'}});
    
	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region: "center",
   		cls:"normalTable edge10",
   		bodyCssClass: 'normalTable',
   		border:true,
   		store: store,
   		enableColumnMove : true,
   		title:"@gpon/GPON.onuBusinessList@",
   		cm : cm,
   		sm : new Ext.grid.CheckboxSelectionModel({singleSelect: true}),
   		plugins: expander,
   		loadMask : true,
   		bbar: buildPagingToolBar(pageSize,store),
   		/*viewConfig:{
   			forceFit: true
   		},*/
	   	listeners : {
	   		rowclick: function(grid, rowIndex, e) {
	   			reOnuLocate();
	   	    },
   			sortchange : function(grid,sortInfo){
   				CustomColumnModel.saveSortInfo('gponOnuBusinessList', sortInfo);
   			},
   		    columnresize: function(){
   	    	    CustomColumnModel.saveCustom('gponOnuBusinessList', cm.columns);
   	        },
   	        cellclick : function(cell,row,col,e){
 	           if(col == getColIndex(grid, "cpe")){
 	        	   expander.toggleRow(row);
 	           }
 	        }
   		},
		store:store,
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

function showLoading(message) {
	if(message){
		$("#loading").text(message);
	}
	var lPos = ($(window).width() - $("#loading").outerWidth()) / 2;
	var tPos = 200;
	$("#loading").show().css({
		top: tPos,
		left: lPos
	});
}

function hideLoading() {
  $("#loading").hide();
}
