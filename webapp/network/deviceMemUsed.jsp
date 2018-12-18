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
    
    function ipRender(value, p, record){
    	if(value != null && value !=""){
    		return value;
    	}else{
    		return "--"
    	}
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
    	exportExcel(grid,'@NETWORK.memUsed@');
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
    	for ( var i = 0; i < entityTypes.length; i++) {
			options += '<option value="' + entityTypes[i].typeId + '">'
			+ entityTypes[i].displayName + '</option>';
		} 
    	return head + options + tail;
    }
    
    function makePercentBar(v,c,r){
    	var color;
    	if(parseInt(v*100)<85)
    		color = "#14a600";
    	else
    		color = "red";
    	var value = parseInt(v*100,10);
    	var left = -50
    	var str = '';
    	str += '<div class="percentBarBg">';
    	if(color == "red"){
    		str +=     '<div class="percentBarRed" style="width:'+ parseInt(value*0.91,10) +'px;">';
    		str +=     '<div class="percentBarLeftConnerRed"></div>';
    	}else{
    		str +=     '<div class="percentBar" style="width:'+ parseInt(value*0.91,10) +'px;">';
    		str +=     '<div class="percentBarLeftConner"></div>';
    	}		
    	str += '</div>';
    	str += '<div class="percentBarTxt">'+ numberToPic(value) +'</div></div>';
    	return str;
    }
    
    function numberToPic(str){
    	str = str.toString();
    	var newStr = '',	
    	    afterDot = false;
    	
    	if(str.length < 0) return;
    	newStr += '<div class="noWidthCenterOuter clearBoth"><ol class="noWidthCenter leftFloatOl pT1">';
    	for(var i=0; i<str.length; i++){
    		var cls = "percentNum" + str.substr(i,1);
    		if(cls === "percentNum."){
    			cls = "percentNumDot";
    		}
    		if(afterDot){
    			cls = "miniPercentNum" + str.substr(i,1);
    		}
   			newStr += '<li><div class="' + cls + '"></div></li>';
   			if(cls === "percentNumDot"){
   				afterDot = true;
   			}
    	}
    	newStr += '<li><div class="percentNumPercent"></div><li></ol></div>';
    	return newStr;
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
   	              align:'center',
   	              renderer : ipRender
   	          }, {
   	              header : "@network/deviceType@",
   	              dataIndex : 'typeName',
   	              sortable : true,
   	              align:'center'
   	          },{
   	              header : "@NETWORK.memUsage@",
   	              dataIndex : 'mem',
   	              sortable : true,
   	              align:'center',
   	              renderer: makePercentBar,
   	              exportRenderer: top.renderPercent
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
            url : '/portal/loadDeviceMemUsedList.tv',
            root : 'data',
            totalProperty : 'total',
            remoteSort : false,
            fields : [ 'entityId','uplinkDevice','name','parentId','typeName','ip','mem',
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