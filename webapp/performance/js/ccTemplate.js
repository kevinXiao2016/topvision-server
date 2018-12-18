var grid = null;
var store = null;

function showEntityPerf(cmcId,entityName, entityType){
	var ccType = EntityType.getCcmtsType();
	var cmtsType = EntityType.getCmtsType();
	if(entityType == ccType){  //cmc性能曲线
		window.parent.addView('entity-' + cmcId, entityName , 'entityTabIcon','/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId=' + cmcId);
	}else if(entityType == cmtsType){ //cmts性能曲线
		window.parent.addView('entity-' + cmcId, entityName , 'entityTabIcon','/cmts/showCmtsCurPerf.tv?module=8&cmcId='+cmcId);
	}
}

function showEntityDetail(entityId, entityName){
	window.parent.addView('entity-' + entityId, entityName , 'entityTabIcon','/epon/oltPerfGraph/showOltPerfViewJsp.tv?module=7&entityId=' + entityId);
}

function deviceNameRender(value, p, record){
	var entityType = record.data.entityType;
	var formatStr = '<a href="#" onclick="showEntityPerf({0},\'{1}\',{2})">{1}</a>';
	return String.format(formatStr, record.data.entityId, record.data.entityName, entityType );
}

function renderIp(value, p, record){
	var typeId = record.data.typeId;
	var entityType = record.data.entityType;
	if (EntityType.isCcmtsWithoutAgentType(typeId)) {
		var formatStr = '<a href="#" onclick="showEntityDetail({0},\'{1}\')">{2}</a>';
		return String.format(formatStr, record.data.parentId,record.data.parentName, value);
	}else{
		var formatStr = '<a href="#" onclick="showEntityPerf({0},\'{1}\',{3})">{2}</a>';
		return String.format(formatStr, record.data.entityId,record.data.entityName, value, entityType);
	}
}
function disabledToolbarBtn(num){ //num为选中的的行的个数;
    if(num > 0){
        disabledBtn(['openBtn', 'closeBtn', 'relaBtn', 'noRelaBtn'], false);
    }else{
    	disabledBtn(['openBtn', 'closeBtn', 'relaBtn', 'noRelaBtn'], true);
    }
};
$(document).ready(function() {
	//在单独安装CC模块的时候,子类型下拉菜单默认只显示CMTS选择
	if(cmcSupport && !eponSupport){
		//构建CCMTS设备类型下拉框
		$.each(ccmtsSubTypes, function(index, subType){
			if(EntityType.isCcmtsWithAgentType(subType.typeId) || EntityType.isCmtsType(subType.typeId)){
				$('#entityType').append('<option value="'+subType.typeId+'">'+subType.displayName+'</option>');
			}
		});
	}else{
		//构建CCMTS设备类型下拉框
		$.each(ccmtsSubTypes, function(index, subType){
			$('#entityType').append('<option value="'+subType.typeId+'">'+subType.displayName+'</option>');
		});
	}
	
	entityChanged()
	
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
	    {id:"entityName",header:'<div class="txtCenter">@Performance.entityName@</div>',dataIndex:'entityName',align:'left',width:160, renderer: deviceNameRender},
	    {id:"entityIp",header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>',dataIndex:'uplinkDevice',align:'left',width:110,renderer: renderIp},
	    {id:"mac",header:'MAC',dataIndex:'mac',align:'center',width:100},
	    {id:"entityTypeDisplayName",header:'@Performance.entityType@',dataIndex:'entityTypeDisplayName',align:'center',width:80},
	    {id:"templateName",header:'<div class="txtCenter">@Performance.perfTemplate@<div>',width:160, dataIndex:'templateName',align:'left',renderer:renderTemplate},
	    {id:"isPerfThreshold",header:'@Performance.perfAlertSwitch@',dataIndex:'isPerfThreshold',align:'center',width:80,renderer: renderSwitch},
	    {header: '@COMMON.opration@', align:'center', dataIndex: 'templateId',width:260, fixed:true, renderer:renderOperation_}
	]);
	var ccType = EntityType.getCcmtsType();
	store = new Ext.data.JsonStore({
	    url: '/cmc/perfThreshold/loadCmcThresholdTemplate.tv',
	    baseParams:{entityType:ccType},
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    fields: ['entityId','entityName','mac','entityIp','uplinkDevice','entityTypeDisplayName','entityType','typeId', 
	             'parentId','parentName','isPerfThreshold', 'templateId', 'templateName']
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
		bodyCssClass: 'normalTable',
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
           {id: 'openBtn', text: '@Performance.openPerfAlert@', cls:'mL10', iconCls: 'bmenu_play', handler: batchOpenThresholdAlarm, disabled: true},"-",
           {id: 'closeBtn', text: '@Performance.closePerfAlertDesc@', iconCls: 'bmenu_stop', handler: batchCloseThresholdAlarm, disabled: true},"-",
           {id: 'relaBtn', text: '@Performance.relaDeviceTemp@', iconCls: 'bmenu_miniIcoLink', handler: batchBindTemplate, disabled: true},"-",
           {id: 'noRelaBtn', text: '@Performance.noRelaTemp@', iconCls: 'bmenu_miniIcoBroken', handler: unBindTemplate, disabled: true},"-",
           {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: refreshGrid}
        ]
    });
    
    set_entity_threshold_GridSize();
    
    $(window).resize(function(){
    	throttle(set_entity_threshold_GridSize, window);
    });
});

function throttle(method, context) {
    clearTimeout(method.tId);
    method.tId = setTimeout(function(){
        method.call(context);
    }, 100);
}

function onSeachClick(){
	//获取查询数据
	var queryObject = {};
	//如果设备别名不为空，则加入查询条件
	if($("#entityName").val()!=""){
		queryObject.entityName = $("#entityName").val();
	}
	//如果MAC地址不为空，则加入查询条件
	if($("#mac").val()!=""){
		//验证MAC地址是否合法(模糊匹配)
		if(!Validator.isFuzzyMacAddress($("#mac").val())){
			$("#mac").focus();
			return false;
		}
		queryObject.mac = $("#mac").val();
	}
	queryObject.tempRela = $("#tempRela").val();
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
		}else{
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
    store.load({
    	params: queryObject,
    	callback : function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
    	}
    });
}

function entityChanged(){
	var entityType = $('#entityType').val();
	if(EntityType.isCcmtsWithoutAgentType(entityType)){
		$('#optionLabel').text("@Performance.relaOlt@:");
		$('#optionSpan').empty().append('<input type="text" id="oltIpAddress" class="normalInput" toolTip="@tip.needCorIp@"/>');
		$('#optionLabel').next().andSelf().show();
	}else if(EntityType.isCcmtsWithAgentType(entityType) || EntityType.isCmtsType(entityType) ){
		$('#optionLabel').text("IP:");
		$('#optionSpan').empty().append('<input type="text" id="ccIpAddress" class="normalInput" toolTip="@tip.needCorIp@"/>');
		$('#optionLabel').next().andSelf().show();
	}else{
		$('#optionLabel').text("");
		$('#optionSpan').empty();
		$('#optionLabel').next().andSelf().hide();
	}
}

//override公用方法中的showTemplateDetail，因为需要传递不一样的originalFrame
function showTemplateDetail(templateId){
	$("#content").hide();
	$("#frameOuter").show();
	var m = Math.random();
	$("#frameInner").attr("src",'/performance/perfThreshold/showModifyTemplate.tv?templateId='+templateId+'&originalFrame=frameccPerfTemp'+"&m="+m);
}

function doRefresh(){
	
}