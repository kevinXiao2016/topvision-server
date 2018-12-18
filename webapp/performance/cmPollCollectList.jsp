<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
	module  performance
</Zeta:Loader>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var completeTime = '${completeTime}';
var baseGrid;
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:baseStore,
        displayInfo: true, items: ["-", String.format("@resources/COMMON.displayPerPage@", pageSize), '-'
    ]});
    return pagingToolbar;
}

function stateRender(data, metadata, record, rowIndex, columnIndex, store) {
	var cellValue = record.data.state;
	if(cellValue == 1){
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/correct.png" border=0 align=absmiddle>',
				"@resources/COMMON.on@");	
	}else{
		return String.format('<img nm3kTip="{0}" class="nm3kTip" src="/images/wrong.png" border=0 align=absmiddle>',
				"@resources/COMMON.off@");	
	}
}

function refreshClick(){
	baseStore.load();
	refreshCompleteTime();
}
$(function(){
	Ext.QuickTips.init();
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var w = document.body.clientWidth;
    var h = document.body.clientHeight;
	var baseColumns = [
	           	{header: '<div class="txtCenter">@cmPoll.descr@</div>',  width: w * 0.15, menuDisabled: true,  align: 'left', dataIndex: 'desc'},
	           	{header: "@Tip.isOpen@", width: w * 0.1, align: 'center', menuDisabled: true, dataIndex: 'state' , renderer:stateRender},
	           	{header: "@cmPoll.executedTaskIn5m@",  width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'executedTaskIn5m'},
	           	{header: "@cmPoll.currentTaskNum@",  width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'executeTaskCounts'},
	           	{header: "@cmPoll.boundTaskNum@", width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'roundTotalTaskCounts'},
	           	{header: "@cmPoll.lastBoundTaskNum@",  width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'nextRoundTotalTaskCounts'},
           		{header: "@cmPoll.maxTaskNum@", width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'maxTaskCounts'},
	           	{header: '@cmPoll.idleTaskNum@',  width: w * 0.15, align: 'center', menuDisabled: true, dataIndex: 'idleCounts'}
	           	];
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/cmpoll/getCmPollCollectList.tv'),
	    root: 'data',
	    remoteSort: true, 
	    totalProperty: 'rowCount',
	    fields: ['engineId','desc', 'state','executeTaskCounts', 'roundTotalTaskCounts',
	             'nextRoundTotalTaskCounts','maxTaskCounts', 'idleCounts', 'executedTaskIn5m']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	
	var toolbar = [{text: '@Tip.refresh@', iconCls: 'bmenu_refresh', handler: refreshClick},
	                '-',
	               {xtype: 'tbtext', text: "@cmPoll.completeTime@@COMMON.maohao@"},
	               {xtype: 'tbtext', text:'<span id="completeTime"></span>'},
	               '->',
	               {xtype: 'tbtext', text: '<span style="float:right">@Performance.unit@@COMMON.maohao@</span>'},
		            {xtype: 'tbtext', text:'<span style="float:right">@cmPoll.taskNum@</span>'}
	       	];
	
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		loadMask: true,
		border: true, 
		store: baseStore, 
		tbar: toolbar,
		cm: baseCm,
		region:'center',
		bbar: buildPagingToolBar(),
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load();
	
	new Ext.Viewport({
	     layout: "border",
	     items: [baseGrid]
	});
	refreshCompleteTime();
})

function refreshCompleteTime(){
	$.ajax({
		  url: '/cmpoll/getCmPollCompleteTime.tv',
  	      type: 'post',
  	      success: function(response) {
  	    	if(response == 0){
  	    		response = "@cmPoll.complete@"
  	  	}
  	    	$("#completeTime").html(response);
			}, error: function(response) {
			}, cache: false
		});
}
</script>
</head>
<body class="whiteToBlack">
<div id="topPart" class="edge10" >
<div class="formtip" id="tips" style="display: none"></div>
</div>
</body>
</Zeta:HTML>