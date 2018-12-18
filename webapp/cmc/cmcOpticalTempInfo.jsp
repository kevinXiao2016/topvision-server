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
	var store,grid,pageBar;
	var pageSize = <%= uc.getPageSize() %>;
	
    // main.js的addView函数会调用
    function doRefresh(){
    	store.reload();
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
		exportExcel(grid,'@network/DEVICEVIEW.cmcOpticalTempRange@');
    }
    
    function nameRender(value, p, record){
    	return String.format("<a href='javascript:;' onclick='showCcmtsPortal({0},\"{1}\",\"{2}\")'>{2}</a>",record.data.cmcId, record.data.mac, value)
    }
    
    function showCcmtsPortal(cmcId , cmcMac ,cmcName){
        window.parent.addView('entity-' + cmcId, cmcName , 'entityTabIcon','/cmc/showCmcPortal.tv?cmcId=' + cmcId);
    }
    
    function optRender(value, p, record){
    	if(value == null || value === '' || value == 0){
    		return "--";
    	}else{
    		return value;
    	}
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
    	var deviceType = EntityType.getCcmtsType();
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
    	var toolbar = [
  	    	 "@CCMTS.searchTitle@:",
  	    	{xtype:'textfield', id:'searchEntity'},
  	    	{xtype: 'tbspacer', width: 3},
  	    	{xtype: 'tbspacer', width: 3},
  	    	buildDeviceTypeSelect(),
  	    	{xtype: 'tbspacer', width: 3},
  			{text: "@COMMON.query@", iconCls: 'bmenu_find', handler:onSeachClick},
  			'->',
  			{text: "@network/exportFile@", iconCls: 'bmenu_exportWithSub', handler:exportFile} 
  		];
        var cm = new Ext.grid.ColumnModel({
        	columns: [
  	          {
  	              header : "@COMMON.alias@",
  	              dataIndex : 'name',
  	              align:'center',
  	              sortable : true,
  	              renderer : nameRender
  	         },{
  	             header : "@CCMTS.macAddress@",
  	             dataIndex : 'mac',
  	             align:'center',
  	             sortable:true
  	         },{
  	             header : "@CCMTS.uplinkdevice@",
  	             sortable : true,
  	             dataIndex : 'uplinkDevice',
  	             align:'center'
  	         }, {
  	             header : "@resources/Common.deviceType@",
  	             dataIndex : 'typeId',
  	             align:'center',
  	             sortable : true
  	         },{
  	             header : "@resources/Common.deviceLocation@",
  	             dataIndex : 'portIndex',
  	             sortable : true,
  	             align:'center'
  	         }, {
  	             header : "@cmc/CMC.optical.dorTemp@",
  	             dataIndex : 'optTempFloat',
  	             sortable : true,
  	             align:'center',
  	             renderer : optRender,
  	             exportRenderer: optRender 
  	         }, {
  	             header : "@checkTime@",
  	             sortable : true,
  	             dataIndex : 'collectTime',
  	             align:'center'
  	         }]
        });

        store = new Ext.data.JsonStore({
            url : '/cmcperf/loadCmcOpticalTempInfo.tv',
            root : 'data',
            totalProperty : 'total',
            remoteSort : true,
            fields : [ 'cmcId', 'mac','ip','uplinkDevice','name', {"name":"typeId","mapping":'displayName'},{"name":"portIndex","mapping":'location'},
                       {"name":'optTempFloat',"mapping":'optTempFloatStr'}, {"name":"collectTime","mapping":'dtStr'}]
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
            cm: cm
        });
        store.load({
            params : {
                start : 0,
                limit : pageSize
            }
        });
        new Ext.Viewport({layout: 'fit', items: [grid]});
    });
</script>
</head>
<body></body>
</Zeta:HTML>
