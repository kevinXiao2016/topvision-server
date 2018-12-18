<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
</Zeta:Loader>
<script type="text/javascript" src="/js/zetaframework/Validator.js"></script>
<script type="text/javascript">
var cm;
var store = null;
var grid = null;

Ext.onReady(function () {
    var w = document.body.clientWidth;
    var h = document.body.clientHeight;
    
    var columns = [
        {header: '@CM.fileName@', width: 150, sortable:false, align: 'center', dataIndex: 'fileName'},
        {header: '@CM.ServiceType@', width: 150, sortable:false, align: 'center', dataIndex: 'serviceType'},   
        {header: I18N.CHANNEL.operation, width: 150, sortable:false, align: 'center', dataIndex: 'op', renderer: renderOpeartion}
    ];
    store = new Ext.data.JsonStore({
        url: 'cm/loadCmServiceTypeList.tv',
        root: 'data',
        remoteSort: true, 
        fields: [ 'fileName', 'serviceType']
    });

    var toolbar = [ 
		{text: '@COMMON.add@', iconCls:'bmenu_new', handler: addCmServiceType},'-',
		{text: I18N.CMC.title.importExcel, iconCls: 'bmenu_inport', handler: showImportCmServiceType},'-',
		{text: '@COMMON.refresh@', iconCls: 'bmenu_refresh', handler: onRefreshClick}
	];
    grid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
        id: 'cmGrid',
        tbar: toolbar,
        store: store, 
        columns : columns,
        viewConfig: { forceFit: true }
    });
    var viewPort = new Ext.Viewport({
 	     layout: "border",
 	     items: [grid]
 	});
    
    store.load();
});

function addCmServiceType(){
	window.top.createDialog("addCmServiceType", "@CM.addServiceType@", 600, 300, "cmc/cm/addCmServiceType.jsp", null, true, true,function(){
		onRefreshClick();
	});
}

function renderOpeartion(value, cellmate, record){
    var fileName = record.data.fileName;
    return String.format('<a href="javascript:;" onclick="modifyCmServiceType(\'{0}\')">@COMMON.edit@</a> / <a href="javascript:;" onclick="onDeleteClick(\'{0}\')">@COMMON.del@</a>', fileName);
}

function modifyCmServiceType(fileName){
	window.top.createDialog('modifyCmServiceType', '@CM.modifyServiceType@', 600, 300, 'cm/showModifyCmServiceType.tv?fileName=' + fileName  , null, true, true,function(){
		onRefreshClick();
	});
}

function onDeleteClick(fileName){
    window.parent.showConfirmDlg(I18N.COMMON.tip, '@CM.confirmDelServiceType@', function(button, text) {
        if (button == "yes") {
			$.ajax({
				url: '/cm/deleteCmServiceType.tv',
				data: {
					'fileName' : fileName
				},
		  	    type: 'post',
		  	    success: function(response) {
			    	top.afterSaveOrDelete({
			   			title: '@COMMON.tip@',
			   			html: '<b class="orangeTxt">@CM.delServiceTypeSuccess@</b>'
			   		});
			    	onRefreshClick();
				}, error: function(response) {
					window.parent.showMessageDlg(I18N.COMMON.tip, '@CM.delServiceTypeFail@');
				}, cache: false
			});
        }
    });
}

function showImportCmServiceType(){
	window.top.createDialog('importCmServiceType', '@CM.importServiceType@', 800, 500, 'cm/showImportCmServiceType.tv', null, true, true,function(){
		onRefreshClick();
	});
}

function onRefreshClick(){
	store.reload();
}
</script>
</head>
<body class="whiteToBlack" ></body>
</Zeta:HTML>
