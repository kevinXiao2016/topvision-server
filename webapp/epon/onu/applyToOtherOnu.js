$(function(){
	createSearch();
	createGrid();
	createConfigTable();
	createViewPort();
});//end document.ready;

function createViewPort(){
	var viewPort = new Ext.Viewport({
  	     layout: "border",
  	     items: [ grid,{
		   	   xtype     : 'container',
		   	   margins   : '10 0 0 10',
		   	   height    : 50,
		   	   region    : 'north',
			   border    : false,
		   	   contentEl : 'putNorth'
	       },{
	    	   xtype     : 'container',
	    	   width     : '100%',
	    	   height    : 170,
	    	   region    : 'south',
	    	   border    : false,
	    	   contentEl : 'putSouth' 
	       }]
  	}); 
}
function createConfigTable(){
	var ckTable = new Nm3kTools.createCheckBoxTable({
		renderId : 'putConfig',
		lineNum  : 5,
		title    : '@ONU.selectConfig@',
		data     : [{
			label   : '@ONU.portEnable@',
			checked : true,
			value   : '1'
		},{
			label   : '@ONU.uniVlanInfo@',
			checked : true,
			value   : '2'
		},{
			label   : '@ONU.uniSpeedDec@',
			checked : true,
			value   : '3'
		},{
			label   : '@ONU.workMode@',
			checked : true,
			value   : '4'
		},{
			label   : '@ONU.uniUSUtgPriSimple@',
			checked : true,
			value   : '5'
		},{
			label   : '@ONU.macLearn@',
			checked : true,
			value   : '6'
		},{
			label   : '@ONU.stasitcEnable@',
			checked : true,
			value   : '7'
		}] 
	});
}
function createSearch(){
	//onu类型选择
    window.typeStore = new Ext.data.JsonStore({
	    url: '/onu/loadOnuTypes.tv',
	    root : 'data',
	    fields: ['typeId','displayName'],
	    listeners : {
			load : function(dataStore, records, options){
					var record = {typeId: -1, displayName: '@COMMON.select@'};
					var $record = new dataStore.recordType(record,"-1");
					dataStore.insert (0,[ $record ]);
			}
		}
	});
    window.typeCombo = new Ext.form.ComboBox({
	    id: 'type',
	    applyTo : typeSelect,
	    width : 170,
		triggerAction : 'all',
		editable : true,
		lazyInit : false,
		emptyText : "@COMMON.select@",
		valueField: 'typeId',
	    displayField: 'displayName',
		store : typeStore
    });
  
    //加载olt对应的slot
	window.slotStore = new Ext.data.JsonStore({
		url : '/onu/getOltSlotList.tv',
		fields : ["slotId", "slotNo"],
		listeners : {
			load : function(dataStore, records, options){
				var entityId = $("#oltSelect").val();
				//只有选择了有效的设备才需要这样做
				if(entityId != null && entityId != -1){
					var record = {slotId: -1, slotNo: '@COMMON.select@'};
					var $record = new dataStore.recordType(record,"-1");
					dataStore.insert (0,[ $record ]);
				}
			}
		}
	});
	
	window.slotCombo = new Ext.form.ComboBox({
		id : "slotCombox",
		store : slotStore,
		applyTo : "slotSelect",
		mode : 'local',
		valueField : 'slotId',
		displayField : 'slotNo',
		emptyText : "@COMMON.select@",
		selectOnFocus : true,
		typeAhead : true,			
		triggerAction : 'all',
		editable : false,
		width : 150,
		enableKeyEvents: true,
		listeners : {
			keydown : function(textField, event){
				if( event.getKey() ==  8 ){
					event.stopEvent();
					return;
				}
			}
		}
	});
	
	//加载slot对应的pon
	window.ponStore = new Ext.data.JsonStore({
		url : '/onu/getOltPonList.tv',
		fields : ["ponId", "ponNo"],
		listeners : {
			load : function(dataStore, records, options){
				var slotValue = slotCombo.getValue();
				//只有选择了有效的板卡才需要这样做
				if(slotValue != null && slotValue != -1){
					var record = {ponId: -1, ponNo: '@COMMON.select@'};
					var $record = new dataStore.recordType(record,"-1");
					dataStore.insert (0,[ $record ]);
				}
			}
		}
	});
	
	window.ponCombo = new Ext.form.ComboBox({
		id : "ponCombox",
		store : ponStore,
		applyTo : "ponSelect",
		mode : 'local',
		valueField : 'ponId',
		displayField : 'ponNo',
		emptyText : "@COMMON.select@",
		selectOnFocus : true,
		typeAhead : true,			
		triggerAction : 'all',
		editable : false,
		width : 170,
		enableKeyEvents: true,
		listeners : {
			keydown : function(textField, event){
				if( event.getKey() ==  8 ){
					event.stopEvent();
					return;
				}
			}
		}
	});
	//当slot改变时加载对应的pon
	slotCombo.on('select', function(comboBox){
		//先清除加载的pon
		ponCombo.clearValue(); 
	  	//加载对应pon
		var slotValue = comboBox.getValue();
		ponStore.load({params: {slotId: slotValue}});
	})
	
	window.selector = new NetworkNodeSelector({
	    id: 'oltSelect',
	    renderTo: "oltContainer",
	    //value : window["entityId"], //@赋值的方式一：配置默认值 
	    autoLayout: true,
	    listeners: {
	      selectChange: function(){
	    	    //olt设备改变时加载对应的slot
	    	    //先清除加载的slot,pon
	    	  	slotCombo.clearValue(); 
	    	  	ponCombo.clearValue();
	    	  	//加载对应的slot
	    		var entityId = $("#oltSelect").val();
	    		slotStore.load({params: {entityId: entityId}});
	    		//这时候删除上次加载的pon口数据,否则上次加载的pon口列表还存在
	    		ponStore.removeAll();
	    	}
	    }
	});
}
function createGrid(){
	sm = new Ext.grid.CheckboxSelectionModel(); 
	cm =  new Ext.grid.ColumnModel([
	    sm,
        {id:"onuName",header:'<div class="txtCenter">@COMMON.alias@</div>',dataIndex:'name',align:'left',sortable:true,renderer:renderName},
        {id:"onuMac",header:'MAC',dataIndex:'onuMac',sortable:true, width:120},
        {id:"onuOperationStatus",header:"@COMMON.status@",dataIndex:'onuOperationStatus',width:50,sortable:true,renderer: renderOnline},
        {id:"onuDesc",header:'<div class="txtCenter">@COMMON.desc@</div>',dataIndex:'onuDesc', align:'left', sortable:true},
	    {id:"entityIp",header:'<div class="txtCenter">@ONU.belongOlt@</div>',dataIndex:'entityIp',width:160,align:'left', sortable:true,renderer:renderOlt},
	    {id:"onuIndex",header:"@ONU.onuAddr@",dataIndex:'onuIndex',sortable:true,width:60,renderer:renderIndex},
        {id:"onuPreType",header:"@ONU.type@",dataIndex:'typeName',width:80,sortable:true},
	    {id:"onuSoftwareVersion",header:"@ONU.softVersion@",dataIndex:'onuSoftwareVersion',sortable:true}
	]);
	store = new Ext.data.JsonStore({
	    url: '/onu/queryForOnuList.tv',
	    idProperty: "onuId",
	    root: 'data', totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ["typeName","onuId","onuIndex","entityId","entityIp","name","onuType","ponPerfStats15minuteEnable","onuIsolationEnable",
	     	    "onuPreType","onuMac","onuOperationStatus","onuSoftwareVersion","temperatureDetectEnable",
	     	   "onuChipVendor","onuPonRevPower","onuPonTransPower","oltponrevpower","manageName", "onuDesc"]
	});
	store.setDefaultSort('entityIp', 'ASC');
    store.load({params: {start:0,limit: pageSize}});
	grid = new Ext.grid.GridPanel({
   		stripeRows:true,
   		region:'center',
   		cls:"normalTable edge10",
   		bodyCssClass: 'normalTable',
   		border:true,
   		store: store,
   		title:"@ONU.onulist@",
   		cm : cm,sm : sm,
   		bbar: buildPagingToolBar(),
   		viewConfig:{
   			forceFit: true
   		},
	   	store:store
   	});
}
function renderName(value,m,record){
    return String.format('<a href="#" onclick=\'showOnuInfo("' + record.data.onuId + '","' + escape(record.data.name) + '");\'>{0}</a>',
            value);
}

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-'
                       		]});
   return pagingToolbar;
}

function renderOlt(value,m,record){
    var disValue = value +  "("+record.data.manageName+")";
	return String.format('<a href="#" onclick=\'showOlt("' + record.data.entityId + '","' + escape(record.data.entityIp) + '");\'>{0}</a>',
	        disValue);
	
}

function renderOnline(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../../images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../../images/fault/trap_off.png" border=0 align=absmiddle>',
			"@COMMON.offline@");	
	}
}

function renderIndex(value, p, record){
	return getLocationByIndex(value, "onu");
}

function renderType(value, p, record){
	try {
		return TYPE[''+value].type;
	}catch (e){
		return TYPE['255'].type;
	}
}
//查询 
function onSeachClick() {
	var name = $("#nameInput").val();
	var mac = $("#macInput").val();
	//验证MAC地址是否符合模糊匹配的格式(只需要在输入了MAC地址条件时才需要验证)
    if (mac != "" && !Validator.isFuzzyMacAddress(mac)) {
    	top.afterSaveOrDelete({
			title: '@COMMON.tip@',
			html: '<b class="orangeTxt">@COMMON.reqValidMac@</b>'
		});
  		$("#macInput").focus();
  		return;
    }
	var status = $("#statusSelect").val();
	var type = typeCombo.getValue();
	var entityId =  $("#oltSelect").val();
	var slotId = slotCombo.getValue();
	var ponId = ponCombo.getValue();
	var param = {onuName: name,onuPreType: type, entityId : entityId, macAddress : mac, status: status,
    		slotId : slotId, ponId: ponId, start:0,limit:pageSize}; 
	param = Ext.apply(param,partitionData);
    store.baseParams=param;
    store.load();
}
//通过index获得location
function getLocationByIndex(index, type){
	var t = parseInt(index / 256) + (index % 256);
	var loc = getNum(t, 1) + "/" + getNum(t, 2) + ":" + getNum(t, 3);
	if(type == "uni"){
		loc = loc + "/" + getNum(t, 4)
	}
	return loc
}
//通过mibIndex获得num
function getNum(index, s){
	var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
		break;
	case 3: num = (index & 0xFF00) >> 8;//ONU
		break;
	case 4: num = index & 0xFF;//UNI
		break;
	}
	return num;
}