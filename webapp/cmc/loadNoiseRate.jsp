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
    
    function exportFile(){
    	exportExcel(grid,'@network/CcmtsUpNoise@');
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
    	var deviceType = EntityType.getCCMTSAndCMTSType();
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
    
    function macRender(v,c,r){
    	if(v == null || v == ""){
    		return '--';
    	}else{
    		return v;
    	}
    }

    //start -- added by wubo  2017.01.03
    function snrHistory(channelIndex,typeId,name,cmcId){
    	if(EntityType.isCcmtsType(typeId)){
    		window.top.addView("entity-" + cmcId,name,'entityTabIcon',"/cmcPerfGraph/showCmcCurPerf.tv?module=13&cmcId="+cmcId+"&timeType=Today&perfType=noise&channelIndex="+channelIndex+"&productType="+typeId,null,true);   
    	}else{
    		window.top.addView("entity-" + cmcId,name,'entityTabIcon',"/cmts/showCmtsCurPerf.tv?module=8&cmcId="+cmcId+"&timeType=Today&perfType=noise&channelIndex="+channelIndex+"&productType="+typeId,null,true);   
    	}            	             
    }
    
    function spectrumInfo(channelIndex,typeId,name,cmcId){    
        window.top.addView("entity-" + cmcId,name,'entityTabIcon',"/cmcSpectrum/showCmcCurSpectrum.tv?module=16&cmcId="+cmcId+"&productType="+typeId,null,true);      	
    }
    
    function manuRender(value, p, record){
        var channelIndex = record.data.ifIndex;
        var cmcId = record.data.cmcId;
        var name = record.data.name; 
        var typeId = record.data.typeId;
        var spectrum = record.data.spectrum;
        if(EntityType.isCcmtsType(typeId)&&spectrum){
        	return String.format("<a href='javascript:;' onClick='snrHistory(\"{0}\",\"{1}\",\"{2}\",{3})'>@cmc/CCMTS.perfDisplay@</a> <span>&nbsp;/&nbsp;</span> <a href='javascript:;'   onClick = 'spectrumInfo(\"{0}\",\"{1}\",\"{2}\",{3})'>@spectrum@</a>",channelIndex, typeId, name,cmcId)	      	       	        	
        }else{
        	return String.format("<a href='javascript:;' onClick='snrHistory(\"{0}\",\"{1}\",\"{2}\",{3})'>@cmc/CCMTS.perfDisplay@</a> ",channelIndex, typeId, name,cmcId)
        }          
    } 
    //end -- added by wubo  2017.01.03

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
        cm = new Ext.grid.ColumnModel({
        	columns: [
   	          //sm,
   	          {
   	             header : "@CMC.label.deviceName@",
   	             dataIndex : 'name',
   	             align:'center',
   	             sortable:true,
   	             renderer : nameRender
   	         }, {
   	             header : "@CCMTS.macAddress@",
   	             dataIndex : 'topCcmtsSysMacAddr',
   	             align:'center',
   	             sortable:true,
   	             renderer: macRender
   	         },{
   	             header : "@CCMTS.uplinkdevice@",
   	             sortable : true,
   	             dataIndex : 'uplinkDevice',
   	             align:'center'
   	         }, {
   	             header : "@resources/Common.deviceType@",
   	             dataIndex : 'typeName',
   	             align:'center',
   	             sortable:true
   	         }, {
   	             header : "@portName@",
   	             dataIndex : 'channelIndex',
   	             align:'center',
   	             sortable:true
   	         }, {
   	             header : "@CHANNEL.snr@",
   	             dataIndex : 'collectValue',
   	             align:'center',
   	             sortable:true
   	         }, {
   	             header : "@checkTime@",
   	             align:'center',
   	             dataIndex : 'collectTime',
   	             sortable:true
   	         }, {
                 header : "@CHANNEL.location@",
                 align:'center',
                 dataIndex : 'location',
                 sortable:true
             }, {
                 header : "@CHANNEL.note@",
                 align:'center',
                 dataIndex : 'note',
                 sortable:true
             }, { //added by wubo   2017.01.03
   	             header : "@COMMON.manu@",
   	             exportable: false,
   	             dataIndex : 'operation',
   	             align:'center',
   	             sortable:false,
   	             renderer : manuRender
   	             } 
   	         ]
        });

        store = new Ext.data.JsonStore({
            url : '/cmcperf/loadNoiseRate.tv',
            root : 'data',
            totalProperty : 'rowCount',
            remoteSort : true,
            fields : [ 'cmcId','parentId','cmcType','uplinkDevice','cmcPortId' ,'ip',{"name":"topCcmtsSysMacAddr","mapping":'mac'}, 
                       'name', 'typeName', {"name":"channelIndex","mapping":'cmcPortName'}, {"name":"collectValue","mapping":'noiseString'},
                       {"name":"collectTime","mapping":'dtStr'},'typeId','ifIndex','spectrum','location','note']
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