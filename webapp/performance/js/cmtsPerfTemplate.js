var grid = null;
var store = null;

$(document).ready(function() {
	//构建CMTS设备类型下拉框
	$.each(cmtsSubTypes, function(index, subType){
		$('#entityType').append('<option value="'+subType.typeId+'">'+subType.displayName+'</option>');
	});
	
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var cm =  new Ext.grid.ColumnModel([
	    sm,
	    {id:"entityName",header:'@Performance.entityName@',dataIndex:'entityName',align:'center',sortable:true},
	    {id:"entityIp",header:'@Performance.entityIp@',dataIndex:'entityIp',align:'center',sortable:true},
	    {id:"entityTypeDisplayName",header:'@Performance.entityType@',dataIndex:'entityTypeDisplayName',align:'center',sortable:true},
	    {id:"templateName",header:'@Performance.perfTemplate@',dataIndex:'templateName',align:'center',sortable:true,renderer:renderTemplate},
	    {id:"isPerfThreshold",header:'@Performance.perfAlertSwitch@',dataIndex:'isPerfThreshold',align:'center',sortable:true,renderer: renderSwitch},
	    {header: '@COMMON.opration@', align:'center', dataIndex: 'templateId',renderer:renderOperation_}
	]);
	var oltType = getOltType();
	store = new Ext.data.JsonStore({
	    url: '/cmts/perfThreshold/loadCmtsThresholdTemplate.tv',
	    baseParams:{entityType:oltType},
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['entityId','entityName','entityIp','entityTypeDisplayName','entityType','typeId','isPerfThreshold', 'templateId', 'templateName']
	});
    store.load({params: {start:0,limit:pageSize}});
    
    grid = new Ext.grid.GridPanel({
    	renderTo: "grid",
		store:store,
        animCollapse:false,
        trackMouseOver:trackMouseOver,
        border: true,
        bbar: new Ext.PagingToolbar({
        	id: 'extPagingBar', 
        	pageSize: pageSize,
        	store:store,
            displayInfo: true, 
            items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-']
        }),
        cm:cm,
		sm:sm,
		bodyCssClass: 'normalTable',
		viewConfig:{
			forceFit:true
		}
    });
    
    grid.on('rowcontextmenu', function(grid, rowIndex, e) {
    	e.preventDefault()
   		var sm = grid.getSelectionModel()
		if (sm != null && !sm.isSelected(rowIndex)) {
			sm.selectRow(rowIndex)
		}
		var record = grid.getStore().getAt(rowIndex) // Get the Record
    });
    
    new Ext.Toolbar({
        renderTo: "toolbar",
        items: [
           {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: refreshGrid},
           {text: '@Performance.openPerfAlert@', iconCls: 'bmenu_saveOk', handler: batchOpenThresholdAlarm},
           {text: '@Performance.closePerfAlertDesc@', iconCls: 'bmenu_miniIcoOff', handler: batchCloseThresholdAlarm},
           {text: '@Performance.relaDeviceTemp@', iconCls: 'bmenu_rela', handler: batchBindTemplate},
           {text: '@Performance.noRelaTemp@', iconCls: 'bmenu_remove', handler: unBindTemplate}
        ]
    });
    
    set_entity_threshold_GridSize();
    
    $(window).resize(function(){
    	set_entity_threshold_GridSize();
    });
});

function onSeachClick(){
	//获取查询数据
	var queryObject = {};
	//如果设备别名不为空，则加入查询条件
	if($("#entityName").val()!=""){
		queryObject.entityName = $("#entityName").val();
	}
	//如果MAC地址不为空，则加入查询条件
	if($("#mac").val()!=""){
		//验证MAC地址是否合法
		if(!Validator.isMac($("#mac").val())){
			$("#mac").focus();
			return false;
		}
		queryObject.mac = $("#mac").val();
	}
	//如果模板名称不为空，则加入查询条件
	if($("#templateName").val()!=""){
		queryObject.templateName = $("#templateName").val();
	}
	//如果设备类型选择了具体设备，则加入查询条件
	if($("#entityType").val()!=-1){
		queryObject.typeId = $("#entityType").val();
		//针对不同的设备类型，需要传递不同的IP(A需要传上联OLT的IP，B需要传IP)
		var entityIp = "";
		if(EntityType.isCcmtsWithoutAgentType($("#entityType").val())){
			entityIp = $("#oltIpAddress").val();
			if(entityIp != ""){
				//验证IP地址是否合法
				if(!Validator.isFuzzyIpAddress(entityIp)){
					$("#oltIpAddress").focus();
					return false;
				}
			}
		}else if(EntityType.isCcmtsWithAgentType($("#entityType").val())){
			entityIp = $("#ccIpAddress").val();
			if(entityIp != ""){
				if(!Validator.isFuzzyIpAddress(entityIp)){
					$("#ccIpAddress").focus();
					return false;
				}
			}
		}
		//如果设备IP不为空，则加入查询条件
		if(entityIp != ""){
			queryObject.entityIp = entityIp;
		}
	}
	queryObject.start = 0;
	queryObject.limit = pageSize;
	
	store.baseParams = queryObject;
    store.load({params: queryObject});
}

//override公用方法中的showTemplateDetail，因为需要传递不一样的originalFrame
function showTemplateDetail(templateId){
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showModifyTemplate.tv?templateId='+templateId+"&m="+m+'&originalFrame=framecmtsPerfTemp');
}