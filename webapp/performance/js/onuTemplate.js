var grid = null;
var store = null;
function showEntityDetail(entityId, entityName){
	//window.parent.addView('entity-' + entityId, entityName , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
	var name = unescape(entityName);
	window.parent.addView('entity-' + entityId, name , 'entityTabIcon','/onu/showOnuPerf.tv?module=4&onuId=' + entityId);
}

function deviceNameRender(value, p, record){
	var deviceName = escape(record.data.entityName);
	var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
	return String.format(formatStr, record.data.entityId, deviceName, record.data.entityName);
}

//可以不提供根据IP跳转至OLT性能查看页面
function ipRender(value, p, record){
	var deviceName = escape(record.data.entityName);
	var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
	return String.format(formatStr, record.data.entityId, deviceName, record.data.entityIp);
}
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['openBtn', 'closeBtn', 'relaBtn', 'noRelaBtn'], false);
    }else{
    	disabledBtn(['openBtn', 'closeBtn', 'relaBtn', 'noRelaBtn'], true);
    }
};
$(document).ready(function() {
	var sm = new Ext.grid.CheckboxSelectionModel({
	    listeners : {
	        rowselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        },
	        rowdeselect : function(sm,rowIndex,record){
	            disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
	        }
	    }
	}); 
	var cm =  new Ext.grid.ColumnModel([
	    sm,
	    {id:"entityName",header:'<div class="txtCenter">@Performance.entityName@</div>',dataIndex:'entityName', align:'left',width:160, renderer: deviceNameRender},
	    {id:"entityIp",header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>',dataIndex:'uplinkDevice',align:'left',width:100},
	    {id:"entityTypeDisplayName",header:'@Performance.entityType@',dataIndex:'entityTypeDisplayName',align:'center',width:80},
	    {id:"templateName",header:'<div class="txtCenter">@Performance.perfTemplate@</div>',dataIndex:'templateName',align:'left',width:140, renderer:renderTemplate},
	    {id:"isPerfThreshold",header:'@Performance.perfAlertSwitch@',dataIndex:'isPerfThreshold',align:'center',width:80,renderer:renderSwitch},
	    {header: '@COMMON.opration@', dataIndex: 'templateId', width:260,fixed:true, renderer:renderOperation_}
	]);
   
	store = new Ext.data.JsonStore({
	    url: '/onu/onuPerfGraph/loadOnuPerfTemplateList.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['entityId','entityName','uplinkDevice','entityIp','entityTypeDisplayName','entityType','typeId','isPerfThreshold', 
	             'templateId', 'templateName']
	});
    
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
            items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'],
            listeners : {
            	beforechange : function(){
            		disabledToolbarBtn(0);
            	}
            }
        }),
        cm:cm,
		sm:sm,
		loadMask : true,
		viewConfig:{
			forceFit:true
		}
    });
    store.load({params: {start:0,limit:pageSize}});
    
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
           {id: 'openBtn',text: '@Performance.openPerfAlert@',cls:'mL10', iconCls: 'bmenu_play', handler: batchOpenThresholdAlarm, disabled: true},'-',
           {id: 'closeBtn',text: '@Performance.closePerfAlertDesc@', iconCls: 'bmenu_stop', handler: batchCloseThresholdAlarm, disabled: true},'-',
           {id: 'relaBtn',text: '@Performance.relaDeviceTemp@', iconCls: 'bmenu_miniIcoLink', handler: batchBindTemplate, disabled: true},'-',
           {id: 'noRelaBtn',text: '@Performance.noRelaTemp@', iconCls: 'bmenu_miniIcoBroken', handler: unBindTemplate, disabled: true},'-',
           {text: '@COMMON.refresh@',iconCls: 'bmenu_refresh', handler: refreshGrid}
        ]
    });
    
    set_entity_threshold_GridSize();
    
    $(window).resize(function(){
    	throttle(set_entity_threshold_GridSize,window)
    });

});
function throttle(method, context) {
    clearTimeout(method.tId);
    method.tId = setTimeout(function(){
        method.call(context);
    }, 100);
}

function onSeachClick() {
	//获取查询数据
	var queryObject = {};
	
	//如果设备别名不为空，则加入查询条件
	if($("#entityName").val()!=""){
		queryObject.deviceName = $("#entityName").val();
	}
	if($("#entityIp").val()!=""){
		//验证IP地址是否合法
		/*if(!Validator.isFuzzyIpAddress($("#entityIp").val())){
			$("#entityIp").focus();
			return false;
		}*/
		queryObject.deviceIp = $("#entityIp").val();
	}
	queryObject.tempRela = $("#tempRela").val();
	//如果模板名称不为空，则加入查询条件
	if($("#templateName").val()!=""){
		queryObject.templateName = $("#templateName").val();
	}
	queryObject.start = 0;
	queryObject.limit = pageSize;
	
	store.baseParams = queryObject;
    store.load({
    	callback : function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
    	}
    });
}

//override公用方法中的showTemplateDetail，因为需要传递不一样的originalFrame
function showTemplateDetail(templateId){
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showModifyTemplate.tv?templateId='+templateId+"&m="+m+'&originalFrame=frameonuPerfTempMgmt');
}