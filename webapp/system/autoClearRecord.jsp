<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module cmts
    IMPORT js/components/segmentButton/SegmentButton
</Zeta:Loader>
<style>
body, html{
	height:100%;
	overflow:hidden;
	/* min-height: 500px;
	min-width: 900px; */
}
</style>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.autoclear.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
/*  function tabActivate() {
window.top.setStatusBarInfo('', '');
}

 function description(value, cellmate, record){
return getI18NSystemLogString(value);
}

 function onSelectAllClick() {
var sm = grid.getSelectionModel();
if (sm != null) {
	sm.selectAll();
}	
} */ 

var cmciiAndOnuStore = null;
var cmciiAndOnuGrid = null;
var cmciStore = null;
var cmciGrid = null;
var pageSize = <%= uc.getPageSize() %>;

var cmciiAndOnuCm = new Ext.grid.ColumnModel([
   	{header:'<div class="txtCenter">'+ I18N.autoclear.oltName + '</div>',align:'center', dataIndex: 'oltName', width: 100, sortable: false},
   	{header:'<div class="txtCenter">'+ 'OLT IP' + '</div>', dataIndex: 'oltIp', width: 120, sortable: false},
   	{header:'<div class="txtCenter">'+ I18N.autoclear.onuName + '</div>', dataIndex: 'onuName', width: 70, sortable: false},
	{header:'<div class="txtCenter">'+ 'ONU MAC | SN' + '</div>', dataIndex: 'onuMac', width: 150, sortable: false},
	{header:'<div class="txtCenter">'+ I18N.autoclear.onuType + '</div>', dataIndex: 'onuType', width: 100, sortable: false},
	{header:'<div class="txtCenter">'+ I18N.autoclear.onuIndex + '</div>', dataIndex: 'onuIndex', width: 50, sortable: false},
	{header:'<div class="txtCenter">'+ I18N.autoclear.offlineTime + '</div>', dataIndex: 'offlineTimeString', width: 150, sortable: false},
	{header:'<div class="txtCenter">'+ I18N.autoclear.clearTime + '</div>', dataIndex: 'clearTimeString', width: 150, sortable: false}
]);

var cmciCm = new Ext.grid.ColumnModel([
	{header:'<div class="txtCenter">'+ '@cmciAlias@' + '</div>',align:'center', dataIndex: 'alias', width: 100, sortable: false},
   	{header:'<div class="txtCenter">'+ '@cmciIp@' + '</div>', dataIndex: 'ipAddress', width: 120, sortable: false},
   	{header:'<div class="txtCenter">'+ '@cmciMac@' + '</div>', dataIndex: 'macAddr', width: 70, sortable: false},
	{header:'<div class="txtCenter">'+ '@cmciType@' + '</div>', dataIndex: 'typeName', width: 150, sortable: false},
	{header:'<div class="txtCenter">'+ '@cmciClearTime@' + '</div>', dataIndex: 'clearTimeString', width: 100, sortable: false},
]);

function onRefreshClick() {
	cmciiAndOnuStore.reload();
}

function onRefreshCmciClick() {
	cmciStore.reload();
}

function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({
		id: 'extPagingBar', 
		pageSize: pageSize, 
		store:cmciiAndOnuStore, 
		displayInfo: true, 
		items: ["-", String.format(I18N.autoclear.displayPerPage, pageSize)]
	});
	return pagingToolbar;
}

function buildCmciPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({
		id: 'extCmciPagingBar', 
		pageSize: pageSize, 
		store:cmciStore, 
		displayInfo: true, 
		items: ["-", String.format(I18N.autoclear.displayPerPage, pageSize)]
	});
	return pagingToolbar;
}

Ext.onReady(function(){
	
	cmciiAndOnuStore = new Ext.data.JsonStore({
	    url: '/autoclear/loadAutoClearHistory.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    sortInfo: {field: 'clearTime', direction: "desc"},
	    fields: ['oltName', 'oltIp', 'onuName','onuMac','onuIndex', 'onuType', 'offlime', 'offlineTimeString', 'clearTime','clearTimeString']
	});
	cmciiAndOnuCm.defaultSortable = true;	
	
	cmciStore = new Ext.data.JsonStore({
		url: '/autoclear/loadAutoClearCmciHistory.tv',
	    root: 'data', 
	    totalProperty: 'rowCount',
	    remoteSort: false,
	    sortInfo: {field: 'clearTime', direction: "desc"},
	    fields: ['alias', 'ipAddress', 'macAddr','typeName', 'clearTime', 'clearTimeString', 'cmcId', 'cmcIndex', 'cmcType', 'cmcEntityID']
	});
	cmciCm.defaultSortable = true;	
	
    cmciiAndOnuGrid = new Ext.grid.GridPanel({
		border:true, 
		region:'center',
		bodyCssClass:"normalTable",
        store: cmciiAndOnuStore, 
        cm: cmciiAndOnuCm, 
        trackMouseOver: trackMouseOver, 
        stripeRows:true,
        viewConfig: {
        	forceFit:true, 
        	showPreview: false
        },
        tbar: [{
        	text: I18N.autoclear.refresh, 
        	iconCls: 'bmenu_refresh',
        	cls:'mL10', 
        	handler: onRefreshClick
        }],
		bbar: buildPagingToolBar(),
		renderTo: 'cmciiAndOnu'
    });
   	cmciiAndOnuStore.load({params: {start: 0, limit: pageSize}}); 
   	
   	cmciGrid = new Ext.grid.GridPanel({
		border:true, 
		region:'center',
		bodyCssClass:"normalTable",
        store: cmciStore, 
        cm: cmciCm, 
        trackMouseOver: trackMouseOver, 
        stripeRows:true,
        viewConfig: {
        	forceFit:true, 
        	showPreview: false
        },
        tbar: [{
        	text: I18N.autoclear.refresh, 
        	iconCls: 'bmenu_refresh',
        	cls:'mL10', 
        	handler: onRefreshCmciClick
        }],
		bbar: buildCmciPagingToolBar(),
		renderTo: 'cmci'
    });
   	cmciStore.load({params: {start: 0, limit: pageSize}});
   	 
    $(window).resize(function(){
		//重新计算grid的高度和宽度
		setTimeout(function(){
			var cmciiAndOnu_height = $('body').height() - $('#cmciiAndOnu').offset().top;
			var cmci_height = $('body').height() - $('#cmci').offset().top;
			var width = $('body').width();
			cmciiAndOnuGrid.setSize(width, cmciiAndOnu_height);
			cmciGrid.setSize(width, cmci_height);
		}, 200)
	}) 
	var data = [{
	    name: '@autoclear.cmciiAndOnuHis@',
	    value: 'cmciiAndOnu'
	},{
	    name: '@autoclear.cmciHis@',
	    value: 'cmci'
	}];
	if(!<%= uc.hasSupportModule("cmc")%>){
		data[0].name = "@autoclear.cmciiAndOnuHis2@";
		data.splice(1,1);
	}
	var tab = new SegmentButton('putTabDiv', data, {
	    callback: function(value){
	    	msg(value);
	    }
	});
	tab.init();
});

Ext.BLANK_IMAGE_URL = '../images/s.gif';

function msg(value){
    var h = $(window).height();
    var h2;
    switch(value){
    	case 'cmciiAndOnu':
    		$("#cmciiAndOnu").css("display","block");
    		$("#cmci").css("display","none");
    		var _height = $(window).height() - $('#cmciiAndOnu').offset().top;
    		cmciiAndOnuGrid.setHeight(_height);
    		break;
    	default:
    		$("#cmciiAndOnu").css("display","none");
    		$("#cmci").css("display","block");
	    	var _height = $(window).height() - $('#cmci').offset().top;
			cmciGrid.setHeight(_height);
    		break;		
    }	
}
</script>
	</head>
	<body class="whiteToBlack">
		<div class="edge10">
			<div id="putTabDiv" class="pB10 floatLeft" style="width:100%;"></div>
		</div>
		<!-- cmcii型设备和Onu设备离线自动清除记录  -->
		<div class="jsTabPart clearBoth">
			<div id="cmciiAndOnu"></div>
		</div>
		
		<!-- cmci型设备离线自动清除记录  -->
		<div class="jsTabPart clearBoth">
			<div id="cmci"></div>
		</div>
	</body>
</Zeta:HTML>