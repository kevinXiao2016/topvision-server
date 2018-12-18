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
	css css.reportTemplate
</Zeta:Loader>
<script type="text/javascript">
$(function(){
	initStatisticGrid();
	initGrid();
	
	new Ext.Viewport({
    	layout: 'border',
	    items: [{
	        region: 'north',
	        border: false,
	        contentEl: 'table-container',
	        height: 230
	      },
	      grid
	    ]
  	});
	
	query();
})

function query() {
	statisticStore.load({
		params: {
			periodCount: $('#periodCount').val()
		}
	})
}

function initStatisticGrid() {
	statisticStore = new Ext.data.JsonStore({
		url: '/admin/loadEngineHealthInfo.tv',
	    root: 'data',
	    idProperty: "engineId",
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['engineId', 'engineName', 'monitors', 'delayedMonitors', 'delayedPercent']
    });
	
	statisticGrid = new Ext.grid.EditorGridPanel({
        title: '性能采集器运行情况统计',
        cls: 'normalTable',
		bodyCssClass : 'normalTable',	
        renderTo : "engine-table",
        store : statisticStore,
        width : '100%',
        height : 180,
        stripeRows: true,
	    viewConfig: {
	      	scrollOffset: 0,
	      	forceFit: true
	    },
	    sm: new Ext.grid.RowSelectionModel({
	        singleSelect: true
      	}),
        columns: [{
            header: '性能采集器编号',
            dataIndex: 'engineId',
            align: 'center',
            width: 170,
            sortable: true
        }, {
            header: '性能采集器名称',
            dataIndex: 'engineName',
            align: 'center',
            width: 270,
            sortable: true
        }, {
            header: '分配任务数',
            dataIndex: 'monitors',
            align: 'center',
            width: 170,
            sortable: true
        }, {
            header: '多周期未执行任务数',
            dataIndex: 'delayedMonitors',
            width: 170,
            align: 'center'
        }, {
            header: '延期率',
            dataIndex: 'delayedPercent',
            width: 170,
            align: 'center'
        }],
        style:{
            margin:"0 auto"
        },
        listeners: {
        	rowclick: function(grid, rowIndex, e) {
        		loadGridData(grid.getStore().getAt(rowIndex).data.engineId);
           	},
        }
    });
}

function initGrid() {
	var cm = new Ext.grid.ColumnModel({
		defaults : {
			menuDisabled : false
		},
		columns : [
	          {header: "性能采集器名称", width: 80, sortable: true, align: 'center', dataIndex: 'engineName'}, 
	          {header: "采集任务编号", width: 80, sortable: true, align: 'center', dataIndex: 'monitorId'}, 
	          {header: "设备编号", width: 80, sortable: true, align: 'center', dataIndex: 'identifyKey'}, 
	          {header: "设备IP", width: 100, sortable: true, align: 'center', dataIndex: 'entityIp'}, 
	          {header: "设备名称", width: 200, sortable: true, align: 'center', dataIndex: 'entityName'},
	          {header: "设备类型", width: 100, sortable: true, align: 'center', dataIndex: 'entityTypeName'},
	          {header: "设备创建时间", width: 150, sortable: false, align: 'center', dataIndex: 'entityCreateTime'},
	          {header: "采集任务类型", width: 100, sortable: false, align: 'center', dataIndex: 'category', renderer: categoryRender}, 
	          {header: "采集任务周期", width: 80, sortable: false, align: 'center', dataIndex: 'period', renderer: timeRender}, 
	          {header: "采集任务创建时间", width: 150, sortable: false, align: 'center', dataIndex: 'createTime'}, 
	          {header: "采集任务最近采集时间", width: 150, sortable: true, align: 'center', dataIndex: 'lastCollectTime'}
	      ]
	});
	
	store = new Ext.data.JsonStore({
	    url: '/admin/loadDelayedPerfMonitors.tv',
	    root: 'data',
	    idProperty: "monitorId",
	    totalProperty: 'rowCount',
	    remoteSort: true,
	    fields: ['monitorId', 'identifyKey', 'category', 'period', 'createTime', 'lastCollectTime', 'entityName', 
	             'parentId', 'entityCreateTime', 'engineName', 'entityIp', 'entityTypeName']
  	});
	
	bbar = new Ext.PagingToolbar({
 	    pageSize: 9999,
 	    store: store,
 	    displayInfo: true,
 	    items: []
 	})
	
	grid = new Ext.grid.EditorGridPanel({
		title: '未执行采集任务列表',
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
	    bbar: bbar,
	    margins: '10px',
	    sm: new Ext.grid.RowSelectionModel({
	        singleSelect: true
      	}),
	    viewConfig: {
	      	scrollOffset: 0,
	      	forceFit: true
	    }
  	});
  	
}

function loadGridData(engineId) {
	store.load({
  		params: {
  			periodCount: $('#periodCount').val(),
  			engineId: engineId
  		}
  	})
}

function timeRender(v) {
	var timeInSecond = v / 1000;
	if(timeInSecond < 60) {
		return timeInSecond + '秒';
	} else {
		return parseInt(timeInSecond/60, 10) + '分钟';
	}
}

function categoryRender(v) {
	switch(v) {
	case 'eponFlowQualityPerf':
		return 'OLT速率';
	case 'eponServiceQualityPerf':
		return 'OLT服务质量';
	case 'eponLinkQualityPerf':
		return 'OLT光链路信息';
	case 'cmcLinkQualityPerf':
		return 'CCMTS光链路信息';
	case 'cmcSignalQualityPerf':
		return 'CCMTS信号质量';
	case 'cmcServiceQualityPerf':
		return 'CCMTS服务质量';
	case 'cmcFlowQualityPerf':
		return 'CCMTS速率';
	case 'cmFlapMonitor':
		return 'CM FLAP';
	case 'cmcOpticalReceiverPerf':
		return '光机接收功率';
	case 'cmcTempQualityPerf':
		return 'CCMTS温度';
	case 'onuLinkQualityPerf':
		return 'ONU光链路信息';
	case 'onuFlowQualityPerf':
		return 'ONU速率';
	case 'onuCatvOrInfoPerf':
		return 'ONU CATV光机信息';
	case 'onuOnlinePerf':
		return '在线信息';
	case 'onlinePerf':
		return '在线信息';
	default:
		return v;
	}
}
</script>
</head>
<body class="whiteToBlack">
	<div id="table-container" style="margin:10px;">
		<div id="query-container">
			<table class="queryTable">
				<tr>
					<td>超期周期数</td>
					<td><input id="periodCount" class="normalInput w200" value="5"/></td>
					<td><a id="simple-query" href="javascript:query();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>查询</span></a></td>
				</tr>
			</table>
		</div>
		
		<div id="engine-table" style="height:200px;">
		</div>
	</div>
	
</body>
</Zeta:HTML>
