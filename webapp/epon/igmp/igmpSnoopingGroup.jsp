<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module igmpconfig
    IMPORT epon/igmp/customOltIgmp
</Zeta:Loader>
<style type="text/css">
    body,html{height:100%;}
	.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = parent.entityId;
var cm,store,grid;

function refreshGlobalGroup(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshSnpGroup.tv',
		type : 'POST',
		data : {
			entityId : entityId
		},
		dataType :　'json',
		success : function() {
			top.afterSaveOrDelete({
       	      	title: "@COMMON.tip@",
       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
       	    });
			store.reload();
		},
		error : function(json) {
			window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
		},
		cache : false
	});
}

//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store: store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}

function staticRender(value, meta, record){
	if(value == 5){
		return "static";
	}else{
		return "no static";
	}
}

$(function(){
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmp.vlanGroupId@', dataIndex: 'groupId'},
		    {header: '@igmp.vlan@', dataIndex: 'vlanId'},
		    {header: '@igmp.groupIp@', dataIndex: 'groupIp'},
		    {header: '@igmp.gourpSourceIp@', dataIndex: 'groupSrcIp'},
		    {header: '@igmp.staticJoinStatus@', dataIndex: 'groupState', renderer : staticRender}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadSnpGroupList.tv',
		fields : ["entityId", "vlanId", "groupId","groupIp","groupSrcIp", "groupState"],
		root : 'data',
		totalProperty: 'rowCount',
		baseParams : {
			entityId : entityId
		}
	});
	grid = new Ext.grid.GridPanel({
		margins : '10 10 0 10',
		cm     : cm,
		store  : store,
		tbar   : [{
			xtype : 'button',
			text : '@igmp.fetchGroup@',
			iconCls : 'bmenu_equipment',
			handler : refreshGlobalGroup
		}],
		region : 'center',
		stripeRows   : true,
		bbar : buildPagingToolBar(),
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		viewConfig   : { forceFit:true},
		bodyCssClass : 'normalTable'
	});
	
	store.load({params: {start: 0, limit: pageSize}});

	new Ext.Viewport({
	    layout : 'border',
		items  : [grid]
	});
	
});//end document.ready;

</script>
</head>
<body class="frameBody">
</body>
</Zeta:HTML>