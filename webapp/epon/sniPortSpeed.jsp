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
    module cmc
    import /js/entityType
    import /js/topN
</Zeta:Loader>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var cm, store,grid,pageBar,sm;
    // main.js的addView函数会调用
    function doRefresh(){
        window.self._refreshNoiseRateList();
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
    
    function nameRender(value, p, record){
    	return String.format("<a href='javascript:;' onclick='showOltEntityPortal(\"{0}\",\"{1}\")'>{0}</a>", record.data.name,record.data.entityId)
    }
    
    function showOltEntityPortal(name,entityId){
        window.top.addView("entity-" + entityId,name,'entityTabIcon',
                "/portal/showEntitySnapJsp.tv?module=1&entityId=" + entityId);
    }
    
    function exportFile(){
    	exportExcel(grid,'@network/NETWORK.sniPortSpeed@');
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
    	var deviceType = EntityType.getOltType();
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
    
    $(function() {
        function _load() {
            store.load({
                params : {
                    start : 0,
                    limit : pageSize
                }
            });
        }
        
        window.self._refreshNoiseRateList=function(){
            _load();
        }
        //---------------------------------------------------------
        //sm=new Ext.grid.CheckboxSelectionModel();
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
        cm = new Ext.grid.ColumnModel({
        	columns: [
   	          //sm,
   	          {
   	             header : "@network/NETWORK.deviceAlias@",
   	             dataIndex : 'name',
   	             align:'center',
   	             sortable:true,
   	             renderer:nameRender
   	         },{
   	             header : "@network/NETWORK.manageIp@",
   	             sortable : true,
   	             dataIndex : 'ip',
   	             align:'center'
   	         },{
   	             header : "@portName@",
   	             dataIndex : 'portIndex',
   	             align:'center',
   	             sortable:true
   	         }, {
   	             header : "@network/NETWORK.inSpeed@",
   	             sortable : true,
   	             dataIndex : 'portInSpeed',
   	             align:'center',
   	             sortable:true
   	         }, {
   	             header : "@network/NETWORK.outSpeed@",
   	             sortable : true,
   	             dataIndex : 'portOutSpeed',
   	             align:'center',
   	             sortable:true
   	         },{
   	             header : "@checkTime@",
   	             align:'center',
   	             dataIndex : 'collectTime',
   	             sortable:true
   	         }]
        });

        store = new Ext.data.JsonStore({
            url : '/epon/perf/loadSniSpeedList.tv',
            root : 'data',
            totalProperty : 'rowCount',
            remoteSort : true,
            fields : [ 'entityId','ip', 'name', {"name":"portIndex","mapping":'portName'}, {"name":"portInSpeed","mapping":'inSpeed'},
                       {"name":"portOutSpeed","mapping":'outSpeed'},{"name":"collectTime","mapping":'dtStr'}]
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
        	bodyCssClass:'normalTable',
        	region : 'center',
            store : store,
            border: false,
            viewConfig:{
              forceFit : true
            }, 
            tbar: toolbar,
            bbar : pageBar,
            cm : cm
        });
        store.load({params:{start:0, limit: pageSize}});
        new Ext.Viewport({layout: 'fit', items: [grid]});
        window.self._refreshNoiseRateList();
    });
</script>
</head>
<body></body>
</Zeta:HTML>
