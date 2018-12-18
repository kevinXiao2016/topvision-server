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
var configLogId = '<s:property value="configLogId"/>';
	
//close the dialog
function cancelClick() {
	window.parent.closeWindow('showConfigLogDetail');
}

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

function resultRender(value, cellmate, record){
	if (value) {
		return String.format('<img alt="{0}" title="{0}" src="../images/fault/confirm.png" border=0 align=absmiddle>',
			'@CMC.GP.configSuccess@');	
	} else {
		return String.format('<img alt="{0}" title="{0}" src="../images/fault/unconfirm.png" border=0 align=absmiddle>',
			'@CMC.GP.configFailed@');	
	}
}

$(function(){
	window.dataStore = new Ext.data.JsonStore({
		url : '/ccmtsspectrumgp/loadLogDetailList.tv',
		root : 'data',
		totalProperty: 'rowCount',
		baseParams : {
			configLogId:configLogId
		},
		remoteSort : false,
		fields :  ['configDetailId','configLogId','templateName','configUnit','configOperation','configTime','configResult']	
	});
	dataStore.setDefaultSort('configTime', 'DESC');

	window.colModel = new Ext.grid.ColumnModel([
       	{header: "@CMC.GP.templateName@", width: 120, sortable: false, align: 'center', dataIndex: 'templateName'},	
  		{header: "@CMC.GP.configUnit@", width: 100, sortable: false, align: 'center', dataIndex: 'configUnit'},
  		{header: "@CMC.GP.configOperation@", width: 200, sortable:true, align : 'center', dataIndex: 'configOperation'},
  		{header: "@CMC.GP.configResult@", width:60, sortable:true, align : 'center', dataIndex: 'configResult',renderer:resultRender},
  		{header: "@CMC.GP.configTime@", width: 130, sortable: true, align: 'center', dataIndex: 'configTime'}
   	]);
	
	window.logDetailGrid = new Ext.grid.GridPanel({
		id : 'logDetailGrid',
		renderTo : 'detailGrid',
		height : 390,
		width : 768,
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
	
	//load data from proxy
	dataStore.load({params : {start : 0,limit : pageSize}});
});

</script>
</head>
<body class="openWinBody">
	<div id="detailGrid" style="margin:15px"></div>
	
	<div id="buttonPanel" align="right" style="margin:10px 15px">	
		<button class="BUTTON75" type="button" onclick="cancelClick()">
			@COMMON.close@
		</button>
	</div>
	
</body>
</Zeta:HTML>