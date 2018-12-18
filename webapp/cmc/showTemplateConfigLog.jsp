<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
</Zeta:Loader>
<head>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;

function buildPagingToolBar() {
	var pagingToolbar = new Ext.PagingToolbar({
		id : 'extPagingBar',
		pageSize : pageSize,
		store : dataStore,
		displayInfo : true,
		items : [ "-", String.format('@COMMON.pagingTip@', pageSize), '-' ]
	});
	return pagingToolbar;
}

function showLogDetail(configLogId){
	window.top.createDialog('showConfigLogDetail',"@CMC.GP.configLogDetail@", 800, 500,
			"/ccmtsspectrumgp/showConfigLogDetail.tv?configLogId="+configLogId, null, true, true);
}

function opeartionRender(value, cellmate, record){
	var configLogId = record.data.configLogId;
	return String.format(
			"<a href='javascript:;' onclick='showLogDetail({0})'>@CMC.GP.checkDetail@</a>", configLogId);
}

function statusRender(value, cellmate, record){
	if (value) {
		return String.format('<img alt="{0}" title="{0}" src="../images/fault/confirm.png" border=0 align=absmiddle>',
			'@CMC.GP.configComplete@');	
	} else {
		return String.format('<img alt="{0}" title="{0}" src="../images/config.gif" border=0 align=absmiddle>',
			'@CMC.GP.configing@');	
	}
}

$(function(){
	window.dataStore = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadTempConfigLogList.tv',
		root : 'data',
		totalProperty: 'rowCount',
		remoteSort : false,
		fields :  ['configLogId','tempLateId','templateName','deviceNum','userId','userName','configTime','configStatus']	
	});
	dataStore.setDefaultSort('configTime', 'DESC');

	window.colModel = new Ext.grid.ColumnModel([
       	{header: "@CMC.GP.templateName@", width: 200, sortable: false, align: 'center', dataIndex: 'templateName'},	
  		{header: "@CMC.GP.deviceNum@", width: 100, sortable: false, align: 'center', dataIndex: 'deviceNum'},
  		{header: "@CMC.GP.operator@", width: 100, sortable:true, align : 'center', dataIndex: 'userName'},
  		{header: "@CMC.GP.configStatus@", width: 120, sortable:true, align : 'center', dataIndex: 'configStatus',renderer:statusRender},
  		{header: "@CMC.GP.configTime@", width: 120, sortable: true, align: 'center', dataIndex: 'configTime'},
  		{header: "@COMMON.manu@", width: 100, sortable: false, align: 'center',renderer : opeartionRender}
   	]);
	
	window.configLogGrid = new Ext.grid.GridPanel({
		id : 'configLogGrid',
		renderTo : document.body,
		height : 400,
		width : 776,
		store : dataStore,
		colModel : colModel,
		bodyCssClass:'normalTable',
		bbar : buildPagingToolBar(),
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		viewConfig: {
	        forceFit: true
		}
	});
	new Ext.Viewport({layout: 'fit', items: [configLogGrid]});
	
	//load data from proxy
	dataStore.load({params : {start : 0,limit : pageSize}});
});

</script>
</head>
<body class="openWinBody">

</body>
</Zeta:HTML>