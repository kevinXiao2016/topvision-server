<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module network
    PLUGIN Portlet
    import /js/entityType
    import /js/topN
</Zeta:Loader>
<script type="text/javascript"> 
	var store,grid,pageBar;
	var pageSize = <%= uc.getPageSize() %>;
	
    // main.js的addView函数会调用
    function doRefresh(){
    	store.reload();
    }
    
  //开始时间与结束时间控件
    var beginTime,stopTime;
    //设置默认查询一周的数据
    var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
    var lastWeek = new Date(); 
    lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
    lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');

    if(beginTime == '' || beginTime == null){
    	beginTime = lastWeek;
    }

    if(stopTime == '' || stopTime == null){
    	stopTime = currentTime;
    }
    
    function nameRender(value, p, record){
    	var cmcId = record.data.parentId;
    	var mac = record.data.mac;
    	var name = record.data.name;
    	var entityId = record.data.entityId;
    	var entityType = record.data.entityType;
    	if(EntityType.isOnuType(entityType)){
    		return String.format("<a href='javascript:;' onclick='showOnuPortal({0},\"{1}\")'>"+ name +"</a>",entityId,name)
    	}else{
    		return String.format("<a href='javascript:;' onclick='showEntityPortal({0},\"{1}\",\"{2}\",{3})'>{2}</a>",cmcId, mac, value,entityId)
    	}
    	
    }
    
    function alertRender(value, p, record){
    	var cmcId = record.data.parentId;
    	var count = record.data.count;
    	var name = record.data.name;
    	var entityId = record.data.entityId;
    	var entityType = record.data.entityType;
    	return String.format('<a href="javascript:;" onclick="viewAlertByAlert(\'{0}\',{1},{2})">{3}</a>',name,entityId,entityType,count)

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
    	//showEntitySnap(id,title)
    	if(EntityType.isOltType(type)){
    		window.top.addView("entity-" + id, title, 'entityTabIcon',
    				"alert/showEntityAlertJsp.tv?module=5&entityId=" + id,null,true);
    	}
    	if(EntityType.isCcmtsType(type)){
    		window.top.addView("entity-" + id, title, 'entityTabIcon',
    				"/cmc/alert/showCmcAlert.tv?module=12&cmcId=" + id,null,true);
    	}
    	if(EntityType.isCmtsType(type)){
    		window.top.addView("entity-" + id, title, 'entityTabIcon',
    				"/cmts/alert/showCmtsAlert.tv?module=7&cmcId="+id+"&productType=" + cmtsType ,null,true);
    	}
    	if(EntityType.isOnuType(type)){
    		var onuType = EntityType.getOnuType();
    		window.top.addView("entity-" + id, title, 'entityTabIcon',
    				"/onu/showOnuAlert.tv?module=3&onuId="+id ,null,true);
    	}
    }
    
    function showOnuPortal(entityId,name){
    	var onuType = EntityType.getOnuType();
    	window.top.addView("entity-" + entityId, name, "entityTabIcon",
			"/onu/showOnuAlert.tv?module=3&onuId="+entityId,null,true);
    }
    
    function onSeachClick() {
    	store.baseParams={
    		start: 0,
    		limit: pageSize,	
    		nmName: $('#searchEntity').val(),
    		deviceType: $('#deviceType').val()
    	}
    	store.load();
    }
    function exportFile(){
    	exportExcel(grid,'@resources/WorkBench.deviceAlert@');
    }
    
    function buildDeviceTypeSelect() {
    	var head = '<td style="width: 40px;"  align="right">'
    			+ '@network/deviceType@:'
    			+ '</td>&nbsp;'
    			+ '<td style="width: 100px;">'
    			+ '<select id="deviceType" class="normalSel" style="width: 100px;">'
    			+ '<option value="-1" selected>' + '@resources/COMMON.pleaseSelect@'
    			+ '</option>';
    	var options = "";
    	var tail = '</select></td>';
    	// 获取设备类型
    	var cmtsType = EntityType.getCCMTSAndCMTSType();
    	var oltType = EntityType.getOltType();
    	var onuType = EntityType.getOnuType();
    	var entityTypes = [];
    	
    	$.ajax({
    		url : '/entity/loadSubEntityType.tv?type=' + oltType,
    		type : 'POST',
    		dateType : 'json',
    		success : function(response) {
    			entityTypes = response.entityTypes;
    		},
    		error : function(entityTypes) {
    		},
    		async : false,
    		cache : false
    	});
    	$.ajax({
    		url : '/entity/loadSubEntityType.tv?type=' + cmtsType,
    		type : 'POST',
    		dateType : 'json',
    		success : function(response) {
    			$.merge(entityTypes,response.entityTypes)
    		},
    		error : function(entityTypes) {
    		},
    		async : false,
    		cache : false
    	});
    	$.ajax({
    		url : '/entity/loadSubEntityType.tv?type=' + onuType,
    		type : 'POST',
    		dateType : 'json',
    		success : function(response) {
    			$.merge(entityTypes,response.entityTypes)
    		},
    		error : function(entityTypes) {
    		},
    		async : false,
    		cache : false
    	});
    	for ( var i = 0; i < entityTypes.length; i++) {
			options += '<option value="' + entityTypes[i].typeId + '">'
			+ entityTypes[i].displayName + '</option>';
		} 
    	return head + options + tail;
    }
    
    $(function(){
        var cm = new Ext.grid.ColumnModel({
        	columns: [
   	          {
   	              header : "@COMMON.alias@",
   	              dataIndex : 'name',
   	              sortable : true,
   	              align:'left',
   	              width : 120,
   	              renderer : nameRender
   	          }, {
   	              header : "@resources/COMMON.uplinkDevice@",
   	              dataIndex : 'uplinkDevice',
   	              sortable : true,
   	              align:'center'
   	          }, {
   	              header : "@deviceType@",
   	              dataIndex : 'displayName',
   	              sortable : true,
   	              align:'center'
   	          },{
   	              header : "@resources/WorkBench.AlertNum@",
   	              dataIndex : 'count',
   	              sortable : true,
   	              align:'center',
   	              renderer : alertRender
   	          }, {
   	              header : "@NETWORK.checkTime@",
   	              dataIndex : 'snapTime',
   	              sortable : true,
   	              align:'center'
   	          }]
        });
        
        var toolbar = [
          	    	 "@network/NETWORK.aliasAndUplinkDevice@:",
          	    	{xtype:'textfield', id:'searchEntity'},
          	    	{xtype: 'tbspacer', width: 3},
          	    	{xtype: 'tbspacer', width: 3},
          	    	buildDeviceTypeSelect(),
          	    	{xtype: 'tbspacer', width: 3},
          			{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onSeachClick},
          			'->',
          			{text: "@network/exportFile@", iconCls: 'bmenu_exportWithSub', handler:exportFile} 
          		];

        store = new Ext.data.JsonStore({
            url : '/portal/loadDeviceAlertList.tv',
            root : 'data',
            totalProperty : 'total',
            remoteSort : false,
            totalProperty : 'rowCount',
            fields : [ 'entityId','entityType','uplinkDevice',{"name":"name","mapping":'entityName'},{"name":"ip","mapping":'host'},
            			'parentId','displayName','count',
                       {"name":"snapTime","mapping":'dtStr'}]
        });

        pageBar = new Ext.PagingToolbar({
            id : 'extPagingBar',
            pageSize : pageSize,
            store : store,
            displayInfo : true,
            items : [ "-", String.format("@COMMON.pagingTip@",pageSize), '-' ]
        });

        grid = new Ext.grid.GridPanel({
        	stripeRows:true,
        	region: 'center',
        	bodyCssClass:'normalTable',
            store : store,
            border : false,
            viewConfig:{
              forceFit: true
            },
            tbar: toolbar,
            bbar : pageBar,
            cm : cm
        });
        
        store.load({
            params : {
                start : 0,
                limit : pageSize
            }
        });
        new Ext.Viewport({layout: 'border', items: [grid]});
    });
</script>
</head>
<body></body>
</Zeta:HTML>
