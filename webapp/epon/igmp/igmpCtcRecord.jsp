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
</Zeta:Loader>
<style type="text/css">
.frameBody .x-panel-body{ background:transparent;}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = parent.entityId;
var cm,store,grid;

var reqtypeArr = ["unknown", "join", "leave"];
var authArr = ["unkown", "deny", "permit", "preview"];
var resultArr = ["unknown", "success", "failure"];
var leaveTypeArr = ["unkown", "selfleaving", "forceleaving"];

//构建分页工具栏
function buildPagingToolBar() {
   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store: store,
       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
   return pagingToolbar;
}

function refreshCtcRecord(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url : '/epon/igmpconfig/refreshCtcRecord.tv',
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

function reqTypeRender(value, meta, record){
	if(value != null){
		return reqtypeArr[value];
	}else{
		return "--";
	}
}

function authRender(value, meta, record){
	if(value != null){
		return authArr[value];
	}else{
		return "--";
	}
}

function resultRender(value, meta, record){
	if(value != null){
		return resultArr[value];
	}else{
		return "--";
	}
}

function leaveTypeRender(value, meta, record){
	//消息类型为join时不处理离开方式
	var reqType = record.data.cdrReqType;
	if(reqType == 2 && value != null){
		return leaveTypeArr[value];
	}else{
		return "--";
	}
}

$(function(){
	cm = new Ext.grid.ColumnModel({
		columns: [
		    {header: '@igmp.cdrSequence@', dataIndex: 'cdrSequence', width:50},
		    {header: '@igmp.cdrSlot@', dataIndex: 'cdrSlot', width:50},
		    {header: '@igmp.cdrPon@', dataIndex: 'cdrPon', width:55},
		    {header: '@igmp.cdrOnu@', dataIndex: 'cdrOnu', width:50},
		    {header: '@igmp.cdrUni@', dataIndex: 'cdrUni', width:60},
		    {header: '@igmp.cdrReqType@', dataIndex: 'cdrReqType', renderer :　reqTypeRender, width:70},
		    {header: '@igmp.reqTimeStr@', dataIndex: 'reqTimeStr', width: 140},
		    {header: '@igmp.cdrReqGrpId@', dataIndex: 'cdrReqGrpId', width:66},
		    {header: '@igmp.cdrGrpAuth@', dataIndex: 'cdrGrpAuth', renderer :　authRender, width:66},
		    {header: '@igmp.cdrReqResult@', dataIndex: 'cdrReqResult', renderer :　resultRender},
		    {header: '@igmp.cdrLeaveType@', dataIndex: 'cdrLeaveType', renderer :　leaveTypeRender},
		    {header: '@igmp.recordTimeStr@', dataIndex: 'recordTimeStr', width: 160}
		]
	});
	store = new Ext.data.JsonStore({
		url : '/epon/igmpconfig/loadCtcRecordList.tv',
		fields : ["entityId", "cdrSequence", "cdrType","cdrSlot","cdrPon","cdrOnu","cdrUni","cdrReqType","cdrReqTime","cdrReqGrpId","cdrGrpAuth","cdrReqResult","cdrLeaveType","cdrRecordTime","reqTimeStr","recordTimeStr"],
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
		region : 'center',
		tbar   : [{
			xtype : 'button',
			text : '@igmp.fetchCtcRecord@',
			iconCls : 'bmenu_equipment',
			handler : refreshCtcRecord
		}],
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