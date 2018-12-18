var grid = null;
var store = null;
var cmArr = null;
var entityMenu = null;
var deactiveStatusIcomcls = "enableIconClass";

Ext.onReady(function() {
	Ext.QuickTips.init();
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
	
	columnArr = [
		 {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',width:80, align:'left',sortable:true,renderer:renderName},
		 {id:"mac",header:"MAC",dataIndex:'onuMac',sortable:true, width:120, renderer: renderEponMac},
		 {id:"gponSn",header:"GPON SN",dataIndex:'onuUniqueIdentification',sortable:true, width:160, renderer: renderGponSn},
		 {id:"onuOperationStatus",header:"@COMMON.status@",dataIndex:'onuOperationStatus',width:80,sortable:true,renderer: renderOnline, align:'left'},
		 {id:"sysUpTime",header:"@network/NETWORK.sysUpTime@",dataIndex:'changeTime',width:150,sortable:true, renderer: renderOnOffTime},
		 {id:"onuDesc",header:'<div class="txtCenter">@COMMON.desc@</div>',dataIndex:'onuDesc', align:'center',hidden:true, sortable:true},
		 {id:"entityIp",header:'<div class="txtCenter">@ONU.entity@</div>',dataIndex:'entityIp',width:150,align:'left', sortable:true,renderer: renderOlt},
		 {id:"ponPattern",header:"@ONU.ponPattern@",dataIndex:'onuEorG',sortable:true,width:80, renderer: renderPonPattern},
		 {id:"logicLocation",header:"@ONU.logicLocation@",dataIndex:'onuIndex',sortable:true,width:80, renderer: renderLogicLoc},
		 {id:"onuType",header:"ONU TYPE",dataIndex:'typeName',width:100,sortable:true},
		 {id:"onuSoftwareVersion",header:"@ONU.softVersion@",dataIndex:'onuSoftwareVersion',width:80, sortable:true},
		 {id:"onuHardwareVersion",header:"@epon/EPON.hardwareVersion@",dataIndex:'topOnuHardwareVersion',width:80, sortable:true},
		 {id:"onuChipVendor",header:"@downlink.prop.chipvender@",dataIndex:'onuChipVendor',width:80, sortable:true,hidden:true,renderer: renderChipVendor},
		 {id:"onucontact",header:"@ONU/ONU.contact@",dataIndex:'contact',sortable:true,width:80, hidden:true,renderer:addCellTooltip},
		 {id:"onulocation",header:"@ONU/ONU.location@",dataIndex:'location',sortable:true,hidden:true,renderer:addCellTooltip},
		 {id:"onunote",header:"@ONU/ONU.note@",dataIndex:'note',sortable:true,hidden:true,renderer:addCellTooltip},
		 {header: "@COMMON.manu@", width:120 ,renderer : manuRender, fixed:true}
	];
	
	//不需要显示列的 id， 纯epon下不需要显示["gponSn", "ponPattern"], 纯gpon下不需要显示["mac", "ponPattern"]
	var spliceColIds = [["gponSn", "ponPattern"],["mac", "ponPattern"]];
	//根据环境初始化显示列
	cmArr = initColumnArr(onuEnvironment, columnArr.slice(0), spliceColIds);
	
	var cmConfig = CustomColumnModel.init("onuDevicelist",cmArr,{}),
		cm = cmConfig.cm,
		sortInfo = cmConfig.sortInfo || {field: 'entityIp', direction: 'ASC'};
		
	store = new Ext.data.JsonStore({
	    url: '/onulist/queryOnuDeviceList.tv',
	    idProperty: "onuId",
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["onuUniqueIdentification","onuMac","typeName","onuId","manageName","onuIndex","entityId","entityIp","name",
	             "onuOperationStatus","onuSoftwareVersion","topOnuHardwareVersion","contact","location","note",'lastOfflineReason',
	     	   "onuChipVendor","onuDesc","attention","onuEorG","onuRunTime","changeTime","tagId","templateId","onuDeactive"]
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
   		title:"@ONU.onulist@",
   		cm : cm,
   		sm : new Ext.grid.CheckboxSelectionModel({singleSelect: true}),
   		loadMask : true,
   		bbar: buildPagingToolBar(pageSize, store),
   		viewConfig:{
   			forceFit: true
   		},
	   	listeners : {
	   		rowclick: function(grid, rowIndex, e) {
	   			reOnuLocate();
	   	    },
   			sortchange : function(grid,sortInfo){
   				CustomColumnModel.saveSortInfo('onuDevicelist', sortInfo);
   			},
   		    columnresize: function(){
   	    	    CustomColumnModel.saveCustom('onuDevicelist', cm.columns);
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

function renderPonPattern(value, p, record){
	if(value == 'E'){
		return "EPON";
	}else{
		return "GPON";
	}
}

function renderChipVendor(value,m,record){
	if(record.data.onuEorG == "E"){
		if(typeof value !='number'){
			value=parseInt(value)
		}
		var v = value.toString(16).toUpperCase();
		var length = v.length;
		for(var i=0;i<4-length;i++){
			v = '0'.concat(v);
		}
		return '0X'.concat(v);
	}else{
		return '--';
	}
}

function renderChipVendor(value){
	if(typeof value !='number'){
		value=parseInt(value)
	}
	var v = value.toString(16).toUpperCase();
	var length = v.length;
	for(var i=0;i<4-length;i++){
		v = '0'.concat(v);
	}
	return '0X'.concat(v);
}

/**
 * 联系人，位置，备注render
 * @param data
 * @param metadata
 * @param record
 * @param rowIndex
 * @param columnIndex
 * @param store
 * @returns
 */
function addCellTooltip(data, metadata, record, rowIndex, columnIndex, store) {
    var cellValue = data;
    if(cellValue == "" || cellValue == null){
    	return "--";
    }
    metadata.attr = ' ext:qtip="' + cellValue + '"';
    return cellValue;
}

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

/**
 * 显示更多
 * @param rid
 * @param event
 */
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
	var onuEorG = record.data.onuEorG;
	var onuDeactive = record.data.onuDeactive;
	if (onuDeactive == 1) {
		deactiveStatusIcomcls = "disableIconClass";
    } else {
    	deactiveStatusIcomcls = "enableIconClass";
    }
	if(onuEorG == "E"){
		$items.push({id:'onuCapDeregister',text: "@ONU.unregister@", handler: unregisterOnu,disabled:!operationDevicePower});
		$items.push({id:'onuReplace',text:"@epon/onu.replace.replace@", handler : replaceOnu,disabled:!operationDevicePower, entityId:record.data.entityId});
	}else if(onuEorG == "G"){
		$items.push({id:'onuDeactive',text: "@COMMON.deactive@", handler: deactiveOnu, iconCls:deactiveStatusIcomcls,disabled:!operationDevicePower});
	}
	$items.push({text: "@COMMON.restore@", handler : resetOnu, disabled:!operationDevicePower});
	$items.push({text: "@COMMON.delete@", handler : deleteOnu, disabled:!operationDevicePower});
	return $items;
}

//--------------------------------------------------------------- render end

//--------------------------------------------------------------- 功能相关 start

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
		var entityId = onu.entityId, onuId = onu.onuId, onuEorG = onu.onuEorG;
		deleteOnuFn(entityId, onuId, onuEorG, callbacks);
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
 * 激活/去激活
 */
function deactiveOnu(){
	var sm = grid.getSelectionModel();
    var record = sm.getSelected();
    var onuId = record.data.onuId;
    var onuDeactiveTemp = record.data.onuDeactive == 1 ? 2 : 1;
    var action = onuDeactiveTemp == 1 ? "@COMMON.active@" : "@COMMON.deactive@";
    window.parent.showConfirmDlg("@COMMON.tip@", "@COMMON.confirm@ "+action+" ONU?", function(type) {
        if (type == 'no') {
            return;
        }
        top.showWaitingDlg("@COMMON.wait@", "@COMMON.ing@ "+action);
        $.ajax({
            url:'/onu/onuDeactive.tv?onuId=' + onuId + '&onuDeactive=' + onuDeactiveTemp,
            type:'POST',
            dateType:'text',
            success:function(response) {
            	top.closeWaitingDlg("@COMMON.tip@");
            	top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">'+action+'@COMMON.success@！</b>'
	   			});
            	store.reload();
            },
            error:function() {
            	top.closeWaitingDlg("@COMMON.tip@");
                window.parent.showMessageDlg("@COMMON.tip@", action + "@COMMON.fail@！");
            },
            cache:false
        });
    });
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
	var callbacks = [refresh, reOnuLocate];
	if(onu){
		var entityId = onu.entityId, onuId = onu.onuId, onuIndex = onu.onuIndex;
		refreshOnuFn(entityId, onuId, onuIndex, callbacks);
	}
}

//--------------------------------------------------------------- 功能相关 end