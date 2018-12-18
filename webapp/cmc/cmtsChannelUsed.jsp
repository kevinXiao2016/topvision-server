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
    
    function exportFile(){
    	exportExcel(grid,'@network/NETWORK.channelUsed@');
    }
    
    function makePercentBar(v,c,r){
    	var color;
    	if(parseInt(v)<85)
    		color = "#14a600";
    	else
    		color = "red";
    	var value = v;
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
        	columns : [
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
 	              header : "@network/channelUsed@",
 	              dataIndex : 'channelUtilization',
 	              align:'center',
 	              sortable:true,
 	              renderer: makePercentBar,
 	              exportRenderer: top.percentRendererIn100
 	          }, {
 	              header : "@checkTime@",
 	              align:'center',
 	              dataIndex : 'collectTime',
 	              sortable:true
 	          }]
        });

        store = new Ext.data.JsonStore({
            url : '/cmcperf/loadChannelUsed.tv',
            root : 'data',
            totalProperty : 'rowCount',
            remoteSort : true,
            fields : [ 'cmcId','parentId','ip','uplinkDevice', {"name":"topCcmtsSysMacAddr","mapping":'cmcMac'}, 'name', 
                       'typeName', {"name":"channelIndex","mapping":'channelName'}, {"name":"channelUtilization","mapping":'channelUsed'},
                       {"name":"collectTime","mapping":'dtStr'} ]
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
