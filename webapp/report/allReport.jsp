<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	css reset
	library Jquery
	library ext
	library zeta
    module report
    import js/jquery/jquery.treeview
    import js.jquery.dragMiddle
</Zeta:Loader>
<style type="text/css">
html, body{
	height:100%;
	background-color: #fff;
}
#taskTree{
	height: 100%;
	width:200px;
	border: 1px solid #d0d0d0;
	margin-right:5px;
	background-color: #fff;
	overflow: scroll;
}
.panel-header{
	width:100%;
	height:20px;
	background-color: #F5F5F5;
	border-bottom: 1px solid #d7d7d7;
    color: #0165b0;
    font-weight: bold;
    line-height: 1em;
    overflow: hidden;
    padding-left: 10px;
    padding-top: 5px;
    font-size: 14px;
}
.pannelTit , .x-panel-header-text{height:25px;padding-top: 5px;}
.my-panel-header{border: 1px solid #d0d0d0;}
.x-panel-header-text{
	padding-top: 0 !important; 
	padding-left: 0 !important; 
}
#tree .linkBtn{
	white-space: nowrap;
}
</style>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.report.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>,
	taskId = '<s:property value="taskId"/>',
	queryData =  {
      start: 0,
      limit: pageSize,
      taskId : taskId || 0,
      reportId: 0
    };
	
function renderDownload(value, p, record) {
	return String.format("<a href='javascript:;' onclick='downloadFile({0})'>@report.download@</a>", record.data.instanceId);
}
$(function(){
	$("#reportTreeUl").delegate('a', 'mouseover', function(){
		var $this = $(this);
		var w = $this.offset().left + $this.outerWidth();
		if( w >= 186){
			var txt = $this.text();
			var tPos = $this.offset().top + 100;
			var o = {text:txt,display:"show",top:tPos, pLeft: 394};
			top.redTip(o);
		}
	});
	$("#reportTreeUl").delegate('a', 'mouseout', function(){
		top.redTip({display:"hide"});
	});
	
	$('#tree').bind('click', function(e){
		e.stopPropagation();
		var $target = $(e.target);
		if($target.hasClass('taskNode')){
			queryData.taskId = $target.attr('taskid');
			queryData.reportId = $target.attr('reportid');
			store.load({
			    params: queryData
			});
		}
	})
	
	loadReportWithTask();
	
	var sm = new Ext.grid.CheckboxSelectionModel({
		header : '<div class="x-grid3-hd-checker"></div>', singleSelect : false
	});
	var cm = [
		sm, 
		{header : '@report.reportName@', sortable : true, dataIndex : 'instanceTitle'}, 
		{header : '@report.mode@', sortable : true, dataIndex : 'fileTypeString'}, 
		{header : "@report.blongsTask@", sortable : true, dataIndex : 'taskName'}, 
		{header : '@report.produceTime@', sortable : true, groupable : false, dataIndex : 'createTimeString'}, 
		{header : '@report.operate@', sortable : false, groupable : false, dataIndex : 'filePath', align : 'center', renderer : renderDownload}];
	
	store = new Ext.data.JsonStore({
		url : '/report/getReportInstanceList.tv',
		root : 'data',
	    totalProperty: 'rowCount',
	    remoteSort: false,
		sm: sm,
		viewConfig:{ 
			forceFit: true 
		},
		fields : ['instanceId', 'instanceTitle', 'reportName','taskName', 'createTimeString', 'fileTypeString',  'filePath', 'reportId']
	});
	
	//store在翻页之前加上查询参数
  	store.on('beforeload', function() {
    	store.baseParams = queryData;
  	});
  	store.load({
	    params: queryData
	});
	
	grid = new Ext.grid.GridPanel({
	    headerCfg: {
	        cls: 'my-panel-header', 
            html: '<p class="pannelTit">@report.allReports@</p>'
        },
        stripeRows:true,
		store: store,
		region : 'center',
		bodyCssClass: 'normalTable',
		border : true,
		columns : cm,
		split: true,
		sm: sm,
		viewConfig:{
		  forceFit: true  
		},
		tbar : [ 
			{text : '@report.remove@', iconCls : 'bmenu_delete', handler : onDeleteClick}, '-', 
			{text : '@report.refresh@', iconCls : 'bmenu_refresh', handler : onRefreshClick} 
		],
		bbar : buildPagingToolBar()
	});
	
	function buildPagingToolBar() {
		var pagingToolbar = new Ext.PagingToolbar({
			id : 'extPagingBar',
			pageSize : pageSize,
			store : store,
			displayInfo : true,
			items : [ "-", String.format('@report.displayperpage@', pageSize), '-' ]
		});
		return pagingToolbar;
	}
	
	function onDeleteClick() {
		var sm = grid.getSelectionModel();
		if (sm != null && sm.hasSelection()) {
			var selections = sm.getSelections();
			var instanceIds = [];
			var instancePaths = [];
			for ( var i = 0; i < selections.length; i++) {
				instanceIds[i] = selections[i].data.instanceId;
				instancePaths[i] = selections[i].data.filePath;
			}
			window.top.showConfirmDlg('@report.tip@', '@report.removeReportConform@', function(type) {
				if (type == 'no') {
					return;
				}
				Ext.Ajax.request({
					url : 'deleteReport.tv',
					success : function() {
						onRefreshClick();
					},
					failure : function() {
						window.parent.showErrorDlg();
					},
					params : {
						instanceIds : instanceIds,
						instancePaths : instancePaths
					}
				});
			});
		} else {
			window.parent.showMessageDlg( '@report.tip@', '@report.plsSelectReport@');
		}
	}
	
	new Ext.Viewport({
 	     layout: "border",
 	   	 items: [{
 	        region: 'west',
 	        border: false,
 	        contentEl: 'taskTree',
 	        width: 200
 	      },
 	      grid
 	    ]
 	});
})

	/* Ext.onReady(function() {
		SelectionModel = new Ext.grid.CheckboxSelectionModel({
			header : '<div class="x-grid3-hd-checker"></div>', singleSelect : false
		});
 		cm = [SelectionModel, {
				header : '@report.reportName@',
				sortable : true,
				dataIndex : 'reportName'
			}, {
				header : '@report.mode@',
				sortable : true,
				dataIndex : 'fileTypeString'
			}, {
				header : "@report.blongsTask@",
				sortable : true,
				dataIndex : 'taskName'
			}, {
				header : '@report.produceTime@',
				sortable : true,
				groupable : false,
				dataIndex : 'createTimeString'
			}, {
				header : '@report.operate@',
				sortable : false,
				groupable : false,
				dataIndex : 'filePath',
				align : 'center',
				renderer : renderDownload
			}];
 		
		store = new Ext.data.JsonStore({
			url : 'getReportByDetail.tv',
			root : 'data',
		    totalProperty: 'rowCount',
		    remoteSort: false,
			sm:SelectionModel,
			viewConfig:{ forceFit: true },
			fields : [ 'reportName','taskName', 'createTimeString', 'fileTypeString',  'filePath', 'reportId']
		});
		  //store在翻页之前加上查询参数
		  store.on('beforeload', function() {
		    store.baseParams = queryData;
		  });
		grid = new Ext.grid.GridPanel({
			    headerCfg: {
			        cls: 'my-panel-header', 
		            html: '<p class="pannelTit">@report.allReports@</p>'
		        },
		        stripeRows:true,
				store: store,
				region : 'center',
				bodyCssClass: 'normalTable',
				border : true,
				columns : cm,
				split: true,
				sm:SelectionModel,
				viewConfig:{
				  forceFit: true  
				},
				tbar : [ {
					text : '@report.remove@',
					iconCls : 'bmenu_delete',
					handler : onDeleteClick
				}, '-', {
					text : '@report.refresh@',
					iconCls : 'bmenu_refresh',
					handler : onRefreshClick
				} ],
				bbar : buildPagingToolBar(),
				loadMask : true
			});
		//组装queryData
		queryData = {
          queryModel: 'advance',
          start: 0,
          limit: pageSize,
          nodeId : taskId
        };
		store.load({
		    params: queryData
		});
		
		var treeLoader = new Ext.tree.TreeLoader({
			dataUrl : '/report/loadReportTaskTreeData.tv',
			listeners:{
            	load  : function () {
            	    //临时做法，将任务的节点的图标换成G1
            	    var images = $('.x-tree-node-leaf .x-tree-node-icon');
            	    images.each(function(){
            	        $(this).attr("src", "/css/treeview/icoG1.png");
            	    });
            	    
            		if(taskId != ""){
	            		templateTree.getNodeById(taskId).select();
            		}
            		var node1 = new Ext.tree.TreeNode({text:'@report.otherReport@',id:-1,leaf:true,iconCls:'reportTaskIcon'});   
            		templateTree.getRootNode().appendChild(node1); 
            	}
			} 
		});
		
	    templateTree = new Ext.tree.TreePanel({
	        region : 'west',
	        width : 200,
			autoScroll: true,
	        border:true,
	        split: true,
	        headerCfg: {
	            cls: 'my-panel-header', 
	            html: '<p class="pannelTit">@report.reportTaskTree@</p>'
	        }, 
            loader : treeLoader ,
            listeners : {
            	'click' : function(node,e){
            	    queryData = {
            	            queryModel: 'advance',
            	            start: 0,
            	            limit: pageSize,
            	            nodeId : node.id
            	          };
           			store.load({
           				params : {start : 0,limit : pageSize, nodeId : node.id}
           			});
            	}
            } 
		});
	    
		var root = new Ext.tree.AsyncTreeNode({
			text : '@report.reportTaskList@',
			draggable : false,
			id : 'source'
		});
		
		templateTree.setRootNode(root);
		root.expand();
		
		var viewPort = new Ext.Viewport({
	  	     layout: "border",
	  	   	 items: [grid , templateTree]
	  	});
		templateTree.render();
	}); */

function loadReportWithTask(){
	$.get('/report/loadReportWithTask.tv', function(reportJson){
		for(var key in reportJson){
			if(key==='noTaskReport'){
				//任务外报表
				$('#reportTreeUl').append(String.format('<li><span class="icoG1"><a href="javascript:;" class="linkBtn taskNode" reportid="0" taskid="-1">{0}</a></span></li>', reportJson[key].reportName, key));
			}else{
				$('#reportTreeUl').append(String.format('<li><span class="icoG1"><a href="javascript:;" class="linkBtn taskNode" reportid="{1}" taskid="0">{0}</a></span><ul id="{1}"></ul></li>', reportJson[key].reportName, key));
				var tasks = reportJson[key].childrens;
				for(var i=0; i<tasks.length; i++){
					$('#'+key).append(String.format('<li><span class="icoG1"><a href="javascript:;" class="linkBtn taskNode" taskid="{0}" reportid="{2}">{1}</a></span></li>', tasks[i].taskId, tasks[i].taskName, key));
				}
			}
		}
		$("#tree").treeview();
	});
}

function downloadFile(instanceId){
	$.ajax({
		url: '/report/isFileExist.tv',
		type: 'POST',
    	data: {instanceId: instanceId},
    	dataType:"json",
   		success: function(json) {
   			if(json.fileExist){
   				window.location.href="/report/downloadReport.tv?instanceId="+instanceId;
   			}else{
   				window.parent.showMessageDlg(I18N.report.tip, I18N.report.filenotexist);
   			}
   		}, error: function() {
   			window.parent.showErrorDlg();
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

function onRefreshClick() {
	//在执行完相关操作后去掉grid表头上的复选框选中状态
	grid.getEl().select('div.x-grid3-hd-checker').removeClass('x-grid3-hd-checker-on');
	store.reload();
} 
	
function doRefresh() {
	onRefreshClick();
}


	
</script>
</head>
<body>
	<div id="taskTree">
		<div style="width:100%; overflow:hidden;">
			<div class="panel-header">@report.taskTree@</div>
			<div class="panel-body">
				<ul id="tree" class="filetree">
					<li>
						<span class="folder"><a href="javascript:;" class="linkBtn taskNode" reportid="0" taskid="0">@report.taskList@</a></span>
						<ul id="reportTreeUl"></ul>
					</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>