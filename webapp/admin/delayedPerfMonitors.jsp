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
</Zeta:Loader>
<script type="text/javascript">
$(function(){
	
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			menuDisabled : false
		},
		columns : [
	          {header: "monitorId", width: 80, sortable: true, align: 'left', dataIndex: 'monitorId'}, 
	          {header: "entityId", width: 80, sortable: true, align: 'center', dataIndex: 'identifyKey'}, 
	          {header: "category", width: 100, sortable: false, align: 'center', dataIndex: 'category'}, 
	          {header: "engineName", width: 80, sortable: true, align: 'left', dataIndex: 'engineName'}, 
	          {header: "name", width: 200, sortable: true, align: 'center', dataIndex: 'entityName'}, 
	          {header: "ip", width: 200, sortable: true, align: 'center', dataIndex: 'entityIp'}, 
	          {header: "period", width: 80, sortable: false, align: 'center', dataIndex: 'period'}, 
	          {header: "createTime", width: 150, sortable: false, align: 'left', dataIndex: 'createTime'}, 
	          {header: "lastCollectTime", width: 150, sortable: true, align: 'center', dataIndex: 'lastCollectTime'}, 
	          {header: "parentId", width: 80, sortable: true, align: 'left', dataIndex: 'parentId'}, 
	          {header: "entityCreateTime", width: 150, sortable: false, align: 'left', dataIndex: 'entityCreateTime'}
	      ]
	});
	
	store = new Ext.data.JsonStore({
	    url: '/admin/loadDelayedPerfMonitors.tv',
	    root: 'data',
	    idProperty: "monitorId",
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: [
	      'monitorId', 'identifyKey', 'category', 'period', 'createTime', 'lastCollectTime', 'entityName', 'parentId', 'entityCreateTime', 'engineName', 'entityIp']
  	});
	  
	grid = new Ext.grid.EditorGridPanel({
	    cls: 'normalTable',
		bodyCssClass : 'normalTable',	
	    region: 'center',
	    border: true,
	    totalProperty: 'rowCount',
	    enableColumnMove: true,
	    store: store,
	    enableColumnMove: true,
	    cm: cm,
	    loadMask: true,
	    margins: '0px 22px 0px 5px',
	    viewConfig: {
	      	scrollOffset: 0,
	      	forceFit: true
	    }
  	});
	
  	new Ext.Viewport({
    	layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'query-container',
	        height: 50
	      },
	      grid
	    ]
  	});
  	
  	store.load({
  		params: {
  			periodCount: $('#periodCount').val()
  		}
  	})
})
</script>
</head>
<body class="whiteToBlack">
	<div id="query-container">
		<table class="queryTable">
			<tr>
				<td>超期周期数</td>
				<td><input id="periodCount" class="normalInput w200" value="5"/></td>
				<td><a id="simple-query" href="javascript:simpleQuery();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>查询</span></a></td>
			</tr>
		</table>
	</div>
</body>
</Zeta:HTML>
