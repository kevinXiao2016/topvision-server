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
    function nameRender(value, p, record){
    	var cmcId = record.data.parentId;
    	var mac = record.data.mac;
    	var entityId = record.data.entityId;
    	return String.format("<a href='javascript:;' onclick='showEntityPortal({0},\"{1}\",\"{2}\",{3})'>{2}</a>",cmcId, mac, value,entityId)
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
    	exportExcel(grid,'@network/NETWORK.entityDelayOut@');
    }
    
    function nameRender(value, p, record){
        var cmcId = record.data.parentId;
        var mac = record.data.mac;
        var entityId = record.data.entityId;
        return String.format("<a href='javascript:;' onclick='showEntityPortal({0},\"{1}\",\"{2}\",{3})'>{2}</a>",cmcId, mac, value,entityId)
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
    	var deviceType = EntityType.getEntityWithIpType();
    	$.ajax({
    		url : '/entity/loadSubEntityType.tv?type=' + deviceType,
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
   	              header : "@resources/COMMON.manageIp@",
   	              dataIndex : 'ip',
   	              sortable : true,
   	              align:'center'
   	          }, {
   	              header : "@network/deviceType@",
   	              dataIndex : 'typeName',
   	              sortable : true,
   	              align:'center'
   	          },{
   	              header : "@resources/WorkBench.delayOutTime@",
   	              dataIndex : 'delayOutTime',
   	              sortable : false,
   	              align:'center'
   	          }, {
   	              header : "@NETWORK.checkTime@",
   	              dataIndex : 'snapTime',
   	              sortable : true,
   	              align:'center'
   	          }]
        });

        store = new Ext.data.JsonStore({
            url : '/portal/loadDeviceDelayOutList.tv',
            root : 'data',
            totalProperty : 'total',
            remoteSort : false,
            fields : [ 'entityId','name','parentId','typeName','mac','ip','delay',
                       {"name":"snapTime","mapping":'dtStr'},'delayOutTime','delayUpdateTime']
        });

        var toolbar = [
            	    	 "@network/NETWORK.aliasAndIp@:",
            	    	{xtype:'textfield', id:'searchEntity'},
            	    	{xtype: 'tbspacer', width: 3},
            	    	{xtype: 'tbspacer', width: 3},
            	    	buildDeviceTypeSelect(),
            	    	{xtype: 'tbspacer', width: 3},
            			{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onSeachClick},
            			'->',
            			{text: "@network/exportFile@", iconCls: 'bmenu_exportWithSub', handler:exportFile} 
            		];
        
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
