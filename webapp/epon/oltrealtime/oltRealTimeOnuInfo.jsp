<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>

<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    css js/ext/ux/RowExpander
    css cmc/cmcRealtimeInfo
    import js.entityType
    import js/ext/ux/RowExpander
</Zeta:Loader>
<style type="text/css">
.txtLeftTh{text-indent: 16px;}
</style>
<script type="text/javascript">
var onuStore, onuGrid;
var entityId = ${entityId};
var onlineFlag = ${onlineFlag};
var lastRow;
var expanderTpl;
var cpes;
Ext.Ajax.timeout = 500000;

function renderStatus(value, p, record){
	if (value == 1) {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_on.png" border=0 align=absmiddle>',
			"@COMMON.online@");	
	} else {
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/fault/trap_off.png" border=0 align=absmiddle>',
			"@resources/COMMON.offline@");	
	}
}

//刷新
function refreshHandler(){
	onuStore.reload();
}

function opticalRender(value, p, record){
	var onlineStatus = record.data.operationStatus;
	if(onlineStatus != 1 || value == null || value === ''){
		return "--";
	}
	return (parseFloat(value) / 100).toFixed(2);
}

function ponRecRender(value, p, record){
	var onlineStatus = record.data.operationStatus;
	if(onlineStatus != 1 || value == null || value === ''){
		return "--";
	}
    if (parseFloat(value) == -2147483648) {
        return "--";
    }
	var decayValue = parseFloat(record.data.transOpticalPower) - parseFloat(value);
	return (parseFloat(value) / 100).toFixed(2)+" ("+ (decayValue/100).toFixed(2)+")";
}

function cmNumRender(value, p, record){
	var onlineStatus = record.data.operationStatus;
	if(onlineStatus != 1 || (!EntityType.isCcmtsType(record.data.typeId))){
		return "--";
	}
	return value;
}

Ext.onReady(function(){
	expander = new Ext.ux.grid.RowExpander({
        id : 'expander' ,
        dataIndex : 'onuName',
        enableCaching : false,
        tpl : ''
    });
	
	expanderTpl = new Ext.XTemplate(
            '<div style="margin:5px 5px 5px 10px;">',
            '<p class="flagP"><span class="flagInfo">@ONU.cpeInfo@</span></p>',
            '<table width="96%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" class="dataTable extGridPanelTable addOneTr" rules="none">',
                '<thead>',
                    '<tr>',
                    '<th align="center">@ONU.uniport@</th>',
                    '<th align="center">CPE MAC</th>',
                    '<th align="center">VLAN ID</th>',
                    '<th align="center">@ONU.onuType@</th>',
                    '</tr>',
                '</thead>',
                '<tbody id="tbody-append-child">',
                '<tpl for="cpes">',
	                '<tr>',
	                    '<td align="center">{uniNo}</td>',
	                    '<td align="center">{mac}</td>',
	                    '<td align="center">{vlan}</td>',
	                    '<td align="center">{type}</td>',
	                '</tr>',
                '</tpl>',
                '</tbody>',
            '</table>',
        '</div>'
    )
	expanderTpl.compile();
    
    expander.on("beforeexpand",function(expander, r, body, row){
        if(lastRow != null){
            expander.collapseRow(lastRow);
        }
        lastRow = row;
        var onu = onuStore.getAt(row).data;
        if(!EntityType.isOnuType(onu.onuType)){
        	return;
        }
        // 加载CPE信息
        window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.loading@");
        $.ajax({
	        url: '/epon/onucpe/loadOnuUniCpeListByIndex.tv',
	        type: 'POST',
	        data: {
	        	entityId: entityId,
	        	onuIndex: onu.onuDeviceIndex
	        },
	        dataType : "json",
	        cache : false,
	        success : function(cpes) {
	        	top.closeWaitingDlg();
	        	//遍历，进行render
	        	$.each(cpes, function(i, cpe){
	        		switch(cpe.type){
	        		case 1:
	        			cpe.type = '@ONU.static@';
	        			break;
	        		case 2:
	        			cpe.type = '@ONU.dynamic@';
                        break;
	        		}
	        		if(!cpe.vlan){
	        		    cpe.vlan = '--';
	        		}
	        	})
	           expanderTpl.overwrite(body, {
	        	   cpes: cpes
	           });
	        },
	        error : function() {
	        	top.closeWaitingDlg();
	            window.parent.showMessageDlg('@COMMON.tip@', '@ONU.loadCpeError@');
	        	expanderTpl.overwrite(body, {
	        	   cpes: []
	           });
	        },
	        complete : function(XHR, TS) {
	            XHR = null
	        }
	    });
    });
    
	var cm = new Ext.grid.ColumnModel([
        expander,
		{header:"<div class='txtCenter'>@COMMON.name@</div>", dataIndex:"onuName",width:100, align:"left"},
		{header:"<div class='txtCenter'>@EPON.type@</div>", dataIndex:"displayType",width:60, align:"left"},
		{header:"@ONU.onlineStatus@", dataIndex:"operationStatus", width:60,renderer : renderStatus},
		{header:"@REALTIME.testDistance@(m)", dataIndex:"testDistance", width:60},
		{header:"@downlink.prop.sendOptRate@(dBm)", dataIndex:"transOpticalPower", width:80, renderer : opticalRender},
		{header:"@downlink.prop.recOptRate@(dBm)", dataIndex:"recvOpticalPower", width:80, renderer : opticalRender},
		{header:"@REALTIME.ponRecvOnu@(dBm)", dataIndex:"oltRecvPower", width:120, renderer : ponRecRender},
		{header:"@REALTIME.cmNumber@", dataIndex:"cmNum", width:80, renderer : cmNumRender}
	]);//end cm;
	
	var  buildToolBar = [
    	{text: '@COMMON.refresh@',cls:'mL10', iconCls: 'bmenu_refresh', handler: refreshHandler}
    ];
	
	onuStore = new Ext.data.JsonStore({
		url : '/epon/oltRealtime/loadOltSubInfo.tv',
		fields : ['onuDeviceIndex',"onuName","onuId","cmNum","location", "onuType","displayType", "operationStatus","testDistance","recvOpticalPower","transOpticalPower","oltRecvPower","typeId"],
		baseParams : {
			entityId : entityId,
			onlineFlag : onlineFlag
		}
	});
	
	onuGrid = new Ext.grid.GridPanel({
		region: 'center',
		border: false,
		stripeRows:false,
		enableColumnMove: false,
		enableColumnResize: true,
		tbar:buildToolBar,
		plugins: expander,
		bodyCssClass: 'normalTable',
		loadMask : {
            msg :'@entitySnapPage.loading@'
        },
		store: onuStore,
		cm : cm,
		viewConfig:{
			forceFit: true
		}
		//If the Grid's view is configured with forceFit=true the autoExpandColumn is ignored;
	});
	
	var viewPort = new Ext.Viewport({
		layout: 'border',
	    items: [onuGrid]
	}); 
	onuStore.load();
	
});//end document.ready;
</script>
	</head>
	<body>
		
			
	</body>
</Zeta:HTML>
